<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>

<html>

<jsp:include page="../../../common/newheader.jsp"></jsp:include>

<body class="skin-blue sidebar-mini">
<div class="wrapper"> 
    
    <!-- Header Start -->
     
    <jsp:include page="../../../common/newtopNavigationPanel.jsp"></jsp:include>
     	 
    <!-- Header End -->
    
    <jsp:include page="../../../common/newleftMenu.jsp"></jsp:include> 
    
    <!-- Content Wrapper. Contains page content -->
    <div class="content-wrapper"> 
        <!-- Content Header (Page header) -->
        <section class="content-header">
            <div class="fullwidth">
                <div style="padding:0 10px 0 4px;">
                    <div id="content-scroll-d" class="content-scroll"> 
                        
                        <!-- Content Wrapper. Contains page content Start -->
                        <div class="fullwidth">
                            <h4 style="margin-top:10px; margin-bottom:0px; font-size:11px;">
                            	<a href="home.html"><img src="img/tile-icon.png" class="vmiddle" alt="Title Icon"/></a> 
                            	<span class="spanBreadCrumb" style="line-height: 30px;">
                            		<strong>
                            		<a href="<%= ControllerConstants.INIT_SERVICE_MANAGER%>">
                            			Service Manager
                            		</a> &nbsp; /
                            		<a id="back" href="initDistributionConfig?serviceId=SVC003&serviceType=Distribution+Service&serviceStatus=INACTIVE">
                            		  Test Distribution Service - SVC003
                            		 </a> &nbsp; /
                            			 Ascii Plugin 
                            		 </strong>
                            	</span>
                            	<ol class="breadcrumb1 breadcrumb">
                                	<li><a href="#"><strong>Service Name</strong></a></li>
                                	<li>Test Distribution Service-SVC003</li>
                              	</ol>
                            </h4>
                            <div class="row">
                                <div class="col-xs-12">
                                    <div class="box  martop" style="border:none; box-shadow: none;"> 
                                        <!-- /.box-header -->
                                        <div class="box-body table-responsive no-padding">
                                            <div class="nav-tabs-custom"> 
                                                <!-- Tabs within a box -->
                                                <ul class="nav nav-tabs pull-right">
                                                    <li class="<c:if test="${(REQUEST_ACTION_TYPE eq 'DISTRIBUTION_PLUGIN_CONFIGURATION')}"><c:out value="active"></c:out></c:if>" onclick="showButtons('parser-config-a');" >
                                                    	<a href="#plugin-configuration" data-toggle="tab" id="plugin_configuration">
                                                    			<spring:message code="distribution.service.plugin.details.configuration.tab" ></spring:message>
														</a>
                                                    </li>
                                                    <li class="<c:if test="${(REQUEST_ACTION_TYPE eq 'DISTRIBUTION_PLUGIN_ATTRIBUTES')}"><c:out value="active" ></c:out></c:if>" onclick="showButtons('parser-attr-list-a');" >
                                                    	<a href="#plugin-attrList" data-toggle="tab" id="plugin_attributes">
															<spring:message code="distribution.service.plugin.details.attributes.tab" ></spring:message>
                                                    	</a>
                                                    </li>
                                                </ul>
                                                <div class="fullwidth tab-content no-padding">
                                                    <jsp:include page="pluginConfiguration.jsp" ></jsp:include>
                                                    <jsp:include page="pluginAttributes.jsp" ></jsp:include>
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
     		<jsp:include page="../../../common/newfooter.jsp"></jsp:include>
     	</div>
	</footer>
    <!-- Footer End --> 
</div>
<!-- ./wrapper --> 
</body>
</html> 
