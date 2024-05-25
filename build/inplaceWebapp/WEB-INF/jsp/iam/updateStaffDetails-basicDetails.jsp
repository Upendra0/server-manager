<%@page import="com.elitecore.sm.util.MapCache"%>
<%@page import="com.elitecore.sm.common.constants.SystemParametersConstant"%>
<%@page import="com.elitecore.sm.iam.model.Staff"%>
<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<%@page import="com.elitecore.sm.common.constants.BaseConstants"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="/WEB-INF/ShowErrorTag.tld" prefix="elitecoreError" %>


<div class="fullwidth borbot tab-pane <c:if test="${(REQUEST_ACTION_TYPE eq 'UPDATE_STAFF_BASIC_DETAIL')}"><c:out value="active"></c:out></c:if>" id="update-staff-details">
	<div class="mtop10">
	<input type="hidden" id="sizeLimit" value="Y"/>
	<form:form modelAttribute="staff_form_bean" method="POST" action="<%= ControllerConstants.UPDATE_STAFF %>" id="staff-form" enctype="multipart/form-data">
	<div class="fullwidth">
							
		<form:input type="hidden" path="id"  id="staff-id" ></form:input>
		<form:input type="hidden" path="username"  id="staff-username" ></form:input>
		<form:input type="hidden" path="password"  id="staff-password" ></form:input>
		<form:input type="hidden" path="staffCode"  id="staff-staffCode" ></form:input>
		<form:input type="hidden" path="firstTimeLogin"  id="staff-firstTimeLogin" ></form:input>

		<form:input type="hidden" path="securityQuestion1"  id="staff-securityQuestion1" ></form:input>
		<form:input type="hidden" path="securityAnswer1"  id="staff-securityAnswer1" ></form:input>
		<form:input type="hidden" path="securityQuestion2"  id="staff-securityQuestion2" ></form:input>
		<form:input type="hidden" path="securityAnswer2"  id="staff-securityAnswer2" ></form:input>
								
		<form:input type="hidden" path="accountState"  id="staff-accountState" ></form:input>
		<form:input type="hidden" path="accountLocked"  id="staff-accountLocked" ></form:input>
		<form:input type="hidden" path="wrongAttempts"  id="staff-wrongAttempts" ></form:input>
		<form:input type="hidden" path="lastWrongAttemptsDate"  id="staff-lastWrongAttemptsDate" ></form:input>
		<form:input type="hidden" path="passwordExpiryDate"  id="staff-passwordExpiryDate" ></form:input>
		<form:input type="hidden" path="lastLoginTime"  id="staff-lastLoginTime" ></form:input>
		<form:input type="hidden" path="createdDate"  id="staff-createdDate" ></form:input>
		<form:input type="hidden" path="lastUpdatedDate"  id="staff-lastUpdatedDate" ></form:input>

		<form:input type="hidden" path="createdByStaffId"  id="staff-createdByStaffId" ></form:input>
		<form:input type="hidden" path="lastUpdatedByStaffId"  id="staff-lastUpdatedByStaffId" ></form:input>
		<form:input type="hidden" path="profilePic"  id="staff-profilePic" ></form:input>
		
		<input type="hidden" name="<%= BaseConstants.REQUEST_ACTION_TYPE %>"  id="<%= BaseConstants.REQUEST_ACTION_TYPE %>" value="<%= BaseConstants.UPDATE_STAFF_BASIC_DETAIL %>" />

		<c:set var="aglIndex" scope="request" value="0"></c:set>
		<c:forEach items="${staff_form_bean.accessGroupList}" var="accessGroupList">
 			<input type="hidden" id="accessGroupList[${aglIndex}].id" name="accessGroupList[${aglIndex}].id" value="${accessGroupList.id}" />
			<c:set var="aglIndex" scope="request" value="${aglIndex + 1}"></c:set>
		</c:forEach>
		
		
		<div class="box box-warning">
              <div class="box-header with-border">
                 <h3 class="box-title"><spring:message code="addStaff.login.details" ></spring:message></h3>
                 <div class="box-tools pull-right">
                   <button class="btn btn-box-tool" data-widget="collapse"><i class="fa fa-minus"></i></button>
                 </div><!-- /.box-tools -->
              </div><!-- /.box-header -->
   			<div class="box-body">
				<div class="col-md-6 inline-form">
	            	<div class="form-group">
	            		<spring:message code="addStaff.username" var="tooltip"></spring:message>
		            	<div class="table-cell-label">${tooltip}<span class="required">*</span></div>
		                <div class="input-group">
		                	<label>${staff_form_bean.username}</label>
	                	</div>
	                </div>
	             </div>
  				</div><!-- /.box-body -->
  		</div>
    	
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
		                	<form:input path="firstName" cssClass="form-control table-cell input-sm" tabindex="4" id="staff-firstName" data-toggle="tooltip" data-placement="bottom"  title="${tooltip }"></form:input>
		                	<spring:bind path="firstName">
								<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
							</spring:bind>
		                </div>
		            </div>
                </div>
                <div class="col-md-6">
                	<spring:message code="addStaff.last.name" var="tooltip"></spring:message>
                	<div class="form-group">
	            		<div class="table-cell-label">${tooltip}<span class="required">*</span></div>
	                	<div class="input-group ">
	                		<form:input path="lastName" cssClass="form-control table-cell input-sm" tabindex="5" id="staff-lastName" data-toggle="tooltip" data-placement="bottom"  title="${tooltip }"></form:input>
	                		<spring:bind path="lastName">
								<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
							</spring:bind>
	                	</div>
                	</div>
                </div>
                <div class="col-md-6">
                	<spring:message code="addStaff.middle.name" var="tooltip" ></spring:message>
                	<div class="form-group">
	            		<div class="table-cell-label">${tooltip }</div>
	                	<div class="input-group ">
	                		<form:input path="middleName" cssClass="form-control table-cell input-sm" tabindex="6" id="staff-middleName" data-toggle="tooltip" data-placement="bottom"  title="${tooltip }"></form:input>
	                		<spring:bind path="middleName">
								<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
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
			                <form:input path="birthDate" cssClass="form-control table-cell input-sm" tabindex="7" id="staff-birthDate"  data-toggle="tooltip" data-placement="bottom" title="${tooltip }" ></form:input> 
			                <spring:bind path="birthDate">
								<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
							</spring:bind>
			            </div>
                	</div>
                </div>
                <div class="col-md-6">
                	<spring:message code="addStaff.address" var="tooltip"></spring:message>
                	<div class="form-group">
            			<div class="table-cell-label">${tooltip }<span class="required">*</span></div>
               	 		<div class="input-group ">
               	 			<form:textarea rows="3" path="address" cssClass="form-control input-sm" tabindex="8" id="staff-address"  data-toggle="tooltip" data-placement="bottom"  title="${tooltip }" ></form:textarea>
               	 			<spring:bind path="address">
								<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
							</spring:bind>
               	 		</div>
                	</div>
                </div>
                <div class="col-md-6">
                	<spring:message code="addStaff.address2" var="tooltip"></spring:message>
                	<div class="form-group">
            			<div class="table-cell-label">${tooltip }</div>
                		<div class="input-group ">
                			<form:textarea rows="3" path="address2" cssClass="form-control input-sm" tabindex="9" id="staff-address2"  data-toggle="tooltip" data-placement="bottom"  title="${tooltip }" ></form:textarea>
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
            		    	<form:input path="city" cssClass="form-control table-cell input-sm" id="staff-city" tabindex="10"  data-toggle="tooltip" data-placement="bottom"  title="${tooltip }" ></form:input>
            		    	<spring:bind path="city">
								<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
							</spring:bind>
            		    </div>
                	</div>
                </div>
                <div class="col-md-6">
                	<spring:message code="addStaff.state" var="tooltip"></spring:message>
                	<div class="form-group">
            			<div class="table-cell-label">${tooltip}</div>
                		<div class="input-group ">
                			<form:input path="state" cssClass="form-control table-cell input-sm" id="staff-state" tabindex="11"  data-toggle="tooltip" data-placement="bottom"  title="${tooltip }" ></form:input>
                			<spring:bind path="state">
								<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
							</spring:bind>
                		</div>
                	</div>
                </div>
                <div class="col-md-6">
                	<spring:message code="addStaff.country" var="tooltip" ></spring:message>
                	<div class="form-group">
            			<div class="table-cell-label">${tooltip}</div>
                		<div class="input-group "> 
                			<form:input path="country" cssClass="form-control table-cell input-sm" id="staff-country" tabindex="12"  data-toggle="tooltip" data-placement="bottom"  title="${tooltip }" ></form:input>
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
            	    		<form:input path="pincode" cssClass="form-control table-cell input-sm" id="staff-pincode" tabindex="13"  data-toggle="tooltip" data-placement="bottom"  title="${tooltip }" ></form:input>
            	    		<spring:bind path="pincode">
								<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
							</spring:bind>
            	    	</div>
                	</div>
                </div>
                      <div class="col-md-6">
                	<div class="form-group">
	                	<spring:message code="addStaff.staff.code" var="tooltip"></spring:message>
	                	<div class="form-label">${tooltip}</div>
	                	<div class="input-group">
	                		${staff_form_bean.staffCode}
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
                			<form:input path="emailId" cssClass="form-control table-cell input-sm" tabindex="16" id="staff-emailId"  data-toggle="tooltip" data-placement="bottom"  title="${tooltip }" ></form:input>
                			<spring:bind path="emailId">
								<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
							</spring:bind>
                		</div>
                	</div>
     			</div>
                <div class="col-md-6 ">
                	<spring:message code="addStaff.emailId2.address" var="tooltip"></spring:message>
                	<div class="form-group">
            			<div class="table-cell-label">${tooltip}</div>
                		<div class="input-group ">
                			<form:input path="emailId2" cssClass="form-control table-cell input-sm" tabindex="17" id="staff-emailId2"  data-toggle="tooltip" data-placement="bottom"  title="${tooltip }" ></form:input>
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
                			<form:input path="mobileNo" cssClass="form-control table-cell input-sm" tabindex="18" id="staff-mobileNo"  data-toggle="tooltip" data-placement="bottom"  title="${tooltip }" ></form:input>
                			<spring:bind path="mobileNo">
								<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
							</spring:bind>
                		</div>
                	</div>
                </div>
                <div class="col-md-6 ">
                	<spring:message code="addStaff.landline.no" var="tooltip" ></spring:message>
                	<div class="form-group">
            			<div class="table-cell-label">${tooltip}</div>
                		<div class="input-group ">
                			<form:input path="landlineNo" cssClass="form-control table-cell input-sm" id="staff-landlineNo" tabindex="19"  data-toggle="tooltip" data-placement="bottom"  title="${tooltip }" ></form:input>
                			<spring:bind path="landlineNo">
								<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
							</spring:bind>
                		</div>
                	</div>
                </div>
     		</div><!-- /.box-body -->
  		</div>
	</div>
    
   	<div class="fullwidth" id="divIpDetail">
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
                			<form:input path="loginIPRestriction" tabindex="20" cssClass="form-control table-cell input-sm" id="staff-loginIPRestriction" data-role="tagsinput" data-toggle="tooltip" data-placement="bottom"  title="${placeholder}"></form:input>
                			<spring:bind path="loginIPRestriction">
								<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
							</spring:bind>
                		</div>
                	</div>
				</div>
    		</div><!-- /.box-body -->
  		</div>
	</div>
    </form:form>
    </div>
</div>

<script type="text/javascript">
 	$( document ).ready(function() {

		$(".datetimepicker").hide();
		
		$("#staff-form input,#staff-form select,#staff-form textarea").keypress(function (event) {
		    if (event.which == 13) {
		        event.preventDefault();
		        $(this).blur();
		        validateFields();
		    }
		});
	});
	

	function validateFields(){
		if($("#sizeLimit").val()=='Y')
			$("#staff-form").submit();
		else
			$("#divResponseMsg").html("<spring:message code='staff.image.size.exceed'></spring:message>");
	}
	
	function resetBasicDetailsForm(){
		$('#staff-form #profilePicPreview').html('<img alt="Staff" src="images/staff_default_profile_pic.png">');
		$("#staff-form #profilePicFile").val('');
		
		$("#staff-form #staff-firstName").val('');
		$("#staff-form #staff-lastName").val('');
		$("#staff-form #staff-middleName").val('');
		$("#staff-form #staff-birthDate").val('');
		$("#staff-form #staff-address").val('');
		$("#staff-form #staff-address2").val('');
		$("#staff-form #staff-city").val('');
		$("#staff-form #staff-state").val('');
		$("#staff-form #staff-country").val('');
		$("#staff-form #staff-pincode").val('');
		$("#staff-form #staff-emailId").val('');
		$("#staff-form #staff-emailId2").val('');
		$("#staff-form #staff-mobileNo").val('');
		$("#staff-form #staff-landlineNo").val('');
		$("#staff-form #staff-loginIPRestriction").val('');
		$("#staff-form .bootstrap-tagsinput span span").click();
		resetWarningDisplay();
	}
</script>
