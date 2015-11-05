<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<form id="provisioningRequestModalForm" action="" onsubmit="return false;">
	<div class="modal fade" data-backdrop="static" id="provisioningRequestModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog" style="width: 70%">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
					<div class="row">
						<div class="col-md-4 col-lg-4">
							<h3>
								<spring:message code="provisioningRequest.label" />
							</h3>
						</div>
						<div class="col-md-5 col-lg-3">
							<h3 id="provisioningRequestId"></h3>
						</div>
						<div class="col-md-3 col-lg-3">
							<h3 id="provisioningRequestState" style="color: green; font-weight: bold"></h3>
						</div>
					</div>
				</div>
				<div class="modal-body">
					<div class="container-fluid">
						<div class="row">
							<div class="col-md-8 col-lg-8 form-group">

							</div>
						</div>
						<div class="row">
							<div class="col-md-4 col-lg-4 form-group">
								<label for="agreementProvisioningRequestModal"><spring:message code="common.agreement" /></label>
								<input id="agreementProvisioningRequestModal" name="agreementProvisioningRequestModal" placeholder='<spring:message code="common.select.option"/>' class="form-control" disabled>
							</div>
							<div class="col-md-4 col-lg-4 form-group">
								<label for="clientProvisioningRequestModal"><spring:message code="common.client" /></label>
								<input id="clientProvisioningRequestModal" name="clientProvisioningRequestModal" class="form-control" disabled>
							</div>
							<div class="col-md-4 col-lg-4 form-group">
								<label for="affiliateProvisioningRequestModal"><spring:message code="common.affiliate" /></label>
								<input id="affiliateProvisioningRequestModal" name="affiliateProvisioningRequestModal" type="text" class="form-control" disabled/>
							</div>
						</div>

						<div class="row">
							<div class="col-md-4 col-lg-4 form-group">
								<label for="deliveryLocationProvisioningRequestModal"><spring:message code="common.deliveryLocation" /></label>
								<input id="deliveryLocationProvisioningRequestModal" name="deliveryLocationProvisioningRequestModal" class="form-control" disabled>
							</div>
							<div class="col-md-4 col-lg-4 form-group">
								<label for="deliveryDateProvisioningRequestModal"><spring:message code="common.deliveryDate" /></label>
								<input id="deliveryDateProvisioningRequestModal" name="deliveryDateProvisioningRequestModal" type="text" class="form-control" disabled>
							</div>
							<div class="col-md-4 col-lg-4 form-group">
								<label for="logisticsOperatorProvisioningRequestModal"><spring:message code="common.logisticsOperator" /></label>
								<input id="logisticsOperatorProvisioningRequestModal" name="logisticsOperatorProvisioningRequestModal" class="form-control" disabled>
							</div>
						</div>

						<div class="row">
							<div class="col-md-12 col-lg-12 form-group">
								<label for="commentProvisioningRequestModal"><spring:message code="provisioningRequest.comment.label" /></label>
								<textarea  id="commentProvisioningRequestModal" name="commentProvisioningRequestModal" class="form-control my-textarea" rows="3"  disabled></textarea>
							</div>
						</div>

						<br>

						<div>
							<table id="provisioningRequestModalProductTable" class="table table-condensed table-hover table-striped">
								<thead>
								<tr>
									<th data-identifier="true" data-column-id="code" data-type="numeric"><spring:message code="common.code" /></th>
									<th data-column-id="description" data-header-css-class="descriptionColumn"><spring:message code="common.product" /></th>
									<th data-column-id="amount" data-type="numeric"><spring:message code="common.amount" /></th>
								</tr>
								</thead>
								<tbody id="provisioningRequestModalProductTableBody">
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
