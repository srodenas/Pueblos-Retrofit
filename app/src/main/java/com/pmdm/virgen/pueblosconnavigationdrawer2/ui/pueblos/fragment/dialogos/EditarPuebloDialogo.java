package com.pmdm.virgen.pueblosconnavigationdrawer2.ui.pueblos.fragment.dialogos;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;

import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
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
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.pmdm.virgen.pueblosconnavigationdrawer2.R;
import com.pmdm.virgen.pueblosconnavigationdrawer2.camara.Camara;
import com.pmdm.virgen.pueblosconnavigationdrawer2.listener.OnPuebloInteractionDialogListener;
import com.pmdm.virgen.pueblosconnavigationdrawer2.modelos_api.PuebloApi;
import com.pmdm.virgen.pueblosconnavigationdrawer2.ui.pueblos.fragment.PuebloFragment;

import java.io.IOException;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditarPuebloDialogo#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditarPuebloDialogo extends AppCompatDialogFragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    /**
     * Son los Argumentos que pasaremos desde el Activity.
     */
    private static final String ARGUMENTO_ID_PUEBLO = PuebloApi.ARGUMENTO_ID;
    private static final String ARGUMENTO_NOMBRE_PUEBLO = PuebloApi.ARGUMENTO_NOMBRE;
    private static final String ARGUMENTO_DESCRIPCION_PUEBLO = PuebloApi.ARGUMENTO_DESCRIPCION;
    private static final String ARGUMENTO_N_HABITANTES = PuebloApi.ARGUMENTO_NUM_HABITANTES;
    private static final String ARGUMENTO_URL_IMAGEN = PuebloApi.ARGUMENTO_URL_IMAGEN;
    private Context contexto;  //contexo del Activity.

    private EditText editNombre, editDescripcion, editNHabitantes;
    private OnPuebloInteractionDialogListener listener; //Referencia la interfaz de las acciones del Dialogo.
    private Activity activity;
    private ImageView imageViewCapturaFoto, imageViewGaleria, imageViewGuardar;
    private ImageView imagenViewFoto;
    private Bitmap bitMap=null;
    SharedPreferences shared;

    private ActivityResultLauncher<Intent> inicioCamara, inicioLecturaGaleria;


    public EditarPuebloDialogo(PuebloFragment p) {
        // Required empty public constructor
        listener = (OnPuebloInteractionDialogListener) p;
    }

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


    private void crearInicioActividadLeerImagenGaleria(){
        inicioLecturaGaleria = registerForActivityResult(
                new ActivityResultContracts
                        .StartActivityForResult(),
                (result)->{

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

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     */
    // TODO: Rename and change types and number of parameters
    public static EditarPuebloDialogo newInstance( Activity acti,    PuebloFragment p, long id, String nombre,
                                                   String descripcion, int nHabitantes, String urlFoto ) {

        EditarPuebloDialogo dialogo = new EditarPuebloDialogo(p);
        dialogo.activity = acti;
        Bundle args = new Bundle();
        // Pasamos los datos del pueblo, a los argumentos del Dialog para que sean recuperados.
        args.putLong(ARGUMENTO_ID_PUEBLO, id);  //pasamos el id del pueblo
        args.putString(ARGUMENTO_NOMBRE_PUEBLO, nombre);
        args.putString(ARGUMENTO_DESCRIPCION_PUEBLO, descripcion);
        args.putInt(ARGUMENTO_N_HABITANTES, nHabitantes);
        args.putString(ARGUMENTO_URL_IMAGEN, urlFoto);

        dialogo.setArguments(args);
        return dialogo;
    }




    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        //return super.onCreateDialog(savedInstanceState);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View vista = inflater.inflate(R.layout.formulario_editar_pueblo_dialogo, null);  //inflamos la vista.
        inicializamosCampos(vista);
        crearInicioActividadCamara();
        crearInicioActividadLeerImagenGaleria();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(vista)
                .setTitle("Datos del Pueblo")
                .setNegativeButton("Cancelar",
                        (dialogo, i) ->{
                            EditarPuebloDialogo.this.dismiss();
                        }
                )
                .setPositiveButton("Guardar",
                        (dialogo, i) ->{
                            String nombre = editNombre.getText().toString();
                            String descripcion = editDescripcion.getText().toString();
                            int nHabitantes=0;
                            boolean error = false;
                            /*
                            Hay que recurrir a seleccionar una imagen de la galería
                            convertirla a base 64 y sobreescribir urlImagen.
                             */
                            try{
                                nHabitantes = Integer.parseInt(editNHabitantes.getText().toString());
                            }catch (NumberFormatException e){
                                e.printStackTrace();
                                error = true;
                            }
                            String urlImagenBase64=null;
                            /*
                           Si hemos hecho foto, volvermos a convertir. En caso contrario, tenemos
                           que conservar la que teníamos (si es que había).
                             */
                            if (bitMap != null){
                                urlImagenBase64  = "data:image/jpeg;base64," + Camara.convert(bitMap);
                            }else{
                                /*
                                Si no hemos subido ninguna otra imagen, hay que mandar la que temíamos
                                recuperamos el bitMAp de nuestro imagenViewFoto y volvemos a convertir
                                 */
                                if (EditarPuebloDialogo.ARGUMENTO_URL_IMAGEN != null){
                                    //recuperamos el bitmap de la imagen que hemos mostrado
                                    bitMap = ((BitmapDrawable)imagenViewFoto.getDrawable()).getBitmap();
                                    urlImagenBase64  = "data:image/jpeg;base64," + Camara.convert(bitMap);
                                }

                            }

                            if (nombre.isEmpty() || descripcion.isEmpty() || error)
                                Toast.makeText(activity, "Los campos no pueden estar vacíos", Toast.LENGTH_SHORT).show();
                            else{
                                //aquí tenemos el id del pueblo que queremos editar. Prácticamente no tocamos nada.
                                long id = EditarPuebloDialogo.this.getArguments().getLong(ARGUMENTO_ID_PUEBLO);

                                listener.editarPueblo(id, nombre, descripcion, nHabitantes, urlImagenBase64);
                                EditarPuebloDialogo.this.dismiss();
                            }

                        });
        return builder.create();


    }

    private boolean comprobarPermisoCamara(){
        String fichPreferencias= getString(R.string.preferencias_fichero_login);
        shared = activity.getSharedPreferences(fichPreferencias, Context.MODE_PRIVATE);
        return shared.getBoolean(getString(R.string.preferencias_permiso_camara), false);
    }

    private void inicializamosCampos(View vista) {
        editNombre = vista.findViewById(R.id.edit_nombre);
        editDescripcion = vista.findViewById(R.id.edit_descripcion);
        editNHabitantes = vista.findViewById(R.id.edit_n_habitantes);
        /*
        Seteamos los campos de la vista con los argumentos recibidos desde el Activity.
         */
        editNombre.setText(this.getArguments().getString(ARGUMENTO_NOMBRE_PUEBLO));
        editDescripcion.setText(this.getArguments().getString(ARGUMENTO_DESCRIPCION_PUEBLO));
        editNHabitantes.setText(String.valueOf(this.getArguments().getInt(ARGUMENTO_N_HABITANTES)));
        String imagen = this.getArguments().getString(ARGUMENTO_URL_IMAGEN);
        //aquí tengo que mostrar también la imagen en el cuadro de imagen.
        RequestOptions options = new RequestOptions();
        options.centerCrop();



        imageViewCapturaFoto = vista.findViewById(R.id.imag_view_cap_foto3);
        imageViewGaleria = vista.findViewById(R.id.image_view_open3);
        imageViewGuardar = vista.findViewById(R.id.image_view_save3);
        imagenViewFoto = vista.findViewById(R.id.text_view_imagen_foto3);

        Glide.with(this)
                //  .load("data:image/jpeg;base64,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XUjZopIn3yyMcx1jY2eC02PVoUEOpKKSroKCncwta0Uskj7jJkha14Ddblzi1otbS5vsL2+N4bWVFNXRPp3PmdKTE/PHkMLXscxsd3Xa7K03BAub3OynFBSNhijhZmLGMaxuY3OVgDRc9egCukEap6OT+kpakxuETqOOMOuy+dsj3ltg6+zxrtcHVYPBsPrKeChhdA90TDP+IjY+PNdz3OiJu8BzOkbgHe1wbWWwUQRnghQSQmsEkfJ8pWSStsWlpY9sdrW7MpBBAUmREBERAUC40Py0n0n7KeqBcaH5aT6T9kHOiIiDYXFF89/oug49h5Lnzii+e/0XQcew8kFaIiAiIgIiIMVjWDR1TMrxYj4XD4mn+I8FrabgjOZ/w+XfXlP2Mvfv/De626i0mkTO0d8Vb92H4P4DFRsyxi7j8Tz8Tj49g7B1LMIi3b1rFY1D6iIjIiIgIiICIiAiIgIiICIiAiIgIiICgXGh+Wk+k/ZT1QLjQ/LSfSfsg50REQbC4ovnv9F0HHsPJc+cUXz3+i6Dj2HkgrREQEREBERAREQEREBERAREQEREBERAREQEREBERAREQEREBQLjQ/LSfSfsp6oFxoflpPpP2Qc6IiINhcUXz3+i6Dj2HkuW+B+Pijlzm+U226rLZ7ONaED4vYoNsItU868Pe9inOvD3vYoNrItU868Pe9inOvD3vYoNrItU868Pe9inOvD3vYoNrItU868Pe9inOvD3vYoNrItU868Pe9inOvD3vYoNrItU868Pe9inOvD3vYoNrItU868Pe9inOvD3vYoNrItU868Pe9inOvD3vYoNrItU868Pe9inOvD3vYoNrItU868Pe9inOvD3vYoNrItU868Pe9inOvD3vYoNrItU868Pe9inOvD3vYoNrItU868Pe9inOvD3vYoNrItU868Pe9inOvD3vYoNrKBcaB/4aT6T9lhudeHvexUX4ZcPWVURjjuS4W20CDWqJdEFDVWiICIiAiIgIiICIiAiIgIiICIiAiIgIiICIiAiIgIiICIiAiIgIURBQviIg//Z")
                .load(imagen)
                .apply(options)
                .into(imagenViewFoto);
        //deshabilitamos o habilitamos, dependiendo del permiso de la cámara.
        imageViewCapturaFoto.setEnabled(comprobarPermisoCamara());
        imageViewGuardar.setEnabled(false);  //de momento no puedo guardar nada.


        imageViewCapturaFoto.setOnClickListener(
                (e)->{
                    //se puede hacer foto y por tanto hay que realizarla.
                    Intent intentCamara = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    //Ahora lanzamos la actividad con el intent
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