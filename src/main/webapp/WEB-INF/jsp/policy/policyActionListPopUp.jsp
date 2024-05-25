<%@page import="com.elitecore.sm.common.constants.SystemParametersConstant"%>
<%@page import="com.elitecore.sm.util.MapCache"%>
<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<%@taglib 	uri="http://www.springframework.org/tags" 			prefix="spring"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="elitecore" %> 
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<html>
<head>
<script type="text/javascript">
		
	var ckActionSelected = new Array();
	var gridDataArr = [];
	var actionList = {};
	//var existingActionIds = new Array();
	var ckAction_glo = new Array();
	
	var checkedActionListData = new Array();
	var checkedActionList = new Array();
		
	$(document).ready(function() {
		$("#action-list").jqGrid({
		      	url: "<%=ControllerConstants.GET_POLICY_RULE_ACTION_LIST%>",
				postData : {
					
					'policyActionName': function () {
						var id  =  $("#search_ActionName").val();
				        return id;
			    		},
			    		
			    		'policyActionDesc': function(){
			    			return $("#search_ActionDescription").val();
			    		},
					
					'serverInstanceId' : function() {
						return ${instanceId};
					}
				},
				datatype : "json",
				colNames : [
					  "#",
					  "<input type='checkbox' id='selectAllActions' onclick='actionHeaderCheckbox(event, this)'></input>",
					  "<spring:message code='business.policy.rule.action.grid.column.name' ></spring:message>",
		           	  "<spring:message code='business.policy.rule.grid.column.description' ></spring:message>",
		              "<spring:message code='business.policy.rule.action.grid.column.type' ></spring:message>",
		              "<spring:message code='business.policy.rule.condition.grid.column.syntaxt' ></spring:message>",
		              "<spring:message code='business.policy.rule.grid.column.action' ></spring:message>"
		             ],
				colModel : [ 
				    {name:'id',index:'id',align:'center',hidden:true},
				    {name: 'checkbox', index: 'checkbox',sortable:false, formatter:actionCheckboxFormatter, align : 'center', width:'30%'},
					{name:'name',index:'name',sortable:true},
					{name:'description',index:'description',sortable:true},
					{name:'type',index:'type',sortable:true},
					{name:'expression',index:'expression',sortable:false,hidden: true},
					{name:'action',index:'action',sortable:true,formatter:ruleActionColumnFormatter}
					],
				rowNum : <%=MapCache.getConfigValueAsInteger(SystemParametersConstant.TOTAL_ROWS_TO_DISPLAY_IN_GRID, 10)%>,
				rowList: [10,20,60,100],
				height : 250,
				sortname : 'id',
				sortorder : "desc",
				pager: "#policyruleActionPagingDiv",
				viewrecords : true,
				timeout : 120000,
				loadtext : "Loading...",
				caption : "<spring:message code='business.policy.action.existing.list.section.title'></spring:message>",
				beforeRequest:function(){
				ckActionSelected = new Array();
				$('#divLoading').dialog({
	                    autoOpen: false,
	                    width: 90,
	                    modal:true,
	                    overlay: { opacity: 0.3, background: "white" },
	                    resizable: false,
	                    height: 125,
	                });
	                //$('#divLoading').dialog('open');	
	                $(".ui-dialog-titlebar").hide();
	            },
				loadComplete : function(data) {
					if(existingActionIds.length > 0) {
						$("#policyruleActionPagingDiv").show();
						var $grid = $("#action-list");
						var rowIds = $grid.jqGrid('getDataIDs');
						for (i = 0; i < rowIds.length; i++) {//iterate over each row
					    	for(j = 0; j < existingActionIds.length; j++) {
					    		if (rowIds[i] == existingActionIds[j]) {
					    			$grid.jqGrid('delRowData', rowIds[i]);
					    			break;
						        }			    		
					    	}
					    } 
					}
					//$("#divLoading").dialog('close');
	
				},
				onPaging : function(pgButton) {
					clearResponseMsgDiv();
					clearActionGrid();
				},
				loadError : function(xhr, st, err) {
					handleGenericError(xhr, st, err);
				},
				beforeSelectRow : function(rowid, e) {
					// this blank function will not select the entire row. Only checkbox can be selectable.
					var $grid = $("#action-list");
					if ($("#jqg_action-list_" + $grid.jqGrid('getCell', rowid, 'id')).is(':checked')) {
						if (ckActionSelected.indexOf($grid.jqGrid('getCell',rowid, 'id')) == -1) {
							ckActionSelected.push($grid.jqGrid('getCell',rowid,'id'));
						}
					} else {
						if (ckActionSelected.indexOf($grid.jqGrid('getCell', rowid,'id')) != -1) {
							ckActionSelected.splice(ckActionSelected.indexOf($grid.jqGrid('getCell',rowid,'id')),1);
						}
					}
					return false;
				},
				recordtext : "<spring:message code='serverManagement.grid.pager.total.records.text'></spring:message>",
				emptyrecords : "<spring:message code='serverManagement.grid.empty.records'></spring:message>",
				loadtext : "<spring:message code='serverManagement.grid.loading.text'></spring:message>",
				pgtext : "<spring:message code='serverManagement.grid.pager.text'></spring:message>"
			}).navGrid("#policyruleActionPagingDiv", {
			edit : false,
			add : false,
			del : false,
			search : false
		});
		$(".ui-jqgrid-titlebar").hide();
		resizeActionGrid();
	
	});
	
	function reloadActionGridDataWithClearMsgRule() {
		clearAllMessages();
		clearResponseMsgDiv();
		clearResponseMsg();
		clearErrorMsg();

		reloadActionGridDataRule();
	}
	
	function reloadActionGridDataRule(){
		
		var $grid = $("#action-list");
		jQuery('#action-list').jqGrid('clearGridData');
		clearActionInstanceGrid();
		$grid.jqGrid('setGridParam',{datatype:'json',page: 1,sortname: 'id',sortorder: "desc", postData:{
				
			'policyActionName': function () {
				var id  =  $("#search_ActionName").val();
		        return id;
	    		},
	    		
	    		'policyActionDesc': function(){
	    			return $("#search_ActionDescription").val();
	    		},
			
			'serverInstanceId' : function() {
				return ${instanceId};
			}
	    		
	    		
		}}).trigger('reloadGrid');
	} 
	
	function clearActionInstanceGrid() {
		var $grid = $("#action-list");
		var rowIds = $grid.jqGrid('getDataIDs');
		// iterate through the rows and delete each of them
		for (var i = 0, len = rowIds.length; i < len; i++) {
			var currRow = rowIds[i];
			$grid.jqGrid('delRowData', currRow);
		}
	}
	
	function resizeActionGrid() {
		var $grid = $("#action-list"), newWidth = $grid.closest(".ui-jqgrid").parent().width();
		if (newWidth < 1000) {
			newWidth = 800;
		}
	}

	function clearActionGrid() {
		var $grid = $("#action-list");
		var rowIds = $grid.jqGrid('getDataIDs');
		// iterate through the rows and delete each of them
		for (var i = 0, len = rowIds.length; i < len; i++) {
			var currRow = rowIds[i];
			$grid.jqGrid('delRowData', currRow);
		}
	}
	
	function ruleActionColumnFormatter(cellvalue, options, rowObject) {
		if( rowObject["type"] == 'expression'){
			return rowObject["expression"];
		}else{
			return rowObject["action"];
		}
	}
	
	var actionRowCount = 0;
	function addActionToGrid() {
		var ckIntanceSelected = [];
	  
	/**	$.each($("input[name='actionCheckbox']:checked"), function(){
	    	rowData = $("#action-list").jqGrid("getRowData", $(this).val());
	    	ckIntanceSelected.push(rowData);
	    });
		var conditionRowCount = 0;
		for (i = 0, n = ckIntanceSelected.length; i < n; i++) {
			conditionRowCount = conditionRowCount + 1;
		}

		$("#policyActionGrid").jqGrid('addRowData', conditionRowCount,	ckIntanceSelected);
		*/
		for(var i=0;i < checkedActionListData.length ; i++){
			ckIntanceSelected.push( checkedActionListData[i] ) ;			
		}
		var conditionRowCount = 0;
		for (i = 0, n = checkedActionListData.length; i < n; i++) {
			conditionRowCount = conditionRowCount + 1;
		}
		
		checkedActionListData = new Array();
		checkedActionList = new Array();
		
		$("#policyActionGrid").jqGrid('addRowData', conditionRowCount,	ckIntanceSelected);
		
		loadJQueryPolicyActionGrid();
		closeFancyBox();
	}
	
	function actionHeaderCheckbox(e, element) {
	    e = e || event;/* get IE event ( not passed ) */
	    e.stopPropagation ? e.stopPropagation() : e.cancelBubble = false;

	    //if root checkbox is checked :-> all child checkbox should ne checked
	    if(element.checked) {
	    	$('input:checkbox[name="actionCheckbox"]').prop('checked',true);
	    	
	    	$('input:checkbox[name="actionCheckbox"]').each(function( index, element ) {	
	    			pushCheckedActionInArray( element );
	    	});
	    	
	    } else {
	    	//if root checkbox is unchecked :-> all child checkbox should be unchecked
	    	$('input:checkbox[name="actionCheckbox"]').prop('checked',false);
	    	
	    	$('input:checkbox[name="actionCheckbox"]').each(function( index, element ) {	
	    		pushCheckedActionInArray( element );
	    	});
	    }
	    
	}

	function updateRootCheckbox(element, gridId) {
		if(!element.checked){
			// if current child checkbox is not checked : uncheck root checkbox
			$('input:checkbox[id="selectAllActions"]').prop('checked',false);
		} else {
			//if current child checkboox is checked and all checkbox are checked then check root checkbox
			var count = jQuery("#"+gridId).jqGrid('getGridParam', 'records');
			if ($('input:checkbox[name="actionCheckbox"]:checked').length === count) {
				$('input:checkbox[id="selectAllActions"]').prop('checked',true);
		    }
		}
	}
	
	function pushCheckedActionInArray( element )
	{
		var id = element.value;
				
		
		if(element.checked && ($.inArray( id+"" ,  checkedActionList ) == -1) ){			
			checkedActionList.push(id +"");			
			checkedActionListData.push( $("#action-list").jqGrid("getRowData", id ) );
		}else{
			checkedActionList.splice(checkedActionList.indexOf(id),1);
			checkedActionListData.splice( checkedActionListData.indexOf( $("#action-list").jqGrid("getRowData", id ) ),1 );
		}		
		console.log( checkedActionList );
	}
	
	function actionCheckboxFormatter(cellvalue, options, rowObject){
		if ($.inArray( rowObject["id"]+"" ,  checkedActionList ) != -1)
		{
		  return '<input type="checkbox" id="'+rowObject["name"]+'_act_select_lnk" name="actionCheckbox" value="'+rowObject["id"]+'" onchange="pushCheckedActionInArray(this);updateRootCheckbox(this, \'action-list\')"  checked />';
		}
		else {
		    return '<input type="checkbox" id="'+rowObject["name"]+'_act_select_lnk" name="actionCheckbox" value="'+rowObject["id"]+'" onchange="pushCheckedActionInArray(this);updateRootCheckbox(this, \'action-list\')"/>';
		}
    }
	
	function pushActionInArray(id,element){
		if(element.checked){
			ckAction_glo.push(id);
		}else{
			ckAction_glo.splice(ckAction_glo.indexOf(id),1);
		}
	}
</script>
</head>
<body>
	<div class="modal-content">
		<div class="modal-header padding10">
			<h4 class="modal-title">
				<i class="icon-hdd"></i>
				<spring:message code="business.policy.action.existing.list.section.title" ></spring:message>
			</h4>
		</div>
		<div id="actionContentDiv" class="modal-body padding10 inline-form">
			


		<div class="fullwidth borbot">
        	 
         	<div class="col-md-6 inline-form" style="padding-left: 0px !important;">
         	
         		<div class="form-group">
	         		<spring:message code="business.policy.rule.action.grid.column.name"  var="label"></spring:message>
	         		<spring:message code="business.policy.rule.action.grid.column.name"  var="tooltip"></spring:message>
	         		
	         		<div class="table-cell-label">${label }</div>
	             	<div class="input-group ">
	             		<input type="text" id="search_ActionName"  name="search_ActionName" maxlength="100" class="form-control table-cell input-sm" data-toggle="tooltip" data-placement="bottom" title="${tooltip }"  >
	             		<span class="input-group-addon add-on last" > <i id="search_ActionName_err" class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="left" title=""></i></span> 
	             		
	             	</div>
	             </div>
	            </div> 
	             
	             
	         	<div class="col-md-6 inline-form" style="padding-left: 0px !important;">
	             <div class="form-group">
	         		<spring:message code="business.policy.rule.grid.column.description"  var="label"></spring:message>
	         		<spring:message code="business.policy.rule.grid.column.description"  var="tooltip"></spring:message>
	         		
	         		<div class="table-cell-label">${label }</div>
	             	<div class="input-group ">
	             		<input type="text" id="search_ActionDescription"  name="search_ActionDescription" maxlength="100" class="form-control table-cell input-sm" data-toggle="tooltip" data-placement="bottom" title="${tooltip }"  >
	             		<span class="input-group-addon add-on last" > <i id="search_ActionDescription_err" class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="left" title=""></i></span> 
	             		
	             	</div>
	             </div>
	             
         	</div>
         	<div class="clearfix"></div>
     		<div class="pbottom15 form-group ">
     			
   					<button id="searchPolicyActionBtn" class="btn btn-grey btn-xs" onclick="reloadActionGridDataWithClearMsgRule();"><spring:message code="btn.label.search" ></spring:message></button>

   			</div>
   			
   		</div>

			<div class="box-body table-responsive no-padding box">
				<table class="table table-hover" id="action-list"></table>
				<div class="clearfix"></div>
				<div id="divLoading" align="center" style="display: none;">
					<img src="img/preloaders/Preloader_10.gif" />
				</div>
			</div>

		</div>
		<div id="buttons-div" class="modal-footer padding10 text-left">
			<button type="button" id="select_action_grid_ok_btn" 
			class="btn btn-grey btn-xs "
				onclick="addActionToGrid();">
				<spring:message code="btn.label.select" ></spring:message>
			</button>
			<button type="button" id="select_action_grid_cancel_btn"
			 class="btn btn-grey btn-xs "
				onclick="closeFancyBox();">
				<spring:message code="btn.label.close" ></spring:message>
			</button>
		</div>

	</div>
	<div class="box-body table-responsive no-padding box">
		<table class="table table-hover" id="policyActionGrid"></table>
		<div id="policyruleActionPagingDiv"></div>
		<div class="clearfix"></div> 
	    <div id="divLoading" align="center" style="display: none;"><img src="img/preloaders/Preloader_10.gif" /></div>
	</div>
	<!-- /.modal-content -->

</body>
</html>		
	
