<%@page import="com.elitecore.sm.common.constants.EngineConstants"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"   pageEncoding="UTF-8"%>
<%@page import="com.elitecore.sm.util.MapCache"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@page import="com.elitecore.sm.common.constants.SystemParametersConstant"%>
<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/security/tags" 	prefix="sec"  %>
<%@taglib 	uri="http://www.springframework.org/tags" 			prefix="spring"%>
<%@taglib uri="/WEB-INF/ShowErrorTag.tld" prefix="elitecoreError" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<div class="tab-content no-padding  mtop15">
	<div class="fullwidth mtop15" id="attribute_details_div">
		<!-- Basic details block start here -->
		<div class="box box-warning" id="attribute_basic_details_div">
			<div class="box-header with-border">
				<h3 class="box-title">
					<spring:message
						code="netflowConfiguration.plugin.basic.details.heading.label" ></spring:message>
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
				<div class="col-md-6 no-padding">
					<spring:message code="device.list.grid.device.name.label"
						var="tooltip" ></spring:message>
					<div class="form-group">
						<div class="table-cell-label">${tooltip}</div>
						<div class="input-group ">
							<input type="text" id="selDeviceName" name="selDeviceName"
								readonly="readonly" class="form-control table-cell input-sm"
								data-toggle="tooltip" value="${deviceName}"
								data-placement="bottom" title="${tooltip }"> <span
								class="input-group-addon add-on last"> <i
								class="glyphicon glyphicon-alert" data-toggle="tooltip"
								data-placement="left" title=""></i>
							</span>
						</div>
					</div>
				</div>
				<div class="col-md-6 no-padding">
					<spring:message code="device.list.grid.device.mapping.name.label"
						var="tooltip" ></spring:message>
					<div class="form-group">
						<div class="table-cell-label">${tooltip}</div>
						<div class="input-group ">
							<input type="text" id="selMappingName" name="selMappingName"
								readonly="readonly" class="form-control table-cell input-sm"
								data-toggle="tooltip" value="${mappingName}"
								data-placement="bottom" title="${tooltip }"> <span
								class="input-group-addon add-on last"> <i
								class="glyphicon glyphicon-alert" data-toggle="tooltip"
								data-placement="left" title=""></i>
							</span>
						</div>
					</div>
				</div>
	<%-- 			<div class="col-md-6 no-padding">
					<spring:message
						code="parserConfiguration.natflow.upload.type.label" var="tooltip" ></spring:message>
					<div class="form-group">
						<div class="table-cell-label">${tooltip}</div>
						<div class="input-group ">

							<input type="radio" name="natflowUploadType" id="natflowTemplate"
								value="true" />&nbsp;&nbsp;
							<spring:message
								code="parserConfiguration.natflow.upload.type.natflow.template.label" ></spring:message>
							&nbsp;&nbsp;&nbsp;<input type="radio" name="natflowUploadType"
								id="mappingSheet" value="false" />&nbsp;&nbsp;
							<spring:message
								code="parserConfiguration.natflow.upload.type.mapping.sheet.label" ></spring:message>
							<span class="input-group-addon add-on last"> <i
								class="glyphicon glyphicon-alert" data-toggle="tooltip"
								data-placement="left" title=""></i>
							</span>
						</div>
					</div>
				</div>
				<div class="col-md-6 no-padding">
					<spring:message
						code="parserConfiguration.natflow.upload.sample.label"
						var="tooltip" ></spring:message>
					<div class="form-group">
						<div class="table-cell-label">${tooltip}</div>
						<div class="input-group ">
							<textarea rows="3" cols="56" id="uploadedSampleTemplate"
								name="uploadedSampleTemplate" class="form-control input-sm"
								title="${tooltip}" data-toggle="tooltip" data-placement="bottom"
								readonly="true"></textarea>
							<span class="input-group-addon add-on last"> <i
								class="glyphicon glyphicon-alert" data-toggle="tooltip"
								data-placement="left" title=""></i>
							</span>
						</div>
					</div>
				</div> --%>
			</div>
			<!-- /.box-body -->
		</div>
		<!-- Basic details block end here -->
		<!-- Attribute div code start here -->
		<div class="fullwidth" id="add_delete_attribute_link_div">
			<div class="title2">
				<spring:message code="parserConfiguration.attribute.grid.heading.label" ></spring:message>
				<span class="title2rightfield"> 
				<sec:authorize access='hasAuthority(\'CREATE_PARSER\')'>
				<span class="title2rightfield-icon1-text">
				     	<a href="#" onclick="displayAddEditPopup('ADD','0');">
						<i class="fa fa-plus-circle"></i>
					</a> 
					<a href="#" onclick="displayAddEditPopup('ADD','0');"> 
						<spring:message code="parserConfiguration.attribute.grid.addattribute.label" ></spring:message>
					</a> 
					<a href="#divAddAttribute" class="fancybox" style="display: none;" id="add_edit_attribute">#</a>
				</span> 
				</sec:authorize>
				<sec:authorize access='hasAuthority(\'DELETE_PARSER\')'>
				<span class="title2rightfield-icon1-text"> 
					<a href="#" onclick="displayDeleteAttributePopup();">
						<i class="fa fa-trash"></i>
					</a> 
					<a href="#" onclick="displayDeleteAttributePopup();"> 
						<spring:message code="parserConfiguration.attribute.grid.deleteattribute.label" ></spring:message>
					</a> 
					<a href="#divDeleteAttribute" class="fancybox" style="display: none;" id="delete_attribute">#</a>
				</span>
				</sec:authorize>
				<span class="title2rightfield-icon1-text">
					<a onclick="addColNames('#natflowAttributeList');downloadCSVFile();">
						<i class="fa fa-download"></i>
					</a> 
					<a href="#" onclick="addColNames('#natflowAttributeList');downloadCSVFile();">
						<spring:message code="parserConfiguration.attribute.grid.exportattribute.label" ></spring:message>
					</a>
				</span> 
				<sec:authorize access='hasAuthority(\'CREATE_PARSER\')'>
				<span class="title2rightfield-icon1-text"> 
					<a href="#" onclick="openUploadAttrPopup('#natflowAttributeList');">
						<i class="fa fa-upload"></i>
					</a> 
				    <a href="#" onclick="openUploadAttrPopup('#natflowAttributeList');">
						<spring:message code="parserConfiguration.attribute.grid.uploadattribute.label" ></spring:message>
					</a> 
				</span>
				</sec:authorize> 
				</span>
			</div>
		</div>
		<div class="col-md-12 inline-form no-padding" id="attribute_grid_div">
			<div class="box-body table-responsive no-padding box"
				id="parser_attribute">
				<table class="table table-hover" id="natflowAttributeList"></table>
				<div id="natflowAttributeListDiv"></div>
				<div class="clearfix"></div>
				<div id="divLoading" align="center" style="display: none;">
					<img src="img/preloaders/Preloader_10.gif" />
				</div>
			</div>
		</div>
		<!-- Attribute div code start here -->

		<form:form modelAttribute="parser_attribute_form_bean" method="POST"
			action="<%= ControllerConstants.ADD_EDIT_PARSER_ATTRIBUTE %>"
			id="add-edit-attribute-form">
			<!-- Add/Edit Attribute from grid  pop-up code start here-->

			<input type="hidden" value="0" id="id" name="id" />
			<input type="hidden" value="${plugInType}" id="selplugInType"
				name="selplugInType" />
			<input type="hidden" value="${mappingId}" id="selConfigMappingId"
				name="selConfigMappingId" />


			<div id="divAddAttribute" style="display: none;">
				<jsp:include page="../addEditParserAttributePopUp.jsp"></jsp:include>
			</div>
		</form:form>
		
		<form action="<%=ControllerConstants.DOWNLOAD_EXCEL_PARSER_COMPOSER_ATTRIBUTE_LIST %>" method="post" id="downloadExcelFileForm">
			<input type="hidden" name="exportedMappingId" id="exportedMappingId"></input>
			<input type="hidden" name="plugInType" id="plugInType"></input>
			<input type="hidden" name="streamType" id="streamType" value="UPSTREAM"/>
		</form>
		
		<!-- Add/Edit Attribute from grid  pop-up code end here-->
		<!-- Delete warning message popup div start here -->
		<a href="#delete_msg_div" class="fancybox" style="display: none;"
			id="delete_warn_msg_link">#</a>
		<div id="delete_msg_div" style="width: 100%; display: none;">
			<div class="modal-content">
				<div class="modal-header padding10">
					<h4 class="modal-title">
						<spring:message code="device.delete.popup.warning.heading.label" ></spring:message>
					</h4>
				</div>
				<div class="modal-body padding10 inline-form">
					<p id="moreWarn">
						<spring:message code="attribute.multiple.selection.warning.label" ></spring:message>
					</p>
					<p id="lessWarn">
						<spring:message code="attribute.no.selection.warning.label" ></spring:message>
					</p>
				</div>
				<div class="modal-footer padding10">
					<button type="button" class="btn btn-grey btn-xs "
						data-dismiss="modal" onclick="closeFancyBox();">
						<spring:message code="btn.label.close" ></spring:message>
					</button>
				</div>
			</div>
			<!-- /.modal-content -->
		</div>
		<!-- Delete warning message popup  div end here-->

		<!-- Delete attribute popup div start here -->
		<div id="divDeleteAttribute" style="width: 100%; display: none;">
			<div class="modal-content">
				<div class="modal-header padding10">
					<h4 class="modal-title">
						<spring:message code="parser.attribute.delete.heading.label" ></spring:message>
					</h4>
				</div>
				<div class="modal-body padding10 inline-form">
					<div class=fullwidth>
						<jsp:include page="../../../common/responseMsgPopUp.jsp" ></jsp:include>
					</div>
					<div class="fullwidth">
						<input type="hidden" value=""  id="deleteAttributeId" name="deleteAttributeId" />
							<div id="delete_selected_attribute_details">
													
							</div>
							<div>
								<spring:message code="attribute.delete.warning.message" ></spring:message>	
							</div>
					</div>
				</div>
				<sec:authorize access="hasAuthority('UPDATE_PARSER')">
					<div id="delete_attribute_bts_div" class="modal-footer padding10">
						<button type="button" class="btn btn-grey btn-xs" id="natflow_del_btn" 
							onclick="deleteAttribute();">
							<spring:message code="btn.label.delete" ></spring:message>
						</button>
						<button type="button" class="btn btn-grey btn-xs"
							onclick="closeFancyBox();">
							<spring:message code="btn.label.cancel" ></spring:message>
						</button>
					</div>
					<div class="modal-footer padding10"
						id="delete_close_attribute_buttons_div" style="display: none;">
						<button type="button" class="btn btn-grey btn-xs " id="closeDeleteAttributePopup"
							onclick="closeFancyBox();reloadAttributeGridData();">
							<spring:message code="btn.label.close" ></spring:message>
						</button>
					</div>
				</sec:authorize>
				<div id="delete_attribute_progress_bar_div"
					class="modal-footer padding10 text-left" style="display: none;">
					<jsp:include page="../../../common/processing-bar.jsp"></jsp:include>
				</div>
			</div>
		</div>
		<!-- Delete attribute popup div end here -->
	</div>
</div>

<!-- MED-8969 START : Attribute upload screen -->
<a href="#divuploadAttrData" class="fancybox" style="display: none;" id="uploadAttrData">#</a>
 		<div id="divuploadAttrData" style=" width:100%; display: none;" >
 		  <jsp:include page="../uploadParserAttrPopup.jsp"></jsp:include>
       </div>
<!-- MED-8969 END : Attribute upload screen --> 

<script type="text/javascript">
$(document).ready(function() {
	
	var flag = '${readOnlyFlag}';
	var selMapName ='${mappingName}';
	selMappingId = '${mappingId}';
	var parserType='${parserType}';
	var selectedMappingType = '${selectedMappingType}';
	urlAction = '<%=ControllerConstants.GET_ATTRIBUTE_GRID_LIST_BY_MAPPING_ID%>';
	
	createAttributeGrid('<%=MapCache.getConfigValueAsInteger(SystemParametersConstant.TOTAL_ROWS_TO_DISPLAY_IN_GRID, 10)%>');
	getAllAttributeListByMappingId(urlAction,selMappingId);
	
	if(flag =='true' || selectedMappingType == '0' ){
		disableAttributeDetail();
	}
	$("#natflow_attribute_form #selAttributeMappingName").val('${mappingName}');
	$("#natflow_attribute_form #selAttributeMappingId").val('${mappingId}');
	$("#natflow_attribute_form #selDeviceName").val('${deviceName}');
});

function updateNatFlowAttributeOrder(){
	console.log("updateOrder Called");
	var ids = $('#natflowAttributeList').jqGrid('getDataIDs');
	window.scrollTo(0, 0);
	resetWarningDisplay();
	clearAllMessages();
	showErrorMsg("<spring:message code='parser.attribute.ordering.wait.label' ></spring:message>");
	$.ajax({
		url: 'reorderAttributeGridListByMappingId',
		cache: false,
		async: true,
		dataType: 'json',
		type: "POST",
		data: {
			'mappingId':'${mappingId}',
			'plugInType':'${plugInType}',
			'attributeIds':ids.toString(),
		},
		success: function(data){
			var response = eval(data);
			var responseObject = response.object;
			var responseMsg = response.msg;
			if(response.code=='200'){
				resetWarningDisplay();
    			clearAllMessages();
				showSuccessMsg(responseMsg);
			}else if(responseObject != undefined && responseObject != 'undefined' && response.code == "400"){
				resetWarningDisplay();
				clearAllMessages();
				addErrorIconAndMsgForAjax(responseObject);
			}
			
		},
	    error: function (xhr,st,err){
			handleGenericError(xhr,st,err);
		}
	});
}
function downloadCSVFile() {
	$("#sampleRequired").val('NO');
	$("#sample_lookup_table_form").submit();
}


</script>
