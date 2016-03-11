<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<script type="text/javascript" src="js/form/input/input.js"></script>
<script type="text/javascript" src="js/form/input/batchExpirationDate.js"></script>
<script type="text/javascript" src="js/form/input/providerSerialized.js"></script>
<script type="text/javascript" src="js/form/input/selfSerialized.js"></script>
<script type="text/javascript">
	$(document).ready(function() {
		new Input();
	});
</script>

<form id="inputForm" action="" onsubmit="return false;">

<div class="row">
	<div class="col-md-6 col-lg-6 form-group">
		<h3><spring:message code="input.label"/></h3>
		<input type="hidden" class="form-control" id="inputId" value="${inputId != null ? inputId : ''}">
	</div>
	<div class="col-md-3 col-lg-3">
		<div id="divInputId" style="display:none;">
			<h3><spring:message code="common.number" />: <span style="color:blue">${inputId != null ? inputId : ''}</span></h3>
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
	<div class="col-md-4 col-lg-4 form-group">
		<label for="conceptInput"><spring:message code="common.concept"/></label>
		<select id="conceptInput" name="concept" class="form-control chosen-select" data-placeholder="<spring:message code='common.select.option'/>" autofocus>
			<option value=""></option>
			<c:forEach items="${concepts}" var="concept">
				<option value="${concept.id}"  ${conceptId == concept.id ? 'selected' : ''}><c:out value="${concept.code}"></c:out> - <c:out value="${concept.description}"></c:out></option>
			</c:forEach>
		</select>
	</div>
	<div id="providerDiv" class="col-md-4 col-lg-4 form-group" >
		<label for="providerInput"><spring:message code="common.provider"/></label>
		<select id="providerInput" name="provider" class="form-control chosen-select" data-placeholder="<spring:message code='common.select.option'/>">
			<option value=""></option>
			<c:forEach items="${providers}" var="provider">
				<option value="${provider.id}" ${providerId == provider.id ? 'selected' : ''}><c:out value="${provider.code}"></c:out> - <c:out value="${provider.name}"></c:out></option>
			</c:forEach>
		</select>
	</div>
	<div id="deliveryLocationDiv" class="col-md-4 col-lg-4 form-group">
		<label for="deliveryLocationInput"><spring:message code="common.originLocation"/></label>
		<select id="deliveryLocationInput" name="deliveryLocation" class="form-control chosen-select" data-placeholder="<spring:message code='common.select.option'/>">
			<option value=""></option>
			<c:forEach items="${deliveryLocations}" var="deliveryLocation">
				<option value="${deliveryLocation.id}" ${deliveryLocationId == deliveryLocation.id ? 'selected' : ''}><c:out value="${deliveryLocation.code}"></c:out> - <c:out value="${deliveryLocation.name}"></c:out></option>
			</c:forEach>
		</select>
	</div>
	<div class="col-md-4 col-lg-4 form-group" >
		<label for="logisticsOperatorInput"><spring:message code="common.providersLogisticsOperator"/></label>
		<select id="logisticsOperatorInput" name="logisticsOperator" class="form-control chosen-select" data-placeholder="<spring:message code='common.select.option'/>">
			<option value=""></option>
			<c:forEach items="${logisticsOperations}" var="logisticsOperation">
				<option value="${logisticsOperation.id}" ${logisticsOperationId == logisticsOperation.id ? 'selected' : ''}><c:out value="${logisticsOperation.code}"></c:out> - <c:out value="${logisticsOperation.name}"></c:out></option>
			</c:forEach>
		</select>
	</div>
</div>

<div class="row">
	<div class="col-md-4 col-lg-4 form-group">
		<label for="agreementInput"><spring:message code="common.agreement"/></label>
		<select id="agreementInput" name="agreement" class="form-control chosen-select" data-placeholder="<spring:message code='common.select.option'/>">
			<option value=""></option>
			<c:forEach items="${agreements}" var="agreement">
				<option value="${agreement.id}" ${agreementId == agreement.id ? 'selected' : ''}><c:out value="${agreement.code}"></c:out> - <c:out value="${agreement.description}"></c:out></option>
			</c:forEach>
		</select>
	</div>
	<div class="col-md-4 col-lg-4 form-group">
		<label for="deliveryNoteNumberInput"><spring:message code="common.deliveryNote"/></label>
		<div class="input-group">
			<input name="deliveryNotePOS" id="deliveryNotePOSInput" type="text" class="form-control" placeholder='<spring:message code="common.deliveryNote.POS"/>' value="${deliveryNotePOSInput != null ? deliveryNotePOSInput : ''}">
  			<span class="input-group-addon">-</span>
  			<input name="deliveryNoteNumber" id="deliveryNoteNumberInput" type="text" class="form-control" placeholder='<spring:message code="common.deliveryNote.number"/>' value="${deliveryNoteNumberInput != null ? deliveryNoteNumberInput : ''}">
		</div>
	</div>
	<div class="col-md-4 col-lg-4 form-group">
		<label for="purchaseOrderNumberInput"><spring:message code="common.purchaseOrder"/></label>
		<input type="text" class="form-control" name="purchaseOrderNumber" id="purchaseOrderNumberInput" value="${purchaseOrderNumber != null ? purchaseOrderNumber : ''}">
	</div>
</div>

<div class="row">
	<div class="col-md-12 col-lg-12 form-group">
		<label for="productInput"><spring:message code="common.addProduct"/></label>
		<input id="productInput" type="search" placeholder='<spring:message code="input.product.placeholder"/>' class="form-control" name="product" autosave="" results="5" incremental="incremental"/>
	</div>
</div>

<br>

<div>
	<table id="productTable" class="table table-condensed table-hover table-striped">
		<thead>
	        <tr>
	            <th data-identifier="true" data-column-id="description" data-css-class="td-description" data-sortable="false"><spring:message code="common.product"/></th>
	            <th data-column-id="amount" data-type="numeric" data-css-class="td-amount" data-sortable="false"><spring:message code="common.amount"/></th>
				<th data-column-id="command" data-formatter="command" data-sortable="false" data-searchable="false"><spring:message code="common.option"/></th>
			</tr>
   	 	</thead>
   	 	<tbody id="productTableBody">
		</tbody>
	</table>
</div>

<div class="row">
	<c:if test="${inputId != null}">
		<div class="col-md-3 col-lg-2">
			<button class="btn btn-danger btn-block" id="delete"><span class="glyphicon glyphicon-remove"></span> <spring:message code="input.cancellation.button"/></button>
		</div>
		<div class="col-md-3 col-lg-2 col-lg-offset-4">
			<button class="btn btn-danger btn-block" onclick="myAbortWarning();" id="abortButton"><span class="glyphicon glyphicon-remove"></span> <spring:message code="common.abort"/></button>
		</div>
		<div class="col-md-3 col-lg-2">
			<button class="btn btn-info" id="forcedInput"><span class="glyphicon glyphicon-log-in"></span> <spring:message code="input.authorizeWithoutInform.button"/></button>
		</div>
		<div class="col-md-3 col-lg-2">
			<button type="submit" class="btn btn-success btn-block" id="confirmButton"><span class="glyphicon glyphicon-ok"></span> <spring:message code="input.authorize.button"/></button>
		</div>
	</c:if>
	<c:if test="${inputId == null}">
		<div class="col-md-3 col-md-offset-6 col-lg-2 col-lg-offset-8">
			<button class="btn btn-danger btn-block" onclick="myAbortWarning();" id="abortButton"><span class="glyphicon glyphicon-remove"></span> <spring:message code="common.abort"/></button>
		</div>
		<div class="col-md-3 col-lg-2">
			<button type="submit" class="btn btn-success btn-block" id="confirmButton"><span class="glyphicon glyphicon-ok"></span> <spring:message code="common.confirm"/></button>
		</div>
	</c:if>
</div>

</form>

<%-- Modal Cantidad de Productos --%>
<form id="productAmountModalForm" action="" onsubmit="return false;">
	<div class="modal fade" data-backdrop="static" id="amountModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog" style="width:250px">
			<div class="modal-content">
				<div class="modal-body">
					<label for="productAmountInput"><spring:message code="common.amount"/></label>
					<input type="text" class="form-control" name="productAmount" id="productAmountInput"/>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal"><spring:message code="common.abort"/></button>
					<button type="button" class="btn btn-primary" id="amountModalAcceptButton"><spring:message code="common.accept"/></button>
				</div>
			</div>
		</div>
	</div>
</form>

<%-- Modal Ingreso Lote y Vencimiento --%>
<form id="batchExpirationDateModalForm" action="" onsubmit="return false;">
	<div class="modal fade" data-backdrop="static" data-keyboard="false" id="batchExpirationDateModal">
		<div class="modal-dialog" style="width: 80%">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
					<h4 class="modal-title"><spring:message code="input.modal.batchExpirationDateModal.title"/></h4>
				</div>
				<div id="batchExpirationDateModalAlertDiv"></div>
				<div class="modal-body">
				<div class="container-fluid">
					<div class="row">
						<div class="col-md-12 col-lg-12 form-group">
							<label><spring:message code="common.product"/>:&nbsp;&nbsp;</label>
							<label id="batchExpirationDateProductLabel"></label>
						</div>
					</div>
					<div class="row">
						<div class="col-md-4 col-lg-4 form-group" id="batchExpirationDateRequestedAmountDiv">
							<label><spring:message code="common.amount"/>:&nbsp;&nbsp;</label>
							<span style="color:black"><label id="batchExpirationDateRequestedAmountLabel"></label></span>
						</div>
						<div class="col-md-4 col-lg-4 form-group" id="batchExpirationDateEnteredAmountDiv">
							<label><spring:message code="common.entered"/>:&nbsp;&nbsp;</label>
							<span style="color:blue"><label id="batchExpirationDateEnteredAmountLabel"></label></span>
						</div>
						<div class="col-md-4 col-lg-4 form-group" id="batchExpirationDateRemainingAmountDiv">
							<label><spring:message code="common.remaining"/>:&nbsp;&nbsp;</label>
							<span style="color:red"><label id="batchExpirationDateRemainingAmountLabel"></label></span>
						</div>
					</div>
					<div class="row">
						<div class="col-md-3 col-lg-3 form-group">
							<input type="text" name="batchExpirationDateBatch" id="batchExpirationDateBatchInput" placeholder='<spring:message code="input.batch.placeholder"/>' class="form-control">
						</div>
						<div class="col-md-3 col-lg-3 form-group">
							<input type="text" name="batchExpirationDateExpirationDate" id="batchExpirationDateExpirationDateInput" placeholder='<spring:message code="input.expirationDate.placeholder"/>' class="form-control">
						</div>
						<div class="col-md-3 col-lg-3 form-group">
							<input type="text" name="batchExpirationDateAmount" id="batchExpirationDateAmountInput" placeholder='<spring:message code="input.amount.placeholder"/>' class="form-control">
						</div>
						<div class="col-md-3 col-lg-3 form-group">
							<button id="batchExpirationDateAddButton" type="button" class="btn btn-warning"> <span class="glyphicon glyphicon-plus"></span>Asignar</button>
						</div>
					</div>
					<br>
						<table id="batchExpirationDateTable" class="table table-condensed table-hover table-striped">
							<thead>
								<tr>
									<th data-identifier="true" data-column-id="id" data-type="numeric" data-visible="false" data-sortable="false"></th>
									<th data-column-id="batch" data-css-class="batch" data-sortable="false"><spring:message code="common.batch" /></th>
									<th data-column-id="expirationDate" data-css-class="expirationDate" data-sortable="false"><spring:message code="common.expirationDate" /></th>
									<th data-column-id="amount" data-type="numeric" data-css-class="amount" data-sortable="false"><spring:message code="common.amount" /></th>
									<th data-column-id="commands" data-formatter="commands" data-sortable="false" data-searchable="false"><spring:message code="administration.commands.tableLabel"/></th>
								</tr>
							</thead>
							<tbody id="batchExpirationDateTableBody">
							</tbody>
						</table>
					</div>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal"><spring:message code="common.abort"/></button>
					<button type="button" class="btn btn-primary" id="batchExpirationDateAcceptButton"><spring:message code="common.confirm"/></button>
				</div>
			</div>
		</div>
	</div>
</form>

<%-- Modal Ingreso Serializado de Origen --%>
<form id="providerSerializedModalForm" action="" onsubmit="return false;">
	<div class="modal fade" data-backdrop="static" data-keyboard="false" id="providerSerializedModal">
		<div class="modal-dialog" style="width: 80%">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
					<h4 class="modal-title"><spring:message code="input.modal.providerSerializedModal.title"/></h4>
				</div>
				<div id="providerSerializedModalAlertDiv"></div>
				<div class="modal-body">
				<div class="container-fluid">
					<div class="row">
						<div class="col-md-12 col-lg-12 form-group">
							<label><spring:message code="common.product"/>:&nbsp;&nbsp;</label>
							<label id="providerSerializedProductLabel"></label>
						</div>
					</div>
					<div class="row">
						<div class="col-md-4 col-lg-4 form-group" id="providerSerializedRequestedAmountDiv">
							<label><spring:message	code="common.amount"/>:&nbsp;&nbsp;</label>
							<span style="color:black"><label id="providerSerializedRequestedAmountLabel"></label></span>
						</div>
						<div class="col-md-4 col-lg-4 form-group" id="providerSerializedEnteredAmountDiv">
							<label><spring:message code="common.entered"/>:&nbsp;&nbsp;</label>
							<span style="color:blue"><label id="providerSerializedEnteredAmountLabel"></label></span>
						</div>
						<div class="col-md-4 col-lg-4 form-group" id="providerSerializedRemainingAmountDiv">
							<label><spring:message code="common.remaining"/>:&nbsp;&nbsp;</label>
							<span style="color:red"><label id="providerSerializedRemainingAmountLabel"></label></span>
						</div>
					</div>
					<div class="row">
						<div class="col-md-6 col-lg-6 form-group">
							<input id="readSerialNumberInput" name="readSerialNumber" placeholder='<spring:message code="common.readSerial"/>' type="text" class="form-control">
						</div>
						<div class="col-md-2 col-lg-2 form-group">
							<input id="providerSerializedBatchInput" name="providerSerializedBatch" placeholder='<spring:message code="input.batch.placeholder"/>' type="text" class="form-control">
						</div>
						<div class="col-md-2 col-lg-2 form-group">
							<input id="providerSerializedExpirationDateInput" name="providerSerializedExpirationDate"  placeholder='<spring:message code="input.expirationDate.placeholder"/>' type="text" class="form-control">
						</div>
						<div class="col-md-2 col-lg-2 form-group">
							<button id="providerSerializedAddButton" type="button" class="btn btn-warning"><span class="glyphicon glyphicon-plus"></span><spring:message code="common.add"/></button>
						</div>
					</div>
                    <div class="row">
                        <div class="col-md-3 col-lg-3 form-group">
                            <label><spring:message	code="common.gtin"/>:&nbsp;&nbsp;</label>
                            <span style="font-style:italic;color:darkorange"><label id="providerSerializedLastGtin"></label></span>
                        </div>
                        <div class="col-md-3 col-lg-3 form-group">
                            <label><spring:message code="common.serialNumber"/>:&nbsp;&nbsp;</label>
                            <span style="font-style:italic;color:darkviolet"><label id="providerSerializedLastSerialNumber"></label></span>
                        </div>
                        <div class="col-md-3 col-lg-3 form-group">
                            <label><spring:message code="common.batch"/>:&nbsp;&nbsp;</label>
                            <span style="font-style:italic;color:forestgreen"><label id="providerSerializedLastBatch"></label></span>
                        </div>
                        <div class="col-md-3 col-lg-3 form-group">
                            <label><spring:message code="common.expirationDate"/>:&nbsp;&nbsp;</label>
                            <span style="font-style:italic;color:brown"><label id="providerSerializedLastExpirationDate"></label></span>
                        </div>
                    </div>
					<br>
						<table id="providerSerializedTable" class="table table-condensed table-hover table-striped">
							<thead>
								<tr>
									<th data-column-id="gtin" data-css-class="gtin" data-sortable="false"><spring:message code="common.gtin"/></th>
									<th data-identifier="true" data-column-id="serialNumber" data-css-class="serialNumber" data-sortable="false"><spring:message code="common.serialNumber"/></th>
									<th data-column-id="batch" data-css-class="batch" data-sortable="false"><spring:message code="common.batch"/></th>
									<th data-column-id="expirationDate" data-css-class="expirationDate" data-sortable="false"><spring:message code="common.expirationDate"/></th>
									<th data-column-id="commands" data-formatter="commands" data-sortable="false" data-searchable="false"><spring:message code="administration.commands.tableLabel"/></th>
								</tr>
							</thead>
							<tbody>
							</tbody>
						</table>
				</div>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal"><spring:message code="common.abort"/></button>
					<button type="button" class="btn btn-primary" id="providerSerializedAcceptButton"><spring:message code="common.confirm"/></button>
				</div>
			</div>
		</div>
	</div>
</form>


<div class="modal fade" data-backdrop="static" id="formatSerializedModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog" style="width: 40%">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
				<h4 class="modal-title"><spring:message code="input.formatSerialized.title"/></h4>
			</div>
			<div class="modal-body">
				<div class="row">
					<div class="col-md-12 col-lg-12 form-group">
						<label for="formatSerializedInput"><spring:message code="common.formatSerial"/></label>
						<select id="formatSerializedInput" name="formatSerialized" class="form-control" data-placeholder="<spring:message code='common.select.option'/>" autofocus>
						</select>
					</div>
				</div>
			</div>
			<div class="modal-footer">
				<button type="button" id="cancel" class="btn btn-default" data-dismiss="modal"><spring:message code="common.no"/></button>
				<button type="button" class="btn btn-primary" data-dismiss="modal" id="formatSerializedAccept"><spring:message code="common.yes"/></button>
			</div>
		</div>
	</div>
</div>

<%-- Modal Ingreso Serializado Propio y Generacion de Etiquetas --%>
<form id="selfSerializedModalForm" action="" onsubmit="return false;">
	<div class="modal fade" data-backdrop="static" data-keyboard="false" id="selfSerializedModal">
		<div class="modal-dialog" style="width: 80%">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
					<h4 class="modal-title"><spring:message code="input.modal.selfSerializedModal.title"/></h4>
				</div>
				<div id="selfSerializedModalAlertDiv"></div>
				<div class="modal-body">
				<div class="container-fluid">
					<div class="row">
						<div class="col-md-12 col-lg-12 form-group">
							<label><spring:message code="common.product"/>:&nbsp;&nbsp;</label>
							<label id="selfSerializedProductLabel"></label>
						</div>
					</div>
					<div class="row">
						<div class="col-md-4 col-lg-4 form-group" id="selfSerializedRequestedAmountDiv">
							<label><spring:message code="common.amount"/>:&nbsp;&nbsp;</label>
							<span style="color:black"><label id="selfSerializedRequestedAmountLabel"></label></span>
						</div>
						<div class="col-md-4 col-lg-4 form-group" id="selfSerializedEnteredAmountDiv">
							<label><spring:message code="common.entered"/>:&nbsp;&nbsp;</label>
							<span style="color:blue"><label id="selfSerializedEnteredAmountLabel"></label></span>
						</div>
						<div class="col-md-4 col-lg-4 form-group" id="selfSerializedRemainingAmountDiv">
							<label><spring:message code="common.remaining"/>:&nbsp;&nbsp;</label>
							<span style="color:red"><label id="selfSerializedRemainingAmountLabel"></label></span>
						</div>
					</div>
					<div class="row">
						<div class="col-md-3 col-lg-3 form-group">
							<input type="text" name="selfSerializedBatch" id="selfSerializedBatchInput" placeholder='<spring:message code="input.batch.placeholder"/>' class="form-control">
						</div>
						<div class="col-md-3 col-lg-3 form-group">
							<input type="text" name="selfSerializedExpirationDate" id="selfSerializedExpirationDateInput" placeholder='<spring:message code="input.expirationDate.placeholder"/>' class="form-control">
						</div>
						<div class="col-md-3 col-lg-3 form-group">
							<input type="text" name="selfSerializedAmount" id="selfSerializedAmountInput" placeholder='<spring:message code="input.amount.placeholder"/>' class="form-control">
						</div>
						<div class="col-md-3 col-lg-3 form-group">
							<button id="selfSerializedGenerateButton" type="button" class="btn btn-warning"> <span class="glyphicon glyphicon-plus"></span><spring:message code="input.modal.selfSerializedModal.Generate"/></button>
						</div>
					</div>
					<br>
						<table id="selfSerializedTable" class="table table-condensed table-hover table-striped">
							<thead>
								<tr>
									<th data-identifier="true" data-column-id="id" data-type="numeric" data-visible="false" data-sortable="false"></th>
									<th data-column-id="batch" data-css-class="batch" data-sortable="false"><spring:message code="common.batch" /></th>
									<th data-column-id="expirationDate" data-css-class="expirationDate" data-sortable="false"><spring:message code="common.expirationDate" /></th>
									<th data-column-id="amount" data-type="numeric" data-css-class="amount" data-sortable="false"><spring:message code="common.amount" /></th>
									<th data-column-id="commands" data-formatter="commands" data-sortable="false" data-searchable="false"><spring:message code="administration.commands.tableLabel"/></th>
								</tr>
							</thead>
							<tbody>
							</tbody>
						</table>
				</div>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal"><spring:message code="common.abort"/></button>
					<button type="button" class="btn btn-primary" id="selfSerializedAcceptButton"><spring:message code="common.confirm"/></button>
				</div>
			</div>
		</div>
	</div>
</form>

<%-- Confirmaci�n del delete de una row --%>
<div class="modal fade" data-backdrop="static" id="deleteRowConfirmationModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog" style="width: 20%">
		<div class="modal-content">
			<div class="modal-body">
				<strong><span style="color:red"><spring:message code="input.deleteRowConfirmationModal.text"/></span></strong>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal"><spring:message code="common.no"/></button>
				<button type="button" class="btn btn-primary" data-dismiss="modal" id="inputDeleteRowConfirmationButton"><spring:message code="common.yes"/></button>
			</div>
		</div>
	</div>
</div>

<%-- Confirmaci�n del delete de una row --%>
<div class="modal fade" data-backdrop="static" id="deleteInputConfirmationModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog" style="width: 20%">
		<div class="modal-content">
			<div class="modal-body">
				<strong><span style="color:red"><spring:message code="input.cancelInputConfirmationModal.text"/></span></strong>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal"><spring:message code="common.no"/></button>
				<button type="button" class="btn btn-primary" data-dismiss="modal" id="cancelInputConfirmation"><spring:message code="common.yes"/></button>
			</div>
		</div>
	</div>
</div>

<%-- Confirmaci�n del delete de una row --%>
<div class="modal fade" data-backdrop="static" id="forcedInputConfirmationModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog" style="width: 40%">
		<div class="modal-content">
            <div class="modal-body">
                <strong><span style="color:red"><spring:message code="input.forcedInputConfirmationModal.text"/></span></strong>
            </div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal"><spring:message code="common.no"/></button>
				<button type="button" class="btn btn-primary" data-dismiss="modal" id="authorizeWithoutInform"><spring:message code="common.yes"/></button>
			</div>
		</div>
	</div>
</div>

