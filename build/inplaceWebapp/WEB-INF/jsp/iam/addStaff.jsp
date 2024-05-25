<%@page import="com.elitecore.sm.util.MapCache"%>
<%@page import="com.elitecore.sm.common.constants.SystemParametersConstant"%>
<%@page import="com.elitecore.sm.common.constants.BaseConstants"%>
<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="/WEB-INF/ShowErrorTag.tld" prefix="elitecoreError" %>
<!DOCTYPE html>

<html>

<!-- topnavigation Start -->

<style>
	.required
	{
		position:relative;
	}
</style>

<jsp:include page="../common/newheader.jsp"></jsp:include>

<body class="skin-blue sidebar-mini">
<script>
$(document).ready(function() {
	//alert("Hello");
$("#staff-password").val('');
$("#staff-confirmPassword").val('');
})
</script>
    <div class="wrapper">

    <!-- Header Start -->
     
    <jsp:include page="../common/newtopNavigationPanel.jsp"></jsp:include>
     	 
    <!-- Header End -->
         
      <jsp:include page="../common/newleftMenu.jsp"></jsp:include>
        
    <!-- sidebar End -->

    <!-- Content Wrapper. Contains page content -->
     <form:form modelAttribute="staff_form_bean" method="POST" action="<%= ControllerConstants.ADD_STAFF %>" id="staff-form">
    <div class="content-wrapper">
   
        <!-- Content Header (Page header) -->
        <section class="content-header">
        	<sec:authorize access="hasAnyAuthority('ADD_STAFF') or hasAnyAuthority('EDIT_STAFF')">
	        <div class="fullwidth">
        		<div style="padding:0 10px 0 4px;">
        			<div id="content-scroll-d" class="content-scroll">
         
         				<!-- Content Wrapper. Contains page content Start -->
         			       <div class="fullwidth">
	          				<h4 style="margin-top:10px; margin-bottom:0px; font-size:11px;">
	                            <img src="img/tile-icon.png" class="vmiddle" alt="Title Icon"/> 
	                            <span class="spanBreadCrumb" style="line-height: 30px;font-weight: bold;">
	                            	<a href="javascript:;" onclick="loadBredthcrumLinks('<%=ControllerConstants.STAFF_MANAGER%>','GET','STAFF_MANAGEMENT');">
				                    	<spring:message code="staffManager.page.heading" ></spring:message>
				                    </a>&nbsp;/&nbsp;
			                    	<a href="javascript:;" onclick="loadBredthcrumLinks('<%=ControllerConstants.STAFF_MANAGER%>','GET','<%= BaseConstants.ADD_STAFF %>');">
				                    	<spring:message code="staffManager.staff.mgmt.tab" ></spring:message>
				                    </a>
	                            </span>
	                        </h4>
        					<jsp:include page="../common/responseMsg.jsp" ></jsp:include>
          					<div class="row">
          						
            					<div class="col-xs-12">
              						<div class="box  martop" style="border:none; box-shadow: none;">
                						<div class="box-body table-responsive no-padding">
                							
                							<div class="nav-tabs-custom">
                
								                <ul class="nav nav-tabs pull-right">
								                  <li class="active"><a data-toggle="tab" id="add-staff-tab-a" href="#revenue-chart"><spring:message code="addStaff.add.staff.page.heading" ></spring:message></a></li>
								                  <li class="disabled" id="assign-access-group-tab-a"><a href="#"><spring:message code="addStaff.assign.access.group" ></spring:message></a></li>
								                  <li class="disabled" id="change-profile-pic-a"><a href="#"><spring:message code="addStaff.change.profile.pic.tab.heading" ></spring:message></a></li>
								                </ul>
                
                								<div style="padding:10px 10px 10px 5px;">
                									<input type="hidden" id="sizeLimit" value="Y"/>
                									
													<form:input type="hidden" path="createdByStaffId"  id="staff-createdByStaffId" ></form:input>
													<form:input type="hidden" path="lastUpdatedByStaffId"  id="staff-lastUpdatedByStaffId" ></form:input>
													<form:input type="hidden" path="createdDate"  id="staff-createdDate" ></form:input>
													<form:input type="hidden" path="lastUpdatedDate"  id="staff-lastUpdatedDate" ></form:input>
													<form:input type="hidden" path="wrongAttempts"  id="staff-wrongAttempts" ></form:input>
													<form:input type="hidden" path="accountState"  id="staff-accountState" ></form:input>
													<c:set var="aglIndex" scope="request" value="0"></c:set>
													<c:forEach items="${staff_form_bean.accessGroupList}" var="accessGroupList">
											 			<input type="hidden" id="accessGroupList[${aglIndex}].id" name="accessGroupList[${aglIndex}].id" value="${accessGroupList.id}" />
														<c:set var="aglIndex" scope="request" value="${aglIndex + 1}"></c:set>
													</c:forEach>
                
													<div class="fullwidth">
														<div class="box box-warning">
											                <div class="box-header with-border">
												                  <h3 class="box-title"><spring:message code="addStaff.login.details" ></spring:message></h3>
												                  <div class="box-tools pull-right">
												                    <button class="btn btn-box-tool" data-widget="collapse"><i class="fa fa-minus"></i></button>
												                  </div><!-- /.box-tools -->
											                </div><!-- /.box-header -->
                											<div class="box-body inline-form">
																<div class="col-md-6">
													            	<div class="form-group">
													            		<spring:message code="addStaff.username" var="tooltip"></spring:message>
														            	<div class="table-cell-label">${tooltip}<span class="required">*</span></div>
														                <div class="input-group">
														                	<form:input path="username" cssClass="form-control table-cell input-sm" tabindex="1" id="staff-username" onkeypress="return validateKey(event)" data-toggle="tooltip" data-placement="bottom" title="${tooltip}" ></form:input>
														                	<spring:bind path="username">
									   											<elitecoreError:showError errorMessage="${status.errorMessage}" errorId="staff-username_error"></elitecoreError:showError>
									   										</spring:bind> 
													                	</div>
													                </div>
													            </div>
													            <div class="col-md-6">
														            <div class="form-group">
													                	<spring:message code="addStaff.password" var="tooltip" ></spring:message>
														            	<div class="table-cell-label">${tooltip}<span class="required">*</span></div>
														                <div class="input-group ">
														                	<form:input type="password" path="password" cssClass="form-control table-cell input-sm" tabindex="2" id="staff-password" title="${tooltip}" onkeypress="return validateKey(event)" data-toggle="tooltip" data-placement="bottom"  ></form:input>
																			<spring:bind path="password">
									   											<elitecoreError:showError errorMessage="${status.errorMessage}" errorId="staff-password_error"></elitecoreError:showError>
									   										</spring:bind>
														                </div>
														            </div>
            													</div>
													            <div class="col-md-6">   
													                <div class="form-group">
													                	<spring:message code="addStaff.confirm.password" var="tooltip"></spring:message>
														            	<div class="table-cell-label">${tooltip}<span class="required">*</span></div>
														                <div class="input-group ">
														                	<form:input type="password" path="confirmPassword" cssClass="form-control table-cell input-sm" tabindex="3" id="staff-confirmPassword" title="${tooltip}" onkeypress="return validateKey(event)"  data-toggle="tooltip" data-placement="bottom"></form:input>
														                	<spring:bind path="confirmPassword">
									   											<elitecoreError:showError errorMessage="${status.errorMessage}" errorId="staff-confirmPassword_error"></elitecoreError:showError>
									   										</spring:bind>
														                </div>
														            </div>
            													</div>
            													
               												</div><!-- /.box-body -->
              											</div>
													</div>
                
                									<div class="fullwidth">
														<div class="box box-warning">
                											<div class="box-header with-border">
												                  <h3 class="box-title"><spring:message code="staff.personal.detail"></spring:message></h3>
												                  <div class="box-tools pull-right">
												                    <button class="btn btn-box-tool" data-widget="collapse"><i class="fa fa-minus"></i></button>
												                  </div><!-- /.box-tools -->
											                </div><!-- /.box-header -->
                											<div class="box-body inline-form">
               	 												 
												            	<div class="col-md-6">
												            		<spring:message code="addStaff.first.name" var="tooltip"></spring:message>
												               		<div class="form-group">
												            		<div class="table-cell-label">${tooltip}<span class="required">*</span></div>
														                <div class="input-group ">
														                	<form:input path="firstName" cssClass="form-control table-cell input-sm" tabindex="4" title="${tooltip}" id="staff-firstName" onkeypress="return validateKey(event)"  data-toggle="tooltip" data-placement="bottom"></form:input>
														                	<spring:bind path="firstName">
									   											<elitecoreError:showError errorMessage="${status.errorMessage}" errorId = "staff-firstName_error"></elitecoreError:showError>
									   										</spring:bind>
														                </div>
														            </div>
												                </div>
												                <div class="col-md-6">
												                	<spring:message code="addStaff.last.name" var="tooltip"></spring:message>
												                	<div class="form-group">
													            		<div class="table-cell-label">${tooltip}<span class="required">*</span></div>
													                	<div class="input-group ">
													                		<form:input path="lastName" cssClass="form-control table-cell input-sm" tabindex="5" title="${tooltip}" id="staff-lastName" onkeypress="return validateKey(event)"  data-toggle="tooltip" data-placement="bottom"></form:input>
													                		<spring:bind path="lastName">
									   											<elitecoreError:showError errorMessage="${status.errorMessage}" errorId = "staff-lastName_error"></elitecoreError:showError>
									   										</spring:bind>
													                	</div>
												                	</div>
												                </div>
												                <div class="col-md-6">
												                	<spring:message code="addStaff.middle.name" var="tooltip" ></spring:message>
												                	<div class="form-group">
													            		<div class="table-cell-label">${tooltip }</div>
													                	<div class="input-group ">
													                		<form:input path="middleName" cssClass="form-control table-cell input-sm" tabindex="6" title="${tooltip}" id="staff-middleName" onkeypress="return validateKey(event)"  data-toggle="tooltip" data-placement="bottom"></form:input>
													                		<spring:bind path="middleName">
									   											<elitecoreError:showError errorMessage="${status.errorMessage}" errorId = "staff-middleName_error"></elitecoreError:showError>
									   										</spring:bind>
													                	</div>
												                	</div>
												                </div>
												                <div class="col-md-6">
												                	<spring:message code="addStaff.birth.date" var="tooltip" ></spring:message>
												                 	<div class="form-group">
												            			<div class="table-cell-label">${tooltip }<span class="required">*</span></div>
												            			<div class="input-group input-append date" id="dateRangePicker" data-date-end-date="0d">
															                <span class="input-group-addon add-on"> <i class="fa fa-calendar"></i></span>
															                <form:input path="birthDate" cssClass="form-control table-cell input-sm" tabindex="7" id="staff-birthDate" onchange="pickerclose(this.id)" title="${tooltip}"   data-toggle="tooltip" data-placement="bottom" ></form:input> 
															                <spring:bind path="birthDate">
									   											<elitecoreError:showError errorMessage="${status.errorMessage}" errorId = "staff-birthDate_error"></elitecoreError:showError>
									   										</spring:bind>
															            </div>
												                	</div>
												                </div>
												                <div class="col-md-6">
												                	<spring:message code="addStaff.address" var="tooltip"></spring:message>
												                	<div class="form-group">
												            			<div class="table-cell-label">${tooltip }<span class="required">*</span></div>
												               	 		<div class="input-group ">
												               	 			<form:textarea path="address" cssClass="form-control input-sm" rows="3" tabindex="8" id="staff-address" title="${tooltip}" onkeypress="return validateKey(event)"  data-toggle="tooltip" data-placement="bottom" ></form:textarea>
												               	 			<spring:bind path="address">
									   											<elitecoreError:showError errorMessage="${status.errorMessage}" errorId = "staff-address_error"></elitecoreError:showError>
									   										</spring:bind>
												               	 		</div>
												                	</div>
												                </div>
												                <div class="col-md-6">
												                	<spring:message code="addStaff.address2" var="tooltip"></spring:message>
												                	<div class="form-group">
												            			<div class="table-cell-label">${tooltip }</div>
												                		<div class="input-group ">
												                			<form:textarea path="address2" cssClass="form-control input-sm" tabindex="9" rows="3" id="staff-address2" title="${tooltip}" onkeypress="return validateKey(event)"  data-toggle="tooltip" data-placement="bottom" ></form:textarea>
												                			<spring:bind path="address2">
									   											<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
									   										</spring:bind>
												                		</div>
												                	</div>
												                </div>
												        
												                <div class="col-md-6">
												                	<spring:message code="addStaff.city" var="tooltip" ></spring:message>
													                <div class="form-group">
												    		        	<div class="table-cell-label">${tooltip}<span class="required">*</span></div>
												            		    <div class="input-group ">
												            		    	<form:input path="city" cssClass="form-control table-cell input-sm" id="staff-city" tabindex="10" title="${tooltip}" onkeypress="return validateKey(event)"  data-toggle="tooltip" data-placement="bottom" ></form:input>
												            		    	<spring:bind path="city">
									   											<elitecoreError:showError errorMessage="${status.errorMessage}" errorId = "staff-city_error"></elitecoreError:showError>
									   										</spring:bind>
												            		    </div>
												                	</div>
												                </div>
												                <div class="col-md-6">
												                	<spring:message code="addStaff.state" var="tooltip"></spring:message>
												                	<div class="form-group">
												            			<div class="table-cell-label">${tooltip}</div>
												                		<div class="input-group ">
												                			<form:input path="state" cssClass="form-control table-cell input-sm" id="staff-state" tabindex="11" title="${tooltip}" onkeypress="return validateKey(event)"  data-toggle="tooltip" data-placement="bottom"></form:input>
												                			<spring:bind path="state">
									   											<elitecoreError:showError errorMessage="${status.errorMessage}" errorId = "staff-state_error"></elitecoreError:showError>
									   										</spring:bind>
												                		</div>
												                	</div>
												                </div>
												                <div class="col-md-6">
												                	<spring:message code="addStaff.country" var="tooltip" ></spring:message>
												                	<div class="form-group">
												            			<div class="table-cell-label">${tooltip}</div>
												                		<div class="input-group "> 
												                			<form:input path="country" cssClass="form-control table-cell input-sm" id="staff-country" tabindex="12" title="${tooltip}" onkeypress="return validateKey(event)"  data-toggle="tooltip" data-placement="bottom" ></form:input>
												                			<spring:bind path="country">
									   											<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
									   										</spring:bind>
												                		</div>
												                	</div>
												                </div>
												                <div class="col-md-6">
												                	<spring:message code="addStaff.pincode" var="tooltip"></spring:message>
													                <div class="form-group">
												    		        	<div class="table-cell-label">${tooltip}<span class="required">*</span></div>
												            	    	<div class="input-group ">
												            	    		<form:input path="pincode" cssClass="form-control table-cell input-sm" id="staff-pincode" tabindex="13" title="${tooltip}" onkeypress="return validateKey(event)"  data-toggle="tooltip" data-placement="bottom" ></form:input>
												            	    		<spring:bind path="pincode">
									   											<elitecoreError:showError errorMessage="${status.errorMessage}" errorId = "staff-pincode_error"></elitecoreError:showError>
									   										</spring:bind>
												            	    	</div>
												                	</div>
												                </div>
												                            <div class="col-md-6 ">
												                
													                <div class="form-group">
													                	<spring:message code="addStaff.staff.code" var="tooltip"></spring:message>
													                	<div class="table-cell-label">${tooltip}<span class="required">*</span></div>
													                	<div class="input-group">
													                		<form:input path="staffCode" cssClass="form-control table-cell input-sm" tabindex="14" id="staff-staffCode" title="${tooltip}" onkeypress="return validateKey(event)"  data-toggle="tooltip" data-placement="bottom"></form:input>
																			<spring:bind path="staffCode">
									   											<elitecoreError:showError errorMessage="${status.errorMessage}" errorId = "staff-staffCode_error"></elitecoreError:showError>
									   										</spring:bind>
													                	</div>
													                </div>
												                </div>
                											</div><!-- /.box-body -->
              											</div>
													</div>
                
                									<div class="fullwidth">
														<div class="box box-warning">
                											<div class="box-header with-border">
                  												<h3 class="box-title"><spring:message code="addStaff.contact.details" ></spring:message></h3>
                  												<div class="box-tools pull-right">
                    												<button class="btn btn-box-tool" data-widget="collapse"><i class="fa fa-minus"></i></button>
                  												</div><!-- /.box-tools -->
                											</div><!-- /.box-header -->
	                										<div class="box-body inline-form">
	            												<div class="col-md-6 ">
	            													<spring:message code="addStaff.email.address" var="tooltip"></spring:message>
												                	<div class="form-group">
												            			<div class="table-cell-label">${tooltip}<span class="required">*</span></div>
												                		<div class="input-group "> 
												                			<form:input path="emailId" cssClass="form-control table-cell input-sm" tabindex="15" id="staff-emailId" title="${tooltip}" onkeypress="return validateKey(event)"  data-toggle="tooltip" data-placement="bottom" ></form:input>
												                			<spring:bind path="emailId">
									   											<elitecoreError:showError errorMessage="${status.errorMessage}" errorId = "staff-emailId_error"></elitecoreError:showError>
									   										</spring:bind>
												                		</div>
												                	</div>
	                											</div>
												                <div class="col-md-6 ">
												                	<spring:message code="addStaff.emailId2.address" var="tooltip"></spring:message>
												                	<div class="form-group">
												            			<div class="table-cell-label">${tooltip}</div>
												                		<div class="input-group ">
												                			<form:input path="emailId2" cssClass="form-control table-cell input-sm" tabindex="16" id="staff-emailId2" title="${tooltip}" onkeypress="return validateKey(event)"   data-toggle="tooltip" data-placement="bottom"></form:input>
												                			<spring:bind path="emailId2">
									   											<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
									   										</spring:bind>
												                		</div>
												                	</div>
												                </div>
												                <div class="col-md-6 ">
												                	<spring:message code="addStaff.mobile.no" var="tooltip" ></spring:message>
												                	<div class="form-group">
												            			<div class="table-cell-label">${tooltip}<span class="required">*</span></div>
												                		<div class="input-group ">
												                			<form:input path="mobileNo" cssClass="form-control table-cell input-sm" tabindex="17" id="staff-mobileNo" title="${tooltip}" onkeypress="return validateKey(event)"  data-toggle="tooltip" data-placement="bottom" ></form:input>
												                			<spring:bind path="mobileNo">
									   											<elitecoreError:showError errorMessage="${status.errorMessage}" errorId = "staff-mobileNo_error"></elitecoreError:showError>
									   										</spring:bind>
												                		</div>
												                	</div>
												                </div>
												                <div class="col-md-6 ">
												                	<spring:message code="addStaff.landline.no" var="tooltip" ></spring:message>
												                	<div class="form-group">
												            			<div class="table-cell-label">${tooltip}</div>
												                		<div class="input-group ">
												                			<form:input path="landlineNo" cssClass="form-control table-cell input-sm" id="staff-landlineNo" tabindex="18" title="${tooltip}" onkeypress="return validateKey(event)"  data-toggle="tooltip" data-placement="bottom" ></form:input>
												                			<spring:bind path="landlineNo">
									   											<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
									   										</spring:bind>
												                		</div>
												                	</div>
												                </div>
	                
	                										</div><!-- /.box-body -->
              											</div>
													</div>
                
                									<div class="fullwidth">
														<div class="box box-warning">
                											<div class="box-header with-border">
                 	 											<h3 class="box-title"><spring:message code="addStaff.additional.details" ></spring:message></h3>
                  												<div class="box-tools pull-right">
                    												<button class="btn btn-box-tool" data-widget="collapse"><i class="fa fa-minus"></i></button>
                  												</div><!-- /.box-tools -->
                											</div><!-- /.box-header -->
                											<div class="box-body inline-form">
																<div class="col-md-6" >
																	<spring:message code="addStaff.login.ip.restriction" var="tooltip"></spring:message>
												                	<div class="form-group">
												            			<div class="table-cell-label">${tooltip}</div>
												                		<div class="input-group ">
												                			<spring:message code="addStaff.login.ip.restriction.placeholder" var="placeholder"></spring:message>
												                			<form:input path="loginIPRestriction" tabindex="19" cssClass="form-control table-cell input-sm" title="${placeholder}" id="staff-loginIPRestriction" data-role="tagsinput" data-toggle="tooltip" data-placement="bottom" ></form:input>
												                			<spring:bind path="loginIPRestriction">
									   											<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
									   										</spring:bind>
												                		</div>
												                	</div>
            													</div>
                											</div><!-- /.box-body -->
              											</div>
													</div>
													
                								</div>
										
											</div>
              							</div><!-- /.box -->
                        			</div>
            					</div>
            				</div>
          				</div>
           				<!-- Content Wrapper. Contains page content End -->
          
          			</div>
          		</div>
          </div>
          </sec:authorize>
       </section>
     
     </div><!-- /.content-wrapper -->
     
     
     <!-- Footer Start -->
	<footer class="main-footer positfix">
    	<div class="fooinn">
        	<div class="fullwidth">
        		<div class="button-set">
	            	<div class="padleft-right" id="staffCreateDiv">
	            		<input id = "save-next" type="submit" class="btn btn-grey btn-xs " tabindex="20" value="<spring:message code="btn.label.save.next" ></spring:message>"/>
						<button id = "resetBtn" class="btn btn-grey btn-xs " tabindex="21" onclick="resetForm();">
							<spring:message code="btn.label.reset" ></spring:message>
						</button>
						<a id = "cancelBtn" class="btn btn-grey btn-xs " style="text-decoration:none;" tabindex="22" href="<%= ControllerConstants.STAFF_MANAGER %>">
							<spring:message code="btn.label.cancel" ></spring:message>
						</a>
					</div>	
				</div>
        	</div>
        	
     		<jsp:include page="../common/newfooter.jsp"></jsp:include>
     	</div>
	</footer>
	</form:form>
    <!-- Footer End -->		 
		<form id="staff-manager-form" action="" method="GET">
			<input type="hidden" id="REQUEST_ACTION_TYPE" name="REQUEST_ACTION_TYPE" value=""/> 
		</form>
    </div><!-- ./wrapper -->

<script type="text/javascript">
			$(function () {
				$('#dateRangePicker').datepicker({
					onSelect:function(){
						$("input[tabindex="+(this.tabIndex+1)+"]").focus();
				        }
				    });
				});
			
			function moveToNextElement(id){
				$('#'+id).focus();
			}
			
			function validateFields(){
				if($("#sizeLimit").val()=='Y'){
					$("#staff-form").submit();
					$("#responseMsgDiv").html('');
				}
				else{
					$("#responseMsgDiv").html("<spring:message code='staff.image.size.exceed'></spring:message>");
				}
				$("#responseMsgDiv").css('display','block');
			}
			
			function validateKey(e){
				// Enter pressed?
				if(e.which == 10 || e.which == 13) {
					validateFields();
            	}
			}
			
			function resetForm(){
				$("#staff-username").val('');
				$("#staff-password").val('');
				$("#staff-confirmPassword").val('');
				
				$('#profilePicPreview').html('<img alt="Staff" src="images/staff_default_profile_pic.png">');
				$("#profilePicFile").val('');
				
				$("#staff-staffCode").val('');
				$("#staff-firstName").val('');
				$("#staff-lastName").val('');
				$("#staff-middleName").val('');
				$("#staff-birthDate").val('');
				$("#staff-address").val('');
				$("#staff-address2").val('');
				$("#staff-city").val('');
				$("#staff-state").val('');
				$("#staff-country").val('');
				$("#staff-pincode").val('');
				$("#staff-emailId").val('');
				$("#staff-emailId2").val('');
				$("#staff-mobileNo").val('');
				$("#staff-landlineNo").val('');
				
				$("#staff-loginIPRestriction").val('');
				$(".bootstrap-tagsinput span span").click();
				resetWarningDisplay();
			}
			
			function pickerclose(id){
			//	$("#staff-address").focus();
			}
			
			function loadBredthcrumLinks(formAction, formMethod,requestAction){
				$("#staff-manager-form").attr("action",formAction);
				$("#staff-manager-form").attr("method",formMethod);
				$("#REQUEST_ACTION_TYPE").val(requestAction);
				$('#staff-manager-form').submit();	
			}
		</script>
		

  </body>
</html>
