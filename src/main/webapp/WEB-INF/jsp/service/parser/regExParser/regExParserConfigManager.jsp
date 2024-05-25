<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page import="com.elitecore.sm.common.constants.EngineConstants"%>

<!DOCTYPE html>

<html>
<script type="text/javascript">

var currentParserType = '${plugInType}';
var jsSpringMsg = {};
	jsSpringMsg.selVendorType="<spring:message code='vendor.type.dropdown.default.selection.label'></spring:message>";
	jsSpringMsg.selDevice="<spring:message code='device.name.dropdown.default.selection.label'></spring:message>";
	jsSpringMsg.selmappingName="<spring:message code='mapping.name.dropdown.default.selection.label'></spring:message>";
	
	jsSpringMsg.mappingCustomLabel = "<spring:message code='mapping.dropdown.custom.option.label'></spring:message>";
	jsSpringMsg.mappingNoneLabel = "<spring:message code='mapping.dropdown.none.option.label'></spring:message>";
	
	jsSpringMsg.invalidDeviceType = "<spring:message code='parserConfiguration.invalid.device.type.message'></spring:message>";
	jsSpringMsg.invalidVendorType = "<spring:message code='parserConfiguration.invalid.vendor.type.message'></spring:message>";
	jsSpringMsg.invalidDevice = "<spring:message code='parserConfiguration.invalid.device.message'></spring:message>";
	jsSpringMsg.invalidMapping = "<spring:message code='parserConfiguration.invalid.mapping.message'></spring:message>";
	
	jsSpringMsg.importantNote = "<spring:message code='label.important.note'></spring:message>";
	jsSpringMsg.associationContent = "<spring:message code='parser.configuration.update.mapping.association.content'></spring:message>";
	
	jsSpringMsg.regExParser='<%=EngineConstants.REGEX_PARSING_PLUGIN%>';
	
	jsSpringMsg.srno="<spring:message code='device.attribute.list.details.sr.no.label'></spring:message>";
	jsSpringMsg.serverInsName="<spring:message code='device.association.details.server.instance.name.label'></spring:message>";
	jsSpringMsg.serverIPPORT="<spring:message code='device.association.details.server.ip.port.label'></spring:message>";
	jsSpringMsg.serviceInsName="<spring:message code='device.association.details.service.instance.name.label'></spring:message>";
	jsSpringMsg.pluginName="<spring:message code='device.association.details.plugin.name.label'></spring:message>";
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
                            		<strong><a href="<%= ControllerConstants.INIT_SERVICE_MANAGER%>"><spring:message code="collectionService.leftmenu.home" ></spring:message></a>&nbsp;/ 
										<a href="#" onclick="viewService();">
										 ${serviceName}&nbsp;-&nbsp;${serviceInstanceId}</a>
                            	</span>
									</strong>
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
                                                    <li class="<c:if test="${(REQUEST_ACTION_TYPE eq 'REGEX_PARSER_CONFIGURATION')}"><c:out value="active"></c:out></c:if>" onclick="showButtons('parser_configuration'); getRegExParserConfig('<%=ControllerConstants.INIT_REGEX_PARSER_CONFIG%>');" >
                                                    	<a href="#regExConfiguration" data-toggle="tab" id="regex-parser-configuration"><spring:message code="parsing.service.config.tab.header"></spring:message></a>
                                                    </li>
                                                    <li class="<c:if test="${(REQUEST_ACTION_TYPE eq 'REGEX_PARSER_ATTRIBUTE')}"><c:out value="active" ></c:out></c:if>" onclick="showButtons('parser_attribute_configuration'); getRegExParserDetails('<%=ControllerConstants.INIT_REGEX_PARSER_ATTRIBUTE%>');" >
                                                    	<a href="#regExAttribute" data-toggle="tab" id="regex-parser-attr-list"><spring:message code="parsing.service.attr.list.tab.header"></spring:message></a>
                                                    </li>
                                                  </sec:authorize>  
                                                </ul>
                                                <div class="fullwidth tab-content no-padding">
                                                <sec:authorize access="hasAnyAuthority('VIEW_PARSER')">
                                                	<div class="tab-pane <c:if test="${(REQUEST_ACTION_TYPE eq 'REGEX_PARSER_CONFIGURATION')}"><c:out value="active"></c:out></c:if>" id="regExConfiguration">
                                                		 <c:if test="${(REQUEST_ACTION_TYPE eq 'REGEX_PARSER_CONFIGURATION')}">
                                                			<jsp:include page="regExParserConfiguration.jsp" ></jsp:include>
                                                		</c:if>
                                                	</div>
                                                	
                                                	<div class="tab-pane <c:if test="${(REQUEST_ACTION_TYPE eq 'REGEX_PARSER_ATTRIBUTE')}"><c:out value="active"></c:out></c:if>" id="regExAttribute">
                                               			<c:if test="${(REQUEST_ACTION_TYPE eq 'REGEX_PARSER_ATTRIBUTE')}">
                                               				<jsp:include page="regExParserAttribute.jsp" ></jsp:include>
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
        
        	<div style="display: hidden;">
				<form id="regex-parser-form" action = "" method="">
					<input type="hidden" id="serviceId" name="serviceId" value="${serviceId}"/>
					<input type="hidden" id="serviceName" name="serviceName" value="${serviceName}"/>
					<input type="hidden" id="instanceId" name="instanceId" value="${instanceId}">
					<input type="hidden" id="serviceType" name="serviceType" value="${serviceType}">
					<input type="hidden" id="serviceInstanceId" name="serviceInstanceId" value="${serviceInstanceId}">
					<input type="hidden" id="plugInId" name="plugInId" value="${plugInId}"/>
					<input type="hidden" id="plugInName" name="plugInName" value="${plugInName}"/> 
					<input type="hidden" id="plugInType" name="plugInType" value="${plugInType}"/>
					<input type="hidden" id="mappingId" name="mappingId" value=""/>
					<input type="hidden" id="deviceName" name="deviceName" value=""/>
					<input type="hidden" id="deviceId" name="deviceId" value=""/>
					<input type="hidden" id="mappingName" name="mappingName" value=""/>
				</form>
			</div>
			
			<div style="display: none;">
			<form id="service_form" method="GET" action="<%= ControllerConstants.INIT_UPDATE_SERVICE %>">
				<input type="hidden" id="serviceId" name="serviceId" value="${serviceId}">
				<input type="hidden" id="serviceType" name="serviceType" value="${serviceType}">
				<input type="hidden" id="serviceName" name="serviceName" value="${serviceName}">
				<input type="hidden" id="instanceId" name="instanceId" value="${instanceId}">
				<input type="hidden" id="serviceInstanceId" name="serviceInstanceId" value="${serviceInstanceId}">
			</form>
			
		</div>
    </div>
    <!-- /.content-wrapper --> 
    
    <!-- Footer Start -->
	<footer class="main-footer positfix">
    	<div class="fooinn">
        	<div class="fullwidth">
       			<div class="padleft-right" id="regExParserConfigDiv" style="display: none;">
        		<sec:authorize access="hasAuthority('UPDATE_PARSER')"> 
					<button class="btn btn-grey btn-xs " id="parser_association_save_next_btn"  type="button" onclick="setReadOnlyFlag();"><spring:message code="btn.label.save.next"></spring:message></button>
                   	<button class="btn btn-grey btn-xs"  id="parser_association_customize_btn"   type="button" onclick="customizeMappingDetails();"><spring:message code="btn.label.customize"></spring:message></button>
                </sec:authorize> 
                <button class="btn btn-grey btn-xs"  id="parser_association_cancel_btn" type="button" onclick="viewService();"><spring:message code="btn.label.cancel"></spring:message></button>
				</div>
					
            	<div class="padleft-right" id="regEx_attribute_default" style="display: none;">
	             	
				</div>
        	</div>
     		<jsp:include page="../../../common/newfooter.jsp"></jsp:include>
     	</div>
	</footer>
    <!-- Footer End --> 
    
</div>
<!-- ./wrapper --> 

<script src="${pageContext.request.contextPath}/customJS/deviceManager.js<%= "?v=" + Math.random() %>" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/customJS/parserManager.js<%= "?v=" + Math.random() %>" type="text/javascript"></script>

<script>

function showHideButtonBasedOnTabsSelected(tabType){
	$("#regExParserConfigDiv").hide();
	$("#regEx_attribute_default").hide();
	
	if(tabType == 'REGEX_PARSER_CONFIGURATION'){
		$("#regExParserConfigDiv").show();
	}else if(tabType == 'REGEX_PARSER_ATTR_LIST'){
		$("#regEx_attribute_default").show();
	}
}

function showButtons(id){
	if(id == 'regex-parser-configuration'){
		showHideButtonBasedOnTabsSelected('REGEX_PARSER_CONFIGURATION');
	}else if(id == 'regex-parser-attr-list'){
		showHideButtonBasedOnTabsSelected('REGEX_PARSER_ATTR_LIST');
	}
}
function getRegExParserDetails(action){

	$("#regex-parser-form").attr("action",action);
	$("#regex-parser-form").attr("method","POST");
	
	$("#regex-parser-form #deviceName").val($("#deviceName").find(":selected").text());
	if($("#regex-parser-form #deviceName").val() == ''){
		$("#regex-parser-form #deviceName").val("${deviceName}");
	}
	
	$("#regex-parser-form #mappingName").val($("#mappingName").find(":selected").text());
	if($("#regex-parser-form #mappingName").val() == ''){
		$("#regex-parser-form #mappingName").val("${mappingName}");
	}
	
	$("#regex-parser-form #mappingId").val($("#mappingName").find(":selected").val());
	if($("#regex-parser-form #mappingId").val() == ''){
		$("#regex-parser-form #mappingId").val("${mappingId}");
	}
	
	$("#regex-parser-form #deviceId").val($("#deviceName").find(":selected").val());
	if($("#regex-parser-form #deviceId").val() == ''){
		$("#regex-parser-form #deviceId").val("${deviceId}");
	}
	$("#regex-parser-form").submit();
}

function getRegExParserConfig(action){

	$("#regex-parser-form").attr("action",action);
	$("#regex-parser-form").attr("method","GET");
	$("#regex-parser-form #deviceName").val($("#device-name").val());
	$("#regex-parser-form #mappingName").val($("#mapping-name").val());
	$("#regex-parser-form #mappingId").val("${selDeviceId}");
	$("#regex-parser-form #deviceId").val("${selMappingId}");
	$("#regex-parser-form").submit();
}
	
		
	
	$(document).ready(function() {
		var activeTab = $(".nav-tabs li.active a");
		var id = activeTab.attr("id");
		showButtons(id);
		
	//	disableDefaultContent();
		
		var parserDeviceTypeId = '${selDeviceTypeId}';
		var parserVendorTypeId = '${selVendorTypeId}';
		var parserDeviceId = '${selDeviceId}';
		var parserMappingId = '${selMappingId}';	
		
		
		setDropdownValue('deviceType',parserDeviceTypeId);
		
		var currentTab = '${REQUEST_ACTION_TYPE}';
		
		
		if(parserDeviceTypeId > 0 && currentTab == 'REGEX_PARSER_CONFIGURATION'){
			console.log("selected device val :: " + $("#deviceType").val());
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
			getMappingDetails('<%=ControllerConstants.GET_MAPPING_DETAILS_ID%>',parserMappingId,"REGEX_PARSING_PLUGIN");
		}
		
	});
	
function viewService(){
		
		$("#service_form").submit();
	}
function setReadOnlyFlag(){
		
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

</script>
</body>
</html>
