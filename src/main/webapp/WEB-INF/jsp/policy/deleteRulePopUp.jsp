 <%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<%@taglib 	uri="http://www.springframework.org/tags" 			prefix="spring"%>
 <div class="modal-content">
		        <div class="modal-header padding10">
		            <h4 class="modal-title"><spring:message code="policymgmt.rule.delete.popup.header"></spring:message></h4>
		        </div>
		        <div class="modal-body padding10 inline-form">
	        		<div id="divDeleteRuleMsg" style="color:red;">
	        			<span class="title">
						</span>
					</div>
			        <p>
			        	<input type='hidden' id="deleteRuleId" />
			        	
			        	<table style='width:100%'>
			        		<tr>
			        			<td width="100%">
									<div id="tblDeleteRuleList">
									</div>
			        			</td>
			        		</tr>
			        	</table>
			        </p>
			        </div>
		        <div id="delete-buttons-div" class="modal-footer padding10">
		            <%-- <button type="button" class="btn btn-grey btn-xs" id="btnExportDeletePopup" onclick="exportServiceInstanceBeforeDelete();"><spring:message code="btn.label.export.config"></spring:message></button> --%>
		            <button type="button" class="btn btn-grey btn-xs" id="btnDeleteRulePopup" onclick="deleteRuleInstance('<%= ControllerConstants.REMOVE_RULE %>');"><spring:message code="btn.label.delete"></spring:message></button>
		            <button type="button" class="btn btn-grey btn-xs " id="btnDeleteRuleCancel" data-dismiss="modal" onclick="closeFancyBox();clearSelection();"><spring:message code="btn.label.cancel"></spring:message></button>
		            
		        </div>
		        <div id="delete-progress-bar-div" class="modal-footer padding10 text-left" style="display: none;">
					<jsp:include page="../common/processing-bar.jsp"></jsp:include>
				</div>	
				
		    </div>
	
		    <!-- /.modal-content -->
		  
		  
		    
