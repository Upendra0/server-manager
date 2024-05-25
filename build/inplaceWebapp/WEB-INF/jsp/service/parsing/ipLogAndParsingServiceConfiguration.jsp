<%@page import="com.elitecore.sm.common.constants.EngineConstants"%>
<%@page import="com.elitecore.sm.common.model.TrueFalseEnum"%>
<%@page import="com.elitecore.sm.common.constants.FormBeanConstants"%>
<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="/WEB-INF/ShowErrorTag.tld" prefix="elitecoreError"%>

<script type="text/javascript" src="${pageContext.request.contextPath}/js/bootstrap-multiselect.js"></script>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap-multiselect.css" type="text/css"/>

<style>
.customLink:hover{
 	color:white;!important
 }
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

<script>


function enableArchiveOption(value){
	if(value=='false'){
		$('#fileGroupingParameter-archivePath').attr('readonly',true);
	} else {
		$('#fileGroupingParameter-archivePath').attr('readonly',false);
	}
}
function enableFileStateOption(value){
	
	if(value=='false'){
		$('#service-fileStatsLoc').prop('readonly',true);
	} else {
		$('#service-fileStatsLoc').prop('readonly',false);
	}
}
function enableFileGroupOptionForIplog(value){
	
	if(value=='false'){
		$('#fileGroupingParameter-groupingType').attr('disabled',true);
		$('#fileGroupingParameter-enableForArchive').val('false');
		$('#fileGroupingParameter-enableForArchive').attr('disabled',true);
		$('#fileGroupingParameter-archivePath').attr('readonly',true);
	} else {
		$('#fileGroupingParameter-groupingType').attr('disabled',false);
		$('#fileGroupingParameter-enableForArchive').val('true');
		$('#fileGroupingParameter-enableForArchive').attr('disabled',true);
		$('#fileGroupingParameter-archivePath').attr('readonly',false);
	}
}
function enableFileGroupOptionForParsing(value){
	
	if(value=='false'){
		$('#fileGroupingParameter-groupingType').attr('disabled',true);
		$('#fileGroupingParameter-sourcewiseArchive').attr('disabled',true);
		$('#fileGrouping-archivePath').attr('disabled',true);
		$('#fileGroupingParameter-groupingDateType').attr('disabled',true);
	} else {
		$('#fileGroupingParameter-groupingType').attr('disabled',false);
		$('#fileGroupingParameter-sourcewiseArchive').attr('disabled',false);
		$('#fileGrouping-archivePath').attr('disabled',false);
		$('#fileGroupingParameter-groupingDateType').attr('disabled',false);
	}
}
function enableCorrelationOption(value){
	if(value=='false'){
		$('#service-mappedSourceField').attr('disabled',true);
		$('#service-destPortField').prop('disabled',true);
		$('#service-destPortFilter').prop('disabled',true);
		$('#service-createRecDestPath').prop('readonly',true);
		$('#service-deleteRecDestPath').prop('readonly',true);
	} else {
		$('#service-mappedSourceField').attr('disabled',false);
		$('#service-destPortField').prop('disabled',false);
		$('#service-destPortFilter').prop('disabled',false);
		$('#service-createRecDestPath').prop('readonly',false);
		$('#service-deleteRecDestPath').prop('readonly',false);
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

function enablefileSplit(value){
	if(value=='false'){
		$('#service-batchSize').attr('readonly',true);
		
	} else {
		$('#service-batchSize').attr('readonly',false);
		
	}
}

function clearFiltervalue(val){
	if(!val.value){
		$('#service-equalCheckValue').val('')
	}
}

</script>

<div class="box-body padding0 mtop10">
	<div class="fullwidth">
			
		<form:form modelAttribute="<%= FormBeanConstants.SERVICE_CONFIG_FORM_BEAN %>" method="POST" id="iplog-parsing-service-configuration-form">

			<form:hidden path="serverInstance.id" id="serverInstanceId" value="${instanceId}"></form:hidden>
			<input type="hidden" name="serviceName" id="serviceName" value="${serviceName}"/>
			<form:hidden path="id" id="serviceId" value="${serviceId}"></form:hidden>
			<form:hidden path="servInstanceId" id="servInstanceId" value="${servInstanceId}"></form:hidden>
			<input type="hidden" id="serviceInstanceId" name="serviceInstanceId" value="${serviceInstanceId}">
			<form:hidden path="svctype.id" id="svctypeId" value="${svctype.id}"></form:hidden>
			<form:hidden path="svctype.type" id="svctypetype" value="${svctype.type}"></form:hidden>
			<form:hidden path="svctype.serviceCategory" id="svctypecategory" value="${svctype.serviceCategory}"></form:hidden>
			<form:hidden path="svctype.typeOfService" id="svctypeOfService" value="${svctype.typeOfService}"></form:hidden>
			<form:hidden path="svctype.description" id="svctypedescription" value="${svctype.description}"></form:hidden>
			<form:hidden path="svctype.alias" id="svctypealias" value="${svctype.alias}"></form:hidden>
			<form:hidden path="svctype.serviceFullClassName" id="svctypeserviceFullClassName" value="${svctype.serviceFullClassName}"></form:hidden>
			<form:hidden path="fileGroupingParameter.id" id="id"></form:hidden>
			<form:input type="hidden" path="enableDBStats" id="enableDBStats" value="${enableDBStats}"></form:input>
			<form:input type="hidden" path="enableFileStats" id="enableFileStats" value="${enableFileStats}"></form:input>
			<form:hidden path="status" id="status" value="${status}"></form:hidden>
			<form:hidden path="enableFileStats" id="enableFilestatusid" value="${enableFileStats}"></form:hidden>
			<form:hidden path="enableDBStats" id = "enableDBStatsid" value = "${enableDBStats}"></form:hidden>
			
			
			<!-- Section-1 start here -->
			<jsp:include page="../serviceBasicDetails.jsp"></jsp:include>
			<!-- section-1 end here -->
			
			<!-- section-2 start here -->
			<jsp:include page="../serviceExecutionParameters.jsp"></jsp:include>
			<!-- section-2 end here -->
			
			<!-- Section-3 start here -->
			<div class="box box-warning">
				<div class="box-header with-border">
					<h3 class="box-title">
						<spring:message code="service.operational.param.section.heading" ></spring:message>
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
				<div class="box-body rightboxtooltip">
					<div class="fullwidth inline-form">
					
						<c:if test="${(serviceType eq 'PARSING_SERVICE')}"> 
						<div class="col-md-6 no-padding rightpart">
							<spring:message code="iplog.parsing.service.config.record.batch.size" var="tooltip" ></spring:message>
							<div class="form-group">
								<div class="table-cell-label">${tooltip}<span class="required">*</span></div>
								<div class="input-group ">
									<form:input path="recordBatchSize" cssClass="form-control table-cell input-sm" tabindex="4" id="service-batchSize" data-toggle="tooltip" data-placement="bottom" title="${tooltip }" onkeydown='isNumericOnKeyDown(event)'></form:input>
									<spring:bind path="recordBatchSize">
										<elitecoreError:showError errorMessage="${status.errorMessage}" errorId="service-batchSize_error"></elitecoreError:showError>
									</spring:bind>
								</div>
							</div>
						</div>
						</c:if>
						<c:if test="${(serviceType eq 'IPLOG_PARSING_SERVICE')}">
							<div class="col-md-6 no-padding rightpart">
								<spring:message code="iplog.parsing.service.config.record.batch.size" var="tooltip" ></spring:message>
								<div class="form-group">
									<div class="table-cell-label">${tooltip}<span class="required">*</span></div>
									<div class="input-group ">
										<form:input path="recordBatchSize" cssClass="form-control table-cell input-sm" tabindex="4" id="ipLog-service-batchSize" data-toggle="tooltip" data-placement="bottom" title="${tooltip }" onkeydown='isNumericOnKeyDown(event)' ></form:input>
										<spring:bind path="recordBatchSize">
											<elitecoreError:showError errorMessage="${status.errorMessage}" errorId="ipLog-service-batchSize_error"></elitecoreError:showError>
										</spring:bind>
									</div>
								</div>
							</div>
						</c:if>
						<c:if test="${(serviceType eq 'IPLOG_PARSING_SERVICE')}">
							<input type='hidden' value='${outputFileHeader}' id='unifiedFields'/>
							<div class='col-md-6 no-padding'>
							    <spring:message code='iplog.parsing.service.config.output.file.header' var='tooltip' ></spring:message>
							    <div class='form-group'>
							       	<div class='table-cell-label'>${tooltip}<span class='required'></span></div>
							       	<div class="input-group ">
										<form:select multiple="multiple" path="outputFileHeader" cssClass="form-control table-cell input-sm" tabindex="4" id="outputFileHeader" data-toggle="tooltip" data-placement="bottom"  title="${tooltip }">
											<c:forEach var="unifiedField" items="${unifiedFieldList}">
			                             		<form:option value="${unifiedField}">${unifiedField}</form:option>
			                             	</c:forEach>										
										</form:select>
										<spring:bind path="outputFileHeader">
											<elitecoreError:showError errorMessage="${status.errorMessage}" errorId="outputFileHeader_error"></elitecoreError:showError>
										</spring:bind>
									</div>
								</div>
							</div>
						</c:if>
						
						<c:if test="${serviceType eq 'PARSING_SERVICE'}">
							<div class="col-md-6 no-padding">
								<spring:message code="parsing.service.config.file.range" var="range" ></spring:message>
								<spring:message code="parsing.service.config.min.file.range" var="minRange" ></spring:message>
								<spring:message code="parsing.service.config.max.file.range" var="maxRange" ></spring:message>
								<div class="form-group">
									<div class="table-cell-label">${range}</div>
									<div class="input-group ">
										<form:input path="minFileRange" id="service-minFileRange" cssClass="form-control table-cell input-sm" data-toggle="tooltip" data-placement="bottom" title="${minRange}" onkeydown='isNumericOnKeyDown(event)'></form:input>
	        							<spring:bind path="minFileRange">
											<elitecoreError:showError errorMessage="${status.errorMessage}" errorId="service-minFileRange_error"></elitecoreError:showError>
										</spring:bind> 
										<form:input path="maxFileRange" id="service-maxFileRange" cssClass="form-control table-cell input-sm" data-toggle="tooltip" data-placement="bottom" title="${maxRange}" onkeydown='isNumericOnKeyDown(event)' ></form:input>
	        							<spring:bind path="maxFileRange">
											<elitecoreError:showError errorMessage="${status.errorMessage}" errorId="service-maxFileRange_error"></elitecoreError:showError>
										</spring:bind>
									</div>
								</div>
							</div>
							<div class="col-md-6 no-padding">
								<spring:message code="parsing.service.config.enable.file.seq" var="tooltip" ></spring:message>
								<div class="form-group">
									<div class="table-cell-label">${tooltip}<span class="required">*</span></div>
									<div class="input-group ">
										<form:select path="fileSeqOrderEnable" cssClass="form-control table-cell input-sm" tabindex="4" id="service-fileStatsEnabled" data-toggle="tooltip" data-placement="bottom"  title="${tooltip }" onchange="enableFileStateOption(this.value);">
											<c:forEach var="trueFalseEnum" items="${trueFalseEnum}">
		                             			<form:option value="${trueFalseEnum.name}">${trueFalseEnum}</form:option>
		                             		</c:forEach>										
										</form:select>
										<spring:bind path="fileSeqOrderEnable">
											<elitecoreError:showError errorMessage="${status.errorMessage}" errorId="service-fileStatsEnabled_error"></elitecoreError:showError>
										</spring:bind>
									</div>
								</div>
							</div>
							<div class="col-md-6 no-padding">
								<spring:message code="parsing.service.config.no.file.alert.interval" var="tooltip" ></spring:message>
								<div class="form-group">
									<div class="table-cell-label">${tooltip}</div>
									<div class="input-group ">
										<form:input path="noFileAlert" cssClass="form-control table-cell input-sm" id="service-noFileAlert" data-toggle="tooltip" data-placement="bottom" title="${tooltip }"></form:input>
										<spring:bind path="noFileAlert">
											<elitecoreError:showError errorMessage="${status.errorMessage}" errorId="service-noFileAlert_error"></elitecoreError:showError>
										</spring:bind>
									</div>
								</div>
							</div>
							<div class="col-md-6 no-padding">
								<spring:message code="parsingService.configuration.file.error.path.label" var="label" ></spring:message>
								<spring:message code="parsingService.configuration.file.error.path.label" var="tooltip" ></spring:message>
								<div class="form-group">
									<div class="table-cell-label">${label}</div>
									<div class="input-group ">
											<form:input path="errorPath"
												cssClass="form-control table-cell input-sm"
												id="parsingService-errorPath" data-toggle="tooltip"
												data-placement="bottom" 
												title="${tooltip }"></form:input>
											<spring:bind path="errorPath">
												<elitecoreError:showError
													errorMessage="${status.errorMessage}" errorId="parsingService-errorPath_error"></elitecoreError:showError>
											</spring:bind>
										</div>
								</div>
							</div>
							<div class="col-md-6 no-padding">
								<spring:message code="parsing.service.config.enable.file.stat" var="label" ></spring:message>
								<spring:message code="parsing.service.config.enable.file.stat.tooltip" var="tooltip" ></spring:message>
								<div class="form-group">
									<div class="table-cell-label">${label}<span class="required">*</span></div>
									<div class="input-group ">
										<form:select path="fileStatInsertEnable" cssClass="form-control table-cell input-sm" tabindex="4" id="service-fileStats-insert-Enabled" data-toggle="tooltip" data-placement="bottom"  title="${tooltip }" onchange="enableFileStateOption(this.value);">
											<c:forEach var="trueFalseEnum" items="${trueFalseEnum}">
		                             			<form:option value="${trueFalseEnum.name}">${trueFalseEnum}</form:option>
		                             		</c:forEach>										
										</form:select>
										<spring:bind path="fileStatInsertEnable">
											<elitecoreError:showError errorMessage="${status.errorMessage}" errorId="service-fileStata_Enabled_error"></elitecoreError:showError>
										</spring:bind>
									</div>
								</div>
							</div>
						</c:if>
					</div>
				</div>
				<!-- Form content end here  -->
			</div>
			<!-- section-3 end here -->
			
			<!-- section-4 start here -->
			<div class="box box-warning">
				<div class="box-header with-border">
					<h3 class="box-title">
						<spring:message code="iplog.parsing.service.config.parsing.param.popup.header" ></spring:message>
					</h3>
					<div class="box-tools pull-right" style="top: -3px;">
						<button class="btn btn-box-tool" data-widget="collapse">
							<i class="fa fa-minus"></i>
						</button>
					</div>
					<!-- /.box-tools -->
				</div>
				<!-- /.box-header end here -->
				<div class="box-body">
					<div class="fullwidth inline-form">
						<div class="col-md-6 no-padding">
							<spring:message code="iplog.parsing.service.config.check.field" var="tooltip" ></spring:message>
							<div class="form-group">
								<div class="table-cell-label">${tooltip}</div>
								<div class="input-group ">
									<jsp:include page="../../common/formAutocomplete.jsp">
										<jsp:param name="unifiedField" value="service-equalCheckField" ></jsp:param>
										<jsp:param name="unifiedFieldPath" value="equalCheckField" ></jsp:param>
									</jsp:include>
									<script>
										$("#service-equalCheckField").on('blur', function() {
											disableCheckValueForParsing();
										});
									</script>
									<spring:bind path="equalCheckField">
										<elitecoreError:showError errorMessage="${status.errorMessage}" errorId="service-equalCheckField_error"></elitecoreError:showError>
									</spring:bind>
								</div>
							</div>
						</div>
						
						<div class="col-md-6 no-padding">
							<spring:message code="iplog.parsing.service.config.check.function" var="tooltip" ></spring:message>
							<div class="form-group">
								<div class="table-cell-label">${tooltip}</div>
								<div class="input-group ">
									<form:select path="equalCheckFunction" cssClass="form-control table-cell input-sm" tabindex="4" id="service-equalCheckFunction" data-toggle="tooltip" data-placement="bottom"  title="${tooltip }" value="${iplog_parsing_service_configuration_form_bean.equalCheckFunction}" onchange="clearFiltervalue(this)">
										<c:forEach items="${parseConnFunctionEnum}" var="parseConnFunctionEnum" >
	                             			<form:option value="${parseConnFunctionEnum.getParseConnFunctionEnum()}">${parseConnFunctionEnum}</form:option>
										</c:forEach>
									</form:select>
									<spring:bind path="equalCheckFunction">
										<elitecoreError:showError errorMessage="${status.errorMessage}" errorId="service-equalCheckFunction_error"></elitecoreError:showError>
									</spring:bind>
								</div>
							</div>
						</div>

						<div class="col-md-6 no-padding">
							<spring:message code="iplog.parsing.service.config.check.value" var="tooltip" ></spring:message>
							<spring:message code="iplog.parsing.service.config.check.value.tooltip" var="tooltip_desc" ></spring:message>
							<div class="form-group">
								<div class="table-cell-label">${tooltip}</div>
								<div class="input-group ">									
									<form:textarea path="equalCheckValue" id="service-equalCheckValue" data-toggle='tooltip' data-placement='bottom' title='${tooltip_desc}' class='md-textarea form-control' rows='3' /> 
									<spring:bind path="equalCheckValue">
										<elitecoreError:showError errorMessage="${status.errorMessage}" errorId="service-equalCheckValue_error"></elitecoreError:showError>
									</spring:bind>
								</div>
							</div>
						</div>
					</div>
				</div>
				<!-- Form content end here  -->
			</div>
			<!-- section-4 end here -->
			
			<!-- section-5 start here -->
			 <jsp:include page="../fileGroupingParameters.jsp"></jsp:include>
			<!-- section-5 end here -->
			
			<!-- section-6 start here -->
			
			<c:if test="${serviceType eq 'IPLOG_PARSING_SERVICE'}">
			<div class="box box-warning">
				<div class="box-header with-border">
					<h3 class="box-title">
						<spring:message code="iplog.parsing.service.config.file.stats.mgmt.popup.header" ></spring:message>
					</h3>
					<div class="box-tools pull-right" style="top: -3px;">
						<button class="btn btn-box-tool" data-widget="collapse">
							<i class="fa fa-minus"></i>
						</button>
					</div>
					<!-- /.box-tools -->
				</div>
				<!-- /.box-header end here -->
				<div class="box-body">
					<div class="fullwidth inline-form">
						<div class="col-md-6 no-padding">
							<spring:message code="iplog.parsing.service.config.enable.file.stats" var="tooltip" ></spring:message>
							<div class="form-group">
								<div class="table-cell-label">${tooltip}</div>
								<div class="input-group ">
									<form:select path="fileStatsEnabled" cssClass="form-control table-cell input-sm" tabindex="4" id="service-fileStatsEnabled" data-toggle="tooltip" data-placement="bottom"  title="${tooltip }" onchange="enableFileStateOption(this.value);">
										<c:forEach var="trueFalseEnum" items="${trueFalseEnum}">
	                             			<form:option value="${trueFalseEnum.name}">${trueFalseEnum}</form:option>
	                             		</c:forEach>										
									</form:select>
									<spring:bind path="fileStatsEnabled">
										<elitecoreError:showError errorMessage="${status.errorMessage}" errorId="service-fileStatsEnabled_error"></elitecoreError:showError>
									</spring:bind>
								</div>
							</div>
						</div>

						<div class="col-md-6 no-padding">
							<spring:message code="iplog.parsing.service.config.file.stats.path" var="tooltip" ></spring:message>
							<div class="form-group">
								<div class="table-cell-label">${tooltip}<i class='fa fa-square'></i></div>
								<div class="input-group ">
									<form:input path="fileStatsLoc" cssClass="form-control table-cell input-sm" tabindex="4" id="service-fileStatsLoc" data-toggle="tooltip" data-placement="bottom" title="${tooltip }"></form:input>
									<script>
										enableFileStateOption('${service_config_form_bean.fileStatsEnabled}');
									</script>
									<spring:bind path="fileStatsLoc">
										<elitecoreError:showError errorMessage="${status.errorMessage}" errorId="service-fileStatsLoc_error"></elitecoreError:showError>
									</spring:bind>
								</div>
							</div>
						</div>
					</div>
				</div>
				<!-- Form content end here  -->
			</div>
			</c:if>
			<!-- section-6 end here -->
			
			<!-- section-7 start here -->
			<c:if test="${serviceType eq 'IPLOG_PARSING_SERVICE'}">
			<div class="box box-warning">
				<div class="box-header with-border">
					<h3 class="box-title">
						<spring:message code="iplog.parsing.service.config.purge.param.popup.header" ></spring:message>
					</h3>
					<div class="box-tools pull-right" style="top: -3px;">
						<button class="btn btn-box-tool" data-widget="collapse">
							<i class="fa fa-minus"></i>
						</button>
					</div>
					<!-- /.box-tools -->
				</div>
				<!-- /.box-header end here -->
				<div class="box-body">
					<div class="fullwidth inline-form">
						<div class="col-md-6 no-padding">
							<spring:message code="iplog.parsing.service.config.purge.interval" var="tooltip" ></spring:message>
							<div class="form-group">
								<div class="table-cell-label">${tooltip}<span class="required">*</span></div>
								<div class="input-group ">
									<form:input path="purgeInterval" cssClass="form-control table-cell input-sm" tabindex="4" id="service-purgeInterval" data-toggle="tooltip" data-placement="bottom" title="${tooltip }" onkeypress="return isNumber(arguments[0] || window.event);"></form:input>
									<spring:bind path="purgeInterval">
										<elitecoreError:showError errorMessage="${status.errorMessage}" errorId="service-purgeInterval_error"></elitecoreError:showError>
									</spring:bind>
								</div>
							</div>
						</div>

						<div class="col-md-6 no-padding">
							<spring:message code="iplog.parsing.service.config.purge.delay" var="tooltip" ></spring:message>
							<div class="form-group">
								<div class="table-cell-label">${tooltip}<span class="required">*</span></div>
								<div class="input-group ">
									<form:input path="purgeDelayInterval" cssClass="form-control table-cell input-sm" tabindex="4" id="service-purgeDelayInterval" data-toggle="tooltip" data-placement="bottom" title="${tooltip }" onkeypress="return isNumber(arguments[0] || window.event);"></form:input>
									<spring:bind path="purgeDelayInterval">
										<elitecoreError:showError errorMessage="${status.errorMessage}" errorId="service-purgeDelayInterval_error"></elitecoreError:showError>
									</spring:bind>
								</div>
							</div>
						</div>
					</div>
				</div>
				<!-- Form content end here  -->
			</div>
			</c:if>
			<!-- section-7 end here -->
			
			<!-- section-8 start here -->
			<c:if test="${serviceType eq 'IPLOG_PARSING_SERVICE'}">
			<div class="box box-warning">
				<div class="box-header with-border">
					<h3 class="box-title">
						<spring:message code="iplog.parsing.service.config.correl.param.popup.header" ></spring:message>
					</h3>
					<div class="box-tools pull-right" style="top: -3px;">
						<button class="btn btn-box-tool" data-widget="collapse">
							<i class="fa fa-minus"></i>
						</button>
					</div>
					<!-- /.box-tools -->
				</div>
				<!-- /.box-header end here -->
				<div class="box-body">
					<div class="fullwidth inline-form">
						<div class="col-md-6 no-padding">
							<spring:message code="iplog.parsing.service.config.enable.correlation" var="tooltip" ></spring:message>
							<div class="form-group">
								<div class="table-cell-label">${tooltip}</div>
								<div class="input-group ">
									<form:select path="correlEnabled" cssClass="form-control table-cell input-sm" tabindex="4" id="service-correlEnabled" data-toggle="tooltip" data-placement="bottom"  title="${tooltip }" onchange="enableCorrelationOption(this.value);">
										<c:forEach var="trueFalseEnum" items="${trueFalseEnum}">
	                             			<form:option value="${trueFalseEnum.name}">${trueFalseEnum}</form:option>
	                             		</c:forEach>
									</form:select>
									<spring:bind path="correlEnabled">
										<elitecoreError:showError errorMessage="${status.errorMessage}" errorId="service-correlEnabled_error"></elitecoreError:showError>
									</spring:bind>
								</div>
							</div>
						</div>
						<div class="col-md-6 no-padding">
							<spring:message code="iplog.parsing.service.config.mapped.source.field" var="tooltip" ></spring:message>
							<div class="form-group">
								<div class="table-cell-label">${tooltip}</div>
								<div class="input-group ">
									<%-- <form:select path="mappedSourceField" cssClass="form-control table-cell input-sm" tabindex="4" id="service-mappedSourceField" data-toggle="tooltip" data-placement="bottom"  title="${tooltip }" value="${iplog_parsing_service_configuration_form_bean.mappedSourceField}">
										<c:forEach items="${unifiedFieldEnum}" var="unifiedFieldEnum" >
	                             			<form:option value="${unifiedFieldEnum}">${unifiedFieldEnum}</form:option>
										</c:forEach>
									</form:select> --%>
									<jsp:include page="../../common/formAutocomplete.jsp">
										<jsp:param name="unifiedField" value="service-mappedSourceField" ></jsp:param>
										<jsp:param name="unifiedFieldPath" value="mappedSourceField" ></jsp:param>
									</jsp:include>
									<spring:bind path="mappedSourceField">
										<elitecoreError:showError errorMessage="${status.errorMessage}" errorId="service-mappedSourceField_error"></elitecoreError:showError>
									</spring:bind>
								</div>
							</div>
						</div>
						<div class="col-md-6 no-padding">
							<spring:message code="iplog.parsing.service.config.dest.port.field" var="tooltip" ></spring:message>
							<div class="form-group">
								<div class="table-cell-label">${tooltip}</div>
								<div class="input-group ">
									<%-- <form:select path="destPortField" cssClass="form-control table-cell input-sm" tabindex="4" id="service-destPortField" data-toggle="tooltip" data-placement="bottom"  title="${tooltip }" value="${iplog_parsing_service_configuration_form_bean.mappedSourceField}">
										<c:forEach items="${unifiedFieldEnum}" var="unifiedFieldEnum" >
	                             			<form:option value="${unifiedFieldEnum}">${unifiedFieldEnum}</form:option>
										</c:forEach>
									</form:select> --%>
									<jsp:include page="../../common/formAutocomplete.jsp">
										<jsp:param name="unifiedField" value="service-destPortField" ></jsp:param>
										<jsp:param name="unifiedFieldPath" value="destPortField" ></jsp:param>
									</jsp:include>
									<spring:bind path="destPortField">
										<elitecoreError:showError errorMessage="${status.errorMessage}" errorId="service-destPortField_error"></elitecoreError:showError>
									</spring:bind>
								</div>
							</div>
						</div>
						<div class="col-md-6 no-padding">
							<spring:message code="iplog.parsing.service.config.desp.port.filter" var="tooltip" ></spring:message>
							<div class="form-group">
								<div class="table-cell-label">${tooltip}</div>
								<div class="input-group ">
									<%-- <form:select path="destPortFilter" cssClass="form-control table-cell input-sm" tabindex="4" id="service-destPortFilter" data-toggle="tooltip" data-placement="bottom"  title="${tooltip }" value="${iplog_parsing_service_configuration_form_bean.mappedSourceField}">
										<c:forEach items="${unifiedFieldEnum}" var="unifiedFieldEnum" >
	                             			<form:option value="${unifiedFieldEnum}">${unifiedFieldEnum}</form:option>
										</c:forEach>
									</form:select> --%>
									<jsp:include page="../../common/formAutocomplete.jsp">
										<jsp:param name="unifiedField" value="service-destPortFilter" ></jsp:param>
										<jsp:param name="unifiedFieldPath" value="destPortFilter" ></jsp:param>
									</jsp:include>
									<spring:bind path="destPortFilter">
										<elitecoreError:showError errorMessage="${status.errorMessage}" errorId="service-destPortFilter_error"></elitecoreError:showError>
									</spring:bind>
								</div>
							</div>
						</div>
						<div class="col-md-6 no-padding">
							<spring:message code="iplog.parsing.service.config.crt.rec.path.loc" var="tooltip" ></spring:message>
							<div class="form-group">
								<div class="table-cell-label">${tooltip}<i class='fa fa-square'></i></div>
								<div class="input-group ">
									<form:input path="createRecDestPath" cssClass="form-control table-cell input-sm" tabindex="4" id="service-createRecDestPath" data-toggle="tooltip" data-placement="bottom" title="${tooltip }"></form:input>
									<spring:bind path="createRecDestPath">
										<elitecoreError:showError errorMessage="${status.errorMessage}" errorId="service-createRecDestPath_error"></elitecoreError:showError>
									</spring:bind>
								</div>
							</div>
						</div>
						<div class="col-md-6 no-padding">
							<spring:message code="iplog.parsing.service.config.del.rec.path.loc" var="tooltip" ></spring:message>
							<div class="form-group">
								<div class="table-cell-label">${tooltip}<i class='fa fa-square'></i></div>
								<div class="input-group ">
									<form:input path="deleteRecDestPath" cssClass="form-control table-cell input-sm" tabindex="4" id="service-deleteRecDestPath" data-toggle="tooltip" data-placement="bottom" title="${tooltip }"></form:input>
									<script>
										enableCorrelationOption('${service_config_form_bean.correlEnabled}');
									</script>
									<spring:bind path="deleteRecDestPath">
										<elitecoreError:showError errorMessage="${status.errorMessage}" errorId="service-deleteRecDestPath_error"></elitecoreError:showError>
									</spring:bind>
								</div>
							</div>
						</div>
					</div>
				</div>
				<!-- Form content end here  -->
			</div>
			</c:if>
			<!-- section-8 end here -->
			
			<!-- section-9 start here -->
			<c:if test="${serviceType eq 'PARSING_SERVICE'}">
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
			</c:if>
			<!-- section-9 end here -->
			
		</form:form>
	    <script type="text/javascript">
			
			function resetConfiguration(){
				resetWarningDisplay();
				clearAllMessages();
				$('#unifiedFields').val('');
				setOutputFileHeader();
				$('#iplog-parsing-service-configuration-form input[type=text]:enabled:not([readonly]), textarea:enabled').val('');
			}
			
			function cancelConfiguration(){ 
				var serviceType = '${serviceType}';
       			
       			if(serviceType == '<%= EngineConstants.IPLOG_PARSING_SERVICE  %>'){
					showButtons('iplog-parsing-service-summary');
					$("#a-iplog-parsing-service-summary").click();
       			} else if (serviceType == '<%= EngineConstants.PARSING_SERVICE  %>'){
       				showButtons('a-parsing-service-summary');
					$("#a-parsing-service-summary").click();
       			}
			}
			
			function updateConfigForm(){
				var serviceType = '${serviceType}';
				console.log('Service Type is : ' + serviceType);
       			if(serviceType == '<%= EngineConstants.IPLOG_PARSING_SERVICE  %>'){
					// here we are manually removing attribute disabled because disabled attr are not submit on form submit
					$('#service-mappedSourceField').removeAttr('disabled');
					$('#fileGroupingParameter-enableForArchive').removeAttr('disabled');
					$('#fileGroupingParameter-groupingType').removeAttr('disabled');
					$('#service-mappedSourceField').removeAttr('disabled');
       			}else{

       				var minFileRange  =  $("#service-minFileRange").val(); 
       				var maxFileRange = $("#service-maxFileRange").val();
       				
       				if(minFileRange == '' || minFileRange == null){
       					$("#service-minFileRange").val('-1');
       				}
       				
       				if(maxFileRange == '' || maxFileRange == null){
       					$("#service-maxFileRange").val('-1');
       				}
       				
       				var noFileAlert = $("#service-noFileAlert").val(); 
       				if(noFileAlert == '' || noFileAlert == null){
       					$("#service-noFileAlert").val('-1');
       				}
       				
       			} 
       			
       			var equalCheckValues = $("#service-equalCheckValue").val().trim();
       			if(equalCheckValues != '' && equalCheckValues != null){
       				validateEqualCheckValues(equalCheckValues)
       			}
       			
       			$('#iplog-parsing-service-configuration-form').submit();
			}			
			
			function validateEqualCheckValues(equalCheckValues){
				
				 equalCheckValues = equalCheckValues.replace(/ +/g, "");
					var lastChar = equalCheckValues[equalCheckValues.length -1];
					if(lastChar==','){
						equalCheckValues = equalCheckValues.substring(0, equalCheckValues.length - 1);
					}					
					$("#service-equalCheckValue").val(equalCheckValues);		
			}
			
			function disableCheckValueForParsing(){
				
				var equalCheckField=$("#service-equalCheckField").val().trim();
				var equalCheckFunction=$("#service-equalCheckFunction").find(":selected").text();
				
				if(equalCheckField == ''){
					$("#service-equalCheckValue").prop('disabled', true);
					$("#service-equalCheckFunction").prop('disabled', true);
					$("#service-equalCheckFunction").val("Equals");
				}else{
					$("#service-equalCheckValue").prop('disabled', false);
					$("#service-equalCheckFunction").prop('disabled', false);
				}
			}
			
			function setOutputFileHeader() {
				 $('#outputFileHeader').multiselect({
				    	maxHeight: '100',
				        buttonWidth: 'auto',
				        nonSelectedText: 'Select',
				        nSelectedText: 'selected',
				        numberDisplayed: 4,
				        enableCaseInsensitiveFiltering: true
				    });
				 	var unifiedFields = $('#unifiedFields').val();
					var unifiedFieldsArr = unifiedFields.split(",");
					$('#outputFileHeader').val(unifiedFieldsArr);
					$('#outputFileHeader').multiselect("refresh");
			}
			
			$(document).ready(function() {
				
       			var serviceType = "${serviceType}";
       			
       			if (serviceType == '<%= EngineConstants.PARSING_SERVICE %>'){
       				
       				$('#iplog-parsing-service-configuration-form').attr('action','<%=ControllerConstants.UPDATE_PARSING_SERVICE_CONFIGURATION %>');
       				//$('#fileGroupingParameter-fileGroupEnable').attr('onClick','enableFileGroupOptionForParsing(this.val)');
       				enableFileGroupOptionForParsing('${service_config_form_bean.fileGroupingParameter.fileGroupEnable}');
       				var enableCDRSummary=$("#service-storeCDRFileSummaryDB").find(":selected").val();
       				enableCDRSummaryOption(enableCDRSummary);

       			}
       			else if(serviceType == '<%= EngineConstants.IPLOG_PARSING_SERVICE  %>'){
       				setOutputFileHeader();
       				$('#iplog-parsing-service-configuration-form').attr('action','<%=ControllerConstants.UPDATE_IPLOG_PARSING_SERVICE_CONFIGURATION %>');
       				//$('#fileGroupingParameter-fileGroupEnable').attr('onClick','enableFileGroupOptionForIplog(this.val)');
       				enableFileGroupOptionForIplog('${service_config_form_bean.fileGroupingParameter.fileGroupEnable}');
       			} 
       			
       			disableCheckValueForParsing();
     		});
			
		</script>
	</div>
</div>
