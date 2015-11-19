<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<script type="text/javascript" src="js/form/configuration/importStock.js" ></script>
<script type="text/javascript">
  $(document).ready(function() {
    new ImportStock();
  });
</script>

<form id="importStockForm" action="" onsubmit="return false;">

  <div class="row">
    <div class="col-md-9 col-lg-9 form-group">
      <h3>
        <spring:message code="administration.importStock.title" />
      </h3>
    </div>
  </div>

  <div class="row">
    <div class="col-md-4 col-lg-4 form-group">
      <label for="conceptInput"><spring:message code="common.concept"/></label>
      <select id="conceptInput" name="concept" class="form-control chosen-select" data-placeholder="<spring:message code='common.select.option'/>" autofocus>
        <option value=""></option>
        <c:forEach items="${concepts}" var="concept">
          <option value="${concept.id}"  ${conceptId == concept.id ? 'selected' : ''}><c:out value="${concept.code}"></c:out> - <c:out value="${concept.description}"></c:out></option>
        </c:forEach>
      </select>
    </div>
    <div class="col-md-4 col-lg-4 form-group">
      <label for="agreementInput"><spring:message code="common.agreement"/></label>
      <select id="agreementInput" name="agreement" class="form-control chosen-select" data-placeholder="<spring:message code='common.select.option'/>">
        <option value=""></option>
        <c:forEach items="${agreements}" var="agreement">
          <option value="${agreement.id}" ${agreementId == agreement.id ? 'selected' : ''}><c:out value="${agreement.code}"></c:out> - <c:out value="${agreement.description}"></c:out></option>
        </c:forEach>
      </select>
    </div>
    <div id="providerDiv" class="col-md-4 col-lg-4 form-group" >
      <label for="providerInput"><spring:message code="common.provider"/></label>
      <select id="providerInput" name="provider" class="form-control chosen-select" data-placeholder="<spring:message code='common.select.option'/>">
        <option value=""></option>
        <c:forEach items="${providers}" var="provider">
          <option value="${provider.id}" ${providerId == provider.id ? 'selected' : ''}><c:out value="${provider.code}"></c:out> - <c:out value="${provider.name}"></c:out></option>
        </c:forEach>
      </select>
    </div>
  </div>

  <div class="row">
    <div class="col-md-3 col-lg-3 form-group">
      <label for="firstRowInput"><spring:message code="common.firstRow"/></label>
      <input name="firstRow" id="firstRowInput" type="text" class="form-control">
    </div>
    <div class="col-md-3 col-lg-3 form-group">
      <label for="typeColumnInput"><spring:message code="common.typeColumn"/></label>
      <input name="typeColumn" id="typeColumnInput" type="text" class="form-control">
    </div>
    <div class="col-md-3 col-lg-3 form-group">
      <label for="gtinColumnInput"><spring:message code="common.gtinColumn"/></label>
      <input name="gtinColumn" id="gtinColumnInput" type="text" class="form-control">
    </div>
    <div class="col-md-3 col-lg-3 form-group" >
      <label for="batchColumnInput"><spring:message code="common.batchColumn"/></label>
      <input name="batchColumn" id="batchColumnInput" type="text" class="form-control">
    </div>

  </div>

  <div class="row">
    <div class="col-md-4 col-lg-4 form-group" >
      <label for="expirationColumnInput"><spring:message code="common.expirationColumn"/></label>
      <input name="expirationColumn" id="expirationColumnInput" type="text" class="form-control">
    </div>
    <div class="col-md-4 col-lg-4 form-group">
      <label for="serialColumnInput"><spring:message code="common.serialColumn"/></label>
      <input name="serialColumn" id="serialColumnInput" type="text" class="form-control">
    </div>
    <div class="col-md-4 col-lg-4 form-group">
      <label for="amountColumnInput"><spring:message code="common.amountColumn"/></label>
      <input name="amountColumn" id="amountColumnInput" type="text" class="form-control">
    </div>
  </div>

  <div class="row">
    <div class="col-md-3 col-lg-3 form-group">
      <label for="importStockInput"><spring:message code="administration.importStock.stockFile" /></label>
      <span class="btn btn-info fileinput-button form-control">
          <i class="glyphicon glyphicon-plus"></i>
              <span><spring:message code="administration.alfabetaUpdateProducts.addAlfabetaUpdateFile" /></span>
          <input id="importStockInput" type="file" name="importStock" data-url="uploadStock.do">
      </span>
    </div>
  </div>

  <div class="row">
    <div class="col-md-2 col-md-offset-8 col-lg-2 col-lg-offset-8">
      <button onclick="location.href='productAdministration.do'"
              class="btn btn-default btn-block" id="abort">
        <span class="glyphicon glyphicon-arrow-left"></span>
        <spring:message code="common.back"/>
      </button>
    </div>
    <div class="col-md-2 col-lg-2">
      <button class="btn btn-success btn-block" id="confirm">
        <span class="glyphicon glyphicon-upload"></span>
        <spring:message code="common.update" />
      </button>
    </div>
  </div>

</form>
