
function initializeDnDServerAndServerInstanceComponents() {
	$("#server-components li").draggable({
		appendTo : "body",
		helper : "clone"
	});
	
	$("#serverDropArea").droppable({
		activeClass : "ui-state-default",
		hoverClass : "server-droppable-hover",
		accept : ":not(.ui-sortable-helper)",
		drop : function(event, ui) {
			var draggedText = $(ui.draggable).attr("id");
			if (draggedText.indexOf("Servers") >= 0) {
				$("#serverDetailsAnchor").click();
			}
		}
	});
}

// This are used under the child frame - addServerInstancePopup.jsp
var serverInstanceDragOnServerId = '';
var serverInstanceDragOnServerIPAddress = '';
var serverInstanceDragOnServerUtilityPort = '';

function assignServerInstanceDroppableEvent(droppableDivId, serverID,
		ipAddress, utilityPort) {
	$("#" + droppableDivId).droppable(
			{
				activeClass : "ui-state-default",
				hoverClass : "ui-state-hover-server-instance",
				accept : ":not(.ui-sortable-helper)",
				drop : function(event, ui) {
					var draggedText = $(ui.draggable).attr("id");
					if (draggedText.indexOf("Server_Instance") >= 0) {
						var divId = this.id;
						serverInstanceDragOnServerId = $("#" + divId).attr(
						'serverID');
						serverInstanceDragOnServerIPAddress = $("#" + divId)
						.attr('ipAddress');
						serverInstanceDragOnServerUtilityPort = $("#" + divId)
						.attr('serverutilityport');
						
						$('#serverId').val($("#" + divId).attr('serverID'));
						$('#serverHostIp')
						.val($("#" + divId).attr('ipAddress'));
						$('#utilityPort').val(
								$("#" + divId).attr('serverutilityport'));
						resetWarningDisplay();
						clearResponseMsgPopUp();
						$("#createServerInstanceContent").show();
						$("#divValidationList").html('');
						$("#createServerInstanceBtn").show();
						$("#cancelInstanceBtn").show();
						$("#closeInstanceBtn").hide();
						$("#addInstance").click();
						// hide import and copy div on server instance drop
						$('#divImport').hide();
						$('#divCopy').hide();
						$('#importFile').val('');
					}
				}
			});
	$("#" + droppableDivId).attr("ipAddress", ipAddress);
	$("#" + droppableDivId).attr("serverID", serverID);
	$("#" + droppableDivId).attr("serverUtilityPort", utilityPort);
}

/* Code should got the response message */

function addNewServerToDB(parentScreen) {
	resetWarningDisplay();
	showErrorMsg('');
	showProgressBar();
	parent.resizeFancyBox();
	$.ajax({
		url : 'addServer',
		cache : false,
		async : true,
		dataType : 'json',
		type : "POST",
		data : {
			"serverType.id" : $("#serverTypeId").val(),
			"serverId":$("#serverServerId").val(),
			"name" : $("#serverName").val(),
			"description" : $("#serverDescription").val(),
			"ipAddress" : $("#serverIPAddress").val(),
			"createdByStaffId" : parent.$("#loggedInStaffId").val(),
			"utilityPort" : $("#serverUtilityPort").val(),
			"groupServerId":$("#groupServerId").val()
			//"containerEnvironment" : $("#containerEnvironment").val()
		},
		success : function(data) {
			hideProgressBar();
			var response = eval(data);
			var moduleName=response.module;
			var responseCode = response.code;
			var responseMsg = response.msg;
			var responseObject = response.object;
			
			if (responseCode == "200") {
				
				parent.showSuccessMsg(responseMsg);
				
				if(parentScreen == 'migration') {
					parent.callbackCreateServer();
				} else {
					var serverType=$("#serverTypeId option:selected").text();
					parent.addNewServer($("#serverName").val(),
							responseObject["id"], $("#serverIPAddress").val(), $(
							"#serverUtilityPort").val(),serverType,
							parent.updateAccessStatus, parent.deleteAccessStatus);
				}
				parent.closeFancyBoxFromChildIFrame();
				if(moduleName=="LICENSE_WARNING"){
					$("#licenseMessage").click();
					return;
				}
				
			} else if (responseObject != undefined
					&& responseObject != 'undefined') {
				resetWarningDisplay();
				if (responseObject["serverId"] != undefined) {
					addErrorMsgForCreateServer("serverServerId",
							responseObject["serverId"]);
				}
				if (responseObject["groupServerId"] != undefined) {
					addErrorMsgForCreateServer("groupServerId",
							responseObject["groupServerId"]);
				}
				if (responseObject["name"] != undefined) {
					addErrorMsgForCreateServer("serverName",
							responseObject["name"]);
				}
				if (responseObject["description"] != undefined) {
					addErrorMsgForCreateServer("serverDescription",
							responseObject["description"]);
				}
				
				if (responseObject["ipAddress"] != undefined) {
					addErrorMsgForCreateServer("serverIPAddress",
							responseObject["ipAddress"]);
				}
				
				if (responseObject["utilityPort"] != undefined) {
					addErrorMsgForCreateServer("serverUtilityPort",
							responseObject["utilityPort"]);
				}
				addErrorClassWhenTitleExistPopUp($("#GENERIC_ERROR_MESSAGE",
						window.parent.document).val());
				parent.resizeFancyBox();
			} else {
				resetWarningDisplay();
				showErrorMsgPopUp(responseMsg);
				parent.resizeFancyBox();
			}
		},
		error : function(xhr, st, err) {
			hideProgressBar();
			handleGenericError(xhr, st, err);
		}
	});
}

function updateServerToDB() {
	
	var name = $("#updt-serverName").val();
	var ipAddr = $("#updt-serverIPAddress").val();
	var utility = $("#updt-serverUtilityPort").val();
	var serverId=$("#updt-serverServerId").val();
	var serverType=$("#serverTypeId option:selected").text();
	resetWarningDisplay();
	showErrorMsg('');
	showProgressBar();
	parent.resizeFancyBox();
	$.ajax({
		url : 'updateServer',
		cache : false,
		async : true,
		dataType : 'json',
		type : "POST",
		data : {
			"id" : $('#updt-id').val(),
			"serverType.id" : $("#updt-serverTypeId").val(),
			"serverId":serverId,
			"name" : name,
			"description" : $("#updt-serverDescription").val(),
			"ipAddress" : ipAddr,
			"createdByStaffId" : parent.$("#loggedInStaffId").val(),
			"utilityPort" : utility,
			"groupServerId":$("#updt-groupServerId").val()
			//"containerEnvironment" : $("#containerEnvironment").val()
		},
		success : function(data) {
			hideProgressBar();
			var response = eval(data);
			
			var responseCode = response.code;
			var responseMsg = response.msg;
			var responseObject = response.object;
			
			if (responseCode == "200") {
				// showSuccessMsg(responseMsg);
				var id = $('#updt-id').val();
				parent.$('#title_' + id).html(name + '-' + ipAddr + '-' +serverType);
				parent.$('#link_' + id).html('' + name + '');
				parent.$('#serverIP_' + id).html(
						'<strong>' + ipAddr + '</strong>');
				parent.$('#utilityPort_' + id).html(
						'<strong>' + utility + '</strong>');
				
				parent.$("#serverInstance_" + id + "_droppable").attr(
						"serverUtilityPort", utility);
				parent.$("#serverInstance_" + id + "_droppable").attr(
						"ipAddress", ipAddr);
				// showSuccessMsg(responseMsg);
				// //reloadGridData();
				// closeFancyBox();
				// reloadGridData();
				// parent.resizeFancyBox();
				//
				// $('#btnClose').show();
				// $('#btnCancel').hide();
				// $('#btnUpdate').hide();
				
				parent.closeFancyBoxFromChildIFrame();
				parent.showSuccessMsg(responseMsg);
				
			} else if (responseObject != undefined
					&& responseObject != 'undefined') {
				resetWarningDisplay();
				// alert(JSON.stringify(responseObject));
				if (responseObject["serverId"] != undefined) {
					addErrorMsgForCreateServer("updt-serverServerId",
							responseObject["serverId"]);
				}
				if (responseObject["groupServerId"] != undefined) {
					addErrorMsgForCreateServer("updt-groupServerId",
							responseObject["groupServerId"]);
				}
				if (responseObject["name"] != undefined) {
					addErrorMsgForCreateServer("updt-serverName",
							responseObject["name"]);
				}
				
				if (responseObject["description"] != undefined) {
					addErrorMsgForCreateServer("updt-serverDescription",
							responseObject["description"]);
				}
				
				if (responseObject["ipAddress"] != undefined) {
					addErrorMsgForCreateServer("updt-serverIPAddress",
							responseObject["ipAddress"]);
				}
				
				if (responseObject["utilityPort"] != undefined) {
					addErrorMsgForCreateServer("updt-serverUtilityPort",
							responseObject["utilityPort"]);
				}
				addErrorClassWhenTitleExist($("#GENERIC_ERROR_MESSAGE",
						window.parent.document).val());
				parent.resizeFancyBox();
			} else {
				resetWarningDisplay();
				showErrorMsg(responseMsg);
				parent.resizeFancyBox();
			}
		},
		error : function(xhr, st, err) {
			hideProgressBar();
			handleGenericError(xhr, st, err);
		}
	});
}

function hideServerDeleteBtnWhenServerInstanceExist() {
	$('table tr').each(
			function() {
				$(this).find('td').each(
						function() {
							// do your stuff, you can use $(this) to get current
							// cell
							var cellId = $(this).attr("id");
							
							/* serverInstance_SVR0002_droppable_td */
							if (cellId != undefined
									&& startsWith(cellId, 'serverInstance_')
									&& endsWith(cellId, '_droppable_td')
									&& $("#" + cellId).children().length > 1
									
							) {
								$("#" + cellId).parent().children().first()
								.children().first().children().first()
								.hide();
							}
						})
			});
}

function showProgressBar() {
	$("#buttons-div").hide();
	$("#progress-bar-div").show();
}

function hideProgressBar() {
	$("#buttons-div").show();
	$("#progress-bar-div").hide();
}

function addNewServerInstanceToDB() {
	resetWarningDisplay();
	clearResponseMsgPopUp();
	showErrorMsg('');
	parent.resizeFancyBox();
	
	var mode = $('input[name="createMode"]:checked').val();
	
	if (mode != undefined && mode == 'CREATE_MODE_IMPORT') {
		if ($('#importFile').val() == '') {
			showErrorMsgPopUp(jsLocaleMsg.importFileMiss);
			return;
		}
	}
	clearResponseMsgPopUp();
	$("#add-buttons-div").hide();
	$("#add-progress-bar-div").show();
	console.log("addNewServerInstanceToDB");
	$
	.ajax({
		url : 'addServerInstance',
		cache : false,
		async : true,
		dataType : 'json',
		type : "POST",
		data : $('#serverInstance-form').serialize(),
		success : function(data) {

			$("#add-buttons-div").show();
			$("#add-progress-bar-div").hide();
			console.log("success::"+data);
			var response = eval(data);
			
			var responseCode = response.code;
			var responseMsg = response.msg;
			var responseObject = response.object;
			var moduleName = response.module;
			
			if (responseCode == "200") {
				parent.addNewServerInstance($("#serverId").val(),
						responseObject["id"], $("#port").val(), $(
						"#name").val(), $("#serverHostIp")
						.val(), responseObject["status"],
						updateAccessStatus, deleteAccessStatus);
				$.fancybox.close();
				parent.resizeFancyBox();
				$('#serverInstance-form').trigger("reset");
				$('#tblInstanceList').html('');
				$('#tblInstanceList').prop('display', 'none');
				$('#tblInstanceList').prop('display', 'none');
				$("#createServerInstanceContent").show();
				$("#divValidationList").html('');
				$("#createServerInstanceBtn").show();
				$("#cancelInstanceBtn").show();
				$("#closeInstanceBtn").hide();
				showSuccessMsg(responseMsg);
				// display success message of synch for instance
				if (mode == 'CREATE_MODE_IMPORT'
					|| mode == 'CREATE_MODE_COPY')
					showSuccessMsg(responseMsg);
				
			} else if (responseObject != undefined
					&& responseObject != 'undefined') {
				
				resetWarningDisplay();
				
				if (mode == 'CREATE_MODE_IMPORT'
					&& moduleName == 'IMPORT') {
					showErrorMsgPopUp(responseMsg);
					$("#createServerInstanceContent").hide();
					$("#divValidationList").html('');
					$("#createServerInstanceBtn").hide();
					$("#cancelInstanceBtn").hide();
					$("#closeInstanceBtn").show();
					var tableString = '<table class="table table-hover">';
					var jsonObjectLength = Object
					.keys(responseObject[0]).length;
					
					if (jsonObjectLength == 2) {
						// Length is 2 , when XSD validation fail
						
						tableString += "<tr>";
						tableString += "<th>" + jsLocaleMsg.LineNo
						+ "</th>";
						tableString += "<th>"
							+ jsLocaleMsg.errorMessage + "</th>";
						tableString += "</tr>";
						
					} else if (jsonObjectLength == 4) {
						// Length is 4 , when Entity validation fail
						tableString += "<tr>";
						tableString += "<th>" + jsLocaleMsg.entityName
						+ "</th>";
						tableString += "<th>"
							+ jsLocaleMsg.propertyName + "</th>";
						tableString += "<th>"
							+ jsLocaleMsg.propertyValue + "</th>";
						tableString += "<th>"
							+ jsLocaleMsg.errorMessage + "</th>";
						tableString += "</tr>";
					} else if (jsonObjectLength == 5) {
						// Length is 5 , when Entity validation fail
						tableString += "<tr>";
						tableString += "<th>" + jsLocaleMsg.moduleName
						+ "</th>";
						tableString += "<th>" + jsLocaleMsg.entityName
						+ "</th>";
						tableString += "<th>"
							+ jsLocaleMsg.propertyName + "</th>";
						tableString += "<th>"
							+ jsLocaleMsg.propertyValue + "</th>";
						tableString += "<th>"
							+ jsLocaleMsg.errorMessage + "</th>";
						tableString += "</tr>";
						
					}
					
					$.each(responseObject, function(index,
							responseObject) {
						if (jsonObjectLength == 2) {
							tableString += "<tr>";
							tableString += "<td>" + responseObject[0]
							+ "</td>";
							tableString += "<td>" + responseObject[1]
							+ "</td>";
							tableString += "</tr>";
							
						} else if (jsonObjectLength == 4) {
							
							tableString += "<tr>";
							tableString += "<td>" + responseObject[0]
							+ "</td>";
							tableString += "<td>" + responseObject[1]
							+ "</td>";
							tableString += "<td>" + responseObject[2]
							+ "</td>";
							tableString += "<td>" + responseObject[3]
							+ "</td>";
							tableString += "</tr>";
						} else if (jsonObjectLength == 5) {
							tableString += "<tr>";
							tableString += "<td>" + responseObject[0]
							+ "</td>";
							tableString += "<td>" + responseObject[1]
							+ "</td>";
							tableString += "<td>" + responseObject[2]
							+ "</td>";
							tableString += "<td>" + responseObject[3]
							+ "</td>";
							tableString += "<td>" + responseObject[4]
							+ "</td>";
							tableString += "</tr>";
						}
					});
					tableString += "</table>"
						$("#divValidationList").html(tableString);
					
				} else {
					if (responseObject != null) {
						addErrorIconAndMsgForAjaxPopUp(responseObject);
						addErrorClassWhenTitleExist($(
								"#GENERIC_ERROR_MESSAGE",
								window.parent.document).val());
						parent.resizeFancyBox();
					} else {
						// to display error of copy config and import
						// config validation fail
						showErrorMsgPopUp(responseMsg);
					}
				}
			} else {
				resetWarningDisplay();
				if (responseMsg == '${instanceRunning}') {
					parent.$('#createServerInstanceBtn').text('Yes');
					parent.$('#cancelInstanceBtn').text('No');
				}
				
				showErrorMsgPopUp(responseMsg);
				parent.resizeFancyBox();
				
				if (mode == 'CREATE_MODE_IMPORT'
					&& moduleName == 'IMPORT') {
					$("#createServerInstanceContent").hide();
					$("#divValidationList").html('');
					$("#createServerInstanceBtn").hide();
					$("#cancelInstanceBtn").hide();
					$("#closeInstanceBtn").show();
				}
			}
		},
		error : function(xhr, st, err) {
			$("#add-buttons-div").show();
			$("#add-progress-bar-div").hide();
			console.log("err::"+err);
			handleGenericError(xhr, st, err);
		}
	});
}

function resetCreateInstanceForm() {
	$('#serverInstance-form').trigger("reset");
}

function addErrorMsgForCreateServer(inputId, errorMsg) {
	// $("#" + inputId).next().children().first().attr("title",errorMsg);	
	$("#" + inputId).next().children().first().attr("data-original-title",
			errorMsg);
}

function closeFancyBoxFromChildIFrame() {
	$.fancybox.close();
}

function addNewServer(serverName, serverId, serverIPAddress, utilityPort,serverType,
		updateaccessright, deleteaccessright) {
	
	if (updateaccessright == true) {
		if (deleteaccessright == true) { // update and delete both rights
			$(
					'<div class="box box-warning" id="itemsList-p1" value="panel1">'
					+ '<div class="box-header with-border">'
					+ '<h3 class="box-title header_text" id="title_'
					+ serverId
					+ '">'
					+ serverName
					+ " - "
					+ serverIPAddress
					+" - "
					+serverType
					+ '</h3>'
					+ '<div class="box-tools pull-right" style="height:100%">'
					+ '<button class="btn btn-box-tool block-collapse-btn" data-toggle="collapse" data-parent="#create-server-block" href="#'
					+ serverId
					+ '_block"><i class="fa fa-minus"></i></button></div></div>'
					+ '<div id="'
					+ serverId
					+ '_block" class="box-body accordion-body collapse in">'
					+ '<table style="border-collapse: collapse; width: 100%;" id="'
					+ serverId
					+ '" >'
					+ '		<tr >'
					+ '			<td class="padding10 border-right1" style="width: 20%;"" id="'
					+ serverId
					+ '" >'
					+ '				<div class="server-drop">'
					+ '				<input type="hidden" id="utilityPort_'
					+ serverId
					+ '"/> '
					+ '<div id="'
					+ serverIPAddress + "_" +serverName + "_del_server_btn"
					+ '">					<a class="ion-ios-close-empty remove-block" onclick="deleteServerCheck('
					+ "'"
					+ serverId
					+ "','"
					+ serverIPAddress
					+ "','"
					+ serverName
					+ "' "
					+ ')"></a></div>'
					+ '					<img src="img/server.png" class="pull-left mright10" height="40px" width="40px" alt="" />'
					+ '					<div class="mtop20 orange dis-inline" id="serverIP_'
					+ serverId
					+ '" style="font-size:14px;"><strong>'
					+ serverIPAddress
					+ '</strong></div><div class="clearfix"></div><br>'
					+ '<div id="'
					+ serverIPAddress + "_" +serverName + "_detail_lnk"
					+ '"><a href="#" class="black" id="link_'
					+ serverId
					+ '" onclick="updateServer(\''
					+ serverId
					+ '\')">'
					+ serverName
					+ '</a></div>'
					+ '</div>			</td>'
					+ '			<td style="width: 80%;" class=""  id="serverInstance_'
					+ serverId
					+ '_droppable_td">'
					+ '		 <div id="'
					+ serverIPAddress + "_" +serverName + "_drop_space"
					+ '">				<div class="col-md-6 mtop10 ui-droppable ui-sortable" id="serverInstance_'
					+ serverId
					+ '_droppable">'
					+ '					<div class="grey-bg box-border padding15 text-center">'
					+ '						<em><strong>'
					+ $("#dragServerInstanceStrongId").html()
					+ '</strong></em>'
					+ '					</div>'
					+ '				</div>'
					+ '			</td>' + '		</tr>' + '</table></div>')
					.prependTo("#cart-added");
		} else { // only update , no delete rights
			
			$(
					'<div class="box box-warning" id="itemsList-p1" value="panel1">'
					+ '<div class="box-header with-border">'
					+ '<h3 class="box-title header_text" id="title_'
					+ serverId
					+ '">'
					+ serverName
					+ " - "
					+ serverIPAddress
					+" - "
					+serverType
					+ '</h3>'
					+ '<div class="box-tools pull-right" style="height:100%">'
					+ '<button class="btn btn-box-tool block-collapse-btn" data-toggle="collapse" data-parent="#create-server-block" href="#'
					+ serverId
					+ '_block"><i class="fa fa-minus"></i></button></div></div>'
					+ '<div id="'
					+ serverId
					+ '_block" class="box-body accordion-body collapse in">'
					+ '<table style="border-collapse: collapse; width: 100%;" id="'
					+ serverId
					+ '" >'
					+ '		<tr >'
					+ '			<td class="padding10 border-right1" style="width: 20%;"" id="'
					+ serverId
					+ '" >'
					+ '				<div class="server-drop">'
					+ '				<input type="hidden" id="utilityPort_'
					+ serverId
					+ '"/> '
					+ '					<img src="img/server.png" class="pull-left mright10" height="40px" width="40px" alt="" />'
					+ '					<div class="mtop20 orange dis-inline" id="serverIP_'
					+ serverId
					+ '" style="font-size:14px;"><strong>'
					+ serverIPAddress
					+ '</strong></div><div class="clearfix"></div><br>'
					+ '<div id="'
					+ serverIPAddress + "_" +serverName + "_detail_lnk"
					+ '"><a href="#" class="black" id="link_'
					+ serverId
					+ '" onclick="updateServer(\''
					+ serverId
					+ '\')">'
					+ serverName
					+ '</a></div>'
					+ '			</td>'
					+ '			<td style="width: 80%;" class=""  id="serverInstance_'
					+ serverId
					+ '_droppable_td">'
					+ '		 <div id="'
					+ serverIPAddress + "_" +serverName + "_drop_space"
					+ '">				<div class="col-md-6 mtop10 ui-droppable ui-sortable" id="serverInstance_'
					+ serverId
					+ '_droppable">'
					+ '					<div class="grey-bg box-border padding15 text-center">'
					+ '						<em><strong>'
					+ $("#dragServerInstanceStrongId").html()
					+ '</strong></em>'
					+ '					</div>'
					+ '				</div>'
					+ '			</td>' + '		</tr>' + '</table></div>')
					.prependTo("#cart-added");
		}
	} else {
		
		if (deleteaccessright == true) { // no update, only delete rights
			
			$(
					'<div class="box box-warning" id="itemsList-p1" value="panel1">'
					+ '<div class="box-header with-border">'
					+ '<h3 class="box-title header_text" id="title_'
					+ serverId
					+ '">'
					+ serverName
					+ " - "
					+ serverIPAddress
					+" - "
					+serverType
					+ '</h3>'
					+ '<div class="box-tools pull-right" style="height:100%">'
					+ '<button class="btn btn-box-tool block-collapse-btn" data-toggle="collapse" data-parent="#create-server-block" href="#'
					+ serverId
					+ '_block"><i class="fa fa-minus"></i></button></div></div>'
					+ '<div id="'
					+ serverId
					+ '_block" class="box-body accordion-body collapse in">'
					+ '<table style="border-collapse: collapse; width: 100%;" id="'
					+ serverId
					+ '" >'
					+ '		<tr >'
					+ '			<td class="padding10 border-right1" style="width: 20%;"" id="'
					+ serverId
					+ '" >'
					+ '				<div class="server-drop">'
					+ '				<input type="hidden" id="utilityPort_'
					+ serverId
					+ '"/> '
					+ '<div id="'
					+ serverIPAddress + "_" +serverName + "_del_server_btn"
					+ '">					<a class="ion-ios-close-empty remove-block" onclick="deleteServerCheck('
					+ "'"
					+ serverId
					+ "','"
					+ serverIPAddress
					+ "','"
					+ serverName
					+ "' "
					+ ')"></a></div>'
					+ '					<img src="img/server.png" class="pull-left mright10" height="40px" width="40px" alt="" />'
					+ '					<div class="mtop20 orange dis-inline" id="serverIP_'
					+ serverId
					+ '" style="font-size:14px;"><strong>'
					+ serverIPAddress
					+ '</strong></div><div class="clearfix"></div><br><a href="#" class="black" id="link_'
					+ serverId
					+ '" style="text-decoration:none;">'
					+ serverName
					+ '</a></div>'
					+ '			</td>'
					+ '			<td style="width: 80%;" class=""  id="serverInstance_'
					+ serverId
					+ '_droppable_td">'
					+ '		 <div id="'
					+ serverIPAddress + "_" +serverName + "_drop_space"
					+ '">				<div class="col-md-6 mtop10 ui-droppable ui-sortable" id="serverInstance_'
					+ serverId
					+ '_droppable">'
					+ '					<div class="grey-bg box-border padding15 text-center">'
					+ '						<em><strong>'
					+ $("#dragServerInstanceStrongId").html()
					+ '</strong></em>'
					+ '					</div>'
					+ '				</div>'
					+ '			</td>' + '		</tr>' + '</table></div>')
					.prependTo("#cart-added");
		} else { // no update , no delete rights
			
			$(
					'<div class="box box-warning" id="itemsList-p1" value="panel1">'
					+ '<div class="box-header with-border">'
					+ '<h3 class="box-title header_text" id="title_'
					+ serverId
					+ '">'
					+ serverName
					+ " - "
					+ serverIPAddress
					+" - "
					+serverType
					+ '</h3>'
					+ '<div class="box-tools pull-right" style="height:100%">'
					+ '<button class="btn btn-box-tool block-collapse-btn" data-toggle="collapse" data-parent="#create-server-block" href="#'
					+ serverId
					+ '_block"><i class="fa fa-minus"></i></button></div></div>'
					+ '<div id="'
					+ serverId
					+ '_block" class="box-body accordion-body collapse in">'
					+ '<table style="border-collapse: collapse; width: 100%;" id="'
					+ serverId
					+ '" >'
					+ '		<tr >'
					+ '			<td class="padding10 border-right1" style="width: 20%;"" id="'
					+ serverId
					+ '" >'
					+ '				<div class="server-drop">'
					+ '				<input type="hidden" id="utilityPort_'
					+ serverId
					+ '"/> '
					+ '					<img src="img/server.png" class="pull-left mright10" height="40px" width="40px" alt="" />'
					+ '					<div class="mtop20 orange dis-inline" id="serverIP_'
					+ serverId
					+ '" style="font-size:14px;"><strong>'
					+ serverIPAddress
					+ '</strong></div><div class="clearfix"></div><br><a href="#" class="black" id="link_'
					+ serverId
					+ '" style="text-decoration:none;">'
					+ serverName
					+ '</a></div>'
					+ '			</td>'
					+ '			<td style="width: 80%;" class=""  id="serverInstance_'
					+ serverId
					+ '_droppable_td">'
					+ '		 <div id="'
					+ serverIPAddress + "_" +serverName + "_drop_space"
					+ '">				<div class="col-md-6 mtop10 ui-droppable ui-sortable" id="serverInstance_'
					+ serverId
					+ '_droppable">'
					+ '					<div class="grey-bg box-border padding15 text-center">'
					+ '						<em><strong>'
					+ $("#dragServerInstanceStrongId").html()
					+ '</strong></em>'
					+ '					</div>'
					+ '				</div>'
					+ '			</td>' + '		</tr>' + '</table></div>')
					.prependTo("#cart-added");
		}
	}
	assignServerInstanceDroppableEvent("serverInstance_" + serverId
			+ "_droppable", serverId, serverIPAddress, utilityPort);
	
	initializeFancyBox();
}

function addNewServerInstance(serverId, serverInstanceDBId, serverInstancePort,
		serverInstanceName, serverHostIp, satus, updateaccessright,
		deleteaccessright) {
	
	var shortServerInstanceName = serverInstanceName;
	if(serverInstanceName.length > 30){
		shortServerInstanceName = serverInstanceName.substring(0,30) + '...';
	}
	
	if (updateaccessright == true) {
		if (deleteaccessright == true) { // update and delete both rights
			
			if (satus == 'INACTIVE') { // if instance is inactive than display
				// different icon
				
				$("#serverInstance_" + serverId + "_droppable_td")
				.prepend(
						'<div class="col-md-6 mtop10">'
						+ '	<div id="loader_'
						+ serverInstanceDBId
						+ '"><img src="img/chk_sts.ico" width="15px" data-toggle="tooltip" data-placement="bottom" title="'
						+ jsLocaleMsg.chkStatus
						+ '" height="15px" style="vertical-align: middle;position: absolute;z-index: 99999;margin-top: 25px;cursor:pointer;" onclick="chkStatusPopup(\''
						+ serverInstanceDBId
						+ '\',\'CREATE_SERVER\');"></div>'
						+ '	<div class="box-border padding10 pos-relative server-box3" style="padding-left:20px;">'
						+'<div id ="'+serverHostIp+"_"+serverInstancePort+"_del_btn"
						+ '">		<a class="ion-ios-close-empty remove-block" onclick="deleteServerInstancePopup('
						+ " 'create_server','"
						+ serverHostIp
						+ "','"
						+ serverInstancePort
						+ "','"
						+ serverInstanceName
						+ "','"
						+ serverInstanceDBId
						+ "','"
						+ serverInstanceName
						+ "' "
						+ ')"></a></div>'
						+ '		<img name="unchecked_server_instance" src="img/server-instance.png" class="pull-left mright10" alt="" height="40px" width="40px" style="padding-left: 5px;" />'
						+ '		<div>'
						+ '			<p class="orange mtop0" style="margin-bottom:0px !important;">'
						+ '				<strong>'
						+ serverInstancePort
						+ '				</strong>'
						+ '			</p>'
						+ '			<a href="#" id="'
						+ serverInstanceDBId
						+ '_name" class="black" data-toggle="modal" rel="tooltip" title="'
						+serverInstanceName
						+'" style="text-decoration:none;">'
						+ shortServerInstanceName
						+ '			</a>'
						+ '		</div>' + '	</div>' + '</div>');
				
			} else { // instance in active state
				
				$("#serverInstance_" + serverId + "_droppable_td")
				.prepend(
						'<div class="col-md-6 mtop10">'
						+ '	<div id="loader_'
						+ serverInstanceDBId
						+ '"><img src="img/preloaders/circle-red.gif" width="15px" data-toggle="tooltip" data-placement="bottom" title="'
						+ jsLocaleMsg.loadStatus
						+ '" height="15px" style="vertical-align: middle;position: absolute;z-index: 99999;margin-top: 25px;"></div>'
						+ '	<div class="box-border padding10 pos-relative server-box2" style="padding-left:15px;">'
						+'<div id ="'+serverHostIp+"_"+serverInstancePort+"_del_btn"
						+ '">		<a class="ion-ios-close-empty remove-block" onclick="deleteServerInstancePopup('
						+ " 'create_server','"
						+ serverHostIp
						+ "','"
						+ serverInstancePort
						+ "','"
						+ serverInstanceName
						+ "','"
						+ serverInstanceDBId
						+ "','"
						+ serverInstanceName
						+ "' "
						+ ')"></a></div>'
						+ '		<img id='+serverHostIp+'#'+serverInstancePort+'#'+serverInstanceName+' name="checked_server_instance" src="img/server-instance.png" class="pull-left mright10" alt="" height="40px" width="40px" style="padding-left: 5px;" />'
						+ '		<div>'
						+ '			<p class="orange mtop0" style="margin-bottom:0px !important;">'
						+ '				<strong>'
						+ serverInstancePort
						+ '				</strong>'
						+ '			</p>'
						+ '		 <div id="'
						+ serverHostIp + "_" +serverInstancePort + "_detail_lnk"
						+ '">		<a href="#" id="'
						+ serverInstanceDBId
						+ '_name" class="black" data-toggle="modal" rel="tooltip" title="'
						+serverInstanceName
						+'" onclick="viewServerInstance('
						+ "'"
						+ serverInstanceDBId
						+ "','ACTIVE'"
						+ ')">'
						+ shortServerInstanceName
						+ '			</a>'
						+ '	</div>'+	'</div>' + '	</div>' + '</div>');
				
			}
		} else { // only update , no delete rights
			
			if (satus == 'INACTIVE') { // if instance is inactive than display
				// different icon
				
				$("#serverInstance_" + serverId + "_droppable_td")
				.prepend(
						'<div class="col-md-6 mtop10">'
						+ '	<div id="loader_'
						+ serverInstanceDBId
						+ '"><img src="img/chk_sts.ico" width="15px" data-toggle="tooltip" data-placement="bottom" title="'
						+ jsLocaleMsg.chkStatus
						+ '" height="15px" style="vertical-align: middle;position: absolute;z-index: 99999;margin-top: 25px;cursor:pointer;" onclick="chkStatusPopup(\''
						+ serverInstanceDBId
						+ '\',\'CREATE_SERVER\');"></div>'
						+ '	<div class="box-border padding10 pos-relative server-box3" style="padding-left:15px;">'
						+
						
						'		<img name="s3" src="img/server-instance.png" class="pull-left mright10" alt="" height="40px" width="40px"  style="padding-left: 5px;" />'
						+ '		<div>'
						+ '			<p class="orange mtop0" style="margin-bottom:0px !important;">'
						+ '				<strong>'
						+ serverInstancePort
						+ '				</strong>'
						+ '			</p>'
						+ '			<a href="#" id="'
						+ serverInstanceDBId
						+ '_name" class="black" data-toggle="modal" rel="tooltip" title="'
						+serverInstanceName
						+'">'
						+ shortServerInstanceName
						+ '			</a>'
						+ '		</div>' + '	</div>' + '</div>');
				
			} else {
				
				$("#serverInstance_" + serverId + "_droppable_td")
				.prepend(
						'<div class="col-md-6 mtop10">'
						+ '	<div id="loader_'
						+ serverInstanceDBId
						+ '"><img src="img/preloaders/circle-red.gif" width="15px" data-toggle="tooltip" data-placement="bottom" title="'
						+ jsLocaleMsg.loadStatus
						+ '" height="15px" style="vertical-align: middle;position: absolute;z-index: 99999;margin-top: 25px;"></div>'
						+ '	<div class="box-border padding10 pos-relative server-box2" style="padding-left:15px;">'
						+
						
						'		<img name="s4" src="img/server-instance.png" class="pull-left mright10" alt="" height="40px" width="40px"  style="padding-left: 5px;" />'
						+ '		<div>'
						+ '			<p class="orange mtop0" style="margin-bottom:0px !important;">'
						+ '				<strong>'
						+ serverInstancePort
						+ '				</strong>'
						+ '			</p>'
						+ '		 <div id="'
						+ serverHostIp + "_" +serverInstancePort + "_detail_lnk"
						+ '">	<a href="#" id="'
						+ serverInstanceDBId
						+ '_name" class="black" data-toggle="modal" rel="tooltip" title="'
						+serverInstanceName
						+'" onclick="viewServerInstance('
						+ "'"
						+ serverInstanceDBId
						+ "','ACTIVE'"
						+ ')">'
						+ shortServerInstanceName
						+ '			</a>'
						+ '	</div>' +	'</div>' + '	</div>' + '</div>');
				
			}
		}
	} else {
		
		if (deleteaccessright == true) { // no update, only delete rights
			
			if (satus == 'INACTIVE') { // if instance is inactive than display
				// different icon
				
				$("#serverInstance_" + serverId + "_droppable_td")
				.prepend(
						'<div class="col-md-6 mtop10">'
						+ '	<div id="loader_'
						+ serverInstanceDBId
						+ '"><img src="img/chk_sts.ico" width="15px" data-toggle="tooltip" data-placement="bottom" title="'
						+ jsLocaleMsg.chkStatus
						+ '" height="15px" style="vertical-align: middle;position: absolute;z-index: 99999;margin-top: 25px;cursor:pointer;" onclick="chkStatusPopup(\''
						+ serverInstanceDBId
						+ '\',\'CREATE_SERVER\');"></div>'
						+ '	<div class="box-border padding10 pos-relative server-box3" style="padding-left:15px;">'
						+'<div id ="'+serverHostIp+"_"+serverInstancePort+"_del_btn"
						+ '">		<a class="ion-ios-close-empty remove-block" onclick="deleteServerInstancePopup('
						+ " 'create_server','"
						+ serverHostIp
						+ "','"
						+ serverInstancePort
						+ "','"
						+ shortServerInstanceName
						+ "','"
						+ serverInstanceDBId
						+ "','"
						+ shortServerInstanceName
						+ "' "
						+ ')"></a></div>'
						+ '		<img name="s5" src="img/server-instance.png" class="pull-left mright10" alt="" height="40px" width="40px" style="padding-left: 5px;" />'
						+ '		<div>'
						+ '			<p class="orange mtop0" style="margin-bottom:0px !important;">'
						+ '				<strong>'
						+ serverInstancePort
						+ '				</strong>'
						+ '			</p>'
						+ '			<a href="#" id="'
						+ serverInstanceDBId
						+ '_name" class="black" data-toggle="modal" rel="tooltip" title="'
						+serverInstanceName
						+'" style="text-decoration:none;">'
						+ shortServerInstanceName
						+ '			</a>'
						+ '		</div>' + '	</div>' + '</div>');
				
			} else {
				
				$("#serverInstance_" + serverId + "_droppable_td")
				.prepend(
						'<div class="col-md-6 mtop10">'
						+ '	<div id="loader_'
						+ serverInstanceDBId
						+ '"><img src="img/preloaders/circle-red.gif" width="15px" data-toggle="tooltip" data-placement="bottom" title="'
						+ jsLocaleMsg.loadStatus
						+ '" height="15px" style="vertical-align: middle;position: absolute;z-index: 99999;margin-top: 25px;"></div>'
						+ '	<div class="box-border padding10 pos-relative server-box2" style="padding-left:15px; ">'
						+'<div id ="'+serverHostIp+"_"+serverInstancePort+"_del_btn"
						+ '">		<a class="ion-ios-close-empty remove-block" onclick="deleteServerInstancePopup('
						+ " 'create_server','"
						+ serverHostIp
						+ "','"
						+ serverInstancePort
						+ "','"
						+ shortServerInstanceName
						+ "','"
						+ serverInstanceDBId
						+ "','"
						+ shortServerInstanceName
						+ "' "
						+ ')"></a>'
						+'</div>'
						+ '		<img name="s6" src="img/server-instance.png" class="pull-left mright10" alt="" height="40px" width="40px" style="padding-left: 5px;" />'
						+ '		<div>'
						+ '			<p class="orange mtop0" style="margin-bottom:0px !important;">'
						+ '				<strong>'
						+ serverInstancePort
						+ '				</strong>'
						+ '			</p>'
						+ '			<a href="#" id="'
						+ serverInstanceDBId
						+ '_name" class="black" data-toggle="modal" rel="tooltip" title="'
						+serverInstanceName
						+'" style="text-decoration:none;">'
						+ shortServerInstanceName
						+ '			</a>'
						+ '		</div>' + '	</div>' + '</div>');
				
			}
		} else { // no update , no delete rights
			
			if (satus == 'INACTIVE') { // if instance is inactive than display
				// different icon
				
				$("#serverInstance_" + serverId + "_droppable_td")
				.prepend(
						'<div class="col-md-6 mtop10">'
						+ '	<div id="loader_'
						+ serverInstanceDBId
						+ '"><img src="img/chk_sts.ico" width="15px" data-toggle="tooltip" data-placement="bottom" title="'
						+ jsLocaleMsg.chkStatus
						+ '" height="15px" style="vertical-align: middle;position: absolute;z-index: 99999;margin-top: 25px;cursor:pointer;" onclick="chkStatusPopup(\''
						+ serverInstanceDBId
						+ '\',\'CREATE_SERVER\');"></div>'
						+ '	<div class="box-border padding10 pos-relative server-box3" style="padding-left:15px;">'
						+ '		<img name="s7" src="img/server-instance.png" class="pull-left mright10" alt="" height="40px" width="40px" style="padding-left: 5px;" />'
						+ '		<div>'
						+ '			<p class="orange mtop0" style="margin-bottom:0px !important;">'
						+ '				<strong>'
						+ serverInstancePort
						+ '				</strong>'
						+ '			</p>'
						+ '			<a href="#" id="'
						+ serverInstanceDBId
						+ '_name" class="black" data-toggle="modal" rel="tooltip" title="'
						+serverInstanceName
						+'" style="text-decoration:none;">'
						+ shortServerInstanceName
						+ '			</a>'
						+ '		</div>' + '	</div>' + '</div>');
				
			} else {
				
				$("#serverInstance_" + serverId + "_droppable_td")
				.prepend(
						'<div class="col-md-6 mtop10">'
						+ '	<div id="loader_'
						+ serverInstanceDBId
						+ '"><img src="img/preloaders/circle-red.gif" width="15px" data-toggle="tooltip" data-placement="bottom" title="'
						+ jsLocaleMsg.loadStatus
						+ '" height="15px" style="vertical-align: middle;position: absolute;z-index: 99999;margin-top: 25px;"></div>'
						+ '	<div class="box-border padding10 pos-relative server-box2" style="padding-left:15px;">'
						+ '		<img name="s7" src="img/server-instance.png" class="pull-left mright10" alt="" height="40px" width="40px" style="padding-left: 5px;" />'
						+ '		<div>'
						+ '			<p class="orange mtop0" style="margin-bottom:0px !important;">'
						+ '				<strong>'
						+ serverInstancePort
						+ '				</strong>'
						+ '			</p>'
						+ '			<a href="#" id="'
						+ serverInstanceDBId
						+ '_name" class="black" data-toggle="modal" rel="tooltip" data-placement="bottom" title='
						+serverInstanceName
						+' style="text-decoration:none;">'
						+ shortServerInstanceName + '			</a>'
						+ '		</div>' + '	</div>' + '</div>');
				
			}
		}
	}
	
	if (satus == 'ACTIVE') // load status for instance only when status is
		// active (normal one time created and up instance)
		loadInstanceStatusGUI(serverInstanceDBId);
	
	initializeFancyBox();
	hideServerDeleteBtnWhenServerInstanceExist();
	// loadInstanceStatusGUI(serverInstanceDBId);
}

function hideServerAndServerInstanceDroppableRegions() {
	try {
		if ($("#createServerAndServerInstanceComponentDiv").length == 0) {
			$('table tr')
			.each(
					function() {
						$(this)
						.find('td')
						.each(
								function() {
									// do your stuff, you can
									// use $(this) to get
									// current cell
									var cellId = $(this).attr(
									"id");
									if (cellId != undefined
											&& startsWith(
													cellId,
											'serverInstance_')
											&& endsWith(cellId,
											'_droppable_td')
											&& $("#" + cellId)
											.children().length > 0) {
										$("#" + cellId)
										.children()
										.last().hide();
									}
								})
					});
			
			$("#cart").hide();
		}
	} catch (e) {
		// do nothing
	}
}

function deleteServerPopUp(serverId, serverIP, serverName) {
	clearAllMessagesPopUp();
	clearAllMessages();
	$("#deleteServerBtn").show();
	$("#cancelServerBtn").show();
	$("#deleteServerId").val(serverId);
	$("#closeServerBtnSuccess").hide();
	$("#closeServerBtn").hide();
	
	$("#tblDeleteServerList").html('');
	
	var servertableString = '<table class="table table-hover" style="width:100%">';
	servertableString += "<tr>";
	servertableString += "<th>" + jsLocaleMsg.serverName + "</th>";
	servertableString += "<th>" + jsLocaleMsg.serverIP + "</th>";
	servertableString += "</tr>";
	servertableString += "<tr>";
	servertableString += "<td>" + serverName + "</td>";
	servertableString += "<td>" + serverIP + "</td>";
	servertableString += "</tr>";
	servertableString += "</table>";
	$("#tblDeleteServerList").append(servertableString);
	
	$("#deleteServerAnchor").click();
}

function protocolTypeChanged() {
	if ($("#protocolType").val() == 'LOCAL') {
		$("#telnetPort").parent().parent().hide();
		$("#shellPrompt").parent().parent().hide();
		$("#shellUsername").parent().parent().hide();
		$("#shellPassword").parent().parent().hide();
		
		$("#telnetPort").val('23');
		$("#shellPrompt").val('');
		$("#shellUsername").val('');
		$("#shellPassword").val('');
	} else {
		$("#telnetPort").parent().parent().show();
		$("#shellPrompt").parent().parent().show();
		$("#shellUsername").parent().parent().show();
		$("#shellPassword").parent().parent().show();
	}
	parent.resizeFancyBox();
}

function viewServerInstance(instanceId) {
	$("#serverInstanceId").val(instanceId);
	// $('#serverInstanceStatus').val(instanceStatus);
	$("#instance_form").submit();
}

function loadServerInstanceListForCopy(serverIpAddress) {
	resetWarningDisplay();
	clearResponseMsgPopUp();
	showErrorMsg('');
	$("#add-buttons-div").hide();
	$("#add-progress-bar-div").show();
	parent.resizeFancyBox();
	$
	.ajax({
		url : 'getAllInstancesByServerTypeForCreate',
		cache : false,
		async : true,
		dataType : 'json',
		type : "POST",
		data:{
			"serverId" : serverIpAddress
		},
		success : function(data) {
			$("#add-buttons-div").show();
			$("#add-progress-bar-div").hide();
			
			var response = eval(data);
			
			var responseCode = response.code;
			var responseMsg = response.msg;
			var responseObject = response.object;
			
			if (responseCode == "200") {
				parent.$('#tblInstanceList').html('');
				
				$.each(responseObject, function(index, responseObject) {
					var tabStr = "<tr>"
						+ "<td width='50px'>"
						+ "<div id='"
						+ responseObject.server.ipAddress + "_" +responseObject.name + "_radio_btn"
						+ "'><input type='radio' name='copyFromId' value='"
						+ responseObject.id + "' id='"
						+ responseObject.id + "' /></td>"
						+ "<td width='150px'>"
						+ responseObject.name
						+ "</td>"
						+ "<td width='200px'>"
						+ responseObject.server.ipAddress + "</td>"
						+ "<td width='100px'>"
						+ responseObject.port + "</td>"
						+ "</tr>";
				parent.$('#tblInstanceList').append(tabStr);
			    });
				
				parent.resizeFancyBox();
			} else if (responseObject != undefined 	&& responseObject != 'undefined') {
				resetWarningDisplay();
				parent.resizeFancyBox();
			} else {
				resetWarningDisplay();
				showErrorMsgPopUp(responseMsg);
				parent.resizeFancyBox();
			}
		},
		error : function(xhr, st, err) {
			$("#add-buttons-div").show();
			$("#add-progress-bar-div").hide();
			handleGenericError(xhr, st, err);
		}
	});
}

function processUpload() {
	// check for file size length
	if (files == null || files[0] == null) {
		showErrorMsgPopUp(jsLocaleMsg.importFileMiss);
		return;
	}
	
	var oMyForm = new FormData();
	oMyForm.append("file", files[0]);
	
	if ((files[0].size / 1024) > 2048) {
		showErrorMsgPopUp('File size can\'t exceed 2 mb');
		return;
	}
	clearResponseMsgPopUp();
	
	$('#fileSubmit').hide();
	$('#upload-progress-bar-div').show();
	$('#createServerInstanceBtn').attr('disabled', 'disabled');
	
	$.ajax({
		dataType : 'json',
		url : "uploadImportFile",
		data : oMyForm,
		type : "POST",
		enctype : 'multipart/form-data',
		processData : false,
		contentType : false,
		success : function(response) {
			
			var responseCode = response.code;
			var responseMsg = response.msg;
			var responseObject = response.object;
			
			if (responseCode == "200") {
				$('#fileSubmit').show();
				$('#upload-progress-bar-div').hide();
				$('#createServerInstanceBtn').removeAttr('disabled', '');
				$('#importFile').val(responseObject.filename);
				showSuccessMsgPopUp(responseMsg);
				parent.resizeFancyBox();
			} else if (responseObject != undefined
					&& responseObject != 'undefined') {
				$('#fileSubmit').show();
				$('#upload-progress-bar-div').hide();
				resetWarningDisplay();
				parent.resizeFancyBox();
			} else {
				$('#fileSubmit').show();
				$('#upload-progress-bar-div').hide();
				resetWarningDisplay();
				showErrorMsgPopUp(responseMsg);
				parent.resizeFancyBox();
			}
		},
		error : function(response) {
			$('#fileSubmit').show();
			$('#upload-progress-bar-div').hide();
			$('#createServerInstanceBtn').removeAttr('disabled', '');
		}
	});
}
var serverInstanceId = '';

var actionLocation = '';
function deleteServerInstancePopup(tabType, serverHost, port, instanceName, id) {
	
	actionLocation = tabType;
	clearAllMessages();
	clearAllMessagesPopUp();
	if (id != '') {
		ckIntanceSelected[0] = id;
	}
	
	$("#divDeleteMsg").html('');
	// $("#btnExportDeletePopup").show();
	$("#btnDeleteCancel").show();
	$("#btnDeleteClose").hide();
	$("#btnDeletePopup").show();
	$("#btnDeleteClose-server-mgmt").hide();
	$("#btnDeleteClose-create-server").hide();
	// $("#btnExportDeletePopup").prop('disabled', false);
	// $("#btnDeletePopup").prop('disabled', true);
	
	if (ckIntanceSelected.length == 0) {
		$("#lessWarn").show();
		$("#moreWarn").hide();
		$("#message").click();
		return;
	} else if (ckIntanceSelected.length > 1) {
		$("#lessWarn").hide();
		$("#moreWarn").show();
		$("#message").click();
		return;
	} else {
		$("#tblDeleteInstanceList").html('');
		var rowId = '', serverIP = '', serverPort = '', serverInstance = '';
		
		if (tabType == 'server_mgmt') {
			rowId = ckIntanceSelected[0];
			serverIP = jQuery('#instanceList').jqGrid('getCell', rowId, 'host');
			serverPort = jQuery('#instanceList').jqGrid('getCell', rowId, 'port');
			serverInstance = $(jQuery('#instanceList').jqGrid('getCell', rowId, 'instanceName')).closest("a").html();
		} else if (tabType == 'create_server') {
			rowId = id;
			serverIP = serverHost;
			serverPort = port;
			serverInstance = instanceName;
		}
		
		var tableString = '<table class="table table-hover" style="width:100%">';
		tableString += "<tr>";
		tableString += "<th>" + jsLocaleMsg.insName + "</th>";
		tableString += "<th>" + jsLocaleMsg.serverIP + "</th>";
		tableString += "<th>" + jsLocaleMsg.serverPort + "</th>";
		tableString += "</tr>";
		tableString += "<tr>";
		tableString += "<td>" + serverInstance + "</td>";
		tableString += "<td>" + serverIP + "</td>";
		tableString += "<td>" + serverPort + "</td>";
		tableString += "</tr>";
		tableString += "</table>";
		
		$("#tblDeleteInstanceList").append(tableString);
		
		$('#deleteServerInstance').click();
	}
	
}

function deleteServerCheck(servId, serverIP, serverName) {
	// clearAllMessagesPopUp();
	clearAllMessages();
	// $("#delete-server-progress-bar-div").show();
	// $("#delete-server-button").hide();
	console.log("serverId"+servId);
	$.ajax({
		url : 'deleteServerCheck',
		cache : false,
		async : true,
		dataType : 'json',
		type : "POST",
		data : {
			serverId : servId
		},
		
		success : function(data) {
			
			var response = eval(data);
			response.msg = decodeMessage(response.msg);
			response.msg = replaceAll("+", " ", response.msg);
			
			if (response.code == 200 || response.code == "200") {
				
				deleteServerPopUp(servId, serverIP, serverName);
				
			} else {
				
				showErrorMsg(response.msg);
				
			}
		},
		error : function(xhr, st, err) {
			$("#delete-server-button").show();
			$("#delete-server-progress-bar-div").hide();
			handleGenericError(xhr, st, err);
		}
	});
	
}

function DeleteInstance(actionURL) {
	clearAllMessages();
	clearAllMessagesPopUp();
	$("#delete-progress-bar-div").show();
	$("#delete-buttons-div").hide();
	
	$.ajax({
		url : actionURL,
		cache : false,
		async : true,
		dataType : 'json',
		type : "POST",
		
		data : {
			serverInstanceId : ckIntanceSelected[0]
		},
		
		success : function(data) {

			$("#delete-buttons-div").show();
			$("#delete-progress-bar-div").hide();
			var response = eval(data);
			response.msg = decodeMessage(response.msg);
			response.msg = replaceAll("+", " ", response.msg);
			
			console.log("Response code is :: " + response.code);
			
			$("#btnDeleteClose-server-mgmt").hide();
			$("#btnDeleteClose-create-server").hide();
			
			if (response.code == 200 || response.code == "200") {
				clearResponseMsgDiv();
				clearResponseMsg();
				clearErrorMsg();
				clearAllMessagesPopUp();
				showSuccessMsgPopUp(response.msg);
				
				$("#btnDeletePopup").hide();
				$("#btnDeleteCancel").hide();
				$("#noteIdbeforeDelete").hide();
				$("#noteAfterdelete").show();
				
				var responseObject = data.object;
				
				if (actionLocation == 'server_mgmt') {
					$("#btnDeleteClose-server-mgmt").show();
				} else if (actionLocation == 'create_server') {
					$("#btnDeleteClose-create-server").show();
				}
				
				exportInstanceBeforeDelete(responseObject);
				
			} else {
				showErrorMsgPopUp(response.msg);
				$("#btnDeletePopup").hide();
				$("#btnDeleteCancel").hide();
				
				if (actionLocation == 'server_mgmt') {
					$("#btnDeleteClose-server-mgmt").show();
				} else if (actionLocation == 'create_server') {
					$("#btnDeleteClose-create-server").show();
				}
				
			}
		},
		error : function(xhr, st, err) {
			$("#delete-buttons-div").show();
			$("#delete-progress-bar-div").hide();
			handleGenericError(xhr, st, err);
			if (actionLocation == 'server_mgmt') {
				$("#btnDeleteClose-server-mgmt").show();
				searchInstanceCriteria();
			} else if (actionLocation == 'create_server') {
				$("#btnDeleteClose-create-server").show();
			}
		}
		
	});
}

function exportInstanceBeforeDelete(exportFilePath) {
	$("#exportInstancesId").val(ckIntanceSelected[0]);
	$("#isExportForDelete").val(true);
	$("#exportPath").val(exportFilePath);
	$("#export-config-form").submit();
}

function uploadAndImportServerInstance(actionUrl, serverInstanceId, importMode,
		currentTab) {
	
	var oMyForm = new FormData();
	oMyForm.append("configFile", files[0]);
	oMyForm.append("importInstanceId", serverInstanceId);
	oMyForm.append("importMode", importMode);
	// check for file size length
	
	clearResponseMsgPopUp();
	
	$("#import-add-btn").hide();
	$("#import-overwrite-btn").hide();
	$("#import-update-btn").hide();
	$("#import-keepboth-btn").hide();
	$("#import-config-progress-bar-div").show();
	$('#import-popup-close-btn').hide();
	$("#import-popup-close-btn-summary").hide();
	
	$
	.ajax({
		dataType : 'json',
		url : actionUrl,
		data : oMyForm,
		type : "POST",
		enctype : 'multipart/form-data',
		processData : false,
		contentType : false,
		success : function(response) {
			
			var responseCode = response.code;
			var responseMsg = response.msg;
			var responseObject = response.object;
			
			if (responseCode == "200") {
				
				if (currentTab == 'UPDATE_INSTANCE_SUMMARY') {
					$('#import-popup-close-btn-summary').show();
				} else {
					$('#import-popup-close-btn').show();
				}
				$('#import-config-progress-bar-div').hide();
				
				closeFancyBox();
				showSuccessMsg(responseMsg);
				parent.resizeFancyBox();
				clearFileContent();
				clearServiceSummaryInstanceGridAfterImport();
			} else if (responseObject != undefined
					&& responseObject != 'undefined') {
				showErrorMsgPopUp(responseMsg);
				$("#importContent").hide();
				$("#divValidationList").html('');
				var tableString = '<table class="table table-hover">';
				var jsonObjectLength = 0;
				if(responseObject && responseObject[0]) {
					jsonObjectLength = Object.keys(responseObject[0]).length;
				}
				
				
				if (jsonObjectLength == 2) {
					// Length is 2 , when XSD validation fail
					tableString += "<tr>";
					tableString += "<th>" + jsLocaleMsg.LineNo
					+ "</th>";
					tableString += "<th>" + jsLocaleMsg.errorMessage
					+ "</th>";
					tableString += "</tr>";
				} else if (jsonObjectLength == 5) {
					// Length is 5 , when Entity validation fail
					
					tableString += "<tr>";
					tableString += "<th>" + jsLocaleMsg.moduleName
					+ "</th>";
					tableString += "<th>" + jsLocaleMsg.entityName
					+ "</th>";
					tableString += "<th>" + jsLocaleMsg.propertyName
					+ "</th>";
					tableString += "<th>" + jsLocaleMsg.propertyValue
					+ "</th>";
					tableString += "<th>" + jsLocaleMsg.errorMessage
					+ "</th>";
					tableString += "</tr>";
					
				}
				
				$.each(responseObject, function(index, responseObject) {
					
					if (jsonObjectLength == 2) {
						
						tableString += "<tr>";
						tableString += "<td>" + responseObject[0]
						+ "</td>";
						tableString += "<td>" + responseObject[1]
						+ "</td>";
						tableString += "</tr>";
						
					} else if (jsonObjectLength == 5) {
						
						tableString += "<tr>";
						tableString += "<td>" + responseObject[0]
						+ "</td>";
						tableString += "<td>" + responseObject[1]
						+ "</td>";
						tableString += "<td>" + responseObject[2]
						+ "</td>";
						tableString += "<td>" + responseObject[3]
						+ "</td>";
						tableString += "<td>" + responseObject[4]
						+ "</td>";
						tableString += "</tr>";
					}
					
				});
				tableString += "</table>"
					
				if(jsonObjectLength === 0) {
					// In case when no need to display table, only one validation message is there
					$("#importContent").show();
					$("#import-add-btn").hide();
					$("#import-overwrite-btn").show();
					$("#import-update-btn").hide();
					$("#import-keepboth-btn").hide();
					$('#import-popup-close-btn').show();
					$('#import-config-progress-bar-div').hide();
					showErrorMsgPopUp(responseMsg);
					parent.resizeFancyBox();
					clearFileContent();
				} else {
					$("#divValidationList").html(tableString);
					$("#import-add-btn").hide();
					$("#import-overwrite-btn").hide();
					$("#import-update-btn").hide();
					$("#import-keepboth-btn").hide();
					$('#import-popup-close-btn').show();
					$('#import-config-progress-bar-div').hide();
					resetWarningDisplay();
					parent.resizeFancyBox();
					clearFileContent();
				}
				
			} else {
				$("#import-add-btn").hide();
				$("#import-overwrite-btn").show();
				$("#import-update-btn").hide();
				$("#import-keepboth-btn").hide();
				$('#import-popup-close-btn').show();
				$('#import-config-progress-bar-div').hide();
				if(!responseMsg || responseMsg === null || responseMsg === '') {
  	        		showErrorMsgPopUp("File upload fail. check logs for more detail.");
  	        	} else {
  	        		showErrorMsgPopUp(responseMsg);
  	        	}
				parent.resizeFancyBox();
				clearFileContent();
			}
		},
		error : function(response) {
			$("#import-add-btn").hide();
			$("#import-overwrite-btn").show();
			$('#import-popup-close-btn').show();
			$("#import-update-btn").hide();
			$("#import-keepboth-btn").hide();
			$('#upload-progress-bar-div').hide();
		}
	});
	
	function clearServiceSummaryInstanceGridAfterImport(){
		var $grid = $("#summaryServiceList");
		$grid.jqGrid('setGridParam',{datatype:'json',page: 1}).trigger('reloadGrid');
		var $grid = $("#summaryServiceList");
		var rowIds = $grid.jqGrid('getDataIDs');
		// iterate through the rows and delete each of them
		for(var i=0,len=rowIds.length;i<len;i++){
			var currRow = rowIds[i];
			$grid.jqGrid('delRowData', currRow);
		}
	}
}

