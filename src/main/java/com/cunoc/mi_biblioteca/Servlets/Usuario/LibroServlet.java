package com.cunoc.mi_biblioteca.Servlets.Usuario;

import com.cunoc.mi_biblioteca.Biblioteca.Existencia;
import com.cunoc.mi_biblioteca.DB.BibliotecaDB;
import com.cunoc.mi_biblioteca.DB.LibroDB;
import com.cunoc.mi_biblioteca.Biblioteca.Libro;
import com.cunoc.mi_biblioteca.DB.Conector;
import com.cunoc.mi_biblioteca.Usuarios.Cliente.Cliente;
import com.cunoc.mi_biblioteca.Usuarios.Cliente.Perfil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "Libro",urlPatterns = "/usuario/libro-servlet")
public class LibroServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Conector conector = (Conector) req.getSession().getAttribute("conector");
        Cliente cliente = (Cliente) req.getSession().getAttribute("cliente");
        LibroDB libroDB = new LibroDB(conector);
        Libro libro = libroDB.buscarLibro( req.getParameter("isbn"));
        String biblioteca = req.getParameter("biblioteca");
        BibliotecaDB bibliotecaDB = new BibliotecaDB(conector);
        req.setAttribute("libro",libro);
        List<Existencia> existencias = libroDB.reportarExistencias(req.getParameter("isbn"));
        req.setAttribute("existencias",existencias);
        boolean valido = (new Perfil(conector)).clasificarValidez(bibliotecaDB.contarPrestamosCliente(String.valueOf(cliente.getCliente_id())), cliente.isSubscrito());
        req.setAttribute("valido",valido);
        req.getRequestDispatcher("/areas/cliente/libro.jsp").forward(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/areas/cliente/libro.jsp").forward(req,resp);
    }
}
