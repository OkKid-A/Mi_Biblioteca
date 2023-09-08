<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: okkid-a
  Date: 9/3/23
  Time: 5:20 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Creacion</title>
    <jsp:include page="/includes/resources.jsp"/>
    <jsp:include page="/includes/header-recepcion.jsp"/>
    <jsp:include page="/areas/cliente/modal-resources.jsp"/>
</head>
<body>
<c:if test="${!empty(error)}">
    <div class="container row">
        <div class="col-10">
            <div class="alert alert-warning alert-dismissible" >
                <a class="close" data-dismiss="alert">x</a>
                    ${error}
            </div>
        </div>
    </div>
</c:if>
<c:if test="${!empty(success)}">
    <div class="row">
        <div class="col-10">
            <div class="alert alert-success alert-dismissible">
                <a class="close" data-dismiss="alert" data-bs-close="alert">&times;</a>
                    ${success}
            </div>
        </div>
    </div>
</c:if>
<div class="container rounded bg-white mt-5 mb-5 justify-content-center">
    <form id="form-nuevocliente" action="${pageContext.request.contextPath}/recepcion/add-cliente-servlet" method="post">
        <div class="row">
    <div class="col-md-5 border-right border-left">
        <div class="p-3 py-5">
            <div class="d-flex justify-content-between align-items-center mb-3">
                <h4 class="text-right">Crea un Nuevo Cliente:</h4>
            </div>
            <div class="row mt-3">
                <div class="col-md-12"><label class="labels">Nombre</label>
                    <input type="text" class="form-control" name="name" id="name" value="" maxlength="60">
                </div>
                <div class="col-md-12"><label class="labels">Nombre de usuario</label>
                    <input type="text" class="form-control" name="username" id="username" value="" maxlength="45">
                </div>
                <div class="col-md-12"><label class="labels">Email</label>
                    <input type="text" class="form-control" name="email" id="email" value="">
                </div>
                <div class="col-md-12"><label class="labels">Contraseña</label>
                    <input type="password" class="form-control" name="password" id="password" value="" maxlength="32">
                </div>
                <div class="col-md-12"><label class="labels">Saldo Inicial</label>
                    <input class="form-control" name="saldo" id ="saldo" type="text" inputmode="decimal" pattern="[0-9]+([\.,][0-9][0-9])?">
                </div>
            </div>
            <div class="mt-5 text-center">
                <button class="btn btn-primary profile-button" type="submit">Guardar Cambios</button>
            </div>
        </div>
    </div>
    </div>
    </form>
</div>
</body>
</html>
