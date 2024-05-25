<%@page import="com.elitecore.sm.common.constants.BaseConstants"%>
<%@page import="com.elitecore.sm.util.MapCache"%>
<%@page
	import="com.elitecore.sm.common.constants.SystemParametersConstant"%>
<%@page import="org.apache.catalina.connector.Request"%>
<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<%@ taglib uri="http://www.springframework.org/security/tags"
	prefix="sec"%>
	<%@page import="com.elitecore.sm.common.constants.EngineConstants"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="/WEB-INF/ShowErrorTag.tld" prefix="elitecoreError"%>


<div class="fullwidth mtop10">

	<!-- Counter section start here -->

	<jsp:include page="../serviceCounterSummary.jsp"></jsp:include>

	<!-- Counter section end here -->

	<!-- Jquery Driver Grid start here -->
	<div class="fullwidth" id="clientGridHeadDiv">
		<div class="title2">
			<spring:message
				code="netflow.collectionService.summary.client.grid.caption" ></spring:message>
		</div>
	</div>

	<div class="box-body table-responsive no-padding box">
		<table class="table table-hover" id="clientGrid"></table>
		<div id="clientGridPagingDiv"></div>
	</div>

	<!-- Jquery Agent Grid start here -->	
	<jsp:include page="../serviceAgentList.jsp"></jsp:include>	
	<!-- Jquery Agent Grid end here -->

	<!--  Jqeury All footer pop up start here -->
	<div id="divSynchronize" style="width: 100%; display: none;">
		<jsp:include page="../synchronizationPopUp.jsp"></jsp:include>
	</div>
	<div id="divImportConfig" style="width: 100%; display: none;">
		<jsp:include page="../importServiceWiseConfigPopUp.jsp"></jsp:include>
	</div>
	<jsp:include page="../commonServiceSyncPublish.jsp"></jsp:include>
	<div id="divStartService" style="width: 100%; display: none;">
		<jsp:include page="../startServicePopUp.jsp"></jsp:include>
	</div>
	<div id="divStopService" style="width: 100%; display: none;">
		<jsp:include page="../stopServicePopUp.jsp"></jsp:include>
	</div>

	<a href="#divSynchronize" class="fancybox" style="display: none;"
		id="synchronize">#</a> <a href="#divImportConfig" class="fancybox"
		style="display: none;" id="importconfig">#</a> <a
		href="#divStopService" class="fancybox" style="display: none;"
		id="stop_service">#</a> <a href="#divStartService" class="fancybox"
		style="display: none;" id="start_service">#</a>


	<form action="<%=ControllerConstants.EXPORT_SERVICE_INSTANCE_CONFIG%>"
		id="export-service-instance-config-form" method="POST">
		<input type="hidden" id="exportServiceInstanceId"
			name="exportServiceInstanceId" value="" /> <input type="hidden"
			id="isExportForDelete" name="isExportForDelete" value="" /> <input
			type="hidden" id="<%=BaseConstants.REQUEST_ACTION_TYPE%>"
			name="<%=BaseConstants.REQUEST_ACTION_TYPE%>"
			value="<%=BaseConstants.SERVICE_MANAGEMENT%>" />
	</form>
	<form
		action="<%=ControllerConstants.INIT_DRIVER_CONFIGURATION%>"
		method="POST" id="driver-config-form">
		<input type="hidden" id="driverId" name="driverId" /> <input
			type="hidden" id="driverTypeAlias" name="driverTypeAlias" /> <input
			type="hidden" id="serviceId" name="serviceId" value="${serviceId}" />
		<input type="hidden" id="serviceName" name="serviceName"
			value="${serviceName}" /> <input type="hidden" id="serverInstanceId"
			name="serverInstanceId" value="${instanceId}" />
			 <input type="hidden" id="instanceId"
			name="instanceId" value="${serverInstanceId}" />
	</form>
</div>

<!-- Client state popup code start here -->
<div id="divMessage" style="width: 100%; display: none;">
	<input type="hidden" id="clientId" />
	<div class="modal-content">
		<div class="modal-header padding10">
			<h4 class="modal-title" id="warning-title">
				<spring:message code="serverManagement.warn.popup.header" ></spring:message>
			</h4>
		</div>

		<div class="modal-body padding10 inline-form">
			<p id="clientMsg">
				<spring:message code="client.change.on.status" ></spring:message>
			</p>
		</div>

		<div class="modal-footer padding10">
			<sec:authorize access="hasAnyAuthority('UPDATE_COLLECTION_CLIENT')">
				<button type="button" class="btn btn-grey btn-xs "
					data-dismiss="modal" onclick="" id="updtBtn">
					<spring:message code="btn.label.yes" ></spring:message>
				</button>
			</sec:authorize>
			<button type="button" class="btn btn-grey btn-xs "
				data-dismiss="modal" id="noBtn"
				onclick="closeFancyBox();resetToggle();">
				<spring:message code="btn.label.no" ></spring:message>
			</button>
		</div>
	</div>
	<!-- /.modal-content -->
</div>
<a href="#divMessage" class="fancybox" style="display: none;"
	id="stateMessage">#</a>
<!-- Client state popup code end here -->

<script type="text/javascript">
	$(document).ready(function() {
		<sec:authorize access="hasAnyAuthority('VIEW_COLLECTION_CLIENT')">
			 if('${serviceType}'=='MQTT_COLLECTION_SERVICE'){
					loadMqttClientGrid();
			 } else if('${serviceType}'=='COAP_COLLECTION_SERVICE') {
				    loadCoAPClientGrid();
			 } else{
					loadClientGrid();
			 }
		</sec:authorize>
		<sec:authorize access="!hasAnyAuthority('VIEW_COLLECTION_CLIENT')">
			$('#clientGridHeadDiv').hide();
		</sec:authorize>
		
		
		if('${serviceType}'=='NATFLOWBINARY_COLLECTION_SERVICE'){
			
			
			getServiceCounterDetails('<%=ControllerConstants.GET_COLLECTION_SERVICE_COUNTER_STATUS%>','${serviceId}','<%=EngineConstants.NATFLOWBINARY_COLLECTION_SERVICE%>','ONLOAD');
			}
			else if('${serviceType}'=='NATFLOW_COLLECTION_SERVICE'){
				
				getServiceCounterDetails('<%=ControllerConstants.GET_COLLECTION_SERVICE_COUNTER_STATUS%>','${serviceId}','<%=EngineConstants.NATFLOW_COLLECTION_SERVICE%>','ONLOAD');
			}
			else if('${serviceType}'=='SYSLOG_COLLECTION_SERVICE'){
				
				getServiceCounterDetails('<%=ControllerConstants.GET_COLLECTION_SERVICE_COUNTER_STATUS%>','${serviceId}','<%=EngineConstants.SYSLOG_COLLECTION_SERVICE%>','ONLOAD');
			}
			else if('${serviceType}'=='GTPPRIME_COLLECTION_SERVICE'){
				
				getServiceCounterDetails('<%=ControllerConstants.GET_COLLECTION_SERVICE_COUNTER_STATUS%>','${serviceId}','<%=EngineConstants.GTPPRIME_COLLECTION_SERVICE%>','ONLOAD');
			
			} else if ('${serviceType}'=='RADIUS_COLLECTION_SERVICE'){
				
				getServiceCounterDetails('<%=ControllerConstants.GET_COLLECTION_SERVICE_COUNTER_STATUS%>','${serviceId}','<%=EngineConstants.RADIUS_COLLECTION_SERVICE%>','ONLOAD');
			}		
		loadJqueryAgentGrid();		
	});
	
function loadClientGrid(){
	$("#clientGrid").jqGrid({
		url: "<%=ControllerConstants.GET_NETFLOW_COLLECTION_CLIENT_LIST%>",
		postData:
				{'serviceId':'${serviceId}' 
				},
		datatype: "json",
		colNames: [
					  "<spring:message code='netflow.collectionService.client.grid.column.client.id' ></spring:message>",
		           	  "<spring:message code='netflow.collectionService.client.grid.column.client.nm' ></spring:message>",
		              "<spring:message code='netflow.collectionService.client.grid.column.client.ip' ></spring:message>",
		              "<spring:message code='netflow.collectionService.client.grid.column.client.port' ></spring:message>",
		              "<spring:message code='netflow.collectionService.client.grid.column.client.file.loc' ></spring:message>",
		              "<spring:message code='netflow.collectionService.client.grid.column.client.status' ></spring:message>"
			],
		colModel: [
			{name:'clientId',index:'id',sortable:false,hidden:true},
			{name:'clientName',index:'name',sortable:false,formatter:clientNameFormatter},
			{name:'clientIp',index:'clientIpAddress',sortable:true,align:'center'},
			{name:'clientPort',index:'clientPort',sortable:true,align:'center'},
			{name:'fileLoc',index:'outFileLocation',align:'left',sortable:false},
			{name:'status',index:'status',align:'center',formatter:clientStatusFormatter}
		],
		rowNum:<%=MapCache.getConfigValueAsInteger(
					SystemParametersConstant.TOTAL_ROWS_TO_DISPLAY_IN_GRID, 10)%>,
		rowList:[10,20,60,100],
		height: 'auto',
		sortname: 'createdDate',
		sortorder: "asc",
		pager: "#clientGridPagingDiv",
		viewrecords: true,
		multiselect: false,
		timeout : 120000,
	    loadtext: "Loading...",
		caption: "<spring:message code="netflow.collectionService.summary.client.grid.caption"></spring:message>",
		beforeRequest:function(){
	    },
		loadComplete: function(data) {
			if('${serviceType}'=='RADIUS_COLLECTION_SERVICE'){
				$('#clientGrid .checkboxbg').attr("disabled", "disabled");				
			}
			$('#clientGrid .checkboxbg').bootstrapToggle();
			$('#clientGrid .checkboxbg').change(function(){
				clientActiveInactiveToggleChanged(this);
			});
		},
		loadError : function(xhr,st,err) {
			handleGenericError(xhr,st,err);
		},
		recordtext: "<spring:message code="netflow.collectionService.client.grid.pager.total.records.text"></spring:message>",
	    emptyrecords: "<spring:message code="netflow.collectionService.client.grid.empty.records"></spring:message>",
		loadtext: "<spring:message code="netflow.collectionService.client.grid.loading.text"></spring:message>",
		pgtext : "<spring:message code="netflow.collectionService.client.grid.pager.text"></spring:message>",
		beforeSelectRow: function (rowid, e){
		}
		}).navGrid("#driverGridPagingDiv",{edit:false,add:false,del:false,search:false});
			$(".ui-jqgrid-titlebar").hide();
			$(".ui-pg-input").height("10px");
}

function loadMqttClientGrid(){
	$("#clientGrid").jqGrid({
		url: "<%=ControllerConstants.GET_NETFLOW_COLLECTION_CLIENT_LIST%>",
		postData:
				{'serviceId':'${serviceId}' 
				},
		datatype: "json",
		colNames: [
					  "<spring:message code='netflow.collectionService.client.grid.column.client.id' ></spring:message>",
		           	  "<spring:message code='netflow.collectionService.client.grid.column.topic.nm' ></spring:message>",
		           	 "<spring:message code='netflow.collectionService.client.grid.column.client.nm' ></spring:message>",
		              "<spring:message code='netflow.collectionService.client.grid.column.client.file.loc' ></spring:message>",
		              "<spring:message code='netflow.collectionService.client.grid.column.client.status' ></spring:message>"
			],
		colModel: [
			{name:'clientId',index:'id',sortable:false,hidden:true},
			{name:'topicName',index:'topicName',sortable:false,formatter:clientNameFormatter},
			{name:'clientName',index:'clientName',sortable:false,formatter:clientNameFormatter},
			{name:'fileLoc',index:'outFileLocation',align:'left',sortable:false},
			{name:'status',index:'status',align:'center',formatter:clientStatusFormatter}
		],
		rowNum:<%=MapCache.getConfigValueAsInteger(
					SystemParametersConstant.TOTAL_ROWS_TO_DISPLAY_IN_GRID, 10)%>,
		rowList:[10,20,60,100],
		height: 'auto',
		sortname: 'createdDate',
		sortorder: "asc",
		pager: "#clientGridPagingDiv",
		viewrecords: true,
		multiselect: false,
		timeout : 120000,
	    loadtext: "Loading...",
		caption: "<spring:message code="netflow.collectionService.summary.client.grid.caption"></spring:message>",
		beforeRequest:function(){
	    },
		loadComplete: function(data) {
			if('${serviceType}'=='RADIUS_COLLECTION_SERVICE'){
				$('#clientGrid .checkboxbg').attr("disabled", "disabled");				
			}
			$('#clientGrid .checkboxbg').bootstrapToggle();
			$('#clientGrid .checkboxbg').change(function(){
				clientActiveInactiveToggleChanged(this);
			});
		},
		loadError : function(xhr,st,err) {
			handleGenericError(xhr,st,err);
		},
		recordtext: "<spring:message code="netflow.collectionService.client.grid.pager.total.records.text"></spring:message>",
	    emptyrecords: "<spring:message code="netflow.collectionService.client.grid.empty.records"></spring:message>",
		loadtext: "<spring:message code="netflow.collectionService.client.grid.loading.text"></spring:message>",
		pgtext : "<spring:message code="netflow.collectionService.client.grid.pager.text"></spring:message>",
		beforeSelectRow: function (rowid, e){
		}
		}).navGrid("#driverGridPagingDiv",{edit:false,add:false,del:false,search:false});
			$(".ui-jqgrid-titlebar").hide();
			$(".ui-pg-input").height("10px");
}

function loadCoAPClientGrid(){
	$("#clientGrid").jqGrid({
		url: "<%=ControllerConstants.GET_NETFLOW_COLLECTION_CLIENT_LIST%>",
		postData:
				{'serviceId':'${serviceId}' 
				},
		datatype: "json",
		colNames: [
					  "<spring:message code='netflow.collectionService.client.grid.column.client.id' ></spring:message>",
		           	  "<spring:message code='netflow.collectionService.client.grid.column.client.nm' ></spring:message>",
		           	  "<spring:message code='netflow.collectionService.client.grid.column.resource.nm' ></spring:message>",
		           	  "<spring:message code='netflow.collectionService.client.grid.column.client.ip' ></spring:message>",
		              "<spring:message code='netflow.collectionService.client.grid.column.client.port' ></spring:message>",
		              "<spring:message code='netflow.collectionService.client.grid.column.client.file.loc' ></spring:message>",
		              "<spring:message code='netflow.collectionService.client.grid.column.client.status' ></spring:message>"
			],
		colModel: [
			{name:'clientId',index:'id',sortable:false,hidden:true},
			{name:'clientName',index:'clientName',sortable:false,formatter:clientNameFormatter},
			{name:'resourcesName',index:'resourcesName',sortable:true,align:'center'},
			{name:'clientIp',index:'clientIpAddress',sortable:true,align:'center'},
			{name:'clientPort',index:'clientPort',sortable:true,align:'center'},
			{name:'fileLoc',index:'outFileLocation',align:'left',sortable:false},
			{name:'status',index:'status',align:'center',formatter:clientStatusFormatter}
		],
		rowNum:<%=MapCache.getConfigValueAsInteger(
					SystemParametersConstant.TOTAL_ROWS_TO_DISPLAY_IN_GRID, 10)%>,
		rowList:[10,20,60,100],
		height: 'auto',
		sortname: 'createdDate',
		sortorder: "asc",
		pager: "#clientGridPagingDiv",
		viewrecords: true,
		multiselect: false,
		timeout : 120000,
	    loadtext: "Loading...",
		caption: "<spring:message code="netflow.collectionService.summary.client.grid.caption"></spring:message>",
		beforeRequest:function(){
	    },
		loadComplete: function(data) {
			$('#clientGrid .checkboxbg').bootstrapToggle();
			$('#clientGrid .checkboxbg').change(function(){
				clientActiveInactiveToggleChanged(this);
			});
		},
		loadError : function(xhr,st,err) {
			handleGenericError(xhr,st,err);
		},
		recordtext: "<spring:message code="netflow.collectionService.client.grid.pager.total.records.text"></spring:message>",
	    emptyrecords: "<spring:message code="netflow.collectionService.client.grid.empty.records"></spring:message>",
		loadtext: "<spring:message code="netflow.collectionService.client.grid.loading.text"></spring:message>",
		pgtext : "<spring:message code="netflow.collectionService.client.grid.pager.text"></spring:message>",
		beforeSelectRow: function (rowid, e){
		}
		}).navGrid("#driverGridPagingDiv",{edit:false,add:false,del:false,search:false});
			$(".ui-jqgrid-titlebar").hide();
			$(".ui-pg-input").height("10px");
}


function clientNameFormatter(cellvalue, options, rowObject){
	<sec:authorize access="!hasAuthority('UPDATE_COLLECTION_CLIENT')">
		return '<a class="link" onclick="" style="text-decoration:none;">' + cellvalue + '</a>' ;
	</sec:authorize>
	<sec:authorize access="hasAuthority('UPDATE_COLLECTION_CLIENT')">
		return '<a class="link" onclick="loadClientList();">' + cellvalue + '</a>' ;
	</sec:authorize>
}

function clientStatusFormatter(cellvalue, options, rowObject){
	var id = rowObject['id'];
	if(cellvalue=='ACTIVE'){
		<sec:authorize access="!hasAuthority('UPDATE_COLLECTION_CLIENT')">
			return '<input type="checkbox" disabled="disabled" class="checkboxbg" id="'+id+'_sts_ACTIVE" checked />';
		</sec:authorize>
		<sec:authorize access="hasAuthority('UPDATE_COLLECTION_CLIENT')">
			return '<input type="checkbox" class="checkboxbg" id="'+id+'_sts_ACTIVE" checked />';
		</sec:authorize>
		//return '<input type="checkbox" class="checkboxbg" id="'+id+'_sts_ACTIVE" checked />';
	} else {
		<sec:authorize access="!hasAuthority('UPDATE_COLLECTION_CLIENT')">
			return '<input type="checkbox" disabled="disabled" class="checkboxbg" id="'+id+'_sts_INACTIVE" />';
		</sec:authorize>
		<sec:authorize access="hasAuthority('UPDATE_COLLECTION_CLIENT')">
			return '<input type="checkbox" class="checkboxbg" id="'+id+'_sts_INACTIVE" />';
		</sec:authorize>
		//return '<input type="checkbox" class="checkboxbg" id="'+id+'_sts_INACTIVE" />';
	}
}

function reloadGridData(){
	var $grid = $("#clientGrid");
	$grid.jqGrid('setGridParam',{datatype:'json',page: 1}).trigger('reloadGrid');
}

function viewClientDetails(){
	$('#tab1').click();
}

function resetClientStatusToggle(id,status){
	
	$('#clientGrid .checkboxbg').removeAttr('onchange');
	
	$('#'+id).bootstrapToggle(status)
	
	$('#clientGrid .checkboxbg').change(function(){
		clientActiveInactiveToggleChanged(this);
	});
}

function clientActiveInactiveToggleChanged(element){
	var id = $(element).attr('id');
	var status = id.split('_')[2];
	
	$('#clientId').val(id.split('_')[0]);
	$('#stateMessage').click();
	if(status=='ACTIVE'){
		$('#clientMsg').html("<spring:message code='client.change.stop.status'></spring:message>");
		$('#updtBtn').attr('onclick','updateClientStatus("INACTIVE")');
		$('#noBtn').attr('onclick','resetClientStatusToggle("'+id+'","on");closeFancyBox();');
	} else {
		$('#clientMsg').html("<spring:message code='client.change.on.status'></spring:message>");
		$('#updtBtn').attr('onclick','updateClientStatus("ACTIVE")');
		$('#noBtn').attr('onclick','resetClientStatusToggle("'+id+'","off");closeFancyBox();');
	}
}

function updateClientStatus(status){
	var id = $('#clientId').val();
	resetWarningDisplay();
	clearAllMessages();
	parent.resizeFancyBox();
	$.ajax({
		url: '<%=ControllerConstants.UPDATE_NETFLOW_CLIENT_STATUS%>',
		cache: false,
		async: true,
		dataType: 'json',
		type: "POST",
		data:
		{
			"clientId"						:   id,
			"status"						:	status
		}, 
		
		success: function(data){
			var response = eval(data);
			
			var responseCode = response.code; 
			var responseMsg = response.msg; 
			var responseObject = response.object;
			
			if(responseCode == "200"){
				resetWarningDisplay();
				clearAllMessages();
				showSuccessMsg(responseMsg);
				if(status=='ACTIVE')
					$('#'+id+'_sts_INACTIVE').attr('id',id+'_sts_ACTIVE');
				else
					$('#'+id+'_sts_ACTIVE').attr('id',id+'_sts_INACTIVE');
				
				closeFancyBox();
				
			}else if(responseObject != undefined && responseObject != 'undefined' && responseCode == "400"){
				hideProgressBar(clientListCounter);
				showErrorMsg(responseMsg);
				addErrorIconAndMsgForAjax(responseObject); 
				
				if(status=='ACTIVE')
					$('#'+id + '_sts_INACTIVE').bootstrapToggle('toggle');
				else
					$('#'+id + '_sts_ACTIVE').bootstrapToggle('toggle');
				
				closeFancyBox();
				
			}else{
				hideProgressBar(clientListCounter);
				resetWarningDisplay();
				clearAllMessages();
				showErrorMsg(responseMsg);
			}
		},
	    error: function (xhr,st,err){
	    	hideProgressBar(clientListCounter);
			handleGenericError(xhr,st,err);
		}
	});
}

function resetToggle(id){
	
}


function exportConfigPopup(){
	$("#exportServiceInstanceId").val($("#serviceId").val()); // set service instance id which is selected for export to submit with form.
	$("#isExportForDelete").val(false);
	$("#REQUEST_ACTION_TYPE").val('<%=BaseConstants.COLLECTION_SERVICE_SUMMARY%>');
	$("#export-service-instance-config-form").submit();
}

var ckIntanceSelected = new Array();

$(document).ready(function() {
	getServiceCounterDetails('<%=ControllerConstants.GET_COLLECTION_SERVICE_COUNTER_STATUS%>','${serviceId}','<%=EngineConstants.GTPPRIME_COLLECTION_SERVICE%>','ONLOAD');
});

</script>

