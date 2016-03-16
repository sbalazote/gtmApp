<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<script type="text/javascript" src="js/form/administration/providerSerializedFormatAdministration.js" ></script>

<div class="row">
	<div class="col-md-9 col-lg-9 form-group">
		<h3>
			<spring:message code="administration.serializeAdministration" />
		</h3>
	</div>
</div>

<div class="row">
	<div class="col-md-4 col-lg-4">
		<button class="btn btn-primary btn-block" id="addProviderSerializedFormat">
			<span class="glyphicon glyphicon-plus"></span>
			<spring:message code="common.add.entity" />
		</button>
	</div>
</div>

<br>

<div>
	<table class="table table-condensed table-hover table-striped" id="serializedFormatTable">
		<thead>
			<tr>
				<th data-column-id="id" data-type="numeric"><spring:message code="common.id" /></th>
				<th data-column-id="gtinLength" data-type="numeric"><spring:message code="common.gtinLength" /></th>
				<th data-column-id="serialNumberLength" data-type="numeric"><spring:message code="common.serialNumberLength" /></th>
				<th data-column-id="expirationDateLength" data-type="numeric"><spring:message code="common.expirationDateLength" /></th>
				<th data-column-id="batchLength" data-type="numeric"><spring:message code="common.batchLength" /></th>
				<th data-column-id="sequence"><spring:message code="common.sequence" /></th>
				<th data-column-id="commands" data-formatter="commands" data-sortable="false" data-searchable="false"><spring:message code="common.action" /></th>
			</tr>
		</thead>
		<tbody id="serializedFormatTableBody">
		</tbody>
	</table>
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