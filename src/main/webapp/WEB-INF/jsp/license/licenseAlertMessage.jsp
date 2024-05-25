<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%> 
<%@page import="com.elitecore.sm.common.constants.BaseConstants"%>
<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<jsp:include page="../common/newheader.jsp"></jsp:include>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>License | Alerts</title>
</head>
<body>

<a href="#divLicenseAlerts" class="fancybox" style="display: none;" id="license_alert_message_link">#</a>
<!-- License alert Message pop-up  start here -->
<div id="divLicenseAlerts" style=" width:100%; display: none;" >
	<div class="modal-content">
		<div class="modal-header padding10">
			<h4 class="modal-title">
				<spring:message code="license.activation.header.label" ></spring:message>
			</h4>
		</div>
		<div class="modal-body padding10 inline-form">
			<div id="licenseMessages">${licenseAlertMessage}</div>
		</div>
		<div class="modal-footer padding10">
			<button type="button" id="license_redirect_action"
				class="btn btn-grey btn-xs " onclick="submitForm();">
				<spring:message code="btn.label.ok" ></spring:message>
			</button>
		</div>
	</div>
</div>
	<c:choose>
		<c:when test="${(isLicenseExpired eq true)}">
			<form id="redirect_action"	action="<%=ControllerConstants.REDIRECT_LICENSE_AGREEMENT%>" method="POST">
				 <input type="hidden" name="disableTrialButton" id="disableTrialButton" value="true" />
			</form>
		</c:when>
		<c:otherwise>
			<form id="redirect_action" action="<%=ControllerConstants.DASHBOARD_REDIRECT_VIEW %>" method="POST">
				<input type="hidden" name="isMessageShow" id="isMessageShow" value="${isEnableShortMessage}" />
				<input type="hidden" name="licenseShortReminder" id="licenseShortReminder" value="${licenseShortReminder}" />
				<input type="hidden" name="isRenew" id="isRenew" value="${isRenew}" />
			</form>
		</c:otherwise>
	</c:choose>
	<!-- License alert Message pop-up  end here -->

<!-- Footer code start here -->
		<footer class="main-footer positfix" style="margin-left: 0;">
			<div class="fooinn">
				<jsp:include page="../common/newfooter.jsp"></jsp:include>
			</div>
		</footer>
		<!-- Footer code end here -->
	<script type="text/javascript">
	$(document).ready(function() {
		$("#license_alert_message_link").click();
	});
	
	function submitForm(){
		$("#redirect_action").submit();
	}
	</script>
</body>
</html>
