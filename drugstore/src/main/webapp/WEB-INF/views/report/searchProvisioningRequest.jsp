<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<script type="text/javascript" src="js/form/report/searchProvisioningRequest.js"></script>
<script type="text/javascript" src="js/form/modals.js"></script>
<script type="text/javascript">
	$(document).ready(function() {
		new SearchProvisioningRequest();
	});
</script>
<jsp:include page="../modals/provisioningRequestModal.jsp" />

<form id="searchProvisioningRequestForm" action="" onsubmit="return false;">
	<div class="row">
		<div class="col-md-9 form-group">
			<h1><spring:message code="provisioningRequest.search.title"/></h1>
		</div>
	</div>
	<div>
		<div class="row">
			<div class="col-md-4 form-group">
				<label for="idSearch"><spring:message code="provisioningRequest.provisioningRequestNumber"/></label>
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
				<label for="agreementSearch"><spring:message code="common.agreement"/></label>
				<select id="agreementSearch" name="agreementSearch" class="form-control chosen-select" data-placeholder="<spring:message code='common.select.option'/>">
					<option value=""></option>
					<c:forEach items="${agreements}" var="agreement">
						<option value="${agreement.id}"><c:out value="${agreement.code}"></c:out> - <c:out value="${agreement.description}"></c:out></option>
					</c:forEach>
				</select>
			</div>
			<div class="col-md-4 form-group">
				<label for="clientSearch"><spring:message code="common.client"/></label>
				<select id="clientSearch" name="clientSearch" class="form-control chosen-select" data-placeholder="<spring:message code='common.select.option'/>">
					<option value=""></option>
					<c:forEach items="${clients}" var="client">
						<option value="${client.id}"><c:out value="${client.code}"></c:out> - <c:out value="${client.name}"></c:out></option>
					</c:forEach>
				</select>
			</div>
			<div class="col-md-4 form-group">
				<label for="affiliateSearch"><spring:message code="common.affiliate"/></label>
				<input id="affiliateSearch" name="affiliateSearch" type="text" class="form-control" />
			</div>
		</div>
		
		<div class="row">
			<div class="col-md-8 form-group">
				<label for="commentTextArea"><spring:message code="provisioningRequest.comment.label" /></label>
				<textarea id="commentTextArea" name="commentTextArea" class="form-control my-textarea" rows="1" ></textarea>
			</div>
			<div class="col-md-4 form-group">
				<label for="stateSearch"><spring:message code="common.state"/></label>
				<select id="stateSearch" name="stateSearch" class="form-control chosen-select" data-placeholder="<spring:message code='common.select.option'/>">
					<option value=""></option>
					<c:forEach items="${states}" var="state">
						<option value="${state.id}"><c:out value="${state.description}"></c:out></option>
					</c:forEach>
				</select>
			</div>
		</div>

		<div class="row">
			<div class="col-md-4 form-group">
				<label for="deliveryLocationSearch"><spring:message code="common.deliveryLocation"/></label>
				<select id="deliveryLocationSearch" name="deliveryLocationSearch" class="form-control chosen-select" data-placeholder="<spring:message code='common.select.option'/>">
					<option value=""></option>
					<c:forEach items="${deliveryLocations}" var="deliveryLocation">
						<option value="${deliveryLocation.id}"><c:out value="${deliveryLocation.code}"></c:out> - <c:out value="${deliveryLocation.name}"></c:out></option>
					</c:forEach>
				</select>
			</div>
			<div class="col-md-4 form-group">
				<label for="logisticsOperatorSearch"><spring:message code="common.logisticsOperator"/></label>
				<select id="logisticsOperatorSearch" name="logisticsOperatorSearch" class="form-control chosen-select" data-placeholder="<spring:message code='common.select.option'/>">
					<option value=""></option>
					<c:forEach items="${logisticsOperators}" var="logisticsOperator">
						<option value="${logisticsOperator.id}"><c:out value="${logisticsOperator.code}"></c:out> - <c:out value="${logisticsOperator.name}"></c:out></option>
					</c:forEach>
				</select>
			</div>
			<div class="col-md-2 form-margin">
				<button class="btn btn-success btn-block" type="submit" id="searchButton">
				<span class="glyphicon glyphicon-search"></span>
				<spring:message code="common.search" />
				</button>
			</div>
			<div class="col-md-2 form-margin">
				<button class="btn btn-info btn-block" type="submit" id="cleanButton">
				<span class="glyphicon glyphicon-trash"></span>
				<spring:message code="common.clean" />
				</button>
			</div>
		</div>
	</div>
	
	<br>
	
	
	<div id=divTable>
		<table class="table table-condensed table-hover table-striped" id="provisioningTable">
			<thead>
		        <tr>
		            <th data-column-id="id" data-type="numeric"><spring:message code="provisioningRequest.provisioningRequestNumber"/></th>
		            <th data-column-id="client"><spring:message code="common.client"/></th>
		            <th data-column-id="agreement"><spring:message code="common.agreement"/></th>
		           	<th data-column-id="state"><spring:message code="common.state"/></th>
		           	<th data-column-id="date"><spring:message code="common.date"/></th>
		          	<th data-column-id="option"><spring:message code="common.option"/></th>
		        </tr>
	   	 	</thead>
	   	 	<tbody id="provisioningTableBody">
			</tbody>
		</table>
	</div>
</form>
