
/* Function will set dropdown dyanmic value */
function setDropdownValue(elementId,elementValue){

	$("#"+elementId).val(elementValue);
}


/* function will get mapping list by device and composer type */
function getMappingByDeviceAndComposerType(urlAction,deviceId,pluginType,elementId,flag){

	$.ajax({
		url: urlAction,
		cache: false,
		async: false,
		dataType:'json',
		type:'POST',
		data:
		 {
			"deviceId"     	 : deviceId,	
			"composerType"   : pluginType,
		}, 
		success: function(data){
			var response = eval(data);
			var responseCode = response.code; 
			var responseObject = response.object;
			
			responseObject = eval(responseObject);
			if(responseCode === "200"){
				
				$("#"+elementId).empty();
				if(flag === 'device_block'){
					$("#"+elementId).html("<option value='0' selected='selected'>"+jsSpringMsg.selmappingName+"</option>");
				}
				
				$.each(responseObject, function(index, responseObject) {
			        $("#"+elementId).append("<option value='"+responseObject.id +"'>"+responseObject.name+"</option>");
			    });
				
				if(flag === 'device_block'){
					$("#"+elementId).append("<option value='-2' >"+jsSpringMsg.mappingCustomLabel+"</option>");
				}else{
					$("#"+elementId).append("<option value='-2' >"+jsSpringMsg.mappingNoneLabel+"</option>");
				}
			}else if(responseObject !== undefined || responseObject !== 'undefined' || responseCode === "400"){
				$("#"+elementId).empty();
				if(flag === 'device_block'){
					$("#"+elementId).html("<option value='-2' selected='selected'>"+jsSpringMsg.mappingCustomLabel+"</option>");
					$("#"+elementId).append("<option value='0' >"+jsSpringMsg.selmappingName+"</option>");
					
					$('#composer_association_save_next_btn').attr('disabled',true);
					
				}else{
					$("#"+elementId).html("<option value='-2' selected='selected'>"+jsSpringMsg.mappingNoneLabel+"</option>");
				}
			}
		},
	    error: function (xhr,st,err){
			handleGenericError(xhr,st,err);
		}
	}); 
}

/*Function will get Mapping Details by selected mapping  */
function getMappingDetails(urlAction, mappingId,pluginType){
	$.ajax({
		url: urlAction,
		cache: false,
		async: false,
		dataType:'json',
		type:'POST',
		data:
		 {
			"mappingId"   : mappingId,
			"pluginType"  : pluginType
		 }, 
		success: function(data){
			var response = eval(data);
			var responseCode = response.code; 
			var responseMsg = response.msg; 
			var responseObject = response.object;
			
			
			if(responseCode === "200"){
				console.log("Controller response ::"+responseObject);
				
				var mappingTypeVal = responseObject.mappingType;
				$('#mappingTypeVal').val(mappingTypeVal);
				
				if(pluginType === jsSpringMsg.asciiComposer){
					setAsciiComposerAdvanceDetail(responseObject);
					setAsciiORFixedLengthAsciiComposerHeaderFooterSummary(responseObject);
				}else if(currentComposerType === jsSpringMsg.asn1Composer){
					setAsn1ComposerAdvanceDetail(responseObject);
				}else if(currentComposerType === jsSpringMsg.fixedLengthAsciiComposer){
					setFixedLengthAsciiAdvanceDetail(responseObject);
					setAsciiORFixedLengthAsciiComposerHeaderFooterSummary(responseObject);
				}else if(currentComposerType === jsSpringMsg.tapComposer){
					setTapComposerAdvanceDetail(responseObject);
				}else if(currentComposerType === jsSpringMsg.rapComposer){
					setRapComposerAdvanceDetail(responseObject);
				}else if(currentComposerType === jsSpringMsg.nrtrdeComposer){
					setNrtrdeComposerAdvanceDetail(responseObject);
				}
				
				if($("#mappingName option:selected").text() === jsSpringMsg.mappingCustomLabel){
					$('#composer_association_save_next_btn').attr('disabled',true);
				}else{
					$('#composer_association_save_next_btn').attr('disabled',false);
				}
				
			}else if(responseObject !== undefined || responseObject !== 'undefined' || responseCode === "400"){
				showErrorMsg(responseMsg);
			}else{
				showErrorMsg(responseMsg);	
			}
		},
	    error: function (xhr,st,err){
			handleGenericError(xhr,st,err);
		}
	}); 
}

/* Function will disable the content */
function disableAdvanceDetailsdiv(){
	$("#advance_details_div_mask").remove();
	$('#advance_details_div').fadeTo('slow',.6);
	$element = $('#advance_details_div');
	var top = Math.ceil($element.position().top);
	var elementStr = "<div id='advance_details_div_mask' style='position: absolute;top:"+top+"px;left:0;width: 100%;height:100%;z-index:2;opacity:0.4;filter: alpha(opacity = 50)'></div>";
	$element.append(elementStr);
}

/*Function will enable the mapping details*/
function enableAdvanceDetailsDiv(){
	$("#advance_details_div").css("opacity","");
	$("#advance_details_div_mask").remove();
}

/* Function will disable the content */
function disableHeaderFooterSummaryDiv(){
	$("#header_footer_summary_mask").remove();
	$('#header_footer_summary').fadeTo('slow',.6);
	$element = $('#header_footer_summary');
	var top = Math.ceil($element.position().top);
	var elementStr = "<div id='header_footer_summary_mask' style='position: absolute;top:"+top+"px;left:0;width: 100%;height:100%;z-index:2;opacity:0.4;filter: alpha(opacity = 50)'></div>";
	$element.append(elementStr);
}

/*Function will enable the summary details*/
function enableHeaderFooterSummaryDiv(){
	$("#header_footer_summary").css("opacity","");
	$("#header_footer_summary_mask").remove();
}

function enableFixedLengthAdvanceDetailsDiv(){
	$("#advance_details_div").css("opacity","");
	$("#advance_details_div_mask").remove();
	$("#fieldSeparator").prop('disabled',false);
}

/* Function will associate the composer mapping with composer and also add the client validation for device configuration.*/
function associateMappingWithComposer(){
	
	var mappingTypeVal = $("#mappingTypeVal").val();
	$("#selectedMappingType").val(mappingTypeVal);
	var isValidated = validateDeviceConfig();
	
	if(isValidated){
		
		$("#id").val($("#mappingName").val());
		$("#mappingDeviceId").val($("#deviceName").val()); //setting device id in submit form as its required to in audit for device details.
		
		$("#selecteMappingName").val($("#mappingName option:selected").text());
		$("#selecteDeviceName").val($("#deviceName option:selected").text());
		
		$("#selDeviceId").val($("#deviceName option:selected").val());
		$("#selMappingId").val($("#mappingName option:selected").val());
		$("#selVendorTypeId").val($("#vendorType option:selected").val());
		$("#selDeviceTypeId").val($("#deviceType option:selected").val());
		 if(currentComposerType === jsSpringMsg.asciiComposer){
			$("#ascii-composer-configuration-form").attr("action",'updateAsciiComposerConfiguration');
			$("#ascii-composer-configuration-form").submit();
		 } else if(currentComposerType === jsSpringMsg.asn1Composer){
			$("#ascii-composer-configuration-form").attr("action",'updateAsn1ComposerConfiguration');
			$("#ascii-composer-configuration-form").submit();
		} else if(currentComposerType === jsSpringMsg.fixedLengthAsciiComposer){
			$("#ascii-composer-configuration-form").attr("action",'updateFixedLengthAsciiComposerConfiguration');
			$("#ascii-composer-configuration-form").submit();
		} else if(currentComposerType === jsSpringMsg.tapComposer){
			$("#ascii-composer-configuration-form").attr("action",'updateTapComposerConfiguration');
			$("#ascii-composer-configuration-form").submit();
		} else if(currentComposerType === jsSpringMsg.rapComposer){
			$("#ascii-composer-configuration-form").attr("action",'updateRapComposerConfiguration');
			$("#ascii-composer-configuration-form").submit();
		} else if(currentComposerType === jsSpringMsg.nrtrdeComposer){
			$("#ascii-composer-configuration-form").attr("action",'updateNrtrdeComposerConfiguration');
			$("#ascii-composer-configuration-form").submit();
		}
		 if(currentComposerType === jsSpringMsg.xmlComposer){
				$("#selDeviceId").val($("#deviceName option:selected").val());
				$("#selMappingId").val($("#mappingName option:selected").val());
				$("#selVendorTypeId").val($("#vendorType option:selected").val());
				$("#selDeviceTypeId").val($("#deviceType option:selected").val());
				$("#xml-composer-configuration-form").attr("action",'updateXMLComposerConfiguration');
				$("#xml-composer-configuration-form").submit();
			}
	}
}

function validateDeviceConfig(){
	if($("#deviceType").val() === '-1'){
		showErrorMsg(jsSpringMsg.invalidDeviceType);
		return false;
	}else if($("#vendorType").val() === '-1'){
		showErrorMsg(jsSpringMsg.invalidVendorType);
		return false;
	}else if($("#deviceName").val() === '-1'){
		showErrorMsg(jsSpringMsg.invalidDevice);
		return false;
	}else if($("#mappingName").val() === '-1' || $("#mappingName").val() === '0'){
		showErrorMsg(jsSpringMsg.invalidMapping);
		return false;
	}
	 resetWarningDisplay();
	 clearAllMessages();
	return true;
}


/*Function will create new or update existing created mapping */
function createOrUpdateMappingDetails(){
	console.log("Mapping Action Type :: "+$("#mappingActionType").val())
	if($("#mappingActionType").val() === 'EDIT'){
		
		if(currentComposerType === jsSpringMsg.asciiComposer){
			enableAdvanceDetailsDiv();
			enableHeaderFooterSummaryDiv();
		}
		else if(currentComposerType === jsSpringMsg.asn1Composer){
			enableAsn1AdvanceDetails();
		}else if(currentComposerType === jsSpringMsg.fixedLengthAsciiComposer){
			enableFixedLengthAdvanceDetailsDiv();
		}else if(currentComposerType === jsSpringMsg.tapComposer || currentComposerType === jsSpringMsg.rapComposer || currentComposerType === jsSpringMsg.nrtrdeComposer){
			enableRoamingAdvanceDetails();
		}
		closeFancyBox();
	}else{

		$("#create_mapping_proccess_bar_div").show();
		$("#close-create-mapping-button-div").hide();
		$("#create_mapping_button_div").hide();
		
		$.ajax({
			url: 'createUpdateComposerMappingDetail',
			cache: false,
			async: true,
			dataType:'json',
			type:'POST',
			data:
			 {
				"id" 		  : $("#createMappingId").val(),
				"actionType"  : $("#mappingActionType").val(),
				"name" 		  : $("#name").val(),
				"mappingId"   : $("#create_mapping_base_mapping_id").val(),
				"device.id"   : $('#deviceName').val(),
				"pluginType"  : currentComposerType
				
			 }, 
			success: function(data){
				var response = eval(data);
				var responseCode = response.code; 
				var responseMsg = response.msg; 
				var responseObject = response.object;
			
				resetWarningDisplayPopUp();
				$('#composer_association_save_next_btn').attr('disabled',false);
				
				if(responseCode === "200"){
				
	
					$("#create_mapping_proccess_bar_div").hide();
					$("#close-create-mapping-button-div").show();
					$("#create_mapping_button_div").hide();
					if(currentComposerType === jsSpringMsg.asciiComposer){
						enableAdvanceDetailsDiv();
						enableHeaderFooterSummaryDiv();
						setAsciiComposerAdvanceDetail(responseObject);
						setAsciiORFixedLengthAsciiComposerHeaderFooterSummary(responseObject);
					}else if(currentComposerType === jsSpringMsg.asn1Composer){
						enableAsn1AdvanceDetails();
						setAsn1ComposerAdvanceDetail(responseObject);
					}else if(currentComposerType === jsSpringMsg.fixedLengthAsciiComposer){
						enableFixedLengthAdvanceDetailsDiv();
						enableHeaderFooterSummaryDiv();
						setFixedLengthAsciiAdvanceDetail(responseObject);
						setAsciiORFixedLengthAsciiComposerHeaderFooterSummary(responseObject);
					}else if(currentComposerType === jsSpringMsg.tapComposer){
						enableRoamingAdvanceDetails();
						setTapComposerAdvanceDetail(responseObject);
					}else if(currentComposerType === jsSpringMsg.rapComposer){
						enableRoamingAdvanceDetails();
						setRapComposerAdvanceDetail(responseObject);
					}else if(currentComposerType === jsSpringMsg.nrtrdeComposer){
						enableRoamingAdvanceDetails();
						setNrtrdeComposerAdvanceDetail(responseObject);
					}
					getMappingByDeviceAndComposerType('getComposerMappingListByDevice',$('#deviceName').val(),currentComposerType,'mappingName','device_block');
					setDropdownValue('mappingName',responseObject["id"]);
					closeFancyBoxFromChildIFrame();
					showSuccessMsg(responseMsg);
					
				}else if(responseObject !== undefined && responseObject !== 'undefined' && responseCode === "400"){
					addErrorIconAndMsgForAjaxPopUp(responseObject);
					$("#create_mapping_proccess_bar_div").hide();
					$("#close-create-mapping-button-div").hide();
					$("#create_mapping_button_div").show();
					
				}else{

					showErrorMsgPopUp(responseMsg);
					
					$("#create_mapping_proccess_bar_div").hide();
					$("#close-create-mapping-button-div").hide();
					$("#create_mapping_button_div").show();
				}
			},
		    error: function (xhr,st,err){
				handleGenericError(xhr,st,err);
			}
		});
	}
}

/* Function will customize selected mapping details with following funcitonality 
 * 	1. create new mapping using default.
 *  2. create new mapping using created custom mapping.
 *  3. update newly created custom mapping.
 *  4. create custom mapping without base mapping.
 *  5. create custom mapping with base mapping.
 */
function customizeMappingDetails(){
	
	var isValidated = validateDeviceConfig();
	var mappingTypeVal = $("#mappingTypeVal").val();
	console.log("Mapping Type Val :: "+ mappingTypeVal);
	if(isValidated){
		resetWarningDisplayPopUp();
		clearAllMessagesPopUp();
		$("#create_mapping_proccess_bar_div").hide();
		$("#close-create-mapping-button-div").hide();
		$("#create_mapping_button_div").show();
		
		$("#customize_selected_deviceType").val($("#deviceType option:selected").text());
		$("#customize_selected_vendorType").val($("#vendorType option:selected").text());
		$("#customize_selected_deviceName").val($("#deviceName option:selected").text());
		$("#customize_selected_mappingName").val($("#mappingName option:selected").text());
		$("#name").val('');
			
		if(mappingTypeVal == 0){
			$("#mappingActionType").val('CREATE');
			$("#createMappingId").val(0);
			$("#create_mapping_base_mapping_id").val($("#mappingName").val());
			$("#base_mapping_div").hide();
			$("#selected_mapping_association_details_div").hide();
			$("#actionType").val('UPDATE');
			$('#create_new_mapping').prop('checked', true);
			$('#customize_new_mapping_div').show();
			
			$("#customize_mode_div").show();
			$("input[type=radio][value=update]").attr("disabled",true);			
			$("#selected_mapping_association_details_div").hide();
			
			
		}else if($("#mappingName option:selected").text() === jsSpringMsg.mappingCustomLabel){
			
			getMappingByDeviceAndComposerType('getComposerMappingListByDevice',$("#deviceName").val(),currentComposerType,'base_mapping_name','create_mapping');
			$("#selected_mapping_association_details_div").hide();
			$("#customize_mode_div").hide();
			
			$("#base_mapping_name").val('-2');
			$("#base_mapping_div").show();
			$("#mappingActionType").val('CREATE');
			$("#actionType").val('UPDATE');
			
			$("#selected_mapping_association_details_div").hide();
			$("#create_mapping_base_mapping_id").val($("#base_mapping_name").val());
			
		}else {
			
			getAllAssociationDetails('getComposerMappingAssociationDetail',$("#mappingName").val(),'parserconfig');
			$("#mappingActionType").val('CREATE');
			$("#createMappingId").val(0);
			$("#actionType").val('UPDATE');
			$("#create_mapping_base_mapping_id").val($("#mappingName").val());
			$("#base_mapping_div").hide();
			$("#selected_mapping_association_details_div").hide();
			$('#create_new_mapping').prop('checked', true);
			$('#customize_new_mapping_div').show();
			$("input[type=radio][value=update]").attr("disabled",false);
		}
		$("#customize_mapping_link").click();
	}
}


/* Function will hide show mapping details options based on selected radio. */
function validateMapingOptions(elementValue){
	if(elementValue === 'create'){
		$("#customize_new_mapping_div").show();
		$("#selected_mapping_association_details_div").hide();
		$("#base_mapping_div").hide();
		$("#mappingActionType").val('CREATE');
		$("#actionType").val('NO_ACTION');
	}else{
		$("#mappingActionType").val('EDIT');
		$("#actionType").val('UPDATE');

		$("#customize_new_mapping_div").hide();
		$("#selected_mapping_association_details_div").show();
		$("#base_mapping_div").hide();
	}
	
}

/* Function will add edit link in attribute link */
function updateFormatter(cellvalue, options, rowObject){
	var uniqueId = getUniqueEditIdForAttribute(rowObject);
	return "<a href='#' id='"+uniqueId+"' class='link' onclick=displayAddEditPopup(\'EDIT\','"+rowObject["id"]+"'); ><i class='fa fa-pencil-square-o' aria-hidden='true'></i></a>";
}

/* Function add view more detail icon in grid */
function viewDetailFormatter(cellvalue, options, rowObject){
	return "<a href='#' class='link' onclick=displayAddEditPopup(\'VIEW\','"+rowObject["id"]+"'); ><i class='fa fa-list' aria-hidden='true'></i></a>";

}

/*
 * this function will return unique edit id for attributes
 */
function getUniqueEditIdForAttribute(rowObject) {
	var uniqueId = "editAttribute";
	if(rowObject["destinationField"] && rowObject["destinationField"] !== 'null' && rowObject["destinationField"] !== "") {
		uniqueId += "_" + rowObject["destinationField"];
	}
	if(rowObject["unifiedField"] && rowObject["unifiedField"] !== 'null' && rowObject["unifiedField"] !== "") {
		uniqueId += "_" + rowObject["unifiedField"];
	}
	return uniqueId;
}

/*
 * this function will return unique checkbox id for attributes
 */
function getUniqueCheckboxIdForAttribute(rowObject) {
	var uniqueId = "checkbox";
	if(rowObject["destinationField"] && rowObject["destinationField"] !== 'null' && rowObject["destinationField"] !== "") {
		uniqueId += "_" + rowObject["destinationField"];
	}
	if(rowObject["unifiedField"] && rowObject["unifiedField"] !== 'null' && rowObject["unifiedField"] !== "") {
		uniqueId += "_" + rowObject["unifiedField"];
	}
	return uniqueId;
}

/*
 * this function will handle root checkbox from child checkbox
 */
function updateRootCheckbox(element, gridId) {
	if(!element.checked){
		// if current child checkbox is not checked : uncheck root checkbox
		$('input:checkbox[id="selectAllAttribute"]').prop('checked',false);
	} else {
		//if current child checkboox is checked and all checkbox are checked then check root checkbox
		var count = jQuery("#"+gridId).jqGrid('getGridParam', 'records');
		if ($('input:checkbox[name="attributeCheckbox"]:checked').length === count) {
			$('input:checkbox[id="selectAllAttribute"]').prop('checked',true);
	    }
	}
}

/*
 * this function will handle select all/ de select all event
 */
function attributeHeaderCheckbox(e, element) {
    e = e || event;/* get IE event ( not passed ) */
    e.stopPropagation ? e.stopPropagation() : e.cancelBubble = false;

    //if root checkbox is checked :-> all child checkbox should ne checked
    if(element.checked) {
    	$('input:checkbox[name="attributeCheckbox"]').prop('checked',true);
    } else {
    	//if root checkbox is unchecked :-> all child checkbox should be unchecked
    	$('input:checkbox[name="attributeCheckbox"]').prop('checked',false);
    }
    
}

function displayAddEditPopup(actionType,attributeId){
	
	 clearAllMessagesPopUp();
	 resetWarningDisplayPopUp();
	 resetComposerAttributeParams();
	 enableComposerAttribute();

	if(actionType === 'ADD'){
		$("#addNewAttribute").show();
		$("#editAttributeDistribution").hide();
		$("#viewClose").hide();
		$("#closebtn").show();
		$("#add_label").show();
		$("#update_label").hide();
		$("#view_label").hide();
		
		//$("#replaceConditionList").val('key=value');
		
		$("#id").val(0);
		
		$("#add_edit_attribute").click();
	}else if(actionType === 'EDIT'){
		if(attributeId > 0){
		
			var responseObject='';
			if(currentComposerType === jsSpringMsg.asciiComposer || currentComposerType === jsSpringMsg.asn1Composer || currentComposerType === jsSpringMsg.fixedLengthAsciiComposer || currentComposerType === jsSpringMsg.tapComposer || currentComposerType === jsSpringMsg.rapComposer|| currentComposerType === jsSpringMsg.nrtrdeComposer){
				responseObject = jQuery("#asciiAttributeList").jqGrid ('getRowData', attributeId);
			}else if(currentComposerType === jsSpringMsg.xmlComposer){
				responseObject = jQuery("#xmlAttributeList").jqGrid ('getRowData', attributeId);
			}
			
			$("#editAttributeDistribution").show();
			$("#addNewAttribute").hide();
			$("#viewClose").hide();
			$("#closebtn").show();
			$("#update_label").show();
			$("#add_label").hide();
			$("#view_label").hide();
			$("#seqNo").show();
		
			$("#id").val(responseObject.id);
			$("#sequenceNumber").val(responseObject.sequenceNumber);
			$("#destinationField").val(responseObject.destinationField);
			$("#description").val(responseObject.description);
			$("#defualtValue").val(responseObject.defualtValue);
			$("#unifiedField").val(responseObject.unifiedField);
			$("#dataType").val(responseObject.dataType);
			if(currentComposerType === jsSpringMsg.asciiComposer){
				$("#dateFormat").val(responseObject.dateFormat);
				$("#trimchars").val(responseObject.trimchars);
				$("#trimPosition").val(responseObject.trimPosition);
				$("#replaceConditionList").val(responseObject.replaceConditionList);
				$("#paddingEnable").val(responseObject.paddingEnable);
				var element = document.getElementById('paddingEnable');
				    element.value = responseObject.paddingEnable;
				$("#length").val(responseObject.length);
				$("#paddingType").val(responseObject.paddingType);
				$("#paddingChar").val(responseObject.paddingChar);
				$("#prefix").val(responseObject.prefix);
				$("#suffix").val(responseObject.suffix);
				$("#asn1DataType").val(responseObject.asn1DataType);
				$("#argumentDataType").val(responseObject.argumentDataType);
				$("#choiceId").val(responseObject.choiceId);
				$("#childAttributes").val(responseObject.childAttributes);
				$("#destFieldDataFormat").val(responseObject.destFieldDataFormat);
			}else if(currentComposerType === jsSpringMsg.fixedLengthAsciiComposer){
				$("#trimchars").val(responseObject.trimchars);
				$("#fixedLengthDateFormat").val(responseObject.fixedLengthDateFormat);
				$("#fixedLength").val(responseObject.fixedLength);
				$("#paddingType").val(responseObject.paddingType);
				$("#paddingChar").val(responseObject.paddingChar);
				$("#prefix").val(responseObject.prefix);
				$("#suffix").val(responseObject.suffix);
				$("#fixedDataType").val(responseObject.dataType);
				diasbleFixedLengthDateFormate();
			}else if(currentComposerType === jsSpringMsg.asn1Composer){
				$("#argumentDataType").val(responseObject.argumentDataType);
				$("#choiceId").val(responseObject.choiceId);
				$("#childAttributes").val(responseObject.childAttributes);
				$("#destFieldDataFormat").val(responseObject.destFieldDataFormat);
				$("#asn1DataType").val(responseObject.asn1DataType);
				$("#trimchars").val(responseObject.trimchars);
			}else if(currentComposerType === jsSpringMsg.tapComposer || currentComposerType === jsSpringMsg.rapComposer || currentComposerType === jsSpringMsg.nrtrdeComposer){
				$("#argumentDataType").val(responseObject.argumentDataType);
				$("#choiceId").val(responseObject.choiceId);
				$("#childAttributes").val(responseObject.childAttributes);
				$("#destFieldDataFormat").val(responseObject.destFieldDataFormat);
				$("#asn1DataType").val(responseObject.asn1DataType);
				$("#trimchars").val(responseObject.trimchars);
				$("#composeFromJsonEnable").val(responseObject.composeFromJson);
				$("#cloneRecordEnable").val(responseObject.cloneRecord);
			}
			$("#plugInType").val(currentComposerType);
			changeDataType();
			changePaddingParamter();
			
			$("#add_edit_attribute").click();
		}else{
			showErrorMsg(jsSpringMsg.failUpdateAtributeMsg);
		}
	}else{
		$("#editAttributeDistribution").hide();
		$("#addNewAttribute").hide();
		$("#update_label").hide();
		$("#add_label").hide();
		$("#view_label").show();
		$("#viewClose").show();
		$("#closebtn").hide();
		$("#seqNo").show();
		
		if(attributeId > 0){
			if(currentComposerType === jsSpringMsg.asciiComposer || currentComposerType === jsSpringMsg.asn1Composer || currentComposerType === jsSpringMsg.fixedLengthAsciiComposer || currentComposerType === jsSpringMsg.tapComposer || currentComposerType === jsSpringMsg.rapComposer || currentComposerType === jsSpringMsg.nrtrdeComposer){
				responseObject = jQuery("#asciiAttributeList").jqGrid ('getRowData', attributeId);
			}else if(currentComposerType === jsSpringMsg.xmlComposer){
				responseObject = jQuery("#xmlAttributeList").jqGrid ('getRowData', attributeId);
			}

			
			$("#id").val(responseObject.id);
			$("#sequenceNumber").val(responseObject.sequenceNumber);
			$("#destinationField").val(responseObject.destinationField);
			$("#description").val(responseObject.description);
			$("#defualtValue").val(responseObject.defualtValue)
			$("#unifiedField").val(responseObject.unifiedField);
			$("#dataType").val(responseObject.dataType);
			$("#dateFormat").val(responseObject.dateFormat);
			$("#trimchars").val(responseObject.trimchars);
			$("#trimPosition").val(responseObject.trimPosition);
			$("#replaceConditionList").val(responseObject.replaceConditionList);
			$("#paddingEnable").val(responseObject.paddingEnable);
			$("#length").val(responseObject.length);
			$("#paddingType").val(responseObject.paddingType);
			$("#paddingChar").val(responseObject.paddingChar);
			$("#asn1DataType").val(responseObject.asn1DataType);
			$("#argumentDataType").val(responseObject.argumentDataType);
			$("#choiceId").val(responseObject.choiceId);
			$("#childAttributes").val(responseObject.childAttributes);
			$("#destFieldDataFormat").val(responseObject.destFieldDataFormat);
			$("#prefix").val(responseObject.prefix);
			$("#suffix").val(responseObject.suffix);
			$("#fixedDataType").val(responseObject.dataType);
			$("#fixedLengthDateFormat").val(responseObject.fixedLengthDateFormat);
			$("#fixedLength").val(responseObject.fixedLength);
			$("#composeFromJsonEnable").val(responseObject.composeFromJson);
			$("#cloneRecordEnable").val(responseObject.cloneRecord);
			if(currentComposerType !== jsSpringMsg.fixedLengthAsciiComposer){
				disableComposerAttrParam();
			}
			$("#add_edit_attribute").click();
			
		}
	}
}

/*Function will reset all composer attribute params */
function resetComposerAttributeParams(){
	
	$("#sequenceNumber").val('');
	$("#destinationField").val('');
	$("#description").val('');
	$("#defualtValue").val('');
	$("#dateFormat").val('');
	$("#fixedLengthDateFormat").val('');
	$("#trimchars").val('');
	$("#fixedLength").val('');
	$("#replaceConditionList").val('');
	$("#length").val('');
	$("#paddingChar").val('');
	$("#prefix").val('');
	$("#suffix").val('');
	$("#unifiedField").val('');
	$("#dataType").val('');
	$("#destFieldDataFormat").val('');
	$("#argumentDataType").val('');
	$("#choiceId").val('');
	$("#childAttributes").val('');
	$("#asn1DataType").val('');
	$("#composeFromJsonEnable").val('false');
	$("#cloneRecordEnable").val('false');
}

function disableComposerAttrParam(){
	
	$("#destinationField").prop('readonly', true);
	$("#description").prop('readonly', true);
	$("#defualtValue").prop('readonly', true);
	$("#unifiedField").prop('disabled', true);
	$("#dataType").prop('disabled', true);
	$("#dateFormat").prop('readonly', true);
	$("#trimchars").prop('readonly', true);
	$("#trimPosition").prop('disabled', true);
	$("#replaceConditionList").prop('readonly', true);
	$("#paddingEnable").prop('disabled', true);
	$("#length").prop('readonly', true);
	$("#paddingType").prop('disabled', true);
	$("#paddingChar").prop('readonly', true);
	$("#prefix").prop('readonly', true);
	$("#suffix").prop('readonly', true);
	$("#asn1DataType").prop('readonly',true);
	$("#argumentDataType").prop('disabled',true);
	$("#choiceId").prop('readonly',true);
	$("#childAttributes").prop('readonly',true);
	$("#destFieldDataFormat").prop('disabled',true);
}

function enableComposerAttribute(){
	
	$("#destinationField").prop('readonly', false);
	$("#description").prop('readonly', false);
	$("#defualtValue").prop('readonly', false);
	$("#unifiedField").prop('disabled', false);
	$("#dataType").prop('disabled', false);
	$("#trimchars").prop('readonly', false);
	$("#trimPosition").prop('disabled', false);
	$("#replaceConditionList").prop('readonly', false);
	$("#paddingEnable").prop('disabled', false);
	$("#asn1DataType").prop('readonly',false);
	$("#argumentDataType").prop('disabled',false);
	$("#choiceId").prop('readonly',false);
	$("#childAttributes").prop('readonly',false);
	$("#destFieldDataFormat").prop('disabled',false);
}

/* Function will disable parser attribute details page */
function disableAttributeDetail(){
	$("#attribute_details_div *").attr("disabled", "disabled").off('click');
	
	$('#attribute_details_div').fadeTo('slow',.6);
	$('#attribute_details_div').append("<div id='attribute_details_div_mask' style='position: absolute;top:0;left:0;z-index:2;opacity:0.4;filter: alpha(opacity = 50)'></div>");
	 
	$('#attribute_details_div a').attr('onclick','void(0)');
	
}

/*Function will add/edit composer attribute details */
function createUpdateComposerAttribute(urlAction, attributeId, actionType){
	var fixedLength='';
	var paddingLength;
	var dataType = $("#dataType").val();
	var dateFormat = $("#dateFormat").val();
	var seqNumber=$("#sequenceNumber").val();
	if(seqNumber == ''){
		seqNumber = '0';
	}
	if($("#length").val() == ''){
		paddingLength='0';
	}else{
		paddingLength=$("#length").val();
	}
	if(urlAction === 'addEditFixedLengthAsciiComposerAttribute'){
		fixedLength=$("#fixedLength").val();
		
		if(fixedLength == ''){
			fixedLength='0';
		}
		dataType=$("#fixedDataType").val();
	}
	$.ajax({
		url: urlAction,
		cache: false,
		async: true,
		dataType:'json',
		type:'POST',
		data:
		 {
			"id" 					: $("#id").val(),
			"sequenceNumber"   		: seqNumber,
			"destinationField"   	: $("#destinationField").val(),
			"unifiedField"  		: $("#unifiedField").val() ,
			"description"   		: $("#description").val(),
			"defualtValue" 	 		: $("#defualtValue").val(),
			"dataType"				: dataType,
			"dateFormat"			: dateFormat,
			"trimchars" 			: $("#trimchars").val(),
			"trimPosition" 			: $("#trimPosition").val(),
			"replaceConditionList" 	: $("#replaceConditionList").val(),
			"paddingEnable" 		: $("#paddingEnable").val(),
			"asn1DataType"			: $("#asn1DataType").val(),
 			"length" 				: paddingLength,
			"paddingType" 			: $("#paddingType").val(),
			"paddingChar" 			: $("#paddingChar").val(),
			"prefix" 				: $("#prefix").val(),
			"suffix" 				: $("#suffix").val(),
			"actionType" 			: actionType,
			"plugInType"    		: $("#selplugInType").val(),
			"mappingId"    			: $("#selConfigMappingId").val(),
			"myComposer.id"    		: $("#selConfigMappingId").val(),
			"destFieldDataFormat"   : $("#destFieldDataFormat").val(),
			"argumentDataType"		: $("#argumentDataType").val(),
			"choiceId"				: $("#choiceId").val(),
			"childAttributes"		: $("#childAttributes").val(),
			"attributeType"			: $("#attributeTypeHidden").val(),
			"fixedLengthDateFormat" : $("#fixedLengthDateFormat").val(),
			"fixedLength"			: fixedLength,
			"composeFromJsonEnable" : $("#composeFromJsonEnable").val(),
			"cloneRecordEnable"     : $("#cloneRecordEnable").val(),
		}, 
		success: function(data){
			var response = eval(data);
			var responseCode = response.code; 
			var responseMsg = response.msg; 
			var responseObject = response.object;
		
			clearAllMessagesPopUp();
			resetWarningDisplayPopUp();
			 
			if(responseCode === "200"){
				$("#editAttributeDistribution").hide();
				$("#addNewAttribute").hide();
				reloadAttributeGridData();
				closeFancyBox();
				showSuccessMsg(responseMsg);
				resetComposerAttributeParams();
			}else if(responseObject !== undefined && responseObject !== 'undefined' && responseCode === "400"){
				addErrorIconAndMsgForAjaxPopUp(responseObject);
			}else{
				showErrorMsgPopUp(responseMsg);
			}
		},
	    error: function (xhr,st,err){
			handleGenericError(xhr,st,err);
		}
	});
}

/*Function will reload attribute grid after add/edit/delete action. */
function reloadAttributeGridData(){
	clearAllMessages();
	
	var $grid='';
	if(currentComposerType === jsSpringMsg.asciiComposer || currentComposerType === jsSpringMsg.asn1Composer || currentComposerType === jsSpringMsg.fixedLengthAsciiComposer || currentComposerType === jsSpringMsg.tapComposer || currentComposerType === jsSpringMsg.rapComposer || currentComposerType === jsSpringMsg.nrtrdeComposer){
		$grid = $("#asciiAttributeList");
		jQuery('#asciiAttributeList').jqGrid('clearGridData');
		clearAttributeGrid();	
		getAllAsciiAttributeListByMappingId(urlAction, selMappingId);
	}else if(currentComposerType === jsSpringMsg.xmlComposer){
		$grid = $("#xmlAttributeList");
		jQuery('#xmlAttributeList').jqGrid('clearGridData');
		clearAttributeGrid();
		getAllXMLAttributeListByMappingId(urlAction,selMappingId);
	}	

	/*$grid.jqGrid('setGridParam',{datatype:'json',page: 1,sortname: 'sequenceNumber',sortorder: "asc", postData:{
	  
   		'mappingId': function () {
	        return selMappingId;
   		},
   		'plugInType':currentComposerType,
	}}).trigger('reloadGrid');*/
	
	
	

}


function clearAttributeGrid(){
	
	var $grid='';
	if(currentComposerType === jsSpringMsg.asciiComposer || currentComposerType === jsSpringMsg.asn1Composer || currentComposerType === jsSpringMsg.fixedLengthAsciiComposer || currentComposerType === jsSpringMsg.tapComposer || currentComposerType === jsSpringMsg.rapComposer || currentComposerType === jsSpringMsg.nrtrdeComposer){
		$grid = $("#asciiAttributeList");
	}else if(currentComposerType === jsSpringMsg.xmlComposer){
		$grid = $("#xmlAttributeList");
	}		
	var rowIds = $grid.jqGrid('getDataIDs');
	// iterate through the rows and delete each of them
	for(var i=0,len=rowIds.length;i<len;i++){
		var currRow = rowIds[i];
		$grid.jqGrid('delRowData', currRow);
	}
}

/*Function will display delete attribute popup */
function displayDeleteAttributePopup(){
	
	//get all selected checkbox array from checkbox name
	ckIntanceSelected = [];
    $.each($("input[name='attributeCheckbox']:checked"), function(){            
    	ckIntanceSelected.push($(this).val());
    });

	clearAllMessagesPopUp();
	resetWarningDisplayPopUp();
	clearAllMessages();
	
	if(ckIntanceSelected.length === 0){
		$("#lessWarn").show();
		$("#moreWarn").hide();
		$("#delete_warn_msg_link").click();
		return;
	}else{
		
		var tableString ="<table class='table table-hover table-bordered'  border='1' ><th>#</th>";
		if(currentComposerType !== jsSpringMsg.asn1Composer){
			tableString+="<th>" + jsSpringMsg.srno + "</th>";
		}
		if(currentComposerType !== jsSpringMsg.fixedLengthAsciiComposer){
			tableString+="<th>"+jsSpringMsg.destinationField+"</th>";
		}
		tableString+="<th>"+jsSpringMsg.unifiedFieldName+"</th><th>"+jsSpringMsg.description+"</th>";
		
		for(var i = 0 ; i< ckIntanceSelected.length;i++){
			
			var rowData='';
			
				
			if(currentComposerType === jsSpringMsg.asciiComposer || currentComposerType === jsSpringMsg.asn1Composer || currentComposerType === jsSpringMsg.fixedLengthAsciiComposer || currentComposerType === jsSpringMsg.tapComposer || currentComposerType === jsSpringMsg.rapComposer || currentComposerType === jsSpringMsg.nrtrdeComposer){
				rowData = $('#asciiAttributeList').jqGrid('getRowData', ckIntanceSelected[i]);
			}else if(currentComposerType === jsSpringMsg.xmlComposer){
				rowData = $('#xmlAttributeList').jqGrid('getRowData', ckIntanceSelected[i]);
			}
			
			tableString += "<tr>";
			tableString += "<td><input type='checkbox' name='attribute_delete' id='attribute_"+ckIntanceSelected[i]+"' checked  onclick=getSelecteAttributeList('"+ckIntanceSelected[i]+"')  value='"+ckIntanceSelected[i]+"' /> </td>";
			if(currentComposerType !== jsSpringMsg.asn1Composer){
				tableString += "<td>"+rowData.sequenceNumber+" </td>";
			}
			if(currentComposerType !== jsSpringMsg.fixedLengthAsciiComposer){
				tableString += "<td>"+rowData.destinationField+" </td>";
			}
			tableString += "<td>"+rowData.unifiedField+"</td>";
			tableString += "<td>"+rowData.description+"</td>";
			tableString += "</tr>";
		}	
		
		tableString+="</table>";
		
		$("#delete_selected_attribute_details").html(tableString);
		$("#delete_attribute_bts_div").show();
		$("#delete_attribute_progress_bar_div").hide();
		$("#delete_close_attribute_buttons_div").hide();
		$("#deleteAttributeId").val(ckIntanceSelected.toString());
		$("#delete_attribute").click();
	}
}

function getSelecteAttributeList(elementId){
	
	if( document.getElementById("attribute_"+elementId).checked === true){
		if(ckIntanceSelected.indexOf(elementId) === -1){
			ckIntanceSelected.push(elementId);
		}
	}else{
		if(ckIntanceSelected.indexOf(elementId) !== -1){
			ckIntanceSelected.splice(ckIntanceSelected.indexOf(elementId), 1);
		}
	}
	$("#deleteAttributeId").val(ckIntanceSelected.toString());
	
}

/*Function will delete attribute  */
function deleteAttribute(){
	$("#delete_attribute_bts_div").hide();
	$("#delete_attribute_progress_bar_div").show();
	$("#delete_close_attribute_buttons_div").hide();
	
	$.ajax({
		url: 'deleteComposerAttribute',
		cache: false,
		async: true,
		dataType:'json',
		type:'POST',
		data:
		 {
			"attributeId" : $("#deleteAttributeId").val()
		 }, 
		success: function(data){
			var response = eval(data);
			var responseCode = response.code; 
			var responseMsg = response.msg; 
		
			clearAllMessagesPopUp();
			resetWarningDisplayPopUp();
			 
			if(responseCode === "200"){
				
				showSuccessMsgPopUp(responseMsg);
				$("#delete_attribute_bts_div").hide();
				$("#delete_attribute_progress_bar_div").hide();
				$("#delete_close_attribute_buttons_div").show();
				ckIntanceSelected = new Array();
			}else{
				showErrorMsgPopUp(responseMsg);
				$("#delete_attribute_bts_div").show();
				$("#delete_attribute_progress_bar_div").hide();
				$("#delete_close_attribute_buttons_div").hide();
			}
		},
	    error: function (xhr,st,err){
			handleGenericError(xhr,st,err);
		}
	});
}

/*Function will redirect user to service dashboard page from breadth crum link */
function goToService(){
	$("#go-to-service-form").submit();
}

function goToDriverPage(){
	$("#go-to-driver-form").submit();
}

/*Function will update order composer attribute details */
function reorderComposerAttribute(urlAction){
	var rowData = jQuery("#asciiAttributeList").jqGrid('getRowData');
	var jsonFormattedData = JSON.stringify(rowData);
	$.ajax({
		url: urlAction,
		cache: false,
		async: true,
		dataType:'json',
		type:'POST',
		data:  {
			"plugInType"    	: $("#selplugInType").val(),
			"mappingId"    		: $("#selConfigMappingId").val(),
			"attributeType"		: $("#attributeType").val(),
			"attributeListJson": jsonFormattedData,
		}, 
		success: function(data){
			var response = eval(data);
			var responseCode = response.code; 
			var responseMsg = response.msg; 
			var responseObject = response.object;
			clearAllMessagesPopUp();
			resetWarningDisplayPopUp();
			if(responseCode === "200"){
				showSuccessMsg(responseMsg);
			}else if(responseObject !== undefined && responseObject !== 'undefined' && responseCode === "400"){
				addErrorIconAndMsgForAjaxPopUp(responseObject);
			}else{
				showErrorMsgPopUp(responseMsg);
			}
		},
	    error: function (xhr,st,err){
			handleGenericError(xhr,st,err);
		}
	});
}

function setAsciiORFixedLengthAsciiComposerHeaderFooterSummary(responseObject){
	$("#fileHeaderSummaryEnable").val(responseObject.fileHeaderSummaryEnable.toString());	
	$("#fileFooterSummaryEnable").val(responseObject.fileFooterSummaryEnable.toString());
	if(responseObject.fileHeaderSummary == null){
		responseObject.fileHeaderSummary = "";
	}
	if(responseObject.fileFooterSummary == null){
		responseObject.fileFooterSummary = "";
	}
	$("#fileHeaderSummary").val(responseObject.fileHeaderSummary.toString());	
	$("#fileFooterSummary").val(responseObject.fileFooterSummary.toString());
	enableDisableSummaryFields('fileHeaderSummaryEnable');
	enableDisableSummaryFields('fileFooterSummaryEnable');
	
}

function enableDisableSummaryFields(fileHeaderOrFooterSummaryID){
	if(fileHeaderOrFooterSummaryID == 'fileHeaderSummaryEnable'){
		if($("#fileHeaderSummaryEnable").val() == 'true'){
			$("#fileHeaderSummary").prop('disabled',false);
			$("#fileHeaderSummaryFunction").prop('disabled',false);
		} else {
			$("#fileHeaderSummary").prop('disabled',true);
			$("#fileHeaderSummaryFunction").prop('disabled',true);
			
		}
	} else {
		if($("#fileFooterSummaryEnable").val() == 'true'){
			$("#fileFooterSummary").prop('disabled',false);
			$("#fileFooterSummaryFunction").prop('disabled',false);
		} else {
			$("#fileFooterSummary").prop('disabled',true);
			$("#fileFooterSummaryFunction").prop('disabled',true);
		}
	}
}

function appendValueToSummaryArea(functionId,areaId){
	if($('#'+areaId).val() != 'undefined' && $('#'+areaId).val() != ''){
		var textareaValue = $('#'+areaId).val(); 
		$('#'+areaId).val(textareaValue + $('#'+functionId).val());	
	}else{
		$('#'+areaId).val($('#'+functionId).val());
	}
}

function downloadExcel(selMappingId,selPlugInType) {
	$("#exportedMappingId").val(selMappingId);
	$("#plugInType").val(selPlugInType);
	$("#downloadExcelFileForm").submit();
}
