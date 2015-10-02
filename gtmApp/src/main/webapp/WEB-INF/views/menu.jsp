<%@ page import="java.io.File" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<script type="text/javascript" src="js/form/changePassword.js"></script>
<script type="text/javascript">
    $(document).ready(function() {
        new ChangePassword();
    });
</script>

<nav class="navbar navbar-default" role="navigation">

	<%
		String relativePath = "";
		String realPath = request.getServletContext().getRealPath("/images/uploadedLogo.png");

		File file = new File(realPath);

		if(file.exists()) {
			relativePath = "./images/uploadedLogo.png";
		} else {
			relativePath = "./images/logo.png";
		}
	%>

	<!-- Brand and toggle get grouped for better mobile display -->
	<div class="navbar-header">
		<a class="navbar-brand" href="home.do"> <span><img alt="Brand" width="25" height="25" src=<%= relativePath %>></span></a>
	</div>

	<!-- Collect the nav links, forms, and other content for toggling -->
	<div class="collapse navbar-collapse navbar-ex1-collapse">
		<ul class="nav navbar-nav">
			<sec:authorize access="hasAnyRole('INPUT', 'INPUT_AUTHORIZATION', 'INPUT_CANCELLATION', 'SERIALIZED_RETURNS', 'OUTPUT', 'OUTPUT_CANCELLATION', 'AGREEMENT_TRANSFER', 'SUPPLYING', 'SUPPLYING_CANCELLATION')">
				<li class="activable dropdown"><a class="dropdown-toggle" data-toggle="dropdown" href="#"><spring:message code="common.stock" /> <b class="caret"></b></a>
					<ul class="dropdown-menu">

						<sec:authorize access="hasAnyRole('INPUT', 'INPUT_AUTHORIZATION', 'INPUT_CANCELLATION')">
							<li class="dropdown-submenu"><a href="#"><spring:message code="administration.input" /></a>
								<ul class="dropdown-menu">
									<sec:authorize access="hasRole('INPUT')">
										<li class="activable"><a href="input.do"><spring:message code="common.input" /></a></li>
										<li class="activable"><a href="pendingInputs.do"><spring:message code="common.pendings" /></a></li>
									</sec:authorize>
									<sec:authorize access="hasRole('INPUT_AUTHORIZATION')">
										<li class="activable"><a href="searchInputToUpdate.do"><spring:message code="common.authorizeSNT" /></a></li>
									</sec:authorize>
									<sec:authorize access="hasRole('INPUT_CANCELLATION')">
										<li><a href="inputCancellation.do"><spring:message code="administration.cancelled.button" /> </a></li>
									</sec:authorize>
								</ul></li>
						</sec:authorize>

						<sec:authorize access="hasRole('SERIALIZED_RETURNS')">
							<li class="activable"><a href="serializedReturns.do"><spring:message code="serializedReturns.label" /></a></li>
						</sec:authorize>

						<sec:authorize access="hasAnyRole('OUTPUT', 'OUTPUT_CANCELLATION')">
							<li class="dropdown-submenu"><a href="#"><spring:message code="administration.output" /></a>
								<ul class="dropdown-menu">
									<sec:authorize access="hasRole('OUTPUT')">
										<li class="activable"><a href="output.do"><spring:message code="common.output.label" /></a></li>
									</sec:authorize>
								</ul></li>
						</sec:authorize>

                        <sec:authorize access="hasRole('INPUT_AUTHORIZATION')">
                            <li><a href="informForcedInputs.do"><spring:message code="common.informForcedInputs" /></a></li>
                        </sec:authorize>

                        <sec:authorize access="hasRole('AGREEMENT_TRANSFER')">
							<li class="activable"><a href="agreementTransfer.do"><spring:message code="common.agreementTransfer" /></a></li>
						</sec:authorize>

					</ul></li>
			</sec:authorize>

			<sec:authorize access="hasRole('SUPPLYING')">
				<li class="activable"><a href="supplying.do"><spring:message code="common.supplying" /></a></li>
			</sec:authorize>

			<sec:authorize access="hasAnyRole('PROVISIONING_REQUEST', 'PROVISIONING_REQUEST_UPDATE','PROVISIONING_REQUEST_PRINT', 'PROVISIONING_REQUEST_AUTHORIZATION', 'ORDER_ASSEMBLY', 'PROVISIONING_REQUEST_CANCELLATION', 'ORDER_ASSEMBLY_CANCELLATION')">
				<li class="activable dropdown"><a class="dropdown-toggle" data-toggle="dropdown" href="#"><spring:message code="common.requests" /> <b class="caret"></b></a>
					<ul class="dropdown-menu">
						<sec:authorize access="hasRole('PROVISIONING_REQUEST')">
							<li><a href="provisioningRequest.do"><spring:message code="common.do" /></a></li>
						</sec:authorize>
						<sec:authorize access="hasRole('PROVISIONING_REQUEST_UPDATE')">
							<li><a href="searchProvisioningToUpdate.do"><spring:message code="common.update" /></a></li>
						</sec:authorize>
						<sec:authorize access="hasRole('PROVISIONING_REQUEST_PRINT')">
							<li><a href="pickingSheet.do"><spring:message code="printing.pickingSheets" /></a></li>
						</sec:authorize>
						<sec:authorize access="hasRole('PROVISIONING_REQUEST_AUTHORIZATION')">
							<li><a href="provisioningRequestAuthorization.do"><spring:message code="common.authorize" /></a></li>
						</sec:authorize>
						<sec:authorize access="hasRole('ORDER_ASSEMBLY')">
							<li class="activable"><a href="orderAssemblySelection.do"><spring:message code="common.orderAssembly" /></a></li>
						</sec:authorize>
						<sec:authorize access="hasRole('ORDER_ASSEMBLY')">
							<li class="activable"><a href="logisticOperatorAssignment.do"><spring:message code="common.logisticOperatorAssingment" /></a></li>
						</sec:authorize>
						<sec:authorize access="hasRole('PROVISIONING_REQUEST_CANCELLATION')">
							<li><a href="provisioningRequestCancellation.do"><spring:message code="provisioningRequest.cancellation.label" /> </a></li>
						</sec:authorize>
						<sec:authorize access="hasRole('ORDER_ASSEMBLY_CANCELLATION')">
							<li><a href="orderCancellation.do"><spring:message code="orderAssembly.orderCancelation" /> </a></li>
						</sec:authorize>
					</ul></li>
			</sec:authorize>

			<sec:authorize access="hasAnyRole('DELIVERY_NOTE_PRINT', 'DELIVERY_NOTE_CANCELLATION', 'PENDING_TRANSACTIONS')">
				<li class="activable dropdown"><a class="dropdown-toggle" data-toggle="dropdown" href="#"><spring:message code="printing.label" /> <b class="caret"></b></a>
					<ul class="dropdown-menu">
						<sec:authorize access="hasRole('DELIVERY_NOTE_PRINT')">
							<li><a href="deliveryNoteSheet.do"><spring:message code="printing.deliveryNotes" /></a></li>
						</sec:authorize>
						<sec:authorize access="hasRole('DELIVERY_NOTE_CANCELLATION')">
							<li><a href="deliveryNoteCancellation.do"><spring:message code="deliveryNote.cancellation.label" /> </a></li>
						</sec:authorize>
                        <sec:authorize access="hasRole('PENDING_TRANSACTIONS')">
                            <li class="activable"><a href="pendingTransactions.do"><spring:message code="common.pendingTransactions" /></a></li>
                        </sec:authorize>
					</ul></li>
			</sec:authorize>




			<li class="activable dropdown"><a class="dropdown-toggle" data-toggle="dropdown" href="#"><spring:message code="administration.lookUp" /> <b class="caret"></b></a>
				<ul class="dropdown-menu">
                    <sec:authorize access="hasAnyRole('INPUT', 'INPUT_AUTHORIZATION', 'INPUT_CANCELLATION')">
					    <li><a href="searchInput.do"><spring:message code="common.inputs" /></a></li>
                    </sec:authorize>
                    <sec:authorize access="hasAnyRole('OUTPUT', 'OUTPUT_CANCELLATION')">
					    <li><a href="searchOutput.do"><spring:message code="common.outputs" /></a></li>
                    </sec:authorize>
					<sec:authorize access="hasAnyRole('PROVISIONING_REQUEST', 'PROVISIONING_REQUEST_UPDATE','PROVISIONING_REQUEST_PRINT', 'PROVISIONING_REQUEST_AUTHORIZATION', 'PROVISIONING_REQUEST_CANCELLATION')">
						<li><a href="searchProvisioningRequest.do"><spring:message code="common.provisioningRequests" /></a></li>
					</sec:authorize>
					<sec:authorize access="hasAnyRole('SUPPLYING', 'SUPPLYING_CANCELLATION')">
						<li><a href="searchSupplying.do"><spring:message code="search.title.supplyings" /></a></li>
					</sec:authorize>
                    <sec:authorize access="hasAnyRole('DELIVERY_NOTE_PRINT', 'DELIVERY_NOTE_CANCELLATION')">
                        <li><a href="searchDeliveryNote.do"><spring:message code="common.deliveryNotes" /></a></li>
                    </sec:authorize>
                    <sec:authorize access="hasAnyRole('ENTITY_ADMINISTRATION', 'USER_ADMINISTRATION')">
                        <li><a href="searchAudit.do"><spring:message code="common.audits" /></a></li>
                    </sec:authorize>
                    <sec:authorize access="hasAnyRole('INPUT', 'INPUT_AUTHORIZATION', 'INPUT_CANCELLATION','OUTPUT', 'OUTPUT_CANCELLATION','PROVISIONING_REQUEST', 'PROVISIONING_REQUEST_UPDATE','PROVISIONING_REQUEST_PRINT', 'PROVISIONING_REQUEST_AUTHORIZATION', 'PROVISIONING_REQUEST_CANCELLATION', 'ORDER_ASSEMBLY', 'ORDER_ASSEMBLY_CANCELLATION')">
                        <li><a href="searchStock.do"><spring:message code="common.stocks" /></a></li>
                        <li><a href="searchSerializedProduct.do"><spring:message code="search.menu.serializedProduct" /></a></li>
                    	<li><a href="searchBatchExpirateDateProduct.do"><spring:message code="search.menu.batchExpirateDateProduct" /></a></li>
                    </sec:authorize>
				</ul></li>

			<sec:authorize access="hasAnyRole('ENTITY_ADMINISTRATION', 'USER_ADMINISTRATION')">
				<li class="activable dropdown"><a class="dropdown-toggle" data-toggle="dropdown" href="#"><spring:message code="administration.label" /> <b class="caret"></b></a>
					<ul class="dropdown-menu">
						<sec:authorize access="hasRole('ENTITY_ADMINISTRATION')">
							<li class="dropdown-submenu"><a href="#"><spring:message code="administration.entity" /></a>
								<ul class="dropdown-menu">
									<li><a href="affiliateAdministration.do"><spring:message code="common.affiliates" /></a></li>
									<li><a href="agentAdministration.do"><spring:message code="common.agents" /></a></li>
									<li><a href="clientAdministration.do"><spring:message code="common.clients" /></a></li>
									<li><a href="conceptAdministration.do"><spring:message code="common.concepts" /></a></li>
									<li><a href="agreementAdministration.do"><spring:message code="common.agreements" /></a></li>
									<li><a href="eventAdministration.do"><spring:message code="common.events" /></a></li>
									<li><a href="deliveryLocationAdministration.do"><spring:message code="common.deliveryLocations" /></a></li>
									<li><a href="logisticsOperatorAdministration.do"><spring:message code="common.logisticsOperators" /></a></li>
									<li><a href="productAdministration.do"><spring:message code="common.products" /></a></li>
									<li><a href="providerAdministration.do"><spring:message code="common.providers" /></a></li>
									<li><a href="deliveryNoteEnumeratorAdministration.do"><spring:message code="common.deliveryNoteEnumerators" /></a></li>
									<li><a href="providerSerializedFormatAdministration.do"><spring:message code="common.serializations" /></a></li>
								</ul></li>
						</sec:authorize>
						<sec:authorize access="hasRole('USER_ADMINISTRATION')">
							<li><a href="userAdministration.do"><spring:message code="administration.users" /></a></li>
							<li><a href="profileAdministration.do"><spring:message code="administration.profiles" /></a></li>
						</sec:authorize>
					</ul></li>
			</sec:authorize>

			<sec:authorize access="hasRole('ENTITY_ADMINISTRATION')">
				<li class="activable"><a href="updateProperty.do"><spring:message code="common.configuration" /></a></li>
			</sec:authorize>
		</ul>

		<ul class="nav navbar-nav navbar-right">
			<li class="dropdown">
				<a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false"><sec:authentication property="principal" /><span class="caret"></span></a>
				<ul class="dropdown-menu">
					<li id="changePassword"><a href="#"><spring:message code="administration.changePassword" /></a></li>
				</ul>
			</li>
			<a role="button" class="btn btn-primary btn-lg logout-button" href="j_spring_security_logout"> <span class="glyphicon glyphicon-log-out" aria-hidden="true"></span> Salir</a>
		</ul>
	</div>
</nav>

<form id="changePasswordForm" action="" onsubmit="return false;">
	<div class="modal fade" data-backdrop="static" id="changePasswordModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog" style="width:400px">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
					<h4 class="modal-title"><spring:message code="administration.changePassword.title"/></h4>
				</div>
				<div id="changePasswordModalAlertDiv"></div>
				<div class="modal-body">
					<div class="row">
                        <div class="col-md-12 col-lg-12 form-group">
						    <label for="actualPasswordInput"><spring:message code="administration.actualPassword"/></label>
						    <input type="password" class="form-control" name="actualPassword" id="actualPasswordInput"/>
                        </div>
					</div>
					<div class="row">
						<div class="col-md-12 col-lg-12 form-group">
							<label for="passwordToChangeInput"><spring:message code="administration.newPassword" /></label>
							<input type="password" class="form-control" id="passwordToChangeInput" name="passwordToChange">
						</div>
					</div>
					<div class="row">
						<div class="col-md-12 col-lg-12 form-group">
							<label for="passwordToChangeInputCheck"><spring:message code="common.repeatPassword" /></label>
							<input type="password" class="form-control" id="passwordToChangeInputCheck" name="passwordToChangeCheck">
						</div>
					</div>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal"><spring:message code="common.abort"/></button>
					<button type="button" class="btn btn-primary" id="confirmChangePassword"><spring:message code="common.accept"/></button>
				</div>
			</div>
		</div>
	</div>
</form>