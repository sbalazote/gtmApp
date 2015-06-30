<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<script type="text/javascript" src="js/form/output/outputCancellation.js"></script>
<script type="text/javascript" src="js/form/modals.js"></script>
<script type="text/javascript">
	$(document).ready(function() {
		new OutputCancellation();
	});
</script>

<jsp:include page="../modals/modals.jsp" />
<jsp:include page="../modals/outputModal.jsp" />

<form action="" onsubmit="return false;">
	<div class="row">
		<div class="col-md-12 form-group">
			<h3><spring:message code="output.outputCancelation" /></h3>
		</div>
	</div>

	<div id="divTable">
		<table class="table table-condensed table-hover table-striped" id="outputTable">
			<thead>
				<tr>
					<th data-column-id="id" data-type="numeric" data-identifier="true"><spring:message code="output.outputId" /></th>
					<th data-column-id="agreement"><spring:message code="common.agreement" /></th>
					<th data-column-id="provider"><spring:message code="common.provider" /></th>
					<th data-column-id="concept"><spring:message code="common.concept" /></th>
					<th data-column-id="date"><spring:message code="common.date" /></th>
					<th data-column-id="action" data-formatter="action" data-sortable="false"><spring:message code="common.option" /></th>
				</tr>
			</thead>
			<tbody id="outputTableBody">
				<c:forEach items="${cancelleables}" var="output" varStatus="status">
				<tr>
					<td><c:out value="${output.id}"></c:out></td>
					<td><c:out value="${output.agreement.description}"></c:out></td>
					<td><c:out value="${output.provider.name}"></c:out></td>
					<td><c:out value="${output.concept.description}"></c:out></td>
					<fmt:formatDate value="${output.date}" var="parsedDate" pattern="dd-MM-yyyy" />
					<td><c:out value="${parsedDate}"></c:out></td>
					<td><spring:message code="common.view"/></td>
				</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>
	<br/>
		<div class="row">
			<div class="col-md-2 col-md-offset-10">
				<button class="btn btn-danger btn-block" type="submit" id="confirmButton">
					<span class="glyphicon glyphicon-ok"></span>
					<spring:message code="provisioningRequest.cancellation.button" />
				</button>
			</div>
		</div>
</form>
