<%@page import="com.elitecore.sm.common.constants.BaseConstants"%>
<%@page import="com.elitecore.sm.common.model.DecodeTypeEnum"%>
<%@page import="com.elitecore.sm.common.constants.FormBeanConstants"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.elitecore.sm.util.MapCache"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@page import="com.elitecore.sm.common.constants.SystemParametersConstant"%>
<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<%@page import="com.elitecore.sm.common.constants.EngineConstants"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="/WEB-INF/ShowErrorTag.tld" prefix="elitecoreError"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
 <script>
$(document).ready(function(){
	      getTableList();
});
</script>
<script>
$(document).ready(function(){
$('#autoSuggestTableList').chosen().change(function(){
    var myValues = $('#autoSuggestTableList').chosen().val();
    var tableName="";
	if($('#autoSuggestTableList').val()!=-1){
		tableName=$('#autoSuggestTableList').find(":selected").text();
	}
    getAutoReloadDbQueryList("getDatabaseQueryList", $("#autoReloadserverInstance").val(),tableName);
});
});
</script>
 <script type="text/javascript" src="${pageContext.request.contextPath}/js/chosen.jquery.js"></script>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/chosen.css" type="text/css"/>
<style>
.ms-drop ul {
    height : 90px !important;
}
.ms-drop input[type=radio]{
    display:none;
}
</style>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<div class="tab-content no-padding clearfix" id="rule-data-configuration-block">
	<div class="fullwidth mtop10">
		<div class="fullwidth">
			<div class="title2">
				<spring:message code="auto.reload.cache.config.search.title" ></spring:message>
			</div>
		</div>
		 <div class="col-md-6 inline-form" style="padding-left: 0px !important;display:none">
			<div class="form-group">
				<div class="table-cell-label">
					<spring:message code="rule.data.mgmt.table.name" ></spring:message>
				</div>
				<div class="input-group">
					<spring:message code="rule.data.mgmt.table.name" var="tooltip" ></spring:message>
					<select  name = "ruleDataTableName" class="form-control table-cell input-sm"  tabindex="3" id="ruleDataTableName" data-toggle="tooltip" data-placement="bottom"  title="${tooltip }">
	             		<option  value="-1" selected="selected">Select Table Name</option>
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
					<spring:message code="rule.data.mgmt.table.name" ></spring:message>
				</div>
				<select class="chosen-select" tabindex="2" id="autoSuggestTableList" style="width:92%">
				<option value="-1">Start typing or Select (All)</option>
          </select>
			</div>
		</div> 
		<div class="col-md-6 inline-form"
			style="padding-left: 0px !important;">
			<div class="form-group">
         		<spring:message code="serverManager.server.instance" var="label" ></spring:message>
         		<spring:message code="serverManager.server.instance" var="tooltip" ></spring:message>
         		<div class="table-cell-label">${label}</div>
             	<div class="input-group">
             		<select class="form-control table-cell input-sm"  tabindex="3" id="autoReloadserverInstance" data-toggle="tooltip" data-placement="bottom"  title="${tooltip}">
             		<option value="-1" selected="selected">Select Server Instance</option>
					<c:if test="${SERVERINSTANCE_LIST !=null && fn:length(SERVERINSTANCE_LIST) gt 0}">
						<c:forEach items="${SERVERINSTANCE_LIST}" var="serverInstance">
							<option value="${serverInstance.id}">${serverInstance.name}</option>
						</c:forEach>
					</c:if>
             		</select>
             		<span class="input-group-addon add-on last" > <i class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="left" title=""></i></span>
             	</div>
            </div>
		</div>
		<div class="col-md-6 inline-form"
			style="padding-left: 0px !important;">
			<div class="form-group">
				<spring:message code="file.reprocess.serviceInstanceList.label" var="label" ></spring:message>
         		<spring:message code="file.reprocess.serviceInstanceList.label.tooltip" var="tooltip" ></spring:message>
         		<div class="table-cell-label">DB Query</div>
             	<div class="input-group">
             	<select  class="form-control table-cell input-sm"  tabindex="3" id="databaseQueryListAuto" data-toggle="tooltip" data-placement="bottom"  title="${tooltip }">
             		<option value="-1">Select DB Query</option>
	             </select>
             		<span class="input-group-addon add-on last" > <i class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="left" title=""></i></span>
             	</div>
			</div>
		</div>


	
	<div class="clearfix"></div>
		<div class="pbottom15 mtop10">
			<button id='search' class="btn btn-grey btn-xs" onclick="reloadAutoReloadGridData('true');"
				tabindex="5">
				<spring:message code="btn.label.search" ></spring:message>
			</button>
			<button class="btn btn-grey btn-xs"
				onclick="resetSearchRuleCriteria();reloadAutoReloadGridData('false');" tabindex="6">
				<spring:message code="btn.label.reset" ></spring:message>
			</button>
		</div>
	<div class="fullwidth">
		<div class="title2">
			<spring:message code="auto.reload.cache.config.list" ></spring:message>
			<span class="title2rightfield"> <span
				class="title2rightfield-icon1-text"> <sec:authorize
						access="hasAuthority('CREATE_AUTO_RELOAD_CACHE')">
						<a href="#" onclick="createAutoReloadCache(); "><i
							class="fa fa-plus-circle"></i></a>

						<a href="#" id="createAutoReloadCache" onclick="createAutoReloadCache();">
							<spring:message code="rule.data.mgmt.add" ></spring:message>
						</a>

					</sec:authorize>
			</span>
			<span class="title2rightfield-icon1-text"> 
				<sec:authorize access="hasAuthority('DELETE_AUTO_RELOAD_CACHE')">
					<a href="#"><i class="fa fa-trash" onclick="displayDeleteAutoReloadJobPopup();"></i></a>

					<a href="#" id="deleteRuleDataTable" onclick="displayDeleteAutoReloadJobPopup();">
						<spring:message code="rule.data.mgmt.delete.link" ></spring:message>
					</a>
					<a href="#divDeleteLookupTable" class="fancybox" style="display: none;" id="delete_lookup_Tables">#</a>
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
	<a href="#divAddAutoReloadCache" class="fancybox" style="display: none;" id="divAddAutoReloadCachePopUp">#</a>
	
	<div id="divAddAutoReloadCache" style=" width:100%; display: none;" >
		<jsp:include page="autoReloadCacheConfigPopUp.jsp"></jsp:include>
	</div>
		
	<div class="box-body table-responsive no-padding box">
		<table class="table table-hover" id="autoReloadGrid"></table>
		<div id="autoReloadGridPagingDiv"></div>
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
					<spring:message code="auto.reload.cache.selection.warning" ></spring:message>
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
	
	<div id="divDeleteLookupTable" style="display: none;">
	
	<div class="modal-content">
					<div class="modal-header padding10">
						<h4 class="modal-title">
							<span id="delete_label" style="display: none;"><spring:message
									code="auto.reload.cache.delete" ></spring:message></span>
								</h4>
					</div>	
		<!-- Delete Popup code started -->	
 		<div id="divDeleteTablePopup" style="width: 100%; display: none;">
			<div class="modal-content">
				<div class="modal-body padding10 inline-form">
					<div class="fullwidth">
						<input type="hidden" value="" id="deleteAutoReloadCacheId"
							name="deleteAutoReloadCacheId" />
						<div id="delete_selected_table_details"></div>
						<div id="delete_warning" style="display: none;">
							<spring:message code="autoReloadCache.delete.warning" ></spring:message>
						</div>
					</div>
				</div>
					<div id="delete_lookup_Table_bts_div" class="modal-footer padding10">
						<sec:authorize access="hasAuthority('DELETE_RULE_DATA_CONFIG')">
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
		<!-- Delete attribute popup div end here -->
		</div>
		</div>
</div>
</div>

<script>
var deletableRows = {};
var ckSelectedAutoReloadJobs = [];
$(document).ready(function() {
	loadRuledataDictGrid(false);
	reloadAutoReloadGridData();
	loadAutoReloadTableList();
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
function loadAutoReloadTableList(){
	$.ajax({
		url: 'getRuleLookUpTableTableList',
		cache: false,
		async: true,
		dataType:'json',
		type:'POST',
		data:
		 {}, 
		success: function(data){
			var response = eval(data);
			var responseCode = response.code; 
			var responseMsg = response.msg; 
		
			clearAllMessagesPopUp();
			resetWarningDisplayPopUp();
			 
			if(responseCode === "200"){
				 $.each(response.object, function(i,tableMap) {
				        $("#autoSuggestTableList").append("<option value='"+tableMap['id']+"'>"+tableMap['viewName']+"</option>");
				  });
			}else{
				showErrorMsgPopUp(responseMsg);
			}
		},
	    error: function (xhr,st,err){
			handleGenericError(xhr,st,err);
		}
	});
}

function createAutoReloadCache(){
	clearAllMessages();
	resetWarningDisplayPopUp();
	ckSelectedAutoReloadJobs = [];
	deletableRows = {};
	$("#databaseQueryList option[value]").remove();
	$('#databaseQueryList').multipleSelect("refresh");
	$("#update_label").hide();
	$("#add_label").show();
	$("#updateLookupTable").hide();
	$("#addNewLookupTable").show();
	$('#tableId').val(0);
	$("#serverInstance").val(-1);
	$('#scheduleType').val("Immediate");
	$('#schedulerId').val(0);
	$("#scheduler").val("");
	$("#schedulerDiv").hide();
	$('#reloadOptions').val("Full");
	$('#ruleLookupTableData_chosen').css('width','192px');
	$("#divAddAutoReloadCachePopUp").click();
}

function formatDuplicateCheckDropdown(){
	
	$('#databaseQueryList').multiselect({
		enableFiltering: true,
		enableHTML : true,
		maxHeight: 200,
		dropDown: true
	});
	$('#databaseQueryList').multiselect("enable");
};

function setValueInMultiSelect(value){
    var valArr = value.split(",");
    var i = 0, size = valArr.length;
    for (i; i < size; i++) {
    	$('#databaseQueryList').multiselect('select', $("#"+valArr[i]).val());
    }
    
   
    /** Edited for dropdown button ID,Duplicate Check Criteria **/
    var multiselectbtn = document.querySelector(".multiselect");
    multiselectbtn.setAttribute("id", "btn_databaseQueryList");    
    $('#btn_databaseQueryList').parent().children(".multiselect-container").attr("id", "multiselect_menu");
    $( '#btn_databaseQueryList' ).click(function(){
    	var showhide = $( '#multiselect_menu').css("display");  // $(".multiselect-container").css("display");
	    	if(showhide == "none" || showhide == null){
	    		$('#multiselect_menu').show();
	    	}else {
	    		$('#multiselect_menu').hide();
	    	}
    	}); 
    /**End of Edited for dropdown button ID **/
}
var tempDBQueryArray = [];
function updateAutoReloadCache(Id,cellvalue){
	clearAllMessages();
	resetWarningDisplayPopUp();
	$("#databaseQueryList option[value]").remove();
	$('#databaseQueryList').multipleSelect("refresh");
	var responseObject = jQuery("#autoReloadGrid").jqGrid('getRowData', Id);
	console.log(responseObject.dbQuery);
	tempDBQueryArray = responseObject.dbQuery.split(",");
	console.log(tempDBQueryArray);
	$("#update_label").show();
	$("#add_label").hide();
	$("#updateLookupTable").show();
	$("#addNewLookupTable").hide();
	$("#id").val(responseObject.id);
	$("#tableId").val(responseObject.tableId);
	$("#ruleLookupTableData").val(responseObject.tableId);
	$("#ruleLookupTableData").trigger("chosen:updated");
	//$("#ruleLookupTableData").multipleSelect("setSelects",[responseObject.tableId]);
	$("#serverInstance option:contains(" + responseObject.serverInstance + ")").attr('selected', 'selected');
	
	getDbQueryList("getDatabaseQueryList", $("#serverInstance").val(),responseObject.viewName,true);
	
	$('#scheduleType').val(responseObject.reloadSchedule);
	if($("#scheduleType").val() === 'Schedule'){
		$("#schedulerDiv").show();
		$("#scheduler").val(responseObject.scheduler);
		$('#schedulerId').val(responseObject.triggerId);
	}else{
		$('#schedulerId').val(0);
		$("#scheduler").val("");
		$("#schedulerDiv").hide();
	}
	$('#reloadOptions').val(responseObject.reloadOptions);
	$('#ruleLookupTableData_chosen').css('width','192px');
	$("#divAddAutoReloadCachePopUp").click();
	
}

function loadRuledataDictGrid(isSearch){
	$("#autoReloadGrid").jqGrid({
		url: "<%= ControllerConstants.INIT_AUTO_RELOAD_CACHE_CONFIG_LIST%>",
		postData : {
			searchName: function () {
		        return $("#autoSuggestTableList").val();
	   		},
	   		searchServerInstance: function () {
		        return $("#autoReloadserverInstance").val();
	   		},
	   		searchDBQuery: function () {
		        return $("#databaseQueryListAuto").val();
	   		},
	   		'isSearch':isSearch
		},
		datatype: "json",
		colNames: [
		            "<input type='checkbox' id='selectAllReloadJob' onclick='AutoReloadJobTableHeaderCheckbox(event, this)'></input>",
					"<spring:message code='rule.data.mgmt.table.id'></spring:message>",
					"<spring:message code='rule.data.mgmt.table.name'></spring:message>",
					"<spring:message code='auto.reload.cache.config.server.instance'></spring:message>",
					"<spring:message code='auto.reload.cache.config.db.query'></spring:message>",
					"<spring:message code='auto.reload.cache.config.reload.schedule'></spring:message>",
					"<spring:message code='rule.data.mgmt.edit'></spring:message>",
					"<spring:message code='business.policy.rule.group.grid.column.delete' ></spring:message>",
					"#","#","#","#"
		],
		colModel: [			
					{name: 'checkbox', index: 'checkbox',sortable:false, formatter:checkBoxFormatter, align : 'center', width: 30},
					{name:'id',index:'id',align:'center'},
					{name:'viewName',index:'viewName',align:'center',sortable: false},
					{name:'serverInstance',index:'serverInstance',align:'center',sortable: false},
					{name:'dbQuery',index:'dbQuery',align:'center',sortable: false},
					{name:'reloadSchedule',index:'reloadSchedule',align:'center',sortable:false},
					{name:'edit',index:'edit',align:'center',formatter:editFormatter,sortable:false},
					{name:'delete',index:'delete',align:'center', formatter:deleteFormatter,sortable: false},
					{name:'reloadOptions',index:'reloadOptions',hidden:true},
					{name:'tableId',index:'tableId',hidden:true},
					{name:'scheduler',index:'scheduler',hidden:true},
					{name:'triggerId',index:'triggerId',hidden:true}
		],
		rowNum:<%= MapCache.getConfigValueAsInteger(SystemParametersConstant.TOTAL_ROWS_TO_DISPLAY_IN_GRID, 10) %>,
		rowList:[5,10,20,50,100],
		height: 'auto',
		sortname: 'id',
		sortorder: "desc",
		pager: "#autoReloadGridPagingDiv",
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
	}).navGrid("#autoReloadGridPagingDiv",{edit:false,add:false,del:false,search:false});
	$(".ui-jqgrid-titlebar").hide();
	$(".ui-pg-input").height("10px");
}

function reloadAutoReloadGridData(isSearch){
	clearAllMessages();
	var $grid = $("#autoReloadGrid");
	jQuery('#autoReloadGrid').jqGrid('clearGridData');
	clearAutoReloadGrid();
	ckSelectedAutoReloadJobs = [];
	deletableRows = {};
	$("#selectAllReloadJob").prop('checked',false);
	$grid.jqGrid('setGridParam',{datatype:'json',page: 1,sortname: 'id',sortorder: "desc",postData:{
		'searchName': function () {
	        return $("#autoSuggestTableList").val();
   		},
   		'searchServerInstance': function () {
	        return $("#autoReloadserverInstance").val();
   		},
   		'searchDBQuery': function () {
	        return $("#databaseQueryListAuto").val();
   		},
   		'isSearch':isSearch}}).trigger('reloadGrid');
}

function clearAutoReloadGrid(){
	$grid = $("#autoReloadGrid");
	var rowIds = $grid.jqGrid('getDataIDs');
	// iterate through the rows and delete each of them
	for(var i=0,len=rowIds.length;i<len;i++){
		var currRow = rowIds[i];
		$grid.jqGrid('delRowData', currRow);
	}
}

function editFormatter(cellvalue, options, rowObject){
	var id=rowObject.viewName+"_edit";
	return '<a id="'+id+'" class="link" onclick="updateAutoReloadCache('+"'" + rowObject["id"]+ "'" +','+"'" + cellvalue+ "'" + ');">' + "<i class='fa fa-pencil-square-o' aria-hidden='true'></i></a>";
}

function deleteFormatter(cellvalue, options, rowObject){
	var id=rowObject.viewName+"_deletelookup";
	return '<a id="'+id+'" href="#" onclick="deleteAutoReloadCache('+"'" + rowObject["id"]+ "'"+');"><i class="fa fa-trash"></i></a>';
}

function deleteAutoReloadCache(id){
	clearAllMessages();
	var rowData = jQuery('#autoReloadGrid').jqGrid ('getRowData', id);
	$("#delete_label").show();
	$('#divDeleteTablePopup').show();
	$("#delete_warning").show();
	$("#deleteAutoReloadCacheId").val(id);
	$('#delete_lookup_table').click();
	var tableString ="<table class='table table-hover table-bordered'  border='1' >";
		tableString+="<th><spring:message code='rule.data.mgmt.table.name' ></spring:message></th>";
		tableString+="<th><spring:message code='auto.reload.cache.config.db.query' ></spring:message></th>";
		tableString += "<tr>";
		tableString += "<td>"+rowData.viewName+"</td>";
		tableString += "<td>"+rowData.dbQuery+"</td>";
		tableString += "</tr>";
		tableString+="</table>";
		$("#delete_selected_table_details").html(tableString);
		$("#delete_lookup_Table_bts_div").show();
		$("#delete_lookup_Table_progress_bar_div").hide();
}

function deleteAutoReloadCacheConfig(){
	var id = $("#deleteAutoReloadCacheId").val();
	
	$.ajax({
		url: 'deleteAutoReloadCacheConfig',
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
$("#autoReloadserverInstance").change(function() {
	var tableName="";
	if($('#autoSuggestTableList').val()!=-1){
		tableName=$('#autoSuggestTableList').find(":selected").text();
	}
	getAutoReloadDbQueryList("getDatabaseQueryList", $("#autoReloadserverInstance").val(),tableName);
	$('#databaseQueryListAuto').find('option').not(':first').remove();
});
function getAutoReloadDbQueryList(urlAction, serverInstanceId, tableName){
	$.ajax({
		url: urlAction, 
		data: {serverInstanceId: serverInstanceId,tableName:tableName} , 
		success: function(data){
      		var response = $.parseJSON(data);
			var msg = response.msg;
			var code = response.code;
			if(code === "200"){
				var dbQueryList = response.object;
	        	var dbQueryOptionList = [];
	        	$('#databaseQueryListAuto').find('option').not(':first').remove();
	        	jQuery.each(dbQueryList, function(i, dbQuery){
	        		jQuery.each(dbQuery, function(i, item){
	        			$("#databaseQueryListAuto").append("<option value='"+item['queryName']+"'>"+item['queryName']+"</option>");
					});
	    		});
			}
    }});
}

function resetSearchRuleCriteria(){
	resetWarningDisplay();
	clearAllMessages();
	$('#autoReloadserverInstance').val('-1');
	$("#autoSuggestTableList").val('-1').trigger("chosen:updated");
	//$("#autoSuggestTableList").multipleSelect("setSelects",[-1]);
	$('#databaseQueryListAuto').find('option').not(':first').remove();
	$('#databaseQueryListAuto').val('-1');
}

function checkBoxFormatter(cellvalue, options, rowObject) {
	if ($.inArray( rowObject.id ,  ckSelectedAutoReloadJobs ) != -1){
		return '<input type="checkbox" id="checkbox_'+rowObject["id"]+'" name="reloadCheckbox" value="'+rowObject["id"]+'" onchange="updateRootCheckbox(this, \'autoReloadGrid\');getSelectedAutoReloadJobList('+rowObject.id+');" checked/>'; 
	} else {
		return '<input type="checkbox" id="checkbox_'+rowObject["id"]+'" name="reloadCheckbox" value="'+rowObject["id"]+'" onchange="updateRootCheckbox(this, \'autoReloadGrid\');getSelectedAutoReloadJobList('+rowObject.id+');"/>';
	}
}

function displayDeleteAutoReloadJobPopup(){	
	clearAllMessagesPopUp();
	resetWarningDisplayPopUp();
	clearAllMessages();
	if(ckSelectedAutoReloadJobs.length === 0){
		$("#warn").show();
		$("#delete_warn_msg_link").click();
		return;
	}else{
		var tableString ="<table class='table table-hover table-bordered'  border='1' >";
		tableString+="<th>AutoReload Job Id</th>";
		tableString+="<th><spring:message code='rule.data.mgmt.table.name' ></spring:message></th>";
		tableString+="<th><spring:message code='auto.reload.cache.config.db.query' ></spring:message></th>";
		$.each(deletableRows, function( index, value ) {
			var rowData = jQuery('#autoReloadGrid').jqGrid ('getRowData', value);
			tableString += "<tr>";
			tableString += "<td>"+value+"</td>";
			tableString += "<td>"+rowData.viewName+"</td>";
			tableString += "<td>"+rowData.dbQuery+"</td>";
			tableString += "</tr>";
			});	
		
		tableString+="</table>"; 
		$("#delete_label").show();
		$("#divDeleteTablePopup").show();
		$("#delete_warning").show();
		$("#delete_selected_table_details").html(tableString);
		$("#delete_lookup_Table_bts_div").show();
		$("#delete_lookup_Table_progress_bar_div").hide();
		$("#deleteAutoReloadCacheId").val(ckSelectedAutoReloadJobs.toString());
		$("#delete_lookup_table").click();
	}
}

function getSelectedAutoReloadJobList(rowId){
	var rowData = jQuery('#autoReloadGrid').jqGrid ('getRowData', rowId);
	if( document.getElementById("checkbox_"+rowData.id).checked === true){
		if(ckSelectedAutoReloadJobs.indexOf(rowId) === -1){
			ckSelectedAutoReloadJobs.push(rowId);
			deletableRowData(rowId);
		}
	}else{
		if(ckSelectedAutoReloadJobs.indexOf(rowId) !== -1){
			ckSelectedAutoReloadJobs.splice(ckSelectedAutoReloadJobs.indexOf(rowId), 1);
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
		$('input:checkbox[id="selectAllReloadJob"]').prop('checked',false);
	} else {
		//if current child checkboox is checked and all checkbox are checked then check root checkbox
		var count = jQuery("#"+gridId).jqGrid('getGridParam', 'reccount');
		if ($('input:checkbox[name="reloadCheckbox"]:checked').length == count) {
			$('input:checkbox[id="selectAllReloadJob"]').prop('checked',true);
	    }
	}
}

function deletableRowData(rowId){
	var deletableRecord;
	var rowData = jQuery('#autoReloadGrid').jqGrid ('getRowData', rowId);
	deletableRows[rowId] = rowData.id;
}

function AutoReloadJobTableHeaderCheckbox(e, element) {
    e = e || event;/* get IE event ( not passed ) */
    e.stopPropagation ? e.stopPropagation() : e.cancelBubble = false;

    var rowId;
    //if root checkbox is checked :-> all child checkbox should ne checked
    if(element.checked) {
    	$('input:checkbox[name="reloadCheckbox"]').prop('checked',true);
    	$.each($("input[name='reloadCheckbox']:checked"), function(){
    		rowId = Number($(this).val()); 
    		if(ckSelectedAutoReloadJobs.indexOf(rowId) === -1){
    			ckSelectedAutoReloadJobs.push(rowId);
        		deletableRowData(rowId);
    		}
        });
    } else {
    	//if root checkbox is unchecked :-> all child checkbox should be unchecked
    	$('input:checkbox[name="reloadCheckbox"]').prop('checked',false);
    	$.each($("input[name='reloadCheckbox']:not(:checked)"), function(){  
    		rowId = Number($(this).val()); 
    		if(ckSelectedAutoReloadJobs.indexOf(rowId) !== -1){
    			ckSelectedAutoReloadJobs.splice(ckSelectedAutoReloadJobs.indexOf(rowId), 1);
    			delete deletableRows[rowId];
    		}
        });
    }
}
</script>
