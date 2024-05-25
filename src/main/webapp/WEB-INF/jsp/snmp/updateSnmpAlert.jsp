<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
  <%@page import="com.elitecore.sm.common.constants.SystemParametersConstant"%>
    <%@page import="com.elitecore.sm.util.MapCache"%>
<%@taglib 	uri="http://www.springframework.org/tags" 			prefix="spring"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="elitecore" %> 
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="/WEB-INF/ShowErrorTag.tld" prefix="elitecoreError"%>
    <%@ taglib 	uri="http://www.springframework.org/security/tags" 	prefix="sec"  %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>


<div class="modal-content">
			<div class="modal-header padding10">
					<h4 class="modal-title">
						<i class="icon-hdd"></i> 
						
						 <span id="alert_label">
						 <spring:message code="snmpConfiguration.alertList.caption.label" ></spring:message></span> 
									
					</h4>
				</div>
				<div class="modal-body padding10 inline-form">
						<input type="hidden" id="snmpAlertId" name="snmpAlertId" /> 
						 
						<div class="fullwidth">
							<spring:message
								code="snmpClientList.grid.column.alertList.id"
								var="name" ></spring:message>
							
							<spring:message
								code="snmpAlertList.alertList.id.tooltip"
								var="tooltip" ></spring:message>
								
							<div class="form-group">
								<div class="table-cell-label">${name}<span class="required">*</span></div>
								<div class="input-group ">
									<input type="text" id="update_alertId" class="form-control table-cell input-sm" data-toggle="tooltip" data-placement="bottom"  title='${tooltip }' readonly="readonly"/> 
	             		<span class="input-group-addon add-on last" > <i class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="left" title=""></i></span> 
								
								</div>
							</div>
						</div>
						<div class="fullwidth">
							<spring:message
								code="snmpClientList.grid.column.alertList.name"
								var="name" ></spring:message>
							
							<spring:message
								code="snmpAlertList.alertList.name.tooltip"
								var="tooltip" ></spring:message>
								
							<div class="form-group">
								<div class="table-cell-label">${name}<span class="required">*</span></div>
								<div class="input-group ">
									
									<input type="text" id="update_name" class="form-control table-cell input-sm" data-toggle="tooltip" data-placement="bottom"  title='${tooltip }' readonly="readonly"/> 
	             		<span class="input-group-addon add-on last" > <i class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="left" title=""></i></span>
									
								</div>
							</div>
						</div>
						<div class="fullwidth">
							<spring:message
								code="snmpAlertList.grid.column.description"
								var="name" ></spring:message>
							
							<spring:message
								code="snmpAlertList.grid.column.description"
								var="tooltip" ></spring:message>
								
							<div class="form-group">
								<div class="table-cell-label">${name}</div>
								<div class="input-group ">
									
									
									<input type="text" id="update_desc" class="form-control table-cell input-sm" data-toggle="tooltip" data-placement="bottom"  title='${tooltip }'/> 
	             		<span class="input-group-addon add-on last" > <i class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="left" title=""></i></span>
									
									
								</div>
							</div>
						</div>
						</div>
						<div id="update-snmp-alert-buttons-div" class="modal-footer padding10">
					
					<sec:authorize access="hasAuthority('EDIT_ALERT_DETAILS')">
					<button id="edit_alert_update_btn" type="button" id="ceditAlertDetails"
							class="btn btn-grey btn-xs " 
							onclick="updateAlertToDB();">
							<spring:message code="btn.label.update" ></spring:message>
						</button>
					</sec:authorize>
					
					<button id="edit_alert_cancel_btn" onclick="javascript:parent.closeFancyBoxFromChildIFrame()" class="btn btn-grey btn-xs ">
						<spring:message code="btn.label.cancel" ></spring:message>
					</button>
					
				</div>
				
				<div id="update-snmp-alert-progress-bar-div" class="modal-footer padding10" style="display: none;">
					<jsp:include page="../common/processing-bar.jsp"></jsp:include>
				</div>
				
</div>

<script>

function updateAlertToDB(){
	resetWarningDisplayPopUp();
	clearAllMessagesPopUp();
	
	showProgressBar("update-snmp-alert");
	parent.resizeFancyBox();
	

	$.ajax({
		url: '<%=ControllerConstants.UPDATE_SNMP_ALERT%>',
		cache: false,
		async: true, 
		dataType: 'json',
		type: "POST",
		data: 
		{
			"id"    				:    $("#id").val(),
			"alertId"    			:    $("#update_alertId").val(),
			"name"    				:    $("#update_name").val(),
			"desc"    				:    $("#update_desc").val()
		},
		success: function(data){
			hideProgressBar("update-snmp-alert");
			getSnmpServerList();

			var response = eval(data);
			
			var responseCode = response.code; 
			var responseMsg = response.msg; 
			var responseObject = response.object;
			 
			if(responseCode == "200"){
				
				showSuccessMsg(responseMsg);
				//reloadGridData();
				closeFancyBox();
				//reloadAlertGridData();
				getSnmpAlertList();
				
			}else if(responseObject != undefined && responseObject != 'undefined' && responseCode == "400"){
				
				$("#create-server-buttons-div").show();
				
				addErrorIconAndMsgForAjaxPopUp(responseObject);
			}else{
				
				resetWarningDisplayPopUp();
				
				showErrorMsgPopUp(responseMsg);
				
			}
		},
	    error: function (xhr,st,err){
	    	hideProgressBar("update-snmp-alert");
	    	
			handleGenericError(xhr,st,err);
		}
	});
}

</script>
