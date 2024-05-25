<%@page import="com.elitecore.sm.common.constants.BaseConstants"%>
<%@page import="com.elitecore.sm.common.constants.LicenseConstants"%>
<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<%@page import="com.elitecore.sm.util.MapCache"%>
<%@page import="com.elitecore.sm.common.constants.SystemParametersConstant"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ page contentType="text/html; charset=UTF-8" %>
<!-- New Left Menu Code Starts Here -->        
 <!-- sidebar Start -->
<style>
#leftSidebar{
    position: absolute;
    overflow: auto;
} 
</style>
      <aside class="main-sidebar"> 
         
        <!-- sidebar: style can be found in sidebar.less -->
        <section class="sidebar" id="leftSidebar"> 
            
            <!-- Sidebar Menu -->
            <ul class="sidebar-menu">
                <li class="header"  style="display:none;">HEADER</li>
                
                <%  String user =(String) session.getAttribute("userName"); %>
                
				<%-- <li class="<c:if test="${(REQUEST_ACTION_TYPE eq 'DASHBOARD')}"><c:out value="active"></c:out></c:if>">
					<a	href="<%=ControllerConstants.INIT_DASHBOARD_MANAGER%>"> 
						<img src="img/Dashboard.png" width="20px" height="20px"/> 
						<span class="nav-text">
						 	<spring:message code="leftmenu.label.dashboard" ></spring:message>
						</span>
					</a>
				</li>
				<li class="<c:if test="${(REQUEST_ACTION_TYPE eq 'WORKFLOW_MANAGEMENT')}"><c:out value="active"></c:out></c:if>"> 
				 			<a	href="<%=ControllerConstants.INIT_WORKFLOW_MANAGER%>">  
							<img src="img/Work_flow_Menu.png" width="20px" height="20px"/>  
							<span class="nav-text"> 
						 	<spring:message code="leftmenu.label.workflow" ></spring:message> 
						</span> 
					</a> 
				</li> --%>
				<%if(!BaseConstants.PROFILE_ADMIN_USERNAME.equalsIgnoreCase(user) && !BaseConstants.MODULE_ADMIN_USERNAME.equalsIgnoreCase(user)){%>
				<sec:authorize access="hasAnyAuthority('SERVER_MANAGER_MENU_VIEW')">
	                <li class="<c:if test="${(REQUEST_ACTION_TYPE eq 'SERVER_MANAGEMENT') or (REQUEST_ACTION_TYPE eq 'CREATE_SERVER')}"><c:out value="active"></c:out></c:if>">
	                    <a id="init_server_manager" href="<%= ControllerConstants.INIT_SERVER_MANAGER %>">
	                    	<img src="img/Server-Manager-Menu.png" width="20px" height="20px"/>
                        	<span>
	                            <spring:message code="leftmenu.label.server.manager" ></spring:message>
    	                    </span>
	                    </a>
	                </li>
                </sec:authorize>
				<sec:authorize access="hasAnyAuthority('SERVICE_MANAGER_MENU_VIEW')">
	                <li class="<c:if test="${(REQUEST_ACTION_TYPE eq 'SERVICE_MANAGEMENT') or (REQUEST_ACTION_TYPE eq 'CREATE_SERVICE')}"><c:out value="active"></c:out></c:if>">
	                    <a id="init_service_manager" href="<%= ControllerConstants.INIT_SERVICE_MANAGER%>">
	                    	<img src="img/Service-Manager_menu.png" width="20px" height="20px"/>
                        	<span>
	                            <spring:message code="leftmenu.label.service.manager" ></spring:message>
    	                    </span>
    	                    
	                    </a>
	                </li>
                </sec:authorize>
                
                <sec:authorize access="hasAnyAuthority('ROAMING_CONFIGURATION_MENU_VIEW')">
	                <li class="<c:if test="${(REQUEST_ACTION_TYPE eq 'ROAMING_PARAMETER')or (REQUEST_ACTION_TYPE eq 'HOST_CONFIGURATION') or (REQUEST_ACTION_TYPE eq 'RAP_TAP_CONFIGURATION')or (REQUEST_ACTION_TYPE eq 'TEST_SIM_MANAGEMENT') or (REQUEST_ACTION_TYPE eq 'FILE_SEQUENCE_MANAGEMENT')}"><c:out value="active"></c:out></c:if>">
	                    <a id="init_roaming_manager" href="<%= ControllerConstants.INIT_ROAMING_CONFIGURATION%>">	                    	
	                    	<img src="img/Roaming_Config_Menu_w.png" width="20px" height="20px"/>
                        	<span>
	                            <spring:message code="leftmenu.lable.roaming.configuration" ></spring:message>
    	                    </span>
    	                    
	                    </a>
	                </li>
                </sec:authorize>
                
                
                
				<sec:authorize access="hasAnyAuthority('STAFF_MANAGER_MENU_VIEW')">
	                <li class="<c:if test="${(REQUEST_ACTION_TYPE eq 'STAFF_MANAGEMENT') or (REQUEST_ACTION_TYPE eq 'ACCESS_GROUP_MANAGEMENT')}"><c:out value="active"></c:out></c:if>">
	                    <a id="staff_manager_lnk" href="#" onclick="loadLefMenuEvent('form-left-menu','<%=ControllerConstants.STAFF_MANAGER%>','GET','','');">

	                    	<img src="img/Staff_manager_menu.png" width="20px" height="20px"/>
                        	<span>
	                            <spring:message code="leftmenu.label.staff.manager" ></spring:message>
    	                    </span>
	                    </a>
	                </li>
                </sec:authorize>
				<sec:authorize access="hasAnyAuthority('VIEW_SYSTEM_PARAMETER')">
	                <li class="<c:if test="${(REQUEST_ACTION_TYPE eq 'General Parameters')}"><c:out value="active"></c:out></c:if>">
	                    <a id="system_manager_lnk" href="<%= ControllerConstants.INIT_ALL_SYSTEM_PARAMETER %>">
	                    	<img src="img/system_manager.png" width="20px" height="20px"/>
                        	<span>
	                            <spring:message code="leftmenu.label.system.manager" ></spring:message>
    	                    </span>
	                    </a>
	                </li>
                </sec:authorize>
                <sec:authorize access="hasAnyAuthority('DEVICE_ATTRIBUTE_MANAGER_MENU_VIEW')">
	                <li class="<c:if test="${(REQUEST_ACTION_TYPE eq 'DEVICE_MANAGEMENT')}"><c:out value="active"></c:out></c:if>">
	                    <a id="devicemgmt_lnk" href="#" onclick="loadLefMenuEvent('form-left-menu','<%=ControllerConstants.INIT_DEVICE_MANAGER%>','POST','DEVICE_MANAGEMENT','');">
	                    	<img src="img/device_manager_menu.png" width="20px" height="20px"/>
                        	<span>
	                            <spring:message code="leftmenu.label.device.configuration" ></spring:message>
    	                    </span>
	                    </a>
	                </li>
                </sec:authorize>
                <%}%>
				<!-- MED-10060 In Development2 sub-tasksAs a system engineer, I want to IP-based license dependency removal in Kubernetes, so that no manual intervention is not required while auto-scaling -->
				 <%--
					<sec:authorize access="hasAnyAuthority('APPLY_FULL_LICENSE')">
					<%
						String licenseDetails = (String) MapCache.getConfigValueAsObject(SystemParametersConstant.LICENSE_DETAILS);
					
						if(LicenseConstants.TRIAL.equals(licenseDetails)){%>
							<li class="<c:if test="${(REQUEST_ACTION_TYPE eq 'APPLY_LICENSE')}"><c:out value="active"></c:out></c:if>">
								<a	id="applylicense_lnk" href="#" onclick="loadLefMenuEvent('license_form','<%=ControllerConstants.LICENSE_MANAGER%>','POST','','apply');"> 
									<img src="img/Licence_Activation.png" width="20px" height="20px"/> 
									<span class="nav-text">
										<spring:message code="leftmenu.label.license.apply" ></spring:message>
									</span>
								</a>
							</li>
						<%} %>
							
							<c:if test="${(isRenew eq true)}">
									<li class="<c:if test="${(REQUEST_ACTION_TYPE eq 'APPLY_LICENSE')}"><c:out value="active"></c:out></c:if>">
									<a	href="#" onclick="loadLefMenuEvent('license_form','<%=ControllerConstants.LICENSE_MANAGER%>','POST','','renew');"> 
										<img src="img/Licence_Activation.png" width="20px" height="20px"/> 
										<span class="nav-text">
											<spring:message code="leftmenu.label.license.renew" ></spring:message>
										</span>
									</a>
								</li>
							</c:if>
					</sec:authorize>
					--%>
					<sec:authorize access="hasAuthority('VIEW_CIRCLE_CONFIGURATION')">
				 	<li class="<c:if test="${(REQUEST_ACTION_TYPE eq 'CIRCLE_CONFIGURATION')}"><c:out value="active"></c:out></c:if>">
						<a	id="circleConfig_lnk" href="<%=ControllerConstants.INIT_CIRCLE_CONFIG_MANAGER%>"> 
							<img src="img/Licence_Activation.png" width="20px" height="20px"/> 
							 <span class="nav-text">
									License Manager
							</span>
						</a>
					</li>
					</sec:authorize>
					
					<%if(!BaseConstants.PROFILE_ADMIN_USERNAME.equalsIgnoreCase(user) && !BaseConstants.MODULE_ADMIN_USERNAME.equalsIgnoreCase(user)){%>
					<sec:authorize access="hasAuthority('VIEW_DATASOURCE')">
				 	<li class="<c:if test="${(REQUEST_ACTION_TYPE eq 'DATABASE_CONFIGURATION')}"><c:out value="active"></c:out></c:if>">
						<a	id="databaseConfig_lnk" href="<%=ControllerConstants.INIT_CONFIGURATION_MANAGER%>"> 
							<img src="img/data_configuration_menu.png" width="20px" height="20px"/> 
							 <span class="nav-text">
									<spring:message code="leftmenu.label.db.configuration" ></spring:message>
							</span>
						</a>
					</li>
					</sec:authorize>
					
					
					<sec:authorize access="hasAnyAuthority('MIS_REPORTS_MENU_VIEW')">
				 	<li class="<c:if test="${(REQUEST_ACTION_TYPE eq 'VIEW_DYNAMIC_REPORT') or (REQUEST_ACTION_TYPE eq 'SERVICE_WISE_DETAIL') or (REQUEST_ACTION_TYPE eq 'SERVICE_WISE_SUMMARY')}"><c:out value="active"></c:out></c:if>">
						<a	id="misreports_lnk" href="#" onclick="loadLefMenuEvent('form-left-menu','<%=ControllerConstants.INIT_MIS_REPORT_MANAGER%>','GET','VIEW_DYNAMIC_REPORT','');"> 
							<img src="img/EMS_Link.png" width="20px" height="20px"/> 
							 <span class="nav-text">
									<spring:message code="leftmenu.label.mis.configuration" ></spring:message>
							</span>
						</a>
					</li>
					</sec:authorize>
					<%}%>
					
					<sec:authorize access="hasAnyAuthority('VIEW_PRODUCT_CONFIGURATION')">
					
					<li class="<c:if test="${(REQUEST_ACTION_TYPE eq 'PRODUCT_CONFIGURATION')}"><c:out value="active"></c:out></c:if>">
						<a	href="<%=ControllerConstants.INIT_PRODUCT_CONFIGURATION%>"> 
							<img src="img/ProductConfiguration_menu.png" width="20px" height="20px"/> 
							<span class="nav-text">
								<spring:message code="leftmenu.label.product.configuration" ></spring:message>
							</span>
						</a>
					</li>
					</sec:authorize>
					
					<sec:authorize access="hasAnyAuthority('VIEW_PROFILE_CONFIGURATION')">
					
					<li class="<c:if test="${(REQUEST_ACTION_TYPE eq 'PROFILE_CONFIGURATION')}"><c:out value="active"></c:out></c:if>">
						<a	href="<%=ControllerConstants.INIT_PROFILE_CONFIGURATION%>"> 
							<img src="img/ProductConfiguration_menu.png" width="20px" height="20px"/> 
							<span class="nav-text">
								<spring:message code="leftmenu.label.profile.configuration" ></spring:message>
							</span>
						</a>
					</li>
					</sec:authorize>
					
					<%if(!BaseConstants.PROFILE_ADMIN_USERNAME.equalsIgnoreCase(user) && !BaseConstants.MODULE_ADMIN_USERNAME.equalsIgnoreCase(user)){%>
						<sec:authorize access="hasAnyAuthority('VIEW_EMS')">
						 <li >
		                   <a  id  = "ems_lnk" href="<%=MapCache.getConfigValueAsString(SystemParametersConstant.NMS_LINK,null)%>" target="_blank">
		                    	<img src="img/EMS_W.png" width="20px" height="20px"/>
	                        	<span>
		                            <spring:message code="leftmenu.label.ems" ></spring:message>
	    	                    </span>
		                    </a>
		                </li>
		                </sec:authorize>

		                <sec:authorize access="hasAnyAuthority('ERROR_REPROCESS_MENU_VIEW')">
					 	<li class="<c:if test="${(REQUEST_ACTION_TYPE eq 'REPROCESSING_FILE')}"><c:out value="active"></c:out></c:if>">
							<a	id="error_reprocessing_lnk" href="#" onclick="loadLefMenuEvent('form-left-menu','<%=ControllerConstants.INIT_REPROCESSING_DETAILS%>','GET','','');" > 
								<img src="img/Error_W.png" width="20px" height="20px"/> 
								 <span class="nav-text">
										<spring:message code="leftmenu.label.error.reprocessing" ></spring:message>
								</span>
							</a>
						</li>
						</sec:authorize>
						
						<%-- <sec:authorize access="hasAnyAuthority('MIGRATION_MANAGER_MENU_VIEW')">
		                <li class="<c:if test="${(REQUEST_ACTION_TYPE eq 'MIGRATION')}"><c:out value="active"></c:out></c:if>">
		                    <a href="#" onclick="loadLefMenuEvent('form-left-menu','<%=ControllerConstants.INIT_MIGRATION%>','POST','MIGRATION','');">
		                    	<img src="img/Migration_W.png" width="20px" height="20px"/>
	                        	<span>
		                            <spring:message code="leftmenu.label.migration" ></spring:message>
	    	                    </span>
		                    </a>
		                </li>
		               </sec:authorize> --%>
		               <sec:authorize access="hasAnyAuthority('VIEW_DICTIONARY_CONFIG')">
		                <li  class="<c:if test="${(REQUEST_ACTION_TYPE eq 'VIEW_DICTIONARY_CONFIG')}"><c:out value="active"></c:out></c:if>">
		                    <a id="init_dict_data_dictnry" href="#" onclick="loadLefMenuEvent('form-left-menu','<%=ControllerConstants.INIT_DICTIOANRY_CONFIG%>','GET','VIEW_DICTIONARY_CONFIG','')";>	                    
		                    <img src="img/Rule_Data_Dictionary_White.png" width="20px" height="20px"/> 
		                       	<span>
		                            <spring:message code="leftMenu.dictionary.menu" ></spring:message>
		   	                    </span>
		                    </a>
		                </li>
		               </sec:authorize>
		               
		               <sec:authorize access="hasAnyAuthority('VIEW_RULE_DATA_CONFIG')">
		                <li class="<c:if test="${(REQUEST_ACTION_TYPE eq 'UPDATE_RULE_DATA_CONFIG')}"><c:out value="active"></c:out></c:if>">
		                    <a id="init_rule_data_dictnry" href="<%= ControllerConstants.INIT_RULE_DATA_CONFIG %>">	                    
		                    <img src="img/Rule_Data_Dictionary_White.png" width="20px" height="20px"/> 
		                       	<span>
		                            <spring:message code="leftMenu.data.dictionary.menu" ></spring:message>
		   	                    </span>
		                    </a>
		                </li>
		               </sec:authorize>
		               
		               <sec:authorize access="hasAnyAuthority('VIEW_TRIGGER_CONFIG')">
		                <li class="<c:if test="${(REQUEST_ACTION_TYPE eq 'UPDATE_TRIGGER_CONFIG')}"><c:out value="active"></c:out></c:if>">
		                    <a id="init_trigger" href="<%= ControllerConstants.INIT_TRIGGER_CONFIG %>">	                    
		                    <img src="img/Scheduler_logo.png" width="20px" height="20px"/> 
		                       	<span>
		                            <spring:message code="leftmenu.lable.trigger" ></spring:message>
		   	                    </span>
		                    </a>
		                </li>
		               </sec:authorize> 
				   <%} %>
            </ul>
            <!-- /.sidebar-menu --> 
        </section>
        <!-- /.sidebar -->
    </aside>
    <form id="form-left-menu" method="" action="">
    	<input type="hidden" id="requestAction" name="REQUEST_ACTION_TYPE" value=""/>
    </form>
    
     <form id="sm-migration-form" method="POST" action="serverManagerMigration">
    	<input type="hidden" id="requestAction" name="REQUEST_ACTION_TYPE" value=""/>
    </form>
    
    <form id="license_form" method="" action="">
    	<input type="hidden" id="requestAction" name="REQUEST_ACTION_TYPE" value=""/>
    	<input type="hidden" id="componentType" name="componentType" value="<%=LicenseConstants.LICENSE_SM%>"/>
    	<input type="hidden" id="license_server_instance_id" name="license_server_instance_id"  value="0">
    	<input type="hidden" id="licenseAction" name="licenseAction"  value="apply">
    </form>
    
    <script type="text/javascript">
    	function loadLefMenuEvent(formId,formAction, formMethod,requestAction,licenseAction){
    		$("#"+formId).attr("action",formAction);
			$("#"+formId).attr("method",formMethod);
			$("#requestAction").val(requestAction);
			$("#licenseAction").val(licenseAction);
			$("#"+formId).submit();	
    	}
    	
    	function startMigration(){
    		$.ajax({
    			url : 'reProcessMigration',
    			cache : false,
    			async : true,
    			dataType : 'json',
    			type : 'POST',
    			success : function(data) {
    				var response = eval(data);
    				var responseCode = response.code;
    				var responseMsg = response.msg;
    				var responseObject = response.object;


    				if (responseCode === "200") {
    					console.log(response);
    				}else {
    					console.log(response);
    					//showErrorMsgPopUp(responseMsg);
    				}
    			},
    			error : function(xhr, st, err) {
    				handleGenericError(xhr, st, err);
    			}
    		});
    	}
    	
    	function uploadServerConfiguration(){
    		$("#sm-migration-form").submit();
    	}
    </script>
<!-- sidebar End -->
<!-- New Left Menu Code Ends Here -->
