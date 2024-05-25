<%@page import="com.elitecore.sm.util.MapCache"%>
<%@page import="com.elitecore.sm.common.constants.SystemParametersConstant"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<%@page import="com.elitecore.sm.common.constants.BaseConstants"%>

<!DOCTYPE html>
<html>

<% 
String action = "create";
String type = "";
String policyActionId = "";
if(request.getAttribute("actionType") != null){
 	action = (String) request.getAttribute("actionType").toString();
}
if(request.getAttribute("types") != null){
 	type   = (String) request.getAttribute("types").toString();
}
if(request.getAttribute("policyActionId") != null){
	policyActionId   = (String) request.getAttribute("policyActionId").toString();
}
%>
<script type="text/javascript"> 
var types = "<%=type%>";
<%-- var actionType = "<%=action%>"; --%>
var policyActionId = "<%=policyActionId%>";
var currentPage = "actionPage";
</script>
<body class="skin-blue sidebar-mini sidebar-collapse" onload="getAllData('')">
<jsp:include page="../common/newheader.jsp"></jsp:include>
<!-- Main div start here -->
	<div>
		<!-- Content wrapper div start here -->
	 <div class="modal-content">
		<div class="modal-header padding10">

			<h4 class="modal-title">
				<span id="add_label" >
				<spring:message code="business.policy.rule.action.create.label" ></spring:message></span>
				<span id="update_label" style="display: none;"><spring:message
				code="business.policy.rule.action.update.label" ></spring:message></span>
			</h4>
		</div>
								
			<div class="modal-body padding10 inline-form" style="overflow: auto;height: 450px;">
									
		 	<!-- Action code start here -->
			<jsp:include page="createAction.jsp"></jsp:include>	
								 
		 	<!-- Action code end here -->
		   	<!-- Bottom Expression info box start here -->
		   	 <jsp:include page="commonActionExpressionInfo.jsp"></jsp:include>
		   	<!-- Bottom Expression Info box end here -->
		
			<!-- Unified right box start here -->
			<jsp:include page="unifiedRightBox.jsp"></jsp:include>	
			<!-- Unified right box end here -->
			
		  	</div>
		</div>
	</div>
		<div style="display: block;">
	<form id="policy_rule_action_reset_form" method="POST">
		<input type="hidden" id="server-instanceName"
			name="server-instanceName" value='${instance}' /> 
		<input
			type="hidden" id="server-instanceHost" name="server-instanceHost"
			value='${host}' /> 
		<input type="hidden" id="server-instancePort"
			name="server-instancePort" value='${port}' /> 
		<input type="hidden"
			id="server-instanceId" name="server-instanceId" value='${instanceId}' />
		<input type="hidden" id="serviceId" name="serviceId"
			value="${serviceId}">
		<input type="hidden" id="serviceType"
			name="serviceType" value="<%=BaseConstants.PROCESSING_SERVICE%>">
		<input type="hidden" id="serviceName" name="serviceName"
			value="${serviceName}">
			<input type="hidden" id="serviceInstanceId" name="serviceInstanceId" value="${serviceInstanceId}">
		<input type="hidden" id="actionType" name="actionType" />
		<input type="hidden" id="policyActionId" name="policyActionId" />
		
	</form>
	<form id="server_instance_policy_action_form" method="POST" >
				<input type="hidden" id="server-instance-name" name="server-instance-name" value='${instanceName}'/>
				<input type="hidden" id="server-instance-host" name="server-instance-host" value='${host}'/>
				<input type="hidden" id="server-instance-port" name="server-instance-port" value='${port}'/>
				<input type="hidden" id="server-instance-id" name="server-instance-id" value='${instanceId}'/>
				<input type="hidden" id="serviceInstanceId" name="serviceInstanceId" value="${serviceInstanceId}">
			<input type="hidden" id="serviceId" name="serviceId" value="${serviceId}">
			<input type="hidden" id="serviceType" name="serviceType" value="<%=BaseConstants.PROCESSING_SERVICE%>">
			<input type="hidden" id="serviceName" name="serviceName" value="${serviceName}">
				<input type="hidden" name="<%= BaseConstants.REQUEST_ACTION_TYPE %>"  id="<%= BaseConstants.POLICY_RULE_ACTION %>" value="<%= BaseConstants.POLICY_RULE_ACTION%>" />
			</form>
</div>

<script src="${pageContext.request.contextPath}/customJS/policyManager.js<%= "?v=" + Math.random() %>" type="text/javascript"></script>

	<script type="text/javascript">
	 
	$(document).ready(function() {
		loadActionInitParams();
	});
	
	function loadActionInitParams(){
		if(actionType == 'create'){
			resetActionDetails();
			 currentPage="actionPage";
			$("#action_new").prop("checked", true);
			$("#action_static_operation_div").show();
			$("#action_type_div").show();
			$("#action_static").prop("checked", true);
			$("#existing_action_list_main_div").hide();
			$("#action_expression_syntax_div").hide(); 
			$("#update_action_expression_update_btn_div" ).hide();
		  	$("#update_action_expression_save_btn_div" ).show();
		 	$("#action-final-update-button" ).hide();
		  	$("#action-final-save-button" ).show();
		  	document.getElementById("action_name").disabled = false;
		  	 
		}else if(actionType == 'update'){
			 currentPage="updatePage";
			 document.getElementById("action_name").disabled = false; 
			 $("#action-final-update-button" ).show();
			 $("#action-final-save-button" ).hide();
			 $("#update_action_expression_update_btn_div" ).show();
		  	 $("#update_action_expression_save_btn_div" ).hide();
			 
		  	 if( types == 'dynamic'){
		  		 $("#action_dynamic_div").show();
		  	 }
		}
	}
	
	 function verifyActionExpression(){
		 
		 var expressionVal =  $("#action_syntax").val();
			if(expressionVal == 'valid'){
				$("#action_invalid_exp_error_icon").hide();
				$("#action_valid_exp_icon").show();
				$('#action_expression_next_btn').prop('disabled', false);
				$('#action_expression_save_next_btn').prop('disabled', false);
				 
				if(currentPage == "actionPage"){
					$("#action_expression_verify_btn_div").hide();
					$("#action_expression_save_btn_div").show();
				}
				
				if(currentPage == "updatePage"){
					$("#action_expression_verify_btn_div").hide();
					$("#update_action_expression_save_btn_div").show();
					$("#update_action_cancel_btn").hide();
					
				}
			}else{
				$("#action_valid_exp_icon").hide();
				$("#action_invalid_exp_error_icon").show();
				$('#action_invalid_msg_tooltip').attr('data-original-title', 'Invalid Expression');
				
			}
	 }
	 
	 function removeActionExpressionInfoBlock(){
		 $( "#action_expression_info_box" ).hide();
	 }
	 function showActionExpressionBox(){
		 $("#action_expression_info_box" ).show();
	 }
	 
	 $('#div_action input').on('change', function() {
		 if(currentPage == "actionPage" || currentPage == "updatePage" ){
			 			 
			var selectedActionVal = $('input:radio[name=type]:checked', '#div_action').val();
			var actionType = $('input:radio[name=actionType]:checked', '#div_action').val();
			
			if(selectedActionVal == 'static'){
			 
			  $("#unified_field_mapping_div").hide();   // it will hide unified field mapping radio
			  $("#action_expression_info_box" ).hide();        // It will hide expression box for statis radio
			  $("#action_dynamic_div").hide();
			  $("#action_static_operation_div").show();    // It will show operation radio box for static Action Type	 
			  $("#action_expression_syntax_div" ).hide();  // It will hide expression textaread box action type 
			  $("#action_expression_save_btn_div" ).hide();    // hide back save button of expression action type
			  $("#update_action_expression_save_btn_div" ).show();
			  
			}else if(selectedActionVal == 'expression'){
			
			  $("#unified_field_mapping_div").hide();   // it will hide unified field mapping radio
			  $("#action_expression_info_box" ).hide();        // It will hide expression box for statis radio
			  $("#action_static_operation_div").hide();    // It will hide operation radio box for static Action Type	 
			  $("#action_dynamic_div" ).hide();            // It will hide syntax box for static action type.
			  $("#action_expression_syntax_div" ).show();  // It will show expression textaread box action type 
			  $('#policy_rule_field_section').hide();	 // Policy save rule form will be hide
			  $("#action_expression_save_btn_div").show();
			  $("#update_action_expression_save_btn_div" ).show();
			  $("#action_invalid_exp_error_icon").hide();
			  $("#action_valid_exp_icon").hide();
			  $("#action_expression_save_next_btn").hide();
			  $("#action_static_next_btn_div" ).hide();  // It will hide next static button
			  $("#create_rule_main_div").removeClass( "col-md-12" ).addClass( "col-md-12" );  // decrease width of main for for static action type.
		
			}else if(selectedActionVal == 'dynamic'){
			
			   $("#unified_field_mapping_div").hide();  
			  $("#action_expression_info_box" ).hide();        // It will hide expression box for statis radio			
			  $("#action_static_operation_div").hide();   
			  $("#action_expression_syntax_div" ).hide();// It will hide operation radio box for static Action Type	 
			  $('#policy_rule_field_section').hide();	 // Policy save rule form will be hide
			  $("#action_dynamic_div" ).show();
			  $("#create_rule_main_div").removeClass( "col-md-12" ).addClass( "col-md-12" );  // increase width of main for for static action type.
			}

			 if( currentPage=="updatePage"){
				  
				  	$("#action-final-update-button" ).show();
				  	$("#action-final-save-button" ).hide();
			  		$("#update_action_expression_update_btn_div" ).show();
			  		$("#update_action_expression_save_btn_div" ).hide();

			  }else{

				  	$("#action-final-update-button" ).hide();	
				  	$("#action-final-save-button" ).show();
			  		$("#update_action_expression_update_btn_div" ).hide();
			  		$("#update_action_expression_save_btn_div" ).show();
			  }
			 
			if(actionType == "newaction"){
				 $("#action_existing_save_btn_div" ).hide();
				 $("#existing_action_list" ).hide();
				 $("#existing_action_list_main_div").hide();
				 $("#action_type_div" ).show();
				 $("#action_new").prop("checked", true);
				 
				 //$("#action_static_next_btn_div" ).show();  // It will hide next static button
			}else if(actionType == "existingaction"){
				    reloadCreateActionGridData();
					loadJQueryPolicyRuleActionExistingListGrid();
     			    $("#action_expression").prop("checked", true);
					$("#existing_action_list_main_div").show();
					$("#action_dynamic_div").hide();
					$("#policy_rule_field_section").hide(); // It will show default save rule form details
					$("#action_static_operation_div").hide();    // It will hide operation radio box for static Action Type	 
					$("#action_expression_syntax_div" ).show();  // It will show expression textaread box action type  
					$("#existing_action_list" ).show();
					$("#action_expression_verify_btn_div" ).hide();
					$("#action_type_div" ).hide();
					$("#action_static_next_btn_div" ).hide();  // It will hide next static button
					$("#note_expression_validate").show();
				
			}
		} 
		 
	 });
	 
	 function clearInstanceGrid(){
			var $grid = $("#existing_action_list");
			var rowIds = $grid.jqGrid('getDataIDs');
			// iterate through the rows and delete each of them
			for(var i=0,len=rowIds.length;i<len;i++){
				var currRow = rowIds[i];
				$grid.jqGrid('delRowData', currRow);
			}
		}
	 
	 function reloadCreateActionGridData(){
			var $grid = $("#existing_action_list");
			jQuery('#existing_action_list').jqGrid('clearGridData');
			$grid.jqGrid('setGridParam',{datatype:'json',page: 1,sortname: 'id',sortorder: "desc", 
				postData:
				{
			    		'policyActionType': function () {
		    	        return 'expression';
			    		},
			    		'serverInstanceId': function () {
				         return ${instanceId};
				         }
				}}).trigger('reloadGrid');
		} 
	 
	 function loadJQueryPolicyRuleActionExistingListGrid(){
		 
			$("#existing_action_list").jqGrid({
				url: "<%= ControllerConstants.GET_POLICY_RULE_ACTION_LIST%>",
				postData:
				{
			    		'policyActionType': function () {
		    	        return 'expression';
			    		},
			    		'serverInstanceId': function () {
				         return ${instanceId};
				         }
				},
				datatype: "json",
				colNames: [
							  "#",
							  "<spring:message code='business.policy.rule.action.grid.column.name' ></spring:message>",
				              "<spring:message code='business.policy.rule.action.grid.column.syntaxt' ></spring:message>",
				              "<spring:message code='business.policy.rule.action.grid.column.syntaxt' ></spring:message>"
					],
				colModel: [
					{name:'id',index:'id',align:'center',sortable:true,hidden:true},
					{name:'name',index:'name',sortable:true},
					{name:'expression',index:'expression',sortable:false,hidden: true},
					{name:'action',index:'action',sortable:true, formatter: ruleAcionActionColumnFormatter},
				],
				rowNum:<%= MapCache.getConfigValueAsInteger(SystemParametersConstant.TOTAL_ROWS_TO_DISPLAY_IN_GRID, 10) %>,
				rowList:[10,20,60,100],
				height: 'auto',
				sortname: 'id',
				sortorder: "desc",
				pager: "#existing_action_list_div",
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
				}).navGrid("#existing_action_list_div",{edit:false,add:false,del:false,search:false});
					$(".ui-jqgrid-titlebar").hide();
					$(".ui-pg-input").height("10px");
		}
	 
		function ruleAcionActionColumnFormatter(cellvalue, options, rowObject) {
			var dataVAl = rowObject["expression"];
			return '<a href="#" id="'+rowObject["name"]+'_lnk" onclick="setCurrentAction('+"&quot;"+dataVAl+"&quot;"+');">' 
			+ rowObject["expression"] + '</a>';
		}

		function redirectPolicyMgmt(){
			
			$("#server_instance_policy_action_form").attr("action","<%=ControllerConstants.INIT_POLICY_RULE_LIST_ACTION_MANAGER%>");
			$("#server_instance_policy_action_form").submit();
		}
		
		function redirectToUpdateRuleActoionOnReset(id){
			$("#policyActionId").val(id);
			setResetActionDetails(policyActionId);
		}
	
	</script>
	 <footer class="main-footer positfix">
	     	<jsp:include page="../common/newfooter.jsp"></jsp:include>
	</footer>	 
</body>
</html>
