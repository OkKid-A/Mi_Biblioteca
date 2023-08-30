package com.cunoc.mi_biblioteca;

import java.io.*;
import java.util.Arrays;

import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebServlet(name = "helloServlet", value = "/hello-servlet")
public class HelloServlet extends HttpServlet {
    private String message;


    public void init() {
        message = "Hello World!";
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");

        // Hello
        PrintWriter out = response.getWriter();
        Arrays.asList("<html><body>",
                "<h1>" + message + "</h1>",
                "</body></html>").forEach(out::println);
    }

    public void destroy() {
    }
}