<%@page import="com.elitecore.sm.common.constants.SystemParametersConstant"%>
<%@page import="com.elitecore.sm.common.constants.BaseConstants"%>
<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<%@page import="com.elitecore.sm.util.MapCache"%>
<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@taglib uri="/WEB-INF/ShowErrorTag.tld" prefix="error" %>

<script src="${pageContext.request.contextPath}/customJS/ajaxLoader.js<%= "?v=" + Math.random() %>" type="text/javascript"></script>
<div class="box-body padding0 mtop10">
	<div class="fullwidth mtop10">
	<form:form modelAttribute="system_param_bean" method="POST" id="sso-system-parameter-form">
    	<div class="box box-warning">
        	<div class="box-header with-border">
            	<h3 class="box-title"><spring:message code="systemParameter.sso.section.heading" ></spring:message></h3>
                 	<div class="box-tools pull-right" style="top:-3px;">
                    	<button class="btn btn-box-tool" data-widget="collapse"><i class="fa fa-minus"></i></button>
                    </div><!-- /.box-tools -->
             </div><!-- /.box-header -->
              <div class="box-body">
              	
					<div class="fullwidth inline-form">
					<input type="hidden" id=<%=BaseConstants.REQUEST_ACTION_TYPE%> name=<%=BaseConstants.REQUEST_ACTION_TYPE%> value=<%=SystemParametersConstant.EDIT_SSO_PARAMETER%> />
					 <input type="hidden" id="CpasswordType" name="CpasswordType" />
					 <input type="hidden" id="CpasswordPolicyNameCustom" name="CpasswordPolicyNameCustom" value="${pwdPolicyDescriptionDB}" />
					<c:set var="loopcount" value="0" scope="page" ></c:set>
					<c:set var="custadd" value="<%=SystemParametersConstant.CUSTOMER_ADDRESS%>" scope="page" />
							
					<c:forEach var="systemParam" items="${system_param_bean.ssoParamList}" varStatus="counter">
					<c:set var="loopcount" value="${loopcount + 1}" scope="page" ></c:set>
						<div class="col-md-2 mtop10" > 
								<p id="ssoParamList[${counter.index}].name">
									<c:out value="${systemParam.name}" ></c:out>
								</p>
							</div>
							<c:choose>
								<c:when test="${not empty systemParam.parameterDetail and (systemParam.alias eq 'DEFAULT_SSO_ACCESS_GROUP')}">
								<div class="col-md-4" style="padding-left:0;">
								<div class="form-group">
								<div class="input-group">
								<form:select path="ssoParamList[${counter.index}].value" cssClass="form-control"
									 id="ssoParamListValue[${counter.index}]" data-toggle="tooltip" data-placement="bottom" title="${systemParam.name}">
									<c:forEach var="systemParamPool" items="${systemParam.parameterDetail}" varStatus="counterlist">
									<form:option value="${systemParamPool.value}"></form:option>
									</c:forEach>
								</form:select> 
								<spring:bind path="ssoParamList[${counter.index}].value">
							   		<error:showError errorMessage="${status.errorMessage}"></error:showError>
								</spring:bind> 
								</div>
								</div>
								</div>
								</c:when>
																		
								<c:when test="${systemParam.alias eq 'SSO_ENABLE'}">
								<div class="col-md-4" style="padding-left:0;">
								<div class="form-group">
								<div class="input-group">
									<form:select path="ssoParamList[${counter.index}].value" cssClass="form-control"
									 id="ssoParamListValue[${counter.index}]" data-toggle="tooltip" data-placement="bottom" title="${systemParam.name}">
									<c:forEach var="systemParamPool" items="${systemParam.parameterDetail}" varStatus="counterlist">
									<form:option value="${systemParamPool.value}"></form:option>
									</c:forEach>
									</form:select> 
									<spring:bind path="ssoParamList[${counter.index}].value">
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
									<form:textarea path="ssoParamList[${counter.index}].value" cssClass="form-control input-sm" id="ssoParamListValue[${counter.index}]" data-toggle="tooltip" data-placement="bottom" title="${systemParam.name}"></form:textarea>
									<spring:bind path="ssoParamList[${counter.index}].value">
							   				<error:showError errorMessage="${status.errorMessage}"></error:showError>
							   			</spring:bind> 
									</div>
									</div>
									</div>
								</c:when>
								<c:when test="${systemParam.alias eq 'SSO_ADMIN_PASSWORD'}">
								<div class="col-md-4" style="padding-left:0;">
								<div class="form-group">
								<div class="input-group" style="width: 90%">		
									<input type="password" value="${systemParam.value}"  name="ssoParamList[${counter.index}].value" class="form-control input-sm" id="ssoParamListValue[${counter.index}]" data-toggle="tooltip" data-placement="bottom" title="${systemParam.name}"/>
								</div>
								</div>
								</div>
								</c:when>
								<c:otherwise>
								<div class="col-md-4" style="padding-left:0;">
								<div class="form-group">
								<div class="input-group">
									<form:input path="ssoParamList[${counter.index}].value" cssClass="form-control table-cell input-sm"
										 id="ssoParamListValue[${counter.index}]" data-toggle="tooltip" data-placement="bottom" title="${systemParam.name}"></form:input>
									  
									<spring:bind path="ssoParamList[${counter.index}].value">
							   				<error:showError errorMessage="${status.errorMessage}"></error:showError>
							   		</spring:bind> 
									</div>
									</div>
									</div>
								</c:otherwise>	
							</c:choose>
						<div class="col-md-6">
							<p id="ssoParamList[${counter.index}].description">									
								<spring:message code="system.parameter.${fn:toLowerCase(fn:replace(systemParam.name,' ', '.'))}.description" ></spring:message> 																
							</p>
						</div>	
						<div class="clearfix"></div>
							<form:hidden path="ssoParamList[${counter.index}].name" id="ssoParamListParameterName[${counter.index}]" ></form:hidden>
							<form:hidden path="ssoParamList[${counter.index}].description" id="ssoParamListDescription[${counter.index}]" ></form:hidden>
							<form:hidden path="ssoParamList[${counter.index}].id" id="ssoParamListParameterId[${counter.index}]" ></form:hidden>
							<form:hidden path="ssoParamList[${counter.index}].alias" id="ssoParamListAliasParameterId[${counter.index}]" ></form:hidden>
							<form:hidden path="ssoParamList[${counter.index}].regularExpression" id="ssoParamListRegularExpression[${counter.index}]" ></form:hidden>
							<form:hidden path="ssoParamList[${counter.index}].errorMessage" id="ssoParamListErrorMessage[${counter.index}]" ></form:hidden>
							<form:hidden path="ssoParamList[${counter.index}].enabled" id="ssoParamListenabled[${counter.index}]" ></form:hidden>
							<form:hidden path="ssoParamList[${counter.index}].parameterGroup.id" id="ssoParamListParameterGroupParameterGroupId[${counter.index}]" ></form:hidden>
							<form:hidden path="ssoParamList[${counter.index}].parameterGroup.name" id="ssoParamListParameterGroupName${counter.index}]" ></form:hidden>
							<form:hidden path="ssoParamList[${counter.index}].parameterGroup.enabled" id="ssoParamListParameterGroupEnabled[${counter.index}]" ></form:hidden>
							<form:hidden path="ssoParamList[${counter.index}].displayOrder" id="ssoParamListdisplayOrder[${counter.index}]" ></form:hidden>
							<form:hidden path="ssoParamList[${counter.index}].createdByStaffId" id="genParamListcreatedByStaffId[${counter.index}]" ></form:hidden>
							<form:hidden path="ssoParamList[${counter.index}].createdDate" id="genParamListcreatedDate[${counter.index}]" ></form:hidden>
					
					</c:forEach>
					<c:set var="ssoTotalRowCount" value="${loopcount}" scope="request"></c:set>
					 
				</div>
			</div>
		</div>
			
		</form:form>
	</div>
</div>
<!-- Delete processing file pop-up div end here -->
<a href="#divMigrateStaff" class="fancybox" style="display: none;" id="migrate_staff_success_lnk"></a>
<!-- Reprocess success modal content start  -->
<div id="divMigrateStaff" style="width: 100%; display: none;">
	<div class="modal-content">
		<div class="modal-header padding10">
			<h4 class="modal-title">
				<spring:message code="system.parameter.login.staff.migration.summary.header" ></spring:message>				
			</h4>
		</div>
		<div class="modal-body padding10 inline-form">
			<p>				
				<span id="migration_success_message"></span>
			</p>			
		</div>
		<div class="modal-footer padding10" id = "migrate-staff-view-status-response-div" style="display:block;">			
			<button id="close_btn" type="button" class="btn btn-grey btn-xs " data-dismiss="modal" onclick="closeFancyBox();"><spring:message code="btn.label.close"></spring:message></button>
		</div>
	</div>
</div>
<script type="text/javascript">

function editSSOParam(){
	$("#page-heading-a").html("<spring:message code='systemParameter.custGroup.edit.tab' ></spring:message>");
	var count = '${loopcount}';

	for ( var i = 0; i < count; i++) {
		if (document.getElementById("ssoParamListValue["+ i + "]").value != null 				
				&& document.getElementById("ssoParamListValue["+ i + "]").value == 'true') {
			document.getElementById("ssoParamListValue[" + i + "]").disabled =true;
			document.getElementById("ssoParamListValue["+ i + "]").value = 'true'
		}else{		
			document.getElementById("ssoParamListValue[" + i + "]").disabled =false;
		}
	}
	
	$("#sso-save-btn").show();
	$("#sso-reset-btn").show();
	$("#sso-cancel-btn").show();
	$("#sso-edit-btn").hide();
	$("#staff-migrate-btn").hide();
	
}

function resetSSOParamForm() {
	var count = '${loopcount}';
	for ( var i = 0; i < count; i++) {
		// loop thorugh the value text boxes and empty them.
		document.getElementById("ssoParamListValue[" + i + "]").value = '';
		
		if (document.getElementById("ssoParamListError["+ i + "]") != null) {
			document.getElementById("ssoParamListError["+ i + "]").html = '';
		}
	}
}
function cancelSSOParamForm(){
	$("#sso-system-parameter-form").attr("action","<%= ControllerConstants.INIT_ALL_SYSTEM_PARAMETER %>");
	$("#sso-system-parameter-form").submit();	
	
}
function validateSSOParamFields(){
	$("#sso-system-parameter-form").attr("action","<%= ControllerConstants.EDIT_CUSTOMER_SYSTEM_PARAMETER%>");
	$("#sso-system-parameter-form").submit();
}

function migrateStaffToKeycloak(){
	waitingDialog.show();
	$.ajax({
		url: '<%=ControllerConstants.MIGRATE_STAFF_TO_KEYCLOAK%>',
		cache: false,
		async: false, 				
		type: 'GET',
		contentType : "application/json",
		success: function(data){
			var response = JSON.parse(data);
			var responseCode = response.code;
			var responseMsg = response.msg;
			var responseObject = response.object;
			waitingDialog.hide();
			var summary = responseObject.replace(/\n/g, '</br>');
			
			if (responseCode === "200") {				
 				$("#migration_success_message").empty();
 				$("#migration_success_message").html(summary); 				
 				$("#migrate_staff_success_lnk").click();			
			}else{
				showErrorMsg(responseObject);
			}
		},
	    error: function (xhr,st,err){
	    	alert('error' + err);
			handleGenericError(xhr,st,err);
		}
	});
}                      
</script>
