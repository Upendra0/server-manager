  <%@page import="com.elitecore.sm.common.constants.SystemParametersConstant"%>
  <%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
    <%@page import="com.elitecore.sm.util.MapCache"%>
    <%@ taglib 	uri="http://www.springframework.org/security/tags" 	prefix="sec"  %>
    <%@taglib 	uri="http://www.springframework.org/tags" 			prefix="spring"%>
    <%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>


	
<script type="text/javascript">



function addServerListPopup(actionType,attributeId){
	 clearAllMessagesPopUp();
	 resetWarningDisplayPopUp();
	 resetSnmpServerAttributeParams();
	 if(actionType === 'ADD'){
			$("#addNewAttribute").show();
			$("#editSnmpServer").hide();
			$("#add_label").show();
			$("#update_label").hide();
			$("#addNewServer").show();
			$("#id").val(0);
			
			$('#addsnmpServerListPopup').click();	
		}
	 else{
			if(attributeId > 0){
				//getAttributeById(attributeId);
				
				var responseObject='';
				responseObject = jQuery("#snmpServergrid").jqGrid ('getRowData', attributeId);
				$("#editSnmpServer").show();
				$("#addNewAttribute").hide();
				$("#update_label").show();
				$("#add_label").hide();
				
				$("#snmpServerId").val(responseObject.SnmpServerId);
				$("#server_name").val(responseObject.SnmpServerName);
				$("#server_hostIP").val(responseObject.SnmpServerHostIP);
				$("#server_port").val(responseObject.SnmpServerPort);
				$("#server_portOffset").val(responseObject.SnmpServerPortOffset);
				$("#server_community").val(responseObject.SnmpServerCommunity);
				$("#addNewServer").hide();
				
				
				$("#addsnmpServerListPopup").click();
			}else{
				showErrorMsg(jsSpringMsg.failUpdateSnmpServerMsg);
			}
		}
				
	$("#create-server-buttons-div").show();
	resetWarningDisplayPopUp();
	clearAllMessagesPopUp();

}

function deletePopup(){
	
	if(ckSnmpServerSelected.length == 0){
		
		$("#lessWarn").show();
		$("#moreWarn").hide();
		$("#message").click();
		return;
	}else if(ckSnmpServerSelected.length > 1){
		$("#lessWarn").hide();
		$("#moreWarn").show();
		$("#message").click();
		return;
	}else{
		// set instance id which is selected for import to submit with form
		$("#deleteSnmpServerId").val(ckSnmpServerSelected[0]);
		$("#delete-snmp-server-warning").show();
		$('#deleteSnmpServer').click();
	}	
}

/*Function will reset all Snmp Server attribute params */
function resetSnmpServerAttributeParams(){
	
	   $("#server_name").val('');
	   $("#server_port").val('');
	   $("#server_portOffset").val('');
	   $("#server_community").val('');
	
}

var snmpServerStatus,snmpServerId;
function snmpserverEnableColumnFormatter(cellvalue, options, rowObject){
	var toggleId = rowObject["SnmpServerId"] + "_" + cellvalue;
	var snmpServerName = rowObject["SnmpServerName"].replace(/ /g, "_");
	var toggleIdDiv = snmpServerName + "_server_enable_btn";
	
	if(cellvalue == 'ACTIVE'){
		return '<div id="'+toggleIdDiv+'"><input class="checkboxbg" id=' + toggleId + ' value= '+cellvalue+' data-on="On" data-off="Off" checked type="checkbox" data-size="mini" onchange = "snmpServerStatusPopup('+"'" + rowObject["SnmpServerId"]+ "','"+rowObject["enable"]+"'" + ');"></div>';
	}else{
		return '<div id="'+toggleIdDiv+'"><input class="checkboxbg" id=' + toggleId + ' value= '+cellvalue+' data-on="On" data-off="Off" type="checkbox" data-size="mini"  onchange="snmpServerStatusPopup('+"'" + rowObject["SnmpServerId"]+ "','"+rowObject["enable"]+"'" + ');"   ></div>';
	}
}

function reloadGridDataa(){
	
	$grid.jqGrid('setGridParam',{datatype:'json',page: 1,sortname: 'id',sortorder: "asc", postData:{
		  
   		'mappingId': function () {
	        return selMappingId;
   		},
   		'plugInType':currentParserType,
	}}).trigger('reloadGrid');
}

function snmpServerStatusPopup(id,status){

	snmpServerId = id;
	snmpServerStatus = status;
	
	
	if(snmpServerStatus == 'ACTIVE'){
		$("#active-snmp-server-warning").show();
	}else{
		$("#inactive-snmp-server-warning").show();
	}
	$("#updateSnmpServerStatus").click();
}

function editSnmpServerColumnFormatter(cellvalue, options, rowObject){
	var snmpServerName = rowObject["SnmpServerName"].replace(/ /g, "_");
	var editId = snmpServerName +"_server_edit_btn";
	return "<a href='#' id='"+ editId +"' +class='link' onclick=addServerListPopup('"+'EDIT'+"','"+rowObject["SnmpServerId"]+"'); ><i class='fa fa-pencil-square-o' aria-hidden='true'></i></a>";
} 

	$(document).ready(function() {
		
		getSnmpServerList();
		
	});
</script>

 	<a href="#divAddServerList" class="fancybox" style="display: none;" id="addsnmpServerListPopup">#</a>
		   	<div id="divAddServerList" style=" width:100%; display: none;" >
			   <jsp:include page="addSnmpServerConfigPopUp.jsp"></jsp:include> 
			</div>
			
				<a href="#divChangeSnmpServerStatus" class="fancybox" style="display: none;" id="updateSnmpServerStatus">#</a>
				
				<div id="divChangeSnmpServerStatus" style=" width:100%; display: none;" >
		    <div class="modal-content">
		        <div class="modal-header padding10">
		            <h4 class="modal-title"><spring:message code="change.snmp.server.status"></spring:message></h4>
		        </div>
		        <div class="modal-body padding10 inline-form">
		        	<span id="deleteDriverResponseMsg" style="display:none;">
		        		<jsp:include page="../common/responseMsgPopUp.jsp" ></jsp:include>
		        	</span>
			        <p id="active-snmp-server-warning" style="display:none;">
			       		 <spring:message code="snmp.server.change.status.active.message" ></spring:message>
			        </p>
			        <p id="inactive-snmp-server-warning" style="display:none;">
			       		 <spring:message code="snmp.server.change.status.inactive.message" ></spring:message>
			        </p>
			        
			        	
		        </div>
		        <sec:authorize access="hasAuthority('EDIT_SNMP_SERVER')">
			        <div id="inactive-snmp-server-div" class="modal-footer padding10">
			            <button id="enable_snmp_server_yes_btn" type="button" class="btn btn-grey btn-xs " onclick="changeSnmpServerStatus();"><spring:message code="btn.label.yes"></spring:message></button>
			            <button id="enable_snmp_server_no_btn" type="button" class="btn btn-grey btn-xs " data-dismiss="modal" onclick="setServerDefaultStatus();"><spring:message code="btn.label.no"></spring:message></button>
			        </div>
		        </sec:authorize>
		    </div>
		    <!-- /.modal-content --> 
		</div>
		
			<script>
			var jsSpringMsg = {};
			jsSpringMsg.failUpdateSnmpServerMsg = "<spring:message code='snmp.server.update.mapping.fail'></spring:message>";
			
		function getSnmpServerList(){
			
			$("#snmpServergrid").jqGrid({
	        	url: "<%=ControllerConstants.GET_SNMP_SERVERLIST%>",
	        	postData:
				{
	        		'snmpserverInstanceId':'${serverInstanceId}' 
				},
		
	            datatype: "json",
	            colNames:["#",
						  "<spring:message code='snmpServerList.grid.column.id' ></spring:message>",
						  "<spring:message code='snmpServerList.grid.column.id' ></spring:message>",
	                      "<spring:message code='snmpServerList.grid.column.name' ></spring:message>",
	                      "<spring:message code='snmpServerList.grid.column.ip' ></spring:message>",
	                      "<spring:message code='snmpServerList.grid.column.port' ></spring:message>",
	                      "<spring:message code='snmpServerList.grid.column.offset' ></spring:message>",
	                      "<spring:message code='snmpServerList.grid.column.community' ></spring:message>",
	                      "<spring:message code='snmpServerList.grid.column.snmpV3EngineId' ></spring:message>",
	                      "<spring:message code='snmpServerList.grid.column.enable' ></spring:message>",
	                      "<spring:message code='snmpServerList.grid.column.edit' ></spring:message>",
	                     ],
	                     colModel: [
							{name : '',index : '',formatter : ServercheckBoxFormatter,sortable : false,width : '20%'},
							{name:'id',index:'id',sortable:true,hidden:true},
	                    	{name:'SnmpServerId',index:'id',sortable:true,hidden:true},
	                    	{name:'SnmpServerName',index:'name',sortable:false},
	                    	{name:'SnmpServerHostIP',index:'hostIP',sortable:true},
	                    	{name:'SnmpServerPort',index:'port',align:'center',sortable:false},
	                    	{name:'SnmpServerPortOffset',index:'portOffset',align:'center',sortable:false},
	                    	{name:'SnmpServerCommunity',index:'community',align:'center',sortable:false},
	                    	{name:'snmpV3EngineId',index:'snmpV3EngineId',align:'center',sortable:false},
	                    	{name:'enable',index:'status',sortable:false,hidden:true,align:'center',formatter:snmpserverEnableColumnFormatter},
	                    	{name:'Edit',index:'edit',align:'center',sortable:false,formatter:editSnmpServerColumnFormatter}
	                    		],
	            rowNum:<%= MapCache.getConfigValueAsInteger(SystemParametersConstant.TOTAL_ROWS_TO_DISPLAY_IN_GRID, 10) %>,
	            rowList:[10,20,60,100],
	            height: 'auto',
	            mtype:'POST',
	    		sortname: 'id',
	    		sortorder: "asc",
	    		pager: "#snmpServergridPagingDiv",
	    		contentType: "application/json; charset=utf-8",
	    		viewrecords: true,
	    		multiselect: false,
	    		timeout : 120000,
	    	    loadtext: "Loading...",
	            caption: "<spring:message code="snmpConfiguration.add.serverList.label"></spring:message>",
	            beforeRequest:function(){
	                $(".ui-dialog-titlebar").hide();
	            }, 
	            beforeSend: function(xhr) {
	                xhr.setRequestHeader("Accept", "application/json");
	                xhr.setRequestHeader("Content-Type", "application/json");
	            },
	            beforeSelectRow: function(rowid, e) {
	            	var status = $(jQuery('#snmpServergrid').jqGrid ('getCell', rowid, 'enable')).closest("input").val();
	            	var $grid = $("#snmpServergrid");
 		           if(status == 'ACTIVE'){
 		        	  return false;
	     			}
 		           
 		           
 		  		 /* if($("#jqg_snmpServergrid_" + $grid.jqGrid ('getCell', rowid, 'SnmpServerId')).is(':checked')){
					if(ckSnmpServerSelected.indexOf($grid.jqGrid ('getCell', rowid, 'SnmpServerId')) == -1){
						
						
						ckSnmpServerSelected.push($grid.jqGrid ('getCell', rowid, 'SnmpServerId'));
					}
				}else{
					if(ckSnmpServerSelected.indexOf($grid.jqGrid ('getCell', rowid, 'SnmpServerId')) != -1){
						ckSnmpServerSelected.splice(ckSnmpServerSelected.indexOf($grid.jqGrid ('getCell', rowid, 'SnmpServerId')), 1);
					}
				} */
 		  		
	            	
	            },
	            loadComplete: function(data) {
	            
	     			 $(".ui-dialog-titlebar").show();
	     			
	     			var $jqgrid = $("#snmpServergrid");      
	     			$(".jqgrow", $jqgrid).each(function (index, row) {
	     				 
	     		        var $row = $(row);
	     		      	
     		            //Find the checkbox of the row and set it disabled
     		            var status = $(jQuery('#snmpServergrid').jqGrid ('getCell', row.id, 'enable')).closest("input").val();
     		       
     		           if(status == 'ACTIVE'){
     		        	  $row.find("input:checkbox").attr("disabled", "disabled");
   	     			}
     		         
	     		    });
	     			$('.checkboxbg').removeAttr("disabled");
	     			$('.checkboxbg').bootstrapToggle();
	     			$('.checkboxbg').change(function(){
	     				$("#serviceInstanceListPagingDiv").show();
	    			});
	     			addSNMPServerHideShow(data.rows);
	     			
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
	    		
	    		}).navGrid("#snmpServergridPagingDiv",{edit:false,add:false,del:false,search:false});
	    			$(".ui-jqgrid-titlebar").hide();
	    			$(".ui-pg-input").height("10px");
	
		}
		
		function ServercheckBoxFormatter(cellvalue, options, rowObject) {
			var snmpServerName = rowObject["SnmpServerName"].replace(/ /g, "_");
			return "<input type='checkbox' name='"+snmpServerName+"_server_chkbox"+"' id='"+snmpServerName+"_server_chkbox"+"' onclick=\"addRowIdForServer(\'"+snmpServerName+"_server_chkbox"+"\', \'"+rowObject["id"]+"\')\"; />";
		}

		function addRowIdForServer(elementId,snmpServerId)
		{
			var deviceElement = document.getElementById(elementId);
			if (deviceElement.checked) {
				if((ckSnmpServerSelected.indexOf(snmpServerId)) == -1){
					ckSnmpServerSelected.push(snmpServerId);
				}		
			}else{
				if(ckSnmpServerSelected.indexOf(snmpServerId) !== -1){
					ckSnmpServerSelected.splice(ckSnmpServerSelected.indexOf(snmpServerId), 1);
				}
			}
		}
		
function changeSnmpServerStatus(){
	resetWarningDisplay();
	clearAllMessages();
	var tempStatus ;

	if(snmpServerStatus == 'ACTIVE'){
		tempStatus = 'INACTIVE';
	}else{
		tempStatus = 'ACTIVE';
	}

	snmp_serverInstanceId = ${serverInstanceId};
	 $.ajax({
		url: '<%=ControllerConstants.UPDATE_SNMP_SERVER_STATUS%>',
		cache: false,
		async: true,
		dataType: 'json',
		type: "POST",
		data:
		 {
			"SnmpServerId"     		: snmpServerId,
			"snmpServerStatus" 		: tempStatus,
			"snmpServerInstanceId" 	: snmp_serverInstanceId
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
					$('#'+snmpServerId +'_'+snmpServerStatus).bootstrapToggle('on');
					$('#'+snmpServerId +'_'+snmpServerStatus).attr("id",'#'+snmpServerId +'_'+tempStatus);
				}else{
					$('#'+snmpServerId +'_'+snmpServerStatus).bootstrapToggle('off');
					$('#'+snmpServerId +'_'+snmpServerStatus).attr("id",'#'+snmpServerId +'_'+tempStatus);
				}
				
				showSuccessMsg(responseMsg);
				closeFancyBox();
				reloadGridData();
			}else if(responseObject != undefined && responseObject != 'undefined' && responseCode == "400"){
				showErrorMsg(responseMsg);
				$('#'+snmpServerId +'_'+snmpServerStatus).bootstrapToggle('toggle');
				closeFancyBox();
				reloadGridData();
			}else{
				resetWarningDisplay();
				clearAllMessages();
				showErrorMsg(responseMsg);
				$('#'+snmpServerId +'_'+snmpServerStatus).bootstrapToggle('toggle');
				closeFancyBox();
				reloadGridData();
			}
			$("#active-snmp-server-warning").hide();
			$("#inactive-snmp-server-warning").hide();
		},
	    error: function (xhr,st,err){
			handleGenericError(xhr,st,err);
		}
	}); 
}
function reloadGridData(){
	var $grid = $("#snmpServergrid");
	$grid.jqGrid('setGridParam',{datatype:'json',page: 1}).trigger('reloadGrid');
}
function setServerDefaultStatus(){
	
	if(snmpServerStatus == 'ACTIVE'){
		$('#'+snmpServerId +'_'+snmpServerStatus).bootstrapToggle('on');
	}else{
		$('#'+snmpServerId +'_'+snmpServerStatus).bootstrapToggle('off');
	}
	closeFancyBox();
}

function addSNMPServerHideShow(flag){
	if(flag){
		$('#addServiceIcon').hide();
		$('#addService').hide();
	} else {
		$('#addServiceIcon').show();
		$('#addService').show();
	}
}
		
</script>
		
		<div class="tab-content no-padding clearfix">
         	<div class="fullwidth">
   				<div class="title2">
   					<spring:message code="snmpConfiguration.add.serverList.label" ></spring:message>
	   				 <span class="title2rightfield">
				             
				             <sec:authorize access="hasAuthority('ADD_SNMP_SERVER')"> 	
				          	<span class="title2rightfield-icon1-text">
				          		<a href="#" id="addServiceIcon" onclick="addServerListPopup('ADD',0);" style="display: none"><i class="fa fa-plus-circle"></i></a>
	          					<a href="#" id="addService" onclick="addServerListPopup('ADD',0);" style="display: none">
	          						<spring:message code="btn.label.add" ></spring:message>
	          					</a>
		          			</span>	
				          	</sec:authorize>
				          
				          		<sec:authorize access="hasAuthority('DELETE_SNMP_SERVER')">
				          		<span class="title2rightfield-icon1-text">
				          		<a href="#" onclick="deletePopup();"> <i class="fa fa-trash"></i></a>
				          		<a href="#" id="deleteService" onclick="deletePopup();"><spring:message code="btn.label.delete" ></spring:message></a>
				          		</span>
				          		</sec:authorize>

				          </span>
		          </div>
   			</div>
	</div>
	
	
	
	<div class="box-body table-responsive no-padding box">
			<table class="table table-hover" id="snmpServergrid"></table>
	           	<div id="snmpServergridPagingDiv"></div> 
	         </div>
