<%@taglib 	uri="http://www.springframework.org/tags" prefix="spring"%>

<div id="divSoftRestart" style=" width:100%; display: none;" >
    <div class="modal-content">
        <div class="modal-header padding10">
            <h4 class="modal-title"><spring:message code="serverMgmt.soft.restart.popup.header"></spring:message></h4>
        </div>
        <div class="modal-body padding10 inline-form">
        <jsp:include page="../common/responseMsgPopUp.jsp" ></jsp:include>
	        <p class="note">
	        	<i class="icon-lightbulb icon-large"></i><span class="required"><spring:message code="label.important.note"></spring:message></span><br/>
	        </p>
			<p>	
				<spring:message code="serverMgmt.soft.restart.popup.note.content"></spring:message>           	
	        </p>
	        <p id="soft-restart-msgMismatch" style="display: none;">	  
				<strong><spring:message code="server.manager.and.server.detail.mismatch"></spring:message></strong>         	
	        </p>
	        <p>
	        	<strong><spring:message code="serverManagement.popup.wish.continue.lbl"></spring:message></strong>
	        </p>
        </div>
        <div id="soft-restart-buttons-div" class="modal-footer padding10">
            <button type="button" class="btn btn-grey btn-xs " id="btn_softresart_yes" onclick="softRestartInstance();"><spring:message code="btn.label.yes"></spring:message></button>
            <button type="button" class="btn btn-grey btn-xs " id="btn_softresart_no"  data-dismiss="modal" onclick="closeFancyBox();searchInstanceCriteria();"><spring:message code="btn.label.no"></spring:message></button>
        </div>
        <div id="soft-restart-progress-bar-div" class="modal-footer padding10 text-left" style="display: none;">
			<jsp:include page="../common/processing-bar.jsp"></jsp:include>
		</div>
    </div>
    <!-- /.modal-content --> 
</div>
