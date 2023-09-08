<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%--
  Created by IntelliJ IDEA.
  User: okkid-a
  Date: 9/2/23
  Time: 12:47 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Inicio</title>
    <jsp:include page="/includes/resources.jsp"/>
    <jsp:include page="/includes/header-recepcion.jsp"/>
    <jsp:include page="/areas/cliente/modal-resources.jsp"/>
</head>
<body>
<c:if test="${!empty(alerta)}">
    <div class="row">
        <div class="col-10">
            <div class="alert alert-warning alert-dismissible" id="alert" name="alert">
                <button type="button" class="btn-close" data-bs-dismiss="alert">x</button>
                    ${alerta}
            </div>
        </div>
    </div>
</c:if>
<c:if test="${!empty(prestamoRes)}">
    <div class="modal fade" role="dialog" id="modalDetalles">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">Solicitud# ${prestamoRes.id}</h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <div class="modal-body">
                    <h5 class="card-title">Detalles del Prestamo: </h5>
                    <ul class="list-group list-group-flush">
                        <li class="list-group-item" id="presNum">Numero: ${prestamoRes.id}</li>
                        <li class="list-group-item" id="diasNumero">Dias: ${prestamoRes.dias}</li>
                        <li class="list-group-item" id="librNombre">Libro: ${prestamoRes.nombreLibro}</li>
                        <li class="list-group-item" id="estadoDias">Estado: ${prestamoRes.estado}</li>
                    </ul>
                    <hr>
                    <h5 class="card-title">Detalles del Usuario: </h5>
                    <ul class="list-group list-group-flush">
                        <li class="list-group-item" id="usName">Usuario: ${userNombre}</li>
                        <li class="list-group-item" id="librosCt">Libros prestados o reservados actualmente: ${libros}</li>
                    </ul>
                </div>
                <div class="modal-footer">
                    <form action="${pageContext.request.contextPath}/recepcion/inicio-servlet?renta=${prestamoRes.id}" method="post">
                        <input type="hidden" name="isbn" value="${prestamoRes.isbn}">
                        <button type="submit" class="btn btn-primary" >Aprobar</button>
                    </form>
                </div>
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
                            <option value="1">Filtrar por:</option>
                            <option value="1">Usuario</option>
                            <option value="0">Numero</option>
                        </select>
                        <input class="form-control" id ="find-solicitud" type="text" placeholder="Busca una solicitud...">
                        <button class="btn btn-dark px-3">
                            <i class="fa fa-search"></i>
                        </button>
                    </div>
                </div>
            </div>
        </div>
        <div class="container">
            <table id="listado" class="table table-bordered table-striped">
                <thead>
                <tr>
                    <th>Numero</th>
                    <th>Usuario</th>
                    <th>Libro</th>
                    <th>Estado</th>
                    <th>Fecha De Solicitud</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="solicitud" items="${solicitudes}" varStatus="status" >
                    <tr>
                        <td>${solicitud.id}</td>
                        <td>${solicitud.nombreUsuario}</td>
                        <td>${solicitud.nombreLibro}</td>
                        <td>${solicitud.estado}</td>
                        <td>${solicitud.fecha_creacion}</td>
                        <td>
                            <form id="form-finalizar" action="${pageContext.request.contextPath}/recepcion/inicio-servlet?solicitud=${solicitud.id}" method="post">
                            <button class="btn btn-dark mb-0" id="finalizar" type="submit" >
                               Finalizar </button>
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
        $("#modalDetalles").modal("show");
    });
    $("#find-solicitud").keyup(function(){
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
