package com.cunoc.mi_biblioteca.Usuarios.Cliente;

import java.util.Date;

public class Suspension {

    private String descripcion;
    private String estado;
    private SuspensionEstado suspensionEstado;
    private Date fecha_inicio;
    private Date fecha_fin;
    private String usuario;
    private int id_cliente;

    public Suspension(String descripcion, String suspensionEstado) {
        this.descripcion = descripcion;
        this.suspensionEstado = SuspensionEstado.clasificar(suspensionEstado);
    }

    public Suspension(Date fecha_inicio, Date fecha_fin, String usuario, int id_cliente) {
        this.fecha_inicio = fecha_inicio;
        this.fecha_fin = fecha_fin;
        this.usuario = usuario;
        this.id_cliente = id_cliente;
    }



    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public int getId_cliente() {
        return id_cliente;
    }

    public void setId_cliente(int id_cliente) {
        this.id_cliente = id_cliente;
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

    public String getSuspensionEstado() {
        return estado;
    }

    public void setSuspensionEstado(SuspensionEstado suspensionEstado) {
        this.suspensionEstado = suspensionEstado;
    }

    public Date getFecha_inicio() {
        return fecha_inicio;
    }

    public void setFecha_inicio(Date fecha_inicio) {
        this.fecha_inicio = fecha_inicio;
    }

    public Date getFecha_fin() {
        return fecha_fin;
    }

    public void setFecha_fin(Date fecha_fin) {
        this.fecha_fin = fecha_fin;
    }
}
