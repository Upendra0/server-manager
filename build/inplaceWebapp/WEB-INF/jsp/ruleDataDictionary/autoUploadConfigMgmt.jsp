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
<script type="text/javascript" src="${pageContext.request.contextPath}/js/multiple-select.js"></script>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/multiple-select.css" type="text/css"/>
<script>
$(document).ready(function(){
	      getTableList();
});
</script>
<style>
.ms-drop ul {
    height : 90px !important;
}
.ms-drop input[type=radio]{
    display:none;
}
</style>
<script>
    $(function() {
        $('#autoSuggestTableList').change(function() {
        }).multipleSelect({
            width: '94%',
            single: true,
            filter: true,
            enableCaseInsensitiveFiltering: true,
            hideDisabled:true
        });
    });
</script>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<div class="tab-content no-padding clearfix" id="rule-data-configuration-block">
	<div class="fullwidth mtop10">
		<div class="fullwidth">
			<div class="title2">
				<spring:message code="auto.upload.config.search.title" ></spring:message>
			</div>
		</div>

		<div class="col-md-6 inline-form"
			style="padding-left: 0px !important;">
			<div class="form-group">
				<div class="table-cell-label">
					<spring:message code="rule.data.mgmt.source.dir" ></spring:message>
				</div>
				<div class="input-group">
					<spring:message code="rule.data.mgmt.source.dir" var="tooltip" ></spring:message>
					<input type="text" id="ruleDataSourceDir"
						class="form-control table-cell input-sm" data-toggle="tooltip"
						tabindex="1" data-placement="bottom" title="${tooltip}"
						style="width: 94%;">
					<!-- <a href='#'><i style='margin-top: 8px;margin-left: 8px;' class='glyphicon glyphicon-search'></i></a> -->
					<span class="input-group-addon add-on last"> <i
						class="glyphicon glyphicon-alert" data-toggle="tooltip"
						data-placement="bottom"></i>
					</span>
				</div>
			</div>
		</div>

		<div class="col-md-6 inline-form"
			style="padding-left: 0px !important;">
			<div class="form-group">
				<div class="table-cell-label">
					<spring:message code="rule.data.mgmt.table.name" ></spring:message>
				</div>
				<select id='autoSuggestTableList' data-placement='bottom'>
						<option value="-1">Start typing or Select (All)</option>
				</select>
				<span  class="input-group-addon add-on last"> 
					<i class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="bottom"></i>
				</span>
			</div>
		</div>
	</div>

	<div class="col-md-6 inline-form"
			style="padding-left: 0px !important;">
			<div class="form-group">
				<div class="table-cell-label">
					<spring:message code="rule.data.mgmt.scheduler" ></spring:message>
				</div>
				<div class="input-group">
					<spring:message code="rule.data.mgmt.scheduler" var="tooltip" ></spring:message>
					<input type="hidden" name="jobSchedulerNm" id="jobSchedulerID" value="-1" />
					<input type="text" id="ruleDataScheduler" class="form-control table-cell input-sm" data-toggle="tooltip" tabindex="1" data-placement="bottom" title="${tooltip}" style="width:94%;" readonly="readonly" /> 
					<a href='#' onclick="selectSchedulerNamePopUp1();"><i style='margin-top: 8px;margin-left: 8px;' class='glyphicon glyphicon-search'></i></a>
					<span  class="input-group-addon add-on last"> 
						<i class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="bottom"></i>
					</span>
				</div>
			</div>
		</div>
	
	<div class="clearfix"></div>
		<div class="pbottom15 mtop10">
			<button id='search' class="btn btn-grey btn-xs" onclick="searchAutoReloadGridData('true');"
				tabindex="5">
				<spring:message code="btn.label.search" ></spring:message>
			</button>
			<button class="btn btn-grey btn-xs"
				onclick="resetSearchRuleCriteria();searchAutoReloadGridData('true');" tabindex="6">
				<spring:message code="btn.label.reset" ></spring:message>
			</button>
		</div>
		
	<div class="fullwidth">
		<div class="title2">
			<spring:message code="auto.upload.config.list" ></spring:message>
			<span class="title2rightfield"> 
				<span
					class="title2rightfield-icon1-text"> <sec:authorize
							access="hasAuthority('VIEW_AUTO_UPLOAD_CONFIGURATION')">
							<a href="#" onclick="createAutoUpload(); "><i
								class="fa fa-plus-circle"></i></a>
	
							<a href="#" id="createAutoUpload" onclick="createAutoUpload();">
								<spring:message code="rule.data.mgmt.add" ></spring:message>
							</a>
	
						</sec:authorize>
				</span>
				<span class="title2rightfield-icon1-text"> 
				<sec:authorize access="hasAuthority('VIEW_AUTO_UPLOAD_CONFIGURATION')">
					<a href="#"><i class="fa fa-trash" onclick="displayDeleteAutoUploadJobPopup();"></i></a>

					<a href="#" id="deleteRuleDataTable" onclick="displayDeleteAutoUploadJobPopup();">
						<spring:message code="rule.data.mgmt.delete.link" ></spring:message>
					</a>
					<a href="#divDeleteLookupTable" class="fancybox" style="display: none;" id="delete_lookup_Tables">#</a>
				</sec:authorize>
			</span>
				
			</span>
		</div>
	
	<%-- <a href="#divUploadLookupTableData" class="fancybox" style="display: none;" id="uploadLookupTableData">#</a>
 		<div id="divUploadLookupTableData" style=" width:100%; display: none;" >
 			<jsp:include page="uploadRuleLookupData.jsp"></jsp:include>
	</div>

	<a href="#divViewFieldList" class="fancybox" style="display: none;" id="viewFieldList">#</a>
 		<div id="divViewFieldList" style=" width:100%; display: none;" >
 			<jsp:include page="viewFieldListPopUp.jsp"></jsp:include>
	</div>--%>
	<a href="#divDeleteLookupTable" class="fancybox" style="display: none;" id="delete_lookup_table">#</a>
	</div>
	
	<a href="#divAddAutoUpload" class="fancybox" style="display: none;" id="divAddAutoUploadPopUp">#</a>
	
	<div id="divAddAutoUpload" style=" width:100%; display: none;" >
		<jsp:include page="autoUploadConfigPopUp.jsp"></jsp:include>
	</div> 
		
	<div class="box-body table-responsive no-padding box">
		<table class="table table-hover" id="autoUploadGrid"></table>
		<div id="autoUploadGridPagingDiv"></div>
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
					<spring:message code="auto.upload.selection.warning" ></spring:message>
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
	
	<!-- Delete Popup code started -->
	<a href="#divDeleteLookupTable" class="fancybox" style="display: none;" id="delete_lookup_table">#</a>
	<div id="divDeleteLookupTable" style="display: none;">	
	<div class="modal-content">
					<div class="modal-header padding10">
						<h4 class="modal-title">
							<span id="delete_label" style="display: none;">
							Delete Auto Upload Configuration
							<%-- <spring:message code="auto.reload.cache.delete" ></spring:message> --%>
									</span>
								</h4>
					</div>	
			
 		<div id="divDeleteTablePopup" style="width: 100%; display: none;">
			<div class="modal-content">
				<div class="modal-body padding10 inline-form">
					<div class="fullwidth">
						<input type="hidden" value="" id="deleteAutoReloadCacheId"
							name="deleteAutoReloadCacheId" />
						<div id="delete_selected_table_details"></div>
						<div id="delete_warning" style="display: none;">
							<spring:message code="auto.upload.config.delete.warning" ></spring:message>
						</div>
					</div>
				</div>
					<div id="delete_lookup_Table_bts_div" class="modal-footer padding10">
						<sec:authorize access="hasAuthority('VIEW_AUTO_UPLOAD_CONFIGURATION')">
							<button id='delete' type="button" class="btn btn-grey btn-xs"
								onclick="deleteAutoReloadCacheConfig();">
								<spring:message code="btn.label.delete" ></spring:message>
							</button>
						</sec:authorize>
						<button type="button" class="btn btn-grey btn-xs"
							onclick="closeFancyBox();reloadAutoReloadGridData();">
							<spring:message code="btn.label.cancel" ></spring:message>
						</button>
					</div>
				
				<div id="delete_lookup_Table_progress_bar_div"
					class="modal-footer padding10 text-left" style="display: none;">
					<jsp:include page="../common/processing-bar.jsp"></jsp:include>
				</div>
			</div>
		</div>
		
		</div>
		</div>
		<!-- Delete attribute popup div end here -->
</div>
</div>
</div>

<!-- Start Schedule List -->
<a href="#divSchedulerList1" class="fancybox" style="display: none;" id="schedulerList1">#</a>
<div id="divSchedulerList1" style=" width:100%; display:none;" >
    <div class="modal-content" style="overflow:hidden">
		<div class="modal-header padding10">
			<h4 class="modal-title">
				<i class="icon-hdd"></i> 
				<%-- <spring:message code="device.tab.heading.label" ></spring:message> --%> Scheduler List
			</h4>
		</div>
		<div id="deviceContentDiv1" class="modal-body" >
			
			<div>
				<label id="lblCounterForDeviceName1" style="display:none;"></label>
				<label id="lblDeviceid1" style="display:none;"></label>
				<span class="title2rightfield">
				 	<span class="title2rightfield-icon1-text" style="font-size: 12px;"> 
				 		<!-- <a href="#" onclick="createPolicy();"  ><spring:message code="business.policy.create.label" ></spring:message></a>  --> 
					</span>
				</span>
			</div>
			<div class="fullwidth table-responsive " style="height:auto;max-height:300px;overflow: auto;">
				<table class="table table-hover" id="schedulerlistgrid1"></table>
				<div id="schedulerGridPagingDiv1"></div>
			</div>
		
		</div>
		<div id="buttons-div" class="modal-footer padding10 text-left">
			<div id="buttons-div" class="modal-footer padding10 text-left">
				<button type="button" class="btn btn-grey btn-xs " id="selectSchedulerbtn1">
					<spring:message code="btn.label.select" ></spring:message>
				</button>
				<button onClick="closeFancyBoxFromChildIFrame();" class="btn btn-grey btn-xs ">
					<spring:message code="btn.label.cancel" ></spring:message>
				</button>
			</div>
		</div>
	</div>
			<!-- /.modal-content -->
</div>


<!-- div for get table list by search start-->		
<a href="#divTableList1" class="fancybox" style="display: none;" id="tableList1">#</a>
<div id="divTableList1" style=" width:100%; display:none;" >
    <div class="modal-content" style="overflow:hidden">
		<div class="modal-header padding10">
			<h4 class="modal-title">
				<i class="icon-hdd"></i> 
				<%-- <spring:message code="device.tab.heading.label" ></spring:message> --%> Table List
			</h4>
		</div>
		<div id="deviceContentDiv1" class="modal-body" >
			
			<div>
				<label id="lblCounterForDeviceName1" style="display:none;"></label>
				<label id="lblDeviceid1" style="display:none;"></label>
				<span class="title2rightfield">
				 	<span class="title2rightfield-icon1-text" style="font-size: 12px;"> 
				 		<!-- <a href="#" onclick="createPolicy();"  ><spring:message code="business.policy.create.label" ></spring:message></a>  --> 
					</span>
				</span>
			</div>
			<div class="fullwidth table-responsive " style="height:auto;max-height:300px;overflow: auto;">
				<table class="table table-hover" id="tablelistgrid1"></table>
				<div id="tableListGridPagingDiv1"></div>
			</div>
		
		</div>
		<div id="buttons-div" class="modal-footer padding10 text-left">
			<div id="buttons-div" class="modal-footer padding10 text-left">
				<button type="button" class="btn btn-grey btn-xs " id ="selectTablebtn1">
					<spring:message code="btn.label.select" ></spring:message>
				</button>
				<button onClick="closeFancyBoxFromChildIFrame();" class="btn btn-grey btn-xs ">
					<spring:message code="btn.label.cancel" ></spring:message>
				</button>
			</div>
		</div>
	</div>
			<!-- /.modal-content -->
</div>
<!-- div for get table list by search end-->
<script>
var deletableRows = {};
var ckSelectedAutoUploadJobs = [];
$(document).ready(function() {
	//loadRuledataDictGrid(false);
	//reloadAutoReloadGridData();
	//var response,responseObject;
	loadRuledataDictGrid();
});
function getTableList() {
	$.ajax({
		url: 'getRuleLookUpTableTableList',
		cache: false,
		async: false,
		dataType: 'json',
		type:'POST',
		success: function(data){
			 var response = eval(data);
		     var responseObject = response.object;
			for (i = 0; i < responseObject.length; i++) { 
				$('#autoSuggestTableList').append("<option value='" + responseObject[i].id+ "'>" + responseObject[i].viewName+ "</option>");
				$('#ruleLookupTableData').append("<option value='" + responseObject[i].id+ "'>" + responseObject[i].viewName+ "</option>");
			}
		},
		error: function (xhr,st,err){
			handleGenericError(xhr,st,err);
		}
	});
}
function againUploadPopUp(){
	$("#divAddAutoUploadPopUp").click();
}

function createAutoUpload(){
	clearAllMessages();
	resetWarningDisplayPopUp();
	//$("#update_label").hide();
	//getTableList();
	$("#add_label").show();
	$("#updateLookupTable").hide();
	$("#addNewLookupTable").show();
	
	
	$("#id").val(0);
	$("#tableId").val(0);
	$('#sourceDirectory').val('');	
	$("#action option:contains(" + 'append' + ")").attr('selected', 'selected');
	$('#filePrefix').val('');
	$('#fileContains').val('');
	$('#schedulerId').val(0);
	$("#ruleLookupTableData").multipleSelect("setSelects",[-1]);
	$("#scheduler").val('');
	$("#job").val('');
	$("#divAddAutoUploadPopUp").click();
}
 
function loadRuledataDictGrid(){
	$("#autoUploadGrid").jqGrid({
		url: "<%= ControllerConstants.INIT_AUTO_UPLOAD_CONFIG_LIST%>" + ".html",
		postData : {},
		datatype: "json",
		colNames: [
		            "<input type='checkbox' id='selectAllUploadJob' onclick='AutoUploadJobTableHeaderCheckbox(event, this)'></input>",
					"<spring:message code='rule.data.mgmt.table.id'></spring:message>",
					"<spring:message code='rule.data.mgmt.source.dir'></spring:message>",
					"<spring:message code='rule.data.mgmt.table.name'></spring:message>",					
					"<spring:message code='rule.data.mgmt.scheduler.name'></spring:message>",
					"LastRunTime",
					"NextRunTime",
					"<spring:message code='rule.data.mgmt.edit'></spring:message>",
					"<spring:message code='business.policy.rule.group.grid.column.delete' ></spring:message>",
					"#","#","#","#","#","#"
		],
		colModel: [		
					{name: 'checkbox', index: 'checkbox',sortable:false, formatter:checkBoxFormatter, align : 'center', width: 30},
					{name:'id',index:'id',align:'center'},
					{name:'sourceDirectory',index:'sourceDirectory',align:'center',sortable: false},
					{name:'tableName',index:'tableName',align:'center',sortable: false},
					{name:'scheduleName',index:'scheduleName',align:'center',sortable:false},
					{name:'lastRunTime',index:'lastRunTime',align:'center',sortable:false},
					{name:'nextRunTime',index:'nextRunTime',align:'center',sortable:false},
					{name:'edit',index:'edit',align:'center',formatter:editFormatter,sortable:false},
					{name:'delete',index:'delete',align:'center', formatter:deleteFormatter,sortable: false},
					{name:'lookuptableId',index:'lookuptableId',hidden:true},
					{name:'action',index:'action',hidden:true},
					{name:'filePrefix',index:'filePrefix',hidden:true},
					{name:'fileContains',index:'fileContains',hidden:true},
					{name:'triggerId',index:'triggerId',hidden:true},
					{name:'jobId',index:'jobId',hidden:true}
		],
		rowNum:<%= MapCache.getConfigValueAsInteger(SystemParametersConstant.TOTAL_ROWS_TO_DISPLAY_IN_GRID, 10) %>,
		rowList:[5,10,20,50,100],
		height: 'auto',
		sortname: 'id',
		sortorder: "desc",
		pager: "#autoUploadGridPagingDiv",
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
				var count = jQuery("#autoReloadGrid").jqGrid('getGridParam', 'reccount');
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
	}).navGrid("#autoUploadGridPagingDiv",{edit:false,add:false,del:false,search:false});
	$(".ui-jqgrid-titlebar").hide();
	$(".ui-pg-input").height("10px");
}

function editFormatter(cellvalue, options, rowObject){
	var id=rowObject.id+"_edit";
	return '<a id="'+id+'" class="link" onclick="updateAutoUpload('+"'" + rowObject["id"]+ "'" +','+"'" + cellvalue+ "'" + ');">' + "<i class='fa fa-pencil-square-o' aria-hidden='true'></i></a>";
}

function autoReloadCheckboxFormatter(cellvalue, options, rowObject){
		 return '<input type="checkbox" id="checkbox_'+rowObject["id"]+'"  name="autoReloadCheckbox" value="'+rowObject["id"]+'" />';
		
}

function checkBoxFormatter(cellvalue, options, rowObject) {
	if ($.inArray( rowObject.id ,  ckSelectedAutoUploadJobs ) != -1){
		return '<input type="checkbox" id="checkbox_'+rowObject["id"]+'" name="uploadCheckbox" value="'+rowObject["id"]+'" onchange="updateRootCheckbox(this, \'autoUploadGrid\');getSelectedAutoUploadJobList('+rowObject.id+');" checked/>'; 
	} else {
		return '<input type="checkbox" id="checkbox_'+rowObject["id"]+'" name="uploadCheckbox" value="'+rowObject["id"]+'" onchange="updateRootCheckbox(this, \'autoUploadGrid\');getSelectedAutoUploadJobList('+rowObject.id+');"/>';
	}
}


function deleteFormatter(cellvalue, options, rowObject){
	var id=rowObject.id+"_deletelookup";
	return '<a id="'+id+'" href="#" onclick="deleteAutoReloadCache('+"'" + rowObject["id"]+ "'"+');"><i class="fa fa-trash"></i></a>';
}

function reloadAutoReloadGridData(){
	clearAllMessages();
	var isSearch= true ;
	var $grid = $("#autoUploadGrid");
	jQuery('#autoUploadGrid').jqGrid('clearGridData');
	ckSelectedAutoUploadJobs = [];
	deletableRows = {};
	$("#selectAllUploadJob").prop('checked',false);
	//clearAutoReloadGrid();

	 $grid.jqGrid('setGridParam',{datatype:'json',page: 1,sortname: 'id',sortorder: "desc",postData:{}}).trigger('reloadGrid');
}
function searchAutoReloadGridData( isSearch ){
	clearAllMessages();
	
	var $grid = $("#autoUploadGrid");
	jQuery('#autoUploadGrid').jqGrid('clearGridData');
	//clearAutoReloadGrid();

	$grid.jqGrid('setGridParam',{datatype:'json',page: 1,sortname: 'id',sortorder: "desc",postData:{
		'searchSourceDir': function () {
	        return $("#ruleDataSourceDir").val();
   		},
   		'searchTableName': function () {
	        return $("#autoSuggestTableList").val();
   		},
   		'searchScheduler': function () {
	        return $("#jobSchedulerID").val();
   		},
   		'isSearch':isSearch}}).trigger('reloadGrid');
	
	// $grid.jqGrid('setGridParam',{datatype:'json',page: 1,sortname: 'id',sortorder: "desc",postData:{}}).trigger('reloadGrid');
}
function updateAutoUpload(Id,cellvalue){
	clearAllMessages();
	resetWarningDisplayPopUp();
	var responseObject = jQuery("#autoUploadGrid").jqGrid('getRowData', Id);
	 console.log(responseObject);
	$("#add_label").hide();
	$("#updateLookupTable").show();
	$("#addNewLookupTable").hide();
	
	$("#id").val(responseObject.id);
	$("#tableId").val(responseObject.lookuptableId);
	$('#sourceDirectory').val(responseObject.sourceDirectory);	
	
	$('#filePrefix').val(responseObject.filePrefix);
	$('#fileContains').val(responseObject.fileContains);
	$('#schedulerId').val(responseObject.triggerId);
	$('#jobId').val(responseObject.jobId);
	$("#ruleLookupTableData").multipleSelect("setSelects",[responseObject.lookuptableId]);
	$("#scheduler").val(responseObject.scheduleName);
	$("#job").val(responseObject.scheduleName);
	
	$("#divAddAutoUploadPopUp").click();
		
	$('#action').val(responseObject.action);
	
	//$("#action option:contains(" + responseObject.action + ")").attr('selected', 'selected');
	//setTimeout(function(){formatDuplicateCheckDropdown();
	// setValueInMultiSelect(responseObject.dbQuery);}, 500);
	
}

function deleteAutoReloadCache(id){
	clearAllMessages();
	var rowData = jQuery('#autoUploadGrid').jqGrid ('getRowData', id);
	$("#delete_label").show();
	$('#divDeleteTablePopup').show();
	$("#delete_warning").show();
	$("#deleteAutoReloadCacheId").val(id);
	$('#delete_lookup_table').click();
	var tableString ="<table class='table table-hover table-bordered'  border='1' >";
		tableString+="<th>AutoUpload Job Id</th>";
		tableString+="<th><spring:message code='rule.data.mgmt.table.name' ></spring:message></th>";
		tableString+="<th><spring:message code='rule.data.mgmt.scheduler.name' ></spring:message></th>";
		tableString+="<th><spring:message code='rule.data.mgmt.source.dir'></spring:message></th>";
		tableString += "<tr>";
		tableString += "<td>"+id+"</td>";
		tableString += "<td>"+rowData.tableName+"</td>";
		tableString += "<td>"+rowData.scheduleName+"</td>";
		tableString += "<td>"+rowData.sourceDirectory+"</td>";
		tableString += "</tr>";
		tableString+="</table>";
		$("#delete_selected_table_details").html(tableString);
		$("#delete_lookup_Table_bts_div").show();
		//$("#delete_lookup_Table_progress_bar_div").hide();
}

function deleteAutoReloadCacheConfig(){
	var id = $("#deleteAutoReloadCacheId").val();
	$.ajax({
		url: 'deleteAutoUploadConfig',
		cache: false,
		async: true,
		dataType:'json',
		type:'POST',
		data:
		 {
			"id" : id
		 }, 
		success: function(data){
			var response = eval(data);
			var responseCode = response.code; 
			var responseMsg = response.msg; 
		
			clearAllMessagesPopUp();
			resetWarningDisplayPopUp();
			 
			if(responseCode === "200"){
				closeFancyBox();
				reloadAutoReloadGridData();
				showSuccessMsg(responseMsg);
			}else{
				showErrorMsgPopUp(responseMsg);
			}
		},
	    error: function (xhr,st,err){
			handleGenericError(xhr,st,err);
		}
	});
}

function resetSearchRuleCriteria(){
	resetWarningDisplay();
	clearAllMessages();
	$('#ruleDataSourceDir').val('');
	$('#ruleDataScheduler').val('');
	$("#autoSuggestTableList").multipleSelect("setSelects",[-1]);
	$('#ruleDataTableID').val('-1');
	$('#jobSchedulerID').val('');
}

function selectSchedulerNamePopUp1(){
	getSchedulerList('<%= ControllerConstants.INIT_TRIGGER_DATA_LIST%>');
	$("#schedulerlistgrid1").jqGrid('resetSelection');
	$("#schedulerList1").click();
}

function getSchedulerList(urlAction) {
	$("#schedulerlistgrid1").jqGrid({
			url: urlAction,
			datatype: "json",
			postData : {},
			colNames: [
			            "#",
						"<spring:message code='trigger.mgmt.jqgrid.trigger.name' ></spring:message>",
						"<spring:message code='trigger.mgmt.jqgrid.description' ></spring:message>",
			            "Frequency"
				],
			colModel: [
				{name:'id',index:'id',hidden:true},
				{name:'triggerName',index:'triggerName',sortable:true},
				{name:'description',index:'description',sortable:true},
				{name:'type',index:'type',align:'center'}
			],
			rowNum : 15,
			rowList : [ 10, 20, 60, 100 ],
			height : 'auto',
			mtype : 'GET',
			contentType : "application/json; charset=utf-8",
			loadtext : "Loading...",
			sortname : 'id',
			sortorder : "desc",
			pager: "#schedulerGridPagingDiv1",
			viewrecords: true,
			multiselect: true,
			beforeSelectRow: function(rowid, e)
			{
			    $("#schedulerlistgrid1").jqGrid('resetSelection');
			    return (true);
			},
			beforeRequest : function() {
			    $('input[id=cb_schedulerlistgrid1]', 'div[id=jqgh_schedulerlistgrid1_cb]').remove();
			},
			loadComplete : function () {
				/* var rowId = jQuery('#schedulerlistgrid tr:eq(0)').attr('id');
				var dataFromTheRow = jQuery('#schedulerlistgrid').jqGrid ('getRowData', rowId);
				
				$('input[id^="jqg_schedulerlistgrid_"]').each(function(index){
					var schedulerName = dataFromTheRow[index].triggerName;
					var schedulerID = dataFromTheRow[index].id;
					$(this).attr("id",schedulerID + "_" + schedulerName + "_chkbox");
					$(this).attr("type","radio");
					$(this).attr("schedulerName","radio_grp");
				}); */
			},
			onSelectRow : function(id){
				rowData = $("#schedulerlistgrid1").jqGrid("getRowData", id);
			},
			timeout : 120000,
		    loadtext: "Loading...",
			loadError : function(xhr,st,err) {
				handleGenericError(xhr,st,err);
			},
			recordtext: "<spring:message code="serverManagement.grid.pager.total.records.text"></spring:message>",
		    emptyrecords: "No Record Found.",
		    viewrecords: true,
			}).navGrid("#schedulerGridPagingDiv1",{edit:false,add:false,del:false,search:false});
			$(".ui-jqgrid-titlebar").hide();
			$(".ui-pg-input").height("10px");
			
			/* if ($("#schedulerlistgrid1").getGridParam("reccount") === 0) {
				  $(".ui-paging-info").html("No Scheuler List Records Found.");
			}  */  
	}

$('#selectSchedulerbtn1').click(function(){
	if(rowData){
		$("#jobSchedulerID").val(rowData.id);
		$("#ruleDataScheduler").val(rowData.triggerName);
	}else{
		$("#jobSchedulerID").val("");
		$("#ruleDataScheduler").val("");
	}
	closeFancyBox();	
});


function selectTableNamePopUp1(){
	getTableListBySearchParams1('<%=ControllerConstants.INIT_RULE_TABLE_DATA_LIST%>');
	$("#tablelistgrid1").jqGrid('resetSelection');
	$("#tableList1").click();
}

function getTableListBySearchParams1(urlAction) {
$("#tablelistgrid1").jqGrid({
		url: urlAction,
		datatype: "json",
		postData : {},
		colNames: [
					  "Table id",
		           	  "Table Name"
			],
		colModel: [
			{name:'id',index:'id',sortable:true, hidden:true},
			{name:'viewName',index:'viewName',  sortable:true}
		],
		rowNum : 15,
		rowList : [ 10, 20, 60, 100 ],
		height : 'auto',
		mtype : 'GET',
		contentType : "application/json; charset=utf-8",
		loadtext : "Loading...",
		sortname : 'id',
		sortorder : "desc",
		pager: "#tableListGridPagingDiv1",
		viewrecords: true,
		multiselect: true,
		beforeSelectRow: function(rowid, e)
		{
		    $("#tablelistgrid1").jqGrid('resetSelection');
		    return (true);
		},
		beforeRequest : function() {
		    $('input[id=cb_tablelistgrid1]', 'div[id=jqgh_tablelistgrid1_cb]').remove();
		},
		loadComplete : function () {
			/* var rowId = jQuery('#tablelistgrid tr:eq(0)').attr('id');
			var dataFromTheRow = jQuery('#tablelistgrid').jqGrid ('getRowData', rowId);
			
			$('input[id^="jqg_tablelistgrid_"]').each(function(index){
				var tableName = dataFromTheRow[index].viewName;
				var tableID = dataFromTheRow[index].id;
				$(this).attr("id",tableID + "_" + tableName + "_chkbox");
				$(this).attr("type","radio");
				$(this).attr("viewName","radio_grp");
			}); */
		},
		onSelectRow : function(id){
			rowData = $("#tablelistgrid1").jqGrid("getRowData", id);
		},
		timeout : 120000,
	    loadtext: "Loading...",
		loadError : function(xhr,st,err) {
			handleGenericError(xhr,st,err);
		},
		recordtext: "<spring:message code="serverManagement.grid.pager.total.records.text"></spring:message>",
	    emptyrecords: "No Record Found.",
	    viewrecords: true,
		}).navGrid("#tableListGridPagingDiv1",{edit:false,add:false,del:false,search:false});
		$(".ui-jqgrid-titlebar").hide();
		$(".ui-pg-input").height("10px");
		
		/* if ($("#tablelistgrid1").getGridParam("reccount") === 0) {
			  $(".ui-paging-info").html("No Table List Record Found.");
		} */   
}

$('#selectTablebtn1').click(function(){
	if(rowData){
		$("#ruleDataTableID").val(rowData.id);
		$("#ruleDataTableName").val(rowData.viewName);
	}else{
		$("#ruleDataTableID").val(-1);
		$("#ruleDataTableName").val("");
	}
	closeFancyBox();
	
});

function displayDeleteAutoUploadJobPopup(){	
	clearAllMessagesPopUp();
	resetWarningDisplayPopUp();
	clearAllMessages();
	if(ckSelectedAutoUploadJobs.length === 0){
		$("#warn").show();
		$("#delete_warn_msg_link").click();
		return;
	}else{
		var tableString ="<table class='table table-hover table-bordered'  border='1' >";
		tableString+="<th>AutoUpload Job Id</th>";
		tableString+="<th><spring:message code='rule.data.mgmt.table.name' ></spring:message></th>";
		tableString+="<th><spring:message code='rule.data.mgmt.scheduler.name' ></spring:message></th>";
		tableString+="<th><spring:message code='rule.data.mgmt.source.dir'></spring:message></th>";
		$.each(deletableRows, function( index, value ) {
			var rowData = jQuery('#autoUploadGrid').jqGrid ('getRowData', value);
			tableString += "<tr>";
			tableString += "<td>"+value+"</td>";
			tableString += "<td>"+rowData.tableName+"</td>";
			tableString += "<td>"+rowData.scheduleName+"</td>";
			tableString += "<td>"+rowData.sourceDirectory+"</td>";
			tableString += "</tr>";
			});	
		
		tableString+="</table>"; 
		$("#delete_label").show();
		$("#divDeleteTablePopup").show();
		$("#delete_warning").show();
		$("#delete_selected_table_details").html(tableString);
		$("#delete_lookup_Table_bts_div").show();
		$("#delete_lookup_Table_progress_bar_div").hide();
		$("#deleteAutoReloadCacheId").val(ckSelectedAutoUploadJobs.toString());
		$("#delete_lookup_table").click();
	}
}

function getSelectedAutoUploadJobList(rowId){
	var rowData = jQuery('#autoUploadGrid').jqGrid ('getRowData', rowId);
	if( document.getElementById("checkbox_"+rowData.id).checked === true){
		if(ckSelectedAutoUploadJobs.indexOf(rowId) === -1){
			ckSelectedAutoUploadJobs.push(rowId);
			deletableRowData(rowId);
		}
	}else{
		if(ckSelectedAutoUploadJobs.indexOf(rowId) !== -1){
			ckSelectedAutoUploadJobs.splice(ckSelectedAutoUploadJobs.indexOf(rowId), 1);
			delete deletableRows[rowId];
		}
	}
}

/*
 * this function will handle root checkbox from child checkbox
 */
function updateRootCheckbox(element, gridId) {
	if(!element.checked){
		// if current child checkbox is not checked : uncheck root checkbox
		$('input:checkbox[id="selectAllUploadJob"]').prop('checked',false);
	} else {
		//if current child checkboox is checked and all checkbox are checked then check root checkbox
		var count = jQuery("#"+gridId).jqGrid('getGridParam', 'reccount');
		if ($('input:checkbox[name="uploadCheckbox"]:checked').length == count) {
			$('input:checkbox[id="selectAllUploadJob"]').prop('checked',true);
	    }
	}
}

function deletableRowData(rowId){
	var deletableRecord;
	var rowData = jQuery('#autoUploadGrid').jqGrid ('getRowData', rowId);
	deletableRows[rowId] = rowData.id;
}

function AutoUploadJobTableHeaderCheckbox(e, element) {
    e = e || event;/* get IE event ( not passed ) */
    e.stopPropagation ? e.stopPropagation() : e.cancelBubble = false;

    var rowId;
    //if root checkbox is checked :-> all child checkbox should ne checked
    if(element.checked) {
    	$('input:checkbox[name="uploadCheckbox"]').prop('checked',true);
    	$.each($("input[name='uploadCheckbox']:checked"), function(){
    		rowId = Number($(this).val()); 
    		if(ckSelectedAutoUploadJobs.indexOf(rowId) === -1){
    			ckSelectedAutoUploadJobs.push(rowId);
        		deletableRowData(rowId);
    		}
        });
    } else {
    	//if root checkbox is unchecked :-> all child checkbox should be unchecked
    	$('input:checkbox[name="uploadCheckbox"]').prop('checked',false);
    	$.each($("input[name='uploadCheckbox']:not(:checked)"), function(){  
    		rowId = Number($(this).val()); 
    		if(ckSelectedAutoUploadJobs.indexOf(rowId) !== -1){
    			ckSelectedAutoUploadJobs.splice(ckSelectedAutoUploadJobs.indexOf(rowId), 1);
    			delete deletableRows[rowId];
    		}
        });
    }
}

</script>
