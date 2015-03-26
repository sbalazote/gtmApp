<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<script type="text/javascript" src="js/form/configuration/updateProperty.js" /></script>
<script type="text/javascript">
	$(document).ready(function() {
		new UpdateProperty();
	});
</script>

<form id="updatePropertyForm" action="" onsubmit="return false;">

<div class="row">
	<div class="col-md-9 form-group">
		<h2><spring:message code="configuration.updateProperties"/></h2>
		<input type="hidden" class="form-control" id="idInput" value="${id}">
		<input type="hidden" class="form-control" id="lastTagInput" value="${lastTag}">
		<input type="hidden" class="form-control" id="lastDeliveryNoteNumberInput" value="${lastDeliveryNoteNumber}">
	</div>
</div>
<div class="panel-group" id="accordion">
	<div class="panel panel-primary">
		<div class="panel-heading">
			<h3 class="panel-title">
		          <a class="accordion-toggle" data-toggle="collapse" data-parent="#accordion" href="#panel1"><spring:message code="configuration.info"/></a>
			</h3>
		</div>
		<div id="panel1" class="panel-collapse">
			<div class="panel-body">
                <div class="row">
					<div class="col-md-4 form-group">
						<label for="codeInput"><spring:message code="configuration.code"/></label> 
						<input type="text" class="form-control" id="codeInput" name="code" value="${code}">
					</div>
					<div class="col-md-4 form-group">
						<label for="nameInput"><spring:message code="configuration.entity.name"/></label> 
						<input type="text" class="form-control" id="nameInput" name="name" value="${name}">
					</div>
					<div class="col-md-4 form-group">
						<label for="taxIdInput"><spring:message code="configuration.taxId"/></label> 
						<input type="text" class="form-control" id="taxIdInput" name="taxId" value="${taxId}">
					</div>
				</div>
				
				<div class="row">
					<div class="col-md-4 form-group">
						<label for="corporateNameInput"><spring:message code="configuration.corporateName"/></label> 
						<input type="text" class="form-control" id="corporateNameInput" name="corporateName" value="${corporateName}">
					</div>
					<div class="col-md-4 form-group">	
						<label for="provinceSelect"><spring:message code="configuration.province"/></label>
						<select class="form-control chosen-select" id="provinceSelect" name="province">
							<option value="${selectedProvince.id}"><c:out value="${selectedProvince.name}"></c:out></option>
							<c:forEach items="${provinces}" var="province" varStatus="status">
								<option value="${province.id}"><c:out value="${province.name}"></c:out></option>
							</c:forEach>
						</select>
					</div>
					<div class="col-md-4 form-group">	
						<label for="localityInput"><spring:message code="configuration.locality"/></label>
						<input type="text" class="form-control" id="localityInput" name="locality" value="${locality}">
					</div>
				</div>
				
				<div class="row">
					<div class="col-md-4 form-group">	
						<label for="addressInput"><spring:message code="configuration.address"/></label>
						<input type="text" class="form-control" id="addressInput" name="address" value="${address}">
					</div>
					<div class="col-md-4 form-group">	
						<label for="zipCodeInput"><spring:message code="configuration.zipCode"/></label>
						<input type="text" class="form-control" id="zipCodeInput" name="zipCode" value="${zipCode}">
					</div>
					<div class="col-md-4 form-group">	
						<label for="phoneInput"><spring:message code="configuration.phone"/></label>
						<input type="text" class="form-control" id="phoneInput" name="phone" value="${phone}">
					</div>
				</div>
				
				<div class="row">
					<div class="col-md-4 form-group">	
						<label for="mailInput"><spring:message code="configuration.mail"/></label>
						<input type="text" class="form-control" id="mailInput" name="mail" value="${mail}">
					</div>
					<div class="col-md-4 form-group">	
						<label for="glnInput"><spring:message code="configuration.gln"/></label>
						<input type="text" class="form-control" id="glnInput" name="gln" value="${gln}">
					</div>
					<div class="col-md-4 form-group">	
						<label for="agentSelect"><spring:message code="configuration.agent"/></label>
						<select class="form-control chosen-select" id="agentSelect" name="agent">
							<option value="${selectedAgent.id}"><c:out value="${selectedAgent.description}"></c:out></option>
							<c:forEach items="${agents}" var="agent" varStatus="status">
								<option value="${agent.id}"><c:out value="${agent.description}"></c:out></option>
							</c:forEach>
						</select>
					</div>
				</div>
			</div>
		</div>
    </div>
	<div class="panel panel-success">
		<div class="panel-heading">
		<h3 class="panel-title">
			<a class="accordion-toggle" data-toggle="collapse" data-parent="#accordion" href="#panel2"><spring:message code="configuration.settings"/></a>
		</h3> 
		</div>
		<div id="panel2" class="panel-collapse">
			<div class="panel-body">
				<div class="row">
					<div class="col-md-6 form-group">	
						<label for="selfSerializedTagFilepathInput"><spring:message code="configuration.selfSerializedTagFilepath"/></label>
						<input type="text" class="form-control" id="selfSerializedTagFilepathInput" name="selfSerializedTagFilepath" value="${selfSerializedTagFilepath}">
					</div>
				</div>
            </div>
		</div>
    </div>
	<div class="panel panel-danger">
		<div class="panel-heading">
		    <h3 class="panel-title">
		    	<a class="accordion-toggle" data-toggle="collapse" data-parent="#accordion" href="#panel3"><spring:message code="configuration.ANMATInformation"/></a>
		    </h3>
		</div>
		<div id="panel3" class="panel-collapse">
			<div class="panel-body">
                <div class="row">
					<div class="col-md-4 form-group">
						<label for="ANMATNameInput"><spring:message code="configuration.ANMATName"/></label>
						<input type="text" class="form-control" id="ANMATNameInput" name="ANMATName" value="${ANMATName}">
					</div>
					<div class="col-md-4 form-group">
						<label for="conceptSelect"><spring:message code="configuration.deliveryNoteConcept"/></label>
						<select id="conceptSelect" name="concept" class="form-control chosen-select" data-placeholder="<spring:message code='common.select.option'/>">
							<option value=""></option>
							<c:forEach items="${deliveryNoteconcepts}" var="concept">
								<option value="${concept.id}"  ${selectedConcept == concept.id ? 'selected' : ''}><c:out value="${concept.code}"></c:out> - <c:out value="${concept.description}"></c:out></option>
							</c:forEach>
						</select>
					</div>
					<div class="col-md-4 form-group">
						<label for="startTraceConceptSelect"><spring:message code="configuration.startTraceConcept"/></label>
						<select id="startTraceConceptSelect" name="startTraceConcept" class="form-control chosen-select" data-placeholder="<spring:message code='common.select.option'/>">
							<option value=""></option>
							<c:forEach items="${startTraceConcepts}" var="concept">
								<option value="${concept.id}"  ${selectedStartTraceConcept == concept.id ? 'selected' : ''}><c:out value="${concept.code}"></c:out> - <c:out value="${concept.description}"></c:out></option>
							</c:forEach>
						</select>
					</div>
				</div>
				<div class="row">
					<div class="col-md-4 form-group">	
						<label for="changePasswordSelect"><spring:message code="common.changePassword"/></label>
						<select class="form-control chosen-select" id="changePasswordSelect" name="changePassword">
							<option value="true"><spring:message code="common.yes"/></option>
							<option value="false" selected><spring:message code="common.no"/></option>
						</select>
					</div>
					<div class="col-md-4 form-group">
						<label for="ANMATPasswordInput"><spring:message code="configuration.ANMATPassword"/></label>
						<input type="password" class="form-control" id="ANMATPasswordInput" name="ANMATPassword" disabled>
					</div>
					<div class="col-md-4 form-group">
						<label for="repeatANMATPasswordInput"><spring:message code="configuration.repeatANMATPassword"/></label>
						<input type="password" class="form-control" id="repeatANMATPasswordInput" name="repeatANMATPassword" disabled>
					</div>
				</div>
				<div class="row">
					<div class="col-md-3 form-group">	
						<label for="proxySelect"><spring:message code="configuration.informProxy"/></label>
						<select class="form-control chosen-select" id="proxySelect" name="active">
							<option value="true" ${informProxy == 'true' ? 'selected' : ''}><spring:message code="common.yes"/></option>
							<option value="false" ${informProxy == 'false' ? 'selected' : ''}><spring:message code="common.no"/></option>
						</select>
					</div>
					<div class="col-md-3 form-group">
						<label for="proxyInput"><spring:message code="configuration.proxyName"/></label>
						<input type="text" class="form-control" id="proxyNameInput" name="proxyName" value="${proxy}">
					</div>
					<div class="col-md-3 form-group">
						<label for="proxyNumberInput"><spring:message code="configuration.proxyNumber"/></label>
						<input type="text" class="form-control" id="proxyNumberInput" name="proxyNumber" value="${proxyPort}">
					</div>
					<div class="col-md-3 form-group">	
						<label for="daysAgoPendingTransactionsInput"><spring:message code="configuration.daysAgoPendingTransactions"/></label>
						<input type="text" class="form-control" id="daysAgoPendingTransactionsInput" name="daysAgoPendingTransactions" value="${daysAgoPendingTransactions}">
					</div>
				</div>
			</div>
		</div>
    </div>
</div>
<br>
<br>
<div class="row">
	<div class="col-md-2 col-md-offset-8">
		<button onclick="location.href='home.do'" class="btn btn-danger btn-block" id="abort"><span class="glyphicon glyphicon-arrow-left"></span> <spring:message code="common.back"/></button>
	</div>
	<div class="col-md-2">
		<button class="btn btn-success btn-block" id="confirm"><span class="glyphicon glyphicon-ok"></span> <spring:message code="common.confirm"/></button>
	</div>
</div>
</form>