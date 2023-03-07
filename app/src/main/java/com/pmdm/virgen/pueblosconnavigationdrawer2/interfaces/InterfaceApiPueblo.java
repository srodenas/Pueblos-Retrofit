package com.pmdm.virgen.pueblosconnavigationdrawer2.interfaces;




import com.pmdm.virgen.pueblosconnavigationdrawer2.modelos_api.LoginBodyApi;
import com.pmdm.virgen.pueblosconnavigationdrawer2.modelos_api.NuevoLoginApi;
import com.pmdm.virgen.pueblosconnavigationdrawer2.modelos_api.NuevoPuebloApi;
import com.pmdm.virgen.pueblosconnavigationdrawer2.responses.RespuestaEliminarPueblo;
import com.pmdm.virgen.pueblosconnavigationdrawer2.responses.RespuestaFallo;
import com.pmdm.virgen.pueblosconnavigationdrawer2.responses.RespuestaInsertaPueblo;
import com.pmdm.virgen.pueblosconnavigationdrawer2.responses.RespuestaListaPueblos;
import com.pmdm.virgen.pueblosconnavigationdrawer2.responses.RespuestaLogin;
import com.pmdm.virgen.pueblosconnavigationdrawer2.responses.RespuestaModificarPueblo;
import com.pmdm.virgen.pueblosconnavigationdrawer2.responses.RespuestaRegistro;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface InterfaceApiPueblo {

    /*
    invoca a una URL cuyo endpoint es auth
    El método es login y le pasaremos por body un objeto de tipo LoginBodyApi
    Recibimos de respuesta es un objeto de tipo RespuestaLogin.
     */
    @POST("auth")
    Call<RespuestaLogin> login(@Body LoginBodyApi loginBodyApi);

    /*
    invoca a una URL cuyo endpoint es registro
    El método es registro y le pasamos por body un objeto LoginBodyApi
    Recibimos como respuesta, un objeto RespuestaRegistro
     */
    @POST("registro")
    Call<RespuestaRegistro> registro(@Body NuevoLoginApi nuevoLoginApi);

    /*
    invoca a una url cuyo endpoint es pueblo
    El método es damePueblos y le pasamos el id por parámetro en la url y el token en el header.
    Devuelve una lista de pueblos del tipo RespuestaListaPueblos.
     */
   @GET("pueblo")
    Call<RespuestaListaPueblos> damePueblos(@Header("api-key") String token);


   @GET("pueblo")
   Call<RespuestaFallo> pruebaGet(@Header("api-key") String token);

   /*
    invoca a una url cuyo endpoint es pueblo, método POST
    El método es insertaPueblo y acepta por el body tanto los datos del nuevo objeto, como
    el token por el header.
    Devuelve como respuesta un objeto del tipo RespuestaInsertaPueblo
    */
    @POST("pueblo")
    Call<RespuestaInsertaPueblo> insertaPueblo(@Body NuevoPuebloApi nuevoPuebloApi,
                                               @Header("api-key") String token);
   

    /*
    El método es modificarPueblo y acepta lo mismo que el insertar.
    Devuelve como respuesta un objeto del tipo RespuestaModificarPueblo
     */
    @PUT("pueblo")
    Call<RespuestaModificarPueblo> modificarPueblo(@Query("id") long id,
                                                   @Body NuevoPuebloApi modificaPueblo,
                                                   @Header("api-key") String token);



    /*
    Elimina un pueblo pasando su idPueblo
    Devuelve como respuesta un objeto del tipo RespuestaEliminarPueblo
     */
    @DELETE("pueblo")
    Call<RespuestaEliminarPueblo> eliminarPueblo(@Query("id") long id,
                                                 @Header("api-key") String token);

}
