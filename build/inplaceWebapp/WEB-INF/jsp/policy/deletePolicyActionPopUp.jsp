 <%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<%@taglib 	uri="http://www.springframework.org/tags" 			prefix="spring"%>
<%@ taglib 	uri="http://www.springframework.org/security/tags" 	prefix="sec"  %>
 <div class="modal-content">
		        <div class="modal-header padding10">
		            <h4 class="modal-title"><spring:message code="policy.action.delete.instance.popup.header"></spring:message></h4>
		        </div>
		        <div class="modal-body padding10 inline-form">
	        		<div id="divDeletePolicyActionMsg" style="color:red;">
	        			<span class="title">
						</span>
					</div>
			        <p>
			        	<input type='hidden' id="deletePolicyActionId" />
			        	
			        	<table style='width:100%'>
			        		<tr>
			        			<td width="100%">
									<div id="tblDeletePolicyActionList">
									</div>
			        			</td>
			        		</tr>
			        	</table>
			        </p>
			        </div>
		        <div id="delete-buttons-div" class="modal-footer padding10">
		            <%-- <button type="button" class="btn btn-grey btn-xs" id="btnExportDeletePopup" onclick="exportServiceInstanceBeforeDelete();"><spring:message code="btn.label.export.config"></spring:message></button> --%>
		            <button type="button" class="btn btn-grey btn-xs" id="btnDeletePolicyActionPopup" onclick="DeletePolicyActionInstance();"><spring:message code="btn.label.delete"></spring:message></button>
		            <button type="button" class="btn btn-grey btn-xs " id="btnDeletePolicyActionCancel" data-dismiss="modal" onclick="closeFancyBox();clearSelection();searchInstanceCriteria();"><spring:message code="btn.label.cancel"></spring:message></button>
		            
		        </div>
		        <div id="delete-progress-bar-div" class="modal-footer padding10 text-left" style="display: none;">
					<jsp:include page="../common/processing-bar.jsp"></jsp:include>
				</div>	
				
		    </div>
	
		    <!-- /.modal-content -->
		  
		  
		    
