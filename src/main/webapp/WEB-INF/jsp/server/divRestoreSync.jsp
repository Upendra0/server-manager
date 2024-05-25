<%@taglib 	uri="http://www.springframework.org/tags" prefix="spring"%>
<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<div id="divRestoreSync" style=" width:100%; display: none;" >
    <div class="modal-content">
        <div class="modal-header padding10">
            <h4 class="modal-title"><spring:message code="serverMgmt.restore.sync.popup.header"></spring:message></h4>
        </div>
        <div class="modal-body padding10 inline-form">
        <jsp:include page="../common/responseMsgPopUp.jsp" ></jsp:include>
	        <div id="restoreSyncDiv">
		        	<div class="form-group" >
		            	<spring:message code="serverMgmt.sync.publish.popup.description" var="tooltip"></spring:message>
		         		<div class="table-cell-label">${tooltip}</div>
		             	<div class="input-group ">
		             		<input type="text" class="form-control" tabindex="14" id="descRestoreSync" name="descRestoreSync"/>
		             	</div>
		         	</div>
		         	<p id="synchronizePopUpMsg" class="synchronizePopUpMsgClass">           	
			           	<span class="note"><spring:message code="label.important.note"></spring:message></span><br/><br/>
			           	<spring:message code="serverMgmt.sync.publish.popup.note.content"></spring:message>
			 		</p>
        	</div>
        <div id="restore-sync-publish-buttons-div" class="modal-footer padding10">
            <button type="button" class="btn btn-grey btn-xs " id="btn_restoreSync_yes" onclick="restoreSyncServerInstance()"><spring:message code="btn.label.restore.sync"></spring:message></button>
            <button type="button" class="btn btn-grey btn-xs " id="btn_restoreSync_no"  data-dismiss="modal" onclick="resetRestoreDescription();"><spring:message code="btn.label.cancel"></spring:message></button>
        </div>
        <div id="restore-sync-publish-progress-bar-div" class="modal-footer padding10 text-left" style="display: none;">
			<jsp:include page="../common/processing-bar.jsp"></jsp:include>
		</div>
    </div>
    <!-- /.modal-content --> 
</div>
</div>
<script type="text/javascript">
function resetRestoreDescription(){
	clearAllMessagesPopUp();
	closeFancyBox();
	$("#descRestoreSync").val("");
	versionConfigId = "";
}
function restoreSyncServerInstance(){
	$("#restore-sync-publish-buttons-div").hide();
	$("#restore-sync-publish-progress-bar-div").show();
	$("#buttons-div").hide();
	$("#progress-bar-div").show();
	clearAllMessages();
	
	$.ajax({
		url: '<%= ControllerConstants.RESTORE_SYNC_INSTANCE %>',
		    cache: false,
			async: true,
			dataType: 'json',
			type: "POST",
			data: {
				id: serverInstance_Id,
				versionConfigId : versionConfigId,
				description : $("#descRestoreSync").val(),
				serverInstancesStatus:'ACTIVE'
			},
			success: function(data){
				$("#restore-sync-publish-buttons-div").show();
				$("#restore-sync-publish-progress-bar-div").hide();
				$("#buttons-div").show();
				$("#progress-bar-div").hide();
				var response = eval(data);
		    	response.msg = decodeMessage(response.msg);
		    	response.msg = replaceAll("+"," ",response.msg);
		    	$("#descRestoreSync").val("");
		    	versionConfigId = "";
		    	if(response.code == 200 || response.code == "200") {
		    		clearResponseMsgDiv();
		    		clearResponseMsg();
		    		clearErrorMsg();
		    		showSuccessMsg(response.msg);
		    		closeFancyBox();
		    		reloadVersionConfigDetail(serverInstance_Id);
		    	}else{
		    		if(response.object=="RESTORE_SYNC_PUBLIC_SUCCESS") {
		    			reloadVersionConfigDetail(serverInstance_Id);
		    		}
		    		showErrorMsg(response.msg);
		    		closeFancyBox();
		    	}
			},
		    error: function (xhr,st,err){
		    	$("#restore-sync-publish-buttons-div").show();
				$("#restore-sync-publish-progress-bar-div").hide();
				$("#buttons-div").show();
				$("#progress-bar-div").hide();
		    	handleGenericError(xhr,st,err);
			}
		});
}
</script>
