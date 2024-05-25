<%@page import="com.elitecore.sm.common.constants.BaseConstants"%>
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
							<span id="upload_label"><spring:message code="dictionary.data.mgmt.fileAdd.msg" ></spring:message></span>
						</h4>
					</div>
					<div class="modal-body padding10 inline-form">
					<div id="UploadPopUpMsg" style="padding: 0px;">
						<jsp:include page="../common/responseMsgPopUp.jsp"></jsp:include>
					</div>
 				<form action="<%= ControllerConstants.DOWNLOAD_SAMPLE_ATTRIBUTE%>" method="POST" id="sample_lookup_table_form">
					<input type="hidden" id="parserType" name="parserType" value='${plugInType}'/>
					<input type="hidden" id="mappingId" name="mappingId" value='${mappingId}'/>
					<input type="hidden" id="colNamesFromGrid" name="colNamesFromGrid"/>
					<input type="hidden" id="sampleRequired" name="sampleRequired" value='YES'/>
					
					   <%-- <div class="col-md-6 no-padding">
							<div class="form-group">
									<spring:message code="dictionary.data.mgmt.parentFolder.path" var="tooltip" ></spring:message>
									<spring:message code="dictionary.data.mgmt.parentFolder.path" var="label" ></spring:message>
									<div class="table-cell-label">${label}</div>
									<div class="input-group">
									<input type="text" value="/dictionary" name="parentFolderPath" id="parentFolderPath" class="form-control table-cell input-sm" disabled/>
							</div>
						</div>
						</div> --%>
						
						<div class="col-md-6 no-padding">
							<div class="form-group">
									<spring:message code="dictionary.data.mgmt.fileUpload.path"
										var="tooltip" ></spring:message>
									<spring:message code="dictionary.data.mgmt.fileUpload.path"
									var="label" ></spring:message>
									<div class="table-cell-label">${label}</div>
									<div class="input-group">
									<%-- <input type="text" name="fileUploadPath" id="fileUploadPath"
										class="form-control table-cell input-sm"
										data-toggle="tooltip" data-placement="bottom"
									title="${tooltip }"/> --%>
									<select  name = "fileUploadPath" class="form-control table-cell input-sm"  tabindex="3" id="fileUploadPath" data-toggle="tooltip" data-placement="bottom"  title="${tooltip }">
				             		<c:forEach items="${DICT_FILE_PATH}" var="DICT_FILE_PATH">
				             			<option value="${DICT_FILE_PATH.value}">${DICT_FILE_PATH.value}</option>
				             		</c:forEach>
		             		</select>
									</div>
						    </div>
					    </div>
					
						<div class="col-md-12 no-padding"
							id="divImport" style="margin-top:0px;">
							<div class="box-body inline-form accordion-body collapse in"
								style="padding: 0px;">
								<div class="col-md-6 no-padding" style="height: 45px">
								<spring:message code="dictionary.data.mgmt.upload.file.label" var="tooltip" ></spring:message>
								<div class="form-group">
								   <div class="table-cell-label">${tooltip}<span class="required">*</span></div>
			                          <div class="input-group">
			                          	<div class="fullwidth" id="control">
				                            <input type="file" class="filestyle form-control"
						             		 id="dataFile1" name="dataFile1">
					             		 </div>
			                          </div>
			                     </div> 	
							  </div>
							</div>
						</div>
				</form>
				</div>
				<div class="modal-footer padding10" id="buttonsDiv">
				
					<% if(Boolean.TRUE.toString().equals((String)MapCache.getConfigValueAsObject(BaseConstants.KUBERNETES_ENV))){  %> 
							<button class="btn btn-grey btn-xs" type="button" id ="upload_parser_attr"  onclick="addNewFileToDictionary();"><spring:message code="dictionary.data.mgmt.add"></spring:message></button> 
					<%}else{ %> 
							<button class="btn btn-grey btn-xs" type="button" id ="upload_parser_attr"  onclick="addNewFileToDictionaryWithSync();"><spring:message code="dictionary.data.mgmt.addSync"></spring:message></button> 
					<%} %>
				
					<button id='close_btn' type="button" class="btn btn-grey btn-xs "
							onclick="$('#fileUploadPath').val($('#fileUploadPath option:first').val());closeFancyBox();clearFileContent();clearAllMessages();clearAllMessagesPopUp();">
							<spring:message code="btn.label.close" ></spring:message>
					</button>
				</div>
				<div id="processbar" class="modal-footer padding10 text-left" style="display: none;">
						<label>Processing Request </label> <img src="img/processing-bar.gif">
				</div>
	</div>
<script type="text/javascript">
function addNewFileToDictionary() {
	clearAllMessages();
	clearAllMessagesPopUp();
	var file = $("#dataFile1").val();
	if(file === ''){
		showErrorMsgPopUp(jsSpringMsg.fileNotSelectMsg);
		return false;
	}else {
		addNewFileToDictionaryFolder('<%=ControllerConstants.ADD_NEW_FILE_TO_DICTIONARY%>');
	}
}
function addNewFileToDictionaryWithSync() {
	clearAllMessages();
	clearAllMessagesPopUp();
	var file = $("#dataFile1").val();
	if(file === ''){
		showErrorMsgPopUp(jsSpringMsg.fileNotSelectMsg);
		return false;
	}else {
		addNewFileToDictionaryFolder('<%=ControllerConstants.ADD_NEW_FILE_TO_DICTIONARY_SYNC%>');
	}
}
function addNewFileToDictionaryFolder(urlAction){
	clearAllMessages();
	clearAllMessagesPopUp();
	var file = $("#dataFile1").val();
	var filePath=$("#fileUploadPath").val();
	var str=$('#selServerInstance').val();
	var ipAddress=str.substring(0, str.indexOf(":"));
	if(ipAddress== undefined || ipAddress=='-1')
			ipAddress= "0";
		
	var utilityPort=str.substring(str.indexOf(":")+1,str.length);
	if(utilityPort==undefined || utilityPort=='-1')
			utilityPort="0";
	
	var oMyForm = new FormData();
    oMyForm.append("file", files[0]);
    oMyForm.append("filePath", filePath);
    oMyForm.append("utilityPort", utilityPort);
    oMyForm.append("ServerIp", ipAddress);
    
    var summaryMODE = datawrittenmode ;
    datawrittenmode ="";
    
   $('#activate_full_license_processbar').show();
   $('#buttonsDiv').hide();
   
   /* $('#activate_full_upload_processbar').show();
   $('#inactive-driver-div').hide(); */
   
	  $.ajax({dataType : 'json',
          url : urlAction,
          data : oMyForm ,
          type : "POST",
          enctype: 'multipart/form-data',
          processData: false,
          contentType:false,
          success : function(response) {
        	 var responsedata = response[0];
        	 uploadedSummary = "File Added & Sync Successfully !";
        	 
  		 	var responseCode = response.code;
  			var responseMsg = response.msg;
  			if(responseCode === "200"){
  			  $('#activate_full_license_processbar').hide();
  			  $('#buttonsDiv').show();
  			  // showSuccessMsg(jsSpringMsg.fileUploadSucessMsg);
  			   setSummaryData(responseCode,uploadedSummary);
			  $('#uploadedData').click();
			  reloadTableGridData('true');
			}else{
				 $('#activate_full_license_processbar').hide();
				 $('#buttonsDiv').show();
				 $('#activate_full_license_btn').show();
				 showErrorMsgPopUp(responseMsg);
			}
          },
          error : function(){
        	  $('#activate_full_license_processbar').hide();
        	  $('#buttonsDiv').show();
        	  $('#activate_full_license_btn').show();
          }
      });	
}


function clearFileContent(){
	$(":file").filestyle('clear');
}
$(document).on("change", "#dataFile1", function(event) {
files = event.target.files;
	$('#dataFile1').html(files[0].name);
});
</script>
