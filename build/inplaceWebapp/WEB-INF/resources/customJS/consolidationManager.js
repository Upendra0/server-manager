/* Function will display progress bar for add/edit action in pop-up. */
function showProgressBar(driverListCounter){
	$('#' + driverListCounter + '_buttons-div').hide();
	$('#' + driverListCounter + '_progress-bar-div').show();
}
/*Function will hide progress bar for add/edit action in pop-up. */
function hideProgressBar(driverListCounter){
	$('#' + driverListCounter + '_buttons-div').show();
	$('#' + driverListCounter + '_progress-bar-div').hide();
}

/*Function will reset number for application order. */
function resetNumbering(){
	
	var listItems = $("#draggableDriverDiv li");
	var counter=0;
	listItems.each(function(idx, li) {
	    var ele = $(li);
	    if($(ele).attr('id') === undefined)
	    	$(ele).remove();
	    else
	    	changeDriverOrder($(ele).attr('id'),counter++);
	});
	
	updateDistributionDriverOrder();
}



function changeDriverOrder(driverId,order){
	for(var index=0;index<driverListDetail.length;index++){
		if(driverListDetail[index].id === driverId)
			driverListDetail[index].order=order;
	}
}

/*Function will redirect to driver configuration page. */
function redirectToDriverConfig(driverListCounter){
	
	$('#driverTypeAlias').val($('#' + driverListCounter + '_driverAlias').val());
	$('#driverId').val($('#' + driverListCounter + '_driverId').val());
	$('#distribution-driver-config-form').submit();
}
/*Function will redirect to distribution service */
function viewDistributionService(){
	$("#distribution_service_form").submit();
}

/* Function will load data for distribution driver configuration and pathlist configuration tab */
function loadDriverTabData(formAction){
	$("#loadDistributionDriverConfiguration").attr("action",formAction);
	$("#loadDistributionDriverConfiguration").submit();
}

/*Function will load dynamic jqgrid for grouping attribute HD */
function loadGroupingAttributeGrid(counter,consGrpAttList){
	$.each(consGrpAttList,function(index,consGrp){
		consGrp.edit = '<a href="#" id="editGroupingAttribute_'+consGrp.groupingField+'" onclick="displayGroupEditPopup('+parseInt(consGrp.id)+','+counter+')"><i class="fa fa-pencil-square-o" aria-hidden="true" id="img_'+parseInt(consGrp.id)+'" data-toggle="tooltip" data-placement="bottom" style="cursor:pointer;"></i></a>';
	});
	
	var uniqueGridId = counter+"_group_grid";
	var selectAllCheckboxId = counter+"_selectAllGroupingAttribute";
	var childCheckboxName = counter+"_groupingAttributeCheckbox";
	
	$("#"+uniqueGridId).jqGrid({
    	url: "",
        datatype: "local",
        colNames:[
                  "id",
                  "<input type='checkbox' id='"+selectAllCheckboxId+"' onclick='attributeHeaderCheckbox(event, this, \""+childCheckboxName+"\")'></input>",
                  jsSpringMsg.groupingField,
                  jsSpringMsg.regexEnabled,
                  jsSpringMsg.regexExpressing,
                  jsSpringMsg.regexMappedField,
                  jsSpringMsg.lookUpTableEnabled,
                  jsSpringMsg.lookUpTableName,
                  jsSpringMsg.columnName,
                  jsSpringMsg.edit
                 ],
		colModel:[
		          {name:'id',index:'id',hidden:true,sortable:false},
		          {name:'checkbox',index:'checkbox',sortable:false,align:'center', width : '30%', formatter: function(cellvalue, options, rowObject) {
						return groupingAttributeCheckboxFormatter(cellvalue, options, rowObject, uniqueGridId, selectAllCheckboxId, childCheckboxName);	
		          }},
		          {name:'groupingField',index:'groupingField',hidden:false,sortable:false},
		          {name:'regExEnable',index:'regExEnable',hidden:false,sortable:false},
		          {name:'regExExpression',index:'regExExpression',hidden:false,sortable:false},
		          {name:'destinationField',index:'destinationField',hidden:false,sortable:false},
		          {name:'enableLookup',index:'enableLookup',hidden:false,sortable:false},
		          {name:'lookUpTableName',index:'lookUpTableName',hidden:false,sortable:false},
		          {name:'lookUpTableColumnName',index:'lookUpTableColumnName',hidden:false,sortable:false},
		          {name:'edit',index:'edit',hidden:false,sortable:false}
        ],
        rowNum:5,
        rowList:[10,20,60,100],
        height: 'auto',
        mtype:'POST',
		sortname: 'id',
 		sortorder: "desc",
        pager: "#"+counter+"_group_gridPagingDiv",
        contentType: "application/json; charset=utf-8",
        viewrecords: true,
        //multiselect: true,
        timeout : 120000,
        loadtext: jsSpringMsg.loadingText,
        caption: "",
        beforeRequest:function(){
            $(".ui-dialog-titlebar").hide();
        }, 
        beforeSelectRow: function(rowid, e){
        	var $myGrid = $(this),
            i = $.jgrid.getCellIndex($(e.target).closest('td')[0]),
            cm = $("#"+counter+"_group_grid").jqGrid('getGridParam', 'colModel');
        	return (cm[i].name === 'cb');
        },
        onSelectRow: function(rowid, status) {
            $('#'+rowid).removeClass('ui-state-highlight');
        },
        beforeSend: function(xhr) {
        	xhr.setRequestHeader("Accept", "application/json");
            xhr.setRequestHeader("Content-Type", "application/json");
        },
        data :consGrpAttList,
		onPaging: function () {
		},
		loadError : function(xhr,st,err) {
			handleGenericError(xhr,st,err);
		},
		onSelectAll:function(){
		},
		recordtext: jsSpringMsg.totalRecordText ,
        emptyrecords: jsSpringMsg.emptyRecordText,
        loadtext: jsSpringMsg.loadingText,
		pgtext : jsSpringMsg.pagerText,
	}).navGrid("#group_gridPagingDiv",{edit:false,add:false,del:false,search:false});
	
	$(".ui-jqgrid-titlebar").hide();
}

/*
 * this function will handle select all/ de select all event
 */
function attributeHeaderCheckbox(e, element, childCheckboxName) {
    e = e || event;/* get IE event ( not passed ) */
    e.stopPropagation ? e.stopPropagation() : e.cancelBubble = false;

    //if root checkbox is checked :-> all child checkbox should ne checked
    if(element.checked) {
    	$('input:checkbox[name="'+childCheckboxName+'"]').prop('checked',true);
    } else {
    	//if root checkbox is unchecked :-> all child checkbox should be unchecked
    	$('input:checkbox[name="'+childCheckboxName+'"]').prop('checked',false);
    }
}

function groupingAttributeCheckboxFormatter(cellvalue, options, rowObject, uniqueGridId, rootCheckboxId, childCheckboxName) {
	var uniqueId = "groupAttributeCheckbox";
	if(rowObject["groupingField"] !== "") {
		uniqueId += "_" + rowObject["groupingField"]; 
	}
	return '<input type="checkbox" id="'+uniqueId+'" name="'+childCheckboxName+'" value="'+rowObject["id"]+'" onchange="updateRootCheckbox(this, \''+uniqueGridId+'\', \''+rootCheckboxId+'\', \''+childCheckboxName+'\')"/>';
}

/*
 * this function will handle root checkbox from child checkbox
 */
function updateRootCheckbox(element, gridId, rootCheckboxId, childCheckboxName) {
	if(!element.checked){
		// if current child checkbox is not checked : uncheck root checkbox
		$('input:checkbox[id="'+rootCheckboxId+'"]').prop('checked',false);
	} else {
		//if current child checkboox is checked and all checkbox are checked then check root checkbox
		var count = jQuery("#"+gridId).jqGrid('getGridParam', 'records');
		if ($('input:checkbox[name="'+childCheckboxName+'"]:checked').length === count) {
			$('input:checkbox[id="'+rootCheckboxId+'"]').prop('checked',true);
	    }
	}
}


/*Function will load dynamic jqgrid for grouping attribute HD */
function loadConsolidationAttributeGrid(counter,consAttList){
	$.each(consAttList,function(index,consAtt){
		consAtt.edit = '<a href="#" id="editConsolidationAttribute_'+consAtt.fieldName+'" onclick="displayAttributeEditPopup('+parseInt(consAtt.id)+','+counter+')"><i class="fa fa-pencil-square-o" aria-hidden="true" id="att_img_'+parseInt(consAtt.id)+'" data-toggle="tooltip" data-placement="bottom" style="cursor:pointer;"></i></a>';
	});
	
	var uniqueGridId = counter+"_cons_grid";
	var selectAllCheckboxId = counter+"_selectAllConsolidationAttribute";
	var childCheckboxName = counter+"_consolidationAttributeCheckbox";
	
	$("#"+counter+"_cons_grid").jqGrid({
    	url: "",
        datatype: "local",
        colNames:[
                  "id",
                  "<input type='checkbox' id='"+selectAllCheckboxId+"' onclick='attributeHeaderCheckbox(event, this, \""+childCheckboxName+"\")'></input>",
                  jsSpringMsg.fieldName,
                  jsSpringMsg.datatype,
                  jsSpringMsg.operation,
                  jsSpringMsg.description,
                  jsSpringMsg.edit
                 ],
		colModel:[
		          {name:'id',index:'id',hidden:true,sortable:false},
		          {name:'checkbox',index:'checkbox',sortable:false,align:'center', width : '30%', formatter: function(cellvalue, options, rowObject) {
						return consolidationAttributeCheckboxFormatter(cellvalue, options, rowObject, uniqueGridId, selectAllCheckboxId, childCheckboxName);	
		          }},
		          {name:'fieldName',index:'fieldName',sortable:false},
		          {name:'dataType',index:'dataType',sortable:false},
		          {name:'operation',index:'operation',sortable:false},
		          {name:'description',index:'description',sortable:false},
		          {name:'edit',index:'edit',hidden:false,sortable:false}
        ],
        rowNum:5,
        rowList:[10,20,60,100],
        height: 'auto',
        mtype:'POST',
		sortname: 'id',
 		sortorder: "desc",
        pager: "#"+counter+"_cons_gridPagingDiv",
        contentType: "application/json; charset=utf-8",
        viewrecords: true,
        //multiselect: true,
        timeout : 120000,
        loadtext: jsSpringMsg.loadingText,
        caption: "",
        beforeRequest:function(){
            $(".ui-dialog-titlebar").hide();
        }, 
        beforeSend: function(xhr) {
        	xhr.setRequestHeader("Accept", "application/json");
            xhr.setRequestHeader("Content-Type", "application/json");
        },
        beforeSelectRow: function(rowid, e){
        	var $myGrid = $(this),
            i = $.jgrid.getCellIndex($(e.target).closest('td')[0]),
            cm = $("#"+counter+"_cons_grid").jqGrid('getGridParam', 'colModel');
        	return (cm[i].name === 'cb');
        },
        onSelectRow: function(rowid, status, e) {
            $('#'+rowid).removeClass('ui-state-highlight');
        },
        data :consAttList,
		onPaging: function () {
		},
		loadError : function(xhr,st,err) {
			handleGenericError(xhr,st,err);
		},
		onSelectAll:function(){
		},
		recordtext: jsSpringMsg.totalRecordText ,
        emptyrecords: jsSpringMsg.emptyRecordText,
        loadtext: jsSpringMsg.loadingText,
		pgtext : jsSpringMsg.pagerText,
	}).navGrid("#cons_gridPagingDiv",{edit:false,add:false,del:false,search:false});
	
	$(".ui-jqgrid-titlebar").hide();
}

function consolidationAttributeCheckboxFormatter(cellvalue, options, rowObject, uniqueGridId, rootCheckboxId, childCheckboxName) {
	var uniqueId = "consolidationAttributeCheckbox";
	if(rowObject["fieldName"] !== "") {
		uniqueId += "_" + rowObject["fieldName"]; 
	}
	return '<input type="checkbox" id="'+uniqueId+'" name="'+childCheckboxName+'" value="'+rowObject["id"]+'" onchange="updateRootCheckbox(this, \''+uniqueGridId+'\', \''+rootCheckboxId+'\', \''+childCheckboxName+'\')"/>';
}


/*Function will reset pathlist parameter for selected block */
function resetConsolidationDefinationParams(counter){
	resetWarningDisplay();
	clearAllMessages();
	$('#'+ counter+ '_block input').val('');
}


/*Function will create new distribution pathlist */
/* HD Changed */
function addConsolidationDefination(counter){
		resetWarningDisplay();
		clearAllMessages();
		showProgressBar(counter);
		var pathListId = '0';
		$('#title_'+counter).html($("#"+counter+"_name").val());
		
		if($("#"+counter+"_acrossFilePartition").val() === ''){
			$("#"+counter+"_acrossFilePartition").val('-1');
		}
		
		$.ajax({
		url: 'createConsolidationDefinitionList',
		cache: false,
		async: true,
		dataType: 'json',
		type: "POST",
		data:{
			"consName"					: $("#"+counter+"_consName").val(),
			"description"				: $("#"+counter+"_description").val(),
			"dateFieldName"				: $("#"+counter+"_dateFieldName").val(),
			"segregationType"		    : $("#"+counter+"_segragationType").val(),
			"acrossFilePartition"		: $("#"+counter+"_acrossFilePartition").val(),
			"consService.id" 			: $('#serviceId').val(),
			"conListCount"			    : counter
		}, 
		success: function(data){
			hideProgressBar(counter);
			
			var responseCode = data.code; 
			var responseMsg = data.msg; 
			var responseObject = data.object;
			
			console.log("Resposne code is :: " + responseCode);
			
			if(responseCode === "200"){
				
				console.log("else if part with 200");
				resetWarningDisplay();
				clearAllMessages();
				hideProgressBar(counter);
				
				var respObj = responseObject;
				var conListId = respObj['id'];
				// set pathlist id hidden for all pathlist block
				$('#'+counter+'_consListId').val(conListId);
				// update block title with path list name
				$('#title_'+counter).html(respObj['consName']);
				$('#'+counter+'_group_grid_main_div').show();
				$('#'+counter+'_group_grid_links_div').show();
				$('#'+counter+'_group_gridPagingDiv').show();
				
				$('#'+counter+'_cons_grid_main_div').show();
				$('#'+counter+'_cons_grid_links_div').show();
				$('#'+counter+'_cons_gridPagingDiv').show();
				
				
	 			$('#'+counter+'_updatebtn').show();
	 			$('#'+counter+'_savebtn').hide();
	 			showSuccessMsg(responseMsg);
	 			
			}else if(responseObject !== undefined && responseObject !== 'undefined' && responseCode === "400"){
				console.log("else if part with 400");
				hideProgressBar(counter);
				addErrorIconAndMsgForAjax(responseObject);
			}else{
				console.log("Going in else part.");
				hideProgressBar(counter);
				resetWarningDisplay();
				clearAllMessages();
				showErrorMsg(responseMsg);
			}
		},
	    error: function (xhr,st,err){
	    	hideProgressBar(counter);
			handleGenericError(xhr,st,err);
		}
	});
}


/*Function will update distribution HD drive path list */
function updateConsolidationDefination(counter){
	resetWarningDisplay();
	clearAllMessages();
	showProgressBar(counter);
	
	if($("#"+counter+"_acrossFilePartition").val() === ''){
		$("#"+counter+"_acrossFilePartition").val('-1');
	}
	
	$.ajax({
		url: 'updateConsolidationDefinitionList',
		cache: false,
		async: true,
		dataType: 'json',
		type: "POST",
		data:{
			"id"						: $("#"+counter+"_consListId").val(),
			"consName"					: $("#"+counter+"_consName").val(),
			"description"				: $("#"+counter+"_description").val(),
			"dateFieldName"				: $("#"+counter+"_dateFieldName").val(),
			"segregationType"		    : $("#"+counter+"_segragationType").val(),
			"acrossFilePartition"		: $("#"+counter+"_acrossFilePartition").val(),
			"consService.id" 			: $('#serviceId').val(),
			"conListCount"			    : counter
		},  
		success: function(data){
			hideProgressBar(counter);
			
			var responseCode = data.code; 
			var responseMsg = data.msg; 
			var responseObject = data.object;
			
			if(responseCode === "200"){
				resetWarningDisplay();
				clearAllMessages();

				var respObj = responseObject;
				var pathListId = respObj['id'];
				if(respObj != null && respObj == 90){
					dateFieldName = respObj.fieldName;
				}
				// set pathlist id hidden for all pathlist block
				$('#'+counter+'_pathListId').val(pathListId);
				// update block title with path list name
				
				$('#title_'+counter).html(respObj['consName']);
	 			$('#'+counter+'_updatebtn').show();
	 			$('#'+counter+'_savebtn').hide();
	 			
	 			showSuccessMsg(responseMsg);
 				closeFancyBox();
				
			}else if(responseObject !== undefined && responseObject !== 'undefined' && responseCode === "400"){
				hideProgressBar(counter);
				addErrorIconAndMsgForAjax(responseObject);
			}else{
				hideProgressBar(counter);
				resetWarningDisplay();
				clearAllMessages();
				showErrorMsg(responseMsg);
			}
		},
	    error: function (xhr,st,err){
	    	hideProgressBar(counter);
			handleGenericError(xhr,st,err);
		}
	});	
}

/*Function will delete pathlist or remove blank block HD */
var deleteBlockId = '';
function deleteConsolidationDefination(counter){
	
	var pathListId = $('#'+counter+'_consListId').val();
	deleteBlockId = counter;
	
	if(pathListId === null || pathListId === 'null' || pathListId === ''){
		$("#flipbox_"+counter).remove();
	}else{
		$("#deletepathListId").val(pathListId);
		$("#closeBtn").hide();
		$("#pathlistMessage").click();
	}
}


/*Function will remove distribution driver pathlist HD*/
function deleteConsolidationDefinationPerm(){
	resetWarningDisplay();
	clearAllMessages();
	 var pathListIdaaa = $("#deletepathListId").val();
	 var serviceId = $('#serviceId').val();
	 $.ajax({
		url: 'deleteConsolidationDefinitionList',
		cache: false,
		async: true,
		dataType: 'json',
		type: "POST",
		data:
		 {
			"id"   : pathListIdaaa,
			"consService.id" : serviceId,
			"conListCount" : deleteBlockId
		 },
		
		success: function(data){
			var response = data;
			var responseCode = response.code; 
			var responseMsg = response.msg; 
			var responseObject = response.object;
			
			if(responseCode === "200"){
				resetWarningDisplay();
				clearAllMessages();
				showSuccessMsg(responseMsg);
				$("#flipbox_"+deleteBlockId).remove();
				closeFancyBox();
			}else if(responseObject !== undefined && responseObject !== 'undefined' && responseCode === "400"){
				showErrorMsg(responseMsg);
				closeFancyBox();
			}else{
				resetWarningDisplay();
				clearAllMessages();
				showErrorMsg(responseMsg);
				closeFancyBox();
			}
		},
	    error: function (xhr,st,err){
			handleGenericError(xhr,st,err);
		}
	}); 
}

function handleLookupEnableDisabled(){
	if($("#enableLookupDropdown").val() === 'true'){
		$("#lookUpTableName").prop("disabled", false);
		$("#lookUpTableColumnName").prop("disabled", false);
	}else{
		$("#lookUpTableName").prop("disabled", true);
		$("#lookUpTableColumnName").prop("disabled", true);
		$("#lookUpTableName").val("");
		$("#lookUpTableColumnName").val("");
	}
}

function handleRegexEnableDisabled(){
	if($("#enableRegex").val() === 'true'){
		$("#regExExpression").prop("disabled", false);
		$("#destinationField").prop("disabled", false);
	}else{
		$("#regExExpression").prop("disabled", true);
		$("#destinationField").prop("disabled", true);
	}
}

/*Function will display add group attribute pop-up */
/* Working Fine HD */
function openAddGroupingAttributePopup(counter){
	clearAllMessagesPopUp();
	resetWarningDisplayPopUp();
	$("#pathlist_id").val($("#"+counter+"_consListId").val());
	$("#current_block_count").val(counter);
	$("#mappingId").val('0');
	$("#composerPluginId").val('0');
	$("#add_group_buttons_div").show();
	$("#update_group_buttons_div").hide();
	$("#view_group_buttons_div").hide();
	$("#view_progress-bar-div").hide();
	$("#add_group_header_label").show();
	$("#update_group_header_label").hide();
	$('#enableRegex').val('false');
	$('#regExExpression').val('');
	$('#enableLookupDropdown').val('false');
	$('#lookUpTableName').val('');
	$('#lookUpTableColumnName').val('');
	
	$('#destinationField').val('');
	$('#groupingField').val('');
	handleRegexEnableDisabled();
	handleLookupEnableDisabled();
	$("#add_grouping_attribute_link").click();
}

/* Newly Added HD */
function openAddConsolidationAttributePopup(counter){
	clearAllMessagesPopUp();
	resetWarningDisplayPopUp();
	$("#pathlist_id1").val($("#"+counter+"_consListId").val());
	$("#current_block_count1").val(counter);
	$("#mappingId1").val('0');
	$("#composerPluginId1").val('0');
	$("#add_cons_buttons_div").show();
	$("#update_cons_buttons_div").hide();
	$("#view_cons_buttons_div").hide();
	$("#view_progress-bar-div1").hide();
	$("#add_cons_header_label").show();
	$("#update_cons_header_label").hide();
	$('#fieldName').val('');
	$("#add_consolidation_attribute_link").click();
}


/*Function will add new composer plugin details HD */
function addGroupingAttributeDetails(){
	resetWarningDisplay();
	clearAllMessages();
	$("#view_progress-bar-div").show();
	$("#add_plugin_buttons_div").hide();
	var counter = $('#current_block_count').val();
	$.ajax({
		url: 'createConsolidationGroupAttributeList',
		cache: false,
		async: true,
		dataType: 'json',
		type: "POST",
		data:
		 {
			"consDefId"					:   $('#pathlist_id').val(),
			"groupingField"				:   $('#groupingField').val(),
			"regExEnable" 				:	$('#enableRegex').val(),
			"regExExpression"      		:	$('#regExExpression').val(),
			"destinationField"			:   $('#destinationField').val(),
			"lookUpEnable"				:   $('#enableLookupDropdown').val(),
			"lookUpTableName"			: 	$('#lookUpTableName').val(),
			"lookUpTableColumnName"		:   $('#lookUpTableColumnName').val()
		}, 
		
		success: function(data){
			hideProgressBar(counter);
			var response = data;
			var responseCode = response.code; 
			var responseMsg = response.msg; 
			var responseObject = response.object;
			if(responseCode === "200"){
				resetWarningDisplay();
				clearAllMessages();
				showSuccessMsg(responseMsg);
				
				var rowData = {};
			 	    rowData.id							= responseObject.id;
	 				rowData.groupingField				= responseObject.groupingField;
	 				rowData.regExEnable					= responseObject.regExEnable;
	 				rowData.regExExpression				= responseObject.regExExpression;
	 				rowData.destinationField			= responseObject.destinationField;
	 				rowData.enableLookup				= responseObject.lookUpEnable;
	 				rowData.lookUpTableName				= responseObject.lookUpTableName;
	 				rowData.lookUpTableColumnName		= responseObject.lookUpTableColumnName;
	 				rowData.edit                        = '<a href="#" id="editGroupingAttribute_'+responseObject.groupingField+'" onclick="displayGroupEditPopup('+parseInt(responseObject.id)+','+counter+')"><i class="fa fa-pencil-square-o" aria-hidden="true" id="att_img_'+parseInt(responseObject.id)+'" data-toggle="tooltip" data-placement="bottom" style="cursor:pointer;"></i></a>';
	 				
 				jQuery("#"+counter+"_group_grid").jqGrid('addRowData',rowData.id,rowData);
 				closeFancyBox();
				$("#add_plugin_buttons_div").hide();
				$("#view_plugin_buttons_div").show();
				$("#view_progress-bar-div").hide();
				closeFancyBox();
			}else if(responseObject !== undefined && responseObject !== 'undefined' && responseCode === "400"){
				$("#add_plugin_buttons_div").show();
				$("#view_plugin_buttons_div").hide();
				$("#view_progress-bar-div").hide();
				addErrorIconAndMsgForAjaxPopUp(responseObject); 
			}else{
				$("#add_plugin_buttons_div").show();
				$("#view_plugin_buttons_div").hide();
				$("#view_progress-bar-div").hide();				
				resetWarningDisplay();
				clearAllMessages();
				showErrorMsgPopUp(responseMsg);
			}
		},
	    error: function (xhr,st,err){
	    	hideProgressBar(counter);
			handleGenericError(xhr,st,err);
		}
	});
}

/* HD */
function addConsolidationAttributeDetails(){
	resetWarningDisplay();
	clearAllMessages();
	$("#view_progress-bar-div1").show();
	$("#add_plugin_buttons_div").hide();
	var counter = $('#current_block_count1').val();
	
	$.ajax({
		url: 'createConsolidationAttributeList',
		cache: false,
		async: true,
		dataType: 'json',
		type: "POST",
		data:
		 {
			"consDefId"				:   $('#pathlist_id1').val(),
			"fieldName"				:   $('#fieldName').val(),
			"dataType" 				:	$('#consolidationAttributeDataTypeField').val(),
			"operation"      		:	$('#consolidationAttributeOperationTypeField').val(),
			"description"			:   $('#consolidationLookUpTableColumnName').val()
		},
		
		success: function(data){
			hideProgressBar(counter);

			var response = data;
			
			var responseCode = response.code; 
			var responseMsg = response.msg; 
			var responseObject = response.object;
			
			if(responseCode === "200"){
				closeFancyBox();
				resetWarningDisplay();
				clearAllMessages();
				showSuccessMsg(responseMsg);
			
				var rowData = {};
				rowData.id						= responseObject.id;
 				rowData.fieldName				= responseObject.fieldName;
 				rowData.dataType				= responseObject.dataType;
 				rowData.operation				= responseObject.operation;
 				rowData.description				= responseObject.description;
 				rowData.edit                    = '<a href="#" id="editConsolidationAttribute_'+responseObject.fieldName+'" onclick="displayAttributeEditPopup('+parseInt(responseObject.id)+','+counter+')"><i class="fa fa-pencil-square-o" aria-hidden="true" id="att_img_'+parseInt(responseObject.id)+'" data-toggle="tooltip" data-placement="bottom" style="cursor:pointer;"></i></a>';
 			 				
 				$("#"+counter+"_cons_grid").jqGrid('addRowData', rowData.id, rowData);
 				closeFancyBox();
				$("#add_plugin_buttons_div").hide();
				$("#view_plugin_buttons_div").show();
				$("#view_progress-bar-div1").hide();
				
			}else if(responseObject !== undefined && responseObject !== 'undefined' && responseCode === "400"){
				$("#update_plugin_buttons_div").show();
				$("#view_plugin_buttons_div").hide();
				$("#view_progress-bar-div1").hide();
				addErrorIconAndMsgForAjaxPopUp(responseObject); 
			}else{
				$("#update_plugin_buttons_div").show();
				$("#view_plugin_buttons_div").hide();
				$("#view_progress-bar-div1").hide();				
				
				resetWarningDisplay();
				clearAllMessages();
				showErrorMsgPopUp(responseMsg);
			}
		},
		
		error: function (xhr,st,err){
	    	hideProgressBar(counter);
			handleGenericError(xhr,st,err);
		}
	});	
		
}
/*Function will open edit plugin pop-up HD */
function displayGroupEditPopup(rowId,counter){
	clearAllMessagesPopUp();
	resetWarningDisplayPopUp();
	
	$("#pathlist_id").val(rowId);
	$("#current_block_count").val(counter); 
	$("#mappingId").val('0');
	$("#composerPluginId").val('0');
	
	$("#add_group_buttons_div").hide();
	$("#update_group_buttons_div").show();
	$("#view_group_buttons_div").hide();
	$("#view_progress-bar-div").hide();
	$("#add_group_header_label").hide(); 		// Hide add header label
	$("#update_group_header_label").show();	// Show update header label
	var rowData = jQuery("#"+counter+"_group_grid").jqGrid ('getRowData', rowId);
	$('#groupingField').val(rowData.groupingField);
	$('#enableRegex').val(rowData.regExEnable);
	$('#regExExpression').val(rowData.regExExpression);
	$('#destinationField').val(rowData.destinationField);
	$('#enableLookupDropdown').val(rowData.enableLookup);
	$('#lookUpTableName').val(rowData.lookUpTableName);
	$('#lookUpTableColumnName').val(rowData.lookUpTableColumnName);
	handleRegexEnableDisabled();
	handleLookupEnableDisabled();
	$("#add_grouping_attribute_link").click();
}

/*Function will open edit plugin pop-up HD */
function displayAttributeEditPopup(rowId,counter){
	clearAllMessagesPopUp();
	resetWarningDisplayPopUp();
	
	$("#pathlist_id1").val(rowId);
	$("#current_block_count1").val(counter); 
	$("#mappingId1").val('0');
	$("#composerPluginId1").val('0');
	
	$("#add_cons_buttons_div").hide();
	$("#update_cons_buttons_div").show();
	$("#view_cons_buttons_div").hide();
	$("#view_progress-bar-div1").hide();
	$("#add_cons_header_label").hide(); 		// Hide add header label
	$("#update_cons_header_label").show();	// Show update header label
	 
	var rowData = jQuery("#"+counter+"_cons_grid").jqGrid ('getRowData', rowId);
	
	$('#fieldName').val(rowData.fieldName);
	$('#consolidationAttributeDataTypeField').val(rowData.dataType);
	$('#consolidationAttributeOperationTypeField').val(rowData.operation);
	$('#consolidationLookUpTableColumnName').val(rowData.description);
	$("#add_consolidation_attribute_link").click();
}



/*Function will update composer HD plugin details */
function updateGroupingAttributeDetails(){
	$("#view_progress-bar-div").show();
	$("#add_plugin_buttons_div").hide();
	var counter = $('#current_block_count').val();
	$.ajax({
		url: 'updateConsolidationGroupAttributeList',
		cache: false,
		async: true,
		dataType: 'json',
		type: "POST",
		data:
		 {
			"consDefId"					:   $("#"+counter+"_consListId").val(),
			"id"						:   $("#pathlist_id").val(),
			"groupingField"				:   $('#groupingField').val(),
			"regExEnable" 				:	$('#enableRegex').val(),
			"regExExpression"      		:	$('#regExExpression').val(),
			"destinationField"			:   $('#destinationField').val(),
			"lookUpEnable"				:   $('#enableLookupDropdown').val(),
			"lookUpTableName"			: 	$('#lookUpTableName').val(),
			"lookUpTableColumnName"		:   $('#lookUpTableColumnName').val()
		}, 
		
		success: function(data){
			hideProgressBar(counter);
			var response = data;
			var responseCode = response.code; 
			var responseMsg = response.msg; 
			var responseObject = response.object;
			if(responseCode === "200"){
				resetWarningDisplay();
				clearAllMessages();
				showSuccessMsg(responseMsg);
			
				var rowData = {};
				rowData.id						    = parseInt(responseObject.id);
 				rowData.groupingField				= responseObject.groupingField;
 				rowData.regExEnable					= responseObject.regExEnable;
 				rowData.regExExpression				= responseObject.regExExpression;
 				rowData.destinationField            = responseObject.destinationField;
 				rowData.enableLookup			    = responseObject.lookUpEnable;
 				rowData.lookUpTableName		 	    = responseObject.lookUpTableName;
 				rowData.lookUpTableColumnName		= responseObject.lookUpTableColumnName;
 			 				
 				$("#"+counter+"_group_grid").jqGrid('setRowData', rowData.id, rowData);
 				
 				closeFancyBox();
				$("#add_plugin_buttons_div").hide();
				$("#view_plugin_buttons_div").show();
				$("#view_progress-bar-div").hide();
 				
			}else if(responseObject !== undefined && responseObject !== 'undefined' && responseCode === "400"){
				$("#update_plugin_buttons_div").show();
				$("#view_plugin_buttons_div").hide();
				$("#view_progress-bar-div").hide();
				addErrorIconAndMsgForAjaxPopUp(responseObject); 
			}else{
				$("#update_plugin_buttons_div").show();
				$("#view_plugin_buttons_div").hide();
				$("#view_progress-bar-div").hide();				
				
				resetWarningDisplay();
				clearAllMessages();
				showErrorMsgPopUp(responseMsg);
			}
		},
	    error: function (xhr,st,err){
	    	hideProgressBar(counter);
			handleGenericError(xhr,st,err);
		}
	});
	
}


/*Function will update composer HD plugin details */
function updateConsolidationAttributeDetails(){
	$("#view_progress-bar-div1").show();
	$("#add_plugin_buttons_div").hide();
	var counter = $('#current_block_count1').val();
	
	
	$.ajax({
		url: 'updateConsolidationAttributeList',
		cache: false,
		async: true,
		dataType: 'json',
		type: "POST",
		data:
		 {
			"consDefId"				:   $("#"+counter+"_consListId").val(),
			"id"					:   $("#pathlist_id1").val(),
			"fieldName"				:   $('#fieldName').val(),
			"dataType" 				:	$('#consolidationAttributeDataTypeField').val(),
			"operation"      		:	$('#consolidationAttributeOperationTypeField').val(),
			"description"			:   $('#consolidationLookUpTableColumnName').val()
		}, 
		
		success: function(data){
			hideProgressBar(counter);
			var response = data;
			var responseCode = response.code; 
			var responseMsg = response.msg; 
			var responseObject = response.object;
			
			if(responseCode === "200"){
				resetWarningDisplay();
				clearAllMessages();
				showSuccessMsg(responseMsg);
			
				var rowData = {};
				rowData.id						= parseInt(responseObject.id);
 				rowData.fieldName				= responseObject.fieldName;
 				rowData.dataType				= responseObject.dataType;
 				rowData.operation				= responseObject.operation;
 				rowData.description            	= responseObject.description;
 			 				
 				$("#"+counter+"_cons_grid").jqGrid('setRowData', rowData.id, rowData);
 				
 				closeFancyBox();
				$("#view_progress-bar-div1").hide();
 				
			}else if(responseObject !== undefined && responseObject !== 'undefined' && responseCode === "400"){
				$("#view_progress-bar-div1").hide();
				addErrorIconAndMsgForAjaxPopUp(responseObject); 
			}else{
				$("#view_progress-bar-div1").hide();				
				
				resetWarningDisplay();
				clearAllMessages();
				showErrorMsgPopUp(responseMsg);
			}
		},
	    error: function (xhr,st,err){
	    	hideProgressBar(counter);
			handleGenericError(xhr,st,err);
		}
	});
	
}

function redirectComposerConfiguration(plugInId,plugInName,plugInType){
	
	$('#plugInId').val(plugInId);
	$('#plugInName').val(plugInName);
	$('#plugInType').val(plugInType);
	$('#distribution-service-composer-config').submit();
}

/*Function will display delete composer plugin details popup HD */
function displayDeletePluginPopup(counter){
		clearAllMessagesPopUp();
		resetWarningDisplay();
		//var myGrid = $("#"+counter+"_group_grid");
		//var selectedRowId = myGrid.jqGrid('getGridParam', 'selarrrow');
		var childCheckboxName = counter+"_groupingAttributeCheckbox"
		var selectedAttributeCheckboxes = [];
	    $.each($("input[name='"+childCheckboxName+"']:checked"), function(){            
	    	selectedAttributeCheckboxes.push($(this).val());
	    });
	    var selectedRowId = "";
	    if(selectedAttributeCheckboxes.length > 0) {
	    	selectedRowId = selectedAttributeCheckboxes[0];
	    }
		
		if(selectedRowId != null && selectedRowId != ''){
			$('#deleteBlockId').val(counter);
			$('#deletepathListIdG').val(counter);
			$('#pathlistMessageG').click();
		}
}

/*Function will delete composer plugin details HD */
function deleteComposerPlugin(){
	var blockId = $('#deletepathListIdG').val();
	
	var childCheckboxName = blockId+"_groupingAttributeCheckbox";
	var selectedAttributeCheckboxes = [];
    $.each($("input[name='"+childCheckboxName+"']:checked"), function(){            
    	selectedAttributeCheckboxes.push($(this).val());
    });
    var rowString = selectedAttributeCheckboxes.join(",");
    
    var rowIds = new Array();
    rowIds = rowString.split(",");
    
    $.ajax({
		url: 'deleteConsolidationGroupAttributeList',
		cache: false,
		async: true,
		dataType: 'json',
		type: "POST",
		data:
		{
			"composerIdList" :  rowString
		}, 
		success: function(data){
			var response = data;
			var responseCode = response.code; 
			var responseMsg = response.msg; 
			var responseObject = response.object;
			
			if(responseCode === "200"){
				resetWarningDisplay();
				clearAllMessages();
				showSuccessMsg(responseMsg);
				for(var i=0;i<rowIds.length;i++){
					var rowid=rowIds[i];
					$("#"+blockId+"_group_grid").jqGrid('delRowData',rowid);
					
				}
				$("#"+blockId+"_selectAllGroupingAttribute").prop('checked',false);
				closeFancyBox();
			}else if(responseObject !== undefined && responseObject !== 'undefined' && responseCode === "400"){
				addErrorIconAndMsgForAjaxPopUp(responseObject); 
			}else{
				resetWarningDisplay();
				clearAllMessages();
				showErrorMsgPopUp(responseMsg);
			}
		},
	    error: function (xhr,st,err){
			handleGenericError(xhr,st,err);
		}
	});
}

/*Function will display delete composer plugin details popup HD */
function displayDeleteAttributePopup(counter){
		clearAllMessagesPopUp();
		resetWarningDisplay();
		//var myGrid = $("#"+counter+"_cons_grid");
		//var selectedRowId = myGrid.jqGrid('getGridParam', 'selarrrow');
		
		var childCheckboxName = counter+"_consolidationAttributeCheckbox";
		var selectedAttributeCheckboxes = [];
	    $.each($("input[name='"+childCheckboxName+"']:checked"), function(){            
	    	selectedAttributeCheckboxes.push($(this).val());
	    });
	    var selectedRowId = "";
	    if(selectedAttributeCheckboxes.length > 0) {
	    	selectedRowId = selectedAttributeCheckboxes[0];
	    }
	    
		if(selectedRowId != null && selectedRowId != ''){
			$('#deleteBlockId').val(counter);
			$('#deletepathListIdA').val(counter);
			$('#pathlistMessageA').click();
		}
}

/*Function will delete composer plugin details HD */
function deleteAttribute(){
	var blockId = $('#deletepathListIdA').val();
    
	var childCheckboxName = blockId+"_consolidationAttributeCheckbox";
	var selectedAttributeCheckboxes = [];
    $.each($("input[name='"+childCheckboxName+"']:checked"), function(){            
    	selectedAttributeCheckboxes.push($(this).val());
    });
    var rowString = selectedAttributeCheckboxes.join(",");
    
    var rowIds = new Array();
    rowIds = rowString.split(",");
    
    $.ajax({
		url: 'deleteConsolidationAttributeList',
		cache: false,
		async: true,
		dataType: 'json',
		type: "POST",
		data:
		{
			"composerIdList" :  rowString
		}, 
		success: function(data){
			var response = data;
			var responseCode = response.code; 
			var responseMsg = response.msg; 
			var responseObject = response.object;
			
			if(responseCode === "200"){
				resetWarningDisplay();
				clearAllMessages();
				showSuccessMsg(responseMsg);
				for(var i=0;i<rowIds.length;i++){
					var rowid=rowIds[i];
					$("#"+blockId+"_cons_grid").jqGrid('delRowData',rowid);
					
				}
				$("#"+blockId+"_selectAllConsolidationAttribute").prop('checked',false);
				closeFancyBox();
			}else if(responseObject !== undefined && responseObject !== 'undefined' && responseCode === "400"){
				addErrorIconAndMsgForAjaxPopUp(responseObject); 
			}else{
				resetWarningDisplay();
				clearAllMessages();
				showErrorMsgPopUp(responseMsg);
			}
		},
	    error: function (xhr,st,err){
			handleGenericError(xhr,st,err);
		}
	});
}


/*Function will display detailed parameter list in the grid. */
function showDetailedParameters(rowId, counter){
	clearAllMessagesPopUp();
	resetWarningDisplay();
	
	var rowData = jQuery("#"+counter+"_grid").jqGrid ('getRowData', rowId);
	
	$('#view_fileExtension').val(rowData.fileExtension);
	$('#view_writeFilenamePrefix').val(rowData.writeFilenamePrefix);
	$('#view_writeFilenameSuffix').val(rowData.writeFilenameSuffix);
	$('#view_destPath').val(rowData.destPath);
	$('#view_fileBackupPath').val(rowData.fileBackupPath);
	$('#view_fileExtension').val(rowData.fileExtension);
	$('#additional_param_link').click();
	
}

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
					"driverType.alias" : $('#' + counter + '_selDriverType')
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
						$('#' + counter + '_selDriverType').prop("disabled",
								true);
						$('#' + counter + '_driverId')
								.val(responseObject["id"]);
						$('#' + counter + '_driverAlias').val(
								$('#' + counter + '_selDriverType').find(
										":selected").val());
						$("#" + counter + "_block").collapse("toggle");

						var driver = {
							'id' : responseObject["id"],
							'type' : $('#' + counter + '_selDriverType').find(
									":selected").val(),
							'alias' : $('#' + counter + '_selDriverType').find(
									":selected").val(),
							'name' : responseObject["name"],
							'order' : responseObject["applicationOrder"],
							'timeout' : $('#' + counter + '_timeout').val(),
							'status' : $('#' + counter + '_status').val()
						};

						distributionDriverListDetail.push(driver);

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
	var distributionDriverType = $('#' + driverListCounter + '_selDriverType')
			.find(":selected").val();

	if (distributionDriverType === jsSpringMsg.ftpDistributionDriver) {
		urlAction = jsSpringMsg.updateFTPDriver;
	} else if (distributionDriverType === jsSpringMsg.sftpDistributionDriver) {
		urlAction = jsSpringMsg.updateSFTPDriver;
	} else if (distributionDriverType === jsSpringMsg.localDistributionDriver) {
		urlAction = jsSpringMsg.updateLocalDriver;
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
									'#' + driverListCounter + '_selDriverType')
									.find(":selected").val(),
							'alias' : $(
									'#' + driverListCounter + '_selDriverType')
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
							&& (responseCode === "400" || responseCode === "133")) {
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

/* */

function displayApplicationOrderPopup() {

	$('#draggableDriverDiv').html('');

	for (var index = 0; index < distributionDriverListDetail.length; index++) {

		var tabstring = "<li style='cursor:pointer;border:1px solid #ddd;margin-bottom:5px;' id="
				+ distributionDriverListDetail[index].id + ">";

		tabstring += "<table class='table table-hover' style='margin-bottom:10px;border-top:0px;'>";
		tabstring += "<tr id=" + distributionDriverListDetail[index].id + ">";
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
function changeDriverOrder(driverId, order) {
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


function resetNumbering() {
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


/* Function will reset pathlist parameter for selected block */
function resetPathlistParams(counter) {
	resetWarningDisplay();
	clearAllMessages();
	$('#' + counter + '_block input').val('');
}

/* Function will create new distribution pathlist */
function addConsolidationPathlist(counter) {
	resetWarningDisplay();
	clearAllMessages();
	showProgressBar(counter);

	$('#title_' + counter).html($("#" + counter + "_name").val());

	$('#' + counter + '_grid_links_div').show();
	$('#' + counter + '_grid_main_div').show();

	loadConsolidationGrid(counter, []);

	$('#' + counter + '_updatebtn').show();
	$('#' + counter + '_savebtn').hide();
	
	hideProgressBar(counter);
	
	/*
	var maxFileCountAlert = $("#" + counter + "_maxCountAlert").val();

	if (maxFileCountAlert === null || maxFileCountAlert === ''
			|| maxFileCountAlert === 'undefined ') {
		maxFileCountAlert = -1;
		$("#" + counter + "_maxCountAlert").val(maxFileCountAlert);
	}

	$
			.ajax({
				url : jsSpringMsg.addPathlistAction,
				cache : false,
				async : true,
				dataType : 'json',
				type : "POST",
				data : {
					"id" : '0',
					"name" : $("#" + counter + "_name").val(),
					"compressedInput" : $("#" + counter + "_compressedInput").val(),
					"maxCountAlert" : maxFileCountAlert,
					"readFilePrefix" : $("#" + counter + "_readFilePrefix").val(),
					"readFileSuffix" : $("#" + counter + "_readFileSuffix").val(),
					"driver.id" : $("#" + counter + "_driverId").val(),
					"pathListCount" : counter,

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

						loadConsolidationGrid(counter, []);

						$('#' + counter + '_updatebtn').show();
						$('#' + counter + '_savebtn').hide();
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
			});*/
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
function deleteConsolidationPathList() {
	resetWarningDisplay();
	clearAllMessages();
	$
			.ajax({
				url : 'deleteConsolidationPathList',
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


/* Function will add new composer plugin details */

/* Function will open edit plugin pop-up */
function displayPluginEditPopup(actionType, rowId, counter) {
	clearAllMessagesPopUp();
	resetWarningDisplayPopUp();

	$("#pathlist_id").val($("#" + counter + "_pathListId").val());
	$("#current_block_count").val(counter);
	$("#add_plugin_buttons_div").hide();
	$("#update_plugin_buttons_div").show();

	$("#view_plugin_buttons_div").hide();
	$("#view_progress-bar-div").hide();
	$("#add_plugin_header_label").hide(); // Hide add header label
	$("#update_plugin_header_label").show(); // Show update header label

	var rowData = jQuery("#" + counter + "_grid").jqGrid('getRowData', rowId);
	
	$('#destPath').val(rowData.destPath);
	$('#writeFilenameSuffix').val(rowData.writeFilenameSuffix);
	$('#writeFilenamePrefix').val(rowData.writeFilenamePrefix);
	$('#fileBackupPath').val(rowData.fileBackupPath);
	$('#fileExtension').val(rowData.fileExtension);
	$('#compressOutFileEnabled').val(rowData.compressOutFileEnabled);
	$('#composerPluginType').val(rowData.composerTypeId);
	$('#composerPluginType').attr('disabled', true);

	$("#add_consolidation_grid_link").click();
}

/* Function will update composer plugin details */
function updateConsolidationDefinitionDetails() {
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
						rowData.name = '<a href="#" class="link" onclick="redirectComposerConfiguration('
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
	if (padingLength === '' || padingLength === null) {
		padingLength = 0;
	}

	var startIndex = $('#' + counter + '_startIndex').val();
	if (startIndex === '' || startIndex === null) {
		startIndex = 0;
	}

	var endIndex = $('#' + counter + '_endIndex').val();
	if (endIndex === '' || endIndex === null) {
		endIndex = 0;
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
					"defaultValue" : $('#' + counter + '_defaultValue').val(),
					"length" : padingLength,
					"composer.id" : $("#composerPluginId").val(),
					"blockCount" : counter

				},

				success : function(data) {
					$('#' + counter + '_char_buttons-div').show();
					$('#' + counter + '_char_progress-bar-div').hide();

					var response = data;

					var responseCode = response.code;
					var responseMsg = response.msg;
					var responseObject = response.object;

					if (responseCode === "200") {
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
						$('#' + counter + '_length').val(responseObject.length);
						$('#' + counter + '_defaultValue').val(
								responseObject.defaultValue);
						$('#' + counter + '_paddingType').val(
								responseObject.paddingType.toString());
						$('#' + counter + '_startIndex').val(
								responseObject.startIndex);
						$('#' + counter + '_endIndex').val(
								responseObject.endIndex);

					} else if (responseObject !== undefined
							&& responseObject !== 'undefined'
							&& responseCode === "400") {
						$('#' + counter + '_char_buttons-div').show();
						$('#' + counter + '_char_progress-bar-div').hide();
						addErrorIconAndMsgForAjax(responseObject);
					} else {
						$('#' + counter + '_char_buttons-div').show();
						$('#' + counter + '_char_progress-bar-div').hide();
						clearAllMessagesPopUp();
						resetWarningDisplayPopUp();
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
					"startIndex" : $('#' + counter + '_startIndex').val(),
					"endIndex" : $('#' + counter + '_endIndex').val(),
					"paddingType" : $('#' + counter + '_paddingType').val(),
					"defaultValue" : $('#' + counter + '_defaultValue').val(),
					"length" : $('#' + counter + '_length').val(),
					"composer.id" : $("#composerPluginId").val(),
					"blockCount" : counter
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
						$('#' + counter + '_startIndex').val(
								responseObject.startIndex);
						$('#' + counter + '_endIndex').val(
								responseObject.endIndex);

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
								char.endIndex, char.paddingType,
								char.defaultValue, char.length, 'UPDATE');
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



/* Function will delete composer plugin details */

/*Function will display detailed parameter list in the grid. */
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
	$('#additional_param_link').click();

}

/*Function will display character rename operation parameters details. */
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
								responseObject["defaultValue"],
								responseObject["length"], 'UPDATE');
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

/*Function will reset all form parameters  */
function resetCharRenameParameters(counter) {

	$("#" + counter + "_query").val('');
	$("#" + counter + "_startIndex").val('');
	$("#" + counter + "_endIndex").val('');
	$("#" + counter + "_length").val('');

}
