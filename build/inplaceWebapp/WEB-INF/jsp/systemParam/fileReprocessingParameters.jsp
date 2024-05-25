<%@page import="com.elitecore.sm.common.constants.SystemParametersConstant"%>
<%@page import="com.elitecore.sm.common.constants.BaseConstants"%>
<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@taglib uri="/WEB-INF/ShowErrorTag.tld" prefix="error" %>

<%@ taglib tagdir="/WEB-INF/tags" prefix="elitecore" %>
<script>
$(document).ready(function() {
	var selectBox = document.getElementById("pwdTypeCombo");
	var selectedValue = selectBox.options[selectBox.selectedIndex].value;
	var selectedName = selectBox.options[selectBox.selectedIndex].text;
	var xyz='${pwdPolicyDescriptionDB}';
	$('#passwordType').val($('#pwdTypeCombo').val());
	console.log("value::" + selectedName);
	document.getElementById("GpasswordType").value=selectedName;
	document.getElementById("GpasswordPolicyNameCustom").value=xyz;
	
})
</script>
<div class="box-body padding0 mtop10">
	<div class="fullwidth mtop10">
		<form:form modelAttribute="system_param_bean" method="POST" action="<%= ControllerConstants.MODIFY_SYSTEM_PARAMETER %>" id="file-reprocessing-form">
			<div class="box box-warning">
				<div class="box-header with-border">
					<h3 class="box-title"><spring:message code="systemParameter.fileReprocessing.section.heading" ></spring:message></h3>
					<div class="box-tools pull-right" style="top:-3px;">
	                   	<button class="btn btn-box-tool" data-widget="collapse"><i class="fa fa-minus"></i></button>
	                   </div>
				</div>
				
				<div class="box-body">
					<div class="fullwidth inline-form">
						<input type="hidden" id=<%=BaseConstants.REQUEST_ACTION_TYPE%> name=<%=BaseConstants.REQUEST_ACTION_TYPE%> value=<%=SystemParametersConstant.EDIT_FILE_REPROCESSING_PARAM%> />
						<!-- <input type="hidden" id="GpasswordType" name="GpasswordType" />
						<input type="hidden" id="GpasswordPolicyNameCustom" name="GpasswordPolicyNameCustom" /> -->
						<c:set var="loopcount" value="0" scope="page" ></c:set>
						<c:forEach var="systemParam" items="${system_param_bean.fileReprocessingParamList}" varStatus="counter">
							<c:set var="loopcount" value="${loopcount + 1}" scope="page" ></c:set>
							<div class="col-md-2 mtop10">	
								<p id="fileReprocessingParamList[${counter.index}].name" >
									<spring:message code="system.parameter.${fn:toLowerCase(fn:replace(systemParam.name,' ', '.'))}.label" ></spring:message>
								</p>
							</div>
							<div class="col-md-4" style="padding-left:0;">
								<div class="form-group">
									<div class="input-group">
										<c:choose>
											<c:when test="${not empty systemParam.parameterDetail}">
												<form:select path="fileReprocessingParamList[${counter.index}].value" cssClass="form-control table-cell input-sm" 
															 data-toggle="tooltip" data-placement="bottom" title="${systemParam.name}"
															 id="fileReprocessingParamListValue[${counter.index}]">
														<form:options items="${systemParam.parameterDetail}"
															itemLabel="name" id="fileReprocessingParamListDetail[${counter.index}]"
															itemValue="value"></form:options>
												</form:select>
												<spring:bind path="fileReprocessingParamList[${counter.index}].value">
												   		<error:showError errorMessage="${status.errorMessage}"></error:showError>
												</spring:bind> 
											</c:when>
											<c:otherwise>
												<form:input path="fileReprocessingParamList[${counter.index}].value" cssClass="form-control table-cell input-sm"
														id="fileReprocessingParamListValue[${counter.index}]" data-toggle="tooltip" data-placement="bottom" title="${systemParam.name}"></form:input>
												   <spring:bind path="fileReprocessingParamList[${counter.index}].value">
												   		<error:showError errorMessage="${status.errorMessage}" errorId="fileReprocessingParamListValue[${counter.index}]_error"></error:showError>
												   </spring:bind>
			
												
											</c:otherwise>
										</c:choose>
									</div>
								</div>
							</div>
							<div class="col-md-6">
								 <p id="fileReprocessingParamList[${counter.index}].description">
									<spring:message code="system.parameter.${fn:toLowerCase(fn:replace(systemParam.name,' ', '.'))}.description" ></spring:message>
								 <p>
						 	</div>	
						 	<div class="clearfix"></div>
						 			
						 	<form:hidden path="fileReprocessingParamList[${counter.index}].name" id="fileReprocessingParamListParameterName[${counter.index}]" ></form:hidden> 
						 	<form:hidden path="fileReprocessingParamList[${counter.index}].description" id="fileReprocessingParamListDescription[${counter.index}]" ></form:hidden>
						 	<form:hidden path="fileReprocessingParamList[${counter.index}].id" id="fileReprocessingParamListParameterId[${counter.index}]" ></form:hidden> 
						 	<form:hidden path="fileReprocessingParamList[${counter.index}].alias" id="fileReprocessingParamListAliasParameterId[${counter.index}]" ></form:hidden> 
						 	<form:hidden path="fileReprocessingParamList[${counter.index}].regularExpression" id="fileReprocessingParamListRegularExpression[${counter.index}]" ></form:hidden> 
						 	<form:hidden path="fileReprocessingParamList[${counter.index}].errorMessage" id="fileReprocessingParamListErrorMessage[${counter.index}]" ></form:hidden> 
						 	<form:hidden path="fileReprocessingParamList[${counter.index}].enabled" id="fileReprocessingParamListSystemEnabled[${counter.index}]" ></form:hidden> 
						 	<form:hidden path="fileReprocessingParamList[${counter.index}].parameterGroup.id" id="fileReprocessingParamListParameterGroupParameterGroupId[${counter.index}]" ></form:hidden>
						 	<form:hidden path="fileReprocessingParamList[${counter.index}].parameterGroup.name" id="fileReprocessingParamListParameterGroupName${counter.index}]" ></form:hidden> 
						 	<form:hidden path="fileReprocessingParamList[${counter.index}].parameterGroup.enabled" id="fileReprocessingParamListParameterGroupEnabled[${counter.index}]" ></form:hidden> 
						 	<form:hidden path="fileReprocessingParamList[${counter.index}].displayOrder" id="fileReprocessingParamListdisplayOrder[${counter.index}]" ></form:hidden>
						 	<form:hidden path="fileReprocessingParamList[${counter.index}].createdByStaffId" id="fileReprocessingParamListcreatedByStaffId[${counter.index}]" ></form:hidden>
						 	<form:hidden path="fileReprocessingParamList[${counter.index}].createdDate" id="fileReprocessingParamListcreatedDate[${counter.index}]" ></form:hidden>
						</c:forEach>
						<c:set var="fileReprocessingTotalRowCount" value="${loopcount}" scope="request" ></c:set>
					</div>
				</div>
			</div>
		</form:form>
	</div>
</div>
<script type="text/javascript">
function editFileReprocessingParam(){
	var count = '${loopcount}';
	$("#page-heading-a").html("<spring:message code='systemParameter.fileReprocessing.edit.tab' ></spring:message>");
	
	for ( var i = 0; i < count; i++) {
		document.getElementById("fileReprocessingParamListValue[" + i + "]").disabled =false;
	}
	
	$("#fileReprocessing-save-btn").show();
	$("#fileReprocessing-reset-btn").show();
	$("#fileReprocessing-cancel-btn").show();
	$("#fileReprocessing-edit-btn").hide();
	
}

function cancelFileReprocessingForm() {
	$("#file-reprocessing-form").attr("action","<%=ControllerConstants.INIT_ALL_SYSTEM_PARAMETER%>");
	$("#file-reprocessing-form").submit();
}

function validateFileReprocessingFields() {
	$("#file-reprocessing-form").attr("action","<%=ControllerConstants.EDIT_SYSTEM_PARAMETER%>");
	var selectBox = document.getElementById("pwdTypeCombo");
	var selectedValue = selectBox.options[selectBox.selectedIndex].value;
	var selectedName = selectBox.options[selectBox.selectedIndex].text;
	var xyz='${pwdPolicyDescriptionDB}';
	$('#passwordType').val($('#pwdTypeCombo').val());
	console.log("value::" + selectedName);
	document.getElementById("GpasswordType").value=selectedName;
	document.getElementById("GpasswordPolicyNameCustom").value=xyz;
	$("#file-reprocessing-form").submit();
}

function resetFileReprocessingForm() {
	var count = '${loopcount}';
	for ( var i = 0; i < count; i++) {
		// loop thorugh the value text boxes and empty them.
		document.getElementById("fileReprocessingParamListValue[" + i + "]").value = '';
		
		if (document.getElementById("fileReprocessingParamListValueError["+ i + "]") != null) {
			document.getElementById("fileReprocessingParamListValueError["+ i + "]").html = '';
		}
	}
	resetWarningDisplay();
}
</script>
