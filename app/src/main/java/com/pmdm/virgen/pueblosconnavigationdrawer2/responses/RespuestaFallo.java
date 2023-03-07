package com.pmdm.virgen.pueblosconnavigationdrawer2.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RespuestaFallo {

    @SerializedName("result")
    @Expose
    private String result;

    @SerializedName("details")
    @Expose
    private String details;

    public String getResult() {
        return result;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
