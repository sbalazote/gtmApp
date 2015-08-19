<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<script type="text/javascript">
	$(document).ready(function() {
		$("#mainTable").bootgrid({
			formatters: {
	        	"option": function(column, row) {
	        		return "<a class=\"a-select\" href=\"#\">Seleccionar</a>";
	        	}
	    	}
		});
		$("#productTableBody").on("click", ".a-select", function(){
			var inputId = $(this).parent().parent().attr("data-row-id");
			$("#inputId").val(inputId);
			$("#myForm").submit();
		});
	});
</script>

<div class="row">
	<div class="col-md-9 col-lg-9 form-group">
		<h3><spring:message code="administration.inputsToAuthorize"/></h3>
	</div>
</div>

<div id="divTable">
	<table class="table table-condensed table-hover table-striped" id="mainTable">
		<thead>
	        <tr>
	          	<th data-column-id="id" data-type="numeric" data-identifier="true"><spring:message code="common.id"/></th>
	        	<th data-column-id="concept"><spring:message code="common.concept"/></th>
	        	<th data-column-id="agreement"><spring:message code="common.agreement"/></th>
	        	<th data-column-id="option" data-formatter="option" data-sortable="false"><spring:message code="common.option"/></th>
	        </tr>
   	 	</thead>
   	 	<tbody id="productTableBody">
			<c:forEach items="${inputs}" var="input" varStatus="status">
			<tr>
				<td><c:out value="${input.id}"></c:out></td>
				<td><c:out value="${input.concept.description}"></c:out></td>
				<td><c:out value="${input.agreement.description}"></c:out></td>
				<td></td>
			</tr>
			</c:forEach>
		</tbody>
	</table>
</div>

<form id="myForm" action="updateInput.do" method="get">
	<input type="hidden" name="id" id="inputId">
</form>
