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
	<div class="col-md-6 form-group">
		<h1><spring:message code="input.label"/></h1>
		<input type="hidden" class="form-control" id="inputId" value="${inputId != null ? inputId : ''}">
	</div>
	<div class="col-md-3">
		<div id="divInputId" style="display:none;">
			<h3><spring:message code="common.number" />: <span style="color:blue">${inputId != null ? inputId : ''}</span></h3>
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
		<label for="conceptInput"><spring:message code="common.concept"/></label>
		<select id="conceptInput" name="concept" class="form-control chosen-select" data-placeholder="<spring:message code='common.select.option'/>">
			<option value=""></option>
			<c:forEach items="${concepts}" var="concept">
				<option value="${concept.id}"  ${conceptId == concept.id ? 'selected' : ''}><c:out value="${concept.code}"></c:out> - <c:out value="${concept.description}"></c:out></option>
			</c:forEach>
		</select>
	</div>
	<div id="providerDiv" class="col-md-4 form-group" >
		<label for="providerInput"><spring:message code="common.provider"/></label>
		<select id="providerInput" name="provider" class="form-control chosen-select" data-placeholder="<spring:message code='common.select.option'/>">
			<option value=""></option>
			<c:forEach items="${providers}" var="provider">
				<option value="${provider.id}" ${providerId == provider.id ? 'selected' : ''}><c:out value="${provider.code}"></c:out> - <c:out value="${provider.name}"></c:out></option>
			</c:forEach>
		</select>
	</div>
	<div id="deliveryLocationDiv" class="col-md-4 form-group">
		<label for="deliveryLocationInput"><spring:message code="common.originLocation"/></label>
		<select id="deliveryLocationInput" name="deliveryLocation" class="form-control chosen-select" data-placeholder="<spring:message code='common.select.option'/>">
			<option value=""></option>
			<c:forEach items="${deliveryLocations}" var="deliveryLocation">
				<option value="${deliveryLocation.id}" ${deliveryLocationId == deliveryLocation.id ? 'selected' : ''}><c:out value="${deliveryLocation.code}"></c:out> - <c:out value="${deliveryLocation.name}"></c:out></option>
			</c:forEach>
		</select>
	</div>
	<div class="col-md-4 form-group">
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
	<div class="col-md-6 form-group">
		<label for="deliveryNoteNumberInput"><spring:message code="common.deliveryNote"/></label>
		<div class="input-group">
			<input name="deliveryNotePOS" id="deliveryNotePOSInput" type="text" class="form-control" placeholder=<spring:message code="common.deliveryNote.POS"/> value="${deliveryNotePOSInput != null ? deliveryNotePOSInput : ''}">
  			<span class="input-group-addon">-</span>
  			<input name="deliveryNoteNumber" id="deliveryNoteNumberInput" type="text" class="form-control" placeholder=<spring:message code="common.deliveryNote.number"/> value="${deliveryNoteNumberInput != null ? deliveryNoteNumberInput : ''}">
		</div>
	</div>
	<div class="col-md-6 form-group">
		<label for="purchaseOrderNumberInput"><spring:message code="common.purchaseOrder"/></label>
		<input type="text" class="form-control" name="purchaseOrderNumber" id="purchaseOrderNumberInput" value="${purchaseOrderNumber != null ? purchaseOrderNumber : ''}">
	</div>
</div>

<div class="row">
	<div class="col-md-12 form-group">
		<label for="productInput"><spring:message code="common.addProduct"/></label>
		<input id="productInput" type="search" placeholder='<spring:message code="input.product.placeholder"/>' class="form-control" name="product" autosave="" results="5" incremental="incremental" />
	</div>
</div>

<br>

<div>
	<table class="table table-striped my-table">
		<thead>
	        <tr>
	            <th><spring:message code="common.product"/></th>
	            <th><spring:message code="common.amount"/></th>
	            <c:if test="${inputId != null}">
	            	<th><spring:message code="common.serialNumber"/></th>
	            	<th><spring:message code="common.batch"/></th>
	            	<th><spring:message code="common.expirationDate"/></th>
	            </c:if>
	            <th></th>
	            <th></th>
	        </tr>
   	 	</thead>
   	 	<tbody id="productTableBody">
   	 		<c:forEach items="${products}" var="inputDetail" varStatus="status">
				<tr>
					<td class='td-description'><c:out value="${inputDetail.product.code}"></c:out> - <c:out value="${inputDetail.product.description}"></c:out></td>
					<td class='td-amount'><c:out value="${inputDetail.amount}"></c:out>
						<span class='span-productId' style='display:none'><c:out value="${inputDetail.product.id}"></c:out></span>
						<span class='span-productType' style='display:none'><c:out value="${inputDetail.product.type}"></c:out></span>
						<span class='span-productGtin' style='display:none'><c:out value="${inputDetail.gtin.number}"></c:out></span>
					</td>
					<td><c:out value="${inputDetail.serialNumber != null ? inputDetail.serialNumber : ''}"></c:out></td>
					<td><c:out value="${inputDetail.batch != null ? inputDetail.batch : ''}"></c:out></td>
					<td><c:out value="${inputDetail.expirationDate != null ? inputDetail.expirationDate : ''}"></c:out></td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</div>

<div class="row">
	<c:if test="${inputId != null}">
		<div class="col-md-2">
			<button class="btn btn-danger btn-block" id="delete"><span class="glyphicon glyphicon-remove"></span> <spring:message code="input.cancellation.button"/></button>
		</div>
		<div class="col-md-2 col-md-offset-4">
			<button class="btn btn-danger btn-block" onclick="location.href='home.do'" id="abortButton"><span class="glyphicon glyphicon-remove"></span> <spring:message code="common.abort"/></button>
		</div>
		<div class="col-md-2">
			<button class="btn btn-info" id="forcedInput"><span class="glyphicon glyphicon-log-in"></span> <spring:message code="input.authorizeWithoutInform.button"/></button>
		</div>
		<div class="col-md-2">
			<button type="submit" class="btn btn-success btn-block" id="confirmButton"><span class="glyphicon glyphicon-ok"></span> <spring:message code="input.authorize.button"/></button>
		</div>
	</c:if>
	<c:if test="${inputId == null}">
		<div class="col-md-2 col-md-offset-8">
			<button class="btn btn-danger btn-block" onclick="location.href='home.do'" id="abortButton"><span class="glyphicon glyphicon-remove"></span> <spring:message code="common.abort"/></button>
		</div>
		<div class="col-md-2">
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
		<div class="modal-dialog" style="width: 900px">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
					<h4 class="modal-title"><spring:message code="input.modal.batchExpirationDateModal.title"/></h4>
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
						<div class="col-md-3 form-group">
							<input type="text" name="batch" id="batchInput" placeholder='<spring:message code="input.batch.placeholder"/>' class="form-control">
						</div>
						<div class="col-md-3 form-group">
							<input type="text" name="expirationDate" id="expirationDateInput" placeholder='<spring:message code="input.expirationDate.placeholder"/>' class="form-control">
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

<%-- Modal Ingreso Serializado de Origen --%>
<form id="providerSerializedModalForm" action="" onsubmit="return false;">
	<div class="modal fade" data-backdrop="static" data-keyboard="false" id="providerSerializedModal">
		<div class="modal-dialog" style="width: 1100px">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
					<h4 class="modal-title"><spring:message code="input.modal.providerSerializedModal.title"/></h4>
				</div>
				<div id="providerSerializedModalAlertDiv"></div>
				<div class="modal-body">
					<div class="row">
						<div class="col-md-12 form-group">
							<label><spring:message code="common.product"/>:&nbsp;&nbsp;</label>
							<label id="providerSerializedProductLabel"></label>
						</div>
					</div>
					<div class="row">
						<div class="col-md-4 form-group">
							<label><spring:message	code="common.amount"/>:&nbsp;&nbsp;</label>
							<span style="color:black"><label id="providerSerializedRequestedAmountLabel"></label></span>
						</div>
						<div class="col-md-4 form-group">
							<label><spring:message code="common.entered"/>:&nbsp;&nbsp;</label>
							<span style="color:blue"><label id="providerSerializedEnteredAmountLabel"></label></span>
						</div>
						<div class="col-md-4 form-group">
							<label><spring:message code="common.remaining"/>:&nbsp;&nbsp;</label>
							<span style="color:red"><label id="providerSerializedRemainingAmountLabel"></label></span>
						</div>
					</div>
					<div class="row">
						<div class="col-md-6 form-group">
							<input id="readSerialNumberInput" name="readSerialNumber" placeholder='<spring:message code="common.readSerial"/>' type="text" class="form-control">
						</div>
						<div class="col-md-2 form-group">
							<input id="providerSerializedBatchInput" name="providerSerializedBatch" placeholder='<spring:message code="input.batch.placeholder"/>' type="text" class="form-control">
						</div>
						<div class="col-md-2 form-group">
							<input id="providerSerializedExpirationDateInput" name="providerSerializedExpirationDate"  placeholder='<spring:message code="input.expirationDate.placeholder"/>' type="text" class="form-control">
						</div>
						<div class="col-md-2 form-group">
							<button id="providerSerializedAddButton" type="button" class="btn btn-warning"><span class="glyphicon glyphicon-plus"></span><spring:message code="common.add"/></button>
						</div>
					</div>
					<div>
						<table id="providerSerializedTable" class="table">
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
					<button type="button" class="btn btn-primary" id="providerSerializedAcceptButton"><spring:message code="common.confirm"/></button>
				</div>
			</div>
		</div>
	</div>
</form>

<%-- Modal Ingreso Serializado Propio y Generacion de Etiquetas --%>
<form id="selfSerializedModalForm" action="" onsubmit="return false;">
	<div class="modal fade" data-backdrop="static" data-keyboard="false" id="selfSerializedModal">
		<div class="modal-dialog" style="width: 900px">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
					<h4 class="modal-title"><spring:message code="input.modal.selfSerializedModal.title"/></h4>
				</div>
				<div id="selfSerializedModalAlertDiv"></div>
				<div class="modal-body">
					<div class="row">
						<div class="col-md-12 form-group">
							<label><spring:message code="common.product"/>:&nbsp;&nbsp;</label>
							<label id="selfSerializedProductLabel"></label>
						</div>
					</div>
					<div class="row">
						<div class="col-md-4 form-group">
							<label><spring:message code="common.amount"/>:&nbsp;&nbsp;</label>
							<span style="color:black"><label id="selfSerializedRequestedAmountLabel"></label></span>
						</div>
						<div class="col-md-4 form-group">
							<label><spring:message code="common.entered"/>:&nbsp;&nbsp;</label>
							<span style="color:blue"><label id="selfSerializedEnteredAmountLabel"></label></span>
						</div>
						<div class="col-md-4 form-group">
							<label><spring:message code="common.remaining"/>:&nbsp;&nbsp;</label>
							<span style="color:red"><label id="selfSerializedRemainingAmountLabel"></label></span>
						</div>
					</div>
					<div class="row">
						<div class="col-md-3 form-group">
							<input type="text" name="selfSerializedBatch" id="selfSerializedBatchInput" placeholder='<spring:message code="input.batch.placeholder"/>' class="form-control">
						</div>
						<div class="col-md-3 form-group">
							<input type="text" name="selfSerializedExpirationDate" id="selfSerializedExpirationDateInput" placeholder='<spring:message code="input.expirationDate.placeholder"/>' class="form-control">
						</div>
						<div class="col-md-3 form-group">
							<input type="text" name="selfSerializedAmount" id="selfSerializedAmountInput" placeholder='<spring:message code="input.amount.placeholder"/>' class="form-control">
						</div>
						<div class="col-md-3 form-group">
							<button id="selfSerializedGenerateButton" type="button" class="btn btn-warning"> <span class="glyphicon glyphicon-plus"></span><spring:message code="input.modal.selfSerializedModal.Generate"/></button>
						</div>
					</div>
					<div>
						<table id="selfSerializedTable" class="table">
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
					<button type="button" class="btn btn-primary" id="selfSerializedAcceptButton"><spring:message code="common.confirm"/></button>
				</div>
			</div>
		</div>
	</div>
</form>

<%-- Confirmaci�n del delete de una row --%>
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

<%-- Confirmaci�n del delete de una row --%>
<div class="modal fade" data-backdrop="static" id="forcedInputConfirmationModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog" style="width:250px">
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

<%-- Modal Productos Serializacion Propia de un eslabon anterior --%>
<form id="productOthersSelfSerielizedForm" action="" onsubmit="return false;">
    <div class="modal fade" data-backdrop="static" id="productOthersSelfSerielized" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
        <div class="modal-dialog" style="width:950px">
            <div class="modal-content">
                <div class="modal-body">
                    <div class="row">
                        <div class="col-md-12 form-group">
                            <label for="productDescriptionOthersSelfSerielized"><spring:message code="common.product"/></label>
                            <input type="text" class="form-control" name="productDescriptionOthersSelfSerielized" id="productDescriptionOthersSelfSerielized"/>
                        </div>
                        <div class="col-md-12 form-group">
                            <label for="productGtinOthersSelfSerielized"><spring:message code="common.gtin"/></label>
                            <select id="productGtinOthersSelfSerielized" name="productGtinOthersSelfSerielized" class="form-control chosen-select" data-placeholder="<spring:message code='common.select.option'/>">
                                <option value=""></option>
                            </select>
                        </div>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal"><spring:message code="common.abort"/></button>
                    <button type="button" class="btn btn-primary" id="othersSelfSerielizedAcceptButton"><spring:message code="common.accept"/></button>
                </div>
            </div>
        </div>
    </div>
</form>
