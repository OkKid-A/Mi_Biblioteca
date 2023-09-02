
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Perfil</title>
    <jsp:include page="/includes/resources.jsp"/>
    <jsp:include page="/includes/header-cliente.jsp"/>
    <jsp:include page="modal-resources.jsp"/>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.4/jquery.min.js"></script>
</head>

<body>
<div class="pt-8 container-fluid justify-content-center">
    <div class="ml-auto row">
        <div class="col-lg-4 align-content-center">
            <div class="card mb-4">
                <div class="card-body text-center">
                    <h3 class="my-3">Subscripcion Premium</h3>
                    <c:choose>
                        <c:when test="${cliente.subscrito}">
                            <p class="text-muted mb-1">Gracias por ser un usuario premium</p>
                            <button type="button" class="btn btn-primary" data-toggle="modal" data-target="#subsModal">Cancelar</button>
                        </c:when>
                        <c:otherwise>
                            <p class="text-muted mb-1">Aun no eres un usuario premium</p>
                            <p class="text-muted mb-4">Subscribete ahora para disfrutar de multiples ventajas</p>
                            <div class="d-flex justify-content-center m
                            b-2">
                                <button type="button" class="btn btn-primary" data-toggle="modal" data-target="#subsModal">Suscribete</button>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
            <div class="card mb-4">
                <div class="card-body text-center">
                    <h3 class="my-3">Balance y Recarga de Saldo</h3>
                    <p class="text-muted mb-4">Saldo actual: $${cliente.saldo}</p>
                    <div class="d-flex justify-content-center mb-2">
                        <button type="button" class="btn btn-primary mr-5" data-toggle="modal" data-target="#modalSaldo">Recargar</button>
                        <button type="button" class="btn btn-primary" data-toggle="modal" data-target="#modalHistory">Historial</button>
                    </div>
                </div>
            </div>
        </div>
        <div class=" col-lg-6 card mb-4 ml-4">
            <div class="card-body">
                <div class="row">
                    <div class="col-sm-3">
                        <p class="mb-0">Nombre</p>
                    </div>
                    <div class="col-sm-5">
                        <p class="text-muted mb-0">${currentUser.nombre}</p>
                    </div>
                    <div class="col-sm-3">
                        <p class="btn btn-dark mb-0" id="nombre" data-toggle="modal" data-target="#modalCambio">
                            Editar</p>
                    </div>
                </div>
                <hr>
                <div class="row">
                    <div class="col-sm-3">
                        <p class="mb-0">Username</p>
                    </div>
                    <div class="col-sm-5">
                        <p class="text-muted mb-0">${currentUser.username}</p>
                    </div>
                    <div class="col-sm-3">
                        <p class="btn btn-dark mb-0" id="username" data-toggle="modal" data-target="#modalCambio" >
                            Editar</p>
                    </div>
                </div>
                <hr>
                <div class="row">
                    <div class="col-sm-3">
                        <p class="mb-0">Email</p>
                    </div>
                    <div class="col-sm-5">
                        <p class="text-muted mb-0">${currentUser.correo}</p>
                    </div>
                    <div class="col-sm-3">
                        <p class="btn btn-dark mb-0"  id="correo" data-toggle="modal" data-target="#modalCambio" >
                            Editar</p>
                    </div>
                </div>
                <hr>
                <div class="row">
                    <div class="col-sm-3">
                        <p class="mb-0">Password</p>
                    </div>
                    <div class="col-sm-5">
                        <p class="text-muted mb-0">**************</p>
                    </div>
                    <div class="col-sm-3">
                        <p class="btn btn-dark mb-0" id="password" data-toggle="modal" data-target="#modalCambio" >
                            Editar</p>
                    </div>
                </div>
                <hr>
                <div class="row">
                    <div class="col-sm-3">
                        <p class="mb-0">Estado</p>
                    </div>
                    <div class="col-sm-5">
                        <p class="text-muted mb-0"><c:if test="${cliente.suspendido}">
                            <c:out value="Suspendido"/>
                                                    </c:if>
                        <c:if test="${!cliente.suspendido}">
                            <c:out value="Activo"/>
                        </c:if>
                        </p>
                    </div>
                    <div class="col-sm-3">
                        <c:if test="${cliente.suspendido}">
                            <p class="btn btn-dark mb-0" type="button" href="${pageContext.request.contextPath}/usuario/suspension-servlet" >
                                Disputar</p>
                        </c:if>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<div>
    <div class="modal fade" role="dialog" tabindex="-1" id="subsModal"  aria-hidden="true">
        <form id="form-subs" action="${pageContext.request.contextPath}/usuario/suscribe-servlet" method="post">
            <div class="modal-dialog" role="document">
                <div class="modal-content">
                    <c:choose>
                        <c:when test="${cliente.subscrito}">
                            <div class="modal-header">
                                <h5 class="modal-title">Cancela tu Subscricion</h5>
                                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                    <span aria-hidden="true">&times;</span>
                                </button>
                            </div>
                            <div class="modal-body">
                                <p>-Recuerda que es no reembolsable</p>
                            </div>
                            <div class="modal-footer">
                                <button type="button" class="close btn btn-dark" data-dismiss="modal" >Cancelar</button>
                                <button type="submit" class="btn btn-primary" form="form-subs">Aceptar</button>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <div class="modal-header">
                                <h5 class="modal-title">Subscribete a Premium</h5>
                                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                    <span aria-hidden="true">&times;</span>
                                </button>
                            </div>
                            <div class="modal-body">
                                <p>-Podras acceder a 8 libros al mismo tiempo</p>
                                <p>-Podras prestar un libro por 14 dias a la vez</p>
                                <p>-Tendras un 40% de descuento en tus envios</p>
                                <p>-Podras reservar libros</p>
                                <p>-Por tan solo $25.00</p>
                            </div>
                            <div class="modal-footer">
                                <button type="button" class="close btn btn-dark" data-dismiss="modal" >Cancelar</button>
                                <button type="submit" class="btn btn-primary" form="form-subs">Aceptar</button>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </form>
    </div>
    <div class="modal fade" role="dialog" id="modalHistory">
            <div class="modal-dialog" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">Transacciones</h5>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div class="modal-body">
                      <jsp:include page="transaccion-tabla.jsp"/>
                    </div>
                </div>
            </div>
    </div>
    <div class="modal fade" role="dialog" id="modalSaldo">
        <form id="form-saldo" action="${pageContext.request.contextPath}/usuario/suscribe-servlet" method="POST">
            <div class="modal-dialog" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">Recargar Saldo</h5>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div class="modal-body">
                        <p>Saldo Actual: $${cliente.saldo}</p>
                        <label for="nuevoSaldo">Ingresa la cantidad que deseas sumar</label>
                        <input class="form-control" name="nuevoSaldo" id ="nuevoSaldo" type="text" inputmode="decimal" pattern="[0-9]+([\.,][0-9]+)?">
                    </div>
                    <div class="modal-footer">
                        <button type="submit" class="btn btn-primary" form="form-saldo">Aceptar</button>

                    </div>
                </div>
            </div>
        </form>
    </div>
    <div class="modal fade" role="dialog" id="modalCambio">
        <form id="form-edit" action="${pageContext.request.contextPath}/usuario/inicio-servlet?accion=${tipoEdit}" method="POST">
            <div class="modal-dialog" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="tituloCambio">Ingresa el nuevo </h5>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div class="modal-body">
                        <input class="form-control" name="nuevoDato" id ="nuevoDato" type="text">
                    </div>
                    <div class="modal-footer">
                        <button type="submit" class="btn btn-primary" form="form-edit">Aceptar</button>

                    </div>
                </div>
            </div>
        </form>
    </div>

    <c:if test="${!empty(orden)}">
        <div class="modal fade" role="dialog" id="myModal">
            <div class="modal-dialog" role="document">
                <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title">Solicitud# ${orden}</h5>
                            <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                <span aria-hidden="true">&times;</span>
                            </button>
                        </div>
                        <div class="modal-body">
                            <h1 class="modal-dialog">Tu solicitud se ha procesado con exito!</h1>
                        </div>
                        <div class="modal-footer">
                            <button type="submit" class="btn btn-primary" >Aceptar</button>
                        </div>
                </div>
            </div>
        </div>
    </c:if>
</div>
</body>
<script type="text/javascript">
    $(document).ready(function (){
        <c:set var="tipoEdit" value="error"/>
        $("#username").on('click',function (){
           $("#tituloCambio").text("Ingresa el nuevo Username:")
            $("#form-edit").attr("action","${pageContext.request.contextPath}/usuario/inicio-servlet?accion=username")
        });
        $("#nombre").on('click',function (){
            $("#tituloCambio").text("Ingresa el nuevo Nombre:")
            $("#form-edit").attr("action","${pageContext.request.contextPath}/usuario/inicio-servlet?accion=nombre")
        });
        $("#correo").on('click',function (){
            $("#tituloCambio").text("Ingresa el nuevo Correo Electronico:")
            $("#form-edit").attr("action","${pageContext.request.contextPath}/usuario/inicio-servlet?accion=correo")
        });
        $("#password").on('click',function (){
            $("#tituloCambio").text("Ingresa la nueva password:")
            $("#form-edit").attr("action","${pageContext.request.contextPath}/usuario/inicio-servlet?accion=password")
        });
    });

</script>
</html>
