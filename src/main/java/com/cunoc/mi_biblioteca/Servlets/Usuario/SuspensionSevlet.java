package com.cunoc.mi_biblioteca.Servlets.Usuario;

import com.cunoc.mi_biblioteca.DB.Conector;
import com.cunoc.mi_biblioteca.Usuarios.Cliente.Cliente;
import com.cunoc.mi_biblioteca.Usuarios.Cliente.Suspension;
import com.cunoc.mi_biblioteca.Usuarios.Cliente.Perfil;
import com.cunoc.mi_biblioteca.Usuarios.Usuario;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "SuspensionSevlet", urlPatterns = "/usuario/suspension-servlet")
public class SuspensionSevlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Conector conector = (Conector) req.getSession().getAttribute("conector");
        Usuario usuario = (Usuario) req.getSession().getAttribute("currentUser");
        Perfil perfil = new Perfil(conector);
        List<Suspension> suspensiones = perfil.buscarSuspension(String.valueOf(usuario.getId()));
        req.setAttribute("suspensiones",suspensiones);
        req.getRequestDispatcher("/areas/cliente/suspensiones.jsp").forward(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Conector conector = (Conector) req.getSession().getAttribute("conector");
        Usuario usuario = (Usuario) req.getSession().getAttribute("currentUser");
        Perfil perfil = new Perfil(conector);
        String inDescripcion = req.getParameter("inDescripcion");
        if (inDescripcion != null){
            perfil.insertSuspension(String.valueOf(usuario.getId()),inDescripcion);
        }
        resp.sendRedirect("/usuario/suspension-servlet");
    }
}
