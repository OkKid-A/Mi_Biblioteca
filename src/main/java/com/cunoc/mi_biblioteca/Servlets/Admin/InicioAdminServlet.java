package com.cunoc.mi_biblioteca.Servlets.Admin;

import com.cunoc.mi_biblioteca.Admin.Admin;
import com.cunoc.mi_biblioteca.DB.Conector;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(urlPatterns = "/admin/inicio-servlet")
public class InicioAdminServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Conector conector = (Conector) req.getSession().getAttribute("conector");
        Admin admin = (Admin) req.getSession().getAttribute("admin");
        req.getRequestDispatcher("/areas/admin/inicio-admin.jsp").forward(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }
}
