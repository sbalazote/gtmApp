<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<script type="text/javascript" src="js/form/administration/agreementAdministration.js" ></script>
<script type="text/javascript" src="js/form/administration/save/saveAgreement.js" ></script>
<script type="text/javascript">
	$(document).ready(function() {
		new SaveAgreement();
	});
</script>

<div class="row">
	<div class="col-md-9 col-lg-9 form-group">
		<h3>
			<spring:message code="administration.agreementAdministration" />
		</h3>
	</div>
</div>

<div class="row">
	<div class="col-md-4 col-lg-4">
		<button class="btn btn-primary btn-block" id="addAgreement">
			<span class="glyphicon glyphicon-plus"></span>
			<spring:message code="common.add.entity" />
		</button>
	</div>
</div>

<br>
<table id="agreementsTable" class="table table-condensed table-hover table-striped">
	<thead>
		<tr>
			<th data-column-id="id" data-header-css-class="idColumn" data-type="numeric"><spring:message code="common.id" /></th>
			<th data-column-id="code" data-header-css-class="codeColumn" data-type="numeric"><spring:message code="common.code" /></th>
			<th data-column-id="description"><spring:message code="common.description" /></th>
			<th data-column-id="numberOfDeliveryNoteDetailsPerPage"><spring:message code="configuration.numberOfDeliveryNoteDetailsPerPage" /></th>
			<%--<th data-column-id="orderLabelPrinter"><spring:message code="configuration.orderLabelPrinter" /></th>--%>
			<th data-column-id="isPickingList"><spring:message code="common.pickingList" /></th>
			<%--<th data-column-id="deliveryNotePrinter"><spring:message code="configuration.deliveryNotePrinter" /></th>--%>
			<th data-column-id="isActive"><spring:message code="common.active" /></th>
			<th data-column-id="deliveryNoteConcept"><spring:message code="configuration.deliveryNoteConcept" /></th>
			<th data-column-id="destructionConcept"><spring:message code="configuration.destructionConcept" /></th>
			<th data-column-id="commands" data-formatter="commands" data-sortable="false"><spring:message code="administration.commands.tableLabel"/></th>
		</tr>
	</thead>
</table>

<%-- Confirmaci�n de que se borrar� definitivamente --%>
<div class="modal fade" data-backdrop="static" id="deleteConfirmationModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog" style="width:250px">
		<div class="modal-content">
			<div class="modal-body">
				<strong><span style="color:red"><spring:message code="administration.entity.delete"/></span></strong>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal"><spring:message code="common.no"/></button>
				<button type="button" class="btn btn-primary" data-dismiss="modal" id="deleteEntityButton"><spring:message code="common.yes"/></button>
			</div>
		</div>
	</div>
</div>

<%-- Modal de Lectura/Modificacion --%>
<div class="modal fade" data-backdrop="static" id="agreementModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog" style="width:80%">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
				<h2 class="modal-title" id="addAgreementLabel" style="display: none;"><spring:message code="common.agreement"/></h2>
				<h2 class="modal-title" id="readAgreementLabel" style="display: none;"><spring:message code="administration.readAgreement"/></h2>
				<h2 class="modal-title" id="updateAgreementLabel" style="display: none;"><spring:message code="administration.updateAgreement"/></h2>
				<input type="hidden" class="form-control" id="idInput">
			</div>
			<div id="agreementModalAlertDiv"></div>
			<div class="modal-body">
				<form id="agreementAdministrationForm" action="" onsubmit="return false;">
					<div class="row">
						<div class="col-md-6 col-lg-6 form-group">
							<label for="codeInput"><spring:message code="common.code" /></label> <input type="text" class="form-control" id="codeInput" name="code">
						</div>
						<div class="col-md-6 col-lg-6 form-group">
							<label for="descriptionInput"><spring:message code="common.description" /></label> <input type="text" class="form-control" id="descriptionInput" name="description">
						</div>
					</div>
					<div class="row">
						<div class="col-md-4 col-lg-4 form-group">
							<label for="numberOfDeliveryNoteDetailsPerPageInput"><spring:message code="configuration.numberOfDeliveryNoteDetailsPerPage" /></label> <input type="text" class="form-control" id="numberOfDeliveryNoteDetailsPerPageInput" name="numberOfDeliveryNoteDetailsPerPage" value="${numberOfDeliveryNoteDetailsPerPage}">
						</div>
						<div class="col-md-4 col-lg-4 form-group">
							<label for="activeSelect"><spring:message code="common.active" /></label> <select class="form-control chosen-select" id="activeSelect" name="active">
							<option value="true"><spring:message code="common.yes" /></option>
							<option value="false"><spring:message code="common.no" /></option>
						</select>
						</div>
						<div class="col-md-4 col-lg-4 form-group">
							<label for="pickingListSelect"><spring:message code="common.pickingList" /></label> <select class="form-control chosen-select" id="pickingListSelect" name="pickingList">
							<option value="true"><spring:message code="common.yes" /></option>
							<option value="false"><spring:message code="common.no" /></option>
						</select>
						</div>
					</div>
					<div class="row">
						<div class="col-md-6 col-lg-6 form-group">
							<label for="orderLabelPrinterSelect"><spring:message code="configuration.orderLabelPrinter" /></label>
							<select class="form-control chosen-select" id="orderLabelPrinterSelect" name="orderLabelPrinter">
								<option value="">- <spring:message code="common.select.option"/> -</option>
								<c:forEach items="${allPrinters}" var="printer" varStatus="status">
									<option value="${printer.getName()}"><c:out value="${printer.getName()}"></c:out></option>
								</c:forEach>
							</select>
						</div>
						<div class="col-md-6 col-lg-6 form-group">
							<label for="deliveryNotePrinterSelect"><spring:message code="configuration.deliveryNotePrinter" /></label>
							<select class="form-control chosen-select" id="deliveryNotePrinterSelect" name="deliveryNotePrinter">
								<option value="">- <spring:message code="common.select.option"/> -</option>
								<c:forEach items="${allPrinters}" var="printer" varStatus="status">
									<option value="${printer.getName()}"><c:out value="${printer.getName()}"></c:out></option>
								</c:forEach>
							</select>
						</div>
					</div>
					<div class="row">
						<div class="col-md-6 col-lg-6 form-group">
							<label for="deliveryNoteConceptSelect"><spring:message code="configuration.deliveryNoteConcept" /></label> 
							<select class="form-control chosen-select" id="deliveryNoteConceptSelect" name="deliveryNoteConcept">
							<option value="">- <spring:message code="common.select.option"/> -</option>
							<c:forEach items="${deliveryNoteConcepts}" var="deliveryNoteConcept" varStatus="status">
							<option value="${deliveryNoteConcept.id}"><c:out value="${deliveryNoteConcept.code}"></c:out> - <c:out value="${deliveryNoteConcept.description}"></c:out></option>
							</c:forEach>
							</select>
						</div>
						<div class="col-md-6 col-lg-6 form-group">
							<label for="destructionConceptSelect"><spring:message code="configuration.destructionConcept" /></label>
							<select class="form-control chosen-select" id="destructionConceptSelect" name="destructionConcept">
							<option value="">- <spring:message code="common.select.option"/> -</option>
							<c:forEach items="${destructionConcepts}" var="destructionConcept" varStatus="status">
							<option value="${destructionConcept.id}"><c:out value="${destructionConcept.code}"></c:out> - <c:out value="${destructionConcept.description}"></c:out></option>
							</c:forEach>
							</select>
						</div>
					</div>
				</form>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal"><spring:message code="common.back"/></button>
				<button class="btn btn-success" id="addButton" style="display: none;"><span class="glyphicon glyphicon-ok"></span> <spring:message code="common.add.entity"/></button>
				<button class="btn btn-success" id="updateButton" style="display: none;"><span class="glyphicon glyphicon-ok"></span> <spring:message code="common.confirm"/></button>
			</div>
		</div>
	</div>
</div>