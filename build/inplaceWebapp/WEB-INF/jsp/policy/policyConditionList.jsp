<%@page import="com.elitecore.sm.util.MapCache"%>
<%@page import="com.elitecore.sm.common.constants.SystemParametersConstant"%>
<%@page import="org.apache.catalina.connector.Request"%>
<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="/WEB-INF/ShowErrorTag.tld" prefix="elitecoreError"%>
<%@page import="com.elitecore.sm.common.constants.BaseConstants"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
 <script src="${pageContext.request.contextPath}/js/bootstrap.min.js"
	type="text/javascript">
</script> 
<script>
		var ckIntanceSelected = new Array();
   		var jsLocaleMsg2 = {};
   		var conditionType = "create";
   		jsLocaleMsg2.policyConditionId = "<spring:message code='policy.condition.grid.column.id'></spring:message>";
   		jsLocaleMsg2.policyConditionName = "<spring:message code='policy.condition.grid.column.name'></spring:message>";
   		jsLocaleMsg2.failUpdateAtributeMsg = "<spring:message code='update.mapping.fail'></spring:message>";
</script>

<div class="fullwidth mtop10">

	<div class="fullwidth borbot">
		<div class="col-md-6 inline-form" style="padding-left: 0px !important;">

			<div class="form-group">
				<spring:message code="business.policy.rule.condition.grid.column.name"
					var="label" ></spring:message>
				<spring:message code="business.policy.rule.condition.grid.column.name.tooltip"
					var="tooltip" ></spring:message>
				<div class="table-cell-label">${label}</div>
				<div class="input-group ">
					<input type="text" id="policyConditionName" name="id" maxlength="100"
						class="form-control table-cell input-sm" data-toggle="tooltip"
						data-placement="bottom" title="${tooltip }"> <span
						class="input-group-addon add-on last"> <i
						class="glyphicon glyphicon-alert" data-toggle="tooltip"
						data-placement="left" title=""></i></span>
				</div>
			</div>

			<div class="form-group">
				<spring:message code="business.policy.rule.condition.grid.column.type"
					var="label" ></spring:message>
				<spring:message code="business.policy.rule.condition.grid.column.type.tooltip"
					var="tooltip" ></spring:message>
				<div class="table-cell-label">${label }</div>
				<div class="input-group " style="display: table;">
					<select name="policyConditionType"
						class="form-control table-cell input-sm" tabindex="3"
						id="policyConditionType" data-toggle="tooltip"
						data-placement="bottom" title="${tooltip }">
						<option value="-1" selected="selected">SELECT CONDITION
							TYPE</option>
						<option value="expression" >expression</option>
						<option value="dynamic">dynamic</option>
						<%-- <c:if
							test="${SERVICE_TYPE_LIST !=null && fn:length(SERVICE_TYPE_LIST) gt 0}">
							<c:forEach items="${SERVICE_TYPE_LIST}" var="serviceType">
								<option value="${serviceType.alias}">${serviceType.type}</option>
							</c:forEach>
						</c:if> --%>
					</select> <span class="input-group-addon add-on last"> <i
						class="glyphicon glyphicon-alert" data-toggle="tooltip"
						data-placement="left" title=""></i></span>
				</div>
			</div>
		</div>
		<div class="col-md-6 inline-form" style="padding-left: 0px !important;">

			<div class="form-group">
				<spring:message
					code="business.policy.rule.condition.grid.column.description" var="label" ></spring:message>
				<spring:message
					code="business.policy.rule.condition.grid.column.description.tooltip"
					var="tooltip" ></spring:message>

				<div class="table-cell-label">${label }</div>
				<div class="input-group ">
					<input type="text" id="policyConditionDesc" name="policyConditionDesc"
						maxlength="100" class="form-control table-cell input-sm"
						data-toggle="tooltip" data-placement="bottom" title="${tooltip }">
					<span class="input-group-addon add-on last"> <i
						class="glyphicon glyphicon-alert" data-toggle="tooltip"
						data-placement="left" title=""></i></span>

				</div>
			</div>

			<div class="form-group">
				<spring:message code="business.policy.rule.condition.grid.column.status"
					var="label" ></spring:message>
				<spring:message code="business.policy.rule.condition.grid.column.status.tooltip"
					var="tooltip" ></spring:message>
				<div class="table-cell-label">${label }</div>
				<div class="input-group " style="display: table;">
					<select name="policyConditionStatus"
						class="form-control table-cell input-sm" tabindex="3"
						id="policyConditionStatus" data-toggle="tooltip"
						data-placement="bottom" title="${tooltip }">
						<option value="-1" selected="selected">SELECT CONDITION
							ASSO. TYPE</option>
						<option value="No" >False</option>
						<option value="Yes" >True</option>
						<%-- <c:if
							test="${SERVICE_TYPE_LIST !=null && fn:length(SERVICE_TYPE_LIST) gt 0}">
							<c:forEach items="${SERVICE_TYPE_LIST}" var="serviceType">
								<option value="${serviceType.alias}">${serviceType.type}</option>
							</c:forEach>
						</c:if> --%>
					</select> <span class="input-group-addon add-on last"> <i
						class="glyphicon glyphicon-alert" data-toggle="tooltip"
						data-placement="left" title=""></i></span>
				</div>
			</div>

			<div class="form-group">
				<div class="table-cell-label">&nbsp;</div>
				<div class="input-group ">&nbsp;&nbsp;&nbsp;</div>
			</div>
		</div>
		<div class="clearfix"></div>
		<div class="pbottom15 form-group ">
			<sec:authorize access="hasAuthority('SEARCH_SERVICE_INSTANCE')">
				<button id="search_btn" class="btn btn-grey btn-xs" onclick="reloadConditionGridDataWithClearMsg();">
					<spring:message code="btn.label.search" ></spring:message>
				</button>
				<button id="reset_btn" class="btn btn-grey btn-xs" onclick="resetConditionSearchParams();">
					<spring:message code="btn.label.reset" ></spring:message>
				</button>
			</sec:authorize>
		</div>

	</div>

	<!-- Jquery Policy Rule Grid start here -->
	<div class="fullwidth">
		<div class="title2">
			<spring:message code="business.policy.rule.condition.tab.heading" ></spring:message>
			<span class="title2rightfield"> <span
				class="title2rightfield-icon1-text"> <sec:authorize
						access="hasAuthority('CREATE_SERVICE_INSTANCE')">
						<a href="#" onclick="redirectToUpdateRuleCondition('create','0');"><i
							class="fa fa-plus-circle"></i></a>

						<a href="#" id="createRuleCondition" onclick="redirectToUpdateRuleCondition('create','0');">
							<spring:message code="business.policy.rule.condition.create.label" ></spring:message>
						</a>

					</sec:authorize>
			</span>
			</span>
		</div>
	</div>

	<div class="box-body table-responsive no-padding box">
		<table class="table table-hover" id="ruleConditiongrid"></table>
		<div id="ruleConditiongridPagingDiv"></div>
	</div>
	<!-- Jquery Policy Rule Grid end here -->


</div>
<div style="display: block;">
	<form id="policy_rule_condition_form" method="POST">
		<input type="hidden" id="server-instanceName"
			name="server-instanceName" value='${instanceName}' /> 
		<input
			type="hidden" id="server-instanceHost" name="server-instanceHost"
			value='${host}' /> 
		<input type="hidden" id="server-instancePort"
			name="server-instancePort" value='${port}' /> 
		<input type="hidden"
			id="server-instanceId" name="server-instanceId" value='${instanceId}' />
		<input type="hidden" id="serviceId" name="serviceId"
			value="${serviceId}">
			<input type="hidden" id="serviceInstanceId" name="serviceInstanceId" value="${serviceInstanceId}">
		<input type="hidden" id="serviceType"
			name="serviceType" value="<%=BaseConstants.PROCESSING_SERVICE%>">
		<input type="hidden" id="serviceName" name="serviceName"
			value="${serviceName}">
		<input type="hidden" id="conditionType" name="conditionType" />
		<input type="hidden" id="policyConditionId" name="policyConditionId" />
		
	</form>
</div>
<a href="#divDeletePolicyCondition" class="fancybox" style="display: none;" id="deletePolicyConditionPopup">#</a>
		   	<div id="divDeletePolicyCondition" style=" width:100%; display: none;" >
			   <jsp:include page="deletePolicyConditionPopUp.jsp"></jsp:include> 
			</div>
<a href="#createPolicyConditionDiv" class="fancybox" style="display: none;" id="createPolicyConditionPopup">#</a>
		   	<div id="createPolicyConditionDiv" style=" width:100%; display: none;" >
			   <jsp:include page="ruleConditionManager.jsp"></jsp:include> 
			</div>
			
<script src="${pageContext.request.contextPath}/customJS/policyManager.js<%= "?v=" + Math.random() %>" type="text/javascript"></script>

<script type="text/javascript">

	$(document).ready(function() {
		loadJQueryPolicyruleConditiongrid();
	});

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

	function clearConditionInstanceGrid() {
		var $grid = $("#ruleConditiongrid");
		var rowIds = $grid.jqGrid('getDataIDs');
		// iterate through the rows and delete each of them
		for (var i = 0, len = rowIds.length; i < len; i++) {
			var currRow = rowIds[i];
			$grid.jqGrid('delRowData', currRow);
		}
	}

	function reloadConditionGridDataWithClearMsg() {
		clearAllMessages();
		clearResponseMsgDiv();
		clearResponseMsg();
		clearErrorMsg();

		reloadConditionGridData();
	}
	
	function reloadConditionGridData(){
		var currentPage = $(".ui-pg-input").val();
		if(!currentPage) {
			currentPage = 1;
		}
		var $grid = $("#ruleConditiongrid");
		jQuery('#ruleConditiongrid').jqGrid('clearGridData');
		clearConditionInstanceGrid();
		$grid.jqGrid('setGridParam',{datatype:'json',page: currentPage,sortname: 'id',sortorder: "desc", postData:{
				'policyConditionName': function () {
				var id  =  $("#policyConditionName").val();
		        return id;
	    		},
	    		'policyConditionType': function () {
		        return $("#policyConditionType").val();
	    		},
	    		'policyConditionDesc': function(){
	    			return $("#policyConditionDesc").val();
	    		},
	    		'policyConditionAssoStatus': function () {
		        return $("#policyConditionStatus").val();
	    		},
	    		'serverInstanceId': function () {
	    	    return ${instanceId};
	        	}
	    		
	    		
		}}).trigger('reloadGrid');
	} 
	var currentPolicyConditionPage = '${currentPage}';
	var oldGrid = '';
function loadJQueryPolicyruleConditiongrid(){
	$("#ruleConditiongrid").jqGrid({
		url: "<%= ControllerConstants.GET_POLICY_RULE_CONDITION_LIST%>",
		postData:
		{
    			'policyConditionName': function () {
    			var id  =  $("#policyConditionName").val();
    	        return id;
	    		},
	    		'policyConditionType': function () {
    	        return $("#policyConditionType").val();
	    		},
	    		'policyConditionDesc': function(){
	    			return $("#policyConditionDesc").val();
	    		},
	    		'policyConditionAssoStatus': function () {
    	        return $("#policyConditionStatus").val();
	    		},
	    		'serverInstanceId': function () {
	        	return ${instanceId};
	            }
		},
		datatype: "json",
		colNames: [
					  "#",
					  "<spring:message code='business.policy.rule.condition.grid.column.name' ></spring:message>",
					  "#",
		           	  "<spring:message code='business.policy.rule.grid.column.description' ></spring:message>",
		              "<spring:message code='business.policy.rule.condition.grid.column.type' ></spring:message>",
		              "<spring:message code='business.policy.rule.condition.grid.column.syntaxt' ></spring:message>",
		              "<spring:message code='business.policy.rule.condition.grid.column.syntaxt' ></spring:message>",
		              "<spring:message code='business.policy.rule.grid.column.delete' ></spring:message>",
		              "value",
		              "unifiedField",
		              "condition"
			],
		colModel: [
			{name:'id',index:'id',align:'center',sortable:true,hidden: true},
			{name:'name',index:'name',sortable:true,formatter: ruleConditionNameFormatter},
			{name:'actual_name',index:'actual_name',hidden:true,formatter: ruleConditionActualNameFormatter},
			{name:'description',index:'description',sortable:true},
			{name:'type',index:'type',sortable:true},
			{name:'expression',index:'expression',sortable:false,hidden: true},
			{name:'conditionExpression',index:'conditionExpression',sortable:true,formatter: ruleConditionColumnFormatter},
			{name:'delete',index:'delete',sortable:false, align:'center',formatter: ruleConditionDeleteColumnFormatter},
			{name:'value',index:'value',sortable:false, align:'center',hidden: true},
			{name:'unifiedField',index:'unifiedField',sortable:false, align:'center',hidden: true},
			{name:'condition',index:'condition',sortable:false, align:'center',hidden: true}
		],
		rowNum:<%= MapCache.getConfigValueAsInteger(SystemParametersConstant.TOTAL_ROWS_TO_DISPLAY_IN_GRID, 10) %>,
		rowList:[10,20,60,100],
		height: 'auto',
		sortname: 'id',
		sortorder: "desc",
		pager: "#ruleConditiongridPagingDiv",
		viewrecords: true,
		multiselect: false,
		timeout : 120000,
	    loadtext: "Loading...",
		caption: "<spring:message code="collectionService.summary.agent.grid.caption"></spring:message>",
		beforeRequest:function(){
			if(oldGrid != ''){
        		$('#ruleConditiongrid tbody').html(oldGrid);
        	} else if(currentPolicyConditionPage && currentPolicyConditionPage > 1) {
				$('#ruleConditiongrid').jqGrid('setGridParam', { postData: { page: currentPolicyConditionPage} });
				currentPolicyConditionPage = 0;
			}
	 		$('#divLoading').dialog({
	             autoOpen: false,
	             width: 90,
	             modal:true,
	             overlay: { opacity: 0.3, background: "white" },
	             resizable: false,
	             height: 125
	         });
	    },
		loadComplete: function(data) {
	 			$(".ui-dialog-titlebar").show();
	 			if ($('#ruleConditiongrid').getGridParam('records') === 0) {
	 				oldGrid = $('#ruleConditiongrid tbody').html();
	             $('#ruleConditiongrid tbody').html("<div style='padding:6px;background:#D8D8D8;text-align:center;'><spring:message code='serverManagement.grid.empty.records'></spring:message></div>");
	             $("#ruleConditiongridPagingDiv").hide();
	        	}else{
	            	$("#ruleConditiongridPagingDiv").show();
	            	ckIntanceSelected = new Array();
	            }
			
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
		}).navGrid("#ruleConditiongridPagingDiv",{edit:false,add:false,del:false,search:false});
			$(".ui-jqgrid-titlebar").hide();
			$(".ui-pg-input").height("10px");
}


function ruleConditionDeleteColumnFormatter(cellvalue, options, rowObject) {
	if(cellvalue == 'Associated'){
		return '<p>' + cellvalue + '</p>';
	}else{ 
		<sec:authorize access="hasAnyAuthority('DELETE_POLICY_ACTION_CONFIGURATION')">  
			return '<a href="#" id="'+rowObject["name"]+'_delete_lnk" onclick="deletePolicyConditionPopup('+"'"+rowObject["id"]+"'"+','+"'"+rowObject["name"]+"'"+')"><i class="fa fa-trash"></i></a>';	
		</sec:authorize>
		<sec:authorize access="!hasAnyAuthority('DELETE_POLICY_ACTION_CONFIGURATION')">  
			return '<a href="#" id="'+rowObject["name"]+'_delete_lnk"><i class="fa fa-trash"></i></a>';	
		</sec:authorize>
	} 
}

function deletePolicyConditionPopup(id,name){
	clearAllMessages();
	$("#divDeletePolicyConditionMsg").html('');
	$("#btnDeletePolicyConditionCancel").show();
	$("#btndeletePolicyConditionPopup").show();
		
		$("#tblDeletePolicyConditionList").html('');
		parent.ckIntanceSelected[0]=id;
		tableString ='<table class="table table-hover" style="width:100%">';
			tableString += "<tr>";
			tableString += "<th>"+jsLocaleMsg2.policyConditionId+"</th>";
			tableString += "<th>"+jsLocaleMsg2.policyConditionName +"</th>";
			tableString += "</tr>";
			tableString += "<tr>";
			tableString += "<td>"+id+"</td>";
			tableString += "<td>"+name+"</td>";
			tableString += "</tr>";
			tableString += "</table>"
		$("#tblDeletePolicyConditionList").html(tableString);
		$('#deletePolicyConditionPopup').click();
}

function DeletePolicyConditionInstance(){
	clearAllMessages();
	$("#delete-progress-bar-div").show();
	$("#delete-buttons-div").hide();

	$.ajax({
				url: "deletePolicyRuleCondition",
			    cache: false,
				async: true,
				dataType: 'json',
				type: "POST",
				data: {
					'conditionId': parent.ckIntanceSelected[0],
				},
				success: function(data){
					$("#delete-buttons-div").show();
						$("#delete-progress-bar-div").hide();
						var response = eval(data);
				    	response.msg = decodeMessage(response.msg);
				    	response.msg = replaceAll("+"," ",response.msg);
				    	
				    	$("#divDeleteMsg").css('color','black');
				    	$("#divDeleteMsg").css('font-weight','bold');
				    	$("#divDeleteMsg").html(response.msg);

				    	if(response.code == 200 || response.code == "200") {
				    		clearResponseMsgDiv();
				    		clearResponseMsg();
				    		clearErrorMsg();
				    		
				    		$("#divDeleteMsg").hide();
				    		closeFancyBox();
		    			    showSuccessMsg(response.msg);
		    			    reloadConditionGridData();
			    			$("#btnDeletePolicyConditionCancel").hide();
		    			
				    	}else{
				    		$("#divDeleteMsg").css('color','red');
					    	$("#divDeleteMsg").css('font-weight','normal');
					    	$("#divDeleteMsg").html(response.msg);
				    		showErrorMsgPopUp(response.msg);
				    	}
				    	
					},
				    error: function (xhr,st,err){
						$("#delete-buttons-div").show();
						$("#delete-progress-bar-div").hide();
				    	handleGenericError(xhr,st,err);
    					$("#btnDeletePolicyConditionCancel").show();
					}
			
			}); 
	
}

</script>
