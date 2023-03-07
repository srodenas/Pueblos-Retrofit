
package com.pmdm.virgen.pueblosconnavigationdrawer2.modelos_api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NuevoPuebloApi {
/*
    @Expose
    private long id;

 */
    @SerializedName("nombre")
    @Expose
    private String nombre;
    @SerializedName("descripcion")
    @Expose
    private String descripcion;

    @SerializedName("habitantes")
    @Expose
    private int habitantes;

    @SerializedName("imagen")
    @Expose
    private String imagen;

    public NuevoPuebloApi(String nombre, String descripcion, int habitantes, String urlImagen) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.habitantes = habitantes;
        this.imagen = urlImagen;
    }

    public int getHabitantes() {
        return habitantes;
    }

    public void setHabitantes(int habitantes) {
        this.habitantes = habitantes;
    }
/*
    public long getId() {
        return id;
    }

    public void setId(long idUsuario) {
        this.id = idUsuario;
    }


 */
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }
}