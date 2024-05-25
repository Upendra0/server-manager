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

<div id="distributionServiceDetailDiv" style="display:none;">
	<div class="tab-content padding0 clearfix" id="distribution-service-file-block">
		<div class="title2">
			<spring:message code="file.reprocess.searchFiles.label" ></spring:message>
		</div>
		<div class="fullwidth borbot">
			<div class="col-md-6 inline-form" style="padding-left: 0px !important;">
	            
	            <div class="form-group">
	         		<spring:message code="file.reprocess.serverInstance.label" var="label" ></spring:message>
	         		<div class="table-cell-label">${label}</div>
	             	<div class="input-group" id="distributionService_ServerInstanceText"></div>
	             </div>
	            
	            <div class="form-group">
	             	<spring:message code="file.reprocess.serviceInstance.label" var="label" ></spring:message>
	         		<div class="table-cell-label">${label}</div>
	             	<div class="input-group" id="distributionService_ServiceInstanceText"></div>
	            </div>
	            
	            <div class="form-group">
	             	<spring:message code="file.reprocess.composerName.label" var="label" ></spring:message>
	         		<div class="table-cell-label">${label}</div>
	             	<div class="input-group" id="distributionService_ComposerNameText"></div>
	            </div>
	            
	            <div class="form-group">
	             	<spring:message code="file.reprocess.composerType.label" var="label" ></spring:message>
	         		<div class="table-cell-label">${label}</div>
	             	<div class="input-group" id="distributionService_ComposerTypeText"></div>
	            </div>
	            
	            <div class="form-group">
	             	<spring:message code="file.reprocess.driverName.label" var="label" ></spring:message>
	         		<div class="table-cell-label">${label}</div>
	             	<div class="input-group" id="distributionService_DriverName"></div>
	            </div>
	            
	            <div class="form-group">
	             	<spring:message code="file.reprocess.driverType.label" var="label" ></spring:message>
	         		<div class="table-cell-label">${label}</div>
	             	<div class="input-group" id="distributionService_DriverType"></div>
	            </div>
			</div>
			<div class="col-md-6 inline-form">
				<div class="form-group">
	         		<spring:message code="file.reprocess.readPath.label" var="label" ></spring:message>
	         		<div class="table-cell-label">${label}</div>
	             	<div class="input-group" id="distributionService_ReadPathText"></div>
	             </div>
	             
	             <div class="form-group">
	         		<spring:message code="file.reprocess.filePath.label" var="label" ></spring:message>
	         		<div class="table-cell-label">${label}</div>
	             	<div class="input-group" id="distributionService_FilePathText"></div>
	             </div>
	             
	             <div class="form-group">
	         		<spring:message code="file.reprocess.fromDate.label" var="label" ></spring:message>
	         		<div class="table-cell-label">${label}</div>
	             	<div class="input-group" id="distributionService_FromDateText"></div>
	             </div>
	             
	             <div class="form-group">
	         		<spring:message code="file.reprocess.toDate.label" var="label" ></spring:message>
	         		<div class="table-cell-label">${label}</div>
	             	<div class="input-group" id="distributionService_ToDateText"></div>
	             </div>
	             
            	 <div class="form-group">
	             	&nbsp;
           	     </div>
           	     
           	     <div class="form-group">
	             	&nbsp;
           	     </div>
	            
			</div>
			
			<div class="pbottom15" style="padding-bottom: 0px;">
				&nbsp;
			</div>
		</div>
		<div class="tab-content no-padding clearfix">
       		<div class="fullwidth">
				<div class="title2">
					<spring:message code="file.reprocess.grid.heading.fileDetails" ></spring:message> 
	          	</div>
			</div>
	   	</div>
	   	<div class="box-body table-responsive no-padding box">
			<table class="table table-hover" id="distributionService_FileDetails"></table>
         	<div id="distributionService_FileDetailsPagingDiv"></div> 
	 	</div>
	 	<div class="pbottom15">
	 		<sec:authorize access="hasAuthority('DIRECT_REPROCESS')">
				<button id="distributionService_FileReprocessDetailBtn" class="btn btn-grey btn-xs" onclick="distributionServiceReprocessOnDetail();" tabindex="7"><spring:message code="btn.label.file.reprocess" ></spring:message></button>
			</sec:authorize>
			<sec:authorize access="hasAuthority('RESTORE_FILES')">
				<button id="distribution-service-restore-btn" class="btn btn-grey btn-xs" onclick="distributionServiceRestoreOnDetail();" tabindex="7"><spring:message code="btn.label.file.restore" ></spring:message></button>
			</sec:authorize>
			<sec:authorize access="hasAuthority('DELETE_FILE')">
				<button id="distributionService_FileReprocessDeleteBtn" class="btn btn-grey btn-xs" onclick="distributionServiceDeleteOnDetail();" tabindex="7"><spring:message code="btn.label.file.reprocess.delete" ></spring:message></button>
			</sec:authorize>
			<button id="distributionService_BackBtn" class="btn btn-grey btn-xs" onclick="backToReprocessingFileFromDistributionDetail();" tabindex="7"><spring:message code="btn.label.file.reprocess.back" ></spring:message></button>
		</div>
	</div>
</div>

<!-- Delete model for file reprocess distribution on search : start -->
<div id="divDeleteConfirmationDistributionSearch" style=" width:100%; display: none;" >
    <div class="modal-content">
        <div class="modal-header padding10">
            <h4 class="modal-title"><spring:message code="label.confirmation.header"></spring:message></h4>
        </div>
        <div class="modal-body padding10 inline-form">
            <p><spring:message code="file.reprocess.grid.checkbox.confirmation.on.delete"></spring:message></p>
        </div>
        <div class="modal-footer padding10">
        	<sec:authorize access="hasAuthority('RESTORE_FILES')">
				<button id="fileRestoreInCountBtn" class="btn btn-grey btn-xs" onclick="fileRestoreInCountBtn();" tabindex="7"><spring:message code="btn.label.file.restore" ></spring:message></button>
			</sec:authorize>
        	<sec:authorize access="hasAuthority('DELETE_FILE')">
            	<button type="button" class="btn btn-grey btn-xs " onclick="onConfirmDeleteDistributionServiceOnSearch()"><spring:message code="btn.label.yes" ></spring:message></button>
            </sec:authorize>
            <button type="button" class="btn btn-grey btn-xs " id="dist_delete_close_div" data-dismiss="modal" onclick="closeFancyBox();"><spring:message code="btn.label.no" ></spring:message></button>
        </div>
    </div>     
</div>
<!-- Delete model for file reprocess distribution on search : end -->

<!-- Delete model for file reprocess distribution on detail : start -->
<div id="divDeleteConfirmationDistributionDetail" style=" width:100%; display: none;" >
    <div class="modal-content">
        <div class="modal-header padding10">
            <h4 class="modal-title"><spring:message code="label.confirmation.header"></spring:message></h4>
        </div>
        <div class="modal-body padding10 inline-form">
            <p><spring:message code="file.reprocess.grid.checkbox.confirmation.on.delete"></spring:message></p>
        </div>
        <div class="modal-footer padding10">
        	<sec:authorize access="hasAuthority('DELETE_FILE')">
            	<button type="button" class="btn btn-grey btn-xs " onclick="onConfirmDeleteDistributionServiceOnDetail()"><spring:message code="btn.label.yes" ></spring:message></button>
            </sec:authorize>
            <button type="button" class="btn btn-grey btn-xs " id="dist_delete_dialod_close" data-dismiss="modal" onclick="closeFancyBox();"><spring:message code="btn.label.no" ></spring:message></button>
        </div>
    </div>     
</div>

<script type="text/javascript">
function getDistributionFileDetailList(serviceIds, serverInstanceIdToServiceListObject){
	var count = jQuery('#distributionServiceGrid').jqGrid('getGridParam', 'reccount');
	if(count && count > 0) {
	    reloadSearchGrid("distributionServiceGrid", serviceIds);
	} else {
		$("#distributionServiceGrid tbody").empty();
		$("#distributionServiceGrid").jqGrid({
        	url: '<%=ControllerConstants.GET_REPROCESS_FILES_FROM_SM%>',
        	postData:
    		{
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
   	    		'serviceInstanceIds': serviceIds.join()
    		},
            datatype: "json",
            jsonReader: { repeatitems: false, root: "rows", page: "page", total: "total", records: "records"},
            colNames:[
					  "#",
                      "<spring:message code='file.reprocess.list.grid.column.id' ></spring:message>",
                      "<spring:message code='file.reprocess.list.grid.column.composerId' ></spring:message>",
                      "<spring:message code='file.reprocess.list.grid.column.fileDetails' ></spring:message>",
                      "<spring:message code='file.reprocess.list.grid.column.serviceId' ></spring:message>",
                      "<spring:message code='file.reprocess.list.grid.column.serverInstanceId' ></spring:message>",
                      "<spring:message code='file.reprocess.list.grid.column.serviceTypeId' ></spring:message>",
					  "<spring:message code='file.reprocess.list.grid.column.serverInstance' ></spring:message>",
					  "<spring:message code='file.reprocess.list.grid.column.serviceInstance' ></spring:message>",
					  "<spring:message code='file.reprocess.list.grid.column.serverIpAndPort' ></spring:message>",
					  "<spring:message code='file.reprocess.list.grid.column.port' ></spring:message>",
                      "<spring:message code='file.reprocess.list.grid.column.composerName' ></spring:message>",
                      "<spring:message code='file.reprocess.list.grid.column.composerType' ></spring:message>",
                      "<spring:message code='file.reprocess.list.grid.column.driverName' ></spring:message>",
                      "<spring:message code='file.reprocess.list.grid.column.driverType' ></spring:message>",
                      "<spring:message code='file.reprocess.list.grid.column.filePath' ></spring:message>",
                      "<spring:message code='file.reprocess.list.grid.column.readFilePath' ></spring:message>",
                      "<spring:message code='file.reprocess.list.grid.column.fileCount' ></spring:message>"
                     ],
                     colModel: [
						{name : '',	index : '', sortable:false, width : "5%", align:'center', formatter:checkBoxFormatterForDistributionSearch},
						{name:'id',index:'id', width : "0%", sortable:false, hidden:true},
						{name:'ComposerId',index:'ComposerId',sortable:false, width : "0%", hidden:true},
						{name:'FileDetails',index:'FileDetails',sortable:false, width : "0%", formatter:fileDetailsColumnFormatterForDistributionSearch, hidden:true},
						{name:'ServiceId',index:'ServiceId',sortable:false, width : "0%", hidden:true },
						{name:'ServerInstanceId',index:'ServerInstanceId',sortable:false, width : "0%", hidden:true},
						{name:'ServiceTypeId',index:'ServiceTypeId',sortable:false, width : "0%", hidden:true},
						{name:'ServerInstance',index:'ServerInstance', width : "15%", sortable:false, align:'center'},
						{name:'ServiceInstance',index:'ServiceInstance', width : "15%", sortable:false, align:'center'},
						{name:'ServerIP:Port',index:'ServerIP:Port', width : "15%", sortable:false, align:'center'},
						{name:'Port',index:'Port', width : "0%", hidden:true, sortable:false},
                    	{name:'ComposerName',index:'ComposerName', width : "15%", sortable:false, align:'center'},
                    	{name:'ComposerType',index:'ComposerType', width : "15%", sortable:false, align:'center'},
                    	{name:'DriverName',index:'DriverName', width : "15%", sortable:false, align:'center'},
                    	{name:'DriverType',index:'DriverType', width : "15%", sortable:false, align:'center'},
                    	{name:'FilePath',index:'FilePath', width : "15%",formatter:filePathColumnFormatterForDistribution, sortable:false},
                    	{name:'ReadFilePath',index:'ReadFilePath', width : "0%", sortable:false, hidden:true},
                    	{name:'FileCount',index:'FileCount', width : "15%",formatter:fileCountColumnFormatterForDistribution, sortable:false}
               		 ],
            rowNum: 'records',
       		rowList: [],        // disable page size dropdown
       	    pgbuttons: false,   // disable page control like next, back button
       	    pgtext: null,       // disable pager text like 'Page 0 of 10'
       	    viewrecords: false, // disable current view record text like 'View 1-10 of 100'
            height: 'auto',
            mtype:'POST',
    		sortname: 'id',
    		sortorder: "asc",
    		//pager: "#distributionServiceGridPagingDiv",
    		contentType: "application/json; charset=utf-8",
    		//viewrecords: true,
    		multiselect: false,
    		timeout : 120000,
    	    loadtext: "<spring:message code="jq.grid.loading.text"></spring:message>",
            caption: "<spring:message code="file.reprocess.grid.heading" ></spring:message> ",
            beforeRequest:function(){
                $(".ui-dialog-titlebar").hide();
            }, 
            beforeSend: function(xhr) {
                xhr.setRequestHeader("Accept", "application/json");
                xhr.setRequestHeader("Content-Type", "application/json");
            },
            beforeSelectRow: function(rowid, e) { },
            loadComplete: function(data) {
            	
     			$(".ui-dialog-titlebar").show();
     			$('.checkboxbg').removeAttr("disabled");
     			$('.checkboxbg').bootstrapToggle();
     			if ($('#distributionServiceGrid').getGridParam('records') === 0) {
					$('#distributionServiceGrid tbody').html(
							"<div style='padding:6px;background:#D8D8D8;text-align:center;'>"
									+ jsSpringMsg.emptyRecord + "</div>");
				} else {
					fileReprocessDistributionSelectedForSearch = new Array();
				}
			}, 
			onPaging: function (pgButton) {
				clearResponseMsgDiv();
				//clearInstanceGrid();
			},
            loadError : function(xhr,st,err) {
    			handleGenericError(xhr,st,err);
    		},
    		beforeSelectRow : function(rowid, e) {
				return false;
			},
			onSelectAll : function(id, status) {

			},
			gridComplete : function() {
				resetWarningDisplay();
				clearAllMessages();
				var rowCount = jQuery("#distributionServiceGrid").jqGrid('getGridParam', 'records');
				 $("input:checkbox[id^=\"errorReprocessCheckbox_\"]").attr("disabled", true);
				if(rowCount > 0) {
					$("#distribution_reprocess_buttons_div").show();
					if(serverInstanceIdToServiceListObject) {
						getErrorReprocessJsonFromEngine(serverInstanceIdToServiceListObject);	
					}
				} else {
					$("#distribution_reprocess_buttons_div").hide();
				} 
				
			},
            recordtext: "<spring:message code="jq.grid.pager.total.records.text"></spring:message>",
    	    emptyrecords: "<spring:message code="jq.grid.empty.records.text"></spring:message>",
    		loadtext: "<spring:message code="jq.grid.loading.text"></spring:message>",
    		
    		}).navGrid("#distributionServiceGridPagingDiv",{edit:false,add:false,del:false,search:false});
    			$(".ui-jqgrid-titlebar").hide();
    			$(".ui-pg-input").height("10px"); 
    			
   			
	}
	count = jQuery('#distributionServiceGrid').jqGrid('getGridParam', 'reccount');
	if(count <= 0) {
		reloadSearchGrid("distributionServiceGrid", serviceIds);
	}

}

function fileDetailsColumnFormatterForDistributionSearch(cellvalue, options, rowObject) {
	var fileDetailsId = "fileDetails_"+rowObject["ServiceId"]+"_"+rowObject["id"];
	return "<div id='"+fileDetailsId+"'><img src='img/preloaders/circle-red.gif' width='20px' data-toggle='tooltip' data-placement='bottom' title='Loading' height='20px' style='z-index:100px;'></div>";
}

function checkBoxFormatterForDistributionSearch(cellvalue, options, rowObject) {
	var reprocessFileId = rowObject["ServiceId"]+"_"+rowObject["id"];
	return "<input type='checkbox' name='errorReprocessCheckbox_" + reprocessFileId + "'  id='errorReprocessCheckbox_"
			+ reprocessFileId + "' onclick=\"addRowIdForDistributionSearch('errorReprocessCheckbox_" + reprocessFileId + "\',\'" + rowObject["id"] + "\')\"; />";
	 
}

function addRowIdForDistributionSearch(elementId, reprocessFileId) {
	var reprocessFileElement = document.getElementById(elementId);
	if (reprocessFileElement && reprocessFileElement.checked) {
		if (fileReprocessDistributionSelectedForSearch.indexOf(reprocessFileId) === -1) {
			fileReprocessDistributionSelectedForSearch.push(reprocessFileId);
		}
	} else {
		if (fileReprocessDistributionSelectedForSearch.indexOf(reprocessFileId) !== -1) {
			fileReprocessDistributionSelectedForSearch.splice(fileReprocessDistributionSelectedForSearch.indexOf(reprocessFileId), 1);
		}
	}
}

function filePathColumnFormatterForDistribution(cellvalue, options, rowObject) {
	var errorPathId = "errorPath_"+rowObject["ServiceId"]+"_"+rowObject["ComposerId"];
	return "<div id='"+errorPathId+"'><center><img src='img/preloaders/circle-red.gif' width='20px' data-toggle='tooltip' data-placement='bottom' title='Loading' height='20px' style='z-index:100px;'></center></div>";
}

function fileCountColumnFormatterForDistribution(cellvalue, options, rowObject) {
	var postfix = "_"+rowObject["ServiceId"]+"_"+rowObject["ComposerId"];
	var fileCountId = "fileCount"+postfix;
	var fileDetailsId = "fileDetails"+postfix;
	var filePathId = "errorPath"+postfix;
	var serverInstance = rowObject["ServerInstance"];
	var serviceInstance = rowObject["ServiceInstance"];
	var composerName = rowObject["ComposerName"];
	var composerType = rowObject["ComposerType"];

	var serverInstanceId = rowObject["ServerInstanceId"];
	var serviceId = rowObject["ServiceId"];
	var serviceTypeId = rowObject["ServiceTypeId"];
	var composerId = rowObject["ComposerId"];
	
	var readFilePath = rowObject["ReadFilePath"];
	var driverName = rowObject["DriverName"];
	var driverType = rowObject["DriverType"];
	
	return "<div id='"+fileCountId+"' style='cursor: pointer;' onclick=\"openDistributionDetailPage('"+fileDetailsId+"', '"+filePathId+"', '"+serverInstance+"', '"+serviceInstance+"', '"+composerName+"', '"+composerType+"', '"+readFilePath+"', '"+serverInstanceId+"', '"+serviceId+"', '"+serviceTypeId+"', '"+composerId+"', '"+driverName+"', '"+driverType+"')\"><center><img src='img/preloaders/circle-red.gif' width='20px' data-toggle='tooltip' data-placement='bottom' title='Loading' height='20px' style='z-index:100px;'></center></div>";
}

function fileDetailsColumnFormatterForDistribution(cellvalue, options, rowObject) {
	var fileDetailsId = "fileDetails_"+rowObject["ServiceId"]+"_"+rowObject["ComposerId"];
	return "<div id='"+fileDetailsId+"'><img src='img/preloaders/circle-red.gif' width='20px' data-toggle='tooltip' data-placement='bottom' title='Loading' height='20px' style='z-index:100px;'></div>";
}

function openDistributionDetailPage(fileDetailsElementId, filePathElementId, serverInstance, serviceInstance, composerName, composerType, readFilePath, serverInstanceId, serviceId, serviceTypeId, composerId, driverName, driverType) {
	var fileDetails = $('#'+fileDetailsElementId).text();
	$("#distributionService_ServerInstanceText").empty();
   	$("#distributionService_ServerInstanceText").html(serverInstance);	
   	$("#distributionService_ServiceInstanceText").empty();
   	$("#distributionService_ServiceInstanceText").html(serviceInstance);	
   	$("#distributionService_ComposerNameText").empty();
   	$("#distributionService_ComposerNameText").html(composerName);
   	$("#distributionService_ComposerTypeText").empty();
   	$("#distributionService_ComposerTypeText").html(composerType);
   	$("#distributionService_ReadPathText").empty();
   	$("#distributionService_ReadPathText").html(readFilePath);
   	$("#distributionService_FilePathText").empty();
   	$("#distributionService_FilePathText").html($('#'+filePathElementId).text());
	$("#distributionService_FromDateText").empty();
   	$("#distributionService_FromDateText").html($("#fromDate").val());
   	$("#distributionService_ToDateText").empty();
   	$("#distributionService_ToDateText").html($("#toDate").val());
   	$("#distributionService_DriverName").empty();
   	$("#distributionService_DriverName").html(driverName);
   	$("#distributionService_DriverType").empty();
   	$("#distributionService_DriverType").html(driverType);
   	
   	var data = [];
   	if(fileDetails) {
   	    try {
   	    	data = JSON.parse(fileDetails);
   	    } catch(e) {
   	        return; // error in parse json in case of failure message
   	    }
   	}
   	
    //return if file count is 0
    if(!data || data.length <= 0) {
    	return;
    }
    
    var counter = 0;
    
    $.each(data, function(index, value) {
    	value.serverInstanceId = serverInstanceId;
    	value.serviceId = serviceId;
    	value.serviceTypeId = serviceTypeId; 
    	value.composerId = composerId;
    	value.pluginId = 0;
    	value.id = ++counter;
    	value.readPath = readFilePath;
    	value.errorPath = $('#'+filePathElementId).text();
   	});
    
    $grid = $("#distributionService_FileDetails");

	var count = jQuery('#distributionService_FileDetails').jqGrid('getGridParam', 'reccount');
	if(count && count > 0) {
		$('#distributionService_FileDetails').jqGrid('clearGridData');
		jQuery("#distributionService_FileDetails")
	    .jqGrid('setGridParam',
	        { 
	            datatype: 'local',
	            data:data
	        })
	    .trigger("reloadGrid");
	} else {
		 $grid.jqGrid({
                data: data,
                datatype: "local",
                colNames:[
					  "#",
			          "<spring:message code='file.reprocess.detail.list.grid.column.name' ></spring:message>",
			          "<spring:message code='file.reprocess.detail.list.grid.column.size' ></spring:message>",
			          "",
			          "<spring:message code='file.reprocess.detail.list.grid.column.download' ></spring:message>",
			          "<spring:message code='file.reprocess.detail.list.grid.column.uploadAndReprocess' ></spring:message>",
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
			          ""
                ],
                colModel: [
                    { name: "#", width: 20, formatter : checkBoxFormatterForDistribution, sortable:false },
                    { name: "fileName", width: 110, sortable:false, align:'center',cellattr: function (rowId, cellValue, rawObject, cm, rdata) { return  "title='"+rawObject["absoluteFileName"]+"'"  ; } },
                    { name: "fileSize", width: 110, sortable:false, align:'center',formatter:convertAndDisplayFileSize },
                    {name : "fileType",width : 110,hidden:true},
                    { name: "Download", width: 110, formatter : downloadFormatterForDistribution, sortable:false },
                    { name: "Upload & Reprocess", width: 110, formatter : uploadAndReprocessFormatterForDistribution, sortable:false },
                    { name: "View", width: 110, formatter : viewFormatter, sortable:false},
                    { name: "id", width: 0, sortable:false, hidden:true },
                    { name: "serverInstanceId", width: 0, sortable:false, hidden:true },
                    { name: "serviceId", width: 0, sortable:false, hidden:true },
                    { name: "serviceTypeId", width: 0, sortable:false, hidden:true },
                    { name: "pluginId", width: 0, sortable:false, hidden:true },
                    { name: "composerId", width: 0, sortable:false, hidden:true },
                    { name: "readPath", width: 0, sortable:false, hidden:true },
                    { name: "errorPath", width: 0, sortable:false, hidden:true },
                    { name: "absoluteFileName", width: 0, sortable:false, hidden:true },
                    { name: "sourceFileType", width: 0, sortable:false, hidden:true }
                ],
                height: 'auto',
                //mtype:'POST',
        		sortname: 'id',
        		sortorder: "asc",
        		pager: "#reprocessFileDetailGridPagingDiv",
        		pgbuttons: true,
				rowList:[5,10,20,50,100], 
        		//contentType: "application/json; charset=utf-8",
        		viewrecords: true,
        		multiselect: false,
        		//timeout : 120000,
        	    loadtext: "<spring:message code="jq.grid.loading.text"></spring:message>",
                caption: "<spring:message code="file.reprocess.grid.heading" ></spring:message> ",
                beforeRequest:function(){
                    $(".ui-dialog-titlebar").hide();
                }, 
                beforeSend: function(xhr) {},
                beforeSelectRow: function(rowid, e) { },
                loadComplete: function(data) {
                	var isShowUpload = isCategoryUploadNReprocessable();
					if(!isShowUpload){
						 $('#distributionService_FileDetails').jqGrid('hideCol',["Upload & Reprocess"]); 
					} else {
						$('#distributionService_FileDetails').jqGrid('showCol',["Upload & Reprocess"]);
					}
					var isShowView = isCategoryViewableForDistribution(composerType);
					var isOutputCategory = isCategoryIsOutput();
					if(isOutputCategory && !isShowView){
						 $('#distributionService_FileDetails').jqGrid('hideCol',["View"]); 
					}
					var isShowReprocessBtn = isShowReprocessButton();
					if(!isShowReprocessBtn){
						 $("#distributionService_FileReprocessDetailBtn").hide(); 
					}else{
						$("#distributionService_FileReprocessDetailBtn").show();
					}
					var isShowRestoreBtn = isShowRestoreButton();					
					if(!isShowRestoreBtn){
						 $("#distribution-service-restore-btn").hide(); 
					}else{
						$("#distribution-service-restore-btn").show();
					}
                	fileReprocessCountSelected = new Array();
         			$(".ui-dialog-titlebar").show();
         			$('.checkboxbg').removeAttr("disabled");
         			$('.checkboxbg').bootstrapToggle();
         			if ($('#distributionService_FileDetails').getGridParam('records') === 0) {
    					$('#distributionService_FileDetails tbody').html(
    							"<div style='padding:6px;background:#D8D8D8;text-align:center;'>"
    									+ jsSpringMsg.emptyRecord + "</div>");
    					$("#distributionService_FileDetailsPagingDiv").hide();
    				} else {
    					$("#distributionService_FileDetailsPagingDiv").show();
    				} 
    			}, 
    			onPaging: function (pgButton) {
    				clearResponseMsgDiv();
    				//clearInstanceGrid();
    			},
                loadError : function(xhr,st,err) {
        			handleGenericError(xhr,st,err);
        		},
        		beforeSelectRow : function(rowid, e) {
    				return false;
    			},
    			onSelectAll : function(id, status) {

    			},
    			gridComplete : function() {
    				 
    			},
                recordtext: "<spring:message code="jq.grid.pager.total.records.text"></spring:message>",
        	    emptyrecords: "<spring:message code="jq.grid.empty.records.text"></spring:message>",
        		loadtext: "<spring:message code="jq.grid.loading.text"></spring:message>",
        		
        		}).navGrid("#distributionService_FileDetailsPagingDiv",{edit:false,add:false,del:false,search:false});
        			$(".ui-jqgrid-titlebar").hide();
        			$(".ui-pg-input").height("10px");
        			
       		    jQuery("#distributionService_FileDetails")
       		    .jqGrid('setGridParam',
       		    { 
    				 datatype: 'local',
        		     data:data
   		        })
       		    .trigger("reloadGrid");
	}
	
	$("#reprocessFileSearchArea").hide();
	$("#reprocessFileViewFileCountArea").hide();
	$("#processingServiceFileDetailsDiv").hide();
	$("#distributionServiceDetailDiv").show();
}

function isShowReprocessButton(){
	var isShowReprocessBtn=true;
	var categoryVal = $('#category').val();
	if(categoryVal == 'Output' || categoryVal == 'Archive' || categoryVal == 'Input'){
		isShowReprocessBtn=false;
	} 
	return isShowReprocessBtn;
}

function isShowRestoreButton(){
	var isShowRestoreBtn=false;
	var categoryVal = $('#category').val();
	if(categoryVal == 'Archive'){
		isShowRestoreBtn=true;
	} 
	return isShowRestoreBtn;
}

function isCategoryUploadNReprocessable(){
	var isShowUpload=true;
	var categoryVal = $('#category').val();
	if(categoryVal == 'Output' || categoryVal == 'Archive' || categoryVal == 'Input'){
		isShowUpload=false;
	} 
	return isShowUpload;
}

function isCategoryIsOutput(){
	var isOutputCategory=false;
	var categoryVal = $('#category').val();
	if(categoryVal == 'Output'){
		isOutputCategory=true;
	} 
	return isOutputCategory;
}

function isCategoryViewableForDistribution(composerType){
	var isShowView=false;
	if(composerType == 'ASCII_COMPOSER_PLUGIN' || composerType == 'FIXED_LENGTH_ASCII_COMPOSER_PLUGIN'){
		isShowView=true;
	} 
	return isShowView;
}

var countForDistributionDetail = 0;
function checkBoxFormatterForDistribution(cellvalue, options, rowObject) {
	countForDistributionDetail++;
	var reprocessFileId = rowObject["id"];
	return "<center><input type='checkbox' name='errorReprocessDistributionDetailCheckbox_" + reprocessFileId + "_"
			+ countForDistributionDetail + "'  id='errorReprocessDistributionDetailCheckbox_"
			+ reprocessFileId + "_" + countForDistributionDetail
			+ "' onclick=\"addRowIdForDistributionDetail('errorReprocessDistributionDetailCheckbox_" + reprocessFileId + "_"
			+ countForDistributionDetail + "\',\'" + rowObject["id"] + "\')\"; /></center>";
}

function downloadFormatterForDistribution(cellvalue, options, rowObject) {
	if(isAccessDownloadFile){
		var serverInstanceId = rowObject["serverInstanceId"];
		var fileName = rowObject["fileName"];
		fileName = encodeURIComponent(fileName)
		var absoluteFileName = rowObject["absoluteFileName"];
		absoluteFileName = encodeURIComponent(absoluteFileName);
		var fileType = rowObject["fileType"];
		return "<center><a href='javascript:void(0)' onclick=downloadErrorFile(\'"+serverInstanceId+"\',\'"+fileName+"\',\'"+absoluteFileName+"\',\'"+fileType+"\');><i class='fa fa-download' aria-hidden='true'></i></a></center>";	
	}else{
		return "<center><a href='javascript:void(0)' ><i class='fa fa-download' aria-hidden='true'></i></a></center>";
	}
	
}

function uploadAndReprocessFormatterForDistribution(cellvalue, options, rowObject) {
	if(isAccessUploadFile){
		var reprocessFileId = rowObject["id"];
		return "<center><a href='javascript:void(0)' onclick='uploadAndReprocessDistributionFile("+reprocessFileId+");'><i class='fa fa-upload' aria-hidden='true'></i></a></center>";
	}else{
		var reprocessFileId = rowObject["id"];
		return "<center><a href='javascript:void(0)'><i class='fa fa-upload' aria-hidden='true'></i></a></center>";
	}
}

var fileReprocessDistributionDetailSelected = new Array();

function addRowIdForDistributionDetail(elementId, reprocessFileId) {
	var reprocessFileElement = document.getElementById(elementId);
	if (reprocessFileElement && reprocessFileElement.checked) {
		if (fileReprocessDistributionDetailSelected.indexOf(reprocessFileId) === -1) {
			fileReprocessDistributionDetailSelected.push(reprocessFileId);
		}
	} else {
		if (fileReprocessDistributionDetailSelected.indexOf(reprocessFileId) !== -1) {
			fileReprocessDistributionDetailSelected.splice(fileReprocessDistributionDetailSelected.indexOf(reprocessFileId), 1);
		}
	}
	
	
}

function continueWithFileSearchOnDistributionOnDetail() {
	fileReprocessDistributionDetailSelected = new Array();
}

function viewDistributionFile() {
	//TODO : 
	$("#viewFileNameInPopupHeader").empty();
	$("#viewFileNameInPopupHeader").html("abc.txt");
	$("textarea#fileContentInTextArea").val("test content...");
	$("#viewFileContentModal").click();
}

function downloadDistributionFile() {
	
}

function uploadAndReprocessDistributionFile(reprocessFileId) {
	$("#selectedDetaiFileForUploadAndReprocess").val(reprocessFileId);
	clearFileContent();
	$("#fileUploadAndReprocess").click();
}

function backToReprocessingFileFromDistributionDetail() {
	$("input:checkbox[id^=\"errorReprocessDistributionDetailCheckbox_\"]").removeAttr('checked');
	$("#reprocessFileSearchArea").show();
	$("#reprocessFileViewFileCountArea").hide();
	$("#distributionServiceDetailDiv").hide();
	$("#processingServiceFileDetailsDiv").hide();
}

//reprocess distribution file detail screen
function distributionServiceReprocessOnDetail() {
	if(fileReprocessDistributionDetailSelected.length == 0){
		$("#reprocessMessage").click();
		return;
	}
	var reprocessFilesObj = getBatchJsonForCountPageAction("DIRECT_REPROCESS", "distributionService_FileDetails", fileReprocessDistributionDetailSelected, "composerId");
	
	waitingDialog.show();
	
	$.ajax({
		url: '<%=ControllerConstants.CREATE_ERROR_REPROCESS_BATCH%>',
		cache: false,
		async: true, 
		dataType: 'json',
		headers: {
		    'Content-Type': 'application/json'
		},
		type: "POST",
		data: JSON.stringify(reprocessFilesObj),
		success: function(data){
			waitingDialog.hide();
			var response = eval(data);
			var responseCode = response.code;
			var responseMsg = response.msg;
			
			if (responseCode === "200") {
				var object = JSON.parse(data.object);
				continueWithFileSearchOnCount('DISTRIBUTION_SERVICE', 0);
				$("#reprocessBatchId").empty();
				$("#reprocessBatchId").html(object.id);
				$("#batchIdForViewStatus").val(parseInt(object.id));
				$("#reprocessSuccessModal").click();
			}else{
				showErrorMsg(responseMsg);
			}
		},
	    error: function (xhr,st,err){
			handleGenericError(xhr,st,err);
		}
	});
}

function distributionServiceRestoreOnDetail() {
	if(fileReprocessDistributionDetailSelected.length == 0){
		$("#restoreMessage").click();
		return;
	}
	var reprocessFilesObj = getBatchJsonForCountPageAction("ARCHIVE_RESTORE", "distributionService_FileDetails", fileReprocessDistributionDetailSelected, "composerId");
	
	waitingDialog.show();
	
	$.ajax({
		url: '<%=ControllerConstants.CREATE_ARCHIVE_RESTORE_BATCH%>',
		cache: false,
		async: true, 
		dataType: 'json',
		headers: {
		    'Content-Type': 'application/json'
		},
		type: "POST",
		data: JSON.stringify(reprocessFilesObj),
		success: function(data){
			waitingDialog.hide();
			var response = eval(data);
			var responseCode = response.code;
			var responseMsg = response.msg;
			
			if (responseCode === "200") {
				var object = JSON.parse(data.object);
				continueWithFileSearchOnCount('DISTRIBUTION_SERVICE', 0);
				$("#restoreBatchId").empty();
				$("#restoreBatchId").html(object.id);
				$("#batchIdForViewStatus").val(parseInt(object.id));
				$("#restoreSuccessModal").click();
			}else{
				showErrorMsg(responseMsg);
			}
		},
	    error: function (xhr,st,err){
			handleGenericError(xhr,st,err);
		}
	});
}

//delete distribution file detail screen
function distributionServiceDeleteOnDetail() {
	if(fileReprocessDistributionDetailSelected.length == 0){
		$("#deleteMessage").click();
		return;
	}
	$("#deleteConfirmationDistributionDetail").click();
}

//reprocess distribution search screen
function distributionServiceReprocessOnSearch() {
	if(fileReprocessDistributionSelectedForSearch.length == 0){
		$("#reprocessMessage").click();
		return;
	}
	var reprocessDistributionFilesObj = getBatchJson("DIRECT_REPROCESS", "distributionServiceGrid", fileReprocessDistributionSelectedForSearch, "ComposerId");
	
	waitingDialog.show();
	$.ajax({
		url: '<%=ControllerConstants.CREATE_ERROR_REPROCESS_BATCH%>',
		cache: false,
		async: true, 
		dataType: 'json',
		headers: {
		    'Content-Type': 'application/json'
		},
		type: "POST",
		data: JSON.stringify(reprocessDistributionFilesObj),
		success: function(data){
			var response = eval(data);
			var responseCode = response.code;
			var responseMsg = response.msg;
			var responseObject = response.object;
			waitingDialog.hide();
			if (responseCode === "200") {
				var object = JSON.parse(data.object);
 				continueWithFileSearchOnDistributionOnSearch();
				$("#reprocessBatchId").empty();
				$("#reprocessBatchId").html(object.id);
				$("#batchIdForViewStatus").val(parseInt(object.id));
				$("#reprocessSuccessModal").click();
			}else{
				showErrorMsg(responseMsg);
			}
		},
	    error: function (xhr,st,err){
			handleGenericError(xhr,st,err);
		}
	});
}

function restoreDistributionFiles() {
	if(fileReprocessDistributionSelectedForSearch.length == 0){
		$("#restoreMessage").click();
		return;
	}
	var reprocessDistributionFilesObj = getBatchJson("ARCHIVE_RESTORE", "distributionServiceGrid", fileReprocessDistributionSelectedForSearch, "ComposerId");
	
	waitingDialog.show();
	$.ajax({
		url: '<%=ControllerConstants.CREATE_ARCHIVE_RESTORE_BATCH%>',
		cache: false,
		async: true, 
		dataType: 'json',
		headers: {
		    'Content-Type': 'application/json'
		},
		type: "POST",
		data: JSON.stringify(reprocessDistributionFilesObj),
		success: function(data){
			var response = eval(data);
			var responseCode = response.code;
			var responseMsg = response.msg;
			var responseObject = response.object;
			waitingDialog.hide();
			if (responseCode === "200") {
				var object = JSON.parse(data.object);
 				continueWithFileSearchOnDistributionOnSearch();
				$("#restoreBatchId").empty();
				$("#restoreBatchId").html(object.id);
				$("#batchIdForViewStatus").val(parseInt(object.id));
				$("#restoreSuccessModal").click();
			}else{
				showErrorMsg(responseMsg);
			}
		},
	    error: function (xhr,st,err){
			handleGenericError(xhr,st,err);
		}
	});
}


function distributionServiceDeleteOnSearch() {
	if(fileReprocessDistributionSelectedForSearch.length == 0){
		$("#deleteMessage").click();
		return;
	}
	$("#deleteConfirmationDistributionSearch").click();
}

function onConfirmDeleteDistributionServiceOnSearch() {
	var reprocessDistributionFilesObj = getBatchJson("DELETE_FILE", "distributionServiceGrid", fileReprocessDistributionSelectedForSearch, "ComposerId");
	
	$("#dist_delete_close_div").click();
	waitingDialog.show();
	$.ajax({
		url: '<%=ControllerConstants.DELETE_FILE_REPROCESS_DETAILS%>',
		cache: false,
		async: true, 
		dataType: 'json',
		headers: {
		    'Content-Type': 'application/json'
		},
		type: "POST",
		data: JSON.stringify(reprocessDistributionFilesObj),
		success: function(data){
			
			var response = eval(data);
			var responseCode = response.code;
			var responseMsg = response.msg;
			
			waitingDialog.hide();
			
			if (responseCode === "200") {
				var object = JSON.parse(data.object);
				continueWithFileSearchOnDistributionOnSearch();
				$("#deleteBatchId").empty();
				$("#deleteBatchId").html(object.id);
				$("#batchIdForViewStatus").val(parseInt(object.id));
				$("#deleteSuccessModal").click();
			}else{
				showErrorMsg(responseMsg);
			}
		},
	    error: function (xhr,st,err){
			handleGenericError(xhr,st,err);
		}
	});
}

function onConfirmDeleteDistributionServiceOnDetail() {
	
	var reprocessFilesObj = getBatchJsonForCountPageAction("DELETE_FILE", "distributionService_FileDetails", fileReprocessDistributionDetailSelected, "composerId");
	
	$("#dist_delete_dialod_close").click();
	waitingDialog.show();
	
	$.ajax({
		url: '<%=ControllerConstants.DELETE_FILE_REPROCESS_DETAILS%>',
		cache: false,
		async: true, 
		dataType: 'json',
		headers: {
		    'Content-Type': 'application/json'
		},
		type: "POST",
		data: JSON.stringify(reprocessFilesObj),
		success: function(data){
			waitingDialog.hide();
			var response = eval(data);
			var responseCode = response.code;
			var responseMsg = response.msg;
			
			if (responseCode === "200") {
				var object = JSON.parse(data.object);
				continueWithFileSearchOnCount('DISTRIBUTION_SERVICE', 0);
				$("#deleteBatchId").empty();
				$("#deleteBatchId").html(object.id);
				$("#batchIdForViewStatus").val(parseInt(object.id));
				$("#deleteSuccessModal").click();
			}else{
				showErrorMsg(responseMsg);
			}
		},
	    error: function (xhr,st,err){
			handleGenericError(xhr,st,err);
		}
	});
}

function continueWithFileSearchOnDistributionOnSearch() {
	//delete(hide) selected row from search grid
	$.each(fileReprocessDistributionSelectedForSearch,function(i){
		$("#"+fileReprocessDistributionSelectedForSearch[i],"#distributionServiceGrid").css({display:"none"});
	});
	fileReprocessDistributionSelectedForSearch = [];
}

function viewFileStatusOnDistribution() {
	closeFancyBox();
	loadReprocessingStatus();
	showButtons('REPROCESSING_STATUS');
}

</script>
