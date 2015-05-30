<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<script type="text/javascript" src="js/form/administration/eventAdministration.js" /></script>
<script type="text/javascript" src="js/form/administration/save/saveEvent.js" /></script>
<script type="text/javascript">
	$(document).ready(function() {
		new SaveEvent();
	});
</script>

<div class="row">
	<div class="col-md-9 form-group">
		<h2>
			<spring:message code="administration.eventAdministration" />
		</h2>
	</div>
</div>

<div class="row">
	<div class="col-md-4">
		<button class="btn btn-primary btn-block" id="addEvent">
			<span class="glyphicon glyphicon-plus"></span>
			<spring:message code="common.add.entity" />
		</button>
	</div>
</div>

<br>
<table id="eventsTable" class="table table-condensed table-hover table-striped">
	<thead>
		<tr>
			<th data-column-id="id" data-type="numeric"><spring:message code="common.id" /></th>
			<th data-column-id="code" data-type="numeric"><spring:message code="common.code" /></th>
			<th data-column-id="description"><spring:message code="common.description" /></th>
			<th data-column-id="originAgent"><spring:message code="common.originAgent" /></th>
			<th data-column-id="destinationAgent"><spring:message code="common.destinationAgent" /></th>
			<th data-column-id="isActive"><spring:message code="common.active" /></th>
			<th data-column-id="commands" data-formatter="commands" data-sortable="false"><spring:message code="administration.commands.tableLabel"/></th>
		</tr>
	</thead>
</table>

<%-- Confirmación de que se borrará definitivamente --%>
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
<div class="modal fade" data-backdrop="static" id="eventModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog" style="width:1000px">
		<div class="modal-content">
			<div class="modal-body">
				<div class="row">
					<div class="col-md-9 form-group">
						<h2 id="addEventLabel" style="display: none;"><spring:message code="common.event"/></h2>
						<h2 id="readEventLabel" style="display: none;"><spring:message code="administration.readEvent"/></h2>
						<h2 id="updateEventLabel" style="display: none;"><spring:message code="administration.updateEvent"/></h2>
						<input type="hidden" class="form-control" id="idInput">
					</div>
				</div>
				<form id="eventAdministrationForm" action="" onsubmit="return false;">
					<div class="row">
						<div class="col-md-4 form-group">
							<label for="codeInput"><spring:message code="common.code" /></label>
							<input type="text" class="form-control" id="codeInput" name="code">
						</div>
						<div class="col-md-8 form-group">
							<label for="descriptionInput"><spring:message code="common.description" /></label>
							<input type="text" class="form-control" id="descriptionInput" name="description">
						</div>
					</div>

					<div class="row">
						<div class="col-md-4 form-group">
							<label for="originAgentIdSelect"><spring:message code="common.originAgent" /></label>
							<select class="form-control chosen-select" id="originAgentIdSelect" name="originAgentId">
								<option value="">-<spring:message code="common.select.option" /> -</option>
								<c:forEach items="${agents}" var="agent" varStatus="status">
									<option value="${agent.id}"><c:out value="${agent.description}"></c:out></option>
								</c:forEach>
							</select>
						</div>
						<div class="col-md-4 form-group">
							<label for="destinationAgentIdSelect"><spring:message code="common.destinationAgent" /></label>
							<select class="form-control chosen-select" id="destinationAgentIdSelect" name="destinationAgentId">
								<option value="">-<spring:message code="common.select.option" /> -</option>
								<c:forEach items="${agents}" var="agent" varStatus="status">
									<option value="${agent.id}"><c:out value="${agent.description}"></c:out></option>
								</c:forEach>
							</select>
						</div>
						<div class="col-md-4 form-group">
							<label for="activeSelect"><spring:message code="common.active" /></label>
							<select class="form-control chosen-select" id="activeSelect" name="active">
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