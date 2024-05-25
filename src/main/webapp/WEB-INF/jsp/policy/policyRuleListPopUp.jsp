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
		
	var ckRuleSelected = new Array();
	var gridDataArr = [];
	var ruleList = {};
	var existingRuleIds = new Array();
		
	$(document).ready(function() {
		
		$("#rule-list").jqGrid({
		      	url: "<%=ControllerConstants.GET_POLICY_RULE_LIST%>",
				postData : {
					ruleName: function () {
		   	        	return $("#search_ruleName").val();
		    		},
		    		description: function () {
		   	        	return $("#search_ruleDescription").val();
		    		},
					serverInstanceId : function() {
						return ${instanceId};
					},
		    		reasonCategory: function () {
		    			return '';
		    		},
		    		reasonSeverity: function () {
		    			return '';
		    		},
		    		reasonErrorCode: function () {
		    			return '';
		    		}
				},
				datatype : "json",
				colNames : [
						"<spring:message code='policymgmt.rulegroup.id' ></spring:message>",
						"<spring:message code='business.policy.rule.grid.column.ruleName' ></spring:message>",
						"<spring:message code='business.policy.rule.grid.column.description' ></spring:message>", ],
				colModel : [ {name : 'id', index : 'id', sortable : true, hidden : true	}, 
				             {name : 'name', index : 'name', sortable : true}, 
				             {name : 'description',	index : 'description', sortable : false} ],
				rowNum : 500,
				rowList : [],
				height : 'auto',
				sortname : 'id',
				sortorder : "desc",
				viewrecords : true,
				multiselect : true,
				timeout : 120000,
				loadtext : "Loading...",
				pager: "#ruleListPager",
				autowidth: true,			
			    pgbuttons: false,     // disable page control like next, back button
				caption : "<spring:message code='business.policy.rule.grid.heading'></spring:message>",
				beforeRequest:function(){
				ckRuleSelected = new Array();
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
					if(existingRuleIds.length > 0) {
						var $grid = $("#rule-list");
						var rowIds = $grid.jqGrid('getDataIDs');
						for (i = 0; i < rowIds.length; i++) {//iterate over each row
					    	for(j = 0; j < existingRuleIds.length; j++) {
					    		if (rowIds[i] == existingRuleIds[j]) {
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
					clearRuleGrid();
				},
				loadError : function(xhr, st, err) {
					handleGenericError(xhr, st, err);
				},
				beforeSelectRow : function(rowid, e) {
					// this blank function will not select the entire row. Only checkbox can be selectable.
					var $grid = $("#rule-list");
					if ($("#jqg_rule-list_" + $grid.jqGrid('getCell', rowid, 'id')).is(':checked')) {
						if (ckRuleSelected.indexOf($grid.jqGrid('getCell',rowid, 'id')) == -1) {
							ckRuleSelected.push($grid.jqGrid('getCell',rowid,'id'));
						}
					} else {
						if (ckRuleSelected.indexOf($grid.jqGrid('getCell', rowid,'id')) != -1) {
							ckRuleSelected.splice(ckRuleSelected.indexOf($grid.jqGrid('getCell',rowid,'id')),1);
						}
					}
					return false;
				},
				onSelectAll : function(id, status) {
					if (status == true) {
						ckRuleSelected = new Array();
						for (i = 0; i < id.length; i++) {
							ckRuleSelected.push(id[i]);
						}
					} else {
						ckRuleSelected = new Array();
					}
				},
				recordtext : "<spring:message code='serverManagement.grid.pager.total.records.text'></spring:message>",
				emptyrecords : "<spring:message code='serverManagement.grid.empty.records'></spring:message>",
				loadtext : "<spring:message code='serverManagement.grid.loading.text'></spring:message>",
				pgtext : null
			}).navGrid("#ruleListPager", {
			edit : false,
			add : false,
			del : false,
			search : false
		});
		$(".ui-jqgrid-titlebar").hide();
		resizeRuleGrid();
	
	});
	
	function reloadRuleGridDataWithClearMsgRule() {
		clearAllMessages();
		clearResponseMsgDiv();
		clearResponseMsg();
		clearErrorMsg();

		reloadRuleGridDataRule();
	}
	
	function reloadRuleGridDataRule(){
		
		var $grid = $("#rule-list");
		jQuery('#rule-list').jqGrid('clearGridData');
		clearRuleInstanceGrid();
		$grid.jqGrid('setGridParam',{datatype:'json',page: 1,sortname: 'id',sortorder: "desc", postData:{
				
			ruleName: function () {
   	        	return $("#search_ruleName").val();
    		},
    		description: function () {
   	        	return $("#search_ruleDescription").val();
    		},
			serverInstanceId : function() {
				return ${instanceId};
			}
	    		
	    		
		}}).trigger('reloadGrid');
	} 
	
	function clearRuleInstanceGrid() {
		var $grid = $("#condition-listrule-list");
		var rowIds = $grid.jqGrid('getDataIDs');
		// iterate through the rows and delete each of them
		for (var i = 0, len = rowIds.length; i < len; i++) {
			var currRow = rowIds[i];
			$grid.jqGrid('delRowData', currRow);
		}
		
	}

	function resizeRuleGrid() {
		var $grid = $("#rule-list"), newWidth = $grid.closest(".ui-jqgrid").parent().width();
		if (newWidth < 1000) {
			newWidth = 800;
		}
	}

	function clearRuleGrid() {
		var $grid = $("#rule-list");
		var rowIds = $grid.jqGrid('getDataIDs');
		// iterate through the rows and delete each of them
		for (var i = 0, len = rowIds.length; i < len; i++) {
			var currRow = rowIds[i];
			$grid.jqGrid('delRowData', currRow);
		}
	}
	
	var ruleRowCount = 0;
	function addRuleToGrid() {
		var selectedRuleArray = new Array();
		var i, selRowIds = $("#rule-list").jqGrid("getGridParam", "selarrrow"), n, rowData;
		for (i = 0, n = ckRuleSelected.length; i < n; i++) {
			ruleRowCount = ruleRowCount + 1;
		    rowData = $("#rule-list").jqGrid("getRowData", ckRuleSelected[i]);
		    selectedRuleArray.push(rowData);
		}

		$("#policyRuleGrid").jqGrid('addRowData', ruleRowCount,	selectedRuleArray);
		selectedRulesCk = [];
		ckRuleSelected = [];
		existingRuleIds = [];
		loadJQueryPolicyRuleGrid();
		closeFancyBox();
	}
</script>
</head>
<body>
	<div class="modal-content">
		<div class="modal-header padding10">
			<h4 class="modal-title">
				<i class="icon-hdd"></i>
				<spring:message code="business.policy.rule.grid.heading" ></spring:message>
			</h4>
		</div>
		<div id="ruleContentDiv" class="modal-body padding10 inline-form">
			<jsp:include page="../common/responseMsg.jsp" ></jsp:include>

	<div class="fullwidth borbot">
        	 
         	<div class="col-md-6 inline-form" style="padding-left: 0px !important;">
         	
         		<div class="form-group">
	         		<spring:message code="business.policy.create.rule.name.label"  var="label"></spring:message>
	         		<spring:message code="business.policy.create.rule.name.label"  var="tooltip"></spring:message>
	         		
	         		<div class="table-cell-label">${label }</div>
	             	<div class="input-group ">
	             		<input type="text" id="search_ruleName"  name="search_ruleName" maxlength="100" class="form-control table-cell input-sm" data-toggle="tooltip" data-placement="bottom" title="${tooltip }"  >
	             		<span class="input-group-addon add-on last" > <i id="ruleName_err" class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="left" title=""></i></span> 
	             		
	             	</div>
	             </div>
	            </div> 
	             
	             
	         	<div class="col-md-6 inline-form" style="padding-left: 0px !important;">
	             <div class="form-group">
	         		<spring:message code="business.policy.rule.grid.column.description"  var="label"></spring:message>
	         		<spring:message code="business.policy.rule.grid.column.description"  var="tooltip"></spring:message>
	         		
	         		<div class="table-cell-label">${label }</div>
	             	<div class="input-group ">
	             		<input type="text" id="search_ruleDescription"  name="search_ruleDescription" maxlength="100" class="form-control table-cell input-sm" data-toggle="tooltip" data-placement="bottom" title="${tooltip }"  >
	             		<span class="input-group-addon add-on last" > <i id="ruleDescription_err" class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="left" title=""></i></span> 
	             		
	             	</div>
	             </div>
	             
         	</div>
         	<div class="clearfix"></div>
     		<div class="pbottom15 form-group "> 
     			
   					<button id="searchRuleBtn" class="btn btn-grey btn-xs" onclick="reloadRuleGridDataWithClearMsgRule();"><spring:message code="btn.label.search" ></spring:message></button>

   			</div>
   			
   		</div>       

			<div class="box-body table-responsive no-padding box">
				<table class="table table-hover" id="rule-list"></table>
					<div id="ruleListPager"></div>
				<div class="clearfix"></div>
				<div id="divLoading" align="center" style="display: none;">
					<img src="img/preloaders/Preloader_10.gif" />
				</div>
			</div>

		</div>
		<div id="buttons-div" class="modal-footer padding10 text-left">
			<button type="button" class="btn btn-grey btn-xs "
				onclick="addRuleToGrid();" id="select_rulelist_grid_ok_btn">
				<spring:message code="btn.label.select" ></spring:message>
			</button>
			<button type="button" class="btn btn-grey btn-xs "
				onclick="closeFancyBox();" id="select_rulelist_grid_close_btn">
				<spring:message code="btn.label.close" ></spring:message>
			</button>
		</div>

	</div>
	<!-- /.modal-content -->

</body>
</html>		
	
