package com.pmdm.virgen.pueblosconnavigationdrawer2.camara;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;



public class Camara {
    public static void almacenarFotoEnGaleria(Activity acti, Bitmap bitmap ){
        OutputStream fos = null;
        File f = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
            ContentValues values = new ContentValues();  //características que tendrá este archivo. JPEJ, PNG, donde se guardará. Mayor control para APIS recientes. Saber
            //cuando se ha terminado de procesar.
            ContentResolver resolver = acti.getContentResolver(); //para que procese todos estos datos.

            String fileName = System.currentTimeMillis() + "imagen_de_ejemplo";
            values.put(MediaStore.Images.Media.DISPLAY_NAME, fileName);
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
            values.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/AppPruebaCamara");
            values.put(MediaStore.Images.Media.IS_PENDING,1); //LA IMAGEN TODAVÍA SE ESTÁ PROCESANDO.

            //SE CREAN COLECCIONES CON ESTOS ARCHIVOS.
            Uri collection = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY);  //PODEMOS ALMACENAR LA FOTO TANTO EN ALMACENAMIENTO INTERNO COMO EXTERNO.
            Uri imageUri = resolver.insert(collection, values);  //ahora procesamos.

            //abrimos nuestro flujo para escritura.
            try {
                fos = resolver.openOutputStream(imageUri);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            //limpiamos todos los valores.
            values.clear();  //para añadir más imagenes.
            values.put(MediaStore.Images.Media.IS_PENDING,0);  //Decimos que YA SE TERMINÓ DE PROCESAR. 0 significa que ya se proceso
            resolver.update(imageUri, values, null, null);  //actualizamos todos estos datos.
        }else{
            //versiones menores de la Q. < API 29
            String imageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();
            String fileName = System.currentTimeMillis() + ".jpg;";

            f = new File(imageDir, fileName);

            try{
                fos = new FileOutputStream(f);
            }catch (FileNotFoundException e){
                e.printStackTrace();
            }


        }//fin else

        //ahora debemos comprimir
        boolean salvado = bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);  //sólo JPEG Damos calidad máxima con 100
        if (salvado)
            Toast.makeText(acti, "La imagen se guardó satisfactoriamente", Toast.LENGTH_SHORT).show();

        if (fos != null) { //si hay flujo abierto
            //vaciamos el buffer
            try {
                fos.flush();
                fos.close();  //cerramos
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        if (f!= null) {  //sólo debemos entrar aquí si tenemos una api más antigua.
            //Ahora buscamos nuevos directorios en la galería y poner todos estos datos de la imagen.
            MediaScannerConnection.scanFile(acti, new String[]{f.toString()}, null, null);
        }
    }


    public static Bitmap convert(String base64Str) throws IllegalArgumentException
    {
        byte[] decodedBytes = Base64.decode(
                base64Str.substring(base64Str.indexOf(",")  + 1),
                Base64.DEFAULT
        );
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

    public static String convert(Bitmap bitmap)
    {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        String str = Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT);
        return str;
    }
}
