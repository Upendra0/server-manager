<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib 	uri="http://www.springframework.org/tags" 			prefix="spring"%>
<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<script>
var serviceType;
$(document).ready(function() {
    serviceType = '<%=request.getParameter("serviceType")%>';		
});
function testFtpSftpConnections(){
	$('#testFtpSftpConnection').hide();
	$('#testFtpSftpConnection-progress-bar-div').show();
	if(serviceType == 'COLLECTION'){
	     $("#ftp-configuration-form").attr("action",'<%=ControllerConstants.TEST_FTP_SFTP_CONNECTION_FOR_COLLECTION%>');
	}else if(serviceType == 'DISTRIBUTION'){
		$("#ftp-configuration-form").attr("action",'<%=ControllerConstants.TEST_FTP_SFTP_CONNECTION_FOR_DISTRIBUTION%>');
	}
	$("#testFtpSftpConnection-progress-bar-div").show();
	$("#progress-bar-div").hide();
}
</script>
<spring:message code="ftp.driver.mgmt.config.test.connection.tooltip" var="tooltip"></spring:message>
<button id="testFtpSftpConnection" class="btn btn-grey btn-xs" onclick="testFtpSftpConnections();" data-toggle="tooltip" data-placement="bottom"  title="${tooltip}">
   <spring:message code="btn.label.testConnection"></spring:message>
</button>
<div id="testFtpSftpConnection-progress-bar-div" class="modal-footer padding0 text-center" style="display: none;">
   <jsp:include page="../common/processing-bar.jsp"></jsp:include>
</div>
