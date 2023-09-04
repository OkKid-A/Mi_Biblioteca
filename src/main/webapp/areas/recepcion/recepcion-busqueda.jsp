<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: okkid-a
  Date: 9/2/23
  Time: 2:19 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Busqueda</title>
    <jsp:include page="/includes/resources.jsp"/>
    <jsp:include page="/includes/header-recepcion.jsp"/>
    <jsp:include page="/areas/cliente/modal-resources.jsp"/>
</head>
<body>
<c:if test="${!empty(alerta)}">
    <div class="row">
        <div class="col-10">
            <div class="alert alert-warning alert-dismissible" >
                <button type="button" class="btn-close" data-bs-dismiss="alert">x</button>
                    ${alerta}
            </div>
        </div>
    </div>
</c:if>
<c:if test="${!empty(existencias)}">
    <div class="modal fade" role="dialog" id="modalExistencias">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">Existencias del libro:</h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <table id="locations" class="table table-bordered table-striped">
                    <thead>
                    <tr>
                        <th>No.</th>
                        <th>Biblioteca</th>
                        <th>Direccion</th>
                        <th>Existencia</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="existencia" items="${existencias}" varStatus="status" >
                        <tr>
                            <td>${existencia.id}</td>
                            <td>${existencia.nombre}</td>
                            <td>${existencia.direccion}</td>
                            <td>${existencia.cantidad}</td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</c:if>
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
                        <button class="btn btn-dark px-3" type="button" id="busquedaGlobal">
                            <i class="fa fa-search pt-1"></i>Busqueda Global
                        </button>
                        <button class="btn btn-dark px-3" type="button" id="busquedaLocal">
                            <i class="fa fa-search pt-1"></i>Busqueda Local
                        </button>
                    </div>
                </div>
            </div>

        </div>
        <div class="container" id="divLocal">
            <h3>Libros en existencia Local</h3>
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
                            <a href="${pageContext.request.contextPath}/recepcion/prestamo-servlet?isbn=${libro.isbn}">
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
        <div class="container" id="divGlobal" >
            <h3>Libros en existencia Global.</h3>
            <table id="listadoCompleto" class="table table-bordered table-striped">
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
                <c:forEach var="libro" items="${librosGlobal}" varStatus="status" >
                    <tr>
                        <td>${libro.nombre}</td>
                        <td>${libro.autor}</td>
                        <td>${libro.isbn}</td>
                        <td>${libro.genero}</td>
                        <td>$${libro.precio}</td>
                        <td>
                            <form action="${pageContext.request.contextPath}/recepcion/busqueda-servlet?isbn=${libro.isbn}" method="post">
                            <button class="btn btn-dark" type="submit">Existencias</button>
                            </form>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </section>
</div>
</body>
<script>
    $(document).ready(function (){
        $("#modalExistencias").modal("show");
    });
    $(document).ready(function (){
        $("#divGlobal").hide()
        $("#busquedaLocal").hide()
        $("#busquedaGlobal").on('click',function (){
            $("#divGlobal").show()
            $("#divLocal").hide()
            $("#busquedaLocal").show()
            $("#busquedaGlobal").hide()
        });
        $("#busquedaLocal").on('click',function (){
            $("#divGlobal").hide()
            $("#divLocal").show()
            $("#busquedaLocal").hide()
            $("#busquedaGlobal").show()
        });
    });
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
        _this = this;
        // Show only matching TR, hide rest of them
        $.each($("#listadoCompleto tbody").find("tr"), function() {
            console.log($(this).text());
            var s = $("#filtro").val();
            if($(this).find('td').eq(s).text().toLowerCase().indexOf($(_this).val().toLowerCase()) === -1)
                $(this).hide();
            else
                $(this).show();
        });
    });
</script>
</body>
</html>
