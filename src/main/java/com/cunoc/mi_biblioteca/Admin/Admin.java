package com.cunoc.mi_biblioteca.Admin;

import com.cunoc.mi_biblioteca.Usuarios.Tipo;
import com.cunoc.mi_biblioteca.Usuarios.Usuario;

public class Admin extends Usuario {

    private int admin_id;


    public Admin(String username, String nombre, Tipo tipo, String correo, int id, int admin_id) {
        super(username, nombre, tipo, correo, id);
        this.admin_id = admin_id;
    }

    public int getAdmin_id() {
        return admin_id;
    }

    public void setAdmin_id(int admin_id) {
        this.admin_id = admin_id;
    }
}
