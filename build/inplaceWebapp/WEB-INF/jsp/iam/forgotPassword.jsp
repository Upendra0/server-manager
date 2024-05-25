<%@page import="java.util.Map"%>
<%@page import="com.elitecore.sm.iam.model.SecurityQuestions"%>
<%@page import="com.elitecore.sm.common.constants.BaseConstants"%>
<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>

<html>

<!-- topnavigation Start -->

<jsp:include page="../common/newheader.jsp"></jsp:include>

<!-- topnavigation End --> 

<script>
$(document).ready(function() {
	changeOption('${FORGOTPWDOPTION}');
});
function showHideButtonBasedOnTabsSelected(tabType){
	$("#divForgotPassword").hide();
	$("#divResetPassword").hide();
	
	if(tabType == 'FORGOT_PASSWORD'){
		$("#divForgotPassword").show();
	}else if(tabType == 'VERIFIED_USERNAME'){
		$("#divResetPassword").show();
	} else if(tabType = 'VERIFIED_LINK_SENT'){
		$("#divForgotPassword").show();
		$("#continue_btn").hide();
	}
}

</script>
   
<body class="skin-blue no-sidebar">
<div class="wrapper"> 

    <!-- topnavigation Start -->
    	<jsp:include page="../common/newtopNavigationPanel.jsp"></jsp:include> 
    <!-- topnavigation End -->
      
    <div class="content-wrapper"> 
     
        <section class="content-header">
           <div class="fullwidth">
             <div style="padding:0 10px 0 4px;">
        
                	<div id="content-scroll-d" class="content-scroll">
                		
                  <!-- Contains page Start -->
                      <div class="container forgot-password-page">
                        	<h5 class="box-title mtop40" id="pageTitle">
                        		<strong><spring:message code="forgotPassword.page.heading" ></spring:message></strong>&nbsp;                    	
                        	</h5>
                        	<jsp:include page="../common/responseMsg.jsp" ></jsp:include>
                        	
                        	<c:choose>
		        			<c:when test="${(REQUEST_ACTION_TYPE eq 'VERIFIED_DETAIL_FOR_FORGOT_PASSWORD')}">
		        				<script>
		        				$(document).ready(function() {
		        					showHideButtonBasedOnTabsSelected('VERIFIED_USERNAME');
		        					$("#pageTitle").html('<strong><spring:message code="resetPassword.page.heading" ></spring:message></strong>');
		        				});
		        				</script>
		        				<form role="form" class="form-horizontal" id="changePasswordForm" method="post" action="<%= ControllerConstants.RESET_PASSWORD_FOR_FORGOT_PASSWORD %>">
									<div id="changePassword-errorDiv">
										<c:set var="allErrors">
											<c:if test="${not empty NEW_PASSWORD_ERROR}">
												${NEW_PASSWORD_ERROR}<br>
											</c:if>
											<c:if test="${not empty CONFIRM_NEW_PASSWORD_ERROR}">
												${CONFIRM_NEW_PASSWORD_ERROR}<br>
											</c:if>
										</c:set>
										
									</div>
									  											        					
		        					<div class="box-border padding15 borderwidth">
                                		<div class="clearfix"></div>
                                		<div class="inline-form mtop10">
											<div class="col-md-6"> 
		                                   		<div class="form-group" id="divNewPass" style="margin-left: 0px;">
		                                   			<div class="table-cell-label"><spring:message code="resetPassword.new.password" ></spring:message><span class=required>*</span></div>
		                       	                	<div class="input-group">
			                           	            	<spring:message code="resetPassword.new.password" var="tooltip"></spring:message>
			                               	            <input type="password" data-toggle="tooltip" data-placement="bottom" class="form-control" id="newpassword_txt" name="newPassword" value="${NEW_PASSWORD}" title="${tooltip}" />
			                               	            <span class="input-group-addon add-on last" > <i class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="bottom" title="${NEW_PASSWORD_ERROR}"></i></span>
			                                   	    </div>
			                                   	 </div>
		                                   	</div>
			                                <div class="clearfix"></div>
		                                   	<div class="col-md-6">
		                                   		<div class="form-group" id="divNewConfPass" style="margin-left: 0px;">
		                                   			<div class="table-cell-label"><spring:message code="resetPassword.confirm.new.password" ></spring:message><span class=required>*</span></div>
		                       	                	<div class="input-group">
			                           	            	<spring:message code="resetPassword.confirm.new.password" var="tooltip"></spring:message>
			                               	            <input type="password" data-toggle="tooltip" data-placement="bottom" class="form-control" id="confirmnewpassword_txt" name="confirmNewPassword" value="${CONFIRM_NEW_PASSWORD}" title="${tooltip}" />
			                               	            <span class="input-group-addon add-on last" > <i class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="bottom" title="${CONFIRM_NEW_PASSWORD_ERROR}"></i></span>
			                                   	    </div>
			                                   	 </div>
		                                   	</div>
		                                   	<div class="clearfix"></div>

			           	                    <script type="text/javascript">
												function validateChangePasswordForm(){
													$("#changePasswordForm").submit();
												}
												
												$("#confirmNewPassword_txt").keypress(function(e) {
									            	// Enter pressed?
									            	if(e.which == 10 || e.which == 13) {
									            		validateChangePasswordForm();
									            	}
									        	});
											</script>
										</div>
									</div>
								</form>
		        			</c:when>
		        			
		        			<c:when test="${(REQUEST_ACTION_TYPE eq 'VERIFIED_LINK_SENT')}">
		        				<script>
		        				$(document).ready(function() {
		        					showHideButtonBasedOnTabsSelected('VERIFIED_LINK_SENT');
		        					$("#pageTitle").html('<strong><spring:message code="resetPassword.page.heading" ></spring:message></strong>');
		        				});
		        				</script>
		        			</c:when>
		        			<c:when test="${(REQUEST_ACTION_TYPE eq 'VERIFIED_LINK_EXPIRED')}">
										<div>
											<a id=forgotpassword_lnk
												href="<%=ControllerConstants.INIT_FORGOT_PASSWORD%>" class=""><spring:message code="index.page.forgot.password.link" htmlEscape="false" ></spring:message></a>
										</div>
										<script>
		        				$(document).ready(function() {
		        					showHideButtonBasedOnTabsSelected('VERIFIED_LINK_SENT');
		        					$("#pageTitle").html('<strong><spring:message code="resetPassword.page.heading" ></spring:message></strong>');
		        				});
		        				</script>
		        			</c:when>
		        			
		        			<c:when test="${(REQUEST_ACTION_TYPE eq 'VERIFIED_USERNAME_FOR_FORGOT_PASSWORD')}">
		        			
		        			<form role="form" id="forgotPasswordForm" method="post" action="<%= ControllerConstants.VERIFY_DETAILS_FOR_FORGOT_PASSWORD %>">
									<div id="forgotPassword-errorDiv">
										<c:set var="allErrors">
											<c:if test="${not empty VERIFICATION_ERROR}">
												${VERIFICATION_ERROR}<br>
											</c:if>

											<c:if test="${not empty QUESTION1_ERROR}">
												${QUESTION1_ERROR}<br>
											</c:if>
											
											<c:if test="${not empty ANSWER1_ERROR}">
												${ANSWER1_ERROR}<br>
											</c:if>
										</c:set>
									</div>
									<div class="box-border padding15 borderwidth">
                            			<p><strong><spring:message code="forgotPassword.verify.details" ></spring:message></strong></p>
                                		<div class="clearfix"></div>
                                		<div class="inline-form mtop10">
	                                		<div class="col-md-6"> 
	                                 			<div class="form-group" id="divUsername" style="margin-left: 0px;">
	                                 				<spring:message code="forgotPassword.username" var="tooltip" ></spring:message>
				                                    <div class="table-cell-label">${tooltip}</div>
				                                    <div class="input-group " >
				                                        <input type="text" class="form-control table-cell input-sm" id="username" name="username" value="${USERNAME}" data-toggle="tooltip" data-placement="bottom"  title="${tooltip}" readonly>
				                                        <span class="input-group-addon add-on last" > <i id="username_required_err" class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="bottom" title="${USERNAME_ERROR}${USERNAME_AND_EMAIL_ID_ERROR}"></i></span>
				                                    </div>
	                                			</div>
	                               			</div>
                               				<div class="clearfix"></div>
                                		<%-- 	<p><spring:message code="forgotPassword.or" ></spring:message></p>
                               				<div class="clearfix"></div> --%>
	                               		<%-- 	<div class="col-md-6">
	                               				<spring:message code="forgotPassword.emailId" var="tooltip"></spring:message> 
			                                 	<div class="form-group" id="divEmail" style="margin-left: 0px;">
			                                    	<div class="table-cell-label">${tooltip}</div>
			                                    	<div class="input-group">
			                                        	<input id="email_txt" type="email" class="form-control table-cell input-sm" data-toggle="tooltip" data-placement="bottom"  id="emailid_email" name="emailId" title="${tooltip}" value="${EMAIL_ID}" readonly >
			                                        	<span class="input-group-addon add-on last" > <i id="email_required_err" class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="bottom" title="${EMAIL_ID_ERROR}${USERNAME_AND_EMAIL_ID_ERROR}"></i></span>
			                                    	</div>
			                                	</div>
	                               			</div> --%>
	                               		</div>
                               			<div class="clearfix"></div>
                               			<c:if test="${QUESTION_LIST != null }">
                               			<div id="questionDetails">
                               			<p><strong><spring:message code="forgotPassword.security.questions" ></spring:message></strong></p>
                              	 		<div class="clearfix"></div>
                                		<div class="inline-form mtop10">
                                			<div class="col-md-6"> 
                                 				<div class="form-group" id="divSecQus" style="margin-left: 0px;">
                                 					<spring:message code="forgotPassword.select.security.question" var="tooltip"></spring:message>
                                    				<div class="table-cell-label">${tooltip}</div>
                                    				<div class="input-group ">
                                        				<select class="form-control table-cell input-sm" data-toggle="tooltip" data-placement="bottom" id="question1" name="question1" title="${tooltip}" value="${QUESTION1}">
                                        				<c:forEach items="${QUESTION_LIST}" var="question">
                                        				<option value='${question}'>${question}</option>
                                        				</c:forEach>
                                        				</select>
                                        				<span class="input-group-addon add-on last" > <i class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="bottom" title="${QUESTION1_ERROR}"></i></span>
                                    				</div>
                                				</div>
                               				</div>
                               				<div class="clearfix"></div>
			                               	<div class="col-md-6"> 
			                                	<div class="form-group" id="divSecQusAns" style="margin-left: 0px;">
			                                		<spring:message code="forgotPassword.answer1" var="tooltip" ></spring:message>
			                                    	<div class="table-cell-label">${tooltip}</div>
			                                    	<div class="input-group ">
			                                        	<input type="text" id="answer1" name="answer1" class="form-control table-cell input-sm" data-toggle="tooltip" data-placement="bottom"  title="${tooltip}" value="${ANSWER1}">
			                                        	<span class="input-group-addon add-on last" > <i id="answer1_err" class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="bottom" title="${ANSWER1_ERROR}"></i></span>
			                                    	</div>
			                                	</div>
			                               </div>
                           				</div>
                           				</div>
                           				</c:if>
		                           		<div class="clearfix"></div>
	                         		</div>
								</form>
		        			
		        			
		        			</c:when>
		        			
		        			<c:otherwise>
								<!--  new design -->
								
								<form role="form" id="forgotPasswordForm" method="post" action="<%= ControllerConstants.RESET_PASSWORD_LINK %>">
									<div id="forgotPassword-errorDiv">
										<c:set var="allErrors">
											<c:choose>
												<c:when test="${not empty USERNAME_AND_EMAIL_ID_ERROR}">
													${USERNAME_AND_EMAIL_ID_ERROR}<br>
												</c:when>
												<c:when test="${not empty USERNAME_ERROR}">
													${USERNAME_ERROR}<br>
												</c:when>
												<c:when test="${not empty EMAIL_ID_ERROR}">
													${EMAIL_ID_ERROR}<br>
												</c:when>
											</c:choose>
											<c:if test="${not empty VERIFICATION_ERROR}">
												${VERIFICATION_ERROR}<br>
											</c:if>

										</c:set>
										
									</div>
									<div class="box-border padding15 borderwidth">
                            			<p><strong><spring:message code="forgotPassword.verify.details" ></spring:message></strong></p>
                                		<div class="clearfix"></div>
                                		<div class="inline-form mtop10">
                                		 <input type="radio" style="display:none" name="forgotPWdOption" id="username_radio" style="padding-right: 15px;" value="username" onchange="resetWarningDisplay();changeOption('username');" checked="checked" />
                                		<div class="clearfix"></div>
	                                		<div class="col-md-6"> 
	                                 			<div class="form-group" id="divUsername" style="margin-left: 0px;">
	                                 				<spring:message code="forgotPassword.username" var="tooltip" ></spring:message>
				                                    <div class="table-cell-label">${tooltip}<span class=required>*</span></div> 
				                                    <div class="input-group ">
				                                        <input type="text" class="form-control table-cell input-sm" data-toggle="tooltip" data-placement="bottom" id="username_txt" name="username"  title="${tooltip}" value="${USERNAME}">
				                                        <span class="input-group-addon add-on last" > <i id="username_err" class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="bottom" title="${USERNAME_ERROR}${USERNAME_AND_EMAIL_ID_ERROR}"></i></span>
				                                    </div>
	                                			</div>
	                               			</div>
                               			<%-- 	<div class="clearfix"></div>
                                			<p><spring:message code="forgotPassword.or" ></spring:message></p>
                               				<div class="clearfix"></div>
	                               			<div class="col-md-6">
	                               				<spring:message code="forgotPassword.emailId" var="tooltip"></spring:message> 
			                                 	<div class="form-group" id="divEmail" style="margin-left: 0px;">
			                                    	<div class="table-cell-label">${tooltip}</div>
			                                    	<div class="input-group">
			                                        	<input id="email_txt" type="email" class="form-control table-cell input-sm" data-toggle="tooltip" data-placement="bottom"  id="emailId" name="emailId" title="${tooltip}" value="${EMAIL_ID}" readonly>
			                                        	<span class="input-group-addon add-on last" > <i id="email_err" class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="bottom" title="${EMAIL_ID_ERROR}${USERNAME_AND_EMAIL_ID_ERROR}"></i></span>
			                                    	</div>
			                                	</div>
	                               			</div> --%>
	                               		</div>
                               			<div class="clearfix"></div>
                               			<c:if test="${QUESTION_LIST != null }">
                               			<div id="questionDetails">
                               			<p><strong><spring:message code="forgotPassword.security.questions" ></spring:message></strong></p>
                              	 		<div class="clearfix"></div>
                                		<div class="inline-form mtop10">
                                			<div class="col-md-6"> 
                                 				<div class="form-group" id="divSecQus" style="margin-left: 0px;">
                                 					<spring:message code="forgotPassword.select.security.question" var="tooltip"></spring:message>
                                    				<div class="table-cell-label">${tooltip}</div>
                                    				<div class="input-group ">
                                        				<select class="form-control table-cell input-sm" data-toggle="tooltip" data-placement="bottom" id="question1_cbx" name="question1" title="${tooltip}" value="${QUESTION1}">
                                        				<c:forEach items="${QUESTION_LIST}" var="question">
                                        				<option value='${question}'>${question}</option>
                                        				</c:forEach>
                                        				</select>
                                        				<span class="input-group-addon add-on last" > <i class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="bottom" title="${QUESTION1_ERROR}"></i></span>
                                    				</div>
                                				</div>
                               				</div>
                               				<div class="clearfix"></div>
			                               	<div class="col-md-6"> 
			                                	<div class="form-group" id="divSecQusAns" style="margin-left: 0px;">
			                                		<spring:message code="forgotPassword.answer1" var="tooltip" ></spring:message>
			                                    	<div class="table-cell-label">${tooltip}</div>
			                                    	<div class="input-group ">
			                                        	<input type="text" id="answer1_txt" name="answer1" class="form-control table-cell input-sm" data-toggle="tooltip" data-placement="bottom"  title="${tooltip}" value="${ANSWER1}">
			                                        	<span class="input-group-addon add-on last" > <i class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="bottom" title="${ANSWER1_ERROR}"></i></span>
			                                    	</div>
			                                	</div>
			                               </div>
                           				</div>
                           				</div>
                           				</c:if>
		                           		<div class="clearfix"></div>
	                         		</div>
								</form>
								<!-- new design end -->
							</c:otherwise>
		        			</c:choose>
                      </div>
                  <!-- Contains page End -->
                  
                  </div>
          
          	</div>
          </div>
        </section>
      </div>

      <!-- Footer Start -->
		<footer class="main-footer positfix">
	    	<div class="fooinn">
	        	<div class="fullwidth">
	        		<div class="button-set">
		        		<div class="padleft-right" id="divForgotPassword" style="display: block;">
		        			<c:if test="${empty ERROR_MSG}" >
		            		<a id="continue_btn" href="#" onclick="validateForgotPasswordForm()" class="btn btn-grey btn-xs " style="text-decoration:none;"><spring:message code="btn.label.continue" ></spring:message></a>
		            		</c:if>
	                        <a id="cancel_btn" href="welcome" class="btn btn-grey btn-xs " style="text-decoration:none;"><spring:message code="btn.label.cancel" ></spring:message></a>
						</div>	
		            	<div class="padleft-right" id="divResetPassword" style="display: none;">
		            		<a id="continue_btn" href="#" onclick="validateChangePasswordForm()" class="btn btn-grey btn-xs " style="text-decoration:none;"><spring:message code="btn.label.continue" ></spring:message></a>
						</div>	
					</div>
	        	</div>
	        	
	     		<jsp:include page="../common/newfooter.jsp"></jsp:include>
	     	</div>
		</footer>
	    <!-- Footer End -->	

    </div>

	 <!-- REQUIRED JS SCRIPTS -->
	
	<!-- jQuery 3.5.1 -->
	<script src="jquery/3.5.1/jquery.min.js"></script>
	<script src="js/jquery-migrate-3.4.0.min.js"></script> 
	<!-- Bootstrap 3.3.2 JS --> 
	<script src="js/bootstrap.min.js" type="text/javascript"></script>
	
	<!-- AdminLTE App --> 
	<script src="js/app.min.js" type="text/javascript"></script> 
<!-- 	<script src="js/jquery.mCustomScrollbar.concat.min.js"></script>  -->


<script type="text/javascript">
		
	function validateForgotPasswordForm(){
		$("#forgotPasswordForm").submit();
	}
	
	$("#username_txt").keypress(function(e) {
          	// Enter pressed?
          	if(e.which == 10 || e.which == 13) {
          		validateForgotPasswordForm();
          	}
      	});
	
	$("#emailid_email").keypress(function(e) {
          	// Enter pressed?
          	if(e.which == 10 || e.which == 13) {
          		validateForgotPasswordForm();
          	}
      	});
	
	$("#question1_cbx").keypress(function(e) {
          	// Enter pressed?
          	if(e.which == 10 || e.which == 13) {
          		validateForgotPasswordForm();
          	}
      	});
	
	$("#answer1_txt").keypress(function(e) {
          	// Enter pressed?
          	if(e.which == 10 || e.which == 13) {
          		validateForgotPasswordForm();
          	}
      	});
	
	$("#newpassword_txt").keypress(function(e) {
          	// Enter pressed?
          	if(e.which == 10 || e.which == 13) {
          		validateChangePasswordForm();
          	}
      	});
	
	$("#confirmNewPassword_txt").keypress(function(e) {
          	// Enter pressed?
          	if(e.which == 10 || e.which == 13) {
          		validateChangePasswordForm();
          	}
      	});
	
	$(document).ready(function() {
		
		$('i').each(function(){
			if($(this).attr('data-original-title') != '')
			{
				$(this).parent().parent().parent().addClass('error');
			}
   		 });
	});
	
	function changeOption(value){
		
		if(value == 'username'){
			$("#username_radio").prop('checked',true);
			$("#username").prop('readonly', false);
			$("#emailId").val('');
			$("#emailId").prop('readonly', true);
		}else if (value== 'email'){
			$("#username").val('');
			$("#email_radio").prop('checked',true);
			$("#emailId").prop('readonly', false);
			$("#username").prop('readonly', true);
		}
		
	}
			 			 
</script>
  </body>
</html>
