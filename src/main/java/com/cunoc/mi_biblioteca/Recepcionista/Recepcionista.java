package com.cunoc.mi_biblioteca.Recepcionista;

import com.cunoc.mi_biblioteca.Usuarios.Tipo;
import com.cunoc.mi_biblioteca.Usuarios.Usuario;

public class Recepcionista extends Usuario {

    private int puesto_biblioteca;
    private int recep_id;

    public Recepcionista(String username, String nombre, Tipo tipo, String correo, int id,int recep_id, int puesto_biblioteca) {
        super(username, nombre, tipo, correo, id);
        this.puesto_biblioteca = puesto_biblioteca;
        this.recep_id = recep_id;
    }

    public int getRecep_id() {
        return recep_id;
    }

    public void setRecep_id(int recep_id) {
        this.recep_id = recep_id;
    }

    public int getPuesto_biblioteca() {
        return puesto_biblioteca;
    }

    public void setPuesto_biblioteca(int puesto_biblioteca) {
        this.puesto_biblioteca = puesto_biblioteca;
    }
}
