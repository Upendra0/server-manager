<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<%@page import="com.elitecore.sm.common.constants.BaseConstants"%>
<%@page import="com.elitecore.sm.common.constants.EngineConstants"%>


<!DOCTYPE html>

<html>
<jsp:include page="../common/newheader.jsp"></jsp:include>
<script src="${pageContext.request.contextPath}/customJS/policyManager.js<%= "?v=" + Math.random() %>" type="text/javascript"></script>
<body class="skin-blue sidebar-mini sidebar-collapse">
	
	<!-- Main div start here -->
	<div>
		<jsp:include page="../common/newtopNavigationPanel.jsp"></jsp:include>
		<jsp:include page="../common/newleftMenu.jsp"></jsp:include>

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
                            		<strong>
                            			<c:choose>
                            				<c:when test="${not empty serviceId}">
                            					<a href="<%= ControllerConstants.INIT_SERVICE_MANAGER%>">
                            						<spring:message code="collectionService.leftmenu.home" ></spring:message>
                            					</a> &nbsp; / &nbsp;
                            					<a href="#" onclick="redirectServiceSummary();">
                            						${serviceName}-${serviceInstanceId}
                            					</a>
                            				</c:when>
                            				<c:otherwise>
                            					<a href="<%= ControllerConstants.INIT_SERVER_MANAGER%>">
                            						<spring:message code="leftmenu.label.server.manager" ></spring:message>
                            					</a> &nbsp; / &nbsp;
                            					<a href="#" onclick="redirectServerInstanceSummary();">
                            						${instanceName} &nbsp; : &nbsp; ${host} &nbsp; - &nbsp; ${port}
                            					</a>
                            				</c:otherwise>
                            			</c:choose>
                            			&nbsp; / &nbsp;
                            			 <spring:message code="business.policy.mgmt.tab.heading" ></spring:message>
                            			
                            		</strong>
                            	</span>
									<ol class="breadcrumb1 breadcrumb mtop10">
           								 <li><strong><spring:message code="updtInstacne.head.status.lbl" ></spring:message></strong></li>
           								 <li><a href="#" data-toggle="tooltip" data-placement="bottom" title="Active" >&nbsp;<i class="fa fa-circle orangecolor"></i></a></li>
            							 <li><b><spring:message code="updtInstacne.head.lst.updt.time.lbl" ></spring:message></b></li>
            							 <li>10/20/2015 13:15:13</li>
          							</ol>
								</h4>
								
								<jsp:include page="../common/responseMsg.jsp" ></jsp:include>	
								
								<!-- Tab code start here  -->
								<div class="row">
									<div class="col-xs-12">
										<div class="box  martop" style="border: none; box-shadow: none;">
											<div class="box-body table-responsive no-padding">
												<div class="nav-tabs-custom tab-content">
												
													<!-- Change Role here -->
													<sec:authorize access="hasAnyAuthority('VIEW_POLICY_CONFIGURATION')">  
														<ul class="nav nav-tabs pull-right">
																<li	class="<c:if test="${(REQUEST_ACTION_TYPE eq 'POLICY')}">
																	<c:out value="active"></c:out></c:if>" onclick="loadPolicyPage();">
																	<a href="#policy-list" id="policy"	data-toggle="tab" aria-expanded="true"> 
																		<spring:message	code="business.policy.tab.heading" ></spring:message>
																	</a>
																</li>
																<li	class="<c:if test="${(REQUEST_ACTION_TYPE eq 'RULE_GROUP')}">
																	<c:out value="active"></c:out></c:if>" onclick="loadPolicyRuleGroupPage();">
																	<a href="#rule-group-list" id="rule_group"	data-toggle="tab" aria-expanded="true"> 
																		<spring:message	code="business.policy.ruleGroup.tab.heading" ></spring:message>
																	</a>
																</li>
																<li class="<c:if test="${(REQUEST_ACTION_TYPE eq 'POLICY_RULE')}">
																	<c:out value="active"></c:out></c:if>" onclick="loadPolicyRulePage();">
																	<a href="#policy-rule-list" id="policy_rule" data-toggle="tab" aria-expanded="false"> 
																		<spring:message	code="business.policy.rule.tab.heading" ></spring:message>
																	</a>
																</li>
																<li class="<c:if test="${(REQUEST_ACTION_TYPE eq 'POLICY_RULE_CONDITION')}">
																	<c:out value="active"></c:out></c:if>" onclick="loadPolicyConditionPage();">
																	<a href="#policy-rule-condition-list" id="policy_rule_condition" data-toggle="tab" aria-expanded="false"> 
																	<spring:message	code="business.policy.rule.condition.tab.heading" ></spring:message>
																</a></li>
																<li class="<c:if test="${(REQUEST_ACTION_TYPE eq 'POLICY_RULE_ACTION')}">
																	<c:out value="active"></c:out></c:if>" onclick="loadPolicyActionPage();">
																	<a href="#policy-rule-action-list" id="policy_rule_action" data-toggle="tab" aria-expanded="false"> 
																	<spring:message	code="business.policy.rule.action.tab.heading" ></spring:message>
																</a></li>
																<li	class="<c:if test="${(REQUEST_ACTION_TYPE eq 'DATABASE_QUERIES')}">
																	<c:out value="active"></c:out></c:if>" onclick="loadDatabaseConfigPage();">
																	<a href="#database-queries-list" id="database_queries"	data-toggle="tab" aria-expanded="true"> 
																		<spring:message	code="business.policy.databaseQueries.tab.heading" ></spring:message>
																	</a>
																</li>
														</ul>
														<div class="fullwidth no-padding tab-content">
															<div id="policy-rule-condition-list"
																class="tab-pane <c:if test="${(REQUEST_ACTION_TYPE eq 'POLICY_RULE_CONDITION')}"><c:out value="active"></c:out></c:if> ">
														 		<c:if test="${(REQUEST_ACTION_TYPE eq 'POLICY_RULE_CONDITION')}">
															 		<jsp:include page="policyConditionList.jsp"></jsp:include> 
														 		</c:if>
															</div>
															<div id="policy-rule-action-list"
																class="tab-pane <c:if test="${(REQUEST_ACTION_TYPE eq 'POLICY_RULE_ACTION')}"><c:out value="active"></c:out></c:if> ">
																<c:if test="${(REQUEST_ACTION_TYPE eq 'POLICY_RULE_ACTION')}">
															 		<jsp:include page="policyActionList.jsp"></jsp:include> 
														 		</c:if>
															</div>
															<div id="policy-rule-list"
																class="tab-pane <c:if test="${(REQUEST_ACTION_TYPE eq 'POLICY_RULE')}"><c:out value="active"></c:out></c:if> ">
														 		<c:if test="${(REQUEST_ACTION_TYPE eq 'POLICY_RULE')}">
															 		<jsp:include page="policyRuleList.jsp"></jsp:include> 
														 		</c:if>
															</div>
															<div id="rule-group-list"
																class="tab-pane  <c:if test="${(REQUEST_ACTION_TYPE eq 'RULE_GROUP')}"><c:out value="active"></c:out></c:if>">
																<c:if test="${(REQUEST_ACTION_TYPE eq 'RULE_GROUP')}">
															 		<jsp:include page="ruleGroupList.jsp"></jsp:include> 
														 		</c:if>
															</div>
															<div id="policy-list"
																class="tab-pane  <c:if test="${(REQUEST_ACTION_TYPE eq 'POLICY')}"><c:out value="active"></c:out></c:if>">
																<c:if test="${(REQUEST_ACTION_TYPE eq 'POLICY')}">
															 		<jsp:include page="policyList.jsp"></jsp:include> 
														 		</c:if>
															</div>
															<div id="database-queries-list"
																class="tab-pane  <c:if test="${(REQUEST_ACTION_TYPE eq 'DATABASE_QUERIES')}"><c:out value="active"></c:out></c:if>">
																<c:if test="${(REQUEST_ACTION_TYPE eq 'DATABASE_QUERIES')}">
																	<jsp:include page="databaseQueriesList.jsp"></jsp:include>
																</c:if>
															</div>
														</div>
													</sec:authorize>
													<!-- Change Role here -->
												</div>
											</div>
										</div>
									</div>
								</div>
								<!-- Tab code end here -->
								<div style="display: none;">
									<form id="instance_form" method="POST" action="<%= ControllerConstants.INIT_UPDATE_SERVER_INSTANCE %>">
									<input type="hidden" id="serverInstanceId" name="serverInstanceId" value='${instanceId}'>
									</form>
								</div>
								<div style="display: none;">
									<form id="server_instance_detail_form" method="GET" >
										<input type="hidden" id="server-instance-name" name="server-instance-name" value='${instanceName}' /> 
										<input type="hidden" id="server-instance-host" name="server-instance-host" value='${host}' /> 
										<input type="hidden" id="server-instance-port" name="server-instance-port" value='${port}' /> 
										<input type="hidden" id="server-instance-id" name="server-instance-id" value='${instanceId}' />
									</form>
								</div>
								<div style="display: none;">
									<form id="service_detail_form" method="GET" action="<%= ControllerConstants.INIT_UPDATE_SERVICE %>">
										<input type="hidden" id="serviceId" name="serviceId" value="${serviceId}">
										<input type="hidden" id="serviceType" name="serviceType" value="<%=EngineConstants.PROCESSING_SERVICE%>">
										<input type="hidden" id="serviceName" name="serviceName" value="${serviceName}">
										<input type="hidden" id="serviceInstanceId" name="serviceInstanceId" value="${serviceInstanceId}">
										<input type="hidden" id="instanceId" name="instanceId" value="${instanceId}">
										<input type="hidden" id="server-instance-id" name="server-instance-id" value="${instanceId}">
									</form>
								</div>
							</div>
						</div>
					</div>
				</div>

			</section>

		</div>
		
		<div style="display: hidden">
			<form id="loadPolicyConditionForm" action ="<%= ControllerConstants.INIT_BUSINESS_POLICY_MGMT %>" method="POST">
				<input type="hidden" id="server-instance-name" name="server-instance-name" value='${instanceName}' /> 
				<input type="hidden" id="server-instance-host" name="server-instance-host" value='${host}' /> 
				<input type="hidden" id="server-instance-port" name="server-instance-port" value='${port}' /> 
				<input type="hidden" id="server-instance-id" name="server-instance-id" value='${instanceId}' />
				<input type="hidden" name="REQUEST_ACTION_TYPE"  id="REQUEST_ACTION_TYPE" value="<%=BaseConstants.POLICY_RULE_CONDITION %>" />
				<input type="hidden" id="serviceInstanceId" name="serviceInstanceId" value="${serviceInstanceId}">
			<input type="hidden" id="serviceId" name="serviceId" value="${serviceId}">
			<input type="hidden" id="serviceType" name="serviceType" value="<%=BaseConstants.PROCESSING_SERVICE%>">
			<input type="hidden" id="serviceName" name="serviceName" value="${serviceName}">
				
			</form>
		</div>
		
		<div style="display: hidden">
			<form id="loadPolicyActionForm" action ="<%= ControllerConstants.INIT_BUSINESS_POLICY_MGMT %>" method="POST">
				<input type="hidden" id="server-instance-name" name="server-instance-name" value='${instanceName}' /> 
				<input type="hidden" id="server-instance-host" name="server-instance-host" value='${host}' /> 
				<input type="hidden" id="server-instance-port" name="server-instance-port" value='${port}' /> 
				<input type="hidden" id="server-instance-id" name="server-instance-id" value='${instanceId}' />
				<input type="hidden" name="REQUEST_ACTION_TYPE"  id="REQUEST_ACTION_TYPE" value="<%=BaseConstants.POLICY_RULE_ACTION %>"/>
				<input type="hidden" id="serviceInstanceId" name="serviceInstanceId" value="${serviceInstanceId}">
			<input type="hidden" id="serviceId" name="serviceId" value="${serviceId}">
			<input type="hidden" id="serviceType" name="serviceType" value="<%=BaseConstants.PROCESSING_SERVICE%>">
			<input type="hidden" id="serviceName" name="serviceName" value="${serviceName}">
				
			</form>
		</div>
		
		<div style="display: hidden">
			<form id="loadPolicyRuleForm" action ="<%= ControllerConstants.INIT_BUSINESS_POLICY_MGMT %>" method="POST">
				<input type="hidden" id="server-instance-name" name="server-instance-name" value='${instanceName}' /> 
				<input type="hidden" id="server-instance-host" name="server-instance-host" value='${host}' /> 
				<input type="hidden" id="server-instance-port" name="server-instance-port" value='${port}' /> 
				<input type="hidden" id="server-instance-id" name="server-instance-id" value='${instanceId}' />
				<input type="hidden" name="REQUEST_ACTION_TYPE"  id="REQUEST_ACTION_TYPE" value="<%=BaseConstants.POLICY_RULE %>" />
				<input type="hidden" id="serviceInstanceId" name="serviceInstanceId" value="${serviceInstanceId}">
			<input type="hidden" id="serviceId" name="serviceId" value="${serviceId}">
			<input type="hidden" id="serviceType" name="serviceType" value="<%=BaseConstants.PROCESSING_SERVICE%>">
			<input type="hidden" id="serviceName" name="serviceName" value="${serviceName}">
				
			</form>
		</div>
		
		<div style="display: hidden">
			<form id="loadPolicyRuleGroupForm" action ="<%= ControllerConstants.INIT_BUSINESS_POLICY_MGMT %>" method="POST">
				<input type="hidden" id="server-instance-name" name="server-instance-name" value='${instanceName}' /> 
				<input type="hidden" id="server-instance-host" name="server-instance-host" value='${host}' /> 
				<input type="hidden" id="server-instance-port" name="server-instance-port" value='${port}' /> 
				<input type="hidden" id="server-instance-id" name="server-instance-id" value='${instanceId}' />
				<input type="hidden" name="REQUEST_ACTION_TYPE"  id="REQUEST_ACTION_TYPE" value="<%=BaseConstants.RULE_GROUP %>" />
				<input type="hidden" id="serviceInstanceId" name="serviceInstanceId" value="${serviceInstanceId}">
			<input type="hidden" id="serviceId" name="serviceId" value="${serviceId}">
			<input type="hidden" id="serviceType" name="serviceType" value="<%=BaseConstants.PROCESSING_SERVICE%>">
			<input type="hidden" id="serviceName" name="serviceName" value="${serviceName}">
				
			</form>
		</div>
		
		<div style="display: hidden">
			<form id="loadPolicyForm" action ="<%= ControllerConstants.INIT_BUSINESS_POLICY_MGMT %>" method="POST">
				<input type="hidden" id="server-instance-name" name="server-instance-name" value='${instanceName}' /> 
				<input type="hidden" id="server-instance-host" name="server-instance-host" value='${host}' /> 
				<input type="hidden" id="server-instance-port" name="server-instance-port" value='${port}' /> 
				<input type="hidden" id="server-instance-id" name="server-instance-id" value='${instanceId}' />
				<input type="hidden" name="REQUEST_ACTION_TYPE"  id="REQUEST_ACTION_TYPE" value="<%=BaseConstants.POLICY %>"/>
				<input type="hidden" id="serviceInstanceId" name="serviceInstanceId" value="${serviceInstanceId}">
			<input type="hidden" id="serviceId" name="serviceId" value="${serviceId}">
			<input type="hidden" id="serviceType" name="serviceType" value="<%=BaseConstants.PROCESSING_SERVICE%>">
			<input type="hidden" id="serviceName" name="serviceName" value="${serviceName}">
				
			</form>
		</div>
		
		<div style="display: hidden">
			<form id="loadDatabaseConfigForm" action ="<%= ControllerConstants.INIT_BUSINESS_POLICY_MGMT %>" method="POST">
				<input type="hidden" id="server-instance-name" name="server-instance-name" value='${instanceName}' /> 
				<input type="hidden" id="server-instance-host" name="server-instance-host" value='${host}' /> 
				<input type="hidden" id="server-instance-port" name="server-instance-port" value='${port}' /> 
				<input type="hidden" id="server-instance-id" name="server-instance-id" value='${instanceId}' />
				<input type="hidden" name="REQUEST_ACTION_TYPE"  id="REQUEST_ACTION_TYPE" value="<%=BaseConstants.DATABASE_QUERIES %>"/>
				<input type="hidden" id="serviceInstanceId" name="serviceInstanceId" value="${serviceInstanceId}">
			<input type="hidden" id="serviceId" name="serviceId" value="${serviceId}">
			<input type="hidden" id="serviceType" name="serviceType" value="<%=BaseConstants.PROCESSING_SERVICE%>">
			<input type="hidden" id="serviceName" name="serviceName" value="${serviceName}">
				
			</form>
		</div>
		
		<!-- Content wrapper div start here -->
		
		<!-- Footer code start here -->
		<footer class="main-footer positfix">
		
	    	<div class="fooinn">
	        	<div class="fullwidth">
	        		<div class="button-set">
	        			<div class="padleft-right" id="policyDiv" style="">
	        			<c:if test="${(REQUEST_ACTION_TYPE eq 'POLICY')}">
							<button id="importPolicyConfigBtn" class="btn btn-grey btn-xs " type="button"
								onclick="importPolicyConfigPopup();">
								<spring:message code="btn.label.import.config"  ></spring:message>
							</button>
							<button id="exportPolicyConfigBtn" class="btn btn-grey btn-xs" type="button"
								onclick="exportPolicyConfig();">
								<spring:message code="btn.label.export.config" ></spring:message>
							</button>
						</c:if>
						</div>
	        		</div>
	       			<div class="padleft-right" id="consoleDefDiv" style="display: none;">
					</div>	
	            	<div class="padleft-right" id="serviceMapDiv" style="display: none;">
					</div>
	        	</div>
	     		<jsp:include page="../common/newfooter.jsp"></jsp:include>
	     	</div>
		</footer>		<!-- Footer code end here -->
	</div>
	<script type="text/javascript">
	function redirectServerInstanceSummary(){
		$("#instance_form").submit();
	}
	function redirectServiceSummary(){
		$("#service_detail_form").submit();
	}
	function loadPolicyConditionPage(){
		$('#loadPolicyConditionForm').submit();	
	}
	function loadPolicyActionPage(){
		$('#loadPolicyActionForm').submit();	
	}
	function loadPolicyRulePage(){
		$('#loadPolicyRuleForm').submit();	
	}
	function loadPolicyRuleGroupPage(){
		$('#loadPolicyRuleGroupForm').submit();	
	}
	function loadPolicyPage(){
			$('#loadPolicyForm').submit();	
	}
	function loadDatabaseConfigPage(){
		$('#loadDatabaseConfigForm').submit();
	}
	
	function importPolicyConfigPopup(){
		clearAllMessages();
		clearResponseMsgPopUp();
	
		$("#importContent").show();
		$("#divValidationList").html('');
		$('#import-config-progress-bar-div').hide();
		$("#import-add-btn").hide();
	    $("#import-overwrite-btn").show();
	    $('#import-popup-close-btn').show();
	    $("#import-popup-close-btn-summary").hide();
	    clearFileContent();
	    
	    var selectedPolicyId = $("input[name='policyRadio']:checked").val();
	    $('#importPolicyConfig').click();
	    if(selectedPolicyId == null || selectedPolicyId == 'null' || selectedPolicyId <= 0) {
	  		$("#import-update-btn").hide();
	  		$("#import-keepboth-btn").hide();
	    } 
	  	else {
	  		$("#import-update-btn").hide();
	  		$("#import-keepboth-btn").hide();
			//$("#message").click();
	    } 
	  
	    
	    /*  var rowId,syncStatus;
	    	
		if(ckIntanceSelected.length == 0){
			
			
			return;
		}else if(ckIntanceSelected.length > 1){
			$("#lessWarn").hide();
			$("#moreWarn").show();
			$("#message").click();
			return;
		}else{
			// set instance id which is selected for import to submit with form
			var state = checkInstanceState(ckIntanceSelected[0]);
			if(state=='CHECK'){
				$("#inactivewarn").click();
				return;
			}
			$("#importPolicyId").val(ckIntanceSelected[0]);
			$('#importPolicyConfig').click();
		 }*/
		
	}
	
	function exportPolicyConfig() {
		// set policy id which is selected for export to submit with form
		var selectedPolicyId = $("input[name='policyRadio']:checked").val();
		if(selectedPolicyId && selectedPolicyId != null && selectedPolicyId != 'null' && selectedPolicyId > 0) {
			$("#exportPolicyId").val(selectedPolicyId);
			$("#isExportForDelete").val(false);
			$("#REQUEST_ACTION_TYPE").val('<%=BaseConstants.POLICY %>');
			$("#exportPath").val("");
			$("#export-policy-config-form").submit();
		} else {
			$("#message").click();
		} 
	}
	</script>
	</body>
	<!-- Main div end here -->
