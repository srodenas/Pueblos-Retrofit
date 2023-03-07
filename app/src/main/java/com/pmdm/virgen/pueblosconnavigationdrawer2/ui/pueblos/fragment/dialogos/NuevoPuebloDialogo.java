package com.pmdm.virgen.pueblosconnavigationdrawer2.ui.pueblos.fragment.dialogos;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;


import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.pmdm.virgen.pueblosconnavigationdrawer2.activity.MainActivity;
import com.pmdm.virgen.pueblosconnavigationdrawer2.R;
import com.pmdm.virgen.pueblosconnavigationdrawer2.camara.Camara;
import com.pmdm.virgen.pueblosconnavigationdrawer2.listener.OnPuebloInteractionDialogListener;
import com.pmdm.virgen.pueblosconnavigationdrawer2.ui.pueblos.fragment.PuebloFragment;

import java.io.IOException;

/*
Diálogo que captura los datos de un nuevo Pueblo.
 */
public class NuevoPuebloDialogo extends AppCompatDialogFragment {
    private OnPuebloInteractionDialogListener listener;
    private MainActivity activity;
    private EditText editNombre, editDescripcion, editNHabitantes;
    private ImageView imageViewCapturaFoto, imageViewGaleria, imageViewGuardar;
    private ImageView imagenViewFoto;
    private Bitmap bitMap=null;
    SharedPreferences shared;
    //Necesitamos registrar estas dos actividades (para la camara y galería).
    //Estas actividades, lanzarán una nueva app y devolverán lo solicitado.
    //Para devolver, deberán invocar al método sobreescrito onActionResult.
    private ActivityResultLauncher<Intent> inicioCamara, inicioLecturaGaleria;


    public NuevoPuebloDialogo(PuebloFragment p){
        listener = (OnPuebloInteractionDialogListener) p;

    }

    //registramos la actividad, para que la cámara nos devuelva la captura
    private void crearInicioActividadCamara(){
        inicioCamara = registerForActivityResult(
                new ActivityResultContracts
                        .StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        bitMap = (Bitmap)result.getData().getExtras().get("data");
                        imagenViewFoto.setImageBitmap(bitMap);
                        imageViewGuardar.setEnabled(true);

                    }
                });
    }



    /*
    ES LA RESPUESTA QUE DEVUELVE EL ACTIVITY QUE LLAMA A LA APP DE LA GALERÍA.
     */
    private void crearInicioActividadLeerImagenGaleria(){
        inicioLecturaGaleria = registerForActivityResult(
                new ActivityResultContracts
                        .StartActivityForResult(),
                (result)->{  //Lo presento ahora con funcione lambda. Lo hemos trabajado.
                        if (result.getResultCode() == activity.RESULT_OK){
                            Uri imagenUri = result.getData().getData();  //recuperamos la imagen por su URI
                            imagenViewFoto.setImageURI(imagenUri);  //cargamos en el View la imagen por su URI
                            imageViewGuardar.setEnabled(false);
                            try {
                                bitMap = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), imagenUri);
                            }catch (IOException e){}
                        }
                }
        );
    }




    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (MainActivity)context;

    }

    private boolean comprobarPermisoCamara(){
        String fichPreferencias= getString(R.string.preferencias_fichero_login);
        shared = activity.getSharedPreferences(fichPreferencias, Context.MODE_PRIVATE);
        return shared.getBoolean(getString(R.string.preferencias_permiso_camara), false);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());  // o getActivity()
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View vista = inflater.inflate(R.layout.formulario_nuevo_pueblo, null);
        asociaCampos(vista);
        crearInicioActividadCamara();
        crearInicioActividadLeerImagenGaleria();
        builder.setView(vista)
                .setTitle("Nuevo Pueblo")
                .setNegativeButton("Cancelar",
                        (dialogo, i )->{
                            Toast.makeText(activity, "Se ha cancelado la inserción", Toast.LENGTH_SHORT).show();
                            dialogo.dismiss();
                        }

                )
                .setPositiveButton("Añadir",
                        (dialogo,i)->{
                            String nombre = editNombre.getText().toString();
                            String descripcion = editDescripcion.getText().toString();
                            /*
                            Aquí hay que recuperar una imagen de la galería. Abrirla y convertirla a Base64.
                            El código hay que guardarlo en url. La Respuesta después de un POST o PUT será también la url de la
                            imagen para que ésta se actualize.
                             */
                            int nHabitantes=0;
                            boolean error = false;
                            try{
                                nHabitantes = Integer.parseInt(editNHabitantes.getText().toString());
                            }catch (NumberFormatException e){  //Mas usss vale, que recordéis el tema de las excepciones.
                                e.printStackTrace();
                                error = true;
                            }
                            String urlImagenBase64=null;
                            /*
                            Debo guardar la imagen en el dispositivo
                            y generar el base64 a partir de esa imagen.
                             */
                          if (bitMap != null){
                              /*
                              Tenemos una imagen y hay que convertirla a base64 para pasarla
                               */
                                urlImagenBase64  = "data:image/jpeg;base64," + Camara.convert(bitMap);
                          }

                            if (nombre.isEmpty() || descripcion.isEmpty() || error)
                                Toast.makeText(activity, "Campos incorrectos", Toast.LENGTH_SHORT).show();
                            else
                                listener.insertarPueblo(nombre, descripcion, nHabitantes, urlImagenBase64);
                            dialogo.dismiss();
                        }
                );
        return builder.create();

    }


    public void pruebaImagenBaseBitmap(Bitmap url){
        RequestOptions options = new RequestOptions();
        options.centerCrop();
        Glide.with(activity).load(url)
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
                .into(imagenViewFoto);
    }

   /* public void pruebaImagenBase(String url){
        RequestOptions options = new RequestOptions();
        options.centerCrop();
        Glide.with(activity).load(url)
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
                .into(imagenViewFotoCopia);
}
*/
    public void asociaCampos(View vista){
        editNombre = (EditText) vista.findViewById(R.id.edit_nombre);
        editDescripcion = (EditText) vista.findViewById(R.id.edit_descripcion);
        editNHabitantes = (EditText) vista.findViewById(R.id.edit_n_habitantes);
        imageViewCapturaFoto = vista.findViewById(R.id.imag_view_cap_foto);
        imageViewGaleria = vista.findViewById(R.id.image_view_open);
        imageViewGuardar = vista.findViewById(R.id.image_view_save);
        imagenViewFoto = vista.findViewById(R.id.text_view_imagen_foto);

        //deshabilitamos o habilitamos, dependiendo del permiso de la cámara.
        imageViewCapturaFoto.setEnabled(comprobarPermisoCamara());
        imageViewGuardar.setEnabled(false);  //de momento no puedo guardar nada.


        imageViewCapturaFoto.setOnClickListener(
                (e)->{
                    //se puede hacer foto y por tanto hay que realizarla.
                    Intent intentCamara = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    //Ahora lanzamos la actividad con el intent que llama a la app de la cámara.
                    inicioCamara.launch(intentCamara);  //lanzamos una actividad con el intent.
                }
        );

        imageViewGaleria.setOnClickListener(
                (e)->{
                    Intent intentGaleria = new Intent(Intent.ACTION_GET_CONTENT);
                    intentGaleria.setType("image/*");
                    inicioLecturaGaleria.launch(intentGaleria);  //lanzamos la actividad con el intent.

                }
        );

        imageViewGuardar.setOnClickListener(
                (e) -> {
                    imageViewGuardar.setEnabled(false);  //ya puedo guardar.
                    Camara.almacenarFotoEnGaleria(activity, bitMap);

                }
        );
    }
}




