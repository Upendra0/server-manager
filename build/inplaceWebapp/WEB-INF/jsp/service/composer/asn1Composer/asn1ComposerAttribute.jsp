<%@page import="com.elitecore.sm.common.constants.BaseConstants"%>
<%@page import="com.elitecore.sm.common.model.DecodeTypeEnum"%>
<%@page import="com.elitecore.sm.common.constants.FormBeanConstants"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="com.elitecore.sm.util.MapCache"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@page
	import="com.elitecore.sm.common.constants.SystemParametersConstant"%>
<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<%@page import="com.elitecore.sm.common.constants.EngineConstants"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/security/tags"
	prefix="sec"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="/WEB-INF/ShowErrorTag.tld" prefix="elitecoreError"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<script type="text/javascript">


$(document).ready(function() {
	
	var destDateFormat=$("#destDateFormat").val();
	var test="";
	var dateFormatEnumArray= new Array();
	var dateFormatEnumVal = document.getElementById('dateFormatEnum');
	for (i = 0; i < dateFormatEnumVal.options.length; i++) {
		dateFormatEnumArray[i] = dateFormatEnumVal .options[i].value;
		if(destDateFormat==dateFormatEnumVal .options[i].value){
			test=destDateFormat;
		}
	}
	if(test!=""){
		$('#dateFormatEnum').val(destDateFormat);
	}
	else{
		$('#dateFormatEnum').val('Other');
	}
	
	
});

	



var attributeSeqNo=0;
</script>
<div class="tab-content no-padding  mtop15">
	<div class="fullwidth mtop15" id="attribute_details_div">
		<form:form modelAttribute="<%=FormBeanConstants.ASN1_COMPOSER_MAPPING_FORM_BEAN %>" method="POST"
			id="ascii_attribute_configuration_form_bean">
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
								<form:input path="name"
									cssClass="form-control table-cell input-sm" tabindex="4"
									id="mapping-name" data-toggle="tooltip" data-placement="bottom"
									title="${tooltip }" ></form:input>
								<spring:bind path="name">
									<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
								</spring:bind>
							</div>
						</div>
					</div>
				
				<div class="col-md-6 no-padding">
							
					<spring:message code="ascii.composer.destionation.date.format" var="label"></spring:message>
					<spring:message code="ascii.composer.destionation.date.format.tooltip" var="tooltip"></spring:message>
						<div class="form-group">
								
							<div class="table-cell-label">${label}<span class="required">*</span></div>
		                    	<div class="input-group">
		                         <form:select path="dateFormatEnum"
									cssClass="form-control table-cell input-sm" tabindex="4"
									id="dateFormatEnum" data-toggle="tooltip"
									data-placement="bottom" title="${tooltip }">
									<c:forEach items="${sourceDateFormatEnum}" var="sourceDateFormatEnum">
										<form:option value="${sourceDateFormatEnum.name}">${sourceDateFormatEnum.name}</form:option>
									</c:forEach>
								</form:select>
								<spring:bind path="dateFormatEnum">
									<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
								</spring:bind>
								 <form:input path="destDateFormat" cssClass="form-control table-cell input-sm" tabindex="4" id="destDateFormat" data-toggle="tooltip" data-placement="bottom" title="${tooltip }"></form:input>
									<spring:bind path="destDateFormat">
										<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
									</spring:bind>
								
		                          </div>
		                   </div> 	
        			</div>
					<div class="col-md-6 no-padding">
						<spring:message code="ascii.composer.destionation.charset" var="label" ></spring:message>
						<spring:message code="ascii.composer.destionation.charset.tooltip" var="tooltip" ></spring:message>
						<div class="form-group">
							<div class="table-cell-label">${label}<span
									class="required">*</span>
							</div>
							<div class="input-group ">
								<form:select path="destCharset"
									cssClass="form-control table-cell input-sm" tabindex="4"
									id="destCharset" data-toggle="tooltip"
									data-placement="bottom" title="${tooltip }">
									<c:forEach items="${sourceCharsetName}" var="sourceCharsetName">
										<form:option value="${sourceCharsetName}">${sourceCharsetName}</form:option>
									</c:forEach>
								</form:select>
								<spring:bind path="destCharset">
									<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
								</spring:bind>
							</div>
						</div>
					</div>
					<div class="col-md-6 no-padding">
						<spring:message code="ascii.composer.destination.file.extension" var="label" ></spring:message>
						<spring:message code="ascii.composer.destination.file.extension.tooltip" var="tooltip" ></spring:message>
						<div class="form-group">
							<div class="table-cell-label">${label}</div>
							<div class="input-group ">
								<form:input path="destFileExt"
									cssClass="form-control table-cell input-sm" tabindex="4"
									id="destFileExt" data-toggle="tooltip" data-placement="bottom"
									title="${tooltip }" ></form:input>
								<spring:bind path="destFileExt">
									<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
								</spring:bind>
							</div>
						</div>
					</div>
					
					<div class="col-md-6 no-padding">
						<spring:message code="asn1.plugin.multi.container.delimiter" var="label" ></spring:message>
						<spring:message code="asn1.plugin.multi.container.delimiter" var="tooltip" ></spring:message>
						<div class="form-group">
							<div class="table-cell-label">${label}</div>
							<div class="input-group ">
								<form:input path="multiContainerDelimiter"
									cssClass="form-control table-cell input-sm" tabindex="4"
									id="multiContainerDelimiter" data-toggle="tooltip" data-placement="bottom"
									title="${tooltip }" ></form:input>
								<spring:bind path="multiContainerDelimiter">
									<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
								</spring:bind>
							</div>
						</div>
					</div>
					
					
					<div class="clearfix"></div>
					<div class='col-md-6 no-padding'> 
							<div class='form-group'> 
								<div id='buttons-div' class='input-group'> 
									<sec:authorize access='hasAuthority(\'UPDATE_COMPOSER\')'>
										<button type='button' class='btn btn-grey btn-xs' id='submitbtn' onclick='addBasicDetail();'><spring:message code='btn.label.update'></spring:message></button>&nbsp;
									</sec:authorize> 
									<button type='button' class='btn btn-grey btn-xs' id='resetbtn' onclick=""><spring:message code='btn.label.reset'></spring:message></button>&nbsp;  
								</div> 
								<div id="progress-bar-div" class="input-group" style="display: none;">
									<jsp:include page="../../../common/processing-bar.jsp"></jsp:include>
								</div>
							</div>  
						</div>
				</div>
				<!-- /.box-body -->
			</div>
		</form:form>
		<!-- Basic details block end here -->
		<!-- Attribute div code start here -->
		<div class="fullwidth" id="add_delete_attribute_link_div">
			<div class="title2">
				<spring:message code="parserConfiguration.attribute.grid.heading.label" ></spring:message>
				<span class="title2rightfield"> 
				<sec:authorize access='hasAuthority(\'CREATE_COMPOSER\')'>
					<span class="title2rightfield-icon1-text"> 
					<a href="#" onclick="displayAddEditPopup('ADD','0');">
						<i class="fa fa-plus-circle"></i>
					</a> 
					<a id="addAttr_lnk" href="#" onclick="displayAddEditPopup('ADD','0');"> 
						<spring:message code="parserConfiguration.attribute.grid.addattribute.label" ></spring:message>
					</a> 
					<a href="#divAddAttribute" class="fancybox" style="display: none;" id="add_edit_attribute">#</a>
				</span> 
				</sec:authorize>
				<sec:authorize access='hasAuthority(\'DELETE_COMPOSER\')'>
				<span class="title2rightfield-icon1-text"> 
					<a id="delete_attribute_btn" href="#" onclick="displayDeleteAttributePopup();">
						<i class="fa fa-trash"></i>
					</a> 
					<a href="#" onclick="displayDeleteAttributePopup();"> 
						<spring:message code="parserConfiguration.attribute.grid.deleteattribute.label" ></spring:message>
					</a> 
					<a href="#divDeleteAttribute" class="fancybox" style="display: none;" id="delete_attribute">#</a>
				</span>
				</sec:authorize>
				<span class="title2rightfield-icon1-text">
				<a onclick="addColNames('#asciiAttributeList');downloadCSVFile();">
							<i class="fa fa-download"></i>
					</a> 
					<a href="#" onclick="addColNames('#asciiAttributeList');downloadCSVFile();">
							<spring:message code="parserConfiguration.attribute.grid.exportattribute.label" ></spring:message>
					</a>
				</span> 				
				<sec:authorize access='hasAuthority(\'CREATE_COMPOSER\')'>
				 <span class="title2rightfield-icon1-text"> 
					<a href="#" onclick="openUploadAttrPopup('#asciiAttributeList');">
						<i class="fa fa-upload"></i>
					</a> 
				    <a href="#" onclick="openUploadAttrPopup('#asciiAttributeList');">
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
				<table class="table table-hover" id="asciiAttributeList"></table>
				<div id="asciiAttributeListDiv"></div>
				<div class="clearfix"></div>
				<div id="divLoading" align="center" style="display: none;">
					<img src="img/preloaders/Preloader_10.gif" />
				</div>
			</div>
		</div>
 		<!-- Attribute div code start here -->

		<form:form modelAttribute="composer_attribute_form_bean" method="POST"
			action="<%=ControllerConstants.ADD_EDIT_ASN1_COMPOSER_ATTRIBUTE%>"
			id="add-edit-attribute-form">
			<!-- Add/Edit Attribute from grid  pop-up code start here-->

			<input type="hidden" value="0" id="id" name="id" />
			<input type="hidden" value="${plugInType}" id="selplugInType"
				name="selplugInType" />
			<input type="hidden" value="${mappingId}" id="selConfigMappingId"
				name="selConfigMappingId" />

			 <div id="divAddAttribute" style="display: none;">
				 <jsp:include page="../addEditViewComposerAttributePopUp.jsp"></jsp:include> 
			</div>
		</form:form>
				
		<form action="<%= ControllerConstants.DOWNLOAD_SAMPLE_COMPOSER_ATTRIBUTE%>" method="POST" id="sample_lookup_table_form">
			<input type="hidden" id="parserType" name="parserType" value='${plugInType}'/>
			<input type="hidden" id="mappingId" name="mappingId" value='${mappingId}'/>
			<input type="hidden" id="sampleRequired" name="sampleRequired" />
			<input type="hidden" id="colNamesFromGrid" name="colNamesFromGrid"/>
			<input type="hidden" id="requestActionType" name="requestActionType" value='${REQUEST_ACTION_TYPE}'/>
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
					<div id="deletePopUpMsg" class=fullwidth>
						<jsp:include page="../../../common/responseMsgPopUp.jsp" ></jsp:include>
					</div>
					<div class="fullwidth">
						<input type="hidden" value="" id="deleteAttributeId"
							name="deleteAttributeId" />
						<div id="delete_selected_attribute_details"></div>
						<div>
							<spring:message code="attribute.composer.delete.warning.message" ></spring:message>
						</div>
					</div>
				</div>
				<sec:authorize access="hasAuthority('DELETE_COMPOSER')">
					<div id="delete_attribute_bts_div" class="modal-footer padding10">
						<button id="delete_confirm" type="button" class="btn btn-grey btn-xs"
							onclick="deleteAttribute();">
							<spring:message code="btn.label.delete" ></spring:message>
						</button>
						<button type="button" class="btn btn-grey btn-xs"
							onclick="closeFancyBox();">
							<spring:message code="btn.label.cancel" ></spring:message>
						</button>
					</div>
					</sec:authorize>
					<div class="modal-footer padding10"
						id="delete_close_attribute_buttons_div" style="display: none;">
						<button type="button" class="btn btn-grey btn-xs " id="closeDeleteAttributePopup"
							onclick="closeFancyBox();reloadAttributeGridData();">
							<spring:message code="btn.label.close" ></spring:message>
						</button>
					</div>
				
				<div id="delete_attribute_progress_bar_div"
					class="modal-footer padding10 text-left" style="display: none;">
					<jsp:include page="../../../common/processing-bar.jsp"></jsp:include>
				</div>
			</div>
		</div>
		<!-- Delete attribute popup div end here -->
	</div>
	<form:form modelAttribute="<%=FormBeanConstants.ASN1_COMPOSER_MAPPING_FORM_BEAN %>" method="POST" action="NO_ACTION" id="ascii-composer-configuration-form">
			
			<input type="hidden" value="<%=DecodeTypeEnum.UPSTREAM%>" name="decodeType" id="decodeType" />
			<input type="hidden" value="${plugInId}" name="plugInId" id="plugInId" />
			<input type="hidden" value="${plugInName}" name="plugInName" id="plugInName" />
			<input type="hidden" value="${plugInType}" name="plugInType" id="plugInType" />
			<input type="hidden" id="driverId" name="driverId" value="${driverId}"/>
	    	<input type="hidden" id="driverName" name="driverName" value="${driverName}"/>
	    	<input type="hidden" id="driverTypeAlias" name="driverTypeAlias" value="${driverTypeAlias}"/>
			<input type="hidden" id="serviceId" name="serviceId" value="${serviceId}">
			<input type="hidden" id="serviceType" name="serviceType" value="${serviceType}" />	
			<input type="hidden" id="serviceName" name="serviceName" value="${serviceName}">
			<input type="hidden" id="instanceId" name="instanceId" value="${instanceId}">
			<input type="hidden" id="serviceInstanceId" name="serviceInstanceId" value="${serviceInstanceId}">
			<input type="hidden" id="id" name="id" value="${mappingId}">
			<input type="hidden" id="selecteMappingName" name="selecteMappingName" value="${mappingName}">
			<input type="hidden" id="selecteDeviceName" name="selecteDeviceName" value="${deviceName}">
			<input type="hidden" id="selDeviceId" name="selDeviceId" value="">
			<input type="hidden" id="selMappingId" name="selMappingId" value="${mappingId}">
			<input type="hidden" id="selVendorTypeId" name="selVendorTypeId" value="">
			<input type="hidden" id="selDeviceTypeId" name="selDeviceTypeId" value="">
				
			<input type="hidden" value="NO_ACTION" name="actionType" id="actionType" />	
			<input type="hidden" value="false" name="readOnlyFlag" id="readOnlyFlag" />
			<input type="hidden" name="nextAttributeType" id="nextAttributeType" />	
			<!-- parser Advance details end here -->
		</form:form>		
</div>

<a href="#divuploadAttrData" class="fancybox" style="display: none;" id="uploadAttrData">#</a>
<div id="divuploadAttrData" style=" width:100%; display: none;" >
	<jsp:include page="../uploadComposerAttrPopup.jsp"></jsp:include>
</div> 

<script type="text/javascript">
$(document).ready(function() {

	$("#selDeviceName").val('${deviceName}');
	$("#mapping-name").val('${mappingName}');
	$("#selDeviceName").prop('disabled',true);
	$("#mapping-name").prop('disabled',true);
	var selectedMappingType = '${selectedMappingType}';
	
	var dateFormat=$("#dateFormatEnum").find(":selected").val();
	if(dateFormat != 'Other'){
		$("#destDateFormat").val('');
		$("#destDateFormat").attr('readOnly',true);
	}
	
	var flag = '${readOnlyFlag}';
	var selMapName ='${mappingName}';
	selMappingId = '${mappingId}';
	urlAction = '<%=ControllerConstants.GET_COMPOSER_ATTRIBUTE_GRID_LIST_BY_MAPPING_ID%>';
	var composerType='${plugInType}';
	var attributeListType = '${REQUEST_ACTION_TYPE}';
	$("#attributeTypeHidden").val(attributeListType);
	if('${REQUEST_ACTION_TYPE}' == 'ASN1_HEADER_COMPOSER_ATTRIBUTE' || '${REQUEST_ACTION_TYPE}' == 'ASN1_TRAILER_COMPOSER_ATTRIBUTE' ){
		$("#attribute_basic_details_div").hide();	
	}
	createAsn1AttributeGrid('<%=MapCache.getConfigValueAsInteger(SystemParametersConstant.TOTAL_ROWS_TO_DISPLAY_IN_GRID, 10)%>');
	getAllAsciiAttributeListByMappingId(urlAction, selMappingId);
 	var nextActionType = '';
	if(attributeListType == '<%= BaseConstants.ASN1_COMPOSER_ATTRIBUTE %>'){
		nextActionType = '<%= BaseConstants.ASN1_HEADER_COMPOSER_ATTRIBUTE %>';
	}else if(attributeListType == '<%= BaseConstants.ASN1_HEADER_COMPOSER_ATTRIBUTE %>'){
		nextActionType = '<%= BaseConstants.ASN1_TRAILER_COMPOSER_ATTRIBUTE %>';
	} 	
	$("#nextAttributeType").val(nextActionType);
	if(flag =='true' || selMapName == 'DEFAULT' ){
		disableAttributeDetail();
	}
});

$(document).on("change","#dateFormatEnum",function(event) {
	resetWarningDisplay();
	clearAllMessages();
	var dateFormat = $('#dateFormatEnum').val();
    	if(dateFormat == 'Other'){
    		$("#destDateFormat").val('');
		    $("#destDateFormat").attr('readOnly',false);
	   }else{
		   $("#destDateFormat").val('');
		   $("#destDateFormat").attr('readOnly',true);
	   }
});

function addBasicDetail(){
	resetWarningDisplay();
	clearAllMessages();
	$("#buttons-div").hide();
	$("#progress-bar-div").show();
	
	var deviceId='${deviceId}';
	var parserMappingId='${mappingId}';
	var composerType='${plugInType}';
	var oMyForm = new FormData();
	
	var dateFormat=$("#dateFormatEnum").find(":selected").val();
	 if(dateFormat == 'Other'){
		oMyForm.append("destDateFormat", $("#destDateFormat").val());
	}else{
		oMyForm.append("destDateFormat", dateFormat);
	} 
	
	oMyForm.append("dateFormatEnum", $("#dateFormatEnum").find(":selected").val());
    oMyForm.append("destCharset", $("#destCharset").find(":selected").val());
    oMyForm.append("destFileExt", $("#destFileExt").val());
    oMyForm.append("multiContainerDelimiter", $("#multiContainerDelimiter").val());
    oMyForm.append("id",parserMappingId);  
    oMyForm.append("name",$("#mapping-name").val());
    oMyForm.append("device.id",deviceId);
    oMyForm.append("composerType.alias", composerType);
    
    
    $.ajax({
		url: '<%=ControllerConstants.UPDATE_ASN1_COMPOSER_MAPPING%>',
		data : oMyForm ,
        type : "POST",
        datatype: "json",
        processData: false, 
        contentType:false,
		 
		success: function(data){
			resetWarningDisplay();
			clearAllMessages();
			
			$("#buttons-div").show();
	    	$("#progress-bar-div").hide();

			var response = $.parseJSON(data);
			
			var responseCode = response.code; 
			var responseMsg = response.msg; 
			var responseObject = response.object;
			
	 		
			if(responseCode == "200"){
				resetWarningDisplay();
    			clearAllMessages();
				showSuccessMsg(responseMsg);
				
			}else if(responseObject != undefined && responseObject != 'undefined' && responseCode == "400"){
				
				resetWarningDisplay();
				clearAllMessages();
				addErrorIconAndMsgForAjax(responseObject);
			}
			
		 },
	    error: function (xhr,st,err){
	    	resetWarningDisplay();
			clearAllMessages();
	    	hideProgressBar(counter);
			handleGenericError(xhr,st,err);
		}
	});
}

/*Function will create JQGRID for ASN1 composer attribute list */
function createAsn1AttributeGrid(defaultRowNum){				
	 
	 $("#asciiAttributeList").jqGrid({	    	
	        datatype: "local",
	        colNames:[
                      "#",
                      "<input type='checkbox' id='selectAllAttribute' onclick='attributeHeaderCheckbox(event, this)'></input>",
                      "<spring:message code='regex.parser.attr.grid.sequence.no' ></spring:message>",
                      "<spring:message code='composer.attr.grid.destination.field.name' ></spring:message>", 
                      "<spring:message code='regex.parser.attr.grid.unified.field.name' ></spring:message>",
                      "<spring:message code='regex.parser.attr.grid.descripation' ></spring:message>",
                      "<spring:message code='parsing.service.add.attr.default.val' ></spring:message>",
                      "<spring:message code='parser.grid.date.format.label'></spring:message>",
                      "<spring:message code='parsing.service.add.attr.trim.char'></spring:message>",
                      "<spring:message code='composer.attr.grid.other.detail' ></spring:message>", 
                      "<spring:message code='regex.parser.attr.grid.edit' ></spring:message>",

                      "<spring:message code='asn1.plugin.attr.asn1.data.type' ></spring:message>",
	                  "<spring:message code='asn1.plugin.attr.argument.data.type' ></spring:message>",
	                  "<spring:message code='asn1.plugin.attr.destination.field.format' ></spring:message>",
                      "<spring:message code='asn1.plugin.attr.choice.id' ></spring:message>",
                      "<spring:message code='asn1.plugin.attr.child.attr' ></spring:message>",
                      "attributeOrder"
	                 ],
			colModel:[
			          {name: 'id', index: 'id',hidden:true,sortable:false,search : false},
			          {name: 'checkbox', index: 'checkbox',sortable:false, formatter:asnComposerAttributeCheckboxFormatter, align : 'center', width:'30%', search : false},
			          {name: 'sequenceNumber',hidden:true, index: 'sequenceNumber',sortable:true, search : false},			          
			          {name: 'destinationField', index: 'destinationField',sortable:false},
					  {name: 'unifiedField',index: 'unifiedField',sortable:true},
					  {name: 'description',index: 'description',sortable:false, search : false},
					  {name: 'defualtValue',index: 'defualtValue',sortable:false,hidden:true, search : false},
					  {name: 'dateFormat',index: 'dateFormat',sortable:false,hidden:true, search : false},					  
					  {name: 'trimchars',index: 'trimchars',sortable:false,hidden:true, search : false},
					  {name: 'otherDetail',index: 'otherDetail', align:'center',sortable:false , formatter : viewDetailFormatter, search : false},
					  {name: 'update',index: 'update', align:'center',sortable:false,formatter: updateFormatter, search : false},
					        
					  {name: 'asn1DataType',index: 'asn1DataType', align:'center',sortable:false,hidden:true, search : false},
					  {name: 'argumentDataType',index: 'argumentDataType', align:'center',sortable:false,hidden:true, search : false},
					  {name: 'destFieldDataFormat',index: 'destFieldDataFormat', align:'center',sortable:false,hidden:true, search : false},
					  {name: 'choiceId',index: 'choiceId', align:'center',sortable:false,hidden:true, search : false},
					  {name: 'childAttributes',index: 'childAttributes', align:'center',sortable:false,hidden:true, search : false},
					  {name: 'attributeOrder',index: 'attributeOrder',hidden:true, search : false}					  
	        ],
	        rowNum:defaultRowNum,
	        rowList:[10,20,60,100],
	        height: 'auto',
	        sortname: 'sequenceNumber',
	 		sortorder: "asc",
	        pager: '#asciiAttributeListDiv',
	        contentType: "application/json; charset=utf-8",
	        viewrecords: true,
	        //multiselect: true,
	        search:true,
	        ignoreCase: true,
	        timeout : 120000,
	        loadtext: 'Loading...',
	        caption: jsSpringMsg.caption,
	        beforeRequest:function(){
	            $(".ui-dialog-titlebar").hide();
	        }, 
	        beforeSend: function(xhr) {
	            xhr.setRequestHeader("Accept", "application/json");
	            xhr.setRequestHeader("Content-Type", "application/json");
	        },
	 		loadComplete: function(data) {
			}, 
			onPaging: function (pgButton) {
				clearResponseMsgDiv();
			},
			loadError : function(xhr,st,err) {
				handleGenericError(xhr,st,err);
			},			
			recordtext: jsSpringMsg.recordtext,
	        emptyrecords: jsSpringMsg.emptyrecords,
			loadtext: jsSpringMsg.loadtext,
			pgtext : jsSpringMsg.pgtext,
	 	}).navGrid("#asciiAttributeListDiv",{edit:false,add:false,del:false,search:true});
		$(".ui-jqgrid-titlebar").hide();
		$('#asciiAttributeList').jqGrid('filterToolbar',{
			stringResult: true,
			searchOperators: false,
			searchOnEnter: false, 
			defaultSearch: "cn"
	    });	
}

/*Function will load composer attributes to JQGRID */
function getAllAsciiAttributeListByMappingId(urlAction, selMappingId){
	
	$.ajax({
		url: urlAction,
		cache: false,
		async: false,
    	data:
    		{
    		   'mappingId':selMappingId,
			   'plugInType':'${plugInType}',
			   'skipPaging':'true',
			   'attributeType' :'${REQUEST_ACTION_TYPE}',
    		},
		dataType: 'json',
		type: "POST",
		success: function(data){						
			var response = eval(data);			
			var responseCode = response.code; 
			var responseMsg = response.msg; 
			var responseObject = response.object;			
			if(responseCode == "200"){				
				$("#asciiAttributeListDiv").show();				
				var attributes=eval(responseObject);				
				var attributeList=attributes['attributeList'];
				if(attributeList!=null && attributeList != 'undefined' && attributeList != undefined){					
					var gridArray = [];
					$.each(attributeList,function(index,attribute){		 				
		 				var rowData = {};
		 				rowData.id					= parseInt(attribute.id);
		 				rowData.checkbox			= attribute.checkbox;
		 				rowData.sequenceNumber		= parseInt(attribute.sequenceNumber);
		 				rowData.destinationField    = attribute.destinationField;
		 				rowData.unifiedField		= attribute.unifiedField;
		 				rowData.description			= attribute.description;
		 				rowData.defualtValue		= attribute.defualtValue;
		 				rowData.dateFormat		    = attribute.dateFormat;
		 				rowData.trimchars			= attribute.trimChar;
		 				
		 				rowData.asn1DataType		= attribute.asn1DataType;
		 				rowData.argumentDataType	= attribute.argumentDataType;
		 				rowData.destFieldDataFormat	= attribute.destFieldDataFormat;
		 				rowData.choiceId			= attribute.choiceId;
		 				rowData.childAttributes		= attribute.childAttributes;
		 				rowData.attributeOrder		= attribute.attributeOrder;

		 				gridArray.push(rowData);
		 			});
					jQuery("#asciiAttributeList").jqGrid('setGridParam', {datatype: 'local', data:gridArray }).trigger("reloadGrid");
				} 
				$('.fancybox-wrap').css('top','10px');
			}else if(responseObject != undefined && responseObject != 'undefined' && responseCode == "400"){
				console.log("got 400 response.");
			}
		},
	    error: function (xhr,st,err){	    		    	
			handleGenericError(xhr,st,err);
		}
	});
}

/*
 * checkbox formatter for all child checkbox
 */
function asnComposerAttributeCheckboxFormatter(cellvalue, options, rowObject) {
	var uniqueId = getUniqueCheckboxIdForAttribute(rowObject);
	return '<input type="checkbox" id="'+uniqueId+'" name="attributeCheckbox" value="'+rowObject["id"]+'" onchange="updateRootCheckbox(this, \'asciiAttributeList\')"/>';
}

/*Function will reload attribute grid after add/edit/delete action. */
function reloadAsciiAttributeGridData(){
	clearAllMessages();
	
	var $grid = $("#asciiAttributeList");
	jQuery('#asciiAttributeList').jqGrid('clearGridData');
	clearAttributeGrid();

	$grid.jqGrid('setGridParam',{datatype:'json',page: 1,sortname: 'id',sortorder: "asc", postData:{
	  
   		'mappingId': function () {
	        return selMappingId;
   		},
		'plugInType':'${plugInType}',
	}}).trigger('reloadGrid');
}
function downloadCSVFile() {
	$("#sampleRequired").val('NO');
	$("#sample_lookup_table_form").submit();
}
</script>
