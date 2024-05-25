<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<%@taglib 	uri="http://www.springframework.org/tags" 			prefix="spring"%>
<%@ taglib 	uri="http://www.springframework.org/security/tags" 	prefix="sec"  %>
 <div class="modal-content">
		        <div class="modal-header padding10">
		            <h4 class="modal-title"><spring:message code="serviceMgmt.stop.popup.header"></spring:message></h4>
		        </div>
		        <div class="modal-body padding10 inline-form">
		        <input type="hidden" id="stopServiceId" /> 
			        <p>
			        	<table>
			        		<tr>
			        			<td>
			        				<img alt="Service Instance" src="img/ftp-img.png" class="img-responsive" />
			        			</td>
			        			<td style="padding-left: 20px;">
			        				<spring:message code="serviceManagement.grid.column.service.id"></spring:message>
			        				&nbsp;&nbsp;<label id="lblServiceId"> </label><br/>		
			        				<spring:message code="serviceManagement.grid.column.service.name"></spring:message>
			        				&nbsp;&nbsp;<label id="lblServiceName"></label><br/>
			        			</td>
			        		</tr>
			        	</table>
			        </p>
			        <p>
			        	<i class="icon-lightbulb icon-large"></i><span class="note"><spring:message code="label.important.note"></spring:message></span><br/>
			        </p>
					<p>	
						<spring:message code="serviceMgmt.stop.popup.note.content"></spring:message>           	
			        </p>
			        <p>
			        	<strong><spring:message code="serviceMgmt.stop.popup.continue.message"></spring:message></strong>
			        </p>
		        </div>
		        <div id="stop-buttons-div" class="modal-footer padding10">
		        	<sec:authorize access="hasAuthority('STOP_SERVICE_INSTANCE')">
		            	<button type="button" class="btn btn-grey btn-xs " onclick="stopServiceInstance();"><spring:message code="btn.label.yes"></spring:message></button>
		            </sec:authorize>
		            <button type="button" class="btn btn-grey btn-xs " id="service-stop-close-btn" data-dismiss="modal" onclick="closeFancyBox();searchInstanceCriteria();"><spring:message code="btn.label.no"></spring:message></button>
		            <button type="button" class="btn btn-grey btn-xs " id="server-stop-close-btn" data-dismiss="modal" onclick="closeFancyBox();resetCheckBox('stop');"><spring:message code="btn.label.no"></spring:message></button>
		        </div>
		        <div id="stop-progress-bar-div" class="modal-footer padding10 text-left" style="display: none;">
					<jsp:include page="../common/processing-bar.jsp"></jsp:include>
				</div>
		    </div>
		    <!-- /.modal-content -->
		    
		    
		    <script type="text/javascript">
	
		    function stopServiceInstance(){
		    	
				var serviceStatus;
				var serviceInstanceId ;
				if(currentTab == 'SERVICE_MANAGEMENT'){
					serviceInstanceId = ckIntanceSelected.join();
				}
				
				if(currentTab == 'UPDATE_INSTANCE_SUMMARY'){
					serviceInstanceId = $("#stopServiceId").val();
				}
				
				if(currentTab == 'COLLECTION_SERVICE_SUMMARY'){
					serviceInstanceId = $("#serviceId").val();
				}
				
				if(currentTab == 'NETFLOW_COLLECTION_SERVICE_SUMMARY' || currentTab == 'PROCESSING_SERVICE_SUMMARY' || currentTab == 'CONSOLIDATION_SERVICE_SUMMARY' 
						|| currentTab == 'DIAMETER_COLLECTION_SERVICE_SUMMARY'){
					serviceInstanceId = $("#serviceId").val();
				}
				if(currentTab == 'IPLOG_SERVICE_SUMMARY' || currentTab == 'PARSING_SERVICE_SUMMARY'){
					serviceInstanceId = $("#startServiceId").val();
				}
				
				if(currentTab == 'DISTRIBUTION_SERVICE_SUMMARY'){
					serviceInstanceId = $("#startServiceId").val();
				}
				
				$("#stop-buttons-div").hide();
				$("#stop-progress-bar-div").show();
				 $.ajax({
					url: '<%= ControllerConstants.STOP_SERVICE_INSTANCE %>',
					    cache: false,
						async: true,
						dataType: 'json',
						type: "POST",
						timeout: 30000,
						data: {
							id: serviceInstanceId
						},
						success: function(data){
							$("#stop-buttons-div").show();
							$("#stop-progress-bar-div").hide();
							var response = eval(data);
					    	response.msg = decodeMessage(response.msg);
					    	response.msg = replaceAll("+"," ",response.msg);
				    	
					    	if(response.code == 200 || response.code == "200") {
					    		//resetSearchInstanceCriteria();
					    		clearResponseMsgDiv();
					    		clearResponseMsg();
					    		clearErrorMsg();
					    		showSuccessMsg(response.msg);
					    		ckIntanceSelected = new Array();
					    		closeFancyBox();
					    		
					    	}else{
					    		$('#'+serviceInstanceId +'_ACTIVE').bootstrapToggle('on');
					    		showErrorMsgPopUp(response.msg);
					    	}
					    	
					    	if(currentTab == 'SERVICE_MANAGEMENT' ){
				    			loadServiceStatusGUI(serviceInstanceId,'service_management');
							}
							
					    	if(currentTab == 'UPDATE_INSTANCE_SUMMARY' ){
				    			loadServiceStatusGUI(serviceInstanceId,'service_management');
				    			$("#server-stop-close-btn").show();
							}
					    	
							if(currentTab == 'COLLECTION_SERVICE_SUMMARY'){
								loadServiceStatusGUI(serviceInstanceId,'collection_summary');
							}
							
							if(currentTab == 'NETFLOW_COLLECTION_SERVICE_SUMMARY'){
								loadServiceStatusGUI(serviceInstanceId,'netflow_collection_summary');
							}
							if(currentTab == 'IPLOG_SERVICE_SUMMARY'){
								loadServiceStatusGUI(serviceInstanceId,'iplog_service_summary');
							}
							if(currentTab == 'PROCESSING_SERVICE_SUMMARY'){
								loadServiceStatusGUI(serviceInstanceId,'iplog_service_summary');
							}
							if(currentTab == 'CONSOLIDATION_SERVICE_SUMMARY'){
								loadServiceStatusGUI(serviceInstanceId,'consolidation_service_summary');
							}
						},
					    error: function (xhr,st,err){
					    	$("#stop-buttons-div").show();
							$("#stop-progress-bar-div").hide();
							if(st == "timeout"){
								clearResponseMsgDiv();
					    		clearResponseMsg();
					    		clearErrorMsg();
					    		showSuccessMsg("A stop call has been initiated to the service it should update in a while,if not try reloading the page to view the changes or else tune the service parameters");
					    		ckIntanceSelected = new Array();
					    		closeFancyBox();
							}
					    	handleGenericError(xhr,st,err);
						}
					}); 
			}
		    
		    </script>
		    
		    
