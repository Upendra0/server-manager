<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="com.elitecore.sm.common.constants.BaseConstants"%>
<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="/WEB-INF/ShowErrorTag.tld" prefix="elitecoreError"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<div class="box box-warning">
	<div class="box-header with-border">
		<h3 class="box-title">
			<spring:message
				code="var.length.ascii.plugin.data.definition.label" ></spring:message>
		</h3>
		<div class="box-tools pull-right">
			<button class="btn btn-box-tool" data-widget="collapse">
				<i class="fa fa-minus"></i>
			</button>
		</div>
	</div>
	<div class="box-body inline-form ">
		
		<div class="col-md-6 no-padding">
			<spring:message code="var.length.ascii.plugin.data.definition.file.label" var="tooltip" ></spring:message>
			<div class="form-group">
				<div class="table-cell-label">${tooltip}</div>
				<div class="input-group ">
					<form:select path="dataDefinitionPath" cssClass="form-control table-cell input-sm" tabindex="4"
										id="dataDefinitionPath"
										data-toggle="tooltip" data-placement="bottom"
										 title="${tooltip }" >
					<form:option value="">---Select---</form:option>				 
					<c:forEach var="dataDefinitionFileEnum" items="${dataDefinitionFileEnum}">
						<form:option value="${dataDefinitionFileEnum}">${dataDefinitionFileEnum}</form:option>
					</c:forEach>
										
					</form:select>
					<spring:bind path="dataDefinitionPath">
						<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
					</spring:bind>
				</div>
			</div>
		</div>
		
	</div>
</div>
<script>
var files;
$(document).on("change","#dataDefinitionFile",function(event) {
   files=event.target.files;
   $('#dataDefinitionFile').html(files[0].name);
});
	
function clearFileContent(){
	$(":file").filestyle('clear');
}

function uploadDataDefinitionFile() {
	var parserMappingId = $("#mappingName").val();	
	var file = $("#dataDefinitionFile").val();
	var serverInstanceId = $("#instanceId").val();
	
	if (file == '') {
		showErrorMsg("<spring:message code='var.length.ascii.plugin.data.definition.no.file.select' ></spring:message>");
		//$("#importMessage").html();
		return;
	}else{
		var oMyForm = new FormData();
	    oMyForm.append("file", files[0]);
	    oMyForm.append("parserMappingId", parserMappingId);
	    oMyForm.append("serverInstanceId", serverInstanceId);
	    
	    $.ajax({
	    	  dataType : 'json',
	          url : '<%=ControllerConstants.UPLOAD_DATA_DEFINITION_FILE%>',
	          data : oMyForm ,
	          type : "POST",
	          enctype: 'multipart/form-data',
	          processData: false,
	          contentType:false,
	          success : function(response) {
	        	clearFileContent();
	  			var responseCode = response.code;
	  			var responseMsg = response.msg;
	  			var responseObject = response.object;
	  			if(responseCode === "200"){
	  				showSuccessMsg(responseMsg);
	  				if(responseObject != '' && responseObject!=undefined){
						$("#dataFileNote").show();
						responseObject = responseObject+" file uploaded successfully.";
						$("#dataFileNote").text(responseObject);
					} else {
						$("#dataFileNote").hide();
					}
				}else{
					showErrorMsg(responseMsg);
				}
	          },
	          error : function(response){
	        	  clearFileContent();
	          }
	      });	
	}
	
}

function getDataDefinitionFileName(mappingId,parserType){
	var mappingId = $("#mappingName").val();
	if(parserType == "VARIABLE_LENGTH_ASCII_PARSING_PLUGIN" || parserType == "VARIABLE_LENGTH_BINARY_PARSING_PLUGIN"){
		$.ajax({
			url: '<%=ControllerConstants.GET_DATA_DEFINITION_FILE_NAME%>',
			cache: false,
			async: false,
			dataType:'json',
			type:'POST',
			data:
			 {
				"mappingId"   : mappingId
			 }, 
			success: function(data){
				var response = eval(data);
				var responseCode = response.code; 
				var responseMsg = response.msg; 
				var responseObject = response.object;
				if(responseCode === "200"){
					if(responseObject != '' && responseObject!=undefined){
						$("#dataFileNote").show();
						responseObject = responseObject+" is already uploaded.";
						$("#dataFileNote").text(responseObject);
					} else {
						$("#dataFileNote").hide();
					}
				}else{
					showErrorMsg(responseMsg);	
				}
			},
		    error: function (xhr,st,err){
				handleGenericError(xhr,st,err);
			}
		}); 
	}
}
</script>
