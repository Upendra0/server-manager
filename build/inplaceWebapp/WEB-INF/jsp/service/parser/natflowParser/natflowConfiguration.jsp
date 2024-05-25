<%@page import="com.elitecore.sm.common.model.DecodeTypeEnum"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"   pageEncoding="UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
 <%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%> 
<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<%@taglib uri="/WEB-INF/ShowErrorTag.tld" prefix="elitecoreError" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<div class="tab-content no-padding  mtop15">
	<div class="fullwidth mtop15">
		
		<!-- Section-1 start here parser basic details -->
		<jsp:include page="../../pluginBasicDetails.jsp"></jsp:include>
		<!-- Section-1 end here parser basic details -->
		
		<!-- Device details Section  start here  -->
		<div class="fullwidth" id="device_details_div">
		<jsp:include page="../../pluginDeviceConfiguration.jsp"></jsp:include>
		</div>
		<!-- Device details Section end here    -->
		
		
			<form:form modelAttribute="natflow_parser_mapping_form_bean" method="POST" action="" id="natflow-parser-configuration-form">
			
			<input type="hidden" value="<%=DecodeTypeEnum.UPSTREAM%>" name="decodeType" id="decodeType" />
			<input type="hidden" value="${plugInId}" name="plugInId" id="plugInId" />
			<input type="hidden" value="${plugInName}" name="plugInName" id="plugInName" />
			<input type="hidden" value="${plugInType}" name="plugInType" id="plugInType" />
			
			<input type="hidden" id="mappingDeviceId" name="device.id" value="0">  <!-- Added this to pass as hidden value so from mapping it can display device details for audit. -->
			
			
			<input type="hidden" id="serviceId" name="serviceId" value="${serviceId}">
			<input type="hidden" id="serviceType" name="serviceType" value="${serviceType}" />	
			<input type="hidden" id="serviceName" name="serviceName" value="${serviceName}">
			<input type="hidden" id="instanceId" name="instanceId" value="${instanceId}">
			<input type="hidden" id="serviceInstanceId" name="serviceInstanceId" value="${serviceInstanceId}">
			<input type="hidden" id="id" name="id" value="0">
			<input type="hidden" id="selecteMappingName" name="selecteMappingName" value="">
			<input type="hidden" id="selecteDeviceName" name="selecteDeviceName" value="">	
			<input type="hidden" id="selectedMappingType" name="selectedMappingType" value="">	
			<input type="hidden" value="NO_ACTION" name="actionType" id="actionType" />	
			<input type="hidden" value="false" name="readOnlyFlag" id="readOnlyFlag" />
			<input type="hidden" value="${isError}" name="isError" id="isError"/>
								
			<!-- parser Advance details section start here -->
			<div class="fullwidth" id="advance_template_details_div">
			 <jsp:include page="../parserTemplateDetails.jsp"></jsp:include>
			</div> 
			<!-- parser Advance details end here -->
		
			<!-- Option template details section start here parser  -->
				<div class="fullwidth" id="option_template_details_div">
					<jsp:include page="../../optionTemplateParam.jsp"></jsp:include>
				</div>
			<!-- Option template details section end here  -->
			
			<!-- Filter Params for natflow parser -->
			<c:if test="${plugInType eq 'NATFLOW_PARSING_PLUGIN' }">
				<div class="fullwidth" id="filter_details_div">
					<jsp:include page="./natflowFilterParams.jsp"></jsp:include>
				</div>
			</c:if>			
		</form:form>		
	</div>
</div>