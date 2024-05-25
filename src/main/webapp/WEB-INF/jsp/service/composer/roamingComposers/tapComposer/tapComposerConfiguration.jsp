<%@page import="com.elitecore.sm.common.constants.FormBeanConstants"%>
<%@page import="com.elitecore.sm.common.constants.BaseConstants"%>
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
		<jsp:include page="../../../pluginBasicDetails.jsp"></jsp:include>
		<!-- Section-1 end here parser basic details -->
		
		<!-- Device details Section  start here  -->
		<div class="fullwidth" id="device_details_div">
		<jsp:include page="../../../pluginDeviceConfiguration.jsp"></jsp:include>
		</div>
		<!-- Device details Section end here    -->
		
		
			<form:form modelAttribute="<%=FormBeanConstants.TAP_COMPOSER_MAPPING_FORM_BEAN%>" method="POST" action="" id="ascii-composer-configuration-form">
			
			<input type="hidden" value="<%=DecodeTypeEnum.DOWNSTREAM%>" name="decodeType" id="decodeType" />
			<input type="hidden" value="${plugInId}" name="plugInId" id="plugInId" />
			<input type="hidden" value="${plugInName}" name="plugInName" id="plugInName" />
			<input type="hidden" value="${plugInType}" name="plugInType" id="plugInType" />
			<input type="hidden" id="driverId" name="driverId" value="${driverId}"/>
	    	<input type="hidden" id="driverName" name="driverName" value="${driverName}"/>
	    	<input type="hidden" id="driverTypeAlias" name="driverTypeAlias" value="${driverTypeAlias}"/>
			<input type="hidden" id="mappingDeviceId" name="device.id" value="0">  <!-- Added this to pass as hidden value so from mapping it can display device details for audit. -->
			<input type="hidden" id="serviceInstanceId" name="serviceInstanceId" value="${serviceInstanceId}">
			<input type="hidden" id="serviceId" name="serviceId" value="${serviceId}">
			<input type="hidden" id="serviceType" name="serviceType" value="${serviceType}" />	
			<input type="hidden" id="serviceName" name="serviceName" value="${serviceName}">
			<input type="hidden" id="instanceId" name="instanceId" value="${instanceId}">
			
			
			<input type="hidden" id="id" name="id" value="0">
			<input type="hidden" id="selecteMappingName" name="selecteMappingName" value="">
			<input type="hidden" id="selecteDeviceName" name="selecteDeviceName" value="">
			<input type="hidden" id="selDeviceId" name="selDeviceId" value="">
			<input type="hidden" id="selMappingId" name="selMappingId" value="">
			<input type="hidden" id="selVendorTypeId" name="selVendorTypeId" value="">
			<input type="hidden" id="selDeviceTypeId" name="selDeviceTypeId" value="">
				
			<input type="hidden" value="NO_ACTION" name="actionType" id="actionType" />	
			<input type="hidden" value="false" name="readOnlyFlag" id="readOnlyFlag" />
			<input type="hidden" id="selectedMappingType" name="selectedMappingType" value="">
			
								
		 	<!-- parser Advance details section start here -->
			<div class="fullwidth" id="advance_details_div">
			<div class="box box-warning">
	<div class="box-header with-border">
		<h3 class="box-title">
			<spring:message
				code="ascii.plugin.advance.details.heading.label" ></spring:message>
		</h3>
		<div class="box-tools pull-right">
			<button class="btn btn-box-tool" data-widget="collapse">
				<i class="fa fa-minus"></i>
			</button>
		</div>
		<!-- /.box-tools -->
	</div>
	<!-- /.box-header -->
	<div class="box-body inline-form ">
			
			<jsp:include page="../roamingComposerAdvanceDetail.jsp"></jsp:include>	
			 
			</div> 
			<!-- parser Advance details end here -->
			</div>
	<!-- /.box-body -->
</div>
		</form:form>		
	</div>
</div>

<!-- Customize mapping pop-up code start here  -->
   
    <a href="#customize_mapping_div" class="fancybox" style="display: none;" id="customize_mapping_link">#</a>
   <form:form modelAttribute="composer_mapping_form_bean" method="POST" action="<%= ControllerConstants.CREATE_UPDATE_MAPPING_DETAILS %>" id="create-mapping-form">
   		
	<input type="hidden" name="create_mapping_base_mapping_id"  id="create_mapping_base_mapping_id" value = " " />
	<input type="hidden" name="mappingActionType"  id="mappingActionType" value="CREATE"/>
	<input type="hidden" name="createMappingId"  id="createMappingId" value="0"/>
   	
   
    <div id="customize_mapping_div" style="width: 100%; display: none;">
			<div class="modal-content">
				<div class="modal-header padding10">
					<h4 class="modal-title">
						<spring:message code="parser.mapping.customize.header.label" ></spring:message>
					</h4>
				</div>
				<div class="modal-body padding10 inline-form">
					<div class="fullwidth"> <jsp:include page="../../../../common/responseMsgPopUp.jsp" ></jsp:include>	</div>
				
					<div class="form-group">
						<spring:message code="device.list.grid.device.type.label" var="tooltip" ></spring:message> 
						<div class="table-cell-label">${tooltip}</div>
						<div class="input-group">
							<input type="text" name="customize_selected_deviceType" id="customize_selected_deviceType" readonly="readonly" class="form-control table-cell input-sm" data-toggle="tooltip" data-placement="bottom" title="${tooltip}"> 
							<span class="input-group-addon add-on last"> <i	class="glyphicon glyphicon-alert" data-toggle="tooltip"	data-placement="left" title=""></i></span>
						</div>
					</div>
					<div class="form-group">
						<spring:message code="device.list.grid.device.vendor.type.label" var="tooltip" ></spring:message> 
						<div class="table-cell-label">${tooltip}</div>
						<div class="input-group">
							<input type="text" name="customize_selected_vendorType" id="customize_selected_vendorType" readonly="readonly" class="form-control table-cell input-sm" data-toggle="tooltip" data-placement="bottom" title="${tooltip}"> 
							<span class="input-group-addon add-on last"> <i	class="glyphicon glyphicon-alert" data-toggle="tooltip"	data-placement="left" title=""></i></span>
						</div>
					</div>
					<div class="form-group">
						<spring:message code="device.list.grid.device.name.label" var="tooltip" ></spring:message>
						<div class="table-cell-label">${tooltip}</div>
						<div class="input-group">
							<input type="text" name="customize_selected_deviceName" id="customize_selected_deviceName" readonly="readonly" class="form-control table-cell input-sm" data-toggle="tooltip" data-placement="bottom" title="${tooltip}"> 
							<span class="input-group-addon add-on last"> <i	class="glyphicon glyphicon-alert" data-toggle="tooltip"	data-placement="left" title=""></i></span>
						</div>
					</div>
					<div class="form-group">
						<spring:message code="device.list.grid.device.mapping.name.label" var="tooltip" ></spring:message> 
						<div class="table-cell-label">${tooltip}</div>
						<div class="input-group">
							<input type="text" name="customize_selected_mappingName" id="customize_selected_mappingName" readonly="readonly" class="form-control table-cell input-sm" data-toggle="tooltip" data-placement="bottom" title="${tooltip}"> 
							<span class="input-group-addon add-on last"> <i	class="glyphicon glyphicon-alert" data-toggle="tooltip"	data-placement="left" title=""></i></span>
						</div>
					</div>
					<div class="form-group" id="customize_mode_div" >
						<spring:message code="parser.mapping.customize.mode.mapping.label" var="tooltip" ></spring:message> 
						<div class="table-cell-label">${tooltip}</div>
						<div class="input-group">
							 <input type="radio" name="customizeMode" id="create_new_mapping" style="padding-right: 15px;" value="create" onclick="validateMapingOptions('create');"/>&nbsp;&nbsp;<spring:message code="parser.mapping.customize.mode.create.new.label" ></spring:message>
	             			&nbsp;&nbsp;&nbsp;<input type="radio" name="customizeMode" id="update_mapping" style="padding-right: 15px;" value="update" onclick="validateMapingOptions('edit');" />&nbsp;&nbsp;<spring:message code="parser.mapping.customize.mode.update.label" ></spring:message>
						</div>
					</div>
					<div class="form-group" id="base_mapping_div" style="display: none;" >
						<spring:message code="parser.mapping.customize.base.mapping.label" var="tooltip" ></spring:message>
						<div class="table-cell-label">${tooltip}</div>
						<div class="input-group">
							<select  name = "base_mapping_name" class="form-control table-cell input-sm"  tabindex="3" id="base_mapping_name" data-toggle="tooltip" data-placement="bottom"  title="${tooltip }"  >
			             		<%-- <option  value="0" selected="selected"><spring:message code="mapping.name.dropdown.default.selection.label" ></spring:message></option> --%>
			             	</select> 
							<span class="input-group-addon add-on last"> <i	class="glyphicon glyphicon-alert" data-toggle="tooltip"	data-placement="left" title=""></i></span>
						</div>
					</div>
					<div class="form-group" id="customize_new_mapping_div" >
						<spring:message code="parser.mapping.customize.new.mapping.label" var="tooltip" ></spring:message> 
						<div class="table-cell-label">${tooltip}</div>
						<div class="input-group">
							<input type="text" name="name" id="name" class="form-control table-cell input-sm" data-toggle="tooltip" data-placement="bottom" title="${tooltip}"> 
							<span class="input-group-addon add-on last"> <i	class="glyphicon glyphicon-alert" data-toggle="tooltip"	data-placement="left" title=""></i></span>
						</div>
					</div>
					<div class="form-group" id="selected_mapping_association_details_div">
					</div>
				</div>
				<div class="modal-footer padding10" id="create_mapping_button_div">
					<sec:authorize access="hasAuthority('UPDATE_PARSER')">
					<button id="createupdateMappingBtn" type="button" class="btn btn-grey btn-xs "	data-dismiss="modal" onclick="createOrUpdateMappingDetails();"><spring:message code="btn.label.submit" ></spring:message></button>
					</sec:authorize>
					<button id="cancelupdateMappingBtn" type="button" class="btn btn-grey btn-xs "	data-dismiss="modal" onclick="closeFancyBox(); setReadOnlyFlagOnCancelBtn();"><spring:message code="btn.label.cancel" ></spring:message></button>
				</div>
				<div class="modal-footer padding10" id="close-create-mapping-button-div" style="display:none;">
		            <button type="button" class="btn btn-grey btn-xs " onclick="closeFancyBox();"><spring:message code="btn.label.close"></spring:message></button>
		        </div>
		        <div id="create_mapping_proccess_bar_div" class="modal-footer padding10 text-left" style="display: none;">
					<jsp:include page="../../../../common/processing-bar.jsp"></jsp:include>
				</div>
				
				
			</div>
			<!-- /.modal-content -->
		</div>
    </form:form>
    
    <!-- Customize mapping pop-up code end here  -->
    
<script type="text/javascript">

$(document).on("change","#vendorType",function(event) {
	   resetWarningDisplay();
	   clearAllMessages();
	   disableRoamingAdvanceDetails();
	   var deviceTypeId = $('#deviceType').val();
	   var vendorId = $('#vendorType').val();
	getDeviceByVendorName('<%=ControllerConstants.GET_DEVICE_LIST_BY_VENDOR%>', deviceTypeId, vendorId,'deviceName');
});	

$(document).on("change","#deviceName",function(event) {
	   resetWarningDisplay();
	   clearAllMessages();	
	   disableRoamingAdvanceDetails();
	   var deviceId = $('#deviceName').val();
	   var pluginType = '${plugInType}';
	   getMappingByDeviceAndComposerType('<%=ControllerConstants.GET_COMPOSER_MAPPING_LIST_BY_DEVICE%>',deviceId,pluginType,'mappingName','device_block');
	   onMappingNameChange();
});	

$(document).on("change","#mappingName",function(event) {
	onMappingNameChange();
});

function onMappingNameChange() {
	resetWarningDisplay();
	clearAllMessages();
	disableRoamingAdvanceDetails();
	var mappingId = $('#mappingName').val();
	$("#actionType").val('NO_ACTION');
	var pluginType = '${plugInType}';
    getMappingDetails('<%=ControllerConstants.GET_COMPOSER_MAPPING_DETAILS_ID%>',mappingId,pluginType);
}

$(document).on("change","#base_mapping_name",function(event) {
	 $("#create_mapping_base_mapping_id").val($("#base_mapping_name").val());
	   
});

$(document).ready(function() {
	
	
});


function setTapComposerAdvanceDetail(responseObject){
	$("#recMainAttribute").val(responseObject.recMainAttribute);
	$("#startFormat").val(responseObject.startFormat);
}
	

</script>
