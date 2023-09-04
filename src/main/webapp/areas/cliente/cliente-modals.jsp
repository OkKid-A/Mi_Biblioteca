<%--
  Created by IntelliJ IDEA.
  User: okkid-a
  Date: 8/31/23
  Time: 9:16 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="modal fade" role="dialog" id="subsModal">
    <form id="form-subs" action="${pageContext.request.contextPath}/usuario/suscribe-servlet" method="post">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
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
            </div>
        </div>
    </form>
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
                    <input class="form-control" name="nuevoSaldo" id ="nuevoSaldo" type="number" step="0.01" required accept="int">
                </div>
                <div class="modal-footer">
                    <button type="submit" class="btn btn-primary" form="form-edit">Aceptar</button>

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

<jsp:useBean id="orden" scope="request" type="java.lang.String"/>
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