<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<script type="text/javascript" src="js/form/output/output.js"></script>
<script type="text/javascript" src="js/form/output/outputBatchExpirateDate.js"></script>
<script type="text/javascript" src="js/form/output/outputSerialized.js"></script>
<script type="text/javascript">
	$(document).ready(function() {
		new Output();
	});
</script>

<form id="outputForm" action="" onsubmit="return false;">

<div class="row">
	<div class="col-md-9 form-group">
		<h3><spring:message code="common.output"/></h3>
	</div>
	<div class="col-md-3 form-group">
		<label for="currentDateInput"><spring:message code="common.date"/></label>
		<div class="input-group">
			<input type="text" class="form-control" name="currentDate" id="currentDateInput">
			<span class="input-group-addon" id="currentDateButton" style="cursor:pointer;">
				<span class="glyphicon glyphicon-calendar"></span>
			</span>
		</div>
	</div>
</div>

<div class="row">
	<div class="col-md-4 form-group">
		<label for="conceptInput"><spring:message code="common.concept"/></label>
		<select id="conceptInput" name="concept" class="form-control chosen-select" data-placeholder="<spring:message code='common.select.option'/>">
			<option value=""></option>
			<c:forEach items="${concepts}" var="concept">
				<option value="${concept.id}"><c:out value="${concept.code}"></c:out> - <c:out value="${concept.description}"></c:out></option>
			</c:forEach>
		</select>
	</div>
	<div id="providerDiv" class="col-md-4 form-group">
		<label for="providerInput"><spring:message code="common.provider"/></label>
		<select id="providerInput" name="provider" class="form-control chosen-select" data-placeholder="<spring:message code='common.select.option'/>">
			<option value=""></option>
			<c:forEach items="${providers}" var="provider">
				<option value="${provider.id}"><c:out value="${provider.code}"></c:out> - <c:out value="${provider.name}"></c:out></option>
			</c:forEach>
		</select>
	</div>
	<div id="deliveryLocationDiv" class="col-md-4 form-group">
		<label for="deliveryLocationInput"><spring:message code="common.deliveryLocation"/></label>
		<select id="deliveryLocationInput" name="deliveryLocation" class="form-control chosen-select" data-placeholder="<spring:message code='common.select.option'/>">
			<option value=""></option>
			<c:forEach items="${deliveryLocations}" var="deliveryLocation">
				<option value="${deliveryLocation.id}"><c:out value="${deliveryLocation.code}"></c:out> - <c:out value="${deliveryLocation.name}"></c:out></option>
			</c:forEach>
		</select>
	</div>
	<div class="col-md-4 form-group">
		<label for="agreementInput"><spring:message code="common.agreement"/></label>
		<select id="agreementInput" name="agreement" class="form-control chosen-select" data-placeholder="<spring:message code='common.select.option'/>">
			<option value=""></option>
			<c:forEach items="${agreements}" var="agreement">
				<option value="${agreement.id}"><c:out value="${agreement.code}"></c:out> - <c:out value="${agreement.description}"></c:out></option>
			</c:forEach>
		</select>
	</div>
</div>

<div class="row">
	<div class="col-md-12 form-group">
		<label for="productOutput"><spring:message code="common.addProduct"/></label>
		<input id="productOutput" type="search" placeholder='<spring:message code="input.product.placeholder"/>' class="form-control" name="product" autosave="" results="5" incremental="incremental" />
	</div>
</div>

<br>

<div>
	<table class="table table-striped my-table">
		<thead>
	        <tr>
	            <th><spring:message code="common.product"/></th>
	            <th><spring:message code="common.amount"/></th>
	            <th></th>
	            <th></th>
	        </tr>
   	 	</thead>
   	 	<tbody id="productTableBody">
		</tbody>
	</table>
</div>

<div class="row">
	<div class="col-md-2 col-md-offset-8">
		<button class="btn btn-danger btn-block" onclick="location.href='home.do'" id="abortButton"><span class="glyphicon glyphicon-remove"></span> <spring:message code="common.abort"/></button>
	</div>
	<div class="col-md-2">
		<button type="submit" class="btn btn-success btn-block" id="confirmButton"><span class="glyphicon glyphicon-ok"></span> <spring:message code="common.confirm"/></button>
	</div>
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

<%-- Confirmación del delete de una row --%>
<div class="modal fade" data-backdrop="static" id="deleteRowConfirmationModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog" style="width:250px">
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

<%-- Modal Ingreso Lote y Vencimiento --%>
<form id="batchExpirationDateModalForm" action="" onsubmit="return false;">
	<div class="modal fade" data-backdrop="static" data-keyboard="false" id="batchExpirationDateModal">
		<div class="modal-dialog" style="width: 900px">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
					<h4 class="modal-title"><spring:message code="orderAssembly.modal.batchExpirationDateModal.title"/></h4>
				</div>
				<div id="batchExpirationDateModalAlertDiv"></div>
				<div class="modal-body">
					<div class="row">
						<div class="col-md-12 form-group">
							<label><spring:message code="common.product"/>:&nbsp;&nbsp;</label>
							<label id="batchExpirationDateProductLabel"></label>
						</div>
					</div>
					<div class="row">
						<div class="col-md-4 form-group">
							<label><spring:message code="common.amount"/>:&nbsp;&nbsp;</label>
							<span style="color:black"><label id="batchExpirationDateRequestedAmountLabel"></label></span>
						</div>
						<div class="col-md-4 form-group">
							<label><spring:message code="common.entered"/>:&nbsp;&nbsp;</label>
							<span style="color:blue"><label id="batchExpirationDateEnteredAmountLabel"></label></span>
						</div>
						<div class="col-md-4 form-group">
							<label><spring:message code="common.remaining"/>:&nbsp;&nbsp;</label>
							<span style="color:red"><label id="batchExpirationDateRemainingAmountLabel"></label></span>
						</div>
					</div>
					<div class="row">
						<div class="col-md-6 form-group">
							<select id="batchExpirationDateSelect" name="batchExpirationDate" class="form-control chosen-select" data-placeholder="<spring:message code='orderAssembly.batchExpirationDate.placeholder'/>"></select>
						</div>
						<div class="col-md-3 form-group">
							<input type="text" name="amount" id="amountInput" placeholder='<spring:message code="input.amount.placeholder"/>' class="form-control">
						</div>
						<div class="col-md-3 form-group">
							<button id="batchExpirationDateAddButton" type="button" class="btn btn-warning"> <span class="glyphicon glyphicon-plus"></span>Asignar</button>
						</div>
					</div>
					<div>
						<table id="batchExpirationDateTable" class="table">
							<thead>
								<tr>
									<th><spring:message code="common.batch" /></th>
									<th><spring:message code="common.expirationDate" /></th>
									<th><spring:message code="common.amount" /></th>
									<th></th>
								</tr>
							</thead>
							<tbody>
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

<%-- Modal Ingreso Serializados --%>
<form id="serializedModalForm" action="" onsubmit="return false;">
	<div class="modal fade" data-backdrop="static" data-keyboard="false" id="serializedModal">
		<div class="modal-dialog" style="width: 900px">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
					<h4 class="modal-title"><spring:message code="orderAssembly.modal.serializedModal.title"/></h4>
				</div>
				<div id="serializedModalAlertDiv"></div>
				<div class="modal-body">
					<div class="row">
						<div class="col-md-12 form-group">
							<label><spring:message code="common.product"/>:&nbsp;&nbsp;</label>
							<label id="serializedProductLabel"></label>
						</div>
					</div>
					<div class="row">
						<div class="col-md-4 form-group">
							<label><spring:message	code="common.amount"/>:&nbsp;&nbsp;</label>
							<span style="color:black"><label id="serializedRequestedAmountLabel"></label></span>
						</div>
						<div class="col-md-4 form-group">
							<label><spring:message code="common.entered"/>:&nbsp;&nbsp;</label>
							<span style="color:blue"><label id="serializedEnteredAmountLabel"></label></span>
						</div>
						<div class="col-md-4 form-group">
							<label><spring:message code="common.remaining"/>:&nbsp;&nbsp;</label>
							<span style="color:red"><label id="serializedRemainingAmountLabel"></label></span>
						</div>
					</div>
					<div class="row">
						<div class="col-md-10 form-group">
							<input id="readSerialNumberInput" name="readSerialNumber" placeholder='<spring:message code="common.readSerial"/>' type="text" class="form-control">
						</div>
						<div class="col-md-2 form-group">
							<button id="serializedAddButton" type="button" class="btn btn-warning"><span class="glyphicon glyphicon-plus"></span><spring:message code="common.add"/></button>
						</div>
					</div>
					<div>
						<table id="serializedTable" class="table">
							<thead>
								<tr>
									<th style="display: none;" ><spring:message code="common.gtin"/></th>
									<th><spring:message code="common.serialNumber"/></th>
									<th><spring:message code="common.batch"/></th>
									<th><spring:message code="common.expirationDate"/></th>
									<th></th>
								</tr>
							</thead>
							<tbody>
							</tbody>
						</table>
					</div>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal"><spring:message code="common.abort"/></button>
					<button type="button" class="btn btn-primary" id="serializedAcceptButton"><spring:message code="common.confirm"/></button>
				</div>
			</div>
		</div>
	</div>
</form>