
<%@page import="com.elitecore.sm.util.MapCache"%>
<%@page import="com.elitecore.sm.common.constants.SystemParametersConstant"%>
<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<%@page import="com.elitecore.sm.util.EliteUtils"%>
<%@page import="com.elitecore.sm.common.constants.BaseConstants"%>
<%@taglib 	uri="http://www.springframework.org/tags" 			prefix="spring"%>
<%@ taglib 	uri="http://www.springframework.org/security/tags" 	prefix="sec"  %>
<%@taglib uri="/WEB-INF/ShowErrorTag.tld" prefix="elitecoreError" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<html>
<body class="skin-blue sidebar-mini sidebar-collapse">
<%-- <jsp:include page="../common/newheader.jsp" ></jsp:include>
<jsp:include page="../common/newleftMenu.jsp"></jsp:include> --%>
<div class="tab-content no-padding clearfix" id="host_Confg_Block">
<form:form modelAttribute="host_configuration_form_bean" method="POST" action="addService"   id="save-host-conf-form">

<div class="title2">
			<spring:message code="roamingconfiguration.hostconfiguration.section.heading" ></spring:message>
</div>
 <div class="col-md-6 inline-form" style="padding-left: 0px !important;">
     	 	<div class="form-group">
	             	<spring:message code="roamingconfiguration.host.confg.name.label" var="name" ></spring:message>
	             	<spring:message code="roamingconfiguration.host.confg.name.tooltip" var="tooltip" ></spring:message>
	         		<div class="table-cell-label">${name}<span class="required">*</span></div>
	             	<div class="input-group ">
	             		<form:input  path="name" id="hostConfigNameId" cssClass="form-control table-cell input-sm" maxlength="50" data-toggle="tooltip" data-placement="bottom" title="${tooltip }"></form:input>
	             		<spring:bind path="name">
										<elitecoreError:showError
											errorId="name_err"
											errorMessage="${status.errorMessage}"></elitecoreError:showError>
									</spring:bind>
	             	</div>
	             </div>
        	 	<div class="form-group">
	             	<spring:message code="roamingconfiguration.host.confg.description.label" var="description" ></spring:message>
	             	<spring:message code="roamingconfiguration.host.confg.description.tooltip" var="tooltip" ></spring:message>
	         		<div class="table-cell-label">${description}</div>
	             	<div class="input-group ">
	             	<form:textarea  path="description" id="hostConfigDescId" cssClass="form-control input-sm" rows="4"  data-toggle="tooltip" data-placement="bottom" title="${tooltip }"></form:textarea>
	             	<spring:bind path="description">
										<elitecoreError:showError
											errorId="description_err"
											errorMessage="${status.errorMessage}"></elitecoreError:showError>
									</spring:bind>
	             	
	             	
	             	</div>
	             </div>
	             
	             <div class="form-group">
	             	<spring:message code="roamingconfiguration.host.confg.timezone.label" var="timezone" ></spring:message>
	             	<spring:message code="roamingconfiguration.host.confg.timezone.tooltip" var="tooltip" ></spring:message>
	         		<div class="table-cell-label">${timezone}<span class="required">*</span></div>
	             	<div class="input-group ">
	             	<form:select  path="timezone" id="host_timezone_id" cssClass="form-control table-cell input-sm"  data-toggle="tooltip" data-placement="bottom" title="${tooltip }">
	             	<option value = "">Select unified field</option>
											<c:forEach var="timeZoneEnum" items="${timeZoneEnum}">
												<form:option value="${timeZoneEnum.name}">${timeZoneEnum}</form:option>
											</c:forEach>
	             	</form:select>
	             	<spring:bind path="timezone">
										<elitecoreError:showError
											errorId="host_timezone_err"
											errorMessage="${status.errorMessage}"></elitecoreError:showError>
									</spring:bind>
	             	
	             	
	             	</div>
	             </div>
	             <div class="form-group">
	             	<spring:message code="roamingconfiguration.host.confg.pmncode.label" var="pmncode" ></spring:message>
	             	<spring:message code="roamingconfiguration.host.confg.pmncode.tooltip" var="tooltip" ></spring:message>
	         		<div class="table-cell-label">${pmncode}<span class="required">*</span></div>
	             	<div class="input-group ">
	             		<form:input  path="pmncode" id="hostConfigPmnId" cssClass="form-control table-cell input-sm" maxlength="15" data-toggle="tooltip" data-placement="bottom" title="${tooltip }"></form:input>
	             		<spring:bind path="pmncode">
										<elitecoreError:showError
											errorId="pmncode_err"
											errorMessage="${status.errorMessage}"></elitecoreError:showError>
									</spring:bind>
	             	</div>
	             </div>
	             
	             <div class="form-group">
	             	<spring:message code="roamingconfiguration.host.confg.tadigcode.label" var="tadigcode" ></spring:message>
	             	<spring:message code="roamingconfiguration.host.confg.tadigcode.tooltip" var="tooltip" ></spring:message>
	         		<div class="table-cell-label">${tadigcode}<span class="required">*</span></div>
	             	<div class="input-group ">
	             		<form:input  path="tadigcode" id="hosttadigId" cssClass="form-control table-cell input-sm" data-toggle="tooltip" maxlength="5" data-placement="bottom" title="${tooltip }"></form:input>
	             		<spring:bind path="tadigcode">
										<elitecoreError:showError
											errorId="tadigcode_err"
											errorMessage="${status.errorMessage}"></elitecoreError:showError>
									</spring:bind>
	             	</div>
	             </div>
	             
	             
</div>

</form:form>

<footer class="main-footer positfix">
    	<div class="fooinn">
        	<div class="fullwidth">
	            	<div class="padleft-right" id="generalParameterBtnsDiv" ">
                		<button class="btn btn-grey btn-xs " id="host-conf-save-btn" type="button" onclick="validateHostConfigurationFields();"><spring:message code="btn.label.save" ></spring:message></button>
           				<button class="btn btn-grey btn-xs"  id="host-conf-reset-btn" type="button" onclick="resetHostConfigurationForm();"><spring:message code="btn.label.reset" ></spring:message></button>
           				<%-- <button class="btn btn-grey btn-xs"  id="host-conf-cancel-btn" type="button" onclick="cancelHostConfigurationForm();"><spring:message code="btn.label.cancel" ></spring:message></button> --%>
						<%-- <button class="btn btn-grey btn-xs"  id="edit-btn"  type="button" onclick="editSystemParam();"><spring:message code="btn.label.edit" ></spring:message></button> --%>											
		           	</div>
		          
		           	</div>
		           	</div>
		           	<jsp:include page="../common/newfooter.jsp"></jsp:include>
		           	</footer>
					

</div>


</body>

<script type="text/javascript">
function validateHostConfigurationFields(){
	$("#save-host-conf-form").attr("action","<%=ControllerConstants.MODIFY_HOST_CONFIGURATION%>")
	$("#save-host-conf-form").submit();
	
}
function resetHostConfigurationForm(){
	$("#hostConfigNameId").val('');
	$("#hostConfigDescId").val('');
	$("#host_timezone_id").val('');
	$("#hosttadigId").val('');
	resetWarningDisplay();
}

</script>
</html>
