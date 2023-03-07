package com.pmdm.virgen.pueblosconnavigationdrawer2.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RespuestaInsertaPueblo {

    @SerializedName("result")
    @Expose
    private String result;

    @SerializedName("insert_id")
    @Expose
    private Integer insertId;

    @SerializedName("file_img")
    @Expose
    private String file_img;



    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Integer getInsertId() {
        return insertId;
    }

    public void setInsertId(Integer insertId) {
        this.insertId = insertId;
    }

    public String getFile_img() {
        return file_img;
    }

    public void setFile_img(String file_img) {
        this.file_img = file_img;
    }
}