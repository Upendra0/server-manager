<%@page import="com.elitecore.sm.common.constants.EngineConstants"%>
<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<%@page import="com.elitecore.sm.common.constants.SystemParametersConstant"%>
<%@page import="com.elitecore.sm.util.MapCache"%>
<%@taglib uri="http://www.springframework.org/tags" 			prefix="spring"%>
<%@taglib uri="http://www.springframework.org/security/tags" 	prefix="sec"  %>
<%@taglib uri="/WEB-INF/ShowErrorTag.tld"                       prefix="elitecoreError" %>
<%@taglib uri="http://www.springframework.org/tags/form"        prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core"               prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions"          prefix="fn" %>

<div id="collectionServiceDetailDiv" style="display:none;">
	<div class="tab-content padding0 clearfix" id="collection-service-file-block">
		<div class="title2">
			<spring:message code="file.reprocess.searchFiles.label" ></spring:message>
		</div>
		<div class="fullwidth borbot">
			<div class="col-md-6 inline-form" style="padding-left: 0px !important;">
	            
	            <div class="form-group">
	         		<spring:message code="file.reprocess.serverInstance.label" var="label" ></spring:message>
	         		<div class="table-cell-label">${label}</div>
	             	<div class="input-group" id="collectionService_ServerInstanceText"></div>
	             </div>
	            
	            <div class="form-group">
	             	<spring:message code="file.reprocess.serviceInstance.label" var="label" ></spring:message>
	         		<div class="table-cell-label">${label}</div>
	             	<div class="input-group" id="collectionService_ServiceInstanceText"></div>
	            </div>
	            
	            <div class="form-group">
	             	<spring:message code="file.reprocess.driverName.label" var="label" ></spring:message>
	         		<div class="table-cell-label">${label}</div>
	             	<div class="input-group" id="collectionService_DriverName"></div>
	            </div>
	            
	            <div class="form-group">
	             	<spring:message code="file.reprocess.driverType.label" var="label" ></spring:message>
	         		<div class="table-cell-label">${label}</div>
	             	<div class="input-group" id="collectionService_DriverType"></div>
	            </div>
			</div>
			<div class="col-md-6 inline-form">
				<div class="form-group">
	         		<spring:message code="file.reprocess.readPath.label" var="label" ></spring:message>
	         		<div class="table-cell-label">${label}</div>
	             	<div class="input-group" id="collectionService_ReadPathText"></div>
	             </div>
	             
	             <div class="form-group">
	         		<spring:message code="file.reprocess.filePath.label" var="label" ></spring:message>
	         		<div class="table-cell-label">${label}</div>
	             	<div class="input-group" id="collectionService_FilePathText"></div>
	             </div>

				<div class="form-group">
					<spring:message code="file.reprocess.fromDate.label" var="label" ></spring:message>
					<div class="table-cell-label">${label}</div>
					<div class="input-group" id="collectionService_FromDateText"></div>
				</div>

				<div class="form-group">
					<spring:message code="file.reprocess.toDate.label" var="label" ></spring:message>
					<div class="table-cell-label">${label}</div>
					<div class="input-group" id="collectionService_ToDateText"></div>
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
			<table class="table table-hover" id="collectionService_FileDetails"></table>
         	<div id="collectionService_FileDetailsPagingDiv"></div> 
	 	</div>
	 	<div class="pbottom15">
			<sec:authorize access="hasAuthority('DELETE_FILE')">
				<button id="collectionService_FileReprocessDeleteBtn" class="btn btn-grey btn-xs" onclick="collectionServiceDeleteOnDetail();" tabindex="7"><spring:message code="btn.label.file.reprocess.delete" ></spring:message></button>
			</sec:authorize>
			<button id="collectionService_BackBtn" class="btn btn-grey btn-xs" onclick="backToReprocessingFileFromCollectionDetail();" tabindex="7"><spring:message code="btn.label.file.reprocess.back" ></spring:message></button>
		</div>
	</div>
</div>

<!-- Delete model for file reprocess collection on search : start -->
<div id="divDeleteConfirmationCollectionSearch" style=" width:100%; display: none;" >
    <div class="modal-content">
        <div class="modal-header padding10">
            <h4 class="modal-title"><spring:message code="label.confirmation.header"></spring:message></h4>
        </div>
        <div class="modal-body padding10 inline-form">
            <p><spring:message code="file.reprocess.grid.checkbox.confirmation.on.delete"></spring:message></p>
        </div>
        <div class="modal-footer padding10">
        	<sec:authorize access="hasAuthority('DELETE_FILE')">
            	<button type="button" class="btn btn-grey btn-xs " onclick="onConfirmDeleteCollectionServiceOnSearch()"><spring:message code="btn.label.yes" ></spring:message></button>
            </sec:authorize>
            <button type="button" class="btn btn-grey btn-xs " id="dist_delete_close_div" data-dismiss="modal" onclick="closeFancyBox();"><spring:message code="btn.label.no" ></spring:message></button>
        </div>
    </div>     
</div>
<!-- Delete model for file reprocess collection on search : end -->

<!-- Delete model for file reprocess collection on detail : start -->
<div id="divDeleteConfirmationCollectionDetail" style=" width:100%; display: none;" >
    <div class="modal-content">
        <div class="modal-header padding10">
            <h4 class="modal-title"><spring:message code="label.confirmation.header"></spring:message></h4>
        </div>
        <div class="modal-body padding10 inline-form">
            <p><spring:message code="file.reprocess.grid.checkbox.confirmation.on.delete"></spring:message></p>
        </div>
        <div class="modal-footer padding10">
        	<sec:authorize access="hasAuthority('DELETE_FILE')">
            	<button type="button" class="btn btn-grey btn-xs " onclick="onConfirmDeleteCollectionServiceOnDetail()"><spring:message code="btn.label.yes" ></spring:message></button>
            </sec:authorize>
            <button type="button" class="btn btn-grey btn-xs " id="dist_delete_dialod_close" data-dismiss="modal" onclick="closeFancyBox();"><spring:message code="btn.label.no" ></spring:message></button>
        </div>
    </div>     
</div>
<script type="text/javascript">
function getCollectionFileDetailList(serviceIds, serverInstanceIdToServiceListObject){
	var count = jQuery('#collectionServiceGrid').jqGrid('getGridParam', 'reccount');
	if(count && count > 0) {
	    reloadSearchGrid("collectionServiceGrid", serviceIds);
	} else {
		$("#collectionServiceGrid").jqGrid({
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
                      "<spring:message code='file.reprocess.list.grid.column.pathId' ></spring:message>",
                      "<spring:message code='file.reprocess.list.grid.column.fileDetails' ></spring:message>",
                      "<spring:message code='file.reprocess.list.grid.column.serviceId' ></spring:message>",
                      "<spring:message code='file.reprocess.list.grid.column.serverInstanceId' ></spring:message>",
                      "<spring:message code='file.reprocess.list.grid.column.serviceTypeId' ></spring:message>",
					  "<spring:message code='file.reprocess.list.grid.column.serverInstance' ></spring:message>",
					  "<spring:message code='file.reprocess.list.grid.column.serviceInstance' ></spring:message>",
					  "<spring:message code='file.reprocess.list.grid.column.serverIpAndPort' ></spring:message>",
					  "<spring:message code='file.reprocess.list.grid.column.port' ></spring:message>",
                      "<spring:message code='file.reprocess.list.grid.column.filePath' ></spring:message>",
                      "<spring:message code='file.reprocess.list.grid.column.readFilePath' ></spring:message>",
                      "<spring:message code='file.reprocess.list.grid.column.fileCount' ></spring:message>"
                     ],
                     colModel: [
						{name : '',	index : '', sortable:false, width : "5%", align:'center', formatter:checkBoxFormatterForCollectionSearch},
						{name:'id',index:'id', width : "0%", sortable:false, hidden:true},
						{name:'PathId',index:'PathId',sortable:false, width : "0%", hidden:true},
						{name:'FileDetails',index:'FileDetails',sortable:false, width : "0%", formatter:fileDetailsColumnFormatterForCollectionSearch, hidden:true},
						{name:'ServiceId',index:'ServiceId',sortable:false, width : "0%", hidden:true },
						{name:'ServerInstanceId',index:'ServerInstanceId',sortable:false, width : "0%", hidden:true},
						{name:'ServiceTypeId',index:'ServiceTypeId',sortable:false, width : "0%", hidden:true},
						{name:'ServerInstance',index:'ServerInstance', width : "15%", sortable:false, align:'center'},
						{name:'ServiceInstance',index:'ServiceInstance', width : "15%", sortable:false, align:'center'},
						{name:'ServerIP:Port',index:'ServerIP:Port', width : "15%", sortable:false, align:'center'},
						{name:'Port',index:'Port', width : "0%", hidden:true, sortable:false},
                    	{name:'FilePath',index:'FilePath', width : "15%",formatter:filePathColumnFormatterForCollection, sortable:false},
                    	{name:'ReadFilePath',index:'ReadFilePath', width : "0%", sortable:false, hidden:true},
                    	{name:'FileCount',index:'FileCount', width : "15%",formatter:fileCountColumnFormatterForCollection, sortable:false}
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
    		//pager: "#collectionServiceGridPagingDiv",
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
     			if ($('#collectionServiceGrid').getGridParam('records') === 0) {
					$('#collectionServiceGrid tbody').html(
							"<div style='padding:6px;background:#D8D8D8;text-align:center;'>"
									+ jsSpringMsg.emptyRecord + "</div>");
				} else {
					fileReprocessCollectionSelectedForSearch = new Array();
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
				var rowCount = jQuery("#collectionServiceGrid").jqGrid('getGridParam', 'records');
				 $("input:checkbox[id^=\"errorReprocessCheckbox_\"]").attr("disabled", false);
				if(rowCount > 0) {
					if(serverInstanceIdToServiceListObject) {
						getErrorReprocessJsonFromEngine(serverInstanceIdToServiceListObject);	
					}
				} 				
			},
            recordtext: "<spring:message code="jq.grid.pager.total.records.text"></spring:message>",
    	    emptyrecords: "<spring:message code="jq.grid.empty.records.text"></spring:message>",
    		loadtext: "<spring:message code="jq.grid.loading.text"></spring:message>",
    		
    		}).navGrid("#collectionServiceGridPagingDiv",{edit:false,add:false,del:false,search:false});
    			$(".ui-jqgrid-titlebar").hide();
    			$(".ui-pg-input").height("10px"); 
	}
	count = jQuery('#collectionServiceGrid').jqGrid('getGridParam', 'reccount');
	if(count <= 0) {
		reloadSearchGrid("collectionServiceGrid", serviceIds);
	}
}
 
function fileDetailsColumnFormatterForCollectionSearch(cellvalue, options, rowObject) {
	var fileDetailsId = "fileDetails_"+rowObject["ServiceId"]+"_"+ rowObject["id"]+"_"+rowObject["PathId"];
	return "<div id='"+fileDetailsId+"'><img src='img/preloaders/circle-red.gif' width='20px' data-toggle='tooltip' data-placement='bottom' title='Loading' height='20px' style='z-index:100px;'></div>";
}

function checkBoxFormatterForCollectionSearch(cellvalue, options, rowObject) {
	var reprocessFileId = rowObject["ServiceId"]+"_"+rowObject["id"];
	return "<input type='checkbox' name='errorReprocessCheckbox_" + reprocessFileId + "'  id='errorReprocessCheckbox_"
			+ reprocessFileId + "' onclick=\"addRowIdForCollectionSearch('errorReprocessCheckbox_" + reprocessFileId + "\',\'" + rowObject["id"] + "\')\"; />";
}

function addRowIdForCollectionSearch(elementId, reprocessFileId) {
	var reprocessFileElement = document.getElementById(elementId);
	if (reprocessFileElement && reprocessFileElement.checked) {
		if (fileReprocessCollectionSelectedForSearch.indexOf(reprocessFileId) === -1) {
			fileReprocessCollectionSelectedForSearch.push(reprocessFileId);
		}
	} else {
		if (fileReprocessCollectionSelectedForSearch.indexOf(reprocessFileId) !== -1) {
			fileReprocessCollectionSelectedForSearch.splice(fileReprocessCollectionSelectedForSearch.indexOf(reprocessFileId), 1);
		}
	}
}

function displayCollectionDeletePopup(){
	if(fileReprocessCollectionSelectedForSearch.length == 0){
		$("#deleteMessage").click();
		return;
	}
	$("#deleteCollectionReprocessConfirmation").click();
}

function filePathColumnFormatterForCollection(cellvalue, options, rowObject) {
	var errorPathId = "errorPath_"+rowObject["ServiceId"]+"_"+rowObject["id"]+"_"+rowObject["PathId"];
	return "<div id='"+errorPathId+"'><center><img src='img/preloaders/circle-red.gif' width='20px' data-toggle='tooltip' data-placement='bottom' title='Loading' height='20px' style='z-index:100px;'></center></div>";
}
function fileCountColumnFormatterForCollection(cellvalue, options, rowObject) {
	var postfix = "_"+rowObject["ServiceId"]+"_"+ rowObject["id"]+"_"+rowObject["PathId"];
	var fileCountId = "fileCount"+postfix;
	var fileDetailsId = "fileDetails"+postfix;
	var filePathId = "errorPath"+postfix;
	var serverInstance = rowObject["ServerInstance"];
	var serviceInstance = rowObject["ServiceInstance"];

	var serverInstanceId = rowObject["ServerInstanceId"];
	var serviceId = rowObject["ServiceId"];
	var serviceTypeId = rowObject["ServiceTypeId"];
	var pathId = rowObject["PathId"];

	var composerName = rowObject["ComposerName"];
	var composerType = rowObject["ComposerType"];
	var composerId = rowObject["ComposerId"];
	
	var readFilePath = rowObject["ReadFilePath"];
	var driverName = rowObject["DriverName"];
	var driverType = rowObject["DriverType"];
	
	return "<div id='"+fileCountId+"' style='cursor: pointer;' onclick=\"openCollectionDetailPage('"+fileDetailsId+"', '"+filePathId+"', '"+serverInstance+"', '"+serviceInstance+"', '"+readFilePath+"', '"+serverInstanceId+"', '"+serviceId+"', '"+serviceTypeId+"', '"+pathId+"', '"+driverName+"', '"+driverType+"', '"+composerType+"')\"><center><img src='img/preloaders/circle-red.gif' width='20px' data-toggle='tooltip' data-placement='bottom' title='Loading' height='20px' style='z-index:100px;'></center></div>";
}

function fileDetailsColumnFormatterForCollection(cellvalue, options, rowObject) {
	var fileDetailsId = "fileDetails_"+rowObject["ServiceId"]+"_"+rowObject["ComposerId"];
	return "<div id='"+fileDetailsId+"'><img src='img/preloaders/circle-red.gif' width='20px' data-toggle='tooltip' data-placement='bottom' title='Loading' height='20px' style='z-index:100px;'></div>";
}

function openCollectionDetailPage(fileDetailsElementId, filePathElementId, serverInstance, serviceInstance, readFilePath, serverInstanceId, serviceId, serviceTypeId, pathId, driverName, driverType, composerType) {
	var fileDetails = $('#'+fileDetailsElementId).text();
	$("#collectionService_ServerInstanceText").empty();
   	$("#collectionService_ServerInstanceText").html(serverInstance);	
   	$("#collectionService_ServiceInstanceText").empty();
   	$("#collectionService_ServiceInstanceText").html(serviceInstance);	
   	$("#collectionService_ReadPathText").empty();
   	$("#collectionService_ReadPathText").html(readFilePath);
   	$("#collectionService_FilePathText").empty();
   	$("#collectionService_FilePathText").html($('#'+filePathElementId).text());
	$("#collectionService_FromDateText").empty();
   	$("#collectionService_FromDateText").html($("#fromDate").val());
   	$("#collectionService_ToDateText").empty();
   	$("#collectionService_ToDateText").html($("#toDate").val());
   	$("#collectionService_DriverName").empty();
   	$("#collectionService_DriverName").html(driverName);
   	$("#collectionService_DriverType").empty();
   	$("#collectionService_DriverType").html(driverType);
   	
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
    	value.composerId = pathId;
    	value.pluginId = 0;
    	value.id = ++counter;
    	value.readPath = readFilePath;
    	value.errorPath = $('#'+filePathElementId).text();
   	});
    
    $grid = $("#collectionService_FileDetails");

	var count = jQuery('#collectionService_FileDetails').jqGrid('getGridParam', 'reccount');
	if(count && count > 0) {
		$('#collectionService_FileDetails').jqGrid('clearGridData');
		jQuery("#collectionService_FileDetails")
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
                    { name: "#", width: 20, formatter : checkBoxFormatterForCollection, sortable:false },
                    { name: "fileName", width: 110, sortable:false, align:'center',cellattr: function (rowId, cellValue, rawObject, cm, rdata) { return  "title='"+rawObject["absoluteFileName"]+"'"  ; } },
                    { name: "fileSize", width: 110, sortable:false, align:'center',formatter:convertAndDisplayFileSize },
                    { name: "fileType",width : 110,hidden:true},
                    { name: "Download", width: 110, formatter : downloadFormatterForCollection, sortable:false },
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
					$('#collectionService_FileDetails').jqGrid('hideCol',["View"]); 
                	fileReprocessCountSelected = new Array();
         			$(".ui-dialog-titlebar").show();
         			$('.checkboxbg').removeAttr("disabled");
         			$('.checkboxbg').bootstrapToggle();
         			if ($('#collectionService_FileDetails').getGridParam('records') === 0) {
    					$('#collectionService_FileDetails tbody').html(
    							"<div style='padding:6px;background:#D8D8D8;text-align:center;'>"
    									+ jsSpringMsg.emptyRecord + "</div>");
    					$("#collectionService_FileDetailsPagingDiv").hide();
    				} else {
    					$("#collectionService_FileDetailsPagingDiv").show();
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
        		
        		}).navGrid("#collectionService_FileDetailsPagingDiv",{edit:false,add:false,del:false,search:false});
        			$(".ui-jqgrid-titlebar").hide();
        			$(".ui-pg-input").height("10px");
        			
       		    jQuery("#collectionService_FileDetails")
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
	$("#collectionServiceDetailDiv").show();
}

var countForCollectionDetail = 0;
function checkBoxFormatterForCollection(cellvalue, options, rowObject) {
	countForCollectionDetail++;
	var reprocessFileId = rowObject["id"];
	return "<center><input type='checkbox' name='errorReprocessCollectionDetailCheckbox_" + reprocessFileId + "_"
			+ countForCollectionDetail + "'  id='errorReprocessCollectionDetailCheckbox_"
			+ reprocessFileId + "_" + countForCollectionDetail
			+ "' onclick=\"addRowIdForCollectionDetail('errorReprocessCollectionDetailCheckbox_" + reprocessFileId + "_"
			+ countForCollectionDetail + "\',\'" + rowObject["id"] + "\')\"; /></center>";
}

function downloadFormatterForCollection(cellvalue, options, rowObject) {
	if(isAccessDownloadFile){
		var serverInstanceId = rowObject["serverInstanceId"];
		var fileName = rowObject["fileName"];
		fileName = encodeURIComponent(fileName)
		var absoluteFileName = rowObject["absoluteFileName"];
		absoluteFileName = encodeURIComponent(absoluteFileName);
		var fileType = "NA";
		
		return "<center><a href='javascript:void(0)' onclick=downloadErrorFile(\'"+serverInstanceId+"\',\'"+fileName+"\',\'"+absoluteFileName+"\',\'"+fileType+"\');><i class='fa fa-download' aria-hidden='true'></i></a></center>";	
	}else{
		return "<center><a href='javascript:void(0)' ><i class='fa fa-download' aria-hidden='true'></i></a></center>";
	}
}

var fileReprocessCollectionDetailSelected = new Array();

function addRowIdForCollectionDetail(elementId, reprocessFileId) {
	var reprocessFileElement = document.getElementById(elementId);
	if (reprocessFileElement && reprocessFileElement.checked) {
		if (fileReprocessCollectionDetailSelected.indexOf(reprocessFileId) === -1) {
			fileReprocessCollectionDetailSelected.push(reprocessFileId);
		}
	} else {
		if (fileReprocessCollectionDetailSelected.indexOf(reprocessFileId) !== -1) {
			fileReprocessCollectionDetailSelected.splice(fileReprocessCollectionDetailSelected.indexOf(reprocessFileId), 1);
		}
	}
}

function viewCollectionFile() {
	//TODO : 
	$("#viewFileNameInPopupHeader").empty();
	$("#viewFileNameInPopupHeader").html("abc.txt");
	$("textarea#fileContentInTextArea").val("test content...");
	$("#viewFileContentModal").click();
}

function uploadAndReprocessCollectionFile(reprocessFileId) {
	$("#selectedDetaiFileForUploadAndReprocess").val(reprocessFileId);
	clearFileContent();
	$("#fileUploadAndReprocess").click();
}

function backToReprocessingFileFromCollectionDetail() {
	$("input:checkbox[id^=\"errorReprocessCollectionDetailCheckbox_\"]").removeAttr('checked');
	$("#reprocessFileSearchArea").show();
	$("#reprocessFileViewFileCountArea").hide();
	$("#collectionServiceDetailDiv").hide();
	$("#processingServiceFileDetailsDiv").hide();
}

//delete collection file detail screen
function collectionServiceDeleteOnDetail() {
	if(fileReprocessCollectionDetailSelected.length == 0){
		$("#deleteMessage").click();
		return;
	}
	$("#deleteConfirmationCollectionDetail").click();
}

function collectionServiceDeleteOnSearch() {
	if(fileReprocessCollectionSelectedForSearch.length == 0){
		$("#deleteMessage").click();
		return;
	}
	$("#deleteConfirmationCollectionSearch").click();
}

function onConfirmDeleteCollectionServiceOnSearch() {
	var reprocessCollectionFilesObj = getBatchJson("DELETE_FILE", "collectionServiceGrid", fileReprocessCollectionSelectedForSearch, "PathId");
	
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
		data: JSON.stringify(reprocessCollectionFilesObj),
		success: function(data){
			
			var response = eval(data);
			var responseCode = response.code;
			var responseMsg = response.msg;
			
			waitingDialog.hide();
			
			if (responseCode === "200") {
				var object = JSON.parse(data.object);
				continueWithFileSearchOnCollectionOnSearch();
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

function onConfirmDeleteCollectionServiceOnDetail() {
	var reprocessFilesObj = getBatchJsonForCountPageAction("DELETE_FILE", "collectionService_FileDetails", fileReprocessCollectionDetailSelected, "PathId");
	
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
				continueWithFileSearchOnCollectionOnDetail();
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

function continueWithFileSearchOnCollectionOnSearch() {
	//delete(hide) selected row from search grid
	$.each(fileReprocessCollectionSelectedForSearch,function(i){
		$("#"+fileReprocessCollectionSelectedForSearch[i],"#collectionServiceGrid").css({display:"none"});
	});
	fileReprocessCollectionSelectedForSearch = [];
}

function continueWithFileSearchOnCollectionOnDetail() {
	//delete(hide) selected row from detail grid
	$.each(fileReprocessCollectionDetailSelected,function(i){
		$("#"+fileReprocessCollectionDetailSelected[i],"#collectionService_FileDetails").css({display:"none"});
	});
	fileReprocessCollectionDetailSelected = [];
}

function viewFileStatusOnCollection() {
	closeFancyBox();
	loadReprocessingStatus();
	showButtons('REPROCESSING_STATUS');
}
</script>
