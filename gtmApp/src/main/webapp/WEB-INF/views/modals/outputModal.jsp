<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<form id="outputModalForm" action="" onsubmit="return false;">
	<div class="modal fade" data-backdrop="static" id="outputModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog" style="width: 70%">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
					<div class="row">
						<div class="col-md-4 col-lg-4">
							<h3>
								<spring:message code="output.label" />
							</h3>
						</div>
						<div class="col-md-5 col-lg-3">
							<h3 id="outputId"></h3>
						</div>
						<div class="col-md-2 col-lg-2"  id="outputCancelled">
							<h3><span class="label label-danger">ANULADO</span></h3>
						</div>
					</div>
					<div id="outputModalANMATCode">
						<div class="row">
							<div class="col-md-5 col-lg-4">
								<h4 id="outputModalTransactionCodeDescription">
									<spring:message code="common.transactionCodeDescription" />
								</h4>
							</div>
							<div class="col-md-2 col-lg-2">
								<h4 id="outputModalTransactionCode" style="color: blue; font-weight: bold"></h4>
							</div>
						</div>
					</div>
                </div>
				<div class="modal-body">
					<div class="container-fluid">
						<div class="row">
							<div class="col-md-4 col-lg-4 form-group">
								<label for="dateModalOutput"><spring:message code="common.date" /></label> <input id="dateModalOutput" name="dateModalOutput" class="form-control" disabled>
							</div>
							<div class="col-md-4 col-lg-4 form-group">
								<label for="conceptModalOutput"><spring:message code="common.concept" /></label> <input id="conceptModalOutput" name="conceptModalOutput" class="form-control" disabled>
							</div>
							<div class="col-md-4 col-lg-4 form-group">
								<label for="clientOrProviderModalOutput"><spring:message code="common.clientOrProvider" /></label> <input id="clientOrProviderModalOutput" name="clientOrProviderModalOutput" class="form-control" disabled>
							</div>
						</div>

						<div class="row">
							<div class="col-md-4 col-lg-4 form-group">
								<label for="agreementModalOutput"><spring:message code="common.agreement" /></label> <input id="agreementModalOutput" name="agreementModalOutput" class="form-control" disabled>
							</div>
						</div>

						<div class="row">
							<div class="col-md-12 col-lg-12 form-group">
								<br>
								<h4 id="outputDeliveriesNotesLabel" style="display: none"><label><spring:message code="common.deliveryNotes"/></label></h4>
								<h3 id="outputDeliveriesNotesNumbers"></h3>
							</div>
						</div>

						<div>
							<table id="outputModalProductTable" class="table table-condensed table-hover table-striped">
								<thead>
									<tr>
										<th data-identifier="true" data-column-id="code" data-type="numeric"><spring:message code="common.code" /></th>
										<th data-column-id="description" data-header-css-class="descriptionColumn"><spring:message code="common.product" /></th>
										<th data-column-id="amount" data-type="numeric"><spring:message code="common.amount" /></th>
										<th data-column-id="command" data-sortable="false"><spring:message code="common.option" /></th>
									</tr>
								</thead>
								<tbody id="outputModalProductTableBody">
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