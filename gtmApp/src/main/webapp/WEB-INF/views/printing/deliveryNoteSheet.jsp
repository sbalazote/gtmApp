<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<script type="text/javascript" src="js/form/printing/deliveryNoteSheet.js"></script>
<script type="text/javascript" src="js/form/modals.js"></script>
<script type="text/javascript">
	$(document).ready(function() {
		new DeliveryNoteSheet();
	});
</script>

<jsp:include page="../modals/modals.jsp" />
<jsp:include page="../modals/orderModal.jsp" />

<div class="row">
	<div class="col-md-9 col-lg-9 form-group">
		<h3><spring:message code="administration.deliveryNoteSheet" /></h3>
	</div>
</div>

<div class="row">
	<div class="col-md-4 col-lg-4 form-group">
		<label for="idSearch"><spring:message code="provisioningRequest.provisioningRequestNumber"/></label>
		<input id="idSearch" name="idSearch" class="form-control" >
	</div>
	<div class="col-md-4 col-lg-4 form-group">
		<label for="agreementSearch"><spring:message code="common.agreement"/></label>
		<select id="agreementSearch" name="agreementSearch" class="form-control chosen-select" data-placeholder="<spring:message code='common.select.option'/>">
			<option value=""></option>
			<c:forEach items="${agreements}" var="agreement">
				<option value="${agreement.id}"><c:out value="${agreement.code}"></c:out> - <c:out value="${agreement.description}"></c:out></option>
			</c:forEach>
		</select>
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

<div class="row">
	<div class="col-md-4 col-lg-4 form-group">
		<label for="logisticsOperatorSearch"><spring:message code="common.logisticsOperator"/></label>
		<select id="logisticsOperatorSearch" name="logisticsOperatorSearch" class="form-control chosen-select" data-placeholder="<spring:message code='common.select.option'/>">
			<option value=""></option>
			<c:forEach items="${logisticsOperators}" var="logisticsOperator">
				<option value="${logisticsOperator.id}"><c:out value="${logisticsOperator.code}"></c:out> - <c:out value="${logisticsOperator.name}"></c:out></option>
			</c:forEach>
		</select>
	</div>
	<div class="col-md-4 col-lg-4 form-group">
		<label for="clientSearch"><spring:message code="common.client"/></label>
		<select id="clientSearch" name="clientSearch" class="form-control chosen-select" data-placeholder="<spring:message code='common.select.option'/>">
			<option value=""></option>
			<c:forEach items="${clients}" var="client">
				<option value="${client.id}"><c:out value="${client.code}"></c:out> - <c:out value="${client.name}"></c:out></option>
			</c:forEach>
		</select>
	</div>
	<div class="col-md-4 col-lg-4 form-group">
		<label for="deliveryLocationSearch"><spring:message code="common.deliveryLocation"/></label>
		<select id="deliveryLocationSearch" name="deliveryLocationSearch" class="form-control chosen-select" data-placeholder="<spring:message code='common.select.option'/>">
			<option value=""></option>
			<c:forEach items="${deliveryLocations}" var="deliveryLocation">
				<option value="${deliveryLocation.id}"><c:out value="${deliveryLocation.code}"></c:out> - <c:out value="${deliveryLocation.name}"></c:out></option>
			</c:forEach>
		</select>
	</div>
</div>

<div id="divTable">
	<table class="table table-condensed table-hover table-striped" id="deliveryNoteTable">
			<thead>
		        <tr>
					<th data-column-id="orderId" data-type="numeric" data-identifier="true" data-visible="false"><spring:message code="common.orderAssembly"/></th>
					<th data-column-id="id" data-type="numeric"><spring:message code="provisioningRequest.provisioningRequestNumber"/></th>
					<th data-column-id="agreement"><spring:message code="common.agreement"/></th>
					<th data-column-id="logisticsOperator"><spring:message code="common.logisticsOperator"/></th>
					<th data-column-id="client"><spring:message code="common.client"/></th>
					<th data-column-id="deliveryLocation"><spring:message code="common.deliveryLocation"/></th>
		          	<th data-column-id="option"><spring:message code="common.option"/></th>
		        </tr>
	   	 	</thead>
	   	 	<tbody id="deliveryNoteTableBody">
			</tbody>
	</table>
</div>
<br>
<div class="row">
	<div class="col-md-3 col-lg-2 col-md-offset-9 col-lg-offset-10">
		<button class="btn btn-success btn-block" type="submit" id="confirmButton">
			<span class="glyphicon glyphicon-ok"></span>
			<spring:message code="common.accept" />
		</button>
	</div>
</div>
