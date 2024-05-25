<%@page import="com.elitecore.sm.common.constants.EngineConstants"%>
<%@page import="com.elitecore.sm.common.model.RollingTypeEnum"%>
<%@page import="com.elitecore.sm.common.model.StateEnum"%>
<%@page
	import="com.elitecore.sm.common.model.DistributionDriverTypeEnum"%>
<%@page import="com.elitecore.sm.common.model.TrueFalseEnum"%>
<%@page import="com.elitecore.sm.common.model.ContentTypeEnum"%>
<%@page import="com.elitecore.sm.common.model.RequestTypeEnum"%>
<%@page import="com.elitecore.sm.common.model.MessageTypeEnum"%>
<%@page import="com.elitecore.sm.common.model.ContentFormatTypeEnum"%>
<%@page import="com.elitecore.sm.common.model.SecurityTypeEnum"%>
<%@page import="com.elitecore.sm.common.model.ProxySchemaTypeEnum"%>
<%@page import="com.elitecore.sm.util.MapCache"%>
<%@page
	import="com.elitecore.sm.common.constants.SystemParametersConstant"%>
<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/security/tags"
	prefix="sec"%>

<script>
	var clientListCounter=-1;
	var clientList = [];
	var serviceType = '${serviceType}';
	var isCollection = false;
	
	var netflowBinary_Collection = "<%= EngineConstants.NATFLOWBINARY_COLLECTION_SERVICE %>";
	var http2_collection = "<%= EngineConstants.HTTP2_COLLECTION_SERVICE %>";
	var netflow_Collection = "<%= EngineConstants.NATFLOW_COLLECTION_SERVICE %>";
	var syslog = "<%= EngineConstants.SYSLOG_COLLECTION_SERVICE %>";
	var mqtt = "<%= EngineConstants.MQTT_COLLECTION_SERVICE %>";	
	var coap = "<%= EngineConstants.COAP_COLLECTION_SERVICE %>";
	var gtpPrime_Collection = "<%= EngineConstants.GTPPRIME_COLLECTION_SERVICE %>";
	if(serviceType == 'NATFLOW_COLLECTION_SERVICE')
		isCollection = true;
	
	function changeRollingTypeOption(counter){
		var rollingType = $('#'+counter+'_rollingType').val();
		rollingType=="<%= RollingTypeEnum.BOTH %>";
		$('#'+counter+'_timeLogRollingUnit').attr('readonly',false);
		$('#'+counter+'_volLogRollingUnit').attr('readonly',false);
		
	}
	
	function changeSNMPAlterStatus(counter){
		var alertStatus = $('#'+counter+'_snmpAlertEnable').val();
		if(alertStatus=='true'){
			$('#'+counter+'_alertInterval').attr('readonly',false);
		} else {
			$('#'+counter+'_alertInterval').attr('readonly',true);
		}
	}
	
	function changeSequenceRangeStatus(counter){
		var appendStatus = $('#'+counter+'_appendFileSequenceInFileName').val();
		if(appendStatus=='true'){
			$('#'+counter+'_appendFilePaddingInFileName').prop("disabled", false);
			$('#'+counter+'_minFileSeqValue').attr('readonly',false);
			$('#'+counter+'_maxFileSeqValue').attr('readonly',false);
		} else {
			$('#'+counter+'_appendFilePaddingInFileName').prop("disabled", true);
			$('#'+counter+'_appendFilePaddingInFileName').find('option[value="false"]').prop('selected',true);
			$('#'+counter+'_minFileSeqValue').attr('readonly',true);
			$('#'+counter+'_minFileSeqValue').val('-1');
			$('#'+counter+'_maxFileSeqValue').attr('readonly',true);
			$('#'+counter+'_maxFileSeqValue').val('-1');
		}
	}
	function changeObserverStatus(counter){
		var registerObserver = $('#'+counter+'_registerObserver').val();
		if(registerObserver=='true'){
			$('#'+counter+'_observerTimeoutDiv').show();
			$('#'+counter+'_requestTypeDiv').hide();
			$('#'+counter+'_reqExecutionIntervalDiv').hide();
			$('#'+counter+'_reqExecutionFreqDiv').hide();
		} else {
			$('#'+counter+'_observerTimeoutDiv').hide();
			$('#'+counter+'_requestTypeDiv').show();
			$('#'+counter+'_reqExecutionIntervalDiv').show();
			$('#'+counter+'_reqExecutionFreqDiv').show();
			$('#'+counter+'_observerTimeout').val('-1');
		}
	}
	
	function changeKafkaParameters(counter){
		var enableKafka = $('#'+counter+'_enableKafka').val();
		if(enableKafka=='true'){
			$('#'+counter+'_kafkaDataSourceConfigDiv').show();
			$('#'+counter+'_topicNameDiv').show();
		} else {
			$('#'+counter+'_topicNameDiv').hide();
			$('#'+counter+'_kafkaDataSourceConfigDiv').hide();
			$('#'+counter+'_kafkaDataSourceConfig').val('-1');
		}
	}
	function changeSecurityParameters(counter){
		var enableSecurity = $('#'+counter+'_enableSecurity').val();
		if(enableSecurity=='true'){
			$('#'+counter+'_securityTypeDiv').show();
			var securityType = $('#'+counter+'_securityType').val();
			if(securityType == 'PSK') {
				$('#'+counter+'_securityIdentityDiv').show();
				$('#'+counter+'_securityKeyDiv').show();
				$('#'+counter+'_secCerLocationDiv').hide();
				$('#'+counter+'_secCerPasswdDiv').hide();
			} else if(securityType == 'X509' || securityType== 'RPK') {
				$('#'+counter+'_securityIdentityDiv').hide();
				$('#'+counter+'_securityKeyDiv').hide();
				$('#'+counter+'_secCerLocationDiv').show();
				$('#'+counter+'_secCerPasswdDiv').show();
			}
		} else {
			$('#'+counter+'_securityTypeDiv').hide();
			$('#'+counter+'_secCerLocationDiv').hide();
			$('#'+counter+'_secCerPasswdDiv').hide();
			$('#'+counter+'_securityIdentityDiv').hide();
			$('#'+counter+'_securityKeyDiv').hide();
		}
	}
	
	function changeSecurityType(counter) {
		var securityType = $('#'+counter+'_securityType').val();
		if(securityType == 'PSK') {
			$('#'+counter+'_securityIdentityDiv').show();
			$('#'+counter+'_securityKeyDiv').show();
			$('#'+counter+'_secCerLocationDiv').hide();
			$('#'+counter+'_secCerPasswdDiv').hide();
		} else if(securityType == 'X509' || securityType== 'RPK') {
			$('#'+counter+'_securityIdentityDiv').hide();
			$('#'+counter+'_securityKeyDiv').hide();
			$('#'+counter+'_secCerLocationDiv').show();
			$('#'+counter+'_secCerPasswdDiv').show();
		}
	}
	
	function changeProxyParameters(counter){
		var enableProxy = $('#'+counter+'_enableProxy').val();
		if(enableProxy=='true'){
			$('#'+counter+'_proxySchemaDiv').show();
			$('#'+counter+'_proxyResourcesDiv').show();
			$('#'+counter+'_proxyServerIpDiv').show();
		} else {
			$('#'+counter+'_proxySchemaDiv').hide();
			$('#'+counter+'_proxyResourcesDiv').hide();
			$('#'+counter+'_proxyServerIpDiv').hide();
			$('#'+counter+'_proxyServerPort').val('-1');
		}
	}
</script>

<script>
function addClientDetail(id,topicName,clientName,clientIp,clientPort,resourcesName,registerObserver,observerTimeout,requestType,messageType,requestTimeout,requestRetryCount, reqExecutionInterval, reqExecutionFreq, exchangeLifeTime, contentFormat, payload, enableSecurity, securityType, securityIdentity, securityKey, secCerLocation, secCerPasswd, enableProxy, proxyServerIp, proxyServerPort, proxySchema, proxyResources,fileLoc,bkpPath,fileFormat,enableFileSeq,enableFilePadding,minSeq,maxSeq,fileRollTime,fileRollUnit,fileCompress,status,snmpEnable,alertInterval,rollingType,nodeAliveRequest,echoRequest,requestExpiryTime,requestRetry,requestBufferCount,redirectionIp,sharedSecretKey,jsonValidate,contentType,uri, enableKafka, kafkaDataSourceId){
		
		clientListCounter++;
		var block = "<form method='POST' id='" + clientListCounter + "_form'> "+
		"<input type='hidden' id='serviceId' name='serviceId' value='${serviceId}'> "+
		"<input type='hidden' id='"+clientListCounter+"_id' name='"+clientListCounter+"_id' value='"+id+"' > "+
		"<div class='box box-warning' id='flipbox_"+clientListCounter+"'>"+
		"	<div class='box-header with-border'> "+
		"<c:if test="${serviceType ne 'MQTT_COLLECTION_SERVICE'}">"+
		"		<h3 class='box-title' id='title_"+clientListCounter+ "'><a href='#' class='title2rightfield-icon1-text' id='link_"+clientListCounter+ "' style='font-size: 12px;text-decoration:none;'>"+ clientName +"</a></h3>"+
	    "</c:if>"+ 
	    "<c:if test="${serviceType eq 'MQTT_COLLECTION_SERVICE'}">"+
		"		<h3 class='box-title' id='title_"+clientListCounter+ "'><a href='#' class='title2rightfield-icon1-text' id='link_"+clientListCounter+ "' style='font-size: 12px;text-decoration:none;'>"+ topicName+"-"+ clientName +"</a></h3>"+
	    "</c:if>"+    
        "		<div class='box-tools pull-right' id='action_"+clientListCounter+ "'>"+
        "			<button class='btn btn-box-tool block-collapse-btn' data-toggle='collapse' data-parent='#driverList' id='editClient_"+clientListCounter+ "' href='#" + clientListCounter + "_block'><i class='fa fa-minus'></i></button>	"+
        "				<sec:authorize access='hasAuthority(\'DELETE_COLLECTION_CLIENT\')'>"+
        "					&nbsp;&nbsp;<a class='ion-ios-close-empty remove-block' style='margin-top:12px;' id='deleteClientPopup_"+clientListCounter+ "' onclick='deleteClientPopup("+' "'+clientListCounter+'" ' + ")'></a>" +
        "				</sec:authorize>"+
        "		</div>"+
        "	</div>	"+
        "	<div class='box-body inline-form accordion-body collapse' id='" + clientListCounter + "_block'>"+
    	"<c:if test="${serviceType eq 'MQTT_COLLECTION_SERVICE'}">"+
        "		<div class='col-md-6 no-padding'>"+
        "			<spring:message code='netflow.collectionService.client.grid.column.topic.nm' var='label'></spring:message>"+
        "			<spring:message code='netflow.collectionService.client.grid.column.topic.nm' var='tooltip'></spring:message>"+
        "  			<div class='form-group'>"+
        "    			<div class='table-cell-label'>${label}<span class='required'>*</span></div>"+
        "      			<div class='input-group '>"+
        "					<input class='form-control table-cell input-sm' id='" + clientListCounter + "_topicName' data-toggle='tooltip' data-placement='bottom'  title='${tooltip}' value='"+topicName+"'/>"+
    	"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>" +
        "       		</div>"+
        "  	 		</div>"+
        "		</div>"+
        "</c:if>"+    
        "<c:if test="${serviceType eq 'MQTT_COLLECTION_SERVICE'}">"+
     	"		<div class='col-md-6 no-padding'>"+
        "			<spring:message code='netflow.collection.service.client.config.tab.client.name' var='label'></spring:message>"+
        "			<spring:message code='netflow.collection.service.client.config.tab.client.name.tooltip' var='tooltip'></spring:message>"+
        "  			<div class='form-group'>"+
        "    			<div class='table-cell-label'>${label}<span class='required'>*</span></div>"+
        "      			<div class='input-group '>"+
        "					<input class='form-control table-cell input-sm' id='" + clientListCounter + "_name' data-toggle='tooltip' data-placement='bottom'  title='${tooltip}' value='"+clientName+"'/>"+
		"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>" +
        "       		</div>"+
        "  	 		</div>"+
        "		</div>"+
        "</c:if>"+    
        "<c:if test="${serviceType ne 'MQTT_COLLECTION_SERVICE'}">"+
     	"		<div class='col-md-6 no-padding'>"+
        "			<spring:message code='netflow.collection.service.client.config.tab.client.name' var='label'></spring:message>"+
        "			<spring:message code='netflow.collection.service.client.config.tab.client.name.tooltip' var='tooltip'></spring:message>"+
        "  			<div class='form-group'>"+
        "    			<div class='table-cell-label'>${label}<span class='required'>*</span></div>"+
        "      			<div class='input-group '>"+
        "					<input class='form-control table-cell input-sm' id='" + clientListCounter + "_name' data-toggle='tooltip' data-placement='bottom'  title='${tooltip}' value='"+clientName+"'/>"+
		"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>" +
        "       		</div>"+
        "  	 		</div>"+
        "		</div>"+
        "</c:if>"+   
        "<c:if test="${serviceType eq 'COAP_COLLECTION_SERVICE'}">"+
        "		<div class='col-md-6 no-padding'>"+
        "			<spring:message code='netflow.collection.service.client.config.tab.resource.name' var='label'></spring:message>"+
        "			<spring:message code='netflow.collection.service.client.config.tab.resource.name.tooltip' var='tooltip'></spring:message>"+
        "  			<div class='form-group'>"+
        "    			<div class='table-cell-label'>${label}<span class='required'>*</span></div>"+
        "      			<div class='input-group '>"+
        "					<input class='form-control table-cell input-sm' id='" + clientListCounter + "_resourcesName' data-toggle='tooltip' data-placement='bottom'  title='${tooltip}' value='"+resourcesName+"'/>"+
		"					<span class='input-group-addon add-on last ' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>" +
        "       		</div>"+
        "  	 		</div>"+
        "		</div>"+
        "</c:if>"+   
        "<c:if test="${serviceType ne 'MQTT_COLLECTION_SERVICE'}">"+
        "		<div class='col-md-6 no-padding' style='display:inline-block;'>"+
        "			<spring:message code='netflow.collection.service.client.config.tab.client.ip' var='clientIP'></spring:message>"+
        "			<spring:message code='netflow.collection.service.client.config.tab.client.ip.tooltip' var='tooltipIp'></spring:message>"+
        "			<spring:message code='netflow.collection.service.client.config.tab.client.port' var='port'></spring:message>"+
        "			<spring:message code='netflow.collection.service.client.config.tab.client.port.tooltip' var='tooltip'></spring:message>"+
        "  			<div class='input-group'>"+
        "   			<div class='table-cell-label'>${clientIP}<span class='required'>*</span><i class='fa fa-square'></i></div>"+
        "				<div class='col-md-6 no-padding'>"+
        "					<div class='form-group'>"+
        "      					<div class='input-group'>"+
        "      				<input class='form-control table-cell input-sm' id='" + clientListCounter + "_clientIpAddress' data-toggle='tooltip' data-placement='bottom'  title='${tooltipIp}' value='"+clientIp+"' />"+
		"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
		"						</div>"+
		"					</div>"+
		"				</div>"+
		"				<div class='col-md-6 no-padding'>"+
		"					<div class='form-group'>"+
		"						<div class='input-group'>"+
		"					<input class='form-control table-cell input-sm' id='" + clientListCounter + "_clientPort' data-toggle='tooltip' data-placement='bottom'  title='${tooltip}' value='"+clientPort+"' onkeydown='isNumericOnKeyDown(event)' />"+
		"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
        "       				</div>"+
        "					</div>"+
        "				</div>"+
        "     		</div>"+
        "		</div>"+
        "</c:if>"+    
        "<c:if test="${serviceType eq 'COAP_COLLECTION_SERVICE'}">"+
        "		<div class='col-md-6 no-padding'>"+
        "			<spring:message code='netflow.collection.service.client.config.tab.register.observer' var='label'></spring:message>"+
        "			<spring:message code='netflow.collection.service.client.config.tab.register.observer.tooltip' var='tooltip'></spring:message>"+
        "  			<div class='form-group'>"+
        "    			<div class='table-cell-label'>${label}</div>"+
        "      			<div class='input-group '>"+
        "					<select class='form-control table-cell input-sm' id='" + clientListCounter + "_registerObserver' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }' value='"+registerObserver+"' onchange='changeObserverStatus("+ clientListCounter +")'>";
					        <%
							 for(TrueFalseEnum enumlist : TrueFalseEnum.values()) { 
								 %>
								 block += "<option value='<%=  enumlist.getName() %>'><%= enumlist.toString() %></option>";
								 <%
							     }
							%>
					
     	block += "	        </select>"+
		"	                <span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
        "       		</div>"+
        "  	 		</div>"+
        "		</div>"+
        "		<div class='col-md-6 no-padding' id='" + clientListCounter + "_observerTimeoutDiv'>"+
        "			<spring:message code='netflow.collection.service.client.config.tab.observer.timeout' var='label'></spring:message>"+
        "			<spring:message code='netflow.collection.service.client.config.tab.observer.timeout.tooltip' var='tooltip'></spring:message>"+
        "  			<div class='form-group'>"+
        "    			<div class='table-cell-label'>${label}</div>"+
        "      			<div class='input-group '>"+
        "					<input class='form-control table-cell input-sm' id='" + clientListCounter + "_observerTimeout' data-toggle='tooltip' data-placement='bottom'  title='${tooltip}' value='"+observerTimeout+"' onkeydown='isNumericOnKeyDown(event)'/>"+
		"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>" +
        "       		</div>"+
        "  	 		</div>"+
        "		</div>"+
        "		<div class='col-md-6 no-padding' id='" + clientListCounter + "_requestTypeDiv'>"+
        "			<spring:message code='netflow.collection.service.client.config.tab.request.type' var='label'></spring:message>"+
        "			<spring:message code='netflow.collection.service.client.config.tab.request.type.tooltip' var='tooltip'></spring:message>"+
        "  			<div class='form-group'>"+
        "    			<div class='table-cell-label'>${label}</div>"+
        "      			<div class='input-group '>"+
        "					<select class='form-control table-cell input-sm' id='" + clientListCounter + "_requestType' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }' value='"+requestType+"'>";
					        <%
							 for(RequestTypeEnum enumlist : RequestTypeEnum.values()) { 
								 %>
								 block += "<option value='<%=  enumlist.toString() %>'><%= enumlist.toString() %></option>";
								 <%
							     }
							%>
					
     	block += "	        </select>"+
		"	                <span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
        "       		</div>"+
        "  	 		</div>"+
        "		</div>"+  
        "		<div class='col-md-6 no-padding'>"+
        "			<spring:message code='netflow.collection.service.client.config.tab.message.type' var='label'></spring:message>"+
        "			<spring:message code='netflow.collection.service.client.config.tab.message.type.tooltip' var='tooltip'></spring:message>"+
        "  			<div class='form-group'>"+
        "    			<div class='table-cell-label'>${label}</div>"+
        "      			<div class='input-group '>"+
        "					<select class='form-control table-cell input-sm' id='" + clientListCounter + "_messageType' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }' value='"+messageType+"'>";
					        <%
							 for(MessageTypeEnum enumlist : MessageTypeEnum.values()) { 
								 %>
								 block += "<option value='<%=  enumlist.toString() %>'><%= enumlist.toString() %></option>";
								 <%
							     }
							%>
					
     	block += "	        </select>"+
		"	                <span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
        "       		</div>"+
        "  	 		</div>"+
        "		</div>"+
        "		<div class='col-md-6 no-padding' id='" + clientListCounter + "_requestTimeoutDiv'>"+
        "			<spring:message code='netflow.collection.service.client.config.tab.request.timeout' var='label'></spring:message>"+
        "			<spring:message code='netflow.collection.service.client.config.tab.request.timeout.tooltip' var='tooltip'></spring:message>"+
        "  			<div class='form-group'>"+
        "    			<div class='table-cell-label'>${label}</div>"+
        "      			<div class='input-group '>"+
        "					<input class='form-control table-cell input-sm' id='" + clientListCounter + "_requestTimeout' data-toggle='tooltip' data-placement='bottom'  title='${tooltip}' value='"+requestTimeout+"' onkeydown='isNumericOnKeyDown(event)'/>"+
		"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>" +
        "       		</div>"+
        "  	 		</div>"+
        "		</div>"+
        "		<div class='col-md-6 no-padding' id='" + clientListCounter + "_requestRetryCountDiv'>"+
        "			<spring:message code='netflow.collection.service.client.config.tab.request.retry.count' var='label'></spring:message>"+
        "			<spring:message code='netflow.collection.service.client.config.tab.request.retry.count.tooltip' var='tooltip'></spring:message>"+
        "  			<div class='form-group'>"+
        "    			<div class='table-cell-label'>${label}</div>"+
        "      			<div class='input-group '>"+
        "					<input class='form-control table-cell input-sm' id='" + clientListCounter + "_requestRetryCount' data-toggle='tooltip' data-placement='bottom'  title='${tooltip}' value='"+requestRetryCount+"' onkeydown='isNumericOnKeyDown(event)'/>"+
		"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>" +
        "       		</div>"+
        "  	 		</div>"+
        "		</div>"+
        "		<div class='col-md-6 no-padding' id='" + clientListCounter + "_reqExecutionIntervalDiv'>"+
        "			<spring:message code='netflow.collection.service.client.config.tab.request.execution.interval' var='label'></spring:message>"+
        "			<spring:message code='netflow.collection.service.client.config.tab.request.execution.interval.tooltip' var='tooltip'></spring:message>"+
        "  			<div class='form-group'>"+
        "    			<div class='table-cell-label'>${label}</div>"+
        "      			<div class='input-group '>"+
        "					<input class='form-control table-cell input-sm' id='" + clientListCounter + "_reqExecutionInterval' data-toggle='tooltip' data-placement='bottom'  title='${tooltip}' value='"+reqExecutionInterval+"' onkeydown='isNumericOnKeyDown(event)'/>"+
		"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>" +
        "       		</div>"+
        "  	 		</div>"+
        "		</div>"+
        "		<div class='col-md-6 no-padding' id='" + clientListCounter + "_reqExecutionFreqDiv'>"+
        "			<spring:message code='netflow.collection.service.client.config.tab.request.execution.frequency' var='label'></spring:message>"+
        "			<spring:message code='netflow.collection.service.client.config.tab.request.execution.frequency.tooltip' var='tooltip'></spring:message>"+
        "  			<div class='form-group'>"+
        "    			<div class='table-cell-label'>${label}</div>"+
        "      			<div class='input-group '>"+
        "					<input class='form-control table-cell input-sm' id='" + clientListCounter + "_reqExecutionFreq' data-toggle='tooltip' data-placement='bottom'  title='${tooltip}' value='"+reqExecutionFreq+"' onkeydown='isNumericOnKeyDown(event)'/>"+
		"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>" +
        "       		</div>"+
        "  	 		</div>"+
        "		</div>"+
        "		<div class='col-md-6 no-padding' id='" + clientListCounter + "_exchangeLifeTimeDiv'>"+
        "			<spring:message code='netflow.collection.service.client.config.tab.exchange.lifetime' var='label'></spring:message>"+
        "			<spring:message code='netflow.collection.service.client.config.tab.exchange.lifetime.tooltip' var='tooltip'></spring:message>"+
        "  			<div class='form-group'>"+
        "    			<div class='table-cell-label'>${label}</div>"+
        "      			<div class='input-group '>"+
        "					<input class='form-control table-cell input-sm' id='" + clientListCounter + "_exchangeLifeTime' data-toggle='tooltip' data-placement='bottom'  title='${tooltip}' value='"+exchangeLifeTime+"' onkeydown='isNumericOnKeyDown(event)'/>"+
		"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>" +
        "       		</div>"+
        "  	 		</div>"+
        "		</div>"+
        "		<div class='col-md-6 no-padding' style='display:none'>"+
        "			<spring:message code='netflow.collection.service.client.config.tab.request.content.format' var='label'></spring:message>"+
        "			<spring:message code='netflow.collection.service.client.config.tab.request.content.format.tooltip' var='tooltip'></spring:message>"+
        "  			<div class='form-group'>"+
        "    			<div class='table-cell-label'>${label}</div>"+
        "      			<div class='input-group '>"+
        "					<select class='form-control table-cell input-sm' id='" + clientListCounter + "_contentFormat' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }' value='"+contentFormat+"'>";
					        <%
							 for(ContentFormatTypeEnum enumlist : ContentFormatTypeEnum.values()) { 
								 %>
								 block += "<option value='<%=  enumlist.toString() %>'><%= enumlist.toString() %></option>";
								 <%
							     }
							%>
					
     	block += "	        </select>"+
		"	                <span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
        "       		</div>"+
        "  	 		</div>"+
        "		</div>"+
        "		<div class='col-md-6 no-padding' style='display:none'>"+
        "			<spring:message code='netflow.collection.service.client.config.tab.request.payload' var='label'></spring:message>"+
        "			<spring:message code='netflow.collection.service.client.config.tab.request.payload.tooltip' var='tooltip'></spring:message>"+
        "  			<div class='form-group'>"+
        "    			<div class='table-cell-label'>${label}</div>"+
        "      			<div class='input-group '>"+
        "					<input class='form-control table-cell input-sm' id='" + clientListCounter + "_payload' data-toggle='tooltip' data-placement='bottom'  title='${tooltip}' value='"+payload+"'/>"+
		"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>" +
        "       		</div>"+
        "  	 		</div>"+
        "		</div>"+
        "</c:if>"+  
        
        "<c:if test="${serviceType eq 'COAP_COLLECTION_SERVICE'}">"+
        "		<div class='col-md-6 no-padding'>"+
        "			<spring:message code='netflow.collection.service.client.config.tab.enable.security' var='label'></spring:message>"+
        "			<spring:message code='netflow.collection.service.client.config.tab.enable.security.tooltip' var='tooltip'></spring:message>"+
        "  			<div class='form-group'>"+
        "    			<div class='table-cell-label'>${label}</div>"+
        "      			<div class='input-group '>"+
        "					<select class='form-control table-cell input-sm' id='" + clientListCounter + "_enableSecurity' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }' value='"+enableSecurity+"' onchange='changeSecurityParameters("+ clientListCounter +")'>";
					        <%
							 for(TrueFalseEnum enumlist : TrueFalseEnum.values()) { 
								 %>
								 block += "<option value='<%=  enumlist.getName() %>'><%= enumlist.toString() %></option>";
								 <%
							     }
							%>
					
     	block += "	        </select>"+
		"	                <span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
        "       		</div>"+
        "  	 		</div>"+
        "		</div> "+
        "		<div class='col-md-6 no-padding' id='" + clientListCounter + "_securityTypeDiv'>"+
        "			<spring:message code='netflow.collection.service.client.config.tab.security.type' var='label'></spring:message>"+
        "			<spring:message code='netflow.collection.service.client.config.tab.security.type.tooltip' var='tooltip'></spring:message>"+
        "  			<div class='form-group'>"+
        "    			<div class='table-cell-label'>${label}</div>"+
        "      			<div class='input-group '>"+
        "					<select class='form-control table-cell input-sm' id='" + clientListCounter + "_securityType' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }' value='"+securityType+"' onchange='changeSecurityType("+ clientListCounter +")'>";
					        <%
							 for(SecurityTypeEnum enumlist : SecurityTypeEnum.values()) { 
								 %>
								 block += "<option value='<%=  enumlist.toString() %>'><%= enumlist.toString() %></option>";
								 <%
							     }
							%>
					
     	block += "	        </select>"+
		"	                <span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
        "       		</div>"+
        "  	 		</div>"+
        "		</div> "+
        "		<div class='col-md-6 no-padding' id='" + clientListCounter + "_securityIdentityDiv'>"+
        "			<spring:message code='netflow.collection.service.client.config.tab.security.identity' var='label'></spring:message>"+
        "			<spring:message code='netflow.collection.service.client.config.tab.security.identity.tooltip' var='tooltip'></spring:message>"+
        "  			<div class='form-group'>"+
        "    			<div class='table-cell-label'>${label}</div>"+
        "      			<div class='input-group '>"+
        "      				<input class='form-control table-cell input-sm' id='" + clientListCounter + "_securityIdentity' data-toggle='tooltip' data-placement='bottom' title='${tooltip }' value='"+securityIdentity+"'/>"+
		"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
        "        		</div>"+
        "     		</div>"+
        "		</div>"+
        "		<div class='col-md-6 no-padding' id='" + clientListCounter + "_securityKeyDiv'>"+
        "			<spring:message code='netflow.collection.service.client.config.tab.security.key' var='label'></spring:message>"+
        "			<spring:message code='netflow.collection.service.client.config.tab.security.key.tooltip' var='tooltip'></spring:message>"+
        "  			<div class='form-group'>"+
        "    			<div class='table-cell-label'>${label}</div>"+
        "      			<div class='input-group '>"+
        "      				<input class='form-control table-cell input-sm' id='" + clientListCounter + "_securityKey' data-toggle='tooltip' data-placement='bottom' title='${tooltip }' value='"+securityKey+"'/>"+
		"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
        "        		</div>"+
        "     		</div>"+
        "		</div>"+
        "		<div class='col-md-6 no-padding' id='" + clientListCounter + "_secCerLocationDiv'>"+
        "			<spring:message code='netflow.collection.service.client.config.tab.security.certificate.location' var='label'></spring:message>"+
        "			<spring:message code='netflow.collection.service.client.config.tab.security.certificate.location.tooltip' var='tooltip'></spring:message>"+
        "  			<div class='form-group'>"+
        "    			<div class='table-cell-label'>${label}<span class='required'>*</span></div>"+
        "      			<div class='input-group '>"+
        "      				<input class='form-control table-cell input-sm' id='" + clientListCounter + "_secCerLocation' data-toggle='tooltip' data-placement='bottom' title='${tooltip }' value='"+secCerLocation+"'/>"+
		"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
        "        		</div>"+
        "     		</div>"+
        "		</div>"+
        "		<div class='col-md-6 no-padding' id='" + clientListCounter + "_secCerPasswdDiv'>"+
        "			<spring:message code='netflow.collection.service.client.config.tab.security.certificate.password' var='label'></spring:message>"+
        "			<spring:message code='netflow.collection.service.client.config.tab.security.certificate.password.tooltip' var='tooltip'></spring:message>"+
        "  			<div class='form-group'>"+
        "    			<div class='table-cell-label'>${label}</div>"+
        "      			<div class='input-group '>"+
        "      				<input class='form-control table-cell input-sm' id='" + clientListCounter + "_secCerPasswd' data-toggle='tooltip' data-placement='bottom' title='${tooltip }' value='"+secCerPasswd+"'/>"+
		"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
        "        		</div>"+
        "     		</div>"+
        "		</div>"+
		"		<div class='col-md-6 no-padding'>"+
        "			<spring:message code='netflow.collection.service.client.config.tab.enable.proxy' var='label'></spring:message>"+
        "			<spring:message code='netflow.collection.service.client.config.tab.enable.proxy.tooltip' var='tooltip'></spring:message>"+
        "  			<div class='form-group'>"+
        "    			<div class='table-cell-label'>${label}</div>"+
        "      			<div class='input-group '>"+
        "					<select class='form-control table-cell input-sm' id='" + clientListCounter + "_enableProxy' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }' value='"+enableProxy+"' onchange='changeProxyParameters("+ clientListCounter +")'>";
					        <%
							 for(TrueFalseEnum enumlist : TrueFalseEnum.values()) { 
								 %>
								 block += "<option value='<%=  enumlist.getName() %>'><%= enumlist.toString() %></option>";
								 <%
							     }
							%>
					
     	block += "	        </select>"+
		"	                <span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
        "       		</div>"+
        "  	 		</div>"+
        "		</div> "+
        "		<div class='col-md-6 no-padding' id='" + clientListCounter + "_proxySchemaDiv'>"+
        "			<spring:message code='netflow.collection.service.client.config.tab.proxy.schema' var='label'></spring:message>"+
        "			<spring:message code='netflow.collection.service.client.config.tab.proxy.schema.tooltip' var='tooltip'></spring:message>"+
        "  			<div class='form-group'>"+
        "    			<div class='table-cell-label'>${label}</div>"+
        "      			<div class='input-group '>"+
        "					<select class='form-control table-cell input-sm' id='" + clientListCounter + "_proxySchema' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }' value='"+proxySchema+"'>";
					        <%
							 for(ProxySchemaTypeEnum enumlist : ProxySchemaTypeEnum.values()) { 
								 %>
								 block += "<option value='<%=  enumlist.toString() %>'><%= enumlist.toString() %></option>";
								 <%
							     }
							%>
					
     	block += "	        </select>"+
		"	                <span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
        "       		</div>"+
        "  	 		</div>"+
        "		</div> "+
        "		<div class='col-md-6 no-padding' id='" + clientListCounter + "_proxyResourcesDiv'>"+
        "			<spring:message code='netflow.collection.service.client.config.tab.proxy.resource.name' var='label'></spring:message>"+
        "			<spring:message code='netflow.collection.service.client.config.tab.proxy.resource.name.tooltip' var='tooltip'></spring:message>"+
        "  			<div class='form-group'>"+
        "    			<div class='table-cell-label'>${label}<span class='required'>*</span></div>"+
        "      			<div class='input-group '>"+
        "					<input class='form-control table-cell input-sm' id='" + clientListCounter + "_proxyResources' data-toggle='tooltip' data-placement='bottom'  title='${tooltip}' value='"+proxyResources+"'/>"+
		"					<span class='input-group-addon add-on last ' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>" +
        "       		</div>"+
        "  	 		</div>"+
        "		</div>"+
        "		<div class='col-md-6 no-padding' style='display:inline-block;' id='" + clientListCounter + "_proxyServerIpDiv'>"+
        "			<spring:message code='netflow.collection.service.client.config.tab.proxy.server.ip' var='proxyIP'></spring:message>"+
        "			<spring:message code='netflow.collection.service.client.config.tab.proxy.server.ip.tooltip' var='tooltipIp'></spring:message>"+
        "			<spring:message code='netflow.collection.service.client.config.tab.proxy.server.port' var='proxyPort'></spring:message>"+
        "			<spring:message code='netflow.collection.service.client.config.tab.proxy.server.port.tooltip' var='tooltip'></spring:message>"+
        "  			<div class='input-group'>"+
        "   			<div class='table-cell-label'>${proxyIP}<span class='required'>*</span><i class='fa fa-square'></i></div>"+
        "				<div class='col-md-6 no-padding'>"+
        "					<div class='form-group'>"+
        "      					<div class='input-group'>"+
        "      				<input class='form-control table-cell input-sm' id='" + clientListCounter + "_proxyServerIp' data-toggle='tooltip' data-placement='bottom'  title='${tooltipIp}' value='"+proxyServerIp+"' />"+
		"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
		"						</div>"+
		"					</div>"+
		"				</div>"+
		"				<div class='col-md-6 no-padding'>"+
		"					<div class='form-group'>"+
		"						<div class='input-group'>"+
		"					<input class='form-control table-cell input-sm' id='" + clientListCounter + "_proxyServerPort' data-toggle='tooltip' data-placement='bottom'  title='${tooltip}' value='"+proxyServerPort+"' onkeydown='isNumericOnKeyDown(event)' />"+
		"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
        "       				</div>"+
        "					</div>"+
        "				</div>"+
        "     		</div>"+
        "		</div>"+
        "		<div class='col-md-6 no-padding'>"+
        "			<spring:message code='netflow.collection.service.client.config.tab.enable.kafka' var='label'></spring:message>"+
        "			<spring:message code='netflow.collection.service.client.config.tab.enable.kafka.tooltip' var='tooltip'></spring:message>"+
        "  			<div class='form-group'>"+
        "    			<div class='table-cell-label'>${label}</div>"+
        "      			<div class='input-group '>"+
        "					<select class='form-control table-cell input-sm' id='" + clientListCounter + "_enableKafka' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }' value='"+enableKafka+"' onchange='changeKafkaParameters("+ clientListCounter +")'>";
					        <%
							 for(TrueFalseEnum enumlist : TrueFalseEnum.values()) { 
								 %>
								 block += "<option value='<%=  enumlist.getName() %>'><%= enumlist.toString() %></option>";
								 <%
							     }
							%>
					
     	block += "	        </select>"+
		"	                <span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
        "       		</div>"+
        "  	 		</div>"+
        "		</div> "+
        "		<div class='col-md-6 no-padding' id='" + clientListCounter + "_kafkaDataSourceConfigDiv'>"+
	    "				<spring:message code='netflow.collection.service.client.config.tab.kafka.datasource' var='label'></spring:message>"+
	    "				<spring:message code='netflow.collection.service.client.config.tab.kafka.datasource.tooltip' var='tooltip'></spring:message>"+
	    "  				<div class='form-group'>"+
		"				<div class='table-cell-label'>${label}</div> "+ 
	    "      				<div class='input-group '>"+
		"  						<select class='form-control table-cell input-sm' id='"+clientListCounter+"_kafkaDataSourceConfig' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }'>" +
	    "   						<c:forEach var='kafkaDataSource' items='${kafkaDataSourceList}'>"+
	    "    							<option value='${kafkaDataSource.id}'>${kafkaDataSource.name}</option>" +
	    "    						</c:forEach>" +
	    "   					</select>" +
		"						<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
	    "        			</div>"+
	    "    			</div>"+
	    "		</div>"+
        "		<div class='col-md-6 no-padding' id='" + clientListCounter + "_topicNameDiv'>"+
        "			<spring:message code='netflow.collectionService.client.grid.column.topic.nm' var='label'></spring:message>"+
        "			<spring:message code='netflow.collectionService.client.grid.column.topic.nm' var='tooltip'></spring:message>"+
        "  			<div class='form-group'>"+
        "    			<div class='table-cell-label'>${label}</div>"+
        "      			<div class='input-group '>"+
        "					<input class='form-control table-cell input-sm' id='" + clientListCounter + "_topicName' data-toggle='tooltip' data-placement='bottom'  title='${tooltip}' value='"+topicName+"'/>"+
    	"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>" +
        "       		</div>"+
        "  	 		</div>"+
        "		</div>"+
        "</c:if>"+  
        
        "		<div class='col-md-6 no-padding'>"+
        "			<spring:message code='netflow.collection.service.client.config.tab.file.loc' var='fileLoc'></spring:message>"+
        "			<spring:message code='netflow.collection.service.client.config.tab.file.loc.tooltip' var='tooltip'></spring:message>"+
        "  			<div class='form-group'>"+
        "    			<div class='table-cell-label'>${fileLoc}<span class='required'>*</span><i class='fa fa-square'></i></div>"+
        "      			<div class='input-group '>"+
        "					<input class='form-control table-cell input-sm' id='" + clientListCounter + "_outFileLocation' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }' value='"+fileLoc+"'/>"+
        "					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
        "        		</div>"+
        "     		</div>"+
        "		</div> ";
        
        if(isCollection==true){
        	block += "	<div class='col-md-6 no-padding'>"+
            "			<spring:message code='netflow.collection.service.client.config.tab.file.bkp.path' var='label'></spring:message>"+
            "  			<div class='form-group'>"+
            "    			<div class='table-cell-label'>${label}</div>"+
            "      			<div class='input-group '>"+
            "					<input class='form-control table-cell input-sm' id='" + clientListCounter + "_bkpBinaryfileLocation' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }' value='"+bkpPath+"'/>"+
            "					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
            "        		</div>"+
            "     		</div>"+
            "		</div> ";
        }

        
        block += "	<div class='col-md-6 no-padding'>"+
        "			<spring:message code='netflow.collection.service.client.config.tab.file.format' var='label'></spring:message>"+
        "			<spring:message code='netflow.collection.service.client.config.tab.file.format.tooltip' var='tooltip'></spring:message>"+
        "  			<div class='form-group'>"+
        "    			<div class='table-cell-label'>${label}<span class='required'>*</span></div>"+
        "      			<div class='input-group '>"+
        "      				<input class='form-control table-cell input-sm' id='" + clientListCounter + "_fileNameFormat' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }' value='"+fileFormat+"'/>"+
		"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
        "        		</div>"+        
        "     		</div>"+
        "		</div>"+
        "		<div class='col-md-6 no-padding'>"+
        "			<spring:message code='netflow.collection.service.client.config.tab.file.seq.enable' var='label'></spring:message>"+
        "			<spring:message code='netflow.collection.service.client.config.tab.file.seq.enable.tooltip' var='tooltip'></spring:message>"+
        "  			<div class='form-group'>"+
        "    			<div class='table-cell-label'>${label}</div>"+
        "      			<div class='input-group '>"+
        "					<select class='form-control table-cell input-sm' id='" + clientListCounter + "_appendFileSequenceInFileName' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }' value='"+enableFileSeq+"' onchange='changeSequenceRangeStatus("+ clientListCounter +")'>";
						        <%
								 for(TrueFalseEnum enumlist : TrueFalseEnum.values()) { 
									 %>
									 block += "<option value='<%=  enumlist.getName() %>'><%= enumlist.toString() %></option>";
									 <%
								     }
								%>
       
		block += "					</select>"+
		"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
        "        		</div>"+
        "     		</div>"+
        "		</div>"+
        "<c:if test="${serviceType eq 'GTPPRIME_COLLECTION_SERVICE'}">"+
       	"		<div class='col-md-6 no-padding'>"+
        "			<spring:message code='gtpprime.collection.service.client.config.tab.file.padding.enable' var='label'></spring:message>"+
        "			<spring:message code='gtpprime.collection.service.client.config.tab.file.padding.enable.tooltip' var='tooltip'></spring:message>"+
        "  			<div class='form-group'>"+
        "    			<div class='table-cell-label'>${label}</div>"+
        "      			<div class='input-group '>"+
        "					<select class='form-control table-cell input-sm' id='" + clientListCounter + "_appendFilePaddingInFileName' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }' value='"+enableFilePadding+"'>";
   						        <%
   								 for(TrueFalseEnum enumlist : TrueFalseEnum.values()) { 
   									 %>
   									 block += "<option value='<%=  enumlist.getName() %>'><%= enumlist.toString() %></option>";
   									 <%
   								     }
   								%>
          
   		block += "					</select>"+
   		"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
        "        		</div>"+
        "     		</div>"+
        "		</div>"+
        "</c:if>"+    
        "		<div class='col-md-6 no-padding' style='display:inline-block;'>"+
        "			<spring:message code='netflow.collection.service.client.config.tab.file.seq.range' var='seqRange'></spring:message>"+
        "			<spring:message code='netflow.collection.service.client.config.tab.min.file.seq.range' var='minRange'></spring:message>"+
        "			<spring:message code='netflow.collection.service.client.config.tab.max.file.seq.range' var='maxRange'></spring:message>"+
        "  			<div class='form-group'>"+
        "   			<div class='table-cell-label'>${seqRange}</div>"+
        "      			<div class='input-group'>"+
        "      				<input class='form-control table-cell input-sm' id='" + clientListCounter + "_minFileSeqValue' data-toggle='tooltip' data-placement='bottom'  title='${minRange}' onkeydown='isNumericOnKeyDown(event)' value='"+minSeq+"'/>"+
		"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
		"					<input class='form-control table-cell input-sm' id='" + clientListCounter + "_maxFileSeqValue' data-toggle='tooltip' data-placement='bottom'  title='${maxRange}' onkeydown='isNumericOnKeyDown(event)' value='"+maxSeq+"'/>"+
		"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
        "       		</div>"+
        "     		</div>"+
        "		</div>"+
        "		<div class='col-md-6 no-padding'>"+
        "			<spring:message code='netflow.collection.service.client.config.tab.file.roll.time' var='label'></spring:message>"+
        "			<spring:message code='netflow.collection.service.client.config.tab.file.roll.time.tooltip' var='tooltip'></spring:message>"+
        "  			<div class='form-group'>"+
        "    			<div class='table-cell-label'>${label}<span class='required'>*</span></div>"+
        "      			<div class='input-group '>"+
        "      				<input class='form-control table-cell input-sm' id='" + clientListCounter + "_timeLogRollingUnit' data-toggle='tooltip' data-placement='bottom' title='${tooltip }' value='"+fileRollTime+"'/>"+
		"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
        "        		</div>"+
        "     		</div>"+
        "		</div>"+
        "		<div class='col-md-6 no-padding'>"+
        "			<spring:message code='netflow.collection.service.client.config.tab.file.roll.unit' var='label'></spring:message>"+
        "			<spring:message code='netflow.collection.service.client.config.tab.file.roll.unit.tooltip' var='tooltip'></spring:message>"+
        "  			<div class='form-group'>"+
        "    			<div class='table-cell-label'>${label}<span class='required'>*</span></div>"+
        "      			<div class='input-group '>"+
        "      				<input class='form-control table-cell input-sm' id='" + clientListCounter + "_volLogRollingUnit' data-toggle='tooltip' data-placement='bottom' title='${tooltip }' value='"+fileRollUnit+"'/>"+
		"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
        "        		</div>"+
        "     		</div>"+
        "		</div>"+
        "<c:if test="${serviceType ne 'GTPPRIME_COLLECTION_SERVICE'}">"+
        "		<div class='col-md-6 no-padding'>"+
        "			<spring:message code='netflow.collection.service.client.config.tab.file.compress' var='label'></spring:message>"+
        "			<spring:message code='netflow.collection.service.client.config.tab.file.compress.tooltip' var='tooltip'></spring:message>"+
        "  			<div class='form-group'>"+
        "    			<div class='table-cell-label'>${label}<span class='required'>*</span></div>"+
        "      			<div class='input-group '>"+
        "					<select class='form-control table-cell input-sm' id='" + clientListCounter + "_inputCompressed' data-toggle='tooltip' data-placement='bottom'  title='${tooltip}' value='"+fileCompress+"'>";
					        <%
							 for(TrueFalseEnum enumlist : TrueFalseEnum.values()) { 
								 %>
								 block += "<option value='<%=  enumlist.getName() %>'><%= enumlist.toString() %></option>";
								 <%
							     }
							%>
	    block +=	"		</select>"+
		"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
        "        		</div>"+
        "     		</div>"+
        "		</div>"+
        "</c:if>"+
        "<c:if test="${(serviceType ne 'GTPPRIME_COLLECTION_SERVICE') and (serviceType ne 'RADIUS_COLLECTION_SERVICE') and (serviceType ne 'HTTP2_COLLECTION_SERVICE') and (serviceType ne 'MQTT_COLLECTION_SERVICE') and(serviceType ne 'COAP_COLLECTION_SERVICE') }">"+
        "		<div class='col-md-6 no-padding'>"+
        "			<spring:message code='netflow.collection.service.client.config.tab.client.snmp.enable' var='label'></spring:message>"+
        "			<spring:message code='netflow.collection.service.client.config.tab.client.snmp.enable.tooltip' var='tooltip'></spring:message>"+
        "  			<div class='form-group'>"+
        "    			<div class='table-cell-label'>${label}</div>"+
        "      			<div class='input-group '>"+
        "					<select class='form-control table-cell input-sm' id='" + clientListCounter + "_snmpAlertEnable' data-toggle='tooltip' data-placement='bottom'  title='${tooltip}' onchange=changeSNMPAlterStatus(\'"+clientListCounter+"\');>";
        	        <%
								 for(TrueFalseEnum enumlist : TrueFalseEnum.values()) { 
									 %>
									 block += "<option value='<%=  enumlist.getName() %>'><%= enumlist.toString() %></option>";
									 <%
								     }
								%>
		block +="			</select>"+
		"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
        "        		</div>"+
        "     		</div>"+
        "		</div>"+
        "</c:if>"+
        "<c:if test="${(serviceType ne 'GTPPRIME_COLLECTION_SERVICE') and (serviceType ne 'RADIUS_COLLECTION_SERVICE') and (serviceType ne 'HTTP2_COLLECTION_SERVICE') and (serviceType ne 'MQTT_COLLECTION_SERVICE') and(serviceType ne 'COAP_COLLECTION_SERVICE')}">"+
        "		<div class='col-md-6 no-padding'>"+
        "			<spring:message code='netflow.collection.service.client.config.tab.client.snmp.interval' var='tooltip'></spring:message>"+
        "  			<div class='form-group'>"+
        "    			<div class='table-cell-label'>${tooltip}</div>"+
        "      			<div class='input-group '>"+
        "      				<input class='form-control table-cell input-sm' id='" + clientListCounter + "_alertInterval' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }' onkeydown='isNumericOnKeyDown(event)' value='"+alertInterval+"'/>"+
		"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
        "        		</div>"+
        "     		</div>"+
        "		</div>"+
        "</c:if>"+
        "<c:if test="${serviceType eq 'HTTP2_COLLECTION_SERVICE'}">"+
        "		<div class='col-md-6 no-padding'>"+
        "			<spring:message code='netflow.collection.service.client.config.tab.json.validate' var='label'></spring:message>"+
        "			<spring:message code='netflow.collection.service.client.config.tab.json.validate.tooltip' var='tooltip'></spring:message>"+
        "  			<div class='form-group'>"+
        "    			<div class='table-cell-label'>${label}</div>"+
        "      			<div class='input-group '>"+
        "					<select class='form-control table-cell input-sm' id='" + clientListCounter + "_jsonValidate' data-toggle='tooltip' data-placement='bottom'  title='${tooltip}' value='"+jsonValidate+"'>";
					        <%
							 for(TrueFalseEnum enumlist : TrueFalseEnum.values()) { 
								 %>
								 block += "<option value='<%=  enumlist.getName() %>'><%= enumlist.toString() %></option>";
								 <%
							     }
							%>
	    block +=	"		</select>"+
		"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
        "        		</div>"+
        "     		</div>"+
        "		</div>"+
        "		<div class='col-md-6 no-padding'>"+
        "			<spring:message code='netflow.collection.service.client.config.tab.content.type' var='label'></spring:message>"+
        "			<spring:message code='netflow.collection.service.client.config.tab.content.type.tooltip' var='tooltip'></spring:message>"+
        "  			<div class='form-group'>"+
        "    			<div class='table-cell-label'>${label}</div>"+
        "      			<div class='input-group '>"+
        "					<select class='form-control table-cell input-sm' id='" + clientListCounter + "_contentType' data-toggle='tooltip' data-placement='bottom'  title='${tooltip}' value='"+contentType+"'>";
					        <%
							 for(ContentTypeEnum enumlist : ContentTypeEnum.values()) { 
								 %>
								 block += "<option value='<%=  enumlist.getName() %>'><%= enumlist.toString() %></option>";
								 <%
							     }
							%>
	    block +=	"		</select>"+
		"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
        "        		</div>"+
        "     		</div>"+
        "		</div>"+
        "		<div class='col-md-6 no-padding' id='" + clientListCounter + "_uriDiv'>"+
        "			<spring:message code='netflow.collection.service.client.config.tab.uri' var='label'></spring:message>"+
        "			<spring:message code='netflow.collection.service.client.config.tab.uri.tooltip' var='tooltip'></spring:message>"+
        "  			<div class='form-group'>"+
        "    			<div class='table-cell-label'>${label}</div>"+
        "      			<div class='input-group '>"+
        "					<input class='form-control table-cell input-sm' id='" + clientListCounter + "_uri' data-toggle='tooltip' data-placement='bottom'  title='${tooltip}' value='"+uri+"'/>"+
		"					<span class='input-group-addon add-on last ' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>" +
        "       		</div>"+
        "  	 		</div>"+
        "		</div>"+
        "</c:if>"+
        "<c:if test="${serviceType ne 'RADIUS_COLLECTION_SERVICE'}">"+
        "		<div class='col-md-6 no-padding'>"+
        "			<spring:message code='netflow.collection.service.client.config.tab.enabl.client' var='tooltip'></spring:message>"+
        "  			<div class='form-group'>"+
        "    			<div class='table-cell-label'>${tooltip}<span class='required'>*</span></div>"+
        "      			<div class='input-group '>"+
        "					<select class='form-control table-cell input-sm' id='" + clientListCounter + "_status' data-toggle='tooltip' data-placement='bottom'  title='${tooltip}'>"+
        "				    	<c:forEach var='stateEnum' items='${stateEnum}' >"+
		"							<option value='${stateEnum}'>${stateEnum}</option>"+
		"				    	</c:forEach>"+
		"					</select>"+
		"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
        "        		</div>"+
        "     		</div>"+
        "		</div>"+
        "</c:if>"+
        "<c:if test="${serviceType eq 'RADIUS_COLLECTION_SERVICE'}">"+
		"		<div class='col-md-6 no-padding'>"+
        "			<spring:message code='netflow.collection.service.client.config.tab.secretkey.client' var='label'></spring:message>"+
        "			<spring:message code='netflow.collection.service.client.config.tab.secretkey.client' var='tooltip'></spring:message>"+
        "  			<div class='form-group'>"+
        "    			<div class='table-cell-label'>${label}<span class = 'required'>*</span></div>"+
        "      			<div class='input-group '>"+
        "      				<input class='form-control table-cell input-sm' id='" + clientListCounter + "_sharedSecretKey' data-toggle='tooltip' data-placement='bottom' title='${tooltip }' value='"+sharedSecretKey+"'/>"+
		"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
        "        		</div>"+
        "     		</div>"+
        "		</div>"+
		"</c:if>"+
		"<c:if test="${serviceType eq 'GTPPRIME_COLLECTION_SERVICE'}">"+
        "		<div class='col-md-6 no-padding'>"+
        "			<spring:message code='gtpprime.collection.service.client.config.tab.node.alive.request' var='label'></spring:message>"+
        "			<spring:message code='gtpprime.collection.service.client.config.tab.node.alive.request.tooltip' var='tooltip'></spring:message>"+
        "  			<div class='form-group'>"+
        "    			<div class='table-cell-label'>${label}</div>"+
        "      			<div class='input-group '>"+
        "					<select class='form-control table-cell input-sm' id='" + clientListCounter + "_nodeAliveRequest' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }' value='${client.nodeAliveRequest}' onchange='changeSequenceRangeStatus("+ clientListCounter +")'>";
							        <%
									 for(TrueFalseEnum enumlist : TrueFalseEnum.values()) { 
										 %>
										 block += "<option value='<%=  enumlist.getName() %>'><%= enumlist.toString() %></option>";
										 <%
									     }
									%>
							
							block += "					</select>"+
		"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
        "        		</div>"+
        "     		</div>"+
        "		</div>"+
		"</c:if>"+
		"<c:if test="${serviceType eq 'GTPPRIME_COLLECTION_SERVICE'}">"+
		"		<div class='col-md-6 no-padding'>"+
        "			<spring:message code='gtpprime.collection.service.client.config.tab.echo.request' var='label'></spring:message>"+
        "			<spring:message code='gtpprime.collection.service.client.config.tab.echo.request.tooltip' var='tooltip'></spring:message>"+
        "  			<div class='form-group'>"+
        "    			<div class='table-cell-label'>${label}</div>"+
        "      			<div class='input-group '>"+
        "      				<input class='form-control table-cell input-sm' id='" + clientListCounter + "_echoRequest' data-toggle='tooltip' data-placement='bottom' title='${tooltip }' value='"+echoRequest+"'/>"+
		"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
        "        		</div>"+
        "     		</div>"+
        "		</div>"+
		"</c:if>"+
		"<c:if test="${serviceType eq 'GTPPRIME_COLLECTION_SERVICE'}">"+
		"		<div class='col-md-6 no-padding'>"+
        "			<spring:message code='gtpprime.collection.service.client.config.tab.request.expiry.time' var='label'></spring:message>"+
        "			<spring:message code='gtpprime.collection.service.client.config.tab.request.expiry.time.tooltip' var='tooltip'></spring:message>"+
        "  			<div class='form-group'>"+
        "    			<div class='table-cell-label'>${label}</div>"+
        "      			<div class='input-group '>"+
        "      				<input class='form-control table-cell input-sm' id='" + clientListCounter + "_requestExpiryTime' data-toggle='tooltip' data-placement='bottom' title='${tooltip }' value='"+requestExpiryTime+"'/>"+
		"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
        "        		</div>"+
        "     		</div>"+
        "		</div>"+
		"</c:if>"+
		"<c:if test="${serviceType eq 'GTPPRIME_COLLECTION_SERVICE'}">"+
		"		<div class='col-md-6 no-padding'>"+
        "			<spring:message code='gtpprime.collection.service.client.config.tab.request.retry' var='label'></spring:message>"+
        "			<spring:message code='gtpprime.collection.service.client.config.tab.request.retry.tooltip' var='tooltip'></spring:message>"+
        "  			<div class='form-group'>"+
        "    			<div class='table-cell-label'>${label}</div>"+
        "      			<div class='input-group '>"+
        "      				<input class='form-control table-cell input-sm' id='" + clientListCounter + "_requestRetry' data-toggle='tooltip' data-placement='bottom' title='${tooltip }' value='"+requestRetry+"'/>"+
		"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
        "        		</div>"+
        "     		</div>"+
        "		</div>"+
		"</c:if>"+
		"<c:if test="${serviceType eq 'GTPPRIME_COLLECTION_SERVICE'}">"+
		"		<div class='col-md-6 no-padding'>"+
        "			<spring:message code='gtpprime.collection.service.client.config.tab.request.buffer.count' var='label'></spring:message>"+
        "			<spring:message code='gtpprime.collection.service.client.config.tab.request.buffer.count.tooltip' var='tooltip'></spring:message>"+
        "  			<div class='form-group'>"+
        "    			<div class='table-cell-label'>${label}</div>"+
        "      			<div class='input-group '>"+
        "      				<input class='form-control table-cell input-sm' id='" + clientListCounter + "_requestBufferCount' data-toggle='tooltip' data-placement='bottom' title='${tooltip }' value='"+requestBufferCount+"'/>"+
		"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
        "        		</div>"+
        "     		</div>"+
        "		</div>"+
		"</c:if>"+
		"<c:if test="${serviceType eq 'GTPPRIME_COLLECTION_SERVICE'}">"+
		"		<div class='col-md-6 no-padding'>"+
        "			<spring:message code='gtpprime.collection.service.client.config.tab.redirection.ip' var='label'></spring:message>"+
        "			<spring:message code='gtpprime.collection.service.client.config.tab.redirection.ip.tooltip' var='tooltip'></spring:message>"+
        "  			<div class='form-group'>"+
        "    			<div class='table-cell-label'>${label}</div>"+
        "      			<div class='input-group '>"+
        "      				<input class='form-control table-cell input-sm' id='" + clientListCounter + "_redirectionIp' data-toggle='tooltip' data-placement='bottom' title='${tooltip }' value='"+redirectionIp+"'/>"+
		"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
        "        		</div>"+
        "     		</div>"+
        "		</div>"+
		"</c:if>"+
        "		<div class='col-md-12 no-padding'>"+
        "			<spring:message code='ftp.driver.mgmt.pathlist.max.count.limit' var='tooltip'></spring:message>"+
        "  			<div class='form-group'>"+
        "				<sec:authorize access='hasAuthority(\'UPDATE_COLLECTION_CLIENT\')'>"+
        "      			<div id='" + clientListCounter + "_buttons-div' class='input-group '>"+
        "      				<button type='button' class='btn btn-grey btn-xs ' id='" + clientListCounter + "_updatebtn' onclick=updateClient(\'"+clientListCounter+"\');><spring:message code='btn.label.update'></spring:message></button>&nbsp;" +
        "					<button type='button' class='btn btn-grey btn-xs ' id='" + clientListCounter + "_resetbtn'  onclick=resetClientDetailDiv(\'"+clientListCounter+"\');><spring:message code='btn.label.reset'></spring:message></button>&nbsp;" +
        "       		</div>"+
        "				<div class='pull-right' style='font-size:10px;'><i class='fa fa-square' style='font-size: 9px'></i>&nbsp;&nbsp;<spring:message code='restart.operation.require.message' ></spring:message> </div>"+
        "				</sec:authorize>"+
        "					<div id='" + clientListCounter + "_progress-bar-div' class='input-group' style='display: none;'> "+
        "						<label>Processing Request &nbsp;&nbsp; </label> "+
        "							<img src='img/processing-bar.gif'>"+
		"					</div> "+
        "   		</div>" +
        "		</div>" +
     	"	</div>"+
		"</div>"+
		"</form>";
		$('#clientList').prepend(block);
		$('#' + clientListCounter + '_inputCompressed').val(fileCompress);
		$('#' + clientListCounter + '_jsonValidate').val(jsonValidate);
		$('#' + clientListCounter + '_contentType').val(contentType);
		$('#' + clientListCounter + '_nodeAliveRequest').val(nodeAliveRequest);
		$('#' + clientListCounter + '_appendFileSequenceInFileName').val(enableFileSeq);
		$('#' + clientListCounter + '_appendFilePaddingInFileName').val(enableFilePadding);
		$('#'+ clientListCounter + '_status option[value="DELETED"]').remove();
		$('#'+ clientListCounter + '_snmpAlertEnable option[value="DELETED"]').remove();
		$('#'+ clientListCounter + '_snmpAlertEnable').val(snmpEnable);
		$('#' + clientListCounter + '_enableSecurity').val(enableSecurity);
		$('#' + clientListCounter + '_enableProxy').val(enableProxy);
		$('#' + clientListCounter + '_registerObserver').val(registerObserver);
		$('#' + clientListCounter + '_securityType').val(securityType);
		$('#' + clientListCounter + '_proxySchema').val(proxySchema);
		$('#' + clientListCounter + '_messageType').val(messageType);
		$('#' + clientListCounter + '_requestType').val(requestType);
		$('#' + clientListCounter + '_enableKafka').val(enableKafka);
		$("#"+ clientListCounter +"_kafkaDataSourceConfig").val(kafkaDataSourceId);
		$('#'+ clientListCounter + '_status').val(status);
		$("#"+ clientListCounter+"_rollingType").val(rollingType);
		$("#"+clientListCounter+"_block").collapse("toggle");
		changeRollingTypeOption(clientListCounter);
		changeSNMPAlterStatus(clientListCounter);
		changeSequenceRangeStatus(clientListCounter);
		changeObserverStatus(clientListCounter);
		changeSecurityParameters(clientListCounter);
		changeProxyParameters(clientListCounter);
		changeKafkaParameters(clientListCounter);
	}
		 
	function addNewClientDetail(){
		clientListCounter++;
        
		var block = "<form method='POST' id='" + clientListCounter + "_form'> "+
		"<input type='hidden' id='" + clientListCounter + "_serviceId' name='serviceId' value='${serviceId}'> "+
		"<input type='hidden' id='"+clientListCounter+"_id' name='"+clientListCounter+"_id' value='0'> "+
		"<div class='box box-warning' id='flipbox_"+clientListCounter+"'>"+		
		"	<div class='box-header with-border'> "+
		"		<h3 class='box-title' id='title_"+clientListCounter+ "'><a href='#' class='title2rightfield-icon1-text' id='link_"+clientListCounter+ "' style='font-size: 12px;text-decoration:none'><spring:message code='netflow.collection.service.client.config.tab.header'></spring:message></a></h3>"+
		"		<div class='box-tools pull-right' id='action_"+clientListCounter+ "'>"+
		"			<button class='btn btn-box-tool block-collapse-btn' id='editClient_"+clientListCounter+ "' data-toggle='collapse' data-parent='#driverList' href='#" + clientListCounter + "_block'><i class='fa fa-minus'></i></button>	"+
		"					<sec:authorize access='hasAuthority(\'DELETE_COLLECTION_CLIENT\')'>"+
		"			&nbsp;&nbsp;<a class='ion-ios-close-empty remove-block' style='margin-top:12px;' id='deleteClientPopup_"+clientListCounter+ "' onclick='deleteClientPopup("+' "'+clientListCounter+'" ' + ")'></a>" +
		"					</sec:authorize>"+
		"		</div>"+
		"	</div>	"+
        "	<div class='box-body inline-form accordion-body collapse in' id='" + clientListCounter + "_block'>"+
        "<c:if test="${serviceType eq 'MQTT_COLLECTION_SERVICE'}">"+
        "		<div class='col-md-6 no-padding'>"+
        "			<spring:message code='netflow.collectionService.client.grid.column.topic.nm' var='label'></spring:message>"+
        "			<spring:message code='netflow.collectionService.client.grid.column.topic.nm' var='tooltip'></spring:message>"+
        "  			<div class='form-group'>"+
        "    			<div class='table-cell-label'>${label}<span class='required'>*</span></div>"+
        "      			<div class='input-group '>"+
        "      				<input class='form-control table-cell input-sm' id='" + clientListCounter + "_topicName' data-toggle='tooltip' data-placement='bottom' title='${tooltip }' value='${client.redirectionIp}' />"+
    	"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
        "        		</div>"+
        "     		</div>"+
        "		</div>"+
        "		<div class='col-md-6 no-padding'>"+
        "			<spring:message code='netflow.collection.service.client.config.tab.client.name' var='label'></spring:message>"+
        "			<spring:message code='netflow.collection.service.client.config.tab.client.name.tooltip' var='tooltip'></spring:message>"+
        "  			<div class='form-group'>"+
        "    			<div class='table-cell-label'>${label}<span class='required'>*</span></div>"+
        "      			<div class='input-group '>"+
        "					<input class='form-control table-cell input-sm' id='" + clientListCounter + "_name' value='DEFAULT' data-toggle='tooltip' data-placement='bottom'  title='${tooltip}'/>"+
		"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>" +
        "       		</div>"+
        "  	 		</div>"+
        "		</div>"+
        "</c:if>"+
        "<c:if test="${serviceType ne 'MQTT_COLLECTION_SERVICE'}">"+
        "		<div class='col-md-6 no-padding'>"+
        "			<spring:message code='netflow.collection.service.client.config.tab.client.name' var='label'></spring:message>"+
        "			<spring:message code='netflow.collection.service.client.config.tab.client.name.tooltip' var='tooltip'></spring:message>"+
        "  			<div class='form-group'>"+
        "    			<div class='table-cell-label'>${label}<span class='required'>*</span></div>"+
        "      			<div class='input-group '>"+
        "					<input class='form-control table-cell input-sm' id='" + clientListCounter + "_name' data-toggle='tooltip' data-placement='bottom'  title='${tooltip}'/>"+
		"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>" +
        "       		</div>"+
        "  	 		</div>"+
        "		</div>"+
        "</c:if>"+
        "<c:if test="${serviceType eq 'COAP_COLLECTION_SERVICE'}">"+
        "		<div class='col-md-6 no-padding'>"+
        "			<spring:message code='netflow.collection.service.client.config.tab.resource.name' var='label'></spring:message>"+
        "			<spring:message code='netflow.collection.service.client.config.tab.resource.name.tooltip' var='tooltip'></spring:message>"+
        "  			<div class='form-group'>"+
        "    			<div class='table-cell-label'>${label}<span class='required'>*</span></div>"+
        "      			<div class='input-group '>"+
        "					<input class='form-control table-cell input-sm' id='" + clientListCounter + "_resourcesName' data-toggle='tooltip' data-placement='bottom'  title='${tooltip}'>"+
		"					<span class='input-group-addon add-on last ' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>" +
        "       		</div>"+
        "  	 		</div>"+
        "		</div>"+
        "</c:if>"+
        "<c:if test="${serviceType ne 'MQTT_COLLECTION_SERVICE'}">"+
        "		<div class='col-md-6 no-padding' style='display:inline-block;'>"+
        "			<spring:message code='netflow.collection.service.client.config.tab.client.ip' var='clientIP'></spring:message>"+
        "			<spring:message code='netflow.collection.service.client.config.tab.client.ip.tooltip' var='tooltipIp'></spring:message>"+
        "			<spring:message code='netflow.collection.service.client.config.tab.client.port' var='port'></spring:message>"+
        "			<spring:message code='netflow.collection.service.client.config.tab.client.port.tooltip' var='tooltip'></spring:message>"+
        "  			<div class='input-group'>"+
        "   			<div class='table-cell-label'>${clientIP}<span class='required'>*</span><i class='fa fa-square'></i></div>"+
        "				<div class='col-md-6 no-padding'>"+
        "					<div class='form-group'>"+
        "      					<div class='input-group'>"+
        "      				<input class='form-control table-cell input-sm' id='" + clientListCounter + "_clientIpAddress' data-toggle='tooltip' data-placement='bottom'  title='${tooltipIp}' />"+
		"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
		"						</div>"+
		"					</div>"+
		"				</div>"+
		"				<div class='col-md-6 no-padding'>"+
		"					<div class='form-group'>"+
		"						<div class='input-group'>"+
		"					<input class='form-control table-cell input-sm' id='" + clientListCounter + "_clientPort' data-toggle='tooltip' data-placement='bottom'  title='${tooltip}' onkeydown='isNumericOnKeyDown(event)' />"+
		"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
        "       				</div>"+
        "					</div>"+
        "				</div>"+
        "     		</div>"+
        "		</div>"+
        "</c:if>"+
        "<c:if test="${serviceType eq 'COAP_COLLECTION_SERVICE'}">"+
        "		<div class='col-md-6 no-padding'>"+
        "			<spring:message code='netflow.collection.service.client.config.tab.register.observer' var='label'></spring:message>"+
        "			<spring:message code='netflow.collection.service.client.config.tab.register.observer.tooltip' var='tooltip'></spring:message>"+
        "  			<div class='form-group'>"+
        "    			<div class='table-cell-label'>${label}</div>"+
        "      			<div class='input-group '>"+
        "					<select class='form-control table-cell input-sm' id='" + clientListCounter + "_registerObserver' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }' onchange='changeObserverStatus("+ clientListCounter +")'>";
					        <%
							 for(TrueFalseEnum enumlist : TrueFalseEnum.values()) { 
								 %>
								 block += "<option value='<%=  enumlist.getName() %>'><%= enumlist.toString() %></option>";
								 <%
							     }
							%>
					
     	block += "	        </select>"+
		"	                <span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
        "       		</div>"+
        "  	 		</div>"+
        "		</div>"+
        "		<div class='col-md-6 no-padding' id='" + clientListCounter + "_observerTimeoutDiv'>"+
        "			<spring:message code='netflow.collection.service.client.config.tab.observer.timeout' var='label'></spring:message>"+
        "			<spring:message code='netflow.collection.service.client.config.tab.observer.timeout.tooltip' var='tooltip'></spring:message>"+
        "  			<div class='form-group'>"+
        "    			<div class='table-cell-label'>${label}</div>"+
        "      			<div class='input-group '>"+
        "					<input class='form-control table-cell input-sm' id='" + clientListCounter + "_observerTimeout' data-toggle='tooltip' data-placement='bottom'  title='${tooltip}' onkeydown='isNumericOnKeyDown(event)'/>"+
		"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>" +
        "       		</div>"+
        "  	 		</div>"+
        "		</div>"+
        "		<div class='col-md-6 no-padding' id='" + clientListCounter + "_requestTypeDiv'>"+
        "			<spring:message code='netflow.collection.service.client.config.tab.request.type' var='label'></spring:message>"+
        "			<spring:message code='netflow.collection.service.client.config.tab.request.type.tooltip' var='tooltip'></spring:message>"+
        "  			<div class='form-group'>"+
        "    			<div class='table-cell-label'>${label}</div>"+
        "      			<div class='input-group '>"+
        "					<select class='form-control table-cell input-sm' id='" + clientListCounter + "_requestType' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }'>";
					        <%
							 for(RequestTypeEnum enumlist : RequestTypeEnum.values()) { 
								 %>
								 block += "<option value='<%=  enumlist.toString() %>'><%= enumlist.toString() %></option>";
								 <%
							     }
							%>
					
     	block += "	        </select>"+
		"	                <span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
        "       		</div>"+
        "  	 		</div>"+
        "		</div>"+  
        "		<div class='col-md-6 no-padding'>"+
        "			<spring:message code='netflow.collection.service.client.config.tab.message.type' var='label'></spring:message>"+
        "			<spring:message code='netflow.collection.service.client.config.tab.message.type.tooltip' var='tooltip'></spring:message>"+
        "  			<div class='form-group'>"+
        "    			<div class='table-cell-label'>${label}</div>"+
        "      			<div class='input-group '>"+
        "					<select class='form-control table-cell input-sm' id='" + clientListCounter + "_messageType' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }'>";
					        <%
							 for(MessageTypeEnum enumlist : MessageTypeEnum.values()) { 
								 %>
								 block += "<option value='<%=  enumlist.toString() %>'><%= enumlist.toString() %></option>";
								 <%
							     }
							%>
					
     	block += "	        </select>"+
		"	                <span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
        "       		</div>"+
        "  	 		</div>"+
        "		</div>"+
        "		<div class='col-md-6 no-padding' id='" + clientListCounter + "_requestTimeoutDiv'>"+
        "			<spring:message code='netflow.collection.service.client.config.tab.request.timeout' var='label'></spring:message>"+
        "			<spring:message code='netflow.collection.service.client.config.tab.request.timeout.tooltip' var='tooltip'></spring:message>"+
        "  			<div class='form-group'>"+
        "    			<div class='table-cell-label'>${label}</div>"+
        "      			<div class='input-group '>"+
        "					<input class='form-control table-cell input-sm' id='" + clientListCounter + "_requestTimeout' data-toggle='tooltip' data-placement='bottom'  title='${tooltip}' onkeydown='isNumericOnKeyDown(event)'/>"+
		"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>" +
        "       		</div>"+
        "  	 		</div>"+
        "		</div>"+
        "		<div class='col-md-6 no-padding' id='" + clientListCounter + "_requestRetryCountDiv'>"+
        "			<spring:message code='netflow.collection.service.client.config.tab.request.retry.count' var='label'></spring:message>"+
        "			<spring:message code='netflow.collection.service.client.config.tab.request.retry.count.tooltip' var='tooltip'></spring:message>"+
        "  			<div class='form-group'>"+
        "    			<div class='table-cell-label'>${label}</div>"+
        "      			<div class='input-group '>"+
        "					<input class='form-control table-cell input-sm' id='" + clientListCounter + "_requestRetryCount' data-toggle='tooltip' data-placement='bottom'  title='${tooltip}' onkeydown='isNumericOnKeyDown(event)'/>"+
		"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>" +
        "       		</div>"+
        "  	 		</div>"+
        "		</div>"+
        "		<div class='col-md-6 no-padding' id='" + clientListCounter + "_reqExecutionIntervalDiv'>"+
        "			<spring:message code='netflow.collection.service.client.config.tab.request.execution.interval' var='label'></spring:message>"+
        "			<spring:message code='netflow.collection.service.client.config.tab.request.execution.interval.tooltip' var='tooltip'></spring:message>"+
        "  			<div class='form-group'>"+
        "    			<div class='table-cell-label'>${label}</div>"+
        "      			<div class='input-group '>"+
        "					<input class='form-control table-cell input-sm' id='" + clientListCounter + "_reqExecutionInterval' data-toggle='tooltip' data-placement='bottom'  title='${tooltip}' onkeydown='isNumericOnKeyDown(event)'/>"+
		"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>" +
        "       		</div>"+
        "  	 		</div>"+
        "		</div>"+
        "		<div class='col-md-6 no-padding' id='" + clientListCounter + "_reqExecutionFreqDiv'>"+
        "			<spring:message code='netflow.collection.service.client.config.tab.request.execution.frequency' var='label'></spring:message>"+
        "			<spring:message code='netflow.collection.service.client.config.tab.request.execution.frequency.tooltip' var='tooltip'></spring:message>"+
        "  			<div class='form-group'>"+
        "    			<div class='table-cell-label'>${label}</div>"+
        "      			<div class='input-group '>"+
        "					<input class='form-control table-cell input-sm' id='" + clientListCounter + "_reqExecutionFreq' data-toggle='tooltip' data-placement='bottom'  title='${tooltip}' onkeydown='isNumericOnKeyDown(event)'/>"+
		"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>" +
        "       		</div>"+
        "  	 		</div>"+
        "		</div>"+
        "		<div class='col-md-6 no-padding' id='" + clientListCounter + "_exchangeLifeTimeDiv'>"+
        "			<spring:message code='netflow.collection.service.client.config.tab.exchange.lifetime' var='label'></spring:message>"+
        "			<spring:message code='netflow.collection.service.client.config.tab.exchange.lifetime.tooltip' var='tooltip'></spring:message>"+
        "  			<div class='form-group'>"+
        "    			<div class='table-cell-label'>${label}</div>"+
        "      			<div class='input-group '>"+
        "					<input class='form-control table-cell input-sm' id='" + clientListCounter + "_exchangeLifeTime' data-toggle='tooltip' data-placement='bottom'  title='${tooltip}' onkeydown='isNumericOnKeyDown(event)'/>"+
		"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>" +
        "       		</div>"+
        "  	 		</div>"+
        "		</div>"+
        "		<div class='col-md-6 no-padding' style='display:none'>"+
        "			<spring:message code='netflow.collection.service.client.config.tab.request.content.format' var='label'></spring:message>"+
        "			<spring:message code='netflow.collection.service.client.config.tab.request.content.format.tooltip' var='tooltip'></spring:message>"+
        "  			<div class='form-group'>"+
        "    			<div class='table-cell-label'>${label}</div>"+
        "      			<div class='input-group '>"+
        "					<select class='form-control table-cell input-sm' id='" + clientListCounter + "_contentFormat' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }'>";
					        <%
							 for(ContentFormatTypeEnum enumlist : ContentFormatTypeEnum.values()) { 
								 %>
								 block += "<option value='<%=  enumlist.toString() %>'><%= enumlist.toString() %></option>";
								 <%
							     }
							%>
					
     	block += "	        </select>"+
		"	                <span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
        "       		</div>"+
        "  	 		</div>"+
        "		</div>"+
        "		<div class='col-md-6 no-padding' style='display:none'>"+
        "			<spring:message code='netflow.collection.service.client.config.tab.request.payload' var='label'></spring:message>"+
        "			<spring:message code='netflow.collection.service.client.config.tab.request.payload.tooltip' var='tooltip'></spring:message>"+
        "  			<div class='form-group'>"+
        "    			<div class='table-cell-label'>${label}</div>"+
        "      			<div class='input-group '>"+
        "					<input class='form-control table-cell input-sm' id='" + clientListCounter + "_payload' data-toggle='tooltip' data-placement='bottom'  title='${tooltip}'/>"+
		"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>" +
        "       		</div>"+
        "  	 		</div>"+
        "		</div>"+

        "		<div class='col-md-6 no-padding'>"+
        "			<spring:message code='netflow.collection.service.client.config.tab.enable.security' var='label'></spring:message>"+
        "			<spring:message code='netflow.collection.service.client.config.tab.enable.security.tooltip' var='tooltip'></spring:message>"+
        "  			<div class='form-group'>"+
        "    			<div class='table-cell-label'>${label}</div>"+
        "      			<div class='input-group '>"+
        "					<select class='form-control table-cell input-sm' id='" + clientListCounter + "_enableSecurity' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }' onchange='changeSecurityParameters("+ clientListCounter +")'>";
					        <%
							 for(TrueFalseEnum enumlist : TrueFalseEnum.values()) { 
								 %>
								 block += "<option value='<%=  enumlist.getName() %>'><%= enumlist.toString() %></option>";
								 <%
							     }
							%>
					
     	block += "	        </select>"+
		"	                <span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
        "       		</div>"+
        "  	 		</div>"+
        "		</div> "+
        "		<div class='col-md-6 no-padding' id='" + clientListCounter + "_securityTypeDiv'>"+
        "			<spring:message code='netflow.collection.service.client.config.tab.security.type' var='label'></spring:message>"+
        "			<spring:message code='netflow.collection.service.client.config.tab.security.type.tooltip' var='tooltip'></spring:message>"+
        "  			<div class='form-group'>"+
        "    			<div class='table-cell-label'>${label}</div>"+
        "      			<div class='input-group '>"+
        "					<select class='form-control table-cell input-sm' id='" + clientListCounter + "_securityType' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }' onchange='changeSecurityType("+ clientListCounter +")'>";
					        <%
							 for(SecurityTypeEnum enumlist : SecurityTypeEnum.values()) { 
								 %>
								 block += "<option value='<%=  enumlist.toString() %>'><%= enumlist.toString() %></option>";
								 <%
							     }
							%>
					
     	block += "	        </select>"+
		"	                <span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
        "       		</div>"+
        "  	 		</div>"+
        "		</div> "+
        "		<div class='col-md-6 no-padding' id='" + clientListCounter + "_securityIdentityDiv'>"+
        "			<spring:message code='netflow.collection.service.client.config.tab.security.identity' var='label'></spring:message>"+
        "			<spring:message code='netflow.collection.service.client.config.tab.security.identity.tooltip' var='tooltip'></spring:message>"+
        "  			<div class='form-group'>"+
        "    			<div class='table-cell-label'>${label}</div>"+
        "      			<div class='input-group '>"+
        "      				<input class='form-control table-cell input-sm' id='" + clientListCounter + "_securityIdentity' data-toggle='tooltip' data-placement='bottom' title='${tooltip }'/>"+
		"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
        "        		</div>"+
        "     		</div>"+
        "		</div>"+
        "		<div class='col-md-6 no-padding' id='" + clientListCounter + "_securityKeyDiv'>"+
        "			<spring:message code='netflow.collection.service.client.config.tab.security.key' var='label'></spring:message>"+
        "			<spring:message code='netflow.collection.service.client.config.tab.security.key.tooltip' var='tooltip'></spring:message>"+
        "  			<div class='form-group'>"+
        "    			<div class='table-cell-label'>${label}</div>"+
        "      			<div class='input-group '>"+
        "      				<input class='form-control table-cell input-sm' id='" + clientListCounter + "_securityKey' data-toggle='tooltip' data-placement='bottom' title='${tooltip }'/>"+
		"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
        "        		</div>"+
        "     		</div>"+
        "		</div>"+
        "		<div class='col-md-6 no-padding' id='" + clientListCounter + "_secCerLocationDiv'>"+
        "			<spring:message code='netflow.collection.service.client.config.tab.security.certificate.location' var='label'></spring:message>"+
        "			<spring:message code='netflow.collection.service.client.config.tab.security.certificate.location.tooltip' var='tooltip'></spring:message>"+
        "  			<div class='form-group'>"+
        "    			<div class='table-cell-label'>${label}<span class='required'>*</span></div>"+
        "      			<div class='input-group '>"+
        "      				<input class='form-control table-cell input-sm' id='" + clientListCounter + "_secCerLocation' data-toggle='tooltip' data-placement='bottom' title='${tooltip }'/>"+
		"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
        "        		</div>"+
        "     		</div>"+
        "		</div>"+
        "		<div class='col-md-6 no-padding' id='" + clientListCounter + "_secCerPasswdDiv'>"+
        "			<spring:message code='netflow.collection.service.client.config.tab.security.certificate.password' var='label'></spring:message>"+
        "			<spring:message code='netflow.collection.service.client.config.tab.security.certificate.password.tooltip' var='tooltip'></spring:message>"+
        "  			<div class='form-group'>"+
        "    			<div class='table-cell-label'>${label}</div>"+
        "      			<div class='input-group '>"+
        "      				<input class='form-control table-cell input-sm' id='" + clientListCounter + "_secCerPasswd' data-toggle='tooltip' data-placement='bottom' title='${tooltip }'/>"+
		"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
        "        		</div>"+
        "     		</div>"+
        "		</div>"+

		"		<div class='col-md-6 no-padding'>"+
        "			<spring:message code='netflow.collection.service.client.config.tab.enable.proxy' var='label'></spring:message>"+
        "			<spring:message code='netflow.collection.service.client.config.tab.enable.proxy.tooltip' var='tooltip'></spring:message>"+
        "  			<div class='form-group'>"+
        "    			<div class='table-cell-label'>${label}</div>"+
        "      			<div class='input-group '>"+
        "					<select class='form-control table-cell input-sm' id='" + clientListCounter + "_enableProxy' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }' onchange='changeProxyParameters("+ clientListCounter +")'>";
					        <%
							 for(TrueFalseEnum enumlist : TrueFalseEnum.values()) { 
								 %>
								 block += "<option value='<%=  enumlist.getName() %>'><%= enumlist.toString() %></option>";
								 <%
							     }
							%>
					
     	block += "	        </select>"+
		"	                <span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
        "       		</div>"+
        "  	 		</div>"+
        "		</div> "+
        "		<div class='col-md-6 no-padding' id='" + clientListCounter + "_proxySchemaDiv'>"+
        "			<spring:message code='netflow.collection.service.client.config.tab.proxy.schema' var='label'></spring:message>"+
        "			<spring:message code='netflow.collection.service.client.config.tab.proxy.schema.tooltip' var='tooltip'></spring:message>"+
        "  			<div class='form-group'>"+
        "    			<div class='table-cell-label'>${label}</div>"+
        "      			<div class='input-group '>"+
        "					<select class='form-control table-cell input-sm' id='" + clientListCounter + "_proxySchema' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }'>";
					        <%
							 for(ProxySchemaTypeEnum enumlist : ProxySchemaTypeEnum.values()) { 
								 %>
								 block += "<option value='<%=  enumlist.toString() %>'><%= enumlist.toString() %></option>";
								 <%
							     }
							%>
					
     	block += "	        </select>"+
		"	                <span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
        "       		</div>"+
        "  	 		</div>"+
        "		</div> "+
        "		<div class='col-md-6 no-padding' id='" + clientListCounter + "_proxyResourcesDiv'>"+
        "			<spring:message code='netflow.collection.service.client.config.tab.proxy.resource.name' var='label'></spring:message>"+
        "			<spring:message code='netflow.collection.service.client.config.tab.proxy.resource.name.tooltip' var='tooltip'></spring:message>"+
        "  			<div class='form-group'>"+
        "    			<div class='table-cell-label'>${label}<span class='required'>*</span></div>"+
        "      			<div class='input-group '>"+
        "					<input class='form-control table-cell input-sm' id='" + clientListCounter + "_proxyResources' data-toggle='tooltip' data-placement='bottom'  title='${tooltip}'/>"+
		"					<span class='input-group-addon add-on last ' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>" +
        "       		</div>"+
        "  	 		</div>"+
        "		</div>"+
        "		<div class='col-md-6 no-padding' style='display:inline-block;' id='" + clientListCounter + "_proxyServerIpDiv'>"+
        "			<spring:message code='netflow.collection.service.client.config.tab.proxy.server.ip' var='proxyIP'></spring:message>"+
        "			<spring:message code='netflow.collection.service.client.config.tab.proxy.server.ip.tooltip' var='tooltipIp'></spring:message>"+
        "			<spring:message code='netflow.collection.service.client.config.tab.proxy.server.port' var='proxyPort'></spring:message>"+
        "			<spring:message code='netflow.collection.service.client.config.tab.proxy.server.port.tooltip' var='tooltip'></spring:message>"+
        "  			<div class='input-group'>"+
        "   			<div class='table-cell-label'>${proxyIP}<span class='required'>*</span><i class='fa fa-square'></i></div>"+
        "				<div class='col-md-6 no-padding'>"+
        "					<div class='form-group'>"+
        "      					<div class='input-group'>"+
        "      				<input class='form-control table-cell input-sm' id='" + clientListCounter + "_proxyServerIp' data-toggle='tooltip' data-placement='bottom'  title='${tooltipIp}'/>"+
		"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
		"						</div>"+
		"					</div>"+
		"				</div>"+
		"				<div class='col-md-6 no-padding'>"+
		"					<div class='form-group'>"+
		"						<div class='input-group'>"+
		"					<input class='form-control table-cell input-sm' id='" + clientListCounter + "_proxyServerPort' data-toggle='tooltip' data-placement='bottom'  title='${tooltip}' onkeydown='isNumericOnKeyDown(event)' />"+
		"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
        "       				</div>"+
        "					</div>"+
        "				</div>"+
        "     		</div>"+
        "		</div>"+
        "		<div class='col-md-6 no-padding'>"+
        "			<spring:message code='netflow.collection.service.client.config.tab.enable.kafka' var='label'></spring:message>"+
        "			<spring:message code='netflow.collection.service.client.config.tab.enable.kafka.tooltip' var='tooltip'></spring:message>"+
        "  			<div class='form-group'>"+
        "    			<div class='table-cell-label'>${label}</div>"+
        "      			<div class='input-group '>"+
        "					<select class='form-control table-cell input-sm' id='" + clientListCounter + "_enableKafka' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }' onchange='changeKafkaParameters("+ clientListCounter +")'>";
					        <%
							 for(TrueFalseEnum enumlist : TrueFalseEnum.values()) { 
								 %>
								 block += "<option value='<%=  enumlist.getName() %>'><%= enumlist.toString() %></option>";
								 <%
							     }
							%>
					
     	block += "	        </select>"+
		"	                <span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
        "       		</div>"+
        "  	 		</div>"+
        "		</div> "+
        "		<div class='col-md-6 no-padding' id='" + clientListCounter + "_kafkaDataSourceConfigDiv'>"+
	    "				<spring:message code='netflow.collection.service.client.config.tab.kafka.datasource' var='label'></spring:message>"+
	    "				<spring:message code='netflow.collection.service.client.config.tab.kafka.datasource.tooltip' var='tooltip'></spring:message>"+
	    "  				<div class='form-group'>"+
		"				<div class='table-cell-label'>${label}</div> "+ 
	    "      				<div class='input-group '>"+
		"  						<select class='form-control table-cell input-sm' id='"+clientListCounter+"_kafkaDataSourceConfig' tabindex='14' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }'>" +
	    "   						<c:forEach var='kafkaDataSource' items='${kafkaDataSourceList}'>"+
	    "    							<option value='${kafkaDataSource.id}'>${kafkaDataSource.name}</option>" +
	    "    						</c:forEach>" +
	    "   					</select>" +
		"						<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
	    "        			</div>"+
	    "    			</div>"+
	    "		</div>"+
	    "		<div class='col-md-6 no-padding' id='" + clientListCounter + "_topicNameDiv'>"+
        "			<spring:message code='netflow.collectionService.client.grid.column.topic.nm' var='label'></spring:message>"+
        "			<spring:message code='netflow.collectionService.client.grid.column.topic.nm' var='tooltip'></spring:message>"+
        "  			<div class='form-group'>"+
        "    			<div class='table-cell-label'>${label}</div>"+
        "      			<div class='input-group '>"+
        "      				<input class='form-control table-cell input-sm' id='" + clientListCounter + "_topicName' data-toggle='tooltip' data-placement='bottom' title='${tooltip }' value='${client.topicName}' />"+
    	"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
        "        		</div>"+
        "     		</div>"+
        "		</div>"+
        "</c:if>"+
        "		<div class='col-md-6 no-padding'>"+
        "			<spring:message code='netflow.collection.service.client.config.tab.file.loc' var='label'></spring:message>"+
        "			<spring:message code='netflow.collection.service.client.config.tab.file.loc.tooltip' var='tooltip'></spring:message>"+
        "  			<div class='form-group'>"+
        "    			<div class='table-cell-label'>${label}<span class='required'>*</span><i class='fa fa-square'></i></div>"+
        "      			<div class='input-group '>"+
        "					<input class='form-control table-cell input-sm' id='" + clientListCounter + "_outFileLocation' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }'/>"+
        "					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
        "        		</div>"+
        "     		</div>"+
        "		</div> ";
        
        if(isCollection==true){
        	block += "<div class='col-md-6 no-padding'>"+
        "			<spring:message code='netflow.collection.service.client.config.tab.file.bkp.path' var='tooltip'></spring:message>"+
        "  			<div class='form-group'>"+
        "    			<div class='table-cell-label'>${tooltip}</div>"+
        "      			<div class='input-group '>"+
        "					<input class='form-control table-cell input-sm' id='" + clientListCounter + "_bkpBinaryfileLocation' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }'/>"+
        "					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
        "        		</div>"+
        "     		</div>"+
        "		</div> "; 
        }
        
        block +=  "	<div class='col-md-6 no-padding'>"+
        "			<spring:message code='netflow.collection.service.client.config.tab.file.format' var='label'></spring:message>"+
        "			<spring:message code='netflow.collection.service.client.config.tab.file.format.tooltip' var='tooltip'></spring:message>"+
        "  			<div class='form-group'>"+
        "    			<div class='table-cell-label'>${label}<span class='required'>*</span></div>";
        
        if(serviceType==netflowBinary_Collection){
        block +="      	<div class='input-group '>"+
        "      				<input class='form-control table-cell input-sm' id='" + clientListCounter + "_fileNameFormat' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }' value='NatflowBinary{yyyyMMddHHmmssSSS}.log' />"+
		"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
        "        		</div>";
        
        }
        else if(serviceType==netflow_Collection){
        block +="      	<div class='input-group '>"+
        "     				<input class='form-control table-cell input-sm' id='" + clientListCounter + "_fileNameFormat' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }' value='Natflow{yyyyMMddHHmmssSSS}.log' />"+
    	"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
        "        		</div>";        	
        }
        else if(serviceType==mqtt){
            block +="      	<div class='input-group '>"+
            "     				<input class='form-control table-cell input-sm' id='" + clientListCounter + "_fileNameFormat' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }' value='Mqtt{yyyyMMddHHmmssSSS}.log' />"+
        	"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
            "        		</div>";        	
        }
        else if(serviceType==coap){
            block +="      	<div class='input-group '>"+
            "     				<input class='form-control table-cell input-sm' id='" + clientListCounter + "_fileNameFormat' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }' value='CoAP{yyyyMMddHHmmssSSS}.log' />"+
        	"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
            "        		</div>";        	
        }
        else if(serviceType==gtpPrime_Collection){
            block +="      	<div class='input-group '>"+
            "     				<input class='form-control table-cell input-sm' id='" + clientListCounter + "_fileNameFormat' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }' value='GTP{yyyyMMddHHmmssSSS}.log' />"+
        	"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
            "        		</div>";        	
            }
        else if(serviceType==syslog){
        block +="      	<div class='input-group '>"+
        "      				<input class='form-control table-cell input-sm' id='" + clientListCounter + "_fileNameFormat' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }' value='SYSLOG{yyyyMMddHHmmssSSS}.log' />"+
    	"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
        "        		</div>";        	
        }
        else if(serviceType==http2_collection){
            block +="      	<div class='input-group '>"+
            "      				<input class='form-control table-cell input-sm' id='" + clientListCounter + "_fileNameFormat' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }' value='Http2{yyyyMMddHHmmssSSS}.log' />"+
        	"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
            "        		</div>";        	
        }
        else{
            block +="      	<div class='input-group '>"+
            "      				<input class='form-control table-cell input-sm' id='" + clientListCounter + "_fileNameFormat' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }' value='RADIUS{yyyyMMddHHmmssSSS}.log' />"+
        	"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
            "        		</div>";        	
            }
        block +="     		</div>"+
        "		</div>"+
        "		<div class='col-md-6 no-padding'>"+
        "			<spring:message code='netflow.collection.service.client.config.tab.file.seq.enable' var='label'></spring:message>"+
        "			<spring:message code='netflow.collection.service.client.config.tab.file.seq.enable.tooltip' var='tooltip'></spring:message>"+
        "  			<div class='form-group'>"+
        "    			<div class='table-cell-label'>${label}</div>"+
        "      			<div class='input-group '>"+
        "					<select class='form-control table-cell input-sm' id='" + clientListCounter + "_appendFileSequenceInFileName' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }' value='${client.appendFileSequenceInFileName}' onchange='changeSequenceRangeStatus("+ clientListCounter +")'>";
						        <%
								 for(TrueFalseEnum enumlist : TrueFalseEnum.values()) { 
									 %>
									 block += "<option value='<%=  enumlist.getName() %>'><%= enumlist.toString() %></option>";
									 <%
								     }
								%>
       
		block += "					</select>"+
		"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
        "        		</div>"+
        "     		</div>"+
        "		</div>"+
        "<c:if test="${serviceType eq 'GTPPRIME_COLLECTION_SERVICE'}">"+
        "		<div class='col-md-6 no-padding'>"+
        "			<spring:message code='gtpprime.collection.service.client.config.tab.file.padding.enable' var='label'></spring:message>"+
        "			<spring:message code='gtpprime.collection.service.client.config.tab.file.padding.enable.tooltip' var='tooltip'></spring:message>"+
        "  			<div class='form-group'>"+
        "    			<div class='table-cell-label'>${label}</div>"+
        "      			<div class='input-group '>"+
        "					<select class='form-control table-cell input-sm' id='" + clientListCounter + "_appendFilePaddingInFileName' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }' value='${client.appendFilePaddingInFileName}'>";
						        <%
								 for(TrueFalseEnum enumlist : TrueFalseEnum.values()) { 
									 %>
									 block += "<option value='<%=  enumlist.getName() %>'><%= enumlist.toString() %></option>";
									 <%
								     }
								%>
       
		block += "					</select>"+
		"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
        "        		</div>"+
        "     		</div>"+
        "		</div>"+
        "</c:if>"+
        "		<div class='col-md-6 no-padding' style='display:inline-block;'>"+
        "			<spring:message code='netflow.collection.service.client.config.tab.file.seq.range' var='seqRange'></spring:message>"+
        "			<spring:message code='netflow.collection.service.client.config.tab.min.file.seq.range' var='minRange'></spring:message>"+
        "			<spring:message code='netflow.collection.service.client.config.tab.max.file.seq.range' var='maxRange'></spring:message>"+
        "  			<div class='form-group'>"+
        "   			<div class='table-cell-label'>${seqRange}<span class = 'required'>*</span></div>"+
        "      			<div class='input-group'>"+
        "      				<input class='form-control table-cell input-sm' id='" + clientListCounter + "_minFileSeqValue' data-toggle='tooltip' data-placement='bottom' title='${minRange}' onkeydown='isNumericOnKeyDown(event)' />"+
		"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
		"					<input class='form-control table-cell input-sm' id='" + clientListCounter + "_maxFileSeqValue' data-toggle='tooltip' data-placement='bottom'  title='${maxRange}' onkeydown='isNumericOnKeyDown(event)' />"+
		"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
        "       		</div>"+
        "     		</div>"+
        "		</div>"+
        "		<div class='col-md-6 no-padding'>"+
        "			<spring:message code='netflow.collection.service.client.config.tab.file.roll.time' var='label'></spring:message>"+
        "			<spring:message code='netflow.collection.service.client.config.tab.file.roll.time.tooltip' var='tooltip'></spring:message>"+
        "  			<div class='form-group'>"+
        "    			<div class='table-cell-label'>${label}<span class='required'>*</span></div>"+
        "      			<div class='input-group '>"+
        "      				<input class='form-control table-cell input-sm' id='" + clientListCounter + "_timeLogRollingUnit' data-toggle='tooltip' data-placement='bottom' title='${tooltip }' value='${client.timeLogRollingUnit}' />"+
		"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
        "        		</div>"+
        "     		</div>"+
        "		</div>"+
        "		<div class='col-md-6 no-padding'>"+
        "			<spring:message code='netflow.collection.service.client.config.tab.file.roll.unit' var='label'></spring:message>"+
        "			<spring:message code='netflow.collection.service.client.config.tab.file.roll.unit.tooltip' var='tooltip'></spring:message>"+
        "  			<div class='form-group'>"+
        "    			<div class='table-cell-label'>${label}<span class='required'>*</span></div>"+
        "      			<div class='input-group '>"+
        "      				<input class='form-control table-cell input-sm' id='" + clientListCounter + "_volLogRollingUnit' data-toggle='tooltip' data-placement='bottom' title='${tooltip }' value='${client.volLogRollingUnit}'  />"+
		"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
        "        		</div>"+
        "     		</div>"+
        "		</div>"+
        "<c:if test="${serviceType ne 'GTPPRIME_COLLECTION_SERVICE'}">"+
        "		<div class='col-md-6 no-padding'>"+
        "			<spring:message code='netflow.collection.service.client.config.tab.file.compress' var='label'></spring:message>"+
        "			<spring:message code='netflow.collection.service.client.config.tab.file.compress.tooltip' var='tooltip'></spring:message>"+
        "  			<div class='form-group'>"+
        "    			<div class='table-cell-label'>${label}<span class='required'>*</span></div>"+
        "      			<div class='input-group '>"+
        "					<select class='form-control table-cell input-sm' id='" + clientListCounter + "_inputCompressed' data-toggle='tooltip' data-placement='bottom'  title='${tooltip}' >";
					        <%
							 for(TrueFalseEnum enumlist : TrueFalseEnum.values()) { 
								 %>
								 block += "<option value='<%=  enumlist.getName() %>'><%= enumlist.toString() %></option>";
								 <%
							     }
							%>
	    block +=	"		</select>"+
		"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
        "        		</div>"+
        "     		</div>"+
        "		</div>"+
        "<c:if test="${(serviceType ne 'HTTP2_COLLECTION_SERVICE') and (serviceType ne 'MQTT_COLLECTION_SERVICE') and (serviceType ne 'RADIUS_COLLECTION_SERVICE') and (serviceType ne 'COAP_COLLECTION_SERVICE')}">"+
        "		<div class='col-md-6 no-padding'>"+
        "			<spring:message code='netflow.collection.service.client.config.tab.client.snmp.enable' var='label'></spring:message>"+
        "			<spring:message code='netflow.collection.service.client.config.tab.client.snmp.enable.tooltip' var='tooltip'></spring:message>"+
        "  			<div class='form-group'>"+
        "    			<div class='table-cell-label'>${label}</div>"+
        "      			<div class='input-group '>"+
        "					<select class='form-control table-cell input-sm' id='" + clientListCounter + "_snmpAlertEnable' data-toggle='tooltip' data-placement='bottom'  title='${tooltip}' onchange=changeSNMPAlterStatus(\'"+clientListCounter+"\');>";
					        <%
							 for(TrueFalseEnum enumlist : TrueFalseEnum.values()) { 
								 %>
								 block += "<option value='<%=  enumlist.getName() %>'><%= enumlist.toString() %></option>";
								 <%
							     }
							%>
		block +="			</select>"+
		"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
        "        		</div>"+
        "     		</div>"+
        "		</div>"+
        "		<div class='col-md-6 no-padding'>"+
        "			<spring:message code='netflow.collection.service.client.config.tab.client.snmp.interval' var='tooltip'></spring:message>"+
        "  			<div class='form-group'>"+
        "    			<div class='table-cell-label'>${tooltip}</div>"+
        "      			<div class='input-group '>"+
        "      				<input class='form-control table-cell input-sm' id='" + clientListCounter + "_alertInterval' data-toggle='tooltip' data-placement='bottom' onkeydown='isNumericOnKeyDown(event)'  title='${tooltip }'/>"+
		"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
        "        		</div>"+
        "     		</div>"+
        "		</div>"+
        "</c:if>"+
        "</c:if>"+
        "<c:if test="${serviceType eq 'HTTP2_COLLECTION_SERVICE'}">"+
        "		<div class='col-md-6 no-padding'>"+
        "			<spring:message code='netflow.collection.service.client.config.tab.json.validate' var='label'></spring:message>"+
        "			<spring:message code='netflow.collection.service.client.config.tab.json.validate.tooltip' var='tooltip'></spring:message>"+
        "  			<div class='form-group'>"+
        "    			<div class='table-cell-label'>${label}</div>"+
        "      			<div class='input-group '>"+
        "					<select class='form-control table-cell input-sm' id='" + clientListCounter + "_jsonValidate' data-toggle='tooltip' data-placement='bottom'  title='${tooltip}'>";
					        <%
							 for(TrueFalseEnum enumlist : TrueFalseEnum.values()) { 
								 %>
								 block += "<option value='<%=  enumlist.getName() %>'><%= enumlist.toString() %></option>";
								 <%
							     }
							%>
	    block +=	"		</select>"+
		"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
        "        		</div>"+
        "     		</div>"+
        "		</div>"+
        "		<div class='col-md-6 no-padding'>"+
        "			<spring:message code='netflow.collection.service.client.config.tab.content.type' var='label'></spring:message>"+
        "			<spring:message code='netflow.collection.service.client.config.tab.content.type.tooltip' var='tooltip'></spring:message>"+
        "  			<div class='form-group'>"+
        "    			<div class='table-cell-label'>${label}</div>"+
        "      			<div class='input-group '>"+
        "					<select class='form-control table-cell input-sm' id='" + clientListCounter + "_contentType' data-toggle='tooltip' data-placement='bottom'  title='${tooltip}'>";
					        <%
							 for(ContentTypeEnum enumlist : ContentTypeEnum.values()) { 
								 %>
								 block += "<option value='<%=  enumlist.getName() %>'><%= enumlist.toString() %></option>";
								 <%
							     }
							%>
	    block +=	"		</select>"+
		"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
        "        		</div>"+
        "     		</div>"+
        "		</div>"+
        "		<div class='col-md-6 no-padding' id='" + clientListCounter + "_uriDiv'>"+
        "			<spring:message code='netflow.collection.service.client.config.tab.uri' var='label'></spring:message>"+
        "			<spring:message code='netflow.collection.service.client.config.tab.uri.tooltip' var='tooltip'></spring:message>"+
        "  			<div class='form-group'>"+
        "    			<div class='table-cell-label'>${label}</div>"+
        "      			<div class='input-group '>"+
        "					<input class='form-control table-cell input-sm' id='" + clientListCounter + "_uri' data-toggle='tooltip' data-placement='bottom'  title='${tooltip}'/>"+
		"					<span class='input-group-addon add-on last ' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>" +
        "       		</div>"+
        "  	 		</div>"+
        "		</div>"+
        "</c:if>"+
        "<c:if test="${serviceType ne 'RADIUS_COLLECTION_SERVICE'}">"+
        "		<div class='col-md-6 no-padding'>"+
        "			<spring:message code='netflow.collection.service.client.config.tab.enabl.client' var='tooltip'></spring:message>"+
        "  			<div class='form-group'>"+
        "    			<div class='table-cell-label'>${tooltip}<span class='required'>*</span></div>"+
        "      			<div class='input-group '>"+
        "					<select class='form-control table-cell input-sm' id='" + clientListCounter + "_status' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }'>"+
        "				    	<c:forEach var='stateEnum' items='${stateEnum}' >"+
		"							<option value='${stateEnum}'>${stateEnum}</option>"+
		"				    	</c:forEach>" +
		"					</select>"+
		"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
        "        		</div>"+
        "     		</div>"+
        "		</div>"+
        "</c:if>"+
        "<c:if test="${serviceType eq 'GTPPRIME_COLLECTION_SERVICE'}">"+
        "		<div class='col-md-6 no-padding'>"+
        "			<spring:message code='gtpprime.collection.service.client.config.tab.node.alive.request' var='label'></spring:message>"+
        "			<spring:message code='gtpprime.collection.service.client.config.tab.node.alive.request.tooltip' var='tooltip'></spring:message>"+
        "  			<div class='form-group'>"+
        "    			<div class='table-cell-label'>${label}</div>"+
        "      			<div class='input-group '>"+
        "					<select class='form-control table-cell input-sm' id='" + clientListCounter + "_nodeAliveRequest' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }' value='${client.nodeAliveRequest}' onchange='changeSequenceRangeStatus("+ clientListCounter +")'>";
						        <%
								 for(TrueFalseEnum enumlist : TrueFalseEnum.values()) { 
									 %>
									 block += "<option value='<%=  enumlist.getName() %>'><%= enumlist.toString() %></option>";
									 <%
								     }
								%>
       
		block += "					</select>"+
		"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
        "        		</div>"+
        "     		</div>"+
        "		</div>"+
        "</c:if>"+
        "<c:if test="${serviceType eq 'GTPPRIME_COLLECTION_SERVICE'}">"+
        "		<div class='col-md-6 no-padding'>"+
        "			<spring:message code='gtpprime.collection.service.client.config.tab.echo.request' var='label'></spring:message>"+
        "			<spring:message code='gtpprime.collection.service.client.config.tab.echo.request.tooltip' var='tooltip'></spring:message>"+
        "  			<div class='form-group'>"+
        "    			<div class='table-cell-label'>${label}<span class = 'required'>*</span></div>"+
        "      			<div class='input-group '>"+
        "      				<input class='form-control table-cell input-sm' id='" + clientListCounter + "_echoRequest' data-toggle='tooltip' data-placement='bottom' title='${tooltip }' value='${client.echoRequest}' />"+
		"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
        "        		</div>"+
        "     		</div>"+
        "		</div>"+
        "</c:if>"+
		"<c:if test="${serviceType eq 'GTPPRIME_COLLECTION_SERVICE'}">"+
        "		<div class='col-md-6 no-padding'>"+
        "			<spring:message code='gtpprime.collection.service.client.config.tab.request.expiry.time' var='label'></spring:message>"+
        "			<spring:message code='gtpprime.collection.service.client.config.tab.request.expiry.time.tooltip' var='tooltip'></spring:message>"+
        "  			<div class='form-group'>"+
        "    			<div class='table-cell-label'>${label}<span class = 'required'>*</span></div>"+
        "      			<div class='input-group '>"+
        "      				<input class='form-control table-cell input-sm' id='" + clientListCounter + "_requestExpiryTime' data-toggle='tooltip' data-placement='bottom' title='${tooltip }' value='${client.requestExpiryTime}' />"+
		"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
        "        		</div>"+
        "     		</div>"+
        "		</div>"+
        "</c:if>"+
		"<c:if test="${serviceType eq 'GTPPRIME_COLLECTION_SERVICE'}">"+
        "		<div class='col-md-6 no-padding'>"+
        "			<spring:message code='gtpprime.collection.service.client.config.tab.request.retry' var='label'></spring:message>"+
        "			<spring:message code='gtpprime.collection.service.client.config.tab.request.retry.tooltip' var='tooltip'></spring:message>"+
        "  			<div class='form-group'>"+
        "    			<div class='table-cell-label'>${label}<span class = 'required'>*</span></div>"+
        "      			<div class='input-group '>"+
        "      				<input class='form-control table-cell input-sm' id='" + clientListCounter + "_requestRetry' data-toggle='tooltip' data-placement='bottom' title='${tooltip }' value='${client.requestRetry}' />"+
		"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
        "        		</div>"+
        "     		</div>"+
        "		</div>"+
        "</c:if>"+
        "<c:if test="${serviceType eq 'RADIUS_COLLECTION_SERVICE'}">"+
        "		<div class='col-md-6 no-padding'>"+
        "			<spring:message code='gtpprime.collection.service.client.config.tab.request.expiry.time' var='label'></spring:message>"+
        "			<spring:message code='gtpprime.collection.service.client.config.tab.request.expiry.time.tooltip' var='tooltip'></spring:message>"+
        "  			<div class='form-group'>"+
        "    			<div class='table-cell-label'>${label}<span class = 'required'>*</span></div>"+
        "      			<div class='input-group '>"+
        "      				<input class='form-control table-cell input-sm' id='" + clientListCounter + "_requestExpiryTime' data-toggle='tooltip' data-placement='bottom' title='${tooltip }' value='${client.requestExpiryTime}' />"+
		"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
        "        		</div>"+
        "     		</div>"+
        "		</div>"+
        "</c:if>"+
		"<c:if test="${serviceType eq 'RADIUS_COLLECTION_SERVICE'}">"+
        "		<div class='col-md-6 no-padding'>"+
        "			<spring:message code='gtpprime.collection.service.client.config.tab.request.retry' var='label'></spring:message>"+
        "			<spring:message code='gtpprime.collection.service.client.config.tab.request.retry.tooltip' var='tooltip'></spring:message>"+
        "  			<div class='form-group'>"+
        "    			<div class='table-cell-label'>${label}<span class = 'required'>*</span></div>"+
        "      			<div class='input-group '>"+
        "      				<input class='form-control table-cell input-sm' id='" + clientListCounter + "_requestRetry' data-toggle='tooltip' data-placement='bottom' title='${tooltip }' value='${client.requestRetry}' />"+
		"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
        "        		</div>"+
        "     		</div>"+
        "		</div>"+
        "</c:if>"+
		"<c:if test="${serviceType eq 'GTPPRIME_COLLECTION_SERVICE'}">"+
        "		<div class='col-md-6 no-padding'>"+
        "			<spring:message code='gtpprime.collection.service.client.config.tab.request.buffer.count' var='label'></spring:message>"+
        "			<spring:message code='gtpprime.collection.service.client.config.tab.request.buffer.count.tooltip' var='tooltip'></spring:message>"+
        "  			<div class='form-group'>"+
        "    			<div class='table-cell-label'>${label}<span class = 'required'>*</span></div>"+
        "      			<div class='input-group '>"+
        "      				<input class='form-control table-cell input-sm' id='" + clientListCounter + "_requestBufferCount' data-toggle='tooltip' data-placement='bottom' title='${tooltip }' value='${client.requestBufferCount}' />"+
		"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
        "        		</div>"+
        "     		</div>"+
        "		</div>"+
        "</c:if>"+
        "<c:if test="${serviceType eq 'GTPPRIME_COLLECTION_SERVICE'}">"+
        "		<div class='col-md-6 no-padding'>"+
        "			<spring:message code='gtpprime.collection.service.client.config.tab.redirection.ip' var='label'></spring:message>"+
        "			<spring:message code='gtpprime.collection.service.client.config.tab.redirection.ip.tooltip' var='tooltip'></spring:message>"+
        "  			<div class='form-group'>"+
        "    			<div class='table-cell-label'>${label}</div>"+
        "      			<div class='input-group '>"+
        "      				<input class='form-control table-cell input-sm' id='" + clientListCounter + "_redirectionIp' data-toggle='tooltip' data-placement='bottom' title='${tooltip }' value='${client.redirectionIp}' />"+
		"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
        "        		</div>"+
        "     		</div>"+
        "		</div>"+
        "</c:if>"+
        "<c:if test="${serviceType eq 'RADIUS_COLLECTION_SERVICE'}">"+
        "		<div class='col-md-6 no-padding'>"+
        "			<spring:message code='netflow.collection.service.client.config.tab.secretkey.client' var='label'></spring:message>"+
        "			<spring:message code='netflow.collection.service.client.config.tab.secretkey.client' var='tooltip'></spring:message>"+
        "  			<div class='form-group'>"+
        "    			<div class='table-cell-label'>${label}<span class = 'required'>*</span></div>"+
        "      			<div class='input-group '>"+
        "      				<input class='form-control table-cell input-sm' id='" + clientListCounter + "_sharedSecretKey' data-toggle='tooltip' data-placement='bottom' title='${tooltip }' value='${client.sharedSecretKey}' />"+
		"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
        "        		</div>"+
        "     		</div>"+
        "		</div>"+
        "</c:if>"+
        "		<div class='col-md-12 no-padding'>"+
        "			<spring:message code='ftp.driver.mgmt.pathlist.max.count.limit' var='tooltip'></spring:message>"+
        "  			<div class='form-group'>"+
        "				<sec:authorize access='hasAuthority(\'CREATE_COLLECTION_CLIENT\')'>"+
        "      			<div id='" + clientListCounter + "_buttons-div' class='input-group '>"+
        "      				<button type='button' class='btn btn-grey btn-xs ' id='" + clientListCounter + "_savebtn' onclick=addClient(\'"+clientListCounter+"\');><spring:message code='btn.label.save'></spring:message></button>&nbsp;" +
        "					<button type='button' class='btn btn-grey btn-xs ' id='" + clientListCounter + "_updatebtn' style='display:none;' onclick=updateClient(\'"+clientListCounter+"\');><spring:message code='btn.label.update'></spring:message></button>&nbsp;" +
        "					<button type='button' class='btn btn-grey btn-xs ' id='" + clientListCounter + "_resetbtn' tabindex='15' onclick=resetClientDetailDiv(\'"+clientListCounter+"\');><spring:message code='btn.label.reset'></spring:message></button>&nbsp;" +
        "       		</div>"+
        "				</sec:authorize>"+
        "				<div class='pull-right' style='font-size:10px;'><i class='fa fa-square' style='font-size: 9px'></i>&nbsp;&nbsp;<spring:message code='restart.operation.require.message' ></spring:message> </div>"+
        "					<div id='" + clientListCounter + "_progress-bar-div' class='input-group' style='display: none;'> "+
        "						<label>Processing Request &nbsp;&nbsp; </label> "+
        "							<img src='img/processing-bar.gif'>"+
		"					</div> "+
        "   		</div>" +
        "		</div>" +
     	"	</div>"+
		"</div>"+
		"</form>";
		$('#clientList').prepend(block);
		
		$('#'+ clientListCounter + '_appendFileSequenceInFileName').val('${client.appendFileSequenceInFileName}');
		$('#'+ clientListCounter + '_appendFilePaddingInFileName').val('${client.appendFilePaddingInFileName}');
		$('#'+ clientListCounter + '_inputCompressed').val('${client.inputCompressed}');
		$('#'+ clientListCounter + '_jsonValidate').val('${client.jsonValidate}');
		$('#'+ clientListCounter + '_contentType').val('${client.contentType}');
		$('#'+ clientListCounter + '_status').val('${client.status}');
		$('#'+ clientListCounter + '_snmpAlertEnable').val('${client.snmpAlertEnable}');
		$('#'+ clientListCounter + '_status option[value="DELETED"]').remove();
		$('#'+ clientListCounter + '_snmpAlertEnable option[value="DELETED"]').remove();
		$('#' + clientListCounter + '_alertInterval').val('${client.alertInterval}');
		$('#' + clientListCounter + '_enableSecurity').val('${client.enableSecurity}');
		$('#' + clientListCounter + '_enableProxy').val('${client.enableProxy}');
		$('#' + clientListCounter + '_registerObserver').val('${client.registerObserver}');
		$('#' + clientListCounter + '_securityType').val('${client.securityType}');
		$('#' + clientListCounter + '_proxySchema').val('${client.proxySchema}');
		$('#' + clientListCounter + '_messageType').val('${client.messageType}');
		$('#' + clientListCounter + '_requestType').val('${client.requestType}');
		$('#'+ clientListCounter + '_observerTimeoutDiv').hide();
		$('#'+ clientListCounter + '_observerTimeout').val('-1');
		$('#'+ clientListCounter + '_requestTimeout').val('-1');
		$('#'+ clientListCounter + '_requestRetryCount').val('-1');
		$('#'+ clientListCounter + '_reqExecutionInterval').val('-1');
		$('#'+ clientListCounter + '_reqExecutionFreq').val('-1');
		$('#'+ clientListCounter + '_exchangeLifeTime').val('247000');
		$('#' + clientListCounter + '_enableKafka').val('${client.enableKafka}');
		$('#' + clientListCounter + '_kafkaDataSourceConfig').val('${client.kafkaDataSourceConfig.id}');
		changeSNMPAlterStatus(clientListCounter);
		changeSequenceRangeStatus(clientListCounter);
		changeObserverStatus(clientListCounter);
		changeSecurityParameters(clientListCounter);
		changeProxyParameters(clientListCounter);
		changeKafkaParameters(clientListCounter);
	}
</script>


<div class="tab-pane" id="netflow-client-config">
	<div class="tab-content no-padding">
		<div class="fullwidth mtop10">
			<div class="title2">
				<spring:message code="netflow.collectionService.client.list.caption" ></spring:message>
				<span class="title2rightfield"> <sec:authorize
						access="hasAuthority('CREATE_COLLECTION_CLIENT')">
						<span class="title2rightfield-icon1-text"> <a href="#"
							onclick="addNewClientDetail();"><i class="fa fa-plus-circle"></i></a>
							<a href="#" id="addClient" onclick="addNewClientDetail();"> <spring:message
									code="btn.label.add" ></spring:message></a>
						</span>
					</sec:authorize>
				</span>
			</div>
			<div class="clearfix"></div>
		</div>

		<!-- Morris chart - Sales -->

		<div class="fullwidth">
			<div id="clientList"></div>

			<c:if test="${clientList != null && fn:length(clientList) gt 0}">
				<c:forEach var="client" items="${clientList}">	
					<script>
					addClientDetail('${client.id}',
										'${client.topicName}',
										'${client.name}',
										'${client.clientIpAddress}',
										'${client.clientPort}',
										'${client.resourcesName}',
										'${client.registerObserver}',
										'${client.observerTimeout}',
										'${client.requestType}',
										'${client.messageType}',
										'${client.requestTimeout}',
										'${client.requestRetryCount}',
										'${client.reqExecutionInterval}',
										'${client.reqExecutionFreq}',
										'${client.exchangeLifeTime}',
										'${client.contentFormat}',
										'${client.payload}',
										'${client.enableSecurity}',
										'${client.securityType}',
										'${client.securityIdentity}',
										'${client.securityKey}',
										'${client.secCerLocation}',
										'${client.secCerPasswd}',
										'${client.enableProxy}',
										'${client.proxyServerIp}',
										'${client.proxyServerPort}',
										'${client.proxySchema}',
										'${client.proxyResources}',
										'${client.outFileLocation}',
										'${client.bkpBinaryfileLocation}',
										'${client.fileNameFormat}',
										'${client.appendFileSequenceInFileName}',
										'${client.appendFilePaddingInFileName}',
										'${client.minFileSeqValue}',
										'${client.maxFileSeqValue}',
										'${client.timeLogRollingUnit}',
										'${client.volLogRollingUnit}',
										'${client.inputCompressed}',
										'${client.status}',
										'${client.snmpAlertEnable}',
										'${client.alertInterval}',
										'${client.rollingType}',
										'${client.nodeAliveRequest}',
										'${client.echoRequest}',
										'${client.requestExpiryTime}',
										'${client.requestRetry}',
										'${client.requestBufferCount}',
										'${client.redirectionIp}',
										'${client.sharedSecretKey}',
										'${client.jsonValidate}',
										'${client.contentType}',
										'${client.uri}',
										'${client.enableKafka}',
										'${client.kafkaDataSourceConfig.id}'
								);
					
					var client = {
								"id"				:'${client.id}',
								"topicName"			:'${client.topicName}',
								"name"				:'${client.name}',
								"ipAddress" 		:'${client.clientIpAddress}',
								"port"				:'${client.clientPort}',
								"resourcesName"			:'${client.resourcesName}',
								"registerObserver"		:'${client.registerObserver}',
								"observerTimeout"		:'${client.observerTimeout}',
								"requestType"			:'${client.requestType}',
								"messageType"			:'${client.messageType}',
								"requestTimeout"		:'${client.requestTimeout}',
								"requestRetryCount"		:'${client.requestRetryCount}',
								"reqExecutionInterval"	:'${client.reqExecutionInterval}',
								"reqExecutionFreq"		:'${client.reqExecutionFreq}',
								"exchangeLifeTime"		:'${client.exchangeLifeTime}',
								"contentFormat"			:'${client.contentFormat}',
								"payload"				:'${client.payload}',
								"enableSecurity"		:'${client.enableSecurity}',
								"securityType"			:'${client.securityType}',
								"securityIdentity"		:'${client.securityIdentity}',
								"securityKey"			:'${client.securityKey}',
								"secCerLocation"		:'${client.secCerLocation}',
								"secCerPasswd"			:'${client.secCerPasswd}',
								"enableProxy"			:'${client.enableProxy}',
								"proxyServerIp"			:'${client.proxyServerIp}',
								"proxyServerPort"		:'${client.proxyServerPort}',
								"proxySchema"			:'${client.proxySchema}',
								"proxyResources"		:'${client.proxyResources}',
								"outfileLoc"			:'${client.outFileLocation}',
								"bkpFileLoc"			:'${client.bkpBinaryfileLocation}',
								"fileNameFormat"		:'${client.fileNameFormat}',
								"fileSeqEnable" 		:'${client.appendFileSequenceInFileName}',
								"filePaddingEnable" 	:'${client.appendFilePaddingInFileName}',
								"minFileSeq"			:'${client.minFileSeqValue}',
								"maxFileSeq"			:'${client.maxFileSeqValue}',
								"timeRollUnit"			:'${client.timeLogRollingUnit}',
								"volRollUnit"			:'${client.volLogRollingUnit}',
								"inputCompress"			:'${client.inputCompressed}',
								"status"				:'${client.status}',
								"snmpAlertEnable"		:'${client.snmpAlertEnable}',
								"alertInterval"			:'${client.alertInterval}',
								"rollingType"			:'${client.rollingType}',
								"nodeAliveRequest"		:'${client.nodeAliveRequest}',
								"echoRequest"			:'${client.echoRequest}',
								"requestExpiryTime" 	:'${client.requestExpiryTime}',
								"requestRetry"      	:'${client.requestRetry}',
								"requestBufferCount"	:'${client.requestBufferCount}',
								"redirectionIp"			:'${client.redirectionIp}',
								"sharedSecretKey"		:'${client.sharedSecretKey}',
								"jsonValidate"			:'${client.jsonValidate}',
								"contentType"			:'${client.contentType}',
								"uri"					:'${client.uri}',
								"enableKafka"			:'${client.enableKafka}',
								"kafkaDataSourceId" :'${client.kafkaDataSourceConfig.id}'
								}
					if(client.clientPort == -1){
						client.port = '';
					}
					
					clientList[clientListCounter] = client;
					
		 			</script>
				</c:forEach>
			 </c:if> 
		</div>
	</div>
</div>


<!-- Client Delete popup code start here -->
<div id="divMessage" style="width: 100%; display: none;">
	<div class="modal-content">
		<div class="modal-header padding10">
			<h4 class="modal-title" id="warning-title">
				<spring:message code="collection.client.delete.header.label" ></spring:message>
			</h4>
			<h4 class="modal-title" id="status-title" style="display: none;">
				<spring:message code="serverManagement.warn.popup.header" ></spring:message>
			</h4>
		</div>

		<div class="modal-body padding10 inline-form">
			<span id="deleteClientResponseMsg" style="display: none;"> <jsp:include
					page="../../common/responseMsgPopUp.jsp" ></jsp:include>
			</span>
			<p id="warningMessage">
				<spring:message code="client.enable.delete.warning.message" ></spring:message>
			</p>
			<p id="deleteWarningMessage">
				<spring:message code="client.delete.warning.message" ></spring:message>
			</p>
		</div>

		<div class="modal-footer padding10" id="active-client-div">
			<button type="button" class="btn btn-grey btn-xs "
				data-dismiss="modal" onclick="closeFancyBox();">
				<spring:message code="btn.label.close" ></spring:message>
			</button>
		</div>

		<sec:authorize access="hasAuthority('DELETE_COLLECTION_CLIENT')">
			<div id="inactive-client-div" class="modal-footer padding10">
				<button type="button" class="btn btn-grey btn-xs " id="deleteClient"
					onclick="deleteClient();">
					<spring:message code="btn.label.yes" ></spring:message>
				</button>
				<button type="button" class="btn btn-grey btn-xs "
					data-dismiss="modal" onclick="closeFancyBox();">
					<spring:message code="btn.label.no" ></spring:message>
				</button>
			</div>
		</sec:authorize>
	</div>
	<!-- /.modal-content -->
</div>
<a href="#divMessage" class="fancybox" style="display: none;"
	id="clientMessage">#</a>
<!-- Driver Delete popup code end here -->

<script>

function resetClientDetailDiv(divCounter){
	$('#'+divCounter + '_block input').val('');
}

function redirectToDriverConfig(clientListCounter){
	
	$('#driverTypeAlias').val($('#' + clientListCounter + '_driverAlias').val());
	$('#driverId').val($('#' + clientListCounter + '_driverId').val());
	$('#collection-driver-config-form').submit();
}

function showProgressBar(clientListCounter){
	$('#' + clientListCounter + '_buttons-div').hide();
	$('#' + clientListCounter + '_progress-bar-div').show();
}

function hideProgressBar(clientListCounter){
	$('#' + clientListCounter + '_buttons-div').show();
	$('#' + clientListCounter + '_progress-bar-div').hide();
}

function addClient(counter){

	resetWarningDisplay();
	clearAllMessages();
	showProgressBar(counter);
	var clientPort = $('#' + clientListCounter + '_clientPort').val();
	if(clientPort == ''){
		clientPort = '-1';
	}
	var minFileRange=$('#' + counter + '_minFileSeqValue').val();
	var maxFileRange=$('#' + counter + '_maxFileSeqValue').val();
	if(minFileRange==''){
		minFileRange='-1';
	}
	if(maxFileRange==''){
		maxFileRange='-1';
	}
	var proxyServerPort = $('#' + counter + '_proxyServerPort').val();
	if(proxyServerPort == ''){
		proxyServerPort = '-1';
	}
	var observerTimeout = $('#' + counter + '_observerTimeout').val();
	if(observerTimeout==''){
		observerTimeout='-1';
	}
	var requestTimeout = $('#' + counter + '_requestTimeout').val();
	if(requestTimeout==''){
		requestTimeout='-1';
	}
	var requestRetryCount = $('#' + counter + '_requestRetryCount').val();
	if(requestRetryCount==''){
		requestRetryCount='-1';
	}
	var reqExecutionInterval = $('#' + counter + '_reqExecutionInterval').val();
	if(reqExecutionInterval==''){
		reqExecutionInterval='-1';
	}
	var reqExecutionFreq = $('#' + counter + '_reqExecutionFreq').val();
	if(reqExecutionFreq==''){
		reqExecutionFreq='-1';
	}
	var exchangeLifeTime = $('#' + counter + '_exchangeLifeTime').val();
	if(exchangeLifeTime==''){
		exchangeLifeTime='247000';
	}
	
	var kafkaDataSourceId='-1';
	if('COAP_COLLECTION_SERVICE'==serviceType) {
		kafkaDataSourceId = $('#' + counter + '_kafkaDataSourceConfig').val();
	}
	
	$.ajax({
		url: '<%=ControllerConstants.CREATE_NETFLOW_COLLECTION_CLIENT%>',
		cache: false,
		async: true,
		dataType: 'json',
		type: "POST",
		data:
		 {
			"id"							:	$('#' + counter + '_id').val(),
			"name" 							:	$('#' + counter + '_name').val(),
			"clientIpAddress"          		:	$('#' + counter + '_clientIpAddress').val(),
			"clientPort" 					:	clientPort,
			"resourcesName"					:	$('#' + counter + '_resourcesName').val(),
			"registerObserver"				:	$('#' + counter + '_registerObserver').val(),
			"observerTimeout"				:	observerTimeout,
			"requestType"					:	$('#' + counter + '_requestType').val(),
			"messageType"					:	$('#' + counter + '_messageType').val(),
			"requestTimeout"				:	requestTimeout,
			"requestRetryCount"				: 	requestRetryCount,
			"reqExecutionInterval"			:	reqExecutionInterval,
			"reqExecutionFreq"				:	reqExecutionFreq,
			"exchangeLifeTime"				:	exchangeLifeTime,
			"contentFormat"					:	$('#' + counter + '_contentFormat').val(),
			"payload"						:	$('#' + counter + '_payload').val(),
			"enableSecurity"				:	$('#' + counter + '_enableSecurity').val(),
			"securityType"					:	$('#' + counter + '_securityType').val(),
			"securityIdentity"				:	$('#' + counter + '_securityIdentity').val(),
			"securityKey"					:	$('#' + counter + '_securityKey').val(),
			"secCerLocation"				:	$('#' + counter + '_secCerLocation').val(),
			"secCerPasswd"					:	$('#' + counter + '_secCerPasswd').val(),
			"enableProxy"					:	$('#' + counter + '_enableProxy').val(),
			"proxyServerIp"					:	$('#' + counter + '_proxyServerIp').val(),
			"proxyServerPort"				:	proxyServerPort,
			"proxySchema"					:	$('#' + counter + '_proxySchema').val(),
			"proxyResources"				:	$('#' + counter + '_proxyResources').val(),
			"fileNameFormat" 				:	$('#' + counter + '_fileNameFormat').val(),
			"appendFileSequenceInFileName" 	:	$('#' + counter + '_appendFileSequenceInFileName').val(),
			"appendFilePaddingInFileName" 	:	$('#' + counter + '_appendFilePaddingInFileName').val(),
			"minFileSeqValue" 				:   minFileRange,
			"maxFileSeqValue"				:	maxFileRange,
			"outFileLocation"				:   $('#' + counter + '_outFileLocation').val(),
			"volLogRollingUnit"				:   $('#' + counter + '_volLogRollingUnit').val(),
			"timeLogRollingUnit"			:   $('#' + counter + '_timeLogRollingUnit').val(),
			"inputCompressed"				:   $('#' + counter + '_inputCompressed').val(),
			"jsonValidate"					:   $('#' + counter + '_jsonValidate').val(),
			"contentType"					:   $('#' + counter + '_contentType').val(),
			"uri"							:   $('#' + counter + '_uri').val(),
			"bkpBinaryfileLocation"			:   $('#' + counter + '_bkpBinaryfileLocation').val(),
			"service.id"					:   $('#' + counter + '_serviceId').val(),
			"clientCount"					: 	counter,
			"snmpAlertEnable"				:   $('#' + counter + '_snmpAlertEnable').val(),
			"alertInterval"					:   $('#' + counter + '_alertInterval').val(),
			"rollingType"					:   $('#' + counter + '_rollingType').val(),
			"status"						:   $('#' + counter + '_status').val(),
			"nodeAliveRequest"				:   $('#' + counter + '_nodeAliveRequest').val(),
			"echoRequest"					:   $('#' + counter + '_echoRequest').val(),
			"requestExpiryTime"				:   $('#' + counter + '_requestExpiryTime').val(),
			"requestRetry"					:   $('#' + counter + '_requestRetry').val(),
			"requestBufferCount"			:   $('#' + counter + '_requestBufferCount').val(),
			"redirectionIp"					:   $('#' + counter + '_redirectionIp').val(),
			"sharedSecretKey"				:   $('#' + counter + '_sharedSecretKey').val(),
			"topicName"						:   $('#' + counter + '_topicName').val(),
			"serviceType"					:	'${serviceType}',
			"enableKafka"					:	$('#' + counter + '_enableKafka').val(),
			"kafkaDataSourceId"				:   kafkaDataSourceId
			
		}, 
		
		success: function(data){
			hideProgressBar(counter);

			var response = eval(data);
			
			var responseCode = response.code; 
			var responseMsg = response.msg; 
			var responseObject = response.object;
			
			if(responseCode == "200"){
				
				
				resetWarningDisplay();
				clearAllMessages();
				showSuccessMsg(responseMsg);

				<c:if test="${serviceType eq 'MQTT_COLLECTION_SERVICE'}">
					$("#link_"+counter).text(responseObject["name"]+"-"+responseObject["topicName"]);
				</c:if>
				<c:if test="${serviceType ne 'MQTT_COLLECTION_SERVICE'}">
					$("#link_"+counter).text(responseObject["name"]);
				</c:if>
				
				
				
				$("#"+counter+"_updatebtn").show();
				$("#"+counter+"_savebtn").hide();
				$('#'+counter+'_id').val(responseObject["id"]);
				$("#"+counter+"_block").collapse("toggle");
				
				var client = {
						"id"							:	$('#' + counter + '_id').val(),
						"name" 							:	$('#' + counter + '_name').val(),
						"clientIpAddress"          		:	$('#' + counter + '_clientIpAddress').val(),
						"clientPort" 					:	$('#' + counter + '_clientPort').val(),
						"resourcesName"					:	$('#' + counter + '_resourcesName').val(),
						"registerObserver"				:	$('#' + counter + '_registerObserver').val(),
						"observerTimeout"				:	$('#' + counter + '_observerTimeout').val(),
						"requestType"					:	$('#' + counter + '_requestType').val(),
						"messageType"					:	$('#' + counter + '_messageType').val(),
						"requestTimeout"				:	$('#' + counter + '_requestTimeout').val(),
						"requestRetryCount"				:	$('#' + counter + '_requestRetryCount').val(),
						"reqExecutionInterval"			:	$('#' + counter + '_reqExecutionInterval').val(),
						"reqExecutionFreq"				:	$('#' + counter + '_reqExecutionFreq').val(),
						"exchangeLifeTime"				:	$('#' + counter + '_exchangeLifeTime').val(),
						"contentFormat"					:	$('#' + counter + '_contentFormat').val(),
						"payload"						:	$('#' + counter + '_payload').val(),
						"enableSecurity"				:	$('#' + counter + '_enableSecurity').val(),
						"securityType"					:	$('#' + counter + '_securityType').val(),
						"securityIdentity"				:	$('#' + counter + '_securityIdentity').val(),
						"securityKey"					:	$('#' + counter + '_securityKey').val(),
						"secCerLocation"				:	$('#' + counter + '_secCerLocation').val(),
						"secCerPasswd"					:	$('#' + counter + '_secCerPasswd').val(),
						"enableProxy"					:	$('#' + counter + '_enableProxy').val(),
						"proxyServerIp"					:	$('#' + counter + '_proxyServerIp').val(),
						"proxyServerPort"				:	$('#' + counter + '_proxyServerPort').val(),
						"proxySchema"					:	$('#' + counter + '_proxySchema').val(),
						"proxyResources"				:	$('#' + counter + '_proxyResources').val(),
						"fileNameFormat" 				:	$('#' + counter + '_fileNameFormat').val(),
						"appendFileSequenceInFileName" 	:	$('#' + counter + '_appendFileSequenceInFileName').val(),
						"appendFilePaddingInFileName" 	:	$('#' + counter + '_appendFilePaddingInFileName').val(),
						"minFileSeqValue" 				:   maxFileRange,
						"maxFileSeqValue"				:	maxFileRange,
						"outFileLocation"				:   $('#' + counter + '_outFileLocation').val(),
						"volLogRollingUnit"				:   $('#' + counter + '_volLogRollingUnit').val(),
						"timeLogRollingUnit"			:   $('#' + counter + '_timeLogRollingUnit').val(),
						"inputCompressed"				:   $('#' + counter + '_inputCompressed').val(),
						"jsonValidate"					:   $('#' + counter + '_jsonValidate').val(),
						"contentType"					:   $('#' + counter + '_contentType').val(),
						"uri"							:   $('#' + counter + '_uri').val(),
						"bkpBinaryfileLocation"			:   $('#' + counter + '_bkpBinaryfileLocation').val(),
						"service.id"					:   $('#' + counter + '_serviceId').val(),
						"clientCount"					: 	counter,
						"snmpAlertEnable"				:   $('#' + counter + '_snmpAlertEnable').val(),
						"alertInterval"					:   $('#' + counter + '_alertInterval').val(),
						"rollingType"					:   $('#' + counter + '_rollingType').val(),
						"nodeAliveRequest"				:   $('#' + counter + '_nodeAliveRequest').val(),
						"echoRequest"					:   $('#' + counter + '_echoRequest').val(),
						"requestExpiryTime"				:   $('#' + counter + '_requestExpiryTime').val(),
						"requestRetry"					:   $('#' + counter + '_requestRetry').val(),
						"requestBufferCount"			:   $('#' + counter + '_requestBufferCount').val(),
						"redirectionIp"					:   $('#' + counter + '_redirectionIp').val(),
						"sharedSecretKey"				:   $('#' + counter + '_sharedSecretKey').val(),
						"status"						:   $('#' + counter + '_status').val(),
						"topicName"						:   $('#' + counter + '_topicName').val(),
						"enableKafka"					:	$('#' + counter + '_enableKafka').val(),
						"kafkaDataSourceId"				:   kafkaDataSourceId
						}
			if(client.clientPort == '-1'){
				client.clientPort == '';
			}
			clientList[counter] = client;
				
			}else if(responseObject != undefined && responseObject != 'undefined' && responseCode == "400"){
				
				hideProgressBar(counter);
				addErrorIconAndMsgForAjax(responseObject); 
					
			}else{
				hideProgressBar(counter);
				resetWarningDisplay();
				clearAllMessages();
				showErrorMsg(responseMsg);
			}
		},
	    error: function (xhr,st,err){
	    	hideProgressBar(counter);
			handleGenericError(xhr,st,err);
		}
	});
	
}


function updateClient(clientListCounter){
	resetWarningDisplay();
	clearAllMessages();
	showProgressBar(clientListCounter);
	parent.resizeFancyBox();
	var minFileRange=$('#' + clientListCounter + '_minFileSeqValue').val();
	var maxFileRange=$('#' + clientListCounter + '_maxFileSeqValue').val();
	if(minFileRange==''){
		minFileRange='-1';
	}
	if(maxFileRange==''){
		maxFileRange='-1';
	}
	var clientPort = $('#' + clientListCounter + '_clientPort').val();
	if(clientPort == ''){
		clientPort = '-1';
	}
	var proxyServerPort = $('#' + clientListCounter + '_proxyServerPort').val();
	if(proxyServerPort == ''){
		proxyServerPort = '-1';
	}
	var observerTimeout = $('#' + clientListCounter + '_observerTimeout').val();
	if(observerTimeout==''){
		observerTimeout='-1';
	}
	var requestTimeout = $('#' + clientListCounter + '_requestTimeout').val();
	if(requestTimeout==''){
		requestTimeout='-1';
	}
	var requestRetryCount = $('#' + clientListCounter + '_requestRetryCount').val();
	if(requestRetryCount==''){
		requestRetryCount='-1';
	}
	var reqExecutionInterval = $('#' + clientListCounter + '_reqExecutionInterval').val();
	if(reqExecutionInterval==''){
		reqExecutionInterval='-1';
	}
	var reqExecutionFreq = $('#' + clientListCounter + '_reqExecutionFreq').val();
	if(reqExecutionFreq==''){
		reqExecutionFreq='-1';
	}
	var exchangeLifeTime = $('#' + clientListCounter + '_exchangeLifeTime').val();
	if(exchangeLifeTime==''){
		exchangeLifeTime='247000';
	}
	var kafkaDataSourceId='-1';
	if('COAP_COLLECTION_SERVICE'==serviceType) {
		kafkaDataSourceId = $('#' + clientListCounter + '_kafkaDataSourceConfig').val();
	}
	
	$.ajax({
		url: '<%=ControllerConstants.UPDATE_NETFLOW_COLLECTION_CLIENT%>',
		cache: false,
		async: true,
		dataType: 'json',
		type: "POST",
		data:
		 {
			"id"							:	$('#' +clientListCounter+'_id').val(),
			"name" 							:	$('#' + clientListCounter + '_name').val(),
			"clientIpAddress"          		:	$('#' + clientListCounter + '_clientIpAddress').val(),
			"clientPort" 					:	clientPort,
			"resourcesName"					:	$('#' + clientListCounter + '_resourcesName').val(),
			"registerObserver"				:	$('#' + clientListCounter + '_registerObserver').val(),
			"observerTimeout"				:	observerTimeout,
			"requestType"					:	$('#' + clientListCounter + '_requestType').val(),
			"messageType"					:	$('#' + clientListCounter + '_messageType').val(),
			"requestTimeout"				:	requestTimeout,
			"requestRetryCount"				: 	requestRetryCount,
			"reqExecutionInterval"			:	reqExecutionInterval,
			"reqExecutionFreq"				:	reqExecutionFreq,
			"exchangeLifeTime"				:	exchangeLifeTime,
			"contentFormat"					:	$('#' + clientListCounter + '_contentFormat').val(),
			"payload"						:	$('#' + clientListCounter + '_payload').val(),
			"enableSecurity"				:	$('#' + clientListCounter + '_enableSecurity').val(),
			"securityType"					:	$('#' + clientListCounter + '_securityType').val(),
			"securityIdentity"				:	$('#' + clientListCounter + '_securityIdentity').val(),
			"securityKey"					:	$('#' + clientListCounter + '_securityKey').val(),
			"secCerLocation"				:	$('#' + clientListCounter + '_secCerLocation').val(),
			"secCerPasswd"					:	$('#' + clientListCounter + '_secCerPasswd').val(),
			"enableProxy"					:	$('#' + clientListCounter + '_enableProxy').val(),
			"proxyServerIp"					:	$('#' + clientListCounter + '_proxyServerIp').val(),
			"proxyServerPort"				:	proxyServerPort,
			"proxySchema"					:	$('#' + clientListCounter + '_proxySchema').val(),
			"proxyResources"				:	$('#' + clientListCounter + '_proxyResources').val(),
			"fileNameFormat" 				:	$('#' + clientListCounter + '_fileNameFormat').val(),
			"appendFileSequenceInFileName" 	:	$('#' + clientListCounter + '_appendFileSequenceInFileName').val(),
			"appendFilePaddingInFileName" 	:	$('#' + clientListCounter + '_appendFilePaddingInFileName').val(),
			"minFileSeqValue" 				:   minFileRange,
			"maxFileSeqValue"				:	maxFileRange,
			"outFileLocation"				:   $('#' + clientListCounter + '_outFileLocation').val(),
			"volLogRollingUnit"				:   $('#' + clientListCounter + '_volLogRollingUnit').val(),
			"timeLogRollingUnit"			:   $('#' + clientListCounter + '_timeLogRollingUnit').val(),
			"inputCompressed"				:   $('#' + clientListCounter + '_inputCompressed').val(),
			"jsonValidate"					:   $('#' + clientListCounter + '_jsonValidate').val(),
			"contentType"					:   $('#' + clientListCounter + '_contentType').val(),
			"uri"							:   $('#' + clientListCounter + '_uri').val(),
			"bkpBinaryfileLocation"			:   $('#' + clientListCounter + '_bkpBinaryfileLocation').val(),
			"clientCount"					: 	clientListCounter,
			"snmpAlertEnable"				:   $('#' + clientListCounter + '_snmpAlertEnable').val(),
			"alertInterval"					:   $('#' + clientListCounter + '_alertInterval').val(),
			"rollingType"					:   $('#' + clientListCounter + '_rollingType').val(),
			"status"						:   $('#' + clientListCounter + '_status').val(),
			"serviceType"					:	'${serviceType}',
			"nodeAliveRequest"				:   $('#' + clientListCounter + '_nodeAliveRequest').val(),
			"echoRequest"					:   $('#' + clientListCounter + '_echoRequest').val(),
			"requestExpiryTime"				:   $('#' + clientListCounter + '_requestExpiryTime').val(),
			"requestRetry"					:   $('#' + clientListCounter + '_requestRetry').val(),
			"requestBufferCount"			:   $('#' + clientListCounter + '_requestBufferCount').val(),
			"redirectionIp"					:   $('#' + clientListCounter + '_redirectionIp').val(),
			"sharedSecretKey"				:   $('#' + clientListCounter + '_sharedSecretKey').val(),
			"service.id"                    :   $('#serviceId').val(),
			"topicName"						:   $('#' + clientListCounter + '_topicName').val(),
			"enableKafka"					:	$('#' + clientListCounter + '_enableKafka').val(),
			"kafkaDataSourceId"				:   kafkaDataSourceId
			
		}, 
		
		success: function(data){
			 hideProgressBar(clientListCounter);

			var response = eval(data);
			
			var responseCode = response.code; 
			var responseMsg = response.msg; 
			var responseObject = response.object;
			
			if(responseCode == "200"){
				resetWarningDisplay();
				clearAllMessages();
				showSuccessMsg(responseMsg);
				
				<c:if test="${serviceType eq 'MQTT_COLLECTION_SERVICE'}">
					$("#link_"+clientListCounter).text(responseObject["name"]+"-"+responseObject["topicName"]);
				</c:if>
				<c:if test="${serviceType ne 'MQTT_COLLECTION_SERVICE'}">
					$("#link_"+clientListCounter).text(responseObject["name"]);
				</c:if>
				$("#"+clientListCounter+"_block").collapse("toggle");
				
				var client = {
						"id"							:	$('#' + clientListCounter + '_id').val(),
						"name" 							:	$('#' + clientListCounter + '_name').val(),
						"clientIpAddress"          		:	$('#' + clientListCounter + '_clientIpAddress').val(),
						"clientPort" 					:	$('#' + clientListCounter + '_clientPort').val(),
						"resourcesName"					:	$('#' + clientListCounter + '_resourcesName').val(),
						"registerObserver"				:	$('#' + clientListCounter + '_registerObserver').val(),
						"observerTimeout"				:	observerTimeout,
						"requestType"					:	$('#' + clientListCounter + '_requestType').val(),
						"messageType"					:	$('#' + clientListCounter + '_messageType').val(),
						"requestTimeout"				:	requestTimeout,
						"requestRetryCount"				:	requestRetryCount,
						"reqExecutionInterval"			:	reqExecutionInterval,
						"reqExecutionFreq"				:	reqExecutionFreq,
						"exchangeLifeTime"				:	exchangeLifeTime,
						"contentFormat"					:	$('#' + clientListCounter + '_contentFormat').val(),
						"payload"						:	$('#' + clientListCounter + '_payload').val(),
						"enableSecurity"				:	$('#' + clientListCounter + '_enableSecurity').val(),
						"securityType"					:	$('#' + clientListCounter + '_securityType').val(),
						"securityIdentity"				:	$('#' + clientListCounter + '_securityIdentity').val(),
						"securityKey"					:	$('#' + clientListCounter + '_securityKey').val(),
						"secCerLocation"				:	$('#' + clientListCounter + '_secCerLocation').val(),
						"secCerPasswd"					:	$('#' + clientListCounter + '_secCerPasswd').val(),
						"enableProxy"					:	$('#' + clientListCounter + '_enableProxy').val(),
						"proxyServerIp"					:	$('#' + clientListCounter + '_proxyServerIp').val(),
						"proxyServerPort"				:	proxyServerPort,
						"proxySchema"					:	$('#' + clientListCounter + '_proxySchema').val(),
						"proxyResources"				:	$('#' + clientListCounter + '_proxyResources').val(),
						"fileNameFormat" 				:	$('#' + clientListCounter + '_fileNameFormat').val(),
						"appendFileSequenceInFileName" 	:	$('#' + clientListCounter + '_appendFileSequenceInFileName').val(),
						"appendFilePaddingInFileName" 	:	$('#' + clientListCounter + '_appendFilePaddingInFileName').val(),
						"minFileSeqValue" 				:   minFileRange,
						"maxFileSeqValue"				:	maxFileRange,
						"outFileLocation"				:   $('#' + clientListCounter + '_outFileLocation').val(),
						"volLogRollingUnit"				:   $('#' + clientListCounter + '_volLogRollingUnit').val(),
						"timeLogRollingUnit"			:   $('#' + clientListCounter + '_timeLogRollingUnit').val(),
						"inputCompressed"				:   $('#' + clientListCounter + '_inputCompressed').val(),
						"jsonValidate"					:   $('#' + clientListCounter + '_jsonValidate').val(),
						"contentType"					:   $('#' + clientListCounter + '_contentType').val(),
						"uri"							:   $('#' + clientListCounter + '_uri').val(),
						"bkpBinaryfileLocation"			:   $('#' + clientListCounter + '_bkpBinaryfileLocation').val(),
						"service.id"					:   $('#' + clientListCounter + '_serviceId').val(),
						"clientCount"					: 	clientListCounter,
						"snmpAlertEnable"				:   $('#' + clientListCounter + '_snmpAlertEnable').val(),
						"alertInterval"					:   $('#' + clientListCounter + '_alertInterval').val(),
						"rollingType"					:   $('#' + clientListCounter + '_rollingType').val(),
						"nodeAliveRequest"				:   $('#' + clientListCounter + '_nodeAliveRequest').val(),
						"echoRequest"					:   $('#' + clientListCounter + '_echoRequest').val(),
						"requestExpiryTime"				:   $('#' + clientListCounter + '_requestExpiryTime').val(),
						"requestRetry"					:   $('#' + clientListCounter + '_requestRetry').val(),
						"requestBufferCount"			:   $('#' + clientListCounter + '_requestBufferCount').val(),
						"redirectionIp"					:   $('#' + clientListCounter + '_redirectionIp').val(),
						"sharedSecretKey"				:   $('#' + clientListCounter + '_sharedSecretKey').val(),
						"status"						:   $('#' + clientListCounter + '_status').val(),
						"topicName"						:   $('#' + clientListCounter + '_topicName').val(),
						"enableKafka"					:	$('#' + clientListCounter + '_enableKafka').val(),
						"kafkaDataSourceId"				:   kafkaDataSourceId
						}
			
			clientList[clientListCounter] = client;
				
			}else if(responseObject != undefined && responseObject != 'undefined' && responseCode == "400"){
				hideProgressBar(clientListCounter);
				showErrorMsg(responseMsg);
				addErrorIconAndMsgForAjax(responseObject); 
				
			}else{
				hideProgressBar(clientListCounter);
				resetWarningDisplay();
				clearAllMessages();
				showErrorMsg(responseMsg);
			}
		},
	    error: function (xhr,st,err){
	    	hideProgressBar(clientListCounter);
			handleGenericError(xhr,st,err);
		}
	});
}
var blockCounter , deleteClientId;
function deleteClientPopup(counter){
	clearAllMessages();
	$("#deleteClientResponseMsg").hide();
	blockCounter = counter;

	deleteClientId= $('#'+counter+'_id').val();
	
	if(deleteClientId == null || deleteClientId == 'null' || deleteClientId == '' || deleteClientId == '0'){
		$("#"+counter+"_form").remove();
	}else{
		var clientStatus = clientList[counter].status;
		if(clientStatus == "INACTIVE" || serviceType == 'RADIUS_COLLECTION_SERVICE'){
			$("#deleteWarningMessage").show();
			$("#inactive-client-div").show();
			$("#warningMessage").hide();
			$("#active-client-div").hide();
			$("#clientMessage").click();
		}else{
			$("#deleteWarningMessage").hide();
			$("#warningMessage").show();
			$("#inactive-client-div").hide();
			$("#active-client-div").show();
			$("#clientMessage").click();	
		}
	}
}

function deleteClient(){
	resetWarningDisplay();
	clearAllMessages();
	 $.ajax({
		url: '<%=ControllerConstants.DELETE_NETFLOW_CLIENT%>',
		cache: false,
		async: true,
		dataType: 'json',
		type: "POST",
		data:
		 {
			"clientId"   : deleteClientId,
		}, 
		
		success: function(data){
			hideProgressBar(clientListCounter);
			var response = eval(data);
			var responseCode = response.code; 
			var responseMsg = response.msg; 
			var responseObject = response.object;
			$("#deleteWarningMessage").hide();
			$("#inactive-client-div").hide();
			$("#warningMessage").hide();
			$("#active-client-div").hide();
		
			$("#warning-title").hide();
			$("#status-title").show();
			
			if(responseCode == "200"){
				resetWarningDisplay();
				clearAllMessages();
				$("#deleteClientResponseMsg").show(); // div handles response message of delete operation
				$('#active-client-div').show(); // To show close button for popup after successful delete
				showSuccessMsg(responseMsg);
				closeFancyBox();
				$('#'+blockCounter+'_form').remove();
				delete clientList[blockCounter];
				
			}else if(responseObject != undefined && responseObject != 'undefined' && responseCode == "400"){
				showErrorMsgPopUp(responseMsg);
				parent.resizeFancyBox();
			}else{
				resetWarningDisplay();
				clearAllMessages();
				showErrorMsgPopUp(responseMsg);
				parent.resizeFancyBox();
			}
		},
	    error: function (xhr,st,err){
			handleGenericError(xhr,st,err);
		}
	}); 
}

$(window).keydown(function(event){
    if(event.keyCode == 13) {
      return false;
    }
  });
</script>




