<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<script type="text/javascript" src="js/form/input/inputCancellation.js"></script>
<script type="text/javascript" src="js/form/modals.js"></script>
<script type="text/javascript">
	$(document).ready(function() {
		new InputCancellation();
	});
</script>
<jsp:include page="../modals/inputModal.jsp" />

<form id="searchInputForm" action="" onsubmit="return false;">
	<div class="row">
		<div class="col-md-12 form-group">
			<h1><spring:message code="input.cancellation.label"/></h1>
		</div>
	</div>
	<div>
		<div class="row">
			<div class="col-md-4 form-group">
				<label for="idSearch"><spring:message code="input.input.number"/></label>
				<input id="idSearch" name="idSearch" class="form-control" >
			</div>
			<div class="col-md-4 form-group">
				<label for="dateFromSearch"><spring:message code="common.dateFrom"/></label>
				<div class="input-group">
					<input type="text" class="form-control" name="dateFromSearch" id="dateFromSearch">
					<span class="input-group-addon" id="dateFromButton" style="cursor:pointer;">
						<span class="glyphicon glyphicon-calendar"></span>
					</span>
				</div>
			</div>
			<div class="col-md-4 form-group">
				<label for="dateToSearch"><spring:message code="common.dateTo"/></label>
				<div class="input-group">
					<input type="text" class="form-control" name="dateToSearch" id="dateToSearch">
					<span class="input-group-addon" id="dateToButton" style="cursor:pointer;">
						<span class="glyphicon glyphicon-calendar"></span>
					</span>
				</div>
			</div>
		</div>
		
		<div class="row">
			<div class="col-md-4 form-group">
				<label for="conceptSearch"><spring:message code="common.concept"/></label>
				<select id="conceptSearch" name="conceptSearch" class="form-control chosen-select" data-placeholder="<spring:message code='common.select.option'/>">
					<option value=""></option>
					<c:forEach items="${concepts}" var="concept">
						<option value="${concept.id}"><c:out value="${concept.code}"></c:out> - <c:out value="${concept.description}"></c:out></option>
					</c:forEach>
				</select>
			</div>
			<div class="col-md-4 form-group">
				<label for="providerSearch"><spring:message code="common.provider"/></label>
				<select id="providerSearch" name="providerSearch" class="form-control chosen-select" data-placeholder="<spring:message code='common.select.option'/>">
					<option value=""></option>
					<c:forEach items="${providers}" var="provider">
						<option value="${provider.id}"><c:out value="${provider.code}"></c:out> - <c:out value="${provider.name}"></c:out></option>
					</c:forEach>
				</select>
			</div>
			<div class="col-md-4 form-group">
				<label for="deliveryLocationSearch"><spring:message code="common.client"/></label>
				<select id="deliveryLocationSearch" name="deliveryLocationSearch" class="form-control chosen-select" data-placeholder="<spring:message code='common.select.option'/>">
					<option value=""></option>
					<c:forEach items="${deliveryLocations}" var="deliveryLocation">
						<option value="${deliveryLocation.id}"><c:out value="${deliveryLocation.code}"></c:out> - <c:out value="${deliveryLocation.name}"></c:out></option>
					</c:forEach>
				</select>
			</div>

		</div>
		<div class="row">
			<div class="col-md-4 form-group">
				<label for="agreementSearch"><spring:message code="common.agreement"/></label>
				<select id="agreementSearch" name="agreementSearch" class="form-control chosen-select" data-placeholder="<spring:message code='common.select.option'/>">
					<option value=""></option>
					<c:forEach items="${agreements}" var="agreement">
						<option value="${agreement.id}"><c:out value="${agreement.code}"></c:out> - <c:out value="${agreement.description}"></c:out></option>
					</c:forEach>
				</select>
			</div>
			<div class="col-md-4 form-group">
				<label for="deliveryNoteNumberSearch"><spring:message code="common.deliveryNote"/></label>
				<input type="text" class="form-control" name="deliveryNoteNumberSearch" id="deliveryNoteNumberSearch" >
			</div>
			<div class="col-md-4 form-group">
				<label for="purchaseOrderNumberSearch"><spring:message code="common.purchaseOrder"/></label>
				<input type="text" class="form-control" name="purchaseOrderNumberSearch" id="purchaseOrderNumberSearch" >
			</div>
		</div>
		<div class="row">
			<div class="col-md-2 col-md-offset-8">
				<button class="btn btn-success btn-block" type="submit" id="searchButton">
				<span class="glyphicon glyphicon-search"></span>
				<spring:message code="common.search" />
				</button>
			</div>
			<div class="col-md-2">
				<button class="btn btn-info btn-block" type="submit" id="cleanButton">
				<span class="glyphicon glyphicon-trash"></span>
				<spring:message code="common.clean" />
				</button>
			</div>
		</div>
	</div>
	<br>
		<table class="table table-condensed table-hover table-striped" id="inputTable">
			<thead>
		        <tr>
		          	<th data-column-id="id" data-type="numeric" data-identifier="true"><spring:message code="common.id"/></th>
		            <th data-column-id="agreement"><spring:message code="common.agreement"/></th>
		            <th data-column-id="providerOrDeliveryLocation"><spring:message code="common.clientOrProvider"/></th>
		            <th data-column-id="concept"><spring:message code="common.concept"/></th>
		            <th data-column-id="date"><spring:message code="common.date"/></th>
		          	<th data-column-id="option" data-formatter="option" data-sortable="false"><spring:message code="common.option"/></th>
		        </tr>
	   	 	</thead>
	   	 	<tbody id="inputTableBody">
			</tbody>
		</table>
	<br>
	<div class="row">
		<div class="col-md-2 col-md-offset-10">
			<button class="btn btn-danger btn-block" type="submit" id="confirmButton">
				<span class="glyphicon glyphicon-ok"></span>
				<spring:message code="provisioningRequest.cancellation.button" />
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