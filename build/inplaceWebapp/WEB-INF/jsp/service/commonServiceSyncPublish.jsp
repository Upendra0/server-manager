<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<jsp:include page="../server/divSyncPublish.jsp"></jsp:include>
<a href="#divSyncPublish" class="fancybox" style="display: none;" id="syncPublish">#</a>
<%-- <div style="display:none";><input type="hidden" name="serverInstanceId"  id="serverInstanceId" value="${instanceId}"/></div> --%>
<script>
function syncPublishPopup(){

	clearAllMessages();

	$('#sync-publish-buttons-div #btn-no').attr('onclick','closeFancyBox();');
		
	$("#syncPublish").click();	
		
} 

function syncPublishInstance(){
	$("#sync-publish-buttons-div").hide();
	$("#sync-publish-progress-bar-div").show();
	$("#buttons-div").hide();
	$("#progress-bar-div").show();
	clearAllMessages();
	$.ajax({
			url: '<%= ControllerConstants.SYNC_PUBLISH_SERVICE_INSTANCE %>',
			    cache: false,
				async: true,
				dataType: 'json',
				type: "POST",
				data: {
					id: $('#serviceId').val(),
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

</script> 
