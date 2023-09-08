<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: okkid-a
  Date: 9/8/23
  Time: 9:49 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Parametros</title>
    <jsp:include page="/includes/resources.jsp"/>
    <jsp:include page="/includes/header-admin.jsp"/>
    <jsp:include page="/areas/cliente/modal-resources.jsp"/>
</head>
<body>
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
<c:if test="${!empty(error)}">
    <div class="row">
        <div class="col-10">
            <div class="alert alert-warning alert-dismissible">
                <a class="close" data-dismiss="alert" data-bs-close="alert">&times;</a>
                    ${error}
            </div>
        </div>
    </div>
</c:if>
<div class="container rounded bg-white mt-5 mb-5 justify-content-center">
        <div class="row">
            <div class="col-md-5 border-right border-left">
                <div class="p-3 py-5">
                    <div class="d-flex justify-content-between align-items-center mb-3">
                        <h4 class="text-right">Parametros</h4>
                    </div>
                    <div class="row mt-3">
                        <form id="form-nuevocliente" action="${pageContext.request.contextPath}/admin/parametros-servlet?action=fecha" method="post">

                        <div class="col-md-12"><label class="labels">Fecha actual: ${fechaActual}</label>
                            <input type="date" id="fecha" name="fecha" value="${fechaActual}" min="${fechaActual}" max="2024-09-01" required/>
                        </div>

                            <div class="mt-5 text-center">
                                <button class="btn btn-primary profile-button" type="submit">Guardar Cambios</button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>

</body>
</html>
