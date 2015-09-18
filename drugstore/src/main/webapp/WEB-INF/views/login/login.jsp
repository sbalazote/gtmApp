<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<script type="text/javascript" src="js/form/login/login.js"></script>
<script type="text/javascript">
	$(document).ready(function() {
		new Login();
	});
</script>

<form id="login" action="j_spring_security_check" method="post" >
	<c:if test="${not empty error}">
        <div class="row">
            <div class="form-group col-md-12 col-lg-12 text-center myAlert">
            </div>
        </div>
        <script type="text/javascript">
            toastr.options = {
                "closeButton": false,
                "debug": false,
                "newestOnTop": false,
                "progressBar": true,
                "preventDuplicates": false,
                "onclick": null,
                "showDuration": "300",
                "hideDuration": "1000",
                "timeOut": 3000,
                "showEasing": "swing",
                "hideEasing": "linear",
                "showMethod": "fadeIn",
                "hideMethod": "fadeOut",
                "target": ".myAlert"
            }
            // Muestro mensaje de error, con un titulo.
            toastr.error('${error}', 'ERROR!');
        </script>
	</c:if>

    <div class="row">
        <div class="form-group col-md-4 col-md-offset-4 col-lg-4 col-lg-offset-4 text-center">
            <img class="text-center" src="${logoPath}" class="img-responsive" alt="Responsive image">
        </div>
    </div>

    <div class="row">
        <div class="form-group col-md-12 col-lg-12 text-center">
            <h3 style="color: #0077b3; font-weight: bold">${name}</h3>
        </div>
    </div>

    <div class="row">
        <div class="form-group col-md-12 col-lg-12 text-center">
            <h3 style="color: #0077b3; font-weight: bold">${softwareName}</h3>
        </div>
    </div>

    <c:if test="${loginDisabled == null}">
        <br/>
        <br/>
        <div class="row">
            <div class="form-group col-md-4 col-md-offset-4 col-lg-4 col-lg-offset-4">
                <div class="input-group input-group-lg">
                    <span class="input-group-addon glyphicon glyphicon-user" aria-hidden="true"></span>
                    <input type="text" class="form-control" name="j_username" placeholder="<spring:message code="login.user.placeholder"/>" autocomplete="off" autofocus >
                </div>
            </div>
        </div>
        <div class="row">
            <div class="form-group col-md-4 col-md-offset-4 col-lg-4 col-lg-offset-4">
                <div class="input-group input-group-lg">
                    <span class="input-group-addon glyphicon glyphicon-lock" aria-hidden="true"></span>
                    <input type="password" class="form-control" name="j_password" placeholder="<spring:message code="login.password.placeholder"/>" autocomplete="off" >
                </div>
            </div>
        </div>
        <br>
        <div class="row">
            <div class="form-group col-md-4 col-md-offset-4 col-lg-4 col-lg-offset-4" >
                <button  class="btn btn-primary btn-lg btn-block" type="submit"><spring:message code="login.login"/></button>
            </div>
        </div>
    </c:if>
</form>
