<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<div class="modal fade" data-backdrop="static" id="orderModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog" style="width: 1000px">
		<div class="modal-content">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
					<h3 class="modal-title">
						<spring:message code="deliveryNote.orderAssemblyModal.label" />
					</h3>
				</div>

				<div class="modal-body">
					<div class="row">
						<div class="col-md-8 form-group"></div>
					</div>

					<div class="row">
						<div class="col-md-4 form-group">
							<label for="orderModalAgreementInput"><spring:message code="common.agreement" /></label>
							<input id="orderModalAgreementInput" name="orderModalAgreement" class="form-control" disabled>
						</div>
						<div class="col-md-4 form-group">
							<label for="orderModalClientInput"><spring:message code="common.client" /></label>
							<input id="orderModalClientInput" name="orderModalClientInput" class="form-control" disabled>
						</div>
						<div class="col-md-4 form-group">
							<label for="orderModalAffiliateInput"><spring:message code="common.affiliate" /></label>
							<input id="orderModalAffiliateInput" name="orderModalAffiliate" type="text" class="form-control" disabled />
						</div>

					</div>

					<div class="row">
						<div class="col-md-4 form-group">
							<label for="orderModalDeliveryLocationInput"><spring:message code="common.deliveryLocation" /></label>
							<input id="orderModalDeliveryLocationInput" name="orderModalDeliveryLocation" class="form-control" disabled>
						</div>
						<div class="col-md-4 form-group">
							<label for="orderModalDeliveryDateInput"><spring:message code="common.deliveryDate" /></label>
							<input id="orderModalDeliveryDateInput" name="orderModalDeliveryDate" type="text" class="form-control" disabled>
						</div>
						<div class="col-md-4 form-group">
							<label for="orderModalLogisticsOperatorInput"><spring:message code="common.logisticsOperator" /></label>
							<input id="orderModalLogisticsOperatorInput" name="orderModalLogisticsOperator" class="form-control" disabled>
						</div>
					</div>

					<div class="row">
						<div class="col-md-12 form-group">
							<label for="orderModalCommentTextarea"><spring:message code="provisioningRequest.comment.label" /></label>
							<textarea id="orderModalCommentTextarea" name="orderModalComment" class="form-control my-textarea" rows="3" disabled></textarea>
						</div>
					</div>

					<br>

					<div>
						<table id="orderModalProductTable" class="table table-striped my-table">
							<thead>
								<tr>
									<th><spring:message code="common.product" /></th>
									<th><spring:message code="common.amount" /></th>
									<th><spring:message code="common.serialNumber"/></th>
	            					<th><spring:message code="common.batch"/></th>
	            					<th><spring:message code="common.expirationDate"/></th>
								</tr>
							</thead>
							<tbody id="orderModalProductTableBody">
							</tbody>
						</table>
					</div>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">
						<spring:message code="common.close" />
					</button>
				</div>
			</div>
		</div>
	</div>
</div>