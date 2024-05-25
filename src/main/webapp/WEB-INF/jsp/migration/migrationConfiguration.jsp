<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<%@page
	import="com.elitecore.sm.common.constants.SystemParametersConstant"%>
<%@page import="com.elitecore.sm.util.MapCache"%>
<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib uri="http://www.springframework.org/security/tags"
	prefix="sec"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="/WEB-INF/ShowErrorTag.tld" prefix="elitecoreError"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/bootstrap-multiselect.js"></script>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/bootstrap-multiselect.css"
	type="text/css" />
<style>
.checkbox {
	color: #000 !important;
}

.multiselect-container {
	width: 100% !important;
}

.btn .caret {
	margin-left: -10px;
}

.btn-group {
	width: 100% !important;
}

.btn-group button {
	width: 100% !important;
}

.btn-default span {
	width: 100% !important;
	background: none !important;
}

.special {
	font-weight: bold !important;
	color: #fff !important;
	background: #FE7B26 !important;
	text-transform: uppercase;
}

.dropdown-menu>.special active>a, .dropdown-menu>.special active>a:focus,
	.dropdown-menu>.special active>a:hover {
	background-color: #FE7B26 !important;
}

.dropdown-menu>.active>a, .dropdown-menu>.active>a:focus, .dropdown-menu>.active>a:hover
	{
	background-color: rgba(0, 0, 0, 0);
}

.special:active a {
	font-weight: bold !important;
	color: #fff !important;
	background: #FE7B26 !important;
	text-transform: uppercase;
}

.special active {
	font-weight: bold !important;
	color: #fff !important;
	background: #FE7B26 !important;
	text-transform: uppercase;
}

#services_div li.special :hover {
	background-color: #FE7B26 !important;
}

#services_div li.special :active {
	background-color: #FE7B26 !important;
}

#services_div li.active {
	background-color: #FE7B26;
}
</style>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<!-- Main Migration Configuration div start here -->

<div class="tab-content no-padding clearfix"
	id="migration-configuration-block">

	<!-- Utility check div -->
	<form name="check-utility-form" action="javascript:;"
		id="migration-configuration-form">

		<div class="title2">
			<spring:message code="migration.server.heading.label" ></spring:message>
		</div>

		<div class="fullwidth borbot">
			<div class="col-md-6 inline-form"
				style="padding-left: 0px !important;">

				<div class="form-group">
					<input type="hidden" id="migrationTrackId" name="migrationTrackId"
						value="0" />
					<spring:message code="migration.pruducttype.label" var="label" ></spring:message>
					<spring:message code="migration.pruducttype.label.tooltip"
						var="tooltip" ></spring:message>
					<div class="table-cell-label">${label}</div>
					<div class="input-group ">
						<select name="serverType" class="form-control table-cell input-sm"
							tabindex="1" id="serverType" data-toggle="tooltip"
							data-placement="bottom" title="${tooltip }"
							onchange="getServerListByServerType();">
							<c:forEach var="serverType" items="${SERVER_TYPE_LIST}">
								<option value="${serverType.id }">${serverType.name}</option>
							</c:forEach>
						</select> <span class="input-group-addon add-on last"> <i
							class="glyphicon glyphicon-alert" data-toggle="tooltip"
							data-placement="left" title=""></i></span>
					</div>
				</div>

				<div class="form-group">
					<spring:message code="migration.serverinstanceport.label"
						var="label" ></spring:message>
					<spring:message code="addServerInstance.instance.port.tooltip"
						var="tooltip" ></spring:message>
					<div class="table-cell-label">${label}</div>
					<div class="input-group ">
						<input type="text" id="serverInstancePort"
							name="serverInstancePort" maxlength="100"
							class="form-control table-cell input-sm" data-toggle="tooltip"
							data-placement="bottom" title="${tooltip }"
							onkeydown="isNumericOnKeyDown(event)" tabindex="4"> <span
							class="input-group-addon add-on last"> <i
							class="glyphicon glyphicon-alert" data-toggle="tooltip"
							data-placement="left" title=""></i></span>
					</div>
				</div>

				<div class="form-group">
					<spring:message code="migration.prefix.label" var="label" ></spring:message>
					<spring:message
						code="migration.serverInstance.naming.prefix.tooltip"
						var="tooltip" ></spring:message>
					<div class="table-cell-label">${label}</div>
					<div class="input-group ">
						<input type="text" id="serverInstancePrefix"
							name="serverInstancePrefix" maxlength="100"
							class="form-control table-cell input-sm" data-toggle="tooltip"
							data-placement="bottom" title="${tooltip }" tabindex="6">
						<span class="input-group-addon add-on last"> <i
							class="glyphicon glyphicon-alert" data-toggle="tooltip"
							data-placement="left" title=""></i></span>
					</div>
				</div>

			</div>

			<div class="col-md-6 inline-form"
				style="padding-left: 0px !important;">

				<div class="form-group" style="margin-bottom: 0;">
					<spring:message code="migration.server.label" var="label" ></spring:message>
					<spring:message code="migration.server.label.tooltip" var="tooltip" ></spring:message>
					<div class="table-cell-label">${label}</div>
					<div class="input-group ">
						<div class="col-md-10 no-padding">
							<div class="form-group">
								<div class="input-group ">
									<select name="servers" class="form-control table-cell input-sm"
										tabindex="2" id="server_id" data-toggle="tooltip"
										data-placement="bottom" title="${tooltip }" tabindex="2">

									</select> <span class="input-group-addon add-on last"> <i
										class="glyphicon glyphicon-alert" data-toggle="tooltip"
										data-placement="left" title=""></i></span>
								</div>
							</div>
						</div>
						<div class="col-md-2 no-padding">
							<div class="form-group">
								<div class="input-group ">
									<button class="btn btn-grey btn-xs"
										onclick="openCreateServerPopup();" tabindex="3">
										<spring:message code="btn.label.add" ></spring:message>
									</button>
								</div>
							</div>
						</div>

					</div>
				</div>

				<div class="form-group">
					<spring:message code="migration.startupscript.label" var="label" ></spring:message>
					<spring:message code="migration.serverInstance.script.name.tooltip"
						var="tooltip" ></spring:message>
					<div class="table-cell-label">${label}</div>
					<div class="input-group ">
						<input type="text" id="serverInstanceScriptName"
							name="serverInstanceScriptName" maxlength="100"
							class="form-control table-cell input-sm" data-toggle="tooltip"
							data-placement="bottom" title="${tooltip }" tabindex="5">
						<span class="input-group-addon add-on last"> <i
							class="glyphicon glyphicon-alert" data-toggle="tooltip"
							data-placement="left" title=""></i></span>
					</div>
				</div>


				<div class="form-group" id="services_div" style="display: none;">
					<spring:message code="migration.services.label" var="label" ></spring:message>
					<spring:message code="migration.services.label.tooltip"
						var="tooltip" ></spring:message>
					<div class="table-cell-label">${label}</div>
					<div class="input-group ">
						<select name="services_id"
							class="form-control table-cell input-sm" tabindex="3"
							multiple="multiple" id="services_id" data-toggle="tooltip"
							data-placement="bottom" title="${tooltip}">
						</select> <span class="input-group-addon add-on last"> <i
							id="serviceType_err" class="glyphicon glyphicon-alert"
							data-toggle="tooltip" data-placement="left" title=""></i></span>
					</div>
				</div>

			</div>

			<div class="clearfix"></div>
			<div class="pbottom15 form-group " id="migration-buttons-div">
				<button class="btn btn-grey btn-xs"
					onclick="populateServiceSlist();" id="populateServicesBtn"
					tabindex="3">
					<spring:message code="btn.label.populateServices" ></spring:message>
				</button>
				<button class="btn btn-grey btn-xs"
					onclick="saveMigrationTrackDetail();"
					id="saveMigrationTrackDetailBtn" style="display: none;"
					tabindex="7">
					<spring:message code="btn.label.add" ></spring:message>
				</button>
				<button class="btn btn-grey btn-xs"
					onclick="resetMigrationTrackDetailForm();"
					id="cancelUpdateMigrationTrackDetailBtn" style="display: none;"
					tabindex="8">
					<spring:message code="btn.label.cancel" ></spring:message>
				</button>
			</div>
			<div id="migration-progress-bar-div" class="input-group"
				style="display: none;">
				<jsp:include page="../common/processing-bar.jsp"></jsp:include>
			</div>
		</div>

	</form>
	<!-- Utility check Div end here -->

	<div class="tab-pane" id="serverInstance-configuration"
		style="display: none">
		<div class="tab-content no-padding">
			<div class="fullwidth mtop10" id="addServerInstanceSymb">
				<div class="title2">
					<spring:message code="serverManager.server.instance" ></spring:message>
					<span class="title2rightfield"> <span
						class="title2rightfield-icon1-text" id="addServerInstanceSymbol">
							<a href="#" onclick="addNewServerInstanceDetail();"><i
								class="fa fa-plus-circle"></i></a> <a href="#" id="addDriver"
							onclick="addNewServerInstanceDetail();"> <spring:message
									code="btn.label.add" ></spring:message></a>
					</span>

					</span>
				</div>
				<div class="clearfix"></div>
			</div>
			<div class="fullwidth">
				<div id="serverInstanceList"></div>
			</div>
		</div>
	</div>
</div>

<div id="divconfirmation" style="width: 100%; display: none;">
	<div class="modal-content">
		<div class="modal-header padding10">
			<h4 class="modal-title">
				<spring:message code="label.confirmation.header" ></spring:message>
			</h4>
		</div>
		<div class="modal-body padding10 inline-form">
			<p>
				<spring:message
					code="migration.grid.checkbox.confirmation.on.delete" ></spring:message>
			</p>
		</div>
		<div class="modal-footer padding10">
			<button type="button" class="btn btn-grey btn-xs "
				onclick="deleteMigrationTrackDetails();">
				<spring:message code="btn.label.yes" ></spring:message>
			</button>
			<button type="button" class="btn btn-grey btn-xs "
				data-dismiss="modal" onclick="closeFancyBox();">
				<spring:message code="btn.label.no" ></spring:message>
			</button>
		</div>
	</div>
	<!-- /.modal-content -->
</div>

<div id="divmessage" style="width: 100%; display: none;">
	<div class="modal-content">
		<div class="modal-header padding10">
			<h4 class="modal-title">
				<spring:message code="label.confirmation.header" ></spring:message>
			</h4>
		</div>
		<div class="modal-body padding10 inline-form">
			<p>
				<spring:message code="migration.grid.checkbox.validation.on.delete" ></spring:message>
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


<div class="fullwidth">
	<div class="title2">
		<spring:message code="migration.track.detail.label" ></spring:message>
		<span class="title2rightfield"> <span
			class="title2rightfield-icon1-text"> <a
				href="javascript: deleteMigration();" tabindex="9"><i
					class="fa fa-trash"></i></a> <a href="#" onclick="deleteMigration();"
				tabindex="10"> <spring:message code="btn.label.delete" ></spring:message>
			</a> <a href="#divconfirmation" class="fancybox" style="display: none;"
				id="confirmation"> <spring:message code="btn.label.delete" ></spring:message>
			</a> <a href="#divmessage" class="fancybox" style="display: none;"
				id="message"> <spring:message code="btn.label.delete" ></spring:message>
			</a>
		</span>
		</span>
	</div>
</div>

<div class="box-body table-responsive no-padding box">
	<table class="table table-hover" id="migrationTrackDetailGrid"></table>
	<div id="migrationTrackDetailGridPagingDiv"></div>
</div>

<div class="pbottom15 form-group " id="migration-buttons-div">
	<button class="btn btn-grey btn-xs" id="startMigrationBtn"
		onclick="startMigration()" tabindex="11">
		<spring:message code="btn.label.start.migration" ></spring:message>
	</button>
	<button class="btn btn-grey btn-xs" id="reprocessMigrationBtn"
		onclick="reprocessMigration()" tabindex="12">
		<spring:message code="btn.label.reprocess.migration" ></spring:message>
	</button>
</div>

<a href="#divReprocessFailMigrationTrack" class="fancybox"
	style="display: none;" id="reprocessFailMigrationTrack">#</a>

<div id="divReprocessFailMigrationTrack"
	style="width: 100%; display: none;">
	<div class="modal-content">
		<div class="modal-header padding10">
			<h4 class="modal-title">
				<spring:message
					code="migration.failed.status.reprocess.header.label" ></spring:message>
			</h4>
		</div>
		<div class="modal-body padding10 inline-form">
			<p id="migration-failed-reprocess-warning" style="display: none;">
				<input type="hidden" value="0"
					name="reprocessFailedMigrationTrackId"
					id="reprocessFailedMigrationTrackId"></input>
				<spring:message code="migration.failed.status.reprocess.message" ></spring:message>
			</p>
		</div>
		<div id="inactive-snmp-server-div" class="modal-footer padding10">
			<button id="migration_reprocess_yes_btn" type="button"
				class="btn btn-grey btn-xs "
				onclick="saveReprocessFailedMigrationTrackDetail();">
				<spring:message code="btn.label.yes" ></spring:message>
			</button>
			<button id="migration_reprocess_no_btn" type="button"
				class="btn btn-grey btn-xs " data-dismiss="modal"
				onclick="closeReprocessFailedMigrationPopup();">
				<spring:message code="btn.label.no" ></spring:message>
			</button>
		</div>
	</div>
	<!-- /.modal-content -->
</div>

<a href="#divMigrationResponseDiv" class="fancybox"
	style="display: none;" id="migrationResponse">#</a>
<div id="divImportConfig" style="width: 100%; display: none;">
	<div id="divMigrationResponseDiv" style="width: 100%; display: none;">
		<div class="modal-content">
			<div class="modal-header padding10">
				<h4 class="modal-title">
					<spring:message code="leftmenu.label.migration" ></spring:message>
				</h4>
			</div>
			<div class="modal-body padding10 inline-form">

				<span id="deleteDriverResponseMsg" style="display: none;"> <jsp:include
						page="../common/responseMsgPopUp.jsp" ></jsp:include>
				</span>
				<p id="responseMessageContent">
					<span id="responseMessageSpan" style="display: inline-block;"></span>
				</p>
				<p id="responseMessageContentXmlName">
					<span></span>
				</p>

			</div>

			<div class="modal-body padding10 inline-form">

				<jsp:include page="../common/responseMsgPopUp.jsp" ></jsp:include>

				<div id="divValidationList" width="100%" style="overflow: auto;"></div>
			</div>
			<div id="migration-response-buttons-div"
				class="modal-footer padding10">
				<button id="failClose"
					onclick="javascript:parent.closeFancyBoxFromChildIFrame()"
					class="btn btn-grey btn-xs " style="display: none;">
					<spring:message code="btn.label.close" ></spring:message>
				</button>

				<button id="successClose"
					onclick="loadMigrationPage('migration-form','<%=ControllerConstants.INIT_MIGRATION%>','POST','MIGRATION','');"
					class="btn btn-grey btn-xs " style="display: none;">
					<spring:message code="btn.label.close" ></spring:message>
				</button>
			</div>
			<div id="migration-response-progress-bar-div"
				class="modal-footer padding10" style="display: none;">
				<jsp:include page="../common/processing-bar.jsp"></jsp:include>
			</div>

		</div>
		<!-- /.modal-content -->
	</div>

	<div id="addServer" style="width: 100%; display: none;">
		<jsp:include page="../server/addServerDetailsPopUp.jsp">
			<jsp:param name="parentScreen" value="migration" ></jsp:param>
		</jsp:include>
	</div>
	<a style="display: none;" class="fancybox" id="serverDetailsAnchor"
		href="#addServer"></a>


</div>

<form id="instance_form" method="POST"
	action="<%=ControllerConstants.INIT_UPDATE_SERVER_INSTANCE%>">
	<input type="hidden" id="serverInstanceId" name="serverInstanceId">
</form>

<form id="migration-form" method="" action="">
	<input type="hidden" id="migration_actionType"
		name="REQUEST_ACTION_TYPE" value="" />
</form>



<script type="text/javascript">

	var reloadGridTime = 7000; // Modify this time for reloading grid and check migration in process status

	window.setTimeout( refreshGrid, reloadGridTime);

	function refreshGrid() {
		$.ajax({
			url: '<%=ControllerConstants.IS_MIGRATION_IN_PROCESS%>',
			cache: false,
			async: true, 
			dataType: 'json',
			type: "POST",
			data: { },
			success: function(data){
 				var response = eval(data);
				var responseCode = response.code; 
				var responseObject = response.object;
				
 				if(responseCode == "200"){
					if(responseObject) {
						reloadMigrationTrackGridData();
						blockGrid();
					} else {
						reloadMigrationTrackGridData();
						unblockGrid();
					}
 				}else if(responseObject != undefined && responseObject != 'undefined' && responseCode == "400"){
					reloadMigrationTrackGridData();
					unblockGrid();
				}else{					
					reloadMigrationTrackGridData();
					unblockGrid();
 				}
			},
		    error: function (xhr,st,err){
				handleGenericError(xhr,st,err);
			}
		});
		
    	window.setTimeout(refreshGrid, reloadGridTime);
 	}
	
	function blockGrid() {
		$("#migrationTrackDetailGrid").closest(".ui-jqgrid").block({
		    message: "<h5>Migration is in process...</h5>",
		    css: { border: "1px solid #a00" }
		});
		$('#startMigrationBtn').prop('disabled', true);
		$('#reprocessMigrationBtn').prop('disabled', true);
	}
	
	function unblockGrid() {
		$("#migrationTrackDetailGrid").closest(".ui-jqgrid").unblock();
		$('#startMigrationBtn').prop('disabled', false);
		$('#reprocessMigrationBtn').prop('disabled', false);
	}

	var jsSpringMsg = {};
	jsSpringMsg.emptyRecord = "<spring:message code='device.list.grid.empty.records'></spring:message>";
	
	
	$(document).ready(function() {
		
		$('#services_id').multiselect({
			maxHeight: '200',
		    buttonWidth: 'auto',
		    nonSelectedText: 'Select',
		    nSelectedText: 'selected',
		    dropDown: true,
	    buttonWidth: '450px',
		    enableFiltering: false
		}); 
		
		getServerListByServerType();
		getMigrationTrackDetailList();
		
	});
	
	function openCreateServerPopup() {
		$("#serverDetailsAnchor").click();
	}
	
	function callbackCreateServer() {
		getServerListByServerType();
	}
	
	function deleteMigration() {
		if(ckIntanceSelected.length == 0){
			$("#message").click();
			return;
		}
		$("#confirmation").click();
	}
	
	function deleteMigrationTrackDetails() {
		$.ajax({
			url: '<%=ControllerConstants.DELETE_MIGRATION_TRACK_DETAIL%>',
			cache: false,
			async: true, 
			dataType: 'json',
			type: "POST",
			data: { "migrationTrackIdList" : ckIntanceSelected.toString() },
			success: function(data){
 				var response = eval(data);
				var responseCode = response.code; 
				var responseMsg = response.msg; 
				hideProgressBar("migration");
				if(responseCode == "200"){
					clearAllMessages();
 					resetWarningDisplay();
			    	clearResponseMsgDiv();
			    	clearResponseMsg();
			    	clearErrorMsg();
			    	showSuccessMsg(responseMsg);
			    	ckIntanceSelected = new Array();
		    		closeFancyBox();
 					resetMigrationTrackDetailForm();
 					reloadMigrationTrackGridData();
 				}else if(responseObject != undefined && responseObject != 'undefined' && responseCode == "400"){
 					showErrorMsg(responseMsg);
 					addErrorIconAndMsgForAjax(responseObject);
				}else{					
 					showErrorMsg(responseMsg);
 				}
			},
		    error: function (xhr,st,err){
				handleGenericError(xhr,st,err);
			}
		});
	}
	
	function startMigration() {
		$.ajax({
			url: '<%=ControllerConstants.DO_MIGRATION%>',
			cache: false,
			async: true, 
			dataType: 'json',
			type: "POST",
			data: { },
			success: function(data){
 				var response = eval(data);
				var responseCode = response.code; 
				var responseMsg = response.msg; 
				var responseObject = response.object;
 				if(responseCode == "200"){
 					clearAllMessages();
 					resetWarningDisplay();
 				}else if(responseObject != undefined && responseObject != 'undefined' && responseCode == "400"){
 					addErrorIconAndMsgForAjax(responseObject);
				}else{					
 					showErrorMsg(responseMsg);
 				}
			},
		    error: function (xhr,st,err){
				handleGenericError(xhr,st,err);
			}
		});
	}
	
	function reprocessMigration() {
		$.ajax({
			url: '<%=ControllerConstants.REPROCESS_MIGRATION%>',
			cache: false,
			async: true, 
			dataType: 'json',
			type: "POST",
			data: { },
			success: function(data){
				
 				var response = eval(data);
				var responseCode = response.code; 
				var responseMsg = response.msg; 
				var responseObject = response.object;
				hideProgressBar("migration");
 				if(responseCode == "200"){
 					clearAllMessages();
 					resetWarningDisplay();
 				}else if(responseObject != undefined && responseObject != 'undefined' && responseCode == "400"){
 					addErrorIconAndMsgForAjax(responseObject);
				}else{					
 					showErrorMsg(responseMsg);
 				}
			},
		    error: function (xhr,st,err){
				handleGenericError(xhr,st,err);
			}
		});
	}
	
	function reprocessMigrationTrackPopup(id){
		$("#reprocessFailedMigrationTrackId").val(id);
		$("#migration-failed-reprocess-warning").show();
		$("#reprocessFailMigrationTrack").click();
	}
	
	function saveReprocessFailedMigrationTrackDetail() {
		resetWarningDisplay();
		clearAllMessages();
		$.ajax({
			url: '<%=ControllerConstants.UPDATE_FAILED_MIGRATION_STATUS%>',
			cache: false,
			async: true, 
			dataType: 'json',
			type: "POST",
			data: { "migrationTrackDetailId" : $("#reprocessFailedMigrationTrackId").val() },
			success: function(data){
 				var response = eval(data);
				var responseCode = response.code; 
				var responseMsg = response.msg; 
				var responseObject = response.object;
 				if(responseCode == "200"){
 					resetWarningDisplay();
 					clearAllMessages();
 					$("#reprocessFailedMigrationTrackId").val(0);
 					closeFancyBox();
 					reloadMigrationTrackGridData();
 				}else if(responseObject != undefined && responseObject != 'undefined' && responseCode == "400"){
 					clearAllMessages();
 					addErrorIconAndMsgForAjax(responseObject);
				}else{		
					clearAllMessages();
 					showErrorMsg(responseMsg);
 				}
			},
		    error: function (xhr,st,err){
				handleGenericError(xhr,st,err);
			}
		});
	}
	
	function closeReprocessFailedMigrationPopup(){
		$('#'+$("#reprocessFailedMigrationTrackId").val() +'_FAILED').bootstrapToggle('off');
		closeFancyBox();
	}
	
	function reloadMigrationTrackGridData(){
		var $grid = $("#migrationTrackDetailGrid");
		$grid.jqGrid('setGridParam',{datatype:'json',page: 1}).trigger('reloadGrid');
	}
	
	function getMigrationTrackDetailList(){
		$("#migrationTrackDetailGrid").jqGrid({
        	url: "<%=ControllerConstants.GET_MIGRATION_TRACK_DETAIL_LIST%>",
        	postData:{},
            datatype: "json",
            colNames:[
                      "#",
					  "<spring:message code='migration.list.grid.column.id' ></spring:message>",
					  "<spring:message code='migration.list.grid.column.server.id' ></spring:message>",
					  "<spring:message code='migration.list.grid.column.servertype.id' ></spring:message>",
					  "<spring:message code='migration.list.grid.column.server.ip.port' ></spring:message>",
                      "<spring:message code='migration.list.grid.column.serverinstance.port' ></spring:message>",
                      "<spring:message code='migration.list.grid.column.migration.prefix' ></spring:message>",
                      "<spring:message code='migration.list.grid.column.scriptname' ></spring:message>",
                      "<spring:message code='migration.list.grid.column.migration.status' ></spring:message>",
                      "<spring:message code='migration.list.grid.column.migration.reprocess' ></spring:message>",
                      "<spring:message code='migration.list.grid.column.migration.attempts' ></spring:message>",
                      "<spring:message code='migration.list.grid.column.migration.detail' ></spring:message>",
                      "<spring:message code='migration.services.label' ></spring:message>",
                      "<spring:message code='migration.list.grid.column.edit' ></spring:message>"
                     ],
                     colModel: [
						{name : '',	index : '', formatter : checkBoxFormatter, sortable : false, width : "5%"},
						{name:'id',index:'id',sortable:true, width : "5%"},
						{name:'ServerId',index:'ServerId',sortable:false,hidden:true, width : "5%"},
						{name:'ServerTypeId',index:'ServerTypeId',sortable:false,hidden:true, width : "5%"},
						{name:'ServerIPAndPort',index:'ServerIPAndPort',sortable:false, width : "5%"},
                    	{name:'ServerInstancePort',index:'ServerInstancePort',sortable:false, width : "5%"},
                    	{name:'MigrationPrefix',index:'MigrationPrefix',sortable:false,hidden:true, width : "5%"},
                    	{name:'SIScriptName',index:'SIScriptName',sortable:false,hidden:true, width : "5%"},
                    	{name:'MigrationStatus',index:'MigrationStatus',sortable:true, width : "5%"},
                    	{name:'Reprocess',index:'Reprocess',align:'center',sortable:false,formatter:reprocessToggleFormatter, width : "5%"},
                    	{name:'Attempts',index:'Attempts',align:'center',sortable:true, width : "5%"},
                    	{name:'MigrationDetail',index:'MigrationDetail',align:'center',sortable:false,formatter:showFailedMigrationDetailFormatter, width : "5%"},
                    	{name:'ServicesList',index:'ServicesList',sortable:false,hidden:true, width : "5%"},
                    	{name:'Edit',index:'Edit',align:'center',sortable:false,formatter:editMigrationTrackDetailColumnFormatter,width : "5%"}
               		 ],
            rowNum:<%=MapCache.getConfigValueAsInteger(
					SystemParametersConstant.TOTAL_ROWS_TO_DISPLAY_IN_GRID, 10)%>,
            rowList:[10,20,60,100],
            height: 'auto',
            mtype:'POST',
    		sortname: 'id',
    		sortorder: "asc",
    		pager: "#migrationTrackDetailGridPagingDiv",
    		contentType: "application/json; charset=utf-8",
    		viewrecords: true,
    		multiselect: false,
    		timeout : 120000,
    	    loadtext: "<spring:message code="jq.grid.loading.text"></spring:message>",
            caption: "<spring:message code="migration.track.detail.label"></spring:message>",
            beforeRequest:function(){
                $(".ui-dialog-titlebar").hide();
            }, 
            beforeSend: function(xhr) {
                xhr.setRequestHeader("Accept", "application/json");
                xhr.setRequestHeader("Content-Type", "application/json");
            },
            beforeSelectRow: function(rowid, e) { },
            loadComplete: function(data) {
            
     			$(".ui-dialog-titlebar").show();
     			$('.checkboxbg').removeAttr("disabled");
     			$('.checkboxbg').bootstrapToggle();
     			if ($('#migrationTrackDetailGrid').getGridParam('records') === 0) {
					$('#migrationTrackDetailGrid tbody').html(
							"<div style='padding:6px;background:#D8D8D8;text-align:center;'>"
									+ jsSpringMsg.emptyRecord + "</div>");
					$("#migrationTrackDetailGridPagingDiv").hide();
				} else {
					$("#migrationTrackDetailGridPagingDiv").show();
					ckIntanceSelected = new Array();
				}
			}, 
			onPaging: function (pgButton) {
				clearResponseMsgDiv();
				//clearInstanceGrid();
			},
            loadError : function(xhr,st,err) {
    			handleGenericError(xhr,st,err);
    		},
    		beforeSelectRow : function(rowid, e) {
				return false;
			},
			onSelectAll : function(id, status) {
				if (status == true) {
					ckIntanceSelected = new Array();
					for (i = 0; i < id.length; i++) {
						ckIntanceSelected.push(id[i]);
					}
				} else {
					ckIntanceSelected = new Array();
				}

			},
            recordtext: "<spring:message code="jq.grid.pager.total.records.text"></spring:message>",
    	    emptyrecords: "<spring:message code="jq.grid.empty.records.text"></spring:message>",
    		loadtext: "<spring:message code="jq.grid.loading.text"></spring:message>",
    		
    		}).navGrid("#migrationTrackDetailGridPagingDiv",{edit:false,add:false,del:false,search:false});
    			$(".ui-jqgrid-titlebar").hide();
    			$(".ui-pg-input").height("10px");

	}
	
	var count = 0;
	function checkBoxFormatter(cellvalue, options, rowObject) {
		count++;
		var deviceUpdatedName = rowObject["id"];

		if (rowObject["MigrationStatus"] == 'OPEN') {
			return "<input type='checkbox' name='device_" + deviceUpdatedName + "_"
					+ count + "'  id='device_"
					+ deviceUpdatedName + "_" + count
					+ "' onclick=\"addRowId('device_" + deviceUpdatedName + "_"
					+ count + "\',\'" + rowObject["id"] + "\')\"; />";
		} else {
			return "<input type='checkbox' name='device_" + deviceUpdatedName + "_"
					+ count + "' disabled='disabled' id='device_" + deviceUpdatedName + "_" + count
					+ "' onclick=\"addRowId(\'device_" + deviceUpdatedName + "_"
					+ count + "\', \'" + rowObject["id"] + "\', \'"
					+ rowObject["mappingId"] + "\')\"; />";

		}
	}
	
	/* Function will add element and set all required for delete. */

	function addRowId(elementId, deviceId, mappingId) {
		var migrationElement = document.getElementById(elementId);
		if (migrationElement.checked) {
			if (ckIntanceSelected.indexOf(deviceId) === -1) {
				ckIntanceSelected.push(deviceId);
			}
		} else {
			if (ckIntanceSelected.indexOf(deviceId) !== -1) {
				ckIntanceSelected.splice(ckIntanceSelected.indexOf(deviceId), 1);
			}
		}
	}
	
	function editMigrationTrackDetailColumnFormatter(cellvalue, options, rowObject){
		var migrationTrackId = rowObject["id"];
		var editId = migrationTrackId +"_migration_track_detail_edit_btn";
		
		if(rowObject["MigrationStatus"] == 'OPEN'){
			return "<a href='#' id='"+ editId +"' +class='link' onclick=updateMigrationTrackDetailPopup('"+'EDIT'+"','"+rowObject["id"]+"'); ><i class='fa fa-pencil-square-o' aria-hidden='true'></i></a>";	
		} else {
			return '';
		}
	} 
	
	function reprocessToggleFormatter(cellvalue, options, rowObject){
		var toggleId = rowObject["id"] + "_" + cellvalue;
		var toggleIdDiv = toggleId + "_migration_reprocess_btn";
		
		if(cellvalue == 'FAILED'){
			return '<div id="'+toggleIdDiv+'"><input class="checkboxbg" id=' + toggleId + ' value= '+cellvalue+' data-on="On" data-off="Off" unchecked type="checkbox" data-size="mini" onchange = "reprocessMigrationTrackPopup('+rowObject["id"]+');"></div>';
		}else{
			return '';
		}
	}
	
	function showFailedMigrationDetailFormatter(cellvalue, options, rowObject) {
		var toggleId = rowObject["id"] + "_mig_detail_id";
		var toggleIdDiv = toggleId + "_migration_detail_btn";
		
		if(rowObject["MigrationStatus"] == 'FAILED' && cellvalue != ''){
			return '<div id="'+toggleIdDiv+'"><button id=' + toggleId + ' type="button" class="btn btn-grey btn-xs " onclick="showFailedMigrationDetailPopup('+rowObject["id"]+');">Mig Detail</button></div>';
		}else{
			return '';
		}
	}
	
	function showFailedMigrationDetailPopup(migrationTrackDetailId) {
		$.ajax({
			url: '<%=ControllerConstants.GET_MIGRATION_TRACK_DETAIL_BY_ID%>',
			cache: false,
			async: true, 
			dataType: 'json',
			type: "POST",
			data: { "migrationTrackDetailId" : migrationTrackDetailId },
			success: function(data){
 				var response = eval(data);
				var responseCode = response.code; 
				var responseMsg = response.msg; 
				var responseObject = response.object;
 				if(responseCode == "200"){
 					clearAllMessages();
 					resetWarningDisplay();
					response = JSON.parse(responseObject.remark);
					responseCode = response.code; 
					responseMsg = response.responseMsg; 
					responseObject = response.object;
					var moduleName = response.moduleName;
					if(moduleName) {
						$("#responseMessageContentXmlName").html("Xml Name : "+moduleName);
					}
					
					$("#responseMessageContent").show();
					$("#responseMessageContent").children('span').html(responseMsg);
					$("#divValidationList").html('');
					var tableString = '<table class="table table-hover" width="100%" style="overflow: auto;">';
					if(responseObject!=null && responseObject[0] != null && responseObject[0].length>1){
					var jsonObjectLength = Object.keys(responseObject[0]).length;
					
					if (jsonObjectLength == 1) {
						// Length is 1 , when XSD validation fail without line number
						tableString += "<tr>";
						tableString += "<th style='white-space: nowrap'>" + jsLocaleMsg.errorMessage + "</th>";
						tableString += "<th style='white-space: nowrap'>" + jsLocaleMsg.fileName + "</th>";
						tableString += "</tr>";
					}
					else if (jsonObjectLength == 2) {
						// Length is 2 , when XSD validation fail without line number
						tableString += "<tr>";
						tableString += "<th style='white-space: nowrap'>" + jsLocaleMsg.fileName
						+ "</th>";
						tableString += "<th style='white-space: nowrap'>" + jsLocaleMsg.errorMessage
						+ "</th>";
						tableString += "</tr>"; 
					}
					
					else if (jsonObjectLength == 3) {
						// Length is 3 , when XSD validation fail
						tableString += "<tr>";
						tableString += "<th style='white-space: nowrap'>" + jsLocaleMsg.LineNo
						+ "</th>";
						tableString += "<th style='white-space: nowrap'>" + jsLocaleMsg.fileName
						+ "</th>";
						tableString += "<th style='white-space: nowrap'>" + jsLocaleMsg.errorMessage
						+ "</th>";
						tableString += "</tr>";
					} else if (jsonObjectLength == 5) {
						// Length is 5 , when Entity validation fail
						
						tableString += "<tr>";
						/*tableString += "<th width='15%' style='white-space: nowrap'>" + jsLocaleMsg.moduleName
						+ "</th>";*/
						tableString += "<th width='15%' style='white-space: nowrap'>" + jsLocaleMsg.entityName
						+ "</th>";
						tableString += "<th width='25%' style='white-space: nowrap'>" + jsLocaleMsg.propertyName
						+ "</th>";
						tableString += "<th width='15%' style='white-space: nowrap'>" + jsLocaleMsg.propertyValue
						+ "</th>";
						tableString += "<th width='30%' style='white-space: nowrap'>" + jsLocaleMsg.errorMessage
						+ "</th>";
						tableString += "</tr>";
						
					}
					
					$.each(responseObject, function(index, responseObject) {
						
						if(jsonObjectLength==Object.keys(responseObject).length){
						
							if (jsonObjectLength == 1) {
								tableString += "<tr>";
								tableString += "<td style='white-space: nowrap'>" + responseObject[0]
								+ "</td>";
								tableString += "</tr>";
							}		
							else if (jsonObjectLength == 2) {
								
								tableString += "<tr>";
								tableString += "<td style='white-space: nowrap'>" + responseObject[0]
								+ "</td>";
								tableString += "<td style='white-space: nowrap'>" + responseObject[1]
								+ "</td>";
								tableString += "</tr>";
								
							}
							
							else if (jsonObjectLength == 3) {
								if(responseObject[0]!= undefined && responseObject[1]!= undefined){
								tableString += "<tr>";
								tableString += "<td  style='white-space: nowrap'>" + responseObject[0]
								+ "</td>";
								tableString += "<td  style='white-space: nowrap'>" + responseObject[2]
								+ "</td>";
								tableString += "<td style='white-space: nowrap'>" + responseObject[1]
								+ "</td>";
								tableString += "</tr>";
								}
							} else if (jsonObjectLength == 5) {
								
								tableString += "<tr>";
								/*tableString += "<td style='white-space: nowrap'>" + responseObject[0]
								+ "</td>";*/
								tableString += "<td style='white-space: nowrap'>" + responseObject[1]
								+ "</td>";
								tableString += "<td style='white-space: nowrap'>" + responseObject[2]
								+ "</td>";
								tableString += "<td style='white-space: nowrap'>" + responseObject[3]
								+ "</td>";
								tableString += "<td style='white-space: nowrap'>" + responseObject[4]
								+ "</td>";
								tableString += "</tr>";
							}
						}
						
					});
					tableString += "</table>"
						$("#divValidationList").html(tableString);
					
					}    
					$("#responseMessageContent").children('span').text(responseMsg);
					$("#failClose").show();
					$("#successClose").hide();
					$("#migrationResponse").click();
					parent.resizeFancyBox();
 				}else if(responseObject != undefined && responseObject != 'undefined' && responseCode == "400"){
 					addErrorIconAndMsgForAjax(responseObject);
				}else{					
 					showErrorMsg(responseMsg);
 				}
			},
		    error: function (xhr,st,err){
				handleGenericError(xhr,st,err);
			}
		});
		
	}
	
	function updateMigrationTrackDetailPopup(actionType, attributeId) {
		if(attributeId > 0){
			var responseObject = jQuery("#migrationTrackDetailGrid").jqGrid ('getRowData', attributeId);
			fillMigrationTrackDetailForm(responseObject);
		}else{
			showErrorMsg(jsSpringMsg.failUpdateSnmpServerMsg);
		}
	}
	
	function getServerListByServerType() {
		$.ajax({
			url: '<%=ControllerConstants.GET_SERVER_LIST_BY_SERVER_TYPE%>',
			cache: false,
			async: true, 
			dataType: 'json',
			type: "POST",
			data: 
			{
				"serverType"	:	$("#migration-configuration-block #migration-configuration-form #serverType").val()
				
			},
			success: function(data){
				
 				var response = eval(data);
				var responseCode = response.code; 
				var responseMsg = response.msg; 
				var responseObject = response.object;
				var serverDropdown = $("#migration-configuration-block #migration-configuration-form #server_id");
 				if(responseCode == "200"){
					serverDropdown.empty(); 
					$.each(responseObject, function(i, item) {
					    serverDropdown.append($("<option></option>")
                                .attr("value", item.id).text(item.name+":"+item.ipAddress));
					});
 				}else if(responseObject != undefined && responseObject != 'undefined' && responseCode == "400"){
					serverDropdown.empty(); 
 					showErrorMsg(responseMsg);
				}else{					
					serverDropdown.empty();
 					showErrorMsg(responseMsg);
 				}
			},
		    error: function (xhr,st,err){
				handleGenericError(xhr,st,err);
			}
		});
	}
	
	function saveMigrationTrackDetail() {
		resetWarningDisplay();
		clearAllMessages();
		showProgressBar("migration");
		var servicesSelectedList = $('#services_id').val()+'';
		
		if(servicesSelectedList == null || servicesSelectedList=="" ||  servicesSelectedList== "null"){
			hideProgressBar("migration");
			showErrorMsg("<spring:message code='select.atleast.one.service.error'></spring:message>");
		}
		else{
		
		$.ajax({
			url: '<%=ControllerConstants.SAVE_MIGRATION_TRACK_DETAIL%>',
			cache: false,
			async: true, 
			dataType: 'json',
			type: "POST",
			data: 
			{  
				"id" : $("#migration-configuration-block #migration-configuration-form #migrationTrackId").val(),
				"server.id" : $("#migration-configuration-block #migration-configuration-form #server_id").val(), 
				"serverInstancePrefix" : $("#migration-configuration-block #migration-configuration-form #serverInstancePrefix").val(),
				"serverInstancePort" : $("#migration-configuration-block #migration-configuration-form #serverInstancePort").val(),
				"serverInstanceScriptName" : $("#migration-configuration-block #migration-configuration-form #serverInstanceScriptName").val(),
				"servicesList" : servicesSelectedList
			},
			success: function(data){
				hideProgressBar("migration");
 				var response = eval(data);
				var responseCode = response.code; 
				var responseMsg = response.msg; 
				var responseObject = response.object;
				hideProgressBar("migration");
 				if(responseCode == "200"){
 					resetWarningDisplay();
 					clearAllMessages();
 					resetMigrationTrackDetailForm();
 					reloadMigrationTrackGridData();
 				}else if(responseObject != undefined && responseObject != 'undefined' && responseCode == "400"){
 					showErrorMsg(responseMsg);
 					addErrorIconAndMsgForAjax(responseObject);
				}else{					
 					showErrorMsg(responseMsg);
 				}
			},
		    error: function (xhr,st,err){
		    	hideProgressBar("migration");
				handleGenericError(xhr,st,err);
			}
		});
		}
		
	}
	
	function showProgressBar(div){
		$("#"+div+"-buttons-div").hide();
		$("#"+div+"-progress-bar-div").show();
	}

	function hideProgressBar(div){
		$("#"+div+"-buttons-div").show();
		$("#"+div+"-progress-bar-div").hide();	
	}
	
	function displayButtonsForUpdateMigrationTrackDetails(flag) {
		if(flag) {
			$("#saveMigrationTrackDetailBtn").html('<spring:message code="btn.label.update"></spring:message>');
			$("#cancelUpdateMigrationTrackDetailBtn").show();
		} else {
			$("#saveMigrationTrackDetailBtn").html('<spring:message code="btn.label.add"></spring:message>');
			$("#cancelUpdateMigrationTrackDetailBtn").hide();
		}
	}
	
	function resetMigrationTrackDetailForm() {
		resetWarningDisplay();
		clearAllMessages();
		$("#migration-configuration-block #migration-configuration-form #serverType").prop('disabled', false);
		$("#migration-configuration-block #migration-configuration-form #server_id").prop('disabled', false);
		$("#migration-configuration-block #migration-configuration-form #migrationTrackId").val(0);
		$("#migration-configuration-block #migration-configuration-form #serverInstancePrefix").val('');
		$("#migration-configuration-block #migration-configuration-form #serverInstancePort").val('');
		$("#migration-configuration-block #migration-configuration-form #serverInstanceScriptName").val('');
		$("#services_div").hide();
		$("#serverInstancePort").prop('disabled', false);
		$("#saveMigrationTrackDetailBtn").hide();
		$("#populateServicesBtn").show();
		
		displayButtonsForUpdateMigrationTrackDetails(false);
	}
	
	function fillMigrationTrackDetailForm(jsonObject) {
	
		clearAllMessages();
		resetWarningDisplay();
    	clearResponseMsgDiv();
    	clearResponseMsg();
    	clearErrorMsg();
$.ajax({
url: '<%=ControllerConstants.FETCH_ALL_SERVICE_LIST_BY_PORT%>',
cache: false,
async: true, 
dataType: 'json',
type: "POST",
data: 
{  
	"serverId" : jsonObject.ServerId, 
	"port" : jsonObject.ServerInstancePort,
},
success: function(data){

hideProgressBar("migration");
		var response = eval(data);
	var responseCode = response.code; 
	var responseMsg = response.msg; 
	var responseObject = response.object;
		if(responseCode == "200"){
			
			var serviceObj=eval(responseObject);
			
			var svcTypeList=serviceObj['svcTypeList'];
			var runningSvcList=serviceObj['runningSvcList'];
			var allSvcList=serviceObj['allServices'];
		
			var svcType;
			var svcName;
			var svcAllName;

			var allSvcStr=JSON.stringify(allSvcList);
			var coll=allSvcList['Collection Service'];
			var runningSvcName;
			var runningSvcNameVar;
			var svcNameVar;
			
			$('#services_id optgroup').each(function(index, optgroup) {
			    $(optgroup).remove();
			});
			$('#services_id option').each(function(index, option) {
		    $(option).remove();
		});
		
		$("#services_id").multiselect('destroy');
			
			$.each(svcTypeList,function(index,value){
				svcType=JSON.stringify(value);
				
				var typVar=value.type;
				
				var svcSpecificListAll=allSvcList[typVar];
				
				if(svcSpecificListAll.length>0){
					var SVCAvailableCounter=0;
					// $('#services_id').append("<optgroup label=\"" + typVar + "\">");
					$.each(svcSpecificListAll,function(index,value){
				
						svcNameVar=JSON.stringify(value);
						svcName=trim(svcNameVar, '\"');
						
						var running=false;
				
						
						$.each(runningSvcList,function(index,value){
	 						
	 						
	 						runningSvcNameVar=JSON.stringify(value);
	 						runningSvcName=trim(runningSvcNameVar, '\"');
	 						
	 						if(runningSvcName==svcName)
	 							{
	 							SVCAvailableCounter++;
	 							if(SVCAvailableCounter==1){
	 								 $('#services_id').append("<optgroup label=\"" + typVar + "\">");
	 							}
	 							
	 							var serviceSplitNameId = svcName.split("-"); 
	 							 $('#services_id').append("<option class='special' value=\"" + svcName + "\">" + serviceSplitNameId[1] + "</option>");
	 							running=true;
	 							//break;
	 							}
	 					});
						if(!running){
							/* var serviceSplitNameId = svcName.split("-");
							 $('#services_id').append("<option value=\"" + svcName + "\">" + serviceSplitNameId[1] + "</option>"); */
						}
						
					});
					
					 $('#services_id').append("</optgroup>");
				}
				
			});
			
			
		
				var servicesListGrid=jsonObject.ServicesList;
				var selectedServicesList=servicesListGrid.split(",");
		
			
			// Set the value

				$("#services_id").val(selectedServicesList);

				// Then refresh

				$("#services_id").multiselect("refresh");
				var serverTypeDropdown = $("#migration-configuration-block #migration-configuration-form #serverType");
				var serverDropdown = $("#migration-configuration-block #migration-configuration-form #server_id");
				
				serverTypeDropdown.val(jsonObject.ServerTypeId);

				getServerListByServerType();
				
				serverDropdown.val(jsonObject.ServerId);
				
				serverTypeDropdown.prop('disabled', true);
				serverDropdown.prop('disabled', true);
				
				$("#migration-configuration-block #migration-configuration-form #migrationTrackId").val(jsonObject.id);
				$("#migration-configuration-block #migration-configuration-form #serverInstancePrefix").val(jsonObject.MigrationPrefix);
				$("#migration-configuration-block #migration-configuration-form #serverInstancePort").val(jsonObject.ServerInstancePort);
				$("#migration-configuration-block #migration-configuration-form #serverInstanceScriptName").val(jsonObject.SIScriptName);
				
				displayButtonsForUpdateMigrationTrackDetails(true);
				
				$("#populateServicesBtn").hide();
				$("#saveMigrationTrackDetailBtn").show();
				$("#serverType").prop('disabled', true);
				$("#serverInstancePort").prop('disabled', true);
				$("#server_id").prop('disabled', true);
				$("#services_div").show();
				$("#services_id").multiselect({
					enableFiltering: false,
					enableHTML : true,
					maxHeight: 200,
					dropDown: true,
					buttonWidth: '450px',
				});
				$("#serverType").prop('disabled', true);
				$("#serverInstancePort").prop('disabled', true);
				$("#server_id").prop('disabled', true);
				
			
		}else if(responseObject != undefined && responseObject != 'undefined' && responseCode == "400"){
			showErrorMsg(responseMsg);
			addErrorIconAndMsgForAjax(responseObject);
	}else{					
			showErrorMsg(responseMsg);
		}
},
error: function (xhr,st,err){
	hideProgressBar("migration");
	handleGenericError(xhr,st,err);
}
});
		
		
		

		
	}
	
	function trim(s, mask) {
	    while (~mask.indexOf(s[0])) {
	        s = s.slice(1);
	    }
	    while (~mask.indexOf(s[s.length - 1])) {
	        s = s.slice(0, -1);
	    }
	    return s;
	}
	
	function populateServiceSlist(){
		
		clearAllMessages();
 					resetWarningDisplay();
			    	clearResponseMsgDiv();
			    	clearResponseMsg();
			    	clearErrorMsg();
		showProgressBar("migration");
		$.ajax({
			url: '<%=ControllerConstants.VALIDATE_MIGRATIONDETAILS_AND_FETCH_ALL_SERVICE_LIST_BY_PORT%>',
			cache: false,
			async: true, 
			dataType: 'json',
			type: "POST",
			data: 
			{  
				"id" : $("#migration-configuration-block #migration-configuration-form #migrationTrackId").val(),
				"server.id" : $("#migration-configuration-block #migration-configuration-form #server_id").val(), 
				"serverInstancePrefix" : $("#migration-configuration-block #migration-configuration-form #serverInstancePrefix").val(),
				"serverInstancePort" : $("#migration-configuration-block #migration-configuration-form #serverInstancePort").val(),
				"serverInstanceScriptName" : $("#migration-configuration-block #migration-configuration-form #serverInstanceScriptName").val(),
				"servicesList" : ""
			},
			success: function(data){
			
			hideProgressBar("migration");
 				var response = eval(data);
				var responseCode = response.code; 
				var responseMsg = response.msg; 
				var responseObject = response.object;
 				if(responseCode == "200"){
 					
 					
 					
 					var serviceObj=eval(responseObject);
 					
 					var svcTypeList=serviceObj['svcTypeList'];
 					var runningSvcList=serviceObj['runningSvcList'];
 					var allSvcList=serviceObj['allServices'];
 				
 					var svcType;
 					var svcName;
 					var svcAllName;
 	
 					var allSvcStr=JSON.stringify(allSvcList);
 					var coll=allSvcList['Collection Service'];
 					var runningSvcName;
 					var runningSvcNameVar;
 					var svcNameVar;
 					$('#services_id optgroup').each(function(index, optgroup) {
 					    $(optgroup).remove();
 					});
 					
 					$('#services_id option').each(function(index, option) {
					    $(option).remove();
					});
					
					$("#services_id").multiselect('destroy');
 					
 					$.each(svcTypeList,function(index,value){
 						svcType=JSON.stringify(value);
 						
 						var typVar=value.type;
 					
 						var svcSpecificListAll=allSvcList[typVar];
 						
 						if(svcSpecificListAll.length>0){
 							var SVCAvailableCounter=0;
 						//	 $('#services_id').append("<optgroup label=\"" + typVar + "\">");
 							$.each(svcSpecificListAll,function(index,value){
 		 					
 		 						svcNameVar=JSON.stringify(value);
 		 						svcName=trim(svcNameVar, '\"');
 		 						
 		 						var running=false;
 		 					
 		 						
 		 						$.each(runningSvcList,function(index,value){
 			 						
 			 						
 			 						runningSvcNameVar=JSON.stringify(value);
 			 						runningSvcName=trim(runningSvcNameVar, '\"');
 			 						
 			 						if(runningSvcName==svcName)
 			 							{
 			 							SVCAvailableCounter++;
 			 							if(SVCAvailableCounter==1){
 			 								 $('#services_id').append("<optgroup label=\"" + typVar + "\">");
 			 							}
 			 							
 			 							var serviceSplitNameId = svcName.split("-"); 
 			 							 $('#services_id').append("<option class='special' value=\"" + svcName + "\">" + serviceSplitNameId[1] + "</option>");
 			 							running=true;
 			 							//break;
 			 							}
 			 					});
 		 						if(!running){
 		 							/* var serviceSplitNameId = svcName.split("-");
 		 							 $('#services_id').append("<option value=\"" + svcName + "\">" + serviceSplitNameId[1] + "</option>"); */
 		 						}
 		 						
 		 					});
 							
 							 $('#services_id').append("</optgroup>");
 						}
 						
 					});
 					
 					$("#populateServicesBtn").hide();
 					$("#saveMigrationTrackDetailBtn").show();
 					$("#serverType").prop('disabled', true);
 					$("#serverInstancePort").prop('disabled', true);
 					$("#server_id").prop('disabled', true);
 					$("#services_div").show();
 					$("#services_id").multiselect({
 						enableFiltering: false,
 						enableHTML : true,
 						maxHeight: 200,
 						dropDown: true,
 						buttonWidth: '450px',
 					});
 					$("#serverType").prop('disabled', true);
 					$("#serverInstancePort").prop('disabled', true);
 					$("#server_id").prop('disabled', true);
 					
 					
 					
 				}else if(responseObject != undefined && responseObject != 'undefined' && responseCode == "400"){
 					showErrorMsg(responseMsg);
 					addErrorIconAndMsgForAjax(responseObject);
				}else{					
 					showErrorMsg(responseMsg);
 				}
			},
		    error: function (xhr,st,err){
		    	hideProgressBar("migration");
				handleGenericError(xhr,st,err);
			}
		});
	}
	
</script>
