<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<script type="text/javascript" src="js/form/input/pendingInputs.js"></script>
<script type="text/javascript" src="js/form/modals.js"></script>
<div class="row">
	<div class="col-md-9 form-group">
		<h3><spring:message code="administration.pendingInputs"/></h3>
	</div>
</div>

<script type="text/javascript">
	$(document).ready(function() {
		new PendingInputs();
	});
</script>
<jsp:include page="../modals/inputModal.jsp" />

<div id="divTable">
	<table class="table table-condensed table-hover table-striped" id="inputTable">
		<thead>
	        <tr>
	        	<th data-column-id="input" data-identifier="true"><spring:message code="common.id"/></th>
	        	<th data-column-id="concept"><spring:message code="common.concept"/></th>
	        	<th data-column-id="agreement"><spring:message code="common.agreement"/></th>
	        	<th data-column-id="option" data-formatter="option" data-sortable="false"><spring:message code="common.option"/></th>
			</tr>
		</thead>
		<tbody id="inputTableBody">
			<c:forEach items="${inputs}" var="input">
				<tr>
					<td><c:out value="${input.id}"></c:out></td>
					<td><c:out value="${input.concept.description}"></c:out></td>
					<td><c:out value="${input.agreement.description}"></c:out></td>
					<td><spring:message code="common.view"/></td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</div>
