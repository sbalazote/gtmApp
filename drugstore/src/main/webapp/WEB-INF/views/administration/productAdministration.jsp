<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<script type="text/javascript" src="js/form/administration/productAdministration.js" /></script>
<script type="text/javascript" src="js/form/administration/save/saveProduct.js" /></script>
<script type="text/javascript" src="js/form/administration/save/saveProductBrand.js" /></script>
<script type="text/javascript" src="js/form/administration/save/saveProductDrugCategory.js" /></script>
<script type="text/javascript" src="js/form/administration/save/saveProductGroup.js" /></script>
<script type="text/javascript" src="js/form/administration/save/saveProductMonodrug.js" /></script>
<script type="text/javascript">
	$(document).ready(function() {
		new SaveProduct();
		new SaveProductBrand();
		new SaveProductDrugCategory();
		new SaveProductGroup();
		new SaveProductMonodrug();
	});
</script>

<div class="row">
	<div class="col-md-9 form-group">
		<h2><spring:message code="administration.productAdministration"/></h2>
	</div>
</div>
	
<div>
	<div class="bs-example bs-example-tabs">
		<ul id="myTab" class="nav nav-tabs">
			<li class="active"><a href="#product" data-toggle="tab"><spring:message code="common.product"/></a></li>
			<li><a href="#productBrand" data-toggle="tab"><spring:message code="common.brand"/></a></li>
			<li><a href="#productMonodrug" data-toggle="tab"><spring:message code="common.monodrug"/></a></li>
			<li><a href="#productDrugCategory" data-toggle="tab"><spring:message code="common.drugCategory"/></a></li>
			<li><a href="#productGroup" data-toggle="tab"><spring:message code="common.productGroup"/></a></li>
		</ul>
		<br>
		<div id="myTabContent" class="tab-content">
			
			<div class="tab-pane fade in active" id="product">
				<div class="row">
					<div class="col-md-4">
						<button class="btn btn-primary btn-block" id="addProduct"><span class="glyphicon glyphicon-plus"></span> <spring:message code="common.add.entity"/></button>
					</div>
					<div class="col-md-4">
						<button class="btn btn-success btn-block" id="alfabetaUpdateProducts"><span class="glyphicon glyphicon-upload"></span> <spring:message code="common.update.alfabeta"/></button>
					</div>
				</div>
				
				<br>
				
				<table id="productsTable" class="table table-condensed table-hover table-striped">
					<thead>
						<tr>
							<th data-column-id="id" data-header-css-class="idColumn" data-type="numeric"><spring:message code="common.id"/></th>
							<th data-column-id="code" data-type="numeric"><spring:message code="common.code"/></th>
							<th data-column-id="description" data-header-css-class="descriptionColumn"><spring:message code="common.description"/></th>
							<th data-column-id="gtin" data-header-css-class="gtinColumn" data-type="numeric"><spring:message code="common.gtin"/></th>
							<th data-column-id="price" data-formatter="price"><spring:message code="common.price"/></th>
							<th data-column-id="isCold"><spring:message code="common.cold"/></th>
							<th data-column-id="commands" data-header-css-class="commandsColumn" data-formatter="commands" data-sortable="false"><spring:message code="administration.commands.tableLabel"/></th>
						</tr>
					</thead>
				</table>
			</div>
			
			<div class="tab-pane fade" id="productBrand">
				<div class="row">
					<div class="col-md-4">
						<button class="btn btn-primary btn-block" id="addProductBrand"><span class="glyphicon glyphicon-plus"></span> <spring:message code="common.add.entity"/></button>
					</div>
				</div>
				
				<br>
				<table id="productBrandsTable" class="table table-condensed table-hover table-striped">				
					<thead>
						<tr>
							<th data-column-id="id" data-header-css-class="idColumn" data-type="numeric"><spring:message code="common.id"/></th>
							<th data-column-id="code" data-type="numeric"><spring:message code="common.code"/></th>
							<th data-column-id="description" data-header-css-class="descriptionColumn"><spring:message code="common.description"/></th>
							<th data-column-id="isActive"><spring:message code="common.active"/></th>
							<th data-column-id="commands" data-header-css-class="commandsColumn" data-formatter="commands" data-sortable="false"><spring:message code="administration.commands.tableLabel"/></th>
						</tr>
					</thead>
				</table>
			</div>

			<div class="tab-pane fade" id="productMonodrug">
				<div class="row">
					<div class="col-md-4">
						<button class="btn btn-primary btn-block" id="addProductMonodrug"><span class="glyphicon glyphicon-plus"></span> <spring:message code="common.add.entity"/></button>
					</div>
				</div>
				
				<br>
				<table id="productMonodrugsTable" class="table table-condensed table-hover table-striped">				
					<thead>
						<tr>
							<th data-column-id="id" data-header-css-class="idColumn" data-type="numeric"><spring:message code="common.id"/></th>
							<th data-column-id="code" data-type="numeric"><spring:message code="common.code"/></th>
							<th data-column-id="description" data-header-css-class="descriptionColumn"><spring:message code="common.description"/></th>
							<th data-column-id="isActive"><spring:message code="common.active"/></th>
							<th data-column-id="commands" data-header-css-class="commandsColumn" data-formatter="commands" data-sortable="false"><spring:message code="administration.commands.tableLabel"/></th>
						</tr>
					</thead>
				</table>
			</div>

			<div class="tab-pane fade" id="productDrugCategory">
				<div class="row">
					<div class="col-md-4">
						<button class="btn btn-primary btn-block" id="addProductDrugCategory"><span class="glyphicon glyphicon-plus"></span> <spring:message code="common.add.entity"/></button>
					</div>
				</div>
				
				<br>
				<table id="productDrugCategoriesTable" class="table table-condensed table-hover table-striped">				
					<thead>
						<tr>
							<th data-column-id="id" data-header-css-class="idColumn" data-type="numeric"><spring:message code="common.id"/></th>
							<th data-column-id="code" data-type="numeric"><spring:message code="common.code"/></th>
							<th data-column-id="description" data-header-css-class="descriptionColumn"><spring:message code="common.description"/></th>
							<th data-column-id="isActive"><spring:message code="common.active"/></th>
							<th data-column-id="commands" data-header-css-class="commandsColumn" data-formatter="commands" data-sortable="false"><spring:message code="administration.commands.tableLabel"/></th>
						</tr>
					</thead>
				</table>
			</div>
			
			<div class="tab-pane fade" id="productGroup">
				<div class="row">
					<div class="col-md-4">
						<button class="btn btn-primary btn-block" id="addProductGroup"><span class="glyphicon glyphicon-plus"></span> <spring:message code="common.add.entity"/></button>
					</div>
				</div>
				
				<br>
				<table id="productGroupsTable" class="table table-condensed table-hover table-striped">				
					<thead>
						<tr>
							<th data-column-id="id" data-header-css-class="idColumn" data-type="numeric"><spring:message code="common.id"/></th>
							<th data-column-id="code" data-type="numeric"><spring:message code="common.code"/></th>
							<th data-column-id="description" data-header-css-class="descriptionColumn"><spring:message code="common.description"/></th>
							<th data-column-id="isActive"><spring:message code="common.active"/></th>
							<th data-column-id="commands" data-header-css-class="commandsColumn" data-formatter="commands" data-sortable="false"><spring:message code="administration.commands.tableLabel"/></th>
						</tr>
					</thead>
				</table>
			</div>
			
		</div>
	</div>
</div>

<%-- Confirmación de que se borrará definitivamente --%>
<div class="modal fade" data-backdrop="static" id="deleteConfirmationModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog" style="width:250px">
		<div class="modal-content">
			<div class="modal-body">
				<strong><span style="color:red"><spring:message code="administration.entity.delete"/></span></strong>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal"><spring:message code="common.no"/></button>
				<button type="button" class="btn btn-primary" data-dismiss="modal" id="deleteEntityButton"><spring:message code="common.yes"/></button>
			</div>
		</div>
	</div>
</div>

<%-- Modal de Lectura/Modificacion Producto --%>
<div class="modal fade" data-backdrop="static" id="productModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog" style="width:1000px">
		<div class="modal-content">
			<div class="modal-body">
				<div class="row">
					<div class="col-md-9 form-group">
						<h2 id="addProductLabel" style="display: none;"><spring:message code="common.product"/></h2>
						<h2 id="readProductLabel" style="display: none;"><spring:message code="administration.readProduct"/></h2>
						<h2 id="updateProductLabel" style="display: none;"><spring:message code="administration.updateProduct"/></h2>
						<input type="hidden" class="form-control" id="productIdInput">
					</div>
				</div>
				<form id="productAdministrationForm" action="" onsubmit="return false;">
					<div class="row">
						<div class="col-md-6 form-group">
							<label for="productCodeInput"><spring:message code="common.code" /></label>
							<input type="text" class="form-control" id="productCodeInput" name="productCode">
						</div>
						<div class="col-md-4 form-group">
							<label for="gtinInput"><spring:message code="common.gtin" /></label>
							<input type="text" class="form-control" id="gtinInput" name="gtin" disabled>
						</div>
						<div class="col-md-2 form-group">
							<label></label>
							<button class="btn btn-default" id="editGtinsButton" data-toggle="modal" data-target="#productGtinsModal" disabled="disabled"><span class="glyphicon glyphicon-edit"></span> <spring:message code="administration.editGtins"/></button>
						</div>
					</div>

					<div class="row">
						<div class="col-md-12 form-group">
							<label for="productDescriptionInput"><spring:message code="common.description" /></label>
							<input type="text" class="form-control" id="productDescriptionInput" name="productDescription">
						</div>
					</div>

					<div class="row">
						<div class="col-md-6 form-group">
							<label for="brandSelect"><spring:message code="common.brand" /></label>
							<select class="form-control chosen-select" id="brandSelect" name="brand">
								<option value="">-<spring:message code="common.select.option" /> -</option>
								<c:forEach items="${brands}" var="brand" varStatus="status">
									<option value="${brand.id}"><c:out value="${brand.code}">
									</c:out> -<c:out value="${brand.description}"></c:out></option>
								</c:forEach>
							</select>
						</div>
						<div class="col-md-6 form-group">
							<label for="monodrugSelect"><spring:message code="common.monodrug" /></label>
							<select class="form-control chosen-select" id="monodrugSelect" name="monodrug">
								<option value="">-<spring:message code="common.select.option" /> -</option>
								<c:forEach items="${monodrugs}" var="monodrug" varStatus="status">
									<option value="${monodrug.id}"><c:out value="${monodrug.code}"></c:out> -
										<c:out value="${monodrug.description}"></c:out></option>
								</c:forEach>
							</select>
						</div>
					</div>

					<div class="row">
						<div class="col-md-6 form-group">
							<label for="productGroupSelect"><spring:message code="common.productGroup" /></label> <select class="form-control chosen-select" id="productGroupSelect" name="productGroup">
								<option value="">-
									<spring:message code="common.select.option" /> -
								</option>
								<c:forEach items="${productGroups}" var="productGroup" varStatus="status">
									<option value="${productGroup.id}"><c:out value="${productGroup.code}"></c:out> -
										<c:out value="${productGroup.description}"></c:out></option>
								</c:forEach>
							</select>
						</div>
						<div class="col-md-6 form-group">
							<label for="drugCategorySelect"><spring:message code="common.drugCategory" /></label> <select class="form-control chosen-select" id="drugCategorySelect" name="drugCategory">
								<option value="">-
									<spring:message code="common.select.option" /> -
								</option>
								<c:forEach items="${drugCategories}" var="drugCategory" varStatus="status">
									<option value="${drugCategory.id}"><c:out value="${drugCategory.code}"></c:out> -
										<c:out value="${drugCategory.description}"></c:out></option>
								</c:forEach>
							</select>
						</div>
					</div>

					<div class="row">
						<div class="col-md-6 form-group">
							<label for="typeSelect"><spring:message code="common.type" /></label>
							<select class="form-control chosen-select" id="typeSelect" name="type">
								<option value="BE"><spring:message code="administration.productType.batchExpiration" /></option>
								<option value="PS"><spring:message code="administration.productType.providerSerialized" /></option>
								<option value="SS"><spring:message code="administration.productType.selfSerialized" /></option>
							</select>
						</div>
						<div class="col-md-6 form-group">
							<label for="productActiveSelect"><spring:message code="common.active" /></label>
							<select class="form-control chosen-select" id="productActiveSelect" name="productActive">
								<option value="true"><spring:message code="common.yes" /></option>
								<option value="false"><spring:message code="common.no" /></option>
							</select>
						</div>
					</div>

					<div class="row">
						<div class="col-md-4 form-group">
							<label for="priceInput"><spring:message code="common.price" /></label>
							<input type="text" class="form-control" id="priceInput" name="price" disabled>
						</div>
						<div class="col-md-2 form-group">
							<label></label>
							<button class="btn btn-default" id="editPricesButton" disabled="disabled"><span class="glyphicon glyphicon-edit"></span> <spring:message code="administration.editPrices"/></button>
						</div>
						<div class="col-md-3 form-group">
							<label for="informAnmatSelect"><spring:message code="common.informAnmat" /></label>
							<select class="form-control chosen-select" id="informAnmatSelect" name="informAnmat">
								<option value="true"><spring:message code="common.yes" /></option>
								<option value="false"><spring:message code="common.no" /></option>
							</select>
						</div>
						<div class="col-md-3 form-group">
							<label for="coldSelect"><spring:message code="common.cold" /></label>
							<select class="form-control chosen-select" id="coldSelect" name="cold">
								<option value="false"><spring:message code="common.no" /></option>
								<option value="true"><spring:message code="common.yes" /></option>
							</select>
						</div>
					</div>
				</form>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal"><spring:message code="common.back"/></button>
				<button class="btn btn-success" id="addProductButton" style="display: none;"><span class="glyphicon glyphicon-ok"></span> <spring:message code="common.add.entity"/></button>
				<button class="btn btn-success" id="updateProductButton" style="display: none;"><span class="glyphicon glyphicon-ok"></span> <spring:message code="common.confirm"/></button>
			</div>
		</div>
	</div>
</div>

<%-- Modal de Edicion de GTINs --%>
<div class="modal fade" data-backdrop="static" id="productGtinsModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog" style="width:1000px">
		<div class="modal-content">
			<div class="modal-header">
        		<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        		<h4 class="modal-title">Edicion de GTINs</h4>
      		</div>
			<div class="modal-body">
			<div class="container-fluid">
				<form class="form-inline">
				<div class="form-group">
    				<label for="currentGtinInput">GTIN actual: </label>
    				<input type="text" class="form-control" id="currentGtinInput" name="currentGtin" disabled>
  				</div>
  				<br><br>
  				<div class="form-inline">
					<div class="form-group">
    				<label for="addGtinInput">GTIN nuevo: </label>
    				<input type="text" class="form-control" id="addGtinInput" name="addGtin">
    				<button type="button" class="form-control btn btn-primary" id="addGtinButton"><span class="glyphicon glyphicon-plus"></span> Agregar</button>
  				</div>
				</div>
				</form>
				<br>
				<table id="productGtinsTable" class="table table-condensed table-hover table-striped">				
					<thead>
						<tr>
							<th data-identifier="true" data-column-id="number" data-type="numeric"><spring:message code="common.gtin"/></th>
							<th data-column-id="date"><spring:message code="common.date"/></th>
							<th data-column-id="commands" data-formatter="commands" data-sortable="false"><spring:message code="administration.commands.tableLabel"/></th>
						</tr>
					</thead>
					<tbody id="productGtinsTableBody">
					</tbody>
				</table>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal"><spring:message code="common.back"/></button>
				<button class="btn btn-success" id="addProductBrandButton" style="display: none;"><span class="glyphicon glyphicon-ok"></span> <spring:message code="common.add.entity"/></button>
				<button class="btn btn-success" id="updateProductBrandButton" style="display: none;"><span class="glyphicon glyphicon-ok"></span> <spring:message code="common.confirm"/></button>
			</div>
		</div>
		</div>
	</div>
</div>

<%-- Modal de Lectura/Modificacion Marca de Producto --%>
<div class="modal fade" data-backdrop="static" id="productBrandModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog" style="width:1000px">
		<div class="modal-content">
			<div class="modal-body">
				<div class="row">
					<div class="col-md-9 form-group">
						<h2 id="addProductBrandLabel" style="display: none;"><spring:message code="common.brand"/></h2>
						<h2 id="readProductBrandLabel" style="display: none;"><spring:message code="administration.readProductBrand"/></h2>
						<h2 id="updateProductBrandLabel" style="display: none;"><spring:message code="administration.updateProductBrand"/></h2>
						<input type="hidden" class="form-control" id="productBrandIdInput">
					</div>
				</div>
				<form id="productBrandAdministrationForm" action="" onsubmit="return false;">
					<div class="row">
						<div class="col-md-4 form-group">
							<label for="productBrandCodeInput"><spring:message code="common.code" /></label>
							<input type="text" class="form-control" id="productBrandCodeInput" name="productBrandCode">
						</div>
						<div class="col-md-4 form-group">
							<label for="productBrandDescriptionInput"><spring:message code="common.description" /></label>
							<input type="text" class="form-control" id="productBrandDescriptionInput" name="productBrandDescription">
						</div>
						<div class="col-md-4 form-group">
							<label for="productBrandActiveSelect"><spring:message code="common.active" /></label>
							<select class="form-control chosen-select" id="productBrandActiveSelect" name="productBrandActive">
								<option value="true"><spring:message code="common.yes" /></option>
								<option value="false"><spring:message code="common.no" /></option>
							</select>
						</div>
					</div>
				</form>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal"><spring:message code="common.back"/></button>
				<button class="btn btn-success" id="addProductBrandButton" style="display: none;"><span class="glyphicon glyphicon-ok"></span> <spring:message code="common.add.entity"/></button>
				<button class="btn btn-success" id="updateProductBrandButton" style="display: none;"><span class="glyphicon glyphicon-ok"></span> <spring:message code="common.confirm"/></button>
			</div>
		</div>
	</div>
</div>

<%-- Modal de Lectura/Modificacion Monodroga de Producto --%>
<div class="modal fade" data-backdrop="static" id="productMonodrugModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog" style="width:1000px">
		<div class="modal-content">
			<div class="modal-body">
				<div class="row">
					<div class="col-md-9 form-group">
						<h2 id="addProductMonodrugLabel" style="display: none;"><spring:message code="common.monodrug"/></h2>
						<h2 id="readProductMonodrugLabel" style="display: none;"><spring:message code="administration.readProductMonodrug"/></h2>
						<h2 id="updateProductMonodrugLabel" style="display: none;"><spring:message code="administration.updateProductMonodrug"/></h2>
						<input type="hidden" class="form-control" id="productMonodrugIdInput">
					</div>
				</div>
				<form id="productMonodrugAdministrationForm" action="" onsubmit="return false;">
					<div class="row">
						<div class="col-md-4 form-group">
							<label for="productMonodrugCodeInput"><spring:message code="common.code" /></label>
							<input type="text" class="form-control" id="productMonodrugCodeInput" name="productMonodrugCode">
						</div>
						<div class="col-md-4 form-group">
							<label for="productMonodrugDescriptionInput"><spring:message code="common.description" /></label>
							<input type="text" class="form-control" id="productMonodrugDescriptionInput" name="productMonodrugDescription">
						</div>
						<div class="col-md-4 form-group">
							<label for="productMonodrugActiveSelect"><spring:message code="common.active" /></label>
							<select class="form-control chosen-select" id="productMonodrugActiveSelect" name="productMonodrugActive">
								<option value="true"><spring:message code="common.yes" /></option>
								<option value="false"><spring:message code="common.no" /></option>
							</select>
						</div>
					</div>
				</form>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal"><spring:message code="common.back"/></button>
				<button class="btn btn-success" id="addProductMonodrugButton" style="display: none;"><span class="glyphicon glyphicon-ok"></span> <spring:message code="common.add.entity"/></button>
				<button class="btn btn-success" id="updateProductMonodrugButton" style="display: none;"><span class="glyphicon glyphicon-ok"></span> <spring:message code="common.confirm"/></button>
			</div>
		</div>
	</div>
</div>

<%-- Modal de Lectura/Modificacion Categoria de Producto --%>
<div class="modal fade" data-backdrop="static" id="productDrugCategoryModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog" style="width:1000px">
		<div class="modal-content">
			<div class="modal-body">
				<div class="row">
					<div class="col-md-9 form-group">
						<h2 id="addProductDrugCategoryLabel" style="display: none;"><spring:message code="common.drugCategory"/></h2>
						<h2 id="readProductDrugCategoryLabel" style="display: none;"><spring:message code="administration.readProductDrugCategory"/></h2>
						<h2 id="updateProductDrugCategoryLabel" style="display: none;"><spring:message code="administration.updateProductDrugCategory"/></h2>
						<input type="hidden" class="form-control" id="productDrugCategoryIdInput">
					</div>
				</div>
				<form id="productDrugCategoryAdministrationForm" action="" onsubmit="return false;">
					<div class="row">
						<div class="col-md-4 form-group">
							<label for="productDrugCategoryCodeInput"><spring:message code="common.code" /></label>
							<input type="text" class="form-control" id="productDrugCategoryCodeInput" name="productDrugCategoryCode">
						</div>
						<div class="col-md-4 form-group">
							<label for="productDrugCategoryDescriptionInput"><spring:message code="common.description" /></label>
							<input type="text" class="form-control" id="productDrugCategoryDescriptionInput" name="productDrugCategoryDescription">
						</div>
						<div class="col-md-4 form-group">
							<label for="productDrugCategoryActiveSelect"><spring:message code="common.active" /></label>
							<select class="form-control chosen-select" id="productDrugCategoryActiveSelect" name="productDrugCategoryActive">
								<option value="true"><spring:message code="common.yes" /></option>
								<option value="false"><spring:message code="common.no" /></option>
							</select>
						</div>
					</div>
				</form>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal"><spring:message code="common.back"/></button>
				<button class="btn btn-success" id="addProductDrugCategoryButton" style="display: none;"><span class="glyphicon glyphicon-ok"></span> <spring:message code="common.add.entity"/></button>
				<button class="btn btn-success" id="updateProductDrugCategoryButton" style="display: none;"><span class="glyphicon glyphicon-ok"></span> <spring:message code="common.confirm"/></button>
			</div>
		</div>
	</div>
</div>

<%-- Modal de Lectura/Modificacion Grupo de Producto --%>
<div class="modal fade" data-backdrop="static" id="productGroupModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog" style="width:1000px">
		<div class="modal-content">
			<div class="modal-body">
				<div class="row">
					<div class="col-md-9 form-group">
						<h2 id="addProductGroupLabel" style="display: none;"><spring:message code="common.productGroup"/></h2>
						<h2 id="readProductGroupLabel" style="display: none;"><spring:message code="administration.readProductGroup"/></h2>
						<h2 id="updateProductGroupLabel" style="display: none;"><spring:message code="administration.updateProductGroup"/></h2>
						<input type="hidden" class="form-control" id="productGroupIdInput">
					</div>
				</div>
				<form id="productGroupAdministrationForm" action="" onsubmit="return false;">
					<div class="row">
						<div class="col-md-4 form-group">
							<label for="productGroupCodeInput"><spring:message code="common.code" /></label>
							<input type="text" class="form-control" id="productGroupCodeInput" name="productGroupCode">
						</div>
						<div class="col-md-4 form-group">
							<label for="productGroupDescriptionInput"><spring:message code="common.description" /></label>
							<input type="text" class="form-control" id="productGroupDescriptionInput" name="productGroupDescription">
						</div>
						<div class="col-md-4 form-group">
							<label for="productGroupActiveSelect"><spring:message code="common.active" /></label>
							<select class="form-control chosen-select" id="productGroupActiveSelect" name="productGroupActive">
								<option value="true"><spring:message code="common.yes" /></option>
								<option value="false"><spring:message code="common.no" /></option>
							</select>
						</div>
					</div>
				</form>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal"><spring:message code="common.back"/></button>
				<button class="btn btn-success" id="addProductGroupButton" style="display: none;"><span class="glyphicon glyphicon-ok"></span> <spring:message code="common.add.entity"/></button>
				<button class="btn btn-success" id="updateProductGroupButton" style="display: none;"><span class="glyphicon glyphicon-ok"></span> <spring:message code="common.confirm"/></button>
			</div>
		</div>
	</div>
</div>