 <%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="../common/newheader.jsp"></jsp:include>
</head>

<body class="skin-blue no-sidebar">
	<div class="wrapper">

		<!-- Header Start -->
		<jsp:include page="licenseHeader.jsp"></jsp:include>
		<!-- Header End -->
		<!-- Content Wrapper. Contains page content -->
		<div class="content-wrapper" style="margin-left: 0;">
			<!-- Content Header (Page header) -->
			<section class="content-header">
				<div class="fullwidth">
					<div style="padding: 0 10px 0 10px;">
						<div 	style="padding-bottom: 0;">
							<div style="width: 100%;">
								<div class="fullwidth">
										<div style="float:left">
										 <h4 style="margin-bottom: 10px">
											<span style="line-height: 30px;"><spring:message code="license.agreement.main.header.label" ></spring:message></span> 
										</h4></div>
										<div style="float:left; margin-left:2%;margin-top:1%;"><jsp:include page="../common/responseMsg.jsp" ></jsp:include></div>
								</div>		
								<div class="clearfix"></div>						
								<p><spring:message code="license.agreement.content.top.heading.text1" ></spring:message></p>
								<div class="row">
									<div class="col-xs-12">
										<div class="box  martop" style="border: none; box-shadow: none;" >
											<div class="fullwidth" >
												<div class="box box-warning" style="overflow:auto;height:350px;">
													<!-- /.box-header -->
													<div class="box-body"  style="text-align: justify;">
														<div ><spring:message code="license.agreement.content.html" ></spring:message></div>
													</div>
													<!-- /.box-body -->
												</div>
											</div>
											<div class="fullwidth"></div>
										</div>
										<!-- /.box -->
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
				<div class="padleft-right mtop15">
				 <p>
					<strong><spring:message code="license.agreement.accept.notification.info" ></spring:message></strong>
				</p>
				</div>
			</section>
		</div>
		<!-- /.content-wrapper -->
		<!-- Main Footer -->

		<!-- Footer Start -->
		<footer class="main-footer positfix">
			<div class="fooinn" >
				<div class="fullwidth">
					<div class="padleft-right text-right" style="padding-right: 24px;">
						<a href="#" onclick="redirectToActivation();" style="text-decoration:none;">
						<button class="btn btn-grey btn-xs mleft10 " type="button">
							<spring:message code="btn.label.accept" ></spring:message> 
						</button>
						</a>
						<a style="text-decoration:none;">
						 	<button class="btn btn-grey btn-xs " type="button" onclick="showAnErrorMsg()" >
								<spring:message code="btn.label.decline" ></spring:message> 
							</button>
						</a>
					</div>
				</div>
				<jsp:include page="../common/newfooter.jsp"></jsp:include>
			</div>
		</footer>
		<!-- Footer End -->
	</div>
	<!-- ./wrapper -->
	
	<div style="display: none;"> 
		<form id="license_activation_action" action="<%=ControllerConstants.LICENSE_ACTIVATION_REDIRECT%>" method="POST">
		  	<input type="hidden" name="disableTrialButton" id="disableTrialButton" value="${disableTrialButton}">
		</form>
	</div>
 <script type="text/javascript">
 	
 	function redirectToActivation(){
 		$("#license_activation_action").submit();
 	}
	function showAnErrorMsg(){ 
		showErrorMsg("<spring:message code='license.apply.proper.msg' ></spring:message>");
	}
</script> 

						
</body>
</html>
