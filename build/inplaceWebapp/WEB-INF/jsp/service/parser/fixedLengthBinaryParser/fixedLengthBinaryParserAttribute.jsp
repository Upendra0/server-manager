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
		<form:form modelAttribute="<%=FormBeanConstants.FIXED_LENGTH_BINARY_PARSER_MAPPING_FORM_BEAN%>" method="POST"
			id="fixedLength_Binary_attribute_configuration_form_bean">
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
					<div class="col-md-6 no-padding">
						<spring:message code="regex.parser.source.charset.name"
							var="tooltip" ></spring:message>
						<div class="form-group">
							<div class="table-cell-label">${tooltip}<span
									class="required">*</span>
							</div>
							<div class="input-group ">
								<form:select path="srcCharSetName"
									cssClass="form-control table-cell input-sm" tabindex="4"
									id="srcCharSetName" data-toggle="tooltip"
									data-placement="bottom" title="${tooltip }">
									<c:forEach items="${sourceCharsetName}" var="sourceCharsetName">
										<form:option value="${sourceCharsetName}">${sourceCharsetName}</form:option>
									</c:forEach>
								</form:select>
								<spring:bind path="srcCharSetName">
									<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
								</spring:bind>
							</div>
						</div>
					</div>
					
					
					<div class="col-md-6 no-padding">
						<spring:message code="parsing.service.attr.grid.column.recordLength"
							var="tooltip" ></spring:message>
						<div class="form-group">
							<div class="table-cell-label">${tooltip}<span
									class="required">*</span>
							</div>
							<div class="input-group ">
								<form:input path="recordLength"
									cssClass="form-control table-cell input-sm" tabindex="4" onkeydown="isNumericOnKeyDown(event)"
									id="recordLength" data-toggle="tooltip" data-placement="bottom"
									title="${tooltip }" ></form:input>
								<spring:bind path="recordLength">
									<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
								</spring:bind>
								
							</div>
						</div>
					</div>
					
					<div class='col-md-12 no-padding'>
					    
					</div>
					<div class='col-md-6 no-padding'> 
							<div class='form-group'> 
								<div id='buttons-div' class='input-group'> 
									<sec:authorize access='hasAuthority(\'UPDATE_PARSER\')'>
										<button type='button' class='btn btn-grey btn-xs' id='submitbtn' onclick='addBasicDetail();'><spring:message code='btn.label.update'></spring:message></button>&nbsp;
									</sec:authorize> 
									<button type='button' class='btn btn-grey btn-xs' id='resetbtn' onclick="resetAttributeDetails()"><spring:message code='btn.label.reset'></spring:message></button>&nbsp;  
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
				<sec:authorize access='hasAuthority(\'CREATE_PARSER\')'>
				<span class="title2rightfield-icon1-text">
				     	<a href="#" onclick="displayAddEditPopup('ADD','0');">
						<i class="fa fa-plus-circle"></i>
					</a> 
					<a href="#" onclick="displayAddEditPopup('ADD','0');"> 
						<spring:message code="parserConfiguration.attribute.grid.addattribute.label" ></spring:message>
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
						<spring:message code="parserConfiguration.attribute.grid.deleteattribute.label" ></spring:message>
					</a> 
					<a href="#divDeleteAttribute" class="fancybox" style="display: none;" id="delete_attribute">#</a>
				</span>
				</sec:authorize>
				<span class="title2rightfield-icon1-text">
					<a onclick="addColNames('#fixedLengthBinaryAttributeList');downloadCSVFile();">
						<i class="fa fa-download"></i>
					</a> 
					<a href="#" onclick="addColNames('#fixedLengthBinaryAttributeList');downloadCSVFile();">
						<spring:message code="parserConfiguration.attribute.grid.exportattribute.label" ></spring:message>
					</a>
				</span> 
				<sec:authorize access='hasAuthority(\'CREATE_PARSER\')'>
				<span class="title2rightfield-icon1-text"> 
					<a href="#" onclick="openUploadAttrPopup('#fixedLengthBinaryAttributeList');">
						<i class="fa fa-upload"></i>
					</a> 
				    <a href="#" onclick="openUploadAttrPopup('#fixedLengthBinaryAttributeList');">
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
				<table class="table table-hover" id="fixedLengthBinaryAttributeList"></table>
				<div id="fixedLengthBinaryAttributeListDiv"></div>
				<div class="clearfix"></div>
				<div id="divLoading" align="center" style="display: none;">
					<img src="img/preloaders/Preloader_10.gif" />
				</div>
			</div>
		</div>
		<!-- Attribute div code start here -->

		<form:form modelAttribute="parser_attribute_form_bean" method="POST"
			action="<%=ControllerConstants.ADD_EDIT_FIXED_LENGTH_BINARY_PARSER_ATTRIBUTE%>"
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
			<div id="divAdditionalInfoAttribute" style="display: none;">
				<jsp:include page="displayAdditionalFixedLengthBinaryInfoPopUp.jsp"></jsp:include>
			</div>
		</form:form>
		
		<form action="<%=ControllerConstants.DOWNLOAD_EXCEL_PARSER_COMPOSER_ATTRIBUTE_LIST %>" method="post" id="downloadExcelFileForm">
			<input type="hidden" name="exportedMappingId" id="exportedMappingId"></input>
			<input type="hidden" name="plugInType" id="plugInType"></input>
			<input type="hidden" name="streamType" id="streamType" value="UPSTREAM"/>
		</form>
		<form action="<%= ControllerConstants.DOWNLOAD_SAMPLE_ATTRIBUTE%>" method="POST" id="sample_lookup_table_form">
			<input type="hidden" id="parserType" name="parserType" value='${plugInType}'/>
			<input type="hidden" id="mappingId" name="mappingId" value='${mappingId}'/>
			<input type="hidden" id="sampleRequired" name="sampleRequired" />
			<input type="hidden" id="colNamesFromGrid" name="colNamesFromGrid"/>
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
							<spring:message code="attribute.delete.warning.message" ></spring:message>
						</div>
					</div>
				</div>
				<sec:authorize access="hasAuthority('DELETE_PARSER')">
					<div id="delete_attribute_bts_div" class="modal-footer padding10">
						<button id="fixedlength_del_btn" type="button" class="btn btn-grey btn-xs"
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
</div>

<a href="#divuploadAttrData" class="fancybox" style="display: none;" id="uploadAttrData">#</a>
<div id="divuploadAttrData" style=" width:100%; display: none;" >
	<jsp:include page="../uploadParserAttrPopup.jsp"></jsp:include>
</div> 	


<script type="text/javascript">
$(document).ready(function() {
	$("#selDeviceName").val('${deviceName}');
	$("#mapping-name").val('${mappingName}');
	$("#selDeviceName").prop('disabled',true);
	$("#mapping-name").prop('disabled',true);
	
	var dateFormat=$("#dateFormat").find(":selected").val();
	if(dateFormat != 'Other'){
		$("#srcDateFormat").val('');
		$("#srcDateFormat").attr('readOnly',true);
	}
	
	var flag = '${readOnlyFlag}';
	var selMapName ='${mappingName}';
	selMappingId = '${mappingId}';
	urlAction = '<%=ControllerConstants.GET_ATTRIBUTE_GRID_LIST_BY_MAPPING_ID%>';
	
	createFixedLengthBinaryAttributeGrid('<%=MapCache.getConfigValueAsInteger(SystemParametersConstant.TOTAL_ROWS_TO_DISPLAY_IN_GRID, 10)%>');
	getAllfixedLengthBinaryAttributeListByMappingId(urlAction, selMappingId);
	
	if(flag =='true' || selMapName == 'DEFAULT' ){
		disableAttributeDetail();
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
	
	var recordLength = $("#recordLength").val();
	 if(recordLength === null || recordLength === '' || recordLength === 'undefined '){
		 recordLength = 0;
	 }
	
	oMyForm.append("dateFormat", $("#dateFormat").find(":selected").val());
    oMyForm.append("srcCharSetName", $("#srcCharSetName").find(":selected").val());
    oMyForm.append("id",parserMappingId);  
    oMyForm.append("name",$("#mapping-name").val());
    oMyForm.append("recordLength",recordLength);
    oMyForm.append("device.id",deviceId);
    oMyForm.append("parserType.alias", parserType);
    
    
    $.ajax({
		url: '<%=ControllerConstants.UPDATE_FIXED_LENGTH_BINARY_PARSER_MAPPING%>',
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
function createFixedLengthBinaryAttributeGrid(defaultRowNum){				
	 
	 $("#fixedLengthBinaryAttributeList").jqGrid({	    	
	        datatype: "local",
	        colNames:[
	                  "#",
	                  "<input type='checkbox' id='selectAllAttribute' onclick='attributeHeaderCheckbox(event, this)'></input>",
	                  jsSpringMsg.unifiedFieldName,
	                  jsSpringMsg.description,
	                  jsSpringMsg.dateFormat,
	                  jsSpringMsg.length,
	                  jsSpringMsg.trimChar,
	                  jsSpringMsg.trimPosition,
	                  jsSpringMsg.additionalinfo,
	                  jsSpringMsg.updateAction,
	                  jsSpringMsg.startLength,
	                  jsSpringMsg.endLength,
	                  jsSpringMsg.readAsBits,
	                  jsSpringMsg.bitStartLength,
	                  jsSpringMsg.bitEndLength,
	                  jsSpringMsg.prefix,
	                  jsSpringMsg.postfix,
	                  jsSpringMsg.defaultVal,
	                  jsSpringMsg.rightDelimiter,
	                  jsSpringMsg.multiRecord
	                 ],
			colModel:[
			          {name: 'id', index: 'id',hidden:true,sortable:false,search : false},
			          {name: 'checkbox', index: 'checkbox',sortable:false, formatter:fixedLengthAttributeCheckboxFormatter, align : 'center', width:'30%',search : false},
					  {name: 'unifiedField',index: 'unifiedField',sortable:true},
					  {name: 'description',index: 'description',sortable:false,search : false},
					  {name: 'sourceFieldFormat',index: 'sourceFieldFormat', sortable:false},
					  {name: 'length',index: 'length', sortable:false,formatter: blankLength,search : false},
					  {name: 'trimChar',index: 'trimChar', sortable:false,search : false},
					  {name: 'trimPosition',index: 'trimPosition', sortable:false , hidden:true,search : false},
					  {name: 'info',index: 'info', align:'center',sortable:false,formatter: additionInfoFormatter,search : false},
					  {name: 'update',index: 'update', align:'center',sortable:false,formatter: fixedLengthUpdateFormatter,search : false}, 
					  {name: 'startLength',index: 'startLength', hidden:true,search : false},
					  {name: 'endLength',index: 'endLength', hidden:true,search : false},
					  {name: 'readAsBits',index: 'readAsBits', hidden:true,search : false},
					  {name: 'bitStartLength',index: 'bitStartLength', hidden:true,search : false},
					  {name: 'bitEndLength',index: 'bitEndLength', hidden:true,search : false},
					  {name: 'prefix',index: 'prefix', hidden:true,search : false},
					  {name: 'postfix',index: 'postfix', hidden:true,search : false},
					  {name: 'defaultText',index: 'defaultText',hidden:true,search : false},
					  {name: 'rightDelimiter',index: 'rightDelimiter', hidden:true,search : false},
					  {name: 'multiRecord',index: 'multiRecord', hidden:true,search : false}
					        	
	        ],
	        rowNum:defaultRowNum,
	        rowList:[10,20,50,100,200],
	        height: 'auto',
	        pager: '#fixedLengthBinaryAttributeListDiv',
	        viewrecords: true,
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
	 	}).navGrid("#fixedLengthBinaryAttributeListDiv",{edit:false,add:false,del:false,search:true});
		$(".ui-jqgrid-titlebar").hide();
		$('#fixedLengthBinaryAttributeList').jqGrid('filterToolbar',{
			stringResult: true,
			searchOperators: false,
			searchOnEnter: false, 
			defaultSearch: "cn"
	    });	
		
		$("#fixedLengthBinaryAttributeList").sortableRows();   
		$("#fixedLengthBinaryAttributeList").disableSelection();
	    $("#fixedLengthBinaryAttributeList").sortable({
	    	items: 'tr:not(:first)'
	    });
	    
	    $('#fixedLengthBinaryAttributeList').navButtonAdd('#fixedLengthBinaryAttributeListDiv', {
	       title: "Update Order",
	       caption: "Update Order",
	       position: "last",
	       onClickButton: updateOrder
	    });
	}

	function updateOrder(){
	console.log("updateOrder Called");
	var ids = $('#fixedLengthBinaryAttributeList').jqGrid('getDataIDs');
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

function getAllfixedLengthBinaryAttributeListByMappingId(urlAction, mappingId){
	
	$.ajax({
		url: urlAction,
		cache: false,
		async: false,
    	data:
    		{
			   'mappingId':mappingId,
			   'plugInType':'${plugInType}'			  
    		},
		dataType: 'json',
		type: "POST",
		success: function(data){						
			var response = eval(data);			
			var responseCode = response.code; 
			var responseMsg = response.msg; 
			var responseObject = response.object;			
			if(responseCode == "200"){				
				$("#fixedLengthBinaryAttributeListDiv").show();				
				var attributes=eval(responseObject);				
				var attributeList=attributes['attributeList'];
				
				if(attributeList!=null && attributeList != 'undefined' && attributeList != undefined){					
					var gridArray = [];
					$.each(attributeList,function(index,attribute){		 				
		 				var rowData = {};
		 				rowData.id					= parseInt(attribute.id);
		 				rowData.checkbox			= attribute.checkbox;
		 				rowData.sourceFieldFormat	= attribute.sourceFieldFormat;
		 				rowData.unifiedField		= attribute.unifiedField;
		 				rowData.description			= attribute.description;
		 				rowData.trimChar			= attribute.trimChar;
		 				rowData.trimPosition		= attribute.trimPosition;
		 				rowData.info				= attribute.info;
		 				rowData.updateAction		= attribute.updateAction;
		 				rowData.length   			= attribute.length;
		 				rowData.startLength   		= attribute.startLength;
		 				rowData.endLength   		= attribute.endLength;
		 				
		 				rowData.readAsBits   		= attribute.readAsBits;
		 				rowData.bitStartLength   	= attribute.bitStartLength;
		 				rowData.bitEndLength   		= attribute.bitEndLength;
		 				
		 				rowData.prefix   			= attribute.prefix;
		 				rowData.postfix   			= attribute.postfix;
		 				rowData.defaultText   		= attribute.defaultText;
		 				rowData.rightDelimiter   	= attribute.rightDelimiter;
		 				rowData.multiRecord   		= attribute.multiRecord;
		 				
		 				gridArray.push(rowData);
		 			});
					jQuery("#fixedLengthBinaryAttributeList").jqGrid('setGridParam', {datatype: 'local', data:gridArray }).trigger("reloadGrid");
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
function fixedLengthAttributeCheckboxFormatter(cellvalue, options, rowObject) {
	var uniqueId = "checkbox";
	if(rowObject["unifiedField"] && rowObject["unifiedField"] !== 'null' && rowObject["unifiedField"] !== "") {
		uniqueId += "_" + rowObject["unifiedField"];
	}
	return '<input type="checkbox" id="'+uniqueId+'" name="attributeCheckbox" value="'+rowObject["id"]+'" onchange="updateRootCheckbox(this, \'fixedLengthBinaryAttributeList\')"/>';
}

/* Function will add edit link in attribute link */
function fixedLengthUpdateFormatter(cellvalue, options, rowObject){
	var uniqueId = "editAttribute";
	if(rowObject["unifiedField"] && rowObject["unifiedField"] !== 'null' && rowObject["unifiedField"] !== "") {
		uniqueId += "_" + rowObject["unifiedField"];
	}
	return "<a href='#' id='"+uniqueId+"' class='link' onclick=displayAddEditPopup(\'EDIT\','"+rowObject["id"]+"'); ><i class='fa fa-pencil-square-o' aria-hidden='true'></i></a>";
}

function blankLength(cellvalue, options, rowObject){
	if(cellvalue == "-1"){
		return "";
	}
	else{
		return cellvalue;
	}
}

/*Function will reload attribute grid after add/edit/delete action. */
function reloadfixedLengthBinaryAttributeGridData(){
	clearAllMessages();
	
	var $grid = $("#fixedLengthBinaryAttributeList");
	jQuery('#fixedLengthBinaryAttributeList').jqGrid('clearGridData');
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
function downloadCSVFile() {
	$("#sampleRequired").val('NO');
	$("#sample_lookup_table_form").submit();
}
</script>
