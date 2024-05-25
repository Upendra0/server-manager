<%@page import="com.elitecore.sm.util.MapCache"%>
<%@page import="com.elitecore.sm.common.constants.SystemParametersConstant"%>
<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<%@page import="com.elitecore.sm.util.EliteUtils"%>
<%@page import="com.elitecore.sm.common.constants.BaseConstants"%>
<%@taglib 	uri="http://www.springframework.org/tags" 			prefix="spring"%>
<%@ taglib 	uri="http://www.springframework.org/security/tags" 	prefix="sec"  %>
<%@taglib uri="/WEB-INF/ShowErrorTag.tld" prefix="elitecoreError" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<head>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/bootstrap-multiselect.js"></script>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap-multiselect.css" type="text/css"/>
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
#file-reprocessing-status-block a:hover, a:focus {
    color: #fe7b26;
    cursor: pointer;
}
#fileBatchStatusListPagingDiv_left {
	width: 40% !important;
}

/* .table-cell-label {
    display: table-cell;
    line-height: 14px;
    vertical-align: middle;
    width: 200px;
} */

</style>

</head>


<div class="tab-content no-padding clearfix" id="file-reprocessing-status-block">
	
	<form name="search-service-mgmt-form"  action="javascript:;" id="search-service-mgmt-form">
	<sec:authorize access="hasAuthority('VIEW_FILE_STATUS')">
		<div class="title2">
			
			<spring:message code="error.reprocess.status.file.search.header" ></spring:message>
		</div>
        <div class="fullwidth borbot">
        	 <div class="col-md-6 inline-form" style="padding-left: 0px !important;">
        	 
       	 		 <div class="form-group">
	         		<spring:message code="search.error.reprocess.batch.id.label"  var="label"></spring:message>
	         		<spring:message code="search.error.reprocess.batch.id.label.tooltip"  var="tooltip"></spring:message>
	         		
	         		<div class="table-cell-label">${label}</div>
	             	<div class="input-group ">
	             		<input type="number" id="search_batch_id"  name="search_batch_id" class="form-control table-cell input-sm" data-toggle="tooltip" data-placement="bottom" title="${tooltip }" value="<c:out value='${batchId}'></c:out>" >
	             		<span class="input-group-addon add-on last" > <i id="search_batch_id_error" class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="left" title=""></i></span> 
	             	</div>
	             </div>
	             
	             <div class="form-group">
	            	<spring:message code="file.reprocess.fromDate.label"  var="label"></spring:message>
	         		<spring:message code="file.reprocess.fromDate.label.tooltip"  var="tooltip"></spring:message>
					<div class="table-cell-label">${label}</div>
					<div class="input-group input-append date" id="fromDatePicker">
						<!-- <span class="input-group-addon add-on"> <i class="fa fa-calendar"></i></span> -->
						<input id="search_file_from" readonly class="form-control table-cell input-sm" title="" data-toggle="tooltip" style="background-color: #ffffff" data-placement="bottom" tabindex="4" title="${tooltip}"/>
						<span class="input-group-addon add-on last" > <i id="fromDatePicker_error" class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="left" title=""></i></span> 
					</div>
			 	 </div>
		 	 </div>
		 	 
		 	 <div class="col-md-6 inline-form" style="padding-left: 0px !important;">
         		 <div class="form-group">
         		 	<div class="table-cell-label">&nbsp;</div>
						<div class="input-group input-append date">
							<span class="input-group-addon add-on last">&nbsp;</span>
						</div>
         		 </div>
	             
	             <div class="form-group">
	            	<spring:message code="file.reprocess.toDate.label"  var="label"></spring:message>
	         		<spring:message code="file.reprocess.toDate.label.tooltip"  var="tooltip"></spring:message>
					<div class="table-cell-label">${label}</div>
					<div class="input-group input-append date" id="toDatePicker">
						<!-- <span class="input-group-addon add-on"> <i class="fa fa-calendar"></i></span> -->
						<input id="search_file_to" readonly class="form-control table-cell input-sm" title="" data-toggle="tooltip" style="background-color: #ffffff" data-placement="left" tabindex="6" title="${tooltip}"/>
						<span class="input-group-addon add-on last" > <i id="toDatePicker_error" class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="left" title=""></i></span>
					</div>
			 	</div>
		 	 </div>
		 	 
		 	 <div class="clearfix"></div>
		 	 
		 	 <div class="col-md-12 inline-form" style="padding-left: 0px !important;" id="advanceTabLink">   
		 	 	<div class="form-group">
		 	 		<a style="text-decoration: underline;" onclick="showAdvanceSearch();">Switch to Advance search</a>
		 	 	</div>
		 	 </div>
		 	 
		 	 <div id="advanceSearchForViewStatus" style="display: none;">
		         <div class="col-md-6 inline-form" style="padding-left: 0px !important;">    
		             <div class="form-group">
		             	<spring:message code="serviceManagement.search.service.type" var="label" ></spring:message>
		         		<spring:message code="serviceManagement.search.service.type.tooltip" var="tooltip" ></spring:message>
		         		<div class="table-cell-label">${label}</div>
		             	<div class="input-group ">
		             	<spring:message code="search.select.service.type.title.label" var="selectType" ></spring:message>
		             		<select  name = "search_service_type" class="form-control table-cell input-sm"  tabindex="3" id="search_service_type" data-toggle="tooltip" data-placement="bottom"  title="${tooltip }">
		             			<option  value="-1" selected="selected">${selectType}</option>
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
		         		<spring:message code="search.error.reprocess.batch.service.label" var="label" ></spring:message>
		         		<spring:message code="search.error.reprocess.batch.service.label.tooltip" var="tooltip" ></spring:message>
		         		<div class="table-cell-label">${label}</div>
		             	<div class="input-group ">
		             		<select  name = "search_service_instance" class="form-control table-cell input-sm"  tabindex="3" multiple="multiple" id="search_service_instance" data-toggle="tooltip" data-placement="bottom"  title="${tooltip }">
		             		</select>
		             		<span class="input-group-addon add-on last" > <i id="search_service_instance_error" class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="left" title=""></i></span>
		             	</div>
		             </div>
		             
		             <div class="form-group">
		         		<spring:message code="search.error.reprocess.status.label"  var="label"></spring:message>
		         		<spring:message code="search.error.reprocess.status.label.tooltip"  var="tooltip"></spring:message>
		         		
		         		<div class="table-cell-label">${label}</div>
		             	<div class="input-group ">
		             	<spring:message code="search.select.error.reprocess.status.title.label"  var="selectStatus"></spring:message>
		             		<select  name = "search_file_status" class="form-control table-cell input-sm"  tabindex="3" id="search_file_status" data-toggle="tooltip" data-placement="bottom"  title="${tooltip }">
		             			<option  value="-1" selected="selected">${selectStatus}</option>
				             		<c:forEach items="${fileProcessingStatusEnum}" var="fileProcessingStatusEnum">
				             			<option value="${fileProcessingStatusEnum}">${fileProcessingStatusEnum}</option>
				             		</c:forEach>
			             		
		             		</select>
		             		<span class="input-group-addon add-on last" > <i id="search_file_status_error" class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="left" title=""></i></span> 
		             	</div>
		             </div>
	     	  	</div> 
	     	  	
			 	<div class="col-md-6 inline-form" style="padding-left: 0px !important;">
		             <div class="form-group">
		             	<spring:message code="search.error.reprocess.file.name.contain.label" var="label" ></spring:message>
		             	<spring:message code="search.error.reprocess.file.name.contain.label.tooltip" var="tooltip" ></spring:message>
		         		<div class="table-cell-label">${label}</div>
		             	<div class="input-group ">
		             		<input type="text" id="search_file_contain"  name="search_file_contain" maxlength="100" class="form-control table-cell input-sm" data-toggle="tooltip" data-placement="bottom" title="${tooltip }"  >
		             		<span class="input-group-addon add-on last" > <i id="search_file_contain_error" class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="left" title=""></i></span>
		             	</div>
		             </div>
		             
	             	 <div class="form-group">
		             	<spring:message code="search.error.reprocess.action.label" var="label" ></spring:message>
		             	<spring:message code="search.error.reprocess.action.label.tooltip" var="tooltip" ></spring:message>
		         		<div class="table-cell-label">${label}</div>
		             	<div class="input-group ">
		             		<spring:message code="search.select.error.reprocess.action.title.label"  var="selectAction"></spring:message>
		             		<select  name = "searchServiceType" class="form-control table-cell input-sm"  tabindex="3" id="search_file_action" data-toggle="tooltip" data-placement="bottom"  title="${tooltip }">
		             			<option  value="-1" selected="selected">${selectAction}</option>
				             		<c:forEach items="${fileProcessingActionEnum}" var="fileProcessingActionEnum">
				             			<option value="${fileProcessingActionEnum}">${fileProcessingActionEnum}</option>
				             		</c:forEach>
			             		
		             		</select>
		             		<span class="input-group-addon add-on last" > <i id="search_file_action_error" class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="left" title=""></i></span> 
		             	</div>
		             </div>
		             
	         	</div>
	         	
	         	<div class="col-md-12 inline-form" style="padding-left: 0px !important;">   
			 	 	<div class="form-group">
			 	 		<a style="text-decoration: underline;" onclick="showBasicSearch();">Switch to Basic search</a>
			 	 	</div>
			 	</div>
	        </div>
         	
         	<div class="clearfix"></div>
     		<div class="pbottom15 form-group ">
     				<sec:authorize access="hasAuthority('SEARCH_BATCH_FILE')">
   						<button id="searchErrorStatusBtn" class="btn btn-grey btn-xs" onclick="reloadGridData();"><spring:message code="btn.label.search" ></spring:message></button>
    					<button id="resetErrorStatusBtn" class="btn btn-grey btn-xs" onclick="resetAllSearchParams();"><spring:message code="btn.label.reset" ></spring:message></button>
    				</sec:authorize>
   			</div>
   		</div>
   	</sec:authorize>
   	</form>
        <div class="tab-content no-padding clearfix">
         	<div class="fullwidth">
   				   <div class="title2">
   					 <spring:message code="error.reprocess.status.file.list.label" ></spring:message>
   					 
   					 <span id="reprocess_category_validation_msg" class="errorMessage" style="padding-left: 25px;display: none;color: red;" > <spring:message code="errore.status.reprocess.category.validation.msg" ></spring:message></span> 
		          </div>
		          	
   			</div>
           	<!-- Morris chart - Sales -->
           
           	<sec:authorize access="hasAnyAuthority('VIEW_FILE_STATUS')">
            <div class="box-body table-responsive no-padding box">
             	<table class="table table-hover" id="fileBatchStatusList"></table>
               	<div id="fileBatchStatusListPagingDiv"></div> 
	           	<div class="clearfix"></div> 
	           	<div id="divLoading" align="center" style="display: none;"><img src="img/preloaders/Preloader_10.gif" /></div>  
            </div>
            
            
            <div class="pbottom15 form-group ">
            		<sec:authorize access="hasAuthority('DIRECT_REPROCESS')">
   						<button id="reprocessBtn" class="btn btn-grey btn-xs" onclick="reprocessFiles();"><spring:message code="btn.label.reprocess"></spring:message></button>
   					</sec:authorize>
   					<sec:authorize access="hasAuthority('MODIFY_FILES')">
    					<button id="revertToOriginalBtn" class="btn btn-grey btn-xs" onclick="revertToOriginalFiles();"><spring:message code="btn.label.rever.to.original"></spring:message></button>
    				</sec:authorize>
    				
    				<a href="#divReprocessSuccessStatusModal" class="fancybox" style="display: none;" id="reprocessSuccessStatusModal">
   					</a>
   					<a href="#divReprocessStatusMessage" class="fancybox" style="display: none;" id="reprocessStatusMessage">
				   	</a>
				   	<a href="#divRevertToOriginalStatusMessage" class="fancybox" style="display: none;" id="revertToOriginalStatusMessage">
				   	</a>
   			</div>
   			
            </sec:authorize>
        </div>
</div>  


<!-- View Batch details pop- up code start here -->
<a href="#divViewBatchDetails" class="fancybox" style="display: none;" id="viewBatchDetails">#</a>
<div id="divViewBatchDetails" style="display: none;">
	<div class="modal-content">
		<div class="modal-header padding10">
			<h4 class="modal-title"><spring:message code="error.reprocess.status.grid.view.details.header" ></spring:message> </h4>
		</div>
		<div class="modal-body padding10 inline-form">
			<div class="fullwidth">
				<div class="title2"><spring:message code="error.reprocess.status.view.detail.header" ></spring:message></div>
			</div>
			<div class="col-md-6 no-padding">
				<spring:message code="error.reprocess.status.grid.batch.id.header"
					var="tooltip" ></spring:message>
				<div class="form-group">
					<div class="table-cell-label">${tooltip} <span class="padding10">:</span>
					</div>
					<div class="input-group " id="view_batch_id"></div>
				</div>
			</div>
			<div class="col-md-6 no-padding">
				<spring:message
					code="error.reprocess.status.grid.action.header" var="tooltip" ></spring:message>
				<div class="form-group">
					<div class="table-cell-label">${tooltip}<span class="padding10">:</span>
					</div>
					<div class="input-group " id="view_action">	</div>
				</div>
			</div>

			<div class="col-md-6 no-padding">
				<spring:message code="error.reprocess.status.grid.id.header" var="tooltip" ></spring:message>
				<div class="form-group">
					<div class="table-cell-label">${tooltip} <span class="padding10">:</span>	</div>
					<div class="input-group " id="view_reprocessing_id"> </div>
				</div>
			</div>
			<div class="col-md-6 no-padding">
				<spring:message code="error.reprocess.status.view.detail.action.details.label"
					var="tooltip" ></spring:message>
				<div class="form-group">
					<div class="table-cell-label">${tooltip} <span class="padding10">:</span>
					</div>
					<div class="input-group " id="view_action_details">	</div>
				</div>
			</div>
			<div class="clearfix"> </div>
			<div class="col-md-6 no-padding">
				<spring:message
					code="error.reprocess.status.view.detail.start.time.label"
					var="tooltip" ></spring:message>
				<div class="form-group">
					<div class="table-cell-label">${tooltip} <span class="padding10">:</span>
					</div>
					<div class="input-group " id="view_start_time">
					</div>
				</div>
			</div>
			<div class="col-md-6 no-padding">
				<spring:message code="error.reprocess.status.grid.status.header"	var="tooltip" ></spring:message>
				<div class="form-group">
					<div class="table-cell-label">${tooltip}
						<span class="padding10">:</span>
					</div>
					<div class="input-group " id="view_status">
					</div>
				</div>
			</div>
			<div class="col-md-6 no-padding">
				<spring:message
					code="error.reprocess.status.view.detail.end.time.label"
					var="tooltip" ></spring:message>
				<div class="form-group">
					<div class="table-cell-label">${tooltip}
						<span class="padding10">:</span>
					</div>
					<div class="input-group " id="view_end_time">
					</div>
				</div>
			</div>
			<div class="col-md-6 no-padding">
				<spring:message
					code="error.reprocess.status.view.detail.fail.reason.label"
					var="tooltip" ></spring:message>
				<div class="form-group">
					<div class="table-cell-label">${tooltip}
						<span class="padding10">:</span>
					</div>
					<div class="input-group " id="view_fail_reason">
					</div>
				</div>
			</div>
			<div  class="clearfix"></div>
			<hr size="2"/>
			<div class="fullwidth">
				<div class="title2"><spring:message
					code="error.reprocess.status.view.detail.file.details.header" ></spring:message></div>
			</div>
			
			<div class="col-md-6 no-padding">
				<spring:message
					code="error.reprocess.status.view.detail.server.instance.label"
					var="tooltip" ></spring:message>
				<div class="form-group">
					<div class="table-cell-label">${tooltip}
						<span class="padding10">:</span>
					</div>
					<div class="input-group " id="view_server_instance">
					</div>
				</div>
			</div>
			 <div class="col-md-6 no-padding">
				<spring:message
					code="error.reprocess.status.view.detail.category.label"
					var="tooltip" ></spring:message>
				<div class="form-group">
					<div class="table-cell-label">${tooltip}
						<span class="padding10">:</span>
					</div>
					<div class="input-group " id="view_category">
					</div>
				</div>
			</div>
			
			<div class="col-md-6 no-padding">
				<spring:message
					code="error.reprocess.status.view.detail.service.instance.label"
					var="tooltip" ></spring:message>
				<div class="form-group">
					<div class="table-cell-label">${tooltip}
						<span class="padding10">:</span>
					</div>
					<div class="input-group " id="view_service_instance">
					</div>
				</div>
			</div>
			
			<div class="col-md-6 no-padding">
				<spring:message
					code="error.reprocess.status.view.detail.file.path.label"
					var="tooltip" ></spring:message>
				<div class="form-group">
					<div class="table-cell-label">${tooltip}
						<span class="padding10">:</span>
					</div>
					<div class="input-group " id="view_file_path">
					</div>
				</div>
			</div>
			
			<div class="col-md-6 no-padding">
				<spring:message
					code="error.reprocess.status.view.detail.plugin.name.label"
					var="tooltip" ></spring:message>
				<div class="form-group">
					<div class="table-cell-label">${tooltip}
						<span class="padding10">:</span>
					</div>
					<div class="input-group " id="view_plugin_name">
					</div>
				</div>
			</div>
			
			
			<div class="col-md-6 no-padding">
				<spring:message
					code="error.reprocess.status.view.detail.file.name.label"
					var="tooltip" ></spring:message>
				<div class="form-group">
					<div class="table-cell-label">${tooltip}
						<span class="padding10">:</span>
					</div>
					<div class="input-group " id="view_file_name" >
					</div>
				</div>
			</div>
			
			
			<div class="col-md-6 no-padding">
				<spring:message
					code="error.reprocess.status.view.detail.plugin.type.label"
					var="tooltip" ></spring:message>
				<div class="form-group">
					<div class="table-cell-label">${tooltip}
						<span class="padding10">:</span>
					</div>
					<div class="input-group " id="view_plugin_type">
					</div>
				</div>
			</div>
			
			<div class="col-md-6 no-padding">
				<spring:message
					code="error.reprocess.status.view.detail.file.size.label"
					var="tooltip" ></spring:message>
				<div class="form-group">
					<div class="table-cell-label">${tooltip}
						<span class="padding10">:</span>
					</div>
					<div class="input-group" id="view_file_size">
					</div>
				</div>
			</div>
			
			
			
		</div>
		
		<div id="import-config-buttons" class="modal-footer padding10">
			<button type="button" id="view-batch-details-close-btn"
				class="btn btn-grey btn-xs " data-dismiss="modal"
				onclick="closeFancyBox();"> <spring:message	code="btn.label.ok"></spring:message></button>
		</div>
	</div>
</div>

<!-- Reprocess success modal content start  -->
<div id="divReprocessSuccessStatusModal" style="width: 100%; display: none;">
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
				<spring:message code="file.reprocess.batchId.message" ></spring:message> : <span id="statusReprocessBatchId"></span>
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


<!-- No file selected for reprocess popup : start -->
<div id="divReprocessStatusMessage" style=" width:100%; display: none;" >
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


<!-- No file selected for revert to original popup : start -->
<div id="divRevertToOriginalStatusMessage" style=" width:100%; display: none;" >
    <div class="modal-content">
        <div class="modal-header padding10">
            <h4 class="modal-title"><spring:message code="device.delete.popup.warning.heading.label"></spring:message></h4>
        </div>
        <div class="modal-body padding10 inline-form">
            <p> No file has been selected. Please select at least one file for revert to original.</p>
        </div>
        <div class="modal-footer padding10">
            <button type="button" class="btn btn-grey btn-xs " data-dismiss="modal" onclick="closeFancyBox();"><spring:message code="btn.label.close" ></spring:message></button>
        </div>
    </div>
</div>
<!-- No file selected for revert to original popup : end -->

<!-- View Batch details pop- up code end here -->

<script type="text/javascript">
var maxDurationForFileSearch = '<%=MapCache.getConfigValueAsObject(SystemParametersConstant.FILE_REPROCESS_MAX_DURATION_FOR_FILE_SEARCH)%>';
var maxCsvFileSize = '<%=MapCache.getConfigValueAsObject(SystemParametersConstant.FILE_REPROCESS_MAX_CSV_FILE_SIZE)%>';
var maxCompressFileSize = '<%=MapCache.getConfigValueAsObject(SystemParametersConstant.FILE_REPROCESS_MAX_COMPRESS_FILE_SIZE)%>';
var fileRecordsBatchSize = '<%=MapCache.getConfigValueAsObject(SystemParametersConstant.FILE_REPROCESS_FILE_RECORDS_BATCH_SIZE)%>';


	

$(document).ready(function() {
	
	var lastTenDay = new Date();
	lastTenDay.setDate(lastTenDay.getDate() - maxDurationForFileSearch);
	
	$("#search_file_from").datepicker('setDate', lastTenDay);

	$("#search_file_to").datepicker('setDate', new Date());
	
	 $('#search_service_instance').multiselect({
    	maxHeight: '200',
        buttonWidth: 'auto',
        nonSelectedText: 'Select',
        nSelectedText: '-1',
        numberDisplayed: 4,
        enableFiltering: true
    }); 
	
	$("#search_service_type").val("-1");
	
	getBatchDetailsBySearchParams();
});
	
	
	
$("#search_service_type").on('change', function() {
	var serviceType = $("#search_service_type").val();
	if(serviceType == -1){
		resetMultiselectDropdown("search_service_instance");
	}
	getServiceListByType("search_service_instance");
});


function resetAllSearchParams(){

	$("#search_service_type").val("-1");
	resetMultiselectDropdown("search_service_instance");
	$("#search_batch_id").val("");
	$("#search_file_contain").val("");
	$("#search_file_status").val("-1");
	$("#search_file_action").val("-1");

	$("#search_file_from").datepicker('setDate', lastTenDay)

	$("#search_file_to").datepicker('setDate', new Date())
	
	reloadGridData();
}

function validateDates(){
	
	var valid = true;
	var fromDate = $("#search_file_from").val();
	var toDate = $("#search_file_to").val()
	
	if(fromDate == null || fromDate == '' || toDate == null || toDate == ''){
		showErrorMsg(jsSpringMsg.dateValidationMsg);
		valid = false;
	}
	
	return valid;
}

function showAdvanceSearch() {
	$("#advanceTabLink").hide();
	$("#advanceSearchForViewStatus").show();
}

function showBasicSearch() {
	resetAdvanceSearch();
	$("#advanceTabLink").show();
	$("#advanceSearchForViewStatus").hide();
}

function resetAdvanceSearch() {
	$("#search_file_status").val(-1);
	$("#search_file_action").val(-1);
	$("#search_service_type").val(-1);
	$("#search_file_contain").val('');
	resetMultiselectDropdown("search_service_instance");
}

function reprocessFiles() {
	if(viewStatusFileSelected.length == 0){
		$("#reprocessStatusMessage").click();
		return;
	}
	var batchObject = getBatchJsonFromStatus("DIRECT_REPROCESS", "fileBatchStatusList");
	waitingDialog.show();
	
	var oMyForm = new FormData();
    oMyForm.append("fileListIds", selectedReprocessIds);
    oMyForm.append("errorReprocess", JSON.stringify(batchObject));
	
	 $.ajax({
		 url: '<%=ControllerConstants.REPROCESS_MODIFIED_FILES%>',
		 dataType : 'json',
		 data : oMyForm ,
	     type : "POST",
	     enctype: 'multipart/form-data',
	     processData: false,
	     contentType:false,
	     async: true, 
		success: function(data){
			var response = eval(data);
			var responseCode = response.code;
			var responseMsg = response.msg;
			var responseObject = response.object;
			
			waitingDialog.hide();
			
			if (responseCode === "200") {
				var object = JSON.parse(data.object);
 				reprocessFilesObj = {};
				reloadGridData();
				selectedReprocessIds = [];
 				$("#statusReprocessBatchId").empty();
 				$("#statusReprocessBatchId").html(object.id);
   				$("#batchIdForViewStatus").val(parseInt(object.id));
 				$("#reprocessSuccessStatusModal").click();
 				
			}else{
				showErrorMsg(responseMsg);
			}
		},
	    error: function (xhr,st,err){
			handleGenericError(xhr,st,err);
		}
	}); 
	
}

function revertToOriginalFiles() {
	
	if(viewStatusFileSelected.length == 0){
		$("#revertToOriginalStatusMessage").click();
		return;
	}
	clearAllMessages();
	clearResponseMsgDiv();
	clearResponseMsg();
	clearErrorMsg();
	var myCustomObjectForMultipleAjaxForViewStatus = {};
	var batchArrayForDetail = [];
	waitingDialog.show();
	
	$.each(viewStatusFileSelected, function(i, item) {
		var batchAndDetail = {};
		var arr = item.split('-');
		batchAndDetail.batchId = parseInt(arr[0]);
		batchAndDetail.detailId = parseInt(arr[1]);
		batchArrayForDetail.push(batchAndDetail);
	});
	
	$.each(batchArrayForDetail, function(i, item) {
		if(myCustomObjectForMultipleAjaxForViewStatus.hasOwnProperty(item["batchId"])) {
			var existingArray = myCustomObjectForMultipleAjaxForViewStatus[item["batchId"]];
			existingArray.push(item["detailId"]);
			myCustomObjectForMultipleAjaxForViewStatus[item["batchId"]] = existingArray;
		} else {
			var newArray = [];
			newArray.push(item["detailId"]);
			myCustomObjectForMultipleAjaxForViewStatus[item["batchId"]] = newArray;
		}
	});
	
	$.each(myCustomObjectForMultipleAjaxForViewStatus, function(key,value){
	    $.ajax({
	    	url: '<%=ControllerConstants.REVERT_MULTIPLE_MODIFIED_FILES%>',
			cache: false,
			async: true, 
			dataType: 'json',
				type: "POST",
				data: {
		    			'detailIdList': value.join() 
		    		},
			success: function(data){
				
				waitingDialog.hide();
 				if(data.code == "200") {
 					reloadGridData();
 					var responseMsg = "<spring:message code='error.status.revert.success.msg'></spring:message>" ;
 					reloadGridData();
 					showSuccessMsg(responseMsg);
 				} else {
 					showErrorMsg(responseMsg);				
 				}
			},
		    error: function (xhr,st,err){
				handleGenericError(xhr,st,err);
			}
		});
	});
	
}

function getBatchJsonFromStatus(errorProcessAction, jqGridId) {
	var selectedStatusIds = [];
	$.each(viewStatusFileSelected, function(i, item) {
		var arr = item.split('-');
		selectedStatusIds.push(parseInt(arr[1]));
	});
	
	reprocessFilesObj = {};
	var newArray = [];
	var selectedCategory = "";
	
	$.each(selectedStatusIds,function(i){
	   	var responseObject = jQuery("#"+jqGridId).jqGrid ('getRowData', selectedStatusIds[i]);
		
	   	console.log(JSON.stringify(responseObject));
	   	
    	var newObject = {};
		newObject.fileName = responseObject.fileName;
		newObject.fileSize = responseObject.fileSize;
		
		console.log("Absolute path is :: " + responseObject.absoluteFilePath);
		
		if(responseObject.absoluteFilePath) {
			newObject.absoluteFilePath = responseObject.absoluteFilePath;	
		} else {
			newObject.absoluteFilePath = "";
		}
		
		newObject.isCompress = responseObject.isCompress;	
		
		newObject.failureReason = "";
		
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
		
		isObjExists = isObjectAlreadyExists(newArray, "parser", responseObject["pluginId"]);
		if(isObjExists) {
			newObject.parser = responseObject["pluginId"];
		} else {
			newObject.parser = {};
			newObject.parser.id = responseObject["pluginId"];
		}
		
		isObjExists = isObjectAlreadyExists(newArray, "composer", responseObject["composerId"]);
		if(isObjExists) {
			newObject.composer = responseObject["composerId"];
		} else {
			newObject.composer = {};
			newObject.composer.id = responseObject["composerId"];
		}
		
		newObject.filePath = responseObject["filePath"];
		newObject.readFilePath = responseObject["readFilePath"];
		
		selectedCategory = responseObject["errorCategory"];
		
		newArray.push(newObject);
		
	});
	
	reprocessFilesObj["reprocessDetailList"] = newArray;
	reprocessFilesObj.errorProcessAction = errorProcessAction;
	reprocessFilesObj.userComment = "";
	reprocessFilesObj.errorCategory = selectedCategory;
	return reprocessFilesObj;
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

function viewFileStatus() {
	closeFancyBox();
	loadReprocessingStatus();
	showButtons('REPROCESSING_STATUS');
}
</script>
<script src="${pageContext.request.contextPath}/customJS/errorReprocessing.js<%= "?v=" + Math.random() %>" type="text/javascript"></script>
 
