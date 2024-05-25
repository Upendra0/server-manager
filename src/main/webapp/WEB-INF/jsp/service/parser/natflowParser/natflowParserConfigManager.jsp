<%@page import="com.elitecore.sm.common.constants.EngineConstants"%>
<%@page import="com.elitecore.sm.common.constants.BaseConstants"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>

<!DOCTYPE html>

<html>
<script type="text/javascript">
var isAddParserAccess = false;
var isDeleteParserAccess = false;
var isEditParserAccess = false;	
var isViewParserAccess = false;

var ckIntanceSelected = new Array();
var currentParserType = '${plugInType}';
var jsSpringMsg = {};
	
	jsSpringMsg.selVendorType="<spring:message code='vendor.type.dropdown.default.selection.label'></spring:message>";
	jsSpringMsg.selDevice="<spring:message code='device.name.dropdown.default.selection.label'></spring:message>";
	jsSpringMsg.selmappingName="<spring:message code='mapping.name.dropdown.default.selection.label'></spring:message>";
	
	jsSpringMsg.attributeName="<spring:message code='parsing.service.attr.grid.column.attr.name' ></spring:message>";
	jsSpringMsg.unifiedFieldName="<spring:message code='parsing.service.attr.grid.column.unified.field' ></spring:message>";
	jsSpringMsg.description="<spring:message code='parsing.service.attr.grid.column.desc' ></spring:message>";
	jsSpringMsg.defaultVal="<spring:message code='parsing.service.attr.grid.column.default.val' ></spring:message>";
	jsSpringMsg.trimChar="<spring:message code='parsing.service.attr.grid.column.trin.char' ></spring:message>";
	jsSpringMsg.trimPosition="<spring:message code='parsing.service.attr.grid.column.trim.position' ></spring:message>";
	jsSpringMsg.updateAction="<spring:message code='staffManager.audit.search.action.lable.name'></spring:message>";
	jsSpringMsg.dateFormat="<spring:message code='parser.grid.date.format.label' ></spring:message>";
	jsSpringMsg.sourceFieldFormat="<spring:message code='parsing.service.attr.grid.column.source.field.format' ></spring:message>";
	jsSpringMsg.sourceFieldFormat="<spring:message code='parsing.service.attr.grid.column.source.field.format' ></spring:message>";
	jsSpringMsg.destDateFormat="<spring:message code='parsing.service.attr.grid.column.dest.date.format' ></spring:message>";
	
	jsSpringMsg.recordtext="<spring:message code='parsing.service.attr.grid.pager.total.records.text'></spring:message>";
	jsSpringMsg.emptyrecords= "<spring:message code='parsing.service.attr.grid.empty.records'></spring:message>";
	jsSpringMsg.loadtext="<spring:message code='parsing.service.attr.grid.loading.text'></spring:message>";
	jsSpringMsg.pgtext="<spring:message code='parsing.service.attr.grid.pager.text'></spring:message>";
	jsSpringMsg.caption= "<spring:message code='parsing.service.attr.grid.caption'></spring:message>";
	
	jsSpringMsg.srno="<spring:message code='device.attribute.list.details.sr.no.label'></spring:message>";
	jsSpringMsg.serverInsName="<spring:message code='device.association.details.server.instance.name.label'></spring:message>";
	jsSpringMsg.serverIPPORT="<spring:message code='device.association.details.server.ip.port.label'></spring:message>";
	jsSpringMsg.serviceInsName="<spring:message code='device.association.details.service.instance.name.label'></spring:message>";
	jsSpringMsg.pluginName="<spring:message code='device.association.details.plugin.name.label'></spring:message>";
	
	jsSpringMsg.attrNotFound="<spring:message code='device.mapping.attribute.not.found'></spring:message>";
	
	jsSpringMsg.natflowParser='<%=EngineConstants.NATFLOW_PARSING_PLUGIN%>';
	jsSpringMsg.natflowASNParser='<%=EngineConstants.NATFLOW_ASN_PARSING_PLUGIN%>';
	jsSpringMsg.asciiParser='<%=EngineConstants.ASCII_PARSING_PLUGIN%>';
	
	jsSpringMsg.failUpdateAtributeMsg = "<spring:message code='update.mapping.fail'></spring:message>";
	
	jsSpringMsg.mappingCustomLabel = "<spring:message code='mapping.dropdown.custom.option.label'></spring:message>";
	jsSpringMsg.mappingNoneLabel = "<spring:message code='mapping.dropdown.none.option.label'></spring:message>";
	jsSpringMsg.mappingDefault = "<spring:message code='mapping.dropdown.default.option.label'></spring:message>";
	
	jsSpringMsg.invalidDeviceType = "<spring:message code='parserConfiguration.invalid.device.type.message'></spring:message>";
	jsSpringMsg.invalidVendorType = "<spring:message code='parserConfiguration.invalid.vendor.type.message'></spring:message>";
	jsSpringMsg.invalidDevice = "<spring:message code='parserConfiguration.invalid.device.message'></spring:message>";
	jsSpringMsg.invalidMapping = "<spring:message code='parserConfiguration.invalid.mapping.message'></spring:message>";
	
	jsSpringMsg.importantNote = "<spring:message code='label.important.note'></spring:message>";
	jsSpringMsg.associationContent = "<spring:message code='parser.configuration.update.mapping.association.content'></spring:message>";
	
	var selMappingId = 0;
	var urlAction  = '';
	
</script>
<jsp:include page="../../../common/newheader.jsp"></jsp:include>
<body class="skin-blue sidebar-mini">
<div class="wrapper"> 
    
    <!-- Header Start -->
     
    <jsp:include page="../../../common/newtopNavigationPanel.jsp"></jsp:include>
     	 
    <!-- Header End -->
    
    <jsp:include page="../../../common/newleftMenu.jsp"></jsp:include> 
    
    <!-- Content Wrapper. Contains page content -->
    <div class="content-wrapper"> 
        <!-- Content Header (Page header) -->
        <section class="content-header">
            <div class="fullwidth">
                <div style="padding:0 10px 0 4px;">
                    <div id="content-scroll-d" class="content-scroll"> 
                        
                        <!-- Content Wrapper. Contains page content Start -->
                        <div class="fullwidth">
                            <h4 style="margin-top:10px; margin-bottom:0px; font-size:11px;">
                            	<a href="home.html"><img src="img/tile-icon.png" class="vmiddle" alt="Title Icon"/></a> 
                            	<span class="spanBreadCrumb" style="line-height: 30px;">
                            		<strong>
                            		<a href="<%= ControllerConstants.INIT_SERVICE_MANAGER%>">
                            			<spring:message code="collectionService.leftmenu.home" ></spring:message>
                            		</a> &nbsp; /
                            		<a id="back" href="javascript:;" onclick="goToService();">
                            		   ${serviceName}&nbsp;-&nbsp;${serviceInstanceId}
                            		 </a> &nbsp; 
                            		 </strong>
                            	</span>
                            	<ol class="breadcrumb1 breadcrumb">
                                	<li>${plugInType}</li>
                                	<li>${plugInName}</li>
                              	</ol>
                            </h4>
                            <jsp:include page="../../../common/responseMsg.jsp" ></jsp:include>	
                           <div class="row">
									<div class="col-xs-12">
										<div class="box martop" style="border: none; box-shadow: none;">
											<div class="box-body table-responsive no-padding">
												<div class="nav-tabs-custom tab-content">
														<ul class="nav nav-tabs pull-right">
															<sec:authorize access="hasAnyAuthority('VIEW_PARSER')">
																<li class="<c:if test="${(REQUEST_ACTION_TYPE eq 'NETFLOW_PARSER_CONFIGURATION')}">
																	<c:out value="active"></c:out></c:if>"	onclick="showButtons('parser_configuration');loadNatflowTabData('<%=ControllerConstants.INIT_NATFLOW_PARSER_MANAGER%>','','GET','natflow_form');" >
																	<a href="#parser-configuration" id="parser_configuration" data-toggle="tab" aria-expanded="false" style="cursor: pointer"> 
																		<spring:message code="parsing.service.config.tab.header"></spring:message>	
																	</a>
																</li>
															</sec:authorize>
															<sec:authorize access="hasAnyAuthority('VIEW_PARSER')">
																<li
																	class="<c:if test="${(REQUEST_ACTION_TYPE eq 'NETFLOW_PARSER_ATTRIBUTE')}"><c:out value="active"></c:out></c:if>"
																	onclick="showButtons('parser_attribute_configuration');loadNatflowAttributeTabData('<%=ControllerConstants.INIT_NETFLOW_PARSER_ATTRIBUTE%>','','POST','natflow_attribute_form');">
																	<a href="#parser-attribute-configuration" id="parser_attribute_configuration"	data-toggle="tab" aria-expanded="true"> 
																	<spring:message code="parsing.service.attr.list.tab.header"></spring:message>
																</a>
																</li>
															</sec:authorize>
														</ul>
														<div class="fullwidth no-padding tab-content">
														<sec:authorize access="hasAnyAuthority('VIEW_PARSER')">
															<div id="parser-configuration"
																class="tab-pane <c:if test="${(REQUEST_ACTION_TYPE eq 'NETFLOW_PARSER_CONFIGURATION')}"><c:out value="active"></c:out></c:if> ">
														 		<c:if test="${(REQUEST_ACTION_TYPE eq 'NETFLOW_PARSER_CONFIGURATION')}">
														 			 <jsp:include page="natflowConfiguration.jsp" ></jsp:include> 
														 		</c:if>	 

															</div>
														</sec:authorize>
														<sec:authorize access="hasAnyAuthority('VIEW_PARSER')">
															<div id="parser-attribute-configuration"
																class="tab-pane  <c:if test="${(REQUEST_ACTION_TYPE eq 'NETFLOW_PARSER_ATTRIBUTE')}"><c:out value="active"></c:out></c:if>">
																<c:if test="${(REQUEST_ACTION_TYPE eq 'NETFLOW_PARSER_ATTRIBUTE')}">
																	<jsp:include page="natflowAttribute.jsp" ></jsp:include>
																</c:if>	

															</div>
														</sec:authorize>
														</div>
												</div>
											</div>
										</div>
									</div>
								</div>
                        </div>
                        <!-- Content Wrapper. Contains page content End --> 
                    </div>
                </div>
            </div>
        </section>
        	<div style="display: block;">
        	
				<form id="netflow-parser-form" action = "" method="">
					<input type="hidden" name="requestAction"  id="requestAction" value = " " />
					<input type="hidden" name="serviceId"  id="serviceId" value="${serviceId}"/>
					<input type="hidden" id="serviceInstanceId" name="serviceInstanceId" value="${serviceInstanceId}">
					<input type="hidden" name="natFlowParserId"  id="natFlowParserId" value="${natflowParserId}"/>
				</form>
				<form id="go-to-service-form" action = "<%=ControllerConstants.INIT_UPDATE_SERVICE %>" method="GET">
					<input type="hidden" id="serviceId" name="serviceId" value="${serviceId}">
					<input type="hidden" id="serviceType" name="serviceType" value="${serviceType}" />	
					<input type="hidden" id="serviceName" name="serviceName" value="${serviceName}">
					<input type="hidden" id="instanceId" name="instanceId" value="${instanceId}">
					<input type="hidden" id="serviceInstanceId" name="serviceInstanceId" value="${serviceInstanceId}">
				</form>
				  
			    <form action="" method="" id="natflow_form" name="natflow_form">
			 		<input type="hidden" id="serviceId" name="serviceId" value="${serviceId}">
					<input type="hidden" id="serviceType" name="serviceType" value="${serviceType}" />	
					<input type="hidden" id="serviceName" name="serviceName" value="${serviceName}">
					<input type="hidden" id="serviceInstanceId" name="serviceInstanceId" value="${serviceInstanceId}">
					<input type="hidden" id="instanceId" name="instanceId" value="${serverInstanceId}">
					<input type="hidden" id="<%=BaseConstants.REQUEST_ACTION_TYPE %>" name="<%=BaseConstants.REQUEST_ACTION_TYPE %>" value="">
					
					<input type="hidden" value="${plugInId}" name="plugInId" id="plugInId" />
					<input type="hidden" value="${plugInName}" name="plugInName" id="plugInName" />
					<input type="hidden" value="${plugInType}" name="plugInType" id="plugInType" />		
			    </form>
			    
			    <form action="" method="" id="natflow_attribute_form" name="natflow_attribute_form">
			 		<input type="hidden" id="serviceId" name="serviceId" value="${serviceId}">
					<input type="hidden" id="serviceType" name="serviceType" value="${serviceType}" />	
					<input type="hidden" id="serviceName" name="serviceName" value="${serviceName}">
					<input type="hidden" id="serviceInstanceId" name="serviceInstanceId" value="${serviceInstanceId}">
					<input type="hidden" id="instanceId" name="instanceId" value="${serverInstanceId}">
					<input type="hidden" id="<%=BaseConstants.REQUEST_ACTION_TYPE %>" name="<%=BaseConstants.REQUEST_ACTION_TYPE %>" value="${REQUEST_ACTION_TYPE}">
					
					<input type="hidden" value="${plugInId}" name="plugInId" id="plugInId" />
					<input type="hidden" value="${plugInName}" name="plugInName" id="plugInName" />
					<input type="hidden" value="${plugInType}" name="plugInType" id="plugInType" />
					<input type="hidden" value="" name="selAttributeMappingId" id="selAttributeMappingId" />
					<input type="hidden" value="" name="selAttributeMappingName" id="selAttributeMappingName" />	
					<input type="hidden" value="true" name="readOnlyFlagAttrubute" id="readOnlyFlagAttrubute" />	
					<input type="hidden" value="${deviceName}" name="selDeviceName" id="selDeviceName" />
						
			    </form>
			</div>
    </div>
    <!-- /.content-wrapper --> 
    
    <!-- Customize mapping pop-up code start here  -->
   
    <a href="#customize_mapping_div" class="fancybox" style="display: none;" id="customize_mapping_link">#</a>
   <form:form modelAttribute="natflow_parser_mapping_form_bean" method="POST" action="<%= ControllerConstants.CREATE_UPDATE_MAPPING_DETAILS %>" id="create-mapping-form">
   		
	<input type="hidden" name="create_mapping_base_mapping_id"  id="create_mapping_base_mapping_id" value = " " />
	<input type="hidden" name="mappingActionType"  id="mappingActionType" value="CREATE"/>
	<input type="hidden" name="createMappingId"  id="createMappingId" value="0"/>
   	<input type="hidden" value="${plugInId}" name="pluginId" id="pluginId"/>
   
    <div id="customize_mapping_div" style="width: 100%; display: none;">
			<div class="modal-content">
				<div class="modal-header padding10">
					<h4 class="modal-title">
						<spring:message code="parser.mapping.customize.header.label" ></spring:message>
					</h4>
				</div>
				<div class="modal-body padding10 inline-form">
					<div class="fullwidth"> <jsp:include page="../../../common/responseMsgPopUp.jsp" ></jsp:include>	</div>
					<div class="form-group">
						<spring:message code="device.list.grid.device.type.label" var="tooltip" ></spring:message> 
						<div class="table-cell-label">${tooltip}</div>
						<div class="input-group">
							<input type="text" name="customize_selected_deviceType" id="customize_selected_deviceType" readonly="readonly" class="form-control table-cell input-sm" data-toggle="tooltip" data-placement="bottom" title="${tooltip}"> 
							<span class="input-group-addon add-on last"> <i	class="glyphicon glyphicon-alert" data-toggle="tooltip"	data-placement="left" title=""></i></span>
						</div>
					</div>
					<div class="form-group">
						<spring:message code="device.list.grid.device.vendor.type.label" var="tooltip" ></spring:message> 
						<div class="table-cell-label">${tooltip}</div>
						<div class="input-group">
							<input type="text" name="customize_selected_vendorType" id="customize_selected_vendorType" readonly="readonly" class="form-control table-cell input-sm" data-toggle="tooltip" data-placement="bottom" title="${tooltip}"> 
							<span class="input-group-addon add-on last"> <i	class="glyphicon glyphicon-alert" data-toggle="tooltip"	data-placement="left" title=""></i></span>
						</div>
					</div>
					<div class="form-group">
						<spring:message code="device.list.grid.device.name.label" var="tooltip" ></spring:message>
						<div class="table-cell-label">${tooltip}</div>
						<div class="input-group">
							<input type="text" name="customize_selected_deviceName" id="customize_selected_deviceName" readonly="readonly" class="form-control table-cell input-sm" data-toggle="tooltip" data-placement="bottom" title="${tooltip}"> 
							<span class="input-group-addon add-on last"> <i	class="glyphicon glyphicon-alert" data-toggle="tooltip"	data-placement="left" title=""></i></span>
						</div>
					</div>
					<div class="form-group">
						<spring:message code="device.list.grid.device.base.mapping.name.label" var="tooltip" ></spring:message> 
						<div class="table-cell-label">${tooltip}</div>
						<div class="input-group">
							<input type="text" name="customize_selected_mappingName" id="customize_selected_mappingName" readonly="readonly" class="form-control table-cell input-sm" data-toggle="tooltip" data-placement="bottom" title="${tooltip}"> 
							<span class="input-group-addon add-on last"> <i	class="glyphicon glyphicon-alert" data-toggle="tooltip"	data-placement="left" title=""></i></span>
						</div>
					</div>
					<div class="form-group" id="customize_mode_div" >
						<spring:message code="parser.mapping.customize.mode.mapping.label" var="tooltip" ></spring:message> 
						<div class="table-cell-label">${tooltip}</div>
						<div class="input-group">
							 <input type="radio" name="customizeMode" id="create_new_mapping" style="padding-right: 15px;" value="create" onclick="validateMapingOptions('create');"/>&nbsp;&nbsp;<spring:message code="parser.mapping.customize.mode.create.new.label" ></spring:message>
	             			&nbsp;&nbsp;&nbsp;<input type="radio" name="customizeMode"  id="update_mapping" style="padding-right: 15px;" value="update" onclick="validateMapingOptions('edit');" />&nbsp;&nbsp;<spring:message code="parser.mapping.customize.mode.update.label" ></spring:message>
						</div>
					</div>
					<div class="form-group" id="base_mapping_div" style="display: none;" >
						<spring:message code="parser.mapping.customize.base.mapping.label" var="tooltip" ></spring:message>
						<div class="table-cell-label">${tooltip}</div>
						<div class="input-group">
							<select  name = "base_mapping_name" class="form-control table-cell input-sm"  tabindex="3" id="base_mapping_name" data-toggle="tooltip" data-placement="bottom"  title="${tooltip }"  >
			             		<%-- <option  value="0" selected="selected"><spring:message code="mapping.name.dropdown.default.selection.label" ></spring:message></option> --%>
			             	</select> 
							<span class="input-group-addon add-on last"> <i	class="glyphicon glyphicon-alert" data-toggle="tooltip"	data-placement="left" title=""></i></span>
						</div>
					</div>
					<div class="form-group" id="customize_new_mapping_div" >
						<spring:message code="parser.mapping.customize.new.mapping.label" var="label" ></spring:message> 
						<spring:message code="device.list.grid.device.mapping.name.label.tooltip" var="tooltip" ></spring:message>
						<div class="table-cell-label">${label}</div>
						<div class="input-group">
							<input type="text" name="name" id="name" class="form-control table-cell input-sm" data-toggle="tooltip" data-placement="top" title="${tooltip}"> 
							<span class="input-group-addon add-on last"> <i	class="glyphicon glyphicon-alert" data-toggle="tooltip"	data-placement="left" title=""></i></span>
						</div>
					</div>
					<div class="form-group" id="selected_mapping_association_details_div">
					</div>
				</div>
				<div class="modal-footer padding10" id="create_mapping_button_div">
					<button id="createupdateMappingBtn" type="button" class="btn btn-grey btn-xs "	data-dismiss="modal" onclick="createOrUpdateMappingDetails();"><spring:message code="btn.label.submit" ></spring:message></button>
					<button type="button" class="btn btn-grey btn-xs "	data-dismiss="modal" onclick="closeFancyBox();setReadOnlyFlagOnCancelBtn();"><spring:message code="btn.label.cancel" ></spring:message></button>
				</div>
				<div class="modal-footer padding10" id="close-create-mapping-button-div" style="display:none;">
		            <button type="button" class="btn btn-grey btn-xs " onclick="closeFancyBox();"><spring:message code="btn.label.close"></spring:message></button>
		        </div>
		        <div id="create_mapping_proccess_bar_div" class="modal-footer padding10 text-left" style="display: none;">
					<jsp:include page="../../../common/processing-bar.jsp"></jsp:include>
				</div>
				
				
			</div>
			<!-- /.modal-content -->
		</div>
    </form:form>
    
    <!-- Customize mapping pop-up code end here  -->
    
    <!-- Footer Start -->
    

    <form action="" method="" id="natflow_form" name="natflow_form">
 		<input type="hidden" id="serviceId" name="serviceId" value="${serviceId}">
		<input type="hidden" id="serviceType" name="serviceType" value="${serviceType}" />	
		<input type="hidden" id="serviceName" name="serviceName" value="${serviceName}">
		<input type="hidden" id="instanceId" name="instanceId" value="${instanceId}">
		<input type="hidden" id="<%=BaseConstants.REQUEST_ACTION_TYPE %>" name="<%=BaseConstants.REQUEST_ACTION_TYPE %>" value="">
		
		<input type="hidden" value="${plugInId}" name="plugInId" id="plugInId" />
		<input type="hidden" value="${plugInName}" name="plugInName" id="plugInName" />
		<input type="hidden" value="${plugInType}" name="plugInType" id="plugInType" />		
    </form>
    

	<footer class="main-footer positfix">
    	<div class="fooinn">
        	<div class="fullwidth">
       			<div class="padleft-right" id="netflowParserConfigDiv" style="display: none;">
        		<sec:authorize access="hasAuthority('UPDATE_PARSER')"> 
					<button class="btn btn-grey btn-xs " id="parser_association_save_next_btn"  type="button" onclick="setReadOnlyFlag()"><spring:message code="btn.label.save.next"></spring:message></button>
                   	<button class="btn btn-grey btn-xs"  id="parser_association_customize_btn"   type="button" onclick="customizeMappingDetails();"><spring:message code="btn.label.customize"></spring:message></button>
                   	<button class="btn btn-grey btn-xs"  id="parser_association_cancel_btn" type="button" onclick="goToService();"><spring:message code="btn.label.cancel"></spring:message></button>
                </sec:authorize> 
				</div>
					
            	<div class="padleft-right" id="netflow_attribute_default" style="display: none;">
	             	<%-- <sec:authorize access="hasAuthority('UPDATE_PARSER')"> 
	           			<button class="btn btn-grey btn-xs " type="button" tabindex="16" onclick="updateDefaultDeviceConfiguration();"><spring:message code="btn.label.update"></spring:message></button>
	                   	<button class="btn btn-grey btn-xs" type="button" tabindex="17" onclick="displayCustomConfigButtons();">Customize Default</button>
	                   	<button class="btn btn-grey btn-xs" type="button" tabindex="18" onclick="cancelParserConfiguration();"><spring:message code="btn.label.cancel"></spring:message></button>
	                </sec:authorize>  --%>
				</div>
        	</div>
     		<jsp:include page="../../../common/newfooter.jsp"></jsp:include>
     	</div>
	</footer>
    <!-- Footer End --> 
    
</div>
<!-- ./wrapper --> 

<script>
	function showHideButtonBasedOnTabsSelected(tabType){
		$("#netflowParserConfigDiv").hide();
		$("#netflow_attribute_default").hide();
		
		if(tabType == 'NETFLOW_PARSER_CONFIGURATION'){
			$("#netflowParserConfigDiv").show();
		}else if(tabType == 'NETFLOW_PARSER_ATTR_LIST'){
			$("#netflow_attribute_default").show();
		}
	}
	
	$("#service-optionTemplateEnable").change(function () {
        enableDisableOptionTemplateConfig(this.value);
    });
	
	$("#service-filterEnable").change(function () {
        enableDisableFilterConfig(this.value);
    });
	
	
	function showButtons(id){
		if(id == 'parser_configuration'){
			showHideButtonBasedOnTabsSelected('NETFLOW_PARSER_CONFIGURATION');
		}else if(id == 'parser_attribute_configuration'){
			showHideButtonBasedOnTabsSelected('NETFLOW_PARSER_ATTR_LIST');
		}
	}
	
	$(document).on("change blur","#vendorType",function(event) {
		   var deviceTypeId = $('#deviceType').val();
		   var vendorId = $('#vendorType').val();
		getDeviceByVendorName('<%=ControllerConstants.GET_DEVICE_LIST_BY_VENDOR%>', deviceTypeId, vendorId,'deviceName');
	});	

	$(document).on("change blur","#deviceName",function(event) {
		   var deviceId = $('#deviceName').val();
		   var parserType = '${plugInType}';
		   
		   if(deviceId != -1){
			   getMappingByDeviceAndParserType('<%=ControllerConstants.GET_MAPPING_LIST_BY_DEVICE%>',deviceId,parserType,'mappingName','device_block');
			   onMappingNameChange();
		   }
		   
	});	
	
	$(document).on("change","#mappingName",function(event) {
		onMappingNameChange();
	});
	
	function onMappingNameChange() {
		var mappingId = $('#mappingName').val();
		var parserType = '${plugInType}';
		getMappingDetails('<%=ControllerConstants.GET_MAPPING_DETAILS_ID%>',mappingId,parserType);
	}
	
	$(document).on("change","#base_mapping_name",function(event) {
		 $("#create_mapping_base_mapping_id").val($("#base_mapping_name").val());
		   
	});
	
</script>
<script src="${pageContext.request.contextPath}/customJS/deviceManager.js<%= "?v=" + Math.random() %>" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/customJS/parserManager.js<%= "?v=" + Math.random() %>" type="text/javascript"></script>

<script>
$(document).ready(function() {
	var currentTab = '${REQUEST_ACTION_TYPE}';
	var activeTab = $(".nav-tabs li.active a");
	var id = activeTab.attr("id");
	showButtons(id);
	
	//$("#service-optionTemplateEnable").val('false');
	$("#readTemplateInitially").val('True');
	$("#deviceType").val('-1');
	
	if(currentTab == 'NETFLOW_PARSER_CONFIGURATION' ){
		var errorMSG = $("#isError").val();
		if(errorMSG){
			enableDisableOptionTemplateConfig('true');
			enableDisableFilterConfig('true');
			enableMappingDetails();
		}	
		else{
			enableDisableOptionTemplateConfig($("#service-optionTemplateEnable").val());
			enableDisableFilterConfig($("#service-filterEnable").val());
			disableMappingDetails();
		}	
	}
	

	var parserDeviceTypeId = '${selDeviceTypeId}';
	var parserVendorTypeId = '${selVendorTypeId}';
	var parserDeviceId = '${selDeviceId}';
	var parserMappingId = '${selMappingId}';	
	
	
	setDropdownValue('deviceType',parserDeviceTypeId);
	
	
	
	
	if(parserDeviceTypeId > 0 && currentTab == 'NETFLOW_PARSER_CONFIGURATION'){
		
		getVendorByDeviceType('getVendorListByDeviceType',$("#deviceType").val() ,'vendorType',' ','null');
		
		setDropdownValue('vendorType',parserVendorTypeId);
		
		 var deviceTypeId = $('#deviceType').val();
		 var vendorId = $('#vendorType').val();
		 
		 getDeviceByVendorName('<%=ControllerConstants.GET_DEVICE_LIST_BY_VENDOR%>', deviceTypeId, vendorId,'deviceName');
		
		setDropdownValue('deviceName',parserDeviceId);
		
		var deviceId = $('#deviceName').val();
		var parserType = '${plugInType}';
		getMappingByDeviceAndParserType('<%=ControllerConstants.GET_MAPPING_LIST_BY_DEVICE%>',deviceId,parserType,'mappingName','device_block');
		setDropdownValue('mappingName',parserMappingId);
		
		var mappingId = $('#mappingName').val();
		if(errorMSG){
			//Don't Update Mapping Details as We have to retain Previous Values.
		}else{
			getMappingDetails('<%=ControllerConstants.GET_MAPPING_DETAILS_ID%>',mappingId,parserType);
			$("#natflow_attribute_form #selAttributeMappingName").val($("#mappingName").find(":selected").text());
			$("#natflow_attribute_form #selAttributeMappingId").val($("#mappingName").find(":selected").val());
			$("#natflow_attribute_form #selDeviceName").val($("#deviceName").find(":selected").text());
		}
	}
	
	/*
	* Styling natflow multi-select form field
	* Keeping dropUp:true because there is no space in down. 
	* remove it when space is available in down.
	*/
	$('#service-filterTransport').multiselect({
		enableFiltering: true,
		enableHTML : true,
		maxHeight: 200,
		dropDown: true,
		buttonWidth: '100%',
		dropUp:true,
	});
	
	
	
	let selectedText = $('.multiselect-selected-text');
	selectedText.css('background', 'none');	
	selectedText.css('text-indent', 'none');
	selectedText.css('float', 'none');
	selectedText.css('overflow', 'none');
	$('.multiselect-container .checkbox').css('color', 'darkblue');

});

function setReadOnlyFlagOnCancelBtn() {
	$("#readOnlyFlag").val('true');
	$("#instanceId").val('${instanceId}');
	$("#serviceType").val('${serviceType}');
}

function setReadOnlyFlag(){
	if('${isValidationFail}' == 'true'){
		$("#actionType").val('UPDATE');
	}
	var actionType=$("#actionType").val();

	if(actionType == 'NO_ACTION'){
		$("#readOnlyFlag").val('true');
		$("#instanceId").val('${instanceId}');
		$("#serviceType").val('${serviceType}');
	}
	associateMappingWithParser();
}

	<sec:authorize access="hasAuthority('VIEW_PARSER')">
		isViewParserAccess = true;
	</sec:authorize>
	<sec:authorize access="hasAuthority('ADD_PARSER')">
		isAddParserAccess = true;
	</sec:authorize>
	<sec:authorize access="hasAuthority('UPDATE_PARSER')">
		isEditParserAccess = true;
	</sec:authorize>
	<sec:authorize access="hasAuthority('DELETE_PARSER')">
		isDeleteParserAccess = true;
	</sec:authorize>

</script>
</body>
</html>
