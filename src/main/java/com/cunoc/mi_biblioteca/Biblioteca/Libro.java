package com.cunoc.mi_biblioteca.Biblioteca;

public class Libro {

    private String autor;
    private String nombre;
    private int isbn;
    private String genero;
    private  double precio;

    public Libro(String autor, String nombre, int isbn, String genero, double precio) {
        this.autor = autor;
        this.nombre = nombre;
        this.isbn = isbn;
        this.genero = genero;
        this.precio = precio;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getIsbn() {
        return isbn;
    }

    public void setIsbn(int isbn) {
        this.isbn = isbn;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }
}
