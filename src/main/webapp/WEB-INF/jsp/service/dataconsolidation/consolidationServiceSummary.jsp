<%@page import="com.elitecore.sm.common.constants.BaseConstants"%>
<%@page import="com.elitecore.sm.util.MapCache"%>
<%@page import="com.elitecore.sm.common.constants.SystemParametersConstant"%>
<%@page import="org.apache.catalina.connector.Request"%>
<%@page import="com.elitecore.sm.common.constants.EngineConstants"%>
<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="/WEB-INF/ShowErrorTag.tld" prefix="elitecoreError"%>


<div class="fullwidth mtop10">	

		<!-- Counter section start here -->
		
		<jsp:include page="../serviceCounterSummary.jsp"></jsp:include>
		<!-- Counter section end here -->			
			
		<!-- Jquery Driver Grid start here -->
		<div class="fullwidth">
			<div class="title2">
       			<spring:message code="consolidation.service.consoli.defi.list.label"></spring:message>
      		</div>
      	</div>	
		
			<div class="box-body table-responsive no-padding box">
			<table class="table table-hover" id="drivergrid"></table>
	           	<div id="driverGridPagingDiv"></div> 
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
		
		<div id="divChangeDriverStatus" style=" width:100%; display: none;" >
		    <div class="modal-content">
		        <div class="modal-header padding10">
		            <h4 class="modal-title"><spring:message code="collection.driver.delete.header.label"></spring:message></h4>
		        </div>
		        <div class="modal-body padding10 inline-form">
		        	<span id="deleteDriverResponseMsg" style="display:none;">
		        		<jsp:include page="../../common/responseMsgPopUp.jsp" ></jsp:include>
		        	</span>
			        <p id="active-driver-warning" style="display:none;">
			       		 <spring:message code="driver.change.status.active.message" ></spring:message>
			        </p>
			        <p id="inactive-driver-warning" style="display:none;">
			       		 <spring:message code="driver.change.status.inactive.message" ></spring:message>
			        </p>
		        </div>
		        <sec:authorize access="hasAuthority('UPDATE_COLLECTION_DRIVER')">
			        <div id="inactive-driver-div" class="modal-footer padding10">
			            <button type="button" class="btn btn-grey btn-xs " onclick="changeDriverStatus();"><spring:message code="btn.label.yes"></spring:message></button>
			            <button type="button" class="btn btn-grey btn-xs " data-dismiss="modal" onclick="setDefaultStatus();"><spring:message code="btn.label.no"></spring:message></button>
			        </div>
		        </sec:authorize>
		    </div>
		    <!-- /.modal-content --> 
		</div>
		
		
		<a href="#divSynchronize" class="fancybox" style="display: none;" id="synchronize">#</a>
       	<a href="#divImportConfig" class="fancybox" style="display: none;" id="importconfig">#</a>
       	<a href="#divStopService" class="fancybox" style="display: none;" id="stop_service">#</a>
       	<a href="#divStartService" class="fancybox" style="display: none;" id="start_service">#</a>
       	<a href="#divChangeDriverStatus" class="fancybox" style="display: none;" id="updateDriverStatus">#</a>
		<!--  Jquery All footer pop up end here  -->
		
		
		<form action="<%=ControllerConstants.EXPORT_SERVICE_INSTANCE_CONFIG%>" id="export-service-instance-config-form" method="POST">
        	<input type="hidden" id="exportServiceInstanceId" name="exportServiceInstanceId"  value=""/>
        	<input type="hidden" id="isExportForDelete" name="isExportForDelete"  value=""/>
        	<input type="hidden" id="<%=BaseConstants.REQUEST_ACTION_TYPE %>" name="<%=BaseConstants.REQUEST_ACTION_TYPE %>" value="<%=BaseConstants.SERVICE_MANAGEMENT%>"/>
       </form>
       <form action="<%= ControllerConstants.INIT_DRIVER_CONFIGURATION %>" method="POST" id="driver-config-form">
    		<input type="hidden" id="driverId" name="driverId"/>
    		<input type="hidden" id="driverTypeAlias" name="driverTypeAlias"/>
    		<input type="hidden" id="serviceId" name="serviceId" value="${serviceId}"/>
    		<input type="hidden" id="serviceInstanceId" name="serviceInstanceId" value="${serviceInstanceId}">
    		<input type="hidden" id="serviceName" name="serviceName" value="${serviceName}"/>
    		<input type="hidden" id="serverInstanceId" name="serverInstanceId" value="${instanceId}"/>
    	</form>
</div>	
<script>
	$(document).ready(function() {
		var consList = eval('${conslist}');
		
		if(consList != 'undefined' && consList != null  ){
			loadJQueryDriverGrid(consList);
		}else{
			consList = [];
			loadJQueryDriverGrid(consList);	
		}		
		loadJqueryAgentGrid();		
		getServiceCounterDetails('<%=ControllerConstants.GET_COLLECTION_SERVICE_COUNTER_STATUS%>','${serviceId}','<%=EngineConstants.COLLECTION_SERVICE%>','ONLOAD'); 
	});
	
function loadJQueryDriverGrid(consList){
	    
		$("#drivergrid").jqGrid({
		url: "",
		postData: {'serviceId':'${serviceId}'},
		datatype: "local",
		colNames: [
					  "<spring:message code='consolidation.service.serv.mapp.grid.column.ser.id' ></spring:message>",
		           	  "Consolidation Defination Name",
		           	  "Segregation Type",
		           	  "Date Field Name"
		           	
				  ],
		colModel: [
					  {name:'id',index:'id',sortable:false,hidden:true},
					  {name:'consName',index:'consName',sortable:false,hidden:false},
					  {name:'segregationType',index:'segregationType',sortable:false,hidden:false},
					  {name:'dateFieldName',index:'dateFieldName',sortable:false,hidden:false}
				  ],
		data: consList,			  
		rowNum:<%=MapCache.getConfigValueAsInteger(
				SystemParametersConstant.TOTAL_ROWS_TO_DISPLAY_IN_GRID, 10)%>,
		rowList:[5,10,15,20],
		height: 'auto',
		pager: "#driverGridPagingDiv",
		viewrecords: true,
		multiselect: false,
		timeout : 120000,
	    loadtext: "Loading...",
		caption: "<spring:message code="collectionService.summary.agent.grid.caption"></spring:message>",
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
		recordtext: "<spring:message code="serverManagement.grid.pager.total.records.text"></spring:message>",
	    emptyrecords: "<spring:message code="consolidation.service.serv.mapp.grid.empty.records"></spring:message>",
		loadtext: "<spring:message code="consolidation.service.serv.mapp.grid.loading.text"></spring:message>",
		pgtext : "<spring:message code="consolidation.service.serv.mapp.grid.pager.text"></spring:message>",
		beforeSelectRow: function (rowid, e){
		}
		}).navGrid("#driverGridPagingDiv",{edit:false,add:false,del:false,search:false});
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


function driverNameFormatter(cellvalue, options, rowObject){
	var textMessage = "This action will refresh all below data";
	return '<a class="link" onclick="viewDriverDetails(\'' + rowObject["driverId"] + '\',\'' + rowObject["driveType"] + '\')">' + cellvalue + '</a>' ;
}



/* var driverStatus,driverType,driverId;
function driverEnableColumnFormatter(cellvalue, options, rowObject){
	var toggleId = rowObject["driverId"] + "_" + cellvalue;
	
	if(cellvalue == 'ACTIVE'){
		return '<input class="checkboxbg" id=' + toggleId + ' data-on="On" data-off="Off" checked type="checkbox" data-size="mini" onchange = "driverStatusPopup('+"'" + rowObject["driverId"]+ "','"+rowObject["driveType"]+"','"+rowObject["enable"]+"'" + ');">';
	}else{
		return '<input class="checkboxbg" id=' + toggleId + ' data-on="On" data-off="Off" type="checkbox" data-size="mini"  onchange="driverStatusPopup('+"'" + rowObject["driverId"]+ "','"+rowObject["driveType"]+"','"+rowObject["enable"]+"'" + ');"   >';
	}
} */

<%-- function driverStatusPopup(id, type, status){
	driverId = id;
	driverType = type
	driverStatus = status;
	
	if(driverStatus == 'ACTIVE'){
		$("#active-driver-warning").show();
	}else{
		$("#inactive-driver-warning").show();
	}
	$("#updateDriverStatus").click();
}

function setDefaultStatus(){
	
	if(driverStatus == 'ACTIVE'){
		$('#'+driverId +'_'+driverStatus).bootstrapToggle('on');
	}else{
		$('#'+driverId +'_'+driverStatus).bootstrapToggle('off');
	}
	closeFancyBox();
}

 function changeDriverStatus(){
	resetWarningDisplay();
	clearAllMessages();
	var tempStatus ;
	
	if(driverStatus == 'ACTIVE'){
		tempStatus = 'INACTIVE';
	}else{
		tempStatus = 'ACTIVE';
	} 
	
	 $.ajax({
		url: '<%=ControllerConstants.UPDATE_COLLECTION_DRIVER_STATUS%>',
		cache: false,
		async: true,
		dataType: 'json',
		type: "POST",
		data:
		 {
			"driverId"     : driverId,
			"driverType"   : driverType,
			"driverStatus" : tempStatus
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
					$('#'+driverId +'_'+driverStatus).bootstrapToggle('on');
					$('#'+driverId +'_'+driverStatus).attr("id",'#'+driverId +'_'+tempStatus);
				}else{
					$('#'+driverId +'_'+driverStatus).bootstrapToggle('off');
					$('#'+driverId +'_'+driverStatus).attr("id",'#'+driverId +'_'+tempStatus);
				}
				
				showSuccessMsg(responseMsg);
				closeFancyBox();
				reloadGridData();
			}else if(responseObject != undefined && responseObject != 'undefined' && responseCode == "400"){
				showErrorMsg(responseMsg);
				$('#'+driverId +'_'+driverStatus).bootstrapToggle('toggle');
				closeFancyBox();
				reloadGridData();
			}else{
				resetWarningDisplay();
				clearAllMessages();
				showErrorMsg(responseMsg);
				$('#'+driverId +'_'+driverStatus).bootstrapToggle('toggle');
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
} --%>
function reloadGridData(){
	var $grid = $("#drivergrid");
	$grid.jqGrid('setGridParam',{datatype:'json',page: 1}).trigger('reloadGrid');
}

/* var i=0;
function srNoFormatter(cellvalue, options, rowObject){
	i=i+1;
	return i;
} */
	
	
function exportConfigPopup(){
	$("#exportServiceInstanceId").val($("#serviceId").val()); // set service instance id which is selected for export to submit with form.
	$("#isExportForDelete").val(false);
	$("#REQUEST_ACTION_TYPE").val('<%=BaseConstants.CONSOLIDATION_SERVICE_SUMMARY%>');
	$("#export-service-instance-config-form").submit();
}

<%-- function viewDriverDetails(driverId,driverType){
	
	$('#driverTypeAlias').val(driverType);
	$('#driverId').val(driverId);
	$('#driver-config-form').submit();
	//loadCollectionDetails('<%=ControllerConstants.INIT_COLLECTION_DRIVER_MANAGER%>','POST','COLLECTION_DRIVER_CONFIGURATION');
}
 --%>

var ckIntanceSelected = new Array();


</script>
