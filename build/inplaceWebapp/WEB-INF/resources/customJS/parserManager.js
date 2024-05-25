var editAttributeBtnIdForParser = "editAttributeForParser";
var groupAttCounter = -1;
/*Function will redirect user to service dashboard page from breadth crum link */
function goToService(){
	$("#go-to-service-form").submit();
}


function getMappingByDeviceTypeAndParserType(deviceTypeId,parserType,elementId){
	$.ajax({
		url: 'getMappingListByDeviceType',
		cache: false,
		async: false,
		dataType:'json',
		type:'POST',
		data:
		 {
			"deviceTypeId"     : deviceTypeId,	
			"parserType"   : parserType,
		}, 
		success: function(data){
			var response =  data;
			var responseCode = response.code; 
			var responseObject = response.object;
			$("#"+elementId).empty();
			if(responseCode === "200"){
				$("#"+elementId).append("<option value='-2' >"+jsSpringMsg.mappingNoneLabel+"</option>");
				$.each(responseObject, function(index, responseObject) {
			        $("#"+elementId).append("<option value='"+responseObject[0] +"'>"+responseObject[1]+"</option>");
			    });
				
			}else {
				$("#"+elementId).html("<option value='-2' selected='selected'>"+jsSpringMsg.mappingNoneLabel+"</option>");
			}
		},
	    error: function (xhr,st,err){
			handleGenericError(xhr,st,err);
		}
	}); 
}

/* function will get mapping list by device and parser type */
function getMappingByDeviceAndParserType(urlAction,deviceId,parserType,elementId,flag){
	
	$.ajax({
		url: urlAction,
		cache: false,
		async: false,
		dataType:'json',
		type:'POST',
		data:
		 {
			"deviceId"     : deviceId,	
			"parserType"   : parserType,
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
					
					$('#parser_association_save_next_btn').attr('disabled',true);
					
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

/* Function will make all option template value read only */
function enableDisableOptionTemplateConfig(elementValue){
	
	if(elementValue === 'true'){
		$("#service-optionCopyTofield").attr('readonly', false);
		$("#service-optionTemplateId").attr('readonly', false);
		$("#service-optionTemplateKey").attr('readonly', false);
		$("#service-optionTemplateValue").attr('readonly', false);
		$("#service-optionCopytoTemplateId").attr('readonly', false);
	}else{
		$("#service-optionCopyTofield").attr('readonly', true);
		$("#service-optionTemplateId").attr('readonly', true);
		$("#service-optionTemplateKey").attr('readonly', true);
		$("#service-optionTemplateValue").attr('readonly', true);
		$("#service-optionCopytoTemplateId").attr('readonly', true);
	}
}

/* Function will toggle filter values read/write */
function enableDisableFilterConfig(elementValue){
	
	if(elementValue === 'true'){
		$("#service-filterProtocol").attr('disabled', false);
		$("#service-filterTransport").attr('disabled', false);
		$("#service-filterPort").attr('disabled', false);
		$('.multiselect').removeAttr('disabled');
  		$('.multiselect').removeClass('disabled');
	}else{
		$("#service-filterProtocol").attr('disabled', true);
		$("#service-filterTransport").attr('disabled', true);
		$("#service-filterPort").attr('disabled', true);
		$('.multiselect').attr('disabled', true);
  		$('.multiselect').css('disabled');
	}
}

/* Remove disabled attribute so that fields data sent to server */
const form = $('#natflow-parser-configuration-form');
form.submit(function(event){
     $('#service-filterProtocol').removeAttr('disabled');
     $('#service-filterTransport').removeAttr('disabled');
     $('#service-filterPort').removeAttr('disabled');
});

/* Disable Device Details */
function disableDeviceDetails(){
	$('#device_details_div').fadeTo('slow',.6);
	$('#device_details_div').append("<div id='device_details_div_mask' style='z-index:2;opacity:0.4;'></div>");
	
}

/*Function will enable device details */
function enableDeviceDetails(){
	$("#device_details_div").css("opacity","");
	$("#device_details_div_mask").remove();
}

/* Function will disable the content */
function disableMappingDetails(){
	
	//Remove Previous Mask if exist on change event.
	$("#advance_template_details_div_mask").remove(); 
	$("#option_template_details_div_mask").remove();
	$("#filter_details_div_mask").remove();
	
	
	$('#advance_template_details_div').fadeTo('slow',.6);
	$element = $('#advance_template_details_div');
	
	var top = Math.ceil($element.position().top);
	var elementStr = "<div id='advance_template_details_div_mask' style='position: absolute;top:"+top+"px;left:0;width: 100%;height:100%;z-index:2;opacity:0.4;filter: alpha(opacity = 50)'></div>";
	$element.append(elementStr); 
	
	$('#option_template_details_div').fadeTo('slow',.6);
	$element = $('#option_template_details_div');
	var top = Math.ceil($element.position().top);
	var elementStr = "<div id='option_template_details_div_mask' style='position: absolute;top:"+top+"px;left:0;width: 100%;height:100%;z-index:2;opacity:0.4;filter: alpha(opacity = 50)'></div>";
	$element.append(elementStr);
	
	$('#filter_details_div').fadeTo('slow',.6);
	$element = $('#filter_details_div');
	var top = Math.ceil($element.position().top);
	var elementStr = "<div id='filter_details_div_mask' style='position: absolute;top:"+top+"px;left:0;width: 100%;height:100%;z-index:2;opacity:0.4;filter: alpha(opacity = 50)'></div>";
	$element.append(elementStr);
}

/*Function will enable the mapping details*/
function enableMappingDetails(){
	
	$("#advance_template_details_div").css("opacity","");
	
	$("#advance_template_details_div_mask").remove(); 
	
	$("#option_template_details_div").css("opacity","");
	$("#option_template_details_div_mask").remove();
	
	$("#filter_details_div").css("opacity","");
	$("#filter_details_div_mask").remove();
	
}

/* Function will disable parser attribute details page */
function disableAttributeDetail(){

	$("#attribute_details_div_mask").remove(); 
	
	$('#attribute_details_div').fadeTo('slow',.6);
	$element = $('#attribute_details_div');

	var top = Math.ceil($element.position().top);
	var elementStr = "<div id='attribute_details_div_mask' style='position: absolute;top:"+top+"px;left:0;width: 100%;height:100%;z-index:2;opacity:0.6;filter: alpha(opacity = 50)'></div>";
	$element.append(elementStr); 
	
}



/*Function will get Mapping Details by selected mapping  */
function getMappingDetails(urlAction, mappingId,parserType){
	$.ajax({
		url: urlAction,
		cache: false,
		async: false,
		dataType:'json',
		type:'POST',
		data:
		 {
			"mappingId"   : mappingId,
			"pluginType"  : parserType
		 }, 
		success: function(data){
			var response = eval(data);
			var responseCode = response.code; 
			var responseMsg = response.msg; 
			var responseObject = response.object;
			
			
			if(responseCode === "200"){
				var mappingTypeVal = responseObject.mappingType;
				$('#mappingTypeVal').val(mappingTypeVal);
				
				if(parserType === jsSpringMsg.natflowParser  || parserType === jsSpringMsg.natflowASNParser){   //Add else if for all other parser type and call its custom function based on setting its value.
					natFlowMappingDetailsAjaxResponse(responseObject); 
				} else if(parserType === jsSpringMsg.asciiParser){
					setAsciiParserAdvanceDetail(responseObject);
				} else if(parserType === jsSpringMsg.asn1Parser ||parserType === jsSpringMsg.RapParser ||parserType === jsSpringMsg.Taparser || parserType === jsSpringMsg.NRTRDEParser ){
					setAsn1ParserAdvanceDetail(responseObject);
				} else if(parserType === jsSpringMsg.xmlParser){
					setXMLParserAdvanceDetail(responseObject);
				} else if(parserType === jsSpringMsg.fixedLengthASCIIParser ){
					setFixedLengthASCIIParserAdvanceDetail(responseObject);
				} else if(parserType === jsSpringMsg.detailLocalParser){
					setDetailLocalParserAdvanceDetail(responseObject);
				} else if(parserType === jsSpringMsg.htmlParser ){
					setHtmlParserAdvanceDetail(responseObject);
				}else if(parserType === jsSpringMsg.pdfParser ){
					setPDFParserAdvanceDetail(responseObject);
				}else if(parserType === jsSpringMsg.varLengthAsciiParser){
					setVarLengthAsciiParserAdvanceDetail(responseObject);
				}else if(parserType === jsSpringMsg.varLengthBinaryParser){
					setVarLengthBinaryParserAdvanceDetail(responseObject);
				}
				if($("#mappingName option:selected").text() === jsSpringMsg.mappingCustomLabel){
					$('#parser_association_save_next_btn').attr('disabled',true);
				}else{
					$('#parser_association_save_next_btn').attr('disabled',false);
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

/* Function will call natflow parser plugin ajax call response */
function natFlowMappingDetailsAjaxResponse(responseObject){
	var skipAttributeForValidation =  responseObject.skipAttributeForValidation;
	if(responseObject.readTemplatesInitially === true){
		$("#readTemplateInitially").val('True');
	}else{
		$("#readTemplateInitially").val('False');
	}
	if(responseObject.overrideTemplate === true){
		$("#overrideTemplate").val('True');
	}else{
		$("#overrideTemplate").val('False');
	}
	if(skipAttributeForValidation == null || skipAttributeForValidation == "" || skipAttributeForValidation == undefined || skipAttributeForValidation == ''){
		skipAttributeForValidation = '';
	}
	$("#skipAttributeForValidation").val(skipAttributeForValidation);
	if(responseObject.defaultTemplate === true){
		$("#defaultTemplate").val('True');
	}else{
		$("#defaultTemplate").val('False');
	}
	$("#service-optionTemplateEnable").val(responseObject.optionTemplateEnable.toString());
	enableDisableOptionTemplateConfig($("#service-optionTemplateEnable").val());
	$("#service-optionCopyTofield").val(responseObject.optionCopyTofield);
	$("#service-optionTemplateId").val(responseObject.optionTemplateId);
	$("#service-optionTemplateKey").val(responseObject.optionTemplateKey);
	$("#service-optionTemplateValue").val(responseObject.optionTemplateValue);
	$("#service-optionCopytoTemplateId").val(responseObject.optionCopytoTemplateId);

	$('#service-filterEnable').val(responseObject.filterEnable.toString());
	enableDisableFilterConfig($("#service-filterEnable").val());
	$('#service-filterProtocol').val(responseObject.filterProtocol);
	if(responseObject.filterTransport!==null)
		$('#service-filterTransport').val(responseObject.filterTransport.split(","));
	$('#service-filterPort').val(responseObject.filterPort);	
}

/* Function will associate the parser mapping with parser and also add the client validation for device configuration.*/
function associateMappingWithParser(){
	
	var mappingTypeVal = $("#mappingTypeVal").val();
	$("#selectedMappingType").val(mappingTypeVal);
	var isValidated = validateDeviceConfig();
	if(isValidated){
		
		$("#mappingDeviceId").val($("#deviceName").val()); //setting device id in submit form as its required to in audit for device details.		
		$("#id").val($("#mappingName").val());
		$("#selecteMappingName").val($("#mappingName option:selected").text());
		$("#selecteDeviceName").val($("#deviceName option:selected").text());
		
		if($("#fileTypeEnum").val() == "LINEAR_KEY_VALUE_RECORD"){
			if($("#excludeCharactersMin").val().trim() == ""){
				$("#excludeCharactersMin").val("-1");				
			}
			if($("#excludeCharactersMax").val().trim() == ""){
				$("#excludeCharactersMax").val("-1");				
			}
		}
		
		if(currentParserType === jsSpringMsg.natflowASNParser){
			$("#natflow-parser-configuration-form").attr("action",'updateNatflowASNParserConfiguration');
			$("#natflow-parser-configuration-form").submit();
		}else if(currentParserType === jsSpringMsg.natflowParser){
			$("#natflow-parser-configuration-form").attr("action",'updateNatflowParserConfiguration');
			$("#natflow-parser-configuration-form").submit();
		}else if(currentParserType === jsSpringMsg.regExParser){
			$("#regEx-parser-configuration-form").attr("action",'updateRegExParserConfiguration');
			$("#regEx-parser-configuration-form").submit();
		}else if(currentParserType === jsSpringMsg.asciiParser){
			$("#selDeviceId").val($("#deviceName option:selected").val());
			$("#selMappingId").val($("#mappingName option:selected").val());
			$("#selVendorTypeId").val($("#vendorType option:selected").val());
			$("#selDeviceTypeId").val($("#deviceType option:selected").val());
			$("#ascii-parser-configuration-form").attr("action",'updateAsciiParserConfiguration');
			$("#ascii-parser-configuration-form").submit();
		}else if(currentParserType === jsSpringMsg.varLengthAsciiParser){
			$("#selDeviceId").val($("#deviceName option:selected").val());
			$("#selMappingId").val($("#mappingName option:selected").val());
			$("#selVendorTypeId").val($("#vendorType option:selected").val());
			$("#selDeviceTypeId").val($("#deviceType option:selected").val());
			$("#var-length-ascii-parser-configuration-form").attr("action",'updateVarLengthAsciiParserConfiguration');
			$("#var-length-ascii-parser-configuration-form").submit();
		}else if(currentParserType === jsSpringMsg.asn1Parser){
			$("#selDeviceId").val($("#deviceName option:selected").val());
			$("#selMappingId").val($("#mappingName option:selected").val());
			$("#selVendorTypeId").val($("#vendorType option:selected").val());
			$("#selDeviceTypeId").val($("#deviceType option:selected").val());
			
			if($("#bufferSize").val() == null || $("#bufferSize").val() == "")
			{
				$("#bufferSize").val(1024);
			}
			if($("#removeAddByte option:selected").val())
			{
				if($("#headerOffset").val() == null || $("#headerOffset").val() == "")
				{
					$("#headerOffset").val(-1);
				}
				if($("#recOffset").val() == null || $("#recOffset").val() == "")
				{
					$("#recOffset").val(-1);
				}		
			}
			$("#ascii-parser-configuration-form").attr("action",'updateAsn1ParserConfiguration');
			$("#ascii-parser-configuration-form").submit();
		}else if(currentParserType === jsSpringMsg.xmlParser){
			$("#selDeviceId").val($("#deviceName option:selected").val());
			$("#selMappingId").val($("#mappingName option:selected").val());
			$("#selVendorTypeId").val($("#vendorType option:selected").val());
			$("#selDeviceTypeId").val($("#deviceType option:selected").val());
			$("#xml-parser-configuration-form").attr("action",'updateXMLParserConfiguration');
			$("#xml-parser-configuration-form").submit();
		}else if(currentParserType === jsSpringMsg.fixedLengthASCIIParser){
			$("#selDeviceId").val($("#deviceName option:selected").val());
			$("#selMappingId").val($("#mappingName option:selected").val());
			$("#selVendorTypeId").val($("#vendorType option:selected").val());
			$("#selDeviceTypeId").val($("#deviceType option:selected").val());
			$("#fixedLengthASCII-parser-configuration-form").attr("action",'updateFixedLengthASCIIParserConfiguration');
			$("#fixedLengthASCII-parser-configuration-form").submit();
		}else if(currentParserType === jsSpringMsg.detailLocalParser){
			$("#selDeviceId").val($("#deviceName option:selected").val());
			$("#selMappingId").val($("#mappingName option:selected").val());
			$("#selVendorTypeId").val($("#vendorType option:selected").val());
			$("#selDeviceTypeId").val($("#deviceType option:selected").val());
			$("#ascii-parser-configuration-form").attr("action",'updateDetailLocalParserConfiguration');
			$("#ascii-parser-configuration-form").submit();
		}else if(currentParserType === jsSpringMsg.RapParser){
			$("#selDeviceId").val($("#deviceName option:selected").val());
			$("#selMappingId").val($("#mappingName option:selected").val());
			$("#selVendorTypeId").val($("#vendorType option:selected").val());
			$("#selDeviceTypeId").val($("#deviceType option:selected").val());
			$("#ascii-parser-configuration-form").attr("action",'updateRapsParserConfiguration');
			$("#ascii-parser-configuration-form").submit();
		}else if(currentParserType === jsSpringMsg.TapParser){
			$("#selDeviceId").val($("#deviceName option:selected").val());
			$("#selMappingId").val($("#mappingName option:selected").val());
			$("#selVendorTypeId").val($("#vendorType option:selected").val());
			$("#selDeviceTypeId").val($("#deviceType option:selected").val());
			$("#ascii-parser-configuration-form").attr("action",'updateTapsParserConfiguration');
			$("#ascii-parser-configuration-form").submit();
		}else if(currentParserType === jsSpringMsg.NRTRDEParser){
			$("#selDeviceId").val($("#deviceName option:selected").val());
			$("#selMappingId").val($("#mappingName option:selected").val());
			$("#selVendorTypeId").val($("#vendorType option:selected").val());
			$("#selDeviceTypeId").val($("#deviceType option:selected").val());
			$("#ascii-parser-configuration-form").attr("action",'updateNrtrdesParserConfiguration');
			$("#ascii-parser-configuration-form").submit();
		}else if(currentParserType === jsSpringMsg.fixedLengthBinaryParser){
			$("#selDeviceId").val($("#deviceName option:selected").val());
			$("#selMappingId").val($("#mappingName option:selected").val());
			$("#selVendorTypeId").val($("#vendorType option:selected").val());
			$("#selDeviceTypeId").val($("#deviceType option:selected").val());
			$("#fixedLengthBinary-parser-configuration-form").attr("action",'updateFixedLengthBinaryParserConfiguration');
			$("#fixedLengthBinary-parser-configuration-form").submit();		
		}else if(currentParserType === jsSpringMsg.htmlParser){
			$("#selDeviceId").val($("#deviceName option:selected").val());
			$("#selMappingId").val($("#mappingName option:selected").val());
			$("#selVendorTypeId").val($("#vendorType option:selected").val());
			$("#selDeviceTypeId").val($("#deviceType option:selected").val());
			$("#html-parser-configuration-form").attr("action",'updateHtmlParserConfiguration');
			$("#html-parser-configuration-form").submit();
		}else if(currentParserType === jsSpringMsg.pdfParser){
			$("#selDeviceId").val($("#deviceName option:selected").val());
			$("#selMappingId").val($("#mappingName option:selected").val());
			$("#selVendorTypeId").val($("#vendorType option:selected").val());
			$("#selDeviceTypeId").val($("#deviceType option:selected").val());
			$("#pdf-parser-configuration-form").attr("action",'updatePDFParserConfiguration');
			$("#pdf-parser-configuration-form").submit();
		}else if(currentParserType === jsSpringMsg.xlsParser){
			$("#selDeviceId").val($("#deviceName option:selected").val());
			$("#selMappingId").val($("#mappingName option:selected").val());
			$("#selVendorTypeId").val($("#vendorType option:selected").val());
			$("#selDeviceTypeId").val($("#deviceType option:selected").val());
			$("#xls-parser-configuration-form").attr("action",'updateXlsParserConfiguration');
			$("#xls-parser-configuration-form").submit();
		}else if(currentParserType === jsSpringMsg.varLengthBinaryParser){
			$("#selDeviceId").val($("#deviceName option:selected").val());
			$("#selMappingId").val($("#mappingName option:selected").val());
			$("#selVendorTypeId").val($("#vendorType option:selected").val());
			$("#selDeviceTypeId").val($("#deviceType option:selected").val());
			$("#var-length-binary-parser-configuration-form").attr("action",'updateVarLengthBinaryParserConfiguration');
			$("#var-length-binary-parser-configuration-form").submit();
		}else if(currentParserType === jsSpringMsg.jsonParser){
			$("#selDeviceId").val($("#deviceName option:selected").val());
			$("#selMappingId").val($("#mappingName option:selected").val());
			$("#selVendorTypeId").val($("#vendorType option:selected").val());
			$("#selDeviceTypeId").val($("#deviceType option:selected").val());
			$("#json-parser-configuration-form").attr("action",'updateJsonParserConfiguration');
			$("#json-parser-configuration-form").submit();
		}else if(currentParserType === jsSpringMsg.mtsiemensParser){
			$("#selDeviceId").val($("#deviceName option:selected").val());
			$("#selMappingId").val($("#mappingName option:selected").val());
			$("#selVendorTypeId").val($("#vendorType option:selected").val());
			$("#selDeviceTypeId").val($("#deviceType option:selected").val());
			$("#mtsiemens-binary-parser-configuration-form").attr("action",'updateMtsiemensParserConfiguration');
			$("#mtsiemens-binary-parser-configuration-form").submit();
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

/*Function will reload attribute grid after add/edit/delete action. */
function reloadAttributeGridData(){
	clearAllMessages();

	var $grid='';
	if(currentParserType === jsSpringMsg.natflowParser || currentParserType === jsSpringMsg.natflowASNParser){
		$grid = $("#natflowAttributeList");
		jQuery('#natflowAttributeList').jqGrid('clearGridData');
		clearAttributeGrid();
		getAllAttributeListByMappingId(urlAction,selMappingId);
	} else if(currentParserType === jsSpringMsg.asciiParser || currentParserType === jsSpringMsg.asn1Parser || currentParserType === jsSpringMsg.RapParser || currentParserType === jsSpringMsg.TapParser || currentParserType === jsSpringMsg.NRTRDEParser ){
		$grid = $("#asciiAttributeList");
		jQuery('#asciiAttributeList').jqGrid('clearGridData');
		clearAttributeGrid();
		getAllAsciiAttributeListByMappingId(urlAction, selMappingId);
	} else if(currentParserType === jsSpringMsg.detailLocalParser){
		$grid = $("#asciiAttributeList");
		jQuery('#asciiAttributeList').jqGrid('clearGridData');
		clearAttributeGrid();
		getAllDetailLocalAttributeListByMappingId(urlAction, selMappingId);
	} else if(currentParserType === jsSpringMsg.varLengthAsciiParser){
		$grid = $("#varLengthAsciiAttributeList");
		jQuery('#varLengthAsciiAttributeList').jqGrid('clearGridData');
		clearAttributeGrid();
		getAllVarLengthAsciiAttributeListByMappingId(urlAction, selMappingId);
	} else if(currentParserType === jsSpringMsg.varLengthBinaryParser){
		$grid = $("#varLengthBinaryAttributeList");
		jQuery('#varLengthBinaryAttributeList').jqGrid('clearGridData');
		clearAttributeGrid();
		getAllVarLengthBinaryAttributeListByMappingId(urlAction, selMappingId);
	} else if( currentParserType === jsSpringMsg.xmlParser){
		$grid = $("#xmlAttributeList");
		jQuery('#xmlAttributeList').jqGrid('clearGridData');
		clearAttributeGrid();
		getAllXMLAttributeListByMappingId(urlAction, selMappingId );
	} else if(currentParserType === jsSpringMsg.fixedLengthASCIIParser){
		$grid = $("#fixedLengthASCIIAttributeList");
		jQuery('#fixedLengthASCIIAttributeList').jqGrid('clearGridData');
		clearAttributeGrid();
		getAllfixedLengthASCIIAttributeListByMappingId(urlAction, selMappingId );
	}else if(currentParserType === jsSpringMsg.fixedLengthBinaryParser){
		$grid = $("#fixedLengthBinaryAttributeList");
		jQuery('#fixedLengthBinaryAttributeList').jqGrid('clearGridData');
		clearAttributeGrid();
		getAllfixedLengthBinaryAttributeListByMappingId(urlAction, selMappingId );
	}else if(currentParserType === jsSpringMsg.htmlParser){
		if($("#groupId").val()!=null){
			$grid = $("#htmlGroupAttributeList");
			jQuery('#htmlGroupAttributeList').jqGrid('clearGridData');
			clearAttributeGrid();
			getAllhtmlGroupAttributeListByMappingId(urlAction, selMappingId );
		}	
		else{
			$grid = $("#htmlAttributeList");
			jQuery('#htmlAttributeList').jqGrid('clearGridData');
			clearAttributeGrid();
			getAllhtmlAttributeListByMappingId(urlAction, selMappingId );
		}
	}else if(currentParserType === jsSpringMsg.pdfParser){
		
		
		var counter = getGroupAttCounter(); 
		groupId=$("#"+counter+"_groupAttributeId").val();
		if(groupId === null || groupId === "" || groupId === undefined || groupId === ''){
			groupId = -1;
			$grid = $("#pdfAttributeList");
			jQuery('#pdfAttributeList').jqGrid('clearGridData');
			clearAttributeGrid();
			getAllPDFAttributeListByMappingId(urlAction, selMappingId );
		}else{
			var selConfigMappingId = $("#selConfigMappingId").val();
			if(!(selConfigMappingId === null || selConfigMappingId === "" || selConfigMappingId === undefined || selConfigMappingId === '')
					&& selConfigMappingId > 0){
				var url = 'getAttributeGridListByGroupId';
				getAttributeListByGroupId(url,groupId,selConfigMappingId,counter);
			}
		}
		setGroupAttCounter(-1);
			
	}else if(currentParserType === jsSpringMsg.xlsParser){
		if($("#groupId").val()!=null){
			$grid = $("#xlsGroupAttributeList");
			jQuery('#xlsGroupAttributeList').jqGrid('clearGridData');
			clearAttributeGrid();
			getAllxlsGroupAttributeListByMappingId(urlAction, selMappingId );
		}	
		else{
			$grid = $("#xlsAttributeList");
			jQuery('#xlsAttributeList').jqGrid('clearGridData');
			clearAttributeGrid();
			getAllxlsAttributeListByMappingId(urlAction, selMappingId );
		}
	}else if(currentParserType === jsSpringMsg.jsonParser){
		$grid = $("#jsonAttributeList");
		jQuery('#jsonAttributeList').jqGrid('clearGridData');
		clearAttributeGrid();
		getAllJsonAttributeListByMappingId(urlAction, selMappingId);
	}else if(currentParserType === jsSpringMsg.mtsiemensParser){
		$grid = $("#mtsiemensAttributeList");
		jQuery('#mtsiemensAttributeList').jqGrid('clearGridData');
		clearAttributeGrid();
		getAllMtsiemensAttributeListByMappingId(urlAction, selMappingId);
	}  
	

	
	
	/*if($grid != ''){
		
		$grid.jqGrid('setGridParam',{datatype:'json',page: 1,sortname: 'id',sortorder: "asc", postData:{
	   		'mappingId': function () {
		        return selMappingId;
	   		},
	   		'plugInType':currentParserType,
		}}).trigger('reloadGrid');
	}*/
	
}

/*Function will create JQGRID for NetFlow parser attribute list */
function createAttributeGrid(defaultRowNum){				
	 
	 $("#natflowAttributeList").jqGrid({	    	
	        datatype: "local",
	        colNames:[
                      "#",
                      "<input type='checkbox' id='selectAllAttribute' onclick='attributeHeaderCheckbox(event, this)'></input>",
                      jsSpringMsg.attributeName,
                      jsSpringMsg.unifiedFieldName,
                      jsSpringMsg.description,
                      jsSpringMsg.defaultVal,
                      jsSpringMsg.trimChar,
                      jsSpringMsg.trimPosition,
		      jsSpringMsg.sourceFieldFormat,
		      jsSpringMsg.destDateFormat,
                      jsSpringMsg.updateAction
	                 ],
			colModel:[
			          {name: 'id', index: 'id',hidden:true,sortable:false, search : false},
			          {name: 'checkbox', index: 'checkbox',sortable:false, formatter:natflowParserAttributeCheckboxFormatter, align : 'center', width:'30%', search : false},
			          {name: 'sourceField', index: 'sourceField',sortable:false},
					  {name: 'unifiedField',index: 'unifiedField',sortable:true},
					  {name: 'description',index: 'description',sortable:false, search : false},
					  {name: 'defaultText',index: 'defaultText',sortable:false, search : false},
					  {name: 'trimChar',index: 'trimChar', sortable:false, search : false},
					  {name: 'trimPosition',index: 'trimPosition', sortable:false, search : false},
					  {name: 'sourceFieldFormat',index: 'sourceFieldFormat', sortable:false, search : false},
					  {name: 'destDateFormat',index: 'destDateFormat', sortable:false, search : false},
					  {name: 'update',index: 'update', align:'center', formatter: updateFormatter,sortable:false, search : false}
	        ],
	        rowNum:defaultRowNum,
	        rowList:[10,20,50,100,200],
	        height: 'auto',
	        sortname: 'sequenceNumber',
	 		sortorder: "asc",
	        pager: '#natflowAttributeListDiv',
	        contentType: "application/json; charset=utf-8",
	        viewrecords: true,
	        //multiselect: true,
	        search:true,
	        ignoreCase: true,
	        timeout : 120000,
	        loadtext: 'Loading...',
	        caption: jsSpringMsg.caption,
	        beforeRequest:function(){
	            $(".ui-dialog-titlebar").hide();
	        }, 
	        beforeSend: function(xhr) {
	            xhr.setRequestHeader("Accept", "application/json");
	            xhr.setRequestHeader("Content-Type", "application/json");
	        },
	 		loadComplete: function(data) {
			}, 
			onPaging: function (pgButton) {
				clearResponseMsgDiv();
			},
			loadError : function(xhr,st,err) {
				handleGenericError(xhr,st,err);
			},			
			recordtext: jsSpringMsg.recordtext,
	        emptyrecords: jsSpringMsg.emptyrecords,
			loadtext: jsSpringMsg.loadtext,
			pgtext : jsSpringMsg.pgtext,
	 	}).navGrid("#natflowAttributeListDiv",{edit:false,add:false,del:false,search:true});
		$(".ui-jqgrid-titlebar").hide();
		$('#natflowAttributeList').jqGrid('filterToolbar',{
			stringResult: true,
			searchOperators: false,
			searchOnEnter: false, 
			defaultSearch: "cn"
	    });	
		
		// MED-4817 IplogParsing->Attributes should be in sequence after sync
		
		$("#natflowAttributeList").sortableRows();   
		$("#natflowAttributeList").disableSelection();
	    $("#natflowAttributeList").sortable({
	    	items: 'tr:not(:first)'
	    });
	    
	    $('#natflowAttributeList').navButtonAdd('#natflowAttributeListDiv', {
           title: "Update Order",
           caption: "Update Order",
           position: "last",
           onClickButton: updateNatFlowAttributeOrder
        });
}

/*Function will load attributes to JQGRID */
function getAllAttributeListByMappingId(urlAction, mappingId){
	
	$.ajax({
		url: urlAction,
		cache: false,
		async: false,
    	data:
    		{
    		   'mappingId':mappingId,
			   'plugInType':currentParserType
    		},
		dataType: 'json',
		type: "POST",
		success: function(data){						
			var response = eval(data);			
			var responseCode = response.code; 
			var responseMsg = response.msg; 
			var responseObject = response.object;			
			if(responseCode == "200"){				
				$("#natflowAttributeListDiv").show();				
				var attributes=eval(responseObject);				
				var attributeList=attributes['attributeList'];
				if(attributeList!=null && attributeList != 'undefined' && attributeList != undefined){					
					var gridArray = [];
					$.each(attributeList,function(index,attribute){		 				
		 				var rowData = {};
		 				rowData.id					 = parseInt(attribute.id);
		 				rowData.checkbox			 = attribute.checkbox;
		 				rowData.sourceField			 = attribute.sourceField;
		 				rowData.unifiedField		 = attribute.unifiedField;
		 				rowData.description			 = attribute.description;
		 				rowData.defaultText		     = attribute.defaultText;
		 				rowData.trimChar             = attribute.trimChar;
		 				rowData.trimPosition		 = attribute.trimPosition;
		 				rowData.sourceFieldFormat	 = attribute.sourceFieldFormat;
		 				rowData.destDateFormat	 = attribute.destDateFormat;
		 				
		 				gridArray.push(rowData);
		 			});
					jQuery("#natflowAttributeList").jqGrid('setGridParam', {datatype: 'local', data:gridArray }).trigger("reloadGrid");
				} 
				$('.fancybox-wrap').css('top','10px');
			}else if(responseObject != undefined && responseObject != 'undefined' && responseCode == "400"){
				console.log("got 400 response.");
			}
		},
	    error: function (xhr,st,err){	    		    	
			handleGenericError(xhr,st,err);
		}
	});
}

/*
 * checkbox formatter for all child checkbox
 */
function natflowParserAttributeCheckboxFormatter(cellvalue, options, rowObject) {
 	var uniqueId = getUniqueCheckboxIdForAttribute(rowObject);
	return '<input type="checkbox" id="'+uniqueId+'" name="attributeCheckbox" value="'+rowObject["id"]+'" onchange="updateRootCheckbox(this, \'natflowAttributeList\')"/>';
}

/* Function will add edit link in attribute link */
function updateFormatter(cellvalue, options, rowObject){
	var uniqueId = "editAttribute";
	if(rowObject["sourceField"] && rowObject["sourceField"] !== 'null' && rowObject["sourceField"] !== "") {
		uniqueId += "_" + rowObject["sourceField"];
	}
	if(rowObject["unifiedField"] && rowObject["unifiedField"] !== 'null' && rowObject["unifiedField"] !== "") {
		uniqueId += "_" + rowObject["unifiedField"];
	}
	return "<a href='#' id='"+uniqueId+"' class='link' onclick=displayAddEditPopup(\'EDIT\','"+rowObject["id"]+"'); ><i class='fa fa-pencil-square-o' aria-hidden='true'></i></a>";
}


/* Function will display additional info in attribute link */
function additionInfoFormatter(cellvalue, options, rowObject){
	return "<a href='#' class='link' onclick=displayAdditionalInfo('"+rowObject["id"]+"'); ><i class='fa fa-info-circle' aria-hidden='true'></i></a>";
}

/*
 * this function will return unique checkbox id for attributes
 */
function getUniqueCheckboxIdForAttribute(rowObject) {
	var uniqueId = "checkbox";
	if(rowObject["sourceField"] && rowObject["sourceField"] !== 'null' && rowObject["sourceField"] !== "") {
		uniqueId += "_" + rowObject["sourceField"];
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

function clearAttributeGrid(){
	
	var $grid='';
	if(currentParserType === jsSpringMsg.natflowParser){
		$grid = $("#natflowAttributeList");
	}else if(currentParserType === jsSpringMsg.asciiParser|| currentParserType === jsSpringMsg.detailLocalParser || currentParserType === jsSpringMsg.asn1Parser || currentParserType === jsSpringMsg.RapParser || currentParserType === jsSpringMsg.TapParser || currentParserType === jsSpringMsg.NRTRDEParser){
		$grid = $("#asciiAttributeList");
	} else if(currentParserType === jsSpringMsg.xmlParser){
		$grid = $("#xmlAttributeList");
	} else if(currentParserType === jsSpringMsg.fixedLengthASCIIParser){
		$grid = $("#fixedLengthASCIIAttributeList");
	}else if(currentParserType === jsSpringMsg.fixedLengthBinaryParser){
		$grid = $("#fixedLengthBinaryAttributeList");
	}else if(currentParserType === jsSpringMsg.htmlParser){
		$grid = $("#htmlAttributeList");
	}else if(currentParserType === jsSpringMsg.pdfParser){
		$grid = $("#pdfAttributeList");
	}else if(currentParserType === jsSpringMsg.xlsParser){
		$grid = $("#xlsAttributeList");
	}else if(currentParserType === jsSpringMsg.varLengthAsciiParser){
		$grid = $("#varLengthAsciiAttributeList");
	}else if(currentParserType === jsSpringMsg.varLengthBinaryParser){
		$grid = $("#varLengthBinaryAttributeList");
	}else if(currentParserType === jsSpringMsg.jsonParser){
		$grid = $("#jsonAttributeList");
	}else if(currentParserType === jsSpringMsg.mtsiemensParser){
		$grid = $("#mtsiemensAttributeList");
	}
	

	
	var rowIds = $grid.jqGrid('getDataIDs');
	// iterate through the rows and delete each of them
	for(var i=0,len=rowIds.length;i<len;i++){
		var currRow = rowIds[i];
		$grid.jqGrid('delRowData', currRow);
	}
}

function displayAdditionalInfo(attributeId){
	if(currentParserType === jsSpringMsg.asn1Parser){
		var responseObject = jQuery("#asciiAttributeList").jqGrid('getRowData', attributeId);
		$("#srcDataFormatPopUp").val(responseObject.srcDataFormat);
		$("#defaultValuePopUp").val(responseObject.defaultText);
		$("#trimCharsPopUp").val(responseObject.trimChar);
		$("#recordInitilializerPopUp").val(responseObject.recordInitilializer);
		$("#recordInitunifiedFieldHoldsChoiceIdilializerPopup").val(responseObject.unifiedFieldHoldsChoiceId);
		$("#view_attribute").click();
	}else if(currentParserType === jsSpringMsg.fixedLengthASCIIParser ){
		var fixedLengthResponseObject = jQuery("#fixedLengthASCIIAttributeList").jqGrid('getRowData', attributeId);
		$("#startLengthPopUp").val(fixedLengthResponseObject.startLength);
		$("#endLengthPopUp").val(fixedLengthResponseObject.endLength);
		$("#prefixPopUp").val(fixedLengthResponseObject.prefix);
		$("#postfixPopUp").val(fixedLengthResponseObject.postfix);
		$("#defaultTextPopUp").val(fixedLengthResponseObject.defaultText);
		$("#rightDelimiterPopUp").val(fixedLengthResponseObject.rightDelimiter);
		$("#trimPositionPopup").val(fixedLengthResponseObject.trimPosition);
		$("#view_attribute").click();
	}else if(currentParserType === jsSpringMsg.fixedLengthBinaryParser){
		var fixedLengthResponseObject = jQuery("#fixedLengthBinaryAttributeList").jqGrid('getRowData', attributeId);
		$("#startLengthPopUp").val(fixedLengthResponseObject.startLength);
		$("#endLengthPopUp").val(fixedLengthResponseObject.endLength);
		$("#readAsBits").val(fixedLengthResponseObject.readAsBits);
		$("#bitStartLengthPopUp").val(fixedLengthResponseObject.bitStartLength);
		$("#bitEndLengthPopUp").val(fixedLengthResponseObject.bitEndLength);
		$("#prefixPopUp").val(fixedLengthResponseObject.prefix);
		$("#postfixPopUp").val(fixedLengthResponseObject.postfix);
		$("#defaultTextPopUp").val(fixedLengthResponseObject.defaultText);
		$("#rightDelimiterPopUp").val(fixedLengthResponseObject.rightDelimiter);
		$("#multiRecord").val(fixedLengthResponseObject.multiRecord);
		$("#view_attribute").click();
	}else if(currentParserType === jsSpringMsg.pdfParser){
		var pdfResponseObject = jQuery("#pdfAttributeList").jqGrid('getRowData', attributeId);
		$("#locationPopUp").val(pdfResponseObject.location);
		$("#columnStartLocationPopUp").val(pdfResponseObject.columnStartLocation);
		$("#columnIdentifierPopUp").val(pdfResponseObject.columnIdentifier);
		$("#referenceRowPopUp").val(pdfResponseObject.referenceRow);
		$("#referenceColPopUp").val(pdfResponseObject.referenceCol);
		$("#columnStartsWithPopUp").val(pdfResponseObject.columnStartsWith);
		$("#defaultTextPopUp").val(pdfResponseObject.defaultText);
		$("#pageNumberPopUp").val(pdfResponseObject.pageNumber);
		$("#valueSeparatorPopUp").val(pdfResponseObject.valueSeparator);
		$("#columnEndsWithPopUp").val(pdfResponseObject.columnEndsWith);
		$("#mandatoryPopUp").val(pdfResponseObject.mandatory);
		$("#tableFooterPopUp").val(pdfResponseObject.tableFooter);
		$("#multiLineAttributePopUp").val(pdfResponseObject.multiLineAttribute);
		$("#multipleValuesPopUp").val(pdfResponseObject.multipleValues);
		$("#rowTextAlignmentPopUp").val(pdfResponseObject.rowTextAlignment);
		$("#view_attribute").click();
	}

}
function displayAddEditPopup(actionType,attributeId){
	
	 clearAllMessagesPopUp();
	 resetWarningDisplayPopUp();
	 resetParserAttributeParams();
	$("#selectAllAttribute").prop('checked',false);
	if(actionType === 'ADD'){
		
		$("#addNewAttribute").show();
		if(currentParserType === jsSpringMsg.natflowParser)
			$("#dateFormatDiv").hide();
		$("#"+editAttributeBtnIdForParser).hide();
		$("#add_label").show();
		$("#update_label").hide();
		$("#sourceFieldFormat").val($("#sourceFieldFormat option:first").val());
		if(currentParserType === jsSpringMsg.xlsParser){
			$("#tableFooter").val($("#tableFooter option:first").val());
			$("#tableRowAttribute").val($("#tableRowAttribute option:first").val());
		}
		$("#id").val(0);
		var element = document.getElementById("recordInitilializer");
		if(element != null && element != ''){
			element.value = true;	
		}
		$("#add_edit_attribute").click();
	}else{
	
		if(attributeId > 0){

			var responseObject="";
			
			if(currentParserType === jsSpringMsg.natflowParser  || currentParserType === jsSpringMsg.natflowASNParser){
				responseObject = jQuery("#natflowAttributeList").jqGrid('getRowData', attributeId);
			} else if(currentParserType === jsSpringMsg.asciiParser || currentParserType === jsSpringMsg.detailLocalParser || currentParserType === jsSpringMsg.asn1Parser || currentParserType === jsSpringMsg.RapParser || currentParserType === jsSpringMsg.TapParser || currentParserType === jsSpringMsg.NRTRDEParser){
				responseObject = jQuery("#asciiAttributeList").jqGrid('getRowData', attributeId);
			} else if(currentParserType === jsSpringMsg.jsonParser){
				responseObject = jQuery("#jsonAttributeList").jqGrid('getRowData', attributeId);
			} else if(currentParserType === jsSpringMsg.mtsiemensParser){
				responseObject = jQuery("#mtsiemensAttributeList").jqGrid('getRowData', attributeId);
			} else if(currentParserType === jsSpringMsg.varLengthAsciiParser){
				responseObject = jQuery("#varLengthAsciiAttributeList").jqGrid('getRowData', attributeId);
			} else if(currentParserType === jsSpringMsg.varLengthBinaryParser){
				responseObject = jQuery("#varLengthBinaryAttributeList").jqGrid('getRowData', attributeId);
			} else if(currentParserType === jsSpringMsg.xmlParser){
				responseObject = jQuery("#xmlAttributeList").jqGrid('getRowData', attributeId);
			} else if(currentParserType === jsSpringMsg.fixedLengthASCIIParser){
				responseObject = jQuery("#fixedLengthASCIIAttributeList").jqGrid('getRowData', attributeId);
			}else if(currentParserType === jsSpringMsg.fixedLengthBinaryParser){
				responseObject = jQuery("#fixedLengthBinaryAttributeList").jqGrid('getRowData', attributeId);
			}else if(currentParserType === jsSpringMsg.htmlParser){
				if($("#groupId").val()!=null){
					responseObject = jQuery("#htmlGroupAttributeList").jqGrid('getRowData', attributeId);
				}
				else{
					responseObject = jQuery("#htmlAttributeList").jqGrid('getRowData', attributeId);
				}
			}else if(currentParserType === jsSpringMsg.pdfParser){
				responseObject = jQuery("#pdfAttributeList").jqGrid('getRowData', attributeId);
			}else if(currentParserType === jsSpringMsg.xlsParser){
				if($("#groupId").val()!=null){
					responseObject = jQuery("#xlsGroupAttributeList").jqGrid('getRowData', attributeId);
				}
				else{
					responseObject = jQuery("#xlsAttributeList").jqGrid('getRowData', attributeId);
				}
			}

			$("#addNewAttribute").hide();
			$("#update_label").show();
			$("#add_label").hide();
			
			$("#id").val(responseObject.id);
			$("#sourceField").val(responseObject.sourceField);
			$("#unifiedField").val(responseObject.unifiedField);
			$("#description").val(responseObject.description);
			$("#trimChars").val(responseObject.trimChar);
			$("#trimPosition").val(responseObject.trimPosition);
			$("#defaultValue").val(responseObject.defaultText);
			
			if(currentParserType === jsSpringMsg.asn1Parser || currentParserType === jsSpringMsg.RapParser ||currentParserType === jsSpringMsg.TapParser || currentParserType === jsSpringMsg.NRTRDEParser){
				$("#ASN1DataType").val(responseObject.ASN1DataType);
				$("#childAttributes").val(responseObject.childAttributes);
				$("#recordInitilializer").val(responseObject.recordInitilializer);
				$("#unifiedFieldHoldsChoiceId").val(responseObject.unifiedFieldHoldsChoiceId);
				console.log("responseObject.srcDataFormat : "+responseObject.srcDataFormat);
				$("#srcDataFormat").val(responseObject.srcDataFormat);
				$("#parseAsJson").val(responseObject.parseAsJson);
				
			}else if( currentParserType === jsSpringMsg.asciiParser){
				$("#sourceFieldFormat").val(responseObject.sourceFieldFormat);
				$("#dateFormatInput").val(responseObject.dateFormatInput);
				$("#portUnifiedField").val(responseObject.portUnifiedField);
				setIPPortSeperator(responseObject.ipPortSeperator);
		    }else if( currentParserType === jsSpringMsg.jsonParser){
				$("#sourceFieldFormat").val(responseObject.sourceFieldFormat);
				$("#dateFormatInput").val(responseObject.dateFormatInput);
			}
			else if( currentParserType === jsSpringMsg.varLengthAsciiParser || currentParserType === jsSpringMsg.detailLocalParser){
				$("#sourceFieldFormat").val(responseObject.sourceFieldFormat);
				$("#dateFormatInput").val(responseObject.dateFormatInput);
				$("#sourceFieldName").val(responseObject.sourceFieldName);
			    	if(responseObject.sourceFieldFormat == 'DATE') {
					$("#dateFormatDiv").show();
					$("#dateFormatInput").val(responseObject.dateFormatInput);
				}else{
					$("#dateFormatDiv").hide();
				}
		    }
		    else if( currentParserType === jsSpringMsg.varLengthBinaryParser){
				$("#startLength").val(responseObject.startLength);
				$("#endLength").val(responseObject.endLength);
				$("#prefix").val(responseObject.prefix);
				$("#postfix").val(responseObject.postfix);
				if(responseObject.length === ""){
					$("#length").val("-1");
				} else {
					$("#length").val(responseObject.length);
				}
				$("#rightDelimiter").val(responseObject.rightDelimiter);
				$("#sourceFieldFormat").val(responseObject.sourceFieldFormat);
				$("#sourceFieldName").val(responseObject.sourceFieldName);
				
				if(responseObject.sourceFieldFormat == 'DATE') {
					$("#dateFormatDiv").show();
					$("#dateFormatInput").val(responseObject.dateFormatInput);
				}else{
					$("#dateFormatDiv").hide();
				}
		    }
			else if(currentParserType === jsSpringMsg.xmlParser){
					$("#sourceFieldFormat").val(responseObject.sourceFieldFormat);
			}
			else if(currentParserType === jsSpringMsg.fixedLengthASCIIParser ){
				$("#sourceField").hide();
				$("#startLength").val(responseObject.startLength);
				$("#endLength").val(responseObject.endLength);
				$("#prefix").val(responseObject.prefix);
				$("#postfix").val(responseObject.postfix);
				if(responseObject.length === ""){
					$("#length").val("-1");
				}
				else{
					$("#length").val(responseObject.length);
				}
				$("#rightDelimiter").val(responseObject.rightDelimiter);
				$("#sourceFieldFormat").val(responseObject.sourceFieldFormat);
			}else if(currentParserType === jsSpringMsg.fixedLengthBinaryParser){
				$("#sourceField").hide();
				$("#startLength").val(responseObject.startLength);
				$("#endLength").val(responseObject.endLength);
				$("#readAsBits").val(responseObject.readAsBits);
				$("#bitStartLength").val(responseObject.bitStartLength);
				$("#bitEndLength").val(responseObject.bitEndLength);
				$("#prefix").val(responseObject.prefix);
				$("#postfix").val(responseObject.postfix);
				if(responseObject.length === ""){
					$("#length").val("-1");
				}
				else{
					$("#length").val(responseObject.length);
				}
				$("#rightDelimiter").val(responseObject.rightDelimiter);
				$("#multiRecord").val(responseObject.multiRecord);
				$("#sourceFieldFormat").val(responseObject.sourceFieldFormat);
			}else if(currentParserType === jsSpringMsg.htmlParser){
				$("#fieldIdentifier").val(responseObject.fieldIdentifier);
				$("#fieldExtractionMethod").val(responseObject.fieldExtractionMethod);
				$("#fieldSectionId").val(responseObject.fieldSectionId);
				$("#containsFieldAttribute").val(responseObject.containsFieldAttribute);
				$("#valueSeparatorAttribute").val(responseObject.valueSeparator);
				$("#valueIndexAttribute").val(responseObject.valueIndex);
				if($("#groupId").val()!=null){
					$("#tdNo").val(responseObject.tdNo);
				}
			}else if(currentParserType === jsSpringMsg.pdfParser){
				$("#sourceField").val(responseObject.sourceField);
				$("#location").val(responseObject.location);
				$("#columnStartLocation").val(responseObject.columnStartLocation);
				$("#columnIdentifier").val(responseObject.columnIdentifier);
				$("#referenceRow").val(responseObject.referenceRow);
				$("#referenceCol").val(responseObject.referenceCol);
				$("#columnStartsWith").val(responseObject.columnStartsWith);
				$("#pageNumber").val(responseObject.pageNumber);
				$("#valueSeparator").val(responseObject.valueSeparator);
				$("#columnEndsWith").val(responseObject.columnEndsWith);
				$("#mandatory").val(responseObject.mandatory);
				$("#tableFooter").val(responseObject.tableFooter);
				$("#multiLineAttribute").val(responseObject.multiLineAttribute);
				$("#multipleValues").val(responseObject.multipleValues);
				$("#rowTextAlignment").val(responseObject.rowTextAlignment);
			}else if(currentParserType === jsSpringMsg.xlsParser){
				$("#columnStartsWith").val(responseObject.columnStartsWith);
				$("#tableFooter").val(responseObject.tableFooter);
				$("#excelRow").val(responseObject.excelRow);
				$("#excelCol").val(responseObject.excelCol);
				$("#relativeExcelRow").val(responseObject.relativeExcelRow);
				$("#startsWith").val(responseObject.startsWith);
				$("#columnContains").val(responseObject.columnContains);
				$("#tableRowAttribute").val(responseObject.tableRowAttribute);
			}
			else if( currentParserType === jsSpringMsg.natflowParser){
				$("#sourceFieldFormat").val(responseObject.sourceFieldFormat);
				$("#dateFormatInput").val(responseObject.destDateFormat);
			    	if(responseObject.sourceFieldFormat == 'DATE') {
					$("#dateFormatDiv").show();
					$("#dateFormatInput").val(responseObject.destDateFormat);
				}else{
					$("#dateFormatDiv").hide();
				}
		    }

			
			$("#"+editAttributeBtnIdForParser).show();
			$("#add_edit_attribute").click();
			if(currentParserType === jsSpringMsg.asciiParser || currentParserType === jsSpringMsg.jsonParser || currentParserType === jsSpringMsg.varLengthAsciiParser
							|| currentParserType === jsSpringMsg.natflowParser){
			     changeSourceFieldFormat();
			}
		}else{
			showErrorMsg(jsSpringMsg.failUpdateAtributeMsg);
		}
	}
	if(currentParserType === jsSpringMsg.fixedLengthBinaryParser){
		handleReadAsBitsEvent();
	}
}

/*Function will get attribute by attribute Id */
function getAttributeById(attributeId){
	$.ajax({
		url: 'getAttributeById',
		cache: false,
		async: true,
		dataType:'json',
		type:'POST',
		data:
		 {
			"attributeId" 	: attributeId
		}, 
		success: function(data){
			var response = eval(data);
			var responseCode = response.code; 
			var responseMsg = response.msg; 
			var responseObject = response.object;
		
			clearAllMessagesPopUp();
			resetWarningDisplayPopUp();
			 
			if(responseCode === "200"){
				$("#"+editAttributeBtnIdForParser).show();
				$("#addNewAttribute").hide();
				$("#update_label").show();
				$("#add_label").hide();
				
				  $("#id").val(responseObject.id);
				  $("#sourceField").val(responseObject.sourceField);
				  $("#unifiedField").val(responseObject.unifiedField);
				  $("#description").val(responseObject.description);
				  $("#trimChars").val(responseObject.trimChars);
				  $("#trimPosition").val(responseObject.trimPosition);
				  $("#defaultValue").val(responseObject.defaultValue);
				
				$("#add_edit_attribute").click();
				
			}else{
				showErrorMsg(responseMsg);
			}
		},
	    error: function (xhr,st,err){
			handleGenericError(xhr,st,err);
		}
	});
}

/*Function will add/edit parser attribute details */
function createUpdateParserAttribute(inputUrl, attributeId, actionType){

	var dateFormat='';
	var sourceFieldFormat='';
	var ASN1DataType = '';
	var srcDataFormat = '';
	var childAttributes = '';
	var recordInitilializer = '';
	var unifiedFieldHoldsChoiceId = ''; 
	var trimPosition = $("#trimPosition").val();
	var attributeTypeId = '';
	var startLength = '';
	var endLength = '';
	var bitStartLength = '';
	var bitEndLength = '';
	var readAsBits = '';
	var length ='';
	var prefix='';
	var postfix='';
	var rightDelimiter='';
	var multiRecord = '';
	var pluginType = $("#selplugInType").val();
	var parseAsJson='';
	var portUnifiedField = '';
	var ipPortSeperator = '';
	var fieldIdentifier = '';
	var fieldExtractionMethod = '';
	var fieldSectionId = '';
	var containsFieldAttribute = '';
	var groupId=-1;
	var tdNo='';
	var location = '';
	var columnStartLocation = '';
	var columnIdentifier = '';
	var referenceRow = '';
	var referenceCol = '';
	var columnStartsWith = '';
	var startsWith = '';
	var tableFooter = '';
	var excelRow = '';
	var excelCol = '';
	var relativeExcelRow = '';
	var sourceFieldName = '';
	var valueSeparator='';
	var valueIndex='';
	var columnContains='';
	var tableRowAttribute='';
	var pageNumber='';
	var columnEndsWith='';
	var mandatory='';
	var multiLineAttribute = '';
	var multipleValues = '';
	var rowTextAlignment = '';
	var destDateFormat='';
	

	// MED-8298
	// Except ASN1, RAP, TAP, NRTRDE,XML, NatFlowParser, FixedLengthBinaryParser,FixedLengthAscii Parser should be false
	var isSourceFieldFormatRequire = true; 
	
	if(pluginType === jsSpringMsg.asciiParser){
		sourceFieldFormat=$("#sourceFieldFormat").val();
		dateFormat=$("#dateFormatInput").val().trim();
		portUnifiedField=$("#portUnifiedField").val().trim();
		ipPortSeperator=$("#ipPortDelimiter").val().trim();
		if(ipPortSeperator=='Other'){
			ipPortSeperator=$("#ipPortSeperator").val().trim();
		}
	}
	else if(currentParserType === jsSpringMsg.xmlParser || currentParserType === jsSpringMsg.natflowParser){
		sourceFieldFormat=$("#sourceFieldFormat").val();
		dateFormat=$("#dateFormatInput").val().trim();
	}else if(pluginType === jsSpringMsg.fixedLengthASCIIParser ){
		startLength=$("#startLength").val();
		endLength=$("#endLength").val();
		length=$("#length").val();
		if(length === null || length === ""){
			length = -1;
		}
		if(startLength === null || startLength === ""){
			startLength = -1;
		}
		if(endLength === null || endLength === ""){
			endLength = -1;
		}
		prefix=$("#prefix").val();
		postfix=$("#postfix").val();
		rightDelimiter=$("#rightDelimiter").val();
		sourceFieldFormat=$("#sourceFieldFormat").val();
		isSourceFieldFormatRequire = false;
	}else if(pluginType === jsSpringMsg.fixedLengthBinaryParser ){
		startLength=$("#startLength").val();
		endLength=$("#endLength").val();
		
		bitStartLength=$("#bitStartLength").val();
		bitEndLength=$("#bitEndLength").val();
		
		length=$("#length").val();
		if(length === null || length === ""){
			length = -1;
		}
		if(startLength === null || startLength === "" || parseInt(startLength)<-1){
			startLength = -1;
		}
		if(endLength === null || endLength === "" || parseInt(endLength)<-1){
			endLength = -1;
		}
		
		if(bitStartLength === null || bitStartLength === ""){
			bitStartLength = -1;
		}
		if(bitEndLength === null || bitEndLength === ""){
			bitEndLength = -1;
		}
		
		readAsBits=$("#readAsBits").val();
		prefix=$("#prefix").val();
		postfix=$("#postfix").val();
		rightDelimiter=$("#rightDelimiter").val();
		multiRecord=$("#multiRecord").val();
		sourceFieldFormat=$("#sourceFieldFormat").val();
		isSourceFieldFormatRequire = false;
	}else if(pluginType === jsSpringMsg.pdfParser ){
		location=$("#location").val();
		columnStartLocation=$("#columnStartLocation").val();
		columnIdentifier=$("#columnIdentifier").val();
		referenceRow=$("#referenceRow").val();
		referenceCol=$("#referenceCol").val();
		multiLineAttribute=$("#multiLineAttribute").val();
		multipleValues=$("#multipleValues").val();
		rowTextAlignment=$("#rowTextAlignment").val();
		columnStartsWith=$("#columnStartsWith").val();
		pageNumber=$("#pageNumber").val();
		valueSeparator=$("#valueSeparator").val();
		columnEndsWith=$("#columnEndsWith").val();
		mandatory=$("#mandatory").val();
		var tmp = getGroupAttCounter(); 
		tableFooter = $("#tableFooter").val();
		groupId=$("#"+tmp+"_groupAttributeId").val();
		if(groupId === null || groupId === "" || groupId === undefined || groupId === ''){
			groupId = -1;
		}
		isSourceFieldFormatRequire = false;
	}
	else if(pluginType === jsSpringMsg.asn1Parser || pluginType === jsSpringMsg.RapParser ||currentParserType === jsSpringMsg.TapParser ||currentParserType === jsSpringMsg.NRTRDEParser){
		ASN1DataType=$("#ASN1DataType").val();
		srcDataFormat=$("#srcDataFormat").val();
		childAttributes=$("#childAttributes").val();
		recordInitilializer=$("#recordInitilializer").val();
		unifiedFieldHoldsChoiceId=$("#unifiedFieldHoldsChoiceId").val();
		attributeTypeId = $("#attributeTypeHidden").val();
		parseAsJson=$("#parseAsJson").val();
		isSourceFieldFormatRequire = false;
	}
	else if(pluginType === jsSpringMsg.htmlParser){
		fieldIdentifier=$("#fieldIdentifier").val();
		fieldExtractionMethod=$("#fieldExtractionMethod").val();
		fieldSectionId=$("#fieldSectionId").val();
		containsFieldAttribute=$("#containsFieldAttribute").val();
		valueSeparator=$("#valueSeparatorAttribute").val();
		valueIndex=$("#valueIndexAttribute").val();
		if($("#groupId").val()!=null){
			groupId	= $("#groupId").val();
			if($("#tdNo").val()!=''){
				tdNo=$("#tdNo").val();
			}
		}	
		isSourceFieldFormatRequire = false;
	}
	else if(pluginType === jsSpringMsg.xlsParser){
		columnStartsWith=$("#columnStartsWith").val();
		startsWith=$("#startsWith").val();
		tableFooter = $("#tableFooter").val();
		excelRow = $("#excelRow").val();
		excelCol = $("#excelCol").val();
		relativeExcelRow = $("#relativeExcelRow").val();
		columnContains=$("#columnContains").val();
		tableRowAttribute=$("#tableRowAttribute").val();
		if($("#groupId").val()!=null){
			groupId	= $("#groupId").val();
		}	
		isSourceFieldFormatRequire = false;
	} else if(pluginType === jsSpringMsg.varLengthAsciiParser){
		sourceFieldFormat=$("#sourceFieldFormat").val();
		dateFormat=$("#dateFormatInput").val().trim();
		sourceFieldName = $("#sourceFieldName").val().trim();
	} else if(pluginType === jsSpringMsg.varLengthBinaryParser){
		sourceFieldFormat=$("#sourceFieldFormat").val();
		dateFormat=$("#dateFormatInput").val().trim();
		sourceFieldName = $("#sourceFieldName").val().trim();
		length=$("#length").val();
		if(length === null || length === ""){
			length = -1;
		}
		startLength=$("#startLength").val();
		endLength=$("#endLength").val();
		if(startLength === null || startLength === ""){
			startLength = -1;
		}
		if(endLength === null || endLength === ""){
			endLength = -1;
		}
		prefix=$("#prefix").val();
		postfix=$("#postfix").val();
		rightDelimiter=$("#rightDelimiter").val();
		attributeTypeId = $("#attributeTypeHidden").val();
	}else if(pluginType === jsSpringMsg.jsonParser){
		sourceFieldFormat=$("#sourceFieldFormat").val();
		dateFormat=$("#dateFormatInput").val().trim();
	}
	else if(pluginType === jsSpringMsg.detailLocalParser){
		sourceFieldFormat=$("#sourceFieldFormat").val();
		dateFormat=$("#dateFormatInput").val().trim();
	}
	
	if(pluginType !== jsSpringMsg.mtsiemensParser){
		if(sourceFieldFormat != 'DATE' && isSourceFieldFormatRequire && $("#dateFormatInput")!=undefined){
			$("#dateFormatInput").val('');
			dateFormat=$("#dateFormatInput").val().trim();
		}
		if ( currentParserType === jsSpringMsg.natflowParser)
		{
			destDateFormat=$("#dateFormatInput").val().trim();
			dateFormat='';
		}
	}

	
	var actionUrl=inputUrl;
	if(pluginType === jsSpringMsg.RapParser){
		actionUrl='addEditRapParserAttributeDetails';
	}else if(pluginType === jsSpringMsg.TapParser){
		actionUrl='addEditTapParserAttributeDetails';
		}else if(pluginType === jsSpringMsg.NRTRDEParser){
			actionUrl='addEditNrtrdeParserAttributeDetails';
		}else if(pluginType === jsSpringMsg.fixedLengthBinaryParser){
			actionUrl='addEditFixedLengthBinaryAttributeDetails';
		}else if(pluginType === jsSpringMsg.htmlParser){
			actionUrl='addEditHtmlAttributeDetails';
		}else if(pluginType === jsSpringMsg.pdfParser){
			actionUrl='addEditPDFAttributeDetails';
		}else if(pluginType === jsSpringMsg.xlsParser){
			actionUrl='addEditXlsAttributeDetails';
		}

	$.ajax({
		url: actionUrl,
		cache: false,
		async: true,
		dataType:'json',
		type:'POST',
		data:  {
			"id" 					: $("#id").val(),
			"sourceField"   		: $("#sourceField").val(),
			"unifiedField"  		: $("#unifiedField").val() ,
			"description"   		: $("#description").val(),
			"defaultValue" 	 		: $("#defaultValue").val(),
			"trimChars" 			: $("#trimChars").val(),
			"trimPosition" 			: $("#trimPosition").val(),
			"actionType" 			: actionType,
			"plugInType"    		: $("#selplugInType").val(),
			"mappingId"    			: $("#selConfigMappingId").val(),
			"sourceFieldFormat" 	: sourceFieldFormat,
			"ASN1DataType" 			: ASN1DataType,
			"srcDataFormat" 		: srcDataFormat,
			"childAttributes" 		: childAttributes,
			"recordInitilializer"	: recordInitilializer,
			"unifiedFieldHoldsChoiceId" : unifiedFieldHoldsChoiceId,
			"attributeType" : attributeTypeId,
			"startLength" : startLength,
			"endLength" : endLength,
			"bitStartLength" : bitStartLength,
			"bitEndLength" : bitEndLength,
			"readAsBits" : readAsBits,
			"length" : length,
			"prefix" : prefix,
			"postfix" : postfix,
			"rightDelimiter" : rightDelimiter,
			"multiRecord" : multiRecord,
			"parserMapping.id" : $("#selConfigMappingId").val(),
			"parseAsJson" : parseAsJson,
			"dateFormat":dateFormat,
			"ipPortSeperator": ipPortSeperator,
			"portUnifiedField":portUnifiedField,
			"fieldIdentifier":fieldIdentifier,
			"fieldExtractionMethod":fieldExtractionMethod,
			"fieldSectionId":fieldSectionId,
			"containsFieldAttribute":containsFieldAttribute,
			"tdNo":tdNo,
			"valueSeparator":valueSeparator,
			"valueIndex":valueIndex,
			"groupId":groupId,
			"location":location,
			"columnStartLocation":columnStartLocation,
			"columnIdentifier":columnIdentifier,
			"referenceRow":referenceRow,
			"referenceCol":referenceCol,
			"columnStartsWith":columnStartsWith,
			"tableFooter":tableFooter,
			"excelRow":excelRow,
			"excelCol":excelCol,
			"relativeExcelRow":relativeExcelRow,
			"sourceFieldName":sourceFieldName,
			"startsWith":startsWith,
			"columnContains":columnContains,
			"pageNumber":pageNumber,
			"columnEndsWith":columnEndsWith,
			"mandatory":mandatory,
			"multiLineAttribute":multiLineAttribute,
			"multipleValues":multipleValues,
			"rowTextAlignment":rowTextAlignment,
			"tableRowAttribute":tableRowAttribute,
			"destDateFormat": destDateFormat
		}, 
		success: function(data){

			var response = eval(data);
			var responseCode = response.code; 
			var responseMsg = response.msg; 
			var responseObject = response.object;
			clearAllMessagesPopUp();
			resetWarningDisplayPopUp();
			if(responseCode === "200"){
				closeFancyBox();
				closeFancyBoxFromChildIFrame();
				reloadAttributeGridData();
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


/*Function will update order parser attribute details */
function reorderParserAttribute(urlAction){
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
		
		var tableString ="<table class='table table-hover table-bordered'  border='1' >";
		tableString+="<th>#</th>"
		
		if(currentParserType !== jsSpringMsg.varLengthBinaryParser && currentParserType !== jsSpringMsg.fixedLengthASCIIParser && currentParserType !== jsSpringMsg.fixedLengthBinaryParser){
			tableString+="<th>"+jsSpringMsg.attributeName+"</th>";
		}
		if(!(currentParserType === jsSpringMsg.htmlParser || currentParserType === jsSpringMsg.xlsParser)){
			tableString+="<th>"+jsSpringMsg.unifiedFieldName+"</th><th>"+jsSpringMsg.description+"</th>";
		}
		else{
			tableString+="<th>"+jsSpringMsg.unifiedFieldName+"</th>";
		}
		for(var i = 0 ; i< ckIntanceSelected.length;i++){
			
			var rowData='';
			
			if(currentParserType === jsSpringMsg.natflowParser){
				rowData = $('#natflowAttributeList').jqGrid('getRowData', ckIntanceSelected[i]);	
			} else if(currentParserType === jsSpringMsg.asciiParser || currentParserType === jsSpringMsg.asn1Parser ||currentParserType === jsSpringMsg.RapParser || currentParserType === jsSpringMsg.TapParser || currentParserType === jsSpringMsg.NRTRDEParser){
				rowData = $('#asciiAttributeList').jqGrid('getRowData', ckIntanceSelected[i]);
			} else if(currentParserType === jsSpringMsg.varLengthAsciiParser){
				rowData = $('#varLengthAsciiAttributeList').jqGrid('getRowData', ckIntanceSelected[i]);
			} else if(currentParserType === jsSpringMsg.varLengthBinaryParser){
				rowData = $('#varLengthBinaryAttributeList').jqGrid('getRowData', ckIntanceSelected[i]);
			} else if(currentParserType === jsSpringMsg.xmlParser){
				rowData = $('#xmlAttributeList').jqGrid('getRowData', ckIntanceSelected[i]);
			} else if(currentParserType === jsSpringMsg.fixedLengthASCIIParser){
				rowData = $('#fixedLengthASCIIAttributeList').jqGrid('getRowData', ckIntanceSelected[i]);
			}else if(currentParserType === jsSpringMsg.fixedLengthBinaryParser ){
				rowData = $('#fixedLengthBinaryAttributeList').jqGrid('getRowData', ckIntanceSelected[i]);
			}else if(currentParserType === jsSpringMsg.htmlParser ){
				if($("#groupId").val()== null ){
					rowData = $('#htmlAttributeList').jqGrid('getRowData', ckIntanceSelected[i]);
				}else{
					rowData = $('#htmlGroupAttributeList').jqGrid('getRowData', ckIntanceSelected[i]);
				}
			}else if(currentParserType === jsSpringMsg.pdfParser ){
				rowData = $('#pdfAttributeList').jqGrid('getRowData', ckIntanceSelected[i]);
			}else if(currentParserType === jsSpringMsg.xlsParser ){
				if($("#groupId").val()== null ){
					rowData = $('#xlsAttributeList').jqGrid('getRowData', ckIntanceSelected[i]);
				}else{
					rowData = $('#xlsGroupAttributeList').jqGrid('getRowData', ckIntanceSelected[i]);
				}
			}else if(currentParserType === jsSpringMsg.jsonParser){
				rowData = $('#jsonAttributeList').jqGrid('getRowData', ckIntanceSelected[i]);
			}else if(currentParserType === jsSpringMsg.mtsiemensParser){
				rowData = $('#mtsiemensAttributeList').jqGrid('getRowData', ckIntanceSelected[i]);
			} 
			 

			
			tableString += "<tr>";
			tableString += "<td><input type='checkbox' name='attribute_delete' id='attribute_"+ckIntanceSelected[i]+"' checked  onclick=getSelecteAttributeList('"+ckIntanceSelected[i]+"')  value='"+ckIntanceSelected[i]+"' /> </td>";
			if(currentParserType !== jsSpringMsg.varLengthBinaryParser && currentParserType !== jsSpringMsg.fixedLengthASCIIParser && currentParserType !== jsSpringMsg.fixedLengthBinaryParser){
				tableString += "<td>"+rowData.sourceField+" </td>";
			}
			tableString += "<td>"+rowData.unifiedField+"</td>";
			if(!(currentParserType === jsSpringMsg.htmlParser || currentParserType === jsSpringMsg.xlsParser)){
				tableString += "<td>"+rowData.description+"</td>";
			}
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
		url: 'deleteAttribute',
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
				reloadAttributeGridData();
				closeFancyBox();
				$("#selectAllAttribute").prop('checked',false);
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


/*Function will reset all parser attribute params */
function resetParserAttributeParams(){
	   $("#sourceField").val('');
	   $("#trimPosition").val('');
	   $("#unifiedField").val('');
	   $("#description").val('');
	   $("#trimChars").val('');
	   $("#defaultValue").val('');
	   $("#sourceFieldFormat").val('');
	   if(currentParserType === jsSpringMsg.asciiParser){
		   $("#sourceFieldFormat").val('');
		   $('#dateFormatInput').val('');
		   $('#portUnifiedField').val('');
		   $('#ipPortSeperator').val('');
		   $('#ipPortDelimiter').val('');
	   }else if(currentParserType === jsSpringMsg.varLengthAsciiParser || jsSpringMsg.natflowParser){
		   $("#sourceFieldName").val('');
		   $('#dateFormatInput').val('');
	   } else if(currentParserType === jsSpringMsg.xmlParser ){
		   $("#sourceFieldFormat").val('');
	   }
	   else if(currentParserType === jsSpringMsg.asn1Parser || currentParserType === jsSpringMsg.RapParser ||currentParserType === jsSpringMsg.TapParser || currentParserType === jsSpringMsg.NRTRDEParser){
		   $("#ASN1DataType").val('');
		   $("#srcDataFormat").val('');
		   $("#childAttributes").val('');
		   $("#recordInitilializer").val('');
		   $("#unifiedFieldHoldsChoiceId").val('');
		   $("#unifiedField").val('');
		   $('#parseAsJson').val('false');
	   }else if(currentParserType === jsSpringMsg.varLengthBinaryParser){
		   $("#startLength").val('');
		   $("#endLength").val('');
		   $("#prefix").val('');
		   $("#postfix").val('');
		   $("#length").val('');
		   $("#rightDelimiter").val('');
		   $("#sourceFieldName").val('');
		   $('#dateFormatInput').val('');
	   }
	   else if(currentParserType === jsSpringMsg.fixedLengthASCIIParser){
		   $("#trimPosition").val('');
		   $("#startLength").val('');
		   $("#endLength").val('');
		   $("#sourceFieldFormat").val('');
		   $("#prefix").val('');
		   $("#postfix").val('');
		   $("#length").val('');
		   $("#rightDelimiter").val('');
		   $("#unifiedField").val('');
	   }else if(currentParserType === jsSpringMsg.fixedLengthBinaryParser){
		   $("#trimPosition").val('');
		   $("#startLength").val('');
		   $("#endLength").val('');
		   $("#bitStartLength").val('');
		   $("#bitEndLength").val('');
		   $('#readAsBits').val('false');
		   $("#sourceFieldFormat").val('');
		   $("#prefix").val('');
		   $("#postfix").val('');
		   $("#length").val('');
		   $("#rightDelimiter").val('');
		   $('#multiRecord').val('false');
		   $("#unifiedField").val('');
	   }else if(currentParserType === jsSpringMsg.pdfParser){
		   $("#sourceField").val('');
		   $("#location").val('');
		   $("#columnStartLocation").val('');
		   $("#columnIdentifier").val('');
		   $("#referenceRow").val('');
		   $("#referenceCol").val('');
		   $('#columnStartsWith').val('');
		   $("#sourceFieldFormat").val('');
		   $("#prefix").val('');
		   $("#postfix").val('');
		   $("#length").val('');
		   $("#unifiedField").val('');
		   $("#pageNumber").val('');
		   $("#valueSeparator").val('');
		   $("#columnEndsWith").val('');
		   $("#mandatory").val('');
		   $("#rowTextAlignment").val('');
		   $("#multiLineAttribute").val('false');
		   $("#multipleValues").val('false');
		   $("#tableFooter").val('false');
		   
	   }else if(currentParserType === jsSpringMsg.htmlParser){
		   $("#fieldIdentifier").val('');
		   $("#fieldExtractionMethod").val('');
		   $("#fieldSectionId").val('');
		   $("#containsFieldAttribute").val('');
		   $("#tdNo").val('');
		   $("#valueSeparatorAttribute").val('');
		   $("#valueIndexAttribute").val('');
	   }else if(currentParserType === jsSpringMsg.xlsParser){
		   $("#columnStartsWith").val('');
		   $("#tableFooter").val('');
		   $("#excelRow").val('');
		   $("#excelCol").val('');
		   $("#relativeExcelRow").val('');
		   $("#startsWith").val('');
		   $("#columnContains").val('');
		   $("#tableRowAttribute").val('');
	   }else if(currentParserType === jsSpringMsg.jsonParser){
		   $("#sourceFieldFormat").val('');
		   $('#dateFormatInput').val('');
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

			//getMappingByDeviceAndParserType('getMappingListByDevice',$("#deviceName").val(),currentParserType,'base_mapping_name','create_mapping');
			getMappingByDeviceTypeAndParserType($("#deviceType").val(), currentParserType,'base_mapping_name');
			
			
			$("#selected_mapping_association_details_div").hide();
			$("#customize_mode_div").hide();
		    $("#base_mapping_name").val('-2');
			$("#base_mapping_div").show();
			$("#mappingActionType").val('CREATE');
			$("#actionType").val('UPDATE');
						
			$("#selected_mapping_association_details_div").hide();
			$("#create_mapping_base_mapping_id").val($("#base_mapping_name").val());
			$('#customize_new_mapping_div').show();
			
		}else {
			$("#customize_mode_div").show();
			$("input[type=radio][value=update]").attr("disabled",false);
			getAllAssociationDetails('getMappingAssociationDetails',$("#mappingName").val(),'UPSTREAM','parserconfig');
			$("#mappingActionType").val('CREATE');
			$("#createMappingId").val(0);
			$("#actionType").val('UPDATE');
			$("#create_mapping_base_mapping_id").val($("#mappingName").val());
			$("#base_mapping_div").hide();
			$("#selected_mapping_association_details_div").hide();
			$('#create_new_mapping').prop('checked', true);
			$('#customize_new_mapping_div').show();
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

function updateAttributeOrder(action){
	
}
/*Function will create new or update existing created mapping */
function createOrUpdateMappingDetails(){
	if($("#mappingActionType").val() === 'EDIT'){
		if(currentParserType === 'NATFLOW_PARSING_PLUGIN' || currentParserType === 'NATFLOW_ASN_PARSING_PLUGIN'){
			enableMappingDetails();	
		} else if(currentParserType === 'ASCII_PARSING_PLUGIN'){
			enableAsciiAdvanceDetails();
		} else if(currentParserType === 'DETAIL_LOCAL_PARSING_PLUGIN'){
			enableDetailLocalAdvanceDetails();
		} else if(currentParserType === 'ASN1_PARSING_PLUGIN'|| currentParserType === 'RAP_PARSING_PLUGIN' || currentParserType ==='TAP_PARSING_PLUGIN'|| currentParserType === 'NRTRDE_PARSING_PLUGIN'){
			enableAsn1AdvanceDetails();
		} else if(currentParserType === 'XML_PARSING_PLUGIN'){
			enableXMLAdvanceDetails();
		} else if(currentParserType === 'FIXED_LENGTH_ASCII_PARSING_PLUGIN'){
			enablefixedLengthASCIIAdvanceDetails();
		}else if(currentParserType === 'FIXED_LENGTH_BINARY_PARSING_PLUGIN'){
			enablefixedLengthBinaryAdvanceDetails();
		}else if(currentParserType === 'HTML_PARSING_PLUGIN'){
			enablehtmlAdvanceDetails();
		}else if(currentParserType === 'PDF_PARSING_PLUGIN'){
			enablePDFAdvanceDetails();
		}else if(currentParserType === 'XLS_PARSING_PLUGIN'){
			enableXlsAdvanceDetails();
		}else if(currentParserType === 'VARIABLE_LENGTH_ASCII_PARSING_PLUGIN'){
			enableVarLengthAsciiAdvanceDetails();
			enableVarLengthAsciiDataDefinition();
		}else if(currentParserType === 'VARIABLE_LENGTH_BINARY_PARSING_PLUGIN'){
			enableVarLengthBinaryAdvanceDetails();
			enableVarLengthBinaryDataDefinition();
		}/*else if(currentParserType === 'DETAIL_LOCAL_PARSING_PLUGIN'){
			enableDLPAdvanceDetails();
		}*/

		closeFancyBox();
	}else{
		$("#create_mapping_proccess_bar_div").show();
		$("#close-create-mapping-button-div").hide();
		$("#create_mapping_button_div").hide();
		
		var baseMapping = $("#base_mapping_name").val()
		
		
		if(baseMapping !== null && baseMapping > 0){
			$("#mappingActionType").val('CUSTOM_CREATE');
		}
		
		$.ajax({
			url: 'createOrUpdateMappingDetails',
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
				"pluginType"  : currentParserType,
				"pluginId"    : $("#pluginId").val()
				
			 }, 
			success: function(data){
				
				var response = eval(data);
				var responseCode = response.code; 
				var responseMsg = response.msg; 
				var responseObject = response.object;
				resetWarningDisplayPopUp();
				$('#parser_association_save_next_btn').attr('disabled',false);
				
				if(responseCode === "200"){

//					showSuccessMsgPopUp(responseMsg);
//					$("#create_mapping_proccess_bar_div").hide();
//					$("#close-create-mapping-button-div").show();
//					$("#create_mapping_button_div").hide();
					if(currentParserType === 'NATFLOW_PARSING_PLUGIN' || currentParserType === 'NATFLOW_ASN_PARSING_PLUGIN'){
						enableMappingDetails();
					} else if(currentParserType === 'ASCII_PARSING_PLUGIN'){
						enableAsciiAdvanceDetails();
						setAsciiParserAdvanceDetail(responseObject);
					} else if(currentParserType === 'DETAIL_LOCAL_PARSING_PLUGIN'){
						enableDetailLocalAdvanceDetails();
						setDetailLocalParserAdvanceDetail(responseObject);
					} else if(currentParserType === 'ASN1_PARSING_PLUGIN' || currentParserType === 'RAP_PARSING_PLUGIN' || currentParserType === 'TAP_PARSING_PLUGIN'|| currentParserType === 'NRTRDE_PARSING_PLUGIN' ){
						enableAsn1AdvanceDetails();
						setAsn1ParserAdvanceDetail(responseObject);
					} else if(currentParserType === 'XML_PARSING_PLUGIN'){
						enableXMLAdvanceDetails();
						setXMLParserAdvanceDetail(responseObject);
					} else if(currentParserType === 'FIXED_LENGTH_ASCII_PARSING_PLUGIN'){
						enablefixedLengthASCIIAdvanceDetails();
						setFixedLengthASCIIParserAdvanceDetail(responseObject);
					}else if(currentParserType === 'PDF_PARSING_PLUGIN'){
						enablePDFAdvanceDetails();
						setPDFParserAdvanceDetail(responseObject);
					}else if(currentParserType === 'VARIABLE_LENGTH_ASCII_PARSING_PLUGIN'){
						enableVarLengthAsciiAdvanceDetails();
						enableVarLengthAsciiDataDefinition();
						setVarLengthAsciiParserAdvanceDetail(responseObject);
					}else if(currentParserType === 'VARIABLE_LENGTH_BINARY_PARSING_PLUGIN'){
						enableVarLengthBinaryAdvanceDetails();
						enableVarLengthBinaryDataDefinition();
						setVarLengthBinaryParserAdvanceDetail(responseObject);
					}
					getMappingByDeviceAndParserType('getMappingListByDevice',$('#deviceName').val(),currentParserType,'mappingName','device_block');
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

/* Function will set dropdown dyanmic value */
function setDropdownValue(elementId,elementValue){
	$("#"+elementId).val(elementValue);
}

/*Function will load onchange tab data */
function loadNatflowTabData(formAction,requestActionType,formMethod,formId){
	$("#"+formId).attr("action",formAction);
	$("#"+formId).attr("method",formMethod);
	$("#"+formId).submit();	
	
}

function loadNatflowAttributeTabData(formAction,requestActionType,formMethod,formId){
	$("#"+formId).attr("action",formAction);
	$("#"+formId).attr("method",formMethod);
	$("#"+formId).submit();	
	
}

function changeSourceFieldFormat(){
	var dataType=$("#sourceFieldFormat").find(":selected").val();
	if(dataType == 'DATE'){
		$("#dateFormatDiv").show();
	}else{
		$("#dateFormatDiv").hide();
	}
}

function downloadExcel(selMappingId,selPlugInType) {
	$("#exportedMappingId").val(selMappingId);
	$("#plugInType").val(selPlugInType);
	$("#downloadExcelFileForm").submit();
}

function setGroupAttCounter(counter){
	groupAttCounter = counter;
}
function getGroupAttCounter(){
	return groupAttCounter;
}
