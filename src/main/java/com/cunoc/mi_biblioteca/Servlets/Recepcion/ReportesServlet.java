package com.cunoc.mi_biblioteca.Servlets.Recepcion;

import com.cunoc.mi_biblioteca.Biblioteca.PrestamoResumen;
import com.cunoc.mi_biblioteca.DB.Conector;
import com.cunoc.mi_biblioteca.Recepcionista.Incidencia;
import com.cunoc.mi_biblioteca.Recepcionista.Recepcionista;
import com.cunoc.mi_biblioteca.Recepcionista.Reportes;
import com.cunoc.mi_biblioteca.Usuarios.Cliente.Perfil;
import com.cunoc.mi_biblioteca.Usuarios.Cliente.Suspension;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet(urlPatterns = "/recepcion/reportes-servlet")
public class ReportesServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Recepcionista recepcionista = (Recepcionista) req.getSession().getAttribute("recepcionista");
        Conector conector = (Conector) req.getSession().getAttribute("conector");
        String tipoReporte = req.getParameter("reporte");
        String tipoEstado = req.getParameter("estado");
        if (tipoReporte!=null){
            Reportes reportes = new Reportes(conector);
            if (tipoReporte.equals(String.valueOf(0))){
                Perfil perfil = new Perfil(conector);
                List<Incidencia> incidencias = perfil.listarPorIncidencia();
                req.setAttribute("incidencias",incidencias);
            } else if (tipoReporte.equals(String.valueOf(1))){
                try {
                    List<Suspension> suspensiones = reportes.listarSuspensiones();
                    req.setAttribute("suspendidos",suspensiones);
                } catch (SQLException e) {
                    String alerta = "No se encontraron usuarios suspendidos";
                    e.printStackTrace();
                }
            } else if (tipoReporte.equals(String.valueOf(2))){
                List<PrestamoResumen> prestamos = reportes.prestamosGlobales(Integer.parseInt(tipoEstado));
                req.setAttribute("prestamos",prestamos);
            }
        }
        req.getRequestDispatcher("/areas/recepcion/reportes.jsp").forward(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Recepcionista recepcionista = (Recepcionista) req.getSession().getAttribute("recepcionista");
        Conector conector = (Conector) req.getSession().getAttribute("conector");
        String tipo = req.getParameter("filtro");
        String tipoEstado = req.getParameter("tipoEstado");
        if (tipo!=null){
            if (tipoEstado!=null){
                resp.sendRedirect("/recepcion/reportes-servlet?reporte="+tipo+"&estado="+tipoEstado);
            } else {
                resp.sendRedirect("/recepcion/reportes-servlet?reporte=" + tipo);
            }
        }
    }
}
