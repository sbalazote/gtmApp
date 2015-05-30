<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<div class="modal fade" data-backdrop="static" id="supplyingModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog" style="width: 1000px">
		<div class="modal-content">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
					<h3 class="modal-title">
						<spring:message code="deliveryNote.supplyingModal.label" />
					</h3>
				</div>

				<div class="modal-body">
					<div class="row">
						<div class="col-md-8 form-group"></div>
					</div>

					<div class="row">
						<div class="col-md-4 form-group">
							<label for="supplyingModalAgreementInput"><spring:message code="common.agreement" /></label>
							<input id="supplyingModalAgreementInput" name="supplyingModalAgreement" class="form-control" disabled>
						</div>
						<div class="col-md-4 form-group">
							<label for="supplyingModalClientInput"><spring:message code="common.client" /></label>
							<input id="supplyingModalClientInput" name="supplyingModalClientInput" class="form-control" disabled>
						</div>
						<div class="col-md-4 form-group">
							<label for="supplyingModalAffiliateInput"><spring:message code="common.affiliate" /></label>
							<input id="supplyingModalAffiliateInput" name="supplyingModalAffiliate" type="text" class="form-control" disabled />
						</div>

					</div>
					<br>
					<div>
						<table id="supplyingModalProductTable" class="table table-striped my-table">
							<thead>
								<tr>
									<th><spring:message code="common.product" /></th>
									<th><spring:message code="common.amount" /></th>
									<th><spring:message code="common.serialNumber"/></th>
	            					<th><spring:message code="common.batch"/></th>
	            					<th><spring:message code="common.expirationDate"/></th>
								</tr>
							</thead>
							<tbody id="supplyingModalProductTableBody">
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