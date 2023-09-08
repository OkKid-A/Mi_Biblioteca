package com.cunoc.mi_biblioteca.Recepcionista;

import com.cunoc.mi_biblioteca.Admin.Predeterminador;
import com.cunoc.mi_biblioteca.Biblioteca.EstadoPrestamo;
import com.cunoc.mi_biblioteca.Biblioteca.PrestamoResumen;
import com.cunoc.mi_biblioteca.DB.BibliotecaDB;
import com.cunoc.mi_biblioteca.DB.Conector;
import com.cunoc.mi_biblioteca.DB.LibroDB;
import com.cunoc.mi_biblioteca.Usuarios.Cliente.Perfil;
import com.cunoc.mi_biblioteca.Usuarios.Cliente.Suspension;
import com.cunoc.mi_biblioteca.Usuarios.Usuario;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Reportes {

    private BibliotecaDB bibliotecaDB;
    private Recepcion recepcion;
    private LibroDB libroDB;
    private Perfil perfil;
    private Predeterminador predeterminador;
    private Conector conector;

    public Reportes(Conector conector) {
        this.conector = conector;
        this.predeterminador = new Predeterminador(conector);
        this.perfil = new Perfil(conector);
        this.bibliotecaDB = new BibliotecaDB(conector);
        this.recepcion = new Recepcion(conector);
    }

    public List<Suspension> listarSuspensiones() throws SQLException {
        List<Suspension> suspensiones = new ArrayList<>();
        String getAllSusp = "SELECT * FROM suspensiones";
        ResultSet susp  = conector.selectFrom(getAllSusp);
        if (susp.next()) {
            do {
                if (!predeterminador.fechaEsPasado(susp.getString("fecha_final"))){
                    Suspension suspension = new Suspension(susp.getDate("fecha_inicio"),
                            susp.getDate("fecha_final"),
                            bibliotecaDB.getUserName(String.valueOf(perfil.getUsuarioIDbyCliente(String.valueOf(susp.getInt("id_cliente"))))),
                            susp.getInt("id_cliente"));
                    suspension.setEstado("Suspendido");
                    suspensiones.add(suspension);
                }
            } while (susp.next());
        }
        return suspensiones;
    }

    public List<PrestamoResumen> prestamosGlobales(int estado){
        List<PrestamoResumen> prestamos = null;
        EstadoPrestamo estadoPrestamo = EstadoPrestamo.ACTIVO;
        switch (estado){
            case 0:
                break;
            case 1:
                estadoPrestamo = EstadoPrestamo.FINALIZADO;
                break;
            case 2:
                estadoPrestamo = EstadoPrestamo.FINALIZADO_INCIDENCIA;
                break;
            case 3:
                estadoPrestamo = EstadoPrestamo.PENDIENTE;
                break;
        }
        String fechaActual = predeterminador.addDias(predeterminador.obtenerFechaActual(),1);
        String fechaInicial = predeterminador.addDias(predeterminador.obtenerFechaActual(),-30);
        String querySelect = (String.format("SELECT p.*, l.nombre, u.nombre " +
                        "FROM prestamo p " +
                        "    INNER JOIN libro l ON p.isbn = l.isbn " +
                        "    INNER JOIN cliente c ON p.cliente_id = c.id_cliente " +
                        "    INNER JOIN usuario u ON c.usuario_id = u.id " +
                        "WHERE estado = %s AND fecha_creacion BETWEEN %s AND %s",
                conector.encomillar(String.valueOf(estadoPrestamo)),conector.encomillar(fechaInicial),
                conector.encomillar(fechaActual)));
        try {
            ResultSet resultSet = conector.selectFrom(querySelect);
            if (resultSet.next()){
                prestamos = recepcion.listarPrestamos(resultSet);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return prestamos;
    }
}
