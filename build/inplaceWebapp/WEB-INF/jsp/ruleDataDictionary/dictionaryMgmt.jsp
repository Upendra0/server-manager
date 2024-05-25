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
				<spring:message code="dictionary.data.mgmt.search.title" ></spring:message>
			</div>
		</div>
	<div class="col-md-6 inline-form"
			style="padding-left: 0px !important;">
			<div class="form-group">
				<div class="table-cell-label">
					<spring:message code="service.wise.summary.selectServer" ></spring:message>
				</div>
				<div class="input-group ">
             		<spring:message code="service.wise.summary.selectServieType" var="selectType" ></spring:message>
             		<spring:message code="dictionary.serverlist.runningUtilityPort"
										var="tooltip" ></spring:message>
             		<select id="selServerInstance" class='form-control table-cell input-sm'  data-toggle='tooltip' data-placement='bottom' title='${tooltip }' onChange='profileServiceList();'>
					 <c:forEach items="${SERVER_LIST}" var="serverList">
						<optgroup label="${serverList.key}">
						<c:forEach items="${serverList.value}" var="server">
							<option value="${server.ipAddress}:${server.utilityPort}">${server.name} : ${server.ipAddress} : ${server.utilityPort} </option>
						 </c:forEach>
						</optgroup>
						</c:forEach>
						<option value="-1" selected="selected"><spring:message code="serviceManager.create.service.allServer.option"></spring:message></option>
			       </select>
             		<span class="input-group-addon add-on last" > <i id="search_service_type_error" class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="left" title=""></i></span>
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
		
		<a href="#divuploadAttrData" class="fancybox" style="display: none;" id="uploadAttrData">#</a>
 		<div id="divuploadAttrData" style=" width:100%; display: none;" >
 		  <jsp:include page="addDictionaryFilePopup.jsp"></jsp:include>
       </div>
		
		    <div id="AddButtonDivId">
            <button id='search' style="float:right;"class="btn btn-grey btn-xs" onclick="addDictData();">
				<spring:message code="btn.label.add" ></spring:message>
			</button>
			</div>
		
		
	<div class="fullwidth">
		<div class="title2">
			<spring:message code="dictionary.data.mgmt.file.list" ></spring:message>
		</div>
	
	<a href="#divUploadLookupTableData" class="fancybox" style="display: none;" id="uploadLookupTableData">#</a>
 		<div id="divUploadLookupTableData" style=" width:100%; display: none;" >
 			<jsp:include page="uploadFilePopup.jsp"></jsp:include>
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
	
	<form action="<%= ControllerConstants.INIT_DICTIOANRY_CONFIG %>" method="GET" id="loadRuleDataDictionary">
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
	
	<form action="<%=ControllerConstants.DOWNLOAD_DICTIONARY_CONFIG_XML%>"
    	method="POST" id="downloadDictionaryConfigXML">
	   <input type="hidden" id="did" name="did" />
    </form>
	
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

<sec:authorize access="hasAnyAuthority('VIEW_DICTIONARY_CONFIG')" var="isUpdateAuthorize"></sec:authorize>

<script>
function addDictData(){
	document.getElementById("dataFile1").value = '';
	$(":file").filestyle('clear');
	$("#fileUploadPath").val($("#fileUploadPath option:first").val());
	$("#uploadAttrData").click();
}
$(document).ready(function() {
	loadRuledataDictGrid(false);
	var selectBoxVal=$("#selServerInstance").val();
	if(selectBoxVal=='-1'){
		$("#AddButtonDivId").hide();
	}else{
		$("#AddButtonDivId").show();
	}
	reloadTableGridData();
});

function resetSearchRuleCriteria(){
	$("#selServerInstance").val("-1");
}
function loadRuledataDictGrid(isSearch){
	$("#tableGrid").jqGrid({
		url: "<%= ControllerConstants.INIT_DICTIONARY_LIST%>",
		postData : {
			ipAddress: function () {
				var str=$('#selServerInstance').val();
				if(str!='-1'){
				var ipAddress=str.substring(0, str.indexOf(":"));
				if(ipAddress!=undefined || ipAddress!='-1')
				    return ipAddress;
				}
				else
					return "0";
    		},
    		utilityPort: function () {
    			var str=$('#selServerInstance').val();
    			if(str!='-1'){
    			var utilityPort=str.substring(str.indexOf(":")+1,str.length);
    			if(utilityPort!=undefined || utilityPort!='-1')
				    return utilityPort;
    			}
				else
					return "0";
    		},
    		'isSearch':isSearch
		},
		datatype: "json",
		colNames: [
					  "#",
					  "<spring:message code='dictionary.data.mgmt.file.path' ></spring:message>",
		           	  "<spring:message code='dictionary.data.mgmt.file.name' ></spring:message>",
		           	  "<spring:message code='rule.data.mgmt.download' ></spring:message>",
		           	<% if(Boolean.TRUE.toString().equals((String)MapCache.getConfigValueAsObject(BaseConstants.KUBERNETES_ENV))){  %> "<spring:message code='dictionary.data.mgmt.upload.label' ></spring:message>" <%}else{ %> "<spring:message code='dictionary.data.mgmt.upload' ></spring:message>" <%} %>
		],
		colModel: [
			{name:'id',index:'id',hidden:true},
			{name:'path',index:'path',sortable:false},
			{name:'filename',index:'filename',sortable:false},
			{name:'download',index:'download',align:'center',formatter:downloadFormatter,sortable:false},
			{name:'upload',index:'upload',align:'center',formatter:uploadFormatter,sortable:false}
		],
		rowNum:<%= MapCache.getConfigValueAsInteger(SystemParametersConstant.TOTAL_ROWS_TO_DISPLAY_IN_GRID, 100) %>,
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
	return '<a class="link" href="#" onclick="downloadDictionaryConfigXML('+"'" + rowObject["id"]+ "'"+')"><i class="fa fa-download"></i></a>'
}
function uploadFormatter(cellvalue, options, rowObject) {
	    var id=rowObject.viewName+"_upload";
		return '<a id="'+id+'" href="#" onclick="uploadPopUp('+"'" + rowObject["id"]+ "'"+');"><i class="fa fa-upload"></i></a>';
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
function reloadTableGridData(isSearch){
	clearAllMessages();
	var selectBoxVal=$("#selServerInstance").val();
	if(selectBoxVal=='-1'){
		$("#AddButtonDivId").hide();
	}else{
		$("#AddButtonDivId").show();
	}
	var $grid = $("#tableGrid");
	jQuery('#tableGrid').jqGrid('clearGridData');
	clearTableGrid();
	$grid.jqGrid('setGridParam',{datatype:'json',page: 1,sortname: 'id',sortorder: "desc",	postData : {
		ipAddress: function () {
			var str=$('#selServerInstance').val();
			if(str!='-1'){
			var ipAddress=str.substring(0, str.indexOf(":"));
				if(ipAddress!=undefined || ipAddress!='-1'){
				    return ipAddress;
				}
			}
			else
				return "0";
		},
		utilityPort: function () {
			var str=$('#selServerInstance').val();
			if(str!='-1'){
			var utilityPort=str.substring(str.indexOf(":")+1,str.length);
				if(utilityPort!=undefined || utilityPort!='-1'){
				    return utilityPort;
				}
			}
			else
				return "0";
		},
		'isSearch':isSearch
	}}).trigger('reloadGrid');
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
function downloadDictionaryConfigXML(id){
	$("#did").val(id);
	$("#downloadDictionaryConfigXML").submit();
}


function loadRuleDataDictionary(){
	$("#loadRuleDataDictionary").submit();
}

</script>
