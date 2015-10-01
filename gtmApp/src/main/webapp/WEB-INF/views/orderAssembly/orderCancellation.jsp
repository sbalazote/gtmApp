<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<script type="text/javascript" src="js/form/orderAssembly/orderCancellation.js"></script>
<script type="text/javascript" src="js/form/modals.js"></script>
<script type="text/javascript">
	$(document).ready(function() {
		new OrderCancellation();
	});
</script>

<jsp:include page="../modals/modals.jsp" />
<jsp:include page="../modals/orderModal.jsp" />

<form action="" onsubmit="return false;">
	<div class="row">
		<div class="col-md-12 col-lg-12 form-group">
			<h3><spring:message code="orderAssembly.orderCancelation"/></h3>
		</div>
	</div>

	<div id="divTable">
		<table class="table table-condensed table-hover table-striped" id="orderTable">
			<thead>
			<tr>
				<th data-column-id="id" data-type="numeric" data-identifier="true"><spring:message code="common.id"/></th>
				<th data-column-id="client"><spring:message code="common.client"/></th>
				<th data-column-id="agreement"><spring:message code="common.agreement"/></th>
				<th data-column-id="action" data-formatter="action" data-sortable="false"><spring:message code="common.action"/></th>
			</tr>
			</thead>
			<tbody id="orderTableBody">
			<c:forEach items="${orders}" var="order" varStatus="status">
				<tr>
					<td><c:out value="${order.id}"></c:out></td>
					<td><c:out value="${order.provisioningRequest.client.name}"></c:out></td>
					<td><c:out value="${order.provisioningRequest.agreement.description}"></c:out></td>
					<td><spring:message code="common.view"/></td>
				</tr>
			</c:forEach>
			</tbody>
		</table>
	</div>
	<br>
	<div class="row">
		<div class="col-md-3 col-md-offset-9 col-lg-2 col-lg-offset-10">
			<button class="btn btn-danger btn-block" type="submit" id="confirmButton">
				<span class="glyphicon glyphicon-ok"></span>
				<spring:message code="provisioningRequest.cancellation.button" />
			</button>
		</div>
	</div>
</form>