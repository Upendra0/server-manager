<%@page import="com.elitecore.sm.common.model.TrueFalseEnum"%>
<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="/WEB-INF/ShowErrorTag.tld" prefix="elitecoreError"%>

<div class="box-body padding0 mtop10">
	<div class="fullwidth">
		<form:form modelAttribute="service_config_form_bean" method="POST" action="<%=ControllerConstants.UPDATE_DIAMETER_SERVICE_CONFIGURATION %>" id="diameter-collection-service-configuration-form">
			<form:hidden path="serverInstance.id" id="serverInstanceId" value="${instanceId}"></form:hidden>
			<input type="hidden" name="serviceName" id="serviceName" value="${serviceName}"/>
			<form:hidden path="id" id="serviceId" value="${serviceId}"></form:hidden>
			<form:hidden path="servInstanceId" id="servInstanceId" value="${servInstanceId}"></form:hidden>
			<%-- <form:hidden path="stackPort" id="stackPort" value="${stackPort}"></form:hidden> --%>
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
			<jsp:include page="../../serviceBasicDetails.jsp"></jsp:include>
		<!-- section-1 end here -->

		<!-- section-2 start here -->
			<jsp:include page="diameterCollServicesListenerParam.jsp"></jsp:include>
		<!-- section-2 end here -->
		
		<!-- section-3 start here -->
			<jsp:include page="../../serviceExecutionParameters.jsp"></jsp:include>
		<!-- section-3 end here -->
		
		<!-- section-4 start here -->
			<jsp:include page="diameterCollServicesOperationalParam.jsp"></jsp:include>
		<!-- section-4 end here -->

		<!-- section-5 start here -->
		<div class="box box-warning">
			<div class="box-header with-border">
				<h3 class="box-title">
					<spring:message code="diameter.service.config.input.packet.header" ></spring:message>
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
						<spring:message code="diameter.service.config.input.packet.field.separator" var="label" ></spring:message>
						<spring:message code="diameter.service.config.input.packet.field.separator.tooltip" var="tooltip" ></spring:message>
						<div class="form-group">
							<div class="table-cell-label">${label}<span class="required">*</span></div>
							<div class="input-group ">
								<form:select path="fieldSeparatorEnum" cssClass="form-control table-cell input-sm" tabindex="4"
											id="service-fieldSeparatorEnum" data-toggle="tooltip" data-placement="bottom" title="${tooltip }">
									<c:forEach var="SeparatorEnum" items="${SeparatorEnum}">
						            		<form:option value="${SeparatorEnum.value}">${SeparatorEnum.input}</form:option>
						            </c:forEach>
								</form:select>
								<spring:bind path="fieldSeparator">
									<elitecoreError:showError errorMessage="${status.errorMessage}" errorId="service-fieldSeparatorEnum_error"></elitecoreError:showError>
								</spring:bind>
								<form:input path="fieldSeparator" cssClass="form-control table-cell input-sm" tabindex="4"
											id="fieldSeparator" data-toggle="tooltip" data-placement="bottom" 
											title="${tooltip }"></form:input>
								<spring:bind path="fieldSeparator">
									<elitecoreError:showError errorMessage="${status.errorMessage}" errorId="service-fieldSeparator_error"></elitecoreError:showError>
								</spring:bind>
							</div>
						</div>
					</div>
					
					<div class="col-md-6 no-padding">
						<spring:message code="diameter.service.config.input.packet.keyvalue.separator" var="label" ></spring:message>
						<spring:message code="diameter.service.config.input.packet.keyvalue.separator.tooltip" var="tooltip" ></spring:message>
						<div class="form-group">
							<div class="table-cell-label">${label}<span class="required">*</span></div>
							<div class="input-group ">
								<form:select path="keyValueSeparatorEnum" cssClass="form-control table-cell input-sm" tabindex="4"
											id="service-keyValueSeparatorEnum" data-toggle="tooltip" data-placement="bottom" title="${tooltip }">
									<c:forEach var="SeparatorEnum" items="${SeparatorEnum}">
						            		<form:option value="${SeparatorEnum.value}">${SeparatorEnum.input}</form:option>
						            </c:forEach>
								</form:select>
								<spring:bind path="keyValueSeparatorEnum">
									<elitecoreError:showError errorMessage="${status.errorMessage}" errorId="service-keyValueSeparatorEnum_error"></elitecoreError:showError>
								</spring:bind>
								<form:input path="keyValueSeparator" cssClass="form-control table-cell input-sm" tabindex="4"
											id="keyValueSeparator" data-toggle="tooltip" data-placement="bottom" 
											title="${tooltip }"></form:input>
								<spring:bind path="keyValueSeparator">
									<elitecoreError:showError errorMessage="${status.errorMessage}" errorId="service-keyValueSeparator_error"></elitecoreError:showError>
								</spring:bind>
							</div>
						</div>
					</div>
					
					<div class="col-md-6 no-padding">
						<spring:message code="diameter.service.config.input.packet.groupfield.separator" var="label" ></spring:message>
						<spring:message code="diameter.service.config.input.packet.groupfield.separator.tooltip" var="tooltip" ></spring:message>
						<div class="form-group">
							<div class="table-cell-label">${label}<span class="required">*</span></div>
							<div class="input-group ">
								<form:select path="groupFieldSeparatorEnum" cssClass="form-control table-cell input-sm" tabindex="4"
											id="service-groupFieldSeparatorEnum" data-toggle="tooltip" data-placement="bottom" title="${tooltip }">
									<c:forEach var="SeparatorEnum" items="${SeparatorEnum}">
						            		<form:option value="${SeparatorEnum.value}">${SeparatorEnum.input}</form:option>
						            </c:forEach>
								</form:select>
								<spring:bind path="groupFieldSeparatorEnum">
									<elitecoreError:showError errorMessage="${status.errorMessage}" errorId="service-groupFieldSeparatorEnum_error"></elitecoreError:showError>
								</spring:bind>
								<form:input path="groupFieldSeparator" cssClass="form-control table-cell input-sm" tabindex="4"
											id="groupFieldSeparator" data-toggle="tooltip" data-placement="bottom" 
											title="${tooltip }"></form:input>
								<spring:bind path="groupFieldSeparator">
									<elitecoreError:showError errorMessage="${status.errorMessage}" errorId="service-groupFieldSeparator_error"></elitecoreError:showError>
								</spring:bind>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
		<!-- section-5 end here -->
		</form:form>
		
		<script type="text/javascript">
			
			function resetConfiguration(){
				resetWarningDisplay();
				clearAllMessages();
				
				$('#diameter-collection-service-configuration-form input[type=text]:enabled:not([readonly]), textarea:enabled').val('');
				$('#service_execution_param_div input[type=text], textarea:enabled').val('');
				$('#diameter_coll_services_operational_param_div input[type=text]:enabled:not([readonly]), textarea:enabled').val('');
				$('#diameter_coll_services_operational_param_div select[type=option], select option:nth(0)').prop("selected","selected");
				$('#service-duplicateRequestCheck').val('true');
				$('#service-resultCodeOnOverload').val('');
				resetOutputPktConfig();
				enableResultCode();
				enablePurgeInterval();
			}
			
			function cancelConfiguration(){ 
				showButtons('diameter-collection-service-summary');
				$("#a-diameter-collection-service-summary").click();
			}
			
			function resetOutputPktConfig()
			{
				$("#service-fieldSeparatorEnum").val($("#service-fieldSeparatorEnum option:first").val());
				$("#service-keyValueSeparatorEnum").val($("#service-keyValueSeparatorEnum option:first").val());
				$("#service-groupFieldSeparatorEnum").val($("#service-groupFieldSeparatorEnum option:first").val());
				$("#fieldSeparator").val('');
				$("#keyValueSeparator").val('');
				$("#groupFieldSeparator").val('');
				$("#fieldSeparator").attr('readOnly',true);
				$("#keyValueSeparator").attr('readOnly',true);
				$("#groupFieldSeparator").attr('readOnly',true);
			}
			
			function enableResultCode(){
				var action = $('#service-actionOnOverload').val();
				if (action === "DROP")
					$("#service-resultCodeOnOverload").attr('readOnly',true);
				else
					$("#service-resultCodeOnOverload").attr('readOnly',false);
			}
			
			function enablePurgeInterval(){
				var duplicateCheck = $('#service-duplicateRequestCheck').val();
				if (duplicateCheck === 'true')
					$("#service-duplicatePurgeInterval").attr('readOnly',false);
				else
					$("#service-duplicatePurgeInterval").attr('readOnly',true);
			}
			
			function updateConfigForm(){
				var resultCode = $('#service-resultCodeOnOverload').val();
				if(resultCode == 'undefined' || resultCode == '' || resultCode == null)
					$('#service-resultCodeOnOverload').val(-1);
				
				var purgeInterval = $('#service-duplicatePurgeInterval').val();
				if(purgeInterval == 'undefined' || purgeInterval == '' || purgeInterval == null)
					$('#service-duplicatePurgeInterval').val(-1);
				
				var fieldSep = $('#service-fieldSeparatorEnum').val();
				if(fieldSep != 'Other'){
					if(fieldSep == 's'){
						$("#fieldSeparator").val(' ');
					}
					else
						$("#fieldSeparator").val(fieldSep);
				}
				
				var keyValSep = $('#service-keyValueSeparatorEnum').val();
				if(keyValSep != 'Other'){
					if(keyValSep == 's'){
						$("#keyValueSeparator").val(' ');
					}
					else
						$("#keyValueSeparator").val(keyValSep);
				}
				
				var grpFieldSep = $('#service-groupFieldSeparatorEnum').val();
				if(grpFieldSep != 'Other'){
					if(grpFieldSep == 's'){
						$("#groupFieldSeparator").val(' ');
					}
					else
						$("#groupFieldSeparator").val(grpFieldSep);
				}
				
				clearAllMessages();
				$('#diameter-collection-service-configuration-form').submit();
			}
			
			$(document).ready(function() {
				enableResultCode();
				enablePurgeInterval();
				// Field Seperator
				var fieldSep = $("#fieldSeparator").val();
				var separatorFindFlag = false;
				if (fieldSep == ' ')
					fieldSep = 's';
				
			    $('select[id="service-fieldSeparatorEnum"] option').each(function(index,seperator)
			    {
			    	if (fieldSep  == seperator.value)
			    	{
			    		$("#service-fieldSeparatorEnum").val(fieldSep);
						$("#fieldSeparator").val('');
						$("#fieldSeparator").attr('readOnly',true);
						separatorFindFlag = true;
			    	}
			    });
			    
			    if (separatorFindFlag == false)
			    {
			    	$("#service-fieldSeparatorEnum").val('Other');
			    	$("#fieldSeparator").val(fieldSep);
					$("#fieldSeparator").attr('readOnly',false);	
			    }
			
				if($('#service-fieldSeparatorEnum').val() != 'Other'){
					$("#fieldSeparator").attr('readOnly',true);
				}
			
				// Key Value Seperator
				var keyValSep = $("#keyValueSeparator").val();
				separatorFindFlag = false;
				if (keyValSep == ' ')
					keyValSep = 's';
				
			    $('select[id="service-keyValueSeparatorEnum"] option').each(function(index,seperator)
			    {
			    	if (keyValSep  == seperator.value)
			    	{
			    		$("#service-keyValueSeparatorEnum").val(keyValSep);
						$("#keyValueSeparator").val('');
						$("#keyValueSeparator").attr('readOnly',true);
						separatorFindFlag = true;
			    	}
			    });
			    
			    if (separatorFindFlag == false)
			    {
			    	$("#service-keyValueSeparatorEnum").val('Other');
			    	$("#keyValueSeparator").val(keyValSep);
					$("#keyValueSeparator").attr('readOnly',false);	
			    }
			
				if($('#service-keyValueSeparatorEnum').val() != 'Other'){
					$("#keyValueSeparator").attr('readOnly',true);
				}
		
				// Group Field Seperator
				var grpfieldSep = $("#groupFieldSeparator").val();
				separatorFindFlag = false;
				if (grpfieldSep == ' ')
					grpfieldSep = 's';
				
			    $('select[id="service-groupFieldSeparatorEnum"] option').each(function(index,seperator)
			    {
			    	if (grpfieldSep  == seperator.value)
			    	{
			    		$("#service-groupFieldSeparatorEnum").val(grpfieldSep);
						$("#groupFieldSeparator").val('');
						$("#groupFieldSeparator").attr('readOnly',true);
						separatorFindFlag = true;
			    	}
			    });
			    
			    if (separatorFindFlag == false)
			    {
			    	$("#service-groupFieldSeparatorEnum").val('Other');
			    	$("#groupFieldSeparator").val(grpfieldSep);
					$("#groupFieldSeparator").attr('readOnly',false);	
			    }
			
				if($('#service-groupFieldSeparatorEnum').val() != 'Other'){
					$("#groupFieldSeparator").attr('readOnly',true);
				}
			});
			
			$(document).on("change","#service-fieldSeparatorEnum",function(event) {
				resetWarningDisplay();
				clearAllMessages();
				var keyValSep = $('#service-fieldSeparatorEnum').val();
			    	if(keyValSep == 'Other'){
			    		$("#fieldSeparator").val('');
					    $("#fieldSeparator").attr('readOnly',false);
				   }else{
					   $("#fieldSeparator").val('');
					   $("#fieldSeparator").attr('readOnly',true);
				   }
			});
			
			$(document).on("change","#service-keyValueSeparatorEnum",function(event) {
				resetWarningDisplay();
				clearAllMessages();
				var keyValSep = $('#service-keyValueSeparatorEnum').val();
			    	if(keyValSep == 'Other'){
			    		$("#keyValueSeparator").val('');
					    $("#keyValueSeparator").attr('readOnly',false);
				   }else{
					   $("#keyValueSeparator").val('');
					   $("#keyValueSeparator").attr('readOnly',true);
				   }
			});
			
			$(document).on("change","#service-groupFieldSeparatorEnum",function(event) {
				resetWarningDisplay();
				clearAllMessages();
				var keyValSep = $('#service-groupFieldSeparatorEnum').val();
			    	if(keyValSep == 'Other'){
			    		$("#groupFieldSeparator").val('');
					    $("#groupFieldSeparator").attr('readOnly',false);
				   }else{
					   $("#groupFieldSeparator").val('');
					   $("#groupFieldSeparator").attr('readOnly',true);
				   }
			});
			
		</script>
	</div>
</div>

