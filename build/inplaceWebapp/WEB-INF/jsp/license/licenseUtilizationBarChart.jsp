<%@page import="com.elitecore.sm.common.constants.LicenseConstants"%>
<%@page import="com.elitecore.sm.util.MapCache"%>
<%@page
	import="com.elitecore.sm.common.constants.SystemParametersConstant"%>
<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<%@page import="com.elitecore.sm.util.EliteUtils"%>
<%@page import="com.elitecore.sm.common.constants.BaseConstants"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/security/tags"
	prefix="sec"%>
<%@taglib uri="/WEB-INF/ShowErrorTag.tld" prefix="elitecoreError"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<%
	String user = (String) session.getAttribute("userName");
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>License Utilization Bar Chart</title>
<%-- <link href="https://fonts.googleapis.com/css?family=Open+Sans:400,700&display=swap" rel="stylesheet"> --%>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/fonts-googleapis.css?family=Open+Sans:400,700&display=swap">
<style>
* {
	font-family: 'Open Sans Condensed', sans-serif;
}

h1, h2 {
	padding: 20px;
}

.chart-container {
	border: lightgrey 1px solid;
}
</style>
<!-- Include barchart CSS -->
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/barchart.css">
</head>
<body>
	<div id="divLoading" align="center" style="display: none;">
			<img src="img/preloaders/Preloader_10.gif" />
	</div>
	<div id="license-utilization-chart-container"></div>
	<!-- Include barchart script -->
	<script src="${pageContext.request.contextPath}/js/barchart.js"></script>
	<!-- Include your own script below, or edit the test script -->
	<script type="text/javascript">
	let data1 = [];
	$( document ).ready(function() {     
		const options = {
			title: 'License Utilization In TPS (k)',
			barColors: ['lightblue']
		}
        getLicenseUtilizationData();
        const chart = document.getElementById('license-utilization-chart-container');
        createBarChart(data1, chart, options);
	});
	
	function getLicenseUtilizationData(){			
		$.ajax({
			url: "<%=ControllerConstants.GET_LICENSE_UTILIZATION_DATA%>",
			cache : false,
			async : false,
			dataType : 'json',
			type : "GET",
			success : function(data) {
				var response = eval(data);
				var responseCode = response.code;
				var responseMsg = response.msg;
				var responseObject = response.object;
				if (responseCode == "200") {	
					var maxTPS = responseObject["MAX_LICENSE_TPS"];					
					var currentTPS = responseObject["CURRENT_LICENSE_TPS"];
					var appliedTPS = responseObject["APPLIED_LICENSE_TPS"];
					
					data1.push(['Max Utilization', maxTPS]);
                    data1.push(['Current Utilization', currentTPS]);
                    data1.push(['Applied License', appliedTPS]);

				} else {					
					data1.push(['Max Utilization', 0]);
                    data1.push(['Current Utilization', 0]);
                    data1.push(['Applied License', 0]);
                    console.log('Failed to get license utilization details.');
				}
			},
			error : function(xhr, st, err) {
				handleGenericError(xhr, st, err);
			}
		});
	}
</script>
</body>
</html>
