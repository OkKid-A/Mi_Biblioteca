package com.cunoc.mi_biblioteca.Servlets.Usuario;

import com.cunoc.mi_biblioteca.Biblioteca.Libro;
import com.cunoc.mi_biblioteca.Recepcionista.Recepcion;
import com.cunoc.mi_biblioteca.DB.Conector;
import com.cunoc.mi_biblioteca.DB.LibroDB;
import com.cunoc.mi_biblioteca.Envios.Bodega;
import com.cunoc.mi_biblioteca.Envios.TipoEncargo;
import com.cunoc.mi_biblioteca.Usuarios.Cliente.Cliente;
import com.cunoc.mi_biblioteca.Usuarios.Subscripcion;
import com.cunoc.mi_biblioteca.Usuarios.Usuario;
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
        String orden = (new Recepcion(conector)).getNumeroOrden();
        req.setAttribute("orden",orden);
        req.getRequestDispatcher("/areas/cliente/checkout.jsp").forward(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String dias = req.getParameter("dias");
        String tipo = req.getParameter("tipoEnvio");
        String isbn = req.getParameter("isbn");
        String biblioOrigen = req.getParameter("biblioteca");
        System.out.println(dias);
        String userID = String.valueOf(((Usuario)req.getSession().getAttribute("currentUser")).getId());
        Conector conector = (Conector) req.getSession().getAttribute("conector");
        Bodega bodega = new Bodega(conector);
        Recepcion recepcion = new Recepcion(conector);
        if (tipo.equals("domicilio")){
            recepcion.insertarPrestamoDomicilio(userID,isbn,biblioOrigen,dias,TipoEncargo.ENTREGA);
        } else if (tipo.equals("recepcion")){
            recepcion.insertarSolicitudPrestamo(userID,isbn,biblioOrigen,dias);
        }

        req.setAttribute("orden",recepcion.getNumeroOrden());
        resp.sendRedirect("/areas/cliente/cliente.jsp");
    }
}
