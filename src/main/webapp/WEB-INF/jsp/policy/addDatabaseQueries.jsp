<%@page import="com.elitecore.sm.common.constants.BaseConstants"%>
<%@page import="com.elitecore.sm.common.constants.FormBeanConstants"%>
<%@taglib 	uri="http://www.springframework.org/tags" 			prefix="spring"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="elitecore" %> 
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="/WEB-INF/ShowErrorTag.tld" prefix="elitecoreError"%>
<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<%@ taglib uri="http://www.springframework.org/security/tags"
	prefix="sec"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.elitecore.sm.parser.model.UnifiedFieldEnum"%>

<script src="${pageContext.request.contextPath}/customJS/autoSuggestionForUnifiedField.js<%= "?v=" + Math.random() %>" type="text/javascript"></script>
<style>
   .ui-autocomplete
   {
       z-index:4000 !important;
       max-height: 200px;
       overflow-y: auto;
       overflow-x: hidden;
   }
   label{
	    display: block;
	    white-space: nowrap;
	    text-overflow: ellipsis;
	    overflow: hidden;
	    width: 100%;
	}
</style>	

	
			<div class="modal-content">
					<div class="modal-header padding10">
						<h4 class="modal-title">
							<span id="add_label" style="display: none;"><spring:message
									code="policymgmt.databaseQueries.add.heading" ></spring:message></span>
							<span id="update_label" style="display: none;"><spring:message
									code="policymgmt.databaseQueries.update.heading" ></spring:message></span>
						</h4>
					</div>
				<div id="content-scroll-d" class="content-scroll" style="height:500px">
					<form:form modelAttribute="<%=FormBeanConstants.DATABASE_QUERY_FORM_BEAN%>" action="<%=ControllerConstants.CREATE_DATABASE_QUERY%>"
						id="database_query_form_bean">
					<div class="modal-body padding10 inline-form">
					<div id="AddPopUpMsg">
						<jsp:include page="../common/responseMsgPopUp.jsp" ></jsp:include>
						</div>			
							<input type="hidden" id = "id"/>
								<form:input path="alias"  style="display: none;"
									cssClass="form-control table-cell input-sm" tabindex="4"
									id="alias" data-toggle="tooltip" data-placement="bottom"
									title="${tooltip }"></form:input>
								
							
						<div class="table-cell-label"><label  id="dcMessage" cssClass="form-control table-cell input-sm" data-placement="bottom"></label></div>
						<div class="col-md-12 no-padding">
							<div class="form-group">
									<spring:message code="policymgmt.databaseQueries.database.query.name"
										var="tooltip" ></spring:message>
									<spring:message code="policymgmt.databaseQueries.database.query.name"
									var="label" ></spring:message>
									<div class="table-cell-label">${label}<span class="required">*</span></div>
									<div class="input-group">
										<form:input path="queryName"
									cssClass="form-control table-cell input-sm" tabindex="4"
									id="queryName" data-toggle="tooltip" data-placement="bottom"
									title="${tooltip }"></form:input>
								<spring:bind path="queryName">
									<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
								</spring:bind>
									</div>
							</div>
						</div>
						<div class="col-md-12 no-padding">
							<div class="form-group">
									<spring:message code="policymgmt.databaseQueries.database.query.value"
										var="tooltip" ></spring:message>
									<spring:message code="policymgmt.databaseQueries.database.query.value"
									var="label" ></spring:message>
									<div class="table-cell-label">${label}<span class="required">*</span></div>
									<div class="input-group">
										<form:input path="queryValue"
									cssClass="form-control table-cell input-sm" tabindex="4"
									id="queryValue" data-toggle="tooltip" data-placement="bottom"
									title="${tooltip }"></form:input>
								<spring:bind path="queryValue">
									<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
								</spring:bind>
									</div>
							</div>
						</div>
						<div class="col-md-12 no-padding">
							<div class="form-group">
									<spring:message code="policymgmt.databaseQueries.description"
										var="tooltip" ></spring:message>
										<spring:message code="policymgmt.databaseQueries.description"
										var="label" ></spring:message>
									<div class="table-cell-label">${label}</div>
									<div class="input-group">
											<form:input path="description"
									cssClass="form-control table-cell input-sm" tabindex="4"
									id="description" data-toggle="tooltip" data-placement="bottom"
									title="${tooltip }"></form:input>
								<spring:bind path="description">
									<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
								</spring:bind>
									</div>
							</div>
						</div>
						<div class="col-md-6 no-padding">
							<div class="form-group">
									<spring:message code="policymgmt.databaseQueries.cache"
										var="tooltip" ></spring:message>
										<spring:message code="policymgmt.databaseQueries.cache"
										var="label" ></spring:message>
									<div class="table-cell-label">${label}</div>
									<div class="input-group">
									<form:select path="cacheEnable"
											cssClass="form-control table-cell input-sm" tabindex="4"
											id="cacheEnable" data-toggle="tooltip"
											data-placement="bottom" title="${tooltip }" onchange="manageConditionActionOutputDB();">
											<c:forEach var="trueFalseEnum" items="${trueFalseEnum}">	
											<form:option value="${trueFalseEnum.name}">${trueFalseEnum}</form:option>
											</c:forEach>
									</form:select>
									<span
											class="input-group-addon add-on last"> <i
											class="glyphicon glyphicon-alert" data-toggle="tooltip"
											data-placement="left" title=""></i></span>
									</div>
							</div>
						</div>
						<div class="col-md-6 no-padding" style="display: none;">
							<div class="form-group">
									<spring:message code="policymgmt.databaseQueries.conditionExpression.enable"
										var="tooltip" ></spring:message>
										<spring:message code="policymgmt.databaseQueries.conditionExpression.enable"
										var="label" ></spring:message>
									<div class="table-cell-label">${label}</div>
									<div class="input-group">
									<form:select path="conditionExpressionEnable"
											cssClass="form-control table-cell input-sm" tabindex="4"
											id="conditionExpressionEnable" data-toggle="tooltip"
											data-placement="bottom" title="${tooltip }" onchange="changeConditionList();" disabled="true" >
											<c:forEach var="trueFalseEnum" items="${trueFalseEnum}">
												<form:option value="${trueFalseEnum.name}">${trueFalseEnum}</form:option>
											</c:forEach>
									</form:select>
									<span
											class="input-group-addon add-on last"> <i
											class="glyphicon glyphicon-alert" data-toggle="tooltip"
											data-placement="left" title=""></i></span>
									</div>
							</div>
						</div>
						<div class="col-md-12 no-padding" id = "conditionExpressionDiv">
							<div class="form-group">
									<spring:message code="policymgmt.databaseQueries.conditionExpression"
										var="tooltip" ></spring:message>
										<spring:message code="policymgmt.databaseQueries.conditionExpression"
										var="label" ></spring:message>
									<div class="table-cell-label">${label}</div>
									<div class="input-group">
										<form:input path="conditionExpression"
									cssClass="form-control table-cell input-sm" tabindex="4"
									id="conditionExpression" data-toggle="tooltip" data-placement="bottom"
									title="${tooltip }"></form:input>
								<spring:bind path="conditionExpression">
									<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
								</spring:bind>
									</div>
							</div>
						</div>
						<div class="col-md-6 no-padding" id="returnMultipleRowsDiv">
							<div class="form-group">
									<spring:message code="policymgmt.databaseQueries.returnMultipleRows"
										var="tooltip" ></spring:message>
										<spring:message code="policymgmt.databaseQueries.returnMultipleRows"
										var="label" ></spring:message>
									<div class="table-cell-label">${label}</div>
									<div class="input-group">
									<form:select path="returnMultipleRowsEnable"
											cssClass="form-control table-cell input-sm" tabindex="4"
											id="returnMultipleRowsEnable" data-toggle="tooltip"
											data-placement="bottom" title="${tooltip }">
											<c:forEach var="trueFalseEnum" items="${trueFalseEnum}">
												<form:option value="${trueFalseEnum.name}">${trueFalseEnum}</form:option>
											</c:forEach>
									</form:select>
									<span
											class="input-group-addon add-on last"> <i
											class="glyphicon glyphicon-alert" data-toggle="tooltip"
											data-placement="left" title=""></i></span>
									</div>
							</div>
						</div>
						<div class="col-md-6 no-padding" id="logicalOperatorDiv">
							<div class="form-group">
									<spring:message code="policymgmt.databaseQueries.logical"
										var="tooltip" ></spring:message>
										<spring:message code="policymgmt.databaseQueries.logical"
										var="label" ></spring:message>
									<div class="table-cell-label">${label}</div>
									<div class="input-group">
									<form:select path="logicalOperator"
											cssClass="form-control table-cell input-sm" tabindex="4"
											id="databaseQueryLogicalOperator" data-toggle="tooltip"
											data-placement="bottom" title="${tooltip }">
											<option value="NA">Select Logical Operator</option>
											<c:forEach var="logicalOperatorEnum" items="${logicalOperatorEnum}">
												<form:option value="${logicalOperatorEnum.value}">${logicalOperatorEnum}</form:option>
											</c:forEach>
									</form:select>
									<span
											class="input-group-addon add-on last"> <i
											class="glyphicon glyphicon-alert" data-toggle="tooltip"
											data-placement="left" title=""></i></span>
									</div>
							</div>
						</div>
						<div class="col-md-12 no-padding" id="divOutputDbField">
							<div class="form-group">
									<spring:message code="policymgmt.databaseQueries.outputDBField"
										var="tooltip" ></spring:message>
										<spring:message code="policymgmt.databaseQueries.outputDBField"
										var="label" ></spring:message>
									<div class="table-cell-label">${label}</div>
									<div class="input-group">
										<form:input path="outputDbField"
									cssClass="form-control table-cell input-sm" tabindex="4"
									id="outputDbField" data-toggle="tooltip" data-placement="bottom"
									title="${tooltip }"></form:input>
								<spring:bind path="outputDbField">
									<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
								</spring:bind>
									</div>
							</div>
						</div>
					<div id="complete_action_div">
					 <div class="col-md-12 no-padding">
						<div class="title2">
							<spring:message code="policymgmt.databaseQueries.conditionList" ></spring:message>
							<span class="title2rightfield"> <span
								class="title2rightfield-icon1-text"> 
								<a href="#" onclick="addAction();"><i
								class="fa fa-plus-circle"></i></a>	
								<a href="#" id="createQueryCondition" onclick="addAction();">
								<spring:message code="policymgmt.databaseQueries.conditionList.add" ></spring:message>
								</a>
							</span>
							</span>
						</div>
						</div> 
						<div class="col-md-12 inline-form no-padding" style="overflow:scroll;height:150px;width:100%;overflow:auto" id="action_grid_div">
							<table class="table table-hover" id="actionList">
							</table>
						</div>
					</div>
					<div id="complete_condition_div">
					 <div class="col-md-12 no-padding">
						<div class="title2">
							<spring:message code="policymgmt.databaseQueries.actionList" ></spring:message>
							<span class="title2rightfield"> <span
								class="title2rightfield-icon1-text"> 
								<a href="#" onclick="addCondition();"><i
								class="fa fa-plus-circle"></i></a>	
								<a href="#" id="createQueryAction" onclick="addCondition();">
								<spring:message code="policymgmt.databaseQueries.actionList.add" ></spring:message>
								</a>
							</span>
							</span>
						</div>
						</div> 
						<div class="col-md-12 inline-form no-padding" style="overflow:scroll;height:150px;width:100%;overflow:auto" id="action_grid_div">
							<table class="table table-hover" id="conditionList">
							</table>
						</div>
					</div>
					</div>
					</form:form>
					<div class="modal-footer padding10">
					<sec:authorize access="hasAuthority('CREATE_POLICY_CONFIGURATION')">
							<button type="button" id="addNewQuery" class="btn btn-grey btn-xs " style="display: none"
								onclick="createUpdateDatabaseQueryWithCon('<%=ControllerConstants.CREATE_DATABASE_QUERY%>');">
								<spring:message code="btn.label.add" ></spring:message>
							</button>
					</sec:authorize>
					<sec:authorize access="hasAuthority('UPDATE_POLICY_CONFIGURATION')">
							<button type="button" id="updateQuery_btn" class="btn btn-grey btn-xs " style="display: none"
								onclick="createUpdateDatabaseQueryWithCon('<%=ControllerConstants.UPDATE_DATABASE_QUERY%>');">
								<spring:message code="btn.label.update" ></spring:message>
							</button>
					</sec:authorize>
					<button type="button" class="btn btn-grey btn-xs "
							data-dismiss="modal"
							onclick="closeFancyBox();reloadQueryGridData();setToOne();" id="closequery_btn">
							<spring:message code="btn.label.close" ></spring:message>
						</button>
					</div>
				</div>
			</div>
<script>
$(document).ready(function() {
	changeConditionList();
	manageConditionActionOutputDB();
});

var i = 1;

function addAction(){
	i += 1;
	var tableString = '<tr id = "act'+i+'">';
		tableString += '<td><center><input type="checkbox" id= "databaseKey'+i+'"/></center></td>';
		tableString += '<td><input type="text" id="databaseFieldName'+i+'" cssClass="form-control table-cell input-sm" data-placement="bottom"/><span class="required">*</span></td>';
		tableString += '<td><select id="policyOperator'+i+'" cssClass="form-control table-cell input-sm" data-placement="bottom">';
		tableString += '<c:forEach var="policyOperatorEnum" items="${policyOperatorEnum}">';										
		tableString += '<option value="${policyOperatorEnum.value}">${policyOperatorEnum.name}</option>';
		tableString += '</c:forEach>';
		tableString += '</td>';
		tableString += '<td><input type="text" name="unifiedField'+i+'" maxlength="100" id="unifiedField'+i+'" onBlur="validateUnifiedFields(unifiedField'+i+')"  cssClass="form-control table-cell input-sm"  data-placement="bottom" > </td>';	
 		tableString += '<td>';
		tableString += '<center><a href="#" onclick="deleteAction('+i+')"><i class="fa fa-trash"></i></a></center>';
		tableString += '</td>';
		tableString += '</tr>';	
		
	$("#actionList").append(tableString);
	var unifiedFieldData= 'unifiedField'+i;
	loadUnfiedAutoComplete(unifiedFieldData,'getAllUnifiedField');
	validateUnifiedFieldsAlphaNumeric(unifiedFieldData);
}

var j = 1;

function addCondition(){
	j += 1;
	var tableString = '<tr id = "con'+j+'">';
		tableString += '<td><input type="text" id="condatabaseFieldName'+j+'" cssClass="form-control table-cell input-sm" data-placement="bottom"/><span class="required">*</span></td>';
		tableString += '<td><input type="text" name="conUnifiedField'+j+'" id="conUnifiedField'+j+'" onBlur="validateUnifiedFields(conUnifiedField'+j+')" cssClass="form-control table-cell input-sm" data-toggle="tooltip" maxlength="100" data-placement="bottom"  title="${tooltip}" > </td>';	
		tableString += '<td>';
		tableString += '<center><a href="#" onclick="deleteCondition('+j+')"><i class="fa fa-trash"></i></a></center>'
		tableString += '</td>';
		tableString += '</tr>';
	$("#conditionList").append(tableString);
	var unifiedFieldData= 'conUnifiedField'+j;
	loadUnfiedAutoComplete(unifiedFieldData,'getAllUnifiedField');
	validateUnifiedFieldsAlphaNumeric(unifiedFieldData);
}

function validateUnifiedFieldsAlphaNumeric(element){
	$("#"+element).keypress(function (e) {
	    var regex = new RegExp("^[a-zA-Z0-9_]+$");
	    var str = String.fromCharCode(!e.charCode ? e.which : e.charCode);
	    if (regex.test(str)) {
	        return true;
	    }

	    e.preventDefault();
	    return false;
	});
}

function validateUnifiedFields(element){
	var id= element.id;
	var value=element.value;
	var msg="<spring:message code='dbquery.unifiedfield'></spring:message>";
	if(!value.match(/^[a-zA-Z]+[a-zA-Z0-9_]*$/g) && value != ""){	
		$("#" + id).css("border-color","red");
		var lmessage = document.getElementById('dcMessage');
		lmessage.style.color = '#FF0000';
		document.getElementById('dcMessage').innerHTML = msg
		$("#addNewQuery").prop('disabled', true);
		$("#updateQuery_btn").prop('disabled', true);
	}
	else{
		$("#" + id).css("border-color","initial");
		checkEachElementColor();
	} 
	
}
function checkEachElementColor(){
	var res = $("input[type=text]").toArray().some(function(el) {
	    return $(el).css("border-color") === "rgb(255, 0, 0)"
	  });
	if(res==false){
		document.getElementById('dcMessage').innerHTML = '';
		$("#addNewQuery").prop('disabled', false);
		$("#updateQuery_btn").prop('disabled', false);
	}
}
function deleteAction(id){
	$("#act"+id).remove();
}

function deleteCondition(id){
	$("#con"+id).remove();
}

function createUpdateDatabaseQueryWithCon(urlAction){
	var actIndex = i;
	var conIndex = j;
	i=1;
	j=1;
	createUpdateDatabaseQuery(urlAction,actIndex,conIndex);
}

function fillActionTable(a,rowData){
	j=a;
	var tableString = '<tr id = "con'+j+'">';
	tableString += '<td><input type="text" value="'+rowData.fieldName+'" id="condatabaseFieldName'+j+'" cssClass="form-control table-cell input-sm" data-placement="bottom"/><span class="required">*</span></td>';
	tableString += '<td><input type="text" name="conUnifiedField'+j+'" id="conUnifiedField'+j+'" onBlur="validateUnifiedFields(conUnifiedField'+j+')"  cssClass="form-control table-cell input-sm" data-toggle="tooltip" maxlength="100" data-placement="bottom" title="${tooltip}" >  </td>';	
	tableString += '<td>';
	tableString += '<center><a href="#" onclick="deleteCondition('+j+')"><i class="fa fa-trash"></i></a></center>'
	tableString += '</td>';
	tableString += '</tr>';
	$("#conditionList").append(tableString);
	$("#conUnifiedField"+j).val(rowData.unifiedField)
	.find("option[value=" + rowData.unifiedField +"]").attr('selected', true);
	var unifiedFieldData= 'conUnifiedField'+j;
	loadUnfiedAutoComplete(unifiedFieldData,'getAllUnifiedField');
	validateUnifiedFieldsAlphaNumeric(unifiedFieldData);
	document.getElementById('dcMessage').innerHTML = '';
}


function fillConditionTable(a,row){
	i=a;
	var tableString = '<tr id = "act'+i+'">';
	tableString += '<td><center><input type="checkbox" id= "databaseKey'+i+'"/></center></td>';
	tableString += '<td><input type="text" value="'+row.fieldName+'" id="databaseFieldName'+i+'" cssClass="form-control table-cell input-sm" data-placement="bottom"/><span class="required">*</span></td>';
	tableString += '<td><select id="policyOperator'+i+'" cssClass="form-control table-cell input-sm" data-placement="bottom">';
	tableString += '<c:forEach var="policyOperatorEnum" items="${policyOperatorEnum}">';										
	tableString += '<option value="${policyOperatorEnum.value}">${policyOperatorEnum.name}</option>';
	tableString += '</c:forEach>';
	tableString += '</td>';
	tableString += '<td><input type="text" name="unifiedField'+i+'" id="unifiedField'+i+'"onBlur="validateUnifiedFields(unifiedField'+i+')" cssClass="form-control table-cell input-sm" data-toggle="tooltip" maxlength="100" data-placement="bottom" title="${tooltip}" > <span class="input-group-addon add-on last"> </td>';	
	tableString += '<td>';
	tableString += '<center><a href="#" onclick="deleteAction('+i+')"><i class="fa fa-trash"></i></a></center>';
	tableString += '</td>';
	tableString += '</tr>';	
	$("#actionList").append(tableString);
	$("#unifiedField"+i).val(row.unifiedField)
	.find("option[value=" + row.unifiedField +"]").attr('selected', true);
 	$("#policyOperator"+i).val(row.operator)
	.find("option[value=" + row.operator +"]").attr('selected', true);
 	$("#databaseKey"+i).prop('checked', JSON.parse(row.databaseKey));
 	var unifiedFieldData= 'unifiedField'+i;
	loadUnfiedAutoComplete(unifiedFieldData,'getAllUnifiedField');
	validateUnifiedFieldsAlphaNumeric(unifiedFieldData);
	document.getElementById('dcMessage').innerHTML = '';
}

function changeConditionList(){
	var conditionExpressionEnable = document.getElementById("conditionExpressionEnable");
	if(conditionExpressionEnable.value == 'false'){
		$("#conditionExpressionDiv").hide();
		$("#logicalOperatorDiv").show();
		$("#complete_action_div").show();
	}else{
		$("#logicalOperatorDiv").hide();
		$("#complete_action_div").hide();
		$("#conditionExpressionDiv").show();
	}
}


function setToOne(){
	i=1;
	j=1;
	document.getElementById('dcMessage').innerHTML = '';
}

function manageConditionActionOutputDB() {
	var cacheEnable = $("#cacheEnable").val();
	if (cacheEnable === "false") {
		$("#complete_action_div").hide();
		$("#complete_condition_div").hide();
		$("#logicalOperatorDiv").hide();
		$("#divOutputDbField").hide();
	} else {
		$("#complete_action_div").show();
		$("#complete_condition_div").show();
		$("#logicalOperatorDiv").show();
		$("#divOutputDbField").show();
	}
	
}
</script>
				
