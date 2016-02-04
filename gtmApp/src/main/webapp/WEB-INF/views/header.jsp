<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.lsntsolutions.gtmApp.config.PropertyProvider" %>
<%@ page import="java.io.File" %>
<%@ page import="java.sql.Connection" %>
<%@ page import="java.sql.DriverManager" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page import="java.sql.Statement" %>
<%
	String artifactId = PropertyProvider.getInstance().getProp(PropertyProvider.ARTIFACT_ID);
	String name = PropertyProvider.getInstance().getProp(PropertyProvider.NAME);
	if (name.equals("")) {
		Connection connection = DriverManager.getConnection(
				PropertyProvider.getInstance().getProp(PropertyProvider.DATABASE_URL),
				PropertyProvider.getInstance().getProp(PropertyProvider.DATABASE_USERNAME),
				PropertyProvider.getInstance().getProp(PropertyProvider.DATABASE_PASSWORD));

		Statement statement = connection.createStatement();

		ResultSet resultset = statement.executeQuery("select * from property");
		resultset.first();
		PropertyProvider.getInstance().setProp(PropertyProvider.NAME, resultset.getString("name"));
	}
	name = PropertyProvider.getInstance().getProp(PropertyProvider.NAME);
%>
<title><%= artifactId + " - " + name %></title>
<meta name="viewport" content="initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,width=device-width,user-scalable=no" />
<meta charset="UTF-8">

<%
	String relativePath = "";
	String realPath = request.getServletContext().getRealPath("/images/uploadedLogo.png");

	File file = new File(realPath);

	if(file.exists()) {
		relativePath = "./images/uploadedLogo.png";
	} else {
		relativePath = "./images/logo.png";
	}
%>
<link rel="shortcut icon" href=<%= relativePath %> type="image/png">

<c:set var="context" value="${pageContext.request.contextPath}"/>

<link rel="stylesheet" type="text/css" href="${context}/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" href="${context}/css/bootstrap-theme.min.css">
<link rel="stylesheet" type="text/css" href="${context}/css/jquery-ui.min.css">
<link rel="stylesheet" type="text/css" href="${context}/css/multi-select.css">
<link rel="stylesheet" type="text/css" href="${context}/css/chosen.min.css">
<link rel="stylesheet" type="text/css" href="${context}/css/chosen-bootstrap.css">
<link rel="stylesheet" type="text/css" href="${context}/css/jquery.fileupload-ui.css">
<link rel="stylesheet" type="text/css" href="${context}/css/bootstrap-dialog.css">
<link rel="stylesheet" type="text/css" href="${context}/css/select2.css">
<link rel="stylesheet" type="text/css" href="${context}/css/select2-bootstrap.css">
<link rel="stylesheet" type="text/css" href="${context}/css/jquery.bootgrid.min.css">
<link rel="stylesheet" type="text/css" href="${context}/css/themes/blue/pace-theme-minimal.css">
<link rel="stylesheet" type="text/css" href="${context}/css/toastr.min.css">
<link rel="stylesheet" type="text/css" href="${context}/css/styles.css">
<link rel="stylesheet" type="text/css" href="${context}/css/font-awesome.min.css">
<link rel="stylesheet" type="text/css" href="${context}/css/fonts.googleapis-roboto.css">

<%--<script type="text/javascript" src="${context}/js/jquery-1.10.2.min.js"></script>--%>
<script type="text/javascript" src="${context}/js/jquery-2.2.0.min.js"></script>
<script type="text/javascript" src="${context}/js/jquery-ui.min.js"></script>
<script type="text/javascript" src="${context}/js/jquery.validate.min.js"></script>
<script type="text/javascript" src="${context}/js/chosen.jquery.min.js"></script>
<script type="text/javascript" src="${context}/js/additional-methods.min.js"></script>
<%--<script type="text/javascript" src="${context}/js/jquery.bootgrid.min.js"></script>--%>
<script type="text/javascript" src="${context}/js/jquery.bootgrid.js"></script>
<script type="text/javascript" src="${context}/js/localization/messages_es.js"></script>
<script type="text/javascript" src="${context}/js/localization/jquery.ui.datepicker-es.min.js"></script>
<script type="text/javascript" src="${context}/js/bootstrap.min.js"></script>
<script type="text/javascript" src="${context}/js/main.js"></script>
<script type="text/javascript" src="${context}/js/alerts.js"></script>
<script type="text/javascript" src="${context}/js/jquery.multi-select.js"></script>
<script type="text/javascript" src="${context}/js/canvas-to-blob.min.js"></script>
<script type="text/javascript" src="${context}/js/load-image.all.min.js"></script>
<script type="text/javascript" src="${context}/js/jquery.iframe-transport.js"></script>
<script type="text/javascript" src="${context}/js/jquery.fileupload.js"></script>
<script type="text/javascript" src="${context}/js/jquery.fileupload-process.js"></script>
<script type="text/javascript" src="${context}/js/jquery.fileupload-image.js"></script>
<script type="text/javascript" src="${context}/js/jquery.fileupload-validate.js"></script>
<script type="text/javascript" src="${context}/js/bootstrap-dialog.js"></script>
<script type="text/javascript" src="${context}/js/select2.js"></script>
<script type="text/javascript" src="${context}/js/select2_locale_es.js"></script>
<script type="text/javascript" src="${context}/js/jquery.blockUI.js"></script>
<script type="text/javascript" src="${context}/js/jquery.base64.js"></script>
<script type="text/javascript" src="${context}/js/pace.js"></script>
<script type="text/javascript" src="${context}/js/toastr.min.js"></script>
<script type="text/javascript" src="${context}/js/jquery.cookie.js"></script>
<script type="text/javascript" src="${context}/js/jQuery.download.js"></script>
<script type="text/javascript" src="${context}/js/jquery.numeric.js"></script>
<script type="text/javascript" src="${context}/js/underscore-min.js"></script>