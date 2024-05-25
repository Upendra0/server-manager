<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="/WEB-INF/ShowErrorTag.tld" prefix="elitecoreError"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<%@page import="com.elitecore.sm.common.constants.BaseConstants"%>
<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>

<form:form modelAttribute="policy_rule_condition_form_bean" method="POST" condition="#" id="create-policy-rule-condition">
	<input type="hidden" id="serviceInstanceId" name="serviceInstanceId" value="${serviceInstanceId}">
			<input type="hidden" id="serviceId" name="serviceId" value="${serviceId}">
			<input type="hidden" id="serviceType" name="serviceType" value="<%=BaseConstants.PROCESSING_SERVICE%>">
			<input type="hidden" id="serviceName" name="serviceName" value="${serviceName}">
	<form:hidden path="server.id" id="server_id" value='${instanceId}' ></form:hidden> 
	<input type="hidden" value="0" id="id" name="id" />
	<div id="div_condition" class="box-body table-responsive no-padding box"
		style="display: block;">
		
		<div class="col-md-12 padding10">
			<div class="form-group">
				<spring:message code="business.policy.create.condition.name.label"  var="tooltipName"></spring:message>
				<div class="table-cell-label">${tooltipName}<span class="required">*</span></div>
				<div class="input-group ">
					<form:input class="form-control " 
						id="condition_name" path="name" name="name" style="resize: none;"
						data-toggle="tooltip" data-placement="bottom" title="${tooltipName}"></form:input>
					<spring:bind path="name">
						<elitecoreError:showError
							errorMessage="${status.errorMessage}"></elitecoreError:showError>
					</spring:bind>
				</div>
			</div>
		</div>
		
		<div class="col-md-12 padding10">
			<div class="form-group">
				<spring:message code="business.policy.create.condition.description.label"  var="tooltipDesc"></spring:message>
				<div class="table-cell-label">${tooltipDesc}</div>
				<div class="input-group ">
					<form:textarea class="form-control " rows="4" cols="30"
						id="condition_description" path="description" name="description" style="resize: none;"
						data-toggle="tooltip" data-placement="bottom" title="${tooltipDesc}"></form:textarea>
					<spring:bind path="description">
						<elitecoreError:showError
							errorMessage="${status.errorMessage}"></elitecoreError:showError>
					</spring:bind>
				</div>
			</div>
		</div>
				
		<div class="col-md-12 padding10">
			<div class="form-group">
				<spring:message code="business.policy.condition.create.new.label"
					var="newtooltip" ></spring:message>
				<div class="table-cell-label">
					<label class="radio-inline" data-toggle="tooltip"
						data-placement="bottom" title="${newtooltip}"> <input
						type="radio" name="conditionType" id="condition_new" value="newcondition"
						checked> ${newtooltip}
					</label>
				</div>
				<div class="table-cell  no-border no-shadow">
					<spring:message code="business.policy.condition.create.existing.label"
						var="existTootip" ></spring:message>
					<label class="radio-inline" data-toggle="tooltip"
						data-placement="bottom" title="${existTootip}"> <input
						type="radio" name="conditionType" id="condition_existing"
						value="existingcondition"> ${existTootip}
					</label>
				</div>
			</div>
		</div>
		<div class="col-md-12 padding10" id="existing_condition_list_main_div"
			style="display: none;">
			<div class="fullwidth">
				<div class="title2">
					<spring:message
						code="business.policy.condition.existing.list.section.title" ></spring:message>
				</div>
			</div>
			<div class="box-body table-responsive no-padding box" >
				<table class="table table-hover" id="existing_condition_list"></table>
				<div id="existing_condition_list_div"></div>
			</div>
		</div> 
		<div class="col-md-12 padding10" id="condition_type_div" style="display: block;">
			<div class="form-group">
				<div class="table-cell-label">
					<spring:message
						code="business.policy.condition.create.condition.type.label" ></spring:message>
				</div>
				<div class="table-cell  no-border no-shadow">
					<%-- <spring:message code="business.policy.condition.create.condition.static"
						var="staticTooltip" ></spring:message>
					
					<label class="radio-inline" data-toggle="tooltip"
						data-placement="bottom" title="${staticTooltip}">
						<form:radiobutton  path="type" name="type" id="condition_static"
						value="static" checked="true" title="${staticTooltip}"></form:radiobutton>  
					</label> ${staticTooltip}
 					--%>
					<spring:message
						code="business.policy.condition.create.condition.expression"
						var="conditionExp" ></spring:message>
						
					<label class="radio-inline" data-toggle="tooltip"
						data-placement="bottom" title="${conditionExp}">
						
						<form:radiobutton  path="type" name="type" id="condition_expression"
						value="expression" checked="true" title="${conditionExp}" ></form:radiobutton> 
					</label> ${conditionExp}
					
 					<spring:message code="business.policy.condition.create.condition.dynamic"
						var="conditionbDynamic" ></spring:message>
					<label class="radio-inline" data-toggle="tooltip"
						data-placement="bottom" title="${conditionbDynamic}">
						
						<form:radiobutton  path="type" name="type" id="condition_dynamic"
						value="dynamic" checked="true" title="${conditionbDynamic}" ></form:radiobutton> 
					</label> ${conditionbDynamic}
				</div>
			</div>
		</div>
				
		<div class="col-md-12 padding10" id="condition_static_operation_div"
			style="display: block;">
		</div>
		<div class="col-md-12 padding10" id="condition_expression_syntax_div">
				<%@ include file="commonConditionExpression.jsp" %>  
		</div>
		
		<div class="col-md-12 padding" id="checkExpressionValidationBtnDiv">
			<div class="form-group">
				<div class="table-cell-label"></div>
				<div class="input-group ">
					<% String kubernetes_env = System.getenv("KUBERNETES_ENV");
				   	   if (kubernetes_env ==null || kubernetes_env.equalsIgnoreCase("false")){  %>                        
				   	   <button id="checkExpressionValidationBtn" class="btn btn-grey btn-xs" type="button" onclick="validateExpression();">
						    <spring:message code="btn.label.expression.validate" ></spring:message>
					    </button>
					 <%} %>
					<div class="table-cell-label" id="expressionValidationMsg" style="width: 560px;color: #004C99;"></div>
				</div>
				<div style="height: 15px"></div>
				<div id="progress-bar-div-condition" style="display: none;">
							<jsp:include page="../common/processing-bar.jsp"></jsp:include>
				</div>	
			</div>
		</div>
		
		<div class="col-md-12 padding10" id="condition_dynamic_div" style="display: none;">
			<div class="col-md-6 no-padding">
						<div class="form-group">
									<spring:message code="business.policy.unified.section.column.unified"
										var="tooltip" ></spring:message>
										<spring:message code="business.policy.unified.section.column.unified"
										var="label" ></spring:message>
									<div class="table-cell-label">${label}</div>
									<div class="input-group">
										<jsp:include page="../common/formAutocomplete.jsp">
											<jsp:param name="unifiedField" value="unifiedField" ></jsp:param>
											<jsp:param name="unifiedFieldPath" value="unifiedField" ></jsp:param>
										</jsp:include>
										<spring:bind path="unifiedField">
										<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
										</spring:bind>
									</div>
							</div>
						</div>
					<div class="col-md-6 no-padding">
							<div class="form-group">
									<spring:message code="policymgmt.databaseQueries.logical"
										var="tooltip" ></spring:message>
										<spring:message code="policymgmt.databaseQueries.logical"
										var="label" ></spring:message>
									<div class="table-cell-label">${label}</div>
									<div class="input-group">
									<form:select path="operator"
											cssClass="form-control table-cell input-sm" tabindex="4"
											id="operator" data-toggle="tooltip"
											data-placement="bottom" title="${tooltip }">
											<option value = "">Select logical operator</option>
											<c:forEach var="logicalOperatorEnum" items="${logicalOperatorEnum}">
												<form:option value="${logicalOperatorEnum.value}">${logicalOperatorEnum.name}</form:option>
											</c:forEach>
									</form:select>
									</div>
							</div>
					</div>
					<div class="col-md-6 no-padding">
							<div class="form-group">
									<spring:message code="business.policy.condition.existing.list.column.action"
										var="tooltip" ></spring:message>
										<spring:message code="business.policy.condition.existing.list.column.action"
										var="label" ></spring:message>
									<div class="table-cell-label">${label}<span class="required">*</span></div>
									<div class="input-group">
									<form:select path="value"
											cssClass="form-control table-cell input-sm" tabindex="4"
											id="value" data-toggle="tooltip"
											data-placement="bottom" title="${tooltip }">
											<c:forEach var="databaseQueryList" items="${databaseQueryList}">
												<form:option value="${databaseQueryList}">${databaseQueryList}</form:option>
											</c:forEach>
									</form:select>
									<spring:bind path="value">
										<elitecoreError:showError
													errorMessage="${status.errorMessage}"></elitecoreError:showError>
										</spring:bind>
									</div>
							</div>
					</div>
					
			</div>
		
		
		
		
		
		
		
		
		<div class="col-md-12 padding10">
			<div class="form-group">
				<div class="table-cell-label">&nbsp;</div>
				<div class="table-cell  no-border no-shadow inline-form">
					<span>
						<%-- <button type="button" id="condition_expression_verify_btn_div" style="display: none;"
							class="btn btn-grey btn-xs " onclick="verifyConditionExpression();">
							<spring:message code="btn.label.verify" ></spring:message>
						</button> --%>
						<sec:authorize access="hasAnyAuthority('CREATE_POLICY_ACTION_CONFIGURATION')">  
							<button type="button" id="update_condition_expression_save_btn_div"
								 class="btn btn-grey btn-xs "
								onclick="saveConditionDetails('<%=ControllerConstants.ADD_POLICY_RULE_CONDITION%>',0,'create'); ">
								<spring:message code="btn.label.save" ></spring:message>
							</button>
						</sec:authorize>
						
						<sec:authorize access="hasAnyAuthority('UPDATE_POLICY_ACTION_CONFIGURATION')">  
							<button type="button" id="update_condition_expression_update_btn_div"
								 class="btn btn-grey btn-xs "
								onclick="saveConditionDetails('<%=ControllerConstants.ADD_POLICY_RULE_CONDITION%>',0,'update'); ">
								<spring:message code="btn.label.update" ></spring:message>
							</button>
						</sec:authorize>
						<button type="button" class="btn btn-grey btn-xs " onclick="resetConditionDetails();">
							<spring:message code="btn.label.reset" ></spring:message>
						</button>
						<button type="button" id="rule_condition_cancel_btn" class="btn btn-grey btn-xs" 
								onclick="closeFancyBox();">
							<spring:message code="btn.label.cancel" ></spring:message>
						</button>
					</span> 
					
					<span id="condition_existing_save_btn_div" style="display: none;">
						<button type="button" class="btn btn-grey btn-xs ">
							<spring:message code="btn.label.save" ></spring:message>
						</button>
						<button type="button" class="btn btn-grey btn-xs ">
							<spring:message code="btn.label.save.as" ></spring:message>
						</button>
						<button type="button" class="btn btn-grey btn-xs ">
							<spring:message code="btn.label.cancel" ></spring:message>
						</button>
					</span>

				</div>
			</div>
		</div>
		<div class="col-md-12 padding" id="note_condition_expression_validate">
			<div class="form-group">
				<div class="table-cell-label">&nbsp;</div>
				<div>
					<span class="required">Note: &nbsp; During expression validation all unified field values will be consider as 1.</span>
					<br><span class="required" style='padding-left : 32px'> &nbsp; All Static values will be written in single quote(' '). </span>
					<br><span class="required">Example: &nbsp; general1 == '2'</span>							  
				</div>
			</div>
		</div>
		</div>

	<!-- Save Condition pop up code end here -->

<script src="${pageContext.request.contextPath}/customJS/policyManager.js<%= "?v=" + Math.random() %>" type="text/javascript"></script>

	<script type="text/javascript">
				
	
	$(document).ready(function() {
		$("#condition_expression").prop("checked", true);
		$('input:radio[name=type]').change(function () {
        	if ( $('#condition_expression').is(":checked")) {
        		$("#checkExpressionValidationBtnDiv").show();
        		$("#note_condition_expression_validate").show();
            }else{
            	$("#checkExpressionValidationBtnDiv").hide();
          		$("#note_condition_expression_validate").hide();
            }            
        });
	});
	
	function resetConditionDetails(conditionType) {
	//clearAllMessages();
	//resetWarningDisplayPopUp();
	$("#expressionValidationMsg").html("");
		$("#condition_expression").prop("checked", true);
		$("#condition_expression_syntax_div").show();
		$("#expressionValidationMsg").html("");
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

	function setCurrentCondition(selectedCondition) {
		var textAreaVal = $('#condition_syntax').val();
		textAreaVal += selectedCondition + ' ';
		$('#condition_syntax').val(textAreaVal);

	}

	function saveConditionDetails(urlAction, conditionId, pageType) {
		/* if($("#value").val()===null){
			$("#value").prop('required',true);
		} */
	/* 	else{ */
			addNewConditionToDB(urlAction, conditionId, pageType);
		/* } */
	}

	function closeConditionDetails() {
		$('#condition_field_section').hide();
	}

	
	$('#div_condition input').on('change', function() {
		var selectedActionVal = $('input:radio[name=type]:checked', '#div_condition').val();
		if(selectedActionVal == 'dynamic'){
			$("#condition_expression_syntax_div").hide();
			$("#condition_dynamic_div").show();
			$("#checkExpressionValidationBtnDiv").hide();
      		$("#note_condition_expression_validate").hide();
		}else{
			$("#checkExpressionValidationBtnDiv").show();
      		$("#note_condition_expression_validate").show();
		}
	})
	
	
	function addNewConditionToDB(urlAction, conditionId, pageType) {
		var unifiedField = "";
		var operator = "";
		var value = "";
		var str = $("#create-policy-rule-condition").serialize();
		resetWarningDisplayPopUp();
		if(document.getElementById('condition_dynamic').checked){
			unifiedField=$("#unifiedField").val();
			operator=$("#operator").val();
			value=$("#value").val();
		}
		$("#create-condition-button-div").hide();
		showProgressBar();
		parent.resizeFancyBox();
		$.ajax({
			url : 'addPolicyRuleCondition',
			cache : false,
			async : true,
			dataType : 'json',
			type : "POST",
			data : {
				"id" : $("#id").val(),
				"name" : $("#condition_name").val(),
				"alias" : $("#condition_name").val(),
				"description" : $("#condition_description").val(),
				"condition" : $('input:radio[name=condition]:checked','#div_condition').val(),
				"conditionExpression" : $("#condition_syntax").val(),
				"type" : $('input:radio[name=type]:checked', '#div_condition').val(),
				"server.id" : $("#server_id").val(),
				"pageType" : pageType,
				"unifiedField" : unifiedField,
				"operator" : operator,
				"value" : value,
				"policyConditionId" : policyConditionId

			},
			success : function(data) {
				hideProgressBar();

				var response = eval(data);
				var responseCode = response.code;
				var responseMsg = response.msg;
				var responseObject = response.object;
				if (responseCode == "200") {
					$("#update_condition_expression_update_btn_div").hide();
					$("#update_condition_expression_save_btn_div").hide();
					
					closeFancyBox();
					try{resetConditionSearchParams();}catch(err){}

					if (currentPage != 'updatePage')
						resetConditionDetails();
					closeFancyBox();
					showSuccessMsg(responseMsg);
					reloadConditionGridData();
				} else if (responseObject != undefined
						&& responseObject != 'undefined' && responseCode == "400") {
					$.each(responseObject, function(key, val) {
						if (key == 'name') {
							$("#condition_name").next().children().first().attr(
									"data-original-title", val).attr("id","condition_name_error");
						} else if (key == 'description') {
							$("#condition_description").next().children().first()
									.attr("data-original-title", val);
						} else if (key == 'expression') {
							$("#condition_syntax").next().children().first().attr(
									"data-original-title", val);
						} else if (key == 'conditionExpression'){
							$("#condition_syntax").next().children().first().attr(
									"data-original-title", val).attr("id","condition_syntax_error");
						} 
						else {
							$("#" + key).next().children().first().attr(
									"data-original-title", val);
						}
						addErrorClassWhenTitleExistPopUp($(
								"#GENERIC_ERROR_MESSAGE", window.parent.document)
								.val());
					});

				} else {
					resetWarningDisplayPopUp();
					showErrorMsgPopUp(responseMsg);
				}
				$("#create-condition-button-div").show();
			},
			error : function(xhr, st, err) {
				hideProgressBar();
				$("#create-condition-button-div").show();
				handleGenericError(xhr, st, err);
				closeFancyBox();
			}
		});
	}
	
</script>
</form:form>
