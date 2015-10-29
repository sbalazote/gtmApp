<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<script type="text/javascript" src="js/form/report/searchBatchExpirateDateProduct.js"></script>
<script type="text/javascript" src="js/form/modals.js"></script>
<script type="text/javascript">
	$(document).ready(function() {
		new SearchBatchExpirateDateProduct();
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
			<h3><spring:message code="search.title.batchExpirateDateProduct"/></h3>
		</div>
	</div>
	<div>
		<div class="row">
			<div class="col-md-12 col-lg-12 form-group">
				<label for="productInput"><spring:message code="common.product"/></label>
				<input id="productInput" name="productInput" type="search" placeholder='<spring:message code="input.product.placeholder"/>' class="form-control" name="product" autosave="" results="5" incremental="incremental" />
			</div>
		</div>
		<div class="row">
			<div class="col-md-4 col-lg-4 form-group">
				<label for="batchSearch"><spring:message code="common.batch"/></label>
				<input type="text" class="form-control" name="batchSearch" id="batchSearch" >
			</div>
			<div class="col-md-4 col-lg-4 form-group">
				<label for="dateSearch"><spring:message code="common.expirationDate"/></label>
				<div class="input-group">
					<input type="text" class="form-control" name="dateSearch" id="dateSearch">
					<span class="input-group-addon" id="dateButton" style="cursor:pointer;">
						<span class="glyphicon glyphicon-calendar"></span>
					</span>
				</div>
			</div>
			<div class="col-md-2 col-lg-2 form-margin">
				<button class="btn btn-success btn-block" type="submit" id="searchButton">
				<span class="glyphicon glyphicon-search"></span>
				<spring:message code="common.search" />
				</button>
			</div>
			<div class="col-md-2 col-lg-2 form-margin">
				<button class="btn btn-info btn-block" type="submit" id="cleanButton">
				<span class="glyphicon glyphicon-trash"></span>
				<spring:message code="common.clean" />
				</button>
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
                <th data-column-id="user"><spring:message code="common.user"/></th>
                <th data-column-id="date" data-order="desc"><spring:message code="common.date"/></th>
                <th data-column-id="view"><spring:message code="common.action"/></th>
            </tr>
            </thead>
            <tbody id="movementsTableBody">
            </tbody>
        </table>
    </div>
</form>

