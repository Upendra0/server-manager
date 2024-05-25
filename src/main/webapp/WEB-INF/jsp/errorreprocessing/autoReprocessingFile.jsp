<%@page import="com.elitecore.core.util.Constant"%>
<%@page import="com.elitecore.sm.common.constants.BaseConstants"%>
<%@page import="com.elitecore.sm.common.constants.EngineConstants"%>
<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<%@page
	import="com.elitecore.sm.common.constants.SystemParametersConstant"%>
<%@page import="com.elitecore.sm.util.MapCache"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/security/tags"
	prefix="sec"%>
<%@taglib uri="/WEB-INF/ShowErrorTag.tld" prefix="elitecoreError"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<script type='text/javascript'
	src="${pageContext.request.contextPath}/js/jquery.ajaxfileupload.js"></script>
<!-- <script src="http://jqueryfiledownload.apphb.com/Scripts/jquery.fileDownload.js"></script>  -->
<script type='text/javascript'
	src="${pageContext.request.contextPath}/js/jquery.fileDownload.js"></script>
<style>
.checkbox {
	color: #000 !important;
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

.multiselect-clear-filter {
	line-height: 1.9;
}

/* .ui-jqgrid .ui-jqgrid-bdiv {
	overflow-y: scroll;
	max-height: 270px;
} */

.ui-jqgrid tr.jqgrow td {
	word-wrap: break-word;
}

.serviceInstanceListPopupDiv > .btn-group > button > .multiselect-selected-text{
	text-indent:0 !important;
}
</style>

<div id="reprocessFileSearchArea">
	<div class="tab-content padding0 clearfix" id="reprocessing-file-block">
		<input type="hidden" id="search_service_type"
			value="PROCESSING_SERVICE">
		<div class="fullwidth borbot">
			<div class="col-md-6 inline-form"
				style="padding-left: 0px !important;">

				<div class="form-group">
					<spring:message code="file.reprocess.serviceInstanceList.label"
						var="label" ></spring:message>
					<spring:message
						code="file.reprocess.serviceInstanceList.label.tooltip"
						var="tooltip" ></spring:message>
					<div class="table-cell-label">${label}<span class="required">*</span>
					</div>
					<div class="input-group">
						<select name="serviceInstanceList"
							class="form-control table-cell input-sm" tabindex="3"
							multiple="multiple" id="serviceInstanceList"
							data-toggle="tooltip" data-placement="bottom" title="${tooltip}">
						</select> <span class="input-group-addon add-on last"> <i
							id="serviceInstanceList_error" class="glyphicon glyphicon-alert"
							data-toggle="tooltip" data-placement="left" title=""></i></span>
					</div>
				</div>
				 <div class="form-group" id = "reprocess_reason_errorcode_div" tabindex="4" style="display: none;">
             	<spring:message code="file.reprocess.errorcode.label" var="label" ></spring:message>
             	<spring:message code="file.reprocess.errorcode.tooltip" var="tooltip" ></spring:message>
         		<div class="table-cell-label">${label}</div>
             	<div class="input-group ">
             		<input type="text" id="reasonErrorCode" class="form-control table-cell input-sm" data-toggle="tooltip" data-placement="bottom" tabindex="5" title="${tooltip}" maxlength="250">
             		<span class="input-group-addon add-on last" > <i id="reasonErrorCode_error" class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="left" title=""></i></span>
           		</div>
            </div>
			</div>
			<div class="col-md-6 inline-form">
				<div class="form-group">
					<spring:message code="file.reprocess.category.label" var="label" ></spring:message>
					<spring:message code="file.reprocess.category.label.tooltip"
						var="tooltip" ></spring:message>
					<div class="table-cell-label">${label}<span class="required">*</span>
					</div>
					<div class="input-group ">
						<spring:message code="select.option.type.category"
							var="selectType" ></spring:message>

						<select name="category" class="form-control table-cell input-sm"
							tabindex="3" id="category" data-toggle="tooltip"
							data-placement="bottom" title="${tooltip}"
							onchange="manageReasonCategoryOption();">
							<option value="-1" selected="selected">${selectType}</option>
							<c:forEach items="${fileProcessingCategoryEnum}"
								var="fileProcessingCategoryEnum">
								<option value="${fileProcessingCategoryEnum.value}">${fileProcessingCategoryEnum.value}</option>
							</c:forEach>

						</select> <span class="input-group-addon add-on last"> <i
							id="category_error" class="glyphicon glyphicon-alert"
							data-toggle="tooltip" data-placement="left" title=""></i></span>
					</div>
				</div>

				<div class="form-group" id="reprocess_reason_category_div"
					style="display: none;">
					<spring:message code="file.reprocess.reason.category.label"
						var="label" ></spring:message>
					<spring:message code="file.reprocess.reason.category.tooltip"
						var="tooltip" ></spring:message>
					<div class="table-cell-label">${label}<span class="required">*</span>
					</div>
					<div class="input-group ">
						<%-- <spring:message code="select.option.type.category"  var="selectType"></spring:message> --%>
						<select name="reasonCategory"
							class="form-control table-cell input-sm" tabindex="3"
							id="reasonCategory" data-toggle="tooltip" data-placement="bottom"
							title="${tooltip}"
							onChange="manageReasonCategoryOption(),getAllRuleList()">
							<option  value="-1" selected="selected">${selectType}</option>
							<c:forEach items="${fileProcessingRuleCategory}"
								var="fileProcessingRuleCategory">
								<option value="${fileProcessingRuleCategory.value}">${fileProcessingRuleCategory.value}</option>
							</c:forEach>

						</select> <span class="input-group-addon add-on last"> <i
							id="reasonCategory_error" class="glyphicon glyphicon-alert"
							data-toggle="tooltip" data-placement="left" title=""></i></span>
					</div>
				</div>

				<div class="form-group" id="reprocess_reason_severity_div"
					style="display: none;">
					<spring:message code="file.reprocess.severity.label" var="label" ></spring:message>
					<spring:message code="file.reprocess.severity.tooltip"
						var="tooltip" ></spring:message>
					<div class="table-cell-label">${label}</div>
					<div class="input-group ">
						<spring:message code="select.option.type.severity"
							var="selectType" ></spring:message>
						<select name="reasonSeverity"
							class="form-control table-cell input-sm" tabindex="3"
							id="reasonSeverity" data-toggle="tooltip" data-placement="bottom"
							title="${tooltip}" onChange="">
							<option value="-1" selected="selected">${selectType}</option>
							<c:forEach items="${fileProcessingRuleSeverity}"
								var="fileProcessingRuleSeverity">
								<option value="${fileProcessingRuleSeverity.value}">${fileProcessingRuleSeverity.value}</option>
							</c:forEach>
						</select> <span class="input-group-addon add-on last"> <i
							id="reasonSeverity_error" class="glyphicon glyphicon-alert"
							data-toggle="tooltip" data-placement="left" title=""></i></span>
					</div>
				</div>



				<div class="form-group" id="rule_list_div" style="display: none;">
					<spring:message code="error.processing.reason.label" var="label" ></spring:message>
					<spring:message code="error.processing.reason.label.tooltip"
						var="tooltip" ></spring:message>
					<div class="table-cell-label">${label}
						<span class="required">*</span>
					</div>
					<div class="input-group ">

						<spring:message code="select.option.type.rule" var="selectType" ></spring:message>
						<select name="ruleList" class="form-control table-cell input-sm"
							tabindex="5" id="ruleList" data-toggle="tooltip"
							data-placement="bottom" title="${tooltip}">
							<option value="-1" selected="selected">${selectType}</option>
						</select> <span class="input-group-addon add-on last"> <i
							id="ruleList_error" class="glyphicon glyphicon-alert"
							data-toggle="tooltip" data-placement="left" title=""></i></span>
					</div>
				</div>
			</div>
			<div class="pbottom15">
				<!-- TODO : authorize has role add here -->
				<button id="searchReprocessingBtn" class="btn btn-grey btn-xs"
					onclick="searchReprocessingFiles();" tabindex="8">
					<spring:message code="btn.label.search" ></spring:message>
				</button>
				<button id="resetServerBtn" class="btn btn-grey btn-xs"
					onclick="resetReprocessingFiles();reloadAutoReloadGridData('false');" tabindex="8">
					<spring:message code="btn.label.reset" ></spring:message>
				</button>
			</div>
		</div>

		<div class="tab-content no-padding clearfix">
			<div class="fullwidth">
				<div class="title2">
					<spring:message code="file.auto.reprocess.grid.heading" ></spring:message>
					<span class="title2rightfield">
					<span class="title2rightfield-icon1-text">
						<a href="#divAddNewConfig" class="fancybox" id="openAddPopup" onclick="getServerInstance();"><i class="fa fa-plus-circle"></i></a>
						<a href="#divAddNewConfig" class="fancybox" id="openAddPopup" onclick="getServerInstance();">Add</a>
					</span>	
					<span class="title2rightfield-icon1-text">
				    <a href="#"><i class="fa fa-trash" onclick="displayDeleteTriggerTablesPopup();"></i></a>
					<a href="#" id="deleteTriggerTable" onclick="displayDeleteTriggerTablesPopup();">Delete</a>
					<a href="#divDeleteTriggers" class="fancybox" style="display: none;" id="delete_triggers">#</a>
					</span>
				</span>
				</div>
			</div>
		</div>

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
					<spring:message code="autoErrorReprocessing.mgmt.selection.warning" ></spring:message>
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
					<spring:message code="autoErrorReprocessing.mgnt.delete" ></spring:message>
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
					<div id="deleteCount"></div>
					<div>
						<spring:message code="autoErrorReprocessing.delete.warning" ></spring:message>
					</div>
				</div>
			</div>
			
			<div id="delete_triggers_bts_div" class="modal-footer padding10">
				<button id="delete_confirm" type="button" class="btn btn-grey btn-xs"
					 onclick="deleteTriggers(true);">
					<spring:message code="btn.label.delete" ></spring:message>
				</button>
				<button type="button" class="btn btn-grey btn-xs"
					onclick="closeFancyBox();reloadAutoReloadGridData();">
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
	
	
	<!-- Processing service grid : start -->
	<div class="box-body table-responsive no-padding box" id="processing_service_div" style="display: block;">
		<table class="table table-hover" id="processingServiceGrid">
		</table>
	    <div id="processingServiceGridPagingDiv"></div> 
	</div>
    <!-- Processing service grid : end -->
</div>	

<!-- Add New Auto Process Config start  -->
	<div id="divAddNewConfig" style="width: 100%; display: none;">
		<div class="modal-content">

			<div class="modal-header padding10">
				<h4 class="modal-title" id="add_label">
					<spring:message code="error.reprocess.auto.popup.header.add"></spring:message>
				</h4>
				<h4 class="modal-title" id="update_label"style="display: none;">
					<spring:message
							code="error.reprocess.auto.popup.header.update" ></spring:message>
				</h4>
			</div>

			<div class="modal-body padding10 inline-form">
			<jsp:include page="../common/responseMsgPopUp.jsp" ></jsp:include>
				<div class="col-md-6 no-padding">
					<div class="form-group">
						<spring:message code="file.reprocess.list.grid.column.serverInstance"
							var="label" ></spring:message>
						<spring:message code="file.reprocess.list.grid.column.serverInstance"
							var="tooltip" ></spring:message>
						<div class="table-cell-label">${label}<span class="required">*</span>
						</div>
						<div class="input-group ">
								<spring:message code="serviceManager.drag.server.instance"
									var="selectType" ></spring:message>
								<select name="search_server_inst"
									class="form-control table-cell input-sm" tabindex="3"
									id="search_server_inst" data-toggle="tooltip"
									data-placement="bottom" title="${tooltip}" onchange="getServiceInstance(this)">
									<option value="-1" selected="selected">${selectType}</option>
								</select>
								<span class="input-group-addon add-on last" > <i class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="bottom" title=""></i></span>
						</div>
					</div>
				</div>
				<div class="col-md-6 no-padding">
		
					<div class="form-group">
						<spring:message code="file.reprocess.serviceInstanceList.label"
							var="label" ></spring:message>
						<spring:message
							code="file.reprocess.serviceInstanceList.label.tooltip"
							var="tooltip" ></spring:message>
						<div class="table-cell-label">${label}<span class="required">*</span>
						</div>
						<div class="input-group serviceInstanceListPopupDiv">
						<spring:message code="serviceManager.select.service.instance"
									var="selectType" ></spring:message>
							<select name="serviceInstanceListPopup"
								class="form-control table-cell input-sm" tabindex="3"
								id="serviceInstanceListPopup"
								data-toggle="tooltip" data-placement="bottom" title="${tooltip}">
								<option value="-1" selected="selected">${selectType}</option>
							</select> <span class="input-group-addon add-on last"> <i
								id="serviceInstanceList_popup_error" class="glyphicon glyphicon-alert"
								data-toggle="tooltip" data-placement="left" title=""></i></span>
						</div>
					</div>
					</div>
				<div class="col-md-6 no-padding">
					<div class="form-group">
					<spring:message code="file.reprocess.category.label" var="label" ></spring:message>
					<spring:message code="file.reprocess.category.label.tooltip"
						var="tooltip" ></spring:message>
					<div class="table-cell-label">${label}<span class="required">*</span>
					</div>
						<div class="input-group ">
							<spring:message code="select.option.type.category"
								var="selectType" ></spring:message>
	
							<select name="category" class="form-control table-cell input-sm"
								tabindex="3" id="categoryPopup" data-toggle="tooltip"
								data-placement="bottom" title="${tooltip}"
								onchange="manageReasonCategoryOptionForPopup();">
								<option value="-1" selected="selected">${selectType}</option>
								<c:forEach items="${fileProcessingCategoryEnum}"
									var="fileProcessingCategoryEnum">
									<option value="${fileProcessingCategoryEnum.value}">${fileProcessingCategoryEnum.value}</option>
								</c:forEach>
	
							</select> <span class="input-group-addon add-on last"> <i
								id="category_error" class="glyphicon glyphicon-alert"
								data-toggle="tooltip" data-placement="left" title=""></i></span>
						</div>
					</div>
				</div>
				<div class="col-md-6 no-padding">
				
					<div class="form-group" id="reprocess_reason_category_div_popup" style="display: none;">
					<spring:message code="file.reprocess.reason.category.label"
						var="label" ></spring:message>
					<spring:message code="file.reprocess.reason.category.tooltip"
						var="tooltip" ></spring:message>
					<div class="table-cell-label">${label}<span class="required">*</span>
					</div>
					<div class="input-group ">
						<select name="reasonCategory"
							class="form-control table-cell input-sm" tabindex="3"
							id="reasonCategoryPopup" data-toggle="tooltip" data-placement="bottom"
							title="${tooltip}"
							onChange="manageReasonCategoryOptionForPopup(),getAllRuleListForPopup()">
							<c:forEach items="${fileProcessingRuleCategory}"
								var="fileProcessingRuleCategory">
								<option value="${fileProcessingRuleCategory.value}">${fileProcessingRuleCategory.value}</option>
							</c:forEach>

						</select> <span class="input-group-addon add-on last"> <i
							id="reasonCategory_error" class="glyphicon glyphicon-alert"
							data-toggle="tooltip" data-placement="left" title=""></i></span>
					</div>
					</div>
				
				</div>
				
				<div class="col-md-6 no-padding">
				<div class="form-group" id="reprocess_reason_severity_div_popup" style="display: none;">
					<spring:message code="file.reprocess.severity.label" var="label" ></spring:message>
					<spring:message code="file.reprocess.severity.tooltip"
						var="tooltip" ></spring:message>
					<div class="table-cell-label">${label}</div>
					<div class="input-group ">
						<spring:message code="select.option.type.severity"
							var="selectType" ></spring:message>
						<select name="reasonSeverity"
							class="form-control table-cell input-sm" tabindex="3"
							id="reasonSeverityPopup" data-toggle="tooltip" data-placement="bottom"
							title="${tooltip}" onChange="getAllRuleListForPopup()">
							<option value="-1" selected="selected">${selectType}</option>
							<c:forEach items="${fileProcessingRuleSeverity}"
								var="fileProcessingRuleSeverity">
								<option value="${fileProcessingRuleSeverity.value}">${fileProcessingRuleSeverity.value}</option>
							</c:forEach>
						</select> <span class="input-group-addon add-on last"> <i
							id="reasonSeverity_error" class="glyphicon glyphicon-alert"
							data-toggle="tooltip" data-placement="left" title=""></i></span>
					</div>
				</div>

			</div>
		
			<div class="col-md-6 no-padding">
				<div class="form-group" id = "reprocess_reason_errorcode_div_popup" tabindex="4" style="display: none;">
             	<spring:message code="file.reprocess.errorcode.label" var="label" ></spring:message>
	             	<spring:message code="file.reprocess.errorcode.tooltip" var="tooltip" ></spring:message>
	         		<div class="table-cell-label">${label}</div>
	             	<div class="input-group ">
	             		<input type="text" id="reasonErrorCodePopup" class="form-control table-cell input-sm" data-toggle="tooltip" data-placement="bottom" tabindex="5" title="${tooltip}" maxlength="250">
	             		<span class="input-group-addon add-on last" > <i id="reasonErrorCode_error" class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="left" title=""></i></span>
	           		</div>
            	</div>
			</div>
			
			<div class="col-md-6 no-padding">
				<div class="form-group">
             	<spring:message code="leftmenu.lable.trigger" var="label" ></spring:message>
	             	<spring:message code="leftmenu.lable.trigger" var="tooltip" ></spring:message>
	             	<div class="table-cell-label">${label}<span class="required">*</span></div>
	             	<div class="input-group">
	             	<input type='hidden' class='form-control table-cell input-sm' id="schedulerId"  data-toggle='tooltip' data-placement='bottom' value="" title='${tooltip }'/>
	             	<a href='#' onclick="selectSchedulerNamePopUp();"><i style='margin-top: 8px;margin-left: 8px;' class='glyphicon glyphicon-search'></i></a>
	             	<sec:authorize access="hasAuthority('VIEW_TRIGGER_CONFIG')">
	             		<input type="hidden" id="triggerAccess" name="triggerAccess" value="true">
	             	</sec:authorize>
	             	<input type="text" name="schedulerName" class="form-control table-cell input-sm" tabindex="1"
						id="schedulerName" data-toggle="tooltip" data-placement="bottom" title="${tooltip }" style="width:84%;" readonly />
	             	<span class="input-group-addon add-on last" ><i style='margin-top: 8px;margin-left: 8px;' class='glyphicon glyphicon-search'></i></span>
	           		</div>
	           	</div>
            </div>

	
			<div class="col-md-6 no-padding">
			<div class="form-group" id = "rule_list_div_popup">
            <spring:message code="error.processing.reason.label"  var="label"></spring:message>
            <spring:message code="error.processing.reason.label.tooltip"  var="tooltip"></spring:message>
         		<div class="table-cell-label">${label} <span class="required">*</span></div>
             	<div class="input-group ">
             		<select  name = "ruleList" class="form-control table-cell input-sm"  tabindex="5" id="ruleListPopup" data-toggle="tooltip" data-placement="bottom"  title="${tooltip}">
             			<!--<option  value="-1" selected="selected">${selectType}</option>-->
             		</select>
             		<span class="input-group-addon add-on last" > <i id="ruleList_error" class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="left" title=""></i></span> 
             	</div>
             </div>
			</div>
			<input type="hidden" name="jobId" id="jobId" value="0" />
			 <input type="hidden" name="id" id="id" value="0" />

			<div class="modal-footer padding10">
				<button type="button" class="btn btn-grey btn-xs " id="addAutoErrReproBtn"
					data-dismiss="modal" onclick="addNewAutoReprocessConfig('<%=ControllerConstants.ADD_NEW_AUTO_ERROR_REPROCESS_CONFIG%>');">
					<spring:message code="btn.label.add" ></spring:message>
				</button>
				<button type="button" class="btn btn-grey btn-xs " id="updateAutoErrReproBtn"
					data-dismiss="modal" onclick="addNewAutoReprocessConfig('<%=ControllerConstants.UPDATE_NEW_AUTO_ERROR_REPROCESS_CONFIG%>');" style="display: none;">
					Update
				</button>
				<button type="button" class="btn btn-grey btn-xs "
					data-dismiss="modal" onclick="closeFancyBox();resetPopup();">
					<spring:message code="btn.label.close" ></spring:message>
				</button>
			</div>
		</div>
	</div>
	</div>
	<!-- Add New Auto Process Config end  -->
	
	<!-- div for get scheduler list by search start-->		
<a href="#divSchedulerList" class="fancybox" style="display: none;" id="schedulerList">#</a>
<div id="divSchedulerList" style=" width:100%; display:none;" >
    <div class="modal-content" style="overflow:hidden">
		<div class="modal-header padding10">
			<h4 class="modal-title">
				<i class="icon-hdd"></i> 
				 <spring:message code="autoErrorReprocessing.config.tab.header" ></spring:message>
			</h4>
		</div>
		<div id="deviceContentDiv" class="modal-body" >
			<div>
				<label id="lblCounterForDeviceName" style="display:none;"></label>
				<label id="lblDeviceid" style="display:none;"></label>
				<span class="title2rightfield">
				 	<span class="title2rightfield-icon1-text" style="font-size: 12px;"> 
				 		<!-- <a href="#" onclick="createPolicy();"  ><spring:message code="business.policy.create.label" ></spring:message></a>  --> 
					</span>
				</span>
			</div>
			<div class="fullwidth table-responsive " style="height:auto;max-height:300px;overflow: auto;">
				<table class="table table-hover" id="schedulerlistgrid"></table>
				<div id="schedulerGridPagingDiv"></div>
			</div>
		
		</div>
		<div id="buttons-div" class="modal-footer padding10 text-left">
			<div id="buttons-div" class="modal-footer padding10 text-left">
				<button type="button" class="btn btn-grey btn-xs " id ="selectSchedulerbtn">
					<spring:message code="btn.label.select" ></spring:message>
				</button>
				<button onClick="" id="cancleBtn" class="btn btn-grey btn-xs ">
					<spring:message code="btn.label.cancel" ></spring:message>
				</button>
			</div>
		</div>
	</div>
			<!-- /.modal-content -->
</div>
<!-- div for get trigger list by search end-->	

<script
	src="${pageContext.request.contextPath}/customJS/errorReprocessing.js<%= "?v=" + Math.random() %>"
	type="text/javascript"></script>
<script
	src="${pageContext.request.contextPath}/customJS/autoErrorReprocess.js<%= "?v=" + Math.random() %>"
	type="text/javascript"></script>	
	
<script type="text/javascript">
var ckSelectedTriggers = [];
var deletableRows = {};
	var files;
	var getServerFlag = false;
	$('input[type=file]').on('change', prepareUpload);
	// Grab the files and set them to our variable
	function prepareUpload(event) {
	  	files = event.target.files;
	}
	
	var isGridCreated = false;
	$(document).ready(function() {
		$("#search_service_type").trigger("change");
		$("#reprocessFilesBtn").hide();
		$("#deleteReprocessFilesBtn").hide();
 		$('#serviceInstanceList').multiselect({
 			enableFiltering: true,
 			enableHTML : true,
			maxHeight: 200,
			dropDown: true,
			buttonWidth: '150px'
		}); 
		$("#search_service_type").val("PROCESSING_SERVICE");
		loadProcessingServiceGridDetails(false);
		reloadAutoReloadGridData();
		
	});
	function reloadAutoReloadGridData(isSearch){
		clearAllMessages();
		var $grid = $("#processingServiceGrid");
		jQuery('#processingServiceGrid').jqGrid('clearGridData');
		clearAutoReloadGrid();
		var category = $("#category").val();
		var rule = $("#ruleList").val();
		var errorCode = $("#reasonErrorCode").val();
		var reasoncategory = $("#reasonCategory").val();
		var severity = $("#reasonSeverity").val();
		$grid.jqGrid('setGridParam',{datatype:'json',page: 1,sortname: 'id',sortorder: "desc",postData:{

    		//"service.id" : service,
			"category": category,
			"severity": severity,
			"reasonCategory":  reasoncategory,
			"rule":rule,
			"errorCode":errorCode,
			'serviceInstanceIds': myCustomObjectForSMAction.join(),
			'isSearch':isSearch
		
		}}).trigger('reloadGrid');
		ckSelectedTriggers=[];
		$("#selectAllTrigger").prop('checked',false);
	}
	function clearAutoReloadGrid(){
		$grid = $("#processingServiceGrid");
		var rowIds = $grid.jqGrid('getDataIDs');
		// iterate through the rows and delete each of them
		for(var i=0,len=rowIds.length;i<len;i++){
			var currRow = rowIds[i];
			$grid.jqGrid('delRowData', currRow);
		}
		
	}
	function clearFileContent(){
		 $(":file").filestyle('clear');
	}
	
	function convertMbtoKb(fileSize){
		//var sizeInKb = fileSize * 1024;
		return parseFloat(sizeInKb.toFixed(2));
	}
	
	function getFileSizeInMb(fileSize){
		//var size = getFileSizeInKb(fileSize);
		//size = size / 1024;
		return parseFloat(fileSize.toFixed(3));
	}
	
	function getFileSizeinBytes(fileSize){
		fileSize = fileSize * 1024;
		return parseFloat(fileSize.toFixed(3));
	}
	
	function initializeFromAndToDate() {
        $("#fromDatePicker").datepicker('setDate', lastTenDay);
        $("#toDatePicker").datepicker('setDate', new Date()); 
	}

	var processingType = '<%=EngineConstants.PROCESSING_SERVICE%>'; 
	var distributionType = '<%=EngineConstants.DISTRIBUTION_SERVICE%>';
	var parsingType = '<%=EngineConstants.PARSING_SERVICE%>';
	var collectionType = '<%=EngineConstants.COLLECTION_SERVICE%>';
	
	$("#search_service_type").on('change', function() {
		var serviceType = $("#search_service_type").val();
		if(serviceType == -1){
			resetMultiselectDropdown("serviceInstanceList");
		//	resetMultiselectDropdown("serviceInstanceListPopup");
			$('#category').val('-1');
			$('#categoryPopup').val('-1');
			$("#rule_list_div").hide();
		}
		getServiceListByType("serviceInstanceListAuto");
	});
	
	$("#serviceInstanceList").on('change', function() {
		$("#category").val('-1');
		$("#reasonCategory").val($("#reasonCategory option:first").val());
		$("#reasonSeverity").val($("#reasonSeverity option:first").val());
		$("#rule_list_div").hide();
		initializeFromAndToDate();
	});
	
	$("#serviceInstanceListPopup").on('change', function() {
		$("#categoryPopup").val('-1');
		$("#reasonCategoryPopup").val($("#reasonCategoryPopup option:first").val());
		$("#reasonSeverityPopup").val($("#reasonSeverityPopup option:first").val());
		//$("#rule_list_div_popup").hide();
	});
	$("#reasonErrorCode").focusout(function() {
		getAllRuleList();
	}); 
	/* $("#reasonErrorCodePopup").focusout(function() {
		getAllRuleListForPopup();
	});  */
	
	$("#category").on('change', function() {
		var serviceType = $("#search_service_type").val()
		if(serviceType != "PARSING_SERVICE") {
			if(serviceType == -1){
				showErrorMsg("<spring:message code='error.reprocessing.service.type.msg'></spring:message>");
				return false;
			} else if(!($("#serviceInstanceList").val()) || $("#serviceInstanceList").val().length <= 0){
				showErrorMsg("At least one service instance is required");
				return false;
			}
			var selectedCategory = $("#category").val();
			if(serviceType  == processingType &&  
					(selectedCategory == 'Error' 
					|| selectedCategory == 'Duplicate' 
					|| selectedCategory =='Input' 
					|| selectedCategory == 'Output'
					|| selectedCategory == 'Archive')){
				$("#rule_list_div").hide();
				$("#processing_service_rule_div").hide();
			}else if(serviceType  == processingType && (selectedCategory == 'Invalid' || selectedCategory == 'Filter')){
				$("#rule_list_div").show();
				getAllRuleList();
				$("#processing_service_rule_div").show();
			}else {
				$("#rule_list_div").hide();
			}
		} 
		
	});
	
	$("#categoryPopup").on('change', function() {
		var serviceType = $("#search_service_type").val();
		var selectedCategory = $("#categoryPopup").val();
		if(serviceType  == processingType && selectedCategory == 'Invalid'){
			$("#reprocess_reason_severity_div_popup").show();
			$("#reprocess_reason_errorcode_div_popup").show();
		}else {
			$("#reprocess_reason_severity_div_popup").hide();
			$("#reprocess_reason_errorcode_div_popup").hide();
		}
		$("#reasonCategoryPopup").val($("#reasonCategoryPopup option:first").val());
		getAllRuleListForPopup();
	});
	
	function getAllRuleList(){
		getInstanceIdList();
		var urlAction  = '<%=ControllerConstants.GET_ALL_RULE_LIST_BY_TYPE%>';
		$.ajax({
			url : urlAction,
			cache : false,
			async : false,
			dataType : 'json',
			type : 'POST',
			data : {
				"serviceIdList" : myCustomObjectForSMAction.join(),
				"category": $("#category").val(),
				"reasonCategory":  $("#reasonCategory").val(),
				"reasonSeverity":  $("#reasonSeverity").val(),
				"reasonErrorCode":  $("#reasonErrorCode").val()
			},
			success : function(data) {
				var response = eval(data);
				var responseCode = response.code;
				var responseMsg = response.msg;
				var responseObject = response.object;
				if (responseCode === "200") {
					$('#ruleList').empty();
					$.each(responseObject, function(index, responseObject) {
				        $("#ruleList").append("<option value='"+responseObject["id"] +"'>"+responseObject["name"]+"</option>");
				    });
				}else {
					showErrorMsg(responseMsg);
				}
			},
			error : function(xhr, st, err) {
				handleGenericError(xhr, st, err);
			}
		});
	}
	
	function getAllRuleListForPopup(){
		getInstanceIdListPopup();
		var urlAction  = '<%=ControllerConstants.GET_ALL_RULE_LIST_BY_TYPE%>';
		$.ajax({
			url : urlAction,
			cache : false,
			async : false,
			dataType : 'json',
			type : 'POST',
			data : {
				"serviceIdList" : myCustomObjectForSMAction.join(),
				"category": $("#categoryPopup").val(),
				"reasonCategory":  $("#reasonCategoryPopup").val(),
				"reasonSeverity":  $("#reasonSeverityPopup").val(),
				"reasonErrorCode":  $("#reasonErrorCodePopup").val()
			},
			success : function(data) {
				var response = eval(data);
				var responseCode = response.code;
				var responseMsg = response.msg;
				var responseObject = response.object;
				if (responseCode === "200") {
					$('#ruleListPopup').empty();
					$.each(responseObject, function(index, responseObject) {
				        $("#ruleListPopup").append("<option value='"+responseObject["id"] +"'>"+responseObject["name"]+"</option>");
				    });
				}else {
					showErrorMsg(responseMsg);
				}
			},
			error : function(xhr, st, err) {
				handleGenericError(xhr, st, err);
			}
		});
	}
	
	var serverInstanceArrayForService = [];
	var myCustomObjectForSMAction = [];
	var myCustomObjectForMultipleAjax = {};
	var serviceIdList ;
	
	function getInstanceIdList(){
		serverInstanceArrayForService = [];
		myCustomObjectForSMAction = [];
		myCustomObjectForMultipleAjax = {};
		$.each($("#serviceInstanceList").val(), function(i, item) {
			var serverInstanceAndService = {};
			var arr = item.split('-');
			serverInstanceAndService.serverInstanceId = parseInt(arr[0]);
			serverInstanceAndService.serviceId = parseInt(arr[1]);
			serverInstanceArrayForService.push(serverInstanceAndService);
			myCustomObjectForSMAction.push(parseInt(arr[1]));
		});
		
		$.each(serverInstanceArrayForService, function(i, item) {
			if(myCustomObjectForMultipleAjax.hasOwnProperty(item["serverInstanceId"])) {
				var existingArray = myCustomObjectForMultipleAjax[item["serverInstanceId"]];
				existingArray.push(item["serviceId"]);
				myCustomObjectForMultipleAjax[item["serverInstanceId"]] = existingArray;
			} else {
				var newArray = [];
				newArray.push(item["serviceId"]);
				myCustomObjectForMultipleAjax[item["serverInstanceId"]] = newArray;
			}
		});
	}
	
	function getInstanceIdListPopup(){
		serverInstanceArrayForService = [];
		myCustomObjectForSMAction = [];
		myCustomObjectForMultipleAjax = {};
		for (i = 0; i < 1; i++) { 
		    var value = $("#serviceInstanceListPopup").val();
		    var serverInstanceAndService = {};
			var arr = value.split('-');
			serverInstanceAndService.serverInstanceId = parseInt(arr[0]);
			serverInstanceAndService.serviceId = parseInt(arr[1]);
			serverInstanceArrayForService.push(serverInstanceAndService);
			myCustomObjectForSMAction.push(parseInt(arr[1]));
		}
		
		$.each(serverInstanceArrayForService, function(i, item) {
			if(myCustomObjectForMultipleAjax.hasOwnProperty(item["serverInstanceId"])) {
				var existingArray = myCustomObjectForMultipleAjax[item["serverInstanceId"]];
				existingArray.push(item["serviceId"]);
				myCustomObjectForMultipleAjax[item["serverInstanceId"]] = existingArray;
			} else {
				var newArray = [];
				newArray.push(item["serviceId"]);
				myCustomObjectForMultipleAjax[item["serverInstanceId"]] = newArray;
			}
		});
	}
	
	function manageReasonCategoryOption(){
		var selectedCategory = $("#category").val();
		if(selectedCategory == 'Filter'){
			$("#reasonCategory option[value='Missing']").remove();
			$("#reasonCategory option[value='Default']").remove();
			$("#reasonCategory option[value='Other']").remove();
			$("#reasonCategory option[value='NA']").remove();
			$("#reasonCategory option[value='Business Validations']").remove();
			$("#reasonCategory option[value='Look up failure']").remove();
			$("#reasonCategory option[value='Multiple values return failure']").remove();
			if($("#reasonCategory option[value='Filter']").length == 0)
				addPopupListValues('reasonCategory','Filter','Filter','Filter');
			$('#reprocess_reason_category_div').show();
			$('#reprocess_reason_severity_div').hide();
			$('#reprocess_reason_errorcode_div').hide();
			$('#reasonSeverity').val('-1');
			$("#reasonErrorCode").val('');
		}else if(selectedCategory == 'Invalid'){
			$('#reprocess_reason_category_div').show();
			$("#reasonCategory option[value='Filter']").remove();
			if($("#reasonCategory option[value='Missing']").length == 0)
				addPopupListValues('reasonCategory','Missing','Missing','Missing');
			if($("#reasonCategory option[value='NA']").length == 0)
				addPopupListValues('reasonCategory','NA','NA','NA');
			if($("#reasonCategory option[value='Default']").length == 0)
				addPopupListValues('reasonCategory','Default','Default','Default');
			if($("#reasonCategory option[value='Other']").length == 0)
				addPopupListValues('reasonCategory','Other','Other','Other');
			if($("#reasonCategory option[value='Business Validations']").length == 0)
				addPopupListValues('reasonCategory','Business Validations','Business Validations','Business Validations');
			if($("#reasonCategory option[value='Look up failure']").length == 0)
				addPopupListValues('reasonCategory','Look up failure','Look up failure','Look up failure');
			if($("#reasonCategory option[value='Multiple values return failure']").length == 0)
				addPopupListValues('reasonCategory','Multiple values return failure','Multiple values return failure','Multiple values return failure');
			
			var selectedReasonCategory = $("#reasonCategory").val();
			if(selectedReasonCategory == 'NA'){
				$('#reprocess_reason_severity_div').hide();
				$('#reprocess_reason_errorcode_div').hide();
			}else{
				$('#reprocess_reason_severity_div').show();
				$('#reprocess_reason_errorcode_div').show();
			}	
		}else if(selectedCategory == '-1'){
			return false;
		}else{
			$('#reprocess_reason_category_div').hide();
			$('#reprocess_reason_severity_div').hide();
			$('#reprocess_reason_errorcode_div').hide();
			$('#reasonSeverity').val('-1');
			$("#reasonErrorCode").val('');
		}
	}
	
	function manageReasonCategoryOptionForPopup(){
		var selectedCategory = $("#categoryPopup").val();
		if(selectedCategory == 'Filter'){
			$('#reprocess_reason_category_div_popup').show();
			$("#reasonCategoryPopup option[value='Missing']").remove();
			$("#reasonCategoryPopup option[value='Default']").remove();
			$("#reasonCategoryPopup option[value='Other']").remove();
			$("#reasonCategoryPopup option[value='NA']").remove();
			$("#reasonCategoryPopup option[value='Business Validations']").remove();
			$("#reasonCategoryPopup option[value='Look up failure']").remove();
			$("#reasonCategoryPopup option[value='Multiple values return failure']").remove();
			if($("#reasonCategoryPopup option[value='Filter']").length == 0)
				addPopupListValues('reasonCategoryPopup','Filter','Filter','Filter');
			$('#reprocess_reason_severity_div').hide();
			$('#reprocess_reason_errorcode_div').hide();
			$('#reasonSeverityPopup').val('-1');
			$("#reasonErrorCodePopup").val('');
			
		}else if(selectedCategory == 'Invalid'){
			$('#reprocess_reason_category_div_popup').show();
			$('#reprocess_reason_severity_div_popup').show();
			$('#reprocess_reason_errorcode_div_popup').show();
			$("#reasonCategoryPopup option[value='Filter']").remove();
			if($("#reasonCategoryPopup option[value='Missing']").length == 0)
				addPopupListValues('reasonCategoryPopup','Missing','Missing','Missing');
			if($("#reasonCategoryPopup option[value='NA']").length == 0)
				addPopupListValues('reasonCategoryPopup','NA','NA','NA');
			if($("#reasonCategoryPopup option[value='Default']").length == 0)
				addPopupListValues('reasonCategoryPopup','Default','Default','Default');
			if($("#reasonCategoryPopup option[value='Other']").length == 0)
				addPopupListValues('reasonCategoryPopup','Other','Other','Other');
			if($("#reasonCategoryPopup option[value='Business Validations']").length == 0)
				addPopupListValues('reasonCategoryPopup','Business Validations','Business Validations','Business Validations');
			if($("#reasonCategoryPopup option[value='Look up failure']").length == 0)
				addPopupListValues('reasonCategoryPopup','Look up failure','Look up failure','Look up failure');
			if($("#reasonCategoryPopup option[value='Multiple values return failure']").length == 0)
				addPopupListValues('reasonCategoryPopup','Multiple values return failure','Multiple values return failure','Multiple values return failure');
			
			var selectedReasonCategory = $("#reasonCategoryPopup").val();
			if(selectedReasonCategory == 'NA'){
			//	$('#reprocess_reason_severity_div').hide();
			//	$('#reprocess_reason_errorcode_div').hide();
			}else{
				//$('#reprocess_reason_severity_div').show();
				//$('#reprocess_reason_errorcode_div').show();
			}	
			
		}else{
			$('#reprocess_reason_category_div_popup').hide();
			$('#reprocess_reason_severity_div_popup').hide();
			$('#reprocess_reason_errorcode_div_popup').hide();
			$('#reasonSeverityPopup').val('-1');
			$("#reasonErrorCodePopup").val('-1');
		}
	}
	
	function addPopupListValues(target,optionId,optionValue,optionText){
		$('#'+ target).append($('<option>', {
			id: optionId,
			value: optionValue,
			text: optionText
		}));
	}
	
	function searchReprocessingFiles() {
		if(!validateReprocessingFileForm()) {
			return;
		}
		var serviceType = $("#search_service_type").val();
		getInstanceIdList();
		$('#processingServiceGrid').jqGrid('clearGridData');
		$('#distributionServiceGrid').jqGrid('clearGridData');
		$('#reprocessFileDetailGrid').jqGrid('clearGridData');
		$('#collectionServiceGrid').jqGrid('clearGridData');
		if(serviceType == processingType) { //processing service
			$("#processing_service_div").show();
			$("#processing-reprocess-buttons-div").show();
			var isShowModifyFilesBtn = isShowModifyFilesButton();
			if(!isShowModifyFilesBtn){
				$("#modifyProcessingFileBtn").hide();
			}else{
				$("#modifyProcessingFileBtn").show();
			}
			var isShowReprocessBtn = isShowReprocessButton();
			if(!isShowReprocessBtn){
				$("#processingReprocessBtn").hide();
			}else{
				$("#processingReprocessBtn").show();
			}
			reloadAutoReloadGridData(true);
		}
		
	}
	function loadProcessingServiceGridDetails(isSearch) {
		/* var service = $("#serviceInstanceListPopup").val().slice(2);	 */
		$("#processingServiceGrid").jqGrid(
				{
					url: "getAutoConfiguredServiceByRule",
		        	postData:{},
					datatype : "json",
					jsonReader: { repeatitems: false, root: "rows", page: "page", total: "total", records: "records"},
					colNames : [	/* "#", */
					            	"id",
					            	"<input type='checkbox' id='selectAllTrigger' onclick='TriggerTableHeaderCheckbox(event, this)'></input>",
					            	"<spring:message code='processing.jqgrid.service.instance.header'></spring:message>",
					            	"Category",
					            	"Rule Name",
					            	"Path",
					            	"Execution Start",
					            	"Edit",
					            	"triggerName",
					            	"triggerId",
					            	"jobId",
					            	"serviceId"
					           ],
					colModel : [
									/* {name : '',	index : '', sortable:false}, */
									{name : 'id',	index : 'id',align:'center',sortable:false,hidden:true},
									{name: 'checkbox', index: 'checkbox',align:'center',sortable:false, formatter:TriggerCheckboxFormatter, align : 'center', width: 30},
									{name : 'serviceName',index : 'serviceName',align:'center',hidden : false,sortable:false},
									{name : 'category',index : 'category',align:'center',hidden : false,sortable:false},
									{name : 'ruleName',index : 'ruleName',align:'center',hidden : false,sortable:false},
									{name : 'path',index : 'path',hidden : false,align:'center',sortable:false},
									{name : 'schedule',index : 'schedule',align:'center',sortable:false},
									{name:'edit',index:'dit',align:'center',align:'center',formatter:editFormatter,sortable:false,width: 50},
									{name:'triggerName',index:'triggerName',hidden:true},
									{name:'triggerId',index:'triggerId',hidden:true},
									{name:'jobId',index:'jobId',hidden:true},
									{name:'serviceId',index:'serviceId',hidden:true}
							   ],
					rowNum:<%= MapCache.getConfigValueAsInteger(SystemParametersConstant.TOTAL_ROWS_TO_DISPLAY_IN_GRID, 10) %>,
					rowList:[5,10,20,50,100], 
	           	    viewrecords: true,
					height : 'auto',
					mtype : 'POST',
					sortname : 'id',
					sortorder : "desc",
					pager : "#processingServiceGridPagingDiv",
					contentType : "application/json; charset=utf-8",
					viewrecords : true,
					multiselect : false,
					timeout : 120000,
					loadtext : "Loading...",
					beforeRequest : function() {
						$(".ui-dialog-titlebar").hide();
					}, 
					beforeSend : function(xhr) {
						xhr.setRequestHeader("Accept", "application/json");
						xhr.setRequestHeader("Content-Type", "application/json");
					},
					loadComplete : function(data) {
						if ($('#processingServiceGrid').getGridParam('records') === 0) {
							$('#processingServiceGrid tbody').html("<div style='padding:6px;background:#D8D8D8;text-align:center;'>No Record found</div>");
							$("#processingServiceGridPagingDiv").hide();
						} else {
							$("#processingServiceGridPagingDiv").show();
							ckIntanceSelected = new Array();
						}
					},
					loadError : function(xhr, st, err) {
						handleGenericError(xhr, st, err);
					},
					beforeSelectRow : function(rowid, e) {
						return false;
					},
					onSelectAll : function(id, status) {
					},
					gridComplete : function() {
						var rowCount = jQuery("#processingServiceGrid").jqGrid('getGridParam', 'records');
						 if(rowCount > 0) {
							$("#processing-reprocess-buttons-div").show();
							$("#file-reprocess-buttons-div").hide();
						} else {
							$("#reprocessFilesBtn").hide();
							$("#deleteReprocessFilesBtn").hide();
						} 
					},
					recordtext: "<spring:message code="serviceManagement.grid.pager.total.records.text"></spring:message>",
			        emptyrecords: "<spring:message code="serviceManagement.grid.empty.records"></spring:message>",
					loadtext: "<spring:message code="serviceManagement.grid.loading.text"></spring:message>",
					pgtext : "<spring:message code="serviceManagement.grid.pager.text"></spring:message>",
				}).navGrid("#processingServiceGridPagingDiv", {
			edit : false,
			add : false,
			del : false,
			search : false
		});
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
	function deleteTriggers(multipleDelete){
		if(multipleDelete){
			$("#deleteTriggersId").val(ckSelectedTriggers.toString());
		}
		$("#delete_triggers_bts_div").hide();
		$("#delete_triggers_progress_bar_div").show();
		$.ajax({
			url: 'deleteAutoErrorReprocessFile',
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
					$("#delete_triggers_progress_bar_div").hide();
					closeFancyBox();
					reloadAutoReloadGridData(false);
					showSuccessMsg(responseMsg);
					ckSelectedLookupTables = new Array();
					deletableRows = {};
					$("#selectAllTrigger").prop('checked',false);
				}else{
					showErrorMsgPopUp(responseMsg);
					$("#delete_triggers_bts_div").show();
					$("#delete_triggers_progress_bar_div").hide();
				}
			},
		    error: function (xhr,st,err){
				handleGenericError(xhr,st,err);
			}
		});
	}
	function deletableRowData(rowId){
		var deletableRecord;
		var rowData = jQuery('#processingServiceGrid').jqGrid ('getRowData', rowId);
		deletableRows[rowId] = rowData.id;
	}
	function getSelectedTriggerList(rowId){
		var rowData = jQuery('#processingServiceGrid').jqGrid ('getRowData', rowId);
		if( document.getElementById("checkbox_"+rowData.id).checked === true){
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
	function TriggerCheckboxFormatter(cellvalue, options, rowObject){
		if ($.inArray( rowObject.id ,  ckSelectedTriggers ) != -1){
			return '<input type="checkbox" id="checkbox_'+rowObject["id"]+'" name="triggerCheckbox" value="'+rowObject["id"]+'" onchange="updateRootCheckbox(this, \'processingServiceGrid\');getSelectedTriggerList('+rowObject.id+');" checked/>';
		} else {
			return '<input type="checkbox" id="checkbox_'+rowObject["id"]+'"  name="triggerCheckbox" value="'+rowObject["id"]+'" onchange="updateRootCheckbox(this, \'processingServiceGrid\');getSelectedTriggerList('+rowObject.id+');"/>';
		}
	}
	function updateRootCheckbox(element, gridId) {
		if(!element.checked){
			// if current child checkbox is not checked : uncheck root checkbox
			$('input:checkbox[id="selectAllTrigger"]').prop('checked',false);
		} else {
			//if current child checkboox is checked and all checkbox are checked then check root checkbox
			var count = jQuery("#"+gridId).jqGrid('getGridParam', 'reccount');
			if ($('input:checkbox[name="triggerCheckbox"]:checked').length == count) {
				$('input:checkbox[id="selectAllTrigger"]').prop('checked',true);
		    }
		}
	}
	function editFormatter(cellvalue, options, rowObject){
		var id=rowObject.id+"_edit";
		return '<a id="'+id+'" class="link" onclick="updateAutoErrorReprocess('+"'" + rowObject["id"]+ "'" +','+"'" + cellvalue+ "'" + ');">' + "<i class='fa fa-pencil-square-o' aria-hidden='true'></i></a>";
	}
	function updateAutoErrorReprocess(id,cellValue){
		$("#add_label").hide();
		$("#addAutoErrReproBtn").hide();
		$("#update_label").show();
		$("#updateAutoErrReproBtn").show();
		clearAllMessagesPopUp();
		resetWarningDisplayPopUp();
		var responseObject = jQuery("#processingServiceGrid").jqGrid('getRowData', id);
		$('#id').val(responseObject.id);
		$('#jobId').val(responseObject.jobId);
		$('#schedulerId').val(responseObject.triggerId);
		clearAllMessages();
		getAutoErrorReprocessById(id,responseObject);
	}
	function getAutoErrorReprocessById(id,rowData) {		
		$("#openAddPopup").click();
		$.ajax({
			url: 'getAutoErrorReprocessById',
			cache: false,
			async: true,
			dataType: 'json',
			type:'POST',
			data:{"id":id},
			success: function(data){
				var response = eval(data);
				var responseObject = response.object;
					var gridData = eval(rowData);										
					$("#search_server_inst").val(responseObject.serverInstanceId);
					getServiceInstance(this);
					var updatedData = function(){
						$('#schedulerName').val(gridData.triggerName);
						$('#jqg_schedulerlistgrid'+"_"+gridData.triggerId).attr("checked","checked");
						$('#serviceInstanceListPopup').val(responseObject.serverInstanceId+"-"+gridData.serviceId);
						getAllRuleListForPopup();
						$('#ruleListPopup').val(responseObject.rule);
					};
				   var timeOut= setTimeout(updatedData,300);
				   
					if(responseObject.category=="Invalid"){
						$("#categoryPopup").val("Invalid");
						$("#reprocess_reason_category_div_popup").show();
						$("#reasonCategoryPopup").val(responseObject.reasoncategory);
						//severity
						$("#reprocess_reason_severity_div_popup").show();
						if(responseObject.severity!=null && responseObject.severity!='-1'){
							$("#reasonSeverityPopup").val(responseObject.severity);
						}else{
							$("#reasonSeverityPopup").val('-1');
						}
						//errorcode
						$("#reprocess_reason_errorcode_div_popup").show();
						if(responseObject.errorCode!='undefined' && responseObject.errorCode!=null){
							$("#reasonErrorCodePopup").val(responseObject.errorCode);
						}else{
							$("#reasonErrorCodePopup").val('');
						}
					}else if(responseObject.category=="Filter"){
						$("#categoryPopup").val("Filter");
						$("#reprocess_reason_category_div_popup").show();
						$("#reasonCategoryPopup").val(responseObject.reasoncategory);
						$("#reprocess_reason_errorcode_div_popup").hide();
						$("#reprocess_reason_severity_div_popup").hide();
					}
			},
			error: function (xhr,st,err){
				handleGenericError(xhr,st,err);
			}
		});
		
		
	}
	function isShowReprocessButton(){
		var isShowReprocess=true;
		var categoryVal = $('#category').val();
		if(categoryVal == 'Output' || categoryVal == 'Archive' || categoryVal == 'Input'){
			isShowReprocess=false;
		} 
		return isShowReprocess;
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
			var tableString ="<table class='table table-hover table-bordered'  border='1' ><th>#</th>";
			tableString+="<th>AUTOERRORREPROCESSDETAIL_ID</th>";
			
			$.each(deletableRows, function( index, value ) {
				tableString += "<tr>";
				tableString += "<td><input type='checkbox' name='table_delete' id='table_"+index+"' checked  onclick=getSelectedTrigger('"+index+"')  value='"+index+"' /> </td>";
				/* tableString += "<td>"+ckIntanceSelected[i]+" </td>"; */
				tableString += "<td>"+value+"</td>";
				tableString += "</tr>";
				});	
			
			tableString+="</table>"; 
			//$("#delete_selected_triggers_details").html(tableString);
			$("#deleteCount").html("Total "+ckSelectedTriggers.length + " Records will be deleted.");
			$("#delete_triggers_bts_div").show();
			$("#delete_triggers_progress_bar_div").hide();
			$("#delete_triggers").click();
		}
	}
	
	function isShowModifyFilesButton(){
		var isShowModifyFiles=true;
		var categoryVal = $('#category').val();
		if(categoryVal == 'Output' || categoryVal == 'Archive' || categoryVal == 'Input'){
			isShowModifyFiles=false;
		} 
		return isShowModifyFiles;
	}
	
	function validateReprocessingFileForm(){
		var serviceType = $("#search_service_type").val();
		var selectedCat = $("#category").val();
		var fromTime = $("#fromDate").val();
		var toTime = $("#toDate").val();
		var timeDiff = Math.abs(new Date(toTime) - new Date(fromTime));
		var diffDays = Math.ceil(timeDiff / (1000 * 3600 * 24)); 
		
		var ruleList = $("#ruleList").val();
		if(serviceType == -1){
			showErrorMsg("<spring:message code='file.reprocess.serviceType.invalid' ></spring:message>");
			return false;
		} else if(!($("#serviceInstanceList").val()) || $("#serviceInstanceList").val().length <= 0){
			showErrorMsg("<spring:message code='file.reprocess.serviceInstance.invalid' ></spring:message>");
			return false;
		} else if(selectedCat == -1) {
			showErrorMsg("<spring:message code='file.reprocess.category.invalid' ></spring:message>");
			return false;
		} else if(serviceType == processingType && ((selectedCat == 'Invalid'|| selectedCat == 'Filter') && (ruleList == '-1' || ruleList == null || ruleList == 'undefined' )) ){
			showErrorMsg("<spring:message code='file.reprocess.rule.invalid' ></spring:message>");
			return false;
		} else if(diffDays > maxDurationForFileSearch) {
			showErrorMsg("Difference between From date and To date cannot be more than "+maxDurationForFileSearch+" days. Please select valid dates.");
			return false;
		}else {
			clearAllMessages();
			clearResponseMsgDiv();
			clearResponseMsg();
			clearErrorMsg();
			return true;
		}
		
	}
	
function validateReprocessingFileFormForPopup(){
		var serviceType = $("#search_server_inst").val();
		var selectedCat = $("#categoryPopup").val();
		var triggerId = $("#schedulerId").val();
		var reasoncategory = $("#reasonCategoryPopup").val();
		var severity = $("#reasonSeverityPopup").val();
		var rule=$("#ruleListPopup").val();
		if(serviceType == -1){
			showErrorMsgPopUp("<spring:message code='service.select.server.instance.empty.msg' ></spring:message>");
			return false;
		} else if(!($("#serviceInstanceListPopup").val()) || $("#serviceInstanceListPopup").val().length <= 0){
			showErrorMsgPopUp("<spring:message code='file.reprocess.serviceInstance.invalid' ></spring:message>");
			return false;
		} else if(selectedCat == -1) {
			showErrorMsgPopUp("<spring:message code='file.reprocess.category.invalid' ></spring:message>");
			return false;
		} else if(reasoncategory == null || reasoncategory == 'undefined' || reasoncategory == -1){
			showErrorMsgPopUp("<spring:message code='file.reprocess.rule.invalid' ></spring:message>");
			return false;
		} else if(rule == null || rule == 'undefined' || rule == '-1'){
			showErrorMsgPopUp("<spring:message code='file.reprocess.rule.invalid' ></spring:message>");
			return false;
		} else if(triggerId == ''){
			showErrorMsgPopUp("<spring:message code='trigger.select.require' ></spring:message>");
			return false;
		}else {
			clearAllMessagesPopUp();
			clearResponseMsgDivPopUp();
			clearResponseMsgPopUp();
			clearErrorMsgpopUp();
			return true;
		}
		
	}
	
	function resetReprocessingFiles() {
		resetWarningDisplay();
		clearAllMessages();
		$('#search_service_type').val('PROCESSING_SERVICE');
		$("#search_service_type").trigger("change");
		resetMultiselectDropdown("serviceInstanceList");
		$('#fileNameContains').val('');
		$('#category').val('-1');
		$('#reasonCategory').val('-1');
		$("#rule_list_div").hide();
		$('#reasonSeverity').val('-1');
	}
	
	function resetPopup(){
		resetWarningDisplay();
		$("#schedulerId").val("");
		$("#schedulerName").val("");
		$("#serviceInstanceListPopup option").each(function(index, option) {
		    $(option).remove();
		});
		$('#serviceInstanceListPopup').append("<option value='-1'>Select Service</option>");
		$("#processing-reprocess-buttons-div").hide();
		$('#categoryPopup').val('-1');
		$('#search_server_inst').val('-1');
		$('#reasonCategoryPopup').val('-1');
		$('#ruleListPopup').val('-1');
		$('#reasonSeverityPopup').val('-1');
		$('#reasonErrorCodePopup').val('');
		$('#serviceInstanceListPopup').val('-1');
		//$("#schedulerlistgrid").jqGrid('resetSelection');		
		clearAllMessagesPopUp();
		$("#rule_list_div_popup").show();
		$('#reprocess_reason_category_div_popup').hide();
		$('#reprocess_reason_severity_div_popup').hide();
		$('#reprocess_reason_errorcode_div_popup').hide();
		$("#ruleListPopup option").each(function(index, option) {
		    $(option).remove();
		});
		$('#add_label').show();
		$("#addAutoErrReproBtn").show();
		$('#update_label').hide();
		$("#updateAutoErrReproBtn").hide();
	}
	
	var reprocessFilesObj = {};
	
	function reprocessFiles() {
		if(fileReprocessSelected.length == 0){
			$("#reprocessMessage").click();
			return;
		}
		reprocessFilesObj = getBatchJson("DIRECT_REPROCESS", "reprocessFileDetailGrid", fileReprocessSelected, "PluginId");
		waitingDialog.show();
		$.ajax({
			url: '<%=ControllerConstants.CREATE_ERROR_REPROCESS_BATCH%>',
			cache: false,
			async: true, 
			dataType: 'json',
			headers: {
			    'Content-Type': 'application/json'
			},
			type: "POST",
			data: JSON.stringify(reprocessFilesObj),
			success: function(data){
				var response = eval(data);
				
				var responseCode = response.code;
				var responseMsg = response.msg;
				var responseObject = response.object;
				
				waitingDialog.hide();
				
				if (responseCode === "200") {
					var object = JSON.parse(data.object);
	 				reprocessFilesObj = {};
	 				//fileReprocessSelected = new Array();
	 				continueWithFileSearch();
	 				$("#reprocessBatchId").empty();
	 				$("#reprocessBatchId").html(object.id);
	 				
       				$("#batchIdForViewStatus").val(parseInt(object.id));
       				
	 				$("#reprocessSuccessModal").click();
				}else{
					showErrorMsg(responseMsg);
				}
			},
		    error: function (xhr,st,err){
				handleGenericError(xhr,st,err);
			}
		});
		
		
	}
	
	function deleteReprocessFiles() {
		if(fileReprocessSelected.length == 0){
			$("#deleteMessage").click();
			return;
		}
		$("#deleteConfirmation").click();
	}
	
	function onConfirmDeleteReprocessDetails() {
		reprocessFilesObj = getBatchJson("DELETE_FILE", "reprocessFileDetailGrid", fileReprocessSelected, "PluginId");
		$("#parsing_close_delete_div").click();
		waitingDialog.show();
		$.ajax({
			url: '<%=ControllerConstants.DELETE_FILE_REPROCESS_DETAILS%>',
			cache: false,
			async: true, 
			dataType: 'json',
			headers: {
			    'Content-Type': 'application/json'
			},
			type: "POST",
			data: JSON.stringify(reprocessFilesObj),
			success: function(data){
				var responseCode = data.code; 
				var responseMsg = data.msg;
				waitingDialog.hide();
				if (responseCode === "200") {
					var object = JSON.parse(data.object);
	 				reprocessFilesObj = {};
	 				continueWithFileSearch();
	 				$("#deleteBatchId").empty();
	 				$("#deleteBatchId").html(object.id);
	 				
	 				$("#batchIdForViewStatus").val(parseInt(object.id));
	 				$("#deleteSuccessModal").click();
				}else{
					showErrorMsg(responseMsg);
				}
			},
		    error: function (xhr,st,err){
				handleGenericError(xhr,st,err);
			}
		});
	}
	
	function reloadFileReprocessGridData(){
		var $grid = $("#reprocessFileDetailGrid");
		$grid.jqGrid('setGridParam',{datatype:'json',page: 1}).trigger('reloadGrid');
	}
	
	function reloadSearchGrid(jqGridId, serviceIds) {
		jQuery("#"+jqGridId)
	    .jqGrid('setGridParam',
	        { 
	    		url: '<%=ControllerConstants.GET_REPROCESS_FILES_FROM_SM%>',
	    		postData:
	    		{
	        		'fromDate': function () {
	        			var dateTime = new Date($("#fromDatePicker").datepicker("getDate"));
	        			var strDateTime =  dateTime.getDate() + "-" + (dateTime.getMonth()+1) + "-" + dateTime.getFullYear();
	        			return strDateTime;
	   	    		},
	   	    		'toDate': function () {
	   	    			var dateTime = new Date($("#toDatePicker").datepicker("getDate"));
	        			var strDateTime =  dateTime.getDate() + "-" + (dateTime.getMonth()+1) + "-" + dateTime.getFullYear();
	        			return strDateTime;
	   	    		},
	   	    		'fileNameContains': function(){
	   	    			return $("#fileNameContains").val();
	   	    		},
	   	    		'category': function () {
	        	        return $("#category").val();
	   	    		},
	   	    		'serviceInstanceIds': serviceIds.join()
	    		},
	            datatype: "json"
	        })
	    .trigger("reloadGrid");
	}
	
	function continueWithFileSearch() {
		//delete(hide) selected row from search grid
		$.each(fileReprocessSelected,function(i){
			$("#"+fileReprocessSelected[i],"#reprocessFileDetailGrid").css({display:"none"});
		});
		fileReprocessSelected = [];
	}
	
	function viewFileStatus() {
		closeFancyBox();
		loadReprocessingStatus();
		showButtons('REPROCESSING_STATUS');
	}
	
	var count = 0;
	function checkBoxFormatter(cellvalue, options, rowObject) {
		count++;
		var reprocessFileId = rowObject["ServiceId"]+"_"+rowObject["id"];
		return "<input type='checkbox' name='errorReprocessCheckbox_" + reprocessFileId + "'  id='errorReprocessCheckbox_"
				+ reprocessFileId + "' onclick=\"addRowId('errorReprocessCheckbox_" + reprocessFileId + "\',\'" + rowObject["id"] + "\')\"; />";
		 
	}
	
	function filePathColumnFormatter(cellvalue, options, rowObject) {
		var errorPathId = "errorPath_"+rowObject["ServiceId"]+"_"+rowObject["PluginId"];
		return "<div id='"+errorPathId+"'><center><img src='img/preloaders/circle-red.gif' width='20px' data-toggle='tooltip' data-placement='bottom' title='Loading' height='20px' style='z-index:100px;'></center></div>";
	}
	
	function isShowReprocessButton(){
		var isShowReprocessBtn=true;
		var categoryVal = $('#category').val();
		if(categoryVal == 'Output' || categoryVal == 'Archive' || categoryVal == 'Input'){
			isShowReprocessBtn=false;
		} 
		return isShowReprocessBtn;
	}
	
	function isCategoryUploadNReprocessable(){
		var isShowUpload=true;
		var categoryVal = $('#category').val();
		if(categoryVal == 'Output' || categoryVal == 'Archive' || categoryVal == 'Input'){
			isShowUpload=false;
		} 
		return isShowUpload;
	}
	
	function isCategoryIsOutput(){
		var isOutputCategory=false;
		var categoryVal = $('#category').val();
		if(categoryVal == 'Output'){
			isOutputCategory=true;
		} 
		return isOutputCategory;
	}
	
	function isCategoryViewable(pluginType){
		var isShowView=false;
		if(pluginType == 'ASCII_PARSING_PLUGIN' || pluginType == 'FIXED_LENGTH_ASCII_PARSING_PLUGIN'){
			isShowView=true;
		} 
		return isShowView;
	}
	
	var count1 = 0;
	function checkBoxFormatterForCount(cellvalue, options, rowObject) {
		count1++;
		var reprocessFileId = rowObject["id"];
		var filePath = $("#filePathText").text();//errorReprocessDetailCheckbox_
		return "<center><input type='checkbox' name='errorReprocessDetailCheckbox_" + reprocessFileId + "_"
				+ count1 + "'  id='errorReprocessDetailCheckbox_"
				+ reprocessFileId + "_" + count1
				+ "' onclick=\"addRowId1('errorReprocessDetailCheckbox_" + reprocessFileId + "_"
				+ count1 + "\',\'" + rowObject["id"] + "\')\"; /></center>";
	}
	
	function downloadFormatter(cellvalue, options, rowObject) {
		if(isAccessDownloadFile){
			var serverInstanceId = rowObject["serverInstanceId"];
			var fileName = rowObject["fileName"];
			fileName = encodeURIComponent(fileName)
			var absoluteFileName = rowObject["absoluteFileName"];
			var fileType = rowObject["fileType"];
			absoluteFileName = encodeURIComponent(absoluteFileName);
			return "<center><a href='javascript:void(0)' onclick=downloadErrorFile(\'"+serverInstanceId+"\',\'"+fileName+"\',\'"+absoluteFileName+"\',\'"+fileType+"\');><i class='fa fa-download' aria-hidden='true'></i> </a></center>";	
		}else{
			return "<center><a href='javascript:void(0)'><i class='fa fa-download' aria-hidden='true'></i> </a></center>";
		}
	}
	
	function uploadAndReprocessFormatter(cellvalue, options, rowObject) {
		if(isAccessUploadFile){
			var reprocessFileId = rowObject["id"];
			return "<center><a href='javascript:void(0)' onclick='uploadAndReprocessFile("+reprocessFileId+");'><i class='fa fa-upload' aria-hidden='true'></i></a></center>";	
		}else{
			return "<center><a href='javascript:void(0)'><i class='fa fa-upload' aria-hidden='true'></i></a></center>";
		}
	}
	
	function convertAndDisplayFileSize(cellvalue, options, rowObject){
		var fileSize = rowObject["fileSize"];
		fileSize = getFileSizeInKb(fileSize);
		return fileSize;
	}
	
	function viewFormatter(cellvalue, options, rowObject) {
		if(isViewFile){
			var fileSize = rowObject["fileSize"];
			var fileName = rowObject["fileName"];
			fileName = encodeURIComponent(fileName)
			var absoluteFileName = rowObject["absoluteFileName"];
			absoluteFileName = encodeURIComponent(absoluteFileName)
			var fileType = rowObject["fileType"];
			fileSize = getFileSizeInKb(fileSize);
			var serverInstanceId = rowObject["serverInstanceId"];
			
			if(fileType == '<%=BaseConstants.FILE_GZ_COMPRESS_EXT%>'){
				if(parseFloat(fileSize) > maxCompressFileSize) {   
					return "<center><i class='fa fa-eye' aria-hidden='true'></i></center>";
				} else {
					return "<center><a href='javascript:void(0)' onclick=viewFileDetails(\'"+serverInstanceId+"\',\'"+fileName+"\',\'"+absoluteFileName+"\',\'"+fileType+"\');><i class='fa fa-eye' aria-hidden='true'></i> </a></center>";	
				}
				
			}else{
				if(parseFloat(fileSize) > maxCsvFileSize) {   
					return "<center><i class='fa fa-eye' aria-hidden='true'></i></center>";
				} else {
					return "<center><a href='javascript:void(0)' onclick=viewFileDetails(\'"+serverInstanceId+"\',\'"+fileName+"\',\'"+absoluteFileName+"\',\'"+fileType+"\');><i class='fa fa-eye' aria-hidden='true'></i> </a></center>";	
				}
			}
		}else{
			return "<center><i class='fa fa-eye' aria-hidden='true'></i></center>";
		}
	}
	
	function viewFileDetails(serverInstanceId,fileName,absoluteFilePath,fileType){
		fileName = decodeURIComponent(fileName);
		absoluteFilePath = decodeURIComponent(absoluteFilePath);
		waitingDialog.show();
		
		$.ajax({
			url: '<%=ControllerConstants.VIEW_ERROR_FILE_DETAILS%>',
			cache: false,
			async: true, 
			dataType: 'json',
			type: "POST",
			data:{
				'serverInstanceId':serverInstanceId,
				'fileName':fileName,
				'filePath':absoluteFilePath,
				'fileType':fileType
			},
			success: function(data){
				var response = eval(data);
				var responseCode = response.code;
				var responseMsg = response.msg;
				var responseObject = response.object;
				
				waitingDialog.hide();
				
				if(responseCode == '200'){
					$("#viewFileNameInPopupHeader").empty();
					$("#viewFileNameInPopupHeader").html(fileName);
					$("textarea#fileContentInTextArea").val(responseObject);
					$("#viewFileContentModal").click();
				}else{
					showErrorMsg(responseMsg);
				}
			},
		    error: function (xhr,st,err){
				handleGenericError(xhr,st,err);
			}
		});
	}
	function addRowId(elementId, reprocessFileId) {
		var reprocessFileElement = document.getElementById(elementId);
		if (reprocessFileElement && reprocessFileElement.checked) {
			if (fileReprocessSelected.indexOf(reprocessFileId) === -1) {
				fileReprocessSelected.push(reprocessFileId);
			}
		} else {
			if (fileReprocessSelected.indexOf(reprocessFileId) !== -1) {
				fileReprocessSelected.splice(fileReprocessSelected.indexOf(reprocessFileId), 1);
			}
		}
	}
	
	function uploadAndReprocessFile(reprocessFileId) {
		$("#selectedDetaiFileForUploadAndReprocess").val(reprocessFileId);
		clearFileContent();
		
		clearAllMessagesPopUp();
		
		$("#fileUploadAndReprocess").click();
	}
	
	function viewFile() {
		$("#viewFileNameInPopupHeader").empty();
		$("#viewFileNameInPopupHeader").html("abc.txt");
		$("textarea#fileContentInTextArea").val("test content...");
		$("#viewFileContentModal").click();
	}
	
	
	function downloadErrorFile(serverInstanceId,fileName,filePath, fileType){
		waitingDialog.show();
		fileName = decodeURIComponent(fileName);
		filePath = decodeURIComponent(filePath);
		$.ajax({
			url: 'downloadProcessingFile',
			cache: false,
			async: true, 
			dataType: 'json',
			type: "POST",
			data:{
				'serverInstanceId':serverInstanceId,
				'fileName':fileName,
				'filePath': decodeURIComponent(filePath),
				'fileType':fileType
			},
			success: function(data){
				var response = eval(data);
				var responseCode = response.code;
				var responseMsg = response.msg;
				var responseObject = response.object;
				
				waitingDialog.hide();
				
				if(responseCode == '200'){
					$.fileDownload(responseObject);	
				}else{
					showErrorMsg(responseMsg);
				}
			},
		    error: function (xhr,st,err){
				handleGenericError(xhr,st,err);
			}
		});
	}
	
	function isObjectAlreadyExists(arrayObject, object, objectValue) {
		var isExists = false;
		$.each(arrayObject, function(key,value){
			if(value[object] && value[object]["id"] == objectValue) {
				isExists = true;
			}
		});
		return isExists;
	}
	
	var fileReprocessCountSelected = new Array();
	function addRowId1(elementId, reprocessFileId) {
		var reprocessFileElement = document.getElementById(elementId);
		if (reprocessFileElement && reprocessFileElement.checked) {
			if (fileReprocessCountSelected.indexOf(reprocessFileId) === -1) {
				fileReprocessCountSelected.push(reprocessFileId);
			}
		} else {
			if (fileReprocessCountSelected.indexOf(reprocessFileId) !== -1) {
				fileReprocessCountSelected.splice(fileReprocessSelected.indexOf(reprocessFileId), 1);
			}
		}
	}
	
	
	function getErrorReprocessJsonFromEngine(serverInstanceIdToServiceListObject) {

		$.each(serverInstanceIdToServiceListObject, function(key,value){
		    $.ajax({
		    	url: '<%=ControllerConstants.GET_REPROCESS_FILE_DETAIL_FROM_ENGINE%>',
				cache: false,
				async: true, 
				dataType: 'json',
					type: "POST",
					data: {
						'fromDate': function () {
							var dateTime = new Date($("#fromDatePicker").datepicker("getDate"));
		        			var strDateTime =  dateTime.getDate() + "-" + (dateTime.getMonth()+1) + "-" + dateTime.getFullYear();
		        			return strDateTime;
			    		},
			    		'toDate': function () {
			    			var dateTime = new Date($("#toDatePicker").datepicker("getDate"));
		        			var strDateTime =  dateTime.getDate() + "-" + (dateTime.getMonth()+1) + "-" + dateTime.getFullYear();
		        			return strDateTime;
			    		},
			    		'fileNameContains': function(){
			    			return $("#fileNameContains").val();
			    		},
			    		'category': function () {
		    	        return $("#category").val();
			    		},
			    		'serviceInstanceIds': value.join() 
			    		},
				success: function(data){
	 				if(data.code == "200") {
	 					$.each(data.object, function(i, item) {
		 				   	for (var key in item) {
						  		if (item.hasOwnProperty(key)) {
						  			//$("input:checkbox[id^=\""+key+"\"]").removeAttr("disabled");
						  			
						  			if(key.indexOf("fileDetails") >= 0) {
						  				$("#"+key).empty();
			 		 				   	$("#"+key).html(JSON.stringify(item[key]));
						  			}else if(key.indexOf("errorPath") >= 0){
						  				var dynamicKey = key.replace('errorPath','errorReprocessCheckbox');
						  				if(parseInt(item[key]) < 0){
								  			$("div[id^=\""+key+"\"]").empty();
								  			$("div[id^=\""+key+"\"]").html('<%=BaseConstants.SERVICE_INSTANCE_NOT_RUNNING%>');
						  				}else{
						  					$("#"+key).empty();
						  					$("#"+key).html(item[key]);
						  				}
						  			} else if(key.indexOf("fileCount") >= 0) {
						  				$("#"+key).empty();
						  				var dynamicKey = key.replace('fileCount','errorReprocessCheckbox');
						  				if(parseInt(item[key]) < 0) {
						  					$("#"+key).html("<center>"+item[key]+"</center>");
						  					$("#"+dynamicKey).attr("disabled", true);
						  					$("div[id^=\""+key+"\"]").html('<%=BaseConstants.SERVICE_INSTANCE_NOT_RUNNING%>');
						  				} else {
						  					if(parseInt(item[key]) == 0){
							  					$("#"+dynamicKey).attr("disabled", true);
						  						$("#"+key).html("<center>"+item[key]+"</center>");
						  						$("#"+key).prop('onclick',null).off('click');
						  					}else{
							  					$("#"+key).html("<center><a style='text-decoration: underline; cursor: pointer;' id='"+key+"_href'>"+item[key]+"</a></center>");
							  					$("#"+dynamicKey).removeAttr("disabled");
						  					}
						  				}
						  			} else {
						  				$("#"+key).empty();
			 		 				   	$("#"+key).html("<center>"+item[key]+"</center>");	
						  			}
		 					  	}
		 					}
		 				});
	 				} else {
	 					if(data && data.object) {
	 						$.each(data.object, function(i, item) {
			 				   	for (var key in item) {
							  		if (item.hasOwnProperty(key)) {
							  			$("input:checkbox[id^=\""+key+"\"]").attr("disabled", true);
							  			$("div[id^=\""+key+"\"]").empty();
							  			$("div[id^=\""+key+"\"]").html(item[key]);
			 					  	}
			 					}
			 				});
	 					}
	 					
	 				}
	 				
				},
			    error: function (xhr,st,err){
					handleGenericError(xhr,st,err);
				}
			});
		});
	}
	
	function fileReprocessInCountBtn() {
		if(fileReprocessCountSelected.length == 0){
			$("#reprocessMessage").click();
			return;
		}
		reprocessFilesObj = getBatchJsonForCountPageAction("DIRECT_REPROCESS", "fileCountDetails", fileReprocessCountSelected, "pluginId");
		
		waitingDialog.show();
		$.ajax({
			url: '<%=ControllerConstants.CREATE_ERROR_REPROCESS_BATCH%>',
			cache: false,
			async: true, 
			dataType: 'json',
			headers: {
			    'Content-Type': 'application/json'
			},
			type: "POST",
			data: JSON.stringify(reprocessFilesObj),
			success: function(data){
				
				var response = eval(data);
				var responseCode = response.code;
				var responseMsg = response.msg;
				var responseObject = response.object;
				
				waitingDialog.hide();
				
				if (responseCode === "200") {
					var object = JSON.parse(data.object);
	 				reprocessFilesObj = {};
	 				continueWithFileSearchOnCount('PARSING_SERVICE', 0);
	 				$("#reprocessBatchId").empty();
	 				$("#reprocessBatchId").html(object.id);
	 				$("#batchIdForViewStatus").val(parseInt(object.id));
	 				$("#reprocessSuccessModal").click();
				}else{
					showErrorMsg(responseMsg);
				}
				
			},
		    error: function (xhr,st,err){
				handleGenericError(xhr,st,err);
			}
		});
	}
	
	function deleteFilesInCountBtn() {
		if(fileReprocessCountSelected.length == 0){
			$("#deleteMessage").click();
			return;
		}
		$("#deleteCountConfirmation").click();
	}
	
	function onConfirmDeleteCountReprocessDetails() {
		reprocessFilesObj = getBatchJsonForCountPageAction("DELETE_FILE", "fileCountDetails", fileReprocessCountSelected, "pluginId");
		
		$("#parsing_file_details_close_div").click();
		waitingDialog.show();
		
		$.ajax({
			url: '<%=ControllerConstants.DELETE_FILE_REPROCESS_DETAILS%>',
			cache: false,
			async: true, 
			dataType: 'json',
			headers: {
			    'Content-Type': 'application/json'
			},
			type: "POST",
			data: JSON.stringify(reprocessFilesObj),
			success: function(data){
				
				var response = eval(data);
				var responseCode = response.code;
				var responseMsg = response.msg;
				
				waitingDialog.hide();
				
				if (responseCode === "200") {
					var object = JSON.parse(data.object);
	 				reprocessFilesObj = {};
	 				
	 				continueWithFileSearchOnCount('PARSING_SERVICE', 0);
	 				$("#deleteBatchId").empty();
	 				$("#deleteBatchId").html(object.id);
	 				$("#batchIdForViewStatus").val(parseInt(object.id));
	 				$("#deleteSuccessModal").click();
	 				
				}else{
					showErrorMsg(responseMsg);
				}
				
			},
		    error: function (xhr,st,err){
				handleGenericError(xhr,st,err);
			}
		});
	}
	
	function getBatchJsonForCountPageAction(errorProcessAction, jqGridId, selectedIdArray, fileDetailsPostfixIdName) {
		reprocessFilesObj = {};
		var newArray = [];
		
		$.each(selectedIdArray,function(i){
			
		   	var responseObject = jQuery("#"+jqGridId).jqGrid ('getRowData', selectedIdArray[i]);
	    	var newObject = {};
			newObject.fileName = responseObject["fileName"];
			newObject.fileSize = parseFloat(getFileSizeinBytes(responseObject["fileSize"]));
			newObject.absoluteFilePath = responseObject["absoluteFileName"];
			
			if(responseObject["fileType"] == '<%=BaseConstants.FILE_GZ_COMPRESS_EXT%>' ){
				newObject.isCompress = 'true';	
			}else{
				newObject.isCompress = 'false';
			}
			
			if(responseObject["sourceFileType"] == '<%=BaseConstants.FILE_GZ_COMPRESS_EXT%>'){
				newObject.isInputSourceCompress = 'true';	
			}else{
				newObject.isInputSourceCompress = 'false';
			}
			
			newObject.failureReason = "";
			newObject.service = responseObject["serviceId"];
			var isObjExists = isObjectAlreadyExists(newArray, "service", responseObject["serviceId"]) ;
			if(isObjExists) {
				newObject.service = responseObject["serviceId"];
				newObject.svctype = responseObject["serviceTypeId"];
				newObject.serverInstance = responseObject["serverInstanceId"];
			} else {
				newObject.service = {};
				newObject.service.id = responseObject["serviceId"];
				newObject.svctype = {};
				newObject.svctype.id = responseObject["serviceTypeId"];
				newObject.serverInstance = {};
				newObject.serverInstance.id = responseObject["serverInstanceId"];
			}
			if(fileDetailsPostfixIdName == "pluginId") {
				var pluginIdForParsing = responseObject["pluginId"];
				if(pluginIdForParsing.indexOf("DEFAULT") !== -1) {
					isObjExists = isObjectAlreadyExists(newArray, "parser", 0) ;
					if(isObjExists) {
						newObject.parser = 0;
					} else {
						newObject.parser = {};
						newObject.parser.id = 0;
					}
					isObjExists = isObjectAlreadyExists(newArray, "composer", 0) ;
					if(isObjExists) {
						newObject.composer = 0;
					} else {
						newObject.composer = {};
						newObject.composer.id = 0;
					}
				} else {
					isObjExists = isObjectAlreadyExists(newArray, "parser", responseObject["pluginId"]) ;
					if(isObjExists) {
						newObject.parser = responseObject["pluginId"];
					} else {
						newObject.parser = {};
						newObject.parser.id = responseObject["pluginId"];
					}
					isObjExists = isObjectAlreadyExists(newArray, "composer", 0) ;
					if(isObjExists) {
						newObject.composer = 0;
					} else {
						newObject.composer = {};
						newObject.composer.id = 0;
					}
				}
			} else if(fileDetailsPostfixIdName == "composerId") {
				
				var composerIdForDistribution = responseObject["composerId"];
				if(composerIdForDistribution.indexOf("DEFAULT") !== -1) {
					isObjExists = isObjectAlreadyExists(newArray, "composer", 0) ;
					if(isObjExists) {
						newObject.composer = 0;
					} else {
						newObject.composer = {};
						newObject.composer.id = 0;
					}
					isObjExists = isObjectAlreadyExists(newArray, "parser", 0) ;
					if(isObjExists) {
						newObject.parser = 0;
					} else {
						newObject.parser = {};
						newObject.parser.id = 0;
					}					
				} else {
					isObjExists = isObjectAlreadyExists(newArray, "composer", composerIdForDistribution) ;
					if(isObjExists) {
						newObject.composer = composerIdForDistribution;
					} else {
						newObject.composer = {};
						newObject.composer.id = composerIdForDistribution;
					}
					isObjExists = isObjectAlreadyExists(newArray, "parser", 0) ;
					if(isObjExists) {
						newObject.parser = 0;
					} else {
						newObject.parser = {};
						newObject.parser.id = 0;
					}	
				}
			}
			
			newObject.filePath = responseObject["errorPath"];
			newObject.readFilePath = responseObject["readPath"]; 
			newArray.push(newObject);
			
		});
		reprocessFilesObj.reprocessDetailList = newArray;
		reprocessFilesObj.errorProcessAction = errorProcessAction;
		reprocessFilesObj.userComment = "";
		reprocessFilesObj.errorCategory = $("#category").val(); 
		
		return reprocessFilesObj;
	}
	
	function getBatchJson(errorProcessAction, jqGridId, selectedIdArray, fileDetailsPostfixIdName) {
		reprocessFilesObj = {};
		var newArray = [];
		$.each(selectedIdArray,function(i){
		   	var responseObject = jQuery("#"+jqGridId).jqGrid ('getRowData', selectedIdArray[i]);
		   	
			var fileDetails = $("#fileDetails_"+responseObject["ServiceId"]+"_"+responseObject[fileDetailsPostfixIdName]).text();
			
			fileDetails = JSON.parse(fileDetails);
			$.each(fileDetails, function(key,value){
		    	var newObject = {};
				newObject.fileName = value.fileName;
				newObject.fileSize = value.fileSize;
				
				newObject.absoluteFilePath = value.absoluteFileName;
				
				if(value.sourceFileType == '<%=BaseConstants.FILE_GZ_COMPRESS_EXT%>'){
					newObject.isInputSourceCompress = 'true';	
				}else{
					newObject.isInputSourceCompress = 'false';
				}
				
				if(value.fileType == '<%=BaseConstants.FILE_GZ_COMPRESS_EXT%>'){
					newObject.isCompress = 'true';	
				}else{
					newObject.isCompress = 'false';
				}
				
				newObject.failureReason = "";
				
				newObject.service = responseObject["ServiceId"];
				var isObjExists = isObjectAlreadyExists(newArray, "service", responseObject["ServiceId"]) ;
				if(isObjExists) {
					newObject.service = responseObject["ServiceId"];
					newObject.svctype = responseObject["ServiceTypeId"];
					newObject.serverInstance = responseObject["ServerInstanceId"];
				} else {
					newObject.service = {};
					newObject.service.id = responseObject["ServiceId"];
					newObject.svctype = {};
					newObject.svctype.id = responseObject["ServiceTypeId"];
					newObject.serverInstance = {};
					newObject.serverInstance.id = responseObject["ServerInstanceId"];
				}
				if(fileDetailsPostfixIdName == "PluginId") {
					var pluginIdForParsing = responseObject["PluginId"];
					if(pluginIdForParsing.indexOf("DEFAULT") !== -1) {
						isObjExists = isObjectAlreadyExists(newArray, "parser", 0);
						if(isObjExists) {
							newObject.parser = 0;
						} else {
							newObject.parser = {};
							newObject.parser.id = 0;
						}
						isObjExists = isObjectAlreadyExists(newArray, "composer", 0) ;
						if(isObjExists) {
							newObject.composer = 0;
						} else {
							newObject.composer = {};
							newObject.composer.id = 0;
						}
					} else {
						isObjExists = isObjectAlreadyExists(newArray, "parser", responseObject["PluginId"]);
						if(isObjExists) {
							newObject.parser = responseObject["PluginId"];
						} else {
							newObject.parser = {};
							newObject.parser.id = responseObject["PluginId"];
						}
						isObjExists = isObjectAlreadyExists(newArray, "composer", 0) ;
						if(isObjExists) {
							newObject.composer = 0;
						} else {
							newObject.composer = {};
							newObject.composer.id = 0;
						}
					}
				} else if(fileDetailsPostfixIdName == "ComposerId") {
					var composerIdForDistribution = responseObject["ComposerId"];
					if(composerIdForDistribution.indexOf("DEFAULT") !== -1) {
						isObjExists = isObjectAlreadyExists(newArray, "composer", 0);
						if(isObjExists) {
							newObject.composer = 0;
						} else {
							newObject.composer = {};
							newObject.composer.id = 0;
						}
						isObjExists = isObjectAlreadyExists(newArray, "parser", 0) ;
						if(isObjExists) {
							newObject.parser = 0;
						} else {
							newObject.parser = {};
							newObject.parser.id = 0;
						}
					} else {
						isObjExists = isObjectAlreadyExists(newArray, "composer", composerIdForDistribution);
						if(isObjExists) {
							newObject.composer = composerIdForDistribution;
						} else {
							newObject.composer = {};
							newObject.composer.id = composerIdForDistribution;
						}
						isObjExists = isObjectAlreadyExists(newArray, "parser", 0) ;
						if(isObjExists) {
							newObject.parser = 0;
						} else {
							newObject.parser = {};
							newObject.parser.id = 0;
						}
					}
					
				} 
				
				newObject.filePath = $("#errorPath_"+responseObject["ServiceId"]+"_"+responseObject[fileDetailsPostfixIdName]).text();
				newObject.readFilePath = responseObject["ReadFilePath"];
				newArray.push(newObject);
			});
			
		});
		reprocessFilesObj["reprocessDetailList"] = newArray;
		reprocessFilesObj.errorProcessAction = errorProcessAction;
		reprocessFilesObj.userComment = "";
		reprocessFilesObj.errorCategory = $("#category").val();
		return reprocessFilesObj;
	}
	
	function continueWithFileSearchOnCount(serviceType, fileUploadDetailId) {
		var newFileDetailArray = [];
		var searchJqGridId = "";
		var detailJqGridId = "";
		var mainId = "";
		var selectedIdArray = [];
		
		if(fileUploadDetailId > 0) {
			var fileUploadIdArray = [];
			fileUploadIdArray.push(fileUploadDetailId);
			if(serviceType == "PARSING_SERVICE") {
				searchJqGridId = "reprocessFileDetailGrid";
				detailJqGridId = "fileCountDetails";
				mainId = "pluginId";
				selectedIdArray = fileUploadIdArray;
			} else if(serviceType == "DISTRIBUTION_SERVICE") {
				searchJqGridId = "distributionServiceGrid";
				detailJqGridId = "distributionService_FileDetails";
				mainId = "composerId";
				selectedIdArray = fileUploadIdArray;
			}
		} else {
			if(serviceType == "PARSING_SERVICE") {
				searchJqGridId = "reprocessFileDetailGrid";
				detailJqGridId = "fileCountDetails";
				mainId = "pluginId";
				selectedIdArray = fileReprocessCountSelected;
			} else if(serviceType == "DISTRIBUTION_SERVICE") {
				searchJqGridId = "distributionServiceGrid";
				detailJqGridId = "distributionService_FileDetails";
				mainId = "composerId";
				selectedIdArray = fileReprocessDistributionDetailSelected;
			}
		}		
		
		
		
			//get all file details object from search screen
			var allFileDetails = getFileDetailsFromDetail(searchJqGridId, detailJqGridId, selectedIdArray, mainId);
			
			//get all file details object from detail screen
			var selectedFileDetails = getSelectedFileDetails(detailJqGridId, selectedIdArray);
			
			//remove file object of detail screen from search screen
			var newFileDetailsArray = [];
			$.each(allFileDetails, function( key1, value1 ) {
				var isExists = false;
				$.each(selectedFileDetails, function( key2, value2 ) {
					if(value1.fileName == value2.fileName ) {
						isExists = true;
					}
				});
				if(!isExists) {
					newFileDetailsArray.push(value1);
				}
			});
			
			//assign new file object array to search screen
			var fileDetailsId = getFileDetailId(searchJqGridId, detailJqGridId, selectedIdArray, mainId);
			$("#"+fileDetailsId).empty();
			$("#"+fileDetailsId).html(JSON.stringify(newFileDetailsArray));
			
			 //assign new file count in search screen
			var fileCountId = getFileCountId(searchJqGridId, detailJqGridId, selectedIdArray, mainId);
			var fileCounts = $("#"+fileCountId).text();
			fileCounts = parseInt(fileCounts);
			fileCounts = fileCounts-selectedIdArray.length;
			$("#"+fileCountId).empty();
			$("#"+fileCountId).html(fileCounts);
			
			//if new file count is 0 in search screen than disable checkbox of that row, and
			// change style of a tag : remove hyperlink style
			if(fileCounts == 0) {
				//disable checkbox
				var checkboxId = getCheckboxId(searchJqGridId, detailJqGridId, selectedIdArray, mainId);
				$('#'+checkboxId).attr('disabled', true);
				
				//remove style from a tag
				$("#"+fileCountId).removeAttr('style');
				
				//remove style from div tag
				var fileCountDivId = getFileCountDivId(searchJqGridId, detailJqGridId, selectedIdArray, mainId);
				$("#"+fileCountDivId).removeAttr('style');
				
				//add black color to a tag
				$("#"+fileCountId).css('color','#222222');
			} 
			
			//delete selected row in detail screen
			deleteSelectedRowFromDetailScreen(detailJqGridId, selectedIdArray);
			
			$("input:checkbox[id^=\"errorReprocessDetailCheckbox_\"]").removeAttr('checked');
			closeFancyBox();
			fileReprocessCountSelected = [];
			fileReprocessDistributionDetailSelected = [];
		
	}
	
	function getFileDetailsFromDetail(searchJqGridId, detailJqGridId, selectedDetailIdArray, mainId) {
		var fileDetails = [];
		$.each(selectedDetailIdArray,function(i){
			var responseObject = jQuery("#"+detailJqGridId).jqGrid ('getRowData', selectedDetailIdArray[i]);
			var pluginIdForSearchGrid = responseObject[mainId];
			var responseObjectSearchGrid = jQuery("#"+searchJqGridId).jqGrid ('getRowData', pluginIdForSearchGrid);
			var fileDetailsId = "fileDetails_"+responseObjectSearchGrid["ServiceId"]+"_"+responseObjectSearchGrid["id"];
			var fileDetailsObj = $('#'+fileDetailsId).text();
			try {
				fileDetails = JSON.parse(fileDetailsObj);
			} catch(err) {
				
			}
			
			return false; 
		});
		return fileDetails;
	}
	
	function getSelectedFileDetails(detailJqGridId, selectedDetailIdArray) {
		var selectedFileDetail = [];
		$.each(selectedDetailIdArray, function(i){
			var responseObject = jQuery("#"+detailJqGridId).jqGrid ('getRowData', selectedDetailIdArray[i]);
			var fileObj = {};
			fileObj.fileName = responseObject["fileName"];
			fileObj.fileSize = responseObject["fileSize"];
			selectedFileDetail.push(fileObj);
		});
		return selectedFileDetail;
	}
	
	function getFileDetailId(searchJqGridId, detailJqGridId, selectedDetailIdArray, mainId) {
		var fileDetailsId = "";
		$.each(selectedDetailIdArray,function(i){
			var responseObject = jQuery("#"+detailJqGridId).jqGrid ('getRowData', selectedDetailIdArray[i]);
			var pluginIdForSearchGrid = responseObject[mainId];
			var responseObjectSearchGrid = jQuery("#"+searchJqGridId).jqGrid ('getRowData', pluginIdForSearchGrid);
			var port = responseObjectSearchGrid["ServiceId"];
			fileDetailsId = "fileDetails_"+port+"_"+responseObjectSearchGrid["id"];
			return false;
		});
		return fileDetailsId;
	}
	
	function getFileCountId(searchJqGridId, detailJqGridId, selectedDetailIdArray, mainId) {
		var fileCountId = "";
		$.each(selectedDetailIdArray,function(i){
			var responseObject = jQuery("#"+detailJqGridId).jqGrid ('getRowData', selectedDetailIdArray[i]);
			var pluginIdForSearchGrid = responseObject[mainId];
			var responseObjectSearchGrid = jQuery("#"+searchJqGridId).jqGrid ('getRowData', pluginIdForSearchGrid);
			var port = responseObjectSearchGrid["ServiceId"];
			fileCountId = "fileCount_"+port+"_"+responseObjectSearchGrid["id"]+"_href";
			return false;
		});
		return fileCountId;
	}
	
	function getFileCountDivId(searchJqGridId, detailJqGridId, selectedDetailIdArray, mainId) {
		var fileCountDivId = "";
		$.each(selectedDetailIdArray,function(i){
			var responseObject = jQuery("#"+detailJqGridId).jqGrid ('getRowData', selectedDetailIdArray[i]);
			var pluginIdForSearchGrid = responseObject[mainId];
			var responseObjectSearchGrid = jQuery("#"+searchJqGridId).jqGrid ('getRowData', pluginIdForSearchGrid);
			var port = responseObjectSearchGrid["ServiceId"];
			fileCountDivId = "fileCount_"+port+"_"+responseObjectSearchGrid["id"];
			return false;
		});
		return fileCountDivId;
	}
	
	function getCheckboxId(searchJqGridId, detailJqGridId, selectedDetailIdArray, mainId) {
		var checkboxId = "";
		$.each(selectedDetailIdArray,function(i){
			var responseObject = jQuery("#"+detailJqGridId).jqGrid ('getRowData', selectedDetailIdArray[i]);
			var pluginIdForSearchGrid = responseObject[mainId];
			var responseObjectSearchGrid = jQuery("#"+searchJqGridId).jqGrid ('getRowData', pluginIdForSearchGrid);
			var port = responseObjectSearchGrid["ServiceId"];
			checkboxId = "errorReprocessCheckbox_"+port+"_"+responseObjectSearchGrid["id"];
			return false;
		});
		return checkboxId;
	}
	
	function deleteSelectedRowFromDetailScreen(detailJqGridId, selectedDetailIdArray) {
		$.each(selectedDetailIdArray,function(i){
			$('#'+detailJqGridId).jqGrid('delRowData', selectedDetailIdArray[i]);
		});
	}

	// Get All Server Instance
	function getServerInstance() {
		if(getServerFlag == false){
			$.ajax({
				url: '<%=ControllerConstants.GET_SERVR_INSTANCE_LIST%>',
				cache: false,
				async: true, 
				dataType: 'json',
				headers: {
				    'Content-Type': 'application/json'
				},
				type: "POST",
				success: function(data){
					$.each(data.object,function(i,value){
						var div_data="<option value="+value.id+">"+value.instanceName+"</option>";
		                $(div_data).appendTo('#search_server_inst'); 
					});
					getServerFlag = true;
				},
			    error: function (xhr,st,err){
					handleGenericError(xhr,st,err);
				}
			});
		}
	}
	function getServiceInstance(element) {
		var serverInst = $('#search_server_inst').val();
		if(serverInst == -1){
			$("#serviceInstanceListPopup option").each(function(index, option) {
			    $(option).remove();
			});
			$('#serviceInstanceListPopup').append("<option value='-1'>Select Service</option>");
			$("#processing-reprocess-buttons-div").hide();
			$('#categoryPopup').val('-1');
			$('#search_server_inst').val('-1');
			$('#reasonCategoryPopup').val('-1');
			$('#ruleListPopup').val('-1');
			$('#reasonSeverityPopup').val('-1');
			$('#reasonErrorCodePopup').val('');
			$('#serviceInstanceListPopup').val('-1');
		}else{
			//var serverId = element.value;
			var serverId = serverInst;
			getServiceListByServer("serviceInstanceListPopup",serverId)
		}
	}
	
	function selectSchedulerNamePopUp(){
		clearAllMessagesPopUp();
		clearResponseMsgDivPopUp();
		clearResponseMsgPopUp();
		clearErrorMsgpopUp();
		var access= $("#triggerAccess").val();
		if(access != "true"){
			showErrorMsgPopUp("You do not have view access of scheduler. Please contact administrator. ");
		}
		else{
			getSchedulerListBySearchParams('<%=ControllerConstants.INIT_TRIGGER_DATA_LIST%>');
			$("#schedulerList").click();
		}
	};
	
	function getSchedulerListBySearchParams(urlAction) {
		$("#schedulerlistgrid").jqGrid({
				url: urlAction,
				datatype: "json",
				postData : {},
				colNames: [
				            "Scheduler Id",
							"<spring:message code='trigger.mgmt.jqgrid.trigger.name' ></spring:message>",
							"<spring:message code='trigger.mgmt.jqgrid.description' ></spring:message>",
							"<spring:message code='trigger.mgmt.jqgrid.Type' ></spring:message>"
					],
				colModel: [
					{name:'id',index:'id',hidden:true},
					{name:'triggerName',index:'triggerName',sortable:true},
					{name:'description',index:'description',  sortable:true},
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
				pager: "#schedulerGridPagingDiv",
				viewrecords: true,
				multiselect: true,
				beforeSelectRow: function(rowid, e)
				{
				    $("#schedulerlistgrid").jqGrid('resetSelection');
				    return (true);
				},
				beforeRequest : function() {
				    $('input[id=cb_schedulerlistgrid]', 'div[id=jqgh_schedulerlistgrid_cb]').remove();
				},
				onSelectRow : function(id){
					rowData = $("#schedulerlistgrid").jqGrid("getRowData", id);
					console.log();
				},
				timeout : 120000,
			    loadtext: "Loading...",
				loadError : function(xhr,st,err) {
					handleGenericError(xhr,st,err);
				},
				recordtext: "<spring:message code="serverManagement.grid.pager.total.records.text"></spring:message>",
			    emptyrecords: "No Record Found.",
			    viewrecords: true,
				}).navGrid("#schedulerGridPagingDiv",{edit:false,add:false,del:false,search:false});
				$(".ui-jqgrid-titlebar").hide();
				$(".ui-pg-input").height("10px");
				
				if ($("#schedulerlistgrid").getGridParam("reccount") === 0) {
					  $(".ui-paging-info").html("No Record Found.");
				}   
		}
 	
		$('#selectSchedulerbtn').click(function(){
			if(rowData){
				$("#schedulerId").val(rowData.id);
				$("#schedulerName").val(rowData.triggerName);
			}else{
				$("#schedulerId").val("");
				$("#schedulerName").val("");
			}
			closeFancyBox();
			$("#openAddPopup").click();
		});
		
		$('#cancleBtn').click(function(){
			closeFancyBox();
			$("#schedulerlistgrid").jqGrid('resetSelection');
			$("#openAddPopup").click();
		});
		
		function openAddPopup() {
			
			$("#divAddNewConfig").click();
			getServerInstance();
		}
		
		function addNewAutoReprocessConfig(actionUrl) {
			if(!validateReprocessingFileFormForPopup()) {
				return;
			}
			clearAllMessagesPopUp();
			clearResponseMsgDivPopUp();
			clearResponseMsgPopUp();
			clearErrorMsgpopUp();
			/* var service = $("#serviceInstanceListPopup").val().slice(2);	 */
			var serviceVal=$("#serviceInstanceListPopup").val();
			var i=serviceVal.indexOf("-");
		    var service = serviceVal.slice(i+1);
			
			var category = $("#categoryPopup").val();
			var serverInstanceId = $("#search_server_inst").val();
			var rule = $("#ruleListPopup").val();
			var errorCode = $("#reasonErrorCodePopup").val();
			var reasoncategory = $("#reasonCategoryPopup").val();
			var severity = $("#reasonSeverityPopup").val();
			var job = $("#schedulerId").val();
			var triggerId = $("#schedulerId").val();
			
			$.ajax({
				url : actionUrl,
				cache : false,
				async : false,
				dataType : 'json',
				type : 'POST',
				data : {
					"service.id" : service,
					"category": category,
					"serverInstanceId": serverInstanceId,
					"severity": severity,
					"reasoncategory":  reasoncategory,
					"rule":rule,
					"errorCode":errorCode,
					"job.triggerId.ID":job,
					"triggerId":triggerId,
					"jobId" : $('#jobId').val(),
					"id"	: $('#id').val()
				},
				success : function(data) {
					var response = eval(data);
					var responseMsg = response.msg;
					closeFancyBox();
					resetPopup();
					reloadAutoReloadGridData(false);
					showSuccessMsg(responseMsg);
				},
				error : function(xhr, st, err) {
					handleGenericError(xhr, st, err);
				}
			});
		}
		function getAllUniqueServerInstanceDetails() {
			var multipleAjaxServerInstanceArrayForService = [];
			var finalServerInstanceObjectForAjax = {};
			var finalServerInstanceObject = {};
			var fullData = jQuery("#processingServiceGrid").jqGrid('getRowData');
			
			for (var i = 0; i < fullData.length; i++) {
				var serverInstanceAndService = {};
				serverInstanceAndService.serverInstanceId = fullData[i].serverInstanceId;
				serverInstanceAndService.serviceId = fullData[i].serviceId;
				multipleAjaxServerInstanceArrayForService.push(serverInstanceAndService);
			}
			$.each(	multipleAjaxServerInstanceArrayForService,function(i, item) {
				if (finalServerInstanceObjectForAjax.hasOwnProperty(item["serverInstanceId"])) {
					var existingArray = finalServerInstanceObjectForAjax[item["serverInstanceId"]];
					existingArray.push(item["serviceId"]);
					finalServerInstanceObjectForAjax[item["serverInstanceId"]] = existingArray;
				} else {
					var newArray = [];
					newArray.push(item["serviceId"]);
					finalServerInstanceObjectForAjax[item["serverInstanceId"]] = newArray;
				}
			});
	return finalServerInstanceObjectForAjax;
}
</script>
