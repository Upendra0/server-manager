<%@page import="com.elitecore.sm.common.constants.SystemParametersConstant"%>
<%@page import="com.elitecore.sm.util.MapCache"%>
<%@page import="com.elitecore.sm.common.constants.BaseConstants"%>
<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@taglib uri="/WEB-INF/ShowErrorTag.tld" prefix="error" %>
<script>
$(document).ready(function() {
	var selectBox = document.getElementById("pwdTypeCombo");
	var selectedValue = selectBox.options[selectBox.selectedIndex].value;
	var selectedName = selectBox.options[selectBox.selectedIndex].text;
	var xyz='${pwdPolicyDescriptionDB}';
	$('#passwordType').val($('#pwdTypeCombo').val());
	console.log("value::" + selectedName);
	document.getElementById("CpasswordType").value=selectedName;
	document.getElementById("CpasswordPolicyNameCustom").value=xyz;
	
})
</script>
<div class="box-body padding0 mtop10">
	<div class="fullwidth mtop10">
	<form:form modelAttribute="system_param_bean" method="POST" id="ldap-system-parameter-form">
    	<div class="box box-warning">
        	<div class="box-header with-border">
            	<h3 class="box-title"><spring:message code="systemParameter.ldap.section.heading" ></spring:message></h3>
                 	<div class="box-tools pull-right" style="top:-3px;">
                    	<button class="btn btn-box-tool" data-widget="collapse"><i class="fa fa-minus"></i></button>
                    </div><!-- /.box-tools -->
             </div><!-- /.box-header -->
              <div class="box-body">
              	
					<div class="fullwidth inline-form">
					<input type="hidden" id=<%=BaseConstants.REQUEST_ACTION_TYPE%> name=<%=BaseConstants.REQUEST_ACTION_TYPE%> value=<%=SystemParametersConstant.EDIT_LOGIN_PARAMETER%> />
					 <input type="hidden" id="CpasswordType" name="CpasswordType" />
					 <input type="hidden" id="CpasswordPolicyNameCustom" name="CpasswordPolicyNameCustom" value="${pwdPolicyDescriptionDB}" />
					<c:set var="loopcount" value="0" scope="page" ></c:set>
					<c:set var="custadd" value="<%=SystemParametersConstant.CUSTOMER_ADDRESS%>" scope="page" />

					<c:forEach var="systemParam" items="${system_param_bean.ldapParamList}" varStatus="counter">
					<c:set var="loopcount" value="${loopcount + 1}" scope="page" ></c:set>
					<c:if test="${Boolean.parseBoolean(MapCache.getConfigValueAsObject(SystemParametersConstant.SSO_ENABLE) eq 'false' ) or (Boolean.parseBoolean(MapCache.getConfigValueAsObject(SystemParametersConstant.SSO_ENABLE)) and fn:contains(systemParam.alias, 'SSO'))}">	
							<div class="col-md-2 mtop10" > 
								<p id="ldapParamList[${counter.index}].name">
									<c:out value="${systemParam.name}" ></c:out>
								</p>
							</div>
							<c:choose>
								<c:when test="${not empty systemParam.parameterDetail}">
								<div class="col-md-4" style="padding-left:0;">
								<div class="form-group">
								<div class="input-group">
								
								<form:select path="ldapParamList[${counter.index}].value" cssClass="form-control"
									id="ldapParamListValue[${counter.index}]" data-toggle="tooltip" data-placement="bottom" title="${systemParam.name}">
									<c:forEach var="systemParamPool" items="${systemParam.parameterDetail}" varStatus="counterlist">
									<form:option value="${systemParamPool.value}"></form:option>
									</c:forEach>
								</form:select> 
								<spring:bind path="ldapParamList[${counter.index}].value">
							   		<error:showError errorMessage="${status.errorMessage}"></error:showError>
								</spring:bind> 
								</div>
								</div>
								</div>
								</c:when>										
								<c:when test="${systemParam.alias eq custadd}">
								<div class="col-md-4" style="padding-left:0;">
								<div class="form-group">
								<div class="input-group">
									<form:textarea path="ldapParamList[${counter.index}].value" cssClass="form-control input-sm" id="ldapParamListValue[${counter.index}]" data-toggle="tooltip" data-placement="bottom" title="${systemParam.name}"></form:textarea>
									<spring:bind path="ldapParamList[${counter.index}].value">
							   				<error:showError errorMessage="${status.errorMessage}"></error:showError>
							   		</spring:bind> 
									</div>
									</div>
									</div>
								</c:when>
								<c:when test="${systemParam.alias eq 'PASSWORD'}">
								<div class="col-md-4" style="padding-left:0;">
								<div class="form-group">
								<div class="input-group" style="width: 90%">
									<input type="password" value="ldapParamList[${counter.index}].value" class="form-control input-sm" id="ldapParamListValue[${counter.index}]" data-toggle="tooltip" data-placement="bottom" title="${systemParam.name}"/>
									</div>
									</div>
									</div>
								</c:when>
								<c:otherwise>
								<div class="col-md-4" style="padding-left:0;">
								<div class="form-group">
								<div class="input-group">
									<form:input path="ldapParamList[${counter.index}].value" cssClass="form-control table-cell input-sm"
										id="ldapParamListValue[${counter.index}]" data-toggle="tooltip" data-placement="bottom" title="${systemParam.name}"></form:input>
									<spring:bind path="ldapParamList[${counter.index}].value">
							   				<error:showError errorMessage="${status.errorMessage}"></error:showError>
							   		</spring:bind> 
									</div>
									</div>
									</div>
								</c:otherwise>
							</c:choose>
						<div class="col-md-6">
							<p id="ldapParamList[${counter.index}].description">									
								<spring:message code="system.parameter.${fn:toLowerCase(fn:replace(systemParam.name,' ', '.'))}.description" ></spring:message> 																
							</p>
						</div>	
						<div class="clearfix"></div>
							<form:hidden path="ldapParamList[${counter.index}].name" id="ldapParamListParameterName[${counter.index}]" ></form:hidden>
							<form:hidden path="ldapParamList[${counter.index}].description" id="ldapParamListDescription[${counter.index}]" ></form:hidden>
							<form:hidden path="ldapParamList[${counter.index}].id" id="ldapParamListParameterId[${counter.index}]" ></form:hidden>
							<form:hidden path="ldapParamList[${counter.index}].alias" id="ldapParamListAliasParameterId[${counter.index}]" ></form:hidden>
							<form:hidden path="ldapParamList[${counter.index}].regularExpression" id="ldapParamListRegularExpression[${counter.index}]" ></form:hidden>
							<form:hidden path="ldapParamList[${counter.index}].errorMessage" id="ldapParamListErrorMessage[${counter.index}]" ></form:hidden>
							<form:hidden path="ldapParamList[${counter.index}].enabled" id="ldapParamListenabled[${counter.index}]" ></form:hidden>
							<form:hidden path="ldapParamList[${counter.index}].parameterGroup.id" id="ldapParamListParameterGroupParameterGroupId[${counter.index}]" ></form:hidden>
							<form:hidden path="ldapParamList[${counter.index}].parameterGroup.name" id="ldapParamListParameterGroupName${counter.index}]" ></form:hidden>
							<form:hidden path="ldapParamList[${counter.index}].parameterGroup.enabled" id="ldapParamListParameterGroupEnabled[${counter.index}]" ></form:hidden>
							<form:hidden path="ldapParamList[${counter.index}].displayOrder" id="ldapParamListdisplayOrder[${counter.index}]" ></form:hidden>
							<form:hidden path="ldapParamList[${counter.index}].createdByStaffId" id="genParamListcreatedByStaffId[${counter.index}]" ></form:hidden>
							<form:hidden path="ldapParamList[${counter.index}].createdDate" id="genParamListcreatedDate[${counter.index}]" ></form:hidden>
					</c:if>
					</c:forEach>
					<c:set var="ldapTotalRowCount" value="${loopcount}" scope="request"></c:set>
				</div>
			</div>
		</div>
			
		</form:form>
	</div>
</div>

<script type="text/javascript">

function editLdapParam(){
	
	$("#page-heading-a").html("<spring:message code='systemParameter.custGroup.edit.tab' ></spring:message>");
	var count = '${loopcount}';
	var sso_alias;
	for ( var i = 0; i < count; i++) {
		if (document.getElementById("ldapParamListValue["+ i + "]").value != null 				
				&& document.getElementById("ldapParamListValue["+ i + "]").value == 'True') {
			document.getElementById("ldapParamListValue[" + i + "]").disabled =true;
		}else{		
			document.getElementById("ldapParamListValue[" + i + "]").disabled =false;
		}
	}
	
	$("#ldap-save-btn").show();
	$("#ldap-reset-btn").show();
	$("#ldap-cancel-btn").show();
	$("#ldap-edit-btn").hide();
	
}

function resetLdapParamForm() {
	var count = '${loopcount}';
	for ( var i = 0; i < count; i++) {
		// loop thorugh the value text boxes and empty them.
		document.getElementById("ldapParamListValue[" + i + "]").value = '';
		
		if (document.getElementById("ldapParamListError["+ i + "]") != null) {
			document.getElementById("ldapParamListError["+ i + "]").html = '';
		}
	}
}
function cancelLdapParamForm(){
	$("#ldap-system-parameter-form").attr("action","<%= ControllerConstants.INIT_ALL_SYSTEM_PARAMETER %>");
	$("#ldap-system-parameter-form").submit();				
}
function validateLdapParamFields(){
	$("#ldap-system-parameter-form").attr("action","<%= ControllerConstants.EDIT_CUSTOMER_SYSTEM_PARAMETER%>");
	var selectBox = document.getElementById("pwdTypeCombo");
	var selectedValue = selectBox.options[selectBox.selectedIndex].value;
	var selectedName = selectBox.options[selectBox.selectedIndex].text;
	var xyz='${pwdPolicyDescriptionDB}';
	$('#passwordType').val($('#pwdTypeCombo').val());	
	document.getElementById("CpasswordType").value=selectedName;
	document.getElementById("CpasswordPolicyNameCustom").value=xyz;
	$("#ldap-system-parameter-form").submit();
}             
</script>
