var maximumJmxCalls = 10;
function getServiceListByServer(elementId,currentServerId){
	var serviceType = $("#search_service_type").val();
	//due to grid refreshed : commented out this function
	//manageProcessingServiceAttribute(serviceType);
	if(serviceType != "-1"){
		$.ajax({
			url : "getServiceByType",
			cache : false,
			async : true,
			dataType : 'json',
			type : "POST",
			data : {
				"serviceAlias" : serviceType
			},
			success : function(data) {
				var response = eval(data);
				var responseCode = response.code;
				var responseMsg = response.msg;
				var responseObject = response.object;
				if (responseCode == "200") {
					resetWarningDisplay();
					clearAllMessages();
					
					if(serviceType == "PROCESSING_SERVICE") {
						$('#category').find('option').hide();
						$('#category option[value="Error"]').show();
						$('#category option[value="Input"]').show();
						$('#category option[value="Output"]').show();
						$('#category option[value="Archive"]').show();
						$('#category').val('Error');
						$("#rule_list_div").hide();
					} 
					if(serviceType == "PROCESSING_SERVICE") {
						$('#category option[value="Filter"]').show();
						$('#category option[value="Invalid"]').show();
						$('#category option[value="Duplicate"]').show();
					}
					
									
					$('#'+elementId+' option').each(function(index, option) {
					    $(option).remove();
					});
					
					$("#"+elementId).multiselect('destroy');
					
					$.each(responseObject, function(index, responseObject) {
						if (elementId == "serviceInstanceListPopup") {
							var serverInstanceId = JSON.parse(responseObject["serverInstance"])["id"];
							if(currentServerId == serverInstanceId){
								$('#'+elementId).append("<option value='" + serverInstanceId+"-"+responseObject["id"]+ "'>" + responseObject["name"]+ "</option>");
							}
						} else {
							$('#'+elementId).append("<option value='" + responseObject["id"]+ "'>" + responseObject["name"]+ "</option>");
						}
					});
				
					
				} else {
					//resetmultiselectdropdown(elementid);
					showErrorMsg(responseMsg);
				}
			},
			error : function(xhr, st, err) {
				handleGenericError(xhr, st, err);
			}
		});
	}
}
function manageProcessingServiceAttribute(serviceType){
	if(serviceType == "PROCESSING_SERVICE"){
		$('#reprocess_reason_category_div').show();
		$('#reprocess_reason_severity_div').show();
		$('#reprocess_reason_errorcode_div').show();
		$("#rule_list_div").show();
	}else{
		$('#reprocess_reason_category_div').hide();
		$('#reprocess_reason_severity_div').hide();
		$('#reprocess_reason_errorcode_div').hide();
		$("#rule_list_div").hide();
	}
}