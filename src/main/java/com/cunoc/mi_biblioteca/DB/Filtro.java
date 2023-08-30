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
                libros = listarLibros(resultados);
            } else {
                throw new NullPointerException();
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return libros.toArray(new Libro[libros.size()]);
    }

    public ArrayList<Libro> filtrarPorUnico(String parametro, String query, String genero){
        ArrayList<Libro> libros = null;
        try {
            ResultSet generoSet = conector.selectFrom(String.format("SELECT nombre FROM genero WHERE id = %s",
                    conector.encomillar(String.valueOf(genero)))) ;
            ResultSet resultados = conector.selectFrom(String.format("SELECT * FROM libro WHERE "+ query +" LIKE = %s AND genero_id = %s",
                     conector.enPorcentaje(parametro),conector.encomillar(generoSet.getString("id"))));
            if (resultados.next()){
                libros = listarLibros(resultados);
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return libros;
    }

    private ArrayList<Libro> listarLibros(ResultSet resultSet) throws SQLException {
        ArrayList<Libro> libros = new ArrayList<>();
        do{
            try (ResultSet genero = conector.selectFrom(String.format("SELECT nombre FROM genero WHERE id = %s" ,
                    conector.encomillar(String.valueOf(resultSet.getInt("genero_id")))))) {
                genero.next();
                libros.add(new Libro(resultSet.getString("autor"),resultSet.getString("nombre"),
                        resultSet.getInt("isbn"),genero.getString("nombre"),resultSet.getDouble("precio")));
            }
        }while (resultSet.next());
        return libros;
    }


}
