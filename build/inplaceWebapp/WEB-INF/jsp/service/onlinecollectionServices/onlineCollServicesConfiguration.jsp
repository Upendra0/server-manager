<%@page import="com.elitecore.sm.common.model.TrueFalseEnum"%>
<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="/WEB-INF/ShowErrorTag.tld" prefix="elitecoreError"%>

<script>
function changeBinaryWriteOption(value){
	if(value=='false'){
		$('#service-binaryfileLocation').attr('readonly',true);
	} else {
		$('#service-binaryfileLocation').attr('readonly',false);
	}
}

function changeOptionTempateOption(value){
	if(value=='false'){
		$('#service-optionTemplateId').attr('readonly',true);
		$('#service-optionTemplateKey').attr('readonly',true);
		$('#service-optionTemplateValue').attr('readonly',true);
		$('#service-optionCopytoTemplateId').attr('readonly',true);
		$('#service-optionCopyTofield').attr('readonly',true);
	} else {
		$('#service-optionTemplateId').attr('readonly',false);
		$('#service-optionTemplateKey').attr('readonly',false);
		$('#service-optionTemplateValue').attr('readonly',false);
		$('#service-optionCopytoTemplateId').attr('readonly',false);
		$('#service-optionCopyTofield').attr('readonly',false);
	}
}

function changeProxyOption(value){
	if(value=='false'){
		$('#service-optionTemplateId').attr('readonly',true);
		$('#service-optionTemplateKey').attr('readonly',true);
		$('#service-optionTemplateValue').attr('readonly',true);
		$('#service-optionCopytoTemplateId').attr('readonly',true);
		$('#service-optionCopyTofield').attr('readonly',true);
	} else {
		$('#service-optionTemplateId').attr('readonly',false);
		$('#service-optionTemplateKey').attr('readonly',false);
		$('#service-optionTemplateValue').attr('readonly',false);
		$('#service-optionCopytoTemplateId').attr('readonly',false);
		$('#service-optionCopyTofield').attr('readonly',false);
	}
}

</script>


<div class="box-body padding0 mtop10">
	<div class="fullwidth">
		
		
		<form:form modelAttribute="service_config_form_bean" method="POST" action="<%=ControllerConstants.UPDATE_NETFLOWCOLLECTION_SERVICE_CONFIGURATION %>" id="netflow-collection-service-configuration-form">

			<form:hidden path="serverInstance.id" id="serverInstanceId" value="${instanceId}"></form:hidden>
			<input type="hidden" name="serviceName" id="serviceName" value="${serviceName}"/>
			<form:hidden path="id" id="serviceId" value="${serviceId}"></form:hidden>
			<form:hidden path="servInstanceId" id="servInstanceId" value="${servInstanceId}"></form:hidden>
			<form:hidden path="serverPort" id="serverPort" value="${serverPort}"></form:hidden>
			<form:hidden path="svctype.id" id="svctypeId" value="${svctype.id}"></form:hidden>
			<form:hidden path="svctype.type" id="svctypetype" value="${svctype.type}"></form:hidden>
			<form:hidden path="svctype.serviceCategory" id="svctypecategory" value="${svctype.serviceCategory}"></form:hidden>
			<form:hidden path="svctype.typeOfService" id="svctypeOfService" value="${svctype.typeOfService}"></form:hidden>
			<form:hidden path="svctype.description" id="svctypedescription" value="${svctype.description}"></form:hidden>
			<form:hidden path="svctype.alias" id="svctypealias" value="${svctype.alias}"></form:hidden>
			<form:hidden path="svctype.serviceFullClassName" id="svctypeserviceFullClassName" value="${svctype.serviceFullClassName}"></form:hidden>
			<form:hidden path="serviceSchedulingParams.id" id="serviceSchedulingParamsid" value="${serviceSchedulingParams.id}"></form:hidden>
			<form:hidden path="serverInstance.server.ipAddress" id="serverInstance.server.ipAddress" value="${serviceSchedulingParams.serverInstance.server.ipAddress}"></form:hidden>
			<form:hidden path="serverInstance.port" id="serverInstance.port" value="${serviceSchedulingParams.serverInstance.port}"></form:hidden>
			<form:hidden path="status" id="status" value="${status}"></form:hidden>
			<form:hidden path="enableFileStats" id="enableFilestatusid" value="${enableFileStats}"></form:hidden>
			<form:hidden path="enableDBStats" id = "enableDBStatsid" value = "${enableDBStats}"></form:hidden>
			
			<!-- Section-1 start here -->
			<jsp:include page="../serviceBasicDetails.jsp"></jsp:include>
			<!-- section-1 end here -->
			
			<!-- section-2 start here -->
			<jsp:include page="onlineCollServicesListenerParam.jsp"></jsp:include>
			<!-- section-2 end here -->
			
			<!-- section-3 start here -->
			<jsp:include page="../serviceExecutionParameters.jsp"></jsp:include>
			<!-- section-3 end here -->
			
			<jsp:include page="onlineCollServicesOperationalParam.jsp" ></jsp:include>
			
			<c:if test="${service_config_form_bean.svctype.alias eq 'HTTP2_COLLECTION_SERVICE'}">
				<jsp:include page="http2collectionservice/http2CollServiceSecurityParam.jsp" ></jsp:include>
			</c:if>
			
			<!-- section-4 start here -->
			<c:if test="${service_config_form_bean.svctype.alias eq 'NATFLOW_COLLECTION_SERVICE'}">
				<jsp:include page="../optionTemplateParam.jsp" ></jsp:include>
			</c:if>
			<!-- section-4 end here -->

		</form:form>
		<div class="box box-warning" id="proxy_template_param_div" style="display: none;">
			<div class="box-header with-border">
				<h3 class="box-title">Proxy Client Parameters</h3>
				<div class="box-tools pull-right" style="top: -3px;">
					<button class="btn btn-box-tool" data-widget="collapse">
						<i class="fa fa-minus"></i>
					</button>
				</div>
			</div>
			<div class="box-body">
				<div class="fullwidth inline-form">
					<div class="col-md-6 no-padding">
						<div class="form-group">
							<div class="table-cell-label">Enable Proxy Client</div>
							<div class="input-group">
								<select id="service-proxyClientEnable" name="optionTemplateEnable" class="form-control table-cell input-sm" title="" tabindex="4" data-toggle="tooltip" data-placement="bottom" onchange="changeOptionTempateOption(this.value);" data-original-title="Enable Option Template">
									<option value="false" selected="selected">False</option>
									<option value="true">True</option>
								</select> 
								<span class="input-group-addon add-on last"> 
									<i class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="left" title="" data-original-title="">
									</i>
								</span>
							</div>
						</div>
					</div>
					<div class="col-md-6 no-padding">
						<div class="form-group">
							<div class="table-cell-label">Proxy Service Port</div>
							<div class="input-group ">
								<input id="service-proxyServicePortId" name="proxyServicePortId" class="form-control table-cell input-sm" title="" tabindex="4" onkeypress="return isTemplateValue(arguments[0] || window.event);" data-toggle="tooltip" data-placement="bottom" type="text" value="263,262" readonly="readonly" data-original-title="Option Template Id">
								<span class="input-group-addon add-on last">
									<i class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="left" title="" data-original-title=""></i>
								</span>
								</div>
						</div>
					</div>
				</div>
			</div>
		</div>

		<script type="text/javascript">
			
			function resetConfiguration(){
				resetWarningDisplay();
				clearAllMessages();
				$('#netflow-collection-service-configuration-form input[type=text]:enabled:not([readonly]), textarea:enabled').val('');
				
				$('#service_execution_param_div input[type=text]:enabled:not([readonly]), textarea:enabled').val('');
				$('#online_coll_services_operational_param_div input[type=text]:enabled:not([readonly]), textarea:enabled').val('');
				$('#option_template_param_div input[type=text]:enabled:not([readonly]), textarea:enabled').val('');
				
				$('#service_execution_param_div select[type=option], select option:nth(0)').prop("selected","selected");
				
				$('#online_coll_services_operational_param_div select[type=option], select option:nth(0)').prop("selected","selected");
				$('#service-newLineCharAvailable').val('true');
				$('#service-enableParallelBinaryWrite').val('false');
				$('#service-readTemplateOnInit').val('true');
				
				$('#option_template_param_div select[type=option], select option:nth(0)').prop("selected","selected");
				$('#service-optionTemplateEnable').val('false');
				resetHttp2SecurityParam();
					
			}
			
			function cancelConfiguration(){ 
				showButtons('netflow-collection-service-summary');
				$("#a-netflow-collection-service-summary").click();
			}
			
			function updateConfigForm(){
				clearAllMessages();
				var netFlowPort = $("#service-netFlowPort").val();
				var proxyPort = $("#service-proxyServicePort").val();
				if(netFlowPort == proxyPort){
					$("#responseMsgDiv").html('');
					$("#responseMsgDiv").show();
					var errorSamePort = "<div style='width=calc();' class='errorMessage' id='divButton'>";
						errorSamePort += "<span id='span_error_txt' class='title'>Listener and Proxy Port Should not be same</span>";
						errorSamePort += "</div>";
						$("#responseMsgDiv").html(errorSamePort);
						return;
				}
				$("#service-proxyServicePort").attr('disabled',false);
				$('#netflow-collection-service-configuration-form').submit();
			}
			
			function changeProxyConfig(flag){
				if(flag == 'true'){
					$("#service-proxyServicePort").prop("disabled",false);	
					$("#btnCheckProxyFreePort").prop("disabled",false);
					$("#proxyClientConfiguration").show();
				} else {
					$("#service-proxyServicePort").prop("disabled",true);
					$("#btnCheckProxyFreePort").prop("disabled",true);
					$("#proxyClientConfiguration").hide();
				}
			}
			
			$(document).ready(function() {
				<c:if test="${service_config_form_bean.svctype.alias eq 'NATFLOW_COLLECTION_SERVICE'}">
					changeBinaryWriteOption('${service_config_form_bean.enableParallelBinaryWrite}');
					changeOptionTempateOption('${service_config_form_bean.optionTemplateEnable}');
					changeProxyConfig('${service_config_form_bean.proxyClientEnable}');
					$('#netflow-collection-service-configuration-form').attr('action','<%=ControllerConstants.UPDATE_NETFLOWCOLLECTION_SERVICE_CONFIGURATION %>');		
				</c:if>
				
				<c:if test="${service_config_form_bean.svctype.alias eq 'NATFLOWBINARY_COLLECTION_SERVICE'}">
					changeProxyConfig('${service_config_form_bean.proxyClientEnable}');
					$('#netflow-collection-service-configuration-form').attr('action','<%=ControllerConstants.UPDATE_NETFLOWBINARY_COLLECTION_SERVICE_CONFIGURATION %>');
				</c:if>
				
				<c:if test="${service_config_form_bean.svctype.alias eq 'SYSLOG_COLLECTION_SERVICE'}">
					$('#netflow-collection-service-configuration-form').attr('action','<%=ControllerConstants.UPDATE_SYSLOG_COLLECTION_SERVICE_CONFIGURATION %>');
				</c:if>
				
				<c:if test="${service_config_form_bean.svctype.alias eq 'MQTT_COLLECTION_SERVICE'}">
					$('#netflow-collection-service-configuration-form').attr('action','<%=ControllerConstants.UPDATE_MQTT_COLLECTION_SERVICE_CONFIGURATION %>');
				</c:if>
				
				<c:if test="${service_config_form_bean.svctype.alias eq 'COAP_COLLECTION_SERVICE'}">
					$('#netflow-collection-service-configuration-form').attr('action','<%=ControllerConstants.UPDATE_COAP_COLLECTION_SERVICE_CONFIGURATION %>');
				</c:if>
			
				<c:if test="${service_config_form_bean.svctype.alias eq 'HTTP2_COLLECTION_SERVICE'}">
					$('#netflow-collection-service-configuration-form').attr('action','<%=ControllerConstants.UPDATE_HTTP2_COLLECTION_SERVICE_CONFIGURATION %>');
				</c:if>
				
				<c:if test="${service_config_form_bean.svctype.alias eq 'GTPPRIME_COLLECTION_SERVICE'}">
					$('#netflow-collection-service-configuration-form').attr('action','<%=ControllerConstants.UPDATE_GTPPRIME_COLLECTION_SERVICE_CONFIGURATION %>');
				</c:if>
				
				<c:if test="${service_config_form_bean.svctype.alias eq 'RADIUS_COLLECTION_SERVICE'}">
				$('#netflow-collection-service-configuration-form').attr('action','<%=ControllerConstants.UPDATE_RADIUS_SERVICE_CONFIGURATION %>');
			</c:if>
			});
			
		</script>
	</div>
</div>

