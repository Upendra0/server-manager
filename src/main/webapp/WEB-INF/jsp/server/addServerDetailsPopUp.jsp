<%@taglib 	uri="http://www.springframework.org/tags" prefix="spring"%>
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
	<body class="skin-blue sidebar-mini sidebar-collapse">
		<div id="serverDetails" style="width: 100%; ">
			<div class="modal-content">
				<div class="modal-header padding10">
					<h4 class="modal-title">
						<i class="icon-hdd"></i> 
						<spring:message code="serverManager.create.server" ></spring:message>
					</h4>
				</div>
				<div id="serverContentDiv" class="modal-body padding10 inline-form">
					<jsp:include page="../common/responseMsgPopUp.jsp" ></jsp:include>
					
					<c:if test="${SERVER_TYPE_LIST != null && fn:length(SERVER_TYPE_LIST) gt 0}">
						
								<div name="create_server_param" class="form-group">
									<div class="table-cell-label">
										<spring:message code="addServerDetails.server.type" var="label"></spring:message>
										<spring:message code="addServerDetails.server.type.tooltip" var="tooltip"></spring:message>
										${label}
									</div>
									<div class="input-group ">
										<select title="" data-placement="bottom" data-toggle="tooltip" class="form-control table-cell input-sm" data-original-title="${tooltip}"  id="serverTypeId" name="serverTypeId">
											<c:forEach var="serverType" items="${SERVER_TYPE_LIST}">
												<option value="${serverType.id }">
													${serverType.name}
												</option>
											</c:forEach>
										</select>
										<span class="input-group-addon add-on last">
											<i id="server_type_err" title="" data-placement="left" data-toggle="tooltip" class="glyphicon glyphicon-alert" data-original-title="">
											</i>
										</span>
									</div>
								</div>
					</c:if>
					 
					 <elitecore:inputHTML
									labelClass="table-cell-label"
									isMandatoryField="true" 
									type="text" 
									name="groupServerId" 
									id="groupServerId" 
									i18NCode="addServerDetails.server.groupServerId"
									i18NToolTipCode="addServerDetails.server.groupServerId.tooltip"
									inputClassName="form-control table-cell input-sm" >
					</elitecore:inputHTML>					
					 <elitecore:inputHTML
									labelClass="table-cell-label"
									isMandatoryField="true" 
									type="text" 
									name="serverServerId" 
									id="serverServerId" 
									i18NCode="addServerDetails.server.serverId"
									i18NToolTipCode="addServerDetails.server.serverId.tooltip"
									inputClassName="form-control table-cell input-sm" >
					</elitecore:inputHTML> 
					
					<elitecore:inputHTML
									labelClass="table-cell-label"
									isMandatoryField="true" 
									type="text" 
									name="serverName" 
									id="serverName" 
									i18NCode="addServerDetails.server.name"
									i18NToolTipCode="addServerDetails.server.name.tooltip"
									inputClassName="form-control table-cell input-sm" >
					</elitecore:inputHTML>
					
					<elitecore:textAreaHTML 
									labelClass="table-cell-label"
									isMandatoryField="false"
									id="serverDescription" 
									name="serverDescription" 
									rows="3"
									inputClassName="form-control input-sm"
									i18NCode="addServerDetails.server.description"
									i18NToolTipCode="addServerDetails.server.description.tooltip">
					</elitecore:textAreaHTML>
					
					<elitecore:inputHTML
									labelClass="table-cell-label"
							    	isMandatoryField="true"  
									type="text" 
									name="serverIPAddress" 
									id="serverIPAddress" 
									i18NCode="addServerDetails.server.ip.address" 
									i18NToolTipCode="addServerDetails.server.ip.address.tooltip"
									inputClassName="form-control table-cell input-sm" >
					</elitecore:inputHTML>
					
					<elitecore:inputHTML
									labelClass="table-cell-label"
									isMandatoryField="true"   
									type="text" 
									name="serverUtilityPort" 
									id="serverUtilityPort" 
									i18NCode="addServerDetails.server.utility.port" 
									i18NToolTipCode="addServerDetails.server.utility.port"
									inputClassName="form-control table-cell input-sm"
									isNumeric="true" >
					</elitecore:inputHTML>

			<%-- 	<div name="containerEnvironment_div" class="form-group">
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
					<button id="btn_create" type="button" class="btn btn-grey btn-xs " onclick="addNewServerToDB('<c:out value="${param.parentScreen}"></c:out>')">
						<spring:message code="btn.label.create" ></spring:message>
					</button>
					<button id="btn_cancel" type="button" onclick="parent.closeFancyBoxFromChildIFrame();" class="btn btn-grey btn-xs ">
						<spring:message code="btn.label.cancel" ></spring:message>
					</button>
				</div>
				<div id="progress-bar-div" class="modal-footer padding10" style="display: none;">
					<jsp:include page="../common/processing-bar.jsp"></jsp:include>
				</div>
				<script type="text/javascript">
					$(document).ready(function() {
						
						// Set default value for utility port
						$('#serverUtilityPort').val('${server_form_bean.utilityPort}');
						
						$("#serverContentDiv").keypress(function (event) {
						    if (event.which == 13) {
						        event.preventDefault();
						        $(this).blur();
						        addNewServerToDB('<c:out value="${param.parentScreen}"></c:out>');
						    }
						});
						$("#serverDetailsAnchor").fancybox({
							   "afterLoad": function(){
									$("#serverName").val('');
									$("#serverServerId").val('');
									$("#serverDescription").val('');
									$("#serverIPAddress").val('');
									$("#serverUtilityPort").val('');
									$("#serverTypeId").val($("#serverTypeId option:first").val());
									$("#groupServerId").val('DEFAULT');
									$("#create-service-buttons-div").show();
									resetWarningDisplayPopUp();
									clearAllMessagesPopUp();
							   },
						});
						
					});		
					
					function getGroupIds(tagName,response) {
						$.ajax({
							url : 'getGroupIdList',
							type : 'GET',
							cache : false,
							async : true,
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
					
				    $( "#groupServerId" ).autocomplete({
						    source: function(request, response) {
						    getGroupIds(request.term,response);
						 	},
						    appendTo : $( "#groupServerId" ).parent(),
				    });
					
				</script>
			</div>
			<!-- /.modal-content -->
		</div>
	</body>
</html>
