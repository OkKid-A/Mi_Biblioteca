package com.cunoc.mi_biblioteca.Envios;

public class Entrega {

    private int id_encargo;
    private int id_prestamo;
    private int isbn;
    private int cliente_id;
    private String cliente_Nombre;
    private EstadoEntrega estadoEntrega;
    private String biblioteca;
    private String nombre_libro;

    public Entrega(int id_encargo, int id_prestamo, int isbn, int cliente_id, String cliente_Nombre, EstadoEntrega estadoEntrega, String biblioteca, String nombre_libro) {
        this.id_encargo = id_encargo;
        this.id_prestamo = id_prestamo;
        this.isbn = isbn;
        this.cliente_id = cliente_id;
        this.cliente_Nombre = cliente_Nombre;
        this.estadoEntrega = estadoEntrega;
        this.biblioteca = biblioteca;
        this.nombre_libro = nombre_libro;
    }

    public int getId_encargo() {
        return id_encargo;
    }

    public void setId_encargo(int id_encargo) {
        this.id_encargo = id_encargo;
    }

    public int getId_prestamo() {
        return id_prestamo;
    }

    public void setId_prestamo(int id_prestamo) {
        this.id_prestamo = id_prestamo;
    }

    public int getIsbn() {
        return isbn;
    }

    public void setIsbn(int isbn) {
        this.isbn = isbn;
    }

    public int getCliente_id() {
        return cliente_id;
    }

    public void setCliente_id(int cliente_id) {
        this.cliente_id = cliente_id;
    }

    public String getCliente_Nombre() {
        return cliente_Nombre;
    }

    public void setCliente_Nombre(String cliente_Nombre) {
        this.cliente_Nombre = cliente_Nombre;
    }

    public String getEstadoEntrega() {
        return String.valueOf(estadoEntrega);
    }

    public void setEstadoEntrega(EstadoEntrega estadoEntrega) {
        this.estadoEntrega = estadoEntrega;
    }

    public String getBiblioteca() {
        return biblioteca;
    }

    public void setBiblioteca(String biblioteca) {
        this.biblioteca = biblioteca;
    }

    public String getNombre_libro() {
        return nombre_libro;
    }

    public void setNombre_libro(String nombre_libro) {
        this.nombre_libro = nombre_libro;
    }
}
