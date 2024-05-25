<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib 	uri="http://www.springframework.org/tags" prefix="spring"%>
<!DOCTYPE html>
<html>
<script>
	var ckSnmpServerSelected = new Array();
	var ckSnmpClientSelected = new Array();
	var ckSnmpClientAlertSelected = new Array();
</script>
<div class="tab-pane <c:if test="${(REQUEST_ACTION_TYPE eq 'UPDATE_SNMP_CONFIG')}"><c:out value="active"></c:out></c:if>" id="SnmpConfig">
		<div class="tab-content no-padding padding0">
		<jsp:include page="snmpServerList.jsp"></jsp:include>
		<jsp:include page="snmpClientList.jsp"></jsp:include>
		<jsp:include page="snmpAlertList.jsp"></jsp:include>
	</div>
		<a href="#divDeleteSnmpServer" class="fancybox" style="display: none;" id="deleteSnmpServer">#</a>
		<div id="divDeleteSnmpServer" style=" width:100%; display: none;" >
		    <div class="modal-content">
		        <div class="modal-header padding10">
		            <h4 class="modal-title"><spring:message code="snmp.Server.delete.header.label"></spring:message></h4>
		        </div>
		        <div class="modal-body padding10 inline-form">
		        <input type="hidden" id="deleteSnmpServerId" name="deleteSnmpServerId" />
		        	<span id="deleteDriverResponseMsg" style="display:none;">
		        		<jsp:include page="../common/responseMsgPopUp.jsp" ></jsp:include>
		        	</span>
			        <p id="delete-snmp-server-warning">
			       		 <spring:message code="snmp.server.delete.message" ></spring:message>
			        </p>
			      
		        </div>
<!-- 		        <sec:authorize access="hasAuthority('UPDATE_COLLECTION_DRIVER')"> -->
			        <div id="delete-snmp-server-buttons-div" class="modal-footer padding10">
			            <button id="del_snmp_server_btn" type="button" class="btn btn-grey btn-xs " onclick="deleteSnmpServer();"><spring:message code="btn.label.yes"></spring:message></button>
			            <button id="no_delete_snmp_server_btn" onclick="javascript:parent.closeFancyBoxFromChildIFrame()" class="btn btn-grey btn-xs "><spring:message code="btn.label.no"></spring:message></button>
			        </div>
			        <div id="delete-snmp-server-progress-bar-div" class="modal-footer padding10" style="display: none;">
					<jsp:include page="../common/processing-bar.jsp"></jsp:include>
				</div>
<!-- 		        </sec:authorize> -->
		    </div>
		    <!-- /.modal-content --> 
		</div>
			<a href="#divMessage" class="fancybox" style="display: none;" id="message">#</a>
		<div id="divMessage" style=" width:100%; display: none;" >
		    <div class="modal-content">
		        <div class="modal-header padding10">
		            <h4 class="modal-title"><spring:message code="snmpServer.delete.warn.popup.header"></spring:message></h4>
		        </div>
		        <div class="modal-body padding10 inline-form">
			        <p id="moreWarn">
			        	<spring:message code="snmp.server.more.instance.checked.warning"></spring:message>    
			        </p>
			        <p id="lessWarn">
			        	<spring:message code="snmp.server.no.instance.checked.warning"></spring:message>    
			        </p>
		        </div>
		        <div class="modal-footer padding10">
		            <button id="close_snmp_server_del_btn" type="button" class="btn btn-grey btn-xs " data-dismiss="modal" onclick="closeFancyBox();"><spring:message code="btn.label.close"></spring:message></button>
		        </div>
		    </div>
		    <!-- /.modal-content --> 
		</div>
		
			<a href="#divDeleteSnmpClient" class="fancybox" style="display: none;" id="deleteSnmpClient">#</a>
		<div id="divDeleteSnmpClient" style=" width:100%; display: none;" >
		    <div class="modal-content">
		        <div class="modal-header padding10">
		            <h4 class="modal-title"><spring:message code="snmpClient.delete.warn.popup.header"></spring:message></h4>
		        </div>
		        <div class="modal-body padding10 inline-form">
		        <input type="hidden" id="deleteSnmpClientId" name="deleteSnmpClientId" />
		        	<span id="deleteClientResponseMsg" style="display:none;">
		        		<jsp:include page="../common/responseMsgPopUp.jsp" ></jsp:include>
		        	</span>
			        <p id="delete-snmp-client-warning">
			       		 <spring:message code="snmp.client.delete.message" ></spring:message>
			        </p>
			      
		        </div>
<!-- 		        <sec:authorize access="hasAuthority('UPDATE_COLLECTION_DRIVER')"> -->
			        <div id="delete-snmp-client-buttons-div" class="modal-footer padding10">
			            <button id="delete_yes_snmp_client_btn" type="button" class="btn btn-grey btn-xs " onclick="deleteSnmpClient();"><spring:message code="btn.label.delete"></spring:message></button>
			            <button id="delete_no_snmp_client_btn" onclick="javascript:parent.closeFancyBoxFromChildIFrame()" class="btn btn-grey btn-xs "><spring:message code="btn.label.no"></spring:message></button>
			        </div>
			       <div id="delete-snmp-client-progress-bar-div" class="modal-footer padding10" style="display: none;">
					<jsp:include page="../common/processing-bar.jsp"></jsp:include>
				</div>
<!-- 		        </sec:authorize> -->
		    </div>
		    <!-- /.modal-content --> 
		</div>
		
		<a href="#divClientMessage" class="fancybox" style="display: none;" id="clientMessage">#</a>
		<div id="divClientMessage" style=" width:100%; display: none;" >
		    <div class="modal-content">
		        <div class="modal-header padding10">
		            <h4 class="modal-title"><spring:message code="snmpClient.delete.warn.popup.header"></spring:message></h4>
		        </div>
		        <div class="modal-body padding10 inline-form">
			        <p id="cmoreWarn">
			        	<spring:message code="snmp.client.more.instance.checked.warning"></spring:message>    
			        </p>
			        <p id="clessWarn">
			        	<spring:message code="snmp.client.no.instance.checked.warning"></spring:message>    
			        </p>
		        </div>
		        <div class="modal-footer padding10">
		            <button id="delete_client_close_btn" type="button" class="btn btn-grey btn-xs " data-dismiss="modal" onclick="closeFancyBox();"><spring:message code="btn.label.close"></spring:message></button>
		        </div>
		    </div>
		    <!-- /.modal-content --> 
		</div>
		
</div>
<script>
$(document).ready(function() {
	$('#lblInstanceHost').text('${lblInstanceHost}');
	
	
});

function deleteSnmpServer(){
	resetWarningDisplayPopUp();
	clearAllMessagesPopUp();

	showProgressBar("delete-snmp-server");
	parent.resizeFancyBox();
	$.ajax({
		url: '<%=ControllerConstants.DELETE_SNMP_SERVER%>',
		cache: false,
		async: true, 
		dataType: 'json',
		type: "POST",
		data: 
		{
			"delete_snmpServerId"			:   $("#deleteSnmpServerId").val(),
			
		},
		success: function(data){
			hideProgressBar("delete-snmp-server");
			getSnmpServerList();
			
			var response = eval(data);
			
			var responseCode = response.code; 
			var responseMsg = response.msg; 
			var responseObject = response.object;
			 
			if(responseCode == "200"){
				//parent.getSnmpServerList();
				$("#delete-snmp-server-buttons-div").show();
				ckSnmpServerSelected = new Array();
				showSuccessMsg(responseMsg);
				//reloadGridData()
				closeFancyBox();
				reloadGridData();
				
			}else{
				
				resetWarningDisplayPopUp();
				$("#delete-snmp-server-buttons-div").show();
				showErrorMsgPopUp(responseMsg);
				reloadGridData();
			}
		},
	    error: function (xhr,st,err){
	    	hideProgressBar("delete-snmp-server");
	    	$("#delete-snmp-server-buttons-div").show();
			handleGenericError(xhr,st,err);
		}
	});
}

function deleteSnmpClient(){
	resetWarningDisplayPopUp();
	clearAllMessagesPopUp();

	showProgressBar("delete-snmp-client");
	parent.resizeFancyBox();
	$.ajax({
		url: '<%=ControllerConstants.DELETE_SNMP_CLIENT%>',
		cache: false,
		async: true, 
		dataType: 'json',
		type: "POST",
		data: 
		{
			"delete_snmpClientId"			:   $("#deleteSnmpClientId").val(),
			
		},
		success: function(data){
			hideProgressBar("delete-snmp-client");
			getSnmpServerList();
			
			var response = eval(data);
			
			var responseCode = response.code; 
			var responseMsg = response.msg; 
			var responseObject = response.object;
			 
			if(responseCode == "200"){
				//parent.getSnmpServerList();
				$("#delete-snmp-client-buttons-div").show();
				ckSnmpClientSelected = new Array();
				showSuccessMsg(responseMsg);
				//reloadGridData()
				closeFancyBox();
				reloadClientGridData();
				
			}else{
				
				resetWarningDisplayPopUp();
				$("#delete-snmp-client-buttons-div").show();
				showErrorMsgPopUp(responseMsg);
				reloadClientGridData();
			}
		},
	    error: function (xhr,st,err){
	    	hideProgressBar();
	    	$("#delete-snmp-server-buttons-div").show();
			handleGenericError(xhr,st,err);
		}
	});
}

function showProgressBar(div){
	
	$("#"+div+"-buttons-div").hide();
	$("#"+div+"-progress-bar-div").show();
	
}

function hideProgressBar(div){
	$("#"+div+"-buttons-div").show();
	$("#"+div+"-progress-bar-div").hide();	
}

</script>

