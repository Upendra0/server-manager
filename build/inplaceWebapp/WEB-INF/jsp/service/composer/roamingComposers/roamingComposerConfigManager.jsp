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
<head>
<script src="${pageContext.request.contextPath}/customJS/deviceManager.js<%= "?v=" + Math.random() %>" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/customJS/composerManager.js<%= "?v=" + Math.random() %>" type="text/javascript"></script> 
</head>
<script type="text/javascript">
var isAddParserAccess = false;
var isDeleteParserAccess = false;
var isEditParserAccess = false;	
var isViewParserAccess = false;

var ckIntanceSelected = new Array();
var currentComposerType = '${plugInType}';
var jsSpringMsg = {};
	
	jsSpringMsg.selVendorType="<spring:message code='vendor.type.dropdown.default.selection.label'></spring:message>";
	jsSpringMsg.selDevice="<spring:message code='device.name.dropdown.default.selection.label'></spring:message>";
	jsSpringMsg.selmappingName="<spring:message code='mapping.name.dropdown.default.selection.label'></spring:message>";
	
	jsSpringMsg.attributeName="<spring:message code='parsing.service.attr.grid.column.attr.name' ></spring:message>";
	jsSpringMsg.unifiedFieldName="<spring:message code='parsing.service.attr.grid.column.unified.field' ></spring:message>";
	jsSpringMsg.description="<spring:message code='parsing.service.attr.grid.column.desc' ></spring:message>";
	jsSpringMsg.defaultVal="<spring:message code='parsing.service.attr.grid.column.default.val' ></spring:message>";
	jsSpringMsg.trimChar="<spring:message code='parsing.service.attr.grid.column.trin.char' ></spring:message>";
	jsSpringMsg.dateFormat="<spring:message code='parser.grid.date.format.label' ></spring:message>";
	jsSpringMsg.updateAction="<spring:message code='staffManager.audit.search.action.lable.name'></spring:message>";
	
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
	
	jsSpringMsg.tapComposer='<%=EngineConstants.TAP_COMPOSER_PLUGIN%>';
	jsSpringMsg.rapComposer='<%=EngineConstants.RAP_COMPOSER_PLUGIN%>';
	jsSpringMsg.nrtrdeComposer='<%=EngineConstants.NRTRDE_COMPOSER_PLUGIN%>';

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
	jsSpringMsg.destinationField="<spring:message code='composer.attr.grid.destination.field.name' ></spring:message>";
	
	jsSpringMsg.groupName="<spring:message code='roaming.group.attr.grid.group.name' ></spring:message>";
	
	var selMappingId = 0;
	var urlAction = '';
	var urlActionGrpAttr = '';
	var ckIntanceSelectedAttrListOnPopUp = new Array();
	var ckIntanceSelectedGroupAttrListOnPopUp = new Array();
	
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
                            		<%-- <a id="back" href="javascript:;" onclick="goToService();">
                            		   ${serviceName}&nbsp;-&nbsp;${serviceInstanceId} --%>
                            		   <a id="back" href="#" onclick="goToDriverPage();">
<%--                             		  ${serviceName}&nbsp;-&nbsp;${serviceInstanceId} --%>
											${driverName}&nbsp;-&nbsp;${driverId}	
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
                                               	 <sec:authorize access="hasAnyAuthority('VIEW_COMPOSER')">
                                                    <li class="<c:if test="${(REQUEST_ACTION_TYPE eq 'ROAMING_COMPOSER_CONFIGURATION')}"><c:out value="active"></c:out></c:if>" onclick="showButtons('composer_configuration'); getRoamingComposerConfig('');" >
                                                    	<a href="#asn1Configuration" data-toggle="tab" id="composer_configuration"><spring:message code="parsing.service.config.tab.header"></spring:message></a>
                                                    </li>
                                                    <li class="<c:if test="${(REQUEST_ACTION_TYPE eq 'ROAMING_COMPOSER_ATTRIBUTE')}"><c:out value="active" ></c:out></c:if>" onclick="showButtons('composer_attribute_configuration'); getRoamingComposerAttrDetails('');" >
                                                    	<a href="#asn1Attribute" data-toggle="tab" id="composer_attribute_configuration"><spring:message code="parsing.service.attr.list.tab.header"></spring:message></a>
                                                    </li>
                                                 </sec:authorize>  
                                                </ul>
                                                <div class="fullwidth tab-content no-padding">
                                                <sec:authorize access="hasAnyAuthority('VIEW_COMPOSER')">
                                                	<div class="tab-pane <c:if test="${(REQUEST_ACTION_TYPE eq 'ROAMING_COMPOSER_CONFIGURATION')}"><c:out value="active"></c:out></c:if>" id="#asn1Configuration">
                                                		<c:if test="${(REQUEST_ACTION_TYPE eq 'ROAMING_COMPOSER_CONFIGURATION')}">
	                                                		<c:if test="${(plugInType eq 'TAP_COMPOSER_PLUGIN')}">
	                                                			<jsp:include page="tapComposer/tapComposerConfiguration.jsp" ></jsp:include>
	                                                		</c:if>
	                                                		<c:if test="${(plugInType eq 'RAP_COMPOSER_PLUGIN')}">
	                                                			<jsp:include page="rapComposer/rapComposerConfiguration.jsp" ></jsp:include>
	                                                		</c:if>
	                                                		<c:if test="${(plugInType eq 'NRTRDE_COMPOSER_PLUGIN')}">
	                                                			<jsp:include page="nrtrdeComposer/nrtrdeComposerConfiguration.jsp" ></jsp:include>
	                                                		</c:if>
                                                		</c:if>
                                                	</div>
                                                	<div class="tab-pane <c:if test="${(REQUEST_ACTION_TYPE eq 'ROAMING_COMPOSER_ATTRIBUTE')}"><c:out value="active"></c:out></c:if>" id="asn1Attribute">
                                               			<c:if test="${(REQUEST_ACTION_TYPE eq 'ROAMING_COMPOSER_ATTRIBUTE')}">
	                                               			<c:if test="${(plugInType eq 'TAP_COMPOSER_PLUGIN')}">
	                                               				<jsp:include page="tapComposer/tapComposerAttribute.jsp" ></jsp:include>
	                                               			</c:if>
	                                               			<c:if test="${(plugInType eq 'RAP_COMPOSER_PLUGIN')}">
	                                               				<jsp:include page="rapComposer/rapComposerAttribute.jsp" ></jsp:include>
	                                               			</c:if>
	                                               			<c:if test="${(plugInType eq 'NRTRDE_COMPOSER_PLUGIN')}">
	                                               				<jsp:include page="nrtrdeComposer/nrtrdeComposerAttribute.jsp" ></jsp:include>
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
        	
				<form id="asn1-composer-form" action = "" method="">
					<input type="hidden" id="serviceId" name="serviceId" value="${serviceId}"/>
					<input type="hidden" id="serviceName" name="serviceName" value="${serviceName}"/>
					<input type="hidden" id="serviceType" name="serviceType" value='${serviceType}'>
					<input type="hidden" id="instanceId" name="instanceId" value="${instanceId}">
					<input type="hidden" id="serviceInstanceId" name="serviceInstanceId" value="${serviceInstanceId}">
					<input type="hidden" id="plugInId" name="plugInId" value="${plugInId}"/>
					<input type="hidden" id="plugInName" name="plugInName" value="${plugInName}"/> 
					<input type="hidden" id="plugInType" name="plugInType" value="${plugInType}"/>
					<input type="hidden" id="mappingId" name="mappingId" value=""/>
					<input type="hidden" id="deviceName" name="deviceName" value=""/>
					<input type="hidden" id="deviceId" name="deviceId" value=""/>
					<input type="hidden" id="mappingName" name="mappingName" value=""/>
					<input type="hidden" id="attributeType" name="attributeType" value=""/>
					<input type="hidden" id="driverId" name="driverId" value="${driverId}"/>
	    			<input type="hidden" id="driverName" name="driverName" value="${driverName}"/>
	    			<input type="hidden" id="driverTypeAlias" name="driverTypeAlias" value="${driverTypeAlias}"/>
				</form>
				
				<form id="go-to-service-form" action = "<%=ControllerConstants.INIT_UPDATE_SERVICE %>" method="GET">
					<input type="hidden" id="serviceId" name="serviceId" value="${serviceId}">
					<input type="hidden" id="serviceType" name="serviceType" value="${serviceType}" />	
					<input type="hidden" id="serviceName" name="serviceName" value="${serviceName}">
					<input type="hidden" id="serviceInstanceId" name="serviceInstanceId" value="${serviceInstanceId}">
					<input type="hidden" id="instanceId" name="instanceId" value="${instanceId}">
				</form>
			   
			   <form action="<%= ControllerConstants.INIT_DISTRIBUTION_DRIVER_CONFIGURATION %>" method="POST" id="go-to-driver-form">
	    		<input type="hidden" id="driverId" name="driverId" value="${driverId}"/>
	    		<input type="hidden" id="driverName" name="driverName" value="${driverName}"/>
	    		<input type="hidden" id="driverTypeAlias" name="driverTypeAlias" value="${driverTypeAlias}"/>
	    		<input type="hidden" id="serviceId" name="serviceId" value="${serviceId}"/>
	    		<input type="hidden" id="serviceName" name="serviceName" value="${serviceName}"/>
	    		<input type="hidden" id="instanceId" name="instanceId" value="${instanceId}"/>
	    		<input type="hidden" id="serviceInstanceId" name="serviceInstanceId" value="${serviceInstanceId}">
	    	</form>
			   
			</div>
    </div>
    <!-- /.content-wrapper --> 

    
    <!-- Footer Start -->
    

	<footer class="main-footer positfix">
    	<div class="fooinn">
        	<div class="fullwidth">
       			<div class="padleft-right" id="asn1ComposerConfigDiv" >
        		<sec:authorize access="hasAuthority('UPDATE_COMPOSER')"> 
					<button class="btn btn-grey btn-xs " id="composer_association_save_next_btn"  type="button" onclick="setReadOnlyFlag();"><spring:message code="btn.label.save.next"></spring:message></button>
                   	<button class="btn btn-grey btn-xs"  id="composer_association_customize_btn"   type="button" onclick="customizeMappingDetails();"><spring:message code="btn.label.customize"></spring:message></button>
                </sec:authorize> 
                <button class="btn btn-grey btn-xs"  id="composer_association_cancel_btn" type="button" onclick="goToService();"><spring:message code="btn.label.cancel"></spring:message></button>
				
				</div>
				<%-- <div class="padleft-right"  id="asn1ComposerConfigbtnDiv" style="display: none;">
					<c:if test="${(REQUEST_ACTION_TYPE ne 'ASN1_TRAILER_COMPOSER_ATTRIBUTE') && (readOnlyFlag eq 'false') }">
						<button class="btn btn-grey btn-xs " id="move_to_next_attribute"  type="button" onclick="setReadOnlyFlagWithUpdateActionType();"><spring:message code="btn.label.save.next"></spring:message></button>
					</c:if>
				</div> --%>
            	<!-- <div class="padleft-right" id="asn1_attribute_default" style="display: none;">
				</div>
				<div class="padleft-right" id="asn1_header_attribute_default" style="display: none;">
				</div>
				<div class="padleft-right" id="asn1_trailer_attribute_default" style="display: none;">
				</div> -->
        	</div>
     		<jsp:include page="../../../common/newfooter.jsp"></jsp:include>
     	</div>
	</footer>
    <!-- Footer End --> 
    
</div>
<!-- ./wrapper --> 

<script>
	function showHideButtonBasedOnTabsSelected(tabType){
		$("#asn1ComposerConfigDiv").hide();
		$("#asn1_attribute_default").hide();
		$("#asn1_header_attribute_default").hide();
		$("#asn1_trailer_attribute_default").hide();
		$("#asn1ComposerConfigbtnDiv").hide();		
		if(tabType == 'ROAMING_COMPOSER_CONFIGURATION'){
			$("#asn1ComposerConfigDiv").show();
		}/* else if(tabType == 'ROAMING_COMPOSER_ATTR_LIST'){
			$("#asn1_attribute_default").show();
			$("#asn1ComposerConfigbtnDiv").show();
		}else if(tabType == 'ROAMING_COMPOSER_HEADER_ATTR_LIST'){
			$("#asn1_header_attribute_default").show();
			$("#asn1ComposerConfigbtnDiv").show();
		}else if(tabType == 'ROAMING_COMPOSER_TRAILER_ATTR_LIST'){
			$("#asn1_trailer_attribute_default").show();
			$("#asn1ComposerConfigbtnDiv").show();
		} */
	}
	
	function showButtons(id){
		if(id == 'composer_configuration'){
			showHideButtonBasedOnTabsSelected('ROAMING_COMPOSER_CONFIGURATION');
		}else if(id == 'composer_attribute_configuration'){
			showHideButtonBasedOnTabsSelected('ROAMING_COMPOSER_ATTR_LIST');
		}/* else if(id == 'composer_header_attribute_configuration'){
			showHideButtonBasedOnTabsSelected('ROAMING_COMPOSER_HEADER_ATTR_LIST');
		}else if(id == 'composer_trailer_attribute_configuration'){
			showHideButtonBasedOnTabsSelected('ROAMING_COMPOSER_TRAILER_ATTR_LIST');
		} */
	}
	
		
</script>
<script src="${pageContext.request.contextPath}/customJS/deviceManager.js<%= "?v=" + Math.random() %>" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/customJS/composerManager.js<%= "?v=" + Math.random() %>" type="text/javascript"></script> 

<script>
$(document).ready(function() {
	var activeTab = $(".nav-tabs li.active a");
	var id = activeTab.attr("id");
	showButtons(id);
	
	$("#deviceType").val('-1');
	
	var readOnlyFlag='${readOnlyFlag}';
	
	if(readOnlyFlag == '' || readOnlyFlag ==true){
		disableRoamingAdvanceDetails();	
	}

	var composerDeviceTypeId = '${selDeviceTypeId}';
	var composerVendorTypeId = '${selVendorTypeId}';
	var composerDeviceId = '${selDeviceId}';
	var composerMappingId = '${selMappingId}';	
	
	setDropdownValue('deviceType',composerDeviceTypeId);
	
	var currentTab = '${REQUEST_ACTION_TYPE}';
	if(composerDeviceTypeId > 0 && currentTab == 'ROAMING_COMPOSER_CONFIGURATION'){
		getVendorByDeviceType('getVendorListByDeviceType',$("#deviceType").val() ,'vendorType',' ','null');
		
		setDropdownValue('vendorType',composerVendorTypeId);
		
		var deviceTypeId = $('#deviceType').val();
		var vendorId = $('#vendorType').val();
		 
		getDeviceByVendorName('<%=ControllerConstants.GET_DEVICE_LIST_BY_VENDOR%>', deviceTypeId, vendorId,'deviceName');
		
		setDropdownValue('deviceName',composerDeviceId);
		
		var deviceId = $('#deviceName').val();
		var pluginType = '${plugInType}';
		
		getMappingByDeviceAndComposerType('<%=ControllerConstants.GET_COMPOSER_MAPPING_LIST_BY_DEVICE%>',deviceId,pluginType,'mappingName','device_block');
		setDropdownValue('mappingName',composerMappingId);
		
		var mappingId = $('#mappingName').val();
		if('${isValidationFail}' == '' || '${isValidationFail}' == false){
			getMappingDetails('<%=ControllerConstants.GET_COMPOSER_MAPPING_DETAILS_ID%>',mappingId,pluginType);	
		}
		
	}
	
});

function getRoamingComposerConfig(action){
	
	if(currentComposerType === jsSpringMsg.tapComposer){
		action = '<%=ControllerConstants.INIT_TAP_COMPOSER_MANGER%>';
	}else if(currentComposerType === jsSpringMsg.rapComposer){
		action = '<%=ControllerConstants.INIT_RAP_COMPOSER_MANGER%>';
	}else if(currentComposerType === jsSpringMsg.nrtrdeComposer){
		action = '<%=ControllerConstants.INIT_NRTRDE_COMPOSER_MANGER%>';
	}
	
	$("#asn1-composer-form").attr("action",action);
	$("#asn1-composer-form").attr("method","GET");
	
	$("#asn1-composer-form #deviceName").val($("#deviceName").find(":selected").text());
	$("#asn1-composer-form #mappingName").val($("#mappingName").find(":selected").text());
	$("#asn1-composer-form #mappingId").val($("#mappingName").find(":selected").val());
	$("#asn1-composer-form #deviceId").val($("#deviceName").find(":selected").val());
	
	$("#asn1-composer-form").submit();
}

function getRoamingComposerAttrDetails(action,attributeType){

	if(currentComposerType === jsSpringMsg.tapComposer){
		action = '<%=ControllerConstants.INIT_TAP_COMPOSER_ATTRIBUTE%>';
	}else if(currentComposerType === jsSpringMsg.rapComposer){
		action = '<%=ControllerConstants.INIT_RAP_COMPOSER_ATTRIBUTE%>';
	}else if(currentComposerType === jsSpringMsg.nrtrdeComposer){
		action = '<%=ControllerConstants.INIT_NRTRDE_COMPOSER_ATTRIBUTE%>';
	}
	
	$("#asn1-composer-form").attr("action",action);
	$("#asn1-composer-form").attr("method","POST");
	$("#asn1-composer-form #attributeType").val(attributeType);
	$("#asn1-composer-form #deviceName").val($("#deviceName").find(":selected").text());
	if($("#asn1-composer-form #deviceName").val() == ''){
		$("#asn1-composer-form #deviceName").val('${deviceName}')
	}
	$("#asn1-composer-form #mappingName").val($("#mappingName").find(":selected").text());
	if($("#asn1-composer-form #mappingName").val() == ''){
		$("#asn1-composer-form #mappingName").val('${mappingName}');
	}
	$("#asn1-composer-form #mappingId").val($("#mappingName").find(":selected").val());
	if($("#asn1-composer-form #mappingId").val() == ''){
		$("#asn1-composer-form #mappingId").val("${mappingId}");
	}
	$("#asn1-composer-form #deviceId").val($("#deviceName").find(":selected").val());
	if($("#asn1-composer-form #deviceId").val() == ''){
		$("#asn1-composer-form #deviceId").val('${deviceId}');
	}
	$("#asn1-composer-form").submit();
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
	
	associateMappingWithComposer();
}

function setReadOnlyFlagOnCancelBtn() {
	$("#readOnlyFlag").val('true');
	$("#instanceId").val('${instanceId}');
	$("#serviceType").val('${serviceType}');
}

function setReadOnlyFlagWithUpdateActionType(){
	
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
	$("#ascii-composer-configuration-form").attr("action",'updateAsn1ComposerConfiguration');
	$("#ascii-composer-configuration-form").submit();
}

function enableRoamingAdvanceDetails(){
	
	console.log("Going to enable content");
	$("#composer_association_save_next_btn").show();
	$("#advance_details_div").css("opacity","");
	
	$("#advance_details_div_mask").remove(); 
	console.log("mask div removed");
	
}
/* Function will disable the content */
function disableRoamingAdvanceDetails(){
	$("#advance_details_div_mask").remove(); 	
	$('#advance_details_div').fadeTo('slow',.6);
	$element = $('#advance_details_div');
	var top = Math.ceil($element.position().top);
	var elementStr = "<div id='advance_details_div_mask' style='position: absolute;top:"+top+"px;left:0;width: 100%;height:100%;z-index:2;opacity:0.4;filter: alpha(opacity = 50)'></div>";
	$element.append(elementStr);
	disableSaveBtn();
}

function disableSaveBtn(){
	$("#move_to_next_attribute").hide();
}
	<sec:authorize access="hasAuthority('VIEW_COMPOSER')">
		isViewComposerAccess = true;
	</sec:authorize>
	<sec:authorize access="hasAuthority('ADD_COMPOSER')">
		isAddComposerAccess = true;
	</sec:authorize>
	<sec:authorize access="hasAuthority('UPDATE_COMPOSER')">
		isEditComposerAccess = true;
	</sec:authorize>
	<sec:authorize access="hasAuthority('DELETE_COMPOSER')">
		isDeleteComposerAccess = true;
	</sec:authorize>
		
</script>
</body>
</html>
