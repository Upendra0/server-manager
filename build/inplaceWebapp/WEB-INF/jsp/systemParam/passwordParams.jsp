<%@page
	import="com.elitecore.sm.common.constants.SystemParametersConstant"%>
<%@page import="com.elitecore.sm.common.constants.BaseConstants"%>
<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<%@taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@taglib uri="/WEB-INF/ShowErrorTag.tld" prefix="error"%>

<script>
$(document).ready(function() {
	var selectBox = document.getElementById("pwdTypeCombo");
	var selectedValue = selectBox.options[selectBox.selectedIndex].value;
	var selectedName = selectBox.options[selectBox.selectedIndex].text;
	console.log("value::" + selectedName);
	document.getElementById("passwordType").value=selectedName;
	if (selectedValue == '<%=SystemParametersConstant.PASSWORD_TYPE_REGEX%>'){
		$('#pwdPolicyDescriptionNameDiv').show();
		$('#pwdPolicyDescriptionDiv').show();
		$('#pwdPolicyDescriptionDescriptionDiv').show();
       document.getElementById("pwdParamList[3].passwordDescription").value='${pwdPolicyDescriptionDB}';
       document.getElementById("pwdRegex").readOnly=true;
       document.getElementById("pwdParamList[3].passwordDescription").readOnly=true;

}
	else{
		$('#pwdPolicyDescriptionNameDiv').hide();
		$('#pwdPolicyDescriptionDiv').hide();
		$('#pwdPolicyDescriptionDescriptionDiv').hide();
		document.getElementById("pwdParamList[3].passwordDescription").value='';
	}
})
</script>
<div class="box-body padding0 mtop10">
	<div class="fullwidth mtop10">
		<form:form modelAttribute="system_param_bean" method="POST"
			action="<%=ControllerConstants.MODIFY_SYSTEM_PARAMETER%>"
			id="pwd-system-parameter-form">
			<div class="box box-warning">
				<div class="box-header with-border">
					<h3 class="box-title">
						<spring:message code="systemParameter.password.section.heading" ></spring:message>
					</h3>
					<div class="box-tools pull-right" style="top: -3px;">
						<button class="btn btn-box-tool" data-widget="collapse">
							<i class="fa fa-minus"></i>
						</button>
					</div>
					<!-- /.box-tools -->
				</div>
				<!-- /.box-header -->
				<div class="box-body">



					<div class="fullwidth inline-form">
						<input type="hidden" id=<%=BaseConstants.REQUEST_ACTION_TYPE%>
							name=<%=BaseConstants.REQUEST_ACTION_TYPE%>
							value=<%=SystemParametersConstant.EDIT_PWD_SYSTEM_PARAM%> />
							 <input type="hidden" id="passwordType" name="passwordType" />
							 <input type="hidden" id="passwordPolicyNameCustom" name="passwordPolicyNameCustom" />
						<c:set var="loopcount" value="0" scope="page" ></c:set>
						<c:set var="pwdType"
							value="<%=SystemParametersConstant.PASSWORD_TYPE%>" scope="page" />
						<c:set var="regExType"
							value="<%=SystemParametersConstant.STAFF_PASSWORD%>" scope="page" />

						<c:forEach var="systemParam"
							items="${system_param_bean.pwdParamList}" varStatus="counter">
							<c:set var="loopcount" value="${loopcount + 1}" scope="page" ></c:set>

							<div class="col-md-2 mtop10" style="padding-left: 0;">
								<p id="pwdParamList[${counter.index}].name">
									<c:out value="${systemParam.name}" ></c:out><span class="required">*</span>
								</p>
							</div>
							<div class="col-md-4" style="padding-left: 0;">
								<div class="form-group">
									<div class="input-group">

										<c:choose>
											<c:when test="${not empty systemParam.parameterDetail}">
												<c:choose>
													<c:when test="${systemParam.alias eq pwdType}">
														<form:select path="pwdParamList[${counter.index}].value"
															cssClass="form-control table-cell input-sm"
															id="pwdTypeCombo" data-toggle="tooltip"
															data-placement="bottom" title="${systemParam.name}"
															onchange="changepwdType()" name="pwdTypeCombo">
															<form:options items="${systemParam.parameterDetail}"
																itemLabel="name" itemValue="value"></form:options>
														</form:select>
														<spring:bind path="pwdParamList[${counter.index}].value">
															<error:showError errorMessage="${status.errorMessage}"></error:showError>
														</spring:bind>
														<script>
													var text = '${systemParam.value}';
													$("option:contains('"+text+"')", "#pwdTypeCombo").attr("selected", true);
													//$("#pwdTypeCombo").find("option:containsext='"+text+"']").attr("selected", true);
												</script>
													</c:when>
													<c:otherwise>
														<form:select path="pwdParamList[${counter.index}].value"
															cssClass="form-control table-cell input-sm"
															id="pwdParamListValue[${counter.index}]"
															data-toggle="tooltip" data-placement="bottom"
															title="${systemParam.name}">
															<form:options items="${systemParam.parameterDetail}"
																itemLabel="name" itemValue="value"></form:options>
														</form:select>
														<spring:bind path="pwdParamList[${counter.index}].value">
															<error:showError errorMessage="${status.errorMessage}"></error:showError>
														</spring:bind>
													</c:otherwise>
												</c:choose>

											</c:when>
											<c:otherwise>
												<c:choose>
													<c:when test="${systemParam.alias eq regExType}">



														<form:input path="pwdParamList[${counter.index}].value"
															cssClass="form-control table-cell input-sm" id="pwdRegex"
															readonly="true" data-toggle="tooltip"
															data-placement="bottom" title="${systemParam.name}" ></form:input>

														<spring:bind path="pwdParamList[${counter.index}].value">
															<error:showError errorMessage="${status.errorMessage}"></error:showError>
														</spring:bind>

													</c:when>
													<c:otherwise>
														<form:input path="pwdParamList[${counter.index}].value"
															cssClass="form-control table-cell input-sm"
															data-toggle="tooltip" data-placement="bottom"
															title="${systemParam.name}"
															id="pwdParamListValue[${counter.index}]" ></form:input>
														<spring:bind path="pwdParamList[${counter.index}].value">
															<error:showError errorMessage="${status.errorMessage}"></error:showError>
														</spring:bind>
													</c:otherwise>
												</c:choose>
											</c:otherwise>
										</c:choose>
									</div>
								</div>
							</div>
							<div class="col-md-6">
								<p id="pwdParamList[${counter.index}].description">

									<spring:message
										code="system.parameter.${fn:toLowerCase(fn:replace(systemParam.name,' ', '.'))}.description" ></spring:message>
								<p>
							</div>
							<div class="clearfix"></div>

							<form:hidden path="pwdParamList[${counter.index}].name"
								id="pwdParamListParameterName[${counter.index}]" ></form:hidden>
							<form:hidden path="pwdParamList[${counter.index}].description"
								id="pwdParamListDescription[${counter.index}]" ></form:hidden>
							<form:hidden path="pwdParamList[${counter.index}].id"
								id="pwdParamListParameterId[${counter.index}]" ></form:hidden>
							<form:hidden path="pwdParamList[${counter.index}].alias"
								id="pwdParamListAliasParameterId[${counter.index}]" ></form:hidden>
							<form:hidden
								path="pwdParamList[${counter.index}].regularExpression"
								id="pwdParamListRegularExpression[${counter.index}]" ></form:hidden>
							<form:hidden path="pwdParamList[${counter.index}].errorMessage"
								id="pwdParamListErrorMessage[${counter.index}]" ></form:hidden>
							<form:hidden path="pwdParamList[${counter.index}].enabled"
								id="pwdParamListenabled[${counter.index}]" ></form:hidden>
							<form:hidden
								path="pwdParamList[${counter.index}].parameterGroup.id"
								id="pwdParamListParameterGroupParameterGroupId[${counter.index}]" ></form:hidden>
							<form:hidden
								path="pwdParamList[${counter.index}].parameterGroup.name"
								id="pwdParamListParameterGroupName${counter.index}]" ></form:hidden>
							<form:hidden
								path="pwdParamList[${counter.index}].parameterGroup.enabled"
								id="pwdParamListParameterGroupEnabled[${counter.index}]" ></form:hidden>
							<form:hidden path="pwdParamList[${counter.index}].displayOrder"
								id="pwdParamListdisplayOrder[${counter.index}]" ></form:hidden>
							<form:hidden
								path="pwdParamList[${counter.index}].createdByStaffId"
								id="genParamListcreatedByStaffId[${counter.index}]" ></form:hidden>
							<form:hidden path="pwdParamList[${counter.index}].createdDate"
								id="genParamListcreatedDate[${counter.index}]" ></form:hidden>
							<c:if test="${systemParam.alias eq pwdType}">
								<div id="pwdPolicyDescriptionNameDiv" class="col-md-2 mtop10" style="padding-left: 0;display:none;">
									<p id="pwdPolicyDescription.name">Password Policy
										Description<span class="required">*</span></p>
								</div>

								<div id="pwdPolicyDescriptionDiv" class="col-md-4" style="padding-left: 0;display:none;">
									<div class="form-group">
										<div class="input-group">
										
											<form:input path="pwdParamList[3].passwordDescription"
															cssClass="form-control table-cell input-sm"
															data-toggle="tooltip" data-placement="bottom"
															title="Password Policy Description"
															id="pwdParamList[3].passwordDescription" ></form:input>
										
										<spring:bind path="pwdParamList[3].passwordDescription">
															<error:showError errorMessage="${status.errorMessage}"></error:showError></spring:bind>
										

										</div>
									</div>
								</div>

								<div id="pwdPolicyDescriptionDescriptionDiv" class="col-md-6" style="display:none;">
									<p id="pwdPolicyDescription.description">PassWord Policy Description according to Regex</p>
									<p></p>
								</div>
								<div class="clearfix"></div>

							</c:if>
						</c:forEach>
						<c:set var="passwordTotalRowCount" value="${loopcount}"
							scope="request" ></c:set>
					</div>
				</div>
			</div>

		</form:form>
	</div>
</div>
<script type="text/javascript">

function editPwdSystemParam(){	
	
//	$("#pwdGroup-tab-a").html('<spring:message code="systemParameter.pwdGroup.edit.tab" ></spring:message>');
	$("#page-heading-a").html("<spring:message code='systemParameter.pwdGroup.edit.tab' ></spring:message>");
	
	document.getElementById("pwdTypeCombo").disabled =false;
	
	var count = '${loopcount}';
	for ( var i = 0; i < 2; i++) {
		document.getElementById("pwdParamListValue[" + i + "]").disabled =false;
	}
	for ( var i = 4; i < count; i++) {
		document.getElementById("pwdParamListValue[" + i + "]").disabled =false;
	}
	var selectBox = document.getElementById("pwdTypeCombo");
    var selectedValue = selectBox.options[selectBox.selectedIndex].value;
    if(selectedValue == '<%=SystemParametersConstant.PASSWORD_TYPE_REGEX%>'){
    	document.getElementById("pwdRegex").readOnly=false;
    	 document.getElementById("pwdParamList[3].passwordDescription").readOnly=false;
    }
    $("#pwd-save-btn").show();
	$("#pwd-reset-btn").show();
	$("#pwd-cancel-btn").show();
	$("#pwd-edit-btn").hide();
}

function resetPwdForm() {
	var count = '${loopcount}';

	if($("#pwdTypeCombo").val()=='<%=SystemParametersConstant.PASSWORD_TYPE_REGEX%>'){		
		document.getElementById("pwdParamList[3].passwordDescription").value='';
		document.getElementById("pwdRegex").value ='';
	}
	for ( var i = 0; i < 2; i++) {
		// loop thorugh the value text boxes and empty them.
		document.getElementById("pwdParamListValue[" + i + "]").value = '';	
	}
	for ( var i = 4; i < count; i++) {
		// loop thorugh the value text boxes and empty them.
		// skipping reset for requler expression field since it is disabled field
		if(document.getElementById("pwdRegex")==document.getElementById("pwdParamListValue[" + i + "]")){
			continue;
		}
		document.getElementById("pwdParamListValue[" + i + "]").value = '';		
	}
	resetWarningDisplay();
}
function cancelPwdForm(){
	$("#pwd-system-parameter-form").attr("action","<%=ControllerConstants.INIT_ALL_SYSTEM_PARAMETER%>");
	$("#pwd-system-parameter-form").submit();				
}

function validatePwdFields(){
	
	$("#pwd-system-parameter-form").attr("action","<%=ControllerConstants.EDIT_SYSTEM_PARAMETER%>");
		var option = $("#pwdTypeCombo>option:selected").html();
		$('#pwdTypeCombo').append($('<option>', {
			value : option,
			text : option
		}));
		$('#pwdTypeCombo').val(option);

		var selectBox = document.getElementById("pwdTypeCombo");
		var selectedValue = selectBox.options[selectBox.selectedIndex].value;
		var selectedName = selectBox.options[selectBox.selectedIndex].text;
		document.getElementById("passwordType").value = selectedName;		
		document.getElementById("passwordPolicyNameCustom").value = document.getElementById("pwdParamList[3].passwordDescription").value;
		
		$("#pwd-system-parameter-form").submit();
	}

	function changepwdType() {

		var selectBox = document.getElementById("pwdTypeCombo");
		var selectedValue = selectBox.options[selectBox.selectedIndex].value;
		var selectedName = selectBox.options[selectBox.selectedIndex].text;

		console.log("value::" + selectedName);

		if (selectedValue == '<%=SystemParametersConstant.PASSWORD_TYPE_REGEX%>'){
			$('#pwdPolicyDescriptionNameDiv').show();
			$('#pwdPolicyDescriptionDiv').show();
			$('#pwdPolicyDescriptionDescriptionDiv').show();
			document.getElementById("pwdRegex").value='';
           document.getElementById("pwdRegex").readOnly=false;
           document.getElementById("pwdParamList[3].passwordDescription").readOnly=false;
    }else{
    	$('#pwdPolicyDescriptionNameDiv').hide();
		$('#pwdPolicyDescriptionDiv').hide();
		$('#pwdPolicyDescriptionDescriptionDiv').hide();
           document.getElementById("pwdRegex").value=selectedValue;
           document.getElementById("passwordType").value=selectedName;
           document.getElementById("pwdRegex").readOnly=true;
           document.getElementById("pwdParamList[3].passwordDescription").value='';
    }
}
                      
</script>



