<jsp:useBean id="currentUser" scope="session" type="com.cunoc.mi_biblioteca.Usuarios.Usuario"/>
<jsp:useBean id="cliente" scope="session" type="com.cunoc.mi_biblioteca.Usuarios.Cliente.Cliente"/>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Suspensiones</title>
    <jsp:include page="/includes/resources.jsp"/>
    <jsp:include page="/includes/header-cliente.jsp"/>
</head>
<body>
<div class="pt-lg-5 align-middle" >
<div class="pt-8 container-fluid justify-content-center">
<c:if test="${!empty(suspensiones)}">
    <div class="container">
        <table id="listado" class="table table-bordered table-striped">
            <thead>
            <tr>
                <th>Numero</th>
                <th>Mensaje</th>
                <th>Estado</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="suspension" items="${suspensiones}" varStatus="status">
            <tr>
                <td>${status.count}</td>
                <td>${suspension.descripcion}</td>
                <td>${suspension.estado}</td>
            </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
</c:if>
<hr>
<c:choose>
    <c:when test="${cliente.suspendido}">
    <div class="pt-5">

        <h1 class="text-center" style="color: #000000">Ingresa una nueva solucitud de activacion.</h1>
        <div class="container justify-content-center">
            <form action="${pageContext.request.contextPath}/usuario/suspension-servlet" method="post">
                <label for="inDescripcion" class="col-form-label">Mensaje</label>
                <textarea class="form-control" name="inDescripcion" id="inDescripcion"></textarea>
                <span>
                <button type="submit" class="btn btn-primary">Aceptar</button>
                </span>
            </form>
        </div>
    </div>
    </c:when>
    <c:otherwise>
        <h1>Tu usuario se encuentra activo </h1>
    </c:otherwise>
</c:choose>
</div>
</div>
</body>
</html>
