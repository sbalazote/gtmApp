<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<script type="text/javascript" src="js/form/administration/alfabetaUpdateProducts.js" /></script>
<script type="text/javascript">
	$(document).ready(function() {
		new AlfabetaUpdateProducts();
	});
</script>

<form id="alfabetaUpdateproductsAdministrationForm" action="" onsubmit="return false;">

	<div class="row">
		<div class="col-md-9 form-group">
			<h2>
				<spring:message code="administration.alfabetaUpdateProducts.title" />
			</h2>
		</div>
	</div>

	<div class="row">
		<div class="col-md-6 form-group">
			<label for="priceFieldByteOffsetInput"><spring:message	code="administration.alfabetaUpdateProducts.priceFieldByteOffset" /></label> 
			<input type="text" class="form-control" id="priceFieldByteOffsetInput" name="priceFieldByteOffset" value="101">
		</div>
		<div class="col-md-6 form-group">
			<label for="priceFieldLengthInput"><spring:message code="administration.alfabetaUpdateProducts.priceFieldLength" /></label> 
			<input type="text" class="form-control" id="priceFieldLengthInput" name="priceFieldLength" value="9">
		</div>
	</div>

	<div class="row">
		<div class="col-md-6 form-group">
			<label for="codeFieldByteOffsetInput"><spring:message code="administration.alfabetaUpdateProducts.codeFieldByteOffset" /></label> 
			<input type="text" class="form-control" id="codeFieldByteOffsetInput" name="codeFieldByteOffset" value="126">
		</div>
		<div class="col-md-6 form-group">
			<label for="codeFieldLengthInput"><spring:message code="administration.alfabetaUpdateProducts.codeFieldLength" /></label> 
			<input type="text" class="form-control" id="codeFieldLengthInput" name="codeFieldLength" value="5">
		</div>
	</div>

	<div class="row">
		<div class="col-md-6 form-group">
			<label for="gtinFieldByteOffsetInput"><spring:message code="administration.alfabetaUpdateProducts.gtinFieldByteOffset" /></label> 
			<input type="text" class="form-control" id="gtinFieldByteOffsetInput" name="gtinFieldByteOffset" value="132">
		</div>
		<div class="col-md-6 form-group">
			<label for="gtinFieldLengthInput"><spring:message code="administration.alfabetaUpdateProducts.GTINFieldLength" /></label> 
			<input type="text" class="form-control" id="gtinFieldLengthInput" name="GTINFieldLength" value="13">
		</div>
	</div>

	<div class="row">
		<div class="col-md-3 form-group">
			<label for="alfabetaUpdateFileInput"><spring:message code="administration.alfabetaUpdateProducts.alfabetaUpdateFile" /></label>
    		<span class="btn btn-info fileinput-button form-control">
        		<i class="glyphicon glyphicon-plus"></i>
        			<span><spring:message code="administration.alfabetaUpdateProducts.addAlfabetaUpdateFile" /></span>
				<input id="alfabetaUpdateFileInput" type="file" name="alfabetaUpdateFile" data-url="upload.do">
			</span>
		</div>
	</div>
	
	<div class="row">
		<div class="col-md-12 form-group">
			<table id="uploaded-files" class="table table-striped my-table">
				<thead>
					<tr>
						<th><spring:message code="administration.alfabetaUpdateProducts.fileName" /></th>
						<th><spring:message code="administration.alfabetaUpdateProducts.fileSize" /></th>
					</tr>
				</thead>
				<tbody></tbody>
			</table>
		</div>
	</div>

	<div class="row">
		<div class="col-md-2 col-md-offset-8">
			<button onclick="location.href='productAdministration.do'"
				class="btn btn-default btn-block" id="abort">
				<span class="glyphicon glyphicon-arrow-left"></span> 
				<spring:message code="common.back"/>
			</button>
		</div>
		<div class="col-md-2">
			<button class="btn btn-success btn-block" id="confirm">
				<span class="glyphicon glyphicon-ok"></span>
				<spring:message code="common.update" />
			</button>
		</div>
	</div>

</form>