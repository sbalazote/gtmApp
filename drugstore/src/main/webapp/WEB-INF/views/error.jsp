<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<script>
	/*$(document).ready(function() {
		$('#alertDiv').html(
			'<div class="alert alert-danger alert-block fade in">' +
			'<strong>Ha ocurrido un error al intentar procesar su solicitud. Por favor, comuníquese con el Administrador del Sistema.</strong></div>');
	});*/
</script>



<div class="row">
	<div class="col-md-12">
		<h1 class="text-danger text-center lead"><c:out value="${msg}"/></h1>
	</div>
</div>
<div class="row">
	<div class="col-md-12">
<c:choose>
    <c:when test="${error == 400}">
    	<img src="./images/error_400.png" class="center-block" height="256" width="256">
    </c:when>
    <c:when test="${error == 403}">
    	<img src="./images/error_403.png" class="center-block" height="256" width="256">
    </c:when>
    <c:when test="${error == 404}">
    	<img src="./images/error_404.png" class="center-block" height="256" width="256">
    </c:when>
    <c:when test="${error == 500}">
    	<img src="./images/error_500.png" class="center-block" height="256" width="256">
    </c:when>
    <c:otherwise></c:otherwise>
</c:choose>
</div>
</div>
