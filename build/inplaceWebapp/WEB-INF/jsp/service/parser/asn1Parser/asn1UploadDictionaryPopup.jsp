<%@page import="com.elitecore.sm.common.constants.FormBeanConstants"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="com.elitecore.sm.util.MapCache"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@page
	import="com.elitecore.sm.common.constants.SystemParametersConstant"%>
<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<%@page import="com.elitecore.sm.common.constants.EngineConstants"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/security/tags"
	prefix="sec"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="/WEB-INF/ShowErrorTag.tld" prefix="elitecoreError"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<script src="${pageContext.request.contextPath}/customJS/parserManager.js<%= "?v=" + Math.random() %>" type="text/javascript"></script>
<!-- MED-8969 START : Attribute upload screen -->
 		  <div class="modal-content">
					<div class="modal-header padding10">
						<h4 class="modal-title">
							<span id="upload_label"><spring:message code="parsing.parser.attr.upload" ></spring:message></span>
						</h4>
					</div>
					<div class="modal-body padding10 inline-form">
					<div id="UploadPopUpMsg" style="padding: 0px;">
						<jsp:include page="../../../common/responseMsgPopUp.jsp"></jsp:include>
					</div>
 				<form id="sample_lookup_table_form">
						<div class="col-md-12 no-padding"
							id="divImport" style="margin-top:-13px;">
							<div class="box-body inline-form accordion-body collapse in"
								style="padding: 0px;">
								<div class="col-md-6 no-padding" style="height: 45px">
								<spring:message code="parserConfiguration.attribute.uploaddictionary.popup.file.label" var="tooltip" ></spring:message>
								<div class="form-group">
								   <div class="table-cell-label">${tooltip}<span class="required">*</span></div>
			                          <div class="input-group">
			                          	<div class="fullwidth" id="control">
				                            <input type="file" class="filestyle form-control" tabindex="14"
						             		 id="dataFile1" name="dataFile1">
					             		 </div>
			                          </div>
			                     </div> 	
							  </div>
							</div>
						</div>
				</form>
				</div>
				<div class="modal-footer padding10" id="divButtons">
				<button class="btn btn-grey btn-xs" type="button" id ="upload_parser_attr" 
						onclick="uploadParserDictPopup();">
						Upload
					</button>
					<button id='close_btn' type="button" class="btn btn-grey btn-xs "
							onclick="closeFancyBox();clearFileContent();clearAllMessages();clearAllMessagesPopUp();">
							<spring:message code="btn.label.close" ></spring:message>
					</button>
				</div>
				<div id="dictProcessbar" class="modal-footer padding10 text-left" style="display: none;">
						<label>Processing Request </label> <img src="img/processing-bar.gif">
				</div>
	</div>
 
 
<script type="text/javascript">
jsSpringMsg.validFileSelectMsg="<spring:message code='parsing.attribute.invalid.file.selected' ></spring:message>";
jsSpringMsg.fileNotSelectMsg= "<spring:message code='parsing.attribute.file.not.selected' ></spring:message>";
jsSpringMsg.fileUploadSucessMsg = "<spring:message code='rule.data.mgmt.upload.success'></spring:message>";


function uploadParserDictPopup() {
	resetWarningDisplay();
	clearAllMessages();
	var file = $("#dataFile1").val();
	var val = file.toLowerCase();
	if(file === ''){
		showErrorMsgPopUp(jsSpringMsg.fileNotSelectMsg);
		return false;
	}
	/* regex = new RegExp("(.*?)\.(csv)$");
	if (!(regex.test(val))) {
	    $(this).val('');
	    showErrorMsgPopUp(jsSpringMsg.validFileSelectMsg);
	    return false;
	}else{ */
		uploadParserDictData('<%=ControllerConstants.UPLOAD_PARSER_DICTIONARY_DATA%>');
	//}
 }

$(document).on("change", "#dataFile1", function(event) {
	files = event.target.files;
	$('#dataFile1').html(files[0].name);
});
 function uploadParserDictData(urlAction){
	 debugger;
		var file = $("#dataFile1").val();
		var parserMappingId='${mappingId}';
		var parserType='${plugInType}';
		var reqAction = '${REQUEST_ACTION_TYPE}';
		var groupId=0;
		if($("#groupId").val()!=null){
			groupId = $("#groupId").val();	
		}
		
		var oMyForm = new FormData();
	    oMyForm.append("file", files[0]);
	    oMyForm.append("parserMappingId", parserMappingId);
	    oMyForm.append("parserType", parserType);
	    oMyForm.append("actionType",reqAction );
	    oMyForm.append("groupId",groupId );
	   $('#dictProcessbar').show();
	   $('#divButtons').hide();
	   
		  $.ajax({dataType : 'json',
	          url : urlAction,
	          data : oMyForm ,
	          type : "POST",
	          enctype: 'multipart/form-data',
	          processData: false,
	          contentType:false,
	          success : function(response) {
	        	 var responsedata = response[0];
	        	 
	  		 	var responseCode = responsedata.code;
	  			var responseMsg = responsedata.msg;
	  			var objectData=responsedata.object;
	  			if(responseCode === "200"){
		  				
		  			$('#dictProcessbar').hide();
		  		    $('#divButtons').show();
		  		    showSuccessMsg(jsSpringMsg.fileUploadSucessMsg);
		  			var flag = '${readOnlyFlag}';
					var selMapName = '${mappingName}';
					selMappingId = '${mappingId}';
					var selectedMappingType = '${selectedMappingType}';
					reloadAttributeGridData();
					setSummaryData( responseCode, responseMsg,objectData);
					$('#uploadedData').click();
	  			    
				}else{
					 $('#dictProcessbar').hide();
					 $('#divButtons').show();
					 reloadAttributeGridData();
					 setSummaryData( responseCode, responseMsg,objectData);
					 $('#uploadedData').click();
				}
	          },
	          error : function(){
	        	  $('#dictProcessbar').hide();
	        	  $('#divButtons').show();
	          }
	      });	
	}
  
	
</script>
