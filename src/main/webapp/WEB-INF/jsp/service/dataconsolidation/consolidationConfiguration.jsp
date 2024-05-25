<%@page import="com.elitecore.sm.common.constants.FormBeanConstants"%>
<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<%@taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="/WEB-INF/ShowErrorTag.tld" prefix="elitecoreError"%>

<div class="box-body padding0 mtop10">
	<div class="fullwidth">
		<form:form modelAttribute="<%=FormBeanConstants.DATA_CONSOLIDATION_SERVICE_CONFIGURATION_FORM_BEAN %>" method="POST" action="<%=ControllerConstants.UPDATE_CONSOLIDATION_SERVICE_CONFIGURATION %>" id="<%=FormBeanConstants.DATA_CONSOLIDATION_SERVICE_CONFIGURATION_FORM_BEAN %>">

			<form:hidden path="serverInstance.id" id="serverInstanceId" value="${instanceId}"></form:hidden>
			<input type="hidden" name="serviceName" id="serviceName" value="${serviceName}"/>
			<form:hidden path="id" id="serviceId" value="${serviceId}"></form:hidden>
			<input type="hidden" id="serviceInstanceId" name="serviceInstanceId" value="${serviceInstanceId}">
			<form:hidden path="servInstanceId" id="servInstanceId" value="${servInstanceId}"></form:hidden>
			<form:hidden path="svctype.id" id="svctypeId" value="${svctype.id}"></form:hidden>
			<form:hidden path="svctype.type" id="svctypetype" value="${svctype.type}"></form:hidden>
			<form:hidden path="svctype.serviceCategory" id="svctypecategory" value="${svctype.serviceCategory}"></form:hidden>
			<form:hidden path="svctype.typeOfService" id="svctypeOfService" value="${svctype.typeOfService}"></form:hidden>
			<form:hidden path="svctype.description" id="svctypedescription" value="${svctype.description}"></form:hidden>
			<form:hidden path="svctype.alias" id="svctypealias" value="${svctype.alias}"></form:hidden>
			<form:hidden path="svctype.serviceFullClassName" id="svctypeserviceFullClassName" value="${svctype.serviceFullClassName}"></form:hidden>
			<form:hidden path="fileGroupParam.id" id="id"></form:hidden>
			<form:hidden path="enableFileStats" id="enableFileStats" value="${enableFileStats}"></form:hidden>
			<form:hidden path="enableDBStats" id="enableDBStats" value="${enableDBStats}"></form:hidden>
			<form:hidden path="status" id="status" value="${status}"></form:hidden>
			<!-- Section-1 start here -->
			<jsp:include page="../serviceBasicDetails.jsp"></jsp:include>
			<!-- section-1 end here -->
			
			<!-- Section-2 start here -->
			<jsp:include page="../serviceExecutionParameters.jsp"></jsp:include>
			<!-- section-2 end here -->
			
			<!-- Section-3 start here -->
			<jsp:include page="consolidationServiceParameters.jsp"></jsp:include>
			<!-- section-3 end here -->
			
			<!-- Section-4 start here -->
			<jsp:include page="../serviceFileGroupParameters.jsp"></jsp:include>
			<!-- section-4 end here -->

			<div class="box box-warning">
				<div class="box-header with-border">
					<h3 class="box-title">
						<spring:message code="consolidation.service.config.consolidation.detail.header" ></spring:message>
					</h3>
					<div class="box-tools pull-right" style="top: -3px;">
						<button class="btn btn-box-tool" data-widget="collapse">
							<i class="fa fa-minus"></i>
						</button>
					</div>
					<!-- /.box-tools -->
				</div>
				<!-- /.box-header end here -->
				<!-- Form content start here  -->
				<div class="box-body">
					<div class="fullwidth inline-form">
					
					<div class="col-md-6 no-padding">
							<spring:message code="consolidation.service.config.consolidation.detail.cons.type.label" var="label" ></spring:message>
							<spring:message code="consolidation.service.config.consolidation.detail.cons.type.tooltip" var="tooltip" ></spring:message>
							<div class="form-group">
								<div class="table-cell-label">${label}<span class="required">*</span></div>
								<div class="input-group ">
									<form:select path="consolidationType"
										cssClass="form-control table-cell input-sm" tabindex="4"
										id="consolidationType"
										data-toggle="tooltip" data-placement="bottom"
										 title="${tooltip }" onchange="changeScheduleParams()">
										<c:forEach items="${consolidationType}" var="consolidationType">
											<form:option value="${consolidationType}">${consolidationType}</form:option>
										</c:forEach>
									</form:select>
									<spring:bind path="consolidationType">
										<elitecoreError:showError
											errorMessage="${status.errorMessage}" errorId="consolidationType_error"></elitecoreError:showError>
									</spring:bind>
								</div>
							</div>
						</div>
						
						<div class="col-md-6 no-padding">
							<spring:message code="consolidation.service.config.consolidation.detail.processing.type.label" var="label" ></spring:message>
							<spring:message code="consolidation.service.config.consolidation.detail.processing.type.label.tooltip" var="tooltip" ></spring:message>
							<div class="form-group">
								<div class="table-cell-label">${label}<span class="required">*</span></div>
								<div class="input-group ">
									<form:select path="acrossFileProcessingType"
										cssClass="form-control table-cell input-sm" tabindex="4"
										id="acrossFileProcessingType"
										data-toggle="tooltip" data-placement="bottom"
										 title="${tooltip }">
										<c:forEach items="${acrossFileProcessingType}" var="acrossFileProcessingType">
											<form:option value="${acrossFileProcessingType}">${acrossFileProcessingType}</form:option>
										</c:forEach>
									</form:select>
									<spring:bind path="svcExecParams.startupMode">
										<elitecoreError:showError
											errorMessage="${status.errorMessage}" errorId="acrossFileProcessingType_error"></elitecoreError:showError>
									</spring:bind>
								</div>
							</div>
						</div> 
						
						<div class="col-md-6 no-padding">
							<spring:message
								code="consolidation.service.config.consolidation.detail.file.partition.label"
								var="name" ></spring:message>
							<spring:message
								code="consolidation.service.config.consolidation.detail.file.partition.label.tooltip"
								var="tooltip" ></spring:message>
							<div class="form-group">
								<div class="table-cell-label">${name}</div>
								<div class="input-group ">
									<form:input path="acrossFilePartition"
										cssClass="form-control table-cell input-sm" tabindex="4"
										id="acrossFilePartition" data-toggle="tooltip"
										data-placement="bottom" 
										title="${tooltip }"></form:input>
									<spring:bind path="acrossFilePartition">
										<elitecoreError:showError
											errorMessage="${status.errorMessage}" errorId="acrossFilePartition_error"></elitecoreError:showError>
									</spring:bind>
								</div>
							</div>
						</div>
						
						<div class="col-md-6 no-padding">
						
							
								<spring:message code="consolidation.service.config.consolidation.detail.file.batch.size.label" var="tooltip"></spring:message>
		                    	<spring:message code="consolidation.service.config.consolidation.detail.file.batch.minsize.label.tooltip" var="minTooltip"></spring:message>
		                    	<spring:message code="consolidation.service.config.consolidation.detail.file.batch.maxsize.label.tooltip" var="maxTooltip"></spring:message>
								<div class="table-cell-label left" style="float:left">${tooltip}<span class="required">*</span></div>
							
							
							<div class="input-group">
							<div class="col-md-6 no-padding">
								<div class="form-group">
		                          	<div class="input-group">
		                          		<form:input path="acrossFileMinBatchSize" id="acrossFileMinBatchSize" cssClass="form-control table-cell input-sm" data-toggle="tooltip" data-placement="bottom" title="${minTooltip}" onkeydown='isNumericOnKeyDown(event)'></form:input>
	        							<spring:bind path="acrossFileMinBatchSize">
											<elitecoreError:showError errorMessage="${status.errorMessage}" errorId="acrossFileMinBatchSize_error"></elitecoreError:showError>
										</spring:bind> 
		                          	</div>
		                         </div> 	
							</div>
							<div class="col-md-6 no-padding">
								<div class="form-group">
		                          	<div class="input-group"> 
		                          		<form:input path="acrossFileMaxBatchSize" id="acrossFileMaxBatchSize" cssClass="form-control table-cell input-sm" data-toggle="tooltip" data-placement="bottom" title="${maxTooltip}" onkeydown='isNumericOnKeyDown(event)' ></form:input>
	        							<spring:bind path="acrossFileMaxBatchSize">
											<elitecoreError:showError errorMessage="${status.errorMessage}" errorId="acrossFileMaxBatchSize_error"></elitecoreError:showError>
										</spring:bind>
		                          	</div>
		                         </div> 	
							</div>
							</div>
		                    	
        				</div>
						
					</div>
				</div>
			</div>
							            
		</form:form>
		<script type="text/javascript">
			
			
			function saveConfiguration(){
				var noFileAlert = $("#noFileAlertInterval").val(); 
   				if(noFileAlert == '' || noFileAlert == null){
   					$("#noFileAlertInterval").val('-1');
   				}
				
				if($("#acrossFileMinBatchSize").val() == ''){
					$("#acrossFileMinBatchSize").val("-1")
				}
				if($("#acrossFileMaxBatchSize").val() == ''){
					$("#acrossFileMaxBatchSize").val("-1")
				}
				if($("#acrossFilePartition").val() === ''){
					$("#acrossFilePartition").val('-1');
				}
				
				if($("#con-service-minFileRange").val() === ''){
					$("#con-service-minFileRange").val('-1');
				}
				if($("#con-service-maxFileRange").val() === ''){
					$("#con-service-maxFileRange").val('-1');
				}
				
     			$("#data_consolidation_service_configuration_form_bean").submit();
			 }
			
			function resetConfiguration(){
				resetWarningDisplay();
				clearAllMessages();
			
				$("#name").val('');
				$("#description").val('');
				$("#svcExecParams-executionInterval").val('');
				$("#svcExecParams-minThread").val('');
				$("#svcExecParams-maxThread").val('');
				$("#svcExecParams-fileBatchSize").val('');
				$("#svcExecParams-queueSize").val('');
				$("#serviceSchedulingParams-time").val('');
				$("#parsing-service-configuration-fileGroupEnable").val('true');
				$("#parsing-service-configuration-groupingType").val('DAY');
				$("#data-service-configuration-archivePath").val('');
				$("#data-service-configuration-sourcewiseArchive").val('true');
				$("#acrossFilePartition").val('100');
				$("#acrossFileMinBatchSize").val('5');
				$("#acrossFileMaxBatchSize").val('15');
			}
			
			function cancelConfiguration(){
				showButtons('consolidation_service_summary');
				$("#consolidation_service_summary").click();
			}
			
			function changeScheduleParams(){
				
				var schType=$("#consolidationType").find(":selected").text();
				if(schType == 'IN_FILE'){
					
					$("#acrossFilePartition").prop("disabled", true);
					$("#acrossFileMinBatchSize").prop("disabled", true);
					$("#acrossFileMaxBatchSize").prop("disabled", true);
					$("#acrossFileProcessingType").prop("disabled", true);
			 	}
				else{
					$("#acrossFilePartition").prop("disabled", false);
					$("#acrossFileMinBatchSize").prop("disabled", false);
					$("#acrossFileMaxBatchSize").prop("disabled", false);
					$("#acrossFileProcessingType").prop("disabled", false);
				}
			}
			
			function toggleGroupParam(){
				if($("#parsing-service-configuration-fileGroupEnable").val() == 'true'){
					$("#parsing-service-configuration-groupingType").prop("disabled", false);
					$("#data-service-configuration-sourcewiseArchive").prop("disabled", false);
					$("#data-service-configuration-sourcewiseArchive").prop("disabled", false);
					$("#data-service-configuration-archivePath").prop("disabled", false);
				}else{
					$("#parsing-service-configuration-groupingType").prop("disabled", true);
					$("#data-service-configuration-sourcewiseArchive").prop("disabled", true);
					$("#data-service-configuration-archivePath").prop("disabled", true);
				}
			}
			
			$(document).ready(function() {
				changeScheduleParams();
				toggleGroupParam();
				$("#parsing-service-configuration-fileGroupEnable").change(toggleGroupParam);					
     		});
		</script>
	</div>
</div>
