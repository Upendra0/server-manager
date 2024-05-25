<%@page import="com.elitecore.sm.util.MapCache"%>
<%@page import="com.elitecore.sm.common.constants.BaseConstants"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="/WEB-INF/ShowErrorTag.tld" prefix="elitecoreError"%>
<!DOCTYPE html>

<html>

<jsp:include page="../common/newheader.jsp"></jsp:include>

<c:choose>
	<c:when
		test="${((REQUEST_ACTION_TYPE ne 'UPDATE_SNMP_CONFIG') and (REQUEST_ACTION_TYPE ne 'UPDATE_SYSTEM_AGENT_CONFIG'))}">
		<script type="text/javascript">
               $(document).ready(function() {
               	var activeTab = $(".nav-tabs li.active a");
                var id = activeTab.attr("id");
                showButtons(id);
                            					
               // each time when page loads or reload get fresh instance status from server
               
               var serverInstance_Id;
               if('${server_instance_form_bean.id}' != null && '${server_instance_form_bean.id}' != ''){
            	   serverInstance_Id = '${server_instance_form_bean.id}';
               }else if('${serverInstanceId}' != null && '${serverInstanceId}' !=''){
            	   serverInstance_Id = '${serverInstanceId}';
               }
                loadInstanceStatus(serverInstance_Id);
               
                });
               
               function showHideButtonBasedOnTabsSelected(tabType) {
           		$("#advanceconfigDiv").hide();
           		$("#systemLogDiv").hide();
           		$("#statisticDiv").hide();
           		$("#summaryDiv").hide();
           		$("#configHistoryDiv").hide();

           		$("#loader_${server_instance_form_bean.id}")
           				.html(
           						'<strong><spring:message code="updtInstacne.head.status.lbl" ></spring:message></strong> &nbsp;|&nbsp;'
           								+ '<img src="img/preloaders/circle-red.gif" width="15px" data-toggle="tooltip" data-placement="bottom" title="Loading Status.." height="15px" style="vertical-align: middle;">');
           		loadInstanceStatus('${server_instance_form_bean.id}');
            		$('#init-update-form #serverInstanceId').val(
            				'${server_instance_form_bean.id}');

           		if (tabType == 'INSTANCE_SUMMARY') {
           			$("#summaryDiv").show();
           			$('#init-update-form #REQUEST_ACTION_TYPE').val('<%=BaseConstants.UPDATE_INSTANCE_SUMMARY%>');
           		}else if(tabType == 'ADVANCE_CONFIG'){
           			$("#advanceconfigDiv").show();
           			$('#init-update-form #REQUEST_ACTION_TYPE').val('<%=BaseConstants.UPDATE_INSTANCE_ADVANCE_CONFIG%>');
           		}else if(tabType == 'STATISTIC'){
           			$("#statisticDiv").show();
           			$('#init-update-form #REQUEST_ACTION_TYPE').val('<%=BaseConstants.UPDATE_INSTANCE_STATISTIC%>');
           		}else if(tabType == 'SYSTEM_LOG'){
           			$("#systemLogDiv").show();
           			$('#init-update-form #REQUEST_ACTION_TYPE').val('<%=BaseConstants.UPDATE_INSTANCE_SYSTEM_LOG%>');
           		}else if(tabType == 'CONFIG_HISTORY'){
                    $("#configHistoryDiv").show();
                    $('#init-update-form #REQUEST_ACTION_TYPE').val('<%=BaseConstants.CONFIG_HISTORY_SUMMARY%>');
                }
           	}
         </script>
	</c:when>
	<c:otherwise>
		<script type="text/javascript">
			$(document).ready(function() {
                 var activeTab = $(".nav-tabs li.active a");
                 var id = activeTab.attr("id");
                 showButtons(id);
                 				
                 // each time when page loads or reload get fresh instance status from server
                 loadInstanceStatus('${serverInstanceId}');
                 });
			
			function showHideButtonBasedOnTabsSelected(tabType) {
				$("#advanceconfigDiv").hide();
				$("#systemLogDiv").hide();
				$("#statisticDiv").hide();
				$("#summaryDiv").hide();

				$("#loader_${serverInstanceId}")
						.html(
								'<strong><spring:message code="updtInstacne.head.status.lbl" ></spring:message></strong> &nbsp;|&nbsp;'
										+ '<img src="img/preloaders/circle-red.gif" width="15px" data-toggle="tooltip" data-placement="bottom" title="Loading Status.." height="15px" style="vertical-align: middle;">');
				
				loadInstanceStatus('${serverInstanceId}');
				$('#init-update-form #serverInstanceId').val(
						'${serverInstanceId}');

				if (tabType == 'INSTANCE_SUMMARY') {
					$("#summaryDiv").show();
					$('#init-update-form #REQUEST_ACTION_TYPE').val('<%=BaseConstants.UPDATE_INSTANCE_SUMMARY%>');
				}else if(tabType == 'ADVANCE_CONFIG'){
					$("#advanceconfigDiv").show();
					$('#init-update-form #REQUEST_ACTION_TYPE').val('<%=BaseConstants.UPDATE_INSTANCE_ADVANCE_CONFIG%>');
				}else if(tabType == 'SYSTEM_LOG'){
					$("#systemLogDiv").show();
					$('#init-update-form #REQUEST_ACTION_TYPE').val('<%=BaseConstants.UPDATE_INSTANCE_SYSTEM_LOG%>');
				}else if(tabType == 'STATISTIC'){
					$("#statisticDiv").show();
					$('#init-update-form #REQUEST_ACTION_TYPE').val('<%=BaseConstants.UPDATE_INSTANCE_STATISTIC%>');
				}
			}
		</script>
	</c:otherwise>

</c:choose>

<script>
	var currentTab = '${REQUEST_ACTION_TYPE}';
	var jsLocaleMsg = {};
	jsLocaleMsg.serviceId = "<spring:message code='serviceManagement.grid.column.service.id'></spring:message>";
	jsLocaleMsg.serviceNm = "<spring:message code='serviceManagement.grid.column.service.name'></spring:message>";
	
	function submitSystemAgentRequest() {
		
		if (currentTab != 'UPDATE_SNMP_CONFIG' && currentTab != 'UPDATE_SYSTEM_AGENT_CONFIG') {
			$('#agent_serverInstanceId').val('${server_instance_form_bean.id}');
		} else {
			$('#agent_serverInstanceId').val('${serverInstanceId}');
		}

		$("#init-update-agent").submit();

	}

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
	jsLocaleMsg.importFileMiss = "<spring:message code='server.instance.import.file.missing'></spring:message>";
	jsLocaleMsg.LineNo = "<spring:message code='server.instance.import.header.label.line.number'></spring:message>";

	jsLocaleMsg.serviceId = "<spring:message code='serviceManagement.grid.column.service.id'></spring:message>";
	jsLocaleMsg.serviceNm = "<spring:message code='serviceManagement.grid.column.service.name'></spring:message>";
</script>

<script type="text/javascript">
	
    
	function redirectPolicyMgmt() {
		$("#server-instance-name").val($('#lblInstanceName').text());
		$("#server-instance-host").val($('#lblInstanceHost').text());
		$("#server-instance-port").val($('#lblInstancePort').text());
		$("#server-instance-id").val($('#instance-id').val());
		if($("#server-instance-id").val() == ''){
			if (currentTab != 'UPDATE_SNMP_CONFIG' && currentTab != 'UPDATE_SYSTEM_AGENT_CONFIG') {
				$('#server-instance-id').val('${server_instance_form_bean.id}');
			} else {
				$('#server-instance-id').val('${serverInstanceId}');
			}	
		}
		$("#business-policy-form").submit();
	}
	
	
	
	function submitSnmpRequest() {
		
		if (currentTab != 'UPDATE_SYSTEM_AGENT_CONFIG' && currentTab != 'UPDATE_SNMP_CONFIG') {
			$('#snmpserverInstanceId').val('${server_instance_form_bean.id}');

		} else {
			$('#snmpserverInstanceId').val('${serverInstanceId}');
		}
		$("#init-update-snmp").submit();
	}

	function submitConsolidateStateForm() {
		$("#consol-state-instanceName").val($('#lblInstanceName').text());
		$("#consol-state-instanceHost").val($('#lblInstanceHost').text());
		$("#consol-state-instancePort").val($('#lblInstancePort').text());
		$("#consol-state-instanceId").val($('#instance-id').val());
		$('#consolidate-state-form').submit();
	}

	function submitInitForm() {
		$('#init-update-form').submit();
		showButtons();
	}

	function showButtons(id) {
		if (id == 'instance-summary-a') {
			showHideButtonBasedOnTabsSelected('INSTANCE_SUMMARY');
		} else if (id == 'advanced-config-a') {
			showHideButtonBasedOnTabsSelected('ADVANCE_CONFIG');
		} else if (id == 'system-log-a') {
			showHideButtonBasedOnTabsSelected('SYSTEM_LOG');
		} else if (id == 'statistic-a') {
			showHideButtonBasedOnTabsSelected('STATISTIC');
		}else if(id == 'config-history-a'){
            showHideButtonBasedOnTabsSelected('CONFIG_HISTORY');
        }
	}
/* 
	function cancelUpdate() {
		//window.location = document.getElementById('cancelUpdateBtn').href;
		window.location = $('#cancelUpdateBtn').prop('href');
		
	} */

	function loadInstanceStatus(serverInstanceId) {
		$.ajax({
					url : 'loadServerInstanceStatus',
					cache : false,
					async : true,
					dataType : 'json',
					type : "POST",
					data : {
						"serverInstanceId" : serverInstanceId
					},
					success : function(data) {
						var response = eval(data);
						var responseObject = response.object;
						var instanceStatus = responseObject.status;
						var toggleId = serverInstanceId + "_" + instanceStatus;
						
						
						if (response.code == '200') {
							if (instanceStatus == 'ACTIVE') {
								$('#loader_' + serverInstanceId + ' > img').remove();
								$('#loader_' + serverInstanceId)
										.html(
												"<strong>Status</strong>&nbsp;|&nbsp; <i class='fa fa-circle orangecolor' id='status_icon' data-placement='bottom' title='ON'></i>");
								
								$("#server_stop_btn").show();
								$("#server_stop_btn").val('true');
								$("#server_start_btn").hide();
								$("#server_start_btn").val('false');								
								$("#server_soft_restart_btn").show();
								$("#server_reload_config_btn").show();
								
								//clearServiceSummaryInstanceGrid(); Throwing Error At Statistics Mgmt Screen

							} else {
								
								$('#loader_' + serverInstanceId + ' > img')
										.remove();
								$('#loader_' + serverInstanceId)
										.html(
												"<strong>Status</strong>&nbsp;|&nbsp; <i class='fa fa-circle grey' id='status_icon' data-toggle='tooltip' data-placement='bottom' title='OFF'></i>");

								$("#server_start_btn").show();
								$("#server_start_btn").val('true');
								$("#server_stop_btn").hide();
								$("#server_stop_btn").val('false');								
								$("#server_soft_restart_btn").show();
								$("#server_reload_config_btn").show();
								
							}
						} else {
							
							$('#loader_' + serverInstanceId + ' > img').remove();
							$('#loader_' + serverInstanceId)
									.html(
											"<strong>Status</strong>&nbsp;|&nbsp; <i class='fa fa-circle grey' id='status_icon' data-toggle='tooltip' data-placement='bottom' title='OFF'></i>");
							
							$("#server_start_btn").show();
							$("#server_start_btn").val('true');
							$("#server_stop_btn").hide();
							$("#server_stop_btn").val('false');								
							$("#server_soft_restart_btn").show();
							$("#server_reload_config_btn").show();
							
							//clearServiceSummaryInstanceGrid(); Throwing Error At Statistics Mgmt Screen
						}
					},
					error : function(xhr, st, err) {
						handleGenericError(xhr, st, err);
					}
				});
	}
</script>

<body class="skin-blue sidebar-mini">
	<div class="wrapper">

		<!-- Header Start -->

		<jsp:include page="../common/newtopNavigationPanel.jsp"></jsp:include>

		<!-- Header End -->
		<jsp:include page="../common/newleftMenu.jsp"></jsp:include>

		<!-- Content Wrapper. Contains page content -->
		<div class="content-wrapper">
			<!-- Content Header (Page header) -->
			<section class="content-header">
				<div class="fullwidth">
					<div style="padding: 0 10px 0 4px;">
						<div id="content-scroll-d" class="content-scroll">

							<!-- Content Wrapper. Contains page content Start -->

							<div class="fullwidth">
								<h4
									style="margin-top: 10px; margin-bottom: 0px; font-size: 11px;">
									<a href="home.html"><img src="img/tile-icon.png"
										class="vmiddle" alt="Home" /></a> <span class="spanBreadCrumb"
										style="line-height: 30px;"> <strong> <a
											href="<%=ControllerConstants.INIT_SERVER_MANAGER%>"><spring:message
													code="leftmenu.label.server.manager" ></spring:message></a>&nbsp;/
									</strong> &nbsp;&nbsp;<label id="lblInstanceName">${instanceName}</label>
										&nbsp;-&nbsp; <label id="lblInstanceHost"></label> &nbsp; :
										&nbsp; <c:choose>
											<c:when
												test="${((REQUEST_ACTION_TYPE ne 'UPDATE_SNMP_CONFIG') and (REQUEST_ACTION_TYPE ne 'UPDATE_SYSTEM_AGENT_CONFIG'))}">
												<label id="lblInstancePort">${server_instance_form_bean.port}</label>
											</c:when>
											<c:otherwise>
												<label id="lblInstancePort">${instancePort}</label>
											</c:otherwise>

										</c:choose>




									</span>
									<ol class="breadcrumb1 breadcrumb mtop10">
										<c:if test="${Boolean.TRUE.toString() ne  MapCache.getConfigValueAsObject(BaseConstants.KUBERNETES_ENV)}">
										<li><c:choose>
												<c:when
													test="${((REQUEST_ACTION_TYPE ne 'UPDATE_SNMP_CONFIG') and (REQUEST_ACTION_TYPE ne 'UPDATE_SYSTEM_AGENT_CONFIG'))}">
													<div id="loader_${server_instance_form_bean.id}">
												</c:when>
												<c:otherwise>
													<div id="loader_${serverInstanceId}">
												</c:otherwise>

											</c:choose> <strong><spring:message
													code="updtInstacne.head.status.lbl" ></spring:message></strong> &nbsp;|&nbsp; <img
											src="img/preloaders/circle-red.gif" width="15px"
											data-toggle="tooltip" data-placement="bottom"
											title="Loading Status.." height="15px"
											style="vertical-align: middle;">
							</div>
							</li></c:if>
							<li><strong><spring:message
										code="updtInstacne.head.lst.updt.time.lbl" ></spring:message></strong> <label
								id="lblInstanceUpdtTime" style="font-weight: normal;">&nbsp;|&nbsp;${lastUpdateTime}</label>
							</li>
							</ol>
							</h4>

							<div class="tab-content no-padding clearfix">
								<div class="fullwidth">
									<div class="title2">
										<%-- 					   					<spring:message code="serverManagement.grid.heading" ></spring:message> --%>
										<span class="title2rightfield"> <span
											class="title2rightfield-icon1-text"> </span>
										</span>
									</div>
								</div>
								<!-- Morris chart - Sales -->
							</div>

							<jsp:include page="../common/responseMsg.jsp" ></jsp:include>
							<div class="row">

								<div class="col-xs-12">
									<div class="box  martop"
										style="border: none; box-shadow: none;">
										<!-- /.box-header -->
										<div class="box-body table-responsive no-padding">
											<div class="nav-tabs-custom">
												<!-- Tabs within a box -->

												<ul class="nav nav-tabs pull-right">
													<sec:authorize
														access="hasAnyAuthority('VIEW_SERVER_AND_SERVER_INSTANCES')">
														<li
															class="<c:if test="${(REQUEST_ACTION_TYPE eq 'UPDATE_INSTANCE_SUMMARY')}"><c:out value="active"></c:out></c:if>"
															onclick="showButtons('instance-summary-a');submitInitForm();">
															<a href="#Summary" id="instance-summary-a"
															data-toggle="tab"><spring:message
																	code="updtInstacne.summary.tab.heading" ></spring:message></a>
														</li>
														<li
															class="<c:if test="${(REQUEST_ACTION_TYPE eq 'UPDATE_INSTANCE_ADVANCE_CONFIG')}"><c:out value="active"></c:out></c:if>"
															onclick="showButtons('advanced-config-a');submitInitForm();">
															<a href="#Advanced-Config" id="advanced-config-a"
															data-toggle="tab"><spring:message
																	code="updtInstacne.advance.config.tab.heading" ></spring:message></a>
														</li>
														<li
															class="<c:if test="${(REQUEST_ACTION_TYPE eq 'UPDATE_INSTANCE_SYSTEM_LOG')}"><c:out value="active"></c:out></c:if>"
															onclick="showButtons('system-log-a');submitInitForm();">
															<a href="#System-log" id="system-log-a" data-toggle="tab"><spring:message
																	code="updtInstacne.log.mgmt.tab.heading" ></spring:message></a>
														</li>
														<li
															class="<c:if test="${(REQUEST_ACTION_TYPE eq 'UPDATE_INSTANCE_STATISTIC')}"><c:out value="active"></c:out></c:if>"
															onclick="showButtons('statistic-a');submitInitForm();">
															<a href="#Statistic" id="statistic-a" data-toggle="tab"><spring:message
																	code="updtInstacne.statistic.mgmt.tab.heading" ></spring:message></a>
														</li>
													</sec:authorize>
													<c:if test="${isPolicyActive !=null}">
														<li onclick="redirectPolicyMgmt();"><a href="#"
															id="policy-mgmt" data-toggle="tab"><spring:message
																	code="updateServerInstance.business.policy.tab.heading" ></spring:message></a>
														</li>
													</c:if>
													<sec:authorize access="hasAnyAuthority('VIEW_SNMP_CONFIG')">
														<li
															class="<c:if test="${(REQUEST_ACTION_TYPE eq 'UPDATE_SNMP_CONFIG')}"><c:out value="active"></c:out></c:if>"
															onclick="submitSnmpRequest();"><a href="#SnmpConfig"
															id="snmp-config" data-toggle="tab"><spring:message
																	code="updtInstacne.summary.snmp.config.tab.header" ></spring:message></a></li>
													</sec:authorize>
												<%-- 	<sec:authorize
														access="hasAnyAuthority('VIEW_WEBSERVICE_DETAIL')">
														<li onclick=""><a href="#" id="web-service"
															data-toggle="tab"><spring:message
																	code="updtInstacne.summary.webservice.tab.header" ></spring:message></a></li>
													</sec:authorize>
													<sec:authorize
														access="hasAnyAuthority('VIEW_CONSOLIDATION_STATISTIC')">
														<li onclick="submitConsolidateStateForm();"><a
															href="#" id="consolidated-state" data-toggle="tab"><spring:message
																	code="updtInstacne.other.tab.consol.state.heading" ></spring:message></a></li>
													</sec:authorize> --%>


													<sec:authorize
														access="hasAnyAuthority('SYSTEM_AGENT_CONFIGURATION')">
														<li
															class="<c:if test="${(REQUEST_ACTION_TYPE eq 'UPDATE_SYSTEM_AGENT_CONFIG')}"><c:out value="active"></c:out></c:if>"
															onclick="submitSystemAgentRequest();"><a
															href="#SystemAgent" id="system-agent" data-toggle="tab"><spring:message
																	code="updtInstacne.summary.agent.start.stop.name.lbl" ></spring:message></a></li>
																											</sec:authorize>
								                   <sec:authorize
														access="hasAnyAuthority('VIEW_CONFIG_HISTORY')">
														<li
															class="<c:if test="${(REQUEST_ACTION_TYPE eq 'CONFIG_HISTORY_SUMMARY')}"><c:out value="active"></c:out></c:if>"
															onclick="showButtons('config-history-a');submitInitForm();">
															<a href="#ConfigHistory" id="config-history-a"
															data-toggle="tab"><spring:message
																	code="updtInstacne.confighistory.tab.heading" ></spring:message></a>
														</li>
												 </sec:authorize>
												</ul>
												<div class="fullwidth tab-content no-padding mtop10">
													<!-- Morris chart - Sales -->
													<sec:authorize
														access="hasAnyAuthority('VIEW_SERVER_AND_SERVER_INSTANCES')">
														<c:if
															test="${(REQUEST_ACTION_TYPE eq 'UPDATE_INSTANCE_SUMMARY')}">
															<jsp:include page="updtInstance-Summary.jsp" ></jsp:include>
														</c:if>
														<c:if
															test="${(REQUEST_ACTION_TYPE eq 'UPDATE_INSTANCE_ADVANCE_CONFIG')}">
															<jsp:include page="updtInstance-AdvanceConfig.jsp" ></jsp:include>
														</c:if>
														<c:if
															test="${(REQUEST_ACTION_TYPE eq 'UPDATE_INSTANCE_SYSTEM_LOG')}">
															<jsp:include page="updtInstance-Systemlog.jsp" ></jsp:include>
														</c:if>
														<c:if
															test="${(REQUEST_ACTION_TYPE eq 'UPDATE_INSTANCE_STATISTIC')}">
															<jsp:include page="updtInstance-StatisticMgmt.jsp" ></jsp:include>
														</c:if>
														
														<c:if
															test="${(REQUEST_ACTION_TYPE eq 'UPDATE_SNMP_CONFIG')}">
															<jsp:include page="../snmp/snmpConfigMgmt.jsp" ></jsp:include>
														</c:if>
														</sec:authorize>
														<sec:authorize
														access="hasAnyAuthority('SYSTEM_AGENT_CONFIGURATION')">
														<c:if
															test="${(REQUEST_ACTION_TYPE eq 'UPDATE_SYSTEM_AGENT_CONFIG')}">
															<jsp:include page="../systemAgent/systemAgentMgmt.jsp" ></jsp:include>
														</c:if>
														</sec:authorize>
														<sec:authorize
														access="hasAnyAuthority('VIEW_CONFIG_HISTORY')">
														<c:if
															test="${(REQUEST_ACTION_TYPE eq 'CONFIG_HISTORY_SUMMARY')}">
															<jsp:include page="versionConfigHistory.jsp" ></jsp:include>
														</c:if>
														</sec:authorize>
												</div>
											</div>
										</div>

										<!-- /.box-body -->
									</div>
									<!-- /.box -->

								</div>
							</div>
						</div>

						<!-- Content Wrapper. Contains page content End -->
					</div>
				</div>
		</div>
		</section>
	</div>
	<!-- /.content-wrapper -->

	<!-- Footer Start -->
	<footer class="main-footer positfix">
		<div class="fooinn">
			<div class="fullwidth">
				<div class="button-set">
					<div class="padleft-right" id="systemLogDiv" style="display: none;">
						<sec:authorize
							access="hasAuthority('UPDATE_SERVER_AND_SERVER_INSTANCES')">
							<button id="update_system_log_mgmt_btn" class="btn btn-grey btn-xs " type="button"
								onclick="submitSystemLogForm();">
								<spring:message code="btn.label.update" ></spring:message>
							</button>
							<button id="reset_system_log_mgmt_btn" class="btn btn-grey btn-xs" type="button"
								onclick="resetSystemLogFields();">
								<spring:message code="btn.label.reset" ></spring:message>
							</button>
						</sec:authorize>
						<button id="cancel_system_log_mgmt_btn" class="btn btn-grey btn-xs" type="button"
							onclick="cancelUpdate();">
							<spring:message code="btn.label.cancel" ></spring:message>
						</button>
					</div>
					<div class="padleft-right" id="advanceconfigDiv"
						style="display: none;">
						<sec:authorize
							access="hasAuthority('UPDATE_SERVER_AND_SERVER_INSTANCES')">
							<button id="update_advance_config_btn" class="btn btn-grey btn-xs " type="button"
								onclick="submitAdvanceConfigForm();">
								<spring:message code="btn.label.update" ></spring:message>
							</button>
							<button id="reset_advance_config_btn" class="btn btn-grey btn-xs" type="button"
								onclick="resetAdvanceConfigFields();">
								<spring:message code="btn.label.reset" ></spring:message>
							</button>
						</sec:authorize>
						<button id="cancel_advance_config_btn" class="btn btn-grey btn-xs" type="button"
							onclick="cancelUpdate();">
							<spring:message code="btn.label.cancel" ></spring:message>
						</button>
					</div>
					<div class="padleft-right" id="statisticDiv" style="display: none;">
						<sec:authorize
							access="hasAuthority('UPDATE_SERVER_AND_SERVER_INSTANCES')">
							<button id="update_static_mgmt_btn" class="btn btn-grey btn-xs " type="button"
								onclick="submitStatisticForm();">
								<spring:message code="btn.label.update" ></spring:message>
							</button>
							<button id="reset_static_mgmt_btn" class="btn btn-grey btn-xs" type="button"
								onclick="resetStatisticFields();">
								<spring:message code="btn.label.reset" ></spring:message>
							</button>
						</sec:authorize>
						<button id="cancel_static_mgmt_btn" class="btn btn-grey btn-xs" type="button"
							onclick="cancelUpdate();">
							<spring:message code="btn.label.cancel" ></spring:message>
						</button>
					</div>
					
					<div class="padleft-right" id="configHistoryDiv" style="display: none;">
					</div>
					
					<div class="padleft-right" id="summaryDiv" style="display: none;">
						<sec:authorize access="hasAuthority('SERVER_INSTANCE_SYNCHRONIZATION')">
							<button id="server_sync_btn" class="btn btn-grey btn-xs " type="button"
								onclick="synchInstanceById();">
								<spring:message code="btn.label.synchronize" ></spring:message>
							</button>
						</sec:authorize>
						<sec:authorize access="hasAuthority('SERVER_INSTANCE_EXPORT_CONFIG')">
							<button id="server_export_btn" class="btn btn-grey btn-xs" type="button"
								onclick="exportConfigPopup();">
								<spring:message code="btn.label.export.config" ></spring:message>
							</button>
						</sec:authorize>
						<sec:authorize access="hasAuthority('SERVER_INSTANCE_IMPORT_CONFIG')">
							<button id="server_import_btn" class="btn btn-grey btn-xs" type="button"
								onclick="importConfigPopup();">
								<spring:message code="btn.label.import.config" ></spring:message>
							</button>
						</sec:authorize>
						<sec:authorize access="hasAuthority('SERVER_INSTANCE_SOFT_RESTART')">
							<button id="server_soft_restart_btn" class="btn btn-grey btn-xs" type="button" style="display: none;"
								onclick="softRestartPopup();">
								<spring:message code="btn.label.soft.restart" ></spring:message>
							</button>
						</sec:authorize>
						<sec:authorize access="hasAuthority('SYNC_PUBLISH_VERSION')">
							<button id="server_sync_publish_btn" class="btn btn-grey btn-xs" type="button"
								onclick="syncPublishPopup();">
								<spring:message code="btn.label.sync.publish" ></spring:message>
							</button>
						</sec:authorize>
						<sec:authorize access="hasAuthority('SERVER_INSTANCE_RELOAD_CONFIG')">
							<button id="server_reload_config_btn" class="btn btn-grey btn-xs" type="button" style="display: none;"
								onclick="reloadConfigPopup();">
								<spring:message code="btn.label.reload.config" ></spring:message>
							</button>
						</sec:authorize>
						<!--                     	To do for access right -->
						<c:if test="${isReloadCache !=null}">
							<sec:authorize access="hasAuthority('SERVER_INSTANCE_RELOAD_CACHE')">
								<button id="server_reload_cache_btn" class="btn btn-grey btn-xs" type="button"
									onclick="reloadCachePopup();">
									<spring:message code="btn.label.reload.cache" ></spring:message>
								</button>
							</sec:authorize>
						</c:if>
						<c:if test="${Boolean.TRUE.toString() ne  MapCache.getConfigValueAsObject(BaseConstants.KUBERNETES_ENV)}">
						<sec:authorize access="hasAuthority('SERVER_INSTANCE_STOP')">
							<button id="server_stop_btn" class="btn btn-grey btn-xs" type="button"
								style="display: none;" onclick="shutdownPopup();" value="">
								<spring:message code="btn.label.shutdown" ></spring:message>
							</button>
						</sec:authorize>
						
						<sec:authorize access="hasAuthority('SERVER_INSTANCE_START')">
							<button id="server_start_btn" class="btn btn-grey btn-xs" type="button"
								style="display: none;" onclick="startPopup();" value="">
								<spring:message code="btn.label.start" ></spring:message>
							</button>
						</sec:authorize>
						</c:if>
					</div>
				</div>
			</div>
			<jsp:include page="../common/newfooter.jsp"></jsp:include>
		</div>
	</footer>
	<!-- Footer End -->

	</div>
	<!-- ./wrapper -->

	<form id="consolidate-state-form" method="POST"
		action="<%=ControllerConstants.INIT_CONSOLIDATION_MANAGER%>">
		<input type="hidden" id="consol-state-instanceName"
			name="consol-state-instanceName" /> <input type="hidden"
			id="consol-state-instanceHost" name="consol-state-instanceHost" /> <input
			type="hidden" id="consol-state-instancePort"
			name="consol-state-instancePort" /> <input type="hidden"
			id="consol-state-instanceId" name="consol-state-instanceId" />
	</form>

	<form id="business-policy-form" method="POST" style="display: none;"
		action="<%=ControllerConstants.INIT_BUSINESS_POLICY_MGMT%>">
		<input type="hidden" id="server-instance-name" name="server-instance-name" /> 
		<input type="hidden" id="server-instance-host" name="server-instance-host" /> 
		<input type="hidden" id="server-instance-port" name="server-instance-port" /> 
		<input type="hidden" id="server-instance-id" name="server-instance-id" />
		<input type="hidden" id="serviceInstanceId" name="serviceInstanceId" value="${serviceInstanceId}">
			<input type="hidden" id="serviceId" name="serviceId" value="${serviceId}">
			<input type="hidden" id="serviceType" name="serviceType" value="<%=BaseConstants.PROCESSING_SERVICE%>">
			<input type="hidden" id="serviceName" name="serviceName" value="${serviceName}">
		
		<input type="hidden" id = "request-action-type" name="<%=BaseConstants.REQUEST_ACTION_TYPE%>" value="<%=BaseConstants.POLICY %>">
	</form>


	<div style="display: none;">
		<a href="<%=ControllerConstants.INIT_SERVER_MANAGER%>" id="cancelUpdateBtn"></a>
	</div>


	<a href="#divReloadCache" class="fancybox" style="display: none;"
		id="reloadcache">#</a>
	<jsp:include page="divReloadCache.jsp"></jsp:include>

	<a href="#divSoftRestart" class="fancybox" style="display: none;"
		id="softrestart">#</a>
	<jsp:include page="divSoftRestart.jsp"></jsp:include>
	
	<a href="#divSyncPublish" class="fancybox" style="display: none;"
		id="syncPublish">#</a>
	<jsp:include page="divSyncPublish.jsp"></jsp:include>

	<a href="#divReloadConfig" class="fancybox" style="display: none;"
		id="reloadconfig">#</a>
	<jsp:include page="divReloadConfig.jsp"></jsp:include>

	<a href="#divSynchronize" class="fancybox" style="display: none;"
		id="synchronize">#</a>
	<div id="divSynchronize" style="width: 100%; display: none;">
		<div class="modal-content">
			<div class="modal-header padding10">
				<h4 class="modal-title">
					<spring:message code="serverMgmt.synchronize.popup.header" ></spring:message>
				</h4>
			</div>
			<div class="modal-body padding10 inline-form">
				<div id="divSyncSIMsg"></div>
				<p>
					<input type='hidden' id="syncInstanceId" />
				<table style='width: 100%'>
					<tr>
						<td width="15%"><img alt="Server Instance"
							src="img/server.png" class="img-responsive" /></td>
						<td width="85%">
							<div id="divInstanceList"></div>
						</td>
					</tr>
				</table>
				</p>
				<div id=synchronizePopUpMsgDiv">
				<p id="synchronizePopUpMsg" class="synchronizePopUpMsgClass">
					<span class="note"><spring:message
							code="label.important.note" ></spring:message></span><br /> <br />
					<spring:message code="serverMgmt.synchronize.popup.note.content" ></spring:message>
				</p>
				</div>
			</div>
			<div id="synch-buttons-div" class="modal-footer padding10">
				<button type="button" class="btn btn-grey btn-xs" id="btnSynchPopup"
					onclick="synchronizesInstance();">
					<spring:message code="btn.label.synchronize" ></spring:message>
				</button>
				<button type="button" class="btn btn-grey btn-xs "
					id="btnSynchCancel" data-dismiss="modal" onclick="closeFancyBox();">
					<spring:message code="btn.label.cancel" ></spring:message>
				</button>
				<button type="button" class="btn btn-grey btn-xs "
					id="btnSynchClose" data-dismiss="modal"
					onclick="closeFancyBox();submitInitForm();" style="display: none;">
					<spring:message code="btn.label.close" ></spring:message>
				</button>
			</div>
			<div id="synch-progress-bar-div"
				class="modal-footer padding10 text-left" style="display: none;">
				<jsp:include page="../common/processing-bar.jsp"></jsp:include>
			</div>
			
		</div>
		<!-- /.modal-content -->
	</div>

	<div id="divExportConfig" style="width: 100%; display: none;">
		<div class="modal-content">
			<form method="POST"
				action="<%=ControllerConstants.EXPORT_SERVER_INSTANCE_CONFIG%>"
				id="export-config-form">
				<input type="hidden" id="exportInstancesId" name="exportInstancesId" />
				<input type="hidden" id="isExportForDelete" name="isExportForDelete"
					value="false" /> <input type="hidden" id="exportPath"
					name="exportPath" /> <input type="hidden"
					id="<%=BaseConstants.REQUEST_ACTION_TYPE%>"
					name="<%=BaseConstants.REQUEST_ACTION_TYPE%>"
					value="<%=BaseConstants.UPDATE_INSTANCE_SUMMARY%>" />
			</form>
		</div>
		<!-- /.modal-content -->
	</div>

	<a href="#divImportConfig" class="fancybox" style="display: none;"
		id="importconfig">#</a>
	<div id="divImportConfig" style="width: 100%; display: none;">

		<jsp:include page="ImportServerInstancePopUp.jsp"></jsp:include>

	</div>

	<a href="#divShutdown" class="fancybox" style="display: none;"
		id="shutdown">#</a>
	<div id="divShutdown" style="width: 100%; display: none;">
		<div class="modal-content">
			<div class="modal-header padding10">
				<h4 class="modal-title">
					<spring:message code="updtInstacne.summary.shutdown.popup.header" ></spring:message>
				</h4>
			</div>
			<div class="modal-body padding10 inline-form">
			<jsp:include page="../common/responseMsgPopUp.jsp" ></jsp:include>
				<p>
					<spring:message code="updtInstacne.summary.shutdown.popup.text" ></spring:message>
				</p>
			</div>
			<div id="shutdown-buttons-div" class="modal-footer padding10">
				<button id="restartbtn" type="button" class="btn btn-grey btn-xs "
					onclick="restartInstance();">
					<spring:message code="btn.label.restart" ></spring:message>
				</button>
				<button id="stopbtn" type="button" class="btn btn-grey btn-xs "
					data-dismiss="modal" onclick="stopInstance();">
					<spring:message code="btn.label.shutdown" ></spring:message>
				</button>
				<button id="cancelbtn" type="button" class="btn btn-grey btn-xs "
					data-dismiss="modal" onclick="closeFancyBox();">
					<spring:message code="btn.label.cancel" ></spring:message>
				</button>
			</div>
			<div id="shutdown-progress-bar-div"
				class="modal-footer padding10 text-left" style="display: none;">
				<jsp:include page="../common/processing-bar.jsp"></jsp:include>
			</div>
		</div>
		<!-- /.modal-content -->
	</div>

	<a href="#divSNMPStatus" class="fancybox" style="display: none;"
		id="a-snmp">#</a>
	<div id="divSNMPStatus" style="width: 100%; display: none;">
		<div class="modal-content">
			<div class="modal-header padding10">
				<h4 class="modal-title">
					<spring:message code="updtInstacne.summary.snmp.popup.header" ></spring:message>
				</h4>
			</div>
			<div class="modal-body padding10 inline-form">
				<div class="form-group">
					<div class="table-cell-label">
						<spring:message
							code="updtInstacne.summary.snmp.popup.chng.sts.lbl" ></spring:message>
					</div>
					<div class="input-group ">
						<input id="inst-snmp-sts" type='checkbox' class="checkboxbg" />
					</div>
				</div>
				<p>
					<spring:message
						code="updtInstacne.summary.snmp.popup.chng.sts.note" ></spring:message>
				</p>
				<p>
					<i class="icon-lightbulb icon-large"></i><span class="note"><spring:message
							code="label.important.note" ></spring:message></span><br />
				</p>
				<p>
					<spring:message code="updtInstacne.summary.snmp.popup.note" ></spring:message>
				</p>
			</div>
			<div id="snmp-buttons-div" class="modal-footer padding10">
				<button type="button" id="snmp_yes_btn" class="btn btn-grey btn-xs "
					onclick="updtSNMPAlert();">
					<spring:message code="btn.label.yes" ></spring:message>
				</button>
				<button type="button" id="snmp_no_btn" class="btn btn-grey btn-xs "
					data-dismiss="modal"
					onclick="closeFancyBox();resetSNMPAlertToggle();">
					<spring:message code="btn.label.no" ></spring:message>
				</button>
			</div>
			<div id="snmp-progress-bar-div"
				class="modal-footer padding10 text-left" style="display: none;">
				<jsp:include page="../common/processing-bar.jsp"></jsp:include>
			</div>
		</div>
		<!-- /.modal-content -->
	</div>

	<a href="#divWebServiceStatus" class="fancybox" style="display: none;"
		id="a-webservice">#</a>
	<div id="divWebServiceStatus" style="width: 100%; display: none;">
		<div class="modal-content">
			<div class="modal-header padding10">
				<h4 class="modal-title">
					<spring:message code="updtInstacne.summary.webservice.popup.header" ></spring:message>
				</h4>
			</div>
			<div class="modal-body padding10 inline-form">
				<div class="form-group">
					<div class="table-cell-label">
						<spring:message
							code="updtInstacne.summary.webservice.popup.chng.sts.lbl" ></spring:message>
					</div>
					<div class="input-group ">
						<input id="inst-webservice-sts" type='checkbox' class="" />
					</div>
				</div>
				<p>
					<spring:message
						code="updtInstacne.summary.webservice.popup.chng.sts.note" ></spring:message>
				</p>
				<p>
					<i class="icon-lightbulb icon-large"></i><span class="note"><spring:message
							code="label.important.note" ></spring:message> </span><br />
				</p>
				<p>
					<spring:message code="label.important.note" ></spring:message>
				</p>
			</div>
			<div id="webservice-buttons-div" class="modal-footer padding10">
				<button type="button" class="btn btn-grey btn-xs "
					onclick="updtWebServiceState();">
					<spring:message code="btn.label.yes" ></spring:message>
				</button>
				<button type="button" class="btn btn-grey btn-xs "
					data-dismiss="modal"
					onclick="closeFancyBox();resetWebserviceToggle();">
					<spring:message code="btn.label.no" ></spring:message>
				</button>
			</div>
			<div id="webservice-progress-bar-div"
				class="modal-footer padding10 text-left" style="display: none;">
				<jsp:include page="../common/processing-bar.jsp"></jsp:include>
			</div>
		</div>
		<!-- /.modal-content -->
	</div>
	
	<a href="#divRestWebServiceStatus" class="fancybox" style="display: none;"
		id="a-restwebservice">#</a>
	<div id="divRestWebServiceStatus" style="width: 100%; display: none;">
		<div class="modal-content">
			<div class="modal-header padding10">
				<h4 class="modal-title">
					<spring:message code="updtInstacne.summary.restwebservice.popup.header" ></spring:message>
				</h4>
			</div>
			<div class="modal-body padding10 inline-form">
				<div class="form-group">
					<div class="table-cell-label">
						<spring:message
							code="updtInstacne.summary.restwebservice.popup.chng.sts.lbl" ></spring:message>
					</div>
					<div class="input-group ">
						<input id="inst-restwebservice-sts" type='checkbox' class="" />
					</div>
				</div>
				<p>
					<spring:message
						code="updtInstacne.summary.webservice.popup.chng.sts.note" ></spring:message>
				</p>
				<p>
					<i class="icon-lightbulb icon-large"></i><span class="note"><spring:message
							code="label.important.note" ></spring:message> </span><br />
				</p>
				<p>
					<spring:message code="label.important.note" ></spring:message>
				</p>
			</div>
			<div id="restwebservice-buttons-div" class="modal-footer padding10">
				<button type="button" class="btn btn-grey btn-xs "
					onclick="updtRestWebServiceState();">
					<spring:message code="btn.label.yes" ></spring:message>
				</button>
				<button type="button" class="btn btn-grey btn-xs "
					data-dismiss="modal"
					onclick="closeFancyBox();resetRestWebserviceToggle();">
					<spring:message code="btn.label.no" ></spring:message>
				</button>
			</div>
			<div id="restwebservice-progress-bar-div"
				class="modal-footer padding10 text-left" style="display: none;">
				<jsp:include page="../common/processing-bar.jsp"></jsp:include>
			</div>
		</div>
		<!-- /.modal-content -->
	</div>

	<a href="#divStopService" class="fancybox" style="display: none;"
		id="stopService">#</a>
	<a href="#divStartService" class="fancybox" style="display: none;"
		id="startService">#</a>
	<div id="divStartService" style="width: 100%; display: none;">
		<jsp:include page="../service/startServicePopUp.jsp"></jsp:include>
	</div>
	<div id="divStopService" style="width: 100%; display: none;">
		<jsp:include page="../service/stopServicePopUp.jsp"></jsp:include>
	</div>
	<a href="#divSyncService" class="fancybox" style="display: none;"
		id="syncServiceInstance">#</a>
	<div id="divSyncService" style="width: 100%; display: none;">
		<jsp:include page="../service/synchronizationPopUp.jsp"></jsp:include>
	</div>

	<a href="#divStartAgent" class="fancybox" style="display: none;"
		id="start-agent">#</a>
	<div id="divStartAgent" style="width: 100%; display: none;">
		<div class="modal-content">
			<div class="modal-header padding10">
				<h4 class="modal-title">
					<spring:message code="agent.start.popup.title" ></spring:message>
				</h4>
			</div>
			<div class="modal-body padding10 inline-form">
				<input type="hidden" id="startAgentId" /> <input type="hidden"
					id="startAgentStatus" />
				<p>
					<spring:message code="agent.start.popup.note" ></spring:message>
				</p>
			</div>
			<div id="agent-start-buttons-div" class="modal-footer padding10">

				<button type="button" class="btn btn-grey btn-xs "
					onclick="changeAgentStatus('Start');">
					<spring:message code="btn.label.yes" ></spring:message>
				</button>
				<button type="button" class="btn btn-grey btn-xs "
					onclick="setDefaultAgentStatus('Start');closeFancyBox();">
					<spring:message code="btn.label.no" ></spring:message>
				</button>

			</div>
			<div id="agent-start-progress-bar-div"
				class="modal-footer padding10 text-left" style="display: none;">
				<jsp:include page="../common/processing-bar.jsp"></jsp:include>
			</div>

		</div>
		<!-- /.modal-content -->
	</div>

	<a href="#divStopAgent" class="fancybox" style="display: none;"
		id="stop-agent">#</a>
	<div id="divStopAgent" style="width: 100%; display: none;">
		<div class="modal-content">
			<div class="modal-header padding10">
				<h4 class="modal-title">
					<spring:message code="agent.stop.popup.title" ></spring:message>
				</h4>
			</div>
			<div class="modal-body padding10 inline-form">
				<input type="hidden" id="stopAgentId" /> <input type="hidden"
					id="stopAgentStatus" />
				<p>
					<spring:message code="agent.stop.popup.note" ></spring:message>
				</p>
			</div>
			<div id="agent-stop-buttons-div" class="modal-footer padding10">

				<button type="button" class="btn btn-grey btn-xs "
					onclick="changeAgentStatus('Stop');">
					<spring:message code="btn.label.yes" ></spring:message>
				</button>
				<button type="button" class="btn btn-grey btn-xs "
					onclick="setDefaultAgentStatus('Stop');closeFancyBox();">
					<spring:message code="btn.label.no" ></spring:message>
				</button>

			</div>
			<div id="agent-stop-progress-bar-div"
				class="modal-footer padding10 text-left" style="display: none;">
				<jsp:include page="../common/processing-bar.jsp"></jsp:include>
			</div>

		</div>
		<!-- /.modal-content -->
	</div>


	<a href="#divStart" class="fancybox" style="display: none;" id="start">#</a>
	<jsp:include page="divStart.jsp"></jsp:include>



	<form method="POST"
		action="<%=ControllerConstants.INIT_UPDATE_SERVER_INSTANCE%>"
		id="init-update-form">
		<c:choose>
			<c:when
				test="${((REQUEST_ACTION_TYPE ne 'UPDATE_SNMP_CONFIG') and (REQUEST_ACTION_TYPE ne 'UPDATE_SYSTEM_AGENT_CONFIG'))}">
				<input type="hidden" id="serverInstanceId" name="serverInstanceId"
					value='${server_instance_form_bean.id}' />

			</c:when>
			<c:otherwise>
				<input type="hidden" id="serverInstanceId" name="serverInstanceId"
					value='${serverInstanceId}' />
				<input type="hidden" id="serverInstanceIp" name="serverInstanceIp"
					value='${serverInstanceIp}' />
			</c:otherwise>

		</c:choose>

		<input type="hidden" id="<%=BaseConstants.REQUEST_ACTION_TYPE%>"
			name="<%=BaseConstants.REQUEST_ACTION_TYPE%>" />
	</form>



	<form method="POST" action="<%=ControllerConstants.INIT_SNMP_CONFIG%>"
		id="init-update-snmp">
		<input type="hidden" id="snmpserverInstanceId"
			name="snmpserverInstanceId" /> <input type="hidden"
			id="<%=BaseConstants.REQUEST_ACTION_TYPE%>"
			name="<%=BaseConstants.REQUEST_ACTION_TYPE%>"
			value="<%=BaseConstants.UPDATE_SNMP_CONFIG%>" />
	</form>

	<form method="POST"
		action="<%=ControllerConstants.INIT_SYSTEM_AGENT_CONFIG%>"
		id="init-update-agent">
		<input type="hidden" id="agent_serverInstanceId"
			name="agent_serverInstanceId" /> <input type="hidden"
			id="<%=BaseConstants.REQUEST_ACTION_TYPE%>"
			name="<%=BaseConstants.REQUEST_ACTION_TYPE%>"
			value="<%=BaseConstants.UPDATE_SYSTEM_AGENT_CONFIG%>" />
	</form>

	<!-- Delete Service Pop-up code start here-->
	<a href="#divDeleteService" class="fancybox" style="display: none;"
		id="deleteServicePopup">#</a>
	<a href="#divMessage" class="fancybox" style="display: none;"
		id="deleteWarnMsg">#</a>
	<div id="divDeleteService" style="width: 100%; display: none;">
		<jsp:include page="../service/deleteServicePopUp.jsp"></jsp:include>
	</div>

	<div id="divMessage" style="width: 100%; display: none;">
		<div class="modal-content">
			<div class="modal-header padding10">
				<h4 class="modal-title">
					<spring:message code="serverManagement.warn.popup.header" ></spring:message>
				</h4>
			</div>
			<div class="modal-body padding10 inline-form">
				<p id="moreWarn">
					<spring:message code="serviceMgmt.more.instance.checked.warning" ></spring:message>
				</p>
				<p id="lessWarn">
					<spring:message code="serviceMgmt.no.instance.checked.warning" ></spring:message>
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
	<!-- Delete Service Pop-up code end here-->
</body>


</html>
