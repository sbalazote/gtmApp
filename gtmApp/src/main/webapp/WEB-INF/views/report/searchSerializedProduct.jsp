<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<script type="text/javascript" src="js/form/report/searchSerializedProduct.js"></script>
<script type="text/javascript" src="js/form/modals.js"></script>
<script type="text/javascript">
	$(document).ready(function() {
		new SearchSerializedProduct();
	});
</script>

<jsp:include page="../modals/modals.jsp" />
<jsp:include page="../modals/inputModal.jsp" />
<jsp:include page="../modals/outputModal.jsp" />
<jsp:include page="../modals/deliveryNoteModal.jsp" />
<jsp:include page="../modals/orderModal.jsp" />
<jsp:include page="../modals/supplyingModal.jsp" />

<form id="searchSerializedProductForm" action="" onsubmit="return false;">
	<div class="row">
		<div class="col-md-9 col-lg-9 form-group">
			<h3><spring:message code="search.title.serializedProduct"/></h3>
		</div>
	</div>
	<div>
        <div class="row">
            <div class="col-md-12 col-lg-12 form-group">
                <label for="serialParserSearch"><spring:message code="search.product.reader"/></label>
                <input type="text" class="form-control" name="serialParserSearch" id="serialParserSearch" placeholder='<spring:message code="search.product.placeholder"/>' autocomplete="off">
            </div>
        </div>
		<div class="row">
			<div class="col-md-2 col-lg-2 col-md-offset-8 col-lg-offset-8">
				<button class="btn btn-success btn-block" id="searchSerialButton">
					<span class="glyphicon glyphicon-search"></span>
					<spring:message code="common.search" />
				</button>
			</div>
			<div class="col-md-2 col-lg-2">
				<button class="btn btn-info btn-block" id="cleanSerialButton">
					<span class="glyphicon glyphicon-trash"></span>
					<spring:message code="common.clean" />
				</button>
			</div>
		</div>
        <div class="row">
			<div class="col-md-12 col-lg-12 form-group">
				<label for="productInput"><spring:message code="common.product"/></label>
				<input id="productInput" name="productInput" type="search" placeholder='<spring:message code="search.product.description"/>' class="form-control" name="product" autosave="" results="5" incremental="incremental" />
			</div>
		</div>
		<div class="row">
			<div class="col-md-8 col-lg-8 form-group">
				<label for="serialNumberSearch"><spring:message code="common.serialNumber"/></label>
				<input type="text" class="form-control" name="serialNumberSearch" id="serialNumberSearch" autocomplete="off">
			</div>
			<div class="col-md-2 col-lg-2 form-margin">
				<button class="btn btn-success btn-block" id="searchButton">
				<span class="glyphicon glyphicon-search"></span>
				<spring:message code="common.search" />
				</button>
			</div>
			<div class="col-md-2 col-lg-2 form-margin">
				<button class="btn btn-info btn-block" id="cleanButton">
				<span class="glyphicon glyphicon-trash"></span>
				<spring:message code="common.clean" />
				</button>
			</div>
		</div>
		<br>
		<div class="row">
			<div class="col-md-12 col-lg-12 form-group">
				<label><spring:message code="common.product"/>:&nbsp;&nbsp;</label>
				<span style="font-style:italic;color:darkviolet"><label id="serializedLastProduct"></label></span>
			</div>
		</div>
		<div class="row">
			<div class="col-md-3 col-lg-3 form-group">
				<label><spring:message	code="common.gtin"/>:&nbsp;&nbsp;</label>
				<span style="font-style:italic;color:darkorange"><label id="serializedLastGtin"></label></span>
			</div>
			<div class="col-md-3 col-lg-3 form-group">
				<label><spring:message	code="common.serialNumber"/>:&nbsp;&nbsp;</label>
				<span style="font-style:italic;color:#1D00AF"><label id="serializedLastSerialNumber"></label></span>
			</div>
			<div class="col-md-3 col-lg-3 form-group">
				<label><spring:message code="common.batch"/>:&nbsp;&nbsp;</label>
				<span style="font-style:italic;color:forestgreen"><label id="serializedLastBatch"></label></span>
			</div>
			<div class="col-md-3 col-lg-3 form-group">
				<label><spring:message code="common.expirationDateAbb"/>:&nbsp;&nbsp;</label>
				<span style="font-style:italic;color:brown"><label id="serializedLastExpirationDate"></label></span>
			</div>
		</div>
	</div>
	<br>
	<div id="divMovements" style="display:none;">
		<h4 style="color:blue"><spring:message code="common.movements"/></h4>
		<table class="table table-condensed table-hover table-striped" id="movementsTable">
			<thead>
		        <tr>
		        	<th data-column-id="id" data-type="numeric"><spring:message code="common.id"/></th>
					<th data-column-id="action"><spring:message code="common.operation"/></th>
					<th data-column-id="cancelled"><spring:message code="common.cancelled"/></th>
		            <th data-column-id="user"><spring:message code="common.user"/></th>
		            <th data-column-id="date"><spring:message code="common.date"/></th>
		            <th data-column-id="view"><spring:message code="common.action"/></th>
		        </tr>
	   	 	</thead>
	   	 	<tbody id="movementsTableBody">
			</tbody>
		</table>
	</div>
</form>
