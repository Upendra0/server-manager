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
						<jsp:include page="../../common/responseMsgPopUp.jsp"></jsp:include>
					</div>
 				<form action="<%= ControllerConstants.DOWNLOAD_SAMPLE_ATTRIBUTE%>" method="POST" id="sample_lookup_table_form">
					<input type="hidden" id="parserType" name="parserType" value='${plugInType}'/>
					<input type="hidden" id="mappingId" name="mappingId" value='${mappingId}'/>
					<input type="hidden" id="colNamesFromGrid" name="colNamesFromGrid"/>
					<input type="hidden" id="sampleRequired" name="sampleRequired" value='YES'/>
						<div class="col-md-12 no-padding"
							id="divImport" style="margin-top:-13px;">
							<div class="box-body inline-form accordion-body collapse in"
								style="padding: 0px;">
								<div class="col-md-6 no-padding" style="height: 45px">
								<spring:message code="rule.data.mgmt.upload.file.label" var="tooltip" ></spring:message>
								<div class="form-group">
								   <div class="table-cell-label">${tooltip}<span class="required">*</span></div>
			                          <div class="input-group">
			                          	<div class="fullwidth" id="control">
				                            <input type="file" class="filestyle form-control" tabindex="14"
						             		 id="dataFile" name="dataFile" accept=".csv">
					             		 </div>
			             				<span class='input-group-addon' style='visibility: visible !important;'> 
			             				    <a onclick="downloadSampleFile();"><img src="img/download-file.png" height="20" width="20"></a>
			             				</span>
			                          </div>
			                     </div> 	
							  </div>
							</div>
						</div>
				</form>
				</div>
				<div class="modal-footer padding10" id="buttonsDiv">
				<button class="btn btn-grey btn-xs" type="button" id ="upload_parser_attr" 
						onclick="uploadParserAttrPopup();">
						Upload
					</button>
					<button id='close_btn' type="button" class="btn btn-grey btn-xs "
							onclick="closeFancyBox();clearFileContent();clearAllMessages();clearAllMessagesPopUp();">
							<spring:message code="btn.label.close" ></spring:message>
					</button>
				</div>
				<div id="processbar" class="modal-footer padding10 text-left" style="display: none;">
						<label>Processing Request </label> <img src="img/processing-bar.gif">
				</div>
	</div>
 
 <a href="#divUploadedData" class="fancybox" style="display: none;" id="uploadedData">#</a>
 		<div id="divUploadedData" style=" width:100%; display: none;" >
 			<jsp:include page="uploadedParserAttrDataSummary.jsp"></jsp:include>
	</div>	
<script type="text/javascript">
jsSpringMsg.validFileSelectMsg="<spring:message code='parsing.attribute.invalid.file.selected' ></spring:message>";
jsSpringMsg.fileNotSelectMsg= "<spring:message code='rule.lookup.file.not.selected' ></spring:message>";
jsSpringMsg.fileUploadSucessMsg = "<spring:message code='rule.data.mgmt.upload.success'></spring:message>";

function clearFileContent(){
	$(":file").filestyle('clear');
}
function addColNames(gridName){
	clearAllMessages();
	clearAllMessagesPopUp();
	clearFileContent()
	/* passing colNames from Grid*/
	var colNamesFromGrid=$(gridName).getGridParam("colNames").slice(2);
	var index = colNamesFromGrid.indexOf('Action');
	var index2= colNamesFromGrid.indexOf('Additional Info');
	if (index > -1) {
		colNamesFromGrid.splice(index, 1);
	}
	if( index2 > -1 ){
		colNamesFromGrid.splice(index2, 1);
	}
		
	$('#colNamesFromGrid').val(colNamesFromGrid.toString());
}

function openUploadAttrPopup(gridName){
	addColNames(gridName);
	$("#uploadAttrData").click();
}
function uploadParserAttrPopup() {
	var file = $("#dataFile").val();
	var val = file.toLowerCase();
	if(file === ''){
		showErrorMsgPopUp(jsSpringMsg.fileNotSelectMsg);
		return false;
	}
	regex = new RegExp("(.*?)\.(csv)$");
	if (!(regex.test(val))) {
	    $(this).val('');
	    showErrorMsgPopUp(jsSpringMsg.validFileSelectMsg);
	    return false;
	}else{
		uploadParserAttrData('<%=ControllerConstants.UPLOAD_PARSER_ATTR_DATA%>');
	}
 }
$(document).on("change", "#dataFile", function(event) {
	files = event.target.files;
	$('#dataFile').html(files[0].name);
});
 function uploadParserAttrData(urlAction){
		var file = $("#dataFile").val();
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
	   $('#processbar').show();
	   $('#buttonsDiv').hide();
	   
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
		  				
		  			$('#processbar').hide();
		  		    $('#buttonsDiv').show();
		  		    showSuccessMsg(jsSpringMsg.fileUploadSucessMsg);
		  			var flag = '${readOnlyFlag}';
					var selMapName = '${mappingName}';
					selMappingId = '${mappingId}';
					var selectedMappingType = '${selectedMappingType}';
					reloadAttributeGridData();
					if(parserType == 'TAP_PARSING_PLUGIN'){
						reloadGroupAttributeGridData();
					}
					setSummaryData( responseCode, responseMsg,objectData);
					$('#uploadedData').click();
	  			    
				}else{
					 $('#processbar').hide();
					 $('#buttonsDiv').show();
					 reloadAttributeGridData();
					 if(parserType == 'TAP_PARSING_PLUGIN'){
							reloadGroupAttributeGridData();
					 }
					 setSummaryData( responseCode, responseMsg,objectData);
					 $('#uploadedData').click();
				}
	          },
	          error : function(){
	        	  $('#processbar').hide();
	        	  $('#buttonsDiv').show();
	          }
	      });	
	}
  
	function downloadSampleFile() {
		$("#sampleRequired").val('YES');
		$("#groupAttrId").val($("#groupId").val());
		$("#sample_lookup_table_form").submit();
	}
</script>
