<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<script type="text/javascript" src="js/form/deliveryNote/deliveryNoteCancellation.js"></script>
<script type="text/javascript" src="js/form/modals.js"></script>
<script type="text/javascript">
	$(document).ready(function() {
		new DeliveryNoteCancellation();
	});
</script>

<jsp:include page="../modals/outputModal.jsp" />
<jsp:include page="../modals/orderModal.jsp" />
<jsp:include page="../modals/supplyingModal.jsp" />

<form action="" onsubmit="return false;">
	<div class="row">
		<div class="col-md-12 form-group">
			<h3><spring:message code="deliveryNote.cancellation.label"/></h3>
		</div>
	</div>
	
	<div>
		<table class="table table-condensed table-hover table-striped" id="deliveryNoteTable">
			<thead>
		        <tr>
		        	<th data-column-id="id" data-identifier="true"><spring:message code="common.id"/></th>
		            <th data-column-id="orderAssemblyOrOutputNumber"><spring:message code="deliveryNote.orderAssemblyOrOutputNumber"/></th>
		            <th data-column-id="class"><spring:message code="deliveryNote.class"/></th>
		            <th data-column-id="deliveryNoteNumbers"><spring:message code="deliveryNote.deliveryNoteNumbers"/></th>
		          	<th data-column-id="option" data-formatter="option" data-sortable="false"><spring:message code="common.option"/></th>
		        </tr>
	   	 	</thead>
	   	 	<tbody id="deliveryNoteTableBody">
				<c:forEach items="${orderDeliveryNotes}" var="orderDeliveryNotes">
					<tr>
						<td><c:out value="A${orderDeliveryNotes.key}"></c:out></td>
						<td><c:out value="${orderDeliveryNotes.key}"></c:out></td>
						<td><spring:message code="deliveryNote.class.orderAssembly"/></td>
						<td><c:out value="${orderDeliveryNotes.value}"></c:out></td>
						<td><spring:message code="common.view"/></td>
					</tr>
				</c:forEach>
				
				<c:forEach items="${outputDeliveryNotes}" var="outputDeliveryNotes">
					<tr>
						<td><c:out value="E${outputDeliveryNotes.key}"></c:out></td>
						<td><c:out value="${outputDeliveryNotes.key}"></c:out></td>
						<td><spring:message code="deliveryNote.class.output"/></td>
						<td><c:out value="${outputDeliveryNotes.value}"></c:out></td>
						<td><spring:message code="common.view"/></td>
					</tr>
				</c:forEach>
				
				<c:forEach items="${supplyingDeliveryNotes}" var="supplyingDeliveryNotes">
					<tr>
						<td><c:out value="D${supplyingDeliveryNotes.key}"></c:out></td>
						<td><c:out value="${supplyingDeliveryNotes.key}"></c:out></td>
						<td><spring:message code="deliveryNote.class.supplying"/></td>
						<td><c:out value="${supplyingDeliveryNotes.value}"></c:out></td>
						<td><spring:message code="common.view"/></td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>
	<br>
	<div class="row">
		<div class="col-md-2 col-md-offset-10">
			<button class="btn btn-danger btn-block" type="submit" id="confirmButton">
				<span class="glyphicon glyphicon-ok"></span>
				<spring:message code="deliveryNote.cancellation.button" />
			</button>
		</div>
	</div>
</form>
