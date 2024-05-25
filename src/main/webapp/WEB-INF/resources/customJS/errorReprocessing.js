var maximumJmxCalls = 10;
function getServiceListByType(elementId){
	var serviceType = $("#search_service_type").val();
	manageProcessingServiceAttribute(serviceType);
	if(serviceType != "-1"){
		$.ajax({
			url : "getServiceByType",
			cache : false,
			async : true,
			dataType : 'json',
			type : "POST",
			data : {
				"serviceAlias" : serviceType
			},
			success : function(data) {
				var response = eval(data);
				var responseCode = response.code;
				var responseMsg = response.msg;
				var responseObject = response.object;
				if (responseCode == "200") {
					resetWarningDisplay();
					clearAllMessages();
					
					if(serviceType == "PARSING_SERVICE" || serviceType == "DISTRIBUTION_SERVICE"  
						||serviceType == "PROCESSING_SERVICE" || serviceType == "DATA_CONSOLIDATION_SERVICE"
							||serviceType == "AGGREGATION_SERVICE" ||serviceType == "ROAMING_VALIDATION_SERVICE" 
								|| serviceType == "ROAMING_COMPOSER_SERVICE") {
						$('#category').find('option').hide();
						$('#category option[value="Error"]').show();
						$('#category option[value="Input"]').show();
						$('#category option[value="Output"]').show();
						$('#category option[value="Archive"]').show();
						
						if(elementId == "serviceInstanceListAuto"){
							$('#category').val('-1');
							elementId = "serviceInstanceList"
						}else{
							$('#category').val('Error');
						}
						$("#rule_list_div").hide();
					} 
					if(serviceType == "PROCESSING_SERVICE") {
						$('#category option[value="Filter"]').show();
						$('#category option[value="Invalid"]').show();
						$('#category option[value="Duplicate"]').show();
					}
					
					if(serviceType == "AGGREGATION_SERVICE") {
						$('#category option[value="Output"]').hide();
						$('#category option[value="Aggregation Error"]').show();
						$('#category option[value="Aggregated Output"]').show();
						$('#category option[value="Non-Aggregated Output"]').show();
					}

					if(serviceType == "COLLECTION_SERVICE" || serviceType=="GTPPRIME_COLLECTION_SERVICE") {
						$('#category').find('option').hide();
						$('#category option[value="Output"]').show();
						$('#category option[value="Duplicate"]').show();
						$('#category').val('Output');
						$("#rule_list_div").hide();
					}
					
					$('#'+elementId+' option').each(function(index, option) {
					    $(option).remove();
					});
					
					$("#"+elementId).multiselect('destroy');
					
					$.each(responseObject, function(index, responseObject) {
						if(elementId == "serviceInstanceList") {
							var serverInstanceId = JSON.parse(responseObject["serverInstance"])["id"];
							$('#'+elementId).append("<option value='" + serverInstanceId+"-"+responseObject["id"]+ "'>" + responseObject["name"]+ "</option>");
						} else {
							$('#'+elementId).append("<option value='" + responseObject["id"]+ "'>" + responseObject["name"]+ "</option>");
						}
						
					});
					
					$("#"+elementId).multiselect({
						includeSelectAllOption: true,
			            selectAllValue: 'select-all-value',
			 			enableFiltering: true,
			 			enableHTML : true,
						maxHeight: 150,
						dropDown: true,
						buttonWidth: '150px'
						
					}); 
					
					$("#"+elementId).multiselect('selectAll', false);
					$("#"+elementId).multiselect('updateButtonText');
					
				} else {
					resetMultiselectDropdown(elementId);
					showErrorMsg(responseMsg);
				}
			},
			error : function(xhr, st, err) {
				handleGenericError(xhr, st, err);
			}
		});
	}
}

function loadBatchDetails(){
	clearResponseMsgDiv();
	clearGridData();
	var $grid = $("#fileBatchStatusList");
	$grid.jqGrid('setGridParam',{datatype:'json',page: 1}).trigger('reloadGrid');
}

function manageProcessingServiceAttribute(serviceType){
	if(serviceType == "PROCESSING_SERVICE"){
		$('#reprocess_reason_category_div').show();
		$('#reprocess_reason_severity_div').show();
		$('#reprocess_reason_errorcode_div').show();
		$("#rule_list_div").show();
	}else{
		$('#reprocess_reason_category_div').hide();
		$('#reprocess_reason_severity_div').hide();
		$('#reprocess_reason_errorcode_div').hide();
		$("#rule_list_div").hide();
	}
}

function reloadGridData() {

	var isValid = validateDates();
	
	if(isValid){
		clearAllMessages();
		clearResponseMsgDiv();
		clearResponseMsg();
		clearErrorMsg();

		var $grid = $("#fileBatchStatusList");
		jQuery('#fileBatchStatusList').jqGrid('clearGridData');
		clearGridData('fileBatchStatusList');

		$grid.jqGrid('setGridParam', {
			datatype : 'json',
			page : 1,
			sortname : 'id',
			sortorder : "desc",
			postData : {
				'serviceType' : function() {
					return $("#search_service_type").val();
				},
				'batchId' : function() {
					var id = $("#search_batch_id").val();
					if (id =='undefined' || id == '' || id == '-1' ) {
						id = 0;
					}
					return id;
				},
				'serviceInstanceIds' : function() {
					var id = $("#search_service_instance").val();
					if (id == 'undefined' || id == '' || id == '-1' || id == null || id == 'null'){
						id = 0;
					}
					return id;
				},
				'serviceId' : 0,
				'reprocessStatus' : function() {
					return $("#search_file_status").val();
				},
				'fileNameContains' : function() {
					return $("#search_file_contain").val();
				},
				'fromDate' : function() {
					return $("#search_file_from").val();
				},
				'toDate' : function() {
					return $("#search_file_to").val();
				},
				'fileAction' : function() {
					return $("#search_file_action").val();
				}
			}
		}).trigger('reloadGrid');
	}
	
}

/* Function will clear current grid data */
function clearGridData(elementId) {
	var $grid = $("#"+elementId);
	var rowIds = $grid.jqGrid('getDataIDs');
	// iterate through the rows and delete each of them
	for (var i = 0, len = rowIds.length; i < len; i++) {
		var currRow = rowIds[i];
		$grid.jqGrid('delRowData', currRow);
	}
}
function getBatchDetailsBySearchParams(){
	$("#fileBatchStatusList").jqGrid(
			{
				url : 'getAllBatchDetails',
				postData : {
					'serviceType' : function() {
						return $("#search_service_type").val();
					},
					'batchId' : function() {
						var id = $("#search_batch_id").val();
						if (id =='undefined' || id == '' || id == '-1' ) {
							id = 0;
						}
						return id;
					},
					'serviceInstanceIds' : function() {
						var id = $("#search_service_instance").val();
						if (id == 'undefined' || id == '' || id == '-1' || id == null || id == 'null'){
							id = 0;
						}
						return id;
					},
					'serviceId' : 0,
					'reprocessStatus' : function() {
						return $("#search_file_status").val();
					},
					'fileNameContains' : function() {
						return $("#search_file_contain").val();
					},
					'fromDate' : function() {
						return $("#search_file_from").val();
					},
					'toDate' : function() {
						return $("#search_file_to").val();
					},
					'fileAction' : function() {
						return $("#search_file_action").val();
					}
				},
				datatype : "json",
				colNames : ["#",
				            jsSpringMsg.id,
				            jsSpringMsg.batchId,
				            jsSpringMsg.serviceType, 
				            jsSpringMsg.fileName, 
				            jsSpringMsg.action, 
				            jsSpringMsg.status,
				            "Type",
				            "actionDetail",
				            "failReason",
				            "startTime",
				            "endTime",
				            "serverInstance",
				            "serviceInstance",
				            "pluginName",
				            "pluginType",
				            "filePath",
				            "fileName",
				            "fileSize",
				            "category",
				            jsSpringMsg.viewDetails,
				            "serviceId",
				            "pluginId",
				            "composerId",
				            "fileBackupPath",
				            "isCompress",
				            "serviceTypeId",
				            "readFilePath",
				            jsSpringMsg.category,
				            "serverInstanceId",
				            "",
				            ""
				          ],
				colModel : [ 
			            	{name : '',	index : '', sortable:false, formatter : checkBoxFormatterForViewStatus, width : "5%", align:'center'},
							{name : 'id',index : 'id',sortable : false, align : 'center', width:'15%'},
							{name : 'batchId',index : 'batchId',sortable : false, align : 'center', width:'10%'},
							{name : 'serviceType',index : 'serviceType',sortable : false, align : 'center', width:'20%'},
							{name : 'fileName',index : 'fileName',sortable : false, align : 'center', width:'20%'},
							{name : 'action',index : 'action',sortable : false, align : 'center', width:'20%'},
							{name : 'status',index : 'status',sortable : false, align : 'center', width:'10%'},
							{name : 'type',index : 'type',sortable : false, align : 'center', width:'10%'},
							{name : 'actionDetail',index : 'actionDetail',sortable : false,hidden:true, width:'0%'},
							{name : 'failReason',index : 'failReason',sortable : false,hidden:true, width:'0%'},
							{name : 'startTime',index : 'startTime',sortable : false,hidden:true, width:'0%'},
							{name : 'endTime',index : 'endTime',sortable : false,hidden:true, width:'0%'},
							{name : 'serverInstance',index : 'serverInstance',sortable : false,hidden:true, width:'0%'},
							{name : 'serviceInstance',index : 'serviceInstance',sortable : false,hidden:true, width:'0%'},
							{name : 'pluginName',index : 'pluginName',sortable : false,hidden:true, width:'0%'},
							{name : 'pluginType',index : 'pluginType',sortable : false,hidden:true, width:'0%'},
							{name : 'filePath',index : 'filePath',sortable : false,hidden:true, width:'0%'},
							{name : 'fileName',index : 'fileName',sortable : false,hidden:true, width:'0%'},
							{name : 'fileSize',index : 'fileSize',sortable : false,hidden:true, width:'0%'},
							{name : 'category',index : 'category',sortable : false,hidden:true, width:'0%'},
							{name : 'viewDetails',index : 'viewDetails',sortable : false,formatter : checkBatchDetails, align : 'center', width:'10%'},
							
							{name : 'serviceId',index : 'serviceId',sortable : false,hidden:true, width:'0%'},
							{name : 'pluginId',index : 'pluginId',sortable : false,hidden:true, width:'0%'},
							{name : 'composerId',index : 'composerId',sortable : false,hidden:true, width:'0%'},
							{name : 'fileBackupPath',index : 'fileBackupPath',sortable : false,hidden:true, width:'0%'},
							{name : 'isCompress',index : 'isCompress',sortable : false,hidden:true, width:'0%'},
							{name : 'serviceTypeId',index : 'serviceTypeId',sortable : false,hidden:true, width:'0%'},
							{name : 'readFilePath',index : 'readFilePath',sortable : false,hidden:true, width:'0%'},
							{name : 'errorCategory',index : 'errorCategory',sortable : false,hidden:false, width:'0%'},
							{name : 'serverInstanceId',index : 'serverInstanceId',sortable : false,hidden:true, width:'0%'},
							{name : 'isModifiedFileReprocessed',index : 'isModifiedFileReprocessed',sortable : false,hidden:true, width:'0%'},
							{name : 'absoluteFilePath',index : 'absoluteFilePath',sortable : false,hidden:true, width:'0%'},
							
							

				],
				rowNum : 10,
				rowList : [ 10, 20, 60, 100 ],
				height : 'auto',
				mtype : 'POST',
				sortname : 'id',
				sortorder : "asc",
				pager : "#fileBatchStatusListPagingDiv",
				contentType : "application/json; charset=utf-8",
				viewrecords : true,
				multiselect : false,
				timeout : 120000,
				loadtext : jsSpringMsg.gridCaption,
				caption : jsSpringMsg.gridCaption,
				beforeRequest : function() {
					$(".ui-dialog-titlebar").hide();
				},
				beforeSend : function(xhr) {
					xhr.setRequestHeader("Accept", "application/json");
					xhr.setRequestHeader("Content-Type", "application/json");
				},
				loadComplete : function(data) {
					$(".ui-dialog-titlebar").show();
					if ($('#fileBatchStatusList').getGridParam('records') === 0) {
						$('#fileBatchStatusList tbody').html(
								"<div style='padding:6px;background:#D8D8D8;text-align:center;'>no record</div>");
						$("#fileBatchStatusListPagingDiv").hide();
					} else {
						$("#fileBatchStatusListPagingDiv").show();
						viewStatusFileSelected = new Array();
					}
				},
				onPaging : function(pgButton) {
					clearResponseMsgDiv();
					clearGridData('fileBatchStatusList');
				},
				loadError : function(xhr, st, err) {
					handleGenericError(xhr, st, err);
				},
				beforeSelectRow : function(rowid, e) {
					return false;
				},
				onSelectAll : function(id, status) {

				},
				recordtext : jsSpringMsg.recordText,
				emptyrecords : jsSpringMsg.emptyRecord,
				loadtext : jsSpringMsg.loadingText,
				pgtext : jsSpringMsg.pagerText,
			}).navGrid("#fileBatchStatusListPagingDiv", {
		edit : false,
		add : false,
		del : false,
		search : false
	});
	$(".ui-jqgrid-titlebar").hide();
}

var viewStatusCount = 0;
function checkBoxFormatterForViewStatus(cellvalue, options, rowObject) {
	viewStatusCount++;

	var reprocessFileId = rowObject["id"]; 
	var isModifyFileReprocessed = rowObject["isModifiedFileReprocessed"]; 
	
	console.log("modified flag is :: " + isModifyFileReprocessed);
	
	if( (isModifyFileReprocessed == false) && ( rowObject["action"] === "BULK_EDIT"  && (rowObject["status"] === "COMPLETED" || rowObject["status"] === "REVERTED" ))) {
		return "<input type='checkbox' name='viewStatusCheckbox_" + reprocessFileId + "'  id='viewStatusCheckbox_"
		+ reprocessFileId + "' onclick=\"addRowIdForViewStatus('viewStatusCheckbox_" + reprocessFileId + "\',\'" + rowObject["batchId"] + "\',\'" + rowObject["id"] + "\')\"; />";
	} else {
		return "<input type='checkbox' disabled name='viewStatusCheckbox_" + reprocessFileId + "'  id='viewStatusCheckbox_"
		+ reprocessFileId + "' />";
	}
	
	
}

var selectedReprocessIds = [];
function addRowIdForViewStatus(elementId, batchId, detailId) {
	var reprocessFileId = batchId+"-"+detailId;
	
	var reprocessFileElement = document.getElementById(elementId);
	if (reprocessFileElement && reprocessFileElement.checked) {
		if (viewStatusFileSelected.indexOf(reprocessFileId) === -1) {
			viewStatusFileSelected.push(reprocessFileId);
			selectedReprocessIds.push(detailId);
			
		}
	} else {
		if (viewStatusFileSelected.indexOf(reprocessFileId) !== -1) {
			viewStatusFileSelected.splice(viewStatusFileSelected.indexOf(reprocessFileId), 1);
			selectedReprocessIds.splice(selectedReprocessIds.indexOf(detailId), 1);
		}
	}
	disableRevertToOriginalBtn();
}

function disableRevertToOriginalBtn() {
	var flag = false;
	var errorCategoryArray = [];
	$.each(viewStatusFileSelected, function(i, item) {
		var arr = item.split('-');
		var detailId = parseInt(arr[1]);
		var rowObject = jQuery("#fileBatchStatusList").jqGrid ('getRowData', detailId);
		errorCategoryArray.push(rowObject["errorCategory"]);
		if(rowObject["status"] === "REVERTED") {
			flag = true;
		}
	});
	
	
	var isErrorCategoryUnique = errorCategoryArray.allValuesSame();
	
	if(!isErrorCategoryUnique) {
		$("#reprocess_category_validation_msg").show();
		$("#reprocessBtn").attr('disabled',true);
	}else{
		$("#reprocessBtn").removeAttr('disabled');
		$("#reprocess_category_validation_msg").hide();
	}
	
	if(flag) {
		$("#revertToOriginalBtn").attr('disabled',true);
	} else {
		$("#revertToOriginalBtn").removeAttr('disabled');
	}
}

Array.prototype.allValuesSame = function() {
    for(var i = 1; i < this.length; i++) {
        if(this[i] !== this[0]) {
            return false;
        }
    }
    return true;
}

function checkBatchDetails(cellvalue, options, rowObject){
	
	return '<a align="center" class="link" onclick=viewBatchDetails(\''+rowObject["id"]+'\')><i class="fa fa-eye" aria-hidden="true"></i></a>';
}

function viewBatchDetails(id){
	var rowData = jQuery("#fileBatchStatusList").jqGrid('getRowData', id);

	$("#view_end_time").html(rowData.endTime);
	$("#view_status").html(rowData.status);
	$("#view_start_time").html(rowData.startTime);
	$("#view_action_details").html(rowData.actionDetail);
	$("#view_action").html(rowData.action);
	$("#view_reprocessing_id").html(rowData.id);
	$("#view_batch_id").html(rowData.batchId);
	$("#view_service_instance").html(rowData.serviceInstance);
	$("#view_category").html(rowData.category);
	$("#view_server_instance").html(rowData.serverInstance);
	$("#view_fail_reason").html(rowData.failReason);
	$("#view_plugin_name").html(rowData.pluginName);
	$("#view_file_path").html(rowData.filePath);
	$("#view_plugin_type").html(rowData.pluginType);
	$("#view_file_name").html(rowData.fileName);
	$("#view_file_size").html(getFileSizeInKb(rowData.fileSize) + "<b>KB</b>");
	
	$("#viewBatchDetails").click();
}

function resetMultiselectDropdown(elementId) {
	$("#"+elementId).multiselect('destroy');
	$("#"+elementId+" option").each(function(index, option) {
	    $(option).remove();
	});
	$('#'+elementId).multiselect({
		enableFiltering: true,
		enableHTML : true,
		maxHeight: 200,
		dropDown: true,
		buttonWidth: '150px'
	}); 
}