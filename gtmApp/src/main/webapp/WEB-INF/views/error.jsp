<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<style type="text/css">
h1 {
    font-family:'Roboto';
    font-weight: 400;
}
</style>

<c:set var="context" value="${pageContext.request.contextPath}"/>

<div class="row">
	<div class="col-md-12 col-lg-12">
		<h1 class="text-center">
			<c:choose>
				<c:when test="${not empty error}">
					<c:out value="${error}" /> - <c:out value="${msg}" />
				</c:when>
				<c:otherwise>
					ERROR - <c:out value="${msg}" />
				</c:otherwise>
			</c:choose>
		</h1>
	</div>
</div>
<div class="row">
	<div class="col-md-12 col-lg-12">
		<c:choose>
			<c:when test="${error == 400}">
				<img src="${context}/images/error_400.png" class="center-block" height="256" width="256">
			</c:when>
			<c:when test="${error == 403}">
				<img src="${context}/images/error_403.png" class="center-block" height="256" width="256">
			</c:when>
			<c:when test="${error == 404}">
				<img src="${context}/images/error_404.png" class="center-block" height="256" width="256">
			</c:when>
			<c:when test="${error == 500}">
				<img src="${context}/images/error_500.png" class="center-block" height="256" width="256">
			</c:when>
			<c:otherwise>
				<img src="${context}/images/generic_error.png" class="center-block" height="256" width="256">
			</c:otherwise>
		</c:choose>
	</div>
</div>
<br>
<div class="row">
	<div class="col-md-6 col-lg-6 col-md-offset-3 col-lg-offset-3">
  		<button type="button" class="btn btn-info btn-lg btn-block" onclick="location.href='${context}/home.do'" id="homeButton"><span class="glyphicon glyphicon-home"></span>	Volver al Men&uacute;</button>
  	</div>
</div>