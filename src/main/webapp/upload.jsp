<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: okkid-a
  Date: 9/4/23
  Time: 6:05 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <jsp:include page="/includes/resources.jsp"/>
    <jsp:include page="/areas/cliente/modal-resources.jsp"/>
</head>
<body>
<div class="pt-5">
    <div class="row">
        <div class="col-sm flex-column">
            <div class="container">
                <form class="was-validated" method="POST" action="../servlets/upload-servlet"
                      enctype="multipart/form-data">
                    <div class="form-group required">
                        <%--@declare id="archivo"--%><label class="text" for="archivo"> Selecciona tu Archivo de
                        Records</label>
                    </div>
                    <div class="form-group custom-file">
                        <input type="file" id="pathFile" name="pathFile" accept="text/csv, .json" required>
                        <label class="custom-file-label" for="pathFile">Escoge tu Archivo...</label>
                    </div>
                    <button type="submit" class="btn btn-dark btn-block">Subir</button>
                </form>
            </div>
        </div>
    </div>
    <div class="row">
        <div class="col-lg">
            <div>

            </div>
        </div>
    </div>
    <script>
        $('#pathFile').on('change', function () {
            var fileName = $(this).val();
            //replace the "Choose a file" label
            $(this).next('.custom-file-label').html(fileName);
        })
    </script>
</div>
</body>
</html>
