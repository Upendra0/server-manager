<%@page import="com.elitecore.sm.common.constants.EngineConstants"%>
<%@page import="com.elitecore.sm.util.MapCache"%>
<%@page import="com.elitecore.sm.common.constants.SystemParametersConstant"%>
<%@page import="org.apache.catalina.connector.Request"%>
<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="/WEB-INF/ShowErrorTag.tld" prefix="elitecoreError"%>
<%@page import="com.elitecore.sm.common.constants.BaseConstants"%>


<div class="fullwidth mtop10">	

		<!-- Counter section start here -->
		
		<jsp:include page="../serviceCounterSummary.jsp"></jsp:include>
		<!-- Counter section end here -->			
			
		<!-- Jquery Driver Grid start here -->
		<div class="fullwidth">
			<div class="title2">
       			<spring:message code="processingService.summary.policy.list.heading"></spring:message>
      		</div>
      	</div>	
		
			<div class="box-body table-responsive no-padding box">
			<table class="table table-hover" id="policygrid"></table>
	        <div id="policyGridPagingDiv"></div> 
	          	
	        </div>
	     

		<!-- Jquery Driver Grid end here -->	
		<!-- Jquery Agent Grid start here -->		
		<jsp:include page="../serviceAgentList.jsp"></jsp:include>		
		<!-- Jquery Agent Grid end here -->	
		
		<!--  Jqeury All footer pop up start here -->
		<div id="divSynchronize" style=" width:100%; display: none;" >
		    <jsp:include page="../synchronizationPopUp.jsp"></jsp:include>
		</div>
		<div id="divImportConfig" style=" width:100%; display: none;" >
		    <jsp:include page="../importServiceWiseConfigPopUp.jsp"></jsp:include>
		</div>
		<jsp:include page="../commonServiceSyncPublish.jsp"></jsp:include>
		<div id="divStartService" style=" width:100%; display: none;" >
		    <jsp:include page="../startServicePopUp.jsp"></jsp:include>
		</div>
		<div id="divStopService" style=" width:100%; display: none;" >
		     <jsp:include page="../stopServicePopUp.jsp"></jsp:include>
		</div>
		
		<a href="#divSynchronize" class="fancybox" style="display: none;" id="synchronize">#</a>
       	<a href="#divImportConfig" class="fancybox" style="display: none;" id="importconfig">#</a>
       	<a href="#divStopService" class="fancybox" style="display: none;" id="stop_service">#</a>
       	<a href="#divStartService" class="fancybox" style="display: none;" id="start_service">#</a>
		<!--  Jquery All footer pop up end here  -->
		
		<form action="<%=ControllerConstants.INIT_DRIVER_CONFIGURATION%>" method="GET" id="driver-config-form">
		</form>
		<div style="display: none;">
		<form id="update_policy_form" method="POST" >
			<input type="hidden" id="server-instanceName" name="server-instanceName" value='${instance}'/>
			<input type="hidden" id="server-instanceHost" name="server-instanceHost" value='${host}'/>
			<input type="hidden" id="server-instancePort" name="server-instancePort" value='${port}'/>
			<input type="hidden" id="server-instanceId" name="server-instance-id" value='${instanceId}'/>
			<input type="hidden" id="serviceInstanceId" name="serviceInstanceId" value="${serviceInstanceId}">
			<input type="hidden" id="serviceId" name="serviceId" value="${serviceId}">
			<input type="hidden" id="serviceType" name="serviceType" value="<%=BaseConstants.PROCESSING_SERVICE%>">
			<input type="hidden" id="serviceName" name="serviceName" value="${serviceName}">
			<input type="hidden" id="REQUEST_ACTION_TYPE" name="REQUEST_ACTION_TYPE" value='POLICY'/>
		</form>
		
		<form action="<%=ControllerConstants.EXPORT_SERVICE_INSTANCE_CONFIG%>" id="export-service-instance-config-form" method="POST">
        	<input type="hidden" id="exportServiceInstanceId" name="exportServiceInstanceId"  value=""/>
        	<input type="hidden" id="isExportForDelete" name="isExportForDelete"  value=""/>
        	<input type="hidden" id="<%=BaseConstants.REQUEST_ACTION_TYPE %>" name="<%=BaseConstants.REQUEST_ACTION_TYPE %>" value="<%=BaseConstants.SERVICE_MANAGEMENT%>"/>
        </form>
	</div>
		
</div>	
<script type="text/javascript">
	$(document).ready(function() {
		loadJQueryPolicyGrid();		
		loadJqueryAgentGrid();		
		getServiceCounterDetails('<%=ControllerConstants.GET_COLLECTION_SERVICE_COUNTER_STATUS%>','${serviceId}','<%=EngineConstants.PROCESSING_SERVICE%>','ONLOAD');
	});

var ckIntanceSelected = new Array();

function loadJQueryPolicyGrid(){
	
	var serviceIdVal = '${serviceId}';
	$("#policygrid").jqGrid({
		url: "<%= ControllerConstants.GET_PROCESSING_POLICY_PATH_LIST%>",
		datatype: "json",
		postData: {serviceId: serviceIdVal},
		colNames: [
					  "<spring:message code='processingService.summary.grid.policy.id' ></spring:message>",
		           	  "<spring:message code='processingService.summary.grid.applied.policy' ></spring:message>",
		              "<spring:message code='processingService.summary.grid.read.file.path' ></spring:message>",
		              "<spring:message code='processingService.summary.grid.write.file.path' ></spring:message>",
			],
		colModel: [
			{name:'policyId',index:'id',sortable:true,hidden:true},
			{name:'policyName',index:'policyName',sortable:true,formatter:policyNameFormatter},
			{name:'readFilePath',index:'svcPathList.readFilePath',sortable:true},
			{name:'writeFilePath',index:'svcPathList.writeFilePath',sortable:true},
		],
		rowNum:<%= MapCache.getConfigValueAsInteger(SystemParametersConstant.TOTAL_ROWS_TO_DISPLAY_IN_GRID, 10) %>,
		rowList:[10,20,60,100],
		height: 'auto',
		sortname: 'id',
		sortorder: "desc",
		pager: "#policyGridPagingDiv",
		viewrecords: true,
		multiselect: false,
		loadonce:true, 
		timeout : 120000,
	    loadtext: "Loading...",
		caption: "<spring:message code="collectionService.summary.agent.grid.caption"></spring:message>",
		beforeRequest:function(){
	    },
		loadComplete: function(data) {
		},
		loadError : function(xhr,st,err) {
			handleGenericError(xhr,st,err);
		},
		recordtext: "<spring:message code="serverManagement.grid.pager.total.records.text"></spring:message>",
	    emptyrecords: "<spring:message code="collectionService.summary.agent.grid.empty.records"></spring:message>",
		loadtext: "<spring:message code="collectionService.summary.agent.grid.loading.text"></spring:message>",
		pgtext : "<spring:message code="collectionService.summary.agent.grid.pager.text"></spring:message>",
		beforeSelectRow: function (rowid, e){
		}
		}).navGrid("#policyGridPagingDiv",{edit:false,add:false,del:false,search:false});
			$(".ui-jqgrid-titlebar").hide();
			$(".ui-pg-input").height("10px");
}

function serviceAgentStatusColumnFormatter(cellvalue, options, rowObject){
	var toggleId = rowObject["agentId"] + "_" + cellvalue;
	
	if(cellvalue == 'ACTIVE'){
		return '<input class="checkboxbg" id=' + toggleId + ' data-on="On" data-off="Off" checked type="checkbox" data-size="mini">';
	}else{
		return '<input class="checkboxbg" id=' + toggleId + ' data-on="On" data-off="Off" type="checkbox" data-size="mini">';
	}
}


function policyNameFormatter(cellvalue, options, rowObject){
	
	return '<a class="link" onclick="viewPolicyDetails('+"'" + rowObject["policyId"]+ "','"+rowObject["policyName"]+"','Policy'" + ');">' + cellvalue + '</a>' ;
	
}


function exportConfigPopup(){
	$("#exportServiceInstanceId").val($("#serviceId").val()); // set service instance id which is selected for export to submit with form.
	$("#isExportForDelete").val(false);
	$("#REQUEST_ACTION_TYPE").val('<%=BaseConstants.PROCESSING_SERVICE_SUMMARY%>');
	$("#export-service-instance-config-form").submit();
}

function viewPolicyDetails(id,name,des){
		var serverId = $("#server-instanceId").val();
		if(serverId != null && serverId != ''){
			$("#server-instance-id").val(serverId);
		}
		$("#policyId").val(id);
		$("#policyName").val(name);
		$("#policyDes").val(des);
		$("#update_policy_form").attr("action","<%=ControllerConstants.INIT_BUSINESS_POLICY_MGMT%>");
		$("#update_policy_form").submit();

}




</script>

