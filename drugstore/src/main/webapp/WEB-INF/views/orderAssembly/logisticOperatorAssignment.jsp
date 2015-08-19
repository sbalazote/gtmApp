<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<script type="text/javascript" src="js/form/orderAssembly/logisticOperatorAssignment.js"></script>
<script type="text/javascript" src="js/form/modals.js"></script>
<script type="text/javascript">
	$(document).ready(function() {
		new LogisticOperatorAssignment();
	});
</script>

<jsp:include page="../modals/modals.jsp" />
<jsp:include page="../modals/orderModal.jsp" />

<div class="row">
	<div class="col-md-9 col-lg-9 form-group">
		<h3><spring:message code="common.logisticsOperatorAssignment" /></h3>
	</div>
</div>
<div class="row">
	<div class="col-md-4 col-lg-4 form-group">
		<label for="agreementSearch"><spring:message code="common.agreement"/></label>
		<select id="agreementSearch" name="agreementSearch" class="form-control chosen-select" data-placeholder="<spring:message code='common.select.option'/>">
			<option value=""></option>
			<c:forEach items="${agreements}" var="agreement">
				<option value="${agreement.id}"><c:out value="${agreement.code}"></c:out> - <c:out value="${agreement.description}"></c:out></option>
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
	<table class="table table-condensed table-hover table-striped" id="logisticsOperatorTable">
			<thead>
		        <tr>
		            <th data-column-id="id" data-type="numeric" data-identifier="true"><spring:message code="common.number"/></th>
					<th data-column-id="agreement"><spring:message code="common.agreement"/></th>
					<th data-column-id="client"><spring:message code="common.client"/></th>
					<th data-column-id="logisticsOperator"><spring:message code="common.logisticsOperator"/></th>
		          	<th data-column-id="action" data-formatter="action" data-sortable="false"><spring:message code="common.option"/></th>
		        </tr>
	   	 	</thead>
	   	 	<tbody id="logisticsOperatorTableBody">
			</tbody>
	</table>
</div>
<br>
<div class="row">
	<div class="col-md-4 col-lg-4 form-group">
		<label for="logisticsOperatorInput"><spring:message code="common.logisticsOperator" /></label>
		<select id="logisticsOperatorInput" name="logisticsOperator" class="form-control chosen-select" data-placeholder="<spring:message code='common.select.option'/>">
			<option value=""></option>
			<c:forEach items="${logisticsOperators}" var="logisticsOperator">
				<option value="${logisticsOperator.id}" ><c:out value="${logisticsOperator.code}"></c:out> - <c:out value="${logisticsOperator.name}"></c:out></option>
			</c:forEach>
		</select>
	</div>

	<div class="col-md-4 col-lg-4 form-margin">
		<button class="btn btn-warning btn-block" type="submit" id="confirmButton">
			<span class="glyphicon glyphicon-ok"></span>
			<spring:message code="common.assignment" />
		</button>
	</div>
</div>