<%@page import="com.elitecore.sm.util.MapCache"%>
<%@page import="com.elitecore.sm.common.constants.SystemParametersConstant"%>
<%@page import="org.apache.catalina.connector.Request"%>
<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<%@ taglib uri="http://www.springframework.org/security/tags"
	prefix="sec"%>
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
   		var jsLocaleMsg1 = {};
   		var jsLocaleMsg2 = {};
   		jsLocaleMsg1.policyActionId = "<spring:message code='policy.action.grid.column.service.id'></spring:message>";
   		jsLocaleMsg1.policyActionName = "<spring:message code='policy.action.grid.column.service.name'></spring:message>";
   		var actionType = "create";
   		jsLocaleMsg2.failUpdateAtributeMsg = "<spring:message code='update.mapping.fail'></spring:message>";
</script> 

<div class="fullwidth mtop10">

	<div class="fullwidth borbot">
		<div class="col-md-6 inline-form" style="padding-left: 0px !important;">

			<div class="form-group">
				<spring:message code="business.policy.rule.action.grid.column.name"
					var="label" ></spring:message>
				<spring:message code="business.policy.rule.action.grid.column.name.tooltip"
					var="tooltip" ></spring:message>
				<div class="table-cell-label">${label}</div>
				<div class="input-group ">
					<input type="text" id="policyActionName" name="id" maxlength="100"
						class="form-control table-cell input-sm" data-toggle="tooltip"
						data-placement="bottom" title="${tooltip }"> <span
						class="input-group-addon add-on last"> <i
						class="glyphicon glyphicon-alert" data-toggle="tooltip"
						data-placement="left" title=""></i></span>
				</div>
			</div>

			<div class="form-group">
				<spring:message code="business.policy.rule.action.grid.column.type"
					var="label" ></spring:message>
				<spring:message code="business.policy.rule.action.grid.column.type.tooltip"
					var="tooltip" ></spring:message>
				<div class="table-cell-label">${label }</div>
				<div class="input-group " style="display: table;">
					<select name="policyType"
						class="form-control table-cell input-sm" tabindex="3"
						id="policyType" data-toggle="tooltip"
						data-placement="bottom" title="${tooltip }">
						<option value="-1" selected="selected">SELECT ACTION
							TYPE</option>
						<option value="static" >static</option>
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
					code="business.policy.rule.action.grid.column.description" var="label" ></spring:message>
				<spring:message
					code="business.policy.rule.action.grid.column.description.tooltip"
					var="tooltip" ></spring:message>

				<div class="table-cell-label">${label }</div>
				<div class="input-group ">
					<input type="text" id="policyActionDesc" name="policyActionDesc"
						maxlength="100" class="form-control table-cell input-sm"
						data-toggle="tooltip" data-placement="bottom" title="${tooltip }">
					<span class="input-group-addon add-on last"> <i
						class="glyphicon glyphicon-alert" data-toggle="tooltip"
						data-placement="left" title=""></i></span>

				</div>
			</div>

			<div class="form-group">
				<spring:message code="business.policy.rule.action.grid.column.status"
					var="label" ></spring:message>
				<spring:message code="business.policy.rule.action.grid.column.status.tooltip"
					var="tooltip" ></spring:message>
				<div class="table-cell-label">${label }</div>
				<div class="input-group " style="display: table;">
					<select name="policyStatus"
						class="form-control table-cell input-sm" tabindex="3"
						id="policyStatus" data-toggle="tooltip"
						data-placement="bottom" title="${tooltip }">
						<option value="-1" selected="selected">SELECT ACTION
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
				<button class="btn btn-grey btn-xs" onclick="reloadGridDataWithClearMsg();" id="search_btn">
					<spring:message code="btn.label.search" ></spring:message>
				</button>
				<button class="btn btn-grey btn-xs" onclick="resetSearchParams();" id="reset_btn">
					<spring:message code="btn.label.reset" ></spring:message>
				</button>
			</sec:authorize>
		</div>

	</div>

	<!-- Jquery Policy Rule Grid start here -->
	<div class="fullwidth">
		<div class="title2">
			<spring:message code="business.policy.rule.action.tab.heading" ></spring:message>
			<span class="title2rightfield"> <span
				class="title2rightfield-icon1-text"> <sec:authorize
						access="hasAuthority('CREATE_SERVICE_INSTANCE')">
						<a href="#" onclick="redirectToUpdateRuleAction('create','0');">
							<i	class="fa fa-plus-circle"></i></a>
						<a href="#" id="createPolicyAction" onclick="redirectToUpdateRuleAction('create','0');">
							<spring:message code="business.policy.rule.action.create.label" ></spring:message>
						</a>

					</sec:authorize>
			</span>
			</span>
		</div>
	</div>

	<div class="box-body table-responsive no-padding box">
		<table class="table table-hover" id="ruleActiongrid"></table>
		<div id="ruleActionGridPagingDiv"></div>
	</div>
	<!-- Jquery Policy Rule Grid end here -->


</div>
<div style="display: block;">
	<form id="policy_rule_action_form" method="POST">
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
		<input type="hidden" id="actionType" name="actionType" />
		<input type="hidden" id="policyActionId" name="policyActionId" />
		
	</form>
</div>
<a href="#divDeletePolicyAction" class="fancybox" style="display: none;" id="deletePolicyActionPopup">#</a>
		   	<div id="divDeletePolicyAction" style=" width:100%; display: none;" >
			   <jsp:include page="deletePolicyActionPopUp.jsp"></jsp:include> 
			</div>
			
<a href="#createPolicyActionDiv" class="fancybox" style="display: none;" id="createPolicyActionPopup" >#</a>
		   	<div id="createPolicyActionDiv" style=" width:100%; display: none;" >
			   <jsp:include page="ruleActionManager.jsp"></jsp:include> 
			</div>

<script src="${pageContext.request.contextPath}/customJS/policyManager.js<%= "?v=" + Math.random() %>" type="text/javascript"></script>
			
<script type="text/javascript">

	$(document).ready(function() {
		loadJQueryPolicyRuleActionGrid();
	});

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
		var $grid = $("#ruleActiongrid");
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
		var currentPage = $(".ui-pg-input").val();
		if(!currentPage) {
			currentPage = 1;
		}
		var $grid = $("#ruleActiongrid");
		jQuery('#ruleActiongrid').jqGrid('clearGridData');
		clearInstanceGrid();
		$grid.jqGrid('setGridParam',{datatype:'json',page: currentPage,sortname: 'id',sortorder: "desc", postData:{
				'policyActionName': function () {
				var id  =  $("#policyActionName").val();
		        return id;
	    		},
	    		'policyActionType': function () {
		        return $("#policyType").val();
	    		},
	    		'policyActionDesc': function(){
	    			return $("#policyActionDesc").val();
	    		},
	    		'policyActionAssoStatus': function () {
		        return $("#policyStatus").val();
	    		},
	    		'serverInstanceId': function () {
	    	    return ${instanceId};
	        	}
	    		
	    		
		}}).trigger('reloadGrid');
	} 
	var currentPolicyActionPage = '${currentPage}';
	var oldGrid = '';
function loadJQueryPolicyRuleActionGrid(){
	$("#ruleActiongrid").jqGrid({
		url: "<%= ControllerConstants.GET_POLICY_RULE_ACTION_LIST%>",
		postData:
		{
    			'policyActionName': function () {
    			var id  =  $("#policyActionName").val();
    	        return id;
	    		},
	    		'policyActionType': function () {
    	        return $("#policyType").val();
	    		},
	    		'policyActionDesc': function(){
	    			return $("#policyActionDesc").val();
	    		},
	    		'policyActionAssoStatus': function () {
    	        return $("#policyStatus").val();
	    		},
	    		'serverInstanceId': function () {
	        	return ${instanceId};
	            }
		},
		datatype: "json",
		colNames: [
					  "#",
					  "<spring:message code='business.policy.rule.action.grid.column.name' ></spring:message>",
					  "#",
		           	  "<spring:message code='business.policy.rule.grid.column.description' ></spring:message>",
		              "<spring:message code='business.policy.rule.action.grid.column.type' ></spring:message>",
		              "<spring:message code='business.policy.rule.action.grid.column.syntaxt' ></spring:message>",
		              "<spring:message code='business.policy.rule.action.grid.column.syntaxt' ></spring:message>",
		              "<spring:message code='business.policy.rule.grid.column.delete' ></spring:message>"
			],
		colModel: [
			{name:'id',index:'id',align:'center',sortable:true ,hidden:true},
			{name:'name',index:'name',sortable:true,formatter: ruleActionNameFormatter},
			{name:'action_name',index:'action_name',hidden:true,formatter: ruleActionActualNameFormatter},
			{name:'description',index:'description',sortable:true},
			{name:'type',index:'type',sortable:true},
			{name:'expression',index:'expression',sortable:false,hidden: true},
			{name:'actionExpression',index:'actionExpression',sortable:true,formatter: ruleActionColumnFormatter},
			{name:'delete',index:'delete',sortable:false, align:'center',formatter: ruleAcionDeleteColumnFormatter},
		],
		rowNum:<%= MapCache.getConfigValueAsInteger(SystemParametersConstant.TOTAL_ROWS_TO_DISPLAY_IN_GRID, 10) %>,
		rowList:[10,20,60,100],
		height: 'auto',
		sortname: 'id',
		sortorder: "desc",
		pager: "#ruleActionGridPagingDiv",
		viewrecords: true,
		multiselect: false,
		timeout : 120000,
	    loadtext: "Loading...",
		caption: "<spring:message code="collectionService.summary.agent.grid.caption"></spring:message>",
		beforeRequest:function(){
			if(oldGrid != ''){
        		$('#ruleActiongrid tbody').html(oldGrid);
        	} else if(currentPolicyActionPage && currentPolicyActionPage > 1) {
				$('#ruleActiongrid').jqGrid('setGridParam', { postData: { page: currentPolicyActionPage} });
				currentPolicyActionPage = 0;
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
 			if ($('#ruleActiongrid').getGridParam('records') === 0) {
 				oldGrid = $('#ruleActiongrid tbody').html();
             $('#ruleActiongrid tbody').html("<div style='padding:6px;background:#D8D8D8;text-align:center;'><spring:message code='serverManagement.grid.empty.records'></spring:message></div>");
             $("#ruleActionGridPagingDiv").hide();
        	}else{
            	$("#ruleActionGridPagingDiv").show();
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
		}).navGrid("#ruleActionGridPagingDiv",{edit:false,add:false,del:false,search:false});
			$(".ui-jqgrid-titlebar").hide();
			$(".ui-pg-input").height("10px");
}

function ruleAcionDeleteColumnFormatter(cellvalue, options, rowObject) {
	if(cellvalue == 'Associated'){
		return '<p>' + cellvalue + '</p>';
	}else{ 
		<sec:authorize access="hasAnyAuthority('DELETE_POLICY_ACTION_CONFIGURATION')">  
			return '<a href="#" id="'+rowObject["name"]+'_delete_lnk" onclick="deletePolicyActionPopup('+"'"+rowObject["id"]+"'"+','+"'"+rowObject["name"]+"'"+')"><i class="fa fa-trash"></i></a>';	
		</sec:authorize>
		<sec:authorize access="!hasAnyAuthority('DELETE_POLICY_ACTION_CONFIGURATION')">  
			return '<a href="#" id="'+rowObject["name"]+'_delete_lnk"><i class="fa fa-trash"></i></a>';	
		</sec:authorize>
	} 
}

function deletePolicyActionPopup(id,name){
	clearAllMessages();
	$("#divDeletePolicyActionMsg").html('');
	$("#btnDeletePolicyActionCancel").show();
	$("#btnDeletePolicyActionPopup").show();
		
		$("#tblDeletePolicyActionList").html('');
		parent.ckIntanceSelected[0]=id;
		tableString ='<table class="table table-hover" style="width:100%">';
			tableString += "<tr>";
			tableString += "<th>"+jsLocaleMsg1.policyActionId+"</th>";
			tableString += "<th>"+jsLocaleMsg1.policyActionName +"</th>";
			tableString += "</tr>";
			tableString += "<tr>";
			tableString += "<td>"+id+"</td>";
			tableString += "<td>"+name+"</td>";
			tableString += "</tr>";
			tableString += "</table>"
		$("#tblDeletePolicyActionList").html(tableString);
		$('#deletePolicyActionPopup').click();
}

function DeletePolicyActionInstance(){
	clearAllMessages();
	$("#delete-progress-bar-div").show();
	$("#delete-buttons-div").hide();

	$.ajax({
				url: "deletePolicyRuleAction",
			    cache: false,
				async: true,
				dataType: 'json',
				type: "POST",
				data: {
					'actionId': parent.ckIntanceSelected[0],
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
		    			    reloadGridData();
			    			$("#btnDeletePolicyActionCancel").hide();
		    			
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
    					$("#btnDeletePolicyActionCancel").show();
					}
			
			}); 
	}

</script>
