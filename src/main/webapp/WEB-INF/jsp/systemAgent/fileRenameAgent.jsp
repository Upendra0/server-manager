<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.elitecore.sm.common.constants.SystemParametersConstant"%>
<%@page import="com.elitecore.sm.util.MapCache"%>
<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="elitecore"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="/WEB-INF/ShowErrorTag.tld" prefix="elitecoreError"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<form:form modelAttribute="file_rename_agent_form_bean" method="POST" action="#" id="update-file-rename-agent-list">
	
	<div id="packetStatasticAgentParameterDiv" class="fullwidth inline-form">
	
 		<input type="hidden" id="server_Instance_Id" name="server_Instance_Id" value='${serverInstanceId}' /> 
 		<!-- systemAgentTypeId -->
 		<input type="hidden" id="fileRenameAgentId" name="fileRenameAgentId" value="${agentId}" />  		 		 		

		<div class="col-md-6 no-padding">
			<spring:message code="fileRenameAgent.initial.delay.label" var="name" ></spring:message>
			<spring:message code="fileRenameAgent.initial.delay.tooltip" var="tooltip" ></spring:message>

			<div class="form-group">
				<div class="table-cell-label">${name}<span class="required">*</span></div>
  				<div class="input-group ">
					<form:input path="initialDelay" cssClass="form-control table-cell input-sm" tabindex="1" id="initialDelay" data-toggle="tooltip"
						data-placement="bottom" title="${tooltip}" ></form:input>
						
					<spring:bind path="initialDelay">
						<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
					</spring:bind>
				</div>  
			</div>
		</div>	
		
		<div class="col-md-6 no-padding">
			<spring:message code="fileRenameAgent.execution.interval.label" var="name" ></spring:message>
			<spring:message code="fileRenameAgent.execution.interval.tooltip" var="tooltip" ></spring:message>

			<div class="form-group">
				<div class="table-cell-label">${name}<span class="required">*</span></div>
  				<div class="input-group ">
					<form:input path="executionInterval" cssClass="form-control table-cell input-sm" tabindex="2" id="executionInterval" data-toggle="tooltip"
						data-placement="bottom" title="${tooltip}" ></form:input>
						
					<spring:bind path="executionInterval">
						<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
					</spring:bind>
				</div>  
			</div>
		</div>		
	</div>	
</form:form>

<!----------- update button --------------->
<div class="fullwidth">
	<div class="button-set">
		<div id="fileRenameButtonDIv">
			<sec:authorize access="hasAuthority('UPDATE_FILE_RENAME_AGENT_DETAILS')">
				<button id="update_btn" class="btn btn-grey btn-xs " tabindex="3" onclick="updateFileRenameAgentDetails()">
					<spring:message code="btn.label.update" ></spring:message>
				</button>
			</sec:authorize>
		</div>
	</div>
</div>

<div id="update-packet-stat-progress-bar-div" style="display: none;">
	<jsp:include page="../common/processing-bar.jsp"></jsp:include>
</div>

       <div class="tab-content no-padding clearfix">
         	<div class="fullwidth">
   				<div class="title2">
   					<spring:message code="packetStata.agent.grid.serviceList.label" ></spring:message>
	   				 <span class="title2rightfield">
				          	<sec:authorize access="hasAuthority('ADD_SERVICE_TO_FILE_RENAME_AGENT')">
				          	<span class="title2rightfield-icon1-text">
				          		<a href="#" onclick="addServiceToFileRenameAgent();"><i class="fa fa-plus-circle"></i></a>
	          					<a href="#" id="addService" onclick="addServiceToFileRenameAgent();">
	          						<spring:message code="btn.label.add" ></spring:message>
	          					</a>
		          			</span>	
				          	</sec:authorize>
				      </span>
		          </div>
   			</div>

		 <sec:authorize access="hasAuthority('VIEW_FILE_RENAME_AGENT_DETAILS')">
			<div class="box-body table-responsive no-padding box" id="fileRenameAgentGrid" >
				<table class="table table-hover" id="packetStatasticAgentDiv"></table>
				<div id="packetStatasticAgentgridPagingDiv"></div> 
			</div>
		</sec:authorize>

        </div>

		<a href="#divCharRenameParams" class="fancybox" style="display: none;" id="char_rename_param_link">#</a>
		<div id="divCharRenameParams" style="width:100%; display: none;" >
			    <div class="modal-content">
			        <div class="modal-header padding10">
			            <h4 class="modal-title"><spring:message code="distributionService.pathlist.char.rename.sub.header"></spring:message></h4>
			        </div>
					
			        <div class="modal-body padding10 inline-form">
			        
		        		<div><jsp:include page="../common/responseMsgPopUp.jsp" ></jsp:include></div>
		        	
		        		<div class="fullwidth">
		        			<span class="title2rightfield"> 
							    <span class="title2rightfield-icon1-text" id="char_rename_add_link" >
				   				 <sec:authorize access="hasAuthority('ADD_SERVICE_TO_FILE_RENAME_AGENT')">	
				   					<a href="#" onclick="addCharRenameOperation('','','','','','','','','ADD');" ><i class="fa fa-plus-circle"></i></a>
				          			<a href="#" id="addCharacterRenameOperations" onclick="addCharRenameOperation('','','','','','','','','','ADD');" >
				   					<spring:message code="btn.label.add" ></spring:message></a>
				   				 </sec:authorize> 
				   				</span> 
				   			</span> 
		        		</div>
						<div class="fullwidth" id="plugin_char_rename_details" style="overflow: auto;height:250px;">
						</div>
			        </div>
			        <div id="view_char_rename_params_buttons-div" class="modal-footer padding10">
			         	<button type="button" id="closeCharacterRenameOperationPopup" class="btn btn-grey btn-xs " data-dismiss="modal" onclick="closeFancyBox();"><spring:message code="btn.label.close"></spring:message></button>
			        </div>
			    </div>
			    <!-- /.modal-content --> 
			</div>
			
			

<a href="#divAddServiceToFileRenameAgent" class="fancybox" style="display: none;" id="addServiceToFileRenameAgent">#</a>
<form:form modelAttribute="file_rename_agent_add_service_form_bean" method="POST" action="#;" id="file_rename_agent_service_form_bean"> 
<div id="divAddServiceToFileRenameAgent" style="width:100%; display: none;"  >

    <div class="modal-content">
    	
        <div class="modal-header padding10">
            <h4 class="modal-title">
            	<span id="add_plugin_header_label" style="display:none;"><spring:message code="fileRename.agent.service.add.grid.header.label" ></spring:message></span>
            	<span id="update_plugin_header_label" style="display:none;"> <spring:message code="fileRename.agent.service.update.grid.header.label" ></spring:message></span>
	   		</h4>
        </div>
        <div class="modal-body padding10 inline-form">
         	<div>
				<jsp:include page="../common/responseMsgPopUp.jsp" ></jsp:include>
        	</div>           
		            <input type="hidden" id="agentserverInstanceId" name="agentserverInstanceId" value='${serverInstanceId}' />
		            <input type="hidden" id="serviceFileRenameConfigId" name="serviceFileRenameConfigId" value='${serviceFileRenameConfigId}' />		           
		           
		            <div class="col-md-12 no-padding">
		           		<spring:message code="fileRename.agent.service.name.label" var="label"></spring:message>
		           		<spring:message code="fileRename.agent.service.name.label.tooltip" var="tooltip"></spring:message>
		            	<div class="form-group">
		         			<div class="table-cell-label">${label}<span class="required">*</span></div>
		             		<div class="input-group">
  		             			<form:select path="service" id="service" class="form-control table-cell input-sm" tabindex="4" data-toggle="tooltip" data-placement="bottom" title="${tooltip}" >
									<form:option value=""></form:option>
								</form:select>  
		             			
		             			<span class="input-group-addon add-on last" > <i class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="left" title=""></i></span>
		             		</div>
		            	</div>
		            </div>
		            
 		            <div class="col-md-12 no-padding">
		           		<spring:message code="fileRename.agent.service.destination.path.label" var="label"></spring:message>
		           		<spring:message code="fileRename.agent.service.destination.path.label.tooltip" var="tooltip"></spring:message>
		            	<div class="form-group">
		         			<div class="table-cell-label">${label}</div>
		             		<div class="input-group">
		             			<form:input path="destinationPath"  id="destinationPath" class="form-control table-cell input-sm" tabindex="5" data-toggle="tooltip" data-placement="bottom" title="${tooltip}" ></form:input>
		             			
		             			<spring:bind path="destinationPath">
									<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
								</spring:bind>
		             		</div>		             		
		            	</div>
		            </div>
		            
		            <div class="col-md-12 no-padding">
		           		<spring:message code="fileRename.agent.service.file.extention.label" var="label"></spring:message>
		           		<spring:message code="fileRename.agent.service.file.extention.label.tooltip" var="tooltip"></spring:message>
		            	<div class="form-group">
		         			<div class="table-cell-label">${label}<span class='required'>*</span></div>
		             		<div class="input-group">
		             			<form:input path="fileExtensitonList"  id="fileExtensitonList" class="form-control table-cell input-sm" tabindex="6" data-toggle="tooltip" data-placement="top" title="${tooltip}" ></form:input>
								<spring:bind path="fileExtensitonList">
									<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
								</spring:bind>		             		

		             		</div>
		             		
		            	</div>
		            </div>
 		            
		            <div class="col-md-12 no-padding">
		           		<spring:message code="fileRename.agent.file.extention.after.rename.label" var="label"></spring:message>
		           		<spring:message code="fileRename.agent.file.extention.after.rename.label.tooltip" var="tooltip"></spring:message>
		            	<div class="form-group">
		         			<div class="table-cell-label">${label}</div>
		             		<div class="input-group">
		             			<form:input path="extAfterRename"  id="extAfterRename" class="form-control table-cell input-sm" tabindex="7" data-toggle="tooltip" data-placement="top" title="${tooltip}" ></form:input>
								<spring:bind path="extAfterRename">
									<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
								</spring:bind>		             			
		             		</div>
		            	</div>
		            </div> 

		            <div class="col-md-12 no-padding">
		           		<spring:message code="character.rename.status.label" var="label"></spring:message>
		           		<spring:message code="character.rename.status.label.tooltip" var="tooltip"></spring:message>
		            	<div class="form-group">
		         			<div class="table-cell-label">${label}<span class="required">*</span></div>
		             		<div class="input-group">								
	                            <form:select path="charRenameOperationEnable" cssClass="form-control table-cell input-sm" id="charRenameOperationEnable" tabindex="8" data-toggle="tooltip" data-placement="top"  title="${tooltip }">								
								<c:forEach var="trueFalseEnum" items="${trueFalseEnum}">
                             			<form:option value="${trueFalseEnum}">${trueFalseEnum}</form:option>
                             		</c:forEach>
								</form:select>

		             			<span class="input-group-addon add-on last" > <i class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="left" title=""></i></span>
		             		</div>
		            	</div>
		            </div>


		             <div class="clearfix"></div>
		             
			         <div id="add_edit_plugin_button_div" class="modal-footer padding10">
	       
				         <div id="add_plugin_buttons_div" class="padding10" style="display:none;">
	 			           <sec:authorize access="hasAuthority('ADD_SERVICE_TO_FILE_RENAME_AGENT')"> 
				         		<button id="save_btn" type="button" class="btn btn-grey btn-xs " data-dismiss="modal" tabindex="8" onclick="addServiceConfig();"><spring:message code="btn.label.save"></spring:message></button>
	 			           </sec:authorize>	 
				         	<button type="button" class="btn btn-grey btn-xs " data-dismiss="modal" tabindex="9" onclick="closeFancyBox();"><spring:message code="btn.label.cancel"></spring:message></button>
				        </div>
				        				       
 				        <div id="update_plugin_buttons_div" class="padding10" style="display:none;">
				         	 <sec:authorize access="hasAuthority('UPDATE_SERVICE_TO_FILE_RENAME_AGENT')">
				         		<button id="updateService_btn" type="button" class="btn btn-grey btn-xs " data-dismiss="modal" tabindex="10" onclick="updateServiceConfiguration();"><spring:message code="btn.label.update"></spring:message></button>
				         	</sec:authorize>	
				         	<button type="button" class="btn btn-grey btn-xs " data-dismiss="modal" tabindex="11" onclick="closeFancyBox();"><spring:message code="btn.label.cancel"></spring:message></button>
				        </div> 
				        
				        <div id="view_plugin_buttons_div" class="modal-footer padding10" style="display:none;">
				         	<button type="button" class="btn btn-grey btn-xs " data-dismiss="modal" tabindex="12" onclick="closeFancyBox();"><spring:message code="btn.label.close"></spring:message></button>
				        </div>
				        <div id="view_progress-bar-div" class="modal-footer padding10 text-left" style="display: none;">
								<jsp:include page="../common/processing-bar.jsp"></jsp:include>
						</div>	
					</div>		
					
			</div><!-- modal body end here -->
    	</div><!-- /.modal-content --> 
	</div>	
</form:form>		
		
		<!-- Delete server warning messages and pop up code start here -->
		<a href="#deleteFileRenameAgentServicePopup" class="fancybox" style="display: none;" id="deleteFileRenameAgentService">#</a>
		   	<div id="deleteFileRenameAgentServicePopup" style=" width:100%; display: none;" >
			   <div class="modal-content">
		       		<div class="modal-header padding10">
		            	<h4 class="modal-title"><spring:message code="fileRename.delete.service.instance.popup.header"></spring:message></h4>
		        	</div>
		        	<div class="modal-body padding10 inline-form">
	        			<div id="divDeleteMsg" style="color:red;"><span class="title"></span></div>
			        	<div id ="delServiceImpNote">
						<p>	           	
			        	   	<span class="note"><spring:message code="label.important.note"></spring:message></span><br/><br/>
			           		<spring:message code="file.rename.agent.delete.service.popup.content"></spring:message>
			        	</p>
			        	</div>
		        	</div>
		        	<div id="delete-buttons-div" class="modal-footer padding10">

		        	    <button type="button" class="btn btn-grey btn-xs" id="btnDeleteServicePopup" onclick="deleteServiceFileRenameConfig();"><spring:message code="btn.label.delete"></spring:message></button>
		        	    <button type="button" class="btn btn-grey btn-xs " id="btnDeleteCancel" data-dismiss="modal" onclick="closeFancyBox();clearSelection();"><spring:message code="btn.label.cancel"></spring:message></button>
		        	</div>
					<div id="delete-progress-bar-div" class="modal-footer padding10 text-left" style="display: none;">
						<jsp:include page="../common/processing-bar.jsp"></jsp:include>
					</div>	
		   	   </div> 
			</div>
			
			<!-- warning message pop-up -->
			
		   	<a href="#divMessage" class="fancybox" style="display: none;" id="deleteWarnMsg">#</a>
		   	<div id="divMessage" style=" width:100%; display: none;" >
			    <div class="modal-content">
			    <input type="hidden" id="deleteBlockId"/>
			    
			        <div class="modal-header padding10">
			            <h4 class="modal-title">Warning</h4>
			        </div>
			        <div class="modal-body padding10 inline-form">
				        <p id="moreWarn">
				        	<spring:message code="serviceMgmt.more.instance.checked.warning"></spring:message>    
				        </p>
				        <p id="lessWarn">
				        	<spring:message code="serviceMgmt.no.instance.checked.warning"></spring:message>    
				        </p>
			        </div>
			        <div class="modal-footer padding10">
			            <button type="button" class="btn btn-grey btn-xs " data-dismiss="modal" tabindex="13" onclick="closeFancyBox();"><spring:message code="btn.label.close"></spring:message></button>
			        </div>
			    </div>
			    <!-- /.modal-content --> 
			</div>		
			
		<a href="#divCharRenameDeletemPopup" class="fancybox" style="display: none;" id="delete_char_rename_param_link">#</a>
		
		<!-- Delete Char Rename operaton parameter pop up code start here -->
			<div id="divCharRenameDeletemPopup" style="width:100%; display: none;" >
		    <div class="modal-content">
		    	<input type="hidden" id="deleteCharRenameBlockId"/>
		    	<input type="hidden" id="deleteCharRenameId"/>
		    	
		        <div class="modal-header padding10">
		            <h4 class="modal-title"><spring:message code="fileRename.agent.char.rename.delete.label"></spring:message></h4>
		        </div>
		        <div class="modal-body padding10 inline-form">
					<jsp:include page="../common/responseMsgPopUp.jsp" ></jsp:include>
		        	<div>
		        		<spring:message code="delete.characterRename.warn.msg"></spring:message>
		        	</div>
		        </div>
		        <div id="delete_buttons-div" class="modal-footer padding10">
		         	<button type="button" id="btndelete" class="btn btn-grey btn-xs " data-dismiss="modal" onclick="deleteCharRenameOperationParameters();"><spring:message code="btn.label.delete"></spring:message></button>
		         	<button type="button" class="btn btn-grey btn-xs " data-dismiss="modal" onclick="closeFancyBox();"><spring:message code="btn.label.close"></spring:message></button>
		        </div>
		        <div id="delete_progress-bar-div" class="modal-footer padding10 text-left" style="display: none;">
						<jsp:include page="../common/processing-bar.jsp"></jsp:include>
				</div>	
		        
		    </div>
		    <!-- /.modal-content --> 
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
		            <button type="button" class="btn btn-grey btn-xs " data-dismiss="modal" onclick="closeFancyBox();"><spring:message code="btn.label.close"></spring:message></button>
		        </div>
		    </div>
		    <!-- /.modal-content --> 
		</div>		
		
			<!-- warning message pop-up -->
			
		   	<a href="#divServiceConfigMessage" class="fancybox" style="display: none;" id="serviceConfigMessage">#</a>
		   	<div id="divServiceConfigMessage" style=" width:100%; display: none;" >
			    <div class="modal-content">
			    <input type="hidden" id="deleteBlockId"/>
			    
			        <div class="modal-header padding10">
			            <h4 class="modal-title">Warning</h4>
			        </div>
			        <div class="modal-body padding10 inline-form">
				        <p id="moreWarn">
				        	<spring:message code="file.rename.agent.service.all.configured"></spring:message>    
				        </p>
			        </div>
			        <div class="modal-footer padding10">
			            <button type="button" class="btn btn-grey btn-xs " data-dismiss="modal" onclick="closeFancyBox();"><spring:message code="btn.label.close"></spring:message></button>
			        </div>
			    </div>
			    <!-- /.modal-content --> 
			</div>	
			
		   	<a href="#divviewCharRenameMessagePopUp" class="fancybox" style="display: none;" id="viewCharRenameMessagePopUp">#</a>
		   	<div id="divviewCharRenameMessagePopUp" style=" width:100%; display: none;" >
			    <div class="modal-content">
			    
			        <div class="modal-header padding10">
			            <h4 class="modal-title">Warning</h4>
			        </div>
			        <div class="modal-body padding10 inline-form">
				        <p id="moreWarn">
				        	<spring:message code="file.rename.agent.char.rename.disable"></spring:message>    
				        </p>
			        </div>
			        <div class="modal-footer padding10">
			            <button type="button" class="btn btn-grey btn-xs " data-dismiss="modal" onclick="closeFancyBox();"><spring:message code="btn.label.close"></spring:message></button>
			        </div>
			    </div>
			    <!-- /.modal-content --> 
			</div>
								
	<script src="${pageContext.request.contextPath}/customJS/fileRenameAgent.js<%= "?v=" + Math.random() %>" type="text/javascript"></script> 		
<script type="text/javascript">

var charRenameOperationCounter = 0;

$(document).ready(function() {
	var serverInstanceId = $("#server_Instance_Id").val();	
	getFileRenameAgentServiceList(serverInstanceId);

}); 


function getFileRenameAgentServiceList(serverInstanceId){
	
	$("#packetStatasticAgentDiv").jqGrid({
		
    	url		: '<%=ControllerConstants.GET_SERVICE_LIST_FOR_FILERENAME_AGENT%>',
		mtype:"POST",
		postData : {
			serverInstanceId : serverInstanceId,
		},
        datatype: "json",
        colNames:[
				"<spring:message code='packetStata.agent.grid.column.svc.id' ></spring:message>",
				"",
				"",
				"",
				"",
				"<spring:message code='packetStata.agent.grid.column.svc.name' ></spring:message>",		
				"",
				"<spring:message code='fileRename.agent.grid.column.charRename' ></spring:message>",
				"<spring:message code='fileRename.agent.grid.column.edit' ></spring:message>",
                "<spring:message code="updtInstance.summary.service.delete.link" ></spring:message>"				
				],
		colModel: [
				{name:'serviceFileRenameConfigId',index:'serviceFileRenameConfigId',sortable:false,hidden:true},
				{name:'extAfterRename',index:'extAfterRename',sortable:false,hidden:true},
				{name:'destinationPath',index:'destinationPath',sortable:false,hidden:true},
				{name:'fileExtensitonList',index:'fileExtensitonList',sortable:false,hidden:true},				
				{name:'serviceId',index:'serviceId',sortable:false, align:'center',hidden:true},
				{name:'serviceName',index:'serviceName',sortable:false, align:'center'},
				{name:'charRenameOperationEnable',index:'charRenameOperationEnable',sortable:false, align:'center',hidden:true},
				{name:'charRename',index:'charRename',sortable:false,align:'center', formatter: charRenameFormatter},
				{name:'edit',index:'edit',sortable:false,align:'center', formatter: editServiceConfigFormatter},
            	{name:'delete',index:'delete',sortable:false, align:'center',formatter: deleteImageFormatter}	 				
				],
                		
        	rowNum:<%= MapCache.getConfigValueAsInteger(SystemParametersConstant.TOTAL_ROWS_TO_DISPLAY_IN_GRID, 10) %>,
        	rowList:[10,20,60,100],
        	height: 'auto',
        	mtype:'POST',
			sortname: 'id',
			sortorder: "asc",
			pager: "#packetStatasticAgentgridPagingDiv",
			contentType: "application/json; charset=utf-8",
			viewrecords: true,
			multiselect: false,
			timeout : 120000,
	    	loadtext: "Loading...",
        	caption: "",
        	beforeRequest:function(){
        	    $(".ui-dialog-titlebar").hide();
        	}, 
        	loadComplete: function(data) {

 				$(".ui-dialog-titlebar").show();
     			var $jqgrid = $("#packetStatasticAgentDiv");

        	},
        	beforeSend: function(xhr) {
           		xhr.setRequestHeader("Accept", "application/json");
            	xhr.setRequestHeader("Content-Type", "application/json");
        	},
			onPaging: function (pgButton) {
				clearResponseMsgDiv();
			},
        	loadError : function(xhr,st,err) {
				handleGenericError(xhr,st,err);
			},
        	recordtext: "<spring:message code="serverManagement.grid.pager.total.records.text"></spring:message>",
	    	emptyrecords: "<spring:message code="snmpServerList.grid.empty.records"></spring:message>",
			loadtext: "<spring:message code="snmpServerList.grid.loading.text"></spring:message>",
		
			}).navGrid("#packetStatasticAgentgridPagingDiv",{edit:false,add:false,del:false,search:false});
				$(".ui-jqgrid-titlebar").hide();
				$(".ui-pg-input").height("10px");
	} 

	function deleteImageFormatter(cellvalue, options, rowObject){		
		var uniqueId = getUniqueIdForFileRenameAgent("deleteService", rowObject);
		return "<a href='#' id='"+uniqueId+"' class='link' onclick=deletePopup('"+rowObject["serviceFileRenameConfigId"]+"'); ><i class='fa fa-trash' aria-hidden='true'></i></a>";

	}
	
	function charRenameFormatter(cellvalue, options, rowObject){
		var uniqueId = getUniqueIdForFileRenameAgent("characterRenameOperation", rowObject);
		var status_char = rowObject["charRenameOperationEnable"];
		if(status_char == true){
			return "<a href='#' id='"+uniqueId+"' class='link' onclick=viewCharRenameOperationByServiceConfigId('"+rowObject["serviceFileRenameConfigId"]+"'); ><i class='fa fa-list' aria-hidden='true'></i></a>";		
		}
		else{
			return "<a href='#' id='"+uniqueId+"' class='link' onclick=viewCharRenameMessagePopUp(); ><i class='fa fa-list' aria-hidden='true'></i></a>"
		}
	}
	
	function editServiceConfigFormatter(cellvalue, options, rowObject){
		var uniqueId = getUniqueIdForFileRenameAgent("editServiceConfiguration", rowObject);
		var serviceFileRenameConfigId = rowObject["serviceFileRenameConfigId"];
		$("#serviceFileRenameConfigId").val(serviceFileRenameConfigId);
				
		var destinationPath = rowObject["destinationPath"];
		destinationPath = encodeURIComponent(destinationPath);
		
		return '<a href="#" id="'+uniqueId+'" class="link" onclick="updateServiceConfigurationPopup('+"'"+rowObject["serviceFileRenameConfigId"]+"'"+','+"'"+rowObject["serviceId"]+"'"+','+"'"+rowObject["serviceName"]+"'"+','+"'"+rowObject["extAfterRename"]+"'"+','+"'"+destinationPath+"'"+','+"'"+rowObject["fileExtensitonList"]+"'"+' ,'+"'"+rowObject["charRenameOperationEnable"]+"'"+')"><i class="fa fa-pencil-square-o" ></i></a>';
				
	}	
	
	function getUniqueIdForFileRenameAgent(prefix, rowObject) {
		var uniqueId = prefix;
		if(rowObject["serviceName"] !== "") {
			uniqueId += "_" + rowObject["serviceName"];
		}
		return uniqueId;
	}


function addCharRenameOperation(id,seqNumber,position,startIndex,endIndex,padding,defaultValue,length,actionType){
	charRenameOperationCounter++
	
	var serviceFileRenameConfigId = $("#serviceFileRenameConfigId").val();
	
	 if(String(defaultValue) === 'null' || String(defaultValue) === 'undefined'){
		 defaultValue = '';
	 }
	
	var charHtmlBody =  "<form method='POST' id='" + charRenameOperationCounter + "_char_form'> "+
						"<div class='box box-warning' id='flipbox_char_"+charRenameOperationCounter+"'>  "+
					    "<input type='hidden' id='"+charRenameOperationCounter+"_char_rename_id'  value='"+id+"'> "+
						"<div class='box-header with-border'> "+
						"	<h3 class='box-title' id='title_"+charRenameOperationCounter+"'>"+
						"       	<spring:message code='distributionService.pathlist.char.rename.sub.header'></spring:message>"+
						"	</h3>  "+
						"	<div class='box-tools pull-right' style='top: -3px;' id='action_"+charRenameOperationCounter+"'> "+ 
						"		<button class='btn btn-box-tool block-collapse-btn' data-toggle='collapse' id='"+seqNumber+"_collapseBtn' href='#"+charRenameOperationCounter+"_char_operation_block'> "+ 
						"			<i class='fa fa-minus'></i> "+
						"		</button> "+
						"		<sec:authorize access='hasAuthority(\'DELETE_SERVICE_FROM_FILE_RENAME_AGENT\')'>"+
					    "					&nbsp;&nbsp;<a class='ion-ios-close-empty remove-block' style='margin-top:12px;' id='"+seqNumber+"_deleteBtn' onclick=displayDeleteCharRenamePopup(\'"+charRenameOperationCounter+"\');></a>&nbsp;" +
					    "		</sec:authorize>"+
						"	</div> "+
						"</div> "+
						"<div class='box-body inline-form accordion-body collapsed in' id='"+charRenameOperationCounter+"_char_operation_block'> "+ 
						"	<div class='fullwidth inline-form'>  "+
						"		 <div class='col-md-6 no-padding'>"+
					    "       	<spring:message code='distributionService.pathlist.plugin.sequence.label.tooltip' var='tooltip'></spring:message>"+
					    "       	<spring:message code='distributionService.pathlist.plugin.sequence.label' var='label'></spring:message>"+
					    "        	<div class='form-group'>"+
					    "    			<div class='table-cell-label'>${label}<span class='required'>*</span></div>"+
					    "         		<div class='input-group'>"+
					    "         			<input  id='" + charRenameOperationCounter + "_sequenceNo' class='form-control table-cell input-sm' tabindex='15' data-toggle='tooltip' data-placement='bottom' title='${tooltip}' />"+
					    "         			<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='bottom' title=''></i></span>"+
					    "         		</div>"+
					    "        	</div>"+
					    "        </div>"+
						"		 <div class='col-md-6 no-padding'> "+
						"			<spring:message code='distributionService.pathlist.plugin.position.label' var='label'></spring:message> "+
						"			<spring:message code='distributionService.pathlist.plugin.position.label.tooltip' var='tooltip'></spring:message> "+
						"			<div class='form-group'>  "+
						"				<div class='table-cell-label'>${label}</div> "+ 
						"				<div class='input-group '> "+
						"					<select class='form-control table-cell input-sm' id='" + charRenameOperationCounter + "_position' tabindex='16' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }'>"+
					    "				    	<c:forEach var='positionEnum' items='${positionEnum}' >"+
						"							<option value='${positionEnum.value}'>${positionEnum}</option>"+
						"				    	</c:forEach>"+
						"					</select>"+
						"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
						"				</div> "+
						"			</div> "+
						"		 </div> "+
					    "		 <div class='col-md-6 no-padding'>"+
						"				<div class='col-md-5 no-padding'>"+
						"       			<spring:message code='distributionService.pathlist.plugin.end.index.label.tooltip' var='maxIndex'></spring:message>"+
						"       			<spring:message code='distributionService.pathlist.plugin.start.index.label.tooltip' var='minIndex'></spring:message>"+
						"       			<spring:message code='distributionService.pathlist.plugin.index.label' var='label'></spring:message>"+
					    "    			<div class='table-cell-label'>${label}</div>"+						
						"				</div>"+
						"				<div class='col-md-3 no-padding'>"+
						"					<div class='form-group'>"+
						"                         	<div class='input-group'>"+
						"                         		<input tabindex='17' id='" + charRenameOperationCounter + "_startIndex' class='form-control table-cell input-sm'  data-toggle='tooltip' data-placement='bottom' title='${minIndex}' value='"+startIndex+"' onkeydown='isNumericOnKeyDown(event)' />"+
					    "         						<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='bottom' title=''></i></span>"+
						"                         	</div>"+
						"                        </div> "+	
						"				</div>"+
						"				<div class='col-md-3 no-padding'>"+
						"					<div class='form-group'>"+
						"                        	<div class='input-group'>"+ 
						"                         		<input tabindex='18' id='" + charRenameOperationCounter + "_endIndex' class='form-control table-cell input-sm'  data-toggle='tooltip' data-placement='bottom' title='${maxIndex}' value='"+endIndex+"'  onkeydown='isNumericOnKeyDown(event)'/>"+
					    "         						<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='bottom' title=''></i></span>"+
						"                         	</div>"+
						"                        </div> "+	
						"				</div>"+
					    " 		</div>"+						
					    "		 <div class='col-md-6 no-padding'> "+
						"			<spring:message code='distributionService.pathlist.plugin.padding.label' var='label'></spring:message> "+
						"			<spring:message code='distributionService.pathlist.plugin.padding.label.tooltip' var='tooltip'></spring:message> "+
						"			<div class='form-group'>  "+
						"				<div class='table-cell-label'>${label}</div> "+ 
						"				<div class='input-group '> "+
						"					<select class='form-control table-cell input-sm' id='" + charRenameOperationCounter + "_paddingType' tabindex='19' data-toggle='tooltip' data-placement='bottom'  title='${tooltip }' >"+
					    "				    	<c:forEach var='positionEnum' items='${positionEnum}' >"+
						"							<option value='${positionEnum.value}'>${positionEnum}</option>"+
						"				    	</c:forEach>"+
						"					</select>"+
						"					<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='left' title='' ></i></span>"+
						"				</div> "+
						"			</div> "+
						"		 </div> "+
						"		 <div class='col-md-6 no-padding'>"+
					    "       	<spring:message code='distributionService.pathlist.plugin.default.label.tooltip' var='tooltip'></spring:message>"+
					    "       	<spring:message code='distributionService.pathlist.plugin.default.label' var='label'></spring:message>"+
					    "        	<div class='form-group'>"+
					    "    			<div class='table-cell-label'>${label}</div>"+
					    "         		<div class='input-group'>"+
					    "         			<input  id='" + charRenameOperationCounter + "_defaultValue' class='form-control table-cell input-sm' tabindex='20'  data-toggle='tooltip' data-placement='bottom' title='${tooltip}' value='"+defaultValue+"' />"+
					    "         			<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='bottom' title=''></i></span>"+
					    "         		</div>"+
					    "        	</div>"+
					    "        </div>"+
					    "		 <div class='col-md-6 no-padding'>"+
					    "       	<spring:message code='distributionService.pathlist.plugin.length.label.tooltip' var='tooltip'></spring:message>"+
					    "       	<spring:message code='distributionService.pathlist.plugin.length.label' var='label'></spring:message>"+
					    "        	<div class='form-group'>"+
					    "    			<div class='table-cell-label'>${label}</div>"+		
					    "         		<div class='input-group'>"+
					    "         			<input  id='" + charRenameOperationCounter + "_length' class='form-control table-cell input-sm' tabindex='21'  data-toggle='tooltip' data-placement='bottom' title='${tooltip}' value='"+length+"' onkeydown='isNumericOnKeyDown(event)' />"+
					    "         			<span class='input-group-addon add-on last' > <i class='glyphicon glyphicon-alert' data-toggle='tooltip' data-placement='bottom' title=''></i></span>"+
					    "         		</div>"+
					    "        	</div>"+
					    "        </div>"+
					    "		<div class='col-md-12 no-padding'>"+
					    "  			<div class='form-group'>"+
					    "      			<div id='" + charRenameOperationCounter + "_char_buttons-div' class='input-group '>"+
						"						<sec:authorize access='hasAuthority(\'ADD_SERVICE_TO_FILE_RENAME_AGENT\')'>"+	
			    		"								<button type='button' class='btn btn-grey btn-xs ' id='" + charRenameOperationCounter + "_char_savebtn' onclick=addCharRenamOperationParams(\'"+charRenameOperationCounter+"\');><spring:message code='btn.label.save'></spring:message></button>&nbsp;" +
			    		"						</sec:authorize>"+
						"						<sec:authorize access='hasAuthority(\'UPDATE_SERVICE_TO_FILE_RENAME_AGENT\')'>"+
						"								<button type='button' class='btn btn-grey btn-xs ' id='" + charRenameOperationCounter + "_char_updatebtn' onclick=updateCharRenamOperationParams(\'"+charRenameOperationCounter+"\');><spring:message code='btn.label.update'></spring:message></button>&nbsp;" +
						"						</sec:authorize>"+	
					   
			     		"				<button type='button' class='btn btn-grey btn-xs ' id='" + charRenameOperationCounter + "_char_resetbtn' onclick=resetCharRenameParameters(\'"+charRenameOperationCounter+"\');><spring:message code='btn.label.reset'></spring:message></button>&nbsp;" +
					    "       		</div>"+
					    "					<div id='" + charRenameOperationCounter + "_char_progress-bar-div' class='input-group' style='display: none;'> "+
					    "						<label>Processing Request &nbsp;&nbsp; </label> "+
					    "							<img src='img/processing-bar.gif'>"+
						"					</div> "+
					    "   		</div>" +
					    "		</div>" +
					    

						"	</div> "+
						"</div> "+
						"</div> "+
						"</form>";
						
		$('#plugin_char_rename_details').append(charHtmlBody);		

		if(actionType == 'UPDATE'){
			$('#' + charRenameOperationCounter + '_position').val(position.toString());
			$('#' + charRenameOperationCounter + '_paddingType').val(padding.toString());
			$("#"+charRenameOperationCounter+"_char_savebtn").hide();
			$("#"+charRenameOperationCounter+"_char_updatebtn").show();
			$("#"+charRenameOperationCounter+"_char_operation_block").collapse("toggle");
			$("#"+charRenameOperationCounter+"_sequenceNo").val(seqNumber);
		}else{
			$("#"+charRenameOperationCounter+"_char_updatebtn").hide();
			$("#"+charRenameOperationCounter+"_char_savebtn").show();
		}
}

function resetCharRenameParameters(counter) {

	$("#" + counter + "_defaultValue").val('');
	$("#" + counter + "_startIndex").val('');
	$("#" + counter + "_endIndex").val('');
	$("#" + counter + "_length").val('');
}



/*
 * Function will add char rename operation to database with its required
 * validations.
 */
function addCharRenamOperationParams(counter) {	

	clearAllMessages();
	resetWarningDisplay();
	clearAllMessagesPopUp();
	resetWarningDisplayPopUp();
	
	$('#' + counter + '_char_buttons-div').hide();
	$('#' + counter + '_char_progress-bar-div').show();
	$('#' + counter + '_char_rename_id').val('0');

	var padingLength = $('#' + counter + '_length').val();
	if (padingLength === '' || padingLength === null) {
		padingLength = 0;
	}

	var startIndex = $('#' + counter + '_startIndex').val();
	if (startIndex === '' || startIndex === null) {
		startIndex = 0;
	}

	var endIndex = $('#' + counter + '_endIndex').val();
	if (endIndex === '' || endIndex === null) {
		endIndex = 0;
	}

	$.ajax({
				url : '<%=ControllerConstants.CREATE_CHAR_RENAME_PARAMS_FILE_RENAME_AGENT%>',
				cache : false,
				async : true,
				dataType : 'json',
				type : "POST",
				data : {
					"svcFileRenConfig.id" : $("#serviceFileRenameConfigId").val(),
					"id" 				  : $('#' + counter + '_char_rename_id').val(),
					"sequenceNo" : $('#' + counter + '_sequenceNo').val(),
					"position" : $('#' + counter + '_position').val(),
					"startIndex" : startIndex,
					"endIndex" : endIndex,
					"paddingType" : $('#' + counter + '_paddingType').val(),
					"defaultValue" : $('#' + counter + '_defaultValue').val(),
					"length" : padingLength,
					"blockCount" : counter

				},

				success : function(data) {
					$('#' + counter + '_char_buttons-div').show();
					$('#' + counter + '_char_progress-bar-div').hide();

					var response = data;

					var responseCode = response.code;
					var responseMsg = response.msg;
					var responseObject = response.object;

					if (responseCode === "200") {
						clearAllMessagesPopUp();
						resetWarningDisplayPopUp();
						showSuccessMsgPopUp(responseMsg);

						$("#" + counter + "_char_updatebtn").show();
						$("#" + counter + "_char_savebtn").hide();

						$('#' + counter + '_char_rename_id').val(
								responseObject.id);
						$("#" + counter + "_char_operation_block").collapse("toggle");
						$('#' + counter + '_sequenceNo').val(responseObject.sequenceNo);
						$('#' + counter + '_position').val(responseObject.position.toString());
						$('#' + counter + '_length').val(responseObject.length);
						$('#' + counter + '_defaultValue').val(responseObject.defaultValue);
						$('#' + counter + '_paddingType').val(responseObject.paddingType.toString());
						$('#' + counter + '_startIndex').val(responseObject.startIndex);
						$('#' + counter + '_endIndex').val(responseObject.endIndex);						

					} else if (responseObject !== undefined && responseObject !== 'undefined' && responseCode === "400") {
						clearAllMessagesPopUp();
						resetWarningDisplayPopUp();
						$('#' + counter + '_char_buttons-div').show();
						$('#' + counter + '_char_progress-bar-div').hide();
						addErrorIconAndMsgForAjaxPopUp(responseObject);
						
					} else {
						$('#' + counter + '_char_buttons-div').show();
						$('#' + counter + '_char_progress-bar-div').hide();
						clearAllMessagesPopUp();
						resetWarningDisplayPopUp();
						showErrorMsgPopUp(responseMsg);
					}
				},
				error : function(xhr, st, err) {
					$('#' + counter + '_char_buttons-div').show();
					$('#' + counter + '_char_progress-bar-div').hide();
					handleGenericError(xhr, st, err);
				}
			});
}

/*
 * Function will update character rename operation parameters for selected service
 * 
 */
function updateCharRenamOperationParams(counter) {
 	resetWarningDisplay();
	clearAllMessages(); 
	
	$('#' + counter + '_char_buttons-div').hide();
	$('#' + counter + '_char_progress-bar-div').show();
	
	var padingLength = $('#' + counter + '_length').val();
	if (padingLength === '' || padingLength === null) {
		padingLength = 0;
	}

	var startIndex = $('#' + counter + '_startIndex').val();
	if (startIndex === '' || startIndex === null) {
		startIndex = 0;
	}

	var endIndex = $('#' + counter + '_endIndex').val();
	if (endIndex === '' || endIndex === null) {
		endIndex = 0;
	}

	$.ajax({
				url : '<%=ControllerConstants.UPDATE_CHAR_RENAME_PARAMS_FILE_RENAME_AGENT%>',
				cache : false,
				async : true,
				dataType : 'json',
				type : "POST",
				data : {
					"svcFileRenConfig.id" : $("#serviceFileRenameConfigId").val(),
					"id" 				  : $('#' + counter + '_char_rename_id').val(),
					"sequenceNo" : $('#' + counter + '_sequenceNo').val(),
					"position" : $('#' + counter + '_position').val(),
					"startIndex" : startIndex,
					"endIndex" : endIndex,
					"paddingType" : $('#' + counter + '_paddingType').val(),
					"defaultValue" : $('#' + counter + '_defaultValue').val(),
					"length" : padingLength,
					"blockCount" : counter
				},

				success : function(data) {
					$('#' + counter + '_char_buttons-div').show();
					$('#' + counter + '_char_progress-bar-div').hide();

					var response = data;

					var responseCode = response.code;
					var responseMsg = response.msg;
					var responseObject = response.object;

					if (responseCode === "200") {
						resetWarningDisplay();
						clearAllMessages();
						showSuccessMsgPopUp(responseMsg);

						$("#" + counter + "_char_updatebtn").show();
						$("#" + counter + "_char_savebtn").hide();

						$("#" + counter + "_char_operation_block").collapse(
								"toggle");
						$('#' + counter + '_sequenceNo').val(
								responseObject.sequenceNo);
						$('#' + counter + '_position').val(
								responseObject.position + "");
						$('#' + counter + '_length').val(responseObject.length);
						$('#' + counter + '_defaultValue').val(
								responseObject.defaultValue);
						$('#' + counter + '_paddingType').val(
								responseObject.paddingType + "");
						$('#' + counter + '_startIndex').val(
								responseObject.startIndex);
						$('#' + counter + '_endIndex').val(
								responseObject.endIndex);
						
					} else if (responseObject !== undefined
							&& responseObject !== 'undefined'
							&& responseCode === "400") {
						$('#' + counter + '_char_buttons-div').show();
						$('#' + counter + '_char_progress-bar-div').hide();
						addErrorIconAndMsgForAjaxPopUp(responseObject);
						
					} else {
						$('#' + counter + '_char_buttons-div').show();
						$('#' + counter + '_char_progress-bar-div').hide();
						resetWarningDisplay();
						clearAllMessages();
						showErrorMsgPopUp(responseMsg);
					}
				},
				error : function(xhr, st, err) {
					$('#' + counter + '_char_buttons-div').show();
					$('#' + counter + '_char_progress-bar-div').hide();
					handleGenericError(xhr, st, err);
				}
			});
	}

/* Function will get char rename parameters for selected serviceFileConfigId. */
function getCharRenameParams(serviceFileRenameConfigId) {
	$.ajax({
		url : 'getAllRenameOperationsBySvcFileRenConfigId',
		cache : false,
		async : true,
		dataType : 'json',
		type : 'POST',
		data : {
			"id" : serviceFileRenameConfigId,
		},
		success : function(data) {
			var response = data;
			var responseCode = response.code;
			var responseObject = response.object;

			$("#charRename_div").html('');

			if (responseCode === "200") {
				if (responseObject !== 'undefined' && responseObject !== null) {
					$.each(responseObject, function(index, char) {
						addCharRenameOperation(char.id, char.sequenceNo,
								char.position, char.startIndex,
								char.endIndex, char.paddingType,
								char.defaultValue, char.length, 'UPDATE');
					});
				}
			} 
		},
		error : function(xhr, st, err) {
			handleGenericError(xhr, st, err);
		}

	});
}

/*Function will display character rename operation parameters details. */
function viewCharRenameOperationByServiceConfigId(serviceFileRenameConfigId) {

	clearAllMessagesPopUp();
	resetWarningDisplay();
	
	$("#serviceFileRenameConfigId").val(serviceFileRenameConfigId);
	
	$.ajax({
		url : 'getAllRenameOperationsBySvcFileRenConfigId',
		cache : false,
		async : true,
		dataType : 'json',
		type : 'POST',
		data : {
			"id" : serviceFileRenameConfigId,
		},
		success : function(data) {
			var response = data;
			var responseCode = response.code;
			var responseObject = response.object;

			$('#plugin_char_rename_details').html('');

			if (responseCode === "200") {

				if (responseObject !== 'undefined' && responseObject !== null) {
					$.each(responseObject, function(index, responseObject) {
						addCharRenameOperation(responseObject["id"],
								responseObject["sequenceNo"],
								responseObject["position"],
								responseObject["startIndex"],
								responseObject["endIndex"],
								responseObject["paddingType"],
								responseObject["defaultValue"],
								responseObject["length"], 'UPDATE');
					});
				}
			} 

			$("#char_rename_param_link").click();
			$("#divCharRenameParams").show();
		},
		error : function(xhr, st, err) {
			handleGenericError(xhr, st, err);
		}
	});
}

function reloadFileRenameAgentGridData(){
    var $grid = $("#packetStatasticAgentDiv");
    $grid.jqGrid('setGridParam',{datatype:'json',page: 1}).trigger('reloadGrid');
}

/* Function will delete character rename block */
var charRenameBlockCounter, charRenameId;
function displayDeleteCharRenamePopup(counter) {
	
	charRenameId = $('#' + counter + '_char_rename_id').val();
	
	charRenameBlockCounter = counter;
	clearAllMessagesPopUp();
	resetWarningDisplay();

	if (charRenameId === null || charRenameId === 'null' || charRenameId === '') {
		$("#" + counter + "_char_form").remove();
	} else {
		$("#delete_char_rename_param_link").click();
	}
}

/* Function will delete char rename operation parameters. */
function deleteCharRenameOperationParameters() {

	clearAllMessagesPopUp();
	resetWarningDisplayPopUp();

	$.ajax({
				url : '<%=ControllerConstants.DELETE_CHAR_RENAME_PARAMS_AGENT%>',
				cache : false,
				async : true,
				dataType : 'json',
				type : "POST",
				data : {
					"id" : charRenameId
				},

				success : function(data) {
					var response = data;
					var responseCode = response.code;
					var responseMsg = response.msg;
					var responseObject = response.object;
					if (responseCode === "200") {
						$("#" + charRenameBlockCounter + "_char_form").remove();
						resetWarningDisplay();
						clearAllMessages();
						showSuccessMsg(responseMsg);
						closeFancyBox();
					} else if (responseObject !== undefined
							&& responseObject !== 'undefined'
							&& responseCode === "400") {
						showErrorMsgPopUp(responseMsg);
					} else {
						showErrorMsgPopUp(responseMsg);
					}
				},
				error : function(xhr, st, err) {
					handleGenericError(xhr, st, err);
				}
			});
	 }
	 
 function viewCharRenameMessagePopUp(){
		$("#viewCharRenameMessagePopUp").click();
		$("#moreWarn").show();
 }
</script>
