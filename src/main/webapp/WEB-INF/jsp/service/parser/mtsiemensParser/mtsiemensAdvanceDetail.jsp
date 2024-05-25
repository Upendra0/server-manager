<%@page import="com.elitecore.sm.common.constants.BaseConstants"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="/WEB-INF/ShowErrorTag.tld" prefix="elitecoreError"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<style>
.left{float:left}
.working-thread-left{width: 67%; float:left;}
</style>


<script type="text/javascript">
$(document).ready(function() {

	showRelatedFileTypeParam();
	
	setTimeout(function(){
		changeFileHeaderParam();
	 	changeFilefooterParam();
	 	onFieldSepValueChange(false);
	 	onFileHeaderParserChange();
   	},1000);
	
	if($('#keyValueSepValues').val() != 'Other'){
		$("#keyValueSeparator").attr('readOnly',true);
	}
	
	if($('#fieldSepValues').val() != 'Other'){
		$("#fieldSeparator").attr('readOnly',true);
	}
	
	if($('#recordHeaderSepEnum').val() != 'Other'){
		$("#recordHeaderSeparator").attr('readOnly',true);
	}
	
});

$(document).on("change","#keyValueSepValues",function(event) {
	resetWarningDisplay();
	clearAllMessages();
	var keyValSep = $('#keyValueSepValues').val();
    	if(keyValSep == 'Other'){
    		$("#keyValueSeparator").val('');
		    $("#keyValueSeparator").attr('readOnly',false);
	   }else{
		   $("#keyValueSeparator").val('');
		   $("#keyValueSeparator").attr('readOnly',true);
	   }
});

$(document).on("change","#fieldSepValues",function(event) {
	onFieldSepValueChange(true);
});

function onFieldSepValueChange(removeFieldSeparator) {
	if(removeFieldSeparator) {
		resetWarningDisplay();
		clearAllMessages();
	}
	var fieldSep = $('#fieldSepValues').val();
	if(removeFieldSeparator) {
		$("#fieldSeparator").val('');
	} 
   	if(fieldSep == 'Other'){
	    $("#fieldSeparator").attr('readOnly',false);
    }else{
 	    $("#fieldSeparator").attr('readOnly',true);
    }
}

$(document).on("change","#recordHeaderSepEnum",function(event) {
	resetWarningDisplay();
	clearAllMessages();
	var recordSep = $('#recordHeaderSepEnum').val();
    	if(recordSep == 'Other'){
    		$("#recordHeaderSeparator").val('');
		    $("#recordHeaderSeparator").attr('readOnly',false);
	   }else{
		   $("#recordHeaderSeparator").val('');
		   $("#recordHeaderSeparator").attr('readOnly',true);
	   }
});

function showRelatedFileTypeParam(){
	resetWarningDisplay();
	clearAllMessages();
	var selectedFileType=$("#fileTypeEnum").find(":selected").val();
	
	if('${isValidationFail}' == '' || '${isValidationFail}' == false){
		$("#find").val('');
		$("#replace").val('');
	}
	
	if(selectedFileType == '<%=BaseConstants.KEY_VALUE_RECORD%>'){
		showKeyValueRecordParam();
	}else if(selectedFileType == '<%=BaseConstants.RECORD_HEADER%>'){
		showRecordHeaderParam();
	}else if(selectedFileType == '<%=BaseConstants.FILE_HEADER_FOOTER%>'){
		showFileHeaderFooterParam();
	}else if(selectedFileType == '<%=BaseConstants.DELIMITER%>'){
		showDelimeterParam();
	}else if(selectedFileType == '<%=BaseConstants.LINEAR_KEY_VALUE_RECORD%>'){
		showLinearKeyValueRecordParam();
	}
}

function showKeyValueRecordParam(){
	hideRecordHeaderParam();
	hideFileHeaderFooterParam();
	hideDelimeterParam();
	hideLinearKeyValueRecordParam();
	$("#keyValSep").show();
	$("#fieldSep").show();
	$("#findAndREplace").show();
	
}

function showRecordHeaderParam(){
	hideKeyValueRecordParam();
	hideFileHeaderFooterParam();
	hideDelimeterParam();
	hideLinearKeyValueRecordParam();
	$("#recordSep").show();
	$("#recordHeader").show();
	$("#findAndREplace").show();
	$("#fieldSep").show();
	
	
}
function showFileHeaderFooterParam(){
	hideDelimeterParam();
	hideKeyValueRecordParam();
	hideRecordHeaderParam();
	hideLinearKeyValueRecordParam();
	$("#enableFileHeader").show();
	$("#fieldSep").show();
	$("#fileHeaderType").show();
	$("#headerContains").show();
	$("#fileFooterType").show();
	$("#footerContains").show();
	$("#findAndREplace").show();
	$("#enableFileFooter").show();
	var fileHeaderEnableValue = $("#fileHeaderEnable").val();
	if(fileHeaderEnableValue == "false") {
		$('#fileHeaderContainsFields').prop('disabled','disabled');
	} else {
		$('#fileHeaderContainsFields').prop('disabled',false);
	}
}

function showDelimeterParam(){
	hideKeyValueRecordParam();
	hideRecordHeaderParam();
	hideFileHeaderFooterParam();
	hideLinearKeyValueRecordParam();
	$("#fieldSep").show();
	$("#findAndREplace").show();
	
}

function hideKeyValueRecordParam(){
	$("#keyValSep").hide();
}

function hideRecordHeaderParam(){
	$("#recordSep").hide();
	$("#recordHeader").hide();
}

function hideFileHeaderFooterParam(){
	
	$("#enableFileHeader").hide();
	$("#fileHeaderType").hide();
	$("#headerContains").hide();
	$("#enableFileFooter").hide();
	$("#fileFooterType").hide();
	$("#footerContains").hide();
	
}

function hideDelimeterParam(){
	
	$("#enableFileFooter").hide();
	$("#fileFooterType").hide();
	$("#footerContains").hide();
	$("#findAndREplace").hide();
	
}

function changeFilefooterParam(){
	if($('#fileFooterEnable').val()=='false'){
		$('#fileFooterParser').prop('disabled','disabled');
		$('#fileFooterContains').prop('disabled','disabled');
		
	} else {
		$('#fileFooterParser').prop('disabled',false);
		$('#fileFooterContains').prop('disabled',false);
		
	}
}


function changeFileHeaderParam(){
	var fileHeader=$("#fileHeaderEnable").find(":selected").val();
	if(fileHeader == 'true'){
		$("#fileHeaderParser").attr("disabled",false);
		$("#fileHeaderContainsFields").attr("disabled",false);
	}else if(fileHeader == 'false'){
		$("#fileHeaderParser").attr("disabled","disabled");
		$("#fileHeaderContainsFields").attr("disabled","disabled");
	}
}


function showLinearKeyValueRecordParam(){
	hideRecordHeaderParam();
	hideFileHeaderFooterParam();
	hideDelimeterParam();
	hideKeyValueRecordParam();
	$("#fieldSep").hide();
	$("#keyValSep").show();
	$('#recordHeaderIdentifier').show();
	$('#excludeLinesStartwith').show();
	$('#excludeMaxCharacters').show();
	$('#excludeMinCharacters').show();
	
}

function  hideLinearKeyValueRecordParam(){
	$("#keyValSep").hide();
	$('#recordHeaderIdentifier').hide();
	$('#excludeLinesStartwith').hide();
	$('#excludeMaxCharacters').hide();
	$('#excludeMinCharacters').hide();
}

</script>


