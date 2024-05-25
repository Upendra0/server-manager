<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<%@page import="com.elitecore.sm.common.constants.BaseConstants"%>

<!DOCTYPE html>
<html>
<head>
	<script type="text/javascript">
	var jsLocaleMsg = {};
		jsLocaleMsg.insName = "<spring:message code='server.instance.sync.header.label.instance.name'></spring:message>";
		jsLocaleMsg.serverIP = "<spring:message code='server.instance.sync.header.label.server.ip'></spring:message>";
		jsLocaleMsg.serverPort = "<spring:message code='server.instance.sync.header.label.server.port'></spring:message>";
		jsLocaleMsg.serverName = "<spring:message code='server.delete.header.label'></spring:message>";
		jsLocaleMsg.moduleName="<spring:message code='server.instance.import.header.label.module.name'></spring:message>";
		jsLocaleMsg.entityName = "<spring:message code='server.instance.import.header.label.entity.name'></spring:message>";
		jsLocaleMsg.propertyName = "<spring:message code='server.instance.import.header.label.property.name'></spring:message>";
		jsLocaleMsg.propertyValue = "<spring:message code='server.instance.import.header.label.property.value'></spring:message>";
		jsLocaleMsg.errorMessage = "<spring:message code='server.instance.import.header.label.error.message'></spring:message>";
		jsLocaleMsg.chkStatus = "<spring:message code='inactive.instance.chk.status.icon.lbl'></spring:message>";
		jsLocaleMsg.loadStatus = "<spring:message code='server.instance.load.status.icon.lbl'></spring:message>";
		jsLocaleMsg.importFileMiss = "<spring:message code='server.instance.import.file.missing'></spring:message>";
		jsLocaleMsg.LineNo = "<spring:message code='server.instance.import.header.label.line.number'></spring:message>";
		jsLocaleMsg.fileName="<spring:message code='server.instance.import.header.label.file.name'></spring:message>";
		
	var	tableId = '';
	var	ruleRecordColumns = [];
	var	ruleRecordColModels = [];
	var ckSelectedLookupRecords = [];
	var ckSelectedLookupTables = [];
	</script>

</head>
<jsp:include page="../common/newheader.jsp"></jsp:include>
<body class="skin-blue sidebar-mini sidebar-collapse">

<!-- Main div start Here -->
<div>
	<jsp:include page="../common/newtopNavigationPanel.jsp"></jsp:include>
	<jsp:include page="../common/newleftMenu.jsp"></jsp:include>
	
	<!-- Content div Start here -->
	<div class="content-wrapper">
			<section class="content-header">
				<div class="fullwidth">
					<div style="padding: 0 10px 0 4px;">
						<div id="content-scroll-d" class="content-scroll">
							<div class="fullwidth">
								<h4 style="margin-top:10px; margin-bottom:0px; font-size:11px;">
									<a href="home.html"><img src="img/tile-icon.png"class="vmiddle" alt="Home" /></a> 
									<span class="spanBreadCrumb" style="line-height: 30px;"> 
										<strong> <spring:message code="rule.data.config.tab.header" ></spring:message></strong>
									</span>
								</h4>
								<jsp:include page="../common/responseMsg.jsp" ></jsp:include>
								<!-- Tab code start here  -->
								<div class="row">
									<div class="col-xs-12">
										<div class="box martop" style="border: none; box-shadow: none;">
											<div class="box-body table-responsive no-padding">
												<div class="nav-tabs-custom tab-content">
			
													 <ul class="nav nav-tabs pull-right">
															<sec:authorize access="hasAnyAuthority('VIEW_RULE_DATA_CONFIG')">
																<li class="<c:if test="${(REQUEST_ACTION_TYPE eq 'UPDATE_RULE_DATA_CONFIG') or (REQUEST_ACTION_TYPE eq 'VIEW_RULE_DATA_RECORDS')}">
																	<c:out value="active"></c:out></c:if>" onclick="loadRuleDataDictionary();">
																	<a href="#collection-service-summary" id="collection_service_summary" data-toggle="tab" aria-expanded="false"> 
																	<spring:message	code="leftMenu.data.dictionary.menu" ></spring:message>
																</a></li>
															</sec:authorize>
															
															<sec:authorize access="hasAnyAuthority('VIEW_AUTO_UPLOAD_CONFIGURATION')">
																<li class="<c:if test="${(REQUEST_ACTION_TYPE eq 'AUTO_UPLOAD_CONFIG')}" ><c:out value="active"></c:out></c:if>" 
																onclick="loadAutoUploadConfig();">
																	<a href="#" id="load_autoupload_config" data-toggle="tab" aria-expanded="false">
																		<spring:message	code="auto.upload.config.header" ></spring:message>
																	</a>
																</li>
															</sec:authorize>
															
															<sec:authorize access="hasAnyAuthority('VIEW_AUTO_RELOAD_CACHE')">
																<li class="<c:if test="${(REQUEST_ACTION_TYPE eq 'AUTO_RELOAD_CACHE_CONFIG')}" ><c:out value="active"></c:out></c:if>" onclick="loadAutoReloadCacheConfig();">
																	<a href="#" id="collection_service_summary" data-toggle="tab" aria-expanded="false">
																		<spring:message	code="auto.reload.cache.config.header" ></spring:message>
																	</a>
																</li>
															</sec:authorize>
															
															<sec:authorize access="hasAnyAuthority('VIEW_AUTO_JOB_PROCESS')">
																<li class="<c:if test="${(REQUEST_ACTION_TYPE eq 'AUTO_UPLOAD_RELOAD_STATUS')}" ><c:out value="active"></c:out></c:if>" onclick="loadViewStatus();">
																	<a href="#" id="collection_service_summary" data-toggle="tab" aria-expanded="false">
																		View Status
																	</a>
																</li>
															</sec:authorize>
																														
														</ul>
														
														<div class="fullwidth no-padding tab-content">
														<sec:authorize access="hasAnyAuthority('VIEW_RULE_DATA_CONFIG')">
															<div id="device_mgmt_div"
																class="tab-pane <c:if test="${(REQUEST_ACTION_TYPE eq 'UPDATE_RULE_DATA_CONFIG')}"><c:out value="active"></c:out></c:if> ">
														 		<c:if test="${(REQUEST_ACTION_TYPE eq 'UPDATE_RULE_DATA_CONFIG')}">
														 			<jsp:include page="ruleDataDictionaryMgmt.jsp"></jsp:include> 
														 		</c:if>	 
															</div>
														</sec:authorize>
														</div>
														
														<div class="fullwidth no-padding tab-content">
														<sec:authorize access="hasAnyAuthority('VIEW_RULE_DATA_CONFIG')">
															<div id="device_mgmt_div"
																class="tab-pane <c:if test="${(REQUEST_ACTION_TYPE eq 'VIEW_RULE_DATA_RECORDS')}"><c:out value="active"></c:out></c:if> ">
														 		<c:if test="${(REQUEST_ACTION_TYPE eq 'VIEW_RULE_DATA_RECORDS')}">
														 			<jsp:include page="viewRuleDataDictionaryRecords.jsp"></jsp:include> 
														 		</c:if>	 
															</div>
														</sec:authorize>
														</div>
														
														<div class="fullwidth no-padding tab-content">
														<sec:authorize access="hasAnyAuthority('VIEW_AUTO_UPLOAD_CONFIGURATION')">
															<div id="device_mgmt_div"
																class="tab-pane <c:if test="${(REQUEST_ACTION_TYPE eq 'AUTO_UPLOAD_CONFIG')}"><c:out value="active"></c:out></c:if> ">
														 		<c:if test="${(REQUEST_ACTION_TYPE eq 'AUTO_UPLOAD_CONFIG')}">
														 			<jsp:include page="autoUploadConfigMgmt.jsp"></jsp:include> 
														 		</c:if>	 
															</div>
														</sec:authorize>
														</div>
														
														<div class="fullwidth no-padding tab-content">
														<sec:authorize access="hasAnyAuthority('VIEW_AUTO_RELOAD_CACHE')">
															<div id="device_mgmt_div"
																class="tab-pane <c:if test="${(REQUEST_ACTION_TYPE eq 'AUTO_RELOAD_CACHE_CONFIG')}"><c:out value="active"></c:out></c:if> ">
														 		<c:if test="${(REQUEST_ACTION_TYPE eq 'AUTO_RELOAD_CACHE_CONFIG')}">
														 			<jsp:include page="autoReloadCacheConfigMgmt.jsp"></jsp:include> 
														 		</c:if>	 
															</div>
														</sec:authorize>
														</div>
														<div class="fullwidth no-padding tab-content">
														<sec:authorize access="hasAnyAuthority('VIEW_AUTO_JOB_PROCESS')">
															<div id="device_mgmt_div"
																class="tab-pane <c:if test="${(REQUEST_ACTION_TYPE eq 'AUTO_UPLOAD_RELOAD_STATUS')}"><c:out value="active"></c:out></c:if> ">
														 		<c:if test="${(REQUEST_ACTION_TYPE eq 'AUTO_UPLOAD_RELOAD_STATUS')}">
														 			<jsp:include page="autoUploadReloadStatusMgmt.jsp"></jsp:include> 
														 		</c:if>	 
															</div>
														</sec:authorize>
														</div>
												</div>
											</div>
										</div>
									</div>
								</div>
								<!-- Tab code end here -->
							</div>
						</div>
					</div>
				</div>
			</section>
			
			<form action="<%= ControllerConstants.INIT_RULE_DATA_CONFIG %>" method="GET" id="loadRuleDataDictionary">
	    	</form>
	    	
	    	<form action="<%= ControllerConstants.INIT_AUTO_RELOAD_CACHE_CONFIG %>" method="GET" id="loadAutoReloadCacheConfig">
	    	</form>
			
			<form action="<%= ControllerConstants.INIT_AUTO_UPLOAD_CONFIG %>" method="GET" id="loadAutoUploadConfig">
	    	</form>
	    	
	    	<form action="<%= ControllerConstants.INIT_VIEW_AUTO_UPLOAD_RELOAD_STATUS %>" method="GET" id="loadAutoUploadReloadStatus">
	    	</form>
	    	
		</div>
		<!-- Content div End here -->
		
		<!-- Footer code start here -->
		<footer class="main-footer positfix">
		    	<div class="fooinn">
		        	<jsp:include page="../common/newfooter.jsp"></jsp:include>
		    	</div>
			</footer>
		<!-- Footer code end here -->

</div>
<!-- Main div end Here -->

<script>

function loadRuleDataDictionary(){
	$("#loadRuleDataDictionary").submit();
}

function loadAutoReloadCacheConfig(){
	$("#loadAutoReloadCacheConfig").submit();
}

function loadAutoUploadConfig(){
	$("#loadAutoUploadConfig").submit();
}

function loadViewStatus(){
	$("#loadAutoUploadReloadStatus").submit();
}
</script>

</body>
</html>
