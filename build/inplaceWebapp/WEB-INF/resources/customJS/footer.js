function replaceAll(find, replace, str){
	while( str.indexOf(find) > -1){
		str = str.replace(find, replace);
	}
	return str;
}

function decodeMessage(value){
	var tempValue = decodeURIComponent(value);
	tempValue = replaceAll("+"," ",tempValue);
	return tempValue;
}

function handleGenericError(xhr,st,err){
	if(xhr.responseText === 'SESSION_TIMEOUT'){
		window.location.href = 'login-fail?error-timeout' ;
	}else if(xhr.responseText === 'ACCESS_DENIED'){
		window.location.href = 'error403' ;
	}
}

function startsWith(str, prefix) {
    return str.lastIndexOf(prefix, 0) === 0;
}
 
function endsWith(str, suffix) {
    return str.indexOf(suffix, str.length - suffix.length) !== -1;
}

function setCookie(cname, cvalue, exdays) {
	var d = new Date();
	d.setTime(d.getTime() + (exdays*24*60*60*1000));
	var expires = "expires="+d.toUTCString();
	document.cookie = cname + "=" + cvalue + "; " + expires;
}

function getCookie(cname) {
	var name = cname + "=";
	var ca = document.cookie.split(';');
	for(var i=0; i<ca.length; i++) {
		var c = ca[i];
		while (c.charAt(0)===' ') c = c.substring(1);
		if (c.indexOf(name) === 0) return c.substring(name.length,c.length);
	}
	return "";
}

function initializeFancyBox() {
	
	try {
		$('.fancybox').fancybox({
			maxWidth : 1100,
			maxHeight : 800,
			fitToView : false,
			/*width : '90%',*/
			height : 'auto',
			autoSize : false,
			closeClick : false,
			openEffect : 'elastic',
			closeEffect : 'elastic',
			tpl : {
				closeBtn : ''
			},
			helpers     : {
				overlay : {closeClick: false} // prevents closing when clicking OUTSIDE fancybox
			},
			keys : {
				close  : null
			}
		});
	} catch(err) {
		console.log("Error : "+err);
	}
	/*$('.fancybox').fancybox({
		maxWidth	: 600,
		//maxHeight	: 600,
		fitToView	: false,
		width		: '90%',
		//height		: '80%',
		autoSize	: true,
		closeClick	: false,
		openEffect	: 'none',
		closeEffect	: 'none'
			
	});*/
	
	/*$('.fancybox').fancybox({
		maxWidth : 1100,
		maxHeight : 400,
		fitToView : false,
		width : '90%',
		height : 'auto',
		autoSize : true,
		closeClick : false,
		openEffect : 'elastic',
		closeEffect : 'elastic',
	    tpl : {
	    	closeBtn : ''
	    },
	    helpers     : {
	        overlay : {closeClick: false} // prevents closing when clicking OUTSIDE fancybox
	    },
	    keys : {
	    	close  : null
	    }
	});*/
}

function resizeFancyBox(){
	$.fancybox.update();
}

function closeFancyBox (){
	$.fancybox.close();
}

function initializeTooltip(){
	$('[data-toggle="tooltip"]').tooltip();
	$(".leftTooltip").tooltip({
	    placement: "left"
	});
}

function toggleSidebar() {
	if ($(window).width() < 767) {
		$(".sidebar-toggle").show();
	}

	if ($(window).width() > 768) {
		$(".sidebar-toggle").hide();
		$("body").addClass("sidebar-collapse").removeClass("sidebar-open");
		$(".sidebar").hover(function() {
			$("body").removeClass("sidebar-collapse");
		});
		$(".sidebar").mouseleave(function() {
			$("body").addClass("sidebar-collapse");
		});
	}
}

function initializeScrollBar() {
	var headerHeight = $(".main-header").outerHeight();
	var footerHeight = $(".main-footer").outerHeight();
	$('#content-scroll-d')
			.css(
					{
						'height' : (($(window).height()) - footerHeight
								- headerHeight - 10)
								+ 'px'
					});
	if($('.page-section').length){
		$('.page-section').css({'height':(($(window).height())- headerHeight - footerHeight  ) -120 +'px'});
	}
}

function addErrorMsgToInputTag(inputId, errorMsg){
	$("#" + inputId).next().children().first().attr("title",errorMsg);
	$("#" + inputId).next().children().first().attr("data-original-title",errorMsg);
}

function addErrorClassWhenTitleExist(genericErrorMessage) {
	var errorExists = false;
	$('i')
			.each(
					function() {
						if ($(this).attr('data-original-title') != ''
								&& $(this).attr('data-original-title') != 'undefined'
								&& $(this).attr('data-original-title') != undefined) {
							$(this).parent().parent().parent()
									.addClass('error');
							try {
								$(this).attr('id',$(this).parent().prev().attr('id')+"_error");
							} catch(err) {
							    console.log("not able to set error id");
							}
							
							errorExists = true;
						}
					});
	if (errorExists && genericErrorMessage!='') {
		showErrorMsg(genericErrorMessage);
	}
}
function addErrorClassWhenTitleExistPopUp(genericErrorMessage) {
	var errorExists = false;
	$('i')
			.each(
					function() {
						if ($(this).attr('data-original-title') != ''
								&& $(this).attr('data-original-title') != 'undefined'
								&& $(this).attr('data-original-title') != undefined) {
							$(this).parent().parent().parent()
									.addClass('error');
							errorExists = true;
						}
					});
	if (errorExists && genericErrorMessage!='') {
		showErrorMsgPopUp(genericErrorMessage);
	}
}

function resetWarningDisplay(){
	try {
		clearAllMessages();
	}catch(err) {
    	// do nothing
    }
    
	$('i').each(function(){
		var isErrorExist = false;
		if($(this).attr('data-original-title') != '' 
				&& $(this).attr('data-original-title') != 'undefined' && $(this).attr('data-original-title') != undefined
				){
			$(this).attr('data-original-title','');
			$(this).parent().parent().parent().removeClass('error');
			isErrorExist = true;
		}
		if(isErrorExist){
			var currentURL = $("#JSP_PAGE_NAME").val();
			if(currentURL!=undefined && currentURL.indexOf("systemParameterManager.jsp")!=-1 && $("#sysParamDBValue").children().length > 0){
				//resetSysParamDBValues();	
			}
		}
	});
}

function resetWarningDisplayPopUp(){
	try {
		clearAllMessagesPopUp();
	}catch(err) {
    	// do nothing
    }
    
	$('i').each(function(){
		var isErrorExist = false;
		if($(this).attr('data-original-title') != '' 
				&& $(this).attr('data-original-title') != 'undefined' && $(this).attr('data-original-title') != undefined
				){
			$(this).attr('data-original-title','');
			$(this).parent().parent().parent().removeClass('error');
			isErrorExist = true;
		}
		if(isErrorExist){
			var currentURL = $("#JSP_PAGE_NAME").val();
			if(currentURL!=undefined && currentURL.indexOf("systemParameterManager.jsp")!=-1 && $("#sysParamDBValue").children().length > 0){
				resetSysParamDBValues();	
			}
		}
	});
}


function removeLastElement() {
	$('.spanBreadCrumb a:last').remove();
}

function appendLastElement(html) {
	removeLastElement();
	$('.spanBreadCrumb').append(
			'<a data-toggle="dropdown" href="#">' + html + '</a>');
}

function isNumericOnKeyDown(e){
	
	// Allow: backspace, delete, tab, escape, enter and .
    if ($.inArray(e.keyCode, [46, 8, 9, 27, 13, 110, 190]) !== -1 ||
         // Allow: Ctrl+A
        (e.keyCode == 65 && e.ctrlKey === true) ||
         // Allow: Ctrl+C
        (e.keyCode == 67 && e.ctrlKey === true) ||
         // Allow: Ctrl+X
        (e.keyCode == 88 && e.ctrlKey === true) ||
         // Allow: home, end, left, right
        (e.keyCode == 109) ||
        //Allow minus sign in numeric values.
        (e.keyCode >= 35 && e.keyCode <= 39)) {
             // let it happen, don't do anything
             return;
    }
    // Ensure that it is a number and stop the keypress
    if ((e.shiftKey || (e.keyCode < 48 || e.keyCode > 57)) && (e.keyCode < 96 || e.keyCode > 105)) {
        e.preventDefault();
    }
}

function isNumericWithMinusOneOnKeyDown(e){
	// Allow: backspace, delete, tab, escape, enter and .
    if ($.inArray(e.keyCode, [46, 8, 9, 27, 13, 110, 190]) !== -1 ||
         // Allow: Ctrl+A
        (e.keyCode == 65 && e.ctrlKey === true) ||
         // Allow: Ctrl+C
        (e.keyCode == 67 && e.ctrlKey === true) ||
         // Allow: Ctrl+X
        (e.keyCode == 88 && e.ctrlKey === true) ||
         // Allow: home, end, left, right
        (e.keyCode == 109) ||
        //Allow minus sign in numeric values.
        (e.keyCode >= 35 && e.keyCode <= 39)) {
             // let it happen, don't do anything
             return;
    }
    // Ensure that it is a number and stop the keypress
    if (e.keyCode!=189 && (e.shiftKey || (e.keyCode < 47 || e.keyCode > 57)) && (e.keyCode < 96 || e.keyCode > 105)) {
        e.preventDefault();
    }
}

function convertToIntergerValue(event){
	if(parseInt(event.target.value)>=-1)
		event.target.value=parseInt(event.target.value);
	else
		event.target.value=-1;
}

function getNumericInputTextField(id){
	var n = $("#" + id).val();
	if(!isNaN(parseFloat(n)) && isFinite(n)){
		return parseInt(n);
	}
	return 0;
}

function addErrorIconAndMsgForAjaxPopUp(responseObject){
	
		$.each(responseObject, function(key,val){
			
			$("#" + key).next().children().first().attr("data-original-title",val).attr("id",key+"_error");

			addErrorClassWhenTitleExistPopUp($("#GENERIC_ERROR_MESSAGE", window.parent.document).val());
		});
	
}

//pass top/bottom to scroll custom scrollbar.
function scrollTo(position) {
	$("#content-scroll-d").mCustomScrollbar("scrollTo", position, { callbacks:false});
}

function addErrorIconAndMsgForAjax(responseObject){
	
	$.each(responseObject, function(key,val){
		if (key.indexOf(".") >= 0)
			key = key.replace('.','_');
		
		$("#" + key).next().children().first().attr("data-original-title",val);

		addErrorClassWhenTitleExist($("#GENERIC_ERROR_MESSAGE", window.parent.document).val());
	});

}

function loadInstanceStatusGUI(serverInstanceId){
	
	$.ajax({
		url: 'loadServerInstanceStatus',
		cache: false,
		async: true,
		dataType: 'json',
		type: "POST",
		data: {
			"serverInstanceId": serverInstanceId
		},
		success: function(data){
			var response = eval(data);
			
			var responseObject = response.object;
			var instanceStatus = responseObject.status;
			var toggleId = serverInstanceId + "_" + instanceStatus;
			
			if(response.code == '200'){
				if(instanceStatus == 'ACTIVE'){
					$('#loader_'+serverInstanceId).remove();
	 			}else{
	 				$('#loader_'+serverInstanceId).next( ".server-box2" ).addClass("server-box3");
	 				$('#loader_'+serverInstanceId).remove();
	 			}
			} else {
				$('#loader_'+serverInstanceId).next( ".server-box2" ).addClass("server-box3");
 				$('#loader_'+serverInstanceId).remove();
			}
		},
	    error: function (xhr,st,err){
			handleGenericError(xhr,st,err);
		}
	});
}

function loadServiceStatusGUI(serviceId,tabType,serverInstanceId,rowObject,tabSubType){
	console.log("footer.js called 360: loadServiceStatusGUI(serviceId,tabType,serverInstanceId,rowObject,tabSubType); ");
	$.ajax({
		url: 'loadServiceStatus',
		cache: false,
		async: true,
		dataType: 'json',
		type: "POST",
		data: {
			"serviceId": serviceId
		},
		success: function(data){
			var response = eval(data);
			var toggleId = serviceId + "_" + response.msg;
			if(response.code==='200'){
				
				if(tabType === 'create'){
					if(response.msg === 'ACTIVE'){
						$('#loader_'+serviceId).remove();
		 			}else{
		 				$('#loader_'+serviceId).next( ".server-box2" ).addClass("server-box3");
		 				$('#loader_'+serviceId).remove();
		 			}
				}
				
				if(tabType === 'service_management'){
					if(rowObject != undefined)
						enableSyncAndStatusFromServerResponse(serviceId,tabType,serverInstanceId,rowObject,tabSubType);
					var responseObject = response.object;
					if (responseObject !== 'undefined' && responseObject !== null){
						var serverData = $.parseJSON(responseObject["serverInstance"]);
						var toggleIdDiv = serverData['name'] +"_"+ responseObject["name"]+"_toggle_on_btn";
					}else{
						toggleIdDiv = toggleId+"_div";
					}
					if(response.msg === 'ACTIVE'){
						$('#service_instance_status_loader_'+serviceId).html('<div id="'+toggleIdDiv+'"><input class="checkboxbg" id=' + toggleId + ' data-on="On" data-off="Off" checked type="checkbox" data-size="mini"></div>');
		 			}else{
		 				$('#service_instance_status_loader_'+serviceId).html('<div id="'+toggleIdDiv+'"><input class="checkboxbg" id=' + toggleId + ' data-on="On" data-off="Off" type="checkbox" data-size="mini"></div>');
		 			}
					
				}
				
				if(tabType === 'collection_summary' || tabType === 'netflow_collection_summary' || tabType === 'netflow_binary_collection_summary' ||  tabType === 'syslog_collection_summary' || tabType === 'iplog_service_summary' || tabType === 'parsing_service_summary' || tabType === 'distribution_summary' || tabType === 'processing_service_summary' || tabType === 'consolidation_service_summary' || tabType === 'aggregation_service_summary'){
					
					if(response.msg === 'ACTIVE'){
						$('#service_instance_status_loader_'+serviceId +' > img').remove();
						$('#service_instance_status_loader_'+serviceId).html("<strong>Status</strong>&nbsp;|&nbsp; <i class='fa fa-circle orangecolor' id='status_icon' data-toggle='tooltip' data-placement='bottom' title='ACTIVE'></i>");
						
						$('#stopBtn').show();
						$('#startBtn').hide();
		 			}else{
		 				
		 				$('#service_instance_status_loader_'+serviceId +' > img').remove();
						$('#service_instance_status_loader_'+serviceId).html("<strong>Status</strong>&nbsp;|&nbsp; <i class='fa fa-circle grey' id='status_icon' data-toggle='tooltip' data-placement='bottom' title='INACTIVE'></i>");
						$('#startBtn').show();
						$('#stopBtn').hide();
		 			}
				}
			} else {
				if(tabType === 'create'){
					$('#loader_'+serviceId).next( ".server-box2" ).addClass("server-box3");
					$('#loader_'+serviceId).remove();
				}
 				if(tabType === 'service_management'){
 					if(rowObject != undefined )
 						enableSyncAndStatusFromServerResponse(serviceId,tabType,serverInstanceId,rowObject,tabSubType);
 					var responseObject = response.object;
					if (responseObject !== 'undefined' && responseObject !== null){
						var serverData = $.parseJSON(responseObject["serverInstance"]);
						var toggleIdDiv = serverData['name'] +"_"+ responseObject["name"]+"_toggle_on_btn";
					}else{
						toggleIdDiv = toggleId+"_div";
					}
 					$('#service_instance_status_loader_'+serviceId).html('<div id="'+toggleIdDiv+'"><input class="checkboxbg" id=' + toggleId + ' data-on="On" data-off="Off" type="checkbox" data-size="mini"></div>');
 				}
 				
 				if(tabType === 'collection_summary' || tabType === 'netflow_collection_summary' || tabType === 'netflow_binary_collection_summary' ||  tabType === 'syslog_collection_summary' || tabType === 'iplog_service_summary' || tabType === 'parsing_service_summary' || tabType === 'distribution_summary' || tabType === 'processing_service_summary' || tabType === 'consolidation_service_summary'  || tabType === 'aggregation_service_summary'){
 					
 					$('#startBtn').show();
					$('#stopBtn').hide();
 					
 					$('#service_instance_status_loader_'+serviceId +' > img').remove();
					$('#service_instance_status_loader_'+serviceId).html("<strong>Status</strong>&nbsp;|&nbsp; <i class='fa fa-circle grey' id='status_icon' data-toggle='tooltip' data-placement='bottom' title='INACTIVE'></i>");
 				}
			}
			
			if(tabType === 'collection_summary' || tabType === 'netflow_collection_summary' || tabType === 'netflow_binary_collection_summary' ||  tabType === 'syslog_collection_summary' || tabType === 'iplog_service_summary' || tabType === 'parsing_service_summary' || tabType === 'distribution_summary' || tabType === 'processing_service_summary' || tabType === 'consolidation_service_summary' || tabType === 'aggregation_service_summary'){
				// set service running status in hidden param at top of page
				$("#serviceStatus").val(response.msg);
			}
			
			if(tabType === 'service_management'){
				
				
				$('#service_sync_'+serviceId).attr("onclick","syncServiceInstanceById('"+serviceId+"')");
				
	
				$('#enable_'+serviceId).attr("onclick","serviceActiveInactiveToggle2(this)");
				var gridCheckBoxes = $("tr#"+serviceId+".jqgrow > td > input.cbox:disabled", "");
				gridCheckBoxes.removeAttr("disabled");

				var $jqgrid = $("#summaryServiceList"); 

     			$(".jqgrow", $jqgrid).each(function (index, row) {
     		        var $row = $(row);
 		            //Find the checkbox of the row and set it disabled
 		           $row.find("input:checkbox").removeAttr("disabled");
 		           
 		         //  $row.find("input:xyz").removeAttr("disabled");
 		     
     		    });

     			
     			
				var $jqgrid = $("#serviceInstanceList");      
     			$(".jqgrow", $jqgrid).each(function (index, row) {
     		        var $row = $(row);
 		            //Find the checkbox of the row and set it disabled
 		            $row.find("input:checkbox").removeAttr("disabled");
     		    });
     			
				$('#serviceInstanceList .checkboxbg').bootstrapToggle();
				$('#summaryServiceList .checkboxbg').bootstrapToggle();
				
     			$('.checkboxbg').change(function(){
     				serviceActiveInactiveToggle2(this);	
					serviceActiveInactiveToggleChanged(this);			
				});
			}
		},
	    error: function (xhr,st,err){
			handleGenericError(xhr,st,err);
		}
	});
}

function enableSyncAndStatusFromServerResponse(serviceId,tabType,serverInstanceId,rowObject,tabSubType){
	if(tabSubType == 'serverInstanceSummary'){
		var toggleId = rowObject["id"] + "_db_" + rowObject["syncStatus"];
		var serviceId = rowObject["id"];
		var serviceName = rowObject["name"];
		var serviceInstanceId=rowObject["servInstanceId"];
		var service = rowObject["name"].replace(/ /g, "_");
		var serviceSyncDiv = service + "_service_sync_status";
		if(rowObject["syncStatus"] == false){
			$("#"+serviceId).find('td[aria-describedby="summaryServiceList_syncStatus"]').html('<div id="'+serviceSyncDiv+'"><img  src="img/orange.png"  alt="'+rowObject["syncStatus"]+'" onclick="setServiceSyncParameters('+"'"+serviceId + "','"+serviceName+"','"+serviceInstanceId+"'"+')"; style="cursor: pointer;" /></div>');
		}else{
			$("#"+serviceId).find('td[aria-describedby="summaryServiceList_syncStatus"]').html('<div id="'+serviceSyncDiv+'"><img src="img/grey.png" style="cursor: pointer;" alt="'+rowObject["syncStatus"]+'"/></div>');
		}
	}else{
		var syncDivId = rowObject["serverIpPort"].replace(':','_') + "_sync_btn";
		if(rowObject["sync_status"] == true){
			$("#"+serviceId).find('td[aria-describedby="serviceInstanceList_sync_status"]').html('<div id="'+syncDivId+'"><img id="synch_'+rowObject["id"]+'" src="img/grey.png" style="cursor: pointer;disabled:true"/></div>');
		}else{
			$("#"+serviceId).find('td[aria-describedby="serviceInstanceList_sync_status"]').html('<div id="'+syncDivId+'"><img id="synch_'+rowObject["id"]+'" src="img/orange.png" style="cursor: pointer;disabled:false;"  alt="'+rowObject["sync_status"]+'" onclick="syncServiceInstanceById('+"'" + rowObject["id"]+ "'"+');"/></div>');
		}
		$("#"+serviceId).find('td[aria-describedby="serviceInstanceList_"]').html("<input type='checkbox' name='"+rowObject["serverInstanceName"]+"_"+rowObject["serviceName"]+"_chkbox"+"' id='"+rowObject["serverInstanceName"]+"_"+rowObject["serviceName"]+"_chkbox"+"' onclick=\"addRowId(\'"+rowObject["serverInstanceName"]+"_"+rowObject["serviceName"]+"_chkbox"+"\', \'"+rowObject["id"]+"\')\"; />");
	}
	
	if(tabSubType == undefined || tabSubType == ''){
		var toggleId = "enable_"+rowObject["id"] + "_" + rowObject["enableStatus"];
		var toggleIdDiv = rowObject["serverInstanceName"] +"_"+ rowObject["serviceName"] + "_toggle_status_btn";
		if(rowObject["enableStatus"] == 'ACTIVE'){
			$("#"+serviceId).find('td[aria-describedby="serviceInstanceList_enableStatus"]').html('<div id="'+toggleIdDiv+'"><input class="checkboxbg" id=' + toggleId + ' data-on="Active" data-off="InActive" checked data-size="mini" type="checkbox"></div>');
		}else{
			$("#"+serviceId).find('td[aria-describedby="serviceInstanceList_enableStatus"]').html('<div id="'+toggleIdDiv+'"><input class="checkboxbg" id=' + toggleId + ' data-on="Active" data-off="InActive" data-size="mini" type="checkbox"></div>');
		}							
	} else {
		var toggleIdName = "enable_"+rowObject["id"] + "_" + rowObject["enableStatus"];
		var toggleId = "enable_"+rowObject["id"];
		var serviceName = rowObject["name"].replace(/ /g, "_");
		var toggleIdDiv = serviceName + "_service_enable_status";
		if(rowObject["enableStatus"] == 'ACTIVE'){
			$("#"+serviceId).find('td[aria-describedby="summaryServiceList_enableStatus"]').html('<div id="'+toggleIdDiv+'"><input class="checkboxbg" id=' + toggleId + ' name=' + toggleIdName + ' data-on="Active" data-off="InActive" checked data-size="mini" type="checkbox"></div>');
		}else{
			$("#"+serviceId).find('td[aria-describedby="summaryServiceList_enableStatus"]').html('<div id="'+toggleIdDiv+'"><input class="checkboxbg" id=' + toggleId + ' name=' + toggleIdName + ' data-on="Active" data-off="InActive" data-size="mini" type="checkbox"></div>');
		}	
	}
}