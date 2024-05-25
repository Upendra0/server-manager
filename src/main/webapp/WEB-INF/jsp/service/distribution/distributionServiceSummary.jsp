<%@page import="com.elitecore.sm.common.constants.BaseConstants"%>
<%@page import="com.elitecore.sm.util.MapCache"%>
<%@page import="com.elitecore.sm.common.constants.SystemParametersConstant"%>
<%@page import="com.elitecore.sm.common.constants.EngineConstants"%>
<%@page import="org.apache.catalina.connector.Request"%>
<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="/WEB-INF/ShowErrorTag.tld" prefix="elitecoreError"%>

<script>
var jsLocaleMsg = {};
jsLocaleMsg.serviceId = "<spring:message code='serviceManagement.grid.column.service.id'></spring:message>";
jsLocaleMsg.serviceNm = "<spring:message code='serviceManagement.grid.column.service.name'></spring:message>";
jsLocaleMsg.moduleName="<spring:message code='server.instance.import.header.label.module.name'></spring:message>";
jsLocaleMsg.entityName = "<spring:message code='server.instance.import.header.label.entity.name'></spring:message>";
jsLocaleMsg.propertyName = "<spring:message code='server.instance.import.header.label.property.name'></spring:message>";
jsLocaleMsg.propertyValue = "<spring:message code='server.instance.import.header.label.property.value'></spring:message>";
jsLocaleMsg.errorMessage = "<spring:message code='server.instance.import.header.label.error.message'></spring:message>";
jsLocaleMsg.LineNo = "<spring:message code='server.instance.import.header.label.line.number'></spring:message>";
</script>
<div class="fullwidth mtop10">

		<!-- Counter section start here -->
			<jsp:include page="../serviceCounterSummary.jsp"></jsp:include>
		<!-- Counter section end here -->
	
		
		<!-- Jquery Driver Grid start here -->
		<div class="fullwidth">
			<div class="title2">
       			<spring:message code="distribution.service.summary.driver.grid.caption"></spring:message>
      		</div>
      	</div>	
		
		<div class="box-body table-responsive no-padding box">
		<table class="table table-hover" id="drivergrid"></table>
           	<div id="driverGridPagingDiv"></div> 
         </div>
		<!-- Jquery Driver Grid end here -->	
		
		
		<!-- Jquery Agent Grid start here -->
		<sec:authorize access="hasAnyAuthority('SYSTEM_AGENT_CONFIGURATION')">
				<jsp:include page="../serviceAgentList.jsp"></jsp:include>
		</sec:authorize>
		<!-- Jquery Agent Grid end here -->	

			
		<a href="#divChangeDriverStatus" class="fancybox" style="display: none;" id="updateDriverStatus">#</a>		
		<!-- Driver status enable disable pop-up code start here  -->
			<div id="divChangeDriverStatus" style=" width:100%; display: none;" >
			    <div class="modal-content">
			        <div class="modal-header padding10">
			            <h4 class="modal-title"><spring:message code="driver.change.status.header"></spring:message></h4>
			        </div>
			        <div class="modal-body padding10 inline-form">
			        	<span id="deleteDriverResponseMsg" style="display:none;">
			        		<jsp:include page="../../common/responseMsgPopUp.jsp" ></jsp:include>
			        	</span>
				        <p id="active-driver-warning" style="display:none;">
				       		 <spring:message code="driver.change.status.active.message" ></spring:message>
				        </p>
				        <p id="inactive-driver-warning" style="display:none;">
				       		 <spring:message code="driver.change.status.inactive.message" ></spring:message>
				        </p>
			        </div>
			        <sec:authorize access="hasAuthority('UPDATE_DISTRIBUTION_DRIVER')">
				        <div id="inactive-driver-grid-div" class="modal-footer padding10">
				            <button type="button" class="btn btn-grey btn-xs " onclick="changeDriverStatus();"><spring:message code="btn.label.yes"></spring:message></button>
				            <button type="button" class="btn btn-grey btn-xs " data-dismiss="modal" onclick="setDefaultStatus();"><spring:message code="btn.label.no"></spring:message></button>
				        </div>
			        </sec:authorize>
			    </div>
			    <!-- /.modal-content --> 
			</div>
		<!-- Driver status enable disable popup code end here -->
		
		<!-- Plug-in List code start here  -->
		<div id="driverPluginList" style=" width:100%; display: none;" >
		     <div class="modal-content">
		        <div class="modal-header padding10">
		            <span class="modal-title">
		            		<spring:message code="distribution.service.summary.driver.plugin.list.heading"></spring:message>
		            </span>
		        </div>
		        <div class="modal-body padding10 inline-form" >
					<div class=fullwidth>
		        		<jsp:include page="../../common/responseMsgPopUp.jsp" ></jsp:include>
		        	</div>
		        	 <div id="plugin_list_details">
		        	 
		        	 </div>
		        </div>
		        <div id="start-buttons-div" class="modal-footer padding10">
		            <button type="button" class="btn btn-grey btn-xs " data-dismiss="modal" onclick="closeFancyBox();"><spring:message code="btn.label.close"></spring:message></button>
		        </div>
		        
		    </div>
		</div>
		<a href="#driverPluginList" class="fancybox" style="display: none;" id="driver_plugin_list">#</a>
		<!-- Plug-in List code end here  -->
		
		<form id="distribution-service-composer-config_from_plugin_list" method="GET" action="<%=ControllerConstants.INIT_COMPOSER_CONFIGURATION%>">
			<input type="hidden" id="serviceId" name="serviceId" value="${serviceId}"/>
			<input type="hidden" id="serviceName" name="serviceName" value="${serviceName}"/>
			<input type="hidden" name="instanceId"  id="instanceId" value="${instanceId}"/>
			<input type="hidden" id="serviceInstanceId" name="serviceInstanceId" value="${instanceId}">
			<input type="hidden" name="serviceType"  id="serviceType" value='<%=EngineConstants.DISTRIBUTION_SERVICE%>'/>
			<input type="hidden" id="plugInId" name="plugInId"/>
			<input type="hidden" id="plugInName" name="plugInName"/> 
			<input type="hidden" id="plugInType" name="plugInType"/>
			<input type="hidden" id="driverIdsummary" name="driverId"/>
			<input type="hidden" id="driverNamesummary" name="driverName"/>
			<input type="hidden" id="driverTypeAliassummary" name="driverTypeAlias"/>
		</form>	
		<form id="plugin_details_form" action="<%= ControllerConstants.INIT_DITRIBUTION_PLUGIN_MANAGER%>" method="get">
		</form>
		
		<!-- Distribution service export functionality -->
		<form action="<%=ControllerConstants.EXPORT_SERVICE_INSTANCE_CONFIG%>" id="export-service-instance-config-form" method="POST">
        	<input type="hidden" id="exportServiceInstanceId" name="exportServiceInstanceId"  value=""/>
        	<input type="hidden" id="isExportForDelete" name="isExportForDelete"  value=""/>
        	<input type="hidden" id="<%=BaseConstants.REQUEST_ACTION_TYPE %>" name="<%=BaseConstants.REQUEST_ACTION_TYPE %>" value="<%=BaseConstants.DISTRIBUTION_SERVICE_SUMMARY%>"/>
       </form>
		
</div>
<script type="text/javascript">
	$(document).ready(function() {
		
		if(currentRequestAction == 'DISTRIBUTION_SERVICE_SUMMARY'){
					
			var currentServiceId = '${serviceId}';
			<sec:authorize access="hasAnyAuthority('VIEW_DISTRIBUTION_DRIVER')">
				loadDistributionDriverGrid('<%= ControllerConstants.GET_DISTRIBUTION_DRIVER_LIST%>','<%= MapCache.getConfigValueAsInteger(SystemParametersConstant.TOTAL_ROWS_TO_DISPLAY_IN_GRID, 10) %>',currentServiceId);
			</sec:authorize>
			<sec:authorize access="hasAnyAuthority('SYSTEM_AGENT_CONFIGURATION')">
				loadJqueryAgentGrid();
			</sec:authorize>
			
			getServiceCounterDetails('<%=ControllerConstants.GET_COLLECTION_SERVICE_COUNTER_STATUS%>','${serviceId}','<%=EngineConstants.DISTRIBUTION_SERVICE%>','ONLOAD');
		}
	});
	
		
	function viewDistributionPluginDetails(){
		$("#plugin_details_form").submit();
	}

</script>	
