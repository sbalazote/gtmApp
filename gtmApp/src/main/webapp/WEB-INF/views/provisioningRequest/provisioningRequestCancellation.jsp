<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<script type="text/javascript" src="js/form/provisioningRequest/provisioningRequestCancellation.js"></script>
<script type="text/javascript" src="js/form/modals.js"></script>
<script type="text/javascript">
	$(document).ready(function() {
		new ProvisioningRequestCancellation();
	});
</script>

<jsp:include page="../modals/modals.jsp" />
<jsp:include page="../modals/provisioningRequestModal.jsp" />

<form id="provisioningRequestCancellationForm" action="" onsubmit="return false;">
	<div class="row">
		<div class="col-md-12 col-lg-12 form-group">
			<h3><spring:message code="provisioningRequest.cancellation.label"/></h3>
		</div>
	</div>
	<div class="row">
		<div class="col-md-8 col-lg-8 form-group">
			<label for="provisioningRequestSearch"><spring:message code="provisioningRequest.label"/></label>
			<input type="text" class="form-control" name="provisioningRequestSearch" id="provisioningRequestSearch" >
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
	<div>
		<table class="table table-condensed table-hover table-striped" id="provisioningTable">
			<thead>
			<tr>
				<th data-column-id="id" data-type="numeric" data-identifier="true"><spring:message code="provisioningRequest.provisioningRequestNumber" /></th>
				<th data-column-id="client"><spring:message code="common.client" /></th>
				<th data-column-id="agreement"><spring:message code="common.agreement" /></th>
				<th data-column-id="state"><spring:message code="common.state" /></th>
				<th data-column-id="date"><spring:message code="common.date" /></th>
				<th data-column-id="action" data-formatter="action" data-sortable="false"><spring:message code="common.option" /></th>
			</tr>
		</thead>
	   	 	<tbody id="provisioningTableBody">
			</tbody>
		</table>
	</div>
	<br>
	<div class="row">
		<div class="col-md-3 col-lg-2 col-md-offset-9 col-lg-offset-10">
			<button class="btn btn-danger btn-block" type="submit" id="confirmButton">
				<span class="glyphicon glyphicon-ok"></span>
				<spring:message code="provisioningRequest.cancellation.button" />
			</button>
		</div>
	</div>
</form>