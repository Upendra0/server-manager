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
<style>
.popover {
    white-space: pre-line;    
}
</style>
<div class="tab-content no-padding clearfix" id="rule-data-configuration-block">
	<div class="fullwidth mtop10">
		
		<div class="col-md-5 inline-form"
            style="padding-left: 0px !important;">
            <div class="form-group">                
                <div class="table-cell-label">Table Name : ${tablename} </div>                
            </div>
        </div>
        <div class="col-md-5 inline-form"
            style="padding-left: 0px !important;">
            <div class="form-group">                
                <div class="table-cell-label">Description : ${tableDescription}</div>
                
            </div>
        </div>
        <div class="col-md-12 inline-form"
            style="padding-left: 0px !important;">
            <div class="form-group">                
                <div class="table-cell-label"><strong>Advance Search : </strong></div>
                
            </div>
        </div>
		
		<!-- Start Search Input-->
		<div class="col-md-8 inline-form"
			style="padding-left: 0px !important;">
			<div class="form-group">
				<%-- <spring:message code="business.policy.condition.existing.list.column.value" var="tooltip" ></spring:message> --%>
				<div class="table-cell-label" style="width: 125px;">Search Record : </div>
				<div class="input-group ">
					<input type="text" id="rule_data_search" class="form-control table-cell input-sm"
						   data-toggle="tooltip" tabindex="5" data-placement="bottom" 
						   title="Enter SQL Query to search specific data from table"> <!-- ${tooltip} -->
					<span class="input-group-addon add-on last inline-form" style="visibility: visible !important;"> 
						<a id="mydiv" href="#" data-toggle="popover" title="" data-trigger="focus"
                        data-content="1. All Sql Operators can be used for search. e.g. NAME like 'ab%' AND ID ='123' 
                        2.If any Field has record value like S'pore then during searching, search query must be written like : city = 'S''pore'
                        3. If you want to search field contains some letter or pattern then search query must be written like : cityName like '%kota%' ">
                    <i class="fa fa-question-circle fa-1x" aria-hidden="true"></i>
                    </a>
					</span>
				</div>
			</div>
		</div>
		
		<div class="clearfix"></div>
		<div class="pbottom15 mtop10">
			<button id='search' class="btn btn-grey btn-xs" tabindex="7" onclick="setSearchValue();">
				<spring:message code="btn.label.search" ></spring:message>
			</button>
			<button id='reset' class="btn btn-grey btn-xs" tabindex="8" onclick="resetSearchLookup();">
				<spring:message code="btn.label.reset" ></spring:message>
			</button>
		</div> 
		
		<!-- End Search Input-->
		
		<div class="fullwidth">
			<div class="title2">&nbsp
				<span class="title2rightfield">
					<span class="title2rightfield-icon1-text"> 
						<sec:authorize access="hasAuthority('VIEW_RULE_DATA_CONFIG')">	
							<a href="#"><i class="fa fa-download" onclick="downloadViewDataCSV();"></i></a>						
							<a href="#" id="downloadViewDataCSV" onclick="downloadViewDataCSV();">Download as CSV</a>							
						</sec:authorize>
					</span>
					<span class="title2rightfield-icon1-text"> 
						<sec:authorize access="hasAuthority('DELETE_RULE_DATA_CONFIG')">
							<a href="#"><i class="fa fa-trash" onclick="displayDeleteLookupRecordsPopup();"></i></a>
		
							<a href="#" id="deleteRuleDataRecords" onclick="displayDeleteLookupRecordsPopup();">
								<spring:message code="rule.data.mgmt.delete.link" ></spring:message>
							</a>
							<a href="#divDeleteLookupRecords" class="fancybox" style="display: none;" id="delete_lookup_records">#</a>
						</sec:authorize>
					</span>
				</span>
			</div>
			
		</div>
		
		<div class="box-body table-responsive no-padding box">
			<table class="table table-hover" id="lookupTableGrid"></table>
			<div id="lookupTableGridPagingDiv"></div>
		</div>
		
		<span class="title2rightfield"> 
			<button id='close' class="btn btn-grey btn-xs" onclick="location.href = '<%= ControllerConstants.INIT_RULE_DATA_CONFIG %>';" tabindex="10">
				<spring:message code="btn.label.close" ></spring:message>
			</button>
		</span>
		
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
						<spring:message code="rule.data.mgmt.selection.warning.label.lookup.record" ></spring:message>
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
		<div id="divDeleteLookupRecords" style="width: 100%; display: none;">
			<div class="modal-content">
				<div class="modal-header padding10">
					<h4 class="modal-title">
						<spring:message code="rule.data.mgmt.delete.lookup.records" ></spring:message>
					</h4>
				</div>
				<div class="modal-body padding10 inline-form">
					<div id="deletePopUpMsg" class=fullwidth>
						<jsp:include page="../common/responseMsgPopUp.jsp" ></jsp:include>
					</div>
					<div class="fullwidth">
						<div id="delete_selected_records_details"></div>
						<div>
							<spring:message code="lookup.table.records.delete.warning" ></spring:message>
						</div>
					</div>
				</div>
				
				<div id="delete_lookup_records_bts_div" class="modal-footer padding10">
					<button id="delete_confirm" type="button" class="btn btn-grey btn-xs"
						onclick="deleteLookupTableRecords();">
						<spring:message code="btn.label.delete" ></spring:message>
					</button>
					<button type="button" class="btn btn-grey btn-xs"
						onclick="closeFancyBox();reloadLookupTableGrid();">
						<spring:message code="btn.label.cancel" ></spring:message>
					</button>
				</div>
			
				<div id="delete_lookup_records_progress_bar_div"
					class="modal-footer padding10 text-left" style="display: none;">
					<jsp:include page="../common/processing-bar.jsp"></jsp:include>
				</div>
			</div>
		</div>
		<!-- Delete attribute popup div end here -->
				<form action="downloadviewlookuptabledata" method="POST" id="download_lookup_table_form">
					<input type="hidden" id="downloadTableId" name = "downloadTableId"/>
					<input type="hidden" id="downloadTableName" name = "downloadTableName"/>
					<input type="hidden" id="viewdataidlist" name = "viewdataidlist"/>
			   </form>
	</div>
</div>

<sec:authorize access="hasAnyAuthority('CREATE_RULE_DATA_CONFIG')" var="isCreateAuthorize"></sec:authorize>
<sec:authorize access="hasAnyAuthority('UPDATE_RULE_DATA_CONFIG')" var="isUpdateAuthorize"></sec:authorize>

<script type="text/javascript">

var tableId = ${tableId};
var tablename = '${tablename}';
var isSearchV = false ;
var searchQueStr ="";
var currentPage = "";
var selectedRows = {};

$(document).ready(function(){
	$('[data-toggle="popover"]').popover({container: "body", placement: "right"
		}).on("show.bs.popover", function() {
	    return $(this).data("bs.popover").tip().css({
	    	"max-width": "25%"
	      });
	    });
	
	createLookupTableColModels();
	
	loadRuledataDictTableRecordsGrid();
    
	if('${isCreateAuthorize}' != 'true'){
	   $("#lookupTableGrid_iladd").hide();
    }
    if('${isUpdateAuthorize}' != 'true'){
 	   $("#lookupTableGrid_iledit").hide();
    } 
    
    $("#lookupTableGrid_iledit").click(function() {
    	var rowid = $("#lookupTableGrid").jqGrid('getGridParam','selrow');
    	if (rowid == null || rowid == "") {
    		showErrorMsg("<spring:message code='lookup.table.edit.select.rows'></spring:message>");
    	}
    });
    
    $("#lookupTableGrid_ilcancel").click(function() {
    	clearAllMessages();    	
    });
});

function setSearchValue()
{	
	isSearchV = true ;
	searchQueStr = $("#rule_data_search").val();	
	clearAllMessages();
	var $grid = $("#lookupTableGrid");
	jQuery('#lookupTableGrid').jqGrid('clearGridData');
	clearLookupTableGrid();

	$grid.jqGrid('setGridParam',{datatype:'json',page: 1,sortname: 'RECORDROWID',sortorder: "desc",postData:{
		 id : tableId ,tableviewname : tablename, isSearch : isSearchV , searchQuery : searchQueStr 
		 }}).trigger('reloadGrid');
	
}

function createLookupTableColModels(){
	ruleRecordColumns = JSON.parse('${tableFieldListJson}');
	for(i = 0; i < ruleRecordColumns.length; i++){
		ruleRecordColModels[i] = {name: ruleRecordColumns[i], index: ruleRecordColumns[i], editable:true, editrules:{required:false}, sortable:true}
	}
	ruleRecordColumns.unshift("RECORDROWID");
	ruleRecordColumns.unshift("<input type='checkbox' id='selectAllLookupRecords' onclick='LookupRecordHeaderCheckbox(event, this)'></input>");
	ruleRecordColModels.unshift({name: 'RECORDROWID', index: 'RECORDROWID',hidden:true,sortable:false, key:true});
	ruleRecordColModels.unshift({name: 'checkbox', index: 'checkbox',sortable:false, formatter:lookupRecordsCheckboxFormatter, align : 'center'});
}

function loadRuledataDictTableRecordsGrid(){
	$("#lookupTableGrid").jqGrid({
		url: '<%= ControllerConstants.INIT_LOOKUP_DATA_LIST%>',
		postData : { id : tableId ,tableviewname : tablename
		},
		datatype: "json",
		colNames: ruleRecordColumns,
		colModel: ruleRecordColModels,
		rowNum:<%= MapCache.getConfigValueAsInteger(SystemParametersConstant.TOTAL_ROWS_TO_DISPLAY_IN_GRID, 10) %>,
		rowList:[5,10,20,50,100],
		height: 'auto',
		sortname: 'RECORDROWID',
		sortorder: "desc",
		pager: "#lookupTableGridPagingDiv",
		viewrecords: true,
		multiselect: false,
		timeout : 120000,
	    loadtext: "Loading...",
		caption: tablename.toUpperCase(),
		loadComplete: function(data) {
			currentPage = jQuery('#lookupTableGrid').getGridParam('page');
 			$(".ui-dialog-titlebar").show();
 			var resData = eval(data);
 			if(data.code == 400){	 				
 				showErrorMsg(data.msg);
 			}
 			var check = true;
			$.each($("input[name='lookupRecordsCheckbox']:not(:checked)"), function(){  
				check = false;
	        });
			var count = jQuery("#lookupTableGrid").jqGrid('getGridParam', 'reccount');
			if(count === 0){
				check = false;
			}
			$("#selectAllLookupRecords").prop('checked',check);
		},
		loadError : function(xhr,st,err) {
			handleGenericError(xhr,st,err);
		},
		recordtext: "<spring:message code="serverManagement.grid.pager.total.records.text"></spring:message>",
	    emptyrecords: "<spring:message code="collectionService.summary.agent.grid.empty.records"></spring:message>",
		loadtext: "<spring:message code="collectionService.summary.agent.grid.loading.text"></spring:message>",
		pgtext : "<spring:message code="collectionService.summary.agent.grid.pager.text"></spring:message>",
		onPaging : function (){
			clearAllMessages();
			$("#lookupTableGrid_ilcancel").click();
		},ondblClickRow: function (rowid) {
			if('${isUpdateAuthorize}' === 'true'){
				clearAllMessages();
		        $(this).jqGrid('editRow', rowid);
		        $("#lookupTableGrid_iledit").click();
			}
	    }
	}).navGrid("#lookupTableGridPagingDiv",{edit:false,add:false,del:false,search:false,refresh:true});
	$(".ui-jqgrid-titlebar").hide();
	$(".ui-pg-input").height("10px");
	$("#lookupTableGrid").jqGrid('inlineNav',"#lookupTableGridPagingDiv",{
        addtext: "Add",
        edittext: "Edit",
        savetext: "Save",
        canceltext: "Cancel",                       
        addParams: { 
            addRowParams: myEditOptions
        },
        //addedrow:"last",
        editParams: myEditOptions, 
    });
	$.extend($.jgrid.inlineEdit, { restoreAfterError: false });
}

var myEditOptions = {
    keys: true,
    //url: /* "clientArray" */"createRuleLookupRecord",
    extraparam: {
    	lookupRecord: function () {
    		var lookupRec = {};
    		var rowid = $("#lookupTableGrid").jqGrid('getGridParam','selrow');
    		for(i = 1; i <= ruleRecordColumns.length; i++){
    			lookupRec[ruleRecordColumns[i]] = $("#"+rowid+"_"+ruleRecordColumns[i]).val();
    		}
        	return JSON.stringify(lookupRec);
        },
        viewName: function () {
			return tablename;
		},
		tableId: function () {
			return tableId;
		}
    },
    oneditfunc: function (rowid) {
    	clearAllMessages();
    },
    beforeSaveRow: function (options, rowid) {
    	
    	if (!checkIfUniqueEmpty()) {
			showErrorMsg("<spring:message code='lookup.table.unique.column.empty'></spring:message>");
			return false;
		}
    	
    	if(options.extraparam.oper === "add"){
    		currentPage = 1;
    		options.url = '<%= ControllerConstants.CREATE_RULE_LOOKUP_RECORD%>';
    	} else if(options.extraparam.oper === "edit"){
    		options.url = '<%= ControllerConstants.UPDATE_RULE_LOOKUP_RECORD%>';
    	}
    },
    successfunc: function (res){
    	res = JSON.parse(res.responseText);
    	if(res.code === "200"){
    		showSuccessMsg(res.msg);
    		reloadLookupTableGrid();
    	} else if(res.code === "400"){
    		showErrorMsg(res.msg);
    	}
    }
};

function reloadLookupTableGrid(){
	//clearAllMessages();
	var $grid = $("#lookupTableGrid");
	jQuery('#lookupTableGrid').jqGrid('clearGridData');
	clearLookupTableGrid();

	$grid.jqGrid('setGridParam',{datatype:'json',page: 1,sortname: 'RECORDROWID',sortorder: "desc",postData:{
		 id : tableId ,tableviewname : tablename, isSearch : true , searchQuery : searchQueStr 
		 }}).trigger('reloadGrid');
}

function clearLookupTableGrid(){
	$grid = $("#lookupTableGrid");
	var rowIds = $grid.jqGrid('getDataIDs');
	// iterate through the rows and delete each of them
	for(var i=0,len=rowIds.length;i<len;i++){
		var currRow = rowIds[i];
		$grid.jqGrid('delRowData', currRow);
	}
}

function lookupRecordsCheckboxFormatter(cellvalue, options, rowObject){
	if ($.inArray( rowObject.RECORDROWID ,  ckSelectedLookupRecords ) != -1){
		return '<input type="checkbox" id="checkbox_'+rowObject["RECORDROWID"]+'" name="lookupRecordsCheckbox" value="'+rowObject["RECORDROWID"]+'" onchange="updateRootCheckbox(this, \'lookupTableGrid\');getSelectedLookupRecordList('+rowObject.RECORDROWID+');" checked></len;i++){>';
	} else {
		return '<input type="checkbox" id="checkbox_'+rowObject["RECORDROWID"]+'"  name="lookupRecordsCheckbox" value="'+rowObject["RECORDROWID"]+'" onchange="updateRootCheckbox(this, \'lookupTableGrid\');getSelectedLookupRecordList('+rowObject.RECORDROWID+');"/>';
	}
}

function getSelectedLookupRecordList(rowId){
	if( document.getElementById("checkbox_"+rowId).checked === true){
		if(ckSelectedLookupRecords.indexOf(rowId) === -1){
			ckSelectedLookupRecords.push(rowId);
			deletableRowData(rowId);
		}
	}else{
		if(ckSelectedLookupRecords.indexOf(rowId) !== -1){
			ckSelectedLookupRecords.splice(ckSelectedLookupRecords.indexOf(rowId), 1);
			delete selectedRows[rowId];
		}
	}
}

function displayDeleteLookupRecordsPopup(){	
	clearAllMessagesPopUp();
	resetWarningDisplayPopUp();
	clearAllMessages();
	
	if(ckSelectedLookupRecords.length === 0){
		$("#warn").show();
		$("#delete_warn_msg_link").click();
		return;
	}else{
		var tableString ="<table class='table table-hover table-bordered'  border='1' ><th>#</th>";
		for(i = 2; i < 4; i++){
			tableString+="<th>"+ruleRecordColumns[i]+"</th>";
		}
		
		$.each(selectedRows, function( rowId, deleteRecord ) {
			tableString += "<tr>";
			tableString += "<td><input type='checkbox' name='table_delete' id='record_"+rowId+"' checked  onclick=getSelectedRecord('"+rowId+"')  value='"+rowId+"' /> </td>";
			for(i = 2; i < 4; i++){
				tableString += "<td>"+deleteRecord[ruleRecordColumns[i]]+"</td>";
			}
			tableString += "</tr>";
			});	
		
		tableString+="</table>";
		
		$("#delete_selected_records_details").html(tableString);
		$("#delete_lookup_records_bts_div").show();
		$("#delete_lookup_records_progress_bar_div").hide();
		$("#delete_lookup_records").click();
	}
}

function deleteLookupTableRecords(){
	var count = jQuery("#lookupTableGrid").jqGrid('getGridParam', 'reccount');
	if(ckSelectedLookupRecords.length >= count){
		currentPage = 1;
	}
	$("#delete_lookup_records_bts_div").hide();
	$("#delete_lookup_records_progress_bar_div").show();
	$.ajax({
		url: '<%= ControllerConstants.DELETE_RULE_LOOKUP_TABLE_RECORDS%>',
		cache: false,
		async: true,
		dataType:'json',
		type:'POST',
		data:
		 {
			recordIds : ckSelectedLookupRecords.toString(),
			viewName  : tablename
		 }, 
		success: function(data){
			var response = eval(data);
			var responseCode = response.code; 
			var responseMsg = response.msg; 
		
			clearAllMessagesPopUp();
			resetWarningDisplayPopUp();
			 
			if(responseCode === "200"){
				$("#delete_lookup_records_bts_div").hide();
				$("#delete_lookup_records_progress_bar_div").hide();
				reloadLookupTableGrid();
				showSuccessMsg(responseMsg);
				closeFancyBox();
				ckSelectedLookupRecords = new Array();
				selectedRows = {};
			}else{
				showErrorMsgPopUp(responseMsg);
				$("#delete_lookup_records_bts_div").show();
				$("#delete_lookup_records_progress_bar_div").hide();
			}
		},
	    error: function (xhr,st,err){
			handleGenericError(xhr,st,err);
		}
	});
}

function LookupRecordHeaderCheckbox(e, element) {
    e = e || event;/* get IE event ( not passed ) */
    e.stopPropagation ? e.stopPropagation() : e.cancelBubble = false;

    var rowId;
    //if root checkbox is checked :-> all child checkbox should ne checked
    if(element.checked) {
    	$('input:checkbox[name="lookupRecordsCheckbox"]').prop('checked',true);
    	$.each($("input[name='lookupRecordsCheckbox']:checked"), function(){
    		rowId = Number($(this).val()); 
    		if(ckSelectedLookupRecords.indexOf(rowId) === -1){
        		ckSelectedLookupRecords.push(rowId);
        		deletableRowData(rowId);
    		}
        });
    } else {
    	//if root checkbox is unchecked :-> all child checkbox should be unchecked
    	$('input:checkbox[name="lookupRecordsCheckbox"]').prop('checked',false);
    	$.each($("input[name='lookupRecordsCheckbox']:not(:checked)"), function(){  
    		rowId = Number($(this).val()); 
    		if(ckSelectedLookupRecords.indexOf(rowId) !== -1){
    			ckSelectedLookupRecords.splice(ckSelectedLookupRecords.indexOf(rowId), 1);
    			delete selectedRows[rowId];
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
		$('input:checkbox[id="selectAllLookupRecords"]').prop('checked',false);
	} else {
		//if current child checkboox is checked and all checkbox are checked then check root checkbox
		var count = jQuery("#"+gridId).jqGrid('getGridParam', 'reccount');
		if ($('input:checkbox[name="lookupRecordsCheckbox"]:checked').length == count) {
			$('input:checkbox[id="selectAllLookupRecords"]').prop('checked',true);
	    }
	}
}

function deletableRowData(rowId){
	var deleteRecord = {};
	var rowData = jQuery('#lookupTableGrid').jqGrid ('getRowData', rowId);
	for(i = 2; i < 4; i++){
		deleteRecord[ruleRecordColumns[i]] = rowData[ruleRecordColumns[i]];
	}
	selectedRows[rowId] = deleteRecord;
}

function getSelectedRecord(rowId){
	rowId = Number(rowId);
	if( document.getElementById("record_"+rowId).checked === true){
		if(ckSelectedLookupRecords.indexOf(rowId) === -1){
			ckSelectedLookupRecords.push(rowId);
			deletableRowData(rowId);
		}
	}else{
		if(ckSelectedLookupRecords.indexOf(rowId) !== -1){
			ckSelectedLookupRecords.splice(ckSelectedLookupRecords.indexOf(rowId), 1);
			delete selectedRows[rowId];
		}
	}
}

function downloadViewDataCSV(){
	 
	$("#downloadTableId").val( tableId );
	$("#downloadTableName").val( tablename );
	$("#viewdataidlist").val( searchQueStr );
	$("#download_lookup_table_form").submit();
}

function resetSearchLookup(){
	clearAllMessagesPopUp();
	resetWarningDisplayPopUp();
	clearAllMessages();
	$("#rule_data_search").val("");
	searchQueStr = "";
	reloadLookupTableGrid();
}

function checkIfUniqueEmpty()
{
	var flag = false;
	var uniqueFields = '${uniqueFields}';
	var rowid = $("#lookupTableGrid").jqGrid('getGridParam','selrow');

	$.each(uniqueFields.split(','), function(){
		var data = $("#"+rowid+"_"+ this.toUpperCase()).val();
		if((data != null) &&  (data.trim() != "") && (data != 'undefined')) {
			flag = true;
		}
	})
	return flag;
}

</script>
