<%@page import="com.elitecore.sm.common.constants.BaseConstants"%>
<%@taglib 	uri="http://www.springframework.org/tags" 			prefix="spring"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="elitecore" %> 
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="/WEB-INF/ShowErrorTag.tld" prefix="elitecoreError"%>
<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<script src="${pageContext.request.contextPath}/customJS/autoSuggestionForUnifiedField.js<%= "?v=" + Math.random() %>" type="text/javascript"></script>
<script type="text/javascript">
$(document).ready(function() {
	$("#dateFormatDiv").hide();
	if($('#ipPortDelimiter').val() != 'Other' || $('#ipPortDelimiter').val()!=""){
		$("#ipPortSeperator").attr('readOnly',true);
	}
});
function changeSourceFieldFormat(){
	var dataType=$("#sourceFieldFormat").find(":selected").val();
	if(dataType == 'DATE'){
		$("#dateFormatDiv").show();
	}else{
		$("#dateFormatDiv").hide();
	}
}
function handleReadAsBitsEvent(){
	var readAsBitsFlag=$("#readAsBits").find(":selected").val();
	if(readAsBitsFlag == 'true' || readAsBitsFlag === 'true'){
		$("#bitStartLength").attr('readOnly',false);
		$("#bitEndLength").attr('readOnly',false);
		document.getElementById("sourceFieldFormat").options[1].disabled=true;
		document.getElementById("sourceFieldFormat").options[2].disabled=true;
		document.getElementById("sourceFieldFormat").options[3].disabled=true;
		document.getElementById("sourceFieldFormat").options[4].disabled=true;
		document.getElementById("sourceFieldFormat").options[5].disabled=true;
		document.getElementById("sourceFieldFormat").options[6].disabled=true;
		
	}else{
		$("#bitStartLength").attr('readOnly',true);
		$("#bitEndLength").attr('readOnly',true);
		document.getElementById("sourceFieldFormat").options[1].disabled=false;
		document.getElementById("sourceFieldFormat").options[2].disabled=false;
		document.getElementById("sourceFieldFormat").options[3].disabled=false;
		document.getElementById("sourceFieldFormat").options[4].disabled=false;
		document.getElementById("sourceFieldFormat").options[5].disabled=false;
		document.getElementById("sourceFieldFormat").options[6].disabled=false;
		document.getElementById("sourceFieldFormat").options[8].disabled=false;
	}
}
$(document).on("change","#ipPortDelimiter",function(event) {
	resetWarningDisplay();
	clearAllMessages();
    if($("#ipPortDelimiter").val() == 'Other'){
    	$("#ipPortSeperator").val('');
	    $("#ipPortSeperator").attr('readOnly',false);
	}else{
	    $("#ipPortSeperator").val('');
	    $("#ipPortSeperator").attr('readOnly',true);
	}
});

function setIPPortSeperator(ipPortSeperator){
	var separatorFind = false;
	<c:forEach var="ipPortFieldSeperator" items="${ipPortFieldSeperator}">
	if(ipPortSeperator == '${ipPortFieldSeperator.value}'){
		$("#ipPortDelimiter").val(ipPortSeperator);
		$("#ipPortSeperator").val('');
		$("#ipPortSeperator").attr('readOnly',true);
		separatorFind=true;
	}
	</c:forEach>

	if(separatorFind == false){
		if(ipPortSeperator == ''){
			$("#ipPortDelimiter").val('');
			$("#ipPortSeperator").val('');
			$("#ipPortSeperator").attr('readOnly',true);
		}else{
			$("#ipPortDelimiter").val('Other');
			$("#ipPortSeperator").val(ipPortSeperator);
			$("#ipPortSeperator").attr('readOnly',false);
		}
	}
}
</script>

			<div class="modal-content">
					<div class="modal-header padding10">
						<h4 class="modal-title">
							<span id="add_label" style="display: none;"><spring:message
									code="parser.attribute.create.heading.label" ></spring:message></span> <span
								id="update_label" style="display: none;"><spring:message
									code="parser.attribute.update.heading.label" ></spring:message></span>
						</h4>
					</div>
					
					<!-- Start : Code For MED-9753 -->
					<div class="modal-body padding10 inline-form" style="overflow-y: auto;">
					<!-- End : Code For MED-9753 -->
					
					<div class="modal-body padding10 inline-form">
					<div id="AddPopUpMsg">
						<jsp:include page="../../common/responseMsgPopUp.jsp" ></jsp:include>
						</div>
						<c:if test="${(plugInType ne 'FIXED_LENGTH_ASCII_PARSING_PLUGIN') and (plugInType ne 'ASCII_PARSING_PLUGIN') and (plugInType ne 'VARIABLE_LENGTH_ASCII_PARSING_PLUGIN') and (plugInType ne 'FIXED_LENGTH_BINARY_PARSING_PLUGIN') and (plugInType ne 'VARIABLE_LENGTH_BINARY_PARSING_PLUGIN')}">
							<div class="form-group">
								<spring:message code="parsing.service.add.attr.attr.name"
									var="tooltip" ></spring:message>
								<div class="table-cell-label">${tooltip}</div>
								<div class="input-group">
									<input type="text" name="sourceField" id="sourceField"
										class="form-control table-cell input-sm" data-toggle="tooltip"
										data-placement="bottom" title="${tooltip}"> <span
										class="input-group-addon add-on last"> <i
										class="glyphicon glyphicon-alert" data-toggle="tooltip"
										data-placement="left" title=""></i></span>
								</div>
							</div>
							<input type="hidden" id="attributeTypeHidden" value=""/>
						</c:if>
						
						<c:if test="${(plugInType eq 'ASCII_PARSING_PLUGIN') or (plugInType eq 'VARIABLE_LENGTH_ASCII_PARSING_PLUGIN')}">
							<div class="form-group">
								<spring:message code="parsing.service.add.attr.attr.name"
									var="tooltip" ></spring:message>
								<div class="table-cell-label">${tooltip}</div>
								<div class="input-group">
									<input type="text" name="sourceField" id="sourceField"
										class="form-control table-cell input-sm" data-toggle="tooltip"
										data-placement="bottom" title="${tooltip}"> <span
										class="input-group-addon add-on last"> <i
										class="glyphicon glyphicon-alert" data-toggle="tooltip"
										data-placement="left" title=""></i></span>
								</div>
							</div>
							<input type="hidden" id="attributeTypeHidden" value=""/>
						</c:if>
						
						<c:if test="${(plugInType eq 'VARIABLE_LENGTH_ASCII_PARSING_PLUGIN') or (plugInType eq 'VARIABLE_LENGTH_BINARY_PARSING_PLUGIN')}">
							<div class="form-group">
								<spring:message code="parsing.service.add.attr.src.field.name"
									var="tooltip" ></spring:message>
								<div class="table-cell-label">${tooltip}</div>
								<div class="input-group">
									<input type="text" name="sourceFieldName" id="sourceFieldName"
										class="form-control table-cell input-sm" data-toggle="tooltip"
										data-placement="bottom" title="${tooltip}"> <span
										class="input-group-addon add-on last"> <i
										class="glyphicon glyphicon-alert" data-toggle="tooltip"
										data-placement="left" title=""></i></span>
								</div>
							</div>
							<input type="hidden" id="attributeTypeHidden" value=""/>
						</c:if>
						
						<c:if test="${(plugInType eq 'FIXED_LENGTH_ASCII_PARSING_PLUGIN') or (plugInType eq 'FIXED_LENGTH_BINARY_PARSING_PLUGIN') or (plugInType eq 'VARIABLE_LENGTH_BINARY_PARSING_PLUGIN')}">
							<input type="hidden" name="sourceField" id="sourceField" value=""/>
							<input type="hidden" id="attributeTypeHidden" value=""/>
						</c:if>
						
						<c:if test="${((plugInType != 'HTML_PARSING_PLUGIN') and (plugInType != 'XLS_PARSING_PLUGIN'))}">
							<div class="form-group">
								<spring:message code="parsing.service.add.attr.desc"
									var="tooltip" ></spring:message>
								<div class="table-cell-label">${tooltip}</div>
								<div class="input-group">
									<input type="text" name="description" id="description"
										class="form-control table-cell input-sm" data-toggle="tooltip"
										data-placement="bottom" title="${tooltip}"> <span
										class="input-group-addon add-on last"> <i
										class="glyphicon glyphicon-alert" data-toggle="tooltip"
										data-placement="left" title=""></i></span>
								</div>
							</div>
						</c:if>
						
						<!-- Start : Code For MED-9753 -->
						<div class="form-group">
							<spring:message code="parsing.service.add.attr.uni.field" var="tooltip" ></spring:message>
							<div class="table-cell-label">${tooltip}
							<c:if test="${(plugInType ne 'ASN1_PARSING_PLUGIN') and (plugInType ne 'RAP_PARSING_PLUGIN') and (plugInType ne 'TAP_PARSING_PLUGIN') and (plugInType ne 'NRTRDE_PARSING_PLUGIN') and (plugInType ne 'VARIABLE_LENGTH_BINARY_PARSING_PLUGIN') and (plugInType ne 'VARIABLE_LENGTH_ASCII_PARSING_PLUGIN') and (plugInType ne 'XLS_PARSING_PLUGIN') and (plugInType ne 'HTML_PARSING_PLUGIN') and (plugInType ne 'PDF_PARSING_PLUGIN')}">
							<span class="required">*</span>
							</c:if>
							</div>
							<jsp:include page="../../common/autocomplete.jsp">
								<jsp:param name="unifiedField" value="unifiedField" ></jsp:param>
							</jsp:include>
						</div>
						<!-- End : Code For MED-9753 -->
						
						<c:if test="${(plugInType eq 'VARIABLE_LENGTH_BINARY_PARSING_PLUGIN') and (REQUEST_ACTION_TYPE eq 'VAR_LENGTH_BINARY_HEADER_PARSER_ATTRIBUTE')}">
							<div class="form-group">
								<spring:message code="parsing.service.attr.grid.column.startLength"
									var="tooltip" ></spring:message>
								<div class="table-cell-label">${tooltip}</div>
								<div class="input-group">
									<input type="text" name="startLength" id="startLength"
										class="form-control table-cell input-sm" data-toggle="tooltip"
										data-placement="bottom" title="${tooltip}" onkeydown="isNumericOnKeyDown(event)" maxlength="7"> <span
										class="input-group-addon add-on last" > <i
										class="glyphicon glyphicon-alert" data-toggle="tooltip"
										data-placement="left" title=""></i></span>
								</div>
							</div>
						</c:if>
						
						<c:if test="${(plugInType eq 'FIXED_LENGTH_ASCII_PARSING_PLUGIN') }">
							<div class="form-group">
								<spring:message code="parsing.service.attr.grid.column.startLength"
									var="tooltip" ></spring:message>
								<div class="table-cell-label">${tooltip}</div>
								<div class="input-group">
									<input type="text" name="startLength" id="startLength"
										class="form-control table-cell input-sm" data-toggle="tooltip"
										data-placement="bottom" title="${tooltip}" onkeydown="isNumericOnKeyDown(event)" maxlength="7"> <span
										class="input-group-addon add-on last" > <i
										class="glyphicon glyphicon-alert" data-toggle="tooltip"
										data-placement="left" title=""></i></span>
								</div>
							</div>
						</c:if>
						<c:if test="${(plugInType eq 'FIXED_LENGTH_BINARY_PARSING_PLUGIN') }">
							<div class="form-group">
								<spring:message code="parsing.service.attr.grid.column.startLength"
									var="tooltip" ></spring:message>
								<div class="table-cell-label">${tooltip}<span class="required">*</span></div>
								<div class="input-group">
									<input type="text" name="startLength" id="startLength"
										class="form-control table-cell input-sm" data-toggle="tooltip"
										data-placement="bottom" title="${tooltip}" onkeydown="isNumericWithMinusOneOnKeyDown(event)" onblur="convertToIntergerValue(event)" maxlength="7"> <span
										class="input-group-addon add-on last" > <i
										class="glyphicon glyphicon-alert" data-toggle="tooltip"
										data-placement="left" title=""></i></span>
								</div>
							</div>
						</c:if>
						<c:if test="${(plugInType eq 'FIXED_LENGTH_ASCII_PARSING_PLUGIN') }">
							<div class="form-group">
								<spring:message code="parsing.service.attr.grid.column.endLength"
									var="tooltip" ></spring:message>
								<div class="table-cell-label">${tooltip}</div>
								<div class="input-group">
									<input type="text" name="endLength" id="endLength"
										class="form-control table-cell input-sm" data-toggle="tooltip"
										data-placement="bottom" title="${tooltip}" onkeydown="isNumericOnKeyDown(event)" maxlength="7"> <span
										class="input-group-addon add-on last"    > <i
										class="glyphicon glyphicon-alert" data-toggle="tooltip"
										data-placement="left" title=""></i></span>
								</div>
							</div>
						</c:if>
						<c:if test="${(plugInType eq 'VARIABLE_LENGTH_BINARY_PARSING_PLUGIN') and (REQUEST_ACTION_TYPE eq 'VAR_LENGTH_BINARY_HEADER_PARSER_ATTRIBUTE')}">
							<div class="form-group">
								<spring:message code="parsing.service.attr.grid.column.endLength"
									var="tooltip" ></spring:message>
								<div class="table-cell-label">${tooltip}</div>
								<div class="input-group">
									<input type="text" name="endLength" id="endLength"
										class="form-control table-cell input-sm" data-toggle="tooltip"
										data-placement="bottom" title="${tooltip}" onkeydown="isNumericOnKeyDown(event)" maxlength="7"> <span
										class="input-group-addon add-on last"    > <i
										class="glyphicon glyphicon-alert" data-toggle="tooltip"
										data-placement="left" title=""></i></span>
								</div>
							</div>
						</c:if>
						<c:if test="${(plugInType eq 'FIXED_LENGTH_BINARY_PARSING_PLUGIN') }">
							<div class="form-group">
								<spring:message code="parsing.service.attr.grid.column.endLength"
									var="tooltip" ></spring:message>
								<div class="table-cell-label">${tooltip}<span class="required">*</span></div>
								<div class="input-group">
									<input type="text" name="endLength" id="endLength"
										class="form-control table-cell input-sm" data-toggle="tooltip"
										data-placement="bottom" title="${tooltip}" onkeydown="isNumericWithMinusOneOnKeyDown(event)" onblur="convertToIntergerValue(event)" maxlength="7"> <span
										class="input-group-addon add-on last"    > <i
										class="glyphicon glyphicon-alert" data-toggle="tooltip"
										data-placement="left" title=""></i></span>
								</div>
							</div>
							
							<div class="form-group">
							<spring:message code="fixed.length.binary.parser.read.as.bit"
								var="tooltip" ></spring:message>
							<div class="table-cell-label">${tooltip}</div>
								<div class="input-group">
									<select name="readAsBits" id="readAsBits" onchange="handleReadAsBitsEvent();"
										class="form-control table-cell input-sm" data-toggle="tooltip"
										data-placement="bottom" title="${tooltip}">
										<c:forEach var="trueFalseEnum" items="${trueFalseEnum}" >
											<option value="${trueFalseEnum.name}">${trueFalseEnum}</option>
										</c:forEach>
									</select> 
									<span class="input-group-addon add-on last"> 
										<i class="glyphicon glyphicon-alert" data-toggle="tooltip"
										data-placement="left" title=""></i>
									</span>
								</div>
							</div>
							<div class="form-group">
								<spring:message code="fixed.length.binary.parser.bit.start.length"
									var="tooltip" ></spring:message>
								<div class="table-cell-label">${tooltip}<span class="required">*</span></div>
								<div class="input-group">
									<input type="text" name="bitStartLength" id="bitStartLength"
										class="form-control table-cell input-sm" data-toggle="tooltip"
										data-placement="bottom" title="${tooltip}" onkeydown="isNumericOnKeyDown(event)" maxlength="7"> <span
										class="input-group-addon add-on last" > <i
										class="glyphicon glyphicon-alert" data-toggle="tooltip"
										data-placement="left" title=""></i></span>
								</div>
							</div>
							<div class="form-group">
								<spring:message code="fixed.length.binary.parser.bit.end.length"
									var="tooltip" ></spring:message>
								<div class="table-cell-label">${tooltip}<span class="required">*</span></div>
								<div class="input-group">
									<input type="text" name="bitEndLength" id="bitEndLength"
										class="form-control table-cell input-sm" data-toggle="tooltip"
										data-placement="bottom" title="${tooltip}" onkeydown="isNumericOnKeyDown(event)" maxlength="7"> <span
										class="input-group-addon add-on last" > <i
										class="glyphicon glyphicon-alert" data-toggle="tooltip"
										data-placement="left" title=""></i></span>
								</div>
							</div>
						</c:if>
						
						<c:if test="${(plugInType eq 'PDF_PARSING_PLUGIN') }">
							<c:if test="${(isGroupConfiguration) }">
							
							<div class="form-group">
							
									<spring:message code="parsing.service.attr.grid.column.referenceCol"
									var="tooltip" ></spring:message>
								<div class="table-cell-label">${tooltip}</div>
								<div class="input-group">
										<input type="text" name="referenceCol" id="referenceCol"
										class="form-control table-cell input-sm" data-toggle="tooltip"
										data-placement="bottom" title="${tooltip}" > <span
										class="input-group-addon add-on last" > <i
										class="glyphicon glyphicon-alert" data-toggle="tooltip"
										data-placement="left" title=""></i></span>
								</div>
							</div>
							
							<div class="form-group">
								<spring:message code="parsing.service.attr.grid.column.referenceRow"
									var="tooltip" ></spring:message>
								<div class="table-cell-label">${tooltip}</div>
								<div class="input-group">
									<input type="text" name="referenceRow" id="referenceRow"
										class="form-control table-cell input-sm" data-toggle="tooltip"
										data-placement="bottom" title="${tooltip}" > <span
										class="input-group-addon add-on last" > <i
										class="glyphicon glyphicon-alert" data-toggle="tooltip"
										data-placement="left" title=""></i></span>
								</div>
							</div>
								<div class="form-group">
									<spring:message code="parsing.service.attr.grid.column.tableFooter"
										var="tooltip" ></spring:message>
									<div class="table-cell-label">${tooltip}</div>
									<div class="input-group">
										<select name="tableFooter" id="tableFooter" 
											class="form-control table-cell input-sm" data-toggle="tooltip"
											data-placement="bottom" title="${tooltip}">
											<c:forEach var="trueFalseEnum" items="${trueFalseEnum}" >
												<option value="${trueFalseEnum.name}">${trueFalseEnum}</option>
											</c:forEach>
										</select> 
										<span class="input-group-addon add-on last"> 
											<i class="glyphicon glyphicon-alert" data-toggle="tooltip"
											data-placement="left" title=""></i>
										</span>
									</div>
								</div>
								
								<div class="form-group">
									<spring:message code="parsing.service.attr.grid.column.multiline.attribute"
										var="tooltip" ></spring:message>
									<div class="table-cell-label">${tooltip}</div>
									<div class="input-group">
										<select name="multiLineAttribute" id="multiLineAttribute" 
											class="form-control table-cell input-sm" data-toggle="tooltip"
											data-placement="bottom" title="${tooltip}">
											<c:forEach var="trueFalseEnum" items="${trueFalseEnum}" >
												<option value="${trueFalseEnum.name}">${trueFalseEnum}</option>
											</c:forEach>
										</select> 
										<span class="input-group-addon add-on last"> 
											<i class="glyphicon glyphicon-alert" data-toggle="tooltip"
											data-placement="left" title=""></i>
										</span>
									</div>
								</div>
								
								<div class="form-group">
									<spring:message code="parsing.service.attr.grid.column.row.text.alignment"
										var="tooltip" ></spring:message>
									<div class="table-cell-label">${tooltip}</div>
									<div class="input-group">
										<input type="text" name="rowTextAlignment" id="rowTextAlignment"
											class="form-control table-cell input-sm" data-toggle="tooltip"
											data-placement="bottom" title="${tooltip}" > <span
											class="input-group-addon add-on last" > <i
											class="glyphicon glyphicon-alert" data-toggle="tooltip"
											data-placement="left" title=""></i></span>
									</div>
								</div>
								
								<div class="form-group">
									<spring:message code="parsing.service.attr.grid.column.mandatory"
										var="tooltip" ></spring:message>
									<div class="table-cell-label">${tooltip}</div>
									<div class="input-group">
										<input type="text" name="mandatory" id="mandatory"
											class="form-control table-cell input-sm" data-toggle="tooltip"
											data-placement="bottom" title="${tooltip}" > <span
											class="input-group-addon add-on last" > <i
											class="glyphicon glyphicon-alert" data-toggle="tooltip"
											data-placement="left" title=""></i></span>
									</div>
								</div>
							</c:if>
							<c:if test="${!(isGroupConfiguration) }">
								<div class="form-group">
									<spring:message code="parsing.service.attr.grid.column.location" var="tooltip" ></spring:message>
									<div class="table-cell-label">${tooltip}</div>
									<div class="input-group">
										<input type="text" name="location" id="location"
											class="form-control table-cell input-sm" data-toggle="tooltip"
											data-placement="bottom" title="${tooltip}" > <span
											class="input-group-addon add-on last" > <i
											class="glyphicon glyphicon-alert" data-toggle="tooltip"
											data-placement="left" title=""></i></span>
									</div>
								</div>
								<div class="form-group">
									<spring:message code="parsing.service.attr.grid.column.columnStartLocation"
										var="tooltip" ></spring:message>
									<div class="table-cell-label">${tooltip}</div>
									<div class="input-group">
										<input type="text" name="columnStartLocation" id="columnStartLocation"
											class="form-control table-cell input-sm" data-toggle="tooltip"
											data-placement="bottom" title="${tooltip}" > <span
											class="input-group-addon add-on last" > <i
											class="glyphicon glyphicon-alert" data-toggle="tooltip"
											data-placement="left" title=""></i></span>
									</div>
								</div>
								<div class="form-group">
									<spring:message code="parsing.service.attr.grid.column.columnIdentifier"
										var="tooltip" ></spring:message>
									<div class="table-cell-label">${tooltip}</div>
									<div class="input-group">
										<input type="text" name="columnIdentifier" id="columnIdentifier"
											class="form-control table-cell input-sm" data-toggle="tooltip"
											data-placement="bottom" title="${tooltip}" > <span
											class="input-group-addon add-on last" > <i
											class="glyphicon glyphicon-alert" data-toggle="tooltip"
											data-placement="left" title=""></i></span>
									</div>
								</div>
								<div class="form-group">
									<spring:message code="parsing.service.attr.grid.column.pageNumber"
										var="tooltip" ></spring:message>
									<div class="table-cell-label">${tooltip}</div>
									<div class="input-group">
										<input type="text" name="pageNumber" id="pageNumber"
											class="form-control table-cell input-sm" data-toggle="tooltip"
											data-placement="bottom" title="${tooltip}" > <span
											class="input-group-addon add-on last" > <i
											class="glyphicon glyphicon-alert" data-toggle="tooltip"
											data-placement="left" title=""></i></span>
									</div>
								</div>
 								<div class="form-group">
									<spring:message code="parsing.service.attr.grid.column.referenceRow"
										var="tooltip" ></spring:message>
									<div class="table-cell-label">${tooltip}</div>
									<div class="input-group">
										<input type="text" name="referenceRow" id="referenceRow"
											class="form-control table-cell input-sm" data-toggle="tooltip"
											data-placement="bottom" title="${tooltip}" > <span
											class="input-group-addon add-on last" > <i
											class="glyphicon glyphicon-alert" data-toggle="tooltip"
											data-placement="left" title=""></i></span>
									</div>
								</div>
							</c:if>
							
							<div class="form-group">
								<spring:message code="parsing.service.attr.grid.column.columnStartsWith"
									var="tooltip" ></spring:message>
								<div class="table-cell-label">${tooltip}</div>
								<div class="input-group">
									<input type="text" name="columnStartsWith" id="columnStartsWith"
										class="form-control table-cell input-sm" data-toggle="tooltip"
										data-placement="bottom" title="${tooltip}" > <span
										class="input-group-addon add-on last" > <i
										class="glyphicon glyphicon-alert" data-toggle="tooltip"
										data-placement="left" title=""></i></span>
								</div>
							</div>
							
							<div class="form-group">
									<spring:message code="parsing.service.attr.grid.column.multipleValues"
										var="tooltip" ></spring:message>
									<div class="table-cell-label">${tooltip}</div>
									<div class="input-group">
										<select name="multipleValues" id="multipleValues" 
											class="form-control table-cell input-sm" data-toggle="tooltip"
											data-placement="bottom" title="${tooltip}">
											<c:forEach var="trueFalseEnum" items="${trueFalseEnum}" >
												<option value="${trueFalseEnum.name}">${trueFalseEnum}</option>
											</c:forEach>
										</select> 
										<span class="input-group-addon add-on last"> 
											<i class="glyphicon glyphicon-alert" data-toggle="tooltip"
											data-placement="left" title=""></i>
										</span>
									</div>
								</div>
						</c:if>
						
						<!-- JIRA-6241 start-->	
						<c:if test="${(plugInType eq 'DETAIL_LOCAL_PARSING_PLUGIN')  or (plugInType eq 'ASCII_PARSING_PLUGIN') or (plugInType eq 'VARIABLE_LENGTH_ASCII_PARSING_PLUGIN')  or (plugInType eq 'VARIABLE_LENGTH_BINARY_PARSING_PLUGIN') or (plugInType eq 'JSON_PARSING_PLUGIN')}">
						<div class="form-group">
							<spring:message code="fixed.length.ascii.parser.date.format"
								var="tooltip" ></spring:message>
							<div class="table-cell-label">${tooltip}</div>
							<div class="input-group">
									<select name="sourceFieldFormat" id="sourceFieldFormat"
										class="form-control table-cell input-sm" data-toggle="tooltip"
										data-placement="bottom" title="${tooltip}" onChange="changeSourceFieldFormat();">
										<option value = "">Select</option>
										<c:forEach var="sourceFieldFormat" items="${sourceFieldFormat}" >
											<option value="${sourceFieldFormat}">${sourceFieldFormat.name}</option>
										</c:forEach>
									</select> <span class="input-group-addon add-on last"> <i
										class="glyphicon glyphicon-alert" data-toggle="tooltip"
										data-placement="left" title=""></i></span>
							</div>
						</div>
						
						<!--  input box for type date format in input box-->
						 <div class="form-group" id="dateFormatDiv" style="display: none;">
							<spring:message code="fixed.length.ascii.composer.date.format"
								var="tooltip" ></spring:message>
							<div class="table-cell-label">${tooltip}</div>
							<div class="input-group">
								<input type="text" name="dateFormatInput" id="dateFormatInput"
									class="form-control table-cell input-sm" data-toggle="tooltip"
									data-placement="bottom" title="${tooltip}"> <span
									class="input-group-addon add-on last"> <i
									class="glyphicon glyphicon-alert" data-toggle="tooltip"
									data-placement="left" title=""></i></span>
							</div>
						</div> 
						</c:if>
						
						<!-- JIRA-6241 end-->	
						<c:if test="${(plugInType eq 'FIXED_LENGTH_ASCII_PARSING_PLUGIN') or (plugInType eq 'XML_PARSING_PLUGIN')}">
						<div class="form-group">
							<spring:message code="fixed.length.ascii.parser.date.format"
								var="tooltip" ></spring:message>
							<div class="table-cell-label">${tooltip}</div>
							<div class="input-group">
								<input type="text" name="sourceFieldFormat" id="sourceFieldFormat"
									class="form-control table-cell input-sm" data-toggle="tooltip"
									data-placement="bottom" title="${tooltip}"> <span
									class="input-group-addon add-on last"> <i
									class="glyphicon glyphicon-alert" data-toggle="tooltip"
									data-placement="left" title=""></i></span>
							</div>
						</div>
						</c:if>
						
						<c:if test="${plugInType eq 'NATFLOW_PARSING_PLUGIN'}">
						<div class="form-group">
							<spring:message code="natflow.parser.source.field.format"
								var="tooltip" ></spring:message>
							<div class="table-cell-label">${tooltip}</div>
							<div class="input-group">
									<select name="sourceFieldFormat" id="sourceFieldFormat"
										class="form-control table-cell input-sm" data-toggle="tooltip"
										data-placement="bottom" title="${tooltip}" onChange="changeSourceFieldFormat();">
										<option value = "">Select</option>
										<c:forEach var="sourceFieldFormat" items="${sourceFieldFormat}" >
											<option value="${sourceFieldFormat}">${sourceFieldFormat.name}</option>
										</c:forEach>
									</select> <span class="input-group-addon add-on last"> <i
										class="glyphicon glyphicon-alert" data-toggle="tooltip"
										data-placement="left" title=""></i></span>
							</div>
						</div>
						<!--  input box for type destination date format in input box-->
						 <div class="form-group" id="dateFormatDiv" style="display: none;">
							<spring:message code="parsing.service.attr.grid.column.dest.date.format"
								var="tooltip" ></spring:message>
							<div class="table-cell-label">${tooltip}</div>
							<div class="input-group">
								<input type="text" name="dateFormatInput" id="dateFormatInput"
									class="form-control table-cell input-sm" data-toggle="tooltip"
									data-placement="bottom" title="${tooltip}"> <span
									class="input-group-addon add-on last"> <i
									class="glyphicon glyphicon-alert" data-toggle="tooltip"
									data-placement="left" title=""></i></span>
							</div>
						</div> 
						</c:if>

						<c:if test="${plugInType eq 'FIXED_LENGTH_BINARY_PARSING_PLUGIN'}">
						<div class="form-group">
							<spring:message code="fixed.length.ascii.parser.date.format"
								var="tooltip" ></spring:message>
							<div class="table-cell-label">${tooltip}</div>
							<div class="input-group">
									<select name="sourceFieldFormat" id="sourceFieldFormat"
										class="form-control table-cell input-sm" data-toggle="tooltip"
										data-placement="bottom" title="${tooltip}">
										<c:forEach var="sourceFieldFormat" items="${sourceFieldFormat}" >
											<option value="${sourceFieldFormat}">${sourceFieldFormat.name}</option>
										</c:forEach>
									</select> <span class="input-group-addon add-on last"> <i
										class="glyphicon glyphicon-alert" data-toggle="tooltip"
										data-placement="left" title=""></i></span>
							</div>
						</div>
						</c:if>
						
						<c:if test="${(plugInType eq 'FIXED_LENGTH_ASCII_PARSING_PLUGIN') or (plugInType eq 'FIXED_LENGTH_BINARY_PARSING_PLUGIN') }">
							<div class="form-group">
								<spring:message code="parsing.service.attr.grid.column.prefix"
									var="tooltip" ></spring:message>
								<div class="table-cell-label">${tooltip}</div>
								<div class="input-group">
									<input type="text" name="prefix" id="prefix"
										class="form-control table-cell input-sm" data-toggle="tooltip"
										data-placement="bottom" title="${tooltip}"> <span
										class="input-group-addon add-on last"> <i
										class="glyphicon glyphicon-alert" data-toggle="tooltip"
										data-placement="left" title=""></i></span>
								</div>
							</div>
						</c:if>
						
						<c:if test="${(plugInType eq 'VARIABLE_LENGTH_BINARY_PARSING_PLUGIN') and (REQUEST_ACTION_TYPE eq 'VAR_LENGTH_BINARY_PARSER_ATTRIBUTE')}">
							<div class="form-group">
								<spring:message code="parsing.service.attr.grid.column.prefix"
									var="tooltip" ></spring:message>
								<div class="table-cell-label">${tooltip}</div>
								<div class="input-group">
									<input type="text" name="prefix" id="prefix"
										class="form-control table-cell input-sm" data-toggle="tooltip"
										data-placement="bottom" title="${tooltip}"> <span
										class="input-group-addon add-on last"> <i
										class="glyphicon glyphicon-alert" data-toggle="tooltip"
										data-placement="left" title=""></i></span>
								</div>
							</div>
						</c:if>
						
						<c:if test="${(plugInType eq 'FIXED_LENGTH_ASCII_PARSING_PLUGIN') or (plugInType eq 'FIXED_LENGTH_BINARY_PARSING_PLUGIN')}">
							<div class="form-group">
								<spring:message code="parsing.service.attr.grid.column.postfix"
									var="tooltip" ></spring:message>
								<div class="table-cell-label">${tooltip}</div>
								<div class="input-group">
									<input type="text" name="postfix" id="postfix"
										class="form-control table-cell input-sm" data-toggle="tooltip"
										data-placement="bottom" title="${tooltip}"> <span
										class="input-group-addon add-on last"> <i
										class="glyphicon glyphicon-alert" data-toggle="tooltip"
										data-placement="left" title=""></i></span>
								</div>
							</div>
						</c:if>
						
						<c:if test="${(plugInType eq 'VARIABLE_LENGTH_BINARY_PARSING_PLUGIN') and (REQUEST_ACTION_TYPE eq 'VAR_LENGTH_BINARY_PARSER_ATTRIBUTE')}">
							<div class="form-group">
								<spring:message code="parsing.service.attr.grid.column.postfix"
									var="tooltip" ></spring:message>
								<div class="table-cell-label">${tooltip}</div>
								<div class="input-group">
									<input type="text" name="postfix" id="postfix"
										class="form-control table-cell input-sm" data-toggle="tooltip"
										data-placement="bottom" title="${tooltip}"> <span
										class="input-group-addon add-on last"> <i
										class="glyphicon glyphicon-alert" data-toggle="tooltip"
										data-placement="left" title=""></i></span>
								</div>
							</div>
						</c:if>
						
						<c:if test="${(plugInType eq 'ASN1_PARSING_PLUGIN' || plugInType eq 'RAP_PARSING_PLUGIN' || plugInType eq 'TAP_PARSING_PLUGIN' || plugInType eq 'NRTRDE_PARSING_PLUGIN')}">
							<div class="form-group">
								<spring:message code="parser.grid.asn1datatype.format.label.tooltip"
									var="tooltip" ></spring:message>
								<div class="table-cell-label">${tooltip}</div>
								<div class="input-group">
									<input type="text" name="ASN1DataType" id="ASN1DataType"
										class="form-control table-cell input-sm" data-toggle="tooltip"
										data-placement="bottom" title="${tooltip}"> <span
										class="input-group-addon add-on last"> <i
										class="glyphicon glyphicon-alert" data-toggle="tooltip"
										data-placement="left" title=""></i></span>
								</div>
							</div>
							<input type="hidden" id="attributeTypeHidden" value=""/>
						</c:if>
						<c:if test="${(plugInType != 'VARIABLE_LENGTH_BINARY_PARSING_PLUGIN')}">
						<div class="form-group">
							<spring:message code="parsing.service.add.attr.default.val"
								var="tooltip" ></spring:message>
							<div class="table-cell-label">${tooltip}</div>
							<div class="input-group">
								<input type="text" name="defaultValue" id="defaultValue"
									class="form-control table-cell input-sm" data-toggle="tooltip"
									data-placement="bottom" title="${tooltip}"> <span
									class="input-group-addon add-on last"> <i
									class="glyphicon glyphicon-alert" data-toggle="tooltip"
									data-placement="left" title=""></i></span>
							</div>
						</div>
						</c:if>
						
						<c:if test="${(plugInType eq 'VARIABLE_LENGTH_BINARY_PARSING_PLUGIN') and (REQUEST_ACTION_TYPE eq 'VAR_LENGTH_BINARY_PARSER_ATTRIBUTE')}">
							<div class="form-group">
								<spring:message code="parsing.service.add.attr.default.val"
									var="tooltip" ></spring:message>
								<div class="table-cell-label">${tooltip}</div>
								<div class="input-group">
									<input type="text" name="defaultValue" id="defaultValue"
										class="form-control table-cell input-sm" data-toggle="tooltip"
										data-placement="bottom" title="${tooltip}"> <span
										class="input-group-addon add-on last"> <i
										class="glyphicon glyphicon-alert" data-toggle="tooltip"
										data-placement="left" title=""></i></span>
								</div>
							</div>
						</c:if>
						
						<c:if test="${(plugInType eq 'PDF_PARSING_PLUGIN') and (!(isGroupConfiguration)) }">
							<div class="form-group">
								<spring:message code="parsing.service.attr.grid.column.valueSeparator.field.attribute"
									var="tooltip" ></spring:message>
								<div class="table-cell-label">${tooltip}</div>
								<div class="input-group">
									<input type="text" name="valueSeparator" id="valueSeparator"
										class="form-control table-cell input-sm" data-toggle="tooltip"
										data-placement="bottom" title="${tooltip}" > <span
										class="input-group-addon add-on last" > <i
										class="glyphicon glyphicon-alert" data-toggle="tooltip"
										data-placement="left" title=""></i></span>
								</div>
							</div>
							<div class="form-group">
								<spring:message code="parsing.service.attr.grid.column.columnEndsWith"
									var="tooltip" ></spring:message>
								<div class="table-cell-label">${tooltip}</div>
								<div class="input-group">
									<input type="text" name="columnEndsWith" id="columnEndsWith"
										class="form-control table-cell input-sm" data-toggle="tooltip"
										data-placement="bottom" title="${tooltip}" > <span
										class="input-group-addon add-on last" > <i
										class="glyphicon glyphicon-alert" data-toggle="tooltip"
										data-placement="left" title=""></i></span>
								</div>
							</div>
							<div class="form-group">
								<spring:message code="parsing.service.attr.grid.column.mandatory"
									var="tooltip" ></spring:message>
								<div class="table-cell-label">${tooltip}</div>
								<div class="input-group">
									<input type="text" name="mandatory" id="mandatory"
										class="form-control table-cell input-sm" data-toggle="tooltip"
										data-placement="bottom" title="${tooltip}" > <span
										class="input-group-addon add-on last" > <i
										class="glyphicon glyphicon-alert" data-toggle="tooltip"
										data-placement="left" title=""></i></span>
								</div>
							</div>
						</c:if>
						
						<c:if test="${(plugInType != 'HTML_PARSING_PLUGIN') and (plugInType != 'VARIABLE_LENGTH_BINARY_PARSING_PLUGIN')}">
						<div class="form-group">
							<spring:message code="parsing.service.add.attr.trim.char" var="tooltip" ></spring:message>
							<div class="table-cell-label">${tooltip}</div>
							<div class="input-group">
								<input type="text" name="trimChars" id="trimChars"
									class="form-control table-cell input-sm" data-toggle="tooltip"
									data-placement="bottom" title="${tooltip}"> <span
									class="input-group-addon add-on last"> <i
									class="glyphicon glyphicon-alert" data-toggle="tooltip"
									data-placement="left" title=""></i></span>
							</div>
						</div>
						</c:if>
						
						<c:if test="${(plugInType eq 'VARIABLE_LENGTH_BINARY_PARSING_PLUGIN') and (REQUEST_ACTION_TYPE eq 'VAR_LENGTH_BINARY_PARSER_ATTRIBUTE')}">
							<div class="form-group">
								<spring:message code="parsing.service.add.attr.trim.char" var="tooltip" ></spring:message>
								<div class="table-cell-label">${tooltip}</div>
								<div class="input-group">
									<input type="text" name="trimChars" id="trimChars"
										class="form-control table-cell input-sm" data-toggle="tooltip"
										data-placement="bottom" title="${tooltip}"> <span
										class="input-group-addon add-on last"> <i
										class="glyphicon glyphicon-alert" data-toggle="tooltip"
										data-placement="left" title=""></i></span>
								</div>
							</div>
						</c:if>
						
						<c:if test="${(plugInType eq 'ASCII_PARSING_PLUGIN') or (plugInType eq 'VARIABLE_LENGTH_ASCII_PARSING_PLUGIN') or (plugInType eq 'ASN1_PARSING_PLUGIN'|| plugInType eq 'RAP_PARSING_PLUGIN' || plugInType eq 'TAP_PARSING_PLUGIN' || plugInType eq 'NRTRDE_PARSING_PLUGIN') or (plugInType eq 'DETAIL_LOCAL_PARSING_PLUGIN')
							or (plugInType eq 'FIXED_LENGTH_ASCII_PARSING_PLUGIN' || plugInType eq 'PDF_PARSING_PLUGIN' || plugInType eq 'FIXED_LENGTH_BINARY_PARSING_PLUGIN') or (plugInType eq 'NATFLOW_PARSING_PLUGIN') or (plugInType eq 'XML_PARSING_PLUGIN')or (plugInType eq 'XLS_PARSING_PLUGIN') or (plugInType eq 'JSON_PARSING_PLUGIN')}">
						<div class="form-group">
							<spring:message code="parsing.service.add.attr.trim.position" var="tooltip" ></spring:message>
							<div class="table-cell-label">${tooltip}</div>
							<div class="input-group">
								<select name="trimPosition" id="trimPosition"
									class="form-control table-cell input-sm" data-toggle="tooltip"
									data-placement="top" title="${tooltip}">
									<option value="">Select trim position</option>
									<c:forEach var="trimPosition" items="${trimPosition}">
										<option value="${trimPosition.value}">${trimPosition}</option>
									</c:forEach>
								</select> <span class="input-group-addon add-on last"> <i
									class="glyphicon glyphicon-alert" data-toggle="tooltip"
									data-placement="left" title=""></i></span>
							</div>
						</div>
						</c:if>
							
						<c:if test="${(plugInType eq 'VARIABLE_LENGTH_BINARY_PARSING_PLUGIN') and (REQUEST_ACTION_TYPE eq 'VAR_LENGTH_BINARY_PARSER_ATTRIBUTE')}">
							<div class="form-group">
								<spring:message code="parsing.service.add.attr.trim.position" var="tooltip" ></spring:message>
								<div class="table-cell-label">${tooltip}</div>
								<div class="input-group">
									<select name="trimPosition" id="trimPosition"
										class="form-control table-cell input-sm" data-toggle="tooltip"
										data-placement="top" title="${tooltip}">
										<option value="">Select trim position</option>
										<c:forEach var="trimPosition" items="${trimPosition}">
											<option value="${trimPosition.value}">${trimPosition}</option>
										</c:forEach>
									</select> <span class="input-group-addon add-on last"> <i
										class="glyphicon glyphicon-alert" data-toggle="tooltip"
										data-placement="left" title=""></i></span>
								</div>
							</div>
						</c:if>
																
						<c:if test="${(plugInType eq 'FIXED_LENGTH_ASCII_PARSING_PLUGIN') }">
							<div class="form-group">
								<spring:message code="parsing.service.attr.grid.column.length"
									var="tooltip" ></spring:message>
								<div class="table-cell-label">${tooltip}</div>
								<div class="input-group">
									<input type="text" name="length" id="length"
										class="form-control table-cell input-sm" data-toggle="tooltip"
										data-placement="bottom" title="${tooltip}"> <span
										class="input-group-addon add-on last"> <i
										class="glyphicon glyphicon-alert" data-toggle="tooltip"
										data-placement="left" title=""></i></span>
								</div>
							</div>
						</c:if>
						<c:if test="${(plugInType eq 'FIXED_LENGTH_BINARY_PARSING_PLUGIN')}">
							<div class="form-group">
								<spring:message code="parsing.service.attr.grid.column.length"
									var="tooltip" ></spring:message>
								<div class="table-cell-label">${tooltip}</div>
								<div class="input-group">
									<input type="text" name="length" id="length"
										class="form-control table-cell input-sm" data-toggle="tooltip"
										data-placement="bottom" title="${tooltip}" onkeydown="isNumericOnKeyDown(event)" maxlength="7"> <span
										class="input-group-addon add-on last"> <i
										class="glyphicon glyphicon-alert" data-toggle="tooltip"
										data-placement="left" title=""></i></span>
								</div>
							</div>
							<div class="form-group">
								<spring:message code="fixed.length.binary.parser.attr.grid.column.multiRecord"
									var="tooltip" ></spring:message>
								<div class="table-cell-label">${tooltip}</div>
								<div class="input-group">
									<select name="multiRecord" id="multiRecord" 
										class="form-control table-cell input-sm" data-toggle="tooltip"
										data-placement="bottom" title="${tooltip}">
										<c:forEach var="trueFalseEnum" items="${trueFalseEnum}" >
											<option value="${trueFalseEnum.name}">${trueFalseEnum}</option>
										</c:forEach>
									</select> 
									<span class="input-group-addon add-on last"> 
										<i class="glyphicon glyphicon-alert" data-toggle="tooltip"
										data-placement="left" title=""></i>
									</span>
								</div>
							</div>
						</c:if>
						
						<c:if test="${(plugInType eq 'VARIABLE_LENGTH_BINARY_PARSING_PLUGIN') and (REQUEST_ACTION_TYPE eq 'VAR_LENGTH_BINARY_PARSER_ATTRIBUTE')}">
							<div class="form-group">
								<spring:message code="parsing.service.attr.grid.column.length"
									var="tooltip" ></spring:message>
								<div class="table-cell-label">${tooltip}</div>
								<div class="input-group">
									<input type="text" name="length" id="length"
										class="form-control table-cell input-sm" data-toggle="tooltip"
										data-placement="bottom" title="${tooltip}" onkeydown="isNumericOnKeyDown(event)" maxlength="7"> <span
										class="input-group-addon add-on last"> <i
										class="glyphicon glyphicon-alert" data-toggle="tooltip"
										data-placement="left" title=""></i></span>
								</div>
							</div>
						</c:if>
						
						<c:if test="${(plugInType eq 'FIXED_LENGTH_ASCII_PARSING_PLUGIN')}">
							<div class="form-group">
								<spring:message code="parsing.service.attr.grid.column.rightDelimiter"
									var="tooltip" ></spring:message>
								<div class="table-cell-label">${tooltip}</div>
								<div class="input-group">
									<input type="text" name="rightDelimiter" id="rightDelimiter"
										class="form-control table-cell input-sm" data-toggle="tooltip"
										data-placement="bottom" title="${tooltip}"> <span
										class="input-group-addon add-on last"> <i
										class="glyphicon glyphicon-alert" data-toggle="tooltip"
										data-placement="left" title=""></i></span>
								</div>
							</div>
						</c:if>
						
						<c:if test="${(plugInType eq 'VARIABLE_LENGTH_BINARY_PARSING_PLUGIN') and (REQUEST_ACTION_TYPE eq 'VAR_LENGTH_BINARY_PARSER_ATTRIBUTE')}">
							<div class="form-group">
								<spring:message code="parsing.service.attr.grid.column.rightDelimiter"
									var="tooltip" ></spring:message>
								<div class="table-cell-label">${tooltip}</div>
								<div class="input-group">
									<input type="text" name="rightDelimiter" id="rightDelimiter"
										class="form-control table-cell input-sm" data-toggle="tooltip"
										data-placement="bottom" title="${tooltip}"> <span
										class="input-group-addon add-on last"> <i
										class="glyphicon glyphicon-alert" data-toggle="tooltip"
										data-placement="left" title=""></i></span>
								</div>
							</div>
						</c:if>
						
						<!-- Start : Code For MED-9753 -->
						<c:if test="${(plugInType eq 'ASCII_PARSING_PLUGIN')}">
								<div class="form-group">
									<spring:message code="parsing.service.add.attr.ip.port.uni.field" var="tooltip" ></spring:message>
									<div class="table-cell-label">${tooltip}</div>
									<jsp:include page="../../common/autocomplete.jsp">
										<jsp:param name="unifiedField" value="portUnifiedField" ></jsp:param>
									</jsp:include>
								</div>
							</c:if>
						<!-- End : Code For MED-9753 -->
						
						<c:if test="${plugInType eq 'ASCII_PARSING_PLUGIN'}">
							<div class="form-group">
								<spring:message code="parsing.service.add.attr.ip.port.seperator"
									var="tooltip" ></spring:message>
								<div class="table-cell-label">${tooltip}</div>
								<div class="input-group">
									<select name="ipPortDelimiter" id="ipPortDelimiter"
										class="form-control table-cell input-sm" data-toggle="tooltip"
										data-placement="bottom" title="${tooltip}">
										<option value = "">Select IP-Port Seperator</option>
										<c:forEach var="ipPortFieldSeperator" items="${ipPortFieldSeperator}">
											<option value="${ipPortFieldSeperator.value}">${ipPortFieldSeperator.input}</option>
										</c:forEach>
									</select> <span class="input-group-addon add-on last"> <i
										class="glyphicon glyphicon-alert" data-toggle="tooltip"
										data-placement="left" title=""></i></span>
									<input type="text" name="ipPortSeperator" id="ipPortSeperator"
										class="form-control table-cell input-sm" data-toggle="tooltip"
										data-placement="bottom" title="${tooltip}"> 
									<span class="input-group-addon add-on last"> <i
										class="glyphicon glyphicon-alert" data-toggle="tooltip"
										data-placement="left" title=""></i></span>
								</div>
							</div>
						</c:if> 
						
						<c:if test="${(plugInType eq 'ASN1_PARSING_PLUGIN') || plugInType eq 'RAP_PARSING_PLUGIN' || plugInType eq 'TAP_PARSING_PLUGIN' || plugInType eq 'NRTRDE_PARSING_PLUGIN'}">
						<div class="form-group">
							<spring:message code="parser.grid.srcDataFormat.format.label"
								var="tooltip" ></spring:message>
							<div class="table-cell-label">${tooltip}</div>
							<div class="input-group">
								<select name="srcDataFormat" id="srcDataFormat"
									class="form-control table-cell input-sm" data-toggle="tooltip"
									data-placement="bottom" title="${tooltip}">
									<option value="" >Select Source Data Format</option>
									
									<c:forEach var="srcDataFormat" items="${srcDataFormat}">
										<option value="${srcDataFormat}">${srcDataFormat}</option>
									</c:forEach>
								</select> 
									<span class="input-group-addon add-on last"> <i class="glyphicon glyphicon-alert" data-toggle="tooltip"
									data-placement="left" title=""></i></span>
							</div>
						</div>
						<div class="form-group">
							<spring:message code="parser.grid.child.attrib.format.label.tooltip"
								var="tooltip" ></spring:message>
								<spring:message code="parser.grid.child.attrib.format.label"
								var="label" ></spring:message>
							<div class="table-cell-label">${label}</div>
							<div class="input-group">
								<input type="text" name="childAttributes" id="childAttributes"
									class="form-control table-cell input-sm" data-toggle="tooltip"
									data-placement="bottom" title="${tooltip}"> <span
									class="input-group-addon add-on last"> <i
									class="glyphicon glyphicon-alert" data-toggle="tooltip"
									data-placement="left" title=""></i></span>
							</div>
						</div>
						
						<div class="form-group">
							<spring:message code="parser.grid.recordInitilializer.format.label.tooltip"
								var="tooltip" ></spring:message>
							<div class="table-cell-label">${tooltip}</div>
							<div class="input-group">
								<select  name="recordInitilializer" id="recordInitilializer"
									class="form-control table-cell input-sm" data-toggle="tooltip"
									data-placement="bottom" title="${tooltip}">
									<option value="true" selected="selected">True</option>
									<option value="false">False</option>
								</select> 
								<span
									class="input-group-addon add-on last"> <i
									class="glyphicon glyphicon-alert" data-toggle="tooltip"
									data-placement="left" title=""></i></span>
							</div>
						</div>
						<c:if test="${(plugInType eq 'ASN1_PARSING_PLUGIN') || plugInType eq 'RAP_PARSING_PLUGIN' || plugInType eq 'TAP_PARSING_PLUGIN' || plugInType eq 'NRTRDE_PARSING_PLUGIN'}">
							<div class="form-group">
								<spring:message code="parser.grid.unifiedFieldHoldsChoiceId.format.label.tooltip" var="tooltip" ></spring:message>
								<div class="table-cell-label">${tooltip}</div>
								<jsp:include page="../../common/autocomplete.jsp">
									<jsp:param name="unifiedField" value="unifiedFieldHoldsChoiceId" ></jsp:param>
								</jsp:include>
							</div>
						</c:if>
						<c:if test="${plugInType eq 'RAP_PARSING_PLUGIN' || plugInType eq 'TAP_PARSING_PLUGIN' || plugInType eq 'NRTRDE_PARSING_PLUGIN'}">
						
						<div class="form-group">
							<spring:message code="parser.grid.parseAsJson.format.label.tooltip"
								var="tooltip" ></spring:message>
							<div class="table-cell-label">${tooltip}</div>
							<div class="input-group">
								<select  name="parseAsJson" id="parseAsJson"
									class="form-control table-cell input-sm" data-toggle="tooltip"
									data-placement="bottom" title="${tooltip}">
									<option value="false">False</option>
									<option value="true" selected="selected">True</option>
								</select> 
								<span
									class="input-group-addon add-on last"> <i
									class="glyphicon glyphicon-alert" data-toggle="tooltip"
									data-placement="left" title=""></i></span>
							</div>
						</div>
						</c:if>
						</c:if>
						<c:if test="${(plugInType eq 'HTML_PARSING_PLUGIN')}">
							<div class="form-group">
								<spring:message code="parser.grid.field.identifier.format.label"
									var="tooltip" ></spring:message>
								<div class="table-cell-label">${tooltip}</div>
								<div class="input-group">
									<input type="text" name="fieldIdentifier" id="fieldIdentifier"
										class="form-control table-cell input-sm" data-toggle="tooltip"
										data-placement="bottom" title="${tooltip}"> <span
										class="input-group-addon add-on last"> <i
										class="glyphicon glyphicon-alert" data-toggle="tooltip"
										data-placement="left" title=""></i></span>
								</div>
							</div>
						
							<div class="form-group">
								<spring:message code="parser.grid.field.extraction.method.format.label"
									var="tooltip" ></spring:message>
								<div class="table-cell-label">${tooltip}</div>
								<div class="input-group">
									<input type="text" name="fieldExtractionMethod" id="fieldExtractionMethod"
										class="form-control table-cell input-sm" data-toggle="tooltip"
										data-placement="bottom" title="${tooltip}" > <span
										class="input-group-addon add-on last"> <i
										class="glyphicon glyphicon-alert" data-toggle="tooltip"
										data-placement="left" title=""></i></span>
								</div>
							</div>
						
							<div class="form-group">
								<spring:message code="parser.grid.field.section.id.format.label"
									var="tooltip" ></spring:message>
								<div class="table-cell-label">${tooltip}</div>
								<div class="input-group">
									<input type="text" name="fieldSectionId" id="fieldSectionId"
										class="form-control table-cell input-sm" data-toggle="tooltip"
										data-placement="bottom" title="${tooltip}"> <span
										class="input-group-addon add-on last"> <i
										class="glyphicon glyphicon-alert" data-toggle="tooltip"
										data-placement="left" title=""></i></span>
								</div>
							</div>
						
							<div class="form-group">
								<spring:message code="parser.grid.contains.field.attribute.format.label"
									var="tooltip" ></spring:message>
								<div class="table-cell-label">${tooltip}</div>
								<div class="input-group">
									<input type="text" name="containsFieldAttribute" id="containsFieldAttribute"
										class="form-control table-cell input-sm" data-toggle="tooltip"
										data-placement="bottom" title="${tooltip}"> <span
										class="input-group-addon add-on last"> <i
										class="glyphicon glyphicon-alert" data-toggle="tooltip"
										data-placement="left" title=""></i></span>
										
							   </div>
							</div>
							
							<div class="form-group">
							<spring:message code="parser.grid.value.separator.label"
									var="tooltip" ></spring:message>
							<div class="table-cell-label">${tooltip}</div>
							<div class="input-group">
							   <input type="text" name="valueSeparatorAttribute" id="valueSeparatorAttribute"
										class="form-control table-cell input-sm" data-toggle="tooltip"
										data-placement="bottom" title="${tooltip}"> <span
										class="input-group-addon add-on last"> <i
										class="glyphicon glyphicon-alert" data-toggle="tooltip"
										data-placement="left" title=""></i></span>
										
							   </div>
							</div>
							
							<div class="form-group">
							<spring:message code="parser.grid.value.index.label"
									var="tooltip" ></spring:message>
							<div class="table-cell-label">${tooltip}</div>
							<div class="input-group">
							   <input type="text" name="valueIndexAttribute" id="valueIndexAttribute"
										class="form-control table-cell input-sm" data-toggle="tooltip"
										data-placement="bottom" title="${tooltip}"> <span
										class="input-group-addon add-on last"> <i
										class="glyphicon glyphicon-alert" data-toggle="tooltip"
										data-placement="left" title=""></i></span>
										
							   </div>
							</div>
							
						</c:if>
						
						<c:if test="${(plugInType eq 'XLS_PARSING_PLUGIN') }">
							<div class="form-group">
								<spring:message code="parser.grid.field.startsWith"
									var="tooltip" ></spring:message>
								<div class="table-cell-label">${tooltip}</div>
								<div class="input-group">
									<input type="text" name="startsWith" id="startsWith"
										class="form-control table-cell input-sm" data-toggle="tooltip"
										data-placement="bottom" title="${tooltip}" > <span
										class="input-group-addon add-on last" > <i
										class="glyphicon glyphicon-alert" data-toggle="tooltip"
										data-placement="left" title=""></i></span>
								</div>
							</div>
							
							<div class="form-group">
								<spring:message code="parser.grid.field.table.footer" var="tooltip" ></spring:message>
								<div class="table-cell-label">${tooltip}</div>
								<div class="input-group">
									<select name="tableFooter" id="tableFooter" 
										class="form-control table-cell input-sm" data-toggle="tooltip"
										data-placement="bottom" title="${tooltip}">
										<c:forEach var="trueFalseEnum" items="${trueFalseEnum}" >
											<option value="${trueFalseEnum.name}">${trueFalseEnum}</option>
										</c:forEach>
									</select> 
									<span class="input-group-addon add-on last"> 
										<i class="glyphicon glyphicon-alert" data-toggle="tooltip"
										data-placement="left" title=""></i>
									</span>
								</div>
							</div>
							
							<div class="form-group">
								<spring:message code="parser.grid.field.excel.row"
									var="tooltip" ></spring:message>
								<div class="table-cell-label">${tooltip}</div>
								<div class="input-group">
									<input type="text" name="excelRow" id="excelRow"
										class="form-control table-cell input-sm" data-toggle="tooltip"
										data-placement="bottom" title="${tooltip}" onkeydown="isNumericOnKeyDown(event)" maxlength="7"> <span
										class="input-group-addon add-on last"    > <i
										class="glyphicon glyphicon-alert" data-toggle="tooltip"
										data-placement="left" title=""></i></span>
								</div>
							</div>
							
							<div class="form-group">
								<spring:message code="parser.grid.field.excel.col"
									var="tooltip" ></spring:message>
								<div class="table-cell-label">${tooltip}</div>
								<div class="input-group">
									<input type="text" name="excelCol" id="excelCol"
										class="form-control table-cell input-sm" data-toggle="tooltip"
										data-placement="bottom" title="${tooltip}" onkeydown="isNumericOnKeyDown(event)" maxlength="7"> <span
										class="input-group-addon add-on last"    > <i
										class="glyphicon glyphicon-alert" data-toggle="tooltip"
										data-placement="left" title=""></i></span>
								</div>
							</div>
							
							<div class="form-group">
								<spring:message code="parser.grid.field.relative.excel.row"
									var="tooltip" ></spring:message>
								<div class="table-cell-label">${tooltip}</div>
								<div class="input-group">
									<input type="text" name="relativeExcelRow" id="relativeExcelRow"
										class="form-control table-cell input-sm" data-toggle="tooltip"
										data-placement="bottom" title="${tooltip}" onkeydown="isNumericOnKeyDown(event)" maxlength="7"> <span
										class="input-group-addon add-on last"    > <i
										class="glyphicon glyphicon-alert" data-toggle="tooltip"
										data-placement="left" title=""></i></span>
								</div>
							</div>
							
							<div class="form-group">
								<spring:message code="parser.grid.field.column.starts.with"
									var="tooltip" ></spring:message>
								<div class="table-cell-label">${tooltip}</div>
								<div class="input-group">
									<input type="text" name="columnStartsWith" id="columnStartsWith"
										class="form-control table-cell input-sm" data-toggle="tooltip"
										data-placement="bottom" title="${tooltip}" > <span
										class="input-group-addon add-on last" > <i
										class="glyphicon glyphicon-alert" data-toggle="tooltip"
										data-placement="left" title=""></i></span>
								</div>
							</div>
							<div class="form-group">
								<spring:message code="parser.grid.field.column.contains"
									var="tooltip" ></spring:message>
								<div class="table-cell-label">${tooltip}</div>
								<div class="input-group">
									<input type="text" name="columnContains" id="columnContains"
										class="form-control table-cell input-sm" data-toggle="tooltip"
										data-placement="bottom" title="${tooltip}" > <span
										class="input-group-addon add-on last" > <i
										class="glyphicon glyphicon-alert" data-toggle="tooltip"
										data-placement="left" title=""></i></span>
								</div>
							</div>
							<div class="form-group">
								<spring:message code="parser.grid.field.table.row.attribute" var="tooltip" ></spring:message>
								<div class="table-cell-label">${tooltip}</div>
								<div class="input-group">
									<select name="tableRowAttribute" id="tableRowAttribute" 
										class="form-control table-cell input-sm" data-toggle="tooltip"
										data-placement="bottom" title="${tooltip}">
										<c:forEach var="trueFalseEnum" items="${trueFalseEnum}" >
											<option value="${trueFalseEnum.name}">${trueFalseEnum}</option>
										</c:forEach>
									</select> 
									<span class="input-group-addon add-on last"> 
										<i class="glyphicon glyphicon-alert" data-toggle="tooltip"
										data-placement="left" title=""></i>
									</span>
								</div>
							</div>
		
						</c:if>
						
					</div>
					<div class="modal-footer padding10">
				<sec:authorize access="hasAuthority('CREATE_PARSER')">
					<c:choose>
						<c:when test="${(plugInType eq 'ASN1_PARSING_PLUGIN' || plugInType eq 'RAP_PARSING_PLUGIN' || plugInType eq 'TAP_PARSING_PLUGIN' || plugInType eq 'NRTRDE_PARSING_PLUGIN')}">
							<button type="button" id="addNewAttribute" class="btn btn-grey btn-xs " style="display: none"
								onclick="createUpdateParserAttribute('<%=ControllerConstants.ADD_EDIT_ASN1_PARSER_ATTRIBUTE%>',0,'ADD');">
								<spring:message code="btn.label.add" ></spring:message>
							</button>
						</c:when>
						<c:when test="${(plugInType eq 'FIXED_LENGTH_ASCII_PARSING_PLUGIN')}">
							<button type="button" id="addNewAttribute" class="btn btn-grey btn-xs " style="display: none"
								onclick="createUpdateParserAttribute('<%=ControllerConstants.ADD_EDIT_FIXED_LENGTH_ASCII_PARSER_ATTRIBUTE%>',0,'ADD');">
								<spring:message code="btn.label.add" ></spring:message>
							</button>
						</c:when>
						<c:when test="${(plugInType eq 'FIXED_LENGTH_BINARY_PARSING_PLUGIN')}">
							<button type="button" id="addNewAttribute" class="btn btn-grey btn-xs " style="display: none"
								onclick="createUpdateParserAttribute('<%=ControllerConstants.ADD_EDIT_FIXED_LENGTH_BINARY_PARSER_ATTRIBUTE%>',0,'ADD');">
								<spring:message code="btn.label.add" ></spring:message>
							</button>
						</c:when>
						<c:when test="${(plugInType eq 'PDF_PARSING_PLUGIN')}">
							<button type="button" id="addNewAttribute" class="btn btn-grey btn-xs " style="display: none"
								onclick="createUpdateParserAttribute('<%=ControllerConstants.ADD_EDIT_PDF_PARSER_ATTRIBUTE%>',0,'ADD');">
								<spring:message code="btn.label.add" ></spring:message>
							</button>
						</c:when>
						<c:when test="${(plugInType eq 'ASCII_PARSING_PLUGIN')}">
							<button type="button" id="addNewAttribute" class="btn btn-grey btn-xs " style="display: none"
								onclick="createUpdateParserAttribute('<%=ControllerConstants.ADD_EDIT_ASCII_PARSER_ATTRIBUTE%>',0,'ADD');">
								<spring:message code="btn.label.add" ></spring:message>
							</button>
						</c:when>
						<c:when test="${(plugInType eq 'VARIABLE_LENGTH_ASCII_PARSING_PLUGIN')}">
							<button type="button" id="addNewAttribute" class="btn btn-grey btn-xs " style="display: none"
								onclick="createUpdateParserAttribute('<%=ControllerConstants.ADD_EDIT_VAR_LENGTH_ASCII_PARSER_ATTRIBUTE%>',0,'ADD');">
								<spring:message code="btn.label.add" ></spring:message>
							</button>
						</c:when>
						<c:when test="${(plugInType eq 'VARIABLE_LENGTH_BINARY_PARSING_PLUGIN')}">
							<button type="button" id="addNewAttribute" class="btn btn-grey btn-xs " style="display: none"
								onclick="createUpdateParserAttribute('<%=ControllerConstants.ADD_EDIT_VAR_LENGTH_BINARY_PARSER_ATTRIBUTE%>',0,'ADD');">
								<spring:message code="btn.label.add" ></spring:message>
							</button>
						</c:when>
						<c:when test="${(plugInType eq 'XML_PARSING_PLUGIN')}">
							<button type="button" id="addNewAttribute" class="btn btn-grey btn-xs " style="display: none"
								onclick="createUpdateParserAttribute('<%=ControllerConstants.ADD_EDIT_XML_PARSER_ATTRIBUTE%>',0,'ADD');">
								<spring:message code="btn.label.add" ></spring:message>
							</button>
						</c:when>
						<c:when test="${(plugInType eq 'HTML_PARSING_PLUGIN')}">
							<button type="button" id="addNewAttribute" class="btn btn-grey btn-xs " style="display: none"
								onclick="createUpdateParserAttribute('<%=ControllerConstants.ADD_EDIT_HTML_PARSER_ATTRIBUTE%>',0,'ADD');">
								<spring:message code="btn.label.add" ></spring:message>
							</button>
						</c:when>
						<c:when test="${(plugInType eq 'XLS_PARSING_PLUGIN')}">
							<button type="button" id="addNewAttribute" class="btn btn-grey btn-xs " style="display: none"
								onclick="createUpdateParserAttribute('<%=ControllerConstants.ADD_EDIT_XLS_PARSER_ATTRIBUTE%>',0,'ADD');">
								<spring:message code="btn.label.add" ></spring:message>
							</button>
						</c:when>
						<c:when test="${(plugInType eq 'JSON_PARSING_PLUGIN')}">
							<button type="button" id="addNewAttribute" class="btn btn-grey btn-xs " style="display: none"
								onclick="createUpdateParserAttribute('<%=ControllerConstants.ADD_EDIT_JSON_PARSER_ATTRIBUTE%>',0,'ADD');">
								<spring:message code="btn.label.add" ></spring:message>
							</button>
						</c:when>
						<c:when test="${(plugInType eq 'MTSIEMENS_BINARY_PARSING_PLUGIN')}">
							<button type="button" id="addNewAttribute" class="btn btn-grey btn-xs " style="display: none"
								onclick="createUpdateParserAttribute('<%=ControllerConstants.ADD_EDIT_MTSIEMENS_BINARY_PARSER_ATTRIBUTE%>',0,'ADD');">
								<spring:message code="btn.label.add" ></spring:message>
							</button>
						</c:when>
						<c:when test="${(plugInType eq 'NATFLOW_PARSING_PLUGIN')}">
							<button type="button" id="addNewAttribute" class="btn btn-grey btn-xs " style="display: none"
								onclick="createUpdateParserAttribute('<%=ControllerConstants.ADD_EDIT_NATFLOW_PARSER_ATTRIBUTE%>',0,'ADD');">
								<spring:message code="btn.label.add" ></spring:message>
							</button>
						</button>
						</c:when>
						<c:otherwise>
							<button type="button" id="addNewAttribute" class="btn btn-grey btn-xs " style="display: none"
								onclick="createUpdateParserAttribute('<%=ControllerConstants.ADD_EDIT_PARSER_ATTRIBUTE%>',0,'ADD');">
								<spring:message code="btn.label.add" ></spring:message>
							</button>
						</c:otherwise>
					</c:choose>	
				</sec:authorize>
				<sec:authorize access="hasAuthority('UPDATE_PARSER')">
					<c:choose>
						<c:when test="${(plugInType eq 'ASN1_PARSING_PLUGIN' || plugInType eq 'RAP_PARSING_PLUGIN' || plugInType eq 'TAP_PARSING_PLUGIN' || plugInType eq 'NRTRDE_PARSING_PLUGIN')}">
							<button type="button" id="editAttributeForParser"
							class="btn btn-grey btn-xs " style="display: none"
							onclick="createUpdateParserAttribute('<%=ControllerConstants.ADD_EDIT_ASN1_PARSER_ATTRIBUTE%>',0,'EDIT');">
							<spring:message code="btn.label.update" ></spring:message>
						</button>
						</c:when>
						<c:when test="${(plugInType eq 'FIXED_LENGTH_ASCII_PARSING_PLUGIN')}">
							<button type="button" id="editAttributeForParser"
							class="btn btn-grey btn-xs " style="display: none"
							onclick="createUpdateParserAttribute('<%=ControllerConstants.ADD_EDIT_FIXED_LENGTH_ASCII_PARSER_ATTRIBUTE%>',0,'EDIT');">
							<spring:message code="btn.label.update" ></spring:message>
						</button>
						</c:when>
						<c:when test="${(plugInType eq 'FIXED_LENGTH_BINARY_PARSING_PLUGIN')}">
							<button type="button" id="editAttributeForParser"
								class="btn btn-grey btn-xs " style="display: none"
								onclick="createUpdateParserAttribute('<%=ControllerConstants.ADD_EDIT_FIXED_LENGTH_BINARY_PARSER_ATTRIBUTE%>',0,'EDIT');">
								<spring:message code="btn.label.update" ></spring:message>
							</button>
						</c:when>
						<c:when test="${(plugInType eq 'PDF_PARSING_PLUGIN')}">
							<button type="button" id="editAttributeForParser"
							class="btn btn-grey btn-xs " style="display: none"
							onclick="createUpdateParserAttribute('<%=ControllerConstants.ADD_EDIT_PDF_PARSER_ATTRIBUTE%>',0,'EDIT');">
							<spring:message code="btn.label.update" ></spring:message>
						</button>
						</c:when>
						<c:when test="${(plugInType eq 'ASCII_PARSING_PLUGIN')}">
							<button type="button" id="editAttributeForParser"
							class="btn btn-grey btn-xs " style="display: none"
							onclick="createUpdateParserAttribute('<%=ControllerConstants.ADD_EDIT_ASCII_PARSER_ATTRIBUTE%>',0,'EDIT');">
							<spring:message code="btn.label.update" ></spring:message>
						</button>
						</c:when>
						<c:when test="${(plugInType eq 'VARIABLE_LENGTH_ASCII_PARSING_PLUGIN')}">
							<button type="button" id="editAttributeForParser"
							class="btn btn-grey btn-xs " style="display: none"
							onclick="createUpdateParserAttribute('<%=ControllerConstants.ADD_EDIT_VAR_LENGTH_ASCII_PARSER_ATTRIBUTE%>',0,'EDIT');">
							<spring:message code="btn.label.update" ></spring:message>
						</button>
						</c:when>
						<c:when test="${(plugInType eq 'VARIABLE_LENGTH_BINARY_PARSING_PLUGIN')}">
							<button type="button" id="editAttributeForParser"
							class="btn btn-grey btn-xs " style="display: none"
							onclick="createUpdateParserAttribute('<%=ControllerConstants.ADD_EDIT_VAR_LENGTH_BINARY_PARSER_ATTRIBUTE%>',0,'EDIT');">
							<spring:message code="btn.label.update" ></spring:message>
						</button>
						</c:when>
						<c:when test="${(plugInType eq 'XML_PARSING_PLUGIN')}">
							<button type="button" id="editAttributeForParser"
							class="btn btn-grey btn-xs " style="display: none"
							onclick="createUpdateParserAttribute('<%=ControllerConstants.ADD_EDIT_XML_PARSER_ATTRIBUTE%>',0,'EDIT');">
							<spring:message code="btn.label.update" ></spring:message>
						</button>
						</c:when>
						<c:when test="${(plugInType eq 'XLS_PARSING_PLUGIN')}">
							<button type="button" id="editAttributeForParser"
							class="btn btn-grey btn-xs " style="display: none"
							onclick="createUpdateParserAttribute('<%=ControllerConstants.ADD_EDIT_XLS_PARSER_ATTRIBUTE%>',0,'EDIT');">
							<spring:message code="btn.label.update" ></spring:message>
						</button>
						</c:when>
						<c:when test="${(plugInType eq 'JSON_PARSING_PLUGIN')}">
							<button type="button" id="editAttributeForParser"
							class="btn btn-grey btn-xs " style="display: none"
							onclick="createUpdateParserAttribute('<%=ControllerConstants.ADD_EDIT_JSON_PARSER_ATTRIBUTE%>',0,'EDIT');">
							<spring:message code="btn.label.update" ></spring:message>
						</button>
						</c:when>
						<c:when test="${(plugInType eq 'MTSIEMENS_BINARY_PARSING_PLUGIN')}">
							<button type="button" id="editAttributeForParser"
							class="btn btn-grey btn-xs " style="display: none"
							onclick="createUpdateParserAttribute('<%=ControllerConstants.ADD_EDIT_MTSIEMENS_BINARY_PARSER_ATTRIBUTE%>',0,'EDIT');">
							<spring:message code="btn.label.update" ></spring:message>
						</button>
						</c:when>
						<c:when test="${(plugInType eq 'NATFLOW_PARSING_PLUGIN')}">
							<button type="button" id="editAttributeForParser"
							class="btn btn-grey btn-xs " style="display: none"
							onclick="createUpdateParserAttribute('<%=ControllerConstants.ADD_EDIT_NATFLOW_PARSER_ATTRIBUTE%>',0,'EDIT');">
							<spring:message code="btn.label.update" ></spring:message>
						</button>
						</c:when>
						<c:otherwise>
							<button type="button" id="editAttributeForParser"
							class="btn btn-grey btn-xs " style="display: none"
							onclick="createUpdateParserAttribute('<%=ControllerConstants.ADD_EDIT_PARSER_ATTRIBUTE%>',0,'EDIT');">
							<spring:message code="btn.label.update" ></spring:message>
						</button>
						</c:otherwise>
					</c:choose>
				</sec:authorize>
						<button type="button" class="btn btn-grey btn-xs "
							data-dismiss="modal"
							onclick="closeFancyBox();reloadAttributeGridData();">
							<spring:message code="btn.label.close" ></spring:message>
						</button>
					</div>
				</div>
		</div>
