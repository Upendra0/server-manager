<%@page import="com.elitecore.sm.common.constants.EngineConstants"%>
<%@ taglib 	uri="http://www.springframework.org/security/tags" 	prefix="sec"  %>
<%@taglib 	uri="http://www.springframework.org/tags" 			prefix="spring"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<%@page import="com.elitecore.sm.common.constants.EngineConstants"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<script>
   		jsLocaleMsg.dragServiceInstanceStrongId = "<spring:message code='serviceManager.drag.service.instance' ></spring:message>";
   		jsLocaleMsg.droppableRegionLabel=  "<spring:message code='serviceManager.droppable.region.label' ></spring:message>";
   		jsLocaleMsg.dragServerInstanceLable= "<spring:message code='serviceManager.drag.server.instance' ></spring:message>";
   		var $optgroup;
   	</script>

<head>
	<style>
		.serviceli{clear: both; foat: left; margin-bottom: 3px; width: 100%;}
		.text-left{text-align: left;}
		.service-strong-left{float:left; width:70%;}
	</style>
<%-- 	<script src="${pageContext.request.contextPath}/js/jquery-ui.js"></script> --%>
	<script src="${pageContext.request.contextPath}/js/bootstrap.js" type="text/javascript"></script>
</head>


<div id="create-service-block" class="col-xs-8 col-sm-9 col-md-10 content-scroll page-section">
	<p class="title2">
		<strong>
			<spring:message code="serviceManager.create.service.label" ></spring:message>
		</strong>
		<span style="float: right;">
		<select id="selServerInstance" class='form-control table-cell input-sm'  data-toggle='tooltip' data-placement='bottom' title='${tooltip }' onChange='profileServiceList();'>
					 <c:forEach items="${SERVER_INSTANCE_LIST}" var="serverType">
						<optgroup label="${serverType.key}">
						<c:forEach items="${serverType.value}" var="serverInstance">
							<option value="${serverInstance.id}">${serverInstance.name} | ${serverInstance.server.ipAddress} : ${serverInstance.port} </option>
						 </c:forEach>
						</optgroup>
						</c:forEach>
						<option value="-1" selected="selected"><spring:message code="serviceManager.create.service.allServerInstance.option"></spring:message></option>
			       </select>
			     
			       </span>
	</p>
	<div id="cart-added" style="font-size: 100%;"></div>
	
</div>
					
					<!-- Initialize all draggable and droppable Javascripts -->
					<a style="display: none;" class="fancybox" id="serverInstanceDetailAnchor" href="#serverInstanceDetails">#</a>
					<a style="display: none;" class="fancybox" id="addServiceAnchor" href="#serviceDetails"></a>
					<a style="display: none;" class="fancybox fancybox.iframe" id="updtServerInstanceAnchor" href="initUpdateServer">Server Instance Details</a>
					<sec:authorize access="hasAuthority('DELETE_SERVER_AND_SERVER_INSTANCES')">
						<a style="display: none;" class="fancybox" id="deleteServerInstance" href="#divDeleteSIMessage"></a>
					</sec:authorize> 
					<script type="text/javascript">
						$(document).ready(function() {
							initializeServerInstanceAndServiceComponents();
							
						});
					</script>
					<script type='text/javascript'>
					<sec:authorize access="!hasAuthority('VIEW_SERVER_AND_SERVER_INSTANCES')">
						updateServerInstanceAccessStatus=false;
					</sec:authorize>
					<sec:authorize access="hasAuthority('VIEW_SERVER_AND_SERVER_INSTANCES')">
						updateServerInstanceAccessStatus=true;
					</sec:authorize>
					<sec:authorize access="!hasAuthority('VIEW_SERVICE_INSTANCE')">
						updateServiceAccessStatus = false;
					</sec:authorize>
					
					<sec:authorize access="hasAuthority('VIEW_SERVICE_INSTANCE')">
						updateServiceAccessStatus = true;
					</sec:authorize>
					
					<sec:authorize access="!hasAuthority('DELETE_SERVICE_INSTANCE')">
						deleteServiceAccessRights = false;
					</sec:authorize>
					
					<sec:authorize access="hasAuthority('DELETE_SERVICE_INSTANCE')">
						deleteServiceAccessRights = true;
					</sec:authorize>
					</script>
					
					<c:choose>
						<c:when test="${CONFIGURED_SERVER_INSTANCE_LIST != null && fn:length(CONFIGURED_SERVER_INSTANCE_LIST) gt 0}">
						<c:forEach var="serverInstance" items="${CONFIGURED_SERVER_INSTANCE_LIST}">
							<c:choose>
						
								<c:when test="${selectedServerInstanceId != null }">
						
									<script type="text/javascript">
										$(document).ready(function() {
										
											var configuredInstanceId='${serverInstance.key}';
											
											//if($("#"+configuredInstanceId+"_block").length==0){
												displayServerInstance('${serverInstance.value.name}',configuredInstanceId,'${serverInstance.value.server.ipAddress}','${serverInstance.value.port}','${serverInstance.value.server.serverType.alias}',updateServerInstanceAccessStatus,true);
											//}
										});
									</script>
						
								</c:when>
								<c:otherwise>
									<script type="text/javascript">
										$(document).ready(function() {
										
											var configuredInstanceId='${serverInstance.key}';
											
											//if($("#"+configuredInstanceId+"_block").length==0){
												displayServerInstance('${serverInstance.value.name}',configuredInstanceId,'${serverInstance.value.server.ipAddress}','${serverInstance.value.port}','${serverInstance.value.server.serverType.alias}',updateServerInstanceAccessStatus,false);
											//}
										});
									</script>
								</c:otherwise>
							</c:choose>
						</c:forEach>
						</c:when>
						<c:otherwise>
							<script type='text/javascript'>
								
								$(document).ready(function() {
									
									displayblankDragArea();
									
								});
							</script>
						</c:otherwise>
					
					</c:choose>
					
					<c:if test="${SERVICE_LIST != null }">
						<c:forEach var="service" items="${SERVICE_LIST}">
							<script type="text/javascript">
							$(document).ready(function() {
								
									addNewService('${service.id}','${service.servInstanceId}','${service.svctype.type}','${service.svctype.alias}','${service.name}',updateServiceAccessStatus,deleteServiceAccessRights,'${service.serverInstance.id}',true);
									
								
							});
							</script>
						</c:forEach>
					</c:if>
					<c:if test="${selectedServerInstanceId != null }">
						
							<script type="text/javascript">
							$(document).ready(function() {
								var selectedServerInstanceId='${selectedServerInstanceId}';
								$("#selServerInstance").val(selectedServerInstanceId);
							});
							</script>
						
					</c:if>
				
			
<sec:authorize access="hasAnyAuthority('CREATE_SERVICE_INSTANCE')">
	<div id="createServerInstanceAndServiceComponentDiv" class="col-xs-4 col-sm-3 col-md-2">
		
		<div class="box-border"  style="overflow: auto;min-height: 250px">
			<h5 class="section-title padding5 borbot margin0 grey-bg">
				<strong>
					<spring:message code="serviceManager.main.service.components" ></spring:message>
				</strong>
			</h5>
			
			
			<ul class="text-center list-unstyled padding0 mtop10" id="service-components">
			<c:if test="${MAIN_SERVICE_TYPE_LIST !=null && fn:length(MAIN_SERVICE_TYPE_LIST) gt 0}">
			<c:forEach var="serviceType" items="${MAIN_SERVICE_TYPE_LIST}">
			<div class="serviceli">
				<li class="ui-draggable ui-draggable-handle" id="Services">
					<div>
					
					<img alt="Service" src="img/${serviceType.type}.png" class="img-responsive pull-left mright10" height="30px" width="30px" id="${serviceType.alias}"/>
					
					<label style="display: block; font-size: 11px" class="text-left">
						<strong class="service-strong-left">
							${serviceType.type}
						</strong>
					</label>
				</div>
				</li>
			</div>
			<br/>
			</c:forEach>
			</c:if>
			</ul>
			
			</div>
			 <div class="box-border"  style="overflow: auto;min-height: 150px">
			<h5 class="section-title padding5 borbot margin0 grey-bg">
				<strong>
					<spring:message code="serviceManager.additional.service.components" ></spring:message>
				</strong>
			</h5>
			
			
			<ul class="text-center list-unstyled padding0 mtop10" id="service-components">
			<c:if test="${ADDITIONAL_SERVICE_TYPE_LIST !=null && fn:length(ADDITIONAL_SERVICE_TYPE_LIST) gt 0}">
			<c:forEach var="serviceType" items="${ADDITIONAL_SERVICE_TYPE_LIST}">
				<div class="serviceli">
					<li class="ui-draggable ui-draggable-handle" id="Services">
						<div style="height:30px">
							<img alt="Service" src="img/${serviceType.type}.png" class="img-responsive pull-left mright10" height="30px" width="30px" id="${serviceType.alias}"/>
							<label style="display: block; font-size: 11px" class="text-left">
								<strong class="service-strong-left">
									${serviceType.type}
								</strong>
							</label>
						</div>
					</li>
				</div>
				
				</c:forEach>
			</c:if>
			</ul>
	
			
		</div> 
	</div>
	
	<div style="display: none;">
		<form id="instance_form" method="POST" action="<%= ControllerConstants.INIT_UPDATE_SERVER_INSTANCE %>">
			<input type="hidden" id="serverInstanceId" name="serverInstanceId">
		</form>
	</div>
	<%-- <div id="serverInstanceDetails" style=" width:100%; display: none;" >
		   <jsp:include page="selectServerInstancePopUp.jsp"></jsp:include>
	</div>  --%>
	<div id="serviceDetails" style=" width:100%; display: none;" >
		   <jsp:include page="addServicePopUp.jsp"></jsp:include>
	</div>
	<div id="divDeleteSIMessage" style=" width:100%; display: none;" >
		    <div class="modal-content">
		        <div class="modal-header padding10">
		            <h4 class="modal-title"><spring:message code="remove.server.instance.popup.header"></spring:message></h4>
		        </div>
		        <div class="modal-body padding10 inline-form">
		        <input type="hidden" id="removeInstanceId" />
			        <p id="importantNote">
			        	<i class="icon-lightbulb icon-large"></i><span class="note"><spring:message code="label.important.note"></spring:message></span><br/>
			        </p>
					<p id="removeAllow">	
						<spring:message code="remove.server.instance.warning.msg.content"></spring:message>
						<strong><spring:message code="updtInstacne.summary.agent.start.status.note"></spring:message></strong>           	
			        </p>
			        <p id="removeNotAllow">	
						<spring:message code="remove.server.instance.notAllow.msg.content"></spring:message>           	
			        </p>
		        </div>
		        <div id="remove-buttons-div" class="modal-footer padding10">
		            <button id=del_yes_btn type="button" class="btn btn-grey btn-xs " onclick="removeInstance();"><spring:message code="btn.label.yes"></spring:message></button>
		            <button id=del_no_btn type="button" class="btn btn-grey btn-xs " data-dismiss="modal" onclick="closeFancyBox();"><spring:message code="btn.label.no"></spring:message></button>
		        </div>
		        <div id="removeNot-buttons-div" class="modal-footer padding10">
		            <button type="button" class="btn btn-grey btn-xs " data-dismiss="modal" onclick="closeFancyBox();"><spring:message code="btn.label.close"></spring:message></button>
		        </div>
		    </div>
		    <!-- /.modal-content --> 
		</div>
		
		<div style="display: none;">
		<form id="profile_service_form" method="POST" action="<%=ControllerConstants.GET_SERVICE_LIST_BY_SERVER_TYPE%>">
			<input type="hidden" id="profileserverInstanceId" name="profileserverInstanceId">
		</form>
	</div>
	
</sec:authorize>
