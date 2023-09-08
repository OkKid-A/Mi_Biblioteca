<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%--
  Created by IntelliJ IDEA.
  User: okkid-a
  Date: 9/3/23
  Time: 7:52 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Incidencias</title>
    <jsp:include page="/includes/resources.jsp"/>
    <jsp:include page="/includes/header-recepcion.jsp"/>
    <jsp:include page="/areas/cliente/modal-resources.jsp"/>
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
            <div class="card form-group col"  style="width: 18rem;">
                <div class="card-body">
                    <h5 class="card-title">Detalles del Usuario </h5>
                    <p class="card-text" id="detalles"></p>
                </div>
                <ul class="list-group list-group-flush">
                    <li class="list-group-item">Usuario: ${cliente.cliente_id}</li>
                    <li class="list-group-item" id="nom">Nombre: ${cliente.nombre}</li>
                    <li class="list-group-item" id="usernam">Nombre de Usuario: ${cliente.username}</li>
                    <li class="list-group-item" id="saldo">Saldo: $${cliente.saldo}</li>
                </ul>
            </div>
            <div class="card mb-4 col">
                <div class="card-body text-center">
                    <h3 class="my-3">Reportar Incidencia</h3>
                    <p class="text-muted mb-1">Por favor reporta aqui cualquier incidente o perdida</p>
                    <p class="text-muted mb-4">Ten en cuenta que esto creara restricciones temporales en tu cuenta por dias</p>
                    <p class="text-muted mb-4">Asegurate de tener al menos $${libro.precio} de saldo para pagar por el libro.</p>
                    <div class="d-flex justify-content-center mb-2">
                        <form action="${pageContext.request.contextPath}/recepcion/incidencia-servlet?prestamo=${prestamoRes.id}&tipo=maltrato" method="post">
                            <input type="hidden" name="isbn" value="${prestamoRes.isbn}">
                            <input type="hidden" name="userID" value="${cliente.cliente_id}">
                            <button type="submit" class="btn btn-primary mr-3" >Reportar Maltrato</button>
                        </form>
                        <form action="${pageContext.request.contextPath}/recepcion/incidencia-servlet?prestamo=${prestamoRes.id}&tipo=perdida" method="post">
                            <input type="hidden" name="isbn" value="${prestamoRes.isbn}">
                            <input type="hidden" name="userID" value="${cliente.cliente_id}">
                            <button type="submit" class="btn btn-primary" >Reportar Perdida</button>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </c:if>
</div>
<hr>
<section>
    <h3>Listado de Prestamos Activos</h3>
    <div class="container-fluid">
    <div class="row p-2 pt-3 pb-3 d-flex justify-content-center">
        <div class="col-md-6">
            <div class="d-flex form-inputs">
                <select class="form-control col-2" id="filtro" required>
                    <option value="0">Filtrar por:</option>
                    <option value="0">Numero</option>
                    <option value="1">Libro</option>
                    <option value="2">Usuario</option>
                </select>
                <input class="form-control" id ="buscador" type="text" placeholder="Busca un prestamo">
            </div>
        </div>
    </div>
</div>
<div class="justify-content-center panel panel-default">
    <table id="listadoActivos" class="pt-4 table table-bordered table-striped" style="max-height: 300px" >
        <thead>
        <tr>
            <th>Numero</th>
            <th>Libro</th>
            <th>Usuario</th>
            <th>ISBN</th>
            <th>Fecha</th>
            <th>Estado</th>
            <th>Finalizar</th>
        </tr>
        </thead>
        <tbody>
        <c:if test="${!empty(prestamosActivos)}">
            <c:forEach var="prestamo" items="${prestamosActivos}" varStatus="status" >
                <tr>
                    <td><a href="${pageContext.request.contextPath}/recepcion/incidencia-servlet?prestamo=${prestamo.id}">
                            ${prestamo.id}</a></td>
                    <td>${prestamo.nombreLibro}</td>
                    <td>${prestamo.nombreUsuario}</td>
                    <td>${prestamo.isbn}</td>
                    <td>${prestamo.fecha_creacion}</td>
                    <td>${prestamo.estado}</td>
                    <td><form id="form-finalizar" action="${pageContext.request.contextPath}/recepcion/incidencia-servlet?prestamo=${prestamo.id}&action=completar" method="post">
                        <input type="hidden" name="isbn" value="${prestamo.isbn}">
                        <button class="btn btn-dark" type="submit" form="form-finalizar">Finalizar</button>
                    </form></td>
                </tr>
            </c:forEach>
        </c:if>
        </tbody>
    </table>
</div>
</section>
</body>
<script>
    $("#buscador").keyup(function(){
        _this = this;
        // Show only matching TR, hide rest of them
        $.each($("#listadoActivos tbody").find("tr"), function() {
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
