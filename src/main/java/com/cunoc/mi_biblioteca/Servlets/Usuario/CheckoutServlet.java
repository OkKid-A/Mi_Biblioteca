package com.cunoc.mi_biblioteca.Servlets.Usuario;

import com.cunoc.mi_biblioteca.Biblioteca.Libro;
import com.cunoc.mi_biblioteca.DB.Conector;
import com.cunoc.mi_biblioteca.DB.LibroDB;
import com.cunoc.mi_biblioteca.Envios.Bodega;
import com.cunoc.mi_biblioteca.Usuarios.Cliente;
import com.cunoc.mi_biblioteca.Usuarios.Subscripcion;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "CheckoutServlet",urlPatterns = "/usuario/checkout-servlet")
public class CheckoutServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Conector conector = (Conector) req.getSession().getAttribute("conector");
        LibroDB libroDB = new LibroDB(conector);
        Libro libro = libroDB.buscarLibro( req.getParameter("isbn"));
        req.setAttribute("libro",libro);
        Cliente cliente = (Cliente) req.getSession().getAttribute("cliente");
        if (cliente.isSubscrito()){
            Subscripcion subscripcion = new Subscripcion(0);
            req.setAttribute("subscripcion", subscripcion);
        }else {
            Subscripcion subscripcion = new Subscripcion(50);
            req.setAttribute("subscripcion",subscripcion);
        }
        req.getRequestDispatcher("/areas/cliente/checkout.jsp").forward(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String dias = req.getParameter("dias");
        String tipo = req.getParameter("tipoEnvio");
        String isbn = req.getParameter("isbn");
        String biblioOrigen = req.getParameter("biblioteca");
        Conector conector = (Conector) req.getSession().getAttribute("conector");
        Bodega bodega = new Bodega(conector);
        if (tipo.equals("domicilio")){
            bodega.insertarEntrega(isbn,biblioOrigen);
        }
    }
}
