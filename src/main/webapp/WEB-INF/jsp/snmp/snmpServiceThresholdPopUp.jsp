<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<%@taglib 	uri="http://www.springframework.org/tags" 			prefix="spring"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="elitecore" %> 
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="/WEB-INF/ShowErrorTag.tld" prefix="elitecoreError"%>
    <%@ taglib 	uri="http://www.springframework.org/security/tags" 	prefix="sec"  %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<script>
var ckSnmpAlertSvc = new Array();

function updateServiceThreshold()
{
	var serviceThresholdAlertList = [];
	showProgressBar("update-snmp-service-threshold");
	$("#tblConfigAlertSvcList").find('tr.serviceDetail').each(function (i) {
        var $tds = $(this).find('td'),
            svcId = $tds.eq(0).text(),
            alertName = $tds.eq(1).text(),
            svcName = $tds.eq(2).text(),
            servInstanceId = $tds.eq(3).text(),
            threshold = $("#"+servInstanceId+"_"+svcName+"_threshold").val(),
            thresholdId =$("#"+svcId+"_thresholdId").val() ;
        
        if(!threshold) {
        	threshold = 0;
        }
       
        var check = ($tds==null);
       
        // do something with productId, product, Quantity
       
        
        var alertSvcThreshold = {};
        
        alertSvcThreshold.svcId=svcId;
        alertSvcThreshold.threshold=threshold;
        alertSvcThreshold.svcName=svcName;
        alertSvcThreshold.servInstanceId=servInstanceId;
        alertSvcThreshold.thresholdId=thresholdId;
        alertSvcThreshold.alertName = alertName;
		
        serviceThresholdAlertList.push(alertSvcThreshold);
 
        
    });	
	
	$.ajax({
		url: '<%=ControllerConstants.UPDATE_SERVICE_THRESHOLD%>',
		cache: false,
		async: true, 
		dataType: 'json',
		type: "POST",
		data: 
		{
			"alertId"					:   $("#selected_alertId").val(),
			"serverInstanceId"			:   $("#server_Instance_Id").val(),
			"selectedAlertType"			:	$("#selected_alertType").val(),
			"alertsvcList"    			:    JSON.stringify(serviceThresholdAlertList),
		},
		success: function(data){
			hideProgressBar("update-snmp-service-threshold");
			
			var response = eval(data);
			
			var responseCode = response.code; 
			var responseMsg = response.msg; 
			var responseObject = response.object;
			 console.log("Response object is ::  " + responseObject);
			if(responseCode == "200"){
				closeFancyBox();
				showSuccessMsg(responseMsg);
				//reloadAlertGridData();
				
				getSnmpAlertList();
					} 
			else if(responseObject != undefined && responseObject != 'undefined' && responseCode == "400"){
				console.log("got 400 response.");
			//	$("#create-server-buttons-div").show();
				
				addErrorIconAndMsgForAjaxPopUp(responseObject);

				
			}else{
				
				resetWarningDisplayPopUp();
				$("#create-server-buttons-div").show();
				showErrorMsgPopUp(responseMsg);
				reloadGridData();
			}
		},
	    error: function (xhr,st,err){
	    	hideProgressBar("update-snmp-service-threshold");
	    	$("#create-server-buttons-div").show();
			handleGenericError(xhr,st,err);
		}
	});
	
}

</script>
<div class="modal-content">
				<div class="modal-header padding10">
					<h4 class="modal-title">
						<i class="icon-hdd"></i> 
						
						 <span id="serviceThreshold_Label" ><spring:message
									code="snmp.serviceThreshold.popUp.label" ></spring:message></span> 
									
							
					</h4>
				</div>
				
				<div id="snmpAlertServiceThresholdC0ntentDiv" class="modal-body padding10 inline-form">
				
					<jsp:include page="../common/responseMsgPopUp.jsp" ></jsp:include>
					
					<input type="hidden" id="server_Instance_Id" name="server_Instance_Id" value='${serverInstanceId}' /> 
					
					<input type="hidden" id="selected_alertId" name="selected_alertId" />
					
					<input type="hidden" id="selected_alertType" name="selected_alertType" /> 
					
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
									<input type="text"
										class="form-control table-cell input-sm" tabindex="4"
										id="alertId" 
										title="${tooltip}" readonly="readonly"/>
								
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
									<input type="text" id="name" class="form-control table-cell input-sm"   title='${tooltip }' readonly="readonly"/> 
	             		<span class="input-group-addon add-on last" > <i class="glyphicon glyphicon-alert" title=""></i></span> 
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
									<input type="text" id="desc" class="form-control table-cell input-sm" data-toggle="tooltip" data-placement="bottom"  title='${tooltip }' readonly="readonly"/> 
	             		<span class="input-group-addon add-on last" > <i class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="left" title=""></i></span>	
									
								</div>
							</div>
						</div>
						
<!-- 						<div id="configAlertSvcContentDiv_threshold" class="fullwidth"> -->
<%-- 							<spring:message --%>
<%-- 								code="snmp.alert.serviceThreshold.grid.threshold" --%>
<%-- 								var="name" /> --%>
							
<%-- 							<spring:message --%>
<%-- 								code="snmp.alert.serviceThreshold.grid.threshold" --%>
<%-- 								var="tooltip" /> --%>
								
<!-- 							<div class="form-group"> -->
<%-- 								<div class="table-cell-label">${name}</div> --%>
<!-- 								<div class="input-group "> -->

<%-- 										<input type="text" id="configAlert_threshold" class="form-control table-cell input-sm" data-toggle="tooltip" data-placement="bottom"  title='${tooltip }'/>  --%>
<!-- 	             		<span class="input-group-addon add-on last" > <i class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="left" title=""></i></span> -->
									
<!-- 								</div> -->
<!-- 							</div> -->
<!-- 						</div> -->
				
			
				</div>
				
				<div id="configAlertSvcContentDiv" class="modal-body padding10 inline-form">
					<div id="sampleTableSvc">
<%-- 						<jsp:include page="../../../common/responseMsgPopUp.jsp" ></jsp:include> --%>
						</div>

						<div class="table-responsive " id= "tblConfigAlertSvcListDiv" style="height:auto;max-height:300px;overflow: auto;">
						<table class="table table-hover table-bordered " id="tblConfigAlertSvcList">
						</table>
						
						</div>

			</div>
				
				<div id="update-snmp-service-threshold-buttons-div" class="modal-footer padding10">
					<button type="button" id="addNewAttribute" class="btn btn-grey btn-xs " onclick="updateServiceThreshold()">
						<spring:message code="btn.label.save" ></spring:message>
					</button>
					
					<sec:authorize access="hasAuthority('ADD_SERVICE_THRESHOLD')">
					<button type="button" id="editAttribute"
							class="btn btn-grey btn-xs " style="display: none"
							onclick="updateServerToDB();">
							<spring:message code="btn.label.update" ></spring:message>
						</button>
					</sec:authorize>
					
					<button id="thresholdPopUpCancel" onclick="javascript:parent.closeFancyBoxFromChildIFrame()" class="btn btn-grey btn-xs ">
						<spring:message code="btn.label.cancel" ></spring:message>
					</button>
					
				</div>
				<div id="update-snmp-service-threshold-progress-bar-div" class="modal-footer padding10" style="display: none;">
					<jsp:include page="../common/processing-bar.jsp"></jsp:include>
				</div>
</div>
</body>
</html>
