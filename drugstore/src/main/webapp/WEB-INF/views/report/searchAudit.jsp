<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<script type="text/javascript" src="js/form/report/searchAudit.js"></script>
<script type="text/javascript">
	$(document).ready(function() {
		new SearchAudit();
	});
</script>

<form id="searchAuditForm" action="" onsubmit="return false;">
	<div class="row">
		<div class="col-md-9 form-group">
			<h3><spring:message code="common.title.searchAudit"/></h3>
		</div>
	</div>
	<div>
		<div class="row">
			<div class="col-md-12 form-group">
				<label for="roleSearch"><spring:message code="common.role"/></label>
				<select id="roleSearch" name="roleSearch" class="form-control chosen-select" data-placeholder="<spring:message code='common.select.option'/>">
					<option value=""></option>
					<c:forEach items="${roles}" var="role">
						<option value="${role.id}"><c:out value="${role.description}"></c:out></option>
					</c:forEach>
				</select>
			</div>
		</div>
		
		<div class="row">
			<div class="col-md-4 form-group">
				<label for="operationNumberSearch"><spring:message code="common.operationNumber"/></label>
				<input type="text" class="form-control" name="operationNumberSearch" id="operationNumberSearch" >
			</div>
			<div class="col-md-4 form-group">
				<label for="dateFromSearch"><spring:message code="common.dateFrom"/></label>
				<div class="input-group">
					<input type="text" class="form-control" name="dateFromSearch" id="dateFromSearch">
					<span class="input-group-addon" id="dateFromButton" style="cursor:pointer;">
						<span class="glyphicon glyphicon-calendar"></span>
					</span>
				</div>
			</div>
			<div class="col-md-4 form-group">
				<label for="dateToSearch"><spring:message code="common.dateTo"/></label>
				<div class="input-group">
					<input type="text" class="form-control" name="dateToSearch" id="dateToSearch">
					<span class="input-group-addon" id="dateToButton" style="cursor:pointer;">
						<span class="glyphicon glyphicon-calendar"></span>
					</span>
				</div>
			</div>
		</div>
		
		<div class="row">
			<div class="col-md-4 form-group">
				<label for="userSearch"><spring:message code="common.user"/></label>
				<select id="userSearch" name="userSearch" class="form-control chosen-select" data-placeholder="<spring:message code='common.select.option'/>">
					<option value=""></option>
					<c:forEach items="${users}" var="user">
						<option value="${user.id}"><c:out value="${user.name}"></c:out></option>
					</c:forEach>
				</select>
			</div>
			<div class="col-md-4 form-group">
				<label for="auditActionSearch"><spring:message code="common.action"/></label>
				<select id="auditActionSearch" name="auditActionSearch" class="form-control chosen-select" data-placeholder="<spring:message code='common.select.option'/>">
					<option value=""></option>
					<c:forEach items="${auditActions}" var="auditAction">
						<option value="${auditAction.id}"><c:out value="${auditAction.description}"></c:out></option>
					</c:forEach>
				</select>
			</div>

			<div class="col-md-2 form-margin">
				<button class="btn btn-success btn-block" type="submit" id="searchButton">
				<span class="glyphicon glyphicon-search"></span>
				<spring:message code="common.search" />
				</button>
			</div>
			<div class="col-md-2 form-margin">
				<button class="btn btn-info btn-block" type="submit" id="cleanButton">
				<span class="glyphicon glyphicon-trash"></span>
				<spring:message code="common.clean" />
				</button>
			</div>
		</div>
	</div>
	
	<br>
<div>
	<div id="divTable">
		<table class="table table-condensed table-hover table-striped" id="auditTable">
			<thead>
		        <tr>
		            <th data-column-id="date"><spring:message code="common.date"/></th>
		            <th data-column-id="role"><spring:message code="common.role"/></th>
		            <th data-column-id="operationNumber" data-type="numeric"><spring:message code="common.operationNumber"/></th>
		            <th data-column-id="action"><spring:message code="common.action"/></th>
		            <th data-column-id="user"><spring:message code="common.user"/></th>
		        </tr>
	   	 	</thead>
	   	 	<tbody id="auditTableBody">
			</tbody>
		</table>
	</div>
	</div>
</form>
