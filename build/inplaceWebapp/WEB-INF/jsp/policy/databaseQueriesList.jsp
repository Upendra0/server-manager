<%@page import="com.elitecore.sm.util.MapCache"%>
<%@page import="com.elitecore.sm.common.constants.FormBeanConstants"%>
<%@page import="com.elitecore.sm.common.constants.SystemParametersConstant"%>
<%@page import="org.apache.catalina.connector.Request"%>
<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<%@page import="com.elitecore.sm.common.constants.BaseConstants"%>
<%@taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="/WEB-INF/ShowErrorTag.tld" prefix="elitecoreError"%>
<div class="fullwidth mtop10">
	<div class="fullwidth borbot tab-pane active">
		<div class="fullwidth">
			<div class="title2">
				<spring:message code="policymgmt.databaseQueries.search.title" ></spring:message>
			</div>
		</div>
			<div class=fullwidth>
		        		<jsp:include page="../common/responseMsg.jsp" ></jsp:include>
		    </div>


		<div class="col-md-6 inline-form"
			style="padding-left: 0px !important;">
			<div class="form-group">
				<div class="table-cell-label">
					<spring:message code="policymgmt.databaseQueries.name" ></spring:message>
				</div>
				<div class="input-group">
					<spring:message code="policymgmt.databaseQueries.name" var="tooltip" ></spring:message>
					<input type="text" id="database-queries-name"
						class="form-control table-cell input-sm" data-toggle="tooltip"
						tabindex="1" data-placement="bottom" title="${tooltip}"> <span
						class="input-group-addon add-on last"> <i
						class="glyphicon glyphicon-alert" data-toggle="tooltip"
						data-placement="bottom"></i>
					</span>
				</div>
			</div>
			<div class="form-group">
				<spring:message code="policymgmt.policy.associationstatus"
					var="tooltip" ></spring:message>
				<div class="table-cell-label">${tooltip}</div>
				<div class="input-group ">
					<select id="query-association-status" class="form-control"
						title="${tooltip}" data-toggle="tooltip" tabindex="3"
						data-placement="bottom">
						<option value="ALL" selected="selected">
							<spring:message code="policymgmt.policy.associationstatus.all" ></spring:message>
						</option>
						<option value="Associated">
							<spring:message
								code="policymgmt.policy.associationstatus.associated" ></spring:message>
						</option>
						<option value="Non-Associated">
							<spring:message
								code="policymgmt.policy.associationstatus.nonassociated" ></spring:message>
						</option>
					</select> <span class="input-group-addon add-on last"> <i
						class="glyphicon glyphicon-alert" data-toggle="tooltip"
						data-placement="bottom"></i>
					</span>
				</div>
			</div>
		</div>

		<div class="col-md-6 inline-form">
			<div class="form-group">
				<spring:message code="policymgmt.rule.description" var="tooltip" ></spring:message>
				<div class="table-cell-label">${tooltip}</div>
				<div class="input-group ">
					<input type="text" id="query-desc"
						class="form-control table-cell input-sm" data-toggle="tooltip"
						tabindex="2" data-placement="bottom" title="${tooltip}"> <span
						class="input-group-addon add-on last"> <i
						class="glyphicon glyphicon-alert" data-toggle="tooltip"
						data-placement="bottom"></i>
					</span>
				</div>
			</div>

		</div>

		<div class="clearfix"></div>
		<div class="pbottom15 mtop10">
			<button class="btn btn-grey btn-xs" onclick="reloadQueryGridData('true');"
				tabindex="5" id="search_btn">
				<spring:message code="btn.label.search" ></spring:message>
			</button>
			<button class="btn btn-grey btn-xs"
				onclick="resetSearchRuleCriteria();" tabindex="6" id="reset_btn">
				<spring:message code="btn.label.reset" ></spring:message>
			</button>
		</div>
	</div>
	<div class="fullwidth">
		<div class="title2">
			<spring:message code="policymgmt.databaseQueries.list" ></spring:message>
			<span class="title2rightfield"> <span
				class="title2rightfield-icon1-text"> <sec:authorize
						access="hasAuthority('CREATE_POLICY_CONFIGURATION')">
						<a href="#" onclick="createQuery('ADD'); "><i
							class="fa fa-plus-circle"></i></a>

						<a href="#" id="createQuery" onclick="createQuery('ADD');">
							<spring:message code="policymgmt.databaseQueries.create.query" ></spring:message>
						</a>

					</sec:authorize>
			</span>
			</span>
		</div>

	<a href="#divDeleteQuery" class="fancybox" style="display: none;" id="delete_query">#</a>
	<a href="#divAdditionalInfoAttribute" class="fancybox" style="display: none;" id="view_attribute">#</a>
	</div>
	<div class="box-body table-responsive no-padding box">
		<table class="table table-hover" id="queryGrid"></table>
		<div id="queryGridPagingDiv"></div>
	</div>

	<!-- Jquery Database Query Grid end here -->


</div>

<div style="display: none;">
	<form id="update_rule_form" method="POST">
		<input type="hidden" id="server-instance-name" name="server-instance-name" value='${instanceName}' /> 
		<input type="hidden" id="server-instance-host" name="server-instance-host" value='${host}' /> 
		<input type="hidden" id="server-instance-port" name="server-instance-port" value='${port}' /> 
		<input type="hidden" id="server-instance-id" name="serverInstanceId" value='${instanceId}' />
		
		<input type="hidden" id="serviceInstanceId" name="serviceInstanceId" value="${serviceInstanceId}">
			<input type="hidden" id="serviceId" name="serviceId" value="${serviceId}">
			<input type="hidden" id="serviceType" name="serviceType" value="<%=BaseConstants.PROCESSING_SERVICE%>">
			<input type="hidden" id="serviceName" name="serviceName" value="${serviceName}">
	</form>
</div>
		<a href="#divAddQuery" class="fancybox" style="display: none;" id="addQueryPopUp">#</a>
		
	 	<div id="divAddQuery" style=" width:100%; display: none;" >
	 		<jsp:include page="addDatabaseQueries.jsp"></jsp:include>
		</div>

<a href="#divViewQueryConditions" class="fancybox" style="display: none;" id="viewQueryConditions">#</a>
 	<div id="divViewQueryConditions" style=" width:100%; display: none;" >
 		<jsp:include page="viewQueryConditionPopUp.jsp"></jsp:include>
</div>

<a href="#divViewQueryActions" class="fancybox" style="display: none;" id="viewQueryActions">#</a>
 	<div id="divViewQueryActions" style=" width:100%; display: none;" >
 		<jsp:include page="viewQueryActionPopUp.jsp"></jsp:include>
</div>

<div id="divAdditionalInfoAttribute" style="display: none;">
	<jsp:include page="viewQueryParamPopUp.jsp"></jsp:include>
	
</div>
<div id="divDeleteQuery" style="display: none;">
	<jsp:include page="deleteDatabaseQuery.jsp"></jsp:include>
	
</div>

<script>

var actionJsonString;
var conditionJsonString;

$(document).ready(function() {
	var isSearch = false;
	loadJQueryRuleGrid(isSearch);
	$('.fancybox').fancybox({
		maxWidth	: 700,
		maxHeight	: 300,
		fitToView	: false,
		width		: '90%',
		autoSize	: false,
		closeClick	: false,
		openEffect	: 'none',
		closeEffect	: 'none'
	});
});

function createQuery(){
	clearAllMessages();
	$("#actionList").empty();
	$("#queryName").prop('disabled',false);
	$("#queryName").val('');
	$("#queryValue").val('');
	$("#returnMultipleRowsEnable option[value=true]").attr("selected", false);
	$("#conditionList").empty();
	var actionTableString = '<tr>';
		actionTableString +='<td>P/C Key&nbsp&nbsp</td>';
		actionTableString +='<td>Database Field Name</td>';
		actionTableString +='<td>Operator</td>';
		actionTableString +='<td>Unified Field Name</td>';
		actionTableString +='<td>Delete</td>';
		actionTableString +='</tr>"';
	$("#actionList").append(actionTableString);
	var conditionTableString = '<tr>';
	conditionTableString += '<td>Database Field Name</td>';
	conditionTableString += '<td>Unified Field Name</td>';
	conditionTableString += '<td><center>Delete</center></td>';
	conditionTableString += '</tr>';
	$("#conditionList").append(conditionTableString);
	$("#add_label").show();
	$("#addNewQuery").show();
	$("#update_label").hide();
	$("#updateQuery_btn").hide();
	$("#id").val("0");
	changeConditionList();
	manageConditionActionOutputDB();
	$("#addQueryPopUp").click();
}

function loadJQueryRuleGrid(isSearch){
	$("#queryGrid").jqGrid({
		url: "<%= ControllerConstants.INIT_DATABASE_QUERY_LIST%>",
		postData : {
			queryName: function () {
   	        	return $("#database-queries-name").val();
    		},
    		description: function () {
   	        	return $("#query-desc").val();
    		},
    		associationStatus: function(){
    			return $("#query-association-status").val();
    		},
    		serverInstanceId: function () {
    			return '${instanceId}';
    		},
    		'isSearch':isSearch
		},
		datatype: "json",
		colNames: [
					  "#",
					  "<spring:message code='policymgmt.databaseQueries.database.query.name' ></spring:message>",
		           	  "<spring:message code='policymgmt.databaseQueries.description' ></spring:message>",
		              "<spring:message code='policymgmt.databaseQueries.conditionList' ></spring:message>",
		              "<spring:message code='policymgmt.databaseQueries.actionList' ></spring:message>",
		              "<spring:message code='policymgmt.rule.grid.column.additionalparam' ></spring:message>",
		              "<spring:message code='business.policy.rule.group.grid.column.delete' ></spring:message>",
		              "<spring:message code='policymgmt.databaseQueries.database.query.value' ></spring:message>",
		              "<spring:message code='policymgmt.databaseQueries.outputDBField' ></spring:message>",
		              "<spring:message code='policymgmt.databaseQueries.returnMultipleRows' ></spring:message>",
		              "<spring:message code='policymgmt.databaseQueries.cache' ></spring:message>",
		              "<spring:message code='policymgmt.databaseQueries.conditionExpression.enable' ></spring:message>",
		              "<spring:message code='policymgmt.databaseQueries.conditionExpression' ></spring:message>",
		              "<spring:message code='policymgmt.databaseQueries.logical' ></spring:message>"
			],
		colModel: [
			{name:'id',index:'id',hidden:true},
			{name:'queryName',index:'queryName',formatter:updateParamFormatter},
			{name:'description',index:'description'},
			{name:'databaseQueryConditions',index:'conditionList',align:'center',formatter:conditionListFormatter},
			{name:'databaseQueryActions',index:'actionList',align:'center',formatter:actionListFormatter},
			{name:'additionalParam',index:'additionalParam',align:'center',formatter:additionalParamFormatter},
			{name:'delete',index:'delete',align:'center', formatter:queryDeleteFormatter},
			{name:'queryValue',index:'queryValue',hidden:true},
			{name:'outputDbField',index:'outputDbField',hidden:true},
			{name:'returnMultipleRowsEnable',index:'returnMultipleRowsEnable',hidden:true},
			{name:'cacheEnable',index:'cacheEnable',hidden:true},
			{name:'conditionExpressionEnable',index:'conditionExpressionEnable',hidden:true},
			{name:'conditionExpression',index:'conditionExpression',hidden:true},
			{name:'logicalOperator',index:'logicalOperator',hidden:true}
		],
		rowNum:<%= MapCache.getConfigValueAsInteger(SystemParametersConstant.TOTAL_ROWS_TO_DISPLAY_IN_GRID, 10) %>,
		rowList:[5,10,20,50,100],
		height: 'auto',
		sortname: 'id',
		sortorder: "desc",
		pager: "#queryGridPagingDiv",
		viewrecords: true,
		multiselect: false,
		timeout : 120000,
	    loadtext: "Loading...",
		caption: "<spring:message code="policymgmt.databaseQueries.list"></spring:message>",
		beforeRequest:function(){
	 		$('#divLoading').dialog({
	             autoOpen: false,
	             width: 90,
	             modal:true,
	             overlay: { opacity: 0.3, background: "white" },
	             resizable: false,
	             height: 125
	         });
	    },
		loadComplete: function(data) {
	 		//$("#divLoading").dialog('close');
	 			$(".ui-dialog-titlebar").show();
	 			if ($('#grid').getGridParam('records') === 0) {
	             $('#grid tbody').html("<div style='padding:6px;background:#D8D8D8;text-align:center;'><spring:message code="collectionService.summary.agent.grid.empty.records"></spring:message></div>");
	             $("#agentGridPagingDiv").hide();
	         }
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
		}).navGrid("#queryGridPagingDiv",{edit:false,add:false,del:false,search:false});
			$(".ui-jqgrid-titlebar").hide();
			$(".ui-pg-input").height("10px");
}

function reloadRuleGridData(){
	var isSearch = true;
	loadJQueryRuleGrid(isSearch);
}



function conditionListFormatter(cellvalue, options, rowObject) {
	return '<a href="#" onclick="queryConditionsPopUp('+"'" + rowObject["id"]+ "'"+');"><i class="fa fa-fw fa-vimeo-square"></i></a>';
}

function additionalParamFormatter(cellvalue, options, rowObject) {
	return "<a href='#' class='link' onclick=displayAdditionalInfo('"+rowObject["id"]+"'); ><i class='fa fa-info-circle' aria-hidden='true'></i></a>";		
}

function actionListFormatter(cellvalue, options, rowObject) {
	return '<a href="#" onclick="queryActionsPopUp('+"'" + rowObject["id"]+ "'"+');"><i class="fa fa-fw fa-vimeo-square"></i></a>';
}

function queryConditionsPopUp(id) {
	clearAllMessages();
	loadJQueryConditionGrid(id);
	$('#viewQueryConditions').click();
}

function queryActionsPopUp(id) {
	clearAllMessages();
	loadJQueryActionGrid(id);
	$('#viewQueryActions').click();
}

function updateParamFormatter(cellvalue, options, rowObject){
	return '<a class="link" id="'+rowObject["queryName"]+'_update_lnk" onclick="updateQuery('+"'" + rowObject["id"]+ "'" +','+"'" + cellvalue+ "'" + ');">' + cellvalue + '</a>' ;
}

function updateQuery(queryId,cellvalue){
	var responseObject = jQuery("#queryGrid").jqGrid('getRowData', queryId);
	clearAllMessages();
	$("#actionList").empty();
	$("#conditionList").empty();
	var actionTableString = '<tr>';
		actionTableString +='<td>P/C Key&nbsp&nbsp</td>';
		actionTableString +='<td>Database Field Name</td>';
		actionTableString +='<td>Operator</td>';
		actionTableString +='<td>Unified Field Name</td>';
		actionTableString +='<td>Delete</td>';
		actionTableString +='</tr>"';
	$("#actionList").append(actionTableString);
	var conditionTableString = '<tr>';
	conditionTableString += '<td>Database Field Name</td>';
	conditionTableString += '<td>Unified Field Name</td>';
	conditionTableString += '<td><center>Delete</center></td>';
	conditionTableString += '</tr>';
	$("#conditionList").append(conditionTableString);
	$("#add_label").hide();
	$("#addNewQuery").hide();
	$("#update_label").show();
	$("#updateQuery_btn").show();
	//To reset selected options
	$("#cacheEnable option[value=true]").attr("selected", false);
	$("#cacheEnable option[value=false]").attr("selected", false);
	$("#returnMultipleRowsEnable option[value=true]").attr("selected", false);
	$("#returnMultipleRowsEnable option[value=false]").attr("selected", false);
	$("#databaseQueryLogicalOperator option[value=and]").attr("selected", false);
	$("#databaseQueryLogicalOperator option[value=or]").attr("selected", false);
	$("#databaseQueryLogicalOperator option[value=NA]").attr("selected", false);
	$("#conditionExpressionEnable option[value=true]").attr("selected", false);
	$("#conditionExpressionEnable option[value=false]").attr("selected", false);
	//
	$("#id").val(queryId);
	$("#queryName").prop('disabled',false);
	$("#description").val(responseObject.description);
	$("#queryValue").val(responseObject.queryValue);
	$("#queryName").val(cellvalue);
	$("#outputDbField").val(responseObject.outputDbField);
	$("#conditionExpression").val(responseObject.conditionExpression);
	$("#cacheEnable option[value=" +responseObject.cacheEnable+ "]").prop("selected", true);
	$("#returnMultipleRowsEnable option[value=" +responseObject.returnMultipleRowsEnable+ "]").prop("selected", true);
	if(responseObject.logicalOperator === ""){
		responseObject.logicalOperator = 'NA';
	}
	$("#databaseQueryLogicalOperator option[value=" +responseObject.logicalOperator+ "]").prop("selected", true);
	$("#conditionExpressionEnable option[value=" +responseObject.conditionExpressionEnable+ "]").prop("selected", true);
	loadConditionTable(queryId);
	loadActionTable(queryId);
	changeConditionList();
	manageConditionActionOutputDB();
	$("#addQueryPopUp").click();
}

function displayAdditionalInfo(queryId){
	var responseObject = jQuery("#queryGrid").jqGrid('getRowData', queryId);
	//To reset selected options
	$("#cacheEnablePopup option[value=true]").attr("selected", false);
	$("#cacheEnablePopup option[value=false]").attr("selected", false);
	$("#returnMultipleRowsEnablePopup option[value=true]").attr("selected", false);
	$("#returnMultipleRowsEnablePopup option[value=false]").attr("selected", false);
	$("#logicalOperatorPopup option[value=and]").attr("selected", false);
	$("#logicalOperatorPopup option[value=or]").attr("selected", false);
	$("#conditionExpressionEnablePopup option[value=true]").attr("selected", false);
	$("#conditionExpressionEnablePopup option[value=false]").attr("selected", false);
	//
	
	$("#queryValuePopup").val(responseObject.queryValue);
	$("#outputDBFieldPopup").val(responseObject.outputDbField);
	$("#conditionExpressionPopup").val(responseObject.conditionExpression);
	$("#cacheEnablePopup option[value=" +responseObject.cacheEnable+ "]").attr("selected", true);
	$("#returnMultipleRowsEnablePopup option[value=" +responseObject.returnMultipleRowsEnable+ "]").attr("selected", true);
	if(responseObject.logicalOperator && responseObject.logicalOperator != "") {
		$("#logicalOperatorPopup option[value=" +responseObject.logicalOperator+ "]").attr("selected", true);	
	}
	$("#conditionExpressionEnablePopup option[value=" +responseObject.conditionExpressionEnable+ "]").attr("selected", true);
	$("#view_attribute").click();
}

function queryDeleteFormatter(cellvalue, options, rowObject){
 	if(cellvalue == 'Associated') {
		return '<p>' + cellvalue + '</p>';
	} else { 
		return '<a href="#" id="'+rowObject["queryName"]+'_delete_lnk" onclick="deleteQueryPopup('+"'" + rowObject["id"]+ "'"+');"><i class="fa fa-trash"></i></a>';			
	}
}

function deleteQueryPopup(id){
	clearAllMessages();
	$("#delete_warning").show();
	$('#delete_label').show();
	$('#divDeleteQueryPopup').show();
	$("#deleteQueryId").val(id);
	$('#delete_query').click();
	var tableString ="<table class='table table-hover table-bordered'  border='1' >";
		tableString+="<th><spring:message code='policymgmt.databaseQueries.database.query.name' ></spring:message></th>";
		tableString+="<th><spring:message code='policymgmt.databaseQueries.description' ></spring:message></th>";
		rowData = $('#queryGrid').jqGrid('getRowData',id);
		tableString += "<tr>";
		tableString += "<td>"+rowData.queryName+"</td>";
		tableString += "<td>"+rowData.description+"</td>";
		tableString += "</tr>";
		tableString+="</table>";
		$("#delete_selected_query_details").html(tableString);
		$("#delete_attribute_bts_div").show();
		$("#delete_attribute_progress_bar_div").hide();
		$("#delete_close_attribute_buttons_div").hide(); 
	
}

function deleteQuery(){
	$("#delete_attribute_bts_div").hide();
	$("#delete_attribute_progress_bar_div").show();
	$("#delete_close_attribute_buttons_div").hide();
	$.ajax({
		url: 'deleteQuery',
		cache: false,
		async: true,
		dataType:'json',
		type:'POST',
		data:
		 {
			"deleteQueryId" : $("#deleteQueryId").val()
		 }, 
		success: function(data){
			var response = eval(data);
			var responseCode = response.code; 
			var responseMsg = response.msg; 
		
			clearAllMessagesPopUp();
			resetWarningDisplayPopUp();
			 
			if(responseCode === "200"){
				$("#delete_attribute_bts_div").hide();
				$("#delete_attribute_progress_bar_div").hide();
				$("#delete_warning").hide();
				reloadQueryGridData(false);
				showSuccessMsg(responseMsg);
				closeFancyBox();
				ckIntanceSelected = new Array();
			}else{
				showErrorMsgPopUp(responseMsg);
				$("#delete_attribute_bts_div").show();
				$("#delete_attribute_progress_bar_div").hide();
				$("#delete_close_attribute_buttons_div").hide();
			}
		},
	    error: function (xhr,st,err){
			handleGenericError(xhr,st,err);
		}
	});
}


function reloadQueryGridData(isSearch){
	clearAllMessages();
	var $grid = $("#queryGrid");
	jQuery('#queryGrid').jqGrid('clearGridData');
	clearQueryGrid();

	$grid.jqGrid('setGridParam',{datatype:'json',page: 1,sortname: 'id',sortorder: "asc", postData:{
	  
   		'serverInstanceId': function () {
	        return '${instanceId}';
   		},
   		'isSearch':isSearch
	}}).trigger('reloadGrid');
}


function clearQueryGrid(){
	$grid = $("#queryGrid");
	var rowIds = $grid.jqGrid('getDataIDs');
	// iterate through the rows and delete each of them
	for(var i=0,len=rowIds.length;i<len;i++){
		var currRow = rowIds[i];
		$grid.jqGrid('delRowData', currRow);
	}
}

function createUpdateDatabaseQuery(urlAction,actIndex,conIndex){
	createUpdateActions(actIndex);
	createUpdateConditions(conIndex);
	var id = $("#id").val();
	var alias = $("#queryName").val();
	var isCacheEnabled = $("#cacheEnable").val();
	var returnMultipleRowsEnable = $("#returnMultipleRowsEnable").val();
	var conditionExpression = $("#conditionExpression").val();
	var isConditionExpressionEnabled = $("#conditionExpressionEnable").val();
	var description = $("#description").val();
	var logicalOperator = $("#databaseQueryLogicalOperator").val();
	if(logicalOperator === 'NA'){
		logicalOperator = "";
	}
	var outputDBField = $("#outputDbField").val();
	var queryName = $("#queryName").val();
	var queryValue = $("#queryValue").val();
	var serverInstanceId = ${instanceId};
	$.ajax({
		url: urlAction,
		cache: false,
		async: true,
		dataType:'json',
		type:"POST",
		data:  {
			"id":id,
			"alias":alias,
			"cacheEnable":isCacheEnabled,
			"conditionExpression":conditionExpression,
			"conditionExpressionEnable":isConditionExpressionEnabled,
			"description":description,
			"logicalOperator":logicalOperator,
			"outputDbField":outputDBField,
			"queryName":queryName,
			"queryValue":queryValue,
			"returnMultipleRowsEnable":returnMultipleRowsEnable,
			"serverInstanceId":serverInstanceId,
			"actionList":actionJsonString,
			"conditionList":conditionJsonString
		}, 
		success: function(data){
			var response = eval(data);
			var responseCode = response.code; 
			var responseMsg = response.msg; 
			var responseObject = response.object;
			clearAllMessagesPopUp();
			resetWarningDisplayPopUp();
			if(responseCode === "200"){
				closeFancyBox();
				closeFancyBoxFromChildIFrame();
				reloadQueryGridData(false);
				showSuccessMsg(responseMsg);
				resetDatabaseQueries();
			}else if(responseObject !== undefined && responseObject !== 'undefined' && responseCode === "400"){
				addErrorIconAndMsgForAjaxPopUp(responseObject);
			}else{
				showErrorMsgPopUp(responseMsg);
			}
		},
	    error: function (xhr,st,err){
			handleGenericError(xhr,st,err);
		}
	});
}

function resetDatabaseQueries() {
	$("#id").val(0);
	$("#queryName").val('');
	$("#cacheEnable").val('false');
	$("#returnMultipleRowsEnable").val('false');
	$("#conditionExpression").val('');
	$("#conditionExpressionEnable").val('false');
	$("#description").val('');
	$("#databaseQueryLogicalOperator").val('NA');
	$("#outputDbField").val('');
	$("#queryName").val('');
	$("#queryValue").val('');
	changeConditionList();
	manageConditionActionOutputDB();
}

function createUpdateConditions(conIndex){
	var data = [];
	var toPass = [];
	var j=0;
	for(var i = 0;i<=conIndex ; i++){
		var elementName = document.getElementById("condatabaseFieldName"+i);
		var elementunifiedField = document.getElementById("conUnifiedField"+i);
		if(elementName!=null){
			var databaseFieldName = elementName.value;
			var unifiedField = elementunifiedField.value;
			data[j] = {"databaseFieldName":databaseFieldName,"unifiedField":unifiedField};
			toPass.push(data[j]);
			j++;
		}
	}
	actionJsonString = JSON.stringify(toPass);
}

function createUpdateActions(actIndex){
		
		var data = [];
		var toPass = [];
		var j=0;
		for(var i = 0;i<=actIndex ; i++){
			var databaseKey = false;
			if ($("#databaseKey"+i).prop('checked')){
				databaseKey = true;
			}
			var elementName = document.getElementById("databaseFieldName"+i);
			var elementOperator = document.getElementById("policyOperator"+i);
			var elementunifiedField = document.getElementById("unifiedField"+i);
			if(elementName!=null){
				var databaseFieldName = elementName.value;
				var policyOperator = elementOperator.value;
				var unifiedField = elementunifiedField.value;
				data[j] = {"databaseKey":databaseKey,"databaseFieldName":databaseFieldName,"policyOperator":policyOperator,"unifiedField":unifiedField};
				toPass.push(data[j]);
				j++;
			}
		}
		conditionJsonString = JSON.stringify(toPass);
}
</script>
