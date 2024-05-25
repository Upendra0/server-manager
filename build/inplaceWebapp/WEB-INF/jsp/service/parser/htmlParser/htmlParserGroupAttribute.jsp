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
	<div class="fullwidth mtop15" id="group_attribute_details_div">
		<form:form
			modelAttribute="<%=FormBeanConstants.HTML_PARSER_MAPPING_FORM_BEAN %>"
			method="POST" id="html_group_attribute_configuration_form_bean">
			<!-- Basic details block start here -->
			<input type="hidden" id="action" name="action" value="ADD" />
			<input type="hidden" id="groupId" name="groupId" />
			<div class="box box-warning" id="attribute_basic_details_div"> 
				<div class="box-header with-border">
					<h3 class="box-title">
						<spring:message
							code="html.plugin.group.configuration.heading.label" ></spring:message>
					</h3>
					<div class="box-tools pull-right">
						<button class="btn btn-box-tool" data-widget="collapse">
							<i class="fa fa-minus"></i>
						</button>
					</div>
				</div>
				<div class="box-body inline-form ">
					<div class="col-md-6 no-padding">
						<spring:message code="group.attribute.grid.table.name.label"
							var="tooltip" ></spring:message>
						<div class="form-group">
							<div class="table-cell-label">${tooltip}</div>
							<div class="input-group">
								<input tabindex="4" id="groupName"
									class="form-control table-cell input-sm" data-toggle="tooltip"
									data-placement="bottom" title="${tooltip}" /> <span
									class="input-group-addon add-on last"> <i
									class="glyphicon glyphicon-alert" data-toggle="tooltip"
									data-placement="bottom" title=""></i></span>
							</div>
						</div>
					</div>
				</div>
			</div>
			<div class="box box-warning" id="attribute_basic_details_div">
				<div class="box-header with-border">
					<h3 class="box-title">
						<spring:message
							code="html.plugin.table.configuration.heading.label" ></spring:message>
					</h3>
					<div class="box-tools pull-right">
						<button class="btn btn-box-tool" data-widget="collapse">
							<i class="fa fa-minus"></i>
						</button>
					</div>
					<!-- /.box-tools -->
				</div>
				<div class="box-body inline-form ">
					<div class="col-md-6 no-padding">
						<spring:message
							code="group.attribute.grid.table.start.identifier.label"
							var="tooltip" ></spring:message>
						<div class="form-group">
							<div class="table-cell-label">${tooltip}</div>
							<div class="input-group">
								<input tabindex="4" id="tableStartIdentifier"
									value="${tableStartIdentifier}"
									class="form-control table-cell input-sm" data-toggle="tooltip"
									data-placement="bottom" title="${tooltip}" /> <span
									class="input-group-addon add-on last"> <i
									class="glyphicon glyphicon-alert" data-toggle="tooltip"
									data-placement="bottom" title=""></i></span>
							</div>
						</div>
					</div>

					<div class="col-md-6 no-padding">
						<spring:message
							code="group.attribute.grid.table.start.identifier.td.label"
							var="tooltip" ></spring:message>
						<div class="form-group">
							<div class="table-cell-label">${tooltip}</div>
							<div class="input-group">
								<input tabindex="4" id="tableStartIdentifierTd"
									class="form-control table-cell input-sm" data-toggle="tooltip"
									data-placement="bottom" title="${tooltip}" /> <span
									class="input-group-addon add-on last"> <i
									class="glyphicon glyphicon-alert" data-toggle="tooltip"
									data-placement="bottom" title=""></i></span>
							</div>
						</div>
					</div>

					<div class="col-md-6 no-padding">
						<spring:message
							code="group.attribute.grid.table.end.identifier.label"
							var="tooltip" ></spring:message>
						<div class="form-group">
							<div class="table-cell-label">${tooltip}</div>
							<div class="input-group">
								<input tabindex="4" id="tableEndIdentifier"
									value="${tableEndIdentifier}"
									class="form-control table-cell input-sm" data-toggle="tooltip"
									data-placement="bottom" title="${tooltip}" /> <span
									class="input-group-addon add-on last"> <i
									class="glyphicon glyphicon-alert" data-toggle="tooltip"
									data-placement="bottom" title=""></i></span>
							</div>
						</div>
					</div>

					<div class="col-md-6 no-padding">
						<spring:message
							code="group.attribute.grid.table.end.identifier.td.label"
							var="tooltip" ></spring:message>
						<div class="form-group">
							<div class="table-cell-label">${tooltip}</div>
							<div class="input-group">
								<input tabindex="4" id="tableEndIdentifierTd"
									value="${ableEndIdentifierTd}"
									class="form-control table-cell input-sm" data-toggle="tooltip"
									data-placement="bottom" title="${tooltip}" /> <span
									class="input-group-addon add-on last"> <i
									class="glyphicon glyphicon-alert" data-toggle="tooltip"
									data-placement="bottom" title=""></i></span>
							</div>
						</div>
					</div>

					<div class="col-md-6 no-padding">
						<spring:message
							code="group.attribute.grid.table.rows.ignore.label" var="tooltip" ></spring:message>
						<div class="form-group">
							<div class="table-cell-label">${tooltip}</div>
							<div class="input-group">
								<input tabindex="4" id="tableRowIgnore"
									class="form-control table-cell input-sm" data-toggle="tooltip"
									data-placement="bottom" title="${tooltip}" /> <span
									class="input-group-addon add-on last"> <i
									class="glyphicon glyphicon-alert" data-toggle="tooltip"
									data-placement="bottom" title=""></i></span>
							</div>
						</div>
					</div>
					<div class='form-group'>
						<div id='buttons-div' class='input-group'>
							<sec:authorize access='hasAuthority(\'UPDATE_PARSER\')'>
								<button type='button' class='btn btn-grey btn-xs' id='submitbtn'
									onclick='addBasicDetail();'>
									<spring:message code='btn.label.update' ></spring:message>
								</button>&nbsp;
							</sec:authorize>
							<button type='button' class='btn btn-grey btn-xs' id='resetbtn'
								onclick="resetAttributeDetails()">
								<spring:message code='btn.label.reset' ></spring:message>
							</button>
							&nbsp;
						</div>
						<div id="progress-bar-div" class="input-group"
							style="display: none;">
							<jsp:include page="../../../common/processing-bar.jsp"></jsp:include>
						</div>
					</div>
				</div>
			</div>
		</form:form>
		<!-- Basic details block end here -->
		<!-- Group Attribute div code start here -->
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
					<a href="#divAddGroupAttribute" class="fancybox" style="display: none;" id="add_edit_attribute">#</a>
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
					<a onclick="addColNames('#htmlGroupAttributeList');downloadCSVFile();">
						<i class="fa fa-download"></i>
					</a> 
					<a href="#" onclick="addColNames('#htmlGroupAttributeList');downloadCSVFile();">
						<spring:message code="parserConfiguration.attribute.grid.exportattribute.label" ></spring:message>
					</a>
				</span> 
				<sec:authorize access='hasAuthority(\'CREATE_PARSER\')'>
				<span class="title2rightfield-icon1-text"> 
					<a href="#" onclick="setUploadPopUpStyle();openUploadAttrPopup('#htmlGroupAttributeList');">
						<i class="fa fa-upload"></i>
					</a> 
				    <a href="#" onclick="setUploadPopUpStyle();openUploadAttrPopup('#htmlGroupAttributeList');">
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
				<table class="table table-hover" id="htmlGroupAttributeList"></table>
				<div id="htmlGroupAttributeListDiv"></div>
				<div class="clearfix"></div>
				<div id="divLoading" align="center" style="display: none;">
					<img src="img/preloaders/Preloader_10.gif" />
				</div>
			</div>
		</div>
		<!-- Attribute div code start here -->

		<form:form modelAttribute="parser_attribute_form_bean" method="POST"
			action="<%= ControllerConstants.ADD_EDIT_HTML_PARSER_ATTRIBUTE %>"
			id="add-edit-attribute-form">
			<!-- Add/Edit Attribute from grid  pop-up code start here-->

			<input type="hidden" value="0" id="id" name="id" />
			<input type="hidden" value="${plugInType}" id="selplugInType"
				name="selplugInType" />
			<input type="hidden" value="${mappingId}" id="selConfigMappingId"
				name="selConfigMappingId" />


			<div id="divAddGroupAttribute" style="display: none;">
				<jsp:include page="htmlGroupAttributeList.jsp"></jsp:include>
			</div>
		</form:form>
		<form action="<%= ControllerConstants.DOWNLOAD_SAMPLE_ATTRIBUTE%>"
			method="POST" id="sample_lookup_table_form">
			<input type="hidden" id="parserType" name="parserType" value='${plugInType}' /> 
			<input type="hidden" id="mappingId" name="mappingId" value='${mappingId}' /> 
			<input type="hidden" id="sampleRequired" name="sampleRequired" /> 
			<input type="hidden" id="groupAttrId" name="groupAttrId" /> 
			<input type="hidden" id="colNamesFromGrid" name="colNamesFromGrid" />
		</form>
		<!-- Add/Edit Group Attribute from grid  pop-up code end here-->
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
						<button id="html_del_btn" type="button"
							class="btn btn-grey btn-xs" onclick="deleteAttribute();">
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
					<button type="button" class="btn btn-grey btn-xs "
						id="closeDeleteAttributePopup"
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

<!-- MED-8969 START : Attribute upload screen -->
<a href="#divuploadAttrData" class="fancybox" style="display: none;"
	id="uploadAttrData">#</a>
<div id="divuploadAttrData" style="width: 100%; display: none;">
<jsp:include page="../uploadParserAttrPopup.jsp"></jsp:include>
</div> 



<!-- MED-8969 END : Attribute upload screen -->
<script type="text/javascript">
	$(document)
			.ready(
					function() {		
						var flag = '${readOnlyFlagGroup}';
						selMappingId = '${mappingId}';
						//var selectedMappingType = '${selectedMappingType}';
						if('${groupAttribute}' != ''){
							var  groupAttributeDetail = eval(${groupAttribute});
							$("#groupName").val(groupAttributeDetail.name);
							$("#tableEndIdentifier").val(groupAttributeDetail.tableEndIdentifier);
							$("#tableStartIdentifier").val(groupAttributeDetail.tableStartIdentifier);
							$("#tableEndIdentifierTd").val(groupAttributeDetail.tableEndIdentifierTdNo);
							$("#tableStartIdentifierTd").val(groupAttributeDetail.tableStartIdentifierTdNo);
							$("#tableRowIgnore").val(groupAttributeDetail.tableRowIgnore);
							$("#groupId").val(groupAttributeDetail.id);
							$("#action").val("UPDATE");		
						}
						if($("#groupId").val()==''){
							$("#add_delete_attribute_link_div").hide();
							$("#attribute_grid_div").hide();					
						}
						else{
							$("#add_delete_attribute_link_div").show();
							$("#attribute_grid_div").show();
						}
						urlAction = '<%=ControllerConstants.GET_PARSER_GROUP_ATTRIBUTE_GRID_LIST%>';
	createhtmlAttributeGrid('<%=MapCache.getConfigValueAsInteger(SystemParametersConstant.TOTAL_ROWS_TO_DISPLAY_IN_GRID, 10)%>');
	getAllhtmlGroupAttributeListByMappingId(urlAction, selMappingId);
	if(flag=='true'){	
		$("#group_attribute_details_div_mask").remove(); 
		
		$('#group_attribute_details_div').fadeTo('slow',.6);
		$element = $('#group_attribute_details_div');

		var top = Math.ceil($element.position().top);
		var elementStr = "<div id='group_attribute_details_div_mask' style='position: absolute;top:"+top+"px;left:0;width: 100%;height:100%;z-index:2;opacity:0.6;filter: alpha(opacity = 50)'></div>";
		$element.append(elementStr); 
	}	 
});

	

function addBasicDetail(){
	resetWarningDisplay();
	clearAllMessages();
	$("#buttons-div").hide();
	$("#progress-bar-div").show();
	
	var tableStartIdentifier=$("#tableStartIdentifier").val();
	var tableStartIdentifierTdNo=$("#tableStartIdentifierTd").val();
	var tableEndIdentifier=$("#tableEndIdentifier").val();
	var tableEndIdentifierTdNo=$("#tableEndIdentifierTd").val();
	var tableRowsToIgnore=$("#tableRowIgnore").val();
	var parserMappingId='${mappingId}';
	var plugInType='${plugInType}';
	var name=$("#groupName").val();	
	var actionType= $("#action").val(); 
	var oMyForm = new FormData();
	
	oMyForm.append("tableStartIdentifier", tableStartIdentifier);
    oMyForm.append("tableStartIdentifierTdNo", tableStartIdentifierTdNo);
    oMyForm.append("tableEndIdentifier",tableEndIdentifier);  
    oMyForm.append("tableEndIdentifierTdNo",tableEndIdentifierTdNo);
    oMyForm.append("tableRowsToIgnore",tableRowsToIgnore);
    oMyForm.append("name",name);
    oMyForm.append("mappingId",parserMappingId);  
    oMyForm.append("plugInType", plugInType);
    oMyForm.append("actionType", actionType);
    if($("#groupId").val()!=''){
    	oMyForm.append("id",$("#groupId").val());
	}
    oMyForm.append("attrLists",null);

    $.ajax({
    	url: '<%=ControllerConstants.ADD_EDIT_PARSER_GROUP_BASIC_DETAILS_ATTRIBUTE%>',
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
				$("#groupId").val(response.object.id);
				$("#action").val("UPDATE");
				$("#add_delete_attribute_link_div").show();
				$("#attribute_grid_div").show();
				
			}else if(responseObject != undefined && responseObject != 'undefined' && responseCode == "400"){
				
				resetWarningDisplay();
				clearAllMessages();
				responseObject["groupName"] = responseObject["name"];
			 	delete responseObject["name"];
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

function getAllhtmlGroupAttributeListByMappingId(urlAction, mappingId){
	
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
				$("#htmlGroupAttributeListDiv").show();
				var attributeList=responseObject.attributeList;
				if(attributeList!=null && attributeList != 'undefined' && attributeList != undefined){					
					var gridArray = [];
					$.each(attributeList,function(index,attribute){		 				
		 				var rowData = {};
		 				rowData.id					= parseInt(attribute.id);
		 				rowData.checkbox			= attribute.checkbox;
		 				rowData.sourceField			= attribute.sourceField;
		 				rowData.unifiedField		= attribute.unifiedField;
		 				rowData.defaultText			= attribute.defaultText;
		 				rowData.tdNo			    = attribute.tdNo;
		 				rowData.updateAction		= attribute.updateAction;
		 				gridArray.push(rowData);
		 			});
					jQuery("#htmlGroupAttributeList").jqGrid('setGridParam', {datatype: 'local', data:gridArray }).trigger("reloadGrid");
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

/*Function will load parser attribute JQGRID */
function createhtmlAttributeGrid(defaultRowNum){	
	 $("#htmlGroupAttributeList").jqGrid({	    	
	        datatype: "local",
	        colNames:[
	                  "#",
	                  "<input type='checkbox' id='selectAllAttribute' onclick='attributeHeaderCheckbox(event, this)'></input>",
	                  jsSpringMsg.attributeName,
	                  jsSpringMsg.unifiedFieldName,
	                  jsSpringMsg.defaultVal,
	                  jsSpringMsg.tdNo,	                  
	                  jsSpringMsg.updateAction
	                 ],
			colModel:[
			          {name: 'id', index: 'id',hidden:true,sortable:false,search : false},
			          {name: 'checkbox', index: 'checkbox',sortable:false, formatter:htmlParserAttributeCheckboxFormatter, align : 'center', width:'30%', search : false},
			          {name: 'sourceField', index: 'sourceField',sortable:true},
					  {name: 'unifiedField',index: 'unifiedField',sortable:true},
					  {name: 'defaultText',index: 'defaultText',sortable:false,search : false},
					  {name: 'tdNo',index: 'tdNo', sortable:false,search : false},
					  {name: 'update',index: 'update', align:'center',sortable:false,formatter: updateFormatter,search : false}

	        ],
	        rowNum:defaultRowNum,
	        rowList:[10,20,50,100,200],
	        height: 'auto',
	        pager: '#htmlGroupAttributeListDiv',
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
	 	}).navGrid("#htmlGroupAttributeListDiv",{edit:false,add:false,del:false,search:true});
		$(".ui-jqgrid-titlebar").hide();
		$('#htmlGroupAttributeList').jqGrid('filterToolbar',{
			stringResult: true,
			searchOperators: false,
			searchOnEnter: false, 
			defaultSearch: "cn"
	    });	
		
		$("#htmlGroupAttributeList").sortableRows();   
		$("#htmlGroupAttributeList").disableSelection();
	    $("#htmlGroupAttributeList").sortable({
	    	items: 'tr:not(:first)'
	    });
	    
}

/*
 * checkbox formatter for all child checkbox
 */
function htmlParserAttributeCheckboxFormatter(cellvalue, options, rowObject) {
	var uniqueId = getUniqueCheckboxIdForAttribute(rowObject);
	return '<input type="checkbox" id="'+uniqueId+'" name="attributeCheckbox" value="'+rowObject["id"]+'" onchange="updateRootCheckbox(this, \'htmlGroupAttributeList\')"/>';
}



function resetAttributeDetails(){
	$('#tableStartIdentifier').val('');
	$('#tableEndIdentifier').val('');
	$('#tableEndIdentifierTd').val('');
	$('#tableStartIdentifierTd').val('');	
	$('#tableRowIgnore').val('');
}
function downloadCSVFile() {
	$("#sampleRequired").val('NO');
 	$("#groupAttrId").val($("#groupId").val());
	$("#sample_lookup_table_form").submit();
}
function setUploadPopUpStyle(){
	document.getElementById("UploadPopUpMsg").style.padding = "8px";
}
</script>
