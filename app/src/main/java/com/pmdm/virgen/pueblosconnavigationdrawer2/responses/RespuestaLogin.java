package com.pmdm.virgen.pueblosconnavigationdrawer2.responses;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Respuesta que genera la API después de un login.
 */
public class RespuestaLogin {

    @SerializedName("result")
    @Expose
    private String result;
    @SerializedName("token")
    @Expose
    private String token;

    @SerializedName("id")
    @Expose
    private long id;

    @SerializedName("nombre")
    @Expose
    private String nombre;

    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("imagen")
    @Expose
    private String imagen;


    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}