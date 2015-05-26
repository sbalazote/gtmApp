<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<script type="text/javascript" src="js/form/supplying/supplying.js"></script>
<script type="text/javascript" src="js/form/output/outputBatchExpirateDate.js"></script>
<script type="text/javascript" src="js/form/output/outputSerialized.js"></script>
<script type="text/javascript">
	$(document).ready(function() {
		new Supplying();
	});
</script>

<form id="supplyingForm" action="" onsubmit="return false;">

	<div class="row">
		<div class="col-md-6 form-group">
			<h1 class="form-supplying-heading"><spring:message code="supplying.label" /></h1> 
			<input type="hidden" class="form-control" id="supplyingId" value="${supplyingId != null ? supplyingId : ''}">
		</div>
		<div class="col-md-3">
			<div id="divSupplyingId" style="display:none;">
				<h3 class="form-supplying-heading text-right"><spring:message code="common.number" />: <span style="color:blue">${supplyingId != null ? supplyingId : ''}</span></h3>
			</div> 
		</div>
		<div class="col-md-3 form-group">
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
		<div class="col-md-4 form-group">
			<label for="clientInput"><spring:message code="common.client" /></label>
			<select id="clientInput" name="client" class="form-control chosen-select" data-placeholder="<spring:message code='common.select.option'/>">
				<option value=""></option>
				<c:forEach items="${clients}" var="client">
					<option value="${client.id}" ${clientId == client.id ? 'selected' : ''}><c:out value="${client.code}"></c:out> - <c:out value="${client.name}"></c:out></option>
				</c:forEach>
			</select>
		</div>
		<div class="col-md-4 form-group">
			<label for="affiliateInput"><spring:message code="common.affiliate" /></label>
			<input type='hidden' id="affiliateInput" name="affiliate" class="form-control">
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
			<label for="productInput"><spring:message code="common.addProduct"/></label>
			<input id="productInput" type="search" placeholder='<spring:message code="common.search"/>' class="form-control" name="product" autosave="" results="5" incremental="incremental"/>
		</div>
	</div>

	<br>

	<div>
		<table class="table table-striped my-table">
			<thead>
				<tr>
					<th><spring:message code="common.product" /></th>
					<th><spring:message code="common.amount" /></th>
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
			<button class="btn btn-danger btn-block" type="submit" id="abortButton" onclick="location.href='home.do'" >
				<span class="glyphicon glyphicon-remove"></span>
				<spring:message code="common.abort" />
			</button>
		</div>
		<div class="col-md-2">
			<button class="btn btn-success btn-block" type="submit" id="confirmButton">
				<span class="glyphicon glyphicon-ok"></span>
				<spring:message code="common.confirm" />
			</button>
		</div>
	</div>

</form>

<!-- Modal Agregar Afiliados -->
<form id="addAffiliateModalForm" action="" onsubmit="return false;">
	<div class="modal fade" data-backdrop="static" id="addAffiliateModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog" style="width:900px">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
					<h3 class="modal-title"><spring:message code="provisioningRequest.addAffiliate"/></h3>
				</div>
				<div id="addAffiliateModalAlertDiv"></div>
				<div class="modal-body">
					<div class="row">
						<div class="col-md-4 form-group">
							<label for="affiliateCodeInput"><spring:message code="common.code"/></label> 
							<input type="text" class="form-control" id="affiliateCodeInput" name="affiliateCode">
						</div>
						<div class="col-md-8 form-group">	
							<label for="affiliateClientInput"><spring:message code="common.client"/></label>
							<input id="affiliateClientInput" name="affiliateClient" class="form-control" disabled>
						</div>

					</div>

					<div class="row">
						<div class="col-md-4 form-group">
							<label for="affiliateSurnameInput"><spring:message code="common.surname"/></label> 
							<input type="text" class="form-control" id="affiliateSurnameInput" name="affiliateSurname">
						</div>
						<div class="col-md-8 form-group">
							<label for="affiliateNameInput"><spring:message code="common.firstname"/></label> 
							<input type="text" class="form-control" id="affiliateNameInput" name="affiliateName">
						</div>
					</div>

					<div class="row">
						<div class="col-md-4 form-group">
							<label for="affiliateDocumentTypeInput"><spring:message code="common.documentType"/></label>
							<input id="affiliateDocumentTypeInput" name="affiliateDocumentType" placeholder='<spring:message code="common.select.option"/>' class="form-control"> 
						</div>
					
						<div class="col-md-8 form-group">
							<label for="affiliateDocumentInput"><spring:message code="common.document"/></label> 
							<input type="text" class="form-control" id="affiliateDocumentInput" name="affiliateDocument">
						</div>
					</div>

				</div>
			
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal"><spring:message code="common.abort"/></button>
					<button type="button" class="btn btn-primary" id="addAffiliateModalAcceptButton"><spring:message code="common.accept"/></button>
				</div>
			</div>
		</div>
	</div>
</form>

<!-- Modal Cantidad de Productos -->
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