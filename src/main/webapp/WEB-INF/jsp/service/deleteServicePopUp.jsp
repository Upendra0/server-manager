 <%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<%@taglib 	uri="http://www.springframework.org/tags" 			prefix="spring"%>
<%@ taglib 	uri="http://www.springframework.org/security/tags" 	prefix="sec"  %>
 <div class="modal-content">
		        <div class="modal-header padding10">
		            <h4 class="modal-title"><spring:message code="serviceMgmt.delete.instance.popup.header"></spring:message></h4>
		        </div>
		        <div class="modal-body padding10 inline-form">
	        		<div id="divDeleteMsg" style="color:red;">
	        			<span class="title">
						</span>
					</div>
			        <p>
			        	<input type='hidden' id="deleteServiceId" />
			        	
			        	<table style='width:100%'>
			        		<tr>
			        			<td width="100%">
									<div id="tblDeleteInstanceList">
									</div>
			        			</td>
			        		</tr>
			        	</table>
			        </p>
			        <div id ="delServiceImpNote">
					<p>	           	
			           	<span class="note"><spring:message code="label.important.note"></spring:message></span><br/><br/>
			           	<spring:message code="serviceMgmt.delete.service.popup.content"></spring:message>
			        </p>
			        </div>
		        </div>
		        <div id="delete-buttons-div" class="modal-footer padding10">
		            <%-- <button type="button" class="btn btn-grey btn-xs" id="btnExportDeletePopup" onclick="exportServiceInstanceBeforeDelete();"><spring:message code="btn.label.export.config"></spring:message></button> --%>
		            <sec:authorize access="hasAuthority('DELETE_SERVICE_INSTANCE')">
		            	<button type="button" class="btn btn-grey btn-xs" id="btnDeletePopup" onclick="DeleteServiceInstance('<%= ControllerConstants.DELETE_SERVICE_INSTANCE %>');"><spring:message code="btn.label.delete"></spring:message></button>
		            </sec:authorize>
		            <button type="button" class="btn btn-grey btn-xs " id="btnDeleteCancel" data-dismiss="modal" onclick="closeFancyBox();clearSelection();searchInstanceCriteria();"><spring:message code="btn.label.cancel"></spring:message></button>
		            
		            <button type="button" class="btn btn-grey btn-xs " id="btnDeleteClose-server-instance" data-dismiss="modal" onclick="closeFancyBox();clearServiceSummaryInstanceGrid();" style="display: none;"><spring:message code="btn.label.close"></spring:message></button>
		            <button type="button" class="btn btn-grey btn-xs " id="btnDeleteClose-service-mgmt" data-dismiss="modal" onclick="closeFancyBox();searchInstanceCriteria();" style="display: none;"><spring:message code="btn.label.close"></spring:message></button>
		            <button type="button" class="btn btn-grey btn-xs " id="btnDeleteClose-create-service" data-dismiss="modal" onclick="closeFancyBox();moveToCreateService('create_service');" style="display: none;"><spring:message code="btn.label.close"></spring:message></button>
		        </div>
		        <div id="delete-progress-bar-div" class="modal-footer padding10 text-left" style="display: none;">
					<jsp:include page="../common/processing-bar.jsp"></jsp:include>
				</div>	
				
		    </div>
		    <!-- /.modal-content -->
		  
		  
		    
