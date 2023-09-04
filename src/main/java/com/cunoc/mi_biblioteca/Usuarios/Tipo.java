package com.cunoc.mi_biblioteca.Usuarios;

public enum Tipo {

    ADMIN(4),
    RECEPCIONISTA(3),
    TRANSPORTISTA(2),
    CLIENTE(1),
    NO_USER(0);

    private final int nivel;

    Tipo(int nivel) {
        this.nivel = nivel;
    }

    public static Tipo clasificararAcceso(int nivel) {
        switch (nivel) {
            case 0:
                return NO_USER;
            case 1:
                return CLIENTE;
            case 2:
                return TRANSPORTISTA;
            case 3:
                return RECEPCIONISTA;
            case 4:
                return ADMIN;
            default:
                return null;
        }
    }

    public int getNivel() {
        return nivel;
    }
}
