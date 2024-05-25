<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<%@page import="com.elitecore.sm.common.constants.BaseConstants"%>


<!DOCTYPE html>
<html>
<script>
	var dbDetailListCounter=0;
</script>

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
								<a href="home.html"><img src="img/tile-icon.png" class="vmiddle" alt="Title Icon"/></a> 
                                    <span class="spanBreadCrumb" style="line-height: 30px;font-weight: bold;">
                                   		<strong>&nbsp;
                                   			<spring:message code="dbconfig.database.configuration.tab.db.label"></spring:message>
                                   		</strong>&nbsp;
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
													<sec:authorize access="hasAnyAuthority('DATABASE_CONFIGURATION_MENU_VIEW')">  
														<ul class="nav nav-tabs pull-right">
														
															<sec:authorize access="hasAnyAuthority('DATABASE_CONFIGURATION')">
																<li class="<c:if test="${(REQUEST_ACTION_TYPE eq 'DATABASE_CONFIGURATION')}"> <c:out value="active"></c:out></c:if>" onclick="loadDSConfigTab();"	>
																	<a href="#database-configuration" id="database_configuration" data-toggle="tab" aria-expanded="false"> 
																		<spring:message code="dbconfig.database.configuration.tab.db.label"></spring:message>
																	</a>
																</li>
																</sec:authorize>
																<sec:authorize access="hasAnyAuthority('VIEW_KAFKA_DATASOURCE')">
																	<li class="<c:if test="${(REQUEST_ACTION_TYPE eq 'KAFKA_CONFIGURATION')}"> <c:out value="active"></c:out></c:if>" onclick="loadKafkaConfigTab();"	>
																		<a href="#kafka-configuration" id="kafka_configuration" data-toggle="tab" aria-expanded="false"> 
																			<spring:message code="dbconfig.kafka.configuration.tab.label"></spring:message>
																		</a>
																	</li>
																</sec:authorize>
														</ul>
														<div class="fullwidth no-padding tab-content">
														<sec:authorize access="hasAnyAuthority('DATABASE_CONFIGURATION')">
															<div id="database-configuration"
																class="tab-pane <c:if test="${(REQUEST_ACTION_TYPE eq 'DATABASE_CONFIGURATION')}"><c:out value="active"></c:out></c:if> ">
	 														 		<jsp:include page="databaseConfiguration.jsp"></jsp:include>  
															</div>
														</sec:authorize>
														<sec:authorize access="hasAnyAuthority('VIEW_KAFKA_DATASOURCE')">
															<div id="kafka-configuration"
																class="tab-pane <c:if test="${(REQUEST_ACTION_TYPE eq 'KAFKA_CONFIGURATION')}"><c:out value="active"></c:out></c:if> ">
	 														 		<jsp:include page="kafkaConfiguration.jsp"></jsp:include>  
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

		</div>
		<!-- Content wrapper div start here -->
		
		<div style="display: hidden">
			<form id="loadDSInstanceStatus-form" action ="<%= ControllerConstants.INIT_CONFIGURATION_MANAGER %>" method="GET">
				<input type="hidden" name="<%= BaseConstants.REQUEST_ACTION_TYPE %>"  id="<%= BaseConstants.REQUEST_ACTION_TYPE %>" value="<%= BaseConstants.DATABASE_CONFIGURATION %>" />
			</form>
		</div>
		<div style="display: hidden">
			<form id="loadKafkaConfigForm" action ="<%= ControllerConstants.INIT_CONFIGURATION_MANAGER %>" method="GET">
				<input type="hidden" name="<%= BaseConstants.REQUEST_ACTION_TYPE %>"  id="<%= BaseConstants.REQUEST_ACTION_TYPE %>" value="<%= BaseConstants.KAFKA_CONFIGURATION%>" />
			</form>
		</div>	
		<!-- Footer code start here -->
		<footer class="main-footer positfix">
		    	<div class="fooinn">
		        	<jsp:include page="../common/newfooter.jsp"></jsp:include>
		    	</div>
			</footer>
		<!-- Footer code end here -->
	</div>
	<!-- Main div end here -->
	<script type="text/javascript">
		function loadDSConfigTab(){
			$("#loadDSInstanceStatus-form").submit();
		}
		function loadKafkaConfigTab(){
			$('#loadKafkaConfigForm').submit();	
		}
	</script>
</body>
</html>	
	
