<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<script type="text/javascript" src="js/form/input/informForcedInputs.js"></script>
<script type="text/javascript" src="js/form/modals.js"></script>
<div class="row">
    <div class="col-md-9 form-group">
        <h3><spring:message code="administration.informForcedInputs"/></h3>
    </div>
</div>

<script type="text/javascript">
    $(document).ready(function() {
        new PendingInputs();
    });
</script>

<jsp:include page="../modals/modals.jsp" />
<jsp:include page="../modals/inputModal.jsp" />

<div id="divTable">
    <table class="table table-condensed table-hover table-striped" id="inputTable">
        <thead>
        <tr>
            <th data-column-id="input" data-identifier="true"><spring:message code="common.id"/></th>
            <th data-column-id="concept"><spring:message code="common.concept"/></th>
            <th data-column-id="agreement"><spring:message code="common.agreement"/></th>
            <th data-column-id="option" data-formatter="option" data-sortable="false"><spring:message code="common.option"/></th>
        </tr>
        </thead>
        <tbody id="inputTableBody">
        <c:forEach items="${inputs}" var="input">
            <tr>
                <td><c:out value="${input.id}"></c:out></td>
                <td><c:out value="${input.concept.description}"></c:out></td>
                <td><c:out value="${input.agreement.description}"></c:out></td>
                <td><spring:message code="common.view"/></td>
            </tr>
        </c:forEach>
        </tbody>
    </table>

    <div class="row">
        <div class="col-md-2 col-md-offset-8">
            <button class="btn btn-warning" id="forcedInput"><span class="glyphicon glyphicon glyphicon-saved"></span> <spring:message code="input.closeInputDefinitely.button"/></button>
        </div>
        <div class="col-md-2">
            <button type="submit" class="btn btn-success btn-block" id="confirmButton"><span class="glyphicon glyphicon-ok"></span> <spring:message code="common.authorize"/></button>
        </div>
    </div>
</div>


<%-- Confirmaci�n del delete de una row --%>
<div class="modal fade" data-backdrop="static" id="forcedInputConfirmationModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog" style="width:400px">
        <div class="modal-content">
            <div class="modal-body">
                <strong><span style="color:red"><spring:message code="input.forcedInputConfirmationModal.text"/></span></strong>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal"><spring:message code="common.no"/></button>
                <button type="button" class="btn btn-primary" data-dismiss="modal" id="authorizeWithoutInform"><spring:message code="common.yes"/></button>
            </div>
        </div>
    </div>
</div>