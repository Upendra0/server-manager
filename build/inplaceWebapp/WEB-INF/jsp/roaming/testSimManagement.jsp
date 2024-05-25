<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="com.elitecore.sm.common.constants.BaseConstants"%>
<%@page import="com.elitecore.sm.util.MapCache"%>
<%@page
	import="com.elitecore.sm.common.constants.SystemParametersConstant"%>
<%@page import="org.apache.catalina.connector.Request"%>
<%@page import="com.elitecore.sm.common.constants.EngineConstants"%>
<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<%@ taglib uri="http://www.springframework.org/security/tags"
	prefix="sec"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="/WEB-INF/ShowErrorTag.tld" prefix="elitecoreError"%>
<html>
<head>
<%--  <link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet" type="text/css" />
<script src="${pageContext.request.contextPath}/js/bootstrap.min.js" type="text/javascript"></script>  --%>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/bootstrap-multiselect.js"></script>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/bootstrap-multiselect.css"
	type="text/css" />
	
<script type="text/javascript">

$(document).ready(function() {
	initializeFromAndToDate();  
	
	$('#inbound-services').multiselect({
    	
	enableHTML : true,
	maxHeight: 400,
	dropDown: true,
	buttonWidth: '150px'
	})	;
	$('#outbound-services').multiselect({
		enableHTML : true,
		maxHeight: 400,
		dropDown: true,
		buttonWidth: '150px'
	});
	
	setValueInMultiSelect('${test_sim_management_data.inBoundServices}', '#inbound-services');
	
	setValueInMultiSelect('${test_sim_management_data.outBoundServices}', '#outbound-services');
});


function setValueInMultiSelect(value,id){
	
	if(value === null || value === '' || value === 'undefined '){
		value = ' ';
	}
	var element = $(id);
    var valArr = value.split(",");
   
    var i = 0, size = valArr.length;
    for (i=0; i < size; i++) {
    	
    	element.multiselect('select', valArr[i]);
    }
}
	
$("#partnerNameId").val(document.getElementById("partner").value);
$("#lobId").val(document.getElementById("lob").value);
$("#primaryTadigId").val(document.getElementById("primaryTadig").value);
$("#secondaryTadigId").val(document.getElementById("secondaryTadig").value);

	function resetTestSimFiledsForm() {
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
		resetWarningDisplay();
		
	}
	
	function saveOrUpdateTestSimFields(){
		

		getDuplicateCheckParamList("#inbound-services");
		var inboundservices = servicesValueList.join();
		getDuplicateCheckParamList("#outbound-services");
		var outboundservices = servicesValueList.join();
		
		$("#partnerNameId").val(document.getElementById("partner").value);
		$("#lobId").val(document.getElementById("lob").value);
		$("#primaryTadigId").val(document.getElementById("primaryTadig").value);
		$("#secondaryTadigId").val(document.getElementById("secondaryTadig").value);
	var inboundpmn = 	$("#inbound-pmncode").val();
	var inboundimsi=	$("#inbound-imsi").val();
	var inboundmsisdn=	$("#inbound-msisdn").val();
	var outboundomncode = $("#outbound-pmncode").val();
	var outboundimsi = 	$("#outbound-imsi").val();
	var outboundmsisdn = 	$("#outbound-msisdn").val();
	var inboundtoDate = $("#inbound-todate").val();
	var inboundFromDate = $("#inbound-fromdate").val();
	var outboundtoDate = $("#outbound-todate").val();
	var outboundFromDate = $("#outbound-fromdate").val();
	
	
	if(inboundpmn == '' || inboundpmn == null){
		$("#inbound-pmncode").val(0);
	}
	if(inboundimsi == '' || inboundimsi == null){
		$("#inbound-imsi").val(0);
	}
	if(inboundmsisdn == '' || inboundmsisdn == null){
		$("#inbound-msisdn").val(0);
	}
	if(outboundomncode == '' || outboundomncode == null){
		$("#outbound-pmncode").val(0);
	}
	if(outboundimsi == '' || outboundimsi == null){
		$("#outbound-imsi").val(0);
	}
	if(outboundmsisdn == '' || outboundmsisdn == null){
		$("#outbound-msisdn").val(0);
	}
	if(outboundservices == '' || outboundservices == null){
		$("#outbound-services").val("none");
	}
	if(inboundservices == '' || inboundservices == null){
		$("#inbound-services").val("none");
	}
	

	
	
		$("#save-test-sim-form").attr("action","<%=ControllerConstants.MODIFY_TEST_SIM_MANAGEMENT%>");
		
		
		$("#save-test-sim-form").submit();	
		
		
		//$("#outbound-fromdate").val()
		
	}
	
	
	var servicesValueList = [];

	 function getDuplicateCheckParamList(id){
		servicesValueList = [];
		$.each($(id).val(), function(i, item) {
			servicesValueList.push(item);
			
		});
		
		
	}
	
	function initializeFromAndToDate() {
		
	    
        $("#fromDatePicker").datepicker('setDate', $("#inbound-fromdate").val());
		
       $("#toDatePicker").datepicker('setDate', $("#inbound-todate").val()); 
       
       $("#outBoudFromDate").datepicker('setDate', $("#outbound-fromdate").val());
		
       $("#outBoundTodate").datepicker('setDate', $("#outbound-todate").val()); 
		
	}

function getMultiSelect(){
	
	$('#inbound-services').multiselect({
    	
		enableHTML : true,
		maxHeight: 400,
		dropDown: true,
		buttonWidth: '150px'
		})	;
		$('#outbound-services').multiselect({
			enableHTML : true,
			maxHeight: 400,
			dropDown: true,
			buttonWidth: '150px'
		});
		
		setValueInMultiSelect('${test_sim_management_data.inBoundServices}', '#inbound-services');
		setValueInMultiSelect('${test_sim_management_data.outBoundServices}', '#outbound-services');
}
	
	

</script>
	
	
<style>
.checkbox {
	color: #000 !important;
}

.btn .caret {
	margin-left: -10px;
}

.btn-group {
	width: 100% !important;
}

.btn-group button {
	width: 100% !important;
}

.btn-default span {
	width: 100% !important;
	background: none !important;
}

.multiselect-clear-filter {
	line-height: 1.9;
}

.ui-jqgrid .ui-jqgrid-bdiv {
	overflow-y: scroll;
	max-height: 270px;
}
.table-roaming-bordered {
    border: 1px solid #D3D3D3;
}
</style>

</head>
<div>
	<p></p>
</div>
<body class="skin-blue sidebar-mini sidebar-collapse">
	<div>
		<p></p>
	</div>
	<div class="fullwidtd inline-form"
		style="padding-left: 0px !important;">
		<div class="box-body padding0 mtop10 clearfix" id="host_Confg_Block">
			<div class="fullwidth mtop10">
				<form:form modelAttribute="test_sim_management_data" method="POST"
					action="addService" id="save-test-sim-form" enctype="multipart/form-data">
					<table class="table table-hover table-roaming-bordered "
						style="width: 100%;">
						<thead>
							<tr>

								<th id="grid_id" role="columnheader"
									class="ui-state-default ui-th-column ui-th-ltr"><strong><spring:message
											code="roaming.configuration.test.simanagement.subscriber.heading" ></spring:message></strong>
								</th>
								<th id="grid_name" role="columnheader"
									class="ui-state-default ui-th-column ui-th-ltr"><strong><spring:message
											code="roaming.configuration.test.simanagement.pmncode.heading" ></spring:message></strong>
								</th>
								<th id="grid_category" role="columnheader"
									class="ui-state-default ui-th-column ui-th-ltr"><strong><spring:message
											code="roaming.configuration.test.simanagement.imsi.heading" ></spring:message></strong>
								</th>
								<th id="grid_type" role="columnheader"
									class="ui-state-default ui-th-column ui-th-ltr"><strong><spring:message
											code="roaming.configuration.test.simanagement.msisdn.heading" ></spring:message></strong>
								</th>

								<th id="grid_is_enabled" role="columnheader"
									class="ui-state-default ui-th-column ui-th-ltr"><strong><spring:message
											code="roaming.configuration.test.simanagement.applicability.dates.heading" ></spring:message></strong>
								</th>
								<th id="grid_is_sync" role="columnheader"
									class="ui-state-default ui-th-column ui-th-ltr"><strong><spring:message
											code="roaming.configuration.test.simanagement.services.heading" ></spring:message></strong>
								</th>
							</tr>
						</thead>
						<tbody>
						
							<tr>
								<td><spring:message
										code="roaming.configuration.test.simanagement.subscriber.type.inbound" ></spring:message>
								</td>
								<td>
									<div class="form-group">
										<div class="input-group">
											<spring:message
												code="roaming.Configuration.test.sim.inbound.pmncode"
												var="tooltip" ></spring:message>
											<form:input path="inBoundPmnCode"
												onkeydown="isNumericOnKeyDown(event)"
												cssClass="form-control table-cell input-sm" tabindex="2"
												id="inbound-pmncode" maxlength="5" data-toggle="tooltip"
												data-placement="bottom" title="${tooltip}" ></form:input>
											<spring:bind path="inBoundPmnCode">
												<elitecoreError:showError
													errorMessage="${status.errorMessage}"
													errorId="inboundpmncode_error"></elitecoreError:showError>
											</spring:bind>
										</div>
									</div>
								</td>
								<td>
									<div class="form-group ">
										<div class="input-group">
											<spring:message
												code="roaming.Configuration.test.sim.inbound.imsi"
												var="tooltip" ></spring:message>
											<form:input path="inBoundImsi"
												onkeydown="isNumericOnKeyDown(event)"
												cssClass="form-control table-cell input-sm" tabindex="2"
												id="inbound-imsi" maxlength="16" data-toggle="tooltip"
												data-placement="bottom" title="${tooltip}" ></form:input>
											<spring:bind path="inBoundImsi">
												<elitecoreError:showError
													errorMessage="${status.errorMessage}"
													errorId="inboundimsi_error"></elitecoreError:showError>
											</spring:bind>
										</div>

									</div>
								</td>
								<td>
									<div class="form-group ">
										<div class="input-group">
											<spring:message
												code="roaming.Configuration.test.sim.inbound.msisdn"
												var="tooltip" ></spring:message>
											<form:input path="inBoundmsisdn"
												onkeydown="isNumericOnKeyDown(event)"
												cssClass="form-control table-cell input-sm" tabindex="2"
												id="inbound-msisdn" maxlength="16" data-toggle="tooltip"
												data-placement="bottom" title="${tooltip}" ></form:input>
											<spring:bind path="inBoundmsisdn">
												<elitecoreError:showError
													errorMessage="${status.errorMessage}"
													errorId="inboundmsisdn_error"></elitecoreError:showError>
											</spring:bind>
										</div>

									</div>
								</td>




								<td class="col-sm-4">
									<div class="row">
										<div class="col-sm-6">
											<spring:message
												code="roaming.Configuration.test.sim.applicability.date.from"
												var="name" ></spring:message>
											<spring:message
												code="roaming.Configuration.test.sim.applicability.date.from"
												var="tooltip" ></spring:message>
											<div class="col-sm-4">${name}</div>
											<div class="input-group input-append date col-sm-8"
												id="fromDatePicker">
												<form:input path="inBoundFromDate" id="inbound-fromdate"
													class="form-control table-cell input-sm"
													style="background-color: #ffffff" data-toggle="tooltip"
													data-placement="bottom" title="${tooltip}" ></form:input>
												<spring:bind path="inBoundFromDate">
													<elitecoreError:showError errorId="inboundfromdate_err"
														errorMessage="${status.errorMessage}"></elitecoreError:showError>
												</spring:bind>
											</div>

										</div>
										<div class="col-sm-6">
											<spring:message
												code="roaming.Configuration.test.sim.applicability.date.to"
												var="name" ></spring:message>
											<spring:message
												code="roaming.Configuration.test.sim.applicability.date.to"
												var="tooltip" ></spring:message>
											<div class="col-sm-4">${name}</div>

											<div class="input-group input-append date col-sm-8"
												id="toDatePicker">
												<form:input path="inBoundToDate" id="inbound-todate"
													cssClass="form-control table-cell input-sm"
													data-toggle="tooltip" data-placement="bottom"
													title="${tooltip }"  ></form:input>
												<spring:bind path="inBoundToDate">
													<elitecoreError:showError errorId="inboundtodate_err"
														errorMessage="${status.errorMessage}"></elitecoreError:showError>
												</spring:bind>
											</div>
										</div>
									</div>
								</td>
								<td class ="col-sm-3">
									<div >
										<div class="form-group">
											<div class="input-group" style="overflow: visible;">
												<form:select path="inBoundServices" id="inbound-services"
													name="inboundServicesList"
													class="form-control table-cell input-sm " tabindex="3"
													multiple="multiple" data-toggle="tooltip"
													data-placement="bottom" title="${tooltip}">
													<c:forEach var="servicesEnum" items="${servicesEnum}">
												<form:option value="${servicesEnum.name}">${servicesEnum}</form:option>
											</c:forEach>
												</form:select>


												<spring:bind path="inBoundServices">
													<elitecoreError:showError errorId="host_timezone_err"
														errorMessage="${status.errorMessage}"></elitecoreError:showError>
												</spring:bind>
											</div>
										</div>
									</div>
								</td>
							</tr>
							<tr>
								<td><spring:message
										code="roaming.configuration.test.simanagement.subscriber.type.outbound" ></spring:message>
								</td>
								<td>
									<div class="form-group">
										<div class="input-group">
											<spring:message
												code="roaming.Configuration.test.sim.outbound.pmncode"
												var="tooltip" ></spring:message>
											<form:input path="outBoundPmnCode"
												onkeydown="isNumericOnKeyDown(event)"
												cssClass="form-control table-cell input-sm" tabindex="2"
												id="outbound-pmncode" maxlength="5" data-toggle="tooltip"
												data-placement="bottom" title="${tooltip}" ></form:input>
											<spring:bind path="outBoundPmnCode">
												<elitecoreError:showError
													errorMessage="${status.errorMessage}"
													errorId="outboundpmncode_error"></elitecoreError:showError>
											</spring:bind>
										</div>
									</div>
								</td>
								<td><div class="form-group">
										<div class="input-group">
											<spring:message
												code="roaming.Configuration.test.sim.outbound.imsi"
												var="tooltip" ></spring:message>
											<form:input path="outBoundImsi"
												onkeydown="isNumericOnKeyDown(event)"
												cssClass="form-control table-cell input-sm" tabindex="2"
												id="outbound-imsi" maxlength="16" data-toggle="tooltip"
												data-placement="bottom" title="${tooltip}" ></form:input>
											<spring:bind path="outBoundImsi">
												<elitecoreError:showError
													errorMessage="${status.errorMessage}"
													errorId="outboundimsi_error"></elitecoreError:showError>
											</spring:bind>
										</div>

									</div></td>
								<td><div class="form-group">
										<div class="input-group">
											<spring:message
												code="roaming.Configuration.test.sim.outbound.msisdn"
												var="tooltip" ></spring:message>
											<form:input path="outBoundmsisdn"
												onkeydown="isNumericOnKeyDown(event)"
												cssClass="form-control table-cell input-sm" tabindex="2"
												id="outbound-msisdn" maxlength="16" data-toggle="tooltip"
												data-placement="bottom" title="${tooltip}"></form:input>
											<spring:bind path="outBoundmsisdn">
												<elitecoreError:showError
													errorMessage="${status.errorMessage}"
													errorId="outboundmsisdn_error"></elitecoreError:showError>
											</spring:bind>
										</div>

									</div></td>
								<td class="col-sm-4">
									<div class="row">
										<spring:message
											code="roaming.Configuration.test.sim.applicability.date.from"
											var="name" ></spring:message>
										<spring:message
											code="roaming.Configuration.test.sim.applicability.date.from"
											var="tooltip" ></spring:message>

										<div class="col-sm-6">
											<div class="col-sm-4">${name}</div>
											
											<div class="input-group input-append date col-sm-8" id="outBoudFromDate">
			                
			                <form:input path="outBoundFromDate" cssClass="form-control table-cell input-sm" tabindex="7" id="outbound-fromdate"  data-toggle="tooltip" data-placement="bottom" title="${tooltip }" ></form:input> 
			                <spring:bind path="outBoundFromDate">
								<elitecoreError:showError errorId="outboundfromdate_err" errorMessage="${status.errorMessage}"></elitecoreError:showError>
							</spring:bind>
			            </div>
			            										</div>

										<spring:message
											code="roaming.Configuration.test.sim.applicability.date.to"
											var="name" ></spring:message>
										<spring:message
											code="roaming.Configuration.test.sim.applicability.date.to"
											var="tooltip" ></spring:message>

										<div class="col-sm-6">
											<div class="col-sm-4">${name}</div>
											
											
											
											<div class="input-group input-append date col-sm-8" id="outBoundTodate">
			                
			                <form:input path="outBoundToDate" cssClass="form-control table-cell input-sm" tabindex="7" id="outbound-todate"  data-toggle="tooltip" data-placement="bottom" title="${tooltip }" ></form:input> 
			                <spring:bind path="outBoundToDate">
								<elitecoreError:showError errorId="outboundfromdate_err" errorMessage="${status.errorMessage}"></elitecoreError:showError>
							</spring:bind>
			            </div>

										</div>
									</div>

								</td>


								<td class ="col-sm-3">
									<div>
										<div class="form-group">
											<div class="input-group" style="overflow: visible;">
												<form:select path="outBoundServices" id="outbound-services"
													name="outboundServicesList"
													class="form-control table-cell input-sm " tabindex="3"
													multiple="multiple" data-toggle="tooltip"
													data-placement="bottom" title="${tooltip}">
													<c:forEach var="servicesEnum" items="${servicesEnum}">
												<form:option value="${servicesEnum.name}">${servicesEnum}</form:option>
											</c:forEach>
												</form:select>


												<spring:bind path="outBoundServices">
													<elitecoreError:showError errorId="host_timezone_err"
														errorMessage="${status.errorMessage}"></elitecoreError:showError>
												</spring:bind>
											</div>
										</div>
									</div>

								</td>
							</tr>
						</tbody>
					</table>
					<div class="clearfix"></div>

					<form:hidden path="partnerName" id="partnerNameId"
						value="${partner}" ></form:hidden>
					<form:hidden path="lob" id="lobId" value="${lob}" ></form:hidden>
<form:hidden path="secondaryTadig" id="secondaryTadigId"
						value="${secondaryTadig}" ></form:hidden>
					<form:hidden path="primaryTadig" id="primaryTadigId" value="${primaryTadig}" ></form:hidden>


				</form:form>
			</div>
		</div>
	</div>

	<footer class="main-footer positfix">
		<div class="fooinn">
			<div class="fullwidth">
				<div class="padleft-right" id="generalParameterBtnsDiv">
					<button class="btn btn-grey btn-xs " id="test-sim-save-btn"
						type="button" onclick="saveOrUpdateTestSimFields();">
						<spring:message code="btn.label.save" ></spring:message>
					</button>
					<button class="btn btn-grey btn-xs" id="test-sim-reset-btn"
						type="button" onclick="resetTestSimFiledsForm();">
						<spring:message code="btn.label.reset" ></spring:message>
					</button>
					<%-- <button class="btn btn-grey btn-xs"  id="host-conf-cancel-btn" type="button" onclick="cancelHostConfigurationForm();"><spring:message code="btn.label.cancel" ></spring:message></button> --%>
					<%-- <button class="btn btn-grey btn-xs"  id="edit-btn"  type="button" onclick="editSystemParam();"><spring:message code="btn.label.edit" ></spring:message></button> --%>
				</div>

			</div>
		</div>
		<jsp:include page="../common/newfooter.jsp"></jsp:include>
	</footer>



</body>






</html>
