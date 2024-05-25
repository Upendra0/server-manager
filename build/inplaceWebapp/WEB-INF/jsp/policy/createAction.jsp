<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="/WEB-INF/ShowErrorTag.tld" prefix="elitecoreError"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<%@page import="com.elitecore.sm.common.constants.BaseConstants"%>

<script src="${pageContext.request.contextPath}/customJS/autoSuggestionForUnifiedField.js<%= "?v=" + Math.random() %>" type="text/javascript"></script>
<form:form modelAttribute="policy_rule_action_form_bean" method="POST" action="#" id="create-policy-rule-action">
<input type="hidden" id="serviceInstanceId" name="serviceInstanceId" value="${serviceInstanceId}">
			<input type="hidden" id="serviceId" name="serviceId" value="${serviceId}">
			<input type="hidden" id="serviceType" name="serviceType" value="<%=BaseConstants.PROCESSING_SERVICE%>">
			<input type="hidden" id="serviceName" name="serviceName" value="${serviceName}">
	<form:hidden path="server.id" id="server_id" value='${instanceId}' ></form:hidden> 
	<input type ="hidden" value= "0" id="id" name="id"/>
	<div id="div_action">
			<jsp:include page="../common/responseMsg.jsp" ></jsp:include>
			<jsp:include page="../common/responseMsgPopUp.jsp" ></jsp:include>
		
		<div id="content-scroll-d" class="content-scroll">
		<div class="modal-body padding inline-form">
			    <!-- Form content start here  -->
				 <div class="col-md-12 no-padding">
					<div class="form-group">
						<spring:message code="business.policy.create.action.name.label"  var="tooltipName"></spring:message>
						<div class="table-cell-label">${tooltipName}<span class="required">*</span></div>
						<div class="input-group ">
							<form:input class="form-control " 
								id="action_name" path="name" name="name" style="resize: none;"
								data-toggle="tooltip" data-placement="bottom" title="${tooltipName}"></form:input>
							<spring:bind path="name">
								<elitecoreError:showError
									errorMessage="${status.errorMessage}"  ></elitecoreError:showError>
								</spring:bind>
						</div>
					</div>
				</div>
				<div class="col-md-12 padding">
					<div class="form-group">
						<spring:message code="business.policy.create.action.description.label"  var="tooltipDesc"></spring:message>
						<div class="table-cell-label">${tooltipDesc}</div>
						<div class="input-group ">
							<form:textarea class="form-control " rows="4" cols="30"
								id="action_description" path="description" name="description" style="resize: none;"
								data-toggle="tooltip" data-placement="bottom" title="${tooltipDesc}"></form:textarea>
							<spring:bind path="description">
								<elitecoreError:showError
									errorMessage="${status.errorMessage}" ></elitecoreError:showError>
								</spring:bind>
						</div>
					</div>
				</div>
			
		<div class="col-md-12 padding">
			<div class="form-group">
				<spring:message code="business.policy.action.create.new.label"
					var="newtooltip" ></spring:message>
				<div class="table-cell-label">
					<label class="radio-inline" data-toggle="tooltip"
						data-placement="bottom" title="${newtooltip}"> <input
						type="radio" name="actionType" id="action_new" value="newaction"
						checked> ${newtooltip}
					</label>
				</div>
				<div class="table-cell  no-border no-shadow">
					<spring:message code="business.policy.action.create.existing.label"
						var="existTootip" ></spring:message>
					<label class="radio-inline" data-toggle="tooltip"
						data-placement="bottom" title="${existTootip}"> <input
						type="radio" name="actionType" id="action_existing"
						value="existingaction"> ${existTootip}
					</label>
				</div>
			</div>
		</div>
		<div class="col-md-12 padding" id="existing_action_list_main_div"
			style="display: none;">
			<div class="fullwidth">
				<div class="title2">
					<spring:message
						code="business.policy.action.existing.list.section.title" ></spring:message>
				</div>
			</div>
			<div class="box-body table-responsive no-padding box" >
				<table class="table table-hover" id="existing_action_list"></table>
				<div id="existing_action_list_div"></div>
			</div>
		</div> 
		<div class="col-md-12 padding" id="action_type_div" style="display: block;">
			<div class="form-group">
				<div class="table-cell-label">
					<spring:message
						code="business.policy.action.create.action.type.label" ></spring:message>
				</div>
				<div class="table-cell  no-border no-shadow">
					<spring:message code="business.policy.action.create.action.static"
						var="staticTooltip" ></spring:message>
					
					<label class="radio-inline" data-toggle="tooltip"
						data-placement="bottom" title="${staticTooltip}">
						<form:radiobutton  path="type" name="type" id="action_static"
						value="static" title="${staticTooltip}"></form:radiobutton>  
					</label> ${staticTooltip}

					<spring:message
						code="business.policy.action.create.action.expression"
						var="actionExp" ></spring:message>
						
					<label class="radio-inline" data-toggle="tooltip"
						data-placement="bottom" title="${actionExp}">
						
						<form:radiobutton  path="type" name="type" id="action_expression"
						value="expression" title="${actionExp}" ></form:radiobutton> 
					</label> ${actionExp}
 					<spring:message code="business.policy.action.create.action.dynamic"
						var="actionbDynamic" ></spring:message>
					<label class="radio-inline" data-toggle="tooltip"
						data-placement="bottom" title="${actionbDynamic}"> 
						<form:radiobutton path="type" name="type" id="action_dynamic"
						value="dynamic" title="${actionbDynamic}"></form:radiobutton>
					</label>${actionbDynamic}
				</div>
			</div>
		</div>
	
		<div class="col-md-12 padding" id="action_static_operation_div"
			style="display: block;">
			<div class="form-group">
				<div class="table-cell-label">
					<spring:message
						code="business.policy.action.create.action.operation.label" ></spring:message>
				</div>
				<div class="table-cell  no-border no-shadow">
					<spring:message
						code="business.policy.action.create.action.operation.invalid"
						var="invalid" ></spring:message>
					<label class="radio-inline" data-toggle="tooltip"
						data-placement="bottom" title="${invalid}"> 
						<form:radiobutton checked="true"  path="action" name="action" id="markAsInvalid"
						value="Invalid" title="${invalid}" ></form:radiobutton> 
						${invalid}
					</label>
					<spring:message
						code="business.policy.action.create.action.operation.filter"
						var="filter" ></spring:message>
					<label class="radio-inline" data-toggle="tooltip"
						data-placement="bottom" title="${filter}"> 
						<form:radiobutton  path="action" name="action" id="markAsFilter"
						value="Filter" title="${filter}" ></form:radiobutton> 
						${filter}
					</label>
					<spring:message
						code="business.policy.action.create.action.operation.clone"
						var="clone" ></spring:message>
					<label class="radio-inline" data-toggle="tooltip"
						data-placement="bottom" title="${clone}">
						 <form:radiobutton  path="action" name="action" id="markAsClone"
						value="Clone" title="${clone}" ></form:radiobutton> 
						${clone}
					</label>
				
				</div>
				
				
				<div class="input-group ">						
					  <form:input  class="form-control" id="action" path="action" name="action" style="resize: none;"
								 data-toggle="tooltip" data-placement="bottom" title="Enter Number of Clone" onkeydown="isNumericOnKeyDown(event)" maxlength="5"></form:input>
						<spring:bind path="action">
								<elitecoreError:showError
									errorMessage="${status.errorMessage}"  ></elitecoreError:showError>
								</spring:bind>
				   </div>
				
				
			</div>
		</div>
		<div class="col-md-12 padding" id="action_expression_syntax_div">
			<%@ include file="commonActionExpression.jsp" %> 
		</div>
		
		<div class="col-md-12 padding">
		<div class="form-group" id="checkExpressionValidationBtnDivForAction">
			<div class="table-cell-label"></div>
			<div class="input-group ">
				<% String kubernetes_env = System.getenv("KUBERNETES_ENV");
				   if (kubernetes_env ==null || kubernetes_env.equalsIgnoreCase("false")){  %>
					<button id="checkExpressionValidationBtnForAction" class="btn btn-grey btn-xs" type="button" onclick="validateExpressionForAction();">
						<spring:message code="btn.label.expression.validate" ></spring:message>
					</button>
				<%} %>
				<div class="table-cell-label" id="expressionValidationMsgForAction" style="width: 560px;color: #004C99;"></div>
			</div>
			<div style="height: 15px"></div>
			<div id="progress-bar-div-action" style="display: none;">
				<jsp:include page="../common/processing-bar.jsp"></jsp:include>
			</div>
		</div>
		</div>
		
		<div class="col-md-12 padding" id="action_dynamic_div">
			<div class="col-md-6 no-padding">
							<div class="form-group">
								<spring:message code="parsing.service.add.attr.uni.field" var="tooltip" ></spring:message>
								<spring:message code="business.policy.rule.grid.column.action" var="label" ></spring:message>
								<div class="table-cell-label">${label}<span class="required">*</span></div>
								<div class="input-group">
										<jsp:include page="../common/autocomplete.jsp">
											<jsp:param name="unifiedField" value="dynamic_action" ></jsp:param>
										</jsp:include>
										<%-- <elitecoreError:showError
													errorMessage="${status.errorMessage}" ></elitecoreError:showError> --%>
									<%-- <select name="dynamic_action" id="dynamic_action"
										class="form-control table-cell input-sm" data-toggle="tooltip"
										data-placement="bottom" title="${tooltip}">
										<option value = "">Select unified field</option>
										<c:forEach var="unifiedField" items="${unifiedField}">
											<option value="${unifiedField.name}">${unifiedField.name}</option>
										</c:forEach>
									</select> 
										<elitecoreError:showError
													errorMessage="${status.errorMessage}" ></elitecoreError:showError> --%>
								</div>
							</div>
			</div><div class="col-md-1 no-padding"><font size="3">=</font></div>
			<div class="col-md-5 no-padding">
							<div class="form-group">
									<div class="input-group">
								<spring:message code="business.policy.rule.grid.column.dynamicaction"
										var="tooltip" ></spring:message>	
								<select name="dynamicQueryEnum" id="dynamicQueryEnum"
									class="form-control table-cell input-sm" data-toggle="tooltip"
									data-placement="bottom" title="${tooltip}">
									<c:forEach var="databaseQueryList" items="${databaseQueryList}">
									<option value="${databaseQueryList}">${databaseQueryList}</option>
									</c:forEach>
								</select>
								<elitecoreError:showError
													errorMessage="${status.errorMessage}" ></elitecoreError:showError>
									</div>
							</div>
				</div>
			</div>
		
		
		<div class="col-md-12 padding">
			<div class="form-group">
				<div class="table-cell-label">&nbsp;</div>
				<div class="table-cell  no-border no-shadow inline-form">
					<span>
						<%-- <button type="button" id="action_expression_verify_btn_div" style="display: none;"
							class="btn btn-grey btn-xs " onclick="verifyActionExpression();">
							<spring:message code="btn.label.verify" ></spring:message>
						</button> --%>
						<sec:authorize access="hasAnyAuthority('CREATE_POLICY_ACTION_CONFIGURATION')">  
							<button type="button" id="update_action_expression_save_btn_div"
								 class="btn btn-grey btn-xs "
								onclick="saveActionDetails('<%=ControllerConstants.ADD_POLICY_RULE_ACTION%>',0,'create');">
								<spring:message code="btn.label.save" ></spring:message>
							</button>
						</sec:authorize>
						
						<sec:authorize access="hasAnyAuthority('UPDATE_POLICY_ACTION_CONFIGURATION')">  
							<button type="button" id="update_action_expression_update_btn_div"
								 class="btn btn-grey btn-xs "
								onclick="saveActionDetails('<%=ControllerConstants.ADD_POLICY_RULE_ACTION%>',0,'update');">
								<spring:message code="btn.label.update" ></spring:message>
							</button>
						</sec:authorize>
						<button type="button" id = "reset_action_btn"class="btn btn-grey btn-xs "
							onclick="resetActionDetails();">
							<spring:message code="btn.label.reset" ></spring:message>
						</button>
						<button type="button" id="rule_action_cancel_btn" class="btn btn-grey btn-xs" 
								onclick="closeFancyBox();">
							<spring:message code="btn.label.cancel" ></spring:message>
						</button>
					</span> 
					
					<span id="action_expression_save_btn_div" style="display: none;">
						<button type="button" id="action_expression_save_next_btn" style="display: none;" 
							class="btn btn-grey btn-xs " onclick="openSaveActionForm();">
							<spring:message code="btn.label.save.next" ></spring:message>
						</button>
					</span> 
					
					<span id="action_existing_save_btn_div" style="display: none;">
						<button type="button" class="btn btn-grey btn-xs ">
							<spring:message code="btn.label.save" ></spring:message>
						</button>
						<button type="button" class="btn btn-grey btn-xs ">
							<spring:message code="btn.label.save.as" ></spring:message>
						</button>
						<button type="button" class="btn btn-grey btn-xs" onclick="closeFancyBox();">
							<spring:message code="btn.label.cancel" ></spring:message>
						</button>
					</span>

				</div>
			</div>
		</div>
		<div class="col-md-12 padding" id="note_expression_validate">
			<div class="form-group">
				<div class="table-cell-label">&nbsp;</div>
				<div>
					<span class="required">Note: &nbsp; During expression validation all unified field values will be consider as 1.</span>
					<br><span class="required" style='padding-left : 32px'> &nbsp; All Static values will be written in single quote(' '). </span>
					<br><span class="required">Example: &nbsp; general1 = general10+'2'</span>							  
				</div>
			</div>
		</div>
		
	</div>
	</div>
</div>

<script src="${pageContext.request.contextPath}/customJS/policyManager.js<%= "?v=" + Math.random() %>" type="text/javascript"></script>

	<script type="text/javascript">
	

	$(document).ready(function () {
		$('#action').hide();
		expressionValidationHideShow();
        $('input:radio[name=action]').change(function () {
        	
            if ( $('#markAsClone').is(":checked")) {
            	if( $("#action").val().trim() == '' ){
            		$("#action").val(1);
            	}
            	$('#action').show();
            }else{
            	$('#action').hide();
            }
            
        });
        
        
 		$('input:radio[name=type]').change(function () {
 			expressionValidationHideShow();
        });
    });
	
	function expressionValidationHideShow() {
		$("#expressionValidationMsgForAction").html("");
		$("#expressionValidationMsg").html("");
    	if ( $('#action_expression').is(":checked")) {
    		$('#condition_expression').prop('checked',false);
    		$("#checkExpressionValidationBtnDivForAction").show();
    		$("#note_expression_validate").show();
        }else{
        	$("#checkExpressionValidationBtnDivForAction").hide();
        	$("#checkExpressionValidationBtnDiv").hide();
        	$("#note_expression_validate").hide();
        }     
    	if($('#condition_expression').is(":checked")){
    		$('#action_expression').prop('checked',false);
    		$("#checkExpressionValidationBtnDiv").show();
    		$("#note_expression_validate").show();
    	}else{
    		$("#checkExpressionValidationBtnDiv").hide();
    	}
	}
	function resetInputDetails(){
		resetWarningDisplayPopUp();
		$("#expressionValidationMsgForAction").html("");
			if(actionType=="create"){
				
			 $("#update_action_expression_update_btn_div" ).hide();
		  	 $("#update_action_expression_save_btn_div" ).show();
		 	 $("#action-final-update-button" ).hide();
		  	 $("#action-final-save-button" ).show();
			 $("#action_name").val('');
    		 $("#action_description").val('');
			 $("#action_syntax").val('');
			 $("#action_invalid_exp_error_icon").hide();
			 $("#action_valid_exp_icon").hide();
			 $("#action_expression_save_next_btn").hide();
			 
			}
			  	 
			else if(actionType=="update"){

			redirectToUpdateRuleActoionOnReset(policyActionId);
		}
		
	}

	function resetActionDetails(){
		clearAllMessages();
		clearResponseMsgDiv();
		clearResponseMsg();
		clearErrorMsg();
		
		resetInputDetails();
	}
	
	function setCurrentAction(selectedAction){
		var textAreaVal=$('#action_syntax').val();
		textAreaVal+=selectedAction  + ' ';
		$('#action_syntax').val(textAreaVal);
		
	}
	
	function openSaveActionForm(){
		resetWarningDisplayPopUp();
		$("#add_action_link").click();
		clearResponseMsg();
	}

	function saveActionDetails(urlAction,actionId,pageType){
		resetWarningDisplayPopUp();
		addNewActionToDB(urlAction,actionId,pageType);
		
	}
	
	function closeActionDetails(){
		$('#action_field_section').hide();
	}
	
	function addNewActionToDB(urlAction,actionId,pageType){
		var str = $("#create-policy-rule-action").serialize();
		var action;
		var expression;
		if(document.getElementById('action_dynamic').checked){
			var queryName = document.getElementById('dynamicQueryEnum');
			action = $("#dynamic_action").val()+"="+queryName.value;
			expression = "";
		}else{
			action = $('input:radio[name=action]:checked', '#div_action').val();
			if( $('input:radio[name=action]:checked', '#div_action').val() == 'Clone' ) {
				if($("#action").val() == "" || $("#action").val().trim() ==  ""){
					$("#action").prop("value","1");
				}
				action = action + '#' + $("#action").val();
			}
			expression=$("#action_syntax").val();
		}
		$("#create-action-button-div").hide();
		showProgressBar();
		parent.resizeFancyBox();
		$.ajax({
			url: 'addPolicyRuleAction',
			cache: false,
			async: true, 
			dataType: 'json',
			type: "POST",
			data: 
			{
				"id" 				:   $("#id").val(),
				"server.id" 		:	$("#server_id").val(),
				"name" 				:	$("#action_name").val(),
				"alias" 			:	$("#action_name").val(),
				"description" 		:	$("#action_description").val(),
				"action" 			:	action,
				"actionExpression" 	:   expression,
				"type" 				:	$('input:radio[name=type]:checked', '#div_action').val(),
				"pageType"			:   pageType,
				"policyActionId"    :   policyActionId
				
			},
			success: function(data){
				hideProgressBar();

				var response = eval(data);
				var responseCode = response.code; 
				var responseMsg = response.msg; 
				var responseObject = response.object;
				
				if(responseCode == "200"){
					
					if(currentPage != 'updatePage')
						resetInputDetails();
						closeFancyBox();
						try{resetSearchParams();}catch(err){}
						showSuccessMsg(responseMsg);
				}else if(responseObject != undefined && responseObject != 'undefined' && responseCode == "400"){
					 $.each(responseObject, function(key,val){						 
						if(key == 'name' ){
							$("#action_name").next().children().first().attr("data-original-title",val).attr("id",key+"_error");
						}else if(key == 'description'){
							$("#action_description").next().children().first().attr("data-original-title",val).attr( "id",key+"_error");
						}else if(key == 'actionExpression'){
							$("#action_syntax").next().children().first().attr("data-original-title",val).attr( "id",key+"_error");
						}else if(key == 'value'){
							$("#dynamicQueryEnum").next().children().first().attr("data-original-title",val).attr("id",key+"_error");
						}else if(key == 'unifiedField'){
							$("#dynamic_action").next().children().first().attr("data-original-title",val).attr("id",key+"_error");
						}else{
							$("#" + key).next().children().first().attr("data-original-title",val);
						}
						addErrorClassWhenTitleExistPopUp($("#GENERIC_ERROR_MESSAGE", window.parent.document).val());
					}); 

				}else{
					resetWarningDisplayPopUp();
					showErrorMsgPopUp(responseMsg);
					reloadGrid();
					
				}
				$("#create-action-button-div").show();
			},
		    error: function (xhr,st,err){
		    	hideProgressBar();
		    	$("#create-action-button-div").show();
				handleGenericError(xhr,st,err);
			}
		});
	}
		
			</script>
</form:form>
