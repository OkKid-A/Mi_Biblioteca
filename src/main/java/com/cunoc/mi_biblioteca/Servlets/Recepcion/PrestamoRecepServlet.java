package com.cunoc.mi_biblioteca.Servlets.Recepcion;

import com.cunoc.mi_biblioteca.Biblioteca.Libro;
import com.cunoc.mi_biblioteca.DB.Conector;
import com.cunoc.mi_biblioteca.DB.LibroDB;
import com.cunoc.mi_biblioteca.Recepcionista.Recepcion;
import com.cunoc.mi_biblioteca.Recepcionista.Recepcionista;
import com.cunoc.mi_biblioteca.Usuarios.Cliente.Cliente;
import com.cunoc.mi_biblioteca.Usuarios.Cliente.Perfil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "PrestamoRecepServlet", urlPatterns = "/recepcion/prestamo-servlet")
public class PrestamoRecepServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Conector conector = (Conector) req.getSession().getAttribute("conector");
        LibroDB libroDB = new LibroDB(conector);
        String isbn = req.getParameter("isbn");
        if (isbn!=null) {
            Libro libro = libroDB.buscarLibro(isbn);
            req.setAttribute("libro", libro);
            Recepcion recepcion = new Recepcion(conector);
            List<Cliente> clientes = recepcion.buscarClientes();
            String clienteID = req.getParameter("cliente");
            if (clienteID==null) {
                req.setAttribute("clientes",clientes);
            }else{
                try {
                    Cliente cliente = recepcion.obtenerClienteByID(clienteID);
                    req.setAttribute("usuario",cliente);
                } catch (SQLException e) {
                    String alerta = "No existe el cliente";
                }
            }
        }
        req.getRequestDispatcher("/areas/recepcion/libro-recepcion.jsp").forward(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String dias = req.getParameter("dias");
        String isbn = req.getParameter("isbn");
        String clienteID = req.getParameter("cliente");
        Recepcionista recepcionista = (Recepcionista) req.getSession().getAttribute("recepcionista");
        int biblioOrigen = recepcionista.getPuesto_biblioteca();
        Conector conector = (Conector) req.getSession().getAttribute("conector");
        Recepcion recepcion = new Recepcion(conector);
        Perfil perfil = new Perfil(conector);
        String alerta = null;
        String rentaId = null;
        if (dias == null) {

        } else if (isbn == null) {

        } else if (clienteID == null) {

        } else if (perfil.clasificarValidez(recepcion.getBibliotecaDB().contarPrestamosCliente(clienteID),
                perfil.buscarSubscrito(clienteID))) {
            rentaId = String.valueOf(recepcion.insertarSolicitudPrestamo(clienteID, isbn, String.valueOf(biblioOrigen), dias));
            try {
                recepcion.getBibliotecaDB().insertarRenta(Integer.parseInt(String.valueOf(rentaId)));
            } catch (SQLException e) {
                rentaId = "failure";
            }
        } else {

        }
        resp.sendRedirect("recepcion/busqueda-servlet?rentaId=" + rentaId);
    }
}
