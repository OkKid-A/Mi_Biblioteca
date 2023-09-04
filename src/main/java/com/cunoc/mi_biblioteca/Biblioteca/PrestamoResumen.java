package com.cunoc.mi_biblioteca.Biblioteca;

import java.util.Date;

public class PrestamoResumen {

    private int isbn;
    private Date fecha_creacion;
    private int id;
    private EstadoPrestamo estado;
    private String nombreLibro;
    private Date fecha_regreso;
    private int dias;
    private String nombreUsuario;

    public PrestamoResumen(int isbn, Date fecha_creacion, int id, EstadoPrestamo estado, String nombreLibro, String nombreUsuario) {
        this.isbn = isbn;
        this.fecha_creacion = fecha_creacion;
        this.id = id;
        this.estado = estado;
        this.nombreLibro = nombreLibro;
        this.nombreUsuario = nombreUsuario;
    }

    public PrestamoResumen(int isbn, Date fecha_creacion, int id, EstadoPrestamo estado, String nombreLibro, Date fecha_regreso) {
        this.isbn = isbn;
        this.fecha_creacion = fecha_creacion;
        this.id = id;
        this.estado = estado;
        this.nombreLibro = nombreLibro;
        this.fecha_regreso = fecha_regreso;
    }

    public PrestamoResumen(int isbn, Date fecha_creacion, int id, EstadoPrestamo estado, String nombreLibro, Date fecha_regreso, int dias) {
        this.isbn = isbn;
        this.fecha_creacion = fecha_creacion;
        this.id = id;
        this.estado = estado;
        this.nombreLibro = nombreLibro;
        this.fecha_regreso = fecha_regreso;
        this.dias = dias;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public int getDias() {
        return dias;
    }

    public void setDias(int dias) {
        this.dias = dias;
    }

    public Date getFecha_regreso() {
        return fecha_regreso;
    }

    public void setFecha_regreso(Date fecha_regreso) {
        this.fecha_regreso = fecha_regreso;
    }

    public int getIsbn() {
        return isbn;
    }

    public void setIsbn(int isbn) {
        this.isbn = isbn;
    }

    public Date getFecha_creacion() {
        return fecha_creacion;
    }

    public void setFecha_creacion(Date fecha_creacion) {
        this.fecha_creacion = fecha_creacion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEstado() {
        return estado.toString();
    }

    public void setEstado(EstadoPrestamo estado) {
        this.estado = estado;
    }

    public String getNombreLibro() {
        return nombreLibro;
    }

    public void setNombreLibro(String nombreLibro) {
        this.nombreLibro = nombreLibro;
    }
}
