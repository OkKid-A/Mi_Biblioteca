package com.cunoc.mi_biblioteca.Recepcionista;

public class Incidencia {

    private int id_usuario;
    private String username;
    private String nombre;
    private boolean suspendido;
    private String estado;
    private int conteo;

    public Incidencia(int id_usuario, String username, String nombre, boolean suspendido, int conteo) {
        this.id_usuario = id_usuario;
        this.username = username;
        this.nombre = nombre;
        this.suspendido = suspendido;
        this.conteo = conteo;
        if (suspendido){
            estado = "Suspendido";
        } else {
            estado = "Activo";
        }
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public boolean isSuspendido() {
        return suspendido;
    }

    public void setSuspendido(boolean suspendido) {
        this.suspendido = suspendido;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public int getConteo() {
        return conteo;
    }

    public void setConteo(int conteo) {
        this.conteo = conteo;
    }
}
