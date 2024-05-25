<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<!DOCTYPE html>

<html>

<jsp:include page="../common/newheader.jsp"></jsp:include>

<body class="skin-blue sidebar-mini">
<div class="wrapper"> 
    
    <!-- Header Start -->
     
    <jsp:include page="../common/newtopNavigationPanel.jsp"></jsp:include>
     	 
    <!-- Header End -->
    
    <jsp:include page="../common/newleftMenu.jsp"></jsp:include> 
    
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
                            			<a href="<%= ControllerConstants.INIT_SERVER_MANAGER%>">
                            				<spring:message code="leftmenu.label.server.manager" ></spring:message>
                            			</a> &nbsp; / &nbsp;
                            			<a href="#" onclick="viewServerInstanceDashboard();">${instance} &nbsp; : &nbsp; ${host} &nbsp; - &nbsp; ${port}</a>
                            		</strong>
                            	</span> 
                            	<ol class="breadcrumb1 breadcrumb">
                                	<li><a href="#"><strong>Service Name</strong></a></li>
                                	<li>Consolidation Service For Gujarat</li>
                              	</ol>
                            </h4>
                            
                            <jsp:include page="../common/responseMsg.jsp" ></jsp:include>
                            <div class="row">
                                <div class="col-xs-12">
                                    <div class="box  martop" style="border:none; box-shadow: none;"> 
                                        <!-- /.box-header -->
                                        <div class="box-body table-responsive no-padding">
                                            <div class="nav-tabs-custom"> 
                                                <!-- Tabs within a box -->
                                                <ul class="nav nav-tabs pull-right">
                                                    <li class="<c:if test="${(REQUEST_ACTION_TYPE eq 'CONSOLIDATION_DEFINITION')}"><c:out value="active"></c:out></c:if>" onclick="showButtons('consoli-def-a');" >
                                                    	<a href="#conslDefi" data-toggle="tab" id="consoli-def-a"><spring:message code="consolidation.service.consoli.defi.tab.header"></spring:message></a>
                                                    </li>
                                                    <li class="<c:if test="${(REQUEST_ACTION_TYPE eq 'CONSOLIDATION_SERVICE_MAPPING')}"><c:out value="active" ></c:out></c:if>" onclick="showButtons('service-map-a');" >
                                                    	<a href="#serviceMap" data-toggle="tab" id="service-map-a"><spring:message code="consolidation.service.service.map.tab.header"></spring:message></a>
                                                    </li>
                                                </ul>
                                                <div class="fullwidth tab-content no-padding">
                                                
                                                    <jsp:include page="consolidationDefinition.jsp" ></jsp:include>
                                                    <jsp:include page="serviceMapping.jsp" ></jsp:include>
                                                    
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
        	<div class="fullwidth">
       			<div class="padleft-right" id="consoleDefDiv" style="display: none;">
<%--        		<sec:authorize access="hasAuthority('UPDATE_SERVER_AND_SERVER_INSTANCES')"> --%>
					
<%--                </sec:authorize> --%>
				</div>	
            	<div class="padleft-right" id="serviceMapDiv" style="display: none;">
				</div>
        	</div>
     		<jsp:include page="../common/newfooter.jsp"></jsp:include>
     	</div>
	</footer>
    <!-- Footer End --> 
    
</div>
<!-- ./wrapper --> 


<form id="instance_form" method="POST" action="<%= ControllerConstants.INIT_UPDATE_SERVER_INSTANCE %>">
	<input type="hidden" id="serverInstanceId" name="serverInstanceId" value="${instanceId}">
</form>

<script>
	function showHideButtonBasedOnTabsSelected(tabType){
		$("#consoleDefDiv").hide();
		$("#serviceMapDiv").hide();
		
		if(tabType == 'CONSOLIDATION_DEFINITION'){
			$("#consoleDefDiv").show();
		}else if(tabType == 'SERVICE_MAPPING'){
			$("#serviceMapDiv").show();
		}
	}
	
	function showButtons(id)
	{
		if(id == 'consoli-def-a'){
			showHideButtonBasedOnTabsSelected('CONSOLIDATION_DEFINITION');
		}else if(id == 'service-map-a'){
			showHideButtonBasedOnTabsSelected('SERVICE_MAPPING');
		}
	}
	
	$(document).ready(function() {
		var activeTab = $(".nav-tabs li.active a");
		var id = activeTab.attr("id");
		showButtons(id);
	});
	
	function viewServerInstanceDashboard(){
		$('#instance_form').submit();
	}

</script>
</body>
</html>
