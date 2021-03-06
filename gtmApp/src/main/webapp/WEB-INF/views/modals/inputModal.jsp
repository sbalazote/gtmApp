<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<form id="inputModalForm" action="" onsubmit="return false;">
	<div class="modal fade" data-backdrop="static" id="inputModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog" style="width: 70%">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
					<div class="row">
						<div class="col-md-4 col-lg-4">
							<h3>
								<spring:message code="input.label" />
							</h3>
						</div>
						<div class="col-md-5 col-lg-3">
							<h3 id="inputId"></h3>
						</div>
						<div class="col-md-2 col-lg-2">
							<h3 id="cancelled" style="color: red; font-weight: bold"></h3>
						</div>
					</div>
					<div id="ANMATCode">
						<div class="row">
							<div class="col-md-4 col-lg-4">
								<h4 id="transactionCodeDescription">
									<spring:message code="common.transactionCodeDescription" />
								</h4>
							</div>
							<div class="col-md-2 col-lg-2">
								<h4 id="transactionCode" style="color: blue; font-weight: bold"></h4>
							</div>
							<div class="col-md-4 col-lg-4">
								<h4 id="selfSerializedTransactionCodeDescription">
									<spring:message code="common.selfSerializedTransactionCodeDescription" />
								</h4>
							</div>
							<div class="col-md-2 col-lg-2">
								<h4 id="selfSerializedTransactionCode" style="color: blue; font-weight: bold"></h4>
							</div>
						</div>
					</div>
				</div>

				<div class="modal-body">
					<div class="container-fluid">
						<div class="row">
							<div class="col-md-3 col-lg-3 form-group">
								<label for="dateModal"><spring:message code="common.date" /></label> <input id="dateModal" name="dateModal" class="form-control" disabled>
							</div>
							<div class="col-md-3 col-lg-3 form-group">
								<label for="conceptModal"><spring:message code="common.concept" /></label> <input id="conceptModal" name="conceptModal" class="form-control" disabled>
							</div>
							<div class="col-md-3 col-lg-3 form-group">
								<label for="agreementModal"><spring:message code="common.agreement" /></label> <input id="agreementModal" name="agreementModal" class="form-control" disabled>
							</div>
							<div class="col-md-3 col-lg-3 form-group">
								<label for="deliveryNoteNumberModal"><spring:message code="common.deliveryNote" /></label> <input type="text" class="form-control" name="deliveryNoteNumberModal" id="deliveryNoteNumberModal" disabled>
							</div>
						</div>

						<div class="row">
							<div class="col-md-6 col-lg-6 form-group">
								<label for="clientOrProviderModal"><spring:message code="common.clientOrProvider" /></label> <input id="clientOrProviderModal" name="clientOrProviderModal" class="form-control" disabled>
							</div>
							<div class="col-md-3 col-lg-3 form-group">
								<label for="logisticsOperatorModal"><spring:message code="common.logisticsOperator" /></label> <input id="logisticsOperatorModal" name="logisticsOperatorModal" class="form-control" disabled>
							</div>
							<div class="col-md-3 col-lg-3 form-group">
								<label for="purchaseOrderNumberModal"><spring:message code="common.purchaseOrder" /></label> <input type="text" class="form-control" name="purchaseOrderNumberModal" id="purchaseOrderNumberModal" disabled>
							</div>
						</div>

						<br>

						<div>
							<table id="inputModalProductTable" class="table table-condensed table-hover table-striped">
								<thead>
									<tr>
										<th data-identifier="true" data-column-id="code" data-type="numeric"><spring:message code="common.code" /></th>
										<th data-column-id="description" data-header-css-class="descriptionColumn"><spring:message code="common.product" /></th>
										<th data-column-id="amount" data-type="numeric"><spring:message code="common.amount" /></th>
										<th data-column-id="command" data-sortable="false"><spring:message code="common.option" /></th>
									</tr>
								</thead>
								<tbody id="inputModalProductTableBody">
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