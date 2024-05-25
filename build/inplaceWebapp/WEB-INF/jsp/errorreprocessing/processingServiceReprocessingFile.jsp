<%@page import="com.elitecore.sm.common.constants.BaseConstants"%>
<%@page import="com.elitecore.sm.common.constants.EngineConstants"%>
<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<%@page import="com.elitecore.sm.common.constants.SystemParametersConstant"%>
<%@page import="com.elitecore.sm.util.MapCache"%>
<%@taglib 	uri="http://www.springframework.org/tags" 			prefix="spring"%>
<%@ taglib 	uri="http://www.springframework.org/security/tags" 	prefix="sec"  %>
<%@taglib uri="/WEB-INF/ShowErrorTag.tld" prefix="elitecoreError" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<script type='text/javascript' src="${pageContext.request.contextPath}/js/jquery.ajaxfileupload.js"></script>
<script type='text/javascript' src="${pageContext.request.contextPath}/js/jquery.fileDownload.js"></script>
<!-- <script src="http://jqueryfiledownload.apphb.com/Scripts/jquery.fileDownload.js"></script> -->

<!-- Processing service file count details grid and status read only details start here -->
<div id="processingServiceFileDetailsDiv" style="display:none;">
	<div class="tab-content padding0 clearfix" id="processing-service-file-block">
		<div class="title2">
			<spring:message code="error.reprocess.search.files.header" ></spring:message> 
		</div>
		<div class="fullwidth borbot">
			<div class="col-md-6 inline-form" style="padding-left: 0px !important;">
	            
	            <div class="form-group">
	         		<spring:message code="file.reprocess.serverInstance.label" var="label" ></spring:message>
	         		<div class="table-cell-label">${label}</div>
	             	<div class="input-group" id="processing_server_intance"></div>
	             	<input type="hidden" id="server_instance_id" value="" /> 
	             </div>
	             <div class="form-group">
	         		<spring:message code="file.reprocess.readPath.label" var="label" ></spring:message>
	         		<div class="table-cell-label">${label}</div>
	             	<div class="input-group" id="file_read_path"></div>
	             </div>
	            
	            <div class="form-group">
	             	<spring:message code="file.reprocess.serviceInstance.label" var="label" ></spring:message>
	         		<div class="table-cell-label">${label}</div>
	             	<div class="input-group" id="processing_service_intance"></div>
	             	<input type="hidden" id="service_instance_id" value="" />
	             	<input type="hidden" id="service_type_id" value="" />
	            </div>
	            
	            <div class="form-group">
	             	<spring:message code="file.reprocess.category.label" var="label" ></spring:message>
	         		<div class="table-cell-label">${label}</div>
	             	<div class="input-group" id="processing_service_category"></div>
	            </div>
	            
	            <div class="form-group" id="processing_service_rule_div" style="display:none;">
	             	<spring:message code="file.reprocess.reason.label" var="label" ></spring:message>
	         		<div class="table-cell-label">${label}</div>
	             	<div class="input-group" id="processing_service_rule"></div>
	            </div>
			</div>
			<div class="col-md-6 inline-form">
	             <div class="form-group">
	         		<spring:message code="file.reprocess.filePath.label" var="label" ></spring:message>
	         		<div class="table-cell-label">${label}</div>
	             	<div class="input-group" id="processing_file_path"></div>
	             </div>
	             
	             <div class="form-group">
	         		<spring:message code="file.reprocess.fromDate.label" var="label" ></spring:message>
	         		<div class="table-cell-label">${label}</div>
	             	<div class="input-group" id="processing_from_date"></div>
	             </div>
	             
	             <div class="form-group">
	         		<spring:message code="file.reprocess.toDate.label" var="label" ></spring:message>
	         		<div class="table-cell-label">${label}</div>
	             	<div class="input-group" id="processing_to_date"></div>
	             </div>
			</div>
			
			<div class="clearfix"></div>
			<div class="pbottom15">
				<button id="processingBackBtn" class="btn btn-grey btn-xs" onclick="backToProcessingServiceDetails();" tabindex="7"><spring:message code="btn.label.back"  ></spring:message></button>
			</div>
		</div>
		<div class="box-body table-responsive no-padding box">
			<table class="table table-hover" id="processingFileCountDetails"></table>
	        <div id="processingFileCountDetailsPagingDiv"></div> 
	 	</div>
	 	
	 	<div class="pbottom15 form-group " id="file-details-reprocess-btn-div" style="display:none;">
	 		<sec:authorize access="hasAuthority('DIRECT_REPROCESS')">
				<button class="btn btn-grey btn-xs" id="processing-service-reprocess-btn"  tabindex="7"  onclick="processingFileDetailReprocess();"><spring:message code="btn.label.file.reprocess" ></spring:message></button>
			</sec:authorize>
			<sec:authorize access="hasAuthority('RESTORE_FILES')">
				<button id="processing-service-restore-btn" class="btn btn-grey btn-xs" onclick="processingFileDetailRestore();" tabindex="8"><spring:message code="btn.label.file.restore" ></spring:message></button>
			</sec:authorize>
			<sec:authorize access="hasAuthority('DELETE_FILE')">
				<button class="btn btn-grey btn-xs" id="processing-service-delete-btn" tabindex="9" onclick="displayFileDetailDeletePopup();" ><spring:message code="parsing.service.attr.grid.column.delete" ></spring:message></button>
			</sec:authorize>
		</div>
	</div>
</div>
<a style="display: none;" class="fancybox" id="searchFileLimitExceed" href="#divSearchFileLimitExceed"></a>
<div id="divSearchFileLimitExceed" style=" width:100%; display: none;" >
	<input type="hidden" id="searchFileLimitExceed"/>
    <div class="modal-content">
        <div class="modal-header padding10">
            <h4 class="modal-title"><spring:message code="btn.label.warning" ></spring:message></h4>
        </div>
        <div class="modal-body padding10 inline-form">
        	<jsp:include page="../common/responseMsgPopUp.jsp" ></jsp:include>			
        </div>                
        <div id="remove-buttons-div" class="modal-footer padding10">
            <button type="button" class="btn btn-grey btn-xs" id="upload_close_div" data-dismiss="modal" onclick="closeFancyBox();"><spring:message code="btn.label.close"></spring:message></button>
        </div>
    </div>
</div>

<!-- Processing service file count details grid and details end here -->

<!-- File record details editable grid html content start here -->
	<a style="display: none;" class="fancybox" id="file_detail_grid_lnk" href="#divFileRecordDetailsGrid"></a>
	<a style="display: none;" class="fancybox" id="close_manual_pop_up_lnk" href="#" onclick="closeFancyBox();"></a>
	
	<div id="divFileRecordDetailsGrid" style=" width:100%; display: none;" >
	    <div class="modal-content">
	        <div class="modal-header padding10">
	            <h4 class="modal-title"> 
	            	<span id="processing_file_name_header"></span>
	            	<input type="hidden" value="0" id="file_record_edit_row_id" />
	            </h4>
	        </div>
	        <div class="modal-body padding10 inline-form">
					<div class="box-body table-responsive no-padding box" id="file_details_grid_div" style="display: block;">
						<table class="table table-hover" id="fileRecordDetailsGrid"> 
						</table>
					    <div id="fileRecordDetailsGridPagingDiv"></div>
					    <div id="procesing_grid_data_loading" class="modal-footer padding10 text-left" style="display: none;border-top: 0px;float: right;">
							<img src="img/processing-bar.gif">
						</div>  
					</div>
	        </div>
	        <div id="editable-grid-btn-div" class="modal-footer padding10" >
	        
	        	<sec:authorize access="hasAuthority('DIRECT_REPROCESS')">
	            	<button id=file_details_reprocess_btn type="button" class="btn btn-grey btn-xs " onclick="reprocessEditedRecords();"> <spring:message code="btn.label.reprocess"></spring:message></button>
	            </sec:authorize>	
	            <button id=file_details_cancel_btn type="button" class="btn btn-grey btn-xs " data-dismiss="modal" onclick="closeFancyBox();"><spring:message code="btn.label.cancel"></spring:message></button>
	        </div>
	        <div id="view-origine-file-btn-div" class="modal-footer padding10" style="display: none;">
	            <button id=view-origin-close-btn type="button" class="btn btn-grey btn-xs " data-dismiss="modal" onclick="closeFancyBox();"><spring:message code="btn.label.close"></spring:message></button>
	        </div>
	    </div>
	    <!-- /.modal-content --> 
	</div>

<!-- File record details editable grid html content end here -->

<!-- File upload and reprocess pop up code start here -->
<a style="display: none;" class="fancybox" id="processing_file_upload_lnk" href="#div_processing_file_upload"></a>
<div id="div_processing_file_upload" style=" width:100%; display: none;" >
	    <div class="modal-content">
	        <div class="modal-header padding10">
	            <h4 class="modal-title" id="upload_file_name"></h4>
	        </div>
	        <div class="modal-body padding10 inline-form">
	        	<jsp:include page="../common/responseMsgPopUp.jsp" ></jsp:include>
				<div class="form-group">
					<spring:message	code="serviceMgmt.import.config.popup.select.file.label" var="tooltip" ></spring:message>
					<div class="table-cell-label">${tooltip}<span class="required">*</span>
					</div>
					<div class="input-group ">
						<input type="hidden" id="file_detail_grid_row_id" value="0" />
						<input type="file" class="filestyle form-control" tabindex="14"	data-buttonText="<spring:message code="serviceMgmt.browse.file"></spring:message>"
							id="processing_upload_file" name="configFile" accept='text/xml'>
					</div>
				</div>
	        </div>
	        <div id="remove-buttons-div" class="modal-footer padding10">
	        	<sec:authorize access="hasAuthority('UPLOAD_REPROCESS_FILE')">
	            	<button id=upload_processing_reprocess_btn type="button" class="btn btn-grey btn-xs " ><spring:message code="btn.label.upload.reprocess"></spring:message></button>
	            </sec:authorize>
	            <button id=upload_processing_cancel_btn type="button" class="btn btn-grey btn-xs " data-dismiss="modal" onclick="closeFancyBox();"><spring:message code="btn.label.cancel"></spring:message></button>
	        </div>
	    </div>
	    <!-- /.modal-content --> 
	</div>

<!-- File upload and reprocess pop up code end here -->

<!--Warning message popup for not selected file start here--> 
<a href="#div_processing_file_message" class="fancybox" style="display: none;" id="processing_file_message">#</a>
<div id="div_processing_file_message" style=" width:100%; display: none;" >
		    <div class="modal-content">
		        <div class="modal-header padding10">
		            <h4 class="modal-title"><spring:message code="serverManagement.warn.popup.header"></spring:message></h4>
		        </div>
		        <div class="modal-body padding10 inline-form">
			        <p>
			        	<p> <spring:message code="file.reprocess.grid.checkbox.validation.on.reprocess"></spring:message></p> 
			        </p>
		        </div>
		        <div class="modal-footer padding10">
		            <button id="processing_no_selection_file_close_btn" type="button" class="btn btn-grey btn-xs " data-dismiss="modal" onclick="closeFancyBox();"><spring:message code="btn.label.close"></spring:message></button>
		        </div>
		    </div>
		    <!-- /.modal-content --> 
		</div>
<!-- Warning message poupu for not selected file code end here -->

<!-- Delete processing file pop-up div start here -->
<a href="#div_processing_file_delete" class="fancybox" style="display: none;" id="processing_file_delete_lnk">#</a>
<div id="div_processing_file_delete" style=" width:100%; display: none;" >
    <div class="modal-content">
        <div class="modal-header padding10">
            <h4 class="modal-title"><spring:message code="label.confirmation.header"></spring:message></h4>
        </div>
        <div class="modal-body padding10 inline-form">
            <p><spring:message code="file.reprocess.grid.checkbox.confirmation.on.delete"></spring:message></p>
        </div>
        <div class="modal-footer padding10">
        
        	<sec:authorize access="hasAuthority('DELETE_FILE')">
        		<button type="button" class="btn btn-grey btn-xs " id = "delete_search_files_btn" onclick="deleteProcessingFiles();" > <spring:message code="btn.label.yes" ></spring:message></button>
           		<button type="button" class="btn btn-grey btn-xs " id = "delete_files_details_btn" onclick="deleteProcessingFileDetails();" style="display:none;"><spring:message code="btn.label.yes" ></spring:message></button>
        	</sec:authorize>
            <button type="button" class="btn btn-grey btn-xs " id="close_delete_confirm_div" data-dismiss="modal" onclick="closeFancyBox();"><spring:message code="btn.label.no" ></spring:message></button>
        </div>
    </div>
    <!-- /.modal-content --> 
</div>
<!-- Delete processing file pop-up div end here -->
<a href="#divProcessingBatchResponse" class="fancybox" style="display: none;" id="processing_batch_success_lnk"></a>
<!-- Reprocess success modal content start  -->
<div id="divProcessingBatchResponse" style="width: 100%; display: none;">
	<div class="modal-content">
		<div class="modal-header padding10">
			<h4 class="modal-title">
				<spring:message code="file.reprocess.reprocessFiles.label" ></spring:message>
			</h4>
		</div>
		<div class="modal-body padding10 inline-form">
			<p>
				<spring:message code="file.reprocess.batch.success" ></spring:message>
			</p>
			<p>
				<spring:message code="file.reprocess.batchId.message" ></spring:message> : <span id="batchId"></span>
				<input type="hidden" value="0" id="response_batch_id" />
			</p>
			<p>
				<spring:message code="file.reprocess.continue.message" ></spring:message>
			</p>
			<p>
				<spring:message code="file.reprocess.viewStatus.message" ></spring:message>
			</p>
		</div>
		<div class="modal-footer padding10" id = "batch-view-status-response-div" style="display:block;">
			<button type="button" class="btn btn-grey btn-xs " id="continue_btn_go_to_back"	onclick="goToSearchScreen();closeFancyBox();" style="display:none;">	<spring:message code="btn.label.file.reprocess.continue" ></spring:message></button>
			
			<button type="button" class="btn btn-grey btn-xs " id="continue_btn"	onclick="closeFancyBox();">	<spring:message code="btn.label.file.reprocess.continue" ></spring:message></button>
			<sec:authorize access="hasAnyAuthority('VIEW_FILE_STATUS')">
				<button type="button" class="btn btn-grey btn-xs " data-dismiss="modal" onclick="viewFileStatus();"><spring:message code="btn.label.file.reprocess.viewStatus" ></spring:message></button>
			 </sec:authorize> 
		</div>
		<div class="modal-footer padding10" id="batch-preview-response-div" style="display: none;">
		
			<sec:authorize access="hasAnyAuthority('PREVIEW_FILE')">
			<button type="button" class="btn btn-grey btn-xs " 	onclick="previewAppliedRuleFiles();"><spring:message code="btn.label.preview" ></spring:message></button>
			
			</sec:authorize>
		</div>
		
	</div>
</div>
<!-- Reprocess success modal content end -->
<a href="" id="download_url"></a>


<script type="text/javascript">

function getFileRecords(){
	var fileGridData = jQuery("#fileRecordDetailsGrid").jqGrid('getRowData');
}

var oldGrid = '';
function loadProcessingServiceGridDetails(serviceIds, serverInstanceIdToServiceListObject){
	
	var count = jQuery('#processingServiceGrid').jqGrid('getGridParam', 'reccount');
	if(count && count > 0) {
		reloadProcessingServiceGrid('processingServiceGrid', 'getProcessingServiceByRule',myCustomObjectForSMAction);
	} else {
		$("#processingServiceGrid").jqGrid(
			{
				url: "getProcessingServiceByRule",
	        	postData:{
	        		'fromDate': function () {
	        			var dateTime = new Date($("#fromDatePicker").datepicker("getDate"));
	        			var strDateTime =  dateTime.getDate() + "-" + (dateTime.getMonth()+1) + "-" + dateTime.getFullYear();
	        			return strDateTime;
	   	    		},
	   	    		'toDate': function () {
	   	    			var dateTime = new Date($("#toDatePicker").datepicker("getDate"));
	        			var strDateTime =  dateTime.getDate() + "-" + (dateTime.getMonth()+1) + "-" + dateTime.getFullYear();
	        			return strDateTime;
	   	    		},
	   	    		'fileNameContains': function(){
	   	    			return $("#fileNameContains").val();
	   	    		},
	   	    		'category': function () {
	        	        return $("#category").val();
	   	    		},
	   	    		'ruleId': function () {
	   	    			var ruleId = $("#ruleList").val();
	   	    			if(ruleId == 'undefined' || ruleId == 'null' || ruleId == null || ruleId == ''){
	   	    				ruleId = 0;
	   	    			}
	        	        return ruleId;
	   	    		},
	   	    		'serviceInstanceIds': myCustomObjectForSMAction.join()
	    		},
				datatype : "json",
				jsonReader: { repeatitems: false, root: "rows", page: "page", total: "total", records: "records"},
				colNames : [	"#",
				            	"",
				            	"",
				            	"<spring:message code='processing.jqgrid.server.instance.header'></spring:message>",
				            	"<spring:message code='processing.jqgrid.service.instance.header'></spring:message>",
				            	"<spring:message code='processing.jqgrid.server.ip.port.header'></spring:message>",
				            	"<spring:message code='processing.jqgrid.server.file.path.header'></spring:message>",
				            	"<spring:message code='processing.jqgrid.server.file.count.header'></spring:message>",
				            	"ServerInstanceId",
				            	"ServiceId",
				            	"RuleId",
				            	"File Details",
				            	"ServiceTypeId",
				            	"Read FilePath",
				            	"PathId"
				           ],
				colModel : [
								{name : '',	index : '', sortable:false, formatter :processingCheckBoxFormatter},
								{name : 'id',	index : 'id', sortable:false,hidden:true },
								{name : 'ruleId',index : 'ruleId',hidden : true},
				            	{name : 'serverInstanceName',serverInstanceName : 'serverInstanceName',hidden : false},
				            	{name : 'serviceName',index : 'serviceName',hidden : false},
				            	{name : 'serverIpPort',index : 'serverIpPort',hidden : false},
				            	{name : 'filePath',index : 'filePath',hidden : false,formatter:processingFilePathColumnFormatter,align:'center'},
				            	{name : 'fileCount',index : 'fileCount',hidden : false,formatter:processingFileCountColumnFormatter,align:'center'},
				            	{name : 'serverInstanceId',index : 'serverInstanceId',hidden : true},
				            	{name : 'serviceId',index : 'serviceId',hidden : true},
				            	{name : 'ruleAlias',index : 'ruleAlias',hidden : true},
				            	{name : 'fileDetails',index : 'fileDetails',hidden : true,formatter:processingFileDetailsColumnFormatter,align:'center'},
				            	{name : 'serviceTypeId',index : 'serviceTypeId',hidden : true},
				            	{name : 'readFilePath',index : 'readFilePath',hidden : true},
				            	{name : 'pathId',index : 'pathId',hidden : true}
						   ],
				rowNum : 'records',
				rowList: [],        // disable page size dropdown
           	    pgbuttons: false,   // disable page control like next, back button
           	    pgtext: "",       // disable pager text like 'Page 0 of 10'
           	    viewrecords: false,  // disable current view record text like 'View 1-10 of 100'
				height : 'auto',
				mtype : 'POST',
				sortname : 'id',
				sortorder : "desc",
				//pager : "#processingServiceGridPagingDiv",
				contentType : "application/json; charset=utf-8",
				viewrecords : false,
				multiselect : false,
				timeout : 120000,
				loadtext : "Loading...",
				caption : "Loading",
				beforeRequest : function() {
					if(oldGrid != ''){
	            		$('#processingServiceGrid tbody').html(oldGrid);
	            	}
					$(".ui-dialog-titlebar").hide();
				},
				beforeSend : function(xhr) {
					xhr.setRequestHeader("Accept", "application/json");
					xhr.setRequestHeader("Content-Type", "application/json");
				},
				loadComplete : function(data) {
					$(".ui-dialog-titlebar").show();
					if ($('#processingServiceGrid').getGridParam('records') === 0) {
						oldGrid = $('#processingServiceGrid tbody').html();
						$('#processingServiceGrid tbody').html("<div style='padding:6px;background:#D8D8D8;text-align:center;'>No Record found</div>");
						$("#processingServiceGridPagingDiv").hide();
					} else {
						$("#processingServiceGridPagingDiv").show();
						ckIntanceSelected = new Array();
					}
				},
				loadError : function(xhr, st, err) {
					handleGenericError(xhr, st, err);
				},
				beforeSelectRow : function(rowid, e) {
					return false;
				},
				onSelectAll : function(id, status) {
				},
				gridComplete : function() {
					resetWarningDisplay();
					clearAllMessages();
					
					var finalServerInstaceObjArray  =  getAllUniqueServerInstanceDetails();
					
					var rowCount = jQuery("#processingServiceGrid").jqGrid('getGridParam', 'records');
					 if(rowCount > 0) {
						$("#processing-reprocess-buttons-div").show();
						$("#file-reprocess-buttons-div").hide();
						// make map of serverInstance to List of service and make ajax from here
						getProcessingServiceErrorDetails(finalServerInstaceObjArray);
					} else {
						$("#reprocessFilesBtn").hide();
						$("#deleteReprocessFilesBtn").hide();
					} 
				},
				recordtext: "<spring:message code="serviceManagement.grid.pager.total.records.text"></spring:message>",
		        emptyrecords: "<spring:message code="serviceManagement.grid.empty.records"></spring:message>",
				loadtext: "<spring:message code="serviceManagement.grid.loading.text"></spring:message>",
				pgtext : "<spring:message code="serviceManagement.grid.pager.text"></spring:message>",
			}).navGrid("#processingServiceGridPagingDiv", {
		edit : false,
		add : false,
		del : false,
		search : false
	});
	$(".ui-jqgrid-titlebar").hide();
  }
	count = jQuery('#processingServiceGrid').jqGrid('getGridParam', 'reccount');
	if(true){
		reloadProcessingServiceGrid('processingServiceGrid', 'getProcessingServiceByRule',myCustomObjectForSMAction);
	}
}	

function reloadProcessingServiceGrid(jqGridId,urlAction,myCustomObjectForSMAction){
	jQuery("#"+jqGridId)
    .jqGrid('setGridParam',
        { 
    		url: urlAction,
    		postData:{
        		'fromDate': function () {
        			return $("#fromDatePicker").datepicker("getDate");
   	    		},
   	    		'toDate': function () {
   	    			return $("#toDatePicker").datepicker("getDate");
   	    		},
   	    		'fileNameContains': function(){
   	    			return $("#fileNameContains").val();
   	    		},
   	    		'category': function () {
        	        return $("#category").val();
   	    		},
   	    		'ruleId': function () {
   	    			var ruleId = $("#ruleList").val();
   	    			if(ruleId == 'undefined' || ruleId == 'null' || ruleId == null || ruleId == ''){
   	    				ruleId = 0;
   	    			}
        	        return ruleId;
   	    		},
   	    		'serviceInstanceIds': myCustomObjectForSMAction.join()
	    		},
            datatype: "json"
        })
    .trigger("reloadGrid");
}



function processingCheckBoxFormatter(cellvalue, options, rowObject) {
	var rowId = rowObject["id"];
	return "<input type='checkbox' name='procesing_search'	id='rowid_"+rowId+"' onclick=getAllSelectRowIdList('"+rowId+"'); />";
}

var selectRowIdList = new Array();
function getAllSelectRowIdList(selectedRowId){
	var selectedElement = document.getElementById("rowid_"+selectedRowId);
	if (selectedElement.checked) {
		if (selectRowIdList.indexOf(selectedRowId) === -1) {
			selectRowIdList.push(selectedRowId);
		}
	} else {
		if (selectRowIdList.indexOf(selectedRowId) !== -1) {
			selectRowIdList.splice(selectRowIdList.indexOf(selectedRowId), 1);
		}
	}
}

function processingFilePathColumnFormatter(cellvalue, options, rowObject) {
	var errorPathId = "errorPath_"+rowObject["serviceId"]+"_"+rowObject["pathId"];
	return "<div id='"+errorPathId+"' align='center' ><img src='img/preloaders/circle-red.gif' align='center' width='20px' data-toggle='tooltip' data-placement='bottom' title='Loading' height='20px' style='z-index:100px;'></div>";
}

function processingFileCountColumnFormatter(cellvalue, options, rowObject) {
	
	var errorCountId = "fileCount_"+rowObject["serviceId"]+"_"+rowObject["pathId"];
	var postfix = "_"+rowObject["serviceId"]+"_"+rowObject["pathId"];
	var fileCountId = "fileCount"+postfix;
	var fileDetailsId = "fileDetails"+postfix;
	var filePathId = "errorPath"+postfix;

	var serverInstanceName = rowObject["serverInstanceName"];
	var serviceName = rowObject["serviceName"];
	
	var serviceTypeId = rowObject["serviceTypeId"];
	var serverInstanceId = rowObject["serverInstanceId"];
	var serviceId = rowObject["serviceId"];
	var readFilePath = rowObject["readFilePath"];
	
	var pathId = rowObject["pathId"]
	
	return "<div id='"+errorCountId+"' align='center' onclick=\"operProcessingServiceFileErrorDetails('"+fileDetailsId+"', '"+filePathId+"', '"+serverInstanceName+"','"+serviceName+"','"+serverInstanceId+"','"+serviceId+"','"+serviceTypeId+"', '"+readFilePath+"','"+pathId+"')\" > <a class='link' ><img src='img/preloaders/circle-red.gif' width='20px' data-toggle='tooltip' data-placement='bottom' title='Loading' height='20px' style='z-index:100px;'></a></div>";
}

function operProcessingServiceFileErrorDetails(fileDetailId, filePath, serverInstanceName,serviceName,serverInstanceId ,serviceId , serviceTypeId, readFilePath,pathId){

	var fileDetailsObj = $('#'+fileDetailId).text();
	
	$("#processing_server_intance").empty();
   	$("#processing_server_intance").html(serverInstanceName);	
   	$("#processing_service_intance").empty();
   	$("#processing_service_intance").html(serviceName);	
   	$("#processing_file_path").empty();
	$("#processing_file_path").html($('#'+filePath).text());
	$("#processing_from_date").empty();
   	$("#processing_from_date").html($("#fromDate").val());
   	$("#processing_to_date").empty();
   	$("#processing_to_date").html($("#toDate").val());
   	
   	$("#file_read_path").empty();
   	$("#file_read_path").html(readFilePath);
   	
   	$("#processing_service_rule").empty();
   	$("#processing_service_rule").html($("#ruleList option:selected").text());
   	
   	$("#processing_service_category").empty();
   	$("#processing_service_category").html($("#category").val());
   	
   	
   	$("#server_instance_id").val(serverInstanceId);
   	$("#service_type_id").val(serviceTypeId);
   	$("#service_instance_id").val(serviceId);
   	
   	
    var gridData = JSON.parse(fileDetailsObj);
    var counter = 0;
    
    $.each(gridData, function(index, value) {
    	value.serverInstanceId = serverInstanceId;
    	value.serviceId = serviceId;
    	value.serviceTypeId = serviceTypeId; 
    	value.composerId = 0;
    	value.pluginId = 0;
    	value.id = ++counter;
    	value.readPath = readFilePath;
    	value.errorPath = $('#'+filePath).text();
    	value.pathId = pathId
   	});
   	
   	
    $("#processingFileCountDetails").GridUnload();
    
   	loadFileDetailGrid(gridData);

   	$("#processing_service_div").hide();
	$("#reprocessFileSearchArea").hide();
	$("#processingServiceFileDetailsDiv").show();
	
	
	$("#delete_search_files_btn").hide();
	$("#delete_files_details_btn").show();
	
	$("#processing-reprocess-buttons-div").hide();
	$("#file-reprocess-buttons-div").show();
	
}

	function loadFileDetailRecordGrid(colModel,colNames,gridData, hideReprocessButton){
		
		var lastsel;
		jQuery("#fileRecordDetailsGrid").jqGrid({
			data : gridData,
			datatype : "local",
			colNames:colNames,
		   	colModel : colModel,
		   	loadonce: false,
		   	rowNum:10,
		   	rowList:[10,20,30],
		   	imgpath: "/image/test",
		   	pager: jQuery('#fileRecordDetailsGridPagingDiv'),
		    viewrecords: true,
			onSelectRow: function(id){
				if(id && id!==lastsel && hideReprocessButton!='view'){
					jQuery('#fileRecordDetailsGrid').restoreRow(lastsel);
					jQuery('#fileRecordDetailsGrid').editRow(id,true);
					lastsel=id;
				}
			},
			 afterEditCell:function (rowid, cellname, value, iRow, iCol){
			 }, 
			cellEdit:false,
			editurl:'clientArray',
		}).navGrid("#fileRecordDetailsGridPagingDiv",{edit:false,add:false,del:false}); 
	}
	function loadFileDetailGrid(fileDetails) {
		$("#processing-reprocess-buttons-div").hide();
		$("#file-details-reprocess-btn-div").show();
		
		$grid = $("#processingFileCountDetails");

		var count = jQuery('#processingFileCountDetails').jqGrid('getGridParam','reccount');
		 var lastsel;
		if (count && count > 0) {
			$('#processingFileCountDetails').jqGrid('clearGridData');
			jQuery("#processingFileCountDetails").jqGrid('setGridParam', {
				datatype : 'local',
				data : fileDetails,
			}).trigger("reloadGrid");
		} else {
			$grid.jqGrid({
								data : fileDetails,
								datatype : "local",
								 colNames:[
					    					  "#",
					    			          "<spring:message code='file.reprocess.detail.list.grid.column.name' ></spring:message>",
					    			          "<spring:message code='file.reprocess.detail.list.grid.column.size' ></spring:message>",
					    			          "",
					    			          "<spring:message code='file.reprocess.detail.list.grid.column.download' ></spring:message>",
					    			          "<spring:message code='file.reprocess.detail.list.grid.column.uploadAndReprocess' ></spring:message>",
					    			          "<spring:message code='error.reprocess.rule.edit.action.header' ></spring:message>",
					    			          "<spring:message code='file.reprocess.detail.list.grid.column.view' ></spring:message>",
					    			          "<spring:message code='file.reprocess.detail.list.grid.column.id' ></spring:message>",
					    					  "<spring:message code='file.reprocess.detail.list.grid.column.serverInstanceId' ></spring:message>",
					    					  "<spring:message code='file.reprocess.detail.list.grid.column.serviceId' ></spring:message>",
					    					  "<spring:message code='file.reprocess.detail.list.grid.column.serviceTypeId' ></spring:message>",
					    					  "<spring:message code='file.reprocess.detail.list.grid.column.pluginId' ></spring:message>",
					    			          "<spring:message code='file.reprocess.detail.list.grid.column.composerId' ></spring:message>",
					    			          "<spring:message code='file.reprocess.detail.list.grid.column.readPath' ></spring:message>",
					    			          "<spring:message code='file.reprocess.detail.list.grid.column.errorPath' ></spring:message>",
					    			          "",
					    			          "",
					    			          ""
					                    ],
								colModel : [ 
								             {name : "#",width : 20,formatter : processingServiceFileDetailsCountFormatter},
								             {name : "fileName",width : 250, cellattr: function (rowId, cellValue, rawObject, cm, rdata) { return  "title='"+rawObject["absoluteFileName"]+"'"  ; },},
								             {name : "fileSize",index:'fileSize',width : 110,formatter : displayFileSize},
								             {name : "fileType",width : 110,hidden:true},
								             {name : "Download",width : 110,formatter : downloadProcessingFileFormatter},
								             {name : "Upload & Reprocess",width : 110,formatter : uploadProcessingFileFormatter},
								             {name : "Edit",width : 110,formatter : editProcessingFileFormatter},
								             { name: "View", width: 110, formatter : viewProcessingFileFormatter},
								             { name: "id", width: 0, sortable:false, hidden:true },
						                     { name: "serverInstanceId", width: 0, sortable:false, hidden:true },
						                     { name: "serviceId", width: 0, sortable:false, hidden:true },
						                     { name: "serviceTypeId", width: 0, sortable:false, hidden:true },
						                     { name: "pluginId", width: 0, sortable:false, hidden:true },
						                     { name: "composerId", width: 0, sortable:false, hidden:true },
						                     { name: "readPath", width: 0, sortable:false, hidden:true },
						                     { name: "errorPath", width: 0, sortable:false, hidden:true },
						                     { name: "pathId", width: 0, sortable:false, hidden:true },
						                     { name: "absoluteFileName", width: 0, sortable:false, hidden:true },
						                     { name: "sourceFileType", width: 0, sortable:false, hidden:true }
						                     
								            ],
								height : 'auto',
								pager:'#processingFileCountDetailsPagingDiv',
								 pgbuttons: true,
								 rowList:[5,10,20,50,100], 
								sortname : 'id',
								sortorder : "asc",
								viewrecords : true,
								multiselect : false,
								loadtext : "<spring:message code="jq.grid.loading.text"></spring:message>",
								caption : "<spring:message code="file.reprocess.grid.heading" ></spring:message> ",
								beforeRequest : function() {
									$(".ui-dialog-titlebar").hide();
								},
								beforeSend : function(xhr) {
								},
								beforeSelectRow : function(rowid, e) {
								},
								loadComplete : function(data) {
									var isShowUpload = isCategoryUploadNReprocessable();
									if(!isShowUpload){
										 $('#processingFileCountDetails').jqGrid('hideCol',["Upload & Reprocess"]); 
									}
									var isShowEdit = isCategoryEditable();
									if(!isShowEdit){
										 $('#processingFileCountDetails').jqGrid('hideCol',["Edit"]); 
									}
									var isShowReprocessBtn = isShowReprocessButton();
									if(!isShowReprocessBtn){
										 $("#processing-service-reprocess-btn").hide(); 
									}else{
										$("#processing-service-reprocess-btn").show();
									}
									var isShowRestoreBtn = isShowRestoreButton();
									if(!isShowRestoreBtn){
										 $("#processing-service-restore-btn").hide(); 
									}else{
										$("#processing-service-restore-btn").show();
									}
									$(".ui-dialog-titlebar").show();
									$('.checkboxbg').removeAttr("disabled");
									$('.checkboxbg').bootstrapToggle();
									if ($('#fileCountDetails').getGridParam('records') === 0) {
										$('#processingFileCountDetails tbody').html("<div style='padding:6px;background:#D8D8D8;text-align:center;'>No record found</div>");
										$("#processingFileCountDetailsPagingDiv").hide();
									} else {
										$("#processingFileCountDetailsPagingDiv").show();
										fileReprocessCountSelected = new Array();
									}
								},
								onPaging : function(pgButton) {
									clearResponseMsgDiv();
									//clearInstanceGrid();
								},
								loadError : function(xhr, st, err) {
									handleGenericError(xhr, st, err);
								},
								beforeSelectRow : function(rowid, e) {
									return false;
								},
								onSelectAll : function(id, status) {

								},
								gridComplete : function() {
									//getErrorReprocessJsonFromEngine(serverInstanceIdToServiceListObject);
								},
								recordtext : "<spring:message code="jq.grid.pager.total.records.text"></spring:message>",
								emptyrecords : "<spring:message code="jq.grid.empty.records.text"></spring:message>",
								loadtext : "<spring:message code="jq.grid.loading.text"></spring:message>",

							}).navGrid("#processingFileCountDetailsPagingDiv", {
						edit : false,
						add : false,
						del : false,
						search : false
					});
			$(".ui-jqgrid-titlebar").hide();
			$(".ui-pg-input").height("10px");
		}
	}
	
	function isCategoryEditable(){
		var isShowEdit=true;
		var categoryVal = $('#category').val();
		if(categoryVal == 'Output' || categoryVal == 'Archive' || categoryVal == 'Input'){
			isShowEdit=false;
		} 
		return isShowEdit;
	}
	
	function isShowReprocessButton(){
		var isShowReprocessBtn=true;
		var categoryVal = $('#category').val();
		if(categoryVal == 'Output' || categoryVal == 'Archive' || categoryVal == 'Input'){
			isShowReprocessBtn=false;
		} 
		return isShowReprocessBtn;
	}
	
	function isCategoryUploadNReprocessable(){
		var isShowUpload=true;
		var categoryVal = $('#category').val();
		if(categoryVal == 'Output' || categoryVal == 'Archive' || categoryVal == 'Input'){
			isShowUpload=false;
		} 
		return isShowUpload;
	}
	
	function displayFileSize(cellvalue, options, rowObject){
		
		var fileSize = rowObject["fileSize"];
		var fileSizeInKb = getFileSizeInKb(fileSize);
		return fileSizeInKb;
	}
	
	function processingServiceFileDetailsCountFormatter(cellvalue, options, rowObject) {
		var rowId = rowObject["id"];
		var filePath = $("#filePathText").text();
		return "<input type='checkbox' name='processing_file_" + rowId+"'  id='processing_file_" + rowId+"' onclick=\"getAllFileDetailIds(\'processing_file_" + rowId+"\', \'" + rowId + "\')\"; />";
	}
	
	var processingSelectedFile = new Array();
	function getAllFileDetailIds(elementId, rowId) {
		var reprocessFileElement = document.getElementById(elementId);
		if (reprocessFileElement && reprocessFileElement.checked) {
			if (processingSelectedFile.indexOf(rowId) === -1) {
				processingSelectedFile.push(rowId);
			}
		} else {
			if (processingSelectedFile.indexOf(rowId) !== -1) {
				processingSelectedFile.splice(processingSelectedFile.indexOf(rowId), 1);
			}
		}
	} 
	
	
	function downloadProcessingFileFormatter(cellvalue, options, rowObject) {
		if(isAccessDownloadFile){
			var serverInstanceId = rowObject["serverInstanceId"];
			var fileName = rowObject["fileName"];
			fileName = encodeURIComponent(fileName);
			var absoluteFileName = rowObject["absoluteFileName"];
			absoluteFileName = encodeURIComponent(absoluteFileName);
			var fileType = rowObject["fileType"];
			return "<div align='center'>  <a href='javascript:;'   onclick=downloadErrorFile(\'"+serverInstanceId+"\',\'"+fileName+"\',\'"+absoluteFileName+"\',\'"+fileType+"\');><i class='fa fa-download' aria-hidden='true'></i> </a><div>";
		}else{
			return "<div align='center'><i class='fa fa-download' aria-hidden='true'></i><div>";	
		}
		
	}
	
		
	var processingFileName;
	var processingFileSize;
	var procesingErrorPath;
	var processingReadFilePath;
	var procesingFileAbsolutePath;
	var processingFileCompress;
	var processingSourceFileType;
	
	function uploadProcessingFileFormatter(cellvalue, options, rowObject) {
		
		if(isAccessUploadFile){
			processingFileName = rowObject["fileName"];
			processingFileSize = rowObject["fileSize"];
			processingReadFilePath = rowObject["readPath"];
			procesingErrorPath = rowObject["errorPath"];	
			procesingFileAbsolutePath = rowObject["absoluteFileName"];
			processingFileType = rowObject["fileType"];
			
			processingSourceFileType = rowObject["sourceFileType"];
			
			clearAllMessagesPopUp();
			
			
		   return "<div align='center'> <a href='#' onclick=displayUploadFilePopup(\'"+rowObject["id"]+"\'); align='center' ><i class='fa fa-upload' aria-hidden='true'></i></a></div>";
		}else{
			 return "<div align='center'> <a href='#' align='center' ><i class='fa fa-upload' aria-hidden='true'></i></a></div>";
		}
	}
	
	function editProcessingFileFormatter(cellvalue, options, rowObject) {
			
		if(isAccessModifyFile){
			var compress = rowObject["fileType"];
			var fileSize =  rowObject["fileSize"];
			fileSize = getFileSizeInKb(fileSize);
			var sourceFileType =  rowObject["sourceFileType"];
			var fileName = rowObject["fileName"];
			fileName = encodeURIComponent(fileName);
			var absoluteFileName = rowObject["absoluteFileName"];
			absoluteFileName = encodeURIComponent(absoluteFileName);
			var readPath = rowObject["readPath"];
			readPath = encodeURIComponent(readPath);
			var errorPath = rowObject["errorPath"];
			errorPath = encodeURIComponent(errorPath);
			var hide = 'hide';
			if(compress == '<%=BaseConstants.FILE_GZ_COMPRESS_EXT%>'){
				if(parseFloat(fileSize) < maxCompressFileSize) { 
					return "<div align='center'><a href='#' align='center' onclick=displayFileRecordDetails("+rowObject["serverInstanceId"]+","+rowObject["serviceId"]+","+rowObject["serviceTypeId"]+",\'"+fileName+"\',\'"+readPath+"\',\'"+errorPath+"\',\'"+rowObject["fileSize"]+"\',\'"+rowObject["id"]+"\',\'"+absoluteFileName+"\',\'"+rowObject["fileType"]+"\',\'"+sourceFileType+"\',\'"+hide+"\'); > <i class='fa fa-pencil-square-o' aria-hidden='true'></i></a></div>";
				}else{
					return "<div align='center'><i class='fa fa-pencil-square-o' aria-hidden='true'></i></div>";
				}	
			}else{
				if(parseFloat(fileSize) < maxCsvFileSize) { 
					return "<div align='center'><a href='#' align='center' onclick=displayFileRecordDetails("+rowObject["serverInstanceId"]+","+rowObject["serviceId"]+","+rowObject["serviceTypeId"]+",\'"+fileName+"\',\'"+readPath+"\',\'"+errorPath+"\',\'"+rowObject["fileSize"]+"\',\'"+rowObject["id"]+"\',\'"+absoluteFileName+"\',\'"+rowObject["fileType"]+"\',\'"+sourceFileType+"\',\'"+hide+"\'); > <i class='fa fa-pencil-square-o' aria-hidden='true'></i></a></div>";	
				}else{
					return "<div align='center'><i class='fa fa-pencil-square-o' aria-hidden='true'></i></div>";
				}
			}
		}else{
			return "<div align='center'> <i class='fa fa-pencil-square-o' aria-hidden='true'></i></div>";
		}
	}
	
	function viewProcessingFileFormatter(cellvalue, options, rowObject) {
		if(isAccessModifyFile){
			
			var compress = rowObject["fileType"];
			var fileSize =  rowObject["fileSize"];
			var sourceFileType =  rowObject["sourceFileType"];
			var fileName = rowObject["fileName"];
			fileName = encodeURIComponent(fileName);
			var absoluteFileName = rowObject["absoluteFileName"];
			absoluteFileName = encodeURIComponent(absoluteFileName);
			fileSize = getFileSizeInKb(fileSize);
			var view ='view';
			if(compress == '<%=BaseConstants.FILE_GZ_COMPRESS_EXT%>'){
				if(parseFloat(fileSize) < maxCompressFileSize) { 
					return "<div align='center'><a href='#' align='center' onclick=displayFileRecordDetails("+rowObject["serverInstanceId"]+","+rowObject["serviceId"]+","+rowObject["serviceTypeId"]+",\'"+fileName+"\',\'"+rowObject["readPath"]+"\',\'"+rowObject["errorPath"]+"\',\'"+rowObject["fileSize"]+"\',\'"+rowObject["id"]+"\',\'"+absoluteFileName+"\',\'"+rowObject["fileType"]+"\',\'"+sourceFileType+"\',\'"+view+"\');> <i class='fa fa-eye' aria-hidden='true'></i></a></div>";
				}else{
					return "<center><i class='fa fa-eye' aria-hidden='true'></i></center>";
				}	
			}else{
				if(parseFloat(fileSize) < maxCsvFileSize) { 
					return "<div align='center'><a href='#' align='center' onclick=displayFileRecordDetails("+rowObject["serverInstanceId"]+","+rowObject["serviceId"]+","+rowObject["serviceTypeId"]+",\'"+fileName+"\',\'"+rowObject["readPath"]+"\',\'"+rowObject["errorPath"]+"\',\'"+rowObject["fileSize"]+"\',\'"+rowObject["id"]+"\',\'"+absoluteFileName+"\',\'"+rowObject["fileType"]+"\',\'"+sourceFileType+"\',\'"+view+"\');> <i class='fa fa-eye' aria-hidden='true'></i></a></div>";	
				}else{
					return "<center><i class='fa fa-eye' aria-hidden='true'></i></center>";
				}
			}
		}else{
			return "<center><i class='fa fa-eye' aria-hidden='true'></i></center>";
		}
	}
	
	function displayUploadFilePopup(rowId){
		
		var rowObject = $("#processingFileCountDetails").jqGrid('getRowData', rowId);
		clearResponseMsgPopUp();
		
		processingFileName = rowObject["fileName"];
		processingFileSize = rowObject["fileSize"];
		processingReadFilePath = rowObject["readPath"];
		procesingErrorPath = rowObject["errorPath"];	
		procesingFileAbsolutePath = rowObject["absoluteFileName"];
		processingFileType = rowObject["fileType"];
		
		processingSourceFileType = rowObject["sourceFileType"];
		
		$("#file_detail_grid_row_id").val(rowId);
		clearFileContent();
		
		$("#upload_file_name").html('');
		$("#upload_file_name").html(processingFileName);
		
		$("#processing_file_upload_lnk").click();
	}
	
	$(document).on("change","#processing_upload_file",function(event) {
		   files=event.target.files;
		   $('#processing_upload_file').html(files[0].name);
	});
	
	var formData = new FormData();
	$(document).on("click","#upload_processing_reprocess_btn",function() {
		
		var file = $("#processing_upload_file").val();
		if (file == '') {
			showErrorMsgPopUp("Please upload file for further process.");
			return;
		}else{
	    	formData.append("file", files[0]);
			createJsonObjectToUpload();
			uploadProcessingFile();
		}
    });	

	var uploadReprocessFileObj = {};
	function createJsonObjectToUpload(){
		var newArray = [];
		
		var newObject = {};
	
		newObject.fileName = processingFileName;
		newObject.fileSize = getFileSizeinBytes(processingFileSize);
		newObject.absoluteFilePath = procesingFileAbsolutePath;
		
		if(processingSourceFileType == '<%=BaseConstants.FILE_GZ_COMPRESS_EXT%>'){
			newObject.isInputSourceCompress = 'true';	
		}else{
			newObject.isInputSourceCompress = 'false';
		}
		
		if(processingFileType == '<%=BaseConstants.FILE_GZ_COMPRESS_EXT%>'){
			newObject.isCompress = 'true';	
		}else{
			newObject.isCompress = 'false';
		}
		
		newObject.failureReason = "";
		
		newObject.service = {};
		newObject.service.id = $("#service_instance_id").val(); 
		newObject.svctype = {};
		newObject.svctype.id = $("#service_type_id").val();
		newObject.serverInstance = {};
		newObject.serverInstance.id = $("#server_instance_id").val();
		newObject.parser = {};
		newObject.parser.id = 0;
		newObject.composer = {};
		newObject.composer.id = 0;
		
		newObject.filePath = procesingErrorPath;
		newObject.readFilePath = processingReadFilePath;
		newArray.push(newObject);
		
		uploadReprocessFileObj["reprocessDetailList"] = newArray;
		uploadReprocessFileObj.errorProcessAction = "UPLOAD_REPROCESS";
		uploadReprocessFileObj.userComment = "";
		uploadReprocessFileObj.errorCategory = $("#category").val();
	}
	
	
	function uploadProcessingFile(){
		
		$("#upload_processing_cancel_btn").click();
		waitingDialog.show();
		
		clearResponseMsgPopUp();
		var oMyForm = new FormData();
	    oMyForm.append("file", files[0]);
	    oMyForm.append("errorReprocess", JSON.stringify(uploadReprocessFileObj));
	    oMyForm.append("serviceType", $("#search_service_type").val());
	    
	   $.ajax({dataType : 'json',
	          url : 'uploadProcessingFile',
	          data : oMyForm ,
	          type : "POST",
	          enctype: 'multipart/form-data',
	          processData: false,
	          contentType:false,
			  success: function(data){
				var responseMsg = data.msg;
				var responseObject = data.object; 
				var responseCode = data.code;
			
				waitingDialog.hide();
				
				if (responseCode === "200") {
					
					var object = JSON.parse(data.object);
	 				var selectedRowId = $("#file_detail_grid_row_id").val();
	 				
	 				var selectedFileToProcess = new Array();
	 				selectedFileToProcess.push(selectedRowId);
	 				removeReprocessedDetailsFromGrid(selectedFileToProcess, 'Upload');
	 				
	 				$("#batchId").empty();
	 				$("#batchId").html(object.id);
	 				$("#batchIdForViewStatus").val(parseInt(object.id));
	 				$("#processing_batch_success_lnk").click();
	 				
				}else{
					$("#processing_file_upload_lnk").click();
					showErrorMsgPopUp(responseMsg);
				}
		    },
		    error: function (xhr,st,err){
				handleGenericError(xhr,st,err);
			}
		});
	}
	
	var finalSelectedFileDetailJson = {};
	var selectedGridFileName;
	var selectedGridFileSize;
	function displayFileRecordDetails(serverInstanceId,serviceId,serviceTypeId,fileName,readFilePath,errorPath,
			fileSize,selectedRowId,absoluteFilePath,fileType,sourceFileType, hideReprocessButton){
		
		fileName = decodeURIComponent(fileName);
		absoluteFilePath = decodeURIComponent(absoluteFilePath);
		readFilePath = decodeURIComponent(readFilePath);
		readFilePath = decodeURIComponent(readFilePath);
		
		if(hideReprocessButton=='view'){
	        $("#file_details_reprocess_btn").hide();
		} else {
			$("#file_details_reprocess_btn").show();
		}
		
		var detailGridArray = [];
		var selectedFileDetails = {};
		
		selectedGridFileName = fileName;
		
		selectedFileDetails.fileName = fileName;
		selectedFileDetails.readFilePath = readFilePath;
		selectedFileDetails.filePath = errorPath;
		selectedFileDetails.fileSize = fileSize;
		
		selectedFileDetails.absoluteFilePath = absoluteFilePath;
		
		if(fileType == '<%=BaseConstants.FILE_GZ_COMPRESS_EXT%>'){
			selectedFileDetails.isCompress = 'true';
		}else{
			selectedFileDetails.isCompress = 'false';
		}
		if(sourceFileType == '<%=BaseConstants.FILE_GZ_COMPRESS_EXT%>'){
			selectedFileDetails.isInputSourceCompress = 'true';	
		}else{
			selectedFileDetails.isInputSourceCompress = 'false';
		}
		
		selectedGridFileSize = fileType;
		
		selectedFileDetails.failureReason = "";
		selectedFileDetails.service = {};
		selectedFileDetails.service.id = serviceId; 
		selectedFileDetails.svctype = {};
		selectedFileDetails.svctype.id = serviceTypeId;
		selectedFileDetails.serverInstance = {};
		selectedFileDetails.serverInstance.id = serverInstanceId;
		selectedFileDetails.parser = {};
		selectedFileDetails.parser.id = 0;
		selectedFileDetails.composer = {};
		selectedFileDetails.composer.id = 0;
		
		detailGridArray.push(selectedFileDetails);
		
		finalSelectedFileDetailJson["reprocessDetailList"] = detailGridArray;
		finalSelectedFileDetailJson.errorProcessAction = "DIRECT_REPROCESS";
		finalSelectedFileDetailJson.userComment = "";
		finalSelectedFileDetailJson.errorCategory = $("#category").val();
		
		$("#file_record_edit_row_id").val(selectedRowId);
		
		$("#processing_file_name_header").html(fileName);
		
		waitingDialog.show();
		
		$.ajax({
			
			url: '<%=ControllerConstants.GET_FILE_RECORD_DETAILS%>',
			cache: false,
			async: true, 
			dataType: 'json',
			type: "POST",
			data : {
				'serverInstanceId' : function() {
					return serverInstanceId;
				},
				'fileName' : function() {
					return fileName;
				},
				'filePath' : function() {
					return absoluteFilePath;
				},
				'fileType' : function() {
					return fileType;
				},
				'isDisplayLimitedRecord' : function() {
					return false;
				}
			},
			success: function(data){
				
				
				var responseCode = data.code; 
				var responseMsg = data.msg;
				var responseObject = data.object; 
				
				waitingDialog.hide();
				
				if (responseCode === "200") {
					
					var colModel = responseObject["columnModel"];
					var colNames =  responseObject["columnName"] ; 
					var gridData = responseObject["gridData"];
					
					$("#fileRecordDetailsGrid").GridUnload();
					loadFileDetailRecordGrid(colModel, colNames, gridData, hideReprocessButton);//loading dynamic grid from file detail records.
					$("#file_detail_grid_lnk").click();
					$("#divFileRecordDetailsGrid").parent().parent().parent().parent().parent().css( "z-index", "1" );
				}else{
					showErrorMsg(responseMsg);
					$("#file_details_cancel_btn").click();
				}
		   },
		    error: function (xhr,st,err){
				handleGenericError(xhr,st,err);
			}
		});
	}
	
	function clearLocalGrid(jqgridId){
		var $grid = $("#"+jqgridId);
		var rowIds = $grid.jqGrid('getDataIDs');
		// iterate through the rows and delete each of them
		for(var i=0,len=rowIds.length;i<len;i++){
			var currRow = rowIds[i];
			$grid.jqGrid('delRowData', currRow);
		}
	}
	
	
	var multipleAjaxServerInstanceArrayForService = [];
	var finalServerInstanceObjectForAjax = {};
	function getAllUniqueServerInstanceDetails() {
		var finalServerInstanceObject = {};
		var fullData = jQuery("#processingServiceGrid").jqGrid('getRowData');
		
		for (var i = 0; i < fullData.length; i++) {
			var serverInstanceAndService = {};
			serverInstanceAndService.serverInstanceId = fullData[i].serverInstanceId;
			serverInstanceAndService.serviceId = fullData[i].serviceId;
			multipleAjaxServerInstanceArrayForService.push(serverInstanceAndService);
		}
		
		$.each(	multipleAjaxServerInstanceArrayForService,function(i, item) {
							if (finalServerInstanceObjectForAjax.hasOwnProperty(item["serverInstanceId"])) {
								var existingArray = finalServerInstanceObjectForAjax[item["serverInstanceId"]];
								existingArray.push(item["serviceId"]);
								finalServerInstanceObjectForAjax[item["serverInstanceId"]] = existingArray;
							} else {
								var newArray = [];
								newArray.push(item["serviceId"]);
								finalServerInstanceObjectForAjax[item["serverInstanceId"]] = newArray;
							}
						});
		
		return finalServerInstanceObjectForAjax;
	}

	
	function processingFileDetailsColumnFormatter(cellvalue, options, rowObject) {
		var errorDetailObj = "fileDetails_" + rowObject["serviceId"] + "_"	+ rowObject["pathId"];
		return "<div  id='"+errorDetailObj+"' align='center'><img src='img/preloaders/circle-red.gif' align='center' width='20px' data-toggle='tooltip' data-placement='bottom' title='Loading' height='20px' style='z-index:100px;'></div>";
	}
	
	

	function getProcessingServiceErrorDetails(serverInstanceIdToServiceListObject) {	
		$.each(serverInstanceIdToServiceListObject, function(key, value) {
			$.ajax({
				url : 'getProcessingErrorDetails',
				cache : false,
				async : true,
				dataType : 'json',
				type : "POST",
				data : {
					'fromDate' : function() {
	        			var dateTypeVar = $('#fromDate').datepicker('getDate');
	        			
	        			return $.datepicker.formatDate('d-m-yy', dateTypeVar);
					},
					'toDate' : function() {
						var dateTypeVar = $('#toDate').datepicker('getDate');
	        			
	        			return $.datepicker.formatDate('d-m-yy', dateTypeVar);
					},
					'fileNameContains' : function() {
						return $("#fileNameContains").val();
					},
					'ruleAlias' : function() {
						return $("#ruleList option:selected").text();
					},
					'ruleId' : function() {
						return $("#ruleList").val();
					},
					'category' : function() {
						return $("#category").val();
					},
					'serviceInstanceIds' : value.join()
				},
				success : function(data) {
					var flag =false;
					$.each(data.object, function(i, item) { 						
 						if(item['isFileLimitExceed'] == true & flag==false){	 							
 							$("#searchFileLimitExceed").click();	 		
 							showErrorMsgPopUp("Count of search results is exceeding 10,000 files. Please provide addition filter/search criteria to view search results.");
 							flag=true;
 						}	 						
 					});
					
					if (data.code == "200") {
						$.each(data.object, function(i, item) {
							for ( var key in item) {
								if (item.hasOwnProperty(key)) {
									
									$("input:checkbox[id^=\""+key+"\"]").removeAttr("disabled");
									
									if (key.indexOf("fileDetails") >= 0) {
										$("#" + key).empty();
										$("#" + key).html(JSON.stringify(item[key]));
									}else if(key.indexOf("errorPath") >= 0){
										
						  				if(parseInt(item[key]) < 0){
								  			$("div[id^=\""+key+"\"]").empty();
								  			var dynamicKey = key.replace('fileDetails','errorReprocessCheckbox');
						  					$("input:checkbox[id^=\""+dynamicKey+"\"]").attr("disabled", true);
								  			$("div[id^=\""+key+"\"]").html('<%=BaseConstants.SERVICE_INSTANCE_NOT_RUNNING%>');
						  				}else{
						  					$("#"+key).empty();
						  					$("#"+key).html(item[key]);
						  				}
						  			}else if(key.indexOf("fileCount") >= 0) {
						  				$("#"+key).empty();
						  				
						  				var dynamicKey = generateCheckBoxId(key);
						  				if(parseInt(item[key]) < 0) {
								  			$("input:checkbox[id^=\""+dynamicKey+"\"]").attr("disabled", true);
								  			$("#"+key).html("<center><a style='text-decoration: underline; cursor: pointer;' id='"+key+"_href'>"+item[key]+"</a></center>");
						  					$("#"+key).html(item[key]);
						  					$("#"+key).prop('onclick',null).off('click');
						  					$("div[id^=\""+key+"\"]").html('<%=BaseConstants.SERVICE_INSTANCE_NOT_RUNNING%>');
						  				} else {
						  					if(parseInt(item[key]) == 0){
						  						$("input:checkbox[id^=\""+dynamicKey+"\"]").attr("disabled", true);
						  						$("#"+key).html("<center>"+item[key]+"</center>");	
						  						$("#"+key).prop('onclick',null).off('click');
						  					}else{
						  						$("#"+key).html("<center><a style='text-decoration: underline; cursor: pointer;' id='"+key+"_href'>"+item[key]+"</a></center>");
						  					}
						  				}
						  			} else {
						  				$("#"+key).empty();
			 		 				   	$("#"+key).html(item[key]);	
						  			}
								}
							}
						});
					} else {
						if(data && data.object) {
	 						$.each(data.object, function(i, item) {
	 							
			 				   	for (var key in item) {
				 				   if (item.hasOwnProperty(key)) {
				 					   var dynamicKey = generateCheckBoxId(key);
							  			$("input:checkbox[id^=\""+dynamicKey+"\"]").attr("disabled", true);
							  			$("div [id^=\""+key+"\"]").empty();
							  			$("div [id^=\""+key+"\"]").html(item[key]);
			 					  	}
			 					}
			 				});
	 					}
	 				}
				},
				error : function(xhr, st, err) {
					handleGenericError(xhr, st, err);
				}
			});
		});
	}
		
	
	function generateCheckBoxId(key){
		if(key.indexOf("fileDetails") >= 0) {
			key = key.replace("fileDetails", "rowid"); 
		}else if(key.indexOf("fileCount") >= 0) {
			key = key.replace("fileCount", "rowid");
		}else if(key.indexOf("errorReprocessCheckbox_") >= 0) {
			key = key.replace("errorReprocessCheckbox_", "rowid");
		}
		return key;
	}
	
	

	function backToProcessingServiceDetails() {
		$("#processing_service_div").show();
		$("#reprocessFileSearchArea").show();
		
		$("#processing-reprocess-buttons-div").show();
		$("#file-reprocess-buttons-div").hide();
		
		$("#delete_search_files_btn").show();
		$("#delete_files_details_btn").hide();
		
		
		$("#processingServiceFileDetailsDiv").hide();
	}
	
	var finalProcessingBatchObj = {};
	function reprocessProcessingFiles(redirectFlag){
		if(selectRowIdList.length == 0){
			$("#processing_file_message").click();
			return;
		}else {
			finalProcessingBatchObj = {};
			createDynamicBatchDetailObj('DIRECT_REPROCESS');
			creatBatchAndreprocessProcessingFiles(finalProcessingBatchObj,selectRowIdList,'Search',redirectFlag);
		}
	}
	
	function restoreProcessingFiles(redirectFlag){
		if(selectRowIdList.length == 0){
			$("#processing_file_message").click();
			return;
		}else {
			finalProcessingBatchObj = {};
			createDynamicBatchDetailObj('ARCHIVE_RESTORE');
			creatBatchAndRestoreProcessingFiles(finalProcessingBatchObj,selectRowIdList,'Search',redirectFlag);
		}
	}

	function creatBatchAndreprocessProcessingFiles(batchObj,selectedRowId,actionFrom,redirectFlag){
		
		waitingDialog.show();
		$.ajax({
			url: '<%=ControllerConstants.REPROCESS_PROCESSING_FILES%>',
			cache: false,
			async: true, 
			dataType: 'json',
			headers: {
			    'Content-Type': 'application/json'
			},
			type: "POST",
			data: JSON.stringify(batchObj),
			success: function(data){
				var response = eval(data);
				var responseCode = response.code;
				var responseMsg = response.msg;
				var responseObject = response.object;
				
				waitingDialog.hide();
				
				if (responseCode === "200") {
					var object = JSON.parse(data.object);
	 				$("#batchId").empty();
	 				$("#batchId").html(object.id);
	 				$("#processing_batch_success_lnk").click();
	 				$("#batchIdForViewStatus").val(parseInt(object.id));
	 				removeReprocessedDetailsFromGrid(selectedRowId, actionFrom);
	 				
	 				$("#batch-view-status-response-div").show();
	 				$("#batch-preview-response-div").hide();
	 				
	 				if(redirectFlag == 'true'){
	 					$("#continue_btn_go_to_back").show();
	 					$("#continue_btn").hide();
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
	
	function creatBatchAndRestoreProcessingFiles(batchObj,selectedRowId,actionFrom,redirectFlag){
		
		waitingDialog.show();
		$.ajax({
			url: '<%=ControllerConstants.RESTORE_PROCESSING_FILES%>',
			cache: false,
			async: true, 
			dataType: 'json',
			headers: {
			    'Content-Type': 'application/json'
			},
			type: "POST",
			data: JSON.stringify(batchObj),
			success: function(data){
				var response = eval(data);
				var responseCode = response.code;
				var responseMsg = response.msg;
				var responseObject = response.object;
				
				waitingDialog.hide();
				
				if (responseCode === "200") {
					var object = JSON.parse(data.object);
	 				$("#batchId").empty();
	 				$("#batchId").html(object.id);
	 				$("#processing_batch_success_lnk").click();
	 				$("#batchIdForViewStatus").val(parseInt(object.id));
	 				removeReprocessedDetailsFromGrid(selectedRowId, actionFrom);
	 				
	 				$("#batch-view-status-response-div").show();
	 				$("#batch-preview-response-div").hide();
	 				
	 				if(redirectFlag == 'true'){
	 					$("#continue_btn_go_to_back").show();
	 					$("#continue_btn").hide();
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
	
	
	function createDynamicBatchDetailObj(errorProcessAction){
		
		var processingArray = [];
		$.each(selectRowIdList,function(i){
			var rowData = jQuery("#processingServiceGrid").jqGrid ('getRowData', selectRowIdList[i]);
			var fileDetails = $("#fileDetails_"+rowData["serviceId"]+"_"+rowData["pathId"]).text();
			
			fileDetails = JSON.parse(fileDetails);
			
			$.each(fileDetails, function(key,value){
				
				var batchDetailsObj = {};
				batchDetailsObj.fileName = value.fileName;
				batchDetailsObj.fileSize = value.fileSize;
				batchDetailsObj.absoluteFilePath = value.absoluteFileName;
				
				if(value.fileType == '<%=BaseConstants.FILE_GZ_COMPRESS_EXT%>'){
					batchDetailsObj.isCompress = 'true';	
				}else{
					batchDetailsObj.isCompress = 'false';
				}
				
				if(value.sourceFileType == '<%=BaseConstants.FILE_GZ_COMPRESS_EXT%>'){
					batchDetailsObj.isInputSourceCompress = 'true';	
				}else{
					batchDetailsObj.isInputSourceCompress = 'false';
				}
				
				
				batchDetailsObj.failureReason = "";
				
				var isObjExists = isObjectAlreadyExists(processingArray, "service", rowData["serviceId"]) ;
				if(isObjExists) {
					batchDetailsObj.service = rowData["serviceId"];
				} else {
					batchDetailsObj.service = {};
					batchDetailsObj.service.id = rowData["serviceId"];
				}
				
				isObjExists = isObjectAlreadyExists(processingArray, "serverInstance", rowData["serverInstanceId"]) ;
				if(isObjExists) {
					batchDetailsObj.serverInstance = rowData["serverInstanceId"];
				} else {
					batchDetailsObj.serverInstance = {};
					batchDetailsObj.serverInstance.id = rowData["serverInstanceId"];
				}
				
				isObjExists = isObjectAlreadyExists(processingArray, "svctype", rowData["serviceTypeId"]) ;
				if(isObjExists) {
					batchDetailsObj.svctype = rowData["serviceTypeId"];
				} else {
					batchDetailsObj.svctype = {};
					batchDetailsObj.svctype.id = rowData["serviceTypeId"];
					
				}
				
				batchDetailsObj.readFilePath = rowData["readFilePath"];
				
				isObjExists = isObjectAlreadyExists(processingArray, "parser", 0) ;
				if(isObjExists) {
					batchDetailsObj.parser = 0;
				} else {
					batchDetailsObj.parser = {};
					batchDetailsObj.parser.id = 0;
				}
				isObjExists = isObjectAlreadyExists(processingArray, "composer", 0) ;
				if(isObjExists) {
					batchDetailsObj.composer = 0;
				} else {
					batchDetailsObj.composer = {};
					batchDetailsObj.composer.id = 0;
				}
				
				batchDetailsObj.filePath = $("#errorPath_"+rowData["serviceId"]+"_"+rowData["pathId"]).text();
				processingArray.push(batchDetailsObj);
				
			});
		});
		
		finalProcessingBatchObj["reprocessDetailList"] = processingArray;
		finalProcessingBatchObj.errorProcessAction = errorProcessAction;
		finalProcessingBatchObj.userComment = "";
		finalProcessingBatchObj.errorCategory = $("#category").val();
		
	}
	
	
	
	function displayDeletePopup(){
		if(selectRowIdList.length == 0){
			$("#processing_file_message").click();
			return;
		}else {
			$("#processing_file_delete_lnk").click();
		}                        
	}

	function deleteProcessingFiles(){
		finalProcessingBatchObj = {};
		createDynamicBatchDetailObj('DELETE_FILE');
		deleteSelectedProcessingFiles(finalProcessingBatchObj,selectRowIdList,'Search');
		
	}
	
	function deleteSelectedProcessingFiles(errorDetailObj,rowIdList,actionType){
		
		$("#close_delete_confirm_div").click();
		waitingDialog.show();
		
		$.ajax({
			url: '<%=ControllerConstants.DELETE_PROCESSING_FILE_DETAILS%>',
			cache: false,
			async: true, 
			dataType: 'json',
			headers: {
			    'Content-Type': 'application/json'
			},
			type: "POST",
			data: JSON.stringify(errorDetailObj),
			success: function(data){
				var response = eval(data);
				var responseCode = response.code;
				var responseMsg = response.msg;
				var responseObject = response.object;
				
				waitingDialog.hide();
				
				if (responseCode === "200") {
					var object = JSON.parse(data.object);
	 				
	 				$("#batchId").empty();
	 				$("#batchId").html(object.id);
	 				$("#batchIdForViewStatus").val(parseInt(object.id));
	 				$("#processing_batch_success_lnk").click();
	 				removeReprocessedDetailsFromGrid(rowIdList, actionType);
				}else{
					$("#processing_file_delete_lnk").click();
					showErrorMsgPopUp(responseMsg);
				}
			},
		    error: function (xhr,st,err){
				handleGenericError(xhr,st,err);
			}
		});
		
		
	}
	
	function processingFileDetailReprocess(){
		if(processingSelectedFile.length == 0){
			$("#reprocessMessage").click();
			return;
		}
		var finalObj = getSelectedFiles("DIRECT_REPROCESS");
		creatBatchAndreprocessProcessingFiles(finalObj,processingSelectedFile,'FileDetails','false');
	}
	
	function processingFileDetailRestore(){
		if(processingSelectedFile.length == 0){
			$("#restoreMessage").click();
			return;
		}
		var finalObj = getSelectedFiles("ARCHIVE_RESTORE");
		creatBatchAndRestoreProcessingFiles(finalObj,processingSelectedFile,'FileDetails','false');
	}

	function getSelectedFiles(errorProcessAction) {
		reprocessFilesObj = {};
		var fileArray = [];
		$.each(processingSelectedFile,function(i){
		   	var responseObject = jQuery("#processingFileCountDetails").jqGrid ('getRowData', processingSelectedFile[i]);
			
	    	var newObject = {};
			newObject.fileName = responseObject["fileName"];
			
			if(responseObject["fileType"] == '<%=BaseConstants.FILE_GZ_COMPRESS_EXT%>'){
				newObject.isCompress = 'true';	
			}else{
				newObject.isCompress = 'false';
			}
			
			if(responseObject["sourceFileType"] == '<%=BaseConstants.FILE_GZ_COMPRESS_EXT%>'){
				newObject.isInputSourceCompress = 'true';	
			}else{
				newObject.isInputSourceCompress = 'false';
			}
			
			newObject.fileSize =  parseFloat(getFileSizeinBytes(responseObject["fileSize"]));
			newObject.absoluteFilePath = responseObject["absoluteFileName"];

			newObject.failureReason = "";
			
			newObject.service = responseObject["serviceId"];
			var isObjExists = isObjectAlreadyExists(fileArray, "service", responseObject["serviceId"]) ;
			if(isObjExists) {
				newObject.service = responseObject["serviceId"];
				newObject.svctype = responseObject["serviceTypeId"];
				newObject.serverInstance = responseObject["serverInstanceId"];
			} else {
				newObject.service = {};
				newObject.service.id = responseObject["serviceId"];
				newObject.svctype = {};
				newObject.svctype.id = responseObject["serviceTypeId"];
				newObject.serverInstance = {};
				newObject.serverInstance.id = responseObject["serverInstanceId"];
			}
			isObjExists = isObjectAlreadyExists(fileArray, "parser", responseObject["pluginId"]) ;
			if(isObjExists) {
				newObject.parser = responseObject["pluginId"];
			} else {
				newObject.parser = {};
				newObject.parser.id = responseObject["pluginId"];
			}
			isObjExists = isObjectAlreadyExists(fileArray, "composer", 0) ;
			if(isObjExists) {
				newObject.composer = 0;
			} else {
				newObject.composer = {};
				newObject.composer.id = 0;
			}
			newObject.filePath = responseObject["errorPath"];
			newObject.readFilePath = responseObject["readPath"];
			fileArray.push(newObject);
			
		});
		reprocessFilesObj.reprocessDetailList = fileArray;
		reprocessFilesObj.errorProcessAction = errorProcessAction;
		reprocessFilesObj.userComment = "";
		reprocessFilesObj.errorCategory = $("#category").val();
		
		return reprocessFilesObj;
	}	
	
	function displayFileDetailDeletePopup(){
		
		if(processingSelectedFile.length == 0){
			$("#processing_file_message").click();
			return;
		}else {
			$("#delete_search_files_btn").hide();
			$("#delete_files_details_btn").show();
			$("#processing_file_delete_lnk").click();
		} 
	}
	
	function deleteProcessingFileDetails(){
		var finalObj = getSelectedFiles("DELETE_FILE");
		deleteSelectedProcessingFiles(finalObj,processingSelectedFile,'FileDetails');
		
	}
	
	
	function reprocessEditedRecords(){
		
		$("#file_details_cancel_btn").click();
		
		waitingDialog.show();
		
		var gridData=$("#fileRecordDetailsGrid").jqGrid('getGridParam','data');
		var gridHeader = jQuery("#fileRecordDetailsGrid").jqGrid("getGridParam", "colNames");
		
		var formData = new FormData();
		var finalFileRecordJson = {}
		finalFileRecordJson.fileRecords = JSON.stringify(gridData);
		
		formData.append("fileRecords", JSON.stringify(gridData));
		formData.append("errorReprocessBatch", JSON.stringify(finalSelectedFileDetailJson));
		
		formData.append('fileName',selectedGridFileName);
		formData.append('fileType',selectedGridFileSize);
		formData.append('gridHeader',JSON.stringify(gridHeader));
		
		
		
	   $.ajax({dataType : 'json',
	          url : '<%=ControllerConstants.REPROCESS_PROCESSING_FILE_RECORDS%>',
	          data : formData ,
	          type : "POST",
	          enctype: 'multipart/form-data',
	          processData: false,
	          contentType:false,
			  success: function(data){

				  var response = eval(data);
					var responseCode = response.code;
					var responseMsg = response.msg;
					var responseObject = response.object;
					
					waitingDialog.hide();
					
					if (responseCode === "200") {
						var object = JSON.parse(data.object);
		 				$("#close_manual_pop_up_lnk").click();
		 				var selectedRowId = $("#file_record_edit_row_id").val();
		 				var selectedFileToProcess = new Array();
		 				selectedFileToProcess.push(selectedRowId);
		 				removeReprocessedDetailsFromGrid(selectedFileToProcess, 'EditGrid');
		 				
		 				$("#batchId").empty();
		 				$("#batchId").html(object.id);
		 				$("#processing_batch_success_lnk").click();
		 				
		 				$("#batchIdForViewStatus").val(parseInt(object.id));
		 				$("#batch-view-status-response-div").show();
		 				$("#batch-preview-response-div").hide();
				
					}else{
						showErrorMsg(responseMsg);
						$("#file_details_cancel_btn").click();
					}
		    },
		    error: function (xhr,st,err){
				handleGenericError(xhr,st,err);
			}
		});
		
	}
	
	function removeReprocessedDetailsFromGrid(selectedRows,actionType){
		
		var selectedRowIdToProcess = new Array();
		
		if(actionType == 'Search'){
			removeSelectedRowFromGrid('processingServiceGrid', selectedRows);
		}else if(actionType == 'FileDetails' ){
			selectedRowIdToProcess = processingSelectedFile;
			processAndRemoveRowsFromGrid(selectedRowIdToProcess);
		}else if(actionType == 'Upload' || actionType == 'EditGrid' ){
			selectedRowIdToProcess = selectedRows;
			processAndRemoveRowsFromGrid(selectedRowIdToProcess);
			$('#processingFileCountDetails').jqGrid('delRowData', selectedRows);
		}
	}
	
	function processAndRemoveRowsFromGrid(selectedRowIdToProcess){
		getSelectedFileDetailsObj('processingServiceGrid', 'processingFileCountDetails', selectedRowIdToProcess, 'rowId_');
		//get all file details object from search screen
		var allFileDetails = getSelectedFileDetailsObj('processingServiceGrid', 'processingFileCountDetails', selectedRowIdToProcess);
		
		//get all file details object from detail screen
		var selectedFileDetails = getAllSelectedFileDetailsRecord('processingFileCountDetails', selectedRowIdToProcess);
		
		//remove file object of detail screen from search screen
		var newFileDetailsArray = [];
		$.each(allFileDetails, function( key1, value1) {
			var isExists = false;
			$.each(selectedFileDetails, function( key2, value2 ) {
				if(value1.fileName == value2.fileName /*&& value1.fileSize == value2.fileSize */) {
					isExists = true;
				}
			});
			
			if(!isExists) {
				newFileDetailsArray.push(value1);
			}
		});
		
		
		
		//assign new file object array to search screen
		var fileDetailsId = getProcessingFileDetailId('processingServiceGrid', 'processingFileCountDetails', selectedRowIdToProcess, '');
		
		$("#"+fileDetailsId).empty();
		$("#"+fileDetailsId).html(JSON.stringify(newFileDetailsArray));
		
		
		//assign new file count in search screen
		var fileCountId = getProcessingFileCountId('processingServiceGrid', 'processingFileCountDetails', selectedRowIdToProcess, '');  
		var fileCounts = $("#"+fileCountId).text();
		fileCounts = parseInt(fileCounts);
		fileCounts = fileCounts-selectedRowIdToProcess.length;
		$("#"+fileCountId).empty();
		$("#"+fileCountId).html("<a class='link' style='text-decoration: underline; cursor: pointer;color:orange;'> "+fileCounts+"</a>");
		
		//if new file count is 0 in search screen than disable checkbox of that row, and
		// change style of a tag : remove hyperlink style
		if(fileCounts == 0) {
			//disable checkbox
			var checkboxId = getProcessingSearchGridCheckBoxId('processingServiceGrid', 'processingFileCountDetails', selectedRowIdToProcess, '');
			
			$('#'+checkboxId).attr('disabled', true);
			
			
			$("#"+fileCountId).removeAttr('style');
			
			//remove style from div tag
			var fileCountDivId = getProcessingFileCountDivId('processingServiceGrid', 'processingFileCountDetails', selectedRowIdToProcess, '');
			$("#"+fileCountDivId).removeAttr('style');
			$("#"+fileCountDivId).prop('onclick',null);
			//add black color to a tag
			$("#"+fileCountId).css('color','#222222');
		}
		
		//delete selected row in detail screen
		removeSelectedRowFromGrid('processingFileCountDetails', selectedRowIdToProcess);
		
	}
	
	
	function removeSelectedRowFromGrid(gridId, selectedRowList) {
		$.each(selectedRowList,function(i){
			$('#'+gridId).jqGrid('delRowData', selectedRowList[i]);
		});
		
		selectRowIdList = new Array();
		processingSelectedFile = new Array(); 
	}
	
	
	function getSelectedFileDetailsObj(searchGridId, detailJqGridId, selectedDetailIdArray) {
		var fileDetails = [];
		$.each(selectedDetailIdArray,function(i){
			var responseObject = jQuery("#"+detailJqGridId).jqGrid ('getRowData', selectedDetailIdArray[i]);
			var searchGridRowId = responseObject["serviceId"]+"_" + responseObject["pathId"];
			var responseObjectSearchGrid = jQuery("#"+searchGridId).jqGrid ('getRowData', searchGridRowId);
			var fileDetailsId = "fileDetails_"+responseObjectSearchGrid["serviceId"]+"_"+ responseObject["pathId"];
			var fileDetailsObj = $('#'+fileDetailsId).text();
			fileDetails = JSON.parse(fileDetailsObj);
		});
		return fileDetails;
	}
	
	
	function getAllSelectedFileDetailsRecord(detailJqGridId, selectedDetailIdArray){
		var selectedFileDetail = [];
		$.each(selectedDetailIdArray, function(i){
			var responseObject = jQuery("#"+detailJqGridId).jqGrid ('getRowData', selectedDetailIdArray[i]);
			var fileObj = {};
			fileObj.fileName = responseObject["fileName"];
			fileObj.fileSize = responseObject["fileSize"];
			selectedFileDetail.push(fileObj);
		});
		return selectedFileDetail;
	}
	
	function getProcessingFileDetailId(searchJqGridId, detailJqGridId, selectedDetailIdArray, mainId){
		var fileDetailsId = "";
		$.each(selectedDetailIdArray,function(i){
			var responseObject = jQuery("#"+detailJqGridId).jqGrid ('getRowData', selectedDetailIdArray[i]);
			
			var searchGridRowId = responseObject["serviceId"]+"_"+responseObject["pathId"];
			var responseObjectSearchGrid = jQuery("#"+searchJqGridId).jqGrid ('getRowData', searchGridRowId);
			fileDetailsId = "fileDetails_"+ searchGridRowId;

			return false;
		});
		return fileDetailsId;
	}
	
	function getProcessingFileCountId(searchJqGridId, detailJqGridId, selectedDetailIdArray, mainId) {
		var fileCountId = "";
		$.each(selectedDetailIdArray,function(i){
			var responseObject = jQuery("#"+detailJqGridId).jqGrid ('getRowData', selectedDetailIdArray[i]);
			var searchGridRowId = responseObject["serviceId"]+"_"+responseObject["pathId"];;
			var responseObjectSearchGrid = jQuery("#"+searchJqGridId).jqGrid ('getRowData', searchGridRowId);
			fileCountId = "fileCount_" + searchGridRowId;
			
			return false;
		});
		return fileCountId;
	}
	
	function getProcessingSearchGridCheckBoxId(searchJqGridId, detailJqGridId, selectedDetailIdArray, mainId) {
		var checkboxId = "";
		$.each(selectedDetailIdArray,function(i){
			var responseObject = jQuery("#"+detailJqGridId).jqGrid ('getRowData', selectedDetailIdArray[i]);
			var searchGridRowId = responseObject["serviceId"]+"_"+responseObject["pathId"];;
			var responseObjectSearchGrid = jQuery("#"+searchJqGridId).jqGrid ('getRowData', searchGridRowId);
			checkboxId = "rowid_"+ responseObjectSearchGrid["id"];
			return false;
		});
		return checkboxId;
	}
	
	function getProcessingFileCountDivId(searchJqGridId, detailJqGridId, selectedDetailIdArray, mainId) {
		var fileCountDivId = "";
		$.each(selectedDetailIdArray,function(i){
			var responseObject = jQuery("#"+detailJqGridId).jqGrid ('getRowData', selectedDetailIdArray[i]);
			var searchGridRowId = responseObject["serviceId"]+"_"+responseObject["pathId"];;
			var responseObjectSearchGrid = jQuery("#"+searchJqGridId).jqGrid ('getRowData', searchGridRowId);
			fileCountDivId = "fileCount_"+ searchGridRowId;
			return false;
		});
		return fileCountDivId;
	}
	
	function isShowRestoreButton(){
		var isShowRestoreBtn=false;
		var categoryVal = $('#category').val();
		if(categoryVal == 'Archive'){
			isShowRestoreBtn=true;
		} 
		return isShowRestoreBtn;
	}
	
</script>
