<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>


	        <div id="staffMgmt-lockUnlock" class="lightbox-content" style="display: none;">
       			<div class="modal-header">
      				<h4 class="modal-title">
      					<spring:message code="updateStaffMgmt-lock.unlock.heading" ></spring:message>
      				</h4>
       			</div>
				<div class="modal-body">
					<div class="fullwidth">
						<div class="col-md-4">
							<p>
								<spring:message code="updateStaffMgmt-current-lock-unlock" ></spring:message>
							</p>
							<p>
								<label  for="updateStaffMgmt-lock-unlock-note" style="color: red;font-weight: bold;">
									<spring:message code="updateStaffMgmt-lock-unlock-note" ></spring:message>
								</label>
								<br/>
								<span id="staff-lock-unlock-errors" class="error" style="color: red !important;"></span>
							</p>
						</div>
						<div class="col-md-4">
							<p>
								<label class="col-lg-4" id="staff-current-lock-unlock">
								</label>
							</p>
						</div>
						<br/>
					</div>
				</div>
				<div class="modal-footer">
					<input type="hidden" id="staff-id-for-lock-unlock">
					<input type="hidden" id="staff-new-account-locked-status">
					<button type="button" class="btn btn-grey btn-xs" onclick="validateChangeLockStatus();" id="staff-lock-unlock-btn">
						<spring:message code="btn.label.continue" ></spring:message>
					</button>
					<button type="button" class="btn btn-grey btn-xs" data-dismiss="modal" onclick="searchStaffCriteria();closeFancyBox();" id="staff-modal-lock-unlock-close-btn">
						<spring:message code="btn.label.cancel" ></spring:message>
					</button>
				</div>
			</div><!-- /.modal -->
			
			<script type="text/javascript">
				function validateChangeLockStatus(){
					$.ajax({
						url: '<%= ControllerConstants.LOCK_UNLOCK_STAFF %>',
					    cache: false,
						async: true,
						dataType: 'json',
						type: "POST",
						data: {
							staffId: 		$("#staff-id-for-lock-unlock").val(),
							accountLocked: 	$("#staff-new-account-locked-status").val()
						},
						success: function(data){
							var response = eval(data);
					    	response.msg = decodeMessage(response.msg);
					    	response.msg = replaceAll("+"," ",response.msg);

					    	if(response.code == 200 || response.code == "200") {
					    		$("#staff-modal-lock-unlock-close-btn").click();
					    		closeFancyBox();
					    		clearAllMessages();
					    		showSuccessMsg(response.msg);
					    	}else{
					    		$("#staff-lock-unlock-errors").html(response.msg);
					    	}
					    	
						},
					    error: function (jqXHR, textStatus, errorThrown){
					    	handleGenericError(jqXHR,textStatus,errorThrown);
						}
					});
					
				}
			</script>
