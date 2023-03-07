package com.pmdm.virgen.pueblosconnavigationdrawer2.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.pmdm.virgen.pueblosconnavigationdrawer2.R;
import com.pmdm.virgen.pueblosconnavigationdrawer2.aplicacion.MiApp;
import com.pmdm.virgen.pueblosconnavigationdrawer2.interfaces.InterfaceApiPueblo;
import com.pmdm.virgen.pueblosconnavigationdrawer2.listener.OnUsuarioActionListener;
import com.pmdm.virgen.pueblosconnavigationdrawer2.modelos_api.LoginBodyApi;
import com.pmdm.virgen.pueblosconnavigationdrawer2.modelos_api.NuevoLoginApi;
import com.pmdm.virgen.pueblosconnavigationdrawer2.responses.RespuestaLogin;
import com.pmdm.virgen.pueblosconnavigationdrawer2.responses.RespuestaRegistro;
import com.pmdm.virgen.pueblosconnavigationdrawer2.ui.dialogos_login.RegistroUsuarioDialogo;

import java.net.HttpURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


/*
¿Cuando aceptamos los permisos de cámara?
1.- Después de registrarnos, ya que podemos hacernos una foto de perfil
2.- Después de iniciar logín.
 */
public class Login extends AppCompatActivity implements OnUsuarioActionListener {

    private ImageView logo;
    private EditText txtEmail;
    private EditText txtPass;
    private SharedPreferences shared;
    private Button btnRegistrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        inicializarCampos();  //referencia los campos
        cargaPreferenciasCompartidas();  //Recuperamos  las preferenicas compartidas.
        cerrarSesion();

        /*
        Consultamos si el usuario está logeado,
        para que no tenga que escribir el email y el login.
        Si devuelve false, es porque no existe esa  preferencia (key) el fichero.
         */
        if (islogeado()){
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
        }
    }


    /**
     * Para el almacenamiento de los datos en las preferencias.
     * Como no es un fragment, simplemente utilizamos el contexto del activity que es this,
     * Si no fuera un activity, habría que llamar al
     * getActivity.getSharedPreferences("Nombre del fichero de prefencias", Context.MODE_PRIVATE)
     * a partir del Contexto del Activity (getActivity) o por medio de nuestro Attach.
     */
    private void cargaPreferenciasCompartidas() {

        String fichPreferencias= getString(R.string.preferencias_fichero_login);
        //rescatamos el valor del string que contiene el nombre del fichero.
        shared = this.getSharedPreferences(fichPreferencias, Context.MODE_PRIVATE);
    }


    /**
     * Referencia los campos de la vista.
     */
    public void inicializarCampos(){
        logo = (ImageView) findViewById(R.id.imagenLogo);
        RequestOptions options = new RequestOptions();
        options.centerCrop();
       // String url = "https://www.diariodesevilla.es/especiales/20-lugares-imprescindibles-de-sevilla/imagenes/catedral-sevilla_1285682100_89371889_2121x1414.jpg";
       /* Glide.with(this)
              //  .load("data:image/jpeg;base64,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XUjZopIn3yyMcx1jY2eC02PVoUEOpKKSroKCncwta0Uskj7jJkha14Ddblzi1otbS5vsL2+N4bWVFNXRPp3PmdKTE/PHkMLXscxsd3Xa7K03BAub3OynFBSNhijhZmLGMaxuY3OVgDRc9egCukEap6OT+kpakxuETqOOMOuy+dsj3ltg6+zxrtcHVYPBsPrKeChhdA90TDP+IjY+PNdz3OiJu8BzOkbgHe1wbWWwUQRnghQSQmsEkfJ8pWSStsWlpY9sdrW7MpBBAUmREBERAUC40Py0n0n7KeqBcaH5aT6T9kHOiIiDYXFF89/oug49h5Lnzii+e/0XQcew8kFaIiAiIgIiIMVjWDR1TMrxYj4XD4mn+I8FrabgjOZ/w+XfXlP2Mvfv/De626i0mkTO0d8Vb92H4P4DFRsyxi7j8Tz8Tj49g7B1LMIi3b1rFY1D6iIjIiIgIiICIiAiIgIiICIiAiIgIiICgXGh+Wk+k/ZT1QLjQ/LSfSfsg50REQbC4ovnv9F0HHsPJc+cUXz3+i6Dj2HkgrREQEREBERAREQEREBERAREQEREBERAREQEREBERAREQEREBQLjQ/LSfSfsp6oFxoflpPpP2Qc6IiINhcUXz3+i6Dj2HkuW+B+Pijlzm+U226rLZ7ONaED4vYoNsItU868Pe9inOvD3vYoNrItU868Pe9inOvD3vYoNrItU868Pe9inOvD3vYoNrItU868Pe9inOvD3vYoNrItU868Pe9inOvD3vYoNrItU868Pe9inOvD3vYoNrItU868Pe9inOvD3vYoNrItU868Pe9inOvD3vYoNrItU868Pe9inOvD3vYoNrItU868Pe9inOvD3vYoNrItU868Pe9inOvD3vYoNrItU868Pe9inOvD3vYoNrItU868Pe9inOvD3vYoNrItU868Pe9inOvD3vYoNrKBcaB/4aT6T9lhudeHvexUX4ZcPWVURjjuS4W20CDWqJdEFDVWiICIiAiIgIiICIiAiIgIiICIiAiIgIiICIiAiIgIiICIiAiIgIURBQviIg//Z")
               .load("https://s3.amazonaws.com/koya-dev-videos/kindness/8da807aa-1e1e-413d-bf9b-5bb084646593/medialibrary/9456621508/videos/1eb78337-d569-41bd-95ad-153d9098de03.png")
                .apply(options)
                .into(logo);
*/
        String url = "data:image/jpeg;base64,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XUjZopIn3yyMcx1jY2eC02PVoUEOpKKSroKCncwta0Uskj7jJkha14Ddblzi1otbS5vsL2+N4bWVFNXRPp3PmdKTE/PHkMLXscxsd3Xa7K03BAub3OynFBSNhijhZmLGMaxuY3OVgDRc9egCukEap6OT+kpakxuETqOOMOuy+dsj3ltg6+zxrtcHVYPBsPrKeChhdA90TDP+IjY+PNdz3OiJu8BzOkbgHe1wbWWwUQRnghQSQmsEkfJ8pWSStsWlpY9sdrW7MpBBAUmREBERAUC40Py0n0n7KeqBcaH5aT6T9kHOiIiDYXFF89/oug49h5Lnzii+e/0XQcew8kFaIiAiIgIiIMVjWDR1TMrxYj4XD4mn+I8FrabgjOZ/w+XfXlP2Mvfv/De626i0mkTO0d8Vb92H4P4DFRsyxi7j8Tz8Tj49g7B1LMIi3b1rFY1D6iIjIiIgIiICIiAiIgIiICIiAiIgIiICgXGh+Wk+k/ZT1QLjQ/LSfSfsg50REQbC4ovnv9F0HHsPJc+cUXz3+i6Dj2HkgrREQEREBERAREQEREBERAREQEREBERAREQEREBERAREQEREBQLjQ/LSfSfsp6oFxoflpPpP2Qc6IiINhcUXz3+i6Dj2HkuW+B+Pijlzm+U226rLZ7ONaED4vYoNsItU868Pe9inOvD3vYoNrItU868Pe9inOvD3vYoNrItU868Pe9inOvD3vYoNrItU868Pe9inOvD3vYoNrItU868Pe9inOvD3vYoNrItU868Pe9inOvD3vYoNrItU868Pe9inOvD3vYoNrItU868Pe9inOvD3vYoNrItU868Pe9inOvD3vYoNrItU868Pe9inOvD3vYoNrItU868Pe9inOvD3vYoNrItU868Pe9inOvD3vYoNrItU868Pe9inOvD3vYoNrItU868Pe9inOvD3vYoNrKBcaB/4aT6T9lhudeHvexUX4ZcPWVURjjuS4W20CDWqJdEFDVWiICIiAiIgIiICIiAiIgIiICIiAiIgIiICIiAiIgIiICIiAiIgIURBQviIg//Z";
        Glide.with(this).load(url)
               // .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                .error(R.drawable.ic_baseline_camera_24)
                .apply(options)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }
                })
               .into(logo);
        //Rescatamos los campos de la vista.
        txtEmail = (EditText) findViewById(R.id.txt_email);
        txtPass = (EditText) findViewById(R.id.txt_password);
        btnRegistrar = findViewById((R.id.btn_registro));

        btnRegistrar.setOnClickListener(evento->{
            RegistroUsuarioDialogo registroUsuarioDialogo = new RegistroUsuarioDialogo(this);
            registroUsuarioDialogo.show(getSupportFragmentManager(), "Registrar nuevo Usuario");

        });


    }


    /**
     * Comprueba si el usuario ya se logeo.
     * @return
     */
    public boolean islogeado(){

       // String email = shared.getString(getString(R.string.preferencias_email),null);  //No es necesario, pero lo escribo.

        /*
        si no encuentra esa key, no se ha iniciado y por tanto es false (valor por defecto).
        Hemos rescatado el valor del string preferencias_is_logeado
        En caso de que exista, tendrá un true y por tanto ya nos logueamos.
         */
        Boolean isLogin = shared.getBoolean(getString(R.string.preferencias_is_logeado), false);
        return isLogin;
    }


    /**
     * Comprueba si los datos del logueo son correctos. En cuyo caso, debe almacenar las preferencias. Carga el activity principal.
     * Si los datos del logueo no son correctos, debe informar.
     * @param view
     */
    public void iniciarLogin(View view){
        //recuperamos los datos.
        String email = txtEmail.getText().toString();
        String pass = txtPass.getText().toString();
        txtEmail.setText("");
        txtPass.setText("");
        //******nuevo codigo
        if (!email.isEmpty() && !pass.isEmpty()){
            Retrofit retrofit = MiApp.retrofit;  //invoco a la instancia de retrofit.
            //creamos la interfaz para realizar la petición.
            InterfaceApiPueblo apiServicioPueblo = retrofit.create(InterfaceApiPueblo.class);
            LoginBodyApi loginBodyApi = new LoginBodyApi(email, pass);
            Call<RespuestaLogin> peticionLogin = apiServicioPueblo.login(loginBodyApi);  //invocamos a la llamada

            peticionLogin.enqueue(
                    new Callback<RespuestaLogin>() {
                        @Override
                        public void onResponse(Call<RespuestaLogin> call, Response<RespuestaLogin> response) {
                            /*
                            Si ha ido bien, ha habido datos, no se ha perdidio internet, etc. Vamos a sacar sus datos.
                             */
                            if (response.code() == HttpURLConnection.HTTP_CREATED){
                                String token = response.body().getToken();
                                SharedPreferences.Editor editor = shared.edit();  //obtenemos nuestro editor de preferencias.
                                editor.putString(getString(R.string.preferencias_email), email); //guardamos en las preferencias del email, el valor del email
                                editor.putString(getString(R.string.preferencias_token), token);
                                editor.putString("preferenciasAvatarUsuario", response.body().getImagen());
                                editor.putLong("preferenciasIdUsuario", response.body().getId());
                                editor.putString("preferenciasNombreUsuario", response.body().getNombre());
                                editor.putBoolean(getString(R.string.preferencias_is_logeado), true);  //guardamos en las preferencias del islogin, el valor true.
                                editor.commit(); //validamos
                                Intent i = new Intent(Login.this, MainActivity.class);  //cambiar a MainActivity.class
                                startActivity(i);
                            }else
                                Toast.makeText(Login.this, "Usuario y/o contraseña incorrecto", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(Call<RespuestaLogin> call, Throwable t) {
                            t.printStackTrace();
                           // Toast.makeText(Login.this, "Error en la llamada de petición login", Toast.LENGTH_SHORT).show();

                        }
                    } //fin onResponse
            );  //fin Callback

            /**
             * Ahora escribimos los datos en el editor de preferencias. Utilizamos nuestra variable Shared para obtener el
             * editor del fichero y a continuación, escribimos clave valor.
             */
        }//fin primer if
        else
            Toast.makeText(this, "Email y/o password incorrectos", Toast.LENGTH_SHORT).show();
    }  //fin método iniciarLogin


    @Override
    public void registrarUsuario(String email, String password, String nombre, String imagen) {
        Retrofit retrofit = MiApp.retrofit;  //invoco a la instancia de retrofit.
        InterfaceApiPueblo apiServicioPueblo = retrofit.create(InterfaceApiPueblo.class);
        Call <RespuestaRegistro> registroUsuario = apiServicioPueblo.registro(new NuevoLoginApi(email, password, nombre,
                imagen,1));
        registroUsuario.enqueue(new Callback<RespuestaRegistro>() {
            @Override
            public void onResponse(Call<RespuestaRegistro> call, Response<RespuestaRegistro> response) {
                String result = response.body().getResult();
                Toast.makeText(Login.this, "Usuario registrado correctamente, debe logearse", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(Call<RespuestaRegistro> call, Throwable t) {
                Toast.makeText(Login.this, "Error en la llamada de creación de usuario", Toast.LENGTH_SHORT).show();

            }
        });

    }

    public void cerrarSesion() {
        SharedPreferences sharedPreferences =
                getSharedPreferences(getString(R.string.preferencias_fichero_login),
                        Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
    }
}