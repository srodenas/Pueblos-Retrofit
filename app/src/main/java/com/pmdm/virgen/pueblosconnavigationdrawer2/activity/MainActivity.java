package com.pmdm.virgen.pueblosconnavigationdrawer2.activity;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.pmdm.virgen.pueblosconnavigationdrawer2.R;
import com.pmdm.virgen.pueblosconnavigationdrawer2.aplicacion.MiApp;
import com.pmdm.virgen.pueblosconnavigationdrawer2.databinding.ActivityMainBinding;
import com.pmdm.virgen.pueblosconnavigationdrawer2.interfaces.InterfaceApiPueblo;
import com.pmdm.virgen.pueblosconnavigationdrawer2.modelos_api.PuebloApi;
import com.pmdm.virgen.pueblosconnavigationdrawer2.responses.RespuestaListaPueblos;
import com.pmdm.virgen.pueblosconnavigationdrawer2.ui.gallery.GalleryFragment;
import com.pmdm.virgen.pueblosconnavigationdrawer2.ui.home.HomeFragment;
import com.pmdm.virgen.pueblosconnavigationdrawer2.ui.pueblos.fragment.PuebloFragment;
import com.pmdm.virgen.pueblosconnavigationdrawer2.ui.pueblos.fragment.dialogos.NuevoPuebloDialogo;
import com.pmdm.virgen.pueblosconnavigationdrawer2.ui.slideshow.SlideshowFragment;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener {

    private List<PuebloApi> listaPueblos;
    private ActivityMainBinding binding;
    private Fragment f = null;
    private String token;
    public static final String TAG="Error App";
    Context contexto;
    private static final int RESPUESTA_PERMISO_CAMARA = 100;
    private SharedPreferences shared;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);
        binding.appBarMain.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });



        /*
        DrawerLayout, es el gestor (contenedor especial) que contendrá dos tipos de contenidos:
        1.- Contenido principal. Aquí va toda la información incluída el toolbar y contenido de la pantalla principal
        2.- Contenido para el Navigation Drawer. Es la vista del NavigationDrawer (ventana deslizante) - NavigationView
         */
        DrawerLayout drawer = binding.drawerLayout;  //Gestor del Navigation Drawer
        NavigationView navigationView = binding.navView;  //Vista del navigation drawer (ventana deslizante)

        /*
        ActionBarDrawerToggle es el botón superior izquierdo, que hace que se deslize nuestra ventana deslizante.
        Tenemos que pasarle el drawer, el toolbar y dos recursos string.
        Primer parámetro es el contexto donde se ejecuta, Segundo parámetro el drawerLayout,
         */
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, binding.appBarMain.toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();  //mostramos el botón que abre/cierra el navigationView.
        navigationView.setNavigationItemSelectedListener(this);



        //rescatamos las preferencias compartidas del usuario.
        String fichPreferencias= getString(R.string.preferencias_fichero_login);
        shared = this.getSharedPreferences(fichPreferencias, Context.MODE_PRIVATE);

        token = shared.getString("preferenciasToken", null);
        TextView nombre = navigationView.getHeaderView(0).findViewById(R.id.text_nombre);
        nombre.setText(shared.getString(getString(R.string.preferencias_nombre),null));

        TextView email= navigationView.getHeaderView(0).findViewById(R.id.text_email);
        email.setText(shared.getString(getString(R.string.preferencias_email),null));

//del navigationView. seleccionamos la única cabecera que tenemos (index = 0). Recuperamos el imagView del Avatar.
        ImageView avatar = navigationView.getHeaderView(0).findViewById(R.id.image_avatar);
       // avatar.setImageResource(R.drawable.ic_face_co);
        RequestOptions options = new RequestOptions();
        options.centerCrop();
        String urlImagen = shared.getString(getString(R.string.preferencias_imagen_usuario),null);
        Glide.with(this).load(urlImagen)
                // .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                .error(R.drawable.ic_face_co)
                .apply(options)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target,
                                                boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target,
                                                   DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }
                })
                .into(avatar);

        avatar.setImageResource(R.drawable.ic_face_co);
        crearObjetosDinamicos();
        compruebaPermisosCamara();

    }

    private boolean compruebaPermisosCamara() {
        //Si versión de nuestro sdk >= que el de nuestra API 23, debemos pedir permisos y estos deben concederse.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this,
                    android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                //Si los permisos fueron ya concedidos por el usuario, simplemente registramos el permiso de la cámara
                SharedPreferences.Editor editor = shared.edit();  //obtenemos nuestro editor de preferencias.
                editor.putBoolean(getString(R.string.preferencias_permiso_camara), true);  //guardamos en las preferencias del islogin, el valor true.
                editor.commit();
                return true;
            }
            else {
                /*
                Solicitamos permisos al usuario y esperamos una respuesta.
                 */
                ActivityCompat.requestPermissions(
                        this,
                        new String[] { Manifest.permission.CAMERA },
                        RESPUESTA_PERMISO_CAMARA);
                return false;
            }
        }
        else{
            return true;
        }
    }

    /*Método que gestina la vuelta a atrás.
    Si quisiéramos que se cerrara la app con el botón atrás,
    sólo cuando estuviera en el home
     */

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }else{
            if (f instanceof HomeFragment){
                super.onBackPressed();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.nav_pueblos){
            f = new PuebloFragment(listaPueblos);
        }else if (id == R.id.nav_comentarios){
            f = new SlideshowFragment();
        }else if (id == R.id.nav_fiestas){
            f = new GalleryFragment();
        }else if (id == R.id.nav_salir){
            cerrarSesion();
        }

        if (f!= null){
            getSupportFragmentManager().beginTransaction().replace(R.id.contenedor, f).commit();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /*
    Método que selecciona la opción del menú de opciones.
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();  //seleccion el id del item

        //Hemos pulsado el botón de add
        if (id==R.id.action_add){
            if (f instanceof PuebloFragment){
                //entonces, la inserción es con un pueblo.
                NuevoPuebloDialogo dNuevo = new NuevoPuebloDialogo((PuebloFragment) f);
                dNuevo.show(getSupportFragmentManager(), "Insertar Nuevo Pueblo");
            }
        }

        return super.onOptionsItemSelected(item);
    }

    public void crearObjetosDinamicos(){
       Retrofit retrofit = MiApp.retrofit;  //invoco a la instancia de retrofit.
       /* Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Parametros.URL_BASE)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        */
        //creamos la interfaz para realizar la petición.<
        InterfaceApiPueblo apiServicioPueblo = retrofit.create(InterfaceApiPueblo.class);
        Call<RespuestaListaPueblos> peticionPueblos = apiServicioPueblo.damePueblos(token);
        peticionPueblos.enqueue(
                new Callback<RespuestaListaPueblos>() {
            @Override
            public void onResponse(Call<RespuestaListaPueblos> call, Response<RespuestaListaPueblos> response) {
                Log.i(TAG, "Hay respuesta");
                if(response.isSuccessful()) {
                    listaPueblos = response.body().getPueblos();
                    f = new PuebloFragment(listaPueblos);
                    getSupportFragmentManager().beginTransaction().add(R.id.contenedor, f).commit();

                }
            }

            @Override
            public void onFailure(Call<RespuestaListaPueblos> call, Throwable t) {
                Log.i(TAG, "No Hay respuesta");

            }
        });
    }



    /*
            - Una vez, solicitado los permisos mediante ActivityCompac.requestPermision(..),
            comprueba la respuesta que el usuario ha marcado (V) o (X)
            - Hay que comprobar el requestCode, uno a uno para verificar si viene de Cámara, almacenamiento o lectura.
         */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //Comprobamos permiso de Cámara.
        if (requestCode == RESPUESTA_PERMISO_CAMARA){
            //El usuario ha pulsado una opción de "aceptar permiso" o "Cancelar"
            if (permissions.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                //Si ha aceptado pulsado el botón de aceptar.
                //Registramos el permiso de la camara.
                //No podremos hacer cámara, si no tenemos esta preferencia.
                SharedPreferences.Editor editor = shared.edit();  //obtenemos nuestro editor de preferencias.
                editor.putBoolean(getString(R.string.preferencias_permiso_camara), true);  //guardamos en las preferencias del islogin, el valor true.
                editor.commit();
            }
            else{
                Toast.makeText(this, "No se han aceptado los permisos de la camara", Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    public void cerrarSesion(){
        SharedPreferences.Editor editor = shared.edit();
        editor.clear();
        editor.commit();
        finish();  //destruye el activity para volver al login.
    }


}