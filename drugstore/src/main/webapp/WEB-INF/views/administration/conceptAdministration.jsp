<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<script type="text/javascript" src="js/form/administration/conceptAdministration.js" ></script>

<div class="row">
	<div class="col-md-9 form-group">
		<h2>
			<spring:message code="administration.conceptAdministration" />
		</h2>
	</div>
</div>

<div class="row">
	<div class="col-md-4">
		<button class="btn btn-primary btn-block" id="addConcept">
			<span class="glyphicon glyphicon-plus"></span>
			<spring:message code="common.add.entity" />
		</button>
	</div>
</div>

<br>
<table id="conceptsTable" class="table table-condensed table-hover table-striped">
	<thead>
		<tr>
			<th data-column-id="id" data-type="numeric"><spring:message code="common.id" /></th>
			<th data-column-id="code" data-type="numeric"><spring:message code="common.code" /></th>
			<th data-column-id="description"><spring:message code="common.description" /></th>
			<th data-column-id="deliveryNotePOS"><spring:message code="common.deliveryNote.POS" /></th>
			<%-- <th data-column-id="isInput"><spring:message code="common.isInput" /></th>
			<th data-column-id="printDeliveryNote"><spring:message code="common.printDeliveryNote" /></th>
			<th data-column-id="deliveryNotesCopies"><spring:message code="common.deliveryNotesCopies" /></th>
			<th data-column-id="isRefund"><spring:message code="common.refund" /></th> --%>
			<th data-column-id="isInformAnmat"><spring:message code="common.informAnmat" /></th>
			<th data-column-id="isActive"><spring:message code="common.active" /></th>
			<%-- <th data-column-id="isClient"><spring:message code="common.isClient" /></th> --%>
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
<div class="modal fade" data-backdrop="static" id="conceptModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog" style="width:1000px">
		<div class="modal-content">
			<div class="modal-body">
				<div class="row">
					<div class="col-md-9 form-group">
						<h2 id="addConceptLabel" style="display: none;"><spring:message code="common.concept"/></h2>
						<h2 id="readConceptLabel" style="display: none;"><spring:message code="administration.readConcept"/></h2>
						<h2 id="updateConceptLabel" style="display: none;"><spring:message code="administration.updateConcept"/></h2>
						<input type="hidden" class="form-control" id="idInput">
					</div>
				</div>
				<form id="conceptAdministrationForm" action="" onsubmit="return false;">
					<div class="row">
						<div class="col-md-4 form-group">
							<label for="codeInput"><spring:message code="common.code" /></label>
							<input type="text" class="form-control" id="codeInput" name="code">
						</div>
						<div class="col-md-4 form-group">
							<label for="descriptionInput"><spring:message code="common.description" /></label>
							<input type="text" class="form-control" id="descriptionInput" name="description">
						</div>
						<div class="col-md-4 form-group">
							<label for="deliveryNoteEnumeratorSelect"><spring:message code="configuration.deliveryNote.POS" /></label> 
							<select class="form-control chosen-select" id="deliveryNoteEnumeratorSelect" name="deliveryNoteEnumerator">
								<option value="">- <spring:message code="common.select.option"/> -</option>
							</select>
						</div>
					</div>

					<div class="row">
						<div class="col-md-4 form-group">
							<label for="inputSelect"><spring:message code="common.isInput" /></label>
							<select	class="form-control chosen-select" id="inputSelect" name="input">
								<option value="true"><spring:message code="common.yes" /></option>
								<option value="false"><spring:message code="common.no" /></option>
							</select>
						</div>
						<div class="col-md-4 form-group">
							<label for="printDeliveryNoteSelect"><spring:message code="common.printDeliveryNote" /></label>
							<select	class="form-control chosen-select" id="printDeliveryNoteSelect"	name="printDeliveryNote">
								<option value="true"><spring:message code="common.yes" /></option>
								<option value="false"><spring:message code="common.no" /></option>
							</select>
						</div>
						<div class="col-md-4 form-group">
							<label for="refundSelect"><spring:message code="common.refund" /></label>
							<select	class="form-control chosen-select" id="refundSelect" name="refund">
								<option value="true"><spring:message code="common.yes" /></option>
								<option value="false"><spring:message code="common.no" /></option>
							</select>
						</div>
					</div>


					<div class="row">
						<div class="col-md-3 form-group">
							<label for="informAnmatSelect"><spring:message code="common.informAnmat" /></label>
							<select	class="form-control chosen-select" id="informAnmatSelect" name="informAnmat">
								<option value="true"><spring:message code="common.yes" /></option>
								<option value="false"><spring:message code="common.no" /></option>
							</select>
						</div>
						<div class="col-md-3 form-group">
							<label for="deliveryNotesCopiesInput"><spring:message code="common.deliveryNotesCopies" /></label>
							<input type="text" class="form-control" id="deliveryNotesCopiesInput" name="deliveryNotesCopies">
						</div>
						<div class="col-md-3 form-group">
							<label for="activeSelect"><spring:message code="common.active" /></label>
							<select	class="form-control chosen-select" id="activeSelect" name="active">
								<option value="true"><spring:message code="common.yes" /></option>
								<option value="false"><spring:message code="common.no" /></option>
							</select>
						</div>
						<div class="col-md-3 form-group">
							<label for="clientSelect"><spring:message code="common.isClient" /></label>
							<select	class="form-control chosen-select" id="clientSelect" name="client">
								<option value="true"><spring:message code="common.yes" /></option>
								<option value="false"><spring:message code="common.no" /></option>
							</select>
						</div>
					</div>

					<div class="row">
						<div class="col-md-3 col-md-offset-1 form-group">
							<label for="conceptEvent"><spring:message code="common.conceptEvent" /></label>
						</div>
						<div class="col-md-3 col-md-offset-4 form-group">
							<label for="conceptEvent"><spring:message code="common.conceptEventSelected" /></label>
						</div>
					</div>

					<div>
					<div class="col-md- form-group">	
						<div class="ms-container">
							<select multiple="multiple" id="my-select" name="my-select[]">
							</select>
						</div>
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