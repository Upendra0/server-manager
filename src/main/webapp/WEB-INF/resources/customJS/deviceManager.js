var selectedDeviceAndMappingId = new Array();
var rowDataToShowInMappingTab = new Array();
var rowDataToShowInDeviceTab = new Array();
var deleteMappingIdArray=new Array();
var deleteDeviceIdArray=new Array();
var selectedDecodeType = '';
/* Function will reload grid data based on search parameters */
function reloadGridData() {

	clearAllMessages();
	clearResponseMsgDiv();
	clearResponseMsg();
	clearErrorMsg();
	clearAllMessagesPopUp();
	selectedDecodeType = '';
	associatedDeviceList = [];
	var $grid = $("#deviceAndMappingList");
	jQuery('#deviceAndMappingList').jqGrid('clearGridData');
	clearInstanceGrid("deviceAndMappingList");
	

	$grid.jqGrid('setGridParam', {
		datatype : 'json',
		page : 1,
		sortname : 'id',
		sortorder : "desc",
		postData : {
			'deviceTypeName' : function() {
				return $("#search_device_type").val();
				;

			},
			'vendorTypeName' : function() {
				return $("#search_vendor_type").val();
			},
			'deviceName' : function() {
				return $("#search_device_name").val();
			},
			'decodeType' : function() {
				var id = $("#search_decode_type").val();
				if (id == 'undefined' || id == '' || id == '-1') {
					id = 0;
				}
				return id;
			},
			'mappingName' : function() {
				return $("#search_mapping_name").val();
			}
		}
	}).trigger('reloadGrid');
}


var oldGridForMapping = '';
function reloadPopUpMappingData(){
	jQuery('#mappingList').jqGrid('clearGridData');
	clearInstanceGrid("mappingList");
	
	//create Mapping Grid
	$("#mappingList").jqGrid({
			data: rowDataToShowInMappingTab,
			datatype : "local",
			colNames : [ "#","",jsSpringMsg.mappingName, "", jsSpringMsg.deviceId, jsSpringMsg.deviceName, jsSpringMsg.deviceType, jsSpringMsg.vendorName],
			colModel : [ {name : '', index : '',formatter : mappingCheckBoxFormatter, sortable : false, width: '8%'},
			             {name : 'id',index : 'id',hidden : true,sortable : false},
			             {name : 'mappingName',index : 'mappingName',sortable : false,width : '60%'},
			             {name : 'mappingType',index : 'mappingType',hidden : true},
			             {name : 'deviceId',index : 'deviceId',hidden : true,sortable : false},
			             {name : 'deviceName',index : 'deviceName',sortable : true,width : '60%'},
			             {name : 'deviceType',index : 'deviceType',sortable : true,width : '40%'},
			             {name : 'vendorType',index : 'vendorType',sortable : true,width : '40%'}],
			rowNum : 10,
			rowList : [ 10, 20, 60, 100 ],
			height : 'auto',
			pager : "#mappingListPagingDiv",
			contentType : "application/json; charset=utf-8",
			viewrecords : true,
			multiselect : false,
			repeatitems: false,
			timeout : 120000,
			loadtext : "Loading...",
			recordtext : jsSpringMsg.recordText,
			emptyrecords : jsSpringMsg.emptyRecord,
			loadtext : jsSpringMsg.loadingText,
			pgtext : jsSpringMsg.pagerText,
		}).navGrid("#mappingListPagingDiv", {
		edit : false,
		add : false,
		del : false,
		search : false
	});
	
	jQuery("#mappingList").jqGrid('setGridParam',{datatype: 'local',data:rowDataToShowInMappingTab }).trigger("reloadGrid");
	reloadPopUpDeviceData();
}

var oldGridForMapping = '';
function reloadPopUpDeviceData(){
	//Device : device id, Device name, Device Type, Vendor Type
	//create Device Grid
	//console.log("enter reloadDeviceData.. rowDataToShowInDeviceTab" + JSON.stringify(rowDataToShowInDeviceTab));
	
	jQuery('#deviceList').jqGrid('clearGridData');
	clearInstanceGrid("deviceList");
	
	$("#deviceList").jqGrid({
			data: rowDataToShowInDeviceTab,
			datatype : "local",
			colNames : [ "#", jsSpringMsg.deviceId, jsSpringMsg.deviceName, jsSpringMsg.deviceType, jsSpringMsg.vendorName],
			colModel : [ {name : '', index : '',formatter : deviceCheckBoxFormatter,sortable : false,  width: '8%'},
			             {name : 'id',index : 'id',hidden : true,sortable : false},
			             {name : 'deviceName',index : 'deviceName',sortable : true,width : '60%'},
			             {name : 'deviceType',index : 'deviceType',sortable : true,width : '60%'},
			             {name : 'vendorType',index : 'vendorType',sortable : true,width : '40%'}],
			rowNum : 10,
			rowList : [ 10, 20, 60, 100 ],
			height : 'auto',
			pager : "#deviceListPagingDiv",
			contentType : "application/json; charset=utf-8",
			viewrecords : true,
			multiselect : false,
			timeout : 120000,
			loadtext : "Loading...",
			loadError : function(xhr, st, err) {
				handleGenericError(xhr, st, err);
			},
			recordtext : jsSpringMsg.recordText,
			emptyrecords : jsSpringMsg.emptyRecord,
			loadtext : jsSpringMsg.loadingText,
			pgtext : jsSpringMsg.pagerText,
		}).navGrid("#deviceListPagingDiv", {
		edit : false,
		add : false,
		del : false,
		search : false
	});
	
	jQuery("#deviceList").jqGrid('setGridParam',{datatype: 'local',data:rowDataToShowInDeviceTab}).trigger("reloadGrid");
}
var uniqueDeviceIdArray = new Array();
function createMappingAndDeviceData(selectedDeviceAndMappingId){
	//console.log("enter createdata.. uniqueDeviceIdArray: " + uniqueDeviceIdArray);
	for(var i=0;len=selectedDeviceAndMappingId.length,i<len ;i++) {
		//Mapping: Mapping id, mapping name, mapping Type,  Device Id, Device name, Device Type, Vendor Type
		//Device : device id, Device name, Device Type, Vendor Name
		var rowData = $("#deviceAndMappingList").jqGrid('getRowData', selectedDeviceAndMappingId[i]);
		//check for mappings to exist in rowdata then only showing that data in Mapping Tab (not NO-MAPPING)
		if(rowData.mappingId && rowData.mappingId !== '') {
			//console.log(i +"  rowData.mappingId : " + rowData.mappingId);
			//alert("rowData.mappingId: " +rowData.mappingId + " rowData.mappingName::" + rowData.mappingName);
			var mappingRow = {"id": rowData.mappingId, "mappingName": rowData.mappingName, "mappingType":rowData.mappingType, "deviceId":rowData.deviceId, "deviceName":rowData.deviceName, "deviceType":rowData.deviceType, "vendorType":rowData.vendorType };
			//console.log("mappingRow: " + JSON.stringify(mappingRow));
			rowDataToShowInMappingTab.push(mappingRow);
		}
		
		//console.log("string 1:"+JSON.stringify(rowDataToShowInMappingTab));
		//check for unassociated Devices and filter Devices for Device Tab
		
		//console.log("associatedDeviceList:: " + associatedDeviceList + " and rowData.deviceId:: " + rowData.deviceId);
		if(associatedDeviceList.indexOf(parseInt(rowData.deviceId)) === -1 && parseInt(rowData.deviceId) > 4) {
			if(uniqueDeviceIdArray.indexOf(parseInt(rowData.deviceId)) === -1){
				uniqueDeviceIdArray.push(parseInt(rowData.deviceId));
				var deviceRow = {"id":rowData.deviceId, "deviceName":rowData.deviceName, "deviceType":rowData.deviceType, "vendorType":rowData.vendorType};
				rowDataToShowInDeviceTab.push(deviceRow);
			}
		}
	}
}


/* Function will clear current grid data */
function clearInstanceGrid(gridID) {
	var $grid = $("#"+gridID);
	var rowIds = $grid.jqGrid('getDataIDs');
	// iterate through the rows and delete each of them
	for (var i = 0, len = rowIds.length; i < len; i++) {
		var currRow = rowIds[i];
		$grid.jqGrid('delRowData', currRow);
	}
}
var oldGrid = '';
/* Function will get all device list by search parameter and render it to grid. */
function getDeviceListBySearchParams(urlAction, rowNum) {
	$("#deviceAndMappingList").jqGrid(
			{
				url : urlAction,
				postData : {
					'deviceTypeName' : function() {
						return $("#search_device_type").val();
					},
					'vendorTypeName' : function() {
						return $("#search_vendor_type").val();
					},
					'deviceName' : function() {
						return $("#search_device_name").val();
					},
					'decodeType' : function() {
						var id = $("#search_decode_type").val();
						if (id === 'undefined' || id === '' || id === '-1') {
							id = 0;
						}
						return id;
					},
					'mappingName' : function() {
						return $("#search_mapping_name").val();
					}
				},
				datatype : "json",
				colNames : [ "#",jsSpringMsg.deviceId, jsSpringMsg.mappingId,jsSpringMsg.deviceId,
						jsSpringMsg.deviceType, jsSpringMsg.vendorName,
						jsSpringMsg.deviceName, jsSpringMsg.decodeType,
						jsSpringMsg.mappingName, jsSpringMsg.mappingPluginType,
						jsSpringMsg.associated, "", "" ],
				colModel : [ {
					name : '',
					index : '',
					formatter : checkBoxFormatter,
					sortable : false,
					width : '5%'
				},{
					name : 'id',
					index : 'id',
					hidden : true
				}, {
					name : 'mappingId',
					index : 'mappingId',
					hidden : true,
					sortable : false
				}, {
					name : 'deviceId',
					index : 'deviceId',
					hidden : true,
					sortable : false
				},{
					name : 'deviceType',
					index : 'deviceType',
					sortable : true,
					width : '13%'
				}, {
					name : 'vendorType',
					index : 'vendorType',
					sortable : true,
					width : '13%'
				}, {
					name : 'deviceName',
					index : 'deviceName',
					sortable : true,
					width : '13%',
					formatter : deviceNameFormatter
				}, {
					name : 'decodeType',
					index : 'decodeType',
					sortable : false,
					width : '7%'
				}, {
					name : 'mappingName',
					index : 'mappingName',
					sortable : false,
					width : '23%',
					formatter : mappingNameFormatter
				}, {
					name : 'pluginType',
					index : 'pluginType',
					sortable : false,
					width : '21%'
				}, {
					name : 'isAssociated',
					index : 'isAssociated',
					sortable : false,
					align : 'center',
					formatter : associationFormatter,
					width : '5%'
				}, {
					name : 'isPreConfigured',
					index : 'isPreConfigured',
					hidden : true
				}, {
					name : 'mappingType',
					index : 'mappingType',
					hidden : true
				},

				],
				rowNum : rowNum,
				rowList : [ 10, 20, 60, 100 ],
				height : 'auto',
				mtype : 'POST',
				sortname : 'device.id',
				sortorder : "desc",
				pager : "#deviceAndMappingListPagingDiv",
				contentType : "application/json; charset=utf-8",
				viewrecords : true,
				multiselect : false,
				timeout : 120000,
				loadtext : "Loading...",
				caption : jsSpringMsg.gridCaption,
				beforeRequest : function() {
					if(oldGrid != ''){
	            		$('#deviceAndMappingList tbody').html(oldGrid);
	            	}
					$(".ui-dialog-titlebar").hide();
				},
				beforeSend : function(xhr) {
					xhr.setRequestHeader("Accept", "application/json");
					xhr.setRequestHeader("Content-Type", "application/json");
				},
				loadComplete : function(data) {
					$(".ui-dialog-titlebar").show();
					if ($('#deviceAndMappingList').getGridParam('records') === 0) {
						oldGrid = $('#deviceAndMappingList tbody').html();
						$('#deviceAndMappingList tbody').html(
								"<div style='padding:6px;background:#D8D8D8;text-align:center;'>"
										+ jsSpringMsg.emptyRecord + "</div>");
						$("#deviceAndMappingListPagingDiv").hide();
					} else {
						$("#deviceAndMappingListPagingDiv").show();
						ckIntanceSelected = new Array();
					}
				},
				onPaging : function(pgButton) {
					clearResponseMsgDiv();
					clearInstanceGrid("deviceAndMappingList");
				},
				loadError : function(xhr, st, err) {
					handleGenericError(xhr, st, err);
				},
				beforeSelectRow : function(rowid, e) {
					return false;
				},
				onSelectAll : function(id, status) {
					if (status == true) {
						ckIntanceSelected = new Array();
						for (i = 0; i < id.length; i++) {
							ckIntanceSelected.push(id[i]);
						}
					} else {
						ckIntanceSelected = new Array();
					}

				},
				recordtext : jsSpringMsg.recordText,
				emptyrecords : jsSpringMsg.emptyRecord,
				loadtext : jsSpringMsg.loadingText,
				pgtext : jsSpringMsg.pagerText,
			}).navGrid("#deviceAndMappingListPagingDiv", {
		edit : false,
		add : false,
		del : false,
		search : false
	});
	$(".ui-jqgrid-titlebar").hide();
}
var count = 0;
function checkBoxFormatter(cellvalue, options, rowObject) {
	count++;
	var deviceUpdatedName = rowObject["deviceName"] + "_" + rowObject["mappingName"];
	//deviceUpdatedName = deviceUpdatedName.replace(/ /g, "_");
	//list all associated DeviceIds for future use
	listAssociatedDevices(rowObject);
	selectedDecodeType = rowObject["decodeType"];
	//console.log("selectedDecodeType:: " + selectedDecodeType);
	if (rowObject["isAssociated"] === true
			|| String(rowObject["mappingType"]) === '0') {
		return "<input type='checkbox' name='deviceAndMappingCk' disabled='disabled' id='device_"
				+ deviceUpdatedName + "_chk"
				+ "' value='"+rowObject["id"]+"'  onclick='addRowId(\'device_" + deviceUpdatedName + "_chk"
				+ "\',\'" + rowObject["id"] + "\');\'; />";
	} else {
		return "<input type='checkbox' name='deviceAndMappingCk' id='device_" + deviceUpdatedName + "_chk"
				+ "' value='"+rowObject["id"]+"' onclick=\"addRowId(\'device_" + deviceUpdatedName + "_chk"
				+ "\', \'" + rowObject["id"] + "\', \'"
				+ rowObject["mappingId"] + "\');\"; />";

	}
}

var associatedDeviceList = new Array();
function listAssociatedDevices(rowObject){
	if (rowObject["isAssociated"] === true || String(rowObject["mappingType"]) === '0') {
		if (associatedDeviceList.indexOf(rowObject["deviceId"]) === -1) {
			associatedDeviceList.push(rowObject["deviceId"]);
		}else if(associatedDeviceList.indexOf(rowObject) !== -1){
			associatedDeviceList.splice(associatedDeviceList.indexOf(rowObject["deviceId"]), 1);
		}	
	}
}

function mappingCheckBoxFormatter(cellvalue, options, rowObject){
	var mappingName = $(rowObject["mappingName"]).attr('value');
	var checkboxId = 'mapping_'+mappingName+ '_chk'; 
	return "<input type='checkbox' name='mappingCk' checked id='"+checkboxId+"' value='"+rowObject["id"]+"'/>"
	
}

function deviceCheckBoxFormatter(cellvalue, options, rowObject){
	var deviceName = $(rowObject["deviceName"]).attr('value');
	var checkboxId = 'device_'+deviceName+ '_chk'; 
	return "<input type='checkbox' name='deviceCk' checked id='"+checkboxId+"' value='"+rowObject["id"]+"'/>"
	
}

function getSelectedMappingChkVal(urlAction){
	$.each($("input[name='mappingCk']:checked"), function(){            
		deleteMappingIdArray.push($(this).val());
	   }); 
	if(deleteMappingIdArray.length > 0){
		deleteMappings(deleteMappingIdArray,urlAction);
	}else{
		console.log("deleteMappingIdArray empty ..!");
		showErrorMsgPopUp(jsSpringMsg.mappingMessage);
	}
	console.log('selected mappigns: ' + deleteMappingIdArray);
}

function getSelectedDeviceChkVal(urlAction){
	$.each($("input[name='deviceCk']:checked"), function(){            
		deleteDeviceIdArray.push($(this).val());
	   }); 
	if(deleteDeviceIdArray.length > 0){
		deleteDevicesAndMappings(deleteDeviceIdArray,urlAction);
	}else{
		console.log("deleteDeviceIdArray empty ..!");
		showErrorMsgPopUp(jsSpringMsg.deviceValidateMessage);
	}
	console.log('selected devices: ' + deleteDeviceIdArray);
}

/* Function will add element and set all required for delete. */

var selectedDeleteMappingId = new Array();
function addRowId(elementId, deviceId, mappingId) {
	
	var deviceElement = document.getElementById(elementId);
	// if ($("#" + elementId).is(':checked')) {
	if (deviceElement.checked) {

		if (ckIntanceSelected.indexOf(deviceId) === -1) {
			ckIntanceSelected.push(deviceId);
		}
		
		if (mappingId != "null" && selectedDeleteMappingId.indexOf(mappingId) === -1) {
			selectedDeleteMappingId.push(mappingId);
		}
	} else {

		if (ckIntanceSelected.indexOf(deviceId) !== -1) {
			ckIntanceSelected.splice(ckIntanceSelected.indexOf(deviceId), 1);
		}

		if (mappingId != "null" && selectedDeleteMappingId.indexOf(mappingId) !== -1) {
			selectedDeleteMappingId.splice(selectedDeleteMappingId
					.indexOf(mappingId), 1);
		}
	}
}

/*
 * function will display device name with link in grid. On-click it will open
 * the pop-up for edit device details.
 */
function deviceNameFormatter(cellvalue, options, rowObject) {
	if (String(rowObject["isPreConfigured"]) === '1'
			&& rowObject["mappingId"] === null
			|| String(rowObject["mappingId"]) === 'null') {
		return '<a class="link" onclick="displayUpdateDevicePopup(' + "'"
				+ rowObject["mappingId"] + "' ,'" + rowObject["deviceId"] + "' "
				+ ' )" value="'+cellvalue+'">' + cellvalue + '</a>';
	} else {
		return '<span>' + cellvalue + '</span>';
	}
}

/* Function will open edit device pop-up */
function displayUpdateDevicePopup(mappingId, deviceId) {

	$("#create_device_span").hide();
	$("#update_device_span").show();

	$("#close-device-buttons-div").hide();
	$("#add-device-buttons-div").show();
	$("#create_device_type_name_div").hide();
	$("#create_vendor_type_name_div").hide();
	var $grid = $("#deviceAndMappingList");
	var rowIds = $grid.jqGrid('getDataIDs');
	var currRow = rowIds[0];
	var rowData = $grid.jqGrid('getRowData', currRow);
	var decodeTypeVal = rowData.decodeType;
	if (mappingId === 'null' && deviceId > 0) {
		$.ajax({
			url : 'getDeviceById',
			cache : false,
			async : false,
			dataType : 'json',
			type : 'POST',
			data : {
				"deviceId" : deviceId,
				"decodeType" : decodeTypeVal,
			},
			success : function(data) {
				var response = eval(data);
				var responseCode = response.code;
				var responseMsg = response.msg;
				var responseObject = response.object;

				clearAllMessagesPopUp();
				resetWarningDisplayPopUp();

				if (responseCode === "200") {

					getAllDeviceTypeList();

					$("#deviceType_id").val(responseObject.deviceTypeId);

					getVendorByDeviceType('getVendorListByDeviceType', $(
							"#deviceType_id").val(), 'vendorType_id', ' ',
							'form');

					$("#deviceType_name").val("");
					$("#vendorType_id").val(responseObject.vendorTypeId);
					$("#vendorType_name").val("");
					$("#name").val(responseObject.deviceName);
					$("#description").val(responseObject.deviceDescription);
					$("#decodeType").val(responseObject.decodeType);
					$("#id").val(responseObject.deviceId);
					$("#deviceCurrentAction").val("EDIT");
					$("#addDevice_link").click();

					$("#create_vendor_type_name_div").hide();

				} else if (responseObject !== undefined
						|| responseObject !== 'undefined'
						|| responseCode === "400") {
					showErrorMsgPopUp(responseMsg);
				} else {
					showErrorMsgPopUp(responseMsg);
				}
			},
			error : function(xhr, st, err) {
				handleGenericError(xhr, st, err);
			}
		});
	}
}

/* Function will display all attribute list details for selected mapping. */
function mappingNameFormatter(cellvalue, options, rowObject) {

	if (rowObject["mappingId"] != null && rowObject["pluginType"]!="REGEX_PARSING_PLUGIN") {
		return '<a class="link" onclick="viewAllMappingAttributes(' + "'"
				+ rowObject["mappingId"] + "','" + rowObject["decodeType"]
				+ "','" + rowObject["mappingName"] + "' " + ')" value="'+cellvalue+'">' + cellvalue
				+ '</a>';
	} else {
		return "<span>" + cellvalue + "</span>";
	}
}

/* Function will display all association list for current mapping. */
function associationFormatter(cellvalue, options, rowObject) {

	if (rowObject["isAssociated"] === false) {
		return '<span style="cursor: pointer;" alt="'
				+ rowObject["isAssociated"]
				+ '"><i class="fa fa-retweet grey"></i></span>';

	} else if (rowObject["isAssociated"] === true) {
		return '<span id="mapping_association'
				+ rowObject["id"]
				+ '" src="img/orange.png"  style="cursor: pointer;" alt="'
				+ rowObject["isAssociated"]
				+ '" onclick="getAllAssociationDetails(\'getMappingAssociationDetails\','
				+ "'" + rowObject["mappingId"] + "','"
				+ rowObject["decodeType"] + "'"
				+ ',\'device\')"><i class="fa fa-retweet orange"></i></span>';
	}
}

/* Function will reset all search parameters */
function resetSearchParams() {
	clearAllMessages();
	clearResponseMsgDiv();
	clearResponseMsg();
	clearErrorMsg();
	clearAllMessagesPopUp();

	$("#search_device_type").val("");
	$("#search_vendor_type").val("");
	$("#search_device_name").val("");
	$("#search_decode_type").val("UPSTREAM");
	$("#search_mapping_name").val("");
	clearInstanceGrid("deviceList");
	reloadGridData();
}

/* Function will display add device popup */
function addDevicePopup() {
	clearAllMessagesPopUp();
	resetWarningDisplayPopUp();
	$("#close-device-buttons-div").hide();
	$("#add-device-buttons-div").show();

	$("#create_device_span").show();
	$("#update_device_span").hide();

	$("#deviceCurrentAction").val("ADD");
	$("#id").val("0");

	$("#addDevice_link").click();

	$("#deviceType_id").val("0");
	$("#create_device_type_name_div").show();

	var deviceTypeId = $("#deviceType_id").val();

	if (deviceTypeId === 'undefined' || deviceTypeId === ''
			|| deviceTypeId === null) {
		deviceTypeId = 0
	}
	getAllDeviceTypeList();
	getVendorByDeviceType('getVendorListByDeviceType', deviceTypeId,
			'vendorType_id', ' ', 'form');

	clearAllMessagesPopUp();
	resetWarningDisplayPopUp();

	$("#deviceType_name").val("");
	$("#vendorType_id").val("0");
	$("#create_vendor_type_name_div").show();

	$("#vendorType_name").val("");
	$("#name").val("");
	$("#description").val("");

}

/* Function will create new device */
function createDevice() {

	if ($("#deviceType_id option:selected").text() != 'OTHER') {
		
		$("#deviceType_name").val($("#deviceType_id option:selected").text());
	}
	if ($("#vendorType_id option:selected").text() != 'OTHER') {
		
		$("#vendorType_name").text($("#vendorType_id option:selected").text());
	}

	$("#add-device-buttons-div").hide();
	$("#device_proccess_bar_div").show();
	clearAllMessagesPopUp();
	resetWarningDisplayPopUp();
	clearResponseMsgDiv();
	$.ajax({
		url : 'createDevice',
		cache : false,
		async : true,
		dataType : 'json',
		type : "POST",
		data : {
			"id" : $("#id").val(),
			"name" : $("#name").val(),
			"deviceType.id" : $("#deviceType_id").val(),
			"vendorType.id" : $("#vendorType_id").val(),
			"deviceType.name" : $("#deviceType_name").val(),
			"vendorType.name" : $("#vendorType_name").val(),
			"decodeType" : $("#decodeType").val(),
			"deviceCurrentAction" : $("#deviceCurrentAction").val()
		},
		success : function(data) {
			$("#device_proccess_bar_div").hide();
			var response = eval(data);
			var responseCode = response.code;
			var responseMsg = response.msg;
			var responseObject = response.object;

			if (responseCode == "200") {
				
				$("#close-device-buttons-div").show();
				$("#add-device-buttons-div").hide();
				parent.reloadGridData();
				parent.showSuccessMsg(responseMsg);
				parent.closeFancyBox();
			} else if (responseObject != undefined
					&& responseObject != 'undefined' && responseCode == "400") {
				
				$("#add-device-buttons-div").show();
				$("#device_proccess_bar_div").hide();

				$.each(responseObject, function(key, val) {

					if (key == 'deviceType.name') {
						$("#deviceType_name").next().children().first().attr(
								"data-original-title", val).attr("id",key+"_error");
					} else if (key == 'vendorType.name') {
						$("#vendorType_name").next().children().first().attr(
								"data-original-title", val).attr("id",key+"_error");
					} else {
						$("#" + key).next().children().first().attr(
								"data-original-title", val).attr("id",key+"_error");
					}
					addErrorClassWhenTitleExistPopUp($(
							"#GENERIC_ERROR_MESSAGE", window.parent.document)
							.val());
				});
			} else {
				showErrorMsgPopUp(responseMsg);
				$("#add-device-buttons-div").show();
				$("#device_proccess_bar_div").hide();
			}
		},
		error : function(xhr, st, err) {
			$("#device_proccess_bar_div").hide();
			handleGenericError(xhr, st, err);
		}
	});
}

/* Function will got all device type list */
function getAllDeviceTypeList() {
	$.ajax({
		url : 'getAllDeviceTypeList',
		cache : false,
		async : false,
		dataType : 'json',
		type : 'POST',
		success : function(data) {
			var response = eval(data);
			var responseCode = response.code;
			var responseMsg = response.msg;
			var responseObject = response.object;

			if (responseCode == "200") {
				$('#deviceType_id').empty();

				$.each(responseObject, function(index, responseObject) {
					$('#deviceType_id').append(
							"<option value='" + responseObject["id"] + "'>"
									+ responseObject["name"] + "</option>");
				});
				$('#deviceType_id').append(
						"<option value='0' selected='selected'>"
								+ jsSpringMsg.selOther + "</option>");

				setElementValue('0', 'deviceType_id');

			} else if (responseObject != undefined
					|| responseObject != 'undefined' || responseCode == "400") {
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

/* Function will display all association details for current selected mapping. */
function getAllAssociationDetails(url, mappingId, decodeType, currentLocation) {

	$
			.ajax({
				url : url,
				cache : false,
				async : true,
				dataType : 'json',
				type : 'POST',
				data : {
					"mappingId" : mappingId,
					"decodeType" : decodeType,
				},
				success : function(data) {
					var response = eval(data);
					var responseCode = response.code;
					var responseMsg = response.msg;
					var responseObject = response.object;
					var tableString = "<table class='table table-hover table-bordered'  border='1' >";
					tableString += "<th>" + jsSpringMsg.srno + "</th><th>"
							+ jsSpringMsg.serverInsName + " </br>"
							+ jsSpringMsg.serverIPPORT + "</th><th>"
							+ jsSpringMsg.serviceInsName + "</th><th>"
							+ jsSpringMsg.pluginName + "</th>";

					if (responseCode == "200") {
						$.each(responseObject, function(index, responseObject) {
							tableString += "<tr>";
							tableString += "<td>" + (index + 1) + "</td>";
							tableString += "<td>"
									+ responseObject['serverInstanceName']
									+ "</br>"
									+ responseObject['serverIpAddress'] + " : "
									+ responseObject['serverInstancePort']
									+ " </td>";
							tableString += "<td>"
									+ responseObject['serviceName'] + "</td>";
							tableString += "<td>"
									+ responseObject['pluginName'] + "</td>";
							tableString += "</tr>";
						});

					} else {
						if (currentLocation != 'device') {
							$("#selected_mapping_association_details_div")
									.hide();
						}
					}

					tableString += "</table>";
					if (currentLocation == 'device') {
						$('#mapping_association_list_div').html(tableString);
						$("#view_mapping_association_link").click();
					} else {
						tableString += "<div><span class='note'>"
								+ jsSpringMsg.importantNote
								+ "</span></br><span>"
								+ jsSpringMsg.associationContent
								+ "</span></div>"
						$('#selected_mapping_association_details_div').html(
								tableString);

					}
				},
				error : function(xhr, st, err) {
					handleGenericError(xhr, st, err);
				}

			});
}

/* Function will set element value */
function setElementValue(elementValue, elementId) {
	$('#' + elementId).val(elementValue);
}

/* Function will display all mapped attribute list for current selected mapping */
function viewAllMappingAttributes(mappingId, deviceType, mappingName) {

	$.ajax({
				url : 'getAttributeListByMappingId',
				cache : false,
				async : true,
				dataType : 'json',
				type : 'POST',
				data : {
					"mappingId" : mappingId,
					"deviceType" : deviceType,
				},
				success : function(data) {
					var response = data;
					var responseCode = response.code;
					var responseObject = response.object;
					var tableString = "<table class='table table-hover table-bordered'  border='1'  >";
					tableString += "<th>" + jsSpringMsg.srno + "</th><th>"
							+ jsSpringMsg.sourceField + "</th><th>"
							+ jsSpringMsg.unifieldField + "</th><th>"
							+ jsSpringMsg.attDescription + "</th><th>"
							+ jsSpringMsg.defaultVal + "</th><th>"
							+ jsSpringMsg.trimChar + "</th><th>"
							+ jsSpringMsg.dateFormat + "</th>"
					if (responseCode === "200") {

						$
								.each(
										responseObject,
										function(index, responseObject) {

											tableString += "<tr>";
											tableString += "<td>" + (index + 1)
													+ "</td>";

											if (responseObject['sourceField'] !== ''
													&& responseObject['sourceField'] !== 'null'
														&& responseObject['sourceField'] !== undefined
													&& responseObject['sourceField'] !== null) {
												tableString += "<td>"
														+ responseObject['sourceField']
														+ "</td>";
											} else {
												tableString += "<td></td>";
											}

											if (responseObject['unifiedField'] !== ''
													&& responseObject['unifiedField'] !== 'null'
														&& responseObject['unifiedField'] !== undefined
													&& responseObject['unifiedField'] !== null) {
												tableString += "<td>"
														+ responseObject['unifiedField']
														+ "</td>";
											} else {
												tableString += "<td></td>";
											}
											if (responseObject['description'] !== ''
													&& responseObject['description'] !== 'null'
													&& responseObject['description'] !== null) {
												tableString += "<td>"
														+ responseObject['description']
														+ "</td>";
											} else {
												tableString += "<td></td>";
											}
											if (deviceType == 'UPSTREAM') {
												if (responseObject['defaultValue'] !== ''
														&& responseObject['defaultValue'] !== 'null'
														&& responseObject['defaultValue'] !== null) {
													tableString += "<td>"
															+ responseObject['defaultValue']
															+ "</td>";
												} else {
													tableString += "<td></td>";
												}
											} else if (deviceType == 'DOWNSTREAM') {
												if (responseObject['defualtValue'] !== ''
														&& responseObject['defualtValue'] !== 'null'
														&& responseObject['defualtValue'] !== null) {
													tableString += "<td>"
															+ responseObject['defualtValue']
															+ "</td>";
												} else {
													tableString += "<td></td>";
												}
											}
											if (deviceType == 'UPSTREAM') {
												if (responseObject['trimChars'] !== ''
														&& responseObject['trimChars'] !== 'null'
														&& responseObject['trimChars'] !== null) {
													tableString += "<td>"
															+ responseObject['trimChars']
															+ "</td>";
												} else {
													tableString += "<td></td>";
												}
											} else if (deviceType == 'DOWNSTREAM') {
												if (responseObject['trimchars'] !== ''
														&& responseObject['trimchars'] !== 'null'
														&& responseObject['trimchars'] !== null) {
													tableString += "<td>"
															+ responseObject['trimchars']
															+ "</td>";
												} else {
													tableString += "<td></td>";
												}
											}
											if (deviceType == 'DOWNSTREAM') {
											if (responseObject['dateFormat'] !== ''
													&& responseObject['dateFormat'] !== 'null'
													&& responseObject['dateFormat'] !== null) {
												tableString += "<td>"
														+ responseObject['dateFormat']
														+ "</td>";
											} else {
												tableString += "<td></td>";
											}

											tableString += "</tr>";
											}
											else{
												tableString += "<td></td>";
											}

										});

						$('#attribute_list_div').html(tableString);
					} else if (responseObject !== undefined
							|| responseObject !== 'undefined'
							|| responseCode === "400") {
						tableString += "<tr>";
						tableString += "<td colspan='7' align='center'>"
								+ jsSpringMsg.attrNotFound + "</td>";
						tableString += "</tr>";

						$('#attribute_list_div').html(tableString);
					}
					tableString += "</table>";
					$('#attribute_name_label').html(mappingName);
					$("#view_attribute_link").click();
				},
				error : function(xhr, st, err) {
					handleGenericError(xhr, st, err);
				}

			});
}

/* Function will populate the vendor type drop down by device type. */
function getVendorByDeviceType(urlAction, deviceTypeId, firstElementId,
		secondElementId, flag) {
	$.ajax({
				url : urlAction,
				cache : false,
				async : false,
				dataType : 'json',
				type : 'POST',
				data : {
					"deviceTypeId" : deviceTypeId,
				},
				success : function(data) {
					var response = eval(data);
					var responseCode = response.code;
					var responseMsg = response.msg;
					var responseObject = response.object;

					if (responseCode == "200") {
						$("#" + firstElementId).empty();

						if (flag != 'form') {
							$("#" + firstElementId).append(
									"<option value='-1' selected='selected'>"
											+ jsSpringMsg.selVendorType
											+ "</option>");
							$("#deviceName").empty();
							$("#deviceName").append(
									"<option value='-1' selected='selected'>"
											+ jsSpringMsg.selDevice
											+ "</option>");
							$("#mappingName").empty();
							$("#mappingName").append(
									"<option value='-1' selected='selected'>"
											+ jsSpringMsg.selmappingName
											+ "</option>");
						}
						$.each(responseObject, function(index, responseObject) {
							$('#' + firstElementId).append(
									"<option value='" + responseObject[0]
											+ "'>" + responseObject[1]
											+ "</option>");
						});

						if (flag == 'form') {
							$('#' + firstElementId).append(
									"<option value='0' selected='selected'>"
											+ jsSpringMsg.selOther
											+ "</option>");
$("#create_vendor_type_name_div").show();
//							displayDevicetypeandVendorTypeElement(0,
//									'create_vendor_type_name_div');

						}

					} else if (responseObject != undefined
							|| responseObject != 'undefined'
							|| responseCode == "400") {

						$("#" + firstElementId).empty();

						if (flag == 'form') {
							$('#' + firstElementId).append(
									"<option value='0'>" + jsSpringMsg.selOther
											+ "</option>");
							displayDevicetypeandVendorTypeElement(0,
									'create_vendor_type_name_div');
						} else {
							$("#" + firstElementId).append(
									"<option value='-1' selected='selected'>"
											+ jsSpringMsg.selVendorType
											+ "</option>");
							$("#deviceName").empty();
							$("#deviceName").append(
									"<option value='-1' selected='selected'>"
											+ jsSpringMsg.selDevice
											+ "</option>");
							$("#mappingName").empty();
							$("#mappingName").append(
									"<option value='-1' selected='selected'>"
											+ jsSpringMsg.selmappingName
											+ "</option>");
						}
					} else {

						if (flag != 'form') {
							$("#" + firstElementId).empty();
							$("#" + firstElementId).append(
									"<option value='-1' selected='selected'>"
											+ jsSpringMsg.selVendorType
											+ "</option>");
						}

						showErrorMsgPopUp(responseMsg);
						$("#deviceName").empty();
						$("#deviceName").append(
								"<option value='-1' selected='selected'>"
										+ jsSpringMsg.selDevice + "</option>");
						$("#mappingName").empty();
						$("#mappingName").append(
								"<option value='-1' selected='selected'>"
										+ jsSpringMsg.selmappingName
										+ "</option>");
					}
				},
				error : function(xhr, st, err) {
					handleGenericError(xhr, st, err);
				}
			});
}

/* Function will populate the device drop down by vendor type. */
function getDeviceByVendorName(urlAction, deviceTypeId, vendorId, elementId) {
	$.ajax({
		url : urlAction,
		cache : false,
		async : false,
		dataType : 'json',
		type : 'POST',
		data : {
			"deviceTypeId" : deviceTypeId,
			"vendorTypeId" : vendorId,
			"decodeType" : $("#decodeType").val()
		},
		success : function(data) {
			var response = eval(data);
			var responseCode = response.code;
			var responseMsg = response.msg;
			var responseObject = response.object;

			responseObject = eval(responseObject);
			if (responseCode == "200") {
				$("#" + elementId).empty();
				$("#" + elementId).html(
						"<option value='-1' selected='selected'>"
								+ jsSpringMsg.selDevice + "</option>");
				$.each(responseObject, function(index, responseObject) {
					$("#" + elementId).append(
							"<option value='" + responseObject.id + "'>"
									+ responseObject.name + "</option>");
				});
			} else if (responseObject != undefined
					|| responseObject != 'undefined' || responseCode == "400") {
				$("#" + elementId).empty();
				$("#" + elementId).html(
						"<option value='-1' selected='selected'>"
								+ jsSpringMsg.selDevice + "</option>");
			}
			$("#mappingName").empty();
			$("#mappingName").append(
					"<option value='-1' selected='selected'>"
							+ jsSpringMsg.selmappingName + "</option>");
		},
		error : function(xhr, st, err) {
			handleGenericError(xhr, st, err);
		}
	});
}

/*
 * Function will display delete device pop up and warning message for multiple
 * selected box
 */

function displayDeleteDevicePopup() {
	
	if (ckIntanceSelected.length == 0) {
		$("#lessWarn").show();
		$("#moreWarn").hide();
		$("#delete_warn_msg_link").click();
		return;
	} else {
		clearAllMessages();
		clearResponseMsgDiv();
		clearResponseMsg();
		clearErrorMsg();
		clearAllMessagesPopUp();
		//get all selected rowIds from main grid
		selectedDeviceAndMappingId = [];
		   $.each($("input[name='deviceAndMappingCk']:checked"), function(){            
			   selectedDeviceAndMappingId.push($(this).val());
		   });
		//console.log("selectedDeviceAndMappingId: " + selectedDeviceAndMappingId);
		//var $mapping_grid = $("#mappingList");
		
		deleteMappingIdArray=[];
		deleteDeviceIdArray=[];
		uniqueDeviceIdArray=[];
		rowDataToShowInMappingTab=[];
		rowDataToShowInDeviceTab=[];
		createMappingAndDeviceData(selectedDeviceAndMappingId);
		
		reloadPopUpMappingData();
		
		/** Upadated by Urja -MED-4951-Start**/
		
		if(rowDataToShowInMappingTab.length>0){
			$('#mapping_selection_to_delete_tab').addClass('active');
			$('#mapping_selection_to_delete').addClass('tab-pane active');
			$('#device_selection_to_delete_tab').removeClass('active');
			$('#device_selection_to_delete').removeClass('tab-pane active');
			$('#device_selection_to_delete').addClass('tab-pane');
		}
		else{
			$('#mapping_selection_to_delete_tab').removeClass('active');
			$('#mapping_selection_to_delete').removeClass('active');
			$('#mapping_selection_to_delete').addClass('tab-pane');
			$('#device_selection_to_delete_tab').addClass('active');
			$('#device_selection_to_delete').addClass('tab-pane active');
		}
		/** End**/
		
		$("#delete_device_and_mapping_link").click();
	}
}


function deleteMappings(deleteMappingIdArray,urlAction){
	clearAllMessagesPopUp();
	resetWarningDisplayPopUp();
	clearResponseMsgDiv();
	
	$("#delete_mapping_bts_div").hide();
	$("#delete_mapping_progress_bar_div").show();
	
	$.ajax({
		url : urlAction,
		cache : false,
		async : true,
		dataType : 'json',
		type : 'POST',
		data : {
			"mappingIdList" : deleteMappingIdArray.toString(),
			"decodeType" : selectedDecodeType
		},
		success : function(data) {
			var response = data;
			var responseCode = response.code;
			var responseMsg = response.msg;
			var responseObject = response.object;
			ckIntanceSelected = new Array();
			if (responseCode === "200") {
				//selectedDeleteMappingId = new Array();
				parent.reloadGridData();
				parent.showSuccessMsg(responseMsg);
				parent.closeFancyBox();
			} else {
				showErrorMsgPopUp(responseMsg);
			}
			$("#delete_mapping_bts_div").show();
			$("#delete_mapping_progress_bar_div").hide();
		},
		error : function(xhr, st, err) {
			handleGenericError(xhr, st, err);
		}
	});
	
}

function deleteDevicesAndMappings(deleteDeviceIdArray, urlAction){
	clearAllMessagesPopUp();
	resetWarningDisplayPopUp();
	clearResponseMsgDiv();
	
	$("#delete_device_bts_div").hide();
	$("#delete_device_progress_bar_div").show();
	
	$.ajax({
		url : urlAction,
		cache : false,
		async : true,
		dataType : 'json',
		type : 'POST',
		data : {
			"deviceIds" : deleteDeviceIdArray.toString(),
			"decodeType" : selectedDecodeType
		},
		success : function(data) {
			var response = data;
			var responseCode = response.code;
			var responseMsg = response.msg;
			var responseObject = response.object;
			ckIntanceSelected = new Array();
			if (responseCode === "200") {
				//selectedDeleteMappingId = new Array();
				parent.reloadGridData();
				parent.showSuccessMsg(responseMsg);
				parent.closeFancyBox();
			} else {
				//$("#delete_device_bts_div").show();
				//$("#delete_device_progress_bar_div").hide();
				showErrorMsgPopUp(responseMsg);
			}
			$("#delete_device_bts_div").show();
			$("#delete_device_progress_bar_div").hide();
		},
		error : function(xhr, st, err) {
			handleGenericError(xhr, st, err);
		}
	});
	
}

/* Function will display device type and vendor type hidden name element text box*/
function displayDevicetypeandVendorTypeElement(selectedVal, elementId) {
	if (selectedVal === '0') {
		$("#" + elementId).show();
	} else {
		$("#" + elementId).hide();
	}
}
