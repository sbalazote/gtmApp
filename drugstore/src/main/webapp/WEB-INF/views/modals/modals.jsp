<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<div class="modal" data-backdrop="static" data-keyboard="false" tabindex="-1" role="dialog" aria-labelledby="batchExpirationDatesModalLabel" id="batchExpirationDatesModal">
  <div class="modal-dialog" style="width: 900px">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
        <h4 class="modal-title" id="batchExpirationDatesModalLabel">
          <spring:message code="input.modal.batchExpirationDates.title" />
        </h4>
      </div>
      <div class="modal-body">
        <div class="container-fluid">
          <div class="row">
            <div class="col-md-12 form-group">
              <h4 id="batchExpirationDateProductDescription" style="color: blue; font-weight: bold"></h4>
            </div>
          </div>
          <br>
          <table id="batchExpirationDatesTable" class="table table-condensed table-hover table-striped">
            <thead>
            <tr>
              <th data-identifier="true" data-column-id="id" data-type="numeric" data-visible="false"></th>
              <th data-column-id="amount" data-type="numeric"><spring:message code="common.amount" /></th>
              <th data-column-id="batch"><spring:message code="common.batch" /></th>
              <th data-column-id="expirationDate"><spring:message code="common.expirationDate" /></th>
            </tr>
            </thead>
            <tbody>
            </tbody>
          </table>
        </div>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-default" data-dismiss="modal">
          <spring:message code="common.close" />
        </button>
      </div>
    </div>
  </div>
</div>

<div class="modal" data-backdrop="static" data-keyboard="false" tabindex="-1" role="dialog" aria-labelledby="serialsModalLabel" id="serialsModal">
  <div class="modal-dialog" style="width: 900px">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
        <h4 class="modal-title" id="serialsModalLabel">
          <spring:message code="input.modal.serials.title" />
        </h4>
      </div>
      <div class="modal-body">
        <div class="container-fluid">
          <div class="row">
            <div class="col-md-12 form-group">
              <h4 id="serializedProductDescription" style="color: blue; font-weight: bold"></h4>
            </div>
          </div>
          <br>
          <table id="serialsTable" class="table table-condensed table-hover table-striped">
            <thead>
            <tr>
              <th data-identifier="true" data-column-id="serialNumber"><spring:message code="common.serialNumber" /></th>
              <th data-column-id="batch"><spring:message code="common.batch" /></th>
              <th data-column-id="expirationDate"><spring:message code="common.expirationDate" /></th>
              <th data-column-id="viewTraceability"><spring:message code="common.view" /></th>
            </tr>
            </thead>
            <tbody>
            </tbody>
          </table>
        </div>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-default" data-dismiss="modal">
          <spring:message code="common.close" />
        </button>
      </div>
    </div>
  </div>
</div>