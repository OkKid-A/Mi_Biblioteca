<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.cunoc.mi_biblioteca.Biblioteca.Libro" %>
<%@ page import="java.util.ArrayList" %><%--

  Created by IntelliJ IDEA.
  User: okkid-a
  Date: 8/27/23
  Time: 8:52 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>

<head>
    <title>Buscar</title>
  <jsp:include page="/includes/resources.jsp"/>
  <jsp:include page="/includes/header-cliente.jsp"/>
</head>
<body>
  <div class="pt-5">
    <section class="header-main border-bottom bg-white">
      <div class="container-fluid">
        <div class="row p-2 pt-3 pb-3 d-flex justify-content-center">
          <div class="col-md-6">
            <div class="d-flex form-inputs">
              <select class="form-control col-2" id="filtro" required>
                <option value="0">Filtrar por:</option>
                <option value="0">Nombre</option>
                <option value="1">Autor</option>
                <option value="2">ISBN</option>
                <option value="3">Genero</option>
                <option value="4">Precio</option>
              </select>
              <input class="form-control" id ="nombre-libro" type="text" placeholder="Search any product...">
              <a class="btn btn-dark px-3" href="${pageContext.request.contextPath}/usuario/busqueda-servlet">
                <i class="fa fa-search pt-1"></i>
              </a>
            </div>
            </div>
          </div>

        </div>
        <div class="container">
          <table id="listado" class="table table-bordered table-striped">
            <thead>
            <tr>
              <th>Nombre</th>
              <th>Autor</th>
              <th>ISBN</th>
              <th>Genero</th>
              <th>Precio</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="libro" items="${libros}" varStatus="status" >
                <tr>
                    <td>
                      <a href="${pageContext.request.contextPath}/usuario/libro-servlet?isbn=${libro.isbn}">
                      ${libro.nombre}</a>
                    </td>
                    <td>${libro.autor}</td>
                    <td>${libro.isbn}</td>
                    <td>${libro.genero}</td>
                    <td>$${libro.precio}</td>
                </tr>
            </c:forEach>
            </tbody>
          </table>
        </div>
      </section>
  </div>


</body>
<script>
  $("#nombre-libro").keyup(function(){
    _this = this;
    // Show only matching TR, hide rest of them
    $.each($("#listado tbody").find("tr"), function() {
      console.log($(this).text());
      var s = $("#filtro").val();
      if($(this).find('td').eq(s).text().toLowerCase().indexOf($(_this).val().toLowerCase()) === -1)
        $(this).hide();
      else
        $(this).show();
    });
  });
</script>
</html>
