package com.pmdm.virgen.pueblosconnavigationdrawer2.ui.dialogos_login;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.core.content.ContextCompat;


import com.pmdm.virgen.pueblosconnavigationdrawer2.R;
import com.pmdm.virgen.pueblosconnavigationdrawer2.aplicacion.MiApp;
import com.pmdm.virgen.pueblosconnavigationdrawer2.camara.Camara;
import com.pmdm.virgen.pueblosconnavigationdrawer2.camara.PermisosCamara;
import com.pmdm.virgen.pueblosconnavigationdrawer2.listener.OnUsuarioActionListener;

import java.io.IOException;

public class RegistroUsuarioDialogo extends AppCompatDialogFragment {
    private Context contexto;  //contexto del Activity.
    private OnUsuarioActionListener mListener;
    private EditText editTextEmail, editTextPassword, editTextNombre;
    private ImageView imageViewCapturaFoto, imagenViewFoto;  //imagen de la cámara y para mostrarla.
    private SharedPreferences shared;
    private ActivityResultLauncher<Intent> inicioActividadCamara;  //actividad que recogerá el resultado de la cámara.
    private Bitmap bitMap=null;  //objeto que recoge un mapa de bits devuelto por la cámara.
    private boolean permisosCamaraConcedidos=false;  //Para saber si habilitar o no el botón de captura de cámara.


    public RegistroUsuarioDialogo(Context context) {
        this.contexto = context;
        this.mListener = (OnUsuarioActionListener) context;
    }
/*
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        contexto = context;
        try{
            mListener = (OnUsuarioActionListener) context;
        }catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "Necesita implementar la interfaz OnUsuarioActionListener");
        }

    }
*/
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(contexto);  // o getActivity()
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View vista = inflater.inflate(R.layout.formulario_registro_usuario, null);
        inicializarCampos(vista);
        permisosCamaraConcedidos = compruebaPermisosCamara();  //
        crearInicioActividadCamara();
        String fichPreferencias = getString(R.string.preferencias_fichero_login);
        shared = contexto.getSharedPreferences(fichPreferencias, Context.MODE_PRIVATE);
        builder.setView(vista)
                .setTitle("Registrar nuevo usaurio")
                .setNegativeButton("Cancelar registro", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(contexto,"Se ha cancelado el registro del nuevo usuario", Toast.LENGTH_SHORT).show();
                        dialogInterface.dismiss();
                    }
                })
                .setPositiveButton("Registrar usuario", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String email = editTextEmail.getText().toString();
                        String pass =  editTextPassword.getText().toString();
                        String nombre = editTextNombre.getText().toString();
                        if (email.isEmpty() || pass.isEmpty() || nombre.isEmpty()){
                            Toast.makeText(contexto, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
                        }else{
                            //rescatamos las preferencias compartidas del usuario.
                            if (compruebaPermisosCamara()) {
                                SharedPreferences.Editor editor = shared.edit();  //obtenemos nuestro editor de preferencias.
                                editor.putBoolean(getString(R.string.preferencias_permiso_camara), true);  //guardamos en las preferencias del islogin, el valor true.
                                editor.commit();
                            }
                            String imagenBase64=null;
                            String urlImagenBase64=null;
                            if (bitMap != null){
                                imagenBase64 = Camara.convert(bitMap);   //devuelve un String
                                urlImagenBase64  = "data:image/jpeg;base64," + imagenBase64;
                            }

                            mListener.registrarUsuario(
                                    email,
                                    pass,
                                    nombre,
                                    urlImagenBase64
                            );
                        }
                        dialogInterface.dismiss();
                    }
                });
        return builder.create();
    }

    private boolean compruebaPermisosCamara() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(
                    contexto, android.Manifest.permission.CAMERA) ==
                    PackageManager.PERMISSION_GRANTED) {

                return true;

            } else if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                Toast.makeText(contexto, "Esta app necesita la camara para el perfil del usuario.", Toast.LENGTH_LONG).show();
                return false;
            } else {
                peticionPermisoCamara.launch(Manifest.permission.CAMERA);
                return false;
            }
        }else
            return true;
    }

    private void inicializarCampos(View vista) {
        editTextEmail = vista.findViewById(R.id.edit_email);
        editTextPassword = vista.findViewById(R.id.edit_password);
        editTextNombre = vista.findViewById(R.id.edit_nombre);
        imageViewCapturaFoto = vista.findViewById(R.id.imag_view_cap_foto2);
        imagenViewFoto = vista.findViewById(R.id.text_view_imagen_foto2);
        imageViewCapturaFoto.setOnClickListener(
                (e)->{
                    //se puede hacer foto y por tanto hay que realizarla.
                    Intent intentCamara = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    //Ahora lanzamos la actividad con el intent
                    inicioActividadCamara.launch(intentCamara);  //lanzamos una actividad con el intent.
                }
        );
    }

    private ActivityResultLauncher<String> peticionPermisoCamara=
            registerForActivityResult(new ActivityResultContracts.RequestPermission(),
                    result -> {
                        if (result) {
                            SharedPreferences.Editor editor = shared.edit();  //obtenemos nuestro editor de preferencias.
                            editor.putBoolean(getString(R.string.preferencias_permiso_camara), true);  //guardamos en las preferencias del islogin, el valor true.
                            editor.commit();
                            permisosCamaraConcedidos = true;
                        } else {
                            Toast.makeText(contexto, "No podrás utilizar la camara.", Toast.LENGTH_LONG).show();
                            permisosCamaraConcedidos = false;
                        }
                    }
            );


    //registramos la actividad, para que la cámara nos devuelva la captura
    private void crearInicioActividadCamara(){
        inicioActividadCamara = registerForActivityResult(
                new ActivityResultContracts
                        .StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {

                        bitMap = (Bitmap)result.getData().getExtras().get("data");
                        imagenViewFoto.setImageBitmap(bitMap);

                    }
                });
    }

}
