<%@page
	import="com.elitecore.sm.common.constants.SystemParametersConstant"%>
<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<%@page import="com.elitecore.sm.util.MapCache"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="elitecore"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<html>
<head>
<script type="text/javascript">
		
		var ckGroupSelected = new Array();
		var gridDataArr = [];
		var ruleGroupList = {};
		
		var ckGroupSelectedData = new Array();
		
		$(document).ready(function() {

	$("#policy-group-list").jqGrid({
	      	url: "<%=ControllerConstants.GET_RULE_GROUP_LIST%>",
			postData : {
				ruleGroupName: function () {
	   	        	return $("#search_rulegroupName").val();
	    		},
	    		description: function () {
	   	        	return $("#search_rulegroupDescription").val();
	    		},
				
				serverInstanceId : function() {
					return ${instanceId};
				},
	    		'existingConditionIds' : function(){
	    			return existingConditionIdsList();
	    		}
			},
			datatype : "json",
			colNames : [
					"<spring:message code='policymgmt.rulegroup.id' ></spring:message>",
					"<input type='checkbox' id='selectAllConditions' onclick='conditionHeaderCheckbox(event, this)'></input>",
					"<spring:message code='policymgmt.rulegroup.name' ></spring:message>",
					"<spring:message code='policymgmt.rulegroup.description' ></spring:message>", ],
			colModel : [ {name : 'id', index : 'id', sortable : true, hidden : true	},
			             {name: 'checkbox', index: 'checkbox',sortable:false, formatter:conditionCheckboxFormatter, align : 'center', width:'30%'},
			             {name : 'name', index : 'name', sortable : true}, 
			             {name : 'description',	index : 'description', sortable : false} ],
			rowNum : <%=MapCache.getConfigValueAsInteger(SystemParametersConstant.TOTAL_ROWS_TO_DISPLAY_IN_GRID, 10)%>,											rowList : [ 10, 20, 60, 100 ],
			height : 'auto',
			sortname : 'id',
			sortorder : "asc",
			pager : "#policy-group-list-paging-div",
			viewrecords : true,
			//multiselect : true,
			timeout : 120000,
			loadtext : "Loading...",
			caption : "<spring:message code='policymgmt.rulegroup.list.caption'></spring:message>",
			beforeRequest:function(){
			//ckGroupSelected = new Array();
            $('#divLoading').dialog({
                    autoOpen: false,
                    width: 90,
                    modal:true,
                    overlay: { opacity: 0.3, background: "white" },
                    resizable: false,
                    height: 125,
                });
               // $('#divLoading').dialog('open');	
                $(".ui-dialog-titlebar").hide();
            },
			loadComplete : function(data) {
				
				if(existingPolicyGroupsIds.length > 0) {
					var $grid = $("#policy-group-list");
					var rowIds = $grid.jqGrid('getDataIDs');
					for (i = 0; i < rowIds.length; i++) {
				    	for(j = 0; j < existingPolicyGroupsIds.length; j++) {
				    		if (rowIds[i] == existingPolicyGroupsIds[j]) {
				    			$grid.jqGrid('delRowData', rowIds[i]);
				    			break;
					        }			    		
				    	}
				    } 
				}
				$("#divLoading").dialog('close');
				selectedCheckBoxOnAllPages();

			},
			onPaging : function(pgButton) {
				clearResponseMsgDiv();
				clearGroupGrid();
			},
			loadError : function(xhr, st, err) {
				handleGenericError(xhr, st, err);
			},
			beforeSelectRow : function(rowid, e) {
				// this blank function will not select the entire row. Only checkbox can be selectable.
				
				return false;
			},
			onSelectAll : function(id, status) {
				if (status == true) {
					ckGroupSelected = new Array();
					for (i = 0; i < id.length; i++) {
						ckGroupSelected.push(id[i]);
					}
				} else {
					ckGroupSelected = new Array();
				}
			},
			recordtext : "<spring:message code='serverManagement.grid.pager.total.records.text'></spring:message>",
			emptyrecords : "<spring:message code='serverManagement.grid.empty.records'></spring:message>",
			loadtext : "<spring:message code='serverManagement.grid.loading.text'></spring:message>",
			pgtext : "<spring:message code='serverManagement.grid.pager.text'></spring:message>"
		}).navGrid("#policy-group-list-paging-div", {
		edit : false,
		add : false,
		del : false,
		search : false
	});
	$(".ui-jqgrid-titlebar").hide();
	resizeGroupGrid();
	
	});
		
		function existingConditionIdsList(){
			var existingIDS = existingPolicyGroupsIds;
			return existingIDS;
		}
		function selectedCheckBoxOnAllPages(){			
			var i=0;
			for (i = 0; i < ckGroupSelected.length; i++) {	
					$('#jqg_policy-group-list_' + ckGroupSelected[i] ).prop('checked',true);
			}
		}
		function pushCheckedConditionInArray(element){
			var id = element.value;		
			console.log( id );
			
			if(element.checked && ($.inArray( id+"" ,  ckGroupSelected ) == -1) ){			
				ckGroupSelected.push(id +"");			
			//	ckGroupSelectedData.push( $("#policy-group-list").jqGrid("getRowData", id ) );
			}else{
				// ckGroupSelected.splice( ckGroupSelected.indexOf(id),1);
				//ckGroupSelectedData.splice( ckGroupSelectedData.indexOf( $("#policy-group-list").jqGrid("getRowData", id ) ),1 );
					for(var y=0; y < ckGroupSelected.length ; y++ ){					
					if( ckGroupSelected[y]['id'] == id ){
						ckGroupSelected.splice( y , 1);
					}
				}
			}
			
			if( element.checked && ($.inArray( $("#policy-group-list").jqGrid("getRowData", id ) ,  ckGroupSelectedData ) == -1) ){
				ckGroupSelectedData.push( $("#policy-group-list").jqGrid("getRowData", id ) );
			}else{
				// ckGroupSelectedData.splice( ckGroupSelectedData.indexOf( $("#policy-group-list").jqGrid("getRowData", id ) ),1 );				
				for(var y=0; y < ckGroupSelectedData.length ; y++ ){					
					if( ckGroupSelectedData[y]['id'] == id ){
						ckGroupSelectedData.splice( y , 1);
					}
				}
			}
			
		}
		
		function conditionCheckboxFormatter(cellvalue, options, rowObject){
			if ($.inArray( rowObject["id"]+"" ,  ckGroupSelected ) != -1)
			{
				//alert(rowObject["name"]);
				return '<input type="checkbox" id="'+rowObject["name"]+'_cond_select_lnk" name="conditionCheckbox" value="'+rowObject["id"]+'" onchange="pushCheckedConditionInArray(this);" checked />';
			}
			else{ 
			   return '<input type="checkbox" id="'+rowObject["name"]+'_cond_select_lnk" name="conditionCheckbox" value="'+rowObject["id"]+'" onchange="pushCheckedConditionInArray(this);"/>';
			}
		}
		
		
		
		
		
		function reloadRuleGroupGridDataWithClearMsgRule() {
			clearAllMessages();
			clearResponseMsgDiv();
			clearResponseMsg();
			clearErrorMsg();

			reloadRuleGroupGridDataRule();
		}
		
		function reloadRuleGroupGridDataRule(){
			
			var $grid = $("#policy-group-list");
			jQuery('#policy-group-list').jqGrid('clearGridData');
			clearRuleGroupInstanceGrid();
			$grid.jqGrid('setGridParam',{datatype:'json',page: 1,sortname: 'id',sortorder: "desc", postData:{
					
				ruleGroupName: function () {
	   	        	return $("#search_rulegroupName").val();
	    		},
	    		description: function () {
	   	        	return $("#search_rulegroupDescription").val();
	    		},
				
				serverInstanceId : function() {
					return ${instanceId};
				},
	    		'existingConditionIds' : function(){
	    			return existingConditionIdsList();
	    		}	
		    		
			}}).trigger('reloadGrid');
		} 
		
		function clearRuleGroupInstanceGrid() {
			var $grid = $("#policy-group-list");
			var rowIds = $grid.jqGrid('getDataIDs');
			// iterate through the rows and delete each of them
			for (var i = 0, len = rowIds.length; i < len; i++) {
				var currRow = rowIds[i];
				$grid.jqGrid('delRowData', currRow);
			}
			
		}

	function resizeGroupGrid() {
		var $grid = $("#policy-group-list"), newWidth = $grid.closest(
				".ui-jqgrid").parent().width();
		if (newWidth < 1000) {
			newWidth = 800;
		}
		//$grid.jqGrid("setGridWidth", newWidth, true);
	}

	function clearGroupGrid() {
		var $grid = $("#policy-group-list");
		var rowIds = $grid.jqGrid('getDataIDs');
		// iterate through the rows and delete each of them
		for (var i = 0, len = rowIds.length; i < len; i++) {
			var currRow = rowIds[i];
			$grid.jqGrid('delRowData', currRow);
		}
	}
	
	function clearRuleGroupGrid() {
		var $grid = $("#ruleGroupgrid");
		var rowIds = $grid.jqGrid('getDataIDs');
		// iterate through the rows and delete each of them
		for (var i = 0, len = rowIds.length; i < len; i++) {
			var currRow = rowIds[i];
			$grid.jqGrid('delRowData', currRow);
		}
	}

	var ruleGroupRowCount = 0;
	function addRuleGroupTOGrid() {
		var selectedGroupsArray = new Array();
		var i, selRowIds = $("#policy-group-list").jqGrid("getGridParam", "selarrrow"), n, rowData;
		// for (i = 0, n = ckGroupSelected.length; i < n; i++) {
		 for (i = 0, n = ckGroupSelectedData.length; i < n; i++) {
			ruleGroupRowCount = ruleGroupRowCount + 1;
		  //  rowData = $("#policy-group-list").jqGrid("getRowData", ckGroupSelected[i]);
		  
		  	rowData = ckGroupSelectedData[ i ];
		  	
		    selectedGroupsArray.push(	rowData	 );
		}

		//clearRuleGroupGrid();
		$("#ruleGroupgrid").jqGrid('addRowData', ruleGroupRowCount,	selectedGroupsArray);
		
		ckGroupSelectedData = new Array();
		ckGroupSelected = new Array();
		
		loadJQueryRuleGroupGrid();
		closeFancyBox();
	}
</script>
</head>
<body>
	<div class="modal-content">
		<div class="modal-header padding10">
			<h4 class="modal-title">
				<i class="icon-hdd"></i>
				<spring:message code="business.policy.grid.column.ruleGroupList" ></spring:message>
			</h4>
		</div>
		<div id="ruleGroupContentDiv" class="modal-body padding10 inline-form">
			<jsp:include page="../common/responseMsg.jsp" ></jsp:include>

			<div class="fullwidth borbot">
        	 
         	<div class="col-md-6 inline-form" style="padding-left: 0px !important;">
         	
         		<div class="form-group">
	         		<spring:message code="policymgmt.rulegroup.name"  var="label"></spring:message>
	         		<spring:message code="policymgmt.rulegroup.name"  var="tooltip"></spring:message>
	         		
	         		<div class="table-cell-label">${label }</div>
	             	<div class="input-group ">
	             		<input type="text" id="search_rulegroupName"  name="search_rulegroupName" maxlength="100" class="form-control table-cell input-sm" data-toggle="tooltip" data-placement="bottom" title="${tooltip }"  >
	             		<span class="input-group-addon add-on last" > <i id="rulegroupName_err" class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="left" title=""></i></span> 
	             		
	             	</div>
	             </div>
	            </div> 
	             
	             
	         	<div class="col-md-6 inline-form" style="padding-left: 0px !important;">
	             <div class="form-group">
	         		<spring:message code="business.policy.rule.grid.column.description"  var="label"></spring:message>
	         		<spring:message code="business.policy.rule.grid.column.description"  var="tooltip"></spring:message>
	         		
	         		<div class="table-cell-label">${label }</div>
	             	<div class="input-group ">
	             		<input type="text" id="search_rulegroupDescription"  name="search_rulegroupDescription" maxlength="100" class="form-control table-cell input-sm" data-toggle="tooltip" data-placement="bottom" title="${tooltip }"  >
	             		<span class="input-group-addon add-on last" > <i id="rulegroupDescription_err" class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="left" title=""></i></span> 
	             		
	             	</div>
	             </div>
	             
         	</div>
         	<div class="clearfix"></div>
     		<div class="pbottom15 form-group "> 
     			
   					<button id="searchRuleGroupBtn" class="btn btn-grey btn-xs" onclick="reloadRuleGroupGridDataWithClearMsgRule();"><spring:message code="btn.label.search" ></spring:message></button>

   			</div>
   			
   		</div> 


			<div class="box-body table-responsive no-padding box">
				<table class="table table-hover" id="policy-group-list"></table>
				 <div id="policy-group-list-paging-div"></div> 
				<div class="clearfix"></div>
				<div id="divLoading" align="center" style="display: none;">
					<img src="img/preloaders/Preloader_10.gif" />
				</div>
			</div>

		</div>
		<div id="buttons-div" class="modal-footer padding10 text-left">
			<button type="button" class="btn btn-grey btn-xs "
				onclick="addRuleGroupTOGrid();" id="select_rulegroup_grid_ok_btn">
				<spring:message code="btn.label.select" ></spring:message>
			</button>
			<button type="button" class="btn btn-grey btn-xs "
				onclick="closeFancyBox();" id="select_rulegroup_grid_close_btn">
				<spring:message code="btn.label.close" ></spring:message>
			</button>
		</div>

	</div>
	<!-- /.modal-content -->

</body>

</html>
