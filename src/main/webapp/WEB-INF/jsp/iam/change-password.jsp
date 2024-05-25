<%@page import="java.util.Map"%>
<%@page import="com.elitecore.sm.iam.model.SecurityQuestions"%>
<%@page import="com.elitecore.sm.common.constants.BaseConstants"%>
<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page import="com.elitecore.sm.util.EliteUtils"%>
<!DOCTYPE html>

<html>

<!-- topnavigation Start -->

<jsp:include page="../common/newheader.jsp"></jsp:include>


<style>
/* .form-horizontal .form-group */
/* { */
/* 	margin-left: 0px; */
/* } */

 .wrapper .main-header nav{ 
 	margin-left: 0 !important; 
 } 
</style>
 
<body class="skin-blue sidebar-mini sidebar-collapse">
	<div class="wrapper"> 

    	<!-- Header Start -->
     
     	 <jsp:include page="../common/newtopNavigationPanel.jsp"></jsp:include>
     	 
    	<!-- Header End -->
         
    	<!-- sidebar Start -->
    	<c:choose>
    		<c:when test="${sessionScope.IS_LOGIN_FIRST_TIME == 'true'}">
    			
    		</c:when>
			<c:otherwise>
    			<jsp:include page="../common/newleftMenu.jsp"></jsp:include>
    		</c:otherwise>
		</c:choose>
            
    	<!-- sidebar End -->
      
    <sec:authorize access="hasAnyAuthority('CHANGE_PASSWORD_MENU_VIEW')">
	   	<div class="content-wrapper"> 
	        <section class="content-header">
	           <div class="fullwidth">
	             <div style="padding:0 10px 0 4px;">
	        
	                <div id="content-scroll-d" class="content-scroll">
	                 
	                  <!-- Content Wrapper. Contains page content Start -->
	                 	
	                     <div class="fullwidth">
	                   	   	<h4 style="margin-top:10px; margin-bottom:0px; font-size:11px;">
	                            <img src="img/tile-icon.png" class="vmiddle" alt="Title Icon"/> 
	                            <span class="spanBreadCrumb" style="line-height: 30px;">
	                            	<a href="home"><i class="icon-home"></i></a>&nbsp;
	                            	<strong><spring:message code="changePassword.page.heading" ></spring:message></strong>
	                            </span>
	                       	</h4>
	                     
	                     	<form role="form" id="changePasswordForm" method="post" action="<%= ControllerConstants.CHANGE_PASSWORD %>">
	                      		<div class="col-md-12" id="changePassword-errorDiv">
	                      			<jsp:include page="../common/responseMsg.jsp" ></jsp:include> 
									<c:set var="allErrors">
										<c:if test="${not empty OLD_PASSWORD_ERROR}">
											${OLD_PASSWORD_ERROR}
										</c:if>
										<c:if test="${not empty NEW_PASSWORD_ERROR}">
											${NEW_PASSWORD_ERROR}<br>
										</c:if>
										<c:if test="${not empty CONFIRM_NEW_PASSWORD_ERROR}">
											${CONFIRM_NEW_PASSWORD_ERROR}<br>
										</c:if>
										
										<c:if test="${not empty QUESTION1_ERROR}">
											${QUESTION1_ERROR}<br>
										</c:if>
										
										<c:if test="${not empty ANSWER1_ERROR}">
											${ANSWER1_ERROR}<br>
										</c:if>
										
										<c:if test="${not empty QUESTION2_ERROR}">
											${QUESTION2_ERROR}<br>
										</c:if>
										
										<c:if test="${not empty ANSWER2_ERROR}">
											${ANSWER2_ERROR}
										</c:if>
									</c:set>
								</div>
	                      		<div class="row">
	                        		<div class="col-xs-12">
	                          			<div class="box  martop" style="border:none; box-shadow: none;">
	                            			<div class="fullwidth">
	                                			<div class="box box-warning">
	<%--                                 			<form role="form" class="form-horizontal" id="changePasswordForm" method="post" action="<%= ControllerConstants.CHANGE_PASSWORD %>"> --%>
								
	                           						<div class="box-header with-border">
	                             						<h3 class="box-title"><spring:message code="changePassword.generate.password" ></spring:message></h3>
		                             					<div class="box-tools pull-right" >
		                              						 <button class="btn btn-box-tool" data-widget="collapse"><i class="fa fa-minus"></i></button>
		                             					</div><!-- /.box-tools -->
	                           						</div><!-- /.box-header -->
	                           				 		<div class="box-body">
	                            						<div class="col-md-6 inline-form" style="padding:0;">
	                            							<spring:message code="changePassword.old.password" var="tooltip"></spring:message>
			                            					<div class="form-group" id="divOldPass">
			                                					<div class="table-cell-label">${tooltip}<span class="required">*</span></div>
			                               						<div class="input-group ">
			                               							<input type="password" class="form-control table-cell input-sm" id="oldpassword_txt" tabindex="1" name="oldPassword" autofocus  data-toggle="tooltip" data-placement="bottom" title="${tooltip}">
			                                       					<span class="input-group-addon add-on last"><i id="old_pwd_err" class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="bottom" title="${OLD_PASSWORD_ERROR}"></i></span>
			                               						</div>
			                            					</div>
								                            <div class="form-group" id="divNewPass">
								                            	<spring:message code="changePassword.new.password" var="tooltip"></spring:message>
								                            	<div class="table-cell-label">${tooltip}<span class="required">*</span></div>	
								                                <div class="input-group ">
								                                    <input type="password" class="form-control table-cell input-sm"  tabindex="2" id="newpassword_txt" name="newPassword" data-toggle="tooltip" data-placement="bottom" title="${tooltip}">
								                                    <span class="input-group-addon add-on last" > <i id="new_pwd_err" class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="bottom" title="${NEW_PASSWORD_ERROR}"></i></span>
								                                </div>
								                            </div>
								                            <div class="form-group" id="divNewConfirmPass">
								                            	<spring:message code="changePassword.confirm.new.password" var="tooltip"></spring:message>
								                            	<div class="table-cell-label">${tooltip}<span class="required">*</span></div>
								                                <div class="input-group ">
								                                    <input type="password" class="form-control table-cell input-sm"  tabindex="3" id="confirmnewpassword_txt" name="confirmNewPassword" data-toggle="tooltip" data-placement="bottom" title="${tooltip}">
								                                    <span class="input-group-addon add-on last" > <i id="new_pwd_confirm_err" class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="bottom" title="${CONFIRM_NEW_PASSWORD_ERROR}"></i></span>
								                                </div>
								                            </div>
	                            
						                           			<c:if test="${sessionScope.IS_LOGIN_FIRST_TIME == 'true'}">
								                               	<h4 class="section-title preface-title">
								                               		<spring:message code="changePassword.security.questions" ></spring:message>
								                               	</h4>
						                               			<div class="form-group" id="divSecQue1">
						                               				<spring:message code="question1.is.missing" var="tooltip"></spring:message>
						                            				<div class="table-cell-label">Question 1</div>
								                                	<div class="input-group ">
								                                    	<select id="question1_cbx" name="question1" class="form-control table-cell input-sm" tabindex="4" data-toggle="tooltip" data-placement="bottom" title="${tooltip}">
											                               	<%
											                               		Map<String,String> questionList = SecurityQuestions.getQuestions();
											                               		for(String question : questionList.keySet()){
											                               	%>
																			<option value="<%= questionList.get(question) %>" <%= request.getAttribute("QUESTION1")!=null?request.getAttribute("QUESTION1").equals(questionList.get(question))?"selected":"":"" %> >
																				<%= questionList.get(question) %>
																			</option>
											                               	<%
											                               		}
											                               	%>
								                               			</select>
								                                    	<span class="input-group-addon add-on last" > <i id="ques1_err" class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="bottom" title="${QUESTION1_ERROR}"></i></span>
								                                	</div>
						                               			</div>
								                               	<div class="form-group" id="divSecAns1">
								                               		<spring:message code="changePassword.answer1" var="tooltip"></spring:message>
								                               		<div class="table-cell-label">Answer 1<span class="required">*</span></div>
								                                	<div class="input-group ">
								                                    	<input type="text"  class="form-control table-cell input-sm" id="answer1_txt" tabindex="5" name="answer1" data-toggle="tooltip" data-placement="bottom" title="${tooltip}" value="${ANSWER1}" />
						                         	            		<span class="input-group-addon add-on last" > <i id="ans1_err" class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="bottom" title="${ANSWER1_ERROR}"></i></span>
								                                	</div>
								                               	</div>
								                               	<div class="form-group" id="divSecQue2">
								                               		<spring:message code="question1.is.missing" var="tooltip"></spring:message>
								                            		<div class="table-cell-label">Question 2</div>
								                                	<div class="input-group ">
								                                    	<select id="question2_cbx" name="question2" class="form-control table-cell input-sm" data-placement="bottom" tabindex="6" data-toggle="tooltip" title="${tooltip}">
											                               	<%
											                               		for(String question : questionList.keySet()){
											                               	%>
																			<option value="<%= questionList.get(question) %>" <%= request.getAttribute("QUESTION2")!=null?request.getAttribute("QUESTION2").equals(questionList.get(question))?"selected":"":"" %> >
																				<%= questionList.get(question) %>
																			</option>
											                               	<%
											                               		}
											                               	%>
								                              			</select>
								                                    	<span class="input-group-addon add-on last" > <i id="ques2_err" class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="bottom" title="${QUESTION2_ERROR}"></i></span>
								                                	</div>
								                              	</div>
								                               	<div class="form-group" id="divSecAns2">
								                               		<spring:message code="changePassword.answer2" var="tooltip"></spring:message>
								                               		<div class="table-cell-label">Answer 2<span class="required">*</span></div>
								                                	<div class="input-group ">
								                                    	<input type="text"  data-placement="bottom" data-toggle="tooltip" tabindex="7" class="form-control table-cell input-sm" id="answer2_txt" name="answer2" title="${tooltip}" value="${ANSWER2}" />
						                         	            		<span class="input-group-addon add-on last" > <i id="ans2_err" class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="bottom" title="${ANSWER2_ERROR}"></i></span>
								                                	</div>
								                               	</div>
								                               	
								                               	<div class="fullwidth">
													            	<div id="staffDetailDiv">
												       				<sec:authorize access="hasAnyAuthority('CHANGE_PASSWORD')">
												             		<c:choose>
																		<c:when test="${sessionScope.IS_LOGIN_FIRST_TIME == 'true'}">
												                     		<div class="col-lg-12">
												                       	       <label><spring:message code="changePassword.security.question.note" ></spring:message>
												                       	         </label>
												                            </div>
																		</c:when>
																	</c:choose>
												          			</sec:authorize>
												                	</div>
													            </div>
															</c:if>
	                        							
	                        							<br/>
	                          							</div>
	                          							
	                            					</div><!-- /.box-body -->
	                            				</div>
	                          				</div><!-- /.box -->
	                                  </div>
	                        		</div>
	                      		</div>
	                  		</form>
	                  <!-- Content Wrapper. Contains page content End -->
	                  
	                  	</div>
	          		</div>
	          	</div>
	          </div>
	        </section>
	      </div>
    </sec:authorize>

     <!-- Footer Start -->
	 <footer class="main-footer positfix">
    	<div class="fooinn">
        	<div class="fullwidth">
        		<div class="button-set">
	            	<div class="padleft-right">
       				<sec:authorize access="hasAnyAuthority('CHANGE_PASSWORD')">
             		<c:choose>
						<c:when test="${sessionScope.IS_LOGIN_FIRST_TIME == 'true'}">
               	            <button class="btn btn-grey btn-xs " id = "continue_btn" tabindex="8" onclick="validateChangePasswordForm();">
               	            	 <spring:message code="btn.label.continue" ></spring:message>
               	            </button>
						</c:when>
						<c:otherwise>
                 	            <button class="btn btn-grey btn-xs " id = "update_btn" tabindex="8" onclick="validateChangePasswordForm()"><spring:message code="btn.label.update" ></spring:message></button>
                 	            <button class="btn btn-grey btn-xs " id = "reset_btn" tabindex="9" onclick="resetChangePasswordForm()"><spring:message code="btn.label.reset" ></spring:message></button>
                 	            <a class="btn btn-grey btn-xs " id="cancel_btn" style="text-decoration:none;" tabindex="10" href="home"><spring:message code="btn.label.cancel" ></spring:message></a>
						</c:otherwise>
					</c:choose>
          			</sec:authorize>
                	</div>
                </div>
        	</div>
     		<jsp:include page="../common/newfooter.jsp"></jsp:include>
     	</div>
	</footer>
    <!-- Footer End -->	 

    </div>

<script type="text/javascript">
			function resetChangePasswordForm(){
				//clearResponseMsgDiv();
				$("#changePassword-errorDiv").html('');
				$("#oldpassword_txt").val('');
				$("#divOldPass").removeClass('error');
				$("#newpassword_txt").val('');
				$("#divNewPass").removeClass('error');
				$("#confirmnewpassword_txt").val('');
				$("#divNewConfirmPass").removeClass('error');
			}

			function validateChangePasswordForm(){
				$("#changePasswordForm").submit();
			}
			
			$("#confirmnewpassword_txt").keypress(function(e) {
            	// Enter pressed?
            	if(e.which == 10 || e.which == 13) {
            		validateChangePasswordForm();
            	}
        	});
			
			$("#newpassword_txt").keypress(function(e) {
            	// Enter pressed?
            	if(e.which == 10 || e.which == 13) {
            		validateChangePasswordForm();
            	}
        	});
			
			$("#oldpassword_txt").keypress(function(e) {
            	// Enter pressed?
            	if(e.which == 10 || e.which == 13) {
            		validateChangePasswordForm();
            	}
        	});
			
			$("#answer1_txt").keypress(function(e) {
            	// Enter pressed?
            	if(e.which == 10 || e.which == 13) {
            		validateChangePasswordForm();
            	}
        	});
			
			$("#answer2_txt").keypress(function(e) {
            	// Enter pressed?
            	if(e.which == 10 || e.which == 13) {
            		validateChangePasswordForm();
            	}
        	});
		</script>

  </body>
</html>
