<%@taglib 	uri="http://www.springframework.org/tags" prefix="spring"%>

<div id="divSyncPublish" style=" width:100%; display: none;" >
    <div class="modal-content">
        <div class="modal-header padding10">
            <h4 class="modal-title"><spring:message code="serverMgmt.sync.publish.popup.header"></spring:message></h4>
        </div>
         <div style="display: none;">
			<input type="hidden" id="syncPublishInstanceId" name="syncPublishInstanceId">
		</div>
        <div class="modal-body padding10 inline-form">
        <jsp:include page="../common/responseMsgPopUp.jsp" ></jsp:include>
	        <div id="syncPublishDiv">
		        	<div class="form-group" >
		            	<spring:message code="serverMgmt.sync.publish.popup.description" var="tooltip"></spring:message>
		         		<div class="table-cell-label">${tooltip}</div>
		             	<div class="input-group ">
		             		<input type="text" class="form-control" tabindex="14" id="descSyncPublish" name="descSyncPublish"/>
		             	</div>
		         	</div>
		         	<p id="synchronizePopUpMsg" class="synchronizePopUpMsgClass">           	
			           	<span class="note"><spring:message code="label.important.note"></spring:message></span><br/><br/>
			           	<spring:message code="serverMgmt.sync.publish.popup.note.content"></spring:message>
			 		</p>
        	</div>
        
        <div id="sync-publish-buttons-div" class="modal-footer padding10">
        	<sec:authorize access="hasAnyAuthority('SYNC_PUBLISH_VERSION')">
            	<button type="button" class="btn btn-grey btn-xs " id="btn_syncpublish_yes" onclick="syncPublishInstance();"><spring:message code="btn.label.sync.publish"></spring:message></button>
            </sec:authorize>
            <button type="button" class="btn btn-grey btn-xs " id="btn_syncpublish_no"  data-dismiss="modal" onclick="resetDescription();"><spring:message code="btn.label.cancel"></spring:message></button>
        </div>
        <div id="sync-publish-progress-bar-div" class="modal-footer padding10 text-left" style="display: none;">
			<jsp:include page="../common/processing-bar.jsp"></jsp:include>
		</div>
    </div>
    <!-- /.modal-content --> 
</div>
</div>

<script type="text/javascript">
function resetDescription(){
	clearAllMessagesPopUp();
	closeFancyBox();
	$("#descSyncPublish").val("");
	$("#syncPublishInstanceId").val("");
}
</script>
