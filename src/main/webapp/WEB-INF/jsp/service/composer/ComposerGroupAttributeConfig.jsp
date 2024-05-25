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

		
		<!-- Group Attribute div code start here -->
		<div class="fullwidth" id="add_delete_group_attribute_link_div">
			<div class="title2">
				<spring:message code="roaming.group.attribute.grid.heading.label" ></spring:message>
				<span class="title2rightfield"> 
				<sec:authorize access='hasAuthority(\'CREATE_COMPOSER\')'>
					<span class="title2rightfield-icon1-text"> 
					<a href="#" onclick="displayAddEditGroupAttributePopup('ADD','0');">
						<i class="fa fa-plus-circle"></i>
					</a> 
					<a id="addGrpAttr_lnk" href="#" onclick="displayAddEditGroupAttributePopup('ADD','0');"> 
						<spring:message code="parserConfiguration.attribute.grid.add.attribute.label" ></spring:message>
					</a> 
					<a href="#divAddGroupAttribute" class="fancybox" style="display: none;" id="add_edit_groupAttribute">#</a>
				</span> 
				</sec:authorize>
				<sec:authorize access='hasAuthority(\'DELETE_COMPOSER\')'>
				<span class="title2rightfield-icon1-text"> 
					<a id="delete_groupAttribute_btn" href="#" onclick="displayDeleteGroupAttributePopup();">
						<i class="fa fa-trash"></i>
					</a> 
					<a href="#" onclick="displayDeleteGroupAttributePopup();"> 
						<spring:message code="parserConfiguration.attribute.grid.delete.attribute.label" ></spring:message>
					</a> 
					<a href="#divDeleteGroupAttribute" class="fancybox" style="display: none;" id="delete_groupAttribute">#</a>
				</span>
				</sec:authorize>
				</span>
			</div>
		</div>
		<div class="col-md-12 inline-form no-padding" id="groupAttr_grid_div">
			<div class="box-body table-responsive no-padding box"
				id="group_attribute">
				<table class="table table-hover" id="groupAttributeList"></table>
				<div id="groupAttributeListDiv"></div>
				<div class="clearfix"></div>
				<div id="divLoading" align="center" style="display: none;">
					<img src="img/preloaders/Preloader_10.gif" />
				</div>
			</div>
		</div>
 		<!-- Group Attribute div code end here -->
 		
		<!-- Add/Edit Group Attribute from grid  pop-up code start here-->
 		<form:form modelAttribute="group_attribute_form_bean" method="POST"
			action="<%=ControllerConstants.ADD_EDIT_GROUP_ATTRIBUTE%>"
			id="add-edit-groupAttribute-form">
			<input type="hidden" value="0" id="id" name="id" />
			<input type="hidden" value="${plugInType}" id="selplugInType"
				name="selplugInType" />
			<input type="hidden" value="${mappingId}" id="selConfigMappingId"
				name="selConfigMappingId" />

			 <div id="divAddGroupAttribute" style="display: none;">
				 <jsp:include page="addEditViewComposerGroupAttributePopUp.jsp"></jsp:include> 
			</div>
		</form:form>
		<!-- Add/Edit Group Attribute from grid  pop-up code end here-->
		
		<!-- Delete warning message popup div for group atribute start here -->
		<a href="#delete_msg_div" class="fancybox" style="display: none;"
			id="delete_warn_msg_grp_link">#</a>
		<div id="delete_msg_div" style="width: 100%; display: none;">
			<div class="modal-content">
				<div class="modal-header padding10">
					<h4 class="modal-title">
						<spring:message code="roaming.group.attribute.delete.heading.label" ></spring:message>
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
		<!-- Delete warning message popup div for group atribute end here-->

		<!-- Delete group attribute popup div start here -->
		<div id="divDeleteGroupAttribute" style="width: 100%; display: none;">
			<div class="modal-content">
				<div class="modal-header padding10">
					<h4 class="modal-title">
						<spring:message code="roaming.group.attribute.delete.heading.label" ></spring:message>
					</h4>
				</div>
				<div class="modal-body padding10 inline-form">
					<div id="deletePopUpMsg" class=fullwidth>
						<jsp:include page="../../common/responseMsgPopUp.jsp" ></jsp:include>
					</div>
					<div class="fullwidth">
						<input type="hidden" value="" id="deleteGroupAttributeId"
							name="deleteGroupAttributeId" />
						<div id="delete_selected_groupAttribute_details"></div>
						<div>
							<spring:message code="attribute.composer.delete.warning.message" ></spring:message>
						</div>
					</div>
				</div>
				<sec:authorize access="hasAuthority('DELETE_COMPOSER')">
					<div id="delete_groupAttribute_bts_div" class="modal-footer padding10">
						<button id="delete_group_confirm" type="button" class="btn btn-grey btn-xs"
							onclick="deleteGroupAttribute();">
							<spring:message code="btn.label.delete" ></spring:message>
						</button>
						<button type="button" class="btn btn-grey btn-xs"
							onclick="closeFancyBox();">
							<spring:message code="btn.label.cancel" ></spring:message>
						</button>
					</div>
					</sec:authorize>
					<div class="modal-footer padding10"
						id="delete_close_groupAttribute_buttons_div" style="display: none;">
						<button type="button" class="btn btn-grey btn-xs " id="closeDeleteGroupAttributePopup"
							onclick="closeFancyBox();reloadGroupAttributeGridData();reloadAttributeGridData();">
							<spring:message code="btn.label.close" ></spring:message>
						</button>
					</div>
				
				<div id="delete_groupAttribute_progress_bar_div"
					class="modal-footer padding10 text-left" style="display: none;">
					<jsp:include page="../../common/processing-bar.jsp"></jsp:include>
				</div>
			</div>
		</div>
		<!-- Delete group attribute popup div end here -->



<script type="text/javascript">
$(document).ready(function() {
	
	selMappingId = '${mappingId}';
	
	urlActionGrpAttr = '<%=ControllerConstants.GET_GROUP_ATTRIBUTE_GRID_LIST_BY_MAPPING_ID%>';
	createGroupAttributeGrid('<%=MapCache.getConfigValueAsInteger(SystemParametersConstant.TOTAL_ROWS_TO_DISPLAY_IN_GRID, 10)%>');
	getAllGroupAttributeListByMappingId(urlActionGrpAttr, selMappingId);
	
	createViewGroupAttributeGridOnPopUp('<%=MapCache.getConfigValueAsInteger(SystemParametersConstant.TOTAL_ROWS_TO_DISPLAY_IN_GRID, 5)%>');
	
	createViewAttributeListGridOnPopUp('<%=MapCache.getConfigValueAsInteger(SystemParametersConstant.TOTAL_ROWS_TO_DISPLAY_IN_GRID, 5)%>');
	
	createUpdateGroupAttributeGridOnPopUp('<%=MapCache.getConfigValueAsInteger(SystemParametersConstant.TOTAL_ROWS_TO_DISPLAY_IN_GRID, 5)%>');
	
	createUpdateAttributeListGridOnPopUp('<%=MapCache.getConfigValueAsInteger(SystemParametersConstant.TOTAL_ROWS_TO_DISPLAY_IN_GRID, 5)%>');
});


/*Function will create JQGRID for group attribute list */
function createGroupAttributeGrid(defaultRowNum){				
	 
	 $("#groupAttributeList").jqGrid({	    	
	        datatype: "local",
	        colNames:[
                      "#",
                      "<input type='checkbox' id='selectAllGroupAttribute' onclick='groupAttributeHeaderCheckbox(event, this)'></input>",
                      "Group Name",
                      "View",
                      "Edit"
	                 ],
			colModel:[
			          {name: 'id', index: 'id',hidden:true, search : false},
			          {name: 'checkbox', index: 'checkbox', sortable:false, formatter:groupAttributeCheckboxFormatter, align : 'center', width:'18%', search : false},
			          {name: 'groupName', index: 'groupName', search : true},
			          {name: 'view', index: 'view', align:'center', sortable:false, formatter: viewFormatter, search : false},
					  {name: 'edit', index: 'edit', align:'center', sortable:false, formatter: editFormatter, search : false}
	        		 ],
	        rowNum:defaultRowNum,
	        rowList:[10,20,60,100],
	        height: 'auto',
	        sortname: 'sequenceNumber',
	 		sortorder: "asc",
	        pager: '#groupAttributeListDiv',
	        contentType: "application/json; charset=utf-8",
	        viewrecords: true,
	        //multiselect: true,
	        //search:true,
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
	 	}).navGrid("#groupAttributeListDiv",{edit:false,add:false,del:false,search:true});
		$(".ui-jqgrid-titlebar").hide();
		$('#groupAttributeList').jqGrid('filterToolbar',{
			stringResult: true,
			searchOperators: false,
			searchOnEnter: false, 
			defaultSearch: "cn"
	    });	
}

/*Function will load all group attributes to JQGRID */
function getAllGroupAttributeListByMappingId(urlActionGrpAttr, selMappingId){
	
	$.ajax({
		url: urlActionGrpAttr,
		cache: false,
		async: false,
    	data:
    		{
    		   'mappingId':selMappingId,
			   'plugInType':'${plugInType}',
    		},
		dataType: 'json',
		type: "POST",
		success: function(data){						
			var response = eval(data);			
			var responseCode = response.code; 
			var responseMsg = response.msg; 
			var responseObject = response.object;			
			if(responseCode == "200"){				
				$("#groupAttributeListDiv").show();				
				var groupAttributes=eval(responseObject);				
				var groupAttributeList=groupAttributes['groupAttributeList'];
				if(groupAttributeList!=null && groupAttributeList != 'undefined' && groupAttributeList != undefined){					
					var gridArray = [];
					$.each(groupAttributeList,function(index,groupAttribute){		 				
		 				var rowData = {};
		 				rowData.id					= parseInt(groupAttribute.id);
		 				rowData.checkbox			= groupAttribute.checkbox;
		 				rowData.groupName			= groupAttribute.name;

		 				gridArray.push(rowData);
		 			});
					jQuery("#groupAttributeList").jqGrid('setGridParam', {datatype: 'local', data:gridArray }).trigger("reloadGrid");
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

function groupAttributeHeaderCheckbox(e, element) {
    e = e || event;/* get IE event ( not passed ) */
    e.stopPropagation ? e.stopPropagation() : e.cancelBubble = false;

    //if root checkbox is checked :-> all child checkbox should ne checked
    if(element.checked) {
    	$('input:checkbox[name="groupAttributeCheckbox"]').prop('checked',true);
    } else {
    	//if root checkbox is unchecked :-> all child checkbox should be unchecked
    	$('input:checkbox[name="groupAttributeCheckbox"]').prop('checked',false);
    }
    
}

function groupAttributeCheckboxFormatter(cellvalue, options, rowObject){
	var uniqueId = getUniqueCheckboxIdForGroupAttribute(rowObject);
	return '<input type="checkbox" id="'+uniqueId+'" name="groupAttributeCheckbox" value="'+rowObject["id"]+'" onchange="updateRootCheckbox(this, \'groupAttributeList\')"/>';

}

function getUniqueCheckboxIdForGroupAttribute(rowObject) {
	var uniqueId = "grpCheckbox";
	if(rowObject["groupName"] && rowObject["groupName"] !== 'null' && rowObject["groupName"] !== "") {
		uniqueId += "_" + rowObject["groupName"];
	}
	return uniqueId;
}

function viewFormatter(cellvalue, options, rowObject){
	return "<a href='#' class='link' onclick=displayAddEditGroupAttributePopup(\'VIEW\','"+rowObject["id"]+"'); ><i class='fa fa-list' aria-hidden='true'></i></a>";

}
	
function editFormatter(cellvalue, options, rowObject){
	var uniqueId = getUniqueEditIdForAttribute(rowObject);
	return "<a href='#' id='"+uniqueId+"' class='link' onclick=displayAddEditGroupAttributePopup(\'EDIT\','"+rowObject["id"]+"'); ><i class='fa fa-pencil-square-o' aria-hidden='true'></i></a>";
}

/*Function will display delete attribute popup */
function displayDeleteGroupAttributePopup(){
	
	//get all selected checkbox array from checkbox name
	ckIntanceSelected = [];
    $.each($("input[name='groupAttributeCheckbox']:checked"), function(){            
    	ckIntanceSelected.push($(this).val());
    });

	clearAllMessagesPopUp();
	resetWarningDisplayPopUp();
	clearAllMessages();
	
	if(ckIntanceSelected.length === 0){
		$("#lessWarn").show();
		$("#moreWarn").hide();
		$("#delete_warn_msg_grp_link").click();
		return;
	}else{
		
		var tableString ="<table class='table table-hover table-bordered'  border='1' ><th>#</th>";
		/* tableString+="<th>" + jsSpringMsg.srno + "</th>"; */
		tableString+="<th>"+jsSpringMsg.groupName+"</th>";
		
		for(var i = 0 ; i< ckIntanceSelected.length;i++){
			
			var rowData='';	
			rowData = $('#groupAttributeList').jqGrid('getRowData', ckIntanceSelected[i]);
			
			tableString += "<tr>";
			tableString += "<td><input type='checkbox' name='groupAttribute_delete' id='groupAttribute_"+ckIntanceSelected[i]+"' checked  onclick=getSelectedGroupAttributeList('"+ckIntanceSelected[i]+"')  value='"+ckIntanceSelected[i]+"' /> </td>";
			/* tableString += "<td>"+ckIntanceSelected[i]+" </td>"; */
			tableString += "<td>"+rowData.groupName+"</td>";
			tableString += "</tr>";
		}	
		
		tableString+="</table>";
		
		$("#delete_selected_groupAttribute_details").html(tableString);
		$("#delete_groupAttribute_bts_div").show();
		$("#delete_attribute_progress_bar_div").hide();
		$("#delete_close_groupAttribute_buttons_div").hide();
		$("#deleteGroupAttributeId").val(ckIntanceSelected.toString());
		$("#delete_groupAttribute").click();
	}
}

function getSelectedGroupAttributeList(elementId){
	
	if( document.getElementById("groupAttribute_"+elementId).checked === true){
		if(ckIntanceSelected.indexOf(elementId) === -1){
			ckIntanceSelected.push(elementId);
		}
	}else{
		if(ckIntanceSelected.indexOf(elementId) !== -1){
			ckIntanceSelected.splice(ckIntanceSelected.indexOf(elementId), 1);
		}
	}
	$("#deleteGroupAttributeId").val(ckIntanceSelected.toString());
	
}

/*Function will delete attribute  */
function deleteGroupAttribute(){
	$("#delete_groupAttribute_bts_div").hide();
	$("#delete_groupAttribute_progress_bar_div").show();
	$("#delete_close_groupAttribute_buttons_div").hide();
	
	$.ajax({
		url: 'deleteGroupAttribute',
		cache: false,
		async: true,
		dataType:'json',
		type:'POST',
		data:
		 {
			'mappingId'		:	selMappingId,
			"attributeId" 	:	$("#deleteGroupAttributeId").val()
		 }, 
		success: function(data){
			var response = eval(data);
			var responseCode = response.code; 
			var responseMsg = response.msg; 
		
			clearAllMessagesPopUp();
			resetWarningDisplayPopUp();
			 
			if(responseCode === "200"){
				
				showSuccessMsgPopUp(responseMsg);
				$("#delete_groupAttribute_bts_div").hide();
				$("#delete_groupAttribute_progress_bar_div").hide();
				$("#delete_close_groupAttribute_buttons_div").show();
				ckIntanceSelected = new Array();
			}else{
				showErrorMsgPopUp(responseMsg);
				$("#delete_groupAttribute_bts_div").show();
				$("#delete_groupAttribute_progress_bar_div").hide();
				$("#delete_close_groupAttribute_buttons_div").hide();
			}
		},
	    error: function (xhr,st,err){
			handleGenericError(xhr,st,err);
		}
	});
}

/*Function will reload group attribute grid after add/edit/delete action. */
function reloadGroupAttributeGridData(){
	clearAllMessages();
	
	jQuery('#groupAttributeList').jqGrid('clearGridData');
	//clearAttributeGrid();

	getAllGroupAttributeListByMappingId(urlActionGrpAttr, selMappingId);	
}

function displayAddEditGroupAttributePopup(actionType,attributeId){
	
	ckIntanceSelectedAttrListOnPopUp = [];
	ckIntanceSelectedGroupAttrListOnPopUp = [];
	
	clearAllMessagesPopUp();
	resetWarningDisplayPopUp();
	$("#name").prop('readonly', false);
	$("#name").val('');

	if(actionType === 'ADD'){
		$("#grpAddNewAttribute").show();
		$("#grpEditAttributeDistribution").hide();
		$("#grpViewClose").hide();
		$("#grpClosebtn").show();
		$("#grpAdd_label").show();
		$("#grpUpdate_label").hide();
		$("#grpView_label").hide();
		$("#view_groupAttr_grid_div").hide();
		$("#view_AttrList_grid_div").hide();
		$("#update_groupAttr_grid_div").show();
		$("#update_AttrList_grid_div").show();
		
		$("#id").val(0);
		
		getAllAddGroupAttributeByGroupId('<%=ControllerConstants.GET_ADD_GROUP_ATTRIBUTE_GRID_LIST_BY_GROUP_ID%>', selMappingId);
		getAllAddAttributeListByGroupId('<%=ControllerConstants.GET_ADD_ATTRIBUTE_GRID_LIST_BY_GROUP_ID%>', selMappingId);
		
		$("#add_edit_groupAttribute").click();
	}else if(actionType === 'EDIT'){
		if(attributeId > 0){
			$("#grpEditAttributeDistribution").show();
			$("#grpAddNewAttribute").hide();
			$("#grpViewClose").hide();
			$("#grpClosebtn").show();
			$("#grpUpdate_label").show();
			$("#grpAdd_label").hide();
			$("#grpView_label").hide();
			$("#view_groupAttr_grid_div").hide();
			$("#view_AttrList_grid_div").hide();
			$("#update_groupAttr_grid_div").show();
			$("#update_AttrList_grid_div").show();

			var responseObject='';
			responseObject = jQuery("#groupAttributeList").jqGrid ('getRowData', attributeId);
			
			$("#id").val(responseObject.id);
			$("#name").val(responseObject.groupName);
			$("#plugInType").val(currentComposerType);
			
			getAllUpdateGroupAttributeByGroupId('<%=ControllerConstants.GET_UPDATE_GROUP_ATTRIBUTE_GRID_LIST_BY_GROUP_ID%>', selMappingId, attributeId);
			getAllUpdateAttributeListByGroupId('<%=ControllerConstants.GET_UPDATE_ATTRIBUTE_GRID_LIST_BY_GROUP_ID%>', selMappingId, attributeId);
			
			$.each($("input[name='groupAttributeCheckboxOnPopUp']:checked"), function(){         
					ckIntanceSelectedGroupAttrListOnPopUp.push($(this).val());
			});
			
			$.each($("input[name='attributeCheckboxOnPopUp']:checked"), function(){           
			   		ckIntanceSelectedAttrListOnPopUp.push($(this).val());
			});
				
			$("#add_edit_groupAttribute").click();
		}else{
			showErrorMsg(jsSpringMsg.failUpdateAtributeMsg);
		}
	}else{
		$("#grpEditAttributeDistribution").hide();
		$("#grpAddNewAttribute").hide();
		$("#grpUpdate_label").hide();
		$("#grpAdd_label").hide();
		$("#grpView_label").show();
		$("#grpViewClose").show();
		$("#grpClosebtn").hide();
		$("#view_groupAttr_grid_div").show();
		$("#view_AttrList_grid_div").show();
		$("#update_groupAttr_grid_div").hide();
		$("#update_AttrList_grid_div").hide();
		
		if(attributeId > 0){
			
			var responseObject='';
			responseObject = jQuery("#groupAttributeList").jqGrid ('getRowData', attributeId);
			
			$("#id").val(responseObject.id);
			$("#name").val(responseObject.groupName);
			$("#name").prop('readonly', true);
			
			getAllViewGroupAttributeByGroupId('<%=ControllerConstants.GET_VIEW_GROUP_ATTRIBUTE_GRID_LIST_BY_GROUP_ID%>', selMappingId, attributeId);
			getAllViewAttributeListByGroupId('<%=ControllerConstants.GET_VIEW_ATTRIBUTE_GRID_LIST_BY_GROUP_ID%>', selMappingId, attributeId);
			
			$("#add_edit_groupAttribute").click();
			
		}
	}
}

/*Function will create JQGRID for view group attribute list on pop up */
function createViewGroupAttributeGridOnPopUp(defaultRowNum){				
	 
	 $("#viewGroupAttributeList").jqGrid({	    	
	        datatype: "local",
	        colNames:[
                      "#",
                      "Group Name"
                      //"View",
                      //"Edit"
	                 ],
			colModel:[
			          {name: 'id', index: 'id',hidden:true, search : false},
			          {name: 'groupName', index: 'groupName', search : true}
			          //{name: 'view', index: 'view', align:'center', sortable:false, formatter: viewFormatter, search : false},
					  //{name: 'edit', index: 'edit', align:'center', sortable:false, formatter: editFormatter, search : false}
	        		 ],
	        rowNum:defaultRowNum,
	        rowList:[10,20,60,100],
	        height: 'auto',
	        sortname: 'sequenceNumber',
	 		sortorder: "asc",
	        pager: '#viewGroupAttributeListDiv',
	        contentType: "application/json; charset=utf-8",
	        viewrecords: true,
	        //multiselect: true,
	        //search:true,
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
	 	}).navGrid("#viewGroupAttributeListDiv",{edit:false,add:false,del:false,search:true});
		$(".ui-jqgrid-titlebar").hide();
		$('#viewGroupAttributeList').jqGrid('filterToolbar',{
			stringResult: true,
			searchOperators: false,
			searchOnEnter: false, 
			defaultSearch: "cn"
	    });	
}

/*Function will load all group attribute list to JQGRID by groupId */
function getAllViewGroupAttributeByGroupId(urlActionGrpAttr, selMappingId, attributeId){
	
	var groupGridObject = jQuery("#groupAttributeList").jqGrid ('getRowData', attributeId);
	jQuery('#viewGroupAttributeList').jqGrid('clearGridData');
	
	$.ajax({
		url: urlActionGrpAttr,
		cache: false,
		async: false,
    	data:
    		{
    		   'groupId'	:	groupGridObject.id,
    		   'mappingId'  :	selMappingId,
			   'plugInType' :	'${plugInType}',
    		},
		dataType: 'json',
		type: "POST",
		success: function(data){						
			var response = eval(data);			
			var responseCode = response.code; 
			var responseMsg = response.msg; 
			var responseObject = response.object;			
			if(responseCode == "200"){				
				$("#viewGroupAttributeListDiv").show();				
				var groupAttributes=eval(responseObject);				
				var groupAttributeList=groupAttributes['groupAttributeList'];
				if(groupAttributeList!=null && groupAttributeList != 'undefined' && groupAttributeList != undefined){					
					var gridArray = [];
					$.each(groupAttributeList,function(index,groupAttribute){		 				
		 				var rowData = {};
		 				rowData.id					= parseInt(groupAttribute.id);
		 				rowData.groupName			= groupAttribute.groupName;

		 				gridArray.push(rowData);
		 			});
					jQuery("#viewGroupAttributeList").jqGrid('setGridParam', {datatype: 'local', data:gridArray }).trigger("reloadGrid");
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

/*Function will create JQGRID for view attribute list on pop up */
function createViewAttributeListGridOnPopUp(defaultRowNum){				
	 
	 $("#viewAttributeList").jqGrid({	    	
	        datatype: "local",
	        colNames:[
                      "#",
                      "<spring:message code='composer.attr.grid.destination.field.name' ></spring:message>", 
                      "<spring:message code='regex.parser.attr.grid.unified.field.name' ></spring:message>"
                      //"<spring:message code='composer.attr.grid.other.detail' ></spring:message>", 
                      //"<spring:message code='regex.parser.attr.grid.edit' ></spring:message>"
	                 ],
			colModel:[
			          {name: 'id', index: 'id',hidden:true, search : false},
			          {name: 'destinationField', index: 'destinationField'},
			          {name: 'unifiedField', index: 'unifiedField'}
			          //{name: 'otherDetail',index: 'otherDetail', align:'center',sortable:false , formatter : viewDetailFormatter, search : false},
					  //{name: 'update',index: 'update', align:'center',sortable:false,formatter: updateFormatter, search : false}
	        		 ],
	        rowNum:defaultRowNum,
	        rowList:[10,20,60,100],
	        height: 'auto',
	        sortname: 'sequenceNumber',
	 		sortorder: "asc",
	        pager: '#viewAttributeListDiv',
	        contentType: "application/json; charset=utf-8",
	        viewrecords: true,
	        //multiselect: true,
	        //search:true,
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
	 	}).navGrid("#viewAttributeListDiv",{edit:false,add:false,del:false,search:true});
		$(".ui-jqgrid-titlebar").hide();
		$('#viewAttributeList').jqGrid('filterToolbar',{
			stringResult: true,
			searchOperators: false,
			searchOnEnter: false, 
			defaultSearch: "cn"
	    });	
}

/*Function will load all attribute list to JQGRID which are already attached with groupId */
function getAllViewAttributeListByGroupId(urlActionAttrList, selMappingId, attributeId){
	
	var groupGridObject = jQuery("#groupAttributeList").jqGrid ('getRowData', attributeId);
	jQuery('#viewAttributeList').jqGrid('clearGridData');
	
	$.ajax({
		url: urlActionAttrList,
		cache: false,
		async: false,
    	data:
    		{
    		   'groupId'	:	groupGridObject.id,
    		   'mappingId'  :	selMappingId,
			   'plugInType' :	'${plugInType}',
    		},
		dataType: 'json',
		type: "POST",
		success: function(data){						
			var response = eval(data);			
			var responseCode = response.code; 
			var responseMsg = response.msg; 
			var responseObject = response.object;			
			if(responseCode == "200"){				
				$("#viewAttributeListDiv").show();				
				var attributes=eval(responseObject);				
				var attributeList=attributes['attributeList'];
				if(attributeList!=null && attributeList != 'undefined' && attributeList != undefined){					
					var gridArray = [];
					$.each(attributeList,function(index,attribute){		 				
		 				var rowData = {};
		 				rowData.id					= parseInt(attribute.id);
		 				rowData.destinationField	= attribute.destinationField;
		 				rowData.unifiedField		= attribute.unifiedField;

		 				gridArray.push(rowData);
		 			});
					jQuery("#viewAttributeList").jqGrid('setGridParam', {datatype: 'local', data:gridArray }).trigger("reloadGrid");
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

/*Function will create JQGRID for view group attribute list on pop up */
function createUpdateGroupAttributeGridOnPopUp(defaultRowNum){				
	 
	 $("#updateGroupAttributeList").jqGrid({	    	
	        datatype: "local",
	        colNames:[
                      "#",
                      "#",//"<input type='checkbox' id='selectAllAttribute' onclick='groupAttributeHeaderCheckbox(event, this)'></input>",
                      "Group Name"
                      //"View",
                      //"Edit"
	                 ],
			colModel:[
			          {name: 'id', index: 'id',hidden:true, search : false},
			          {name: 'checkbox', index: 'checkbox', sortable:false, formatter: groupAttributeOnPopUpCheckboxFormatter, align : 'center', width:'9%', search : false},
			          {name: 'groupName', index: 'groupName', search : true}
			          //{name: 'view', index: 'view', align:'center', sortable:false, formatter: viewFormatter, search : false},
					  //{name: 'edit', index: 'edit', align:'center', sortable:false, formatter: editFormatter, search : false}
	        		 ],
	        rowNum:defaultRowNum,
	        rowList:[10,20,60,100],
	        height: 'auto',
	        sortname: 'sequenceNumber',
	 		sortorder: "asc",
	        pager: '#updateGroupAttributeListDiv',
	        contentType: "application/json; charset=utf-8",
	        viewrecords: true,
	        //multiselect: true,
	        //search:true,
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
	 	}).navGrid("#updateGroupAttributeListDiv",{edit:false,add:false,del:false,search:true});
		$(".ui-jqgrid-titlebar").hide();
		$('#updateGroupAttributeList').jqGrid('filterToolbar',{
			stringResult: true,
			searchOperators: false,
			searchOnEnter: false, 
			defaultSearch: "cn"
	    });	
}

/*Function will load all group attribute list to JQGRID which are eligible to add with groupId */
function getAllAddGroupAttributeByGroupId(urlActionAttrList, selMappingId){
	
	jQuery('#updateGroupAttributeList').jqGrid('clearGridData');
	
	$.ajax({
		url: urlActionAttrList,
		cache: false,
		async: false,
    	data:
    		{
    		   'mappingId'  :	selMappingId,
			   'plugInType' :	'${plugInType}',
    		},
		dataType: 'json',
		type: "POST",
		success: function(data){						
			var response = eval(data);			
			var responseCode = response.code; 
			var responseMsg = response.msg; 
			var responseObject = response.object;			
			if(responseCode == "200"){				
				$("#updateGroupAttributeListDiv").show();				
				var groupAttributes=eval(responseObject);				
				var groupAttributeList=groupAttributes['groupAttributeList'];
				if(groupAttributeList!=null && groupAttributeList != 'undefined' && groupAttributeList != undefined){					
					var gridArray = [];
					$.each(groupAttributeList,function(index,groupAttribute){		 				
		 				var rowData = {};
		 				rowData.id					= parseInt(groupAttribute.id);
		 				rowData.checkbox			= groupAttribute.checkbox;
		 				rowData.groupName			= groupAttribute.groupName;

		 				gridArray.push(rowData);
		 			});
					jQuery("#updateGroupAttributeList").jqGrid('setGridParam', {datatype: 'local', data:gridArray }).trigger("reloadGrid");
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

/*Function will load all group attribute list to JQGRID which are already attached and as well as eligible to attach with groupId */
function getAllUpdateGroupAttributeByGroupId(urlActionAttrList, selMappingId, attributeId){
	
	var groupGridObject = jQuery("#groupAttributeList").jqGrid ('getRowData', attributeId);
	jQuery('#updateGroupAttributeList').jqGrid('clearGridData');
	
	$.ajax({
		url: urlActionAttrList,
		cache: false,
		async: false,
    	data:
    		{
    		   'groupId'	:	groupGridObject.id,
    		   'mappingId'  :	selMappingId,
			   'plugInType' :	'${plugInType}',
    		},
		dataType: 'json',
		type: "POST",
		success: function(data){						
			var response = eval(data);			
			var responseCode = response.code; 
			var responseMsg = response.msg; 
			var responseObject = response.object;			
			if(responseCode == "200"){				
				$("#updateGroupAttributeListDiv").show();				
				var groupAttributes=eval(responseObject);				
				var attachedGroupAttributeList=groupAttributes['attachedGroupAttributeList'];
				var eligibleGroupAttributeList=groupAttributes['eligibleGroupAttributeList'];
				var gridArray = [];
				if(attachedGroupAttributeList!=null && attachedGroupAttributeList != 'undefined' && attachedGroupAttributeList != undefined){		
					$.each(attachedGroupAttributeList,function(index,groupAttribute){		 				
		 				var rowData = {};
		 				rowData.id					= parseInt(groupAttribute.id);
		 				rowData.checkbox			= groupAttribute.checkbox;
		 				rowData.groupName			= groupAttribute.groupName;

		 				gridArray.push(rowData);
		 			});
				}
				if(eligibleGroupAttributeList!=null && eligibleGroupAttributeList != 'undefined' && eligibleGroupAttributeList != undefined){	
					$.each(eligibleGroupAttributeList,function(index,groupAttribute){		 				
		 				var rowData = {};
		 				rowData.id					= parseInt(groupAttribute.id);
		 				rowData.checkbox			= groupAttribute.checkbox;
		 				rowData.groupName			= groupAttribute.groupName;

		 				gridArray.push(rowData);
		 			});
				}
				
				jQuery("#updateGroupAttributeList").jqGrid('setGridParam', {datatype: 'local', data:gridArray }).trigger("reloadGrid");
				
				if(attachedGroupAttributeList!=null && attachedGroupAttributeList != 'undefined' && attachedGroupAttributeList != undefined){	
					$.each(attachedGroupAttributeList,function(index,groupAttribute){
		 				var rowData = jQuery('#updateGroupAttributeList').jqGrid ('getRowData', groupAttribute.id);
						var checkBoxId = getUniqueCheckboxIdForGroupAttributeOnPopUp(rowData);
						$("#"+checkBoxId).prop('checked', true);
		 			});
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

/*Function will create JQGRID for view attribute list on pop up */
function createUpdateAttributeListGridOnPopUp(defaultRowNum){				
	 
	 $("#updateAttributeList").jqGrid({	    	
	        datatype: "local",
	        colNames:[
                      "#",
                      "#",//"<input type='checkbox' id='selectAllAttributeOnPopUp' onclick='attributeListHeaderCheckboxOnPopUp(event, this)'></input>",
                      "<spring:message code='composer.attr.grid.destination.field.name' ></spring:message>", 
                      "<spring:message code='regex.parser.attr.grid.unified.field.name' ></spring:message>"
                      //"<spring:message code='composer.attr.grid.other.detail' ></spring:message>", 
                      //"<spring:message code='regex.parser.attr.grid.edit' ></spring:message>"
	                 ],
			colModel:[
			          {name: 'id', index: 'id',hidden:true, search : false},
			          {name: 'checkbox', index: 'checkbox', sortable:false, formatter:attributeListOnPopUpCheckboxFormatter, align : 'center', width:'18%', search : false},
			          {name: 'destinationField', index: 'destinationField'},
			          {name: 'unifiedField', index: 'unifiedField'}
			          //{name: 'otherDetail',index: 'otherDetail', align:'center',sortable:false , formatter : viewDetailFormatter, search : false},
					  //{name: 'update',index: 'update', align:'center',sortable:false,formatter: updateFormatter, search : false}
	        		 ],
	        rowNum:defaultRowNum,
	        rowList:[10,20,60,100],
	        height: 'auto',
	        sortname: 'sequenceNumber',
	 		sortorder: "asc",
	        pager: '#updateAttributeListDiv',
	        contentType: "application/json; charset=utf-8",
	        viewrecords: true,
	        //multiselect: true,
	        //search:true,
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
	 	}).navGrid("#updateAttributeListDiv",{edit:false,add:false,del:false,search:true});
		$(".ui-jqgrid-titlebar").hide();
		$('#updateAttributeList').jqGrid('filterToolbar',{
			stringResult: true,
			searchOperators: false,
			searchOnEnter: false, 
			defaultSearch: "cn"
	    });	
}

/*Function will load all attribute list to JQGRID which are eligible to add with groupId */
function getAllAddAttributeListByGroupId(urlActionAttrList, selMappingId){
	
	jQuery('#updateAttributeList').jqGrid('clearGridData');
	
	$.ajax({
		url: urlActionAttrList,
		cache: false,
		async: false,
    	data:
    		{
    		   'mappingId'  :	selMappingId,
			   'plugInType' :	'${plugInType}',
    		},
		dataType: 'json',
		type: "POST",
		success: function(data){						
			var response = eval(data);			
			var responseCode = response.code; 
			var responseMsg = response.msg; 
			var responseObject = response.object;			
			if(responseCode == "200"){				
				$("#updateAttributeListDiv").show();				
				var attributes=eval(responseObject);				
				var attributeList=attributes['attributeList'];
				if(attributeList!=null && attributeList != 'undefined' && attributeList != undefined){					
					var gridArray = [];
					$.each(attributeList,function(index,attribute){		 				
		 				var rowData = {};
		 				rowData.id					= parseInt(attribute.id);
		 				rowData.checkbox			= attribute.checkbox;
		 				rowData.destinationField	= attribute.destinationField;
		 				rowData.unifiedField		= attribute.unifiedField;

		 				gridArray.push(rowData);
		 			});
					jQuery("#updateAttributeList").jqGrid('setGridParam', {datatype: 'local', data:gridArray }).trigger("reloadGrid");
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

/*Function will load all attribute list to JQGRID which are already attached and as well as eligible to attach with groupId */
function getAllUpdateAttributeListByGroupId(urlActionAttrList, selMappingId, attributeId){
	
	var groupGridObject = jQuery("#groupAttributeList").jqGrid ('getRowData', attributeId);
	jQuery('#updateAttributeList').jqGrid('clearGridData');
	
	$.ajax({
		url: urlActionAttrList,
		cache: false,
		async: false,
    	data:
    		{
    		   'groupId'	:	groupGridObject.id,
    		   'mappingId'  :	selMappingId,
			   'plugInType' :	'${plugInType}',
    		},
		dataType: 'json',
		type: "POST",
		success: function(data){						
			var response = eval(data);			
			var responseCode = response.code; 
			var responseMsg = response.msg; 
			var responseObject = response.object;			
			if(responseCode == "200"){				
				$("#updateAttributeListDiv").show();				
				var attributes=eval(responseObject);				
				var attachedAttributeList=attributes['attachedAttributeList'];
				var eligibleAttributeList=attributes['eligibleAttributeList'];
				var gridArray = [];
				if(attachedAttributeList!=null && attachedAttributeList != 'undefined' && attachedAttributeList != undefined){		
					$.each(attachedAttributeList,function(index,attribute){		 				
		 				var rowData = {};
		 				rowData.id					= parseInt(attribute.id);
		 				rowData.checkbox			= attribute.checkbox;
		 				rowData.destinationField	= attribute.destinationField;
		 				rowData.unifiedField		= attribute.unifiedField;

		 				gridArray.push(rowData);
		 			});
				}
				if(eligibleAttributeList!=null && eligibleAttributeList != 'undefined' && eligibleAttributeList != undefined){	
					$.each(eligibleAttributeList,function(index,attribute){		 				
		 				var rowData = {};
		 				rowData.id					= parseInt(attribute.id);
		 				rowData.checkbox			= attribute.checkbox;
		 				rowData.destinationField	= attribute.destinationField;
		 				rowData.unifiedField		= attribute.unifiedField;

		 				gridArray.push(rowData);
		 			});
				}
				
				jQuery("#updateAttributeList").jqGrid('setGridParam', {datatype: 'local', data:gridArray }).trigger("reloadGrid");
				
				if(attachedAttributeList!=null && attachedAttributeList != 'undefined' && attachedAttributeList != undefined){	
					$.each(attachedAttributeList,function(index,attribute){
		 				var rowData = jQuery('#updateAttributeList').jqGrid ('getRowData', attribute.id);
						var checkBoxId = getUniqueCheckboxIdForAttributeListOnPopUp(rowData);
						$("#"+checkBoxId).prop('checked', true);
		 			});
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

function groupAttributeOnPopUpCheckboxFormatter(cellvalue, options, rowObject){
	var uniqueId = getUniqueCheckboxIdForGroupAttributeOnPopUp(rowObject);
	
	return '<input type="checkbox" id="'+uniqueId+'" name="groupAttributeCheckboxOnPopUp" value="'+rowObject["id"]+'" onchange="updateRootCheckbox(this, \'updateGroupAttributeList\')" onclick="getSelectedGroupAttributeListOnPopUp('+uniqueId+','+rowObject.id+')" />';

}

function getUniqueCheckboxIdForGroupAttributeOnPopUp(rowObject) {
	var uniqueId = "onPopUpGrpAttrCheckbox";
	if(rowObject["groupName"] && rowObject["groupName"] !== 'null' && rowObject["groupName"] !== "") {
		uniqueId += "_" + rowObject["groupName"].replace(/ /g, "_");
	}
	return uniqueId;
}

function getSelectedGroupAttributeListOnPopUp(uniqueId,elementId){
	var id = elementId.toString();
	var ckBoxId = uniqueId.id;
	if( document.getElementById(ckBoxId).checked === true){
		if(ckIntanceSelectedGroupAttrListOnPopUp.indexOf(id) === -1){
			ckIntanceSelectedGroupAttrListOnPopUp.push(id);
		}
	}else{
		if(ckIntanceSelectedGroupAttrListOnPopUp.indexOf(id) !== -1){
			ckIntanceSelectedGroupAttrListOnPopUp.splice(ckIntanceSelectedAttrListOnPopUp.indexOf(id), 1);
		}
	}
}

function groupAttributeListHeaderCheckboxOnPopUp(e, element) {
    e = e || event;/* get IE event ( not passed ) */
    e.stopPropagation ? e.stopPropagation() : e.cancelBubble = false;

    //if root checkbox is checked :-> all child checkbox should ne checked
    if(element.checked) {
    	$('input:checkbox[name="groupAttributeCheckboxOnPopUp"]').prop('checked',true);
    } else {
    	//if root checkbox is unchecked :-> all child checkbox should be unchecked
    	$('input:checkbox[name="groupAttributeCheckboxOnPopUp"]').prop('checked',false);
    }
    
}

function attributeListOnPopUpCheckboxFormatter(cellvalue, options, rowObject){
	var uniqueId = getUniqueCheckboxIdForAttributeListOnPopUp(rowObject);
	
	return '<input type="checkbox" id="'+uniqueId+'" name="attributeCheckboxOnPopUp" value="'+rowObject["id"]+'" onchange="updateRootCheckbox(this, \'updateAttributeList\')" onclick="getSelectedAttributeListOnPopUp('+uniqueId+','+rowObject.id+')" />';

}

function getUniqueCheckboxIdForAttributeListOnPopUp(rowObject) {
	var uniqueId = "onPopUpAttrCheckbox_"+rowObject.id;
	if(rowObject["destinationField"] && rowObject["destinationField"] !== 'null' && rowObject["destinationField"] !== "") {
		uniqueId += "_" + rowObject["destinationField"].replace(/ /g, "_");
	}
	if(rowObject["unifiedField"] && rowObject["unifiedField"] !== 'null' && rowObject["unifiedField"] !== "") {
		uniqueId += "_" + rowObject["unifiedField"].replace(/ /g, "_");
	}
	return uniqueId;
}

function getSelectedAttributeListOnPopUp(uniqueId,elementId){
	var id = elementId.toString();
	var ckBoxId = uniqueId.id;
	if( document.getElementById(ckBoxId).checked === true){
		if(ckIntanceSelectedAttrListOnPopUp.indexOf(id) === -1){
			ckIntanceSelectedAttrListOnPopUp.push(id);
		}
	}else{
		if(ckIntanceSelectedAttrListOnPopUp.indexOf(id) !== -1){
			ckIntanceSelectedAttrListOnPopUp.splice(ckIntanceSelectedAttrListOnPopUp.indexOf(id), 1);
		}
	}
}

function attributeListHeaderCheckboxOnPopUp(e, element) {
    e = e || event;/* get IE event ( not passed ) */
    e.stopPropagation ? e.stopPropagation() : e.cancelBubble = false;

    //if root checkbox is checked :-> all child checkbox should ne checked
    if(element.checked) {
    	$('input:checkbox[name="attributeCheckboxOnPopUp"]').prop('checked',true);
    } else {
    	//if root checkbox is unchecked :-> all child checkbox should be unchecked
    	$('input:checkbox[name="attributeCheckboxOnPopUp"]').prop('checked',false);
    }
    
}

</script>
