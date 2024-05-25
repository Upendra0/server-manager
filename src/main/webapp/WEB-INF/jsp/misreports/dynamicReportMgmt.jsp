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
				<spring:message code="dynamic.report.search" ></spring:message>
			</div>
		</div>
		
		<div class="col-md-6 inline-form"
			style="padding-left: 0px !important;">
			<div class="form-group">
				<div class="table-cell-label">
					<spring:message code="dynamic.report.report.name" ></spring:message>
				</div>
				<div class="input-group">
					<spring:message code="dynamic.report.report.name" var="tooltip" ></spring:message>
					<input type="text" id="dynamicReportName"
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
				<spring:message code="dynamic.report.report.desc" var="tooltip" ></spring:message>
				<div class="table-cell-label">${tooltip}</div>
				<div class="input-group ">
					<input type="text" id="dynamic-report-desc"
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
			<button id='search' class="btn btn-grey btn-xs" onclick="reloadTableGridData();"
				tabindex="5">
				<spring:message code="btn.label.search" ></spring:message>
			</button>
			<button class="btn btn-grey btn-xs"
				onclick="resetSearchReportCriteria();reloadTableGridData();" tabindex="6">
				<spring:message code="btn.label.reset" ></spring:message>
			</button>
		</div>
	   		       
	<div class="box-body table-responsive no-padding box">
		<table class="table table-hover" id="tableGrid"></table>
		<div id="tableGridPagingDiv"></div>
	</div>
	
	<form action="<%= ControllerConstants.INIT_DYNAMIC_REPORT_HEADER_LIST%>" method="POST" id="view_dynamic_report_table_records_form">
		<input type="hidden" id="viewDynamicReportTableId" name = "viewDynamicReportTableId"/>
		<input type="hidden" id="viewDynamicReportName" name = "viewDynamicReportName"/>
		<input type="hidden" id="viewDynamicReportDescription" name = "viewDynamicReportDescription"/>
		<input type="hidden" id="viewDynamicReportTableViewName" name = "viewDynamicReportTableViewName"/>
		<input type="hidden" id="viewDynamicReportTableDisplayfields" name = "viewDynamicReportTableDisplayfields"/>
	</form>
	
</div>
</div>
<script>
$(document).ready(function() {
	loadRuledataDictGrid(false);
	reloadTableGridData();
});

function loadRuledataDictGrid(isSearch){
	$("#tableGrid").jqGrid({
		url: "<%= ControllerConstants.INIT_DYNAMIC_REPORT_LIST%>",
		postData : {
			reportName: function () {
   	        	return $("#dynamicReportName").val();
    		},
    		reportDesc: function () {
   	        	return $("#dynamic-report-desc").val();
    		},
    		'isSearch':isSearch
		},
		datatype: "json",
		colNames: [
					  "#",
					  "#",
					  "#",
					  "<spring:message code='dynamic.report.report.name' ></spring:message>",
		           	  "<spring:message code='dynamic.report.report.desc' ></spring:message>",
		              "<spring:message code='dynamic.report.report.view' ></spring:message>"  ],
		colModel: [
			{name:'id',index:'id',hidden:true},
			{name:'viewName',index:'viewName',hidden:true},
			{name:'displayfields',index:'displayfields',hidden:true},
			{name:'reportName',index:'reportName'},
			{name:'description',index:'description'},
			{name:'viewRecords',index:'viewRecords',align:'center',formatter:viewRecordsFormatter,sortable:false}
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
		caption: "<spring:message code="dynamic.report.list"></spring:message>",
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
	});
	$(".ui-jqgrid-titlebar").hide();
	$(".ui-pg-input").height("10px");
}

function reloadTableGridData(){
	
	clearAllMessages();
	var $grid = $("#tableGrid");
	jQuery('#tableGrid').jqGrid('clearGridData');
	clearTableGrid();

	$grid.jqGrid('setGridParam',{datatype:'json',page: 1,sortname: 'id',sortorder: "desc",postData:{
	  
   		'reportName': function () {
	        return $("#dynamicReportName").val();
   		},
   		'reportDesc': function () {
	        return $("#dynamic-report-desc").val();
   		},
   		/* 'isSearch':isSearch */}}).trigger('reloadGrid');
}

function resetSearchReportCriteria(){
	$("#dynamicReportName").val("");
	$("#dynamic-report-desc").val("");
}

function clearTableGrid(){
	$grid = $("#tableGrid");
	var rowIds = $grid.jqGrid('getDataIDs');
	for(var i=0,len=rowIds.length;i<len;i++){
		var currRow = rowIds[i];
		$grid.jqGrid('delRowData', currRow);
	}
}

function viewRecordsFormatter(cellvalue, options, rowObject){
	var id=rowObject.viewName+"_viewRecords";
	return '<a id="'+id+'" href="#" onclick="viewRecordsInit('+"'" + rowObject["id"]+ "'"+');"><i class="fa fa-fw fa-vimeo-square"></i></a>';
} 

function viewRecordsInit(id){
	var responseObject = jQuery("#tableGrid").jqGrid('getRowData', id);
	$("#viewDynamicReportTableId").val(responseObject.id);
	$("#viewDynamicReportName").val(responseObject.reportName);
	$("#viewDynamicReportDescription").val(responseObject.description);
	$("#viewDynamicReportTableViewName").val(responseObject.viewName);
	$("#viewDynamicReportTableDisplayfields").val(responseObject.displayfields);
	$("#view_dynamic_report_table_records_form").submit();
} 

</script>
