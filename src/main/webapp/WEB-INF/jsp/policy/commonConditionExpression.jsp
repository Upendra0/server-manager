<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="/WEB-INF/ShowErrorTag.tld" prefix="elitecoreError"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>

<div class="form-group">
	<spring:message
		code="business.policy.condition.create.condition.syntax.label"
		var="tooltip" ></spring:message>
	<spring:message code='Policy.condition.conditionExpression.wrong' var='wrongexpression'></spring:message>  
	<spring:message code='processingService.validation.expression.success' var='validexpression'></spring:message>  
	<div class="table-cell-label">${tooltip}<span class="required">*</span>
	</div>
	<div class="table-cell  no-border no-shadow inline-form">
		<form:textarea class="form-control " rows="4" cols="30"
			id="condition_syntax" path="conditionExpression"
			name="condition_syntax" data-toggle="tooltip"
			data-placement="bottom" title="${tooltip}" ></form:textarea>
		<spring:bind path="conditionExpression">
			<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
		</spring:bind>
	</div>
	<span id= "condition-info-block" class="input-group-addon add-on last inline-form"
		style="visibility: visible !important;"> <a href="#"
		onclick="showConditionExpressionBox();" data-toggle="tooltip"
		title="<spring:message code="business.policy.expression.view.info" ></spring:message>">
			<span class="glyphicon glyphicon-info-sign "></span>
	</a>
	</span>
	 <span id="condition_invalid_exp_error_icon" class="input-group-addon add-on last " style="visibility: visible !important; display: none;">
			<i id="condition_invalid_msg_tooltip" class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="bottom" title=""></i>
	 </span> 
	<span id="condition_valid_exp_icon"	class="input-group-addon add-on last inline-form"	style="display: none; visibility: visible !important; color: #00CC00;">
		<span class="glyphicon glyphicon-ok-circle "></span>
	</span>
</div>

<script type="text/javascript" >

$( document ).ready(function() {
	$("#expressionValidationMsg").html("");
});
	
var dynamicData;
function getConditionAllData(tagName) {
	$.ajax({
		url : 'getConditionTags',
		type : 'POST',
		cache : false,
		async : false,
		data : {
			"tagName" : tagName,
		},
		success : function(data) {
			//alert("Data is :: " + data);
			dynamicData = data;
		},
		error : function(xhr, st, err) {
			alert("Error");
		}
	});
}

function validateExpression() {
	$("#expressionValidationMsg").html("");
	showProgressBarCon();
	$.ajax({
		url : "validateConditionExpression",
		cache : false,
		async : true,
		dataType : 'json',
		type : "POST",
		data : {"conditionExpression" : $("#condition_syntax").val(),"serverInstanceId" : $("#server-instanceId").val() },
		success : function(data) {
			hideProgressBarCon();
			var responseCode = data.code;
			var responseMsg = data.msg;
			var responseObject = data.object;
			if (responseCode === "200") {
				if(responseObject){
					$("#expressionValidationMsg").html('${validexpression}');
				}else{
					$("#expressionValidationMsg").html('${wrongexpression}');
				}
				
				resetWarningDisplay();
				clearAllMessages();

			} else if (responseObject !== undefined
					&& responseObject !== 'undefined'
					&& responseCode === "400") {
				addErrorIconAndMsgForAjax(responseObject);
			} else {
				resetWarningDisplay();
				clearAllMessages();
				$("#expressionValidationMsg").html(responseMsg);
			}
		},
		error : function(xhr, st, err) {
			hideProgressBar();
			handleGenericError(xhr, st, err);
		}
	});	
}

$(function() {

	function split(val) {
		return val.split(/\s/);
	}
	function extractLast(term) {
		return split(term).pop();
	}
	$("#condition_syntax")
	// don't navigate away from the field on tab when selecting an item
	.on("keydown",function(event) {
			if (event.keyCode === $.ui.keyCode.TAB
						&& $(this).autocomplete("instance").menu.active) {
					event.preventDefault();
				}
			}).autocomplete({
				minLength : 0,
				source : function(request, response) {
					getConditionAllData(request.term);
					response($.ui.autocomplete.filter(dynamicData,extractLast(request.term)));
				},
				
				appendTo : $('#create-policy-rule-condition'),
				focus : function() {
					// prevent value inserted on focus
					return false;
				},
				select : function(event, ui) {
					var terms = split(this.value);
					// remove the current input
					terms.pop();
					// add the selected item
					terms.push(ui.item.value);
					// add placeholder to get the comma-and-space at the end
					terms.push("");
					this.value = terms.join(" ");
					return false;
				}
			});
}); 
function showProgressBarCon(){
	$("#buttons-div").hide();
	$("#progress-bar-div-condition").show();
}

function hideProgressBarCon(){
	$("#buttons-div").show();
	$("#progress-bar-div-condition").hide();	
} 

</script>
