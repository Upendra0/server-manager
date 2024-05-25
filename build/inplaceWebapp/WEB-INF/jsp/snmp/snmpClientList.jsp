  <%@page import="com.elitecore.sm.common.constants.SystemParametersConstant"%>
  <%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
    <%@page import="com.elitecore.sm.util.MapCache"%>
    <%@ taglib 	uri="http://www.springframework.org/security/tags" prefix="sec"  %>
    <%@taglib 	uri="http://www.springframework.org/tags" prefix="spring"%>
    <%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<style>
.fancybox-wrap fancybox-desktop fancybox-type-inline fancybox-opened{
	position: fixed;
	top: 32px;
}
</style>
 	<a href="#divAddClientList" class="fancybox" style="display: none;" id="addsnmpClientListPopup">#</a>
		   	 <div id="divAddClientList" style=" width:100%; display: none;top: 10px;" >
			   <jsp:include page="addSnmpClientConfig.jsp"></jsp:include> 
			</div> 
			
			<a href="#divViewMappedClientList" class="fancybox" style="display: none;" id="viewMappedClientListPopup">#</a>
		   	 <div id="divViewMappedClientList" style=" width:100%; display: none;" >
			   <jsp:include page="snmpClientMappedAlertPopUp.jsp"></jsp:include> 
			</div> 
			
				<a href="#divChangesnmpClientStatus" class="fancybox" style="display: none;" id="updatesnmpClientStatus">#</a>
				
				<div id="divChangesnmpClientStatus" style=" width:100%; display: none;" >
		    <div class="modal-content">
		        <div class="modal-header padding10">
		            <h4 class="modal-title"><spring:message code="snmp.Client.status.header.label"></spring:message></h4>
		        </div>
		        <div class="modal-body padding10 inline-form">
		        	<span id="enableClientStatusResponseMsg" style="display:none;">
		        		<jsp:include page="../common/responseMsgPopUp.jsp" ></jsp:include>
		        	</span>
			        <p id="active-snmp-client-warning" style="display:none;">
			       		 <spring:message code="snmp.client.change.status.active.message" ></spring:message>
			        </p>
			        <p id="inactive-snmp-client-warning" style="display:none;">
			       		 <spring:message code="snmp.client.change.status.inactive.message" ></spring:message>
			        </p>
			        
			        	
		        </div>
		        <sec:authorize access="hasAuthority('EDIT_SNMP_CLIENT')">
			        <div id="inactive-snmp-client-div" class="modal-footer padding10">
			            <button id="yes_client_status_enable" type="button" class="btn btn-grey btn-xs " onclick="changesnmpClientStatus();"><spring:message code="btn.label.yes"></spring:message></button>
			            <button id="no_client_status_enable" type="button" class="btn btn-grey btn-xs " data-dismiss="modal" onclick="setDefaultStatus();"><spring:message code="btn.label.no"></spring:message></button>
			        </div>
		        </sec:authorize>
		    </div>
		    <!-- /.modal-content --> 
		</div>
		
			<script>
			var jsSpringMsg = {};
			jsSpringMsg.failUpdatesnmpServerMsg = "<spring:message code='snmp.server.update.mapping.fail'></spring:message>";
			
			function editClientColumnFormatter(cellvalue, options, rowObject){
				
				clearSearchOptions();
				var clientName = rowObject["snmpClientName"].replace(/ /g, "_");
				var clientEditId = clientName +"_client_edit_lnk";
				return "<a href='#' id='" + clientEditId + "'class='link' onclick=addClientListPopup('"+'EDIT'+"','"+rowObject["snmpClientId"]+"'); ><i class='fa fa-pencil-square-o' aria-hidden='true'></i></a>";
			}
			function clearSearchOptions(){
				
			    $("#snmpClientAlertgrid").jqGrid('setGridParam', { search: false, postData: { "filters": ""} }).trigger("reloadGrid");
			}
			
			function alertMappingColumnFormatter(cellvalue, options, rowObject){
				var clientName = rowObject["snmpClientName"].replace(/ /g, "_");
				var clientMappingId = clientName +"_client_alert_mapping_lnk";
				return "<a href='#' id='" + clientMappingId + "' class='link' onclick=showMappedAlertPopup('"+rowObject["id"]+"'); ><i class='fa fa-list'></i></a>";
			}
			
			var snmpClientStatus,snmpClientId;
			function snmpClientEnableColumnFormatter(cellvalue, options, rowObject){
				var toggleId = rowObject["snmpClientId"] + "_" + cellvalue;
				var clientName = rowObject["snmpClientName"].replace(/ /g, "_");
				var toggleDivId = clientName + "_client_enable_toggle";
				
				if(cellvalue == 'ACTIVE'){
					return '<div id="'+toggleDivId+'"><input class="checkboxbg" id=' + toggleId + ' value= '+cellvalue+' data-on="On" data-off="Off" checked type="checkbox" data-size="mini" onchange = "snmpClientStatusPopup('+"'" + rowObject["snmpClientId"]+ "','"+rowObject["enable"]+"'" + ');"></div>';
				}else{
					return '<div id="'+toggleDivId+'"><input class="checkboxbg" id=' + toggleId + ' value= '+cellvalue+' data-on="On" data-off="Off" type="checkbox" data-size="mini"  onchange="snmpClientStatusPopup('+"'" + rowObject["snmpClientId"]+ "','"+rowObject["enable"]+"'" + ');"   ></div>';
				}
			}
			
			function showMappedAlertPopup(selectedClientId){
			
				$.ajax({
					url: '<%=ControllerConstants.GET_CONFIGURED_ALERT_LIST%>',
					cache: false,
					async: true, 
					dataType: 'json',
					type: "POST",
					data: 
					{
						"selected_snmpClientId"					:   selectedClientId,
					},
					success: function(data){
						hideProgressBar();
						$("#mappedAlertLabel").show();

						var response = eval(data);
						
						var responseCode = response.code; 
						var responseMsg = response.msg; 
						var responseObject = response.object;
						 console.log("Response object is ::  " + responseObject);
						if(responseCode == "200"){
							//parent.getSnmpServerList();
							
						
							var alerts=eval(responseObject);
							
							
						
							var configuredAlertList=alerts['configuredAlertList'];
							
						
							 $("#snmpClientMappedAlertgrid").jqGrid('clearGridData');
								if(configuredAlertList!=null && configuredAlertList != 'undefined' && configuredAlertList != undefined){
									
									displayGridForMappedClientMappedAlert(configuredAlertList);
								
									$.each(configuredAlertList,function(index,attribute){
						 				
						 				var rowData = {};
						 				rowData.id							= parseInt(attribute.id);
						 				rowData.alertId						= attribute.alertId;
						 				rowData.name						= attribute.name;
						 				rowData.alertType					= attribute.alertType;
						 				
						 				
						 				
						 				jQuery("#snmpClientMappedAlertgrid").jqGrid('addRowData',rowData.id,rowData);
						 				
						 			
						 			});
									
									jQuery("#snmpClientMappedAlertgrid").setGridParam({ rowNum: 10 }).trigger("reloadGrid");
									
									$('#viewMappedClientListPopup').click();	
									
								} 
						}else if(responseObject != undefined && responseObject != 'undefined' && responseCode == "400"){
							console.log("got 400 response.");
						
							
							addErrorIconAndMsgForAjaxPopUp(responseObject);
							
						}else{
							
							resetWarningDisplayPopUp();
							
							showErrorMsgPopUp(responseMsg);
							reloadClientGridData();
						}
					},
				    error: function (xhr,st,err){
				    	hideProgressBar();
				    	$("#create-server-buttons-div").show();
						handleGenericError(xhr,st,err);
					}
				});
				
			}
			
			function snmpClientStatusPopup(id,status){

				snmpClientId = id;
				snmpClientStatus = status;
				
				
				if(snmpClientStatus == 'ACTIVE'){
					$("#active-snmp-client-warning").show();
				}else{
					$("#inactive-snmp-client-warning").show();
				}
				$("#updatesnmpClientStatus").click();
			}
			
			function deleteClientPopup(){
				
				if(ckSnmpClientSelected.length == 0){
					
					$("#clessWarn").show();
					$("#cmoreWarn").hide();
					$("#clientMessage").click();
					return;
				}else if(ckSnmpClientSelected.length > 1){
					$("#clessWarn").hide();
					$("#cmoreWarn").show();
					$("#clientMessage").click();
					return;
				}else{
					// set instance id which is selected for import to submit with form
					$("#deleteSnmpClientId").val(ckSnmpClientSelected[0]);
					$("#delete-snmp-client-warning").show();
					$('#deleteSnmpClient').click();
				}	
			}
			
function getsnmpClientList(){

			$("#snmpClientgrid").jqGrid({
	        	url: "<%=ControllerConstants.GET_SNMP_CLIENTLIST%>",
	        	postData:
				{
	        		'snmpserverInstanceId':'${serverInstanceId}' 
				},
			
	            datatype: "json",
	            colNames:["#",
						  "<spring:message code='snmpClientList.grid.column.id' ></spring:message>",
						  "<spring:message code='snmpClientList.grid.column.id' ></spring:message>",
	                      "<spring:message code='snmpClientList.grid.column.name' ></spring:message>",
	                      "<spring:message code='snmpClientList.grid.column.ip' ></spring:message>",
	                      "<spring:message code='snmpClientList.grid.column.port' ></spring:message>",
	                      "<spring:message code='snmpClientList.grid.column.community' ></spring:message>",
	                      "<spring:message code='snmpClientList.grid.column.version' ></spring:message>",
	                      "<spring:message code='snmpClientList.grid.column.advance' ></spring:message>",
	                      "<spring:message code='snmpClientList.grid.column.alertMapping' ></spring:message>",
	                      
	                      "<spring:message code='snmpServerList.grid.column.authAlgo' ></spring:message>",
	                      "<spring:message code='snmpServerList.grid.column.authPass' ></spring:message>",
	                      "<spring:message code='snmpServerList.grid.column.privAlgo' ></spring:message>",
	                      "<spring:message code='snmpServerList.grid.column.privPass' ></spring:message>",
	                      
	                      "<spring:message code='snmpServerList.grid.column.enable' ></spring:message>",
	                      "<spring:message code='snmpServerList.grid.column.edit' ></spring:message>",
	                     ],
	                     colModel: [
							{name : '',index : '',formatter : checkBoxFormatter,sortable : false,width : '20%'}, 
							{name:'id',index:'id',sortable:true,hidden:true},
	                    	{name:'snmpClientId',index:'id',sortable:true,hidden:true},
	                    	{name:'snmpClientName',index:'name',sortable:false},
	                    	{name:'snmpClientHostIP',index:'hostIP',sortable:true},
	                    	{name:'snmpClientPort',index:'port',align:'center',sortable:false},
	                    	{name:'snmpClientCommunity',index:'community',align:'center',sortable:false},
	                    	{name:'snmpClientVersion',index:'version',align:'center',sortable:false},
	                    	{name:'snmpClientAdvance',index:'advance',align:'center',sortable:false},
	                    	{name:'alertMapping',index:'alertMapping',sortable:false, align:'center',formatter:alertMappingColumnFormatter},
	                    	
	                    	{name:'snmpV3AuthAlgorithm',index:'snmpV3AuthAlgorithm',align:'center',sortable:false,hidden:true},
	                    	{name:'snmpV3AuthPassword',index:'snmpV3AuthPassword',align:'center',sortable:false,hidden:true},
	                    	{name:'snmpV3PrivAlgorithm',index:'snmpV3PrivAlgorithm',align:'center',sortable:false,hidden:true},
	                    	{name:'snmpV3PrivPassword',index:'snmpV3PrivPassword',align:'center',sortable:false,hidden:true},
	                    	
	                    	{name:'enable',index:'status',sortable:false, align:'center',formatter:snmpClientEnableColumnFormatter},
	                    	{name:'Edit',index:'edit',align:'center',sortable:false,formatter:editClientColumnFormatter}
	                    		],
	            rowNum:<%= MapCache.getConfigValueAsInteger(SystemParametersConstant.TOTAL_ROWS_TO_DISPLAY_IN_GRID, 10) %>,
	            rowList:[10,20,60,100],
	            height: 'auto',
	            mtype:'POST',
	    		sortname: 'id',
	    		sortorder: "asc",
	    		pager: "#snmpClientgridPagingDiv",
	    		contentType: "application/json; charset=utf-8",
	    		viewrecords: true,
	    		multiselect: false,
	    		timeout : 120000,
	    	    loadtext: "Loading...",
	            caption: "<spring:message code="snmpConfiguration.add.clientList.label"></spring:message>",
	            beforeRequest:function(){
	                $(".ui-dialog-titlebar").hide();
	            }, 
	            beforeSend: function(xhr) {
	                xhr.setRequestHeader("Accept", "application/json");
	                xhr.setRequestHeader("Content-Type", "application/json");
	            },
	            beforeSelectRow: function(rowid, e) {
	            	var status = $(jQuery('#snmpClientgrid').jqGrid ('getCell', rowid, 'enable')).closest("input").val();
	            	var $grid = $("#snmpClientgrid");
 		           if(status == 'ACTIVE'){
 		        	  return false;
	     			}
 		           
 		           
 		  		/* if($("#jqg_snmpClientgrid_" + $grid.jqGrid ('getCell', rowid, 'snmpClientId')).is(':checked')){
					if(ckSnmpClientSelected.indexOf($grid.jqGrid ('getCell', rowid, 'snmpClientId')) == -1){
						
						
						ckSnmpClientSelected.push($grid.jqGrid ('getCell', rowid, 'snmpClientId'));
					}
				}else{
					if(ckSnmpClientSelected.indexOf($grid.jqGrid ('getCell', rowid, 'snmpClientId')) != -1){
						ckSnmpClientSelected.splice(ckSnmpClientSelected.indexOf($grid.jqGrid ('getCell', rowid, 'snmpClientId')), 1);
					}
				} */
 		  		
	            	
	            },
	            loadComplete: function(data) {
	            
	     			 $(".ui-dialog-titlebar").show();
	     			
	     			var $jqgrid = $("#snmpClientgrid");      
	     			$(".jqgrow", $jqgrid).each(function (index, row) {
	     				 
	     		        var $row = $(row);
	     		      	
     		            //Find the checkbox of the row and set it disabled
     		            var status = $(jQuery('#snmpClientgrid').jqGrid ('getCell', row.id, 'enable')).closest("input").val();
     		       
     		           if(status == 'ACTIVE'){
     		        	  $row.find("input:checkbox").attr("disabled", "disabled");
   	     			}
     		         
	     		    });
	     			$('.checkboxbg').removeAttr("disabled");
	     			$('.checkboxbg').bootstrapToggle();
	     			$('.checkboxbg').change(function(){
	     				$("#snmpClientgridPagingDiv").show();
	    			});
	     			
	     			
	     			
				}, 
				onPaging: function (pgButton) {
					clearResponseMsgDiv();
					//clearInstanceGrid();
				},
	            loadError : function(xhr,st,err) {
	    			handleGenericError(xhr,st,err);
	    		},
	            recordtext: "<spring:message code="serverManagement.grid.pager.total.records.text"></spring:message>",
	    	    emptyrecords: "<spring:message code="snmpServerList.grid.empty.records"></spring:message>",
	    		loadtext: "<spring:message code="snmpServerList.grid.loading.text"></spring:message>",
	    		
	    		}).navGrid("#snmpClientgridPagingDiv",{edit:false,add:false,del:false,search:false});
	    			$(".ui-jqgrid-titlebar").hide();
	    			$(".ui-pg-input").height("10px");
	
		}
		
function checkBoxFormatter(cellvalue, options, rowObject) {
	var snmpClientName = rowObject["snmpClientName"].replace(/ /g, "_");
	return "<input type='checkbox' name='"+snmpClientName+"_client_chkbox"+"' id='"+snmpClientName+"_client_chkbox"+"' onclick=\"addRowId(\'"+snmpClientName+"_client_chkbox"+"\', \'"+rowObject["id"]+"\')\"; />";
}

function addRowId(elementId,snmpClientId)
{
	var deviceElement = document.getElementById(elementId);
	if (deviceElement.checked) {
		if((ckSnmpClientSelected.indexOf(snmpClientId)) == -1){
			ckSnmpClientSelected.push(snmpClientId);
		}		
	}else{
		if(ckSnmpClientSelected.indexOf(snmpClientId) !== -1){
			ckSnmpClientSelected.splice(ckSnmpClientSelected.indexOf(snmpClientId), 1);
		}
	}
}
		
function changesnmpClientStatus(){
	resetWarningDisplay();
	clearAllMessages();
	var tempStatus ;

	if(snmpClientStatus == 'ACTIVE'){
		tempStatus = 'INACTIVE';
	}else{
		tempStatus = 'ACTIVE';
	}

	snmp_clientInstanceId = ${serverInstanceId};
	 $.ajax({
		url: '<%=ControllerConstants.UPDATE_SNMP_CLIENT_STATUS%>',
		cache: false,
		async: true,
		dataType: 'json',
		type: "POST",
		data:
		 {
			"snmpClientId"     		: snmpClientId,
			"snmpClientStatus" 		: tempStatus,
			"snmpClientInstanceId" 	: snmp_clientInstanceId
		}, 
		
		success: function(data){
			
			var response = eval(data);
			var responseCode = response.code; 
			var responseMsg = response.msg; 
			var responseObject = response.object;
			if(responseCode == "200"){
				resetWarningDisplay();
				clearAllMessages();
				if(tempStatus == 'ACTIVE'){
					$('#'+snmpClientId +'_'+snmpClientStatus).bootstrapToggle('on');
					$('#'+snmpClientId +'_'+snmpClientStatus).attr("id",'#'+snmpClientId +'_'+tempStatus);
				}else{
					$('#'+snmpClientId +'_'+snmpClientStatus).bootstrapToggle('off');
					$('#'+snmpClientId +'_'+snmpClientStatus).attr("id",'#'+snmpClientId +'_'+tempStatus);
				}
				
				showSuccessMsg(responseMsg);
				closeFancyBox();
				reloadClientGridData();
			}else if(responseObject != undefined && responseObject != 'undefined' && responseCode == "400"){
				showErrorMsg(responseMsg);
				$('#'+snmpClientId +'_'+snmpClientStatus).bootstrapToggle('toggle');
				closeFancyBox();
				reloadClientGridData();
			}else{
				resetWarningDisplay();
				clearAllMessages();
				showErrorMsg(responseMsg);
				$('#'+snmpClientId +'_'+snmpClientStatus).bootstrapToggle('toggle');
				closeFancyBox();
				reloadClientGridData();
			}
			$("#active-snmp-client-warning").hide();
			$("#inactive-snmp-client-warning").hide();
		},
	    error: function (xhr,st,err){
			handleGenericError(xhr,st,err);
		}
	}); 
}
function reloadClientGridData(){
	var $grid = $("#snmpClientgrid");
	$grid.jqGrid('setGridParam',{datatype:'json',page: 1}).trigger('reloadGrid');
}
function setDefaultStatus(){
	
	if(snmpClientStatus == 'ACTIVE'){
		$('#'+snmpClientId +'_'+snmpClientStatus).bootstrapToggle('on');
	}else{
		$('#'+snmpClientId +'_'+snmpClientStatus).bootstrapToggle('off');
	}
	closeFancyBox();
}
function resetSnmpClientAttributeParams(){
	   $("#client_name").val('');
	   $("#client_hostIP").val('');
	   $("#client_port").val('');
	
}
function addClientListPopup(actionType,attributeId){
	 
 	clearAllMessagesPopUp();
 	 resetWarningDisplayPopUp();
 	$("#client_name").prop('readonly', false);
	$("#client_hostIP").prop('readonly', false);
	$("#client_port").prop('readonly', false);
	$("#client_version").prop('disabled', false);
	$("#client_advance").prop('disabled', false);
	$("#client_community").prop('disabled', false);
	
	$("#client_snmpV3AuthAlgorithm").prop('disabled', false);
	$("#client_snmpV3AuthPassword").prop('disabled', false);
	$("#client_snmpV3PrivAlgorithm").prop('disabled', false);
	$("#client_snmpV3PrivPassword").prop('disabled', false);
	
	var ver = document.getElementById("client_version");
	var algo = document.getElementById("client_snmpV3AuthAlgorithm");
 	
 	 resetSnmpClientAttributeParams();
	 if(actionType === 'ADD'){
			$("#caddNewAttribute").show();
 			$("#ceditAttribute").hide();
 			$("#c_update_label").hide();
 			$("#c_add_label").show();
 			$("#calert_editAttribute").hide();
 			$("#add-snmp-client-alert-buttons-div").hide();
 			$("#add-snmp-client-buttons-div").show();
 			$("#addSnmpClientAlertListC0ntentDiv").hide();
 			
			
 			$("#id").val(0);
			
			$('#addsnmpClientListPopup').click();	
			
 		}
 	 else{
 			if(attributeId > 0){
 				//getAttributeById(attributeId);
 				$("#addSnmpClientAlertListC0ntentDiv").show();
				
 				var responseObject='';
 				responseObject = jQuery("#snmpClientgrid").jqGrid ('getRowData', attributeId);
 				$("#add-snmp-client-buttons-div").show();
 				$("#ceditAttribute").show();
 				$("#caddNewAttribute").hide();
 				$("#c_update_label").show();
 				$("#c_add_label").hide();
 				$("#addSnmpClientAlertListC0ntentDiv").show();
 				$("#calert_editAttribute").show();
 				$("#add-snmp-client-alert-buttons-div").show();
 				$("#calert_editAttribute").show();
 				
 				
		
 				$("#snmpClientId").val(responseObject.snmpClientId);
 				$("#client_name").val(responseObject.snmpClientName);
 				$("#client_hostIP").val(responseObject.snmpClientHostIP);
 				$("#client_port").val(responseObject.snmpClientPort);
 				$("#client_version").val(responseObject.snmpClientVersion);
 				$("#client_community").val(responseObject.snmpClientCommunity);
 				
 				$("#client_snmpV3AuthAlgorithm").val(responseObject.snmpV3AuthAlgorithm);
 				$("#client_snmpV3AuthPassword").val(responseObject.snmpV3AuthPassword);
 				$("#client_snmpV3PrivAlgorithm").val(responseObject.snmpV3PrivAlgorithm);
 				$("#client_snmpV3PrivPassword").val(responseObject.snmpV3PrivPassword);
 				
 				//$("#client_advance").val(responseObject.snmpClientAdvance.name);
 				console.log("value for enum::"+responseObject.snmpClientAdvance);
 				var element = document.getElementById('client_advance');
 			    element.value = responseObject.snmpClientAdvance;
 				
 			    resetSnmpClientAlertGrid();
 				addAlertsInUpdatePopup(responseObject.snmpClientId);
				$("#addsnmpClientListPopup").click();
 			}else{
 				showErrorMsg(jsSpringMsg.failUpdatesnmpServerMsg);
 			}
		}
		
		handleV3Params('version',ver.options[ver.selectedIndex].value);
		if(ver.options[ver.selectedIndex].value == 'V3'){
			handleV3Params('Algo',algo.options[algo.selectedIndex].value);
		}
}

//resets Alert List searching parameters and reloads all Alert Lists inside SnmpClientAlertGrid whatever Alert Lists snmpClientgrid has  
function resetSnmpClientAlertGrid(){
		var $grid = $('#snmpClientgrid');
		var currentPageOnGrid = $grid.jqGrid('getGridParam','page');
		$("#gs_alertId").val('');
		$("#gs_name").val('');
		$("#gs_alertType").val('');
		$grid.jqGrid('setGridParam',{datatype:'json',page: currentPageOnGrid }).trigger('reloadGrid');
}

function addAlertsInUpdatePopup(clientId)
{
	
	$.ajax({
		url: '<%=ControllerConstants.ADD_ALERTS_TO_UPDATE_CLIENT%>',
		cache: false,
		async: false, 
		dataType: 'json',
		type: "POST",
		data: 
		{
			"clientId"					:   clientId,
		},
		success: function(data){
			hideProgressBar();
			
			var response = eval(data);
			
			var responseCode = response.code; 
			var responseMsg = response.msg; 
			var responseObject = response.object;
			 
			if(responseCode == "200"){
				//parent.getSnmpServerList();
				
			//	$("#add-snmp-client-buttons-div").hide();
				$("#add-snmp-client-alert-buttons-div").show();
				$("#calert_addNewAttribute").hide();
				$("#calert_editAttribute").show();
				ckSnmpClientAlertSelected=new Array();
				$("#addSnmpClientAlertListC0ntentDiv").show();
				
				var alerts=eval(responseObject);
				
				
				var updatealertList=alerts['alertList'];
				var configuredAlertList=alerts['configuredAlertList'];
				
			//collectionServiceMaxFilesAlert_alert_chkbox
				$("#snmpClientAlertgrid").jqGrid('clearGridData');
				 
					if(updatealertList!=null && updatealertList != 'undefined' && updatealertList != undefined){
						
						displayGridForClientAlert(updatealertList);
					
						$.each(updatealertList,function(index,attribute){
			 				
			 				var rowData = {};
			 				rowData.id							= parseInt(attribute.id);
			 				rowData.alertId						= attribute.alertId;
			 				rowData.name						= attribute.name;
			 				rowData.alertType					= attribute.alertType;
			 		
			 				jQuery("#snmpClientAlertgrid").jqGrid('addRowData',rowData.id,rowData);
			 			});
						jQuery("#snmpClientAlertgrid").setGridParam({ rowNum: 10 }).trigger("reloadGrid");
						var $grid = $("#snmpClientAlertgrid");
							$.each(configuredAlertList,function(index,attribute){
			 				
			 				var rowData = {};
			 				rowData.id							= parseInt(attribute.id);
			 				rowData.alertId						= attribute.alertId;
			 				rowData.name						= attribute.name;
			 				rowData.alertType					= attribute.alertType;
			 				
			 			
			 				ckSnmpClientAlertSelected.push(attribute.id.toString());
			 				
			 				var snmpAlertNameChk = (attribute.name).replace(/ /g, "_");
							//alert("id="+snmpAlertName+"_alert_chkbox");
							//return "<input type='checkbox' name='"+snmpAlertName+"_alert_chkbox"+"' id='"+snmpAlertName+"_alert_chkbox"+"' onclick=\"addRowIdForAlert(\'"+snmpAlertName+"_alert_chkbox"+"\', \'"+rowObject["id"]+"\')\"; />";
			 				
							$("#"+snmpAlertNameChk+"_alert_chkbox").prop('checked', true);
			 				
			 			//	jQuery("#snmpClientAlertgrid").jqGrid('setSelection',rowId,true);
			 				
			 			});
						
							
						
						
					} 
			}else if(responseObject != undefined && responseObject != 'undefined' && responseCode == "400"){
				console.log("got 400 response.");
			//	$("#create-server-buttons-div").show();
				
				addErrorIconAndMsgForAjaxPopUp(responseObject);
				
			}else{
				
				resetWarningDisplayPopUp();
				$("#create-server-buttons-div").show();
				showErrorMsgPopUp(responseMsg);
				reloadClientGridData();
			}
		},
	    error: function (xhr,st,err){
	    	hideProgressBar();
	    	$("#create-server-buttons-div").show();
			handleGenericError(xhr,st,err);
		}
	});
	
}




		$(document).ready(function() {
			getsnmpClientList();
		})

</script>
		
		<div class="tab-content no-padding clearfix">
         	<div class="fullwidth">
   				<div class="title2">
   					<spring:message code="snmpConfiguration.add.clientList.label" ></spring:message>
	   				 <span class="title2rightfield">
				             
				             <sec:authorize access="hasAuthority('ADD_SNMP_CLIENT')"> 	
				          	<span class="title2rightfield-icon1-text">
				          		<a href="#" id="addClient" onclick="addClientListPopup('ADD',0);"><i class="fa fa-plus-circle"></i></a>
	          					<a href="#" id="addClient" onclick="addClientListPopup('ADD',0);">
	          						<spring:message code="btn.label.add" ></spring:message>
	          					</a>
		          			</span>	
				          	</sec:authorize>
				          
				          		<sec:authorize access="hasAuthority('DELETE_SNMP_CLIENT')">
				          		<span class="title2rightfield-icon1-text">
				          		<a href="#" id="delete_client_img" onclick="deleteClientPopup();"> <i class="fa fa-trash"></i></a>
				          		<a href="#" id="delete_client_btn" onclick="deleteClientPopup();"><spring:message code="btn.label.delete" ></spring:message></a>
				          		</span>
				          		</sec:authorize>

				          </span>
		          </div>
   			</div>
	</div>

	<div class="box-body table-responsive no-padding box">
			<table class="table table-hover" id="snmpClientgrid"></table>
	           	<div id="snmpClientgridPagingDiv"></div> 
	         </div>
