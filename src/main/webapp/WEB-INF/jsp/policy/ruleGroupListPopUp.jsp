<%@page import="com.elitecore.sm.common.constants.SystemParametersConstant"%>
<%@page import="com.elitecore.sm.util.MapCache"%>
<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<%@taglib 	uri="http://www.springframework.org/tags" 			prefix="spring"%>
<%@ taglib 	uri="http://www.springframework.org/security/tags" 	prefix="sec"  %>

<div class="modal-content">
	<div class="modal-header padding10">
		<h4 class="modal-title">
			<spring:message code="business.policy.rule.group.grid.heading" ></spring:message>
		</h4>
	</div>

	<div class="box-body table-responsive no-padding box">
		<table class="table table-hover" id="ruleGroupGrid"></table>
		<div id="ruleGroupGridPagingDiv"></div>
		<div class="clearfix"></div> 
	    <div id="divLoading" align="center" style="display: none;"><img src="img/preloaders/Preloader_10.gif" /></div>
	</div>


	<div id="view_group_list_delete-buttons-div" class="modal-footer padding10">
		<button type="button" class="btn btn-grey btn-xs "
			id="btnGroupRuleListClose" data-dismiss="modal"
			onclick="closeFancyBox();clearGroupRuleGrid();">
			<spring:message code="btn.label.close" ></spring:message>
		</button>

	</div>
	
	
	
	<script>
	
	function clearGroupRuleGrid(){
		var $grid = $("#ruleGroupGrid");
		var rowIds = $grid.jqGrid('getDataIDs');
		for(var i=0,len=rowIds.length;i<len;i++){
			var currRow = rowIds[i];
			$grid.jqGrid('delRowData', currRow);
		}
	}
	
	function loadJQueryRuleGroupGrid($policyId){
		clearGroupRuleGrid();
		console.log($policyId);
		
		$("#ruleGroupGrid").jqGrid({
			url: "<%= ControllerConstants.GET_POLICY_GROUP_LIST%>",
			postData : {
				'policyId': $policyId
			},
			datatype: "json",
			colNames: [
						  "#",
						  "<spring:message code='business.policy.rule.group.grid.column.ruleName' ></spring:message>",
			           	  "<spring:message code='business.policy.rule.group.grid.column.description' ></spring:message>"
				],
			colModel: [
				{name:'id',index:'id',hidden:true},
				{name:'name',index:'name',sortable:false},
				{name:'description',index:'description',sortable:false}
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
				console.log("BEFORE REQUeST::::::");
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
				$("#ruleGroupGrid").jqGrid('setGridParam', { 
			        postData: {'policyId': $policyId }
			 	}).trigger("reloadGrid", [{current: true}]);
	}
	
	</script>

</div>

