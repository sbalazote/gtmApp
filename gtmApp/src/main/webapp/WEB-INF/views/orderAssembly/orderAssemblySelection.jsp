<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<script type="text/javascript" src="js/form/orderAssembly/orderAssemblySelection.js"></script>
<script type="text/javascript">
	$(document).ready(function() {
		new OrderAssemblySelection();
	});
</script>

<input type="hidden" class="form-control" id="orderAssemblySelectionReturn" name="orderAssemblySelectionReturn" value="${orderAssemblySelectionReturn}">

<div class="row">
	<div class="col-md-9 col-lg-9 form-group">
		<h3><spring:message code="common.orderAssembly"/></h3>
	</div>
</div>
<div class="row">
	<div class="col-md-4 col-lg-4 form-group">
		<label for="provisioningRequestSearch"><spring:message code="provisioningRequest.provisioningRequestNumber"/></label>
		<input type="text" class="form-control" name="provisioningRequestSearch" id="provisioningRequestSearch" autofocus>
	</div>
	<div class="col-md-4 col-lg-4 form-group">
		<label for="agreementSearch"><spring:message code="common.agreement"/></label>
		<select id="agreementSearch" name="agreementSearch" class="form-control chosen-select" data-placeholder="<spring:message code='common.select.option'/>">
			<option value=""></option>
			<c:forEach items="${agreements}" var="agreement">
				<option value="${agreement.id}"  ${agreementSearchFilterId == agreement.id ? 'selected' : ''}><c:out value="${agreement.code}"></c:out> - <c:out value="${agreement.description}"></c:out></option>
			</c:forEach>
		</select>
	</div>
	<div class="col-md-4 col-lg-4 form-group">
		<label for="clientSearch"><spring:message code="common.client"/></label>
		<select id="clientSearch" name="clientSearch" class="form-control chosen-select" data-placeholder="<spring:message code='common.select.option'/>">
			<option value=""></option>
			<c:forEach items="${clients}" var="client">
				<option value="${client.id}"  ${clientSearchFilterId == client.id ? 'selected' : ''}><c:out value="${client.code}"></c:out> - <c:out value="${client.name}"></c:out></option>
			</c:forEach>
		</select>
	</div>
</div>
<div class="row">
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
	        	<th data-column-id="id" data-type="numeric" data-order="asc"><spring:message code="provisioningRequest.provisioningRequestNumber"/></th>
	            <th data-column-id="client"><spring:message code="common.client"/></th>
	            <th data-column-id="agreement"><spring:message code="common.agreement"/></th>
				<th data-column-id="state"><spring:message code="common.state" /></th>
				<th data-column-id="date"><spring:message code="common.date" /></th>
	          	<th data-column-id="option"><spring:message code="common.option"/></th>
	        </tr>
   	 	</thead>
   	 	<tbody id="orderTableBody">
		</tbody>
	</table>
</div>

<form id="myForm" action="orderAssembly.do" method="get">
	<input type="hidden" name="id" id="provisioningRequestId">
	<input type="hidden" name="agreementSearchFilterId" id="agreementSearchFilterIdInput">
	<input type="hidden" name="clientSearchFilterId" id="clientSearchFilterIdInput">
</form>
