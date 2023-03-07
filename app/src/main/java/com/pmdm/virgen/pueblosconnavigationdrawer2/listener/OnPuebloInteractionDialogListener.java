package com.pmdm.virgen.pueblosconnavigationdrawer2.listener;

public interface OnPuebloInteractionDialogListener {
    //Inserta un nuevo pueblo


    public void insertarPueblo(String nombre, String descripcion, int nHabitantes, String imagen );
    //Edita un pueblo ya existente.

    public void editarPueblo(long id, String nombre, String descripcion, int nHabitantes, String imagen);

    public void eliminarPueblo(long id);

}
