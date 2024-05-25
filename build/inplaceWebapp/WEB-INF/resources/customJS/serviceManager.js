/**
 * 
 */
function initializeServerInstanceAndServiceComponents(){
	$( "#serverInstance-components li" ).draggable({
		appendTo: "body",
		helper: "clone"
	});
	$( "#service-components li" ).draggable({
		appendTo: "body",
		helper: "clone"
	});

	$( "#serverInstanceDropArea" ).droppable({
		activeClass: "ui-state-default",
		hoverClass: "ui-state-hover",
		accept: ":not(.ui-sortable-helper)",
		drop: function( event, ui ) {
			var draggedText = $(ui.draggable).attr("id");
			if(draggedText.indexOf("Server_Instance") >= 0){
				if ($('#serverInstanceTable tr').length >0){
					var selectedid='';
					$("#serverInstanceTable tr").each(function() {
						
						$(this).children().each(function(){
							
							selectedid +=$(this).attr('id')+",";
							return false;
						});
					});
				}
				$("#selectedInstanceId").val(selectedid);
				$("#serverInstanceDetailAnchor").click();
			}
		}
	});
}

function displayblankDragArea(){
	
	$('<div id="cart">'+
			
			'<div class="box box-warning">'+
			'<div class="box-header with-border">'+
			'<h3 class="box-title header_text" id="header1">'+jsLocaleMsg.droppableRegionLabel+'</h3>'+
			'<div class="box-tools pull-right" style="height: 100%">'+
			'<button class="btn btn-box-tool" data-widget="collapse">'+
				'<i class="fa fa-minus"></i>'+
			'</button>'+
			'</div>'+
	 '</div>'+
	 '<div class="box-body">'+
		'<ol style="width: 100%; list-style: none;">'+
			'<li>'+
				'<table class="full-size">'+
					'<tr>'+
						'<td id="serverInstanceDropArea" width="20%" class="text-center grey-bg box-border ui-droppable ui-sortable padding15">'+
							'<em class="mtop40">'+
								'<strong>'+jsLocaleMsg.dragServerInstanceLable+'</strong>'+
							'</em>'+
						'</td>'+
						'<td width="80%">'+
							'<div class="col-md-6 " id="serviceDragArea">'+
								'<div class="grey-bg box-border padding15 text-center">'+
									'<em>'+
										'<strong id="dragServiceInstanceStrongId">'+jsLocaleMsg.dragServiceInstanceStrongId+'</strong>'+
									'</em>'+
								'</div>'+
							'</div>'+
						'</td>'+
					'</tr>'+
				'</table>'+
			'</li>'+
		'</ol>'+
		'</div>'+
	'</div>' ).prependTo("#cart-added");
}

function displayServerInstance(serverInstanceName,serverInstanceDBId, serverIPAddress,port,serverType,accessright,isSelectedInstance){
	
	if(isSelectedInstance){
		if(accessright == true){
			$('<div class="box box-warning" id="box_'+serverInstanceDBId+'" value="panel1">'+
					'<div class="box-header with-border">'+
					'<h3 class="box-title header_text" id="title_'+serverInstanceDBId+'">' + serverInstanceName + " - " + serverIPAddress + '</h3>'+
					'<div class="box-tools pull-right" style="height:100%">'+
					'<button class="btn btn-box-tool block-collapse-btn" data-toggle="collapse" data-parent="#create-server-block" href="#' + serverInstanceDBId + '_block"><i class="fa fa-minus"></i></button></div></div>'+
					'<div id="' + serverInstanceDBId + '_block" class="box-body accordion-body collapse in">'+
					'<table style="border-collapse: collapse; width: 100%;" id="serverInstanceTable" >' +
					  '		<tr >' + 
					  
					  '			<td class="padding10 border-right1" style="width: 20%;"" id="' + serverInstanceDBId + '" >' + 
					  '	<div id="loader_'+serverInstanceDBId+'"><img src="img/preloaders/circle-red.gif" width="15px" data-toggle="tooltip" data-placement="bottom" title="Loading Status.." height="15px" style="vertical-align: middle;position: absolute;z-index: 99999;margin-top: 37px;"></div>' +
					  '				<div class="mtop10 server-box2 serverInstance-drop">'+
					  '		 <div id="'
						+ serverIPAddress + "_" +serverInstanceName + "_del_btn"
						+ '"> <a class="ion-ios-close-empty remove-block" id="'+serverInstanceDBId+'_delete" onclick="deleteInstanceFromGUI(\'' + serverInstanceDBId + '\')"></a></div>' +
					  '					<img src="img/server-instance.png" class="mtop10 pull-left mright10" alt="" height="30%" width="30%" />' +
					  '					<div class="mtop10 orange dis-inline" id="serverIP_'+serverInstanceDBId+'"><p><strong>'+ serverType+ '<br>' + serverIPAddress + '<br>'+
					  						+ port + '</strong></p></div></div>'+
					  ' <div id="'
					  + serverIPAddress + "_" +serverInstanceName + "_detail_btn"
					  + '">	<a href="#" style="margin-left: 15px;" class="black" id="link_'+serverInstanceDBId+'" onclick="viewServerInstance(\'' + serverInstanceDBId + '\')">' + serverInstanceName + '</a></div>' +
					  '			</td>' + 
					  //'</div'+
					  '			<td style="width: 80%;" class=""  id="service_'+serverInstanceDBId+'_droppable_td">' +
					  
					  '		 <div id="'
						+ serverIPAddress + "_" +serverInstanceName + "_svc_drop_space"
					 + '">'+
					 '<div class="col-md-6 mtop10 ui-droppable ui-sortable" id="service_'+serverInstanceDBId+'_droppable">' +
		          	  '					<div class="grey-bg box-border padding15 text-center">' +
		              '						<em><strong>' + jsLocaleMsg.dragServiceInstanceStrongId + '</strong></em>' +
		              '					</div>' +
		          	  '				</div>' +
		          	'				</div>' +
		          	  '			</td>' +
					  '		</tr>' + 
					  '</table></div>').prependTo("#cart-added");

		}else{
			$('<div class="box box-warning" id="box_'+serverInstanceDBId+'" value="panel1">'+
					'<div class="box-header with-border")">'+
					'<h3 class="box-title header_text" id="title_'+serverInstanceDBId+'">' + serverInstanceName + " - " + serverIPAddress + '</h3>'+
					'<div class="box-tools pull-right" style="height:100%">'+
					'<button class="btn btn-box-tool block-collapse-btn" data-toggle="collapse" data-parent="#create-server-block" href="#' + serverInstanceDBId + '_block"><i class="fa fa-minus"></i></button></div></div>'+
					'<div id="' + serverInstanceDBId + '_block" class="box-body accordion-body collapse in">'+
					'<table style="border-collapse: collapse; width: 100%;" id="serverInstanceTable" >' +
					  '		<tr >' + 
					  
					  '			<td class="padding10 border-right1" style="width: 20%;"" id="' + serverInstanceDBId + '" >' + 
					  '	<div id="loader_'+serverInstanceDBId+'"><img src="img/preloaders/circle-red.gif" width="15px" data-toggle="tooltip" data-placement="bottom" title="Loading Status.." height="15px" style="vertical-align: middle;position: absolute;z-index: 99999;margin-top: 37px;"></div>' +
					  '				<div class="mtop10 server-box2 serverInstance-drop">'+
					  '		 <div id="'
						+ serverIPAddress + "_" +serverInstanceName + "_del_btn"
						+ '"> <a class="ion-ios-close-empty remove-block" id="'+serverInstanceDBId+'_delete" onclick="deleteInstanceFromGUI(\'' + serverInstanceDBId + '\')"></a></div>' +
					  '					<img src="img/server-instance.png" class="mtop10 pull-left mright10" alt="" height="30%" width="30%" />' +
					  '					<div class="mtop10 orange dis-inline" id="serverIP_'+serverInstanceDBId+'"><p><strong>'+ serverType+ '<br>' + serverIPAddress + '<br>'+
					  						+ port + '</strong></p></div></div>'+
					  '					<a href="#" style="margin-left: 15px;" class="black" id="link_'+serverInstanceDBId+'" style="text-decoration:none;">' + serverInstanceName + '</a>' +
					  '			</td>' + 
					  '			<td style="width: 80%;" class=""  id="service_'+serverInstanceDBId+'_droppable_td">' +
					  
					  '		 <div id="'+ serverIPAddress + "_" +serverInstanceName + "_svc_drop_space"+
					  '">'+
					  '<div class="col-md-6 mtop10 ui-droppable ui-sortable" id="service_'+serverInstanceDBId+'_droppable">' +
		          	  '					<div class="grey-bg box-border padding15 text-center">' +
		              '						<em><strong>' + jsLocaleMsg.dragServiceInstanceStrongId + '</strong></em>' +
		              '					</div>' +
		          	  '				</div>' +
		          	'				</div>' +
		          	  '			</td>' +
					  '		</tr>' + 
					  '</table></div>').prependTo("#cart-added");
		}
	}else{
		if(accessright == true){
			$('<div class="box box-warning" id="box_'+serverInstanceDBId+'" value="panel1">'+
					'<div class="box-header with-border">'+
					'<h3 class="box-title header_text" id="title_'+serverInstanceDBId+'">' + serverInstanceName + " - " + serverIPAddress + '</h3>'+
					'<div class="box-tools pull-right" style="height:100%">'+
					'<button class="btn btn-box-tool block-collapse-btn" data-toggle="collapse" data-parent="#create-server-block" href="#' + serverInstanceDBId + '_block"><i class="fa fa-minus"></i></button></div></div>'+
					'<div id="' + serverInstanceDBId + '_block" class="box-body accordion-body collapse in">'+
					'<table style="border-collapse: collapse; width: 100%;" id="serverInstanceTable" >' +
					  '		<tr >' + 
					  
					  '			<td class="padding10 border-right1" style="width: 20%;"" id="' + serverInstanceDBId + '" >' + 
					  '	<div id="loader_'+serverInstanceDBId+'"><img src="img/preloaders/circle-red.gif" width="15px" data-toggle="tooltip" data-placement="bottom" title="Loading Status.." height="15px" style="vertical-align: middle;position: absolute;z-index: 99999;margin-top: 37px;"></div>' +
					  '				<div class="mtop10 server-box2 serverInstance-drop">'+
					  '		 <div id="'
						+ serverIPAddress + "_" +serverInstanceName + "_del_btn"
						+ '"> <a class="ion-ios-close-empty remove-block" id="'+serverInstanceDBId+'_delete" onclick="deleteInstanceFromGUI(\'' + serverInstanceDBId + '\')"></a></div>' +
					  '					<img src="img/server-instance.png" class="mtop10 pull-left mright10" alt="" height="30%" width="30%" />' +
					  '					<div class="mtop10 orange dis-inline" id="serverIP_'+serverInstanceDBId+'"><p><strong>'+ serverType+ '<br>' + serverIPAddress + '<br>'+
					  						+ port + '</strong></p></div></div>'+
					  ' <div id="'
					  + serverIPAddress + "_" +serverInstanceName + "_detail_btn"
					   + '"> <a href="#" style="margin-left: 15px;" class="black" id="link_'+serverInstanceDBId+'" onclick="viewServerInstance(\'' + serverInstanceDBId + '\')">' + serverInstanceName + '</a></div>' +
					  '			</td>' +
					  //'</div'+
					  '			<td style="width: 80%;" class=""  id="service_'+serverInstanceDBId+'_droppable_td">' +
					 /* '				<div class="col-md-6 mtop10 ui-droppable ui-sortable" id="service_'+serverInstanceDBId+'_droppable">' +
		          	  '					<div class="grey-bg box-border padding15 text-center">' +
		              '						<em><strong>' + jsLocaleMsg.dragServiceInstanceStrongId + '</strong></em>' +
		              '					</div>' +
		          	  '				</div>' +*/
		          	  '			</td>' +
					  '		</tr>' + 
					  '</table></div>').prependTo("#cart-added");

		}else{
			$('<div class="box box-warning" id="box_'+serverInstanceDBId+'" value="panel1">'+
					'<div class="box-header with-border")">'+
					'<h3 class="box-title header_text" id="title_'+serverInstanceDBId+'">' + serverInstanceName + " - " + serverIPAddress + '</h3>'+
					'<div class="box-tools pull-right" style="height:100%">'+
					'<button class="btn btn-box-tool block-collapse-btn" data-toggle="collapse" data-parent="#create-server-block" href="#' + serverInstanceDBId + '_block"><i class="fa fa-minus"></i></button></div></div>'+
					'<div id="' + serverInstanceDBId + '_block" class="box-body accordion-body collapse in">'+
					'<table style="border-collapse: collapse; width: 100%;" id="serverInstanceTable" >' +
					  '		<tr >' + 
					  
					  '			<td class="padding10 border-right1" style="width: 20%;"" id="' + serverInstanceDBId + '" >' + 
					  '	<div id="loader_'+serverInstanceDBId+'"><img src="img/preloaders/circle-red.gif" width="15px" data-toggle="tooltip" data-placement="bottom" title="Loading Status.." height="15px" style="vertical-align: middle;position: absolute;z-index: 99999;margin-top: 37px;"></div>' +
					  '				<div class="mtop10 server-box2 serverInstance-drop">'+
					  '		 <div id="'
						+ serverIPAddress + "_" +serverInstanceName + "_del_btn"
						+ '"> <a class="ion-ios-close-empty remove-block" id="'+serverInstanceDBId+'_delete" onclick="deleteInstanceFromGUI(\'' + serverInstanceDBId + '\')"></a></div>' +
					  '					<img src="img/server-instance.png" class="mtop10 pull-left mright10" alt="" height="30%" width="30%" />' +
					  '					<div class="mtop10 orange dis-inline" id="serverIP_'+serverInstanceDBId+'"><p><strong>'+ serverType+ '<br>' + serverIPAddress + '<br>'+
					  						+ port + '</strong></p></div></div>'+
					  '					<a href="#" style="margin-left: 15px;" class="black" id="link_'+serverInstanceDBId+'" style="text-decoration:none;">' + serverInstanceName + '</a>' +
					  '			</td>' + 
					  '			<td style="width: 80%;" class=""  id="service_'+serverInstanceDBId+'_droppable_td">' +
					 /* '				<div class="col-md-6 mtop10 ui-droppable ui-sortable" id="service_'+serverInstanceDBId+'_droppable">' +
		          	  '					<div class="grey-bg box-border padding15 text-center">' +
		              '						<em><strong>' + jsLocaleMsg.dragServiceInstanceStrongId + '</strong></em>' +
		              '					</div>' +
		          	  '				</div>' +*/
		          	  '			</td>' +
					  '		</tr>' + 
					  '</table></div>').prependTo("#cart-added");
		}
	}
	
	
	loadInstanceStatusGUI(serverInstanceDBId);	
	assignServiceDroppableEvent("service_" + serverInstanceDBId + "_droppable",serverInstanceDBId, serverIPAddress);
	
	initializeFancyBox();
}


var serverInstanceDragId = '',serviceType='',label='',servicelabel='';
function assignServiceDroppableEvent(droppableDivId,serverInstanceID, ipAddress){
	$( "#" + droppableDivId).droppable({
		activeClass: "ui-state-default",
		hoverClass: "ui-state-hover-server-instance",
		accept: ":not(.ui-sortable-helper)",
		drop: function( event, ui ) {
			var draggedText = $(ui.draggable).attr("id");
			
			
			if(draggedText.indexOf("Services") >= 0){
				var divId = this.id;
				
				servicelabel=$(ui.draggable).children("div").children("label").children("strong").text();
				servicelabel=servicelabel.trim();
				serviceType = $(ui.draggable).find('img').attr('id');
                
            	$("#serverInstanceID").val(serverInstanceID);
            	$("#serviceTypeAlias").val(serviceType);
            	$("#servicelabel").val(servicelabel);
	            
				$("#addServiceAnchor").click();
			}
		}
	});
}

function addNewService(serviceId, servInstanceId, serviceLabel,serviceType,serviceName,accessRightStatus,deleteRightsStatus,serverInstanceDBId,isConfiguredService){
	
	var shortServiceName = serviceName;
	if(serviceName.length > 35){
		shortServiceName = serviceName.substring(0, 35) + "...";
	}
	
	if(accessRightStatus == true){
		if(deleteRightsStatus == true){ // update and delete both rights
			$("#service_"+serverInstanceDBId+"_droppable_td").append(
					
					'<div class="col-md-6 mtop10">' +
					'	<div id="loader_'+serviceId+'"><img src="img/preloaders/circle-red.gif" width="15px" data-toggle="tooltip" data-placement="bottom" title="Loading Status.." height="15px" style="vertical-align: middle;position: absolute;z-index: 99999;margin-top: 25px;"></div>' +
					'	<div class="box-border padding10 pos-relative server-box2" style="overflow:hidden;">' +
					' <div id="'+serviceName + "_svc_delete_btn"+
					'">	<a class="ion-ios-close-empty remove-block" onclick="deletePopup('+" 'create_service','" + serviceId+ "','" + servInstanceId + "','"+serviceName+"' "+');"></a></div>' +
					'		<img src="img/'+serviceLabel+'.png" class="pull-left mright10" height="10%" width="10%" alt="" />' +
					'		<div>' +
					'			<p class="mtop0" style="margin-bottom:0px !important;">' +
					
					serviceLabel +
					
					'			</p>' +
					'		 <div id="'+serviceName + "_svc_detail"
					+ '">		<a href="#" class="black" data-toggle="modal" rel="tooltip" title="'+serviceName+'" onclick="viewService('+"'" + serviceId+ "','"+serviceType+"','"+serviceName+"','"+serverInstanceDBId+"'"+')">'					
					+ shortServiceName +
					'			</a>' +
					'		</div>' +
					'		</div>' +
					'	</div>' +
					'</div>');
		}else{ // only update , no delete rights
			$("#service_"+serverInstanceDBId+"_droppable_td").append(
					
					'<div class="col-md-6 mtop10">' +
					'	<div id="loader_'+serviceId+'"><img src="img/preloaders/circle-red.gif" width="15px" data-toggle="tooltip" data-placement="bottom" title="Loading Status.." height="15px" style="vertical-align: middle;position: absolute;z-index: 99999;margin-top: 25px;"></div>' +
					'	<div class="box-border padding10 pos-relative server-box2">' +
					'		<img src="img/'+serviceLabel+'.png" class="pull-left mright10" height="10%" width="10%" alt="" />' +
					'		<div>' +
					'			<p class="mtop0" style="margin-bottom:0px !important;">' +
					
					serviceLabel +
					
					'			</p>' +
					'		 <div id="'+serviceName + "_svc_detail"
					+ '">		<a href="#" class="black" onclick="viewService('+"'" + serviceId+ "','"+serviceType+"','"+serviceName+"','"+serverInstanceDBId+"'"+')">'
						+serviceName +
					'			</a>' +
					'		</div>' +
					'		</div>' +
					'	</div>' +
					'</div>');
		}
		
		
	}else{
		
		if(deleteRightsStatus == true){ // no update, only delete rights
			$("#service_"+serverInstanceDBId+"_droppable_td").append(
					
					'<div class="col-md-6 mtop10">' +
					'	<div id="loader_'+serviceId+'"><img src="img/preloaders/circle-red.gif" width="15px" data-toggle="tooltip" data-placement="bottom" title="Loading Status.." height="15px" style="vertical-align: middle;position: absolute;z-index: 99999;margin-top: 25px;"></div>' +
					'	<div class="box-border padding10 pos-relative server-box2">' +
					' <div id="'+serviceName + "_svc_delete_btn"+
					'">	<a class="ion-ios-close-empty remove-block" onclick="deletePopup('+" 'create_service','" + serviceId+ "','"+serviceName+"' "+');"></a></div>' +
					'		<img src="img/'+serviceLabel+'.png" class="pull-left mright10" height="10%" width="10%" alt="" />' +
					'		<div>' +
					'			<p class="mtop0" style="margin-bottom:0px !important;">' +
					
					serviceLabel +
					
					'			</p>' +
					'			<a href="#" class="black" style="text-decoration:none;" >'
						+serviceName +
					'			</a>' +
					'		</div>' +
					'	</div>' +
					'</div>');
		}else{ // no update , no delete rights
			$("#service_"+serverInstanceDBId+"_droppable_td").append(
					
					'<div class="col-md-6 mtop10">' +
					'	<div id="loader_'+serviceId+'"><img src="img/preloaders/circle-red.gif" width="15px" data-toggle="tooltip" data-placement="bottom" title="Loading Status.." height="15px" style="vertical-align: middle;position: absolute;z-index: 99999;margin-top: 25px;"></div>' +
					'	<div class="box-border padding10 pos-relative server-box2">' +
					'		<img src="img/'+serviceLabel+'.png" class="pull-left mright10" height="10%" width="10%" alt="" />' +
					'		<div>' +
					'			<p class="mtop0" style="margin-bottom:0px !important;">' +
					
					serviceLabel +
					
					'			</p>' +
					'			<a href="#" class="black" style="text-decoration:none;" >'
						+serviceName +
					'			</a>' +
					'		</div>' +
					'	</div>' +
					'</div>');
		}
		
	}
	
	$('#'+serverInstanceDBId+'_delete').addClass('disabled');
	initializeFancyBox();
	loadServiceStatusGUI(serviceId,'create');

}

function viewService(serviceId,serviceType,serviceName,instanceId){
	
	$("#serviceId").val(serviceId);	
	$("#serviceType").val(serviceType);
	$("#serviceName").val(serviceName);
	$("#instanceId").val(instanceId);
	$("#service_form").submit();
}

function addNewServiceToDB(updateServiceAccessStatus,deleteServiceAccessRights){
	var str = $("#create-service_form").serialize();
	
	//resetWarningDisplay();
	//showErrorMsg('');
	$("#create-service-buttons-div").hide();
	showProgressBar();
	parent.resizeFancyBox();
	$.ajax({
		url: 'addService',
		cache: false,
		async: true, 
		dataType: 'json',
		type: "POST",
		data: str,
		/*{
			"name" 						:	$("#serviceName").val(),
			"description" 				:	$("#serviceDescription").val(),
			"serverInstanceID" 			:	$("#serverInstanceID").val(),
			"serviceType" 				:	$("#serviceType").val()
			
		},*/
		success: function(data){
			hideProgressBar();

			var response = eval(data);
			
			var responseCode = response.code; 
			var responseMsg = response.msg; 
			var responseObject = response.object;
			
			if(responseCode == "200"){
				parent.addNewService(responseObject,responseObject,$("#servicelabel").val(),$("#serviceTypeAlias").val(),$("#name").val(),updateServiceAccessStatus,deleteServiceAccessRights,$("#serverInstanceID").val(),false);
				parent.closeFancyBoxFromChildIFrame();
				parent.resizeFancyBox();
				
			}else if(responseObject != undefined && responseObject != 'undefined' && responseCode == "400"){
				
				$("#create-service-buttons-div").show();
				
				addErrorIconAndMsgForAjaxPopUp(responseObject);
			}else{
				resetWarningDisplayPopUp();
				showErrorMsgPopUp(responseMsg);
				$("#create-service-buttons-div").show();
				parent.resizeFancyBox();
			}
		},
	    error: function (xhr,st,err){
	    	hideProgressBar();
	    	$("#create-service-buttons-div").show();
			handleGenericError(xhr,st,err);
		}
	});
}

function deleteInstanceFromGUI(instanceId){
	
			if ($('#'+instanceId+'_delete').hasClass('disabled'))
            {
				$("#removeAllow").hide();
				$("#removeNotAllow").show();
				$("#remove-buttons-div").hide();
				$("#removeNot-buttons-div").show();
				$("#importantNote").hide();
            }else{
            	$("#removeAllow").show();
				$("#removeNotAllow").hide();
				$("#remove-buttons-div").show();
				$("#removeNot-buttons-div").hide();
            	$("#removeInstanceId").val(instanceId);
            	$("#importantNote").show();
            }
			$("#deleteServerInstance").click();
	
}

function uploadAndImportServiceInstance(actionUrl , serviceInstanceId, serverId, importMode){
	var oMyForm = new FormData();
    oMyForm.append("file", files[0]);
    oMyForm.append("importServiceInstanceId", serviceInstanceId);
    oMyForm.append("serverId", serverId);
    oMyForm.append("importMode", importMode);
    // check for file size length
    clearResponseMsgPopUp();
    $('#import-config-progress-bar-div').show();
    $('#importServiceInstance').hide();
    
    if(importMode > 0) {
    	$('#importServiceInstanceAdd').hide();
        $('#importServiceInstanceUpdate').hide();
        $('#importServiceInstanceKeepboth').hide();
        $('#importServiceInstanceOverwrite').hide();
    }
    
    
    $('#import-popup-close-btn').hide();
    $.ajax({dataType : 'json',
          url : actionUrl,
          data : oMyForm ,
          type : "POST",
          enctype: 'multipart/form-data',
          processData: false,
          contentType:false,
          success : function(response) {
  			var responseCode = response.code;
  			var responseMsg = response.msg;
  			var responseObject = response.object;
  			if(responseCode === "200"){
  				$('#import-popup-close-btn').show();
  	        	$('#import-config-progress-bar-div').hide();
  	        	
  	        	closeFancyBox();
  	        	parent.resizeFancyBox();
  	        	location.reload();
			}else if(responseObject !== undefined && responseObject !== 'undefined'){
				showErrorMsgPopUp(responseMsg);
				$("#importContent").hide();
				$("#divValidationList").html('');
				$('#importServiceInstance').hide();
				
				if(importMode > 0) {
			    	$('#importServiceInstanceAdd').hide();
			        $('#importServiceInstanceUpdate').hide();
			        $('#importServiceInstanceKeepboth').hide();
			        $('#importServiceInstanceOverwrite').hide();
			    }
				
				$('#import-popup-close-btn').show();
  	        	$('#import-config-progress-bar-div').hide();
  	        	var tableString ='<table class="table table-hover">';
  	        	var jsonObjectLength=Object.keys(responseObject[0]).length;
  	        	if(jsonObjectLength === 2){
  	        		//Length is 2 , when XSD validation fail
  	        		tableString += "<tr>";
  					tableString += "<th>"+jsLocaleMsg.LineNo+"</th>";
  					tableString += "<th>"+jsLocaleMsg.errorMessage+"</th>";
  					tableString += "</tr>";
  	        	}else if(jsonObjectLength === 5){
  	        		//Length is 4 , when Entity validation fail
  	        		tableString += "<tr>";
  	        		tableString += "<th>"+jsLocaleMsg.moduleName+"</th>";
  					tableString += "<th>"+jsLocaleMsg.entityName+"</th>";
  					tableString += "<th>"+jsLocaleMsg.propertyName+"</th>";
  					tableString += "<th>"+jsLocaleMsg.propertyValue+"</th>";
  					tableString += "<th>"+jsLocaleMsg.errorMessage+"</th>";
  					tableString += "</tr>";
  	        	}
				$.each(responseObject, function(index,responseObject) {
					if(jsonObjectLength === 2){
						tableString += "<tr>";
						tableString += "<td>"+responseObject[0]+"</td>";
						tableString += "<td>"+responseObject[1]+"</td>";
						tableString += "</tr>";
					}else if(jsonObjectLength === 5){
						tableString += "<tr>";
						tableString += "<td>"+responseObject[0]+"</td>";
						tableString += "<td>"+responseObject[1]+"</td>";
						tableString += "<td>"+responseObject[2]+"</td>";
						tableString += "<td>"+responseObject[3]+"</td>";
						tableString += "<td>"+responseObject[4]+"</td>";
						tableString += "</tr>";
					}
					
				});
				tableString += "</table>"
				$("#divValidationList").html(tableString);
				resetWarningDisplay();
				parent.resizeFancyBox();
			}else{
				$('#importServiceInstance').show();
				$('#import-popup-close-btn').show();
				
				if(importMode > 0) {
			    	$('#importServiceInstanceAdd').hide();
			        $('#importServiceInstanceUpdate').hide();
			        $('#importServiceInstanceKeepboth').hide();
			        $('#importServiceInstanceOverwrite').show();
			    }
				
  	        	$('#import-config-progress-bar-div').hide();
  	        	if(!responseMsg || responseMsg === null || responseMsg === '') {
  	        		showErrorMsgPopUp("File upload fail. check logs for more detail.");
  	        	} else {
  	        		showErrorMsgPopUp(responseMsg);
  	        	}
				parent.resizeFancyBox();
			}
          },
          error : function(response){
        	$('#importServiceInstance').show();
        	
        	if(importMode > 0) {
		    	$('#importServiceInstanceAdd').hide();
		        $('#importServiceInstanceUpdate').hide();
		        $('#importServiceInstanceKeepboth').hide();
		        $('#importServiceInstanceOverwrite').show();
		    }
        	
        	$('#import-popup-close-btn').show();
          	$('#upload-progress-bar-div').hide();
          }
      });	
	
}

function showProgressBar(){
	$("#buttons-div").hide();
	$("#progress-bar-div").show();
}

function hideProgressBar(){
	$("#buttons-div").show();
	$("#progress-bar-div").hide();	
}


var actionLocation;
function deletePopup(tabType,id,servInstanceId,name){
	actionLocation = tabType;
	clearAllMessages();
	if(id !== ''){
		ckIntanceSelected[0] = id;
	}
	
	$("#btnDeleteClose-server-instance").hide();
	$("#btnDeleteClose-service-mgmt").hide();
	$("#btnDeleteClose-create-service").hide();
	
	$("#divDeleteMsg").html('');
	$("#btnExportDeletePopup").show();
	$("#btnDeleteCancel").show();
	$("#btnDeletePopup").show();
	$("#btnExportDeletePopup").prop('disabled', false);
	$("#btnDeletePopup").prop('disabled', false);
	$("#btnDeletePopup").show();
	$("#delServiceImpNote").show();
	
	if(ckIntanceSelected.length === 0){
		$("#lessWarn").show();
		$("#moreWarn").hide();
		$("#deleteWarnMsg").click();
		return;
	}else if(ckIntanceSelected.length > 1){
		$("#lessWarn").hide();
		$("#moreWarn").show();
		$("#deleteWarnMsg").click();
		return;
	}else{
	
		$("#tblDeleteInstanceList").html('');
		var rowId='',tableString = '';
		var serviceId , serviceName,toggleId;
		
		if(tabType === 'service_mgmt' ){

			 rowId = ckIntanceSelected[0];
			 serviceId= jQuery('#serviceInstanceList').jqGrid ('getCell',rowId, 'servInstanceId' );
			 serviceName = $(jQuery('#serviceInstanceList').jqGrid ('getCell', rowId, 'serviceName')).closest("a").html();

		}else if(tabType === 'server_instance'){

			rowId = ckIntanceSelected[0];
			ckIntanceSelected.push(rowId);
			
			serviceId= jQuery('#summaryServiceList').jqGrid ('getCell',rowId, 'servInstanceId' );
			serviceName = $(jQuery('#summaryServiceList').jqGrid ('getCell', rowId, 'name')).closest("a").html();
			
		}else if(tabType === 'create_service'){
			serviceId= servInstanceId;
			serviceName = name;
		}
		
		
		$("#tblDeleteInstanceList").html('');
		tableString ='<table class="table table-hover" style="width:100%">';
			tableString += "<tr>";
			tableString += "<th>"+jsLocaleMsg.serviceId+"</th>";
			tableString += "<th>"+jsLocaleMsg.serviceNm +"</th>";
			tableString += "</tr>";
			tableString += "<tr>";
			tableString += "<td>"+serviceId+"</td>";
			tableString += "<td>"+serviceName+"</td>";
			tableString += "</tr>";
			tableString += "</table>"
		$("#tblDeleteInstanceList").html(tableString);
		$('#deleteServicePopup').click();
	}		
}

function DeleteServiceInstance(actionUrl){
	clearAllMessages();
	$("#delete-progress-bar-div").show();
	$("#delete-buttons-div").hide();

	$.ajax({
				url: actionUrl,
			    cache: false,
				async: true,
				dataType: 'json',
				type: "POST",
				data: {
					'exportServiceInstanceId': ckIntanceSelected[0],
				},
				success: function(data){
					$("#delete-buttons-div").show();
					$("#delete-progress-bar-div").hide();
					var response = eval(data);
			    	response.msg = decodeMessage(response.msg);
			    	response.msg = replaceAll("+"," ",response.msg);
			    	
			    	$("#divDeleteMsg").html(response.msg);
			    	$("#btnDeleteClose-server-instance").hide();
			    	$("#btnDeleteClose-service-mgmt").hide();
			    	$("#btnDeleteClose-create-service").hide();

			    	if(response.code === 200 || response.code === "200") {
			    		clearResponseMsgDiv();
			    		clearResponseMsg();
			    		clearErrorMsg();
			    		
			    		var toggleId = ckIntanceSelected[0];
			    		var exportServiceId = toggleId;
						var responseCode = data.code;
		    			var response = data.object;

		    			$(".status").show();
		    			
		    			$("#btnExportDeletePopup").hide();
		    			$("#btnDeletePopup").hide();
		    			$("#btnDeleteCancel").hide();
		    			$("#delServiceImpNote").hide();
	    			
	    			    if(actionLocation === 'service_mgmt' ){
	    					$("#btnDeleteClose-service-mgmt").show();
				    	}else if(actionLocation === 'server_instance'){
				    		$("#btnDeleteClose-server-instance").show();
				    	}else if(actionLocation === 'create_service'){
				    		$("#btnDeleteClose-create-service").show();
				    	}
	    			    
	    			    exportServiceInstanceBeforeDelete(response,exportServiceId);

			    	}
				    	
					},
				    error: function (xhr,st,err){
						$("#delete-buttons-div").show();
						$("#delete-progress-bar-div").hide();
				    	handleGenericError(xhr,st,err);
				    	
				    	if(actionLocation == 'service_mgmt' ){
		    					$("#btnDeleteClose-service-mgmt").show();
						    	searchInstanceCriteria();
				    	}else if(actionLocation == 'server_instance'){
				    		$("#btnDeleteClose-server-instance").show();
				    		clearServiceSummaryInstanceGrid();
				    	}
					}
			
			}); 
	} 

function reloadInstanceGridData(){
	var $grid = $("#summaryServiceList");
	$grid.jqGrid('setGridParam',{datatype:'json',page: 1}).trigger('reloadGrid');
}

function profileServiceList(){
	
	var selectedServerinstance=$("#selServerInstance").find(":selected").val();
	
	$("#profileserverInstanceId").val(selectedServerinstance);
	$("#profile_service_form").submit();
	
}


