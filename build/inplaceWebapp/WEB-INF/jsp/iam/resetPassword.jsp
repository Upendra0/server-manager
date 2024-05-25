<%@page import="java.util.Map"%>
<%@page import="com.elitecore.sm.iam.model.SecurityQuestions"%>
<%@page import="com.elitecore.sm.common.constants.BaseConstants"%>
<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page import="com.elitecore.sm.util.EliteUtils"%>

<!DOCTYPE html>

<html>

<!-- topnavigation Start -->

<jsp:include page="../common/newheader.jsp"></jsp:include>


<style>
.form-horizontal .form-group
{
	margin-left: 0px;
}

/* .wrapper .main-header nav{ */
/* 	margin-left: 0 !important; */
/* } */
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
      
    	<sec:authorize access="hasAnyAuthority('RESET_PASSWORD_MENU_VIEW')">
    	
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
	                            	<strong>
	                            		<a data-toggle="dropdown" href="#">
					                    	<spring:message code="resetPassword.page.heading" ></spring:message>
					                    </a></strong>
	                            </span>
	                            <div style="display:inline-flex;width=calc();color: #FE7B26;font-weight:bolder;">
			                 		<spring:message code="resetPassword.page.heading.message" ></spring:message>
			                 	</div>
	                       	</h4>
	                     
                      		<div class="col-md-12" id="resetPassword-errorDiv">
                     			<jsp:include page="../common/responseMsg.jsp" ></jsp:include> 
								<div class="row">
	                        		<div class="col-xs-12">
	                        			<div class="box  martop" style="border:none; box-shadow: none;">
	                        				<div class="fullwidth">
												<form role="form" class="form-horizontal" id="changePasswordForm" method="post" action="<%= ControllerConstants.RESET_PASSWORD %>">
													<div class="col-md-12" id="changePassword-errorDiv">
														<c:set var="allErrors">
															<c:if test="${not empty NEW_PASSWORD_ERROR}">
																${NEW_PASSWORD_ERROR}<br>
															</c:if>
															<c:if test="${not empty CONFIRM_NEW_PASSWORD_ERROR}">
																${CONFIRM_NEW_PASSWORD_ERROR}<br>
															</c:if>
														</c:set>
													</div>
		                                			<div class="box box-warning">
		                           						<div class="box-header with-border">
		                             						<h3 class="box-title"><spring:message code="resetPassword.generate.password" ></spring:message></h3>
			                             					<div class="box-tools pull-right" >
			                              						 <button class="btn btn-box-tool" data-widget="collapse"><i class="fa fa-minus"></i></button>
			                             					</div><!-- /.box-tools -->
		                           						</div><!-- /.box-header -->
		                           				 		<div class="box-body">
		                            						<div class="col-md-6 inline-form" style="padding:0;">
				                            					<div class="form-group">
												                	<spring:message code="resetPassword.new.password" var="tooltip"></spring:message>
													            	<div class="table-cell-label">${tooltip}<span class="required">*</span></div>
													                <div class="input-group ">
													                	<input type="password" class="form-control table-cell input-sm" id="newPassword" name="newPassword" value="${NEW_PASSWORD}" title="${tooltip}"  data-toggle="tooltip" data-placement="bottom"/>
													                	<span class="input-group-addon add-on last"><i class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="bottom" title="${NEW_PASSWORD_ERROR}"></i></span>
													                </div>
													            </div>
													            <div class="form-group">
												                	<spring:message code="resetPassword.confirm.new.password" var="tooltip"></spring:message>
													            	<div class="table-cell-label">${tooltip}<span class="required">*</span></div>
													                <div class="input-group ">
													                	<input type="password" class="form-control" id="confirmNewPassword" name="confirmNewPassword" value="${CONFIRM_NEW_PASSWORD}" title="${tooltip}"  data-toggle="tooltip" data-placement="bottom" />
													                	<span class="input-group-addon add-on last"><i class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="bottom" title="${CONFIRM_NEW_PASSWORD_ERROR}"></i></span>
													                </div>
													            </div>
		                          							</div>
		                          							
		                            					</div><!-- /.box-body -->
		                            					<br/>
		                            				</div>
						                         </form>
						                     </div>
						                 </div>			
									</div>
								</div>
							</div>
                        </div>
                    </div>
	                <!-- Content Wrapper. Contains page content End -->
	                  
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
       				<sec:authorize access="hasAnyAuthority('RESET_PASSWORD')">
       					<button id="continue_btn" class="btn btn-grey btn-xs " type="button" onclick="validateChangePasswordForm()"> 
 				          <spring:message code="btn.label.continue" ></spring:message> 
 				    </button>
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

			function validateChangePasswordForm(){
				$("#changePasswordForm").submit();
			}
			
			$("#confirmNewPassword").keypress(function(e) {
            	// Enter pressed?
            	if(e.which == 10 || e.which == 13) {
            		validateChangePasswordForm();
            	}
        	});
			
	</script>

  </body>
</html>
