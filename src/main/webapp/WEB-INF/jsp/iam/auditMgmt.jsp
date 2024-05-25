<%@ taglib 	uri="http://www.springframework.org/security/tags" 	prefix="sec"  %>
<%@taglib 	uri="http://www.springframework.org/tags" prefix="spring"%>
<%@page import="com.elitecore.sm.util.MapCache"%>
<%@page import="com.elitecore.sm.common.constants.SystemParametersConstant"%>
<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<%@page import="com.elitecore.sm.common.constants.BaseConstants"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<style>
.table-cell-label {
    display: table-cell;
    vertical-align: middle;
    width: 140px;
}
</style>
<script src="${pageContext.request.contextPath}/js/ajaxfileupload.js"></script>
<div class="tab-content no-padding clearfix" id="service-manager-block">
	
	<form name="search-staff-audit"  action="javascript:;" id="search-staff-audit-form">
	<sec:authorize access="hasAuthority('VIEW_AUDIT')">
		<div class="title2">
			 <spring:message code="staffManager.audit.search.heading.label" ></spring:message> 
		</div>
        <div class="fullwidth borbot">
         	<div class="col-md-6 inline-form no-padding">
					<spring:message code="staffManager.audit.search.entity.lable.name" var="tooltip" ></spring:message>
					<div class="form-group">
						<div class="table-cell-label">${tooltip}</div>
						<div class="input-group ">
							<select  name = "auditEntity" class="form-control table-cell input-sm"  tabindex="1" id="auditEntity" data-toggle="tooltip" data-placement="bottom"  title="${tooltip }" onchange="getAuditMasterEntity('SUBENTITY',this.value);"  >
            						<option  value="-1" selected="selected"><spring:message	code="staffAudit.management.entity.dropdown.default.label" ></spring:message></option>
            					</select>
            					<span class="input-group-addon add-on last" > <i class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="left" title=""></i></span>
						</div>
					</div>
				</div>
		         
				 <div class="col-md-6 inline-form no-padding">
					<spring:message code="staffManager.audit.search.sub.entity.lable.name" var="tooltip" ></spring:message>
					<div class="form-group">
						<div class="table-cell-label">${tooltip}</div>
						<div class="input-group ">
								<select  name = "auditSubEntity" class="form-control table-cell input-sm"  tabindex="2" id="auditSubEntity" data-toggle="tooltip" data-placement="bottom"  title="${tooltip }" onchange="getAuditMasterEntity('ACTION',this.value);">
            						<option  value="-1" selected="selected"><spring:message	code="staffAudit.management.action.dropdown.default.label" ></spring:message></option>
            					</select>
            					<span class="input-group-addon add-on last" > <i class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="left" title=""></i></span>
						</div>
					</div>
				</div>
				<div class="col-md-6 inline-form no-padding">
					<spring:message code="staffManager.audit.search.action.lable.name" var="tooltip" ></spring:message>
					<div class="form-group">
						<div class="table-cell-label">${tooltip}</div>
						<div class="input-group ">
							<select  name = "auditAction" class="form-control table-cell input-sm"  tabindex="3" id="auditAction" data-toggle="tooltip" data-placement="bottom"  title="${tooltip }">
            						<option  value="-1" selected="selected"><spring:message	code="staffAudit.management.sub.entity.dropdown.default.label" ></spring:message></option>
            					</select>
            					<span class="input-group-addon add-on last" > <i class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="left" title=""></i></span>
						</div>
					</div>
				</div>
				 <div class="col-md-6 inline-form no-padding">
					<spring:message	code="staffManager.audit.search.staff.lable.name"	var="tooltip" ></spring:message>
					<div class="form-group">
						<div class="table-cell-label">${tooltip}</div>
						<div class="input-group ">
								<input type="text" id="staffName" name="staffName" maxlength="100" tabindex="4" class="form-control table-cell input-sm" data-toggle="tooltip" data-placement="bottom" title="${tooltip }"  >
            					<span class="input-group-addon add-on last" > <i class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="left" title=""></i></span>
						</div>
					</div>
				</div>
				<div class="col-md-6 inline-form no-padding">
					<spring:message	code="staffManager.audit.search.label.duration"	var="tooltip" ></spring:message>                  	
                   	<div class="form-group">
                     	<div class="table-cell-label"><div class="table-cell-label">${tooltip}</div></div>
								<input type="text" id="durationFrom" name="durationFrom" maxlength="100" tabindex="5" class="form-control table-cell input-sm" data-toggle="tooltip" data-placement="bottom" title="${tooltip }"  >
            					<span class="input-group-addon add-on last" > <i class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="left" title=""></i></span>
							    
							    <input type="text" id="durationTo" name="durationTo" maxlength="100"  tabindex="6" class="form-control table-cell input-sm" data-toggle="tooltip" data-placement="bottom" title="${tooltip }"  >
            					<span class="input-group-addon add-on last" > <i class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="left" title=""></i></span>                 	 
                      </div>
    			</div> 
    			<div class="col-md-6 inline-form no-padding">
					<spring:message	code="staffManager.audit.search.label.action.type"	var="tooltip" ></spring:message>                  	
                   	<div class="form-group">
                     	<div class="table-cell-label"><div class="table-cell-label">${tooltip}</div></div>
								<select  name = "actionType" class="form-control table-cell input-sm"  tabindex="2" id="actionType" data-toggle="tooltip" data-placement="bottom"  title="${tooltip }" onchange="getAuditMasterEntity('ACTION',this.value);">
            						<option  value="ALL" selected="selected"><spring:message	code="staffAudit.management.action.type.dropdown.default.label"	></spring:message></option>
            						<option  value="<%=BaseConstants.CREATE_ACTION%>" ><%=BaseConstants.CREATE_ACTION%></option>
            						<option  value="<%=BaseConstants.UPDATE_ACTION%>" ><%=BaseConstants.UPDATE_ACTION%></option>
            						<option  value="<%=BaseConstants.DELETE_ACTION%>" ><%=BaseConstants.DELETE_ACTION%></option>
            						<option  value="<%=BaseConstants.SM_ACTION%>" ><%=BaseConstants.SM_ACTION%></option>
            					</select>
            					<span class="input-group-addon add-on last" > <i class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="left" title=""></i></span>
                      </div>
    			</div>
    				<%-- <div class="col-md-6 inline-form no-padding">
					<spring:message	code="staffManager.audit.search.label.action.type"	var="tooltip" ></spring:message>                  	
                   	<div class="form-group">
                     	<div class="table-cell-label"><div class="table-cell-label">${tooltip}</div></div>
							
							<input type="file" class="filestyle form-control" tabindex="14"
							data-buttonText="<spring:message code="serviceMgmt.browse.file"></spring:message>"
							id="templateFile" name="templateFile" accept='text/xml'>
            				<button class="btn btn-grey btn-xs" id="tempUpload">Upload Template</button>
            					
                      </div>
    			</div> --%>
    			
    			
         	<div class="clearfix"></div>
     		<div class="pbottom15 form-group ">
     			<sec:authorize access="hasAuthority('VIEW_AUDIT')">
   					<button id = "searchAuditId" class="btn btn-grey btn-xs" onclick="searchAuditDetails();"><spring:message code="btn.label.search" ></spring:message></button>
    				<button id = "resetSearchAuditId" class="btn btn-grey btn-xs" onclick="resetSearchAuditCriteria();"><spring:message code="btn.label.reset" ></spring:message></button>
    			</sec:authorize>
   			</div>
   			
   		</div>
   	</sec:authorize>
   	</form>
        <div class="tab-content no-padding clearfix">
         	<div class="fullwidth">
   				<div class="title2">
   					<spring:message code="staffManager.audit.grid.heading" ></spring:message> 
		        </div>
		        
   			</div>
           	<!-- Morris chart - Sales -->
           	<sec:authorize access="hasAuthority('VIEW_AUDIT')">
            <div class="box-body table-responsive no-padding box">
             	<table class="table table-hover" id="staffAuditList"></table>
               	<div id="staffAuditListDiv"></div> 
	           	<div class="clearfix"></div> 
	           	<div id="divLoading" align="center" style="display: none;"><img src="img/preloaders/Preloader_10.gif" /></div>  
            </div>
            </sec:authorize>
        </div>
        
        <form id="audit-form" method="POST" action="<%= ControllerConstants.VIEW_STAFF_AUDIT_DETAILS %>">
			<input type="hidden" id="systemAuditId" name="systemAuditId" value="">
		</form>
        
</div>
<script type="text/javascript">
	$("#durationFrom").datepicker();
	$("#durationTo").datepicker();
	
		if(currentTab == 'STAFF_AUDIT_MANAGEMENT'){
			$(document).ready(function() {
				<sec:authorize access="hasAuthority('VIEW_AUDIT')">
					getAuditMasterEntity('ENTITY', '-1');
					getStaffAuditList();
				</sec:authorize>
			});
	   }
	var entityId, entityType;
	function getAuditMasterEntity(selEntityType,selEnityId){
		
		entityId = selEnityId;
		entityType = selEntityType;
		 $.ajax({
				url: '<%=ControllerConstants.GET_AUDIT_ENTITY%>',
				cache: false,
				async: true,
				dataType:'json',
				type:'POST',
				data:
				 {
					"entityId"   : entityId,
					"entityType"  : entityType,
				}, 
				success: function(data){
					var response = eval(data);
					var responseCode = response.code; 
					var responseMsg = response.msg; 
					var responseObject = response.object;
					if(responseCode == "200"){
						if(entityType == 'ENTITY' &&  entityId == '-1' ){
							setAuditEntity('auditEntity', responseObject);
						}else if(entityType == 'SUBENTITY'  ){
							$("#auditSubEntity").empty();
							$("#auditSubEntity").html("<option value='-1' selected='selected'>SELECT SUB ENTITY</option>");
							$("#auditAction").empty();
							$("#auditAction").html("<option value='-1' selected='selected'>SELECT ACTION</option>");
							setAuditEntity('auditSubEntity', responseObject);
						}else{
							$("#auditAction").empty();
							$("#auditAction").html("<option value='-1' selected='selected'>SELECT ACTION</option>");
							setAuditEntity('auditAction', responseObject);
						}
						
					}else if(responseObject != undefined && responseObject != 'undefined' && responseCode == "400"){
						console.log("Not able to fetch the entity list.");
					}
				},
			    error: function (xhr,st,err){
					handleGenericError(xhr,st,err);
				}
			}); 
	}
	
	function setAuditEntity(elementId, data){
		$.each(data, function(index, data) {
	        $('#'+elementId).append("<option value='"+data.id +"'>"+data.alias+"</option>");
	    });
	}	
	
	function resetSearchAuditCriteria(){
		$("#auditEntity").val('-1');
		$("#auditSubEntity").val('-1');
		$("#auditAction").val('-1');
		
		$("#staffName").val('');
		$("#durationFrom").val('');
		$("#durationTo").val('');
		searchAuditDetails();
		
	}
	
	function searchAuditDetails(){
		clearAllMessages();
		clearResponseMsgDiv();
		clearResponseMsg();
		clearErrorMsg();
		
		var $grid = $("#staffAuditList");
		jQuery('#staffAuditList').jqGrid('clearGridData');
		clearAuditGrid();
		$grid.jqGrid('setGridParam',{datatype:'json',page: 1,sortname: 'id',sortorder: "desc", 
			postData:
    		{
        		'entityId': function () {
        	        return $("#auditEntity").val();
   	    		},
   	    		'subEntityId': function () {
        	        return $("#auditSubEntity").val();
   	    		},
   	    		'auditActivityId': function(){
   	    			return $("#auditAction").val();
   	    		},
   	    		'userName': function () {
        	        return $("#staffName").val();
   	    		},
   	    		'actionType': function () {
        	        return $("#actionType").val();
   	    		},
   	    		
   	    		'durationFrom': function () {
        	        return $("#durationFrom").val();
   	    		},
   	    		'durationTo': function () {
   	    		 	return $("#durationTo").val();
   	    		}
    		}
		}).trigger('reloadGrid');
		
	} 
	function clearAuditGrid(){
		var $grid = $("#staffAuditList");
		var rowIds = $grid.jqGrid('getDataIDs');
		// iterate through the rows and delete each of them
		for(var i=0,len=rowIds.length;i<len;i++){
			var currRow = rowIds[i];
			$grid.jqGrid('delRowData', currRow);
		}
	}
	function getStaffAuditList(){
		$("#staffAuditList").jqGrid({
        	url: "<%= ControllerConstants.GET_STAFF_AUDIT_LIST%>",
        	postData:
        		{
	        		'entityId': function () {
	        	        return $("#auditEntity").val();
	   	    		},
	   	    		'subEntityId': function () {
	        	        return $("#auditSubEntity").val();
	   	    		},
	   	    		'auditActivityId': function(){
	   	    			return $("#auditAction").val();
	   	    		},
	   	    		'userName': function () {
	        	        return $("#staffName").val();
	   	    		},
	   	    		'actionType': function () {
	        	        return $("#actionType").val();
	   	    		},
	   	    		'durationFrom': function () {
	        	        return $("#durationFrom").val();
	   	    		},
	   	    		'durationTo': function () {
	   	    		 	return $("#durationTo").val();
	   	    		}
        		},
            datatype: "json",
            colNames:[
					  "<spring:message code='staffAudit.management.grid.column.id' ></spring:message>",
                      "<spring:message code='staffAudit.management.grid.column.name' ></spring:message>",
                      "<spring:message code='staffAudit.management.grid.column.action' ></spring:message>",
                      "<spring:message code='staffAudit.management.grid.column.remark' ></spring:message>",
                      "<spring:message code='staffAudit.management.grid.column.ipaddress' ></spring:message>",
                      "<spring:message code='staffAudit.management.grid.column.date' ></spring:message>",
                      "<spring:message code='staffAudit.management.grid.column.detailview' ></spring:message>",
                      "<spring:message code='staffAudit.management.grid.column.count' ></spring:message>",
                      "<spring:message code='staffAudit.management.grid.column.detailview' ></spring:message>",
                     ],
			colModel:[
				{name:'id',index:'id',sortable:true,hidden: true},
				{name:'staffName',index:'staffName',sortable:true,editable: true},
				{name:'currentAction',index:'currentAction',sortable:false},
				{name:'remark',index:'remark',sortable:false},
            	{name:'ipAdress',index:'ipAdress',sortable:false, align:'center'},
				{name:'auditDate',index:'auditDate',sortable:true},
				{name:'viewDetails',index:'viewDetails',sortable:false, align:'center',formatter:detailViewFormatter},
				{name:'actionType',index:'actionType',sortable:false, hidden: true },
				{name:'sysAuditDetailsAvailable',index:'sysAuditDetailsAvailable',sortable:false, hidden: true },
				
            ],
            rowNum:<%= MapCache.getConfigValueAsInteger(SystemParametersConstant.TOTAL_ROWS_TO_DISPLAY_IN_GRID, 10) %>,
            rowList:[10,20,60,100],
            height: 'auto',
            mtype:'POST',
			sortname: 'auditDate',
     		sortorder: "desc",
            pager: "#staffAuditListDiv",
            contentType: "application/json; charset=utf-8",
            viewrecords: true,
            multiselect: false,
            timeout : 120000,
            loadtext: "Loading...",
            caption: "<spring:message code='staffAudit.management.grid.caption'></spring:message>",
            
            beforeRequest:function(){
                $(".ui-dialog-titlebar").hide();
            }, 
            beforeSend: function(xhr) {
                xhr.setRequestHeader("Accept", "application/json");
                xhr.setRequestHeader("Content-Type", "application/json");
            },
     		 loadComplete: function(data) {
     			$(".ui-dialog-titlebar").show();
     			if ($('#staffAuditList').getGridParam('records') === 0) {
                    $('#staffAuditList tbody').html("<div style='padding:6px;background:#D8D8D8;text-align:center;'><spring:message code="staffAudit.management.grid.empty.records"></spring:message></div>");
                    $("#staffAuditListDiv").hide();
                }else{
                	$("#staffAuditListDiv").show();
                	ckIntanceSelected = new Array();
                }
     			
     			var $jqgrid = $("#staffAuditList");      
			}, 
			onPaging: function (pgButton) {
				clearResponseMsgDiv();
				//clearInstanceGrid();
			},
			loadError : function(xhr,st,err) {
				handleGenericError(xhr,st,err);
			},
			beforeSelectRow: function (rowid, e){
				// this blank function will not select the entire row. Only checkbox can be selectable.
			},
			recordtext: "<spring:message code="staffAudit.management.grid.pager.total.records.text"></spring:message>",
	        emptyrecords: "<spring:message code="staffAudit.management.grid.empty.records"></spring:message>",
			loadtext: "<spring:message code="staffAudit.management.grid.loading.text"></spring:message>",
			pgtext : "<spring:message code="staffAudit.management.grid.pager.text"></spring:message>",
			cellEdit: true, 
		}).navGrid("#staffAuditListDiv",{edit:false,add:false,del:false,search:false});
		
		$(".ui-jqgrid-titlebar").hide();
	}
	
	function detailViewFormatter(cellvalue, options, rowObject){
		
		<sec:authorize access="hasAuthority('VIEW_AUDIT')">
			var actionType = rowObject["actionType"];
			var sysAuditDetailsAvailable = rowObject["sysAuditDetailsAvailable"];
			if(actionType  == 'UPDATE_ACTION' && sysAuditDetailsAvailable == true){				
				return '<a class="link" onclick="viewAuditDetails('+"'" + rowObject["id"]+ "'" + ')"><i class="fa fa-th-list orange"></i></a>' ;	
			}else{
				return '<a class="link"><i class="fa fa-th-list grey"></i></a>' ;
			}
		</sec:authorize>
	}
	
	
	function viewAuditDetails(sysAuditId){
		$("#systemAuditId").val(sysAuditId)
		$("#audit-form").submit();
	}
	
	
	
//Temp parsing file upload code.
	
$(document).on("click","#tempUpload",function() {
		
		var file = $("#templateFile").val();
		console.log("File object is : " + file);
		var actionUrl = '<%=ControllerConstants.IMPORT_TEMPLATE_FILE%>';
		uploadTemplate(actionUrl);
		
    });		
$(document).on("change","#templateFile",function(event) {
	   files=event.target.files;
	   $('#templateFile').html(files[0].name);
	});	
	
function uploadTemplate(actionUrl){
	
	var oMyForm = new FormData();
    oMyForm.append("file", files[0]);
    
   $.ajax({dataType : 'json',
          url : actionUrl,	
          data : oMyForm ,
          type : "POST",
          enctype: 'multipart/form-data',
          processData: false, 
          contentType:false,
          success : function(response) {

  			var responseCode = response.code; 
  			var responseMsg = response.msg; 
  			var responseObject = response.object;
  			
  			if(responseCode == "200"){
  				console.log("Got response successfully.");
			}
			
          },
          error : function(response){
        	  console.log("Error found in upload file.");
          }
      });	
	
}
	
</script>
