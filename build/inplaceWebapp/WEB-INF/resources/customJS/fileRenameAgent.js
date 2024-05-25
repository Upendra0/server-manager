function updateFileRenameAgentDetails(){
		
		resetWarningDisplay();
		clearAllMessages();
		$("#fileRenameButtonDIv").hide();
		showProgressBar();
		
		$.ajax({
			url: 'updateFileRenameAgentDetails',
			cache: false,
			async: true, 
			dataType: 'json',
			type: "POST",
			data: 
			{
				"initialDelay" 				:   $("#initialDelay").val(),
				"executionInterval" 		:	$("#executionInterval").val(),
				"id"						:	$("#fileRenameAgentId").val(),
				"serverInstance.id"			:   $("#server_Instance_Id").val(),	
			},
			success: function(data){
				hideProgressBar();
				var response = eval(data);
				var responseCode = response.code; 
				var responseMsg = response.msg; 
				var responseObject = response.object;
				 if(responseObject.id != null && responseObject.id !== ''){
					 $("#agentserverInstanceId").val(responseObject.id);
				 }
				if(responseCode == "200"){
					
					$("#fileRenameButtonDIv").show();
					showSuccessMsg(responseMsg);
					
				}else if(responseObject != undefined && responseObject != 'undefined' && responseCode == "400"){
					$("#fileRenameButtonDIv").show();
					addErrorIconAndMsgForAjax(responseObject);
				}else{
					resetWarningDisplayPopUp();
					$("#fileRenameButtonDIv").show();
					showErrorMsg(responseMsg);
				}
			},
		    error: function (xhr,st,err){
		    	hideProgressBar();
		    	$("#fileRenameButtonDIv").show();
				handleGenericError(xhr,st,err);
			}
		});
	}


function addServiceToFileRenameAgent(){
	
	clearAllMessagesPopUp();
	resetWarningDisplayPopUp();
	clearAllMessages();
	resetWarningDisplay();
	
	var serverInstanceId = $('#agentserverInstanceId').val();
	$.ajax({
		url	  : 'loadDropDownServiceList',
		cache : false,
		async : true,
		dataType : 'json',
		type : "POST",
		data : {
			"agent_server_Instance_Id" 	:  serverInstanceId,
		},

		success: function(data){
			var response = eval(data);
			var responseCode = response.code; 
			var responseMsg = response.msg; 
			var responseObject = response.object;
						
			if(responseCode == "200"){
				
				$("#service").prop('disabled', false);
				$("#addServiceToFileRenameAgent").click();
				$("#divAddServiceToFileRenameAgent").show();
				$("#add_plugin_buttons_div").show();
				$("#add_plugin_header_label").show();
				$("#update_plugin_buttons_div").hide();
				$("#update_plugin_header_label").hide();
				
				$('#service').empty();				
 				$.each(responseObject, function(key, value) {
					$('#service').append("<option value='"+key+"'>"+value+"</option>");
					$('#fileExtensitonList').val('');
					$('#extAfterRename').val('');
					$('#destinationPath').val('');					
				 });  
				
			}else if(responseObject != undefined || responseObject != 'undefined' || responseCode == "400"){
				
			//	$("#serviceConfigMessage").click();
				showErrorMsg(responseMsg);
				//$("#addServiceToFileRenameAgent").click();
				
				//$("#moreWarn").show();
				
			}else{
				showErrorMsg(responseMsg);	
			}
		},
		
		error : function(xhr, st, err) {
			hideProgressBar(counter);
			handleGenericError(xhr, st, err);
		}
	});
	
} 

function updateServiceConfigurationPopup(serviceFileRenameConfigId,serviceId,serviceName,extAfterRename,destinationPath,fileExtensitonList,charRenameOperationEnable){
	
	clearAllMessagesPopUp();
	resetWarningDisplayPopUp();
	clearAllMessages();
	resetWarningDisplay();

	$('#fileExtensitonList').val(fileExtensitonList);
	$('#extAfterRename').val(extAfterRename),
	$('#destinationPath').val(decodeURIComponent(destinationPath)),
	$('#serviceFileRenameConfigId').val(serviceFileRenameConfigId),
 	$('#service').append( new Option(serviceName,serviceId,true,true) ); 

	var charEnable = "False";
	if(charRenameOperationEnable == 'true'){
		charEnable = "True";
	}
 	$('#charRenameOperationEnable').val(charEnable);
	$("#service").prop('disabled', true); 
	$("#addServiceToFileRenameAgent").click();
	$("#divAddServiceToFileRenameAgent").show();
	$("#update_plugin_buttons_div").show();
	$("#update_plugin_header_label").show();
	$("#add_plugin_buttons_div").hide();
	$("#add_plugin_header_label").hide();	
	
}

function updateServiceConfiguration() {	
	
	clearAllMessagesPopUp();
	resetWarningDisplayPopUp();
	clearAllMessages();
	resetWarningDisplay();
	$("#view_progress-bar-div").show();
	$("#update_plugin_buttons_div").hide();

	$.ajax({
		url	  : 'updateServiceToFileRenamingAgent',
		cache : false,
		async : true,
		dataType : 'json',
		type : "POST",
		data : {
			
			"service.serverInstance.id" : 	$('#server_Instance_Id').val(),			
			"agent.serverInstance.id" 	: 	$('#server_Instance_Id').val(),
			"agent.id" 					: 	$('#fileRenameAgentId').val(),
			"service.id" 				:   $('#service option:selected').val(),
			"charRenameOperationEnable" :   $('#charRenameOperationEnable option:selected').val(),
			"fileExtensitonList" 		: 	$('#fileExtensitonList').val(),
			"extAfterRename"  			: 	$('#extAfterRename').val(),
			"destinationPath" 			: 	$('#destinationPath').val(),
			"id" 						: 	$('#serviceFileRenameConfigId').val(),

		},

		success : function(data) {
			var response = data;

			var responseCode = response.code;
			var responseMsg = response.msg;
			var responseObject = response.object;

			if (responseCode === "200") {

				resetWarningDisplay();
				clearAllMessages();
				$("#view_progress-bar-div").hide();
				closeFancyBox();
				showSuccessMsg(responseMsg);
		
				reloadFileRenameAgentGridData();

			} else if (responseObject !== undefined && responseObject !== 'undefined' && responseCode === "400") {
				
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

function addServiceConfig() {

	clearAllMessagesPopUp();
	resetWarningDisplayPopUp();

	$("#view_progress-bar-div").show();
	$("#add_plugin_buttons_div").hide();
	
	$.ajax({
		url	  : 'addServiceToFileRenamingAgent',
		cache : false,
		async : true,
		dataType : 'json',
		type : "POST",
		data : {

			"agent.serverInstance.id" : 	$('#server_Instance_Id').val(),
			"service.serverInstance.id" : 	$('#server_Instance_Id').val(),			
			"agent.id" 					: 	$('#fileRenameAgentId').val(),
			"service.id" 				:   $('#service option:selected').val(),
			"charRenameOperationEnable" :   $('#charRenameOperationEnable option:selected').val(),
			"fileExtensitonList" 		: 	$('#fileExtensitonList').val(),
			"extAfterRename"  			: 	$('#extAfterRename').val(),
			"destinationPath" 			: 	$('#destinationPath').val(),

		},

		success : function(data) {
			var response = data;

			var responseCode = response.code;
			var responseMsg = response.msg;
			var responseObject = response.object;

			if (responseCode === "200") {

				resetWarningDisplay();
				clearAllMessages();
				$("#view_progress-bar-div").hide();
				closeFancyBox();
				showSuccessMsg(responseMsg);
				reloadFileRenameAgentGridData();

			} else if (responseObject !== undefined && responseObject !== 'undefined' && responseCode === "400") {
				
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

	

function clearServiceSummaryInstanceGrid(){}

function deletePopup(serviceFileRenameConfigId){	

	$("#serviceFileRenameConfigId").val(serviceFileRenameConfigId);
	$("#deleteFileRenameAgentService").click();
	$("#deleteFileRenameAgentServicePopup").show();
} 	 
	 
function deleteServiceFileRenameConfig() {	
	
	clearAllMessagesPopUp();
	resetWarningDisplayPopUp();
	clearAllMessages();
	resetWarningDisplay();
	
 	$("#delete-progress-bar-div").show(); 
	$("#delete-buttons-div").hide();
	
	var serviceFileRenameConfigId= $("#serviceFileRenameConfigId").val();

	$.ajax({
		url : 'deleteServiceFileRenameAgentConfig',
		cache : false,
		async : true,
		dataType : 'json',
		type : 'POST',
		data : 
		{
			"serviceFileRenameConfigId" : serviceFileRenameConfigId,
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
				closeFancyBox();
				reloadFileRenameAgentGridData();
				$("#delete-progress-bar-div").hide();
				
			} else if (responseObject !== undefined && responseObject !== 'undefined' && responseCode === "400") {
				showErrorMsg(responseMsg);
				$("#delete-progress-bar-div").hide();
			} else {
				showErrorMsg(responseMsg);			
				$("#delete-progress-bar-div").hide();
			}
		},
		error : function(xhr, st, err) {
			handleGenericError(xhr, st, err);
		}
	});
}

