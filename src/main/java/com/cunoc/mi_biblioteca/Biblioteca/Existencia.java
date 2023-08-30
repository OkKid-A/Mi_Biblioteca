package com.cunoc.mi_biblioteca.Biblioteca;

public class Existencia extends Biblioteca{

    private int cantidad;
    private int isbn;

    public Existencia(int id, String nombre, String direccion, int cantidad, int isbn) {
        super(id, nombre, direccion);
        this.cantidad = cantidad;
        this.isbn = isbn;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public int getIsbn() {
        return isbn;
    }

    public void setIsbn(int isbn) {
        this.isbn = isbn;
    }
}
