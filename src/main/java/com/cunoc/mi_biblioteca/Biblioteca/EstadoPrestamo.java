package com.cunoc.mi_biblioteca.Biblioteca;

import org.eclipse.tags.shaded.org.apache.regexp.RE;

public enum EstadoPrestamo {
    ACTIVO,
    PENDIENTE,
    VENCIDO,
    COMPLETADO,
    MALTRATO,
    PERDIDA;

    public static EstadoPrestamo clasifica(String nuevo){
        switch (nuevo){
            case "ACTIVO":
                return ACTIVO;
            case "PENDIENTE":
                return  PENDIENTE;
            case "VENCIDO":
                return VENCIDO;
            case "COMPLETADO":
                return COMPLETADO;
            case "MALTRATO":
            case "DAÑO":
                return MALTRATO;
            case "PERDIDA":
                return PERDIDA;
            default:
                return null;
        }
    }
}
