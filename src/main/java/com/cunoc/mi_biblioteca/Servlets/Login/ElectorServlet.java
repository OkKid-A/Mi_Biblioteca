package com.cunoc.mi_biblioteca.Servlets.Login;

import com.cunoc.mi_biblioteca.DB.Conector;
import com.cunoc.mi_biblioteca.Usuarios.Cliente.Cliente;
import com.cunoc.mi_biblioteca.Usuarios.Tipo;
import com.cunoc.mi_biblioteca.Usuarios.Usuario;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet(name = "ElectorServlet", value = "/login/elector-servlet")

public class ElectorServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        Conector conector = new Conector();
        HttpSession session = request.getSession();
        session.setMaxInactiveInterval(3600);
        session.setAttribute("conector",conector);
        Usuario user = null;
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        System.out.println(username);
        String query = String.format("SELECT * FROM usuario WHERE username = %s AND password = MD5(%s)",
                "'" + username + "'", "'" + password + "'");
        try {
            ResultSet usuario = conector.selectFrom(query);
            if (!usuario.next()) {
            } else {
                user = new Usuario(usuario.getString("username"),usuario.getString("nombre"),
                        Tipo.clasificararAcceso(Integer.parseInt(usuario.getString("tipo"))),
                        usuario.getString("email"),usuario.getInt("id"));

                request.getSession(true).setAttribute("currentUser", user);
            }
            conector.closeThis();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            switch (user.getTipo()) {
                case TRANSPORTISTA:
                    break;
                case RECEPCIONISTA:
                    response.sendRedirect("fabrica-servlet");
                    break;
                case CLIENTE:
                    int id = user.getId();
                    query = String.format("SELECT * FROM cliente WHERE id_cliente = %s",id);
                    ResultSet clienteSet = conector.selectFrom(query);
                    if (clienteSet.next()){
                        Cliente cliente = new Cliente(user.getUsername(),user.getNombre(),user.getTipo(),user.getCorreo(),user.getId(),
                                clienteSet.getInt("saldo"),clienteSet.getBoolean("subscrito"),clienteSet.getBoolean("suspendido"));
                        System.out.println(cliente.isSubscrito());
                        request.getSession().setAttribute("cliente", cliente);
                    }
                    response.sendRedirect("/usuario/inicio-servlet");
                    break;
                case ADMIN:
                    response.sendRedirect("admin/usuarios-servlet");
                    break;
            }
        } catch (IOException |NullPointerException e) {
            try {
                response.sendRedirect("../login.jsp");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {

        Conector conector = new Conector();
        HttpSession session = request.getSession();
        session.setMaxInactiveInterval(3600);
        session.setAttribute("conector",conector);
        request.getSession().removeAttribute("currentUser");
        request.getSession().removeAttribute("cliente");
        try {
            response.sendRedirect("../index.jsp");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
