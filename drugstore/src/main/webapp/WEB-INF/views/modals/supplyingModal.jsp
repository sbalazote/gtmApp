<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<form id="supplyingModalForm" action="" onsubmit="return false;">
	<div class="modal fade" data-backdrop="static" id="supplyingModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog" style="width: 1100px">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
					<div class="row">
						<div class="col-md-4">
							<h3>
								<spring:message code="deliveryNote.supplyingModal.label" />
							</h3>
						</div>
						<div class="col-md-3">
							<h3 id="supplyingId"></h3>
						</div>
						<div class="col-md-2"  id="supplyingCancelled">
							<h3><span class="label label-danger">ANULADO</span></h3>
						</div>
					</div>
					<div id="supplyingModalANMATCode">
						<div class="row">
							<div class="col-md-4">
								<h4 id="supplyingModalTransactionCodeDescription">
									<spring:message code="common.transactionCodeDescription" />
								</h4>
							</div>
							<div class="col-md-2">
								<h4 id="supplyingModalTransactionCode" style="color: darkred; font-weight: bold"></h4>
							</div>
						</div>
					</div>
					<br>
					<div class="row">
						<div class="col-md-12  text-center">
							<h3 id="supplyingDeliveriesNotesNumbers"></h3>
						</div>
					</div>
				</div>

				<div class="modal-body">
					<div class="container-fluid">
						<div class="row">
							<div class="col-md-4 form-group">
								<label for="supplyingModalAgreementInput"><spring:message code="common.agreement" /></label> <input id="supplyingModalAgreementInput" name="supplyingModalAgreement" class="form-control" disabled>
							</div>
							<div class="col-md-4 form-group">
								<label for="supplyingModalClientInput"><spring:message code="common.client" /></label> <input id="supplyingModalClientInput" name="supplyingModalClient" class="form-control" disabled>
							</div>
							<div class="col-md-4 form-group">
								<label for="supplyingModalAffiliateInput"><spring:message code="common.affiliate" /></label> <input id="supplyingModalAffiliateInput" name="supplyingModalAffiliate" type="text" class="form-control" disabled />
							</div>
						</div>
						<br>
						<div>
							<table id="supplyingModalProductTable" class="table table-condensed table-hover table-striped">
								<thead>
								<tr>
									<th data-identifier="true" data-column-id="code" data-type="numeric"><spring:message code="common.code" /></th>
									<th data-column-id="description" data-header-css-class="descriptionColumn"><spring:message code="common.product" /></th>
									<th data-column-id="amount" data-type="numeric"><spring:message code="common.amount" /></th>
									<th data-column-id="command" data-sortable="false"><spring:message code="common.option" /></th>
								</tr>
								</thead>
								<tbody id="supplyingModalProductTableBody">
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
</form>