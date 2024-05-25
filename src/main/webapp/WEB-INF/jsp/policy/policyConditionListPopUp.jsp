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
		
	var ckConditionSelected = new Array();
	var ckConditionSelected_glo = new Array();
	var gridDataArr = [];
	var conditionList = {};
	//var existingConditionIds = new Array();
		
	var checkedConditionList = new Array();
	var checkedConditionListData = new Array();
	
	$(document).ready(function() {
		$("#condition-list").jqGrid({
		      	url: "<%=ControllerConstants.GET_POLICY_RULE_CONDITION_LIST%>",
				postData : {
					'serverInstanceId' : function() {
						return ${instanceId};
					},
		      	
					'policyConditionName': function () {
		    			var id  =  $("#search_policyName").val();
		    	        return id;
			    		},
			    		
			    		'policyConditionDesc': function(){
			    			return $("#search_policyDescription").val();
			    		},
			    		'existingConditionIds' : function(){
			    			return existingConditionIdsList();
			    		}
   	    		
				},
				datatype : "json",
				colNames : [
					  "#",
					   "<input type='checkbox' id='selectAllConditions' onclick='conditionHeaderCheckbox(event, this)'></input>",
					  "<spring:message code='business.policy.rule.condition.grid.column.name' ></spring:message>",
		           	  "<spring:message code='business.policy.rule.grid.column.description' ></spring:message>",
		              "<spring:message code='business.policy.rule.condition.grid.column.type' ></spring:message>",
		              "<spring:message code='business.policy.rule.condition.grid.column.syntaxt' ></spring:message>",
		              "<spring:message code='business.policy.rule.grid.column.condition' ></spring:message>",
		              "value"
		             ],
				colModel : [ {name:'id',index:'id',align:'center',hidden:true},
				    {name: 'checkbox', index: 'checkbox',sortable:false, formatter:conditionCheckboxFormatter, align : 'center', width:'30%'},
					{name:'name',index:'name',sortable:true},
					{name:'description',index:'description',sortable:true},
					{name:'type',index:'type',sortable:true},
					{name:'expression',index:'expression',sortable:false,hidden: true},
					{name:'action',index:'action',sortable:true,formatter:ConditionColumnFormatter},
					{name:'value',index:'value',sortable:true,hidden:true,formatter:ConditionColumnFormatter}
					],
				rowNum : <%=MapCache.getConfigValueAsInteger(SystemParametersConstant.TOTAL_ROWS_TO_DISPLAY_IN_GRID, 10)%>,											rowList : [ 10, 20, 60, 100 ],
				height : 250,
				sortname : 'id',
				sortorder : "desc",
				viewrecords : true,
				pager: "#policyruleConditionPagingDiv",
				timeout : 120000,
				loadtext : "Loading...",
				caption : "<spring:message code='business.policy.condition.existing.list.section.title'></spring:message>",
				beforeRequest:function(){
				ckConditionSelected = new Array();
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
					if(existingConditionIds.length > 0) {
						$("#policyruleConditionPagingDiv").show();
						var $grid = $("#condition-list");
						var rowIds = $grid.jqGrid('getDataIDs');
						for (i = 0; i < rowIds.length; i++) {//iterate over each row
					    	for(j = 0; j < existingConditionIds.length; j++) {
					    		if (rowIds[i] == existingConditionIds[j]) {
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
					clearConditionGrid();
				},
				loadError : function(xhr, st, err) {
					handleGenericError(xhr, st, err);
				},
				beforeSelectRow : function(rowid, e) {
					// this blank function will not select the entire row. Only checkbox can be selectable.
					var $grid = $("#condition-list");
					if ($("#jqg_condition-list_" + $grid.jqGrid('getCell', rowid, 'id')).is(':checked')) {
						if (ckConditionSelected.indexOf($grid.jqGrid('getCell',rowid, 'id')) == -1) {
							ckConditionSelected.push($grid.jqGrid('getCell',rowid,'id'));
						}
					} else {
						if (ckConditionSelected.indexOf($grid.jqGrid('getCell', rowid,'id')) != -1) {
							ckConditionSelected.splice(ckConditionSelected.indexOf($grid.jqGrid('getCell',rowid,'id')),1);
						}
					}
					return false;
				},
				recordtext : "<spring:message code='serverManagement.grid.pager.total.records.text'></spring:message>",
				emptyrecords : "<spring:message code='serverManagement.grid.empty.records'></spring:message>",
				loadtext : "<spring:message code='serverManagement.grid.loading.text'></spring:message>",
				pgtext : "<spring:message code='serverManagement.grid.pager.text'></spring:message>"
			}).navGrid("#policyruleConditionPagingDiv", {
			edit : false,
			add : false,
			del : false,
			search : false
		});
		$(".ui-jqgrid-titlebar").hide();
		resizeConditionGrid();
	
	});
	
	function reloadConditionGridDataWithClearMsgRule() {
		clearAllMessages();
		clearResponseMsgDiv();
		clearResponseMsg();
		clearErrorMsg();

		reloadConditionGridDataRule();
	}
	
	function reloadConditionGridDataRule(){
		
		var $grid = $("#condition-list");
		jQuery('#condition-list').jqGrid('clearGridData');
		clearConditionInstanceGrid();
		$grid.jqGrid('setGridParam',{datatype:'json',page: 1,sortname: 'id',sortorder: "desc", postData:{
				
			'serverInstanceId' : function() {
				return ${instanceId};
			},
      	
			'policyConditionName': function () {
    			var id  =  $("#search_policyName").val();
    	        return id;
	    		},
	    		
	    		'policyConditionDesc': function(){
	    			return $("#search_policyDescription").val();
	    		}
	    		
	    		
		}}).trigger('reloadGrid');
	} 
	
	function clearConditionInstanceGrid() {
		var $grid = $("#condition-list");
		var rowIds = $grid.jqGrid('getDataIDs');
		// iterate through the rows and delete each of them
		for (var i = 0, len = rowIds.length; i < len; i++) {
			var currRow = rowIds[i];
			$grid.jqGrid('delRowData', currRow);
		}
	}
	
	function resizeConditionGrid() {
		var $grid = $("#condition-list"), newWidth = $grid.closest(".ui-jqgrid").parent().width();
		if (newWidth < 1000) {
			newWidth = 800;
		}
	}

	function clearConditionGrid() {
		var $grid = $("#condition-list");
		var rowIds = $grid.jqGrid('getDataIDs');
		// iterate through the rows and delete each of them
		for (var i = 0, len = rowIds.length; i < len; i++) {
			var currRow = rowIds[i];
			$grid.jqGrid('delRowData', currRow);
		}
	}
	
	function ConditionColumnFormatter(cellvalue, options, rowObject) {
		if(rowObject["type"] == 'dynamic'){
			return rowObject["value"];
		}
		if( rowObject["type"] == 'expression'){
			return rowObject["expression"];
		}else{
			return rowObject["action"];
		}
	}
	
	var conditionRowCount = 0;
	function addConditionToGrid() {
		var ckIntanceSelected = [];
	    /*
		$.each($("input[name='conditionCheckbox']:checked"), function(){
	    	rowData = $("#condition-list").jqGrid("getRowData", $(this).val());
	    	ckIntanceSelected.push(rowData);
	    });
		var conditionRowCount = 0;
		for (i = 0, n = ckIntanceSelected.length; i < n; i++) {
			conditionRowCount = conditionRowCount + 1;
		}

		$("#policyConditionGrid").jqGrid('addRowData', conditionRowCount,	ckIntanceSelected);
		*/
		for(var i=0;i < checkedConditionListData.length ; i++){
			ckIntanceSelected.push( checkedConditionListData[i] ) ;			
		}
		var conditionRowCount = 0;
		for (i = 0, n = checkedConditionListData.length; i < n; i++) {
			conditionRowCount = conditionRowCount + 1;
		}
		// $("#policyConditionGrid").jqGrid('addRowData', conditionRowCount,	ckIntanceSelected);
		
		checkedConditionListData = new Array();
		checkedConditionList = new Array();		
		
		//$("#policyConditionGrid").jqGrid('setGridParam',{datatype:'json',page: 1}).trigger('reloadGrid');
		// jQuery("#policyConditionGrid").trigger("reloadGrid");
		loadJQueryPolicyConditionGrid();
		$("#policyConditionGrid").jqGrid('addRowData', conditionRowCount,	ckIntanceSelected);
		
		closeFancyBox();
	}
	function pushConditionInArray(id,element){
		
		if(element.checked){
			ckConditionSelected_glo.push(id);
		}else{
			ckConditionSelected_glo.splice(ckConditionSelected_glo.indexOf(id),1);
		}
		
	}
	
	function conditionHeaderCheckbox(e, element) {
	    e = e || event;/* get IE event ( not passed ) */
	    e.stopPropagation ? e.stopPropagation() : e.cancelBubble = false;

	    //if root checkbox is checked :-> all child checkbox should ne checked
	    if(element.checked) {
	    	$('input:checkbox[name="conditionCheckbox"]').prop('checked',true);
	    	
	    	$('input:checkbox[name="conditionCheckbox"]').each(function( index, element ) {
	    	       pushCheckedConditionInArray(element);
	    	  });
	    } else {
	    	//if root checkbox is unchecked :-> all child checkbox should be unchecked
	    	$('input:checkbox[name="conditionCheckbox"]').prop('checked',false);
	    	
	    	$('input:checkbox[name="conditionCheckbox"]').each(function( index, element ) {
	    		   pushCheckedConditionInArray(element);
	    	  });
	    }
	    
	}
	
	function updateRootCheckbox(element, gridId) {
		if(!element.checked){
			// if current child checkbox is not checked : uncheck root checkbox
			$('input:checkbox[id="selectAllConditions"]').prop('checked',false);
		} else {
			//if current child checkboox is checked and all checkbox are checked then check root checkbox
			var count = jQuery("#"+gridId).jqGrid('getGridParam', 'records');
			if ($('input:checkbox[name="conditionCheckbox"]:checked').length === count) {
				$('input:checkbox[id="selectAllConditions"]').prop('checked',true);
		    }
		}
	}

	function pushCheckedConditionInArray(element){
		var id = element.value;		
		
		
		if(element.checked && ($.inArray( id+"" ,  checkedConditionList ) == -1) ){			
			checkedConditionList.push(id +"");			
			checkedConditionListData.push( $("#condition-list").jqGrid("getRowData", id ) );
		}else{
			checkedConditionList.splice(checkedConditionList.indexOf(id),1);
			checkedConditionListData.splice( checkedConditionListData.indexOf( $("#condition-list").jqGrid("getRowData", id ) ),1 );
		}		
		
	}
	
	function conditionCheckboxFormatter(cellvalue, options, rowObject){
		if ($.inArray( rowObject["id"]+"" ,  checkedConditionList ) != -1)
		{
			//alert(rowObject["name"]);
			return '<input type="checkbox" id="'+rowObject["name"]+'_cond_select_lnk" name="conditionCheckbox" value="'+rowObject["id"]+'" onchange="pushCheckedConditionInArray(this);updateRootCheckbox(this, \'condition-list\');" checked />';
		}
		else{ 
		   return '<input type="checkbox" id="'+rowObject["name"]+'_cond_select_lnk" name="conditionCheckbox" value="'+rowObject["id"]+'" onchange="pushCheckedConditionInArray(this);updateRootCheckbox(this, \'condition-list\');"/>';
		}
	}
</script>
</head>
<body>
	<div class="modal-content">
		<div class="modal-header padding10">
			<h4 class="modal-title">
				<i class="icon-hdd"></i>
				<spring:message code="business.policy.condition.existing.list.section.title" ></spring:message>
			</h4>
		</div>
		<div id="conditionContentDiv" class="modal-body padding10 inline-form">
			


<div class="fullwidth borbot">
        	 
         	<div class="col-md-6 inline-form" style="padding-left: 0px !important;">
         	
         		<div class="form-group">
	         		<spring:message code="business.policy.rule.condition.grid.column.name"  var="label"></spring:message>
	         		<spring:message code="business.policy.rule.condition.grid.column.name"  var="tooltip"></spring:message>
	         		
	         		<div class="table-cell-label">${label }</div>
	             	<div class="input-group ">
	             		<input type="text" id="search_policyName"  name="search_policyName" maxlength="100" class="form-control table-cell input-sm" data-toggle="tooltip" data-placement="bottom" title="${tooltip }"  >
	             		<span class="input-group-addon add-on last" > <i id="policyName_err" class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="left" title=""></i></span> 
	             		
	             	</div>
	             </div>
	            </div> 
	             
	             
	         	<div class="col-md-6 inline-form" style="padding-left: 0px !important;">
	             <div class="form-group">
	         		<spring:message code="business.policy.rule.grid.column.description"  var="label"></spring:message>
	         		<spring:message code="business.policy.rule.grid.column.description"  var="tooltip"></spring:message>
	         		
	         		<div class="table-cell-label">${label }</div>
	             	<div class="input-group ">
	             		<input type="text" id="search_policyDescription"  name="search_policyDescription" maxlength="100" class="form-control table-cell input-sm" data-toggle="tooltip" data-placement="bottom" title="${tooltip }"  >
	             		<span class="input-group-addon add-on last" > <i id="policyDescription_err" class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="left" title=""></i></span> 
	             		
	             	</div>
	             </div>
	             
         	</div>
         	<div class="clearfix"></div>
     		<div class="pbottom15 form-group ">
     			
   					<button id="searchPolicyBtn" class="btn btn-grey btn-xs" onclick="reloadConditionGridDataWithClearMsgRule();"><spring:message code="btn.label.search" ></spring:message></button>
   					
   			</div>
   			
   		</div>       

			<div class="box-body table-responsive no-padding box">
				<table class="table table-hover" id="condition-list"></table>
				<div class="clearfix"></div>
				<div id="divLoading" align="center" style="display: none;">
					<img src="img/preloaders/Preloader_10.gif" />
				</div>
			</div>

		</div>
		<div id="buttons-div" class="modal-footer padding10 text-left">
			<button type="button" class="btn btn-grey btn-xs "
				onclick="addConditionToGrid();" id="select_condition_grid_ok_btn">
				<spring:message code="btn.label.select" ></spring:message>
			</button>
			<button type="button" class="btn btn-grey btn-xs "
				onclick="closeFancyBox();" id="select_condition_grid_close_grid_btn">
				<spring:message code="btn.label.close" ></spring:message>
			</button>
		</div>

	</div>
	<div class="box-body table-responsive no-padding box">
		<table class="table table-hover" id="policyConditionGrid"></table>
		<div id="policyruleConditionPagingDiv"></div>
		<div class="clearfix"></div> 
	    <div id="divLoading" align="center" style="display: none;"><img src="img/preloaders/Preloader_10.gif" /></div>
	</div>
	<!-- /.modal-content -->

</body>
</html>		
	
