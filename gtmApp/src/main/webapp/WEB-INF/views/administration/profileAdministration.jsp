<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<script type="text/javascript" src="js/form/administration/profileAdministration.js" ></script>
<script type="text/javascript" src="js/form/administration/save/saveProfile.js" ></script>
<script type="text/javascript">
  $(document).ready(function() {
    new SaveProfile();
  });
</script>

<div class="row">
  <div class="col-md-9 col-lg-9 form-group">
    <h3>
      <spring:message code="administration.profileAdministration" />
    </h3>
  </div>
</div>

<div class="row">
  <div class="col-md-4 col-lg-4">
    <button class="btn btn-primary btn-block" id="addProfile">
      <span class="glyphicon glyphicon-plus"></span>
      <spring:message code="common.add.entity" />
    </button>
  </div>
</div>

<br>
<table id="profilesTable" class="table table-condensed table-hover table-striped">
  <thead>
  <tr>
    <th data-column-id="id" data-type="numeric"><spring:message code="common.id" /></th>
    <th data-column-id="description"><spring:message code="common.description" /></th>
    <th data-column-id="commands" data-formatter="commands" data-sortable="false"><spring:message code="administration.commands.tableLabel"/></th>
  </tr>
  </thead>
</table>

<%-- Confirmaci�n de que se borrar� definitivamente --%>
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

<%-- Modal de Lectura/Modificacion --%>
<div class="modal fade" data-backdrop="static" id="profileModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
  <div class="modal-dialog" style="width:70%">
    <div class="modal-content">
      <div class="modal-body">
          <div class="row">
            <div class="col-md-9 col-lg-9 form-group">
              <h2 id="addProfileLabel" style="display: none;"><spring:message code="common.profile"/></h2>
              <h2 id="readProfileLabel" style="display: none;"><spring:message code="administration.readProfile"/></h2>
              <h2 id="updateProfileLabel" style="display: none;"><spring:message code="administration.updateProfile"/></h2>
              <input type="hidden" class="form-control" id="idInput">
            </div>
          </div>

          <form id="profileAdministrationForm" action="" onsubmit="return false;">
            <div class="row">
              <div class="col-md-12 col-lg-12 form-group">
                <label for="descriptionInput"><spring:message code="common.description" /></label>
                <input type="text" class="form-control" id="descriptionInput" name="description">
              </div>
            </div>
            <div class="row">
              <div class="col-md-4 col-lg-4 form-group">
                <label for="role"><spring:message code="common.roles" /></label>
              </div>
              <div class="col-md-12 col-lg-12 form-group">
                <div class="ms-container">
                  <select multiple="multiple" id="my-select" name="my-select[]">
                    <c:forEach items="${roles}" var="role" varStatus="status">
                      <option value="${role.id}"><c:out value="${role.id} - ${role.description}"></c:out></option>
                    </c:forEach>
                  </select>
                </div>
              </div>
            </div>

          </form>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-default" data-dismiss="modal"><spring:message code="common.back"/></button>
        <button class="btn btn-success" id="addButton" style="display: none;"><span class="glyphicon glyphicon-ok"></span> <spring:message code="common.add.entity"/></button>
        <button class="btn btn-success" id="updateButton" style="display: none;"><span class="glyphicon glyphicon-ok"></span> <spring:message code="common.confirm"/></button>
      </div>
    </div>
  </div>
</div>