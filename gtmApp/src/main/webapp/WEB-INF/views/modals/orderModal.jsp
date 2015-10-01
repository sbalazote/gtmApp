<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<div class="modal fade" data-backdrop="static" id="orderModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog" style="width: 70%">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
				<div class="row">
					<div class="col-md-4 col-lg-4">
						<h3>
							<spring:message code="deliveryNote.orderAssemblyModal.label" />
						</h3>
					</div>
					<div class="col-md-5 col-lg-3">
						<h3 id="orderId"></h3>
					</div>
					<div class="col-md-2 col-lg-2"  id="orderCancelled">
						<h3><span class="label label-danger">ANULADO</span></h3>
					</div>
				</div>
			</div>
			<div class="modal-body">
				<div class="container-fluid">
					<div class="row">
						<div class="col-md-8 col-lg-8 form-group"></div>
					</div>

					<div class="row">
						<div class="col-md-4 col-lg-4 form-group">
							<label for="orderModalAgreementInput"><spring:message code="common.agreement" /></label>
							<input id="orderModalAgreementInput" name="orderModalAgreement" class="form-control" disabled>
						</div>
						<div class="col-md-4 col-lg-4 form-group">
							<label for="orderModalClientInput"><spring:message code="common.client" /></label>
							<input id="orderModalClientInput" name="orderModalClientInput" class="form-control" disabled>
						</div>
						<div class="col-md-4 col-lg-4 form-group">
							<label for="orderModalAffiliateInput"><spring:message code="common.affiliate" /></label>
							<input id="orderModalAffiliateInput" name="orderModalAffiliate" type="text" class="form-control" disabled />
						</div>

					</div>

					<div class="row">
						<div class="col-md-4 col-lg-4 form-group">
							<label for="orderModalDeliveryLocationInput"><spring:message code="common.deliveryLocation" /></label>
							<input id="orderModalDeliveryLocationInput" name="orderModalDeliveryLocation" class="form-control" disabled>
						</div>
						<div class="col-md-4 col-lg-4 form-group">
							<label for="orderModalDeliveryDateInput"><spring:message code="common.deliveryDate" /></label>
							<input id="orderModalDeliveryDateInput" name="orderModalDeliveryDate" type="text" class="form-control" disabled>
						</div>
						<div class="col-md-4 col-lg-4 form-group">
							<label for="orderModalLogisticsOperatorInput"><spring:message code="common.logisticsOperator" /></label>
							<input id="orderModalLogisticsOperatorInput" name="orderModalLogisticsOperator" class="form-control" disabled>
						</div>
					</div>

					<div class="row">
						<div class="col-md-12 col-lg-12 form-group">
							<label for="orderModalCommentTextarea"><spring:message code="provisioningRequest.comment.label" /></label>
							<textarea id="orderModalCommentTextarea" name="orderModalComment" class="form-control my-textarea" rows="3" disabled></textarea>
						</div>
					</div>

					<br>

					<div>
						<table id="orderModalProductTable" class="table table-condensed table-hover table-striped">
							<thead>
							<tr>
								<th data-identifier="true" data-column-id="code" data-type="numeric"><spring:message code="common.code" /></th>
								<th data-column-id="description" data-header-css-class="descriptionColumn"><spring:message code="common.product" /></th>
								<th data-column-id="amount" data-type="numeric"><spring:message code="common.amount" /></th>
								<th data-column-id="command" data-sortable="false"><spring:message code="common.option" /></th>
							</tr>
							</thead>
							<tbody id="orderModalProductTableBody">
							</tbody>
						</table>
					</div>
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