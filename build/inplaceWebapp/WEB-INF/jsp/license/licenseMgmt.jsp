<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<!DOCTYPE html>
<html>
<jsp:include page="../common/newheader.jsp"></jsp:include>
<script type="text/javascript">
var jsSpringMsg = {};
jsSpringMsg.fileNotSelectMsg= "<spring:message code='license.apply.upload.file.not.select.msg' ></spring:message>";
jsSpringMsg.fullLicenseSuccessMsg = "<spring:message code='full.license.create.success' ></spring:message>";
jsSpringMsg.engineFullLicenseSuccessMsg = "<spring:message code='engine.full.license.create.success' ></spring:message>";
jsSpringMsg.containerLicenseSuccessMsg = "<spring:message code='container.license.create.success' ></spring:message>";
</script>
<body class="skin-blue sidebar-mini sidebar-collapse">
	
	<!-- Main div start here -->
	<div>
		<jsp:include page="../common/newtopNavigationPanel.jsp"></jsp:include>
		<jsp:include page="../common/newleftMenu.jsp"></jsp:include>
		<div class="content-wrapper">
			<section class="content-header">
				<div class="fullwidth">
					<div style="padding: 0 10px 0 4px;">
						<div id="content-scroll-d" class="content-scroll">
							<div class="fullwidth">
								<h4 style="margin-top:10px; margin-bottom:0px; font-size:11px;">
									<a href="home.html"><img src="img/tile-icon.png"class="vmiddle" alt="Home" /></a> 
									<span class="spanBreadCrumb" style="line-height: 30px;"> 
										<strong>&nbsp;
											
												<c:choose>
												    <c:when test="${(licenseType eq 'APPLY_LICENSE')}">
												        <spring:message	code="license.apply.full.tab.label" ></spring:message>
												    </c:when>
												    <c:otherwise>
												        <spring:message	code="license.renew.full.tab.label" ></spring:message>
												    </c:otherwise>
												</c:choose>
											
										</strong>
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
													
														<ul class="nav nav-tabs pull-right">
														
															<sec:authorize access="hasAnyAuthority('FULL_LICENSE_MANAGEMENT')">
																<li class="<c:if test="${(REQUEST_ACTION_TYPE eq 'APPLY_LICENSE')}"><c:out value="active"></c:out></c:if>"	>
																	<a href="javascript:;" id="apply_license_tab" data-toggle="tab" aria-expanded="false">
																		<c:choose>
																		    <c:when test="${(licenseType eq 'APPLY_LICENSE')}">
																		        <spring:message	code="license.apply.full.tab.label" ></spring:message>
																		    </c:when>
																		    <c:otherwise>
																		        <spring:message	code="license.renew.full.tab.label" ></spring:message>
																		    </c:otherwise>
																		</c:choose>
																	</a>
																</li>
															</sec:authorize>
														</ul>

														<sec:authorize access="hasAnyAuthority('FULL_LICENSE_MANAGEMENT')">
															<div id="apply-license-tab" class="tab-pane <c:if test="${(REQUEST_ACTION_TYPE eq 'APPLY_LICENSE')}"><c:out value="active"></c:out></c:if>">
														 		<jsp:include page="applyLicense.jsp"></jsp:include>  
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
			</section>
		</div>
		<a href="#divSuccessMessage" class="fancybox" style="display: none;" id="checkboxServerSuccess">#</a>
		<div id="divSuccessMessage" style=" width:100%; display: none;" >
		    <div class="modal-content">
		        <div class="modal-header padding10">
		            <h4 class="modal-title"><spring:message code="license.activation.header.label"></spring:message></h4>
		        </div>
		        <div class="modal-body padding10 inline-form">
		        <input type="hidden" id="checkboxServerId" name="checkboxServerId" />
		        	<span id="deleteDriverResponseMsg" style="display:none;">
		        		<jsp:include page="../common/responseMsgPopUp.jsp" ></jsp:include>
		        	</span>
			        <p id="license_sucess_message">
			        </p>
		        </div>
		        <c:choose>
		        <c:when test="${(componentType eq 'SERVER_MANAGER_CONTAINER')}"> 
		        	<div class="modal-footer padding10">
			            <button type="button" class="btn btn-grey btn-xs " data-dismiss="modal" onclick="closeFancyBox();" ><spring:message code="btn.label.ok"></spring:message></button>
			        </div>
		        </c:when>
		        <c:otherwise>
			        <div class="modal-footer padding10">
			            <button type="button" class="btn btn-grey btn-xs " data-dismiss="modal" onclick="logoutFormSubmit();" ><spring:message code="btn.label.ok"></spring:message></button>
			        </div>
		        </c:otherwise>
		        </c:choose>
				
		    </div>
		    <!-- /.modal-content --> 
		</div>
		<form action="<%=request.getContextPath()%>/<%= ControllerConstants.GET_TRIAL_LICENSE %>" method="GET" id="login_page">
			</form> 
		
		<!-- Content wrapper div start here -->
		<!-- Footer code start here -->
		<script src="${pageContext.request.contextPath}/customJS/license.js<%= "?v=" + Math.random() %>" type="text/javascript"></script>
		<footer class="main-footer positfix" style="margin-left: 0;">
			<div class="fooinn">
				<jsp:include page="../common/newfooter.jsp"></jsp:include>
			</div>
		</footer>
		<!-- Footer code end here -->
	</div>
	<!-- Main div end here -->
</body>
</html>	
