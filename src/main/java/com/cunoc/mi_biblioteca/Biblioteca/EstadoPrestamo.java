package com.cunoc.mi_biblioteca.Biblioteca;

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
                return MALTRATO;
            case "PERDIDA":
                return PERDIDA;
            default:
                return null;
        }
    }
}
