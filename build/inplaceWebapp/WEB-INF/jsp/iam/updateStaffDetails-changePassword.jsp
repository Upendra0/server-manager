<%@page import="com.elitecore.sm.iam.model.Staff"%>
<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<%@page import="com.elitecore.sm.common.constants.BaseConstants"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<div class="fullwidth borbot padding0 tab-pane <c:if test="${(REQUEST_ACTION_TYPE eq 'UPDATE_STAFF_CHANGE_PASSWORD')}"><c:out value="active"></c:out></c:if>" id="update-staff-change-password">
	<div class="col-md-6 inline-form"><br/>
<!-- 		<h4 class="section-title preface-title"> -->
<%-- 			<spring:message code="changePassword.generate.password" ></spring:message> --%>
<!-- 		</h4> -->
			<form method="POST" action="<%= ControllerConstants.UPDATE_STAFF_PASSWORD %>" id="staff-change-password-form">
				<input type="hidden" id="staffUsername" name="staffUsername" value="${staff_form_bean.username}"/>
				<input type="hidden" name="<%= BaseConstants.REQUEST_ACTION_TYPE %>"  id="<%= BaseConstants.REQUEST_ACTION_TYPE %>" value="<%= BaseConstants.UPDATE_STAFF_CHANGE_PASSWORD %>" />
					<div class="form-group">
		               	<spring:message code="changePassword.new.password" var="tooltip"></spring:message>
		            	<div class="table-cell-label">${tooltip}<span class="required">*</span></div>
		                <div class="input-group ">
		                	<input type="password" class="form-control table-cell input-sm" tabindex="1" id="newPassword" name="newPassword"  data-toggle="tooltip" data-placement="bottom"  title="${tooltip }"  />
		                	<span class="input-group-addon add-on last">
		                		<i class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="bottom" title="${NEW_PASSWORD_ERROR}"></i>
		                	</span>
		                </div>
		            </div>
		            <div class="form-group">
		               	<spring:message code="changePassword.confirm.new.password" var="tooltip"></spring:message>
		            	<div class="table-cell-label">${tooltip} <span class="required">*</span></div>
		                <div class="input-group ">
		                	<input type="password" class="form-control table-cell input-sm" tabindex="2" id="confirmNewPassword" name="confirmNewPassword"  data-toggle="tooltip" data-placement="bottom" title="${tooltip }" />
		                	<span class="input-group-addon add-on last">
		                		<i class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="bottom" title="${CONFIRM_NEW_PASSWORD_ERROR}"></i>
		                	</span>
		                </div>
		            </div>
		            <div class="form-group">
		               	<spring:message code="updateStaffMgmt-reason-for-change" var="tooltip" ></spring:message>
		            	<div class="table-cell-label">${tooltip} <span class="required">*</span></div>
		                <div class="input-group ">
		                	<textarea rows="3" class="form-control input-sm" tabindex="3" id="staff-change-password-reason-for-change" name="staff-change-password-reason-for-change"  data-toggle="tooltip" data-placement="bottom" title="${tooltip }"></textarea>
		                	<span class="input-group-addon add-on last"><i class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="bottom" title="${STAFF_CHANGE_PASSWORD_REASON_FOR_CHANGE_ERROR}"></i></span>
		                </div>
		            </div>
		         	<br/>
		         	<br/>
		         	<br/>
			</form>
	</div>
</div>
<script type="text/javascript">
	$( document ).ready(function() {
		$("#staff-change-password-form input,#staff-change-password-form select,#staff-change-password-form textarea").keypress(function (event) {
		    if (event.which == 13) {
		        event.preventDefault();
		        $(this).blur();
		        validateFieldsForChangePassword();
		    }
		});
	});



	function validateFieldsForChangePassword(){
		$("#staff-change-password-form").submit();
	}
	
	function resetChangePasswordForm(){
		$("#newPassword").val('');
		$("#newPasswordError").html('');
		
		$("#confirmNewPassword").val('');
		$("#confirmNewPasswordError").html('');
		
		$("#staff-change-password-reason-for-change").val('');
		$("#staff-change-password-reason-for-change-errors").html('');
		
		resetWarningDisplay();
	}
	
	$( document ).ready(function() {
		$("#staff-change-password-form input, select, textarea").keypress(function (event) {
		    if (event.which == 13) {
		        event.preventDefault();
		        $(this).blur();
		        validateFieldsForChangePassword();
		    }
		});
	});
	
</script>
