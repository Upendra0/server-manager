<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>

<!DOCTYPE html>
<html>

<jsp:include page="../common/newheader.jsp"></jsp:include>

<body class="skin-blue sidebar-mini sidebar-collapse">
	
	<!-- Main div start here -->
	<div>
		<jsp:include page="../common/newtopNavigationPanel.jsp"></jsp:include>
		<jsp:include page="../common/newleftMenu.jsp"></jsp:include>

		<!-- Content wrapper div start here -->
		<div class="content-wrapper">
			<section class="content-header">
				<div class="fullwidth">
					<div style="padding: 0 10px 0 4px;">
						<div id="content-scroll-d" class="content-scroll">
							<div class="fullwidth">
								<h4 style="margin-top:10px; margin-bottom:0px; font-size:11px;">
								<a href="home.html"><img src="img/tile-icon.png" class="vmiddle" alt="Title Icon"/></a> 
                                    	 <span class="spanBreadCrumb" style="line-height: 30px;"> 
											 <sec:authorize access="hasAnyAuthority('STAFF_MANAGER_MENU_VIEW')">                                    	 
												<strong><a href="<%=ControllerConstants.STAFF_MANAGER%>">
												<spring:message code="staffManager.staff.mgmt.tab" ></spring:message></a>
												</strong>
											</sec:authorize>
											&nbsp;/ 
											<sec:authorize access="hasAnyAuthority('STAFF_MANAGER_MENU_VIEW')">                                    	 
												<strong>
												<a href="javascript:;" onclick="redirectToStaffMgmt();" >
													<spring:message code="staffManager.audit.tab.label" ></spring:message>
												</a>
												</strong>
											</sec:authorize>
										</span>
								</h4>
								<jsp:include page="../common/responseMsg.jsp" ></jsp:include>	
								
								<!-- Tab code start here  -->
								<div class="row">
									<div class="col-xs-12">
										<div class="box  martop" style="border: none; box-shadow: none;">
											<div class="box-body table-responsive no-padding">
												<div class="nav-tabs-custom tab-content">
													<!-- Change Role here -->
													<sec:authorize access="hasAnyAuthority('VIEW_AUDIT')">  
														<ul class="nav nav-tabs pull-right">
																<li class="active">
																	<a href="#staff-audit-details-view" id="staff_audit_details_view" data-toggle="tab" aria-expanded="false"> 
																		<spring:message code="staffManager.audit.detail.tab.label" ></spring:message>
																	</a>
																</li>
														</ul>
														<div class="fullwidth padding10 tab-content">
															<div class="fullwidtd inline-form" style="overflow: auto; height:700px;">
						
																<table class="table table-hover table-bordered padleft-right"  style="width:100%;" border="1" >
																	<tr>
																		<th><spring:message code="staffAudit.management.details.table.column.id" ></spring:message></th>
																		<th><spring:message code="staffAudit.management.details.table.column.fieldname" ></spring:message></th>
																		<th><spring:message code="staffAudit.management.details.table.column.oldvalue" ></spring:message></th>
																		<th><spring:message code="staffAudit.management.details.table.column.updatedvalue" ></spring:message></th>
																	</tr>	
																	<c:forEach items="${auditDetailList}" var="auditDetailList" varStatus="audit">
																		<tr>
																			<td class="padleft-right">${audit.index +1}</td>
																			<td class="padleft-right">${auditDetailList.fieldname}</td>
																		    <td class="padleft-right">${auditDetailList.oldvalue}</td>
											   								<td class="padleft-right">${auditDetailList.newvalue}</td>
										   								</tr> 
																	</c:forEach>		
																</table>
															 </div>			
																		
														</div>
													</sec:authorize>
													<!-- Change Role here -->
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
		<form id="audit-details-mgmt" action="<%=ControllerConstants.STAFF_AUDIT%>" method="POST">
			<input type="hidden" id="REQUEST_ACTION_TYPE" name="REQUEST_ACTION_TYPE" value="STAFF_AUDIT_MANAGEMENT"/> 
		</form>
			</section>

		</div>
		<!-- Content wrapper div start here -->
		<script type="text/javascript">
			function redirectToStaffMgmt(){
				$('#audit-details-mgmt').submit();	
			}
		</script>
		<!-- Footer code start here -->
		<footer class="main-footer positfix">
		    	<div class="fooinn">
		        	<jsp:include page="../common/newfooter.jsp"></jsp:include>
		    	</div>
			</footer>
		<!-- Footer code end here -->
	</div>
	<!-- Main div end here -->
</body>
</html>	
	
