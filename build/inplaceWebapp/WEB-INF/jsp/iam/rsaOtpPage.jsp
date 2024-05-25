<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<%@ page import="com.elitecore.sm.common.constants.ReadFromPropertiesFile"%>
<%@page import="com.elitecore.sm.common.constants.BaseConstants"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="/WEB-INF/ShowErrorTag.tld" prefix="elitecoreError"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="elitecore"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title><spring:message code="header.server.manager.title" ></spring:message></title>
<meta
	content='width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no'
	name='viewport'>
	<!-- jQuery 3.5.1 -->
	<script src="${pageContext.request.contextPath}/jquery/3.5.1/jquery.min.js"></script>
	<script src="${pageContext.request.contextPath}/js/jquery-migrate-3.4.0.min.js"></script>
<script src="${pageContext.request.contextPath}/js/jquery.blockUI.js"
	type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/js/ajaxfileupload.js"></script>
<link rel="shortcut icon" type="image/x-icon"
	href="${pageContext.request.contextPath}/img/favicon.ico">
<!--  	Bootstrap 3.3.4  -->
<link href="css/bootstrap.min.css" rel="stylesheet" type="text/css" />
<!-- 	Font Awesome Icons  -->
<link href="css/font-awesome.min.css" rel="stylesheet" type="text/css" />
<!-- 	 Theme style  -->
<link href="css/styles.css" rel="stylesheet" type="text/css" />

<link href="css/theme-color.css" rel="stylesheet">
<link href="css/theme-font.css" rel="stylesheet">
<link href="css/custome.css" rel="stylesheet">
<link href="css/login.css" rel="stylesheet" type="text/css" />
<style type="text/css">

.topright {
  position: absolute;
  top: 30px;
  right: 10%;
  font-size: 14px;
  color: #fff;
}

</style>
</head>
<script type="text/javascript">
function verifyOtp(otp){
		var urlAction  = '${pageContext.request.contextPath}/<%=ControllerConstants.RSA_HOME%>';
		$.ajax({
			url : urlAction,
			cache : false,
			async : false,
			dataType : 'json',
			type : 'POST',
			data : {
				"otp": $("#otp_txt").val()
			},
			success : function(data) {
				var response = eval(data);
				var responseCode = response.code;
				var responseMsg = response.msg;
				var responseObject = response.object;
				console.log(response);
				console.log(responseCode);
				if (responseCode === "200") {
			     	 window.location.href = "${pageContext.request.contextPath}/home?isRsaSuccess=yes";
				}else {
					showMsg(responseMsg);
					$('#otp_txt').val('');
					$('#otp_txt').focus();
				}
			},
			error : function(xhr, st, err) {
				handleGenericError(xhr, st, err);
			}
				
		});
	}
</script>
<script>
	function showMsg(message) {
		var topPixel = "11%";

		if ($(window).width() < 767) {
			topPixel = "-17%";
		}

		$.blockUI({
			message : '<h5>' + message + '</h5>',
			//timeout : 10000,
			showOverlay : false,
			centerY : true,
			css : {
				cursor : 'default',
				width : 'auto',
				weight : 'bold',
				top : '60%',
				left : '34%',
				//right : '38%',
				border : 'none',
				//padding : '2px 5px 2px 5px',
				//backgroundColor : '#000',
				//'-webkit-border-radius' : '10px',
				//'-moz-border-radius' : '10px',
				//opacity : .6,
				color : ' #FF0000'
			}
		});
	}
</script>

<body class="login-page"
	style="background-image: none; background-color: #fff;overflow-x: hidden;">

	<!-- Header Start -->
	<div class="fullwidth pull-left login-header"
		style="background: #fe7b26">
		<div class="fullwidth pull-left logobox logocenter">
			<div class="login-logo" style="float: left;">
				<a href="#"><img src="img/logo-1.png"></a>
			</div>
			  <div class="topright"><% out.println(session.getAttribute("userName")); %></div>
		</div>
	</div>
    <br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br>
    <div class="row">
		<div class="col-xs-4 col-sm-4 col-lg-4"></div>
		<div class="col-xs-4 col-sm-4 col-lg-4"><span style="font-size: 20px;color: #DD4B39;padding: 1%"><b>Provide Verification Code</b></span></div>
		<div class="col-xs-4 col-sm-4 col-lg-4"></div>
	</div>
	<div class="row">
		<div class="col-xs-4 col-sm-4 col-lg-4"></div>
		<div class="col-xs-4 col-sm-4 col-lg-4">
			<div
				style="height: auto; border: solid #DCDCDC 4px; border-style: solid; border-width: thin; border-radius: 8px; padding: 3%">
				<span style="color:#808080;font-size:14px;">Please provide the code as
					shown in <% out.println(ReadFromPropertiesFile.getProperties("authenticator.name")); %> Authenticator.</span>
				<form role="form" id="loginForm" class="form-inline" method="post">
					<div class="form-group"
						style="margin-top: 3%; font-family: sans-serif; font-size: 14px;">
						 <label for="email" style="color: #808080;"><b>Enter Code :</b> </label> 
						 <input type="text"
							style="margin-left:8px;border:solid 1px #808080;color: #000;background-color: #fff" class="form-control" autocomplete="off"
							data-toggle="tooltip" id="otp_txt" name="otp_txt"
							autofocus="autofocus" data-placement="bottom"
							title="<spring:message code="index.page.verification.code.lable" ></spring:message>" />
						<input id="login_btn" type="button" style="margin-left:12px;"  onclick="verifyOtp()"
							class="btn btn-primary btn-flat"
							value="<spring:message code="btn.label.login"></spring:message>">
					</div>
				</form>
			</div>
		</div>
		<div class="col-xs-4 col-sm-4 col-lg-4"></div>
	</div>
</html>
