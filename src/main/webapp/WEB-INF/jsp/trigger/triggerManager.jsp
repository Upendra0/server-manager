<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<%@page import="com.elitecore.sm.common.constants.BaseConstants"%>
    
<!DOCTYPE html>
<html>
<head>
<script type="text/javascript">
var ckSelectedTriggers = [];
</script>
</head>
<jsp:include page="../common/newheader.jsp"></jsp:include>
<body class="skin-blue sidebar-mini sidebar-collapse">
<div>
	<jsp:include page="../common/newtopNavigationPanel.jsp"></jsp:include>
	<jsp:include page="../common/newleftMenu.jsp"></jsp:include>
	
	<!-- Content div Start here -->
	<div class="content-wrapper">
			<section class="content-header">
				<div class="fullwidth">
					<div style="padding: 0 10px 0 4px;">
						<div id="content-scroll-d" class="content-scroll">
							<div class="fullwidth">
								<h4 style="margin-top:10px; margin-bottom:0px; font-size:11px;">
									<a href="home.html"><img src="img/tile-icon.png"class="vmiddle" alt="Home" /></a> 
									<span class="spanBreadCrumb" style="line-height: 30px;"> 
										<strong> <spring:message code="trigger.config.tab.header" ></spring:message></strong>
									</span>
								</h4>
								<jsp:include page="../common/responseMsg.jsp" ></jsp:include>
								<!-- Tab code start here  -->
								<div class="row">
									<div class="col-xs-12">
										<div class="box martop" style="border: none; box-shadow: none;">
											<div class="box-body table-responsive no-padding">
												<div class="nav-tabs-custom tab-content">
			
			
													<ul class="nav nav-tabs pull-right">
														<sec:authorize access="hasAnyAuthority('VIEW_TRIGGER_CONFIG')">
															<li class="<c:if test="${(REQUEST_ACTION_TYPE eq 'UPDATE_TRIGGER_CONFIG') or (REQUEST_ACTION_TYPE eq 'VIEW_TRIGGER_RECORDS')}">
																<c:out value="active"></c:out></c:if>">
																<a href="#trigger-summary" id="trigger_summary" data-toggle="tab" aria-expanded="false"> 
																<spring:message	code="trigger.config.tab.header" ></spring:message>
															</a></li>
														</sec:authorize>
													</ul>
												 	
													<div class="fullwidth no-padding tab-content">
													<sec:authorize access="hasAnyAuthority('VIEW_TRIGGER_CONFIG')">
														<div id="trigger_mgmt_div"
															class="tab-pane <c:if test="${(REQUEST_ACTION_TYPE eq 'VIEW_TRIGGER_RECORDS')}"><c:out value="active"></c:out></c:if> ">
													 		<c:if test="${(REQUEST_ACTION_TYPE eq 'VIEW_TRIGGER_RECORDS')}">
													 			<jsp:include page="triggerMgmt.jsp"></jsp:include> 
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
		<!-- Content div End here -->
		
		<!-- Footer code start here -->
		<footer class="main-footer positfix">
		    	<div class="fooinn">
		        	<jsp:include page="../common/newfooter.jsp"></jsp:include>
		    	</div>
			</footer>
		<!-- Footer code end here -->
</div>
	
</body>
</html>
