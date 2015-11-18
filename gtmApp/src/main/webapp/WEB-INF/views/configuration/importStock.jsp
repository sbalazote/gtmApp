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
