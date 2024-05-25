<%@taglib 	uri="http://www.springframework.org/tags" 			prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="elitecore" %> 
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<html>
<style>
   .ui-autocomplete
   {
       z-index:4000 !important;
       max-height: 200px;
       overflow-y: auto;
       overflow-x: hidden;
   }
</style>	
	<jsp:include page="../common/newheader.jsp"></jsp:include>
	<body class="skin-blue sidebar-mini sidebar-collapse">
		<div id="serverDetails" style="width: 100%; ">
			<div class="modal-content">
				<div class="modal-header padding10">
					<h4 class="modal-title">
						<i class="icon-hdd"></i> 
						<spring:message code="serverManager.updt.server" ></spring:message>
					</h4>
				</div>
				<div id="serverContentDiv" class="modal-body padding10 inline-form">
					<jsp:include page="../common/responseMsg.jsp" ></jsp:include>
					
					<input type="hidden" name="id" id="updt-id" value="${server_form_bean.id}" />
					
					<c:if test="${SERVER_TYPE_LIST != null && fn:length(SERVER_TYPE_LIST) gt 0}">
						<c:choose>
							<c:when test="${fn:length(SERVER_TYPE_LIST) eq 1}">
								<select style="display: none;" id="updt-serverTypeId" name="serverTypeId">
									<c:forEach var="serverType" items="${SERVER_TYPE_LIST}">
										<option value="${serverType.id }">
											${serverType.name}
										</option>
									</c:forEach>
								</select>
							</c:when>
							<c:otherwise>
								<div class="form-group">
									<div class="table-cell-label">
										<spring:message code="addServerDetails.server.type" var="label"></spring:message>
										<spring:message code="addServerDetails.server.type.tooltip" var="tooltip"></spring:message>
										${label}
									</div>
									<div class="input-group ">
										<select title="" data-placement="bottom" data-toggle="tooltip" class="form-control table-cell input-sm" data-original-title="${tooltip}"  id="updt-serverTypeId" name="serverTypeId" value="${server_form_bean.serverType}" disabled="disabled">
											<c:forEach var="serverType" items="${SERVER_TYPE_LIST}">
												<option value="${serverType.id }">
													${serverType.name}
												</option>
											</c:forEach>
										</select>
										<span class="input-group-addon add-on last">
											<i title="" data-placement="bottom" data-toggle="tooltip" class="glyphicon glyphicon-alert" data-original-title="">
											</i>
										</span>
									</div>
								</div>
							</c:otherwise>
						</c:choose>
					</c:if>
					<elitecore:inputHTML
									labelClass="table-cell-label"
									isMandatoryField="true" 
									type="text" 
									name="groupServerId" 
									id="updt-groupServerId" 
									i18NCode="addServerDetails.server.groupServerId"
									i18NToolTipCode="addServerDetails.server.groupServerId.tooltip"
									inputClassName="form-control table-cell input-sm" 
									inputValue='${server_form_bean.groupServerId}'>
					</elitecore:inputHTML>
					 <elitecore:inputHTML
									labelClass="table-cell-label"
									isMandatoryField="true" 
									type="text" 
									name="serverServerId" 
									id="updt-serverServerId" 
									i18NCode="addServerDetails.server.serverId"
									i18NToolTipCode="addServerDetails.server.serverId.tooltip"
									inputClassName="form-control table-cell input-sm" 
									inputValue='${server_form_bean.serverId}'
									>
					</elitecore:inputHTML> 
					
					<elitecore:inputHTML
									labelClass="table-cell-label"
									isMandatoryField="true" 
									type="text" 
									name="serverName" 
									id="updt-serverName" 
									i18NCode="addServerDetails.server.name" 
									i18NToolTipCode="addServerDetails.server.name.tooltip"
									inputClassName="form-control table-cell input-sm"
									inputValue='${server_form_bean.name}'
									 >
					</elitecore:inputHTML>
					
					<elitecore:textAreaHTML 
									labelClass="table-cell-label"
									isMandatoryField="false"
									id="updt-serverDescription" 
									name="serverDescription" 
									rows="3"
									inputClassName="form-control input-sm"
									i18NCode="addServerDetails.server.description"
									i18NToolTipCode="addServerDetails.server.description.tooltip"
									inputValue='${server_form_bean.description}'>
					</elitecore:textAreaHTML>
					
					<c:choose>
					    <c:when test="${SERVER_INSTANCE_LIST != null && fn:length(SERVER_INSTANCE_LIST) gt 0}">
							<div class="form-group">
		               			<spring:message code="addServerDetails.server.ip.address" var="label"></spring:message>
		               			<spring:message code="addServerDetails.server.ip.address.tooltip" var="tooltip"></spring:message>
		            			<div class="table-cell-label">${label}<span class="required">*</span></div>
		                		<div class="input-group">
		                			<input type="text" name="serverIPAddress" id="updt-serverIPAddress" class="form-control table-cell input-sm" data-toggle="tooltip" data-placement="bottom" title="${tooltip}"  disabled="disabled" value='${server_form_bean.ipAddress}' >
		                			<span class="input-group-addon add-on last" > <i class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="left" title=""></i></span>
		                		</div>
		               		</div>
					    </c:when>    
					    <c:otherwise>
					       <div class="form-group">
		               			<spring:message code="addServerDetails.server.ip.address" var="label"></spring:message>
		               			<spring:message code="addServerDetails.server.ip.address.tooltip" var="tooltip"></spring:message>
		            			<div class="table-cell-label">${label}<span class="required">*</span></div>
		                		<div class="input-group">
		                			<input type="text" name="serverIPAddress" id="updt-serverIPAddress" class="form-control table-cell input-sm" data-toggle="tooltip" data-placement="bottom" title="${tooltip}"  value='${server_form_bean.ipAddress}' >
		                			<span class="input-group-addon add-on last" > <i class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="left" title=""></i></span>
		                		</div>
		               		</div>
					    </c:otherwise>
					</c:choose>
					
					<elitecore:inputHTML
									labelClass="table-cell-label"
									isMandatoryField="true" 
									type="text" 
									name="serverUtilityPort" 
									id="updt-serverUtilityPort" 
									i18NCode="addServerDetails.server.utility.port" 
									i18NToolTipCode="addServerDetails.server.utility.port"
									inputClassName="form-control table-cell input-sm"
									inputValue='${server_form_bean.utilityPort}'
									isNumeric="true" >
					</elitecore:inputHTML>
					
				<%-- <div name="containerEnvironment_div" class="form-group">
					<div class="table-cell-label">
						<spring:message code="addServerDetails.server.container.environment" var="label" ></spring:message>
						<spring:message code="addServerDetails.server.container.environment" var="tooltip" ></spring:message>
						${label}
					</div>
					<div class="input-group">
						<select class='form-control table-cell input-sm' id='containerEnvironment' data-toggle='tooltip' data-placement='bottom' title='${tooltip}' name="containerEnvironment">
							<c:forEach var='truefalseEnum' items='${truefalseEnum}'>
								<option value='${truefalseEnum.name}'>${truefalseEnum}</option>
							</c:forEach>
						</select> 
						<span class="input-group-addon add-on last"> 
							<i id="containerEnvironment_err" title="" data-placement="left" data-toggle="tooltip" class="glyphicon glyphicon-alert"	data-original-title=""> </i>
						</span>
					</div>
				</div> --%>
					
				</div>
				<div id="buttons-div" class="modal-footer padding10">
					<sec:authorize access="hasAuthority('UPDATE_SERVER_AND_SERVER_INSTANCES')">
						<button type="button" class="btn btn-grey btn-xs " id="btnUpdate" onclick="updateServerToDB()">
							<spring:message code="btn.label.update" ></spring:message>
						</button>
					</sec:authorize>
					<button type="button" class="btn btn-grey btn-xs " id="btnCancel" onclick="javascript:parent.closeFancyBoxFromChildIFrame()">
						<spring:message code="btn.label.cancel" ></spring:message>
					</button>
					<button type="button" class="btn btn-grey btn-xs " id="btnClose"  onclick="javascript:parent.closeFancyBoxFromChildIFrame()" style="display:none;">
						<spring:message code="btn.label.close" ></spring:message>
					</button>
					
				</div>
				<div id="progress-bar-div" class="modal-footer padding10" style="display: none;">
					<jsp:include page="../common/processing-bar.jsp"></jsp:include>
				</div>
				<script type="text/javascript">
					$(document).ready(function() {
						$("#serverContentDiv input, select, textarea").keypress(function (event) {
						    if (event.which == 13) {
						        event.preventDefault();
						        $(this).blur();
						        updateServerToDB();
						    }
						});
						
						$('#updt-serverTypeId').val('${server_form_bean.serverType.id}');
					});
					function getGroupIds(tagName,response) {
						debugger
						$.ajax({
							url : 'getGroupIdList',
							type : 'GET',
							cache : false,
							async : false,
							data : {
								"tagName" : tagName,
							},
							success : function(data) {
								response($.ui.autocomplete.filter(data,tagName));
							},
							error : function(xhr, st, err) {
								alert("Error");
							}
						});
					}
					
				    $( "#updt-groupServerId" ).autocomplete({
						    source: function(request, response) {
						    getGroupIds(request.term,response);
						 	},
						    appendTo : $( "#updt-groupServerId" ).parent(),
				    });
					
				</script>
			</div>
			<!-- /.modal-content -->
		</div>
	</body>
</html>
