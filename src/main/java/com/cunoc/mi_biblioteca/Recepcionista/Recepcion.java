package com.cunoc.mi_biblioteca.Recepcionista;

import com.cunoc.mi_biblioteca.Admin.Predeterminador;
import com.cunoc.mi_biblioteca.Biblioteca.EstadoPrestamo;
import com.cunoc.mi_biblioteca.Biblioteca.PrestamoResumen;
import com.cunoc.mi_biblioteca.DB.BibliotecaDB;
import com.cunoc.mi_biblioteca.DB.Conector;
import com.cunoc.mi_biblioteca.Envios.Bodega;
import com.cunoc.mi_biblioteca.Envios.TipoEncargo;
import com.cunoc.mi_biblioteca.Envios.TipoEntrega;
import com.cunoc.mi_biblioteca.Usuarios.Cliente.Cliente;
import com.cunoc.mi_biblioteca.Usuarios.Cliente.Perfil;
import com.cunoc.mi_biblioteca.Usuarios.Tipo;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class Recepcion {

    private Conector conector;
    private Bodega bodega;
    private BibliotecaDB bibliotecaDB;
    public Perfil perfil;
    private Predeterminador predeterminador;

    public Recepcion(Conector conector) {
        this.conector = conector;
        this.bodega = new Bodega(conector);
        this.bibliotecaDB = new BibliotecaDB(conector);
        this.perfil = new Perfil(conector);
        this.predeterminador = new Predeterminador(conector);
    }

    public int crearCliente(String name, String username, String email, String password, String saldo) throws SQLException {
        String insertQuery = "INSERT INTO usuario (nombre,username,email,password,tipo) VALUES (?,?,?,MD5(?),?)";
        conector.updateWithException(insertQuery,new String[]{name,username,email,password, String.valueOf(Tipo.CLIENTE.getNivel())});
        String selectId = String.format("SELECT * FROM usuario WHERE username = %s",conector.encomillar(username));
        ResultSet idSet = conector.selectFrom(selectId);
        idSet.next();
        int id = idSet.getInt("id");
        String insertCliente = "INSERT INTO cliente (saldo,suspendido,subscrito,usuario_id) VALUES (?,?,?,?)";
        conector.updateWithException(insertCliente, new String[]{saldo, String.valueOf(0), String.valueOf(0),String.valueOf(id)});
        String selectClienteid = String.format("SELECT id_cliente FROM cliente WHERE usuario_id = %s",conector.encomillar(String.valueOf(id)));
        ResultSet clienteSet = conector.selectFrom(selectClienteid);
        clienteSet.next();
        return clienteSet.getInt("id_cliente");
    }

    public List<Cliente> buscarClientes(){
        String selectQuery = "SELECT c.*, u.*FROM cliente c INNER JOIN usuario u ON u.id=c.id_cliente";
        ResultSet clientes = conector.selectFrom(selectQuery);
        List<Cliente> listado = new ArrayList<Cliente>();
        try {
            if (clientes.next()){
                do {
                    Cliente cliente = new Cliente(clientes.getString("username"),clientes.getString("nombre"),
                            Tipo.CLIENTE,clientes.getString("email"),clientes.getInt("id"),clientes.getInt("id_cliente"),
                            clientes.getInt("saldo"),clientes.getBoolean("subscrito"),clientes.getBoolean("suspendido"));
                    int prestAct = bibliotecaDB.contarPrestamosCliente(String.valueOf(cliente.getId()));
                    cliente.setPrestAct(prestAct);
                    Boolean valido = perfil.clasificarValidez(prestAct,cliente.isSubscrito());
                    cliente.setValido(valido);
                    listado.add(cliente);
                } while (clientes.next());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return listado;
    }

    public Cliente obtenerClienteByID(String id) throws SQLException {
        String selectQuery = String.format("SELECT c.*, u.* FROM cliente c INNER JOIN usuario u ON u.id=c.usuario_id WHERE c.usuario_id = %s",id);
        ResultSet clienteSet = conector.selectFrom(selectQuery);
        Cliente cliente = null;
            if (clienteSet.next()){
                do {
                    cliente = new Cliente(clienteSet.getString("username"),clienteSet.getString("nombre"),
                            Tipo.CLIENTE,clienteSet.getString("email"),clienteSet.getInt("id"),clienteSet.getInt("id_cliente"),
                            clienteSet.getInt("saldo"),clienteSet.getBoolean("subscrito"),clienteSet.getBoolean("suspendido"));
                    int prestAct = bibliotecaDB.contarPrestamosCliente(String.valueOf(cliente.getId()));
                    cliente.setPrestAct(prestAct);
                    Boolean valido = perfil.clasificarValidez(prestAct,cliente.isSubscrito());
                    cliente.setValido(valido);
                } while (clienteSet.next());
            }
        return cliente;
    }

    public int buscarUsuarioByPrestamo(int prestamoID){
        String query = String.format("SELECT cliente_id FROM prestamo WHERE id_prestamo = %s",conector.encomillar(String.valueOf(prestamoID)));
        ResultSet resultSet = conector.selectFrom(query);
        int userID = 0;
        try {
            if (resultSet.next()){
                int cxID = resultSet.getInt("cliente_id");
                String userQuery = String.format("SELECT usuario_id FROM cliente WHERE id_cliente = %s",cxID);
                ResultSet user = conector.selectFrom(userQuery);
                if (user.next()){
                    userID = user.getInt("usuario_id");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return userID;
    }

    public int buscarClienteByPrestamo(int prestamoID){
        String query = String.format("SELECT cliente_id FROM prestamo WHERE id_prestamo = %s",conector.encomillar(String.valueOf(prestamoID)));
        ResultSet resultSet = conector.selectFrom(query);
        int cxID = 0;
        try {
            if (resultSet.next()){
                cxID = resultSet.getInt("cliente_id");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return cxID;
    }

    public List<PrestamoResumen> prestamosPendBiblioteca(String biblioID){
        List<PrestamoResumen> prestamos = null;
        String querySelect = (String.format("SELECT p.*, l.nombre, u.nombre " +
                        "FROM prestamo p " +
                        "    INNER JOIN libro l ON p.isbn = l.isbn " +
                        "    INNER JOIN cliente c ON p.cliente_id = c.id_cliente " +
                        "    INNER JOIN usuario u ON c.usuario_id = u.id " +
                        "WHERE biblio_origen = %s AND estado = %s"
                ,biblioID, conector.encomillar(String.valueOf(EstadoPrestamo.PENDIENTE))));
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
            PrestamoResumen prestamoResumen = new PrestamoResumen(
                    resultSet.getInt("isbn"),resultSet.getDate("fecha_creacion"),
                    resultSet.getInt("id_prestamo"), EstadoPrestamo.clasifica(resultSet.getString("estado")),
                    resultSet.getString("l.nombre"), resultSet.getString("u.nombre"));
            prestamos.add(prestamoResumen);
        }while (resultSet.next());
        return prestamos;
    }

    public void insertarPrestamo(String user, String isbn, String biblio_origen, String dias, String recepcionistaID){
        String queryPrestamo= "INSERT INTO prestamo (cliente_id, isbn, biblio_origen, dias_reservados, fecha_creacion, recepcionista_id," +
                "tipo_entrega,estado) VALUES (?,?,?,?,?,?,?)";
        conector.update(queryPrestamo,new String[]{user,isbn,biblio_origen,dias, predeterminador.obtenerFechaString(),recepcionistaID,String.valueOf(TipoEntrega.RECEPCION),
                String.valueOf(EstadoPrestamo.PENDIENTE)});
    }

    public int insertarSolicitudPrestamo(String user, String isbn, String biblio_origen, String dias){
        String queryPrestamo= "INSERT INTO prestamo (cliente_id, isbn, biblio_origen, dias_reservados, fecha_creacion," +
                "tipo_entrega,estado) VALUES (?,?,?,?,?,?,?)";
        conector.update(queryPrestamo,new String[]{user,isbn,biblio_origen,dias,predeterminador.obtenerFechaString(),String.valueOf(TipoEntrega.RECEPCION),
                String.valueOf(EstadoPrestamo.PENDIENTE)});
        int numeroOrden = Integer.parseInt(getNumeroOrden());
        reducirDisponibles(biblio_origen,isbn);

        return numeroOrden;
    }

    public void insertarPrestamoDomicilio(String user, String isbn, String biblio_origen, String dias, TipoEncargo tipoEncargo){
        String transportistaID = bodega.insertarEncargo(isbn,biblio_origen,tipoEncargo);
        String queryPrestamo= "INSERT INTO prestamo (cliente_id, isbn, biblio_origen, dias_reservados, fecha_creacion," +
                "tipo_entrega,transportista_id, estado) VALUES (?,?,?,?,?,?,?,?)";
        conector.update(queryPrestamo,new String[]{user,isbn,biblio_origen,dias, predeterminador.obtenerFechaString(),String.valueOf(TipoEntrega.DOMICILIO),transportistaID,
                String.valueOf(EstadoPrestamo.PENDIENTE)});
        bodega.insertarEntrega(isbn,user);
        reducirDisponibles(biblio_origen,isbn);
    }

    public void reducirDisponibles(String biblio_origen, String isbn){
        try (ResultSet resultados = conector.selectFrom(String.format("SELECT b.*, e.disponibles, e.isbn FROM biblioteca b" +
                " INNER JOIN existencia e ON b.id_biblioteca=e.id_biblioteca WHERE isbn = %s AND e.id_biblioteca = %s;", isbn,biblio_origen))) {
            if (resultados.next()){
                String insertQuery = "UPDATE existencia SET disponibles = ? WHERE id_biblioteca = ? AND isbn = ?";
                conector.update(insertQuery,new String[]{String.valueOf((resultados.getInt("disponibles")-1)),biblio_origen,isbn});
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void addDisponibles(String biblio_origen, String isbn){
        try (ResultSet resultados = conector.selectFrom(String.format("SELECT b.*, e.disponibles, e.isbn FROM biblioteca b" +
                " INNER JOIN existencia e ON b.id_biblioteca=e.id_biblioteca WHERE isbn = %s AND e.id_biblioteca = %s;", isbn,biblio_origen))) {
            if (resultados.next()){
                String insertQuery = "UPDATE existencia SET disponibles = ? WHERE id_biblioteca = ? AND isbn = ?";
                conector.update(insertQuery,new String[]{String.valueOf((resultados.getInt("disponibles")+1)),biblio_origen,isbn});
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public String  getNumeroOrden(){
        String no_orden = null;
        try {
            ResultSet resultSet = conector.selectFrom("SELECT MAX(id_prestamo) AS 'id' FROM prestamo");
            if (resultSet.next()){
                no_orden = resultSet.getString("id");
                return no_orden;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return no_orden;
    }

    public BibliotecaDB getBibliotecaDB() {
        return bibliotecaDB;
    }
}
