
package com.pmdm.virgen.pueblosconnavigationdrawer2.modelos_api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

;


public class PuebloApi {
    public static final String ARGUMENTO_ID = "id";
    public static final String ARGUMENTO_NOMBRE = "nombre";
    public static final String ARGUMENTO_DESCRIPCION = "descripcion";
    public static final String ARGUMENTO_NUM_HABITANTES = "habitantes";
    public static final String ARGUMENTO_URL_IMAGEN = "imagen";


    @SerializedName("id")
    @Expose
    private long id;
    @SerializedName("nombre")
    @Expose
    private String nombre;
    @SerializedName("descripcion")
    @Expose
    private String descripcion;

    @SerializedName("imagen")
    @Expose
    private String imagen;

    @SerializedName("habitantes")
    @Expose
    private int habitantes;

    public PuebloApi(long id, String nombre, String descripcion, String imagen, int habitantes) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.imagen = imagen;
        this.habitantes = habitantes;
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

    public void setHabitantes(int habitantes){this.habitantes = habitantes;}

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getImagen() {
        return imagen;
    }

    public int getHabitantes(){ return habitantes;}

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }


}
