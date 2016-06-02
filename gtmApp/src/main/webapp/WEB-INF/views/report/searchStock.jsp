<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<script type="text/javascript" src="js/form/report/searchStock.js"></script>
<script type="text/javascript">
	$(document).ready(function() {
		new SearchStock();
	});
</script>

<jsp:include page="../modals/modals.jsp" />

<form id="searchStockForm" action="" onsubmit="return false;">
	<div class="row">
		<div class="col-md-9 col-lg-9 form-group">
			<h3><spring:message code="search.title.stock"/></h3>
		</div>
	</div>
	<div>
		
		<div class="row">
			<div class="col-md-12 col-lg-12 form-group">
				<label for="productInput"><spring:message code="common.product"/></label>
				<input id="productInput" type="search" placeholder='<spring:message code="input.product.placeholder"/>' class="form-control" name="product" autosave="" results="5" incremental="incremental" />
			</div>
		</div>
		
		<div class="row">
			<div class="col-md-4 col-lg-4 form-group">
				<label for="batchNumberSearch"><spring:message code="common.batch"/></label>
				<input type="text" class="form-control" name="batchNumberSearch" id="batchNumberSearch" autocomplete="off">
			</div>
			<div class="col-md-4 col-lg-4 form-group">
				<label for="dateFromSearch"><spring:message code="common.expirateDateFrom"/></label>
				<div class="input-group">
					<input type="text" class="form-control" name="dateFromSearch" id="dateFromSearch">
					<span class="input-group-addon" id="dateFromButton" style="cursor:pointer;">
						<span class="glyphicon glyphicon-calendar"></span>
					</span>
				</div>
			</div>
			<div class="col-md-4 col-lg-4 form-group">
				<label for="dateToSearch"><spring:message code="common.expirateDateTo"/></label>
				<div class="input-group">
					<input type="text" class="form-control" name="dateToSearch" id="dateToSearch">
					<span class="input-group-addon" id="dateToButton" style="cursor:pointer;">
						<span class="glyphicon glyphicon-calendar"></span>
					</span>
				</div>
			</div>
		</div>

		<div class="row">
			<div class="col-md-4 col-lg-4 form-group">
				<label for="serialNumberSearch"><spring:message code="common.serialNumber"/></label>
				<input type="text" class="form-control" name="serialNumberSearch" id="serialNumberSearch" autocomplete="off">
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
			<div class="col-md-4 col-lg-4 form-group">
				<label for="monodrugSearch"><spring:message code="common.monodrug"/></label>
				<select id="monodrugSearch" name="agreementSearch" class="form-control chosen-select" data-placeholder="<spring:message code='common.select.option'/>">
					<option value=""></option>
					<c:forEach items="${monodrugs}" var="monodrug">
						<option value="${monodrug.id}"><c:out value="${monodrug.code}"></c:out> - <c:out value="${monodrug.description}"></c:out></option>
					</c:forEach>
				</select>
			</div>

		</div>

		<div>
			<div class="row">
				<div class="col-md-3 col-lg-2 col-md-offset-6 col-lg-offset-8">
					<button class="btn btn-success btn-block" type="button" id="searchButton">
						<span class="glyphicon glyphicon-search"></span>
						<spring:message code="common.search" />
					</button>
				</div>
				<div class="col-md-3 col-lg-2">
					<button class="btn btn-info btn-block" type="reset" id="cleanButton">
						<span class="glyphicon glyphicon-trash"></span>
						<spring:message code="common.clean" />
					</button>
				</div>
			</div>
		</div>
	</div>
	
	<br>

	<div id="divTable">
		<table class="table table-condensed table-hover table-striped" id="stockTable">
			<thead>
		        <tr>
		        	<th data-identifier="true" data-column-id="id" data-type="numeric" data-visible="false"></th>
		        	<th data-column-id="code" data-type="numeric"><spring:message code="common.code" /></th>
		            <th data-column-id="product" data-header-css-class="descriptionColumn"><spring:message code="common.product"/></th>
					<th data-column-id="agreement" data-formatter="agreement"><spring:message code="common.agreement"/></th>
					<th data-column-id="gtin"><spring:message code="common.gtin"/></th>
					<th data-column-id="serialNumber" data-visible="false"><spring:message code="common.serialNumber"/></th>
		            <th data-column-id="amount" data-type="numeric"><spring:message code="common.amount"/></th>
		            <th data-column-id="command" data-formatter="command" data-sortable="false"><spring:message code="common.option"/></th>
		        </tr>
	   	 	</thead>
	   	 	<tbody id="stockTableBody">
			</tbody>
		</table>
	</div>
</form>

