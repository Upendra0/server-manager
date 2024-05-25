
<%@page import="com.elitecore.sm.util.MapCache"%>
<%@page
	import="com.elitecore.sm.common.constants.SystemParametersConstant"%>
<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<%@page import="com.elitecore.sm.util.EliteUtils"%>
<%@page import="com.elitecore.sm.common.constants.BaseConstants"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/security/tags"
	prefix="sec"%>
<%@taglib uri="/WEB-INF/ShowErrorTag.tld" prefix="elitecoreError"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<html>
<script>
	var pathListCounter=-1;
	var serviceDbStats='${serviceDbStats}';
	var forDuplicateEnabled = '${forDuplicatEnabled}';
</script>
<body class="skin-blue sidebar-mini sidebar-collapse">
	<%-- <jsp:include page="../common/newheader.jsp" ></jsp:include>
<jsp:include page="../common/newleftMenu.jsp"></jsp:include> --%>
	<div class="fullwidth inline-form"
		style="padding-left: 0px !important;">
		<div class="box-body padding0 mtop10 clearfix" id="host_Confg_Block">
			<div class="fullwidth mtop10">

				<div>
					<div class="col-md-6 inline-form"
						style="padding-left: 0px !important;">

						<%--  <div class="form-group">
	             	<spring:message code="roaming.configuration.partner.label" var="label" ></spring:message>
	             	<spring:message code="roaming.configuration.partner.tooltip" var="tooltip" ></spring:message>
	         		<div class="table-cell-label">${label}</div>
	             	<div class="input-group col-md-6 ">
	             		<input type="text" id="partner" name="partner" maxlength="50" value = "${partner}" class="form-control table-cell input-sm" data-toggle="tooltip" data-placement="bottom" title="${tooltip }">
	             		<a href='#' onclick = 'displayPartnerPopUp();' tabindex='9' style='vertical-align:middle;' id='partnerId'><i style='margin-top: 8px;margin-left: 8px;' class='glyphicon glyphicon-search'></i></a>
	             		<a href="#selectPartnerDiv" class="fancybox" style="display: none;" id="viewMore">#</a>
	             		<span class="input-group-addon add-on last" > <i id="partnerId_err" class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="left" title=""></i></span>
	             	</div>
	             	
	             	
	             </div>  --%>

						<div class='form-group'>
							<spring:message code="roaming.configuration.partner.label"
								var="label" ></spring:message>
							<spring:message code="roaming.configuration.partner.tooltip"
								var="tooltip" ></spring:message>
							<div class="table-cell-label">${label}<span
									class='required'>*</span>
							</div>
							<div class='input-group'>
								<input type="hidden" class="form-control table-cell input-sm"
									id="${pathListCounter}_partnerId" data-toggle="tooltip"
									data-placement="bottom" title="${tooltip }" /> <input
									type="text" class="form-control table-cell input-sm"
									id="partner" value="${partner}" data-toggle="tooltip"
									data-placement="bottom" title="${tooltip }" style="width: 42%;" />
								<span> <a href="#"
									onclick="selectPartnerPopUp(); "
									id="select_popup_${pathListCounter}_lnk"><i
										style='margin-top: 8px; margin-left: 8px;'
										class='glyphicon glyphicon-search'></i></a></span> <span
									class="input-group-addon add-on last"> <i
									id="partnerId_err" class="glyphicon glyphicon-alert"
									data-toggle="tooltip" data-placement="left" title=""></i></span>
							</div>

						</div>
						<div class="form-group ">
							<spring:message code="roaming.configuration.lob.label"
								var="label" ></spring:message>
							<spring:message code="roaming.configuration.lob.label"
								var="tooltip" ></spring:message>
							<div class="table-cell-label">${label}</div>
							<div class="input-group">
								<input type="text" class="form-control table-cell input-sm"
									id="lob" value="${lob}" data-toggle="tooltip"
									data-placement="bottom" title="${tooltip }" readonly="readonly" />
							</div>
						</div>
					</div>
					<div class="col-md-6 inline-form"
						style="padding-left: 0px !important;">
						<div class="form-group ">
							<spring:message code="roaming.configuration.primary.tadig.label"
								var="label" ></spring:message>
							<spring:message code="roaming.configuration.primary.tadig.label"
								var="tooltip" ></spring:message>
							<div class="table-cell-label">${label}</div>
							<div class="input-group">
								<input type="text" class="form-control table-cell input-sm"
									id="primaryTadig" value="${primaryTadig}" data-toggle="tooltip"
									data-placement="bottom" title="${tooltip }" readonly="readonly" />
							</div>
						</div>
						<div class="form-group ">
							<spring:message
								code="roaming.configuration.secondary.tadig.label" var="label" ></spring:message>
							<spring:message
								code="roaming.configuration.secondary.tadig.label" var="tooltip" ></spring:message>
							<div class="table-cell-label">${label}</div>
							<div class="input-group">
								<input type="text" class="form-control table-cell input-sm"
									id="secondaryTadig" value="${secondaryTadig}"
									data-toggle="tooltip" data-placement="bottom"
									title="${tooltip }" readonly="readonly" />
							</div>
						</div>

					</div>
				</div>

				<%-- <div>
 <button type="button" id="searchButton" class="btn btn-grey btn-xs " onclick="searchRapTapConfigurationDetails()"><spring:message code="btn.label.search"></spring:message></button>
 
 
 </div>
  --%>




			</div>
			<div class="row">
				<div class="col-xs-12">
					<div class="box martop" style="border: none; box-shadow: none;">
						<!-- /.box-header -->
						<div class="box-body table-responsive no-padding">

							<div class="nav-tabs-custom">

								<!-- Tabs within a box -->
								<ul class="nav nav-tabs">

									<li
										class="<c:if test="${(REQUEST_ACTION_TYPE eq 'RAP_TAP_CONFIGURATION') }"><c:out value="active"></c:out></c:if>"
										onclick="loadFileManagementTab()"><a
										href="#genGroup-tab1" data-toggle="tab" id="genGroup-tab-a1"><spring:message
												code="roamingconfiguration.filemanagement.tab" ></spring:message></a></li>


									<li
										class="<c:if test="${(REQUEST_ACTION_TYPE eq 'TEST_SIM_MANAGEMENT') }"><c:out value="active"></c:out></c:if>"
										onclick="loadTestSimManagementTab()"><a
										href="#genGroup-tab" data-toggle="tab" id="genGroup-tab-a"><spring:message
												code="roamingconfiguration.testsim.management.tab" ></spring:message></a></li>

									<li
										class="<c:if test="${(REQUEST_ACTION_TYPE eq 'FILE_SEQUENCE_MANAGEMENT') }"><c:out value="active"></c:out></c:if>"
										onclick="loadFileSequenceManagementTab()"><a
										href="#genGroup-tab2" data-toggle="tab" id="genGroup-tab-a"><spring:message
												code="roamingconfiguration.filesequence.management.tab" ></spring:message></a>
									</li>


								</ul>
								<div class="fullwidth tab-content no-padding">

									<div
										class="tab-pane <c:if test="${(REQUEST_ACTION_TYPE eq 'RAP_TAP_CONFIGURATION') }"><c:out value="active"></c:out></c:if>"
										id="genGroup-tab">
										<c:if
											test="${(REQUEST_ACTION_TYPE eq 'RAP_TAP_CONFIGURATION') }"><jsp:include
												page="fileManagement.jsp" ></jsp:include></c:if>
									</div>

									<div
										class="tab-pane <c:if test="${(REQUEST_ACTION_TYPE eq 'TEST_SIM_MANAGEMENT') }"><c:out value="active"></c:out></c:if>"
										id="genGroup-tab1">
										<c:if
											test="${(REQUEST_ACTION_TYPE eq 'TEST_SIM_MANAGEMENT') }"><jsp:include
												page="testSimManagement.jsp" ></jsp:include></c:if>
									</div>

									<div
										class="tab-pane <c:if test="${(REQUEST_ACTION_TYPE eq 'FILE_SEQUENCE_MANAGEMENT') }"><c:out value="active"></c:out></c:if>"
										id="genGroup-tab2">
										<c:if
											test="${(REQUEST_ACTION_TYPE eq 'FILE_SEQUENCE_MANAGEMENT') }"><jsp:include
												page="fileSequenceManagement.jsp" ></jsp:include></c:if>
									</div>


								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>

	</div>



	<div style="display: hidden">
		<form id="loadFileManagementTab"
			action="<%=ControllerConstants.INIT_ROAMING_CONFIGURATION%>"
			method="GET">
			<input type="hidden" name="<%=BaseConstants.REQUEST_ACTION_TYPE%>"
				id="<%=BaseConstants.RAP_TAP_CONFIGURATION%>"
				value="<%=BaseConstants.RAP_TAP_CONFIGURATION%>" />
		</form>
	</div>


	<div style="display: hidden">
		<form id="loadTestSimManagementTab"
			action="<%=ControllerConstants.INIT_ROAMING_CONFIGURATION%>"
			method="GET">
			<input type="hidden" name="<%=BaseConstants.REQUEST_ACTION_TYPE%>"
				id="<%=BaseConstants.TEST_SIM_MANAGEMENT%>"
				value="<%=BaseConstants.TEST_SIM_MANAGEMENT%>" />
		</form>
	</div>

	<div style="display: hidden">
		<form id="loadFileSequenceManagementTab"
			action="<%=ControllerConstants.INIT_ROAMING_CONFIGURATION%>"
			method="GET">
			<input type="hidden" id="<%=BaseConstants.FILE_SEQUENCE_MANAGEMENT%>"
				name="<%=BaseConstants.REQUEST_ACTION_TYPE%>"
				value="<%=BaseConstants.FILE_SEQUENCE_MANAGEMENT%>">
		</form>
	</div>

	<div id=selectPartnerDiv>
		<jsp:include page="partnerDetails.jsp"></jsp:include>
	</div>

</body>

<head>
<script type="text/javascript">
function validateRoamingParameterFields(){
	$("#save-roaming-parm-form").attr("action","<%=ControllerConstants.MODIFY_ROAMING_PARAMETER%>")
	$("#save-roaming-parm-form").submit();
	
}
function resetRoamingParameterForm(){
	$("#tapin_frequencyId").val('');
	$("#nrtrde_in_frequencyId").val('');
	$("#nrtrde_out_frequencyId").val('');
	resetWarningDisplay();
}
function loadFileManagementTab(){
	$("#partnerNameId").val(document.getElementById("partner").value);
	$("#lobId").val(document.getElementById("lob").value);
	$("#primaryTadigId").val(document.getElementById("primaryTadig").value);
	$("#secondaryTadigId").val(document.getElementById("secondaryTadig").value);
		
	$("#loadFileManagementTab").submit();	
}
function loadTestSimManagementTab(){
	$("#partnerNameId").val(document.getElementById("partner").value);
	$("#lobId").val(document.getElementById("lob").value);
	$("#primaryTadigId").val(document.getElementById("primaryTadig").value);
	$("#secondaryTadigId").val(document.getElementById("secondaryTadig").value);
	$("#loadTestSimManagementTab").submit();
}
function loadFileSequenceManagementTab(){
	$("#partnerNameId").val(document.getElementById("partner").value);
	$("#lobId").val(document.getElementById("lob").value);
	$("#primaryTadigId").val(document.getElementById("primaryTadig").value);
	$("#secondaryTadigId").val(document.getElementById("secondaryTadig").value);
	$("#loadFileSequenceManagementTab").submit();
}
function searchRapTapConfigurationDetails(){
	resetWarningDisplay();
	clearAllMessages();
	$("#partnerNameId").val(document.getElementById("partner").value);
	$("#lobId").val(document.getElementById("lob").value);
	var req = '${REQUEST_ACTION_TYPE}';
	if(req === '<%=BaseConstants.FILE_SEQUENCE_MANAGEMENT%>'){
		searchFileSequenceDetails();
		
	}else if (req === '<%=BaseConstants.TEST_SIM_MANAGEMENT%>'){
		searchTestSimManagement();
	}else if(req === '<%=BaseConstants.RAP_TAP_CONFIGURATION%>'){
		searchRoamingFileManagement();
	}
	
}	

function searchFileSequenceDetails(){
	var ajaxRequest = new XMLHttpRequest();
	var partner = document.getElementById("partner").value
	var lob = document.getElementById("lob").value
	$.ajax({
		url: '<%=ControllerConstants.VIEW_FILE_SEQUENCE_MANAGEMENT%>',
    	data:
    		{
    		   'partner':partner,
			   'lob':lob,
			   
    		},
		dataType: 'json',
		type: "POST",
		success: function(data){	
			var response = eval(data);			
			var responseCode = response.code; 
			var responseMsg = response.msg; 
			var responseObject = response.object;	
			if(responseCode == "200"){
				var fileSequence=eval(responseObject);				
				var fileSequenceList=fileSequence['fileSequenceList'];
				
				if(fileSequenceList.length == 0){
					$("#test-tap-in").val('');
					$("#commercial-tap-in").val('');
					$("#commercial-rap-out").val('');
					$("#test-rap-out").val('');
					$("#commercail-rap-in").val('');
					$("#test-rap-in").val('');
					$("#commercial-tap-out").val('');
					$("#test-tap-out").val('');
					$("#nrtrdeOut").val('');
					$("#nrtrdeIn").val('');
					 displaySequencyNumber();
				}	
				else{
					for(var i = 0; i < fileSequenceList.length; i++) {
						
					    var obj = fileSequenceList[i];
					    Object.keys(obj).forEach(function(k) {
					    	
					        if(k === '<%=SystemParametersConstant.TEST_RAP_IN + SystemParametersConstant.RAP_IN%>') {
					        	$("#test-rap-in").val(obj[k]);
					          
					        }
					        else if (k === '<%=SystemParametersConstant.TEST_RAP_OUT + SystemParametersConstant.RAP_OUT%>') {
					        	$("#test-rap-out").val(obj[k]);
					        	
					        }else if (k === '<%=SystemParametersConstant.TEST_TAP_IN + SystemParametersConstant.TAP_IN%>') {
					        	$("#test-tap-in").val(obj[k]);
					        	
							}else if (k === '<%=SystemParametersConstant.TEST_TAP_OUT + SystemParametersConstant.TAP_OUT%>') {
								$("#test-tap-out").val(obj[k]);
								
							}else if (k === '<%=SystemParametersConstant.COMMERICAL_NRTRDE_IN + SystemParametersConstant.NRTRDE_IN%>') {
								$("#nrtrdeIn").val(obj[k]);
								
							}else if (k === '<%=SystemParametersConstant.COMMERICAL_RAP_IN + SystemParametersConstant.RAP_IN%>') {
								$("#commercail-rap-in").val(obj[k]);
								
							}else if (k === '<%=SystemParametersConstant.COMMERICAL_RAP_OUT + SystemParametersConstant.RAP_OUT%>') {
								$("#commercial-rap-out").val(obj[k]);
								
							}else if (k === '<%=SystemParametersConstant.COMMERICAL_TAP_IN + SystemParametersConstant.TAP_IN%>') {
								$("#commercial-tap-in").val(obj[k]);
								
							}else if (k === '<%=SystemParametersConstant.COMMERICAL_TAP_OUT + SystemParametersConstant.TAP_OUT%>') {
								$("#commercial-tap-out").val(obj[k]);
								
							}else{
								$("#nrtrdeOut").val(obj[k]);
								
							}
					        displaySequencyNumber();
					        
					    });
					   
					}
				}

				
			}else if(responseObject != undefined && responseObject != 'undefined' && responseCode == "400"){
				console.log("got 400 response.");
			}
			
		},
	    error: function (xhr,st,err){	    		    	
			handleGenericError(xhr,st,err);
		}
	});

	
}
	
	
	
function searchTestSimManagement(){
	$('#outbound-services').multiselect("clearSelection");
	$('#inbound-services').multiselect("clearSelection");
	
	
	
	var partner = document.getElementById("partner").value
	var lob = document.getElementById("lob").value
	$.ajax({
		url: '<%=ControllerConstants.VIEW_TEST_SIM_MANAGEMENT%>',
    	data:
    		{
    		   'partner':partner,
			   'lob':lob,
			   
    		},
		dataType: 'json',
		type: "POST",
		success: function(data){	
			var response = eval(data);			
			var responseCode = response.code; 
			var responseMsg = response.msg; 
			var responseObject = response.object;			
			if(responseCode == "200"){
				var testSim=eval(responseObject);				
				var testSimList=testSim['testsimList'];
				
				if(testSimList.length == 0){
					$("#inbound-pmncode").val('');
					$("#inbound-imsi").val('');
					$("#inbound-msisdn").val('');
					$("#inbound-fromdate").val('');
					$("#inbound-todate").val('');
					$("#inbound-services").val('');
					$("#outbound-pmncode").val('');
					$("#outbound-imsi").val('');
					$("#outbound-msisdn").val('');
					$("#outbound-fromdate").val('');
					$("#outbound-todate").val('');
					$("#outbound-services").val('');
					
				}	
				else{
					for(var i = 0; i < testSimList.length; i++) {
						
					    var obj = testSimList[i];
					    Object.keys(obj).forEach(function(k) {
					    	
					    
					        if(k === '<%=SystemParametersConstant.INBOUND_PMNCODE%>') {
					        	$("#inbound-pmncode").val(obj.inboundPmncode);
					        }
					        else if (k === '<%=SystemParametersConstant.INBOUND_IMSI%>') {
					        	$("#inbound-imsi").val(obj.inboundImsi);
					        	
					        }else if (k === '<%=SystemParametersConstant.INBOUND_MSISDN%>') {
					        	$("#inbound-msisdn").val(obj.inboundMsisdn);
					        	
							}else if (k === '<%=SystemParametersConstant.INBOUND_FROMDATE%>') {
								
								$("#inbound-fromdate").val(obj.inboundFromDate);
								
							}else if (k === '<%=SystemParametersConstant.INBOUND_TODATE%>') {
								$("#inbound-todate").val(obj.inboundTodate);	
							}else if (k === '<%=SystemParametersConstant.INBOUND_SERVICES%>') {
								setValueInMultiSelect(obj.inboundServices, '#inbound-services');	
							}else if (k === '<%=SystemParametersConstant.OUTBOUND_PMNCODE%>') {
								
								$("#outbound-pmncode").val(obj.outboundPmncode);
							}else if (k === '<%=SystemParametersConstant.OUTBOUND_IMSI%>') {
								$("#outbound-imsi").val(obj.outboundImsi);
							}else if (k === '<%=SystemParametersConstant.OUTBOUND_MSISDN%>') {
								$("#outbound-msisdn").val(obj.outboundMsisdn);
							}else if (k === '<%=SystemParametersConstant.OUTBOUND_FROMDATE%>') {
								$("#outbound-fromdate").val(obj.outboundFromDate);
							}else if (k === '<%=SystemParametersConstant.OUTBOUND_TODATE%>') {
								$("#outbound-todate").val(obj.outboundTodate);	
							}else{
								setValueInMultiSelect(obj.outboundServices, '#outbound-services');	
							}
					        
					    });
					   
					}
				}

				
			}else if(responseObject != undefined && responseObject != 'undefined' && responseCode == "400"){
				console.log("got 400 response.");
			}
		},
	    error: function (xhr,st,err){	    		    	
			handleGenericError(xhr,st,err);
		}
	});
		
	
}
function searchRoamingFileManagement(){
	var partner = document.getElementById("partner").value
	var lob = document.getElementById("lob").value
	$.ajax({
		url: '<%=ControllerConstants.VIEW_FILE_MANAGEMENT%>',
    	data:
    		{
    		   'partner':partner,
			   'lob':lob,
			   
    		},
		dataType: 'json',
		type: "POST",
		success: function(data){	
			var response = eval(data);			
			var responseCode = response.code; 
			var responseMsg = response.msg; 
			var responseObject = response.object;			
			if(responseCode == "200"){
				var fileManagement=eval(responseObject);				
				var fileManagementList=fileManagement['fileManagement'];
				
				if(fileManagementList.length == 0){
					$("#testMaxRecordsInTapOut_id").val('');
					$("#testMaxFileSizeOfTapOut_id").val('');
					$("#testMaxRecordsInNrtrdeOut_id").val('');
					$("#testMaxfileSizeOfnrtrdeOut_id").val('');
					$("#commercialMaxRecordsInTapOut_id").val('');
					$("#commercialMaxFileSizeOfTapOut_id").val('');
					$("#commercialMaxRecordsInNrtrdeOut_id").val('');
					$("#commercialMaxfileSizeOfnrtrdeOut_id").val('');	
					$("#testRegeneratedTapOutFileSequence_id").val('');
					$("#testFileValidation_id").val('');
					$("#testSendTapNotification_id").val('');
					$("#testRapInVersion_id").val('');
					$("#testRapOutVersion_id").val('');
					$("#testSendNrtrde_id").val('');
					$("#testNrtrdeInVersion_id").val('');
					$("#testNrtrdeOutVersion_id").val('');
					$("#commercialTapInVersion_id").val('');
					$("#commercialTapOutVersion_id").val('');
					$("#commercialRegeneratedTapOutFileSequence_id").val('');
					$("#commercialFileValidation_id").val('');
					$("#commercialSendTapNotification_id").val('');
					$("#commercialRapInVersion_id").val('');
					$("#commercialRapOutVersion_id").val('');
					$("#commercialSendNrtrde_id").val('');
					$("#commercialNrtrdeInVersion_id").val('');
					$("#commercialNrtrdeOutVersion_id").val('');
					$("#testTapInVersion_id").val('');
					$("#testTapOutVersion_id").val('');
					
				}	
				else{
					for(var i = 0; i < fileManagementList.length; i++) {
						
					    var obj = fileManagementList[i];
					    Object.keys(obj).forEach(function(k) {
					    	
					    	if(k === '<%=SystemParametersConstant.TEST_TAPIN_VERSION%>'){
					    		
					    		$("#testTapInVersion_id").val(obj.testTapInVersion);
					    	}else if (k === '<%=SystemParametersConstant.TEST_TAPOUT_VERSION%>') {
					    		$("#testTapOutVersion_id").val(obj.testTapOutVersion);
							}else if(k === '<%=SystemParametersConstant.TEST_MAX_RECORDS_TAPOUT%>'){
								$("#testMaxRecordsInTapOut_id").val(obj.testMaxRecordsInTapOut);
							}
							else if(k === '<%=SystemParametersConstant.TEST_MAXFILE_TAPOUT%>'){
								
								$("#testMaxFileSizeOfTapOut_id").val(obj.testMaxFileSizeOfTapOut);
							}
							else if(k === '<%=SystemParametersConstant.TEST_SEND_NOTIFICATION%>'){
								$("#testSendTapNotification_id").val(obj.testSendTapNotification);
							}
							else if(k === '<%=SystemParametersConstant.TEST_GENERAT_FILESEQUENCE%>'){
								$("#testRegeneratedTapOutFileSequence_id").val(obj.testRegeneratedTapOutFileSequence);
							}
							else if(k === '<%=SystemParametersConstant.TEST_FILE_VALIDATION%>'){
								$("#testFileValidation_id").val(obj.testFileValidation);
							}
							else if(k === '<%=SystemParametersConstant.TEST_RAPIN_VERSION%>'){
								$("#testRapInVersion_id").val(obj.testRapInVersion);
							}
							else if(k === '<%=SystemParametersConstant.TEST_RAPOUT_VERSION%>'){
								$("#testRapOutVersion_id").val(obj.testRapOutVersion);
							}else if(k === '<%=SystemParametersConstant.TEST_SENDNRTRDE%>'){
								$("#testSendNrtrde_id").val(obj.testSendNrtrde);
							}
					    	
							else if(k === '<%=SystemParametersConstant.TEST_NRTRDE_IN_VERSION%>'){
								$("#testNrtrdeInVersion_id").val(obj.testNrtrdeInVersion);
							}
							else if(k === '<%=SystemParametersConstant.TEST_NRTRDE_OUT_VERSION%>'){
								$("#testNrtrdeOutVersion_id").val(obj.testNrtrdeOutVersion);
							}
							else if(k === '<%=SystemParametersConstant.TEST_MAX_RECORD_NRTRDE%>'){
								
								$("#testMaxRecordsInNrtrdeOut_id").val(obj.testMaxRecordsInNrtrdeOut);
							}
							else if(k === '<%=SystemParametersConstant.TEST_MAX_FILESIZE_NRTRDE%>'){
								
								$("#testMaxfileSizeOfnrtrdeOut_id").val(obj.testMaxfileSizeOfnrtrdeOut);
							}
							else if(k === '<%=SystemParametersConstant.COMMERCIAL_TAPIN_VERSION%>'){
					    		$("#commercialTapInVersion_id").val(obj.commercialTapInVersion);
					    	}else if (k === '<%=SystemParametersConstant.COMMERCIAL_TAPOUT_VERSION%>') {
					    		$("#commercialTapOutVersion_id").val(obj.commercialTapOutVersion);
							}else if(k === '<%=SystemParametersConstant.COMMERCIAL_MAX_RECORDS_TAPOUT%>'){
								
								$("#commercialMaxRecordsInTapOut_id").val(obj.commercialMaxRecordsInTapOut);
							}
							else if(k === '<%=SystemParametersConstant.COMMERCIAL_MAX_FILESIZE_TAPOUT%>'){
								$("#commercialMaxFileSizeOfTapOut_id").val(obj.commercialMaxFileSizeOfTapOut);
							}
							else if(k === '<%=SystemParametersConstant.COMMERCIAL_TAP_NOTIFICATION%>'){
								$("#commercialSendTapNotification_id").val(obj.commercialSendTapNotification);
							}
							else if(k === '<%=SystemParametersConstant.COMMERCIAL_GENERATE_FILESEQUENCE%>'){
								$("#commercialRegeneratedTapOutFileSequence_id").val(obj.commercialRegeneratedTapOutFileSequence);
							}
							else if(k === '<%=SystemParametersConstant.COMMERCIAL_FILE_VALIDATION%>'){
								$("#commercialFileValidation_id").val(obj.commercialFileValidation);
							}
							else if(k === '<%=SystemParametersConstant.COMMERCIAL_RAPIN_VERSION%>'){
								$("#commercialRapInVersion_id").val(obj.commercialRapInVersion);
							}
							else if(k === '<%=SystemParametersConstant.COMMERCIAL_RAPOUT_VERSION%>'){
								$("#commercialRapOutVersion_id").val(obj.commercialRapOutVersion);
							}else if(k === '<%=SystemParametersConstant.COMMERCIAL_SEND_NRTRDE%>'){
								$("#commercialSendNrtrde_id").val(obj.commercialSendNrtrde);
							}
					    	
							else if(k === '<%=SystemParametersConstant.COMMERCIAL_NRTRDE_IN_VERSION%>'){
								$("#commercialNrtrdeInVersion_id").val(obj.commercialNrtrdeInVersion);
							}
							else if(k === '<%=SystemParametersConstant.COMMERCIAL_NRTRDE_OUT_VERSION%>'){
								$("#commercialNrtrdeOutVersion_id").val(obj.commercialNrtrdeOutVersion);
							}
							else if(k === '<%=SystemParametersConstant.COMMERCIAL_MAX_RECORDS%>'){
								$("#commercialMaxRecordsInNrtrdeOut_id").val(obj.commercialMaxRecordsInNrtrdeOut);
							}
							else if(k === '<%=SystemParametersConstant.COMMERCIAL_MAX_FILESIZE_NRTRDE%>'){
								$("#commercialMaxfileSizeOfnrtrdeOut_id").val(obj.commercialMaxfileSizeOfnrtrdeOut);
							}
							
					        
					    });
					   
					}
				}

				
			}else if(responseObject != undefined && responseObject != 'undefined' && responseCode == "400"){
				console.log("got 400 response.");
			}
		},
	    error: function (xhr,st,err){	    		    	
			handleGenericError(xhr,st,err);
		}
	});
}	
function closeFancyBoxFromChildIFrame() {
	$.fancybox.close();
	window.location.reload();
}


function getPartnerListBySearchParams(urlAction) {
	var instanceid = '${instanceId}';
	var partner = document.getElementById("partner").value;
	$("#partnerlistgrid").jqGrid({
		url: '<%=ControllerConstants.GET_PARTNER_LIST_BY_NAME%>',
		datatype: "json",
		postData : {
			'partner' : partner
			
		},
		colNames: [
					  "partner id",
		           	  "Partner Name",
		           	  "LOB",
		           	  "Primary TADIG",
		           	  "Secondary TADIG"
			],
		colModel: [
			{name:'id',index:'id',sortable:true, hidden:true},
			{name:'partner',index:'partner',  sortable:true},
			{name:'lob',index:'lob',  sortable:true},
			{name:'primaryTadig',index:'primaryTadig',sortable:true},
			{name:'secondaryTadig',index:'secondaryTadig',sortable:true},
		],
		rowNum : 15,
		rowList : [ 10, 20, 60, 100 ],
		height : 'auto',
		mtype : 'POST',
		contentType : "application/json; charset=utf-8",
		loadtext : "Loading...",
		sortname : 'id',
		sortorder : "desc",
		pager: "#partnerGridPagingDiv",
		viewrecords: true,
		multiselect: true,
		timeout : 120000,
		 beforeSelectRow: function(rowid, e)
		{
		    $("#partnerlistgrid").jqGrid('resetSelection');
		    return (true);
		},
		beforeRequest : function() {
		    $('input[id=cb_partnerlistgrid]', 'div[id=jqgh_partnerlistgrid_cb]').remove();
		},
		 loadComplete : function () {
			var rowId = jQuery('#partnerlistgrid tr:eq(0)').attr('id');
			var dataFromTheRow = jQuery('#partnerlistgrid').jqGrid ('getRowData', rowId);
			
			$('input[id^="jqg_partnerlistgrid_"]').each(function(index){
				var lob = dataFromTheRow[index].lob;
				var deviceID = dataFromTheRow[index].id;
				$(this).attr("id",deviceID + "_" + lob + "_chkbox");
				$(this).attr("type","radio");
				$(this).attr("lob","radio_grp");
			});
		},
		onSelectRow : function(id){
			rowData = $("#partnerlistgrid").jqGrid("getRowData", id);
			console.log()
			
		},
		
	    loadtext: "Loading...",
		loadError : function(xhr,st,err) {
			handleGenericError(xhr,st,err);
		},
		recordtext: "<spring:message code="serverManagement.grid.pager.total.records.text"></spring:message>",
	    emptyrecords: "No Record Found.",
	    viewrecords: true,
		}).navGrid("#partnerGridPagingDiv",{edit:false,add:false,del:false,search:false});
		
	
		$(".ui-jqgrid-titlebar").hide();
		$(".ui-pg-input").height("10px");

		if ($("#partnerlistgrid").getGridParam("reccount") === 0) {
			  $(".ui-paging-info").html("No Record Found.");
		}  
		
}
function onSelectPartner(){
	var counter = $('#lblCounter').text();
	  selectPartner(counter);
	  window.location.refresh();
}


function selectPartner(){
	if(rowData){
		//$("#"+pathListCounter + "_partnerId").val(rowData.id);
		$("#partner").val(rowData.partner);
		$("#lob").val(rowData.lob);
		$("#primaryTadig").val(rowData.primaryTadig);
		$("#secondaryTadig").val(rowData.secondaryTadig);
	}else{
		$("#"+pathListCounter + "_partnerId").val("");
		$("#"+pathListCounter + "_parentDevice").val("");
	}
	
	searchRapTapConfigurationDetails();
	closeFancyBox();
}
function selectPartnerPopUp(pathListCounter){
	
	getPartnerListBySearchParams('<%=ControllerConstants.GET_PARTNER_LIST_BY_NAME%>');
	$("#lblCounter").text(pathListCounter);
	$('input[id$="_chkbox"]').each(function(index){
		$(this).prop("checked",false);
	});
	
	
	if($("#"+pathListCounter + "_partnerId").val()){
		jQuery("#partnerlistgrid").resetSelection();
		$('#partnerlistgrid tr').each(function() {
		    $.each(this.cells, function(){
		    	
		       if($(this).attr("aria-describedby") && $(this).attr("aria-describedby") == 'partnerlistgrid_partnerId'){
		    	   $("#"+pathListCounter + "_partnerId").val();
		    	   if($(this).text() == $("#"+pathListCounter + "_partnerId").val()){
		    		   var lob = $(this).next().text();
		    		   console.log("lob:::" + lob);
		    		   $("[id='"+lob+"_chkbox']").prop('checked', true);
		    		   jQuery("#partnerlistgrid").setSelection($(this).parent()[0].id, true);
		    		   return;
		    	   }
		       }
		    });
		});
	}
	$("#partnerList").click();
}	
</script>
</head>
</html>
