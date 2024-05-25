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
		<form:form modelAttribute="<%=FormBeanConstants.PROCESSING_SERVICE_CONFIGURATION_FORM_BEAN%>" method="POST" action="<%= ControllerConstants.UPDATE_PROCESSING_SERVICE_CONFIGURATION %>" id="processing-service-configuration-form">
			
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
			<form:hidden path="fileGroupingParameter.id" id="id"></form:hidden>
            <form:hidden path="status" id="status" value="${status}"></form:hidden>
            
            <form:hidden path="enableFileStats" id="enableFileStats" value="${enableFileStats}"></form:hidden>
            <form:hidden path="enableDBStats" id="enableDBStats" value="${enableDBStats}"></form:hidden>            
           
            <!-- Section-0 start here -->
			<jsp:include page="../serviceBasicDetails.jsp"></jsp:include>
			<!-- section-0 end here -->

			<!-- section-2 start here -->
			<jsp:include page="../serviceExecutionParameters.jsp"></jsp:include>
			<!-- section-2 end here -->
			
			<!-- Section-2.1 start here -->
			<jsp:include page="processingExecutionParams.jsp"></jsp:include>
			<!-- section-2.1 end here -->
			
			<!-- Section-3 start here -->
			<jsp:include page="processingFileGroupingParams.jsp"></jsp:include>
			<!-- section-3 end here -->

			<!-- section-4 start here -->
			<div class="box box-warning">
				<div class="box-header with-border">
					<h3 class="box-title">
						<spring:message
							code="processingService.configuration.duplicate.record.detection" ></spring:message>
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
					<div class="box-body">
						<div class="fullwidth inline-form">
							<div class="col-md-6 no-padding">
								<spring:message
									code="processingService.configuration.purge.cache.interval"	var="tooltip" ></spring:message>
								<div class="form-group">
									<div class="table-cell-label">${tooltip}<span class="required">*</span></div>
									<div class="input-group ">
										<form:input path="acrossFileDuplicatePurgeCacheInterval" cssClass="form-control table-cell input-sm" tabindex="4" id="duplicatecheck-acrossFileDuplicatePurgeCacheInterval" data-toggle="tooltip"
											data-placement="bottom" 
											title="${tooltip }" ></form:input>
										<spring:bind path="acrossFileDuplicatePurgeCacheInterval">
											<elitecoreError:showError errorMessage="${status.errorMessage}" errorId="duplicatecheck-acrossFileDuplicatePurgeCacheInterval_error"></elitecoreError:showError>
										</spring:bind>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
				<!-- Form content end here  -->
			</div>
			<!-- section-4 end here --> 
			
			<!-- section-5 start here -->
			<div class="box box-warning">
				<div class="box-header with-border">
					<h3 class="box-title">
						<spring:message
							code="processingService.configuration.global.sequence.rule" ></spring:message>
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
					
					<div class="col-md-6 no-padding" >
							<spring:message
								code="processingService.configuration.enable.global.sequence.rule"
								var="tooltip" ></spring:message>
							<div class="form-group">
								<div class="table-cell-label">${tooltip}</div>
								<div class="input-group ">
									<form:select path="globalSeqEnabled"
										cssClass="form-control table-cell input-sm" tabindex="4"
										id="global-globalSeqEnabled"
										data-toggle="tooltip" data-placement="bottom"
										 title="${tooltip }">
										<c:forEach items="${trueFalseEnum}" var="trueFalseEnum">
											<form:option value="${trueFalseEnum.name}" >${trueFalseEnum}</form:option>
										</c:forEach>
									</form:select>
									<spring:bind path="globalSeqEnabled">
										<elitecoreError:showError
											errorMessage="${status.errorMessage}" errorId="global-globalSeqEnabled_error"></elitecoreError:showError>
									</spring:bind>
								</div>
							</div>
						</div>
							<div class="col-md-6 no-padding">
							<spring:message
								code="processingService.configuration.global.sequence.rule.device"
								var="tooltip" ></spring:message>
							<div class="form-group">
								<div class="table-cell-label">${tooltip}</div>
								<div class="input-group ">
									<form:input path="globalSeqDeviceName" cssClass="form-control table-cell input-sm" tabindex="4" id="global-globalSeqDeviceName" data-toggle="tooltip"
										data-placement="bottom" 
										title="${tooltip }" ></form:input>
									<spring:bind path="globalSeqDeviceName">
										<elitecoreError:showError errorMessage="${status.errorMessage}" errorId="global-globalSeqDeviceName_error"></elitecoreError:showError>
									</spring:bind>
								</div>
							</div>
						</div>
						
						<div class="col-md-6 no-padding">
							<spring:message code="processingService.configuration.global.sequence.rule.sequence.range"	var="tooltip" ></spring:message>
							<div class="form-group">
								<div class="table-cell-label">${tooltip}</div>
								<div class="input-group ">
									<form:input path="globalSeqMaxLimit" cssClass="form-control table-cell input-sm" tabindex="4" id="global-globalSeqMaxLimit" data-toggle="tooltip"
										data-placement="bottom" 
										title="${tooltip }" ></form:input>
									<spring:bind path="globalSeqMaxLimit">
										<elitecoreError:showError errorMessage="${status.errorMessage}" errorId="global-globalSeqMaxLimit_error"></elitecoreError:showError>
									</spring:bind>
								</div>
							</div>
						</div>
									
						
			</div>
			</div>
			</div>
			<!-- section-5 end here -->
			<div class="box box-warning">
					<div class="box-header with-border">
						<h3 class="box-title">
							<spring:message code="parsing.service.config.cdr.date.summary.popup.header" ></spring:message>
						</h3>
						<div class="box-tools pull-right" style="top: -3px;">
							<button class="btn btn-box-tool" data-widget="collapse">
								<i class="fa fa-minus"></i>
							</button>
						</div>
					</div>
					
					
					<div class="box-body">
						<div class="fullwidth inline-form">
							<div class="col-md-6 no-padding">
								<spring:message code="parsing.service.config.enable.cdr.summary" var="tooltip" ></spring:message>
								<div class="form-group">
									<div class="table-cell-label">${tooltip}</div>
									<div class="input-group ">
										<form:select path="storeCDRFileSummaryDB" cssClass="form-control table-cell input-sm" tabindex="4" id="service-storeCDRFileSummaryDB" data-toggle="tooltip" data-placement="bottom"  title="${tooltip}" onchange="enableCDRSummaryOption(this.value);">
											<c:forEach var="trueFalseEnum" items="${trueFalseEnum}">
		                             			<form:option value="${trueFalseEnum.name}">${trueFalseEnum}</form:option>
		                             		</c:forEach>
										</form:select>
										<spring:bind path="storeCDRFileSummaryDB">
											<elitecoreError:showError errorMessage="${status.errorMessage}" errorId="service-storeCDRFileSummaryDB_error"></elitecoreError:showError>
										</spring:bind>
									</div>
								</div>
							</div>
							<div class="col-md-6 no-padding">
								<spring:message code="parsing.service.config.mapped.unified.field" var="tooltip" ></spring:message>
								<div class="form-group">
									<div class="table-cell-label">${tooltip}</div>
									<div class="input-group ">
										<jsp:include page="../../common/formAutocomplete.jsp">
											<jsp:param name="unifiedField" value="service-dateFieldForSummary" ></jsp:param>
											<jsp:param name="unifiedFieldPath" value="dateFieldForSummary" ></jsp:param>
										</jsp:include>
										<%-- <form:select path="dateFieldForSummary" cssClass="form-control table-cell input-sm" tabindex="4" id="service-dateFieldForSummary" data-toggle="tooltip" data-placement="bottom"  title="${tooltip }">
											<c:forEach items="${unifiedFieldEnum}" var="unifiedFieldEnum" >
		                             			<form:option value="${unifiedFieldEnum}">${unifiedFieldEnum}</form:option>
											</c:forEach>
										</form:select> --%>
										<spring:bind path="dateFieldForSummary">
											<elitecoreError:showError errorMessage="${status.errorMessage}" errorId="service-dateFieldForSummary_error"></elitecoreError:showError>
										</spring:bind>
									</div>
								</div>
							</div>
							<div class="col-md-6 no-padding">
								<spring:message code="parsing.service.config.summary.type" var="tooltip" ></spring:message>
								<div class="form-group">
									<div class="table-cell-label">${tooltip}</div>
									<div class="input-group ">
										<form:select path="typeForSummary" cssClass="form-control table-cell input-sm" tabindex="4" id="service-typeForSummary" data-toggle="tooltip" data-placement="bottom"  title="${tooltip }">
											<c:forEach var="cdrDataSummaryTypeEnum" items="${cdrDataSummaryTypeEnum}">
		                             			<form:option value="${cdrDataSummaryTypeEnum}">${cdrDataSummaryTypeEnum}</form:option>
		                             		</c:forEach>
										</form:select>
										<spring:bind path="typeForSummary">
											<elitecoreError:showError errorMessage="${status.errorMessage}" errorId="service-typeForSummary_error"></elitecoreError:showError>
										</spring:bind>
									</div>
								</div>
							</div>
							<div class="col-md-6 no-padding">
								<spring:message code="parsing.service.config.override.file.date" var="tooltip" ></spring:message>
								<div class="form-group">
									<div class="table-cell-label">${tooltip}</div>
									<div class="input-group ">
										<form:select path="overrideFileDateEnabled" cssClass="form-control table-cell input-sm" tabindex="4" id="service-overrideFileDateEnabled" data-toggle="tooltip" data-placement="bottom"  title="${tooltip }">
											<c:forEach var="trueFalseEnum" items="${trueFalseEnum}">
		                             			<form:option value="${trueFalseEnum.name}">${trueFalseEnum}</form:option>
		                             		</c:forEach>
										</form:select>
										<spring:bind path="overrideFileDateEnabled">
											<elitecoreError:showError errorMessage="${status.errorMessage}" errorId="service-overrideFileDateEnabled_error"></elitecoreError:showError>
										</spring:bind>
									</div>
								</div>
							</div>
							<div class="col-md-6 no-padding">
								<spring:message code="parsing.service.config.override.type" var="tooltip" ></spring:message>
								<div class="form-group">
									<div class="table-cell-label">${tooltip}<i class='fa fa-square'></i></div>
									<div class="input-group ">
										<form:select path="overrideFileDateType" cssClass="form-control table-cell input-sm" tabindex="4" id="service-overrideFileDateType" data-toggle="tooltip" data-placement="bottom"  title="${tooltip }">
											<c:forEach items="${overrideFileDateTypeEnum}" var="overrideFileDateTypeEnum" >
		                             			<form:option value="${overrideFileDateTypeEnum}">${overrideFileDateTypeEnum}</form:option>
											</c:forEach>
										</form:select>
										<spring:bind path="overrideFileDateType">
											<elitecoreError:showError errorMessage="${status.errorMessage}" errorId="service-overrideFileDateType_error"></elitecoreError:showError>
										</spring:bind>
									</div>
								</div>
							</div>
						</div>
					</div>
					
				</div>
			
			
					
						
			</div>
			</div>
			</div>
		</form:form>
		
		<script type="text/javascript" >
			$(document).ready(function() {
				enableFileGroupType($("#fileGroupingParameter-fileGroupEnable").find(":selected").val());
				enableGlobalSeqParams($("#global-globalSeqEnabled").find(":selected").val());
				var enableCDRSummary=$("#service-storeCDRFileSummaryDB").find(":selected").val();
       				enableCDRSummaryOption(enableCDRSummary);
 			});
			
			
			$("#fileGroupingParameter-fileGroupEnable").on('change', function() {
				enableFileGroupType($("#fileGroupingParameter-fileGroupEnable").find(":selected").val());
			});
			
			$("#global-globalSeqEnabled").on('change', function() {
				enableGlobalSeqParams($("#global-globalSeqEnabled").find(":selected").val());
			});
			
			
			function enableGlobalSeqParams(value){
				if(value == 'false'){
					$('#global-globalSeqDeviceName').attr('disabled',true);
					$('#global-globalSeqMaxLimit').attr('disabled',true);
				}else{
					$('#global-globalSeqDeviceName').attr('disabled',false);
					$('#global-globalSeqMaxLimit').attr('disabled',false);
				}
			}
			
			function enableFileGroupType(value){
				if(value=='false'){
					$('#fileGroupingParameter-groupingType').attr('disabled',true);
					/**Edited : Following elements will be disabled while Enable File Grouping will be false **/
					$('#fileGroupingParameter-enableForArchivePath').attr('disabled',true);
					$('#fileGroupingParameter-enableForFilterPath').attr('disabled',true);
					$('#fileGroupingParameter-enableForInvalidPath').attr('disabled',true);
					$('#fileGroupingParameter-enableForDuplicatePath').attr('disabled',true);
					$('#fileGroupingParameter-sourcewiseArchive').attr('disabled',true);
					$('#fileGroupingParameter-groupingDateType').attr('disabled',true);
					
					/** MED-5838 **/
					$('#fileGroupingParameter-enableForArchive').attr('disabled',true);
					$('#fileGroupingParameter-enableForFilter').attr('disabled',true);
					$('#fileGroupingParameter-enableForInvalid').attr('disabled',true);
					$('#fileGroupingParameter-enableForDuplicate').attr('disabled',true);
					$('#fileGroupingParameter-filterGroupType').attr('disabled',true);
				} else {
					$('#fileGroupingParameter-groupingType').attr('disabled',false);
					/**Edited : Following elements will be enabled while Enable File Grouping will be true **/
					$('#fileGroupingParameter-enableForArchivePath').attr('disabled',false);
					$('#fileGroupingParameter-enableForFilterPath').attr('disabled',false);
					$('#fileGroupingParameter-enableForInvalidPath').attr('disabled',false);
					$('#fileGroupingParameter-enableForDuplicatePath').attr('disabled',false);
					$('#fileGroupingParameter-sourcewiseArchive').attr('disabled',false);
					$('#fileGroupingParameter-groupingDateType').attr('disabled',false);
					
					/** MED-5838 **/
					$('#fileGroupingParameter-enableForArchive').attr('disabled',false);
					$('#fileGroupingParameter-enableForFilter').attr('disabled',false);
					$('#fileGroupingParameter-enableForInvalid').attr('disabled',false);
					$('#fileGroupingParameter-enableForDuplicate').attr('disabled',false);
					$('#fileGroupingParameter-filterGroupType').attr('disabled',false);
				}
			}
			
			function enableCDRSummaryOption(value){
				if(value=='false'){
					$('#service-dateFieldForSummary').attr('disabled',true);
					$('#service-typeForSummary').attr('disabled',true);
					$('#service-overrideFileDateEnabled').attr('disabled',true);
					$('#service-overrideFileDateType').attr('disabled',true);
				} else {
					$('#service-dateFieldForSummary').attr('disabled',false);
					$('#service-typeForSummary').attr('disabled',false);
					$('#service-overrideFileDateEnabled').attr('disabled',false);
					$('#service-overrideFileDateType').attr('disabled',false);
					}
				}
			
			function showAcrossFileParam(){
				
				var schType=$("#duplicateRecordPolicyType").find(":selected").text();
				
				if(schType == 'ACROSS_FILE'){
					$("#acrossFileParams").show();
				}else{
					$("#acrossFileParams").hide();
				}
			}
			
			function saveConfiguration(){
				var minFileRange  =  $("#service-minFileRange").val(); 
   				var maxFileRange = $("#service-maxFileRange").val();
   				var globalSeqMaxLimit=$("#global-globalSeqMaxLimit").val();
   				/* var unifiedFields = $('#duplicatecheck-unifiedFields option:selected'); */
   				var selectedFields = [];
   				var noFileAlert = $("#noFileAlert").val(); 
   				if(noFileAlert == '' || noFileAlert == null){
   					$("#noFileAlert").val('-1');
   				}
   				if(minFileRange === ''){
   					$("#service-minFileRange").val('-1');
   				}	
   				if(maxFileRange === ''){
   					$("#service-maxFileRange").val('-1');
   				}
   				if(globalSeqMaxLimit === ''){
   					$("#global-globalSeqMaxLimit").val('-1');
   				}
   				$('#processing-service-configuration-form').submit();
			}
			
			function resetConfiguration(){
				resetWarningDisplay();
				$("#svcExecParams-executionInterval").val('');
				$("#svcExecParams-minThread").val('');
				$("#svcExecParams-maxThread").val('');
				$("#svcExecParams-fileBatchSize").val('');
				$("#archivePath").val('');
				$("#minDiskSpaceRequiredGB").val('');
				$("#dateFieldForSummary").val('');
				$("#svcExecParams-queueSize").val('');
				$("#service-minFileRange").val('');
				$("#service-maxFileRange").val('');
				$("#recordBatchSize").val('');
				$("#processRecordLimit").val('');
				$("#noFileAlert").val('');
				$("#fileCopyFolders").val('');
				$("#archivePath").val('');
				$("#duplicatecheck-acrossFileDuplicatePurgeCacheInterval").val('');
				$("#globalSeqDeviceName").val('');
				$("#globalSeqMaxLimit").val('');
				
				
			}
			
			function cancelConfiguration(){
				showButtons('processing_service_summary');
				$("#processing_service_summary").click();
			 
			}
		</script>
		
	</div>
	</div>


