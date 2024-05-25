<%@page import="com.elitecore.sm.util.MapCache"%>
<%@page
	import="com.elitecore.sm.common.constants.SystemParametersConstant"%>
<%@page import="org.apache.catalina.connector.Request"%>
<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<%@ taglib uri="http://www.springframework.org/security/tags"
	prefix="sec"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="/WEB-INF/ShowErrorTag.tld" prefix="elitecoreError"%>
<%@page import="com.elitecore.sm.common.constants.BaseConstants"%>
<div class="fullwidth mtop10">
	<div class="fullwidth borbot tab-pane active">
		<div class="fullwidth">
			<div class="title2">
				<spring:message code="policymgmt.policy.search.title" ></spring:message>
			</div>
		</div>
		<div class="col-md-6 inline-form" style="padding-left: 0px !important;">
			<div class="form-group">
				<div class="table-cell-label">
					<spring:message code="policymgmt.policy.name" ></spring:message>
				</div>
				<div class="input-group">
					<spring:message code="policymgmt.policy.name" var="tooltip" ></spring:message>
					<input type="text" id="search-policy-name" class="form-control table-cell input-sm" data-toggle="tooltip" tabindex="1"
					data-placement="bottom" title="${tooltip}"> 
						<span class="input-group-addon add-on last"> 
							<i class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="bottom"></i>
						</span>
				</div>
			</div>
			<div class="form-group">
				<spring:message code="policymgmt.policy.associationstatus" var="tooltip" ></spring:message>
				<div class="table-cell-label">${tooltip}</div>
				<div class="input-group ">
					<select id="search-policy-association-status" class="form-control" title="${tooltip}" data-toggle="tooltip" tabindex="3"
					data-placement="bottom">
						<option value="ALL" selected="selected">
							<spring:message code="policymgmt.policy.associationstatus.all" ></spring:message>
						</option>
						<option value="Associated">
							<spring:message code="policymgmt.policy.associationstatus.associated" ></spring:message>
						</option>
						<option value="Non-Associated">
							<spring:message code="policymgmt.policy.associationstatus.nonassociated" ></spring:message>
						</option>
					</select> 
					<span class="input-group-addon add-on last"> 
						<i class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="bottom"></i>
					</span>
				</div>
			</div>
		</div>

		<div class="col-md-6 inline-form">
			<div class="form-group">
				<spring:message code="policymgmt.policy.description" var="tooltip" ></spring:message>
				<div class="table-cell-label">${tooltip}</div>
				<div class="input-group ">
					<input type="text" id="search-policy-desc" class="form-control table-cell input-sm" data-toggle="tooltip" tabindex="2"
					data-placement="bottom" title="${tooltip}"> 
						<span class="input-group-addon add-on last"> 
							<i class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="bottom"></i>
						</span>
				</div>
			</div>

		</div>	

		<div class="clearfix"></div>
		<div class="pbottom15 mtop10">
			<button class="btn btn-grey btn-xs" onclick="reloadPolicyGridData();" tabindex="5" id="search_btn">
				<spring:message code="btn.label.search" ></spring:message>
			</button>
			<button class="btn btn-grey btn-xs"	onclick="resetSearchPolicyCriteria();" tabindex="6" id="reset_btn">
				<spring:message code="btn.label.reset" ></spring:message>
			</button>
		</div>
	</div>
	
	<!-- Policy Grid start -->
	<div class="fullwidth">
		<div class="title2">
			<spring:message code="business.policy.grid.heading" ></spring:message>
			<span class="title2rightfield"> 
				<span class="title2rightfield-icon1-text"> 
					<sec:authorize access="hasAuthority('CREATE_POLICY_CONFIGURATION')">
						<a href="#" onclick="createPolicy();">
							<i class="fa fa-plus-circle"></i>
						</a>
						<a href="#" id="create-policy" onclick="createPolicy();"> 
							<spring:message code="business.policy.create.label" ></spring:message>
						</a>
					</sec:authorize>
				</span>
			</span>
		</div>
	</div>

	<div class="box-body table-responsive no-padding box">
		<table class="table table-hover" id="policy-grid"></table>
		<div id="policyGridPagingDiv"></div>
		<div class="clearfix"></div> 
	    <div id="divLoading" align="center" style="display: none;"><img src="img/preloaders/Preloader_10.gif" /></div>
	</div>

</div>



<div style="display: none;">
	<form id="update-policy-form" name="update-policy-form" method="POST">
		<input type="hidden" id="server-instance-name" name="server-instance-name" value='${instanceName}' /> 
		<input type="hidden" id="server-instance-host" name="server-instance-host" value='${host}' /> 
		<input type="hidden" id="server-instance-port" name="server-instance-port" value='${port}' /> 
		<input type="hidden" id="server-instance-id" name="server-instance-id" value='${instanceId}' />
		
		<input type="hidden" id="serviceId" name="serviceId" value="${serviceId}"> 
		<input type="hidden" id="serviceType" name="serviceType" value="<%=BaseConstants.PROCESSING_SERVICE%>">
		<input type="hidden" id="serviceName" name="serviceName" value="${serviceName}">
		 <input type="hidden" id="serviceInstanceId" name="serviceInstanceId" value="${serviceInstanceId}">
		<input type="hidden" id="policy-id" name="policy-id" /> 
		<input type="hidden" id="policy-name" name="policy-name" /> 
		<input type="hidden" id="policy-description" name="policy-description" />
	</form>
</div>

<a href="#divDeletePolicy" class="fancybox" style="display: none;" id="deletePolicyPopup">#</a>
  <div id="divDeletePolicy" style=" width:100%; display: none;" >
  <jsp:include page="deletePolicyPopUp.jsp"></jsp:include>
</div>

<a href="#divGroupRules" class="fancybox" style="display: none;" id="viewGroupRulesPopup">#</a>
 	<div id="divGroupRules" style=" width:100%; display: none;" >
 		<jsp:include page="ruleGroupListPopUp.jsp"></jsp:include>
	</div>
	
<a href="#divImportPolicyConfig" class="fancybox" style="display: none;" id="importPolicyConfig">#</a>
<a href="#divMessage" class="fancybox" style="display: none;" id="message">#</a>
<div id="divImportPolicyConfig" style=" width:100%; display: none;" >
	<jsp:include page="ImportPolicyPopup.jsp"></jsp:include>
</div>

<div id="divMessage" style=" width:100%; display: none;" >
    <div class="modal-content">
        <div class="modal-header padding10">
            <h4 class="modal-title"><spring:message code="serverManagement.warn.popup.header"></spring:message></h4>
        </div>
        <div class="modal-body padding10 inline-form">
	        <p>
	        	<%-- <spring:message code="serverMgmt.no.instance.checked.warning"></spring:message>   --%>
	        	Please select policy by checking radio button  
	        </p>
        </div>
        <div class="modal-footer padding10">
            <button id="no_selection_delete_close_btn" type="button" class="btn btn-grey btn-xs " data-dismiss="modal" onclick="closeFancyBox();"><spring:message code="btn.label.close"></spring:message></button>
        </div>
    </div>
    <!-- /.modal-content --> 
</div>

<div id="divPolicyExportConfig" style="width: 100%; display: none;">
	<div class="modal-content">
		<form method="POST"	action="<%=ControllerConstants.EXPORT_POLICY_CONFIG%>" id="export-policy-config-form">
			<input type="hidden" id="exportPolicyId" name="exportPolicyId" />
			<input type="hidden" id="serverInstanceId" name="serverInstanceId" />
			<input type="hidden" id="isExportForDelete" name="isExportForDelete" value="false" /> 
			<input type="hidden" id="exportPath" name="exportPath" /> 
			<input type="hidden" id="<%=BaseConstants.REQUEST_ACTION_TYPE%>"
					name="<%=BaseConstants.REQUEST_ACTION_TYPE%>"
					value="<%=BaseConstants.POLICY %>" />
		</form>
	</div>
	<!-- /.modal-content -->
</div>


<script type="text/javascript">

	var ckIntanceSelected = new Array();
	var jsLocaleMsg = {};
	jsLocaleMsg.description = "<spring:message code='policymgmt.policy.description'></spring:message>";
	jsLocaleMsg.policyName = "<spring:message code='policymgmt.policy.name'></spring:message>";
	var j=0;

	$(document).ready(function() {
		loadJQueryPolicyGrid();
		j = 0;
	});

	function reloadPolicyGridData(){

		$("#import-add-btn").hide();
		$("#import-overwrite-btn").show();
		$("#import-update-btn").hide();
		$("#import-keepboth-btn").hide();
		$('#import-popup-close-btn').show();
		$('#import-config-progress-bar-div').hide();
		var $grid = $("#policy-grid");
		jQuery('#policy-grid').jqGrid('clearGridData');
		clearPolicyGrid();
		$grid.jqGrid('setGridParam',{datatype:'json',page: 1,sortname: 'id',sortorder: "desc", postData:{
			policyName: function () {
   	        	return $("#search-policy-name").val();
    		},
    		description: function () {
   	        	return $("#search-policy-desc").val();
    		},
    		associationStatus: function(){
    			return $("#search-policy-association-status").val();
    		},
    		serverInstanceId: function () {
    			return '${instanceId}';
    		}		   
		   
		}}).trigger('reloadGrid');
		} 

function loadJQueryPolicyGrid(){
	$("#policy-grid").jqGrid({
		url: "<%= ControllerConstants.GET_POLICY_LIST%>",
		postData: {
			policyName: function () {
   	        	return $("#search-policy-name").val();
    		},
    		description: function () {
   	        	return $("#search-policy-desc").val();
    		},
    		associationStatus: function(){
    			return $("#search-policy-association-status").val();
    		},
    		serverInstanceId: function () {
    			return '${instanceId}';
    		}
    	},
		datatype: "json",
		colNames: [
					  "#",
					  "#",
					  "<spring:message code='business.policy.grid.column.policyName' ></spring:message>",
		           	  "<spring:message code='business.policy.grid.column.description' ></spring:message>",
		              "<spring:message code='business.policy.grid.column.ruleGroupList' ></spring:message>",
		              "<spring:message code='business.policy.grid.column.delete' ></spring:message>"
			],
		colModel: [
			{name:'id',index:'id',hidden:true},
			{name:'checkbox', index: 'checkbox',sortable:false, formatter:policyCheckboxFormatter, align : 'center', width:'30%'},
			{name:'policyName',index:'name',sortable:true,formatter:policyNameFormatter},
			{name:'description',index:'description',sortable:true},
			{name:'groups',index:'groups',align:'center',sortable:true,formatter:groupListFormatter},
			{name:'associationStatus',index:'associationStatus',sortable:true, align:'center', formatter: policyDeleteColumnFormatter}
		],
		rowNum:<%= MapCache.getConfigValueAsInteger(SystemParametersConstant.TOTAL_ROWS_TO_DISPLAY_IN_GRID, 10) %>,
		rowList:[5,10,20,50,100],
		height: 'auto',
		sortname: 'id',
		sortorder: "desc",
		pager: "#policyGridPagingDiv",
		viewrecords: true,
		multiselect: false,
		timeout : 120000,
	    loadtext: "Loading...",
		caption: "<spring:message code="collectionService.summary.agent.grid.caption"></spring:message>",
		beforeRequest:function(){
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
 			if ($('#policy-grid').getGridParam('records') === 0) {
             $('#policy-grid tbody').html("<div style='padding:6px;background:#D8D8D8;text-align:center;'><spring:message code='serverManagement.grid.empty.records'></spring:message></div>");
             $("#policyGridPagingDiv").hide();
        	}else{
            	$("#policyGridPagingDiv").show();
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
		}).navGrid("#policyGridPagingDiv",{edit:false,add:false,del:false,search:false});
			$(".ui-jqgrid-titlebar").hide();
			$(".ui-pg-input").height("10px");
	}
	
	/*
	 * checkbox formatter for all child checkbox
	 */
	function policyCheckboxFormatter(cellvalue, options, rowObject) {
		var uniqueId = rowObject["policyName"]+"_radio";
		return '<input type="radio" id="'+uniqueId+'" name="policyRadio" value="'+rowObject["id"]+'"/>';
	}
	
	var k=0;
	function policysrNoFormatter(cellvalue, options, rowObject){
		
		k=k+1;
		return k;
	}
	
	function policyNameFormatter(cellvalue, options, rowObject){
		
		return '<a class="link" id="'+rowObject["policyName"]+'_update_policy_lnk" onclick="updatePolicy('+"'" + rowObject["id"]+ "','"+rowObject["policyName"]+"','"+rowObject["description"]+"'" + ');">' + cellvalue + '</a>' ;
	}
	
	function groupListFormatter(cellvalue, options, rowObject) {
		return '<a href="#" onclick="groupListPopUp('+"'" + rowObject["id"]+ "'"+');"><i class="fa fa-fw fa-vimeo-square"></i></a>';
		
	}
	
	function groupListPopUp(id){
		clearAllMessages();
		$("#view_group_list_delete-buttons-div").show();
		loadJQueryRuleGroupGrid(id);
		$('#viewGroupRulesPopup').click();
	}
	
	function deleteImageFormatter(cellvalue, options, rowObject){
		
		var toggleId = rowObject["id"];
		var servInstanceId = rowObject["servInstanceId"];
		var service_name = rowObject["name"];
		var tag_name = 'server_instance';
		
		return '<i class="fa fa-trash"  onclick="deletePopup('+"'" + tag_name + "','"+toggleId+"','"+servInstanceId+"','"+service_name+"'" + ')" style="cursor: pointer; cursor: hand;"></i>';
	}
	
	function policyDeleteColumnFormatter(cellvalue, options, rowObject) {
		
		if(cellvalue == 'Associated') {
			return '<p>' + cellvalue + '</p>';
		} else {
			return '<a href="#" id="'+rowObject["policyName"]+'_delete_lnk" onclick="deletePolicyPopup('+"'"+rowObject["id"]+"'"+','+"'"+rowObject["policyName"]+"','"+rowObject["description"]+"'"+')"><i class="fa fa-trash"></i></a>';			
		}
	}
	
	function ruleGroupListFormatter(cellvalue, options, rowObject){
		return '<a href="#" onclick="policyListPopUp('+"'" + rowObject["policyName"]+ "'"+');"><i class="fa fa-fw fa-vimeo-square"></i></a>';
	}
	
	function policyListPopUp(policyName){
		clearAllMessages();
		$("#divPolicyName #lblPolicyName").text(policyName);
		$("#policyList").click();
	}
	
	function createPolicy(){
		$("#update-policy-form").attr("action","<%=ControllerConstants.INIT_CREATE_POLICY%>");
		$("#update-policy-form").submit();
	}
	
	function updatePolicy(id,name,des){
		$("#policy-id").val(id);
		$("#policy-name").val(name);
		$("#policy-description").val(des);
		$("#update-policy-form").attr("action","<%=ControllerConstants.INIT_CREATE_POLICY%>");
		$("#update-policy-form").submit();
	}
	
	function searchPolicy(){
		// clearResponseMsgDiv();
		clearPolicyGrid();
		var $grid = $("#policy-grid");
		$grid.jqGrid('setGridParam',{datatype:'json',page: 1}).trigger('reloadGrid');
	}

	function resetSearchPolicyCriteria(){
		$("#search-policy-name").val('');
		$("#search-policy-desc").val('');
		$("#search-policy-association-status").val('ALL');
		$("#search-policy-status").val('ALL');
		searchPolicy();
	}
	
	function clearPolicyGrid(){
		var $grid = $("#policy-grid");
		var rowIds = $grid.jqGrid('getDataIDs');
		for(var i=0,len=rowIds.length;i<len;i++){
			var currRow = rowIds[i];
			$grid.jqGrid('delRowData', currRow);
		}
	}
	
	function policySrNoFormatter(cellvalue, options, rowObject){
		j=j+1;
		return j;
	}
	
	function deletePolicyPopup(id,name,desc){
		clearAllMessages();

		$("#divDeletePolicyMsg").html('');
		$("#btnDeletePolicyCancel").show();
		$("#btnDeletePolicyPopup").show();
		if(desc =="null"){
			desc = " ";
		}
		$("#tblDeletePolicyList").html('');
		parent.ckIntanceSelected[0]=id;
		tableString ='<table class="table table-hover" style="width:100%">';
		tableString += "<tr>";
		tableString += "<th>"+jsLocaleMsg.policyName+"</th>";
		tableString += "<th>"+jsLocaleMsg.description +"</th>";
		tableString += "</tr>";
		tableString += "<tr>";
		tableString += "<td>"+name+"</td>";
		tableString += "<td>"+desc+"</td>";
		tableString += "</tr>";
		tableString += "</table>"
		$("#tblDeletePolicyList").html(tableString);
		$('#deletePolicyPopup').click();
	}
	
	function deletePolicyInstance(actionUrl){
		
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
				'policyId': parent.ckIntanceSelected[0],
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
				    $('#policy-grid').trigger('reloadGrid');
				    $("#btnDeletePolicyCancel").hide();
				   
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
			    $("#btnDeletePolicyCancel").show();
			}

		});
	} 

</script>


<style>
.ui-jqgrid-btable .btn {
	font-size: 10px;
}
</style>
