<%@page import="com.elitecore.sm.common.constants.SystemParametersConstant"%>
<%@page import="com.elitecore.sm.common.constants.BaseConstants"%>
<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@taglib uri="/WEB-INF/ShowErrorTag.tld" prefix="error" %>

<%@ taglib tagdir="/WEB-INF/tags" prefix="elitecore" %>
<html>
<jsp:include page="../common/newheader.jsp" ></jsp:include>
<body class="skin-blue sidebar-mini sidebar-collapse">
<div class="wrapper"> 
<jsp:include page="../common/newtopNavigationPanel.jsp"></jsp:include>
<jsp:include page="../common/newleftMenu.jsp"></jsp:include>
<div class="content-wrapper">	
			<section class="content-header">
				<div class="fullwidth">
				<div style="padding:0 10px 0 4px;">
				<div id="content-scroll-d" class="content-scroll">
				
				<div class="fullwidth">
                	<h4 style="margin-top:10px; margin-bottom:0px; font-size:11px;">
                	<a href="home.html"><img src="img/tile-icon.png" class="vmiddle" alt="Title Icon"/></a> 
                                    <span class="spanBreadCrumb" style="line-height: 30px;font-weight: bold;">
                                   		<strong>&nbsp;
                                   			<spring:message code="roamingconfiguration.page.heading" ></spring:message>
                                   		</strong>&nbsp;
                                    </span>
                    </h4>
                    <jsp:include page="../common/responseMsg.jsp" ></jsp:include>
                    <sec:authorize access="hasAnyAuthority('VIEW_ROAMING_CONFIGURATION') or hasAnyAuthority('MODIFY_ROAMING_CONFIGURATION')">
          
                     <div class="row">
                		<div class="col-xs-12">
                  		<div class="box martop" style="border:none; box-shadow: none;">
                    	<!-- /.box-header -->
                    	<div class="box-body table-responsive no-padding">
                    	
	                    <div class="nav-tabs-custom">
                    
                    	<!-- Tabs within a box -->
                    	  <ul class="nav nav-tabs">
                    		
                    		<li class="<c:if test="${(REQUEST_ACTION_TYPE eq 'ROAMING_PARAMETER') }"><c:out value="active"></c:out></c:if>" onclick ="loadRoamingParameterTab()">
                      		<a href="#genGroup-tab1" data-toggle="tab" id="genGroup-tab-a1" ><spring:message code="roamingconfiguration.roamingparameter.tab" ></spring:message></a>
                      		</li>
                    		
                    		
                      		<%-- <li class="<c:if test="${(REQUEST_ACTION_TYPE eq 'HOST_CONFIGURATION') }"><c:out value="active"></c:out></c:if>" onclick ="loadHostConfigurationTab()">
                      		<a href="#genGroup-tab" data-toggle="tab" id="genGroup-tab-a" ><spring:message code="roamingconfiguration.host.configuration.tab" ></spring:message></a>
                      		</li> --%>
                      		
                      		<li class="<c:if test="${(REQUEST_ACTION_TYPE eq 'RAP_TAP_CONFIGURATION') or (REQUEST_ACTION_TYPE eq 'TEST_SIM_MANAGEMENT') or (REQUEST_ACTION_TYPE eq 'FILE_SEQUENCE_MANAGEMENT')}"><c:out value="active"></c:out></c:if>"onclick ="loadRAPTAPConfigurationTab()" >
                      		<a href="#genGroup-tab12" data-toggle="tab" id="genGroup-tab-a" ><spring:message code="roamingconfiguration.tap.rap.configuration.tab" ></spring:message></a>
                      		</li>
                      		
                    	  </ul> 
                    	  <div class="fullwidth tab-content no-padding">
                    	  	
                    	  	<div class="tab-pane <c:if test="${(REQUEST_ACTION_TYPE eq 'HOST_CONFIGURATION') }"><c:out value="active"></c:out></c:if>" id="genGroup-tab" >
                    	  		<c:if test="${(REQUEST_ACTION_TYPE eq 'HOST_CONFIGURATION') }"><jsp:include page="hostConfiguration.jsp" ></jsp:include></c:if>
                    	  	</div>
                    	  	<div class="tab-pane <c:if test="${(REQUEST_ACTION_TYPE eq 'RAP_TAP_CONFIGURATION') or (REQUEST_ACTION_TYPE eq 'TEST_SIM_MANAGEMENT') or (REQUEST_ACTION_TYPE eq 'FILE_SEQUENCE_MANAGEMENT')}"><c:out value="active"></c:out></c:if>" id="genGroup-tab12" >
                    	  		<c:if test="${(REQUEST_ACTION_TYPE eq 'RAP_TAP_CONFIGURATION') or (REQUEST_ACTION_TYPE eq 'TEST_SIM_MANAGEMENT') or (REQUEST_ACTION_TYPE eq 'FILE_SEQUENCE_MANAGEMENT') }"><jsp:include page="raptapConfiguration.jsp" ></jsp:include></c:if>
                    	  	</div>
                    	  	<div class="tab-pane <c:if test="${(REQUEST_ACTION_TYPE eq 'ROAMING_PARAMETER') }"><c:out value="active"></c:out></c:if>" id="genGroup-tab1" >
                    	  		<c:if test="${(REQUEST_ACTION_TYPE eq 'ROAMING_PARAMETER') }"><jsp:include page="roamingParameter.jsp" ></jsp:include></c:if>
                    	  	</div>
						  	
                    	  		
						  </div>
					    </div>
					  	</div>
					 </div>
					 </div>
					 </div>
                    </sec:authorize>
                </div>
                </div>
                </div>
                 </div>
                </section>
                </div>
             <script type="text/javascript">
		           	
		           	
		           	
		           	function loadHostConfigurationTab(){
		       			$('#loadHostConfigurationForm').submit();	
		       		}
		           	function loadRAPTAPConfigurationTab(){
		           		$('#loadRAPTAPConfigurationForm').submit();
		           	}
		           	function loadRoamingParameterTab(){
		           		$('#loadRoamingParameterForm').submit();
		           	}
		           	
		           	</script>
		           	
<%-- <jsp:include page="../common/newfooter.jsp"></jsp:include> --%>



	<div style="display: hidden">
		<form id="loadRoamingParameterForm" action ="<%= ControllerConstants.INIT_ROAMING_CONFIGURATION %>" method="GET">
			<%-- <input type="hidden" name="<%= BaseConstants.REQUEST_ACTION_TYPE %>"  id="<%= BaseConstants.REQUEST_ACTION_TYPE %>" value="<%= BaseConstants.ROAMING_PARAMETER%>" /> --%>
		</form>
	</div>

<div style="display: hidden">
		<form id="loadHostConfigurationForm" action ="<%= ControllerConstants.INIT_ROAMING_CONFIGURATION %>" method="GET">
			<input type="hidden" name="<%= BaseConstants.REQUEST_ACTION_TYPE %>"  id="<%= BaseConstants.HOST_CONFIGURATION %>" value="<%= BaseConstants.HOST_CONFIGURATION%>" />
		</form>
	</div>
	
	<div style="display: hidden">
		<form id="loadRAPTAPConfigurationForm" action ="<%= ControllerConstants.INIT_ROAMING_CONFIGURATION %>" method="GET">
			<input type="hidden" name="<%= BaseConstants.REQUEST_ACTION_TYPE %>"  id="<%= BaseConstants.RAP_TAP_CONFIGURATION %>" value="<%= BaseConstants.RAP_TAP_CONFIGURATION%>" />
		</form>
	</div>

     	</div>
</body>


</html>
