package com.cunoc.mi_biblioteca.Usuarios.Cliente;

import com.cunoc.mi_biblioteca.Biblioteca.EstadoPrestamo;
import com.cunoc.mi_biblioteca.Biblioteca.EstadoRenta;
import com.cunoc.mi_biblioteca.DB.Conector;
import com.cunoc.mi_biblioteca.Recepcionista.Incidencia;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Perfil {

    private Conector conector;

    public Perfil(Conector conector) {
        this.conector = conector;
    }

    public boolean clasificarValidez(int prestAct, boolean subscrito){
        String selectParQuery = "SELECT * FROM parametros WHERE nombre= \"Limite de libros\"";
        ResultSet restriccionLibros = conector.selectFrom(selectParQuery);
        String selectParSUbQuery = "SELECT * FROM parametros WHERE nombre= \"Limite de libros premium\"";
        ResultSet restSubLibros = conector.selectFrom(selectParSUbQuery);
        try {
            restriccionLibros.next();
            restSubLibros.next();
            int restLibros = Integer.parseInt(restriccionLibros.getString("valor"));
            int restriccionSub = Integer.parseInt(restSubLibros.getString("valor"));
            if ((subscrito && prestAct < restriccionSub) || (!subscrito && prestAct <restLibros)){
                return  true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    public void insertSuspension(String id,String descripcion){
        String suspQuery = String.format("SELECT * FROM resumir WHERE cliente_id = %s AND estado = %s",id, conector.encomillar(String.valueOf(SuspensionEstado.PENDIENTE)));
        String inQuery = "INSERT IGNORE INTO resumir (cliente_id,descripcion,estado) VALUES(?,?,?)";
        try(ResultSet resultSet = conector.selectFrom(suspQuery)) {
            if (resultSet.next()) {
                String updateQuery= "UPDATE resumir SET descripcion = ? WHERE cliente_id = ? AND estado = ?";
                conector.update(updateQuery,new String[]{descripcion,id, String.valueOf(SuspensionEstado.PENDIENTE)});
            }else {
                conector.update(inQuery,new String[]{id,descripcion, String.valueOf(SuspensionEstado.PENDIENTE)});
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    public List<Suspension> buscarSuspension(String id){
        String suspQuery = String.format("SELECT * FROM resumir WHERE cliente_id= %s",id);
        ResultSet resultSet = conector.selectFrom(suspQuery);
        List<Suspension> suspensiones = new ArrayList<>();
        Suspension suspension;
        try {
            if (resultSet.next()){
                do {
                    suspension = new Suspension(resultSet.getString("descripcion"), resultSet.getString("estado"));
                    suspensiones.add(suspension);
                }while (resultSet.next());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return suspensiones;
    }

    public boolean buscarSubscrito(String id){
        String validQuery = String.format("SELECT subscrito FROM cliente WHERE id_cliente = %s",id);
        ResultSet resultSet = conector.selectFrom(validQuery);
        boolean subscrito = false;
        try {
            if (resultSet.next()){
                subscrito = resultSet.getBoolean("subscrito");
                return subscrito;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return subscrito;
    }

    public void editarNombre(String id, String nombre){
        String nombreQuery = "UPDATE usuario SET nombre = ? WHERE id = ?";
        conector.update(nombreQuery,new String[]{nombre,id});
    }

    public void editarUsername(String id, String username){
        String nombreQuery = "UPDATE usuario SET username = ? WHERE id = ?";
        conector.update(nombreQuery,new String[]{username,id});
    }

    public void editarEmail(String id, String email){
        String nombreQuery = "UPDATE usuario SET email = ? WHERE id = ?";
        conector.update(nombreQuery,new String[]{email,id});
    }

    public void editarPasword(String id, String password){
        String nombreQuery = "UPDATE usuario SET password = MD5(?) WHERE id = ?";
        conector.update(nombreQuery,new String[]{password,id});
    }

    public void editarSubscripcion(double costo, String cliente_id, boolean subscrito){
        String subscritoQuery = "UPDATE cliente SET subscrito = ? WHERE id_cliente = ?";
        if (subscrito){
            conector.update(subscritoQuery, new String[]{String.valueOf(1), cliente_id});
            restarSaldo(costo,cliente_id);
        } else {
            conector.update(subscritoQuery, new String[]{String.valueOf(0), cliente_id});
        }
    }

    public double restarSaldo(double monto, String cliente_id) {
        Double saldo = obtenerSaldo(cliente_id);
        saldo = saldo - monto;
        conector.update("UPDATE cliente SET saldo = ? WHERE id_cliente = ?", new String[]{String.valueOf(saldo), cliente_id});
        return saldo;
    }

    public double sumarSaldo(double monto, String id){
        double saldo = obtenerSaldo(id);
        saldo = saldo + monto;
        conector.update("UPDATE cliente SET saldo = ? WHERE id_cliente = ?", new String[]{String.valueOf(saldo), id});
        return saldo;}

    public double obtenerSaldo(String id) {
        ResultSet resultSet = conector.selectFrom(String.format("SELECT saldo FROM cliente WHERE id_cliente = %s", conector.encomillar(id)));
        double saldo = 0;
        try {
            resultSet.next();
            saldo = resultSet.getInt("saldo");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return saldo;
    }

    public List<Incidencia> listarPorIncidencia(){
        ResultSet resultSet = conector.selectFrom(String.format("SELECT  u.*,c.suspendido,c.id_cliente as id, COUNT(id_cliente) as incidencias" +
                "    FROM prestamo p" +
                "        INNER JOIN Mi_Biblioteca.cliente c on p.cliente_id = c.id_cliente" +
                "        INNER JOIN Mi_Biblioteca.usuario u on c.usuario_id  = u.id "+
                "   WHERE estado = %s " +
                "    GROUP BY id_cliente", conector.encomillar(String.valueOf(EstadoPrestamo.FINALIZADO_INCIDENCIA))));
        List<Incidencia> clientesTotal = new ArrayList<>();
        try {
            if (resultSet.next()){
                int i = 1;
                do {
                    clientesTotal.add(new Incidencia(resultSet.getInt("id"),resultSet.getString("username"),
                            resultSet.getString("nombre"),resultSet.getBoolean("suspendido"),
                            resultSet.getInt("incidencias")));
                    i++;
                }while (resultSet.next() && i<4);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return clientesTotal;
    }

    public List<Transaccion> obtenerTransacciones(String id){
        String suspQuery = String.format("SELECT * FROM transaccion WHERE id_cliente = %s",id);
        List<Transaccion> transaccions = new ArrayList<>();
        Transaccion transaccion;
        try (ResultSet resultSet = conector.selectFrom(suspQuery);){
            if (resultSet.next()){
                do {
                    transaccion = new Transaccion(resultSet.getDate("fecha"),resultSet.getDouble("valor"));
                    transaccions.add(transaccion);
                }while (resultSet.next());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return transaccions;
    }

    public int getClienteIDByUsuario(String userID) throws SQLException {
        String query = String.format("SELECT id_cliente FROM cliente WHERE usuario_id = %s",userID);
        ResultSet resultSet = conector.selectFrom(query);
        resultSet.next();
        return resultSet.getInt("id_cliente");
    }

    public int getRecepIDByUsuario(String userID) throws SQLException {
        String query = String.format("SELECT id_recepcionista FROM recepcionista WHERE usuario_id = %s",userID);
        ResultSet resultSet = conector.selectFrom(query);
        resultSet.next();
        return resultSet.getInt("id_recepcionista");
    }

    public int getTransIDByUsuario(String userID) throws SQLException {
        String query = String.format("SELECT transportista_id FROM transportista WHERE usuario_id = %s",userID);
        ResultSet resultSet = conector.selectFrom(query);
        resultSet.next();
        return resultSet.getInt("transportista_id");
    }

    public int getAdminIDByUsuario(String userID) throws SQLException {
        String query = String.format("SELECT admin_id FROM admin WHERE usuario_id = %s",userID);
        ResultSet resultSet = conector.selectFrom(query);
        resultSet.next();
        return resultSet.getInt("admin_id");
    }

    public int getUsuarioIDbyCliente(String clienteID) throws SQLException {
        String query = String.format("SELECT usuario_id FROM cliente WHERE id_cliente = %s",clienteID);
        ResultSet resultSet = conector.selectFrom(query);
        resultSet.next();
        return resultSet.getInt("usuario_id");
    }


}
