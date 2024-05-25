<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="/WEB-INF/ShowErrorTag.tld" prefix="elitecoreError"%>

<div class="box-body padding0 mtop10">
	<div class="fullwidth">
		<form:form modelAttribute="distribution_service_configuration_form_bean"	method="POST" action="<%=ControllerConstants.UPDATE_DISTRIBUTION_SERVICE_CONFIGURATION%>"	id="distribution-service-configuration-form">
			<form:hidden path="serverInstance.id" id="serverInstanceId" value="${instanceId}"></form:hidden>
			<input type="hidden" name="serviceName" id="serviceName" value="${serviceName}"/>
			<form:hidden path="id" id="serviceId" value="${serviceId}"></form:hidden>
			<form:hidden path="servInstanceId" id="servInstanceId" value="${servInstanceId}"></form:hidden>
			<form:hidden path="svctype.id" id="svctypeId" value="${svctype.id}"></form:hidden>
			<form:hidden path="svctype.type" id="svctypetype" value="${svctype.type}"></form:hidden>
			<form:hidden path="svctype.serviceCategory" id="svctypecategory" value="${svctype.serviceCategory}"></form:hidden>
			<form:hidden path="svctype.typeOfService" id="svctypeOfService" value="${svctype.typeOfService}"></form:hidden>
			<form:hidden path="svctype.description" id="svctypedescription" value="${svctype.description}"></form:hidden>
			<form:hidden path="svctype.alias" id="svctypealias" value="${svctype.alias}"></form:hidden>
			<form:hidden path="svctype.serviceFullClassName" id="svctypeserviceFullClassName" value="${svctype.serviceFullClassName}"></form:hidden>
			<form:hidden path="serviceSchedulingParams.id" id="serviceSchedulingParamsid" value="${serviceSchedulingParams.id}"></form:hidden>
			<form:hidden path="fileGroupingParameter.id" id="id"></form:hidden>
			<form:hidden path="status" id="status" value="${status}"></form:hidden>
			<form:hidden path="enableFileStats" id="enableFilestatusid" value="${enableFileStats}"></form:hidden>
			<form:hidden path="enableDBStats" id = "enableDBStatsid" value = "${enableDBStats}"></form:hidden>
			
			<!-- Section-1 start here -->
			<jsp:include page="../serviceBasicDetails.jsp"></jsp:include>
			<!-- section-1 end here -->
			
			<!-- Section-2 start here -->
			<jsp:include page="../serviceExecutionParameters.jsp"></jsp:include>
			<!-- section-2 end here -->
			
			<!-- Section-3 start here -->
			<jsp:include page="distributionServiceParams.jsp"></jsp:include>
			<!-- section-3 end here -->
			
			<!-- Section-4 start here -->
			<jsp:include page="../serviceSchedulingParams.jsp"></jsp:include>
			<!-- section-4 end here -->
			
			<!-- Section-5 start here -->
				<jsp:include page="../fileGroupingParameters.jsp"></jsp:include>
			<!-- section-5 end here -->
			
				
		</form:form>
		
		<script type="text/javascript">
			
			function saveConfiguration(){
				$("#distribution_driver_configuration").click();
				
			 }
			function resetConfiguration(){
				$("#svcExecParams-executionInterval").val('');
				$("#minThread").val('');
				$("#maxThread").val('');
				$("#fileBatchSize").val('');
				$("#svcExecParams-queueSize").val('');
				$("#minDiskSpaceRequiredGB").val('');
				$("#processRecordLimit").val('');
				$("#writeRecordLimit").val('');
				$("#timestenDatasourceName").val('');
				$("#archivePath").val('');
				$("#serviceSchedulingParams-time").val('');
				
			}
			function cancelConfiguration(){
				showButtons('collection_service_summary');
				$("#distribution_service_summary").click();
			}
		</script>
	</div>
</div>		
	
