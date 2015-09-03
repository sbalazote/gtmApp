<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<!DOCTYPE html>
<html>
	<head>
		<tiles:insertAttribute name="header" />
	</head>
	<body>
		<div id="wrap">
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