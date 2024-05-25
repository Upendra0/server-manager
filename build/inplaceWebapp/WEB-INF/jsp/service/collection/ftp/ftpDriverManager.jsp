<%@page import="com.elitecore.sm.common.constants.EngineConstants"%>
<%@page import="javax.persistence.EnumType"%>
<%@page import="com.elitecore.sm.common.constants.BaseConstants"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<%@page import="com.elitecore.sm.common.constants.BaseConstants"%>
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
                            		<span class="spanBreadCrumb" style="line-height: 30px;"> 
										<strong><a href="<%= ControllerConstants.INIT_SERVICE_MANAGER%>"><spring:message code="collectionService.leftmenu.home" ></spring:message></a>&nbsp;/ 
										<a href="#" onclick="viewService()">
										${serviceName}&nbsp;-&nbsp;${serviceInstanceId}</a>
										</strong>
									</span>
                            		</strong>
                            	</span> 
                            	<ol class="breadcrumb1 breadcrumb mtop10">
                                	<li><a href="#"><strong><spring:message code="collectionService.driver.add.driver.name"></spring:message></strong></a></li>
                                	<li>${driverName}</li>
                              	</ol>
                            </h4>
                            
                            <jsp:include page="../../../common/responseMsg.jsp" ></jsp:include>
                            <div class="row">
                                <div class="col-xs-12">
                                    <div class="box  martop" style="border:none; box-shadow: none;"> 
                                        <!-- /.box-header -->
                                        <div class="box-body table-responsive no-padding">
                                            <div class="nav-tabs-custom"> 
                                                <!-- Tabs within a box -->
                                                <ul class="nav nav-tabs pull-right">
                                                	<sec:authorize access="hasAnyAuthority('VIEW_COLLECTION_DRIVER')">
                                                    <li class="<c:if test="${(REQUEST_ACTION_TYPE eq 'FTP_CONFIGURATION')}"><c:out value="active"></c:out></c:if>" onclick="loadDriverConfigPage(); showButtons('ftp-config-a');" >
                                                    
                                                    	<c:choose>
														  <c:when test="${(driverTypeAlias eq 'FTP_COLLECTION_DRIVER')}">
																<a href="#ftpConfig" data-toggle="tab" id="ftp-config-a"><spring:message code="ftp.driver.mgmt.config.tab.header"></spring:message></a>
														  </c:when>
														  <c:when test="${(driverTypeAlias eq 'SFTP_COLLECTION_DRIVER')}">
																<a href="#ftpConfig" data-toggle="tab" id="ftp-config-a"><spring:message code="sftp.driver.mgmt.config.tab.header"></spring:message></a>
														  </c:when>
														  <c:otherwise>
														    	<a href="#ftpConfig" data-toggle="tab" id="ftp-config-a"><spring:message code="local.driver.mgmt.config.tab.header"></spring:message></a>
														  </c:otherwise>
														</c:choose>
                                                    	
                                                    	
                                                    </li>
                                                    </sec:authorize>
                                                    <sec:authorize access="hasAnyAuthority('VIEW_PATHLIST')">
                                                    
                                                    <li class="<c:if test="${(REQUEST_ACTION_TYPE eq 'PATH_LIST_CONFIGURATION')}"><c:out value="active" ></c:out></c:if>" onclick="loadPathListConfigPage(); showButtons('path-list-config-a');" >
                                                    	<a href="#pathListConfig" data-toggle="tab" id="path-list-config-a"><spring:message code="ftp.driver.mgmt.pathlist.config.tab.header"></spring:message></a>
                                                    </li>
                                                    </sec:authorize>
                                                </ul>
                                                <div class="fullwidth tab-content no-padding">
                                                	<c:if test="${(REQUEST_ACTION_TYPE eq 'FTP_CONFIGURATION')}">
                                                    <jsp:include page="ftpConfiguration.jsp" ></jsp:include>
                                                    </c:if>
                                                    <c:if test="${(REQUEST_ACTION_TYPE eq 'PATH_LIST_CONFIGURATION')}">
                                                    <jsp:include page="ftpPathlistConfiguration.jsp" ></jsp:include>
                                                    </c:if>
                                                    
                                                </div>
                                            </div>
                                        </div>
                                        
                                        <!-- /.box-body --> 
                                    </div>
                                    <!-- /.box --> 
                                    
                                </div>
                            </div>
                            <div style="display: hidden">
		<form id="loadParsingConfigForm" method="POST">
			<input type="hidden" id="driverId" name="driverId" value="${driverId}"/>
    		<input type="hidden" id="driverName" name="driverName"/>
    		<input type="hidden" id="driverTypeAlias" name="driverTypeAlias" value="${driverTypeAlias}"/>
    		<input type="hidden" id="serviceId" name="serviceId" value="${serviceId}"/>
    		<input type="hidden" id="serviceName" name="serviceName" value="${serviceName}"/>
    		<input type="hidden" id="serverInstanceId" name="serverInstanceId" value="${serverInstanceId}"/>
    		<input type="hidden" id="serviceDBStats" name="serviceDBStats" value="${serviceDbStats}"/>
    		<input type="hidden" id="serviceInstanceId" name="serviceInstanceId" value="${serviceInstanceId}">
    		<input type="hidden" id="forDuplicatEnabled" name="forDuplicatEnabled" value="${forDuplicatEnabled}"/>
		</form>
	</div>
	<div style="display: none;">
			<form id="service_form" method="GET" action="<%= ControllerConstants.INIT_UPDATE_SERVICE %>">
				<input type="hidden" id="serviceId" name="serviceId" value="${serviceId}">
				<input type="hidden" id="serviceType" name="serviceType" value='<%=EngineConstants.COLLECTION_SERVICE%>'>
				<input type="hidden" id="serviceName" name="serviceName" value="${serviceName}">
				<input type="hidden" id="instanceId" name="instanceId" value="${serverInstanceId}">
			</form>
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
        	<div class="fullwidth">
       			<div class="padleft-right" id="ftpConfigDiv" style="display: none;">
        		<sec:authorize access="hasAuthority('UPDATE_COLLECTION_DRIVER')"> 
					<button id="update_btn" class="btn btn-grey btn-xs " type="button" tabindex="19" onclick="updateConfiguration();"><spring:message code="btn.label.update"></spring:message></button>
                   	<button id="reset_btn" class="btn btn-grey btn-xs" type="button" tabindex="20" onclick="resetFTPConfiguration();"><spring:message code="btn.label.reset"></spring:message></button>
                </sec:authorize>
                   	<button id="cancel_btn" class="btn btn-grey btn-xs" type="button" tabindex="21" onclick="viewService();"><spring:message code="btn.label.cancel"></spring:message></button>
                 <div class="pull-right" style="font-size:10px;"><i class="fa fa-square" style="font-size: 9px"></i>&nbsp;&nbsp;<spring:message code="restart.operation.require.message" ></spring:message> </div>
				</div>	
            	<div class="padleft-right" id="pathlistConfigDiv" style="display: none;">
				</div>
				
        	</div>
        	
	        	<script type="text/javascript">
		        	$(document).ready(function() {
		        		var activeTab = $(".nav-tabs li.active a");
		        		var id = activeTab.attr("id");
		        		showButtons(id);
		        	});
	        	</script>
     		<jsp:include page="../../../common/newfooter.jsp"></jsp:include>
     	</div>
	</footer>
    <!-- Footer End --> 
    
</div>
<!-- ./wrapper --> 

<script>
	function showHideButtonBasedOnTabsSelected(tabType){
		$("#ftpConfigDiv").hide();
		$("#pathlistConfigDiv").hide();
		
		if(tabType == 'FTP_CONFIGURATION'){
			<sec:authorize access="hasAuthority('UPDATE_COLLECTION_DRIVER')"> 
				$("#ftpConfigDiv").show();
			</sec:authorize>
		}else if(tabType == 'PATH_LIST_CONFIG'){
			<sec:authorize access="hasAuthority('UPDATE_PATHLIST')"> 
				$("#pathlistConfigDiv").show();
			</sec:authorize>
		}
	}
	
	function showButtons(id)
	{
		if(id == 'ftp-config-a'){
			showHideButtonBasedOnTabsSelected('FTP_CONFIGURATION');
		}else if(id == 'path-list-config-a'){
			showHideButtonBasedOnTabsSelected('PATH_LIST_CONFIG');
		}
	}
	
	function loadPathListConfigPage(){

		$("#driverName").val('${driverName}');
		$("#loadParsingConfigForm").attr("action","<%= ControllerConstants.INIT_PATHLIST_CONFIGURATION%>");
		$("#loadParsingConfigForm").submit();
		
	}
	
	function loadDriverConfigPage(){
		$("#loadParsingConfigForm").attr("action","<%= ControllerConstants.INIT_DRIVER_CONFIGURATION%>");
		$("#loadParsingConfigForm").submit();
		
	}
	
	function viewService(){
		$("#service_form").submit();
	}

</script>
</body>
</html>
