  <%@page import="com.elitecore.sm.common.constants.SystemParametersConstant"%>
  <%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
    <%@page import="com.elitecore.sm.util.MapCache"%>
    <%@ taglib 	uri="http://www.springframework.org/security/tags" prefix="sec"  %>
    <%@taglib 	uri="http://www.springframework.org/tags" prefix="spring"%>
    <%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>



<script>

var snmpAlertUpdate = [];
function serviceThresholdColumnFormatter(cellvalue, options, rowObject){	
	var alertName = rowObject["alertName"].replace(/ /g, "_");
    var alertSvcThdId = alertName + "_alert_svcthd_lnk";
    
	if(cellvalue=="true"){
		return "<a href='#' id='"+alertSvcThdId+"' class='link' onclick=addserviceThresholdPopup('"+rowObject["id"]+"'); ><i class='fa fa-list orangecolor'></i></a>";
	}
	else if(cellvalue=="false"){
		return "<a href='#' class='link' onclick=addserviceThresholdPopup('"+rowObject["id"]+"'); ><i class='fa fa-list'></i></a>";
	}
	else{
		return "<a href='#' class='link' ><i class='fa fa-list'></i></a>";
	}
} 

$(document).ready(function() {
	getSnmpAlertList();	
})
			
function editColumnFormatter(cellvalue, options, rowObject){
	var alertName = rowObject["alertName"].replace(/ /g, "_");
	var alerteditId = alertName + "_alert_edit_lnk";

	return "<a href='#' id='"+alerteditId+"' class='link' onclick=updateAlertListPopup('"+'EDIT'+"','"+rowObject["id"]+"'); ><i class='fa fa-pencil-square-o' aria-hidden='true'></i></a>";
}


function updateAlertListPopup(actionType,attributeId){
	 
 	clearAllMessagesPopUp();
	resetWarningDisplayPopUp();
 	
	if(attributeId > 0){
		
		var responseObject='';
		responseObject = jQuery("#snmpAlertgrid").jqGrid ('getRowData', attributeId);
		$("#snmpAlertId").val(responseObject.id);
		$("#update_alertId").val(responseObject.alertId);
		$("#update_name").val(responseObject.alertName);
		$("#update_desc").val(responseObject.desc);
		
		$("#updateAlertListPopup").click();
	}else{
		showErrorMsg(jsSpringMsg.failUpdatesnmpServerMsg);
	}
}

			
function addserviceThresholdPopup(attributeId){	
	if(attributeId>0){		
		var responseObject='';
		responseObject = jQuery("#snmpAlertgrid").jqGrid ('getRowData', attributeId);
		getServiceListWithThreshold(responseObject.id);
		
	 	$("#alertId").val(responseObject.alertId);
	 	$("#name").val(responseObject.alertName);
	 	$("#desc").val(responseObject.desc);
	
		$("#addsnmpAlertServiceThresholdPopup").click();
	}
	else{
		showErrorMsg(jsSpringMsg.failUpdateSnmpServerMsg);
	}
		
	resetWarningDisplayPopUp();
	clearAllMessagesPopUp();
}

function getServiceListWithThreshold(alertId)
{
	$("#selected_alertId").val(alertId);
	$.ajax({
		url: '<%=ControllerConstants.GET_SNMP_ALERTLIST_SERVICETHRESHOLD%>',
		cache: false,
		async: true, 
		dataType: 'json',
		type: "POST",
		data: 
		{
			"alertId"					:   alertId,
			"serverInstanceId"			:   $("#server_Instance_Id").val(),
		},
		success: function(data){
			hideProgressBar();
			
			var response = eval(data);
			
			var responseCode = response.code; 
			var responseMsg = response.msg; 
			var responseObject = response.object;
			 console.log("Response object is ::  " + responseObject);
			if(responseCode == "200"){
				//parent.getSnmpServerList();
				
			//	$("#create-client-buttons-div").hide();
				$("#create-client-alerts-buttons-div").show();
				$("#calert_addNewAttribute").hide();
				$("#calert_editAttribute").show();
				$("#configAlertSvcContentDiv_threshold").hide();
				ckSnmpClientAlertSelected=new Array();
			//	$("#addSnmpClientAlertListC0ntentDiv").show();
			//	showSuccessMsg(responseMsg);
				var serviceObj=eval(responseObject);
				
				
				var svcList=serviceObj['svcList'];
				var svcType;
				var genericThreshold;
				
				var xyz=svcList[0];
				
				$.each(svcList,function(index,value){
					svcType=value.thresholdType
				});
				$("#selected_alertType").val(svcType);
				
				if(svcType=="SVC"){
				
				$("#tblConfigAlertSvcListDiv").show();
				var tableString  = "<tr> <th><spring:message code='snmp.alert.serviceThreshold.grid.serviceId' ></spring:message></th><th><spring:message code='snmp.alert.serviceThreshold.grid.serviceName' ></spring:message></th><th><spring:message code='snmp.alert.serviceThreshold.grid.threshold' ></spring:message></th></tr>"		
				}
				else if(svcType=="SERVER_INSTANCE"){
					
					$("#tblConfigAlertSvcListDiv").show();
					var tableString  = "<tr> <th><spring:message code='snmp.alert.serviceThreshold.grid.serverInstanceId' ></spring:message></th><th><spring:message code='snmp.alert.serviceThreshold.grid.serverInstanceName' ></spring:message></th><th><spring:message code='snmp.alert.serviceThreshold.grid.threshold' ></spring:message></th></tr>"		
				}
				else if(svcType=="GENERIC"){
					
					$("#tblConfigAlertSvcListDiv").show();
					var tableString  = "<tr> <th><spring:message code='snmp.alert.serviceThreshold.grid.serverInstanceId' ></spring:message></th><th><spring:message code='snmp.alert.serviceThreshold.grid.serverInstanceName' ></spring:message></th><th><spring:message code='snmp.alert.serviceThreshold.grid.threshold' ></spring:message></th></tr>"
					
				}
			$.each(svcList,function(index,value){
					var serviceName = value.name;
					serviceName = serviceName.replace(/ /g,"_");
					tableString     += "<tr class='serviceDetail'>";
					
					tableString     += "<td id='"+index+"_id"+"' width='10%' hidden='true' >"+value.id+"</td>";
					tableString     += "<td id='"+index+"_id2"+"' width='10%' hidden='true' >"+value.alertName+"</td>";
					tableString     += "<td id='"+index+"_id3"+"' width='10%' hidden='true' >"+serviceName+"</td>";
					tableString     += "<td id='"+index+"_id1"+"' width='10%'>"+value.servInstanceId+"</td>";
					tableString     += "<td id='"+index+"_name"+"' width='30%'>"+value.name+"</td>";
					tableString     += "<td id='"+index+"_threshold"+"' width='60%'>"+
										"<div class='col-md-9 no-padding'>"+
									   "<div class='form-group'>"+
									   "<div class='input-group'>"+
									   "<input type='text' id='"+value.servInstanceId+"_"+serviceName+"_threshold"+"' class='form-control table-cell input-sm' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }' value='"+value.threshold+"' onkeydown='isNumericOnKeyDown(event)'/> "+
									   "<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span> "+
									   "</div></div></div>"+
									   "</td>";
									   tableString     += "<td id='"+index+"_thresholdIdTD"+"'><input type='hidden' id='"+value.id+"_thresholdId"+"' class='form-control table-cell input-sm' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }' value='"+value.thresholdId+"' /> </td>";
 					tableString     += "</tr>";
					});
				$("#tblConfigAlertSvcList").html(tableString);
					} 
			else if(responseObject != undefined && responseObject != 'undefined' && responseCode == "400"){
				console.log("got 400 response.");
			//	$("#create-server-buttons-div").show();
				
				addErrorIconAndMsgForAjaxPopUp(responseObject);
				
			}else{
				
				resetWarningDisplayPopUp();
				$("#create-server-buttons-div").show();
				showErrorMsgPopUp(responseMsg);
				reloadAlertGridData();
			}
		},
	    error: function (xhr,st,err){
	    	hideProgressBar();
	    	$("#create-server-buttons-div").show();
			handleGenericError(xhr,st,err);
		}
	});
	
}

function reloadAlertGridData(){
	var $grid = $("#snmpAlertgrid");
	$grid.jqGrid('setGridParam',{datatype:'json',page: 1}).trigger('reloadGrid');	
}

function getSnmpAlertList(){
	showProgressBar('snmp-alert-list');			
	$.ajax({
		url: '<%=ControllerConstants.GET_SNMP_ALERTLIST%>',
		cache: false,
		async: false, 
		dataType: 'json',
		type: "POST",
		data: 
		{
			"snmpserverInstanceId"			:   $("#server_Instance_Id").val(),			
		},
		success: function(data){						
			var response = eval(data);			
			var responseCode = response.code; 
			var responseMsg = response.msg; 
			var responseObject = response.object;
			console.log("Response object is ::  " + responseObject);
			if(responseCode == "200"){				
				$("#snmpAlertgridPagingDiv").show();				
				//reloadAlertGridData();
				var alerts=eval(responseObject);				
				var alertList=alerts['alertList'];
				
				$("#snmpAlertgrid").jqGrid('clearGridData');
				if(alertList!=null && alertList != 'undefined' && alertList != undefined){					
					createGridForSnmpAlertList(alertList);
					$.each(alertList,function(index,attribute){
		 				
		 				var rowData = {};
		 				rowData.id							= parseInt(attribute.id);
		 				rowData.alertId						= attribute.alertId;
		 				rowData.alertName					= attribute.name;
		 				rowData.desc						= attribute.desc;
		 				rowData.threshold					= attribute.threshold;
		 				rowData.serviceThreshold			= attribute.serviceThreshold
		 				
		 				jQuery("#snmpAlertgrid").jqGrid('addRowData',rowData.id,rowData);
		 				jQuery("#snmpAlertgrid").jqGrid('setSelection',rowData.id,true);

		 			});
					jQuery("#snmpAlertgrid").setGridParam({ rowNum: 10 }).trigger("reloadGrid");
				} 
				$('.fancybox-wrap').css('top','10px');
			}else if(responseObject != undefined && responseObject != 'undefined' && responseCode == "400"){
				console.log("got 400 response.");
			}
		},
	    error: function (xhr,st,err){	    		    	
			handleGenericError(xhr,st,err);
		}
	});
}

function createGridForSnmpAlertList(alertList){
	
	$("#snmpAlertgrid").jqGrid({  	  
	    datatype: "local",
        colNames:["<spring:message code='snmpClientList.grid.column.alertList.id' ></spring:message>",
	  			  "<spring:message code='snmpClientList.grid.column.alertList.id' ></spring:message>",
                  "<spring:message code='snmpClientList.grid.column.alertList.name' ></spring:message>",
                  "<spring:message code='snmpAlertList.grid.column.description' ></spring:message>",
                  "<spring:message code='snmpAlertList.grid.column.serviceThreshold' ></spring:message>",
                  "<spring:message code='snmpAlertList.grid.column.serviceThreshold' ></spring:message>",
                  "<spring:message code='snmpAlertList.grid.column.alertType' ></spring:message>",
                  "<spring:message code='snmpServerList.grid.column.edit' ></spring:message>",             
                 ],
		colModel:[
				{name:'id',index:'id',sortable:true,hidden:true,search : false},
				{name:'alertId',index:'alertId',sortable:true,hidden:true,search : false},
				{name:'alertName',index:'alertName',sortable:true},
				{name:'desc',index:'desc',sortable:false},
				{name:'threshold',index:'threshold',align:'center',sortable:false,hidden:true,search : false},
				{name:'serviceThreshold',index:'serviceThreshold',align:'center',sortable:false,formatter:serviceThresholdColumnFormatter,search : false},
				{name:'alertType',index:'alertType',align:'center',sortable:false,hidden:true,search : false},
				{name:'Edit',index:'edit',align:'center',formatter:editColumnFormatter,search : false},
        		],        
        rowNum:<%= MapCache.getConfigValueAsInteger(SystemParametersConstant.TOTAL_ROWS_TO_DISPLAY_IN_GRID, 10) %>,
        rowList:[10,20,60,100],
        height: 380,
        ignoreCase: true,
        mtype:'POST',
		sortname: 'name',
 		sortorder: "desc",
 		search:true,
        pager: "#snmpAlertgridPagingDiv",
        contentType: "application/json; charset=utf-8",
        viewrecords: true,
        multiselect: false,
        timeout : 120000,
        loadtext: "Loading...",
        caption: "<spring:message code='snmpConfiguration.alertList.caption.label'></spring:message>",
        beforeRequest:function(){
            $(".ui-dialog-titlebar").hide();
        }, 
        beforeSend: function(xhr) {
            xhr.setRequestHeader("Accept", "application/json");
            xhr.setRequestHeader("Content-Type", "application/json");
        },       
        loadComplete: function(data) {        	
			$(".ui-dialog-titlebar").show();	
			hideProgressBar('snmp-alert-list');
		},		
		loadError : function(xhr,st,err) {
			handleGenericError(xhr,st,err);
			hideProgressBar('snmp-alert-list');
		},		
		recordtext: "<spring:message code="serverManagement.grid.pager.total.records.text"></spring:message>",
	    emptyrecords: "<spring:message code="snmpServerList.grid.empty.records"></spring:message>",
		loadtext: "<spring:message code="snmpServerList.grid.loading.text"></spring:message>",
		pgtext : "<spring:message code='regex.parser.attr.grid.pager.text'></spring:message>",
	}).navGrid("#snmpAlertgridPagingDiv",{edit:false,add:false,del:false,search:true,refresh:false});
	
	$(".ui-jqgrid-titlebar").hide();
	$(".ui-pg-input").height("10px");
	
	$('#snmpAlertgrid').jqGrid('filterToolbar',{
		stringResult: true,
		searchOperators: false,
		searchOnEnter: false, 
		defaultSearch: "cn"
    });		
}
</script> 
 					
<a href="#divAddsnmpAlertServiceThresholdPopup" class="fancybox" style="display: none;" id="addsnmpAlertServiceThresholdPopup">#</a>
<div id="divAddsnmpAlertServiceThresholdPopup" style=" width:100%; display: none;" >
	<jsp:include page="snmpServiceThresholdPopUp.jsp"></jsp:include> 
</div>
<div class="tab-content no-padding clearfix">
	<div class="fullwidth">
 		<div class="title2">
 			<spring:message code="snmpConfiguration.alertList.caption.label" ></spring:message>
	    </div>
  	</div>
</div>
		
<div class="box-body table-responsive no-padding box">
	<table class="table table-hover" id="snmpAlertgrid"></table>
    <div id="snmpAlertgridPagingDiv"></div> 
</div>
        
<a href="#divUpdateAlertList" class="fancybox" style="display: none;" id="updateAlertListPopup">#</a>
<div id="divUpdateAlertList" style=" width:100%; display: none;" >
	<jsp:include page="updateSnmpAlert.jsp"></jsp:include> 
</div> 

<div id="snmp-alert-list-progress-bar-div" class="modal-footer padding10" style="display: none;">
	<jsp:include page="../common/processing-bar.jsp"></jsp:include>
</div>
