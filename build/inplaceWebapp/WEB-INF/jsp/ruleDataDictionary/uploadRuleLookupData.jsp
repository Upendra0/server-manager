<%@page import="com.elitecore.sm.common.constants.BaseConstants"%>
<%@page import="com.elitecore.sm.common.constants.FormBeanConstants"%>
<%@taglib 	uri="http://www.springframework.org/tags" 			prefix="spring"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="elitecore" %> 
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="/WEB-INF/ShowErrorTag.tld" prefix="elitecoreError"%>
<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>


<!-- Pathlist Delete popup code start here -->
    	<div id="divMessage" style=" width:100%; display: none;" >
		    <div class="modal-content">
		        <div class="modal-header padding10" >
		           <h4 class="modal-title" id="status-title" ><spring:message code="rule.data.mgmt.upload.data.header.label"></spring:message></h4>
		        </div>
		        
		        <div class="modal-body padding10 inline-form">
		        	<span id="deleteDriverResponseMsg" style="display:none;">
		        		<jsp:include page="../common/responseMsgPopUp.jsp" ></jsp:include>
		        		<jsp:include page="../service/collection/ftp/deviceDialog.jsp"></jsp:include>
		        	</span>
			        <p id="deleteWarningMessage">
			       		 <spring:message code="rule.lookup.data.override.warn.message"></spring:message>
			        </p>
		        </div>
		        
		       <!-- To Do add role here -->
		       <sec:authorize access="hasAuthority('UPDATE_RULE_DATA_CONFIG')">
			        <div id="inactive-driver-div" class="modal-footer padding10">
			            <button type="button" class="btn btn-grey btn-xs " id = "upload_data" onclick="uploadRuleLookupData('<%=ControllerConstants.UPLOAD_RULE_DATA%>');"><spring:message code="btn.label.yes"></spring:message></button>
			            <button type="button" class="btn btn-grey btn-xs " data-dismiss="modal" onclick="closeFancyBox();"><spring:message code="btn.label.no"></spring:message></button>
			        </div>
			     </sec:authorize>   
			         <div class="modal-footer padding10" id="reaload-driver-details" style="display:none;">
			            <button type="button" class="btn btn-grey btn-xs " data-dismiss="modal" onclick="closeFancyBox()();"><spring:message code="btn.label.close"></spring:message></button>
			        </div>
			        
			        <div id="activate_full_upload_processbar" class="modal-footer padding10 text-left" style="display: none;">
						<img src="img/processing-bar.gif">
					</div>
					
		    </div>
		    <!-- /.modal-content --> 
	</div>
   	<a href="#divMessage" class="fancybox" style="display: none;" id="upload_delete_link">#</a>
    	<!-- Pathlist Delete popup code end here -->



	<div class="modal-content">
					<div class="modal-header padding10">
						<h4 class="modal-title">
							<span id="upload_label"><spring:message code="rule.data.mgmt.upload" ></spring:message></span>
						</h4>
					</div>
				<div class="modal-body padding10 inline-form">
					<div id="UploadPopUpMsg">
						<jsp:include page="../common/responseMsgPopUp.jsp" ></jsp:include>
					</div>
			<!-- start of upload description-->
					<div class="col-md-12 no-padding" id="divImport">
							<div class="box-body inline-form accordion-body collapse in"
								style="padding: 0px;">
							<!-- 	<div class="col-md-6 no-padding" style="height: 45px"> -->
			                          	<div class="input-group">
				                          	<div class="fullwidth">
				                          	Upload & Overwrite : Newly uploaded records will replace existing reference data records.
				                          		<div class="table-cell-label"></div>	
				             		 		</div>			             				 
			                          	</div> 							
			                          	<div class="input-group">
				                          	<div class="fullwidth">
				                          	Upload & Append : Newly uploaded records will get added to existing reference data records.
				                          		<div class="table-cell-label"></div>	
				             		 		</div>			             				 
			                          	</div>							
			                          	<div class="input-group">
				                          	<div class="fullwidth">
				                          	Upload & Update : Existing records with same primary key gets updated and new records get appended.
				                          		<div class="table-cell-label"></div>	
				             		 		</div>			             				 
			                          	</div>
			                          <div class="form-group">
			                          <div class="input-group">
				                          	<div class="fullwidth">
				                          		<div class="table-cell-label"></div>	
				             		 		</div>			             				 
			                          	</div>
			                          </div>	
								<!-- </div> -->
							</div>
						</div>
			<!-- end of upload description-->
					<form action="<%= ControllerConstants.DOWNLOAD_LOOKUP_DATA_FILE%>" method="POST" id="sample_lookup_table_form">

 
					<input id="uploadTableId" name="downloadTableId" style="display: none;" cssClass="form-control table-cell input-sm" tabindex="4"
									data-toggle="tooltip" data-placement="bottom"/>
									
					<input type="hidden" id="includeDataForSampleFile" name="includeData" value="false" />
						<div class="col-md-6 no-padding">
							<div class="form-group">
									<spring:message code="rule.data.mgmt.table.name" var="tooltip" ></spring:message>
									<spring:message code="rule.data.mgmt.table.name" var="label" ></spring:message>
									<div class="table-cell-label">${label}<span class="required">*</span></div>
									<div class="input-group">
									<input type="text" name="downloadTableName" id="uploadViewName" class="form-control table-cell input-sm"
										data-placement="bottom"/>
							</div>
						</div>
						</div>
						<div class="col-md-6 no-padding">
							<div class="form-group">
									<spring:message code="rule.data.mgmt.desc"
										var="tooltip" ></spring:message>
									<spring:message code="rule.data.mgmt.desc"
									var="label" ></spring:message>
									<div class="table-cell-label">${label}</div>
									<div class="input-group">
									<input type="text" name="uploadDescription" id="uploadDescription"
										class="form-control table-cell input-sm"
										data-placement="bottom"/>
									</div>
							</div>
					</div>
						<div class="col-md-12 no-padding"
							id="divImport">
							<div class="box-body inline-form accordion-body collapse in"
								style="padding: 0px;">
								<div class="col-md-6 no-padding" style="height: 45px">
								<spring:message code="rule.data.mgmt.upload.file.label" var="tooltip" ></spring:message>
								<div class="form-group">
								<div class="table-cell-label">${tooltip}<span class="required">*</span></div>
							
			                          	<div class="input-group">
			                          	<div class="fullwidth" id="control">
			                          		<input type="file" class="filestyle form-control" tabindex="14" data-buttonText="<spring:message code="serviceMgmt.browse.file"></spring:message>"
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
				<button class="btn btn-grey btn-xs" type="button" id ="upload_lookup_data_append" 
						onclick="uploadDataConfirmationPopup('append');">
						Append
					</button>
					<button class="btn btn-grey btn-xs" type="button" id ="upload_lookup_data_overwrite" onclick="uploadDataConfirmationPopup('overwrite');">
					   Overwrite						
					</button>
					<button class="btn btn-grey btn-xs" type="button" id ="upload_lookup_data_update" onclick="uploadDataConfirmationPopup('update');">
					   Update						
					</button>
					
					<button id='close_btn' type="button" class="btn btn-grey btn-xs "
							data-dismiss="modal"
							onclick="closeFancyBox();resetCounter();clearAllMessages();clearAllMessagesPopUp();">
							<spring:message code="btn.label.close" ></spring:message>
					</button>
				</div>
				
				<div id="activate_full_license_processbar" class="modal-footer padding10 text-left" style="display: none;">
						<label>Processing Request </label> <img src="img/processing-bar.gif">
				</div>
	</div>
	
	<!-- Duplicate Data Download pop up -->
	
	<a href="#divUploadedData" class="fancybox" style="display: none;" id="uploadedData">#</a>
 		<div id="divUploadedData" style=" width:100%; display: none;" >
 			<jsp:include page="uploadedLookUpDataSummary.jsp"></jsp:include>
	</div>

<script>
var jsSpringMsg = {};
jsSpringMsg.fileNotSelectMsg= "<spring:message code='rule.lookup.file.not.selected' ></spring:message>";
jsSpringMsg.fileUploadSucessMsg = "<spring:message code='rule.data.mgmt.upload.success'></spring:message>";
var files;

var uploadedSummary ="";
var datawrittenmode="";

$(document).on("change", "#dataFile", function(event) {
	files = event.target.files;
	$('#dataFile').html(files[0].name);
});

function uploadDataConfirmationPopup(mode) {
	clearAllMessages();
	clearAllMessagesPopUp();
	var file = $("#dataFile").val();
	if(file === ''){
		showErrorMsgPopUp(jsSpringMsg.fileNotSelectMsg);
		return false;
	}else {
		datawrittenmode = mode ;
		uploadRuleLookupData('<%=ControllerConstants.UPLOAD_RULE_DATA%>');
		/* $("#upload_delete_link").click(); */
	}
}

function uploadRuleLookupData(urlAction){
	clearAllMessages();
	clearAllMessagesPopUp();
	var file = $("#dataFile").val();
	var uploadTableId = $("#uploadTableId").val();
	var uploadViewName = $("#uploadViewName").val();
	var oMyForm = new FormData();
    oMyForm.append("file", files[0]);
    oMyForm.append("uploadTableId", uploadTableId);
    oMyForm.append("uploadViewName", uploadViewName);
    
    oMyForm.append("datawrittenmode", datawrittenmode );
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
        	 uploadedSummary = response[1];
        	 
  		 	var responseCode = responsedata.code;
  			var responseMsg = responsedata.msg;
  			if(responseCode === "200"){
  				
  			   $('#activate_full_license_processbar').hide();
  			   $('#buttonsDiv').show();
			  
  			    showSuccessMsg(jsSpringMsg.fileUploadSucessMsg);
		  			 
  			    if( summaryMODE == 'append' || summaryMODE == 'overwrite' || summaryMODE == 'update'  ){
		  			  setSummaryData( uploadedSummary, summaryMODE );
		  			  $('#uploadedData').click();
  			    }
  			    
			}else{
				 $('#activate_full_license_processbar').hide();
				 $('#buttonsDiv').show();
				 $('#activate_full_license_btn').show();
				 showErrorMsgPopUp(responseMsg);
				 /* uploadPopUp(uploadTableId); */
			}
          },
          error : function(){
        	  $('#activate_full_license_processbar').hide();
        	  $('#buttonsDiv').show();
        	  $('#activate_full_license_btn').show();
          }
      });	
}

function downloadSampleFile(){
	$("#sample_lookup_table_form").submit();
}

</script>
						
