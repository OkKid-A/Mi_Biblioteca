<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: okkid-a
  Date: 9/4/23
  Time: 12:15 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Envios</title>
    <jsp:include page="/includes/resources.jsp"/>
    <jsp:include page="/includes/header-transporte.jsp"/>
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
<section>
    <h3>Listado de Transportes a Usuarios</h3>
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
    <div class="justify-content-center panel panel-default container">
        <table id="listadoActivos" class="pt-4 table table-bordered table-striped" style="max-height: 300px" >
            <thead>
            <tr>
                <th>Numero</th>
                <th>No Prestamo</th>
                <th>Usuario</th>
                <th>Libro</th>
                <th>Biblioteca de Origen</th>
                <th>Estado</th>
                <th>Cambiar Estado</th>
            </tr>
            </thead>
            <tbody>
            <c:if test="${!empty(entregas)}">
                <c:forEach var="entrega" items="${entregas}" varStatus="status" >
                    <tr>
                        <td>${entrega.id_encargo}</td>
                        <td>${entrega.id_prestamo}</td>
                        <td>${entrega.cliente_Nombre}</td>
                        <td>${entrega.nombre_libro}</td>
                        <td>${entrega.biblioteca}</td>
                        <td>${entrega.estadoEntrega}</td>
                        <td>
                            <c:choose>
                                <c:when test="${entrega.estadoEntrega == 'PENDIENTE'}">
                                    <form  action="${pageContext.request.contextPath}/transporte/inicio-servlet?entrega=${entrega.id_encargo}&estado=1" method="post">
                                        <button class="btn-dark btn" type="submit">
                                            En Camino
                                        </button>
                                    </form>
                                </c:when>
                                <c:when test="${entrega.estadoEntrega == 'EN_CAMINO'}">
                                    <form  action="${pageContext.request.contextPath}/transporte/inicio-servlet?entrega=${entrega.id_encargo}&estado=2" method="post">
                                        <input type="hidden" value="${entrega.id_prestamo}" name="prestamo">
                                        <button class="btn-dark btn" type="submit">
                                            Finalizar
                                        </button>
                                    </form>
                                </c:when>
                                <c:otherwise>
                                    Finalizado
                                </c:otherwise>
                            </c:choose>
                        </td>
                    </tr>
                </c:forEach>
            </c:if>
            </tbody>
        </table>
    </div>
</section>
</body>
</html>
