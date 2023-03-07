package com.pmdm.virgen.pueblosconnavigationdrawer2.listener;


import com.pmdm.virgen.pueblosconnavigationdrawer2.modelos_api.PuebloApi;

/**
 * Esta interfaz, se encargará de gestionar los
 * eventos click sobre un pueblo y el botón de editar y borrar.
 */
public interface OnPuebloInteractionListener {

    public void onPuebloClick(PuebloApi pueblo);
    public void onPuebloEditarClick(PuebloApi pueblo);
    public void onPuebloBorrarClick(PuebloApi pueblo);
}
