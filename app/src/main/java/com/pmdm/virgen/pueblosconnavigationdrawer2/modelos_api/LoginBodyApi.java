package com.pmdm.virgen.pueblosconnavigationdrawer2.modelos_api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


/**
 * Objeto que pasamos por método POST al loguear.
 * @Body
 */
public class LoginBodyApi {

    @SerializedName("id")
    @Expose
    private long id;

    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("password")
    @Expose
    private String password;

    @SerializedName("nombre")
    @Expose
    private String nombre;

    @SerializedName("disponible")
    @Expose
    private int disponible;

    @SerializedName("imagen")
    @Expose
    private String imagen;

    public LoginBodyApi(String email, String password) {
        this.email = email;
        this.password = password;
        this.disponible = 1;
    }

    public LoginBodyApi(String email, String password, String nombre, int disponible) {
        this.email = email;
        this.password = password;
        this.nombre = nombre;
        this.disponible = disponible;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getDisponible() {
        return disponible;
    }

    public void setDisponible(int disponible) {
        this.disponible = disponible;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }
}