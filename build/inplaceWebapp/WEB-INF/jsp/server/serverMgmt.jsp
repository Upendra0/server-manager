<%@page import="com.elitecore.sm.common.constants.LicenseConstants"%>
<%@page import="com.elitecore.sm.util.MapCache"%>
<%@page import="com.elitecore.sm.common.constants.SystemParametersConstant"%>
<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<%@page import="com.elitecore.sm.util.EliteUtils"%>
<%@page import="com.elitecore.sm.common.constants.BaseConstants"%>
<%@taglib 	uri="http://www.springframework.org/tags" 			prefix="spring"%>
<%@taglib 	uri="http://www.springframework.org/security/tags" 	prefix="sec"  %>
<%@taglib uri="/WEB-INF/ShowErrorTag.tld" prefix="elitecoreError" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>


<head>
<script src="${pageContext.request.contextPath}/js/bootstrap.min.js" type="text/javascript"></script>
</head>

<style>
.ui-state-highlight { background: white !important;border-color: black !white;}

</style>

<div class="tab-content padding0 clearfix" id="server-manager-block">
		<div class="title2">
			<spring:message code="serverManagement.page.heading" ></spring:message>
		</div>
        <div class="fullwidth borbot">
        	<div class="col-md-6 inline-form" style="padding-left: 0px !important;">

			<div class="form-group">
				
				<spring:message code="serverManagement.search.server.type" var="label" ></spring:message>
				<spring:message code="serverManagement.search.server.type.tooltip"
					var="tooltip" ></spring:message>
				<div class="table-cell-label">${label}</div>
				<div class="input-group ">
					<select name="search-server-serverType" class="form-control table-cell input-sm"
						tabindex="1" id="search-server-serverType" data-toggle="tooltip"
						data-placement="bottom" title="${tooltip }"
						onchange="getServerListByServerType();">
						<option value="-1" selected="selected"><spring:message
								code="serverManagement.search.server.type.allServerType.option" ></spring:message></option>
						<c:forEach var="serverType" items="${SERVER_TYPE_LIST}">
							<option value="${serverType.id }">${serverType.name}</option>
						</c:forEach>
						
					</select> <span class="input-group-addon add-on last"> <i
						class="glyphicon glyphicon-alert" data-toggle="tooltip"
						data-placement="left" title=""></i></span>
				</div>
			</div>


			<div class="form-group">
	         		<spring:message code="serverManager.server" var="label" ></spring:message>
	         		<spring:message code="serverManagement.search.server.name.tooltip" var="tooltip" ></spring:message>
	         		<div class="table-cell-label">${label }</div>
	             	<div class="input-group ">
	             		<input type="text" id="search-server-name" class="form-control table-cell input-sm" data-toggle="tooltip" data-placement="bottom" title="${tooltip }"  >
	             		<span class="input-group-addon add-on last" ><i class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="bottom" title=""></i></span>
	             	</div>
	             </div>
	             
	             <div class="form-group">
	             	<spring:message code="serverManagement.search.server.host.ip" var="label" ></spring:message>
	             	<spring:message code="serverManagement.search.server.host.ip.tooltip" var="tooltip" ></spring:message>
	         		<div class="table-cell-label">${label }</div>
	             	<div class="input-group ">
	             		<input type="text" id="search-server-host" class="form-control table-cell input-sm" data-toggle="tooltip" data-placement="bottom" title="${tooltip }"  >
	             		<span class="input-group-addon add-on last" > <i class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="left" title=""></i></span></div>
	             </div>
	             
	             
     	  	</div>
         	<div class="col-md-6 inline-form">

				<div class="form-group">
	         		<spring:message code="serverManager.server.instance"  var="label"></spring:message>
	         		<spring:message code="serverManagement.search.server.instance.name.tooltip"  var="tooltip"></spring:message>
	         		<div class="table-cell-label">${label}</div>
	             	<div class="input-group ">
	             		<input type="text" id="search-server-instance" class="form-control table-cell input-sm" data-toggle="tooltip" data-placement="bottom" title="${tooltip }"  >
	             		<span class="input-group-addon add-on last" > <i class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="left" title=""></i></span>
	             	</div>
	             </div>
	             
	             <div class="form-group">
	             	<spring:message code="addServerInstance.instance.port" var="label" ></spring:message>
	             	<spring:message code="serverManagement.search.server.host.port.tooltip" var="tooltip" ></spring:message>
	         		<div class="table-cell-label">${label }</div>
	             	<div class="input-group ">
	             		<input type="number" id="search-server-instance-port" class="form-control table-cell input-sm" data-toggle="tooltip" data-placement="bottom" title="${tooltip }" onkeydown="isNumericOnKeyDown(event)" >
	             		<span class="input-group-addon add-on last" > <i class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="left" title=""></i></span>
	             	</div>
	             </div>
	             
	             <div class="form-group">
	             	<spring:message code="serverManagement.grid.column.server.sync" var="tooltip" ></spring:message>
	         		<div class="table-cell-label">${tooltip }</div>
	             	<div class="input-group ">
	             		<input type="radio" id="sync_status_radio" name="search-server-instance-sync-status" style="padding-right: 15px;" value="yes"/>&nbsp;&nbsp;	<spring:message code="synchronize.search.status.sync" ></spring:message>
	             		&nbsp;&nbsp;&nbsp;<input type="radio" id="unsync_status_radio" name="search-server-instance-sync-status" style="padding-right: 15px;" value="no"/>&nbsp;&nbsp;<spring:message code="synchronize.search.status.notsync" ></spring:message>
	             	</div>
	             </div>
	             
	             
         	</div>

	             	<div class="table-cell-label">&nbsp;</div>
	             	<div class="input-group ">
	             		&nbsp;
	             		<span class="input-group-addon add-on last" > <i class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="left" title=""></i></span>
	             	</div>

     		<div class="pbottom15">
     			<sec:authorize access="hasAuthority('VIEW_SERVER_AND_SERVER_INSTANCES')">
   					<button id="searchServerBtn" class="btn btn-grey btn-xs" onclick="searchInstanceCriteria();"><spring:message code="btn.label.search" ></spring:message></button>
    				<button id="resetServerBtn" class="btn btn-grey btn-xs" onclick="resetSearchInstanceCriteria();"><spring:message code="btn.label.reset" ></spring:message></button>
    			</sec:authorize>
   			</div>
   		</div>
   
        <div class="tab-content no-padding clearfix">
         	<div class="fullwidth">
   				<div class="title2">
   					<spring:message code="serverManagement.grid.heading" ></spring:message>
	   				 <span class="title2rightfield">
				          
				          	<sec:authorize access="hasAuthority('DELETE_SERVER_AND_SERVER_INSTANCES')">
				          		<span class="title2rightfield-icon1-text">
				          		<a href="#" onclick="deleteServerInstancePopup('server_mgmt','','','','');"> <i class="fa fa-trash"></i></a>
				          		<a href="#" id="deleteServerInstanceTxt" onclick="deleteServerInstancePopup('server_mgmt','','','','');"><spring:message code="btn.label.delete" ></spring:message></a>
				          		</span>
				          	</sec:authorize>
				          	
				          	<a href="#divSynchronize" class="fancybox" style="display: none;" id="synchronize">#</a>
							<a href="#divReloadConfig" class="fancybox" style="display: none;" id="reloadconfig">#</a>
				          	<a href="#divSoftRestart" class="fancybox" style="display: none;" id="softrestart">#</a>
				          	<a href="#divSyncPublish" class="fancybox" style="display: none;" id="syncPublish">#</a>	
				          	<a href="#divStartStopInstance" class="fancybox" style="display: none;" id="startstop">#</a>
				          	<a href="#divImportConfig" class="fancybox" style="display: none;" id="importconfig">#</a>
				          	<a href="#divReloadCache" class="fancybox" style="display: none;" id="reloadcache">#</a>
				          	<a href="#divCopyConfig" class="fancybox" style="display: none;" id="copyconfig">#</a>
				          	<a href="#divMessage" class="fancybox" style="display: none;" id="message">#</a>
				          	<a href="#divOffWarn" class="fancybox" style="display: none;" id="offwarn">#</a>
				          	<a href="#divInactiveWarn" class="fancybox" style="display: none;" id="inactivewarn">#</a>
				          	<a href="#divStart" class="fancybox" style="display: none;" id="start">#</a>
				          	<a href="#divStop" class="fancybox" style="display: none;" id="stop">#</a>
				          
			          </span>
		          </div>
   			</div>
           	<!-- Morris chart - Sales -->
            <div class="box-body table-responsive no-padding box">
             	<table class="table table-hover" id="instanceList"></table>
               	<div id="instanceListPagingDiv"></div> 
	           	<div class="clearfix"></div> 
	           	<div id="divLoading" align="center" style="display: none;"><img src="img/preloaders/Preloader_10.gif" /></div>  
            </div>
        </div>
        
        <div style="display: none;">
			<form id="serverinstance_form" method="POST" action="">
				<input type="hidden" id="instanceId" name="instanceId">
			</form>
		</div>
	
		<jsp:include page="divReloadConfig.jsp"></jsp:include>
	
		<jsp:include page="divSoftRestart.jsp"></jsp:include>
		<jsp:include page="divSyncPublish.jsp"></jsp:include>
		<jsp:include page="divReloadCache.jsp"></jsp:include>
		
		<div id="divSynchronize" style=" width:100%; display: none;" >
		    <div class="modal-content">
		        <div class="modal-header padding10">
		            <h4 class="modal-title"><spring:message code="serverMgmt.synchronize.popup.header"></spring:message></h4>
		        </div>
		        <div class="modal-body padding10 inline-form">
	        		<div id="divSyncMsg">
					</div>
					<jsp:include page="../common/responseMsg.jsp" ></jsp:include>
			        <p>
			        	<input type='hidden' id="syncInstanceId" />
			        	<table style='width:100%'>
			        		<tr>
			        			<td width="15%">
			        				<img alt="Server Instance" src="img/server.png" class="img-responsive" />
			        			</td>
			        			<td width="85%">
									<div id="divInstanceList">
									</div>
			        			</td>
			        		</tr>
			        	</table>
			        </p>
					<p id="synchronizePopUpMsg" class="synchronizePopUpMsgClass">           	
			           	<span class="note"><spring:message code="label.important.note"></spring:message></span><br/><br/>
			           	<spring:message code="serverMgmt.synchronize.popup.note.content"></spring:message>
			        </p>
		        </div>
		        <div id="synch-buttons-div" class="modal-footer padding10">
		        	<sec:authorize access="hasAuthority('SERVER_INSTANCE_SYNCHRONIZATION')">
		            	<button type="button" class="btn btn-grey btn-xs" id="btnSynchPopup" onclick="synchronizesInstance();"><spring:message code="btn.label.synchronize"></spring:message></button>
		            </sec:authorize>
		            <button type="button" class="btn btn-grey btn-xs " id="btnSynchCancel" data-dismiss="modal" onclick="closeFancyBox();clearSelection();searchInstanceCriteria();"><spring:message code="btn.label.cancel"></spring:message></button>
		            <button type="button" class="btn btn-grey btn-xs " id="btnSynchClose" data-dismiss="modal" onclick="closeFancyBox();clearSelection();searchInstanceCriteria();" style="display: none;"><spring:message code="btn.label.close"></spring:message></button>
		        </div>
		        <div id="synch-progress-bar-div" class="modal-footer padding10 text-left" style="display: none;">
					<jsp:include page="../common/processing-bar.jsp"></jsp:include>
				</div>	
		    </div>
		    <!-- /.modal-content --> 
		</div>
		<div id="divStartStopInstance" style=" width:100%; display: none;" >
		    <div class="modal-content">
		        <div class="modal-header padding10">
		            <h4 class="modal-title"></h4>
		        </div>
		        <div class="modal-body padding10 inline-form">
		        <jsp:include page="../common/responseMsgPopUp.jsp" ></jsp:include>
		        	<input type="hidden" id="startStopInstanceId" /> 
			        <p id ="startNote">
			        	<table>
			        		<tr>
			        			<td>
			        				<img alt="Server Instance" src="img/server.png" class="img-responsive" />
			        			</td>
			        			<td style="padding-left: 20px;">
			        				<strong><spring:message code="serverManagement.popup.host.ip.lbl"></spring:message></strong> : &nbsp;&nbsp;<label id="lblServerIp"></label>&nbsp; : &nbsp; <label id="lblPort"></label> <br/>		
			        				<spring:message code="serverManagement.popup.instance.name.lbl"></spring:message> : &nbsp;&nbsp;<label id="lblInstance"></label><br/>
			        			</td>
			        		</tr>
			        	</table>
			        </p>
					<p id="stopNote" style="display: none;">	           	
			           	<span class="note"><spring:message code="label.important.note"></spring:message></span><br/><br/>
			           	<sec:authorize access="hasAuthority('SERVER_INSTANCE_STOP')">
	            			<strong><spring:message code="btn.label.stop"></spring:message></strong> : <spring:message code="serverMgmt.start.stop.instance.popup.stop.btn.content"></spring:message><br/>
	            		</sec:authorize>
			           	<br/><strong><spring:message code="btn.label.restart"></spring:message></strong> : <spring:message code="serverMgmt.start.stop.instance.popup.restart.btn.content"></spring:message>
			        </p>
		        </div>
		        <div id="buttons-div" class="modal-footer padding10">
		        	<sec:authorize access="hasAuthority('SERVER_INSTANCE_STOP')">
	            		<button type="button" class="btn btn-grey btn-xs " onclick="stopPopup();" id="btnStop"><spring:message code="btn.label.stop"></spring:message></button>
	            	</sec:authorize>
	            	<sec:authorize access="hasAuthority('SERVER_INSTANCE_START')">
	            		<button type="button" class="btn btn-grey btn-xs " id="btnStart" onclick="startPopup();"><spring:message code="btn.label.start"></spring:message></button>
	            		<button type="button" class="btn btn-grey btn-xs " data-dismiss="modal" onclick="restartInstance();" id="btnRestart"><spring:message code="btn.label.restart"></spring:message></button>
	            	</sec:authorize>
           			<button type="button" class="btn btn-grey btn-xs " id="btnCancel" data-dismiss="modal" onclick="closeFancyBox();clearSelection();searchInstanceCriteria();"><spring:message code="btn.label.cancel"></spring:message></button>
		        </div>
		        <div id="progress-bar-div" class="modal-footer padding10 text-left" style="display: none;">
					<jsp:include page="../common/processing-bar.jsp"></jsp:include>
				</div>	
		        
		    </div>
		    <!-- /.modal-content --> 
		</div>
	
		<div id="divImportConfig" style=" width:100%; display: none;" >
			<jsp:include page="ImportServerInstancePopUp.jsp"></jsp:include>
		</div>
		
		<div id="divMessage" style=" width:100%; display: none;" >
		    <div class="modal-content">
		        <div class="modal-header padding10">
		            <h4 class="modal-title"><spring:message code="serverManagement.warn.popup.header"></spring:message></h4>
		        </div>
		        <div class="modal-body padding10 inline-form">
			        <p id="moreWarn">
			        	<spring:message code="serverMgmt.more.instance.checked.warning"></spring:message>    
			        </p>
			        <p id="lessWarn">
			        	<spring:message code="serverMgmt.no.instance.checked.warning"></spring:message>    
			        </p>
		        </div>
		        <div class="modal-footer padding10">
		            <button id="no_selection_delete_close_btn" type="button" class="btn btn-grey btn-xs " data-dismiss="modal" onclick="closeFancyBox();"><spring:message code="btn.label.close"></spring:message></button>
		        </div>
		    </div>
		    <!-- /.modal-content --> 
		</div>
		
		<div id="divCopyConfig" style=" width:100%; display: none;" >
			<div class="modal-header padding10">
		            <h4 class="modal-title"><spring:message code="serverMgmt.copy.config.popup.header"></spring:message></h4>
		    </div>
		   <div  id="copyMessage" class="modal-body padding10 inline-form">
		    </div>
	       	<div class="col-md-12 inline-form no-padding" style="overflow:scroll;height:150px;width:100%;overflow:auto" id="copy_table_grid">
							<table class="table table-hover" id="tblCopyFrom">	
	            </table>
	            <table class="table table-hover" id="tblCopyTo">
	        	 	
	            </table>
		        <p>
		        	<i class="icon-lightbulb icon-large"></i><span class="note"><spring:message code="label.important.note"></spring:message></span><br/>
		        </p>
				<p>	
					<spring:message code="serverMgmt.copy.config.popup.note.content"></spring:message>           	
		        </p>
		    </div>
		    <div id="copy-config-buttons-div" class="modal-footer padding10">
		            <button type="button" class="btn btn-grey btn-xs " onclick="copyConfig();" id="btncopyConfigPopUp"><spring:message code="btn.label.copy.config"></spring:message></button>
		            <button type="button" class="btn btn-grey btn-xs " data-dismiss="modal" id="copyConfigCancel" onclick="closeFancyBox();clearSelection();searchInstanceCriteria();"><spring:message code="btn.label.cancel"></spring:message></button>
		            <button type="button" class="btn btn-grey btn-xs " id="btnCopyClose" data-dismiss="modal" onclick="closeFancyBox();clearSelection();searchInstanceCriteria();" style="display: none;"><spring:message code="btn.label.close"></spring:message></button>
		 	</div>
		 	<div id="copy-config-progress-bar-div" class="modal-footer padding10 text-left" style="display: none;">
					<jsp:include page="../common/processing-bar.jsp"></jsp:include>
			</div>	
		    <!-- /.modal-content --> 
		</div>
		
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
		
		<div id="divStop" style=" width:100%; display: none;" >
		    <div class="modal-content">
		        <div class="modal-header padding10">
		            <h4 class="modal-title"><spring:message code="serverMgmt.stop.popup.header"></spring:message></h4>
		        </div>
		        <div class="modal-body padding10 inline-form">
			        <p>
			        	<i class="icon-lightbulb icon-large"></i><span class="note"><spring:message code="label.important.note"></spring:message></span><br/>
			        </p>
					<p>	
						<spring:message code="serverMgmt.stop.popup.note.content"></spring:message>           	
			        </p>
			        <p id="stop-msgMismatch" style="display: none;">	  
						<strong><spring:message code="server.manager.and.server.detail.mismatch"></spring:message></strong>         	
			        </p>
			        <p>
			        	<strong><spring:message code="serverManagement.popup.wish.continue.lbl"></spring:message></strong>
			        </p>
		        </div>
		        <div id="stop-buttons-div" class="modal-footer padding10">
		            <button id="btn_stopyes" type="button" class="btn btn-grey btn-xs " onclick="stopInstance();"><spring:message code="btn.label.yes"></spring:message></button>
		            <button id="btn_stopno" type="button" class="btn btn-grey btn-xs " data-dismiss="modal" onclick="searchInstanceCriteria(); closeFancyBox();"><spring:message code="btn.label.no"></spring:message></button>
		        </div>
		        <div id="stop-progress-bar-div" class="modal-footer padding10 text-left" style="display: none;">
					<jsp:include page="../common/processing-bar.jsp"></jsp:include>
				</div>
		    </div>
		    <!-- /.modal-content --> 
		</div>
		
		
			<jsp:include page="divStart.jsp"></jsp:include>
		

		<div style="display: none;">
			<form id="instance_form" method="POST" action="<%= ControllerConstants.INIT_UPDATE_SERVER_INSTANCE %>">
				<input type="hidden" id="serverInstanceId" name="serverInstanceId">
			</form>
			
			<form id="apply_license_form" method="POST" action="<%= ControllerConstants.LICENSE_MANAGER %>">
				<input type="hidden" id="componentType" name="componentType"  value="<%=LicenseConstants.LICENSE_ENGINE%>">
				<input type="hidden" id="license_intance_id" name="license_server_instance_id"  value="0">
				<input type="hidden" id="license_action" name="licenseAction"  value="">
			</form>
		</div>	
</div>  

<script type="text/javascript">
		var ckIntanceSelected = new Array();
		var gridDataArr = [];
		var instanceList = {};

		var oldGrid = '';
		
		$(document).ready(function() {

			<!-- Search Server Instance -->

				$("#instanceList").jqGrid({
		        	url: "getServerInstaneList",
		        	postData: {
		        		serverType: function () {
		        	        return $("#search-server-serverType").val();
		   	    		},
		        		instanceName: function () {
		        	        return $("#search-server-instance").val();
		   	    		},
		   	    		host: function () {
		        	        return $("#search-server-host").val();
		   	    		},
		   	    		serverName: function () {
		        	        return $("#search-server-name").val();
		   	    		},
		   	    		port: function () {
		        	        return $("#search-server-instance-port").val();
		   	    		},
		   	    		sync_status: function () {
		        	        return $("input:radio[name='search-server-instance-sync-status']:checked").val();
		   	    		},
		        	},
		            datatype: "json",
		            colNames:["#",
		                      "<spring:message code='serverManagement.grid.column.id' ></spring:message>",
		                      "<spring:message code='serverManagement.grid.column.server.name' ></spring:message>",
		                      "<spring:message code='serverManagement.grid.column.server.type' ></spring:message>",
		                      "<spring:message code='serverManagement.grid.column.server.instance' ></spring:message>",
		                      "<spring:message code='serverManagement.grid.column.server.hostIp' ></spring:message>",
		                      "<spring:message code='serverManagement.grid.column.server.port' ></spring:message>",
		                      "<spring:message code='serverManagement.grid.column.server.startTime' ></spring:message>",
		                      "<spring:message code='serverManagement.grid.column.server.license' ></spring:message>",
		                      "<spring:message code='serverManagement.grid.column.server.status' ></spring:message>",
		                      "<spring:message code='serverManagement.grid.column.server.sync' ></spring:message>"
		                      
		                      
		                     ],
					colModel:[
						{name : '',index : '',formatter : checkBoxFormatter,sortable : false,width : '20%'}, 
		            	{name:'id',index:'id',sortable:true,hidden: true},
		            	{name:'servername',index:'server.name'},
		            	{name:'serverType',sortable:false,index:'server.type'},
		                {name:'instanceName',index:'name',sortable:true, formatter:nameFormatter},
		            	{name:'host',index:'server.ipAddress',sortable:true},
		            	{name:'port',index:'port',align:'center',sortable:true},
		            	{name:'serverStartTime',index:'serverStartTime',sortable:false,formatter:displayServerStartTime,<c:if test="${Boolean.TRUE.toString() eq  MapCache.getConfigValueAsObject(BaseConstants.KUBERNETES_ENV)}">hidden:true</c:if>},
		            	{name:'license',index:'license',sortable:false,hidden:true},
		            	{name:'status',index:'state',sortable:false,align:'center',formatter:instanceStateColumnFormatter,<c:if test="${Boolean.TRUE.toString() eq  MapCache.getConfigValueAsObject(BaseConstants.KUBERNETES_ENV)}">hidden:true</c:if>},
		            	{name:'syncStatus',index:'isSynced',sortable:false,align:'center',formatter:syncColumnFormatter}
		            ],
		            rowNum:<%= MapCache.getConfigValueAsInteger(SystemParametersConstant.TOTAL_ROWS_TO_DISPLAY_IN_GRID, 10) %>,
		            rowList:[10,20,60,100],
		            height: 'auto',
					sortname: 'id',
		     		sortorder: "desc",
		            pager: "#instanceListPagingDiv",
		            viewrecords: true,
		           	multiselect: false,
		            timeout : 120000,
		            loadtext: "Loading...",
		            caption: "<spring:message code='serverManagement.grid.caption'></spring:message>",
		            beforeRequest:function(){
		            	if(oldGrid != ''){
		            		$('#instanceList tbody').html(oldGrid);
		            	}
		            	$('#divLoading').dialog({
		                    autoOpen: false,
		                    width: 90,
		                    modal:true,
		                    overlay: { opacity: 0.3, background: "white" },
		                    resizable: false,
		                    height: 125,
		                });
		                $('#divLoading').dialog('open');
		                $(".ui-dialog-titlebar").hide();
		            },
		     		loadComplete: function(data) {
		     			
		     			gridDataArr = data.rows;
		     			
		     			$.each(gridDataArr, function (index, instance) {
		     				instanceList[instance.id]=instance;
		     			});
		     			
		     			$("#divLoading").dialog('close');
		     			$(".ui-dialog-titlebar").show();
		     			if ($('#instanceList').getGridParam('records') === 0) {
		     				oldGrid = $('#instanceList tbody').html();
		                    $('#instanceList tbody').html("<div style='padding:6px;background:#D8D8D8;text-align:center;'><spring:message code='serverManagement.grid.empty.records'></spring:message></div>");
		                    $("#instanceListPagingDiv").hide();
		                }else{
		                	$("#instanceListPagingDiv").show();
		                	ckIntanceSelected = new Array();
			    			resizeInstanceGrid();
		                }

		     			$('.checkboxbg').bootstrapToggle();
		    			
		     			$('.checkboxbg').change(function(){
		    				instanceActiveInactiveToggleChanged(this);
		    			});
		     			
		     			// disable all row of jqgrid at start when status will be loaded disabled prop will removed
		     		    var $jqgrid = $("#instanceList");      
		     			$(".jqgrow", $jqgrid).each(function (index, row) {
		     		        var $row = $(row);
	     		            //Find the checkbox of the row and set it disabled
	     		            //$row.find("input:checkbox").attr("disabled", "disabled");
		     		    });
		     			
		     			// enable checkbox and synch click event for inactive server instance
 		     			 $.each(gridDataArr, function (index, instance) {
		     				if(instance.status='INACTIVE'){
		     					var Checkboxes = $("tr#"+instance.id+".jqgrow > td > input.cbox:disabled", "");
							//	Checkboxes.removeAttr("disabled");
								
								// enable synch flag click event if has access right than
								<sec:authorize access="hasAuthority('SERVER_INSTANCE_SYNCHRONIZATION')">
							//	$('#synch_'+instance.id).attr("onclick","synchInstanceById('"+instance.id+"')");
								</sec:authorize>
		     				}
		     				
		     			});  
					},
					onPaging: function (pgButton) {
						clearResponseMsgDiv();
						clearInstanceGrid();
					},
					loadError : function(xhr,st,err) {
						handleGenericError(xhr,st,err);
					},
					beforeSelectRow: function (rowid, e){
					},
					recordtext: "<spring:message code='serverManagement.grid.pager.total.records.text'></spring:message>",
			        emptyrecords: "<spring:message code='serverManagement.grid.empty.records'></spring:message>",
					loadtext: "<spring:message code='serverManagement.grid.loading.text'></spring:message>",
					pgtext : "<spring:message code='serverManagement.grid.pager.text'></spring:message>"
				}).navGrid("#staffListPagingDiv",{edit:false,add:false,del:false,search:false});
				
				$(".ui-jqgrid-titlebar").hide();
				resizeInstanceGrid();
				
		});
		
		function checkBoxFormatter(cellvalue, options, rowObject) {		
			return "";
		}
		
		function addRowId(elementId,serverId)
		{
			var deviceElement = document.getElementById(elementId);
			if (deviceElement.checked) {
				if((ckIntanceSelected.indexOf(serverId)) == -1){
					ckIntanceSelected.push(serverId);
				}		
			}else{
				if(ckIntanceSelected.indexOf(serverId) !== -1){
					ckIntanceSelected.splice(ckIntanceSelected.indexOf(serverId), 1);
				}
			}
		}
		
		function instanceActiveInactiveToggleChanged(element){
			var toggleId = $(element).prop('id');
			var id_status  = toggleId.split("_");
			var id = id_status[0];
			
			var rowId = id;
			var serverIP= jQuery('#instanceList').jqGrid ('getCell',rowId, 'host' );
			var serverPort = jQuery('#instanceList').jqGrid ('getCell', rowId, 'port');
			var serverInstance = $(jQuery('#instanceList').jqGrid ('getCell', rowId, 'instanceName')).closest("a").html();

			if(id_status[1]=='INACTIVE'){
				$("#btnStop").hide();
				$("#btnRestart").hide();
				$("#btnStart").show();
				$("#divStartStopInstance .modal-title").html("<spring:message code='serverMgmt.start.stop.instance.popup.start.header'></spring:message>");
				$('#stopNote').hide();
			}else if(id_status[1]=='ACTIVE'){
				$("#btnStop").show();
				$("#btnRestart").show();
				$("#btnStart").hide();
				$("#divStartStopInstance .modal-title").html("<spring:message code='serverMgmt.start.stop.instance.popup.stop.header'></spring:message>");
				$('#stopNote').show();
			}
			
			ckIntanceSelected = new Array();
			ckIntanceSelected[0]=id;
			$("#divStartStopInstance #lblInstance").text(serverInstance);
			$("#divStartStopInstance #lblServerIp").text(serverIP);
			$("#divStartStopInstance #lblPort").text(serverPort);
			$("#divStartStopInstance #synchronize").click();
			
			$("#startstop").click();
		}
		
		function instanceStateColumnFormatter(cellvalue, options, rowObject){
			var status = rowObject["status"];
			if(status=='INACTIVE'){
				$('#license_'+rowObject["id"]).html('-');
				$('#serverStartTime_'+rowObject["id"]).html('-');
				return '<div id="loader_'+rowObject["id"]+'"><img src="img/chk_sts.ico" width="20px" data-toggle="tooltip" id="'+rowObject["id"]+'_CHECK" data-placement="bottom" title="'+jsLocaleMsg.chkStatus+'" height="20px" class=""chkStatus style="z-index:100px;cursor:pointer;" onclick="chkStatusPopup(\''+rowObject["id"]+'\',\'SERVER_MANAGEMENT\');"></div>'
			} else {
				var toggleId = rowObject["id"] + "_" + cellvalue;
				loadInstanceStatus(rowObject);
				return '<div id="loader_'+rowObject["id"]+'"><img src="img/preloaders/circle-red.gif" width="20px" data-toggle="tooltip" data-placement="bottom" title="'+jsLocaleMsg.loadStatus+'" height="20px" style="z-index:100px;"></div>';			
			}
		}
		
		 function displayLicenseDetails(cellvalue, options, rowObject){
			 return '<div id="license_'+rowObject["id"]+'" align="center"><img src="img/preloaders/circle-red.gif" width="20px" data-toggle="tooltip"   data-placement="bottom" title="'+jsLocaleMsg.loadStatus+'" height="20px" style="z-index:100px;"></div>';
		 }
		 
		 function displayServerStartTime(cellvalue, options, rowObject){
			 return '<div id="serverStartTime_'+rowObject["id"]+'" align="center"><img src="img/preloaders/circle-red.gif" width="20px" data-toggle="tooltip"   data-placement="bottom" title="'+jsLocaleMsg.loadStatus+'" height="20px" style="z-index:100px;"></div>';
		 }
		
		function nameFormatter(cellvalue, options, rowObject){
			var status = rowObject["status"];
			var serverDetailDivId = rowObject["host"] +"_"+ rowObject["port"] + "_detail_lnk";
			<sec:authorize access="hasAuthority('VIEW_SERVER_AND_SERVER_INSTANCES')">
				return '<a class="link" id="' + serverDetailDivId + '_name" href="#" onclick="viewServerInstance('+"'" + rowObject["id"]+ "'"+')">' + cellvalue + '</a>' ;
			</sec:authorize>
			<sec:authorize access="!hasAuthority('VIEW_SERVER_AND_SERVER_INSTANCES')">
				return cellvalue;
			</sec:authorize>
		}
		
		function syncColumnFormatter(cellvalue, options, rowObject){
			return '';			
		}
		
		function searchInstanceCriteria(){
			clearResponseMsgDiv();
			clearInstanceGrid();
			var $grid = $("#instanceList");
			$grid.jqGrid('setGridParam',{datatype:'json',page: 1}).trigger('reloadGrid');
		}

		function resetSearchInstanceCriteria(){
			$("#search-server-instance").val('');
			$("#search-server-name").val('');
			$("#search-server-host").val('');
			$("#search-server-instance-port").val('');
			
			$('input:radio[name=search-server-instance-status]').each(function () { $(this).prop('checked', false); });
			$('input:radio[name=search-server-instance-sync-status]').each(function () { $(this).prop('checked', false); });
			searchInstanceCriteria();
		}
		
		function clearInstanceGrid(){
			var $grid = $("#instanceList");
			var rowIds = $grid.jqGrid('getDataIDs');
			// iterate through the rows and delete each of them
			for(var i=0,len=rowIds.length;i<len;i++){
				var currRow = rowIds[i];
				$grid.jqGrid('delRowData', currRow);
			}
		}
		
		function resizeInstanceGrid(){
			var $grid = $("#instanceList"),
		    newWidth = $grid.closest(".ui-jqgrid").parent().width();
	 	    if(newWidth < 1000){
		    	newWidth = 800;
		    }
		    //$grid.jqGrid("setGridWidth", newWidth, true);
		}
		
		function reloadConfigPopup(){
			clearAllMessages();
			$('#reload-config-buttons-div #btn-no').attr('onclick','closeFancyBox();searchInstanceCriteria();');
			
			if(ckIntanceSelected.length == 0){
				$("#lessWarn").show();
				$("#moreWarn").hide();
				$("#message").click();
				return;
			}else if(ckIntanceSelected.length > 1){
				$("#lessWarn").hide();
				$("#moreWarn").show();
				$("#message").click();
				return;
			}else{
				var state = checkInstanceState(ckIntanceSelected[0]);
				if(state=='ACTIVE'){
					$("#reloadconfig").click();	
					var instance = instanceList[ckIntanceSelected[0]];

					if(instance['syncStatus']==false)
						$('#reload-config-msgMismatch').show();
					else
						$('#reload-config-msgMismatch').hide();
				} else if(state=='CHECK') {
					$("#inactivewarn").click();
				} else {
					$("#offwarn").click();
				}
			}
		}
		
		function softRestartPopup(){
			clearAllMessages();
			$('#soft-restart-buttons-div #btn-no').attr('onclick','closeFancyBox();searchInstanceCriteria();');
			
			if(ckIntanceSelected.length == 0){
				$("#lessWarn").show();
				$("#moreWarn").hide();
				$("#message").click();
				return;
			}else if(ckIntanceSelected.length > 1){
				$("#lessWarn").hide();
				$("#moreWarn").show();
				$("#message").click();
				return;
			}else{
				var state = checkInstanceState(ckIntanceSelected[0]);
			
				if(state=='ACTIVE'){
					// find from instance list to get current synch status because on each synch we reload grid so will get latest status
					var instance = instanceList[ckIntanceSelected[0]];

					if(instance['syncStatus']==false)
						$('#soft-restart-msgMismatch').show();
					else
						$('#soft-restart-msgMismatch').hide();
					
					$("#softrestart").click();	
				}else if(state=='CHECK') {
					$("#inactivewarn").click();
				} else
					$("#offwarn").click();
			}
		}
		
		function syncPublishPopup(){
			clearAllMessages();
			$('#sync-publish-buttons-div #btn-no').attr('onclick','closeFancyBox();searchInstanceCriteria();');
			
			if(ckIntanceSelected.length == 0){
				$("#lessWarn").show();
				$("#moreWarn").hide();
				$("#message").click();
				return;
			}else if(ckIntanceSelected.length > 1){
				$("#lessWarn").hide();
				$("#moreWarn").show();
				$("#message").click();
				return;
			}else{
				$("#syncPublish").click();	
			}
		}
		
		function reloadCachePopup(){
			
			clearAllMessages();
			$('#reload-cache-buttons-div #btn-no').attr('onclick','closeFancyBox();searchInstanceCriteria();');
			
			if(ckIntanceSelected.length == 0){
				$("#lessWarn").show();
				$("#moreWarn").hide();
				$("#message").click();
				return;
			}else if(ckIntanceSelected.length > 1){
				$("#lessWarn").hide();
				$("#moreWarn").show();
				$("#message").click();
				return;
			}else{
				var state = checkInstanceState(ckIntanceSelected[0]);
			
				if(state=='ACTIVE'){
					// find from instance list to get current synch status because on each synch we reload grid so will get latest status
					var instance = instanceList[ckIntanceSelected[0]];

					if(instance['syncStatus']==false)
						$('#reload-cache-msgMismatch').show();
					else
						$('#reload-cache-msgMismatch').hide();
					
					$("#reloadcache").click();	
				} else if(state=='CHECK') {
					$("#inactivewarn").click();
				} else
					$("#offwarn").click();
			}
		}

		function startPopup(){
			clearAllMessages();
			
			
			$('#start-buttons-div #btn-no').attr('onclick','closeFancyBox();searchInstanceCriteria();');
			
			// find from instance list to get current synch status because on each synch we reload grid so will get latest status
			var instance = instanceList[ckIntanceSelected[0]];
			if(instance['syncStatus']==false)
				$('#start-msgMismatch').show();
			else
				$('#start-msgMismatch').hide();
			
			$("#start").click();
		}
		
		function stopPopup(){
			clearAllMessages();
			$("#stop").click();
		}
		
		function synchronizePopup(){
			clearAllMessages();
			$("#btnSynchPopup").show();
			$("#btnSynchCancel").show();
			$("#btnSynchClose").hide();
			
			var tableString ='<table class="table table-hover" style="width:100%">';
			if(ckIntanceSelected.length == 0){
				$("#lessWarn").show();
				$("#moreWarn").hide();
				$("#message").click();
				return;
			}
			
			var rowId='',serverIP='',serverPort='',serverInstance='';
			
			if(ckIntanceSelected.length >0){
				tableString += "<tr>";
				tableString += "<th><spring:message code='server.instance.sync.header.label.instance.name'></spring:message></th>";
				tableString += "<th><spring:message code='server.instance.sync.header.label.server.ip'></spring:message></th>";
				tableString += "<th><spring:message code='server.instance.sync.header.label.server.port'></spring:message></th>";
				tableString += "<th class='status'><spring:message code='server.instance.sync.header.label.server.status'></spring:message></th>";
				tableString += "</tr>";
			}
			
			for(var index=0;index<ckIntanceSelected.length;index++){
			
				rowId = ckIntanceSelected[index];
				serverIP= jQuery('#instanceList').jqGrid ('getCell',rowId, 'host' );
				serverPort = jQuery('#instanceList').jqGrid ('getCell', rowId, 'port');
				serverInstance = $(jQuery('#instanceList').jqGrid ('getCell', rowId, 'instanceName')).closest("a").html();
				
				tableString += "<tr>";
				tableString += "<td>"+serverInstance+"</td>";
				tableString += "<td>"+serverIP+"</td>";
				tableString += "<td>"+serverPort+"</td>";
				tableString += "<td class='status' id='res_"+rowId+"'></td>";
				tableString += "</tr>";
			}
			tableString += "</table>"
			$("#divInstanceList").html(tableString);
			
			$('.status').hide();
			$('#divSyncMsg').html('');
			$("#synchronize").click();

			// set instnce id to synchronize
			$("#syncInstanceId").val(rowId);
		}
		
		function synchInstanceById(id){
			clearAllMessages();
			$("#btnSynchPopup").show();
			$("#btnSynchCancel").show();
			$("#btnSynchClose").hide();
			
	        var rowData = jQuery("#instanceList").getRowData(id); 
	        var ch = jQuery("#instanceList").find('#'+id+' input[type=checkbox]').prop('checked',true);
			
	        ckIntanceSelected[0] = id;
			var rowId = ckIntanceSelected[0];
			var serverIP= jQuery('#instanceList').jqGrid ('getCell',id, 'host' );
			var serverPort = jQuery('#instanceList').jqGrid ('getCell', id, 'port');
			var serverInstance = $(jQuery('#instanceList').jqGrid ('getCell', id, 'instanceName')).closest("a").html();
			
			$("#divInstanceList").html('');
			var tableString ='<table class="table table-hover" style="width:100%">';
			tableString += "<tr>";
			tableString += "<th><spring:message code='server.instance.sync.header.label.instance.name'></spring:message></th>";
			tableString += "<th><spring:message code='server.instance.sync.header.label.server.ip'></spring:message></th>";
			tableString += "<th><spring:message code='server.instance.sync.header.label.server.port'></spring:message></th>";
			tableString += "<th class='status'><spring:message code='server.instance.sync.header.label.server.status'></spring:message></th>";
			tableString += "</tr>";
			tableString += "<tr>";
			tableString += "<td>"+serverInstance+"</td>";
			tableString += "<td>"+serverIP+"</td>";
			tableString += "<td>"+serverPort+"</td>";
			tableString += "<td class='status' id='res_"+rowId+"'></td>";
			tableString += "</tr>";
			tableString += "</table>"
			$("#divInstanceList").html(tableString);
			$('.status').hide();
			
			$('#divSyncMsg').html('');
			$("#synchronize").click();

			// set instnce id to synchronize
			$("#syncInstanceId").val(id);
		}
		
		function exportConfigPopup(){
			clearAllMessages();
			if(ckIntanceSelected.length == 0){
				$("#lessWarn").show();
				$("#moreWarn").hide();
				$("#message").click();
				return;
			}else if(ckIntanceSelected.length > 1){
				$("#lessWarn").hide();
				$("#moreWarn").show();
				$("#message").click();
				return;
			}else{
				
				var state = checkInstanceState(ckIntanceSelected[0]);
				if(state=='CHECK'){
					$("#inactivewarn").click();
					return;
				}
				
				// set instance id which is selected for import to submit with form
				$("#exportInstancesId").val(ckIntanceSelected[0]);
				$("#isExportForDelete").val(false);
				$("#REQUEST_ACTION_TYPE").val('<%=BaseConstants.SERVER_MANAGEMENT%>');
				$("#exportPath").val("");
				$("#export-config-form").submit();
			}
		}
		
		function copyConfigPopup(){
			clearAllMessages();
			$("#copyMessage").html('');
			$("#btncopyConfigPopUp").hide();
			$("#copyConfigCancel").show();
			$("#btnCopyClose").hide();
			if(ckIntanceSelected.length == 0){
				$("#lessWarn").show();
				$("#moreWarn").hide();
				$("#message").click();
				return;
			}else if(ckIntanceSelected.length > 1){
				$("#lessWarn").hide();
				$("#moreWarn").show();
				$("#message").click();
				return;
			}else{
				
				var state = checkInstanceState(ckIntanceSelected[0]);
				if(state=='CHECK'){
					$("#inactivewarn").click();
					return;
				}
				
				$("#tblCopyFrom").html('');
				$("#tblCopyTo").html('');
				
				var data =  jQuery("#instanceList").jqGrid('getRowData');
				
				var gridData = jQuery("#instanceList").jqGrid('getRowData', ckIntanceSelected[0]);
				var instanceName = $(jQuery('#instanceList').jqGrid ('getCell', ckIntanceSelected[0], 'instanceName')).closest("a").html()
				$("#copy-config-progress-bar-div").show();
				$.ajax({
					url: 'getAllInstancesByServerType',
					cache: false,
					async: true,
					dataType:'json',
					type:'POST',
					data:
					 {
						"serverInstanceId" : gridData.id
					}, 
					success: function(data){
						var response = eval(data);
						var responseCode = response.code; 
						var responseMsg = response.msg; 
						var responseObject = response.object;
						$("#copy-config-progress-bar-div").hide();
						$("#btncopyConfigPopUp").show();
						var copyFromBody = "<table class='table table-hover table-bordered'  border='1' style='overflow: auto;height:200px;' ><thead><tr><th width='20%'><spring:message code='server.instance.copy.from.label'></spring:message></th><th width='20%'><spring:message code='server.instance.sync.header.label.instance.name'></spring:message></th><th width='20%'><spring:message code='server.instance.sync.header.label.server.ip'></spring:message></th><th width='40%'><spring:message code='server.instance.sync.header.label.server.port'></spring:message></th></tr></tr></thead>";
						copyFromBody+= "<tr><td><input id ='"+gridData.host+"_"+instanceName+"_chkbox"+"'type='checkbox' checked value='"+gridData.id+"'/></td><td>"+instanceName+"</td><td>"+gridData.host+"</td><td>"+gridData.port + "</td><td></td></tr>";
						copyFromBody+= " </table>";
						$("#tblCopyFrom").html(copyFromBody);
						
						var copyToBody = "<table class='table table-hover table-bordered'  border='1' ><thead><tr><th width='20%'><spring:message code='server.instance.copy.to.label'></spring:message></th><th width='20%'><spring:message code='server.instance.sync.header.label.instance.name'></spring:message></th><th width='20%'><spring:message code='server.instance.sync.header.label.server.ip'></spring:message></th><th width='20%'><spring:message code='server.instance.sync.header.label.server.port'></spring:message></th><th class='status' width='20%'><spring:message code='server.instance.sync.header.label.server.copy.status'></spring:message></th></tr></thead>";	
						if(responseCode == "200"){
							$.each(responseObject, function(index, responseObject) {
								if(responseObject.id != ckIntanceSelected[0]){
									copyToBody+="<tr><td><input id ='"+responseObject.server.ipAddress+"_"+responseObject.name+"_chkbox"+"'type='checkbox' value='"+responseObject.id+"' onchange='clearMessage();'/></td><td>"+responseObject.name+"</td><td>"+responseObject.server.ipAddress+"</td><td>"+responseObject.port + "</td><td class='status' id='status_"+responseObject.id+"'></td></tr>";
								}
						    });
							copyToBody+="</table>";
							$(".status").hide();
							$("#tblCopyTo").html(copyToBody);
						}else{
							showErrorMsgPopUp(responseMsg);
						}
					},
				    error: function (xhr,st,err){
						handleGenericError(xhr,st,err);
					}
				});
				
				$('#copyconfig').click();
			}
		}
		
		function synchInstanceById(id){
			clearAllMessages();
			$("#btnSynchPopup").show();
			$("#btnSynchCancel").show();
			$("#btnSynchClose").hide();
			
	        var rowData = jQuery("#instanceList").getRowData(id); 
	        var ch = jQuery("#instanceList").find('#'+id+' input[type=checkbox]').prop('checked',true);
			
	        ckIntanceSelected[0] = id;
			var rowId = ckIntanceSelected[0];
			var serverIP= jQuery('#instanceList').jqGrid ('getCell',id, 'host' );
			var serverPort = jQuery('#instanceList').jqGrid ('getCell', id, 'port');
			var serverInstance = $(jQuery('#instanceList').jqGrid ('getCell', id, 'instanceName')).closest("a").html();
			
			$("#divInstanceList").html('');
			var tableString ='<table class="table table-hover" style="width:100%">';
			tableString += "<tr>";
			tableString += "<th><spring:message code='server.instance.sync.header.label.instance.name'></spring:message></th>";
			tableString += "<th><spring:message code='server.instance.sync.header.label.server.ip'></spring:message></th>";
			tableString += "<th><spring:message code='server.instance.sync.header.label.server.port'></spring:message></th>";
			tableString += "<th class='status'><spring:message code='server.instance.sync.header.label.server.status'></spring:message></th>";
			tableString += "</tr>";
			tableString += "<tr>";
			tableString += "<td>"+serverInstance+"</td>";
			tableString += "<td>"+serverIP+"</td>";
			tableString += "<td>"+serverPort+"</td>";
			tableString += "<td class='status' id='res_"+rowId+"'></td>";
			tableString += "</tr>";
			tableString += "</table>"
			$("#divInstanceList").html(tableString);
			$('.status').hide();
			
			$('#divSyncMsg').html('');
			$("#synchronize").click();

			// set instnce id to synchronize
			$("#syncInstanceId").val(id);
		}
		
		
		function synchronizesInstance(){
			
			$("#synch-buttons-div").hide();
			$("#synch-progress-bar-div").show();
			clearAllMessages();
			
			// generate SI state string to pass
			var instanceStatus="";
			$.each(ckIntanceSelected, function(key,val){
				instanceStatus += checkInstanceState(val) + ",";
			});
			
			instanceStatus = instanceStatus.substring(0,instanceStatus.length-1);
			
			$.ajax({
					url: '<%= ControllerConstants.SYNC_SERVER_INSTANCE %>',
 				    cache: false,
 					async: true,
 					dataType: 'json',
 					type: "POST",
 					data: {
 						serverInstanceId: ckIntanceSelected.join(),
 						serverInstancesStatus:instanceStatus
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
 				    		$('#divSyncMsg').html('<span class="title" style="color:black;font-weight:bolder">'+response.msg+'</span>');
 				    		
			    			var response = data.object;
			    			$.each(response, function(key,val){
			    				var resObj =val;
			    				if(resObj.code == "700") {
			    					$('#res_'+key).text(resObj.msg+" Click the link to Configure Path ");
			    					$('#res_'+key).append("<form method='post' action='initSystemAgentConfig' id='configurePakStatisticsPath_"+key+"'><input type='hidden' name='agent_serverInstanceId' value='"+key+"'></input><a href='#' onclick='parentNode.submit()' >Configure</a></form>");
			    				} else if (resObj.code == "701") {
			    					$('#res_'+key).text(resObj.msg+" Click the link to Configure DataSource ");			    					
			    					$('#res_'+key).append("<form method='post' action='initUpdateServerInstance' id='configureDataSource_"+key+"'><input type='hidden' name='serverInstanceId' value='"+key+"'></input><input type='hidden' name='REQUEST_ACTION_TYPE' value='UPDATE_INSTANCE_ADVANCE_CONFIG'></input><a href='#' onclick='parentNode.submit()' >Configure</a></form>");
			    				}else if(resObj.code == "702") {
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
			    			$("#synchronizePopUpMsgDiv").hide();
			    			$(".synchronizePopUpMsgClass").empty();
			    			$("#synchronizePopUpMsg").hide();
 				    		
 				    	}else{
 
 				    		$('#divSyncMsg').html('<span class="title" style="color: #FF0000;">'+response.msg+'</span>');
 				    	}
 					},
 				    error: function (xhr,st,err){
 				    	$("#synch-buttons-div").show();
	 					$("#synch-progress-bar-div").hide();
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
 						id: ckIntanceSelected.join()
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
			var instanceId = ckIntanceSelected.join();
			if(instanceId!=undefined && instanceId!=""){
				$("#syncPublishInstanceId").val(instanceId);
			}
			$.ajax({
					url: '<%= ControllerConstants.SYNC_PUBLISH_INSTANCE %>',
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
						id: ckIntanceSelected.join()
					},
					success: function(data){
						$("#reload-config-buttons-div").show();
						$("#reload-config-progress-bar-div").hide();
						var response = eval(data);
				    	response.msg = decodeMessage(response.msg);
				    	response.msg = replaceAll("+"," ",response.msg);
			    	
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
				    		showErrorMsg(response.msg);
				    		closeFancyBox();
				    		clearSelection();
				    	}
					},
				    error: function (xhr,st,err){
				    	$("#reload-config-buttons-div").show();
						$("#reload-config-progress-bar-div").hide();
				    	handleGenericError(xhr,st,err);
					}
				});
		}
		
		function stopInstance(){
			$("#stop-buttons-div").hide();
			$("#stop-progress-bar-div").show();
			$.ajax({
				url: '<%= ControllerConstants.STOP_SERVER_INSTANCE %>',
				    cache: false,
					async: true,
					dataType: 'json',
					type: "POST",
					timeout : 15000,
					data: {
						id: ckIntanceSelected.join()
					},
					success: function(data){
						$("#stop-buttons-div").show();
						$("#stop-progress-bar-div").hide();
						var response = eval(data);
				    	response.msg = decodeMessage(response.msg);
				    	response.msg = replaceAll("+"," ",response.msg);
			    	
				    	if(response.code == 200 || response.code == "200") {
				    		//resetSearchInstanceCriteria();
				    		searchInstanceCriteria();
				    		clearResponseMsgDiv();
				    		clearResponseMsg();
				    		clearErrorMsg();
				    		showSuccessMsg(response.msg);
				    		ckIntanceSelected = new Array();
				    		closeFancyBox();
				    		clearSelection();
				    	}else{
				    		showErrorMsg(response.msg);
							closeFancyBox();
				    		clearSelection();
				    	}
					},
				    error: function (xhr,st,err){
				    	if(st == "timeout"){
				    		closeFancyBox();
				    		resetSearchInstanceCriteria();
				    		clearResponseMsgDiv();
				    		clearResponseMsg();
				    		clearErrorMsg();
				    		showSuccessMsg("A shutdown call has been initiated to the server it should update in a while,if not try reloading the page to view the changes.");
				    		$.ajax({
								url: '<%= ControllerConstants.STOP_SERVER_INSTANCE %>',
								    cache: false,
									async: true,
									dataType: 'json',
									type: "POST",
									data: {
										id: ckIntanceSelected.join()
									},
									success: function(data){
										$("#stop-buttons-div").show();
										$("#stop-progress-bar-div").hide();
										var response = eval(data);
								    	response.msg = decodeMessage(response.msg);
								    	response.msg = replaceAll("+"," ",response.msg);
							    	
								    	if(response.code == 200 || response.code == "200") {
								    		//resetSearchInstanceCriteria();
								    		searchInstanceCriteria();
								    		clearResponseMsgDiv();
								    		clearResponseMsg();
								    		clearErrorMsg();
								    		showSuccessMsg(response.msg);
								    		ckIntanceSelected = new Array();
								    		closeFancyBox();
								    		clearSelection();
								    	}else{
								    		showErrorMsg(response.msg);
											closeFancyBox();
								    		clearSelection();
								    	}
									},
								    error: function (xhr,st,err){
								    	$("#stop-buttons-div").show();
										$("#stop-progress-bar-div").hide();
								    	handleGenericError(xhr,st,err);
									}
								});
				    	}
				    	$("#stop-buttons-div").show();
						$("#stop-progress-bar-div").hide();
				    	handleGenericError(xhr,st,err);
					}
				});
		}
		
		function startInstance(){
			$("#start-buttons-div").hide();
			$("#stop-progress-bar-div").show();
			$.ajax({
				url: '<%= ControllerConstants.START_SERVER_INSTANCE %>',
				    cache: false,
					async: true,
					dataType: 'json',
					type: "POST",
					data: {
						id: ckIntanceSelected.join()
					},
					success: function(data){
						$("#start-buttons-div").show();
						$("#stop-progress-bar-div").hide();
						var response = eval(data);
				    	response.msg = decodeMessage(response.msg);
				    	response.msg = replaceAll("+"," ",response.msg);
			    	
				    	if(response.code == 200 || response.code == "200") {
				    		//resetSearchInstanceCriteria();
				    		searchInstanceCriteria();
				    		clearResponseMsgDiv();
				    		clearResponseMsg();
				    		clearErrorMsg();
				    		showSuccessMsg(response.msg);
				    		ckIntanceSelected = new Array();
				    		closeFancyBox();
				    		clearSelection();
				    	}else{
				    		$('#'+ckIntanceSelected.join()+'_INACTIVE').bootstrapToggle('off');
				    		showErrorMsgPopUp(response.msg);
				    		$("#btnStart").hide();
				    		$("#startNote").hide();				    		
				    		clearSelection();
				    	}
					},
				    error: function (xhr,st,err){
				    	$("#start-buttons-div").show();
						$("#start-progress-bar-div").hide();
				    	handleGenericError(xhr,st,err);
					}
				});
		}
		
		function clearSelection(){
			var grid = $("#instanceList");
			var rowIds = grid.getDataIDs();
			// iterate through the rows and deselect each of them
			for(var i=0,len=rowIds.length;i< len;i++){
				jQuery("#instanceList").find('#'+rowIds[i]+' input[type=checkbox]').prop('checked',false);
			}
			ckIntanceSelected = new Array();
		}
		
		function checkInstanceState(rowId){
			
			if($(jQuery('#instanceList').jqGrid ('getCell', rowId, 'status')).find(".checkboxbg").length > 0){
				return $(jQuery('#instanceList').jqGrid ('getCell', rowId, 'status')).find(".checkboxbg").prop('id').split('_')[1];	
			} else {
				return $(jQuery('#instanceList').jqGrid ('getCell', rowId, 'status')).find("img").prop('id').split('_')[1];
			}
		}
		
		function restartInstance(){
			$("#buttons-div").hide();
			$("#progress-bar-div").show();
			
			$.ajax({
					url: '<%= ControllerConstants.RESTART_SERVER_INSTANCE %>',
 				    cache: false,
 					async: true,
 					dataType: 'json',
 					type: "POST",
 					timeout : 15000,
 					data: {
 						id: ckIntanceSelected.join()
 					},
 					success: function(data){
 						$("#buttons-div").show();
 						$("#progress-bar-div").hide();
 						var response = eval(data);
 				    	response.msg = decodeMessage(response.msg);
 				    	response.msg = replaceAll("+"," ",response.msg);

 				    	if(response.code == 200 || response.code == "200") {
 				    		//resetSearchInstanceCriteria();
 				    		searchInstanceCriteria();
 				    		clearResponseMsgDiv();
 				    		clearResponseMsg();
 				    		clearErrorMsg();
 				    		showSuccessMsg(response.msg);
 				    		ckIntanceSelected = new Array();
 				    		closeFancyBox();
 				    		clearSelection();
 				    	}else{
 				    		$('#'+ckIntanceSelected.join()+'_ACTIVE').bootstrapToggle('off');
 				    		$('#'+ckIntanceSelected.join()+'_ACTIVE').attr('id',ckIntanceSelected.join()+'_INACTIVE');
 				    		showErrorMsgPopUp(response.msg);
				    		$("#btnStart").hide();
				    		$("#startNote").hide();				    		
 				    		/* closeFancyBox(); */
				    		clearSelection();
 				    	}
 					},
 				    error: function (xhr,st,err){
 				    	if(st == "timeout"){
 				    		clearResponseMsgDiv();
 				    		clearResponseMsg();
 				    		clearErrorMsg();
 				    		closeFancyBox();
 				    		clearSelection();
 				    		showSuccessMsg("A restart call has been initiated to the server it should update in a while,if not try reloading the page to view the changes.");
 				    		$.ajax({
 								url: '<%= ControllerConstants.RESTART_SERVER_INSTANCE %>',
 			 				    cache: false,
 			 					async: true,
 			 					dataType: 'json',
 			 					type: "POST",
 			 					data: {
 			 						id: ckIntanceSelected.join()
 			 					},
 			 					success: function(data){
 			 						$("#buttons-div").show();
 			 						$("#progress-bar-div").hide();
 			 						var response = eval(data);
 			 				    	response.msg = decodeMessage(response.msg);
 			 				    	response.msg = replaceAll("+"," ",response.msg);

 			 				    	if(response.code == 200 || response.code == "200") {
 			 				    		//resetSearchInstanceCriteria();
 			 				    		searchInstanceCriteria();
 			 				    		clearResponseMsgDiv();
 			 				    		clearResponseMsg();
 			 				    		clearErrorMsg();
 			 				    		showSuccessMsg(response.msg);
 			 				    		ckIntanceSelected = new Array();
 			 				    		closeFancyBox();
 			 				    		clearSelection();
 			 				    	}else{
 			 				    		$('#'+ckIntanceSelected.join()+'_ACTIVE').bootstrapToggle('off');
 			 				    		$('#'+ckIntanceSelected.join()+'_ACTIVE').attr('id',ckIntanceSelected.join()+'_INACTIVE');
 			 				    		showErrorMsgPopUp(response.msg);
 							    		$("#btnStart").hide();
 							    		$("#startNote").hide();				    		
 			 				    		/* closeFancyBox(); */
 							    		clearSelection();
 			 				    	}
 			 					},
 			 				    error: function (xhr,st,err){
 			 						$("#buttons-div").show();
 			 						$("#progress-bar-div").hide();
 			 				    	handleGenericError(xhr,st,err);
 			 					}
 			 				});
 				    	}
 				    	
 						$("#buttons-div").show();
 						$("#progress-bar-div").hide();
 				    	handleGenericError(xhr,st,err);
 					}
 				});
		}

		function copyConfig(){
			
			var copyToIds="";
			var copyFromId="";
			
			var copyFromRes = $("#tblCopyFrom tr:has(:checked)");
			var result = $("#tblCopyTo tr:has(:checked)");
			
			if(result.length<1){
				$("#lessWarn").show();
				$("#moreWarn").hide();
				$("#copyMessage").html("<spring:message code='serverMgmt.no.to.instance.checked.warning'></spring:message>");
				return;
			} else if(copyFromRes.length<1){
				$("#lessWarn").show();
				$("#moreWarn").hide();
				$("#copyMessage").html("<spring:message code='serverMgmt.no.from.instance.checked.warning'></spring:message>");
				return;
			} else{
				$("#copy-config-buttons-div").hide();
				$("#copy-config-progress-bar-div").show();
				var valuesFrom = $('#tblCopyFrom tr input:checked').map(function() {
			        return this.value;
			    }).get();
				copyFromId=valuesFrom.toString();
				
				var values = $('#tblCopyTo tr input:checked').map(function() {
			        return this.value;
			    }).get();
				copyToIds=values.toString();
			 
			}
			
			 $.ajax({
				url: '<%= ControllerConstants.COPY_SERVER_INSTANCE_CONFIG %>',
				    cache: false,
					async: true,
					dataType: 'json',
					type: "POST",
					data: {
						copyFromId: copyFromId,
						copyToIds: copyToIds
					},
					
					success: function(data){
						$("#copy-config-buttons-div").show();
 						$("#copy-config-progress-bar-div").hide();
 						var response = eval(data);
 				    	response.msg = decodeMessage(response.msg);
 				    	response.msg = replaceAll("+"," ",response.msg);
 				    	
				    	
 				    	if(response.code == 200 || response.code == "200") {
 				    		clearResponseMsgDiv();
 				    		clearResponseMsg();
 				    		clearErrorMsg();
 				    		ckIntanceSelected = new Array();
 				    		//closeFancyBox();
 				    		clearSelection();
 				    		$('#copyMessage').html('<span class="title" style="color:black;font-weight:bolder">'+response.msg+'</span>');
 				    		
							var responseCode = data.code;
 				    		
			    			var response = data.object;
			    			$.each(response, function(key,val){
			    				var resObj =val;
			    			    $('#status_'+key).text(resObj.msg);
			    			});
			    			$(".status").show();
			    			$("#btncopyConfigPopUp").hide();
			    			$("#copyConfigCancel").hide();
			    			$("#btnCopyClose").show();
			    			
 				    	}else{
 				    		$('#copyMessage').html('<span class="title" style="color:#FF0000;">'+response.msg+'</span>');
 				    		showErrorMsg(response.msg);
 				    		closeFancyBox();
				    		clearSelection();
 				    	}
 					},
 				    error: function (xhr,st,err){
 						$("#buttons-div").show();
 						$("#progress-bar-div").hide();
 				    	handleGenericError(xhr,st,err);
 					}
				
 					});
 		}
		
		function clearMessage(){
			$("#copyMessage").html('');
		}
		

		
		function goToApplyLicense(serverInstanceId, licenseAction){
			 $('#license_intance_id').val(serverInstanceId);
			 $('#license_action').val(licenseAction);
			 $("#apply_license_form").submit();
		}
		
		
		function loadInstanceStatus(rowObject){
			var serverInstanceId = rowObject["id"];
			var syncDivId = rowObject["host"] +"_"+ rowObject["port"] + "_sync_btn";
			clearErrorMsg2();
			$.ajax({
				url: 'loadServerInstanceStatus',
				cache: false,
				async: true,
				dataType: 'json',
				type: "POST",
				data: {
					"serverInstanceId": serverInstanceId
				},
				success: function(data){ 
					
					var response = eval(data);
					var responseObject = response.object;
					
					var instanceStatus = responseObject.status;
					var toggleId = serverInstanceId + "_" + instanceStatus;
					var toggleIdDiv = rowObject["host"] +"_"+ rowObject["port"] + "_toggle_btn";
					var syncDivId = rowObject["host"] +"_"+ rowObject["port"] + "_sync_btn";
					if(rowObject["syncStatus"] == false){
						$("#"+serverInstanceId).find('td[aria-describedby="instanceList_syncStatus"]').html('<div id="'+syncDivId+'"><img id="synch_'+rowObject["id"]+'" src="img/orange.png" style="cursor: pointer;disabled:false" onclick ="synchInstanceById('+"'" + rowObject["id"]+ "'"+')" /></div>');
					} else {
						$("#"+serverInstanceId).find('td[aria-describedby="instanceList_syncStatus"]').html('<div id="'+syncDivId+'"><img id="synch_'+rowObject["id"]+'" src="img/grey.png" style="cursor: pointer;disabled:true"/></div>');
					}
					$("#"+serverInstanceId).find('td[aria-describedby="instanceList_"]').html("<input type='checkbox' name='"+rowObject["host"]+"_"+rowObject["port"]+"_chkbox"+"' id='"+rowObject["host"]+"_"+rowObject["port"]+"_chkbox"+"' style='cursor:pointer;disabled:true' onclick=\"addRowId(\'"+rowObject["host"]+"_"+rowObject["port"]+"_chkbox"+"\', \'"+rowObject["id"]+"\')\"; />");
					var chkboxId= rowObject["host"]+"_"+rowObject["port"]+"_chkbox";
					document.getElementById(""+chkboxId).disabled= false;
					
					if(response.code == '200'){
						if(instanceStatus == 'ACTIVE'){
							<sec:authorize access="hasAuthority('SERVER_INSTANCE_STOP')">
							$('#loader_'+serverInstanceId).html('<div id="'+toggleIdDiv+'"><input class="checkboxbg" id=' + toggleId + ' data-on="On" data-off="Off" checked type="checkbox" data-size="mini"></div>');
							</sec:authorize>
							<sec:authorize access="!hasAuthority('SERVER_INSTANCE_STOP')">
							$('#loader_'+serverInstanceId).html('<div id="'+toggleIdDiv+'"><input class="checkboxbg" id=' + toggleId + ' data-on="On" data-off="Off" checked type="checkbox" data-size="mini" disabled></div>');
							</sec:authorize>
			 			}else{
			 				<sec:authorize access="hasAuthority('SERVER_INSTANCE_START')">
			 				$('#loader_'+serverInstanceId).html('<div id="'+toggleIdDiv+'"><input class="checkboxbg" id=' + toggleId + ' data-on="On" data-off="Off" type="checkbox" data-size="mini"></div>');
			 				</sec:authorize>
			 				<sec:authorize access="!hasAuthority('SERVER_INSTANCE_START')">
			 				$('#loader_'+serverInstanceId).html('<div id="'+toggleIdDiv+'"><input class="checkboxbg" id=' + toggleId + ' data-on="On" data-off="Off" type="checkbox" data-size="mini" disabled></div>');
			 				</sec:authorize>
			 			}
					} else {
						<sec:authorize access="hasAuthority('SERVER_INSTANCE_START')">
						$('#loader_'+serverInstanceId).html('<div id="'+toggleIdDiv+'"><input class="checkboxbg" id=' + toggleId + ' data-on="On" data-off="Off" type="checkbox" data-size="mini"></div>');
						</sec:authorize>
						<sec:authorize access="!hasAuthority('SERVER_INSTANCE_START')">
						$('#loader_'+serverInstanceId).html('<div id="'+toggleIdDiv+'"><input class="checkboxbg" id=' + toggleId + ' data-on="On" data-off="Off" type="checkbox" data-size="mini" disabled></div>');
						</sec:authorize>
					}
					
					var licenseAction =  responseObject.licenseAction;
					var licenseDuration =  responseObject.licenseDuration;
					
					var activationLink;
					var startTimeLink;
					if(licenseDuration == 'Registered'){
						activationLink = "<span>"+licenseDuration+"</span>";
					}else if(licenseDuration == 'Trial'){
						activationLink = "<a href='#' class='link' onclick=goToApplyLicense('"+serverInstanceId+"','"+licenseAction+"')>"+licenseDuration+"</a>";
					}else if(licenseDuration == 'Upload'){
						activationLink = "<a href='#' class='link' onclick=goToApplyLicense('"+serverInstanceId+"','"+licenseAction+"')>"+licenseDuration+"</a>";
					}else if(licenseDuration == 'NO_LICENSE' ){
						activationLink = "<span  title='<spring:message code='engine.trial.license.msg'></spring:message>'>-</span>";
					}else{
						activationLink = "<a href='#' class='link' onclick=goToApplyLicense('"+serverInstanceId+"','"+licenseAction+"')>"+licenseDuration+"</a>";	
					}
					startTimeLink = "<span>"+responseObject.serverStartTime+"</span>";
					$('#license_'+serverInstanceId).html(activationLink);
					$('#serverStartTime_'+serverInstanceId).html(startTimeLink);
					
					// enable checkbox for server instance in row
					var chkboxId= rowObject["host"]+"_"+rowObject["port"]+"_chkbox"
					//document.getElementById(chkboxId).disabled= false;
					
					// enable synch flag after loading server instance status if has access right than
					<sec:authorize access="hasAuthority('SERVER_INSTANCE_SYNCHRONIZATION')">
					//$('#synch_'+serverInstanceId).attr("onclick","synchInstanceById('"+serverInstanceId+"')");
					</sec:authorize>
					
					$('.checkboxbg').bootstrapToggle();
					$('.checkboxbg').change(function(){
	    				instanceActiveInactiveToggleChanged(this);
	    			});
				},
			    error: function (xhr,st,err){
					handleGenericError(xhr,st,err);
				}
			});
		}
		
		$( document ).ready(function() {
			$("#server-manager-block input").keypress(function (event) {
			    if (event.which == 13) {
			        event.preventDefault();
			        $(this).blur();
			        searchInstanceCriteria();
			    }
			});
		});
		
		function reloadCache(){
			clearAllMessages();
			$("#reload-cache-buttons-div").hide();
			$("#reload-cache-progress-bar-div").show();
			$.ajax({
					url: '<%= ControllerConstants.SERVER_INSTANCE_RELOAD_CACHE %>',
 				    cache: false,
 					async: true,
 					dataType: 'json',
 					type: "POST",
 					data: {
 						id: ckIntanceSelected.join()
 					},
 					success: function(data){
 						$("#reload-cache-buttons-div").show();
 						$("#reload-cache-progress-bar-div").hide();
 						var response = eval(data);
 				    	response.msg = decodeMessage(response.msg);
 				    	response.msg = replaceAll("+"," ",response.msg);
				    	
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
 				    		showErrorMsg(response.msg);
 				    		closeFancyBox();
				    		clearSelection();
 				    	}
 					},
 				    error: function (xhr,st,err){
 						$("#reload-cache-buttons-div").show();
 						$("#reload-cache-progress-bar-div").hide();
 				    	handleGenericError(xhr,st,err);
 					}
 				});
		}

</script>
  
 
