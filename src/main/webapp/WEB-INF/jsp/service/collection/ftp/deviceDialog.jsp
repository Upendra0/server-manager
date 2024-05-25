<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<%@page import="com.elitecore.sm.util.MapCache"%>
<%@page import="com.elitecore.sm.common.constants.SystemParametersConstant"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ page contentType="text/html; charset=UTF-8" %>

	<a href="#divDeviceList" class="fancybox" style="display: none;" id="deviceList">#</a>
	<div id="divDeviceList" style=" width:100%; display:none;" >
		    <div class="modal-content" style="overflow:hidden">
				<div class="modal-header padding10">
					<h4 class="modal-title">
						<i class="icon-hdd"></i> 
						<spring:message code="device.tab.heading.label" ></spring:message>
					</h4>
				</div>
				<div id="deviceContentDiv" class="modal-body" >
					
<div >
					<label id="lblCounterForDeviceName" style="display:none;"></label>
					<label id="lblDeviceid" style="display:none;"></label>
					<span class="title2rightfield">
					 	<span class="title2rightfield-icon1-text" style="font-size: 12px;"> 
					 		<!-- <a href="#" onclick="createPolicy();"  ><spring:message code="business.policy.create.label" ></spring:message></a>  --> 
						</span>
					</span>
				</div>
					<div class="fullwidth table-responsive " style="height:auto;max-height:300px;overflow: auto;">
					<table class="table table-hover" id="devicelistgrid"></table>
					<div id="deviceGridPagingDiv"></div>
					</div>
				
			</div>
			<div id="buttons-div" class="modal-footer padding10 text-left">
					<div id="buttons-div" class="modal-footer padding10 text-left">
					<button type="button" class="btn btn-grey btn-xs " id ="selectDevicebtn">
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
		getDeviceListBySearchParams('<%=ControllerConstants.GET_DEVICE_LIST_BY_DECODETYPE%>');
	});
	
	function getDeviceListBySearchParams(urlAction) {
	
		var instanceid = '${instanceId}';
		$("#devicelistgrid").jqGrid({
			url: urlAction,
			datatype: "json",
			postData : {
				'decodeType' : 'UPSTREAM'
				
			},
			colNames: [
						  "device id",
			           	  "Device Name",
			           	  "Device Type",
			           	  "Vendor Name"
				],
			colModel: [
				{name:'id',index:'id',sortable:true, hidden:true},
				{name:'deviceName',index:'deviceName',  sortable:true},
				{name:'deviceType',index:'deviceType',  sortable:true},
				{name:'vendorType',index:'vendorType',  sortable:true},
			],
			rowNum : 15,
			rowList : [ 10, 20, 60, 100 ],
			height : 'auto',
			mtype : 'POST',
			contentType : "application/json; charset=utf-8",
			loadtext : "Loading...",
			sortname : 'id',
			sortorder : "desc",
			pager: "#deviceGridPagingDiv",
			viewrecords: true,
			multiselect: true,
			beforeSelectRow: function(rowid, e)
			{
			    $("#devicelistgrid").jqGrid('resetSelection');
			    return (true);
			},
			beforeRequest : function() {
			    $('input[id=cb_devicelistgrid]', 'div[id=jqgh_devicelistgrid_cb]').remove();
			},
			loadComplete : function () {
				var rowId = jQuery('#devicelistgrid tr:eq(0)').attr('id');
				var dataFromTheRow = jQuery('#devicelistgrid').jqGrid ('getRowData', rowId);
				
				$('input[id^="jqg_devicelistgrid_"]').each(function(index){
					var deviceName = dataFromTheRow[index].deviceName;
					var deviceID = dataFromTheRow[index].id;
					deviceName.rep
					$(this).attr("id",deviceID +"_devicechkbox");
					$(this).attr("name","radio_grp");
					$(this).val(deviceName);
					$(this).attr("type","radio");
					$(this).attr("deviceName","radio_grp");
				});
			},
			onSelectRow : function(id){
				rowData = $("#devicelistgrid").jqGrid("getRowData", id);
				console.log()
			},
			timeout : 120000,
		    loadtext: "Loading...",
			loadError : function(xhr,st,err) {
				handleGenericError(xhr,st,err);
			},
			recordtext: "<spring:message code="serverManagement.grid.pager.total.records.text"></spring:message>",
		    emptyrecords: "No Record Found.",
		    viewrecords: true,
			}).navGrid("#deviceGridPagingDiv",{edit:false,add:false,del:false,search:false});
			$(".ui-jqgrid-titlebar").hide();
			$(".ui-pg-input").height("10px");
			
			if ($("#devicelistgrid").getGridParam("reccount") === 0) {
				  $(".ui-paging-info").html("No Record Found.");
			}   
	}

	$('#selectDevicebtn').click(function(){
		//  var counter = $('#lblCounter').text();
		  var counter = $('#lblCounterForDeviceName').text();
		  selectDevice(counter);

	});

	function selectDevice(pathListCounter){
		if(rowData){
			$("#"+pathListCounter + "_deviceId").val(rowData.id);
			$("#"+pathListCounter + "_parentDevice").val(rowData.deviceName);
		}else{
			$("#"+pathListCounter + "_deviceId").val("");
			$("#"+pathListCounter + "_parentDevice").val("");
		}
		closeFancyBox();
	}

	function selectDevicePopUp(pathListCounter){
		//$("#lblCounter").text(pathListCounter);		
		$("#lblCounterForDeviceName").text(pathListCounter);
		$('input[id$="_devicechkbox"]').each(function(index){
			$(this).prop("checked",false);
		});

		//IF Device Already Selected then Select it in Popup Grid
		if($("#"+pathListCounter + "_deviceId").val()){
			jQuery("#devicelistgrid").resetSelection();
			$('#devicelistgrid tr').each(function() {
			    $.each(this.cells, function(){
			    	
			       if($(this).attr("aria-describedby") && $(this).attr("aria-describedby") == 'devicelistgrid_id'){
			    	   $("#"+pathListCounter + "_deviceId").val();
			    	   if($(this).text() == $("#"+pathListCounter + "_deviceId").val()){
			    		   var deviceName = $(this).next().text();
			    		   console.log("deviceName:::" + deviceName);
			    		   $("#"+$("#"+pathListCounter + "_deviceId").val()+"_devicechkbox").prop('checked', true);
			    		   jQuery("#devicelistgrid").setSelection($(this).parent()[0].id, true);
			    		   return;
			    	   }
			       }
			    });
			});
		}
		$("#deviceList").click();
	}


	</script>
