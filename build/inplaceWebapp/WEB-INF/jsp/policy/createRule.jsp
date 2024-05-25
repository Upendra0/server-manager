<%@page import="com.elitecore.sm.common.constants.FormBeanConstants"%>
<%@page import="com.elitecore.sm.common.constants.BaseConstants"%>
<%@page import="com.elitecore.sm.util.MapCache"%>
<%@page import="com.elitecore.sm.common.constants.SystemParametersConstant"%>
<%@page import="org.apache.catalina.connector.Request"%>
<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>

<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/security/tags" 	prefix="sec"  %>
<%@taglib uri="/WEB-INF/ShowErrorTag.tld" prefix="elitecoreError" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
<script type="text/javascript">

	var selectedConditionCk = new Array();
	var existingConditionIds = new Array();
	var selectedActionCk = new Array();
	var existingActionIds = new Array();
	var datatype;
	var jsSpringMsg2 = {};
	jsSpringMsg2.policyConditionId = "<spring:message code='policy.condition.grid.column.id'></spring:message>";
	jsSpringMsg2.policyConditionName = "<spring:message code='policy.condition.grid.column.name'></spring:message>";
	jsSpringMsg2.policyActionId = "<spring:message code='policy.action.grid.column.service.id'></spring:message>";
	jsSpringMsg2.policyActionName = "<spring:message code='policy.action.grid.column.service.name'></spring:message>";

	
	$(document).ready(function() {
		var ruleId='${rule_form_bean.id}';
		
		if(ruleId == '' || ruleId == 0){
			datatype='local';
			
		}else{
			datatype='json';
			$("#rule-name").prop("readOnly", false);
		}
		loadJQueryPolicyConditionGrid();
		loadJQueryPolicyActionGrid();
		manageOptionForRuleCategory();
	});
	
	function selectConditionPopUp() {
		$('#search_policyName').val('');
		$('#search_policyDescription').val('');
		$('#selectAllConditions').attr('checked', false);
		existingConditionIds = new Array();
		var fullData = jQuery("#policyConditionGrid").jqGrid('getRowData');
		for(i = 0; i < fullData.length; i++) {
			existingConditionIds.push(fullData[i]['id']);
		}
		console.log(existingConditionIds);
		var $grid = $("#condition-list");
		// $grid.jqGrid('setGridParam',{datatype:'json',page: 1}).trigger('reloadGrid');
		
		reloadConditionGridList();
		
		$('#selectConditionList').click();
	}
	
	function reloadConditionGridList(){
		
		var $grid = $("#condition-list");
		jQuery('#condition-list').jqGrid('clearGridData');
		$grid.jqGrid('setGridParam',{datatype:'json',page: 1,sortname: 'id',sortorder: "desc", postData:{
				
			'serverInstanceId' : function() {
				return ${instanceId};
			},
      	
			'policyConditionName': function () {
    			var id  =  $("#search_policyName").val();
    	        return id;
	    		},
	    		
	    		'policyConditionDesc': function(){
	    			return $("#search_policyDescription").val();
	    		},
	    	'existingConditionIds' : function(){
    			return existingConditionIdsList();
    		}	
	    		
		}}).trigger('reloadGrid');
				
	}
	
	function existingConditionIdsList(){
		var existingConditionIdsList = new Array();
		existingConditionIdsList = existingConditionIds;
		return existingConditionIdsList;
	}
	
	function selectActionPopUp() {
		$('#search_ActionName').val('');
		$('#search_ActionDescription').val('');
		$('#selectAllActions').attr('checked', false);
		existingActionIds = new Array();
		var fullData = jQuery("#policyActionGrid").jqGrid('getRowData');
		for(i = 0; i < fullData.length; i++) {
			existingActionIds.push(fullData[i]['id']);
		}
		var $grid = $("#action-list");
		//$grid.jqGrid('setGridParam',{datatype:'json',page: 1}).trigger('reloadGrid');
		reloadActionGridList();
		
		$('#selectActionList').click();
	}
	
	function reloadActionGridList(){
		
		var $grid = $("#action-list");
		jQuery('#action-list').jqGrid('clearGridData');
		clearActionInstanceGrid();
		$grid.jqGrid('setGridParam',{datatype:'json',page: 1,sortname: 'id',sortorder: "desc", postData:{
				
			'policyActionName': function () {
				var id  =  $("#search_ActionName").val();
		        return id;
	    		},
	    		
	    		'policyActionDesc': function(){
	    			return $("#search_ActionDescription").val();
	    		},
			
			'serverInstanceId' : function() {
				return ${instanceId};
			},
	    	'existingActionIds' : function(){
    			return existingActionIdsList();
    		}
	    		
	    		
		}}).trigger('reloadGrid');
	} 
	
	function  existingActionIdsList(){		
		return existingActionIds;
	}
	
	function loadJQueryPolicyConditionGrid(){
		$("#policyConditionGrid").jqGrid({
			url:"<%= ControllerConstants.GET_POLICY_CONDITION_LIST_BY_RULE_ID %>", 
			mtype:"GET",
			postData : {
				'ruleId': function () {
	    			return '${rule_form_bean.id}';
	    		}
			},
			datatype:datatype,
			colNames: [   
						  "<spring:message code='business.policy.rule.grid.column.id' ></spring:message>",
						  "<spring:message code='policy.condition.grid.column.name'></spring:message>",
						  "#",
						  "<spring:message code='business.policy.rule.grid.column.description' ></spring:message>",
						  "<spring:message code='business.policy.condition.type.label' ></spring:message>",
						  "<spring:message code='business.policy.rule.condition.grid.column.syntaxt' ></spring:message>",
			           	  "<spring:message code='business.policy.condition.syntax.label' ></spring:message>",
			           	  "#",
			           	  "value",
			              "unifiedField",
			              "condition"
				],
			colModel: [
				{name:'id',index:'id',hidden:true},
				{name:'name',index:'name', sortable: true, formatter: ruleConditionNameFormatter},
				{name:'actual_name',index:'actual_name',hidden:true,formatter: ruleConditionActualNameFormatter},
				{name:'description',index:'description',sortable:true, hidden:true},
				{name:'type',index:'type', sortable: true},
				{name:'expression',index:'expression',sortable:false,hidden: true},
				{name:'action',index:'action',sortable: true,formatter: ruleConditionColumnFormatter},
				{name:'relId',index:'relId',hidden:true},
				{name:'value',index:'value',sortable:false, align:'center',hidden: true},
				{name:'unifiedField',index:'unifiedField',sortable:false, align:'center',hidden: true},
				{name:'condition',index:'condition',sortable:false, align:'center',hidden: true}
			],
		
		//	rowList:[5,10,20,60,100],
			height: 'auto',
			pager: "#policyConditionGridPagingDiv",
			
			rownumbers: true,
			rowNum : -1,
			rowList: [],        // disable page size dropdown
       	    pgbuttons: false,   // disable page control like next, back button
       	    pgtext: null,
       	    loadonce: true,
       	    sortname : 'applicationOrder',
       	    
			viewrecords: true,
			multiselect: true,
			timeout : 120000,
		    loadtext: "Loading...",
			caption: "<spring:message code="business.policy.condition.grid.column.ruleList"></spring:message>",
			autowidth: true,
			beforeRequest:function(){
 		 		$('#divLoading').dialog({
		             autoOpen: false,
		             width: 90,
		             modal:true,
		             overlay: { opacity: 0.3, background: "white" },
		             resizable: false,
		             height: 125
		         });
		 		selectedRulesCk = new Array(); 
		    },
			loadComplete: function(data) {
		 		//$("#divLoading").dialog('close');
		 			$(".ui-dialog-titlebar").show();
		 			if ($('#grid').getGridParam('records') === 0) {
		             $('#grid tbody').html("<div style='padding:6px;background:#D8D8D8;text-align:center;'><spring:message code="collectionService.summary.agent.grid.empty.records"></spring:message></div>");
		             $("#agentGridPagingDiv").hide();
		         }
				
			},
			loadError : function(xhr,st,err) {
				handleGenericError(xhr,st,err);
			},
			recordtext: "<spring:message code="serverManagement.grid.pager.total.records.text"></spring:message>",
		    emptyrecords: "<spring:message code="collectionService.summary.agent.grid.empty.records"></spring:message>",
			loadtext: "<spring:message code="collectionService.summary.agent.grid.loading.text"></spring:message>",
		//	pgtext : "<spring:message code="collectionService.summary.agent.grid.pager.text"></spring:message>",
			beforeSelectRow: function (rowid, e){
				var $grid = $("#policyConditionGrid");
				if($("#jqg_policyConditionGrid_" + rowid).is(':checked')){
					if(selectedConditionCk.indexOf(rowid) == -1){
						selectedConditionCk.push(rowid);
					}
				}else{
					if(selectedConditionCk.indexOf(rowid) != -1){
						selectedConditionCk.splice(selectedConditionCk.indexOf(rowid), 1);
					}
				}
			    return false;
			},
			onSelectAll : function(id, status) {
				if (status == true) {
					selectedConditionCk = new Array();
					for (i = 0; i < id.length; i++) {
						selectedConditionCk.push(id[i]);
					}
				} else {
					selectedConditionCk = new Array();
				}
			}
			}).navGrid("#policyConditionGridPagingDiv",{edit:false,add:false,del:false,search:false,refresh:false});
				$(".ui-jqgrid-titlebar").hide();
				$(".ui-pg-input").height("10px");
				
				$("#policyConditionGrid").sortableRows();   
				$("#policyConditionGrid").disableSelection();
			    $("#policyConditionGrid").sortable({
			    	items: 'tr:not(:first)'
			    });
				$("#policyConditionGrid").bind('sortstop', function(event, ui) { 
					});
				$("#policyConditionGrid").jqGrid('gridDnD');
				
	}
	
	
	function loadJQueryPolicyActionGrid(){
		$("#policyActionGrid").jqGrid({
			url:"<%= ControllerConstants.GET_POLICY_ACTION_LIST_BY_RULE_ID %>", 
			mtype:"GET",
			postData : {
				'ruleId': function () {
	    			return '${rule_form_bean.id}';
	    		}
			},
			datatype:datatype,
			colNames: [
						  "<spring:message code='business.policy.rule.grid.column.id' ></spring:message>",
						  "<spring:message code='policy.action.grid.column.service.name'></spring:message>",
						  "#",
						  "<spring:message code='business.policy.rule.grid.column.description' ></spring:message>",
						  "<spring:message code='business.policy.rule.action.grid.column.type' ></spring:message>",
						  "<spring:message code='business.policy.rule.action.grid.column.syntaxt' ></spring:message>",
			           	  "<spring:message code='business.policy.condition.syntax.label' ></spring:message>",
			           	  "#"
				],
			colModel: [
				{name:'id',index:'id',hidden:true},
				{name:'name',index:'name', sortable: true, formatter: ruleActionNameFormatter},
				{name:'action_name',index:'action_name',hidden:true, formatter: ruleActionActualNameFormatter},
				{name:'description',index:'description',sortable:true, hidden:true,},
				{name:'type',index:'type', sortable: true},
				{name:'expression',index:'expression',sortable:false,hidden: true},
				{name:'action',index:'action',sortable: true,formatter: ruleActionColumnFormatter},
				{name:'relId',index:'relId',hidden:true}
			],
			
			// rowList:[5,10,20,60,100],
			height: 'auto',
			pager: "#policyActionGridPagingDiv",
			
			rownumbers: true,
			rowNum : -1,
			rowList: [],        // disable page size dropdown
       	    pgbuttons: false,   // disable page control like next, back button
       	    pgtext: null,
       	    loadonce: true,
       	    sortname : 'applicationOrder',
       	    
			viewrecords: true,
			multiselect: true,
			timeout : 120000,
		    loadtext: "Loading...",
			caption: "<spring:message code="business.policy.action.grid.column.ruleList"></spring:message>",
			autowidth: true,
			beforeRequest:function(){
 		 		$('#divLoading').dialog({
		             autoOpen: false,
		             width: 90,
		             modal:true,
		             overlay: { opacity: 0.3, background: "white" },
		             resizable: false,
		             height: 125
		         });
		 		selectedRulesCk = new Array(); 
		    },
			loadComplete: function(data) {
		 		//$("#divLoading").dialog('close');
		 			$(".ui-dialog-titlebar").show();
		 			if ($('#grid').getGridParam('records') === 0) {
		             $('#grid tbody').html("<div style='padding:6px;background:#D8D8D8;text-align:center;'><spring:message code="collectionService.summary.agent.grid.empty.records"></spring:message></div>");
		             $("#agentGridPagingDiv").hide();
		         }
				
			},
			loadError : function(xhr,st,err) {
				handleGenericError(xhr,st,err);
			},
			recordtext: "<spring:message code="serverManagement.grid.pager.total.records.text"></spring:message>",
		    emptyrecords: "<spring:message code="collectionService.summary.agent.grid.empty.records"></spring:message>",
			loadtext: "<spring:message code="collectionService.summary.agent.grid.loading.text"></spring:message>",
		//	pgtext : "<spring:message code="collectionService.summary.agent.grid.pager.text"></spring:message>",
			beforeSelectRow: function (rowid, e){
				var $grid = $("#policyActionGrid");
				if($("#jqg_policyActionGrid_" + rowid).is(':checked')){
					if(selectedActionCk.indexOf(rowid) == -1){
						selectedActionCk.push(rowid);
					}
				}else{
					if(selectedActionCk.indexOf(rowid) != -1){
						selectedActionCk.splice(selectedActionCk.indexOf(rowid), 1);
					}
				}
			    return false;
			},
			onSelectAll : function(id, status) {
				if (status == true) {
					selectedActionCk = new Array();
					for (i = 0; i < id.length; i++) {
						selectedActionCk.push(id[i]);
					}
				} else {
					selectedActionCk = new Array();
				}
			}
			}).navGrid("#policyActionGridPagingDiv",{edit:false,add:false,del:false,search:false,refresh:false});
				$(".ui-jqgrid-titlebar").hide();
				$(".ui-pg-input").height("10px");
				
				$("#policyActionGrid").sortableRows();   
				$("#policyActionGrid").disableSelection();
			    $("#policyActionGrid").sortable({
			    	items: 'tr:not(:first)'
			    });
				$("#policyActionGrid").bind('sortstop', function(event, ui) {});
				$("#policyActionGrid").jqGrid('gridDnD');
				
	}
	
 	function deleteSelectedConditions() {
		var i, n;
		if(selectedConditionCk.length > 0) {
				for (i = 0, n = selectedConditionCk.length; i < n; i++) {
				    $('#policyConditionGrid').jqGrid('delRowData',selectedConditionCk[i]);
				    selectedConditionCk[i] = null;
				}
		}	
		
		$('#cb_policyConditionGrid').attr('checked', false);
		selectedConditionCk = new Array();
		closeFancyBox();
	} 
	
	function deleteConditionPopup(){
		clearAllMessagesPopUp();
		resetWarningDisplayPopUp();
		clearAllMessages();
		if(selectedConditionCk != null && selectedConditionCk.length > 0){
			var tableString ="<table class='table table-hover table-bordered'  border='1' >";
			tableString+="<th>#</th><th>"+jsSpringMsg2.policyConditionId+"</th><th>"+jsSpringMsg2.policyConditionName+"</th>";
		
			for(var i = 0 ; i< selectedConditionCk.length;i++){
				
				var	rowData = $('#policyConditionGrid').jqGrid('getRowData', selectedConditionCk[i]);
				if(rowData != null && rowData != ''){
					tableString += "<tr>";
					tableString += "<td><input type='checkbox' name='condition_delete' id='condition_"+selectedConditionCk[i]+"' checked  onclick=getSelectedConditionList('"+selectedConditionCk[i]+"')  value='"+selectedConditionCk[i]+"' /> </td>";
					tableString += "<td>"+rowData.id+" </td>";
					tableString += "<td>"+rowData.name+"</td>";
					tableString += "</tr>";
				}
			}	
			tableString+="</table>";
			$("#delete_selected_condition_details").html(tableString);
			$("#delete_condition_bts_div").show();
			$("#delete_condition_progress_bar_div").hide();
			$("#selectedConditionDeleteBtn").show();
			$("#delete_close_condition_buttons_div").hide();
			$("#deleteConditionId").val(selectedConditionCk.toString());
			$("#delete_condition").click();
		}
		else{
			var tableString ="<table class='table table-hover table-bordered'  border='1' >";
			tableString+="<b><p>No conditions selected </p></b></table>";
			$("#delete_selected_condition_details").html(tableString);
			$("#delete_condition_bts_div").show();
			$("#selectedConditionDeleteBtn").hide();
			$("#delete_condition_progress_bar_div").hide();
			$("#delete_close_condition_buttons_div").hide();
			$("#deleteConditionId").val(selectedConditionCk.toString());
			$("#delete_condition").click();
		}
		
	}
		
	
	function getSelectedConditionList(elementId){
		if( document.getElementById("condition_"+elementId).checked === true){
			if(selectedConditionCk.indexOf(elementId) === -1){
				selectedConditionCk.push(elementId);
				document.getElementById("condition_"+elementId).checked = false;
			}
		}else{
			if(selectedConditionCk.indexOf(elementId) !== -1){
				selectedConditionCk.splice(selectedConditionCk.indexOf(elementId), 1);
			}
		}
		$("#deleteConditionId").val(selectedConditionCk.toString());
		
	}
	
	function deleteActionPopup(){
		clearAllMessagesPopUp();
		resetWarningDisplayPopUp();
		clearAllMessages();
		if(selectedActionCk != null && selectedActionCk.length > 0){
			var tableString ="<table class='table table-hover table-bordered'  border='1' >";
			tableString+="<th>#</th><th>"+jsSpringMsg2.policyActionId+"</th><th>"+jsSpringMsg2.policyActionName+"</th>";
		
			for(var i = 0 ; i< selectedActionCk.length;i++){
				if(selectedActionCk[i] != null){
					var	rowData = $('#policyActionGrid').jqGrid('getRowData', selectedActionCk[i]);
					tableString += "<tr>";
					tableString += "<td><input type='checkbox' name='action_delete' id='action_"+selectedActionCk[i]+"' checked  onclick=getSelectedActionList('"+selectedActionCk[i]+"')  value='"+selectedActionCk[i]+"' /> </td>";
					tableString += "<td>"+rowData.id+" </td>";
					tableString += "<td>"+rowData.name+"</td>";
					tableString += "</tr>";
				}
			}	
			tableString+="</table>";
				$("#delete_selected_action_details").html(tableString);
				$("#delete_action_bts_div").show();
				$("#delete_action_progress_bar_div").hide();
				$("#delete_close_action_buttons_div").hide();
				$("#selectedActionDeleteBtn").show();
				$("#deleteActionId").val(selectedActionCk.toString());
				$("#delete_action").click();
		}else{
			var tableString ="<table class='table table-hover table-bordered'  border='1' >";
			tableString+="<b><p>No actions selected </p></b></table>";
			$("#delete_selected_action_details").html(tableString);
			$("#delete_action_bts_div").show();
			$("#delete_action_progress_bar_div").hide();
			$("#delete_close_action_buttons_div").hide();
			$("#selectedActionDeleteBtn").hide();
			$("#deleteActionId").val(selectedActionCk.toString());
			$("#delete_action").click();
		}
	}
	
	function getSelectedActionList(elementId){
		if( document.getElementById("action_"+elementId).checked === true){
			if(selectedActionCk.indexOf(elementId) === -1){
				selectedActionCk.push(elementId);
				document.getElementById("action_"+elementId).checked = false;
			}
		}else{
			if(selectedActionCk.indexOf(elementId) !== -1){
				selectedActionCk.splice(selectedActionCk.indexOf(elementId), 1);
			}
		}
		$("#deleteActionId").val(selectedActionCk.toString());
		
	}
	
	function deleteSelectedActions() {
		var i, n;
		if(selectedActionCk.length > 0) {
			
				for (i = 0, n = selectedActionCk.length; i < n; i++) {
				    $('#policyActionGrid').jqGrid('delRowData',selectedActionCk[i]);
				    selectedActionCk[i] =null;
				}
		}	
		$('#cb_policyActionGrid').attr('checked', false);
		selectedActionCk = new Array();
		closeFancyBox();
	}
	
	function createRule(){
		var currentPage = '${currentPage}';
		var ruleId ='${rule_form_bean.id}';
		var conditionIdList = $('#policyConditionGrid').jqGrid('getDataIDs');
		var actionIdList = $('#policyActionGrid').jqGrid('getDataIDs');
	
		var conditionFullData = jQuery("#policyConditionGrid").jqGrid('getRowData');
		var actionFullData = jQuery("#policyActionGrid").jqGrid('getRowData');
		
		
				
		var condition_name="";
		for(var i=0 ; i < conditionFullData.length ; i++)
		{
			condition_name = $(conditionFullData[i]["name"]).text();
			conditionFullData[i]["name"] = condition_name.trim();
		}
		var action_name="";
		for(var j=0 ; j < actionFullData.length ; j++)
		{
			action_name = $(actionFullData[j]["name"]).text();
			actionFullData[j]["name"] = action_name.trim();
		}
		
		
		if(conditionFullData == null || conditionFullData == 'null' || conditionFullData == 'undefined' || conditionFullData == '' ){
			
			$("#validationMsg").html('');
			$("#validationMsg").html('<spring:message code="policy.condition.validation.no.select.msg" ></spring:message>');
			$("#validation_msg_popup_lnk").click();
			
			return false;
		}
		
		if(actionFullData == null || actionFullData == 'null' || actionFullData == 'undefined' || actionFullData == '' ){
			
			$("#validationMsg").html('');
			$("#validationMsg").html('<spring:message code="policy.action.validation.no.select.msg" ></spring:message>');
			$("#validation_msg_popup_lnk").click();
			
			return false;
		}
		
		
		$('#policy-rule-conditions').val(JSON.stringify(conditionFullData));
		$('#policy-rule-actions').val(JSON.stringify(actionFullData));
		$('#currentPage').val(currentPage);
		
		if(ruleId != '' && ruleId != 0){
			$("#rule-id").val(ruleId);
			$("#create-rule-form").attr("action","<%=ControllerConstants.UPDATE_RULE%>");
			$("#create-rule-form").submit();
		}else{
			$('#currentPage').val(0);
			<%-- $("#create-rule-form").attr("action","<%=ControllerConstants.CREATE_RULE%>"); --%>
			$("#create-rule-form").submit();
		}
		
	}
	
	function resetField(ruleId) {
		$("#ruleId").val(ruleId);
		$("#update-rule-form2").attr("action","<%=ControllerConstants.INIT_CREATE_RULE%>");
		$("#update-rule-form2").submit();
		
	}

	function resetConditionDetails(conditionType) {
		//clearAllMessages();
		//resetWarningDisplayPopUp();
			$("#condition_expression").prop("checked", true);
			$("#condition_expression_syntax_div").show();
			$("#condition_dynamic_div").hide();
			if (conditionType == "create") {
				$("#update_condition_expression_update_btn_div").hide();
				$("#update_condition_expression_save_btn_div").show();
				$("#condition-final-update-button").hide();
				$("#condition-final-save-button").show();
				$("#condition_name").val('');
				$("#condition_description").val('');
				$("#condition_syntax").val('');
				$("#condition_syntax").val('');
				 var dropDown = document.getElementById("unifiedField");
			      dropDown.selectedIndex = 0;
			     var dropDownOperator = document.getElementById("operator");
			     dropDownOperator.selectedIndex = 0;
			     var dropDownValue = document.getElementById("value");
			     dropDownValue.selectedIndex = 0;
				
				$("#condition_invalid_exp_error_icon").hide();
				$("#condition_valid_exp_icon").hide();
				$("#condition_expression_save_next_btn").hide();
			} else if (conditionType == "update") {
				redirectToUpdateRuleConditionOnReset(policyConditionId);
			}
		}
	
	function resetConditionSearchParams() {
	
		clearAllMessages();
		clearResponseMsgDiv();
		clearResponseMsg();
		clearErrorMsg();
	
		$("#policyConditionName").val('');
		$("#policyConditionType").val('-1');
		$("#policyConditionDesc").val('');
		$("#policyConditionStatus").val('-1');
	
		reloadConditionGridDataWithClearMsg();
	}
	
	function reloadConditionGridDataWithClearMsg() {
		clearAllMessages();
		clearResponseMsgDiv();
		clearResponseMsg();
		clearErrorMsg();
	
		reloadConditionGridData();
	}
	
	function reloadConditionGridData(){
		
		var $grid = $("#policyConditionGrid");
		jQuery('#policyConditionGrid').jqGrid('clearGridData');
		clearConditionInstanceGrid();
		$grid.jqGrid('setGridParam',{datatype:'json',page: 1,sortname: 'id',sortorder: "desc", postData:{
			
			'ruleId': function () {
				return '${rule_form_bean.id}';
			}
	    			
		}}).trigger('reloadGrid');
	}
	
	function clearConditionInstanceGrid() {
		var $grid = $("#policyConditionGrid");
		var rowIds = $grid.jqGrid('getDataIDs');
		// iterate through the rows and delete each of them
		for (var i = 0, len = rowIds.length; i < len; i++) {
			var currRow = rowIds[i];
			$grid.jqGrid('delRowData', currRow);
		}
	}
	
	function resetSearchParams(){
		
		clearAllMessages();
		clearResponseMsgDiv();
		clearResponseMsg();
		clearErrorMsg();
		
		$("#policyActionName").val('');
		$("#policyType").val('-1');
		$("#policyActionDesc").val('');
		$("#policyStatus").val('-1');
	
		reloadGridDataWithClearMsg();
	}
	
	function clearInstanceGrid(){
		var $grid = $("#policyActionGrid");
		var rowIds = $grid.jqGrid('getDataIDs');
		// iterate through the rows and delete each of them
		for(var i=0,len=rowIds.length;i<len;i++){
			var currRow = rowIds[i];
			$grid.jqGrid('delRowData', currRow);
		}
	}
	
	function reloadGridDataWithClearMsg(){
		clearAllMessages();
		clearResponseMsgDiv();
		clearResponseMsg();
		clearErrorMsg();
		
		reloadGridData();
	}
	
	function reloadGridData(){
		
		
		var $grid = $("#policyActionGrid");
		jQuery('#policyActionGrid').jqGrid('clearGridData');
		clearInstanceGrid();
		$grid.jqGrid('setGridParam',{datatype:'json',page: 1,sortname: 'id',sortorder: "desc", postData:{
				
			'ruleId': function () {
				return '${rule_form_bean.id}';
			}
	    		
		}}).trigger('reloadGrid');
	}
	
	function manageOptionForRuleCategory(){
		var ruleCategory = $("#category option:selected").val();
		if(ruleCategory == 'Filter' || ruleCategory == 'NA'){
			$("#rule-category-child-component-div").hide();
			$("#severity").val('');
			$("#rule-errorCode").val('');
		}else{
			$("#rule-category-child-component-div").show();
 			if($("#severity").val() === '' || $("#severity").val() === null)
			{
				$("#severity").val($("#severity option:first").val());
			}
		}
	}

</script>
</head>
<jsp:include page="../common/newheader.jsp"></jsp:include>
<script src="${pageContext.request.contextPath}/customJS/policyManager.js<%= "?v=" + Math.random() %>" type="text/javascript"></script>
<body>
	<div style="display: none;">
		<form id="update-rule-form2" name="update-rule-form2" method="GET">
			<input type="hidden" id="server-instance-name" name="server-instance-name" value='${instanceName}' /> 
			<input type="hidden" id="server-instance-host" name="server-instance-host" value='${host}' /> 
			<input type="hidden" id="server-instance-port" name="server-instance-port" value='${port}' /> 
			<input type="hidden" id="server-instance-id" name="server-instance-id" value='${instanceId}' />
			<input type="hidden" id="serviceInstanceId" name="serviceInstanceId" value="${serviceInstanceId}">
			<input type="hidden" id="serviceId" name="serviceId" value="${serviceId}"> 
			<input type="hidden" id="serviceType" name="serviceType" value="<%=BaseConstants.PROCESSING_SERVICE%>">
			<input type="hidden" id="serviceName" name="serviceName" value="${serviceName}">
			 
			<input type="hidden" id="ruleId" name="ruleId" /> 
			<!-- <input type="hidden" id="ruleGroupName"	name="ruleGroupName" /> 
			<input type="hidden" id="ruleGroupDesc" name="ruleGroupDesc" /> -->
		</form>
	</div>

	<div class="box-body padding0 mtop10">
		<div class="fullwidth">
			<form:form id="create-rule-form" modelAttribute="<%=FormBeanConstants.RULE_FORM_BEAN %>" method="POST" action="<%=ControllerConstants.CREATE_RULE%>">
				<input type="hidden" name="policy-rule-conditions" id="policy-rule-conditions"/>
				<input type="hidden" name="policy-rule-actions" id="policy-rule-actions"/>
				<input type="hidden" id="server-instance-name" name="server-instance-name" value='${instanceName}' /> 
				<input type="hidden" id="server-instance-host" name="server-instance-host" value='${host}' /> 
				<input type="hidden" id="server-instance-port" name="server-instance-port" value='${port}' /> 
				<input type="hidden" id="server-instance-id" name="server-instance-id" value='${instanceId}' />
				<input type="hidden" id="instanceId" name="instanceId" value='${instanceId}' />
				<input type="hidden" id="serviceInstanceId" name="serviceInstanceId" value="${serviceInstanceId}">
			<input type="hidden" id="serviceId" name="serviceId" value="${serviceId}">
			<input type="hidden" id="serviceType" name="serviceType" value="<%=BaseConstants.PROCESSING_SERVICE%>">
			<input type="hidden" id="serviceName" name="serviceName" value="${serviceName}">
			<input type="hidden" id="currentPage" name="currentPage" value="${currentPage}">
				<form:hidden id="rule-alias" path="alias" ></form:hidden>
				<form:hidden id="rule-id" path="id" ></form:hidden>
				<div class="fullwidth inline-form">
					<div class="col-md-6 no-padding">
						<spring:message code="business.policy.create.rule.name.label" var="tooltip" ></spring:message>
						<div class="form-group">
							<div class="table-cell-label">${tooltip }
								<span class="required">*</span>
							</div>
							<div class="input-group">
								<form:input id="rule-name" path="name" class="form-control table-cell input-sm" tabindex="1"
									data-toggle="tooltip" data-placement="bottom" title="${tooltip }" ></form:input>
								<spring:bind path="name">
										<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
								</spring:bind>
							</div>
						</div>
						
						<spring:message code="business.policy.rule.group.grid.column.description" var="tooltip" ></spring:message>
						<div class="form-group">
							<div class="table-cell-label">${tooltip }</div>
							<div class="input-group ">
								<form:textarea class="form-control input-sm" id="rule-description" path="description" tabindex="2"
									data-toggle="tooltip" rows="3" data-placement="bottom" title="${tooltip}"></form:textarea>
								<spring:bind path="description">
										<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
								</spring:bind>
							</div>
						</div>
						
						<spring:message code="policymgmt.rule.create.operator" var="tooltip" ></spring:message>
						<div class="form-group">
							<div class="table-cell-label">${tooltip }</div>
							<div class="input-group ">
								<form:select path="operator" cssClass="form-control table-cell input-sm" tabindex="3"
								id="rule-operator" data-toggle="tooltip" data-placement="bottom" title="${tooltip }">
									<form:option value="AND">AND</form:option>
									<form:option value="OR">OR</form:option>
								</form:select>
								<spring:bind path="operator">
										<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
								</spring:bind>
							</div>
						</div>
						
 						<spring:message code="business.policy.create.rule.alert.id.label" var="tooltip" ></spring:message>
						<div class="form-group">
							<div class="table-cell-label">${tooltip }</div>
							<div class="input-group ">
								<select class="form-control table-cell input-sm" tabindex="4" name="rule-alert"
									id="rule-alert" data-toggle="tooltip" data-placement="bottom" title="${tooltip }">
										<option value="">-- Select SNMP Alert --</option>
										<c:choose>
											<c:when test="${not empty rule_form_bean.alert.id && rule_form_bean.alert.id != 0}">
												<c:forEach items="${snmpAlerts }" var="snmpAlert">
													<c:choose>
														<c:when test="${(snmpAlert.id eq rule_form_bean.alert.id )}">
															<option value="${snmpAlert.id }" selected="selected">${snmpAlert.name }</option>
														</c:when>
														<c:otherwise>
															<option value="${snmpAlert.id }">${snmpAlert.name }</option>
														</c:otherwise>
													</c:choose>
												</c:forEach>
											</c:when>
											<c:otherwise>
												<c:forEach items="${snmpAlerts }" var="snmpAlert">
													<option value="${snmpAlert.id }">${snmpAlert.name }</option>
												</c:forEach>
												</c:otherwise>
										</c:choose>
								</select>
							<%--	<form:select path="alert" cssClass="form-control table-cell input-sm" tabindex="4"
									id="rule-alert" data-toggle="tooltip" data-placement="bottom" title="${tooltip }">
										<form:option value="">-- Select SNMP Alert --</form:option>
										<c:forEach items="${snmpAlerts }" var="snmpAlert">
											<form:option value="${snmpAlert.id }">${snmpAlert.name }</form:option>
										</c:forEach>
								</form:select>
								<spring:bind path="alert">
										<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
								</spring:bind>--%>
							</div>
						</div>
						
						<spring:message code="business.policy.create.rule.alert.description.label" var="tooltip" ></spring:message>
						<div class="form-group">
							<div class="table-cell-label">${tooltip }</div>
							<div class="input-group ">
								<form:textarea class="form-control input-sm" id="rule-alert-description" path="alertDescription" tabindex="5"
									data-toggle="tooltip" rows="3" data-placement="bottom" title="${tooltip}"></form:textarea>
								<spring:bind path="alertDescription">
										<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
								</spring:bind>
							</div>
						</div>
						
						<spring:message code="business.policy.create.rule.global.rule.label" var="tooltip" ></spring:message>
						<div class="form-group">
							<div class="table-cell-label">${tooltip }</div>
							<div class="input-group ">
								<form:input id="rule-globalSequenceRuleId" path="globalSequenceRuleId" class="form-control table-cell input-sm" tabindex="6"
									data-toggle="tooltip" data-placement="bottom" title="${tooltip }" ></form:input>
								<spring:bind path="globalSequenceRuleId">
										<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
								</spring:bind>
							</div>
						</div> 
						
						<spring:message code="business.policy.create.rule.category.label" var="tooltip" ></spring:message>
						<div class="form-group">
							<div class="table-cell-label">${tooltip }</div>
							<div class="input-group">
								<form:select path="category" cssClass="form-control table-cell input-sm" tabindex="4" id="category"
										data-toggle="tooltip" data-placement="bottom" title="${tooltip }" onchange="manageOptionForRuleCategory();">
										<c:forEach var="policyRuleCategory" items="${policyRuleCategory}">
											<option ${policyRuleCategory.value eq rule_form_bean.category ? 'selected' : ''} id ="${policyRuleCategory}" value ="${policyRuleCategory.value}">${policyRuleCategory.value}</option>
										</c:forEach>
										
								</form:select>
								<spring:bind path="category">
										<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
								</spring:bind>
							</div>
						</div> 
						
						<div id="rule-category-child-component-div">
							<spring:message code="business.policy.create.rule.severity.label" var="tooltip" ></spring:message>
							<div class="form-group">
								<div class="table-cell-label">${tooltip }</div>
								<div class="input-group ">
									<form:select path="severity" cssClass="form-control table-cell input-sm" tabindex="4" id="severity"
											data-toggle="tooltip" data-placement="bottom" title="${tooltip }">
											<c:forEach var="policyRuleSeverity" items="${policyRuleSeverity}">
												<option ${policyRuleSeverity.value eq rule_form_bean.severity ? 'selected' : ''} id ="${policyRuleSeverity}" value ="${policyRuleSeverity.value}">${policyRuleSeverity.value}</option>
											</c:forEach>
									</form:select>
									<spring:bind path="severity">
											<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
									</spring:bind>
								</div>
							</div> 
							
							<spring:message code="business.policy.create.rule.error.code.label" var="tooltip" ></spring:message>
							<div class="form-group">
								<div class="table-cell-label">${tooltip }</div>
								<div class="input-group ">
									<form:input id="rule-errorCode" path="errorCode" class="form-control table-cell input-sm" tabindex="6"
										data-toggle="tooltip" data-placement="bottom" title="${tooltip }" maxlength="250" ></form:input>
									<spring:bind path="errorCode">
											<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
									</spring:bind>
								</div>
							</div> 
						</div>
					</div>
				</div>
			</form:form>
		</div>
	</div>
	
	<%-- Policy condition list grid start here --%>
	<div class="fullwidth">
		<div class="title2">
			<spring:message code="business.policy.condition.existing.list.section.title" ></spring:message>
			<span class="title2rightfield"> 
				<span class="title2rightfield-icon1-text"> 
					<sec:authorize access="hasAuthority('UPDATE_POLICY_RULE_CONFIGURATION')">
						<a href="#" onclick="redirectToUpdateRuleCondition('create','0');">
							<i class="fa fa-plus-circle"></i>
						</a>
						
						<a href="#" id="createRuleCondition" onclick="redirectToUpdateRuleCondition('create','0');" tabindex="8">
							<spring:message	code="business.policy.rule.condition.create.label" ></spring:message>
						</a>
					</sec:authorize>
				</span>
				<span class="title2rightfield-icon1-text"> 
					<sec:authorize access="hasAuthority('UPDATE_POLICY_RULE_CONFIGURATION')">
						<a href="#" onclick="selectConditionPopUp();">
							<i class="fa fa-plus-circle"></i>
						</a>
						<a href="#" id="addCondition" onclick="selectConditionPopUp();" tabindex="7"> 
							<spring:message	code="business.policy.rule.grid.column.add" ></spring:message>
						</a>
					</sec:authorize>
				</span> 
				<span class="title2rightfield-icon1-text"> 
					<sec:authorize access="hasAuthority('UPDATE_POLICY_RULE_CONFIGURATION')">
						<a href="#" onclick="deleteConditionPopup();">
							<i class="fa fa-trash"></i>
						</a>
						<!-- <a href="#divDeleteService" class="fancybox" style="display: none;" id="deleteService" tabindex="4">#</a> -->
						<a href="#" id="deleteCondition" onclick="deleteConditionPopup();" tabindex="8">
							<spring:message	code="business.policy.rule.grid.column.delete" ></spring:message>
						</a>
						<a href="#divDeleteCondition" class="fancybox" style="display: none;" id="delete_condition">#</a>
					</sec:authorize>
				</span>
			</span>
		</div>
	</div>
	
	
	<div class="box-body table-responsive no-padding box">
		<table class="table table-hover" id="policyConditionGrid"></table>
		<div id="policyConditionGridPagingDiv"></div>
		<div class="clearfix"></div> 
	    <div id="divLoading" align="center" style="display: none;"><img src="img/preloaders/Preloader_10.gif" /></div>
	</div>
	
	<a href="#divSelectConditionList" class="fancybox" style="display: none;" id="selectConditionList">#</a>
	<div id="divSelectConditionList" style="width: 100%; display: none;">
		<jsp:include page="policyConditionListPopUp.jsp"></jsp:include>
	</div>
	
 	<a href="#createPolicyConditionDiv" class="fancybox" style="display: none;" id="createPolicyConditionPopup">#</a>
		   	<div id="createPolicyConditionDiv" style=" width:100%; display: none;" >
			     <jsp:include page="ruleConditionManager.jsp"></jsp:include>  
			</div> 
	
	<%-- Condition list grid ends --%>
	
	<%-- Policy Action list grid start here --%>
	<div class="fullwidth">
		<div class="title2">
			<spring:message code="business.policy.action.existing.list.section.title" ></spring:message>
			<span class="title2rightfield"> 
 			
				<span class="title2rightfield-icon1-text"> 
					<sec:authorize access="hasAuthority('UPDATE_POLICY_RULE_CONFIGURATION')">
						<a href="#" onclick="redirectToUpdateRuleAction('create','0');">
							<i class="fa fa-plus-circle"></i>
						</a>
						
						<a href="#" id="createRuleAction" onclick="redirectToUpdateRuleAction('create','0');" tabindex="8">
							<spring:message	code="business.policy.rule.action.create.label" ></spring:message>
						</a>
					</sec:authorize>
				</span> 
				
				<span class="title2rightfield-icon1-text"> 
					<sec:authorize access="hasAuthority('UPDATE_POLICY_RULE_CONFIGURATION')">
						<a href="#" onclick="selectActionPopUp();">
							<i class="fa fa-plus-circle"></i>
						</a>
						<a href="#" id="addAction" onclick="selectActionPopUp();" tabindex="9" > 
							<spring:message	code="business.policy.rule.grid.column.add" ></spring:message>
						</a>
					</sec:authorize>
				</span> 
				<span class="title2rightfield-icon1-text"> 
					<sec:authorize access="hasAuthority('UPDATE_POLICY_RULE_CONFIGURATION')">
						<a href="#" onclick="deleteActionPopup();" id="delete_action_lnk">
							<i class="fa fa-trash"></i>
						</a>
						<!-- <a href="#divDeleteService" class="fancybox" style="display: none;" id="deleteService" tabindex="4">#</a> -->
						<a href="#" id="delete_action_lnk" onclick="deleteActionPopup();" tabindex="10" id="delete_action">
							<spring:message	code="business.policy.rule.grid.column.delete" ></spring:message>
						</a>
						<a href="#divDeleteAction" class="fancybox" style="display: none;" id="delete_action">#</a>
					</sec:authorize>
				</span>
			</span>
		</div>
	</div>
	
	
	<div class="box-body table-responsive no-padding box">
		<table class="table table-hover" id="policyActionGrid">
		</table>
		<div id="policyActionGridPagingDiv"></div>
		<div class="clearfix"></div> 
	    <div id="divLoading" align="center" style="display: none;"><img src="img/preloaders/Preloader_10.gif" /></div>

	</div>

		 
	<a href="#divSelectActionList" class="fancybox" style="display: none;" id="selectActionList">#</a>
	<div id="divSelectActionList" style="width: 100%; display: none;">
		<jsp:include page="policyActionListPopUp.jsp"></jsp:include>
	</div>

 	<a href="#createPolicyActionDiv" class="fancybox" style="display: none;" id="createPolicyActionPopup">#</a>
		 <div id="createPolicyActionDiv" style=" width:100%; display: none;" >
			     <jsp:include page="ruleActionManager.jsp"></jsp:include> 
		 </div> 
	
	<%-- Action list grid ends --%>
	
	
			<!-- Delete Condition popup div start here -->
		<div id="divDeleteCondition" style="width: 100%; display: none;">
			<div class="modal-content">
				<div class="modal-header padding10">
					<h4 class="modal-title">
						<spring:message code="policy.condition.delete.instance.popup.header" ></spring:message>
					</h4>
				</div>
				<div class="modal-body padding10 inline-form">
					<div id="deletePopUpMsg" class=fullwidth>
						<jsp:include page="../common/responseMsgPopUp.jsp" ></jsp:include>
					</div>
					<div class="fullwidth">
						<input type="hidden" value="" id="deleteConditionId"
							name="deleteConditionId" />
						<div id="delete_selected_condition_details"></div>

					</div>
				</div>
					<div id="delete_condition_bts_div" class="modal-footer padding10">
						<button type="button" class="btn btn-grey btn-xs" id="selectedConditionDeleteBtn"
							onclick="deleteSelectedConditions();">
							<spring:message code="btn.label.delete" ></spring:message>
						</button>
						<button type="button" class="btn btn-grey btn-xs" id="selectedConditionCloseBtn"
							onclick="closeFancyBox();">
							<spring:message code="btn.label.cancel" ></spring:message>
						</button>
					</div>
					<div class="modal-footer padding10"
						id="delete_close_condition_buttons_div" style="display: none;">
						<button type="button" class="btn btn-grey btn-xs "
							onclick="closeFancyBox();">
							<spring:message code="btn.label.close" ></spring:message>
						</button>
					</div>
				
				<div id="delete_condition_progress_bar_div"
					class="modal-footer padding10 text-left" style="display: none;">
					<jsp:include page="../common/processing-bar.jsp"></jsp:include>
				</div>
			</div>
		</div>
		<!-- Delete Condition popup div end here -->
		
		<!-- Delete Action popup div start here -->
		<div id="divDeleteAction" style="width: 100%; display: none;">
			<div class="modal-content">
				<div class="modal-header padding10">
					<h4 class="modal-title">
						<spring:message code="policy.action.delete.instance.popup.header" ></spring:message>
					</h4>
				</div>
				<div class="modal-body padding10 inline-form">
					<div id="deletePopUpMsg" class=fullwidth>
						<jsp:include page="../common/responseMsgPopUp.jsp" ></jsp:include>
					</div>
					<div class="fullwidth">
						<input type="hidden" value="" id="deleteActionId"
							name="deleteActionId" />
						<div id="delete_selected_action_details"></div>

					</div>
				</div>
					<div id="delete_action_bts_div" class="modal-footer padding10">
						<button type="button" class="btn btn-grey btn-xs" id="selectedActionDeleteBtn"
							onclick="deleteSelectedActions();">
							<spring:message code="btn.label.delete" ></spring:message>
						</button>
						<button type="button" class="btn btn-grey btn-xs" id="selectedActionCloseBtn"
							onclick="closeFancyBox();">
							<spring:message code="btn.label.cancel" ></spring:message>
						</button>
					</div>
					<div class="modal-footer padding10"
						id="delete_close_action_buttons_div" style="display: none;">
						<button type="button" class="btn btn-grey btn-xs "
							onclick="closeFancyBox();">
							<spring:message code="btn.label.close" ></spring:message>
						</button>
					</div>
				
				<div id="delete_action_progress_bar_div"
					class="modal-footer padding10 text-left" style="display: none;">
					<jsp:include page="../common/processing-bar.jsp"></jsp:include>
				</div>
			</div>
		</div>
		<!-- Delete Action popup div end here -->

</body>
</html>
