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
<script src="${pageContext.request.contextPath}/customJS/aggregationManager.js<%= "?v=" + Math.random() %>" type="text/javascript"></script>
<script>
function loadConditionGrid(defConditionList){
	var uniqueGridId = "condition_grid";
	var selectAllCheckboxId = "selectAllConditionAttribute";
	var childCheckboxName = "conditionAttributeCheckbox";
					
	$("#"+uniqueGridId).jqGrid({
        datatype: "local",
        colNames:[
                  "id",
                  jsSpringMsg.coreExpressionLabel,
                  jsSpringMsg.conditionAction ,
		],
		colModel:[
		          {name:'id',index:'id',hidden:true,sortable:false},
		          {name:'conditionExpressionValue',index:'conditionExpressionValue',sortable:false},
				  {name:'conditionAction',index:'conditionAction',sortable:false},
        ],			        
        rowNum:jsSpringMsg.totalGridRows,
        rowList:[10,20,60,100],
        height: 'auto',
        mtype:'POST',
		sortname: 'id',
 		sortorder: "desc",
        pager: "#condition_gridPagingDiv",
        contentType: "application/json; charset=utf-8",
        viewrecords: true,
        timeout : 120000,
        loadtext: jsSpringMsg.loadingText,
        caption: "",
        beforeRequest:function(){
            $(".ui-dialog-titlebar").hide();
        }, 
        onSelectRow: function(rowid, status) {
            $('#'+rowid).removeClass('ui-state-highlight');
        },
        data :defConditionList,
		onPaging: function () {
		},
		loadError : function(xhr,st,err) {
			handleGenericError(xhr,st,err);
		},
		onSelectAll:function(){
		},
		recordtext: jsSpringMsg.totalRecordText ,
        emptyrecords: jsSpringMsg.emptyRecordText,
        loadtext: jsSpringMsg.loadingText,
		pgtext : jsSpringMsg.pagerText,
		
		
	}).navGrid("#condition_gridPagingDiv",{edit:false,add:false,del:false,search:false});
	$(".ui-jqgrid-titlebar").hide();
}
</script>
<div class="fullwidth mtop10">	

		<!-- Counter section start here -->
		
		<jsp:include page="../serviceCounterSummary.jsp"></jsp:include>
		<!-- Counter section end here -->			
			
		<!-- Jquery Driver Grid start here -->
		<div class="fullwidth">
			<div class="title2">
       			<spring:message code="aggregationService.summary.condition.list.heading"></spring:message>
      		</div>
      	</div>	
		<div class="box-body table-responsive no-padding box">
				<table class="table table-hover" id="condition_grid"></table>
		        <div id="condition_gridPagingDiv"></div> 
		</div>		
	     <!-- Jquery Driver Grid end here -->	
	     
		<!-- Jquery Agent Grid start here -->		
		<jsp:include page="../serviceAgentList.jsp"></jsp:include>		
		<!-- Jquery Agent Grid end here -->	
		
		<!--  Jqeury All footer pop up start here -->
		<div id="divSynchronize" style=" width:100%; display: none;" >
		    <jsp:include page="../synchronizationPopUp.jsp"></jsp:include>
		</div>
		<jsp:include page="../commonServiceSyncPublish.jsp"></jsp:include>
		<div id="divImportConfig" style=" width:100%; display: none;" >
		    <jsp:include page="../importServiceWiseConfigPopUp.jsp"></jsp:include>
		</div>
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
		
		<div style="display: none;">
		
		<form action="<%=ControllerConstants.EXPORT_SERVICE_INSTANCE_CONFIG%>" id="export-service-instance-config-form" method="POST">
        	<input type="hidden" id="exportServiceInstanceId" name="exportServiceInstanceId"  value=""/>
        	<input type="hidden" id="isExportForDelete" name="isExportForDelete"  value=""/>
        	<input type="hidden" id="<%=BaseConstants.REQUEST_ACTION_TYPE %>" name="<%=BaseConstants.REQUEST_ACTION_TYPE %>" value="<%=BaseConstants.SERVICE_MANAGEMENT%>"/>
        </form>
	</div>
</div>	
<script type="text/javascript">
	$(document).ready(function() {
		defConditionList = JSON.parse('${defConditionList}');
		if(!(defConditionList != 'undefined' && defConditionList != null)){
			defConditionList = [];
		}
		loadConditionGrid(defConditionList);		
		loadJqueryAgentGrid();		
		getServiceCounterDetails('<%=ControllerConstants.GET_AGGREGATION_SERVICE_COUNTER_STATUS%>','${serviceId}','<%=EngineConstants.AGGREGATION_SERVICE%>','ONLOAD');
	});

var ckIntanceSelected = new Array();

function serviceAgentStatusColumnFormatter(cellvalue, options, rowObject){
	var toggleId = rowObject["agentId"] + "_" + cellvalue;
	
	if(cellvalue == 'ACTIVE'){
		return '<input class="checkboxbg" id=' + toggleId + ' data-on="On" data-off="Off" checked type="checkbox" data-size="mini">';
	}else{
		return '<input class="checkboxbg" id=' + toggleId + ' data-on="On" data-off="Off" type="checkbox" data-size="mini">';
	}
}

function exportConfigPopup(){
	$("#exportServiceInstanceId").val($("#serviceId").val()); // set service instance id which is selected for export to submit with form.
	$("#isExportForDelete").val(false);
	$("#REQUEST_ACTION_TYPE").val('<%=BaseConstants.AGGREGATION_SERVICE_SUMMARY%>');
	$("#export-service-instance-config-form").submit();
}

</script>

