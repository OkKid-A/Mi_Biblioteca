package com.cunoc.mi_biblioteca.Servlets.Usuario;

import com.cunoc.mi_biblioteca.Biblioteca.Existencia;
import com.cunoc.mi_biblioteca.DB.LibroDB;
import com.cunoc.mi_biblioteca.Biblioteca.Libro;
import com.cunoc.mi_biblioteca.DB.Conector;
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
        LibroDB libroDB = new LibroDB(conector);
        Libro libro = libroDB.buscarLibro( req.getParameter("isbn"));
        String biblioteca = req.getParameter("biblioteca");
        req.setAttribute("libro",libro);
        List<Existencia> existencias = libroDB.reportarExistencias(req.getParameter("isbn"));
        req.setAttribute("existencias",existencias);

        req.getRequestDispatcher("/areas/cliente/libro.jsp").forward(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/areas/cliente/libro.jsp").forward(req,resp);
    }
}
