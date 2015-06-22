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
					<div class="container-fluid">
						<div class="row">
							<div class="col-md-8 form-group"></div>
						</div>

						<div class="row">
							<div class="col-md-4 form-group">
								<label for="supplyingModalAgreementInput"><spring:message code="common.agreement" /></label> <input id="supplyingModalAgreementInput" name="supplyingModalAgreement" class="form-control" disabled>
							</div>
							<div class="col-md-4 form-group">
								<label for="supplyingModalClientInput"><spring:message code="common.client" /></label> <input id="supplyingModalClientInput" name="supplyingModalClientInput" class="form-control" disabled>
							</div>
							<div class="col-md-4 form-group">
								<label for="supplyingModalAffiliateInput"><spring:message code="common.affiliate" /></label> <input id="supplyingModalAffiliateInput" name="supplyingModalAffiliate" type="text" class="form-control" disabled />
							</div>

						</div>
						<br>
						<div>
							<table id="productTable" class="table table-condensed table-hover table-striped">
								<thead>
									<tr>
										<th data-identifier="true" data-column-id="code" data-type="numeric"><spring:message code="common.code" /></th>
										<th data-column-id="description"><spring:message code="common.product" /></th>
										<th data-column-id="amount" data-type="numeric"><spring:message code="common.amount" /></th>
										<th data-column-id="command" data-sortable="false"><spring:message code="common.option" /></th>
									</tr>
								</thead>
								<tbody id="productTableBody">
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
</div>

<div class="modal fade" data-backdrop="static" data-keyboard="false" id="batchExpirationDatesModal">
	<div class="modal-dialog" style="width: 900px">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
				<h4 class="modal-title">
					<spring:message code="input.modal.batchExpirationDates.title" />
				</h4>
			</div>
			<div class="modal-body">
				<div class="container-fluid">
					<div class="row">
						<div class="col-md-12 form-group">
							<h4 id="batchExpirationDateProductDescription" style="color: blue; font-weight: bold"></h4>
						</div>
					</div>
					<br>
					<table id="batchExpirationDatesTable" class="table table-condensed table-hover table-striped">
						<thead>
							<tr>
								<th data-identifier="true" data-column-id="id" data-type="numeric" data-visible="false"><spring:message code="common.serialNumber" /></th>
								<th data-column-id="amount" data-type="numeric"><spring:message code="common.amount" /></th>
								<th data-column-id="batch"><spring:message code="common.batch" /></th>
								<th data-column-id="expirationDate"><spring:message code="common.expirationDate" /></th>
							</tr>
						</thead>
						<tbody>
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

<div class="modal fade" data-backdrop="static" data-keyboard="false" id="serialsModal">
	<div class="modal-dialog" style="width: 900px">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
				<h4 class="modal-title">
					<spring:message code="input.modal.serials.title" />
				</h4>
			</div>
			<div class="modal-body">
				<div class="container-fluid">
					<div class="row">
						<div class="col-md-12 form-group">
							<h4 id="serializedProductDescription" style="color: blue; font-weight: bold"></h4>
						</div>
					</div>
					<br>
					<table id="serialsTable" class="table table-condensed table-hover table-striped">
						<thead>
							<tr>
								<th data-identifier="true" data-column-id="serialNumber"><spring:message code="common.serialNumber" /></th>
								<th data-column-id="batch"><spring:message code="common.batch" /></th>
								<th data-column-id="expirationDate"><spring:message code="common.expirationDate" /></th>
							</tr>
						</thead>
						<tbody>
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