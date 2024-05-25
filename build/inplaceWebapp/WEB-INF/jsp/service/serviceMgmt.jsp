<%@page import="com.elitecore.sm.util.MapCache"%>
<%@page import="com.elitecore.sm.common.constants.SystemParametersConstant"%>
<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<%@page import="com.elitecore.sm.util.EliteUtils"%>
<%@page import="com.elitecore.sm.common.constants.BaseConstants"%>
<%@taglib 	uri="http://www.springframework.org/tags" 			prefix="spring"%>
<%@ taglib 	uri="http://www.springframework.org/security/tags" 	prefix="sec"  %>
<%@taglib uri="/WEB-INF/ShowErrorTag.tld" prefix="elitecoreError" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<head>
<script src="${pageContext.request.contextPath}/js/bootstrap.min.js" type="text/javascript"></script>
<script type='text/javascript' src="${pageContext.request.contextPath}/js/jquery.ajaxfileupload.js"></script>

 <style>
 .ui-jqgrid .loading { 
	display: block !important; 
	background: url(img/sm-ajax-loader.gif); 
	border-style: none;
    background-repeat: no-repeat;
    z-index: 10000;
    height: 32px;
    width: 32px;
  }   
  
.loadingHide{
	display: none !important;
}

.loadingDisplay{
	display: block !important;
}
</style> 
</head>


<div class="tab-content no-padding clearfix" id="service-manager-block">
	
	<form name="search-service-mgmt-form"  action="javascript:;" id="search-service-mgmt-form">
	<sec:authorize access="hasAuthority('SEARCH_SERVICE_INSTANCE')">
		<div class="title2">
			<spring:message code="serviceManagement.page.heading" ></spring:message>
		</div>
        <div class="fullwidth borbot">
        	 <div class="col-md-6 inline-form" style="padding-left: 0px !important;">
        	 
        	 	<div class="form-group">
	             	<spring:message code="serviceManagement.search.service.id" var="label" ></spring:message>
	             	<spring:message code="serviceManagement.search.service.id.tooltip" var="tooltip" ></spring:message>
	         		<div class="table-cell-label">${label}</div>
	             	<div class="input-group ">
	             		<input type="text" id="serviceInstanceId" name="id" maxlength="100" class="form-control table-cell input-sm" data-toggle="tooltip" data-placement="bottom" title="${tooltip }"  >
	             		<span class="input-group-addon add-on last" > <i id="serviceInstance_err" class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="left" title=""></i></span>
	             	</div>
	             </div>
	             
	             <div class="form-group">
	         		<spring:message code="serviceManagement.search.service.type" var="label" ></spring:message>
	         		<spring:message code="serviceManagement.search.service.type.tooltip" var="tooltip" ></spring:message>
	         		<div class="table-cell-label">${label }</div>
	             	<div class="input-group " style="display: table;">
	             		<select  name = "searchServiceType" class="form-control table-cell input-sm"  tabindex="3" id="searchServiceType" data-toggle="tooltip" data-placement="bottom"  title="${tooltip }">
	             			<option  value="-1" selected="selected">SELECT SERVICE TYPE</option>
	             			<c:if test="${SERVICE_TYPE_LIST !=null && fn:length(SERVICE_TYPE_LIST) gt 0}">
			             		<c:forEach items="${SERVICE_TYPE_LIST}" var="serviceType">
			             			<option value="${serviceType.alias}">${serviceType.type}</option>
			             		</c:forEach>
		             		</c:if>
	             		</select>
	             		<span class="input-group-addon add-on last" > <i id="serviceType_err" class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="left" title=""></i></span>
	             	</div>
	             </div>
	             
	           	  <div class="form-group">
	             	<spring:message code="serviceManagement.search.server.sync.status" var="tooltip" ></spring:message>
	         		<div class="table-cell-label">${tooltip }</div>
	             	<div class="input-group ">
	             		 <input type="radio" name="syncStatus" id="syncStatusYes" style="padding-right: 15px;" value="true"/>&nbsp;&nbsp;<spring:message code="synchronize.search.status.sync" ></spring:message>
	             		&nbsp;&nbsp;&nbsp;<input type="radio" id="syncStatusNo" name="syncStatus" style="padding-right: 15px;" value="false"/>&nbsp;&nbsp;<spring:message code="synchronize.search.status.notsync" ></spring:message>
	             	</div>
	             </div>
     	  	</div> 
         	<div class="col-md-6 inline-form" style="padding-left: 0px !important;">
         	
         		<div class="form-group">
	         		<spring:message code="serviceManagement.search.service.instance.name"  var="label"></spring:message>
	         		<spring:message code="serviceManagement.search.service.instance.name.tooltip"  var="tooltip"></spring:message>
	         		
	         		<div class="table-cell-label">${label }</div>
	             	<div class="input-group ">
	             		<input type="text" id="serviceInstanceName"  name="name" maxlength="100" class="form-control table-cell input-sm" data-toggle="tooltip" data-placement="bottom" title="${tooltip }"  >
	             		<span class="input-group-addon add-on last" > <i id="serviceName_err" class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="left" title=""></i></span> 
	             		
	             	</div>
	             </div>
	             
	             <div class="form-group">
	             	<spring:message code="serviceManagement.search.service.serverinstance.name" var="label" ></spring:message>
	             	<spring:message code="serviceManagement.search.service.serverinstance.name.tooltip" var="tooltip" ></spring:message>
	         		<div class="table-cell-label">${label }</div>
	             	<div class="input-group ">
	             		<input type="text" id="serverInstanceName" name="serverInstance.name" maxlength="100" class="form-control table-cell input-sm" data-toggle="tooltip" data-placement="bottom" title="${tooltip }"  >
	             		<span class="input-group-addon add-on last" > <i id="serverInstanceName_err" class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="left" title=""></i></span> 
	             	</div>
	             </div>
	             
	              <div class="form-group">
	         		<div class="table-cell-label">&nbsp;</div>
	             	<div class="input-group ">
	             		&nbsp;&nbsp;&nbsp;
	             	</div>
	             </div> 
         	</div>
         	<div class="clearfix"></div>
     		<div class="pbottom15 form-group ">
     			<sec:authorize access="hasAuthority('SEARCH_SERVICE_INSTANCE')">
   					<button id="searchServiceBtn" class="btn btn-grey btn-xs" onclick="loadGridData();"><spring:message code="btn.label.search" ></spring:message></button>
    				<button id="resetSearchServiceBtn" class="btn btn-grey btn-xs" onclick="resetSearchParams();"><spring:message code="btn.label.reset" ></spring:message></button>
    			</sec:authorize>
   			</div>
   			
   		</div>
   	</sec:authorize>
   	</form>
        <div class="tab-content no-padding clearfix">
         	<div class="fullwidth">
   				<div class="title2">
   					<spring:message code="serviceManagement.grid.heading" ></spring:message>
	   				 <span class="title2rightfield">
				          
				          	<sec:authorize access="hasAuthority('CREATE_SERVICE_INSTANCE')">
				          	<span class="title2rightfield-icon1-text">
				          		<a href="#" onclick="moveToCreateService('create_service');"><i class="fa fa-plus-circle"></i></a>
	          					<a href="#" id="addService" onclick="moveToCreateService('create_service');">
	          						<spring:message code="btn.label.add" ></spring:message>
	          					</a>
		          			</span>	
				          	</sec:authorize>
				          
				          	<sec:authorize access="hasAuthority('DELETE_SERVICE_INSTANCE')">
				          		<span class="title2rightfield-icon1-text">
				          		<a href="#" onclick="deletePopup('service_mgmt','','','');"> <i class="fa fa-trash"></i></a>
				          		<a href="#" id="deleteService" onclick="deletePopup('service_mgmt','','','');"><spring:message code="btn.label.delete" ></spring:message></a>
				          		</span>
				          	</sec:authorize>
				          	
				          	<a href="#divSynchronize" class="fancybox" style="display: none;" id="synchronize">
				          		#
				          	</a>
				          	<a href="#divImportConfig" class="fancybox" style="display: none;" id="importconfig">
				          		#
				          	</a>
				          	<a href="#divSyncPublish" class="fancybox" style="display: none;"
									id="syncPublish">#</a>
				          	<a href="#divStartService" class="fancybox" style="display: none;" id="startService">#</a>
				          	<a href="#divStopService" class="fancybox" style="display: none;" id="stopService">#</a>
				          </span>
		          </div>
   			</div>
           	<!-- Morris chart - Sales -->
            <div class="box-body table-responsive no-padding box">
             	<table class="table table-hover" id="serviceInstanceList"></table>
               	<div id="serviceInstanceListPagingDiv"></div> 
	           	<div class="clearfix"></div> 
	           	<div id="divLoading" align="center" style="display: none;"><img src="img/preloaders/Preloader_10.gif" /></div>  
            </div>
        </div>
		
		
		
		<div id="divImportConfig" style=" width:100%; display: none;" >
		   <jsp:include page="importConfigPopUp.jsp"></jsp:include>
		</div>
			<jsp:include page="../server/divSyncPublish.jsp"></jsp:include>
		<div id="divSynchronize" style=" width:100%; display: none;" >
		    <jsp:include page="synchronizationPopUp.jsp"></jsp:include>
		</div>
		
		<div id="divStartService" style=" width:100%; display: none;" >
		    <jsp:include page="startServicePopUp.jsp"></jsp:include>
		</div>
		<div id="divStopService" style=" width:100%; display: none;" >
		   <jsp:include page="stopServicePopUp.jsp"></jsp:include> 
		</div>
		
		<a href="#divChangeServiceStatus" class="fancybox" style="display: none;" id="updateServiceStatus">#</a>
		<div id="divChangeServiceStatus" style=" width:100%; display: none;" >
		    <div class="modal-content">
		        <div id="changeStatuslbl" class="modal-header padding10">
		            <h4 class="modal-title"><spring:message code="change.service.status.lable"></spring:message></h4>
		        </div>
		        <div id="startServicelbl" class="modal-header padding10">
		            <h4 class="modal-title"><spring:message code="serviceMgmt.start.popup.header"></spring:message></h4>
		        </div>
		        <div class="modal-body padding10 inline-form">
		        	<input type="hidden" id="serviceEnableStatus" name="serviceEnableStatus" />
		        	<input type="hidden" id="serviceEnableId" name="serviceEnableId" />
		        	
		        	<jsp:include page="../common/responseMsgPopUp.jsp" ></jsp:include>
		        	
			        <p id="active-service-warning" style="display:none;">
			       		 <spring:message code="change.service.status.popup.msg" ></spring:message>
			        </p>
			        <p id="inactive-service-warning" style="display:none;">
			       		 <spring:message code="change.service.status.popup.msg" ></spring:message>
			        </p>
			        <p id="on-service-warning" style="display:none;">
			       		<spring:message code="change.service.status.start.service.popup.msg" ></spring:message> 
			        </p>
			        <p id="off-service-warning" style="display:none;">
			       		<spring:message code="change.service.status.inactive.service.popup.msg" ></spring:message> 
			        </p>
			       
		        </div>

			        <div  class="modal-footer padding10">
			        	<div id="update-service-div">
			        		<sec:authorize access="hasAuthority('UPDATE_SERVICE_INSTANCE')">
			            		<button id="service_status_yes" type="button" class="btn btn-grey btn-xs " onclick="changeServiceStatus();"><spring:message code="btn.label.yes"></spring:message></button>
			            	</sec:authorize>
			            	<button id="service_status_no" type="button" class="btn btn-grey btn-xs " data-dismiss="modal" onclick="setDefaultStatus();"><spring:message code="btn.label.no"></spring:message></button>
			            </div>
			              <button type="button" id="close-btn" class="btn btn-grey btn-xs " onclick="setStartDefaultStatus();"><spring:message code="btn.label.close"></spring:message></button>
			              <button type="button" id="enable-close-btn" class="btn btn-grey btn-xs " onclick="setDefaultStatus();"><spring:message code="btn.label.close"></spring:message></button>
			              <button type="button" id="cancel-btn" class="btn btn-grey btn-xs " onclick="searchInstanceCriteria();closeFancyBox();"><spring:message code="btn.label.close"></spring:message></button>
			              
			        </div>
			       
			        <div id="update-service-progress-div" class="modal-footer padding10">
			           <jsp:include page="../common/processing-bar.jsp"></jsp:include>
			        </div>
			        
		    </div>
		    <!-- /.modal-content --> 
		</div>
		
		
</div>  

<script type="text/javascript">
 
		$(document).ready(function() {
			getSearviceInstaceListBySearchParams();
 			$(window).keydown(function(event){
 				
			    if(event.keyCode == 13) {
			      loadGridData();
			    }
			  });
 			
 			
 		});
		

		function loadGridData(){
			clearAllMessages();
			clearResponseMsgDiv();
    		clearResponseMsg();
    		clearErrorMsg();
    		clearSelection();
    		
			var $grid = $("#serviceInstanceList");
			jQuery('#serviceInstanceList').jqGrid('clearGridData');
			clearInstanceGrid();
			$grid.jqGrid('setGridParam',{datatype:'json',page: 1,sortname: 'id',sortorder: "desc", postData:{
        		'serviceId': function () {
        			var id  =  $("#serviceInstanceId").val();
        			if(id == 'undefined' || id == ''){
        				id = '0';
        			}
        	        return id;
   	    		},
   	    		'serviceInstanceName': function () {
        	        return $("#serviceInstanceName").val();
   	    		},
   	    		'serverInstanceName': function(){
   	    			return $("#serverInstanceName").val();
   	    		},
   	    		'status': function () {
        	        return $('input[name=status]:checked', '#search-service-mgmt-form').val();
   	    		},
   	    		'serviceType': function () {
        	        return $("#searchServiceType").val();
   	    		},
   	    		'syncStatus': function () {
   	    			return $('input[name=syncStatus]:checked', '#search-service-mgmt-form').val();
   	    		}
    		}}).trigger('reloadGrid');
			
		} 
		var oldGrid = '';
		function getSearviceInstaceListBySearchParams(){
			$("#serviceInstanceList").jqGrid({
	        	url: "<%= ControllerConstants.GET_SERVICE_INSTANCE_LIST%>",
	        	postData:
	        		{
		        		'serviceId': function () {
		        			var id  =  $("#serviceInstanceId").val();
		        		
		        			if(id == 'undefined' || id == ''){
		        				id = '0';
		        			}
		        	        return id;
		   	    		},
		   	    		'serviceInstanceName': function () {
		        	        return $("#serviceInstanceName").val();
		   	    		},
		   	    		'serverInstanceName': function(){
		   	    			return $("#serverInstanceName").val();
		   	    		},
		   	    		'status': function () {
		        	        return $("input:radio[name='status']:checked").val();
		   	    		},
		   	    		'serviceType': function () {
		        	        return $("#searchServiceType").val();
		   	    		},
		   	    		'syncStatus': function () {
		        	        return $("input:radio[name='syncStatus']:checked").val();
		   	    		}
	        		},
	            datatype: "json",
	            colNames:["#",
						  "<spring:message code='serviceManagement.grid.column.id' ></spring:message>",
	                      "<spring:message code='serviceManagement.grid.column.service.id' ></spring:message>",
	                      "<spring:message code='serviceManagement.grid.column.service.serviceInstanceId' ></spring:message>",
	                      "<spring:message code='serviceManagement.grid.column.service.name' ></spring:message>",
	                      "<spring:message code='serviceManagement.grid.column.service.type' ></spring:message>",
	                      "",
	                      "<spring:message code='serviceManagement.grid.column.service.serverInstance' ></spring:message>",
	                      "<spring:message code='serviceManager.select.server.serverIpPort' ></spring:message>",
	                      "<spring:message code='serverManagement.grid.column.id' ></spring:message>",
	                      "<spring:message code='serviceManagement.grid.column.service.enableStatus' ></spring:message>",
	                      "<spring:message code='serviceManagement.grid.column.service.status' ></spring:message>",
	                      "<spring:message code='serviceManagement.grid.column.service.sync' ></spring:message>",
	                     ],
				colModel:[
					{name : '',index : '',formatter : checkBoxFormatter,sortable : false,width : '20%'},
					{name:'id',index:'id',sortable:true,hidden: true},
	            	{name:'serviceId',index:'id',sortable:true, hidden: true},
	            	{name:'servInstanceId',index:'servInstanceId'},
	            	{name:'serviceName',index:'name',sortable:true,formatter:nameFormatter},
	                {name:'serviceTypeName',index:'svctype.type',sortable:false},
	                {name:'serviceType',index:'svctype',hidden:true},
	            	{name:'serverInstanceName',index:'serverInstance.name',sortable:false,formatter:instancenameFormatter},
	            	{name:'serverIpPort',index:'serverIpPort',align:'center',sortable:false},
	            	{name:'serverInstanceId',index:'serverInstance.id',sortable:true,hidden:true},
	            	{name:'enableStatus',index:'serviceState',sortable:true,align:'center',formatter:servicEnableColumnFormatter},
	            	{name:'serviceStatus',index:'status',sortable:false, align:'center',formatter:serviceStateColumnFormatter,<c:if test="${Boolean.TRUE.toString() eq  MapCache.getConfigValueAsObject(BaseConstants.KUBERNETES_ENV)}">hidden:true</c:if>},
	            	{name:'sync_status',index:'syncStatus',sortable:true, align:'center',formatter:syncColumnFormatter}
	            ],
	            rowNum:<%= MapCache.getConfigValueAsInteger(SystemParametersConstant.TOTAL_ROWS_TO_DISPLAY_IN_GRID, 10) %>,
	            rowList:[10,20,60,100],
	            height: 'auto',
	            mtype:'POST',
				sortname: 'id',
	     		sortorder: "desc",
	            pager: "#serviceInstanceListPagingDiv",
	            contentType: "application/json; charset=utf-8",
	            viewrecords: true,
	            multiselect: false,
	            timeout : 120000,
	            caption: "<spring:message code="serviceManagement.grid.caption"></spring:message>",
	            beforeRequest:function(){
	            	console.log("old grid is :: " + oldGrid);
	            	if(oldGrid != ''){
	            		$('#serviceInstanceList tbody').html(oldGrid);
	            	}
	                $(".ui-dialog-titlebar").hide();
	                
	                $("#load_serviceInstanceList").removeClass("loadingHide").addClass( "loading ui-state-default ui-state-active" );  
	                $(".ui-paging-info").html(''); 
	                
	            }, 
	            beforeSend: function(xhr) {
	                xhr.setRequestHeader("Accept", "application/json");
	                xhr.setRequestHeader("Content-Type", "application/json");
	            },
	     		 loadComplete: function(data) {
	     			 $(".ui-dialog-titlebar").show();
	     			
	     			$("#load_serviceInstanceList").removeClass( "loading ui-state-default ui-state-active" ).addClass( "loadingHide" );  
	     			
	     			$(".ui-paging-info").html(''); 
	     			
	     			if ($('#serviceInstanceList').getGridParam('records') === 0) {
	     				oldGrid = $('#serviceInstanceList tbody').html();
	                    $('#serviceInstanceList tbody').html("<div style='padding:6px;background:#D8D8D8;text-align:center;'><spring:message code="serviceManagement.grid.empty.records"></spring:message></div>");
	                    $("#serviceInstanceListPagingDiv").hide();
	                }else{
	                	$("#serviceInstanceListPagingDiv").show();
	                	
	                	ckIntanceSelected = new Array();
		    			resizeInstanceGrid();
	                }
	     			$('.checkboxbg').bootstrapToggle();
	     			
	     			$('.checkboxbg').change(function(){
	     				serviceActiveInactiveToggleChanged(this);
	    			}); 
	     			
	     			var $jqgrid = $("#serviceInstanceList");      
	     			$(".jqgrow", $jqgrid).each(function (index, row) {
	     		        var $row = $(row);
     		            //Find the checkbox of the row and set it disabled
     		            $row.find("input:checkbox").attr("disabled", "disabled");
	     		    }); 
	     		    
				}, 
				onPaging: function (pgButton) {
					clearResponseMsgDiv();
					clearInstanceGrid();
				},
				loadError : function(xhr,st,err) {
					handleGenericError(xhr,st,err);
				},
				onSelectAll:function(id,status){

					if(status==true){
						ckIntanceSelected = new Array();
						for(i=0;i<id.length;i++){
							ckIntanceSelected.push(id[i]);
			         	}
					} else {
						ckIntanceSelected = new Array();
					} 

				},
				recordtext: "<spring:message code="serviceManagement.grid.pager.total.records.text"></spring:message>",
				loadtext: "",
				pgtext : "<spring:message code="serviceManagement.grid.pager.text"></spring:message>",
			}).navGrid("#serviceInstanceListPagingDiv",{edit:false,add:false,del:false,search:false});
			
			$(".ui-jqgrid-titlebar").hide();
		}
		
		function checkBoxFormatter(cellvalue, options, rowObject) {
			return "";
		}
		
		function addRowId(elementId,Id)
		{
			var deviceElement = document.getElementById(elementId);
			if (deviceElement.checked) {
				if((ckIntanceSelected.indexOf(Id)) == -1){
					ckIntanceSelected.push(Id);
				}		
			}else{
				if(ckIntanceSelected.indexOf(Id) !== -1){
					ckIntanceSelected.splice(ckIntanceSelected.indexOf(Id), 1);
				}
			}
		}
	
		function serviceActiveInactiveToggleChanged(element){
			clearResponseMsgDiv();
    		clearResponseMsg();
    		clearErrorMsg();
    		$("#active-service-warning").hide();
			$("#inactive-service-warning").hide();
			$("#on-service-warning").hide();
			$("#off-service-warning").hide();
			$("#close-btn").hide();
			$("#enable-close-btn").hide();
			$("#cancel-btn").hide();
			$("#update-service-div").hide();
			$("#update-service-progress-div").hide();
			$("#startServicelbl").hide();
			$("#changeStatuslbl").show();
    		$("#syncWarningMsg").hide();
			
    		$("#server-stop-close-btn").hide();
			$("#service-stop-close-btn").hide();
			
			$("#server-start-close-btn").hide();
			$("#service-start-close-btn").hide();

			var toggleId = $(element).prop('id');
			var id_status  = toggleId.split("_");

			if(id_status.indexOf("enable") != -1) {

				var rowId = id_status[1];
				var serviceEnableStatus=id_status[2];
				var serviceStatus = jQuery('#serviceInstanceList').jqGrid ('getCell', rowId, 'serviceStatus');
				var tempStatusId=$(serviceStatus).find("input:checkbox").prop('id');
				var temp_status  = tempStatusId.split("_");
				var status=temp_status[1];
				
				serviceEnablePopup(rowId,status,serviceEnableStatus);
				
			}else{
				
				var id = id_status[0];
				var rowId = id;
				var serviceId = jQuery('#serviceInstanceList').jqGrid ('getCell', rowId, 'serviceId');
				var servInstanceId = jQuery('#serviceInstanceList').jqGrid ('getCell', rowId, 'servInstanceId');
				var serviceName = $(jQuery('#serviceInstanceList').jqGrid ('getCell', rowId, 'serviceName')).closest("a").html();
				var syncStatus=$(jQuery('#serviceInstanceList').jqGrid ('getCell', rowId, 'sync_status')).closest("img").attr("alt");
				
				
				ckIntanceSelected = new Array();
				ckIntanceSelected[0]=id;

				if(id_status[1] == 'INACTIVE'){
					
					$("#divStartService #lblServiceId").text(servInstanceId);
					$("#divStartService #lblServiceName").text(serviceName);
					$("#service-start-close-btn").show();
					if(syncStatus == 'false'){
						$("#syncWarningMsg").show();
					}
					
					//find enable status
					
					var enableStatus = jQuery('#serviceInstanceList').jqGrid ('getCell', rowId, 'enableStatus');
					var tempStatusId=$(enableStatus).find("input:checkbox").prop('id');
					var temp_status  = tempStatusId.split("_");

					var status=temp_status[2];

					if(status == 'INACTIVE'){
						clearAllMessages();
						clearResponseMsgPopUp();
						$("#serviceEnableStatus").val(id_status[1]);
						$("#serviceEnableId").val(serviceId);
						$("#startServicelbl").show();
						$("#changeStatuslbl").hide();
						$("#off-service-warning").show();
						$("#update-service-div").hide();
						$("#cancel-btn").hide();
						$("#close-btn").show();
						$("#enable-close-btn").hide();
						$("#update-service-progress-div").hide();
						$("#updateServiceStatus").click();
					}else{
						$("#startService").click();
					}
					
				}else if(id_status[1] == 'ACTIVE'){
					
					$("#divStopService #lblServiceId").text(servInstanceId);
					$("#divStopService #lblServiceName").text(serviceName);
					
					$("#service-stop-close-btn").show();
					$("#stopService").click();
					
				}
			}
			
			
		}
		
		function serviceStateColumnFormatter(cellvalue, options, rowObject){
			
			var toggleId = rowObject["id"] + "_" + cellvalue;
			var divId = rowObject["serverInstanceName"] +"_"+ rowObject["serviceName"] + "_load_img";
			var currentTab = '<c:out value="${REQUEST_ACTION_TYPE}"></c:out>';
			if(currentTab == 'SERVICE_MANAGEMENT'){
				loadServiceStatusGUI(rowObject["id"],'service_management',rowObject["servInstanceId"],rowObject); 
			}
			return '<div id="service_instance_status_loader_'+rowObject["id"]+'"><div id="'+divId+'"><img src="img/preloaders/circle-red.gif" width="20px" data-toggle="tooltip" data-placement="bottom" title="Loading Status.." height="20px"></div></div>';
		}
		
		function servicEnableColumnFormatter(cellvalue, options, rowObject){
			var toggleIdDiv = rowObject["serverInstanceName"] +"_"+ rowObject["serviceName"] + "_toggle_status_btn";
			return '<div id="'+toggleIdDiv+'"></div>'; 
		}
		
		
		
		function nameFormatter(cellvalue, options, rowObject){
			<sec:authorize access="!hasAuthority('VIEW_SERVICE_INSTANCE')">
				return cellvalue;
			</sec:authorize>
			
			<sec:authorize access="hasAuthority('VIEW_SERVICE_INSTANCE')">
				return '<a class="link" id="'+ rowObject["serviceName"] + "_svc_detail_btn"+ '" onclick="viewService('+"'" + rowObject["id"]+ "','"+rowObject["serviceType"]+"','"+rowObject["serviceName"]+"','"+rowObject["serverInstanceId"]+"'" + ')">' + cellvalue + '</a>' ;
			</sec:authorize>
		}
		
		function instancenameFormatter(cellvalue, options, rowObject){
			
			<sec:authorize access="!hasAuthority('VIEW_SERVER_AND_SERVER_INSTANCES')">
				return cellvalue;
			</sec:authorize>
			<sec:authorize access="hasAuthority('VIEW_SERVER_AND_SERVER_INSTANCES')">
				return '<div id="'+ rowObject["serviceName"] + "_" +rowObject["serverIpPort"] + "_server_detail_btn"+ 
				'"> <a class="link" onclick="viewServerInstance('+"'" + rowObject["serverInstanceId"]+ "'" + ')">' + cellvalue + '</a></div>' ;
			</sec:authorize>
		}
		
		function syncColumnFormatter(cellvalue, options, rowObject){
			return '';
		}
		 
		function searchInstanceCriteria(){
			clearResponseMsgDiv();
			clearInstanceGrid();
			var $grid = $("#serviceInstanceList");
			$grid.jqGrid('setGridParam',{datatype:'json',page: 1}).trigger('reloadGrid');
		}

		function resetSearchInstanceCriteria(){
			$("#search-service-name").val('');
			$("#search-service-serverinstance-name").val('');
			$("#search-service-id").val('');
			$("#serverInstanceName").val('');
			$('input:radio[name=search-service-instance-sync-status]').each(function () { $(this).prop('checked', false); });
			searchInstanceCriteria();
		}
		
		function clearInstanceGrid(){
			var $grid = $("#serviceInstanceList");
			var rowIds = $grid.jqGrid('getDataIDs');
			// iterate through the rows and delete each of them
			for(var i=0,len=rowIds.length;i<len;i++){
				var currRow = rowIds[i];
				$grid.jqGrid('delRowData', currRow);
			}
		}
		
		function resizeInstanceGrid(){
			var $grid = $("#serviceInstanceList"),
		    newWidth = $grid.closest(".ui-jqgrid").parent().width();
	 	    if(newWidth < 1000){
		    	newWidth = 800;
		    }
		    //$grid.jqGrid("setGridWidth", newWidth, true);
		}
		
		
		function clearSelection(){
			var grid = $("#serviceInstanceList");
			var rowIds = grid.getDataIDs();
			// iterate through the rows and deselect each of them
			for(var i=0,len=rowIds.length;i< len;i++){
				jQuery("#serviceInstanceList").find('#'+rowIds[i]+' input[type=checkbox]').prop('checked',false);
			}
			ckIntanceSelected = new Array();
		}
		
		function checkInstanceState(rowId){
			return $(jQuery('#serviceInstanceList').jqGrid ('getCell', rowId, 'serviceStatus')).find(".checkboxbg").prop('id').split('_')[1];	
		}
		
		function syncPublishPopup(){
			clearAllMessages();
			if(ckIntanceSelected.length == 0){
				$("#lessWarn").show();
				$("#moreWarn").hide();
				$("#deleteWarnMsg").click();
				return;
			}else if(ckIntanceSelected.length > 1){
				$("#lessWarn").hide();
				$("#moreWarn").show();
				$("#deleteWarnMsg").click();
				return;
			}else{
				$("#syncPublish").click();	
			}
		}
		
		function synchronizePopup(){
			clearAllMessages();
			
			$("#syncPopupBtn").show();
			$("#cancelSyncBtn").show();
			$("#server-synchCloseBtn").hide();
			$("#service-synchCloseBtn").hide();
			
			$("#btnSynchClose").hide();
			
			var tableString ='<table class="table table-hover" style="width:100%">';
			if(ckIntanceSelected.length == 0){
				$("#lessWarn").show();
				$("#moreWarn").hide();
				$("#deleteWarnMsg").click();
				return;
			}
			
			var rowId='',serviceId='',serviceName='';
			
			if(ckIntanceSelected.length >0){
				tableString += "<tr>";
				tableString += "<th><spring:message code='serviceManagement.grid.column.service.id'></spring:message></th>";
				tableString += "<th><spring:message code='serviceManager.add.service.name' ></spring:message></th>";
				tableString += "<th class='status'><spring:message code='serviceMgmt.synchronize.popup.status'></spring:message></th>";
				tableString += "</tr>";
			}
			
			for(var index=0;index < ckIntanceSelected.length;index++){
			
				rowId = ckIntanceSelected[index];
				serviceId= jQuery('#serviceInstanceList').jqGrid ('getCell',rowId, 'servInstanceId' );
				serviceName = $(jQuery('#serviceInstanceList').jqGrid ('getCell', rowId, 'serviceName')).closest("a").html();
				
				tableString += "<tr>";
				tableString += "<td>"+serviceId+"</td>";
				tableString += "<td>"+serviceName+"</td>";
				tableString += "<td class='status' id='res_"+rowId+"'></td>";
				tableString += "</tr>";
			}
			tableString += "</table>"
			$("#divServiceList").html(tableString);
			
			$('.status').hide();
			$('#divSyncMsg').html('');
			$("#synchronize").click();
			// set instnce id to synchronize
			$("#syncServiceId").val(rowId);
		}
			
		function resetSearchParams(){
			clearAllMessages();
			clearResponseMsgDiv();
    		clearResponseMsg();
    		clearErrorMsg();
    		clearSelection();
    		
			$("#serviceInstanceName").val('');
			$("#serverInstanceName").val('');
			$("#searchServiceType").val('-1');
			$("#serviceInstanceId").val('');
			$('input:radio[name=syncStatus]').each(function () { $(this).prop('checked', false); });
			
			loadGridData();
		}
			
		function exportConfigPopup(){
			clearAllMessages();
			if(ckIntanceSelected.length == 0){
				$("#lessWarn").show();
				$("#moreWarn").hide();
				$("#deleteWarnMsg").click();
				return;
			}else if(ckIntanceSelected.length > 1){
				$("#lessWarn").hide();
				$("#moreWarn").show();
				$("#deleteWarnMsg").click();
				return;
			}else{
				$("#exportServiceInstanceId").val(ckIntanceSelected[0]); // set service instance id which is selected for export to submit with form.
				$("#isExportForDelete").val(false);
				$("#REQUEST_ACTION_TYPE").val('<%=BaseConstants.SERVICE_MANAGEMENT%>');
				$("#exportPath").val("");
				$("#export-service-instance-config-form").submit();
			}
			
		}
		
		function serviceEnablePopup(id,serviceStatus,enableStatus){
			clearAllMessages();
			clearResponseMsgPopUp();
			
			$("#serviceEnableStatus").val(enableStatus);
			$("#serviceEnableId").val(id);
			$("#active-service-warning").hide();
			$("#inactive-service-warning").hide();
			$("#on-service-warning").hide();
			$("#off-service-warning").hide();
			$("#close-btn").hide();
			$("#enable-close-btn").hide();
			$("#cancel-btn").hide();
			$("#update-service-div").hide();
			$("#update-service-progress-div").hide();
			$("#startServicelbl").hide();
			$("#changeStatuslbl").show();
			
			if(enableStatus == 'ACTIVE'){
				
				if(serviceStatus == 'ACTIVE'){
					$("#on-service-warning").show();
					$("#update-service-div").hide();
					$("#close-btn").hide();
					$("#enable-close-btn").show();
					$("#update-service-progress-div").hide();
				}else{
					$("#active-service-warning").show();
					$("#update-service-div").show();
					$("#update-service-progress-div").hide();
				}
				
			}else{
				
				$("#inactive-service-warning").show();
				$("#update-service-div").show();
				$("#update-service-progress-div").hide();
			}
			
			$("#updateServiceStatus").click();
		}

		function serviceActiveInactiveToggle2(element){	}		
		
		function changeServiceStatus(){
			resetWarningDisplay();
			clearAllMessages();
			var tempStatus ;
			$("#update-service-div").hide();
			$("#update-service-progress-div").show();
			
			var serviceStatus=$("#serviceEnableStatus").val();
			if(serviceStatus == 'ACTIVE'){
				tempStatus = 'INACTIVE';
			}else{
				tempStatus = 'ACTIVE';
			}
			
			 $.ajax({
				url: '<%=ControllerConstants.UPDATE_SERVICE_ENABLE_STATUS%>',
				cache: false,
				async: true,
				dataType: 'json',
				type: "POST",
				data:
				 {
					"serviceId"     : $("#serviceEnableId").val(),
					"serviceStatus" : tempStatus
				}, 
				
				success: function(data){
					
					var response = eval(data);
					var responseCode = response.code; 
					var responseMsg = response.msg; 
					var responseObject = response.object;
					$("#update-service-div").show();
					$("#update-service-progress-div").hide();
					if(responseCode == "200"){
						resetWarningDisplay();
						clearAllMessages();
						if(tempStatus == 'ACTIVE'){
							$('#enable_'+$("#serviceEnableId").val() +'_'+serviceStatus).bootstrapToggle('on');
							$('#enable_'+$("#serviceEnableId").val() +'_'+serviceStatus).attr("id",'#enable_'+$("#serviceEnableId").val() +'_'+tempStatus);
						}else{
							$('#enable_'+$("#serviceEnableId").val() +'_'+serviceStatus).bootstrapToggle('off');
							$('#enable_'+$("#serviceEnableId").val() +'_'+serviceStatus).attr("id",'#enable_'+$("#serviceEnableId").val() +'_'+tempStatus);
						}
						
						showSuccessMsgPopUp(responseMsg);
						$("#cancel-btn").show();
						$("#active-service-warning").hide();
						$("#inactive-service-warning").hide();
						$("#on-service-warning").hide();
						$("#off-service-warning").hide();
						$("#close-btn").hide();
						$("#enable-close-btn").hide();
						$("#update-service-div").hide();
						$("#update-service-progress-div").hide();
						$("#startServicelbl").hide();
						
					}else if(responseObject != undefined && responseObject != 'undefined' && responseCode == "400"){
						showErrorMsg(responseMsg);
						$('#enable_'+$("#serviceEnableId").val() +'_'+serviceStatus).bootstrapToggle('toggle');
						$("#cancel-btn").show();
						$("#active-service-warning").hide();
						$("#inactive-service-warning").hide();
						$("#on-service-warning").hide();
						$("#off-service-warning").hide();
						$("#close-btn").hide();
						$("#enable-close-btn").hide();
						$("#update-service-div").hide();
						$("#update-service-progress-div").hide();
						$("#startServicelbl").hide();
					}else{
						resetWarningDisplay();
						clearAllMessages();
						showErrorMsg(responseMsg);
						$('#enable_'+$("#serviceEnableId").val() +'_'+serviceStatus).bootstrapToggle('toggle');
						$("#cancel-btn").show();
						$("#active-service-warning").hide();
						$("#inactive-service-warning").hide();
						$("#on-service-warning").hide();
						$("#off-service-warning").hide();
						$("#close-btn").hide();
						$("#enable-close-btn").hide();
						$("#update-service-div").hide();
						$("#update-service-progress-div").hide();
						$("#startServicelbl").hide();
					}
					$("#active-service-warning").hide();
					$("#inactive-service-warning").hide();
				},
			    error: function (xhr,st,err){
					handleGenericError(xhr,st,err);
				}
			}); 
		}
		
		function setDefaultStatus(){
			var serviceStatus=$("#serviceEnableStatus").val();
			if(serviceStatus == 'ACTIVE'){
				$('#enable_'+$("#serviceEnableId").val() +'_'+serviceStatus).bootstrapToggle('on');
			}else{
				$('#enable_'+$("#serviceEnableId").val() +'_'+serviceStatus).bootstrapToggle('off');
			}
			closeFancyBox();
		}
		
		function setStartDefaultStatus(){
			var serviceStatus=$("#serviceEnableStatus").val();
			if(serviceStatus == 'ACTIVE'){
				$('#'+$("#serviceEnableId").val() +'_'+serviceStatus).bootstrapToggle('on');
			}else{
				$('#'+$("#serviceEnableId").val() +'_'+serviceStatus).bootstrapToggle('off');
			}
			closeFancyBox();
		}
		
		function syncPublishInstance(){
			$("#sync-publish-buttons-div").hide();
			$("#sync-publish-progress-bar-div").show();
			$("#buttons-div").hide();
			$("#progress-bar-div").show();
			clearAllMessages();
			var rowId = ckIntanceSelected[0];
			var serviceId = jQuery('#serviceInstanceList').jqGrid ('getCell', rowId, 'serviceId');
			if(serviceId!=undefined && serviceId!=""){
				$("#syncPublishInstanceId").val(serviceId);
			}
			$.ajax({
					url: '<%= ControllerConstants.SYNC_PUBLISH_SERVICE_INSTANCE %>',
 				    cache: false,
 					async: true,
 					dataType: 'json',
 					type: "POST",
 					data: {
 						id: $("#syncPublishInstanceId").val(),
 						description : $("#descSyncPublish").val(),
 						serverInstancesStatus:'ACTIVE'
 					},
 					success: function(data){
 						$("#sync-publish-buttons-div").show();
 						$("#sync-publish-progress-bar-div").hide();
 						$("#buttons-div").show();
 						$("#progress-bar-div").hide();
 						var response = eval(data);
 				    	response.msg = decodeMessage(response.msg);
 				    	response.msg = replaceAll("+"," ",response.msg);
 				    	$("#descSyncPublish").val("");
 				    	if(response.code == 200 || response.code == "200") {
 				    		resetSearchInstanceCriteria();
 				    		clearResponseMsgDiv();
 				    		clearResponseMsg();
 				    		clearErrorMsg();
 				    		showSuccessMsg(response.msg);
 				    		ckIntanceSelected = new Array();
 				    		closeFancyBox();
 				    		clearSelection();
 				    	}else{
 				    		showErrorMsgPopUp(response.msg);
				    		clearSelection();
 				    	}
 					},
 				    error: function (xhr,st,err){
 				    	$("#sync-publish-buttons-div").show();
 						$("#sync-publish-progress-bar-div").hide();
 						$("#buttons-div").show();
 						$("#progress-bar-div").hide();
 				    	handleGenericError(xhr,st,err);
 					}
 				});
		}

		  
</script>
  
 
