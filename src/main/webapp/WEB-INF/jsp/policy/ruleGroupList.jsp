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
				<spring:message code="policymgmt.rulegroup.search.title" ></spring:message>
			</div>
		</div>


		<div class="col-md-6 inline-form"
			style="padding-left: 0px !important;">
			<div class="form-group">
				<div class="table-cell-label">
					<spring:message code="policymgmt.rulegroup.name" ></spring:message>
				</div>
				<div class="input-group">
					<spring:message code="policymgmt.rulegroup.name" var="tooltip" ></spring:message>
					<input type="text" id="search-rulegroup-name"
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
					<select id="search-rulegroup-association-status" class="form-control"
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
		</div>

		<div class="col-md-6 inline-form">
			<div class="form-group">
				<spring:message code="policymgmt.rulegroup.description" var="tooltip" ></spring:message>
				<div class="table-cell-label">${tooltip}</div>
				<div class="input-group ">
					<input type="text" id="search-rulegroup-desc"
						class="form-control table-cell input-sm" data-toggle="tooltip"
						tabindex="2" data-placement="bottom" title="${tooltip}"> <span
						class="input-group-addon add-on last"> <i
						class="glyphicon glyphicon-alert" data-toggle="tooltip"
						data-placement="bottom"></i>
					</span>
				</div>
			</div>

		</div>

		<div class="clearfix"></div>
		<div class="pbottom15 mtop10">
			<button class="btn btn-grey btn-xs" onclick="reloadRuleGroupGridData();"
				tabindex="5" id="search_btn">
				<spring:message code="btn.label.search" ></spring:message>
			</button>
			<button class="btn btn-grey btn-xs"
				onclick="resetSearchRuleGroupCriteria();" tabindex="6" id="reset_btn"> 
				<spring:message code="btn.label.reset" ></spring:message>
			</button>
		</div>
	</div>
	<div class="fullwidth">
		<div class="title2">
			<spring:message code="business.policy.rule.group.grid.heading" ></spring:message>
			<span class="title2rightfield"> <span
				class="title2rightfield-icon1-text"> <sec:authorize
						access="hasAuthority('CREATE_POLICY_GROUP_CONFIGURATION')">
						<a href="#" onclick="createRuleGroup();"><i
							class="fa fa-plus-circle"></i></a>

						<a href="#" id="createRuleGroup" onclick="createRuleGroup();">
							<spring:message code="business.policy.rule.group.create.label" ></spring:message>
						</a>

					</sec:authorize>
			</span>
			</span>
		</div>


	</div>

	<div class="box-body table-responsive no-padding box">
		<table class="table table-hover" id="ruleGroupGrid"></table>
		<div id="ruleGroupGridPagingDiv"></div>
		<div class="clearfix"></div> 
	    <div id="divLoading" align="center" style="display: none;"><img src="img/preloaders/Preloader_10.gif" /></div>
	</div>

	<!-- Jquery Policy Rule Group Grid end here -->


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

<a href="#divGroupRules" class="fancybox" style="display: none;" id="viewGroupRulesPopup">#</a>
 	<div id="divGroupRules" style=" width:100%; display: none;" >
 		<jsp:include page="viewGroupRulesPopUp.jsp"></jsp:include>
</div>

<a href="#divDeleteGroup" class="fancybox" style="display: none;" id="deleteRuleGroupPopUp">#</a>
 	<div id="divDeleteGroup" style=" width:100%; display: none;" >
 		<jsp:include page="deleteRuleGroupPopUp.jsp"></jsp:include>
</div>

<script type="text/javascript">
var ckIntanceSelected = new Array();
	var msg = {};
	msg.groupName = "<spring:message code='policymgmt.rulegroup.name'></spring:message>";
	msg.description = "<spring:message code='policymgmt.rulegroup.description'></spring:message>";

	$(document).ready(function() {
		loadJQueryRuleGroupGrid();
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

		
	});

	function loadJQueryRuleGroupGrid(){
		$("#ruleGroupGrid").jqGrid({
			url: "<%= ControllerConstants.GET_RULE_GROUP_LIST%>",
			postData : {
				ruleGroupName: function () {
	   	        	return $("#search-rulegroup-name").val();
	    		},
	    		description: function () {
	   	        	return $("#search-rulegroup-desc").val();
	    		},
	    		associationStatus: function(){
	    			return $("#search-rulegroup-association-status").val();
	    		},
	    		serverInstanceId: function () {
	    			return '${instanceId}';
	    		}
			},
			datatype: "json",
			colNames: [
						  "#",
						  "<spring:message code='business.policy.rule.group.grid.column.ruleName' ></spring:message>",
			           	  "<spring:message code='business.policy.rule.group.grid.column.description' ></spring:message>",
			              "<spring:message code='business.policy.rule.group.list.rule.heading' ></spring:message>",
			              "<spring:message code='business.policy.rule.group.grid.column.delete' ></spring:message>"
				],
			colModel: [
				{name:'id',index:'id',hidden:true},
				{name:'name',index:'name',key:true, sortable:true,formatter:ruleGroupNameFormatter},
				{name:'description',index:'description',sortable:true},
				{name:'ruleList',index:'ruleList',align:'center',sortable:true,formatter:ruleListFormatter},
				{name:'associationStatus',index:'associationStatus',sortable:true, align:'center', formatter:groupDeleteColumnFormatter}
			],
			rowNum:<%= MapCache.getConfigValueAsInteger(SystemParametersConstant.TOTAL_ROWS_TO_DISPLAY_IN_GRID, 10) %>,
			rowList:[5,10,20,50,100],
			height: 'auto',
			sortname: 'id',
			sortorder: "desc",
			pager: "#ruleGroupGridPagingDiv",
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
	 			if ($('#ruleGroupGrid').getGridParam('records') === 0) {
	             $('#ruleGroupGrid tbody').html("<div style='padding:6px;background:#D8D8D8;text-align:center;'><spring:message code='serverManagement.grid.empty.records'></spring:message></div>");
	             $("#ruleGroupGridPagingDiv").hide();
	        	}else{
	            	$("#ruleGroupGridPagingDiv").show();
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
			}).navGrid("#ruleGridPagingDiv",{edit:false,add:false,del:false,search:false});
				$(".ui-jqgrid-titlebar").hide();
				$(".ui-pg-input").height("10px");
	}
	
	var j=0;
	function ruleGroupsrNoFormatter(cellvalue, options, rowObject){
		
		j=j+1;
		return j;
	}
	
	function ruleGroupNameFormatter(cellvalue, options, rowObject){
		
		return '<a class="link" id="'+rowObject["name"]+'_update_group_lnk" onclick="updateRuleGroup('+"'" + rowObject["id"]+ "','"+rowObject["ruleGroupName"]+"','"+rowObject["description"]+"'" + ');">' + cellvalue + '</a>' ;
	}
	
	
	function createRuleGroup(){
		var serviceId='${serviceId}';
		if(serviceId == ''){
			$("#server_instance_detail_form").attr("action","<%=ControllerConstants.INIT_CREATE_RULE_GROUP%>");
			$("#server_instance_detail_form").submit();
		}else{
			$("#service_detail_form").attr("action","<%=ControllerConstants.INIT_CREATE_RULE_GROUP%>");
			$("#service_detail_form").submit();
		}
	}
	
	function updateRuleGroup(id,name,des){
		$("#ruleGroupId").val(id);
		$("#ruleGroupName").val(name);
		$("#ruleGroupDes").val(des);
		$("#update_rulegroup_form").attr("action","<%=ControllerConstants.INIT_CREATE_RULE_GROUP%>");
		$("#update_rulegroup_form").submit();
	}
	
	function reloadRuleGroupGridData() {


		var $grid = $("#ruleGroupGrid");
		jQuery('#ruleGroupGrid').jqGrid('clearGridData');
		$grid.jqGrid('setGridParam',{datatype:'json',page: 1,sortname: 'id',sortorder: "desc", postData:{
			ruleGroupName: function () {
   	        	return $("#search-rulegroup-name").val();
    		},
    		description: function () {
   	        	return $("#search-rulegroup-desc").val();
    		},
    		associationStatus: function(){
    			return $("#search-rulegroup-association-status").val();
    		},
    		serverInstanceId: function () {
    			return '${instanceId}';
    		}		   
		   
		}}).trigger('reloadGrid');
	} 
	
	function resetSearchRuleGroupCriteria() {
		$("#search-rulegroup-name").val('');
		$("#search-rulegroup-desc").val('');
		$("#search-rulegroup-association-status").val('ALL');
		searchRuleGroup();
	}
	
	function searchRuleGroup() {
		// clearResponseMsgDiv();
		clearRuleGroupGrid();
		var $grid = $("#ruleGroupGrid");
		$grid.jqGrid('setGridParam',{datatype:'json',page: 1}).trigger('reloadGrid');
	}
	
	function clearRuleGroupGrid() {
		var $grid = $("#ruleGroupGrid");
		var rowIds = $grid.jqGrid('getDataIDs');
		for(var i=0,len=rowIds.length;i<len;i++){
			var currRow = rowIds[i];
			$grid.jqGrid('delRowData', currRow);
		}
	}
	
	function ruleListFormatter(cellvalue, options, rowObject) {
		return '<a href="#" onclick="ruleListPopUp('+"'" + rowObject["id"]+ "'"+');"><i class="fa fa-fw fa-vimeo-square"></i></a>';
		
	}
		
	function groupDeleteColumnFormatter(cellvalue, options, rowObject) {
		
		if(cellvalue == 'Associated') {
			return '<p>' + cellvalue + '</p>';
		} else {
			return '<a href="#" id="'+rowObject["name"]+'_delete_lnk" onclick="deleteRuleGroupPopup('+"'"+rowObject["id"]+"'"+','+"'"+rowObject["name"]+"','"+rowObject["description"]+"'"+')"><i class="fa fa-trash"></i></a>';			
		}
	}
	
	function deleteRuleGroupPopup(id,name,desc){
		clearAllMessages();
		
		$("#divDeleteRuleGroupMsg").html('')
		$("#btnDeleteRuleGroupPopup").show();
		$("#btnDeleteRuleGroupCancel").show();
		$("#tblDeleteRuleGroupList").html('');
		if(desc == "null"){
			desc = " ";
		}
		parent.ckIntanceSelected[0]=id;
		tableString ='<table class="table table-hover" style="width:100%">';
		tableString += "<tr>";
 		tableString += "<th>"+msg.groupName+"</th>";
 		tableString += "<th>"+msg.description +"</th>";
		tableString += "</tr>";
		tableString += "<tr>";
		tableString += "<td>"+name+"</td>";
		tableString += "<td>"+desc+"</td>";
		tableString += "</tr>";
		tableString += "</table>"
		$("#tblDeleteRuleGroupList").html(tableString);
		$('#deleteRuleGroupPopUp').click();
	}
	function clearGroupRuleGrid(){
		var $grid = $("#groupRuleGrid");
		var rowIds = $grid.jqGrid('getDataIDs');
		for(var i=0,len=rowIds.length;i<len;i++){
			var currRow = rowIds[i];
			$grid.jqGrid('delRowData', currRow);
		}
	}
	
	function reloadGroupRuleGridData(id){


		var $grid = $("#groupRuleGrid");
		jQuery('#groupRuleGrid').jqGrid('clearGridData');
		clearGroupRuleGrid();
		$grid.jqGrid('setGridParam',{datatype:'json',page: 1,sortname: 'id',sortorder: "desc", postData:{
			'ruleGroupId': id   
		   
		}}).trigger('reloadGrid');
		} 
	
	function ruleListPopUp(id){
		clearAllMessages();

		$("#btnGroupRuleListClose").show();
		loadJQueryGroupRuleGrid(id);
		$('#viewGroupRulesPopup').click();
	}
	
	function deleteRuleGroupInstance(actionUrl){
		
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
				'ruleGroupId': parent.ckIntanceSelected[0],
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
				    $('#ruleGroupGrid').trigger('reloadGrid');
				    $("#btnDeleteRuleGroupCancel").hide();
				   
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
			    $("#btnDeleteRuleGroupCancel").show();
			}

		});
	} 


</script> 
