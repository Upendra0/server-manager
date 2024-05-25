<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<%@taglib 	uri="http://www.springframework.org/tags" 			prefix="spring"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="elitecore" %> 
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="/WEB-INF/ShowErrorTag.tld" prefix="elitecoreError"%>
    <%@ taglib 	uri="http://www.springframework.org/security/tags" 	prefix="sec"  %>

			<div class="modal-content">
				<div class="modal-header padding10">
					<h4 class="modal-title">
						<i class="icon-hdd"></i> 
						
						 <span id="add_label" style="display: none;"><spring:message
									code="snmpConfiguration.add.serverList.label" ></spring:message></span> 
									<span id="update_label" style="display: none;"><spring:message
									code="snmpConfiguration.update.serverList.label" ></spring:message></span>
									
<%-- 						<spring:message code="snmpConfiguration.add.serverList.label" ></spring:message> --%>
					</h4>
				</div>
				<form:form modelAttribute="snmp_config_form_bean" method="POST" action="#" id="create-snmp-server-list">
				<div id="snmpServerListC0ntentDiv" class="modal-body padding10 inline-form">
				
					<jsp:include page="../common/responseMsgPopUp.jsp" ></jsp:include>
					
 					<input type="hidden" id="server_Instance_Id" name="server_Instance_Id" value='${serverInstanceId}' /> 
 					
 					<input type="hidden" id="SnmpType" name="SnmpType" value="server" /> 
 					
 					<input type="hidden" id="snmpServerId" name="snmpServerId" /> 
<!-- 					<input type="hidden" id="servicelabel" name="servicelabel" value="" /> -->
					
					<div class="fullwidth">
							<spring:message
								code="snmpConfiguration.add.serverList.name"
								var="name" ></spring:message>
							
							<spring:message
								code="snmpConfiguration.add.serverList.name.tooltip"
								var="tooltip" ></spring:message>
								
							<div class="form-group">
								<div class="table-cell-label">${name}<span class="required">*</span></div>
								<div class="input-group ">
									<form:input path="name"
										cssClass="form-control table-cell input-sm" tabindex="1"
										id="server_name" data-toggle="tooltip"
										data-placement="bottom" 
										title="${tooltip}" ></form:input>
									<spring:bind path="name">
										<elitecoreError:showError
											errorMessage="${status.errorMessage}" errorId="server_name_error"></elitecoreError:showError>
									</spring:bind>
								</div>
							</div>
						</div>
						<div class="fullwidth">
							<spring:message
								code="snmpConfiguration.add.serverList.ipaddress"
								var="name" ></spring:message>
							
							<spring:message
								code="snmpConfiguration.add.serverList.ipaddress.tooltip"
								var="tooltip" ></spring:message>
								
							<div class="form-group">
								<div class="table-cell-label">${name}<span class="required">*</span></div>
								<div class="input-group ">
									<form:input path="hostIP"
										cssClass="form-control table-cell input-sm" tabindex="2"
										id="server_hostIP" data-toggle="tooltip"
										data-placement="bottom" 
										title="${tooltip}" value="${serverInstanceIp}" ></form:input>
									<spring:bind path="hostIP">
										<elitecoreError:showError
											errorMessage="${status.errorMessage}" errorId="server_hostIP_error"></elitecoreError:showError>
									</spring:bind>
								</div>
							</div>
						</div>
						<div class="fullwidth">
							<spring:message
								code="snmpConfiguration.add.serverList.port"
								var="name" ></spring:message>
							
							<spring:message
								code="snmpConfiguration.add.serverList.port.tooltip"
								var="tooltip" ></spring:message>
								
							<div class="form-group">
								<div class="table-cell-label">${name}<span class="required">*</span></div>
								<div class="input-group ">
									<form:input path="port"
										cssClass="form-control table-cell input-sm" tabindex="3"
										id="server_port" data-toggle="tooltip"
										data-placement="bottom" 
										title="${tooltip}" ></form:input>
									<spring:bind path="port">
										<elitecoreError:showError
											errorMessage="${status.errorMessage}" errorId="server_port_error"></elitecoreError:showError>
									</spring:bind>
								</div>
							</div>
						</div>
						<div class="fullwidth">
							<spring:message
								code="snmpConfiguration.add.serverList.offset"
								var="name" ></spring:message>
							
							<spring:message
								code="snmpConfiguration.add.serverList.offset.tooltip"
								var="tooltip" ></spring:message>
								
							<div class="form-group">
								<div class="table-cell-label">${name}</div>
								<div class="input-group ">
									<form:input path="portOffset"
										cssClass="form-control table-cell input-sm" tabindex="4"
										id="server_portOffset" data-toggle="tooltip"
										data-placement="bottom" 
										title="${tooltip}" ></form:input>
									<spring:bind path="portOffset">
										<elitecoreError:showError
											errorMessage="${status.errorMessage}" errorId="server_portOffset_error"></elitecoreError:showError>
									</spring:bind>
								</div>
							</div>
						</div>
						<div class="fullwidth">
							<spring:message
								code="snmpConfiguration.add.serverList.community"
								var="name" ></spring:message>
							
							<spring:message
								code="snmpConfiguration.add.serverList.community.tooltip"
								var="tooltip" ></spring:message>
								
							<div class="form-group">
								<div class="table-cell-label">${name}<span class="required">*</span></div>
								<div class="input-group ">
									<%-- <form:select path="community" cssClass="form-control table-cell input-sm" tabindex="5" name="server_community" id="server_community" data-toggle="tooltip" data-placement="bottom"  title="${tooltip }">
	                             		<c:forEach items="${communityEnum}" var="communityEnum">
											<c:if test="${communityEnum!='Private'}">
                             			 		<form:option value="${communityEnum}" >${communityEnum}</form:option>
                             				</c:if>
										</c:forEach>
									</form:select> --%>
									
									<form:input path="community" cssClass="form-control table-cell input-sm" tabindex="5" name="server_community" id="server_community" data-toggle="tooltip" data-placement="top"  title="${tooltip }"></form:input>
											
									<spring:bind path="community">
										<elitecoreError:showError
											errorMessage="${status.errorMessage}" errorId=""></elitecoreError:showError>
									</spring:bind>
								</div>
							</div>
						</div>
			
				</div>
				</form:form>
				<div id="add-snmp-buttons-div" class="modal-footer padding10">
					<button type="button" id="addNewServer" class="btn btn-grey btn-xs " onclick="addNewServerToDB()">
						<spring:message code="btn.label.save" ></spring:message>
					</button>
					
					<sec:authorize access="hasAuthority('EDIT_SNMP_SERVER')">

					<button type="button" id="editSnmpServer" class="btn btn-grey btn-xs " style="display: none"
							onclick="updateServerToDB();">
							<spring:message code="btn.label.update" ></spring:message>
						</button>
					</sec:authorize>
					
					<button id="cancel_snmp_server_btn" onclick="javascript:parent.closeFancyBoxFromChildIFrame()" class="btn btn-grey btn-xs ">
						<spring:message code="btn.label.cancel" ></spring:message>
					</button>
					
				</div>
				<div id="add-snmp-progress-bar-div" class="modal-footer padding10" style="display: none;">
					<jsp:include page="../common/processing-bar.jsp"></jsp:include>
				</div>
 				<script type="text/javascript"> 
// 					$(document).ready(function() {
// 						$("#addServiceAnchor").fancybox({
// 							   "afterLoad": function(){
// 									$("#name").val('');
// 									$("#description").val('');
// 									$("#create-service-buttons-div").show();
// 									resetWarningDisplayPopUp();
// 									clearAllMessagesPopUp();
// 							   },
// 							});
						
						

// 						$(window).keydown(function(event){
			 				
// 						    if(event.keyCode == 13) {
// 						    	event.preventDefault();
// 						        $(this).blur();
// 					        createService();
// 						    }
// 						  });
						
// 					});
					
					function createSnmpServer(){
						//resetWarningDisplayPopUp();
						//clearAllMessagesPopUp();
						//addNewServiceToDB(parent.updateServiceAccessStatus,parent.deleteServiceAccessRights);
						addNewServerToDB()
					}
				
					function addNewServerToDB(){
						resetWarningDisplayPopUp();
						clearAllMessagesPopUp();
						
						var offset=$("#server_portOffset").val();
						if(offset=="" || offset==null)
							{
							offset='-1';
							}
						
						$("#add-snmp-buttons-div").hide();
						
						showProgressBar('add-snmp');
						parent.resizeFancyBox();
						$.ajax({
							url: '<%=ControllerConstants.CREATE_SNMP_SERVERLIST%>',
							cache: false,
							async: true, 
							dataType: 'json',
							type: "POST",
							data: 
							{
								"serverInstance.id"			:   $("#server_Instance_Id").val(),
								"name" 						:	$("#server_name").val(),
								"hostIP" 					:	$("#server_hostIP").val(),
								"port" 						:	$("#server_port").val(),
								"portOffset" 				:	offset,
								//"community" 				:	$("#server_community").find(":selected").text(),
								"community" 				:	$("#server_community").val(),
								"SnmpType"					:   $("#SnmpType").val(),
								
							},
							success: function(data){
								hideProgressBar('add-snmp');
								getSnmpServerList();

								var response = eval(data);
								
								var responseCode = response.code; 
								var responseMsg = response.msg; 
								var responseObject = response.object;
								 
								if(responseCode == "200"){
									//parent.getSnmpServerList();
									$("#create-server-buttons-div").show();
									showSuccessMsg(responseMsg);
									//reloadGridData()
									closeFancyBox();
									reloadGridData();
									
								}else if(responseObject != undefined && responseObject != 'undefined' && responseCode == "400"){
									
									$("#create-server-buttons-div").show();
									
									addErrorIconAndMsgForAjaxPopUp(responseObject);
								}else{
									
									resetWarningDisplayPopUp();
									$("#create-server-buttons-div").show();
									showErrorMsgPopUp(responseMsg);
									reloadGridData();
								}
							},
						    error: function (xhr,st,err){
						    	hideProgressBar();
						    	$("#create-server-buttons-div").show();
								handleGenericError(xhr,st,err);
							}
						});
					}
					
					
					function updateServerToDB(){
						resetWarningDisplayPopUp();
						clearAllMessagesPopUp();
						
						var offset=$("#server_portOffset").val();
						if(offset=="" || offset==null)
							{
							offset='-1';
							}
						
						$("#create-server-buttons-div").hide();
						showProgressBar();
						parent.resizeFancyBox();
						$.ajax({
							url: '<%=ControllerConstants.UPDATE_SNMP_SERVERLIST%>',
							cache: false,
							async: true, 
							dataType: 'json',
							type: "POST",
							data: 
							{
								"serverInstance.id"			:   $("#server_Instance_Id").val(),
								"id"						:   $("#snmpServerId").val(),
								"name" 						:	$("#server_name").val(),
								"hostIP" 					:	$("#server_hostIP").val(),
								"port" 						:	$("#server_port").val(),
								"portOffset" 				:	offset,
								//"community" 				:	$("#server_community").find(":selected").text(),
								"community" 				:	$("#server_community").val(),
								"SnmpType"					:   $("#SnmpType").val(),
								
							},
							success: function(data){
								hideProgressBar();
								getSnmpServerList();

								var response = eval(data);
								
								var responseCode = response.code; 
								var responseMsg = response.msg; 
								var responseObject = response.object;
								 
								if(responseCode == "200"){
									//parent.getSnmpServerList();
									$("#create-server-buttons-div").show();
									showSuccessMsg(responseMsg);
									//reloadGridData();
									closeFancyBox();
									reloadGridData();
									getSnmpAlertList();
								}else if(responseObject != undefined && responseObject != 'undefined' && responseCode == "400"){
									
									$("#create-server-buttons-div").show();
									
									addErrorIconAndMsgForAjaxPopUp(responseObject);
								}else{
									
									resetWarningDisplayPopUp();
									$("#create-server-buttons-div").show();
									showErrorMsgPopUp(responseMsg);
									
								}
							},
						    error: function (xhr,st,err){
						    	hideProgressBar();
						    	$("#create-server-buttons-div").show();
								handleGenericError(xhr,st,err);
							}
						});
					}
					
				</script> 
			</div>
			<!-- /.modal-content -->
			
		
