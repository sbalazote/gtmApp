<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<script type="text/javascript">
	$(document).ready(function() {
		$("#mainTable").bootgrid({
			formatters: {
	        	"option": function(column, row) {
	        		return "<a type='button' class='btn btn-sm btn-default a-select' href='#'><span class='glyphicon glyphicon-check'></span> Seleccionar</a>";
				}
	    	}
		});
		$("#productTableBody").on("click", ".a-select", function(){
			var provisioningId = $(this).parent().parent().attr("data-row-id");
			$("#provisioningRequestId").val(provisioningId);
			$("#myForm").submit();
		});
	});
</script>

<div class="row">
	<div class="col-md-9 col-lg-9 form-group">
		<h3><spring:message code="administration.provisioningRequestUpdate"/></h3>
	</div>
</div>

<div>
	<table class="table table-condensed table-hover table-striped" id="mainTable">
		<thead>
	        <tr>
	            <th data-column-id="id" data-type="numeric" data-identifier="true"><spring:message code="provisioningRequest.provisioningRequestNumber"/></th>
	            <th data-column-id="client"><spring:message code="common.client"/></th>
	            <th data-column-id="agreement"><spring:message code="common.agreement"/></th>
				<th data-column-id="date"><spring:message code="common.date" /></th>
	          	<th data-column-id="option" data-formatter="option" data-sortable="false"><spring:message code="common.option"/></th>
	        </tr>
   	 	</thead>
   	 	<tbody id="productTableBody">
			<c:forEach items="${provisionings}" var="provisioning" varStatus="status">
			<tr>
				<td><c:out value="${provisioning.id}"></c:out></td>
				<td><c:out value="${provisioning.client.name}"></c:out></td>
				<td><c:out value="${provisioning.agreement.description}"></c:out></td>
				<td><fmt:formatDate pattern="dd/MM/yyyy" value="${provisioning.deliveryDate}"/></td>
				<td></td>
			</tr>
			</c:forEach>
		</tbody>
	</table>
</div>

<form id="myForm" action="updateProvisioningRequest.do" method="get">
	<input type="hidden" name="id" id="provisioningRequestId">
</form>
