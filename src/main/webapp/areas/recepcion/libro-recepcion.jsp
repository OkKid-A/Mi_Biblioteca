<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: okkid-a
  Date: 9/2/23
  Time: 8:10 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Prestamo</title>
<jsp:include page="/includes/resources.jsp"/>
<jsp:include page="/includes/header-cliente.jsp"/>
</head>
<body>
<section class="py-5 mt-4">
    <h3 class="text-center" style="color: #000000"> Detalles del Libro </h3>
    <div class="container px-4 px-lg-5 my-5">
        <div class="row gx-4 gx-lg-3 align-items-center">
            <div class="col-md-6"><img class="card-img-top mb-5 mb-md-0" width="150" height="256"
                                       src="${pageContext.request.contextPath}/resources/book.png" alt=".."></div>
            <div class="col-md-6">
                <h1 class="display-5 fw-bolder">Nombre: ${libro.nombre}</h1>
                <div class="small mb-1">ISBN: ${libro.isbn}</div>
                <div class="fs-5 mb-5">
                    <span>Precio: $${libro.precio}</span>
                </div>
                <p class="lead">Genero: ${libro.genero}</p>
            </div>
        </div>
        <c:if test="${!empty(usuario)}">
            <div class="row gx-4 gx-lg-3 align-items-center">
                <table class="table table-bordered table-striped">
                    <thead>
                    <tr>
                        <th>ID</th>
                        <th>Usuario</th>
                        <th>Prestamos Activos</th>
                        <th>Subscripcion</th>
                        <th>Saldo</th>
                        <th>Cambiar<th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr>
                        <td>${usuario.id}</td>
                        <td>${usuario.nombre}</td>
                        <td>${usuario.prestAct}</td>
                        <td>${usuario.subscribed}</td>
                        <td>$${usuario.saldo}</td>

                                <td><a class="btn btn-dark mb-0" id="terminar" type="button" href="${pageContext.request.contextPath}/recepcion/prestamo-servlet?isbn=${libro.isbn}">
                                    Cambiar</a>
                                </td>
                    </tr>
                    </tbody>
                </table>
            </div>
            <div class="container">
                <h1 class="text-center" style="color: #000000"> Ordena Aqui </h1>
                <div class="container" >
                    <div class="pt-8 container-fluid justify-content-center">
                        <div class="ml-auto row-auto align-content-middle">
                            <form id="submitForm" action="${pageContext.request.contextPath}/recepcion/prestamo-servlet?isbn=${libro.isbn}&cliente=${usuario.id}" method="POST" data-parsley-validate=""
                                  data-parsley-errors-messages-disabled="true">
                                <div class="col-4 container align-content-center">
                                    <div class="row-auto required ">
                                        <label class="card" for="dias" >Selecciona cuantos dias tendras el libro</label>
                                        <select class="form-control" id="dias" name="dias" required>
                                            <option value="1" selected>1</option>
                                            <option value="2">2</option>
                                            <option value="3">3</option>
                                            <option value="4">4</option>
                                            <option value="5">5</option>
                                            <option value="6">6</option>
                                            <option value="7">7</option>
                                            <option value="8">8</option>
                                        </select>
                                    </div>
                                    <div class="row-auto" id="resumen" >
                                        <label class="d-flex text card">Resumen de Costo</label>
                                        <div class="card">
                                            <ul class="list-group list-group-flush">
                                                <li class="list-group-item">Libro: $${libro.precio}</li>
                                                <li class="list-group-item shadow" id="total">Total:
                                                    $<c:out value="${libro.precio}"/></li>
                                            </ul>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-4 container align-content-center" id="completar">
                                    <div class="card row-auto" >
                                        <h5 class="card-title">Detalles del Prestamo </h5>
                                        <ul class="list-group list-group-flush">
                                            <li class="list-group-item">Libro: ${libro.nombre}</li>
                                            <li class="list-group-item" id="diasNumero">Dias: 1</li>
                                            <li class="list-group-item" id="ordenTotal">Precio: $${libro.precio}</li>
                                            <li class="list-group-item">Biblioteca: ${recepcionista.puesto_biblioteca}</li>
                                        </ul>
                                </div>
                                <div class="ccol-4 container d-flex justify-content-center align-content-middle">
                                    <div class="card-body text-center row">
                                                <span class="align-middle">
                                                <button  class="btn btn-primary btn-dark" id="completo" type="submit">Completar orden</button>
                                                </span>
                                    </div>
                                </div>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </c:if>
    </div>
    <c:if test="${empty(usuario)}">
    <div id="selectUser">
        <h3 class="text-center" style="color: #000000"> Selecciona al Usuario </h3>
        <section class="header-main border-bottom bg-white">
            <div class="container-fluid">
                <div class="row p-2 pt-3 pb-3 d-flex justify-content-center">
                    <div class="col-md-6">
                        <div class="d-flex form-inputs">
                            <select class="form-control col-2" id="filtro" required>
                                <option value="1">Filtrar por:</option>
                                <option value="1">Usuario</option>
                                <option value="0">ID</option>
                            </select>
                            <input class="form-control" id ="findUser" type="text" placeholder="Busca una solicitud...">
                            <button class="btn btn-dark px-3">
                                <i class="fa fa-search"></i>
                            </button>
                        </div>
                    </div>
                </div>
            </div>
            <div class="container">
                <table id="listUser" class="table table-bordered table-striped">
                    <thead>
                    <tr>
                        <th>ID</th>
                        <th>Usuario</th>
                        <th>Prestamos Activos</th>
                        <th>Subscripcion</th>
                        <th>Saldo</th>
                        <th>Seleccionar<th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="cliente" items="${clientes}" varStatus="status" >
                        <tr>
                            <td>${cliente.id}</td>
                            <td>${cliente.nombre}</td>
                            <td>${cliente.prestAct}</td>
                            <td>${cliente.subscribed}</td>
                            <td>$${cliente.saldo}</td>
                            <td>
                                <c:choose>
                                    <c:when test="${cliente.suspendido}">
                                        Esta suspendido
                                    </c:when>
                                    <c:when test="${cliente.valido}">
                                            <a class="btn btn-dark mb-0" id="finalizar"
                                            href="${pageContext.request.contextPath}/recepcion/prestamo-servlet?isbn=${libro.isbn}&cliente=${cliente.id}">Seleccionar</a>
                                    </c:when>
                                    <c:otherwise>
                                        <c:out value="Invalido"/>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </section>
    </div>
    </c:if>

</section>
</body>
<script>
    $("#findUser").keyup(function(){
        _this = this;
        // Show only matching TR, hide rest of them
        $.each($("#listUser tbody").find("tr"), function() {
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
