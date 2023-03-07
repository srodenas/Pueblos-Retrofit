
package com.pmdm.virgen.pueblosconnavigationdrawer2.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.pmdm.virgen.pueblosconnavigationdrawer2.modelos_api.PuebloApi;

import java.util.List;

;


public class RespuestaListaPueblos {

    @SerializedName("result")
    @Expose
    private String result;
    @SerializedName("pueblos")
    @Expose
    private List<PuebloApi> pueblos = null;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public List<PuebloApi> getPueblos() {
        return pueblos;
    }

    public void setPueblos(List<PuebloApi> pueblos) {
        this.pueblos = pueblos;
    }

}
