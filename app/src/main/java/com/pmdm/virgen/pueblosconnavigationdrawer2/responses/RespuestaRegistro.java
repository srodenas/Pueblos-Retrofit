package com.pmdm.virgen.pueblosconnavigationdrawer2.responses;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class RespuestaRegistro {

    @SerializedName("result")
    @Expose
    private String result;
    @SerializedName("insert_id")
    @Expose
    private int insertId;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public int getInsertId() {
        return insertId;
    }

    public void setInsertId(int insertId) {
        this.insertId = insertId;
    }

}