<%@page import="com.elitecore.sm.util.MapCache"%>
<%@page import="com.elitecore.sm.common.constants.BaseConstants"%>
<%@page import="com.elitecore.sm.common.constants.EngineConstants"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>

<!DOCTYPE html>

<html>

<script>
	var driverListCounter=0;
	var ckIntanceSelected = new Array();	
	var isViewComposerAccess =  false;
	var isUpdateAccessToChangeDriverStatus =  false;
	var isViewDriverAccess = false;
	
	var currentTab = '${REQUEST_ACTION_TYPE}';
	var currentRequestAction = '${REQUEST_ACTION_TYPE}';

	var jsSpringMsg = {};
	
	jsSpringMsg.applicationOrderGridHeading="<spring:message code='distribution.service.summary.driver.grid.column.driver.applicationorder' ></spring:message>";
	jsSpringMsg.driverNameGridHeading="<spring:message code='distribution.service.summary.driver.grid.column.driver.name' ></spring:message>";
	jsSpringMsg.driverTypeGridHeading="<spring:message code='distribution.service.summary.driver.grid.column.driver.type' ></spring:message>";
	jsSpringMsg.driverHostGridHeading="<spring:message code='distribution.service.summary.driver.grid.column.driver.host' ></spring:message>";
	jsSpringMsg.driverPortGridHeading="<spring:message code='distribution.service.summary.driver.grid.column.driver.port' ></spring:message>";
	jsSpringMsg.driverPluginListGridHeading="<spring:message code='distribution.service.distribution.driver.plugin.list' ></spring:message>";
	jsSpringMsg.driverStatusGridHeading="<spring:message code='distribution.service.distribution.driver.enable' ></spring:message>";
	
	jsSpringMsg.gridCaption ="<spring:message code='distribution.service.distribution.driver.grid.caption'></spring:message>";
	jsSpringMsg.recordText ="<spring:message code='distribution.service.summary.driver.grid.pager.total.records.text'></spring:message>";
	jsSpringMsg.emptyRecord ="<spring:message code='distribution.service.summary.driver.grid.empty.records'></spring:message>";
	jsSpringMsg.loadText="<spring:message code='distribution.service.summary.driver.grid.loading.text'></spring:message>"
	jsSpringMsg.pgText="<spring:message code='distribution.service.summary.driver.grid.pager.text'></spring:message>";
	
	jsSpringMsg.associatedPluginList="<spring:message code='distribution.service.summary.associated.plugin.label'></spring:message>";
	
	jsSpringMsg.composerName ="<spring:message code='distribution.service.summary.driver.plugin.list.column.name.label'></spring:message>"; 
	jsSpringMsg.composerType = "<spring:message code='distribution.service.summary.driver.plugin.list.column.type.label'></spring:message>";
	jsSpringMsg.pathListName = "<spring:message code='distribution.service.summary.driver.plugin.list.column.read.path.label'></spring:message>"  
		
	
	
	
</script>
<jsp:include page="../commonServiceImport.jsp"></jsp:include>

<jsp:include page="../../common/newheader.jsp"></jsp:include>


<body class="skin-blue sidebar-mini sidebar-collapse">
	
	<!-- Main div start here -->
	<div>
		<jsp:include page="../../common/newtopNavigationPanel.jsp"></jsp:include>
		<jsp:include page="../../common/newleftMenu.jsp"></jsp:include>

		<!-- Content wrapper div start here -->
		<div class="content-wrapper">
			<section class="content-header">
				<div class="fullwidth">
					<div style="padding: 0 10px 0 4px;">
						<div id="content-scroll-d" class="content-scroll">
							<div class="fullwidth">
								<h4 style="margin-top:10px; margin-bottom:0px; font-size:11px;">
									<a href="home.html"><img src="img/tile-icon.png"class="vmiddle" alt="Home" /></a> 
									<span class="spanBreadCrumb" style="line-height: 30px;"> 
										<strong><a href="<%= ControllerConstants.INIT_SERVICE_MANAGER%>"><spring:message code="collectionService.leftmenu.home" ></spring:message></a>&nbsp;/ 
										${serviceName}&nbsp;-&nbsp;${serviceInstanceId}
										</strong>
									</span>
									<ol class="breadcrumb1 breadcrumb mtop10">
										 <c:if test="${Boolean.TRUE.toString() ne  MapCache.getConfigValueAsObject(BaseConstants.KUBERNETES_ENV)}">
           								 <li>
	           								 <div id="service_instance_status_loader_${serviceId}">
	                                			<strong><spring:message code="updtInstacne.head.status.lbl" ></spring:message></strong> <span style="color:#CCCCCC;"> &nbsp;|&nbsp;</span>
	                                			<img src="img/preloaders/circle-red.gif" width="15px" data-toggle="tooltip" data-placement="bottom" title="Loading Status.." height="15px" style="vertical-align: middle;">
	                                		</div>
           								 </li></c:if>
            							 <li><b><spring:message code="updtInstacne.head.lst.updt.time.lbl" ></spring:message></b></li>
            							 <li>${lastUpdateTime}</li>
          							</ol>
								</h4>
								
								<jsp:include page="../../common/responseMsg.jsp" ></jsp:include>	
								
								<!-- Tab code start here  -->
								<div class="row">
									<div class="col-xs-12">
										<div class="box  martop" style="border: none; box-shadow: none;">
											<div class="box-body table-responsive no-padding">
												<div class="nav-tabs-custom tab-content">
												<input type="hidden" id="serviceId" name="serviceId" value="${serviceId}">
												<input type="hidden" id="serviceName" name="serviceName" value="${serviceName}">
												<input type="hidden" id="serviceInstanceId" name="serviceInstanceId" value="${serviceInstanceId}">
												<input type="hidden" id="serviceStatus" name="serviceStatus" value="">
												
													<!-- Change Role here -->
													<sec:authorize access="hasAnyAuthority('VIEW_SERVICE_INSTANCE')">  
														<ul class="nav nav-tabs pull-right">
														
															<sec:authorize access="hasAnyAuthority('VIEW_SERVICE_INSTANCE')">
																<li class="<c:if test="${(REQUEST_ACTION_TYPE eq 'DISTRIBUTION_SERVICE_SUMMARY')}">
																	<c:out value="active"></c:out></c:if>"	onclick="showButtons('distribution_service_summary');loadDistributionDetails('<%=ControllerConstants.INIT_DISTRIBUTION_SERVICE_MANAGER%>','GET','DISTRIBUTION_SERVICE_SUMMARY');">
																	<a href="#distribution-service-summary" id="distribution_service_summary" data-toggle="tab" aria-expanded="false"> 
																	<spring:message	code="collectionService.summary.tab" ></spring:message>
																</a></li>
															</sec:authorize>
															<sec:authorize access="hasAnyAuthority('VIEW_SERVICE_INSTANCE')">
																<li
																	class="<c:if test="${(REQUEST_ACTION_TYPE eq 'DISTRIBUTION_SERVICE_CONFIGURATION')}"><c:out value="active"></c:out></c:if>"
																	onclick="showButtons('distribution_service_configuration');loadDistributionDetails('<%=ControllerConstants.INIT_DISTRIBUTION_SERVICE_CONFIGURATION%>','POST','DISTRIBUTION_SERVICE_CONFIGURATION');">
																	<a href="#distribution-service-configuration" id="distribution_service_configuration"	data-toggle="tab" aria-expanded="true"> 
																	<spring:message	code="collectionService.configuration.tab" ></spring:message>
																</a>
																</li>
															</sec:authorize>
															<sec:authorize access="hasAnyAuthority('VIEW_SERVICE_INSTANCE')">
																<li
																	class="<c:if test="${(REQUEST_ACTION_TYPE eq 'DISTRIBUTION_DRIVER_CONFIGURATION')}"><c:out value="active"></c:out></c:if>"
																	onclick="showButtons('distribution_driver_configuration');loadDistributionDetails('<%=ControllerConstants.INIT_DISTRIBUTION_DRIVER_MANAGER%>','POST','DISTRIBUTION_DRIVER_CONFIGURATION');">
																	<a href="#distribution-driver-configuration" id="distribution_driver_configuration"	data-toggle="tab" aria-expanded="true"> 
																	<spring:message	code="collectionService.driver.configuration.tab" ></spring:message>
																</a>
																</li>
															</sec:authorize>
														</ul>
														<div class="fullwidth no-padding tab-content">
														<sec:authorize access="hasAnyAuthority('VIEW_SERVICE_INSTANCE')">
															<div id="distribution-service-summary"
																class="tab-pane <c:if test="${(REQUEST_ACTION_TYPE eq 'DISTRIBUTION_SERVICE_SUMMARY')}"><c:out value="active"></c:out></c:if> ">
	 														 		<jsp:include page="distributionServiceSummary.jsp"></jsp:include>  
															</div>
														</sec:authorize>
														<sec:authorize access="hasAnyAuthority('VIEW_SERVICE_INSTANCE')">
															<div id="distribution-service-configuration"
																class="tab-pane  <c:if test="${(REQUEST_ACTION_TYPE eq 'DISTRIBUTION_SERVICE_CONFIGURATION')}"><c:out value="active"></c:out></c:if>">
																
																<c:if test="${(REQUEST_ACTION_TYPE eq 'DISTRIBUTION_SERVICE_CONFIGURATION')}">
																	<jsp:include page="distributionServiceConfiguration.jsp"></jsp:include> 
																</c:if>
 																
															</div>
														</sec:authorize>
														
														<sec:authorize access="hasAnyAuthority('VIEW_SERVICE_INSTANCE')">
															<div id="distribution-service-configuration"
																class="tab-pane  <c:if test="${(REQUEST_ACTION_TYPE eq 'DISTRIBUTION_DRIVER_CONFIGURATION')}"><c:out value="active"></c:out></c:if>">
																<c:if test="${(REQUEST_ACTION_TYPE eq 'DISTRIBUTION_DRIVER_CONFIGURATION')}">
																	<jsp:include page="distributionDriverConfig.jsp"></jsp:include>
																</c:if>
															</div>
														</sec:authorize>
														
														</div>
													</sec:authorize>
													<!-- Change Role here -->
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
				
			<div style="display: hidden;">
				<form id="distribution_service_details" action = "" method="">
					<input type="hidden" name="requestAction"  id="requestAction" value = " " />
					<input type="hidden" name="instanceId"  id="instanceId" value="${instanceId}"/>
					<input type="hidden" name="serviceType"  id="serviceType" value="${serviceType}"/>
					<input type="hidden" name="serviceName"  id="serviceName" value="${serviceName}"/>
					<input type="hidden" name="serviceId"  id="serviceId" value="${serviceId}"/>
					<input type="hidden" id="serviceInstanceId" name="serviceInstanceId" value="${serviceInstanceId}">
				</form>
			</div>	
				
			 <form action="<%= ControllerConstants.INIT_DISTRIBUTION_DRIVER_CONFIGURATION %>" method="POST" id="distribution-driver-config-form">
	    		<input type="hidden" id="driverId" name="driverId"/>
	    		<input type="hidden" id="driverName" name="driverName" value="${driverName}"/>
	    		<input type="hidden" id="driverTypeAlias" name="driverTypeAlias"/>
	    		<input type="hidden" id="serviceId" name="serviceId" value="${serviceId}"/>
	    		<input type="hidden" id="serviceName" name="serviceName" value="${serviceName}"/>
	    		<input type="hidden" id="instanceId" name="instanceId" value="${instanceId}"/>
	    		<input type="hidden" id="serviceInstanceId" name="serviceInstanceId" value="${serviceInstanceId}">
	    	</form>	
	    	
			<form id="distribution-service-composer-config" method="GET" action="<%=ControllerConstants.INIT_COMPOSER_CONFIGURATION%>">
				<input type="hidden" id="serviceId" name="serviceId" value="${serviceId}"/>
				<input type="hidden" id="serviceName" name="serviceName" value="${serviceName}"/>
				<input type="hidden" name="instanceId"  id="instanceId" value="${instanceId}"/>
				<input type="hidden" name="serviceType"  id="serviceType" value="${serviceType}"/>
				<input type="hidden" id="serviceInstanceId" name="serviceInstanceId" value="${serviceInstanceId}">
				<input type="hidden" id="composerId" name="composerId"/>
				<input type="hidden" id="composerName" name="composerName"/> 
				<input type="hidden" id="composerType" name="composerType"/>
			</form>	
		</div>
		<!-- Content wrapper div start here -->
		
		<!-- Footer code start here -->
		<footer class="main-footer positfix">
		    	<div class="fooinn">
		    		<div class="fullwidth">
						<div class="button-set">
			            	<div class="padleft-right" id="summaryBtnDiv" style="display: none;">
			            		<sec:authorize access="hasAuthority('SYNCHRONIZE_SERVICE_INSTANCE')">
			            		<button class="btn btn-grey btn-xs " tabindex="21"  onclick="syncServiceInstanceById('${serviceId}');">
									<spring:message code="btn.label.synchronize" ></spring:message>
								</button>
								</sec:authorize>
								<sec:authorize access="hasAuthority('EXPORT_SERVICE_INSTANCE_CONFIG')">
								<button class="btn btn-grey btn-xs " tabindex="23" onclick="exportConfigPopup('<%=BaseConstants.DISTRIBUTION_SERVICE_SUMMARY%>');">
									<spring:message code="btn.label.export.config" ></spring:message>
								</button>
								</sec:authorize>
								<sec:authorize access="hasAuthority('IMPORT_SERVICE_INSTANCE_CONFIG')">
								<button id="importService_btn" class="btn btn-grey btn-xs " tabindex="22" onclick="importConfigPopup();">
									<spring:message code="btn.label.import.config"  ></spring:message>
								</button>
								</sec:authorize>
								<sec:authorize access="hasAuthority('SYNC_PUBLISH_VERSION')">
								<button id="syncPublishBtn" class="btn btn-grey btn-xs " tabindex="22" onclick="syncPublishPopup();">
									<spring:message code="btn.label.sync.publish"  ></spring:message>
								</button>
								</sec:authorize>
								<c:if test="${Boolean.TRUE.toString() ne  MapCache.getConfigValueAsObject(BaseConstants.KUBERNETES_ENV)}">
									<sec:authorize access="hasAuthority('UPDATE_SERVICE_INSTANCE')">
										<button id="startBtn" style="display:none;" class="btn btn-grey btn-xs " tabindex="23"onclick="starStoptDistributionService('${serviceId}','${serviceName}','${serviceInstanceId}');">
											<spring:message code="btn.label.start" ></spring:message>
										</button>
										<button id="stopBtn" style="display:none;" class="btn btn-grey btn-xs " tabindex="24"onclick="starStoptDistributionService('${serviceId}','${serviceName}','${serviceInstanceId}');">
											<spring:message code="btn.label.stop" ></spring:message>
										</button>
									</sec:authorize>
								</c:if>
							</div>	
				           	
		            		<div class="padleft-right" id="serviceConfigBtnDiv" style="display: none;">
		            		<sec:authorize access="hasAuthority('UPDATE_SERVICE_INSTANCE')">
			            		<button id="updt_btn" class="btn btn-grey btn-xs " tabindex="21" onclick="updateDistributionServiceConfiguration();">
									<spring:message code="btn.label.update" ></spring:message>
								</button>
								<button id="reset_btn" class="btn btn-grey btn-xs " tabindex="22" onclick="resetAllDistributionParameters();">
									<spring:message code="btn.label.reset" ></spring:message>
								</button>
								<button id="cancel_btn" class="btn btn-grey btn-xs " tabindex="23"  onclick="loadDistributionDetails('<%=ControllerConstants.INIT_DISTRIBUTION_SERVICE_MANAGER%>','GET','DISTRIBUTION_SERVICE_SUMMARY');">
									<spring:message code="btn.label.cancel" ></spring:message>
								</button>
							</sec:authorize>	
		            		</div>
						</div>
		        	</div>
		        	
		        	
		        	<!--  Jqeury All footer pop up start here -->
						<div id="divSynchronize" style=" width:100%; display: none;" >
						    <jsp:include page="../synchronizationPopUp.jsp"></jsp:include>
						</div>
						
						<div id="divImportConfig" style=" width:100%; display: none;" >
						    <jsp:include page="../importServiceWiseConfigPopUp.jsp"></jsp:include>
						</div>
						
						<jsp:include page="../../server/divSyncPublish.jsp"></jsp:include>
						<div id="divStartService" style=" width:100%; display: none;" >
						    <jsp:include page="../startServicePopUp.jsp"></jsp:include>
						</div>
						<div id="divStopService" style=" width:100%; display: none;" >
						     <jsp:include page="../stopServicePopUp.jsp"></jsp:include>
						</div>
						
						<a href="#divSynchronize" class="fancybox" style="display: none;" id="synchronize_service_link">#</a>
				       	<a href="#divImportConfig" class="fancybox" style="display: none;" id="importconfig">#</a>
				       	<a href="#divSyncPublish" class="fancybox" style="display: none;" id="syncPublish">#</a>
				       	<a href="#divStartService" class="fancybox" style="display: none;" id="start_service_link">#</a>
				       	<a href="#divStopService" class="fancybox" style="display: none;" id="stop_service_link">#</a>
					<!--  Jquery All footer pop up end here  -->
		        	<script type="text/javascript">
		        	
			        	<sec:authorize access="hasAnyAuthority('VIEW_COMPOSER')">
			        		isViewComposerAccess = true;
			        	</sec:authorize>
			        	
			        	<sec:authorize access="hasAnyAuthority('UPDATE_DISTRIBUTION_DRIVER')">
			        		isUpdateAccessToChangeDriverStatus = true;
			        	</sec:authorize>
		        	
			        	<sec:authorize access="hasAnyAuthority('VIEW_DISTRIBUTION_DRIVER')">
			        		isViewDriverAccess = true;
		        		</sec:authorize>
			        	
		        		$(document).ready(function() {
			       			var activeTab = $(".nav-tabs li.active a");
			    			var id = activeTab.attr("id");
			    			showButtons(id);
			    			loadServiceStatusGUI('${serviceId}','distribution_summary'); // Method will load service instance status like service is running or not.
							disableFileGroupingParams('${distribution_service_configuration_form_bean.fileGroupingParameter.fileGroupEnable}');
			     		});
		        	</script>
		        	<jsp:include page="../../common/newfooter.jsp"></jsp:include>
		    	</div>
		    	<script src="${pageContext.request.contextPath}/customJS/distributionServiceManager.js<%= "?v=" + Math.random() %>" type="text/javascript"></script>
		    	<script src="${pageContext.request.contextPath}/customJS/distributionDriverManager.js<%= "?v=" + Math.random() %>" type="text/javascript"></script>
		    	
			</footer>
		<!-- Footer code end here -->
	</div>
	<!-- Main div end here -->
	
<script>
function syncPublishPopup(){

	clearAllMessages();

	$('#sync-publish-buttons-div #btn-no').attr('onclick','closeFancyBox();');
		
	$("#syncPublish").click();	
		
} 

function syncPublishInstance(){
	$("#sync-publish-buttons-div").hide();
	$("#sync-publish-progress-bar-div").show();
	$("#buttons-div").hide();
	$("#progress-bar-div").show();
	clearAllMessages();
	$.ajax({
			url: '<%= ControllerConstants.SYNC_PUBLISH_SERVICE_INSTANCE %>',
			    cache: false,
				async: true,
				dataType: 'json',
				type: "POST",
				data: {
					id: $('#serviceId').val(),
					description : $("#descSyncPublish").val(),
					serverInstancesStatus:'ACTIVE'
				},
				success: function(data){
					$("#sync-publish-buttons-div").show();
					$("#sync-publish-progress-bar-div").hide();
					$("#buttons-div").show();
					$("#progress-bar-div").hide();
					var response = eval(data);
			    	response.msg = decodeMessage(response.msg);
			    	response.msg = replaceAll("+"," ",response.msg);
			    	$("#descSyncPublish").val("");
			    	if(response.code == 200 || response.code == "200") {
			    		clearResponseMsgDiv();
			    		clearResponseMsg();
			    		clearErrorMsg();
			    		showSuccessMsg(response.msg);
			    		closeFancyBox();
			    	}else{
			    		showErrorMsg(response.msg);
		    			closeFancyBox();
			    	}
				},
			    error: function (xhr,st,err){
			    	$("#sync-publish-buttons-div").show();
					$("#sync-publish-progress-bar-div").hide();
					$("#buttons-div").show();
					$("#progress-bar-div").hide();
			    	handleGenericError(xhr,st,err);
				}
			});
}

</script> 
