/*------------- Policy Condition's Functions Start here--------------*/
function ruleConditionNameFormatter(cellvalue, options, rowObject) {
	return "<a href='#' class='link' id='"+rowObject["name"]+"_name_lnk' onclick=redirectToUpdateRuleCondition(\'update\','"
			+ rowObject["id"] + "'); > " + cellvalue + " </a>";
}

function ruleConditionActualNameFormatter(cellvalue, options, rowObject) {
	return rowObject["name"];
}

function ruleConditionColumnFormatter(cellvalue, options, rowObject) {
	
	if(rowObject["type"] === 'dynamic'){
		return rowObject["value"];
	}
	if (rowObject["type"] === 'expression') {
		return rowObject["expression"];
	} else {
		return rowObject["action"];
	}
}

function redirectToUpdateRuleCondition(pageType, id) {
	$("#expressionValidationMsg").html("");
	clearAllMessagesPopUp();
	resetWarningDisplayPopUp();
	resetConditionDetails(pageType);
	if (pageType === 'create') {

		conditionType = 'create';
		policyConditionId = '';
		document.getElementById("condition_name").disabled = false;
		$("#checkExpressionValidationBtnDivForAction").show();
		$("#update_condition_expression_save_btn_div").show();
		$("#update_condition_expression_update_btn_div").hide();

		loadConditionInitParams(pageType);
		clearConditionField();

		$("#add_label").show();
		$("#update_label").hide();
		$("#id").val(0);

		$("#createPolicyConditionPopup").click();
	} else if (pageType === 'update') {
		conditionType = 'update';
		document.getElementById("condition_new").checked = true;
		document.getElementById("condition_existing").checked = false;
		policyConditionId = id;
		if (id > 0) {

			setResetConditionDetails(id);

			$("#createPolicyConditionPopup").click();
		} else {
			showErrorMsg(jsLocaleMsg2.failUpdateAtributeMsg);
		}

		loadConditionInitParams(pageType);

	} else {

		$("#update_condition_expression_update_btn_div").hide();
		$("#update_condition_expression_save_btn_div").hide();
		$("#update_label").hide();
		$("#add_label").hide();
		$("#viewClose").show();

		if (id > 0) {

			var responseObject = jQuery("#ruleConditiongrid").jqGrid(
					'getRowData', id);

			$("#id").val(responseObject.id);
			$("#condition_name").val(responseObject.name);
			$("#condition_description").val(responseObject.description);
			$("#div_condition").val(responseObject.condition);
			$("#condition_syntax").val(responseObject.expression);
			$("#type").val(responseObject.type);
			$("#createPolicyConditionPopup").click();

		}
	}
	
}

function setResetConditionDetails(id) {
	var responseObject = jQuery("#ruleConditiongrid").jqGrid('getRowData', id);
    if(jQuery.isEmptyObject( responseObject )){
    	responseObject = jQuery("#policyConditionGrid").jqGrid('getRowData', id);
    	}
    
    clearConditionField();
	$("#existing_condition_list_main_div").hide();
	$("#condition_type_div").show();
	$("#update_condition_expression_update_btn_div").show();
	$("#update_condition_expression_save_btn_div").hide();
	$("#update_label").show();
	$("#add_label").hide();
	if(responseObject.type === "dynamic"){
		document.getElementById("condition_dynamic").checked = true;
		document.getElementById("condition_expression").checked = false;
		$("#id").val(responseObject.id);
		$("#condition_name").val(responseObject.actual_name);
		$("#condition_description").val(responseObject.description);
		$("#condition_expression_syntax_div").hide();
		$("#condition_dynamic_div").show();
		$("#unifiedField").val(responseObject.unifiedField);
		$("#operator").val(responseObject.condition);
		$("#value").val(responseObject.value);
		$("#note_condition_expression_validate").hide();
	}
	else{
		document.getElementById("condition_dynamic").checked = false;
		document.getElementById("condition_expression").checked = true;
		$("#condition_expression_syntax_div").show();
		$("#condition_dynamic_div").hide();
		$("#id").val(responseObject.id);
		$("#condition_name").val(responseObject.actual_name);
		$("#condition_description").val(responseObject.description);
		$("#div_condition").val(responseObject.condition);
		$("#condition_syntax").val(responseObject.expression);
		$("#type").val(responseObject.type);
		$("#note_condition_expression_validate").show();
	}
}

function clearConditionField(){
    $("#unifiedField").val('');
	$("#operator").val('');
	$("#value").val('');
	$("#condition_syntax").val('');
}

/*------------- Policy Condition's Functions End here--------------*/

/*------------- Policy Action's Functions Start here--------------*/
function ruleActionNameFormatter(cellvalue, options, rowObject) {

	return "<a href='#' class='link' id='"+rowObject["name"]+"_name_lnk' onclick=redirectToUpdateRuleAction(\'update\','"
			+ rowObject["id"] + "'); > " + cellvalue + " </a>";
}

function ruleActionActualNameFormatter(cellvalue, options, rowObject) {
	return rowObject["name"];
}

function ruleActionColumnFormatter(cellvalue, options, rowObject) {
	
	if (rowObject["type"] === 'expression') {
		return rowObject["expression"];
	} else if(rowObject["type"] === 'dynamic' || rowObject["action"] === 'Clone' || rowObject["action"] === 'Filter'
		 || rowObject["action"] === 'Invalid') {
		return rowObject["action"];
	} else if(  rowObject["action"].includes('Clone') ){
		return  rowObject["action"];
	} 
	else {
		return "";
	}
}

function redirectToUpdateRuleAction(pageType, id) {
	$("#expressionValidationMsgForAction").html("");
	clearAllMessagesPopUp();
	resetWarningDisplayPopUp();
	resetActionDetails();
	if (pageType === 'create') {
		actionType = 'create';
		policyActionId = '';
		document.getElementById("action_name").disabled = false;
		$("#update_action_expression_save_btn_div").show();
		$("#action_dynamic_div").hide();
		
		$("#action_new").prop("checked", true).click();
		$("#checkExpressionValidationBtnDiv").hide();
		$("#note_expression_validate").hide();
		loadActionInitParams();
		clearActionField();
		
		$("span#add_label").show();
		$("span#update_label").hide();
		$("#id").val(0);
		
		$("#action").val(1);
		$("#action").hide();
		$("#markAsInvalid").prop("checked", true).click();

		$("#createPolicyActionPopup").click();

	} else if (pageType === 'update') {
			
		actionType = 'update';
		policyActionId = id;

		if (id > 0) {
			$("span#add_label").hide();
			$("span#update_label").show();
			$("#action_new").prop("checked", true).click();
			var responseObject = jQuery("#ruleActiongrid").jqGrid('getRowData',
					id);
			if(jQuery.isEmptyObject( responseObject )){
			    responseObject = jQuery("#policyActionGrid").jqGrid('getRowData', id);
			    }
			
			if(jQuery.isEmptyObject( responseObject )){
				var policyActionGriddata =   jQuery("#policyActionGrid").jqGrid('getRowData');
				for(var i=0 ; i < policyActionGriddata.length ; i++){					
				    if( policyActionGriddata[i]["id"] == id ){
					responseObject = policyActionGriddata[i];
						break;
					}				
				}
		   }
			
			 if( responseObject.type == 'expression'){
				$("#action_expression").prop("checked", true).click();
   			    $("#action_expression_save_btn_div").show();
   			    $("#action_expression_syntax_div").show();
   			    $("#action_syntax").show();
			  	$("#action_static_operation_div").hide();    // It will hide operation radio box for static Action Type	 
			  	$("#action_dynamic_div").hide();
			  	$("#checkExpressionValidationBtnDivForAction").show();
			  	$("#note_expression_validate").show();
			 } else if(responseObject.type == 'dynamic') {
				$("#action_dynamic").prop("checked", true).click();
			  	$("#action_static_operation_div").hide();   
			  	$("#action_expression_syntax_div").hide();
			  	$("#action_dynamic_div").show();
			  	$("#checkExpressionValidationBtnDivForAction").hide();
			  	$("#note_expression_validate").hide();
			 } else{
				 $("#action_new").prop("checked", true).click();
				 
		   		 $("#action_static_operation_div").show();    // It will show operation radio box for static Action Type	 
			     $("#action_expression_syntax_div" ).hide();
			     $("#action_dynamic_div").hide();
			     
			     $("#action_static").prop("checked", true).click();
			     $("#checkExpressionValidationBtnDivForAction").hide();			     
			     $("#note_expression_validate").hide();
			     var actionn = responseObject.action;
			     
			     if(actionn != null && actionn != 'undefined' && actionn.includes('Clone') ){
			    	 var cloneval = responseObject.action.split('#');
			    	 $("#markAsClone").prop("checked", true).click();			    	 
			     	 $("#action").val(cloneval[1]);			     	 
			    	 $('#action').show();
			    	 
			     }else{
			    	 $("#action").val('');
			    	 $('#action').hide();
			     }
			     if(responseObject.action == 'Clone'){
			    	 $("#markAsClone").prop("checked", true).click();
			     }
			     else if(responseObject.action == 'Filter'){
			    	 $("#markAsFilter").prop("checked", true).click();
			     }
			     else if(responseObject.action == 'Invalid'){
			    	 $("#markAsInvalid").prop("checked", true).click();
			     }
			 } 
			setResetActionDetails(id);
			

			$("#createPolicyActionPopup").click();

		} else {
			showErrorMsg(jsLocaleMsg2.failUpdateAtributeMsg);
		}
		
		loadActionInitParams();
		
	} else {

		$("#update_action_expression_update_btn_div").hide();
		$("#update_action_expression_save_btn_div").hide();
		$("span#update_label").hide();
		$("span#add_label").hide();
		$("#viewClose").show();

		if (id > 0) {

			var responseObject = jQuery("#ruleActiongrid").jqGrid('getRowData',
					id);

			$("#id").val(responseObject.id);
			$("#action_name").val(responseObject.action_name);
			$("#action_description").val(responseObject.description);
			$("#div_action").val(responseObject.action);
			$("#action_syntax").val(responseObject.expression);
			$("#createPolicyActionPopup").click();

		}
	}
}

function setResetActionDetails(id) {
	var responseObject = jQuery("#ruleActiongrid").jqGrid('getRowData',id);
    if(jQuery.isEmptyObject( responseObject )){
	    responseObject = jQuery("#policyActionGrid").jqGrid('getRowData', id);
	    }
    
    if(jQuery.isEmptyObject( responseObject )){
		var policyActionGriddata =   jQuery("#policyActionGrid").jqGrid('getRowData');
		for(var i=0 ; i < policyActionGriddata.length ; i++){					
		    if( policyActionGriddata[i]["id"] == id ){
			responseObject = policyActionGriddata[i];
				break;
			}				
		}
   }
    
    
    clearActionField();
    $("#existing_action_list_main_div").hide();
    $("#action_type_div").show();
	$("#update_action_expression_update_btn_div").show();
	$("#update_action_expression_save_btn_div").hide();
	$("#update_label").show();
	$("#add_label").hide();

	$("#id").val(responseObject.id);
	$("#action_name").val(responseObject.action_name);
	$("#action_description").val(responseObject.description);
	$("#div_action").val(responseObject.action);
	
	if(responseObject.type == 'dynamic'){
		var action = responseObject.action.split("=");
	    $("#dynamic_action").val(action[0]);
		$("#dynamicQueryEnum").val(action[1]);
	}else if(responseObject.type == 'expression'){
	    $("#action_syntax").val(responseObject.expression);
	}
}

function clearActionField(){
    $("#dynamic_action").val('');
    $("#dynamicQueryEnum").val('');
    $("#action_syntax").val('');
}

/*------------- Policy Action's Functions End here--------------*/