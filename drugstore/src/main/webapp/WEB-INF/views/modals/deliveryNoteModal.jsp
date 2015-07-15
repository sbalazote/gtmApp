<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<form id="deliveryNoteModalForm" action="" onsubmit="return false;">
	<div class="modal fade" data-backdrop="static" id="deliveryNoteModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog" style="width: 1100px">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
					<div class="row">
						<div class="col-md-4">
							<h3><spring:message code="deliveryNote.title"/></h3>
						</div>
						<div class="col-md-4">
							<h3 id="deliveryNoteId"></h3>
						</div>
						<div class="col-md-2" id="deliveryNoteCancelled">
							<h3><span class="label label-danger">ANULADO</span></h3>
						</div>
					</div>
					<div id="deliveryNoteModalANMATCode">
						<div class="row">
							<div class="col-md-4" >
								<h4 id="deliveryNoteModalTransactionCodeDescription"><spring:message code="common.transactionCodeDescription"/></h4>
							</div>
							<div class="col-md-2">
								<h4 id="deliveryNoteModalTransactionCode" style="color:blue;font-weight:bold"></h4>
							</div>
						</div>
					</div>
				</div>
				<div class="modal-body">
					<div class="container-fluid">
					<div class="row">
						<div class="col-md-12 form-group">
							<h3 id="entityId"></h3>
							<br>
						</div>
					</div>

					<div class="row">
						<div class="col-md-2 form-group">
							<label for="dateDeliveryNoteModal"><spring:message code="common.date"/></label>
							<input id="dateDeliveryNoteModal" name="dateDeliveryNoteModal" class="form-control" disabled>
						</div>
						<div class="col-md-6 form-group">
							<label for="clientOrProviderDeliveryNoteModal"><spring:message code="common.clientOrProvider"/></label>
							<input id="clientOrProviderDeliveryNoteModal" name="clientOrProviderDeliveryNoteModal" class="form-control" disabled>
						</div>
						<div class="col-md-4 form-group">
							<label for="agreementDeliveryNoteModal"><spring:message code="common.agreement"/></label>
							<input id="agreementDeliveryNoteModal" name="agreementDeliveryNoteModal" class="form-control" disabled>
						</div>
					</div>
					
					<br>
					
					<div>
						<table id="deliveryNoteModalProductTable" class="table table-condensed table-hover table-striped">
							<thead>
						        <tr>
									<th data-identifier="true" data-column-id="code" data-type="numeric"><spring:message code="common.code" /></th>
									<th data-column-id="description" data-header-css-class="descriptionColumn"><spring:message code="common.product" /></th>
									<th data-column-id="amount" data-type="numeric"><spring:message code="common.amount" /></th>
									<th data-column-id="command" data-sortable="false"><spring:message code="common.option" /></th>
						        </tr>
					   	 	</thead>
					   	 	<tbody id="deliveryNoteModalProductTableBody">
							</tbody>
						</table>
					</div>
					</div>
					</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-default" data-dismiss="modal"><spring:message code="common.close"/></button>
				</div>
			</div>
		</div>
	</div>
</form>