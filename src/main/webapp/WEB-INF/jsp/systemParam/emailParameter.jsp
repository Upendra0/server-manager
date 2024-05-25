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
	<form:form modelAttribute="system_param_bean" method="POST" id="email-system-parameter-form">
    	<div class="box box-warning">
        	<div class="box-header with-border">
            	<h3 class="box-title"><spring:message code="systemParameter.email.section.heading" ></spring:message></h3>
                 	<div class="box-tools pull-right" style="top:-3px;">
                    	<button class="btn btn-box-tool" data-widget="collapse"><i class="fa fa-minus"></i></button>
                    </div><!-- /.box-tools -->
             </div><!-- /.box-header -->
              <div class="box-body">
              	
					<div class="fullwidth inline-form">
					<input type="hidden" id=<%=BaseConstants.REQUEST_ACTION_TYPE%> name=<%=BaseConstants.REQUEST_ACTION_TYPE%> value=<%=SystemParametersConstant.EDIT_EMAIL_PARAMETER%> />
					 <input type="hidden" id="CpasswordType" name="CpasswordType" />
					 <input type="hidden" id="CpasswordPolicyNameCustom" name="CpasswordPolicyNameCustom" value="${pwdPolicyDescriptionDB}" />
					<c:set var="loopcount" value="0" scope="page" ></c:set>
					<c:set var="custadd" value="<%=SystemParametersConstant.CUSTOMER_ADDRESS%>" scope="page" />

					<c:forEach var="systemParam" items="${system_param_bean.emailParamList}" varStatus="counter">
					<c:set var="loopcount" value="${loopcount + 1}" scope="page" ></c:set>
							<div class="col-md-2 mtop10" > 
								<p id="emailParamList[${counter.index}].name">
									<c:out value="${systemParam.name}" ></c:out>
								</p>
							</div>
									<c:choose>
									<c:when test="${systemParam.alias eq 'AUTHENTICATION_TYPE'}">
										<div class="col-md-4" style="padding-left:0;">
										<div class="form-group">
										<div class="input-group">
											<form:select path="emailParamList[${counter.index}].value" cssClass="form-control"
											id="emailParamListValue[${counter.index}]" data-toggle="tooltip" data-placement="bottom" title="${systemParam.name}">
											<form:option value="true"></form:option>
											<form:option value="false"></form:option>
										</form:select> 
										<spring:bind path="emailParamList[${counter.index}].value">
									   		<error:showError errorMessage="${status.errorMessage}"></error:showError>
										</spring:bind> 
											</div>
											</div>
											</div>
										</c:when>
										<c:when test="${not empty systemParam.parameterDetail}">
										<div class="col-md-4" style="padding-left:0;">
										<div class="form-group">
										<div class="input-group">
							
										<form:select path="emailParamList[${counter.index}].value" cssClass="form-control"
											id="emailParamListValue[${counter.index}]" data-toggle="tooltip" data-placement="bottom" title="${systemParam.name}">
											<form:options items="${systemParam.parameterDetail}" itemLabel="name"></form:options>
										</form:select> 
										<spring:bind path="emailParamList[${counter.index}].value">
									   		<error:showError errorMessage="${status.errorMessage}"></error:showError>
										</spring:bind> 
										</div>
										</div>
										</div>
										</c:when>
										
										<c:when test="${systemParam.alias eq 'RESET_PASSWORD_MAIL_TEMPLET' || systemParam.alias eq 'RESET_PASSWORD_MAIL_SUBJECT'}">
										<div class="col-md-4" style="padding-left:0;">
										<div class="form-group">
										<div class="input-group">
											<form:textarea path="emailParamList[${counter.index}].value" cssClass="form-control input-sm" id="emailParamListValue[${counter.index}]" data-toggle="tooltip" data-placement="bottom" title="${systemParam.name}"></form:textarea>
											<spring:bind path="emailParamList[${counter.index}].value">
									   				<error:showError errorMessage="${status.errorMessage}"></error:showError>
									   			</spring:bind> 
											</div>
											</div>
											</div>
										</c:when>										
										<c:when test="${systemParam.alias eq 'FROM_EMAIL_PASSWORD' || systemParam.alias eq 'FROM_EMAIL_PASSWORD'}">
										<div class="col-md-4" style="padding-left:0;">
										<div class="form-group">
										<div class="input-group" style="width: 90%" >
											<input type="password" value="${systemParam.value}" name="emailParamList[${counter.index}].value" class="form-control input-sm" id="emailParamListValue[${counter.index}]" data-toggle="tooltip" data-placement="bottom" title="${systemParam.name}"/>
											<%-- <form:password path="emailParamList[${counter.index}].value" cssClass="form-control input-sm" id="emailParamListValue[${counter.index}]" data-toggle="tooltip" data-placement="bottom" title="${systemParam.name}"></form:password>
											<spring:bind path="emailParamList[${counter.index}].value">
									   				<error:showError errorMessage="${status.errorMessage}"></error:showError>
									   			</spring:bind>  --%>
											</div>
											</div>
											</div>
										</c:when>
										<c:otherwise>
										<div class="col-md-4" style="padding-left:0;">
										<div class="form-group">
										<div class="input-group">
											<form:input path="emailParamList[${counter.index}].value" cssClass="form-control table-cell input-sm"
												id="emailParamListValue[${counter.index}]" data-toggle="tooltip" data-placement="bottom" title="${systemParam.name}"></form:input>
											<spring:bind path="emailParamList[${counter.index}].value">
									   				<error:showError errorMessage="${status.errorMessage}"></error:showError>
									   		</spring:bind> 
											</div>
											</div>
											</div>
										</c:otherwise>
									</c:choose>
						<div class="col-md-6">
							<p id="emailParamList[${counter.index}].description">									
								<spring:message code="system.parameter.${fn:toLowerCase(fn:replace(systemParam.name,' ', '.'))}.description" ></spring:message>																		
							</p>
						</div>	
						<div class="clearfix"></div>
							<form:hidden path="emailParamList[${counter.index}].name" id="emailParamListParameterName[${counter.index}]" ></form:hidden>
							<form:hidden path="emailParamList[${counter.index}].description" id="emailParamListDescription[${counter.index}]" ></form:hidden>
							<form:hidden path="emailParamList[${counter.index}].id" id="emailParamListParameterId[${counter.index}]" ></form:hidden>
							<form:hidden path="emailParamList[${counter.index}].alias" id="emailParamListAliasParameterId[${counter.index}]" ></form:hidden>
							<form:hidden path="emailParamList[${counter.index}].regularExpression" id="emailParamListRegularExpression[${counter.index}]" ></form:hidden>
							<form:hidden path="emailParamList[${counter.index}].errorMessage" id="emailParamListErrorMessage[${counter.index}]" ></form:hidden>
							<form:hidden path="emailParamList[${counter.index}].enabled" id="emailParamListenabled[${counter.index}]" ></form:hidden>
							<form:hidden path="emailParamList[${counter.index}].parameterGroup.id" id="emailParamListParameterGroupParameterGroupId[${counter.index}]" ></form:hidden>
							<form:hidden path="emailParamList[${counter.index}].parameterGroup.name" id="emailParamListParameterGroupName${counter.index}]" ></form:hidden>
							<form:hidden path="emailParamList[${counter.index}].parameterGroup.enabled" id="emailParamListParameterGroupEnabled[${counter.index}]" ></form:hidden>
							<form:hidden path="emailParamList[${counter.index}].displayOrder" id="emailParamListdisplayOrder[${counter.index}]" ></form:hidden>
							<form:hidden path="emailParamList[${counter.index}].createdByStaffId" id="genParamListcreatedByStaffId[${counter.index}]" ></form:hidden>
							<form:hidden path="emailParamList[${counter.index}].createdDate" id="genParamListcreatedDate[${counter.index}]" ></form:hidden>
					</c:forEach>
					<c:set var="emailTotalRowCount" value="${loopcount}" scope="request"></c:set>
				</div>
					<div class="col-md-2 mtop10 no-padding">
						<p><spring:message code="system.parameter.footer.image.label" ></spring:message></p>
					</div>
					<div class="col-md-4" style="padding-left: 0;">
						<div class="form-group">
							<div class="input-group">
							<div class="fullwidth" id="control">
								<input type="file" class="filestyle form-control" tabindex="14" data-buttonText="<spring:message code="serviceMgmt.browse.file"></spring:message>" id="footerImageFile" name="footerImageFile" accept="image/*">
							</div>
							<span class='input-group-addon' style='visibility: visible !important;'>
								<button type="button" id="email-footer-image-upload" class="btn btn-grey btn-xs " style="display: block" onclick="uploadEmailFooterImage2('footerImage','${systemParam.id}');">
											Upload
								</button>
							</span>
							</div>
						</div>
					</div>
					<div class="col-md-6 no-padding">
						<p ><spring:message code="system.parameter.footer.image.location.description" ></spring:message></p>
					</div>
			</div>
		</div>
			
		</form:form>
		
		<!-- Email footer Image form Start  -->
		<form:form modelAttribute="system_param_bean" method="POST" id="email-footer-logo-form" enctype="multipart/form-data">
    	<div class="box box-warning">
        	<div class="box-header with-border">
            	<h3 class="box-title"><spring:message code="systemParameter.email.footer.image.section.heading" ></spring:message></h3>
                 	<div class="box-tools pull-right" style="top:-3px;">
                    	<button class="btn btn-box-tool" data-widget="collapse"><i class="fa fa-minus"></i></button>
                    </div><!-- /.box-tools -->
             </div><!-- /.box-header -->
              <div class="box-body">
              	
					<div class="fullwidth inline-form">
					<input type="hidden" id=<%=BaseConstants.REQUEST_ACTION_TYPE%> name=<%=BaseConstants.REQUEST_ACTION_TYPE%> value=<%=SystemParametersConstant.EDIT_EMAIL_FOOTER_IMAGE%> />
					<input type="hidden" id="logoTypeImage" name="logoType" />
					<input type="hidden" id="emailFooterImageId" name="emailFooterImageId"/>
					<input type="hidden" id="CLpasswordType" name="CLpasswordType" />
					 <input type="hidden" id="CLpasswordPolicyNameCustom" name="CLpasswordPolicyNameCustom" value="${pwdPolicyDescriptionDB}" />
					<c:set var="loopcount" value="0" scope="page" ></c:set>
					<c:set var="custLogo" value="<%=SystemParametersConstant.CUSTOMER_LOGO%>" scope="page" />
					<c:set var="custLargeLogo" value="<%=SystemParametersConstant.CUSTOMER_LOGO_LARGE%>" scope="page" />
					

					<c:forEach var="systemParam" items="${system_param_bean.emailFooterLogoList}" varStatus="counter">
						
					<c:set var="loopcount" value="${loopcount + 1}" scope="page" ></c:set>
							
							<div class="col-md-2 mtop10" > 
								<p id="custLogoParamList[${counter.index}].name">
									<c:out value="${systemParam.name}" ></c:out>
								</p>
							</div>
								
									<c:choose>
										
										<c:when test="${systemParam.alias eq 'FOOTER_IMAGE'}">
										
											<div class="col-md-1 col-sm-2 col-xs-3" >
											<div id="emailFooterPreview" style="height:50px;width:50px;margin-bottom:5px;border:1px solid #ccc">
												<img alt="Logo" src="${FOOTER_IMAGE}" id="emailFooterImg" style="height:50px; width:50px " data-toggle="tooltip" data-placement="bottom" title="${systemParam.name}"/>
											</div>
											</div>
											<div class="col-md-3 col-sm-12 col-xs-7" style="padding-left:0;">
											<div class="form-group">
												<input type="file" class="filestyle form-control" data-buttonText="<spring:message code="serviceMgmt.browse.file"></spring:message>" id="emailFooterFile" name="emailFooterFile" accept='image/*'>
												 <spring:bind path="emailFooterLogoList[${counter.index}].value">
									   				<error:showError errorMessage="${status.errorMessage}"></error:showError>
									   			</spring:bind>
											</div>
											</div>
											<div class="col-md-1 col-sm-2 col-xs-3" style="padding-left:0;">
											<div class="form-group">
												<button class="btn btn-grey btn-xs " id="email-footer-upload" type="button" onclick="validateFileImage('footerImage','${systemParam.id}');"><spring:message code="btn.label.upload" ></spring:message></button>
											</div>
											</div>
											<script type="text/javascript">
												$("#emailFooterFile").on("change", function(){
												
													var files = !!this.files ? this.files : [];
											    	if (!files.length || !window.FileReader) return; // no file selected, or no FileReader support

													if (/^image/.test( files[0].type)){ // only image file
														// Reference: http://stackoverflow.com/questions/19816705/javascript-jquery-size-and-dimension-of-uploaded-image-file
														var _URL = window.URL || window.webkitURL;
														var file = files[0];
														var img = new Image();
														var sizeKB = file.size / 1024;
														img.onload = function(){
															$('#emailFooterPreview').html('');
															$('#emailFooterPreview').append(img);
															$('#emailFooterPreview img').css("height", "50px");
															$('#emailFooterPreview img').css("width", "50px");
														}
														img.src = _URL.createObjectURL(file);
													}
												});
											</script>
										</c:when>
										
									</c:choose>
						<div class="col-md-5">
							<p id="emailFooterLogoList[${counter.index}].description">
									 <spring:message code="system.parameter.${fn:toLowerCase(fn:replace(systemParam.name,' ', '.'))}.description" ></spring:message> 
									<%-- <c:out value="${systemParam.description}" ></c:out> --%>
							</p>
						</div>	
						<div class="clearfix"></div>
							<form:hidden path="emailFooterLogoList[${counter.index}].name" id="emailFooterLogoListParameterName[${counter.index}]" ></form:hidden>
							<form:hidden path="emailFooterLogoList[${counter.index}].description" id="emailFooterLogoListDescription[${counter.index}]" ></form:hidden>
							<form:hidden path="emailFooterLogoList[${counter.index}].value" id="emailFooterLogoListValue[${counter.index}]" ></form:hidden>
							<form:hidden path="emailFooterLogoList[${counter.index}].id" id="emailFooterLogoListParameterId[${counter.index}]" ></form:hidden>
							<form:hidden path="emailFooterLogoList[${counter.index}].alias" id="emailFooterLogoListAliasParameterId[${counter.index}]" ></form:hidden>
							<form:hidden path="emailFooterLogoList[${counter.index}].regularExpression" id="emailFooterLogoListRegularExpression[${counter.index}]" ></form:hidden>
							<form:hidden path="emailFooterLogoList[${counter.index}].errorMessage" id="emailFooterLogoListErrorMessage[${counter.index}]" ></form:hidden>
							<form:hidden path="emailFooterLogoList[${counter.index}].enabled" id="emailFooterLogoListenabled[${counter.index}]" ></form:hidden>
							<form:hidden path="emailFooterLogoList[${counter.index}].parameterGroup.id" id="emailFooterLogoListParameterGroupParameterGroupId[${counter.index}]" ></form:hidden>
							<form:hidden path="emailFooterLogoList[${counter.index}].parameterGroup.name" id="emailFooterLogoListParameterGroupName${counter.index}]" ></form:hidden>
							<form:hidden path="emailFooterLogoList[${counter.index}].parameterGroup.enabled" id="emailFooterLogoListParameterGroupEnabled[${counter.index}]" ></form:hidden>
							<form:hidden path="emailFooterLogoList[${counter.index}].displayOrder" id="emailFooterLogoListdisplayOrder[${counter.index}]" ></form:hidden>
							<form:hidden path="emailFooterLogoList[${counter.index}].createdByStaffId" id="emailFooterLogoListcreatedByStaffId[${counter.index}]" ></form:hidden>
							<form:hidden path="emailFooterLogoList[${counter.index}].createdDate" id="emailFooterLogoListcreatedDate[${counter.index}]" ></form:hidden>
					</c:forEach>
					<c:set var="emailFooterRowCount" value="${loopcount1}" scope="request"></c:set>
				</div>
			</div>
		</div>
			
		</form:form>
		<!-- Email footer Image form End  -->
	</div>
</div>

<script type="text/javascript">
	function editEmailParam() {
		resetWarningDisplay();
		clearAllMessages();
		$("#page-heading-a").html(
				"<spring:message code='systemParameter.custGroup.edit.tab' ></spring:message>");
		var count = '${emailTotalRowCount}';
		for (var i = 0; i < count; i++) {
			document.getElementById("emailParamListValue[" + i + "]").disabled = false;
		}
		$("#footerImageFile").attr("disabled",false);
		$("#email-footer-logo-form").hide();
		$("#email-footer-image-upload").show();
		$("#email-save-btn").show();
		$("#email-reset-btn").show();
		$("#email-cancel-btn").show();
		$("#email-edit-btn").hide();

	}

	function resetEmailParamForm() {
		resetWarningDisplay();
		clearAllMessages();
		var count = '${emailTotalRowCount}';
		for (var i = 0; i < count; i++) {
			// loop thorugh the value text boxes and empty them.
			document.getElementById("emailParamListValue[" + i + "]").value = '';

			if (document.getElementById("emailParamListError[" + i + "]") != null) {

				document.getElementById("emailParamListError[" + i + "]").html = '';
			}
		}
	}
	function cancelEmailParamForm() {
		$("#email-system-parameter-form").attr("action","<%= ControllerConstants.INIT_ALL_SYSTEM_PARAMETER %>");
		$("#email-footer-logo-form").hide();
	$("#email-system-parameter-form").submit();				
}
function validateEmailParamFields(){
	resetWarningDisplay();
	clearAllMessages();
	$("#email-system-parameter-form").attr("action","<%= ControllerConstants.EDIT_CUSTOMER_SYSTEM_PARAMETER%>");
	var selectBox = document.getElementById("pwdTypeCombo");
	var selectedValue = selectBox.options[selectBox.selectedIndex].value;
	var selectedName = selectBox.options[selectBox.selectedIndex].text;
	var xyz='${pwdPolicyDescriptionDB}';
	$('#passwordType').val($('#pwdTypeCombo').val());	
	document.getElementById("CpasswordType").value=selectedName;
	document.getElementById("CpasswordPolicyNameCustom").value=xyz;
	$("#email-system-parameter-form").submit();
}

function uploadEmailFooterImage2(logoType,logoId){
	resetWarningDisplay();
	clearAllMessages();
	var inputFile = document.getElementById('footerImageFile');
	var file = inputFile.files[0];
	var selectBox = document.getElementById("pwdTypeCombo");
	$("#emailFooterImageId").val(logoId);
	$("#logoTypeImage").val(logoType);
	var selectedValue = selectBox.options[selectBox.selectedIndex].value;
	var selectedName = selectBox.options[selectBox.selectedIndex].text;
	var xyz='${pwdPolicyDescriptionDB}';
	$('#passwordType').val($('#pwdTypeCombo').val());	
	document.getElementById("CLpasswordType").value=selectedName;
	document.getElementById("CLpasswordPolicyNameCustom").value=xyz;	
	resetWarningDisplayPopUp();
	if (file == null) {
		showErrorMsg("<spring:message code='system.parameter.email.param.image.missing.error' ></spring:message>");
	}else {
		if(!ValidateImageExtension(file.name)){
			showErrorMsg("<spring:message code='failed.file.extension.message'></spring:message>");
	    	return;
		}
		i = 0;
		count = 0;
		$("#fieldList").empty();
		var fileName = file.name;
		if(fileName != null && fileName != undefined && fileName != ""){
			var oMyForm = new FormData();
			oMyForm.append("emailFooterFile", file);
			oMyForm.append("CLpasswordPolicyNameCustom", xyz);
			oMyForm.append("CLpasswordType", selectedName);
			oMyForm.append("emailFooterImageId", $("#emailFooterImageId").val());
			oMyForm.append("logoTypeImage", $("#logoTypeImage").val());
			$.ajax({
				dataType : 'json',
				url : "editEmailFooterImage2",
				data : oMyForm,
				type : "POST",
				enctype : 'multipart/form-data',
				processData : false,
				contentType : false,
				success : function(response) {
					var responseObject = eval(response);
					if (responseObject != undefined && responseObject.code == "200") {
						showSuccessMsg("<spring:message code='system.parameter.email.param.image.upload.success'></spring:message>");
					} else if (response == undefined && response == 'undefined') {
						showErrorMsg("<spring:message code='system.parameter.email.param.image.upload.error'></spring:message>");
					} else {
						showErrorMsg("<spring:message code='system.parameter.email.param.image.upload.error'></spring:message>");
					}
				},
				error : function(response) {
					showErrorMsg("<spring:message code='system.parameter.email.param.image.upload.error'></spring:message>");
				}
			});
		}else{
			showErrorMsg("<spring:message code='system.parameter.email.param.image.missing.error'></spring:message>");			
		}
	}
}
</script>
