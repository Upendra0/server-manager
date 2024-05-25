<%@page import="com.elitecore.sm.common.constants.BaseConstants"%>
<%@page import="com.elitecore.sm.common.constants.FormBeanConstants"%>
<%@taglib 	uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="elitecore" %> 
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="/WEB-INF/ShowErrorTag.tld" prefix="elitecoreError"%>
<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/chosen.css" type="text/css"/>
 <script type="text/javascript" src="${pageContext.request.contextPath}/js/chosen.jquery.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/multiple-select.js"></script>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/multiple-select.css"
	type="text/css" />
<script>
$(document).ready(function(){
	$('#ruleLookupTableData').chosen().change(function(){
    var myValues = $('#ruleLookupTableData').chosen().val();
    var tableName="";
	if(myValues!=-1){
		tableName=$('#ruleLookupTableData').find(":selected").text();
	}
	$("#databaseQueryList option[value]").remove();
	$('#databaseQueryList').multipleSelect("refresh");
	getDbQueryList("getDatabaseQueryList",$("#serverInstance").val(),tableName);
});
});
</script>
<script>
$(function() {
   $('#databaseQueryList').multipleSelect({
       width: '100%',
       filter: true,
       enableCaseInsensitiveFiltering: true,
       hideDisabled:true
   });
   
});
/* $(function() {
        $('#ruleLookupTableData') .change(function() {
        	$("#ruleLookupTableData option[value='-1']").remove();
        	var tableName="";
        	if($('#ruleLookupTableData').val()!=-1){
        		tableName=$('#ruleLookupTableData').find(":selected").text();
        	}
        	$("#test option[value]").remove();
			$('#test').multipleSelect("refresh");
        	getDbQueryList("getDatabaseQueryList", $("#serverInstance").val(),tableName);
        }).multipleSelect({
            width: '100%',
            single: true,
            filter: true,
            enableCaseInsensitiveFiltering: true,
            hideDisabled:true
        });
    }); */
/* $(document).ready(function() {
	 	$('#databaseQueryList').multiselect({
			includeSelectAllOption: true,
			enableFiltering: true,
			enableHTML : true,
			maxHeight: 150,
			dropDown: true,
			buttonWidth: '150px',
			onDropdownHide: function(element, checked) {
		        var dbQueries = $('#databaseQueryList option:selected');
		        dbQueryList = [];
		        $(dbQueries).each(function(index, dbQuery){
		        	dbQueryArray.push($(this).val());
		        });
		    }
		});
	 }); */
</script>	
 <script type="text/javascript" src="${pageContext.request.contextPath}/js/chosen.jquery.js"></script>
<style>
.checkbox{
	color: #000 !important;
}
.btn .caret {
    margin-left: -10px;
}
.btn-group{
	width: 100% !important;
}
.btn-group button{
	width: 100% !important;
}
.btn-default span{
	width: 100% !important;
	background:none !important ;
	text-indent:0 !important;
}
.multiselect-clear-filter {
	line-height: 1.9;
}
.ui-jqgrid .ui-jqgrid-bdiv { overflow-y: scroll;  max-height: 270px; }
.ui-jqgrid tr.jqgrow td{ word-wrap: break-word; }
</style>

<div class="modal-content">
	<div class="modal-header padding10">
		<h4 class="modal-title">
			<span id="add_label" style="display: none;"><spring:message
					code="auto.reload.cache.config.add.title" ></spring:message></span>
			<span id="update_label" style="display: none;"><spring:message
					code="auto.reload.cache.config.add.title" ></spring:message></span>
		</h4>
	</div>
	<div class="modal-body padding10 inline-form">
		<div id="AddPopUpMsg">
		<jsp:include page="../common/responseMsgPopUp.jsp" ></jsp:include>
		</div>			
		<input name="id" style="display: none;" cssClass="form-control table-cell input-sm" id="id" value="0" data-toggle="tooltip" data-placement="bottom"/>
        <div class="col-md-6 no-padding">
			 <div class="form-group">
				<spring:message code="rule.data.mgmt.table.name" var="tooltip" ></spring:message>
				<spring:message code="rule.data.mgmt.table.name" var="label" ></spring:message>
				<div class="table-cell-label">${label}<span class="required">*</span>
				</div>
				<div class="input-group" style="width:192px;">
				<select class="chosen-select" tabindex="2" id="ruleLookupTableData" style="width:192px;">
				<option value="-1">Start typing or Select (All)</option>
                </select>
				<span class="input-group-addon add-on last" > <i class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="left" title=""></i></span>
				</div>
			</div>
			
			<div class="form-group">
         		<spring:message code="auto.reload.cache.config.schedule.type" var="label" ></spring:message>
         		<spring:message code="auto.reload.cache.config.schedule.type" var="tooltip" ></spring:message>
         		<div class="table-cell-label">${label}<span class="required">*</span></div>
             	<div class="input-group">
             		<select  name = "scheduleType" class="form-control table-cell input-sm"  tabindex="3" id="scheduleType" data-toggle="tooltip" data-placement="bottom"  title="${tooltip}">
             			<c:forEach items="${SCHEDULE_TYPE}" var="scheduleType">
							<option value="${scheduleType}">${scheduleType}</option>
						</c:forEach>
             		</select>
             		<span class="input-group-addon add-on last" > <i id="serviceInstanceList_error" class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="left" title=""></i></span>
             	</div>
            </div>
            <div class="form-group" id="schedulerDiv" style="Display:none;">
				<spring:message code="auto.reload.cache.config.scheduler" var="tooltip" ></spring:message>
				<spring:message code="auto.reload.cache.config.scheduler" var="label" ></spring:message>
				<div class="table-cell-label">${label}<span class="required">*</span></div>
				<div class="input-group">
					<input type='hidden' class='form-control table-cell input-sm' id="schedulerId"  data-toggle='tooltip' data-placement='bottom' value="0" title='${tooltip }'/>
					<a href='#' onclick="selectSchedulerNamePopUp();"><i style='margin-top: 8px;margin-left: 8px;' class='glyphicon glyphicon-search'></i></a>
					<input type="text" name="schedulerName" class="form-control table-cell input-sm" tabindex="1"
							id="scheduler" data-toggle="tooltip" data-placement="bottom" title="${tooltip }" style="width:88%;" readonly />
					<span class="input-group-addon add-on last" > <i class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="left" title=""></i></span>
				</div>
			</div>
        </div>
        <div class="col-md-6 no-padding">
        	<div class="form-group">
         		<spring:message code="serverManager.server.instance" var="label" ></spring:message>
         		<spring:message code="serverManager.server.instance" var="tooltip" ></spring:message>
         		<div class="table-cell-label">${label}<span class="required">*</span></div>
             	<div class="input-group">
             		<select  name = "serverInstance" class="form-control table-cell input-sm"  tabindex="3" id="serverInstance" data-toggle="tooltip" data-placement="bottom"  title="${tooltip}">
             		<option value="-1" selected="selected">select server instance</option>
					<c:if test="${SERVERINSTANCE_LIST !=null && fn:length(SERVERINSTANCE_LIST) gt 0}">
						<c:forEach items="${SERVERINSTANCE_LIST}" var="serverInstance">
							<option value="${serverInstance.id}">${serverInstance.name}</option>
						</c:forEach>
					</c:if>
             		</select>
             		<span class="input-group-addon add-on last" > <i class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="left" title=""></i></span>
             	</div>
            </div>
            <%-- <div class="form-group">
         		<spring:message code="file.reprocess.serviceInstanceList.label" var="label" ></spring:message>
         		<spring:message code="file.reprocess.serviceInstanceList.label.tooltip" var="tooltip" ></spring:message>
         		<div class="table-cell-label">DB Query<span class="required">*</span></div>
             	<div class="input-group">
             		<select  name = "databaseQueryList" class="form-control table-cell input-sm"  tabindex="3" multiple="multiple" id="databaseQueryList" data-toggle="tooltip" data-placement="bottom"  title="${tooltip}">
             		</select>
             		<span id ="amit" class="input-group-addon add-on last" > <i class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="left" title=""></i></span>
             	</div>
            </div> --%>
            
            
             <div class="form-group">
				<spring:message code="file.reprocess.serviceInstanceList.label" var="label" ></spring:message>
         		<spring:message code="file.reprocess.serviceInstanceList.label.tooltip" var="tooltip" ></spring:message>
         		<div class="table-cell-label">DB Query<span class="required">*</span></div>
				<div class="input-group">
				<select id='databaseQueryList' data-placement='bottom' multiple="multiple">
				</select> 
				<span class="input-group-addon add-on last" > <i class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="left" title=""></i></span>
				</div>
			</div>
            <div class="form-group">
         		<spring:message code="auto.reload.cache.config.reload.options" var="label" ></spring:message>
         		<spring:message code="auto.reload.cache.config.reload.options" var="tooltip" ></spring:message>
         		<div class="table-cell-label">${label}<span class="required">*</span></div>
             	<div class="input-group">
             		<select  name = "reloadOptions" class="form-control table-cell input-sm"  tabindex="3" id="reloadOptions" data-toggle="tooltip" data-placement="bottom"  title="${tooltip}">
             			<c:forEach items="${RELOAD_OPTIONS}" var="reloadOption">
             			<c:if test="${reloadOption ne 'Delta'}">
							<option value="${reloadOption}">${reloadOption}</option>
						</c:if>	
						</c:forEach>
             		</select>
             		<span class="input-group-addon add-on last" > <i id="serviceInstanceList_error" class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="left" title=""></i></span>
             	</div>
            </div>
        </div><br><br><br><br><br><br><br><br><br><br><br><br><br>
	</div>
	
	<div class="modal-footer padding10">
		<sec:authorize access="hasAuthority('CREATE_RULE_DATA_CONFIG')">
				<button type="button" id="addNewLookupTable" class="btn btn-grey btn-xs " style="display: none"
					onclick="createUpdateAutoReloadCache('createAutoReloadCacheConfig');">
					<spring:message code="btn.label.add" ></spring:message>
				</button>
		</sec:authorize>
		<sec:authorize access="hasAuthority('UPDATE_RULE_DATA_CONFIG')">
				<button type="button" id="updateLookupTable" class="btn btn-grey btn-xs " style="display: none"
					onclick="createUpdateAutoReloadCache('updateAutoReloadCacheConfig');">
					<spring:message code="btn.label.update" ></spring:message>
				</button>
		</sec:authorize>
		<button id='close_btn' type="button" class="btn btn-grey btn-xs "
				data-dismiss="modal"
				onclick="closeFancyBox();">
				<spring:message code="btn.label.close" ></spring:message>
		</button>
	</div>
</div>

<!-- div for get table list by search start-->		
<a href="#divTableList" class="fancybox" style="display: none;" id="tableList">#</a>
<div id="divTableList" style=" width:100%; display:none;" >
    <div class="modal-content" style="overflow:hidden">
		<div class="modal-header padding10">
			<h4 class="modal-title">
				<i class="icon-hdd"></i> 
				<%-- <spring:message code="device.tab.heading.label" ></spring:message> --%> Table List
			</h4>
		</div>
		<div id="deviceContentDiv" class="modal-body" >
			
			<div>
				<label id="lblCounterForDeviceName" style="display:none;"></label>
				<label id="lblDeviceid" style="display:none;"></label>
				<span class="title2rightfield">
				 	<span class="title2rightfield-icon1-text" style="font-size: 12px;"> 
				 		<!-- <a href="#" onclick="createPolicy();"  ><spring:message code="business.policy.create.label" ></spring:message></a>  --> 
					</span>
				</span>
			</div>
			<div class="fullwidth table-responsive " style="height:auto;max-height:300px;overflow: auto;">
				<table class="table table-hover" id="tablelistgrid"></table>
				<div id="tableListGridPagingDiv"></div>
			</div>
		
		</div>
		<div id="buttons-div" class="modal-footer padding10 text-left">
			<div id="buttons-div" class="modal-footer padding10 text-left">
				<button type="button" class="btn btn-grey btn-xs " id ="selectTablebtn">
					<spring:message code="btn.label.select" ></spring:message>
				</button>
				<button onClick="closeFancyBoxFromChildIFrame();addAutoReloadCachePopUp();" class="btn btn-grey btn-xs ">
					<spring:message code="btn.label.cancel" ></spring:message>
				</button>
			</div>
		</div>
	</div>
			<!-- /.modal-content -->
</div>
<!-- div for get table list by search end-->

<!-- div for get scheduler list by search start-->		
<a href="#divSchedulerList" class="fancybox" style="display: none;" id="schedulerList">#</a>
<div id="divSchedulerList" style=" width:100%; display:none;" >
    <div class="modal-content" style="overflow:hidden">
		<div class="modal-header padding10">
			<h4 class="modal-title">
				<i class="icon-hdd"></i> 
				<%-- <spring:message code="device.tab.heading.label" ></spring:message> --%> Scheduler List
			</h4>
		</div>
		<div id="deviceContentDiv" class="modal-body" >
			
			<div>
				<label id="lblCounterForDeviceName" style="display:none;"></label>
				<label id="lblDeviceid" style="display:none;"></label>
				<span class="title2rightfield">
				 	<span class="title2rightfield-icon1-text" style="font-size: 12px;"> 
				 		<!-- <a href="#" onclick="createPolicy();"  ><spring:message code="business.policy.create.label" ></spring:message></a>  --> 
					</span>
				</span>
			</div>
			<div class="fullwidth table-responsive " style="height:auto;max-height:300px;overflow: auto;">
				<table class="table table-hover" id="schedulerlistgrid"></table>
				<div id="schedulerGridPagingDiv"></div>
			</div>
		
		</div>
		<div id="buttons-div" class="modal-footer padding10 text-left">
			<div id="buttons-div" class="modal-footer padding10 text-left">
				<button type="button" class="btn btn-grey btn-xs " id ="selectSchedulerbtn">
					<spring:message code="btn.label.select" ></spring:message>
				</button>
				<button onClick="closeFancyBoxFromChildIFrame();addAutoReloadCachePopUp();" class="btn btn-grey btn-xs ">
					<spring:message code="btn.label.cancel" ></spring:message>
				</button>
			</div>
		</div>
	</div>
			<!-- /.modal-content -->
</div>
<!-- div for get trigger list by search end-->	
		
<script>
var dbQueryArray = [];



function selectTableNamePopUp(){
	getTableListBySearchParams('<%=ControllerConstants.INIT_RULE_TABLE_DATA_LIST%>');
	$("#tablelistgrid").jqGrid('resetSelection');
	$("#tableList").click();
};

function getTableListBySearchParams(urlAction) {
$("#tablelistgrid").jqGrid({
		url: urlAction,
		datatype: "json",
		postData : {},
		colNames: [
					  "Table id",
		           	  "Table Name"
			],
		colModel: [
			{name:'id',index:'id',sortable:true, hidden:true},
			{name:'viewName',index:'viewName',  sortable:true}
		],
		rowNum : 15,
		rowList : [ 10, 20, 60, 100 ],
		height : 'auto',
		mtype : 'GET',
		contentType : "application/json; charset=utf-8",
		loadtext : "Loading...",
		sortname : 'id',
		sortorder : "desc",
		pager: "#tableListGridPagingDiv",
		viewrecords: true,
		multiselect: true,
		beforeSelectRow: function(rowid, e)
		{
		    $("#tablelistgrid").jqGrid('resetSelection');
		    return (true);
		},
		beforeRequest : function() {
		    $('input[id=cb_tablelistgrid]', 'div[id=jqgh_tablelistgrid_cb]').remove();
		},
		loadComplete : function () {
			/* var rowId = jQuery('#tablelistgrid tr:eq(0)').attr('id');
			var dataFromTheRow = jQuery('#tablelistgrid').jqGrid ('getRowData', rowId);
			
			$('input[id^="jqg_tablelistgrid_"]').each(function(index){
				var tableName = dataFromTheRow[index].viewName;
				var tableID = dataFromTheRow[index].id;
				$(this).attr("id",tableID + "_" + tableName + "_chkbox");
				$(this).attr("type","radio");
				$(this).attr("viewName","radio_grp");
			}); */
		},
		onSelectRow : function(id){
			rowData = $("#tablelistgrid").jqGrid("getRowData", id);
		},
		timeout : 120000,
	    loadtext: "Loading...",
		loadError : function(xhr,st,err) {
			handleGenericError(xhr,st,err);
		},
		recordtext: "<spring:message code="serverManagement.grid.pager.total.records.text"></spring:message>",
	    emptyrecords: "No Record Found.",
	    viewrecords: true,
		}).navGrid("#tableListGridPagingDiv",{edit:false,add:false,del:false,search:false});
		$(".ui-jqgrid-titlebar").hide();
		$(".ui-pg-input").height("10px");
		
		if ($("#tablelistgrid").getGridParam("reccount") === 0) {
			  $(".ui-paging-info").html("No Record Found.");
		}   
}

$('#selectTablebtn').click(function(){
	if(rowData){
		$("#tableId").val(rowData.id);
		$("#ruleLookupTableData").val(rowData.viewName);
	}else{
		$("#tableId").val(0);
		$("#ruleLookupTableData").val("");
	}
	closeFancyBox();
	addAutoReloadCachePopUp();
});

$("#serverInstance").change(function() {
	var tableName="";
	if($('#ruleLookupTableData').val()!=-1){
		tableName=$('#ruleLookupTableData').find(":selected").text();
	}
	$("#databaseQueryList option[value]").remove();
	$('#databaseQueryList').multipleSelect("refresh");	
	getDbQueryList("getDatabaseQueryList", $("#serverInstance").val(),tableName);
});

function getDbQueryList(urlAction, serverInstanceId, tableName,isUpdate){
	$.ajax({
		url: urlAction, 
		data: {serverInstanceId: serverInstanceId,tableName:tableName} , 
		success: function(data){
      		var response = $.parseJSON(data);
			var msg = response.msg;
			var code = response.code;
			if(code === "200"){
				dbQueryOptionList=[];
				
				var dbQueryList = response.object;
	        	jQuery.each(dbQueryList, function(i, dbQuery){
	        		jQuery.each(dbQuery, function(i, item){
	        			$(function() {
	        				 $('#databaseQueryList').append($('<option>', {
	        			        value: item.queryName,
	        			        text: item.queryName}));
	        			    $('#databaseQueryList').multipleSelect({
	        			        width: '100%',
	        			        filter: true,
	        			        enableCaseInsensitiveFiltering: true,
	        			        hideDisabled:true
	        			    });
	        			});
	        			dbQueryOptionList.push({
		                    'label': item['queryName'],
		                    'value': item['queryName']
		                })
					});
	    		});
	        	if(isUpdate){
    				$("#databaseQueryList").multipleSelect("setSelects", tempDBQueryArray);
    			}
		       //jQuery('#databaseQueryList').multiselect('dataprovider', dbQueryOptionList);
		       
			}
    }});
}

$("#scheduleType").change(function() {
	if($("#scheduleType").val() === 'Schedule'){
		$("#schedulerDiv").show();
	}else{
		$("#scheduler").val("");
		$("#schedulerDiv").hide();
	}  
});

function selectSchedulerNamePopUp(){
	getSchedulerListBySearchParams('<%=ControllerConstants.INIT_TRIGGER_DATA_LIST%>');
	$("#schedulerlistgrid").jqGrid('resetSelection');
	$("#schedulerList").click();
};

function getSchedulerListBySearchParams(urlAction) {
$("#schedulerlistgrid").jqGrid({
		url: urlAction,
		datatype: "json",
		postData : {},
		colNames: [
		            "#",
					"<spring:message code='trigger.mgmt.jqgrid.trigger.name' ></spring:message>",
					"<spring:message code='trigger.mgmt.jqgrid.description' ></spring:message>",
		            "Frequency"
			],
		colModel: [
			{name:'id',index:'id',hidden:true},
			{name:'triggerName',index:'triggerName',sortable:true},
			{name:'description',index:'description',sortable:true},
			{name:'type',index:'type',align:'center'}
		],
		rowNum : 15,
		rowList : [ 10, 20, 60, 100 ],
		height : 'auto',
		mtype : 'GET',
		contentType : "application/json; charset=utf-8",
		loadtext : "Loading...",
		sortname : 'id',
		sortorder : "desc",
		pager: "#schedulerGridPagingDiv",
		viewrecords: true,
		multiselect: true,
		beforeSelectRow: function(rowid, e)
		{
		    $("#schedulerlistgrid").jqGrid('resetSelection');
		    return (true);
		},
		beforeRequest : function() {
		    $('input[id=cb_schedulerlistgrid]', 'div[id=jqgh_schedulerlistgrid_cb]').remove();
		},
		loadComplete : function () {
			/* var rowId = jQuery('#schedulerlistgrid tr:eq(0)').attr('id');
			var dataFromTheRow = jQuery('#schedulerlistgrid').jqGrid ('getRowData', rowId);
			
			$('input[id^="jqg_schedulerlistgrid_"]').each(function(index){
				var schedulerName = dataFromTheRow[index].triggerName;
				var schedulerID = dataFromTheRow[index].id;
				$(this).attr("id",schedulerID + "_" + schedulerName + "_chkbox");
				$(this).attr("type","radio");
				$(this).attr("schedulerName","radio_grp");
			}); */
		},
		onSelectRow : function(id){
			rowData = $("#schedulerlistgrid").jqGrid("getRowData", id);
		},
		timeout : 120000,
	    loadtext: "Loading...",
		loadError : function(xhr,st,err) {
			handleGenericError(xhr,st,err);
		},
		recordtext: "<spring:message code="serverManagement.grid.pager.total.records.text"></spring:message>",
	    emptyrecords: "No Record Found.",
	    viewrecords: true,
		}).navGrid("#schedulerGridPagingDiv",{edit:false,add:false,del:false,search:false});
		$(".ui-jqgrid-titlebar").hide();
		$(".ui-pg-input").height("10px");
		
		if ($("#schedulerlistgrid").getGridParam("reccount") === 0) {
			  $(".ui-paging-info").html("No Record Found.");
		}   
}

$('#selectSchedulerbtn').click(function(){
	if(rowData){
		$("#schedulerId").val(rowData.id);
		$("#scheduler").val(rowData.triggerName);
	}else{
		$("#schedulerId").val("");
		$("#scheduler").val("");
	}
	closeFancyBox();
	addAutoReloadCachePopUp();
});

function addAutoReloadCachePopUp(){
	$("#divAddAutoReloadCachePopUp").click();
}

function createUpdateAutoReloadCache(url){
	setTimeout(function(){loadXML(url);}, 200);
}

function loadXML(url) {
	var dbQueries = $('#databaseQueryList option:selected');
    dbQueryList = [];
    $(dbQueries).each(function(index, dbQuery){
    	dbQueryArray.push($(this).val());
    });
	resetWarningDisplay();
	clearAllMessages();
	var dbquerieslist = dbQueryArray.toString();
	
	$.ajax({
		url: url,
		cache: false,
		async: true,
		dataType: 'json',
		type: "POST",
		data:
		 {
			"id"						:	$("#id").val(),
			"ruleLookupTableData.id"	:	$('#ruleLookupTableData').val(),
			"serverInstance.id" 		:	$('#serverInstance').val(),
			"databaseQueryList"			:	dbquerieslist,
			"scheduleType"				:	$('#scheduleType').val(),
			"triggerId"					:	$('#schedulerId').val(),
			"reloadOptions"				:	$('#reloadOptions').val()
		}, 
		
		success: function(data){
			var response = eval(data);
			
			var responseCode = response.code; 
			var responseMsg = response.msg; 
			var responseObject = response.object;
			dbQueryArray = [];
			if(responseCode == "200"){
				resetWarningDisplay();
				clearAllMessages();
				closeFancyBoxFromChildIFrame();
				reloadAutoReloadGridData();
				showSuccessMsg(responseMsg);
			}else if(responseObject != undefined && responseObject != 'undefined' && responseCode == "400"){
				addErrorIconAndMsgForAjaxPopUp(responseObject);
				if (responseObject.ruleLookupTableData) {
				    $("#ruleLookupTableData").next().next().children().first().attr("data-original-title",responseObject.ruleLookupTableData).attr("id","databaseQueryList_error1");
					addErrorClassWhenTitleExistPopUp($(
							"#GENERIC_ERROR_MESSAGE",
							window.parent.document).val());
				}
				if(responseObject.databaseQueryList){
					$("#databaseQueryList").next().next().children().first().attr("data-original-title",responseObject.databaseQueryList).attr("id","databaseQueryList_error");
					addErrorClassWhenTitleExistPopUp($("#GENERIC_ERROR_MESSAGE", window.parent.document).val());
				}
			}else{
				resetWarningDisplay();
				clearAllMessages();
				showErrorMsgPopUp(responseMsg);
				 
			}
		},
	    error: function (xhr,st,err){
	    	hideProgressBar(pathListCounter);
			handleGenericError(xhr,st,err);
		}
	});
}
</script>
