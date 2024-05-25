<%@page import="com.elitecore.sm.common.constants.BaseConstants"%>
<%@page import="com.elitecore.sm.common.model.DecodeTypeEnum"%>
<%@page import="com.elitecore.sm.common.constants.FormBeanConstants"%>
<%@page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@page import="com.elitecore.sm.util.MapCache"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@page import="com.elitecore.sm.common.constants.SystemParametersConstant"%>
<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<%@page import="com.elitecore.sm.common.constants.EngineConstants"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="/WEB-INF/ShowErrorTag.tld" prefix="elitecoreError"%>
<style>
.overUtilized{
	background: red;
	color: white;
	font-weight: bold;
}
</style>
<div class="tab-content no-padding clearfix" id="circle-manager-block">
	<!-- Grid content start here -->
	<div class="tab-content no-padding clearfix">
		<div class="fullwidth">
			<div class="title2">
				<spring:message code="circle.data.mgmt.circle.list" ></spring:message>
				<span class="title2rightfield"> 
					<sec:authorize access="hasAuthority('ADD_CIRCLE')">
						<span class="title2rightfield-icon1-text"> 
						<a href="#"	onclick="addCirclePopup();"><i class="fa fa-plus-circle"></i></a>
						<a href="#" id="addCricle" onclick="addCirclePopup();">
							<spring:message	code="btn.label.add" ></spring:message>
						</a>
						</span>
					</sec:authorize> 					
					<a href="#divAddCircle" class="fancybox" style="display: none;"	id="addCircle_link">#</a> 
				</span>
			</div>
		</div>

	 	<sec:authorize access="hasAuthority('VIEW_CIRCLE')">
			<div class="modal-body padding10 inline-form">
				<div class="box-body table-responsive no-padding box">
					<table class="table table-hover" id="circleGrid"></table>
					<div id="circleGridPagingDiv"></div>
				</div>
			</div>
		</sec:authorize>
	</div>
	<!-- Grid content end here -->
	
	<!-- Device Popoup code start here  -->
	<form:form modelAttribute="circle_form_bean" method="POST" action="javascript:;" id="device-form">
	<div id="divAddCircle" style=" width:100%; display: none;" >
	    <div class="modal-content">
		        <div class="modal-header padding10">
		            <h4 class="modal-title">
		            	<span style="display:none;" id="create_circle_span">
		            		<spring:message code="circle.create.popup.heading.label" ></spring:message>
		            	</span>		            	
		            </h4>
		        </div>
		        <div class="modal-body padding10 inline-form">
		        	<div class=fullwidth>
		        		<jsp:include page="../common/responseMsgPopUp.jsp" ></jsp:include>
		        	</div>
		        	<div class="fullwidth">
		        		<input type="hidden" value="ADD" id="circleCurrentAction" name="circleCurrentAction"/>
		        		<form:hidden path="id" id="circle_id" value="0"></form:hidden> 
	               		<div class="form-group" id="create_circle_name_div" style="display:none;">
	               			<spring:message code="circle.list.grid.circle.name.label" var="tooltip"></spring:message>
	            			<div class="table-cell-label">${tooltip}<span class="required">*</span></div>
	                		<div class="input-group">
	                			<form:input path="name" cssClass="form-control table-cell input-sm" id="circle_name" tabindex="1"  data-toggle="tooltip" data-placement="bottom"  title="${tooltip }"></form:input>
								<spring:bind path="name">
									<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
								</spring:bind> 	
	                		</div>
	               		</div>
	               		<div class="form-group" id="create_circle_description_div" style="display:none;">
	               			<spring:message code="circle.data.mgmt.circle.description" var="tooltip"></spring:message>
	            			<div class="table-cell-label">${tooltip}</div>
	                		<div class="input-group">
	                			<form:input path="description" cssClass="form-control table-cell input-sm" id="circle_description" tabindex="1"  data-toggle="tooltip" data-placement="bottom"  title="${tooltip }"></form:input>
								<spring:bind path="description">
									<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
								</spring:bind> 	
	                		</div>
	               		</div>               		
		        	</div>
		        </div>
		        <sec:authorize access="hasAuthority('ADD_CIRCLE')">
			        <div class="modal-footer padding10" id="add-circle-buttons-div">
			            <button type="button" id="create_circle_btn" class="btn btn-grey btn-xs " onclick="createCircle();"><spring:message code="btn.label.save"></spring:message></button>
			            <button type="button" class="btn btn-grey btn-xs " onclick="closeFancyBox();"><spring:message code="btn.label.cancel"></spring:message></button>
			        </div>
			        <div class="modal-footer padding10" id="close-circle-buttons-div" style="display:none;">
			            <button type="button" class="btn btn-grey btn-xs " onclick="closeFancyBox();reloadGridData();"><spring:message code="btn.label.close"></spring:message></button>
			        </div>
		        </sec:authorize>
		        <div id="circle_proccess_bar_div" class="modal-footer padding10 text-left" style="display: none;">
					<jsp:include page="../common/processing-bar.jsp"></jsp:include>
				</div>
		    </div>
		    <!-- /.modal-content -->
	</div>
	</form:form>
	<sec:authorize access="hasAuthority('UPLOAD_CIRCLE_DEVICE_LICENSE')">
		<a href="#divUploadLicenseKey" class="fancybox" style="display: none;" id="uploadLicenseKey">#</a>
 		<div id="divUploadLicenseKey" style=" width:100%; display: none;">
 			<jsp:include page="uploadLicenseFilePopup.jsp"></jsp:include>
		</div>
	</sec:authorize>	
	<sec:authorize access="hasAuthority('EDIT_CIRCLE')">
		<a href="#divUpdateCircle" class="fancybox" style="display: none;" id="updateCirclePopup">#</a>
		<div id="divUpdateCircle" style=" width:100%; display: none;" >
			<jsp:include page="updateCircle.jsp"></jsp:include> 
		</div>
	</sec:authorize>
	<sec:authorize access="hasAuthority('DELETE_CIRCLE')">
		<a href="#divDeleteCircle" class="fancybox" style="display: none;" id="deleteCirclePopup">#</a>
	   	<div id="divDeleteCircle" style=" width:100%; display: none;" >
		   <jsp:include page="deleteCirclePopUp.jsp"></jsp:include> 
		</div>
	</sec:authorize>
	<sec:authorize access="hasAuthority('VIEW_CIRCLE_DEVICE_LICENSE_INFO')">
		<a href="#divLicenseInfo" class="fancybox" style="display: none;" id="licenseInfoPopup">#</a>	   
		<div id="divLicenseInfo" style=" width:100%; display: none;" >
		   <div class="modal-content">
		        <div class="modal-header padding10">
		            <h4 class="modal-title"> <span id="attribute_name_label"></span> </h4>
		        </div>
		        <div class="modal-body padding10 inline-form">
					<div id="license_info_div" style="overflow: auto;height: auto;"></div>	        	
		        </div>
	
		        <div id="start-buttons-div" class="modal-footer padding10">
		            <button type="button" class="btn btn-grey btn-xs " id="server-start-close-btn" data-dismiss="modal" onclick="closeFancyBox();"><spring:message code="btn.label.close"></spring:message></button>
		        </div>
		    </div>
		</div>
	</sec:authorize>
	<sec:authorize access="hasAuthority('VIEW_CIRCLE')">
		<a href="#divMappedDevicesInfo" class="fancybox" style="display: none;" id="mappedDevicesInfoPopup">#</a>	   
		<div id="divMappedDevicesInfo" style=" width:100%; display: none;" >
		   <div class="modal-content">
		        <div class="modal-header padding10">
		            <h4 class="modal-title"> <span id="mapped_devices_name_label"></span> </h4>
		        </div>
		        <div class="modal-body padding10 inline-form">
					<div id="mapped_devices_info_div" style="overflow: auto;height: auto;"></div>	        	
		        </div>
	
		        <div id="start-buttons-div" class="modal-footer padding10">
		            <button type="button" class="btn btn-grey btn-xs " id="mapped-devices-close-btn" data-dismiss="modal" onclick="closeFancyBox();"><spring:message code="btn.label.close"></spring:message></button>
		        </div>
		    </div>
		</div>
	</sec:authorize>
<script type="text/javascript">
var viewCircle = false;
var updateCircle = false;
var deleteCircle = false;
var uploadLicense = false;

<sec:authorize access="hasAuthority('VIEW_CIRCLE')">
viewCircle = true;
</sec:authorize>

<sec:authorize access="hasAuthority('EDIT_CIRCLE')">
updateCircle = true;
</sec:authorize>

<sec:authorize access="hasAuthority('DELETE_CIRCLE')">
deleteCircle = true;
</sec:authorize>

<sec:authorize access="hasAuthority('UPLOAD_CIRCLE_DEVICE_LICENSE')">
uploadLicense = true;
</sec:authorize>

$(document).ready(function() {
	getCircleList('<%=ControllerConstants.GET_CIRCLE_LIST%>','<%=MapCache.getConfigValueAsInteger(SystemParametersConstant.TOTAL_ROWS_TO_DISPLAY_IN_GRID, 10)%>');
});

/* Function will get all device list by search parameter and render it to grid. */
function getCircleList(urlAction, rowNum) {
	$.ajax({url: '<%=ControllerConstants.GET_CIRCLE_LIST%>',
		cache : false,
		async : false,
		dataType : 'json',
		type : "GET",
		data : {},
		success : function(data) {
			hideProgressBar();
			var response = eval(data);
			var responseCode = response.code;
			var responseMsg = response.msg;
			var responseObject = response.object;
			if (responseCode == "200") {
				var circles = eval(responseObject);
				var circleList = responseObject;
				displayCircleGrid(circleList);
				$("#circleGrid").jqGrid('clearGridData');				
				if (circleList != null && circleList != 'undefined'	&& circleList != undefined) {
					$.each(circleList, function(index, attribute) {
						var rowData = {};
						rowData.id = parseInt(attribute.id);														
						rowData.name = attribute.name;
						rowData.description = attribute.description;
						rowData.isAssociated = attribute.isAssociated;
						rowData.isLicenseApplied = attribute.isLicenseApplied;
						rowData.isLicenseExhausted = attribute.isLicenseExhausted;						
						jQuery("#circleGrid").jqGrid('addRowData',rowData.id,rowData);
					});
					jQuery("#circleGrid").setGridParam({rowNum : <%=MapCache.getConfigValueAsInteger(SystemParametersConstant.TOTAL_ROWS_TO_DISPLAY_IN_GRID, 10)%>	}).trigger("reloadGrid");				
				}
			} else if (responseObject != undefined && responseObject != 'undefined' && responseCode == "400") {
				addErrorIconAndMsgForAjaxPopUp(responseObject);
			} else {
				resetWarningDisplayPopUp();
				showErrorMsgPopUp(responseMsg);
				reloadClientGridData();
			}
		},
		error : function(xhr, st, err) {
			hideProgressBar();
			$("#create-server-buttons-div").show();
			handleGenericError(xhr, st, err);
		}
	});
}

function displayCircleGrid(circleList) {
	
	$("#circleGrid").jqGrid({
			datatype : "local",
			colNames : [
					"<spring:message code='circle.data.mgmt.circle.id' ></spring:message>",
					"<spring:message code='circle.data.mgmt.circle.name' ></spring:message>",
					"<spring:message code='circle.data.mgmt.circle.description' ></spring:message>",					
					"<spring:message code='circle.data.mgmt.circle.deviceInfo' ></spring:message>",
					"<spring:message code='circle.data.mgmt.circle.licenseInfo' ></spring:message>",
					"<spring:message code='circle.data.mgmt.circle.upload.licneseKey' ></spring:message>",
					"<spring:message code='circle.data.mgmt.circle.edit.circle' ></spring:message>",
					"<spring:message code='circle.data.mgmt.circle.delete.circle' ></spring:message>",
					"<spring:message code='circle.data.mgmt.circle.associated' ></spring:message>",
					"<spring:message code='circle.data.mgmt.circle.license.applied' ></spring:message>",
					""					
			],
			colModel : [ {name : 'id', index : 'id', hidden : true}, 
						 {name : 'name', index : 'name', sortable : true}, 
						 {name : 'description',	index : 'description', sortable : false}, 
						 {name : 'device', index : 'deviceInfo', align:'center', formatter : mappedDevicesDetailsFormatter, sortable : false},
						 {name : 'licenseInfo', index : 'licenseInfo', align:'center', formatter : licenseInfoFormatter, sortable : false},						 						 
						 {name : 'license',	index : 'license', align:'center', formatter : uploadFormatter, sortable : false},
						 {name : 'edit', index:'edit', align:'center', formatter:editCircleFormatter, sortable : false},
						 {name : 'delete', index:'delete', align:'center', formatter:deleteCircleFormatter, sortable : false},
						 {name : 'isAssociated', index:'isAssociated', align:'center', hidden : true},
						 {name : 'isLicenseApplied', index:'isLicenseApplied', align:'center', hidden : true},
						 {name : 'isLicenseExhausted', index:'isLicenseExhausted', align:'center', hidden : true}						 
			],
			rowNum :<%=MapCache.getConfigValueAsInteger(SystemParametersConstant.TOTAL_ROWS_TO_DISPLAY_IN_GRID, 10)%>,
			rowList : [ 10, 20, 50, 100 ],
			height : 'auto',
			mtype : 'GET',
			sortname : 'name',
			sortorder : "asc",
			pager : "#circleGridPagingDiv",
			contentType : "application/json; charset=utf-8",
			viewrecords : true,
			multiselect : false,
			timeout : 120000,
			loadtext : "Loading...",
			caption : "<spring:message code='snmpClientList.grid.column.alertList.lable'></spring:message>",
			beforeRequest : function() {
				$(".ui-dialog-titlebar").hide();
			},
			beforeSend : function(xhr) {
				xhr.setRequestHeader("Accept",
						"application/json");
				xhr.setRequestHeader("Content-Type",
						"application/json");
			},
			loadComplete : function(data) {
				$(".ui-dialog-titlebar").show();				 				    
				$.each(data.rows, function (i, item) {
			        var rowId = data.rows[i].id || data.rows[i]._id_;
			        var myRow = new Array(item.valueOf());
			        var status = $("#circleGrid").getCell(rowId,"isLicenseExhausted");			        
			        if(status == 'true')
			        	jQuery('#circleGrid').setCell(rowId, 'name', '', { background: 'red', color : 'white'});			        
			    });
				
			},
			onPaging : function(pgButton) {

			},
			beforeSelectRow : function(rowid, e) {

			},
			loadError : function(xhr, st, err) {
				handleGenericError(xhr, st, err);
			},
			onSelectAll : function(id, status) {

			},
			recordtext : "<spring:message code='regex.parser.attr.grid.pager.total.records.text'></spring:message>",
			emptyrecords : "<spring:message code='regex.parser.attr.grid.empty.records'></spring:message>",
			loadtext : "<spring:message code='regex.parser.attr.grid.loading.text'></spring:message>",
			pgtext : "<spring:message code='regex.parser.attr.grid.pager.text'></spring:message>",
		}).navGrid("#circleGridPagingDiv", {
			edit : false,
			add : false,
			del : false,
			search : false
	});
	$(".ui-jqgrid-titlebar").hide();
}

/* Function will create new device */
function createCircle() {
	$("#add-circle-buttons-div").hide();
	$("#circle_proccess_bar_div").show();
	clearAllMessagesPopUp();
	resetWarningDisplayPopUp();
	clearResponseMsgDiv();
	$.ajax({
		url : 'createCircle',
		cache : false,
		async : true,
		dataType : 'json',
		type : "POST",
		data : {
			"id" : $("#circle_id").val(),
			"name" : $("#circle_name").val(),
			"description" : $("#circle_description").val(),
			"circleCurrentAction" : $("#circleCurrentAction").val()
		},
		success : function(data) {
			$("#circle_proccess_bar_div").hide();
			var response = eval(data);
			var responseCode = response.code;
			var responseMsg = response.msg;
			var responseObject = response.object;
			if (responseCode == "200") {
				$("#close-circle-buttons-div").show();
				$("#add-circle-buttons-div").hide();
				closeFancyBox();
				reloadGridData();
				showSuccessMsg(responseMsg);
			} else if (responseObject != undefined
					&& responseObject != 'undefined' && responseCode == "400") {
				$("#add-circle-buttons-div").show();
				$.each(responseObject, function(key,val){						 
					if(key == 'name' ){
						$("#circle_name").next().children().first().attr("data-original-title",val).attr("id",key+"_error");
					}else if(key == 'description'){
						$("#circle_description").next().children().first().attr("data-original-title",val).attr( "id",key+"_error");
					}
					addErrorClassWhenTitleExistPopUp($("#GENERIC_ERROR_MESSAGE", window.parent.document).val());
				}); 
			} else {
				showErrorMsgPopUp(responseMsg);
				$("#add-circle-buttons-div").show();
				$("#circle_proccess_bar_div").hide();
			}
		},
		error : function(xhr, st, err) {
			$("#circle_proccess_bar_div").hide();
			handleGenericError(xhr, st, err);
		}
	});
}

/* Function will display add device popup */
function addCirclePopup() {
	clearAllMessagesPopUp();
	resetWarningDisplayPopUp();
	$("#close-circle-buttons-div").hide();
	$("#add-circle-buttons-div").show();
	$("#create_circle_span").show();
	//$("#update_circle_span").hide();

	$("#circleCurrentAction").val("ADD");
	//$("#id").val("0");

	$("#addCircle_link").click();
	$("#circle_id").val("0");
	$("#create_circle_span").show();
	
	$("#create_circle_name_div").show();
	$("#create_circle_description_div").show();

	clearAllMessagesPopUp();
	resetWarningDisplayPopUp();

	$("#circle_name").val("");
	$("#circle_id").val("0");
	$("#circle_description").val("");

}

/*Function will reload grid data */
function reloadGridData(){
	getCircleList('','');
}

function uploadFormatter(cellvalue, options, rowObject) {	
    var circleId = rowObject["id"];
    if(uploadLicense){
	    if(circleId=='1'){
	    	return '<p></p>';
	    }else{
	    	return '<a id="'+circleId+'_upload" href="#" onclick="uploadPopUp('+"'" + circleId + "'"+');"><i class="fa fa-upload"></i></a>';    	
	    }
    }else{
    	return '<p></p>';
    }
} 

function uploadPopUp(id){
	$("#uploadTableId").val(id);
	document.getElementById("dataFile").value = '';
	$(":file").filestyle('clear');
	$("#uploadLicenseKey").click();
}

function getAssociatedDeviceInfoFormatter(cellvalue, options, rowObject){	
	var circleId = rowObject["id"];
	return "<a href='#' id='"+circleId+"' class='link'><i class='fa fa-list orangecolor'></i></a>";
}

function licenseInfoFormatter(cellvalue, options, rowObject){	
    var circleId = rowObject["id"];    
    var isLicenseApplied = rowObject["isLicenseApplied"];    
	if(circleId=="1"){
		return "<p></p>";
	}else if(isLicenseApplied){
		return "<a href='#' id='"+circleId+"' class='link' onclick=showLicenseInfo('"+circleId+"'); ><i class='fa fa-list orangecolor'></i></a>";
	}else{
		return "<a href='#' id='"+circleId+"' class='link' ><i class='fa fa-list greycolor'></i></a>";
	}
}

function editCircleFormatter(cellvalue, options, rowObject){
	var circleId = rowObject["id"];
	if(updateCircle){
		if(circleId!=1){
			return "<a href='#' id='"+circleId+"' class='link' onclick=updateCirclePopup('"+circleId+"'); ><i class='fa fa-pencil-square-o' aria-hidden='true'></i></a>";		
		}else{
			return '<p></p>';
		}
	}else{
    	return '<p></p>';
    }
}

function mappedDevicesDetailsFormatter(cellvalue, options, rowObject) {
	var circleId = rowObject["id"];
	if (rowObject["isAssociated"] === false) {
		return '<span style="cursor: pointer;" alt="'
				+ rowObject["isAssociated"]
				+ '"><i class="fa fa-retweet grey"></i></span>';
	} else {
		return '<span id="mapping_association_'	+ rowObject["id"] + '" src="img/orange.png"  style="cursor: pointer;" alt="'
				+ rowObject["id"] + '" onclick="viewAllMappedDevices('+"'"+rowObject["id"]+"'"+')"><i class="fa fa-retweet orange"></i></span>';
	}
}

function deleteCircleFormatter(cellvalue, options, rowObject){
	var circleId = rowObject["id"];
	var isAssociated = rowObject["isAssociated"];	
	if(deleteCircle){
	 	if(circleId == '1') {
			return '';
		} else if(isAssociated) { 
			return 'Associated';			
		} else { 
			return '<a href="#" id="'+rowObject["id"]+'_delete_lnk" onclick="deleteCirclePopup('+"'"+rowObject["id"]+"'"+','+"'"+rowObject["name"]+"','"+rowObject["description"]+"'"+')"><i class="fa fa-trash orange"></i></a>';
		}
	}else{
		return '';
	}
}

function updateCirclePopup(attributeId){
	 
 	clearAllMessagesPopUp();
	resetWarningDisplayPopUp();
 	
	if(attributeId > 0){
		
		var responseObject='';
		responseObject = jQuery("#circleGrid").jqGrid ('getRowData', attributeId);
		$("#circleId").val(responseObject.id);
		$("#update_name").val(responseObject.name);
		$("#update_desc").val(responseObject.description);
		
		$("#updateCirclePopup").click();
	}else{
		showErrorMsg(jsSpringMsg.failUpdatesnmpServerMsg);
	}
}

function showLicenseInfo(circleId) {
	$.ajax({url : '<%=ControllerConstants.LICENSE_INFO%>',
			cache : false,
			async : false,
			dataType : 'json',
			type : 'GET',
			data : {
				"id" : circleId
			},
			success : function(data) {
				var response = data;
				var responseCode = response.code;
				var responseObject = response.object;
				var tableString = "<table class='table table-hover table-bordered'  border='1'>";
				tableString += "<th>CIRCLE</th>";
				tableString += "<th>DEVICE</th>";
				tableString += "<th>APPLIED TPS</th>";
				tableString +="<th>LATEST TPS</th>";
				tableString +="<th>MAXIMUM TPS</th>";
				if (responseCode === "200") {
														
					$.each(responseObject,function(index, responseObject) {
						
						var appTps = responseObject.appliedTps;
						var curTps = responseObject.currentTps;
						var maxTps = responseObject.maxTps;
					
						tableString += "<tr>";
						tableString += "<td>" + responseObject.circleName;
						tableString += "</td>";
						tableString += "<td>" + responseObject.deviceName;
						tableString += "</td>";
						tableString += "<td>" + responseObject.appliedTps;
						tableString += "</td>";
						if(curTps > appTps){
							tableString += "<td class='overUtilized'>" + responseObject.currentTps;
							tableString += "</td>";
						}else{
							tableString += "<td>" + responseObject.currentTps;
							tableString += "</td>";
						}
						if(maxTps > appTps){
							tableString += "<td class='overUtilized'>" + responseObject.maxTps;
							tableString += "</td>";
						}else{
							tableString += "<td>" + responseObject.maxTps;
							tableString += "</td>";
						}										
						tableString += "</td>";
						tableString += "</tr>";										
					});
					tableString += "</table>";
					$('#license_info_div').html(tableString);
				} else if (responseObject !== undefined
						|| responseObject !== 'undefined'
						|| responseCode === "400") {
					tableString += "<tr>";
					tableString += "<td colspan='5' align='center'>"
							+ jsSpringMsg.attrNotFound + "</td>";
					tableString += "</tr>";
					tableString += "</table>";
					$('#license_info_div').html(tableString);
				}		
				$('#attribute_name_label').html('License Utilization Information');
				$("#licenseInfoPopup").click();
			},
			error : function(xhr, st, err) {
				handleGenericError(xhr, st, err);
			}

		});
}

function viewAllMappedDevices(circleId) {

	$.ajax({
		url : '<%=ControllerConstants.GET_MAPPED_DEVICES_INFO%>',
		cache : false,
		async : false,
		dataType : 'json',
		type : 'GET',
		data : {
			"id" : circleId
		},
		success : function(data) {
			var response = data;
			var responseCode = response.code;
			var responseObject = response.object;
			var tableString = "<table class='table table-hover table-bordered'  border='1'>";
			tableString += "<th>SERVER INSTANCE NAME</th>";
			tableString += "<th>SERVICE NAME</th>";
			tableString += "<th>DEVICE TYPE</th>";
			tableString +="<th>DEVICE NAME</th>";
			if (responseCode === "200") {				
				$.each(responseObject,function(index, responseObject) {
									tableString += "<tr>";
									tableString += "<td>" + responseObject.serverInstanceName;
									tableString += "</td>";
									tableString += "<td>" + responseObject.serviceName;
									tableString += "</td>";
									tableString += "<td>" + responseObject.deviceType;
									tableString += "</td>";
									tableString += "<td>" + responseObject.deviceName;
									tableString += "</td>";
									tableString += "</tr>";										
								});
				tableString += "</table>";
				$('#mapped_devices_info_div').html(tableString);
			} else if (responseObject !== undefined
					|| responseObject !== 'undefined'
					|| responseCode === "400") {
				tableString += "<tr>";
				tableString += "<td colspan='7' align='center'>"
						+ jsSpringMsg.attrNotFound + "</td>";
				tableString += "</tr>";
				tableString += "</table>";
				$('#mapped_devices_info_div').html(tableString);
			}		
			$('#mapped_devices_name_label').html('Devices And Circle Mapping Information');
			$("#mappedDevicesInfoPopup").click();
		},
		error : function(xhr, st, err) {
			handleGenericError(xhr, st, err);
		}

	});
}

function deleteCirclePopup(id, name, description){
	clearAllMessages();
	$("#delete_warning").show();
	$('#delete_label').show();
	$('#divDeleteCirclePopup').show();
	$("#deleteCircleId").val(id);
	$('#deleteCirclePopup').click();
	var tableString ="<table class='table table-hover table-bordered'  border='1' >";
		tableString+="<th><spring:message code='circle.data.mgmt.circle.name' ></spring:message></th>";
		tableString+="<th><spring:message code='circle.data.mgmt.circle.description' ></spring:message></th>";		
		tableString += "<tr>";
		tableString += "<td>"+name+"</td>";
		tableString += "<td>"+description+"</td>";
		tableString += "</tr>";
		tableString+="</table>";
		$("#delete_selected_query_details").html(tableString);
		$("#delete_attribute_bts_div").show();
		$("#delete_attribute_progress_bar_div").hide();
		$("#delete_close_attribute_buttons_div").hide(); 
}

function deleteCircle1(){
	$("#delete_attribute_bts_div").hide();
	$("#delete_attribute_progress_bar_div").show();
	$("#delete_close_attribute_buttons_div").hide();
	var deleteCircleId = $("#deleteCircleId").val();
	$.ajax({
		url: '<%=ControllerConstants.DELETE_CIRCLE%>',
		cache: false,
		async: true,
		dataType:'json',
		type:'POST',
		data:
		 {
			"id" : deleteCircleId
		 }, 
		success: function(data){
			var response = eval(data);
			var responseCode = response.code; 
			var responseMsg = response.msg; 
		
			clearAllMessagesPopUp();
			resetWarningDisplayPopUp();
			 
			if(responseCode === "200"){
				$("#delete_attribute_bts_div").hide();
				$("#delete_attribute_progress_bar_div").hide();
				$("#delete_warning").hide();
				reloadGridData();
				showSuccessMsg(responseMsg);
				closeFancyBox();				
			}else{
				showErrorMsg(responseMsg);
				$("#delete_attribute_bts_div").show();
				$("#delete_attribute_progress_bar_div").hide();
				$("#delete_close_attribute_buttons_div").hide();
			}
		},
	    error: function (xhr,st,err){
	    	closeFancyBox();
			handleGenericError(xhr,st,err);			
		}
	});
}

</script>
