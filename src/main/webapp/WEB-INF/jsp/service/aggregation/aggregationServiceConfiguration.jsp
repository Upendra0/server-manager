<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<%@page import="com.elitecore.sm.common.constants.FormBeanConstants"%>
<%@ taglib uri="http://www.springframework.org/security/tags"	prefix="sec"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@taglib uri="/WEB-INF/ShowErrorTag.tld" prefix="elitecoreError"%>

<script type="text/javascript" src="${pageContext.request.contextPath}/js/bootstrap-multiselect.js"></script>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap-multiselect.css" type="text/css"/>
<style>
.checkbox{
	color: #000 !important;
}
.btn .caret {
    margin-left: -10px;
}
.btn-group{
	width: 100% !important;
}
.btn-group button{
	width: 100% !important;
}
.btn-default span{
width: 100% !important;
background:none !important ;
}
</style>


<div class="box-body padding0 mtop10">
	<div class="fullwidth">
		<form:form modelAttribute="<%=FormBeanConstants.AGGREGATION_SERVICE_CONFIGURATION_FORM_BEAN%>" method="POST" action="<%= ControllerConstants.UPDATE_AGGREGATION_SERVICE_CONFIGURATION %>" id="aggregation-service-configuration-form">
			
			<form:hidden path="serverInstance.id" id="serverInstanceId" value="${instanceId}"></form:hidden>
			<input type="hidden" name="serviceName" id="serviceName" value="${serviceName}"/>
			<input type="hidden" name="serviceId" id="serviceId" value="${serviceId}"/>
			<form:hidden path="id" id="serviceId" value="${serviceId}"></form:hidden>
			<form:hidden path="servInstanceId" id="servInstanceId" value="${servInstanceId}"></form:hidden>
			<form:hidden path="svctype.id" id="svctypeId" value="${svctype.id}"></form:hidden>
			<form:hidden path="svctype.type" id="svctypetype" value="${svctype.type}"></form:hidden>
			<form:hidden path="svctype.serviceCategory" id="svctypecategory" value="${svctype.serviceCategory}"></form:hidden>
			<form:hidden path="svctype.typeOfService" id="svctypeOfService" value="${svctype.typeOfService}"></form:hidden>
			<form:hidden path="svctype.description" id="svctypedescription" value="${svctype.description}"></form:hidden>
			<form:hidden path="svctype.alias" id="svctypealias" value="${svctype.alias}"></form:hidden>
			<form:hidden path="svctype.serviceFullClassName" id="svctypeserviceFullClassName" value="${svctype.serviceFullClassName}"></form:hidden>
			<form:hidden path="fileGroupingParameter.id" id="id"></form:hidden>
            <form:hidden path="status" id="status" value="${status}"></form:hidden>
            <form:hidden path="enableFileStats" id="enableFilestatusid" value="${enableFileStats}"></form:hidden>
			<form:hidden path="enableDBStats" id = "enableDBStatsid" value = "${enableDBStats}"></form:hidden>
           
            <!-- Section-0 start here -->
			<jsp:include page="../serviceBasicDetails.jsp"></jsp:include>
			<!-- section-0 end here -->

			<!-- section-2 start here -->
			<jsp:include page="../serviceExecutionParameters.jsp"></jsp:include>
			<!-- section-2 end here -->
			
			<!-- Section-2.1 start here -->
			<jsp:include page="aggregationOperationalParams.jsp"></jsp:include>
			<!-- section-2.1 end here -->
			
			<!-- Section-3 start here -->
			<jsp:include page="aggregationSchedulingParams.jsp"></jsp:include>
			<!-- section-3 end here -->
			
			<!-- Section-4 start here -->
			<jsp:include page="aggregationFileGroupingParams.jsp"></jsp:include>
			<!-- section-4 end here -->
		</form:form>
		
		<script type="text/javascript" >
			$(document).ready(function() {
				enableFileGroupType($("#fileGroupingParameter-fileGroupEnable").find(":selected").val());
 			});
			
			
			$("#fileGroupingParameter-fileGroupEnable").on('change', function() {
				enableFileGroupType($("#fileGroupingParameter-fileGroupEnable").find(":selected").val());
			});
			
			
			function enableFileGroupType(value){
				if(value=='false'){
					$('#fileGroupingParameter-groupingType').attr('disabled',true);
					$('#fileGroupingParameter-enableForArchivePath').attr('disabled',true);
					$('#fileGroupingParameter-sourcewiseArchive').attr('disabled',true);
				} else {
					$('#fileGroupingParameter-groupingType').attr('disabled',false);
					$('#fileGroupingParameter-enableForArchivePath').attr('disabled',false);
					$('#fileGroupingParameter-sourcewiseArchive').attr('disabled',false);
				}
			}
			
			function saveConfiguration(){
				var minFileRange  =  $("#service-minFileRange").val(); 
   				var maxFileRange = $("#service-maxFileRange").val();
   				var fileBatchSize = $("#svcExecParams-fileBatchSize").val();
   				var selectedFields = [];
   				var noFileAlert = $("#noFileAlert").val(); 
   				if(noFileAlert == '' || noFileAlert == null){
   					$("#noFileAlert").val('-1');
   				}
   				if(minFileRange == ''){
   					$("#service-minFileRange").val('-1');
   				}	
   				if(maxFileRange == ''){
   					$("#service-maxFileRange").val('-1');
   				}
   				if(fileBatchSize == ''){
   					$("#svcExecParams-fileBatchSize").val('0');
   				}
   				
   				if($("#svcExecParams-executionInterval").val() == ''){
   					$("#svcExecParams-executionInterval").val('0');
   				}
   				if($("#svcExecParams-minThread").val() == ''){
   					$("#svcExecParams-minThread").val('0');
   				}
   				if($("#svcExecParams-maxThread").val() == ''){
   					$("#svcExecParams-maxThread").val('0');
   				}
   				if($("#svcExecParams-queueSize").val().trim() == ''){
   					$("#svcExecParams-queueSize").val(0);
   				}   				
   				
       			$('#aggregation-service-configuration-form').submit();
			}
			
			function resetConfiguration(){
				resetWarningDisplay();
				$("#archivePath").val('');
				$("#minDiskSpaceRequiredGB").val('');
				$("#dateFieldForSummary").val('');
				$("#recordBatchSize").val('');
				$("#processRecordLimit").val('');
				$("#noFileAlert").val('');
				$("#fileCopyFolders").val('');
				$("#archivePath").val('');
				$("#svcExecParams-executionInterval").val(60);
				$("#svcExecParams-queueSize").val(10000);
				$("#svcExecParams-minThread").val(5);
				$("#svcExecParams-maxThread").val(10);
				$("#svcExecParams-fileBatchSize").val(10);				
				$("#service-minFileRange").val(1);
				$("#service-maxFileRange").val(300);
				$("#noFileAlert").val(10);
				$('#serviceSchedulingParams-schedulingEnabled option')[0].selected = true;
				toggleScheduleApply();
				$('#fileGroupingParameter-fileGroupEnable option')[0].selected = true;
				enableFileGroupType($("#fileGroupingParameter-fileGroupEnable").find(":selected").val());
				$('#fileGroupingParameter-sourcewiseArchive option')[0].selected = true;
				$("#delimiter").val('|');
				
			}
			
			function cancelConfiguration(){
				showButtons('aggregation_service_configuration');
				$("#aggregation_service_configuration").click();
			 
			}
		</script>
		
	</div>
	</div>


