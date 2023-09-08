<%--
  Created by IntelliJ IDEA.
  User: okkid-a
  Date: 9/8/23
  Time: 12:49 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Admin</title>
    <jsp:include page="/includes/resources.jsp"/>
    <jsp:include page="/includes/header-admin.jsp"/>
    <jsp:include page="/areas/cliente/modal-resources.jsp"/>
</head>
<body>
<div>
    <section class="header-main border-bottom bg-white">
        <div class="container-fluid">
            <div class="row p-2 pt-3 pb-3 d-flex justify-content-center">
                <div class="col-auto">
                    <div class="d-flex form-inputs justify-content-center">
                        <form id="form-reporte" action="${pageContext.request.contextPath}/admin/inicio-servlet" method="post">
                            <label for="filtro">Selecciona el tipo de reporte:</label>
                            <select class="form-control " id="filtro" name="filtro" required>
                                <option value="0" selected>Top 10 de libros mas prestados</option>
                                <option value="1">Top 5 usuarios de recepcion con mas prestamos finalizados</option>
                                <option value="2">Prestamos realizados en los ultimos 30 dias</option>
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
</body>
</html>
