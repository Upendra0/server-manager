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
<div class="tab-content no-padding  mtop15">
	<div class="fullwidth mtop15" id="attribute_details_div">
		<form:form modelAttribute="<%=FormBeanConstants.ROAMING_PARSER_MAPPING_FORM_BEAN %>" method="POST"
			id="ascii_attribute_configuration_form_bean">
			<!-- Basic details block start here -->
			<div class="box box-warning" id="attribute_basic_details_div" >
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
							
					<spring:message code="regex.parser.source.date.format" var="tooltip"></spring:message>
					<div class="form-group">
							
						<div class="table-cell-label">${tooltip}<span class="required">*</span></div>
	                    	<div class="input-group">
	                         <form:select path="dateFormat"
								cssClass="form-control table-cell input-sm" tabindex="4"
								id="dateFormat" data-toggle="tooltip"
								data-placement="bottom" title="${tooltip }">
								<c:forEach items="${sourceDateFormatEnum}" var="sourceDateFormatEnum">
									<form:option value="${sourceDateFormatEnum.name}">${sourceDateFormatEnum.name}</form:option>
								</c:forEach>
							</form:select>
							<spring:bind path="dateFormat">
								<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
							</spring:bind>
							 <form:input path="srcDateFormat" cssClass="form-control table-cell input-sm" tabindex="4" id="srcDateFormat" data-toggle="tooltip" data-placement="bottom" title="${tooltip }"></form:input>
								<spring:bind path="srcDateFormat">
									<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
								</spring:bind>
	                         </div>
	                  </div> 	
        			</div>
					<div class="clearfix"></div>
					<div class='col-md-6 no-padding'> 
						<div class='form-group'> 
							<div id='buttons-div' class='input-group'> 
								<sec:authorize access='hasAuthority(\'UPDATE_PARSER\')'>
									<button type='button' class='btn btn-grey btn-xs' id='submitbtn' onclick='addBasicDetail();'><spring:message code='btn.label.update'></spring:message></button>&nbsp;
								</sec:authorize> 
								<button type='button' class='btn btn-grey btn-xs' id='resetbtn' onclick="resetAttributeDetails()"><spring:message code='btn.label.reset'></spring:message></button>&nbsp;  
							</div> 
							<div id="progress-bar-div" class="input-group" style="display: none;">
								<jsp:include page="../../../../common/processing-bar.jsp"></jsp:include>
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
				<sec:authorize access='hasAuthority(\'CREATE_PARSER\')'>
					<span class="title2rightfield-icon1-text"> 
					<a href="#" onclick="displayAddEditPopup('ADD','0');">
						<i class="fa fa-plus-circle"></i>
					</a> 
					<a href="#" onclick="displayAddEditPopup('ADD','0');"> 
						<spring:message code="parserConfiguration.attribute.grid.add.attribute.label" ></spring:message>
					</a> 
					<a href="#divAddAttribute" class="fancybox" style="display: none;" id="add_edit_attribute">#</a>
					<a href="#divAdditionalInfoAttribute" class="fancybox" style="display: none;" id="view_attribute">#</a>
				
				</span> 
				</sec:authorize>
				<sec:authorize access='hasAuthority(\'DELETE_PARSER\')'>
				<span class="title2rightfield-icon1-text"> 
					<a href="#" onclick="displayDeleteAttributePopup();">
						<i class="fa fa-trash"></i>
					</a> 
					<a href="#" onclick="displayDeleteAttributePopup();"> 
						<spring:message code="parserConfiguration.attribute.grid.delete.attribute.label" ></spring:message>
					</a> 
					<a href="#divDeleteAttribute" class="fancybox" style="display: none;" id="delete_attribute">#</a>
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

		<form:form modelAttribute="parser_attribute_form_bean" method="POST"
			action="<%=ControllerConstants.ADD_EDIT_RAP_PARSER_ATTRIBUTE%>"
			id="add-edit-attribute-form">
			<!-- Add/Edit Attribute from grid  pop-up code start here-->

			<input type="hidden" value="0" id="id" name="id" />
			<input type="hidden" value="${plugInType}" id="selplugInType"
				name="selplugInType" />
			<input type="hidden" value="${mappingId}" id="selConfigMappingId"
				name="selConfigMappingId" />


			<div id="divAddAttribute" style="display: none;">
				<jsp:include page="../../addEditParserAttributePopUp.jsp"></jsp:include>
				
			</div>
			<div id="divAdditionalInfoAttribute" style="display: none;">
				<jsp:include page="../displayAdditionalInfoPopUp.jsp"></jsp:include>
				
			</div>
		</form:form>
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
						<jsp:include page="../../../../common/responseMsgPopUp.jsp" ></jsp:include>
					</div>
					<div class="fullwidth">
						<input type="hidden" value="" id="deleteAttributeId"
							name="deleteAttributeId" />
						<div id="delete_selected_attribute_details"></div>
						<div>
							<spring:message code="attribute.delete.warning.message" ></spring:message>
						</div>
					</div>
				</div>
				<sec:authorize access="hasAuthority('DELETE_PARSER')">
					<div id="delete_attribute_bts_div" class="modal-footer padding10">
						<button id="asn1_del_btn" type="button" class="btn btn-grey btn-xs"
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
					<jsp:include page="../../../../common/processing-bar.jsp"></jsp:include>
				</div>
			</div>
		</div>
		<!-- Delete attribute popup div end here -->
		 <div>
			<jsp:include page="../../ParserGroupAttributeConfig.jsp"></jsp:include> <!-- All Group attribute related Stuff -->
		</div>
	</div>
		<form:form modelAttribute="<%=FormBeanConstants.ROAMING_PARSER_MAPPING_FORM_BEAN %>" method="POST" action="NO_ACTION" id="roaming-parser-mapping-form">
			
			<input type="hidden" value="<%=DecodeTypeEnum.UPSTREAM%>" name="decodeType" id="decodeType" />
			<input type="hidden" value="${plugInId}" name="plugInId" id="plugInId" />
			<input type="hidden" value="${plugInName}" name="plugInName" id="plugInName" />
			<input type="hidden" value="${plugInType}" name="plugInType" id="plugInType" />
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



<script type="text/javascript">
$(document).ready(function() {
	var element = document.getElementById("recordInitilializer");
	 if(element!=null) {
		element.value = true;
	} 
	$("#selDeviceName").val('${deviceName}');
	$("#mapping-name").val('${mappingName}');
	$("#selDeviceName").prop('disabled',true);
	$("#mapping-name").prop('disabled',true);
	$("#id").val('${mappingid}');
	var dateFormat=$("#dateFormat").find(":selected").val();
	if(dateFormat != 'Other'){
		$("#srcDateFormat").val('');
		$("#srcDateFormat").attr('readOnly',true);
	}
	var flag = '${readOnlyFlag}';
	var selMapName ='${mappingName}';
	selMappingId = '${mappingId}';
	urlAction  = '<%=ControllerConstants.GET_ATTRIBUTE_GRID_LIST_BY_MAPPING_ID%>';
	var parserType='${plugInType}';
	var attributeListType = '${REQUEST_ACTION_TYPE}';
	$("#attributeTypeHidden").val(attributeListType);
	createASN1AttributeGrid('<%=MapCache.getConfigValueAsInteger(SystemParametersConstant.TOTAL_ROWS_TO_DISPLAY_IN_GRID, 10)%>')
	getAllAsciiAttributeListByMappingId(urlAction, selMappingId);
	if(flag =='true' || selMapName == 'DEFAULT' ){
		disableAttributeDetail();
		$("#update_order_attribute").hide();
	}
});

$(document).on("change","#dateFormat",function(event) {
	resetWarningDisplay();
	clearAllMessages();
	var dateFormat = $('#dateFormat').val();
    	if(dateFormat == 'Other'){
    		$("#srcDateFormat").val('');
		    $("#srcDateFormat").attr('readOnly',false);
	   }else{
		   $("#srcDateFormat").val('');
		   $("#srcDateFormat").attr('readOnly',true);
	   }
});

function addBasicDetail(){
	resetWarningDisplay();
	clearAllMessages();
	$("#buttons-div").hide();
	$("#progress-bar-div").show();
	
	var actionUrl='';
	 if('${plugInType}'==='RAP_PARSING_PLUGIN'){
		actionUrl='<%=ControllerConstants.UPDATE_RAP_PARSER_MAPPING%>';
	} else if('${plugInType}'==='NRTRDE_PARSING_PLUGIN'){
		actionUrl='<%=ControllerConstants.UPDATE_NRTRDE_PARSER_MAPPING%>';
	}else{
		actionUrl='<%=ControllerConstants.UPDATE_TAP_PARSER_MAPPING%>';
	}
	var deviceId='${deviceId}';
	var parserMappingId='${mappingId}';
	var parserType='${plugInType}';
	var oMyForm = new FormData();
	
	var dateFormat=$("#dateFormat").find(":selected").val();
	if(dateFormat == 'Other'){
		oMyForm.append("srcDateFormat", $("#srcDateFormat").val());
	}else{
		oMyForm.append("srcDateFormat", dateFormat);
	}
	oMyForm.append("dateFormat", $("#dateFormat").find(":selected").val());
    oMyForm.append("id",parserMappingId);  
    oMyForm.append("name",$("#mapping-name").val());
    oMyForm.append("device.id",deviceId);
    oMyForm.append("parserType.alias", parserType);
    $.ajax({
		url : actionUrl,
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

/*Function will load parser attribute JQGRID */
function createASN1AttributeGrid(defaultRowNum){
	$("#asciiAttributeList").jqGrid({	    	
        datatype: "local",
        colNames:[
                  "#",
                  "<input type='checkbox' id='selectAllAttribute' onclick='attributeHeaderCheckbox(event, this)'></input>",
                  jsSpringMsg.attributeName,
                  jsSpringMsg.unifiedFieldName,
                  jsSpringMsg.description,
                  jsSpringMsg.asnDataType,
                  jsSpringMsg.childAttrib,
                  jsSpringMsg.additionalinfo,
                  jsSpringMsg.updateAction,
              	  jsSpringMsg.recordInitializer,
              	  jsSpringMsg.unifiedChoiceHolder,
                  jsSpringMsg.srcDataFormat,
                  jsSpringMsg.trimChar,
                  jsSpringMsg.trimPosition,
                  jsSpringMsg.defaultVal,
                  jsSpringMsg.unifiedChoiceHolder,
                  jsSpringMsg.parseAsJson,
                  "attributeOrder",
                  
        ],
		colModel:[
		          {name: 'id', index: 'id',hidden:true,sortable:false,search:false},
		          {name: 'checkbox', index: 'checkbox',sortable:false, formatter:asnParserAttributeCheckboxFormatter, align : 'center', width:'30%',search:false},
		          {name: 'sourceField', index: 'sourceField',sortable:false},
				  {name: 'unifiedField',index: 'unifiedField',sortable:true},
				  {name: 'description',index: 'description',sortable:false,search:false},
				  {name: 'ASN1DataType',index: 'ASN1DataType',sortable:false,search:false},
				  {name: 'childAttributes',index: 'childAttributes',sortable:false,search:false},
				  {name: 'info',index: 'info', align:'center',sortable:false,formatter: additionInfoFormatter,search:false},
				  {name: 'update',index: 'update', align:'center',sortable:false,formatter: updateFormatter,search:false},
				  {name: 'recordInitilializer',index: 'recordInitilializer', hidden:true,search:false},
				  {name: 'unifiedFieldHoldsChoiceId',index: 'unifiedFieldHoldsChoiceId', hidden:true,search:false},
				  {name: 'srcDataFormat',index: 'srcDataFormat', hidden:true,search:false},
				  {name: 'trimChar',index: 'trimChar', hidden:true,search:false},
				  {name: 'trimPosition',index: 'trimChar', hidden:true,search:false},
				  {name: 'defaultText',index: 'defaultText',hidden:true,search:false},
				  {name: 'unifiedFieldHoldsChoiceId',index: 'unifiedFieldHoldsChoiceId',hidden:true,search:false},
				  {name: 'attributeOrder',index: 'attributeOrder',hidden:true,search:false},
				  {name: 'parseAsJson',index: 'parseAsJson',hidden:true,search:false}
        ],
        rowNum:defaultRowNum,
        rowList:[10,20,50,100,200],
        height: 'auto',
        pager: '#asciiAttributeListDiv',
        viewrecords: true,
 		sortorder: "asc",
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
 			/* $(".ui-dialog-titlebar").show();
 			if ($('#asciiAttributeList').getGridParam('records') === 0) {
                $('#asciiAttributeList tbody').html("<div style='padding:6px;background:#D8D8D8;text-align:center;'>"+jsSpringMsg.emptyrecords+"</div>");
                $("#asciiAttributeListDiv").hide();
            }else{
            	$("#asciiAttributeListDiv").show();
            	ckIntanceSelected = new Array();
            }
 			var flag = '${readOnlyFlag}';
 			var selMapName ='${mappingName}';
 			
 			if(flag =='true' || selMapName == 'DEFAULT' ){
 				disableAttributeDetail();
 			} */
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
 	}).navGrid("#asciiAttributeListDiv",{edit:false,add:false,del:false,search:false});
	$(".ui-jqgrid-titlebar").hide();
	$("#asciiAttributeList").sortableRows();   
       $("#asciiAttributeList").sortable({
            items: 'tr:not(:first)'
       });
       $("#asciiAttributeList").bind('sortstop', function(event, ui) { 
           });
       $("#asciiAttributeList").jqGrid('gridDnD');
	$('#asciiAttributeList').jqGrid('filterToolbar',{
		stringResult: true,
		searchOperators: false,
		searchOnEnter: false, 
		defaultSearch: "cn"
	});	
	$("#asciiAttributeList").sortableRows();   
	$("#asciiAttributeList").disableSelection();
    $("#asciiAttributeList").sortable({
    	items: 'tr:not(:first)'
    });
    
    $('#asciiAttributeList').navButtonAdd('#asciiAttributeListDiv', {
       title: "Update Order",
       caption: "Update Order",
       position: "last",
       onClickButton: updateOrder
    });
}

function updateOrder(){
console.log("updateOrder Called");
var ids = $('#asciiAttributeList').jqGrid('getDataIDs');
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

function getAllAsciiAttributeListByMappingId(urlAction, mappingId){
					
	$.ajax({
		url: urlAction,
		cache: false,
		async: false,
    	data:
	    	{
			   'mappingId':mappingId,
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
		 				rowData.id					      = parseInt(attribute.id);
		 				rowData.checkbox			      = attribute.checkbox;
		 				rowData.sourceField			      = attribute.sourceField;
		 				rowData.unifiedField		      = attribute.unifiedField;
		 				rowData.description			      = attribute.description;
		 				rowData.ASN1DataType			  = attribute.ASN1DataType;
		 				rowData.childAttributes			  = attribute.childAttributes;
		 				rowData.info		              = attribute.info;
		 				rowData.update			          = attribute.update;
		 				rowData.recordInitilializer		  = attribute.recordInitilializer;
		 				rowData.unifiedFieldHoldsChoiceId = attribute.unifiedFieldHoldsChoiceId;
		 				rowData.srcDataFormat		      = attribute.srcDataFormat;
		 				rowData.trimChar		          = attribute.trimChar;
		 				rowData.trimPosition		      = attribute.trimPosition;
		 				rowData.defaultText		          = attribute.defaultText;
		 				rowData.unifiedFieldHoldsChoiceId = attribute.unifiedFieldHoldsChoiceId;
		 				rowData.attributeOrder		      = attribute.attributeOrder;
		 				rowData.parseAsJson		          = attribute.parseAsJson;

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
function asnParserAttributeCheckboxFormatter(cellvalue, options, rowObject) {
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

function resetAttributeDetails(){
	$('#srcDateFormat').val('');
}

</script>
