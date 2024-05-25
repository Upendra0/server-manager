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
	jsSpringMsg.defaultVal="<spring:message code='parsing.service.attr.grid.column.default.val' ></spring:message>";
	jsSpringMsg.sourceFieldFormat="<spring:message code='parsing.service.attr.grid.column.source.field.format' ></spring:message>";
	jsSpringMsg.excelRow="<spring:message code='parsing.service.attr.grid.column.excel.row' ></spring:message>";
	jsSpringMsg.excelCol="<spring:message code='parsing.service.attr.grid.column.excel.col' ></spring:message>";
	jsSpringMsg.relativeExcelRow="<spring:message code='parsing.service.attr.grid.column.relative.excel.row' ></spring:message>";
	jsSpringMsg.trimChar="<spring:message code='parsing.service.attr.grid.column.trin.char' ></spring:message>";
	jsSpringMsg.trimPosition="<spring:message code='parsing.service.attr.grid.column.trim.position' ></spring:message>";
	jsSpringMsg.startsWith="<spring:message code='parser.grid.field.startsWith' ></spring:message>";
    jsSpringMsg.tableFooter="<spring:message code='parsing.service.attr.grid.column.table.footer' ></spring:message>";
    jsSpringMsg.columnStartsWith="<spring:message code='parsing.service.attr.grid.column.starts.with' ></spring:message>";
    jsSpringMsg.columnContains="<spring:message code='parsing.service.attr.grid.column.contains' ></spring:message>";
    jsSpringMsg.tableRowAttribute="<spring:message code='parsing.service.attr.grid.table.row.attribute' ></spring:message>";
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
	
	jsSpringMsg.xlsParser='<%=EngineConstants.XLS_PARSING_PLUGIN%>';
	
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
															<li
																class="<c:if test="${(REQUEST_ACTION_TYPE eq 'XLS_PARSER_CONFIGURATION')}"><c:out value="active"></c:out></c:if>"
																onclick="showButtons('parser_configuration'); getxlsParserConfig('<%=ControllerConstants.INIT_XLS_PARSER_CONFIG%>');">
																<a href="#xlsConfiguration" data-toggle="tab"
																id="parser_configuration"><spring:message
																		code="parsing.service.config.tab.header" ></spring:message></a>
															</li>
															<li
																class="<c:if test="${(REQUEST_ACTION_TYPE eq 'XLS_PARSER_ATTRIBUTE')}"><c:out value="active" ></c:out></c:if>"
																onclick="showButtons('parser_attribute_configuration'); getxlsParserAttrDetails('<%=ControllerConstants.INIT_XLS_PARSER_ATTRIBUTE%>');">
																<a href="#xlsAttribute" data-toggle="tab"
																id="parser_attribute_configuration"><spring:message
																		code="parsing.service.attr.list.tab.header" ></spring:message></a>
															</li>
															<li
																class="<c:if test="${(REQUEST_ACTION_TYPE eq 'XLS_PARSER_GROUP_ATTRIBUTE')}"><c:out value="active" ></c:out></c:if>"
																onclick="showButtons('parser_attribute_configuration'); getxlsParserGroupAttrDetails('<%=ControllerConstants.INIT_XLS_PARSER_GROUP_ATTRIBUTE%>','true');">
																<a href="#xlsGroupAttribute" data-toggle="tab"
																id="parser_group_attribute_configuration"><spring:message
																		code="parsing.service.group.attr.list.tab.header" ></spring:message></a>
															</li>
														</sec:authorize>
													</ul>
													<div class="fullwidth tab-content no-padding">
														<sec:authorize access="hasAnyAuthority('VIEW_PARSER')">
															<div
																class="tab-pane <c:if test="${(REQUEST_ACTION_TYPE eq 'XLS_PARSER_CONFIGURATION')}"><c:out value="active"></c:out></c:if>"
																id="xlsConfiguration">
																<c:if
																	test="${(REQUEST_ACTION_TYPE eq 'XLS_PARSER_CONFIGURATION')}">
																	<jsp:include page="xlsParserConfiguration.jsp" ></jsp:include>
																</c:if>
															</div>

															<div
																class="tab-pane <c:if test="${(REQUEST_ACTION_TYPE eq 'XLS_PARSER_ATTRIBUTE')}"><c:out value="active"></c:out></c:if>"
																id="xlsAttribute">
																<c:if
																	test="${(REQUEST_ACTION_TYPE eq 'XLS_PARSER_ATTRIBUTE')}">
																	<jsp:include page="xlsParserAttribute.jsp" ></jsp:include>
																</c:if>
															</div>

															<div
																class="tab-pane <c:if test="${(REQUEST_ACTION_TYPE eq 'XLS_PARSER_GROUP_ATTRIBUTE')}"><c:out value="active"></c:out></c:if>"
																id="xlsGroupAttribute">
																<c:if
																	test="${(REQUEST_ACTION_TYPE eq 'XLS_PARSER_GROUP_ATTRIBUTE')}">
																	<jsp:include page="xlsParserGroupConfig.jsp" ></jsp:include>
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
        	
				<form id="xls-parser-form" action = "" method="">
					<input type="hidden" id="serviceId" name="serviceId" value="${serviceId}"/>
					<input type="hidden" id="serviceName" name="serviceName" value="${serviceName}"/>
					<input type="hidden" id="serviceType" name="serviceType" value='${serviceType}'>
					<input type="hidden" id="serviceInstanceId" name="serviceInstanceId" value="${serviceInstanceId}">
					<input type="hidden" id="instanceId" name="instanceId" value="${instanceId}">
					<input type="hidden" id="plugInId" name="plugInId" value="${plugInId}"/>
					<input type="hidden" id="plugInName" name="plugInName" value="${plugInName}"/> 
					<input type="hidden" id="plugInType" name="plugInType" value="${plugInType}"/>
					<input type="hidden" id="mappingId" name="mappingId" value=""/>
					<input type="hidden" id="deviceName" name="deviceName" value=""/>
					<input type="hidden" id="deviceId" name="deviceId" value=""/>
					<input type="hidden" id="mappingName" name="mappingName" value=""/>
					<input type="hidden" id="readOnlyFlagGroup" name="readOnlyFlagGroup" value=""/>
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
					<div class="padleft-right" id="xlsParserConfigDiv"
						style="display: none;">
						<sec:authorize access="hasAuthority('UPDATE_PARSER')">
							<button class="btn btn-grey btn-xs "
								id="parser_association_save_next_btn" type="button"
								onclick="setReadOnlyFlag();">
								<spring:message code="btn.label.save.next" ></spring:message>
							</button>
							<button class="btn btn-grey btn-xs"
								id="parser_association_customize_btn" type="button"
								onclick="customizeMappingDetails();">
								<spring:message code="btn.label.customize" ></spring:message>
							</button>
						</sec:authorize>
						<button class="btn btn-grey btn-xs"
							id="parser_association_cancel_btn" type="button"
							onclick="goToService();">
							<spring:message code="btn.label.cancel" ></spring:message>
						</button>
					</div>
					<div class="padleft-right" id="xlsParserConfigbtnDiv"
						style="display: none;">
						<c:if
							test="${(REQUEST_ACTION_TYPE eq 'XLS_PARSER_ATTRIBUTE') && (readOnlyFlag eq 'false') }">
							<button class="btn btn-grey btn-xs " id="move_to_next_attribute"
								type="button" onclick="getxlsParserGroupAttrDetails('<%=ControllerConstants.INIT_XLS_PARSER_GROUP_ATTRIBUTE%>','false');">
								<spring:message code="btn.label.save.next" ></spring:message>
							</button>
						</c:if>
					</div>
					<div class="padleft-right" id="xls_attribute_default"
						style="display: none;"></div>
				</div>
				<script>
				function showHideButtonBasedOnTabsSelected(tabType){
					$("#xlsParserConfigDiv").hide();
					$("#xls_attribute_default").hide();
					
					if(tabType == 'XLS_PARSER_CONFIGURATION'){
						$("#xlsParserConfigDiv").show();
					}else if(tabType == 'XLS_PARSER_ATTR_LIST'){
						$("#xls_attribute_default").show();
						//$("#xlsParserConfigDiv").show();
						$("#xlsParserConfigbtnDiv").show();
					}
				}
				
				function showButtons(id){
					if(id == 'parser_configuration'){
						showHideButtonBasedOnTabsSelected('XLS_PARSER_CONFIGURATION');
					}else if(id == 'parser_attribute_configuration'){
						showHideButtonBasedOnTabsSelected('XLS_PARSER_ATTR_LIST');
					}
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
					disableXlsAdvanceDetails();	
				}
			
				var parserDeviceTypeId = '${selDeviceTypeId}';
				var parserVendorTypeId = '${selVendorTypeId}';
				var parserDeviceId = '${selDeviceId}';
				var parserMappingId = '${selMappingId}';	
				
				
				setDropdownValue('deviceType',parserDeviceTypeId);
				
				var currentTab = '${REQUEST_ACTION_TYPE}';
				
				
				if(parserDeviceTypeId > 0 && currentTab == 'XLS_PARSER_CONFIGURATION'){
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
					
					var mappingId = $('#mappingName').val();
					if('${isValidationFail}' == '' || '${isValidationFail}' == false){
						getMappingDetails('<%=ControllerConstants.GET_MAPPING_DETAILS_ID%>',mappingId,parserType);	
					}
					
				}
				
			});
			
			function getxlsParserConfig(action){
			
				$("#xls-parser-form").attr("action",action);
				$("#xls-parser-form").attr("method","GET");
				
				$("#xls-parser-form #deviceName").val($("#deviceName").find(":selected").text());
				$("#xls-parser-form #mappingName").val($("#mappingName").find(":selected").text());
				$("#xls-parser-form #mappingId").val($("#mappingName").find(":selected").val());
				$("#xls-parser-form #deviceId").val($("#deviceName").find(":selected").val());
				
				$("#xls-parser-form").submit();
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
			//	$("group_attribute_details_div").show();
				$("#xls-parser-configuration-form").attr("action",'updateXlsParserConfiguration');
				$("#xls-parser-configuration-form").submit();
			}
			function getxlsParserAttrDetails(action){
				
				$("#xls-parser-form").attr("action",action);
				$("#xls-parser-form").attr("method","POST");
				
				$("#xls-parser-form #deviceName").val($("#deviceName").find(":selected").text());
				if($("#xls-parser-form #deviceName").val() == ''){
					$("#xls-parser-form #deviceName").val('${deviceName}')
				}
				
				$("#xls-parser-form #mappingName").val($("#mappingName").find(":selected").text());
				if($("#xls-parser-form #mappingName").val() == ''){
					$("#xls-parser-form #mappingName").val('${mappingName}');
				}
				
				$("#xls-parser-form #mappingId").val($("#mappingName").find(":selected").val());
				if($("#xls-parser-form #mappingId").val() == ''){
					$("#xls-parser-form #mappingId").val("${mappingId}");
				}
				$("#xls-parser-form #deviceId").val($("#deviceName").find(":selected").val());
				if($("#xls-parser-form #deviceId").val() == ''){
					$("#xls-parser-form #deviceId").val('${deviceId}');
				}
				$("#xls-parser-form").submit();
			}
			function getxlsParserGroupAttrDetails(action,flag){
				$("#xls-parser-form").attr("action",action);
				$("#xls-parser-form").attr("method","POST");
				$("#xls-parser-form #mappingId").val($("#mappingName").find(":selected").val());
				if($("#xls-parser-form #mappingId").val() == ''){
					$("#xls-parser-form #mappingId").val("${mappingId}");
				}
				$("#xls-parser-form #mappingName").val($("#mappingName").find(":selected").text());
				if($("#xls-parser-form #mappingName").val() == ''){
					$("#xls-parser-form #mappingName").val('${mappingName}');
				}
				$("#xls-parser-form #readOnlyFlagGroup").val(flag);
				$("#xls-parser-form").submit();
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
			
		 	/* Function will disable the content */
			function disableXlsAdvanceDetails(){
				$("#advance_details_div_mask").remove();
				$('#advance_details_div').fadeTo('slow',.6);
				$element = $('#advance_details_div');
				var top = Math.ceil($element.position().top);
				var elementStr = "<div id='advance_details_div_mask' style='position: absolute;top:"+top+"px;left:0;width: 100%;height:100%;z-index:2;opacity:0.4;filter: alpha(opacity = 50)'></div>";
				$element.append(elementStr);
				
			} 
			
			/*Function will enable the mapping details*/
			function enableXlsAdvanceDetails(){
				$("#advance_details_div_mask").remove();
				$("#advance_details_div").css("opacity","");
				
			}
						
			function setXlsParserAdvanceDetail(responseObject){
			    var fileParsed = document.getElementById('fileParsed');
			    fileParsed.value =  responseObject.fileParsed;
			    var recordWisePdfFormat = document.getElementById('recordWisePdfFormat');
			    recordWisePdfFormat.value = responseObject.recordWisePdfFormat;
			    //showContainsFieldParam();
			}

		
			</script>
     		<jsp:include page="../../../common/newfooter.jsp"></jsp:include>
     	</div>
	</footer>
    <!-- Footer End --> 
    
</div>
<!-- ./wrapper --> 
</body>
</html>
