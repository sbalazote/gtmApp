<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>


<form id="orderAssemblyForm" action="batchExpirationDateOrderAssembly.do" method="get">
    <input type="hidden" class="form-control" id="provisioningRequestId" name="provisioningRequestId" value="${provisioningRequestId}">
    <input type="hidden" class="form-control" id="provisioningRequestDetailId" name="provisioningRequestDetailId" value="${provisioningRequestDetailId}">

    <div class="row">
        <div class="col-md-3 form-group">
            <h3 id="provisioningRequestIdFormated" class="form-provisioningRequest-heading"><spring:message code="provisioningRequest.provisioningRequestNumber" />: <span style="color:blue">${provisioningRequestIdFormated != null ? provisioningRequestIdFormated : ''}</span></h3>
        </div>
    </div>
    <div class="row">
        <div class="col-md-3 form-group">
            <span class="form-provisioningRequest-heading">${productDescription}</span>
        </div>
    </div>

    <div class="row">
        <div class="col-md-2 col-lg-2 form-group">
            <label for="amount"><spring:message code="common.amount"/>: ${amount}</label>
            <input type="hidden" class="form-control" id="amount" name="amount" value="${amount}">
        </div>
        <div class="col-md-2 col-lg-2 form-group">
            <label for="assignAmount"><spring:message code="common.assignAmount"/>: ${amountAssign}</label>
            <input type="hidden" class="form-control" id="assignAmount" name="assignAmount" value="${amountAssign}">
        </div>
    </div>
    <div class="row">
        <div class="col-md-4 col-lg-4 form-group">
            <label for="stockInput"><spring:message code="common.stock"/></label>
            <select id="stockInput" name="stockInput" class="form-control chosen-select" data-placeholder="<spring:message code='common.select.option'/>" autofocus>
                <option value=""></option>
                <c:forEach items="${stock}" var="stock">
                    <option value="${stock.id}"><c:out value="${stock.batch}"></c:out> - <c:out value="${stock.expirationDate}"></c:out> - <c:out value="${stock.amount}"></c:out></option>
                </c:forEach>
            </select>
        </div>
    </div>
    <div class="row">
        <div class="col-md-4 col-lg-4 form-group">
            <label for="stockAmountInput"><spring:message code="common.amount"/></label>
            <input type="text" class="form-control" name="stockAmountInput" id="stockAmountInput">
        </div>
    </div>
    <br>
    <div class="row">
        <div class="col-md-2 col-lg-2 col-md-offset-8 col-lg-offset-8">
            <button class="btn btn-danger btn-block" id="abortButton"><span class="glyphicon glyphicon-remove"></span> <spring:message code="common.abort"/></button>
        </div>
        <div class="col-md-2 col-lg-2">
            <button type="submit" class="btn btn-success btn-block" id="confirmButton"><span class="glyphicon glyphicon-ok"></span> <spring:message code="common.confirm"/></button>
        </div>
    </div>
</form>