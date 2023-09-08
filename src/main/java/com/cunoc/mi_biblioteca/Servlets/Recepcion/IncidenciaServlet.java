package com.cunoc.mi_biblioteca.Servlets.Recepcion;

import com.cunoc.mi_biblioteca.Admin.Predeterminador;
import com.cunoc.mi_biblioteca.Biblioteca.EstadoPrestamo;
import com.cunoc.mi_biblioteca.Biblioteca.EstadoRenta;
import com.cunoc.mi_biblioteca.Biblioteca.Libro;
import com.cunoc.mi_biblioteca.Biblioteca.PrestamoResumen;
import com.cunoc.mi_biblioteca.DB.BibliotecaDB;
import com.cunoc.mi_biblioteca.DB.Conector;
import com.cunoc.mi_biblioteca.DB.LibroDB;
import com.cunoc.mi_biblioteca.Recepcionista.Recepcion;
import com.cunoc.mi_biblioteca.Usuarios.Cliente.Cliente;
import com.cunoc.mi_biblioteca.Usuarios.Cliente.Perfil;
import com.cunoc.mi_biblioteca.Usuarios.Usuario;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "IncidenciaServlet", urlPatterns = "/recepcion/incidencia-servlet")
public class IncidenciaServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Conector conector = (Conector) req.getSession().getAttribute("conector");
        String prestamo = req.getParameter("prestamo");
        BibliotecaDB bibliotecaDB = new BibliotecaDB(conector);
        List<PrestamoResumen> prestamosActivos = bibliotecaDB.buscarPrestamosActivos();
        String alerta = req.getParameter("alerta");
        req.removeAttribute("alerta");
        if (prestamo!=null){
            LibroDB libroDB = new LibroDB(conector);
            Recepcion recepcion = new Recepcion(conector);
            PrestamoResumen prestamoResumen = bibliotecaDB.buscarPrestamo(prestamo);
            Libro libro =  libroDB.buscarLibro(String.valueOf(prestamoResumen.getIsbn()));
            try {
                Cliente cliente = recepcion.obtenerClienteByID(String.valueOf(recepcion.buscarUsuarioByPrestamo(Integer.parseInt(prestamo))));
                req.setAttribute("cliente",cliente);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            req.setAttribute("libro",libro);
            req.setAttribute("prestamoRes",prestamoResumen);
        }
        if (alerta!=null){
            if (alerta.equals("failMoney")){
                alerta = "El usuario no tienes suficiente saldo!";
                req.setAttribute("alerta",alerta);
            }
            if (alerta.equals("success")){
                alerta = "Se ha reportado la incidencia";
                req.setAttribute("alerta",alerta);
            }
            if (alerta.equals("success-final")){
                alerta = "Se completo el prestamos sin incidencia";
                req.setAttribute("alerta",alerta );
            }
        }
        req.setAttribute("prestamosActivos",prestamosActivos);
        req.getRequestDispatcher("/areas/recepcion/incidencias.jsp").forward(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Conector conector = (Conector) req.getSession().getAttribute("conector");
        String prestamo = req.getParameter("prestamo");
        String tipo = req.getParameter("tipo");
        String isbn = req.getParameter("isbn");
        String id = req.getParameter("userID");
        String action = req.getParameter("action");
        BibliotecaDB bibliotecaDB = new BibliotecaDB(conector);
        Recepcion recepcion = new Recepcion(conector);
        LibroDB libroDB = new LibroDB(conector);
        Libro libro =  libroDB.buscarLibro(isbn);
        Perfil perfil = new Perfil(conector);
        double precio = 0;
        double saldo = 0;
        double precioMulta = (new Predeterminador(conector)).multaMora();
        if (libro!=null){
            precio = libro.getPrecio();
        }
        String alerta = "failMoney";
        if (id!=null) {
            saldo = perfil.obtenerSaldo(id);
        } else if (prestamo!=null){
            saldo = perfil.obtenerSaldo(String.valueOf(recepcion.buscarClienteByPrestamo(Integer.parseInt(prestamo))));
        }
        if (prestamo!=null && tipo != null && saldo >= precio){
            if (tipo.equals("maltrato")){
                bibliotecaDB.insertarIncidencia(EstadoRenta.DAÑO,precio, Integer.parseInt(prestamo), id);
            } else if (tipo.equals("perdida")){
                bibliotecaDB.insertarIncidencia(EstadoRenta.PERDIDA,precio, Integer.parseInt(prestamo), id);
            }
            alerta = "success";
        } else if (prestamo!=null && action!= null && saldo >= precioMulta){
            try {
                bibliotecaDB.finalizarPrestamo(prestamo);
                alerta = "success-final";
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else if (saldo < precio || saldo < precioMulta){
            alerta = "failMoney";
        }
        String link = "/recepcion/incidencia-servlet?alerta="+alerta;
        resp.sendRedirect(link);
    }
}
