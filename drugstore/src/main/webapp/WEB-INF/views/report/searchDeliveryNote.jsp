<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<script type="text/javascript" src="js/form/report/searchDeliveryNote.js"></script>
<script type="text/javascript" src="js/form/modals.js"></script>
<script type="text/javascript">
	$(document).ready(function() {
		new SearchDeliveryNote();
	});
</script>

<jsp:include page="../modals/modals.jsp" />
<jsp:include page="../modals/deliveryNoteModal.jsp" />

<form id="searchDeliveryNoteForm" action="" onsubmit="return false;">
	<div class="row">
		<div class="col-md-9 col-lg-9 form-group">
			<h3><spring:message code="search.title.deliveryNote"/></h3>
		</div>
	</div>
	<div class="bs-example bs-example-tabs">
		<ul id="myTab" class="nav nav-tabs">
			<li class="active"><a href="#deliveryNotesOutputs" data-toggle="tab"><spring:message code="common.outputs"/></a></li>
			<li><a href="#deliveryNotesOrders" data-toggle="tab"><spring:message code="common.orders"/></a></li>
            <li><a href="#deliveryNotesSupplyings" data-toggle="tab"><spring:message code="common.supplyings"/></a></li>
        </ul>
		<br>
		<div id="myTabContent" class="tab-content">
			<div class="tab-pane fade in active" id="deliveryNotesOutputs">
				<div>
					<div class="row">
						<div class="col-md-4 col-lg-4 form-group">
							<label for="outputDeliveryNoteNumberSearch"><spring:message code="common.deliveryNote"/></label>
							<div class="input-group">
								<input type="text" class="form-control" name="outputPOSDeliveryNoteNumberSearch" id="outputPOSDeliveryNoteNumberSearch" placeholder='<spring:message code="common.deliveryNote.POS"/>'>
								<span class="input-group-addon">-</span>
								<input type="text" class="form-control" name="outputDeliveryNoteNumberSearch" id="outputDeliveryNoteNumberSearch" placeholder='<spring:message code="common.deliveryNote.number"/>' >
							</div>
						</div>
						<div class="col-md-4 col-lg-4 form-group">
							<label for="outputDateFromSearch"><spring:message code="common.dateFrom"/></label>
							<div class="input-group">
								<input type="text" class="form-control" name="outputDateFromSearch" id="outputDateFromSearch">
								<span class="input-group-addon" id="outputDateFromButton" style="cursor:pointer;">
									<span class="glyphicon glyphicon-calendar"></span>
								</span>
							</div>
						</div>
						<div class="col-md-4 col-lg-4 form-group">
							<label for="outputDateToSearch"><spring:message code="common.dateTo"/></label>
							<div class="input-group">
								<input type="text" class="form-control" name="outputDateToSearch" id="outputDateToSearch">
								<span class="input-group-addon" id="outputDateToButton" style="cursor:pointer;">
									<span class="glyphicon glyphicon-calendar"></span>
								</span>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-md-4 col-lg-4 form-group">
							<label for="outputIdSearch"><spring:message code="common.outputNumber"/></label>
							<input type="text" class="form-control" name="outputIdSearch" id="outputIdSearch" >
						</div>
						<div class="col-md-4 col-lg-4 form-group">
							<label for="outputProviderSearch"><spring:message code="common.provider"/></label>
							<select id="outputProviderSearch" name="outputProviderSearch" class="form-control chosen-select" data-placeholder="<spring:message code='common.select.option'/>">
								<option value=""></option>
								<c:forEach items="${providers}" var="provider">
									<option value="${provider.id}"><c:out value="${provider.code}"></c:out> - <c:out value="${provider.name}"></c:out></option>
								</c:forEach>
							</select>
						</div>
						<div class="col-md-4 col-lg-4 form-group">
							<label for="outputConceptSearch"><spring:message code="common.concept"/></label>
							<select id="outputConceptSearch" name="outputConceptSearch" class="form-control chosen-select" data-placeholder="<spring:message code='common.select.option'/>">
								<option value=""></option>
								<c:forEach items="${concepts}" var="concept">
									<option value="${concept.id}"><c:out value="${concept.code}"></c:out> - <c:out value="${concept.description}"></c:out></option>
								</c:forEach>
							</select>
						</div>
					</div>
					<div class="row">
						<div class="col-md-4 col-lg-4 form-group">
							<label for="outputAgreementSearch"><spring:message code="common.agreement"/></label>
							<select id="outputAgreementSearch" name="outputAgreementSearch" class="form-control chosen-select" data-placeholder="<spring:message code='common.select.option'/>">
								<option value=""></option>
								<c:forEach items="${agreements}" var="agreement">
									<option value="${agreement.id}"><c:out value="${agreement.code}"></c:out> - <c:out value="${agreement.description}"></c:out></option>
								</c:forEach>
							</select>
						</div>
						<div class="col-md-4 col-lg-4 form-group">
							<label for="outputDeliveryLocationSearch"><spring:message code="common.deliveryLocation"/></label>
							<select id="outputDeliveryLocationSearch" name="outputDeliveryLocationSearch" class="form-control chosen-select" data-placeholder="<spring:message code='common.select.option'/>">
								<option value=""></option>
								<c:forEach items="${deliveryLocations}" var="deliveryLocation">
									<option value="${deliveryLocation.id}"><c:out value="${deliveryLocation.code}"></c:out> - <c:out value="${deliveryLocation.name}"></c:out></option>
								</c:forEach>
							</select>
						</div>
					</div>
					<div class="row">
						<div class="col-md-6 col-lg-8 form-group">
							<label for="outputProductInput"><spring:message code="common.product"/></label>
							<input id="outputProductInput" name="outputProductInput" type="search" placeholder='<spring:message code="search.product.description"/>' class="form-control" name="product" autosave="" results="5" incremental="incremental" />
						</div>
						<div class="col-md-3 col-lg-2 form-margin">
							<button class="btn btn-success btn-block" type="submit" id="searchOutputButton">
							<span class="glyphicon glyphicon-search"></span>
							<spring:message code="common.search" />
							</button>
						</div>
						<div class="col-md-3 col-lg-2 form-margin">
							<button class="btn btn-info btn-block" type="submit" id="cleanOutputButton">
							<span class="glyphicon glyphicon-trash"></span>
							<spring:message code="common.clean" />
							</button>
						</div>
					</div>
					<br>
					<div id="divOutputTable">
						<table class="table table-condensed table-hover table-striped" id="deliveryNoteTableOutput">
							<thead>
						        <tr>
									<th data-column-id="id" data-header-css-class="idColumn" data-type="numeric"><spring:message code="common.id"/></th>
						            <th data-column-id="number" data-header-css-class="descriptionColumn" data-type="numeric"><spring:message code="common.deliveryNote"/></th>
						            <th data-column-id="date"><spring:message code="common.date"/></th>
						          	<th data-column-id="action"><spring:message code="common.option"/></th>
						        </tr>
					   	 	</thead>
					   	 	<tbody id="deliveryNoteTableOutputBody">
							</tbody>
						</table>
					</div>
				</div>
			</div>
			<div class="tab-pane fade" id="deliveryNotesSupplyings">
				<div>
					<div class="row">
						<div class="col-md-4 col-lg-4 form-group">
							<label for="supplyingDeliveryNoteNumberSearch"><spring:message code="common.deliveryNote"/></label>
							<div class="input-group">
								<input type="text" class="form-control" name="supplyingPOSDeliveryNoteNumberSearch" id="supplyingPOSDeliveryNoteNumberSearch" placeholder='<spring:message code="common.deliveryNote.POS"/>'>
								<span class="input-group-addon">-</span>
								<input type="text" class="form-control" name="supplyingDeliveryNoteNumberSearch" id="supplyingDeliveryNoteNumberSearch" placeholder='<spring:message code="common.deliveryNote.number"/>' >
							</div>
						</div>
						<div class="col-md-4 col-lg-4 form-group">
							<label for="supplyingDateFromSearch"><spring:message code="common.dateFrom"/></label>
							<div class="input-group">
								<input type="text" class="form-control" name="supplyingDateFromSearch" id="supplyingDateFromSearch">
								<span class="input-group-addon" id="supplyingDateFromButton" style="cursor:pointer;">
									<span class="glyphicon glyphicon-calendar"></span>
								</span>
							</div>
						</div>
						<div class="col-md-4 col-lg-4 form-group">
							<label for="supplyingDateToSearch"><spring:message code="common.dateTo"/></label>
							<div class="input-group">
								<input type="text" class="form-control" name="supplyingDateToSearch" id="supplyingDateToSearch">
								<span class="input-group-addon" id="supplyingDateToButton" style="cursor:pointer;">
									<span class="glyphicon glyphicon-calendar"></span>
								</span>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-md-4 col-lg-4 form-group">
							<label for="supplyingIdSearch"><spring:message code="common.supplyingNumber"/></label>
							<input type="text" class="form-control" name="supplyingIdSearch" id="supplyingIdSearch" >
						</div>
                        <div class="col-md-4 col-lg-4 form-group">
                            <label for="supplyingClientSearch"><spring:message code="common.client" /></label>
                            <select id="supplyingClientSearch" name="supplyingClientSearch" class="form-control" data-placeholder="<spring:message code='common.select.option'/>">
								<option value=""></option>
								<c:forEach items="${clients}" var="client">
									<option value="${client.id}"><c:out value="${client.code}"></c:out> - <c:out value="${client.name}"></c:out></option>
								</c:forEach>
                            </select>
                        </div>
						<div class="col-md-4 col-lg-4 form-group">
							<label for="supplyingAffiliate"><spring:message code="common.affiliate"/></label>
							<input id="supplyingAffiliate" type="search" placeholder='<spring:message code="common.affiliate"/>' class="form-control" name="supplyingAffiliate" autosave="" results="5" incremental="incremental" />
						</div>
					</div>
					<div class="row">
						<div class="col-md-4 col-lg-4 form-group">
							<label for="supplyingAgreementSearch"><spring:message code="common.agreement"/></label>
							<select id="supplyingAgreementSearch" name="supplyingAgreementSearch" class="form-control" data-placeholder="<spring:message code='common.select.option'/>">
								<option value=""></option>
								<c:forEach items="${agreements}" var="agreement">
									<option value="${agreement.id}"><c:out value="${agreement.code}"></c:out> - <c:out value="${agreement.description}"></c:out></option>
								</c:forEach>
							</select>
						</div>
						<div class="col-md-8 col-lg-8 form-group">
							<label for="supplyingProductInput"><spring:message code="common.product"/></label>
							<input id="supplyingProductInput" name="supplyingProductInput" type="search" placeholder='<spring:message code="search.product.description"/>' class="form-control" name="product" autosave="" results="5" incremental="incremental" />
						</div>
					</div>
					<div class="row">
                        <div class="col-md-3 col-lg-2 col-md-offset-6 col-lg-offset-8">
							<button class="btn btn-success btn-block" type="submit" id="searchSupplyingButton">
							<span class="glyphicon glyphicon-search"></span>
							<spring:message code="common.search" />
							</button>
						</div>
						<div class="col-md-3 col-lg-2">
							<button class="btn btn-info btn-block" type="submit" id="cleanSupplyingButton">
							<span class="glyphicon glyphicon-trash"></span>
							<spring:message code="common.clean" />
							</button>
						</div>
                    </div>
                    <br>
                    <div id="divSupplyingTable">
                        <table class="table table-condensed table-hover table-striped" id="deliveryNoteTableSupplying">
                            <thead>
                                <tr>
									<th data-column-id="id" data-header-css-class="idColumn" data-type="numeric"><spring:message code="common.id"/></th>
                                    <th data-column-id="number" data-header-css-class="descriptionColumn" data-type="numeric"><spring:message code="common.deliveryNote"/></th>
                                    <th data-column-id="date"><spring:message code="common.date"/></th>
                                    <th data-column-id="action"><spring:message code="common.option"/></th>
                                </tr>
                            </thead>
                            <tbody id="deliveryNoteTableSupplyingBody">
                            </tbody>
                        </table>
                    </div>
				</div>
            </div>
            <div class="tab-pane fade" id="deliveryNotesOrders">
                <div>
                    <div class="row">
						<div class="col-md-4 col-lg-4 form-group">
							<label for="orderDeliveryNoteNumberSearch"><spring:message code="common.deliveryNote"/></label>
							<div class="input-group">
								<input type="text" class="form-control" name="orderPOSDeliveryNoteNumberSearch" id="orderPOSDeliveryNoteNumberSearch" placeholder='<spring:message code="common.deliveryNote.POS"/>'>
								<span class="input-group-addon">-</span>
								<input type="text" class="form-control" name="orderDeliveryNoteNumberSearch" id="orderDeliveryNoteNumberSearch" placeholder='<spring:message code="common.deliveryNote.number"/>' >
							</div>
						</div>
						<div class="col-md-4 col-lg-4 form-group">
							<label for="orderDateFromSearch"><spring:message code="common.dateFrom"/></label>
							<div class="input-group">
								<input type="text" class="form-control" name="orderDateFromSearch" id="orderDateFromSearch">
									<span class="input-group-addon" id="orderDateFromButton" style="cursor:pointer;">
										<span class="glyphicon glyphicon-calendar"></span>
									</span>
							</div>
						</div>
						<div class="col-md-4 col-lg-4 form-group">
							<label for="orderDateToSearch"><spring:message code="common.dateTo"/></label>
							<div class="input-group">
								<input type="text" class="form-control" name="orderDateToSearch" id="orderDateToSearch">
									<span class="input-group-addon" id="orderDateToButton" style="cursor:pointer;">
										<span class="glyphicon glyphicon-calendar"></span>
									</span>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-md-4 col-lg-4 form-group">
							<label for="orderIdSearch"><spring:message code="common.orderNumber"/></label>
							<input type="text" class="form-control" name="orderIdSearch" id="orderIdSearch" >
						</div>
						<div class="col-md-4 col-lg-4 form-group">
							<label for="orderClientSearch"><spring:message code="common.client" /></label>
							<select id="orderClientSearch" name="orderClientSearch" class="form-control" data-placeholder="<spring:message code='common.select.option'/>">
								<option value=""></option>
								<c:forEach items="${clients}" var="client">
									<option value="${client.id}"><c:out value="${client.code}"></c:out> - <c:out value="${client.name}"></c:out></option>
								</c:forEach>
							</select>
						</div>
						<div class="col-md-4 col-lg-4 form-group">
							<label for="orderAffiliate"><spring:message code="common.affiliate"/></label>
							<input id="orderAffiliate" type="search" placeholder='<spring:message code="common.affiliate"/>' class="form-control" name="orderAffiliate" autosave="" results="5" incremental="incremental" />
						</div>
					</div>
					<div class="row">
						<div class="col-md-4 col-lg-4 form-group">
							<label for="orderAgreementSearch"><spring:message code="common.agreement"/></label>
							<select id="orderAgreementSearch" name="orderAgreementSearch" class="form-control" data-placeholder="<spring:message code='common.select.option'/>">
								<option value=""></option>
								<c:forEach items="${agreements}" var="agreement">
									<option value="${agreement.id}"><c:out value="${agreement.code}"></c:out> - <c:out value="${agreement.description}"></c:out></option>
								</c:forEach>
							</select>
						</div>
						<div class="col-md-4 col-lg-4 form-group">
							<label for="orderDeliveryLocationSearch"><spring:message code="common.deliveryLocation"/></label>
							<select id="orderDeliveryLocationSearch" name="orderDeliveryLocationSearch" class="form-control" data-placeholder="<spring:message code='common.select.option'/>">
								<option value=""></option>
								<c:forEach items="${deliveryLocations}" var="deliveryLocation">
									<option value="${deliveryLocation.id}"><c:out value="${deliveryLocation.code}"></c:out> - <c:out value="${deliveryLocation.name}"></c:out></option>
								</c:forEach>
							</select>
						</div>
					</div>
					<div class="row">
						<div class="col-md-6 col-lg-8 form-group">
							<label for="orderProductInput"><spring:message code="common.product"/></label>
							<input id="orderProductInput" name="orderProductInput" type="search" placeholder='<spring:message code="search.product.description"/>' class="form-control" name="product" autosave="" results="5" incremental="incremental" />
						</div>
                        <div class="col-md-3 col-lg-2 form-margin">
                            <button class="btn btn-success btn-block" type="submit" id="searchOrderButton">
                                <span class="glyphicon glyphicon-search"></span>
                                <spring:message code="common.search" />
                            </button>
                        </div>
                        <div class="col-md-3 col-lg-2 form-margin">
                            <button class="btn btn-info btn-block" type="submit" id="cleanOrderButton">
                                <span class="glyphicon glyphicon-trash"></span>
                                <spring:message code="common.clean" />
                            </button>
                        </div>
                    </div>
                    <br>
                    <div id="divOrderTable">
                        <table class="table table-condensed table-hover table-striped" id="deliveryNoteTableOrder">
                            <thead>
                            <tr>
								<th data-column-id="id" data-header-css-class="idColumn" data-type="numeric"><spring:message code="common.id"/></th>
                                <th data-column-id="number" data-header-css-class="descriptionColumn" data-type="numeric"><spring:message code="common.deliveryNote"/></th>
                                <th data-column-id="date"><spring:message code="common.date"/></th>
                                <th data-column-id="action"><spring:message code="common.option"/></th>
                            </tr>
                            </thead>
                            <tbody id="deliveryNoteTableOrderBody">
                            </tbody>
                        </table>
                    </div>
                </div>
			</div>
		</div>
	</div>
</form>



