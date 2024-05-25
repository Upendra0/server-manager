<%@page import="com.elitecore.sm.util.MapCache"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<%@page import="com.elitecore.sm.common.constants.BaseConstants"%>

<!DOCTYPE html>

<html>
<script>
	var updateAccessStatus = false;
	var currentTab = '${REQUEST_ACTION_TYPE}';
</script>
<script>
   		var jsLocaleMsg = {};
   		jsLocaleMsg.serviceId = "<spring:message code='serviceManagement.grid.column.service.id'></spring:message>";
   		jsLocaleMsg.serviceNm = "<spring:message code='serviceManagement.grid.column.service.name'></spring:message>";
   		jsLocaleMsg.entityName = "<spring:message code='server.instance.import.header.label.entity.name'></spring:message>";
   		jsLocaleMsg.propertyName = "<spring:message code='server.instance.import.header.label.property.name'></spring:message>";
   		jsLocaleMsg.propertyValue = "<spring:message code='server.instance.import.header.label.property.value'></spring:message>";
   		jsLocaleMsg.errorMessage = "<spring:message code='server.instance.import.header.label.error.message'></spring:message>";
   	</script>
   	
<jsp:include page="../commonServiceImport.jsp"></jsp:include>  	
  
<jsp:include page="../../common/newheader.jsp"></jsp:include>

<body class="skin-blue sidebar-mini sidebar-collapse">
<%=session.getAttribute("responseMsg")%>
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
	                                			<strong><spring:message code="updtInstacne.head.status.lbl" ></spring:message></strong><span style="color:#CCCCCC;"> &nbsp;|&nbsp;</span>
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
										<div class="box martop" style="border: none; box-shadow: none;">
											<div class="box-body table-responsive no-padding">
												<div class="nav-tabs-custom tab-content">
												<input type="hidden" id="serviceId" name="serviceId" value="${serviceId}">
												<input type="hidden" id="serviceName" name="serviceName" value="${serviceName}">
												<input type="hidden" id="serviceInstanceId" name="serviceInstanceId" value="${serviceInstanceId}">
												<input type="hidden" id="serviceStatus" name="serviceStatus" value="">
														<ul class="nav nav-tabs pull-right">
														
															<sec:authorize access="hasAnyAuthority('VIEW_SERVICE_INSTANCE')">
																<li class="<c:if test="${(REQUEST_ACTION_TYPE eq 'COLLECTION_SERVICE_SUMMARY')}">
																	<c:out value="active"></c:out></c:if>"	onclick="showButtons('collection_service_summary'); loadCollectionDetails('<%=ControllerConstants.INIT_COLLECTION_SERVICE_MANAGER%>','GET','COLLECTION_SERVICE_SUMMARY');" >
																	<a href="#collection-service-summary" id="collection_service_summary" data-toggle="tab" aria-expanded="false"> 
																	<spring:message	code="collectionService.summary.tab" ></spring:message>
																</a></li>
															</sec:authorize>
															<sec:authorize access="hasAnyAuthority('VIEW_SERVICE_INSTANCE')">
																<li
																	class="<c:if test="${(REQUEST_ACTION_TYPE eq 'COLLECTION_SERVICE_CONFIGURATION')}"><c:out value="active"></c:out></c:if>"
																	onclick="loadCollectionDetails('<%=ControllerConstants.INIT_COLLECTION_SERVICE_CONFIGURATION%>','Post','COLLECTION_SERVICE_CONFIGURATION'); showButtons('collection_service_configuration');">
																	<a href="#collection-service-configuration" id="collection_service_configuration"	data-toggle="tab" aria-expanded="true"> 
																	<spring:message	code="collectionService.configuration.tab" ></spring:message>
																</a>
																</li>
															</sec:authorize>
															<sec:authorize access="hasAnyAuthority('VIEW_COLLECTION_DRIVER')">
																<li
																	class="<c:if test="${(REQUEST_ACTION_TYPE eq 'COLLECTION_DRIVER_CONFIGURATION')}"><c:out value="active"></c:out></c:if>"
																	onclick="showButtons('collection_driver_configuration');loadCollectionDetails('<%=ControllerConstants.INIT_COLLECTION_DRIVER_MANAGER%>','POST','COLLECTION_DRIVER_CONFIGURATION');">
																	<a href="#collection-driver-configuration" id="collection_driver_configuration"	data-toggle="tab" aria-expanded="true"> 
																	<spring:message	code="collectionService.driver.configuration.tab" ></spring:message>
																</a>
																</li>
															</sec:authorize>
														</ul>
														<div class="fullwidth no-padding tab-content">
														<sec:authorize access="hasAnyAuthority('VIEW_SERVICE_INSTANCE')">
															<div id="collection-service-summary"
																class="tab-pane <c:if test="${(REQUEST_ACTION_TYPE eq 'COLLECTION_SERVICE_SUMMARY')}"><c:out value="active"></c:out></c:if> ">
														 		<c:if test="${(REQUEST_ACTION_TYPE eq 'COLLECTION_SERVICE_SUMMARY')}">
														 			<jsp:include page="collectionServiceSummary.jsp"></jsp:include>
														 		</c:if>	 

															</div>
														</sec:authorize>
														<sec:authorize access="hasAnyAuthority('VIEW_SERVICE_INSTANCE')">
															<div id="collection-service-configuration"
																class="tab-pane  <c:if test="${(REQUEST_ACTION_TYPE eq 'COLLECTION_SERVICE_CONFIGURATION')}"><c:out value="active"></c:out></c:if>">
																<c:if test="${(REQUEST_ACTION_TYPE eq 'COLLECTION_SERVICE_CONFIGURATION')}">
																	<jsp:include page="collectionServiceConfiguration.jsp"></jsp:include>
																</c:if>	

															</div>
														</sec:authorize>
														<sec:authorize access="hasAnyAuthority('VIEW_COLLECTION_DRIVER')">
															<div id="collection-driver-configuration"
																class="tab-pane  <c:if test="${(REQUEST_ACTION_TYPE eq 'COLLECTION_DRIVER_CONFIGURATION')}"><c:out value="active"></c:out></c:if>">
																<c:if test="${(REQUEST_ACTION_TYPE eq 'COLLECTION_DRIVER_CONFIGURATION')}"> 
																	<jsp:include page="collectionDriverConfiguration.jsp"></jsp:include>
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
			
			
			<div style="display: hidden;">
				<form id="collection-details" action = "" method="">
					<input type="hidden" name="requestAction"  id="requestAction" value = " " />
					<input type="hidden" name="instanceId"  id="instanceId" value="${instanceId}"/>
					<input type="hidden" name="serviceType"  id="serviceType" value="${serviceType}"/>
					<input type="hidden" name="serviceName"  id="serviceName" value="${serviceName}"/>
					<input type="hidden" name="serviceId"  id="serviceId" value="${serviceId}"/>
					<input type="hidden" id="serviceInstanceId" name="serviceInstanceId" value="${serviceInstanceId}">
				</form>
			</div>
		</div>
		
		<!-- Content wrapper div start here -->
		
		<!-- Footer code start here -->
		<footer class="main-footer positfix">
		    	<div class="fooinn">
		    		<div class="fullwidth">
						<div class="button-set">
			            	<div class="padleft-right" id="summarybtnDiv" style="display: none;">
			            		<sec:authorize access="hasAuthority('SYNCHRONIZE_SERVICE_INSTANCE')">
			            		<button class="btn btn-grey btn-xs " tabindex="21"  onclick="syncServiceInstanceById('${serviceId}')">
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
								<button id = "btnResetService" class="btn btn-grey btn-xs " tabindex="21"  onclick="resetConfiguration();">
									<spring:message code="btn.label.reset" ></spring:message>
								</button>
							</sec:authorize>	
								<button class="btn btn-grey btn-xs " tabindex="21"  onclick="cancelConfiguration();">
									<spring:message code="btn.label.cancel" ></spring:message>
								</button>
		            		</div>
						</div>
		        	</div>
		        	 <script type="text/javascript">
		        	 
		        	 
			       		$(document).ready(function() {
			       			var activeTab = $(".nav-tabs li.active a");
			    			var id = activeTab.attr("id");
			    			showButtons(id);
			    			
			    			loadServiceStatusGUI('${serviceId}','collection_summary'); // Method will load service instance status like service is running or not.
			    			
			     		});
			       		
			       		function showButtons(tabType){
			       			$("#summarybtnDiv").hide();
			   				$("#configurationbtnDiv").hide();
			   				
			       			if(tabType == 'collection_service_summary'){
			       				$("#summarybtnDiv").show();
			       			}else if(tabType == 'collection_service_configuration'){
			       				$("#configurationbtnDiv").show();
			       			}else if (tabType == 'collection_driver_configuration'){
			       				$("#summarybtnDiv").hide();
				   				$("#configurationbtnDiv").hide();
			       			}
			       		}
						
						function setServiceStarStopAction(){

							$("#syncWarningMsg").hide();
							

				    		$("#server-stop-close-btn").hide();
							$("#service-stop-close-btn").hide();
							
							$("#server-start-close-btn").hide();
							$("#service-start-close-btn").hide();	
							
			       			var serviceStatus = $("#serviceStatus").val();
			       			$("#startServiceId").val('${serviceId}');
			       			if(serviceStatus == 'INACTIVE'){
			       				console.log("goint to start service");
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
						
						function loadCollectionDetails(formAction, formMethod,requestAction){
							$("#collection-details").attr("action",formAction);
							$("#collection-details").attr("method",formMethod);
							$("#requestAction").val(requestAction);
							$('#collection-details').submit();	
						}
			       		
			       		function startService(){
		        			closeFancyBox();
		        		}
		
		        	</script> 
		        	<jsp:include page="../../common/newfooter.jsp"></jsp:include>
		    	</div>
			</footer>
		<!-- Footer code end here -->
	</div>
	<!-- Main div end here -->
