package com.cunoc.mi_biblioteca.Servlets.Usuario;

import com.cunoc.mi_biblioteca.DB.Conector;
import com.cunoc.mi_biblioteca.Usuarios.Cliente.Cliente;
import com.cunoc.mi_biblioteca.Usuarios.Cliente.Perfil;
import com.cunoc.mi_biblioteca.Usuarios.Cliente.Transaccion;
import com.cunoc.mi_biblioteca.Usuarios.Usuario;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet (name = "UsuarioInicio", urlPatterns = "/usuario/inicio-servlet")
public class UsuarioServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Usuario usuario = (Usuario) req.getSession().getAttribute("currentUser");
        Cliente cliente = (Cliente) req.getSession().getAttribute("cliente");
        Conector conector = (Conector) req.getSession().getAttribute("conector");
        Perfil perfil = new Perfil(conector);
        try {
            List<Transaccion> transacciones = perfil.obtenerTransacciones(String.valueOf(cliente.getCliente_id()));
            if (transacciones!=null){
                req.setAttribute("transacciones",transacciones);

            }
        } catch (NullPointerException e){
            e.printStackTrace();
        }
        req.setAttribute("usuario",usuario);
        req.getRequestDispatcher("/areas/cliente/cliente.jsp").forward(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String tipoEdit = req.getParameter("accion");
        Usuario usuario = (Usuario) req.getSession().getAttribute("currentUser");
        Conector conector = (Conector) req.getSession().getAttribute("conector");
        String valorNuevo = req.getParameter("nuevoDato");
        Perfil perfil = new Perfil(conector);
        if (tipoEdit != null) {
            if (tipoEdit.equals("nombre")) {
                perfil.editarNombre(String.valueOf(usuario.getId()), valorNuevo);
                usuario.setNombre(valorNuevo);
            } else if (tipoEdit.equals("username")) {
                perfil.editarUsername(String.valueOf(usuario.getId()), valorNuevo);
                usuario.setUsername(valorNuevo);
            } else if (tipoEdit.equals("correo")) {
                perfil.editarEmail(String.valueOf(usuario.getId()), valorNuevo);
                usuario.setCorreo(valorNuevo);
            } else if (tipoEdit.equals("password")) {
                perfil.editarPasword(String.valueOf(usuario.getId()), valorNuevo);
            }
        }
        req.getSession().setAttribute("currentUser",usuario);
        resp.sendRedirect("/usuario/inicio-servlet");
    }
}
