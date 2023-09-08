package com.cunoc.mi_biblioteca.Servlets.Usuario;

import com.cunoc.mi_biblioteca.Biblioteca.Libro;
import com.cunoc.mi_biblioteca.DB.Conector;
import com.cunoc.mi_biblioteca.DB.Filtro;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet (name = "BusquedaServlet", urlPatterns = "/usuario/busqueda-servlet")
public class BusquedaServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Conector conector = new Conector();
        Filtro filtro = new Filtro(conector);
        List<Libro> listaLibros = List.of((filtro.filtrarPorUnico()));
        req.setAttribute("libros", listaLibros);
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/areas/cliente/busqueda.jsp");
        req.getRequestDispatcher("/areas/cliente/busqueda.jsp").forward(req,resp);
    }
}
