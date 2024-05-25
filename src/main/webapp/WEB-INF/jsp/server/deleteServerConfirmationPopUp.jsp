<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<%@taglib 	uri="http://www.springframework.org/tags" 			prefix="spring"%>
<a style="display: none;"  class="fancybox fancybox.inline black" id="deleteServerAnchor"  href="#deleteServerConfirmation">Delete Confirmation</a>
<div id="deleteServerConfirmation" style="width: 100%; display: none;">
	<div class="modal-content">
		<div class="modal-header padding10">
			<h4 class="modal-title"><spring:message code="delete.server.popup.header"></spring:message></h4>
		</div>
		<div class="modal-body padding10 inline-form">
		<input type="hidden" id="deleteServerId" value="" />
		<jsp:include page="../common/responseMsgPopUp.jsp" ></jsp:include>
		<p>
			        	<table style='width:100%'>
			        		<tr>
			        			<td width="15%">
			        				<img alt="Server Instance" src="img/server.png" class="img-responsive" />
			        			</td>
			        			<td width="85%">
			        				<div id="tblDeleteServerList">
									</div>
			        			</td>
			        		</tr>
			        	</table>
			        </p>
		<p id="delete-server-popup-content">	           	
			<span class="note"><spring:message code="label.important.note"></spring:message></span><br/><br/>
			<spring:message code="delete.server.popup.content"></spring:message>
		</p>
			
		</div>
		<div class="modal-footer padding10" id="delete-server-button">
			<button type="button" class="btn btn-grey btn-xs " id="deleteServerBtn" onclick="deleteServer()"><spring:message code="btn.label.delete"></spring:message></button>
			<button type="button" onclick="$.fancybox.close();" id="cancelServerBtn" class="btn btn-grey btn-xs "><spring:message code="btn.label.cancel"></spring:message></button>
			<button type="button" onclick="refreshCreateServerPage()" style="display:none" id="closeServerBtnSuccess" class="btn btn-grey btn-xs "><spring:message code="btn.label.close"></spring:message></button>
			<button type="button" onclick="$.fancybox.close();" style="display:none" id="closeServerBtn" class="btn btn-grey btn-xs "><spring:message code="btn.label.close"></spring:message></button>

		</div>
		<div id="delete-server-progress-bar-div" class="modal-footer padding10 text-left" style="display: none;">
					<jsp:include page="../common/processing-bar.jsp"></jsp:include>
		</div>
	</div>
	<!-- /.modal-content -->
</div>
<script type="text/javascript">
function deleteServer(){
	clearAllMessagesPopUp();
	clearAllMessages();
	$("#delete-server-progress-bar-div").show();
	$("#delete-server-button").hide();
	
	 $.ajax({
			url: '<%=ControllerConstants.DELETE_SERVER %>',
			    cache: false,
				async: true,
				dataType: 'json',
				type: "POST",
				data: {
					serverId: $("#deleteServerId").val()
				},
				
				success: function(data){
						$("#delete-server-button").show();
						$("#deleteServerBtn").hide();
						$("#cancelServerBtn").hide();
						$("#closeServerBtnSuccess").show();
						$("#closeServerBtn").hide();
						$("#delete-server-progress-bar-div").hide();
						
						var response = eval(data);
				    	response.msg = decodeMessage(response.msg);
				    	response.msg = replaceAll("+"," ",response.msg);
				    	
				    	if(response.code === 200 || response.code === "200") {
				    		clearAllMessagesPopUp();
				    		showSuccessMsgPopUp(response.msg);
				    		$("#popUpContent").hide();
				    		$("#delete-server-popup-content").hide();
		    			
				    	}else{
				    		$("#deleteServerBtn").hide();
							$("#cancelServerBtn").hide();
							$("#closeServerBtn").show();
							$("#closeServerBtnSuccess").hide();
				    		showErrorMsgPopUp(response.msg);
				    		
			    			clearSelection();
				    	}
					},
				    error: function (xhr,st,err){
				    	$("#delete-server-button").show();
						$("#delete-server-progress-bar-div").hide();
				    	handleGenericError(xhr,st,err);
					}
			
					});
	
}
function refreshCreateServerPage(){
	$("#create_server").click();
}
</script>
