<%@page import="com.elitecore.sm.common.model.TriggerTypeEnum"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="com.elitecore.sm.common.constants.BaseConstants"%>
<%@page import="com.elitecore.sm.common.constants.FormBeanConstants"%>
<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<%@page import="com.elitecore.sm.common.model.FirstLastEnum"%>
<%@taglib 	uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="elitecore" %> 
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="/WEB-INF/ShowErrorTag.tld" prefix="elitecoreError"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<div class="modal-content">
			<div class="modal-header padding10">
				<h4 class="modal-title">
					<span id="add_label" style="display: none;"><spring:message
							code="trigger.mgmt.add.title" ></spring:message></span>
					<span id="update_label" style="display: none;"><spring:message
							code="trigger.mgmt.update.title" ></spring:message></span>
				</h4>
			</div>
			<form:form modelAttribute="<%=FormBeanConstants.TRIGGER_FORM_BEAN%>" action="<%=ControllerConstants.CREATE_TRIGGER_CONFIG%>"
				id="trigger_from_bean">
			<div class="modal-body padding10 inline-form">
				
				<div id="AddPopUpMsg">
					<jsp:include page="../common/responseMsgPopUp.jsp" ></jsp:include>
				</div>			
				
				<form:input path="ID"  style="display: none;"
						cssClass="form-control table-cell input-sm" tabindex="4"
						id="id" data-toggle="tooltip" data-placement="bottom"></form:input>
				<div class="form-group">
				<div class="col-md-6 no-padding" style="margin-top:10px">
					<div class="form-group">
						<spring:message code="trigger.mgmt.jqgrid.trigger.name"
							var="tooltip" ></spring:message>
						<spring:message code="trigger.mgmt.jqgrid.trigger.name"
							var="label" ></spring:message>
						<div class="table-cell-label">${label}<span class="required">*</span></div>
						<div class="input-group">
							<form:input path="triggerName"
								cssClass="form-control table-cell input-sm" tabindex="4"
								id="triggerName" data-toggle="tooltip" data-placement="bottom"
								title="${tooltip}" autocomplete="off"></form:input>
							<spring:bind path="triggerName">
								<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
							</spring:bind>
						</div>
					</div>
				</div>
				
				<div class="col-md-6 no-padding" style="margin-top:10px">
					<div class="form-group">
						<spring:message code="trigger.mgmt.jqgrid.trigger.description"
							var="tooltip" ></spring:message>
						<spring:message code="trigger.mgmt.jqgrid.trigger.description"
							var="label" ></spring:message>
						<div class="table-cell-label">${label}</div>
						<div class="input-group">
							<form:input path="description"
								cssClass="form-control table-cell input-sm" tabindex="4"
								id="description" data-toggle="tooltip" data-placement="bottom"
								title="${tooltip }" autocomplete="off"></form:input>
							<spring:bind path="description">
								<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
							</spring:bind>
						</div>
					</div>
				</div>
				</div>
				<hr>
				
				
				
			<!-- Start Recurrence Pattern-->
			<div class="form-group">
				<div class="col-md-12 no-padding">
					<div class="form-group">
						<spring:message code="trigger.mgmt.recurrence.pattern" var="label" ></spring:message>
						<label class="table-cell-label">${label}</label>
					</div>
				</div>
				<div class="col-md-2 no-padding">
					<div class="form-group">
						<div id="rpMenu1">
							<c:set var="totalTriggerTypes"
								value="${fn:length(triggerTypeEnum)}" ></c:set>
							<c:forEach var="triggerTypeEnum" items="${triggerTypeEnum}"
								varStatus="triggerTypeCounter">
								<c:if test="${triggerTypeEnum eq 'Minute'}">
									<input id="${triggerTypeEnum}" type="radio" checked="checked"
										value="${triggerTypeEnum}" name="triggerTypeGroup"
										onclick=viewRcrPtrBasedOnType(this.id)>&nbsp;&nbsp;<label>${triggerTypeEnum}</label>
									<br>
								</c:if>
								<c:if
									test="${triggerTypeEnum ne 'SELECT SCHEDULER TYPE' && triggerTypeEnum ne 'Minute' }">
									<input id="${triggerTypeEnum}" type="radio"
										value="${triggerTypeEnum}" name="triggerTypeGroup"
										onclick=viewRcrPtrBasedOnType(this.id)>&nbsp;&nbsp;<label>${triggerTypeEnum}</label>
									<br>
								</c:if>
							</c:forEach>
						</div>
					</div>
				</div>
				<div class="col-md-7 no-padding" style='display:inline-block;'>
					<div class="form-group">
						<div class="vl"></div>
						<div id="rpMenu2"></div>
					</div>
				</div>
				<div class="col-md-3 no-padding">
					<div class="form-group">
						<div class="vl"></div>
						<div id="rpMenu3" style="float:left;"></div>
					</div>
				</div>
			</div>
			<!-- End Recurrence Pattern-->
				
			<hr>

			<!-- Start Range of Recurrence -->
			<div class="form-group">
				<div class="col-md-12 no-padding">
					<div class="form-group">
						<spring:message code="trigger.mgmt.recurrence.range" var="label" ></spring:message>
						<label class="table-cell-label">${label}</label>
					</div>
				</div>
				<div class="col-md-6 no-padding">
					<div class="form-group">
						<div class="col-md-4 no-padding">
							<div class="form-group">
								<spring:message code="trigger.mgmt.execution.start" var="label" ></spring:message>
								<label style="margin-top:5px">${label}<span class="required">*</span></label>
							</div>
						</div>
						<div class="col-md-8 no-padding">
							<div class="input-group input-append date" id="startDatePicker">
								<input data-original-title="" id="startAtDate" readonly=""
									class="form-control table-cell input-sm"
									style="background-color: #ffffff" title=""
									data-toggle="tooltip" data-placement="bottom" tabindex="6">
								<span class="input-group-addon add-on bottom"> <i
									data-original-title="" id="startAtDate_error"
									class="glyphicon glyphicon-alert" data-toggle="tooltip"
									data-placement="left" title=""></i></span>
							</div>
						</div>
						<div class="col-md-4 no-padding"></div>
						<div class="col-md-8 no-padding" style="margin-top:10px">
							<div class="form-group">
								<div class="col-md-6 no-padding">
									<div id="startAthours"></div>
								</div>
								<div class="col-md-6 no-padding">	
									<div id="startAtMinutes"></div>
								</div>	
							</div>
						</div>
					</div>
				</div>
				<div class="col-md-6">
					<div class="form-group">
						<input id="noenddate" class="paddingleft" type="radio" checked="checked"
							onclick="disableNoEndDateRadioButtonField()" name="endType">&nbsp;&nbsp;
						<spring:message code="trigger.mgmt.recurrence.range.noenddate"
							var="label" ></spring:message>
						<label>${label}</label>
					</div>
				</div>

				<div class="col-md-6"></div>
				<div class="col-md-6">
					<div class="col-md-4 no-padding">
						<input id="endby" class="paddingleft" type="radio"
							onclick="disableEndByRadioButtonField()" name="endType">&nbsp;&nbsp;
						<spring:message code="trigger.mgmt.recurrence.range.endby"
							var="label" ></spring:message>
						<label>${label}</label>
					</div>
					<div class="col-md-8 no-padding">
						<div class="input-group input-append date" id="endDatePicker">
							<input data-original-title="" id="endAtDate" readonly="" class="form-control table-cell input-sm" 
								style="background-color: #ffffff" title="" data-toggle="tooltip" 
								data-placement="bottom" tabindex="6"></br>
							<span class="input-group-addon add-on bottom"> <i data-original-title="" id="endAtDate_error" 
								class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="left" title=""></i></span> 
						</div>
					</div>
					<div class="col-md-4 no-padding"></div>
					<div class="col-md-8 no-padding" style="margin-top:10px">
						<div class="form-group">
							<div class="col-md-6 no-padding">
								<div id="endAthours"></div>
							</div>
							<div class="col-md-6 no-padding">
								<div id="endAtMinutes"></div>
							</div>							
						</div>
					</div>
				</div>
			</div>
			<!-- End Range of Recurrence -->

		</div>
	</form:form>
	<div class="modal-footer padding10">
		<button type="button" id="addTrigger" class="btn btn-grey btn-xs "
			style="display: none"
			onclick="validateRangeOfRecurrence('<%=ControllerConstants.CREATE_TRIGGER_CONFIG%>');">
			<spring:message code="btn.label.add" ></spring:message>
		</button>
		<button type="button" id="updateTrigger" class="btn btn-grey btn-xs "
			style="display: none"
			onclick="validateRangeOfRecurrence('<%=ControllerConstants.UPDATE_TRIGGER_CONFIG%>');">
			<spring:message code="btn.label.update" ></spring:message>
		</button>
		<button id='close_btn' type="button" class="btn btn-grey btn-xs "
			data-dismiss="modal" onclick="closeFancyBox();reloadTableGridData();">
			<spring:message code="btn.label.close" ></spring:message>
		</button>
	</div>
</div>
<script>
var executeAt="";
var hours="";
var until="";
var totalhours = 24;
var totalminutes=60;
function recurrenceMenu3(label1,label2){
	executeAt ="<div class='cleardisplay'>"
	+"<label style='margin-left:20px;margin-top:20px'><spring:message code='trigger.mgmt.recurrence.pattern.executeat'></spring:message></label>";
	until ="<div class='cleardisplay'>"
	+"<label style='margin-left:20px;margin-top:20px;padding-right:35px'><spring:message code='trigger.mgmt.recurrence.pattern.until'></spring:message></label>";
	hoursOrMinutesDropdown("executionStartingHour",label1,totalhours,"10px");
	executeAt +=hours;
	hoursOrMinutesDropdown("executionEndingHour",label1,totalhours,"10px");
	until +=hours;
	//hoursOrMinutesDropdown("executionStartingMinute",label2,totalminutes,"10px");
	//executeAt +=hours;
	//hoursOrMinutesDropdown("executionEndingMinute",label2,totalhours,"10px");
	//until +=hours;
	executeAt +="</div>";
	until +="</div>";
}

function rangeOfRecurrence(){
	hoursOrMinutesDropdown("startAtHour","hour(s)",totalhours,"5px");
	$('#startAthours').append(hours);
	hoursOrMinutesDropdown("startAtMinute","minute(s)",totalminutes,"5px");
	$('#startAtMinutes').append(hours);
	hoursOrMinutesDropdown("endAtHour","hour(s)",totalhours,"5px");
	$('#endAthours').append(hours);
	hoursOrMinutesDropdown("endAtMinute","minute(s)",totalminutes,"5px");
	$('#endAtMinutes').append(hours);
}

function hoursOrMinutesDropdown(id,label,size,marginleft){
	hours = "<div id='time' class='displayinlineblock' style='margin-left:"+marginleft+"'>"
		+"<select name='"+id+"' class='dropdowncss' id='"+id+"' >";
	var hour="";
	for(var i=0;i<size;i++){
		if(i<10){
			hour="0"+i;
		}else{
			hour=i;
		}
		hours += "<option value='"+hour+"' id='"+i+"'>"+hour+"</option>";
	}
	if(label=="hour(s)"){
		hours += "</select><label style='margin-left:5px'>"+label+"<span class='required'>*</span></label></div>";		
	}else{
		hours += "</select><label style='margin-left:5px'>"+label+"<span class='required'>*</span></label></div>";	
	}
}

function viewRcrPtrBasedOnType(triggerTypeEnum){
	var blockforrpMenu2="";
	var blockforrpMenu3="";
	$('#rpMenu2').empty();
	$('#rpMenu3').empty();
	recurrenceMenu3("hour(s)","minute(s)");
	clearAllMessagesPopUp();
	if(triggerTypeEnum=='Minute'){
		
		blockforrpMenu2 += 	"<div class='cleardisplay' style='margin-left:10px;'><div class='form-group'><div class='col-md-3 no-padding'><label style='margin-left:25px;margin-top:5px;'><spring:message code='trigger.mgmt.recurrence.pattern.every'></spring:message></label></div>"
			+"<div class='col-md-6 no-padding'><div class='input-group'><input class='form-control table-cell input-sm' id='alterationCount' type='text' onkeypress='return isNumber(event)' autocomplete='off' value=''/><span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span></div></div>"
			+"<div class='col-md-3 no-padding'><label style='margin-left:10px;margin-top:5px;'><spring:message code='trigger.mgmt.recurrence.pattern.minutes'></spring:message><span class='required'>*</span></label></div></div></div>";
			
			
		blockforrpMenu3+=executeAt;
		blockforrpMenu3+=until;		
		
	} else if (triggerTypeEnum=='Hourly'){
		
		blockforrpMenu2 += 	"<div class='cleardisplay' style='margin-left:10px;'><div class='form-group'><div class='col-md-3 no-padding'><label style='margin-left:25px;margin-top:5px;'><spring:message code='trigger.mgmt.recurrence.pattern.every'></spring:message></label></div>"
			+"<div class='col-md-6 no-padding'><div class='input-group'><input class='form-control table-cell input-sm' id='alterationCount' type='text' onkeypress='return isNumber(event)' autocomplete='off'/><span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span></div></div>"
			+"<div class='col-md-3 no-padding'><label style='margin-left:10px;margin-top:5px;'><spring:message code='trigger.mgmt.recurrence.pattern.hours'></spring:message><span class='required'>*</span></label></div></div></div>";
			
		blockforrpMenu3+=executeAt;
		blockforrpMenu3+=until;	
		
	} else if(triggerTypeEnum=='Daily'){
		
		blockforrpMenu2 += 	"<div class='cleardisplay' style='margin-left:10px;'><div class='form-group'><div class='col-md-3 no-padding'><label style='margin-left:25px;margin-top:5px;'><spring:message code='trigger.mgmt.recurrence.pattern.every'></spring:message></label></div>"
			+"<div class='col-md-6 no-padding'><div class='input-group'><input class='form-control table-cell input-sm' id='alterationCount' type='text' onkeypress='return isNumber(event)' autocomplete='off'/><span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span></div></div>"
			+"<div class='col-md-3 no-padding'><label style='margin-left:10px;margin-top:5px;'><spring:message code='trigger.mgmt.recurrence.pattern.days'></spring:message><span class='required'>*</span></label></div></div></div>";
			
		blockforrpMenu3+=executeAt;
		
	} else if(triggerTypeEnum=='Weekly'){
		blockforrpMenu2 +=""
			
			+"<input id='alterationCount' type='hidden' value='1'/>"
			+"<c:set var='totalWeekEnums' value='${fn:length(weekEnum)}' ></c:set>"
			+"<div class='cleardisplay'>"
			+"<c:forEach var='weekEnum' items='${weekEnum}' varStatus='weekEnumCounter'>"
			+"<c:if test='${weekEnumCounter.count < 4 }'>"
			+"<input style='margin-left:25px;' type='checkbox' id='${weekEnum.name}' value='${weekEnum.name}' name='dayOfWeek' />&nbsp;${weekEnum}&nbsp; &nbsp;"
			+"</c:if>"
			+"</c:forEach>"
			+"</div>"
			+"<div class='cleardisplay'>"
			+"<c:forEach var='weekEnum' items='${weekEnum}' varStatus='weekEnumCounter'>"
			+"<c:if test='${weekEnumCounter.count >= 4 }'>"
			+"<input style='margin-left:25px;' type='checkbox' id='${weekEnum.name}' value='${weekEnum.name}' name='dayOfWeek' />&nbsp;${weekEnum}&nbsp; &nbsp;"
			+"</c:if>"
			+"</c:forEach>"
			+"</div>";
			
		blockforrpMenu3+=executeAt;
		
	} else if(triggerTypeEnum=='Monthly'){
		blockforrpMenu2 += "<div class='cleardisplay' style='margin-left:10px;'><div class='form-group'>"
			+"<div class='col-md-3 no-padding'><input id='day' type='radio' style='margin-left:20px;' checked='checked' name='group3' onclick='disableEveryRadioButtonField()'>&nbsp;"
			+"<label><spring:message code='trigger.mgmt.recurrence.pattern.day'></spring:message></label></div>"
			+"<div class='col-md-5 no-padding'><div class='input-group'><input class='form-control table-cell input-sm' id='dayOfMonth' type='text' onkeypress='return isNumber(event)' autocomplete='off'><span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span></div></div>"
			+"<div class='col-md-4 no-padding'><label style='margin-top:5px'><spring:message code='trigger.mgmt.recurrence.pattern.ofevery'></spring:message></label></div>"
			+"</div>"
			
			+"<div class='cleardisplay' style='margin-top:5px'><div class='form-group'>"
			+"<div class='col-md-3 no-padding'><input id='every' type='radio' style='margin-left:20px;' name='group3' onclick='disableDayRadioButtonField()'>&nbsp;"
			+"<label><spring:message code='trigger.mgmt.recurrence.pattern.every'></spring:message></label></div>"
			
			+"<div class='col-md-5 no-padding'><select name='firstOrLastDayOfMonth' disabled class='dropdowncss' id='firstOrLastDayOfMonth'>";
			
			<% for(FirstLastEnum firstLastEnum : FirstLastEnum.values()) { 
				 %>
				 blockforrpMenu2 += "<option value='<%=  firstLastEnum.getName() %>' id='<%=  firstLastEnum.getName() %>'><%= firstLastEnum.toString() %></option>";
				 <%
			     }
			%>
			
			blockforrpMenu2 +="</select></div>"
			
			+"<div class='col-md-4 no-padding'><label><spring:message code='trigger.mgmt.recurrence.pattern.dayofevery'></spring:message></label></div>"
			+"</div></div>"
			+"<div class='col-md-3 no-padding'></div>"
			+"<div class='col-md-5 no-padding'><div class='input-group'><input class='form-control table-cell input-sm' name='alterationCount' id='alterationCount' type='text' onkeypress='return isNumber(event)' autocomplete='off'><span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span></div></div>"
			+"<div class='col-md-4 no-padding'><label style='margin-top:5px'><spring:message code='trigger.mgmt.recurrence.pattern.months'></spring:message><span class='required'>*</span></label></div>"
			
			+"</div>";
			
		blockforrpMenu3+=executeAt;
		
	}
	$('#rpMenu2').append(blockforrpMenu2);  
	$('#rpMenu3').append(blockforrpMenu3);  
}

function disableEveryRadioButtonField(){
	$("#dayOfMonth").prop("readonly", false);
	$("#firstOrLastDayOfMonth").prop("disabled", true);
	$("#F").prop('selected', true);
	$("#alterationCount").val("");
}
function disableDayRadioButtonField(){
	$("#dayOfMonth").prop("readonly", true);
	$("#firstOrLastDayOfMonth").prop("disabled", false);
	$("#dayOfMonth").val("");
	$("#alterationCount").val("");
}
function disableNoEndDateRadioButtonField(){
	$("#endAtDate").prop("disabled", true);
	$("#endAtHour").prop("disabled", true);
	$("#endAtMinute").prop("disabled", true);
	$("#endAtDate").val(today);
	$("#endAtHour").val($("#"+dropdownDefaultValue).val());
	$("#endAtMinute").val($("#"+dropdownDefaultValue).val());
}
function disableEndByRadioButtonField(){
	$("#endAtDate").prop("disabled", false);
	$("#endAtHour").prop("disabled", false);
	$("#endAtMinute").prop("disabled", false);
}

function initializeFromAndToDate() {
    $("#startDatePicker").datepicker('setDate', new Date());
    $("#endDatePicker").datepicker('setDate', new Date()); 
}

function isNumber(evt) {
    evt = (evt) ? evt : window.event;
    var charCode = (evt.which) ? evt.which : evt.keyCode;
    if (charCode > 31 && (charCode < 48 || charCode > 57)) {
        return false;
    }
    return true;
}

function validateRangeOfRecurrence(url){
	var error = '';
	
	var startDate = Date.parse($("#startAtDate").val());
	var startTime = $("#startAtHour").val()+":"+$("#startAtMinute").val();
	var recurrenceType=$('input[name=triggerTypeGroup]:checked').val();
	var length = $('input[name="dayOfWeek"]:checked').length;
	if(length==0 && recurrenceType=="Weekly"){
		showErrorMsgPopUp("Select atleast one checkbox.");
		return false;
	}
	if($("#endby").prop("checked")){
		var endDate = Date.parse($("#endAtDate").val());
		var endTime = $("#endAtHour").val()+":"+$("#endAtMinute").val();
		
		if (startDate > endDate){
		    error = 'Start date should be less than End date.';
		    showErrorMsgPopUp(error);
		    return false;
		}
		else if(startDate == endDate && endTime <= startTime){
		    error = 'Start time should be less than End time.';
		    showErrorMsgPopUp(error);
		    return false;
		} else {
			saveTrigger(url);
		}
	}else{
		saveTrigger(url);
	}
}

function saveTrigger(url){
	clearAllMessages();
	clearAllMessagesPopUp();
	resetWarningDisplayPopUp();
	var id=$("#id").val();
	var triggerName=$("#triggerName").val().trim();
	var description=$("#description").val().trim();
	var recurrenceType=$('input[name=triggerTypeGroup]:checked').val();
	var executionStartingHour=$("#executionStartingHour").val();
	var executionStartingMinute=$("#executionStartingMinute").val();
	var executionEndingHour=$("#executionEndingHour").val();
	var executionEndingMinute=$("#executionEndingMinute").val();
	var startAtDate=$("#startAtDate").val();
	var startAtHour=$("#startAtHour").val();
	var startAtMinute=$("#startAtMinute").val();
	var endAtDate=null;
	var endAtHour=null;
	var endAtMinute=null;
	var dayOfMonth=null;
	var firstOrLastDayOfMonth=null;
	if($("#endby").prop("checked")){
		endAtDate=$("#endAtDate").val();
		endAtHour=$("#endAtHour").val();
		endAtMinute=$("#endAtMinute").val();
	}
	var alterationCount=$("#alterationCount").val();
	if($("#day").prop("checked")){
		dayOfMonth=$("#dayOfMonth").val();
	}else if($("#every").prop("checked")){
		firstOrLastDayOfMonth=$("#firstOrLastDayOfMonth").val();
	}
	var dayOfWeek="";
	var length = $('input[name="dayOfWeek"]:checked').length;
	$('input[name="dayOfWeek"]:checked').each(function(index) {
		if(index === (length - 1)){
			dayOfWeek+=this.value;
		}else{
			dayOfWeek+=this.value+",";
		}
	});
	
	
	$.ajax({
		url: url,
		cache: false,
		async: true,
		dataType:'json',
		type:"POST",
		data:  {
			"ID":id,
			"triggerName":triggerName,
			"description":description,
			"recurrenceType":recurrenceType,
			"executionStartingHour":executionStartingHour,
			"executionStartingMinute":executionStartingMinute,
			"executionEndingHour":executionEndingHour,
			"executionEndingMinute":executionEndingMinute,
			"startAtDate":startAtDate,
			"startAtHour":startAtHour,
			"startAtMinute":startAtMinute,
			"endAtDate":endAtDate,
			"endAtHour":endAtHour,
			"endAtMinute":endAtMinute,
			"alterationCount":alterationCount,
			"firstOrLastDayOfMonth":firstOrLastDayOfMonth,
			"dayOfMonth":dayOfMonth,
			"dayOfWeek":dayOfWeek,
		}, 
		success: function(data){
			var response = eval(data);
			var responseCode = response.code; 
			var responseMsg = response.msg; 
			var responseObject = response.object;
			clearAllMessagesPopUp();
			resetWarningDisplayPopUp();
			if(responseCode === "200"){
				closeFancyBox();
				closeFancyBoxFromChildIFrame();
				reloadTableGridData();
				showSuccessMsg(responseMsg);
			}else if(responseObject !== undefined && responseObject !== 'undefined' && responseCode === "400"){
				
				var modifiedJsonKey = modifyJsonKey(responseObject);
				
				addErrorIconAndMsgForAjaxPopUp( modifiedJsonKey );
				
			}else{
				showErrorMsgPopUp(responseMsg);
			}
		},
	    error: function (xhr,st,err){
			handleGenericError(xhr,st,err);
		}
	});
}

function modifyJsonKey( myJSONDatea ){
	
	$.each( myJSONDatea , function(key,val){				
				if(key!=null){				
			    	var keys =  key.replace("[","");
			    		keys =  keys.replace("]","");
					if(  keys !=  key){
					   	myJSONDatea[keys]=val;
					}		
				}
	});
	
	console.log( myJSONDatea );
	
	return myJSONDatea ;
}

</script>
<style>
.vl {
    border-left: 1px solid black;
    height: 140px;
    float:left;
}

.displayinlineblock{
	display: inline-block;
	margin-left : 2px;
}

.dropdowncss{
	height: 30px;
    font-size: 12px;
    line-height: 1.5;
    border-radius: 3px;
    display:table-cell;
    border-collapse: separate;
    background-color: white;
    text-align: center;
}

#cleardisplay{
 clear:both;
}
.custominput-sm{
	height: 30px;
	padding: 5px 10px;
    font-size: 12px;
    line-height: 1.5;
    border-radius: 3px;
    border: 1px solid #ccc;
    color: #555;
}
#endAtDate,#startAtDate{
	/* width : 80px; */
	/* border: 1px solid #ccc; */
}
#firstOrLastDayOfMonth{
	padding: 5px 7px;
}
#paddingleft{
	padding-left:35px;
}
hr {
    display: block;
    height: 1px;
    border: 0;
    border-top: 1px solid #ccc;
    margin: 1em 0;
    padding: 0;
}
</style>
