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
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<div class="tab-content no-padding clearfix" id="rule-data-configuration-block">
	<div class="fullwidth mtop10">
				<div id="AddPopUpMsg">
					<jsp:include page="../common/responseMsgPopUp.jsp" ></jsp:include>
				</div>	
		<div class="fullwidth">
			<div class="title2"> <spring:message code="rule.data.auto.job.statistics.view.status"></spring:message></div>
		</div>
		<div class="col-md-6 inline-form" style="padding-left: 0px !important;">
			<div class="form-group">
				<spring:message code="rule.data.auto.job.statistics.batch.id" var="label" ></spring:message>
         		<spring:message code="rule.data.auto.job.statistics.batch.id" var="tooltip" ></spring:message>
         		<div class="table-cell-label">${label}</div>
				<div class="input-group">
					<spring:message code="rule.data.mgmt.table.name" var="tooltip" ></spring:message>
					<input name="batchId" type="text" id="batchId" class="form-control table-cell input-sm" onkeydown="isNumericOnKeyDown(event)" data-toggle="tooltip" tabindex="1" data-placement="bottom" title="${tooltip}" >
					<span  class="input-group-addon add-on last"> 
						<i class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="bottom"></i>
					</span>
				</div>
			</div>
		</div>
		<div class="col-md-6 inline-form" style="padding-left: 0px !important;">
			<div class="form-group">
         		<spring:message code="rule.data.auto.job.statistics.process" var="label" ></spring:message>
         		<spring:message code="rule.data.auto.job.statistics.process" var="tooltip" ></spring:message>
         		<div class="table-cell-label">${label}</div>
             	<div class="input-group">
             		<select name="process" class="form-control table-cell input-sm"  tabindex="3" id="autoReloadUploadViewStatus" data-toggle="tooltip" data-placement="bottom"  title="${tooltip}">
             		<c:forEach items="${PROCESS_ENUM}" var="processEnum">
						<option value="${processEnum.value}">${processEnum}</option>
					</c:forEach>
             		</select>
             		<span class="input-group-addon add-on last" > 
             			<i class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="left" title=""></i>
             		</span>
             	</div>
            </div>
		</div>
		<div class="fullwidth" id="advanceSearchLink">
			<div class="">
				<a href="#" onclick="showAdvanceSearchAttribute('advance');"><spring:message code="rule.data.auto.job.statistics.advance.search"></spring:message></a> 
			</div>
		</div>
		
		
		<!-- This division Contains Advance Search Parameters By Vipin Suman-->
		<!-- JIRA Id MED-6589  -->
		<!-- Advance Search Parameters Division Start  -->
		<div id="advanceSearchDivision" style="display: none;">
			<div class="col-md-6 inline-form"
				style="padding-left: 0px !important;">
				<div class="form-group">
					<spring:message code="rule.data.mgmt.table.name" var="tooltip" ></spring:message>
					<spring:message code="rule.data.mgmt.table.name" var="label" ></spring:message>
					<div class="table-cell-label">${label}</div>
					<div class="input-group">
						<select name="viewName" class="form-control table-cell input-sm" tabindex="3" id="ruleLookupTableData" data-toggle="tooltip" data-placement="bottom" title="${tooltip}">
							<option value="-1">Select Table Name</option>
							<c:forEach items="${tableList}" var="tableList">
								<option value="${tableList.viewName}">${tableList.viewName}</option>
							</c:forEach>
						</select> <span class="input-group-addon add-on last"> <i
							class="glyphicon glyphicon-alert" data-toggle="tooltip"
							data-placement="left" title=""></i>
						</span>
					</div>
				</div>
			</div>
			<div class="col-md-6 inline-form"
				style="padding-left: 0px !important;">
				<div class="form-group">
					<spring:message code="rule.data.auto.job.statistics.status" var="tooltip" ></spring:message>
					<spring:message code="rule.data.auto.job.statistics.status" var="label" ></spring:message>
					<div class="table-cell-label">${label}</div>
					<div class="input-group">
						<select name="status" class="form-control table-cell input-sm" tabindex="3" id="viewStatus" data-toggle="tooltip" data-placement="bottom" title="${tooltip}">
							<option value="-1">Select Status</option>
							<c:forEach items="${AUTO_OPERATION_STATUS_ENUM}" var="AUTO_OPERATION_STATUS_ENUM">
								<c:if test="${AUTO_OPERATION_STATUS_ENUM ne 'ALL' and  AUTO_OPERATION_STATUS_ENUM ne 'INPROGRESS'}">
									<option value="${AUTO_OPERATION_STATUS_ENUM}">${AUTO_OPERATION_STATUS_ENUM}</option>
								</c:if>
							</c:forEach>
						</select> 
						<span class="input-group-addon add-on last"> 
							<i class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="left" title=""></i>
						</span>
					</div>
				</div>
			</div>
			<div class="col-md-6 inline-form"
				style="padding-left: 0px !important;">
				<div class="form-group">
					<spring:message code="rule.data.auto.job.statistics.excecution.start" var="label" ></spring:message>
					<spring:message code="rule.data.auto.job.statistics.excecution.start" var="tooltip" ></spring:message>
					<div class="table-cell-label">${label}</div>
					<div class="input-group input-append date" id="fromDatePicker">
						<input id="fromDate" readonly class="form-control table-cell input-sm" style="background-color: #ffffff" data-toggle="tooltip" data-placement="bottom" tabindex="6" title="${tooltip}" /> 
							<span class="input-group-addon add-on last"> 
								<i id="fromDate_error" class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="left" title=""></i>
						</span>
					</div>
				</div>
			</div>
			<div class="col-md-6 inline-form"
				style="padding-left: 0px !important;">
				<div class="form-group">
					<spring:message code="rule.data.auto.job.statistics.excecution.end" var="label" ></spring:message>
					<spring:message code="rule.data.auto.job.statistics.excecution.end" var="tooltip" ></spring:message>
					<div class="table-cell-label">${label}</div>
					<div class="input-group input-append date" id="toDatePicker">
						<input name="toDate" id="toDate" readonly class="form-control table-cell input-sm" style="background-color: #ffffff" data-toggle="tooltip" data-placement="left" tabindex="7" title="${tooltip}" /> 
							<span class="input-group-addon add-on last"> 
								<i id="toDate_error" class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="left" title=""></i>
						</span>
					</div>
				</div>
			</div>
			<div class="col-md-6 inline-form"
				style="padding-left: 0px !important;">
				<div class="form-group" id="autoReloadScheduleType">
					<spring:message code="rule.data.auto.job.statistics.scheduler.type" var="label" ></spring:message>
					<spring:message code="rule.data.auto.job.statistics.scheduler.type" var="tooltip" ></spring:message>
					<div class="table-cell-label">${label}</div>
					<div class="input-group">
						<select name="scheduleType" class="form-control table-cell input-sm" tabindex="3" id="scheduleType" data-toggle="tooltip" data-placement="bottom" title="${tooltip}">
							<option value="-1">Select Scheduler Type</option>
							<c:forEach items="${TRIGGER_TYPE_ENUM}" var="TRIGGER_TYPE_ENUM">
								<c:if test="${TRIGGER_TYPE_ENUM ne 'SELECT'}">
									<option value="${TRIGGER_TYPE_ENUM}">${TRIGGER_TYPE_ENUM}</option>
								</c:if>
							</c:forEach>
						</select> <span class="input-group-addon add-on last"> 
							<i class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="left" title=""></i>
						</span>
					</div>
				</div>
			</div>
			<div class="col-md-6 inline-form"
				style="padding-left: 0px !important;" id="autoJobSchedulerName">
				<div class="form-group">
					<spring:message code="rule.data.auto.job.statistics.scheduler.name" var="label" ></spring:message>
					<spring:message code="rule.data.auto.job.statistics.scheduler.name" var="tooltip" ></spring:message>
					<div class="table-cell-label">${label}</div>
					<div class="input-group">
						<select name="scheduler" class="form-control table-cell input-sm" tabindex="3" id="viewScheduler" data-toggle="tooltip" data-placement="bottom" title="${tooltip}">
							<option value="-1">Select Scheduler Name</option>
							<c:forEach items="${schedulerNameList}" var="schedulerNameList">
								<option value="${schedulerNameList.triggerName}">${schedulerNameList.triggerName}</option>
							</c:forEach>
						</select> 
						<span class="input-group-addon add-on last"> 
							<i class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="left" title=""></i>
						</span>
					</div>
				</div>
			</div>
			<div id="autoUploadSourceDirectory" style="display: none;">
				<div class="col-md-6 inline-form"
					style="padding-left: 0px !important;">
					<div class="form-group">
						<spring:message code="rule.data.auto.job.statistics.action" var="label" ></spring:message>
						<spring:message code="rule.data.auto.job.statistics.action" var="tooltip" ></spring:message>
						<div class="table-cell-label">${label}</div>
						<div class="input-group">
							<select name="action" class="form-control table-cell input-sm" tabindex="3" id="viewStatusAction" data-toggle="tooltip" data-placement="bottom" title="${tooltip}">
								<option value="-1">Select Action</option>
								<c:forEach items="${jobActionTypeEnum}" var="jobActionTypeEnum">
									<option value="${jobActionTypeEnum}">${jobActionTypeEnum}</option>
								</c:forEach>
							</select> 
							<span class="input-group-addon add-on last"> 
								<i class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="left" title=""></i>
							</span>
						</div>
					</div>
				</div>
				<div class="col-md-6 inline-form"
					style="padding-left: 0px !important;">
					<div class="form-group">
						<spring:message code="rule.data.auto.job.statistics.source.directory" var="label" ></spring:message>
						<spring:message code="rule.data.auto.job.statistics.source.directory" var="tooltip" ></spring:message>
						<div class="table-cell-label">${label}</div>
						<div class="input-group">
							<input name="sourceDirectory" type="text" id="sourceDirectory" class="form-control table-cell input-sm" data-toggle="tooltip" tabindex="1" data-placement="bottom" title="${tooltip}">
							<span class="input-group-addon add-on last"> 
							<i data-placement="bottom"></i>
							</span>
						</div>
					</div>
				</div>
			</div>
		</div>

		<div class="fullwidth" id="basicSearchLink" style="display: none;">
			<div class="">
				<a href="#" onclick="showAdvanceSearchAttribute('basic');"><spring:message code="rule.data.auto.job.statistics.basic.search"></spring:message></a> 
			</div>
		</div>
		<!-- This division Contains Advance Search Parameters By Vipin Suman-->
		<!-- JIRA Id MED-6589  -->
		<!-- Advance Search Parameters Division End  -->
		
		
		
		
		<!-- This Division For Search Or Reset Button For Search Criteria Start-->
		<div class="clearfix"></div>
		<div class="pbottom15 mtop10">
			<button id='search' class="btn btn-grey btn-xs" onclick="searchAutoJobProcess();" tabindex="5">
				<spring:message code="btn.label.search" ></spring:message>
			</button>
			<button class="btn btn-grey btn-xs" onclick="resetSearchRuleCriteria();reloadAutoReloadGridData('false');" tabindex="6">
				<spring:message code="btn.label.reset" ></spring:message>
			</button>
		</div>
		<!-- This Division For Search Or Reset Button For Search Criteria End-->


		<!-- This Grid for Auto Reload View Status  By Vipin Suman-->
		<!-- JIRA Id MED-6589  -->
		<!-- Auto Upload Reload Grid Start  -->
		<div class="fullwidth" id="autoReloadViewStatusGrid" style="display: block;">
			<div class="title2"> <spring:message code="rule.data.auto.job.statistics.reload.view.status.list"></spring:message> </div>
			<div class="box-body table-responsive no-padding box">
				<table class="table table-hover" id="autoReloadGrid"></table>
				<div id="autoReloadGridPagingDiv"></div>
			</div>
		</div>
		<!-- This Grid for Auto Reload View Status  -->
		<!-- JIRA Id MED-6589  -->
		<!-- Auto Upload Reload Grid End  -->
		
		
		
		<!-- This Grid for Auto Upload View Status  By Vipin Suman-->
		<!-- JIRA Id MED-6589  -->
		<!-- Auto Upload Upload Grid Start  -->		
		<div class="fullwidth" id="autoUploadViewStatusGrid" style="display: none;">
			<div class="title2"> <spring:message code="rule.data.auto.job.statistics.upload.view.status.list"></spring:message> </div>
			<div class="box-body table-responsive no-padding box">
				<table class="table table-hover" id="autoUploadGrid"></table>
				<div id="autoUploadGridPagingDiv"></div>
			</div>
		</div>
		<!-- This Grid for Auto Upload View Status  -->
		<!-- JIRA Id MED-6589  -->
		<!-- Auto Upload Upload Grid End  -->
		
		<!-- View Batch details pop- up code start here -->
		<a href="#divViewReloadBatchDetails" class="fancybox" style="display: none;" id="viewReloadBatchDetails">#</a>
		<div id="divViewReloadBatchDetails" style="display: none;">
			<div class="modal-content">
				<div class="modal-header padding" id="autoReloadViewDetailHeader">
					<h4 class="modal-title"><spring:message code="rule.data.auto.job.statistics.reload.batch.details"></spring:message></h4>
				</div>
				<div class="modal-header padding" id="autoUploadViewDetailHeader" style="display: none">
					<h4 class="modal-title"><spring:message code="rule.data.auto.job.statistics.upload.batch.details"></spring:message></h4>
				</div>
				<div class="modal-body padding inline-form">
					<div class="col-md-6 no-padding">
						<div class="form-group">
							<div class="table-cell-label"><spring:message code="rule.data.auto.job.statistics.batch.id" ></spring:message><span class="padding">:</span>
							</div>
							<div class="input-group " id="view_batch_id"></div>
						</div>
					</div>
					<div class="col-md-6 no-padding">
						<div class="form-group">
							<div class="table-cell-label"><spring:message code="rule.data.auto.job.statistics.excecution.start"></spring:message><span class="padding">:</span>
							</div>
							<div class="input-group " id="view_execution_start"></div>
						</div>
					</div>
					<div class="col-md-6 no-padding">
						<div class="form-group">
							<div class="table-cell-label"><spring:message code="rule.data.auto.job.statistics.excecution.end"></spring:message><span class="padding">:</span>
							</div>
							<div class="input-group " id="view_execution_end"></div>
						</div>
					</div>
					<div class="col-md-6 no-padding">
						<div class="form-group">
							<div class="table-cell-label"><spring:message code="rule.data.auto.job.statistics.scheduler.name"></spring:message><span class="padding">:</span>
							</div>
							<div class="input-group " id="view_scheduler"></div>
						</div>
					</div>
					<div class="col-md-6 no-padding">
						<div class="form-group">
							<div class="table-cell-label"><spring:message code="rule.data.auto.job.statistics.status"></spring:message><span class="padding">:</span>
							</div>
							<div class="input-group " id="view_status"></div>
						</div>
					</div>
					<div class="col-md-6 no-padding" id="autoUploadTableName">
						<div class="form-group">
							<div class="table-cell-label"><spring:message code="rule.data.mgmt.table.name"></spring:message><span class="padding">:</span>
							</div>
							<div class="input-group " id="view_table_name"></div>
						</div>
					</div>
					<div class="col-md-6 no-padding" id="autoReloadServerInstance">
						<div class="form-group">
							<div class="table-cell-label"><spring:message code="rule.data.auto.job.statistics.server.instance"></spring:message><span class="padding">:</span>
							</div>
							<div class="input-group " id="view_server_instance"></div>
						</div>
					</div>
					<div id="autoUploadViewFields" style="display: block;">
					<div class="col-md-6 no-padding" id="">
						<div class="form-group">
							<div class="table-cell-label"><spring:message code="rule.data.auto.job.statistics.source.directory"></spring:message><span class="padding">:</span>
							</div>
							<div class="input-group " id="view_source_directory"></div>
						</div>
					</div>
					<div class="col-md-6 no-padding" id="">
						<div class="form-group">
							<div class="table-cell-label"><spring:message code="rule.data.auto.job.statistics.action"></spring:message><span class="padding">:</span>
							</div>
							<div class="input-group " id="view_action"></div>
						</div>
					</div>
					<div class="col-md-6 no-padding" id="">
						<div class="form-group">
							<div class="table-cell-label"><spring:message code="rule.data.auto.job.statistics.file.prefix"></spring:message><span class="padding">:</span>
							</div>
							<div class="input-group " id="view_file_prefix"></div>
						</div>
					</div>
					<div class="col-md-6 no-padding" id="">
						<div class="form-group">
							<div class="table-cell-label"><spring:message code="rule.data.auto.job.statistics.file.contains"></spring:message><span class="padding">:</span>
							</div>
							<div class="input-group " id="view_file_contains"></div>
						</div>
					</div>
					</div>
					<div class="col-md-6 no-padding" id="reasonDiv">
						<div class="form-group">
							<div class="table-cell-label"><spring:message code="rule.data.auto.job.statistics.reason"></spring:message><span class="padding">:</span></div>
							<div class="input-group " id="view_reason"></div>
						</div>
					</div>
					<div class="col-md-12 " >
						<div class="col-md-6" style="text-align: center;" id="autoUploadFilesCount" >
							<strong><spring:message code="rule.data.auto.job.statistics.files.count"></spring:message></strong>
							<center><table class="table table-hover table-bordered" border="1" id="autoUploadFilesCountTable">
							<tr>
								<th><spring:message code="rule.data.auto.job.statistics.total"></spring:message></th>
								<th><spring:message code="rule.data.auto.job.statistics.success"></spring:message></th>
								<th><spring:message code="rule.data.auto.job.statistics.failed"></spring:message></th>
							</tr>
						</table>
						</center>
						</div>
						<div class="col-md-6" style="text-align: center;" id="autoUploadRecordCount">
							<strong><spring:message code="rule.data.auto.job.statistics.records.count"></spring:message></strong>
							<center><table class="table table-hover table-bordered" border="1" id="autoUploadRecordDataTable">
							<tr>
								<th><spring:message code="rule.data.auto.job.statistics.total"></spring:message></th>
								<th><spring:message code="rule.data.auto.job.statistics.success"></spring:message></th>
								<th id="uploadDuplicate"><spring:message code="rule.data.auto.job.statistics.duplicate"></spring:message></th>
								<th id="uploadUpdate" style="display: none;"><spring:message code="rule.data.auto.job.statistics.update"></spring:message></th>
								<th><spring:message code="rule.data.auto.job.statistics.failed"></spring:message></th>
							</tr>
						</table>
						</center>
						</div>
						<div class="col-md-6" style="text-align: center;" id="autoReloadRecordCount">
							<strong><spring:message code="rule.data.auto.job.statistics.records.count"></spring:message></strong>
							<center><table class="table table-hover table-bordered" border="1" id="autoReloadRecordCountTable">
							<tr>
								<th><spring:message code="rule.data.auto.job.statistics.query"></spring:message></th>
								<th><spring:message code="rule.data.auto.job.statistics.success"></spring:message></th>
								<th><spring:message code="rule.data.auto.job.statistics.failed"></spring:message></th>
							</tr>
						</table>
						</center>
						</div>
					</div>

				</div>
				<div id="import-config-buttons" class="modal-footer padding">
					<button type="button" id="view-batch-details-close-btn" class="btn btn-grey btn-xs " data-dismiss="modal" onclick="closeFancyBox();">
						<spring:message code="rule.data.auto.job.statistics.close"></spring:message>
					</button>
				</div>
			</div>
		</div>
	</div>
</div>

<script>

function resetSearchRuleCriteria(){
	$("#autoReloadUploadViewStatus").val("AutoReloadCache");
	$("#autoReloadViewStatusGrid").show();
	$("#autoUploadViewStatusGrid").hide();
	$("#autoReloadGrid").show();
	$("#autoUploadGrid").hide();
	$("#ruleLookupTableData").val("-1");
	$("#viewStatus").val("-1");
	$("#fromDatePicker").datepicker('setDate', new Date());
    $("#toDatePicker").datepicker('setDate', new Date()); 	
	$("#scheduleType").val("-1");
	$("#viewScheduler").val("-1");
	$("#viewStatusAction").val("-1");
	$("#sourceDirectory").val("");
	$("#batchId").val("");
	$("#autoUploadSourceDirectory").hide();
}

function searchAutoJobProcess(){
	if($("#autoReloadUploadViewStatus").val() == "AutoUpload"){
		reloadAutoUploadGridData(true);
		$("#autoReloadGrid").hide();
		$("#autoUploadGrid").show();
		$("#autoReloadViewStatusGrid").hide();
		$("#autoUploadViewStatusGrid").show();
	} else {
		reloadAutoReloadGridData(true);
		$("#autoReloadGrid").show();
		$("#autoUploadGrid").hide();
		$("#autoReloadViewStatusGrid").show();
		$("#autoUploadViewStatusGrid").hide();
	}
}

function viewRecordsInit(id,jobProcess){
	var rowData = "";
	if(jobProcess == 'AutoUpload'){
		$("#autoUploadViewDetailHeader").show();
		$("#autoReloadViewDetailHeader").hide();
		rowData = jQuery("#autoUploadGrid").jqGrid('getRowData', id);
	}
	else{
		$("#autoUploadViewDetailHeader").hide();
		$("#autoReloadViewDetailHeader").show();
		rowData = jQuery("#autoReloadGrid").jqGrid('getRowData', id);
	}
	$("#view_batch_id").html(rowData.batchId);
	$("#view_execution_start").html(rowData.fromDate);
	$("#view_execution_end").html(rowData.toDate);
	$("#view_scheduler").html(rowData.schedulerName);
	$("#view_status").html(rowData.jobStatus);
	$("#view_table_name").html(rowData.viewName);
	if(rowData.jobStatus=="Completed"){
		$("#reasonDiv").hide();
	}else{
		$("#reasonDiv").show();
		$("#view_reason").html(rowData.reason);
	}
	if(rowData.jobProcess == "AutoUpload"){
		$("#autoReloadServerInstance").hide();
		$("#autoUploadViewFields").show();
		$("#autoUploadFilesCount").show();
		$("#autoUploadRecordCount").show();
		$("#autoReloadRecordCount").hide();
		$("#autoReloadRecordCount").removeClass();
		$("#view_source_directory").html(rowData.sourceDirectory);
		$("#view_scheduler").html(rowData.schedulerName);
		$("#view_action").html(rowData.jobAction);
		$("#view_file_prefix").html(rowData.filePrefix);
		if(rowData.jobAction == 'update'){
			$("#uploadDuplicate").hide();
			$("#uploadUpdate").show();
		}else {
			$("#uploadDuplicate").show();
			$("#uploadUpdate").hide();
		}
		$("#autoUploadFilesCountTable").find("tr:gt(0)").remove();
		$("#autoUploadRecordDataTable").find("tr:gt(0)").remove();
		$("#autoUploadFilesCountTable").append("<tr style='text-align:center;'><td>"+rowData.fileTotal+"</td><td>"+rowData.fileSuccess+"</td><td>"+rowData.fileFailed+"</td></tr>");
		$("#autoUploadRecordDataTable").append("<tr style='text-align:center;'><td>"+rowData.recordTotal+"</td><td>"+rowData.recordSuccess+"</td><td>"+rowData.duplicateRecord+"</td><td>"+rowData.recordFailed+"</td></tr>");
	}else{
		$("#view_server_instance").html(rowData.serverInstance);
		$("#autoReloadServerInstance").show();
		$("#autoUploadViewFields").hide();
		$("#autoUploadFilesCount").hide();
		$("#autoUploadRecordCount").hide();
		$("#autoReloadRecordCount").removeClass().addClass("col-md-12");
		$("#autoReloadRecordCount").show();
		var reloadQueryArr = rowData.reloadQuery.split(",");
		var reloadQueryStatusArr = rowData.reloadQueryStatus.split(",");
		var reloadQueryCountArr = rowData.reloadRecordCount.split(",");
		$("#autoReloadRecordCountTable").find("tr:gt(0)").remove();
		for (var i = 0; i < reloadQueryArr.length; i++) {
			if(reloadQueryStatusArr[i] == 'SUCCESS' || reloadQueryStatusArr[i] == 'Success')
		    	$("#autoReloadRecordCountTable").append("<tr style='text-align:left;'><td>"+reloadQueryArr[i]+"</td><td>"+reloadQueryCountArr[i]+"</td><td>0</td></tr>");
			else if(reloadQueryStatusArr[i] == 'FAILED' || reloadQueryStatusArr[i] == 'Failed')
				$("#autoReloadRecordCountTable").append("<tr style='text-align:left;'><td>"+reloadQueryArr[i]+"</td><td>0</td><td>"+reloadQueryCountArr[i]+"</td></tr>");
		}
	}
	$("#viewReloadBatchDetails").click();
}

function showAdvanceSearchAttribute(searchType){
	if(searchType == 'advance'){
		$("#advanceSearchLink").hide();
		$("#advanceSearchDivision").show();
		$("#basicSearchLink").show();
		$("#fromDatePicker").datepicker('setDate', new Date());
	    $("#toDatePicker").datepicker('setDate', new Date()); 
	} else{
		$("#advanceSearchLink").show();
		$("#advanceSearchDivision").hide();
		$("#basicSearchLink").hide();
		$("#ruleLookupTableData").val("-1");
		$("#viewStatus").val("-1");
		$("#fromDate").val("");
		$("#toDate").val("");
		$("#scheduleType").val("-1");
		$("#viewScheduler").val("-1");
		$("#viewStatusAction").val("-1");
		$("#sourceDirectory").val("");
	}
}

function initializeFromAndToDate() {
    
    $("#fromDatePicker").datepicker('setDate', new Date());
    $("#toDatePicker").datepicker('setDate', new Date()); 
	
}

$(document).ready(function() {
	initializeFromAndToDate()
	loadAutoReloadGrid(false);
	reloadAutoReloadGridData(false);
	loadAutoUploadGrid(false);
	reloadAutoUploadGridData(false);
	showAdvanceSearchAttribute('basic');
});

$("#autoReloadUploadViewStatus" ).change(function() {
	  if($("#autoReloadUploadViewStatus").val() == "AutoReloadCache") {
		  //$("#autoUploadAction").hide();
		  $("#autoUploadSourceDirectory").hide();
		  $("#viewStatusAction").val("-1");
		  $("#sourceDirectory").val("");
	  } else {
		  //$("#autoUploadAction").show();
		  $("#autoUploadSourceDirectory").show();
		  $("#scheduleType").val("-1");
	  }
});

function loadAutoReloadGrid(isSearch){
	$("#autoReloadGrid").jqGrid({
		url: "<%= ControllerConstants.VIEW_AUTO_JOB_LIST %>",
		postData : {
			'batchId' : function() {
				var id = $("#batchId").val();
				if (id =='undefined' || id == '' || id == '-1' ) {
					id = 0;
				}
				return id;
			},
			'process' : function() {
				return $("#autoReloadUploadViewStatus").val();
			},
			'sourceDirectory' : function() {
				return $("#sourceDirectory").val();
			},
			'action' : function() {
				return $("#viewStatusAction").val();
			},
			'scheduleType' : function() {
				return $("#scheduleType").val();
			},
			'viewName' : function() {
				return $("#ruleLookupTableData").val();
			},
			'scheduler' : function() {
				return $("#viewScheduler").val();
			},
			'status' : function() {
				return $("#viewStatus").val();
			},
			'fromDate' : function() {
				return $("#fromDate").val();
			},
			'toDate' : function() {
				return $("#toDate").val();
			},
			'isSearch' : isSearch
		},
		datatype: "json",
		colNames: [
					"#",
					"Batch Id",
					"<spring:message code='rule.data.mgmt.table.name'></spring:message>",
					"<spring:message code='auto.reload.cache.config.server.instance'></spring:message>",
					"Scheduler Name",
					"Status",
					"Execution Date",
					"Details",
					"#",
					"#",
					"#",
					"#",
					"#",
					"#",
					"#",
					"#",
					"#",
					"#",
					"#",
					"#",
					"#",
					"#",
					"#",
					"#"
					
		],
		colModel: [			
					{name:'id',index:'id',hidden:true},
					{name:'batchId',index:'batchId',align:'center',sortable: false},
					{name:'viewName',index:'viewName',align:'center',sortable: false},
					{name:'serverInstance',index:'serverInstance',align:'center',sortable: false},
					{name:'schedulerName',index:'schedulerName',align:'center',sortable:false},
					{name:'jobStatus',index:'jobStatus',align:'center',sortable:false},
					{name:'fromDate',index:'fromDate',align:'center',sortable:false},
					{name:'viewDetails',index:'viewDetails',align:'center',formatter:viewDetailsFormatter,sortable:false},
					{name:'jobProcess',index:'jobProcess',hidden:true},
					{name:'jobAction',index:'jobAction',hidden:true},
					{name:'scheduler',index:'scheduler',hidden:true},
					{name:'fileTotal',index:'fileTotal',hidden:true},
					{name:'toDate',index:'toDate',hidden:true},
					{name:'fileSuccess',index:'fileSuccess',hidden:true},
					{name:'fileFailed',index:'fileFailed',hidden:true},
					{name:'recordTotal',index:'recordTotal',hidden:true},
					{name:'recordSuccess',index:'recordSuccess',hidden:true},
					{name:'duplicateRecord',index:'duplicateRecord',hidden:true},
					{name:'recordFailed',index:'recordFailed',hidden:true},
					{name:'reloadQueryStatus',index:'reloadQueryStatus',hidden:true},
					{name:'reloadRecordCount',index:'reloadRecordCount',hidden:true},
					{name:'reloadQuery',index:'reloadQuery',hidden:true},
					{name:'sourceDirectory',index:'sourceDirectory',hidden:true},
					{name:'reason',index:'reason',hidden:true}
		],
		rowNum:<%= MapCache.getConfigValueAsInteger(SystemParametersConstant.TOTAL_ROWS_TO_DISPLAY_IN_GRID, 10) %>,
		rowList:[10,20,50,100],
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

function loadAutoUploadGrid(isSearch){
	$("#autoUploadGrid").jqGrid({
		url: "<%= ControllerConstants.VIEW_AUTO_JOB_LIST %>",
		postData : {
			'batchId' : function() {
				var id = $("#batchId").val();
				if (id =='undefined' || id == '' || id == '-1' ) {
					id = 0;
				}
				return id;
			},
			'process' : function() {
				return $("#autoReloadUploadViewStatus").val();
			},
			'sourceDirectory' : function() {
				return $("#sourceDirectory").val();
			},
			'action' : function() {
				return $("#viewStatusAction").val();
			},
			'scheduleType' : function() {
				return $("#scheduleType").val();
			},
			'viewName' : function() {
				return $("#ruleLookupTableData").val();
			},
			'scheduler' : function() {
				return $("#viewScheduler").val();
			},
			'status' : function() {
				return $("#viewStatus").val();
			},
			'fromDate' : function() {
				return $("#fromDate").val();
			},
			'toDate' : function() {
				return $("#toDate").val();
			},
			'isSearch' : isSearch
		},
		datatype: "json",
		colNames: [
					"#",
					"Batch Id",
					"<spring:message code='rule.data.mgmt.table.name'></spring:message>",
					"Source Directory",
					"Scheduler Name",
					"Status",
					"Execution Date",
					"Details",
					"#",
					"#",
					"#",
					"#",
					"#",
					"#",
					"#",
					"#",
					"#",
					"#",
					"#",
					"#",
					"#",
					"#",
					"#",
					"#",
					"#"
		],
		colModel: [			
					{name:'id',index:'id',hidden:true},
					{name:'batchId',index:'batchId',align:'center',sortable: false},
					{name:'viewName',index:'viewName',align:'center',sortable: false},
					{name:'sourceDirectory',index:'sourceDirectory',align:'center',sortable: false},
					{name:'schedulerName',index:'schedulerName',align:'center',sortable:false},
					{name:'jobStatus',index:'jobStatus',align:'center',sortable:false},
					{name:'fromDate',index:'fromDate',align:'center',sortable:false},
					{name:'viewDetails',index:'viewDetails',align:'center',formatter:viewDetailsFormatter,sortable:false},
					{name:'jobProcess',index:'jobProcess',hidden:true},
					{name:'jobAction',index:'jobAction',hidden:true},
					{name:'scheduler',index:'scheduler',hidden:true},
					{name:'fileTotal',index:'fileTotal',hidden:true},
					{name:'toDate',index:'toDate',hidden:true},
					{name:'fileSuccess',index:'fileSuccess',hidden:true},
					{name:'fileFailed',index:'fileFailed',hidden:true},
					{name:'recordTotal',index:'recordTotal',hidden:true},
					{name:'recordSuccess',index:'recordSuccess',hidden:true},
					{name:'duplicateRecord',index:'duplicateRecord',hidden:true},
					{name:'recordFailed',index:'recordFailed',hidden:true},
					{name:'reloadQueryStatus',index:'reloadQueryStatus',hidden:true},
					{name:'reloadRecordCount',index:'reloadRecordCount',hidden:true},
					{name:'reloadQuery',index:'reloadQuery',hidden:true},
					{name:'sourceDirectory',index:'sourceDirectory',hidden:true},
					{name:'filePrefix',index:'filePrefix',hidden:true},
					{name:'reason',index:'reason',hidden:true}
		],
		rowNum:<%= MapCache.getConfigValueAsInteger(SystemParametersConstant.TOTAL_ROWS_TO_DISPLAY_IN_GRID, 10) %>,
		rowList:[10,20,50,100],
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
				var count = jQuery("#autoUploadGrid").jqGrid('getGridParam', 'reccount');
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


function viewDetailsFormatter(cellvalue, options, rowObject){
	var id=rowObject.viewName+"_viewRecords";
	return '<a id="'+id+'" href="#" onclick="viewRecordsInit('+"'" + rowObject["id"]+ "'"+','+"'" + rowObject["jobProcess"]+ "'"+');"><i class="fa fa-fw fa-vimeo-square"></i></a>';
}

function reloadAutoReloadGridData(isSearch){
	clearAllMessages();
	if(!validateSearchCriteria())
		return false;
	var $grid = $("#autoReloadGrid");
	jQuery('#autoReloadGrid').jqGrid('clearGridData');
	clearAutoReloadGrid();
	$grid.jqGrid('setGridParam',{datatype:'json',page: 1,sortname: 'id',sortorder: "desc",postData:{
		'batchId' : function() {
			var id = $("#batchId").val();
			if (id =='undefined' || id == '' || id == '-1' ) {
				id = 0;
			}
			return id;
		},
		'process' : function() {
			return $("#autoReloadUploadViewStatus").val();
		},
		'sourceDirectory' : function() {
			return $("#sourceDirectory").val();
		},
		'action' : function() {
			return $("#viewStatusAction").val();
		},
		'scheduleType' : function() {
			return $("#scheduleType").val();
		},
		'viewName' : function() {
			return $("#ruleLookupTableData").val();
		},
		'scheduler' : function() {
			return $("#viewScheduler").val();
		},
		'status' : function() {
			return $("#viewStatus").val();
		},
		'fromDate' : function() {
			return $("#fromDate").val();
		},
		'toDate' : function() {
			return $("#toDate").val();
		},
		'isSearch' : isSearch
	}}).trigger('reloadGrid');
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

function reloadAutoUploadGridData(isSearch){
	clearAllMessages();
	if(!validateSearchCriteria())
		return false;
	var $grid = $("#autoUploadGrid");
	jQuery('#autoUploadGrid').jqGrid('clearGridData');
	clearAutoUploadGrid();
	$grid.jqGrid('setGridParam',{datatype:'json',page: 1,sortname: 'id',sortorder: "desc",postData:{
		'batchId' : function() {
			var id = $("#batchId").val();
			if (id =='undefined' || id == '' || id == '-1' ) {
				id = 0;
			}
			return id;
		},
		'process' : function() {
			return $("#autoReloadUploadViewStatus").val();
		},
		'sourceDirectory' : function() {
			return $("#sourceDirectory").val();
		},
		'action' : function() {
			return $("#viewStatusAction").val();
		},
		'scheduleType' : function() {
			return $("#scheduleType").val();
		},
		'viewName' : function() {
			return $("#ruleLookupTableData").val();
		},
		'scheduler' : function() {
			return $("#viewScheduler").val();
		},
		'status' : function() {
			return $("#viewStatus").val();
		},
		'fromDate' : function() {
			return $("#fromDate").val();
		},
		'toDate' : function() {
			return $("#toDate").val();
		},
		'isSearch' : isSearch
	}}).trigger('reloadGrid');
}

function clearAutoUploadGrid(){
	$grid = $("#autoUploadGrid");
	var rowIds = $grid.jqGrid('getDataIDs');
	// iterate through the rows and delete each of them
	for(var i=0,len=rowIds.length;i<len;i++){
		var currRow = rowIds[i];
		$grid.jqGrid('delRowData', currRow);
	}
}

function validateSearchCriteria(){
	clearAllMessagesPopUp();
	var startDate = Date.parse(new Date($("#fromDate").val()));
	var endDate = Date.parse($("#toDate").val());
	var todayDate = Date.parse(new Date());
	if(todayDate < startDate){
	    showErrorMsgPopUp("Start Date Can't be future date");
	    return false;
	}
	if(todayDate < endDate){
	    showErrorMsgPopUp("End Date Can't be future date");
	    return false;
	}
	if (startDate > endDate){
	    showErrorMsgPopUp('Start date should be less than End date.');
	    return false;
	}
	
	return true;
	
}

</script>
