<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<%@page import="com.elitecore.sm.common.constants.BaseConstants"%>


<!DOCTYPE html>

<html>
<jsp:include page="../common/newheader.jsp"></jsp:include>


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
                            					<a href="#" onclick="redirectServiceSummary();">
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
                            					<a href="#" onclick="redirectPolicyMgmt();">
                            						 <spring:message code="business.policy.mgmt.tab.heading" ></spring:message>
                            					</a>
                            		</strong>
                            	</span>
									<ol class="breadcrumb1 breadcrumb mtop10">
           								 <li><strong><spring:message code="updtInstacne.head.status.lbl" ></spring:message></strong></li>
           								 <li><a href="#" data-toggle="tooltip" data-placement="bottom" title="Active">&nbsp;<i class="fa fa-circle orangecolor"></i></a></li>
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
													  
														<ul class="nav nav-tabs pull-right">
														
															<sec:authorize access="hasAnyAuthority('CREATE_POLICY_CONFIGURATION')">
																<li class="active">
																	<a href="#create-policy" id="create_policy" data-toggle="tab" aria-expanded="false">
																	<c:choose> 
																	<c:when test="${not empty policy_form_bean.id && policy_form_bean.id != 0}">
																		<spring:message	code="business.policy.update.label" ></spring:message>
																	</c:when>
																	<c:otherwise>
																		<spring:message	code="business.policy.create.label" ></spring:message>
																	</c:otherwise>
																	</c:choose> 
																</a></li>
															</sec:authorize>
														</ul>
														<div class="fullwidth no-padding tab-content">
														<sec:authorize access="hasAnyAuthority('CREATE_POLICY_CONFIGURATION')">
															<div id="create-policy"
																class="tab-pane active">
														 		<jsp:include page="createPolicy.jsp"></jsp:include> 
															</div>
														</sec:authorize>
														</div>
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
										<input type="hidden" id="REQUEST_ACTION_TYPE" name="REQUEST_ACTION_TYPE" value='POLICY'/>
									
									</form>
								</div>
								<div style="display: none;">
									<form id="service_detail_form" method="GET" action="<%= ControllerConstants.INIT_PROCESSING_SERVICE_MANAGER %>">
										<input type="hidden" id="serviceInstanceId" name="serviceInstanceId" value="${serviceInstanceId}">
										<input type="hidden" id="serviceId" name="serviceId" value="${serviceId}">
										<input type="hidden" id="serviceType" name="serviceType" value="<%=BaseConstants.PROCESSING_SERVICE%>">
										<input type="hidden" id="serviceName" name="serviceName" value="${serviceName}">
										<input type="hidden" id="instanceId" name="instanceId" value="${instanceId}">
										<input type="hidden" id="server-instanceId" name="instanceId" value='${instanceId}'/>
										<input type="hidden" id="server-instance-id" name="server-instance-id" value="${instanceId}">
										<input type="hidden" id="REQUEST_ACTION_TYPE" name="REQUEST_ACTION_TYPE" value='POLICY'/>
									</form>
								</div>
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
			            	<div class="padleft-right" id="policyBtnDiv">
							<sec:authorize access="hasAuthority('CREATE_POLICY_CONFIGURATION')">
								<c:choose>
									<c:when test="${not empty policy_form_bean.id && policy_form_bean.id != 0}">
										<button class="btn btn-grey btn-xs " tabindex="21" onclick="createPolicy();" id="update_policy_btn">
											<spring:message code="btn.label.update" ></spring:message>
										</button>
									</c:when>
									<c:otherwise>
										<button class="btn btn-grey btn-xs " tabindex="21"	onclick="createPolicy();" id="create_policy_btn">
											<spring:message code="btn.label.create" ></spring:message>
										</button>
									</c:otherwise>
								</c:choose>
							</sec:authorize>

							<button class="btn btn-grey btn-xs " tabindex="23"  onclick="resetField(${policy_form_bean.id});" id="reset_policy_btn">									
								<spring:message code="btn.label.reset" ></spring:message> 
							</button>
								
								<button class="btn btn-grey btn-xs " tabindex="22" onclick="redirectPolicyMgmt('POLICY');" id="cancel_policy_btn">
									<spring:message code="btn.label.cancel"  ></spring:message>
								</button>
							</div>	
				           	
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
	function redirectPolicyMgmt(action){
		$("#REQUEST_ACTION_TYPE").val(action);
		var serviceId= "${serviceId}";
		if(serviceId == ''){
			$("#server_instance_detail_form").attr("action","<%=ControllerConstants.INIT_BUSINESS_POLICY_MGMT%>");
			$("#server_instance_detail_form").submit();
		}else{
			$("#service_detail_form").attr("method","POST");
			$("#service_detail_form").attr("action","<%=ControllerConstants.INIT_BUSINESS_POLICY_MGMT%>");
			$("#service_detail_form").submit();
		}
	}
	function redirectServiceSummary(){
		$("#service_detail_form").submit();
	}
	function resetPolicyForm(policyId) {
		$("#policy-id").val(policyId);
		$("#update-policy-form").attr("action","<%=ControllerConstants.INIT_CREATE_POLICY%>");
		$("#update-policy-form").submit();
		var $form = $("#update-policy-form");
		$form.attr("action","<%=ControllerConstants.INIT_CREATE_POLICY%>");
		$form.submit();		
	}
	</script>
	</body>
	<!-- Main div end here -->
