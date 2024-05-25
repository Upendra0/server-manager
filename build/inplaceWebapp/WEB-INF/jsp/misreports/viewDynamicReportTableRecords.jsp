<%@page import="com.elitecore.sm.common.constants.BaseConstants"%>
<%@page import="com.elitecore.sm.common.model.DecodeTypeEnum"%>
<%@page import="com.elitecore.sm.common.constants.FormBeanConstants"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="com.elitecore.sm.util.MapCache"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@page	import="com.elitecore.sm.common.constants.SystemParametersConstant"%>
<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<%@page import="com.elitecore.sm.common.constants.EngineConstants"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/security/tags"
	prefix="sec"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="/WEB-INF/ShowErrorTag.tld" prefix="elitecoreError"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">


<html>
<body>
	<div class="tab-content no-padding clearfix"
		id="rule-data-configuration-block">
		<div class="fullwidth mtop10">

			<div class="col-md-5 inline-form"
				style="padding-left: 0px !important;">
				<div class="form-group">
					<div class="table-cell-label">Report Name : ${reportName}</div>
				</div>
			</div>
			<div class="col-md-5 inline-form"
				style="padding-left: 0px !important;">
				<div class="form-group">
					<div class="table-cell-label">Description : ${description}</div>

				</div>
			</div>
			<div class="col-md-12 inline-form"
				style="padding-left: 0px !important;">
				<div class="form-group">
					<div class="table-cell-label">
						<strong>Advance Search : </strong>
					</div>

				</div>
			</div>

			<!-- Start Search Input-->
			<div class="col-md-8 inline-form"
				style="padding-left: 0px !important;">
				<div class="form-group">
					<%-- <spring:message code="business.policy.condition.existing.list.column.value" var="tooltip" ></spring:message> --%>
					<div class="table-cell-label" style="width: 125px;">Search
						Record :</div>
					<div class="input-group ">
						<input type="text" id="dynamic_report_data_search"
							class="form-control table-cell input-sm" data-toggle="tooltip"
							tabindex="5" data-placement="bottom"
							title="Enter SQL Query to search specific data from table">
						<!-- ${tooltip} -->
						<span class="input-group-addon add-on last inline-form"
							style="visibility: visible !important;"> <a href="#"
							data-toggle="popover" title="" data-trigger="focus"
							data-content="All Sql Operators can be used for search. e.g. NAME like 'ab%' AND country ='india'">
								<i class="fa fa-question-circle fa-1x" aria-hidden="true"></i>
						</a>
						</span>
					</div>
				</div>
			</div>

			<div class="clearfix"></div>
			<div class="pbottom15 mtop10">
				<button id='search' class="btn btn-grey btn-xs" tabindex="7"
					onclick="setSearchValue();">
					<spring:message code="btn.label.search" ></spring:message>
				</button>
				<button id='reset' class="btn btn-grey btn-xs" tabindex="8"
					onclick="resetSearchLookup();">
					<spring:message code="btn.label.reset" ></spring:message>
				</button>
			</div>

			<!--Below link calls for PDF Generation param '4' excludes column in PDF-->
			<sec:authorize access="hasAuthority('DOWNLOAD_DYNAMIC_REPORT')">
				<div class="title2">
					<span class="title2rightfield"> <span
						class="title2rightfield-icon1-text"> <a href="#"
							id="exportDataPdf" onclick="exportReportData('pdf');"><spring:message
									code="service.wise.summary.generate.pdf" ></spring:message></a> <a href="#"
							id="exportDataXls" onclick="exportReportData('xls');"><spring:message
									code="service.wise.summary.generate.excel" ></spring:message> </a>
					</span>
					</span>
				</div>
			</sec:authorize>
			<div class="clearfix"></div>
			<div class="box-body table-responsive no-padding box">
				<table class="table table-hover" id="dynamicReportTableGrid"></table>
			</div>
				<div id="dynamicReportTableGridPagingDiv"></div>	
			<form id="dynamicReport_download_form" action="" method="POST"
				name="dynamicReport_download_form">
				<input type="hidden" name="searchQuery" id="searchQuery" value="" />
				<input type="hidden" name="fileType" id="fileType" value="" /> <input
					type="hidden" name="viewName" id="viewName" value="" /> <input
					type="hidden" name="reportName" id="reportName" value="" />
			</form>


			<div class="pbottom15 mtop10 title2rightfield">
				<button id='backToReport' class="btn btn-grey btn-xs" tabindex="7"
					onclick="loadDynamicReport();">
					<spring:message code="btn.label.back" ></spring:message>
				</button>
			</div>
		</div>

		<script type="text/javascript">

var tableId = ${tableId};
var viewName = '${viewName}';
var reportName= '${reportName}';
var MAX_EXCEL_ROWS_LIMIT = 65000; // Max rows supported by excel

$(document).ready(function(){
	$('[data-toggle="popover"]').popover({container: "body", placement: "right"
		}).on("show.bs.popover", function() {
	    return $(this).data("bs.popover").tip().css({
	    	"max-width": "25%"
	      });
	    });
	showErrorMsg("<h4> Loading . . . </h4>");
	createDynamicReportColModels();
	loadDynamicReportTableRecordsGrid();
});

function setSearchValue()
{
	searchQueStr = $("#dynamic_report_data_search").val();	
	clearAllMessages();
	var $grid = $("#dynamicReportTableGrid");
	jQuery('#dynamicReportTableGrid').jqGrid('clearGridData');
	cleardynamicReportTableGrid();

	$grid.jqGrid('setGridParam',{datatype:'json',page: 1,sortname: 'RECORDROWID',sortorder: "desc",postData:{
		 id : tableId ,viewName : viewName, searchQuery : searchQueStr 
		 }}).trigger('reloadGrid');
	
} 

function cleardynamicReportTableGrid(){
	$grid = $("#dynamicReportTableGrid");
	var rowIds = $grid.jqGrid('getDataIDs');
	for(var i=0,len=rowIds.length;i<len;i++){
		var currRow = rowIds[i];
		$grid.jqGrid('delRowData', currRow);
	}
}

function createDynamicReportColModels(){
	ruleRecordColumns = JSON.parse('${tableFieldListJson}');
	for(i = 0; i < ruleRecordColumns.length; i++){
		ruleRecordColModels[i] = {name: ruleRecordColumns[i], index: ruleRecordColumns[i], editable:true, editrules:{required:true}, sortable:true}
	}
}

//original
 function loadDynamicReportTableRecordsGrid(){
	$("#dynamicReportTableGrid").jqGrid({
		url: '<%= ControllerConstants.INIT_DYNAMIC_REPORT_RECORD_LIST%>',
			postData : { id : tableId ,viewName : viewName 
		},
		datatype: "json",
		colNames: ruleRecordColumns,
		colModel: ruleRecordColModels,
		rowNum:<%= MapCache.getConfigValueAsInteger(SystemParametersConstant.TOTAL_ROWS_TO_DISPLAY_IN_GRID, 10) %>,
		rowList:[5,10,20,50,100],
		height: 'auto',
		sortorder: "desc",
		pager: "#dynamicReportTableGridPagingDiv",
		viewrecords: true,
		multiselect: false,
		timeout : 120000,
		caption: reportName,
		loadComplete: function(data) {
			currentPage = jQuery('#dynamicReportTableGrid').getGridParam('page');
			$(".ui-dialog-titlebar").show();
			clearAllMessages();
 			var resData = eval(data);
 			if(data.code == 400){	 				
 				showErrorMsg(data.msg);
 			}
		}, 
		loadError : function(xhr,st,err) {
			handleGenericError(xhr,st,err);
		},
		recordtext: "<spring:message code="serverManagement.grid.pager.total.records.text"></spring:message>",
	     emptyrecords: "<spring:message code="collectionService.summary.agent.grid.empty.records"></spring:message>",
		loadtext: "<spring:message code="collectionService.summary.agent.grid.loading.text"></spring:message>",
		pgtext : "<spring:message code="collectionService.summary.agent.grid.pager.text"></spring:message>",
		 onPaging : function (){
			showErrorMsg("<h4> Loading . . . </h4>");
			$("#dynamicReportTableGrid_ilcancel").click();
		} 
	}).navGrid("#dynamicReportTableGridPagingDiv",{edit:false,add:false,del:false,search:false,refresh:true});
}

function resetSearchLookup(){
	$("#dynamic_report_data_search").val("");
}

//Added support to show Alert msg 
function getReportCount(){
	var searchTerm = $('#dynamic_report_data_search').val();
	var reportCount = 0;
	//console.log(viewName ,searchTerm, 'hi');
	$.ajax({
		url: '<%=ControllerConstants.GET_DYNAMIC_REPORT_COUNT%>?SearchQuery=' +searchTerm+ '&viewName=' +viewName,
		cache: false,
		async: false, 
		dataType:'json',
		type: "GET",
		success: function(data){	
			var response = eval(data);
			var responseCode = response.code; 
			var count = response.msg; 
			if(responseCode == "200"){
				console.log('count... ' + count);
				reportCount = count;
				}
			},
		error: function(xhr,status,error){
	    	clearAllMessages();
	    	showErrorMsg(xhr.responseText);
		} 
			
	});
	return reportCount;
}

 function exportReportData(file){
	clearAllMessages();    
	document.dynamicReport_download_form.fileType.value=file;
	document.dynamicReport_download_form.searchQuery.value=$('#dynamic_report_data_search').val();
	document.dynamicReport_download_form.viewName.value=viewName;
	document.dynamicReport_download_form.reportName.value=reportName;
	document.dynamicReport_download_form.method='POST';
	
	// function to call controlller to fetch records count from DB to show alert msg to end user MEDSUP 2202
	var resultCount =  getReportCount(viewName);
	
	if(resultCount >MAX_EXCEL_ROWS_LIMIT && file=="xls"){
		showErrorMsg("Application downloading only 65K records as records are more than 65K due to Limitations that we have in an EXCEL <br/> Either use PDF to download all or Use the search option to download only needed matching to your criteria");		
	}
	//console.log("Result Count is: "+ resultCount);
	document.dynamicReport_download_form.action='<%=request.getContextPath()%>/<%=ControllerConstants.DOWNLOAD_DYNAMIC_REPORT%>';  // send it to server which will open this contents in excel file
	document.dynamicReport_download_form.submit();    
}
</script>
</body>
</html>
