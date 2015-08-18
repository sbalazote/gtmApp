<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<script type="text/javascript" src="js/form/administration/userAdministration.js" ></script>
<script type="text/javascript" src="js/form/administration/save/saveUser.js" ></script>
<script type="text/javascript">
	$(document).ready(function() {
		new SaveUser();
	});
</script>

<div class="row">
	<div class="col-md-9 form-group">
		<h3>
			<spring:message code="administration.userAdministration" />
		</h3>
	</div>
</div>

<div class="row">
	<div class="col-md-4">
		<button class="btn btn-primary btn-block" id="addUser">
			<span class="glyphicon glyphicon-plus"></span>
			<spring:message code="common.add.entity" />
		</button>
	</div>
</div>

<br>
<table id="usersTable" class="table table-condensed table-hover table-striped">
	<thead>
		<tr>
			<th data-column-id="id" data-type="numeric"><spring:message code="common.id" /></th>
			<th data-column-id="name"><spring:message code="common.entity.name" /></th>
			<th data-column-id="password"><spring:message code="common.password" /></th>
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
<div class="modal fade" data-backdrop="static" id="userModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog" style="width:1000px">
		<div class="modal-content">
			<div class="modal-body">
				<div class="row">
					<div class="col-md-9 form-group">
						<h2 id="addUserLabel" style="display: none;"><spring:message code="common.user"/></h2>
						<h2 id="readUserLabel" style="display: none;"><spring:message code="administration.readUser"/></h2>
						<h2 id="updateUserLabel" style="display: none;"><spring:message code="administration.updateUser"/></h2>
						<input type="hidden" class="form-control" id="idInput">
					</div>
				</div>
				<form id="userAdministrationForm" action="" onsubmit="return false;">
					<div class="row">
						<div class="col-md-6 form-group">
							<label for="nameInput"><spring:message code="common.entity.name" /></label>
							<input type="text" class="form-control" id="nameInput" name="name">
						</div>
						<div class="col-md-6 form-group">
							<label for="activeSelect"><spring:message code="common.active" /></label>
							<select class="form-control chosen-select" id="activeSelect" name="active">
								<option value="true"><spring:message code="common.yes" /></option>
								<option value="false"><spring:message code="common.no" /></option>
							</select>
						</div>
					</div>

					<div class="row">
						<div class="col-md-6 form-group">
							<label for="passwordInput"><spring:message code="common.password" /></label>
							<input type="password" class="form-control" id="passwordInput" name="password">
						</div>
						<div class="col-md-6 form-group">
							<label for="passwordInputCheck"><spring:message code="common.repeatPassword" /></label>
							<input type="password" class="form-control" id="passwordInputCheck" name="passwordCheck">
						</div>
					</div>


					<div class="row">
						<div class="col-md-4 form-group">
							<label for="role"><spring:message code="common.roles" /></label>
						</div>
					</div>

					<div>
						<div class="col-md- form-group">
							<div class="ms-container">
								<select multiple="multiple" id="my-select" name="my-select[]">
									<c:forEach items="${roles}" var="role" varStatus="status">
										<option value="${role.id}"><c:out value="${role.id} - ${role.description}"></c:out></option>
									</c:forEach>
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