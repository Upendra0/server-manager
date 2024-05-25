/*Function will create new distribution driver  */
function addDistributionDriver(counter) {
	resetWarningDisplay();
	clearAllMessages();
	showProgressBar(counter);

	$
			.ajax({
				url : 'createDistributionDriver',
				cache : false,
				async : true,
				dataType : 'json',
				type : "POST",
				data : {
					"name" : $('#' + counter + '_name').val(),
					"driverType.alias" : $('#' + counter + '_driverType')
							.find(":selected").val(),
					"applicationOrder" : $('#' + counter + '_appOrder').val(),
					"status" : $('#' + counter + '_status').find(":selected")
							.val(),
					"service.id" : $('#serviceId').val(),
					"driverCount" : $('#' + counter + '_driverCount').val(),

				},

				success : function(data) {
					hideProgressBar(counter);

					var response = data;

					var responseCode = response.code;
					var responseMsg = response.msg;
					var responseObject = response.object;

					if (responseCode === "200") {
						resetWarningDisplay();
						clearAllMessages();
						showSuccessMsg(responseMsg);
						$("#link_" + counter).text(
								responseObject["applicationOrder"] + "-"
										+ responseObject["name"]);
						$("#link_" + counter)
								.css('textDecoration', 'underline');

						$("#" + counter + "_updatebtn").show();
						$("#" + counter + "_savebtn").hide();
						$('#' + counter + '_driverId')
								.val(responseObject["id"]);
						$('#' + counter + '_driverAlias').val(
								$('#' + counter + '_driverType').find(
										":selected").val());
						$("#" + counter + "_block").collapse("toggle");

						var driver = {
							'id' : responseObject["id"],
							'type' : $('#' + counter + '_driverType').find(
									":selected").val(),
							'alias' : $('#' + counter + '_driverType').find(
									":selected").val(),
							'name' : responseObject["name"],
							'order' : responseObject["applicationOrder"],
							'timeout' : $('#' + counter + '_timeout').val(),
							'status' : $('#' + counter + '_status').val()
						};

						distributionDriverListDetail.push(driver);
						countTotalDriversExist++;
						displayReorderLink();

					} else if (responseObject !== undefined
							&& responseObject !== 'undefined'
							&& responseCode === "400") {

						hideProgressBar(counter);
						addErrorIconAndMsgForAjax(responseObject);

					} else {
						hideProgressBar(counter);
						resetWarningDisplay();
						clearAllMessages();
						showErrorMsg(responseMsg);
					}
				},
				error : function(xhr, st, err) {
					hideProgressBar(counter);
					handleGenericError(xhr, st, err);
				}
			});
}

/* Function will update distribution driver details */
function updateDistributionDriver(driverListCounter) {
	resetWarningDisplay();
	clearAllMessages();
	showProgressBar(driverListCounter);
	parent.resizeFancyBox();
	var urlAction = jsSpringMsg.updateFTPDriver;
	var distributionDriverType = $('#' + driverListCounter + '_driverType')
			.find(":selected").val();

	if (distributionDriverType === jsSpringMsg.ftpDistributionDriver) {
		urlAction = jsSpringMsg.updateFTPDriver;
	} else if (distributionDriverType === jsSpringMsg.sftpDistributionDriver) {
		urlAction = jsSpringMsg.updateSFTPDriver;
	} else if (distributionDriverType === jsSpringMsg.localDistributionDriver) {
		urlAction = jsSpringMsg.updateLocalDriver;
	} else if (distributionDriverType === jsSpringMsg.databaseDistributionDriver) {
		urlAction = jsSpringMsg.updateDatabaseDriver;
	}

	$
			.ajax({
				url : urlAction,
				cache : false,
				async : true,
				dataType : 'json',
				type : "POST",
				data : {
					"id" : $('#' + driverListCounter + '_driverId').val(),
					"name" : $('#' + driverListCounter + '_name').val(),
					"driverType.alias" : distributionDriverType,
					"applicationOrder" : $(
							'#' + driverListCounter + '_appOrder').val(),
					"timeout" : $('#' + driverListCounter + '_timeout').val(),
					"status" : $('#' + driverListCounter + '_status').find(
							":selected").val(),
					"service.id" : $('#serviceId').val(),
					"driverCount" : $('#' + driverListCounter + '_driverCount')
							.val(),

				},

				success : function(data) {
					hideProgressBar(driverListCounter);

					var response = data;

					var responseCode = response.code;
					var responseMsg = response.msg;
					var responseObject = response.object;

					if (responseCode === "200") {
						resetWarningDisplay();
						clearAllMessages();
						showSuccessMsg(responseMsg);

						$("#link_" + driverListCounter).text(
								responseObject["applicationOrder"] + "-"
										+ responseObject["name"]);
						$("#link_" + driverListCounter).css('textDecoration',
								'underline');
						$("#" + driverListCounter + "_block")
								.collapse("toggle");

						var driver = {
							'id' : $('#' + driverListCounter + '_driverId')
									.val(),
							'type' : $(
									'#' + driverListCounter + '_driverType')
									.find(":selected").val(),
							'alias' : $(
									'#' + driverListCounter + '_driverType')
									.find(":selected").val(),
							'name' : $('#' + driverListCounter + '_name').val(),
							'order' : $('#' + driverListCounter + '_appOrder')
									.val(),
							'timeout' : $('#' + driverListCounter + '_timeout')
									.val(),
							'status' : $('#' + driverListCounter + '_status')
									.find(":selected").val()
						};

						for (var index = 0; index < distributionDriverListDetail.length; index++) {
							if (distributionDriverListDetail[index].id === $(
									'#' + driverListCounter + '_driverId')
									.val())
								distributionDriverListDetail[index] = driver;
						}

					} else if (responseObject !== undefined
							&& responseObject !== 'undefined'
							&& responseCode === "400") {
						hideProgressBar(driverListCounter);
						showErrorMsg(responseMsg);
						addErrorIconAndMsgForAjax(responseObject);

					} else {
						hideProgressBar(driverListCounter);
						resetWarningDisplay();
						clearAllMessages();
						showErrorMsg(responseMsg);
					}
				},
				error : function(xhr, st, err) {
					hideProgressBar(driverListCounter);
					handleGenericError(xhr, st, err);
				}
			});
}

/* Function will display progress bar for add/edit action in pop-up. */
function showProgressBar(driverListCounter) {
	$('#' + driverListCounter + '_buttons-div').hide();
	$('#' + driverListCounter + '_progress-bar-div').show();
}
/* Function will hide progress bar for add/edit action in pop-up. */
function hideProgressBar(driverListCounter) {
	$('#' + driverListCounter + '_buttons-div').show();
	$('#' + driverListCounter + '_progress-bar-div').hide();
}

var blockCounter, deleteDriverId;
/* Function will display delete driver pop-up */
function deleteDistributionDriverPopup(counter) {

	$("#active-driver-div").hide();
	$("#inactive-driver-div").hide();

	blockCounter = counter;

	deleteDriverId = $('#' + counter + '_driverId').val();

	if (deleteDriverId === null || deleteDriverId === 'null'
			|| deleteDriverId === '') {
		$("#" + counter + "_form").remove();
	} else {
		var driverStatus = $("#" + blockCounter + "_status").val();

		if (driverStatus === "INACTIVE") {
			$("#deleteWarningMessage").show();
			$("#inactive-driver-div").show();
			$("#warningMessage").hide();
			$("#active-driver-div").hide();
			$("#driverMessage").click();
		} else {
			$("#deleteWarningMessage").hide();
			$("#warningMessage").show();
			$("#inactive-driver-div").hide();
			$("#active-driver-div").show();

			$("#driverMessage").click();
		}
	}
}

/*
 * Function will delete distribution driver and update application order for
 * each driver.
 */
function deleteDistributionDriver() {
	resetWarningDisplay();
	clearAllMessages();

	var blockId = $('#deleteBlockId').val();
	$
			.ajax({
				url : 'deleteDistributionDriver',
				cache : false,
				async : true,
				dataType : 'json',
				type : "POST",
				data : {
					"driverId" : deleteDriverId,
					"serviceId" : $("#serviceId").val(),
				},

				success : function(data) {
					hideProgressBar(driverListCounter);
					var response = data;
					var responseCode = response.code;
					var responseMsg = response.msg;
					var responseObject = response.object;
					$("#deleteWarningMessage").hide();
					$("#inactive-driver-div").hide();
					$("#warningMessage").hide();
					$("#active-driver-div").hide();

					$("#warning-title").hide();
					$("#status-title").show();

					if (responseCode === "200") {
						resetWarningDisplay();
						clearAllMessages();
						$("#deleteDriverResponseMsg").show();
						showSuccessMsgPopUp(responseMsg);
						$("#reaload-driver-details").show();
						parent.resizeFancyBox();

					} else if (responseObject !== undefined
							&& responseObject !== 'undefined'
							&& responseCode === "400") {

						showErrorMsgPopUp(responseMsg);
						parent.resizeFancyBox();
						$("#reaload-driver-details").show();

					} else {

						resetWarningDisplay();
						clearAllMessages();
						showErrorMsgPopUp(responseMsg);
						$("#reaload-driver-details").show();
						parent.resizeFancyBox();
					}
				},
				error : function(xhr, st, err) {
					handleGenericError(xhr, st, err);
				}
			});

}

/* Function will reload driver details. */
function reloadDriverDetails() {
	closeFancyBox();
	loadDistributionDetails('initDistributionDriverManager', 'POST',
			'DISTRIBUTION_DRIVER_CONFIGURATION');
}

/* Function will reset selected block parameters */
function resetDistributionDriverDetailDiv(divCounter) {
	resetWarningDisplay();
	clearAllMessages();
	$('#' + divCounter + '_name').val('');
}

function displayApplicationOrderPopup() {

	$('#draggableDriverDiv').html('');

	for (var index = 0; index < distributionDriverListDetail.length; index++) {

		var tabstring = "<li style='cursor:pointer;border:1px solid #ddd;margin-bottom:5px;' name = " + distributionDriverListDetail[index].id 
	    + " id=" + distributionDriverListDetail[index].name.replace(new RegExp(" ", 'g'), "_") +"_"+distributionDriverListDetail[index].alias+ ">";
		
		tabstring += "<table class='table table-hover' style='margin-bottom:10px;border-top:0px;'>";
		tabstring += "<tr id=" + distributionDriverListDetail[index].id +">";
		tabstring += "<td width='30%'>"
				+ distributionDriverListDetail[index].name + "</td>";
		tabstring += "<td width='30%'>"
				+ distributionDriverListDetail[index].type + "</td>";
		tabstring += "<td width='30%'>"
				+ distributionDriverListDetail[index].alias + "</td>";
		tabstring += "</table>";
		tabstring += "<li>";

		$('#draggableDriverDiv').append(tabstring);
	}

	$('#application_popup_link').click();
}

/* Function will reset number for application order. */
/*function changeDriverOrder(driverId, order) {
	console.log("driverId:"+driverId);
	console.log("order:"+order);
	for (var index = 0; index < distributionDriverListDetail.length; index++) {
		console.log("outside if ::" + distributionDriverListDetail[index].id
				+ "::" + distributionDriverListDetail[index].order);
		if (distributionDriverListDetail[index].id == driverId){
			console.log("inside if ::" + distributionDriverListDetail[index].id
					+ "::" + distributionDriverListDetail[index].order);
			distributionDriverListDetail[index].order = order;
		}
	}
}
*/

function changeDriverOrder(driverId,order){
	for(var index=0;index<distributionDriverListDetail.length;index++){
		if(distributionDriverListDetail[index].id==driverId)
			distributionDriverListDetail[index].order=order;
	}
}

/*function resetNumbering() {
	var listItems = $("#draggableDriverDiv li");
	var counter = 0;
	listItems.each(function(idx, li) {
		var ele = $(li);
		console.log($(ele).attr('id'));
		if ($(ele).attr('id') === undefined)
			$(ele).remove();
		else
			changeDriverOrder($(ele).attr('id'), counter++);
	});

	for (var index = 0; index < distributionDriverListDetail.length; index++) {
		console.log("resetNumbering::" + distributionDriverListDetail[index].id
				+ "::" + distributionDriverListDetail[index].order);
	}

	updateDistributionDriverOrder();
}
*/
function resetNumbering(){
	
	var listItems = $("#draggableDriverDiv li");
	var counter=0;
	listItems.each(function(idx, li) {
	    var ele = $(li);
	    if($(ele).attr('name')==undefined)
	    	$(ele).remove();
	    else
	    	changeDriverOrder($(ele).attr('name'),counter++);
	});
	
	updateDistributionDriverOrder();
}



/* Function will update distribution driver application order. */
function updateDistributionDriverOrder() {
	resetWarningDisplay();
	clearAllMessages();
	parent.resizeFancyBox();

	var orderArray = [];

	for (var index = 0; index < distributionDriverListDetail.length; index++) {
		var driver = {};
		driver.driverId = distributionDriverListDetail[index].id;
		driver.order = distributionDriverListDetail[index].order;
		console.log(driver.driverId + "::" + driver.order);
		orderArray.push(driver);
	}

	$('#reorder-buttons-div').hide();
	$('#reorder-progress-bar-div').show();

	$
			.ajax({
				url : 'updateDistributionDriverOrder',
				cache : false,
				async : true,
				dataType : 'json',
				type : "POST",
				data : {
					'driverOrderList' : JSON.stringify(orderArray)
				},

				success : function(data) {
					$('#reorder-buttons-div').show();
					$('#reorder-progress-bar-div').hide();

					var response = data;

					var responseCode = response.code;
					var responseMsg = response.msg;
					var responseObject = response.object;

					if (responseCode === "200") {
						resetWarningDisplay();
						clearAllMessages();
						showSuccessMsg(responseMsg);

						$('#driverList').html('');
						closeFancyBox();
						driverListCounter = -1;
						distributionDriverListDetail = [];

						var driverList = responseObject;
						for (var index = 0; index < driverList.length; index++) {

							var driver = driverList[index];
							var driverType = JSON.parse(driver.driverType);

							addUpdateDriverDetail(driver.id, driverType.type,
									driverType.alias, driver.name,
									driver.applicationOrder, driver.status,
									'UPDATE');

							var driverJsonObject = {
								'id' : driver.id,
								'type' : driverType.type,
								'alias' : driverType.alias,
								'name' : driver.name,
								'order' : driver.applicationOrder,
								'timeout' : driver.timeout,
								'status' : driver.status
							};

							distributionDriverListDetail.push(driverJsonObject);
						}
					} else if (responseObject !== undefined
							&& responseObject !== 'undefined'
							&& responseCode === "400") {
						hideProgressBar(driverListCounter);
						showErrorMsg(responseMsg);
						addErrorIconAndMsgForAjax(responseObject);
						closeFancyBox();
					} else {
						hideProgressBar(driverListCounter);
						resetWarningDisplay();
						clearAllMessages();
						showErrorMsg(responseMsg);
						closeFancyBox();
					}
				},
				error : function(xhr, st, err) {
					hideProgressBar(driverListCounter);
					handleGenericError(xhr, st, err);
				}
			});
}

/* Function will redirect to driver configuration page. */
function redirectToDriverConfig(driverListCounter) {

	$('#driverTypeAlias')
			.val($('#' + driverListCounter + '_driverAlias').val());
	$('#driverId').val($('#' + driverListCounter + '_driverId').val());
	$('#distribution-driver-config-form').submit();
}
/* Function will redirect to distribution service */
function viewDistributionService() {
	$("#distribution_service_form").submit();
}

/*
 * Function will load data for distribution driver configuration and pathlist
 * configuration tab
 */
function loadDriverTabData(formAction) {
	$("#loadDistributionDriverConfiguration").attr("action", formAction);
	$("#loadDistributionDriverConfiguration").submit();
}

/* Function will load dynamic jqgrid for distribution driver pathlist */
function loadComposerPluginGrid(counter, composerList, pathname) {
	var selectAllCheckboxId = pathname+"_selectAllPlugin";
	var childCheckboxName = counter+"_pluginCheckbox"
	var uniqueGridId = counter + "_grid"; 
	$("#" + uniqueGridId)
			.jqGrid(
					{
						url : "",
						datatype : "local",
						colNames : [ "id",
						             "<input type='checkbox' id='"+selectAllCheckboxId+"' onclick='pluginHeaderCheckbox(event, this, \""+childCheckboxName+"\")'></input>",
						             jsSpringMsg.pluginName,
								jsSpringMsg.pluginType, "pluginTypeId", "",
								"readFilenamePrefix", "readFilenameSuffix",
								"readFilenameContains",
								"readFilenameExcludeTypes",
								"compressOutFileEnabled",
								"writeFilenamePrefix", "writeFilenameSuffix",
								"fileExtension",
								"defaultFileExtensionRemoveEnabled", 
								"fileSplitEnabled",
								jsSpringMsg.writeFilePath,
								"Additional Params",
								jsSpringMsg.characterRename, "fileBackupPath",
								"composerMappingId", jsSpringMsg.editAction, ],
						colModel : [ {
							name : 'id',
							index : 'id',
							hidden : true,
							sortable : false
						}, {
							name : 'checkbox',
							index : 'checkbox',
							width : '20%',
							sortable : false
						}, {
							name : 'name',
							index : 'name',
							sortable : false
						}, {
							name : 'composerType',
							index : 'composerType',
							sortable : false
						}, {
							name : 'composerTypeId',
							index : 'composerTypeId',
							sortable : false,
							hidden : true
						}, {
							name : 'composerTypeAlias',
							index : 'composerTypeAlias',
							sortable : false,
							hidden : true
						}, {
							name : 'readFilenamePrefix',
							index : 'readFilenamePrefix',
							sortable : false,
							hidden : true
						}, {
							name : 'readFilenameSuffix',
							index : 'readFilenameSuffix',
							sortable : false,
							hidden : true
						}, {
							name : 'readFilenameContains',
							index : 'readFilenameContains',
							sortable : false,
							hidden : true
						}, {
							name : 'readFilenameExcludeTypes',
							index : 'readFilenameExcludeTypes',
							sortable : false,
							hidden : true
						}, {
							name : 'compressOutFileEnabled',
							index : 'compressOutFileEnabled',
							sortable : false,
							hidden : true
						}, {
							name : 'writeFilenamePrefix',
							index : 'writeFilenamePrefix',
							sortable : false,
							hidden : true
						}, {
							name : 'writeFilenameSuffix',
							index : 'writeFilenamePrefix',
							sortable : false,
							hidden : true
						}, {
							name : 'fileExtension',
							index : 'fileExtension',
							sortable : false,
							hidden : true
						}, {
							name : 'defaultFileExtensionRemoveEnabled',
							index : 'defaultFileExtensionRemoveEnabled',
							sortable : false,
							hidden : true	
							
						}, {
							name : 'fileSplitEnabled',
							index : 'fileSplitEnabled',
							sortable : false,
							hidden : true	
							
						}, {
							name : 'destPath',
							index : 'destPath',
							sortable : false
						}, {
							name : 'additionalParam',
							index : 'additionalParam',
							sortable : false,
							align : 'center'
						}, {
							name : 'characterRename',
							index : 'characterRename',
							sortable : false,
							align : 'center'
						}, {
							name : 'fileBackupPath',
							index : 'fileBackupPath',
							sortable : false,
							hidden : true
						}, {
							name : 'composerMappingId',
							index : 'composerMappingId',
							sortable : false,
							hidden : true
						}, {
							name : 'edit',
							index : 'edit',
							align : 'center',
							sortable : false
						} ],
						rowNum : jsSpringMsg.totalRows,
						rowList : [ 10, 20, 60, 100 ],
						height : 'auto',
						mtype : 'POST',
						sortname : 'id',
						sortorder : "desc",
						pager : "#" + counter + "_gridPagingDiv",
						contentType : "application/json; charset=utf-8",
						viewrecords : true,
						// multiselect : true,
						timeout : 120000,
						loadtext : jsSpringMsg.loadingText,
						caption : "",
						beforeRequest : function() {
							$(".ui-dialog-titlebar").hide();
						},
						beforeSend : function(xhr) {
							xhr.setRequestHeader("Accept", "application/json");
							xhr.setRequestHeader("Content-Type",
									"application/json");
						},
						loadComplete : function() {
							$(".ui-dialog-titlebar").show();

							$
									.each(
											composerList,
											function(index, composer) {
												var uniqueId = "checkbox";
												if(composer.name !== "") {
													uniqueId += "_" + composer.name; 
												}
												var rowData = {};
												rowData.id = parseInt(composer.id);
												rowData.checkbox = '<input type="checkbox" id="'+uniqueId+'" name="'+childCheckboxName+'" value="'+rowData.id+'" onchange="updateRootCheckbox(this, \''+uniqueGridId+'\', \''+selectAllCheckboxId+'\', \''+childCheckboxName+'\')"/>';
												rowData.name = '<a id="plugin_link_txt" href="#" class="link" onclick="redirectComposerConfiguration('
														+ "'"
														+ rowData.id
														+ "','"
														+ composer.name
														+ "','"
														+ composer.pluginTypeAlias
														+ "'"
														+ ');" >'
														+ composer.name
														+ '</a>';
												
												rowData.composerTypeId = composer.pluginTypeId;
												rowData.composerType = composer.pluginType;
												rowData.composerTypeAlias = composer.pluginTypeAlias;
												rowData.readFilenamePrefix = composer.readFilenamePrefix;
												rowData.readFilenameSuffix = composer.readFilenameSuffix;
												rowData.readFilenameContains = composer.readFilenameContains;
												rowData.readFilenameExcludeTypes = composer.readFilenameExcludeTypes;
												rowData.compressOutFileEnabled = composer.compressOutFileEnabled;
												rowData.writeFilenamePrefix = composer.writeFilenamePrefix;
												rowData.writeFilenameSuffix = composer.writeFilenameSuffix;
												rowData.fileExtension = composer.fileExtension;
												rowData.defaultFileExtensionRemoveEnabled = composer.defaultFileExtensionRemoveEnabled;
												rowData.fileSplitEnabled = composer.fileSplitEnabled;
												rowData.destPath = composer.destPath;
												rowData.additionalParam = "<a href='#' class='link' id='view_additional_param_"+pathname +"_"+composer.name+"_lnk' onclick=showDetailedParameters('"
														+ rowData.id
														+ "','"
														+ counter
														+ "'); ><i class='fa fa-list' aria-hidden='true' style='cursor:pointer;'></i></a>";
												rowData.characterRename = "<a href='#' class='link' id='char_rename_"+pathname +"_"+composer.name+"_lnk' onclick=viewCharRenameOperationByPlugin('"
														+ rowData.id
														+ "'); ><i class='fa fa-list' aria-hidden='true' style='cursor:pointer;'></i></a>";
												rowData.fileBackupPath = composer.fileBackupPath;
												rowData.composerMappingId = composer.composerMappingId;
												rowData.edit = "<a href='#' class='link' id='edit_plugin_"+pathname +"_"+composer.name+"_lnk' onclick=displayPluginEditPopup(\'EDIT\','"
														+ rowData.id
														+ "','"
														+ counter
														+ "'); ><i class='fa fa-pencil-square-o' aria-hidden='true'></i></a>";
												
												if(composer.pluginType === 'Default Composer'){
													rowData.name = '<a href="#" class="link" style="text-decoration:none;">'
														+ composer.name
														+ '</a>';;
													rowData.additionalParam = '';
													rowData.characterRename = '';
												}
												jQuery("#" + counter + "_grid")
														.jqGrid('addRowData',
																rowData.id,
																rowData);
											});
						},
						onPaging : function() {
						},
						loadError : function(xhr, st, err) {
							handleGenericError(xhr, st, err);
						},
						onSelectAll : function() {
						},
						recordtext : jsSpringMsg.totalRecordText,
						emptyrecords : jsSpringMsg.emptyRecordText,
						loadtext : jsSpringMsg.loadingText,
						pgtext : jsSpringMsg.pagerText,
					}).navGrid("#gridPagingDiv", {
				edit : false,
				add : false,
				del : false,
				search : false
			});

	$(".ui-jqgrid-titlebar").hide();
}

/*
 * this function will handle select all/ de select all event
 */
function pluginHeaderCheckbox(e, element, childCheckboxName) {
    e = e || event;/* get IE event ( not passed ) */
    e.stopPropagation ? e.stopPropagation() : e.cancelBubble = false;

    // if root checkbox is checked :-> all child checkbox should ne checked
    if(element.checked) {
    	$('input:checkbox[name="'+childCheckboxName+'"]').prop('checked',true);
    } else {
    	// if root checkbox is unchecked :-> all child checkbox should be
		// unchecked
    	$('input:checkbox[name="'+childCheckboxName+'"]').prop('checked',false);
    }
}

/*
 * this function will handle root checkbox from child checkbox
 */
function updateRootCheckbox(element, gridId, rootCheckboxId, childCheckboxName) {
	if(!element.checked){
		// if current child checkbox is not checked : uncheck root checkbox
		$('input:checkbox[id="'+rootCheckboxId+'"]').prop('checked',false);
	} else {
		// if current child checkboox is checked and all checkbox are checked
		// then check root checkbox
		var count = jQuery("#"+gridId).jqGrid('getGridParam', 'records');
		if ($('input:checkbox[name="'+childCheckboxName+'"]:checked').length === count) {
			$('input:checkbox[id="'+rootCheckboxId+'"]').prop('checked',true);
	    }
	}
}

/* Function will reset pathlist parameter for selected block */
function resetPathlistParams(counter) {
	resetWarningDisplay();
	$('#' + counter + '_block input').val('');
	$("#"+counter+"_fileGrepDateEnabled").val('false');
	$("#"+counter+"_position").val('left')
	$("#"+counter+"_startIndex").val('-1')
	$("#"+counter+"_endIndex").val('-1')
	$("#"+counter+"_dateFormat").val('yyyyMMddHHmm');
	changeFileDateParam(counter);
}

function changeFileDateParam(pathListCounter){
	
	var fileDateEnable=$("#"+pathListCounter+"_fileGrepDateEnabled").find(":selected").val();
	
	if(fileDateEnable == 'false'){
		$("#"+pathListCounter+"_dateFormat").prop('readonly', true);
		$("#"+pathListCounter+"_position").prop('disabled', true);
		$("#"+pathListCounter+"_startIndex").prop('readonly', true);
		$("#"+pathListCounter+"_endIndex").prop('readonly', true);
	}else{
		$("#"+pathListCounter+"_dateFormat").prop('readonly', false);
		$("#"+pathListCounter+"_position").prop('disabled', false);
		$("#"+pathListCounter+"_startIndex").prop('readonly', false);
		$("#"+pathListCounter+"_endIndex").prop('readonly', false);
	}
	
}

/* Function will create new distribution pathlist */
function addDistributionPathlist(counter) {
	resetWarningDisplay();
	clearAllMessages();
	showProgressBar(counter);

	var maxFileCountAlert = $("#" + counter + "_maxFilesCountAlert").val();
	var startIndex = $("#"+counter+"_startIndex").val();
	var endIndex = $("#"+counter+"_endIndex").val();
	
	if (maxFileCountAlert === null || maxFileCountAlert === ''
			|| maxFileCountAlert === 'undefined ') {
		maxFileCountAlert = -1;
		$("#" + counter + "_maxFilesCountAlert").val(maxFileCountAlert);
	}
	
	if(startIndex === null || startIndex === '' || startIndex === 'undefined' || startIndex === undefined){
		startIndex = -1;
		$("#"+counter+"_startIndex").val("-1");
	}
	if(endIndex === null || endIndex === '' || endIndex === 'undefined' || endIndex === undefined){
		endIndex = -1;
		$("#"+counter+"_endIndex").val("-1");
	}
	$.ajax({
		url : jsSpringMsg.addPathlistAction,
		cache : false,
		async : true,
		dataType : 'json',
		type : "POST",
		data : {
			"id"                          : '0',
			"name"                        : $("#" + counter + "_name").val(),
			"readFilePath"                : $("#" + counter + "_readFilePath").val(),
			"maxFilesCountAlert"          : maxFileCountAlert,
			"compressInFileEnabled"       : $("#" + counter + "_compressInFileEnabled").val(),
			"fileNamePattern"           :   $('#' + counter + '_fileNamePattern').val(),
			"readFilenamePrefix"          : $("#" + counter + "_readFilenamePrefix").val(),
			"readFilenameSuffix"          : $("#" + counter + "_readFilenameSuffix").val(),
			"readFilenameContains"        : $("#" + counter + "_readFilenameContains").val(),
			"readFilenameExcludeTypes"    : $("#" + counter + "_readFilenameExcludeTypes").val(),
			"writeFilePath"               : $("#" + counter + "_writeFilePath").val(),
			"compressOutFileEnabled"      : $("#" + counter + "_compressOutFileEnabled").val(),
			"archivePath"                 : $("#" + counter + "_archivePath").val(),
			"driver.id"                   : $("#" + counter + "_driverId").val(),
			"fileGrepDateEnabled"         : $("#"+counter+"_fileGrepDateEnabled").val(),
			"position"			          : $("#"+counter+"_position").val(),
			"startIndex"		          : parseInt(startIndex),
			"endIndex"			          : parseInt(endIndex),
			"dateFormat"                  : $("#"+counter+"_dateFormat").val(),
			"pathListCount"               : counter,
			"pathId"				      :	$('#'+counter+'_pathId').val(),
			"referenceDevice"			  : $('#' + counter + '_parentDevice').val(),
			"parentDevice.id"			  : $('#' + counter + '_deviceId').val(),
			"service.id"                  : $('#serviceId').val()
		},
		success : function(data) {
			hideProgressBar(counter);

			var responseCode = data.code;
			var responseMsg = data.msg;
			var responseObject = data.object;

			if (responseCode === "200") {
				resetWarningDisplay();
				clearAllMessages();

				var respObj = responseObject;

				var pathListId = respObj['id'];
				// set pathlist id hidden for all pathlist block
				$('#' + counter + '_pathListId').val(pathListId);
				// update block title with path list name
				$('#title_' + counter).html(respObj['name']);

				$('#' + counter + '_grid_links_div').show();
				$('#' + counter + '_grid_main_div').show();

				loadComposerPluginGrid(counter, [], respObj['name']);

				$('#' + counter + '_updatebtn').show();
				$('#' + counter + '_savebtn').hide();
	 			$('#' + counter + '_pathId').val(respObj['pathId']);

				showSuccessMsg(responseMsg);

			} else if (responseObject !== undefined
					&& responseObject !== 'undefined'
					&& responseCode === "400") {
				hideProgressBar(counter);
				addErrorIconAndMsgForAjax(responseObject);
			} else {
				hideProgressBar(counter);
				resetWarningDisplay();
				clearAllMessages();
				showErrorMsg(responseMsg);
			}
		},
		error : function(xhr, st, err) {
			hideProgressBar(counter);
			handleGenericError(xhr, st, err);
		}
	});
}

/* Function will update distribution drive path list */
function updateDistributionPathList(counter) {
	resetWarningDisplay();
	clearAllMessages();
	showProgressBar(counter);

	var maxFileCountAlert = $("#" + counter + "_maxFilesCountAlert").val();
	var startIndex = $("#"+counter+"_startIndex").val();
	var endIndex = $("#"+counter+"_endIndex").val();
	if (maxFileCountAlert === null || maxFileCountAlert === ''
			|| maxFileCountAlert === 'undefined ') {
		maxFileCountAlert = -1;
		$("#" + counter + "_maxFilesCountAlert").val(maxFileCountAlert);
	}

	if(startIndex === null || startIndex === '' || startIndex === 'undefined' || startIndex === undefined){
		startIndex = -1;
		$("#"+counter+"_startIndex").val("-1");
	}
	if(endIndex === null || endIndex === '' || endIndex === 'undefined' || endIndex === undefined){
		endIndex = -1;
		$("#"+counter+"_endIndex").val("-1");
	}
	$.ajax({
		url : jsSpringMsg.updatePathlistAction,
		cache : false,
		async : true,
		dataType : 'json',
		type : "POST",
		data : {
			"id"                        : $("#" + counter + "_pathListId").val(),
			"name"                      : $("#" + counter + "_name").val(),
			"readFilePath"              : $("#" + counter + "_readFilePath").val(),
			"maxFilesCountAlert"        : maxFileCountAlert,
			"compressInFileEnabled"     : $("#" + counter + "_compressInFileEnabled").val(),
			"fileNamePattern"           :   $('#' + counter + '_fileNamePattern').val(),
			"readFilenamePrefix"        : $("#" + counter + "_readFilenamePrefix").val(),
			"readFilenameSuffix"        : $("#" + counter + "_readFilenameSuffix").val(),
			"readFilenameContains"      : $("#" + counter + "_readFilenameContains").val(),
			"readFilenameExcludeTypes"  : $("#" + counter + "_readFilenameExcludeTypes").val(),
			"writeFilePath"             : $("#" + counter + "_writeFilePath").val(),
			"compressOutFileEnabled"    : $("#" + counter + "_compressOutFileEnabled").val(),
			"archivePath"               : $("#" + counter + "_archivePath").val(),
			"driver.id"                 : $("#" + counter + "_driverId").val(),
			"fileGrepDateEnabled"       : $("#"+counter+"_fileGrepDateEnabled").val(),
			"position"			        : $("#"+counter+"_position").val(),
			"startIndex"		        : parseInt(startIndex),
			"endIndex"		        	: parseInt(endIndex),
			"dateFormat"                : $("#"+counter+"_dateFormat").val(),
			"pathListCount"             : counter,
			"pathId"                    : $("#"+counter+"_pathId").val(),
			"service.id"                : $('#serviceId').val(),
			"referenceDevice"			: $('#' + counter + '_parentDevice').val(),
			"parentDevice.id"			: $('#' + counter + '_deviceId').val(),
			"service.servInstanceId"	: $('#serviceInstanceId').val(),
			"dbReadFileNameExtraSuffix"	: $('#' + counter + '_dbReadFileNameExtraSuffix').val()
		},
		success : function(data) {
			hideProgressBar(counter);

			var responseCode = data.code;
			var responseMsg = data.msg;
			var responseObject = data.object;

			if (responseCode === "200") {
				resetWarningDisplay();
				clearAllMessages();

				var respObj = responseObject;
				var pathListId = respObj['id'];

				// set pathlist id hidden for all pathlist block
				$('#' + counter + '_pathListId').val(pathListId);
				// update block title with path list name

				$('#title_' + counter).html(respObj['name']);
				$('#' + counter + '_updatebtn').show();
				$('#' + counter + '_savebtn').hide();

				showSuccessMsg(responseMsg);
				closeFancyBox();

			} else if (responseObject !== undefined
					&& responseObject !== 'undefined'
					&& responseCode === "400") {
				hideProgressBar(counter);
				addErrorIconAndMsgForAjax(responseObject);
			} else {
				hideProgressBar(counter);
				resetWarningDisplay();
				clearAllMessages();
				showErrorMsg(responseMsg);
			}
		},
		error : function(xhr, st, err) {
			hideProgressBar(counter);
			handleGenericError(xhr, st, err);
		}
	});
}

/* Function will delete pathlist or remove blank block */
var deleteBlockId = '';
function deletePathListDetails(counter) {

	var pathListId = $('#' + counter + '_pathListId').val();
	deleteBlockId = counter;

	if (pathListId === null || pathListId === 'null' || pathListId === '') {
		$("#flipbox_" + counter).remove();
	} else {
		$("#pathlist_delete_link").click();
	}
}

/* Function will remove distribution driver pathlist */
function deleteDistributionDriverPathList() {
	resetWarningDisplay();
	clearAllMessages();
	$
			.ajax({
				url : 'deleteDistributionDriverPathList',
				cache : false,
				async : true,
				dataType : 'json',
				type : "POST",
				data : {
					"pathlistId" : $('#' + deleteBlockId + '_pathListId').val()
				},

				success : function(data) {
					var response = data;
					var responseCode = response.code;
					var responseMsg = response.msg;
					var responseObject = response.object;
					if (responseCode === "200") {
						resetWarningDisplay();
						clearAllMessages();
						showSuccessMsg(responseMsg);
						$("#flipbox_" + deleteBlockId).remove();
						closeFancyBox();
					} else if (responseObject !== undefined
							&& responseObject !== 'undefined'
							&& responseCode === "400") {
						showErrorMsg(responseMsg);
						closeFancyBox();
					} else {
						resetWarningDisplay();
						clearAllMessages();
						showErrorMsg(responseMsg);
						closeFancyBox();
					}
				},
				error : function(xhr, st, err) {
					handleGenericError(xhr, st, err);
				}
			});
}

/* Function will display add composer plugin pop-up */
function openAddComposerPluginPopup(counter, pathName) {

	clearAllMessagesPopUp();
	resetWarningDisplayPopUp();

	$("#pathlist_id").val($("#" + counter + "_pathListId").val());
	$("#current_block_count").val(counter);
	$("#current_block_path_name").val($("#" + counter + "_name").val());
	$("#mappingId").val('0');
	$("#composerPluginId").val('0');
	resetPluginParams();

	$("#add_plugin_buttons_div").show();
	$("#update_plugin_buttons_div").hide();

	$("#view_plugin_buttons_div").hide();
	$("#view_progress-bar-div").hide();
	$("#add_plugin_header_label").show();
	$("#update_plugin_header_label").hide();
	$("#add_composer_plugin_link").click();
	$('#composerPluginType').attr('disabled', false);
	$("#charRename_div").html('');
	$("#char_rename_add_link").hide();

}

function resetPluginParams() {
	$('#name').val('');
	$('#readFilenamePrefix').val('');
	$('#readFilenameSuffix').val('');
	$('#readFilenameContains').val('');
	$('#readFilenameExcludeTypes').val('');
	$('#destPath').val('');
	$('#writeFilenamePrefix').val('');
	$('#fileBackupPath').val('');
}

/* Function will add new composer plugin details */
function addComposerPluginDetails() {
	resetWarningDisplay();
	clearAllMessages();

	$("#view_progress-bar-div").show();
	$("#add_plugin_buttons_div").hide();

	var counter = $('#current_block_count').val();
	var pathName = $('#current_block_path_name').val()
	
	var selectAllCheckboxId = pathName+"_selectAllPlugin";
	var childCheckboxName = counter+"_pluginCheckbox"
	var uniqueGridId = counter + "_grid"; 
	
	var composerObj = {};

	composerObj['myDistDrvPathlist.id'] = $('#pathlist_id').val();
	composerObj['id'] = -1;
	composerObj['name'] = $('#name').val();
	composerObj['composerType.id'] = $('#composerPluginType').val();
	composerObj['composerType'] = $('#composerPluginType option:selected')
			.text();
	composerObj['destPath'] = $('#destPath').val();
	composerObj['writeFilenamePrefix'] = $('#writeFilenamePrefix').val();
	composerObj['writeFilenameSuffix'] = $('#writeFilenameSuffix').val();
	composerObj['fileBackupPath'] = $('#fileBackupPath').val();
	composerObj['fileExtension'] = $('#fileExtension').val();
	composerObj['defaultFileExtensionRemoveEnabled'] = $('#defaultFileExtensionRemoveEnabled').val();
	composerObj['fileSplitEnabled'] = $('#fileSplitEnabled').val();
	composerObj['composerMapping.id'] = '0';

	$
			.ajax({
				url : 'createComposerPlugin',
				cache : false,
				async : true,
				dataType : 'json',
				type : "POST",
				data : {
					"id" : composerObj['id'],
					"myDistDrvPathlist.id" : composerObj['myDistDrvPathlist.id'],
					"name" : composerObj['name'],
					"composerType.id" : composerObj['composerType.id'],
					"destPath" : composerObj['destPath'],
					"fileBackupPath" : composerObj['fileBackupPath'],
					"composerMapping.id" : composerObj['composerMapping.id'],
					"writeFilenamePrefix" : composerObj['writeFilenamePrefix'],
					"writeFilenameSuffix" : composerObj['writeFilenameSuffix'],
					"fileExtension" : composerObj['fileExtension'],
					"defaultFileExtensionRemoveEnabled" : composerObj['defaultFileExtensionRemoveEnabled'],
					"fileSplitEnabled" : composerObj['fileSplitEnabled'],
					"pathListCount" : counter
				},

				success : function(data) {
					hideProgressBar(counter);

					var response = data;

					var responseCode = response.code;
					var responseMsg = response.msg;
					var responseObject = response.object;

					if (responseCode === "200") {
						resetWarningDisplay();
						clearAllMessages();
						showSuccessMsg(responseMsg);

						var rowData = {};
						rowData.id = parseInt(responseObject.id);
						var uniqueId = "checkbox";
						if(responseObject.name !== "") {
							uniqueId += "_" + responseObject.name; 
						}
						rowData.checkbox = '<input type="checkbox" id="'+uniqueId+'" name="'+childCheckboxName+'" value="'+rowData.id+'" onchange="updateRootCheckbox(this, \''+uniqueGridId+'\', \''+selectAllCheckboxId+'\', \''+childCheckboxName+'\')"/>';
						rowData.name = '<a id="plugin_link_txt" href="#" class="link" onclick="redirectComposerConfiguration('
								+ "'"
								+ rowData.id
								+ "','"
								+ responseObject.name
								+ "','"
								+ responseObject.pluginTypeAlias
								+ "'"
								+ ');" >' + responseObject.name + '</a>';
						rowData.composerTypeId = composerObj['composerType.id'];
						rowData.composerType = composerObj['composerType'];
						rowData.composerTypeAlias = responseObject.pluginTypeAlias;
						rowData.writeFilenamePrefix = responseObject.writeFilenamePrefix;
						rowData.writeFilenameSuffix = responseObject.writeFilenameSuffix;
						rowData.fileExtension = responseObject.fileExtension;
						rowData.defaultFileExtensionRemoveEnabled = responseObject.defaultFileExtensionRemoveEnabled;
						rowData.fileSplitEnabled = responseObject.fileSplitEnabled;
						rowData.destPath = responseObject.destPath;
						rowData.additionalParam = "<a href='#' class='link' id='view_additional_param_"+pathName +"_"+composerObj['name']+"_lnk' onclick=showDetailedParameters('"
								+ rowData.id
								+ "','"
								+ counter
								+ "'); ><i class='fa fa-list' aria-hidden='true' style='cursor:pointer;'></i></a>";
						rowData.characterRename = "<a href='#' class='link' id='char_rename_"+pathName +"_"+composerObj['name']+"_lnk' onclick=viewCharRenameOperationByPlugin('"
								+ rowData.id
								+ "'); ><i class='fa fa-list' aria-hidden='true' style='cursor:pointer;'></i></a>";
						rowData.fileBackupPath = responseObject.fileBackupPath;
						rowData.composerMappingId = responseObject.composerMappingId;

						var charObj = responseObject.characterRename;
						rowData.edit = "<a href='#' class='link'id='edit_plugin_"+pathName +"_"+composerObj['name']+"_lnk'  onclick=displayPluginEditPopup(\'EDIT\','"
								+ rowData.id
								+ "','"
								+ counter
								+ "','"
								+ charObj
								+ "'); ><i class='fa fa-pencil-square-o' aria-hidden='true'></i></a>";
						
						if(responseObject.pluginType === 'Default Composer'){
							rowData.name = '<a href="#" class="link" style="text-decoration:none;">'
								+ responseObject.name
								+ '</a>';;
							rowData.additionalParam = '';
							rowData.characterRename = '';
						}
						
						jQuery("#" + counter + "_grid").jqGrid('addRowData',
								rowData.id, rowData);
						closeFancyBox();
						$("#add_plugin_buttons_div").hide();
						$("#view_plugin_buttons_div").show();
						$("#view_progress-bar-div").hide();

					} else if (responseObject !== undefined
							&& responseObject !== 'undefined'
							&& responseCode === "400") {
						$("#add_plugin_buttons_div").show();
						$("#view_plugin_buttons_div").hide();
						$("#view_progress-bar-div").hide();
						addErrorIconAndMsgForAjaxPopUp(responseObject);
					} else {
						$("#add_plugin_buttons_div").show();
						$("#view_plugin_buttons_div").hide();
						$("#view_progress-bar-div").hide();

						resetWarningDisplay();
						clearAllMessages();
						showErrorMsgPopUp(responseMsg);
					}
				},
				error : function(xhr, st, err) {
					hideProgressBar(counter);
					handleGenericError(xhr, st, err);
				}
			});
}

/* Function will open edit plugin pop-up */
function displayPluginEditPopup(actionType, rowId, counter) {

	clearAllMessagesPopUp();
	resetWarningDisplayPopUp();

	$("#pathlist_id").val($("#" + counter + "_pathListId").val());
	$("#current_block_count").val(counter);

	var rowData = jQuery("#" + counter + "_grid").jqGrid('getRowData', rowId);

	$("#mappingId").val(rowData.composerMappingId);
	$("#add_plugin_buttons_div").hide();
	$("#update_plugin_buttons_div").show();

	$("#view_plugin_buttons_div").hide();
	$("#view_progress-bar-div").hide();
	$("#add_plugin_header_label").hide(); // Hide add header label
	$("#update_plugin_header_label").show(); // Show update header label

	// Setting all plugin details

	$("#composerPluginId").val(rowData.id);
	$('#name').val(
			$(jQuery("#" + counter + "_grid").jqGrid('getCell', rowId, 'name'))
					.closest("a").html());

	$('#destPath').val(rowData.destPath);
	$('#writeFilenameSuffix').val(rowData.writeFilenameSuffix);
	$('#writeFilenamePrefix').val(rowData.writeFilenamePrefix);
	$('#fileBackupPath').val(rowData.fileBackupPath);
	$('#fileExtension').val(rowData.fileExtension);
	$('#defaultFileExtensionRemoveEnabled').val(rowData.defaultFileExtensionRemoveEnabled);
	$('#fileSplitEnabled').val(rowData.fileSplitEnabled);
	$('#compressOutFileEnabled').val(rowData.compressOutFileEnabled);
	$('#composerPluginType').val(rowData.composerTypeId);
	$('#composerPluginType').attr('disabled', true);

	$("#add_composer_plugin_link").click();
}

/* Function will update composer plugin details */
function updateComposerPluginDetails() {
	$("#view_progress-bar-div").show();
	$("#add_plugin_buttons_div").hide();

	var counter = $('#current_block_count').val();
	var composerObj = {};

	composerObj['myDistDrvPathlist.id'] = $('#pathlist_id').val();
	composerObj['id'] = $('#composerPluginId').val();
	composerObj['name'] = $('#name').val();
	composerObj['composerType.id'] = $('#composerPluginType').val();
	composerObj['composerType'] = $('#composerPluginType option:selected')
			.text();
	composerObj['destPath'] = $('#destPath').val();
	composerObj['writeFilenamePrefix'] = $('#writeFilenamePrefix').val();
	composerObj['writeFilenameSuffix'] = $('#writeFilenameSuffix').val();
	composerObj['fileExtension'] = $('#fileExtension').val();
	composerObj['defaultFileExtensionRemoveEnabled'] = $('#defaultFileExtensionRemoveEnabled').val();
	composerObj['fileSplitEnabled'] = $('#fileSplitEnabled').val();
	composerObj['fileBackupPath'] = $('#fileBackupPath').val();
	composerObj['composerMapping.id'] = $("#mappingId").val();

	$
			.ajax({
				url : 'updateComposerPlugin',
				cache : false,
				async : true,
				dataType : 'json',
				type : "POST",
				data : {
					"id" : composerObj['id'],
					"myDistDrvPathlist.id" : composerObj['myDistDrvPathlist.id'],
					"name" : composerObj['name'],
					"composerType.id" : composerObj['composerType.id'],
					"destPath" : composerObj['destPath'],
					"fileBackupPath" : composerObj['fileBackupPath'],
					"composerMapping.id" : composerObj['composerMapping.id'],
					"writeFilenamePrefix" : composerObj['writeFilenamePrefix'],
					"writeFilenameSuffix" : composerObj['writeFilenameSuffix'],
					"fileExtension" : composerObj['fileExtension'],
					"defaultFileExtensionRemoveEnabled" : composerObj['defaultFileExtensionRemoveEnabled'],
					"fileSplitEnabled" : composerObj['fileSplitEnabled'],
					"pathListCount" : counter
				},

				success : function(data) {
					hideProgressBar(counter);

					var response = data;

					var responseCode = response.code;
					var responseMsg = response.msg;
					var responseObject = response.object;

					if (responseCode === "200") {
						resetWarningDisplay();
						clearAllMessages();
						showSuccessMsg(responseMsg);

						var rowData = {};
						rowData.id = parseInt(responseObject.id);
						rowData.name = '<a id="plugin_link_txt" href="#" class="link" onclick="redirectComposerConfiguration('
								+ "'"
								+ rowData.id
								+ "','"
								+ responseObject.name
								+ "','"
								+ responseObject.pluginTypeAlias
								+ "'"
								+ ');" >' + responseObject.name + '</a>';
						rowData.composerTypeId = composerObj['composerType.id'];
						;
						rowData.composerType = composerObj['composerType'];
						rowData.composerTypeAlias = responseObject.pluginTypeAlias;
						rowData.writeFilenamePrefix = responseObject.writeFilenamePrefix;
						rowData.writeFilenameSuffix = responseObject.writeFilenameSuffix;
						rowData.destPath = responseObject.destPath;
						rowData.fileExtension = responseObject.fileExtension;
						rowData.defaultFileExtensionRemoveEnabled = responseObject.defaultFileExtensionRemoveEnabled;
						rowData.fileSplitEnabled = responseObject.fileSplitEnabled;
						rowData.additionalParam = "<a href='#' class='link' onclick=showDetailedParameters('"
								+ rowData.id
								+ "','"
								+ counter
								+ "'); ><i class='fa fa-list' aria-hidden='true' style='cursor:pointer;'></i></a>";
						rowData.characterRename = "<a href='#' class='link' onclick=viewCharRenameOperationByPlugin('"
								+ rowData.id
								+ "'); ><i class='fa fa-list' aria-hidden='true' style='cursor:pointer;'></i></a>";
						rowData.fileBackupPath = responseObject.fileBackupPath;
						rowData.composerMappingId = responseObject.composerMappingId;
						rowData.edit = "<a href='#' class='link' onclick=displayPluginEditPopup(\'EDIT\','"
								+ rowData.id
								+ "','"
								+ counter
								+ "'); ><i class='fa fa-pencil-square-o' aria-hidden='true'></i></a>";

						if(responseObject.pluginType === 'Default Composer'){
							rowData.name = '<a href="#" class="link" style="text-decoration:none;">'
								+ responseObject.name
								+ '</a>';;
							rowData.additionalParam = '';
							rowData.characterRename = '';
						}
						$("#" + counter + "_grid").jqGrid('setRowData',
								rowData.id, rowData);
						closeFancyBox();
						$("#add_plugin_buttons_div").hide();
						$("#view_plugin_buttons_div").show();
						$("#view_progress-bar-div").hide();

					} else if (responseObject !== undefined
							&& responseObject !== 'undefined'
							&& responseCode === "400") {
						$("#update_plugin_buttons_div").show();
						$("#view_plugin_buttons_div").hide();
						$("#view_progress-bar-div").hide();
						addErrorIconAndMsgForAjaxPopUp(responseObject);
					} else {
						$("#update_plugin_buttons_div").show();
						$("#view_plugin_buttons_div").hide();
						$("#view_progress-bar-div").hide();

						resetWarningDisplay();
						clearAllMessages();
						showErrorMsgPopUp(responseMsg);
					}
				},
				error : function(xhr, st, err) {
					hideProgressBar(counter);
					handleGenericError(xhr, st, err);
				}
			});

}

/* Function will delete character rename block */
var charRenameBlockCounter, charRenameId;
function displayDeleteCharRenamePopup(counter) {
	charRenameId = $('#' + counter + '_char_rename_id').val();
	charRenameBlockCounter = counter;
	clearAllMessagesPopUp();
	resetWarningDisplay();

	if (charRenameId === null || charRenameId === 'null' || charRenameId === '') {
		$("#" + counter + "_char_form").remove();
	} else {
		$("#delete_char_rename_param_link").click();
	}
}

/* Function will delete char rename operation parameters. */
function deleteCharRenameOperationParams() {
	clearAllMessagesPopUp();
	resetWarningDisplayPopUp();

	$
			.ajax({
				url : 'deletePluginCharRenameParams',
				cache : false,
				async : true,
				dataType : 'json',
				type : "POST",
				data : {
					"id" : charRenameId
				},

				success : function(data) {
					var response = data;
					var responseCode = response.code;
					var responseMsg = response.msg;
					var responseObject = response.object;
					if (responseCode === "200") {
						$("#" + charRenameBlockCounter + "_char_form").remove();
						resetWarningDisplay();
						clearAllMessages();
						showSuccessMsg(responseMsg);
						closeFancyBox();
					} else if (responseObject !== undefined
							&& responseObject !== 'undefined'
							&& responseCode === "400") {
						showErrorMsg(responseMsg);
					} else {
						showErrorMsg(responseMsg);
					}
				},
				error : function(xhr, st, err) {
					handleGenericError(xhr, st, err);
				}
			});
}

/*
 * Function will add char rename operation to database with its required
 * validations.
 */
function addCharRenamOperationParams(counter) {

	clearAllMessagesPopUp();
	resetWarningDisplayPopUp();
	$('#' + counter + '_char_buttons-div').hide();
	$('#' + counter + '_char_progress-bar-div').show();
	$('#' + counter + '_char_rename_id').val('0');

	var padingLength = $('#' + counter + '_length').val();
	if (padingLength == '' || padingLength == null) {
		padingLength = -1;
		$('#' + counter + '_length').val(" ");
	}

	var startIndex = $('#' + counter + '_char_startIndex').val();
	if (startIndex == '' || startIndex == null) {
		startIndex = -1;
		$('#' + counter + '_char_startIndex').val(" ");
	}

	var endIndex = $('#' + counter + '_char_endIndex').val();
	if (endIndex == '' || endIndex == null) {
		endIndex = -1;
		$('#' + counter + '_char_endIndex').val(" ");
	}
	$
			.ajax({
				url : 'createPluginCharRenameParams',
				cache : false,
				async : true,
				dataType : 'json',
				type : "POST",
				data : {
					"id" : $('#' + counter + '_char_rename_id').val(),
					"sequenceNo" : $('#' + counter + '_sequenceNo').val(),
					"query" : $('#' + counter + '_query').val(),
					"position" : $('#' + counter + '_position').val(),
					"startIndex" : startIndex,
					"endIndex" : endIndex,
					"paddingType" : $('#' + counter + '_paddingType').val(),
					"paddingValue" : $('#' + counter + '_paddingValue').val(),
					"defaultValue" : $('#' + counter + '_defaultValue').val(),
					"length" : padingLength,
					"composer.id" : $("#composerPluginId").val(),
					"blockCount" : counter,
					"cacheEnable":$('#' + counter + '_cacheEnable').val()

				},

				success : function(data) {
					$('#' + counter + '_char_buttons-div').show();
					$('#' + counter + '_char_progress-bar-div').hide();

					var response = data;

					var responseCode = response.code;
					var responseMsg = response.msg;
					var responseObject = response.object;

					if (responseCode === "200") {
						$('#errorMessageCharacterRename').hide();
						clearAllMessagesPopUp();
						resetWarningDisplayPopUp();
						showSuccessMsgPopUp(responseMsg);

						$("#" + counter + "_char_updatebtn").show();
						$("#" + counter + "_char_savebtn").hide();

						$('#' + counter + '_char_rename_id').val(
								responseObject.id);
						$("#" + counter + "_char_operation_block").collapse(
								"toggle");

						$('#' + counter + '_sequenceNo').val(
								responseObject.sequenceNo);
						$('#' + counter + '_query').val(responseObject.query);
						$('#' + counter + '_position').val(
								responseObject.position.toString());
						$('#' + counter + '_defaultValue').val(
								responseObject.defaultValue);
						$('#' + counter + '_paddingType').val(
								responseObject.paddingType.toString());
						$('#' + counter + '_paddingValue').val(
								responseObject.paddingValue.toString());
						var charStartIndex = responseObject.startIndex;
						if(charStartIndex>=0){
							$('#' + counter + '_char_startIndex').val(charStartIndex);
						} else {
							$('#' + counter + '_char_startIndex').val('');
						}
						if(responseObject.endIndex==-1){
							$('#' + counter + '_char_endIndex').val('');
						} else {
							$('#' + counter + '_char_endIndex').val(responseObject.endIndex);
						}
						if(responseObject.length==-1){
							$('#' + counter + '_length').val('');
						}else{
							$('#' + counter + '_length').val(responseObject.length);
						}

					} else if (responseObject !== undefined
							&& responseObject !== 'undefined'
							&& responseCode === "400") {
						$('#' + counter + '_char_buttons-div').show();
						$('#' + counter + '_char_progress-bar-div').hide();
						addErrorIconAndMsgForAjax(responseObject);
						$('#errorMessageCharacterRename').show();
						$("#errorMessageCharacterRename").append("<span>Given input values may not be as per defined guideline. Get more detail on hover of error icon.</span>");
					} else {
						$('#' + counter + '_char_buttons-div').show();
						$('#' + counter + '_char_progress-bar-div').hide();
						clearAllMessagesPopUp();
						resetWarningDisplayPopUp();
						$('#errorMessageCharacterRename').hide();
						showErrorMsgPopUp(responseMsg);
					}
				},
				error : function(xhr, st, err) {
					$('#' + counter + '_char_buttons-div').show();
					$('#' + counter + '_char_progress-bar-div').hide();
					handleGenericError(xhr, st, err);
				}
			});
}

/*
 * Function will update character rename operation parameters for selected
 * plugin.
 */
function updateCharRenamOperationParams(counter) {
	resetWarningDisplay();
	clearAllMessages();
	$('#' + counter + '_char_buttons-div').hide();
	$('#' + counter + '_char_progress-bar-div').show();
	console.log("UPDATE CHAR RENAME OPERATION PARAM:::DIST:::");
	var padingLength = $('#' + counter + '_length').val();
	if (padingLength == '' || padingLength == null) {
		padingLength = -1;
		$('#' + counter + '_length').val("");
	}
	var fileNameLength = $('#' + counter + '_length').val();
	if (fileNameLength == '' || fileNameLength == null) {
		fileNameLength = -1;
		$('#' + counter + '_length').val("");
	}
	var startIndex = $('#' + counter + '_char_startIndex').val();
	if (startIndex == '' || startIndex == null || startIndex==-1) {
		startIndex = -1;
		$('#' + counter + '_char_startIndex').val("");
	} 

	var endIndex = $('#' + counter + '_char_endIndex').val();
	if (endIndex == '' || endIndex == null) {
		endIndex = -1;
		$('#' + counter + '_char_endIndex').val("");
	}

	$
			.ajax({
				url : 'updatePluginCharRenameParams',
				cache : false,
				async : true,
				dataType : 'json',
				type : "POST",
				data : {
					"id" : $('#' + counter + '_char_rename_id').val(),
					"sequenceNo" : $('#' + counter + '_sequenceNo').val(),
					"query" : $('#' + counter + '_query').val(),
					"position" : $('#' + counter + '_position').val(),
					"startIndex" : startIndex,
					"endIndex" : endIndex,
					"paddingType" : $('#' + counter + '_paddingType').val(),
					"paddingValue" : $('#' + counter + '_paddingValue').val(),
					"defaultValue" : $('#' + counter + '_defaultValue').val(),
					"length" : padingLength,
					"composer.id" : $("#composerPluginId").val(),
					"blockCount" : counter,
					"cacheEnable":$('#' + counter + '_cacheEnable').val()
				},

				success : function(data) {
					$('#' + counter + '_char_buttons-div').show();
					$('#' + counter + '_char_progress-bar-div').hide();

					var response = data;

					var responseCode = response.code;
					var responseMsg = response.msg;
					var responseObject = response.object;

					if (responseCode === "200") {
						resetWarningDisplay();
						clearAllMessages();
						showSuccessMsg(responseMsg);

						$("#" + counter + "_char_updatebtn").show();
						$("#" + counter + "_char_savebtn").hide();

						$("#" + counter + "_char_operation_block").collapse(
								"toggle");
						$('#' + counter + '_sequenceNo').val(
								responseObject.sequenceNo);
						$('#' + counter + '_query').val(responseObject.query);
						$('#' + counter + '_position').val(
								responseObject.position + "");
						$('#' + counter + '_length').val(responseObject.length);
						$('#' + counter + '_defaultValue').val(
								responseObject.defaultValue);
						$('#' + counter + '_paddingType').val(
								responseObject.paddingType + "");
						$('#' + counter + '_paddingValue').val(
								responseObject.paddingValue + "");
						$('#' + counter + '_cacheEnable').val(
								responseObject.isCacheEnable + "");
						
						var charStartIndex = responseObject.startIndex;
						if(charStartIndex>=0){
							$('#' + counter + '_char_startIndex').val(charStartIndex);
						} else {
							$('#' + counter + '_char_startIndex').val('');
						}
						
						if(responseObject.endIndex==-1){
							$('#' + counter + '_char_endIndex').val('');
						} else {
							$('#' + counter + '_char_endIndex').val(responseObject.endIndex);
						}
						if(responseObject.length==-1){
							$('#' + counter + '_length').val('');
						}else{
							$('#' + counter + '_length').val(responseObject.length);
						}

					} else if (responseObject !== undefined
							&& responseObject !== 'undefined'
							&& responseCode === "400") {
						$('#' + counter + '_char_buttons-div').show();
						$('#' + counter + '_char_progress-bar-div').hide();
						addErrorIconAndMsgForAjax(responseObject);
					} else {
						$('#' + counter + '_char_buttons-div').show();
						$('#' + counter + '_char_progress-bar-div').hide();
						resetWarningDisplay();
						clearAllMessages();
						showErrorMsg(responseMsg);
					}
				},
				error : function(xhr, st, err) {
					$('#' + counter + '_char_buttons-div').show();
					$('#' + counter + '_char_progress-bar-div').hide();
					handleGenericError(xhr, st, err);
				}
			});

}
/* Function will get char rename parameters for selected plugin id. */
function getCharRenameParams(pluginId) {
	$.ajax({
		url : 'getCharRenameOperationById',
		cache : false,
		async : true,
		dataType : 'json',
		type : 'POST',
		data : {
			"id" : pluginId,
		},
		success : function(data) {
			var response = data;
			var responseCode = response.code;
			var responseObject = response.object;

			$("#charRename_div").html('');

			if (responseCode === "200") {
				if (responseObject !== 'undefined' && responseObject !== null) {
					$.each(responseObject, function(index, char) {
						addCharRenameOperation(char.id, char.sequenceNo,
								char.query, char.position, char.startIndex,
								char.endIndex, char.paddingType,char.paddingValue,
								char.defaultValue, char.length, char.isCacheEnable,'UPDATE');
					});
				}
			} else {
				console.log("No char rename operation parameters are found.");
			}
		},
		error : function(xhr, st, err) {
			handleGenericError(xhr, st, err);
		}

	});
}

function redirectComposerConfiguration(plugInId, plugInName, plugInType) {

	$('#plugInId').val(plugInId);
	$('#plugInName').val(plugInName);
	$('#plugInType').val(plugInType);
	$('#distribution-service-composer-config').submit();
}

/* Function will display delete composer plugin details popup */
function displayDeletePluginPopup(counter) {
	clearAllMessagesPopUp();
	resetWarningDisplay();

	// var myGrid = $("#" + counter + "_grid");
	// var selectedRowId = myGrid.jqGrid('getGridParam', 'selarrrow');
	var childCheckboxName = counter+"_pluginCheckbox"
	var selectedPluginCheckboxes = [];
    $.each($("input[name='"+childCheckboxName+"']:checked"), function(){            
    	selectedPluginCheckboxes.push($(this).val());
    });
    var selectedRowId = "";
    if(selectedPluginCheckboxes.length > 0) {
    	selectedRowId = selectedPluginCheckboxes[0];
    }

	$("#warning_msg").hide();
	$("#delete_plugin_note").hide();
	$("#btndelete").hide();
	$("#delete_buttons-div").show();

	if (selectedRowId != null && selectedRowId != '') {

		$("#delete_plugin_note").show();
		$("#btndelete").show();
		$('#delete_buttons-div #btndelete').attr('onClick',
				'deleteComposerPlugin("' + counter + '")');

		$('#deleteBlockId').val(counter);
	} else {
		$("#warning_msg").show();
	}
	$('#delete_plugin_link').click();

}

/* Function will delete composer plugin details */
function deleteComposerPlugin() {
	var blockId = $('#deleteBlockId').val();

	// var myGrid = $("#" + blockId + "_grid");
	// var selectedRowId = myGrid.jqGrid('getGridParam', 'selarrrow');
	// var rowString = selectedRowId.join(",");
	// var rowIds;
	// rowIds = rowString.split(",");
	
	var childCheckboxName = blockId+"_pluginCheckbox";
	var selectedPluginCheckboxes = [];
    $.each($("input[name='"+childCheckboxName+"']:checked"), function(){            
    	selectedPluginCheckboxes.push($(this).val());
    });
    var rowString = selectedPluginCheckboxes.join(",");
    
    var rowIds = [];
    rowIds = rowString.split(",");

	$
			.ajax({
				url : 'deleteComposerPlugin',
				cache : false,
				async : true,
				dataType : 'json',
				type : "POST",
				data : {
					"composerIdList" : rowString
				},

				success : function(data) {

					var response = data;

					var responseCode = response.code;
					var responseMsg = response.msg;
					var responseObject = response.object;

					if (responseCode === "200") {
						resetWarningDisplay();
						clearAllMessages();
						showSuccessMsg(responseMsg);

						for (var i = 0; i < rowIds.length; i++) {
							var rowid = rowIds[i];
							$("#" + blockId + "_grid").jqGrid('delRowData',
									rowid);

						}

						closeFancyBox();
					} else if (responseObject !== undefined
							&& responseObject !== 'undefined'
							&& responseCode === "400") {
						addErrorIconAndMsgForAjaxPopUp(responseObject);
					} else {
						resetWarningDisplay();
						clearAllMessages();
						showErrorMsgPopUp(responseMsg);
					}
				},
				error : function(xhr, st, err) {
					handleGenericError(xhr, st, err);
				}
			});
}

/* Function will display detailed parameter list in the grid. */
function showDetailedParameters(rowId, counter) {
	clearAllMessagesPopUp();
	resetWarningDisplay();

	var rowData = jQuery("#" + counter + "_grid").jqGrid('getRowData', rowId);

	$('#view_fileExtension').val(rowData.fileExtension);
	$('#view_writeFilenamePrefix').val(rowData.writeFilenamePrefix);
	$('#view_writeFilenameSuffix').val(rowData.writeFilenameSuffix);
	$('#view_destPath').val(rowData.destPath);
	$('#view_fileBackupPath').val(rowData.fileBackupPath);
	$('#view_fileExtension').val(rowData.fileExtension);
	$('#view_defaultFileExtensionRemoveEnabled').val(rowData.defaultFileExtensionRemoveEnabled);
	$('#view_fileSplitEnabled').val(rowData.fileSplitEnabled);
	$('#additional_param_link').click();

}

/* Function will display character rename operation parameters details. */
function viewCharRenameOperationByPlugin(pluginId) {
	$("#composerPluginId").val(pluginId);

	$.ajax({
		url : 'getCharRenameOperationById',
		cache : false,
		async : true,
		dataType : 'json',
		type : 'POST',
		data : {
			"id" : pluginId,
		},
		success : function(data) {
			var response = data;
			var responseCode = response.code;
			var responseObject = response.object;

			$('#plugin_char_rename_details').html('');

			if (responseCode === "200") {

				if (responseObject !== 'undefined' && responseObject !== null) {
					$.each(responseObject, function(index, responseObject) {
						addCharRenameOperation(responseObject["id"],
								responseObject["sequenceNo"],
								responseObject["query"],
								responseObject["position"],
								responseObject["startIndex"],
								responseObject["endIndex"],
								responseObject["paddingType"],
								responseObject["paddingValue"],
								responseObject["defaultValue"],
								responseObject["length"],responseObject["isCacheEnable"],'UPDATE');
					});
				}
			}

			$("#char_rename_add_link").show();
			$("#char_rename_param_link").click();
		},
		error : function(xhr, st, err) {
			handleGenericError(xhr, st, err);
		}
	});
}

/* Function will reset all form parameters */
function resetCharRenameParameters(counter) {
	$("#" + counter + "_query").val('');
	$("#" + counter + "_char_startIndex").val('');
	$("#" + counter + "_char_endIndex").val('');
	$("#" + counter + "_length").val('');
	$("#" + counter + "_sequenceNo").val('');
	$("#" + counter + "_defaultValue").val('');
	$("#" + counter + "_position").val($("#" + counter + "_position option:first").val());
	$("#" + counter + "_paddingType").val($("#" + counter + "_paddingType option:first").val());
	$("#" + counter + "_paddingValue").val('');
}

function updateFormatter(cellvalue, options, rowObject){
	return "<a href='#' class='link' onclick=displayAddEditPopup(\'EDIT\','"+rowObject["id"]+"'); ><i class='fa fa-pencil-square-o' aria-hidden='true'></i></a>";
}

function displayAddEditPopup(actionType,attributeId){
	
	clearAllMessagesPopUp();
	resetWarningDisplayPopUp();
	resetDriverAttributeParams();
	 
	if(actionType === 'ADD'){
		$("#addNewAttribute").show();
		$("#editAttribute").hide();
		$("#viewClose").hide();
		$("#closebtn").show();
		$("#add_label").show();
		$("#update_label").hide();
		$("#view_label").hide();
		$("#id").val(0);
		
		$("#add_edit_attribute").click();
	}else if(actionType === 'EDIT'){
		if(attributeId > 0){
			
			var responseObject = jQuery("#attributeList").jqGrid ('getRowData', attributeId);
			
			$("#editAttribute").show();
			$("#addNewAttribute").hide();
			$("#viewClose").hide();
			$("#closebtn").show();
			$("#update_label").show();
			$("#add_label").hide();
			$("#view_label").hide();
			$("#id").val(responseObject.id);
			$("#databaseFieldName").val(responseObject.databaseFieldName);
			if(responseObject.unifiedFieldName != undefined && responseObject.unifiedFieldName != null && responseObject.unifiedFieldName == ' '){
				responseObject.unifiedFieldName = '';
			}
			$("#unifiedFieldName").val(responseObject.unifiedFieldName);
			$("#dataType").val(responseObject.dataType);
			$("#defualtValue").val(responseObject.defualtValue);
			var element = document.getElementById('paddingEnable');	
			element.value = responseObject.paddingEnable;	
			$("#length").val(responseObject.length);	
			$("#paddingType").val(responseObject.paddingType);	
			$("#paddingChar").val(responseObject.paddingChar);	
			$("#prefix").val(responseObject.prefix);	
			$("#suffix").val(responseObject.suffix);	
			changePaddingParamter();
			$("#add_edit_attribute").click();
		}else{
			showErrorMsg(jsSpringMsg.failUpdateAtributeMsg);
		}
	}else{
		
		$("#editAttribute").hide();
		$("#addNewAttribute").hide();
		$("#update_label").hide();
		$("#add_label").hide();
		$("#view_label").show();
		$("#viewClose").show();
		$("#closebtn").hide();
		
		if(attributeId > 0){
			
			var	responseObject = jQuery("#attributeList").jqGrid ('getRowData', attributeId);
			
			$("#id").val(responseObject.id);
			$("#databaseFieldName").val(responseObject.databaseFieldName);
			$("#unifiedFieldName").val(responseObject.unifiedFieldName);
			$("#dataType").val(responseObject.dataType);
			$("#defualtValue").val(responseObject.defualtValue);
			
			$("#add_edit_attribute").click();
			
		}
	}
}

/* Function will display delete attribute popup */
function displayDeleteAttributePopup(){

	clearAllMessagesPopUp();
	resetWarningDisplayPopUp();
	clearAllMessages();
	
	if(ckIntanceSelected.length === 0){
		$("#lessWarn").show();
		$("#moreWarn").hide();
		$("#delete_warn_msg_link").click();
		return;
	}else{
		
		var tableString ="<table class='table table-hover table-bordered'  border='1' >";
		tableString+="<th>#</th><th>"+jsSpringMsg.databaseFieldName+"</th><th>"+jsSpringMsg.unifiedFieldName+"</th><th>"+jsSpringMsg.dataType+"</th>";
	
		for(var i = 0 ; i< ckIntanceSelected.length;i++){
			
			var	rowData = $('#attributeList').jqGrid('getRowData', ckIntanceSelected[i]);
			
			tableString += "<tr>";
			tableString += "<td><input type='checkbox' name='attribute_delete' id='attribute_"+ckIntanceSelected[i]+"' checked  onclick=getSelecteAttributeList('"+ckIntanceSelected[i]+"')  value='"+ckIntanceSelected[i]+"' /> </td>";
			tableString += "<td>"+rowData.databaseFieldName+" </td>";
			tableString += "<td>"+rowData.unifiedFieldName+"</td>";
			tableString += "<td>"+rowData.dataType+"</td>";
			tableString += "</tr>";
		}	
		tableString+="</table>";
		$("#delete_selected_attribute_details").html(tableString);
		$("#delete_attribute_bts_div").show();
		$("#delete_attribute_progress_bar_div").hide();
		$("#delete_close_attribute_buttons_div").hide();
		$("#deleteAttributeId").val(ckIntanceSelected.toString());
		$("#delete_attribute").click();
	}
}

/* Function will delete attribute */
function deleteAttribute(){
	$("#delete_attribute_bts_div").hide();
	$("#delete_attribute_progress_bar_div").show();
	$("#delete_close_attribute_buttons_div").hide();
	
	$.ajax({
		url: 'deleteDatabaseDriverAttribute',
		cache: false,
		async: true,
		dataType:'json',
		type:'POST',
		data:
		 {
			"attributeId" : $("#deleteAttributeId").val()
		 }, 
		success: function(data){
			var response = eval(data);
			var responseCode = response.code; 
			var responseMsg = response.msg; 
			 
			clearAllMessagesPopUp();
			resetWarningDisplayPopUp();
			
			if(responseCode === "200"){
				
				showSuccessMsgPopUp(responseMsg);
				
				$("#delete_attribute_bts_div").hide();
				$("#delete_attribute_progress_bar_div").hide();
				$("#delete_close_attribute_buttons_div").show();
				ckIntanceSelected = new Array();
			}else{
				showErrorMsgPopUp(responseMsg);
				$("#delete_attribute_bts_div").show();
				$("#delete_attribute_progress_bar_div").hide();
				$("#delete_close_attribute_buttons_div").hide();
			}
		},
	    error: function (xhr,st,err){
			handleGenericError(xhr,st,err);
		}
	});
}

/* Function will reload attribute grid after add/edit/delete action. */
function reloadAttributeGridData(){
	
		clearAllMessages();
		var $grid = $("#attributeList");
		var currentPageOnGrid = $grid.jqGrid('getGridParam','page');
		jQuery('#attributeList').jqGrid('clearGridData');
		clearAttributeGrid();	
		$grid.jqGrid('setGridParam',{page: currentPageOnGrid }).trigger('reloadGrid');
		getDriverAttributeListByDriverId(urlAction, selDriverId);
}


/* Function will add/edit driver attribute details */
function createUpdateDriverAttribute(urlAction, attributeId, actionType){

	var paddingLength;	
	if($("#length").val() == ''){	
		paddingLength='0';	
	}else{	
		paddingLength=$("#length").val();	
	}
		
	$.ajax({
		url: urlAction,
		cache: false,
		async: true,
		dataType:'json',
		type:'POST',
		data:
		 {
			"id" 					: $("#id").val(),
			"databaseFieldName"   	: $("#databaseFieldName").val(),
			"unifiedFieldName"  	: $("#unifiedFieldName").val() ,
			"dataType"   			: $("#dataType").val(),
			"defualtValue" 	 		: $("#defualtValue").val(),
			"actionType" 			: actionType,
			"dbDisDriver.id"		: $("#driverId").val(),
			"paddingEnable" 		: $("#paddingEnable").val(),	
			"length" 				: paddingLength,	
			"paddingType" 			: $("#paddingType").val(),	
			"paddingChar" 			: $("#paddingChar").val(),	
			"prefix" 				: $("#prefix").val(),	
			"suffix" 				: $("#suffix").val(),

		}, 
		success: function(data){
			var response = eval(data);
			var responseCode = response.code; 
			var responseMsg = response.msg; 
			var responseObject = response.object;
		
			clearAllMessagesPopUp();
			resetWarningDisplayPopUp();
			 
			if(responseCode === "200"){
				
				$("#editAttribute").hide();
				$("#addNewAttribute").hide();
				reloadAttributeGridData();
				closeFancyBox();
				showSuccessMsg(responseMsg);
			}else if(responseObject !== undefined && responseObject !== 'undefined' && responseCode === "400"){
				showErrorMsgPopUp(responseMsg);
				addErrorIconAndMsgForAjaxPopUp(responseObject);
			}else{
				showErrorMsgPopUp(responseMsg);
			}
		},
	    error: function (xhr,st,err){
			handleGenericError(xhr,st,err);
		}
	});
}

/* Function will reset all driver attribute params */
function resetDriverAttributeParams(){
	
	$("#databaseFieldName").val('');
	$("#unifiedFieldName").val('');
	$("#dataType").val('');
	$("#defualtValue").val('');
	$("#length").val('');	
	$("#paddingChar").val('');	
	$("#prefix").val('');	
	$("#suffix").val('');
}

function clearAttributeGrid(){
	
	var	$grid = $("#attributeList");
	var rowIds = $grid.jqGrid('getDataIDs');
	// iterate through the rows and delete each of them
	for(var i=0,len=rowIds.length;i<len;i++){
		var currRow = rowIds[i];
		$grid.jqGrid('delRowData', currRow);
	}
}

function getSelecteAttributeList(elementId){
	
	if( document.getElementById("attribute_"+elementId).checked === true){
		if(ckIntanceSelected.indexOf(elementId) === -1){
			ckIntanceSelected.push(elementId);
		}
	}else{
		if(ckIntanceSelected.indexOf(elementId) !== -1){
			ckIntanceSelected.splice(ckIntanceSelected.indexOf(elementId), 1);
		}
	}
	$("#deleteAttributeId").val(ckIntanceSelected.toString());
	
}

