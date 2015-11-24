<%@ page import="com.lsntsolutions.gtmApp.config.PropertyProvider" %>
<%@ page import="com.lsntsolutions.gtmApp.model.DeliveryNoteConfig" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<script type="text/javascript" src="js/form/configuration/updateProperty.js" ></script>
<script type="text/javascript">
	$(document).ready(function() {
		new UpdateProperty();
	});
</script>

<span id="licenseInfo" style="display:none"> Licencia Expira: <%=PropertyProvider.getInstance().getProp(PropertyProvider.LICENSE_EXPIRATION)%></span>

<form id="updatePropertyForm" action="" onsubmit="return false;">

<div class="row">
	<div class="col-md-9 col-lg-9 form-group">
		<h3><spring:message code="configuration.updateProperties"/></h3>
		<input type="hidden" class="form-control" id="idInput" value="${id}">
		<input type="hidden" class="form-control" id="lastTagInput" value="${lastTag}">
		<input type="hidden" class="form-control" id="lastDeliveryNoteNumberInput" value="${lastDeliveryNoteNumber}">
	</div>
</div>
	<br>
<div class="panel-group" id="accordion">
	<div class="panel panel-primary">
		<div class="panel-heading">
			<h3 class="panel-title">
		          <a class="accordion-toggle" data-toggle="collapse" data-parent="#accordion" href="#panel1"><spring:message code="configuration.info"/></a>
			</h3>
		</div>
		<div id="panel1" class="panel-collapse collapse">
			<div class="panel-body">
                <div class="row">
					<div class="col-md-4 col-lg-4 form-group">
						<label for="codeInput"><spring:message code="configuration.code"/></label> 
						<input type="text" class="form-control" id="codeInput" name="code" value="${code}">
					</div>
					<div class="col-md-4 col-lg-4 form-group">
						<label for="nameInput"><spring:message code="configuration.entity.name"/></label> 
						<input type="text" class="form-control" id="nameInput" name="name" value="${name}">
					</div>
					<div class="col-md-4 col-lg-4 form-group">
						<label for="taxIdInput"><spring:message code="configuration.taxId"/></label> 
						<input type="text" class="form-control" id="taxIdInput" name="taxId" value="${taxId}">
					</div>
				</div>
				
				<div class="row">
					<div class="col-md-4 col-lg-4 form-group">
						<label for="corporateNameInput"><spring:message code="configuration.corporateName"/></label> 
						<input type="text" class="form-control" id="corporateNameInput" name="corporateName" value="${corporateName}">
					</div>
					<div class="col-md-4 col-lg-4 form-group">
						<label for="provinceSelect"><spring:message code="configuration.province"/></label>
						<select class="form-control" id="provinceSelect" name="province">
							<option value="${selectedProvince.id}"><c:out value="${selectedProvince.name}"></c:out></option>
							<c:forEach items="${provinces}" var="province" varStatus="status">
								<option value="${province.id}"><c:out value="${province.name}"></c:out></option>
							</c:forEach>
						</select>
					</div>
					<div class="col-md-4 col-lg-4form-group">
						<label for="localityInput"><spring:message code="configuration.locality"/></label>
						<input type="text" class="form-control" id="localityInput" name="locality" value="${locality}">
					</div>
				</div>
				
				<div class="row">
					<div class="col-md-4 col-lg-4 form-group">
						<label for="addressInput"><spring:message code="configuration.address"/></label>
						<input type="text" class="form-control" id="addressInput" name="address" value="${address}">
					</div>
					<div class="col-md-4 col-lg-4 form-group">
						<label for="zipCodeInput"><spring:message code="configuration.zipCode"/></label>
						<input type="text" class="form-control" id="zipCodeInput" name="zipCode" value="${zipCode}">
					</div>
					<div class="col-md-4 col-lg-4 form-group">
						<label for="phoneInput"><spring:message code="configuration.phone"/></label>
						<input type="text" class="form-control" id="phoneInput" name="phone" value="${phone}">
					</div>
				</div>
				
				<div class="row">
					<div class="col-md-4 col-lg-4 form-group">
						<label for="mailInput"><spring:message code="configuration.mail"/></label>
						<input type="text" class="form-control" id="mailInput" name="mail" value="${mail}">
					</div>
					<div class="col-md-4 col-lg-4 form-group">
						<label for="glnInput"><spring:message code="configuration.gln"/></label>
						<input type="text" class="form-control" id="glnInput" name="gln" value="${gln}">
					</div>
					<div class="col-md-4 col-lg-4 form-group">
						<label for="agentSelect"><spring:message code="configuration.agent"/></label>
						<select class="form-control" id="agentSelect" name="agent">
							<option value="${selectedAgent.id}"><c:out value="${selectedAgent.description}"></c:out></option>
							<c:forEach items="${agents}" var="agent" varStatus="status">
								<option value="${agent.id}"><c:out value="${agent.description}"></c:out></option>
							</c:forEach>
						</select>
					</div>
				</div>
				<div class="row">
					<div class="col-md-4 col-lg-4 form-group">
						<label for="VATLiabilitySelect"><spring:message code="common.VATLiability"/></label>
						<select class="form-control" id="VATLiabilitySelect" name="VATLiability">
							<option value="${selectedVATLiability.id}"><c:out value="${selectedVATLiability.acronym}: ${selectedVATLiability.description}"></c:out></option>
							<c:forEach items="${VATLiabilities}" var="VATLiability" varStatus="status">
								<option value="${VATLiability.id}"><c:out value="${VATLiability.acronym}: ${VATLiability.description}"></c:out></option>
							</c:forEach>
						</select>
					</div>
				</div>
				<div class="row">
					<div class="col-md-4 col-lg-4 form-group">
						<br>
						<span class="btn btn-success fileinput-button" id="uploadLogoSpan">
        					<i class="glyphicon glyphicon-upload"></i><span> <spring:message code="configuration.uploadLogo"/></span>
        					<input id="uploadLogoInput" type="file" name="uploadLogo" data-url="uploadLogo.do">
    					</span>
					</div>
					<br>
					<br>
					<div id="progress" class="progress col-md-12 col-lg-12 form-group">
						<div class="progress-bar progress-bar-striped" aria-valuemin="0" aria-valuemax="100" role="progressbar"></div>
					</div>
					<div id="files" class="files col-md-4 form-group"></div>
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
		<div id="panel2" class="panel-collapse collapse">
			<div class="panel-body">
				<div class="row">
					<div class="col-md-12 col-lg-12 form-group">
						<label for="selfSerializedTagFilepathInput"><spring:message code="configuration.selfSerializedTagFilepath"/></label>
						<input type="text" class="form-control" id="selfSerializedTagFilepathInput" name="selfSerializedTagFilepath" value="${selfSerializedTagFilepath}">
					</div>
				</div>
				<div class="row">
					<div class="col-md-4 col-lg-4 form-group">
						<label for="proxySelect"><spring:message code="configuration.informProxy"/></label>
						<select class="form-control" id="proxySelect" name="active">
							<option value="true" ${informProxy == 'true' ? 'selected' : ''}><spring:message code="common.yes"/></option>
							<option value="false" ${informProxy == 'false' ? 'selected' : ''}><spring:message code="common.no"/></option>
						</select>
					</div>
					<div class="col-md-4 col-lg-4 form-group">
						<label for="proxyNameInput"><spring:message code="configuration.proxyName"/></label>
						<input type="text" class="form-control" id="proxyNameInput" name="proxyName" value="${proxy}">
					</div>
					<div class="col-md-4 col-lg-4 form-group">
						<label for="proxyNumberInput"><spring:message code="configuration.proxyNumber"/></label>
						<input type="text" class="form-control" id="proxyNumberInput" name="proxyNumber" value="${proxyPort}">
					</div>
				</div>
				<div class="row">
					<div class="col-md-4 col-lg-4 form-group">
						<label for="provisioningRequireAuthorizationSelect"><spring:message code="common.provisioningRequireAuthorization"/></label>
						<select class="form-control" id="provisioningRequireAuthorizationSelect" name="provisioningRequireAuthorization">
							<option value="true" ${provisioningRequireAuthorization == 'true' ? 'selected' : ''}><spring:message code="common.yes"/></option>
							<option value="false" ${provisioningRequireAuthorization == 'false' ? 'selected' : ''}><spring:message code="common.no"/></option>
						</select>
					</div>
					<div class="col-md-4 col-lg-4 form-group">
						<label for="printPickingListSelect"><spring:message code="common.pickingList"/></label>
						<select class="form-control" id="printPickingListSelect" name="printPickingList">
							<option value="true" ${printPickingList == 'true' ? 'selected' : ''}><spring:message code="common.yes"/></option>
							<option value="false" ${printPickingList == 'false' ? 'selected' : ''}><spring:message code="common.no"/></option>
						</select>
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
		<div id="panel3" class="panel-collapse collapse">
			<div class="panel-body">
                <div class="row">
					<div class="col-md-4 col-lg-4 form-group">
						<label for="ANMATNameInput"><spring:message code="configuration.ANMATName"/></label>
						<input type="text" class="form-control" id="ANMATNameInput" name="ANMATName" value="${ANMATName}">
					</div>
					<div class="col-md-4 col-lg-4 form-group">
						<label for="startTraceConceptSelect"><spring:message code="configuration.startTraceConcept"/></label>
						<select id="startTraceConceptSelect" name="startTraceConcept" class="form-control" data-placeholder="<spring:message code='common.select.option'/>">
							<option value=""></option>
							<c:forEach items="${concepts}" var="concept">
								<option value="${concept.id}"  ${selectedStartTraceConcept == concept.id ? 'selected' : ''}><c:out value="${concept.code}"></c:out> - <c:out value="${concept.description}"></c:out></option>
							</c:forEach>
						</select>
					</div>
					<div class="col-md-4 col-lg-4 form-group">
						<label for="supplyingConceptSelect"><spring:message code="configuration.supplyingConcept"/></label>
						<select id="supplyingConceptSelect" name="supplyingConcept" class="form-control" data-placeholder="<spring:message code='common.select.option'/>">
							<option value=""></option>
							<c:forEach items="${deliveryNoteconcepts}" var="concept">
								<option value="${concept.id}"  ${selectedSupplyingConcept == concept.id ? 'selected' : ''}><c:out value="${concept.code}"></c:out> - <c:out value="${concept.description}"></c:out></option>
							</c:forEach>
						</select>
					</div>
				</div>
				<div class="row">
					<div class="col-md-4 col-lg-4 form-group">
						<label for="changePasswordSelect"><spring:message code="common.changePassword"/></label>
						<select class="form-control" id="changePasswordSelect" name="changePassword">
							<option value="true"><spring:message code="common.yes"/></option>
							<option value="false" selected><spring:message code="common.no"/></option>
						</select>
					</div>
					<div class="col-md-4 col-lg-4 form-group">
						<label for="ANMATPasswordInput"><spring:message code="configuration.ANMATPassword"/></label>
						<input type="password" class="form-control" id="ANMATPasswordInput" name="ANMATPassword" disabled>
					</div>
					<div class="col-md-4 col-lg-4 form-group">
						<label for="repeatANMATPasswordInput"><spring:message code="configuration.repeatANMATPassword"/></label>
						<input type="password" class="form-control" id="repeatANMATPasswordInput" name="repeatANMATPassword" disabled>
					</div>
				</div>
				<div class="row">
					<div class="col-md-6 col-lg-6 form-group">
						<label for="daysAgoPendingTransactionsInput"><spring:message code="configuration.daysAgoPendingTransactions"/></label>
						<input type="text" class="form-control" id="daysAgoPendingTransactionsInput" name="daysAgoPendingTransactions" value="${daysAgoPendingTransactions}">
					</div>
					<div class="col-md-6 col-lg-6 form-group">
						<label for="informAnmatSelect"><spring:message code="common.informAnmat"/></label>
						<select class="form-control" id="informAnmatSelect" name="informAnmat">
							<option value="true" ${informAnmat == 'true' ? 'selected' : ''}><spring:message code="common.yes"/></option>
							<option value="false" ${informAnmat == 'false' ? 'selected' : ''}><spring:message code="common.no"/></option>
						</select>
					</select>
					</div>
				</div>
			</div>
		</div>
    </div>
	<div class="panel panel-warning">
		<div class="panel-heading">
			<h3 class="panel-title">
				<a class="accordion-toggle" data-toggle="collapse" data-parent="#accordion" href="#panel4"><spring:message code="configuration.deliveryNoteConfig"/></a>
			</h3>
		</div>
		<div id="panel4" class="panel-collapse collapse">
			<div class="panel-body">
				<div class="row">
					<div class="col-md-4 col-lg-4 form-group">
						<label><spring:message code="configuration.fontSize"/></label>
						<input type="text" class="form-control" id="fontSizeInput" name="fontSize" value="${fontSize}">
					</div>
					<div class="col-md-4 col-lg-4 form-group">
						<label><spring:message code="configuration.number"/></label>
						<div class="input-group">
							<span class="input-group-addon"><spring:message code="configuration.positionX"/></span>
							<input type="text" class="form-control" id="numberXInput" name="numberX" value="${numberX}">
							<span class="input-group-addon"><spring:message code="configuration.positionY"/></span>
							<input type="text" class="form-control" id="numberYInput" name="numberY" value="${numberY}">
							<span class="input-group-addon"><spring:message code="configuration.isPrinting"/>
								<input type="checkbox" id="numberPrintInput" name="numberPrint" ${numberPrint == '1' ? 'checked' : ''}>
							</span>
						</div>
					</div>
					<div class="col-md-4 col-lg-4 form-group">
						<label><spring:message code="configuration.date"/></label>
						<div class="input-group">
							<span class="input-group-addon"><spring:message code="configuration.positionX"/></span>
							<input type="text" class="form-control" id="dateXInput" name="dateX" value="${dateX}">
							<span class="input-group-addon"><spring:message code="configuration.positionY"/></span>
							<input type="text" class="form-control" id="dateYInput" name="dateY" value="${dateY}">
							<span class="input-group-addon"><spring:message code="configuration.isPrinting"/>
								<input type="checkbox" id="datePrintInput" name="datePrint" ${datePrint == '1' ? 'checked' : ''}>
							</span>
						</div>
					</div>
				</div>
				<div class="row">
					<div class="col-md-4 col-lg-4 form-group">
						<label><spring:message code="configuration.issuer.corporateName"/></label>
						<div class="input-group">
							<span class="input-group-addon"><spring:message code="configuration.positionX"/></span>
							<input type="text" class="form-control" id="issuerCorporateNameXInput" name="issuerCorporateNameX" value="${issuerCorporateNameX}">
							<span class="input-group-addon"><spring:message code="configuration.positionY"/></span>
							<input type="text" class="form-control" id="issuerCorporateNameYInput" name="issuerCorporateNameY" value="${issuerCorporateNameY}">
							<span class="input-group-addon"><spring:message code="configuration.isPrinting"/>
								<input type="checkbox" id="issuerCorporateNamePrintInput" name="issuerCorporateNamePrint" ${issuerCorporateNamePrint == '1' ? 'checked' : ''}>
							</span>
						</div>
					</div>
					<div class="col-md-4 col-lg-4 form-group">
						<label><spring:message code="configuration.issuer.gln"/></label>
						<div class="input-group">
							<span class="input-group-addon"><spring:message code="configuration.positionX"/></span>
							<input type="text" class="form-control" id="issuerGlnXInput" name="issuerGlnX" value="${issuerGlnX}">
							<span class="input-group-addon"><spring:message code="configuration.positionY"/></span>
							<input type="text" class="form-control" id="issuerGlnYInput" name="issuerGlnY" value="${issuerGlnY}">
							<span class="input-group-addon"><spring:message code="configuration.isPrinting"/>
								<input type="checkbox" id="issuerGlnPrintInput" name="issuerGlnPrint" ${issuerGlnPrint == '1' ? 'checked' : ''}>
							</span>
						</div>
					</div>
				</div>
				<div class="row">
					<div class="col-md-4 col-lg-4 form-group">
						<label><spring:message code="configuration.issuer.address"/></label>
						<div class="input-group">
							<span class="input-group-addon"><spring:message code="configuration.positionX"/></span>
							<input type="text" class="form-control" id="issuerAddressXInput" name="issuerAddressX" value="${issuerAddressX}">
							<span class="input-group-addon"><spring:message code="configuration.positionY"/></span>
							<input type="text" class="form-control" id="issuerAddressYInput" name="issuerAddressY" value="${issuerAddressY}">
							<span class="input-group-addon"><spring:message code="configuration.isPrinting"/>
								<input type="checkbox" id="issuerAddressPrintInput" name="issuerAddressPrint" ${issuerAddressPrint == '1' ? 'checked' : ''}>
							</span>
						</div>
					</div>
					<div class="col-md-4 col-lg-4 form-group">
						<label><spring:message code="configuration.issuer.locality"/></label>
						<div class="input-group">
							<span class="input-group-addon"><spring:message code="configuration.positionX"/></span>
							<input type="text" class="form-control" id="issuerLocalityXInput" name="issuerLocalityX" value="${issuerLocalityX}">
							<span class="input-group-addon"><spring:message code="configuration.positionY"/></span>
							<input type="text" class="form-control" id="issuerLocalityYInput" name="issuerLocalityY" value="${issuerLocalityY}">
							<span class="input-group-addon"><spring:message code="configuration.isPrinting"/>
								<input type="checkbox" id="issuerLocalityPrintInput" name="issuerLocalityPrint" ${issuerLocalityPrint == '1' ? 'checked' : ''}>
							</span>
						</div>
					</div>
					<div class="col-md-4 col-lg-4 form-group">
						<label><spring:message code="configuration.issuer.zipcode"/></label>
						<div class="input-group">
							<span class="input-group-addon"><spring:message code="configuration.positionX"/></span>
							<input type="text" class="form-control" id="issuerZipcodeXInput" name="issuerLocalityX" value="${issuerZipcodeX}">
							<span class="input-group-addon"><spring:message code="configuration.positionY"/></span>
							<input type="text" class="form-control" id="issuerZipcodeYInput" name="issuerLocalityY" value="${issuerZipcodeY}">
							<span class="input-group-addon"><spring:message code="configuration.isPrinting"/>
								<input type="checkbox" id="issuerZipcodePrintInput" name="issuerZipcodePrint" ${issuerZipcodePrint == '1' ? 'checked' : ''}>
							</span>
						</div>
					</div>
				</div>
				<div class="row">
					<div class="col-md-4 col-lg-4 form-group">
						<label><spring:message code="configuration.issuer.province"/></label>
						<div class="input-group">
							<span class="input-group-addon"><spring:message code="configuration.positionX"/></span>
							<input type="text" class="form-control" id="issuerProvinceXInput" name="issuerProvinceX" value="${issuerProvinceX}">
							<span class="input-group-addon"><spring:message code="configuration.positionY"/></span>
							<input type="text" class="form-control" id="issuerProvinceYInput" name="issuerProvinceY" value="${issuerProvinceY}">
							<span class="input-group-addon"><spring:message code="configuration.isPrinting"/>
								<input type="checkbox" id="issuerProvincePrintInput" name="issuerAddressPrint" ${issuerProvincePrint == '1' ? 'checked' : ''}>
							</span>
						</div>
					</div>
					<div class="col-md-4 col-lg-4 form-group">
						<label><spring:message code="configuration.issuer.vatliability"/></label>
						<div class="input-group">
							<span class="input-group-addon"><spring:message code="configuration.positionX"/></span>
							<input type="text" class="form-control" id="issuerVatliabilityXInput" name="issuerVatliabilityX" value="${issuerVatliabilityX}">
							<span class="input-group-addon"><spring:message code="configuration.positionY"/></span>
							<input type="text" class="form-control" id="issuerVatliabilityYInput" name="issuerVatliabilityY" value="${issuerVatliabilityY}">
							<span class="input-group-addon"><spring:message code="configuration.isPrinting"/>
								<input type="checkbox" id="issuerVatliabilityPrintInput" name="issuerVatliabilityPrint" ${issuerVatliabilityPrint == '1' ? 'checked' : ''}>
							</span>
						</div>
					</div>
					<div class="col-md-4 col-lg-4 form-group">
						<label><spring:message code="configuration.issuer.tax"/></label>
						<div class="input-group">
							<span class="input-group-addon"><spring:message code="configuration.positionX"/></span>
							<input type="text" class="form-control" id="issuerTaxXInput" name="issuerTaxX" value="${issuerTaxX}">
							<span class="input-group-addon"><spring:message code="configuration.positionY"/></span>
							<input type="text" class="form-control" id="issuerTaxYInput" name="issuerTaxY" value="${issuerTaxY}">
							<span class="input-group-addon"><spring:message code="configuration.isPrinting"/>
								<input type="checkbox" id="issuerTaxPrintInput" name="issuerTaxPrint" ${issuerTaxPrint == '1' ? 'checked' : ''}>
							</span>
						</div>
					</div>
				</div>
				<div class="row">
					<div class="col-md-4 col-lg-4 form-group">
						<label><spring:message code="configuration.deliveryLocation.corporateName"/></label>
						<div class="input-group">
							<span class="input-group-addon"><spring:message code="configuration.positionX"/></span>
							<input type="text" class="form-control" id="deliveryLocationCorporateNameXInput" name="deliveryLocationCorporateNameX" value="${deliveryLocationCorporateNameX}">
							<span class="input-group-addon"><spring:message code="configuration.positionY"/></span>
							<input type="text" class="form-control" id="deliveryLocationCorporateNameYInput" name="deliveryLocationCorporateNameY" value="${deliveryLocationCorporateNameY}">
							<span class="input-group-addon"><spring:message code="configuration.isPrinting"/>
								<input type="checkbox" id="deliveryLocationCorporateNamePrintInput" name="deliveryLocationCorporateNamePrint" ${deliveryLocationCorporateNamePrint == '1' ? 'checked' : ''}>
							</span>
						</div>
					</div>
					<div class="col-md-4 col-lg-4 form-group">
						<label><spring:message code="configuration.deliveryLocation.gln"/></label>
						<div class="input-group">
							<span class="input-group-addon"><spring:message code="configuration.positionX"/></span>
							<input type="text" class="form-control" id="deliveryLocationGlnXInput" name="deliveryLocationGlnX" value="${deliveryLocationGlnX}">
							<span class="input-group-addon"><spring:message code="configuration.positionY"/></span>
							<input type="text" class="form-control" id="deliveryLocationGlnYInput" name="deliveryLocationGlnY" value="${deliveryLocationGlnY}">
							<span class="input-group-addon"><spring:message code="configuration.isPrinting"/>
								<input type="checkbox" id="deliveryLocationGlnPrintInput" name="deliveryLocationGlnPrint" ${deliveryLocationGlnPrint == '1' ? 'checked' : ''}>
							</span>
						</div>
					</div>
				</div>
				<div class="row">
					<div class="col-md-4 col-lg-4 form-group">
						<label><spring:message code="configuration.deliveryLocation.address"/></label>
						<div class="input-group">
							<span class="input-group-addon"><spring:message code="configuration.positionX"/></span>
							<input type="text" class="form-control" id="deliveryLocationAddressXInput" name="deliveryLocationAddressX" value="${deliveryLocationAddressX}">
							<span class="input-group-addon"><spring:message code="configuration.positionY"/></span>
							<input type="text" class="form-control" id="deliveryLocationAddressYInput" name="deliveryLocationAddressY" value="${deliveryLocationAddressY}">
							<span class="input-group-addon"><spring:message code="configuration.isPrinting"/>
								<input type="checkbox" id="deliveryLocationAddressPrintInput" name="deliveryLocationAddressPrint" ${deliveryLocationAddressPrint == '1' ? 'checked' : ''}>
							</span>
						</div>
					</div>
					<div class="col-md-4 col-lg-4 form-group">
						<label><spring:message code="configuration.deliveryLocation.locality"/></label>
						<div class="input-group">
							<span class="input-group-addon"><spring:message code="configuration.positionX"/></span>
							<input type="text" class="form-control" id="deliveryLocationLocalityXInput" name="deliveryLocationLocalityX" value="${deliveryLocationLocalityX}">
							<span class="input-group-addon"><spring:message code="configuration.positionY"/></span>
							<input type="text" class="form-control" id="deliveryLocationLocalityYInput" name="deliveryLocationLocalityY" value="${deliveryLocationLocalityY}">
							<span class="input-group-addon"><spring:message code="configuration.isPrinting"/>
								<input type="checkbox" id="deliveryLocationLocalityPrintInput" name="deliveryLocationLocalityPrint" ${deliveryLocationLocalityPrint == '1' ? 'checked' : ''}>
							</span>
						</div>
					</div>
					<div class="col-md-4 col-lg-4 form-group">
						<label><spring:message code="configuration.deliveryLocation.zipcode"/></label>
						<div class="input-group">
							<span class="input-group-addon"><spring:message code="configuration.positionX"/></span>
							<input type="text" class="form-control" id="deliveryLocationZipcodeXInput" name="deliveryLocationLocalityX" value="${deliveryLocationZipcodeX}">
							<span class="input-group-addon"><spring:message code="configuration.positionY"/></span>
							<input type="text" class="form-control" id="deliveryLocationZipcodeYInput" name="deliveryLocationLocalityY" value="${deliveryLocationZipcodeY}">
							<span class="input-group-addon"><spring:message code="configuration.isPrinting"/>
								<input type="checkbox" id="deliveryLocationZipcodePrintInput" name="deliveryLocationZipcodePrint" ${deliveryLocationZipcodePrint == '1' ? 'checked' : ''}>
							</span>
						</div>
					</div>
				</div>
				<div class="row">
					<div class="col-md-4 col-lg-4 form-group">
						<label><spring:message code="configuration.deliveryLocation.province"/></label>
						<div class="input-group">
							<span class="input-group-addon"><spring:message code="configuration.positionX"/></span>
							<input type="text" class="form-control" id="deliveryLocationProvinceXInput" name="deliveryLocationProvinceX" value="${deliveryLocationProvinceX}">
							<span class="input-group-addon"><spring:message code="configuration.positionY"/></span>
							<input type="text" class="form-control" id="deliveryLocationProvinceYInput" name="deliveryLocationProvinceY" value="${deliveryLocationProvinceY}">
							<span class="input-group-addon"><spring:message code="configuration.isPrinting"/>
								<input type="checkbox" id="deliveryLocationProvincePrintInput" name="deliveryLocationAddressPrint" ${deliveryLocationProvincePrint == '1' ? 'checked' : ''}>
							</span>
						</div>
					</div>
					<div class="col-md-4 col-lg-4 form-group">
						<label><spring:message code="configuration.deliveryLocation.vatliability"/></label>
						<div class="input-group">
							<span class="input-group-addon"><spring:message code="configuration.positionX"/></span>
							<input type="text" class="form-control" id="deliveryLocationVatliabilityXInput" name="deliveryLocationVatliabilityX" value="${deliveryLocationVatliabilityX}">
							<span class="input-group-addon"><spring:message code="configuration.positionY"/></span>
							<input type="text" class="form-control" id="deliveryLocationVatliabilityYInput" name="deliveryLocationVatliabilityY" value="${deliveryLocationVatliabilityY}">
							<span class="input-group-addon"><spring:message code="configuration.isPrinting"/>
								<input type="checkbox" id="deliveryLocationVatliabilityPrintInput" name="deliveryLocationVatliabilityPrint" ${deliveryLocationVatliabilityPrint == '1' ? 'checked' : ''}>
							</span>
						</div>
					</div>
					<div class="col-md-4 col-lg-4 form-group">
						<label><spring:message code="configuration.deliveryLocation.tax"/></label>
						<div class="input-group">
							<span class="input-group-addon"><spring:message code="configuration.positionX"/></span>
							<input type="text" class="form-control" id="deliveryLocationTaxXInput" name="deliveryLocationTaxX" value="${deliveryLocationTaxX}">
							<span class="input-group-addon"><spring:message code="configuration.positionY"/></span>
							<input type="text" class="form-control" id="deliveryLocationTaxYInput" name="deliveryLocationTaxY" value="${deliveryLocationTaxY}">
							<span class="input-group-addon"><spring:message code="configuration.isPrinting"/>
								<input type="checkbox" id="deliveryLocationTaxPrintInput" name="deliveryLocationTaxPrint" ${deliveryLocationTaxPrint == '1' ? 'checked' : ''}>
							</span>
						</div>
					</div>
				</div>
				<div class="row">
					<div class="col-md-4 col-lg-4 form-group">
						<label><spring:message code="configuration.affiliate"/></label>
						<div class="input-group">
							<span class="input-group-addon"><spring:message code="configuration.positionX"/></span>
							<input type="text" class="form-control" id="affiliateXInput" name="affiliateX" value="${affiliateX}">
							<span class="input-group-addon"><spring:message code="configuration.positionY"/></span>
							<input type="text" class="form-control" id="affiliateYInput" name="affiliateY" value="${affiliateY}">
							<span class="input-group-addon"><spring:message code="configuration.isPrinting"/>
								<input type="checkbox" id="affiliatePrintInput" name="affiliatePrint" ${affiliatePrint == '1' ? 'checked' : ''}>
							</span>
						</div>
					</div>
					<div class="col-md-4 col-lg-4 form-group">
						<label><spring:message code="configuration.order"/></label>
						<div class="input-group">
							<span class="input-group-addon"><spring:message code="configuration.positionX"/></span>
							<input type="text" class="form-control" id="orderXInput" name="orderX" value="${orderX}">
							<span class="input-group-addon"><spring:message code="configuration.positionY"/></span>
							<input type="text" class="form-control" id="orderYInput" name="orderY" value="${orderY}">
							<span class="input-group-addon"><spring:message code="configuration.isPrinting"/>
								<input type="checkbox" id="orderPrintInput" name="orderPrint" ${orderPrint == '1' ? 'checked' : ''}>
							</span>
						</div>
					</div>
					<div class="col-md-4 col-lg-4 form-group">
						<label><spring:message code="configuration.product.details.y"/></label>
						<div class="input-group">
							<span class="input-group-addon"><spring:message code="configuration.positionY"/></span>
							<input type="text" class="form-control" id="productDetailsYInput" name="productDetailsY" value="${productDetailsY}">
						</div>
					</div>
					<div class="col-md-4 col-lg-4 form-group">
						<label><spring:message code="configuration.product.description.x"/></label>
						<div class="input-group">
							<span class="input-group-addon"><spring:message code="configuration.positionX"/></span>
							<input type="text" class="form-control" id="productDescriptionXInput" name="productDescriptionX" value="${productDescriptionX}">
							<span class="input-group-addon"><spring:message code="configuration.isPrinting"/>
								<input type="checkbox" id="productDescriptionPrintInput" name="productDescriptionPrint" ${productDescriptionPrint == '1' ? 'checked' : ''}>
							</span>
						</div>
					</div>
				</div>
				<div class="row">
					<div class="col-md-3 col-lg-3 form-group">
						<label><spring:message code="configuration.product.monodrug.x"/></label>
						<div class="input-group">
							<span class="input-group-addon"><spring:message code="configuration.positionX"/></span>
							<input type="text" class="form-control" id="productMonodrugXInput" name="productMonodrugX" value="${productMonodrugX}">
							<span class="input-group-addon"><spring:message code="configuration.isPrinting"/>
								<input type="checkbox" id="productMonodrugPrintInput" name="productMonodrugPrint" ${productMonodrugPrint == '1' ? 'checked' : ''}>
							</span>
						</div>
					</div>
					<div class="col-md-3 col-lg-3 form-group">
						<label><spring:message code="configuration.product.brand.x"/></label>
						<div class="input-group">
							<span class="input-group-addon"><spring:message code="configuration.positionX"/></span>
							<input type="text" class="form-control" id="productBrandXInput" name="productBrandX" value="${productBrandX}">
							<span class="input-group-addon"><spring:message code="configuration.isPrinting"/>
								<input type="checkbox" id="productBrandPrintInput" name="productBrandPrint" ${productBrandPrint == '1' ? 'checked' : ''}>
							</span>
						</div>
					</div>
					<div class="col-md-3 col-lg-3 form-group">
						<label><spring:message code="configuration.product.amount.x"/></label>
						<div class="input-group">
							<span class="input-group-addon"><spring:message code="configuration.positionX"/></span>
							<input type="text" class="form-control" id="productAmountXInput" name="productAmountX" value="${productAmountX}">
							<span class="input-group-addon"><spring:message code="configuration.isPrinting"/>
								<input type="checkbox" id="productAmountPrintInput" name="productAmountPrint" ${productAmountPrint == '1' ? 'checked' : ''}>
							</span>
						</div>
					</div>
					<div class="col-md-3 col-lg-3 form-group">
						<label><spring:message code="configuration.product.batchExpirationdate.x"/></label>
						<div class="input-group">
							<span class="input-group-addon"><spring:message code="configuration.positionX"/></span>
							<input type="text" class="form-control" id="productBatchExpirationdateXInput" name="productBatchExpirationdateX" value="${productBatchExpirationdateX}">
							<span class="input-group-addon"><spring:message code="configuration.isPrinting"/>
								<input type="checkbox" id="productBatchExpirationdatePrintInput" name="productBatchExpirationdatePrint" ${productBatchExpirationdatePrint == '1' ? 'checked' : ''}>
							</span>
						</div>
					</div>
				</div>
				<div class="row">
					<div class="col-md-3 col-lg-3 form-group">
						<label><spring:message code="configuration.serial.column1.x"/></label>
						<div class="input-group">
							<span class="input-group-addon"><spring:message code="configuration.positionX"/></span>
							<input type="text" class="form-control" id="serialColumn1XInput" name="serialColumn1X" value="${serialColumn1X}">
							<span class="input-group-addon"><spring:message code="configuration.isPrinting"/>
								<input type="checkbox" id="serialColumn1PrintInput" name="serialColumn1Print" ${serialColumn1Print == '1' ? 'checked' : ''}>
							</span>
						</div>
					</div>
					<div class="col-md-3 col-lg-3 form-group">
						<label><spring:message code="configuration.serial.column2.x"/></label>
						<div class="input-group">
							<span class="input-group-addon"><spring:message code="configuration.positionX"/></span>
							<input type="text" class="form-control" id="serialColumn2XInput" name="serialColumn2X" value="${serialColumn2X}">
							<span class="input-group-addon"><spring:message code="configuration.isPrinting"/>
								<input type="checkbox" id="serialColumn2PrintInput" name="serialColumn2Print" ${serialColumn2Print == '1' ? 'checked' : ''}>
							</span>
						</div>
					</div>
					<div class="col-md-3 col-lg-3 form-group">
						<label><spring:message code="configuration.serial.column3.x"/></label>
						<div class="input-group">
							<span class="input-group-addon"><spring:message code="configuration.positionX"/></span>
							<input type="text" class="form-control" id="serialColumn3XInput" name="serialColumn3X" value="${serialColumn3X}">
							<span class="input-group-addon"><spring:message code="configuration.isPrinting"/>
								<input type="checkbox" id="serialColumn3PrintInput" name="serialColumn3Print" ${serialColumn3Print == '1' ? 'checked' : ''}>
							</span>
						</div>
					</div>
					<div class="col-md-3 col-lg-3 form-group">
						<label><spring:message code="configuration.serial.column4.x"/></label>
						<div class="input-group">
							<span class="input-group-addon"><spring:message code="configuration.positionX"/></span>
							<input type="text" class="form-control" id="serialColumn4XInput" name="serialColumn4X" value="${serialColumn4X}">
							<span class="input-group-addon"><spring:message code="configuration.isPrinting"/>
								<input type="checkbox" id="serialColumn4PrintInput" name="serialColumn4Print" ${serialColumn4Print == '1' ? 'checked' : ''}>
							</span>
						</div>
					</div>
				</div>
				<div class="row">
					<div class="col-md-4 col-lg-4 form-group">
						<label><spring:message code="configuration.numberOfItems"/></label>
						<div class="input-group">
							<span class="input-group-addon"><spring:message code="configuration.positionX"/></span>
							<input type="text" class="form-control" id="numberOfItemsXInput" name="numberOfItemsX" value="${numberOfItemsX}">
							<span class="input-group-addon"><spring:message code="configuration.positionY"/></span>
							<input type="text" class="form-control" id="numberOfItemsYInput" name="numberOfItemsY" value="${numberOfItemsY}">
							<span class="input-group-addon"><spring:message code="configuration.isPrinting"/>
								<input type="checkbox" id="numberOfItemsPrintInput" name="numberOfItemsPrint" ${numberOfItemsPrint == '1' ? 'checked' : ''}>
							</span>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<br>
<br>
<div class="row">
	<div class="col-md-2 col-md-offset-8 col-lg-2 col-lg-offset-8">
		<button onclick="location.href='home.do'" class="btn btn-danger btn-block" id="abort"><span class="glyphicon glyphicon-arrow-left"></span> <spring:message code="common.back"/></button>
	</div>
	<div class="col-md-2 col-lg-2">
		<button class="btn btn-success btn-block" id="confirm"><span class="glyphicon glyphicon-ok"></span> <spring:message code="common.confirm"/></button>
	</div>
</div>
</form>