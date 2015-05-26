<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<script type="text/javascript" src="js/form/orderAssembly/orderAssembly.js"></script>
<script type="text/javascript" src="js/form/orderAssembly/orderAssemblyBatchExpirationDate.js"></script>
<script type="text/javascript" src="js/form/orderAssembly/orderAssemblySerialized.js"></script>
<script type="text/javascript">
	$(document).ready(function() {
		new OrderAssembly();
	});
</script>

<input type="hidden" class="form-control" id="provisioningRequestId" name="provisioningRequestId" value="${provisioningRequestId}">

<form id="orderAssemblyForm" action="" onsubmit="return false;">

<div class="row">
	<div class="col-md-8 form-group">
		<h3 class="form-provisioningRequest-heading"><spring:message code="common.orderAssembly" /></h3>
		<input type="hidden" class="form-control" id="agreementId" value="${agreement.id}">
	</div>
</div>

<div class="row">
	<div class="col-md-4 form-group">
		<label for="agreementInput"><spring:message code="common.agreement" /></label>
		<input id="agreementInput" name="agreement"  value="${agreement.code} - ${agreement.description}" class="form-control" disabled>
	</div>
	<div class="col-md-4 form-group">
		<label for="clientInput"><spring:message code="common.client" /></label>
		<input id="clientInput" name="client" value="${client.code} - ${client.name}" class="form-control" disabled>
	</div>
	<div class="col-md-4 form-group">
		<label for="affiliateInput"><spring:message code="common.affiliate" /></label>
		<input id="affiliateInput" name="affiliate" value="${affiliate.code} - ${affiliate.surname} ${affiliate.name}" class="form-control" disabled/>
	</div>
</div>

<div class="row">
	<div class="col-md-4 form-group">
		<label for="deliveryLocationInput"><spring:message code="common.deliveryLocation" /></label>
		<input id="deliveryLocationInput" name="deliveryLocation" value="${deliveryLocation.code} - ${deliveryLocation.name}" class="form-control" disabled>
	</div>
	<div class="col-md-4 form-group">
		<label for="deliveryDateInput"><spring:message code="common.deliveryDate" /></label>
		<input  id="deliveryDateInput" name="deliveryDate" value="<fmt:formatDate pattern="dd/MM/yyyy" value="${deliveryDate}"/>" class="form-control" disabled/>
	</div>
	<div class="col-md-4 form-group">	
		<label for="logisticsOperatorInput"><spring:message code="common.logisticsOperator"/></label>
		<input id="logisticsOperatorInput" name="logisticsOperator" value="${logisticsOperator != null ? logisticsOperator.name : ''}" class="form-control" disabled>
	</div>
</div>

<div class="row">
	<div class="col-md-12 form-group">
		<label for="productInput"><spring:message code="orderAssembly.readProductGTINSerialNumber.label"/></label>
		<input id="productInput" name="product" type="text" placeholder='<spring:message code="orderAssembly.readProductGTINSerialNumber.placeholder"/>' class="form-control" >
	</div>
</div>

<br>

<div>
	<table class="table table-striped my-table" id="mainTable">
		<thead>
	        <tr>
	            <th><spring:message code="common.product"/></th>
	            <th><spring:message code="orderAssembly.requestedAmount"/></th>
	            <th></th>
	        </tr>
   	 	</thead>
   	 	<tbody id="productTableBody">
			<c:forEach items="${provisioningRequestDetails}" var="provisioningRequestDetail" varStatus="status">
			<tr>
				<td class="td-description"><c:out value="${provisioningRequestDetail.product.code}"></c:out> - <c:out value="${provisioningRequestDetail.product.description}"></c:out></td>
				<td class="td-amount"><c:out value="${provisioningRequestDetail.amount}"></c:out></td>
				<td>
					<span class="span-productId" style="display:none"><c:out value="${provisioningRequestDetail.product.id}"></c:out></span>
					<span class="span-productType" style="display:none"><c:out value="${provisioningRequestDetail.product.type}"></c:out></span>
					<span class="span-productGtin" style="display:none"><c:out value="${provisioningRequestDetail.product.lastGtin}"></c:out></span>
					<a class="assign-button" href="javascript:void(0);"><spring:message code="orderAssembly.assign"/></a>
				</td>
			</tr>
			</c:forEach>
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