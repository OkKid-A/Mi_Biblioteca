package com.cunoc.mi_biblioteca.DB;

import com.cunoc.mi_biblioteca.Biblioteca.Libro;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Filtro {

private final Conector conector;

    public Filtro(Conector conector){
        this.conector = conector;
    }

    public Libro[] filtrarPorUnico(){
        ArrayList<Libro> libros = null;
        try {
            ResultSet resultados = conector.selectFrom("SELECT * FROM libro ");
            if (resultados.next()){
                libros = listarLibros(resultados,false);
            } else {
                throw new NullPointerException();
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return libros.toArray(new Libro[libros.size()]);
    }

    public Libro[] filtrarPorBiblioteca(String idBiblio){
        ArrayList<Libro> libros = null;
        try {
            ResultSet resultados = conector.selectFrom(String.format("SELECT b.*, e.disponibles, l.* FROM biblioteca b INNER JOIN existencia e ON b.id_biblioteca=e.id_biblioteca " +
                    "INNER JOIN libro l ON e.isbn=l.isbn WHERE b.id_biblioteca = %s",idBiblio));
            if (resultados.next()){
                libros = listarLibros(resultados,true);
            } else {
                throw new NullPointerException();
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return libros.toArray(new Libro[libros.size()]);
    }

    private ArrayList<Libro> listarLibros(ResultSet resultSet, boolean filtrado) throws SQLException {
        ArrayList<Libro> libros = new ArrayList<>();
        do{
            try (ResultSet genero = conector.selectFrom(String.format("SELECT nombre FROM genero WHERE id = %s" ,
                    conector.encomillar(String.valueOf(resultSet.getInt("genero_id")))))) {
                genero.next();
                String nombreLibre = null;
                if (filtrado){
                    nombreLibre = resultSet.getString("l.nombre");
                } else {
                    nombreLibre = resultSet.getString("nombre");
                }
                libros.add(new Libro(resultSet.getString("autor"),nombreLibre,
                        resultSet.getInt("isbn"),genero.getString("nombre"),resultSet.getDouble("precio")));
            }
        }while (resultSet.next());
        return libros;
    }


}
