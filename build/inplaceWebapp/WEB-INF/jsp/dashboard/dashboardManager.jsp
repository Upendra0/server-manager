<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
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
									<a href="home.html"><img id="home_lnk" src="img/tile-icon.png" class="vmiddle" alt="Home" /></a> 
									<span class="spanBreadCrumb" style="line-height: 30px;"> 
										<strong>&nbsp;
											 <spring:message code="dashboard.header.titile" ></spring:message>
										</strong>
									</span>
								</h4>
								
								<jsp:include page="../common/responseMsg.jsp" ></jsp:include>	
								
								<!-- Tab code start here  -->
								<div class="row">
									<div class="col-xs-12">
										<div class="box  martop" style="border: none; box-shadow: none;">
											<div class="box-body table-responsive no-padding">
												<div class="nav-tabs-custom tab-content">
												
													<!-- Change Role here -->
													<sec:authorize access="hasAnyAuthority('SERVER_MANAGER_MENU_VIEW')">  
														<ul class="nav nav-tabs pull-right">
															<sec:authorize access="hasAnyAuthority('SERVER_MANAGEMENT')">
																<li class="<c:if test="${(REQUEST_ACTION_TYPE eq 'DASHBOARD')}">
																	<c:out value="active"></c:out></c:if>"	onclick="tabButtonManagement('server_view');">
																	<a href="#server-view" id="server_view" data-toggle="tab" aria-expanded="false"> 
																		<spring:message code="dashboard.tab.server.heading" ></spring:message>
																	</a>
																	</li>
															</sec:authorize>
															 <sec:authorize access="hasAnyAuthority('CREATE_SERVER')">
																<li
																	class="<c:if test="${(REQUEST_ACTION_TYPE eq 'SERVER_INTANCES')}"><c:out value="active"></c:out></c:if>" onclick="tabButtonManagement('server_intances_view');">
																	<a href="#server-intances-view" id="server_intances_view"	data-toggle="tab" aria-expanded="true"> 
																		<spring:message code="dashboard.tab.server.instance.heading" ></spring:message>
																	</a>
																</li>
															</sec:authorize> 
															<li class="pull-right">
																<select id="server_instance_list" class="form-control table-cell input-sm" data-toggle="tooltip" data-placement="bottom" title="Server Instance" style="display:none; height: 27px;">
																	 <option value="Mediation Server-192.168.0.146:8080">Mediation Server-192.168.0.146:8080</option>
																	 <option value="CGF Server-192.168.1.19:8081">CGF Server-192.168.1.19:8081</option>
																	 <option value="IPLMS_Server - 192.168.1.168:9090">IPLMS_Server - 192.168.1.168:9090</option>
																	 <option value="Mediation Server-192.168.1.198:9191">Mediation Server-192.168.1.198:9191</option>
																	 <option value="Mediation Server-192.168.4.20:8089" >Mediation Server-192.168.4.20:8089</option>
																</select>	
															</li>
														</ul>
														<sec:authorize access="hasAnyAuthority('SERVER_MANAGEMENT')">
															<div id="server-view"
																class="tab-pane <c:if test="${(REQUEST_ACTION_TYPE eq 'DASHBOARD')}"><c:out value="active"></c:out></c:if> ">
														 		<jsp:include page="serverDashboard.jsp"></jsp:include> 
															</div>
														</sec:authorize>

														<div id="server-intances-view"
															class="tab-pane  <c:if test="${(REQUEST_ACTION_TYPE eq 'SERVER_INTANCES')}"><c:out value="active"></c:out></c:if>">
															<jsp:include page="serverInstanceDashboard.jsp"></jsp:include>
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
		</div>
		<!-- Content wrapper div start here -->
		<!-- Footer code start here -->
			<footer class="main-footer positfix">
		    	<div class="fooinn">
		        	<jsp:include page="../common/newfooter.jsp"></jsp:include>
		    	</div>
			</footer>
			<script type="text/javascript">
				function tabButtonManagement(type){
					if(type == 'server_intances_view'){
						$("#server_instance_list").show();
					}else if(type == 'server_view'){
						$("#server_instance_list").hide();
					}
				}
				
				var selectedInstanceName = $('#server_instance_list').val();
				
				 $("#server_instance_list").change(function () {
				        selectedInstanceName = $('#server_instance_list').val();
				        console.log("Selected Instance..." + selectedInstanceName);
				        loadAllChart();
				    });
			
				 console.log("Selected Instance..." + selectedInstanceName);
			</script>
		<!-- Footer code end here -->
	</div>
	<!-- Main div end here -->
