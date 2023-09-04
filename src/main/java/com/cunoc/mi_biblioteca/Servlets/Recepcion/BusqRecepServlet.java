package com.cunoc.mi_biblioteca.Servlets.Recepcion;

import com.cunoc.mi_biblioteca.Biblioteca.Existencia;
import com.cunoc.mi_biblioteca.Biblioteca.Libro;
import com.cunoc.mi_biblioteca.DB.Conector;
import com.cunoc.mi_biblioteca.DB.Filtro;
import com.cunoc.mi_biblioteca.DB.LibroDB;
import com.cunoc.mi_biblioteca.Recepcionista.Recepcionista;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "BusquedaRecepcionServlet", urlPatterns = "/recepcion/busqueda-servlet")
public class BusqRecepServlet extends HttpServlet{

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Conector conector = (Conector) req.getSession().getAttribute("conector");
        setServlet(req, conector);
        String rentaID = req.getParameter("rentaID");
        String alerta = null;
        if (null != rentaID){
            if (rentaID.equals("failure")){
                alerta = "Error al intentar procesar el prestamo";
            } else {
                alerta = "Prestamo# "+rentaID+" creado con exito!";
            }
        }
        req.getRequestDispatcher("/areas/recepcion/recepcion-busqueda.jsp").forward(req,resp);
    }

    private void setServlet(HttpServletRequest req, Conector conector) {
        Recepcionista recepcionista = (Recepcionista) req.getSession().getAttribute("recepcionista");
        Filtro filtro = new Filtro(conector);
        List<Libro> listaLibros = List.of(filtro.filtrarPorBiblioteca(String.valueOf(recepcionista.getPuesto_biblioteca())));
        List<Libro> listaLibrosGlobal = List.of(filtro.filtrarPorUnico());
        req.setAttribute("libros", listaLibros);
        req.setAttribute("librosGlobal", listaLibrosGlobal);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Conector conector = (Conector) req.getSession().getAttribute("conector");
        LibroDB libroDB = new LibroDB(conector);
        setServlet(req, conector);
        String isbn = req.getParameter("isbn");
        if (isbn!=null) {
            List<Existencia> existencias = libroDB.reportarExistencias(isbn);
            req.setAttribute("existencias",existencias);
            req.getRequestDispatcher("/areas/recepcion/recepcion-busqueda.jsp").forward(req,resp);
        } else {
            resp.sendRedirect("/recepcion/busqueda-servlet");
        }
    }
}
