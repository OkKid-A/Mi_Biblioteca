package com.cunoc.mi_biblioteca.Usuarios.Cliente;

public enum SuspensionEstado {

    PENDIENTE,
    RECHAZADO,
    APROBADO;

    public static SuspensionEstado clasificar(String suspension){
        if (suspension.equals("APROBADO")) {
            return APROBADO;
        } else if (suspension.equals("RECHAZADO")){
            return RECHAZADO;
        } else{
            return PENDIENTE;
        }
    }
}
