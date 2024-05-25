<%@page import="com.elitecore.sm.util.MapCache"%>
<%@page import="com.elitecore.sm.common.constants.SystemParametersConstant"%>
<%@page import="org.apache.catalina.connector.Request"%>
<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<%@page import="com.elitecore.sm.common.constants.BaseConstants"%>
<%@taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="/WEB-INF/ShowErrorTag.tld" prefix="elitecoreError"%>
<%-- <script src="${pageContext.request.contextPath}/js/bootstrap.min.js"
	type="text/javascript"></script> --%>
<div class="fullwidth mtop10">
	<div class="fullwidth borbot tab-pane active">
		<div class="fullwidth">
			<div class="title2">
				<spring:message code="policymgmt.rule.search.title" ></spring:message>
			</div>
		</div>


		<div class="col-md-6 inline-form"
			style="padding-left: 0px !important;">
			<div class="form-group">
				<div class="table-cell-label">
					<spring:message code="policymgmt.rule.name" ></spring:message>
				</div>
				<div class="input-group">
					<spring:message code="policymgmt.rule.name" var="tooltip" ></spring:message>
					<input type="text" id="search-rule-name"
						class="form-control table-cell input-sm" data-toggle="tooltip"
						tabindex="1" data-placement="bottom" title="${tooltip}"> <span
						class="input-group-addon add-on last"> <i
						class="glyphicon glyphicon-alert" data-toggle="tooltip"
						data-placement="bottom"></i>
					</span>
				</div>
			</div>
			<div class="form-group">
				<spring:message code="policymgmt.policy.associationstatus"
					var="tooltip" ></spring:message>
				<div class="table-cell-label">${tooltip}</div>
				<div class="input-group ">
					<select id="search-rule-association-status" class="form-control"
						title="${tooltip}" data-toggle="tooltip" tabindex="3"
						data-placement="bottom">
						<option value="ALL" selected="selected">
							<spring:message code="policymgmt.policy.associationstatus.all" ></spring:message>
						</option>
						<option value="Associated">
							<spring:message
								code="policymgmt.policy.associationstatus.associated" ></spring:message>
						</option>
						<option value="Non-Associated">
							<spring:message
								code="policymgmt.policy.associationstatus.nonassociated" ></spring:message>
						</option>
					</select> <span class="input-group-addon add-on last"> <i
						class="glyphicon glyphicon-alert" data-toggle="tooltip"
						data-placement="bottom"></i>
					</span>
				</div>
			</div>
			<div class="form-group" id = "reprocess_reason_severity_div" style="display: none;">
	     		<spring:message code="file.reprocess.severity.label"  var="label"></spring:message>
	     		<spring:message code="file.reprocess.severity.tooltip"  var="tooltip"></spring:message>
	     		<div class="table-cell-label">${label}</div>
	         	<div class="input-group ">
	         		<spring:message code="select.option.type.severity"  var="selectType"></spring:message>
	         		<select name = "reasonSeverity" class="form-control table-cell input-sm"  tabindex="3" id="reasonSeverity" data-toggle="tooltip" data-placement="bottom"  title="${tooltip}" onchange="reloadRuleGridData()">
	         			<option  value="-1" selected="selected">${selectType}</option>
	          		<c:forEach items="${policyRuleSeverity}" var="policyRuleSeverity">
	          			<option value="${policyRuleSeverity.value}">${policyRuleSeverity.value}</option>
	          		</c:forEach>
	         		</select>
	         		<span class="input-group-addon add-on last" > <i id="reasonSeverity_error" class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="left" title=""></i></span> 
	         	</div>
         	</div>
		</div>

		<div class="col-md-6 inline-form">
			<div class="form-group">
				<spring:message code="policymgmt.rule.description" var="tooltip" ></spring:message>
				<div class="table-cell-label">${tooltip}</div>
				<div class="input-group ">
					<input type="text" id="search-rule-desc"
						class="form-control table-cell input-sm" data-toggle="tooltip"
						tabindex="2" data-placement="bottom" title="${tooltip}"> <span
						class="input-group-addon add-on last"> <i
						class="glyphicon glyphicon-alert" data-toggle="tooltip"
						data-placement="bottom"></i>
					</span>
				</div>
			</div>

			<div class="form-group" id = "reprocess_reason_category_div">
	       		<spring:message code="file.reprocess.reason.category.label"  var="label"></spring:message>
	       		<spring:message code="file.reprocess.reason.category.tooltip"  var="tooltip"></spring:message>
	       		<div class="table-cell-label">${label}</div>
	           	<div class="input-group ">
	           		<spring:message code="select.option.type.category"  var="selectType"></spring:message>
	           		<select name = "reasonCategory" class="form-control table-cell input-sm"  tabindex="3" id="reasonCategory" data-toggle="tooltip" data-placement="bottom"  title="${tooltip}" onChange="manageReasonCategoryOption()">
	           			<option  value="-1" selected="selected">${selectType}</option>
	             		<c:forEach items="${policyRuleCategory}" var="policyRuleCategory">
	             			<option value="${policyRuleCategory.value}">${policyRuleCategory.value}</option>
	             		</c:forEach>
	            		
	           		</select>
	           		<span class="input-group-addon add-on last" > <i id="reasonCategory_error" class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="left" title=""></i></span> 
	           	</div>
         	</div>
         	
         	<div class="form-group" id = "reprocess_reason_errorcode_div" tabindex="4" style="display: none;">
             	<spring:message code="file.reprocess.errorcode.label" var="label" ></spring:message>
             	<spring:message code="file.reprocess.errorcode.tooltip" var="tooltip" ></spring:message>
         		<div class="table-cell-label">${label}</div>
             	<div class="input-group ">
             		<input type="text" id="reasonErrorCode" class="form-control table-cell input-sm" data-toggle="tooltip" data-placement="bottom" tabindex="5" title="${tooltip}" maxlength="250">
             		<span class="input-group-addon add-on last" > <i id="reasonErrorCode_error" class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="left" title=""></i></span>
           		</div>
            </div>
		</div>

		<div class="clearfix"></div>
		<div class="pbottom15 mtop10">
			<button class="btn btn-grey btn-xs" onclick="reloadRuleGridData();"
				tabindex="5" id="search_btn">
				<spring:message code="btn.label.search" ></spring:message>
			</button>
			<button class="btn btn-grey btn-xs"
				onclick="resetSearchRuleCriteria();" tabindex="6" id="reset_btn">
				<spring:message code="btn.label.reset" ></spring:message>
			</button>
		</div>
	</div>
	<div class="fullwidth">
		<div class="title2">
			<spring:message code="policymgmt.rule.list.caption" ></spring:message>
			<span class="title2rightfield"> <span
				class="title2rightfield-icon1-text"> <sec:authorize
						access="hasAuthority('CREATE_POLICY_CONFIGURATION')">
						<a href="#" onclick="createRule();"><i
							class="fa fa-plus-circle"></i></a>

						<a href="#" id="createRule" onclick="createRule();">
							<spring:message code="business.policy.create.rule.tab.heading" ></spring:message>
						</a>

					</sec:authorize>
			</span>
			</span>
		</div>


	</div>

	<div class="box-body table-responsive no-padding box">
		<table class="table table-hover" id="ruleGrid"></table>
		<div id="ruleGridPagingDiv"></div>
		<div class="clearfix"></div> 
	    <div id="divLoading" align="center" style="display: none;"><img src="img/preloaders/Preloader_10.gif" /></div>
	</div>

	<!-- Jquery Policy Rule Grid end here -->


</div>
<%-- <a href="#divRuleList" class="fancybox" style="display: none;"
	id="ruleList">#</a>
<div id="divRuleList" style="width: 100%; display: none;">
	<jsp:include page="policyRuleListPopUp.jsp"></jsp:include>
</div> --%>

<div style="display: none;">
	<form id="update_rule_form" method="GET">
		<input type="hidden" id="server-instance-name" name="server-instance-name" value='${instanceName}' /> 
		<input type="hidden" id="server-instance-host" name="server-instance-host" value='${host}' /> 
		<input type="hidden" id="server-instance-port" name="server-instance-port" value='${port}' /> 
		<input type="hidden" id="server-instance-id" name="server-instance-id" value='${instanceId}' />
		<input type="hidden" id="serviceInstanceId" name="serviceInstanceId" value="${serviceInstanceId}">
		<input type="hidden" id="serviceId" name="serviceId" value="${serviceId}"> 
		<input type="hidden" id="serviceType" name="serviceType" value="<%=BaseConstants.PROCESSING_SERVICE%>">
		<input type="hidden" id="serviceName" name="serviceName" value="${serviceName}">
		
		<input type="hidden" id="ruleId" name="ruleId" />
		<input type="hidden" id="currentPage" name="currentPage" value='${currentPage}' />
		<!-- <input type="hidden" id="ruleGroupId" name="ruleGroupId" /> 
		<input type="hidden" id="ruleGroupName"	name="ruleGroupName" /> 
		<input type="hidden" id="ruleGroupDesc" name="ruleGroupDesc" /> -->
	</form>
</div>

<a href="#divDeleteRule" class="fancybox" style="display: none;" id="deleteRulePopUp">#</a>
 	<div id="divDeleteRule" style=" width:100%; display: none;" >
 		<jsp:include page="deleteRulePopUp.jsp"></jsp:include>
</div>

<a href="#divViewAdditioanlParam" class="fancybox" style="display: none;" id="viewAdditioanlParam">#</a>
 	<div id="divViewAdditioanlParam" style=" width:100%; display: none;" >
 		<jsp:include page="viewRuleParamPopUp.jsp"></jsp:include>
</div>

<a href="#divViewRuleConditions" class="fancybox" style="display: none;" id="viewRuleConditions">#</a>
 	<div id="divViewRuleConditions" style=" width:100%; display: none;" >
 		<jsp:include page="viewRuleConditionPopUp.jsp"></jsp:include>
</div>

<a href="#divViewRuleActions" class="fancybox" style="display: none;" id="viewRuleActions">#</a>
 	<div id="divViewRuleActions" style=" width:100%; display: none;" >
 		<jsp:include page="viewRuleActionPopUp.jsp"></jsp:include>
</div>
<div class="box-body table-responsive no-padding box">
		<table class="table table-hover" id="policyRuleGrid"></table>
		<div id="rulePagingDiv"></div>
		<div class="clearfix"></div> 
	    <div id="divLoading" align="center" style="display: none;"><img src="img/preloaders/Preloader_10.gif" /></div>
	</div>

<script type="text/javascript">
var ckIntanceSelected = new Array();
var currentPolicyRulePage = '${currentPage}';	

function manageReasonCategoryOption(){
	var selectedReasonCategory = $("#reasonCategory").val();
	if(selectedReasonCategory == 'NA' || selectedReasonCategory == 'Filter'){
		$('#reprocess_reason_severity_div').hide();
		$('#reprocess_reason_errorcode_div').hide();
	}else{
		$('#reprocess_reason_severity_div').show();
		$('#reprocess_reason_errorcode_div').show();
	}	
		
}
	$(document).ready(function() {
		loadJQueryRuleGrid();
		$('.fancybox').fancybox({
			maxWidth	: 700,
			maxHeight	: 300,
			fitToView	: false,
			width		: '90%',
			//height		: '80%',
			autoSize	: false,
			closeClick	: false,
			openEffect	: 'none',
			closeEffect	: 'none'
		});
		manageReasonCategoryOption();
	});
	var oldGrid = '';
	function loadJQueryRuleGrid(){
		$("#ruleGrid").jqGrid({
			url: "<%= ControllerConstants.GET_POLICY_RULE_LIST%>",
			postData : {
				ruleName: function () {
	   	        	return $("#search-rule-name").val();
	    		},
	    		description: function () {
	   	        	return $("#search-rule-desc").val();
	    		},
	    		associationStatus: function(){
	    			return $("#search-rule-association-status").val();
	    		},
	    		serverInstanceId: function () {
	    			return '${instanceId}';
	    		},
	    		reasonCategory: function () {
	    			return $("#reasonCategory").val();
	    		},
	    		reasonSeverity: function () {
	    			return $("#reasonSeverity").val();
	    		},
	    		reasonErrorCode: function () {
	    			return $("#reasonErrorCode").val();
	    		}
			},
			datatype: "json",
			colNames: [
						  "#",
						  "<spring:message code='business.policy.create.rule.name.label' ></spring:message>",
			           	  "<spring:message code='business.policy.create.rule.description.label' ></spring:message>",
			              "<spring:message code='policymgmt.rule.grid.column.condition' ></spring:message>",
			              "<spring:message code='policymgmt.rule.grid.column.action' ></spring:message>",
			              "<spring:message code='policymgmt.rule.grid.column.additionalparam' ></spring:message>",
			              "<spring:message code='business.policy.create.rule.category.label' ></spring:message>",
						  "<spring:message code='business.policy.create.rule.severity.label' ></spring:message>",
						  "<spring:message code='business.policy.create.rule.error.code.label' ></spring:message>",
			              "<spring:message code='business.policy.rule.group.grid.column.delete' ></spring:message>",
			              "#",
			              "#",
			              "#",
			              "#"
				],
			colModel: [
				{name:'id',index:'id',hidden:true},
				{name:'name',index:'name',sortable:true,formatter:ruleNameFormatter},
				{name:'description',index:'description',sortable:true},
				{name:'conditionList',index:'conditionList',align:'center',formatter:conditionListFormatter},
				{name:'actionList',index:'actionList',align:'center',formatter:actionListFormatter},
				{name:'additionalParam',index:'additionalParam',align:'center',formatter:additionalParamFormatter},
				{name : 'category',	index : 'category', sortable : false},
	            {name : 'severity',	index : 'severity', sortable : false}, 
	            {name : 'error_code',	index : 'error_code', sortable : false}, 
				{name:'associationStatus',index:'associationStatus',align:'center', formatter:ruleDeleteColumnFormatter},
				{name:'alertId',index:'alertId',hidden:true},
				{name:'alertDescription',index:'alertId',hidden:true},
				{name:'globalSequenceId',index:'alertId',hidden:true},
				{name:'operator',index:'alertId',hidden:true}
			],
			rowNum:<%= MapCache.getConfigValueAsInteger(SystemParametersConstant.TOTAL_ROWS_TO_DISPLAY_IN_GRID, 10) %>,
			rowList:[5,10,20,50,100],
			height: 'auto',
			sortname: 'id',
			sortorder: "desc",
			pager: "#rulePagingDiv",
			viewrecords: true,
			multiselect: false,
			timeout : 120000,
		    loadtext: "Loading...",
			caption: "<spring:message code="policymgmt.rule.list.caption"></spring:message>",
			beforeRequest:function(){
				if(oldGrid != ''){
            		$('#ruleGrid tbody').html(oldGrid);
            	} else if(currentPolicyRulePage && currentPolicyRulePage > 1) {
					$('#ruleGrid').jqGrid('setGridParam', { postData: { page: currentPolicyRulePage} });
					currentPolicyRulePage = 0;
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
	 			if ($('#ruleGrid').getGridParam('records') === 0) {
	 				oldGrid = $('#ruleGrid tbody').html();
	             $('#ruleGrid tbody').html("<div style='padding:6px;background:#D8D8D8;text-align:center;'><spring:message code='serverManagement.grid.empty.records'></spring:message></div>");
	             $("#rulePagingDiv").hide();
	        	}else{
	            	$("#rulePagingDiv").show();
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
			}).navGrid("#rulePagingDiv",{edit:false,add:false,del:false,search:false});
				$(".ui-jqgrid-titlebar").hide();
				$(".ui-pg-input").height("10px");
	}
	
	var j=0;
	function rulesrNoFormatter(cellvalue, options, rowObject){
		
		j=j+1;
		return j;
	}
	

	function conditionListFormatter(cellvalue, options, rowObject) {
		return '<a href="#" onclick="ruleRuleConditionsPopUp('+"'" + rowObject["id"]+ "'"+');"><i class="fa fa-fw fa-vimeo-square"></i></a>';
	}
	
	function actionListFormatter(cellvalue, options, rowObject) {
		return '<a href="#" onclick="ruleRuleActionsPopUp('+"'" + rowObject["id"]+ "'"+');"><i class="fa fa-fw fa-vimeo-square"></i></a>';
	}
	
	function additionalParamFormatter(cellvalue, options, rowObject) {
		return '<a href="#" onclick="viewRuleParamPopup('+"'"+rowObject["alertId"]+"'"+','+"'"+rowObject["alertDescription"]+"'"+','+"'"+rowObject["globalSequenceId"]+"'"+','+"'"+rowObject["operator"]+ "'"+');"><i class="fa fa-fw fa-vimeo-square"></i></a>';		
	}
	
	function ruleNameFormatter(cellvalue, options, rowObject){
		
		return '<a class="link" id="'+rowObject["name"]+'_name_lnk" onclick="updateRule('+"'" + rowObject["id"]+ "'" + ');">' + cellvalue + '</a>' ;
	}

	function ruleDeleteColumnFormatter(cellvalue, options, rowObject) {
		
		if(cellvalue == 'Associated') {
			return '<p>' + cellvalue + '</p>';
		} else {
			return '<a href="#" id="'+rowObject["name"]+'_delete_lnk" onclick="deleteRulePopup('+"'"+rowObject["id"]+"'"+','+"'"+rowObject["name"]+"','"+rowObject["description"]+"'"+')"><i class="fa fa-trash"></i></a>';			
		}
	}
	
	
	function createRule(){
		
		var serviceId='${serviceId}';

		if(serviceId == ''){
			$("#server_instance_detail_form").attr("action","<%=ControllerConstants.INIT_CREATE_RULE%>");
			$("#server_instance_detail_form").submit();
		}else{
			$("#service_detail_form").attr("action","<%=ControllerConstants.INIT_CREATE_RULE%>");
			$("#service_detail_form").submit();
		}
	}
	
	function updateRule(id,name,des){
		var currentPage = $(".ui-pg-input").val();
		$("#currentPage").val(currentPage);
		$("#ruleId").val(id);
		$("#update_rule_form").attr("action","<%=ControllerConstants.INIT_CREATE_RULE%>");
		$("#update_rule_form").submit();
	}
	
	function reloadRuleGridData() {
		var $grid = $("#ruleGrid");
		jQuery('#ruleGrid').jqGrid('clearGridData');
		$grid.jqGrid('setGridParam',{datatype:'json',page: 1,sortname: 'id',sortorder: "desc", postData:{
			ruleName: function () {
   	        	return $("#search-rule-name").val();
    		},
    		description: function () {
   	        	return $("#search-rule-desc").val();
    		},
    		associationStatus: function(){
    			return $("#search-rule-association-status").val();
    		},
    		serverInstanceId: function () {
    			return '${instanceId}';
    		},
    		reasonCategory: function () {
    			return $("#reasonCategory").val();
    		},
    		reasonSeverity: function () {
    			return $("#reasonSeverity").val();
    		},
    		reasonErrorCode: function () {
    			return $("#reasonErrorCode").val();
    		}		   
		   
		}}).trigger('reloadGrid');
	} 
	
	function resetSearchRuleCriteria() {
		
		$("#search-rule-name").val('');
		$("#search-rule-desc").val('');
		$("#search-rule-association-status").val('ALL');
		searchRule();
	}
	
	function searchRule() {
		
		// clearResponseMsgDiv();
		clearRuleGrid();
		var $grid = $("#ruleGrid");
		$grid.jqGrid('setGridParam',{datatype:'json',page: 1}).trigger('reloadGrid');
	}
	
	function clearRuleGrid() {
		var $grid = $("#ruleGrid");
		var rowIds = $grid.jqGrid('getDataIDs');
		for(var i=0,len=rowIds.length;i<len;i++){
			var currRow = rowIds[i];
			$grid.jqGrid('delRowData', currRow);
		}
	}
	
	
	function deleteRulePopup(id,name,desc){
		clearAllMessages();
		
		$("#divDeleteRuleMsg").html('')
		$("#btnDeleteRulePopup").show();
		$("#btnDeleteRuleCancel").show();

		$("#tblDeleteRuleList").html('');
		parent.ckIntanceSelected[0]=id;
		tableString ='<table class="table table-hover" style="width:100%">';
		tableString += "<tr>";
 		tableString += "<th><spring:message code='business.policy.create.rule.name.label'></spring:message></th>";
 		tableString += "<th><spring:message code='business.policy.create.rule.description.label'></spring:message></th>";
		tableString += "</tr>";
		tableString += "<tr>";
		tableString += "<td>"+name+"</td>";
		tableString += "<td>"+desc+"</td>";
		tableString += "</tr>";
		tableString += "</table>"
		$("#tblDeleteRuleList").html(tableString);
		$('#deleteRulePopUp').click();
	}
	
	function viewRuleParamPopup(alertId, alertDescription, globalSequenceId, operator){
		clearAllMessages();
		
		$("#btnDeleteRulePopup").show();
		$("#btnViewRuleParamClose").show();

		$("#tblRuleParamList").html('');
		parent.ckIntanceSelected[0]=id;
		tableString ='<table class="table table-hover" style="width:100%">';
		tableString += "<tr>";
 		tableString += "<th><spring:message code='policymgmt.rule.view.additional.param.popup.column.name.header'></spring:message></th>";
 		tableString += "<th><spring:message code='policymgmt.rule.view.additional.param.popup.column.value.header'></spring:message></th>";
		tableString += "</tr>";
		tableString += "<tr>";
		tableString += "<td><spring:message code='business.policy.create.rule.alert.id.label'></spring:message></td>";
		tableString += "<td>"+alertId+"</td>";
		tableString += "</tr>";
		tableString += "<tr>";
		tableString += "<td><spring:message code='business.policy.create.rule.alert.description.label'></spring:message></td>";
		tableString += "<td>"+alertDescription+"</td>";
		tableString += "</tr>";
		tableString += "<tr>";
		tableString += "<td><spring:message code='business.policy.create.rule.global.rule.label'></spring:message></td>";
		tableString += "<td>"+globalSequenceId+"</td>";
		tableString += "</tr>";
		tableString += "<tr>";
		tableString += "<td><spring:message code='policymgmt.rule.create.operator'></spring:message></td>";
		tableString += "<td>"+operator+"</td>";
		tableString += "</tr>";
		tableString += "</table>"
		$("#tblRuleParamList").html(tableString);
		$('#viewAdditioanlParam').click();
	}
	
	function ruleRuleConditionsPopUp(id) {
		clearAllMessages();

		$("#btnRuleConditionListClose").show();
		loadJQueryRuleConditionGrid(id);
		$('#viewRuleConditions').click();
	}
	
	function ruleRuleActionsPopUp(id) {
		clearAllMessages();

		$("#btnRuleActionListClose").show();
		loadJQueryRuleActionGrid(id);
		$('#viewRuleActions').click();
	}
	
	
	function reloadRuleData(id){
		

		var $grid = $("#ruleGrid");
		jQuery('#ruleGrid').jqGrid('clearGridData');
		clearRuleGrid();
		$grid.jqGrid('setGridParam',{datatype:'json',page: 1,sortname: 'id',sortorder: "desc", postData:{
			'ruleId': id   
		   
		}}).trigger('reloadGrid');
		} 
	
	
	function deleteRuleInstance(actionUrl){
		
		clearAllMessages();
		$("#delete-progress-bar-div").show();
		$("#delete-buttons-div").hide();

		$.ajax({
			type: "POST",
			url: actionUrl,
			cache: false,
			async: true,
			dataType: 'json',
			data: {
				'ruleId': parent.ckIntanceSelected[0],
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
				    $('#ruleGrid').trigger('reloadGrid');
				    $("#btnDeleteRuleCancel").hide();
				   
				}else{
				    $("#divDeleteMsg").css('color','red');
				    $("#divDeleteMsg").css('font-weight','normal');
				    $("#divDeleteMsg").html(response.msg);
				    showErrorMsg(response.msg);
				}
			   
			},
			   error: function (xhr,st,err){
			$("#delete-buttons-div").show();
			$("#delete-progress-bar-div").hide();
			    handleGenericError(xhr,st,err);
			    $("#btnDeleteRuleCancel").show();
			}

		});
	} 


</script> 
