<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<%@page import="com.elitecore.sm.util.MapCache"%>
<%@page import="com.elitecore.sm.common.constants.SystemParametersConstant"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ page contentType="text/html; charset=UTF-8" %>

	<a href="#divPolicyList" class="fancybox" style="display: none;" id="policyList">#</a>
	<div id="divPolicyList" style=" width:100%; display:none;" >
		    <div class="modal-content" style="overflow:hidden">
				<div class="modal-header padding10">
					<h4 class="modal-title">
						<i class="icon-hdd"></i> 
						<spring:message code="business.policy.grid.heading" ></spring:message>
					</h4>
				</div>
				<div id="policyContentDiv" class="modal-body" >
					
				<div >
					<label id="lblCounter" style="display:none;"></label>
					<label id="lblPolicyid" style="display:none;"></label>
					<span class="title2rightfield">
					 	<span class="title2rightfield-icon1-text" style="font-size: 12px;"> 
					 		<!-- <a href="#" onclick="createPolicy();"  ><spring:message code="business.policy.create.label" ></spring:message></a>  --> 
						</span>
					</span>
				</div>

					<div class="fullwidth table-responsive " style="height:auto;max-height:300px;overflow: auto;">
					<table class="table table-hover" id="policylistgrid"></table>
					<div id="policyGridPagingDiv"></div>
					</div>
				
			</div>
			<div id="buttons-div" class="modal-footer padding10 text-left">
					<div id="buttons-div" class="modal-footer padding10 text-left">
					<button type="button" class="btn btn-grey btn-xs " id ="selectPolicybtn">
						<spring:message code="btn.label.select" ></spring:message>
					</button>
					<button onClick="closeFancyBoxFromChildIFrame();" class="btn btn-grey btn-xs ">
						<spring:message code="btn.label.cancel" ></spring:message>
					</button>
				</div>
				</div>
			</div>
			<!-- /.modal-content -->
	</div>
	
	<script lang="javascript">
	
	$(document).ready(function() {
		loadJQueryPolicyListGrid();
	});
	var currentActionType = '${REQUEST_ACTION_TYPE}';
	var rowData = null;
	function loadJQueryPolicyListGrid(){
	
		var instanceid = '${instanceId}';
		$("#policylistgrid").jqGrid({
			url: "<%= ControllerConstants.GET_PROCESSING_POLICY_LIST%>",
			datatype: "json",
			postData: {instanceId: instanceid, actionType:currentActionType},
			colNames: [
						  "<spring:message code='processingService.summary.grid.policy.id' ></spring:message>",
			           	  "<spring:message code='processingService.policy.grid.policy.name' ></spring:message>",
			              "<spring:message code='processingService.policy.grid.policy.description' ></spring:message>",
				],
			colModel: [
				{name:'policyId',index:'id',sortable:true, hidden:true},
				{name:'policyName',index:'policyName',  sortable:false},
				{name:'description',index:'description', sortable:false},
			],
			rowNum:15,
			rowList:[10,20,60,100],
			height: 'auto',
			sortname: 'id',
			sortorder: "desc",
			//pager: "#policyGridPagingDiv",
			viewrecords: true,
			multiselect: true,
			beforeSelectRow: function(rowid, e)
			{
			    $("#policylistgrid").jqGrid('resetSelection');
			    return (true);
			},
			beforeRequest : function() {
			    $('input[id=cb_policylistgrid]', 'div[id=jqgh_policylistgrid_cb]').remove();
			},
			loadComplete : function () {
				var rowId = jQuery('#policylistgrid tr:eq(0)').attr('id');
				var dataFromTheRow = jQuery('#policylistgrid').jqGrid ('getRowData', rowId);
				$('input[id^="jqg_policylistgrid_"]').each(function(index){
					var policyName = dataFromTheRow[index].policyName;
					$(this).attr("id", policyName + "_chkbox");
					$(this).attr("type","radio");
					$(this).attr("name","radio_grp");
				});
		
			    //$('input[id^="jqg_policylistgrid_"]').attr("type", "radio");
			},
			onSelectRow : function(id){
				rowData = $("#policylistgrid").jqGrid("getRowData", id);
			},
			timeout : 120000,
		    loadtext: "Loading...",
			caption: "<spring:message code="collectionService.summary.agent.grid.caption"></spring:message>",
			loadError : function(xhr,st,err) {
				handleGenericError(xhr,st,err);
			},
			recordtext: "<spring:message code="serverManagement.grid.pager.total.records.text"></spring:message>",
		    emptyrecords: "No Record Found.",
		    viewrecords: true,
			}).navGrid("#policyGridPagingDiv",{edit:false,add:false,del:false,search:false});
			$(".ui-jqgrid-titlebar").hide();
			$(".ui-pg-input").height("10px");
			
			if ($("#policylistgrid").getGridParam("reccount") === 0) {
				  $(".ui-paging-info").html("No Record Found.");
			}   
	}

	$('#selectPolicybtn').click(function(){
		  var counter = $('#lblCounter').text();
		  selectPolicy(counter);

	});

	function selectPolicy(pathListCounter){
		if(rowData){
			$("#"+pathListCounter + "_policyId").val(rowData.policyId);
			$("#"+pathListCounter + "_policy").val(rowData.policyName);
		}else{
			$("#"+pathListCounter + "_policyId").val("");
			$("#"+pathListCounter + "_policy").val("");
		}
		closeFancyBox();
	}

	function selectPolicyPopUp(pathListCounter){
		$("#lblCounter").text(pathListCounter);
		$('input[id$="_chkbox"]').each(function(index){
			$(this).prop("checked",false);
		});
		
		//IF Policy Already Selected then Select it in Popup Grid
		if($("#"+pathListCounter + "_policyId").val()){
			jQuery("#policylistgrid").resetSelection();
			$('#policylistgrid tr').each(function() {
			    $.each(this.cells, function(){
			       if($(this).attr("aria-describedby") && $(this).attr("aria-describedby") == 'policylistgrid_policyId'){
			    	   if($(this).text() == $("#"+pathListCounter + "_policyId").val()){
			    		   var policyName = $(this).next().text();
			    		   console.log("policyName:::" + policyName);
			    		   $("[id='"+policyName+"_chkbox']").prop('checked', true);
			    		   jQuery("#policylistgrid").setSelection($(this).parent()[0].id, true);
			    		   return;
			    	   }
			       }
			    });
			});
		}
		$("#policyList").click();
	}

	function createPolicy(){
		$("#service_detail_form").attr("action","<%=ControllerConstants.INIT_CREATE_POLICY%>");
		$("#service_detail_form").submit();
	}

	</script>
