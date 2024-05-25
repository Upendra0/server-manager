<%@taglib 	uri="http://www.springframework.org/tags" 			prefix="spring"%>
<%@ taglib 	uri="http://www.springframework.org/security/tags" 	prefix="sec"  %>
<%@ taglib 	uri="http://java.sun.com/jsp/jstl/core" 			prefix="c" %>
<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<%@page import="com.elitecore.sm.common.constants.BaseConstants"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"   pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<script>
	var updateAccessStatus = false;
	var deleteAccessStatus = false;
	var ckIntanceSelected = new Array();
	var currentTab = '${REQUEST_ACTION_TYPE}';
</script>
	
   	<script>
   		var jsLocaleMsg = {};
   		jsLocaleMsg.insName = "<spring:message code='server.instance.sync.header.label.instance.name'></spring:message>";
   		jsLocaleMsg.serverIP = "<spring:message code='server.instance.sync.header.label.server.ip'></spring:message>";
   		jsLocaleMsg.serverPort = "<spring:message code='server.instance.sync.header.label.server.port'></spring:message>";
   		jsLocaleMsg.serverName = "<spring:message code='server.delete.header.label'></spring:message>";
   		jsLocaleMsg.moduleName="<spring:message code='server.instance.import.header.label.module.name'></spring:message>";
   		jsLocaleMsg.entityName = "<spring:message code='server.instance.import.header.label.entity.name'></spring:message>";
   		jsLocaleMsg.propertyName = "<spring:message code='server.instance.import.header.label.property.name'></spring:message>";
   		jsLocaleMsg.propertyValue = "<spring:message code='server.instance.import.header.label.property.value'></spring:message>";
   		jsLocaleMsg.errorMessage = "<spring:message code='server.instance.import.header.label.error.message'></spring:message>";
   		jsLocaleMsg.chkStatus = "<spring:message code='inactive.instance.chk.status.icon.lbl'></spring:message>";
   		jsLocaleMsg.loadStatus = "<spring:message code='server.instance.load.status.icon.lbl'></spring:message>";
   		jsLocaleMsg.importFileMiss = "<spring:message code='server.instance.import.file.missing'></spring:message>";
   		jsLocaleMsg.LineNo = "<spring:message code='server.instance.import.header.label.line.number'></spring:message>";
   		
   		var files = null;

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
	                            			<spring:message code="leftmenu.label.server.manager" ></spring:message>
	                            		</strong>
	                            	</span>
								</h4>
								<jsp:include page="../common/responseMsg.jsp" ></jsp:include>
	         					<div class="row">
		           					<div class="col-xs-12">
		             					<div class="box  martop" style="border:none; box-shadow: none;">
		               						<div class="box-body table-responsive no-padding">
		               							<div class="nav-tabs-custom tab-content">
		               								<sec:authorize access="hasAnyAuthority('SERVER_MANAGER_MENU_VIEW')">
			               								
			               								<ul class="nav nav-tabs pull-right">
											                <sec:authorize access="hasAnyAuthority('SERVER_MANAGEMENT')">
				                 								<li class="<c:if test="${(REQUEST_ACTION_TYPE eq 'SERVER_MANAGEMENT')}"><c:out value="active"></c:out></c:if>" onclick="loadServerMgmtTab();showButtons('server_mgmt');" >
				                 									<a href="#server-mgmt" id="server_mgmt" data-toggle="tab" aria-expanded="false">
				                 										<spring:message code="serverManager.server.mgmt" ></spring:message>
				                 									</a>
				                 								</li>
				                 							</sec:authorize>
				                 							<sec:authorize access="hasAnyAuthority('CREATE_SERVER_AND_SERVER_INSTANCES')">
				                 								<li class="<c:if test="${(REQUEST_ACTION_TYPE eq 'CREATE_SERVER')}"><c:out value="active"></c:out></c:if>" onclick="reloadServerInstanceGUIStatus();showButtons('create_server');">
				                 									<a href="#create-server" id="create_server" data-toggle="tab" aria-expanded="true">
				                 										<spring:message code="serverManager.create.server" ></spring:message>
				                 									</a>
				                 								</li>
				                 							</sec:authorize>
			               								</ul>
			               								
			               								<sec:authorize access="hasAnyAuthority('SERVER_MANAGEMENT')">
				           									<div id="server-mgmt" class="fullwidth mtop10 tab-pane <c:if test="${(REQUEST_ACTION_TYPE eq 'SERVER_MANAGEMENT')}"><c:out value="active"></c:out></c:if> ">
					           									<c:if test="${(REQUEST_ACTION_TYPE eq 'SERVER_MANAGEMENT')}">
					           										<jsp:include page="serverMgmt.jsp"></jsp:include>
					           									</c:if>
				           									</div>
				           									
				           								</sec:authorize>
				           								<sec:authorize access="hasAnyAuthority('CREATE_SERVER_AND_SERVER_INSTANCES')">
				           								<a href="#divMessage" class="fancybox" style="display: none;" id="licenseMessage">#</a>
				           								<div id="divMessage" style="width:100%; display: none;" >
														    <div class="modal-content">
														        <div class="modal-header padding10">
														            <h4 class="modal-title"><spring:message code="serverManagement.warn.popup.header"></spring:message></h4>
														        </div>
														        <div class="modal-body padding10 inline-form">
															        <p id="moreWarn">
															        	<spring:message code="server.create.success.with.warning"></spring:message>    
															        </p>
														        </div>
														        <div class="modal-footer padding10">
														            <button type="button" class="btn btn-grey btn-xs " data-dismiss="modal" onclick="closeFancyBox();"><spring:message code="btn.label.close"></spring:message></button>
														        </div>
														    </div>
		  													  <!-- /.modal-content --> 
															</div>	
				         									<div id="create-server" class="fullwidth mtop10 tab-pane  <c:if test="${(REQUEST_ACTION_TYPE eq 'CREATE_SERVER')}"><c:out value="active"></c:out></c:if>">
				         										<c:if test="${(REQUEST_ACTION_TYPE eq 'CREATE_SERVER')}">
				         											<jsp:include page="createServer.jsp"></jsp:include>
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
		   	<a href="#divDeleteServerInstance" class="fancybox" style="display: none;" id="deleteServerInstance">#</a>
			<div id="divDeleteServerInstance" style=" width:100%; display: none;" >
				<jsp:include page="deleteServerInstancePopUp.jsp"></jsp:include> 
			</div>
			
			<a href="#divCheckStatus" class="fancybox" style="display: none;" id="checkStatus">#</a>
			<input type="hidden" id="chkInstanceId" />
			<input type="hidden" id="chkTab" />
			<div id="divCheckStatus" style=" width:100%; display: none;" >
			    <div class="modal-content">
			        <div class="modal-header padding10">
			            <h4 class="modal-title"><spring:message code="serverMgmt.instance.check.status.popup.header"></spring:message></h4>
			        </div>
			        <div class="modal-body padding10 inline-form">
				        <p>
				        	<spring:message code="serverMgmt.instance.check.status.popup.note"></spring:message>    
				        </p>
			        </div>
			        <div class="modal-footer padding10" id="check-status-button-div">
			        	<button type="button" class="btn btn-grey btn-xs " data-dismiss="modal" onclick="loadReplaceInstanceStatus();"><spring:message code="btn.label.check"></spring:message></button>
			            <button type="button" class="btn btn-grey btn-xs " data-dismiss="modal" onclick="closeFancyBox();clearSelection();"><spring:message code="btn.label.close"></spring:message></button>
			        </div>
			        <div id="check-status-progress-bar-div" class="modal-footer padding10 text-left" style="display: none;">
					<jsp:include page="../common/processing-bar.jsp"></jsp:include>
				</div>
			    </div>
			    <!-- /.modal-content --> 
			</div>
			
		<footer class="main-footer positfix">
		    	<div class="fooinn">
		    		<div class="fullwidth">
		        		<div class="button-set">
			            	<div class="padleft-right" id="serverListDiv" style="display: none;">
			            		<sec:authorize access="hasAuthority('SERVER_INSTANCE_SYNCHRONIZATION')">
			            		<button id="synchronizeBtn" class="btn btn-grey btn-xs " tabindex="21"  onclick="synchronizePopup();">
									<spring:message code="btn.label.synchronize" ></spring:message>
								</button>
								</sec:authorize>
								<sec:authorize access="hasAuthority('SERVER_INSTANCE_IMPORT_CONFIG')">
								<button id="serverimportconfigBtn" class="btn btn-grey btn-xs " tabindex="22" onclick="importConfigPopup();">
									<spring:message code="btn.label.import.config"  ></spring:message>
								</button>
								</sec:authorize>
								<sec:authorize access="hasAuthority('SERVER_INSTANCE_EXPORT_CONFIG')">
								<button id="serverexportconfigBtn" class="btn btn-grey btn-xs " tabindex="23" onclick="exportConfigPopup();">
									<spring:message code="btn.label.export.config" ></spring:message>
								</button>
								</sec:authorize>
								<sec:authorize access="hasAuthority('SYNC_PUBLISH_VERSION')">
								<button id="serverSyncPublishBtn" class="btn btn-grey btn-xs " tabindex="23" onclick="syncPublishPopup();">
									<spring:message code="btn.label.sync.publish" ></spring:message>
								</button>
								</sec:authorize>
								<sec:authorize access="hasAuthority('SERVER_INSTANCE_COPY_CONFIG')">
								<button id="servercopyconfigBtn" class="btn btn-grey btn-xs " tabindex="23" onclick="copyConfigPopup()">
									<spring:message code="btn.label.copy.config" ></spring:message>
								</button>
								</sec:authorize>
								<sec:authorize access="hasAuthority('SERVER_INSTANCE_RELOAD_CONFIG')">
								<button id="serverreloadconfigBtn" class="btn btn-grey btn-xs " tabindex="23" onclick="reloadConfigPopup();">
									<spring:message code="btn.label.reload.config" ></spring:message>
								</button>
								</sec:authorize>
								<sec:authorize access="hasAuthority('SERVER_INSTANCE_SOFT_RESTART')">
								<button id="serversoftrestartBtn" class="btn btn-grey btn-xs " tabindex="23" onclick="softRestartPopup();">
									<spring:message code="btn.label.soft.restart" ></spring:message>
								</button>
								</sec:authorize>
								
	 <%-- 							<!--To do for access right role -->
								<sec:authorize access="hasAuthority('SERVER_INSTANCE_RELOAD_CACHE')">
								<button class="btn btn-grey btn-xs " tabindex="23" onclick="reloadCachePopup();">
									<spring:message code="btn.label.reload.cache" ></spring:message>
								</button>
								</sec:authorize> --%>
							</div>	
				           	
		            		<div class="padleft-right" id="createServerDiv" style="display: none;">
		            		</div>
						</div>
		        	</div>
		        	<script type="text/javascript">
			       		
			       		function showHideButtonBasedOnTabsSelected(tabType){
			       			$("#serverListDiv").hide();
			   				$("#createServerDiv").hide();
			   				
			       			if(tabType == 'SERVER_MANAGEMENT'){
			       				$("#serverListDiv").show();
			       			}else if(tabType == 'CREATE_SERVER'){
			       				$("#createServerDiv").show();
			       			}
			       		}
			       		
			       		function showButtons(id)
			       		{
			       			if(id == 'server_mgmt'){
			       				showHideButtonBasedOnTabsSelected('SERVER_MANAGEMENT');
			       				searchInstanceCriteria();
			       			}else if(id == 'create_server'){
			       				showHideButtonBasedOnTabsSelected('CREATE_SERVER');
			       			}
			       		}
			       		
			       		$(document).ready(function() {
			       			var activeTab = $(".nav-tabs li.active a");
			    			var id = activeTab.attr("id");
			    			showButtons(id);
			     		});
			       		
			       		function reloadServerInstanceGUIStatus(){
			       			$('#reloadInstanceStatus-form').submit();
			       			loadCreateServer();
			       		}
			       		function loadServerMgmtTab(){
			       			$("#loadServerManagementForm").submit();
			       		}
			       		function moveToCreateServer(id){
							showButtons(id);
							$("#create_server").click();
							
						}
			       		
			       		// for inactive instance open popup to check instance running instance 
			       		// its common for both tab so put here in manager
			       		function chkStatusPopup(serverInstanceId,source){
			       			
			       			clearAllMessages();
			       			
			       			$('#chkInstanceId').val(serverInstanceId);
			       			$('#chkTab').val(source);
			       			
			       			$('#checkStatus').click();
			       		}
			       		
			       		function loadReplaceInstanceStatus(){
			    			
			       			var serverInstanceId = $('#chkInstanceId').val();
			       			var source = $('#chkTab').val();
			       			$('#check-status-button-div').hide();
			       			$('#check-status-progress-bar-div').show();
			       			
			       			if(source=='CREATE_SERVER')
			       				$('#loader_'+serverInstanceId).html('<img src="img/preloaders/circle-red.gif" width="15px" data-toggle="tooltip" data-placement="bottom" title="Loading Status.." height="15px" style="vertical-align: middle;position: absolute;z-index: 99999;margin-top: 25px;">');
			       			else if(source=='SERVER_MANAGEMENT'){
			       				$('#loader_'+serverInstanceId).html('<img src="img/preloaders/circle-red.gif" width="15px" data-toggle="tooltip" data-placement="bottom" title="Loading Status.." class="mCS_img_loaded" width="20px" height="20px" style="z-index: 99999;">');
			       			}
			       			
			    			$.ajax({
			    				url: 'loadServerInstanceStatus',
			    				cache: false,
			    				async: true,
			    				dataType: 'json',
			    				type: "POST",
			    				data: {
			    					"serverInstanceId": serverInstanceId
			    				},
			    				success: function(data){
			    					var response = eval(data);
			    					
			    					var responseObject = response.object;
			    					var instanceStatus = responseObject.status;
			    					var toggleId = serverInstanceId + "_" + instanceStatus;
			    					
			    					if(response.code=='200'){
			    						if(instanceStatus == 'ACTIVE'){
			    							// if instance is active than update status in database
			    							updateInstanceRunningSts(serverInstanceId);
			    			 			} else {
			    			 				// server instance status is inactive
				    						showErrorMsg("<spring:message code='server.instance.check.status.fail'></spring:message>");
				    						
			    			 				if(source=='CREATE_SERVER'){
			    			 					$('#loader_'+serverInstanceId).html('<img src="img/chk_sts.ico" width="15px" data-toggle="tooltip" data-placement="bottom" title="Check Status.." height="15px" style="vertical-align: middle;position: absolute;z-index: 99999;margin-top: 25px;cursor:pointer;" onclick="chkStatusPopup(\''+serverInstanceId+'\',\''+source+'\');">');	
			    			 				} else {
			    			 					$('#loader_'+serverInstanceId).html('<img src="img/chk_sts.ico" width="20px" data-toggle="tooltip" data-placement="bottom" title="Check Status.." height="20px" style="z-index: 99999;cursor:pointer;" onclick="chkStatusPopup(\''+serverInstanceId+'\',\''+source+'\');">');
			    			 				}
			    			 			}
			    					} else {
			    						// server instance status is inactive
			    						showErrorMsg("<spring:message code='server.instance.check.status.fail'></spring:message>");
			    						if(source=='CREATE_SERVER'){
		    			 					$('#loader_'+serverInstanceId).html('<img src="img/chk_sts.ico" width="15px" data-toggle="tooltip" data-placement="bottom" title="Check Status.." height="15px" style="vertical-align: middle;position: absolute;z-index: 99999;margin-top: 25px;cursor:pointer;" onclick="chkStatusPopup(\''+serverInstanceId+'\',\''+source+'\');">');	
		    			 				} else {
		    			 					$('#loader_'+serverInstanceId).html('<img src="img/chk_sts.ico" width="20px" data-toggle="tooltip" data-placement="bottom" title="Check Status.." height="20px" style="z-index: 99999;cursor:pointer;" onclick="chkStatusPopup(\''+serverInstanceId+'\',\''+source+'\');">');
		    			 				}
			    					}
			    					
			    					$('.checkboxbg').bootstrapToggle();
			    					$('.checkboxbg').change(function(){
			    	    				instanceActiveInactiveToggleChanged(this);
			    	    			});
			    					
			    					$('#check-status-button-div').show();
					       			$('#check-status-progress-bar-div').hide();
			    					closeFancyBox();
			    				},
			    			    error: function (xhr,st,err){
			    					handleGenericError(xhr,st,err);
			    				}
			    			});
			    		}
			       		
			       		function updateInstanceRunningSts(serverInstanceId){
			       		
			       			var serverInstanceId = $('#chkInstanceId').val();
			       			var source = $('#chkTab').val();
			       			
			       			$.ajax({
			    				url: 'updateServerInstaneStatus',
			    				cache: false,
			    				async: true,
			    				dataType: 'json',
			    				type: "POST",
			    				data: {
			    					"serverInstanceId": serverInstanceId
			    				},
			    				success: function(data){
			    					var response = eval(data);
			    					var toggleId = serverInstanceId + "_" + response.msg;
			    					
			    					if(response.code=='200'){ // if update instance status for inactive instance in db success than
			    						
	    								if(source=='CREATE_SERVER'){
	    									$('#loader_'+serverInstanceId).next().removeClass('server-box3');
		    								$('#loader_'+serverInstanceId).next().addClass('server-box2');
			    							$('#loader_'+serverInstanceId).html('');
			    							
			    							// set name link once we get status as active for inactive server instance
			    							<sec:authorize access="hasAuthority('VIEW_SERVER_AND_SERVER_INSTANCES')">
		    									$('#'+serverInstanceId+'_name').attr('onclick','viewServerInstance(\'' + serverInstanceId + '\',\"ACTIVE\")');
		    									$('#'+serverInstanceId+'_name').css('text-decoration','underline');
		    								</sec:authorize>
		    								
		    							} else {
		    								<sec:authorize access="hasAuthority('SERVER_INSTANCE_STOP')">
		    									$('#loader_'+serverInstanceId).html('<input class="checkboxbg" id=' + toggleId + ' data-on="On" data-off="Off" checked type="checkbox" data-size="mini">');
		    								</sec:authorize>
		    								<sec:authorize access="!hasAuthority('SERVER_INSTANCE_STOP')">
		    									$('#loader_'+serverInstanceId).html('<input class="checkboxbg" id=' + toggleId + ' data-on="On" data-off="Off" checked type="checkbox" data-size="mini" disabled>');
		    								</sec:authorize>
		    								
		    								$('.checkboxbg').bootstrapToggle();
		    								$('.checkboxbg').change(function(){
		    				    				instanceActiveInactiveToggleChanged(this);
		    				    			});
		    								
		    								// set name link once we get status as active for inactive server instance
		    								<sec:authorize access="hasAuthority('VIEW_SERVER_AND_SERVER_INSTANCES')">
		    									$('#'+serverInstanceId+'_name').attr('onclick','viewServerInstance(\'' + serverInstanceId + '\')');
		    									$('#'+serverInstanceId+'_name').css('text-decoration','underline');
		    								</sec:authorize>
		    								// enable synch flag after loading server instance status if has access right than
		    								<sec:authorize access="hasAuthority('SERVER_INSTANCE_SYNCHRONIZATION')">
		    									$('#synch_'+serverInstanceId).attr("onclick","synchInstanceById('"+serverInstanceId+"')");
		    								</sec:authorize>
		    								
		    								// enable checkbox for server instance in row
		    								var Checkboxes = $("tr#"+serverInstanceId+".jqgrow > td > input.cbox:disabled", "");
		    								Checkboxes.removeAttr("disabled");
		    							}
			    					} else { // if update instance status for inactive instance in db fail than
			    						showErrorMsg(response.msg);
			    						if(source=='CREATE_SERVER'){
		    			 					$('#loader_'+serverInstanceId).html('<img src="img/chk_sts.ico" width="20px" data-toggle="tooltip" data-placement="bottom" title="Check Status.." height="20px" style="vertical-align: middle;position: absolute;z-index: 99999;margin-top: 25px;cursor:pointer;" onclick="chkStatusPopup(\''+serverInstanceId+'\',\''+source+'\');">');	
		    			 				} else {
		    			 					$('#loader_'+serverInstanceId).html('<img src="img/chk_sts.ico" width="20px" data-toggle="tooltip" data-placement="bottom" title="Check Status.." height="20px" style="z-index: 99999;cursor:pointer;" onclick="chkStatusPopup(\''+serverInstanceId+'\',\''+source+'\');">');
		    			 				}
			    					}
			    				},
			    			    error: function (xhr,st,err){
			    			    	handleGenericError(xhr,st,err);
			    				}
			    			});
			       		}
			       		
			       	</script>
		        	<jsp:include page="../common/newfooter.jsp"></jsp:include>
		    	</div>
			</footer>
		</div><!-- ./wrapper -->
	</body>
	
	<div style="display: hidden">
		<form id="reloadInstanceStatus-form" action ="<%= ControllerConstants.GET_SERVER_INSTANCE_MAP %>" method="GET">
			<input type="hidden" name="<%= BaseConstants.REQUEST_ACTION_TYPE %>"  id="<%= BaseConstants.REQUEST_ACTION_TYPE %>" value="<%= BaseConstants.CREATE_SERVER %>" />
		</form>
	</div>
	<div style="display: hidden">
		<form id="loadServerManagementForm" action ="<%= ControllerConstants.INIT_SERVER_MANAGER %>" method="GET">
			<input type="hidden" name="<%= BaseConstants.REQUEST_ACTION_TYPE %>"  id="<%= BaseConstants.REQUEST_ACTION_TYPE %>" value="<%= BaseConstants.SERVER_MANAGEMENT%>" />
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
