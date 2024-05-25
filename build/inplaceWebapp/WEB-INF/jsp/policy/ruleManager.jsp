<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<%@page import="com.elitecore.sm.common.constants.BaseConstants"%>

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
			<div id="content-scroll-d" class="content-scroll">
		<div class="content-wrapper">
			<section class="content-header">
				<div class="fullwidth">
					<div style="padding: 0 10px 0 4px;">
					
							<div class="fullwidth">
								<h4 style="margin-top:10px; margin-bottom:0px; font-size:11px;">
									<span class="spanBreadCrumb" style="line-height: 30px;">
									<a href="home.html"><img src="img/tile-icon.png"class="vmiddle" alt="Home" /></a> 
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
                            					</a>
                            					&nbsp; / &nbsp;
                            					<a href="#" onclick="redirectServerInstanceSummary();">
                            						${instanceName} &nbsp; : &nbsp; ${host} &nbsp; - &nbsp; ${port}
                            					</a>
                            				</c:otherwise>
                            			</c:choose>
                            			&nbsp; / &nbsp;
                            			
                           			
                           			<a href="#" onclick="redirectPolicyMgmt();">
                           			 	<strong><spring:message code="business.policy.mgmt.tab.heading" ></spring:message></strong>
                           			</a>
                           			</strong>  
									</span>
								</h4>
								<jsp:include page="../common/responseMsg.jsp" ></jsp:include>
<%-- 								<jsp:include page="../common/responseMsgPopUp.jsp" ></jsp:include>	   --%>
								
								<!-- Tab code start here  -->
								<div class="row">
									<div class="col-xs-12">
										<div class="box  martop" style="border: none; box-shadow: none;">
											<div class="box-body table-responsive no-padding">
												<div class="nav-tabs-custom tab-content">
													<!-- Change Role here -->
												
													<sec:authorize access="hasAnyAuthority('VIEW_POLICY_CONFIGURATION')">  
														<ul class="nav nav-tabs pull-right">
															<sec:authorize access="hasAnyAuthority('VIEW_POLICY_CONFIGURATION')">
																<li class="<c:if test="${(REQUEST_ACTION_TYPE eq 'CREATE_NEW_RULE')}">
																	<c:out value="active"></c:out></c:if>"	>
																	<a href="#create-new-rule" id="create_new_rule" data-toggle="tab" aria-expanded="false">
																	<c:choose>
																	    <c:when test="${not empty rule_form_bean.id && rule_form_bean.id != 0}">
																			<spring:message code="business.policy.update.rule.tab.heading" ></spring:message>
																	    </c:when>
																	    <c:otherwise>
																			<spring:message code="business.policy.create.rule.tab.heading" ></spring:message>
																	    </c:otherwise>
																	</c:choose>
																	</a>
																</li>
															</sec:authorize>
														</ul>
														<div class="fullwidth no-padding tab-content">
															<sec:authorize access="hasAnyAuthority('CREATE_POLICY_RULE_CONFIGURATION')">
																<%-- <div id="create-rule" class="tab-pane <c:if test="${(REQUEST_ACTION_TYPE eq 'CREATE_NEW_RULE')}"><c:out value="active"></c:out></c:if> "> --%>
																<div id="create-rule" class="tab-pane active">
		 														 	<jsp:include page="createRule.jsp"></jsp:include>
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
								
								
								<div style="display: none;">
									<form id="instance_form" method="POST" action="<%= ControllerConstants.INIT_UPDATE_SERVER_INSTANCE %>">
									<input type="hidden" id="serverInstanceId" name="serverInstanceId" value='${instanceId}'>
									</form>
								</div>
								<div style="display: none;">
									<form id="server_instance_detail_form" method="POST" >
										<input type="hidden" id="server-instance-name" name="server-instance-name" value='${instanceName}' /> 
										<input type="hidden" id="server-instance-host" name="server-instance-host" value='${host}' /> 
										<input type="hidden" id="server-instance-port" name="server-instance-port" value='${port}' /> 
										<input type="hidden" id="server-instance-id" name="server-instance-id" value='${instanceId}' />
										<input type="hidden" id="serviceInstanceId" name="serviceInstanceId" value="${serviceInstanceId}">
										<input type="hidden" id="serviceId" name="serviceId" value="${serviceId}">
										<input type="hidden" id="serviceType" name="serviceType" value="<%=BaseConstants.PROCESSING_SERVICE%>">
										<input type="hidden" id="serviceName" name="serviceName" value="${serviceName}">
										<input type="hidden" id="REQUEST_ACTION_TYPE" name="REQUEST_ACTION_TYPE" value='POLICY_RULE'/>
									</form>
								</div>
								<div style="display: none;">
									<form id="service_detail_form" method="POST" action="<%= ControllerConstants.INIT_UPDATE_SERVICE %>">
										<input type="hidden" id="serviceInstanceId" name="serviceInstanceId" value="${serviceInstanceId}">
										<input type="hidden" id="serviceId" name="serviceId" value="${serviceId}">
										<input type="hidden" id="serviceType" name="serviceType" value="<%=BaseConstants.PROCESSING_SERVICE%>">
										<input type="hidden" id="serviceName" name="serviceName" value="${serviceName}">
										<input type="hidden" id="instanceId" name="instanceId" value="${instanceId}">
										<input type="hidden" id="REQUEST_ACTION_TYPE" name="REQUEST_ACTION_TYPE" value='POLICY_RULE'/>
										<input type="hidden" id="server-instance-id" name="server-instance-id" value="${instanceId}">
									</form>
								</div>
								
								
								
							</div>
					
					</div>
				</div>
			</section>
		
		<!-- Footer code start here -->
		
		    	<div class="fooinn">
				<div class="fullwidth">
					<div class="button-set">

						<div class="padleft-right" id="ruleBtnDiv">

							<sec:authorize
								access="hasAuthority('CREATE_POLICY_RULE_CONFIGURATION')">
								<c:choose>
									<c:when
										test="${not empty rule_form_bean.id && rule_form_bean.id != 0}">
										<button class="btn btn-grey btn-xs " tabindex="21"
											onclick="createRule();" id="update_rule_btn">
											<spring:message code="btn.label.update" ></spring:message>
										</button>
									</c:when>
									<c:otherwise>
										<button class="btn btn-grey btn-xs " tabindex="21"
											onclick="createRule();" id="create_rule_btn">
											<spring:message code="btn.label.create" ></spring:message>
										</button>
									</c:otherwise>
								</c:choose>
							</sec:authorize>

							<button class="btn btn-grey btn-xs " tabindex="22"
								onclick="resetField(${rule_form_bean.id});" id="reset_rule_btn">
								<spring:message code="btn.label.reset" ></spring:message>
							</button>
							<button class="btn btn-grey btn-xs " tabindex="23"
								onclick="redirectPolicyMgmt('POLICY_RULE');" id="cancel_rule_btn">
								<spring:message code="btn.label.cancel" ></spring:message>
							</button>
						</div>
					</div>
				</div>
				
				<!-- Policy validation msg for select rule/condition/group. -->
 				<jsp:include page="policyCommonValidaton.jsp"></jsp:include>  
				<jsp:include page="../common/newfooter.jsp"></jsp:include>
		    	</div>
		</div>
		    </div>
		<!-- Footer code end here -->
					
		
		<script type="text/javascript">
			
		function redirectServerInstanceSummary(){
			$("#instance_form").submit();
		}
		function redirectServiceSummary(){
			$("#service_detail_form").submit();
		}
		function redirectPolicyMgmt(action){
			$("#REQUEST_ACTION_TYPE").val(action);
			var serviceId='${serviceId}';
			if(serviceId == ''){
				$("#server_instance_detail_form").attr("action","<%=ControllerConstants.INIT_BUSINESS_POLICY_MGMT%>");
				$("#server_instance_detail_form").submit();
			}else{
				$("#service_detail_form").attr("action","<%=ControllerConstants.INIT_BUSINESS_POLICY_MGMT%>");
				$("#service_detail_form").submit();
			}
		}

		</script>
	</div>
</body>
</html>
