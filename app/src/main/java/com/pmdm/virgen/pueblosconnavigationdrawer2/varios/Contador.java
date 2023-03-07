package com.pmdm.virgen.pueblosconnavigationdrawer2.varios;


public class Contador {
    private static long id=0;

    public static void increId(){
        id++;
    }
    public static long dameId(){
        return id;
    }
}
