<%@page import="com.elitecore.sm.common.constants.BaseConstants"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@page import="com.elitecore.sm.util.MapCache"%>
<%@page import="com.elitecore.sm.common.constants.SystemParametersConstant"%>
<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<sec:authorize access="hasAnyAuthority('SYSTEM_AGENT_CONFIGURATION')">
<div class="fullwidth">
	<div class="title2">
		<spring:message code="parsingService.summary.section.system.agent.heading" ></spring:message>
       </div>
</div>

<div class="box-body table-responsive no-padding box">
	<table class="table table-hover" id="agentgrid">
	</table>
	<div id="agentGridPagingDiv"></div>
</div>

 <form action="<%= ControllerConstants.SPECIFIC_SYSTEM_AGENT_CONFIG %>" method="POST" id="agent-config-form">
    		<input type="hidden" id="agent_server_Instance_Id" name="agent_server_Instance_Id" value='${instanceId}'/>
    		<input type="hidden" id="systemAgentTypeId" name="systemAgentTypeId"/>
    		<input type="hidden" id="<%=BaseConstants.REQUEST_ACTION_TYPE %>" name="<%=BaseConstants.REQUEST_ACTION_TYPE %>" value="<%=BaseConstants.UPDATE_SYSTEM_AGENT_CONFIG%>"/>
 </form>
</sec:authorize>
<script type="text/javascript">

function loadJqueryAgentGrid(){
	<sec:authorize access="hasAuthority('PACKET_STATASTIC_AGENT') or hasAuthority('FILE_RENAME_AGENT')">
	$("#agentgrid").jqGrid({
	url: "<%= ControllerConstants.GET_AGENT_LIST_SUMMARY %>",
	mtype:"POST",
	postData:
	{
		'serverInstanceId':'${instanceId}' ,
		'serviceId':'${serviceId}',
		'isServerInstanceSummary': 'false',
	},
	datatype: "json",
	colNames: [
	           	  "<spring:message code='service.summary.grid.column.agent.agentId' ></spring:message>",
	              "<spring:message code='service.summary.grid.column.agent.typeOfAgent' ></spring:message>",
	              "<spring:message code='service.summary.grid.column.agent.agentTypeId' ></spring:message>",
	              "<spring:message code='service.summary.grid.column.agent.lastExecutionDate' ></spring:message>",
	              "<spring:message code='service.summary.grid.column.agent.nextExecutionDate' ></spring:message>",
	              "<spring:message code='service.summary.grid.column.agent.serviceStatus' ></spring:message>",
	              "<spring:message code='service.summary.grid.column.agent.agentStatus' ></spring:message>",
		],
	colModel: [
		{name:'id',index:'id',sortable:true,hidden: true},
		{name:'typeOfAgent',index:'agentType.type',sortable:false,formatter:agentnameFormatter},
		{name:'agentTypeId',index:'agentTypeId',hidden:true},
		{name:'lastExecutionDate',index:'lastExecutionDate',sortable:false,align:'center',formatter: lastExecutionDateFormatter},
		{name:'nextExecutionDate',index:'nextExecutionDate',sortable:false,align:'center',formatter : nextExecutionDateFormatter},
		{name:'serviceStatus',index:'serviceStatus',sortable:false,align:'center', formatter:serviceStateFormatter },
		{name:'agentStatus',index:'agentStatus',sortable:false,align:'center', formatter:agentStateFormatter }
	],
	rowNum:<%= MapCache.getConfigValueAsInteger(SystemParametersConstant.TOTAL_ROWS_TO_DISPLAY_IN_GRID, 10) %>,
	rowList:[10,20,60,100],
	height: 'auto',
	sortname: 'id',
	sortorder: "desc",
	pager: "#agentGridPagingDiv",
	viewrecords: true,
	multiselect: false,
	timeout : 120000,
    loadtext: "Loading...",
	caption: "<spring:message code="parsingService.summary.agent.grid.caption"></spring:message>",
	
	loadComplete: function(data) {		
		var $jqgrid = $("#agentgrid");      
		$(".jqgrow", $jqgrid).each(function (index, row) {
			 
	        var $row = $(row);
	      	
            //Find the checkbox of the row and set it disabled
            var agentType = $(jQuery('#agentgrid').jqGrid ('getCell', row.id, 'typeOfAgent')).closest("a").html();
       		if(agentType == 'Packet Statistics Agent'){
       			 $row.find("input:checkbox").attr("disabled", "disabled");	       			
       		}
       		$row.hide();
	       	<sec:authorize access="hasAuthority('PACKET_STATASTIC_AGENT')">
		       	if(agentType == 'Packet Statistics Agent'){
				$row.show();				       			
       		}
	       	</sec:authorize>
	       	
	        <sec:authorize access="hasAuthority('FILE_RENAME_AGENT')">
		        if(agentType == 'File Rename Agent'){
       			 $row.show();	       			
       		}
	        </sec:authorize>
       	
	    }); 		
		$('#agentgrid .checkboxbg').attr("disabled", "disabled");
		$('#agentgrid .checkboxbg').bootstrapToggle();
		
	},
	loadError : function(xhr,st,err) {
		handleGenericError(xhr,st,err);
	},
	recordtext: "<spring:message code="serverManagement.grid.pager.total.records.text"></spring:message>",
    emptyrecords: "<spring:message code="parsingService.summary.agent.grid.empty.records"></spring:message>",
	loadtext: "<spring:message code="parsingService.summary.agent.grid.loading.text"></spring:message>",
	pgtext : "<spring:message code="parsingService.summary.agent.grid.pager.text"></spring:message>",
	beforeSelectRow: function (rowid, e){
	}
	}).navGrid("#agentGridPagingDiv",{edit:false,add:false,del:false,search:false});
		$(".ui-jqgrid-titlebar").hide();
	</sec:authorize>
		
	}
	function agentnameFormatter(cellvalue, options, rowObject){
		
		return '<a  class = "link" style="cursor: pointer;" onclick="redirectAgentConfig('+"'" + rowObject["agentTypeId"]+ "'"+')">' + cellvalue + '</a>' ;
	}

	function agentStateFormatter(cellvalue, options, rowObject){
		var toggleId = rowObject["id"] + "_agentStat_" + cellvalue;
		if(cellvalue == 'INACTIVE'){
			return '<input class="checkboxbg" id="'+ toggleId +'" onchange="" data-switch-no-init="" type="checkbox">';
		}else{
			return '<input class="checkboxbg" id="'+ toggleId +'" onchange="" data-switch-no-init="" type="checkbox" checked="checked">';
		}
	}
	
	function serviceStateFormatter(cellvalue, options, rowObject){
		var toggleId = rowObject["id"] + "_serviceStat_" + cellvalue;
		
		if(cellvalue == 'false'){
			return '<input class="checkboxbg" id="'+ toggleId +'" onchange="" data-switch-no-init="" type="checkbox">';
		}else{
			return '<input class="checkboxbg" id="'+ toggleId +'" onchange="" data-switch-no-init="" type="checkbox" checked="checked">';
		}
	}
	

	function redirectAgentConfig(agentTypeId){
		$("#systemAgentTypeId").val(agentTypeId);
		$("#agent-config-form").submit();
	}
	
	function lastExecutionDateFormatter(cellvalue, options, rowObject){
		
		var toggleId = rowObject["id"] + "_lastExeTime";
		loadAgentInfomation(rowObject["id"],rowObject["typeOfAgent"]);
		return '<div id="agent_last_exeTime_'+rowObject["id"]+'"><img src="img/preloaders/circle-red.gif" width="20px" data-toggle="tooltip" data-placement="bottom" title="Loading Status.." height="20px"></div>';
		
	}
	
	function nextExecutionDateFormatter(cellvalue, options, rowObject){
		
		var toggleId = rowObject["id"] + "_nextExeTime";
		return '<div id="agent_next_exeTime_'+rowObject["id"]+'"><img src="img/preloaders/circle-red.gif" width="20px" data-toggle="tooltip" data-placement="bottom" title="Loading Status.." height="20px"></div>';
		
	}
	
	function loadAgentInfomation(agentId,agenttype){
		
		$.ajax({
			url: '<%=ControllerConstants.LOAD_AGENT_INFORMATION%>',
			cache: false,
			async: true,
			dataType: 'json',
			type: "POST",
			data: {
				"serverInstanceId": '${instanceId}',
				"agentType"		  :	agenttype,
			},
			success: function(data){
				
				var response = eval(data);
				 
				 var responseCode = response.code; 
				 var responseMsg = response.msg; 
				 var responseObject = response.object;
				 if(responseCode == "200"){
					
					 $('#agent_last_exeTime_'+agentId).html('<p>'+responseObject["lastExecutionDate"]+"</p>");
					 $('#agent_next_exeTime_'+agentId).html('<p>'+responseObject["nextExecutionDate"]+"</p>");
					
				 }
				
				
			},
		    error: function (xhr,st,err){
				handleGenericError(xhr,st,err);
			}
		});
	}

</script>
