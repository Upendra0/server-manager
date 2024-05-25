<%@page import="com.elitecore.sm.common.constants.BaseConstants"%>
<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="/WEB-INF/ShowErrorTag.tld" prefix="elitecoreError" %>
<%@page import="com.elitecore.sm.common.constants.SystemParametersConstant"%>
<%@page import="com.elitecore.sm.util.MapCache"%>

<div class="tab-pane <c:if test="${(REQUEST_ACTION_TYPE eq 'UPDATE_INSTANCE_SUMMARY')}"><c:out value="active"></c:out></c:if>" id="Summary">
   <div class="tab-content no-padding padding0">            
 	<form:form modelAttribute="server_instance_form_bean" method="POST" action="<%= ControllerConstants.UPDATE_ADVANCE_CONFIG %>" id="serverInstance-updateSummary" enctype="multipart/form-data">
     	
     	<input type="hidden" id="instanceStatus" name="instanceStatus" value="${serverInstanceStatus}" />
     	<form:input type="hidden" path="id"  id="instance-id" ></form:input>
     	<form:input type="hidden" path="fileStatInDBEnable"  id="instance-fileStatInDBEnable" ></form:input>
     	<form:input type="hidden" path="snmpAlertEnable"  id="instance-snmpAlertEnable" ></form:input>
     	<form:input type="hidden" path="webservicesEnable"  id="instance-webservicesEnable" ></form:input>
     	<form:input type="hidden" path="restWebservicesEnable"  id="instance-restWebservicesEnable" ></form:input>
     	<form:input type="hidden" path="logsDetail.level"  id="instance-logsDetail-level" ></form:input>
     	<form:input type="hidden" path="lastUpdatedDate" name="lastUpdatedDate" id="instance-lastUpdate"></form:input>
     	<form:input type="hidden" path="fileCdrSummaryDBEnable"  id="instance-fileCdrSummaryDBEnable" ></form:input>
     	
   
     	<form:input type="hidden" path="fileStorageLocation" name="fileStorageLocation"></form:input>
     	
		<form:input type="hidden" path="logsDetail.rollingType" name="logsDetail.rollingType"></form:input>
		<form:input type="hidden" path="logsDetail.rollingValue" name="logsDetail.rollingValue"></form:input>
		<form:input type="hidden" path="logsDetail.maxRollingUnit" name="logsDetail.maxRollingUnit"></form:input>
		<form:input type="hidden" path="logsDetail.logPathLocation" name="logsDetail.logPathLocation"></form:input>
		<form:input type="hidden" path="thresholdSysAlertEnable" name="thresholdSysAlertEnable"></form:input>
		<form:input type="hidden" path="thresholdMemory" name="thresholdMemory"></form:input>
		<form:input type="hidden" path="thresholdTimeInterval" name="thresholdTimeInterval"></form:input>
		<form:input type="hidden" path="loadAverage" name="loadAverage"></form:input>
		
		<form:input type="hidden" path="server.id" name="server.id" id="instance-server-id"></form:input>
		<form:input type="hidden" path="server.name" name="server.name" id="instance-server-name"></form:input>
		<form:input type="hidden" path="server.description" name="server.description" id="instance-server-description"></form:input>
		<form:input type="hidden" path="server.ipAddress" name="server.ipAddress" id="instance-server-ip"></form:input>
<%-- 		<form:input type="hidden" path="server.issynced" name="server.issynced" id="instance-server-issynced"></form:input> --%>
		<form:input type="hidden" path="server.utilityPort" name="server.utilityPort" id="instance-server-utilityport"></form:input>
		
		<form:input type="hidden" path="port" name="port" id="instance-port"></form:input>
		<form:input type="hidden" path="name" name="name" id="instance-name"></form:input>
		<form:input type="hidden" path="lastUpdatedDate" name="lastUpdatedDate" id="instance-lastUpdate"></form:input>
		<input type="hidden" id="synchSISts" value="${server_instance_form_bean.syncSIStatus}" />
                            		
        <input type="hidden" id="synchChildSts" value="${server_instance_form_bean.syncChildStatus}" />
     	
     	<input type="hidden" name="<%= BaseConstants.REQUEST_ACTION_TYPE %>"  id="<%= BaseConstants.REQUEST_ACTION_TYPE %>" value="<%= BaseConstants.UPDATE_INSTANCE_SUMMARY %>" />
       
		<div class="fullwidth padding0">
	       	<div class="title2"><spring:message code="updtInstacne.summary.service.grid.caption"></spring:message>
	          <span class="title2rightfield">
	          	<sec:authorize access="hasAuthority('CREATE_SERVICE_INSTANCE')">
		          <span class="title2rightfield-icon1-text">
		          	<i class="fa fa-plus-circle" id="add_service_img" onclick="loadCreateServicePage();"  style="cursor: pointer; cursor: hand;"></i>
		          	<a href="#" id="add_service_lnk" onclick="loadCreateServicePage();">
		          		<spring:message code="updtInstacne.summary.service.create.link" ></spring:message>
		          	</a>
		          </span>
		        </sec:authorize>
	          </span>
	      	</div>
	    </div>

         <div class="box-body table-responsive no-padding box" style="margin-bottom:0;" id="tblServiceList">
       		<table class="table table-hover" id="summaryServiceList"></table>
           	<div id="summaryServiceListPagingDiv"></div> 
          	<div class="clearfix"></div>   
          		<div id="divLoading" align="center" style="display: none;"><img src="img/preloaders/Preloader_10.gif" /></div>  
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
			            			<button type="button" id="yes_btn" class="btn btn-grey btn-xs " onclick="changeServiceStatus();"><spring:message code="btn.label.yes"></spring:message></button>
			            		</sec:authorize>
			            		<button type="button" id="no_btn" class="btn btn-grey btn-xs " data-dismiss="modal" onclick="setDefaultStatus();"><spring:message code="btn.label.no"></spring:message></button>
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
		
		
       <sec:authorize access="hasAuthority('PACKET_STATASTIC_AGENT') or hasAuthority('FILE_RENAME_AGENT')">
		<div class="fullwidth mtop15">
       		<div class="title2">
       			<spring:message code="updtInstacne.summary.agent.grid.caption"></spring:message>
      		</div>
    	</div>
    	<div class="box  martop" id="tblAgentList"> 	
	       	<div class="box-body table-responsive no-padding box">
	       		<table class="table table-hover" id="agentList"></table>
	           	<div id="agentListPagingDiv"></div> 
	          	<div class="clearfix"></div>   
	    	</div>
    	</div>
    	</sec:authorize>
        
        <div class="fullwidth tab-content no-padding">
            <div class="greybg">
                <div class="greybg-textbox" style="width: 25%"><spring:message code="updtInstacne.snmp.alert.sts.label" ></spring:message></div>
                <div class="greybg-whitebox" style="width: 25%;padding-top:6px !important;">
                	<sec:authorize access="hasAuthority('UPDATE_SERVER_AND_SERVER_INSTANCES')">
                    	<input data-toggle="toggle" data-size="mini" data-on="On" data-off="Off" type="checkbox" id="snmpSts">
                    </sec:authorize>
                    <sec:authorize access="!hasAuthority('UPDATE_SERVER_AND_SERVER_INSTANCES')">
                    	<input data-toggle="toggle" data-size="mini" data-on="On" data-off="Off" type="checkbox" disabled id="snmpSts">
                    </sec:authorize>
                </div>
                <%-- uncomment below code for show toggle for web service  
                <div class="greybg-textbox" style="width: 16%"><spring:message code="updtInstacne.WEb.servc.label" ></spring:message></div>
                <div class="greybg-whitebox" style="width: 17%;padding-top:6px !important;">
                	<sec:authorize access="hasAuthority('UPDATE_SERVER_AND_SERVER_INSTANCES')">
                   		<input data-toggle="toggle" data-size="mini" data-on="On" data-off="Off" type="checkbox" id="webServiceSts">
                    </sec:authorize>
                    <sec:authorize access="!hasAuthority('UPDATE_SERVER_AND_SERVER_INSTANCES')">
                    	<input data-toggle="toggle" data-size="mini" data-on="On" data-off="Off" type="checkbox" disabled id="webServiceSts">
                    </sec:authorize>
                </div> --%>
                 <%-- <c:if test="${(isIplmsInstance)}"> --%>
	              	<%-- 	<div class="greybg-textbox" style="width: 13%"><spring:message code="updtInstacne.rest.web.service.label" ></spring:message></div>
		                <div class="greybg-whitebox" style="width: 20%;padding-top:6px !important;">
		                	<sec:authorize access="hasAuthority('UPDATE_SERVER_AND_SERVER_INSTANCES')">
		                   		<input data-toggle="toggle" data-size="mini" data-on="On" data-off="Off" type="checkbox" id="restWebServiceSts">
		                    </sec:authorize>
		                    <sec:authorize access="!hasAuthority('UPDATE_SERVER_AND_SERVER_INSTANCES')">
		                    	<input data-toggle="toggle" data-size="mini" data-on="On" data-off="Off" type="checkbox" disabled id="restWebServiceSts">
		                    </sec:authorize>
		                </div>  --%>
		         <%-- </c:if> --%>
		                <div class="greybg-textbox" style="width: 25%"><spring:message code="updtInstacne.log.lvl.label" ></spring:message></div>
		                <div class="greybg-whitebox" style="width: 25%;padding-top:6px !important;">
		                    <label style="margin:4px 0 3px;" id="logLevel"></label>
		                </div>
		        
	            </div>
            	
        </div>
    
	</form:form>
	</div>
	  <div style="display: none;">
			<form id="service_form" method="GET" action="<%= ControllerConstants.INIT_UPDATE_SERVICE %>">
				<input type="hidden" id="serviceId" name="serviceId">
				<input type="hidden" id="serviceType" name="serviceType">
				<input type="hidden" id="serviceName" name="serviceName">
				<input type="hidden" id="instanceId" name="instanceId">
			</form>
		</div>
		
		<!-- create service form code start here-- Onclick add it will goto create service page. -->
		<div style="display: hidden">
			
			<form id="loadCreateServiceForm" method="POST" action="<%=ControllerConstants.GET_SERVICE_LIST_BY_SERVER_TYPE%>">
			
			<input type="hidden" id="profileserverInstanceId" name="profileserverInstanceId">
		</form>
		</div>
		<!-- create service form code end here-->
		
		
		<!-- Service Export hidden form code star here -->
		<div style="display: none;">
			<form action="<%=ControllerConstants.EXPORT_SERVICE_INSTANCE_CONFIG%>" id="export-service-instance-config-form" method="POST">
	        	<input type="hidden" id="exportServiceInstanceId" name="exportServiceInstanceId"  value=""/>
	        	<input type="hidden" id="isExportForDelete" name="isExportForDelete"  value=""/>
	        	<input type="hidden" id="exportPath" name="exportPath" />
	        	<input type="hidden" id="<%=BaseConstants.REQUEST_ACTION_TYPE %>" name="<%=BaseConstants.REQUEST_ACTION_TYPE %>" value="<%=BaseConstants.SERVICE_MANAGEMENT%>"/>
	       </form>
		</div>
		<!-- Service Export hidden form code end here -->
		
		<form action="<%= ControllerConstants.SPECIFIC_SYSTEM_AGENT_CONFIG %>" method="POST" id="agent-config-form">
    		<input type="hidden" id="agent_server_Instance_Id" name="agent_server_Instance_Id"/>
    		<input type="hidden" id="systemAgentTypeId" name="systemAgentTypeId"/>
    		<input type="hidden" id="<%=BaseConstants.REQUEST_ACTION_TYPE %>" name="<%=BaseConstants.REQUEST_ACTION_TYPE %>" value="<%=BaseConstants.UPDATE_SYSTEM_AGENT_CONFIG%>"/>
	 </form>
	 
	 
	 <a href="#divOffWarn" class="fancybox" style="display: none;" id="offwarn">#</a>
	 <a href="#divInactiveWarn" class="fancybox" style="display: none;" id="inactivewarn">#</a>	 
	 
	 	<div id="divOffWarn" style=" width:100%; display: none;" >
		    <div class="modal-content">
		        <div class="modal-header padding10">
		            <h4 class="modal-title"><spring:message code="serverManagement.warn.popup.header"></spring:message></h4>
		        </div>
		        <div class="modal-body padding10 inline-form">
			        <p>
			        	<spring:message code="serverMgmt.instance.off.warning"></spring:message>    
			        </p>
		        </div>
		        <div class="modal-footer padding10">
		            <button type="button" class="btn btn-grey btn-xs " data-dismiss="modal" onclick="closeFancyBox();clearSelection();"><spring:message code="btn.label.close"></spring:message></button>
		        </div>
		    </div>
		    <!-- /.modal-content --> 
		</div>
		
		<div id="divInactiveWarn" style=" width:100%; display: none;" >
		    <div class="modal-content">
		        <div class="modal-header padding10">
		            <h4 class="modal-title"><spring:message code="serverManagement.warn.popup.header"></spring:message></h4>
		        </div>
		        <div class="modal-body padding10 inline-form">
			        <p>
			        	<spring:message code="serverMgmt.instance.inactive.warning"></spring:message>    
			        </p>
		        </div>
		        <div class="modal-footer padding10">
		            <button type="button" class="btn btn-grey btn-xs " data-dismiss="modal" onclick="closeFancyBox();clearSelection();"><spring:message code="btn.label.close"></spring:message></button>
		        </div>
		    </div>
		    <!-- /.modal-content --> 
		</div>
 </div>
 
 <script type="text/javascript">
 	var ckIntanceSelected = new Array();
	var servicesList = {};
				
		$(document).ready(function() {

			reloadInstanceGridData();
    		loadInstanceStatus($('#instance-id').val());
			loadServiceSummaryGrid();
			<sec:authorize access="hasAuthority('PACKET_STATASTIC_AGENT') or hasAuthority('FILE_RENAME_AGENT')">
			$("#agentList").jqGrid({
	        	url: "<%= ControllerConstants.GET_AGENT_LIST_SUMMARY %>",
	        	mtype:"POST",
	        	postData: {
	        		serverInstanceId :$("#instance-id").val(),
	        		isServerInstanceSummary: 'true',
	        	},
	            datatype: "json",
	            colNames:[
						  "<spring:message code='updtInstacne.summary.agent.grid.column.agent.id' ></spring:message>",
	                      "<spring:message code='updtInstacne.summary.agent.grid.column.agent.name' ></spring:message>",
	                      "<spring:message code='service.summary.grid.column.agent.agentTypeId' ></spring:message>",
	                      "<spring:message code='updtInstacne.summary.agent.grid.column.last.exe' ></spring:message>",
	                      "<spring:message code='updtInstacne.summary.agent.grid.column.nxt.exe' ></spring:message>",
	                      "<spring:message code='updtInstacne.summary.agent.grid.column.status' ></spring:message>",
	                     ],
				colModel:[
	            	{name:'id',index:'id',hidden:true},
	                {name:'typeOfAgent',index:'typeOfAgent',sortable:false, formatter:agentNameFormatter},
	                {name:'agentTypeId',index:'agentTypeId.type',hidden:true},
	            	{name:'lastExecutionDate',index:'lastExecutionDate',sortable:false , align:'center',formatter: lastExecutionDateFormatter},
	            	{name:'nextExecutionDate',index:'nextExecutionDate',sortable:false , align:'center',formatter : nextExecutionDateFormatter},
	            	{name:'agentStatus',index:'agentStatus',sortable:false, align:'center',formatter: agentStateFormatter},
	            	
	            ],
	            rowNum:<%= MapCache.getConfigValueAsInteger(SystemParametersConstant.TOTAL_ROWS_TO_DISPLAY_IN_GRID, 10) %>,
	            rowList:[10,20,60,100],
	            height: 'auto',
				sortname: 'id',
	     		sortorder: "desc",
	            pager: "#agentListPagingDiv",
	            viewrecords: true,
	            multiselect: false,
	            caption: "<spring:message code="updtInstacne.agent.list.grid.caption"></spring:message>",
	     		loadComplete: function(data) {
	    	 		
	     			
	    	 		
	    	 		$('#snmpSts').change(function(){
	    	 			updtSNMPAlertPopup();
	    			});
	    	 		
	    	 		$('#webServiceSts').change(function(){
	    	 			updtWebServicePopup();
	    			});
	    	 		
	    	 		$('#restWebServiceSts').change(function(){
	    	 			updtRestWebServicePopup();
	    			});
	    	 		
	    	 		 var $jqgrid = $("#agentList");      
	     			$(".jqgrow", $jqgrid).each(function (index, row) {
	     				 
	     		        var $row = $(row);
	     		      	
     		            //Find the checkbox of the row and set it disabled
     		            var agentType = $(jQuery('#agentList').jqGrid ('getCell', row.id, 'typeOfAgent')).closest("a").html();
     		       		if(agentType == 'Packet Statistics Agent'){
     		       			 $row.find("input:checkbox").attr("disabled", "disabled");
     		       			
     		       		}
     		       		$row.hide();
	     		       	<sec:authorize access="hasAuthority('PACKET_STATASTIC_AGENT')">
		     		       if(agentType == 'Packet Statistics Agent'){
								$row.show();				       			
				       		}
	     		       	</sec:authorize>
	     		       	
	     		       <sec:authorize access="hasAuthority('FILE_RENAME_AGENT')">
		     		      if(agentType == 'File Rename Agent'){
				       			 $row.show();
				       			
				       		}
	     		       </sec:authorize>
     		       	
	     		    }); 
	     			
					$('#agentList .checkboxbg').bootstrapToggle();
	     			
	    	 		$('#agentList .checkboxbg').change(function(){
	    	 			agentActiveInactiveToggleChanged(this);
	    			});
	     			
				},
				onPaging: function (pgButton) {
					clearResponseMsgDiv();
				},
				loadError : function(xhr,st,err) {
					handleGenericError(xhr,st,err);
				},
				beforeSelectRow: function (rowid, e){
					
					// this blank function will not select the entire row. Only checkbox can be selectable.					
				
				},
				recordtext: "<spring:message code="updtInstacne.agent.list.grid.pager.total.records.text"></spring:message>",
		        emptyrecords: "<spring:message code="updtInstacne.agent.list.grid.empty.records"></spring:message>",
				loadtext: "<spring:message code="updtInstacne.agent.list.grid.loading.text"></spring:message>",
				pgtext : "<spring:message code="updtInstacne.agent.list.grid.pager.text"></spring:message>"
			}).navGrid("#agentListPagingDiv",{edit:false,add:false,del:false,search:false});
			
			$(".ui-jqgrid-titlebar").hide();
			resizeAgentGrid();
			</sec:authorize>
		});
		
		function loadServiceSummaryGrid(){		
			$("#summaryServiceList").jqGrid({

				url: "<%= ControllerConstants.GET_SERVICE_LIST_SUMMARY %>",
	        	mtype:"GET",
	        	postData: {
 	        		serverInstanceId :$("#instance-id").val() 
	        	},
	            datatype: "json",
	            colNames:[
	                      '<spring:message code="updtInstacne.summary.service.grid.column.id" ></spring:message>',
	                      '<spring:message code="updtInstacne.summary.service.grid.column.id" ></spring:message>',
	                      '<spring:message code="updtInstacne.summary.service.grid.column.name" ></spring:message>',
	                      '<spring:message code="updtInstacne.summary.service.grid.column.type" ></spring:message>',
	                      '<spring:message code="updtInstacne.summary.service.grid.column.type" ></spring:message>',
	                      '<spring:message code="updtInstacne.summary.service.enableStatus" ></spring:message>',	                      
	                      '<spring:message code="updtInstacne.summary.service.grid.column.status" ></spring:message>',	                      	  	                      
	                      '<spring:message code="updtInstacne.summary.service.grid.column.lst.updt.dt" ></spring:message>',
	                      '<spring:message code="updtInstacne.summary.service.grid.column.sync" ></spring:message>',
	                      '<spring:message code="updtInstance.summary.service.delete.link" ></spring:message>'
	                     ],
	                     
				colModel:[
	            	{name:'id',index:'id',hidden:true,sortable:true},
	            	{name:'servInstanceId',index:'servInstanceId',hidden:false,sortable:true,align:'center'},
	                {name:'name',index:'name',sortable:true,formatter:serviceNameFormatter},
	                {name:'serviceTypeName',index:'serviceTypeName',sortable:false},
	            	{name:'serviceType',index:'serviceType',sortable:true,hidden:true},            	
	            	{name:'enableStatus',index:'serviceState',sortable:false,align:'center', formatter: servicEnableColumnFormatter },	            	
	            	{name:'status',index:'status',sortable:false, align:'center',formatter: serviceStateFormatter,<c:if test="${Boolean.TRUE.toString() eq  MapCache.getConfigValueAsObject(BaseConstants.KUBERNETES_ENV)}">hidden:true</c:if>},	            		            	
	            	{name:'lastUpdatedDate',index:'lastUpdatedDate',sortable:true},
	            	{name:'syncStatus',index:'syncStatus',sortable:true, align:'center',formatter: serviceSyncStatusFormatter},
	            	{name:'delete',index:'delete',sortable:false, align:'center',formatter: deleteImageFormatter}	            	
	            ],
	            rowNum:<%= MapCache.getConfigValueAsInteger(SystemParametersConstant.TOTAL_ROWS_TO_DISPLAY_IN_GRID, 10) %>,
	            rowList:[10,20,60,100],
	            height: 'auto',
				sortname: 'lastUpdatedDate',
	     		sortorder: "desc",
	            pager: "#summaryServiceListPagingDiv",
	            contentType: "application/json; charset=utf-8",	            
	            viewrecords: true,
	            multiselect: false,
	            timeout : 120000,
	            loadtext: "Loading...",
	            caption: "<spring:message code="staffMgmt.grid.caption"></spring:message>",
	            beforeRequest:function(){
	                $(".ui-dialog-titlebar").hide();
	            }, 
	            beforeSend: function(xhr) {
	                xhr.setRequestHeader("Accept", "application/json");
	                xhr.setRequestHeader("Content-Type", "application/json");
	            },	            
	            
	            /* caption: "<spring:message code="staffMgmt.grid.caption"></spring:message>", */
	     		loadComplete: function(data) {
	     			
					$('.checkboxbg').bootstrapToggle();
	     			
	     			/* $('.checkboxbg').change(function(){
 	     				serviceActiveInactiveToggle2(this);  
	    			}); */ 
	     				 
	     				     			
 	    			var $jqgrid = $("#summaryServiceList");      
	     			$(".jqgrow", $jqgrid).each(function (index, row) {
	     		        var $row = $(row);
     		            //Find the checkbox of the row and set it disabled
     		            $row.find("input:checkbox").attr("disabled", "disabled");
	     		    });   
	     			
	     			
	     			$('#summaryServiceList .checkboxbg').bootstrapToggle();
	     			
	    	 		$('#summaryServiceList .checkboxbg').change(function(){
	    	 			serviceActiveInactiveToggleChanged(this);
	    			});
	    	 		
				},
				onPaging: function (pgButton) {
					clearResponseMsgDiv();
				},
				loadError : function(xhr,st,err) {
					handleGenericError(xhr,st,err);
				},
				beforeSelectRow: function (rowid, e){
					var $grid = $("#summaryServiceList");
					
					if($("#jqg_summaryServiceList_" + $grid.jqGrid ('getCell', rowid, 'id')).is(':checked')){
						
						if(ckIntanceSelected.indexOf($grid.jqGrid ('getCell', rowid, 'id')) == -1){
							ckIntanceSelected.push($grid.jqGrid ('getCell', rowid, 'id'));
						}
					}else{
						if(ckIntanceSelected.indexOf($grid.jqGrid ('getCell', rowid, 'id')) != -1){
							ckIntanceSelected.splice(ckIntanceSelected.indexOf($grid.jqGrid ('getCell', rowid, 'id')), 1);
						}
					}
				    return false;
					
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
				recordtext: "<spring:message code="updtInstacne.service.list.grid.pager.total.records.text"></spring:message>",
		        emptyrecords: "<spring:message code="updtInstacne.service.list.grid.empty.records"></spring:message>",
				loadtext: "<spring:message code="updtInstacne.service.list.grid.loading.text"></spring:message>",
				pgtext : "<spring:message code="updtInstacne.service.list.grid.pager.text"></spring:message>"
			}).navGrid("#summaryServiceListPagingDiv",{edit:false,add:false,del:false,search:false});
			
			$(".ui-jqgrid-titlebar").hide();
			$(".ui-pg-input").height("10px");
		}
		
		$(document).ready(function(){
			
			if($("#instance-fileStatInDBEnable").val()=='true'){
				$('#fileStatSts').attr('checked','checked');
				$('#fileStatSts').bootstrapToggle('on');
			}
			if($("#instance-snmpAlertEnable").val()=='true'){
				$("#snmpSts").attr('checked','checked');
				$('#snmpSts').bootstrapToggle('on');
			}
			if($("#instance-webservicesEnable").val()=='true'){
				$('#webServiceSts').attr('checked','checked');
				$('#webServiceSts').bootstrapToggle('on');
			}
			if($("#instance-restWebservicesEnable").val()=='true'){
				$('#restWebServiceSts').attr('checked','checked');
				$('#restWebServiceSts').bootstrapToggle('on');
			}
			$("#logLevel").text($('#instance-logsDetail-level').val());
		});
		
		
		function serviceActiveInactiveToggle2(element){
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
			
			$("#server-start-close-btn").show();
			$("#service-start-close-btn").hide();

			var toggleId = $(element).prop('name');
			var id_status  = toggleId.split("_");
	
			if(id_status.indexOf("enable") != -1) {

				var rowId = id_status[1];
				var serviceEnableStatus=id_status[2];
				var serviceStatus = jQuery('#summaryServiceList').jqGrid('getCell', rowId, 'status');
				var serviceStateStatus = jQuery('#summaryServiceList').jqGrid('getCell', rowId, 'serviceState');
				var tempStatusId = $(serviceStatus).find("input:checkbox").prop('id');
				var temp_status  = tempStatusId.split("_"); 
				var status=temp_status[1];
				
				serviceEnablePopup(rowId,status,serviceEnableStatus);
				
			}else{
				
				var id = id_status[0];
				var rowId = id;
				var serviceId = jQuery('#summaryServiceList').jqGrid ('getCell', rowId, 'id');
				var servInstanceId = jQuery('#summaryServiceList').jqGrid ('getCell', rowId, 'servInstanceId');
				var serviceName = $(jQuery('#summaryServiceList').jqGrid ('getCell', rowId, 'name')).closest("a").html();
				var syncStatus=$(jQuery('#summaryServiceList').jqGrid ('getCell', rowId, 'syncStatus')).closest("img").attr("alt");
				var serviceStateStatus = jQuery('#summaryServiceList').jqGrid('getCell', rowId, 'serviceState');
				var serviceEnableStatus=id_status[2];
				
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
					
					var enableStatus = jQuery('#summaryServiceList').jqGrid ('getCell', rowId, 'enableStatus');
 					var tempStatusId=$(enableStatus).find("input:checkbox").prop('id');
					var temp_status  = tempStatusId.split("_");
 
					var status=temp_status[2];

					if(status == 'INACTIVE'){
						clearAllMessages();
						clearResponseMsgPopUp();
						$("#serviceEnableStatus").val(serviceEnableStatus);
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
					$("#service-start-close-btn").show();
					$("#service-stop-close-btn").show();
					$("#stopService").click();
					
				}
			} 
	
		}
		
		
		
		function serviceStateFormatter(cellvalue, options, rowObject){
			 var toggleId = rowObject["id"] + "_" + cellvalue;
			 var serviceName = rowObject["name"].replace(/ /g, "_");
			 var toggleIdDiv = serviceName + "_service_status";
			 
			 loadServiceStatusGUI(rowObject["id"],'service_management','',rowObject,'serverInstanceSummary'); 
			 return '<div id="service_instance_status_loader_'+rowObject["id"]+'"><div id="'+toggleIdDiv+'"><img src="img/preloaders/circle-red.gif" width="20px" data-toggle="tooltip" data-placement="bottom" title="Loading Status.." height="20px"></div></div>';
		}
				
		function exportServiceInstanceBeforeDelete(exportData ,id){

				$("#exportServiceInstanceId").val(id);
				$("#isExportForDelete").val(true);
				$("#REQUEST_ACTION_TYPE").val('<%=BaseConstants.SERVICE_MANAGEMENT%>');
				//$("#btnExportDeletePopup").prop('disabled', true);
				$("#btnDeletePopup").prop('disabled', false);
				$("#exportPath").val(exportData);
				$("#export-service-instance-config-form").submit();
		}
		
		function serviceSyncStatusFormatter(cellvalue, options, rowObject){
			return '';
		}
		
		function setDefaultStatus(){
			var serviceStatus=$("#serviceEnableStatus").val();
			if(serviceStatus == 'ACTIVE'){
				$('#enable_'+$("#serviceEnableId").val()).bootstrapToggle('on');
			}else{
				$('#enable_'+$("#serviceEnableId").val()).bootstrapToggle('off');
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
		
		function loadCreateServicePage(){
			$("#profileserverInstanceId").val($('#instance-id').val());
   			$('#loadCreateServiceForm').submit();	
   		}
		
		function setServiceSyncParameters(serviceId, serviceName,serviceInstanceId){
			$("#syncServiceId").val(serviceId);
			$("#syncServiceName").val(serviceName);
			$("#syncServiceInstanceId").val(serviceInstanceId);
			syncServiceInstanceById(serviceId);
		}
		
		function checkServiceInstanceStatus(rowId){
			return $(jQuery('#summaryServiceList').jqGrid ('getCell', rowId, 'status')).find(".checkboxbg").prop('id').split('_')[1];	
		}
		
		function serviceNameFormatter(cellvalue, options, rowObject){
			var serverInstanceId = $('#instance-id').val();
			var serviceName = rowObject["name"].replace(/ /g, "_");
			var serviceDetailLnkId = serviceName+"_service_detail_lnk";
			<sec:authorize access="!hasAuthority('VIEW_SERVICE_INSTANCE')">
				return cellvalue;
			</sec:authorize>
			<sec:authorize access="hasAuthority('VIEW_SERVICE_INSTANCE')">
				return '<a class="link" id="'+serviceDetailLnkId+'" onclick="viewService('+"'" + rowObject["id"]+ "','"+rowObject["serviceType"]+"','"+rowObject["name"]+"','"+serverInstanceId+"'" + ')">' + cellvalue + '</a>' ;
			</sec:authorize>
		}
		
		
		function deleteImageFormatter(cellvalue, options, rowObject){
		
			var toggleId = rowObject["id"];
			var servInstanceId = rowObject["servInstanceId"];
			var service_name = rowObject["name"];
			var tag_name = 'server_instance';
			var serviceName = rowObject["name"].replace(/ /g, "_");
			var serviceDelImgId = serviceName+"_service_del_img";
			
			return '<i class="fa fa-trash" id="'+serviceDelImgId+'" onclick="deletePopup('+"'" + tag_name + "','"+toggleId+"','"+servInstanceId+"','"+service_name+"'" + ')" style="cursor: pointer; cursor: hand;"></i>';
		}
		
		
		
		
		function checkInstanceState(rowId){
			return $(jQuery('#summaryServiceList').jqGrid ('getCell', rowId, 'status')).find(".checkboxbg").prop('id').split('_')[1];	
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
		
		
		
		
		
		function servicEnableColumnFormatter(cellvalue, options, rowObject){
			var toggleIdName = "enable_"+rowObject["id"] + "_" + cellvalue;
			var toggleId = "enable_"+rowObject["id"];
			var serviceName = rowObject["name"].replace(/ /g, "_");
			var toggleIdDiv = serviceName + "_service_enable_status";
			return '';
			if(cellvalue == 'ACTIVE'){
				return '<div id="'+toggleIdDiv+'"><input class="checkboxbg" id=' + toggleId + ' name=' + toggleIdName + ' data-on="Active" data-off="InActive" checked data-size="mini" type="checkbox"></div>';
			}else{
				return '<div id="'+toggleIdDiv+'"><input class="checkboxbg" id=' + toggleId + ' name=' + toggleIdName + ' data-on="Active" data-off="InActive" data-size="mini" type="checkbox"></div>';
			}
		}
		
		function agentStateFormatter(cellvalue, options, rowObject){
			var toggleId = rowObject["id"] + "_agentStatus_" + cellvalue;
			var agentType = rowObject["typeOfAgent"].replace(/ /g, "_");
			var toggleIdDiv = agentType + "_status";
			if(cellvalue == 'INACTIVE'){
				return '<div id="'+toggleIdDiv+'"><input class="checkboxbg" id="'+ toggleId +'" onchange="" data-switch-no-init="" type="checkbox"></div>';
			}else{
				return '<div id="'+toggleIdDiv+'"><input class="checkboxbg" id="'+ toggleId +'" onchange="" data-switch-no-init="" type="checkbox" checked="checked"></div>';
			}
		}
		
		
		function agentNameFormatter(cellvalue, options, rowObject){
			var agentType = rowObject["typeOfAgent"].replace(/ /g, "_");
			var detailLinkDiv = agentType + "_detail_lnk";
			return '<a  id="'+detailLinkDiv+'" class = "link" style="cursor: pointer;" onclick="redirectAgentConfig('+"'" + rowObject["agentTypeId"]+ "'"+')">' + cellvalue + '</a>' ;
		}
		

		function redirectAgentConfig(agentTypeId){
			$("#systemAgentTypeId").val(agentTypeId);
			$("#agent_server_Instance_Id").val($("#instance-id").val());
			$("#agent-config-form").submit();
		}
		function resizeAgentGrid(){
			var $grid = $("#agentList"),
		    newWidth = $grid.closest(".ui-jqgrid").parent().width();
			
	 	    if(newWidth < 1000){
		    	newWidth = 800;
		    } 
		    $grid.jqGrid("setGridWidth", newWidth, true);
		}
		
		function viewService(serviceId,serviceType,serviceName,instanceId){
			$("#serviceId").val(serviceId);	
			$("#serviceType").val(serviceType);
			$("#serviceName").val(serviceName);
			$("#instanceId").val(instanceId);
			$("#service_form").submit();
		}
	
		function clearServiceSummaryInstanceGrid(){
			clearResponseMsgDiv();
			var $grid = $("#summaryServiceList");
			$grid.jqGrid('setGridParam',{datatype:'json',page: 1}).trigger('reloadGrid');
			var $grid = $("#summaryServiceList");
			var rowIds = $grid.jqGrid('getDataIDs');
			// iterate through the rows and delete each of them
			for(var i=0,len=rowIds.length;i<len;i++){
				var currRow = rowIds[i];
				$grid.jqGrid('delRowData', currRow);
			}
		}
		
		$(document).ready(function() {
			$('#lblInstanceName').text($('#instance-name').val());
			$('#lblInstanceHost').text($('#instance-server-ip').val());
			$('#lblInstancePort').text($('#instance-port').val());
		});
		
		function updtSNMPAlertPopup(){
			clearResponseMsgDiv();
			var status = $('#snmpSts').prop('checked');
			if(status==true)
				$('#inst-snmp-sts').bootstrapToggle('off');
			else
				$('#inst-snmp-sts').bootstrapToggle('on');
			
			$('#a-snmp').click();
		}
		
		function resetSNMPAlertToggle(){
			resetToggle('snmpSts',updtSNMPAlertPopup);
		}
		
		function updtSNMPAlert(){
			clearResponseMsgDiv();
			var status = $('#snmpSts').prop('checked');
			$("#snmp-buttons-div").hide();
			$("#snmp-progress-bar-div").show();
			
			$.ajax({
				url: '<%= ControllerConstants.UPDATE_SNMP_ALERT_STATUS %>',
				    cache: false,
					async: true,
					dataType: 'json',
					type: "POST",
					data: {
						id: $('#instance-id').val(),
						status : status
					},
					success: function(data){
						$("#snmp-buttons-div").show();
						$("#snmp-progress-bar-div").hide();
						var response = eval(data);
				    	response.msg = decodeMessage(response.msg);
				    	response.msg = replaceAll("+"," ",response.msg);
			    	
				    	if(response.code == 200 || response.code == "200") {
				    		clearResponseMsgDiv();
				    		clearResponseMsg();
				    		clearErrorMsg();
				    		showSuccessMsg(response.msg);
				    		closeFancyBox();
				    	}else{
				    		$("#snmp-buttons-div").show();
							$("#snmp-progress-bar-div").hide();
				    		$('#snmpSts').attr('checked',status);
				    		showErrorMsg(response.msg);
				    		closeFancyBox();
				    	}
					},
				    error: function (xhr,st,err){
				    	handleGenericError(xhr,st,err);
				    	$("#snmp-buttons-div").show();
						$("#snmp-progress-bar-div").hide();
					}
				});
		}
		
		function updtFileStateInDb(){
			clearResponseMsgDiv();
			var status = $('#fileStatSts').prop('checked');
			$.ajax({
				url: '<%= ControllerConstants.UPDATE_FILE_STATE_DB %>',
				    cache: false,
					async: true,
					dataType: 'json',
					type: "POST",
					data: {
						id: $('#instance-id').val(),
						status : status
					},
					success: function(data){
						var response = eval(data);
				    	response.msg = decodeMessage(response.msg);
				    	response.msg = replaceAll("+"," ",response.msg);
			    	
				    	if(response.code == 200 || response.code == "200") {
				    		clearResponseMsgDiv();
				    		clearResponseMsg();
				    		clearErrorMsg();
				    		showSuccessMsg(response.msg);
				    		closeFancyBox();
				    	}else{
				    		$('#fileStatSts').attr('checked',status);
				    		showErrorMsg(response.msg);
				    		closeFancyBox();
				    	}
					},
				    error: function (xhr,st,err){
				    	handleGenericError(xhr,st,err);
					}
				});
		}
		
		function updtWebServicePopup(){
			clearResponseMsgDiv();
			var status = $('#webServiceSts').prop('checked');
			if(status==true)
				$('#inst-webservice-sts').bootstrapToggle('off');
			else
				$('#inst-webservice-sts').bootstrapToggle('on');
			
			$('#a-webservice').click();
		}
		
		function updtRestWebServicePopup(){
			clearResponseMsgDiv();
			var status = $('#restWebServiceSts').prop('checked');
			if(status==true)
				$('#inst-restwebservice-sts').bootstrapToggle('off');
			else
				$('#inst-restwebservice-sts').bootstrapToggle('on');
			
			$('#a-restwebservice').click();
		}
		
		function resetWebserviceToggle(){
			resetToggle('webServiceSts',updtWebServicePopup);
		}
		
		function resetRestWebserviceToggle(){
			resetToggle('restWebServiceSts',updtRestWebServicePopup);
		}
		
		function updtWebServiceState(){
			clearResponseMsgDiv();
			var status = $('#webServiceSts').prop('checked');
			$("#webservice-buttons-div").hide();
			$("#webservice-progress-bar-div").show();
			
			$.ajax({
				url: '<%= ControllerConstants.UPDATE_WEB_SERVICE_STATE %>',
				    cache: false,
					async: true,
					dataType: 'json',
					type: "POST",
					data: {
						id: $('#instance-id').val(),
						status : status
					},
					success: function(data){
						$("#webservice-buttons-div").show();
						$("#webservice-progress-bar-div").hide();
						var response = eval(data);
				    	response.msg = decodeMessage(response.msg);
				    	response.msg = replaceAll("+"," ",response.msg);
			    	
				    	if(response.code == 200 || response.code == "200") {
				    		clearResponseMsgDiv();
				    		clearResponseMsg();
				    		clearErrorMsg();
				    		showSuccessMsg(response.msg);
				    		closeFancyBox();
				    	}else{
				    		$("#webservice-buttons-div").show();
							$("#webservice-progress-bar-div").hide();
				    		$('#webServiceSts').attr('checked',status);
				    		showErrorMsg(response.msg);
				    		closeFancyBox();
				    	}
					},
				    error: function (xhr,st,err){
				    	handleGenericError(xhr,st,err);
					}
				});
		}
		
		function updtRestWebServiceState(){
			clearResponseMsgDiv();
			var status = $('#restWebServiceSts').prop('checked');
			$("#restwebservice-buttons-div").hide();
			$("#restwebservice-progress-bar-div").show();
			
			$.ajax({
				url: '<%= ControllerConstants.UPDATE_REST_WEB_SERVICE_STATE %>',
				    cache: false,
					async: true,
					dataType: 'json',
					type: "POST",
					data: {
						id: $('#instance-id').val(),
						status : status
					},
					success: function(data){
						$("#restwebservice-buttons-div").show();
						$("#restwebservice-progress-bar-div").hide();
						var response = eval(data);
				    	response.msg = decodeMessage(response.msg);
				    	response.msg = replaceAll("+"," ",response.msg);
			    	
				    	if(response.code == 200 || response.code == "200") {
				    		clearResponseMsgDiv();
				    		clearResponseMsg();
				    		clearErrorMsg();
				    		showSuccessMsg(response.msg);
				    		closeFancyBox();
				    	}else{
				    		$("#restwebservice-buttons-div").show();
							$("#restwebservice-progress-bar-div").hide();
				    		$('#restwebServiceSts').attr('checked',status);
				    		showErrorMsg(response.msg);
				    		closeFancyBox();
				    	}
					},
				    error: function (xhr,st,err){
				    	handleGenericError(xhr,st,err);
					}
				});
		}
		
		function resetToggle(sourceId,func){
			var status = $('#'+sourceId).prop('checked');
			$('#'+sourceId).unbind('change');
			
			if(status==true)
				$('#'+sourceId).bootstrapToggle('off');
			else
				$('#'+sourceId).bootstrapToggle('on');
			
			$('#'+sourceId).change(function(){
				func();
			});
		}
		
		var toggleServiceId ,toggleServiceStatus, currentGridEvent ;
		
        function serviceActiveInactiveToggleChanged(element){
            clearResponseMsgDiv();
            clearResponseMsg();
            clearErrorMsg();
            $("#server-stop-close-btn").hide();
            $("#service-stop-close-btn").hide();
            $("#syncWarningMsg").hide();
			$("#off-service-warning").hide();            
           
            $("#server-start-close-btn").show();
            $("#service-start-close-btn").hide();
            currentGridEvent = element
           
            var toggleId = $(element).prop('id');
            var id_status  = toggleId.split("_");
            var id = id_status[0];
            var rowId = id;
            var serviceId = jQuery('#summaryServiceList').jqGrid ('getCell', rowId, 'id');
            toggleServiceId = serviceId;
			var servInstanceId = jQuery('#summaryServiceList').jqGrid ('getCell', rowId, 'servInstanceId');
            var serviceName = $(jQuery('#summaryServiceList').jqGrid ('getCell', rowId, 'name')).closest("a").html();
            toggleServiceStatus = id_status[1];
            var syncStatus=$(jQuery('#summaryServiceList').jqGrid ('getCell', rowId, 'syncStatus')).closest("img").attr("alt");
           
            ckIntanceSelected = new Array();
            ckIntanceSelected[0]=id;

            if(id_status[1] == 'INACTIVE'){
                $("#divStartService #lblServiceId").text(servInstanceId);
                $("#divStartService #lblServiceName").text(serviceName);
                $("#startServiceId").val(serviceId);
                $("#server-start-close-btn").show();
                if(syncStatus == 'false'){
                    $("#syncWarningMsg").show();
                }
                $("#startService").click();
               
            }else if(id_status[1] == 'ACTIVE'){
               
                $("#divStopService #lblServiceId").text(servInstanceId);
                $("#divStopService #lblServiceName").text(serviceName);
                $("#stopServiceId").val(serviceId);
                $("#server-stop-close-btn").show();
                $("#stopService").click();
            }
        }
		
		function resetCheckBox(actionType){
			$('#'+toggleServiceId +'_'+toggleServiceStatus).unbind('change');
			if(toggleServiceStatus == 'ACTIVE'){
				$('#'+toggleServiceId +'_'+toggleServiceStatus).bind('change');
				$('#'+toggleServiceId +'_'+toggleServiceStatus).bootstrapToggle('on');
			}else{
				$('#'+toggleServiceId +'_'+toggleServiceStatus).bind('change');
				$('#'+toggleServiceId +'_'+toggleServiceStatus).bootstrapToggle('off');
			}
			
			$('#'+toggleServiceId +'_'+toggleServiceStatus + '.checkboxbg').change(function(){
				serviceActiveInactiveToggleChanged(currentGridEvent);
			});
		}
		
		function agentActiveInactiveToggleChanged(element){
			
			var toggleId = $(element).prop('id');
			var id_status  = toggleId.split("_");
			var id = id_status[0];			
			var rowId = id;
 			var agentId= jQuery('#agentList').jqGrid ('getCell',rowId,'id'); 
			
			if(id_status[2]=='INACTIVE'){
				$("#startAgentId").val(agentId);
				$("#startAgentStatus").val(id_status[2]);
				$("#start-agent").click();
				
			}else if(id_status[2]=='ACTIVE'){
				$("#stopAgentId").val(agentId);
				$("#stopAgentStatus").val(id_status[2]);
				$("#stop-agent").click();
			}
			
		}
		
		function reloadCachePopup(){
			clearAllMessages();
			$('#reload-cache-buttons-div #btn-no').attr('onclick','closeFancyBox();');
			
			if($('#synchSISts').val()=='false' || $('#synchChildSts').val()=='false')
				$('#reload-cache-msgMismatch').show();
			else
				$('#reload-cache-msgMismatch').hide();
			
			$("#reloadcache").click();	
		}
		
		function softRestartPopup(){

			clearAllMessages();

			$('#soft-restart-buttons-div #btn-no').attr('onclick','closeFancyBox();');
			
			if($('#server_start_btn').val() == 'false'){
				
				if($('#synchSISts').val()=='false' || $('#synchChildSts').val()=='false')
					$('#soft-restart-msgMismatch').show();
				else{
					$('#soft-restart-msgMismatch').hide();
				}
				
				$("#softrestart").click();	
				
			} else{
				
				$("#offwarn").click();
			}
		}
		
		function syncPublishPopup(){

			clearAllMessages();

			$('#sync-publish-buttons-div #btn-no').attr('onclick','closeFancyBox();');
				
			$("#syncPublish").click();	
				
		}
		
		function reloadConfigPopup(){
			clearAllMessages();
			
			$('#reload-config-buttons-div #btn-no').attr('onclick','closeFancyBox();');
			
			if($('#server_start_btn').val()=='false'){
				
				if($('#synchSISts').val()=='false' || $('#synchChildSts').val()=='false')
					$('#reload-config-msgMismatch').show();
				else
					$('#reload-config-msgMismatch').hide();
				
				$("#reloadconfig").click();		
				
			} else{
				
				$("#offwarn").click();
			}

		}
		
		function shutdownPopup(){
			clearAllMessages();
			clearErrorMsgpopUp();
    		clearResponseMsgPopUp();
			$('#shutdown').click();
		}
		
		function startPopup(){
			
			clearAllMessages();
			clearResponseMsgDiv();
    		clearErrorMsgpopUp();
    		clearResponseMsgPopUp();
    		
			$('#start-buttons-div #btn-no').attr('onclick','closeFancyBox();');
			
			if($('#synchSISts').val()=='false' || $('#synchChildSts').val()=='false')
				$('#start-msgMismatch').show();
			else
				$('#start-msgMismatch').hide();
			
			$('#start').click();
		}
		
		function searchInstanceCriteria(){
			clearResponseMsgDiv();
			 
			var $grid = $("#summaryServiceList");
			$grid.jqGrid('setGridParam',{datatype:'json',page: 1}).trigger('reloadGrid');
 		}
		
		function exportConfigPopup(){
			clearAllMessages();
			
			$("#exportInstancesId").val($('#instance-id').val());
			$("#isExportForDelete").val(false);
			$("#REQUEST_ACTION_TYPE").val('<%=BaseConstants.UPDATE_INSTANCE_SUMMARY%>');
			$("#export-config-form").submit();
		}
		
		
		function restartInstance(){
			clearResponseMsgDiv();
			$("#shutdown-buttons-div").hide();
			$("#shutdown-progress-bar-div").show();
			$.ajax({
					url: '<%= ControllerConstants.RESTART_SERVER_INSTANCE %>',
					    cache: false,
						async: true,
						dataType: 'json',
						type: "POST",
						timeout : 15000,
						data: {
							id: $('#instance-id').val()
						},
						success: function(data){
							$("#shutdown-buttons-div").show();
							$("#shutdown-progress-bar-div").hide();
							var response = eval(data);
					    	response.msg = decodeMessage(response.msg);
					    	response.msg = replaceAll("+"," ",response.msg);
				    	
					    	if(response.code == 200 || response.code == "200") {
					    		clearResponseMsgDiv();
					    		clearResponseMsg();
					    		clearErrorMsg();
					    		showSuccessMsg(response.msg);
					    		closeFancyBox();
					    		reloadInstanceGridData();
					    		
					    	}else{
					    		showErrorMsgPopUp(response.msg);
					    	}
						},
					    error: function (xhr,st,err){
							$("#shutdown-buttons-div").show();
							$("#shutdown-progress-bar-div").hide();
							if(st=="timeout"){
								closeFancyBox();
								showSuccessMsg("A restart call has been initiated to the server it should restart in a while,if not try reloading the page to view the changes or restart again.");
								reloadInstanceGridData();
					    		loadInstanceStatus($('#instance-id').val());
					    		$.ajax({
									url: '<%= ControllerConstants.RESTART_SERVER_INSTANCE %>',
									    cache: false,
										async: true,
										dataType: 'json',
										type: "POST",
										data: {
											id: $('#instance-id').val()
										},
										success: function(data){
											$("#shutdown-buttons-div").show();
											$("#shutdown-progress-bar-div").hide();
											var response = eval(data);
									    	response.msg = decodeMessage(response.msg);
									    	response.msg = replaceAll("+"," ",response.msg);
								    	
									    	if(response.code == 200 || response.code == "200") {
									    		closeFancyBox();
									    		
									    	}else{
									    		showErrorMsgPopUp(response.msg);
									    	}
										},
									    error: function (xhr,st,err){
											$("#shutdown-buttons-div").show();
											$("#shutdown-progress-bar-div").hide();
									    	handleGenericError(xhr,st,err);
										}
									});
							}
					    	handleGenericError(xhr,st,err);
						}
					});
		}
		
		function synchInstanceById(){
			
			clearAllMessages();
			
			var id = $('#instance-id').val();
			
			if(id != null && id != ''){
				$("#btnSynchPopup").show();
				$("#btnSynchCancel").show();
				$("#btnSynchClose").hide();
				
				var serverIP= $('#instance-server-ip').val();
				var serverPort = $('#instance-port').val();
				var serverInstance = $('#instance-name').val();
				
				$("#divInstanceList").html('');
				var tableString ='<table class="table table-hover" style="width:100%">';
				tableString += "<tr>";
				tableString += "<th>Instance Name</th>";
				tableString += "<th>Server IP</th>";
				tableString += "<th>Server Port</th>";
				tableString += "<th class='status'>Sync Status</th>";
				tableString += "</tr>";
				tableString += "<tr>";
				tableString += "<td>"+serverInstance+"</td>";
				tableString += "<td>"+serverIP+"</td>";
				tableString += "<td>"+serverPort+"</td>";
				tableString += "<td class='status' id='res_"+id+"'></td>";
				tableString += "</tr>";
				tableString += "</table>"
				$("#divInstanceList").html(tableString);
				$('.status').hide();
				
				$('#divSyncSIMsg').html('');
				$("#synchronize").click();

				// set instnce id to synchronize
				$("#syncInstanceId").val(id);
			}
		}
		
		function synchronizesInstance(){
			$("#synch-buttons-div").hide();
			$("#synch-progress-bar-div").show();
			clearAllMessages();
			
			$.ajax({
					url: '<%= ControllerConstants.SYNC_SERVER_INSTANCE %>',
					    cache: false,
						async: true,
						dataType: 'json',
						type: "POST",
						data: {
							serverInstanceId: $("#syncInstanceId").val(),
							serverInstancesStatus:'ACTIVE'
						},
						success: function(data){
							$("#synch-buttons-div").show();
							$("#synch-progress-bar-div").hide();
							var response = eval(data);
					    	response.msg = decodeMessage(response.msg);
					    	response.msg = replaceAll("+"," ",response.msg);
				    	
					    	
					    	if(response.code == 200 || response.code == "200") {
					    		
					    		clearResponseMsgDiv();
					    		clearResponseMsg();
					    		clearErrorMsg();
					    		ckIntanceSelected = new Array();
					    		var responseCode = data.code;
					    		$('#divSyncSIMsg').html('<span class="title" style="color:black;font-weight:bolder">'+response.msg+'</span>');
			    			var response = data.object;
			    			$.each(response, function(key,val){
			    				var resObj =val;			    				
			    				if (resObj.code == "700") {
			    					$('#res_'+key).text(resObj.msg+" Click the link to Configure Path ");
			    					$('#res_'+key).append("<form method='post' action='initSystemAgentConfig' id='configurePakStatisticsPath_"+key+"'><input type='hidden' name='agent_serverInstanceId' value='"+key+"'></input><a href='#' onclick='parentNode.submit()' >Configure</a></form>");
			    				} else if(resObj.code == "701") {
			    					$('#res_'+key).text(resObj.msg+" Click the link to Configure DataSource ");
			    					$('#res_'+key).append("<form method='post' action='initUpdateServerInstance' id='configureDataSource_"+key+"'><input type='hidden' name='serverInstanceId' value='"+key+"'></input><input type='hidden' name='REQUEST_ACTION_TYPE' value='UPDATE_INSTANCE_ADVANCE_CONFIG'></input><a href='#' onclick='parentNode.submit()' >Configure</a></form>");
			    				} else if(resObj.code == "702") {
			    					$('#res_'+key).text(resObj.msg+" Click the link to Configure License Circle ");
			    					$('#res_'+key).append("<form method='get' action='initCircleConfigurationManager'><a href='#' onclick='parentNode.submit()' >Configure</a></form>");
			    				}else {
			    					$('#res_'+key).text(resObj.msg);
			    				}
			    			});
			    			$(".status").show();
			    			$("#btnSynchPopup").hide();
			    			$("#btnSynchCancel").hide();
			    			$("#btnSynchClose").show();
			    			$(".synchronizePopUpMsgClass").empty();
			    			
					    		
					    	}else{
					    		$('#divSyncSIMsg').html('<span class="title" style="color: #FF0000;">'+response.msg+'</span>');
					    	}
						},
					    error: function (xhr,st,err){
					    	$("#synch-buttons-div").show();
	 					$("#synch-progress-bar-div").hide();
					    	handleGenericError(xhr,st,err);
						}
					});
		}
		
		function reloadCache(){
			$("#reload-cache-buttons-div").hide();
			$("#reload-cache-progress-bar-div").show();
			var reloadType = $('input[name=reloadType]:checked').val();
			
			 var databaseQueryString = "";
		     var selMulti = $("#DatabaseQueryList option:selected").each(function(){
		    	 databaseQueryString += (databaseQueryString == "") ? "" : ",";
		    	 databaseQueryString += $(this).text();
		     });
		     
		    if (reloadType == "dynamic" && databaseQueryString == "" ) {
		    	showErrorMsgPopUp("<spring:message code='reloadCache.databaseQuery.invalid' ></spring:message>");
		    	$("#reload-cache-buttons-div").show();
				$("#reload-cache-progress-bar-div").hide();
		    	return false;
		    	
		    } else {
		    	$.ajax({
					url: '<%= ControllerConstants.SERVER_INSTANCE_RELOAD_CACHE %>',
					    cache: false,
						async: true,
						dataType: 'json',
						type: "POST",
						data: {
							id: $('#instance-id').val(),
							reloadType : reloadType,
							databaseQuery : databaseQueryString
						},
						success: function(data){
							$("#reload-cache-buttons-div").show();
							$("#reload-cache-progress-bar-div").hide();
							var response = eval(data);
					    	response.msg = decodeMessage(response.msg);
					    	response.msg = replaceAll("+"," ",response.msg);
				    	
					    	if(response.code == 200 || response.code == "200") {
					    		resetReloadCacheOptions();
					    		clearResponseMsgDiv();
					    		clearResponseMsg();
					    		clearErrorMsg();
					    		showSuccessMsg(response.msg);
					    		closeFancyBox();
					    	}else{
					    		showErrorMsgPopUp(response.msg);
					    	}
						},
					    error: function (xhr,st,err){
							$("#reload-cache-buttons-div").show();
							$("#reload-cache-progress-bar-div").hide();
					    	handleGenericError(xhr,st,err);
						}
					});
		    }
		}
		
		function reloadConfig(){
			$("#reload-config-buttons-div").hide();
			$("#reload-config-progress-bar-div").show();
			$.ajax({
				url: '<%= ControllerConstants.SERVER_INSTANCE_RELOAD_CONFIG %>',
				    cache: false,
					async: true,
					dataType: 'json',
					type: "POST",
					data: {
						id: $('#instance-id').val()
					},
					success: function(data){
						$("#reload-config-buttons-div").show();
						$("#reload-config-progress-bar-div").hide();
						var response = eval(data);
				    	response.msg = decodeMessage(response.msg);
				    	response.msg = replaceAll("+"," ",response.msg);
			    	
				    	if(response.code == 200 || response.code == "200") {
				    		clearResponseMsgDiv();
				    		clearResponseMsg();
				    		clearErrorMsg();
				    		closeFancyBox();
				    		showSuccessMsg(response.msg);
				    	}else{
				    		showErrorMsg(response.msg);
				    		closeFancyBox();
				    	}
					},
				    error: function (xhr,st,err){
				    	$("#reload-config-buttons-div").show();
						$("#reload-config-progress-bar-div").hide();
				    	handleGenericError(xhr,st,err);
					}
				});
		}
		
		function softRestartInstance(){
			$("#soft-restart-buttons-div").hide();
			$("#soft-restart-progress-bar-div").show();
			$("#buttons-div").hide();
			$("#progress-bar-div").show();
			$.ajax({
					url: '<%= ControllerConstants.SOFT_RESTART_INSTANCE %>',
					    cache: false,
						async: true,
						dataType: 'json',
						type: "POST",
						data: {
							id: $('#instance-id').val()
						},
						success: function(data){
							$("#soft-restart-buttons-div").show();
							$("#soft-restart-progress-bar-div").hide();
							$("#buttons-div").show();
							$("#progress-bar-div").hide();
							var response = eval(data);
					    	response.msg = decodeMessage(response.msg);
					    	response.msg = replaceAll("+"," ",response.msg);
				    	
					    	if(response.code == 200 || response.code == "200") {
					    		clearResponseMsgDiv();
					    		clearResponseMsg();
					    		clearErrorMsg();
					    		showSuccessMsg(response.msg);
					    		closeFancyBox();
					    	}else{
					    		showErrorMsg(response.msg);
					    		closeFancyBox();
					    	}
						},
					    error: function (xhr,st,err){
					    	$("#soft-restart-buttons-div").show();
							$("#soft-restart-progress-bar-div").hide();
							$("#buttons-div").show();
							$("#progress-bar-div").hide();
					    	handleGenericError(xhr,st,err);
						}
					});
		}
		
		function syncPublishInstance(){
			$("#sync-publish-buttons-div").hide();
			$("#sync-publish-progress-bar-div").show();
			$("#buttons-div").hide();
			$("#progress-bar-div").show();
			clearAllMessages();
			$.ajax({
					url: '<%= ControllerConstants.SYNC_PUBLISH_INSTANCE %>',
 				    cache: false,
 					async: true,
 					dataType: 'json',
 					type: "POST",
 					data: {
 						id: $('#instance-id').val(),
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
 				    		clearResponseMsgDiv();
 				    		clearResponseMsg();
 				    		clearErrorMsg();
 				    		showSuccessMsg(response.msg);
 				    		closeFancyBox();
 				    	}else{
 				    		showErrorMsg(response.msg);
				    		closeFancyBox();
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
		
		function importConfig(){
			clearAllMessages();
			
			if($('#configFile').val()==''){
				$('#divImportMsg').html('Please select import file');
				return;
			}
			
			$("#import-config-buttons").hide();
			$("#import-config-progress-bar-div").show();
			$("#import-config-form").submit();
		}
		
		function stopInstance(){
			clearResponseMsgDiv();
			$("#shutdown-buttons-div").hide();
			$("#shutdown-progress-bar-div").show();
			$.ajax({
				url: '<%= ControllerConstants.STOP_SERVER_INSTANCE %>',
				    cache: false,
					async: true,
					dataType: 'json',
					type: "POST",
					timeout : 15000,
					data: {
						id: $('#instance-id').val()
					},
					success: function(data){
						$("#shutdown-buttons-div").show();
						$("#shutdown-progress-bar-div").hide();
						var response = eval(data);
				    	response.msg = decodeMessage(response.msg);
				    	response.msg = replaceAll("+"," ",response.msg);
			    	
				    	if(response.code == 200 || response.code == "200") {
				    		clearResponseMsgDiv();
				    		clearResponseMsg();
				    		clearErrorMsg();
				    		reloadInstanceGridData();
				    		loadInstanceStatus($('#instance-id').val());
				    		closeFancyBox();
				    		showSuccessMsg(response.msg);				    		
				    		$("#server_stop_btn").hide();
				    		
				    	}else{
				    		showErrorMsg(response.msg);
				    		closeFancyBox();
				    	}
					},
				    error: function (xhr,st,err){
				    	$("#shutdown-buttons-div").show();
						$("#shutdown-progress-bar-div").hide();
						if(st == "timeout"){
							reloadInstanceGridData();
				    		loadInstanceStatus($('#instance-id').val());
				    		closeFancyBox();
				    		showSuccessMsg("A shutdown call has been initiated to the server it should update in a while,if not try reloading the page to view the changes.");
				    		$.ajax({
								url: '<%= ControllerConstants.STOP_SERVER_INSTANCE %>',
								    cache: false,
									async: true,
									dataType: 'json',
									type: "POST",
									data: {
										id: $('#instance-id').val()
									},
									success: function(data){
										$("#shutdown-buttons-div").show();
										$("#shutdown-progress-bar-div").hide();
										var response = eval(data);
								    	response.msg = decodeMessage(response.msg);
								    	response.msg = replaceAll("+"," ",response.msg);
							    	
								    	if(response.code == 200 || response.code == "200") {
								    		reloadInstanceGridData();
								    		loadInstanceStatus($('#instance-id').val());
								    		closeFancyBox();		    		
								    		$("#server_stop_btn").hide();
								    		
								    	}else{
								    		showErrorMsg(response.msg);
								    		closeFancyBox();
								    	}
									},
								    error: function (xhr,st,err){
								    	$("#shutdown-buttons-div").show();
										$("#shutdown-progress-bar-div").hide();
								    	handleGenericError(xhr,st,err);
									}
								});
				    		$("#server_stop_btn").hide();
				    		
						}
				    	handleGenericError(xhr,st,err);
					}
				});
		}
		
		function startInstance(){
			$("#server-start-buttons-div").hide();
			$("#server-start-progress-bar-div").show();
			$.ajax({
				url: '<%= ControllerConstants.START_SERVER_INSTANCE %>',
				    cache: false,
					async: true,
					dataType: 'json',
					type: "POST",
					data: {
						id: $('#instance-id').val()
					},
					success: function(data){
						$("#server-start-buttons-div").show();
						$("#server-start-progress-bar-div").hide();
						var response = eval(data);
				    	response.msg = decodeMessage(response.msg);
				    	response.msg = replaceAll("+"," ",response.msg);
			    	
				    	if(response.code == 200 || response.code == "200") {
				    		clearResponseMsgDiv();
				    		clearResponseMsg();
				    		clearErrorMsg();
				    		closeFancyBox();
				    		showSuccessMsg(response.msg);
				    		reloadInstanceGridData();
				    		loadInstanceStatus($('#instance-id').val());
				    		
				    	}else{
				    		showErrorMsgPopUp(response.msg);				    		
				    		
				    	}
					},
				    error: function (xhr,st,err){
				    	$("#server-start-buttons-div").show();
						$("#server-start-progress-bar-div").hide();
				    	handleGenericError(xhr,st,err);
					}
				});
		}
		
		function lastExecutionDateFormatter(cellvalue, options, rowObject){
			
			var toggleId = rowObject["id"] + "_lastExeTime";
			loadAgentInfomation(rowObject["id"],rowObject["typeOfAgent"]);
			return '<div id="agent_last_exeTime_'+rowObject["id"]+'"><img src="img/preloaders/circle-red.gif" width="20px" data-toggle="tooltip" data-placement="bottom" title="Loading Status.." height="20px"></div>';
			
		}
		
		function nextExecutionDateFormatter(cellvalue, options, rowObject){
			
			var toggleId = rowObject["id"] + "_nextExeTime";
			return '<div id="agent_next_exeTime_'+rowObject["id"]+'"><img src="img/preloaders/circle-red.gif" width="20px" data-toggle="tooltip" data-placement="bottom" title="Loading Status.." height="20px"></div>';
			
		}
		
		function loadAgentInfomation(agentId,agenttype){
			
			$.ajax({
				url: '<%=ControllerConstants.LOAD_AGENT_INFORMATION%>',
				cache: false,
				async: true,
				dataType: 'json',
				type: "POST",
				data: {
					"serverInstanceId": $('#instance-id').val(),
					"agentType"		  :	agenttype,
				},
				success: function(data){
					
					var response = eval(data);
					 
					 var responseCode = response.code; 
					 var responseMsg = response.msg; 
					 var responseObject = response.object;
					 if(responseCode == "200"){
						
						 $('#agent_last_exeTime_'+agentId).html('<p>'+responseObject["lastExecutionDate"]+"</p>");
						 $('#agent_next_exeTime_'+agentId).html('<p>'+responseObject["nextExecutionDate"]+"</p>");
						
					 }
					
					
				},
			    error: function (xhr,st,err){
					handleGenericError(xhr,st,err);
				}
			});
		}
		
		function changeAgentStatus(action){
			resetWarningDisplay();
			clearAllMessages();
			var tempStatus ;
			var agentStatus;
			var agentId;
			if(action == 'Start'){
				$("#agent-start-buttons-div").hide();
				$("#agent-start-progress-bar-div").show();
				agentStatus=$("#startAgentStatus").val();
				agentId=$("#startAgentId").val();
				
			}else{
				$("#agent-stop-buttons-div").hide();
				$("#agent-stop-progress-bar-div").show();
				agentStatus=$("#stopAgentStatus").val();
				agentId=$("#stopAgentId").val();
			}
			if(agentStatus == 'ACTIVE'){
				tempStatus = 'INACTIVE';
			}else{
				tempStatus = 'ACTIVE';
			}
			

			 $.ajax({
				url: '<%=ControllerConstants.UPDATE_AGENT_STATUS%>',
				cache: false,
				async: true,
				dataType: 'json',
				type: "POST",
				data:
				 {
					"agentId"     : agentId,
					"agentStatus" : tempStatus
				}, 
				
				success: function(data){
					
					var response = eval(data);
					var responseCode = response.code; 
					var responseMsg = response.msg; 
					var responseObject = response.object;
					
					if(action == 'Start'){
						$("#agent-start-buttons-div").show();
						$("#agent-start-progress-bar-div").hide();
						
					}else{
						$("#agent-stop-buttons-div").show();
						$("#agent-stop-progress-bar-div").hide();
					}
					
					if(responseCode == "200"){
						resetWarningDisplay();
						clearAllMessages();
						
						if(tempStatus == 'ACTIVE'){
							$('#'+agentId+'_agentStatus_'+agentStatus).bootstrapToggle('on');
							$('#'+agentId+'_agentStatus_'+agentStatus).attr("id",'#'+agentId+'_agentStatus_'+tempStatus);
							
						}else{
							$('#'+agentId+'_agentStatus_'+agentStatus).bootstrapToggle('off');
							$('#'+agentId+'_agentStatus_'+agentStatus).attr("id",'#'+agentId+'_agentStatus_'+tempStatus);
						}
						reloadAgentGridData();
						showSuccessMsg(responseMsg);
						closeFancyBox();
						
					}else if(responseObject != undefined && responseObject != 'undefined' && responseCode == "400"){
						showErrorMsg(responseMsg);
						$('#'+agentId+'_agentStatus_'+agentStatus).bootstrapToggle('toggle');
						closeFancyBox();
						reloadAgentGridData();
					}else{
						resetWarningDisplay();
						clearAllMessages();
						showErrorMsg(responseMsg);
						$('#'+agentId+'_agentStatus_'+agentStatus).bootstrapToggle('toggle');
						closeFancyBox();
						reloadAgentGridData();
					}
				},
			    error: function (xhr,st,err){
					handleGenericError(xhr,st,err);
				}
			}); 
		}
		
		function setDefaultAgentStatus(action){
			var agentStatus;
			var agentId;
			
			if(action == 'Start'){
				
				agentStatus=$("#startAgentStatus").val();
				agentId=$("#startAgentId").val();
				
			}else{

				agentStatus=$("#stopAgentStatus").val();
				agentId=$("#stopAgentId").val();
			}
			
			if(agentStatus == 'ACTIVE'){
				$('#'+agentId+'_agentStatus_'+agentStatus).bootstrapToggle('on');
			}else{
				$('#'+agentId+'_agentStatus_'+agentStatus).bootstrapToggle('off');
			}
			closeFancyBox();
		}

		function reloadAgentGridData(){
		    var $grid = $("#agentList");
		    $grid.jqGrid('setGridParam',{datatype:'json',page: 1}).trigger('reloadGrid');
		}
</script>
