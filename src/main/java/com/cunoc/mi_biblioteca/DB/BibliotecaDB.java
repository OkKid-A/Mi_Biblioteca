package com.cunoc.mi_biblioteca.DB;

import com.cunoc.mi_biblioteca.Admin.Predeterminador;
import com.cunoc.mi_biblioteca.Biblioteca.EstadoPrestamo;
import com.cunoc.mi_biblioteca.Biblioteca.EstadoRenta;
import com.cunoc.mi_biblioteca.Biblioteca.PrestamoResumen;
import com.cunoc.mi_biblioteca.Recepcionista.Recepcion;
import com.cunoc.mi_biblioteca.Usuarios.Cliente.Perfil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class BibliotecaDB {

    private Conector conector;
    private Perfil perfil;
    private Predeterminador predeterminador;

    public BibliotecaDB(Conector conector) {
        this.conector = conector;
        this.perfil = new Perfil(conector);
        this.predeterminador = new Predeterminador(conector);
    }

    public int insertarRenta(int id_renta) throws SQLException {
        String insertRenta = "INSERT INTO renta (id_prestamo,fecha_inicio) VALUES (?,?)";
        conector.updateWithException(insertRenta,new String[]{String.valueOf(id_renta),predeterminador.obtenerFechaString()});
        String updatePrestamo = "UPDATE prestamo SET estado = ? WHERE id_prestamo = ?";
        conector.update(updatePrestamo,new String[]{String.valueOf(EstadoPrestamo.ACTIVO),String.valueOf(id_renta)});
        return id_renta;
    }

    public void insertarIncidencia(EstadoRenta estadoPrestamo, double valorRenta, int id_renta, String id){
        String insertRenta = ("UPDATE renta SET multa = ? AND tipo_multa = ? WHERE id_prestamo = ?");
        conector.update(insertRenta,new String[]{String.valueOf(valorRenta),String.valueOf(estadoPrestamo), String.valueOf(id_renta)});
        String insertPrestamo = "UPDATE prestamo SET estado = ? WHERE id_prestamo = ?";
        conector.update(insertPrestamo,new String[]{String.valueOf(EstadoPrestamo.FINALIZADO_INCIDENCIA),String.valueOf(id_renta)});
        insertarSuspension(Integer.parseInt(id));
        (new Perfil(conector)).restarSaldo(valorRenta,id);
    }

    public void insertarSuspension(int cliente_id){
        String insertarSuspension = "INSERT INTO suspensiones (Id_cliente, fecha_inicio, fecha_final) VALUES (?,?,?)";
        String fechaActual = predeterminador.obtenerFechaString();
        String fechaFinal = predeterminador.addDias(predeterminador.obtenerFechaActual(),predeterminador.obtenerDiasSuspension());
        conector.update(insertarSuspension,new String[]{String.valueOf(cliente_id),fechaActual, fechaFinal});
        String updateCliente = "UPDATE cliente SET suspendido = 1 WHERE id_cliente = ?";
        conector.update(updateCliente,new String[]{String.valueOf(cliente_id)});
    }

    public PrestamoResumen buscarPrestamo(String idPrestamo){
        String querySelect = (String.format("SELECT p.*, l.nombre FROM prestamo p" +
                        " INNER JOIN libro l ON p.isbn = l.isbn WHERE " +
                        "(estado = %s OR estado = %s OR estado = %s) AND id_prestamo = %s"
                , conector.encomillar(String.valueOf(EstadoPrestamo.ACTIVO)),
                conector.encomillar(String.valueOf(EstadoPrestamo.PENDIENTE)),
                conector.encomillar(String.valueOf(EstadoPrestamo.VENCIDO)),
                idPrestamo));
        PrestamoResumen prestamo = null;
        try {
            ResultSet resultSet = conector.selectFrom(querySelect);
            if (resultSet.next()){
                Date regreso = resultSet.getDate("fecha_creacion");
                Calendar calendar = GregorianCalendar.getInstance();
                calendar.setTime(regreso);
                calendar.add(GregorianCalendar.DAY_OF_YEAR, resultSet.getInt("dias_reservados"));
                prestamo = new PrestamoResumen(resultSet.getInt("isbn"),
                        resultSet.getDate("fecha_creacion"),
                        resultSet.getInt("id_prestamo"),
                        EstadoPrestamo.clasifica(resultSet.getString("estado"))
                        ,resultSet.getString("nombre"),
                        calendar.getTime(), resultSet.getInt("dias_reservados"));
                return prestamo;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return prestamo;
    }
    public PrestamoResumen buscarPrestamo(String id_cliente, String idPrestamo){
        String querySelect = (String.format("SELECT p.*, l.nombre FROM prestamo p" +
                " INNER JOIN libro l ON p.isbn = l.isbn WHERE cliente_id = %s " +
                "AND (estado = %s OR estado = %s OR estado = %s) AND id_prestamo = %s"
                ,id_cliente, conector.encomillar(String.valueOf(EstadoPrestamo.ACTIVO)),
                conector.encomillar(String.valueOf(EstadoPrestamo.PENDIENTE)),
                conector.encomillar(String.valueOf(EstadoPrestamo.VENCIDO)),
                idPrestamo));
            PrestamoResumen prestamo = null;
        try {
            ResultSet resultSet = conector.selectFrom(querySelect);
            if (resultSet.next()){
                prestamo =crearFromResultSet(resultSet);
                return prestamo;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return prestamo;
    }
    public PrestamoResumen crearFromResultSet(ResultSet resultSet) throws SQLException {
        Date regreso = resultSet.getDate("fecha_creacion");
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(regreso);
        calendar.add(GregorianCalendar.DAY_OF_YEAR, resultSet.getInt("dias_reservados"));
        PrestamoResumen prestamoResumen = new PrestamoResumen(resultSet.getInt("isbn"),
                resultSet.getDate("fecha_creacion"),
                resultSet.getInt("id_prestamo"),
                EstadoPrestamo.clasifica(resultSet.getString("estado"))
                ,resultSet.getString("nombre"),
                calendar.getTime(), resultSet.getInt("dias_reservados"));
        String getUser = String.valueOf(perfil.getUsuarioIDbyCliente(resultSet.getString("cliente_id")));
            String username = getUserName(getUser);
            prestamoResumen.setNombreUsuario(username);
        return prestamoResumen;
    }
    public List<PrestamoResumen> buscarPrestamosActivos(String id){
        List<PrestamoResumen> prestamos = null;
        try {
            String clienteID = String.valueOf(perfil.getClienteIDByUsuario(id));
            String querySelect = (String.format("SELECT p.*, l.nombre FROM prestamo p" +
                            " INNER JOIN libro l ON p.isbn = l.isbn WHERE cliente_id = %s AND (estado = %s OR estado = %s)"
                    ,clienteID, conector.encomillar(String.valueOf(EstadoPrestamo.ACTIVO)),conector.encomillar(String.valueOf(EstadoPrestamo.VENCIDO))));
            ResultSet resultSet = conector.selectFrom(querySelect);
            if (resultSet.next()){
                prestamos = listarPrestamos(resultSet);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return prestamos;
    }
    public List<PrestamoResumen> buscarPrestamosActivos(){
        List<PrestamoResumen> prestamos = null;
        String querySelect = (String.format("SELECT p.*, l.nombre FROM prestamo p" +
                        " INNER JOIN libro l ON p.isbn = l.isbn WHERE estado = %s OR estado = %s"
                ,conector.encomillar(String.valueOf(EstadoPrestamo.ACTIVO)),conector.encomillar(String.valueOf(EstadoPrestamo.VENCIDO))));
        try {
            ResultSet resultSet = conector.selectFrom(querySelect);
            if (resultSet.next()){
                prestamos = listarPrestamos(resultSet);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return prestamos;
    }

    public List<PrestamoResumen> buscarPrestamosPendientes(String id){
        List<PrestamoResumen> prestamos = null;
        try {
            String clienteID = String.valueOf(perfil.getClienteIDByUsuario(id));
            String querySelect = (String.format("SELECT p.*, l.nombre FROM prestamo p" +
                            " INNER JOIN libro l ON p.isbn = l.isbn WHERE cliente_id = %s AND estado = %s"
                    ,clienteID, conector.encomillar(String.valueOf(EstadoPrestamo.PENDIENTE))));
            ResultSet resultSet = conector.selectFrom(querySelect);
            if (resultSet.next()){
                prestamos = listarPrestamos(resultSet);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return prestamos;
    }




    public List<PrestamoResumen> listarPrestamos(ResultSet resultSet) throws SQLException {
        List<PrestamoResumen> prestamos = new ArrayList<>();
        do {
            PrestamoResumen resumen = crearFromResultSet(resultSet);
            prestamos.add(resumen);
        }while (resultSet.next());
        return prestamos;
    }

    public int contarPrestamosCliente(String clienteID){
        String countQuery = String.format("SELECT COUNT(id_prestamo) AS contar FROM prestamo WHERE estado = %s AND cliente_id = %s" ,
                conector.encomillar(String.valueOf(EstadoPrestamo.ACTIVO)),clienteID);
        ResultSet contado = conector.selectFrom(countQuery);
        int prestamos = 0;
        try {
            if (contado.next()){
                prestamos = contado.getInt("contar");
                return prestamos;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return prestamos;
    }
    public String getUserName(String userId){
        String countQuery = String.format("SELECT nombre FROM usuario WHERE id = %s" ,
                userId);
        ResultSet contado = conector.selectFrom(countQuery);
        String nombre = null;
        try {
            if (contado.next()){
                nombre = contado.getString("nombre");
                return nombre;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return nombre;
    }

    public void finalizarPrestamo(String id_prestamo) throws SQLException {
        String getPrestamo = String.format("SELECT p.*, r.fecha_inicio, r.id_renta " +
                "                        FROM prestamo p " +
                "                            INNER JOIN renta r ON p.id_prestamo = r.id_prestamo " +
                "                        WHERE p.id_prestamo = %s", id_prestamo);
        String estado = "UPDATE prestamo SET estado = ? WHERE id_prestamo = ?";
        String updateRenta = "UPDATE renta SET multa = ?, tipo_multa = ? WHERE id_renta = ?";
        ResultSet resultSet = conector.selectFrom(getPrestamo);
        if (resultSet.next()){
            Date fecha_crecion  = resultSet.getDate("fecha_inicio");
            int dias = resultSet.getInt("dias_reservados");
            String fechaMaxima = predeterminador.addDias(fecha_crecion,dias);
            boolean vencio = predeterminador.fechaEsPasado(fechaMaxima);
            if (vencio){
                conector.update(estado, new String[]{String.valueOf(EstadoPrestamo.FINALIZADO_INCIDENCIA),id_prestamo});
                conector.update(updateRenta,new String[]{String.valueOf(predeterminador.multaMora()),
                        String.valueOf(EstadoRenta.MORA),
                        String.valueOf(resultSet.getInt("id_renta"))
                });
                insertarSuspension(resultSet.getInt("cliente_id"));
                perfil.restarSaldo(predeterminador.multaMora(), String.valueOf(resultSet.getInt("cliente_id")));
            } else {
                conector.update(estado, new String[]{String.valueOf(EstadoPrestamo.FINALIZADO),id_prestamo});
            }
            (new Recepcion(conector)).addDisponibles(String.valueOf(resultSet.getInt("biblio_origen")),
                    String.valueOf(resultSet.getInt("isbn")));
        }
        String update = "UPDATE prestamo SET ";
    }
}
