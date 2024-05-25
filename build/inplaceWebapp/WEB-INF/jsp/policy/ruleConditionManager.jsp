<%@page import="com.elitecore.sm.util.MapCache"%>
<%@page
	import="com.elitecore.sm.common.constants.SystemParametersConstant"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/security/tags"
	prefix="sec"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<%@page import="com.elitecore.sm.common.constants.BaseConstants"%>

<!DOCTYPE html>
<html>

<%
	String condition = "create";
	String type = "";
	String policyConditionId = "";
	if (request.getAttribute("conditionType") != null) {
		condition = (String) request.getAttribute("conditionType")
				.toString();
	}
	if (request.getAttribute("types") != null) {
		type = (String) request.getAttribute("types").toString();
	}
	if (request.getAttribute("policyConditionId") != null) {
		policyConditionId = (String) request.getAttribute(
				"policyConditionId").toString();
	}
%>
<script type="text/javascript"> 
var types = "<%=type%>";
<%-- var conditionType = "<%=condition%>"; --%>
var policyConditionId = "<%=policyConditionId%>";
var currentPage = "conditionPage";
</script>
<body class="skin-blue sidebar-mini sidebar-collapse">
<jsp:include page="../common/newheader.jsp"></jsp:include>

	<!-- Main div start here -->
	<div>
		<!-- Content wrapper div start here -->
		<div class="modal-content">
			<div class="modal-header padding10">
						
						<h4 class="modal-title">
							<span id="add_label" style="display: none;"><spring:message
									code="business.policy.rule.condition.create.label" ></spring:message></span> 
							<span id="update_label" style="display: none;"><spring:message
									code="business.policy.rule.condition.update.label" ></spring:message></span>
						</h4>
			</div>

			<div class="modal-body padding10 inline-form"
				style="overflow: auto; height: 450px;">
					<div id="AddPopUpMsg">
						<jsp:include page="../common/responseMsgPopUp.jsp" ></jsp:include>
					</div>
				<!-- Condition code start here -->
				<jsp:include page="createCondition.jsp"></jsp:include>

				<!-- Condition code end here -->
				<!-- Bottom Expression info box start here -->
				<jsp:include page="commonConditionExpressionInfo.jsp"></jsp:include>
				<!-- Bottom Expression Info box end here -->

			</div>

				<!-- Unified right box start here -->
				<jsp:include page="unifiedRightBox.jsp"></jsp:include>
				<!-- Unified right box end here -->
		</div>
	</div>

	<form id="server_instance_policy_condition_form" method="POST">
		<input type="hidden" id="server-instance-name"
			name="server-instance-name" value='${instance}' /> <input
			type="hidden" id="server-instance-host" name="server-instance-host"
			value='${host}' /> <input type="hidden" id="server-instance-port"
			name="server-instance-port" value='${port}' /> <input type="hidden"
			id="server-instance-id" name="server-instance-id"
			value='${instanceId}' /> <input type="hidden"
			id="REQUEST_ACTION_TYPE" name="REQUEST_ACTION_TYPE"
			value='POLICY_RULE_CONDITION' />
	</form>

	<div style="display: block;">
		<form id="policy_rule_condition_reset_form" method="POST">
			<input type="hidden" id="server-instanceName"
				name="server-instanceName" value='${instance}' /> 
				<input
				type="hidden" id="server-instanceHost" name="server-instanceHost"
				value='${host}' /> 
				<input type="hidden" id="server-instancePort"
				name="server-instancePort" value='${port}' /> 
				<input type="hidden"
				id="server-instanceId" name="server-instanceId"
				value='${instanceId}' /> 
				<input type="hidden" id="serviceInstanceId" name="serviceInstanceId" value="${serviceInstanceId}">
			<input type="hidden" id="serviceId" name="serviceId" value="${serviceId}">
			<input type="hidden" id="serviceType" name="serviceType" value="<%=BaseConstants.PROCESSING_SERVICE%>">
			<input type="hidden" id="serviceName" name="serviceName" value="${serviceName}">

				<input type="hidden"
				id="conditionType" name="conditionType" /> 
				<input type="hidden"
				id="policyConditionId" name="policyConditionId" />

		</form>
	</div>
	
<script src="${pageContext.request.contextPath}/customJS/policyManager.js<%= "?v=" + Math.random() %>" type="text/javascript"></script>

	<script type="text/javascript">
	 
	$(document).ready(function() {
		loadConditionInitParams("create");
		$('input:radio[name=type]').change(function () {
        	if ( $('#condition_expression').is(":checked")) {
        		$("#checkExpressionValidationBtnDiv").show();
            }else{
            	$("#checkExpressionValidationBtnDiv").hide();
            }            
        });
	});
	
 	function loadConditionInitParams(conditionType){
		if(conditionType == 'create'){
			resetConditionDetails(conditionType);
			 currentPage="conditionPage";
			 $("#condition_new").prop("checked", true);
			 $("#existing_condition_list_main_div").hide();
			 $("#condition_type_div" ).show();
			 $("#update_condition_expression_update_btn_div" ).hide();
		  	 $("#update_condition_expression_save_btn_div" ).show();
		 	 $("#condition-final-update-button" ).hide();
		  	 $("#condition-final-save-button" ).show();
		  	 $("#condition_expression_syntax_div" ).show(); 
		  	 
		}else if(conditionType == 'update'){
			 currentPage="updatePage";
			 document.getElementById("condition_name").disabled = false; 
				$("#condition-final-update-button" ).show();
			  	$("#condition-final-save-button" ).hide();
			 $("#update_condition_expression_update_btn_div" ).show();
		  	 $("#update_condition_expression_save_btn_div" ).hide();
			 if(document.getElementById("condition_dynamic").checked){
				 $("#condition_expression_syntax_div").hide();
				 $("#checkExpressionValidationBtnDiv").hide();
			 }
			 else{
				 if( types == null || types == '' || types == 'expression'){
				  	$("#condition_static_operation_div").hide();    // It will hide operation radio box for static Condition Type	 
				  	$("#condition_expression_syntax_div" ).show();
				  	$("#checkExpressionValidationBtnDiv").show();
				 }else{
			   		 $("#condition_static_operation_div").show();    // It will show operation radio box for static Condition Type	 
				     $("#condition_expression_syntax_div" ).hide();
				 }
			 }
		}
	} 
	
	 function removeConditionExpressionInfoBlock(){
		 $( "#condition_expression_info_box" ).hide();
	 }
	 function showConditionExpressionBox(){
		 $("#condition_expression_info_box" ).show();
	 }
	
	 $('#div_condition input').on('change', function() {
		 if(currentPage == "conditionPage" || currentPage == "updatePage" ){
			 			 
			var selectedConditionVal = $('input:radio[name=type]:checked', '#div_condition').val();
			var conditionType = $('input:radio[name=conditionType]:checked', '#div_condition').val();
			
			if(selectedConditionVal == 'static'){
			 
			  $("#unified_field_mapping_div").hide();   // it will hide unified field mapping radio
			  $("#condition_expression_info_box" ).hide();        // It will hide expression box for statis radio
			 
			  $("#condition_static_operation_div").show();    // It will show operation radio box for static Condition Type	 
			  $("#condition_expression_syntax_div" ).hide();  // It will hide expression textaread box condition type 
			  $("#condition_expression_save_btn_div" ).hide();    // hide back save button of expression condition type
			  $("#update_condition_expression_save_btn_div" ).show();
			  
			}else if(selectedConditionVal == 'expression'){
			
			  $("#unified_field_mapping_div").hide();   // it will hide unified field mapping radio
			  $("#condition_expression_info_box" ).hide();        // It will hide expression box for statis radio
			  $("#condition_static_operation_div").hide();    // It will hide operation radio box for static Condition Type	 
			  $("#condition_expression_syntax_div" ).show();  // It will show expression textaread box condition type 
			  $("#condition_dynamic_div" ).hide();            // It will hide syntax box for static condition type.
			  $('#policy_rule_field_section').hide();	 // Policy save rule form will be hide
			  $("#condition_expression_save_btn_div").show();
			  $("#update_condition_expression_save_btn_div" ).show();
				
			  $("#condition_invalid_exp_error_icon").hide();
			  $("#condition_valid_exp_icon").hide();
			  $("#condition_expression_save_next_btn").hide();
			 
				 $("#condition_static_next_btn_div" ).hide();  // It will hide next static button
			  
			  $("#create_rule_condition_main_div").removeClass( "col-md-12" ).addClass( "col-md-9" );  // decrease width of main for for static condition type.
		
			}else if(selectedConditionVal == 'condition_dynamic'){
				 
			  $("#unified_field_mapping_div").show();   // it will show unified field mapping radio
			  $("#condition_expression_info_box" ).hide();        // It will hide expression box for statis radio
				
			  $("#condition_static_operation_div").hide();    // It will hide operation radio box for static Condition Type	 
			  $("#condition_expression_syntax_div" ).hide();  // It will hide expression textaread box condition type 
			  $('#save_rule_btn').prop('disabled', true); // Enable save Rule button for static condition
				
			  $('#policy_rule_field_section').hide();	 // Policy save rule form will be hide
			  $("#condition_expression_verify_btn_div" ).hide();  // hide the button of verify of expression condition type.
			  $("#condition_expression_save_btn_div" ).hide();    // hide back save button of expression condition type
			  $("#create_rule_condition_main_div").removeClass( "col-md-12" ).addClass( "col-md-9" );  // increase width of main for for static condition type.
			  
			  $("#condition_static_next_btn_div" ).hide();  // It will hide next static button
			}

			 if( currentPage=="updatePage"){
				  
				  	$("#condition-final-update-button" ).show();
				  	$("#condition-final-save-button" ).hide();
			  		$("#update_condition_expression_update_btn_div" ).show();
			  		$("#update_condition_expression_save_btn_div" ).hide();

			  }else{

				  	$("#condition-final-update-button" ).hide();	
				  	$("#condition-final-save-button" ).show();
			  		$("#update_condition_expression_update_btn_div" ).hide();
			  		$("#update_condition_expression_save_btn_div" ).show();
			  }
			
			if(conditionType == "newcondition"){
				
				 $("#condition_new").prop("checked", true);
				 $("#condition_existing_save_btn_div" ).hide();
				 $("#existing_condition_list" ).hide();
				 $("#existing_condition_list_main_div").hide();
				 $("#condition_type_div" ).show();
				 
			}else if(conditionType == "existingcondition"){
					reloadCreateConditionGridData()
					loadJQueryPolicyRuleConditionExistingListGrid();
     			    $("#condition_expression").prop("checked", true);
					$("#existing_condition_list_main_div").show();
					$("#condition_dynamic_div").hide();
					$("#policy_rule_field_section").hide(); // It will show default save rule form details
					$("#condition_static_operation_div").hide();    // It will hide operation radio box for static Condition Type	 
					$("#condition_expression_syntax_div" ).show();  // It will show expression textaread box condition type  
					$("#existing_condition_list" ).show();
					$("#condition_expression_verify_btn_div" ).hide();
					$("#condition_type_div" ).hide();
					$("#note_condition_expression_validate").show();
					 $("#condition_static_next_btn_div" ).hide();  // It will hide next static button
				
			}
		} 
		 
	 });
	 
	 function clearInstanceGrid(){
			var $grid = $("#existing_condition_list");
			var rowIds = $grid.jqGrid('getDataIDs');
			// iterate through the rows and delete each of them
			for(var i=0,len=rowIds.length;i<len;i++){
				var currRow = rowIds[i];
				$grid.jqGrid('delRowData', currRow);
			}
		}
	 
	 function reloadCreateConditionGridData(){
		 	clearAllMessages();
			var $grid = $("#existing_condition_list");
			jQuery('#existing_condition_list').jqGrid('clearGridData');
			clearInstanceGrid();
			$grid.jqGrid('setGridParam',{datatype:'json',page: 1,sortname: 'id',sortorder: "desc", 
				postData:
				{
			    		'policyConditionType': function () {
		    	        return 'expression';
			    		},
			    		'serverInstanceId': function () {
				         return ${instanceId};
				         }
				}}).trigger('reloadGrid');
		} 
	 
	 function loadJQueryPolicyRuleConditionExistingListGrid(){
			$("#existing_condition_list").jqGrid({
				url: "<%= ControllerConstants.GET_POLICY_RULE_CONDITION_LIST%>",
				postData:
				{
			    		'policyConditionType': function () {
		    	        return 'expression';
			    		},
			    		'serverInstanceId': function () {
				         return ${instanceId};
				         }
				},
				datatype: "json",
				colNames: [
							  "#",
							  "<spring:message code='business.policy.rule.condition.grid.column.name' ></spring:message>",
				              "<spring:message code='business.policy.rule.condition.grid.column.syntaxt' ></spring:message>",
				              "<spring:message code='business.policy.rule.condition.grid.column.syntaxt' ></spring:message>"
					],
				colModel: [
					{name:'id',index:'id',align:'center',sortable:true,hidden:true},
					{name:'name',index:'name',sortable:true},
					{name:'expression',index:'expression',sortable:false,hidden: true},
					{name:'condition',index:'condition',sortable:true,formatter: ruleAcionConditionColumnFormatter},
				],
				rowNum:<%= MapCache.getConfigValueAsInteger(SystemParametersConstant.TOTAL_ROWS_TO_DISPLAY_IN_GRID, 10) %>,
				rowList:[10,20,60,100],
				height: 'auto',
				sortname: 'id',
				sortorder: "desc",
				pager: "#existing_condition_list_div",
				viewrecords: true,
				multiselect: false,
				timeout : 120000,
			    loadtext: "Loading...",
				caption: "<spring:message code="collectionService.summary.agent.grid.caption"></spring:message>",
				beforeRequest:function(){
			    },
				loadComplete: function(data) {
				},
				loadError : function(xhr,st,err) {
					handleGenericError(xhr,st,err);
				},
				recordtext: "<spring:message code="serverManagement.grid.pager.total.records.text"></spring:message>",
			    emptyrecords: "<spring:message code="collectionService.summary.agent.grid.empty.records"></spring:message>",
				loadtext: "<spring:message code="collectionService.summary.agent.grid.loading.text"></spring:message>",
				pgtext : "<spring:message code="collectionService.summary.agent.grid.pager.text"></spring:message>",
				beforeSelectRow: function (rowid, e){
				}
				}).navGrid("#existing_condition_list_div",{edit:false,add:false,del:false,search:false});
					$(".ui-jqgrid-titlebar").hide();
					$(".ui-pg-input").height("10px");
		}
	 
		function ruleAcionConditionColumnFormatter(cellvalue, options, rowObject) {
			var dataVAl = rowObject["expression"];
			return '<a href="#" id="'+ rowObject["name"] +'_lnk" onclick="setCurrentCondition('+"&quot;"+dataVAl+"&quot;"+');">' 
					+ rowObject["expression"] + '</a>';
		}

		function redirectPolicyMgmt(){
			
			$("#server_instance_policy_condition_form").attr("action","<%=ControllerConstants.INIT_BUSINESS_POLICY_MGMT%>");
			$("#server_instance_policy_condition_form").submit();
		}
		function redirectToUpdateRuleConditionOnReset(id){
			$("#policyConditionId").val(id);
			setResetConditionDetails(policyConditionId);
		}
		
	</script>

	<footer class="main-footer positfix">
		<jsp:include page="../common/newfooter.jsp"></jsp:include>
	</footer>
</body>
</html>
