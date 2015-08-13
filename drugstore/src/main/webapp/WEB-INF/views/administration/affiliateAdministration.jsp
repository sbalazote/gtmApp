<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<script type="text/javascript" src="js/form/administration/affiliateAdministration.js" ></script>
<script type="text/javascript" src="js/form/administration/save/saveAffiliate.js" ></script>
<script type="text/javascript">
	$(document).ready(function() {
		new SaveAffiliate();
	});
</script>

<div class="row">
	<div class="col-md-9 form-group">
		<h3>
			<spring:message code="administration.affiliateAdministration" />
		</h3>
	</div>
</div>

<div class="row">
	<div class="col-md-4">
		<button class="btn btn-primary btn-block" id="addAffiliate">
			<span class="glyphicon glyphicon-plus"></span>
			<spring:message code="common.add.entity" />
		</button>
	</div>
</div>

<br>
<table id="affiliatesTable" class="table table-condensed table-hover table-striped">
	<thead>
		<tr>
			<th data-column-id="id" data-header-css-class="idColumn" data-type="numeric"><spring:message code="common.id" /></th>
			<th data-column-id="code" data-header-css-class="codeColumn" data-type="numeric"><spring:message code="common.code" /></th>
			<th data-column-id="name"><spring:message code="common.entity.name" /></th>
			<th data-column-id="surname"><spring:message code="common.surname" /></th>
			<th data-column-id="documentType"><spring:message code="common.documentType" /></th>
			<th data-column-id="document"><spring:message code="common.document" /></th>
			<th data-column-id="client"><spring:message code="common.client" /></th>
			<th data-column-id="isActive"><spring:message code="common.active" /></th>
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
<div class="modal fade" data-backdrop="static" id="affiliateModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog" style="width:1000px">
		<div class="modal-content">
			<div class="modal-body">
				<div class="row">
					<div class="col-md-9 form-group">
						<h2 id="addAffiliateLabel" style="display: none;"><spring:message code="common.affiliate"/></h2>
						<h2 id="readAffiliateLabel" style="display: none;"><spring:message code="administration.readAffiliate"/></h2>
						<h2 id="updateAffiliateLabel" style="display: none;"><spring:message code="administration.updateAffiliate"/></h2>
						<input type="hidden" class="form-control" id="idInput">
					</div>
				</div>
				<form id="affiliateAdministrationForm" action="" onsubmit="return false;">
				<div class="row">
					<div class="col-md-4 form-group">
						<label for="codeInput"><spring:message code="common.code"/></label> 
						<input type="text" class="form-control" id="codeInput" name="code">
					</div>
					<div class="col-md-8 form-group">	
						<label for="clientSelect"><spring:message code="common.client"/></label>
						<select class="form-control chosen-select" id="clientSelect" name="client">
							<option value="">- <spring:message code="common.select.option"/> -</option>
							<c:forEach items="${clients}" var="client" varStatus="status">
							<option value="${client.id}"><c:out value="${client.name}"></c:out></option>
							</c:forEach>
						</select>
					</div>
				</div>
				<div class="row">
					<div class="col-md-4 form-group">
						<label for="surnameInput"><spring:message code="common.surname"/></label> 
						<input type="text" class="form-control" id="surnameInput" name="surname">
					</div>
					<div class="col-md-8 form-group">
						<label for="nameInput"><spring:message code="common.firstname"/></label> 
						<input type="text" class="form-control" id="nameInput" name="name">
					</div>
				</div>
				<div class="row">
					<div class="col-md-4 form-group">
						<label for="documentTypeSelect"><spring:message code="common.documentType"/></label> 
						<select class="form-control chosen-select" id="documentTypeSelect" name="documentType">
							<option value="">- <spring:message code="common.select.option"/> -</option>
							<c:forEach items="${documentTypes}" var="documentType">
								<option value="${documentType.key}">${documentType.value}</option>
							</c:forEach>
						</select>
					</div>
					<div class="col-md-4 form-group">
						<label for="documentInput"><spring:message code="common.document"/></label> 
						<input type="text" class="form-control" id="documentInput" name="document">
					</div>
					<div class="col-md-4 form-group">	
						<label for="activeSelect"><spring:message code="common.active"/></label>
						<select class="form-control chosen-select" id="activeSelect" name="active">
							<option value="true"><spring:message code="common.yes"/></option>
							<option value="false"><spring:message code="common.no"/></option>
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