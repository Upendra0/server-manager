<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<%@page import="com.elitecore.sm.common.constants.LicenseConstants"%>
<%@page import="com.elitecore.sm.util.MapCache"%>
<%@page import="com.elitecore.sm.common.constants.SystemParametersConstant"%>
<!DOCTYPE html>
<html>
<jsp:include page="../common/newheader.jsp"></jsp:include>
<script type='text/javascript' src="${pageContext.request.contextPath}/js/jquery.ajaxfileupload.js"></script>
<script type="text/javascript">
showToggleSidebar =false;
var jsSpringMsg = {};
jsSpringMsg.fileNotSelectMsg= "<spring:message code='license.apply.upload.file.not.select.msg' ></spring:message>";
jsSpringMsg.trailLicenseSucessMsg = "<spring:message code='trial.license.activate.success' ></spring:message>";
jsSpringMsg.fullLicenseSuccessMsg = "<spring:message code='full.license.create.success' ></spring:message>";

jsSpringMsg.engineFullLicenseSuccessMsg = "<spring:message code='engine.full.license.create.success' ></spring:message>";


</script>
<style>
.borbot {
    border-bottom: 1px solid #CCCCCC;
    font-size: 16px;
}
hr {
    background-color:#FE7B26;
}
</style>
<body class="skin-blue sidebar-mini">
	<div class="wrapper">
		<!-- Header Start -->
			<jsp:include page="licenseHeader.jsp"></jsp:include>
		<!-- Header End -->

		<!-- Content Wrapper. Contains page content -->
		<div class="content-wrapper" style="margin-left: 0;">
			<!-- Content Header (Page header) -->
			<section class="content-header">

				<div class="fullwidth mtop15">
					<div style="padding: 0 10px 0 10px;">
						<div  style="padding-bottom: 0;">
							<div class="fullwidth">
								<div class="col-sm-6 inline-form" style="padding-left: 0;">
									<jsp:include page="../common/responseMsg.jsp" ></jsp:include> 
								</div>
								<div class="col-sm-6 text-right mtop10"	style="padding-right: 0;">
									
									<span class="title2rightfield">
											<span class="title2rightfield-icon1-text">
												<a href="#" onclick="gotoAgreement();"><spring:message code="license.activation.agreement.link.label" ></spring:message></a>
											</span>
									</span>									
								</div>
								<div class="clearfix"></div>
							</div>
							<div class="fullwidth mtop10">
								<div class="col-sm-12" >&nbsp;</div>								
							</div>
							
							<div class="fullwidth mtop10">
								<div class="col-sm-6 inline-form" style="padding-left: 0;">
									<div class="form-group">
										<div class="table-cell-label"><label><spring:message code="license.activation.crestel.product.label" ></spring:message><span class="required">*</span></label></div>
										<div class="input-group ">
										
										<c:if test="${SERVER_TYPE_LIST !=null && fn:length(SERVER_TYPE_LIST) gt 0}">
						             		<c:forEach items="${SERVER_TYPE_LIST}" var="serverType">
						             			<input class="checkboxbg"data-on="On" data-off="Off"  type="checkbox" id="checkboxes[${serverType.id}]" data-size="mini"   name="productType" value="${serverType.alias}">&nbsp;${serverType.name}&nbsp;
						             		</c:forEach>
					             		</c:if>
	             					</div>
									</div>
								</div>	
								<div class="col-sm-6 text-right mtop10" style="padding-right: 0;">
								</div>
								<div class="clearfix"></div>
							</div>
							<div class="row">
								<div class="col-md-12">
									<div class="box  martop"	style="border: none; box-shadow: none;">
										<div class="fullwidth">
											<div class="box box-warning">
												<!-- /.box-header -->
												<div class="box-body pbottom10">
													<div class="col-sm-6 web-sep-right">
														<div class="fullwidth  min-height-box"
															style="min-height: 270px;">
															<p class="mtop0 pbottom10 borbot">
																<i class="fa fa-fw fa-save font16"></i>
																<strong><spring:message code="license.activation.trial.heading.label" ></spring:message></strong>
															</p>
															<p>
																<spring:message code="license.activation.trial.middle.text.subtext1" ></spring:message>
																 <u><spring:message code="license.activation.trial.middle.text.subtext2" ></spring:message></u>
																<spring:message code="license.activation.trial.middle.text.subtext3" ></spring:message>
															</p>
															<p><spring:message code="license.activation.trial.middle.text2" ></spring:message></p>
														</div>
														<div class="fullwidth">
															<div class="padleft-right"id="trialLicenseNextBtnDiv" style="text-align: center ;" >
																<button class="btn btn-grey btn-xs"
																	id="trial_license__next_btn" type="button" onclick="trialLicense();">
																	<spring:message code="btn.label.activate.tria.license" ></spring:message>
																</button>
															</div>
															 <div id="trial-license-add-progress-bar-div" class="modal-footer padding10 text-left" style="display: none;">
																<jsp:include page="../common/processing-bar.jsp"></jsp:include>
															</div>
														</div>
													</div>
													<div class="col-sm-6 ">
														<div class="fullwidth min-height-box"
															style="min-height: 270px;">
															<p class="mtop0 pbottom10 borbot">
																<i class="fa fa-fw fa-save font16"></i> 
																<strong><spring:message code="license.activation.full.heading.label" ></spring:message></strong>
															</p>
															<div class="fullwidth">
																<!-- <i class="fa fa-fw fa-save font16 vmiddle"></i> -->
																<a href="#" class="vmiddle link" onclick="downloadLicenseRegistrationForm();"><img src="<%=request.getContextPath()%>/img/download_license.png" height="20" width="20"/></a>
																<a href="#" class="vmiddle link" onclick="downloadLicenseRegistrationForm();"><spring:message code="license.activation.form.download.link.label" ></spring:message></a>
															</div>
															<p class="mtop15">
																<strong><spring:message code="license.activation.upload.key.lable" ></spring:message></strong>
															</p>
															<div class="fullwidth">
																<input type="file" id="licenseActivationKey" class="filestyle" data-buttonText="<spring:message code="serviceMgmt.browse.file"></spring:message>">
															</div>
															<p class="mtop15">
																<spring:message code="license.activation.full.middle.text.subtext1" ></spring:message><a href="mailto:<spring:message code="license.activation.full.middle.text.support.email" ></spring:message>"><spring:message code="license.activation.full.middle.text.support.email" ></spring:message></a>.
															<spring:message code="license.activation.full.middle.text.subtext2" ></spring:message> 
															</p>
														</div>
														<div class="fullwidth">
															<div id="activate_full_license_btn" class="padleft-right" style="text-align: center;">
																<button class="btn btn-grey btn-xs" type="button" onclick="activateFullLicense('<%=ControllerConstants.ACTIVATE_FULL_LICENSE%>','<%=LicenseConstants.LICENSE_SM%>');"><spring:message code="btn.label.activate.full.license" ></spring:message></button>
															</div>
															<div id="activate_full_license_processbar" class="modal-footer padding10 text-left" style="display: none;">
																<img src="img/processing-bar.gif">
															</div>
														</div>
													</div>
												</div>
												<!-- /.box-body -->
											</div>
										</div>
									</div>
									<!-- /.box -->
									<p><spring:message code="license.activation.bottom.notificaton.text" ></spring:message> </p>
								</div>
								
							</div>
								
						</div>
						
					</div>
				</div>
					
			</section>
			<div class="col-md-12 mtop10" >&nbsp;</div>
			<div id="bottombar" class="col-md-12 mtop10" style="padding-top:10px;">  
				<hr style="border-color: #FE7B26; height: 4px;"/>
			</div>
		</div>
		<!-- /.content-wrapper -->.
			
			
			<form action="<%=request.getContextPath()%>/<%= ControllerConstants.INIT_LICENSE_AGREEMENT %>" method="GET" id="home_page">
			</form>
			<form action="<%=request.getContextPath()%>/<%= ControllerConstants.GET_TRIAL_LICENSE %>" method="GET" id="login_page">
			</form> 
			<form action="<%=request.getContextPath()%>/<%= ControllerConstants.DOWNLOAD_LICENSE_FORM %>" method="POST" id="license_download_form">
			</form> 
			
		<!-- Main Footer Start -->
		<footer class="main-footer positfix" style="margin-left: 0;">
			
			<div class="fooinn">
				 <jsp:include page="../common/newfooter.jsp"></jsp:include> 
			</div>
		</footer>
		
		<!-- Main Footer End -->

	</div>
	
	
	<a href="#divcheckboxvalidation" class="fancybox" style="display: none;" id="checkboxServer">#</a>
		<div id="divcheckboxvalidation" style=" width:100%; display: none;" >
		    <div class="modal-content">
		        <div class="modal-header padding10">
		            <h4 class="modal-title"><spring:message code="license.activation.header.label"></spring:message></h4>
		        </div>
		        <div class="modal-body padding10 inline-form">
		        <input type="hidden" id="checkboxServerId" name="checkboxServerId" />
		        	
		        	<span id="deleteDriverResponseMsg" style="display:none;">
		        	
		        		<jsp:include page="../common/responseMsgPopUp.jsp" ></jsp:include>
		        	
		        	</span>
			        <p id="Alert-License-product-checkbox-warning">
			       		 <spring:message code="license.alert.product.not.selected" ></spring:message>
			        </p>
		        </div>
			        <div id="delete-license-popup-progress-bar-div" class="modal-footer padding10" style="display: none;">
					<jsp:include page="../common/processing-bar.jsp"></jsp:include>
				</div>
					<div class="modal-footer padding10">
		            <button type="button" class="btn btn-grey btn-xs " data-dismiss="modal" onclick="closeFancyBox();"><spring:message code="btn.label.close"></spring:message></button>
		        </div>
		    </div>
		    <!-- /.modal-content --> 
		</div>
	<!-- ./wrapper -->
	
	
	<!-- div for sucessful selection of div -->
	
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
				<div class="modal-footer padding10">
		            <button type="button" class="btn btn-grey btn-xs " data-dismiss="modal" onclick="redirectToHome();" ><spring:message code="btn.label.ok"></spring:message></button>
		        </div>
		    </div>
		    <!-- /.modal-content --> 
		</div>
					<c:url value="/home" var="logoutUrl" ></c:url>
					<%-- <c:url value="/j_spring_security_logout" var="logoutUrl" ></c:url> --%>
					<% if(Boolean.parseBoolean(MapCache.getConfigValueAsObject(SystemParametersConstant.SSO_ENABLE).toString())){ %>								
						<c:url	value="/sso/j_spring_security_logout" var="logoutUrl" ></c:url>
					<%}else{ %>
						<c:url	value="/j_spring_security_logout" var="logoutUrl" ></c:url> 
					<%} %>
					<form action="${logoutUrl}" method="get" id="logoutForm">
					</form>
 	<div style="display: none;"> 
 			
 			<form action="<%=ControllerConstants.REDIRECT_LICENSE_AGREEMENT%>" method="POST" id="license_agreement">
 				<input type="hidden" name="disableTrialButton" id="disableTrialButton" value="${disableTrialButton}" />
 			</form>
 	</div>
 	
 	
 	<c:choose>
    <c:when test="${disableTrialButton eq 'true'}">
        <script type="text/javascript">
	        function disableTrialButton(){
		 		 $('#trial_license__next_btn').attr('disabled',true);
		 	}
			disableTrialButton();
	 	</script>
    </c:when>    
</c:choose>
	
	 <script type="text/javascript">
	
			function gotoAgreement() {
				$("#license_agreement").submit();

			}

			function redirectToHome() {
				$("#logoutForm").submit();
			}

			function trialLicense() {
				clearAllMessages();
				validate();
			}

			function redirectLogIn() {
				$("#login_page").submit();
				clearAllMessages();
			}
		</script>
<script src="${pageContext.request.contextPath}/customJS/license.js<%= "?v=" + Math.random() %>" type="text/javascript"></script>
</body>
</html>
