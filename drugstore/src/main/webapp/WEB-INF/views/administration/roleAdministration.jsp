<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<script type="text/javascript" src="js/form/administration/roleAdministration.js" /></script>

<div class="row">
	<div class="col-md-9 form-group">
		<h2><spring:message code="administration.roleAdministration"/></h2>
	</div>
</div>
	<div class="bs-example bs-example-tabs">
	<ul id="myTab" class="nav nav-tabs">
		<li class="active"><a href="#name" data-toggle="tab"><spring:message code="common.entity.name"/></a></li>
	</ul>
	<br>
	<div id="myTabContent" class="tab-content">
		<div class="tab-pane fade in active" id="name">
			<div class="row">
				<div class="col-md-12 form-group">
					<input id="roleAdm" type="search" placeholder='<spring:message code="common.affiliate"/>' class="form-control" name="affiliateAdm" autosave="" results="5" incremental="incremental" />
				</div>
			</div>
			<div class="row">
				<div class="col-md-4">
					<button class="btn btn-primary btn-block" id="addRole"><span class="glyphicon glyphicon-plus"></span> <spring:message code="common.add.entity"/></button>
				</div>
				<div class="col-md-4">
					<button class="btn btn-warning btn-block" id="updateRole" disabled><span class="glyphicon glyphicon-pencil"></span> <spring:message code="common.modify.delete.entity"/></button>
				</div>
				<div class="col-md-4">
					<button class="btn btn-info btn-block" id="readRole" disabled><span class="glyphicon glyphicon-eye-open"></span> <spring:message code="common.view"/></button>
				</div>
			</div>
		</div>
	</div>
</div>