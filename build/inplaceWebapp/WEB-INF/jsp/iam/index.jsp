<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<!doctype html>
<!--[if lt IE 7]>      <html class="no-js lt-ie9 lt-ie8 lt-ie7 lt-ie10"> <![endif]-->
<!--[if IE 7]>         <html class="no-js lt-ie9 lt-ie8 lt-ie10"> <![endif]-->
<!--[if IE 8]>         <html class="no-js lt-ie9 lt-ie10"> <![endif]-->
<!--[if IE 9]>         <html class="no-js lt-ie10"> <![endif]-->
<html>
	<jsp:include page="../common/newheader.jsp" ></jsp:include>
	
	<!-- Page-specific Plugin CSS: -->
    <link rel="stylesheet" href="styles/vendor/jquery.pnotify.default.css">
	<link rel="stylesheet" href="styles/vendor/select2/select2.css">
	
   <body class="skin-blue sidebar-mini">
   <div class="wrapper"> 
    	
        <jsp:include page="../common/newtopNavigationPanel.jsp"></jsp:include>
        <jsp:include page="../common/newleftMenu.jsp"></jsp:include>
        <div class="content-wrapper">
        
        <section class="content-header">
        <div class="fullwidth">
        <div id="content-scroll-d" class="content-scroll">
        <div class="fullwidth">
        <h4 style="margin-top:10px; margin-bottom:0px; font-size:11px;">
        <a href="home"><img id="home_lnk" src="img/tile-icon.png" class="vmiddle" alt="Title Icon"/></a>
        	<span class="spanBreadCrumb" style="line-height: 30px;"><strong><spring:message code="index.page.heading" ></spring:message></strong></span>
        	</h4>
        	<jsp:include page="../common/responseMsg.jsp" ></jsp:include>
        	<%-- MED-10060, 11146 --%>
        	<%
        	String kubernetes_env = System.getenv("KUBERNETES_ENV");
        	String grafana_dashborad_url = null;
        	boolean is_kubernetes = false;
        	if (kubernetes_env !=null && kubernetes_env.equalsIgnoreCase("true")){
        		is_kubernetes = true;
        		grafana_dashborad_url = System.getenv("GRAFANA_DASHBOARD_URL");        		
        	}
        	%>
            <% if(!is_kubernetes) { %>
            	<%-- <jsp:include page="licenseDetails.jsp" ></jsp:include> --%>
            <% }else if(grafana_dashborad_url != null){ %>
				<%-- <iframe  class="fullwidth" src="<%=grafana_dashborad_url%>" height="750" style="border:none;"></iframe> --%>
			<% } %>
            </br>
            <%-- <jsp:include page="../license/licenseUtilizationBarChart.jsp"></jsp:include> --%>
          </div>
          </div>
          </div>
          

        </section>
        </div>
	        <%-- <div>
	        	
	        </div> --%>
        </div>
 		<footer class="main-footer positfix">      
			<div class="fooinn">
        		<div class="fullwidth">
        
					<jsp:include page="../common/newfooter.jsp"></jsp:include>
				</div>
			</div>
		</footer>			
    </body>
    <%@ include file="migration-popup.jsp" %> 
</html>
