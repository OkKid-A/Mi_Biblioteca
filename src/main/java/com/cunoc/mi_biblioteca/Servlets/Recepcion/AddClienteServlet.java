package com.cunoc.mi_biblioteca.Servlets.Recepcion;

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

@WebServlet (name = "AddClienteServlet", urlPatterns = "/recepcion/add-cliente-servlet")
public class AddClienteServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String error = req.getParameter("error");
        String id = req.getParameter("id");
        String alerta = null;
        if (id!=null){
            alerta = "Se agrego un nuevo cliente con ID: " + id;
            req.setAttribute("success",alerta);
            req.removeAttribute(id);
        } else if (error!=null){
            alerta = "Hubo un error en los datos del cliente";
            req.setAttribute("error",alerta);
            req.removeAttribute(error);
        }
        req.getRequestDispatcher("/areas/recepcion/cliente-creacion.jsp").forward(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Recepcionista recepcionista = (Recepcionista) req.getSession().getAttribute("recepcionista");
        Conector conector = (Conector) req.getSession().getAttribute("conector");
        String username = req.getParameter("username");
        String nombre = req.getParameter("name");
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String saldo = req.getParameter("saldo");
        Recepcion recepcion = new Recepcion(conector);
        if (username!=null&&nombre!=null&email!=null&password!=null&saldo!=null){
            try {
               int id = recepcion.crearCliente(nombre,username,email,password,saldo);
               resp.sendRedirect("/recepcion/add-cliente-servlet?id=" +id);
            } catch (SQLException e) {
                e.printStackTrace();
                String error = "error";
                resp.sendRedirect("/recepcion/add-cliente-servlet?error=" +error);
            }
        }
    }
}
