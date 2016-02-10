<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<script type="text/javascript" src="js/form/orderAssembly/orderManagement.js"></script>
<script type="text/javascript" src="js/form/modals.js"></script>
<script type="text/javascript">
	$(document).ready(function() {
		new OrderManagement();
	});
</script>

<jsp:include page="../modals/modals.jsp" />
<jsp:include page="../modals/orderModal.jsp" />

<form action="" onsubmit="return false;">
	<div class="row">
		<div class="col-md-12 col-lg-12 form-group">
			<c:if test="${cancellation == true}">
				<h3><spring:message code="orderAssembly.cancellation.label"/></h3>
			</c:if>
			<c:if test="${cancellation == false}">
				<h3><spring:message code="orderAssembly.reprint.label"/></h3>
			</c:if>
		</div>
		<input type="hidden" class="form-control" id="cancellation" value="${cancellation}">
	</div>
	<div class="row">
		<div class="col-md-8 col-lg-8 form-group">
			<label for="provisioningRequestSearch"><spring:message code="provisioningRequest.provisioningRequestNumber"/></label>
			<input type="text" class="form-control" name="provisioningRequestSearch" id="provisioningRequestSearch" >
		</div>
		<div class="col-md-2 col-lg-2 form-margin">
			<button class="btn btn-success btn-block" type="submit" id="searchButton">
				<span class="glyphicon glyphicon-search"></span>
				<spring:message code="common.search" />
			</button>
		</div>
		<div class="col-md-2 col-lg-2 form-margin">
			<button class="btn btn-info btn-block" type="submit" id="cleanButton">
				<span class="glyphicon glyphicon-trash"></span>
				<spring:message code="common.clean" />
			</button>
		</div>
	</div>

	<div id="divTable">
		<table class="table table-condensed table-hover table-striped" id="orderTable">
			<thead>
			<tr>
				<th data-column-id="orderId" data-type="numeric" data-identifier="true" data-visible="false"><spring:message code="common.orderAssembly"/></th>
				<th data-column-id="id" data-type="numeric"><spring:message code="provisioningRequest.provisioningRequestNumber"/></th>
				<th data-column-id="client"><spring:message code="common.client"/></th>
				<th data-column-id="agreement"><spring:message code="common.agreement"/></th>
				<sec:authorize access="hasRole('ORDER_LABEL_PRINT')">
				<th data-column-id="viewOrder" data-formatter="viewOrder" data-sortable="false"><spring:message code="common.action"/></th>
				</sec:authorize>
				<sec:authorize access="hasRole('ORDER_LABEL_PRINT')">
					<th data-column-id="printLabel" data-formatter="printLabel" data-sortable="false"><spring:message code="common.action"/></th>
				</sec:authorize>
			</tr>
			</thead>
			<tbody id="orderTableBody">
			</tbody>
		</table>
	</div>
	<br>
	<sec:authorize access="hasRole('ORDER_ASSEMBLY_CANCELLATION')">
		<c:if test="${cancellation == true}">
			<div class="row">
				<div class="col-md-3 col-md-offset-9 col-lg-2 col-lg-offset-10">
					<button class="btn btn-danger btn-block" type="submit" id="confirmButton">
						<span class="glyphicon glyphicon-ok"></span>
						<spring:message code="provisioningRequest.cancellation.button" />
					</button>
				</div>
			</div>
		</c:if>
	</sec:authorize>
</form>