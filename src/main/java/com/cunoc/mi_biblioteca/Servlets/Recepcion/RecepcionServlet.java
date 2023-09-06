package com.cunoc.mi_biblioteca.Servlets.Recepcion;

import com.cunoc.mi_biblioteca.Biblioteca.PrestamoResumen;
import com.cunoc.mi_biblioteca.DB.Conector;
import com.cunoc.mi_biblioteca.Recepcionista.Recepcion;
import com.cunoc.mi_biblioteca.Recepcionista.Recepcionista;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet (name = "RecepcionServlet",urlPatterns = "/recepcion/inicio-servlet")
public class RecepcionServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        getRecepcionista(req);
        req.getRequestDispatcher("/areas/recepcion/inicio.jsp").forward(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Conector conector = (Conector) req.getSession().getAttribute("conector");
        Recepcion recepcion = new Recepcion(conector);
        String prestamoID = req.getParameter("solicitud");
        String rentaId = req.getParameter("renta");
        if (rentaId!=null) {
            String alerta = null;
            try {
                recepcion.getBibliotecaDB().insertarRenta(Integer.parseInt(rentaId));
                alerta = "Se activo con exito el prestamo# " + rentaId;
            } catch (SQLException e) {
                alerta = "Ocurrio un error";
            }
            req.setAttribute("alerta",alerta);
            getRecepcionista(req);
            req.getRequestDispatcher("/areas/recepcion/inicio.jsp").forward(req,resp);
        } else if (prestamoID!=null){
            int clienteID = recepcion.buscarClienteByPrestamo(Integer.parseInt(prestamoID));
            int userId = recepcion.buscarUsuarioByPrestamo(Integer.parseInt(prestamoID));
            PrestamoResumen prestamoResumen = recepcion.getBibliotecaDB().buscarPrestamo(String.valueOf(clienteID),prestamoID);
            int prestActivos = recepcion.getBibliotecaDB().contarPrestamosCliente(String.valueOf(clienteID));
            String nombreUsuario = recepcion.getBibliotecaDB().getUserName(String.valueOf(userId));
            req.setAttribute("userNombre",nombreUsuario);
            req.setAttribute("prestamoRes",prestamoResumen);
            req.setAttribute("libros",prestActivos);
            getRecepcionista(req);
            req.getRequestDispatcher("/areas/recepcion/inicio.jsp").forward(req,resp);
        } else {
            resp.sendRedirect("/recepcion/inicio-servlet");
        }

    }

    private void getRecepcionista(HttpServletRequest req) {
        Recepcionista recepcionista = (Recepcionista) req.getSession().getAttribute("recepcionista");
        Conector conector = (Conector) req.getSession().getAttribute("conector");
        Recepcion recepcion = new Recepcion(conector);
        List<PrestamoResumen> solicitudes = recepcion.prestamosPendBiblioteca(String.valueOf(recepcionista.getPuesto_biblioteca()));
        req.setAttribute("solicitudes",solicitudes);
    }
}
