<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<%@page	import="com.elitecore.sm.common.constants.SystemParametersConstant"%>
<%@page import="com.elitecore.sm.util.MapCache"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib tagdir="/WEB-INF/tags" prefix="elitecore"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="/WEB-INF/ShowErrorTag.tld" prefix="elitecoreError"%>
<%@taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<form:form modelAttribute="circle_form_bean" method="POST" action="javascript:;" id="device-form">
<div class="modal-content">
	<div class="modal-header padding10">
		<h4 class="modal-title">
			<i class="icon-hdd"></i> 
			<span id="circle_label"> 
				<spring:message code="circle.update.popup.heading.label" ></spring:message>
			</span>
		</h4>
	</div>
	<div class="modal-body padding10 inline-form">
		<input type="hidden" id="circleId" name="circleId" />
		<div class=fullwidth>
       		<jsp:include page="../common/responseMsgPopUp.jsp" ></jsp:include>
       	</div>
		<div class="fullwidth">
			<spring:message code="circle.data.mgmt.circle.name" var="name" ></spring:message>
			<spring:message code="circle.name.tooltip" var="tooltip"></spring:message>
			<div class="form-group">
				<div class="table-cell-label">${name}<span class="required">*</span></div>
				<div class="input-group ">										
					<form:input path="name" cssClass="form-control table-cell input-sm" id="update_name" 
						tabindex="1"  data-toggle="tooltip" data-placement="bottom"  title="${tooltip }"></form:input>					
					<spring:bind path="name">
						<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
					</spring:bind> 
				</div>
			</div>
		</div>
		<div class="fullwidth">		
			<spring:message code="circle.data.mgmt.circle.description" var="name" ></spring:message>
			<spring:message code="circle.description.tooltip" var="tooltip"></spring:message>
			<div class="form-group">
				<div class="table-cell-label">${name}</div>
				<div class="input-group ">	
					<form:input path="description" cssClass="form-control table-cell input-sm" id="update_desc" 
						tabindex="1"  data-toggle="tooltip" data-placement="bottom"  title="${tooltip }"></form:input> 					
					<spring:bind path="description">
						<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
					</spring:bind> 
				</div>
			</div>
		</div>
	</div>
	<div id="update-circle-buttons-div" class="modal-footer padding10">

		<sec:authorize access="hasAuthority('EDIT_ALERT_DETAILS')">
			<button id="update_circle_btn" type="button"
				id="updateCircleDetails" class="btn btn-grey btn-xs "
				onclick="updateCircleToDB();">
				<spring:message code="btn.label.update" ></spring:message>
			</button>
		</sec:authorize>

		<button id="update_circle_cancel_btn"
			onclick="javascript:parent.closeFancyBoxFromChildIFrame()"
			class="btn btn-grey btn-xs ">
			<spring:message code="btn.label.cancel" ></spring:message>
		</button>

	</div>

	<div id="update-circle-progress-bar-div"
		class="modal-footer padding10" style="display: none;">
		<jsp:include page="../common/processing-bar.jsp"></jsp:include>
	</div>

</div>
</form:form>
<script>

function updateCircleToDB(){
	resetWarningDisplayPopUp();
	clearAllMessagesPopUp();
	
	showProgressBar("update-circle");
	parent.resizeFancyBox();
	
	$.ajax({
		url: '<%=ControllerConstants.UPDATE_CIRCLE%>',
			cache : false,
			async : true,
			dataType : 'json',
			type : "POST",
			data : {
				"id" : $("#circleId").val(),
				"name" : $("#update_name").val(),
				"description" : $("#update_desc").val()				
			},
			success : function(data) {
				hideProgressBar("update-circle");
				var response = eval(data);
				var responseCode = response.code;
				var responseMsg = response.msg;
				var responseObject = response.object;

				if (responseCode == "200") {
					showSuccessMsg(responseMsg);
					closeFancyBox();
					reloadGridData();
				} else if (responseObject != undefined
						&& responseObject != 'undefined'
						&& responseCode == "400") {

					$("#update-circle-buttons-div").show();					
					$.each(responseObject, function(key,val){						 
						if(key == 'name' ){
							$("#update_name").next().children().first().attr("data-original-title",val).attr("id",key+"_error");
						}else if(key == 'description'){
							$("#update_desc").next().children().first().attr("data-original-title",val).attr( "id",key+"_error");
						}
						addErrorClassWhenTitleExistPopUp($("#GENERIC_ERROR_MESSAGE", window.parent.document).val());
					}); 
				} else {
					resetWarningDisplayPopUp();
					showErrorMsgPopUp(responseMsg);
				}
			},
			error : function(xhr, st, err) {
				hideProgressBar("update-circle");
				handleGenericError(xhr, st, err);
			}
		});
	}
</script>
