<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<form id="provisioningRequestModal" action="" onsubmit="return false;">
	<div class="modal fade" data-backdrop="static" id="provisioningModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog" style="width: 70%">
			<div class="modal-content">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
						<h3 class="modal-title"><spring:message code="provisioningRequest.label"/></h3>
					</div>
					
					<div class="modal-body">
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
								<textarea  id="commentProvisioningRequestModalTextArea" name="commentProvisioningRequestModalTextArea" class="form-control my-textarea" rows="3"  disabled></textarea>
							</div>
						</div>
						
						<br>
					
						<div>
							<table id="productTable" class="table table-striped my-table">
								<thead>
									<tr>
										<th><spring:message code="common.product" /></th>
										<th><spring:message code="common.amount" /></th>
									</tr>
								</thead>
								<tbody id="productTableBodyProvisioningRequest">
								</tbody>
							</table>
						</div>
					</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-default" data-dismiss="modal"><spring:message code="common.close"/></button>
					</div>
				</div>
			</div>
		</div>
	</div>
</form>
