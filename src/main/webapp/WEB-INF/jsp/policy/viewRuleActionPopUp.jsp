 <%@page import="com.elitecore.sm.common.constants.SystemParametersConstant"%>
<%@page import="com.elitecore.sm.util.MapCache"%>
<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<%@taglib 	uri="http://www.springframework.org/tags" 			prefix="spring"%>
<%@ taglib 	uri="http://www.springframework.org/security/tags" 	prefix="sec"  %>

<script>
var id;
function clearRuleActionGrid(){
	var $grid = $("#ruleActionGrid");
	var rowIds = $grid.jqGrid('getDataIDs');
	for(var i=0,len=rowIds.length;i<len;i++){
		var currRow = rowIds[i];
		$grid.jqGrid('delRowData', currRow);
	}
}

function loadJQueryRuleActionGrid($ruleId){
	clearRuleActionGrid();
	$("#ruleActionGrid").jqGrid({
		url:"<%= ControllerConstants.GET_POLICY_ACTION_LIST_BY_RULE_ID %>",
		mtype:"GET",
		postData : {
			'ruleId': $ruleId
		},
		datatype:'json',
		colNames: [
			 	"<spring:message code='business.policy.rule.grid.column.id' ></spring:message>",
			  	"<spring:message code='policy.action.grid.column.service.name'></spring:message>",
			  	"<spring:message code='business.policy.rule.action.grid.column.type' ></spring:message>",
          	  	"<spring:message code='business.policy.condition.syntax.label' ></spring:message>",
          	  	"#"
			],
		colModel: [
				{name:'id',index:'id',hidden:true},
				{name:'name',index:'name', sortable: true},
				{name:'type',index:'type', sortable: true},
				{name:'action',index:'action',sortable: true, formatter: ruleActionColumnFormatter},
				{name:'relId',index:'relId',hidden:true}
		],
		rowNum:<%= MapCache.getConfigValueAsInteger(SystemParametersConstant.TOTAL_ROWS_TO_DISPLAY_IN_GRID, 10) %>,
		rowList:[5,10,20,60,100],
		height: 'auto',
		sortname: 'id',
		sortorder: "desc",
		pager: "#ruleActionGridPagingDiv",
		viewrecords: true,
		timeout : 120000,
	    loadtext: "Loading...",
	    caption: "<spring:message code="business.policy.action.grid.column.ruleList"></spring:message>",
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
	    },
	    beforeSelectRow: function(rowid, e) {
	        return false;
	    },
		loadComplete: function(data) {
	 		//$("#divLoading").dialog('close');
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
		pgtext : "<spring:message code="collectionService.summary.agent.grid.pager.text"></spring:message>",
		}).navGrid("#ruleActionGridPagingDiv",{edit:false,add:false,del:false,search:false});
			$(".ui-jqgrid-titlebar").hide();
			$(".ui-pg-input").height("10px");
			
			$("#ruleActionGrid").disableSelection();
			$("#ruleActionGrid").jqGrid('setGridParam', { 
		        postData: {'ruleId': $ruleId }
		 	}).trigger("reloadGrid", [{current: true}]);
}
function ruleActionColumnFormatter(cellvalue, options, rowObject) {
	
	if (rowObject["type"] === 'expression') {
		return rowObject["expression"];
	} else {
		return rowObject["action"];
	}
}
</script>

 <div class="modal-content">
		        <div class="modal-header padding10">
		            <h4 class="modal-title"><spring:message code="business.policy.action.grid.column.ruleList"></spring:message></h4>
		        </div>
					
	<div class="box-body table-responsive no-padding box">
		<table class="table table-hover" id="ruleActionGrid"></table>
		<div id="ruleActionGridPagingDiv"></div>
		<div class="clearfix"></div> 
	    <div id="divLoading" align="center" style="display: none;"><img src="img/preloaders/Preloader_10.gif" /></div>
	</div>
	
			        
		        <div id="delete-buttons-div" class="modal-footer padding10">
		            <button type="button" class="btn btn-grey btn-xs " id="btnRuleActionListClose" data-dismiss="modal" onclick="closeFancyBox();clearSelection();"><spring:message code="btn.label.close"></spring:message></button>
		            
		        </div>
				
		    </div>
	
		    <!-- /.modal-content -->
		  
		  
		    
