<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<script type="text/javascript" src="js/form/deliveryNote/pendingTransactions.js"></script>
<script type="text/javascript" src="js/form/modals.js"></script>
<script type="text/javascript">
	$(document).ready(function() {
		new PendingTransactions();
	});
</script>

<jsp:include page="../modals/modals.jsp" />
<jsp:include page="../modals/outputModal.jsp" />
<jsp:include page="../modals/orderModal.jsp" />
<jsp:include page="../modals/supplyingModal.jsp" />

<form action="" onsubmit="return false;">
	<div class="row">
		<div class="col-md-12 col-lg-12 form-group">
			<h3><spring:message code="common.pendingTransactions.label"/></h3>
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
	<div class="col-md-3 col-lg-2 col-md-offset-6 col-lg-offset-8">
		<button class="btn btn-info btn-block" type="submit" id="confirmDeliveryNotesWithoutInform">
			<span class="glyphicon glyphicon-log-in"></span>
			<spring:message code="common.updatedManually" />
		</button>
	</div>
	<div class="col-md-3 col-lg-2">
		<button class="btn btn-primary btn-block" type="submit" id="confirmDeliveryNotesButton">
			<span class="glyphicon glyphicon-ok"></span>
			<spring:message code="common.confirm" />
		</button>
	</div>
</div>
</form>

<div class="modal fade" data-backdrop="static" id="forcedDeliveryNoteConfirmationModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog" style="width:50%">
		<div class="modal-content">
			<div class="modal-body">
				<div class="row">
					<div class="col-md-12 col-lg-12 form-group">
						<strong><span style="color:red"><spring:message code="common.informedManuallyModal"/></span></strong>
					</div>
				</div>
				<div>
					<table class="table table-condensed table-hover table-striped" id="deliveryNotesAlreadyInformsTable">
						<thead>
							<tr>
								<th data-column-id="deliveryNoteAlreadyInformId" data-identifier="true"><spring:message code="common.number"/></th>
								<th data-column-id="deliveryNoteAlreadyInformTransactionCode"><spring:message code="common.transactionCodeDescription"/></th>
							</tr>
						</thead>
						<tbody id="deliveryNotesAlreadyInformsBody">
						</tbody>
					</table>
				</div>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal"><spring:message code="common.no"/></button>
				<button type="button" class="btn btn-primary" data-dismiss="modal" id="confirmDeliveryNotesWithoutInformModal"><spring:message code="common.yes"/></button>
			</div>
		</div>
	</div>
</div>

