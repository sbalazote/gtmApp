<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<script type="text/javascript" src="js/form/administration/logisticsOperatorAdministration.js" ></script>
<script type="text/javascript" src="js/form/administration/save/saveLogisticsOperator.js" ></script>
<script type="text/javascript">
	$(document).ready(function() {
		new SaveLogisticsOperator();
	});
</script>

<div class="row">
	<div class="col-md-9 col-lg-9 form-group">
		<h3>
			<spring:message code="administration.logisticsOperatorAdministration" />
		</h3>
	</div>
</div>

<div class="row">
	<div class="col-md-4 col-lg-4">
		<button class="btn btn-primary btn-block" id="addLogisticsOperator">
			<span class="glyphicon glyphicon-plus"></span>
			<spring:message code="common.add.entity" />
		</button>
	</div>
</div>

<br>
<table id="logisticsOperatorsTable" class="table table-condensed table-hover table-striped">
	<thead>
		<tr>
			<th data-column-id="id" data-header-css-class="idColumn" data-type="numeric"><spring:message code="common.id" /></th>
			<th data-column-id="code" data-header-css-class="codeColumn" data-type="numeric"><spring:message code="common.code" /></th>
			<th data-column-id="name" data-header-css-class="nameColumn"><spring:message code="common.entity.name" /></th>
			<th data-column-id="taxId" data-type="numeric"><spring:message code="common.taxId" /></th>
			<th data-column-id="corporateName"><spring:message code="common.corporateName" /></th>
			<th data-column-id="isActive"><spring:message code="common.active" /></th>
			<th data-column-id="isInput"><spring:message code="common.logisticOperator.isInput" /></th>
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
<div class="modal fade" data-backdrop="static" id="logisticsOperatorModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog" style="width:80%">
		<div class="modal-content">
			<div class="modal-body">
				<div class="row">
					<div class="col-md-9 col-lg-9 form-group">
						<h2 id="addLogisticsOperatorLabel" style="display: none;"><spring:message code="common.logisticsOperator"/></h2>
						<h2 id="readLogisticsOperatorLabel" style="display: none;"><spring:message code="administration.readLogisticsOperator"/></h2>
						<h2 id="updateLogisticsOperatorLabel" style="display: none;"><spring:message code="administration.updateLogisticsOperator"/></h2>
						<input type="hidden" class="form-control" id="idInput">
					</div>
				</div>
				<form id="logisticsOperatorAdministrationForm" action="" onsubmit="return false;">
					<div class="row">
						<div class="col-md-4 col-lg-4 form-group">
							<label for="codeInput"><spring:message code="common.code" /></label>
							<input type="text" class="form-control" id="codeInput" name="code">
						</div>
						<div class="col-md-4 col-lg-4 form-group">
							<label for="nameInput"><spring:message code="common.entity.name" /></label>
							<input type="text" class="form-control" id="nameInput" name="name">
						</div>
						<div class="col-md-4 col-lg-4 form-group">
							<label for="taxIdInput"><spring:message code="common.taxId" /></label>
							<input type="text" class="form-control" id="taxIdInput" name="taxId">
						</div>
					</div>
					<div class="row">
						<div class="col-md-6 col-lg-6 form-group">
							<label for="glnInput"><spring:message code="common.gln" /></label>
							<input type="text" class="form-control" id="glnInput" name="gln">
						</div>
						<div class="col-md-6 col-lg-6 form-group">
							<label for="agentSelect"><spring:message code="common.agent" /></label>
							<select class="form-control chosen-select" id="agentSelect" name="agent">
								<option value="">-<spring:message code="common.select.option" /> -</option>
								<c:forEach items="${agents}" var="agent" varStatus="status">
									<option value="${agent.id}"><c:out value="${agent.description}"></c:out></option>
								</c:forEach>
							</select>
						</div>
					</div>

					<div class="row">
						<div class="col-md-4 col-lg-4 form-group">
							<label for="corporateNameInput"><spring:message code="common.corporateName" /></label>
							<input type="text" class="form-control" id="corporateNameInput" name="corporateName">
						</div>
						<div class="col-md-4 col-lg-4 form-group">
							<label for="provinceSelect"><spring:message code="common.province" /></label>
							<select class="form-control chosen-select" id="provinceSelect" name="province">
								<option value="">-<spring:message code="common.select.option" /> -</option>
								<c:forEach items="${provinces}" var="province" varStatus="status">
									<option value="${province.id}"><c:out value="${province.name}"></c:out></option>
								</c:forEach>
							</select>
						</div>
						<div class="col-md-4 col-lg-4 form-group">
							<label for="localityInput"><spring:message code="common.locality" /></label>
							<input type="text" class="form-control" id="localityInput" name="locality">
						</div>
					</div>

					<div class="row">
						<div class="col-md-3 col-lg-3 form-group">
							<label for="addressInput"><spring:message code="common.address" /></label>
							<input type="text" class="form-control" id="addressInput" name="address">
						</div>
						<div class="col-md-3 col-lg-3 form-group">
							<label for="phoneInput"><spring:message code="common.phone" /></label>
							<input type="text" class="form-control" id="phoneInput" name="phone">
						</div>
						<div class="col-md-2 col-lg-2 form-group">
							<label for="zipCodeInput"><spring:message code="common.zipCode" /></label>
							<input type="text" class="form-control" id="zipCodeInput" name="zipCode">
						</div>
						<div class="col-md-2 col-lg-2 form-group">
							<label for="activeSelect"><spring:message code="common.active" /></label>
							<select class="form-control chosen-select" id="activeSelect" name="active">
								<option value="true"><spring:message code="common.yes" /></option>
								<option value="false"><spring:message code="common.no" /></option>
							</select>
						</div>
						<div class="col-md-2 col-lg-2 form-group">
							<label for="isInputSelect"><spring:message code="common.logisticOperator.isInput" /></label>
							<select class="form-control chosen-select" id="isInputSelect" name="isInput">
								<option value="true"><spring:message code="common.yes" /></option>
								<option value="false"><spring:message code="common.no" /></option>
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