<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<form id="inputModalForm" action="" onsubmit="return false;">
	<div class="modal fade" data-backdrop="static" id="inputModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog" style="width: 1000px">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
					<div class="row">
						<div class="col-md-4">
							<h3><spring:message code="input.label"/></h3>
						</div>
						<div class="col-md-2">
							<h3 id="inputId"></h3>
						</div>
						<div class="col-md-2">
							<h3 id="cancelled" style="color:red;font-weight:bold"></h3>
						</div>
					</div>
					<div id="ANMATCode">
						<div class="row">
							<div class="col-md-4" >
								<h4 id="transactionCodeDescription"><spring:message code="common.transactionCodeDescription"/></h4>
							</div>
							<div class="col-md-2">
								<h4 id="transactionCode" style="color:blue;font-weight:bold"></h4>
							</div>
						</div>
					</div>
				</div>
				
				<div class="modal-body">
					<div class="row">
						<div class="col-md-4 form-group">
							<label for="dateModal"><spring:message code="common.date"/></label>
							<input id="dateModal" name="dateModal" class="form-control" disabled>
						</div>
						<div class="col-md-4 form-group">
							<label for="conceptModal"><spring:message code="common.concept"/></label>
							<input id="conceptModal" name="conceptModal" class="form-control" disabled>
						</div>
						<div class="col-md-4 form-group">
							<label for="clientOrProviderModal"><spring:message code="common.clientOrProvider"/></label>
							<input id="clientOrProviderModal" name="clientOrProviderModal" class="form-control" disabled>
						</div>
					</div>
					
					<div class="row">
						<div class="col-md-4 form-group">
							<label for="agreementModal"><spring:message code="common.agreement"/></label>
							<input id="agreementModal" name="agreementModal" class="form-control" disabled>
						</div>
						<div class="col-md-4 form-group">
							<label for="deliveryNoteNumberModal"><spring:message code="common.deliveryNote"/></label>
							<input type="text" class="form-control" name="deliveryNoteNumberModal" id="deliveryNoteNumberModal" disabled>
						</div>
						<div class="col-md-4 form-group">
							<label for="purchaseOrderNumberModal"><spring:message code="common.purchaseOrder"/></label>
							<input type="text" class="form-control" name="purchaseOrderNumberModal" id="purchaseOrderNumberModal" disabled>
						</div>
					</div>
					
					<br>
					
					<div>
						<table class="table table-striped my-table">
							<thead>
						        <tr>
						            <th><spring:message code="common.product"/></th>
						            <th><spring:message code="common.amount"/></th>
						            <th><spring:message code="common.serialNumber"/></th>
	            					<th><spring:message code="common.batch"/></th>
	            					<th><spring:message code="common.expirationDate"/></th>
						        </tr>
					   	 	</thead>
					   	 	<tbody id="productTableBody">
							</tbody>
						</table>
					</div>
					
					<div class="modal-footer">
						<button type="button" class="btn btn-default" data-dismiss="modal"><spring:message code="common.close"/></button>
					</div>
				</div>
			</div>
		</div>
	</div>
</form>