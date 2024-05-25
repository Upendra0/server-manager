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
<div class="box box-warning">
	<div class="box-header with-border">
		<h3 class="box-title">
			<spring:message
				code="ascii.plugin.advance.details.heading.label" ></spring:message>
		</h3>
		<div class="box-tools pull-right">
			<button class="btn btn-box-tool" data-widget="collapse">
				<i class="fa fa-minus"></i>
			</button>
		</div>
		<!-- /.box-tools -->
	</div>
	<!-- /.box-header -->
	<div class="box-body inline-form ">
		<div class="col-md-6 no-padding">
			<spring:message code="ascii.plugin.advance.details.file.type.label" var="label" ></spring:message>
			<spring:message code="ascii.plugin.advance.details.file.type.label.tooltip" var="tooltip" ></spring:message>
			<div class="form-group">
				<div class="table-cell-label">${label}</div>
				<div class="input-group ">
					<form:select path="fileTypeEnum" cssClass="form-control table-cell input-sm" tabindex="4"
										id="fileTypeEnum"
										data-toggle="tooltip" data-placement="bottom"
										 title="${tooltip }" onchange="showRelatedFileTypeParam()">
										 
					<c:forEach var="fileTypeEnum" items="${fileTypeEnum}">
						<c:if test="${fileTypeEnum == 'DELIMITER'}">
	                     	<form:option selected="selected" value="${fileTypeEnum}">${fileTypeEnum}</form:option>
	                    </c:if>
	                    <c:if test="${fileTypeEnum != 'DELIMITER'}">
	                    	<form:option value="${fileTypeEnum}">${fileTypeEnum}</form:option>
	                    </c:if> 
	                </c:forEach>
										
					</form:select>
					<spring:bind path="fileTypeEnum">
						<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
					</spring:bind>
				</div>
			</div>
		</div>
		
		<div class="col-md-6 no-padding" id="keyValSep" style="display:none">
			<spring:message code="ascii.plugin.advance.details.keyValue.separator.label" var="label" ></spring:message>
			<spring:message code="ascii.plugin.advance.details.keyValue.separator.label.tooltip" var="tooltip" ></spring:message>
			<div class="form-group">
				<div class="table-cell-label">${label}<span class="required">*</span></div>
				<div class="input-group ">
				<form:select path="keyValueSeparatorEnum" cssClass="form-control table-cell input-sm" tabindex="4"
										id="keyValueSepValues"
										data-toggle="tooltip" data-placement="bottom"
										 title="${tooltip }">
										 
					<c:forEach var="SeparatorEnum" items="${SeparatorEnum}">
	                   		<form:option value="${SeparatorEnum.value}">${SeparatorEnum.input}</form:option>
	                </c:forEach>
										
					</form:select>
					<spring:bind path="keyValueSeparatorEnum">
						<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
					</spring:bind>
					<form:input path="keyValueSeparator"
										cssClass="form-control table-cell input-sm" tabindex="4"
										id="keyValueSeparator" data-toggle="tooltip"
										data-placement="bottom" 
										title="${tooltip }"></form:input>
					<spring:bind path="keyValueSeparator">
						<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
					</spring:bind>
				</div>
			</div>
		</div>
		
		<div class="col-md-6 no-padding" id="recordSep" style="display:none">
			<spring:message code="ascii.plugin.advance.details.recordHeader.separator.label" var="label" ></spring:message>
			<spring:message code="ascii.plugin.advance.details.recordHeader.separator.label.tooltip" var="tooltip" ></spring:message>
			<div class="form-group">
				<div class="table-cell-label">${label}</div>
				<div class="input-group ">
				<form:select path="recordHeaderSepEnum" cssClass="form-control table-cell input-sm" tabindex="4"
										id="recordHeaderSepEnum"
										data-toggle="tooltip" data-placement="bottom"
										 title="${tooltip }" >
										 
					<c:forEach var="SeparatorEnum" items="${SeparatorEnum}">
	                   		<form:option value="${SeparatorEnum.value}">${SeparatorEnum.input}</form:option>
	                </c:forEach>
										
					</form:select>
					<spring:bind path="recordHeaderSepEnum">
						<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
					</spring:bind>
					
					<form:input path="recordHeaderSeparator"
										cssClass="form-control table-cell input-sm" tabindex="4"
										id="recordHeaderSeparator" data-toggle="tooltip"
										data-placement="bottom" 
										title="${tooltip }" ></form:input>
					<spring:bind path="recordHeaderSeparator">
						<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
					</spring:bind>
				</div>
			</div>
		</div>
			
		<div class="col-md-6 no-padding" id="recordHeader" style="display:none">
			<spring:message code="ascii.plugin.advance.details.recordHeader.length.label" var="label" ></spring:message>
			<spring:message code="ascii.plugin.advance.details.recordHeader.length.label.tooltip" var="tooltip" ></spring:message>
			<div class="form-group">
				<div class="table-cell-label">${label}</div>
				<div class="input-group ">
					
					<form:input path="recordHeaderLength"
										cssClass="form-control table-cell input-sm" tabindex="4"
										id="recordHeaderLength" data-toggle="tooltip"
										data-placement="bottom" 
										title="${tooltip }" onkeydown='isNumericOnKeyDown(event)' ></form:input>
					<spring:bind path="recordHeaderLength">
						<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
					</spring:bind>
				</div>
			</div>
		</div>
		
		<jsp:include page="../../asciiFileHeaderDetail.jsp"></jsp:include>		
		
		<div class="col-md-6 no-padding" id="enableFileFooter" style="display:none">
			<spring:message code="ascii.plugin.advance.details.fileFooter.enable.label" var="label" ></spring:message>
			<spring:message code="ascii.plugin.advance.details.fileFooter.enable.label.tooltip" var="tooltip" ></spring:message>
			<div class="form-group">
				<div class="table-cell-label">${label}</div>
				<div class="input-group ">
					
					<form:select path="fileFooterEnable" cssClass="form-control table-cell input-sm" tabindex="4"
										id="fileFooterEnable"
										data-toggle="tooltip" data-placement="bottom"
										 title="${tooltip }" onChange="changeFilefooterParam();">
										 
					<c:forEach var="trueFalseEnum" items="${trueFalseEnum}">
						<form:option value="${trueFalseEnum.name}">${trueFalseEnum}</form:option>
					</c:forEach>
										
					</form:select>
					<spring:bind path="fileFooterEnable">
						<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
					</spring:bind>
				</div>
			</div>
		</div>
		
		<div class="col-md-6 no-padding" id="fileFooterType" style="display:none">
			<spring:message code="ascii.plugin.advance.details.fileFooter.type.label" var="label" ></spring:message>
			<spring:message code="ascii.plugin.advance.details.fileFooter.type.label.tooltip" var="tooltip" ></spring:message>
			<div class="form-group">
				<div class="table-cell-label">${label}</div>
				<div class="input-group ">
					
					<form:select path="fileFooterParser" cssClass="form-control table-cell input-sm" tabindex="4"
										id="fileFooterParser"
										data-toggle="tooltip" data-placement="bottom"
										 title="${tooltip }" >
										 
					<c:forEach var="headerFooterTypeEnum" items="${headerFooterTypeEnum}">
						<form:option value="${headerFooterTypeEnum}">${headerFooterTypeEnum}</form:option>
					</c:forEach>
										
					</form:select>
					<spring:bind path="fileFooterParser">
						<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
					</spring:bind>
				</div>
			</div>
		</div>
		
		<div class="col-md-6 no-padding" id="footerContains" style="display:none">
			<spring:message code="ascii.plugin.advance.details.fileFooter.containsField.label" var="label" ></spring:message>
			<spring:message code="ascii.plugin.advance.details.fileFooter.containsField.label.tooltip" var="tooltip" ></spring:message>
			<div class="form-group">
				<div class="table-cell-label">${label}</div>
				<div class="input-group ">
					
					<form:input path="fileFooterContains"
										cssClass="form-control table-cell input-sm" tabindex="4"
										id="fileFooterContains" data-toggle="tooltip"
										data-placement="top" 
										title="${tooltip }" ></form:input>
					<spring:bind path="fileFooterContains">
						<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
					</spring:bind>
				</div>
			</div>
		</div>
		
		
		<div class="col-md-6 no-padding" id="findAndREplace" style="display:none">
		
				<spring:message code="ascii.plugin.advance.details.find.replace.label" var="label" ></spring:message>
				<spring:message code="ascii.plugin.advance.details.find.replace.label.tooltip" var="tooltip" ></spring:message>
				<div class="table-cell-label left">${label}</div>
			<div class="input-group">				
			<div class="col-md-6 no-padding">
				<div class="form-group">
		        	<div class="input-group">
		            	<form:input path="find" cssClass="form-control table-cell input-sm" tabindex="4"
										id="find" data-toggle="tooltip" data-placement="bottom" title="${tooltip }"></form:input>
					<spring:bind path="find">
						<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
					</spring:bind>
		               </div>
		         </div>
		    </div>
			<div class="col-md-6 no-padding">
				<div class="form-group">
		        	<div class="input-group"> 
		             <form:input path="replace"
										cssClass="form-control table-cell input-sm" tabindex="4"
										id="replace" data-toggle="tooltip"
										data-placement="bottom" 
										title="${tooltip }"></form:input>
					<spring:bind path="replace">
						<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
					</spring:bind>
		             </div>
		           </div> 	
			</div>
			</div>
	</div>


<div class="col-md-6 no-padding" id="recordHeaderIdentifier" style="display:none">
			<spring:message code="ascii.plugin.advance.details.recordHeaderIdentifier.length.label" var="label" ></spring:message>
			<spring:message code="ascii.plugin.advance.details.recordHeaderIdentifier.length.tooltip" var="tooltip" ></spring:message>
			<div class="form-group">
				<div class="table-cell-label">${label}<span class="required">*</span></div>
				<div class="input-group ">
					
					<form:input path="recordHeaderIdentifier"
										cssClass="form-control table-cell input-sm" tabindex="4"
										id="recordHeaderIdentifierId" data-toggle="tooltip"
										data-placement="bottom" 
										title="${tooltip }"></form:input>
					<spring:bind path="recordHeaderIdentifier">
						<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
					</spring:bind>
				</div>
			</div>
		</div>
		
 <div class="col-md-4 no-padding" id="excludeMinCharacters" style="display:none">
			<spring:message code="ascii.plugin.advance.details.excludeCharacters.lable" var="label" ></spring:message>
			<spring:message code="ascii.plugin.advance.details.excludeCharacters.min.tooltip" var="mintooltip" ></spring:message>
			
			<div class="form-group ">
				<div class="table-cell-label">${label}</div>
				<div class="input-group ">
					
					<form:input path="excludeCharactersMin"
										cssClass="form-control table-cell input-sm " tabindex="4"
										id="excludeCharactersMin" data-toggle="tooltip"
										data-placement="bottom" 
										title="${mintooltip }" onkeydown='isNumericOnKeyDown(event)' maxlength="3" ></form:input>
					<spring:bind path="excludeCharactersMin">
						<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
					</spring:bind>
				</div>
			</div>
			</div>
			<div class="col-md-2 no-padding" id="excludeMaxCharacters" style="display:none">
			<spring:message code="ascii.plugin.advance.details.excludeCharacters.max.tooltip" var="maxtooltip" ></spring:message>
			<div class = "form-group">
				<div class="input-group">
					
					<form:input path="excludeCharactersMax"
										cssClass="form-control table-cell input-sm " tabindex="4"
										id="excludeCharactersMax" data-toggle="tooltip"
										data-placement="bottom" 
										title="${maxtooltip }" onkeydown='isNumericOnKeyDown(event)' maxlength="3" ></form:input>
					<spring:bind path="excludeCharactersMax">
						<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
					</spring:bind>
				</div>
			
			</div>
			
		</div>	 

 <div class="col-md-6 no-padding" id="excludeLinesStartwith" style="display:none">
			<spring:message code="ascii.plugin.advance.details.excludeLinesStartWith.lable" var="label" ></spring:message>
			<spring:message code="ascii.plugin.advance.details.excludeLinesStartWith.tooltip" var="tooltip" ></spring:message>
			<div class="form-group">
				<div class="table-cell-label">${label}</div>
				<div class="input-group ">
					
					<form:input path="excludeLinesStart"
										cssClass="form-control table-cell input-sm" tabindex="4"
										id="excludeLinesStart" data-toggle="tooltip"
										data-placement="bottom" 
										title="${tooltip }" ></form:input>
					<spring:bind path="excludeLinesStart">
						<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
					</spring:bind>
				</div>
			</div>
		</div> 









	</div>
	<!-- /.box-body -->
</div>

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


