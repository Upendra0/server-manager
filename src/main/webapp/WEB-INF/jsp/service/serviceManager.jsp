<%@page import="com.elitecore.sm.common.constants.BusinessModelConstants"%>
<%@taglib 	uri="http://www.springframework.org/tags" 			prefix="spring"%>
<%@ taglib 	uri="http://www.springframework.org/security/tags" 	prefix="sec"  %>
<%@ taglib 	uri="http://java.sun.com/jsp/jstl/core" 			prefix="c" %>
<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<%@page import="com.elitecore.sm.common.constants.BaseConstants"%>
<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
 <script>
	var updateServerInstanceAccessStatus = false;
	var updateServiceAccessStatus=false;
	var deleteServiceAccessRights=false;
	var currentTab = '${REQUEST_ACTION_TYPE}';
	
	var ckIntanceSelected = new Array();
</script>

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
	
	<jsp:include page="../common/newheader.jsp"></jsp:include>
	
<body class="skin-blue sidebar-mini sidebar-collapse">
	<div>

		<jsp:include page="../common/newtopNavigationPanel.jsp"></jsp:include>
		<jsp:include page="../common/newleftMenu.jsp"></jsp:include>

		<div class="content-wrapper">
			<section class="content-header">
				<div class="fullwidth">
		       		<div style="padding:0 10px 0 4px;">
		       			<div id="content-scroll-d" class="content-scroll">
		        			<div class="fullwidth">
								<h4 style="margin-top:10px; margin-bottom:0px; font-size:11px;">
									<a href="home.html"><img src="img/tile-icon.png" class="vmiddle" alt="Home"/></a> 
	                            	<span class="spanBreadCrumb" style="line-height: 30px;">
	                            		<strong>&nbsp;
	                            			<spring:message code="leftMenu.service.manager.menu" ></spring:message>
	                            		</strong>
	                            	</span>
								</h4>
								<jsp:include page="../common/responseMsg.jsp" ></jsp:include>
	         					<div class="row">
		           					<div class="col-xs-12">
		             					<div class="box  martop" style="border:none; box-shadow: none;">
		               						<div class="box-body table-responsive no-padding">
		               							<div class="nav-tabs-custom tab-content">
		               								<sec:authorize access="hasAnyAuthority('SERVICE_MANAGER_MENU_VIEW')">
			               								
			               								<ul class="nav nav-tabs pull-right">
											                <sec:authorize access="hasAnyAuthority('SERVICE_MANAGEMENT')">
				                 								<li class="<c:if test="${(REQUEST_ACTION_TYPE eq 'SERVICE_MANAGEMENT')}"><c:out value="active"></c:out></c:if>" onclick="loadServiceManagementPage();showButtons('service_mgmt');" >
				                 									<a href="#service-mgmt" id="service_mgmt" data-toggle="tab" aria-expanded="false">
				                 										<spring:message code="serviceManager.service.mgmt" ></spring:message>
				                 									</a>
				                 								</li>
				                 							</sec:authorize>
				                 							<sec:authorize access="hasAnyAuthority('CREATE_SERVICE_INSTANCE')">
				                 								<li class="<c:if test="${(REQUEST_ACTION_TYPE eq 'CREATE_SERVICE')}"><c:out value="active"></c:out></c:if>" onclick="loadCreateServicePage();showButtons('create_service');">
				                 									<a href="#create-service" id="create_service" data-toggle="tab" aria-expanded="true">
				                 										<spring:message code="serviceManager.create.service" ></spring:message>
				                 									</a>
				                 								</li>
				                 							</sec:authorize>
			               								</ul>
			               								
			               								<sec:authorize access="hasAnyAuthority('SERVICE_MANAGEMENT')">
				           									<div id="service-mgmt" class="fullwidth mtop10 tab-pane <c:if test="${(REQUEST_ACTION_TYPE eq 'SERVICE_MANAGEMENT')}"><c:out value="active"></c:out></c:if> ">
				           									<c:if test="${(REQUEST_ACTION_TYPE eq 'SERVICE_MANAGEMENT')}">
				           										<jsp:include page="serviceMgmt.jsp"></jsp:include>
				           										</c:if>
				           									</div>
				           								</sec:authorize>
				           								<sec:authorize access="hasAnyAuthority('CREATE_SERVICE_INSTANCE')">
				         									<div id="create-service" class="fullwidth mtop10 tab-pane <c:if test="${(REQUEST_ACTION_TYPE eq 'CREATE_SERVICE')}"><c:out value="active"></c:out></c:if>">
				         									<c:if test="${(REQUEST_ACTION_TYPE eq 'CREATE_SERVICE')}">
				         										<jsp:include page="createService.jsp"></jsp:include>
				         										</c:if>
				         									</div>
				         								</sec:authorize>
			               							</sec:authorize>
	             								</div>
											</div>
		               					</div>
		             				</div>
		           				</div>
		           				
		         				</div>
		         			</div>
		         		</div>
		      		</div>
		    	</section>
		   	</div>
		   	
		   	<!-- Delete server warning messages and pop up code start here -->
		   	<a href="#divDeleteService" class="fancybox" style="display: none;" id="deleteServicePopup">#</a>
		   	<div id="divDeleteService" style=" width:100%; display: none;" >
			   <jsp:include page="deleteServicePopUp.jsp"></jsp:include> 
			</div>
			
			<!-- warning message pop-up -->
			
		   	<a href="#divMessage" class="fancybox" style="display: none;" id="deleteWarnMsg">#</a>
		   	<div id="divMessage" style=" width:100%; display: none;" >
			    <div class="modal-content">
			        <div class="modal-header padding10">
			            <h4 class="modal-title">Warning</h4>
			        </div>
			        <div class="modal-body padding10 inline-form">
				        <p id="moreWarn">
				        	<spring:message code="serviceMgmt.more.instance.checked.warning"></spring:message>    
				        </p>
				        <p id="lessWarn">
				        	<spring:message code="serviceMgmt.no.instance.checked.warning"></spring:message>    
				        </p>
			        </div>
			        <div class="modal-footer padding10">
			            <button id = "closebtn"type="button" class="btn btn-grey btn-xs " data-dismiss="modal" onclick="closeFancyBox();"><spring:message code="btn.label.close"></spring:message></button>
			        </div>
			    </div>
			    <!-- /.modal-content --> 
			</div>
			<!-- Delete server warning messages and pop up code end here -->
			
				<!-- Service Export hidden form code star here -->
				<div style="display: none;">
					<form action="<%=ControllerConstants.EXPORT_SERVICE_INSTANCE_CONFIG%>" id="export-service-instance-config-form" method="POST">
			        	<input type="hidden" id="exportServiceInstanceId" name="exportServiceInstanceId"  value=""/>
			        	<!-- <input type="hidden" id="name" name="name"  value="Collection Service"/> -->
			        	<input type="hidden" id="isExportForDelete" name="isExportForDelete"  value=""/>
			        	<input type="hidden" id="exportPath" name="exportPath" />
			        	<input type="hidden" id="<%=BaseConstants.REQUEST_ACTION_TYPE %>" name="<%=BaseConstants.REQUEST_ACTION_TYPE %>" value="<%=BaseConstants.SERVICE_MANAGEMENT%>"/>
			       </form>
				</div>
				<!-- Service Export hidden form code end here -->
			<footer class="main-footer positfix">
		    	<div class="fooinn">
		    		<div class="fullwidth">
		        		<div class="button-set">
			            	<div class="padleft-right" id="serviceListDiv" style="display: none;">
			            		<sec:authorize access="hasAuthority('SYNCHRONIZE_SERVICE_INSTANCE')">
			            		<button id="synchronizeBtn" class="btn btn-grey btn-xs " tabindex="21"  onclick="synchronizePopup();">
									<spring:message code="btn.label.synchronize" ></spring:message>
								</button>
								</sec:authorize>
								<sec:authorize access="hasAuthority('EXPORT_SERVICE_INSTANCE_CONFIG')">
								<button id="exportConfigBtn" class="btn btn-grey btn-xs " tabindex="23" onclick="exportConfigPopup();">
									<spring:message code="btn.label.export.config" ></spring:message>
								</button>
								</sec:authorize>
								<sec:authorize access="hasAuthority('IMPORT_SERVICE_INSTANCE_CONFIG')">
								<button id="importConfigBtn" class="btn btn-grey btn-xs " tabindex="22" onclick="importConfigPopup();">
									<spring:message code="btn.label.import.config"  ></spring:message>
								</button>
								</sec:authorize>
								<sec:authorize access="hasAuthority('SYNC_PUBLISH_VERSION')">
								<button id="syncPublishBtn" class="btn btn-grey btn-xs " tabindex="22" onclick="syncPublishPopup();">
									<spring:message code="btn.label.sync.publish"  ></spring:message>
								</button>
								</sec:authorize>
							</div>	
		            		<div class="padleft-right" id="createServiceDiv" style="display: none;">
		            		</div>
						</div>
		        	</div>
		        	<script type="text/javascript">
			       		
			       		function showHideButtonBasedOnTabsSelected(tabType){
			       			$("#serviceListDiv").hide();
			   				$("#createServiceDiv").hide();
			   				
			       			if(tabType == 'SERVICE_MANAGEMENT'){
			       				$("#serviceListDiv").show();
			       			}else if(tabType == 'CREATE_SERVICE'){
			       				$("#createServiceDiv").show();
			       			}
			       		}
			       		
			       		function showButtons(id)
			       		{
			       			if(id == 'service_mgmt'){
			       				showHideButtonBasedOnTabsSelected('SERVICE_MANAGEMENT');
			       				//searchInstanceCriteria();
			       			}else if(id == 'create_service'){
			       				showHideButtonBasedOnTabsSelected('CREATE_SERVICE');
			       			}
			       		}
			       		function loadCreateServicePage(){
			       			$('#loadCreateServiceForm').submit();	
			       		}
			       		
			       		function loadServiceManagementPage(){
			       			$('#loadServiceManagementForm').submit();	
			       		}
			       		$(document).ready(function() {
			       			var activeTab = $(".nav-tabs li.active a");
			    			var id = activeTab.attr("id");
			    			showButtons(id);
			     		});
			       		
			       	  function exportServiceInstanceBeforeDelete(exportData ,id){
							$("#exportServiceInstanceId").val(id);
							$("#isExportForDelete").val(true);
							$("#REQUEST_ACTION_TYPE").val('<%=BaseConstants.SERVICE_MANAGEMENT%>');
							//$("#btnExportDeletePopup").prop('disabled', true);
							$("#btnDeletePopup").prop('disabled', false);
							$("#exportPath").val(exportData);
							$("#export-service-instance-config-form").submit();
					  }
			       		
						function moveToCreateService(id){
							showButtons(id);
							$("#create_service").click();
							
						}
						
			       	</script>
		        	<jsp:include page="../common/newfooter.jsp"></jsp:include>
		    	</div>
			</footer>
		</div><!-- ./wrapper -->
	</body>
	<div style="display: hidden">
		<form id="loadCreateServiceForm" action ="<%= ControllerConstants.INIT_SERVICE_MANAGER %>" method="GET">
			<input type="hidden" name="<%= BaseConstants.REQUEST_ACTION_TYPE %>"  id="<%= BaseConstants.REQUEST_ACTION_TYPE %>" value="<%= BaseConstants.CREATE_SERVICE%>" />
		</form>
	</div>
	<div style="display: hidden">
		<form id="loadServiceManagementForm" action ="<%= ControllerConstants.INIT_SERVICE_MANAGER %>" method="GET">
			<input type="hidden" name="<%= BaseConstants.REQUEST_ACTION_TYPE %>"  id="<%= BaseConstants.SERVICE_MANAGEMENT %>" value="<%= BaseConstants.SERVICE_MANAGEMENT%>" />
		</form>
	</div>
	<div style="display: none;">
			<form id="service_form" method="GET" action="<%= ControllerConstants.INIT_UPDATE_SERVICE %>">
				<input type="hidden" id="serviceId" name="serviceId">
				<input type="hidden" id="serviceType" name="serviceType">
				<input type="hidden" id="serviceName" name="serviceName">
				<input type="hidden" id="instanceId" name="instanceId">
			</form>
		</div>
		<div style="display: none;">
			<form id="instance_form" method="POST" action="<%= ControllerConstants.INIT_UPDATE_SERVER_INSTANCE %>">
				<input type="hidden" id="serverInstanceId" name="serverInstanceId">
				<input type="hidden" id="serverInstanceStatus" name="serverInstanceStatus">
			</form>
		</div>
		<div id="divExportConfig" style=" width:100%; display: none;" >
		    <div class="modal-content">
		    <form method="POST" action="<%=ControllerConstants.EXPORT_SERVER_INSTANCE_CONFIG%>" id="export-config-form">
		        	<input type="hidden" id="exportInstancesId" name="exportInstancesId" />
		        	<input type="hidden" id="isExportForDelete" name="isExportForDelete" />
		        	<input type="hidden" id="exportPath" name="exportPath" />
		        	<input type="hidden" id="<%=BaseConstants.REQUEST_ACTION_TYPE %>" name="<%=BaseConstants.REQUEST_ACTION_TYPE %>" value="<%=BaseConstants.SERVER_MANAGEMENT%>"/>
		       </form>
		    </div>
		    <!-- /.modal-content --> 
		</div>
	
	
</html>
