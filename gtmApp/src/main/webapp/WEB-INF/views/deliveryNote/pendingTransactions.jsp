<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<script type="text/javascript" src="js/form/deliveryNote/pendingTransactions.js"></script>
<script type="text/javascript" src="js/form/modals.js"></script>
<script type="text/javascript">
	$(document).ready(function() {
		new PendingTransactions();
	});
</script>

<jsp:include page="../modals/modals.jsp" />
<jsp:include page="../modals/outputModal.jsp" />
<jsp:include page="../modals/orderModal.jsp" />
<jsp:include page="../modals/supplyingModal.jsp" />

<form action="" onsubmit="return false;">
	<div class="row">
		<div class="col-md-12 col-lg-12 form-group">
			<h3><spring:message code="common.pendingTransactions.label"/></h3>
		</div>
	</div>
	<div>
		<table class="table table-condensed table-hover table-striped" id="deliveryNoteTable">
			<thead>
		        <tr>
		           	<th data-column-id="id" data-identifier="true"><spring:message code="common.id"/></th>
           			<th data-column-id="orderAssemblyOrOutputNumber"><spring:message code="deliveryNote.orderAssemblyOrOutputNumber"/></th>
           			<th data-column-id="class"><spring:message code="deliveryNote.class"/></th>
					<th data-column-id="date" data-type="datetime"><spring:message code="common.date"/></th>
           			<th data-column-id="deliveryNoteNumbers"><spring:message code="deliveryNote.deliveryNoteNumbers"/></th>
					<th data-column-id="option" data-formatter="option" data-sortable="false"><spring:message code="common.option"/></th>
		        </tr>
	   	 	</thead>
	   	 	<tbody id="deliveryNoteTableBody">
				<c:forEach items="${orderDeliveryNotes}" var="orderDeliveryNotes">
					<tr>
						<c:set var="key" value="${orderDeliveryNotes.key}"/>
						<td><c:out value="${orderDeliveryNotes.key}"/></td>
						<td><c:out value="${fn:substring(key, 1, -1)}"/></td>
						<td><spring:message code="deliveryNote.class.orderAssembly"/></td>
						<%--<td><fmt:formatDate value="${orderDeliveryNotes.value[0].date}" pattern="dd/MM/yyyy HH:mm:ss a"/></td>--%>
						<td><c:out value="${orderDeliveryNotes.value[0].date}"/></td>
						<td>
							<c:forEach items="${orderDeliveryNotes.value}" var="deliveryNote" varStatus="status">
								<c:if test="${status.first}">[</c:if>
								<c:out value="${deliveryNote.number}"/>
								<c:if test="${!status.last}">, </c:if>
								<c:if test="${status.last}">]</c:if>
							</c:forEach>
						</td>
						<td><spring:message code="common.view"/></td>
					</tr>
				</c:forEach>
	
				<c:forEach items="${outputDeliveryNotes}" var="outputDeliveryNotes">
					<tr>
						<c:set var="key" value="${outputDeliveryNotes.key}"/>
						<td><c:out value="${outputDeliveryNotes.key}"/></td>
						<td><c:out value="${fn:substring(key, 1, -1)}"/></td>
						<td><spring:message code="deliveryNote.class.output"/></td>
                        <%--<td><fmt:formatDate value="${outputDeliveryNotes.value[0].date}" pattern="dd/MM/yyyy HH:mm:ss a"/></td>--%>
						<td><c:out value="${outputDeliveryNotes.value[0].date}"/></td>
						<td>
							<c:forEach items="${outputDeliveryNotes.value}" var="deliveryNote" varStatus="status">
								<c:if test="${status.first}">[</c:if>
								<c:out value="${deliveryNote.number}"/>
								<c:if test="${!status.last}">, </c:if>
								<c:if test="${status.last}">]</c:if>
							</c:forEach>
						</td>
						<td><spring:message code="common.view"/></td>
					</tr>
				</c:forEach>
				<c:forEach items="${supplyingDeliveryNotes}" var="supplyingDeliveryNotes">
					<tr>
						<c:set var="key" value="${supplyingDeliveryNotes.key}"/>
						<td><c:out value="${supplyingDeliveryNotes.key}"/></td>
						<td><c:out value="${fn:substring(key, 1, -1)}"/></td>
						<td><spring:message code="deliveryNote.class.supplying"/></td>
                        <%--<td><fmt:formatDate value="${supplyingDeliveryNotes.value[0].date}" pattern="dd/MM/yyyy HH:mm:ss a"/></td>--%>
						<td><c:out value="${supplyingDeliveryNotes.value[0].date}"/></td>
						<td>
							<c:forEach items="${supplyingDeliveryNotes.value}" var="deliveryNote" varStatus="status">
								<c:if test="${status.first}">[</c:if>
								<c:out value="${deliveryNote.number}"/>
								<c:if test="${!status.last}">, </c:if>
								<c:if test="${status.last}">]</c:if>
							</c:forEach>
						</td>
						<td><spring:message code="common.view"/></td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>
<br>
<div class="row">
	<div class="col-md-3 col-lg-2 col-md-offset-6 col-lg-offset-8">
		<button class="btn btn-info btn-block" type="submit" id="confirmDeliveryNotesWithoutInform">
			<span class="glyphicon glyphicon-log-in"></span>
			<spring:message code="common.updatedManually" />
		</button>
	</div>
	<div class="col-md-3 col-lg-2">
		<button class="btn btn-primary btn-block" type="submit" id="confirmDeliveryNotesButton">
			<span class="glyphicon glyphicon-ok"></span>
			<spring:message code="common.confirm" />
		</button>
	</div>
</div>
</form>

<div class="modal fade" data-backdrop="static" id="forcedDeliveryNoteConfirmationModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog" style="width:50%">
		<div class="modal-content">
			<div class="modal-body">
				<div class="row">
					<div class="col-md-12 col-lg-12 form-group">
						<strong><span style="color:red"><spring:message code="common.informedManuallyModal"/></span></strong>
					</div>
				</div>
				<div>
					<table class="table table-condensed table-hover table-striped" id="deliveryNotesAlreadyInformsTable">
						<thead>
							<tr>
								<th data-column-id="deliveryNoteAlreadyInformId" data-identifier="true"><spring:message code="common.number"/></th>
								<th data-column-id="deliveryNoteAlreadyInformTransactionCode"><spring:message code="common.transactionCodeDescription"/></th>
							</tr>
						</thead>
						<tbody id="deliveryNotesAlreadyInformsBody">
						</tbody>
					</table>
				</div>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal"><spring:message code="common.no"/></button>
				<button type="button" class="btn btn-primary" data-dismiss="modal" id="confirmDeliveryNotesWithoutInformModal"><spring:message code="common.yes"/></button>
			</div>
		</div>
	</div>
</div>

