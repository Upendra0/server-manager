<%@page import="com.elitecore.sm.common.constants.EngineConstants"%>
<%@page import="com.elitecore.sm.common.model.TrueFalseEnum"%>
<%@page import="com.elitecore.sm.common.constants.FormBeanConstants"%>
<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="/WEB-INF/ShowErrorTag.tld" prefix="elitecoreError"%>

<script>

var serviceType = '${serviceType}';

function resetConfiguration(){
	resetWarningDisplay();
	clearAllMessages();
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
		
	if(serviceType == '<%= EngineConstants.IPLOG_PARSING_SERVICE  %>'){
		// here we are manually removing attribute disabled because disabled attr are not submit on form submit
		$('#service-mappedSourceField').removeAttr('disabled');
		$('#fileGroupingParameter-enableForArchive').removeAttr('disabled');
		$('#fileGroupingParameter-groupingType').removeAttr('disabled');
		$('#service-mappedSourceField').removeAttr('disabled');
	} 
	$('#iplog-parsing-service-configuration-form').submit();
}

function enableArchiveOption(value){
	if(value=='false'){
		$('#fileGroupingParameter-archivePath').attr('readonly',true);
	} else {
		$('#fileGroupingParameter-archivePath').attr('readonly',false);
	}
}

function enableFileStateOption(value){
	if(value=='false'){
		$('#service-fileStatsLoc').attr('readonly',true);
	} else {
		$('#service-fileStatsLoc').attr('readonly',false);
	}
}
function enableFileGroupOptionForIplog(value){
	
	if(value=='false'){
		$('#fileGroupingParameter-groupingType').attr('disabled',true);
		$('#fileGroupingParameter-enableForArchive').val('false');
		$('#fileGroupingParameter-enableForArchive').attr('disabled',true);
		$('#service-fileStatsLoc').attr('readonly',true);
		$('#fileGroupingParameter-archivePath').attr('readonly',true);
	} else {
		$('#fileGroupingParameter-groupingType').attr('disabled',false);
		$('#fileGroupingParameter-enableForArchive').val('true');
		$('#fileGroupingParameter-enableForArchive').attr('disabled',true);
		$('#service-fileStatsLoc').attr('readonly',false);
		$('#fileGroupingParameter-archivePath').attr('readonly',false);
	}
}

function enableFileGroupOptionForParsing(value){
	
	if(value=='false'){
		$('#fileGroupingParameter-groupingType').attr('disabled',true);
	} else {
		$('#fileGroupingParameter-groupingType').attr('disabled',false);
	}
}

function enableCorrelationOption(value){
	if(value=='false'){
		$('#service-mappedSourceField').attr('disabled',true);
		$('#service-destPortField').attr('readonly',true);
		$('#service-destPortFilter').attr('readonly',true);
		$('#service-createRecDestPath').attr('readonly',true);
		$('#service-deleteRecDestPath').attr('readonly',true);
	} else {
		$('#service-mappedSourceField').attr('disabled',false);
		$('#service-destPortField').attr('readonly',false);
		$('#service-destPortFilter').attr('readonly',false);
		$('#service-createRecDestPath').attr('readonly',false);
		$('#service-deleteRecDestPath').attr('readonly',false);
	}
}

function enableCDRSummaryOption(value){
	if(value=='false'){
		$('#service-dateFieldForSummary').attr('disabled',true);
		$('#service-typeForSummary').attr('readonly',true);
		$('#service-overrideFileDateEnabled').attr('readonly',true);
		$('#service-overrideFileDateType').attr('readonly',true);
	} else {
		$('#service-dateFieldForSummary').attr('disabled',false);
		$('#service-typeForSummary').attr('readonly',false);
		$('#service-overrideFileDateEnabled').attr('readonly',false);
		$('#service-overrideFileDateType').attr('readonly',false);
	}
}

 $(document).ready(function() {
		alert(serviceType);
	if(serviceType == 'IPLOG_PARSING_SERVICE'){
		alert('type 1');
		$('#iplog-parsing-service-configuration-form').attr('action','<%=ControllerConstants.UPDATE_IPLOG_PARSING_SERVICE_CONFIGURATION %>');
  		$('#fileGroupingParameter-fileGroupEnable').attr('onClick','enableFileGroupOptionForIplog(this.val)');
  		enableFileGroupOptionForIplog('${service_config_form_bean.fileGroupingParameter.fileGroupEnable}');
  		enableCorrelationOption('${service_config_form_bean.correlEnabled}');
  		enableFileStateOption('${service_config_form_bean.fileStatsEnabled}');
	} else if (serviceType == 'PARSING_SERVICE'){
		alert('type 2');
		<%--$('#iplog-parsing-service-configuration-form').attr('action','<%=ControllerConstants.UPDATE_PARSING_SERVICE_CONFIGURATION %>');
		$('#fileGroupingParameter-fileGroupEnable').attr('onClick','enableFileGroupOptionForParsing(this.val)');
		enableFileGroupOptionForParsing('${service_config_form_bean.fileGroupingParameter.fileGroupEnable}');
		enableCDRSummaryOption('${service_config_form_bean.storeCDRFileSummaryDB}'); --%>
	} 
});  

</script>

<div class="box-body padding0 mtop10">
	<div class="fullwidth">
			
		<form:form modelAttribute="<%= FormBeanConstants.SERVICE_CONFIG_FORM_BEAN %>" method="POST" id="iplog-parsing-service-configuration-form">

			<form:hidden path="serverInstance.id" id="serverInstanceId" value="${instanceId}"></form:hidden>
			<input type="hidden" name="serviceName" id="serviceName" value="${serviceName}"/>
			<form:hidden path="id" id="serviceId" value="${serviceId}"></form:hidden>
			<form:hidden path="svctype.id" id="svctypeId" value="${svctype.id}"></form:hidden>
			<form:hidden path="svctype.type" id="svctypetype" value="${svctype.type}"></form:hidden>
			<form:hidden path="svctype.category" id="svctypecategory" value="${svctype.category}"></form:hidden>
			<form:hidden path="svctype.description" id="svctypedescription" value="${svctype.description}"></form:hidden>
			<form:hidden path="svctype.alias" id="svctypealias" value="${svctype.alias}"></form:hidden>
			<form:hidden path="svctype.serviceFullClassName" id="svctypeserviceFullClassName" value="${svctype.serviceFullClassName}"></form:hidden>
			<form:hidden path="fileGroupingParameter.id" id="id"></form:hidden>
			
			<!-- Section-1 start here -->
			<jsp:include page="serviceBasicDetails.jsp"></jsp:include>
			<!-- section-1 end here -->
			
			<!-- section-2 start here -->
			<jsp:include page="serviceExecutionParameters.jsp"></jsp:include>
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
					
						<div class="col-md-6 no-padding rightpart">
							<spring:message code="iplog.parsing.service.config.record.batch.size" var="tooltip" ></spring:message>
							<div class="form-group">
								<div class="table-cell-label">${tooltip}<span class="required">*</span></div>
								<div class="input-group ">
									<form:input path="recordBatchSize" cssClass="form-control table-cell input-sm" tabindex="4" id="service-batchSize" data-toggle="tooltip" data-placement="bottom" title="${tooltip }" onkeydown='isNumericOnKeyDown(event)' ></form:input>
									<spring:bind path="recordBatchSize">
										<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
									</spring:bind>
								</div>
							</div>
						</div>
						
						<c:if test="${serviceType eq 'PARSING_SERVICE'}">
							<div class="col-md-6 no-padding">
								<spring:message code="parsing.service.file.copy.folder" var="tooltip" ></spring:message>
								<div class="form-group">
									<div class="table-cell-label">${tooltip}<span class="required">*</span></div>
									<div class="input-group ">
										<form:input path="fileCopyFolders" cssClass="form-control table-cell input-sm" id="service-fileCopyFolders" data-toggle="tooltip" data-placement="bottom" title="${tooltip }"></form:input>
										<spring:bind path="fileCopyFolders">
											<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
										</spring:bind>
									</div>
								</div>
							</div>
							<div class="col-md-6 no-padding">
								<spring:message code="parsing.service.config.file.range" var="range" ></spring:message>
								<spring:message code="parsing.service.config.min.file.range" var="minRange" ></spring:message>
								<spring:message code="parsing.service.config.max.file.range" var="maxRange" ></spring:message>
								<div class="form-group">
									<div class="table-cell-label">${range}<span class="required">*</span></div>
									<div class="input-group ">
										<form:input path="minFileRange" id="service-minFileRange" cssClass="form-control table-cell input-sm" data-toggle="tooltip" data-placement="bottom" title="${minRange}" onkeydown='isNumericOnKeyDown(event)'></form:input>
	        							<spring:bind path="minFileRange">
											<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
										</spring:bind> 
										<form:input path="maxFileRange" id="service-maxFileRange" cssClass="form-control table-cell input-sm" data-toggle="tooltip" data-placement="bottom" title="${maxRange}" onkeydown='isNumericOnKeyDown(event)' ></form:input>
	        							<spring:bind path="maxFileRange">
											<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
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
											<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
										</spring:bind>
									</div>
								</div>
							</div>
							<div class="col-md-6 no-padding">
								<spring:message code="parsing.service.config.no.file.alert.interval" var="tooltip" ></spring:message>
								<div class="form-group">
									<div class="table-cell-label">${tooltip}<span class="required">*</span></div>
									<div class="input-group ">
										<form:input path="noFileAlert" cssClass="form-control table-cell input-sm" id="service-noFileAlert" data-toggle="tooltip" data-placement="bottom" title="${tooltip }"></form:input>
										<spring:bind path="noFileAlert">
											<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
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
									<form:select path="equalCheckField" cssClass="form-control table-cell input-sm" tabindex="4" id="service-equalCheckField" data-toggle="tooltip" data-placement="bottom"  title="${tooltip }" value="${iplog_parsing_service_configuration_form_bean.equalCheckField}">
										<c:forEach items="${unifiedFieldEnum}" var="unifiedFieldEnum" >
	                             			<form:option value="${unifiedFieldEnum.getUnifiedFiedEnum()}">${unifiedFieldEnum}</form:option>
										</c:forEach>
									</form:select>
									<spring:bind path="equalCheckField">
										<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
									</spring:bind>
								</div>
							</div>
						</div>

						<div class="col-md-6 no-padding">
							<spring:message code="iplog.parsing.service.config.check.value" var="tooltip" ></spring:message>
							<div class="form-group">
								<div class="table-cell-label">${tooltip}</div>
								<div class="input-group ">
									<form:input path="equalCheckValue" cssClass="form-control table-cell input-sm" tabindex="4" id="service-equalCheckValue" data-toggle="tooltip" data-placement="bottom" title="${tooltip}"></form:input>
									<spring:bind path="equalCheckValue">
										<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
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
			 <jsp:include page="fileGroupingParameters.jsp"></jsp:include>
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
										<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
									</spring:bind>
								</div>
							</div>
						</div>

						<div class="col-md-6 no-padding">
							<spring:message code="iplog.parsing.service.config.file.stats.path" var="tooltip" ></spring:message>
							<div class="form-group">
								<div class="table-cell-label">${tooltip}<i class='fa fa-square'></i></div>
								<div class="input-group ">
									<form:input path="fileStatsLoc" cssClass="form-control table-cell input-sm" tabindex="4" id="service-fileStatsLoc" readonly="false" data-toggle="tooltip" data-placement="bottom" title="${tooltip }"></form:input>
									<script>
										enableFileStateOption('${iplog_parsing_service_configuration_form_bean.fileStatsEnabled}');
									</script>
									<spring:bind path="fileStatsLoc">
										<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
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
										<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
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
										<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
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
										<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
									</spring:bind>
								</div>
							</div>
						</div>
						<div class="col-md-6 no-padding">
							<spring:message code="iplog.parsing.service.config.mapped.source.field" var="tooltip" ></spring:message>
							<div class="form-group">
								<div class="table-cell-label">${tooltip}</div>
								<div class="input-group ">
									<form:select path="mappedSourceField" cssClass="form-control table-cell input-sm" tabindex="4" id="service-mappedSourceField" data-toggle="tooltip" data-placement="bottom"  title="${tooltip }" value="${iplog_parsing_service_configuration_form_bean.mappedSourceField}">
										<c:forEach items="${unifiedFieldEnum}" var="unifiedFieldEnum" >
	                             			<form:option value="${unifiedFieldEnum}">${unifiedFieldEnum}</form:option>
										</c:forEach>
									</form:select>
									<spring:bind path="mappedSourceField">
										<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
									</spring:bind>
								</div>
							</div>
						</div>
						<div class="col-md-6 no-padding">
							<spring:message code="iplog.parsing.service.config.dest.port.field" var="tooltip" ></spring:message>
							<div class="form-group">
								<div class="table-cell-label">${tooltip}</div>
								<div class="input-group ">
									<form:input path="destPortField" cssClass="form-control table-cell input-sm" tabindex="4" id="service-destPortField" data-toggle="tooltip" data-placement="bottom" title="${tooltip}" onkeypress="return isNumber(arguments[0] || window.event);"></form:input>
									<spring:bind path="destPortField">
										<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
									</spring:bind>
								</div>
							</div>
						</div>
						<div class="col-md-6 no-padding">
							<spring:message code="iplog.parsing.service.config.desp.port.filter" var="tooltip" ></spring:message>
							<div class="form-group">
								<div class="table-cell-label">${tooltip}</div>
								<div class="input-group ">
									<form:input path="destPortFilter" cssClass="form-control table-cell input-sm" tabindex="4" id="service-destPortFilter" data-toggle="tooltip" data-placement="bottom" title="${tooltip }" onkeypress="return isNumber(arguments[0] || window.event);"></form:input>
									<spring:bind path="destPortFilter">
										<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
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
										<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
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
										enableCorrelationOption('${iplog_parsing_service_configuration_form_bean.correlEnabled}');
									</script>
									<spring:bind path="deleteRecDestPath">
										<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
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
											<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
										</spring:bind>
									</div>
								</div>
							</div> 
							<div class="col-md-6 no-padding">
								<spring:message code="parsing.service.config.mapped.unified.field" var="tooltip" ></spring:message>
								<div class="form-group">
									<div class="table-cell-label">${tooltip}</div>
									<div class="input-group ">
										<form:select path="dateFieldForSummary" cssClass="form-control table-cell input-sm" tabindex="4" id="service-dateFieldForSummary" data-toggle="tooltip" data-placement="bottom"  title="${tooltip }">
											<c:forEach items="${unifiedFieldEnum}" var="unifiedFieldEnum" >
		                             			<form:option value="${unifiedFieldEnum}">${unifiedFieldEnum}</form:option>
											</c:forEach>
										</form:select>
										<spring:bind path="dateFieldForSummary">
											<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
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
											<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
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
											<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
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
											<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
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
	</div>
</div>
