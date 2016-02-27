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

<jsp:include page="../modals/modals.jsp" />
<jsp:include page="../modals/outputModal.jsp" />
<jsp:include page="../modals/orderModal.jsp" />
<jsp:include page="../modals/supplyingModal.jsp" />

<form id="deliveryNoteCancellationForm" action="" onsubmit="return false;">
	<div class="row">
		<div class="col-md-12 col-lg-12 form-group">
			<h3><spring:message code="deliveryNote.cancellation.label"/></h3>
		</div>
	</div>
	<div class="row">
		<div class="col-md-8 col-lg-8 form-group">
			<label for="deliveryNoteNumberSearch"><spring:message code="common.deliveryNote"/></label>
			<div class="input-group">
				<input type="text" class="form-control" name="POSDeliveryNoteNumberSearch" id="POSDeliveryNoteNumberSearch" placeholder='<spring:message code="common.deliveryNote.POS"/>'>
				<span class="input-group-addon">-</span>
				<input type="text" class="form-control" name="deliveryNoteNumberSearch" id="deliveryNoteNumberSearch" placeholder='<spring:message code="common.deliveryNote.number"/>' >
			</div>
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
	<br>
	<div>
		<table class="table table-condensed table-hover table-striped" id="deliveryNoteTable">
			<thead>
		        <tr>
		        	<th data-column-id="id" data-header-css-class="idColumn" data-identifier="true"><spring:message code="common.id"/></th>
		            <th data-column-id="orderAssemblyOrOutputNumber"><spring:message code="deliveryNote.orderAssemblyOrOutputNumber"/></th>
		            <th data-column-id="class"><spring:message code="deliveryNote.class"/></th>
					<th data-column-id="date"><spring:message code="common.date"/></th>
					<th data-column-id="deliveryNoteNumbers"><spring:message code="deliveryNote.deliveryNoteNumbers"/></th>
		          	<th data-column-id="option" data-formatter="option" data-sortable="false"><spring:message code="common.option"/></th>
		        </tr>
	   	 	</thead>
	   	 	<tbody id="deliveryNoteTableBody">
			</tbody>
		</table>
	</div>
	<br>
	<div class="row">
		<div class="col-md-3 col-lg-2 col-md-offset-9 col-lg-offset-10">
			<button class="btn btn-danger btn-block" type="submit" id="confirmButton">
				<span class="glyphicon glyphicon-ok"></span>
				<spring:message code="deliveryNote.cancellation.button" />
			</button>
		</div>
	</div>
</form>

<%-- Aviso de consulta demasiado amplia --%>
<div class="modal fade" data-backdrop="static" id="queryTooLarge" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog" style="width:250px">
		<div class="modal-content">
			<div class="modal-body">
				<strong><span><spring:message code="common.queryTooLarge"/></span></strong>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-primary" data-dismiss="modal" id="acceptButton"><spring:message code="common.accept"/></button>
			</div>
		</div>
	</div>
</div>