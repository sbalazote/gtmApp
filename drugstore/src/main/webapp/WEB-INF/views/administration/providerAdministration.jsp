<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<script type="text/javascript" src="js/form/administration/providerAdministration.js" ></script>
<script type="text/javascript" src="js/form/administration/save/saveProvider.js" ></script>
<script type="text/javascript" src="js/form/administration/save/saveProviderType.js" ></script>
<script type="text/javascript">
	$(document).ready(function() {
		new SaveProvider();
		new SaveProviderType();
	});
</script>

<div class="row">
	<div class="col-md-9 col-lg-9 form-group">
		<h3><spring:message code="administration.providerAdministration"/></h3>
	</div>
</div>
<div>
	<div class="bs-example bs-example-tabs">
		<ul id="myTab" class="nav nav-tabs">
			<li class="active"><a href="#provider" data-toggle="tab"><spring:message code="common.entity.name"/></a></li>
			<li><a href="#providerType" data-toggle="tab"><spring:message code="common.providerType"/></a></li>
		</ul>
		<br>
		<div id="myTabContent" class="tab-content">
			<div class="tab-pane fade in active" id="provider">
				<div class="row">
					<div class="col-md-4 col-lg-4">
						<button class="btn btn-primary btn-block" id="addProvider"><span class="glyphicon glyphicon-plus"></span> <spring:message code="common.add.entity"/></button>
					</div>
				</div>
				
				<br>
				<table id="providersTable" class="table table-condensed table-hover table-striped">
					<thead>
						<tr>
							<th data-column-id="id" data-header-css-class="idColumn" data-type="numeric"><spring:message code="common.id"/></th>
							<th data-column-id="code" data-header-css-class="codeColumn" data-type="numeric"><spring:message code="common.code"/></th>
							<th data-column-id="name" data-header-css-class="nameColumn"><spring:message code="common.entity.name"/></th>
							<th data-column-id="taxId" data-type="numeric"><spring:message code="common.taxId"/></th>
							<th data-column-id="corporateName"><spring:message code="common.corporateName"/></th>
							<%-- <th data-column-id="province"><spring:message code="common.province"/></th>
							<th data-column-id="locality"><spring:message code="common.locality"/></th>
							<th data-column-id="address"><spring:message code="common.address"/></th>
							<th data-column-id="zipCode"><spring:message code="common.zipCode"/></th>
							<th data-column-id="VATLiability"><spring:message code="common.VATLiability"/></th>
							<th data-column-id="phone"><spring:message code="common.phone"/></th>
							<th data-column-id="email"><spring:message code="common.email"/></th>
							<th data-column-id="gln"><spring:message code="common.gln"/></th>
							<th data-column-id="agent"><spring:message code="common.agent"/></th>
							<th data-column-id="type"><spring:message code="common.type"/></th> --%>
							<th data-column-id="isActive"><spring:message code="common.active"/></th>
							<th data-column-id="commands" data-formatter="commands" data-sortable="false"><spring:message code="administration.commands.tableLabel"/></th>
						</tr>
					</thead>
				</table>
				
			</div>
			<div class="tab-pane fade" id="providerType">
				<div class="row">
					<div class="col-md-4 col-lg-4">
						<button class="btn btn-primary btn-block" id="addProviderType"><span class="glyphicon glyphicon-plus"></span> <spring:message code="common.add.entity"/></button>
					</div>
				</div>
				
				<br>
				<table id="providerTypesTable" class="table table-condensed table-hover table-striped">
					<thead>
						<tr>
							<th data-column-id="id" data-type="numeric"><spring:message code="common.id"/></th>
							<th data-column-id="code" data-type="numeric"><spring:message code="common.code"/></th>
							<th data-column-id="description"><spring:message code="common.description"/></th>
							<th data-column-id="isActive"><spring:message code="common.active"/></th>
							<th data-column-id="commands" data-formatter="commands" data-sortable="false"><spring:message code="administration.commands.tableLabel"/></th>
						</tr>
					</thead>
				</table>
			</div>
		</div>
	</div>
</div>

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

<%-- Modal de Lectura/Modificacion de Proveedor --%>
<div class="modal fade" data-backdrop="static" id="providerModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog" style="width:70%">
		<div class="modal-content">
			<div class="modal-body">
				<div class="row">
					<div class="col-md-9 col-lg-9 form-group">
						<h2 id="addProviderLabel" style="display: none;"><spring:message code="common.provider"/></h2>
						<h2 id="readProviderLabel" style="display: none;"><spring:message code="administration.readProvider"/></h2>
						<h2 id="updateProviderLabel" style="display: none;"><spring:message code="administration.updateProvider"/></h2>
						<input type="hidden" class="form-control" id="providerIdInput">
					</div>
				</div>
				<form id="providerAdministrationForm" action="" onsubmit="return false;">
					<div class="row">
						<div class="col-md-4 col-lg-4 form-group">
							<label for="providerCodeInput"><spring:message code="common.code" /></label>
							<input type="text" class="form-control" id="providerCodeInput" name="providerCode">
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
						<div class="col-md-8 col-lg-8 form-group">
							<label for="addressInput"><spring:message code="common.address" /></label>
							<input type="text" class="form-control" id="addressInput" name="address">
						</div>
						<div class="col-md-4 col-lg-4 form-group">
							<label for="emailInput"><spring:message code="common.email" /></label>
							<input type="text" class="form-control" id="emailInput" name="email">
						</div>
					</div>

					<div class="row">
						<div class="col-md-4 col-lg-4 form-group">
							<label for="phoneInput"><spring:message code="common.phone" /></label>
							<input type="text" class="form-control" id="phoneInput" name="phone">
						</div>
						<div class="col-md-4 col-lg-4 form-group">
							<label for="zipCodeInput"><spring:message code="common.zipCode" /></label>
							<input type="text" class="form-control" id="zipCodeInput" name="zipCode">
						</div>
						<div class="col-md-4 col-lg-4 form-group">
							<label for="glnInput"><spring:message code="common.gln" /></label>
							<input type="text" class="form-control" id="glnInput" name="gln">
						</div>
					</div>

					<div class="row">
						<div class="col-md-3 col-lg-3 form-group">
							<label for="agentSelect"><spring:message code="common.agent" /></label>
							<select class="form-control chosen-select" id="agentSelect" name="agent">
								<option value="">-<spring:message code="common.select.option" /> -</option>
								<c:forEach items="${agents}" var="agent" varStatus="status">
									<option value="${agent.id}"><c:out value="${agent.description}"></c:out></option>
								</c:forEach>
							</select>
						</div>
						<div class="col-md-3 col-lg-3 form-group">
							<label for="typeSelect"><spring:message code="common.providerType" /></label>
							<select class="form-control chosen-select" id="typeSelect" name="type">
								<option value="">-<spring:message code="common.select.option" /> -</option>
								<c:forEach items="${types}" var="type" varStatus="status">
									<option value="${type.id}"><c:out value="${type.description}"></c:out></option>
								</c:forEach>
							</select>
						</div>
						<div class="col-md-3 col-lg-3 form-group">
							<label for="VATLiabilitySelect"><spring:message code="common.VATLiability" /></label>
							<select class="form-control chosen-select" id="VATLiabilitySelect" name="VATLiability">
								<option value="">-<spring:message code="common.select.option" /> -</option>
								<c:forEach items="${VATLiabilities}" var="VATLiability" varStatus="status">
									<option value="${VATLiability.id}"><c:out value="${VATLiability.acronym}: ${VATLiability.description}"></c:out></option>
								</c:forEach>
							</select>
						</div>
						<div class="col-md-3 col-lg-3 form-group">
							<label for="providerActiveSelect"><spring:message code="common.active" /></label>
							<select class="form-control chosen-select" id="providerActiveSelect" name="providerActive">
								<option value="true"><spring:message code="common.yes" /></option>
								<option value="false"><spring:message code="common.no" /></option>
							</select>
						</div>
					</div>
				</form>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal"><spring:message code="common.back"/></button>
				<button class="btn btn-success" id="addProviderButton" style="display: none;"><span class="glyphicon glyphicon-ok"></span> <spring:message code="common.add.entity"/></button>
				<button class="btn btn-success" id="updateProviderButton" style="display: none;"><span class="glyphicon glyphicon-ok"></span> <spring:message code="common.confirm"/></button>
			</div>
		</div>
	</div>
</div>

<%-- Modal de Lectura/Modificacion de Tipo de Proveedor --%>
<div class="modal fade" data-backdrop="static" id="providerTypeModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog" style="width:80%">
		<div class="modal-content">
			<div class="modal-body">
				<div class="row">
					<div class="col-md-9 form-group">
						<h2 id="addProviderTypeLabel" style="display: none;"><spring:message code="common.providerType"/></h2>
						<h2 id="readProviderTypeLabel" style="display: none;"><spring:message code="administration.readProviderType"/></h2>
						<h2 id="updateProviderTypeLabel" style="display: none;"><spring:message code="administration.updateProviderType"/></h2>
						<input type="hidden" class="form-control" id="providerTypeIdInput">
					</div>
				</div>
				<form id="providerTypeAdministrationForm" action="" onsubmit="return false;">
					<div class="row">
						<div class="col-md-4 col-lg-4 form-group">
							<label for="providerTypeCodeInput"><spring:message code="common.code" /></label>
							<input type="text" class="form-control" id="providerTypeCodeInput" name="providerTypeCode">
						</div>
						<div class="col-md-4 col-lg-4  form-group">
							<label for="descriptionInput"><spring:message code="common.description" /></label>
							<input type="text" class="form-control" id="descriptionInput" name="description">
						</div>
						<div class="col-md-4 col-lg-4 form-group">
							<label for="providerTypeActiveSelect"><spring:message code="common.active" /></label>
							<select class="form-control chosen-select" id="providerTypeActiveSelect" name="providerTypeActive">
								<option value="true"><spring:message code="common.yes" /></option>
								<option value="false"><spring:message code="common.no" /></option>
							</select>
						</div>
					</div>
				</form>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal"><spring:message code="common.back"/></button>
				<button class="btn btn-success" id="addProviderTypeButton" style="display: none;"><span class="glyphicon glyphicon-ok"></span> <spring:message code="common.add.entity"/></button>
				<button class="btn btn-success" id="updateProviderTypeButton" style="display: none;"><span class="glyphicon glyphicon-ok"></span> <spring:message code="common.confirm"/></button>
			</div>
		</div>
	</div>
</div>