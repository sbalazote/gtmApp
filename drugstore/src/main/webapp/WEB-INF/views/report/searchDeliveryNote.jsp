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
		<div class="col-md-9 form-group">
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
						<div class="col-md-8 form-group">
							<label for="deliveryNoteNumberOutputSearch"><spring:message code="common.deliveryNote"/></label>
							<input type="text" class="form-control" name="deliveryNoteNumberOutputSearch" id="deliveryNoteNumberOutputSearch" >
						</div>
						<div class="col-md-4 form-group">
							<label for="deliveryLocationSearch"><spring:message code="common.deliveryLocation"/></label>
							<select id="deliveryLocationSearch" name="deliveryLocationSearch" class="form-control chosen-select" data-placeholder="<spring:message code='common.select.option'/>">
								<option value=""></option>
								<c:forEach items="${deliveryLocations}" var="deliveryLocation">
									<option value="${deliveryLocation.id}"><c:out value="${deliveryLocation.code}"></c:out> - <c:out value="${deliveryLocation.name}"></c:out></option>
								</c:forEach>
							</select>
						</div>
					</div>
					<div class="row">
						<div class="col-md-4 form-group">
							<label for="providerSearch"><spring:message code="common.provider"/></label>
							<select id="providerSearch" name="providerSearch" class="form-control chosen-select" data-placeholder="<spring:message code='common.select.option'/>">
								<option value=""></option>
								<c:forEach items="${providers}" var="provider">
									<option value="${provider.id}"><c:out value="${provider.code}"></c:out> - <c:out value="${provider.name}"></c:out></option>
								</c:forEach>
							</select>
						</div>
						<div class="col-md-4 form-group">
							<label for="agreementSearch"><spring:message code="common.agreement"/></label>
							<select id="agreementSearch" name="agreementSearch" class="form-control chosen-select" data-placeholder="<spring:message code='common.select.option'/>">
								<option value=""></option>
								<c:forEach items="${agreements}" var="agreement">
									<option value="${agreement.id}"><c:out value="${agreement.code}"></c:out> - <c:out value="${agreement.description}"></c:out></option>
								</c:forEach>
							</select>
						</div>
						<div class="col-md-2 form-margin">
							<button class="btn btn-success btn-block" type="submit" id="searchOutputButton">
							<span class="glyphicon glyphicon-search"></span>
							<spring:message code="common.search" />
							</button>
						</div>
						<div class="col-md-2 form-margin">
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
						<div class="col-md-2 form-group">
							<label for="deliveryNoteNumberSupplyingSearch"><spring:message code="common.deliveryNote"/></label>
							<input type="text" class="form-control" name="deliveryNoteNumberSupplyingSearch" id="deliveryNoteNumberSupplyingSearch" >
						</div>
                        <div class="col-md-3 form-group">
                            <label for="clientSearch"><spring:message code="common.client" /></label>
                            <select id="clientSearch" name="client" style="width:100% !important;" class="form-control" data-placeholder="<spring:message code='common.select.option'/>">
                                <option value=""></option>
                            </select>
                        </div>
                        <div class="col-md-3 form-group">
                            <label for="agreementSupplyingSearch"><spring:message code="common.agreement"/></label>
                            <select id="agreementSupplyingSearch" name="agreement" class="form-control" data-placeholder="<spring:message code='common.select.option'/>">
                                <option value=""></option>
                            </select>
                        </div>
                        <div class="col-md-2 form-margin">
							<button class="btn btn-success btn-block" type="submit" id="searchSupplyingButton">
							<span class="glyphicon glyphicon-search"></span>
							<spring:message code="common.search" />
							</button>
						</div>
						<div class="col-md-2 form-margin">
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
                        <div class="col-md-12 form-group">
                            <label for="affiliate"><spring:message code="common.affiliate"/></label>
                            <input id="affiliate" type="search" placeholder='<spring:message code="common.affiliate"/>' class="form-control" name="affiliate" autosave="" results="5" incremental="incremental" />
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-8 form-group">
                            <label for="deliveryNoteNumberOrderSearch"><spring:message code="common.deliveryNote"/></label>
                            <input type="text" class="form-control" name="deliveryNoteNumberOrderSearch" id="deliveryNoteNumberOrderSearch" >
                        </div>
                        <div class="col-md-2 form-margin">
                            <button class="btn btn-success btn-block" type="submit" id="searchOrderButton">
                                <span class="glyphicon glyphicon-search"></span>
                                <spring:message code="common.search" />
                            </button>
                        </div>
                        <div class="col-md-2 form-margin">
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



