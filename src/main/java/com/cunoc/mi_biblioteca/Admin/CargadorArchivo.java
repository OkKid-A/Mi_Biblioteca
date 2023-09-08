package com.cunoc.mi_biblioteca.Admin;

import com.cunoc.mi_biblioteca.Biblioteca.EstadoPrestamo;
import com.cunoc.mi_biblioteca.Biblioteca.EstadoRenta;
import com.cunoc.mi_biblioteca.DB.Conector;
import com.cunoc.mi_biblioteca.Envios.Entrega;
import com.cunoc.mi_biblioteca.Envios.EstadoEntrega;
import com.cunoc.mi_biblioteca.Envios.TipoEncargo;
import com.cunoc.mi_biblioteca.Envios.TipoEntrega;
import com.cunoc.mi_biblioteca.Recepcionista.Recepcion;
import com.cunoc.mi_biblioteca.Usuarios.Tipo;
import jakarta.servlet.http.PushBuilder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CargadorArchivo {

    private BufferedReader neededReader;
    private Conector conector;
    private Recepcion recepcion;
    private JSONParser jsonParser;

    public CargadorArchivo(Conector conector){
        this.conector = conector;
        this.jsonParser = new JSONParser();
        this.recepcion = new Recepcion(conector);
    }

    public void cargarArchivo(BufferedReader reader) throws IOException, ParseException, SQLException {
        Object datos = jsonParser.parse(reader);
        JSONObject jsonObject = (JSONObject) datos;
        JSONObject adminList = (JSONObject) jsonObject.get("admin");
        JSONArray recepcionistasList = convertirArray(jsonObject.get("usuariosRecepcion"));
        JSONArray transporList = convertirArray(jsonObject.get("usuariosTransporte"));
        JSONArray usuariosList = convertirArray(jsonObject.get("usuarios"));
        JSONArray categoriasList = convertirArray(jsonObject.get("categorias"));
        JSONArray librosList = convertirArray(jsonObject.get("libros"));
        JSONArray listadoBiblio = convertirArray(jsonObject.get("bibliotecas"));
        JSONArray soliPrestList = convertirArray(jsonObject.get("solicitudesPrestamo"));
        JSONArray prestamosList = convertirArray(jsonObject.get("prestamos"));
        JSONArray trasladosList = convertirArray(jsonObject.get("transportesB"));
        JSONArray entregasList = convertirArray(jsonObject.get("transportesU"));
        JSONArray incidencisList = convertirArray(jsonObject.get("incidencias"));
        JSONArray resumirList = convertirArray(jsonObject.get("solicitudesRevocacion"));
        ingresarClientes(usuariosList);
        ingresarAdmin(adminList);
        ingresarCategorias(categoriasList);
        ingresarLibros(librosList);
        ingresarBibliotecas(listadoBiblio);
        ingresarRecepcionistas(recepcionistasList);
        ingresarTransportistas(transporList);
        ingresarSolPrestamo(soliPrestList);
        ingresarPrestamos(prestamosList);
        ingresarEntregas(entregasList);
        ingresarIncidencias(incidencisList);
        ingresarResumir(resumirList);
    }

    public void ingresarClientes(JSONArray usuarios) throws SQLException {
        String insertCliente = "INSERT INTO cliente (saldo,suspendido,subscrito,usuario_id,id_cliente) VALUES (?,?,?,?,?)";
        int size = ((JSONArray)usuarios.get(0)).size();
        for (int i = 0; i < size;i++) {
            JSONObject cliente = ((JSONObject)(((JSONArray)usuarios.get(0)).get(i)));
            System.out.println((String) cliente.get("username"));
            int id = ingresarUsuario(cliente,1);
            conector.update(insertCliente,new String[]{
                    clasificarTipo(cliente.get("saldoInicial")),
                    String.valueOf(0),
                    String.valueOf(0),
                    String.valueOf(id),
                    String.valueOf((Long) cliente.get("codigo"))
            });
        }
    }

    public void ingresarAdmin(JSONObject admin) throws SQLException {
        String insertAdmin = "INSERT INTO admin (usuario_id,admin_id) VALUES (?,?)";
            int id = ingresarUsuario(admin,4);
            conector.update(insertAdmin, new String[]{
                    String.valueOf(id),
                    String.valueOf((Long) admin.get("codigo"))
            });
        }

    public void ingresarRecepcionistas(JSONArray recepcionistas) throws SQLException {
        String insertRecep = "INSERT INTO recepcionista (id_recepcionista, puesto_biblioteca, usuario_id, suspendido) VALUES (?,?,?,?)";
        int size = ((JSONArray)recepcionistas.get(0)).size();
        for (int i = 0; i < size;i++) {
            JSONObject recepcionista = ((JSONObject)(((JSONArray)recepcionistas.get(0)).get(i)));
            int id = ingresarUsuario(recepcionista,3);
            conector.update(insertRecep,new String[]{
                    String.valueOf((Long) recepcionista.get("codigo")),
                    String.valueOf((Long) recepcionista.get("biblioteca")),
                    String.valueOf(id),
                    String.valueOf(0)
            });
        }
    }

    public void ingresarTransportistas(JSONArray transportistas) throws SQLException {
        String insertTrans = "INSERT INTO transportista (transportista_id, usuario_id, suspendido) VALUES (?,?,?)";
        int size = ((JSONArray)transportistas.get(0)).size();
        for (int i = 0; i < size ;i++) {
            JSONObject transportista = ((JSONObject)(((JSONArray)transportistas.get(0)).get(i)));
            int id = ingresarUsuario(transportista,2);
            conector.updateWithException(insertTrans, new String[]{
                    String.valueOf((Long) transportista.get("codigo")),
                    String.valueOf(id),
                    String.valueOf(0)
            });
        }
    }

    public void ingresarCategorias(JSONArray categorias) throws SQLException {
        String insertCate = "INSERT INTO genero (id,nombre,sinopsis) VALUES (?,?,?)";
        int size = ((JSONArray)categorias.get(0)).size();
        for (int i = 0; i < size ;i++) {
            JSONObject genero = ((JSONObject)(((JSONArray)categorias.get(0)).get(i)));
            conector.updateWithException(insertCate, new String[]{
                    String.valueOf((Long) genero.get("codigo")),
                    (String) genero.get("nombre"),
                    (String) genero.get("descripcion")
            });
        }
    }

    public void ingresarLibros(JSONArray libros) throws SQLException {
        String insertLibros = "INSERT INTO libro (isbn,autor,precio,genero_id,nombre) VALUES (?,?,?,?,?)";
        int size = ((JSONArray)libros.get(0)).size();
        for (int i = 0; i < size ;i++) {
            JSONObject libro = ((JSONObject)(((JSONArray)libros.get(0)).get(i)));
            conector.updateWithException(insertLibros, new String[]{
                    String.valueOf((Long) libro.get("isbn")),
                    (String) libro.get("autor"),
                    clasificarTipo(libro.get("costo")),
                    String.valueOf((Long) libro.get("categoria")),
                    (String) libro.get("nombre")
            });
        }
    }

    public void ingresarBibliotecas(JSONArray bibliotecas) throws SQLException {
        String insertBiblio = "INSERT INTO biblioteca (id_biblioteca,nombre,direccion) VALUES (?,?,?)";
        String insertExistencia = "INSERT IGNORE INTO existencia (id_biblioteca,cantidad,isbn,disponibles) " +
                "VALUES (?,?,?,?)";
        int size = ((JSONArray)bibliotecas.get(0)).size();
        for (int i = 0; i < size ;i++) {
            JSONObject biblioteca = ((JSONObject)(((JSONArray)bibliotecas.get(0)).get(i)));
            conector.updateWithException(insertBiblio, new String[]{
                    String.valueOf((Long) biblioteca.get("codigo")),
                    (String) biblioteca.get("nombre"),
                    (String) biblioteca.get("direccion")
            });
            JSONArray existencias = convertirArray(biblioteca.get("libros"));
            int size1 = ((JSONArray)existencias.get(0)).size();
            for (int j = 0; j < size1 ;j++) {
                JSONObject existencia = ((JSONObject)(((JSONArray)existencias.get(0)).get(j)));
                conector.updateWithException(insertExistencia, new String[]{
                        String.valueOf((Long) biblioteca.get("codigo")),
                        String.valueOf((Long) existencia.get("existencias")),
                        String.valueOf((Long) existencia.get("isbn")),
                        String.valueOf((Long) existencia.get("existencias"))
                });
            }
        }
    }

    public void ingresarSolPrestamo(JSONArray soliPrestamos) throws SQLException {
        String insertPrestamos = "INSERT  INTO prestamo (id_prestamo, biblio_origen, recepcionista_id, cliente_id, isbn, " +
                "fecha_creacion, estado, tipo_entrega, transportista_id, dias_reservados) VALUES (?,?,?,?,?,?,?,?,?,?)";
        int size = ((JSONArray)soliPrestamos.get(0)).size();
        for (int i = 0; i < size ;i++) {
            JSONObject prestamo = ((JSONObject)(((JSONArray)soliPrestamos.get(0)).get(i)));
            conector.updateWithException(insertPrestamos, new String[]{
                    String.valueOf((Long) prestamo.get("codigo")),
                    String.valueOf((Long) prestamo.get("biblioteca")),
                    String.valueOf((Long) prestamo.get("recepcionista")),
                    String.valueOf((Long) prestamo.get("usuario")),
                    String.valueOf((Long) prestamo.get("isbn")),
                    (String) prestamo.get("fecha"),
                    String.valueOf(EstadoPrestamo.clasifica((String) prestamo.get("estado"))),
                    String.valueOf(TipoEntrega.valueOf((String) prestamo.get("tipoEntrega"))),
                    clasificarNulo(prestamo.get("transportista")),
                    String.valueOf(8)
            });
        }
    }

    public void ingresarPrestamos(JSONArray prestamos) throws SQLException {
        String insertPrestamo = "INSERT  INTO prestamo (biblio_origen, recepcionista_id, cliente_id, isbn, " +
                "fecha_creacion, estado, dias_reservados) VALUES (?,?,?,?,?,?,?)";
        String insertRenta = "INSERT  INTO renta (id_renta, multa, fecha_inicio, id_prestamo) VALUES (?,?,?,?)";
        int size = ((JSONArray)prestamos.get(0)).size();
        for (int i = 0; i < size ;i++) {
            JSONObject prestamo = ((JSONObject)(((JSONArray)prestamos.get(0)).get(i)));
            conector.updateWithException(insertPrestamo, new String[]{
                    String.valueOf((Long) prestamo.get("biblioteca")),
                    String.valueOf((Long) prestamo.get("recepcionista")),
                    String.valueOf((Long) prestamo.get("usuario")),
                    String.valueOf((Long) prestamo.get("isbn")),
                    (String) prestamo.get("fecha"),
                    String.valueOf(EstadoPrestamo.clasifica((String) prestamo.get("estado"))),
                    String.valueOf(8)
            });
            recepcion.reducirDisponibles(String.valueOf((Long) prestamo.get("biblioteca")), String.valueOf((Long) prestamo.get("isbn")));
            ResultSet resultSet = conector.selectFrom("SELECT MAX(id_prestamo) AS 'id' FROM prestamo");
            resultSet.next();
            int id_prestamo = resultSet.getInt("id");
            conector.updateWithException(insertRenta, new String[]{
                    String.valueOf((Long) prestamo.get("codigo")),
                    clasificarTipo(prestamo.get("multa")),
                    (String) prestamo.get("fecha"),
                    String.valueOf(id_prestamo)
            });
        }
    }

    public void ingresarEntregas(JSONArray entregas) throws SQLException {
        String insertEncargo = "INSERT INTO encargo (numero_encargo, biblio_origen, transportista_id, fecha, estado, tipo_encargo) " +
                "VALUES (?,?,?,?,?,?)";
        String insertEntrega = "INSERT INTO entrega (numero_encargo, id_prestamo, isbn, cliente_id) VALUES (?,?,?,?)";
        int size = ((JSONArray)entregas.get(0)).size();
        for (int i = 0; i < size ;i++) {
            JSONObject entrega = ((JSONObject)(((JSONArray)entregas.get(0)).get(i)));
            conector.updateWithException(insertEncargo, new String[]{
                    String.valueOf((Long) entrega.get("codigo")),
                    String.valueOf((Long) entrega.get("biblioteca")),
                    String.valueOf((Long) entrega.get("transportista")),
                    (String) entrega.get("fecha"),
                    String.valueOf(EstadoEntrega.clasificar((String) entrega.get("estado"))),
                    String.valueOf(TipoEncargo.ENTREGA)
            });
            int posibleEN = buscarPrestamos(entrega);
            if (posibleEN==-1){
                String insertPrestamos = "INSERT  INTO prestamos (id_prestamo, biblio_origen, cliente_id, isbn, " +
                        "fecha_creacion, estado, tipo_entrega, transportista_id) VALUES (?,?,?,?,?,?,?,?)";
                conector.updateWithException(insertPrestamos, new String[]{
                        String.valueOf((Long) entrega.get("codigo")),
                        String.valueOf((Long) entrega.get("biblioteca")),
                        String.valueOf((Long) entrega.get("usuario")),
                        String.valueOf((Long) entrega.get("isbn")),
                        (String) entrega.get("fecha"),
                        String.valueOf(EstadoPrestamo.PENDIENTE),
                        String.valueOf(TipoEntrega.DOMICILIO),
                        String.valueOf((Long) entrega.get("transportista"))
                });
                int nuevo = buscarPrestamos(entrega);
                conector.updateWithException(insertEntrega,new String[]{
                        String.valueOf((Long) entrega.get("codigo")),
                        String.valueOf(nuevo),
                        String.valueOf((Long) entrega.get("isbn")),
                        String.valueOf((Long) entrega.get("usuario"))
                });
            } else {
                conector.updateWithException(insertEntrega,new String[]{
                        String.valueOf((Long) entrega.get("codigo")),
                        String.valueOf(posibleEN),
                        String.valueOf((Long) entrega.get("isbn")),
                        String.valueOf((Long) entrega.get("usuario"))
                });
            }
        }
    }

    public void ingresarIncidencias(JSONArray incidencias) throws SQLException {
        String update = "UPDATE renta SET multa = ?, tipo_multa = ? WHERE id_renta = ?";
        int size = ((JSONArray)incidencias.get(0)).size();
        for (int i = 0; i < size ;i++) {
            JSONObject incidencia = ((JSONObject)(((JSONArray)incidencias.get(0)).get(i)));
            conector.updateWithException(update, new String[]{
                    clasificarTipo(incidencia.get("cantidadPagada")),
                    String.valueOf(EstadoRenta.clasifica((String) incidencia.get("tipo"))),
                    String.valueOf((Long) incidencia.get("prestamo"))
            });
        }
    }

    public void ingresarResumir(JSONArray resumir) throws SQLException {
        String insertRes = "INSERT  INTO resumir (id,cliente_id,descripcion,estado) VALUES (?,?,?,?)";
        int size = ((JSONArray)resumir.get(0)).size();
        for (int i = 0; i < size ;i++) {
            JSONObject suspension = ((JSONObject)(((JSONArray)resumir.get(0)).get(i)));
            conector.updateWithException(insertRes,new String[]{
                    String.valueOf((Long) suspension.get("codigo")),
                    String.valueOf((Long) suspension.get("usuario")),
                    (String) suspension.get("detalle"),
                    (String) suspension.get("estado")
            });
        }
    }

    public int buscarPrestamos(JSONObject entrega) throws SQLException {
        String select = String.format("SELECT * FROM prestamo WHERE biblio_origen = %s AND cliente_id = %s AND isbn = %s AND fecha_creacion = %s AND transportista_id = %s",
                String.valueOf((Long) entrega.get("biblioteca")),
                String.valueOf((Long) entrega.get("usuario")),
                String.valueOf((Long) entrega.get("isbn")),
                conector.encomillar((String) entrega.get("fecha")),
                String.valueOf((Long) entrega.get("transportista")));
        ResultSet resultSet = conector.selectFrom(select);
        if (resultSet.next()){
            return resultSet.getInt("id_prestamo");
        } else {
            return -1;
        }
    }

    public int ingresarUsuario(JSONObject usuario, int nivel) throws SQLException {
        String insertUserQuery = "INSERT INTO usuario (username,nombre,password,tipo,email) VALUES (?,?,MD5(?),?,?)";
        conector.update(insertUserQuery,new String[]{
                (String) usuario.get("username"),
                (String) usuario.get("nombre"),
                (String) usuario.get("password"),
                String.valueOf((nivel)),
                (String) usuario.get("email")
        });
        String selectId = String.format("SELECT * FROM usuario WHERE username = %s",conector.encomillar((String) usuario.get("username")));
        ResultSet idSet = conector.selectFrom(selectId);
        idSet.next();
        return idSet.getInt("id");
    }


    private JSONArray convertirArray(Object jsonObject){
        JSONArray jsonArray = new JSONArray();
        jsonArray.add(jsonObject);
        return jsonArray;
    }

    public String clasificarTipo(Object dato){
        String valid = null;
        try {
            return String.valueOf((Double) dato);
        } catch (ClassCastException e){
            return String.valueOf((Long) dato);
        }
    }

    public String clasificarNulo(Object dato){
        if (((Long) dato) == null){
            return null;
        } else {
            return String.valueOf((Long) dato);
        }
    }

}
