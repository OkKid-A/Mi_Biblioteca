package com.cunoc.mi_biblioteca.Envios;

import com.cunoc.mi_biblioteca.DB.Conector;
import org.eclipse.tags.shaded.org.apache.regexp.RE;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Bodega {

    private Conector conector;

    public Bodega(Conector conector){
        this.conector = conector;
    }

    public List<Entrega> buscarEntregasPorID(String transID) throws SQLException {
        String query = String.format("SELECT l.nombre,u.nombre,e.*,d.*,b.nombre " +
                "    FROM encargo e" +
                "        INNER JOIN biblioteca b on e.biblio_origen = b.id_biblioteca" +
                "        INNER JOIN entrega d on e.numero_encargo = d.numero_encargo" +
                "        INNER JOIN libro l on d.isbn = l.isbn" +
                "        INNER JOIN usuario u on d.cliente_id = u.id " +
                "WHERE transportista_id = %s",transID);
        ResultSet resultSet = conector.selectFrom(query);
        List<Entrega> entregas = new ArrayList<>();
        if (resultSet.next()){
            do {
                Entrega entrega = new Entrega(resultSet.getInt("d.numero_encargo"),
                        resultSet.getInt("id_prestamo"),
                        resultSet.getInt("isbn"),
                        resultSet.getInt("cliente_id"),
                        resultSet.getString("u.nombre"),
                        EstadoEntrega.clasificar(resultSet.getString("estado")),
                        resultSet.getString("b.nombre"),
                        resultSet.getString("l.nombre")
                );
                entregas.add(entrega);
            }while (resultSet.next());
        }
        return entregas;
    }

    public String insertarEncargo(String isbn, String biblio_origen, TipoEncargo tipoEncargo){

        String insertQuery = "INSERT INTO encargo (estado, transportista_id,fecha,biblio_origen,tipo_encargo)" +
                "VALUES (?,?,curdate(),?,?)";
        String transportistaID = null;
        try {
                transportistaID = asignarTransportista();
                System.out.println(transportistaID);
            conector.update(insertQuery, new String[]{String.valueOf(EstadoEntrega.PENDIENTE), String.valueOf(transportistaID),
                    biblio_origen, String.valueOf(tipoEncargo)});
            return transportistaID;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }


    public void cambiarEstadoEntrega(String id_encargo,EstadoEntrega estadoEntrega) throws SQLException {
        String updateQuery = "UPDATE encargo SET estado = ? WHERE numero_encargo = ?";
        conector.updateWithException(updateQuery,new String[]{String.valueOf(estadoEntrega),id_encargo});
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
        String queryTransportista = String.format("SELECT transportista_id, IFNULL(encargo_count,0) as valor " +
                "FROM transportista " +
                "LEFT JOIN " +
                "        (SELECT transportista_id, count(numero_encargo) as encargo_count" +
                "         FROM encargo WHERE estado = %s " +
                "         GROUP BY transportista_id) prop " +
                "USING (transportista_id) ORDER BY valor",conector.encomillar(String.valueOf(EstadoEntrega.PENDIENTE)));
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
