<jsp:useBean id="currentUser" scope="session" type="com.cunoc.mi_biblioteca.Usuarios.Usuario"/>
<jsp:useBean id="cliente" scope="session" type="com.cunoc.mi_biblioteca.Usuarios.Cliente.Cliente"/>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%--
  Created by IntelliJ IDEA.
  User: okkid-a
  Date: 9/1/23
  Time: 11:23 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Prestamos</title>
    <jsp:include page="/includes/resources.jsp"/>
    <jsp:include page="/includes/header-cliente.jsp"/>
    <jsp:include page="modal-resources.jsp"/>
</head>
<body>
<div class="justify-content-center container-fluid row">
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

    <div class="justify-content-center mx-auto row">
        <div class="card form-group col"  style="width: 18rem;">
            <div class="card-body">
                <h5 class="card-title">Detalles del Prestamo </h5>
                <p class="card-text" id="textoTipoPrestamo"></p>
            </div>
            <ul class="list-group list-group-flush">
                <li class="list-group-item">Libro: ${prestamoRes.nombreLibro}</li>
                <li class="list-group-item" id="diasNumero">Dias: ${prestamoRes.dias}</li>
                <li class="list-group-item" id="precioa">Precio: $${libro.precio}</li>
                <li class="list-group-item" id="estadoDias">Estado: ${prestamoRes.estado}</li>
                <li class="list-group-item" id="ordenTotal">Fecha de Retorno: <fmt:formatDate value="${prestamoRes.fecha_regreso}" pattern="dd/MM/yyyy"/></li>
            </ul>
        </div>
        <div class="card mb-4 col">
            <div class="card-body text-center">
                <h3 class="my-3">Reportar Incidencia</h3>
                        <p class="text-muted mb-1">Por favor reporta aqui cualquier incidente o perdida</p>
                        <p class="text-muted mb-4">Ten en cuenta que esto creara restricciones temporales en tu cuenta por dias</p>
                <p class="text-muted mb-4">Asegurate de tener al menos $${libro.precio} de saldo para pagar por el libro.</p>
                <div class="d-flex justify-content-center mb-2">
                    <form action="${pageContext.request.contextPath}/prestamo/usuario-servlet?prestamo=${prestamoRes.id}&tipo=maltrato" method="post">
                        <input type="hidden" name="isbn" value="${prestamoRes.isbn}">
                        <button type="submit" class="btn btn-primary mr-3" >Reportar Maltrato</button>
                    </form>
                    <form action="${pageContext.request.contextPath}/prestamo/usuario-servlet?prestamo=${prestamoRes.id}&tipo=perdida" method="post">
                        <input type="hidden" name="isbn" value="${prestamoRes.isbn}">
                        <button type="submit" class="btn btn-primary" >Reportar Perdida</button>
                    </form>
                        </div>
            </div>
        </div>
    </div>
    </c:if>
</div>
<div class="justify-content-center panel panel-default">
    <h3>Listado de Prestamos Activos</h3>
    <table id="listPrestamos" class="pt-4 table table-bordered table-striped" style="max-height: 300px" >
        <thead>
        <tr>
            <th>Numero</th>
            <th>Libro</th>
            <th>ISBN</th>
            <th>Fecha</th>
            <th>Estado</th>
        </tr>
        </thead>
        <tbody>
        <c:if test="${!empty(prestamosActivos)}">
        <c:forEach var="prestamo" items="${prestamosActivos}" varStatus="status" >
            <tr>
                <td>${prestamo.id}</td>
                <td>
                    <a href="${pageContext.request.contextPath}/prestamo/usuario-servlet?prestamo=${prestamo.id}">
                            ${prestamo.nombreLibro}</a>
                </td>
                <td>${prestamo.isbn}</td>
                <td>${prestamo.fecha_creacion}</td>
                <td>${prestamo.estado}</td>
            </tr>
        </c:forEach>
        </c:if>
        </tbody>
    </table>
    <hr>
    <h3>Listado de Prestamos Pendientes</h3>
    <table id="listaPending" class="pt-4 table table-bordered table-striped" style="max-height: 300px" >
        <thead>
        <tr>
            <th>Numero</th>
            <th>Libro</th>
            <th>ISBN</th>
            <th>Fecha</th>
            <th>Estado</th>
        </tr>
        </thead>
        <tbody>
        <c:if test="${!empty(prestamosPending)}">
            <c:forEach var="prestamo" items="${prestamosPending}" varStatus="status" >
                <tr>
                    <td>${prestamo.id}</td>
                    <td>${prestamo.nombreLibro}</td>
                    <td>${prestamo.isbn}</td>
                    <td>${prestamo.fecha_creacion}</td>
                    <td>${prestamo.estado}</td>
                </tr>
            </c:forEach>
        </c:if>
        </tbody>
    </table>
</div>
</body>
</html>
