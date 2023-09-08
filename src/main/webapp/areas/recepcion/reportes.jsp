<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: okkid-a
  Date: 9/3/23
  Time: 10:23 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Reportes</title>
    <jsp:include page="/includes/resources.jsp"/>
    <jsp:include page="/includes/header-recepcion.jsp"/>
    <jsp:include page="/areas/cliente/modal-resources.jsp"/>
</head>
<body>
<div>
    <section class="header-main border-bottom bg-white">
        <div class="container-fluid">
            <div class="row p-2 pt-3 pb-3 d-flex justify-content-center">
                <div class="col-auto">
                    <div class="d-flex form-inputs justify-content-center">
                        <form id="form-reporte" action="${pageContext.request.contextPath}/recepcion/reportes-servlet" method="post">
                        <label for="filtro">Selecciona el tipo de reporte:</label>
                            <select class="form-control " id="filtro" name="filtro" required>
                            <option value="0" selected>Usuarios con más incidencias</option>
                            <option value="1">Usuarios suspendidos actualmente</option>
                            <option value="2">Prestamos realizados en los ultimos 30 dias</option>
                        </select>
                            <select class="form-control " id="tipoEstado" name="tipoEstado"  required>
                                <option value="0" selected>Activos</option>
                                <option value="1">Finalizados</option>
                                <option value="2">Finalizado con Incidencia</option>
                                <option value="3">Pendiente</option>
                            </select>
                        <button class="btn btn-dark px-3" type="submit" form="form-reporte">
                            <i class="fa fa-search pt-1"></i>Procesar
                        </button>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </section>
</div>
<c:if test="${!empty(incidencias)}">
<div id="incidencias">
    <div class="container" id="divLocal">
        <h3>Top 5 usuarios con mas incidencias</h3>
        <table id="listadoInci" class="table table-bordered table-striped">
            <thead>
            <tr>
                <th>ID</th>
                <th>Nombre</th>
                <th>Nombre de usuario</th>
                <th>Estado</th>
                <th>Incidencias</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="incidencia" items="${incidencias}" varStatus="status" >
                <tr>
                    <td>${incidencia.id_usuario}</td>
                    <td>${incidencia.nombre}</td>
                    <td>${incidencia.username}</td>
                    <td>${incidencia.estado}</td>
                    <td>${incidencia.conteo}</td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
</div>
</c:if>
<c:if test="${!empty(suspendidos)}">
    <div id="incidencias">
        <div class="container" >
            <h3>Usuarios suspendidos actualmente:</h3>
            <table id="listaSuspen" class="table table-bordered table-striped">
                <thead>
                <tr>
                    <th>ID de Cliente</th>
                    <th>Nombre</th>
                    <th>Estado</th>
                    <th>Fecha de Inicio de la Suspension</th>
                    <th>Fecha del Final de la Suspension</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="suspendido" items="${suspendidos}" varStatus="status" >
                    <tr>
                        <td>${suspendido.id_cliente}</td>
                        <td>${suspendido.usuario}</td>
                        <td>${suspendido.suspensionEstado}</td>
                        <td>${suspendido.fecha_inicio}</td>
                        <td>${suspendido.fecha_fin}</td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</c:if>
<c:if test="${!empty(prestamos)}">
    <div id="incidencias">
        <div class="container" >
            <h3>Prestamos realizados en los ultimos 30 dias:</h3>
            <table id="listaPrest" class="table table-bordered table-striped">
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
                <c:forEach var="solicitud" items="${prestamos}" varStatus="status" >
                    <tr>
                        <td>${solicitud.id}</td>
                        <td>${solicitud.nombreUsuario}</td>
                        <td>${solicitud.nombreLibro}</td>
                        <td>${solicitud.estado}</td>
                        <td>${solicitud.fecha_creacion}</td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</c:if>
</body>
<script>
    $(document).ready(function () {
           $("#tipoEstado").hide();
    });
    $("#filtro").on("change", function (){
        var selected = document.getElementById("filtro");
        var valor = selected.value;
        if (valor == 2){
            $("#tipoEstado").show();
        } else {
            $("#tipoEstado").hide();
        }
    });
</script>
</html>
