package com.cunoc.mi_biblioteca.Biblioteca;

public enum EstadoRenta {

    DAÑO,
    PERDIDA,
    MORA;

    public static EstadoRenta clasifica(String nuevo){
        switch (nuevo){
            case "MORA":
                return MORA;
            case "PERDIDA":
                return PERDIDA;
            default:
                return DAÑO;
        }
    }
}
