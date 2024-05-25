<%@page import="com.elitecore.sm.common.constants.BaseConstants"%>
<%@ taglib uri="http://www.springframework.org/security/tags"
	prefix="sec"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="elitecore"%>
<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<%-- <%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %> --%>
<head>
<script src="${pageContext.request.contextPath}/js/bootstrap.min.js"
	type="text/javascript"></script>
<script type='text/javascript'
	src="${pageContext.request.contextPath}/js/jquery.ajaxfileupload.js"></script>
<script src="${pageContext.request.contextPath}/js/bootstrap.js" type="text/javascript"></script>
</head>

<div id="create-server-block"
	class="col-xs-8 col-sm-9 col-md-10 content-scroll page-section padding0">
	<p class="">
		<strong> <spring:message
				code="serverManager.create.server.label" ></spring:message>
		</strong>
	</p>
	<div id="cart">
		<div class="ui-widget-content">
			<div class="box box-warning">
				<div class="box-header with-border">
					<h3 class="box-title header_text" id="header1">
						<spring:message code="serverManager.droppable.region.label" ></spring:message>
					</h3>
					<div class="box-tools pull-right" style="height: 100%">
						<button class="btn btn-box-tool" data-widget="collapse">
							<i class="fa fa-minus"></i>
						</button>
					</div>
				</div>
				<div class="box-body">
					<ol style="width: 100%; list-style: none;">
						<li>
							<table class="full-size">
								<tr>
									<td id="serverDropArea" width="20%"
										class="text-center grey-bg box-border ui-droppable ui-sortable">
										<em class="mtop40"> <strong> <spring:message
													code="serverManager.drag.server" ></spring:message>
										</strong>
									</em>
									</td>
									<td width="80%">
										<div class="col-md-6 ">
											<div class="grey-bg box-border padding15 text-center">
												<em> <strong id="dragServerInstanceStrongId">
														<spring:message code="serverManager.drag.server.instance" ></spring:message>
												</strong>
												</em>
											</div>
										</div>
									</td>
								</tr>
							</table>
						</li>
					</ol>
				</div>
				<jsp:include page="deleteServerConfirmationPopUp.jsp"></jsp:include>
				<!-- Initialize all draggable and droppable Javascripts -->
				
					<div id="addServer" style=" width:100%; display: none;" >
		   				<jsp:include page="addServerDetailsPopUp.jsp"></jsp:include>
					</div>
					<a style="display: none;" class="fancybox" id="serverDetailsAnchor" href="#addServer"></a>
					<a style="display: none;" class="fancybox fancybox.iframe"
					id="addServerInstanceAnchor" href="initAddServerInstance">Server
					Instance Details</a> <a style="display: none;"
					class="fancybox fancybox.iframe" id="updtServerInstanceAnchor"
					href="initUpdateServer">Server Instance Details</a>

				<script type="text/javascript">
							$(document).ready(function() {
								initializeDnDServerAndServerInstanceComponents();
							});
						</script>

				<script type='text/javascript'>
						
						
						 
						<sec:authorize access="!hasAuthority('VIEW_SERVER_AND_SERVER_INSTANCES')">
							updateAccessStatus = false;
						</sec:authorize>
						
						<sec:authorize access="hasAuthority('VIEW_SERVER_AND_SERVER_INSTANCES')">
								updateAccessStatus = true;
						</sec:authorize>
						<sec:authorize access="!hasAuthority('DELETE_SERVER_AND_SERVER_INSTANCES')">
								deleteAccessStatus = false;
						</sec:authorize>
							<sec:authorize access="hasAuthority('DELETE_SERVER_AND_SERVER_INSTANCES')">
								deleteAccessStatus = true;
						</sec:authorize>
					
						/* <sec:authorize access="!hasAuthority('UPDATE_SERVER_AND_SERVER_INSTANCES')">
								updateAccessStatus = false;
						</sec:authorize>
						<sec:authorize access="hasAuthority('UPDATE_SERVER_AND_SERVER_INSTANCES')">
								updateAccessStatus = true;
						</sec:authorize> */
				
							 
						</script>

				<c:if test="${SERVER_LIST != null }">
					<c:forEach var="server" items="${SERVER_LIST}">
						<script type='text/javascript'>
									$(document).ready(function() {
											addNewServer('${server.name}','${server.id}', '${server.ipAddress}','${server.utilityPort}','${server.serverType.name}',updateAccessStatus,deleteAccessStatus);
									});
								</script>
					</c:forEach>
				</c:if>

				<c:if test="${SERVER_INSTANCE_LIST != null }">
					<c:forEach var="serverInstance" items="${SERVER_INSTANCE_LIST}">
						<script type="text/javascript">
								$(document).ready(function() {
									try {
											addNewServerInstance('${serverInstance.server.id}', '${serverInstance.id}', '${serverInstance.port}','${serverInstance.name}','${serverInstance.server.ipAddress}','${serverInstance.status}',updateAccessStatus,deleteAccessStatus);
									} catch (e) {
										// do nothing
									}
								});
								</script>
					</c:forEach>
				</c:if>
			</div>
		</div>
	</div>
	<div id="cart-added" style="font-size: 100%;"></div>
</div>

<div id="createServerAndServerInstanceComponentDiv"
	class="col-xs-4 col-sm-3 col-md-2">
	<div class="box-border page-section">

		<h5 class="section-title padding5 borbot margin0 grey-bg">
			<strong> <spring:message code="serverManager.components" ></spring:message>
			</strong>
		</h5>
		<ul class="text-center list-unstyled padding0 mtop10"
			id="server-components">
			<li class="ui-draggable ui-draggable-handle" id="Servers"><img
				alt="Server" src="img/server.png" class="img-responsive" /> <label
				style="display: block;"> <strong> <spring:message
							code="serverManager.server" ></spring:message>
				</strong>
			</label></li>
			<li class="ui-draggable ui-draggable-handle mtop20"
				id="Server_Instance"><img alt="Server Instance"
				src="img/server-instance.png" class="img-responsive" /> <label
				style="display: block;"> <strong> <spring:message
							code="serverManager.server.instance" ></spring:message>
				</strong>
			</label></li>
		</ul>
	</div>
</div>

<div style="display: none;">
	<form id="instance_form" method="POST"
		action="<%= ControllerConstants.INIT_UPDATE_SERVER_INSTANCE %>">
		<input type="hidden" id="serverInstanceId" name="serverInstanceId">
	</form>
</div>

<a href="#server-instance" class="fancybox" style="display: none;"
	id="addInstance">#</a>

<div id="server-instance" style="width: 100%; display: none;">
	<form:form modelAttribute="server_instance_form_bean" method="POST"
		action="<%= ControllerConstants.ADD_SERVER_INSTANCE %>"
		id="serverInstance-form" enctype="multipart/form-data">
		<div class="modal-content"
			style="background-color: white; border: none;">
			<div class="modal-header padding10">
				<h4 class="modal-title">
					<i class="icon-hdd"></i>
					<spring:message code="addServerInstance.instance.heading" ></spring:message>
				</h4>
			</div>

			<div id="serverInstanceContentDiv"
				class="modal-body padding10 inline-form">
				<jsp:include page="../common/responseMsgPopUp.jsp" ></jsp:include>
				<div id="divValidationList"></div>
				<div id="createServerInstanceContent">
					<div>

						<form:input type="hidden" path="server.id" id="serverId" ></form:input>
						<form:input type="hidden" path="server.utilityPort"
							name="utilityPort" id="utilityPort" ></form:input>
						<form:input type="hidden" path="logsDetail.rollingValue"
							name="utilityPort" id="utilityPort" ></form:input>
						<form:input type="hidden" path="logsDetail.maxRollingUnit"
							name="utilityPort" id="utilityPort" ></form:input>
						<form:input type="hidden" path="thresholdMemory"
							name="utilityPort" id="utilityPort" ></form:input>
						<form:input type="hidden" path="thresholdTimeInterval"
							name="utilityPort" id="utilityPort" ></form:input>
						<form:input type="hidden" path="loadAverage" name="utilityPort"
							id="utilityPort" ></form:input>

						<input type="hidden" name="importFile" id="importFile" value="" />

						<div class="col-md-12 no-padding">
							<div class="form-group">
								<spring:message code="addServerInstance.instance.name"
									var="label" ></spring:message>
								<spring:message code="addServerInstance.instance.name.tooltip"
									var="tooltip" ></spring:message>
								<div class="table-cell-label">${label}<span
										class="required">*</span>
								</div>
								<div class="input-group">
									<form:input path="name" id="name" tabindex="1"
										cssClass="form-control table-cell input-sm"
										data-toggle="tooltip" data-placement="bottom"
										title="${tooltip}" ></form:input>
									<span class="input-group-addon add-on last"> <i
										id = "server_instance_name_err" class="glyphicon glyphicon-alert" data-toggle="tooltip"
										data-placement="left" title=""></i></span>
								</div>
							</div>
						</div>
						<div class="col-md-12 no-padding">
							<div class="form-group">
								<spring:message code="addServerInstance.instance.description"
									var="label" ></spring:message>
								<spring:message
									code="addServerInstance.instance.description.tooltip"
									var="tooltip" ></spring:message>
								<div class="table-cell-label">${label}</div>
								<div class="input-group">
									<form:textarea path="description" tabindex="2" id="description"
										rows="2" cssClass="form-control input-sm"
										data-toggle="tooltip" data-placement="bottom"
										title="${tooltip}" ></form:textarea>
									<span class="input-group-addon add-on last"> <i
										class="glyphicon glyphicon-alert" data-toggle="tooltip"
										data-placement="left" title=""></i></span>
								</div>
							</div>
						</div>
						<div class="col-md-6 no-padding">
							<div class="form-group">
								<spring:message code="addServerInstance.instance.server.ip"
									var="label" ></spring:message>
								<spring:message
									code="addServerInstance.instance.server.ip.tooltip"
									var="tooltip" ></spring:message>
								<div class="table-cell-label">${label}<span
										class="required">*</span>
								</div>
								<div class="input-group">
									<form:input path="server.ipAddress" tabindex="3"
										id="serverHostIp" cssClass="form-control table-cell input-sm"
										data-toggle="tooltip" data-placement="bottom"
										title="${tooltip}" readonly="true" ></form:input>
									<span class="input-group-addon add-on last"> <i
										class="glyphicon glyphicon-alert" data-toggle="tooltip"
										data-placement="left" title=""></i></span>
								</div>
							</div>
						</div>
						<div class="col-md-6 no-padding">
							<div class="form-group">
								<spring:message code="addServerInstance.instance.port"
									var="label" ></spring:message>
								<spring:message code="addServerInstance.instance.port.tooltip"
									var="tooltip" ></spring:message>
								<div class="table-cell-label">${label}<span
										class="required">*</span>
								</div>
								<div class="input-group">
									<form:input path="port" id="port" tabindex="4"
										cssClass="form-control table-cell input-sm"
										onkeypress="return isNumber(arguments[0] || window.event);"
										data-toggle="tooltip" data-placement="bottom"
										title="${tooltip}" onblur="generateScript()" ></form:input>
									<span class="input-group-addon add-on last"> <i
										id = "server_instance_port_err" class="glyphicon glyphicon-alert" data-toggle="tooltip"
										data-placement="left" title=""></i></span>
								</div>
							</div>
						</div>
						<div class="col-md-6 no-padding">
							<spring:message code="addServerInstance.instance.min.memory"
								var="tooltip" ></spring:message>
							<div class="form-group">
								<div class="table-cell-label">
									<spring:message
										code="addServerInstance.instance.min.memory.alloc" ></spring:message>
									<span class="required">*</span>
								</div>
								<div class="input-group">
									<form:input path="minMemoryAllocation" tabindex="5"
										id="minMemoryAllocation"
										cssClass="form-control table-cell input-sm"
										data-toggle="tooltip" data-placement="bottom"
										title="${tooltip}"
										onkeypress="return isNumber(arguments[0] || window.event);" ></form:input>
									<span class="input-group-addon add-on last"> <i
										id = "min_memory_err" class="glyphicon glyphicon-alert" data-toggle="tooltip"
										data-placement="left" title=""></i></span>
								</div>
							</div>
						</div>
						<div class="col-md-6 no-padding">
							<div class="form-group">
								<spring:message code="addServerInstance.instance.max.memory"
									var="tooltip" ></spring:message>
								<div class="table-cell-label">
									<spring:message
										code="addServerInstance.instance.max.memory.alloc" ></spring:message>
									<span class="required">*</span>
								</div>
								<div class="input-group">
									<form:input path="maxMemoryAllocation" tabindex="6"
										id="maxMemoryAllocation"
										cssClass="form-control table-cell input-sm"
										data-toggle="tooltip" data-placement="bottom"
										title="${tooltip}"
										onkeypress="return isNumber(arguments[0] || window.event);" ></form:input>
									<span class="input-group-addon add-on last"> <i
										id = "max_memory_err" class="glyphicon glyphicon-alert" data-toggle="tooltip"
										data-placement="left" title=""></i></span>
								</div>
							</div>
						</div>
						<div class="col-md-6 no-padding">
							<div class="form-group">
								<spring:message code="addServerInstance.instance.script"
									var="placeholder" ></spring:message>
								<spring:message code="addServerInstance.instance.script.tooltip"
									var="tooltip" ></spring:message>
								<div class="table-cell-label">${placeholder}</div>
								<div class="input-group">
									<form:input path="scriptName" id="scriptName" tabindex="7"
										cssClass="form-control table-cell input-sm"
										data-toggle="tooltip" data-placement="bottom"
										title="${tooltip}" ></form:input>
									<span class="input-group-addon add-on last"> <i
										id = "script_name_name_err" class="glyphicon glyphicon-alert" data-toggle="tooltip"
										data-placement="left" title=""></i></span>
								</div>
							</div>
						</div>
						<div class="col-md-6 no-padding">
							<div class="form-group">
								<spring:message
									code="addServerInstance.instance.max.connection.retry"
									var="label" ></spring:message>
								<spring:message
									code="addServerInstance.instance.max.connection.retry.tooltip"
									var="tooltip" ></spring:message>
								<div class="table-cell-label">${label}<span
										class="required">*</span>
								</div>
								<div class="input-group">
									<form:input path="maxConnectionRetry" tabindex="8"
										id="maxConnectionRetry"
										cssClass="form-control table-cell input-sm"
										data-toggle="tooltip" data-placement="bottom"
										title="${tooltip}"
										 onkeypress="return isNumber(arguments[0] || window.event);"></form:input>
									<span class="input-group-addon add-on last"> <i
										id = "max_try_err" class="glyphicon glyphicon-alert" data-toggle="tooltip"
										data-placement="left" title=""></i></span>
								</div>
							</div>
						</div>
						<div class="col-md-6 no-padding">
							<div class="form-group">
								<spring:message
									code="addServerInstance.instance.connection.retry.interval"
									var="label" ></spring:message>
								<spring:message
									code="addServerInstance.instance.connection.retry.interval.tooltip"
									var="tooltip" ></spring:message>
								<div class="table-cell-label">${label}<span
										class="required">*</span>
								</div>
								<div class="input-group">
									<form:input path="retryInterval" tabindex="9"
										id="retryInterval" cssClass="form-control table-cell input-sm"
										data-toggle="tooltip" data-placement="bottom"
										title="${tooltip}"
										onkeypress="return isNumber(arguments[0] || window.event);" ></form:input>
									<span class="input-group-addon add-on last"> <i
										id = "retry_interval_err" class="glyphicon glyphicon-alert" data-toggle="tooltip"
										data-placement="left" title=""></i></span>
								</div>
							</div>
						</div>
						<div class="col-md-6 no-padding">
							<div class="form-group">
								<spring:message
									code="addServerInstance.instance.connection.timeout"
									var="label" ></spring:message>
								<spring:message
									code="addServerInstance.instance.connection.timeout.tooltip"
									var="tooltip" ></spring:message>
								<div class="table-cell-label">${label}<span
										class="required">*</span>
								</div>
								<div class="input-group">
									<form:input path="connectionTimeout" tabindex="10"
										id="connectionTimeout"
										cssClass="form-control table-cell input-sm"
										data-toggle="tooltip" data-placement="bottom"
										title="${tooltip}"
										onkeypress="return isNumber(arguments[0] || window.event);" ></form:input>
									<span class="input-group-addon add-on last"> <i
										id = "connection_timeout_err" class="glyphicon glyphicon-alert" data-toggle="tooltip"
										data-placement="left" title=""></i></span>
								</div>
							</div>
						</div>
						<div class="col-md-12 no-padding">
							<div class="form-group">
								<spring:message code="addServerInstance.instance.port"
									var="tooltip" ></spring:message>
								<div class="table-cell-label">
									Create Type<span class="required">*</span>
								</div>
								<div id="create_type_button_group" class="input-group">
									<input type="radio" name="createMode" id="new_radio" tabindex="11"
										value="<%= BaseConstants.CREATE_MODE_NEW %>"
										onclick="changeCreateType('new')" checked="checked" />&nbsp;New&nbsp;&nbsp;&nbsp;
									<input type="radio" name="createMode" id="import_radio" tabindex="12"
										value="<%= BaseConstants.CREATE_MODE_IMPORT %>"
										onclick="changeCreateType('import')" />&nbsp;Import <input
										type="radio" name="createMode" id="copy_radio" tabindex="13"
										value="<%= BaseConstants.CREATE_MODE_COPY %>"
										onclick="changeCreateType('copy')" />&nbsp;Copy <span
										class="input-group-addon add-on last"> <i 
										id = "create_type_err" class="glyphicon glyphicon-alert" data-toggle="tooltip"
										data-placement="left" title=""></i></span>
								</div>
							</div>
						</div>
						<div class="col-md-12 no-padding" style="display: none;"
							id="divImport">
							<div class="box-body inline-form accordion-body collapse in"
								style="padding: 0px;">
								<div class="col-md-6 no-padding">
									<div class="form-group">
										<spring:message
											code="serverMgmt.import.config.popup.select.file.label"
											var="tooltip" ></spring:message>
										<div class="table-cell-label">${tooltip}<span
												class="required">*</span>
										</div>
										<div class="input-group ">
											<input type="file" tabindex="14"
												class="filestyle form-control" tabindex="14"
												data-buttonText="<spring:message code="serviceMgmt.browse.file"></spring:message>"
												id="configFile" name="configFile" accept='text/xml'>
										</div>
									</div>
								</div>
								<div class="col-md-6 no-padding">
									<div class="form-group">
										&nbsp;&nbsp;
										<button type="button" id="fileSubmit"
											class="btn btn-grey btn-xs" value="Upload">Upload</button>
										<div id="upload-progress-bar-div"
											class="modal-footer padding10 text-left"
											style="display: none; border-top: 0px; float: left;">
											<img src="img/processing-bar.gif">
										</div>
									</div>
								</div>
							</div>
						</div>
						<div class="col-md-12 no-padding" style="display: none;"
							id="divCopy">
							<div class="box-body inline-form accordion-body collapse in"
								style="height: 80px; overflow: scroll; overflow-x: hidden; padding: 0px; margin-bottom: 15px;">
								<table class="table table-hover" style="width: 730px;">
									<thead>
										<tr>
											<th width="50px"><spring:message
													code="createServerInstance.copy.table.select.label" ></spring:message></th>
											<th width="150px"><spring:message
													code="createServerInstance.copy.table.SI.label" ></spring:message></th>
											<th width="200px"><spring:message
													code="createServerInstance.copy.table.ip.label" ></spring:message></th>
											<th width="100px"><spring:message
													code="createServerInstance.copy.table.Port.label" ></spring:message></th>
										</tr>
									</thead>
									<tbody>
										<tr>
											<td colspan="4">
												<div class="scrollit">
													<table class="table table-hover" align="right"
														id="tblInstanceList">
													</table>
												</div>
											</td>
										</tr>
									</tbody>
								</table>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>

		<div id="add-buttons-div" class="modal-footer padding10">
			<button id="createServerInstanceBtn" type="button"
				class="btn btn-grey btn-xs " onclick="addNewServerInstanceToDB();">
				<spring:message code="btn.label.create" ></spring:message>
			</button>
			<button id="cancelInstanceBtn"
				onclick="closeFancyBox();resetWarningDisplay();resetCreateInstanceForm();"
				type="button" class="btn btn-grey btn-xs ">
				<spring:message code="btn.label.cancel" ></spring:message>
			</button>
			<button id="closeInstanceBtn"
				onclick="closeFancyBox();resetWarningDisplay();reloadServerInstanceGUIStatus();"
				type="button" style="display: none" class="btn btn-grey btn-xs ">
				<spring:message code="btn.label.close" ></spring:message>
			</button>
		</div>
		<div id="add-progress-bar-div"
			class="modal-footer padding10 text-left" style="display: none;">
			<jsp:include page="../common/processing-bar.jsp"></jsp:include>
		</div>
	</form:form>
</div>



<script type="text/javascript">
	$(document).ready(function() {
		
		<c:set var="instanceRunning"><spring:message code="server.instance.already.running.on.port"></spring:message></c:set>
		
		$("#serverId").val(parent.serverInstanceDragOnServerId);
		$("#serverIPAddress").val(parent.serverInstanceDragOnServerIPAddress);
		$("#serverUtilityPort").val(parent.serverInstanceDragOnServerUtilityPort);
		$("#serverHostIp").val($("#serverIPAddress").val());
		
		$("#serverInstanceContentDiv").keypress(function (event) {
		    if (event.which == 13) {
		        event.preventDefault();
		        $(this).blur();
		        addNewServerInstanceToDB();
		    }
		});
	});
	
	function generateScript(){
		var port = parent.$('#port').val();
		if(port != null && port != ''){
			parent.$("#scriptName").val('startServer_'+port+ '.sh');
		}
		else{
			parent.$("#scriptName").val('');
		}
	}
	
	function changeCreateType(type){
		if(type=='new'){
			$('#divImport').hide();
			$('#divCopy').hide();
			$('#createServerInstanceBtn').removeAttr('disabled','');
		} else if(type=='import'){
			$('#divImport').show();
			$('#divCopy').hide();
		} else if(type=='copy'){
			$('#divImport').hide();
			$('#divCopy').show();
			$('#createServerInstanceBtn').removeAttr('disabled','');
			
			//loadServerInstanceListForCopy($("#serverHostIp").val());
			loadServerInstanceListForCopy($("#serverId").val());
		}
		parent.resizeFancyBox();
	}
	
	function displayOptionType(type){
		
		if(type=='next'){
			$('#createOptionDiv').show();
			$('#backBtn').show();
			$('#nextBtn').hide();
			$('#createServerInstanceBtn').show();
			$('#serverInstanceContentDiv').hide();	
		} else if (type=='prev'){
			$('#createOptionDiv').hide();
			$('#backBtn').hide();
			$('#nextBtn').show();
			$('#createServerInstanceBtn').hide();
			$('#serverInstanceContentDiv').show();	
		}
	}
	
	function changeOptionType(type){
		if(type=='import'){
			$('#importDiv').show();
			$('#copyDiv').hide();
		} else if(type=='copy'){
			$('#importDiv').hide();
			$('#copyDiv').show();
		}
	}
	
</script>

<script type="text/javascript">

$(document)
.on(
        "change",
        "#configFile",
        function(event) {
         files=event.target.files;
         $('#configFile').html(files[0].name);
        });

$(document)
.on(
        "click",
        "#fileSubmit",
        function() {
        processUpload();
        });
       


$(document).ready(function() {
	hideServerAndServerInstanceDroppableRegions();
	
	$("#configFile").change(function (e) {
		$in=$(this);
	  	$('#configFile').html($in.val());
   });
});

function updateServer(id){
	var href = $('#updtServerInstanceAnchor').attr('href');
	if(href.indexOf('?')!= -1)
		href = href.substr(0,href.indexOf('?'));
	href = href + '?id='+id;
	$('#updtServerInstanceAnchor').attr('href',href);
	$('#updtServerInstanceAnchor').click();
}

</script>
