package com.cunoc.mi_biblioteca.Biblioteca;

import org.eclipse.tags.shaded.org.apache.regexp.RE;

public enum EstadoPrestamo {
    ACTIVO,
    PENDIENTE,
    VENCIDO,
    FINALIZADO,
    FINALIZADO_INCIDENCIA;

    public static EstadoPrestamo clasifica(String nuevo){
        switch (nuevo){
            case "ACTIVO":
                return ACTIVO;
            case "PENDIENTE":
                return  PENDIENTE;
            case "VENCIDO":
                return VENCIDO;
            case "FINALIZADO":
            case "FINALIZADA":
                return FINALIZADO;
            case "FINALIZADO_INCIDENCIA":
            case "FINALIZADO CON INCIDENCIA":
                return FINALIZADO_INCIDENCIA;
            default:
                return null;
        }
    }
}
