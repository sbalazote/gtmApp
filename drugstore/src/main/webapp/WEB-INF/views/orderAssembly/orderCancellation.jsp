<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<script type="text/javascript" src="js/form/orderAssembly/orderCancellation.js"></script>
<script type="text/javascript" src="js/form/modals.js"></script>
<script type="text/javascript">
	$(document).ready(function() {
		new OrderCancellation();
	});
</script>

<jsp:include page="../modals/modals.jsp" />
<jsp:include page="../modals/orderModal.jsp" />

<form action="" onsubmit="return false;">
	<div class="row">
		<div class="col-md-12 col-lg-12 form-group">
			<h3><spring:message code="orderAssembly.orderCancelation"/></h3>
		</div>
	</div>
	
	<div id="divTable">
		<table class="table table-condensed table-hover table-striped" id="orderTable">
			<thead>
		        <tr>
		            <th data-column-id="id" data-type="numeric" data-identifier="true"><spring:message code="common.id"/></th>
		            <th data-column-id="client"><spring:message code="common.client"/></th>
		            <th data-column-id="agreement"><spring:message code="common.agreement"/></th>
		          	<th data-column-id="action" data-formatter="action" data-sortable="false"><spring:message code="common.action"/></th>
		        </tr>
	   	 	</thead>
	   	 	<tbody id="orderTableBody">
				<c:forEach items="${orders}" var="order" varStatus="status">
				<tr>
					<td><c:out value="${order.provisioningRequest.id}"></c:out></td>
					<td><c:out value="${order.provisioningRequest.client.name}"></c:out></td>
					<td><c:out value="${order.provisioningRequest.agreement.description}"></c:out></td>
					<td><spring:message code="common.view"/></td>
				</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>
	<br>
	<div class="row">
		<div class="col-md-3 col-md-offset-9 col-lg-2 col-lg-offset-10">
			<button class="btn btn-danger btn-block" type="submit" id="confirmButton">
				<span class="glyphicon glyphicon-ok"></span>
				<spring:message code="provisioningRequest.cancellation.button" />
			</button>
		</div>
	</div>
</form>

<form id="orderFormModal" action="" onsubmit="return false;">
	<div class="modal fade" data-backdrop="static" id="orderModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog" style="width: 70%">
			<div class="modal-content">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
						<div class="row">
							<div class="col-md-4 col-lg-4">
								<h3><spring:message code="orderAssembly.label"/></h3>
							</div>
							<div class="col-md-2 col-lg-2">
								<h3 id="orderId"></h3>
							</div>
							<div class="col-md-2 col-lg-2">
								<h3 id="cancelled" style="color:red;font-weight:bold"></h3>
							</div>
						</div>
					</div>
					
					<div class="modal-body">
					
						<div class="row">
							<div class="col-md-4 col-lg-4 form-group">
								<label for="agreementInput"><spring:message code="common.agreement" /></label>
								<input id="agreementInput" name="agreement" placeholder='<spring:message code="common.select.option"/>' class="form-control" disabled>
							</div>
							<div class="col-md-4 col-lg-4 form-group">
								<label for="clientInput"><spring:message code="common.client" /></label>
								<input id="clientInput" name="clientInput" class="form-control" disabled>
							</div>
							<div class="col-md-4 col-lg-4 form-group">
								<label for="affiliateInput"><spring:message code="common.affiliate" /></label>
								<input name="affiliate" type="text" class="form-control" disabled/>
							</div>

						</div>
					
						<div class="row">
							<div class="col-md-4 col-lg-4 form-group">
								<label for="deliveryLocationInput"><spring:message code="common.deliveryLocation" /></label>
								<input id="deliveryLocationInput" name="deliveryLocation" class="form-control" disabled>
							</div>
							<div class="col-md-4 col-lg-4 form-group">
								<label for="deliveryDateInput"><spring:message code="common.deliveryDate" /></label>
								<input name="deliveryDate" type="text" class="form-control" disabled>
							</div>
							<div class="col-md-4 col-lg-4 form-group">
								<label for="logisticsOperatorInput"><spring:message code="common.logisticsOperator" /></label>
								<input id="logisticsOperatorInput" name="logisticsOperator" class="form-control" disabled>
							</div>
						</div>
						
						<div class="row">
							<div class="col-md-12 col-lg-12 form-group">
								<label for="comment"><spring:message code="provisioningRequest.comment.label" /></label>
								<textarea name="comment" class="form-control my-textarea" name="comment" id="comment" rows="3"  disabled></textarea>
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
								<tbody id="productTableBody">
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