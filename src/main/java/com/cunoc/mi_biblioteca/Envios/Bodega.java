package com.cunoc.mi_biblioteca.Envios;

import com.cunoc.mi_biblioteca.DB.Conector;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Bodega {

    private Conector conector;

    public Bodega(Conector conector){
        this.conector = conector;
    }

    public void insertarEntrega(String isbn, String biblio_origen){
        String queryTransportista = String.format("SELECT COUNT(numero_encargo), transportista_id FROM encargo WHERE estado = 'pendiente' " +
                "GROUP BY transportista_id ORDER BY COUNT(numero_encargo)");
        ResultSet resultSet = conector.selectFrom(queryTransportista);
        String insertQuery = "INSERT INTO encargo (isbn, estado, transportista_id,fecha,biblio_origen)" + "VALUES (?,?,?,?,?)";
        int transportistaID = 0;
        try {
            if (resultSet.next()){
                transportistaID = resultSet.getInt("COUNT(numero_encargo)");
                System.out.println(transportistaID);
            }
            conector.update(insertQuery, new String[]{isbn,"pendiente", String.valueOf(transportistaID), String.valueOf(java.time.LocalDate.now()), biblio_origen});
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
