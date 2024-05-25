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
	document.getElementById("CLpasswordType").value=selectedName;
	document.getElementById("CLpasswordPolicyNameCustom").value=xyz;
	
})
</script>
<div class="box-body padding0 mtop10">
	<div class="fullwidth mtop10">
	<form:form modelAttribute="system_param_bean" method="POST" id="custLogo-system-parameter-form" enctype="multipart/form-data">
    	<div class="box box-warning">
        	<div class="box-header with-border">
            	<h3 class="box-title"><spring:message code="systemParameter.customer.logo.section.heading" ></spring:message></h3>
                 	<div class="box-tools pull-right" style="top:-3px;">
                    	<button class="btn btn-box-tool" data-widget="collapse"><i class="fa fa-minus"></i></button>
                    </div><!-- /.box-tools -->
             </div><!-- /.box-header -->
              <div class="box-body">
              	
					<div class="fullwidth inline-form">
					<input type="hidden" id=<%=BaseConstants.REQUEST_ACTION_TYPE%> name=<%=BaseConstants.REQUEST_ACTION_TYPE%> value=<%=SystemParametersConstant.EDIT_CUST_LOGO_SYSTEM_PARAM%> />
					<input type="hidden" id="logoType" name="logoType" />
					<input type="hidden" id="custLogoId" name="custLogoId"/>
					<input type="hidden" id="CLpasswordType" name="CLpasswordType" />
					 <input type="hidden" id="CLpasswordPolicyNameCustom" name="CLpasswordPolicyNameCustom" value="${pwdPolicyDescriptionDB}" />
					<c:set var="loopcount" value="0" scope="page" ></c:set>
					<c:set var="custLogo" value="<%=SystemParametersConstant.CUSTOMER_LOGO%>" scope="page" />
					<c:set var="custLargeLogo" value="<%=SystemParametersConstant.CUSTOMER_LOGO_LARGE%>" scope="page" />
					

					<c:forEach var="systemParam" items="${system_param_bean.custLogoParamList}" varStatus="counter">
						
					<c:set var="loopcount" value="${loopcount + 1}" scope="page" ></c:set>
							
							<div class="col-md-2 mtop10" > 
								<p id="custLogoParamList[${counter.index}].name">
									<c:out value="${systemParam.name}" ></c:out>
								</p>
							</div>
								
									<c:choose>
										
										<c:when test="${systemParam.alias eq custLogo}">
										
											<div class="col-md-1 col-sm-2 col-xs-3" >
											<div id="custLogoPreview" style="height:50px;width:50px;margin-bottom:5px;border:1px solid #ccc">
												<img alt="Logo" src="${CUSTOMER_LOGO}" id="custSmallLogoImg" style="height:50px; width:50px " data-toggle="tooltip" data-placement="bottom" title="${systemParam.name}"/>
											</div>
											</div>
											<div class="col-md-3 col-sm-12 col-xs-7" style="padding-left:0;">
											<div class="form-group">
												<input type="file" class="filestyle form-control" data-buttonText="<spring:message code="serviceMgmt.browse.file"></spring:message>" id="custLogofile" name="custLogofile" accept='image/*'>
												 <spring:bind path="custLogoParamList[${counter.index}].value">
									   				<error:showError errorMessage="${status.errorMessage}"></error:showError>
									   			</spring:bind>
											</div>
											</div>
											<div class="col-md-1 col-sm-2 col-xs-3" style="padding-left:0;">
											<div class="form-group">
												<button class="btn btn-grey btn-xs " id="small-logo-upload" type="button" onclick="validateFileImage('small','${systemParam.id}');"><spring:message code="btn.label.upload" ></spring:message></button>
											</div>
											</div>
											<script type="text/javascript">
												$("#custLogofile").on("change", function(){
												
													var files = !!this.files ? this.files : [];
											    	if (!files.length || !window.FileReader) return; // no file selected, or no FileReader support

													if (/^image/.test( files[0].type)){ // only image file
														// Reference: http://stackoverflow.com/questions/19816705/javascript-jquery-size-and-dimension-of-uploaded-image-file
														var _URL = window.URL || window.webkitURL;
														var file = files[0];
														var img = new Image();
														var sizeKB = file.size / 1024;
														img.onload = function(){
															$('#custLogoPreview').html('');
															$('#custLogoPreview').append(img);
															$('#custLogoPreview img').css("height", "50px");
															$('#custLogoPreview img').css("width", "50px");
														}
														img.src = _URL.createObjectURL(file);
													}
												});
											</script>
										</c:when>
										<c:when test="${systemParam.alias eq custLargeLogo}">
									
											<div class="col-md-1 col-sm-2 col-xs-3" >
											<div id="custLargeLogoPreview" style="height:50px;width:103px;margin-bottom:5px;border:1px solid #ccc">
												<img alt="Logo" src="${CUSTOMER_LOGO_LARGE}" id="custLargeLogoImg" style="height:50px; width:100px " data-toggle="tooltip" data-placement="bottom" title="${systemParam.name}"/>
											</div>
											</div>
											<div class="col-md-3 col-sm-9 col-xs-7" style="padding-left:0;">
											<div class="form-group">
												<input type="file" class="filestyle form-control" data-buttonText="<spring:message code="serviceMgmt.browse.file"></spring:message>" id="custLargeLogofile" name="custLargeLogofile" accept='image/*'>
												 <spring:bind path="custLogoParamList[${counter.index}].value">
									   				<error:showError errorMessage="${status.errorMessage}"></error:showError>
									   			</spring:bind>
											</div>
											</div>
											<div class="col-md-1 col-sm-2 col-xs-3" style="padding-left:0;">
											<div class="form-group">
												<button class="btn btn-grey btn-xs " id="large-logo-upload" type="button" onclick="validateFileImage('large','${systemParam.id}');"><spring:message code="btn.label.upload" ></spring:message></button>
												
											</div>
											</div>
											<script type="text/javascript">
												$("#custLargeLogofile").on("change", function(){
												
													var files = !!this.files ? this.files : [];
											    	if (!files.length || !window.FileReader) return; // no file selected, or no FileReader support

													if (/^image/.test( files[0].type)){ // only image file
														// Reference: http://stackoverflow.com/questions/19816705/javascript-jquery-size-and-dimension-of-uploaded-image-file
														var _URL = window.URL || window.webkitURL;
														var file = files[0];
														var img = new Image();
														var sizeKB = file.size / 1024;
														img.onload = function(){
															$('#custLargeLogoPreview').html('');
															$('#custLargeLogoPreview').append(img);
															$('#custLargeLogoPreview img').css("height", "50px");
															$('#custLargeLogoPreview img').css("width", "100px");
														}
														img.src = _URL.createObjectURL(file);
													}
												});
											</script>
										</c:when>
										
									</c:choose>
						<div class="col-md-5">
							<p id="custLogoParamList[${counter.index}].description">
									
									 <spring:message code="system.parameter.${fn:toLowerCase(fn:replace(systemParam.name,' ', '.'))}.description" ></spring:message> 
									<%-- <c:out value="${systemParam.description}" ></c:out> --%>
									
							</p>
						</div>	
						<div class="clearfix"></div>
							<form:hidden path="custLogoParamList[${counter.index}].name" id="custLogoParamListParameterName[${counter.index}]" ></form:hidden>
							<form:hidden path="custLogoParamList[${counter.index}].description" id="custLogoParamListDescription[${counter.index}]" ></form:hidden>
							<form:hidden path="custLogoParamList[${counter.index}].value" id="custLogoParamListValue[${counter.index}]" ></form:hidden>
							<form:hidden path="custLogoParamList[${counter.index}].id" id="custLogoParamListParameterId[${counter.index}]" ></form:hidden>
							<form:hidden path="custLogoParamList[${counter.index}].alias" id="custLogoParamListAliasParameterId[${counter.index}]" ></form:hidden>
							<form:hidden path="custLogoParamList[${counter.index}].regularExpression" id="custLogoParamListRegularExpression[${counter.index}]" ></form:hidden>
							<form:hidden path="custLogoParamList[${counter.index}].errorMessage" id="custLogoParamListErrorMessage[${counter.index}]" ></form:hidden>
							<form:hidden path="custLogoParamList[${counter.index}].enabled" id="custLogoParamListenabled[${counter.index}]" ></form:hidden>
							<form:hidden path="custLogoParamList[${counter.index}].parameterGroup.id" id="custLogoParamListParameterGroupParameterGroupId[${counter.index}]" ></form:hidden>
							<form:hidden path="custLogoParamList[${counter.index}].parameterGroup.name" id="custLogoParamListParameterGroupName${counter.index}]" ></form:hidden>
							<form:hidden path="custLogoParamList[${counter.index}].parameterGroup.enabled" id="custLogoParamListParameterGroupEnabled[${counter.index}]" ></form:hidden>
							<form:hidden path="custLogoParamList[${counter.index}].displayOrder" id="custLogoParamListdisplayOrder[${counter.index}]" ></form:hidden>
							<form:hidden path="custLogoParamList[${counter.index}].createdByStaffId" id="genParamListcreatedByStaffId[${counter.index}]" ></form:hidden>
							<form:hidden path="custLogoParamList[${counter.index}].createdDate" id="genParamListcreatedDate[${counter.index}]" ></form:hidden>
							<form:hidden path="custLogoParamList[${counter.index}].image" id="custLogoParamListParameterImage[${counter.index}]" ></form:hidden>
					</c:forEach>
					<c:set var="customerLogoRowCount" value="${loopcount}" scope="request"></c:set>
				</div>
			</div>
		</div>
			
		</form:form>
	</div>
</div>

<script type="text/javascript">

function editCustLogoSystemParam(){
	
	$("#page-heading-a").html("<spring:message code='systemParameter.custLogo.edit.tab' ></spring:message>");
	var count = '${loopcount}';
	for ( var i = 0; i < count; i++) {
		document.getElementById("custLogoParamListValue[" + i + "]").disabled =false;
	}
	
	$("#custLogo-edit-btn").hide();
	$("#small-logo-upload").show();
	$("#large-logo-upload").show();
	$("#custLogofile").parent("div").show();
	$("#custLargeLogofile").parent("div").show();
}


function uploadCustLogo(logoType,logoId){
	$("#custLogoId").val(logoId);
	$("#logoType").val(logoType);
	var selectBox = document.getElementById("pwdTypeCombo");
	var selectedValue = selectBox.options[selectBox.selectedIndex].value;
	var selectedName = selectBox.options[selectBox.selectedIndex].text;
	var xyz='${pwdPolicyDescriptionDB}';
	$('#passwordType').val($('#pwdTypeCombo').val());
	console.log("value::" + selectedName);
	document.getElementById("CLpasswordType").value=selectedName;
	document.getElementById("CLpasswordPolicyNameCustom").value=xyz;
	$("#custLogo-system-parameter-form").attr("action","<%= ControllerConstants.EDIT_CUSTOMER_LOGO%>");
	$("#custLogo-system-parameter-form").submit();
	
}

function uploadEmailFooterImage(logoType,logoId){
	$("#emailFooterImageId").val(logoId);
	$("#logoTypeImage").val(logoType);
	var selectBox = document.getElementById("pwdTypeCombo");
	var selectedValue = selectBox.options[selectBox.selectedIndex].value;
	var selectedName = selectBox.options[selectBox.selectedIndex].text;
	var xyz='${pwdPolicyDescriptionDB}';
	$('#passwordType').val($('#pwdTypeCombo').val());
	console.log("value::" + selectedName);
	document.getElementById("CLpasswordType").value=selectedName;
	document.getElementById("CLpasswordPolicyNameCustom").value=xyz;
	$("#email-footer-logo-form").attr("action","<%= ControllerConstants.EDIT_EMAIL_FOOTER_IMAGE%>");
	$("#email-footer-logo-form").submit();
	
}

function validateFileImage(logoType,logoId){
	resetWarningDisplay();
	clearAllMessages();
	if(logoType=='large'){
		var file = $("#custLargeLogofile").val();
	}
	else if(logoType == 'small'){
		var file = $("#custLogofile").val();
	} else {
		var file = $("#emailFooterFile").val();
	}
	
	
	if(file == null || file=='' || file == undefined || file== 'undefined'){
	
			showErrorMsg("<spring:message code='image.no.file.select' ></spring:message>");
			
			return;
		
		    		
	}else if(!ValidateImageExtension(file)){
		
    	showErrorMsg("<spring:message code='failed.file.extension.message'></spring:message>");

    	return;
	}
	else{
    	if(logoType != 'footerImage')
			uploadCustLogo(logoType,logoId);
    	else
    		uploadEmailFooterImage(logoType,logoId);
	}
	
}
    
    
function ValidateImageExtension(sFileName) {
	
	var validFileExtensions = [".jpg", ".jpeg", ".bmp", ".gif", ".png"];
	var blnValid = false;
            
            if (sFileName.length > 0) {
                for (var j = 0; j < validFileExtensions.length; j++) {
                    var sCurExtension = validFileExtensions[j];
                    if (sFileName.substr(sFileName.length - sCurExtension.length, sCurExtension.length).toLowerCase() == sCurExtension.toLowerCase()) {
                        blnValid = true;
                        break;
                    }
                }
            }
            
            if (!blnValid) {
                   
                    return false;
                }
           
       
   
  
    return blnValid;
}
                      
</script>

