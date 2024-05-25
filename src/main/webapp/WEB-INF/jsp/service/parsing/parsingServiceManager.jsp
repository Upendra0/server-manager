<%@page import="com.elitecore.sm.parser.model.PluginTypeMaster"%>
<%@page import="com.elitecore.sm.common.constants.SystemParametersConstant"%>
<%@page import="com.elitecore.sm.util.MapCache"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
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
	                                			<strong><spring:message code="updtInstacne.head.status.lbl" ></spring:message></strong> &nbsp;|&nbsp;
	                                			<img src="img/preloaders/circle-red.gif" width="15px" data-toggle="tooltip" data-placement="bottom" title="Loading Status.." height="15px" style="vertical-align: middle;">
	                                		</div>
           								 </li>
           								 </c:if>
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
																<li id="tab1" class="<c:if test="${(REQUEST_ACTION_TYPE eq 'PARSING_SERVICE_SUMMARY')}"><c:out value="active"></c:out></c:if>" onclick="showButtons('parsing-service-summary');loadSummary();" >
																	<a href="#parsing-service-summary" id="a-parsing-service-summary" data-toggle="tab" aria-expanded="false"> 
																		<spring:message	code="iplog.parsing.service.summary.tab.header" ></spring:message>
																	</a>
																</li>
															</sec:authorize>
															<sec:authorize access="hasAnyAuthority('VIEW_SERVICE_INSTANCE')">
																<li id="tab2" class="<c:if test="${(REQUEST_ACTION_TYPE eq 'PARSING_SERVICE_CONFIGURATION')}"><c:out value="active"></c:out></c:if>" onclick="showButtons('parsing-service-configuration');loadConfiguration();">
																	<a href="#parsing-service-configuration" id="a-parsing-service-configuration"	data-toggle="tab" aria-expanded="true"> 
																		<spring:message	code="iplog.parsing.service.config.tab.header" ></spring:message>
																	</a>
																</li>
															</sec:authorize>
															<sec:authorize access="hasAnyAuthority('VIEW_PATHLIST')">
																<li id="tab3" class="<c:if test="${(REQUEST_ACTION_TYPE eq 'PARSING_SERVICE_PATHLIST_CONFIGURATION')}"><c:out value="active"></c:out></c:if>" onclick="showButtons('parsing-pathlist-config');loadPathlist();">
																	<a href="#parsing-pathlist-config" id="a-parsing-pathlist-config"	data-toggle="tab" aria-expanded="true"> 
																		<spring:message	code="iplog.parsing.service.pathlist.tab.header" ></spring:message>
																	</a>
																</li>
															</sec:authorize>
														</ul>
														<div class="fullwidth no-padding tab-content">
															<sec:authorize access="hasAnyAuthority('VIEW_SERVICE_INSTANCE')">
																<div id="parsing-service-summary" class="tab-pane <c:if test="${(REQUEST_ACTION_TYPE eq 'PARSING_SERVICE_SUMMARY')}"><c:out value="active"></c:out></c:if> ">
																	<c:if test="${(REQUEST_ACTION_TYPE eq 'PARSING_SERVICE_SUMMARY')}">
															 			<jsp:include page="ipLogAndParsingServiceSummary.jsp"></jsp:include>
															 		</c:if>	 
																</div>
															</sec:authorize>
															<sec:authorize access="hasAnyAuthority('VIEW_SERVICE_INSTANCE')">
																<div id="parsing-service-configuration" class="tab-pane  <c:if test="${(REQUEST_ACTION_TYPE eq 'PARSING_SERVICE_CONFIGURATION')}"><c:out value="active"></c:out></c:if>">
																	<c:if test="${(REQUEST_ACTION_TYPE eq 'PARSING_SERVICE_CONFIGURATION')}">
																		<jsp:include page="ipLogAndParsingServiceConfiguration.jsp"></jsp:include>
																	</c:if>	
																</div>
															</sec:authorize>
															<sec:authorize access="hasAnyAuthority('VIEW_PATHLIST')">
																<div id="iplog-parsing-pathlist-config" class="tab-pane  <c:if test="${(REQUEST_ACTION_TYPE eq 'PARSING_SERVICE_PATHLIST_CONFIGURATION')}"><c:out value="active"></c:out></c:if>">
																	<c:if test="${(REQUEST_ACTION_TYPE eq 'PARSING_SERVICE_PATHLIST_CONFIGURATION')}"> 
																		<jsp:include page="parsingPathList.jsp"></jsp:include>
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

		</div>
		
		<form id="parsing-service-config" method="POST" action="<%= ControllerConstants.INIT_UPDATE_PARSING_SERVICE_CONFIGURATION %>">
			<input type="hidden" name="serviceId"  value="${serviceId}"/>
			<input type="hidden" name="serviceType"  value="${serviceType}"/>
			<input type="hidden" id="serviceInstanceId" name="serviceInstanceId" value="${serviceInstanceId}"/>
		</form>
		
	
		<form id="form-parsing-service-summary" method="GET" action="<%= ControllerConstants.INIT_PARSING_SERVICE_MANAGER %>">
			<input type="hidden" name="serviceId"  value="${serviceId}"/>
			<input type="hidden" name="serviceType"  value="${serviceType}"/>
			<input type="hidden" name="serviceName"  value="${serviceName}"/>
			<input type="hidden" name="instanceId"  value="${instanceId}"/>
			<input type="hidden" id="serviceInstanceId" name="serviceInstanceId" value="${serviceInstanceId}"/>
		</form>
		
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
			
			<form id="parsing-service-parser-config" method="GET" action="<%=ControllerConstants.INIT_PARSER_CONFIGURATION%>">
			<input type="hidden" id="serviceId" name="serviceId" value="${serviceId}"/>
			<input type="hidden" id="serviceName" name="serviceName" value="${serviceName}"/>
			<input type="hidden" name="instanceId"  id="instanceId" value="${instanceId}"/>
			<input type="hidden" name="serviceType"  id="serviceType" value="${serviceType}"/>
			<input type="hidden" id="serviceInstanceId" name="serviceInstanceId" value="${serviceInstanceId}">
			<input type="hidden" id="plugInId" name="plugInId"/>
			<input type="hidden" id="plugInName" name="plugInName"/> 
			<input type="hidden" id="plugInType" name="plugInType"/>
		</form>
		
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
		            			<sec:authorize access="hasAnyAuthority('UPDATE_SERVICE_INSTANCE')">
			            		<button id="btnUpdateService" class="btn btn-grey btn-xs " tabindex="21" onclick="updateConfigForm();">
									<spring:message code="btn.label.update" ></spring:message>
								</button>
								<button id="btnResetService" class="btn btn-grey btn-xs " tabindex="21"  onclick="resetConfiguration();">
									<spring:message code="btn.label.reset" ></spring:message>
								</button>
								<button class="btn btn-grey btn-xs " tabindex="21"  onclick="cancelConfiguration();">
									<spring:message code="btn.label.cancel" ></spring:message>
								</button>
								</sec:authorize>
								<div class="pull-right" style="font-size:10px;"><i class="fa fa-square" style="font-size: 9px"></i>&nbsp;&nbsp;<spring:message code="restart.operation.require.message" ></spring:message> </div>
		            		</div>
		            		<div class="padleft-right" id="pathlistConfigurationbtnDiv" style="display: none;height:30px;">
		            			<div class="pull-right" style="font-size:10px;"><i class="fa fa-square" style="font-size: 9px"></i>&nbsp;&nbsp;<spring:message code="restart.operation.require.message" ></spring:message> </div>
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
			       			$("#configurationbtnDiv").hide();
			       			$("#summarybtnDiv").hide();
			       			$("#pathlistConfigurationbtnDiv").hide();
			       			
			       			if(tabType == 'a-parsing-service-summary'){
			       				$("#summarybtnDiv").show();
			       			} else if(tabType == 'a-parsing-service-configuration'){
			       				$("#configurationbtnDiv").show();
			       			} else if (tabType == 'a-parsing-pathlist-config'){
				   				$("#pathlistConfigurationbtnDiv").show();
			       			}
			       			// load service status on click of each tab
			       			loadServiceStatusGUI('${serviceId}','parsing_service_summary');
			       		}
			       		
			       		function startService(){
		        			closeFancyBox();
		        		}
							
			       		function loadConfiguration(){
			       			$('#parsing-service-config').attr("action","<%= ControllerConstants.INIT_UPDATE_PARSING_SERVICE_CONFIGURATION %>")
			       			$('#parsing-service-config').submit();
			       			showButtons('a-iplog-parsing-service-configuration');
			       		}
			       		
			       		function loadSummary(){
			       			$('#form-parsing-service-summary').submit();
			       			showButtons('a-iplog-parsing-service-summary');
			       		}
			       		
			       		function loadPathlist(){
			       			$('#parsing-service-config').attr("action","<%= ControllerConstants.INIT_UPDATE_PARSING_PATHLIST_CONFIGURATION %>")
			       			$('#parsing-service-config').submit();
			       			showButtons('a-iplog-parsing-service-configuration');
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
			       		
			       			
			       		function redirectParserConfig(parserId,parserName,parserType){
			       			<%-- <%PluginTypeMaster pluginMaster = (PluginTypeMaster) MapCache.loadObjectById(1, SystemParametersConstant.PARSER_PLUGIN_TYPE_LIST);%>
			       			
			       			var type = '<%=pluginMaster.getAlias()%>'; --%>
			       			$('#plugInId').val(parserId);
			       			$('#plugInName').val(parserName);
			       			$('#plugInType').val(parserType);
			       			$('#parsing-service-parser-config').submit();
			       			
			       		}
		
		        	</script> 
		        	<jsp:include page="../../common/newfooter.jsp"></jsp:include>
		    	</div>
			</footer>
		<!-- Footer code end here -->
	</div>
	<!-- Main div end here -->
