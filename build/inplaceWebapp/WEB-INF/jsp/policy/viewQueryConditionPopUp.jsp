<%@page import="com.elitecore.sm.common.constants.SystemParametersConstant"%>
<%@page import="com.elitecore.sm.util.MapCache"%>
<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<%@taglib 	uri="http://www.springframework.org/tags" 			prefix="spring"%>
<%@ taglib 	uri="http://www.springframework.org/security/tags" 	prefix="sec"  %>
<div class="modal-content">
		        <div class="modal-header padding10">
		            <h4 class="modal-title"><spring:message code="policymgmt.databaseQueries.conditionList"></spring:message></h4>
		        </div>
					
	<div id = "conditionPopUpGrid" class="box-body table-responsive no-padding box">
	</div>
	        
     <div id="closeGridDiv" class="modal-footer padding10">
         <button type="button" class="btn btn-grey btn-xs " id="queryConditionListClose" data-dismiss="modal" onclick="closeFancyBox();removeConditionGrid();"><spring:message code="btn.label.close"></spring:message></button>
     </div>
      <button type="button" class="btn btn-grey btn-xs " id="queryConListUpClose" data-dismiss="modal" style="display: none;" onclick="closeFancyBox();"><spring:message code="btn.label.close"></spring:message></button>
     
</div>
<script>

var conditionRows;

function loadJQueryConditionGrid(id){
	
	$("#conditionPopUpGrid").append('<table class="table table-hover" id="conditionQueryGrid"></table><div id="conditionQueryGridPagingDiv"></div>');
	$("#conditionQueryGrid").jqGrid({
		url: "<%= ControllerConstants.INIT_DATABASE_QUERY_CONDITIONS%>",
		postData : {
			"queryId":id
		},
		datatype: "json",
		colNames: [
		           "#",
		           "databaseKey",
		           "<spring:message code='policymgmt.databaseQueries.fieldname' ></spring:message>",
		           "<spring:message code='policymgmt.databaseQueries.operator' ></spring:message>",
		           "<spring:message code='policymgmt.databaseQueries.unified' ></spring:message>"
		           ],
		colModel: [
		          	{name:'queryId',index:'id',hidden:true},
		          	{name:'databaseKey',index:'databaseKey',hidden:true},
		          	{name:'fieldName',index:'name'},
		          	{name:'operator',index:'operator'},
		          	{name:'unifiedField',index:'unifiedField'}
		          ],
		        rowNum:<%= MapCache.getConfigValueAsInteger(SystemParametersConstant.TOTAL_ROWS_TO_DISPLAY_IN_GRID, 10) %>,
		  		rowList:[5,10,20,50,100],
		  		height: 'auto',
		  		sortname: 'id',
		  		sortorder: "desc",
		  		pager: "#conditionQueryGridPagingDiv",
		  		viewrecords: true,
		  		multiselect: false,
		  		timeout : 120000,
		  	    loadtext: "Loading...",
		  		caption: "<spring:message code="policymgmt.databaseQueries.list"></spring:message>",
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
		  	 		//$("#divLoading").dialog('close');
		  	 			$(".ui-dialog-titlebar").show();
		  	 			if ($('#conditionQueryGrid').getGridParam('records') === 0) {
		  	             $('#conditionQueryGrid tbody').html("<div style='padding:6px;background:#D8D8D8;text-align:center;'><spring:message code="collectionService.summary.agent.grid.empty.records"></spring:message></div>");
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
		  		beforeSelectRow: function (rowid, e){
		  		}
		  		}).navGrid("#queryGridPagingDiv",{edit:false,add:false,del:false,search:false,refresh:false});
		  			$(".ui-jqgrid-titlebar").hide();
		  			$(".ui-pg-input").height("10px");
}

function removeConditionGrid(){
	$("#conditionPopUpGrid").empty();
	
}
//Time out is set becuase jquery cannot load data and DOM is not available
function loadConditionTable(queryId){
	
	$("#conditionPopUpGrid").empty();
	loadJQueryConditionGrid(queryId);
	setTimeout(function(){ 
	var conditionRows = jQuery("#conditionQueryGrid").getDataIDs();		
	 for(a=0;a<conditionRows.length;a++)
	 {
	    row=jQuery("#conditionQueryGrid").getRowData(conditionRows[a]);
	    fillConditionTable(a,row);
	 }
	}, 1500);
}


</script>
