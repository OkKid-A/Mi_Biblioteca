<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<jsp:useBean id="currentUser" scope="session" type="com.cunoc.mi_biblioteca.Usuarios.Usuario"/>
<jsp:useBean id="cliente" scope="session" type="com.cunoc.mi_biblioteca.Usuarios.Cliente.Cliente"/>
<%--
  Created by IntelliJ IDEA.
  User: okkid-a
  Date: 9/1/23
  Time: 11:15 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<h3>Listado de Prestamos Pendientes</h3>
<table id="listaPending" class="pt-4 table table-bordered table-striped" style="max-height: 300px" >
  <thead>
  <tr>
    <th>Numero</th>
    <th>Monto</th>
    <th>Tipo</th>
    <th>Fecha</th>
  </tr>
  </thead>
  <tbody>
  <c:if test="${!empty(transacciones)}">
    <c:forEach var="transaccion" items="${transacciones}" varStatus="status" >
      <tr>
        <td>${status.count}</td>
        <td>$<fmt:formatNumber type="number" maxFractionDigits="2" minFractionDigits="2" value="${transaccion.valor}"/></td>
        <td>${transaccion.estado}</td>
        <td>${transaccion.fecha}</td>
      </tr>
    </c:forEach>
  </c:if>
  </tbody>
</table>
