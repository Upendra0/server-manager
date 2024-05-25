<%@page import="com.elitecore.sm.common.constants.SystemParametersConstant"%>
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
	<form:form modelAttribute="system_param_bean" method="POST" id="cust-system-parameter-form">
    	<div class="box box-warning">
        	<div class="box-header with-border">
            	<h3 class="box-title"><spring:message code="systemParameter.customer.section.heading" ></spring:message></h3>
                 	<div class="box-tools pull-right" style="top:-3px;">
                    	<button class="btn btn-box-tool" data-widget="collapse"><i class="fa fa-minus"></i></button>
                    </div><!-- /.box-tools -->
             </div><!-- /.box-header -->
              <div class="box-body">
              	
					<div class="fullwidth inline-form">
					<input type="hidden" id=<%=BaseConstants.REQUEST_ACTION_TYPE%> name=<%=BaseConstants.REQUEST_ACTION_TYPE%> value=<%=SystemParametersConstant.EDIT_CUST_SYSTEM_PARAM%> />
					 <input type="hidden" id="CpasswordType" name="CpasswordType" />
					 <input type="hidden" id="CpasswordPolicyNameCustom" name="CpasswordPolicyNameCustom" value="${pwdPolicyDescriptionDB}" />
					<c:set var="loopcount" value="0" scope="page" ></c:set>
					
					<c:set var="custadd" value="<%=SystemParametersConstant.CUSTOMER_ADDRESS%>" scope="page" />

					<c:forEach var="systemParam" items="${system_param_bean.custParamList}" varStatus="counter">
						
					<c:set var="loopcount" value="${loopcount + 1}" scope="page" ></c:set>
							
							<div class="col-md-2 mtop10" > 
								<p id="custParamList[${counter.index}].name">
									<c:out value="${systemParam.name}" ></c:out>
								</p>
							</div>
								
									<c:choose>
										<c:when test="${not empty systemParam.parameterDetail}">
										<div class="col-md-4" style="padding-left:0;">
										<div class="form-group">
										<div class="input-group">
							
										<form:select path="custParamList[${counter.index}].value" cssClass="form-control"
											id="custParamListValue[${counter.index}]" data-toggle="tooltip" data-placement="bottom" title="${systemParam.name}">
											<form:options items="${systemParam.parameterDetail}" itemLabel="name"></form:options>
										</form:select> 
										<spring:bind path="custParamList[${counter.index}].value">
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
											<form:textarea path="custParamList[${counter.index}].value" cssClass="form-control input-sm" id="custParamListValue[${counter.index}]" data-toggle="tooltip" data-placement="bottom" title="${systemParam.name}"></form:textarea>
											<spring:bind path="custParamList[${counter.index}].value">
									   				<error:showError errorMessage="${status.errorMessage}"></error:showError>
									   			</spring:bind> 
											</div>
											</div>
											</div>
										</c:when>
										<c:otherwise>
										<div class="col-md-4" style="padding-left:0;">
										<div class="form-group">
										<div class="input-group">
											<form:input path="custParamList[${counter.index}].value" cssClass="form-control table-cell input-sm"
												id="custParamListValue[${counter.index}]" data-toggle="tooltip" data-placement="bottom" title="${systemParam.name}"></form:input>
											<spring:bind path="custParamList[${counter.index}].value">
									   				<error:showError errorMessage="${status.errorMessage}"></error:showError>
									   		</spring:bind> 
											</div>
											</div>
											</div>
										</c:otherwise>
									</c:choose>
						<div class="col-md-6">
							<p id="custParamList[${counter.index}].description">
									
									 <spring:message code="system.parameter.${fn:toLowerCase(fn:replace(systemParam.name,' ', '.'))}.description" ></spring:message> 
									<%-- <c:out value="${systemParam.description}" ></c:out> --%>
									
							</p>
						</div>	
						<div class="clearfix"></div>
							<form:hidden path="custParamList[${counter.index}].name" id="custParamListParameterName[${counter.index}]" ></form:hidden>
							<form:hidden path="custParamList[${counter.index}].description" id="custParamListDescription[${counter.index}]" ></form:hidden>
							<form:hidden path="custParamList[${counter.index}].id" id="custParamListParameterId[${counter.index}]" ></form:hidden>
							<form:hidden path="custParamList[${counter.index}].alias" id="custParamListAliasParameterId[${counter.index}]" ></form:hidden>
							<form:hidden path="custParamList[${counter.index}].regularExpression" id="custParamListRegularExpression[${counter.index}]" ></form:hidden>
							<form:hidden path="custParamList[${counter.index}].errorMessage" id="custParamListErrorMessage[${counter.index}]" ></form:hidden>
							<form:hidden path="custParamList[${counter.index}].enabled" id="custParamListenabled[${counter.index}]" ></form:hidden>
							<form:hidden path="custParamList[${counter.index}].parameterGroup.id" id="custParamListParameterGroupParameterGroupId[${counter.index}]" ></form:hidden>
							<form:hidden path="custParamList[${counter.index}].parameterGroup.name" id="custParamListParameterGroupName${counter.index}]" ></form:hidden>
							<form:hidden path="custParamList[${counter.index}].parameterGroup.enabled" id="custParamListParameterGroupEnabled[${counter.index}]" ></form:hidden>
							<form:hidden path="custParamList[${counter.index}].displayOrder" id="custParamListdisplayOrder[${counter.index}]" ></form:hidden>
							<form:hidden path="custParamList[${counter.index}].createdByStaffId" id="genParamListcreatedByStaffId[${counter.index}]" ></form:hidden>
							<form:hidden path="custParamList[${counter.index}].createdDate" id="genParamListcreatedDate[${counter.index}]" ></form:hidden>
					</c:forEach>
					<c:set var="customerTotalRowCount" value="${loopcount}" scope="request"></c:set>
				</div>
			</div>
		</div>
			
		</form:form>
	</div>
</div>

<script type="text/javascript">

function editCustSystemParam(){
	
	$("#page-heading-a").html("<spring:message code='systemParameter.custGroup.edit.tab' ></spring:message>");
	var count = '${loopcount}';
	for ( var i = 0; i < count; i++) {
		document.getElementById("custParamListValue[" + i + "]").disabled =false;
	}
	
	$("#cust-save-btn").show();
	$("#cust-reset-btn").show();
	$("#cust-cancel-btn").show();
	$("#cust-edit-btn").hide();
	
}

function resetCustForm() {
	var count = '${loopcount}';
	for ( var i = 0; i < count; i++) {
		// loop thorugh the value text boxes and empty them.
		document.getElementById("custParamListValue[" + i + "]").value = '';
		
		if (document.getElementById("custParamListError["+ i + "]") != null) {
			
			document.getElementById("custParamListError["+ i + "]").html = '';
		}
	}
}
function cancelCustForm(){
	$("#cust-system-parameter-form").attr("action","<%= ControllerConstants.INIT_ALL_SYSTEM_PARAMETER %>");
	$("#cust-system-parameter-form").submit();				
}
function validateCustFields(){
	
	$("#cust-system-parameter-form").attr("action","<%= ControllerConstants.EDIT_CUSTOMER_SYSTEM_PARAMETER%>");
	var selectBox = document.getElementById("pwdTypeCombo");
	var selectedValue = selectBox.options[selectBox.selectedIndex].value;
	var selectedName = selectBox.options[selectBox.selectedIndex].text;
	var xyz='${pwdPolicyDescriptionDB}';
	$('#passwordType').val($('#pwdTypeCombo').val());
	console.log("value::" + selectedName);
	document.getElementById("CpasswordType").value=selectedName;
	document.getElementById("CpasswordPolicyNameCustom").value=xyz;
	$("#cust-system-parameter-form").submit();
}
                      
                      
</script>

