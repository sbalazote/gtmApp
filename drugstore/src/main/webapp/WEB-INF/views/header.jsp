<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<script type="text/javascript">
	$(document).ready(function() {
		$("#currentTime").clock({
			"langSet" : "es",
			"format" : "24",
			"calendar" : "false"
		});
	});
</script>

<div class="row">
	<div class="col-md-11 text-center">
		<h3><spring:message code="administration.drugManagement"/> <spring:message code="administration.drugManagement.version"/></h3>
    </div>
  	<div class="col-md-1">
		<div id="currentTime"></div>
    </div>
</div>