package com.cunoc.mi_biblioteca.Usuarios;

public class Usuario {

    private String nombre;
    private String correo;
    private Tipo tipo;
    private int id;
    private String username;
    private String password;

    public Usuario(String username, String nombre, Tipo tipo, String correo, int id) {
        this.nombre = nombre;
        this.correo = correo;
        this.tipo = tipo;
        this.id = id;
        this.username = username;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public Tipo getTipo() {
        return tipo;
    }

    public void setTipo(Tipo tipo) {
        this.tipo = tipo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
