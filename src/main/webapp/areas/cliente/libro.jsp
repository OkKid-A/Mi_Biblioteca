<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: okkid-a
  Date: 8/28/23
  Time: 5:31 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>${libro.nombre}</title>
    <jsp:include page="/includes/resources.jsp"/>
    <jsp:include page="/includes/header-cliente.jsp"/>
</head>
<body>
<body>
<section class="py-5">
    <div class="container px-4 px-lg-5 my-5">
        <div class="row gx-4 gx-lg-3 align-items-center">
            <div class="col-md-6"><img class="card-img-top mb-5 mb-md-0" src="https://dummyimage.com/600x700/dee2e6/6c757d.jpg" alt="..." /></div>
            <div class="col-md-6">
                <h1 class="display-5 fw-bolder">${libro.nombre}</h1>
                <div class="small mb-1">ISBN: ${libro.isbn}</div>
                <div class="fs-5 mb-5">
                    <span>$${libro.precio}</span>
                </div>
                <p class="lead">Genero: ${libro.genero}</p>
            </div>
        </div>
    </div>
    <h1 class="text-center" style="color: #000000"> Ordena Aqui </h1>
    <div class="container">
        <table id="locations" class="table table-bordered table-striped">
            <thead>
            <tr>
                <th>No.</th>
                <th>Nombre</th>
                <th>Direccion</th>
                <th>Existencia</th>
                <th>Prestar</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="existencia" items="${existencias}" varStatus="status" >
                <tr>
                    <td>${existencia.id}</td>
                    <td>${existencia.nombre}</td>
                    <td>${existencia.direccion}</td>
                    <td>${existencia.cantidad}</td>
                    <td>
                        <c:choose>
                            <c:when test="${cliente.suspendido}">
                                Estas suspendido
                            </c:when>
                            <c:when test="${valido}">
                                <c:choose>
                                <c:when test="${existencia.cantidad>0}">
                                    <a class="btn btn-dark" href="${pageContext.request.contextPath}/usuario/checkout-servlet?isbn=${libro.isbn}&biblioteca=${existencia.id}">
                                        Prestar</a>
                                </c:when>
                                <c:otherwise>
                                    Actualmente no disponible
                                </c:otherwise>
                                </c:choose>
                            </c:when>
                            <c:otherwise>
                                Alcanzaste el limite de prestamos
                            </c:otherwise>
                        </c:choose>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
</section>

<!-- Footer-->
<footer class="py-5 bg-dark">
    <div class="container"><p class="m-0 text-center text-white">Copyright &copy; Your Website 2023</p></div>
</footer>
<!-- Bootstrap core JS-->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js"></script>
<!-- Core theme JS-->
<script src="js/scripts.js"></script>
</body>
</body>
</html>
