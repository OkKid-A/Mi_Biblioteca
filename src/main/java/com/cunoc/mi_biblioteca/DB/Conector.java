package com.cunoc.mi_biblioteca.DB;

import jakarta.ws.rs.PUT;

import java.sql.*;

public class Conector {

    private final String usuario = "rootdba";
    private final String portUrl = "jdbc:mysql://localhost:3306/Mi_Biblioteca";
    private final String password = "1234";
    private Connection connection;
    private ResultSet resultados;

    public Conector() {

    }

    public Connection realizarConneccion() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(portUrl, usuario, password);
            return connection;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ResultSet selectFrom(String query) {
        try {
            Connection conneccion = realizarConneccion();
            Statement statement = conneccion.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            resultados = statement.executeQuery(query);
            return resultados;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }

    public void update(String query, String[] valores) {
        try {
            Connection conneccion = realizarConneccion();
            PreparedStatement preparedStatement = conneccion.prepareStatement(query);
            for (int k = 0; k < valores.length; k++) {
                preparedStatement.setString(k + 1, valores[k]);
            }
            preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        closeThis();
    }



    public void closeThis() {
        try {
            if (resultados != null) {
                resultados.close();
            }
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String encomillar(String input){
        return ("'" + input + "'");
    }

    public String enPorcentaje(String input){
        return ('%' + input + '%');
    }
}
