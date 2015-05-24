<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<script type="text/javascript" src="js/form/provisioningRequest/provisioningRequest.js"></script>
<script type="text/javascript">
	$(document).ready(function() {
		new ProvisioningRequest();
	});
</script>

<form id="provisioningRequestForm" action="" onsubmit="return false;" data-plus-as-tab="true">

	<div class="row">
		<div class="col-md-9 form-group">
			<h1 class="form-provisioningRequest-heading"><spring:message code="provisioningRequest.label" /></h1> 
			<input type="hidden" class="form-control" id="provisioningId" value="${provisioningRequestId != null ? provisioningRequestId : ''}">
		</div>
		<div id="divProvisioningId" class="col-md-3 form-group" style="display:none;">
			<h3 class="form-provisioningRequest-heading text-right"><spring:message code="common.number" />: <span style="color:blue">${provisioningRequestId != null ? provisioningRequestId : ''}</span></h3>
		</div> 
	</div>

	<div class="row">
		<div class="col-md-4 form-group">
			<label for="agreementInput"><spring:message code="common.agreement" /></label>
			<select id="agreementInput" name="agreement" class="form-control chosen-select" data-placeholder="<spring:message code='common.select.option'/>" autofocus>
				<option value=""></option>
				<c:forEach items="${agreements}" var="agreement">
					<option value="${agreement.id}" ${agreementId == agreement.id ? 'selected' : ''}><c:out value="${agreement.code}"></c:out> - <c:out value="${agreement.description}"></c:out></option>
				</c:forEach>
			</select>
		</div>
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
				<%-- <c:forEach items="${affiliates}" var="affiliate">
					<option value="${affiliate.id}" ${affiliateId == affiliate.id ? 'selected' : ''}><c:out value="${affiliate.code}"></c:out> - <c:out value="${affiliate.surname} ${affiliate.name}"></c:out></option>
				</c:forEach> --%>
		</div>
	</div>

	<div class="row">
		<div class="col-md-6 form-group">
			<label for="deliveryLocationInput"><spring:message code="common.deliveryLocation" /></label>
			<select id="deliveryLocationInput" name="deliveryLocation" class="form-control chosen-select" data-placeholder="<spring:message code='common.select.option'/>">
				<option value=""></option>
				<c:forEach items="${deliveryLocations}" var="deliveryLocation">
					<option value="${deliveryLocation.id}" ${deliveryLocationId == deliveryLocation.id ? 'selected' : ''}><c:out value="${deliveryLocation.code}"></c:out> - <c:out value="${deliveryLocation.name}"></c:out></option>
				</c:forEach>
			</select>
		</div>
		<div class="col-md-2 form-group">
			<label for="deliveryDateInput"><spring:message code="common.deliveryDate" /></label>
			<div class="input-group">
				<input type="hidden" id="deliveryDatePreloadedInput" name="deliveryDatePreloaded" value="<fmt:formatDate pattern="dd/MM/yyyy" value="${deliveryDate}"/>"/>
				<input type="text" class="form-control" name="deliveryDate" id="deliveryDateInput" value="<fmt:formatDate pattern="dd/MM/yyyy" value="${deliveryDate}"/>"/>
				<span class="input-group-addon" id="deliveryDateButton" style="cursor:pointer;">
					<span class="glyphicon glyphicon-calendar"></span>
				</span>
			</div>
		</div>
		<div class="col-md-4 form-group">
			<label for="logisticsOperatorInput"><spring:message code="common.logisticsOperator" /></label>
			<select id="logisticsOperatorInput" name="logisticsOperator" class="form-control chosen-select" data-placeholder="<spring:message code='common.select.option'/>">
				<option value=""></option>
				<c:forEach items="${logisticsOperators}" var="logisticsOperator">
					<option value="${logisticsOperator.id}" ${logisticsOperatorId == logisticsOperator.id ? 'selected' : ''}><c:out value="${logisticsOperator.code}"></c:out> - <c:out value="${logisticsOperator.name}"></c:out></option>
				</c:forEach>
			</select>
		</div>
	</div>
	
	<div class="row">
		<div class="col-md-12 form-group">
			<label for="productInput"><spring:message code="common.addProduct"/></label>
			<input id="productInput" type="search" placeholder='<spring:message code="common.search"/>' class="form-control" name="product" autosave="" results="5" incremental="incremental" disabled/>
		</div>
	</div>

	<div class="row">
		<div class="col-md-12 form-group">
			<label for="commentTextarea"><spring:message code="provisioningRequest.comment.label" /></label>
			<textarea class="form-control my-textarea" name="comment" id="commentTextarea" rows="3">${comment}</textarea>
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
			<c:forEach items="${productsSelected}" var="provisioningDetail" varStatus="status">
				<tr>
					<td><c:out value="${provisioningDetail.product.code}"></c:out> - <c:out value="${provisioningDetail.product.description}"></c:out>
					<span class='span-productId' style='display:none'>${provisioningDetail.product.id}</span>
					</td>
					<td class='td-amount'><c:out value="${provisioningDetail.amount}"></c:out></td>
					<td>
						<a href='javascript:void(0);' class='edit-row'><i class='glyphicon glyphicon-pencil'></i></a>&nbsp;&nbsp;&nbsp;&nbsp;<a href='javascript:void(0);' class='delete-row'><i class='glyphicon glyphicon-remove'></i></a>
					</td>
				</tr>
			</c:forEach>
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

<%-- Confirmaci�n del delete de una row --%>
<div class="modal fade" data-backdrop="static" id="deleteRowConfirmationModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog" style="width:250px">
		<div class="modal-content">
			<div class="modal-body">
				<strong><span style="color:red"><spring:message code="input.deleteRowConfirmationModal.text"/></span></strong>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal"><spring:message code="common.no"/></button>
				<button type="button" class="btn btn-primary" data-dismiss="modal" id="provisoningDeleteRowConfirmationButton"><spring:message code="common.yes"/></button>
			</div>
		</div>
	</div>
</div>