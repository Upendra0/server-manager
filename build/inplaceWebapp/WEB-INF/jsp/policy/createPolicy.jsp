<%@page import="com.elitecore.sm.common.constants.BaseConstants"%>
<%@page import="com.elitecore.sm.common.constants.FormBeanConstants"%>
<%@page import="com.elitecore.sm.util.MapCache"%>
<%@page
	import="com.elitecore.sm.common.constants.SystemParametersConstant"%>
<%@page import="org.apache.catalina.connector.Request"%>
<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<%@taglib uri="/WEB-INF/ShowErrorTag.tld" prefix="elitecoreError" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/security/tags"
	prefix="sec"%>
<%@taglib uri="/WEB-INF/ShowErrorTag.tld" prefix="error"%>
<html>
<head>

<!-- Policy validation msg for select rule/condition/group. -->
<jsp:include page="policyCommonValidaton.jsp"></jsp:include>

<script type="text/javascript">
	var selectedGroupsCk = new Array();
	var existingPolicyGroupsIds = new Array();
	function selectGroupPopUp() {
		$("#search_rulegroupName").val('');
		$("#search_rulegroupDescription").val('');
		
		existingPolicyGroupsIds = new Array();
		
		var fullData = jQuery("#ruleGroupgrid").jqGrid('getRowData');
		for(i = 0; i < fullData.length; i++) {
			existingPolicyGroupsIds.push(fullData[i]['id']);
		}
		console.log(existingPolicyGroupsIds);
		
		jQuery('#policy-group-list').jqGrid('clearGridData');
		$('#policy-group-list').trigger('reloadGrid');
		$('#selectRuleGroupList').click();
	}

	$(document).ready(function() {
	var policyId='${policy_form_bean.id}';
		
		if(policyId == '' || policyId == 0){
			datatype='local';
			
		}else{
			datatype='json';
			$("#policy-name").val('${policy_form_bean.name}');
			$("#policy-name").prop("readOnly", true);
			$("#policy-description").val('${policy_form_bean.description}');
		}
		loadJQueryRuleGroupGrid();
		
	});
	
    function policyNameFormatter(cellvalue, options, rowObject){
		
    	return '<a class="link" id="'+rowObject["name"]+'_update_group_lnk" onclick="updateRuleGroup('+"'" + rowObject["id"]+ "','"+rowObject["ruleGroupName"]+"','"+rowObject["description"]+"'" + ');">' + cellvalue + '</a>' ;
	}
	
    function updateRuleGroup(id,name,des){
		$("#ruleGroupId").val(id);
		$("#ruleGroupName").val(name);
		$("#ruleGroupDes").val(des);
		$("#update_rulegroup_form").attr("action","<%=ControllerConstants.INIT_CREATE_RULE_GROUP%>");
		$("#update_rulegroup_form").submit();
	}
    
	function loadJQueryRuleGroupGrid(){
		$("#ruleGroupgrid").jqGrid({
			url:"<%= ControllerConstants.GET_POLICY_GROUP_LIST %>",
			mtype:"GET",
			postData: {
   	    		'policyId': function () {
   	    			return '${policy_form_bean.id}';
   	    		}
   	    		
        	},
			datatype: datatype,
			colNames: [
				          "<spring:message code='business.policy.rule.grid.column.id' ></spring:message>",
				          "<spring:message code='policymgmt.rulegroup.applicationorder'></spring:message>",
						  "<spring:message code='business.policy.rule.group.grid.column.ruleName' ></spring:message>",
			           	  "<spring:message code='business.policy.rule.group.grid.column.description' ></spring:message>",
			           	  "#"
			              
				],
			colModel: [
				{name:'relId',index:'relId',hidden:true},
				{name: 'applicationOrder', index: 'applicationOrder', hidden: true},
				{name:'name',index:'name',sortable:true,formatter:policyNameFormatter, key:true},
				{name:'description',index:'description',sortable:true},
				{name:'id',index:'id',hidden:true},
			],
			rowNum:<%=MapCache.getConfigValueAsInteger(
					SystemParametersConstant.TOTAL_ROWS_TO_DISPLAY_IN_GRID, 10)%>,
			rowList:[5,10,20,60,100],
			height: 'auto',
			sortname: 'applicationOrder',
			sortorder: "asc",
			pager: "#ruleGroupgridPagingDiv",
			viewrecords: true,
			multiselect: true,
			timeout : 120000,
		    loadtext: "Loading...",
			caption: "<spring:message code="policymgmt.policy.policygroup"></spring:message>",
			autowidth:true,
			beforeRequest:function(){
		 		$('#divLoading').dialog({
		             autoOpen: false,
		             width: 90,
		             modal:true,
		             overlay: { opacity: 0.3, background: "white" },
		             resizable: false,
		             height: 125
		         });
		 		selectedGroupsCk = new Array();
		    },
			loadComplete: function(data) {
		 		/* $("#divLoading").dialog('close');
		 			$(".ui-dialog-titlebar").show();
		 			if ($('#grid').getGridParam('records') === 0) {
		             $('#grid tbody').html("<div style='padding:6px;background:#D8D8D8;text-align:center;'><spring:message code="collectionService.summary.agent.grid.empty.records"></spring:message></div>");
		             $("#agentGridPagingDiv").hide();
		         } */
			},
			loadError : function(xhr,st,err) {
				handleGenericError(xhr,st,err);
			},

			recordtext: "<spring:message code="serverManagement.grid.pager.total.records.text"></spring:message>",
		    emptyrecords: "<spring:message code="collectionService.summary.agent.grid.empty.records"></spring:message>",
			loadtext: "<spring:message code="collectionService.summary.agent.grid.loading.text"></spring:message>",
			pgtext : "<spring:message code="collectionService.summary.agent.grid.pager.text"></spring:message>",
			beforeSelectRow: function (rowid, e){
				var $grid = $("#ruleGroupgrid");
				if(document.getElementById('jqg_ruleGroupgrid_'+rowid).checked){
					if(selectedGroupsCk.indexOf(rowid) == -1){
						selectedGroupsCk.push(rowid);
					}
				}else{
					if(selectedGroupsCk.indexOf(rowid) != -1){
						selectedGroupsCk.splice(selectedGroupsCk.indexOf(rowid), 1);
					}
				}
			    return false;
			},
			onSelectAll : function(id, status) {
				if (status == true) {
					selectedGroupsCk = new Array();
					for (i = 0; i < id.length; i++) {
						selectedGroupsCk.push(id[i]);
					}
				} else {
					selectedGroupsCk = new Array();
				}
			}
			}).navGrid("#ruleGroupgridPagingDiv",{edit:false,add:false,del:false,search:false});
			$(".ui-jqgrid-titlebar").hide();
			$(".ui-pg-input").height("10px");

			$("#ruleGroupgrid").sortableRows();   
			$("#ruleGroupgrid").disableSelection();
		    $("#ruleGroupgrid").sortable({
		    	items: 'tr:not(:first)'
		    });
			$("#ruleGroupgrid").bind('sortstop', function(event, ui) { 
				});
			$("#ruleGroupgrid").jqGrid('gridDnD');
			
	}
	
	
	function deleteSelectedGroupsById(){
		if(selectedGroupsCk.length > 0) {
			for (i = 0, n = selectedGroupsCk.length; i < n; i++) {
			    $('#ruleGroupgrid').jqGrid('delRowData',selectedGroupsCk[i]);
			}
		}
		
		existingPolicyGroupsIds = new Array();
		var fullData = jQuery("#ruleGroupgrid").jqGrid('getRowData');
		for(i = 0; i < fullData.length; i++) {
			existingPolicyGroupsIds.push(fullData[i]['id']);
		}
		
		selectedGroupsCk = new Array();
		closeFancyBox();
	}
	
	/* function deleteSelectedGroups() {
		var i, n;
		if(selectedGroupsCk.length > 0) {
			
			if(confirm("Are you sure to remove " + selectedGroupsCk.length + " records?")) {
				for (i = 0, n = selectedGroupsCk.length; i < n; i++) {
					console.log("Row Id: " + selectedGroupsCk[i]);
				    $('#ruleGroupgrid').jqGrid('delRowData',selectedGroupsCk[i]);
				}
			}
		}	
		
		//$('#ruleGroupgrid').trigger('reloadGrid');
	} */
	
	function deleteSelectedGroups() {
		clearAllMessagesPopUp();
		resetWarningDisplayPopUp();
		clearAllMessages();
		if(selectedGroupsCk != null && selectedGroupsCk.length > 0){
			var tableString ="<table class='table table-hover table-bordered'  border='1' >";
			tableString+="<th>#</th><th>Group ID</th><th>Group Name</th>";
		
			for(var i = 0 ; i< selectedGroupsCk.length;i++){
				
				//var	rowData = $('#policyConditionGrid').jqGrid('getRowData', selectedConditionCk[i]);
				var rowData = $('#ruleGroupgrid').jqGrid('getRowData',selectedGroupsCk[i]);
				tableString += "<tr>";
				tableString += "<td><input type='checkbox' name='group_delete' id='group_"+selectedGroupsCk[i]+"' checked  onclick=getSelectedGroupList('"+selectedGroupsCk[i]+"')  value='"+selectedGroupsCk[i]+"' /> </td>";
				tableString += "<td>"+rowData.id+" </td>";
				tableString += "<td>"+rowData.name+"</td>";
				tableString += "</tr>";
			}	
			tableString+="</table>";
				$("#delete_selected_group_details").html(tableString);
				$("#delete_group_bts_div").show();
				$("#delete_group_progress_bar_div").hide();
				$("#delete_close_group_buttons_div").hide();
				$("#selectedGroupDeleteBtn").show();
				$("#deleteGroupId").val(selectedGroupsCk.toString());
				$("#delete_group").click();
			
			//$('#ruleGroupgrid').trigger('reloadGrid');
		}else{
			var tableString ="<table class='table table-hover table-bordered'  border='1' >";
			tableString+="<b><p>No groups selected</p></b></table>";
			$("#delete_selected_group_details").html(tableString);
			$("#delete_group_bts_div").show();
			$("#selectedGroupDeleteBtn").hide();
			$("#delete_group_progress_bar_div").hide();
			$("#delete_close_group_buttons_div").hide();
			$("#deleteGroupId").val(selectedGroupsCk.toString());
			$("#delete_group").click();
		}
	}
	function getSelectedGroupList(elementId){
		if( document.getElementById("group_"+elementId).checked === true){
			if(selectedRulesCk.indexOf(elementId) === -1){
				selectedRulesCk.push(elementId);
				document.getElementById("group_"+elementId).checked === false
			}
		}else{
			if(selectedRulesCk.indexOf(elementId) !== -1){
				selectedRulesCk.splice(selectedRulesCk.indexOf(elementId), 1);
			}
		}
		$("#deleteRuleId").val(selectedRulesCk.toString());
		
	}
	
	function ruleGroupNameFormatter(cellvalue, options, rowObject){
		
		return '<a class="link" onclick="ruleListPopUp('+"'" + rowObject["ruleGroupName"]+ "'" + ');">' + cellvalue + '</a>' ;
	}
	
	function createPolicy(){
		var policyId ='${policy_form_bean.id}';
		
		var policyGroupList = {};
		var idList = $('#ruleGroupgrid').jqGrid('getDataIDs');
		var grid = $('#ruleGroupgrid');
		var fullData = jQuery("#ruleGroupgrid").jqGrid('getRowData');
		
		
		if(fullData == null || fullData == 'null' || fullData == 'undefined' || fullData == '' ){
			
			$("#validationMsg").html('');
			$("#validationMsg").html('<spring:message code="policy.rule.validation.no.select.msg" ></spring:message>');
			$("#validation_msg_popup_lnk").click();
			
			return false;
		 }
		
		
		
		$('#policy-groups').val(JSON.stringify(fullData));
		
		if(policyId != '' && policyId != 0){
			$("#policy-id").val(policyId);
			$("#create-policy-form").attr("action","<%=ControllerConstants.UPDATE_POLICY%>");
			$("#create-policy-form").submit();
		}else{
			$("#create-policy-form").attr("action","<%=ControllerConstants.CREATE_POLICY%>");
			$("#create-policy-form").submit();
		}
	}
	
	function resetField(policyId){
		$("#policy-id").val(policyId);
		$("#update-policy-form2").attr("action","<%=ControllerConstants.INIT_CREATE_POLICY%>");
		$("#update-policy-form2").submit();
		
	}
	
	
	function ruleListPopUp(ruleGroupName){
		clearAllMessages();
		$("#divRuleGroupName #lblRuleGroupName").text(ruleGroupName);
		$("#ruleList").click();
		
	}
</script>
</head>
<jsp:include page="../common/newheader.jsp"></jsp:include>
<body>
<div style="display: none;">
	<form id="update-policy-form2" name="update-policy-form2" method="POST">
		<input type="hidden" id="server-instance-name" name="server-instance-name" value='${instanceName}' /> 
		<input type="hidden" id="server-instance-host" name="server-instance-host" value='${host}' /> 
		<input type="hidden" id="server-instance-port" name="server-instance-port" value='${port}' /> 
		<input type="hidden" id="server-instance-id" name="server-instance-id" value='${instanceId}' />
		<input type="hidden" id="serviceInstanceId" name="serviceInstanceId" value="${serviceInstanceId}">
		<input type="hidden" id="serviceId" name="serviceId" value="${serviceId}"> 
		<input type="hidden" id="serviceType" name="serviceType" value="<%=BaseConstants.PROCESSING_SERVICE%>">
		<input type="hidden" id="serviceName" name="serviceName" value="${serviceName}">
		 <input type="hidden" id="serviceInstanceId" name="serviceInstanceId" value="${serviceInstanceId}">
		<input type="hidden" id="policy-id" name="policy-id" /> 
		<input type="hidden" id="policy-name" name="policy-name" /> 
		<input type="hidden" id="policy-description" name="policy-description" />
	</form>
</div>


	<div class="box-body padding0 mtop10">
		<div class="fullwidth">
			<form:form id="create-policy-form" modelAttribute="<%=FormBeanConstants.POLICY_FORM_BEAN %>" method="POST" action="<%=ControllerConstants.CREATE_POLICY%>">
				<input type="hidden" name="policy-groups" id="policy-groups">
				<input type="hidden" id="server-instance-name" name="server-instance-name" value='${instanceName}' />
				<input type="hidden" id="server-instance-host" name="server-instance-host" value='${host}' /> 
				<input type="hidden" id="server-instance-port" name="server-instance-port" value='${port}' /> 
				<input type="hidden" id="server-instance-id" name="server-instance-id" value='${instanceId}' />
				<input type="hidden" name="serviceId" value="${serviceId}"> 
				<input type="hidden" name="serviceType" value="<%=BaseConstants.PROCESSING_SERVICE%>">
				<input type="hidden" name="serviceName" value="${serviceName}">
				<input type="hidden" name="serviceInstanceId" value="${serviceInstanceId}">
				<form:hidden id="policy-alias" path="alias" ></form:hidden>
				<form:hidden id="policy-id" path="id" ></form:hidden>
				<div class="fullwidth inline-form">
					<div class="col-md-6 no-padding">
						<spring:message code="business.policy.grid.column.policyName" var="tooltip" ></spring:message>
						<div class="form-group">
							<div class="table-cell-label">${tooltip }
								<span class="required">*</span>
							</div>
							<div class="input-group">
								<form:input id="policy-name" path="name" class="form-control table-cell input-sm" tabindex="1"
									data-toggle="tooltip" data-placement="bottom" title="${tooltip }" ></form:input>
								<spring:bind path="name">
										<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
								</spring:bind>
							</div>
						</div>
						<spring:message code="business.policy.rule.grid.column.description" var="tooltip" ></spring:message>
						<div class="form-group">
							<div class="table-cell-label">${tooltip }</div>
							<div class="input-group ">
								<form:textarea class="form-control input-sm" id="rule-description" path="description" tabindex="2"
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
	
	<%-- Rule Group list grid starts --%>
	<div class="fullwidth">
		<div class="title2">
			<spring:message code="business.policy.grid.column.ruleGroupList" ></spring:message>
			<span class="title2rightfield"> 
				<span class="title2rightfield-icon1-text"> 
					<sec:authorize access="hasAuthority('UPDATE_POLICY_CONFIGURATION')">
						<a href="#" onclick="selectGroupPopUp();">
							<i class="fa fa-plus-circle"></i>
						</a>
						<a href="#" id="addRule" onclick="selectGroupPopUp();" tabindex="3"> 
							<spring:message	code="business.policy.rule.grid.column.add" ></spring:message>
						</a>
					</sec:authorize>
				</span> 
				<span class="title2rightfield-icon1-text"> 
					<sec:authorize access="hasAuthority('UPDATE_POLICY_CONFIGURATION')">
						<a href="#" onclick="deleteSelectedGroups();">
							<i class="fa fa-trash"></i>
						</a>
						<a href="#divDeleteGroup" class="fancybox" style="display: none;" id="delete_group" tabindex="4">#</a>
						<a href="#" id="deleteGroup" onclick="deleteSelectedGroups();">
							<spring:message	code="business.policy.rule.grid.column.delete" ></spring:message>
						</a>
					</sec:authorize>
				</span>
			</span>
		</div>
	</div>
	<div class="box-body table-responsive no-padding box">
		<table class="table table-hover" id="ruleGroupgrid"></table>
		<div id="ruleGroupgridPagingDiv"></div>
		<div class="clearfix"></div> 
	    <div id="divLoading" align="center" style="display: none;"><img src="img/preloaders/Preloader_10.gif" /></div>
	</div>
	<%-- Rule Group list grid ends --%>

	<a href="#divSelectRuleGroupList" class="fancybox" style="display: none;" id="selectRuleGroupList">#</a>
	<div id="divSelectRuleGroupList" style="width: 100%; display: none;">
		<jsp:include page="selectRuleGroupPopUp.jsp"></jsp:include>
	</div>

	<a href="#divRuleList" class="fancybox" style="display: none;"
		id="ruleList">#</a>
	<div id="divRuleList" style="width: 100%; display: none;">
		<jsp:include page="policyRuleListPopUp.jsp"></jsp:include>
	</div>
<%-- <jsp:include page="../common/newfooter.jsp"></jsp:include> --%>

	<div id="divDeleteGroup" style="width: 100%; display: none;">
			<div class="modal-content">
				<div class="modal-header padding10">
					<h4 class="modal-title">
						<spring:message code="policymgmt.rulegroup.delete.popup.header"></spring:message>
					</h4>
				</div>
				<div class="modal-body padding10 inline-form">
					<div id="deletePopUpMsg" class=fullwidth>
						<jsp:include page="../common/responseMsgPopUp.jsp" ></jsp:include>
					</div>
					<div class="fullwidth">
						<input type="hidden" value="" id="deleteGroupId"
							name="deleteGroupId" />
						<div id="delete_selected_group_details"></div>

					</div>
				</div>
					<div id="delete_group_bts_div" class="modal-footer padding10">
						<button type="button" class="btn btn-grey btn-xs" id = "selectedGroupDeleteBtn"
							onclick="deleteSelectedGroupsById();">
							<spring:message code="btn.label.delete" ></spring:message>
						</button>
						<button type="button" class="btn btn-grey btn-xs" id = "selectedGroupCloseBtn"
							onclick="closeFancyBox();">
							<spring:message code="btn.label.cancel" ></spring:message>
						</button>
					</div>
					<div class="modal-footer padding10"
						id="delete_close_group_buttons_div" style="display: none;">
						<button type="button" class="btn btn-grey btn-xs "
							onclick="closeFancyBox();">
							<spring:message code="btn.label.close" ></spring:message>
						</button>
					</div>
				
				<div id="delete_group_progress_bar_div"
					class="modal-footer padding10 text-left" style="display: none;">
					<jsp:include page="../common/processing-bar.jsp"></jsp:include>
				</div>
			</div>
		</div>


        <div style="display: none;">
	    	<form id="update_rulegroup_form" method="GET">
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
		
</body>

</html>
