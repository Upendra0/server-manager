<%@page import="com.elitecore.sm.common.constants.BaseConstants"%>
<%@page import="com.elitecore.sm.common.model.DecodeTypeEnum"%>
<%@page import="com.elitecore.sm.common.constants.FormBeanConstants"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="com.elitecore.sm.util.MapCache"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@page
	import="com.elitecore.sm.common.constants.SystemParametersConstant"%>
<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<%@page import="com.elitecore.sm.common.constants.EngineConstants"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/security/tags"
	prefix="sec"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="/WEB-INF/ShowErrorTag.tld" prefix="elitecoreError"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<div class="tab-content no-padding clearfix" id="rule-data-configuration-block">
<div class="fullwidth mtop10">
		<div class="fullwidth">
			<div class="title2">
				<spring:message code="rule.data.mgmt.search.title" ></spring:message>
			</div>
		</div>
		<div class="col-md-6 inline-form"
			style="padding-left: 0px !important;">
			<div class="form-group">
				<div class="table-cell-label">
					<spring:message code="rule.data.mgmt.table.name" ></spring:message>
				</div>
				<div class="input-group">
					<spring:message code="rule.data.mgmt.table.name" var="tooltip" ></spring:message>
					<input type="text" id="ruleDataTableName"
						class="form-control table-cell input-sm" data-toggle="tooltip"
						tabindex="1" data-placement="bottom" title="${tooltip}"> <span
						class="input-group-addon add-on last"> <i
						class="glyphicon glyphicon-alert" data-toggle="tooltip"
						data-placement="bottom"></i>
					</span>
				</div>
			</div>
		</div>
		<div class="col-md-6 inline-form"
			style="padding-left: 0px !important;">
			<div class="form-group">
				<spring:message code="rule.data.mgmt.desc" var="tooltip" ></spring:message>
				<div class="table-cell-label">${tooltip}</div>
				<div class="input-group ">
					<input type="text" id="rule-data-desc"
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
			<button id='search' class="btn btn-grey btn-xs" onclick="reloadTableGridData('true');"
				tabindex="5">
				<spring:message code="btn.label.search" ></spring:message>
			</button>
			<button class="btn btn-grey btn-xs"
				onclick="resetSearchRuleCriteria();reloadTableGridData('false');" tabindex="6">
				<spring:message code="btn.label.reset" ></spring:message>
			</button>
		</div>
	<div class="fullwidth">
		<div class="title2">
			<spring:message code="rule.data.mgmt.list" ></spring:message>
			<span class="title2rightfield"> <span
				class="title2rightfield-icon1-text"> <sec:authorize
						access="hasAuthority('CREATE_RULE_DATA_CONFIG')">
						<a href="#" onclick="createLookUpTable(); "><i
							class="fa fa-plus-circle"></i></a>

						<a href="#" id="createRuleData" onclick="createLookUpTable();">
							<spring:message code="rule.data.mgmt.add" ></spring:message>
						</a>

					</sec:authorize>
			</span>
			<span class="title2rightfield-icon1-text"> 
				<sec:authorize access="hasAuthority('DELETE_RULE_DATA_CONFIG')">
					<a href="#"><i class="fa fa-trash" onclick="displayDeleteLookupTablesPopup();"></i></a>

					<a href="#" id="deleteRuleDataTable" onclick="displayDeleteLookupTablesPopup();">
						<spring:message code="rule.data.mgmt.delete.link" ></spring:message>
					</a>
					<a href="#divDeleteLookupTables" class="fancybox" style="display: none;" id="delete_lookup_Tables">#</a>
				</sec:authorize>
			</span>
			</span>
		</div>
	
	<a href="#divUploadLookupTableData" class="fancybox" style="display: none;" id="uploadLookupTableData">#</a>
 		<div id="divUploadLookupTableData" style=" width:100%; display: none;" >
 			<jsp:include page="uploadRuleLookupData.jsp"></jsp:include>
	</div>

	<a href="#divViewFieldList" class="fancybox" style="display: none;" id="viewFieldList">#</a>
 		<div id="divViewFieldList" style=" width:100%; display: none;" >
 			<jsp:include page="viewFieldListPopUp.jsp"></jsp:include>
	</div>
	<a href="#divDeleteLookupTable" class="fancybox" style="display: none;" id="delete_lookup_table">#</a>
	</div>
	<a href="#divAddRuleTable" class="fancybox" style="display: none;" id="divAddRuleTablePopUp">#</a>
		
	<div id="divAddRuleTable" style=" width:100%; display: none;" >
		<jsp:include page="addRuleTablePopUp.jsp"></jsp:include>
	</div>
	<div class="box-body table-responsive no-padding box">
		<table class="table table-hover" id="tableGrid"></table>
		<div id="tableGridPagingDiv"></div>
	</div>
	<div id="divDeleteLookupTable" style="display: none;">
		<jsp:include page="deleteLookupTable.jsp"></jsp:include>
	</div>
	
	<form action="<%= ControllerConstants.DOWNLOAD_LOOKUP_DATA_FILE%>" method="POST" id="download_lookup_table_form">
		<input type="hidden" id="downloadTableId" name = "downloadTableId"/>
		<input type="hidden" id="downloadTableName" name = "downloadTableName"/>
		<input type="hidden" id="includeData" name ="includeData" />
	</form>
	
	<form action="<%= ControllerConstants.INIT_LOOKUP_DATA_CONFIG%>" method="POST" id="view_lookup_table_records_form">
		<input type="hidden" id="viewLookupTableId" name = "viewLookupTableId"/>
		<input type="hidden" id="viewLookupTablename" name = "viewLookupTablename"/>
		<input type="hidden" id="viewLookupTableDescription" name = "viewLookupTableDescription"/>
	</form>
	
	<!-- Delete warning message popup div start here -->
	<a href="#delete_msg_div" class="fancybox" style="display: none;"
		id="delete_warn_msg_link">#</a>
	<div id="delete_msg_div" style="width: 100%; display: none;">
		<div class="modal-content">
			<div class="modal-header padding10">
				<h4 class="modal-title">
					<spring:message code="device.delete.popup.warning.heading.label" ></spring:message>
				</h4>
			</div>
			<div class="modal-body padding10 inline-form">
				<p id="warn">
					<spring:message code="rule.data.mgmt.selection.warning.label.lookup.table" ></spring:message>
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
	<div id="divDeleteLookupTables" style="width: 100%; display: none;">
		<div class="modal-content">
			<div class="modal-header padding10">
				<h4 class="modal-title">
					<spring:message code="rule.data.mgmt.delete" ></spring:message>
				</h4>
			</div>
			<div class="modal-body padding10 inline-form">
				<div id="deletePopUpMsg" class=fullwidth>
					<jsp:include page="../common/responseMsgPopUp.jsp" ></jsp:include>
				</div>
				<div class="fullwidth">
					<input type="hidden" value="" id="deleteLookupTablesId"
						name="deleteLookupTablesId" />
					<div id="delete_selected_tables_details"></div>
					<div>
						<spring:message code="lookup.table.delete.warning" ></spring:message>
					</div>
				</div>
			</div>
			
			<div id="delete_lookup_Tables_bts_div" class="modal-footer padding10">
				<button id="delete_confirm" type="button" class="btn btn-grey btn-xs"
					onclick="deleteLookupTable(true);">
					<spring:message code="btn.label.delete" ></spring:message>
				</button>
				<button type="button" class="btn btn-grey btn-xs"
					onclick="closeFancyBox();reloadTableGridData();">
					<spring:message code="btn.label.cancel" ></spring:message>
				</button>
			</div>
		
			<div id="delete_lookup_Tables_progress_bar_div"
				class="modal-footer padding10 text-left" style="display: none;">
				<jsp:include page="../common/processing-bar.jsp"></jsp:include>
			</div>
		</div>
	</div>
	<!-- Delete attribute popup div end here -->
	
	<!-- Download Option Popup for Rule Lookup Data  Start -->
	
	<a href="#downloadPopUpOption" class="fancybox" style="display: none;" id="downloadPopUp">#</a>
 	<div id="downloadPopUpOption" style=" width:100%; display: none;" >
 		<div class="modal-header padding10">
		   <h4 class="modal-title">Export Rule Lookup Data Table</h4>
		</div>
		<div class="col-md-12">
			<div class="col-md-6">Export Table Fields Only
				<a onclick="downloadTableData(false);"><img src="img/download-file.png" height="20" width="20"></a>
			</div>
			<div class="col-md-6">Export Table Fields with Data
				<a onclick="downloadTableData(true);"><img src="img/download-file.png" height="20" width="20"></a>
			</div>
		</div>
		<div id="closeGridDiv" class="modal-footer padding10">
        	 <button type="button" class="btn btn-grey btn-xs " id="fieldListClose" data-dismiss="modal" onclick="closeFancyBox();removeFieldGrid();"><spring:message code="btn.label.close"></spring:message></button>
     	</div>		
	</div>
	
	<!-- Download Option Popup for Rule Lookup Data  End -->
	
</div>
</div>

<sec:authorize access="hasAnyAuthority('DELETE_RULE_DATA_CONFIG')" var="isDeleteAuthorize"></sec:authorize>
<sec:authorize access="hasAnyAuthority('UPDATE_RULE_DATA_CONFIG')" var="isUpdateAuthorize"></sec:authorize>

<script>

function downloadPopUp(id){
	var responseObject = jQuery("#tableGrid").jqGrid('getRowData', id);
	$("#downloadTableId").val(responseObject.id);
	$("#downloadTableName").val(responseObject.viewName);
	$("#downloadPopUp").click();
}

//If flag is true then download table with data
// then download only table meta data 
function downloadTableData(flag){
	$("#includeData").val(flag);
	$("#download_lookup_table_form").submit();
}

var deletableRows = {};
$(document).ready(function() {
	loadRuledataDictGrid(false);
	reloadTableGridData();
});

var fieldList;
function resetSearchRuleCriteria(){
	$("#ruleDataTableName").val("");
	$("#rule-data-desc").val("");
}


function createLookUpTable(){
	clearAllMessages();
	resetWarningDisplayPopUp();
	$("#update_label").hide();
	$("#updateLookupTable").hide();
	$("#add_label").show();
	$("#limit_reached").hide();
	$("#addNewLookupTable").show();
	$("#fieldList").empty();
	$("#viewName").val("");
	$("#description").val("");
	$("#uploadLookupTable").show();
	resetCounter();
	var actionTableString = '<tr>';
		actionTableString +='<td>Field Name</td>';
		/* actionTableString +='<td>Display Name</td>'; */
		actionTableString +='<td>Unique</td>';
		actionTableString +='<td>Delete</td>';
		actionTableString +='</tr>"';
	$("#fieldList").append(actionTableString);
	//addCompulsaryId();
	$("#divAddRuleTablePopUp").click();
	$("#importDiv").show();
}


function createUpdateRuleLookupTable(action,index){
	clearAllMessages();
	resetWarningDisplayPopUp();
	createFieldJson(index);
	$("#createTableDiv").hide();
	$("#activate_full_license_processbar_add_table").show();
	var viewName = $("#viewName").val();
	var description = $("#description").val();
	var fileName = $("#ruleDataLookupFile").val();
	var isFileWithData = $("#ruleDataLookupFileWithData").val();
	if(isFileWithData == undefined || isFileWithData == "")
		isFileWithData = false;
	$.ajax({
		url: action,
		cache: false,
		async: true,
		dataType:'json',
		type:"POST",
		data:  {
			"id":$("#id").val(),
			"viewName":viewName,
			"description":description,
			"fieldList":fieldList,
			"fileName":fileName,
			"isFileWithData":isFileWithData
		}, 
		success: function(data){
			$("#createTableDiv").show();
			$("#activate_full_license_processbar_add_table").hide();
			if(fileName != null && fileName != "" && (isFileWithData == "true")){
				 var response = data;
	        	 var responsedata = response[0];
	        	 if(responsedata !== undefined && responsedata !== 'undefined'){
		        	 var uploadedSummary = response[1];
		        	 var summaryMODE = 'append';
		  		 	var responseCode = responsedata.code;
		  			var responseMsg = responsedata.msg;
		  			if(responseCode === "200"){
		  			    showSuccessMsgPopUp(jsSpringMsg.fileUploadSucessMsg);
				  		setSummaryData( uploadedSummary, summaryMODE );
				  		$('#uploadedData').click();
					}else{
						 showErrorMsgPopUp(responseMsg);
					}
				} else {
					var response = eval(data);
					var responseObject = response.object;
					var modifiedJsonKey = modifyJsonKey( responseObject );
					addErrorIconAndMsgForAjaxPopUp( modifiedJsonKey );
				}
	          
			}else{
				var response = eval(data);
				var responseCode = response.code; 
				var responseMsg = response.msg; 
				var responseObject = response.object;
				clearAllMessagesPopUp();
				resetWarningDisplayPopUp();
				if(responseCode === "200"){
					closeFancyBox();
					closeFancyBoxFromChildIFrame();
					reloadTableGridData();
					showSuccessMsg(responseMsg);
				}else if(responseObject !== undefined && responseObject !== 'undefined' && responseCode === "400"){
					var modifiedJsonKey = modifyJsonKey( responseObject );
					addErrorIconAndMsgForAjaxPopUp( modifiedJsonKey );
				}else{
					showErrorMsgPopUp(responseMsg);
				}
			}
		},
	    error: function (xhr,st,err){
	    	$("#createTableDiv").show();
	    	$("#activate_full_license_processbar_add_table").hide();
			handleGenericError(xhr,st,err);
		}
	});
}


function modifyJsonKey( myJSONDatea ){
	
	$.each( myJSONDatea , function(key,val){				
				if(key!=null){				
			    	var keys =  key.replace("[","");
			    		keys =  keys.replace("]","");
					if(  keys !=  key){
					   	myJSONDatea[keys]=val;
					 }		
				}
	});
	
	console.log( myJSONDatea );
	
	return myJSONDatea ;
}


function createFieldJson(index){
	var data = [];
	var toPass = [];
	var j=0;
	for(var i = 0;i<=index ; i++){
		var elementFieldName = document.getElementById("lookUpFieldDetailData"+ i +"fieldName" + i);
	//	var elementDisplayName = document.getElementById("lookUpFieldDetailData"+ i +"displayName" + i);
		var elementUnique = document.getElementById("unique"+i);
		if(elementFieldName != null){
			var fieldName = elementFieldName.value;
			var displayName = fieldName ; //elementDisplayName.value;
			var unique = elementUnique.value;
			data[j] = {"viewFieldName":fieldName,"displayName":displayName,"unique":unique};
			toPass.push(data[j]);
			j++;
		}
	}
	fieldList = JSON.stringify(toPass);
}

function loadRuledataDictGrid(isSearch){
	$("#tableGrid").jqGrid({
		url: "<%= ControllerConstants.INIT_RULE_TABLE_DATA_LIST%>",
		postData : {
			searchName: function () {
   	        	return $("#ruleDataTableName").val();
    		},
    		searchDesc: function () {
   	        	return $("#rule-data-desc").val();
    		},
    		'isSearch':isSearch
		},
		datatype: "json",
		colNames: [
					  "#",
					  "<input type='checkbox' id='selectAllLookupTables' onclick='LookupTableHeaderCheckbox(event, this)'></input>",
					  "<spring:message code='rule.data.mgmt.table.name' ></spring:message>",
		           	  "<spring:message code='rule.data.mgmt.desc' ></spring:message>",
		              "<spring:message code='rule.data.mgmt.field.list' ></spring:message>",
		          	  "<spring:message code='rule.data.mgmt.edit' ></spring:message>",
		           	  "<spring:message code='rule.data.mgmt.download' ></spring:message>",
		           	  "<spring:message code='rule.data.mgmt.upload' ></spring:message>",
		           	  "<spring:message code='rule.data.mgmt.view.records' ></spring:message>",	
		              "<spring:message code='business.policy.rule.group.grid.column.delete' ></spring:message>"	,
		              "viewDataSize"	  ],
		colModel: [
			{name:'id',index:'id',hidden:true},
			{name: 'checkbox', index: 'checkbox',sortable:false, formatter:lookupTablesCheckboxFormatter, align : 'center', width: 30},
			{name:'viewName',index:'viewName'},
			{name:'description',index:'description'},
			{name:'fieldList',index:'fieldList',align:'center',formatter:fieldListFormatter,sortable: false},
			{name:'edit',index:'edit',align:'center',formatter:editFormatter,sortable:false, hidden:true },
			{name:'download',index:'download',align:'center',formatter:downloadFormatter,sortable:false},
			{name:'upload',index:'upload',align:'center',formatter:uploadFormatter,sortable:false},
			{name:'viewRecords',index:'viewRecords',align:'center',formatter:viewRecordsFormatter,sortable:false},
			{name:'delete',index:'delete',align:'center', formatter:lookupTableDeleteFormatter,sortable: false},
			{name:'viewDataSize',index:'viewDataSize',hidden:true,sortable: false}
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
		caption: "<spring:message code="rule.data.mgmt.list"></spring:message>",
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
	 			var check = true;
				$.each($("input[name='lookupTablesCheckbox']:not(:checked)"), function(){  
					check = false;
		        });
				var count = jQuery("#tableGrid").jqGrid('getGridParam', 'reccount');
				if(count === 0){
					check = false;
				}
				$("#selectAllLookupTables").prop('checked',check);
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

function downloadFormatter(cellvalue, options, rowObject) {
	var id=rowObject.viewName+"_download";
	if( rowObject["viewDataSize"] < rowObject["viewDataDownloadLimit"] ){
		 return '<a id="'+id+'" href="#" onclick="downloadPopUp('+"'" + rowObject["id"]+ "'"+');"><i class="fa fa-fw fa-vimeo-square"></i></a>';
	}else{
		return '<a id="'+id+'" href="#" ><i class="fa fa-fw fa-vimeo-square" style="color:gray"></i></a>';
	}
}


function uploadFormatter(cellvalue, options, rowObject) {
	var id=rowObject.viewName+"_upload";
	if('${isUpdateAuthorize}' === 'true'){
		return '<a id="'+id+'" href="#" onclick="uploadPopUp('+"'" + rowObject["id"]+ "'"+');"><i class="fa fa-fw fa-vimeo-square"></i></a>';
	} else {
		return '<a id="'+id+'" href="#" " style="color:gray;"><i class="fa fa-fw fa-vimeo-square"></i></a>';
	}
}


function uploadPopUp(id){
	var responseObject = jQuery("#tableGrid").jqGrid('getRowData', id);
	$("#uploadTableId").val(responseObject.id);
	$("#uploadViewName").val(responseObject.viewName);
	$("#uploadDescription").val(responseObject.description);
	$('#uploadViewName').prop('readonly', true);
	$('#uploadDescription').prop('readonly', true);
	document.getElementById("dataFile").value = '';
	$(":file").filestyle('clear');
	$("#uploadLookupTableData").click();
}


function fieldListFormatter(cellvalue, options, rowObject) {
	var id=rowObject.viewName+"_fieldList";
	return '<a id="'+id+'" href="#" onclick="fieldListPopUp('+"'" + rowObject["id"]+ "'"+');"><i class="fa fa-fw fa-vimeo-square"></i></a>';
}


function fieldListPopUp(id) {
	clearAllMessages();
	var rowNum = <%= MapCache.getConfigValueAsInteger(SystemParametersConstant.TOTAL_ROWS_TO_DISPLAY_IN_GRID, 10) %>;
	loadJQueryFieldGrid(id,rowNum);
	$('#viewFieldList').click();
}

function lookupTableDeleteFormatter(cellvalue, options, rowObject){
	var id=rowObject.viewName+"_deletelookup";
	if('${isDeleteAuthorize}' === 'true'){
		return '<a id="'+id+'" href="#" onclick="deleteLookUpTablePopup('+"'" + rowObject["id"]+ "'"+');"><i class="fa fa-trash"></i></a>';
	} else {
		return '<a id="'+id+'" href="#" " style="color:gray;"><i class="fa fa-trash"></i></a>';
	}	
}

function deleteLookUpTablePopup(id){
	clearAllMessages();
	$("#delete_warning").show();
	$('#delete_label').show();
	$('#divDeleteTablePopup').show();
	$("#deleteLookupTablesId").val(id);
	$('#delete_lookup_table').click();
	var tableString ="<table class='table table-hover table-bordered'  border='1' >";
		tableString+="<th><spring:message code='rule.data.mgmt.table.name' ></spring:message></th>";
		tableString+="<th><spring:message code='rule.data.mgmt.desc' ></spring:message></th>";
		rowData = $('#tableGrid').jqGrid('getRowData',id);
		tableString += "<tr>";
		tableString += "<td>"+rowData.viewName+"</td>";
		tableString += "<td>"+rowData.description+"</td>";
		tableString += "</tr>";
		tableString+="</table>";
		$("#delete_selected_table_details").html(tableString);
		$("#delete_lookup_Table_bts_div").show();
		$("#delete_lookup_Table_progress_bar_div").hide();
	
}

function reloadTableGridData(isSearch){
	clearAllMessages();
	var $grid = $("#tableGrid");
	jQuery('#tableGrid').jqGrid('clearGridData');
	clearTableGrid();

	$grid.jqGrid('setGridParam',{datatype:'json',page: 1,sortname: 'id',sortorder: "desc",postData:{
	  
   		'searchName': function () {
	        return $("#ruleDataTableName").val();
   		},
   		'searchDesc': function () {
	        return $("#rule-data-desc").val();
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

function editFormatter(cellvalue, options, rowObject){
	var id=rowObject.viewName+"_edit";
	return '<a id="'+id+'" class="link" onclick="updateLookupTable('+"'" + rowObject["id"]+ "'" +','+"'" + cellvalue+ "'" + ');">' + "<i class='fa fa-pencil-square-o' aria-hidden='true'></i></a>";
}
function updateLookupTable(tableId,cellvalue){
	var responseObject = jQuery("#tableGrid").jqGrid('getRowData', tableId);
	clearAllMessages();
	$("#viewName").val(responseObject.viewName);
	$("#description").val(responseObject.description);
	$("#fieldList").empty();
	var actionTableString = '<tr>';
	actionTableString +='<td>Field Name</td>';
	/* actionTableString +='<td>Display Name</td>'; */
	actionTableString +='<td>Unique</td>';
	actionTableString +='<td>Delete</td>';
	actionTableString +='</tr>"';
	$("#fieldList").append(actionTableString);
	$("#add_label").hide();
	$("#addNewLookupTable").hide();
	$("#update_label").show();
	$("#updateLookupTable").show();
	$("#id").val(tableId);
	$("#description").val(responseObject.description);
	loadFieldListTable(tableId);
	$("#divAddRuleTablePopUp").click();
	$("#importDiv").hide();
}

function deleteLookupTable(multipleDelete){
	if(multipleDelete){
		$("#deleteLookupTablesId").val(ckSelectedLookupTables.toString());
	}
	$("#delete_lookup_Tables_bts_div").hide();
	$("#delete_lookup_Table_bts_div").hide();
	$("#delete_lookup_Tables_progress_bar_div").show();
	$("#delete_lookup_Table_progress_bar_div").show();
	$.ajax({
		url: 'deleteRuleLookupTable',
		cache: false,
		async: true,
		dataType:'json',
		type:'POST',
		data:
		 {
			"tableId" : $("#deleteLookupTablesId").val()
		 }, 
		success: function(data){
			var response = eval(data);
			var responseCode = response.code; 
			var responseMsg = response.msg; 
		
			clearAllMessagesPopUp();
			resetWarningDisplayPopUp();
			 
			if(responseCode === "200"){
				$("#delete_lookup_Tables_bts_div").hide();
				$("#delete_lookup_Table_bts_div").hide();
				$("#delete_lookup_Tables_progress_bar_div").hide();
				$("#delete_lookup_Table_progress_bar_div").hide();
				$("#delete_warning").hide();
				reloadTableGridData();
				showSuccessMsg(responseMsg);
				closeFancyBox();
				ckSelectedLookupTables = new Array();
				deletableRows = {};
			}else{
				showErrorMsgPopUp(responseMsg);
				$("#delete_lookup_Tables_bts_div").show();
				$("#delete_lookup_Table_bts_div").show();
				$("#delete_lookup_Tables_progress_bar_div").hide();
				$("#delete_lookup_Table_progress_bar_div").hide();
			}
		},
	    error: function (xhr,st,err){
			handleGenericError(xhr,st,err);
		}
	});
}

function viewRecordsFormatter(cellvalue, options, rowObject){
	var id=rowObject.viewName+"_viewRecords";
	return '<a id="'+id+'" href="#" onclick="viewRecordsInit('+"'" + rowObject["id"]+ "'"+');"><i class="fa fa-fw fa-vimeo-square"></i></a>';
}

function viewRecordsInit(id){
	var responseObject = jQuery("#tableGrid").jqGrid('getRowData', id);
	$("#viewLookupTableId").val(responseObject.id);
	$("#viewLookupTablename").val(responseObject.viewName);
	$("#viewLookupTableDescription").val(responseObject.description);
	$("#view_lookup_table_records_form").submit();
}

function lookupTablesCheckboxFormatter(cellvalue, options, rowObject){
	if ($.inArray( rowObject.id ,  ckSelectedLookupTables ) != -1){
		return '<input type="checkbox" id="checkbox_'+rowObject["viewName"]+'" name="lookupTablesCheckbox" value="'+rowObject["id"]+'" onchange="updateRootCheckbox(this, \'tableGrid\');getSelectedLookupTableList('+rowObject.id+');" checked/>';
	} else {
		return '<input type="checkbox" id="checkbox_'+rowObject["viewName"]+'"  name="lookupTablesCheckbox" value="'+rowObject["id"]+'" onchange="updateRootCheckbox(this, \'tableGrid\');getSelectedLookupTableList('+rowObject.id+');"/>';
	}
}

function getSelectedLookupTableList(rowId){
	var rowData = jQuery('#tableGrid').jqGrid ('getRowData', rowId);
	if( document.getElementById("checkbox_"+rowData.viewName).checked === true){
		if(ckSelectedLookupTables.indexOf(rowId) === -1){
			ckSelectedLookupTables.push(rowId);
			deletableRowData(rowId);
		}
	}else{
		if(ckSelectedLookupTables.indexOf(rowId) !== -1){
			ckSelectedLookupTables.splice(ckSelectedLookupTables.indexOf(rowId), 1);
			delete deletableRows[rowId];
		}
	}
}

function displayDeleteLookupTablesPopup(){	
	clearAllMessagesPopUp();
	resetWarningDisplayPopUp();
	clearAllMessages();
	if(ckSelectedLookupTables.length === 0){
		$("#warn").show();
		$("#delete_warn_msg_link").click();
		return;
	}else{
		var tableString ="<table class='table table-hover table-bordered'  border='1' ><th>#</th>";
		tableString+="<th>Table Name</th>";
		
		$.each(deletableRows, function( index, value ) {
			tableString += "<tr>";
			tableString += "<td><input type='checkbox' name='table_delete' id='table_"+index+"' checked  onclick=getSelectedTable('"+index+"')  value='"+index+"' /> </td>";
			/* tableString += "<td>"+ckIntanceSelected[i]+" </td>"; */
			tableString += "<td>"+value+"</td>";
			tableString += "</tr>";
			});	
		
		tableString+="</table>";
		
		$("#delete_selected_tables_details").html(tableString);
		$("#delete_lookup_Tables_bts_div").show();
		$("#delete_lookup_Tables_progress_bar_div").hide();
		//$("#deleteLookupTablesId").val(ckSelectedLookupTables.toString());
		$("#delete_lookup_Tables").click();
	}
}

function LookupTableHeaderCheckbox(e, element) {
    e = e || event;/* get IE event ( not passed ) */
    e.stopPropagation ? e.stopPropagation() : e.cancelBubble = false;

    var rowId;
    //if root checkbox is checked :-> all child checkbox should ne checked
    if(element.checked) {
    	$('input:checkbox[name="lookupTablesCheckbox"]').prop('checked',true);
    	$.each($("input[name='lookupTablesCheckbox']:checked"), function(){
    		rowId = Number($(this).val()); 
    		if(ckSelectedLookupTables.indexOf(rowId) === -1){
        		ckSelectedLookupTables.push(rowId);
        		deletableRowData(rowId);
    		}
        });
    } else {
    	//if root checkbox is unchecked :-> all child checkbox should be unchecked
    	$('input:checkbox[name="lookupTablesCheckbox"]').prop('checked',false);
    	$.each($("input[name='lookupTablesCheckbox']:not(:checked)"), function(){  
    		rowId = Number($(this).val()); 
    		if(ckSelectedLookupTables.indexOf(rowId) !== -1){
    			ckSelectedLookupTables.splice(ckSelectedLookupTables.indexOf(rowId), 1);
    			delete deletableRows[rowId];
    		}
        });
    }
}

/*
 * this function will handle root checkbox from child checkbox
 */
function updateRootCheckbox(element, gridId) {
	if(!element.checked){
		// if current child checkbox is not checked : uncheck root checkbox
		$('input:checkbox[id="selectAllLookupTables"]').prop('checked',false);
	} else {
		//if current child checkboox is checked and all checkbox are checked then check root checkbox
		var count = jQuery("#"+gridId).jqGrid('getGridParam', 'reccount');
		if ($('input:checkbox[name="lookupTablesCheckbox"]:checked').length == count) {
			$('input:checkbox[id="selectAllLookupTables"]').prop('checked',true);
	    }
	}
}

function deletableRowData(rowId){
	var deletableRecord;
	var rowData = jQuery('#tableGrid').jqGrid ('getRowData', rowId);
	deletableRows[rowId] = rowData.viewName;
}

function getSelectedTable(rowId){
	rowId = Number(rowId);
	if( document.getElementById("table_"+rowId).checked === true){
		if(ckSelectedLookupTables.indexOf(rowId) === -1){
			ckSelectedLookupTables.push(rowId);
			deletableRowData(rowId);
		}
	}else{
		if(ckSelectedLookupTables.indexOf(rowId) !== -1){
			ckSelectedLookupTables.splice(ckSelectedLookupTables.indexOf(rowId), 1);
			delete deletableRows[rowId];
		}
	}
}
</script>
