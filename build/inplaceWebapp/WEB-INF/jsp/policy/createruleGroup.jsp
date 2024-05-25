<%@page import="com.elitecore.sm.common.constants.FormBeanConstants"%>
<%@page import="com.elitecore.sm.common.constants.BaseConstants"%>
<%@page import="com.elitecore.sm.util.MapCache"%>
<%@page import="com.elitecore.sm.common.constants.SystemParametersConstant"%>
<%@page import="org.apache.catalina.connector.Request"%>
<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>

<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib 	uri="http://www.springframework.org/security/tags" 	prefix="sec"  %>
<%@taglib uri="/WEB-INF/ShowErrorTag.tld" prefix="elitecoreError" %>
<html>
<head>
<style>
.ui-jqgrid .ui-jqgrid-bdiv { overflow-y: scroll;  max-height: 370px; }
</style>
<script type="text/javascript">

	var selectedRulesCk = new Array();
	var existingRuleIds = new Array();
	var datatype;
	
	var ruleGroupName = '${rulegroup_form_bean.name}';
	$(document).ready(function() {
		
		var ruleGroupId='${rulegroup_form_bean.id}';
		if(ruleGroupId == '' || ruleGroupId == 0){
			datatype='local';
			
		}else{
			datatype='json';
			$("#rule-group-name").val('${rulegroup_form_bean.name}');
			$("#rule-group-name").prop("readOnly", false);
			$("#rule-group-description").val('${rulegroup_form_bean.description}');
		}
		loadJQueryPolicyRuleGrid();
	});
	
	function selectRulePopUp() {
		existingRuleIds = [];
		$("#search_ruleName").val('');
		$("#search_ruleDescription").val('');
		var fullData = jQuery("#policyRuleGrid").jqGrid('getRowData');
		for(i = 0; i < fullData.length; i++) {
			existingRuleIds.push(fullData[i]['id']);
		}
		console.log(existingRuleIds);
		$('#rule-list').trigger('reloadGrid');
		$('#selectRuleList').click();
		
	}
	
    function ruleNameFormatter(cellvalue, options, rowObject){
		
		return '<a class="link" id="'+rowObject["name"]+'_name_lnk" onclick="updateRule('+"'" + rowObject["id"]+ "'" + ');">' + cellvalue + '</a>' ;
	}
    
    function updateRule(id,name,des){
		$("#ruleId").val(id);
		$("#update_rule_form").attr("action","<%=ControllerConstants.INIT_CREATE_RULE%>");
		$("#update_rule_form").submit();
	}
	
	function loadJQueryPolicyRuleGrid(){
		$("#policyRuleGrid").jqGrid({
			url:"<%= ControllerConstants.GET_POLICY_RULE_LIST_BY_RULE_GROUP_ID %>",
			mtype:"GET",
			postData : {
				'ruleGroupId': function () {
	    			return '${rulegroup_form_bean.id}';
	    		}
			},
			datatype:datatype,
			jsonReader: { repeatitems: false, root: "rows", page: "page", total: "total", records: "records"},
			colNames: [
						  "<spring:message code='business.policy.rule.grid.column.id' ></spring:message>",
						  "<spring:message code='policymgmt.rulegroup.applicationorder'></spring:message>",
						  "<spring:message code='business.policy.rule.grid.column.ruleName' ></spring:message>",
			           	  "<spring:message code='business.policy.rule.grid.column.description' ></spring:message>",
			              "#"
				],
			colModel: [
				{name:'id',index:'id',hidden:true},
				{name: 'applicationOrder', index: 'applicationOrder', hidden: true},
				{name:'name',index:'name', sortable: false , key:true, formatter:ruleNameFormatter, cellattr:function(rowId, cellValue, rawObject, cm, rdata){ 
					return "id='"+rawObject["name"]+"'"  ;
				}},
				{name:'description',index:'description',sortable: false ,cellattr:function(rowId, cellValue, rawObject, cm, rdata) {
					return "id='"+rawObject["description"]+"'"  ;
				}},
				{name:'relId',index:'relId',hidden:true}
			],
			rownumbers: true,
			rowNum : 500,
			rowList: [],        // disable page size dropdown
       	    pgbuttons: false,   // disable page control like next, back button
       	    //pgtext: "",       // disable pager text like 'Page 0 of 10'
			height : 'auto',
			sortname : 'applicationOrder',
			sortorder : "asc",
			pager : "#policyRuleGridPagingDiv",
			contentType : "application/json; charset=utf-8",
			viewrecords : true,
			
			multiselect: true,
			timeout : 120000,
		    loadtext: "Loading...",
			caption: "<spring:message code="business.policy.rule.group.grid.heading"></spring:message>",
			autowidth: true,		    		    
			beforeRequest:function(){
		 		$('#divLoading').dialog({
		             autoOpen: false,
		             width: 90,
		             modal:true,
		             overlay: { opacity: 0.3, background: "white" },
		             resizable: false,
		             height: 125
		         });
		 		selectedRulesCk = new Array();
		    },
			loadComplete: function(data) {
		 		$("#divLoading").dialog('close');
		 		//var rowIds = $grid.jqGrid('getDataIDs');
		 		//alert(rowIds);
		 			$(".ui-dialog-titlebar").show();
		 			if ($('#grid').getGridParam('records') === 0) {
		             $('#grid tbody').html("<div style='padding:6px;background:#D8D8D8;text-align:center;'><spring:message code="collectionService.summary.agent.grid.empty.records"></spring:message></div>");
		             $("#agentGridPagingDiv").hide();
		         }
				
			},
			loadError : function(xhr,st,err) {
				handleGenericError(xhr,st,err);
			},
			recordtext: "<spring:message code="serverManagement.grid.pager.total.records.text"></spring:message>",
		    emptyrecords: "<spring:message code="collectionService.summary.agent.grid.empty.records"></spring:message>",
			loadtext: "<spring:message code="collectionService.summary.agent.grid.loading.text"></spring:message>",
			pgtext : null,
			beforeSelectRow: function (rowid, e){
				var $grid = $("#policyRuleGrid");
				if(document.getElementById('jqg_policyRuleGrid_'+rowid).checked){
					if(selectedRulesCk.indexOf(rowid) == -1){
						selectedRulesCk.push(rowid);
					}
				}else{
					if(selectedRulesCk.indexOf(rowid) != -1){
						selectedRulesCk.splice(selectedRulesCk.indexOf(rowid), 1);
					}
				}
			    return false;
			},
			onSelectAll : function(id, status) {
				if (status == true) {
					selectedRulesCk = new Array();
					for (i = 0; i < id.length; i++) {
						selectedRulesCk.push(id[i]);
					}
				} else {
					selectedRulesCk = new Array();
				}
			}
			}).navGrid("#policyRuleGridPagingDiv",{edit:false,add:false,del:false,search:false});
				$(".ui-jqgrid-titlebar").hide();
				$(".ui-pg-input").height("10px");
				
				$("#policyRuleGrid").sortableRows();   
				$("#policyRuleGrid").disableSelection();
			    $("#policyRuleGrid").sortable({
			    	items: 'tr:not(:first)'
			    });
				$("#policyRuleGrid").bind('sortstop', function(event, ui) { 
					});
				$("#policyRuleGrid").jqGrid('gridDnD');
				
	}
	
	function deleteSelectedRulesById(){
		if(selectedRulesCk.length > 0) {
			for (i = 0, n = selectedRulesCk.length; i < n; i++) {
			    $('#policyRuleGrid').jqGrid('delRowData',selectedRulesCk[i]);
			}
		}	
		selectedRulesCk = [];
		ckRuleSelected = [];
		existingRuleIds = [];
		closeFancyBox();
	}
	
	function deleteSelectedRules() {
		clearAllMessagesPopUp();
		resetWarningDisplayPopUp();
		clearAllMessages();
		if(selectedRulesCk != null && selectedRulesCk.length > 0){
		var tableString ="<table class='table table-hover table-bordered'  border='1' >";
		tableString+="<th>#</th><th>Rule ID</th><th>Rule Name</th>";
			for(var i = 0 ; i< selectedRulesCk.length;i++){
			
				//var	rowData = $('#policyConditionGrid').jqGrid('getRowData', selectedConditionCk[i]);
				var rowData = $('#policyRuleGrid').jqGrid('getRowData',selectedRulesCk[i]);
				tableString += "<tr>";
				tableString += "<td><input type='checkbox' name='rule_delete' id='rule_"+selectedRulesCk[i]+"' checked  onclick=getSelectedRuleList('"+selectedRulesCk[i]+"')  value='"+selectedRulesCk[i]+"' /> </td>";
				tableString += "<td>"+rowData.id+" </td>";
				tableString += "<td>"+rowData.name+"</td>";
				tableString += "</tr>";
			}	
				tableString+="</table>";
					$("#delete_selected_rule_details").html(tableString);
					$("#delete_rule_bts_div").show();
					$("#delete_rule_progress_bar_div").hide();
					$("#delete_close_rule_buttons_div").hide();
					$("#selectedRuleDeleteBtn").show();
					$("#deleteRuleId").val(selectedRulesCk.toString());
					$("#delete_rule").click();
				
			//$('#policyRuleGrid').trigger('reloadGrid');
		}else{
			var tableString ="<table class='table table-hover table-bordered'  border='1' >";
			tableString+="<b><p>No rules selected</p></b></table>";
			$("#delete_selected_rule_details").html(tableString);
			$("#delete_rule_bts_div").show();
			$("#delete_rule_progress_bar_div").hide();
			$("#delete_close_rule_buttons_div").hide();
			$("#selectedRuleDeleteBtn").hide();
			$("#deleteRuleId").val(selectedRulesCk.toString());
			$("#delete_rule").click();
		}
	}
	
	function getSelectedRuleList(elementId){
		if( document.getElementById("rule_"+elementId).checked === true){
			if(selectedRulesCk.indexOf(elementId) === -1){
				selectedRulesCk.push(elementId);
				document.getElementById("rule_"+elementId).checked === false;
			}
		}else{
			if(selectedRulesCk.indexOf(elementId) !== -1){
				selectedRulesCk.splice(selectedRulesCk.indexOf(elementId), 1);
			}
		}
		$("#deleteRuleId").val(selectedRulesCk.toString());
		
	}
	
	function createRuleGroup(){
		var rulegroupId ='${rulegroup_form_bean.id}';
		
		var idList = $('#policyRuleGrid').jqGrid('getDataIDs');
		var grid = $('#policyRuleGrid');
		var fullData = jQuery("#policyRuleGrid").jqGrid('getRowData');
		
		
		
		 if(fullData == null || fullData == 'null' || fullData == 'undefined' || fullData == '' ){
			
			$("#validationMsg").html('');
			$("#validationMsg").html('<spring:message code="policy.rule.validation.no.select.msg" ></spring:message>');
			$("#validation_msg_popup_lnk").click();
			
			return false;
		 } 
		
		$('#policy-grouprules').val(JSON.stringify(fullData));
		
		if(rulegroupId != '' && rulegroupId != 0){
			$("#rulegroup-id").val(rulegroupId);
			$("#create-rulegroup-form").attr("action","<%=ControllerConstants.UPDATE_RULE_GROUP%>");
			$("#create-rulegroup-form").submit();
		}else{
			$("#create-rulegroup-form").attr("action","<%=ControllerConstants.CREATE_RULE_GROUP%>");
			$("#create-rulegroup-form").submit();
		}
		
	}
	
	function resetField(ruleGroupId) {
		$("#ruleGroupId").val(ruleGroupId);
		$("#update-rulegroup-form2").attr("action","<%=ControllerConstants.INIT_CREATE_RULE_GROUP%>");
		$("#update-rulegroup-form2").submit();
		
	}
	
</script>
</head>
<jsp:include page="../common/newheader.jsp"></jsp:include>
<body>
	<div style="display: none;">
		<form id="update-rulegroup-form2" name="update-rulegroup-form2" method="GET">
			<input type="hidden" id="server-instance-name" name="server-instance-name" value='${instanceName}' /> 
			<input type="hidden" id="server-instance-host" name="server-instance-host" value='${host}' /> 
			<input type="hidden" id="server-instance-port" name="server-instance-port" value='${port}' /> 
			<input type="hidden" id="server-instance-id" name="server-instance-id" value='${instanceId}' />
			
			<input type="hidden" id="serviceInstanceId" name="serviceInstanceId" value="${serviceInstanceId}">
			<input type="hidden" id="serviceId" name="serviceId" value="${serviceId}">
			<input type="hidden" id="serviceType" name="serviceType" value="<%=BaseConstants.PROCESSING_SERVICE%>">
			<input type="hidden" id="serviceName" name="serviceName" value="${serviceName}">
			 
			<input type="hidden" id="ruleGroupId" name="ruleGroupId" /> 
			<input type="hidden" id="ruleGroupName"	name="ruleGroupName" /> 
			<input type="hidden" id="ruleGroupDesc" name="ruleGroupDesc" />
		</form>
	</div>

	<div class="box-body padding0 mtop10">
		<div class="fullwidth">
			<form:form id="create-rulegroup-form" modelAttribute="<%=FormBeanConstants.RULEGROUP_FORM_BEAN %>" method="POST" action="<%=ControllerConstants.CREATE_RULE_GROUP%>">
				<input type="hidden" name="policy-grouprules" id="policy-grouprules">
				<input type="hidden" id="server-instance-name" name="server-instance-name" value='${instanceName}' /> 
				<input type="hidden" id="server-instance-host" name="server-instance-host" value='${host}' /> 
				<input type="hidden" id="server-instance-port" name="server-instance-port" value='${port}' /> 
				<input type="hidden" id="server-instance-id" name="server-instance-id" value='${instanceId}' />
				<input type="hidden" id="serviceInstanceId" name="serviceInstanceId" value="${serviceInstanceId}">
			<input type="hidden" id="serviceId" name="serviceId" value="${serviceId}">
			<input type="hidden" id="serviceType" name="serviceType" value="<%=BaseConstants.PROCESSING_SERVICE%>">
			<input type="hidden" id="serviceName" name="serviceName" value="${serviceName}">
				<form:hidden id="rulegroup-alias" path="alias" ></form:hidden>
				<form:hidden id="rulegroup-id" path="id" ></form:hidden>
				<div class="fullwidth inline-form">
					<div class="col-md-6 no-padding">
						<spring:message code="business.policy.rule.group.grid.column.ruleName" var="tooltip" ></spring:message>
						<div class="form-group">
							<div class="table-cell-label">${tooltip }
								<span class="required">*</span>
							</div>
							<div class="input-group">
								<form:input id="rule-group-name" path="name" class="form-control table-cell input-sm" tabindex="1"
									data-toggle="tooltip" data-placement="bottom" title="${tooltip }" ></form:input>
								<spring:bind path="name">
										<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
								</spring:bind>
							</div>
						</div>
						<spring:message code="business.policy.rule.group.grid.column.description" var="tooltip" ></spring:message>
						<div class="form-group">
							<div class="table-cell-label">${tooltip }</div>
							<div class="input-group ">
								<form:textarea class="form-control input-sm" id="rule-group-description" path="description" tabindex="2"
									data-toggle="tooltip" rows="3" data-placement="bottom" title="${tooltip}"></form:textarea>
								<spring:bind path="description">
										<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
								</spring:bind>
							</div>
						</div>
					</div>
				</div>
			</form:form>
		</div>
	</div>
	
	<%-- Policy rule list grid start here --%>
	<div class="fullwidth">
		<div class="title2">
			<spring:message code="business.policy.rule.group.grid.column.ruleList" ></spring:message>
			<span class="title2rightfield"> 
				<span class="title2rightfield-icon1-text"> 
					<sec:authorize access="hasAuthority('UPDATE_POLICY_GROUP_CONFIGURATION')">
						<a href="#" onclick="selectRulePopUp();">
							<i class="fa fa-plus-circle"></i>
						</a>
						<a href="#" id="addRule" onclick="selectRulePopUp();" tabindex="3"> 
							<spring:message	code="business.policy.rule.grid.column.add" ></spring:message>
						</a>
					</sec:authorize>
				</span> 
				<span class="title2rightfield-icon1-text"> 
					<sec:authorize access="hasAuthority('UPDATE_POLICY_GROUP_CONFIGURATION')">
						<a href="#" onclick="deleteSelectedRules();">
							<i class="fa fa-trash"></i>
						</a>
						<a href="#divDeleteService" class="fancybox" style="display: none;" id="deleteService" tabindex="4">#</a>
						<a href="#" id="deleteRule" onclick="deleteSelectedRules();">
							<spring:message	code="business.policy.rule.grid.column.delete" ></spring:message>
						</a>
						<a href="#divDeleteRule" class="fancybox" style="display: none;" id="delete_rule">#</a>
					</sec:authorize>
				</span>
			</span>
		</div>
	</div>
	
	
	<div class="box-body table-responsive no-padding box">
		<table class="table table-hover" id="policyRuleGrid"></table>
		<div id="policyRuleGridPagingDiv"></div>
		<div class="clearfix"></div> 
	    <div id="divLoading" align="center" style="display: none;"><img src="img/preloaders/Preloader_10.gif" /></div>
	</div>
	<%-- Rule list grid ends --%>
	
	<a href="#divSelectRuleList" class="fancybox" style="display: none;" id="selectRuleList">#</a>
	<div id="divSelectRuleList" style="width: 100%; display: none;">
		<jsp:include page="policyRuleListPopUp.jsp"></jsp:include>
	</div>
	
	<div id="divDeleteRule" style="width: 100%; display: none;">
			<div class="modal-content">
				<div class="modal-header padding10">
					<h4 class="modal-title">
						<spring:message code="policymgmt.rule.delete.popup.header"></spring:message>
					</h4>
				</div>
				<div class="modal-body padding10 inline-form">
					<div id="deletePopUpMsg" class=fullwidth>
						<jsp:include page="../common/responseMsgPopUp.jsp" ></jsp:include>
					</div>
					<div class="fullwidth">
						<input type="hidden" value="" id="deleteRuleId"
							name="deleteRuleId" />
						<div id="delete_selected_rule_details"></div>

					</div>
				</div>
					<div id="delete_rule_bts_div" class="modal-footer padding10">
						<button type="button" class="btn btn-grey btn-xs" id = "selectedRuleDeleteBtn"
							onclick="deleteSelectedRulesById();">
							<spring:message code="btn.label.delete" ></spring:message>
						</button>
						<button type="button" class="btn btn-grey btn-xs" id = "selectedRuleCloseBtn"
							onclick="closeFancyBox();">
							<spring:message code="btn.label.cancel" ></spring:message>
						</button>
					</div>
					<div class="modal-footer padding10"
						id="delete_close_rule_buttons_div" style="display: none;">
						<button type="button" class="btn btn-grey btn-xs "
							onclick="closeFancyBox();">
							<spring:message code="btn.label.close" ></spring:message>
						</button>
					</div>
				
				<div id="delete_rule_progress_bar_div"
					class="modal-footer padding10 text-left" style="display: none;">
					<jsp:include page="../common/processing-bar.jsp"></jsp:include>
				</div>
			</div>
		</div>
	
	<jsp:include page="policyCommonValidaton.jsp"></jsp:include>
	
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
	    </form>
    </div>

</body>
</html>
