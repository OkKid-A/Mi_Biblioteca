package com.cunoc.mi_biblioteca.Envios;

import com.cunoc.mi_biblioteca.DB.Conector;


import java.sql.ResultSet;
import java.sql.SQLException;

public class Bodega {

    private Conector conector;

    public Bodega(Conector conector){
        this.conector = conector;
    }

    public String insertarEncargo(String isbn, String biblio_origen, TipoEncargo tipoEncargo){

        String insertQuery = "INSERT INTO encargo (estado, transportista_id,fecha,biblio_origen,tipo_encargo)" + "VALUES (?,?,curdate(),?,?)";
        String transportistaID = null;
        try {
                transportistaID = asignarTransportista();
                System.out.println(transportistaID);
            conector.update(insertQuery, new String[]{"pendiente", String.valueOf(transportistaID), biblio_origen, String.valueOf(tipoEncargo)});
            return transportistaID;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }


    public void insertarEntrega(String isbn, String cliente_id){
        String queryEntrega = "INSERT INTO entrega (id_prestamo, numero_encargo, isbn, cliente_id) Values(?,?,?,?)";
        String id_prestamo = null;
        String numero_encargo = null;
        try {
            ResultSet resultSet = conector.selectFrom("SELECT MAX(id_prestamo) AS 'id' FROM prestamo");
            ResultSet encargo = conector.selectFrom("SELECT MAX(numero_encargo) AS 'id' FROM encargo");
            if (resultSet.next()&& encargo.next()){
                id_prestamo = resultSet.getString("id");
                numero_encargo = encargo.getString("id");
                conector.update(queryEntrega, new String[]{id_prestamo,numero_encargo, isbn, cliente_id});
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public String asignarTransportista() throws SQLException{
        String queryTransportista = String.format("SELECT COUNT(numero_encargo), transportista_id FROM encargo WHERE estado = 'pendiente' " +
                "GROUP BY transportista_id ORDER BY COUNT(numero_encargo)");
        ResultSet resultSet = conector.selectFrom(queryTransportista);
        int transportistaID = 0;
            if (resultSet.next()){
                transportistaID = resultSet.getInt("transportista_id");
                System.out.println(transportistaID);
                return String.valueOf(transportistaID);
            }
        return null;
    }


}
