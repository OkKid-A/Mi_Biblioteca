package com.cunoc.mi_biblioteca.Servlets.Prestamos;

import com.cunoc.mi_biblioteca.Biblioteca.EstadoPrestamo;
import com.cunoc.mi_biblioteca.Biblioteca.Libro;
import com.cunoc.mi_biblioteca.Biblioteca.PrestamoResumen;
import com.cunoc.mi_biblioteca.DB.BibliotecaDB;
import com.cunoc.mi_biblioteca.DB.Conector;
import com.cunoc.mi_biblioteca.DB.LibroDB;
import com.cunoc.mi_biblioteca.Usuarios.Cliente.Cliente;
import com.cunoc.mi_biblioteca.Usuarios.Usuario;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "PrestamoServlet", urlPatterns = "/prestamo/usuario-servlet")
public class PrestamoServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Conector conector = (Conector) req.getSession().getAttribute("conector");
        Usuario usuario = (Usuario) req.getSession().getAttribute("currentUser");
        String prestamo = req.getParameter("prestamo");
        BibliotecaDB bibliotecaDB = new BibliotecaDB(conector);
        String usuarioID = String.valueOf(usuario.getId()) ;
        List<PrestamoResumen> prestamosActivos = bibliotecaDB.buscarPrestamosActivos(usuarioID);
        List<PrestamoResumen> prestamoPending = bibliotecaDB.buscarPrestamosPendientes(usuarioID);
        LibroDB libroDB = new LibroDB(conector);
        String alerta = req.getParameter("alerta");
        if (prestamo!=null){
            PrestamoResumen prestamoResumen = bibliotecaDB.buscarPrestamo(usuarioID, prestamo);
            Libro libro =  libroDB.buscarLibro(String.valueOf(prestamoResumen.getIsbn()));
            req.setAttribute("libro",libro);
            req.setAttribute("prestamoRes",prestamoResumen);
        }
        if (alerta!=null){
            if (alerta.equals("failMoney")){
                alerta = "No tienes suficiente saldo!";
                req.setAttribute("alerta",alerta);
            }
        }
        req.setAttribute("prestamosActivos",prestamosActivos);
        req.setAttribute("prestamosPending",prestamoPending);
        req.getRequestDispatcher("/areas/cliente/prestamos.jsp").forward(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Conector conector = (Conector) req.getSession().getAttribute("conector");
        Usuario usuario = (Usuario) req.getSession().getAttribute("currentUser");
        Cliente cliente = (Cliente) req.getSession().getAttribute("cliente");
        String prestamo = req.getParameter("prestamo");
        String tipo = req.getParameter("tipo");
        String isbn = req.getParameter("isbn");
        BibliotecaDB bibliotecaDB = new BibliotecaDB(conector);
        LibroDB libroDB = new LibroDB(conector);
        Libro libro =  libroDB.buscarLibro(isbn);
        double precio = libro.getPrecio();
        String alerta = "failMoney";
        if (prestamo!=null && tipo != null && cliente.getSaldo() >= precio){
            if (tipo.equals("maltrato")){
                bibliotecaDB.insertarIncidencia(EstadoPrestamo.MALTRATO,precio, Integer.parseInt(prestamo), String.valueOf(usuario.getId()));
                cliente.setSaldo(cliente.getSaldo()-precio);
            } else if (tipo.equals("perdida")){
                bibliotecaDB.insertarIncidencia(EstadoPrestamo.PERDIDA,precio, Integer.parseInt(prestamo), String.valueOf(usuario.getId()));
                cliente.setSaldo(cliente.getSaldo()-precio);
            }
             alerta = "success";
        } else if (cliente.getSaldo() < precio){
           alerta = "failMoney";
        }
        req.getSession().setAttribute("cliente",cliente);
        String link = "/prestamo/usuario-servlet?alerta="+alerta;
        resp.sendRedirect(link);
    }
}
