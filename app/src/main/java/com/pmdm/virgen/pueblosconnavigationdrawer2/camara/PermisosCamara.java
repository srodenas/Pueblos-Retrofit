package com.pmdm.virgen.pueblosconnavigationdrawer2.camara;

import android.Manifest;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;

import com.pmdm.virgen.pueblosconnavigationdrawer2.R;

public class PermisosCamara {

    public static boolean compruebaPermisosCamara(Activity activity, int RESPUESTA_PERMISO_CAMARA,  SharedPreferences shared){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(activity, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                //Si los permisos fueron ya concedidos por el usuario, simplemente usamos la camara
                SharedPreferences.Editor editor = shared.edit();  //obtenemos nuestro editor de preferencias.
                editor.putBoolean(activity.getString(R.string.preferencias_permiso_camara), true);  //guardamos en las preferencias del islogin, el valor true.
                editor.commit();
                return true;
            }
            else {
                /*
                Solicitamos permisos al usuario y esperamos una respuesta.
                - Necesita el Activity, un array con los permisos a solicitar. En nuestro caso, lo hacemos uno a uno.
                y el código de respuesta para que al comprobarlo, sepamos de dónde venimos.
                 */
                ActivityCompat.requestPermissions(
                        activity,
                        new String[] { Manifest.permission.CAMERA },
                        RESPUESTA_PERMISO_CAMARA);
                return false;
            }
        }
        else{
            //no tenemos que pedir permisos, porque nuestro sdk es inferior a la 23.
            return true;
        }
    }


}
