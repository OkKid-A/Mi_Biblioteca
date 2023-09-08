package com.cunoc.mi_biblioteca.Servlets.Admin;

import com.cunoc.mi_biblioteca.Admin.Predeterminador;
import com.cunoc.mi_biblioteca.DB.Conector;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;

@WebServlet(urlPatterns = "/admin/parametros-servlet")
public class ParametrosServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Conector conector = (Conector) req.getSession().getAttribute("conector");
        Predeterminador predeterminador = new Predeterminador(conector);
        String fechaActual = predeterminador.obtenerFechaString();
        req.setAttribute("fechaActual",fechaActual);
        String alerta =  req.getParameter("alerta");
        if (alerta!=null) {
            if (alerta.equals("empty")) {

            } else if (alerta.equals("success")) {
                String success = "Se cambio la fecha con exito";
                req.setAttribute("success", success);
            } else if (alerta.equals("errorCambio")){
                String cambio = "Hubo un error al actualizar los prestamos";
                req.setAttribute("error",cambio);
            }
        }
        req.getRequestDispatcher("/areas/admin/parametros.jsp").forward(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Conector conector = (Conector) req.getSession().getAttribute("conector");
        String accion = req.getParameter("action");
        Predeterminador predeterminador = new Predeterminador(conector);
        String alerta = "empty";
        if (accion!=null){
            if (accion.equals("fecha")){
                String fecha = req.getParameter("fecha");
                try {
                    predeterminador.updateFecha(fecha);
                } catch (ParseException e) {
                    alerta = "error";
                } catch (SQLException e) {
alerta = "errorCambio";                }
                alerta = "success";
            }
        }
        resp.sendRedirect("/admin/parametros-servlet?alerta=" + alerta);
    }
}
