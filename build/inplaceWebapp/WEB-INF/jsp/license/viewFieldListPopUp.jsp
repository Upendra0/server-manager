<%@page import="com.elitecore.sm.common.constants.SystemParametersConstant"%>
<%@page import="com.elitecore.sm.util.MapCache"%>
<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<%@taglib 	uri="http://www.springframework.org/tags" 			prefix="spring"%>
<%@ taglib 	uri="http://www.springframework.org/security/tags" 	prefix="sec"  %>
<div class="modal-content">
		        <div class="modal-header padding10">
		            <h4 class="modal-title"><spring:message code="rule.data.mgmt.field.list"></spring:message></h4>
		        </div>
					
	<div id = "fieldListPopUpGrid" class="box-body table-responsive no-padding box">
	</div>
	        
    <div id="closeGridDiv" class="modal-footer padding10">
     	<sec:authorize access="hasAuthority('UPDATE_RULE_DATA_CONFIG')">
     		<button type="button" class="btn btn-grey btn-xs " id="fieldListEdit" data-dismiss="modal" onclick="closeFancyBox();editLookupTable();">Edit</button>
      	</sec:authorize>	
         <button type="button" class="btn btn-grey btn-xs " id="fieldListClose" data-dismiss="modal" onclick="closeFancyBox();removeFieldGrid();"><spring:message code="btn.label.close"></spring:message></button>
     </div>
     
      <button type="button" class="btn btn-grey btn-xs " id="fieldListUpClose" data-dismiss="modal" style="display: none;" onclick="closeFancyBox();"><spring:message code="btn.label.close"></spring:message></button>
      
</div>
<script>

var conditionRows;
var tableIDForEdit;

	function editLookupTable(){		
		updateLookupTable( tableIDForEdit , "" );
	}
	
	function editTableEnableDisable( currtableid ){
		var ResponseObj = jQuery("#tableGrid").jqGrid('getRowData', currtableid );		
		console.log( ResponseObj );
		if(ResponseObj.viewDataSize > 0){
			$("#fieldListEdit").hide();
		}else{
			$("#fieldListEdit").show();
		}
	}

function loadJQueryFieldGrid(id,rowNum){
	editTableEnableDisable( id );
	tableIDForEdit = id;
	$("#fieldListPopUpGrid").append('<table class="table table-hover" id="fieldListGrid"></table><div id="fieldListGridPagingDiv"></div>');
	var load_once = false;
	if (rowNum == 0)
		load_once = true;
	$("#fieldListGrid").jqGrid({
		url: "<%= ControllerConstants.INIT_RULE_FIELD_LIST%>",
		postData : {
			"tableId":id
		},
		datatype: "json",
		colNames: [
		           "#",
		           "<spring:message code='rule.data.mgmt.field.name' ></spring:message>",
		           "<spring:message code='rule.data.mgmt.display.name' ></spring:message>",
		           "<spring:message code='rule.data.mgmt.unique' ></spring:message>"
		           ],
		colModel: [
		          	{name:'tableId',index:'id',hidden:true},
		          	{name:'viewFieldName',index:'viewFieldName'},
		          	{name:'displayName',index:'displayName' , hidden:true },
		          	{name:'unique',index:'unique'}
		          ],
		        loadonce:load_once,
		        rowNum:rowNum,
		  		rowList:[5,10,20,50,100],
		  		height: 250,
		  		sortname: 'id',
		  		sortorder: "asc",
		  		pager: "#fieldListGridPagingDiv",
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
		  	 		count = $('#fieldListGrid').getGridParam('reccount');
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

function removeFieldGrid(){
	$("#fieldListPopUpGrid").empty();
}

//Time out is set becuase jquery cannot load data and because of that DOM is not available
function loadFieldListTable(tableId){
	tableIDForEdit = tableId ;
	
	$("#fieldListPopUpGrid").empty();
	var rowNum = 0;
	loadJQueryFieldGrid(tableId,rowNum);
	setTimeout(function(){
	var conditionRows = jQuery("#fieldListGrid").getDataIDs();
	 for(a=0;a<conditionRows.length;a++)
	 {
	    row=jQuery("#fieldListGrid").getRowData(conditionRows[a]);
	    fillFieldListTable(a+1,row);
	 }
	 $("#fieldListPopUpGrid").empty();
	}, 500);
}


</script>
