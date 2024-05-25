<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>


	        <div class="lightbox-content" id="staffMgmt-activeInActive" style="display: none;">
       			<div class="modal-header">
<!-- 	           				<button type="button" class="close" data-dismiss="modal" aria-hidden="true" onclick="searchStaffCriteria()">&times;</button> -->
      				<h4 class="modal-title">
      					<spring:message code="updateStaffMgmt-change.status.heading" ></spring:message>
      				</h4>
       			</div>
				<div class="modal-body">
					<div class="fullwidth">
						 <div class="col-md-4">
						 	<p>
								<spring:message code="updateStaffMgmt-current-status" ></spring:message>
						 	</p>
						 	<p>
						 		<spring:message code="updateStaffMgmt-reason-for-change" ></spring:message>
							</p>
						</div>
						<div class="col-md-4">
							<p><label class="col-lg-4 control-label" id="staff-current-status"></label></p>
						</div>
          			</div>
        			<div class="clearfix" id="staff-reason-for-change-content">
	           			<input type="hidden" id="staff-id" > 
						<input type="hidden" id="staff-state" >
	                   	<textarea rows="3" class="form-control" id="staff-reason-for-change"></textarea>
	                   	<span id="staff-reason-for-change-errors" class="error" style="color: red !important;"></span>
	                </div>
					<div class="modal-footer">
						<button type="button" class="btn btn-grey btn-xs" onclick="validateChangeStaffStatus();" id="staff-active-inactive-btn">
							<spring:message code="btn.label.update" ></spring:message>
						</button>
						<button type="button" class="btn btn-grey btn-xs" data-dismiss="modal" onclick="searchStaffCriteria();closeFancyBox();" id="staff-modal-close-btn">
							<spring:message code="btn.label.cancel" ></spring:message>
						</button>
					</div>
				</div><!-- /.modal-dialog -->
			</div><!-- /.modal -->
			
			<script type="text/javascript">
				function validateChangeStaffStatus(){
					$.ajax({
						url: '<%= ControllerConstants.CHANGE_STAFF_STATE %>',
					    cache: false,
						async: true,
						dataType: 'json',
						type: "POST",
						data: {
							staffId: 	$("#staff-id").val(),
							state: 		$("#staff-state").val(),
							reason:		$("#staff-reason-for-change").val()
						},
						success: function(data){
							var response = eval(data);
							
					    	response.msg = decodeMessage(response.msg);
					    	response.msg = replaceAll("+"," ",response.msg);
		
					    	if(response.code == 200 || response.code == "200") {
					    		$("#staff-reason-for-change").val('');
					    		$("#staff-modal-close-btn").click();
					    		closeFancyBox();
					    		clearAllMessages();
					    		showSuccessMsg(response.msg);
					    	}else{
					    		$("#staff-reason-for-change").focus();
					    		$("#staff-reason-for-change-errors").html(response.msg);
					    	}
					    	
						},
					    error: function (jqXHR, textStatus, errorThrown){
					    	handleGenericError(jqXHR,textStatus,errorThrown);
						}
					});
					
				}
				
			</script>
			

