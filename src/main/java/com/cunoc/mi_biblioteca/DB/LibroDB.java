package com.cunoc.mi_biblioteca.DB;

import com.cunoc.mi_biblioteca.Biblioteca.Existencia;
import com.cunoc.mi_biblioteca.Biblioteca.Libro;
import com.cunoc.mi_biblioteca.DB.Conector;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LibroDB {

    private Conector conector;

    public LibroDB(Conector conector){
        this.conector = conector;
    }

    public Libro buscarLibro(String isbn){
        Libro encontrado = null;
        try  {
            ResultSet genero = null;
            ResultSet resLibro = conector.selectFrom(String.format("SELECT * FROM libro WHERE isbn = %s",isbn));
            if (resLibro.next()) {
                genero = conector.selectFrom(String.format("SELECT nombre FROM genero WHERE id = %s",
                        conector.encomillar(String.valueOf(resLibro.getInt("genero_id")))));
            }
            genero.next();
            encontrado = new Libro(resLibro.getString("autor"),resLibro.getString("nombre"),
                    resLibro.getInt("isbn"),genero.getString("nombre"),resLibro.getDouble("precio"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return encontrado;
    }

    public List<Existencia> reportarExistencias(String isbn){
        List<Existencia> existencias = null;
        try {
            ResultSet resultados = conector.selectFrom(String.format("SELECT b.*, e.cantidad, e.isbn FROM biblioteca b" +
                    " INNER JOIN existencia e ON b.id_biblioteca=e.id_biblioteca WHERE isbn = %s;",isbn));
            if (resultados.next()){
                existencias = listarExistencias(resultados,isbn);
            } else {
                throw new NullPointerException();
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return existencias;
    }

    private List<Existencia> listarExistencias(ResultSet resultSet, String isbn) throws SQLException {
        List<Existencia> existencias = new ArrayList<>();
        do{
            try  {
                existencias.add(new Existencia(resultSet.getInt("id_biblioteca"),resultSet.getString("nombre"),
                        resultSet.getString("direccion"),resultSet.getInt("cantidad"),Integer.parseInt(isbn)));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }while (resultSet.next());
        return existencias;
    }
}
