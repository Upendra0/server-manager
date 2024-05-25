<%@page import="com.elitecore.core.util.Constant"%>
<%@page import="com.elitecore.sm.common.constants.BaseConstants"%>
<%@page import="com.elitecore.sm.common.constants.EngineConstants"%>
<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<%@page import="com.elitecore.sm.common.constants.SystemParametersConstant"%>
<%@page import="com.elitecore.sm.util.MapCache"%>
<%@page import="com.elitecore.sm.common.constants.BusinessModelConstants"%>
<%@taglib 	uri="http://www.springframework.org/tags" 			prefix="spring"%>
<%@ taglib 	uri="http://www.springframework.org/security/tags" 	prefix="sec"  %>
<%@ taglib 	uri="http://java.sun.com/jsp/jstl/core" 			prefix="c" %>
<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<%@page import="com.elitecore.sm.common.constants.BaseConstants"%>
<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<jsp:include page="../common/newheader.jsp"></jsp:include>

<script type="text/javascript">

var jsSpringMsg = {};

jsSpringMsg.id = "<spring:message code='error.reprocess.status.grid.id.header'></spring:message>";
jsSpringMsg.batchId = "<spring:message code='error.reprocess.status.grid.batch.id.header'></spring:message>";
jsSpringMsg.serviceType = "<spring:message code='error.reprocess.status.grid.service.type.header'></spring:message>";
jsSpringMsg.fileName = "<spring:message code='error.reprocess.status.grid.filen.name.header'></spring:message>";
jsSpringMsg.action = "<spring:message code='error.reprocess.status.grid.action.header'></spring:message>";
jsSpringMsg.status = "<spring:message code='error.reprocess.status.grid.status.header'></spring:message>";
jsSpringMsg.viewDetails="<spring:message code='error.reprocess.status.grid.view.details.header'></spring:message>";

jsSpringMsg.emptyRecord = "<spring:message code='device.list.grid.empty.records'></spring:message>";
jsSpringMsg.recordText = "<spring:message code='device.list.grid.pager.total.records.text'></spring:message>";
jsSpringMsg.pagerText = "<spring:message code='device.list.grid.pager.text'></spring:message>";
jsSpringMsg.loadingText = "<spring:message code='device.list.grid.loading.text'></spring:message>";

jsSpringMsg.gridCaption = "<spring:message code='device.tab.heading.label'></spring:message>";

jsSpringMsg.dateValidationMsg = "<spring:message code='error.reprocess.status.validation.msg'></spring:message>";
jsSpringMsg.category = "<spring:message code='error.reprocess.status.view.detail.category.header.label'></spring:message>";

</script>


<script type="text/javascript">
var isAccessDirectReprocess = false;
var isAccessUploadFile = false;
var isAccessDownloadFile = false;
var isAccessSearchFile = false;
var isAccessViewBatchDetails = false;
var isAccessDeleteFile = false;
var isAccessModifyFile = false;
var isAccessPreviewFile = false;
var isViewFile = false;


var maxDurationForFileSearch = '<%=MapCache.getConfigValueAsObject(SystemParametersConstant.FILE_REPROCESS_MAX_DURATION_FOR_FILE_SEARCH)%>';
var maxCsvFileSize = '<%=MapCache.getConfigValueAsObject(SystemParametersConstant.FILE_REPROCESS_MAX_CSV_FILE_SIZE)%>';
var maxCompressFileSize = '<%=MapCache.getConfigValueAsObject(SystemParametersConstant.FILE_REPROCESS_MAX_COMPRESS_FILE_SIZE)%>';
var fileRecordsBatchSize = '<%=MapCache.getConfigValueAsObject(SystemParametersConstant.FILE_REPROCESS_FILE_RECORDS_BATCH_SIZE)%>';

var lastTenDay = new Date();
lastTenDay.setDate(lastTenDay.getDate() -maxDurationForFileSearch);

<sec:authorize access="hasAuthority('VIEW_FILE')">
	isViewFile = true;
</sec:authorize>

<sec:authorize access="hasAuthority('DIRECT_REPROCESS')">
	isAccessDirectReprocess = true;
</sec:authorize>

<sec:authorize access="hasAuthority('UPLOAD_REPROCESS_FILE')">
	isAccessUploadFile = true;
	</sec:authorize>

<sec:authorize access="hasAuthority('DELETE_FILE')">
	isAccessDeleteFile = true;
	</sec:authorize>

<sec:authorize access="hasAuthority('DOWNLOAD_FILE')">
	isAccessDownloadFile = true;
	</sec:authorize>

<sec:authorize access="hasAuthority('SEARCH_FILE')">
	isAccessSearchFile = true;
</sec:authorize>

<sec:authorize access="hasAuthority('VIEW_DETAILS')">
	isAccessViewBatchDetails =true;
	</sec:authorize>

<sec:authorize access="hasAuthority('PREVIEW_FILE')">
	isAccessPreviewFile = true;
</sec:authorize>

<sec:authorize access="hasAuthority('MODIFY_FILES')">
	isAccessModifyFile = true;
</sec:authorize>

</script>

<body class="skin-blue sidebar-mini sidebar-collapse">

	<div>
		<jsp:include page="../common/newtopNavigationPanel.jsp"></jsp:include>
		<jsp:include page="../common/newleftMenu.jsp"></jsp:include>
		<div class="content-wrapper">
			<section class="content-header">
				<div class="fullwidth">
		       		<div style="padding:0 10px 0 4px;">
		       			<div id="content-scroll-d" class="content-scroll">
		        			<div class="fullwidth">
								<h4 style="margin-top:10px; margin-bottom:0px; font-size:11px;">
									<a href="home.html"><img src="img/tile-icon.png" class="vmiddle" alt="Home"/></a> 
	                            	<span class="spanBreadCrumb" style="line-height: 30px;">
	                            		<strong>&nbsp;
	                            			<spring:message code="leftmenu.label.error.reprocessing" ></spring:message>
	                            		</strong>
	                            	</span>
								</h4>
								<jsp:include page="../common/responseMsg.jsp" ></jsp:include>
	         					<div class="row">
		           					<div class="col-xs-12">
		             					<div class="box  martop" style="border:none; box-shadow: none;">
		               						<div class="box-body table-responsive no-padding">
		               							<div class="nav-tabs-custom tab-content">
		               								<sec:authorize access="hasAnyAuthority('ERROR_REPROCESS_MENU_VIEW')">
			               								
			               								<ul class="nav nav-tabs pull-right">
											                <sec:authorize access="hasAnyAuthority('REPROCESS_FILE')">
				                 								<li class="<c:if test="${(REQUEST_ACTION_TYPE eq 'REPROCESSING_FILE')}"><c:out value="active"></c:out></c:if>" onclick="loadReprocessingFile();showButtons('REPROCESSING_FILE');" >
				                 									<a href="#service-mgmt" id="service_mgmt" data-toggle="tab" aria-expanded="false">
				                 										<spring:message code="error.reprocess.file.tab.header" ></spring:message>
				                 									</a>
				                 								</li>
				                 							</sec:authorize>
				                 							<sec:authorize access="hasAnyAuthority('VIEW_FILE_STATUS')">
				                 								<li class="<c:if test="${(REQUEST_ACTION_TYPE eq 'REPROCESSING_STATUS')}"><c:out value="active"></c:out></c:if>" onclick="loadReprocessingStatus();showButtons('REPROCESSING_STATUS');">
				                 									<a href="#create-service" id="create_service" data-toggle="tab" aria-expanded="true">
				                 										<spring:message code="error.reprocess.status.tab.header" ></spring:message>
				                 									</a>
				                 								</li>
				                 							</sec:authorize>
				                 							
				                 							<sec:authorize access="hasAnyAuthority('AUTO_REPROCESS_FILE')"> 
					                 							<li class="<c:if test="${(REQUEST_ACTION_TYPE eq 'AUTOREPROCESSING_FILE')}"><c:out value="active"></c:out></c:if>" onclick="loadAutoReprocessingFile();">
					                 								<a href="#auto-service-mgmt" id="auto_service-mgmt" data-toggle="tab" aria-expanded="true">
					                 									<spring:message code="error.reprocess.auto.reprocess.tab.header" ></spring:message>
					                 								</a>
					                 							</li>
				                 							</sec:authorize>
				                 							
			               								</ul>
			               								
			               								<sec:authorize access="hasAnyAuthority('REPROCESS_FILE')">
				           									<div id="service-mgmt" class="fullwidth mtop10 tab-pane <c:if test="${(REQUEST_ACTION_TYPE eq 'REPROCESSING_FILE')}"><c:out value="active"></c:out></c:if> ">
				           									<c:if test="${(REQUEST_ACTION_TYPE eq 'REPROCESSING_FILE')}">
				           										<jsp:include page="reprocessingFile.jsp"></jsp:include>
				           										</c:if>
				           									</div>
				           								</sec:authorize>
				           								<sec:authorize access="hasAnyAuthority('VIEW_FILE_STATUS')">
				         									<div id="create-service" class="fullwidth mtop10 tab-pane <c:if test="${(REQUEST_ACTION_TYPE eq 'REPROCESSING_STATUS')}"><c:out value="active"></c:out></c:if>">
				         									<c:if test="${(REQUEST_ACTION_TYPE eq 'REPROCESSING_STATUS')}">
				         										<jsp:include page="reprocessingStatus.jsp"></jsp:include>
				         										</c:if>
				         									</div>
				         								</sec:authorize>
				         								<sec:authorize access="hasAnyAuthority('AUTO_REPROCESS_FILE')"> 
				         									<div id="auto-service-mgmt" class="fullwidth mtop10 tab-pane <c:if test="${(REQUEST_ACTION_TYPE eq 'AUTOREPROCESSING_FILE')}"><c:out value="active"></c:out></c:if>">
				         									<c:if test="${(REQUEST_ACTION_TYPE eq 'AUTOREPROCESSING_FILE')}">
				         										<jsp:include page="autoReprocessingFile.jsp"></jsp:include>
				         										</c:if>
				         									</div>
				         								</sec:authorize>
			               							</sec:authorize>
	             								</div>
											</div>
		               					</div>
		             				</div>
		           				</div>
		           				
		         				</div>
		         			</div>
		         		</div>
		      		</div>
		    	</section>
		   	</div>
		 
		 <!-- Hidden form content start here -->
		 <div style="display: hidden">
			<form id="loadReprocessingStatus" action ="<%= ControllerConstants.INIT_REPROCESSING_STATUS %>" method="POST">
				<input type="hidden" name="<%= BaseConstants.REQUEST_ACTION_TYPE %>"  id="<%= BaseConstants.REQUEST_ACTION_TYPE %>" value="<%= BaseConstants.REPROCESSING_STATUS%>" />
				<input type="hidden" name="<%= BaseConstants.BATCH_ID %>"  id="batchIdForViewStatus"/>
			</form>
		</div>  	
		<div style="display: hidden">
			<form id="loadReprocessingFile" action ="<%= ControllerConstants.INIT_REPROCESSING_DETAILS %>" method="GET">
				<input type="hidden" name="<%= BaseConstants.REQUEST_ACTION_TYPE %>"  id="<%= BaseConstants.REQUEST_ACTION_TYPE %>" value="<%= BaseConstants.REPROCESSING_FILE%>" />
			</form>
		</div>     	
		
		<div style="display: hidden">
			<form id="loadAutoReprocessingFile" action ="<%= ControllerConstants.INIT_AUTOREPROCESSING_DETAILS %>" method="GET">
				<input type="hidden" name="<%= BaseConstants.REQUEST_ACTION_TYPE %>"  id="<%= BaseConstants.REQUEST_ACTION_TYPE %>" value="<%= BaseConstants.AUTOREPROCESSING_FILE%>" />
			</form>
		</div>  
		
			<footer class="main-footer positfix">
		    	<div class="fooinn">
		    		<div class="fullwidth">
		        		<div class="button-set">
			            	<div class="padleft-right" id="reprocessFile" style="display: none;">
			            		<sec:authorize access="hasAuthority('SYNCHRONIZE_SERVICE_INSTANCE')">
			            		<button id="reprocessBtn" class="btn btn-grey btn-xs " tabindex="21"  >
									<spring:message code="btn.label.reprocess" ></spring:message>
								</button>
								</sec:authorize>
								<sec:authorize access="hasAuthority('EXPORT_SERVICE_INSTANCE_CONFIG')">
								<button id="reprocessDeleteBtn" class="btn btn-grey btn-xs " tabindex="23">
									<spring:message code="btn.label.delete" ></spring:message>
								</button>
								</sec:authorize>
							</div>	
		            		<div class="padleft-right" id="reprocessStatus" style="display: none;">
			            		
		            		</div>
						</div>
		        	</div>
		        	<script type="text/javascript">
			       		
			       		function showHideButtonBasedOnTabsSelected(tabType){
			       			$("#serviceListDiv").hide();
			   				$("#createServiceDiv").hide();
			       		}
			       		
			       		function showButtons(id){
			       			/* if(id == 'service_mgmt'){
			       				showHideButtonBasedOnTabsSelected('REPROCESSING_FILE');
			       				//searchInstanceCriteria();
			       			}else if(id == 'create_service'){
			       				showHideButtonBasedOnTabsSelected('REPROCESSING_STATUS');
			       			} */
			       		}
			       		function loadReprocessingStatus(){
			       			$('#loadReprocessingStatus').submit();	
			       		}
			       		
			       		function loadReprocessingFile(){
			       			$('#loadReprocessingFile').submit();	
			       		}
			       		
			       		function loadAutoReprocessingFile(){
			       			$('#loadAutoReprocessingFile').submit();	
			       		}
			       		 
			       		function getFileSizeInKb(fileSize){
			       			fileSize = fileSize / 1024;
			       			return parseFloat(fileSize.toFixed(3));
			       		}
						
			       		/* function replaceBackwarSlash(path){
			       			console.log("before update " + path);
			       			var updatedPath = path.replace(/\\/g, "\\\\");
			       			console.log("After update " + updatedPath);
			       			return updatedPath
			       		} */
			       		
			       	</script>
			       	
			       	<script src="${pageContext.request.contextPath}/customJS/ajaxLoader.js<%= "?v=" + Math.random() %>" type="text/javascript"></script>
			       	
		        	<jsp:include page="../common/newfooter.jsp"></jsp:include>
		    	</div>
			</footer>
		</div><!-- ./wrapper -->
	</body>
	
</html>
