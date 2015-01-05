<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<script>
	$(document).ready(function() {
		setInterval(function() {
			var date = new Date();
			var fecha = date.toLocaleDateString();
			var hora = date.toLocaleTimeString();
			$("#headerDate").text(fecha).css({
				"position" : "absolute",
				"right" : "20px",
				"top" : "8px"
			});
			$("#headerHour").text(hora).css({
				"position" : "absolute",
				"right" : "20px",
				"top" : "47px"
			});
		}, 1000);
	});
</script>

<div class="my-page-header">
	<h1>
		<spring:message code="administration.drugManagement.system"/>
	</h1>
	<p id=headerDate></p>
	<p id=headerHour></p>
</div>