<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<%@taglib 	uri="http://www.springframework.org/tags" 			prefix="spring"%>
<%@ taglib 	uri="http://www.springframework.org/security/tags" 	prefix="sec"  %>

		    <div class="modal-content">
		        <div class="modal-header padding10">
		            <h4 class="modal-title"><spring:message code="serverMgmt.delete.instance.popup.header"></spring:message></h4>
		        </div>
		        <div class="modal-body padding10 inline-form">
	        		<jsp:include page="../common/responseMsgPopUp.jsp" ></jsp:include>			        
	        		<p>
			        	<input type='hidden' id="syncInstanceId" />
			        	<table style='width:100%'>
			        		<tr>
			        			<td width="15%">
			        				<img alt="Server Instance" src="img/server-instance.png" class="img-responsive" />
			        			</td>
			        			<td width="85%">
			        				<div id="tblDeleteInstanceList">
									</div>
			        			</td>
			        		</tr>
			        	</table>
			        </p>
					<div id ="noteIdbeforeDelete" >
					<p>	           	
			           	<span class="note"><spring:message code="label.important.note"></spring:message></span><br/>
			           	<spring:message code="serverMgmt.delete.instance.popup.export.content"></spring:message>			           
			        </p>
			        </div>
			        <div id="noteAfterdelete"   style="display: none;">
			        <p>	           	
			           	<span class="note"><spring:message code="label.information.note"></spring:message></span><br/>
			           	<spring:message code="serverMgmt.delete.instance.popup.export.information"></spring:message>			        
			        </p>			        
			        </div>
			        
			        
			        
		        </div>
		        <div id="delete-buttons-div" class="modal-footer padding10">
		          
		            <button type="button" class="btn btn-grey btn-xs" id="btnDeletePopup" onclick="DeleteInstance('<%= ControllerConstants.DELETE_SERVER_INSTANCE %>');"><spring:message code="btn.label.delete"></spring:message></button>
		            <button type="button" class="btn btn-grey btn-xs " id="btnDeleteCancel" data-dismiss="modal" onclick="closeFancyBox();clearSelection();searchInstanceCriteria();"><spring:message code="btn.label.cancel"></spring:message></button>
		            <button type="button" class="btn btn-grey btn-xs " id="btnDeleteClose" data-dismiss="modal" onclick="closeFancyBox();clearSelection();searchInstanceCriteria();" style="display: none;"><spring:message code="btn.label.close"></spring:message></button>
		            
		            <button type="button" class="btn btn-grey btn-xs " id="btnDeleteClose-server-mgmt" data-dismiss="modal" onclick="closeFancyBox();resetSearchInstanceCriteria();" style="display: none;"><spring:message code="btn.label.close"></spring:message></button>
		            <button type="button" class="btn btn-grey btn-xs " id="btnDeleteClose-create-server" data-dismiss="modal" onclick="closeFancyBox();moveToCreateServer('create_server');" style="display: none;"><spring:message code="btn.label.close"></spring:message></button>
		        </div>
		        <div id="delete-progress-bar-div" class="modal-footer padding10 text-left" style="display: none;">
					<jsp:include page="../common/processing-bar.jsp"></jsp:include>
				</div>	
		    </div>
		    <!-- /.modal-content --> 
		
