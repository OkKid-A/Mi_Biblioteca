package com.cunoc.mi_biblioteca.Envios;

public enum EstadoEntrega {

    PENDIENTE,
    FINALIZADO,
    EN_CAMINO,
    ERROR;


    public static EstadoEntrega clasificar(String estado){
        switch (estado){
            case "PENDIENTE":
                return PENDIENTE;
            case "FINALIZADO":
                return FINALIZADO;
            case "EN_CAMINO":
            case "EN CAMINO":
                return EN_CAMINO;
            default:
                return ERROR;
        }
    }
}
