<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<script type="text/javascript" src="js/form/serializedReturns/serializedReturns.js"></script>
<script type="text/javascript">
	$(document).ready(function() {
		new SerializedReturns();
	});
</script>

<form id="serializedReturnsForm" action="" onsubmit="return false;">

<div class="row">
	<div class="col-md-6 col-lg-6 form-group">
		<h3><spring:message code="serializedReturns.label"/></h3>
		<input type="hidden" class="form-control" id="serializedReturnsId" value="${serializedReturnsId != null ? serializedReturnsId : ''}">
	</div>
	<div class="col-md-3 col-lg-3">
		<div id="divSerializedReturnsId" style="display:none;">
			<h3><spring:message code="common.number" />: <span style="color:blue">${serializedReturnsId != null ? serializedReturnsId : ''}</span></h3>
		</div> 
	</div>
	<div class="col-md-3 col-lg-3 form-group">
		<label for="currentDateInput"><spring:message code="common.date"/></label>
		<div class="input-group">
			<input type="text" class="form-control" name="currentDate" id="currentDateInput" value="<fmt:formatDate pattern="dd/MM/yyyy" value="${date}"/>"/>
			<span class="input-group-addon" id="currentDateButton" style="cursor:pointer;">
				<span class="glyphicon glyphicon-calendar"></span>
			</span>
		</div>
	</div>
</div>

<div class="row">
	<div class="col-md-6 col-lg-6 form-group">
		<label for="conceptInput"><spring:message code="common.concept"/></label>
		<select id="conceptInput" name="concept" class="form-control chosen-select" data-placeholder="<spring:message code='common.select.option'/>">
			<option value=""></option>
			<c:forEach items="${concepts}" var="concept">
				<option value="${concept.id}"  ${conceptId == concept.id ? 'selected' : ''}><c:out value="${concept.code}"></c:out> - <c:out value="${concept.description}"></c:out></option>
			</c:forEach>
		</select>
	</div>
	<div id="providerDiv" class="col-md-6 col-lg-6 form-group" >
		<label for="providerInput"><spring:message code="common.provider"/></label>
		<select id="providerInput" name="provider" class="form-control chosen-select" data-placeholder="<spring:message code='common.select.option'/>">
			<option value=""></option>
			<c:forEach items="${providers}" var="provider">
				<option value="${provider.id}" ${providerId == provider.id ? 'selected' : ''}><c:out value="${provider.code}"></c:out> - <c:out value="${provider.name}"></c:out></option>
			</c:forEach>
		</select>
	</div>
	<div id="deliveryLocationDiv" class="col-md-6 col-lg-6 form-group">
		<label for="deliveryLocationInput"><spring:message code="common.originLocation"/></label>
		<select id="deliveryLocationInput" name="deliveryLocation" class="form-control chosen-select" data-placeholder="<spring:message code='common.select.option'/>">
			<option value=""></option>
			<c:forEach items="${deliveryLocations}" var="deliveryLocation">
				<option value="${deliveryLocation.id}" ${deliveryLocationId == deliveryLocation.id ? 'selected' : ''}><c:out value="${deliveryLocation.code}"></c:out> - <c:out value="${deliveryLocation.name}"></c:out></option>
			</c:forEach>
		</select>
	</div>
</div>

<div class="row">
	<div class="col-md-8 col-lg-8 form-group">
		<label for="deliveryNoteNumberInput"><spring:message code="common.deliveryNote"/></label>
		<div class="input-group">
			<input name="deliveryNotePOS" id="deliveryNotePOSInput" type="text" class="form-control" placeholder=<spring:message code="common.deliveryNote.POS"/> value="${deliveryNotePOSInput != null ? deliveryNotePOSInput : ''}">
  			<span class="input-group-addon">-</span>
  			<input name="deliveryNoteNumber" id="deliveryNoteNumberInput" type="text" class="form-control" placeholder=<spring:message code="common.deliveryNote.number"/> value="${deliveryNoteNumberInput != null ? deliveryNoteNumberInput : ''}">
		</div>
	</div>
	<div class="col-md-4 col-lg-4 form-group">
		<label for="agreementInput"><spring:message code="common.agreement"/></label>
		<select id="agreementInput" name="agreement" class="form-control chosen-select" data-placeholder="<spring:message code='common.select.option'/>">
			<option value=""></option>
			<c:forEach items="${agreements}" var="agreement">
				<option value="${agreement.id}" ${agreementId == agreement.id ? 'selected' : ''}><c:out value="${agreement.code}"></c:out> - <c:out value="${agreement.description}"></c:out></option>
			</c:forEach>
		</select>
	</div>
</div>

<div class="row">
	<div class="col-md-12 col-lg-12 form-group">
		<label for="serialNumberInput"><spring:message code="common.readSerial"/></label>
		<input id="serialNumberInput" type="search" placeholder='<spring:message code="serializedReturns.serialNumber.placeholder"/>' class="form-control" name="serialNumber" autosave="" results="5" incremental="incremental" />
	</div>
</div>

<br>

<div>
	<table id="productTable" class="table table-condensed table-hover table-striped">
		<thead>
	        <tr>
				<th data-column-id="description" data-css-class="td-description" data-sortable="false"><spring:message code="common.product"/></th>
				<th data-column-id="gtin" data-css-class="gtin" data-sortable="false"><spring:message code="common.gtin"/></th>
				<th data-column-id="serialNumber" data-css-class="serialNumber" data-sortable="false"><spring:message code="common.serialNumber" /></th>
				<th data-column-id="batch" data-css-class="batch" data-sortable="false"><spring:message code="common.batch" /></th>
				<th data-column-id="expirationDate" data-css-class="expirationDate" data-sortable="false"><spring:message code="common.expirationDate" /></th>

	        </tr>
   	 	</thead>
   	 	<tbody id="productTableBody">
		</tbody>
	</table>
</div>

<div class="row">
	<c:if test="${serializedReturnsId != null}">
		<div class="col-md-3 col-lg-2">
			<button class="btn btn-danger btn-block" id="delete"><span class="glyphicon glyphicon-remove"></span> <spring:message code="serializedReturns.cancellation.button"/></button>
		</div>
		<div class="col-md-2 col-lg-2 col-md-offset-6 col-lg-offset-6">
			<button class="btn btn-danger btn-block" onclick="myAbortWarning();" id="abortButton"><span class="glyphicon glyphicon-remove"></span> <spring:message code="common.abort"/></button>
		</div>
		<div class="col-md-2 col-lg-2">
			<button type="submit" class="btn btn-success btn-block" id="confirmButton"><span class="glyphicon glyphicon-ok"></span> <spring:message code="serializedReturns.authorize.button"/></button>
		</div>
	</c:if>
	<c:if test="${serializedReturnsId == null}">
		<div class="col-md-2 col-lg-2 col-md-offset-8 col-lg-offset-8">
			<button class="btn btn-danger btn-block" onclick="myAbortWarning();" id="abortButton"><span class="glyphicon glyphicon-remove"></span> <spring:message code="common.abort"/></button>
		</div>
		<div class="col-md-2 col-lg-2">
			<button type="submit" class="btn btn-success btn-block" id="confirmButton"><span class="glyphicon glyphicon-ok"></span> <spring:message code="common.confirm"/></button>
		</div>
	</c:if>
</div>

</form>

<%-- Confirmaci�n del delete de una row --%>
<div class="modal fade" data-backdrop="static" id="destructionModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog" style="width: 40%">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
				<h4 class="modal-title"><spring:message code="serializedReturns.destructionModal"/></h4>
			</div>
			<div class="modal-body">
				<div class="row">
					<div class="col-md-12 col-lg-12 form-group">
						<label for="destructionConceptInput"><spring:message code="common.concept"/></label>
						<select id="destructionConceptInput" name="destructionConcept" class="form-control" data-placeholder="<spring:message code='common.select.option'/>" autofocus>
							<option value=""></option>
							<c:forEach items="${destructionConcepts}" var="concept">
								<option value="${concept.id}"  ${conceptId == concept.id ? 'selected' : ''}><c:out value="${concept.code}"></c:out> - <c:out value="${concept.description}"></c:out></option>
							</c:forEach>
						</select>
					</div>
				</div>
			</div>
			<div class="modal-footer">
				<button type="button" id="cancelDestruction" class="btn btn-default" data-dismiss="modal"><spring:message code="common.no"/></button>
				<button type="button" class="btn btn-primary" data-dismiss="modal" id="destructionAccept"><spring:message code="common.yes"/></button>
			</div>
		</div>
	</div>
</div>

<%-- Confirmaci�n del delete de una row --%>
<div class="modal fade" data-backdrop="static" id="serializedReturnErrorModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog" style="width: 40%">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
				<h4 class="modal-title"><spring:message code="serializedReturns.errors.forcedInputModal"/></h4>
			</div>
			<div class="modal-body">
				<div id="alertSerializedReturnModalDiv"></div>
			</div>
			<div class="modal-footer">
				<button type="button" id="cancel" class="btn btn-default" data-dismiss="modal"><spring:message code="common.no"/></button>
				<button type="button" class="btn btn-primary" data-dismiss="modal" id="forcedInput"><spring:message code="common.yes"/></button>
			</div>
		</div>
	</div>
</div>
