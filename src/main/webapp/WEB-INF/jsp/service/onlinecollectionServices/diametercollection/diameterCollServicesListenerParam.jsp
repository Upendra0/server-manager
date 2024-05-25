<%@page import="com.elitecore.sm.util.MapCache"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="/WEB-INF/ShowErrorTag.tld" prefix="elitecoreError"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page import="com.elitecore.sm.common.constants.BaseConstants"%>
<script>
var jsLocaleMsg = {};
jsLocaleMsg.checkfreePort = "<spring:message code="btn.label.checkfreePort" ></spring:message>";
jsLocaleMsg.portMissing = "<spring:message code='service.listener.port.missing'></spring:message>";

function checkPortAvailability(){
	clearAllMessages();
	clearResponseMsgDiv();
	clearResponseMsg();
	clearErrorMsg();
	resetWarningDisplay();
	var port = $('#service-netFlowPort').val().trim();

	if(port != '' && port.length>0){
	
		$('#btnCheckFreePort').hide();
		$('#imgLoader').show();
		$.ajax({                                                                                                                                                                                                                                                      
			url: '<%= ControllerConstants.CHECK_PORT_AVAILIBILITY %>',
			cache: false,
			async: true,
			dataType: 'json',
			type: "POST",
			data: {
				"port" : port,
				"serverInstanceId": '${instanceId}'
			},
			success: function(data){
				var response = eval(data);
				var responseCode = response.code; 
				var responseMsg = response.msg; 
				var responseObject = data.object;
				$('#imgLoader').hide();
				$('#btnCheckFreePort').show();
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
			<spring:message code="diameter.service.config.stack.details.header" ></spring:message>
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
				<spring:message code="diameter.service.config.stack.details.identity" var="label" ></spring:message>
				<spring:message code="diameter.service.config.stack.details.identity.tooltip" var="tooltip" ></spring:message>
				<div class="form-group">
					<div class="table-cell-label">${label}</div>
					<div class="input-group ">
						<form:input path="stackIdentity" cssClass="form-control table-cell input-sm" tabindex="4"	id="service-identity" data-toggle="tooltip" data-placement="bottom" title="${tooltip }" maxlength="255"></form:input>
						<spring:bind path="stackIdentity">
							<elitecoreError:showError errorMessage="${status.errorMessage}" errorId="service-identity_error"></elitecoreError:showError>
						</spring:bind>
					</div>
				</div>
			</div>
			
			<div class="col-md-6 no-padding">
				<spring:message code="diameter.service.config.stack.details.realm" var="label" ></spring:message>
				<spring:message code="diameter.service.config.stack.details.realm.tooltip" var="tooltip" ></spring:message>
				<div class="form-group">
					<div class="table-cell-label">${label}</div>
					<div class="input-group ">
						<form:input path="stackRealm" cssClass="form-control table-cell input-sm" tabindex="4"	id="service-realm" data-toggle="tooltip" data-placement="bottom" title="${tooltip }" maxlength="255"></form:input>
						<spring:bind path="stackRealm">
							<elitecoreError:showError errorMessage="${status.errorMessage}" errorId="service-realm_error"></elitecoreError:showError>
						</spring:bind>
					</div>
				</div>
			</div>
			
			<div class="col-md-6 no-padding">
				<spring:message code="diameter.service.config.stack.details.ipaddr" var="label" ></spring:message>
				<spring:message code="diameter.service.config.stack.details.ipaddr.tooltip" var="tooltip" ></spring:message>
				<div class="form-group">
					<div class="table-cell-label">${label}<span class="required">*</span></div>
					<div class="input-group ">
						<form:input path="stackIp" cssClass="form-control table-cell input-sm" tabindex="4"	id="service-ipAddress" data-toggle="tooltip" data-placement="bottom" title="${tooltip }"></form:input>
						<spring:bind path="stackIp">
							<elitecoreError:showError errorMessage="${status.errorMessage}" errorId="service-ipAddress_error"></elitecoreError:showError>
						</spring:bind>
					</div>
				</div>
			</div>

			<div class="col-md-6 no-padding">
				<spring:message code="diameter.service.config.stack.details.port" var="label" ></spring:message>
				<spring:message code="diameter.service.config.stack.details.port.tooltip" var="tooltip" ></spring:message>
				<div class="col-md-9 no-padding">
					<div class="form-group">
						<div class="table-cell-label">${label}<span class="required">*</span></div>
						<div class="input-group" style="vertical-align: middle;">
							<form:input path="stackPort" cssClass="form-control table-cell input-sm" tabindex="4" id="service-netFlowPort" data-toggle="tooltip" maxlength="5" data-placement="bottom" title="${tooltip }" onkeypress="return isNumber(arguments[0] || window.event);"></form:input>
							<!-- <input type="hidden" id="isPortFree" value="false" /> -->
							<spring:bind path="stackPort">
								<elitecoreError:showError errorMessage="${status.errorMessage}" errorId="service-netFlowPort_error"></elitecoreError:showError>
							</spring:bind>
							</div>
					</div>
				</div>
				<div class="col-md-3 no-padding">
					<c:if test="${Boolean.TRUE.toString() ne  MapCache.getConfigValueAsObject(BaseConstants.KUBERNETES_ENV)}">
					<input type="button" value="<spring:message code="btn.label.checkfreePort" ></spring:message>" id="btnCheckFreePort" class="btn btn-grey btn-xs " onclick="checkPortAvailability();"/>
					<img src="${pageContext.request.contextPath}/img/loader.gif" width="20px" height="20px" id="imgLoader" style="margin-left: 5px;display: none;margin-top: 3px;" title="" />
					</c:if>
				</div>
			</div>
		</div>
	</div>
</div>
	<!-- Form content end here  -->
