package com.cunoc.mi_biblioteca.DB;

import com.cunoc.mi_biblioteca.Biblioteca.EstadoPrestamo;
import com.cunoc.mi_biblioteca.Biblioteca.Prestamo;
import com.cunoc.mi_biblioteca.Biblioteca.PrestamoResumen;
import com.cunoc.mi_biblioteca.Usuarios.Cliente.Perfil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

public class BibliotecaDB {

    private Conector conector;

    public BibliotecaDB(Conector conector) {
        this.conector = conector;
    }

    public int insertarRenta(int id_renta) throws SQLException {
        String insertRenta = "INSERT INTO renta (id_renta,fecha_inicio) VALUES (?,curdate())";
        conector.updateWithException(insertRenta,new String[]{String.valueOf(id_renta)});
        String updatePrestamo = "UPDATE prestamo SET estado = ? WHERE id_prestamo = ?";
        conector.update(updatePrestamo,new String[]{String.valueOf(EstadoPrestamo.ACTIVO),String.valueOf(id_renta)});
        return id_renta;
    }

    public void insertarIncidencia(EstadoPrestamo estadoPrestamo, double valorRenta, int id_renta,String id){
        String insertRenta = ("UPDATE renta SET multa = ? AND tipo_multa = ? WHERE id_renta = ?");
        conector.update(insertRenta,new String[]{String.valueOf(valorRenta),String.valueOf(estadoPrestamo), String.valueOf(id_renta)});
        String insertPrestamo = "UPDATE prestamo SET estado = ? WHERE id_prestamo = ?";
        conector.update(insertPrestamo,new String[]{String.valueOf(estadoPrestamo),String.valueOf(id_renta)});
        (new Perfil(conector)).restarSaldo(valorRenta,id);
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
    public PrestamoResumen buscarPrestamo(String id, String idPrestamo){
        String querySelect = (String.format("SELECT p.*, l.nombre FROM prestamo p" +
                " INNER JOIN libro l ON p.isbn = l.isbn WHERE cliente_id = %s " +
                "AND (estado = %s OR estado = %s OR estado = %s) AND id_prestamo = %s"
                ,id, conector.encomillar(String.valueOf(EstadoPrestamo.ACTIVO)),
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
        String getUser = String.format("SELECT username FROM usuario WHERE id = %s",resultSet.getInt("cliente_id"));
        try (ResultSet user = conector.selectFrom(getUser)) {
            user.next();
            String username = user.getString("username");
            prestamoResumen.setNombreUsuario(username);
        }
        return prestamoResumen;
    }
    public List<PrestamoResumen> buscarPrestamosActivos(String id){
        List<PrestamoResumen> prestamos = null;
        String querySelect = (String.format("SELECT p.*, l.nombre FROM prestamo p" +
                        " INNER JOIN libro l ON p.isbn = l.isbn WHERE cliente_id = %s AND (estado = %s OR estado = %s)"
                ,id, conector.encomillar(String.valueOf(EstadoPrestamo.ACTIVO)),conector.encomillar(String.valueOf(EstadoPrestamo.VENCIDO))));
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
        String querySelect = (String.format("SELECT p.*, l.nombre FROM prestamo p" +
                " INNER JOIN libro l ON p.isbn = l.isbn WHERE cliente_id = %s AND estado = %s"
                ,id, conector.encomillar(String.valueOf(EstadoPrestamo.PENDIENTE))));
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




    public List<PrestamoResumen> listarPrestamos(ResultSet resultSet) throws SQLException {
        List<PrestamoResumen> prestamos = new ArrayList<>();
        do {
            PrestamoResumen resumen = crearFromResultSet(resultSet);
            prestamos.add(resumen);
        }while (resultSet.next());
        return prestamos;
    }

    public int contarPrestamosUsuario(String userId){
        String countQuery = String.format("SELECT COUNT(id_prestamo) AS contar FROM prestamo WHERE estado = %s AND cliente_id = %s" ,
                conector.encomillar(String.valueOf(EstadoPrestamo.ACTIVO)),userId);
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
}
