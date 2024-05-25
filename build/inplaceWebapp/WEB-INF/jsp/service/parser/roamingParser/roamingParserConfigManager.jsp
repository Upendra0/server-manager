<%@page import="com.elitecore.sm.parser.model.ASN1ATTRTYPE"%>
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
	jsSpringMsg.dateFormat="<spring:message code='parser.grid.date.format.label' ></spring:message>";
	jsSpringMsg.updateAction="<spring:message code='staffManager.audit.search.action.lable.name'></spring:message>";
	jsSpringMsg.childAttrib="<spring:message code='parser.grid.child.attrib.format.label'></spring:message>";
	jsSpringMsg.asnDataType="<spring:message code='parser.grid.asn1datatype.format.label.tooltip'></spring:message>";
	jsSpringMsg.srcDataFormat="<spring:message code='parser.grid.srcDataFormat.format.label'></spring:message>";
	jsSpringMsg.recordInitializer="<spring:message code='parser.grid.recordInitilializer.format.label.tooltip'></spring:message>";
	jsSpringMsg.unifiedChoiceHolder="<spring:message code='parser.grid.unifiedFieldHoldsChoiceId.format.label.tooltip'></spring:message>";
	jsSpringMsg.additionalinfo="<spring:message code='label.additional.information'></spring:message>";
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
	jsSpringMsg.parseAsJson="<spring:message code='parsing.service.attr.grid.column.attr.parseAsJson'></spring:message>";                         
	jsSpringMsg.attrNotFound="<spring:message code='device.mapping.attribute.not.found'></spring:message>";
	jsSpringMsg.RapParser='<%=EngineConstants.RAP_PARSING_PLUGIN%>';
	jsSpringMsg.TapParser='<%=EngineConstants.TAP_PARSING_PLUGIN%>';
	jsSpringMsg.NRTRDEParser='<%=EngineConstants.NRTRDE_PARSING_PLUGIN%>';
	
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
                                    <div class="box  martop" style="border:none; box-shadow: none;"> 
                                        <!-- /.box-header -->
                                        <div class="box-body table-responsive no-padding">
                                            <div class="nav-tabs-custom"> 
                                                <!-- Tabs within a box -->
                                                <ul class="nav nav-tabs pull-right">
                                               	 <sec:authorize access="hasAnyAuthority('VIEW_PARSER')">
                                                    <li class="<c:if test="${(REQUEST_ACTION_TYPE eq 'ROAMING_PARSER_CONFIGURATION')}"><c:out value="active"></c:out></c:if>" onclick="showButtons('parser_configuration'); getRoamingParserConfig('<%=ControllerConstants.INIT_RAP_PARSER_CONFIG%>');" >
                                                    	<a href="#asn1Configuration" data-toggle="tab" id="parser_configuration"><spring:message code="parsing.service.config.tab.header"></spring:message></a>
                                                    </li>
                                                    <li class="<c:if test="${(REQUEST_ACTION_TYPE eq 'ROAMING_PARSER_ATTRIBUTE')}"><c:out value="active" ></c:out></c:if>" onclick="showButtons('parser_attribute_configuration'); getRoamingParserAttrDetails('<%=ControllerConstants.INIT_RAP_PARSER_ATTRIBUTE%>'); " >
                                                    	<a href="#asn1Attribute" data-toggle="tab" id="parser_attribute_configuration"><spring:message code="parsing.service.attr.list.tab.header"></spring:message></a>
                                                    </li>
                                                  </sec:authorize>  
                                                </ul>
                                                <div class="fullwidth tab-content no-padding">
                                                <sec:authorize access="hasAnyAuthority('VIEW_PARSER')">
                                                	<div class="tab-pane <c:if test="${(REQUEST_ACTION_TYPE eq 'ROAMING_PARSER_CONFIGURATION')}"><c:out value="active"></c:out></c:if>" id="asn1Configuration">
                                                		<c:if test="${(REQUEST_ACTION_TYPE eq 'ROAMING_PARSER_CONFIGURATION')}">
                                                		<c:if test="${(plugInType eq 'RAP_PARSING_PLUGIN')}">
                                                			<jsp:include page="rapParser/rapParserConfiguration.jsp" ></jsp:include>
                                                		</c:if>
                                                		<c:if test="${(plugInType eq 'TAP_PARSING_PLUGIN')}">
                                                			<jsp:include page="tapParser/tapParserConfiguration.jsp" ></jsp:include>
                                                		</c:if>
																	<c:if test="${(plugInType eq 'NRTRDE_PARSING_PLUGIN')}">
																		<jsp:include
																			page="nrtrdeParser/nrtrdeParserConfiguration.jsp" ></jsp:include>
																	</c:if>
																</c:if>
                                                	</div>
                                                	<div class="tab-pane <c:if test="${(REQUEST_ACTION_TYPE eq 'ROAMING_PARSER_ATTRIBUTE')}"><c:out value="active"></c:out></c:if>" id="asn1Attribute">
                                               			<c:if test="${(REQUEST_ACTION_TYPE eq 'ROAMING_PARSER_ATTRIBUTE')}">
                                                		<c:if test="${(plugInType eq 'RAP_PARSING_PLUGIN')}">
                                                			<jsp:include page="rapParser/rapParserAttribute.jsp" ></jsp:include>
                                                		</c:if>
                                                		<c:if test="${(plugInType eq 'TAP_PARSING_PLUGIN')}">
                                                			<jsp:include page="tapParser/tapParserAttribute.jsp" ></jsp:include>
                                                		</c:if>
                                                		<c:if test="${(plugInType eq 'NRTRDE_PARSING_PLUGIN')}">
                                                			<jsp:include page="nrtrdeParser/nrtrdeParserAttribute.jsp" ></jsp:include>
                                                		</c:if>
                                                		 </c:if> 
                                                	</div>
                                                	</sec:authorize>
                                                </div>
                                            </div>
                                        </div>
                                        
                                        <!-- /.box-body --> 
                                    </div>
                                    <!-- /.box --> 
                                    
                                </div>
                            </div>
                        </div>
                        <!-- Content Wrapper. Contains page content End --> 
                    </div>
                </div>
            </div>
        </section>
        	<div style="display: block;">
        	
				<form id="ascii-parser-form" action = "" method="">
					<input type="hidden" id="serviceId" name="serviceId" value="${serviceId}"/>
					<input type="hidden" id="serviceName" name="serviceName" value="${serviceName}"/>
					<input type="hidden" id="serviceType" name="serviceType" value='${serviceType}'>
					<input type="hidden" id="serviceInstanceId" name="serviceInstanceId" value="${serviceInstanceId}">
					<input type="hidden" id="instanceId" name="instanceId" value="${instanceId}">
					<input type="hidden" id="plugInId" name="plugInId" value="${plugInId}"/>
					<input type="hidden" id="serviceInstanceId" name="serviceInstanceId" value="${serviceInstanceId}">
					<input type="hidden" id="plugInName" name="plugInName" value="${plugInName}"/> 
					<input type="hidden" id="plugInType" name="plugInType" value="${plugInType}"/>
					<input type="hidden" id="mappingId" name="mappingId" value=""/>
					<input type="hidden" id="deviceName" name="deviceName" value=""/>
					<input type="hidden" id="deviceId" name="deviceId" value=""/>
					<input type="hidden" id="mappingName" name="mappingName" value=""/>
					<input type="hidden" id="attributeType" name="attributeType" value=""/>
				</form>
				
				<form id="go-to-service-form" action = "<%=ControllerConstants.INIT_UPDATE_SERVICE %>" method="GET">
					<input type="hidden" id="serviceId" name="serviceId" value="${serviceId}">
					<input type="hidden" id="serviceType" name="serviceType" value="${serviceType}" />	
					<input type="hidden" id="serviceName" name="serviceName" value="${serviceName}">
					<input type="hidden" id="serviceInstanceId" name="serviceInstanceId" value="${serviceInstanceId}">
					<input type="hidden" id="instanceId" name="instanceId" value="${instanceId}">
				</form>
			   
			</div>
    </div>
    <!-- /.content-wrapper --> 
    <!-- Footer Start -->
	<footer class="main-footer positfix">
    	<div class="fooinn">
        	<div class="fullwidth">
       			<div class="padleft-right" id="asn1ParserConfigDiv" >
        		<sec:authorize access="hasAuthority('UPDATE_PARSER')"> 
					<button class="btn btn-grey btn-xs " id="parser_association_save_next_btn"  type="button" onclick="setReadOnlyFlag();"><spring:message code="btn.label.save.next"></spring:message></button>
                   	<button class="btn btn-grey btn-xs"  id="parser_association_customize_btn"   type="button" onclick="customizeMappingDetails();"><spring:message code="btn.label.customize"></spring:message></button>
                </sec:authorize> 
                <button class="btn btn-grey btn-xs"  id="parser_association_cancel_btn" type="button" onclick="goToService();"><spring:message code="btn.label.cancel"></spring:message></button>
				
				</div>
				<div class="padleft-right"  id="asn1ParserConfigbtnDiv" style="display: none;">
					<c:if test="${(readOnlyFlag eq 'false') }">
						<button class="btn btn-grey btn-xs " id="move_to_next_attribute"  type="button" onclick="setReadOnlyFlagWithUpdateActionType();"><spring:message code="btn.label.save.next"></spring:message></button>
					</c:if>
		       		<sec:authorize access="hasAuthority('UPDATE_PARSER')"> 
						<button class="btn btn-grey btn-xs"  id="update_order_attribute" type="button" style="display: none;"  onclick="reorderParserAttribute('<%=ControllerConstants.REORDER_ATTRIBUTE_GRID_LIST_BY_MAPPING_ID%>');" ><spring:message code="btn.label.updateorder"></spring:message></button>
					</sec:authorize>
				</div>
            	<div class="padleft-right" id="asn1_attribute_default" style="display: none;">
				</div>
				<div class="padleft-right" id="asn1_header_attribute_default" style="display: none;">
				</div>
				<div class="padleft-right" id="asn1_trailer_attribute_default" style="display: none;">
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
		
		$("#asn1ParserConfigDiv").hide();
		$("#asn1_attribute_default").hide();
		$("#asn1_header_attribute_default").hide();
		$("#asn1_trailer_attribute_default").hide();
		$("#asn1ParserConfigbtnDiv").hide();		
		if(tabType === 'ROAMING_PARSER_CONFIGURATION'){
			$("#asn1ParserConfigDiv").show();
		}
	}
	
	function showButtons(id){
		if(id == 'parser_configuration'){
			showHideButtonBasedOnTabsSelected('ROAMING_PARSER_CONFIGURATION');
		}else if(id == 'parser_attribute_configuration'){
			showHideButtonBasedOnTabsSelected('ASN1_PARSER_ATTR_LIST');
		}
	}
	
	function disableSaveBtn(){
		$("#move_to_next_attribute").hide();
	}
	
		
</script>
<script src="${pageContext.request.contextPath}/customJS/deviceManager.js<%= "?v=" + Math.random() %>" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/customJS/parserManager.js<%= "?v=" + Math.random() %>" type="text/javascript"></script>

<script>
$(document).ready(function() {
	var activeTab = $(".nav-tabs li.active a");
	var id = activeTab.attr("id");
	showButtons(id);
	$("#deviceType").val('-1');
	
	var readOnlyFlag='${readOnlyFlag}';
	
	if(readOnlyFlag == '' || readOnlyFlag ==true){
		disableAsn1AdvanceDetails();	
	}

	var parserDeviceTypeId = '${selDeviceTypeId}';
	var parserVendorTypeId = '${selVendorTypeId}';
	var parserDeviceId = '${selDeviceId}';
	var parserMappingId = '${selMappingId}';	
	
	setDropdownValue('deviceType',parserDeviceTypeId);
	
	var currentTab = '${REQUEST_ACTION_TYPE}';
	if(parserDeviceTypeId > 0 && currentTab == 'ROAMING_PARSER_CONFIGURATION'){
	
		
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
		if('${isValidationFail}' == '' || '${isValidationFail}' == false){
			getMappingDetails('<%=ControllerConstants.GET_MAPPING_DETAILS_ID%>',mappingId,parserType);	
		}
		
	}
	
});

function getRoamingParserConfig(action){

	 if(currentParserType === jsSpringMsg.RapParser){
		action = '<%=ControllerConstants.INIT_RAP_PARSER_CONFIG%>';
	}else if(currentParserType === jsSpringMsg.TapParser){
		action = '<%=ControllerConstants.INIT_TAP_PARSER_CONFIG%>';
	}else if(currentParserType === jsSpringMsg.NrtrdeParser){
		action = '<%=ControllerConstants.INIT_NRTRDE_PARSER_CONFIG%>';
	} 
	$("#ascii-parser-form").attr("action",action);
	$("#ascii-parser-form").attr("method","GET");
	
	$("#ascii-parser-form #deviceName").val($("#deviceName").find(":selected").text());
	$("#ascii-parser-form #mappingName").val($("#mappingName").find(":selected").text());
	$("#ascii-parser-form #mappingId").val($("#mappingName").find(":selected").val());
	$("#ascii-parser-form #deviceId").val($("#deviceName").find(":selected").val());
	
	$("#ascii-parser-form").submit();
}

function getRoamingParserAttrDetails(action,attributeType){
	
	 if(currentParserType === jsSpringMsg.RapParser){
			action = '<%=ControllerConstants.INIT_RAP_PARSER_ATTRIBUTE%>';
		}else if(currentParserType === jsSpringMsg.TapParser){
			action = '<%=ControllerConstants.INIT_TAP_PARSER_ATTRIBUTE%>';
		}else if(currentParserType === jsSpringMsg.NrtrdeParser){
			action = '<%=ControllerConstants.INIT_NRTRDE_PARSER_ATTRIBUTE%>';
		} 
	$("#ascii-parser-form").attr("action",action);
	$("#ascii-parser-form").attr("method","POST");
	$("#ascii-parser-form #attributeType").val(attributeType);
	$("#ascii-parser-form #deviceName").val($("#deviceName").find(":selected").text());
	if($("#ascii-parser-form #deviceName").val() == ''){
		$("#ascii-parser-form #deviceName").val('${deviceName}')
	}
	
	$("#ascii-parser-form #mappingName").val($("#mappingName").find(":selected").text());
	if($("#ascii-parser-form #mappingName").val() == ''){
		$("#ascii-parser-form #mappingName").val('${mappingName}');
	}
	
	$("#ascii-parser-form #mappingId").val($("#mappingName").find(":selected").val());
	if($("#ascii-parser-form #mappingId").val() == ''){
		$("#ascii-parser-form #mappingId").val("${mappingId}");
	}
	$("#ascii-parser-form #deviceId").val($("#deviceName").find(":selected").val());
	if($("#ascii-parser-form #deviceId").val() == ''){
		$("#ascii-parser-form #deviceId").val('${deviceId}');
	}
	$("#ascii-parser-form").submit();
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

function setReadOnlyFlagOnCancelBtn() {
	$("#readOnlyFlag").val('true');
	$("#instanceId").val('${instanceId}');
	$("#serviceType").val('${serviceType}');
}

function setReadOnlyFlagWithUpdateActionType(){
	var url='';
	 if(currentParserType === jsSpringMsg.RapParser){
		 url = '<%=ControllerConstants.UPDATE_RAP_PARSER_CONFIGURATION%>';
		}else if(currentParserType === jsSpringMsg.TapParser){
			url = '<%=ControllerConstants.UPDATE_TAP_PARSER_CONFIGURATION%>';
		}else if(currentParserType === jsSpringMsg.NrtrdeParser){
			url = '<%=ControllerConstants.UPDATE_NRTRDE_PARSER_CONFIGURATION%>';
		} 
	$("#actionType").val('UPDATE');
	if('${isValidationFail}' == 'true'){
		$("#actionType").val('UPDATE');
	}
	var actionType=$("#actionType").val();

	if(actionType == 'NO_ACTION'){
		$("#readOnlyFlag").val('true');
		$("#instanceId").val('${instanceId}');
		$("#serviceType").val('${serviceType}');
	}
	$("#roaming-parser-mapping-form").attr("action",url);
	$("#roaming-parser-mapping-form").submit();
}

/* Function will disable the content */
function disableAsn1AdvanceDetails(){
	
	$("#advance_details_div_mask").remove(); 
	
	$('#advance_details_div').fadeTo('slow',.6);
	$element = $('#advance_details_div');
	var top = Math.ceil($element.position().top);
	var elementStr = "<div id='advance_details_div_mask' style='position: absolute;top:"+top+"px;left:0;width: 100%;height:100%;z-index:2;opacity:0.4;filter: alpha(opacity = 50)'></div>";
	$element.append(elementStr);
	disableSaveBtn();
}

/*Function will enable the mapping details*/
function enableAsn1AdvanceDetails(){
	console.log("Going to enable content");
	$("#advance_details_div").css("opacity","");
	
	$("#advance_details_div_mask").remove(); 
	console.log("mask div removed");
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
