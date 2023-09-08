package com.cunoc.mi_biblioteca.Servlets.Usuario;

import com.cunoc.mi_biblioteca.DB.Conector;
import com.cunoc.mi_biblioteca.Usuarios.Cliente.Cliente;
import com.cunoc.mi_biblioteca.Usuarios.Cliente.Perfil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "SubscribeServlet", urlPatterns = "/usuario/suscribe-servlet")
public class SusbcribeServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Conector conector = (Conector) req.getSession().getAttribute("conector");
        Cliente cliente = (Cliente) req.getSession().getAttribute("cliente");
        String monto = req.getParameter("nuevoSaldo");
        Perfil perfil = new Perfil(conector);
        if (monto != null) {
            cliente.setSaldo(perfil.sumarSaldo(Double.parseDouble(monto),String.valueOf(cliente.getCliente_id())));
        } else {
            if (cliente.isSubscrito()){
                perfil.editarSubscripcion(25,String.valueOf(cliente.getCliente_id()),false);
                cliente.setSubscrito(false);
            } else {
                perfil.editarSubscripcion(25,String.valueOf(cliente.getCliente_id()),true);
                cliente.setSaldo(cliente.getSaldo()-25);
                cliente.setSubscrito(true);
            }
        }
        req.getSession().setAttribute("cliente",cliente);
        resp.sendRedirect("/usuario/inicio-servlet");
    }
}
