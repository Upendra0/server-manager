<%@taglib 	uri="http://www.springframework.org/tags" prefix="spring"%>

<div id="divStart" style=" width:100%; display: none;" >
    <div class="modal-content">
        <div class="modal-header padding10">
            <h4 class="modal-title"><spring:message code="serverMgmt.start.popup.header"></spring:message></h4>
        </div>
        <div class="modal-body padding10 inline-form">
        	<jsp:include page="../common/responseMsgPopUp.jsp" ></jsp:include>
	        <p>
	        	<i class="icon-lightbulb icon-large"></i><span class="note"><spring:message code="label.important.note"></spring:message></span><br/>
	        </p>
			<p>	
				<spring:message code="serverMgmt.start.popup.note.content"></spring:message>           	
	        </p>
	        <p id="start-msgMismatch" style="display: none;">	  
				<strong><spring:message code="server.manager.and.server.detail.mismatch"></spring:message></strong>         	
	        </p>
	        <p>
	        	<strong><spring:message code="serverManagement.popup.wish.continue.lbl"></spring:message></strong>
	        </p>
        </div>
        <div id="server-start-buttons-div" class="modal-footer padding10">
            <button type="button" id="btn-yes" class="btn btn-grey btn-xs " onclick="startInstance();"><spring:message code="btn.label.yes"></spring:message></button>
            <button type="button" id="btn-no" class="btn btn-grey btn-xs " data-dismiss="modal" onclick="closeFancyBox();searchInstanceCriteria();"><spring:message code="btn.label.no"></spring:message></button>
        </div>
        <div id="server-start-progress-bar-div" class="modal-footer padding10 text-left" style="display: none;">
			<jsp:include page="../common/processing-bar.jsp"></jsp:include>
		</div>
    </div>
    <!-- /.modal-content --> 
</div>
