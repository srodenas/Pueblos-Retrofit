package com.pmdm.virgen.pueblosconnavigationdrawer2.aplicacion;

import android.app.Application;


import com.pmdm.virgen.pueblosconnavigationdrawer2.R;
import com.pmdm.virgen.pueblosconnavigationdrawer2.interfaces.Parametros;


import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/*
patron singleton Curso 2022/2023 (Santiago Rodenas Herráiz). PMDM.
Todo lo que se ejecute aquí, perdurará en la app.

 */
public class MiApp extends Application {
    public static final int RESPUESTA_PERMISO_CAMARA = 100;
    //private static final String fichPreferencias = "preferencias_fichero_login";


    /*
    public static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(Parametros.URL_BASE)
            .addConverterFactory(GsonConverterFactory.create(new GsonBuilder()
                    .excludeFieldsWithModifiers()
                    .create())
            )
            .build();
     */


    public static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(Parametros.URL_BASE)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    @Override
    public void onCreate() {
        super.onCreate();

    }

}
