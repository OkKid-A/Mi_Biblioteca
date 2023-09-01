<%--@elvariable id="orden" type="java.lang.String"--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: okkid-a
  Date: 8/28/23
  Time: 11:41 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Checkout</title>
    <jsp:include page="/includes/resources.jsp"/>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.4/jquery.min.js"></script>

</head>
<body>
<div class="pt-5" >
    <div class="container blue" >
        <div class="row">
            <div class="col-md-7 mx-auto">
                <div class="card-body d-flex " style="background-color: #ffffff">
                    <form id="submitForm" action="${pageContext.request.contextPath}/usuario/checkout-servlet?isbn=${libro.isbn}&biblioteca=<%out.print(request.getParameter("biblioteca"));%>" method="POST" data-parsley-validate=""
                          data-parsley-errors-messages-disabled="true">
                        <div class="form-group required id-11">
                            <label class="text card" for="dias" >Selecciona cuantos dias tendras el libro</label>
                            <select class="form-control col-2" id="dias" name="dias" required>
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
                        <div class="form-group">
                            <label class="text card" for="seleccionEntrega"> Selecciona el metodo de entrega</label>
                            <div class="form-check required " id="seleccionEntrega">
                                <div >
                                <input class="form-check-input" type="radio" name="tipoEnvio" id="domicilio" value="domicilio" checked>
                                <label class="form-check-label" for="domicilio">
                                    Entrega a Domicilio.
                                </label>
                                </div>
                                <div>
                                <input class="form-check-input" type="radio" name="tipoEnvio" id="recepcion" value="recepcion" >
                                <label class="form-check-label" for="recepcion">
                                   Recoger en Biblioteca.
                                </label>
                                </div>
                            </div>
                        </div>
                        <div class=" form-group" id="resumen" >
                                <div class=" row-md-1">
                                    <label class="d-flex text card">Resumen de Costo</label>
                                </div>
                                <div class="row">
                                    <div class="card " style="width: 18rem;">
                                        <div>
                                            <ul class="list-group list-group-flush">
                                                <li class="list-group-item">Libro: $${libro.precio}</li>
                                                <li class="list-group-item" id="envioPrecio">Envio: $${subscripcion.envio}</li>
                                                <li class="list-group-item shadow" id="total">Total:
                                               $<c:out value="${libro.precio + subscripcion.envio}"/></li>

                                            </ul>
                                        </div>
                                    </div>
                                </div>
                        </div>
                        <div class=" form-group d-flex justify-content-center" id="completar">
                            <div class="card form-group row"  style="width: 18rem;">
                                <div class="card-body">
                                    <h5 class="card-title">Detalles del Prestamo </h5>
                                    <p class="card-text" id="textoTipoPrestamo"></p>
                                </div>
                                <ul class="list-group list-group-flush">
                                    <li class="list-group-item">Libro: $${libro.nombre}</li>
                                    <li class="list-group-item" id="diasNumero">Dias: 1</li>
                                    <li class="list-group-item" id="ordenTotal">Precio: $${libro.precio}</li>
                                    <li class="list-group-item">Biblioteca: <%out.print(request.getParameter("biblioteca"));%></li>
                                </ul>
                                <div class="card-body text-center">
                                    <span class="align-middle">
                                    <button  class="btn btn-primary btn-dark" id="completo" data-toggle="modal" data-target="#exampleModal">Completar orden</button>
                                    </span>
                                </div>
                            </div>
                        </div>
                    </form>
                </div>
        </div>
    </div>
</div>
</div>
<div class="pb-5 text-center">
    <label class="text-align-center copyright" style="color: #ffffff"> @ 2023 Gomu Gomu no Code</label>
</div>
    <script>
        $(document).ready(function () {
            $("input[name = tipoEnvio]").on("click", function (){
                var seleccionado = $("input[name = tipoEnvio]:checked").val();
                if (seleccionado == "domicilio"){
                    $("#completar").hide();
                    $("#envioPrecio").show();
                    $("#total").text("Total: $" + <c:out value="${libro.precio + subscripcion.envio}"/>)
                    $("#ordenTotal").text("Total: $" + <c:out value="${libro.precio + subscripcion.envio}"/>)
                } else {
                    $("#completar").show();
                    $("#envioPrecio").hide();
                    $("#total").text("Total: $" + <c:out value="${libro.precio}"/>)
                    $("#ordenTotal").text("Total: $" + <c:out value="${libro.precio}"/>)
                }
            });
            $("#completo").on("click",function (){
                $("#myModal").modal('show');
            })
            $(window).on("load",function (){
                ("div.id-11 select").val("1").change();
                $("input[name = tipoEnvio]:checked").select();

            })
            $("#dias").on("change", function (){
                var selected = document.getElementById("dias")
                var diasNum = document.getElementById("diasNumero");
                $("#diasNumero").text("Dias: " + this.value);
            })
        });
    </script>
</body>
</html>