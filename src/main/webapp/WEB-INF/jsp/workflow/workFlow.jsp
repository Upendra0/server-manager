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
									<a href="home.html"><img src="img/tile-icon.png"class="vmiddle" alt="Home" /></a> 
									<span class="spanBreadCrumb" style="line-height: 30px;"> 
										<strong>&nbsp; 
											 <spring:message code="workflow.tab.heading" ></spring:message>
										</strong>
									</span>
								</h4>
								
								<jsp:include page="../common/responseMsg.jsp" ></jsp:include>	
								
								<!-- Tab code start here  -->
								<div class="fullwidth">
									<img src="img/workFlow.png" alt="WorkFlow" />
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
		<!-- Footer code end here -->
	</div>
	<!-- Main div end here -->
