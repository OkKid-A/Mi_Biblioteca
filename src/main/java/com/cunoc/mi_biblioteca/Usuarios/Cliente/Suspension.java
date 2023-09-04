package com.cunoc.mi_biblioteca.Usuarios.Cliente;

public class Suspension {

    private String descripcion;
    private String estado;
    private SuspensionEstado suspensionEstado;

    public Suspension(String descripcion, String suspensionEstado) {
        this.descripcion = descripcion;
        this.suspensionEstado = SuspensionEstado.clasificar(suspensionEstado);
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getEstado() {
        return String.valueOf(suspensionEstado);
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public SuspensionEstado getSuspensionEstado() {
        return suspensionEstado;
    }

    public void setSuspensionEstado(SuspensionEstado suspensionEstado) {
        this.suspensionEstado = suspensionEstado;
    }
}
