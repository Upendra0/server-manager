<%@page import="com.elitecore.sm.common.constants.SystemParametersConstant"%>
<%@page import="com.elitecore.sm.util.MapCache"%>
<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<%@taglib 	uri="http://www.springframework.org/tags" 			prefix="spring"%>
<%@ taglib 	uri="http://www.springframework.org/security/tags" 	prefix="sec"  %>
<div class="modal-content">
		        <div class="modal-header padding10">
		            <h4 class="modal-title">Upload Complete</h4>
		        </div>

	<!-- <div id="uploadedSummaryPopUp" class="box-body table-responsive no-padding box">
	</div> -->
	     
	    <%--  <div class="col-md-6 no-padding">
							<div class="form-group">
									<spring:message code="rule.data.mgmt.table.name" var="tooltip" ></spring:message>
									<spring:message code="rule.data.mgmt.table.name" var="label" ></spring:message>
									<div class="table-cell-label">${label}<span class="required">*</span></div>
									<div class="input-group">
									<input type="text" name="uploadViewName" id="uploadViewName" class="form-control table-cell input-sm"
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
									<input type="text" name="uploadDescription" id="uploadDescription1"
										class="form-control table-cell input-sm"
										data-placement="bottom"/>
									</div>
							</div>
					</div>
			--%>	
			
			<div class="col-md-12"
							id="divImport">
							<div class="box-body inline-form accordion-body collapse in"
								style="padding: 0px;">
								  <div class="col-md-6 no-padding" style="height: 45px">  								
								 	Summary of uploaded records
				                          		<div class="table-cell-label">  </div>					             		 		 
								  </div> 
							</div>
						</div>
			
			<div class="col-md-12"
							id="divImporttotalNoRecords">
							<div class="box-body inline-form accordion-body collapse in"
								style="padding: 0px;">
								<!-- <div class="col-md-6 no-padding" style="height: 45px"> -->
								
								<div class="form-group">													
			                          	<div class="input-group">
				                          	<div class="fullwidth">				                          	
				                          		Total Number records :
				                          		<label  id="TotalRecord" ></label></div>
				                          			
				                          		<div class="table-cell-label">
				             		 		</div>			             				 
			                          	</div>
			                         </div> 	
								<!-- </div> -->
							</div>
						</div> 
			
			<div class="col-md-12"
							id="divImportValidRecords">
							<div class="box-body inline-form accordion-body collapse in"
								style="padding: 0px;">
							<!-- 	<div class="col-md-6 no-padding" style="height: 45px"> -->
								
								<div class="form-group">							
								
															
			                          	<div class="input-group">
				                          	<div class="fullwidth">
				                          	Number of valid records (Added <span id="slashUpdate">/ Updated</span>) : <label  id="ValidRecord" ></label>
				                          		<div class="table-cell-label"></div>	
				             		 		</div>			             				 
			                          	</div>
			                         </div> 	
								<!-- </div> -->
							</div>
						</div> 
				
					<div class="col-md-12"
							id="divImportDuplicateRecords">
							<div class="box-body inline-form accordion-body collapse in"
								style="padding: 0px;">
								<div class="col-md-6 no-padding">
								<%-- <spring:message code="rule.data.mgmt.upload.file.label" var="tooltip" ></spring:message> --%>
								<div class="form-group">								
			                          	<div class="input-group">
			                          	
			                          	Number of duplicate records :	<label  id="DuplicateRecord" ></label> 
			                          		<!-- <div class="table-cell-label"></div> -->
			                          		<div id="downloadcsvlink" style="display: inline;">(Download as CSV
				             				<a onclick="downloadDuplicateDataFile();">
				             				<img src="img/download-file.png" height="15" width="15">
				             				</a>)</div>
			             		 						             				 
			                          	</div>
			                         </div> 	
								</div>
							</div>
						</div> 
						
						<div class="col-md-12" id="divImportErrorRecords">
							<div class="box-body inline-form accordion-body collapse in" style="padding: 0px;">
								<div class="col-md-6 no-padding">
								<div class="form-group">								
			                          	<div class="input-group">
			                          	Number of Error records :	<label  id="ErrorRecord" ></label> 
			                          		<div id="downloadcsvlinkError" style="display: inline;">(Download as CSV
				             					<a onclick="downloadErrorDataFile();">
				             						<img src="img/download-file.png" height="15" width="15">
				             					</a>)
				             				</div>
			                          	</div>
			                         </div> 	
								</div>
							</div>
						</div> 
	        
     <div id="closeDiv" class="modal-footer padding10">
         <button type="button" class="btn btn-grey btn-xs " id="uploadedSummaryClose" data-dismiss="modal" onclick="closeFancyBox();reloadTableGridData();">
         	<spring:message code="btn.label.close"></spring:message>
         </button>
     </div>
    
     <form action="downloadlookupduplicatedata" method="POST" id="download_lookup_file_form">
		
		<input type="hidden" id="downloadFileName" name = "downloadFileName"/>
		<input type="hidden" id="downloadFileType" name = "downloadFileType"/>
	</form>
	
	
</div>

<script type="text/javascript">

var duplicateRecordFileName = "";
var errorRecordFileName = "";

function setSummaryData( uploadedSummary , summaryMODE ){
	console.log("uploadedSummary = " + uploadedSummary);
	
	var ValidRecord = uploadedSummary.ValidRecord;
	var TotalRecord = uploadedSummary.TotalRecord;
	var DuplicateRecord = uploadedSummary.DuplicateRecord;
	var ErrorRecord = uploadedSummary.ErrorRecord;
	
	duplicateRecordFileName = uploadedSummary.duplicateRecordFileName;
	errorRecordFileName = uploadedSummary.errorRecordFileName;

	$("#ValidRecord").text(ValidRecord);
	$("#TotalRecord").text(TotalRecord);
	$("#DuplicateRecord").text(DuplicateRecord);
	$("#ErrorRecord").text(ErrorRecord);
	
	if( summaryMODE == 'append'){
		$("#divImportDuplicateRecords").show();
		$("#divImportValidRecords").show();
		$("#divImporttotalNoRecords").show();
		$("#divImportErrorRecords").show();
		$("#slashUpdate").hide();
	} else if (summaryMODE == 'overwrite') {
		$("#divImportDuplicateRecords").show();
		$("#divImportValidRecords").show();
		$("#divImporttotalNoRecords").show();
		$("#divImportErrorRecords").show();
		$("#slashUpdate").hide();
	} else if (summaryMODE == 'update') {
		$("#divImportDuplicateRecords").hide();
		$("#divImportValidRecords").show();
		$("#divImporttotalNoRecords").hide();
		$("#divImportErrorRecords").show();
		$("#slashUpdate").show();
	}
	
	if( ( duplicateRecordFileName== undefined || duplicateRecordFileName== 'undefined' || duplicateRecordFileName=="" || duplicateRecordFileName== null ) ){
		$("#downloadcsvlink").hide();
	}else{
		$("#downloadcsvlink").show();
	}
	
	if( ( errorRecordFileName== undefined || errorRecordFileName== 'undefined' || errorRecordFileName=="" || errorRecordFileName== null ) ){
		$("#downloadcsvlinkError").hide();
	}else{
		$("#downloadcsvlinkError").show();
	}
}

function downloadDuplicateDataFile(){
	if( duplicateRecordFileName!="" && duplicateRecordFileName!= null && duplicateRecordFileName!= undefined ){
		$("#downloadFileName").val( duplicateRecordFileName );
		$("#downloadFileType").val("Duplicate");
		$("#download_lookup_file_form").submit();
	}
}

function downloadErrorDataFile(){
	if( errorRecordFileName!="" && errorRecordFileName!= null && errorRecordFileName!= undefined ){
		$("#downloadFileName").val( errorRecordFileName );
		$("#downloadFileType").val("Error");
		$("#download_lookup_file_form").submit();
	}
}

</script>
