<%@page import="com.elitecore.sm.util.MapCache"%>
<%@page import="com.elitecore.sm.common.constants.SystemParametersConstant"%>
<%@page import="com.elitecore.sm.common.constants.EngineConstants"%>
<%@page import="javax.persistence.EnumType"%>
<%@page import="com.elitecore.sm.common.constants.BaseConstants"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<%@page import="com.elitecore.sm.common.constants.BaseConstants"%>
<!DOCTYPE html>
<html>
<jsp:include page="../../../common/newheader.jsp"></jsp:include>
<script type="text/javascript">

var jsSpringMsg = {};

jsSpringMsg.pluginName  =  "<spring:message code='distribution.service.pathlist.plugin.grid.column.name' ></spring:message>";
jsSpringMsg.pluginType  =  "<spring:message code='distribution.service.pathlist.plugin.grid.column.type' ></spring:message>";
jsSpringMsg.writeFilePath = "<spring:message code='distribution.service.pathlist.plugin.grid.column.write.path' ></spring:message>";
jsSpringMsg.writePrefix = "<spring:message code='distribution.service.pathlist.add.plugin.write.prefix.label' ></spring:message>";
jsSpringMsg.writeSuffix = "<spring:message code='distribution.service.pathlist.add.plugin.write.suffix.label' ></spring:message>";
jsSpringMsg.backUpPath = "<spring:message code='distribution.service.pathlist.add.plugin.backup.path' ></spring:message>";
jsSpringMsg.fileExtension = "<spring:message code='distribution.service.pathlist.add.plugin.file.ext' ></spring:message>";
jsSpringMsg.characterRename ="<spring:message code='distribution.service.pathlist.add.plugin.char.renam.oper.header' ></spring:message>";
jsSpringMsg.editAction = "<spring:message code='distribution.service.pathlist.plugin.grid.column.edit' ></spring:message>";

jsSpringMsg.totalRows = '<%= MapCache.getConfigValueAsInteger(SystemParametersConstant .TOTAL_ROWS_TO_DISPLAY_IN_GRID, 10) %>';

jsSpringMsg.pagerText = "<spring:message code='jq.grid.pager.text' ></spring:message>";
jsSpringMsg.emptyRecordText = "<spring:message code='jq.grid.empty.records.text' ></spring:message>";
jsSpringMsg.totalRecordText ="<spring:message code='jq.grid.pager.total.records.text' ></spring:message>";
jsSpringMsg.loadingText = "<spring:message code='jq.grid.loading.text' ></spring:message>";

jsSpringMsg.addPathlistAction = '<%=ControllerConstants.CREATE_DISTRIBUTION_DRIVER_PATH_LIST%>';
jsSpringMsg.updatePathlistAction = '<%=ControllerConstants.UPDATE_DISTRIBUTION_DRIVER_PATH_LIST%>';


jsSpringMsg.seqNo  =  "<spring:message code='distributionService.pathlist.plugin.sequence.label' ></spring:message>";
jsSpringMsg.query  =  "<spring:message code='distributionService.pathlist.plugin.query.label' ></spring:message>";
jsSpringMsg.position = "<spring:message code='distributionService.pathlist.plugin.position.label' ></spring:message>";
jsSpringMsg.startIndex = "<spring:message code='distribution.service.pathlist.add.plugin.start.idx' ></spring:message>";
jsSpringMsg.endIndex = "<spring:message code='distribution.service.pathlist.add.plugin.end.idx' ></spring:message>";
jsSpringMsg.padding = "<spring:message code='distributionService.pathlist.plugin.padding.label' ></spring:message>";
jsSpringMsg.defaultVal = "<spring:message code='distributionService.pathlist.plugin.default.label' ></spring:message>";
jsSpringMsg.paddingLength ="<spring:message code='distributionService.pathlist.plugin.length.label' ></spring:message>";

jsSpringMsg.noResult ="<spring:message code='jq.grid.empty.records.text' ></spring:message>";
jsSpringMsg.databaseFieldName="<spring:message code='database.driver.attrlist.database.field.name.label' ></spring:message>";
jsSpringMsg.unifiedFieldName="<spring:message code='parsing.service.add.attr.uni.field' ></spring:message>";
jsSpringMsg.dataType="<spring:message code='composer.attr.grid.data.type' ></spring:message>";
jsSpringMsg.defualtValue="<spring:message code='parsing.service.add.attr.default.val' ></spring:message>";

var selDriverId = 0;
var urlAction = '';

</script>
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
                            		<span class="spanBreadCrumb" style="line-height: 30px;"> 
										<strong><a href="<%= ControllerConstants.INIT_SERVICE_MANAGER%>"><spring:message code="collectionService.leftmenu.home" ></spring:message></a>&nbsp;/ 
										<a href="#" onclick="viewDistributionService()">
										${serviceName}&nbsp;-&nbsp;${serviceInstanceId}</a>
										</strong>
									</span>
                            		</strong>
                            	</span> 
                            	<ol class="breadcrumb1 breadcrumb mtop10">
                                	<li><a href="#"><strong><spring:message code="collectionService.driver.add.driver.name"></spring:message></strong></a></li>
                                	<li>${driverName}</li>
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
                                                
                                                	<%-- <sec:authorize access="hasAnyAuthority('VIEW_DISTRIBUTION_DRIVER')"> --%>
                                                	
                                                    <li class="<c:if test="${(REQUEST_ACTION_TYPE eq 'DISTRIBUTION_DRIVER_CONFIGURATION')}"><c:out value="active"></c:out></c:if>" onclick="loadDriverTabData('<%=ControllerConstants.INIT_DISTRIBUTION_DRIVER_CONFIGURATION %>'); showButtons('ftp-config-a');" >
                                                   
                                                    	<c:choose>
														  <c:when test="${(driverTypeAlias eq 'FTP_DISTRIBUTION_DRIVER')}">
																<a href="#ftpConfig" data-toggle="tab" id="ftp-config-a"><spring:message code="ftp.driver.mgmt.config.tab.header"></spring:message></a>
														  </c:when>
														  <c:when test="${(driverTypeAlias eq 'SFTP_DISTRIBUTION_DRIVER')}">
																<a href="#ftpConfig" data-toggle="tab" id="ftp-config-a"><spring:message code="sftp.driver.mgmt.config.tab.header"></spring:message></a>
														  </c:when>
														  <c:when test="${(driverTypeAlias eq 'LOCAL_DISTRIBUTION_DRIVER')}">
																<a href="#ftpConfig" data-toggle="tab" id="ftp-config-a"><spring:message code="local.driver.mgmt.config.tab.header"></spring:message></a>
														  </c:when>
														  <c:otherwise>
														    	<a href="#ftpConfig" data-toggle="tab" id="ftp-config-a"><spring:message code="database.driver.mgmt.config.tab.header"></spring:message></a>
														  </c:otherwise>
														</c:choose>
                                                    </li>
                                                  <%--   </sec:authorize> --%>
                                                  <%--   <sec:authorize access="hasAnyAuthority('VIEW_DISTRIBUTION_DRIVER')"> --%>
                                               	<c:choose>
													<c:when test="${(driverTypeAlias eq 'DATABASE_DISTRIBUTION_DRIVER')}"> 
                                                    <li class="<c:if test="${(REQUEST_ACTION_TYPE eq 'DISTRIBUTION_DRIVER_ATTRLIST_CONFIGURATION')}"><c:out value="active" ></c:out></c:if>" onclick="loadDriverTabData('<%=ControllerConstants.INIT_DISTRIBUTION_DRIVER_ATTRLIST_MANAGER %>'); showButtons('attr-list-config-a');" >
                                                    	<a href="#attrListConfig" data-toggle="tab" id="attr-list-config-a"><spring:message code="database.driver.mgmt.attrlist.config.tab.header"></spring:message></a>
                                                    </li>
                                                    </c:when>
                                                </c:choose>
                                                
                                                    <li class="<c:if test="${(REQUEST_ACTION_TYPE eq 'DISTRIBUTION_DRIVER_PATHLIST_CONFIGURATION')}"><c:out value="active" ></c:out></c:if>" onclick="loadDriverTabData('<%=ControllerConstants.INIT_DISTRIBUTION_DRIVER_PATHLIST_MANAGER %>'); showButtons('path-list-config-a');" >
                                                    	<a href="#pathListConfig" data-toggle="tab" id="path-list-config-a"><spring:message code="ftp.driver.mgmt.pathlist.config.tab.header"></spring:message></a>
                                                    </li>
                                                  <%--   </sec:authorize> --%>
                                                </ul>
                                                <div class="fullwidth tab-content no-padding">
                                                	<c:if test="${(REQUEST_ACTION_TYPE eq 'DISTRIBUTION_DRIVER_CONFIGURATION')}">
                                                    		<jsp:include page="distributionDriverConfiguration.jsp" ></jsp:include> 
                                                    </c:if>
                                                    
                                                    <c:if test="${(REQUEST_ACTION_TYPE eq 'DISTRIBUTION_DRIVER_ATTRLIST_CONFIGURATION')}">
                                                    	<jsp:include page="databaseDistributionDriverAttrList.jsp" ></jsp:include>
                                                    </c:if>
                                                    
                                                    <c:if test="${(REQUEST_ACTION_TYPE eq 'DISTRIBUTION_DRIVER_PATHLIST_CONFIGURATION')}">
                                                    	<jsp:include page="pathlistConfiguration.jsp" ></jsp:include>
                                                    </c:if>
                                                </div>
                                            </div>
                                        </div>
                                        <!-- /.box-body --> 
                                    </div>
                                    <!-- /.box --> 
                                    
                                </div>
                            </div>
                            <div style="display: hidden;">
								<form id="loadDistributionDriverConfiguration" method="POST">
									<input type="hidden" id="driverId" name="driverId" value="${driverId}"/>
						    		<input type="hidden" id="driverName" name="driverName" value="${driverName}" />
						    		<input type="hidden" id="driverTypeAlias" name="driverTypeAlias" value="${driverTypeAlias}"/>
						    		<input type="hidden" id="serviceId" name="serviceId" value="${serviceId}"/>
						    		<input type="hidden" id="serviceName" name="serviceName" value="${serviceName}"/>
						    		<input type="hidden" id="instanceId" name="instanceId" value="${instanceId}"/>
						    		<input type="hidden" id="serviceDBStats" name="serviceDBStats" value="${serviceDbStats}"/>
						    		<input type="hidden" id="serviceInstanceId" name="serviceInstanceId" value="${serviceInstanceId}">
								</form>
							</div>
						    <div style="display: none;">
								<form id="distribution_service_form" method="GET" action="<%= ControllerConstants.INIT_DISTRIBUTION_SERVICE_MANAGER %>">
									<input type="hidden" id="serviceId" name="serviceId" value="${serviceId}">
									<input type="hidden" id="serviceType" name="serviceType" value='<%=EngineConstants.DISTRIBUTION_SERVICE%>'>
									<input type="hidden" id="serviceName" name="serviceName" value="${serviceName}">
									<input type="hidden" id="instanceId" name="instanceId" value="${instanceId}">
									<input type="hidden" id="serviceInstanceId" name="serviceInstanceId" value="${serviceInstanceId}">
								</form>
								
								<form id="distribution-service-composer-config" method="GET" action="<%=ControllerConstants.INIT_COMPOSER_CONFIGURATION%>">
									<input type="hidden" id="serviceId" name="serviceId" value="${serviceId}"/>
									<input type="hidden" id="serviceName" name="serviceName" value="${serviceName}"/>
									<input type="hidden" name="instanceId"  id="instanceId" value="${instanceId}"/>
									<input type="hidden" id="serviceInstanceId" name="serviceInstanceId" value="${serviceInstanceId}">
									<input type="hidden" name="serviceType"  id="serviceType" value='<%=EngineConstants.DISTRIBUTION_SERVICE%>'/>
									<input type="hidden" id="plugInId" name="plugInId"/>
									<input type="hidden" id="plugInName" name="plugInName"/> 
									<input type="hidden" id="plugInType" name="plugInType"/>
									<input type="hidden" id="driverId" name="driverId" value="${driverId}"/>
						    		<input type="hidden" id="driverName" name="driverName" value="${driverName}" />
						    		<input type="hidden" id="driverTypeAlias" name="driverTypeAlias" value="${driverTypeAlias}"/>
									
								</form>	
							</div>
                        </div>
                        <!-- Content Wrapper. Contains page content End --> 
                    </div>
                </div>
            </div>
        </section>
    </div>
    
    <!-- /.content-wrapper --> 
    
    <!-- Footer Start -->
	<footer class="main-footer positfix">
    	<div class="fooinn">
        	<div class="fullwidth">
       			<div class="padleft-right" id="ftpConfigDiv" style="display: none;">
        		<sec:authorize access="hasAuthority('UPDATE_DISTRIBUTION_DRIVER')"> 
					<button id="updateBtn" class="btn btn-grey btn-xs " type="button" tabindex="19" onclick="updateConfiguration();"><spring:message code="btn.label.update"></spring:message></button>
                   	<button id="resetBtn" class="btn btn-grey btn-xs" type="button" tabindex="20" onclick="resetFTPConfiguration();"><spring:message code="btn.label.reset"></spring:message></button>
                </sec:authorize>
                   	<button class="btn btn-grey btn-xs" type="button" tabindex="21" onclick="viewDistributionService();"><spring:message code="btn.label.cancel"></spring:message></button>
                 <div class="pull-right" style="font-size:10px;"><i class="fa fa-square" style="font-size: 9px"></i>&nbsp;&nbsp;<spring:message code="restart.operation.require.message" ></spring:message> </div>
				</div>	
				<div class="padleft-right" id="attrlistConfigDiv" style="display: none;">
				</div>
            	<div class="padleft-right" id="pathlistConfigDiv" style="display: none;">
				</div>
				
        	</div>
     		<jsp:include page="../../../common/newfooter.jsp"></jsp:include>
     	</div>
	</footer>
    <!-- Footer End --> 
    
</div>
<!-- ./wrapper --> 

<script>
	$(document).ready(function() {
		var activeTab = $(".nav-tabs li.active a");
		var id = activeTab.attr("id");
		showButtons(id);
	});

	function showHideButtonBasedOnTabsSelected(tabType){
		$("#ftpConfigDiv").hide();
		$("#pathlistConfigDiv").hide();
		$("#attrlistConfigDiv").hide();
		
		if(tabType == 'DISTRIBUTION_DRIVER_CONFIGURATION'){
			$("#ftpConfigDiv").show();
		}else if(tabType == 'DISTRIBUTION_DRIVER_PATHLIST_CONFIGURATION'){
			$("#pathlistConfigDiv").show();
		}else if(tabType == 'DISTRIBUTION_DRIVER_ATTRLIST_CONFIGURATION'){
			$("#attrlistConfigDiv").show();
		}
	}
	
	function showButtons(id)
	{
		if(id == 'ftp-config-a'){
			showHideButtonBasedOnTabsSelected('DISTRIBUTION_DRIVER_CONFIGURATION');
		}else if(id == 'path-list-config-a'){
			showHideButtonBasedOnTabsSelected('DISTRIBUTION_DRIVER_PATHLIST_CONFIGURATION');
		}else if(id == 'attr-list-config-a'){
			showHideButtonBasedOnTabsSelected('DISTRIBUTION_DRIVER_ATTRLIST_CONFIGURATION');
		}
	}
	
	function updateConfiguration(){
		var driverType = '${driverTypeAlias}';			
		var ftpMaxRetryCountDistribution=$('#ftp-maxRetrycount').val();
		var ftpNoFileAlertDistribution=$('#ftp-ftpConnectionParams-noFileAlert').val();
		var ftpMinFileRange=$('#ftp-minFileRange').val();
		var ftpMaxFileRange=$('#ftp-maxFileRange').val();
		var hostPort=$('#ftp-ftpConnectionParams-port').val();
		if(ftpMaxRetryCountDistribution==null || ftpMaxRetryCountDistribution==''){
			$('#ftp-maxRetrycount').val('-1');
		} 
		if(ftpNoFileAlertDistribution==null || ftpNoFileAlertDistribution==''){
			$('#ftp-ftpConnectionParams-noFileAlert').val('-1');
		} 
		if(ftpMinFileRange==null || ftpMinFileRange==''){
			$('#ftp-minFileRange').val('-1');
		} 
		if(ftpMaxFileRange==null || ftpMaxFileRange==''){
			$('#ftp-maxFileRange').val('-1');
		} 
		if(hostPort==null || hostPort==''){
			$('#ftp-ftpConnectionParams-port').val('-1');
		}
		
		if(driverType == 'FTP_DISTRIBUTION_DRIVER' || driverType == 'SFTP_DISTRIBUTION_DRIVER' || driverType == 'LOCAL_DISTRIBUTION_DRIVER'){
			var ftpAttributes = $("#ftp-attributes_dropdown").val();
			if (ftpAttributes === null || ftpAttributes === '' || ftpAttributes === 'undefined')
					ftpAttributes = "";
			else
					ftpAttributes = ftpAttributes.join(",");
			$("#ftp-attributes").val(ftpAttributes);
			
			var rollingDuration = $("#ftp-fileRollingDuration").val();
			if(rollingDuration == null || rollingDuration == ''){
				$('#ftp-fileRollingDuration').val('-1');
			} 
			
			var controlFileStart = $("#controlFileSeq-startRange").val();
			if(controlFileStart == null || controlFileStart == ''){
				$('#controlFileSeq-startRange').val('-1');
			} 
			
			var controlFileEnd = $("#controlFileSeq-endRange").val();
			if(controlFileEnd == null || controlFileEnd == ''){
				$('#controlFileSeq-endRange').val('-1');
			}
			
			var ftp_attrSep = $("#ftp-attributesSep_dropdown").val();
			if (ftp_attrSep == "s") {
				$('#ftp-attributeSeparator').val(' ');
			} else {
				$('#ftp-attributeSeparator').val(ftp_attrSep);
			}
			
			$('#ftp-attributeSeparator').attr('disabled',false);
			$('#ftp-fileSeqEnable').attr('disabled',false);
			$('#controlFileSeq-resetFrequency').attr('disabled',false);
			$('#controlFileSeq-paddingEnable').attr('disabled',false);
		}
				
		if(driverType == 'FTP_DISTRIBUTION_DRIVER'){
			$("#ftp-configuration-form").attr("action",'<%=ControllerConstants.UPDATE_FTP_DISTRIBUTION_DRIVER_CONFIGURATION%>');
		}else if(driverType == 'SFTP_DISTRIBUTION_DRIVER'){
			$("#ftp-configuration-form").attr("action",'<%=ControllerConstants.UPDATE_SFTP_DISTRIBUTION_DRIVER_CONFIGURATION%>');
		}else if(driverType == 'LOCAL_DISTRIBUTION_DRIVER'){
			$("#ftp-configuration-form").attr("action",'<%=ControllerConstants.UPDATE_LOCAL_DISTRIBUTION_DRIVER_CONFIGURATION%>');
		}else if(driverType == 'DATABASE_DISTRIBUTION_DRIVER'){
			$("#ftp-configuration-form").attr("action",'<%=ControllerConstants.UPDATE_DATABASE_DISTRIBUTION_DRIVER_CONFIGURATION%>');
		}
		$("#ftp-configuration-form").submit();
	}
</script>
<script src="${pageContext.request.contextPath}/customJS/distributionDriverManager.js<%= "?v=" + Math.random() %>" type="text/javascript"></script>
</body>
</html>
