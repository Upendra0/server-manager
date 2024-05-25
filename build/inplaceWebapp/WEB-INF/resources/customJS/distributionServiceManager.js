$(document).on("change","#fileGroupingParameter-fileGroupEnable",function(event) {
   var fileGroup = $('#fileGroupingParameter-fileGroupEnable').val();
   disableFileGroupingParams(fileGroup);
});

function showButtons(tabType){
	$("#summaryBtnDiv").hide();
	$("#serviceConfigBtnDiv").hide();
	
	if(tabType === 'distribution_service_summary'){
		$("#summaryBtnDiv").show();
	}else if(tabType === 'distribution_service_configuration'){
		$("#serviceConfigBtnDiv").show();
	}else if (tabType === 'distribution_driver_configuration'){
		$("#summaryBtnDiv").hide();
		$("#serviceConfigBtnDiv").hide();
	}
}

function distributionServicesStartStopPopup(){
	$("#summary_start_stop_popup").click();	
}

function startService(){
	closeFancyBox();
}

/* Function will load distribution driver service summary page grid.*/
function loadDistributionDriverGrid(urlAction,totalPageCount, serviceId){
		$("#drivergrid").jqGrid({
			url: urlAction,
			datatype: "json",
			postData:{
						'serviceId':serviceId
			 	     },
			colNames: [
						  "#",
						  jsSpringMsg.applicationOrderGridHeading,
						  jsSpringMsg.driverNameGridHeading,
						  jsSpringMsg.driverTypeGridHeading,
						  "",
						  jsSpringMsg.driverHostGridHeading,
						  jsSpringMsg.driverPortGridHeading,
						  jsSpringMsg.driverPluginListGridHeading,
						  jsSpringMsg.driverStatusGridHeading
			             
				],
			colModel: [
				{name:'id',index:'id',sortable:false,hidden:true},
				{name:'applicationOrder',index:'applicationOrder',sortable:true},
				{name:'driverName',index:'driverName',sortable:true,formatter:driverNameFormatter},
				{name:'driveTypeName',index:'descriminatorValue',sortable:true},
				{name:'driveType',index:'driverType',hidden:true},
				{name:'ipAddress',index:'ipAddress',sortable:false},
				{name:'port',index:'port',sortable:false},
				{name:'pluginList',index:'pluginList',sortable:false,formatter:pluginNameFormatter},
				{name:'enable',index:'enable',sortable:false, align:'center', formatter:driverEnableColumnFormatter},
				
			],
			rowNum:totalPageCount,
			rowList:[10,20,60,100],
			height: 'auto',
			sortname: 'applicationOrder',
			sortorder: "asc",
			pager: "#driverGridPagingDiv",
			viewrecords: true,
			multiselect: false,
			timeout : 120000,
		    loadtext: "Loading...",
			caption: jsSpringMsg.gridCaption,
			beforeRequest:function(){
		    },
			loadComplete: function(data) {
				$('.checkboxbg').bootstrapToggle();
				
	 			$('.checkboxbg').change(function(){
				});
				
			},
			loadError : function(xhr,st,err) {
				handleGenericError(xhr,st,err);
			},
			recordtext: jsSpringMsg.recordText,
		    emptyrecords: jsSpringMsg.emptyRecord,
			loadtext: jsSpringMsg.loadText,
			pgtext : jsSpringMsg.pgText,
			beforeSelectRow: function (rowid, e){
			}
			}).navGrid("#driverGridPagingDiv",{edit:false,add:false,del:false,search:false});
				$(".ui-jqgrid-titlebar").hide();
				$(".ui-pg-input").height("10px");
	}
	
/*Function will add link of driver name and onclick redirect driver to the driver configuration page. */
function driverNameFormatter(cellvalue, options, rowObject){
	
	if(isViewDriverAccess){
		return '<a class = "link" style="cursor: pointer;" onclick="viewDistributionDriverDetails(\'' + rowObject["id"] + '\',\'' + rowObject["driverType"] + '\')">' + cellvalue + '</a>' ;
	}else{
		return '<span>' + cellvalue + '</span>' ;
	}
	
}

/*Function will display toggle button for distribution driver. on click change the driver status.*/
var distributionDriverStatus,distributionDriverType,distributionDriverId;
function driverEnableColumnFormatter(cellvalue, options, rowObject){
	var toggleId = rowObject["id"] + "_" + cellvalue;
	
	if(cellvalue === 'ACTIVE'){
		if(isUpdateAccessToChangeDriverStatus){
			return '<input class="checkboxbg" id=' + toggleId + ' data-on="On" data-off="Off" checked type="checkbox" data-size="mini" onchange = "driverStatusPopup('+"'" + rowObject["id"]+ "','"+rowObject["driverType"]+"','"+rowObject["enable"]+"'" + ');">';
		}else{
			return '<input class="checkboxbg " id=' + toggleId + ' disabled  data-on="On" data-off="Off" checked type="checkbox" data-size="mini" >';
		}
		
	}else{
		if(isUpdateAccessToChangeDriverStatus){
			return '<input class="checkboxbg" id=' + toggleId + ' data-on="On" data-off="Off" type="checkbox" data-size="mini"  onchange="driverStatusPopup('+"'" + rowObject["id"]+ "','"+rowObject["driverType"]+"','"+rowObject["enable"]+"'" + ');"   >';
		}else{
			return '<input class="checkboxbg" id=' + toggleId + ' disabled data-on="On" data-off="Off" type="checkbox" data-size="mini"  >';
		}
	}
}

function driverStatusPopup(id, type, status){
	
	distributionDriverId = id;
	distributionDriverType = type
	distributionDriverStatus = status;
	
	if(distributionDriverStatus === 'ACTIVE'){
		$("#active-driver-warning").show();
	}else{
		$("#inactive-driver-warning").show();
	}
	
	$("#updateDriverStatus").click();
}


/*Function will add link for plug-in name and display list of plug-in for selected driver. */ 	
function pluginNameFormatter(cellvalue, options, rowObject){
	
	var driverId=  rowObject["id"];
	var driverName = rowObject["driverName"];
	var driverTypeAlias = rowObject["driverType"]
	if(isViewComposerAccess){
		return '<a class = "link" style="cursor: pointer;" onclick="viewPluginListDetails('+driverId+',\''+driverName + '\',\'' + driverTypeAlias +'\')">'+cellvalue+ '</a>' ;	
	}else{
		return '<span>'+cellvalue+ '</span>' ;
	}
}

/*Function will display distribution plugin list. */
function viewPluginListDetails(driverId, driverName, driverTypeAlias){
	$.ajax({
		url: 'getDistributionDriverPluginList',
		    cache: false,
			async: true,
			dataType: 'json',
			type: "POST",
			data: {
				id: driverId
			},
			success: function(data){
				
				var response = data;
				response.msg = decodeMessage(response.msg);
		    	response.msg = replaceAll("+"," ",response.msg);
		    	var responseObject = response.object;
		    	clearAllMessagesPopUp();
				resetWarningDisplayPopUp();
		    	
		    	if(response.code === 200 || response.code === "200") {
		    		
		    		$("#plugin_list_details").empty();
		    		var tableBody="";
		    		var tableHeader= "<table class='table table-hover'  style='width:100%'>"+
		    						  			 "<tr><th> "+jsSpringMsg.composerName+" </th>"+
		    						  		 	  	 "<th>"+jsSpringMsg.composerType+" </th>"+
		    						  		 	  	 "<th>"+jsSpringMsg.pathListName+" </th>"+
	      						  		 	     "</tr>";
		    		
			    		$.each(responseObject, function(index, data) {
			    			if(responseObject[index][2].trim() !== 'Default Composer'){
			    				tableBody+= 	"<tr>" +
			    								"<td><a href='#' class='link' onclick=\"redirectComposerConfigurationFromSummary(\'"+responseObject[index][0]+"\',\'"+responseObject[index][1]+"\',\'"+responseObject[index][4] +"\',\'"+driverId+"\',\'"+driverName+"\',\'"+driverTypeAlias+"\')\";>"+responseObject[index][1]+" <a/></td>"+
				  								"<td>"+responseObject[index][2]+"</label></td>"+
				  								"<td>"+responseObject[index][3]+"</td>"+
				  							"</tr>"
			    			}else{
			    				tableBody+= 	"<tr>" +
								"<td><a href='#' class='link' style='text-decoration:none;'>"+responseObject[index][1]+" <a/></td>"+
  								"<td>"+responseObject[index][2]+"</label></td>"+
  								"<td>"+responseObject[index][3]+"</td>"+
  							"</tr>"
			    			}					
					    });
		    						  		 	  
		    		var tableEnd= "</table>";
		    		$("#plugin_list_details").html(tableHeader+tableBody+tableEnd);	
		    		$("#driver_plugin_list").click();
		    						   
		    	}else{
		    		showErrorMsgPopUp(response.msg);
		    		$("#plugin_list_details").empty();
		    		$("#driver_plugin_list").click();
		    	}
			},
		    error: function (xhr,st,err){
		    	handleGenericError(xhr,st,err);
			}
		});
	
}

/*Function will set driver status to default. */
function setDefaultStatus(){
	
	if(distributionDriverStatus === 'ACTIVE'){
		$('#'+distributionDriverId +'_'+distributionDriverStatus).bootstrapToggle('on');
	}else{
		$('#'+distributionDriverId +'_'+distributionDriverStatus).bootstrapToggle('off');
	}
	closeFancyBox();
}

/* Function will update distribution driver status  */
function changeDriverStatus(){
	resetWarningDisplay();
	clearAllMessages();
	var tempStatus ;
	
	if(distributionDriverStatus === 'ACTIVE'){
		tempStatus = 'INACTIVE';
	}else{
		tempStatus = 'ACTIVE';
	}
	
	 $.ajax({
		url: 'updateDistributionDriverStatus',
		cache: false,
		async: true,
		dataType: 'json',
		type: "POST",
		data:
		 {
			"driverId"     : distributionDriverId,
			"driverType"   : distributionDriverType,
			"driverStatus" : tempStatus
		}, 
		
		success: function(data){
			
			var response = data;
			var responseCode = response.code; 
			var responseMsg = response.msg; 
			var responseObject = response.object;
			if(responseCode === "200"){
				resetWarningDisplay();
				clearAllMessages();
				if(tempStatus === 'ACTIVE'){
					$('#'+distributionDriverId +'_'+distributionDriverStatus).bootstrapToggle('on');
					$('#'+distributionDriverId +'_'+distributionDriverStatus).attr("id",'#'+distributionDriverId +'_'+tempStatus);
				}else{
					$('#'+distributionDriverId +'_'+distributionDriverStatus).bootstrapToggle('off');
					$('#'+distributionDriverId +'_'+distributionDriverStatus).attr("id",'#'+distributionDriverId +'_'+tempStatus);
				}
				
				showSuccessMsg(responseMsg);
				closeFancyBox();
				reloadGridData();
			}else if(responseObject !== undefined && responseObject !== 'undefined' && responseCode === "400"){
				showErrorMsg(responseMsg);
				$('#'+distributionDriverId +'_'+distributionDriverStatus).bootstrapToggle('toggle');
				closeFancyBox();
				reloadGridData();
			}else{
				resetWarningDisplay();
				clearAllMessages();
				showErrorMsg(responseMsg);
				$('#'+distributionDriverId +'_'+distributionDriverStatus).bootstrapToggle('toggle');
				closeFancyBox();
				reloadGridData();
			}
			$("#active-driver-warning").hide();
			$("#inactive-driver-warning").hide();
		},
	    error: function (xhr,st,err){
			handleGenericError(xhr,st,err);
		}
	}); 
}
/*Function will reload grid data */
function reloadGridData(){
	var $grid = $("#drivergrid");
	$grid.jqGrid('setGridParam',{datatype:'json',page: 1}).trigger('reloadGrid');
}

/* Method will load tab data */
function loadDistributionDetails(formAction, formMethod,requestAction){
	$("#distribution_service_details").attr("action",formAction);
	$("#distribution_service_details").attr("method",formMethod);
	$("#requestAction").val(requestAction);
	$('#distribution_service_details').submit();	
}

function disableFileGroupingParams(elementVal){
	if(elementVal === 'true'){
		$('#fileGroupingParameter-groupingType').attr('readonly',false);
		$('#fileGroupingParameter-sourcewiseArchive').attr('disabled',false);
		$('#fileGroupingParameter-groupingDateType').attr('disabled',false);
	}else{
		$('#fileGroupingParameter-groupingType').attr('readonly',true);
		$('#fileGroupingParameter-sourcewiseArchive').attr('disabled',true);
		$('#fileGroupingParameter-groupingDateType').attr('disabled',true);
	}
	
}

/* Function will submit service configuration parameters.*/
function updateDistributionServiceConfiguration(){
	
	if($("#writeRecordLimit").val() === '' || $("#writeRecordLimit").val() <= '0'){
		$("#writeRecordLimit").val('-1');
	}
	
	if($("#serviceSchedulingParams-schedulingEnabled").val() === 'true' &&  $("#svcExecParams-executionInterval").val() === ''){
		 $("#svcExecParams-executionInterval").val('0');
	}
		
	$('#distribution-service-configuration-form').submit();
}

/*Function will reset all service configuration parameters. */
function resetAllDistributionParameters(){
		resetWarningDisplay();
		clearAllMessages();
		$("#name").val('');
		$("#description").val('');		
		$("#svcExecParams-minThread").val('');
		$("#svcExecParams-maxThread").val('');
		$("#svcExecParams-fileBatchSize").val('');
		$("#svcExecParams-queueSize").val('');
	
		if($("#serviceSchedulingParams-schedulingEnabled").val() === 'true'){
			$("#serviceSchedulingParams-time").val('');
		} else {
			$("#svcExecParams-executionInterval").val('');
		}
				
		$("#timestenDatasourceName").val('');
		$("#writeRecordLimit").val('');
		$("#processRecordLimit").val('');
		
}

/*Function will redirect user to driver details config parameters. */
function viewDistributionDriverDetails(driverId,driverType){
	$('#driverTypeAlias').val(driverType);
	$('#driverId').val(driverId);
	$('#distribution-driver-config-form').submit();
}


/*Function will start/stop distribution service. */
function starStoptDistributionService(serviceId,serviceName,serviceInstanceId) {

	$("#syncWarningMsg").hide();

	$("#server-stop-close-btn").hide();
	$("#service-stop-close-btn").hide();

	$("#server-start-close-btn").hide();
	$("#service-start-close-btn").hide();

	$("#startServiceId").val(serviceId);
	
	var serviceStatus = $("#serviceStatus").val();
	
	if (serviceStatus === 'INACTIVE') {
		
		$("#divStartService #lblServiceId").text(serviceInstanceId);
		$("#divStartService #lblServiceName").text(serviceName);
		$("#service-start-close-btn").show();
		var syncStatus = '${syncStatus}';
		if (syncStatus === 'false') {
			$("#syncWarningMsg").show();
		}
		$("#start_service_link").click();

	} else {
		$("#service-stop-close-btn").show();
		$("#divStopService #lblServiceId").text(serviceInstanceId);
		$("#divStopService #lblServiceName").text(serviceName);
		$("#stop_service_link").click();
	}	

}

/* Function will export distribution service details as a xml format*/
function exportConfigPopup(requestActionType){
	$("#exportServiceInstanceId").val($("#serviceId").val()); // set service instance id which is selected for export to submit with form.
	$("#isExportForDelete").val(false);
	$("#REQUEST_ACTION_TYPE").val(requestActionType);
	$("#export-service-instance-config-form").submit();
}

function redirectComposerConfigurationFromSummary(plugInId,plugInName,plugInType, driverId, driverName, driverTypeAlias){
	
	$('#composerId').val(plugInId);
	$('#composerName').val(plugInName);
	$('#composerType').val(plugInType);
	$('#plugInId').val(plugInId);
	$('#plugInName').val(plugInName);
	$('#plugInType').val(plugInType);
	$('#driverIdsummary').val(driverId);
	$('#driverNamesummary').val(driverName);
	$('#driverTypeAliassummary').val(driverTypeAlias);
	$('#distribution-service-composer-config_from_plugin_list').submit();
}
