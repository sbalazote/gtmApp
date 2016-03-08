<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>


<form method="post" action="searchProvisioningById.do">
    <c:if test="${not empty error}">
    <div class="alert alert-danger" role="alert">
        <span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span>
        <strong>Error:</strong>
        ${error}
    </div>
    </c:if>
    <c:if test="${not empty success}">
        <div class="alert alert-success" role="alert">
            <span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span>
            <strong>Operacion Exitosa:</strong>
                ${success}
        </div>
    </c:if>

    <div class="row">
        <div class="col-md-12 col-lg-12 form-group">
            <label for="provisioningRequestId"><spring:message code="common.provisioningRequests.number"/></label>
            <input id="provisioningRequestId" type="input" class="form-control" name="provisioningRequestId" autosave="" results="5" incremental="incremental"/>
        </div>
    </div>
    <input type="submit" value="Confirmar">
</form>