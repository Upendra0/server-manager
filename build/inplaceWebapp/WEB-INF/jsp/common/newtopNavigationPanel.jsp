
<%@page import="com.elitecore.sm.util.MapCache"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page import="com.elitecore.sm.util.EliteUtils"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@page import="com.elitecore.sm.iam.model.Staff"%>
<%@page import="com.elitecore.sm.common.constants.BaseConstants"%>
<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<%@page
	import="com.elitecore.sm.common.constants.BusinessModelConstants"%>
<%@page
	import="com.elitecore.sm.common.constants.SystemParametersConstant"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%-- <jsp:include page="pageLoad.jsp"></jsp:include>  --%>
<header class="main-header">
	<sec:authorize access="isAuthenticated()">
		<c:if test="${not sessionScope.IS_LOGIN_FIRST_TIME}">
			<!--  			         Logo   -->
			<a href="#" class="logo"> <!-- mini logo for sidebar mini 50x50 pixels   -->
				<span class="logo-mini"><img
					src="<%=new EliteUtils().getCustomerLogo(SystemParametersConstant.CUSTOMER_LOGO,"img/crestel_small_logo.png") %>"
					class="img-responsive" alt="Logo" height="50px" width="50px" /></span> <!-- logo for regular state and mobile devices   -->
				<span class="logo-lg"><img
					src="<%=new EliteUtils().getCustomerLogo(SystemParametersConstant.CUSTOMER_LOGO_LARGE,"img/Crestel_large_logo.jpg") %>"
					alt="Logo" height="50px" width="100px" /></span>
			</a>
		</c:if>
	</sec:authorize>


	<!-- Header Navbar -->
	<nav class="navbar navbar-static-top" role="navigation"
		style="min-height: 32px;">
		<!-- Sidebar toggle button-->
		<%
      			 String licenseExpshortMessage = "" ;
      			 if( session.getAttribute("licenseShortReminder") != null && session.getAttribute(BaseConstants.STAFF_DETAILS) != null){
      				licenseExpshortMessage = new String((String)session.getAttribute("licenseShortReminder")).trim();
      			}  
		%> 
		<a href="#" class="server-logo"><img src="img/server-logo.png"
			alt="Server Logo" /></a> 
		<span class="server-logo">	
			<span id="license_short_reminder" style="padding-left:10px; display: inline-block; vertical-align: middle;"> 
				<%= licenseExpshortMessage %>
			</span>
		</span>
		<sec:authorize access="hasAnyAuthority('HOME')">
			<c:if test="${not sessionScope.IS_LOGIN_FIRST_TIME}">
				<div class="navbar-custom-menu">
					<ul class="nav navbar-nav">
						<li class="dropdown messages-menu">
							<!--                 			<span id="notification_count">3</span> -->
							<a data-toggle="dropdown" class="dropdown-toggle"
							onclick="initHeight();" href="javascript:"> <i class="fa">
									<!--                 						<img alt="Notification Logo" src="images/icon-speaker.gif" width="24px" height="24px"><label id="lblNotificationCount" style="color: white;">4</label> -->
							</i>
						</a>
						</li>

						<!--	Uncomment below code for show static notification 
                			 <li class="dropdown messages-menu" style="padding-left: 15px;">
                				<a data-toggle="dropdown" class="dropdown-toggle" onclick="initHeight();" href="javascript: ;" id="notificationLink"> 
                					<i class="fa">
                						<img alt="Server Logo" src="img/top-icon1.png"><label id="lblNotificationCount" style="color: white;padding-left:5px;">8</label>
                					</i>
                				</a>
                				<div id="notificationContainer" style="padding-right: 15px;">
									<div id="notificationTitle">Notifications 
										<i class="fa">
                							<i class="fa">
		                						<label id="lblCount" class="glyph-white-background">8</label>
		                					</i>
                						</i>
                					</div>
									<div id="notificationsBody" class="notifications" style="padding-left: 20px;">
										<div style="border-bottom: 2px black;">
											<table style="width: 90%;">
												<tr>
													<td width="15%" rowspan="2"><img src="img/info.png" width="20" height="20"/></td>
													<td width="85%;">Product License is near to expire. Please renew the product license</td>
												</tr>
												<tr>
													<td align="right" style="font-size: 11px">12-02-2015 12:13:12</td>
												</tr>
											</table>
											
										</div>
										<hr width="100%;" style="margin: 0px 0px 0px 0px;"/>
										<div style="border-bottom: 2px black;">
											<table style="width: 90%;">
												<tr>
													<td width="15%" rowspan="2"><img src="img/info.png" width="20" height="20"/></td>
													<td width="85%;">Product License is near to expire. Please renew the product license</td>
												</tr>
												<tr>
													<td align="right" style="font-size: 11px">11-02-2015 12:12:12</td>
												</tr>
											</table>
											<hr width="100%;" style="margin: 0px 0px 0px 0px;"/>
										</div>
										<div style="border-bottom: 2px black;">
											<table style="width: 90%;">
												<tr>
													<td width="15%" rowspan="2"><img src="img/alert.png" width="20" height="20"/></td>
													<td width="85%;">SGSN Connection Lost</td>
												</tr>
												<tr>
													<td align="right" style="font-size: 11px">11-02-2015 10:15:00</td>
												</tr>
											</table>
											<hr width="100%;" style="margin: 0px 0px 0px 0px;"/>
										</div>
										<div style="border-bottom: 2px black;">
											<table style="width: 90%;">
												<tr>
													<td width="15%" rowspan="2"><img src="img/warning.png" width="20" height="20"/></td>
													<td width="85%;">TPS Violation</td>
												</tr>
												<tr>
													<td align="right" style="font-size: 11px">11-02-2015 09:30:00</td>
												</tr>
											</table>
											<hr width="100%;" style="margin: 0px 0px 0px 0px;"/>
										</div>
										<div style="border-bottom: 2px black;">
											<table style="width: 90%;">
												<tr>
													<td width="15%" rowspan="2"><img src="img/info.png" width="20" height="20"/></span></td>
													<td width="85%;">Product License is near to expire. Please renew the product license</td>
												</tr>
												<tr>
													<td align="right" style="font-size: 11px">10-02-2015 12:12:12</td>
												</tr>
											</table>
											<hr width="100%;" style="margin: 0px 0px 0px 0px;"/>
										</div>
										<div style="border-bottom: 2px black;">
											<table style="width: 90%;">
												<tr>
													<td width="15%" rowspan="2"><img src="img/alert.png" width="20" height="20"/></td>
													<td width="85%;">SGSN Connection Lost</td>
												</tr>
												<tr>
													<td align="right" style="font-size: 11px">10-02-2015 10:15:00</td>
												</tr>
											</table>
											<hr width="100%;" style="margin: 0px 0px 0px 0px;"/>
										</div>
										<div style="border-bottom: 2px black;">
											<table style="width: 90%;">
												<tr>
													<td width="15%" rowspan="2"><img src="img/alert.png" width="20" height="20"/></td>
													<td width="85%;">SGSN Connection Lost</td>
												</tr>
												<tr>
													<td align="right" style="font-size: 11px">09-02-2015 10:15:00</td>
												</tr>
											</table>
											<hr width="100%;" style="margin: 0px 0px 0px 0px;"/>
										</div>
										<div style="border-bottom: 2px black;">
											<table style="width: 90%;">
												<tr>
													<td width="15%" rowspan="2"><img src="img/info.png" width="20" height="20"/></td>
													<td width="85%;">SGSN Connection Lost</td>
												</tr>
												<tr>
													<td align="right" style="font-size: 11px">08-02-2015 10:15:00</td>
												</tr>
											</table>
										</div>
									</div>
									<div id="notificationFooter"><a href="#" id="viewAllLink" style="color: black;" onclick="viewFullHeight();">View All</a>
									<a href="#" id="viewLessLink" style="color: black;" onclick="initHeight();">View Less</a></div>
								</div>
                			</li> -->

						<li class="dropdown"><a class="dropdown-toggle"
							href="<%=ControllerConstants.INIT_ABOUT_US %>"> <i class="fa">
									<img src="img/about_us.png">
							</i>
						</a></li>

						<li class="dropdown user user-menu notifications-menu"><sec:authorize
								access="isAuthenticated()">
								<a href="#" class="dropdown-toggle" data-toggle="dropdown" id="profile_img_lnk">
									<%-- <spring:message code="topNavigationPanel.welcome"></spring:message>, --%>
									<%
				               			 String content = "img/staff_default_profile_pic.png" ;
				               			 if( session.getAttribute(BaseConstants.STAFF_DETAILS) != null && ((Staff)session.getAttribute(BaseConstants.STAFF_DETAILS)).getProfilePic()!=null){
				               				 //content =new String( MapCache.getConfigValueAsString(SystemParametersConstant.SYSTEM_BACKUP_FILE_PATH,SystemParametersConstant.SYSTEM_BACKUP_FILE_PATH_VALUE)+((Staff)session.getAttribute(BaseConstants.STAFF_DETAILS)).getProfilePic());
				               				content = new String((String)session.getAttribute(BaseConstants.STAFF_LOGO));
				               			}  
				               		%> 
				               		
											               				 <img alt="No Image Found" src="<%= content %>" width="24" height="24" id="profile_img">

								</span> <i class="fa fa-angle-down"></i>
								</a>

							</sec:authorize>
							
							<ul class="dropdown-menu">
								<li>
								<%
				               			 String stafftype = "" ;
				               			 if( session.getAttribute(BaseConstants.STAFF_DETAILS) != null){
				               				 //content =new String( MapCache.getConfigValueAsString(SystemParametersConstant.SYSTEM_BACKUP_FILE_PATH,SystemParametersConstant.SYSTEM_BACKUP_FILE_PATH_VALUE)+((Staff)session.getAttribute(BaseConstants.STAFF_DETAILS)).getProfilePic());
				               				Staff staff =  (Staff) session.getAttribute(BaseConstants.STAFF_DETAILS);
				               				stafftype = staff.getStafftype(); 
				               			}  
				               		%> 
									<!-- inner menu: contains the actual data -->
									<%if(stafftype.equalsIgnoreCase(BaseConstants.LOCAL_STAFF)){%>
									<%if(Boolean.parseBoolean(MapCache.getConfigValueAsObject(SystemParametersConstant.SSO_ENABLE).toString())){ %>
											<div class="slimScrollDiv"
										style="position: relative; overflow: hidden; width: auto; height: 75px;">						
									<% } else {%>
										<div class="slimScrollDiv"
										style="position: relative; overflow: hidden; width: auto; height: 113px;">	
									<%} %>		
									<% } else {%>
									<div class="slimScrollDiv"
										style="position: relative; overflow: hidden; width: auto; height: 38px;">
									<%} %>
											
											
											<%if(stafftype.equalsIgnoreCase(BaseConstants.LOCAL_STAFF)){ %>								
											<%if(Boolean.parseBoolean(MapCache.getConfigValueAsObject(SystemParametersConstant.SSO_ENABLE).toString())){ %>
												<c:url	value="/sso/j_spring_security_logout" var="logoutUrl" ></c:url>
											<ul class="menu"
											style="overflow: hidden; width: 100%; height: 123px;">
											<li><a id="my_profile_lnk" href="<%= ControllerConstants.MY_PROFILE %>"><img
													src="img/My_profile_O.png" width="12px" height="12px" />
													&nbsp;&nbsp;<spring:message code="myProfile.page.heading" ></spring:message>
											</a></li>			
											<% } else {%>
												
											
											<c:url	value="/sso/j_spring_security_logout" var="logoutUrl" ></c:url>
											<ul class="menu"
											style="overflow: hidden; width: 100%; height: 123px;">
											<li><a id="my_profile_lnk" href="<%= ControllerConstants.MY_PROFILE %>"><img
													src="img/My_profile_O.png" width="12px" height="12px" />
													&nbsp;&nbsp;<spring:message code="myProfile.page.heading" ></spring:message>
											</a></li>
											<li><sec:authorize
													access="hasAnyAuthority('CHANGE_PASSWORD_MENU_VIEW')">
													<li><a
														id="change_pswd_lnk" href="<%= ControllerConstants.INIT_CHANGE_PASSWORD %>">
															<img src="img/Change_Password_O.png" width="12px"
															height="12px" /> &nbsp;&nbsp;<spring:message
																code="leftMenu.change.password.menu" ></spring:message>
													</a></li>
												</sec:authorize></li>
											<%} %>		
											<%} else {%>
											<ul class="menu"
											style="overflow: hidden; width: 100%; height: 50px;">
											<%} %>
											
											<li>
												<!-- Logout Form --> 
<%-- 												 <c:url	value="/j_spring_security_logout" var="logoutUrl" ></c:url> --%>
												<% if(Boolean.parseBoolean(MapCache.getConfigValueAsObject(SystemParametersConstant.SSO_ENABLE).toString())){ %>								
												<c:url	value="/sso/j_spring_security_logout" var="logoutUrl" ></c:url>
												<%}else{ %>
												<c:url	value="/j_spring_security_logout" var="logoutUrl" ></c:url> 
												<%} %>
												<form action="${logoutUrl}" method="post" id="logoutForm">
												</form> <script>
														function logoutFormSubmit() {
															document.getElementById("logoutForm").submit();
														}
													</script> <!-- End of Logout Form --> <a
												id="logout_lnk" href="javascript: logoutFormSubmit();"> <img
													src="img/Logout_O.png" width="12px" height="12px" />
													&nbsp;&nbsp;<spring:message code="logout.menu.heading" ></spring:message>
											</a>
											</li>
										</ul>
									</div>
								</li>
							</ul></li>
					</ul>
				</div>
			</c:if>
		</sec:authorize>

		<!-- Navbar Right Menu -->

	</nav>
</header>

<script type="text/javascript">
	$(document).ready(function()
	{
		$("#notificationLink").click(function()
		{
			$("#notificationContainer").fadeToggle(300);
			$("#notification_count").fadeOut("slow");
			return false;
		});

		//Document Click
		$(document).click(function()
		{
			$("#notificationContainer").hide();
		});
		//Popup Click
		$("#notificationContainer").click(function()
		{
			return false
		});
	});
	
	function viewFullHeight(){
		$('#notificationsBody').css('height',$('#notificationsBody').css('max-height'));
		$('#notificationsBody').css('overflow','scroll');
		$('#notificationsBody').css('overflow-x','hidden');
		$('#viewLessLink').show();
		$('#viewAllLink').hide();
	}
	
	function initHeight(){
		$('#notificationsBody').css('height','150px');
		$('#notificationsBody').css('overflow','hidden');
		$('#notificationsBody').css('margin-bottom','5px');
		$('#viewLessLink').hide();
		$('#viewAllLink').show();
	}
	
	var isDisplay = '${isMessageShow}';
	if(isDisplay.toString() == 'true'){
		$("#license_short_reminder").show();
	}
	
</script>

<style>
#nav {
	list-style: none;
	margin: 0px;
	padding: 0px;
}

#nav li {
	float: left;
	margin-right: 20px;
	font-size: 14px;
	font-weight: bold;
}

#nav li a {
	color: #333333;
	text-decoration: none
}

#nav li a:hover {
	color: #006699;
	text-decoration: none
}

#notification_li {
	position: relative
}

#notificationContainer {
	background-color: #fff;
	border: 1px solid rgba(100, 100, 100, .4);
	-webkit-box-shadow: 0 3px 8px rgba(0, 0, 0, .25);
	overflow: visible;
	position: absolute;
	top: 40px;
	margin-left: -170px;
	width: 400px;
	max-height: 366px;
	z-index: 999999;
	display: none;
}

#notificationContainer:before {
	content: '';
	display: block;
	position: absolute;
	width: 0;
	height: 0;
	color: transparent;
	border: 10px solid black;
	border-color: transparent transparent white;
	margin-top: -20px;
	margin-left: 188px;
}

#notificationTitle {
	z-index: 1000;
	font-weight: bold;
	padding: 8px;
	font-size: 13px;
	background-color: #FE7B26;
	width: 100%;
	color: white;
	border-bottom: 1px solid #dddddd;
}

#notificationsBody {
	font-size: 10px;
	padding: 10px 0px 10px 0px !important;
	height: 200px;
	max-height: 300px;
	overflow: hidden;
	overflow-x: hidden;
	margin-left: 5px;
	margin-right: 5px;
}

#notificationFooter {
	background-color: white;
	color: black;
	text-align: center;
	font-weight: bold;
	/* 		padding: 8px; */
	height: 25px;
	font-size: 12px;
	border-top: 1px solid #dddddd;
	border-bottom: 1px solid #dddddd;
}

#notification_count {
	padding: 3px 7px 3px 7px;
	background: #cc0000;
	color: #ffffff;
	font-weight: bold;
	margin-left: 77px;
	border-radius: 9px;
	position: absolute;
	margin-top: -11px;
	font-size: 11px;
}

.glyph-white-background {
	text-align: center;
	width: 20px;
	height: 15px;
	background-color: white;
	border-radius: 50%;
	color: #FE7B26;
}
</style>
