<%@page import="com.elitecore.sm.util.MapCache"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="/WEB-INF/ShowErrorTag.tld" prefix="elitecoreError"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page import="com.elitecore.sm.common.model.TrueFalseEnum"%>
<%@page import="com.elitecore.sm.common.constants.BaseConstants"%>
<script>
var jsLocaleMsg = {};
jsLocaleMsg.checkfreePort = "<spring:message code="btn.label.checkfreePort" ></spring:message>";
jsLocaleMsg.portMissing = "<spring:message code='service.listener.port.missing'></spring:message>";

function checkPortAvailability(id){
	clearAllMessages();
	clearResponseMsgDiv();
	clearResponseMsg();
	clearErrorMsg();
	resetWarningDisplay();
	var port = $('#service-netFlowPort').val().trim();
	var isProxyPort = false;
	
	if(id == 'btnCheckFreePort'){
		port = $('#service-netFlowPort').val().trim();
	}
		
	
	if(id == 'btnCheckProxyFreePort'){
		port = $('#service-proxyServicePort').val().trim();		
		isProxyPort = true;
	}
	
	if(port != '' && port.length>0){
	
		if(isProxyPort){
			$('#btnCheckProxyFreePort').hide();
			$('#imgLoaderProxy').show();
		}else{
			$('#btnCheckFreePort').hide();
			$('#imgLoader').show();
		}
		
		
		$.ajax({                                                                                                                                                                                                                                                      
			url: '<%= ControllerConstants.CHECK_PORT_AVAILIBILITY %>',
			cache: false,
			async: true,
			dataType: 'json',
			type: "POST",
			data: {
				"port" : port,
				"serverInstanceId": '${instanceId}',
				"isProxyPort":isProxyPort
			},
			success: function(data){
				var response = eval(data);
				
				var responseCode = response.code; 
				var responseMsg = response.msg; 
				var responseObject = data.object;
				
				if(isProxyPort){
					$('#btnCheckProxyFreePort').show();
					$('#imgLoaderProxy').hide();
				}else{
					$('#imgLoader').hide();
					$('#btnCheckFreePort').show();
				}
				
				
				if(responseCode == "200"){
					showSuccessMsg("<spring:message code='onlineServices.checkfreePort.success'></spring:message>")
				}else if(responseCode == "400"){
					addErrorIconAndMsgForAjax(responseObject);
				}
				else{
					addErrorIconAndMsgForAjax(responseObject);
				}

			},
		    error: function (xhr,st,err){
		    	$('#imgLoader').hide();
				$('#btnCheckFreePort').show();
				$('#btnCheckProxyFreePort').show();
				$('#imgLoaderProxy').hide();
				handleGenericError(xhr,st,err);
				
			//	$('#imgLoader').hide();
			
			}
		});
	} else {
		var object = {'service-netFlowPort':jsLocaleMsg.portMissing};
		addErrorIconAndMsgForAjax(object);
		$('#imgLoader').hide();
		$('#btnCheckFreePort').show();
	}
}
</script>
<div class="box box-warning">
	<div class="box-header with-border">
		<h3 class="box-title">
			<spring:message code="netflow.collSer.config.conn.popu.header" ></spring:message>
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
				<spring:message code="netflow.collSer.config.conn.popu.ipaddr" var="label" ></spring:message>
				<spring:message code="netflow.collSer.config.conn.popu.ipaddr.tooltip" var="tooltip" ></spring:message>
				<div class="form-group">
					<div class="table-cell-label">${label}<span class="required">*</span><i class="fa fa-square"></i></div>
					<div class="input-group ">
						<form:input path="serverIp" cssClass="form-control table-cell input-sm" tabindex="4"	id="service-ipAddress" data-toggle="tooltip" data-placement="bottom" title="${tooltip }"></form:input>
						<spring:bind path="serverIp">
							<elitecoreError:showError errorMessage="${status.errorMessage}" errorId="service-ipAddress_error"></elitecoreError:showError>
						</spring:bind>
					</div>
				</div>
			</div>

			<div class="col-md-6 no-padding">
				<spring:message code="netflow.collSer.config.conn.popu.port" var="label" ></spring:message>
				<spring:message code="netflow.collSer.config.conn.popu.port.tooltip" var="tooltip" ></spring:message>
				<div class="col-md-9 no-padding">
				<div class="form-group">
					<div class="table-cell-label">${label}<span class="required">*</span><i class="fa fa-square"></i></div>
					<div class="input-group" style="vertical-align: middle;">
						<form:input path="netFlowPort" cssClass="form-control table-cell input-sm" tabindex="4" id="service-netFlowPort" data-toggle="tooltip" data-placement="bottom" title="${tooltip }" onkeypress="return isNumber(arguments[0] || window.event);"></form:input>
						<!-- <input type="hidden" id="isPortFree" value="false" /> -->
						<spring:bind path="netFlowPort">
							<elitecoreError:showError errorMessage="${status.errorMessage}" errorId="service-netFlowPort_error"></elitecoreError:showError>
						</spring:bind>
						</div>
						</div>
						</div>
						<div class="col-md-3 no-padding">
						<c:if test="${Boolean.TRUE.toString() ne  MapCache.getConfigValueAsObject(BaseConstants.KUBERNETES_ENV)}">
							<input type="button" value="<spring:message code="btn.label.checkfreePort" ></spring:message>" id="btnCheckFreePort" class="btn btn-grey btn-xs " onclick="checkPortAvailability('btnCheckFreePort');"/>
							<img src="${pageContext.request.contextPath}/img/loader.gif" width="20px" height="20px" id="imgLoader" style="margin-left: 5px;display: none;margin-top: 3px;" title="" />
						</c:if>						
						</div>
				
			</div>
			<c:if test="${(serviceType eq 'NATFLOW_COLLECTION_SERVICE') or (serviceType eq 'NATFLOWBINARY_COLLECTION_SERVICE')}">
						 <div class="col-md-6 no-padding">
							<spring:message code="online.service.enable.proxy.client" var="tooltip" ></spring:message>
							<div class="form-group">
								<div class="table-cell-label">${tooltip}<i class="fa fa-square"></i></div>
								<div class="input-group">
									<form:select path="proxyClientEnable" cssClass="form-control table-cell input-sm" tabindex="4" id="service-optionTemplateEnable" data-toggle="tooltip" data-placement="bottom"  title="${tooltip}" value="${NETFLOW_COLLECTION_SERVICE_CONFIGURATION_FORM_BEAN.proxyClientEnable}" onchange="changeProxyConfig(this.value);">
									<%
									 for(TrueFalseEnum enumlist : TrueFalseEnum.values()) { 
										 %>
										 	<form:option value="<%=  enumlist.getName() %>"><%= enumlist.toString() %></form:option>
										 <%
									     }
									%>
									</form:select>
									<spring:bind path="proxyClientEnable">
										<elitecoreError:showError errorMessage="${status.errorMessage}" errorId="service-proxyClientEnable_error"></elitecoreError:showError>
									</spring:bind>
								</div>
							</div>
						</div>
				<div class="col-md-6 no-padding" id="proxyServiceDiv">
				<spring:message code="online.service.port.proxy.client" var="label" ></spring:message>
				<spring:message code="online.service.port.proxy.client" var="tooltip" ></spring:message>
				<div class="col-md-9 no-padding">
				<div class="form-group">
					<div class="table-cell-label">${label}<span class="required">*</span><i class="fa fa-square"></i></div>
					<div class="input-group" style="vertical-align: middle;">
						<form:input path="proxyServicePort" cssClass="form-control table-cell input-sm" tabindex="4" id="service-proxyServicePort" data-toggle="tooltip" data-placement="bottom" title="${tooltip }" onkeypress="return isNumber(arguments[0] || window.event);"></form:input>
						<!-- <input type="hidden" id="isPortFree" value="false" /> -->
						<spring:bind path="proxyServicePort">
							<elitecoreError:showError errorMessage="${status.errorMessage}" errorId="service-proxyServicePort_error"></elitecoreError:showError>
						</spring:bind>
						</div>
						</div>
						</div>
						<div class="col-md-3 no-padding">
						<c:if test="${Boolean.TRUE.toString() ne  MapCache.getConfigValueAsObject(BaseConstants.KUBERNETES_ENV)}">
						<input type="button" value="<spring:message code="btn.label.checkfreePort" ></spring:message>" id="btnCheckProxyFreePort" class="btn btn-grey btn-xs " onclick="checkPortAvailability('btnCheckProxyFreePort');"/>
						<img src="${pageContext.request.contextPath}/img/loader.gif" width="20px" height="20px" id="imgLoaderProxy" style="margin-left: 5px;display: none;margin-top: 3px;" title="" />
						</c:if>
					</div>
			</div>
			<div class='col-md-12 no-padding title2' style='margin-bottom: 40px;' id="proxyClientConfiguration" >
				<span class='title2leftfield' id='proxyClientAddIcon'>
					<span class='title2rightfield-icon1-text'>
						<a href='#' onclick="viewProxyClientConfiguration('${serviceId}');"><i class='fa fa-plus-circle'></i></a>
						<a href='#' onclick="viewProxyClientConfiguration('${serviceId}')">
		    				<strong>Proxy Client Configuration</strong>
		    			</a>
					</span>
				</span>
			</div>
				<%-- <jsp:include page="proxyClientList.jsp" ></jsp:include> --%>
			</c:if>
			
			
			<c:if test="${(serviceType eq 'GTPPRIME_COLLECTION_SERVICE')}">
			<div class="col-md-6 no-padding">
				<spring:message code="gtpprime.collSer.config.conn.popu.rediripaddr" var="label" ></spring:message>
				<spring:message code="gtpprime.collSer.config.conn.popu.rediripaddr.tooltip" var="tooltip" ></spring:message>
				<div class="form-group">
					<div class="table-cell-label">${label}</div>
					<div class="input-group ">
						<form:input path="redirectionIP" cssClass="form-control table-cell input-sm" tabindex="5" id="service-redirectIpAddress" data-toggle="tooltip" data-placement="bottom" title="${tooltip }"></form:input>
						<spring:bind path="redirectionIP">
							<elitecoreError:showError errorMessage="${status.errorMessage}" errorId="service-redirectIpAddress_error"></elitecoreError:showError>
						</spring:bind>
					</div>
				</div>
			</div>
			</c:if>
			</div>
		</div>
	</div>
	<!-- Form content end here  -->
</div>


<!-- View char rename operation parameters popup code start here-->
		<a href="#divProxyClientList" class="fancybox" style="display: none;" id="proxy_client_param_link">#</a>
		<div id="divProxyClientList" style="width:100%; display: none;" >
			    <div class="modal-content">
			        <div class="modal-header padding10">
			            <h4 class="modal-title"><spring:message code="online.service.proxy.client.configuration"></spring:message></h4>
			        </div>
			        <div class="modal-body padding10 inline-form">
			        	<div style="width= calc(); display: block; color:red; padding-bottom: 0px;" id="noteMessageProxyClientConfig">
							<spring:message code="online.service.proxy.client.configure.note"></spring:message>
						</div>
			        	<div style="width= calc(); display: none; color:red; padding-bottom: 0px;" id="errorMessageProxyClientConfig">
							
						</div>
						<div  style="width= calc(); display: none; font:bold; padding-bottom: 0px;" id="successMessageProxyClientConfig">
							
						</div> 
		        		<div class="fullwidth">
		        			<span class="title2rightfield"> 
							    <span class="title2rightfield-icon1-text" id="proxy_client_add_link" >
				   					<a href="#" onclick="addProxyClient('','','','ADD');" ><i class="fa fa-plus-circle"></i></a>
				          			<a href="#" id="addProxyClientConfig" onclick="addProxyClient('','','','ADD');" >
				   					<spring:message code="btn.label.add" ></spring:message></a>
				   				</span> 
				   			</span> 
		        		</div>
						<div class="fullwidth" id="proxy_Config_details" style="overflow: auto;height:300px;">
						</div>
			        </div>
			        <div id="view_char_rename_params_buttons-div" class="modal-footer padding10">
			         	<button id="closePopup_btn" type="button" class="btn btn-grey btn-xs " data-dismiss="modal" onclick="closeFancyBox(); resetWarningMsg();"><spring:message code="btn.label.close"></spring:message></button>
			        </div>
			    </div>
			    <!-- /.modal-content --> 
			</div>
		<!-- View char rename operation parameters popup code end here-->

<a href="#divProxyConfigDeletemPopup" class="fancybox" style="display: none;" id="delete_proxy_config_param_link">#</a>
		
		<!-- Delete Char Rename operaton parameter pop up code start here -->
			<div id="divProxyConfigDeletemPopup" style="width:100%; display: none;" >
		    <div class="modal-content">
		    	<input type="hidden" id="deleteCharRenameBlockId"/>
		    	<input type="hidden" id="deleteCharRenameId"/>
		    	
		        <div class="modal-header padding10">
		            <h4 class="modal-title"><spring:message code="online.service.proxy.client.delete.popup.label"></spring:message></h4>
		        </div>
		        <div class="modal-body padding10 inline-form">
		        	<jsp:include page="../../common/responseMsgPopUp.jsp" ></jsp:include>
		        	
		        	<div>
		        		<spring:message code="delete.proxyparam.warn.msg"></spring:message>
		        	</div>
		        </div>
		        <div id="delete_buttons-div" class="modal-footer padding10">
		         	<button type="button" id="btndelete" class="btn btn-grey btn-xs " data-dismiss="modal" onclick="deleteProxyConfigParams('${serviceId}');"><spring:message code="btn.label.delete"></spring:message></button>
		         	<button type="button" class="btn btn-grey btn-xs " data-dismiss="modal" onclick="closeFancyBoxFromChildIFrame();viewProxyClientConfiguration('${serviceId}');"><spring:message code="btn.label.close"></spring:message></button>
		        </div>
		        <div id="delete_progress-bar-div" class="modal-footer padding10 text-left" style="display: none;">
					<jsp:include page="../../common/processing-bar.jsp"></jsp:include>
				</div>	
		        
		    </div>
		    <!-- /.modal-content --> 
		</div>
		
		<!-- Delete char Rename operation parameter pop up code end here -->

<script>
	var proxyConfigCounter = 1;	
	
	function addProxyClient(id,proxyClinetIP,proxyClientPort,mode){
		var charHtmlBody =  "<form method='POST' id='" + proxyConfigCounter + "_proxy_config_form'> "+
		"<div class='box box-warning' id='flipbox_char_"+proxyConfigCounter+"'>  "+
	    "<input type='hidden' id='"+proxyConfigCounter+"_proxy_config_id'  value='"+id+"'> "+
		"<div class='box-header with-border'> "+
		"	<h3 class='box-title' id='title_"+proxyConfigCounter+"'>"+
		"       	Proxy Client Configuration Parameter"+
		"	</h3>  "+
		"	<div class='box-tools pull-right' style='top: -3px;' id='action_"+proxyConfigCounter+"'> "+ 
		"		<button id='"+proxyConfigCounter+"_collapse_block' class='btn btn-box-tool block-collapse-btn' data-toggle='collapse' href='#"+proxyConfigCounter+"_proxy_client_block'> "+ 
		"			<i class='fa fa-minus'></i> "+
		"		</button> "+
	    "					&nbsp;&nbsp;<a class='ion-ios-close-empty remove-block' style='margin-top:12px;' onclick=displayDeleteProxyConfigPopup(\'"+proxyConfigCounter+"\');></a>&nbsp;" +
		"	</div> "+
		"</div> "+
		"<div class='box-body inline-form accordion-body collapsed in' id='"+proxyConfigCounter+"_proxy_client_block'> "+ 
		"	<div class='fullwidth inline-form'>  "+
		"		 <div class='col-md-6 no-padding'>"+
	    "       	<spring:message code='online.service.proxy.client.ip' var='tooltip'></spring:message>"+
	    "       	<spring:message code='online.service.proxy.client.ip' var='label'></spring:message>"+
	    "        	<div class='form-group'>"+
	    "    			<div class='table-cell-label'>${label}<span class='required'>*</span></div>"+
	    "         		<div class='input-group'>"+
	    "         			<input  id='" + proxyConfigCounter + "_proxyIp' value='"+proxyClinetIP+"' class='form-control table-cell input-sm' tabindex='1' data-toggle='tooltip' data-placement='bottom' title='${tooltip}' />"+
	    "         			<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='bottom' title=''></i></span>"+
	    "         		</div>"+
	    "        	</div>"+
	    "        </div>"+
		"		 <div class='col-md-6 no-padding'>"+
		"			<spring:message code='online.service.proxy.client.port' var='label'></spring:message> "+
		"			<spring:message code='online.service.proxy.client.port' var='tooltip'></spring:message>"+
		"			<div class='form-group'>"+
		"			<div class='table-cell-label'>${label}<span class='required'>*</span></div> "+
		"			<div class='input-group'>"+
		"				<input id='"+proxyConfigCounter+"_proxyPort' value='"+proxyClientPort+"' class='form-control table-cell input-sm' tabindex='1' data-toggle='tooltip' data-placement='bottom' title='proxyClientPort' onkeydown='isNumericOnKeyDown(event)' />"+
		"				<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='bottom' title=''></i></span>"+
		"			</div>"+
		"			</div>"+
		"			</div>"+
	    "		<div class='col-md-12 no-padding'>"+
	    "  			<div class='form-group'>"+
	    "      			<div id='" + proxyConfigCounter + "_proxy_buttons-div' class='input-group '>"+
		"								<button type='button' class='btn btn-grey btn-xs ' id='" + proxyConfigCounter + "_proxy_savebtn' onclick=addProxyParams(\'"+proxyConfigCounter+"\');><spring:message code='btn.label.save'></spring:message></button>&nbsp;" +
		"								<button type='button' class='btn btn-grey btn-xs ' id='" + proxyConfigCounter + "_proxy_updatebtn' onclick=updateProxyParams(\'"+proxyConfigCounter+"\');><spring:message code='btn.label.update'></spring:message></button>&nbsp;" +
	   
 		"				<button type='button' class='btn btn-grey btn-xs ' onclick=resetProxyParameters(\'"+proxyConfigCounter+"\');><spring:message code='btn.label.reset'></spring:message></button>&nbsp;" +
	    "       		</div>"+
	    "					<div id='" + proxyConfigCounter + "_proxy_progress-bar-div' class='input-group' style='display: none;'> "+
	    "						<label>Processing Request &nbsp;&nbsp; </label> "+
	    "							<img src='img/processing-bar.gif'>"+
		"					</div> "+
	    "   		</div>" +
	    "		</div>" +
		"	</div> "+
		"</div> "+
		"</div> "+
		"</form>";
		$('#proxy_Config_details').append(charHtmlBody);
		
		if(mode == 'ADD'){
			$("#"+proxyConfigCounter+"_proxy_updatebtn").hide();
			$("#"+proxyConfigCounter+"_proxy_savebtn").show();
			
		} else {
			$("#"+proxyConfigCounter+"_proxy_updatebtn").show();
			$("#"+proxyConfigCounter+"_proxy_savebtn").hide();
			$("#"+proxyConfigCounter+"_proxy_client_block").collapse("toggle");
		}
		proxyConfigCounter++;
	}
	
	function viewProxyClientConfiguration(serviceId){
		//$('#errorMessageProxyClientConfig').hide();
		resetWarningDisplay();
		//clearAllMessagesPopUp();
		$.ajax({
			url : 'getNatFlowClientListByServiceId',
			cache : false,
			async : true,
			dataType : 'json',
			type : 'POST',
			data : {
				"serviceId" : serviceId
			},
			success : function(data) {
				var response = data;
				var responseCode = response.code;
				var responseObject = response.object;
				
				$('#proxy_Config_details').html('');

				if (responseCode === "200") {
					if (responseObject !== 'undefined' && responseObject !== null) {
						$.each(responseObject, function(index, responseObject) {
							addProxyClient(responseObject["id"],responseObject["proxyIp"],responseObject["proxyPort"],'UPDATE');
						});
					}
				}
				$("#proxy_client_add_link").show();
				$("#proxy_client_param_link").click();
			},
			error : function(xhr, st, err) {
				handleGenericError(xhr, st, err);
			}
		});

	}
	
	function addProxyParams(counter){
		clearAllMessagesPopUp();
		clearProxyErrorMsg();
		$('#errorMessageProxyClientConfig').hide();
		$('#' + counter + '_proxy_buttons-div').hide();
		$('#' + counter + '_proxy_progress-bar-div').show();
		$('#' + counter + '_proxy_config_id').val('0');
		var proxyClinetIP = $('#' + counter + '_proxyClinetIP').val();
		$.ajax({
				url : 'addProxyClientParams',
				cache : false,
				async : true,
				dataType : 'json',
				type : "POST",
				data : {
					"id" 			: $('#' + counter + '_proxy_config_id').val(),
					"proxyIp" 	: $('#' + counter + '_proxyIp').val(),
					"proxyPort" 		: $('#' + counter + '_proxyPort').val(),
					"service.id"    : '${serviceId}',
					"blockCount" : counter
				},

				success : function(data) {
					$('#' + counter + '_proxy_buttons-div').show();
					$('#' + counter + '_proxy_progress-bar-div').hide();
					var response = data;
					var responseCode = response.code;
					var responseMsg = response.msg;
					var responseObject = response.object;
					if (responseCode === "200") {
						clearProxyErrorMsg();
						clearAllMessagesPopUp();
						resetWarningDisplayPopUp();
						showProxySuccessMessage(responseMsg);
						$("#" + counter + "_proxy_updatebtn").show();
						$("#" + counter + "_proxy_savebtn").hide();
						$('#' + counter + '_proxy_config_id').val(responseObject.id);
						$("#" + counter + "_proxy_client_block").collapse("toggle");
					} else if (responseObject !== undefined && responseObject !== 'undefined' && responseCode === "400") {
						$('#' + counter + '_proxy_buttons-div').show();
						$('#' + counter + '_proxy_progress-bar-div').hide();
						addErrorIconAndMsgForAjax(responseObject);
						clearProxyErrorMsg();
						showProxyErrorMsg(responseMsg);
					} else {
						$('#' + counter + '_proxy_buttons-div').show();
						$('#' + counter + '_proxy_progress-bar-div').hide();
						clearAllMessagesPopUp();
						resetWarningDisplayPopUp();
						$('#errorMessageProxyClientConfig').hide();
						showErrorMsgPopUp(responseMsg);
					}
				},
				error : function(xhr, st, err) {
					$('#' + counter + '_proxy_buttons-div').show();
					$('#' + counter + '_proxy_progress-bar-div').hide();
					handleGenericError(xhr, st, err);
				}
			});
	}
	
	function resetWarningMsg() {
		$("#errorMessageProxyClientConfig").hide();
	}
	
	function resetProxyParameters(counter){
		$("#"+counter+"_proxyPort").val('');
		$("#"+counter+"_proxyIp").val('');
	}
	
	var proxyConfigParamId,blockCounter = '';
	function displayDeleteProxyConfigPopup(counter) {
		var proxyConfigId = $('#' + counter + '_proxy_config_id').val();
		clearAllMessagesPopUp();
		resetWarningDisplay();
		proxyConfigParamId = proxyConfigId;
		blockCounter = counter;
		if (proxyConfigParamId === null || proxyConfigParamId === 'null' || proxyConfigParamId === '' || proxyConfigParamId === '0') {
			$("#" + counter + "_proxy_config_form").remove();
		} else {
			$("#delete_proxy_config_param_link").click();
		}
	}
	
function deleteProxyConfigParams(serviceId){
		clearAllMessagesPopUp();
		resetWarningDisplayPopUp();
		clearProxyErrorMsg();
		$.ajax({
				url : 'deleteProxyConfigParams',
				cache : false,
				async : true,
				dataType : 'json',
				type : "POST",
				data : {
					"id" : proxyConfigParamId
				},
				success : function(data) {
					var response = data;
					var responseCode = response.code;
					var responseMsg = response.msg;
					var responseObject = response.object;
					if (responseCode === "200") {
						$("#" + blockCounter + "_proxy_config_form").remove();
						resetWarningDisplay();
						clearAllMessages();
						showSuccessMsg(responseMsg);
						closeFancyBox();
						viewProxyClientConfiguration(serviceId);
					} else if (responseObject !== undefined && responseObject !== 'undefined' && responseCode === "400") {
						showErrorMsg(responseMsg);
					} else {
						showErrorMsg(responseMsg);
					}
				},
				error : function(xhr, st, err) {
					handleGenericError(xhr, st, err);
				}
			});
	}
	
function updateProxyParams(counter) {
	$('#errorMessageProxyClientConfig').hide();
	resetWarningDisplay();
	clearAllMessages();
	clearAllMessagesPopUp();
	$('#' + counter + '_proxy_buttons-div').hide();
	$('#' + counter + '_proxy_progress-bar-div').show();
	$.ajax({
		url : 'updateProxyParams',
		cache : false,
		async : true,
		dataType : 'json',
		type : "POST",
		data : {
			"id" 			: $('#' + counter + '_proxy_config_id').val(),
			"proxyIp" 	: $('#' + counter + '_proxyIp').val(),
			"proxyPort" 		: $('#' + counter + '_proxyPort').val(),
			"service.id"    : '${serviceId}',
			"blockCount" : counter
		},
		success : function(data) {
			$('#' + counter + '_proxy_buttons-div').show();
			$('#' + counter + '_proxy_progress-bar-div').hide();

			var response = data;
			var responseCode = response.code;
			var responseMsg = response.msg;
			var responseObject = response.object;
			if (responseCode === "200") {
				clearProxyErrorMsg();
				showProxySuccessMessage(responseMsg);
				$("#" + counter + "_proxy_updatebtn").show();
				$("#" + counter + "_proxy_savebtn").hide();
				$("#" + counter + "_proxy_client_block").collapse("toggle");
			} else if (responseObject !== undefined
					&& responseObject !== 'undefined'
					&& responseCode === "400") {
				$('#' + counter + '_proxy_buttons-div').show();
				$('#' + counter + '_proxy_progress-bar-div').hide();
				addErrorIconAndMsgForAjax(responseObject);
				clearAllMessagesPopUp();
				showProxyErrorMsg(responseMsg);
			} else {
				$('#' + counter + '_proxy_buttons-div').show();
				$('#' + counter + '_proxy_progress-bar-div').hide();
				clearProxyErrorMsg();
				clearAllMessagesPopUp();
				showProxyErrorMsg(responseMsg);
			}
		},
		error : function(xhr, st, err) {
			$('#' + counter + '_proxy_buttons-div').show();
			$('#' + counter + '_proxy_progress-bar-div').hide();
			handleGenericError(xhr, st, err);
		}
	});

 }
	
	function clearProxyErrorMsg(){
		$("#errorMessageProxyClientConfig").html('');
		$("#successMessageProxyClientConfig").html('');
		$("#errorMessageProxyClientConfig").hide();
		$("#successMessageProxyClientConfig").hide();
	}
	
	function showProxySuccessMessage(msg){
		$("#successMessageProxyClientConfig").html('<strong>'+msg+'</strong>');
		$("#successMessageProxyClientConfig").show();
	}
	
	function showProxyErrorMsg(msg){
		$("#errorMessageProxyClientConfig").html(msg);
		$("#errorMessageProxyClientConfig").show();
	}
	
	function showSuccessMsgProxy(msg){
		$("#proxy_success_text").show();
		$("#proxy_success_text").html(msg);
	}
	
	function showErrorMsgPopUp(errorMsg){
		$(".divResponseMsg").html(
				'<div style="width: calc();" class="errorMessage" id="divButtonPopUp">'  +
					'<span id="span_success_txt" class="title">' + errorMsg + '</span>' +
				'</div>'	
		);
		$(".divResponseMsg").show();
	}

	function showSuccessMsgPopUp(msg){
		$(".divResponseMsg").html(
				'<div style="width:calc();" class="successMessage" id="divButtonPopUp">'  +
					'<span id="span_success_txt" class="title">' + msg + '</span>' +
				'</div>'	
		);
		$(".divResponseMsg").show();
	}
	
	function clearAllMessagesPopUp(){
		clearResponseMsgDivPopUp();
		clearResponseMsgPopUp();
		clearErrorMsgpopUp();
	}

	function clearResponseMsgDivPopUp(){
		$('#responseMsgDivpopUp').html('');
		$('.divResponseMsg').html('');
		$(".divResponseMsg").hide();
		
	}
	function clearResponseMsgPopUp(){
		$('#divResponseMsgPopUp').html('');
		$('.divResponseMsg').html('');
		$(".divResponseMsg").hide();
		
	}
	function clearErrorMsgpopUp(){
		$('#divErrorMsgPopUp').html('');
		$('.divResponseMsg').html('');
		$(".divResponseMsg").hide();
		
	}
</script>
