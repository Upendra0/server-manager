<%@page import="com.elitecore.sm.common.constants.BusinessModelConstants"%>
<%@taglib 	uri="http://www.springframework.org/tags" 			prefix="spring"%>
<%@ taglib 	uri="http://www.springframework.org/security/tags" 	prefix="sec"  %>
<%@ taglib 	uri="http://java.sun.com/jsp/jstl/core" 			prefix="c" %>
<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<%@page import="com.elitecore.sm.common.constants.BaseConstants"%>
<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<script>
	var currentTab = '${REQUEST_ACTION_TYPE}';
	console.log(currentTab);
	var	ruleRecordColumns = [];
	var	ruleRecordColModels = [];
</script>
	
	<jsp:include page="../common/newheader.jsp"></jsp:include>
	
	 <!-- <script src="js/jquery-ui.min.js"></script> --> 
<body class="skin-blue sidebar-mini sidebar-collapse">
	<div>

		<jsp:include page="../common/newtopNavigationPanel.jsp"></jsp:include>
		<jsp:include page="../common/newleftMenu.jsp"></jsp:include>

		<div class="content-wrapper">
			<section class="content-header">
				<div class="fullwidth">
		       		<div style="padding:0 10px 0 4px;">
		       			<div id="content-scroll-d" class="content-scroll">
		        			<div class="fullwidth">
								<h4 style="margin-top:10px; margin-bottom:0px; font-size:11px;">
									<a href="home.html"><img src="img/tile-icon.png" class="vmiddle" alt="Home"/></a> 
	                            	<span class="spanBreadCrumb" style="line-height: 30px;">
	                            		<strong>&nbsp;
	                            			<spring:message code="leftmenu.label.mis.configuration" ></spring:message>
	                            		</strong>
	                            	</span>
								</h4>
								<jsp:include page="../common/responseMsg.jsp" ></jsp:include>
	         					<div class="row">
		           					<div class="col-xs-12">
		             					<div class="box  martop" style="border:none; box-shadow: none;">
		               						<div class="box-body table-responsive no-padding">
		               							<div class="nav-tabs-custom tab-content">
		               								<sec:authorize access="hasAnyAuthority('MIS_REPORTS_MENU_VIEW')">
			               								
			               								<ul class="nav nav-tabs pull-right">
											                <sec:authorize access="hasAnyAuthority('SERVICE_WISE_SUMMARY')">
				                 								<li class="<c:if test="${(REQUEST_ACTION_TYPE eq 'SERVICE_WISE_SUMMARY')}"><c:out value="active"></c:out></c:if>" onclick="loadServiceWiseSummary();<!-- showButtons('service_wise_summary'); -->" >
				                 									<a href="#service-wise-summary" id="service_wise_summary" data-toggle="tab" aria-expanded="false">
				                 										<spring:message code="reportManager.service.summary" ></spring:message>
				                 									</a>
				                 								</li>
				                 							</sec:authorize>
				                 							<sec:authorize access="hasAnyAuthority('SERVICE_WISE_DETAIL')">
				                 								<li class="<c:if test="${(REQUEST_ACTION_TYPE eq 'SERVICE_WISE_DETAIL')}"><c:out value="active"></c:out></c:if>" onclick="loadServiceWiseDetail();<!-- showButtons('service_wise_detail'); -->">
				                 									<a href="#service-wise-detail" id="service_wise_detail" data-toggle="tab" aria-expanded="true">
				                 										<spring:message code="reportManager.service.detail" ></spring:message>
				                 									</a>
				                 								</li>
				                 							</sec:authorize>
				                 							<sec:authorize access="hasAnyAuthority('DYNAMIC_REPORT')">
				                 								<li class="<c:if test="${(REQUEST_ACTION_TYPE eq 'VIEW_DYNAMIC_REPORT') or (REQUEST_ACTION_TYPE eq 'VIEW_DYNAMIC_REPORT_TABLE_RECORDS') or (REQUEST_ACTION_TYPE eq 'DYNAMIC_REPORT') }"><c:out value="active"></c:out></c:if>" onclick="loadDynamicReport();<!-- showButtons('service_wise_detail'); -->">
				                 									<a href="#dynamic-report" id="dynamic_report" data-toggle="tab" aria-expanded="true">
				                 										<spring:message code="reportManager.service.dynamicreport" ></spring:message>
				                 									</a>
				                 								</li>
				                 							</sec:authorize>
			               								</ul>
			               								
			               								<sec:authorize access="hasAnyAuthority('SERVICE_WISE_SUMMARY')">
				           									<div id="service-mgmt" class="fullwidth mtop10 tab-pane <c:if test="${(REQUEST_ACTION_TYPE eq 'SERVICE_WISE_SUMMARY')}"><c:out value="active"></c:out></c:if> ">
				           									<c:if test="${(REQUEST_ACTION_TYPE eq 'SERVICE_WISE_SUMMARY')}">
				           										<jsp:include page="serviceWiseSummary.jsp"></jsp:include>
				           										</c:if>
				           									</div>
				           								</sec:authorize>
				           								<sec:authorize access="hasAnyAuthority('SERVICE_WISE_DETAIL')">
				         									<div id="create-service" class="fullwidth mtop10 tab-pane <c:if test="${(REQUEST_ACTION_TYPE eq 'SERVICE_WISE_DETAIL')}"><c:out value="active"></c:out></c:if>">
				         									<c:if test="${(REQUEST_ACTION_TYPE eq 'SERVICE_WISE_DETAIL')}">
				         										<jsp:include page="serviceWiseDetail.jsp"></jsp:include>
				         										</c:if>
				         									</div>
				         								</sec:authorize>
				         							
				         								<sec:authorize access="hasAnyAuthority('DYNAMIC_REPORT')">
				         									<div id="dynamic-report-mgmt" class="fullwidth mtop10 tab-pane <c:if test="${(REQUEST_ACTION_TYPE eq 'VIEW_DYNAMIC_REPORT')}"><c:out value="active"></c:out></c:if>">
				         									<c:if test="${(REQUEST_ACTION_TYPE eq 'VIEW_DYNAMIC_REPORT')}">
				         										<jsp:include page="dynamicReportMgmt.jsp"></jsp:include>
				         										</c:if>
				         									</div>
				         								</sec:authorize>
				         								
				         								<sec:authorize access="hasAnyAuthority('DYNAMIC_REPORT')">
				         									<div id="dynamic-report-mgmt" class="fullwidth mtop10 tab-pane <c:if test="${(REQUEST_ACTION_TYPE eq 'VIEW_DYNAMIC_REPORT_TABLE_RECORDS')}"><c:out value="active"></c:out></c:if>">
				         									<c:if test="${(REQUEST_ACTION_TYPE eq 'VIEW_DYNAMIC_REPORT_TABLE_RECORDS')}">
				         										<jsp:include page="viewDynamicReportTableRecords.jsp"></jsp:include>
				         										</c:if>
				         									</div>
				         								</sec:authorize>
				         								
			               							</sec:authorize>
	             								</div>
											</div>
		               					</div>
		             				</div>
		           				</div>
		           				
		         				</div>
		         			</div>
		         		</div>
		      		</div>
		    	</section>
		   	</div>
		</div><!-- ./wrapper -->
	</body>
	<div style="display: hidden">
		<form id="loadServiceWiseSummaryForm" action ="<%= ControllerConstants.INIT_MIS_REPORT_MANAGER %>" method="GET">
			<input type="hidden" name="<%= BaseConstants.REQUEST_ACTION_TYPE %>"  id="<%= BaseConstants.REQUEST_ACTION_TYPE %>" value="<%= BaseConstants.SERVICE_WISE_SUMMARY%>" />
		</form>
	</div>
	<div style="display: hidden">
		<form id="loadServiceWiseDetailForm" action ="<%= ControllerConstants.INIT_MIS_REPORT_MANAGER %>" method="GET">
			<input type="hidden" name="<%= BaseConstants.REQUEST_ACTION_TYPE %>"  id="<%= BaseConstants.REQUEST_ACTION_TYPE %>" value="<%= BaseConstants.SERVICE_WISE_DETAIL%>" />
		</form>
	</div>
	<div style="display: hidden">
		<form id="loadDynamicReportForm" action ="<%= ControllerConstants.INIT_DYNAMIC_REPORT_CONFIG %>" method="GET">
			<input type="hidden" name="<%= BaseConstants.REQUEST_ACTION_TYPE %>"  id="<%= BaseConstants.REQUEST_ACTION_TYPE %>" value="<%= BaseConstants.VIEW_DYNAMIC_REPORT%>" />
		</form>
	</div>
	
		<!-- Footer code start here -->
		<footer class="main-footer positfix">
		    	<div class="fooinn">
		        	<jsp:include page="../common/newfooter.jsp"></jsp:include>
		    	</div>
			</footer>
		<!-- Footer code end here -->
	</div>
	<!-- Main div end here -->
	<script type="text/javascript">
	
	function showHideButtonBasedOnTabsSelected(tabType){
			$("#serviceListDiv").hide();
			$("#createServiceDiv").hide();
			
			if(tabType == 'SERVICE_WISE_SUMMARY'){
				$("#serviceListDiv").show();
			}else if(tabType == 'SERVICE_WISE_DETAIL'){
				$("#createServiceDiv").show();
			}
		}
		
		function showButtons(id)
		{
			if(id == 'service_wise_summary'){
				showHideButtonBasedOnTabsSelected('SERVICE_WISE_SUMMARY');
			}else if(id == 'service_wise_detail'){
				showHideButtonBasedOnTabsSelected('SERVICE_WISE_DETAIL');
			}
		}
	
	function loadServiceWiseSummary(){
		$("#loadServiceWiseSummaryForm").submit();
	}
	
	function loadServiceWiseDetail(){
		$("#loadServiceWiseDetailForm").submit();
	}
	
	function loadDynamicReport(){
		$("#loadDynamicReportForm").submit();
		
	}
	</script>
	
	
</html>
