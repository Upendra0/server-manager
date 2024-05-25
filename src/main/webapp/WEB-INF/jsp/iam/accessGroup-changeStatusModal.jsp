<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="/WEB-INF/ShowErrorTag.tld" prefix="elitecoreError" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="elitecore" %>

	<div id="accessGrpMgmt-activeInActive" style="display: none;" class="lightbox-content">
    	<div class="modal-content">
        	<div class="modal-header padding10">
				<h4 class="modal-title">
					<spring:message code="updateAccessGroup-change.status.heading" ></spring:message>
				</h4>
			</div>
			<div class="modal-body padding10 inline-form">
				<div style="display:inline-flex;width=calc();color: #FF0000;padding-bottom: 5px" id="divErrorMsg" >
				</div>
				<elitecore:inputLabelWithValue 
									labelClass="table-cell-label" 
									i18NLabelCode="updateAccessGroup-current-assign-status" 
									labelValueClass="" 
									id="ag-current-assign-status">
				</elitecore:inputLabelWithValue>
				
				<elitecore:inputLabelWithValue 
									labelClass="table-cell-label" 
									i18NLabelCode="updateAccessGroup-current-status" 
									labelValueClass="" 
									id="ag-current-status">
				</elitecore:inputLabelWithValue>
				
				<elitecore:textAreaHTML 
									labelClass="table-cell-label"
									isMandatoryField="true"
									id="ag-reason-for-change" 
									name="ag-reason-for-change" 
									rows="3"
									inputClassName="form-control"
									i18NCode="updateAccessGroup-reason-for-change"
									i18NToolTipCode="updateAccessGroup-reason-for-change">
				</elitecore:textAreaHTML>
				
				<div class="clearfix">
					<input type="hidden" id="ag-access-group-id" > 
					<input type="hidden" id="ag-access-group-state" >
				</div>
				
				<div class="modal-footer padding10">
            	<button type="button" class="btn btn-grey btn-xs" onclick="validateFieldsForChangeState()" id="ag-active-inactive-btn">
					<spring:message code="addAccessGroup-access.group.update" ></spring:message>
				</button>
				<button type="button" class="btn btn-grey btn-xs" data-dismiss="modal" onclick="searchCriteria();resetWarningDisplay();closeFancyBox();" id="ag-modal-close-btn">
					<spring:message code="addAccessGroup-access.group.cancel" ></spring:message>
				</button>
			</div>
		</div><!-- /.modal-content -->
	</div>
	</div>
		
	       	
			<script type="text/javascript">
				function validateFieldsForChangeState(){
					clearAllMessages();
					clearResponseMsgDiv();
					
					$.ajax({
						url: '<%= ControllerConstants.CHANGE_ACCESS_GROUP_STATE %>',
					    cache: false,
						async: true,
						dataType: 'json',
						type: "POST",
						data: {
							accessGroupId: 	$("#ag-access-group-id").val(),
							state: 			$("#ag-access-group-state").val(),
							reason: 		$("#ag-reason-for-change").val()
						},
						success: function(data){
							var response = eval(data);
					    	response.msg = decodeMessage(response.msg);
					    	response.msg = replaceAll("+"," ",response.msg);
						
					    	console.log("Code is : " + response.msg);
					    	
					    	if(response.code == 200 || response.code == "200") {
					    		$("#ag-reason-for-change").val('');
					    		$("#ag-modal-close-btn").click();
					    		closeFancyBox();
					    		showSuccessMsg(response.msg);
					    	}else{
					    		resetWarningDisplay();
					    		
					    		$("#ag-reason-for-change").next().children().first().attr("data-original-title",response.msg);
					    		$('#ag-reason-for-change').parent().parent().addClass('error');
					    		
					    		$('#divErrorMsg').html($("#GENERIC_ERROR_MESSAGE").val());
					    		$('#divErrorMsg').show();
					    		
					    		
					    	}
						},
					    error: function (jqXHR, textStatus, errorThrown){
							alert(textStatus, errorThrown);
						}
					});
				}
				
				
			</script>
