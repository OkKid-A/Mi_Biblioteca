package com.cunoc.mi_biblioteca.Servlets.Login;

import com.cunoc.mi_biblioteca.Admin.CargadorArchivo;
import com.cunoc.mi_biblioteca.Admin.Lector;
import com.cunoc.mi_biblioteca.DB.Conector;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;

@WebServlet (name = "UploadServlet", urlPatterns = "/servlets/upload-servlet")
@MultipartConfig
public class UpdloadServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Conector conector = new Conector();
        CargadorArchivo cargadorArchivo = new CargadorArchivo(conector);
            InputStream archivo = null;
            try {
                archivo = (req.getPart("pathFile")).getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(archivo, StandardCharsets.UTF_8));
                Lector lector = new Lector();
                cargadorArchivo.cargarArchivo(reader);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ServletException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                throw new RuntimeException(e);
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        try {
                resp.sendRedirect("../index.jsp");
            } catch (IOException e) {
                e.printStackTrace();
            }

    }
}
