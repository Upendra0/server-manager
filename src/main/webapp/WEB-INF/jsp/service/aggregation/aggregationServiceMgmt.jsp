<%@page import="com.elitecore.sm.common.constants.BaseConstants"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<%@page import="com.elitecore.sm.common.constants.SystemParametersConstant"%>
<%@page import="com.elitecore.sm.util.MapCache"%>


<!DOCTYPE html>

<html>
<script>
	var updateAccessStatus = false;
	var currentTab = '${REQUEST_ACTION_TYPE}';
	var jsSpringMsg = {};
	jsSpringMsg.pagerText = "<spring:message code='jq.grid.pager.text' ></spring:message>";
	jsSpringMsg.emptyRecordText = "<spring:message code='jq.grid.empty.records.text' ></spring:message>";
	jsSpringMsg.totalRecordText ="<spring:message code='jq.grid.pager.total.records.text' ></spring:message>";
	jsSpringMsg.loadingText = "<spring:message code='jq.grid.loading.text' ></spring:message>";
	jsSpringMsg.totalGridRows = "<%= MapCache.getConfigValueAsInteger(SystemParametersConstant.TOTAL_ROWS_TO_DISPLAY_IN_GRID, 10) %>";
	jsSpringMsg.coreExpressionLabel =  "<spring:message code='aggregationService.summary.grid.condition.expression.label' ></spring:message>";
	jsSpringMsg.conditionAction = "<spring:message code='aggregationService.summary.grid.condition.action' ></spring:message>";
	jsSpringMsg.keyAttributeAggfieldLabel = "<spring:message code='aggregationService.service.definition.keyattribute.aggfield.label' ></spring:message>";
	
	jsSpringMsg.aggAttributeOutputFieldNameLabel = "<spring:message code='aggregationService.service.definition.aggattribute.outputfieldname.label' ></spring:message>";
	jsSpringMsg.aggAttributeOpeationExpressionLabel = "<spring:message code='aggregationService.service.definition.aggattribute.operationexpression.label' ></spring:message>";
	jsSpringMsg.aggAttributeOutputFieldDataTypeLabel = "<spring:message code='aggregationService.service.definition.aggattribute.aggoutputfielddatatypefield.label' ></spring:message>";
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
										<strong><a href="<%= ControllerConstants.INIT_SERVICE_MANAGER%>"><spring:message code="aggregationService.leftmenu.home" ></spring:message></a>&nbsp;/ 
										${serviceName}&nbsp;-&nbsp;${serviceInstanceId}
										</strong>
									</span>
									<ol class="breadcrumb1 breadcrumb mtop10">
										 <c:if test="${Boolean.TRUE.toString() ne  MapCache.getConfigValueAsObject(BaseConstants.KUBERNETES_ENV)}">
	       								 <li>
	           								 <div id="service_instance_status_loader_${serviceId}">
	                                			<strong><spring:message code="updtInstacne.head.status.lbl" ></spring:message></strong> &nbsp;|&nbsp;
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
													<sec:authorize access="hasAnyAuthority('SERVICE_MANAGER_MENU_VIEW')">  
														<ul class="nav nav-tabs pull-right">
														
															<sec:authorize access="hasAnyAuthority('VIEW_SERVICE_INSTANCE')">
																<li class="<c:if test="${(REQUEST_ACTION_TYPE eq 'AGGREGATION_SERVICE_SUMMARY')}">
																	<c:out value="active"></c:out></c:if>"	onclick="showButtons('aggregation_service_summary');loadSummary();">
																	<a href="#aggregation-service-summary" id="aggregation_service_summary" data-toggle="tab" aria-expanded="false"> 
																	<spring:message	code="aggregationService.summary.tab" ></spring:message>
																</a></li>
															</sec:authorize>
															<sec:authorize access="hasAnyAuthority('VIEW_SERVICE_INSTANCE')">
																<li
																	class="<c:if test="${(REQUEST_ACTION_TYPE eq 'AGGREGATION_SERVICE_CONFIGURATION')}"><c:out value="active"></c:out></c:if>"
																	onclick="showButtons('aggregation_service_configuration');loadConfiguration();">
																	<a href="#aggregation-service-configuration" id="aggregation_service_configuration"	data-toggle="tab" aria-expanded="true"> 
																	<spring:message	code="aggregationService.configuration.tab" ></spring:message>
																</a>
																</li>
															</sec:authorize>
															<sec:authorize access="hasAnyAuthority('VIEW_PATHLIST')">
																<li
																	class="<c:if test="${(REQUEST_ACTION_TYPE eq 'AGGREGATION_DEFINITION_CONFIGURATION')}"><c:out value="active"></c:out></c:if>"
																	onclick="showButtons('aggregation_definition_configuration');loadAggregationDefinitionConfigPage();">
																	<a href="#aggregation-definition-configuration" id="aggregation_definition_configuration"	data-toggle="tab" aria-expanded="true"> 
																	<spring:message	code="aggregationService.definition.configuration.tab" ></spring:message>
																</a>
																</li>
															</sec:authorize>
															<sec:authorize access="hasAnyAuthority('VIEW_PATHLIST')">
																<li
																	class="<c:if test="${(REQUEST_ACTION_TYPE eq 'AGGREGATION_PATHLIST_CONFIGURATION')}"><c:out value="active"></c:out></c:if>"
																	onclick="showButtons('aggregation_driver_configuration');loadPathListConfigPage();">
																	<a href="#aggregation-pathList-configuration" id="aggregation_pathList_configuration"	data-toggle="tab" aria-expanded="true"> 
																	<spring:message	code="aggregationService.pathList.configuration.tab" ></spring:message>
																</a>
																</li>
															</sec:authorize>
														</ul>
														<div class="fullwidth no-padding tab-content">
														<sec:authorize access="hasAnyAuthority('VIEW_SERVICE_INSTANCE')">
															<div id="aggregation-service-summary"
																class="tab-pane <c:if test="${(REQUEST_ACTION_TYPE eq 'AGGREGATION_SERVICE_SUMMARY')}"><c:out value="active"></c:out></c:if> ">
														 		 <c:if test="${(REQUEST_ACTION_TYPE eq 'AGGREGATION_SERVICE_SUMMARY')}">
														 			<jsp:include page="aggregationServiceSummary.jsp"></jsp:include> 
														 		 </c:if>	
															</div>
														</sec:authorize>
														<sec:authorize access="hasAnyAuthority('VIEW_SERVICE_INSTANCE')">
															<div id="aggregation-service-configuration"
																class="tab-pane  <c:if test="${(REQUEST_ACTION_TYPE eq 'AGGREGATION_SERVICE_CONFIGURATION')}"><c:out value="active"></c:out></c:if>">
																<c:if test="${(REQUEST_ACTION_TYPE eq 'AGGREGATION_SERVICE_CONFIGURATION')}">
																	<jsp:include page="aggregationServiceConfiguration.jsp"></jsp:include>
																</c:if>
															</div>
														</sec:authorize>
														<sec:authorize access="hasAnyAuthority('VIEW_SERVICE_INSTANCE')">
															<div id="aggregation-definition-configuration"
																class="tab-pane  <c:if test="${(REQUEST_ACTION_TYPE eq 'AGGREGATION_DEFINITION_CONFIGURATION')}"><c:out value="active"></c:out></c:if>">
																	<c:if test="${(REQUEST_ACTION_TYPE eq 'AGGREGATION_DEFINITION_CONFIGURATION')}">
																		<jsp:include page="aggregationDefinitionConfiguration.jsp"></jsp:include>
																	</c:if>
															</div>
														</sec:authorize>
														<sec:authorize access="hasAnyAuthority('VIEW_PATHLIST')">
															<div id="aggregation-pathList-configuration"
																class="tab-pane  <c:if test="${(REQUEST_ACTION_TYPE eq 'AGGREGATION_PATHLIST_CONFIGURATION')}"><c:out value="active"></c:out></c:if>">
																	<c:if test="${(REQUEST_ACTION_TYPE eq 'AGGREGATION_PATHLIST_CONFIGURATION')}">
																		<jsp:include page="aggregationPathListConfiguration.jsp"></jsp:include>
																	</c:if>
															</div>
														</sec:authorize>
														</div>
													</sec:authorize>
													<!-- Change Role here -->
													<div style="display: none;">
														<form id="aggregation_service_form" method="POST">
															<input type="text" id="serviceId" name="serviceId" value="${serviceId}">
															<input type="text" id="serviceType" name="serviceType" value="${serviceType}">
															<input type="text" id="serviceName" name="serviceName" value="${serviceName}">
															<input type="hidden" id="serviceInstanceId" name="serviceInstanceId" value="${serviceInstanceId}">
															<input type="hidden" name="instanceId"  value="${instanceId}"/>
														</form>
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

		</div>
		<!-- Content wrapper div start here -->
		
		<!-- Footer code start here -->
		<footer class="main-footer positfix">
		    	<div class="fooinn">
		    		<div class="fullwidth">
						<div class="button-set">
			            	<div class="padleft-right" id="summarybtnDiv" style="display: none;">
			            		<sec:authorize access="hasAuthority('SYNCHRONIZE_SERVICE_INSTANCE')">
			            		<button class="btn btn-grey btn-xs " tabindex="21" onclick="syncServiceInstanceById('${serviceId}')">
									<spring:message code="btn.label.synchronize" ></spring:message>
								</button>
								</sec:authorize>
								<sec:authorize access="hasAuthority('EXPORT_SERVICE_INSTANCE_CONFIG')">
									<button class="btn btn-grey btn-xs " tabindex="23" onclick="exportConfigPopup();">
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
									<sec:authorize access="hasAuthority('START_SERVICE_INSTANCE')">
										<button id="startBtn" style="display:none;" class="btn btn-grey btn-xs " tabindex="23"onclick="setServiceStarStopAction()">
											<spring:message code="btn.label.start" ></spring:message>
										</button>
									</sec:authorize>
									<sec:authorize access="hasAuthority('STOP_SERVICE_INSTANCE')">
										<button id="stopBtn" style="display:none;" class="btn btn-grey btn-xs " tabindex="24"onclick="setServiceStarStopAction()">
											<spring:message code="btn.label.stop" ></spring:message>
										</button>
									</sec:authorize>
								</c:if>
								
							</div>	
				           	
		            		<div class="padleft-right" id="configurationbtnDiv" style="display: none;">
		            		<sec:authorize access="hasAuthority('UPDATE_SERVICE_INSTANCE')">
			            		<button id="btnUpdateService" class="btn btn-grey btn-xs " tabindex="21" onclick="saveConfiguration();">
									<spring:message code="btn.label.update" ></spring:message>
								</button>
								<button id="btnResetService" class="btn btn-grey btn-xs " tabindex="21"  onclick="resetConfiguration();">
									<spring:message code="btn.label.reset" ></spring:message>
								</button>
								<button id="btnCancelService" class="btn btn-grey btn-xs " tabindex="21"  onclick="cancelConfiguration();">
									<spring:message code="btn.label.cancel" ></spring:message>
								</button>
							</sec:authorize>	
		            		</div>
		            		<div id="aggregation-definition-progress-bar-div" class="modal-footer padding10 text-left" style="display: none;">
								<jsp:include page="../../common/processing-bar.jsp"></jsp:include>
							</div>	
		            		<div class="padleft-right" id="aggdefinitionbtnDiv" style="display: none;">
			            		<sec:authorize access="hasAuthority('UPDATE_SERVICE_INSTANCE')">
				            		<button id="btnUpdateDefinition" class="btn btn-grey btn-xs " tabindex="21" onclick="saveDefinition();">
										<spring:message code="btn.label.update" ></spring:message>
									</button>
									<button id="btnCancelDefinition" class="btn btn-grey btn-xs " tabindex="21"  onclick="cancelDefinition();">
										<spring:message code="btn.label.cancel" ></spring:message>
									</button>
								</sec:authorize>	
		            		</div>
						</div>
		        	</div>
		        	 <script type="text/javascript">
		        	 
		        	 
			       		$(document).ready(function() {
			       			var activeTab = $(".nav-tabs li.active a");
			    			var id = activeTab.attr("id");
			    			showButtons(id);
			     		});
			       		
			       		function showButtons(tabType){
			       			$("#summarybtnDiv").hide();
			   				$("#configurationbtnDiv").hide();
			   				$("#aggdefinitionbtnDiv").hide();
			   				
			       			if(tabType == 'aggregation_service_summary'){
			       				$("#summarybtnDiv").show();
			       			}else if(tabType == 'aggregation_service_configuration'){
			       				$("#configurationbtnDiv").show();
			       			}else if (tabType == 'aggregation_pathList_configuration'){
			       				//$("#summarybtnDiv").hide();
				   				//$("#configurationbtnDiv").hide();
			       			}else if(tabType == 'aggregation_definition_configuration'){
			       				$("#aggdefinitionbtnDiv").show();
			       			}
			       			loadServiceStatusGUI('${serviceId}','aggregation_service_summary');
			       		}
			       		
			       		function loadConfiguration(){
			       			$('#aggregation_service_form').attr("action","<%= ControllerConstants.INIT_AGGREGATION_SERVICE_CONFIGURATION %>");
			       			$('#aggregation_service_form').submit();
			       		}
			       		
			       		function loadSummary(){
			       			$('#aggregation_service_form').attr("method","GET");
			       			$('#aggregation_service_form').attr("action","<%= ControllerConstants.INIT_AGGREGATION_SERVICE_MANAGER %>");
			       			$('#aggregation_service_form').submit();
			       		}
			       		
			       		function loadPathListConfigPage(){
			       			$("#aggregation_service_form").attr("action","<%= ControllerConstants.INIT_AGGREGATION_PATHLIST_CONFIGURATION%>");
			       			$("#aggregation_service_form").submit();
			       		}
			       		
			       		function loadAggregationDefinitionConfigPage(){
			       			$("#aggregation_service_form").attr("action","<%= ControllerConstants.INIT_AGGREGATION_DEFINITION_CONFIGURATION%>");
			       			$("#aggregation_service_form").submit();
			       		}
			       		
			       		function aggregationServicesStartStopPopup(){
			       			$("#summary_start_stop_popup").click();	
			       		}
			       		
			       		function startService(){
		        			closeFancyBox();
		        		}
			       		
			       		function setServiceStarStopAction(){
							$("#syncWarningMsg").hide();
				    		$("#server-stop-close-btn").hide();
							$("#service-stop-close-btn").hide();
							$("#server-start-close-btn").hide();
							$("#service-start-close-btn").hide();	
			       			var serviceStatus = $("#serviceStatus").val();
			       			console.log("Service Status::" + serviceStatus);
			       			$("#startServiceId").val('${serviceId}');
			       			if(serviceStatus == 'INACTIVE'){
			       				$("#divStartService #lblServiceId").text('${serviceInstanceId}');
			    				$("#divStartService #lblServiceName").text('${serviceName}');
			    				$("#service-start-close-btn").show();
			    				var syncStatus='${syncStatus}';
			    				if(syncStatus == 'false'){
			    					$("#syncWarningMsg").show();
			    				}
			    				$("#start_service").click();
			    			}else if(serviceStatus == 'ACTIVE'){
			    				$("#service-stop-close-btn").show();
			    				$("#divStopService #lblServiceId").text('${serviceInstanceId}');
			    				$("#divStopService #lblServiceName").text('${serviceName}');
			    				$("#stop_service").click();
			    			}
				       	}

		        	</script> 
		        	<jsp:include page="../../common/newfooter.jsp"></jsp:include>
		    	</div>
			</footer>
		<!-- Footer code end here -->
	</div>
	<!-- Main div end here -->
