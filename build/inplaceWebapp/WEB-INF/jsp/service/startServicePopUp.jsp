<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<%@taglib 	uri="http://www.springframework.org/tags" 			prefix="spring"%>
<%@ taglib 	uri="http://www.springframework.org/security/tags" 	prefix="sec"  %>
 <div class="modal-content">
		        <div class="modal-header padding10">
		            <h4 class="modal-title"><spring:message code="serviceMgmt.start.popup.header"></spring:message></h4>
		        </div>
		        <div class="modal-body padding10 inline-form">
		        <input type="hidden" id="startServiceId" /> 
			        <p>
			        	<table>
			        		<tr>
			        			<td>
			        				<img alt="Service Instance" src="img/ftp-img.png" class="img-responsive" />
			        			</td>
			        			<td style="padding-left: 20px;">
			        				<spring:message code="serviceManagement.grid.column.service.id"></spring:message>
			        				&nbsp;&nbsp;<label id="lblServiceId"> SVC</label> <br/>		
			        				<spring:message code="serviceManagement.grid.column.service.name"></spring:message>
			        				&nbsp;&nbsp;<label id="lblServiceName">ServiceName</label><br/>
			        			</td>
			        		</tr>
			        	</table>
			       
			        <p>
			        	<i class="icon-lightbulb icon-large"></i><span class="note"><spring:message code="label.important.note"></spring:message></span><br/>
			        </p>
					<p>	
						<spring:message code="serviceMgmt.start.popup.note.content"></spring:message>           	
			        </p>
			        <div id="syncWarningMsg" style="color: #FF0000 ; display: none;">
			        <p>	
						<spring:message code="serviceMgmt.start.popup.note.sync.warning.content"></spring:message>           	
			        </p>
			        </div>
			        <p>
			        	<strong><spring:message code="serviceMgmt.start.popup.continue.message"></spring:message></strong>
			        </p>
		        </div>
		        <div id="start-buttons-div" class="modal-footer padding10">
		        	<sec:authorize access="hasAuthority('START_SERVICE_INSTANCE')">
		            	<button type="button" class="btn btn-grey btn-xs" id="service-start-btn" onclick="startServiceInstance();"><spring:message code="btn.label.yes"></spring:message></button>
		            </sec:authorize>
		            <button type="button" class="btn btn-grey btn-xs" id="service-start-close-btn" data-dismiss="modal" onclick="closeFancyBox();searchInstanceCriteria();"><spring:message code="btn.label.no"></spring:message></button>
		            <button type="button" class="btn btn-grey btn-xs" id="server-start-close-btn" data-dismiss="modal" onclick="closeFancyBox();resetCheckBox('start');"><spring:message code="btn.label.no"></spring:message></button>
		        </div>
		        <div id="start-progress-bar-div" class="modal-footer padding10 text-left" style="display: none;">
					<jsp:include page="../common/processing-bar.jsp"></jsp:include>
				</div>
		    </div>
		    <!-- /.modal-content -->
		    
		    
		  <script type="text/javascript">
		  
		  function startServiceInstance(){
			 
				$("#start-buttons-div").hide();
				$("#start-progress-bar-div").show();
				
				var serviceInstanceId ;
				var serviceStatus;
				
				if(currentTab == 'SERVICE_MANAGEMENT'){
					serviceInstanceId = ckIntanceSelected.join();
				}
				
				if(currentTab == 'COLLECTION_SERVICE_SUMMARY'){
					serviceInstanceId = $("#serviceId").val();
				}
				if(currentTab == 'NETFLOW_COLLECTION_SERVICE_SUMMARY' || currentTab == 'PROCESSING_SERVICE_SUMMARY' || currentTab == 'CONSOLIDATION_SERVICE_SUMMARY'
						|| currentTab == 'DIAMETER_COLLECTION_SERVICE_SUMMARY'){
					serviceInstanceId = $("#serviceId").val();
				}
				if(currentTab == 'UPDATE_INSTANCE_SUMMARY'){
					serviceInstanceId = $("#startServiceId").val();
				}
				if(currentTab == 'IPLOG_SERVICE_SUMMARY'){
					serviceInstanceId = $("#startServiceId").val();
				}
				if(currentTab == 'PARSING_SERVICE_SUMMARY'){
					serviceInstanceId = $("#startServiceId").val();
				}
				
				if(currentTab == 'DISTRIBUTION_SERVICE_SUMMARY'){
					serviceInstanceId = $("#startServiceId").val();
				}
				if(currentTab == 'AGGREGATION_SERVICE_SUMMARY'){
					serviceInstanceId = $("#startServiceId").val();
				}
				
				 $.ajax({
					url: '<%= ControllerConstants.START_SERVICE_INSTANCE %>',
					    cache: false,
						async: true,
						dataType: 'json',
						type: "POST",
						timeout : 15000,
						data: {
							id: serviceInstanceId
						},
						success: function(data){
							$("#start-buttons-div").show();
							$("#start-progress-bar-div").hide();
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
					    		$('#'+serviceInstanceId +'_INACTIVE').bootstrapToggle('off');
					    		showErrorMsgPopUp(response.msg);
					    		closeFancyBox();
					    	}
					    	
					    	if(currentTab == 'SERVICE_MANAGEMENT' ){
				    			loadServiceStatusGUI(serviceInstanceId,'service_management');
							}
							
					    	if(currentTab == 'UPDATE_INSTANCE_SUMMARY' ){
				    			loadServiceStatusGUI(serviceInstanceId,'service_management');
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
							if(currentTab == 'PARSING_SERVICE_SUMMARY'){
								loadServiceStatusGUI(serviceInstanceId,'parsing_service_summary');
							}
							if(currentTab == 'PROCESSING_SERVICE_SUMMARY'){
								loadServiceStatusGUI(serviceInstanceId,'processing_service_summary');
							}
							if(currentTab == 'PROCESSING_SERVICE_SUMMARY'){
								loadServiceStatusGUI(serviceInstanceId,'consolidation_service_summary');
							}
						},
					    error: function (xhr,st,err){
					    	if(st == "timeout"){
					    		clearResponseMsgDiv();
					    		clearResponseMsg();
					    		clearErrorMsg();
					    		ckIntanceSelected = new Array();
					    		closeFancyBox();
					    		showSuccessMsg("A start call has been initiated to the service it should update in a while,if not try reloading the page or starting it again to view the changes.");
					    	}
					    	$("#start-buttons-div").show();
							$("#start-progress-bar-div").hide();
					    	handleGenericError(xhr,st,err);
						}
					}); 
			}
		  </script>  
		    
