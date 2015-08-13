<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<script type="text/javascript" src="js/form/administration/save/saveProviderSerializedFormat.js" ></script>
<script type="text/javascript">
    $(document).ready(function() {
        new SaveProviderSerializedFormat();
    });
</script>

<form id="providerSerializedFormatAdministrationForm" action="" onsubmit="return false;">

<div class="row">
    <div class="col-md-9 form-group">
        <h3><spring:message code="common.serializedFormat"/></h3>
    </div>
</div>

<div class="row">
    <div class="col-md-4 form-group">
        <label for="fieldType"><spring:message code="common.field"/></label>
        <select class="form-control chosen-select" id="fieldType" name="type">
            <option value="G"><spring:message code="common.gtin"/></option>
            <option value="S"><spring:message code="common.serialNumber"/></option>
            <option value="E"><spring:message code="common.expirationDate"/></option>
            <option value="B"><spring:message code="common.batch"/></option>
        </select>
    </div>
    <div class="col-md-4 form-group">
        <label for="lengthInput"><spring:message code="common.length"/></label>
        <input type="text" class="form-control" id="lengthInput" name="length">
    </div>
    <div class="col-md-4 form-margin">
        <button class="btn btn-default btn-block" id="add"><span class="glyphicon glyphicon-plus"></span> <spring:message code="common.add"/></button>
    </div>
</div>

<div class="row">
    <div class="col-md-12 form-group">
        <table class="table" id="fieldsTable">
              <thead>
                <tr>
                  <th><spring:message code="common.position"/></th>
                  <th><spring:message code="common.field"/></th>
                  <th><spring:message code="common.length"/></th>
                  <th><spring:message code="common.action"/></th>
                </tr>
              </thead>
              <tbody>
              </tbody>
        </table>
    </div>
</div>

<div class="row">
    <div class="col-md-2 col-md-offset-8">
        <button onclick="location.href='providerSerializedFormatAdministration.do'" class="btn btn-default btn-block" id="abort"><span class="glyphicon glyphicon-arrow-left"></span> <spring:message code="common.back"/></button>
    </div>
    <div class="col-md-2">
        <button class="btn btn-success btn-block" id="confirm"><span class="glyphicon glyphicon-ok"></span> <spring:message code="common.add.entity"/></button>
    </div>
</div>

</form>