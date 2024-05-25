<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page
	import="com.elitecore.sm.common.constants.SystemParametersConstant"%>
<%@page import="com.elitecore.sm.util.MapCache"%>
<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="elitecore"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="/WEB-INF/ShowErrorTag.tld" prefix="elitecoreError"%>
<%@ taglib uri="http://www.springframework.org/security/tags"
	prefix="sec"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">


<form:form modelAttribute="agent_form_bean" method="POST" action="#"
	id="update-packetStatastic-agent-list">

	<div id="packetStatasticAgentParameterDiv"
		class="fullwidth inline-form">

		<input type="hidden" id="server_Instance_Id" name="server_Instance_Id"
			value='${serverInstanceId}' /> <input type="hidden" id="PackagentId"
			name="PackagentId" value="${agentId}" />
		<jsp:include page="../common/responseMsgPopUp.jsp" ></jsp:include>
		<div class="col-md-12 no-padding">
			<spring:message code="packetStatasticAgent.storage.path.name"
				var="name" ></spring:message>

			<spring:message code="packetStatasticAgent.storage.path.tooltip"
				var="tooltip" ></spring:message>

			<div class="form-group">
				<div class="table-cell-label">${name}<span class="required">*</span>
				</div>
				<div class="input-group ">
					<form:input path="storageLocation"
						cssClass="form-control table-cell input-sm" tabindex="4"
						id="storageLocation" data-toggle="tooltip" data-placement="bottom"
						title="${tooltip}" ></form:input>
					<spring:bind path="storageLocation">
						<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
					</spring:bind>
				</div>
			</div>
		</div>
		<div class="col-md-12 no-padding">
			<spring:message code="packetStatasticAgent.execution.interval.name"
				var="name" ></spring:message>

			<spring:message
				code="packetStatasticAgent.execution.interval.tooltip" var="tooltip" ></spring:message>

			<div class="form-group">
				<div class="table-cell-label">${name}<span class="required">*</span>
				</div>
				<div class="input-group ">
					<form:input path="executionInterval"
						cssClass="form-control table-cell input-sm" tabindex="4"
						id="executionInterval" data-toggle="tooltip"
						data-placement="bottom" title="${tooltip}"
						onkeydown="isNumericOnKeyDown(event)" ></form:input>
					<spring:bind path="executionInterval">
						<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
					</spring:bind>
				</div>
			</div>
		</div>
	</div>


</form:form>



<div class="fullwidth">
	<div class="button-set">
		<div id="packetStatButtonDIv">
			<sec:authorize access="hasAuthority('EDIT_PACKET_STATASTIC_AGENT')">
				<button id="update_packet_stats_btn" class="btn btn-grey btn-xs " tabindex="21"
					onclick="updatePacketStatDetails()">
					<spring:message code="btn.label.update" ></spring:message>
				</button>
			</sec:authorize>


		</div>

	</div>
</div>

<div id="update-packet-stat-progress-bar-div"
	 style="display: none;">
	<jsp:include page="../common/processing-bar.jsp"></jsp:include>
</div>

<div class="tab-content no-padding clearfix">
         	<div class="fullwidth">
   				<div class="title2">
   					<spring:message code="packetStata.agent.grid.serviceList.label" ></spring:message>

		          </div>
   			</div>
	</div>

<div class="box-body table-responsive no-padding box">
			<table class="table table-hover" id="packetStatasticAgentDiv"></table>
	           	<div id="packetStatasticAgentgridPagingDiv"></div> 
	         </div>
	         
	   <a href="#divChangepacketStatasticAgentStatus" class="fancybox" style="display: none;" id="updatepacketStatasticAgentStatus">#</a>
				
				<div id="divChangepacketStatasticAgentStatus" style=" width:100%; display: none;" >
		    <div class="modal-content">
		        <div class="modal-header padding10">
		            <h4 class="modal-title"><spring:message code="packet.statstic.enable.offline.header.label"></spring:message></h4>
		        </div>
		        <div class="modal-body padding10 inline-form">
		        	<span id="divChangepacketStatasticAgentStatusrResponseMsg" style="display:none;">
		        		<jsp:include page="../common/responseMsgPopUp.jsp" ></jsp:include>
		        	</span>
			        <p id="offline-service-agent-warning" style="display:none;">
			       		 <spring:message code="offline.service.agent.warning.message" ></spring:message>
			        </p>
			        <p id="offline-service-agent-off-warning" style="display:none;">
			       		 <spring:message code="offline.service.agent.off.warning.message" ></spring:message>
			        </p>
			        	
		        </div>
		        <sec:authorize access="hasAuthority('EDIT_PACKET_STATASTIC_AGENT')">
			        <div id="inactive-snmp-server-div" class="modal-footer padding10">
			            <button id="changepacketstats_btn" type="button" class="btn btn-grey btn-xs " onclick="changePacketStatasticAgentStatus();"><spring:message code="btn.label.yes"></spring:message></button>
			            <button id="cancelpacketstats_btn" type="button" class="btn btn-grey btn-xs " data-dismiss="modal" onclick="setpacketStatasticAgentDefaultStatus();"><spring:message code="btn.label.no"></spring:message></button>
			            
			        </div>
		        </sec:authorize>
		    </div>
		    <!-- /.modal-content --> 
		</div>
<script>

function reloadPacketStatAgentGridData(){
	var $grid = $("#packetStatasticAgentDiv");
	$grid.jqGrid('setGridParam',{datatype:'json',page: 1}).trigger('reloadGrid');
}

function setpacketStatasticAgentDefaultStatus(){
	
	if(packetStatasticAgentServiceStatus == 'true'){
		$('#'+packetStatasticAgentServiceId +'_'+packetStatasticAgentServiceStatus).bootstrapToggle('on');
	}else{
		$('#'+packetStatasticAgentServiceId +'_'+packetStatasticAgentServiceStatus).bootstrapToggle('off');
	}
	closeFancyBox();
}

function updatePacketStatDetails(){
	resetWarningDisplay();
	clearAllMessages();
	$("#packetStatButtonDIv").hide();
	showProgressBar();
	
	$.ajax({
		url: '<%=ControllerConstants.UPDATE_PACKET_STATASTIC_AGENT%>',
		cache: false,
		async: true, 
		dataType: 'json',
		type: "POST",
		data: 
		{
			
			
			"storageLocation" 					:	$("#storageLocation").val(),
			"executionInterval" 				:	$("#executionInterval").val(),
			"id"								:	$("#PackagentId").val(),
			"serverInstance.id"					:   $("#server_Instance_Id").val(),
			
			
		},
		success: function(data){
			hideProgressBar();


			var response = eval(data);
			
			var responseCode = response.code; 
			var responseMsg = response.msg; 
			var responseObject = response.object;
			 
			if(responseCode == "200"){
				
				$("#packetStatButtonDIv").show();
				showSuccessMsg(responseMsg);
				reloadPacketStatAgentGridData();
				
			}else if(responseObject != undefined && responseObject != 'undefined' && responseCode == "400"){
				
				$("#packetStatButtonDIv").show();
				
				addErrorIconAndMsgForAjax(responseObject);
			}else{
				
				resetWarningDisplayPopUp();
				$("#packetStatButtonDIv").show();
				showErrorMsg(responseMsg);
				reloadPacketStatAgentGridData();
			}
		},
	    error: function (xhr,st,err){
	    	hideProgressBar();
	    	$("#packetStatButtonDIv").show();
			handleGenericError(xhr,st,err);
			reloadPacketStatAgentGridData();
		}
	});
}

function changePacketStatasticAgentStatus(){
	resetWarningDisplay();
	clearAllMessages();
	var tempStatus ;

	if(packetStatasticAgentServiceStatus == 'true'){
		tempStatus = 'false';
	}else{
		tempStatus = 'true';
	}

	 $.ajax({
		url: '<%=ControllerConstants.UPDATE_PACKET_STATASTIC_AGENT_STATUS%>',
		cache: false,
		async: true,
		dataType: 'json',
		type: "POST",
		data:
		 {
			"svcAgentId"     						: packetStatasticAgentServiceId,
			"svcAgentStatus" 						: tempStatus,
			
		}, 
		
		success: function(data){
			
			var response = eval(data);
			var responseCode = response.code; 
			var responseMsg = response.msg; 
			var responseObject = response.object;
			if(responseCode == "200"){
				resetWarningDisplay();
				clearAllMessages();
				reloadPacketStatAgentGridData();
				if(tempStatus == 'true'){
					$('#'+packetStatasticAgentServiceId +'_'+packetStatasticAgentServiceStatus).bootstrapToggle('on');
					$('#'+packetStatasticAgentServiceId +'_'+packetStatasticAgentServiceStatus).attr("id",'#'+packetStatasticAgentServiceId +'_'+tempStatus);
				}else{
					$('#'+packetStatasticAgentServiceId +'_'+packetStatasticAgentServiceStatus).bootstrapToggle('off');
					$('#'+packetStatasticAgentServiceId +'_'+packetStatasticAgentServiceStatus).attr("id",'#'+packetStatasticAgentServiceId +'_'+tempStatus);
				}
				reloadPacketStatAgentGridData();
				showSuccessMsg(responseMsg);
				closeFancyBox();
				
			}else if(responseObject != undefined && responseObject != 'undefined' && responseCode == "400"){
				showErrorMsg(responseMsg);
				$('#'+packetStatasticAgentServiceId +'_'+packetStatasticAgentServiceStatus).bootstrapToggle('toggle');
				reloadPacketStatAgentGridData();
				closeFancyBox();
				
			}else{
				resetWarningDisplay();
				clearAllMessages();
				showErrorMsg(responseMsg);
				$('#'+packetStatasticAgentServiceId +'_'+packetStatasticAgentServiceStatus).bootstrapToggle('toggle');
				closeFancyBox();
				reloadPacketStatAgentGridData();
			}
			$("#offline-service-agent-warning").hide();
			$("#offline-service-agent-off-warning").hide();
			
		},
	    error: function (xhr,st,err){
			handleGenericError(xhr,st,err);
		}
	}); 
}

function showProgressBar(){
	$("#packetStatButtonDIv").hide();
	$("#update-packet-stat-progress-bar-div").show();
}

function hideProgressBar(){
	$("#packetStatButtonDIv").show();
	$("#update-packet-stat-progress-bar-div").hide();	
}

$(document).ready(function() {
	
	getPacketStatasticServiceList();
	
});

var packetStatasticAgentServiceStatus,packetStatasticAgentServiceId,svcCategory;
function packetStataAgentStatusEnableColumnFormatter(cellvalue, options, rowObject){
	var toggleId = rowObject["id"] + "_" + cellvalue;
	var serviceName = rowObject["servicePacketStatsSvcName"].replace(/ /g, "_"); 
	var toggleDivId = serviceName + "_enable_btn";
	var toggleCheckboxId = serviceName + "_toggle_checkbox";
	
	if(cellvalue == 'true'){
		return '<div id="'+toggleDivId+'"><input class="checkboxbg" id=' + toggleCheckboxId + ' value= '+cellvalue+' data-on="On" data-off="Off" checked type="checkbox" data-size="mini" onchange = "packetStataAgentStatusPopup('+"'" + rowObject["id"]+ "','"+rowObject["servicePacketStatsSvcCategory"]+ "','"+rowObject["enable"]+"'" + ');"></div>';
	}else{
		return '<div id="'+toggleDivId+'"><input class="checkboxbg" id=' + toggleCheckboxId + ' value= '+cellvalue+' data-on="On" data-off="Off" type="checkbox" data-size="mini"  onchange="packetStataAgentStatusPopup('+"'" + rowObject["id"]+ "','"+rowObject["servicePacketStatsSvcCategory"]+ "','"+rowObject["enable"]+"'" + ');"   ></div>';
	}
}

function packetStataAgentStatusPopup(id,category,status){

	resetWarningDisplay();
	clearAllMessages();
 	packetStatasticAgentServiceId = id;
 	packetStatasticAgentServiceStatus = status;
	svcCategory=category;
	if(status == 'false'){
		$("#offline-service-agent-warning").show();
		$("#updatepacketStatasticAgentStatus").click();
	}
	else{
		$("#offline-service-agent-off-warning").show();
		$("#updatepacketStatasticAgentStatus").click();
		//changePacketStatasticAgentStatus();
	}
	
}

function getPacketStatasticServiceList(){
	
	$("#packetStatasticAgentDiv").jqGrid({
    	url: "<%=ControllerConstants.GET_AGENT_SERVICELIST%>",
    	postData:
		{
    		'agentId':$("#PackagentId").val() 
		},

        datatype: "json",
        colNames:[
				  "<spring:message code='packetStata.agent.grid.column.id' ></spring:message>",
				  "<spring:message code='packetStata.agent.grid.column.svc.id' ></spring:message>",
                  "<spring:message code='packetStata.agent.grid.column.svc.name' ></spring:message>",
                  "<spring:message code='packetStata.agent.grid.column.svc.type' ></spring:message>",
                  "<spring:message code='packetStata.agent.grid.column.svc.category' ></spring:message>",
                  "<spring:message code='packetStata.agent.grid.column.enable' ></spring:message>",
                  ""
                  
                 ],
                 colModel: [
					{name:'id',index:'id',sortable:true,hidden:true},
                	{name:'servicePacketStatsId',index:'servicePacketStatsId',sortable:false},
                	{name:'servicePacketStatsSvcName',index:'servicePacketStatsSvcName',sortable:false},
                	{name:'servicePacketStatsSvcType',index:'servicePacketStatsSvcType',sortable:false},
                	{name:'servicePacketStatsSvcCategory',index:'servicePacketStatsSvcCategory',align:'center',sortable:false,hidden:true},
                	{name:'enable',index:'enable',sortable:false, align:'center',formatter:packetStataAgentStatusEnableColumnFormatter},
                	{name:'serverType',index:'serverType',align:'center',sortable:false,hidden:true}
                		],
        rowNum:<%= MapCache.getConfigValueAsInteger(SystemParametersConstant.TOTAL_ROWS_TO_DISPLAY_IN_GRID, 10) %>,
        rowList:[10,20,60,100],
        height: 'auto',
        mtype:'POST',
		sortname: 'id',
		sortorder: "asc",
		pager: "#packetStatasticAgentgridPagingDiv",
		contentType: "application/json; charset=utf-8",
		viewrecords: true,
		multiselect: false,
		timeout : 120000,
	    loadtext: "Loading...",
        caption: "<spring:message code="packetStata.agent.grid.serviceList.label"></spring:message>",
        beforeRequest:function(){
            $(".ui-dialog-titlebar").hide();
        }, 
        beforeSend: function(xhr) {
            xhr.setRequestHeader("Accept", "application/json");
            xhr.setRequestHeader("Content-Type", "application/json");
        },
        beforeSelectRow: function(rowid, e) {
       
        	
        },
        loadComplete: function(data) {
        
 			var $jqgrid = $("#packetStatasticAgentDiv");      
  			$(".jqgrow", $jqgrid).each(function (index, row) {
  				 
  		        var $row = $(row);
  		      	
		            //Find the checkbox of the row and set it disabled
		           
		            var svcCategory = $('#packetStatasticAgentDiv').jqGrid ('getCell', row.id, 'servicePacketStatsSvcCategory');
		            var svcType = $('#packetStatasticAgentDiv').jqGrid ('getCell', row.id, 'servicePacketStatsSvcType');
		            var serverType = $('#packetStatasticAgentDiv').jqGrid ('getCell', row.id, 'serverType');
		            if(svcCategory == 'ONLINE' || ((svcType == 'Parsing Service') && serverType ==  'MEDIATION')){
		       			$row.find("input:checkbox").attr("disabled", "disabled");
		       			
		       		}
		         
  		    }); 
  			$('#packetStatasticAgentDiv .checkboxbg').bootstrapToggle();
 			
 			
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
		
		}).navGrid("#packetStatasticAgentgridPagingDiv",{edit:false,add:false,del:false,search:false});
			$(".ui-jqgrid-titlebar").hide();
			$(".ui-pg-input").height("10px");

}
</script>
