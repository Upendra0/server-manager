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

<%@ taglib tagdir="/WEB-INF/tags" prefix="elitecore"%>
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
		<form:form modelAttribute="system_param_bean" method="POST"
			action="<%=ControllerConstants.MODIFY_SYSTEM_PARAMETER%>"
			id="system-parameter-form">
			<div class="box box-warning">
				<div class="box-header with-border">
					<h3 class="box-title">
						<spring:message code="systemParameter.general.section.heading" ></spring:message>
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
							value=<%=SystemParametersConstant.EDIT_GEN_SYSTEM_PARAM%> /> <input
							type="hidden" id="GpasswordType" name="GpasswordType" /> <input
							type="hidden" id="GpasswordPolicyNameCustom"
							name="GpasswordPolicyNameCustom" />
						<c:set var="loopcount" value="0" scope="page" ></c:set>

						<c:forEach var="systemParam"
							items="${system_param_bean.genParamList}" varStatus="counter">
							<c:set var="loopcount" value="${loopcount + 1}" scope="page" ></c:set>

							<div class="col-md-2 mtop10">
								<p id="genParamList[${counter.index}].name">
									<spring:message
										code="system.parameter.${fn:toLowerCase(fn:replace(systemParam.name,' ', '.'))}.label" ></spring:message>
									&nbsp<span class="required">*</span>
								</p>
							</div>
							<div class="col-md-4" style="padding-left: 0;">
								<div class="form-group">
									<div class="input-group">

										<c:choose>
											<c:when test="${not empty systemParam.parameterDetail}">
												<form:select path="genParamList[${counter.index}].value"
													cssClass="form-control table-cell input-sm"
													data-toggle="tooltip" data-placement="bottom"
													title="${systemParam.name}"
													id="genParamListValue[${counter.index}]">
													<form:options items="${systemParam.parameterDetail}"
														itemLabel="name" id="genParamListDetail[${counter.index}]"
														itemValue="value"></form:options>
												</form:select>
												<spring:bind path="genParamList[${counter.index}].value">
													<error:showError errorMessage="${status.errorMessage}"></error:showError>
												</spring:bind>
											</c:when>
											<c:otherwise>
												<form:input path="genParamList[${counter.index}].value"
													cssClass="form-control table-cell input-sm"
													id="genParamListValue[${counter.index}]"
													data-toggle="tooltip" data-placement="bottom"
													title="${systemParam.name}" ></form:input>
												<spring:bind path="genParamList[${counter.index}].value">
													<error:showError errorMessage="${status.errorMessage}"
														errorId="genParamListValue[${counter.index}]_error"></error:showError>
												</spring:bind>


											</c:otherwise>
										</c:choose>
									</div>
								</div>
							</div>
							<div class="col-md-6">
								<p id="genParamList[${counter.index}].description">
									<spring:message
										code="system.parameter.${fn:toLowerCase(fn:replace(systemParam.name,' ', '.'))}.description" ></spring:message>
								<p>
							</div>
							<div class="clearfix"></div>

							<form:hidden path="genParamList[${counter.index}].name"
								id="genParamListParameterName[${counter.index}]" ></form:hidden>
							<form:hidden path="genParamList[${counter.index}].description"
								id="genParamListDescription[${counter.index}]" ></form:hidden>
							<form:hidden path="genParamList[${counter.index}].id"
								id="genParamListParameterId[${counter.index}]" ></form:hidden>
							<form:hidden path="genParamList[${counter.index}].alias"
								id="genParamListAliasParameterId[${counter.index}]" ></form:hidden>
							<form:hidden
								path="genParamList[${counter.index}].regularExpression"
								id="genParamListRegularExpression[${counter.index}]" ></form:hidden>
							<form:hidden path="genParamList[${counter.index}].errorMessage"
								id="genParamListErrorMessage[${counter.index}]" ></form:hidden>
							<form:hidden path="genParamList[${counter.index}].enabled"
								id="genParamListSystemEnabled[${counter.index}]" ></form:hidden>
							<form:hidden
								path="genParamList[${counter.index}].parameterGroup.id"
								id="genParamListParameterGroupParameterGroupId[${counter.index}]" ></form:hidden>
							<form:hidden
								path="genParamList[${counter.index}].parameterGroup.name"
								id="genParamListParameterGroupName${counter.index}]" ></form:hidden>
							<form:hidden
								path="genParamList[${counter.index}].parameterGroup.enabled"
								id="genParamListParameterGroupEnabled[${counter.index}]" ></form:hidden>
							<form:hidden path="genParamList[${counter.index}].displayOrder"
								id="genParamListdisplayOrder[${counter.index}]" ></form:hidden>
							<form:hidden
								path="genParamList[${counter.index}].createdByStaffId"
								id="genParamListcreatedByStaffId[${counter.index}]" ></form:hidden>
							<form:hidden path="genParamList[${counter.index}].createdDate"
								id="genParamListcreatedDate[${counter.index}]" ></form:hidden>

						</c:forEach>

						<c:set var="generalTotalRowCount" value="${loopcount}"
							scope="request" ></c:set>

					</div>
				</div>
			</div>
		</form:form>
	</div>
</div>

<script>
		
				function resetForm() {
					var count = '${loopcount}';
					for ( var i = 0; i < count; i++) {
						// loop thorugh the value text boxes and empty them.
						document.getElementById("genParamListValue[" + i + "]").value = '';
						
						if (document.getElementById("genParamListValueError["+ i + "]") != null) {
							document.getElementById("genParamListValueError["+ i + "]").html = '';
						}
					}
					resetWarningDisplay();
				}
				
				function editSystemParam(){
								
					$("#page-heading-a").html("<spring:message code='systemParameter.genGroup.edit.tab' ></spring:message>");					
					var count = '${loopcount}';
					for ( var i = 0; i < count; i++) {
						document.getElementById("genParamListValue[" + i + "]").disabled =false;
					}
					$("#save-btn").show();
					$("#reset-btn").show();
					$("#cancel-btn").show();
					$("#edit-btn").hide();					
				}
				
				function validateFields(){
					$("#system-parameter-form").attr("action","<%=ControllerConstants.EDIT_SYSTEM_PARAMETER%>");
					var selectBox = document.getElementById("pwdTypeCombo");
					var selectedValue = selectBox.options[selectBox.selectedIndex].value;
					var selectedName = selectBox.options[selectBox.selectedIndex].text;
					var xyz='${pwdPolicyDescriptionDB}';
					$('#passwordType').val($('#pwdTypeCombo').val());
					console.log("value::" + selectedName);
					document.getElementById("GpasswordType").value=selectedName;
					document.getElementById("GpasswordPolicyNameCustom").value=xyz;
					$("#system-parameter-form").submit();
				}
				
				
				function cancelForm(){
					$("#system-parameter-form").attr("action","<%=ControllerConstants.INIT_ALL_SYSTEM_PARAMETER%>");
		$("#system-parameter-form").submit();
	}
</script>



