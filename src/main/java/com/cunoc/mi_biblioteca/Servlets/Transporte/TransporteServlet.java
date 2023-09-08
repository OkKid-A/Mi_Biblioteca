package com.cunoc.mi_biblioteca.Servlets.Transporte;

import com.cunoc.mi_biblioteca.DB.BibliotecaDB;
import com.cunoc.mi_biblioteca.DB.Conector;
import com.cunoc.mi_biblioteca.Envios.Bodega;
import com.cunoc.mi_biblioteca.Envios.Entrega;
import com.cunoc.mi_biblioteca.Envios.EstadoEntrega;
import com.cunoc.mi_biblioteca.Usuarios.Transportista;
import com.cunoc.mi_biblioteca.Usuarios.Usuario;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "TransporteServlet", urlPatterns = "/transporte/inicio-servlet")
public class TransporteServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Conector conector = (Conector) req.getSession().getAttribute("conector");
        String transportistaID = String.valueOf(((Transportista)req.getSession().getAttribute("transportista")).getTransID());
        Bodega bodega = new Bodega(conector);
        String error = req.getParameter("error");
        String alerta = null;
        try {
            List<Entrega> entregas = bodega.buscarEntregasPorID(transportistaID);
            req.setAttribute("entregas",entregas);
        } catch (SQLException e) {
            alerta = "No se encontraron entregas";
            req.setAttribute("alerta",alerta);
        }
        if (error!=null){
            req.setAttribute("error","Hubo un error al intentar cambiar el estado");
        }
         req.getRequestDispatcher("/areas/transporte/inicio-transporte.jsp").forward(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Conector conector = (Conector) req.getSession().getAttribute("conector");
        String estado = req.getParameter("estado");
        String id_encargo = req.getParameter("entrega");
        Bodega bodega = new Bodega(conector);
        String prestamo = req.getParameter("prestamo");
        String error = null;
        if (estado!=null){
            if (estado.equals(String.valueOf(1))){
                try {
                    bodega.cambiarEstadoEntrega(id_encargo, EstadoEntrega.EN_CAMINO);
                } catch (SQLException e) {
                    resp.sendRedirect("/transporte/inicio-servlet?error="+1);
                }
            } else if (estado.equals(String.valueOf(2))){
                try {
                    bodega.cambiarEstadoEntrega(id_encargo, EstadoEntrega.FINALIZADO);
                    BibliotecaDB bibliotecaDB = new BibliotecaDB(conector);
                    bibliotecaDB.insertarRenta(Integer.parseInt(prestamo));
                } catch (SQLException | NullPointerException e) {
                    resp.sendRedirect("/transporte/inicio-servlet?error="+1);
                }
            }
            resp.sendRedirect("/transporte/inicio-servlet");
        }else {

            resp.sendRedirect("/transporte/inicio-servlet?error="+1);
        }
    }
}
