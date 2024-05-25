<%@page import="com.elitecore.core.util.Constant"%>
<%@page import="com.elitecore.sm.common.constants.BaseConstants"%>
<%@page import="com.elitecore.sm.common.constants.EngineConstants"%>
<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<%@page import="com.elitecore.sm.common.constants.SystemParametersConstant"%>
<%@page import="com.elitecore.sm.util.MapCache"%>
<%@taglib 	uri="http://www.springframework.org/tags" 			prefix="spring"%>
<%@ taglib 	uri="http://www.springframework.org/security/tags" 	prefix="sec"  %>
<%@taglib uri="/WEB-INF/ShowErrorTag.tld" prefix="elitecoreError" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<script type='text/javascript' src="${pageContext.request.contextPath}/js/jquery.ajaxfileupload.js"></script>
<!-- <script src="http://jqueryfiledownload.apphb.com/Scripts/jquery.fileDownload.js"></script>  -->
<script type='text/javascript' src="${pageContext.request.contextPath}/js/jquery.fileDownload.js"></script>
<style>
.checkbox{
	color: #000 !important;
}
.btn .caret {
    margin-left: -10px;
}
.btn-group{
	width: 100% !important;
}
.btn-group button{
	width: 100% !important;
}
.btn-default span{
	width: 100% !important;
	background:none !important ;
}
.multiselect-clear-filter {
	line-height: 1.9;
}
.ui-jqgrid .ui-jqgrid-bdiv { overflow-y: scroll;  max-height: 270px; }
.ui-jqgrid tr.jqgrow td{ word-wrap: break-word; }
</style>

<div id="reprocessFileSearchArea">
<div class="tab-content padding0 clearfix" id="reprocessing-file-block">
<div id="note"><div class="errorMessage" id="note"><span id="note" style="color:red;" class="title">Please Note : Archive Category search will be performed based on Grouping type of each service Configuration.</span></div></div>
	<div class="title2">
		<%-- <spring:message code="serverManagement.page.heading" ></spring:message> --%>
		Search files
	</div>
	<div class="fullwidth borbot">
		<div class="col-md-6 inline-form" style="padding-left: 0px !important;">
			<div class="form-group">
             	<spring:message code="file.reprocess.serviceType.label" var="label" ></spring:message>
             	<spring:message code="file.reprocess.serviceType.label.tooltip" var="tooltip" ></spring:message>
         		<div class="table-cell-label">${label}<span class="required">*</span></div>
             	<div class="input-group ">
             		<spring:message code="service.wise.summary.selectServieType" var="selectType" ></spring:message>
             		<select  name = "search_service_type" class="form-control table-cell input-sm"  tabindex="3" id="search_service_type" data-toggle="tooltip" data-placement="bottom"  title="${tooltip}">
             			<option  value="-1" selected="selected">${selectType} </option>
             			<c:if test="${serviceTypeList !=null && fn:length(serviceTypeList) gt 0}">
		             		<c:forEach items="${serviceTypeList}" var="serviceType">
		             			<option value="${serviceType.alias}">${serviceType.type}</option>
		             		</c:forEach>
	             		</c:if>
             		</select>
             		<span class="input-group-addon add-on last" > <i id="search_service_type_error" class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="left" title=""></i></span>
             	</div>
             </div>
            
            <div class="form-group">
         		<spring:message code="file.reprocess.serviceInstanceList.label" var="label" ></spring:message>
         		<spring:message code="file.reprocess.serviceInstanceList.label.tooltip" var="tooltip" ></spring:message>
         		<div class="table-cell-label">${label}<span class="required">*</span></div>
             	<div class="input-group">
             		<select  name = "serviceInstanceList" class="form-control table-cell input-sm"  tabindex="3" multiple="multiple" id="serviceInstanceList" data-toggle="tooltip" data-placement="bottom"  title="${tooltip}">
             		</select>
             		<span class="input-group-addon add-on last" > <i id="serviceInstanceList_error" class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="left" title=""></i></span>
             	</div>
             </div>
            
            <div class="form-group">
             	<spring:message code="file.reprocess.fileNameContains.label" var="label" ></spring:message>
             	<spring:message code="file.reprocess.fileNameContains.label.tooltip" var="tooltip" ></spring:message>
         		<div class="table-cell-label">${label}</div>
             	<div class="input-group ">
             		<input type="text" id="fileNameContains" class="form-control table-cell input-sm" data-toggle="tooltip" data-placement="bottom" tabindex="5" title="${tooltip}">
             		<span class="input-group-addon add-on last" > <i id="fileNameContains_error" class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="left" title=""></i></span>
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
         		<spring:message code="file.reprocess.category.label"  var="label"></spring:message>
         		<spring:message code="file.reprocess.category.label.tooltip"  var="tooltip"></spring:message>
         		<div class="table-cell-label">${label}<span class="required">*</span></div>
             	<div class="input-group ">
             		<spring:message code="select.option.type.category"  var="selectType"></spring:message>
             		<select  name = "category" class="form-control table-cell input-sm"  tabindex="3" id="category" data-toggle="tooltip" data-placement="bottom"  title="${tooltip}"
             		onchange="manageReasonCategoryOption();">
             			<option  value="-1" selected="selected">${selectType}</option>
		             		<c:forEach items="${fileProcessingCategoryEnum}" var="fileProcessingCategoryEnum">
		             			<option value="${fileProcessingCategoryEnum.value}">${fileProcessingCategoryEnum.value}</option>
		             		</c:forEach>
	             		
             		</select>
             		<span class="input-group-addon add-on last" > <i id="category_error" class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="left" title=""></i></span> 
             	</div>
             </div>
            
            <div class="form-group" id = "reprocess_reason_category_div" style="display: none;">
         		<spring:message code="file.reprocess.reason.category.label"  var="label"></spring:message>
         		<spring:message code="file.reprocess.reason.category.tooltip"  var="tooltip"></spring:message>
         		<div class="table-cell-label">${label}<span class="required">*</span></div>
             	<div class="input-group ">
             		<%-- <spring:message code="select.option.type.category"  var="selectType"></spring:message> --%>
             		<select name = "reasonCategory" class="form-control table-cell input-sm"  tabindex="3" id="reasonCategory" data-toggle="tooltip" data-placement="bottom"  title="${tooltip}" onChange="manageReasonCategoryOption(),getAllRuleList()">
             			<%-- <option  value="-1" selected="selected">${selectType}</option> --%>
		             		<c:forEach items="${fileProcessingRuleCategory}" var="fileProcessingRuleCategory">
		             			<option value="${fileProcessingRuleCategory.value}">${fileProcessingRuleCategory.value}</option>
		             		</c:forEach>
	             		
             		</select>
             		<span class="input-group-addon add-on last" > <i id="reasonCategory_error" class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="left" title=""></i></span> 
             	</div>
             </div>
             
             <div class="form-group" id = "reprocess_reason_severity_div" style="display: none;">
         		<spring:message code="file.reprocess.severity.label"  var="label"></spring:message>
         		<spring:message code="file.reprocess.severity.tooltip"  var="tooltip"></spring:message>
         		<div class="table-cell-label">${label}</div>
             	<div class="input-group ">
             		<spring:message code="select.option.type.severity"  var="selectType"></spring:message>
             		<select name = "reasonSeverity" class="form-control table-cell input-sm"  tabindex="3" id="reasonSeverity" data-toggle="tooltip" data-placement="bottom"  title="${tooltip}" onChange="getAllRuleList()">
             			<option  value="-1" selected="selected">${selectType}</option>
	             		<c:forEach items="${fileProcessingRuleSeverity}" var="fileProcessingRuleSeverity">
	             			<option value="${fileProcessingRuleSeverity.value}">${fileProcessingRuleSeverity.value}</option>
	             		</c:forEach>
             		</select>
             		<span class="input-group-addon add-on last" > <i id="reasonSeverity_error" class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="left" title=""></i></span> 
             	</div>
             </div>
             
             
            
            <div class="form-group" id = "rule_list_div" style="display: none;">
            <spring:message code="error.processing.reason.label"  var="label"></spring:message>
            <spring:message code="error.processing.reason.label.tooltip"  var="tooltip"></spring:message>
         		<div class="table-cell-label">${label} <span class="required">*</span></div>
             	<div class="input-group ">
             	
             	<spring:message code="select.option.type.rule"  var="selectType"></spring:message>
             		<select  name = "ruleList" class="form-control table-cell input-sm"  tabindex="5" id="ruleList" data-toggle="tooltip" data-placement="bottom"  title="${tooltip}">
             			<option  value="-1" selected="selected">${selectType}</option>
             		</select>
             		<span class="input-group-addon add-on last" > <i id="ruleList_error" class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="left" title=""></i></span> 
             	</div>
             </div>
            
		 	<div class="form-group">
            	<spring:message code="file.reprocess.fromDate.label"  var="label"></spring:message>
         		<spring:message code="file.reprocess.fromDate.label.tooltip"  var="tooltip"></spring:message>
				<div class="table-cell-label">${label}<span class="required">*</span></div>
				<div class="input-group input-append date" id="fromDatePicker">
					<!-- <span class="input-group-addon add-on"> <i class="fa fa-calendar"></i></span> -->
					<input id="fromDate" readonly class="form-control table-cell input-sm" style="background-color: #ffffff" title="" data-toggle="tooltip" data-placement="bottom" tabindex="6" title="${tooltip}"/>
					<span class="input-group-addon add-on last" > <i id="fromDate_error" class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="left" title=""></i></span> 
				</div>
		 	</div>
            
		 	<div class="form-group">
	            	<spring:message code="file.reprocess.toDate.label"  var="label"></spring:message>
	         		<spring:message code="file.reprocess.toDate.label.tooltip"  var="tooltip"></spring:message>
					<div class="table-cell-label">${label}<span class="required">*</span></div>
					<div class="input-group input-append date" id="toDatePicker">
						<!-- <span class="input-group-addon add-on"> <i class="fa fa-calendar"></i></span> -->
						<input id="toDate" readonly class="form-control table-cell input-sm" style="background-color: #ffffff" title="" data-toggle="tooltip" data-placement="left" tabindex="7" title="${tooltip}"/>
						<span class="input-group-addon add-on last" > <i id="toDate_error" class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="left" title=""></i></span>
					</div>
			 	</div>
		</div>
		<div class="pbottom15">
			<!-- TODO : authorize has role add here -->
				<sec:authorize access="hasAuthority('SEARCH_FILES')">
					<button id="searchReprocessingBtn" class="btn btn-grey btn-xs" onclick="searchReprocessingFiles();" tabindex="8"><spring:message code="btn.label.search" ></spring:message></button>
				</sec:authorize>
   				<button id="resetServerBtn" class="btn btn-grey btn-xs" onclick="resetReprocessingFiles();" tabindex="8"><spring:message code="btn.label.reset" ></spring:message></button>
		</div>
	</div>
	
	<div class="tab-content no-padding clearfix">
       	<div class="fullwidth">
			<div class="title2">
				<spring:message code="file.reprocess.grid.heading" ></spring:message> 
          	</div>
		</div>
   	</div>
	
</div>
<!-- Delete model for file reprocess : start -->
<div id="divDeleteConfirmation" style=" width:100%; display: none;" >
    <div class="modal-content">
        <div class="modal-header padding10">
            <h4 class="modal-title"><spring:message code="label.confirmation.header"></spring:message></h4>
        </div>
        <div class="modal-body padding10 inline-form">
            <p><spring:message code="file.reprocess.grid.checkbox.confirmation.on.delete"></spring:message></p>
        </div>
        <div class="modal-footer padding10">
 			<sec:authorize access="hasAuthority('DELETE_FILE')">       
            	<button type="button" class="btn btn-grey btn-xs " onclick="onConfirmDeleteReprocessDetails()"><spring:message code="btn.label.yes" ></spring:message></button>
            </sec:authorize>	
            <button type="button" class="btn btn-grey btn-xs " id="parsing_close_delete_div" data-dismiss="modal" onclick="closeFancyBox();"><spring:message code="btn.label.no" ></spring:message></button>
        </div>
    </div>     
</div>
<!-- Delete model for file reprocess : end -->

<!-- Delete model for file reprocess : count screen : start -->
<div id="divDeleteCountConfirmation" style=" width:100%; display: none;" >
    <div class="modal-content">
        <div class="modal-header padding10">
            <h4 class="modal-title"><spring:message code="label.confirmation.header"></spring:message></h4>
        </div>
        <div class="modal-body padding10 inline-form">
            <p><spring:message code="file.reprocess.grid.checkbox.confirmation.on.delete"></spring:message></p>
        </div>
        <div class="modal-footer padding10">
        	<sec:authorize access="hasAuthority('DELETE_FILE')"> 
            	<button type="button" class="btn btn-grey btn-xs " onclick="onConfirmDeleteCountReprocessDetails()"><spring:message code="btn.label.yes" ></spring:message></button>
            </sec:authorize>	
            
            <button type="button" class="btn btn-grey btn-xs " id="parsing_file_details_close_div" data-dismiss="modal" onclick="closeFancyBox();"><spring:message code="btn.label.no" ></spring:message></button>
            
        </div>
    </div>     
</div>
<!-- Delete model for file reprocess : count screen : end -->

<!-- No file selected for delete popup : start -->
<div id="divDeleteMessage" style=" width:100%; display: none;" >
    <div class="modal-content">
        <div class="modal-header padding10">
            <h4 class="modal-title"><spring:message code="device.delete.popup.warning.heading.label"></spring:message></h4>
        </div>
        <div class="modal-body padding10 inline-form">
            <p> <spring:message code="file.reprocess.grid.checkbox.validation.on.delete"></spring:message></p>
        </div>
        <div class="modal-footer padding10">
            <button type="button" class="btn btn-grey btn-xs " data-dismiss="modal" onclick="closeFancyBox();"><spring:message code="btn.label.close" ></spring:message></button>
        </div>
    </div>
</div>
<!-- No file selected for delete popup : end -->

<!-- No file selected for reprocess popup : start -->
<div id="divReprocessMessage" style=" width:100%; display: none;" >
    <div class="modal-content">
        <div class="modal-header padding10">
            <h4 class="modal-title"><spring:message code="device.delete.popup.warning.heading.label"></spring:message></h4>
        </div>
        <div class="modal-body padding10 inline-form">
            <p> <spring:message code="file.reprocess.grid.checkbox.validation.on.reprocess"></spring:message></p>
        </div>
        <div class="modal-footer padding10">
            <button type="button" class="btn btn-grey btn-xs " data-dismiss="modal" onclick="closeFancyBox();"><spring:message code="btn.label.close" ></spring:message></button>
        </div>
    </div>
</div>
<!-- No file selected for reprocess popup : end -->

<!-- No file selected for restore popup : start -->
<div id="divRestoreMessage" style=" width:100%; display: none;" >
    <div class="modal-content">
        <div class="modal-header padding10">
            <h4 class="modal-title"><spring:message code="device.delete.popup.warning.heading.label"></spring:message></h4>
        </div>
        <div class="modal-body padding10 inline-form">
            <p> <spring:message code="file.reprocess.grid.checkbox.validation.on.restore"></spring:message></p>
        </div>
        <div class="modal-footer padding10">
            <button type="button" class="btn btn-grey btn-xs " data-dismiss="modal" onclick="closeFancyBox();"><spring:message code="btn.label.close" ></spring:message></button>
        </div>
    </div>
</div>
<!-- No file selected for reprocess popup : end -->


<!-- Reprocess success modal content start  -->
<div id="divReprocessSuccessModal" style="width: 100%; display: none;">
	<div class="modal-content">
		<div class="modal-header padding10">
			<h4 class="modal-title">
				<spring:message code="file.reprocess.reprocessFiles.label" ></spring:message>
			</h4>
		</div>
		<div class="modal-body padding10 inline-form">
			<p>
				<spring:message code="file.reprocess.batch.success" ></spring:message>
			</p>
			<p>
				<spring:message code="file.reprocess.batchId.message" ></spring:message> : <span id="reprocessBatchId"></span>
			</p>
			<p>
				<spring:message code="file.reprocess.continue.message" ></spring:message>
			</p>
			<p>
				<spring:message code="file.reprocess.viewStatus.message" ></spring:message>
			</p>
		</div>
		<div class="modal-footer padding10">
			<button type="button" class="btn btn-grey btn-xs "
				onclick="closeFancyBox();">
				<spring:message code="btn.label.file.reprocess.continue" ></spring:message>
			</button>
			<sec:authorize access="hasAnyAuthority('VIEW_FILE_STATUS')">
			<button type="button" class="btn btn-grey btn-xs "
				data-dismiss="modal" onclick="viewFileStatus();">
				<spring:message code="btn.label.file.reprocess.viewStatus" ></spring:message>
			</button>
			</sec:authorize>
		</div>
	</div>
</div>
<!-- Reprocess success modal content end -->

<!-- Reprocess success modal content start  -->
<div id="divRestoreSuccessModal" style="width: 100%; display: none;">
	<div class="modal-content">
		<div class="modal-header padding10">
			<h4 class="modal-title">
				<spring:message code="file.reprocess.restoreFiles.label" ></spring:message>
			</h4>
		</div>
		<div class="modal-body padding10 inline-form">
			<p>
				<spring:message code="file.reprocess.batchRestore.success" ></spring:message>
			</p>
			<p>
				<spring:message code="file.reprocess.batchId.message" ></spring:message> : <span id="restoreBatchId"></span>
			</p>
			<p>
				<spring:message code="file.reprocess.continue.message" ></spring:message>
			</p>
			<p>
				<spring:message code="file.reprocess.viewStatus.message" ></spring:message>
			</p>
		</div>
		<div class="modal-footer padding10">
			<button type="button" class="btn btn-grey btn-xs "
				onclick="closeFancyBox();">
				<spring:message code="btn.label.file.reprocess.continue" ></spring:message>
			</button>
			<sec:authorize access="hasAnyAuthority('VIEW_FILE_STATUS')">
			<button type="button" class="btn btn-grey btn-xs "
				data-dismiss="modal" onclick="viewFileStatus();">
				<spring:message code="btn.label.file.reprocess.viewStatus" ></spring:message>
			</button>
			</sec:authorize>
		</div>
	</div>
</div>
<!-- Reprocess success modal content end -->

<!-- Reprocess delete success modal content start  -->
<div id="divDeleteSuccessModal" style="width: 100%; display: none;">
	<div class="modal-content">
		<div class="modal-header padding10">
			<h4 class="modal-title">
				<spring:message code="file.reprocess.reprocessFiles.label" ></spring:message>
			</h4>
		</div>
		<div class="modal-body padding10 inline-form">
			<p>
				<spring:message code="file.reprocess.batchDelete.success" ></spring:message>
			</p>
			<p>
				<spring:message code="file.reprocess.batchId.message" ></spring:message> : <span id="deleteBatchId"></span>
			</p>
			<p>
				<spring:message code="file.reprocess.continue.message" ></spring:message>
			</p>
			<p>
				<spring:message code="file.reprocess.viewStatus.message" ></spring:message>
			</p>
		</div>
		<div class="modal-footer padding10">
			<button type="button" class="btn btn-grey btn-xs "
				onclick="closeFancyBox();">
				<spring:message code="btn.label.file.reprocess.continue" ></spring:message>
			</button>
			<sec:authorize access="hasAnyAuthority('VIEW_FILE_STATUS')">
			<button type="button" class="btn btn-grey btn-xs "
				data-dismiss="modal" onclick="viewFileStatus();">
				<spring:message code="btn.label.file.reprocess.viewStatus" ></spring:message>
			</button>
			</sec:authorize>
		</div>
	</div>
</div>
<!-- Reprocess delete success modal content end -->

<!-- View file content : start  -->
<div id="divViewFileContentModal" style="width: 100%; display: none;">
	<div class="modal-content">
		<div class="modal-header padding10">
			<h4 class="modal-title" id="viewFileNameInPopupHeader"></h4>
		</div>
		<div class="modal-body padding10 inline-form">
			<textarea id="fileContentInTextArea" style="height: 350px; width: 780px;" disabled></textarea>
		</div>
		<div class="modal-footer padding10">
			<button type="button" class="btn btn-grey btn-xs " data-dismiss="modal" onclick="closeFancyBox();"><spring:message code="btn.label.close" ></spring:message></button>
		</div>
	</div>
</div>
<!-- View file content : end -->

<!-- File upload and reprocess pop up code (general) start here -->
<a style="display: none;" class="fancybox" id="fileUploadAndReprocess" href="#divFileUploadAndReprocess"></a>
<div id="divFileUploadAndReprocess" style=" width:100%; display: none;" >
	<input type="hidden" id="selectedDetaiFileForUploadAndReprocess"/>
    <div class="modal-content">
        <div class="modal-header padding10">
            <h4 class="modal-title"><spring:message code="btn.label.upload.reprocess" ></spring:message></h4>
        </div>
        <div class="modal-body padding10 inline-form">
        	<jsp:include page="../common/responseMsgPopUp.jsp" ></jsp:include>
			<div class="form-group">
				<spring:message	code="serviceMgmt.import.config.popup.select.file.label" var="tooltip" ></spring:message>
				<div class="table-cell-label">${tooltip}<span class="required">*</span>
				</div>
				<div class="input-group ">
					<input type="file" class="filestyle form-control" tabindex="14"	data-buttonText="<spring:message code="serviceMgmt.browse.file"></spring:message>"
						id="uploadAndReprocessFileObj" name="uploadAndReprocessFileObj" accept='text/xml'>
				</div>
			</div>
        </div>
        <div id="remove-buttons-div" class="modal-footer padding10">
        	<sec:authorize access="hasAuthority('UPLOAD_REPROCESS_FILE')">
            	<button type="button" class="btn btn-grey btn-xs" onclick="uploadAndReprocessAllFile();"><spring:message code="btn.label.upload.reprocess"></spring:message>  </button>
            </sec:authorize>
            	
            <button type="button" class="btn btn-grey btn-xs" id="upload_close_div" data-dismiss="modal" onclick="closeFancyBox();"><spring:message code="btn.label.cancel"></spring:message></button>
        </div>
    </div>
</div>


<!-- File upload and reprocess pop up code (general) end here -->

<!-- Parsing service grid : start  -->
<div class="box-body table-responsive no-padding box" id="reprocessFileDetailGrid_div" style="display:block;">
	<table class="table table-hover" id="reprocessFileDetailGrid"></table>
    <div id="reprocessFileDetailGridPagingDiv"></div> 
</div>
<!-- Parsing service grid : end  -->

<!-- Parsing service grid buttons : start -->
<div class="pbottom15 form-group " id="file-reprocess-buttons-div" style="display:block;"> 
	
	
	<sec:authorize access="hasAuthority('DIRECT_REPROCESS')">
		<button class="btn btn-grey btn-xs" id="reprocessFilesBtn" onclick="reprocessFiles()" tabindex="7"><spring:message code="btn.label.file.reprocess" ></spring:message></button>
	</sec:authorize>
	
	<sec:authorize access="hasAuthority('RESTORE_FILES')">
		<button class="btn btn-grey btn-xs" id="restoreFilesBtn" onclick="restoreFiles()" tabindex="8"><spring:message code="btn.label.file.restore" ></spring:message></button>
	</sec:authorize>
	
	<sec:authorize access="hasAuthority('DELETE_FILE')">
		<button class="btn btn-grey btn-xs" id="deleteReprocessFilesBtn" onclick="deleteReprocessFiles()" tabindex="8"><spring:message code="parsing.service.attr.grid.column.delete" ></spring:message></button>
	</sec:authorize>
	
	<a href="#divDeleteConfirmation" class="fancybox" style="display: none;" id="deleteConfirmation">
   		<spring:message code="btn.label.delete" ></spring:message>
   	</a>
	<a href="#divDeleteMessage" class="fancybox" style="display: none;" id="deleteMessage">
   		<spring:message code="btn.label.delete" ></spring:message>
   	</a>
   	<a href="#divReprocessMessage" class="fancybox" style="display: none;" id="reprocessMessage">
   		<spring:message code="btn.label.delete" ></spring:message>
   	</a>   	
   	<a href="#divDeleteCountConfirmation" class="fancybox" style="display: none;" id="deleteCountConfirmation">
   		<spring:message code="btn.label.delete" ></spring:message>
   	</a>
   	  	
   	<a href="#divReprocessSuccessModal" class="fancybox" style="display: none;" id="reprocessSuccessModal">
   	</a>
   	<a href="#divDeleteSuccessModal" class="fancybox" style="display: none;" id="deleteSuccessModal">
   	</a>
   	<a href="#divViewFileContentModal" class="fancybox" style="display: none;" id="viewFileContentModal">
   	</a>
   	<a href="#divRestoreMessage" class="fancybox" style="display: none;" id="restoreMessage">
   		<spring:message code="btn.label.delete" ></spring:message>
   	</a>
   	<a href="#divRestoreCountConfirmation" class="fancybox" style="display: none;" id="restoreCountConfirmation">
   		<spring:message code="btn.label.delete" ></spring:message>
   	</a>
   		<a href="#divRestoreSuccessModal" class="fancybox" style="display: none;" id="restoreSuccessModal">
   	</a>
   	
</div>
<!-- Parsing service grid buttons : end -->

<!-- Processing service grid : start -->
<div class="box-body table-responsive no-padding box" id="processing_service_div" style="display: none;">
	<table class="table table-hover" id="processingServiceGrid"></table>
    <div id="processingServiceGridPagingDiv"></div> 
</div>
<!-- Processing service grid : end -->

<!-- Processing service grid buttons : start -->
<div class="pbottom15 form-group " id="processing-reprocess-buttons-div" style="display:none;">
	<sec:authorize access="hasAuthority('DIRECT_REPROCESS')">
		<button class="btn btn-grey btn-xs" id="processingReprocessBtn"  tabindex="7"  onclick="reprocessProcessingFiles('false');"><spring:message code="btn.label.file.reprocess" ></spring:message></button>
	</sec:authorize>
	<sec:authorize access="hasAuthority('RESTORE_FILES')">
		<button class="btn btn-grey btn-xs" id="processingRestoreFilesBtn" onclick="restoreProcessingFiles('false');" tabindex="8"><spring:message code="btn.label.file.restore" ></spring:message></button>
	</sec:authorize>
	<sec:authorize access="hasAuthority('MODIFY_FILES')">
		<button class="btn btn-grey btn-xs" id="modifyProcessingFileBtn"  tabindex="8"  onclick="modifyProcessingFiles();"><spring:message code="btn.label.file.modify" ></spring:message></button>
	</sec:authorize>
	<sec:authorize access="hasAuthority('DELETE_FILE')">
		<button class="btn btn-grey btn-xs" id="processingDeleteBtn" tabindex="9" onclick="displayDeletePopup();" ><spring:message code="parsing.service.attr.grid.column.delete" ></spring:message></button>
	</sec:authorize>
</div>
<!-- Processing service grid buttons : end -->

<!-- Distribution service grid : start -->
<div class="box-body table-responsive no-padding box" id="distribution_service_grid_div" style="display: none;">
	<table class="table table-hover" id="distributionServiceGrid"></table>
    <div id="distributionServiceGridPagingDiv"></div> 
</div>
<!-- Distribution service grid : end -->

<!-- Distribution service grid buttons : start -->
<div class="pbottom15 form-group " id="distribution_reprocess_buttons_div" style="display:none;">

	<sec:authorize access="hasAuthority('DIRECT_REPROCESS')">
		<button class="btn btn-grey btn-xs" id="distributionService_SearchReprocessBtn"  tabindex="7"  onclick="distributionServiceReprocessOnSearch();"><spring:message code="btn.label.file.reprocess" ></spring:message></button>
	</sec:authorize>
	<sec:authorize access="hasAuthority('RESTORE_FILES')">
		<button class="btn btn-grey btn-xs" id="distributionRestoreFilesBtn" onclick="restoreDistributionFiles()" tabindex="8"><spring:message code="btn.label.file.restore" ></spring:message></button>
	</sec:authorize>
	<sec:authorize access="hasAuthority('DELETE_FILE')">
		<button class="btn btn-grey btn-xs" id="distributionService_SearchDeleteBtn" tabindex="8" onclick="distributionServiceDeleteOnSearch();" ><spring:message code="parsing.service.attr.grid.column.delete" ></spring:message></button>
	</sec:authorize>

	<a href="#divReprocessSuccessDistributionSearchModal" class="fancybox" style="display: none;" id="reprocessSuccessDistributionSearchModal">
   	</a>
   	<a href="#divDeleteConfirmationDistributionSearch" class="fancybox" style="display: none;" id="deleteConfirmationDistributionSearch">
   	</a>
   	<a href="#divDeleteSuccessDistributionSearchModal" class="fancybox" style="display: none;" id="deleteSuccessDistributionSearchModal">
   	</a>
   	<a href="#divDeleteConfirmationDistributionDetail" class="fancybox" style="display: none;" id="deleteConfirmationDistributionDetail">
   	</a>
   	<a href="#divReprocessSuccessDistributionDetailModal" class="fancybox" style="display: none;" id="reprocessSuccessDistributionDetailModal">
   	</a>
   	<a href="#divDeleteSuccessDistributionDetailModal" class="fancybox" style="display: none;" id="deleteSuccessDistributionDetailModal">
   	</a>
   	
   	<%-- <a href="#divRestoreMessage" class="fancybox" style="display: none;" id="restoreMessage">
   		<spring:message code="btn.label.delete" ></spring:message>
   	</a>
   	<a href="#divRestoreCountConfirmation" class="fancybox" style="display: none;" id="restoreCountConfirmation">
   		<spring:message code="btn.label.delete" ></spring:message>
   	</a> --%>
   	<a href="#divRestoreDistributionSuccessModal" class="fancybox" style="display: none;" id="restoreDistributionSuccessModal">
   	</a>
</div>
<!-- Distribution service grid buttons : end -->


<!-- Collection service grid : start -->
<div class="box-body table-responsive no-padding box" id="collection_service_div" style="display: none;">
	<table class="table table-hover" id="collectionServiceGrid"></table>
    <div id="collectionServiceGridPagingDiv"></div> 
</div>
<a href="#divDeleteConfirmationCollectionSearch" class="fancybox" style="display: none;" id="deleteConfirmationCollectionSearch">#</a>
<a href="#divDeleteConfirmationCollectionDetail" class="fancybox" style="display: none;" id="deleteConfirmationCollectionDetail">#</a>
<a href="#divDeleteConfirmationCollectionSearch" class="fancybox" style="display: none;" id="deleteCollectionReprocessConfirmation">
   		<spring:message code="btn.label.delete" ></spring:message>
</a>
<!-- Collection service grid : end -->

<!-- Collection service grid buttons : start -->
<div class="pbottom15 form-group " id="collection-reprocess-buttons-div" style="display:none;">
	<sec:authorize access="hasAuthority('DELETE_FILE')">
		<button class="btn btn-grey btn-xs" id="collectionDeleteBtn" tabindex="9" onclick="displayCollectionDeletePopup();" ><spring:message code="parsing.service.attr.grid.column.delete" ></spring:message></button>
	</sec:authorize>
</div>

<!-- Collection service grid buttons : end -->


</div>
<div id="reprocessFileViewFileCountArea" style="display: none;">
	<div class="tab-content padding0 clearfix" id="reprocessing-file-block">
		<div class="title2">
			<spring:message code="file.reprocess.searchFiles.label" ></spring:message>
		</div>
		<div class="fullwidth borbot">
			<div class="col-md-6 inline-form" style="padding-left: 0px !important;">
	            
	            <div class="form-group">
	         		<spring:message code="file.reprocess.serverInstance.label" var="label" ></spring:message>
	         		<div class="table-cell-label">${label}</div>
	             	<div class="input-group" id="serverInstanceText"></div>
	             </div>
	            
	            <div class="form-group">
	             	<spring:message code="file.reprocess.serviceInstance.label" var="label" ></spring:message>
	         		<div class="table-cell-label">${label}</div>
	             	<div class="input-group" id="serviceInstanceText"></div>
	            </div>
	            
	            <div class="form-group">
	             	<spring:message code="file.reprocess.pluginName.label" var="label" ></spring:message>
	         		<div class="table-cell-label">${label}</div>
	             	<div class="input-group" id="pluginNameText"></div>
	            </div>
	            
	            <div class="form-group">
	             	<spring:message code="file.reprocess.pluginType.label" var="label" ></spring:message>
	         		<div class="table-cell-label">${label}</div>
	             	<div class="input-group" id="pluginTypeText"></div>
	            </div>
			</div>
			<div class="col-md-6 inline-form">
				<div class="form-group">
	         		<spring:message code="file.reprocess.readPath.label" var="label" ></spring:message>
	         		<div class="table-cell-label">${label}</div>
	             	<div class="input-group" id="readPathText"></div>
	             </div>
	             
	             <div class="form-group">
	         		<spring:message code="file.reprocess.filePath.label" var="label" ></spring:message>
	         		<div class="table-cell-label">${label}</div>
	             	<div class="input-group" id="filePathText"></div>
	             </div>
	             
	             <div class="form-group">
	         		<spring:message code="file.reprocess.fromDate.label" var="label" ></spring:message>
	         		<div class="table-cell-label">${label}</div>
	             	<div class="input-group" id="fromDateText"></div>
	             </div>
	             
	             <div class="form-group">
	         		<spring:message code="file.reprocess.toDate.label" var="label" ></spring:message>
	         		<div class="table-cell-label">${label}</div>
	             	<div class="input-group" id="toDateText"></div>
	             </div>
	            
			</div>
			
			<div class="pbottom15" style="padding-bottom: 0px;">
				&nbsp;
			</div>
		</div>
		<div class="tab-content no-padding clearfix">
       		<div class="fullwidth">
				<div class="title2">
					<spring:message code="file.reprocess.grid.heading.fileDetails" ></spring:message> 
	          	</div>
			</div>
	   	</div>
		<div class="box-body table-responsive no-padding box">
			<table class="table table-hover" id="fileCountDetails"></table>
	         	<div id="fileCountDetailsPagingDiv"></div> 
	 	</div>
	 	<div class="pbottom15">
	 		<sec:authorize access="hasAuthority('DIRECT_REPROCESS')">
				<button id="fileReprocessInCountBtn" class="btn btn-grey btn-xs" onclick="fileReprocessInCountBtn();" tabindex="7"><spring:message code="btn.label.file.reprocess" ></spring:message></button>
			</sec:authorize>
			<sec:authorize access="hasAuthority('RESTORE_FILES')">
				<button id="fileRestoreInCountBtn" class="btn btn-grey btn-xs" onclick="fileRestoreInCountBtn();" tabindex="7"><spring:message code="btn.label.file.restore" ></spring:message></button>
			</sec:authorize>
			<sec:authorize access="hasAuthority('DELETE_FILE')">
				<button id="fileReprocessInCountBtn" class="btn btn-grey btn-xs" onclick="deleteFilesInCountBtn();" tabindex="7"><spring:message code="btn.label.file.reprocess.delete" ></spring:message></button>
			</sec:authorize>			
			<button id="backBtn" class="btn btn-grey btn-xs" onclick="backToReprocessingFile();" tabindex="7"><spring:message code="btn.label.file.reprocess.back" ></spring:message></button>
		</div>
		
	</div>
</div>
<!-- Processing service content page start here -->
 
<jsp:include page="processingServiceReprocessingFile.jsp" flush="true"></jsp:include>
<jsp:include page="applyRuleAndCondition.jsp" flush="true"></jsp:include>
<jsp:include page="distributionServiceReprocessingFile.jsp" flush="true"></jsp:include>
<jsp:include page="collectionServiceReprocessingFile.jsp" flush="true"></jsp:include>

<!-- Processing service content page end here -->

<a style="display: none;" class="fancybox" id="searchFileLimitExceed" href="#divSearchFileLimitExceed"></a>
<div id="divSearchFileLimitExceed" style=" width:100%; display: none;" >
	<input type="hidden" id="searchFileLimitExceed"/>
    <div class="modal-content">
        <div class="modal-header padding10">
            <h4 class="modal-title"><spring:message code="btn.label.warning" ></spring:message></h4>
        </div>
        <div class="modal-body padding10 inline-form">
        	<jsp:include page="../common/responseMsgPopUp.jsp" ></jsp:include>			
        </div>                
        <div id="remove-buttons-div" class="modal-footer padding10">
            <button type="button" class="btn btn-grey btn-xs" id="upload_close_div" data-dismiss="modal" onclick="closeFancyBox();"><spring:message code="btn.label.close"></spring:message></button>
        </div>
    </div>
</div>

<script src="${pageContext.request.contextPath}/customJS/errorReprocessing.js<%= "?v=" + Math.random() %>" type="text/javascript"></script>
<script type="text/javascript">
	var files;
	
	// Add events
	$('input[type=file]').on('change', prepareUpload);

	// Grab the files and set them to our variable
	function prepareUpload(event) {
	  	files = event.target.files;
	}
	
	var isGridCreated = false;
	$(document).ready(function() {
		$("#reprocessFilesBtn").hide();
		$("#restoreFilesBtn").hide();
		$("#deleteReprocessFilesBtn").hide();
		$("#reprocessFileViewFileCountArea").hide();
 		$('#serviceInstanceList').multiselect({
 			enableFiltering: true,
 			enableHTML : true,
			maxHeight: 200,
			dropDown: true,
			buttonWidth: '150px'
		}); 
		
		$("#search_service_type").val("-1");
		
		initializeFromAndToDate();
	});
	
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
			$('#category').val('-1');
			$("#rule_list_div").hide();
		}
		getServiceListByType("serviceInstanceList");
	});
	
	$("#serviceInstanceList").on('change', function() {
		$("#category").val('-1');
		$("#reasonCategory").val($("#reasonCategory option:first").val());
		$("#reasonSeverity").val($("#reasonSeverity option:first").val());
		$("#rule_list_div").hide();
		initializeFromAndToDate();
	});
	
	$("#reasonErrorCode").focusout(function() {
		getAllRuleList();
	});
	
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
	
	function manageReasonCategoryOption(){
		var selectedCategory = $("#category").val();
		if(selectedCategory == 'Filter'){
			$('#reprocess_reason_category_div').show();
			$("#reasonCategory option[value='Missing']").remove();
			$("#reasonCategory option[value='Default']").remove();
			$("#reasonCategory option[value='Other']").remove();
			$("#reasonCategory option[value='NA']").remove();
			$("#reasonCategory option[value='Business Validations']").remove();
			$("#reasonCategory option[value='Look up failure']").remove();
			$("#reasonCategory option[value='Multiple values return failure']").remove();
			if($("#reasonCategory option[value='Filter']").length == 0)
				addPopupListValues('reasonCategory','Filter','Filter','Filter');
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
			
		}else{
			$('#reprocess_reason_category_div').hide();
			$('#reprocess_reason_severity_div').hide();
			$('#reprocess_reason_errorcode_div').hide();
			$('#reasonSeverity').val('-1');
			$("#reasonErrorCode").val('');
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
			
			$("#reprocessFileDetailGrid_div").hide();
			$("#file-reprocess-buttons-div").hide();
			
			$("#distribution_service_grid_div").hide();
			$("#distribution_reprocess_buttons_div").hide();
			
			$("#collection_service_div").hide();
			$("#collection-reprocess-buttons-div").hide();
			
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
			var isShowRestoreBtn = isShowRestoreButton();
			if(!isShowRestoreBtn){
				$("#processingRestoreFilesBtn").hide();
			}else{
				$("#processingRestoreFilesBtn").show();
			}
			
			loadProcessingServiceGridDetails(myCustomObjectForSMAction, myCustomObjectForMultipleAjax);
			
		} else if(serviceType == distributionType) { //distribution service
			
			$("#processing_service_div").hide();
			$("#processing-reprocess-buttons-div").hide();

			$("#reprocessFileDetailGrid_div").hide();
			$("#file-reprocess-buttons-div").hide();
			
			$("#distribution_service_grid_div").show();
			$("#distribution_reprocess_buttons_div").show();
			
			$("#collection_service_div").hide();
			$("#collection-reprocess-buttons-div").hide();
			
			getDistributionFileDetailList(myCustomObjectForSMAction, myCustomObjectForMultipleAjax);
			
			var isShowReprocessBtn = isShowReprocessButton();
			if(!isShowReprocessBtn){
				$("#distributionService_SearchReprocessBtn").hide();
			}else{
				$("#distributionService_SearchReprocessBtn").show();
			}
			var isShowRestorBtn = isShowRestoreButton();
			if(!isShowRestorBtn){
				$("#distributionRestoreFilesBtn").hide();
			}else{
				$("#distributionRestoreFilesBtn").show();
			}
			
		} else if(serviceType == parsingType) { //parsing service
			
			$("#reprocessFileDetailGrid_div").show();
			$("#file-reprocess-buttons-div").show();
			
			$("#processing_service_div").hide();
			$("#processing-reprocess-buttons-div").hide();
			
			$("#distribution_service_grid_div").hide();
			$("#distribution_reprocess_buttons_div").hide();
			
			$("#collection_service_div").hide();
			$("#collection-reprocess-buttons-div").hide();

			getReprocessFileDetailList(myCustomObjectForSMAction, myCustomObjectForMultipleAjax);

			var isShowReprocessBtn = isShowReprocessButton();			
			if(!isShowReprocessBtn){
				$("#reprocessFilesBtn").hide();
			}else{
				$("#reprocessFilesBtn").show();
			}
			var isShowRestorBtn = isShowRestoreButton();
			if(!isShowRestorBtn){
				$("#restoreFilesBtn").hide();
			}else{
				$("#restoreFilesBtn").show();
			}
		} else if(serviceType == collectionType) { //collection service
			
			$("#collection_service_div").show();
			$("#collection-reprocess-buttons-div").show();
			
			$("#processing_service_div").hide();
			$("#processing-reprocess-buttons-div").hide();
			
			$("#distribution_service_grid_div").hide();
			$("#distribution_reprocess_buttons_div").hide();
			
			$("#reprocessFileDetailGrid_div").hide();
			$("#file-reprocess-buttons-div").hide();
			
			getCollectionFileDetailList(myCustomObjectForSMAction, myCustomObjectForMultipleAjax);
		}
	}
	
	function isShowReprocessButton(){
		var isShowReprocess=true;
		var categoryVal = $('#category').val();
		if(categoryVal == 'Output' || categoryVal == 'Archive' || categoryVal == 'Input'){
			isShowReprocess=false;
		} 
		return isShowReprocess;
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
		} else if($("#fromDatePicker").datepicker("getDate").getTime() > $("#toDatePicker").datepicker("getDate").getTime()) {
			showErrorMsg("<spring:message code='file.reprocess.toDateFromDate.invalid' ></spring:message>");
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
	
	function resetReprocessingFiles() {
		resetWarningDisplay();
		clearAllMessages();
		$('#search_service_type').val('-1');
		resetMultiselectDropdown("serviceInstanceList");
		$('#fileNameContains').val('');
		$('#category').val('-1');
		$("#rule_list_div").hide();
		initializeFromAndToDate();
		
		$("#processing_service_div").hide();
		$("#processing-reprocess-buttons-div").hide();

		$("#reprocessFileDetailGrid_div").hide();
		$("#file-reprocess-buttons-div").hide();
		
		$("#distribution_service_grid_div").hide();
		$("#distribution_reprocess_buttons_div").hide();
		
		$("#collection_service_div").hide();
		$("#collection-reprocess-buttons-div").hide();
		
	}
	
	var reprocessFilesObj = {};
	
	function reprocessFiles() {
		if(fileReprocessSelected.length == 0){
			$("#restoreMessage").click();
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
	
	function restoreFiles() {
		if(fileReprocessSelected.length == 0){
			$("#restoreMessage").click();
			return;
		}
		reprocessFilesObj = getBatchJson("ARCHIVE_RESTORE", "reprocessFileDetailGrid", fileReprocessSelected, "PluginId");

		waitingDialog.show();
		$.ajax({
			url: '<%=ControllerConstants.CREATE_ARCHIVE_RESTORE_BATCH%>',
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
	 				$("#restoreBatchId").empty();
	 				$("#restoreBatchId").html(object.id);
	 				
       				$("#batchIdForViewStatus").val(parseInt(object.id));
       				
	 				$("#restoreSuccessModal").click();
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
	
	var oldParsingSearchGrid = '';
	
	var engineDateFormat = '<%=Constant.SHORT_DATE_FORMAT%>';
	function getReprocessFileDetailList(serviceIds, serverInstanceIdToServiceListObject){

		var count = jQuery('#reprocessFileDetailGrid').jqGrid('getGridParam', 'reccount');
		if(count && count > 0) {
		    reloadSearchGrid("reprocessFileDetailGrid", serviceIds);
		} else { 
			$("#reprocessFileDetailGrid").jqGrid({
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
	            datatype: "json",
	            jsonReader: { repeatitems: false, root: "rows", page: "page", total: "total", records: "records"},
	            colNames:[
						  "#",
	                      "<spring:message code='file.reprocess.list.grid.column.id' ></spring:message>",
	                      "<spring:message code='file.reprocess.list.grid.column.pluginId' ></spring:message>",
	                      "<spring:message code='file.reprocess.list.grid.column.fileDetails' ></spring:message>",
	                      "<spring:message code='file.reprocess.list.grid.column.serviceId' ></spring:message>",
	                      "<spring:message code='file.reprocess.list.grid.column.serverInstanceId' ></spring:message>",
	                      "<spring:message code='file.reprocess.list.grid.column.serviceTypeId' ></spring:message>",
						  "<spring:message code='file.reprocess.list.grid.column.serverInstance' ></spring:message>",
						  "<spring:message code='file.reprocess.list.grid.column.serviceInstance' ></spring:message>",
						  "<spring:message code='file.reprocess.list.grid.column.serverIpAndPort' ></spring:message>",
						  "<spring:message code='file.reprocess.list.grid.column.port' ></spring:message>",
	                      "<spring:message code='file.reprocess.list.grid.column.plugInName' ></spring:message>",
	                      "<spring:message code='file.reprocess.list.grid.column.plugInType' ></spring:message>",
	                      "<spring:message code='file.reprocess.list.grid.column.filePath' ></spring:message>",
	                      "<spring:message code='file.reprocess.list.grid.column.readFilePath' ></spring:message>",
	                      "<spring:message code='file.reprocess.list.grid.column.fileCount' ></spring:message>"
	                     ],
	                     colModel: [
							{name : '',	index : '', sortable:false, formatter : checkBoxFormatter, width : "5%", align:'center'},
							{name:'id',index:'id', width : "0%", sortable:false,hidden:true},
							{name:'PluginId',index:'PluginId',sortable:false, width : "0%",hidden:true},
							{name:'FileDetails',index:'FileDetails',sortable:false, width : "0%", hidden:true, formatter:fileDetailsColumnFormatter},
							{name:'ServiceId',index:'ServiceId',sortable:false, width : "0%",hidden:true},
							{name:'ServerInstanceId',index:'ServerInstanceId',sortable:false, width : "0%",hidden:true},
							{name:'ServiceTypeId',index:'ServiceTypeId',sortable:false, width : "0%",hidden:true},
							{name:'ServerInstance',index:'ServerInstance', width : "15%", sortable:false, align:'center'},
							{name:'ServiceInstance',index:'ServiceInstance', width : "15%", sortable:false, align:'center'},
							{name:'ServerIP:Port',index:'ServerIP:Port', width : "15%", sortable:false, align:'center'},
							{name:'Port',index:'Port', width : "0%", hidden:true, sortable:false},
	                    	{name:'PluginName',index:'PluginName', width : "15%", sortable:false, align:'center'},
	                    	{name:'PluginType',index:'PluginType', width : "15%", sortable:false, align:'center'},
	                    	{name:'FilePath',index:'FilePath', width : "15%",formatter:filePathColumnFormatter, sortable:false},
	                    	{name:'ReadFilePath',index:'ReadFilePath', width : "0%", sortable:false, hidden:true},
	                    	{name:'FileCount',index:'FileCount', width : "15%",formatter:fileCountColumnFormatter, sortable:false}
	               		 ],
           		rowList: [],        // disable page size dropdown
           	    pgbuttons: false,   // disable page control like next, back button
           	    pgtext: null,       // disable pager text like 'Page 0 of 10'
           	    viewrecords: false, // disable current view record text like 'View 1-10 of 100'
           	 	rowNum: 'records',
	            height: 'auto',
	            mtype:'POST',
	    		sortname: 'id',
	    		sortorder: "asc",
	    		//pager: "#reprocessFileDetailGridPagingDiv",
	    		contentType: "application/json; charset=utf-8",
	    		//viewrecords: true,
	    		multiselect: false,
	    		timeout : 120000,
	    	    loadtext: "<spring:message code="jq.grid.loading.text"></spring:message>",
	            caption: "<spring:message code="file.reprocess.grid.heading" ></spring:message> ",
	            beforeRequest:function(){
	            	if(oldParsingSearchGrid != ''){
	            		$('#reprocessFileDetailGrid tbody').html(oldParsingSearchGrid);
	            	}
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
	     			if ($('#reprocessFileDetailGrid').getGridParam('records') === 0) {
	     				oldParsingSearchGrid = $('#reprocessFileDetailGrid tbody').html();
						$('#reprocessFileDetailGrid tbody').html(
								"<div style='padding:6px;background:#D8D8D8;text-align:center;'>"
										+ jsSpringMsg.emptyRecord + "</div>");
						//$("#reprocessFileDetailGridPagingDiv").hide();
					} else {
						//$("#reprocessFileDetailGridPagingDiv").show();
						fileReprocessSelected = new Array();
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
						fileReprocessSelected = new Array();
						for (i = 0; i < id.length; i++) {
							fileReprocessSelected.push(id[i]);
						}
					} else {
						fileReprocessSelected = new Array();
					}

				},
				gridComplete : function() {
					resetWarningDisplay();
					clearAllMessages();
					var rowCount = jQuery("#reprocessFileDetailGrid").jqGrid('getGridParam', 'records');
					$("input:checkbox[id^=\"errorReprocessCheckbox_\"]").attr("disabled", true);
					if(rowCount > 0) {
// 						$("#reprocessFilesBtn").show();
						$("#deleteReprocessFilesBtn").show();
						if(serverInstanceIdToServiceListObject) {
							getErrorReprocessJsonFromEngine(serverInstanceIdToServiceListObject);	
						}
					} else {
						$("#reprocessFilesBtn").hide();
						$("#deleteReprocessFilesBtn").hide();
					}
					
				},
	            recordtext: "<spring:message code="jq.grid.pager.total.records.text"></spring:message>",
	    	    emptyrecords: "<spring:message code="jq.grid.empty.records.text"></spring:message>",
	    		loadtext: "<spring:message code="jq.grid.loading.text"></spring:message>",
	    		
	    		}).navGrid("#reprocessFileDetailGridPagingDiv",{edit:false,add:false,del:false,search:false});
	    			$(".ui-jqgrid-titlebar").hide();
	    			$(".ui-pg-input").height("10px"); 
		}
		count = jQuery('#reprocessFileDetailGrid').jqGrid('getGridParam', 'reccount');
		if(count <= 0) {
		    reloadSearchGrid("reprocessFileDetailGrid", serviceIds);
		}

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
	
	function fileCountColumnFormatter(cellvalue, options, rowObject) {
		var postfix = "_"+rowObject["ServiceId"]+"_"+rowObject["PluginId"];
		var fileCountId = "fileCount"+postfix;
		var fileDetailsId = "fileDetails"+postfix;
		var filePathId = "errorPath"+postfix;
		var serverInstance = rowObject["ServerInstance"];
		var serviceInstance = rowObject["ServiceInstance"];
		var pluginName = rowObject["PluginName"];
		var pluginType = rowObject["PluginType"];

		var serverInstanceId = rowObject["ServerInstanceId"];
		var serviceId = rowObject["ServiceId"];
		var serviceTypeId = rowObject["ServiceTypeId"];
		var pluginId = rowObject["PluginId"]
		
		var readFilePath = rowObject["ReadFilePath"];
		
		return "<div id='"+fileCountId+"' style='cursor: pointer;' onclick=\"openFileCountDetailPage('"+fileDetailsId+"', '"+filePathId+"', '"+serverInstance+"', '"+serviceInstance+"', '"+pluginName+"', '"+pluginType+"', '"+readFilePath+"', '"+serverInstanceId+"', '"+serviceId+"', '"+serviceTypeId+"', '"+pluginId+"')\"><center><img src='img/preloaders/circle-red.gif' width='20px' data-toggle='tooltip' data-placement='bottom' title='Loading' height='20px' style='z-index:100px;'></center></div>";
	}
	
	function fileDetailsColumnFormatter(cellvalue, options, rowObject) {
		var fileDetailsId = "fileDetails_"+rowObject["ServiceId"]+"_"+rowObject["PluginId"];
		return "<div id='"+fileDetailsId+"'><img src='img/preloaders/circle-red.gif' width='20px' data-toggle='tooltip' data-placement='bottom' title='Loading' height='20px' style='z-index:100px;'></div>";
	}
	
	var oldParsingCountGrid = '';
	function openFileCountDetailPage(fileDetailsElementId, filePathElementId, serverInstance, serviceInstance, pluginName, pluginType, readFilePath, serverInstanceId, serviceId, serviceTypeId, pluginId) {
		var fileDetails = $('#'+fileDetailsElementId).text();
		$("#serverInstanceText").empty();
	   	$("#serverInstanceText").html(serverInstance);	
	   	$("#serviceInstanceText").empty();
	   	$("#serviceInstanceText").html(serviceInstance);	
	   	$("#pluginNameText").empty();
	   	$("#pluginNameText").html(pluginName);
	   	$("#pluginTypeText").empty();
	   	$("#pluginTypeText").html(pluginType);
	   	$("#readPathText").empty();
	   	$("#readPathText").html(readFilePath);
	   	$("#filePathText").empty();
	   	$("#filePathText").html($('#'+filePathElementId).text());
		$("#fromDateText").empty();
	   	$("#fromDateText").html($("#fromDate").val());
	   	$("#toDateText").empty();
	   	$("#toDateText").html($("#toDate").val());
	   	var data = [];
	   	if(fileDetails) {
	   	    try {
	   	    	data = JSON.parse(fileDetails);
	   	    } catch(e) {
	   	        return; // error in parse json in case of failure message
	   	    }
	   	}
        
        //return if file count is 0
        if(!data || data.length <= 0) {
        	return;
        }
        
        var counter = 0;
        
        $.each(data, function(index, value) {
        	value.serverInstanceId = serverInstanceId;
        	value.serviceId = serviceId;
        	value.serviceTypeId = serviceTypeId; 
        	value.pluginId = pluginId;
        	value.composerId = 0;
        	value.id = ++counter;
        	value.readPath = readFilePath;
        	value.errorPath = $('#'+filePathElementId).text();
       	});
        
        $("#fileCountDetails").GridUnload();
        $grid = $("#fileCountDetails");

		var count = jQuery('#fileCountDetails').jqGrid('getGridParam', 'reccount');
		if(count && count > 0) {
			$('#fileCountDetails').jqGrid('clearGridData');
			jQuery("#fileCountDetails")
		    .jqGrid('setGridParam',
		        { 
		            datatype: 'local',
		            data:data
		        })
		    .trigger("reloadGrid");
		} else {
			 $grid.jqGrid({
	                data: data,
	                datatype: "local",
	                colNames:[
	    					  "#",
	    			          "<spring:message code='file.reprocess.detail.list.grid.column.name' ></spring:message>",
	    			          "<spring:message code='file.reprocess.detail.list.grid.column.size' ></spring:message>",
	    			          "",
	    			          "<spring:message code='file.reprocess.detail.list.grid.column.download' ></spring:message>",
	    			          "<spring:message code='file.reprocess.detail.list.grid.column.uploadAndReprocess' ></spring:message>",
	    			          "<spring:message code='file.reprocess.detail.list.grid.column.view' ></spring:message>",
	    			          "<spring:message code='file.reprocess.detail.list.grid.column.id' ></spring:message>",
	    					  "<spring:message code='file.reprocess.detail.list.grid.column.serverInstanceId' ></spring:message>",
	    					  "<spring:message code='file.reprocess.detail.list.grid.column.serviceId' ></spring:message>",
	    					  "<spring:message code='file.reprocess.detail.list.grid.column.serviceTypeId' ></spring:message>",
	    					  "<spring:message code='file.reprocess.detail.list.grid.column.pluginId' ></spring:message>",
	    			          "<spring:message code='file.reprocess.detail.list.grid.column.composerId' ></spring:message>",
	    			          "<spring:message code='file.reprocess.detail.list.grid.column.readPath' ></spring:message>",
	    			          "<spring:message code='file.reprocess.detail.list.grid.column.errorPath' ></spring:message>",
	    			          "",
	    			          ""
	                    ],
	                    colModel: [
	                        { name: "#", width: 20, formatter : checkBoxFormatterForCount, sortable:false },
	                        { name: "fileName", width: 110, sortable:false, align:'center' ,cellattr: function (rowId, cellValue, rawObject, cm, rdata) { return  "title='"+rawObject["absoluteFileName"]+"'"  ; }},
	                        { name: "fileSize", width: 110, sortable:false,formatter:convertAndDisplayFileSize, align:'center' },
	                        { name: "fileType", width : 110, hidden:true },
	                        { name: "Download", width: 110, formatter : downloadFormatter, sortable:false },
	                        { name: "Upload & Reprocess", width: 110, formatter : uploadAndReprocessFormatter, sortable:false },
	                        { name: "View", width: 110, formatter : viewFormatter, sortable:false},
	                        { name: "id", width: 0, sortable:false, hidden:true },
	                        { name: "serverInstanceId", width: 0, sortable:false, hidden:true },
	                        { name: "serviceId", width: 0, sortable:false, hidden:true },
	                        { name: "serviceTypeId", width: 0, sortable:false, hidden:true },
	                        { name: "pluginId", width: 0, sortable:false, hidden:true },
	                        { name: "composerId", width: 0, sortable:false, hidden:true },
	                        { name: "readPath", width: 0, sortable:false, hidden:true },
	                        { name: "errorPath", width: 0, sortable:false, hidden:true },
	                        { name: "absoluteFileName", width: 0, sortable:false, hidden:true },
	                        { name: "sourceFileType", width: 0, sortable:false, hidden:true }
	                    ],
	                height: 'auto',
	                //mtype:'POST',
	        		sortname: 'id',
	        		sortorder: "asc",
	        		pager: "#reprocessFileDetailGridPagingDiv",
	        		pgbuttons: true,
					rowList:[5,10,20,50,100], 
	        		//contentType: "application/json; charset=utf-8",
	        		viewrecords: true,
	        		multiselect: false,
	        		//timeout : 120000,
	        	    loadtext: "<spring:message code="jq.grid.loading.text"></spring:message>",
	                caption: "<spring:message code="file.reprocess.grid.heading" ></spring:message> ",
	                beforeRequest:function(){
	                	if(oldParsingCountGrid != ''){
	                		$('#fileCountDetails tbody').html(oldParsingCountGrid);
	                	}
	                    $(".ui-dialog-titlebar").hide();
	                }, 
	                beforeSend: function(xhr) {},
	                beforeSelectRow: function(rowid, e) { },
	                loadComplete: function(data) {
	                	var isShowUpload = isCategoryUploadNReprocessable();
						if(!isShowUpload){
							 $('#fileCountDetails').jqGrid('hideCol',["Upload & Reprocess"]); 
						}
						var isShowView = isCategoryViewable(pluginType);
						var isOutputCategory = isCategoryIsOutput();
						if(!isShowView && !isOutputCategory){
							 $('#fileCountDetails').jqGrid('hideCol',["View"]); 
						}
						var isShowReprocessBtn = isShowReprocessButton();
						if(!isShowReprocessBtn){
							 $("#fileReprocessInCountBtn").hide(); 
						}else{
							$("#fileReprocessInCountBtn").show();
						}
						var isShowRestoreBtn = isShowRestoreButton();
						if(!isShowRestoreBtn){
							 $("#fileRestoreInCountBtn").hide(); 
						}else{
							 $("#fileRestoreInCountBtn").show();
						}
	                	fileReprocessCountSelected = new Array();
	                	
	         			$(".ui-dialog-titlebar").show();
	         			$('.checkboxbg').removeAttr("disabled");
	         			$('.checkboxbg').bootstrapToggle();
	         			if ($('#fileCountDetails').getGridParam('records') === 0) {
	         				oldParsingCountGrid = $('#fileCountDetails tbody').html();
	    					$('#fileCountDetails tbody').html(
	    							"<div style='padding:6px;background:#D8D8D8;text-align:center;'>"
	    									+ jsSpringMsg.emptyRecord + "</div>");
	    					$("#fileCountDetailsPagingDiv").hide();
	    				} else {
	    					$("#fileCountDetailsPagingDiv").show();
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

	    			},
	    			gridComplete : function() {
	    				 
	    			},
	                recordtext: "<spring:message code='jq.grid.pager.total.records.text'></spring:message>",
	        	    emptyrecords: "<spring:message code='jq.grid.empty.records.text'></spring:message>",
	        		loadtext: "<spring:message code='jq.grid.loading.text'></spring:message>",
	        		
	        		}).navGrid("#fileCountDetailsPagingDiv",{edit:false,add:false,del:false,search:false});
	        			$(".ui-jqgrid-titlebar").hide();
	        			$(".ui-pg-input").height("10px");
	        			
	        			jQuery("#fileCountDetails")
	        		    .jqGrid('setGridParam',
	        		        { 
	        		            datatype: 'local',
	        		            data:data
	        		        })
	        		    .trigger("reloadGrid");
		}
		$("#reprocessFileSearchArea").hide();
		$("#reprocessFileViewFileCountArea").show();
		
	}
	
	function isShowReprocessButton(){
		var isShowReprocessBtn=true;
		var categoryVal = $('#category').val();
		if(categoryVal == 'Output' || categoryVal == 'Archive' || categoryVal == 'Input'){
			isShowReprocessBtn=false;
		} 
		return isShowReprocessBtn;
	}
	
	function isShowRestoreButton(){
		var isShowRestoreBtn=false;
		var categoryVal = $('#category').val();
		if(categoryVal == 'Archive'){
			isShowRestoreBtn=true;
		}
		return isShowRestoreBtn;
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
	
	
	function backToReprocessingFile() {
		$("input:checkbox[id^=\"errorReprocessDetailCheckbox_\"]").removeAttr('checked');
		$("#reprocessFileSearchArea").show();
		$("#reprocessFileViewFileCountArea").hide();
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
	 					var flag=false;
	 					$.each(data.object, function(i, item) {	 						
	 						if(item['isFileLimitExceed'] == true & flag==false){	 							
	 							$("#searchFileLimitExceed").click();	 		
	 							showErrorMsgPopUp("Count of search results is exceeding 10,000 files. Please provide addition filter/search criteria to view search results.");
	 							flag=true;
	 						}	 						
	 					});
	 					$.each(data.object, function(i, item) {	 											
		 				   	for (var key in item) {		 		
		 				   		if (item.hasOwnProperty(key)) {
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
	
	function fileRestoreInCountBtn() {
		if(fileReprocessCountSelected.length == 0){
			$("#restoreMessage").click();
			return;
		}
		reprocessFilesObj = getBatchJsonForCountPageAction("ARCHIVE_RESTORE", "fileCountDetails", fileReprocessCountSelected, "pluginId");
		
		waitingDialog.show();
		$.ajax({
			url: '<%=ControllerConstants.CREATE_ARCHIVE_RESTORE_BATCH%>',
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
	 				$("#restoreBatchId").empty();
	 				$("#restoreBatchId").html(object.id);
	 				$("#batchIdForViewStatus").val(parseInt(object.id));
	 				$("#restoreSuccessModal").click();
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
		   	
		   	var fileDetails = "";
		   	if(jqGridId == "collectionServiceGrid")
				fileDetails = $("#fileDetails_"+responseObject["ServiceId"]+"_"+responseObject["id"]+"_"+responseObject[fileDetailsPostfixIdName]).text();
		   	else 
		   		fileDetails = $("#fileDetails_"+responseObject["ServiceId"]+"_"+responseObject[fileDetailsPostfixIdName]).text();
			
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
	
	function uploadAndReprocessAllFile() {
		
		clearAllMessages();
		clearResponseMsgDiv();
		clearResponseMsg();
		clearErrorMsg();
		clearResponseMsgPopUp();
		
		
		var serviceType = $("#search_service_type").val();
		var selectedDetailId = $("#selectedDetaiFileForUploadAndReprocess").val();
		
		var file = $("#uploadAndReprocessFileObj").val();
		if (file == '') {
			showErrorMsgPopUp("Please upload file for further process.");
			return;
		}else{
	    	var reprocessFilesObjForFileUpload = {};
	    	if(serviceType == processingType) {
	    		createJsonObjectToUpload();
				uploadProcessingFile();	
    		} else if(serviceType == distributionType) {
	    		reprocessFilesObjForFileUpload = getObjectForUploadAndReprocessFile(serviceType, selectedDetailId);
	    	} else if(serviceType == parsingType) {
	    		reprocessFilesObjForFileUpload = getObjectForUploadAndReprocessFile(serviceType, selectedDetailId);
	    	}
	    	
	    	$("#upload_close_div").click();
	    	
	    	waitingDialog.show();
	    	
	    	var oMyForm = new FormData();
		    oMyForm.append("file", files[0]);
		    oMyForm.append("errorReprocess", JSON.stringify(reprocessFilesObjForFileUpload));
		    oMyForm.append("serviceType", $("#search_service_type").val());
		    
		   	$.ajax({dataType : 'json',
	        	url : 'uploadProcessingFile',
		        data : oMyForm ,
		        type : "POST",
		        enctype: 'multipart/form-data',
		        processData: false,
		        contentType:false,
				success: function(data){
					var responseMsg = data.msg;
					var responseObject = data.object; 
					var responseCode = data.code;
					
					waitingDialog.hide();
					
					if(responseCode == "200"){
						continueWithFileSearchOnCount(serviceType, selectedDetailId);
						responseObject = JSON.parse(responseObject);
						$("#reprocessBatchId").empty();
						$("#reprocessBatchId").html(responseObject.id);
						$("#batchIdForViewStatus").val(parseInt(responseObject.id));
						$("#reprocessSuccessModal").click();
					}else{
						$("#fileUploadAndReprocess").click();
						showErrorMsgPopUp(responseMsg);
					}
		    	},
		    	error: function (xhr,st,err){
					handleGenericError(xhr,st,err);
				}
			}); 
			
		}
	}
	
	function getObjectForUploadAndReprocessFile(serviceType, selectedDetailRowId) {
		var reprocessFilesObjForFileUpload = {};
		var newArray = [];
		var jqGridId = "";
		var mainId = "";
		
		if(serviceType == distributionType) {
			jqGridId = "distributionService_FileDetails";
			mainId = "composerId";
		} else if(serviceType == processingType) {
			
		} else if(serviceType == parsingType) {
			jqGridId = "fileCountDetails";
			mainId = "pluginId";
		}
		
		
		var responseObject = jQuery("#"+jqGridId).jqGrid ('getRowData', selectedDetailRowId);
    	var newObject = {};
    	
    	newObject.fileName = responseObject["fileName"];
    	
		newObject.fileSize = parseFloat(getFileSizeinBytes(responseObject["fileSize"]));
		
		newObject.absoluteFilePath = responseObject["absoluteFileName"]; 
		
		if(responseObject["sourceFileType"] == '<%=BaseConstants.FILE_GZ_COMPRESS_EXT%>'){
			newObject.isInputSourceCompress = 'true';	
		}else{
			newObject.isInputSourceCompress = 'false';
		}
		
		var fileType = responseObject["fileType"]; 
		
		if(fileType == '<%=BaseConstants.FILE_GZ_COMPRESS_EXT%>'){
			newObject.isCompress = 'true';	
		}else{
			newObject.isCompress = 'false';
		}
		
		
		
		newObject.failureReason = "";
		
		newObject.service = responseObject["serviceId"];
		newObject.service = {};
		newObject.service.id = responseObject["serviceId"];
		newObject.svctype = {};
		newObject.svctype.id = responseObject["serviceTypeId"];
		newObject.serverInstance = {};
		newObject.serverInstance.id = responseObject["serverInstanceId"];
	
		if(mainId == "pluginId") {
			var pluginIdForUpload = responseObject["pluginId"];
			if(pluginIdForUpload.indexOf("DEFAULT") !== -1) {
				newObject.parser = {};
				newObject.parser.id = 0;
				newObject.composer = {};
				newObject.composer.id = 0;
			} else {
				newObject.parser = {};
				newObject.parser.id = responseObject["pluginId"];
				newObject.composer = {};
				newObject.composer.id = 0;
			}
		} else if(mainId == "composerId") {
			var composerIdForUpload = responseObject["composerId"];
			if(composerIdForUpload.indexOf("DEFAULT") !== -1) {
				newObject.composer = {};
				newObject.composer.id = 0;
				newObject.parser = {};
				newObject.parser.id = 0;
			} else {
				newObject.composer = {};
				newObject.composer.id = responseObject["composerId"];
				newObject.parser = {};
				newObject.parser.id = 0;
			}
		}
		
		newObject.filePath = responseObject["errorPath"];
		newObject.readFilePath = responseObject["readPath"];
		newArray.push(newObject);
		
		reprocessFilesObjForFileUpload.reprocessDetailList = newArray;
		reprocessFilesObjForFileUpload.errorProcessAction = "UPLOAD_REPROCESS";
		reprocessFilesObjForFileUpload.userComment = "";
		reprocessFilesObjForFileUpload.errorCategory = $("#category").val();
		return reprocessFilesObjForFileUpload;
	}
	
	
	
	
</script>
