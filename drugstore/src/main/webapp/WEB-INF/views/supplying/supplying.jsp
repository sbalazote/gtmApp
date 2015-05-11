<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<script type="text/javascript" src="js/form/supplying/supplying.js"></script>
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
		<div class="col-md-6 form-group">
			<label for="clientInput"><spring:message code="common.client" /></label>
			<select id="clientInput" name="client" class="form-control chosen-select" data-placeholder="<spring:message code='common.select.option'/>">
				<option value=""></option>
				<c:forEach items="${clients}" var="client">
					<option value="${client.id}" ${clientId == client.id ? 'selected' : ''}><c:out value="${client.code}"></c:out> - <c:out value="${client.name}"></c:out></option>
				</c:forEach>
			</select>
		</div>
		<div class="col-md-6 form-group">
			<label for="affiliateInput"><spring:message code="common.affiliate" /></label>
			<input type='hidden' id="affiliateInput" name="affiliate" class="form-control">
		</div>
	</div>

	<div class="row">
		<div class="col-md-12 form-group">
			<label for="productInput"><spring:message code="common.addProduct"/></label>
			<input id="productInput" type="search" placeholder='<spring:message code="common.search"/>' class="form-control" name="product" autosave="" results="5" incremental="incremental" disabled/>
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
			<c:forEach items="${productsSelected}" var="supplyingDetail" varStatus="status">
				<tr>
					<td><c:out value="${supplyingDetail.product.code}"></c:out> - <c:out value="${supplyingDetail.product.description}"></c:out>
					<span class='span-productId' style='display:none'>${supplyingDetail.product.id}</span>
					</td>
					<td class='td-amount'><c:out value="${supplyingDetail.amount}"></c:out></td>
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

<%-- Confirmación del delete de una row --%>
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