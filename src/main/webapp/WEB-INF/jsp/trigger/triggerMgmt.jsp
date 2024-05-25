<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<%@page import="com.elitecore.sm.common.constants.SystemParametersConstant"%>
<%@page import="com.elitecore.sm.util.MapCache"%>    
<%@ taglib uri="http://www.springframework.org/security/tags" 	prefix="sec"  %>
<%@taglib 	uri="http://www.springframework.org/tags" 			prefix="spring"%>
<%@taglib uri="/WEB-INF/ShowErrorTag.tld" prefix="elitecoreError" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<div class="tab-content no-padding clearfix" id="trigger-configuration-block">
<div class="fullwidth mtop10">
		<div class="fullwidth">
			<div class="title2">
				<spring:message code="trigger.mgmt.search.title" ></spring:message>
			</div>
		</div>
		<div class="col-md-6 inline-form" style="padding-left: 0px !important;">
			<div class="form-group">
				<div class="table-cell-label">
					<spring:message code="trigger.mgmt.jqgrid.trigger.name" ></spring:message>
				</div>
				<div class="input-group">
					<spring:message code="trigger.mgmt.jqgrid.trigger.name" var="tooltip" ></spring:message>
					<input type="text" id="searchTriggerName"
						class="form-control table-cell input-sm" data-toggle="tooltip"
						tabindex="1" data-placement="bottom" title="${tooltip}"> <span
						class="input-group-addon add-on last"> <i
						class="glyphicon glyphicon-alert" data-toggle="tooltip"
						data-placement="bottom"></i>
					</span>
				</div>
			</div>
		</div>
		<div class="col-md-6 inline-form" style="padding-left: 0px !important;">
			<div class="form-group">
				<div class="table-cell-label">
					<spring:message code="trigger.mgmt.jqgrid.Type" ></spring:message>
				</div>
            	<div class="input-group ">
            		<select  name ="triggerTypeEnum" class="form-control table-cell input-sm" id="triggerTypeEnum" >
	             		<c:forEach items="${triggerTypeEnum}" var="triggerTypeEnum">
	             			<option value="${triggerTypeEnum}">${triggerTypeEnum}</option>
	             		</c:forEach>
            		</select>
            		<span class="input-group-addon add-on last" > <i class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="left" title=""></i></span>
            	</div>
            </div>	
        </div>


	
	<div class="clearfix"></div>
		<div class="pbottom15 mtop10">
			<button id='search' class="btn btn-grey btn-xs" onclick="reloadTableGridData('true');"
				tabindex="5">
				<spring:message code="btn.label.search" ></spring:message>
			</button>
			<button class="btn btn-grey btn-xs"
				onclick="resetSearchTriggerCriteria();reloadTableGridData('false');" tabindex="6">
				<spring:message code="btn.label.reset" ></spring:message>
			</button>
		</div>
		
		<div class="fullwidth">
		<div class="title2">
			<spring:message code="trigger.config.tab.header" ></spring:message>
			<span class="title2rightfield"> <span
				class="title2rightfield-icon1-text"> <sec:authorize
						access="hasAuthority('CREATE_TRIGGER_CONFIG')">
						<a href="#" onclick="createTrigger(); "><i
							class="fa fa-plus-circle"></i></a>

						<a href="#" id="createTrigger" onclick="createTrigger();">
							<spring:message code="trigger.mgmt.add" ></spring:message>
						</a>

					</sec:authorize>
			</span>
			<span class="title2rightfield-icon1-text"> 
				<sec:authorize access="hasAuthority('DELETE_TRIGGER_CONFIG')">
					<a href="#"><i class="fa fa-trash" onclick="displayDeleteTriggerTablesPopup();"></i></a>

					<a href="#" id="deleteTriggerTable" onclick="displayDeleteTriggerTablesPopup();">
						<spring:message code="trigger.mgmt.delete.link" ></spring:message>
					</a>
					<a href="#divDeleteTriggers" class="fancybox" style="display: none;" id="delete_triggers">#</a>
				</sec:authorize>
			</span>
			</span>
		</div>
	
	</div>
	
	<a href="#divAddTriggerTable" class="fancybox" style="display: none;" id="divAddTriggerTablePopUp">#</a>
		
	<div id="divAddTriggerTable" style=" width:100%; display: none;" >
		<jsp:include page="addTriggerPopUp.jsp"></jsp:include>
	</div>
	<div class="box-body table-responsive no-padding box">
		<table class="table table-hover" id="tableGrid"></table>
		<div id="tableGridPagingDiv"></div>
	</div>
	
	<!-- Delete warning message popup div start here -->
	<a href="#delete_msg_div" class="fancybox" style="display: none;"
		id="delete_warn_msg_link">#</a>
	<div id="delete_msg_div" style="width: 100%; display: none;">
		<div class="modal-content">
			<div class="modal-header padding10">
				<h4 class="modal-title">
					<spring:message code="trigger.delete.popup.warning" ></spring:message>
				</h4>
			</div>
			<div class="modal-body padding10 inline-form">
				<p id="warn">
					<spring:message code="trigger.mgmt.selection.warning" ></spring:message>
				</p>
			</div>
			<div class="modal-footer padding10">
				<button type="button" class="btn btn-grey btn-xs "
					data-dismiss="modal" onclick="closeFancyBox();">
					<spring:message code="btn.label.close" ></spring:message>
				</button>
			</div>
		</div>
		<!-- /.modal-content -->
	</div>
	<!-- Delete warning message popup  div end here-->
	
	<!-- Delete attribute popup div start here -->
	<div id="divDeleteTriggers" style="width: 100%; display: none;">
		<div class="modal-content">
			<div class="modal-header padding10">
				<h4 class="modal-title">
					<spring:message code="trigger.mgmt.delete" ></spring:message>
				</h4>
			</div>
			<div class="modal-body padding10 inline-form">
				<div id="deletePopUpMsg" class=fullwidth>
					<jsp:include page="../common/responseMsgPopUp.jsp" ></jsp:include>
				</div>
				<div class="fullwidth">
					<input type="hidden" value="" id="deleteTriggersId"
						name="deleteTriggersId" />
					<div id="delete_selected_triggers_details"></div>
					<div>
						<spring:message code="trigger.delete.warning" ></spring:message>
					</div>
				</div>
			</div>
			
			<div id="delete_triggers_bts_div" class="modal-footer padding10">
				<button id="delete_confirm" type="button" class="btn btn-grey btn-xs"
					 onclick="deleteTriggers(true);">
					<spring:message code="btn.label.delete" ></spring:message>
				</button>
				<button type="button" class="btn btn-grey btn-xs"
					onclick="closeFancyBox();reloadTableGridData();">
					<spring:message code="btn.label.cancel" ></spring:message>
				</button>
			</div>
		
			<div id="delete_triggers_progress_bar_div"
				class="modal-footer padding10 text-left" style="display: none;">
				<jsp:include page="../common/processing-bar.jsp"></jsp:include>
			</div>
		</div>
	</div>
	<!-- Delete attribute popup div end here -->
	
	<a href="#divAssociationList" class="fancybox" style="display: none;" id="view_association_link">#</a>
	<div id="divAssociationList" style=" width:100%; display: none;" >
	   <div class="modal-content">
	        <div class="modal-header padding10">
	            <h4 class="modal-title"><spring:message code="trigger.mgmt.association.popup.header.label"></spring:message> </h4>
	        </div>
	        <div class="modal-body padding10 inline-form">
	        		<div>
	        			<p><spring:message code="trigger.association.popup.description.label"></spring:message></p>
	        			<p><b><spring:message code="trigger.association.association.label"></spring:message></b></p>
	        		</div>
					<div id="association_list_div"></div>	        	
	        </div>
	        <div id="start-buttons-div" class="modal-footer padding10">
	            <button type="button" class="btn btn-grey btn-xs " id="server-start-close-btn" data-dismiss="modal" onclick="closeFancyBox();"><spring:message code="btn.label.close"></spring:message></button>
	        </div>
	    </div>
	</div>
	
</div>
</div>
		
<script type="text/javascript">
var deletableRows = {};
function resetSearchTriggerCriteria(){
	$("#searchTriggerName").val("");
	$("#triggerTypeEnum").val("SELECT SCHEDULER TYPE");
}

$(document).ready(function() {
	loadTriggerGrid(false);
	reloadTableGridData();
	initializeFromAndToDate();
	viewRcrPtrBasedOnType('Minute');
	rangeOfRecurrence();
	disableNoEndDateRadioButtonField();
});

function loadTriggerGrid(isSearch){
	$("#tableGrid").jqGrid({
		url: "<%= ControllerConstants.INIT_TRIGGER_DATA_LIST%>",
		postData : {
			searchName: function () {
   	        	return $("#searchTriggerName").val();
    		},
    		searchType: function () {
   	        	return $("#triggerTypeEnum").val();
    		},
    		'isSearch':isSearch
		},
		datatype: "json",
		colNames: [
					  "#",
					  "#",
					  "<spring:message code='trigger.mgmt.jqgrid.trigger.name' ></spring:message>",
		           	  "<spring:message code='trigger.mgmt.jqgrid.description' ></spring:message>",
		              "<spring:message code='trigger.mgmt.jqgrid.Type' ></spring:message>",
		          	  "<spring:message code='trigger.mgmt.jqgrid.execution.start' ></spring:message>",
		           	  "<spring:message code='trigger.mgmt.jqgrid.execution.end' ></spring:message>",
		           	  "<spring:message code='trigger.mgmt.edit' ></spring:message>",
		           	  "<spring:message code='trigger.mgmt.jqgrid.association' ></spring:message>"
		           ],
		colModel: [
			{name:'id',index:'id',hidden:true},
			{name: 'checkbox', index: 'checkbox',sortable:false, formatter:checkBoxFormatter, align : 'center', width: 30},
			{name:'triggerName',index:'triggerName'},
			{name:'description',index:'description'},
			{name:'type',index:'type',sortable:false,align:'center'},
			{name:'executionStart',sortable:false,index:'executionStart',align : 'center'},
			{name:'executionEnd',index:'executionEnd',sortable:false,align : 'center'},
			{name:'edit',index:'edit',align:'center',formatter:editFormatter,sortable:false,width: 50},
			{name:'association',index:'association', formatter:associationFormatter,sortable:false,align : 'center',width: 50}
		],
		rowNum:<%= MapCache.getConfigValueAsInteger(SystemParametersConstant.TOTAL_ROWS_TO_DISPLAY_IN_GRID, 10) %>,
		rowList:[5,10,20,50,100],
		height: 'auto',
		sortname: 'id',
		sortorder: "desc",
		pager: "#tableGridPagingDiv",
		viewrecords: true,
		multiselect: false,
		timeout : 120000,
	    loadtext: "Loading...",
		caption: "<spring:message code="trigger.mgmt.list"></spring:message>",
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
	             //$("#agentGridPagingDiv").hide();
	         }
	 			var check = true;
				$.each($("input[name='TriggerCheckbox']:not(:checked)"), function(){  
					check = false;
		        });
				var count = jQuery("#tableGrid").jqGrid('getGridParam', 'reccount');
				if(count === 0){
					check = false;
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
	}).navGrid("#tableGridPagingDiv",{edit:false,add:false,del:false,search:false});
	$(".ui-jqgrid-titlebar").hide();
	$(".ui-pg-input").height("10px");
}

function editFormatter(cellvalue, options, rowObject){
	var id=rowObject.triggerName+"_edit";
	return '<a id="'+id+'" class="link" onclick="updateTrigger('+"'" + rowObject["id"]+ "'" +','+"'" + cellvalue+ "'" + ');">' + "<i class='fa fa-pencil-square-o' aria-hidden='true'></i></a>";
}
var recurrenceType = "Minute";
var dropdownDefaultValue=0;
var now = new Date();
var day = ("0" + now.getDate()).slice(-2);
var month = ("0" + (now.getMonth() + 1)).slice(-2);
var today = (month)+"/"+(day)+"/"+now.getFullYear();

function createTrigger(){
	clearAllMessages();
	resetWarningDisplayPopUp();
	$("#triggerName").val("");
	$("#description").val("");
	$("#"+recurrenceType).prop("checked",true);
	viewRcrPtrBasedOnType(recurrenceType);
	$("#alterationCount").val("");
	$("#executionStartingHour").val($("#"+dropdownDefaultValue).val());
	$("#executionStartingMinute").val($("#"+dropdownDefaultValue).val());
	$("#executionEndingHour").val($("#"+dropdownDefaultValue).val());
	$("#executionEndingMinute").val($("#"+dropdownDefaultValue).val());
	$("#startAtDate").val(today);
	$("#startAtHour").val($("#"+dropdownDefaultValue).val());
	$("#startAtMinute").val($("#"+dropdownDefaultValue).val());
	$("#endAtDate").val(today);
	$("#endAtHour").val($("#"+dropdownDefaultValue).val());
	$("#endAtMinute").val($("#"+dropdownDefaultValue).val());
	$("#add_label").show();
	$("#update_label").hide();
	$("#addTrigger").show();
	$("#updateTrigger").hide();
	$("#divAddTriggerTablePopUp").click();
}

function updateTrigger(tableId,cellvalue){
	clearAllMessagesPopUp();
	resetWarningDisplayPopUp();
	var responseObject = jQuery("#tableGrid").jqGrid('getRowData', tableId);
	clearAllMessages();
	$("#triggerName").val(responseObject.triggerName);
	$("#description").val(responseObject.description);
	var id = $("#id").val(tableId);
	$("#"+responseObject.type).prop("checked",true);
	viewRcrPtrBasedOnType(responseObject.type);
	getTriggerById(tableId);
}

function getTriggerById(Id) {
	$.ajax({
		url: 'getTriggerById',
		cache: false,
		async: true,
		dataType: 'json',
		type:'POST',
		data:{"id":Id},
		success: function(data){
			var response = eval(data);
			var responseObject = response.object;
				if(responseObject.recurrenceType=="Minute" || responseObject.recurrenceType=="Hourly" || responseObject.recurrenceType=="Daily"){
					$("#alterationCount").val(responseObject.alterationCount);
					if(responseObject.recurrenceType!="Daily"){
						$("#executionEndingHour").val($("#"+responseObject.executionEndingHour).val());
						$("#executionEndingMinute").val($("#"+responseObject.executionEndingMinute).val());
					}
				} else if(responseObject.recurrenceType=="Weekly"){
					var daysOfWeek = responseObject.dayOfWeek.split(',');
					for(var i = 0; i < daysOfWeek.length; i++) {
						$("#"+daysOfWeek[i]).prop("checked",true);
					}
				} else if(responseObject.recurrenceType=="Monthly"){
					if(responseObject.dayOfMonth!=undefined){
						$("#dayOfMonth").val(responseObject.dayOfMonth);
						$("#day").prop("checked",true);
					}else{
						$("#"+responseObject.firstOrLastDayOfMonth).prop('selected', true);
						$("#every").prop("checked",true);
						$("#firstOrLastDayOfMonth").prop("disabled", false);
						$("#dayOfMonth").prop("disabled", true);
					}
					$("#alterationCount").val(responseObject.alterationCount);
				}
				$("#executionStartingHour").val($("#"+responseObject.executionStartingHour).val());
				$("#executionStartingMinute").val($("#"+responseObject.executionStartingMinute).val());
				var startDate = responseObject.startAtDate;
				startDate = startDate.substr(0,startDate.indexOf(" "));
				$("#startAtDate").val(startDate);
				$("#startAtHour").val($("#"+responseObject.startAtHour).val());
				$("#startAtMinute").val($("#"+responseObject.startAtMinute).val());
				if(responseObject.endAtDate!=undefined){
					$("#endby").prop("checked",true);
					var endDate =  responseObject.endAtDate;
					endDate = endDate.substr(0,endDate.indexOf(" "));
					$("#endAtDate").val(endDate);
					$("#endAtHour").val($("#"+responseObject.endAtHour).val());
					$("#endAtMinute").val($("#"+responseObject.endAtMinute).val());
					$("#endAtDate").prop("disabled", false);
					$("#endAtHour").prop("disabled", false);
					$("#endAtMinute").prop("disabled", false);
				}else{
					$("#noenddate").prop("checked",true);
				}
				$("#add_label").hide();
				$("#update_label").show();
				$("#addTrigger").hide();
				$("#updateTrigger").show();
				$("#divAddTriggerTablePopUp").click();
		},
		error: function (xhr,st,err){
			handleGenericError(xhr,st,err);
		}
	});
}

function deleteTriggers(multipleDelete){
	if(multipleDelete){
		$("#deleteTriggersId").val(ckSelectedTriggers.toString());
	}
	$("#delete_triggers_bts_div").hide();
	//$("#delete_lookup_Table_bts_div").hide();
	$("#delete_triggers_progress_bar_div").show();
	//$("#delete_lookup_Table_progress_bar_div").show();
	
	$.ajax({
		url: 'deleteTriggerConfig',
		cache: false,
		async: true,
		dataType:'json',
		type:'POST',
		data:
		 {
			"ids" : $("#deleteTriggersId").val()
		 }, 
		success: function(data){
			var response = eval(data);
			var responseCode = response.code; 
			var responseMsg = response.msg; 
		
			clearAllMessagesPopUp();
			resetWarningDisplayPopUp();
			 
			if(responseCode === "200"){
				$("#delete_triggers_bts_div").hide();
				//$("#delete_lookup_Table_bts_div").hide();
				$("#delete_triggers_progress_bar_div").hide();
				//$("#delete_lookup_Table_progress_bar_div").hide();
				//$("#delete_warning").hide();
				reloadTableGridData();
				showSuccessMsg(responseMsg);
				closeFancyBox();
				ckSelectedLookupTables = new Array();
				deletableRows = {};
			}else{
				showErrorMsgPopUp(responseMsg);
				$("#delete_triggers_bts_div").show();
				//$("#delete_lookup_Table_bts_div").show();
				$("#delete_triggers_progress_bar_div").hide();
				//$("#delete_lookup_Table_progress_bar_div").hide();
			}
		},
	    error: function (xhr,st,err){
			handleGenericError(xhr,st,err);
		}
	});
}

function getSelectedTriggerList(rowId){
	var rowData = jQuery('#tableGrid').jqGrid ('getRowData', rowId);
	if( document.getElementById("checkbox_"+rowData.triggerName).checked === true){
		if(ckSelectedTriggers.indexOf(rowId) === -1){
			ckSelectedTriggers.push(rowId);
			deletableRowData(rowId);
		}
	}else{
		if(ckSelectedTriggers.indexOf(rowId) !== -1){
			ckSelectedTriggers.splice(ckSelectedTriggers.indexOf(rowId), 1);
			delete deletableRows[rowId];
		}
	}
}

function deletableRowData(rowId){
	var deletableRecord;
	var rowData = jQuery('#tableGrid').jqGrid ('getRowData', rowId);
	deletableRows[rowId] = rowData.triggerName;
}

function TriggerTableHeaderCheckbox(e, element) {
    e = e || event;/* get IE event ( not passed ) */
    e.stopPropagation ? e.stopPropagation() : e.cancelBubble = false;

    var rowId;
    //if root checkbox is checked :-> all child checkbox should ne checked
    if(element.checked) {
    	$('input:checkbox[name="triggerCheckbox"]').prop('checked',true);
    	$.each($("input[name='triggerCheckbox']:checked"), function(){
    		rowId = Number($(this).val()); 
    		if(ckSelectedTriggers.indexOf(rowId) === -1){
    			ckSelectedTriggers.push(rowId);
        		deletableRowData(rowId);
    		}
        });
    } else {
    	//if root checkbox is unchecked :-> all child checkbox should be unchecked
    	$('input:checkbox[name="triggerCheckbox"]').prop('checked',false);
    	$.each($("input[name='triggerCheckbox']:not(:checked)"), function(){  
    		rowId = Number($(this).val()); 
    		if(ckSelectedTriggers.indexOf(rowId) !== -1){
    			ckSelectedTriggers.splice(ckSelectedTriggers.indexOf(rowId), 1);
    			delete deletableRows[rowId];
    		}
        });
    }
}


function reloadTableGridData(isSearch){
	clearAllMessages();
	var $grid = $("#tableGrid");
	jQuery('#tableGrid').jqGrid('clearGridData');
	clearTableGrid();
	ckSelectedTriggers = [];
	deletableRows = {};

	$grid.jqGrid('setGridParam',{datatype:'json',page: 1,sortname: 'id',sortorder: "desc",postData:{
	  
   		'searchName': function () {
	        return $("#searchTriggerName").val();
   		},
   		'searchType': function () {
	        return $("#triggerTypeEnum").val();
   		},
   		'isSearch':isSearch}}).trigger('reloadGrid');
	
}

function clearTableGrid(){
	$grid = $("#tableGrid");
	var rowIds = $grid.jqGrid('getDataIDs');
	// iterate through the rows and delete each of them
	for(var i=0,len=rowIds.length;i<len;i++){
		var currRow = rowIds[i];
		$grid.jqGrid('delRowData', currRow);
	}
}

function displayDeleteTriggerTablesPopup(){	
	clearAllMessagesPopUp();
	resetWarningDisplayPopUp();
	clearAllMessages();
	if(ckSelectedTriggers.length === 0){
		$("#warn").show();
		$("#delete_warn_msg_link").click();
		return;
	}else{
		var tableString ="<table class='table table-hover table-bordered'  border='1' >";
		tableString+="<th>Scheduler Name</th>";
		
		$.each(deletableRows, function( index, value ) {
			tableString += "<tr>";
			tableString += "<td>"+value+"</td>";
			tableString += "</tr>";
			});	
		
		tableString+="</table>"; 
		
		$("#delete_selected_triggers_details").html(tableString);
		$("#delete_triggers_bts_div").show();
		$("#delete_triggers_progress_bar_div").hide();
		//$("#deleteLookupTablesId").val(ckSelectedLookupTables.toString());
		$("#delete_triggers").click();
	}
}

function getSelectedTrigger(rowId){
	rowId = Number(rowId);
	if( document.getElementById("table_"+rowId).checked === true){
		if(ckSelectedTriggers.indexOf(rowId) === -1){
			ckSelectedTriggers.push(rowId);
			deletableRowData(rowId);
		}
	}else{
		if(ckSelectedTriggers.indexOf(rowId) !== -1){
			ckSelectedTriggers.splice(ckSelectedTriggers.indexOf(rowId), 1);
			delete deletableRows[rowId];
		}
	}
}

function associationFormatter(cellvalue, options, rowObject) {

	if (rowObject["association"] === false) {
		return '<span style="cursor: pointer;" alt="'
				+ rowObject["association"]
				+ '"><i class="fa fa-retweet grey"></i></span>';

	} else if (rowObject["association"] === true) {
		return '<span id="mapping_association'
				+ rowObject["id"]
				+ '" src="img/orange.png"  style="cursor: pointer;" alt="'
				+ rowObject["association"] 
				+ '" onclick="getAllAssociationDetails(' + rowObject["id"] + ')"><i class="fa fa-retweet orange"></i></span>';
	}
}

function checkBoxFormatter(cellvalue, options, rowObject) {
	
	if (rowObject["association"] === true) {
		return '<input type="checkbox" disabled="disabled" id="checkbox_'+rowObject["triggerName"]+'" name="triggerCheckbox" value="'+rowObject["id"]+'"/>';
	} else {
		return '<input type="checkbox" id="checkbox_'+rowObject["triggerName"]+'" name="triggerCheckbox" value="'+rowObject["id"]+'" onchange="getSelectedTriggerList('+rowObject.id+');"/>';
	}
}

/* Function will display all association details for current selected scheduler. */
function getAllAssociationDetails(id) {
	$.ajax({
		url : "getAssociationDetails",
		cache : false,
		async : true,
		dataType : 'json',
		type : 'POST',
		data : {
			"id" : id,
		},
		success : function(data) {
			var response = eval(data);
			var responseCode = response.code;
			var responseMsg = response.msg;
			var responseObject = response.object;
			var tableString = "<table class='table table-hover table-bordered'  border='1'>";
			tableString += "<th> Job Id </th>"
					+"<th> Job Type </th>";

			if (responseCode == "200") {
				$.each(responseObject, function(index, responseObject) {
					tableString += "<tr>";
					tableString += "<td>"+ responseObject['jobId'] +"</td>";
					tableString += "<td>"+ responseObject['jobType'] + "</td>";
					tableString += "</tr>";
				});

			}
			tableString += "</table>";
			$('#association_list_div').html(tableString);
			$("#view_association_link").click();
			
		},
		error : function(xhr, st, err) {
			handleGenericError(xhr, st, err);
		}

	}); 
}

</script>		
