package com.cunoc.mi_biblioteca.Usuarios;

public class Transportista extends Usuario{

    private int transID;

    public Transportista(String username, String nombre, Tipo tipo, String correo, int id, int transID) {
        super(username, nombre, tipo, correo, id);
        this.transID = transID;
    }

    public int getTransID() {
        return transID;
    }

    public void setTransID(int transID) {
        this.transID = transID;
    }
}
