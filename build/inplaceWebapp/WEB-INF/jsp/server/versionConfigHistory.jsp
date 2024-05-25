<%@page import="com.elitecore.sm.common.constants.BaseConstants"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="/WEB-INF/ShowErrorTag.tld" prefix="elitecoreError"%>
<%@page
	import="com.elitecore.sm.common.constants.SystemParametersConstant"%>
<%@page import="com.elitecore.sm.util.MapCache"%>
<style type="text/css">
.ui-jqgrid .ui-jqgrid-bdiv { overflow-y: scroll ;max-height: 420px;}
</style>
<script type="text/javascript">
var checkedVersionConfig = [];
var checkedVersionConfigName = [];
var serverInstance_Id;
var versionConfigId;
$(document).ready(function() {
	$('#configHistoryDiv').hide();
	if('${server_instance_form_bean.id}' != null && '${server_instance_form_bean.id}' != ''){
		   serverInstance_Id = '${server_instance_form_bean.id}';
	}else if('${serverInstanceId}' != null && '${serverInstanceId}' !=''){
		   serverInstance_Id = '${serverInstanceId}';
	}
	getVersionConfigDetail(serverInstance_Id);
	var isRightGiven ='<sec:authorize access="hasAnyAuthority(\'COMPARE_VERSION_CONFIGURATION\')">'+  + '</sec:authorize>';
	if(isRightGiven==''){
		$("#configHistoryList").hideCol('checkbox').setGridWidth(700);
	}else{
		$("#configHistoryList").showCol('checkbox');
	}
});
function versionNameFormatter(cellvalue, options, rowObject){
	return '<a class="link" id="versionConfig_' + rowObject["name"] + '" href="#" onclick="downloadVersionConfigXML('+"'" + rowObject["id"]+ "'"+')"> '+ rowObject["name"] +' </a>'
}
function downloadVersionConfigXML(id){
	$("#sid").val(id);
	$("#downloadVersionConfigXML").submit();
}
function openRestoreSyncPopup(id){
	versionConfigId = id;
	$("#restoreSync").click();
	 
}
function upgradeFormatter(cellvalue, options, rowObject){
	  var isRightGiven ='<sec:authorize access="hasAnyAuthority(\'RESTORE_SYNC_VERSION\')">'+  + '</sec:authorize>';
      if(isRightGiven==''){
	      return 'Restore & Sync'
	 }else{
		 return '<a class="link" id="' + rowObject["id"] + '" href="#" onclick="openRestoreSyncPopup('+rowObject["id"]+')"> Restore & Sync </a>'
	 }
}
function configHistoryCheckboxFormatter(cellvalue, options, rowObject) {
	var uniqueId = getUniqueCheckboxIdForAttribute(rowObject);
	return '<input type="checkbox" id="'+uniqueId+'" name="attributeCheckbox" value="'+rowObject["id"]+'" onchange="updateRootCheckbox('+"'" + uniqueId + "'"+',this, \'configHistoryList\','+"'"+rowObject["id"]+"'"+','+"'"+rowObject["name"]+"'"+')"/>';
}
function getUniqueCheckboxIdForAttribute(rowObject) {
	var uniqueId = "checkbox";
	if(rowObject["name"] && rowObject["name"] !== 'null' && rowObject["name"] !== "") {
		uniqueId += "_" + rowObject["name"];
	}
	return uniqueId;
}

function updateRootCheckbox(uniqueId,element, gridId, versionConfigId, versionConfigName) {
		var count = jQuery("#"+gridId).jqGrid('getGridParam', 'records');
		if($('input:checkbox[name="attributeCheckbox"]:checked').length > 2){
			$('input:checkbox[id='+"'" + uniqueId + "'"+']').prop('checked',false);
			$("#checkboxServer").click();
		} else {
			if($('input:checkbox[id='+"'" + uniqueId + "'"+']').prop('checked')==true){
				checkedVersionConfig.push(versionConfigId);	
				checkedVersionConfigName.push(versionConfigName);
			} else {
				checkedVersionConfig.pop(versionConfigId);
				checkedVersionConfigName.pop(versionConfigName);
			}
		}
}

function reloadVersionConfigDetail(serverInstance_Id){
	
	$.ajax({
		url: "<%=ControllerConstants.GET_VERSION_CONFIG_DETAIL%>",
		cache: false,
		async: false,
    	data:
    		{
    		 serverInstanceId :serverInstance_Id,	  
    		},
		dataType: 'json',
		type: "POST",
		success: function(data){	
			var response = eval(data);	
			var gridArray = response.rows;
			var responseCode = response.code; 
			var responseMsg = response.msg; 
			var responseObject = response.object;		
			$("#configHistoryDiv").show();
			jQuery("#configHistoryList").jqGrid('setGridParam', {datatype: 'local', data:gridArray }).trigger("reloadGrid");
			/* if(responseCode == "200"){				
				$("#configHistoryDiv").show();				
				var attributes=eval(responseObject);				
				var versionConfigList=attributes['attributeList'];
				if(versionConfigList!=null && versionConfigList != 'undefined' && versionConfigList != undefined){					
					var gridArray = [];
					$.each(versionConfigList,function(index,versionConfig){		 				
		 				var rowData = {};
		 				rowData.id					= parseInt(versionConfig.id);
		 				rowData.checkbox			= versionConfig.checkbox;
		 				rowData.description			= versionConfig.description;
		 				rowData.description			= versionConfig.description;
		 				rowData.publishedby			= versionConfig.publishedby;
		 				gridArray.push(rowData);
		 			});
					jQuery("#configHistoryList").jqGrid('setGridParam', {datatype: 'local', data:gridArray }).trigger("reloadGrid");
				} 
				$('.fancybox-wrap').css('top','10px');
			}else if(responseObject != undefined && responseObject != 'undefined' && responseCode == "400"){
				console.log("got 400 response.");
			} */
		},
	    error: function (xhr,st,err){	    		    	
			handleGenericError(xhr,st,err);
		}
	});
}

function getVersionConfigDetail(serverInstance_Id){
	$('#configHistoryDiv').show();
	$("#configHistoryList").jqGrid({
    	url: "<%=ControllerConstants.GET_VERSION_CONFIG_DETAIL%>",
    	postData: {
    		serverInstanceId :serverInstance_Id,
    	},
							datatype : "json",
							colNames : [
								    "#",
								    "<spring:message code='versionconfig.grid.column.id' ></spring:message>",
									"<spring:message code='versionconfig.grid.column.version' ></spring:message>",
									"<spring:message code='versionconfig.grid.column.description' ></spring:message>",
									"<spring:message code='versionconfig.grid.column.publishedby' ></spring:message>",
									"<spring:message code='versionconfig.grid.column.action' ></spring:message>"
									],
									
							colModel:[
								    {name: 'checkbox', index: 'checkbox',sortable:false, formatter: configHistoryCheckboxFormatter, align : 'center', width:'30%', search : false},
								    {name:'id',index:'id',sortable:false,hidden: true,align:'center'},
					            	{name: 'name',index:'name',sortable:false,align:'center',formatter:versionNameFormatter},
					            	{name:'description',index:'description',sortable:false,align:'center'},
					            	{name:'createdByStaffId',index:'createdByStaffId',sortable:false,align:'center'},
					            	{name :'restore&sync',index :'',formatter : upgradeFormatter,hidden : false,align:'center',sortable:false},
						            ],
						
							//rowList : [ 10, 20, 60, 100 ],
							height : 'auto',
							mtype : 'POST',	
							sortname : 'id',
							sortorder : "desc",
							//pager : "#configHistoryListPagingDiv",
							contentType : "application/json; charset=utf-8",
							viewrecords : true,
							multiselect : false,
							timeout : 120000,
							loadtext : "Loading...",
							caption : "<spring:message code='serviceManagement.grid.caption'></spring:message>",
							beforeRequest : function() {
								$(".ui-dialog-titlebar").hide();
							},
							beforeSend : function(xhr) {
								xhr.setRequestHeader("Accept","application/json");
								xhr.setRequestHeader("Content-Type","application/json");
							},
							loadComplete : function(data) {
								$(".ui-dialog-titlebar").show();
								if ($('#configHistoryList').getGridParam('records') === 0) {
									$('#configHistoryList tbody').html("<div style='padding:6px;background:#D8D8D8;text-align:center;'><spring:message code="serviceManagement.grid.empty.records"></spring:message></div>");
									$("#configHistoryListPagingDiv").hide();
								} else {
									$("#configHistoryListPagingDiv").show();
								}
							},
							onPaging : function(pgButton) {
								clearResponseMsgDiv();
							},
							loadError : function(xhr, st, err) {
								handleGenericError(xhr, st, err);
							},
							
							gridComplete: function () {
							        var grid = this;
							        $('td[rowspan="1"]', grid).each(function () {
							            var spans = $('td[rowspanid="' + this.id + '"]', grid).length + 1;
										
							            if (spans > 1) {
							                $(this).attr('rowspan', spans);
							            }
							        });
							        prevCellVal = { cellId: undefined, value: undefined };
									utilityCellVal = { cellId: undefined, value: undefined };
									upgradeCellVal = { cellId: undefined, value: undefined };
							 },
							recordtext : "<spring:message code="serviceManagement.grid.pager.total.records.text"></spring:message>",
							emptyrecords : "<spring:message code="serviceManagement.grid.empty.records"></spring:message>",
							loadtext : "<spring:message code="serviceManagement.grid.loading.text"></spring:message>",
							pgtext : "<spring:message code="serviceManagement.grid.pager.text"></spring:message>",
						}).navGrid("#configHistoryListPagingDiv", {
					edit : false,
					add : false,
					del : false,
					search : false
				});
			
		$(".ui-jqgrid-titlebar").hide();
	}		
	
function compareVersionConfig(){
	clearAllMessages();
	if(checkedVersionConfig.length == 2) {
		var versionConfigId1 = checkedVersionConfig[0];
		var versionConfigId2 = checkedVersionConfig[1];
		var versionConfigName1 = checkedVersionConfigName[0];
		var versionConfigName2 = checkedVersionConfigName[1];
		if(versionConfigId1>versionConfigId2){
			versionConfigId2 = checkedVersionConfig[0];
			versionConfigId1 = checkedVersionConfig[1];
			versionConfigName2 = checkedVersionConfigName[0];
			versionConfigName1 = checkedVersionConfigName[1];
		}
		$.ajax({
			url: "<%=ControllerConstants.COMPARE_VERSION_CONFIG_XML%>",
			cache: false,
			async: false,
	    	data: {
	    		versionConfigId1 :versionConfigId1,
	    		versionConfigId2 :versionConfigId2,
	    	},
			dataType: 'json',
			type: "POST",
			success: function(data){	
				var response = eval(data);	
				var responseCode = response.code; 
				var responseMsg = response.msg; 
				var responseObject = response.object;		
				if(responseCode == "200"){		
					$("#compareTextLeft").append(responseObject);
					$("h4.versionCompare").text("Version Comparision of "+versionConfigName1+" and "+versionConfigName2);
					$("#comapreVersionPopup").click();
					checkedVersionConfig = [];
					checkedVersionConfigName = [];
					$('input:checkbox[name="attributeCheckbox"]').prop('checked',false);
				}else if(responseObject != undefined && responseObject != 'undefined' && responseCode == "400"){
					console.log("got 400 response.");
				} 
			},
		    error: function (xhr,st,err){	    		    	
				handleGenericError(xhr,st,err);
			}
		});
	} else {
		$("#checkboxCompareVersionValidation").click();
	}
}

</script>
<div class="title2">
	<spring:message code="config.history.grid.heading" ></spring:message>
	        <sec:authorize access="hasAnyAuthority('COMPARE_VERSION_CONFIGURATION')">
			<button id="compareVersionConfigBtn" class="btn btn-grey btn-xs"
				title="select 2 versions to compare" onclick="compareVersionConfig()" style="float:right;">
				<b> <spring:message code="config.history.compare.button"></spring:message></b></button>
			</sec:authorize>
</div>
<br>
<div
	class="tab-pane <c:if test="${(REQUEST_ACTION_TYPE eq 'CONFIG_HISTORY_SUMMARY')}"><c:out value="active"></c:out></c:if>"
	id="ConfigHistory">
	<div class="tab-content no-padding padding0">

		<table class="table table-hover" id="configHistoryList"></table>
		<!-- <div id="configHistoryListPagingDiv"></div> -->
		<div class="clearfix"></div>
		<div id="divLoading" align="center" style="display: none;">
			<img src="img/preloaders/Preloader_10.gif" />
		</div>
	</div>
</div>
</div>


<a href="#divRestoreSync" class="fancybox" style="display: none;"
	id="restoreSync">#</a>
<jsp:include page="divRestoreSync.jsp"></jsp:include>


<form action="<%=ControllerConstants.DOWNLOAD_VERSION_CONFIG_XML%>"
	method="POST" id="downloadVersionConfigXML">
	<input type="hidden" id="sid" name="sid" />
</form>

<a href="#divcheckboxvalidation" class="fancybox" style="display: none;"
	id="checkboxServer">#</a>
<div id="divcheckboxvalidation" style="width: 100%; display: none;">
	<div class="modal-content">
		<div class="modal-header padding10">
			<h4 class="modal-title">
				<spring:message code="product.Type.Select.header.label" ></spring:message>
			</h4>
		</div>
		<div class="modal-body padding10 inline-form">
			<p id="Alert-License-product-checkbox-warning">
				<spring:message code="version.config.checkbox.comparision" ></spring:message>
			</p>
		</div>
		<div id="delete-license-popup-progress-bar-div"
			class="modal-footer padding10" style="display: none;">
			<jsp:include page="../common/processing-bar.jsp"></jsp:include>
		</div>
		<div class="modal-footer padding10">
			<button type="button" class="btn btn-grey btn-xs "
				data-dismiss="modal" onclick="closeFancyBox();">
				<spring:message code="btn.label.close" ></spring:message>
			</button>
		</div>
	</div>
</div>

<a href="#divcomapreVersionValidation" class="fancybox" style="display: none;"
	id="checkboxCompareVersionValidation">#</a>
<div id="divcomapreVersionValidation" style="width: 100%; display: none;">
	<div class="modal-content">
		<div class="modal-header padding10">
			<h4 class="modal-title">
				<spring:message code="product.Type.Select.header.label" ></spring:message>
			</h4>
		</div>
		<div class="modal-body padding10 inline-form">
			<p id="Alert-License-product-checkbox-warning">
				<spring:message code="version.config.checkbox.comparision.limit" ></spring:message>
			</p>
		</div>
		<div class="modal-footer padding10">
			<button type="button" class="btn btn-grey btn-xs "
				data-dismiss="modal" onclick="closeFancyBox();">
				<spring:message code="btn.label.close" ></spring:message>
			</button>
		</div>
	</div>
</div>

<a href="#divcomapreVersionPopup" class="fancybox" style="display: none;"
	id="comapreVersionPopup">#</a>
<div id="divcomapreVersionPopup" style="width: 100%; height:50%;display: none;">
	<div class="modal-content">
		<div class="modal-header padding10">
			<h4 class="modal-title versionCompare">
				<spring:message code="version.config.comparision" ></spring:message>
			</h4>
		</div>
		<div class="modal-body padding10" style="height:500px;overflow-x: scroll;overflow-y: auto;">
			<div id="compareTextLeft"></div>
		</div>
		<div class="modal-footer padding10">
			<div class="col-md-6 no-padding">
				<div style='Color:black;display: inline; float:left;font-weight:bold;'><spring:message code="version.config.line.note"></spring:message> &nbsp;&nbsp;</div>
				<div style='Color:red;display: inline; float:left;'><spring:message code="version.config.line.removed"></spring:message></div><br/>
				<div style='Color:green;display: inline; float:left; padding-left:44px;'><spring:message code="version.config.line.added"></spring:message></div>
			</div>
			<div class="col-md-6 no-padding">
				<button type="button" class="btn btn-grey btn-xs " style="margin-top:10px;"
					data-dismiss="modal" onclick="closeFancyBox();">
					<spring:message code="btn.label.close" ></spring:message>
				</button>
			</div>
		</div>
	</div>
</div>
