<!DOCTYPE html>
<html>
	<head>
		<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
		<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
		<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
		<%@ page import="com.drogueria.config.PropertyProvider" %>
		<%@ page import="java.sql.*" %>
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

		<link rel="shortcut icon" href="./images/logo.png" type="image/png">
	
		<link rel="stylesheet" type="text/css" href="css/bootstrap.css">
		<link rel="stylesheet" type="text/css" href="css/bootstrap-theme.css">
		<link rel="stylesheet" type="text/css" href="css/jquery-ui.css">
		<link rel="stylesheet" type="text/css" href="css/styles.css">
		<link rel="stylesheet" type="text/css" href="css/multi-select.css">
		<link rel="stylesheet" type="text/css" href="css/chosen.css">
		<link rel="stylesheet" type="text/css" href="css/chosen-bootstrap.css">
		<link rel="stylesheet" type="text/css" href="css/jquery.fileupload-ui.css">
		<link rel="stylesheet" type="text/css" href="css/bootstrap-dialog.css">
		<link rel="stylesheet" type="text/css" href="css/select2.css">
		<link rel="stylesheet" type="text/css" href="css/select2-bootstrap.css">
		<link rel="stylesheet" type="text/css" href="css/jquery.bootgrid.css">
		<link rel="stylesheet" type="text/css" href="css/themes/blue/pace-theme-corner-indicator.css">
		<link rel="stylesheet" type="text/css" href="css/jqClock.css">
		<link rel="stylesheet" type="text/css" href="//maxcdn.bootstrapcdn.com/font-awesome/4.1.0/css/font-awesome.min.css">
		<link rel="stylesheet" type="text/css" href='//fonts.googleapis.com/css?family=Merriweather:900italic'>
		<link rel="stylesheet" type="text/css" href='//fonts.googleapis.com/css?family=Roboto:500,700,400'>
		
		<script type="text/javascript" src="js/jquery-1.10.2.min.js"></script>
		<script type="text/javascript" src="js/jquery-ui.min.js"></script>
		<script type="text/javascript" src="js/jquery.validate.min.js"></script>
		<script type="text/javascript" src="js/chosen.jquery.min.js"></script>
		<script type="text/javascript" src="js/additional-methods.min.js"></script>
		<script type="text/javascript" src="js/jquery.bootgrid.js"></script>
		<script type="text/javascript" src="js/localization/messages_es.js"></script>
		<script type="text/javascript" src="js/localization/jquery.ui.datepicker-es.min.js"></script>
		<script type="text/javascript" src="js/bootstrap.min.js"></script>
		<script type="text/javascript" src="js/main.js"></script>
		<script type="text/javascript" src="js/alerts.js"></script>
		<script type="text/javascript" src="js/jquery.multi-select.js"></script>
		<script type="text/javascript" src="js/canvas-to-blob.min.js"></script>
		<script type="text/javascript" src="js/load-image.all.min.js"></script>
		<script type="text/javascript" src="js/jquery.iframe-transport.js"></script>
		<script type="text/javascript" src="js/jquery.fileupload.js"></script>
		<script type="text/javascript" src="js/jquery.fileupload-process.js"></script>
		<script type="text/javascript" src="js/jquery.fileupload-image.js"></script>
		<script type="text/javascript" src="js/jquery.fileupload-validate.js"></script>
		<script type="text/javascript" src="js/bootstrap-dialog.js"></script>
		<script type="text/javascript" src="js/select2.js"></script>
		<script type="text/javascript" src="js/select2_locale_es.js"></script>
		<script type="text/javascript" src="js/jquery.blockUI.js"></script>
		<script type="text/javascript" src="js/jspdf.debug.js"></script>
		<script type="text/javascript" src="js/jquery.base64.js"></script>
		<script type="text/javascript" src="js/pace.js"></script>
		<script type="text/javascript" src="js/jquery.cookie.js"></script>
		<script type="text/javascript" src="js/jQuery.download.js"></script>
		<script type="text/javascript" src="js/emulatetab.joelpurra.js"></script>
		<script type="text/javascript" src="js/plusastab.joelpurra.js"></script>
		<script type="text/javascript" src="js/jquery.ya-enter2tab.js"></script>
		<script type="text/javascript" src="js/jquery.numeric.js"></script>
	</head>
	<body>
		<div id="wrap">
			<div id="header">
				<tiles:insertAttribute name="header" />
			</div>
			<div id="menu">
				<tiles:insertAttribute name="menu" />
			</div>
			<div id="body">
				<div class="content">
					<div id="alertDiv"></div>
					<tiles:insertAttribute name="body" />
				</div>
			</div>
			<div id="footer">
				<tiles:insertAttribute name="footer" />
			</div>
		</div>
	</body>
</html>