<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<%@page import="com.elitecore.sm.common.constants.BaseConstants"%>

<!DOCTYPE html>
<html>
<head>
<script type="text/javascript">	
	var isAddDeviceAccess = false;
	var isDeleteDeviceAccess = false;
	var isEditDeviceAccess = false;	
	var isViewDeviceAccess = false;
	var ckIntanceSelected = new Array();
	
	var jsSpringMsg = {};
	
		jsSpringMsg.deviceType = "<spring:message code='device.list.grid.device.type.label'></spring:message>";
		jsSpringMsg.vendorName = "<spring:message code='device.list.grid.device.vendor.type.label'></spring:message>";
		jsSpringMsg.deviceName = "<spring:message code='device.list.grid.device.name.label'></spring:message>";
		jsSpringMsg.decodeType = "<spring:message code='device.list.search.device.decode.type.label'></spring:message>";
		jsSpringMsg.mappingName = "<spring:message code='device.list.grid.device.mapping.name.label'></spring:message>";
		jsSpringMsg.associated = "<spring:message code='device.list.grid.device.associated.label'></spring:message>";
		jsSpringMsg.mappingId="<spring:message code='device.list.grid.device.mapping.label.column.id'></spring:message>";
		jsSpringMsg.deviceId="<spring:message code='device.list.grid.device.label.column.id'></spring:message>";
		
		jsSpringMsg.emptyRecord = "<spring:message code='device.list.grid.empty.records'></spring:message>";
		jsSpringMsg.recordText = "<spring:message code='device.list.grid.pager.total.records.text'></spring:message>";
		jsSpringMsg.pagerText = "<spring:message code='device.list.grid.pager.text'></spring:message>";
		jsSpringMsg.loadingText = "<spring:message code='device.list.grid.loading.text'></spring:message>";
		
		jsSpringMsg.gridCaption = "<spring:message code='device.tab.heading.label'></spring:message>";
		jsSpringMsg.srno="<spring:message code='device.attribute.list.details.sr.no.label'></spring:message>";
		jsSpringMsg.sourceField="<spring:message code='device.attribute.list.details.sourcefield.label'></spring:message>";
		jsSpringMsg.unifieldField="<spring:message code='device.attribute.list.details.unifiedfield.label'></spring:message>";
		jsSpringMsg.attDescription="<spring:message code='device.attribute.list.details.description.label'></spring:message>";
		jsSpringMsg.defaultVal="<spring:message code='device.attribute.list.details.defaultval.label'></spring:message>";
		jsSpringMsg.trimChar="<spring:message code='device.attribute.list.details.trimchar.label'></spring:message>";
		jsSpringMsg.dateFormat="<spring:message code='device.attribute.list.details.dateformat.label'></spring:message>";

		jsSpringMsg.attrNotFound="<spring:message code='device.mapping.attribute.not.found'></spring:message>";
		
		jsSpringMsg.selDevice="<spring:message code='device.name.dropdown.default.selection.label'></spring:message>";
		jsSpringMsg.selVendorType="<spring:message code='vendor.type.dropdown.default.selection.label'></spring:message>";
		jsSpringMsg.selDeviceType="<spring:message code='decode.type.dropdown.default.selection.label'></spring:message>";
		jsSpringMsg.selOther="<spring:message code='device.dropdown.other.option.label'></spring:message>";
		jsSpringMsg.selmappingName="<spring:message code='mapping.name.dropdown.default.selection.label'></spring:message>";
		
		jsSpringMsg.serverInsName="<spring:message code='device.association.details.server.instance.name.label'></spring:message>";
		jsSpringMsg.serverIPPORT="<spring:message code='device.association.details.server.ip.port.label'></spring:message>";
		jsSpringMsg.serviceInsName="<spring:message code='device.association.details.service.instance.name.label'></spring:message>";
		jsSpringMsg.pluginName="<spring:message code='device.association.details.plugin.name.label'></spring:message>";
		
		
		jsSpringMsg.mappingDefault = "<spring:message code='mapping.dropdown.default.option.label'></spring:message>";
		jsSpringMsg.mappingMessage="<spring:message code='deviceConfig.invalid.mapping.message'></spring:message>";
		jsSpringMsg.deviceValidateMessage="<spring:message code='device.invalid.message'></spring:message>";
		
		jsSpringMsg.mappingPluginType="<spring:message code='device.list.grid.device.mapping.type.label'></spring:message>";
		
		
		jsSpringMsg.getAttributeAction = '<%=ControllerConstants.GET_ATTRIBUTE_LIST_BY_MAPPING_ID%>';
</script>

</head>
<jsp:include page="../common/newheader.jsp"></jsp:include>
<body class="skin-blue sidebar-mini sidebar-collapse">

<!-- Main div start Here -->
<div>
	<jsp:include page="../common/newtopNavigationPanel.jsp"></jsp:include>
	<jsp:include page="../common/newleftMenu.jsp"></jsp:include>
	
	<!-- Content div Start here -->
	<div class="content-wrapper">
			<section class="content-header">
				<div class="fullwidth">
					<div style="padding: 0 10px 0 4px;">
						<div id="content-scroll-d" class="content-scroll">
							<div class="fullwidth">
								<h4 style="margin-top:10px; margin-bottom:0px; font-size:11px;">
									<a href="home.html"><img src="img/tile-icon.png"class="vmiddle" alt="Home" /></a> 
									<span class="spanBreadCrumb" style="line-height: 30px;"> 
										<strong> <spring:message code="leftmenu.label.device.configuration" ></spring:message></strong>
									</span>
								</h4>
								<jsp:include page="../common/responseMsg.jsp" ></jsp:include>
								<!-- Tab code start here  -->
								<div class="row">
									<div class="col-xs-12">
										<div class="box martop" style="border: none; box-shadow: none;">
											<div class="box-body table-responsive no-padding">
												<div class="nav-tabs-custom tab-content">
			
													 <ul class="nav nav-tabs pull-right">
															<sec:authorize access="hasAnyAuthority('VIEW_DEVICE')">
																<li class="<c:if test="${(REQUEST_ACTION_TYPE eq 'DEVICE_MANAGEMENT')}">
																	<c:out value="active"></c:out></c:if>"	onclick="showButtons('collection_service_summary'); loadCollectionDetails('<%=ControllerConstants.INIT_COLLECTION_SERVICE_MANAGER%>','GET','COLLECTION_SERVICE_SUMMARY');" >
																	<a href="#collection-service-summary" id="collection_service_summary" data-toggle="tab" aria-expanded="false"> 
																	<spring:message	code="device.tab.heading.label" ></spring:message>
																</a></li>
															</sec:authorize>
														</ul>
														
														<div class="fullwidth no-padding tab-content">
														<sec:authorize access="hasAnyAuthority('VIEW_DEVICE')">
															<div id="device_mgmt_div"
																class="tab-pane <c:if test="${(REQUEST_ACTION_TYPE eq 'DEVICE_MANAGEMENT')}"><c:out value="active"></c:out></c:if> ">
														 		<c:if test="${(REQUEST_ACTION_TYPE eq 'DEVICE_MANAGEMENT')}">
														 			<jsp:include page="deviceManagement.jsp"></jsp:include> 
														 		</c:if>	 
															</div>
														</sec:authorize>
														</div>
												</div>
											</div>
										</div>
									</div>
								</div>
								<!-- Tab code end here -->
							</div>
						</div>
					</div>
				</div>
			</section>
		</div>
		<!-- Content div End here -->
		
		<!-- Footer code start here -->
		<footer class="main-footer positfix">
		    	<div class="fooinn">
		        	<jsp:include page="../common/newfooter.jsp"></jsp:include>
		    	</div>
			</footer>
		<!-- Footer code end here -->

</div>
<!-- Main div end Here -->
<script type="text/javascript">
	<sec:authorize access="hasAuthority('VIEW_DEVICE')">
		isViewDeviceAccess = true;
	</sec:authorize>
	<sec:authorize access="hasAuthority('ADD_DEVICE')">
		isAddDeviceAccess = true;
	</sec:authorize>
	<sec:authorize access="hasAuthority('DELETE_DEVICE')">
		isDeleteDeviceAccess = true;
	</sec:authorize>
</script>
<script src="${pageContext.request.contextPath}/customJS/deviceManager.js<%= "?v=" + Math.random() %>" type="text/javascript"></script>
</body>
</html>
