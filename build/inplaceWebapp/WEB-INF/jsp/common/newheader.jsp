<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page import="com.elitecore.sm.common.constants.SystemParametersConstant"%>
<%@page import="com.elitecore.sm.util.MapCache"%>

<!-- New Header Code Starts Here -->
<head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">

        <title>
        	<spring:message code="header.server.manager.title" ></spring:message>
        </title>
        
        <link rel="shortcut icon" type="image/x-icon" href="${pageContext.request.contextPath}/img/favicon.ico"> 
        <meta name="description" content="">
        <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
        <!-- Place favicon.ico and apple-touch-icon.png in the root directory -->
        
        <!-- Bootstrap 3.3.4 -->
			<link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet" type="text/css" />
		<!-- Font Awesome Icons -->
			<link href="${pageContext.request.contextPath}/css/font-awesome.min.css" rel="stylesheet" type="text/css" />
		<!-- Ionicons -->
			<link href="${pageContext.request.contextPath}/css/ionicons.min.css" rel="stylesheet" type="text/css" />
		<!-- Theme style -->
			<link href="${pageContext.request.contextPath}/css/styles.css" rel="stylesheet" type="text/css" />
			<link href="${pageContext.request.contextPath}/css/skin.min.css" rel="stylesheet" type="text/css" />
		
			<link href="${pageContext.request.contextPath}/css/theme-color.css" rel="stylesheet">
			<link href="${pageContext.request.contextPath}/css/theme-font.css" rel="stylesheet">
			<link href="${pageContext.request.contextPath}/css/custome.css" rel="stylesheet">
			
		<!--  common css -->
        	<link rel="stylesheet" href="${pageContext.request.contextPath}/css/common.css">
        
        <!--login page css-->
			<link href="${pageContext.request.contextPath}/css/login.css" rel="stylesheet" type="text/css" />
			
		<!--scroll bar css -->
			<link rel="stylesheet" href="${pageContext.request.contextPath}/css/jquery.mCustomScrollbar.css">
			
		<!-- JQGRID resources -->
			<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/jquery-ui.css">
			<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/ui.jqgrid.css">
		
			<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/jquery.fancybox.css" media="screen" />
			<link href="${pageContext.request.contextPath}/css/bootstrap-switch.css" rel="stylesheet">
			<link href="${pageContext.request.contextPath}/css/bootstrap-toggle.css" rel="stylesheet">
			 <link href="${pageContext.request.contextPath}/css/datepicker.css" rel="stylesheet">
			
		<!-- Ionicons -->
			<link href="${pageContext.request.contextPath}/css/ionicons.min.css" rel="stylesheet" type="text/css" />
			
		<!-- REQUIRED JS SCRIPTS -->

		<!-- jQuery 2.1.4 --> 
		
		<!--	<script src="${pageContext.request.contextPath}/js/jQuery-2.1.4.min.js"></script>  -->
			
		<!-- jQuery 3.5.1 --> 
			<script src="${pageContext.request.contextPath}/jquery/3.5.1/jquery.min.js"></script>
            		<script src="${pageContext.request.contextPath}/js/jquery-migrate-3.4.0.min.js"></script>
            		<script src="${pageContext.request.contextPath}/js/jquery-ui.min.js"></script>
			<script src="${pageContext.request.contextPath}/js/jquery-ui.js"></script>
		
		<!-- JQGRID resources -->
			<script type='text/javascript' src="${pageContext.request.contextPath}/js/grid.locale-en.js"></script>
			<script type='text/javascript' src="${pageContext.request.contextPath}/js/jquery.jqGrid.min.js"></script>
			<%-- <script type='text/javascript' src="${pageContext.request.contextPath}/js/jquery-ui.js"></script>   --%> 
		
		<!-- Bootstrap 3.3.2 JS --> 
			
		    <c:if test="${empty isBootstrapDisable}">
			    <script src="${pageContext.request.contextPath}/js/bootstrap.min.js" type="text/javascript"></script> 
		    </c:if>
					
		<!-- AdminLTE App --> 
			<script src="${pageContext.request.contextPath}/js/app.min.js" type="text/javascript"></script>
			<script src="${pageContext.request.contextPath}/js/bootstrap.file-input.js" type="text/javascript"></script> 
			<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.fancybox.js"></script>
			
			
			
		<!--scroll bar JS -->
			<script src="${pageContext.request.contextPath}/js/jquery.mCustomScrollbar.concat.min.js"></script>
			
		<!-- TOGGLE ON/OFF SWITCH --> 
		<script src="${pageContext.request.contextPath}/js/bootstrap-toggle.js"></script>
		<script src="${pageContext.request.contextPath}/js/bootstrap-datepicker.min.js"></script>			
        <!-- REQUIRED JS SCRIPTS -->
        
        <!-- Date Time Picker -->
        <!-- https://github.com/smalot/bootstrap-datetimepicker -->
        <!-- NOTE: Original JS file is modified: Proton is forcing bootstrap 2 plugin mode in order to support font icons -->
        <script src="${pageContext.request.contextPath}/js/vendor/bootstrap-datetimepicker.js"></script>
        <!-- JSTree -->
		<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/jstree.style.min.css" />
        <script src="${pageContext.request.contextPath}/js/jstree.min.js"></script>
        
        <script src="${pageContext.request.contextPath}/js/jquery.blockUI.js" type="text/javascript"></script> 
        <script src="${pageContext.request.contextPath}/js/ajaxfileupload.js"></script>
        
        <script src="${pageContext.request.contextPath}/customJS/footer.js<%= "?v=" + Math.random() %>" type="text/javascript"></script>
		<script src="${pageContext.request.contextPath}/customJS/serverManager.js<%= "?v=" + Math.random() %>" type="text/javascript"></script>
		<script src="${pageContext.request.contextPath}/customJS/serviceManager.js<%= "?v=" + Math.random() %>" type="text/javascript"></script>
	
		<!-- <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.5.2/jquery.min.js"></script> -->
		<script src="js/modernizr.js"></script>
		<script src="js/jsvalidation.js"></script>
		<script type="text/javascript">
			//This will redirect user to login page in case of session timeout in ajax
			$.ajaxSetup({
				complete: function(xhr, status) {
					if(xhr.responseText.indexOf("<spring:message code='session.timeout'></spring:message>") !== -1) {
						window.location.href = "/ServerManager/login-fail?error-timeout";
					}
			    }
			});
			/* Added For MED-5064 , MED-4814 -start */
			 $(document).ready(function() {
				try {
					$.fn.datepicker.defaults.autoclose = true;
				} catch (err) {
					console.log("Error : "+err);
				}
				var dateFormat='<%= MapCache.getConfigValueAsObject(SystemParametersConstant.DATE_FORMAT)%>';
				if(dateFormat==null ||dateFormat==""){
					try {
						$.fn.datepicker.defaults.format= '<%= MapCache.getConfigValueAsString(SystemParametersConstant.DATE_FORMAT, "dd/MM/yyyy").toLowerCase() %>';
					} catch (err) {
						console.log("Error : "+err);
					}
				}
				else{
					try {
						$.fn.datepicker.defaults.format= '<%= MapCache.getConfigValueAsObject(SystemParametersConstant.DATE_FORMAT).toString().toLowerCase() %>';
					} catch (err) {
						console.log("Error : "+err);
					}
				}	
				// MED-8180
				$('select').parent('div').removeAttr("style");
				$('select').parent('div').removeClass().addClass('input-group-select');
			}); 
			/* MED-5064,MED-4814 -end */
		</script>
		
        <style>
			.ui-jqgrid tr.jqgrow td {
				white-space: normal !important;
			}
			
			.spanBreadCrumb a{
				color: black;
				-moz-text-decoration-line: none;
			}
			
			/* CSS for Create Server*/
			.ui-draggable-dragging {
				z-index: 3;
				display: block;
			}
			
			.server-droppable-hover{
				background-color: #A9A9A9 !important;
/*     			background-image: url("") !important; */
			}
			
			.ui-state-hover {
				/* display: block; */
        	}

			#cart {
				width: 100%;
				margin-top: 0px;
			}

			/* style the list to maximize the droppable hitarea */
			#cart ol {
				margin: 0;
				padding: 0;
			}
			
			.ui-state-hover-server-instance {
				border: 1px solid transparent;
			}
			.ui-state-hover-service{
				border: 1px solid transparent;
			}
			
			.ui-state-hover-server-instance > .grey-bg {
				background: #dadada;
			}
			
			.dynamic-server {
				float: left;
				width: 25%;
				border: 1px solid #dddddd;
				height: 150px;
			}
			
			.spanBreadCrumb a{
				color: black;
			}

			.dynamic-server-instance {
				border: 1px solid #dddddd;
				height: 150px;
				padding-bottom:10px;
				vertical-align: top;
			}

			#cart-added td {
				vertical-align:top;
				padding-bottom: 10px;
			}
			
			.server-drop {
				position: relative;
			}

			#serverInstance1 > div {
				background: #fff;
			}
			
			.input-group-select{
				position:relative;
				display:flex;
				border-collapse:separate;
				width: 97%;
			}
			
			.comment {
				width: 400px;
			}
			
			a.morelink {
				text-decoration:none;
				outline: none;
			}
			
			.morecontent span {
				display: none;
			}
			
			#serverInstance1_droppable {
				margin-bottom:10px;
			}
			
			.ui-draggable{
				cursor: move;
			}
			/* End of CSS for Create Server*/
			
			/* This only works with JavaScript, if it's not present, don't show loader */
			.no-js #loader { display: none;  }
			.js #loader { display: block; position: absolute; left: 100px; top: 0; }
			.se-pre-con {
				position: fixed;
				left: 0px;
				top: 0px;
				width: 100%;
				height: 100%;
				z-index: 9999;
				/* background: url(http://smallenvelop.com/wp-content/uploads/2014/08/Preloader_10.gif) center no-repeat #fff; */
				background: url('img/preloaders/circle-red.gif') center no-repeat #fff;
			}
			.ui-pg-input{
				height: 18px !important;
			}
	</style>

		
		<!-- JSTree -->
		<link rel="stylesheet" href="styles/jstree.style.min.css" />
        <script src="js/jstree.min.js"></script>
        <script type="text/javascript"> var showToggleSidebar = true; </script>
        
        <script type="text/javascript" src="${pageContext.request.contextPath}/js/bootstrap-multiselect.js"></script>
		<link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap-multiselect.css" type="text/css"/>
</head>
<!-- New Header Code Ends Here -->
