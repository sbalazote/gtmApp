<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<script type="text/javascript" src="js/form/administration/deliveryNoteEnumeratorAdministration.js" ></script>
<script type="text/javascript" src="js/form/administration/save/saveDeliveryNoteEnumerator.js" ></script>
<script type="text/javascript">
  $(document).ready(function() {
    new SaveDeliveryNoteEnumerator();
  });
</script>

<div class="row">
  <div class="col-md-9 form-group">
    <h3>
      <spring:message code="administration.deliveryNoteEnumeratorAdministration" />
    </h3>
  </div>
</div>

<div class="row">
  <div class="col-md-4">
    <button class="btn btn-primary btn-block" id="addAgent">
      <span class="glyphicon glyphicon-plus"></span>
      <spring:message code="common.add.entity" />
    </button>
  </div>
</div>

<br>
<table id="deliveryNoteEnumeratorsTable" class="table table-condensed table-hover table-striped">
  <thead>
  <tr>
    <th data-column-id="id" data-type="numeric"><spring:message code="common.id" /></th>
    <th data-column-id="deliveryNotePOS" data-type="numeric"><spring:message code="common.deliveryNote.POS" /></th>
    <th data-column-id="lastDeliveryNoteNumber"><spring:message code="common.lastDeliveryNoteNumber" /></th>
    <th data-column-id="isActive"><spring:message code="common.active" /></th>
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
<div class="modal fade" data-backdrop="static" id="deliveryNoteEnumeratorModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
  <div class="modal-dialog" style="width:1000px">
    <div class="modal-content">
      <div class="modal-body">
        <div class="row">
          <div class="col-md-9 form-group">
            <h2 id="addDeliveryNoteEnumeratorLabel" style="display: none;"><spring:message code="common.deliveryNote.POS"/></h2>
            <h2 id="readDeliveryNoteEnumeratorLabel" style="display: none;"><spring:message code="administration.readDeliveryNoteEnumerator"/></h2>
            <h2 id="updateDeliveryNoteEnumeratorLabel" style="display: none;"><spring:message code="administration.updateDeliveryNoteEnumerator"/></h2>
            <input type="hidden" class="form-control" id="idInput">
          </div>
        </div>
        <form id="deliveryNoteEnumeratorAdministrationForm" action="" onsubmit="return false;">
          <div class="row">
            <div class="col-md-6 form-group">
              <label for="deliveryNotePOSInput"><spring:message code="common.deliveryNote.POS.number"/></label>
              <input type="text" class="form-control" id="deliveryNotePOSInput" name="deliveryNotePOS">
            </div>
            <div class="col-md-6 form-group">
              <label for="lastDeliveryNoteNumberInput"><spring:message code="common.lastDeliveryNoteNumber"/></label>
              <input type="text" class="form-control" id="lastDeliveryNoteNumberInput" name="lastDeliveryNoteNumber">
            </div>
            </div>
          <div class="row">
            <div class="col-md-6 form-group">
              <label for="activeSelect"><spring:message code="common.active"/></label>
              <select class="form-control chosen-select" id="activeSelect" name="active">
                <option value="true"><spring:message code="common.yes"/></option>
                <option value="false"><spring:message code="common.no"/></option>
              </select>
            </div>
            <div class="col-md-6 form-group">
              <label for="fakeSelect"><spring:message code="common.printDeliveryNote"/></label>
              <select class="form-control chosen-select" id="fakeSelect" name="fake">
                <option value="false"><spring:message code="common.yes"/></option>
                <option value="true"><spring:message code="common.no"/></option>
              </select>
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