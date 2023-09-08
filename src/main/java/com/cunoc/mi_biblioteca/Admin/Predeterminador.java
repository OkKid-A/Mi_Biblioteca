package com.cunoc.mi_biblioteca.Admin;

import com.cunoc.mi_biblioteca.Biblioteca.EstadoPrestamo;
import com.cunoc.mi_biblioteca.Biblioteca.PrestamoResumen;
import com.cunoc.mi_biblioteca.DB.BibliotecaDB;
import com.cunoc.mi_biblioteca.DB.Conector;
import com.cunoc.mi_biblioteca.Recepcionista.Recepcion;
import com.cunoc.mi_biblioteca.Recepcionista.Reportes;
import com.cunoc.mi_biblioteca.Usuarios.Cliente.Suspension;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Predeterminador {

    private Conector conector;

    public Predeterminador(Conector conector) {
        this.conector = conector;
    }

    public Date obtenerFechaActual(){
        String buscar = "SELECT * FROM parametros WHERE nombre = 'Fecha Actual'";
        ResultSet fecha  = conector.selectFrom(buscar);
        try {
            if (fecha.next()){
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String fechaString = fecha.getString("valor");
                Date date = dateFormat.parse(fechaString);
                return date;
            }
        } catch (SQLException e) {
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public String obtenerFechaString(){
        Date diaActual = obtenerFechaActual();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(diaActual);
    }

    public int obtenerDiasSuspension(){
        String buscar = "SELECT * FROM parametros WHERE nombre = 'Dias de suspension por incidencia'";
        ResultSet dias  = conector.selectFrom(buscar);
        try {
            if (dias.next()){
                return Integer.parseInt(dias.getString("valor"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }

    public String addDias(Date date, int dias){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, dias);
        return sdf.format(c.getTime());
    }


    public boolean fechaEsPasado(String fechaNueva){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date fechaComparar = sdf.parse(fechaNueva);
            Date fechaActual = obtenerFechaActual();
            if (fechaActual.after(fechaComparar)){
                return true;
            } else {
                return false;
            }
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public double multaMora (){
            String buscar = "SELECT * FROM parametros WHERE nombre = 'Multa por mora'";
            ResultSet multa  = conector.selectFrom(buscar);
            try {
                if (multa.next()){
                    return Double.parseDouble(multa.getString("valor"));
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            return 0;
    }

    public void updateFecha(String fecha) throws ParseException, SQLException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = sdf.parse(fecha);
        String fechaString = sdf.format(date);
        String update = "UPDATE parametros SET valor = ? WHERE nombre = 'Fecha Actual'";
        conector.update(update,new String[]{
                fechaString
        });
        aplicarCambiosFecha();
    }

    public  void aplicarCambiosFecha() throws SQLException {
        String insertPrestamo = "UPDATE prestamo SET estado = ? WHERE id_prestamo = ?";
        String buscarClientesSuspendidos = "SELECT * FROM cliente WHERE suspendido = 1";
        String selectSuspension = "SELECT * FROM suspensiones WHERE id_cliente = %s ORDER BY fecha_final DESC";
        String setLibre = "UPDATE cliente SET suspendido = 0 WHERE id_cliente = ?";
        BibliotecaDB bibliotecaDB = new BibliotecaDB(conector);
Reportes reportes = new Reportes(conector);
ArrayList<PrestamoResumen> prestamosActivos = (ArrayList<PrestamoResumen>) bibliotecaDB.buscarPrestamosActivos();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        for (PrestamoResumen i:
             prestamosActivos) {
            Date fechaRegreso = i.getFecha_regreso();
            String fechaString = dateFormat.format(fechaRegreso);
            if (fechaEsPasado(fechaString)){
                conector.update(insertPrestamo,new String[]{String.valueOf(EstadoPrestamo.VENCIDO), String.valueOf(i.getId())});
            }
        }
        ResultSet suspendidos = conector.selectFrom(buscarClientesSuspendidos);
        if (suspendidos.next()){
            do {
                int cliente_id = suspendidos.getInt("id_cliente");
                ResultSet suspension = conector.selectFrom(String.format(selectSuspension,cliente_id));
                if (suspension.next()){
                    if (fechaEsPasado(dateFormat.format(suspension.getDate("fecha_final")))){
                        conector.update(setLibre,new String[]{
                                String.valueOf(cliente_id)
                        });
                    }
                }
            }while (suspendidos.next());
        }
        List<Suspension> suspensions = reportes.listarSuspensiones();
        for (Suspension s:
             suspensions) {

        }
    }
}
