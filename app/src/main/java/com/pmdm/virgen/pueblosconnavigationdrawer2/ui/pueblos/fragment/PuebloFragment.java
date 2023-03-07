package com.pmdm.virgen.pueblosconnavigationdrawer2.ui.pueblos.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;


import com.pmdm.virgen.pueblosconnavigationdrawer2.activity.DetallePuebloActivity;
import com.pmdm.virgen.pueblosconnavigationdrawer2.activity.MainActivity;
import com.pmdm.virgen.pueblosconnavigationdrawer2.R;
import com.pmdm.virgen.pueblosconnavigationdrawer2.adapter.MyPuebloRecyclerViewAdapter;
import com.pmdm.virgen.pueblosconnavigationdrawer2.aplicacion.MiApp;
import com.pmdm.virgen.pueblosconnavigationdrawer2.interfaces.InterfaceApiPueblo;
import com.pmdm.virgen.pueblosconnavigationdrawer2.listener.OnPuebloInteractionDialogListener;
import com.pmdm.virgen.pueblosconnavigationdrawer2.listener.OnPuebloInteractionListener;
import com.pmdm.virgen.pueblosconnavigationdrawer2.modelos_api.NuevoPuebloApi;
import com.pmdm.virgen.pueblosconnavigationdrawer2.modelos_api.PuebloApi;
import com.pmdm.virgen.pueblosconnavigationdrawer2.responses.RespuestaEliminarPueblo;
import com.pmdm.virgen.pueblosconnavigationdrawer2.responses.RespuestaInsertaPueblo;
import com.pmdm.virgen.pueblosconnavigationdrawer2.responses.RespuestaListaPueblos;
import com.pmdm.virgen.pueblosconnavigationdrawer2.responses.RespuestaModificarPueblo;
import com.pmdm.virgen.pueblosconnavigationdrawer2.ui.pueblos.fragment.dialogos.DialogoConfirmacion;
import com.pmdm.virgen.pueblosconnavigationdrawer2.ui.pueblos.fragment.dialogos.EditarPuebloDialogo;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class PuebloFragment extends Fragment implements OnPuebloInteractionListener, OnPuebloInteractionDialogListener {

    private static final String TAG = "MiActivity";
    private List<PuebloApi> listaPueblos;
    private MyPuebloRecyclerViewAdapter miAdaptador;
    private MainActivity activity;
    private Context contexto;
    private String token;

    public PuebloFragment(){}

    public PuebloFragment(List<PuebloApi> lista) {
        listaPueblos = lista;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    /*
    Comprobamos si el Activity implementa de la interfaz.
     */
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.contexto = context;  //por si me hace falta el contexto del activity
        activity = (MainActivity) context;  //es la referencia al activity
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pueblos, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            miAdaptador = new MyPuebloRecyclerViewAdapter(listaPueblos, this, contexto);
            recyclerView.setAdapter(miAdaptador);
        }
        return view;
    }


    @Override
    public void onPuebloClick(PuebloApi pueblo) {
        Intent intentDetalles = new Intent(this.contexto, DetallePuebloActivity.class);
        // Pasamos el id del pueblo
        intentDetalles.putExtra(pueblo.ARGUMENTO_ID, pueblo.getId());
        startActivity(intentDetalles);
    }


    @Override
    public void onPuebloEditarClick(PuebloApi pueblo) {
        Toast.makeText(activity, "Edición: " + pueblo.toString(), Toast.LENGTH_SHORT).show();
        Log.i(TAG, "Edicion: " + pueblo.toString());
        EditarPuebloDialogo dialogoEditar =  EditarPuebloDialogo.newInstance(activity,
                this,
                pueblo.getId(),
                pueblo.getNombre(),
                pueblo.getDescripcion(),
                pueblo.getHabitantes(),
                pueblo.getImagen());

        dialogoEditar.show(activity.getSupportFragmentManager(), "Edición datos del Pueblo "+ pueblo.getNombre());
        //actualizarListadoPueblosRetrofit();
    }

    @Override


    public void onPuebloBorrarClick(PuebloApi pueblo) {
        //Toast.makeText(this, "Borrado: "+pueblo.toString(), Toast.LENGTH_SHORT).show();
        Log.i(TAG, "Borrado: "+ pueblo.toString());
        DialogoConfirmacion.newInstance(activity, this, pueblo.getId());

    }


   /*
    Actualización insertar a Realm Santiago Rodenas Herráiz.
   */
    public void insertarPueblo(String nombre, String descripcion, int nHabitantes, String urlImagen) {
        //rescatamos las preferencias compartidas del usuario.
        String fichPreferencias= getString(R.string.preferencias_fichero_login);
        SharedPreferences shared = contexto.getSharedPreferences(fichPreferencias, Context.MODE_PRIVATE);
        token = shared.getString("preferenciasToken", null);
        //SUPONEMOS QUE LA IMAGEN YA VIENE EN BASE64 DESDE EL DIALOGO DE INSERCIÓN
        Retrofit retrofit = MiApp.retrofit;

        InterfaceApiPueblo apiServicioPueblo = retrofit.create(InterfaceApiPueblo.class);
        NuevoPuebloApi nuevoPuebloApi = new NuevoPuebloApi(nombre,descripcion, nHabitantes, urlImagen);
        Call<RespuestaInsertaPueblo> peticionInsertaPueblo = apiServicioPueblo.insertaPueblo(nuevoPuebloApi, token);
        peticionInsertaPueblo.enqueue(
                new Callback<RespuestaInsertaPueblo>() {
                    @Override
                    public void onResponse(Call<RespuestaInsertaPueblo> call, Response<RespuestaInsertaPueblo> response) {
                        Log.i(TAG, "Hay respuesta");
                        if (response.isSuccessful()){
                            //actualizarListadoPueblos();
                            PuebloApi nuevoPueblo = new PuebloApi(response.body().getInsertId(), nombre, descripcion,
                                    response.body().getFile_img(), nHabitantes);
                            insertarPuebloDinamicamente(nuevoPueblo);
                            actualizarListadoPueblosDinamicamente();
                        }
                    }

                    @Override
                    public void onFailure(Call<RespuestaInsertaPueblo> call, Throwable t) {
                        Log.i(TAG, "No Hay respuesta");
                    }
                }
        );

    }

/*
    Actualización edición a Realm Santiago Rodenas Herráiz.
 */
    public void editarPueblo(long id, String nombre, String descripcion, int nHabitantes, String urlImagen){

        //rescatamos las preferencias compartidas del usuario.
        String fichPreferencias= getString(R.string.preferencias_fichero_login);
        SharedPreferences shared = contexto.getSharedPreferences(fichPreferencias, Context.MODE_PRIVATE);
        token = shared.getString("preferenciasToken", null);

        //SUPONEMOS QUE LA IMAGEN YA VIENE EN BASE64 DESDE EL DIALOGO DE MODIFICACIÓN

        Retrofit retrofit = MiApp.retrofit;
        InterfaceApiPueblo apiServicioPueblo = retrofit.create(InterfaceApiPueblo.class);
        NuevoPuebloApi modificarPuebloApi = new NuevoPuebloApi(nombre, descripcion, nHabitantes, urlImagen);
        Call<RespuestaModificarPueblo> peticionModificarPueblo = apiServicioPueblo.modificarPueblo(id, modificarPuebloApi, token);
        peticionModificarPueblo.enqueue(
                new Callback<RespuestaModificarPueblo>() {
                    @Override
                    public void onResponse(Call<RespuestaModificarPueblo> call, Response<RespuestaModificarPueblo> response) {
                        Log.i(TAG, "Hay respuesta");
                        if (response.isSuccessful()){
                            PuebloApi modificado = new PuebloApi(id, nombre, descripcion, response.body().getFile_img(), nHabitantes);
                            editarPuebloDinamicamente(modificado);
                            actualizarListadoPueblosDinamicamente();

                        }
                    }

                    @Override
                    public void onFailure(Call<RespuestaModificarPueblo> call, Throwable t) {
                        Log.i(TAG, "No Hay respuesta");

                    }
                }
        );


    }

    @Override
    public void eliminarPueblo(long id) {
        String fichPreferencias= getString(R.string.preferencias_fichero_login);
        SharedPreferences shared = contexto.getSharedPreferences(fichPreferencias, Context.MODE_PRIVATE);
        token = shared.getString("preferenciasToken", null);

        Retrofit retrofit = MiApp.retrofit;
        InterfaceApiPueblo apiServicioPueblo = retrofit.create(InterfaceApiPueblo.class);
        Call<RespuestaEliminarPueblo> peticionEliminarPueblo = apiServicioPueblo.eliminarPueblo(id, token);
        peticionEliminarPueblo.enqueue(
                new Callback<RespuestaEliminarPueblo>() {
                    @Override
                    public void onResponse(Call<RespuestaEliminarPueblo> call, Response<RespuestaEliminarPueblo> response) {
                        if (response.isSuccessful()){
                            eliminarPuebloDinamicamente(id);
                            Toast.makeText (contexto, "Se ha eliminado correctamente", Toast.LENGTH_SHORT).show();
                            actualizarListadoPueblosDinamicamente();
                        }
                    }

                    @Override
                    public void onFailure(Call<RespuestaEliminarPueblo> call, Throwable t) {
                        Log.i(TAG, "No Hay respuesta");

                    }
                }
        );

    }


    private void actualizarListadoPueblosDinamicamente(){
        miAdaptador.notifyDataSetChanged();
    }

    private void actualizarListadoPueblosRetrofit(){
        Retrofit retrofit = MiApp.retrofit;
        InterfaceApiPueblo apiServicioPueblo = retrofit.create(InterfaceApiPueblo.class);
        Call<RespuestaListaPueblos> peticionPueblos = apiServicioPueblo.damePueblos(token);
        peticionPueblos.enqueue(
                new Callback<RespuestaListaPueblos>() {
                    @Override
                    public void onResponse(Call<RespuestaListaPueblos> call, Response<RespuestaListaPueblos> response) {
                        Log.i(TAG, "Hay respuesta");
                        if(response.isSuccessful()) {
                            listaPueblos = response.body().getPueblos();
                            //falta actualizar el adaptador
                            miAdaptador.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onFailure(Call<RespuestaListaPueblos> call, Throwable t) {
                        Log.i(TAG, "No Hay respuesta");

                    }
                });

    }

    private void insertarPuebloDinamicamente(PuebloApi p){
        listaPueblos.add(p);
    }


    private void editarPuebloDinamicamente(PuebloApi mod){
        int i=0;
        int pos;
        PuebloApi aux=null;
        int tam = listaPueblos.size();
        while (aux==null && i<tam){
            if (listaPueblos.get(i).getId() == mod.getId()) {
                aux = listaPueblos.get(i);
                pos = i;
            }
            i++;
        }
        if (aux!=null) {
            aux.setNombre(mod.getNombre());
            aux.setDescripcion(mod.getDescripcion());
            aux.setImagen(mod.getImagen());
            aux.setHabitantes(mod.getHabitantes());
        }
    }


    private void eliminarPuebloDinamicamente(long id){
        int i=0;
        boolean encontrado = false;
        int tam = listaPueblos.size();
        while (!encontrado && i<tam){
            if (listaPueblos.get(i).getId()==id) {
                listaPueblos.remove(i);
                encontrado = true;
            }
            i++;
        }

    }
}