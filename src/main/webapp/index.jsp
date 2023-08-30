<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
  <title>Login</title>
  <jsp:include page="/includes/resources.jsp"/>
</head>
<style>
body{
  background-color: #4c9fc4;
}
</style>
<body>
<div class="pt-5" >
  <div class="container blue" >
    <h1 class="text-center" style="color: #ffffff"> Mi Biblioteca </h1>
    <div class="row">
      <div class="col-md-5 mx-auto">
        <div class="card-transparent card-body" style="background-color: #ffffff">
          <form id="submitForm" action="${pageContext.request.contextPath}login/elector-servlet" method="POST" data-parsley-validate=""
                data-parsley-errors-messages-disabled="true">
            <div class="form-group required">
              <label class="text" for="username" > Ingresa tu Usuario</label>
              <input type="text" class="form-control text" id="username" required name="username" style=
                      "color: black" placeholder="Usuario">
            </div>
            <div class="form-group required">
              <label class="d-flex flex-row align-items-center" for="password"> Ingresa tu Contraseña</label>
              <input type="password" class="form-control" required id="password" name="password" style=
                      "color: black" placeholder="Contraseña">
            </div>
            <div class="form-group pt-0">
              <button class="btn btn-primary btn-block" type="submit"> Iniciar Sesion</button>
              <a href="areas/upload.jsp" class="text-center">Subir Archivo de Records</a>
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
</body>
</html>