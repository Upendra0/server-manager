<%@page import="com.elitecore.sm.common.constants.FormBeanConstants"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="com.elitecore.sm.util.MapCache"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@page
	import="com.elitecore.sm.common.constants.SystemParametersConstant"%>
<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<%@page import="com.elitecore.sm.common.constants.EngineConstants"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/security/tags"
	prefix="sec"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="/WEB-INF/ShowErrorTag.tld" prefix="elitecoreError"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<script src="${pageContext.request.contextPath}/customJS/parserManager.js<%= "?v=" + Math.random() %>" type="text/javascript"></script>
<!-- MED-8969 START : Attribute upload screen -->
 		  <div class="modal-content">
					<div class="modal-header padding10">
						<h4 class="modal-title">
							<span id="generate_label"><spring:message code="parsing.parser.attr.generate" ></spring:message></span>
						</h4>
					</div>
					<div class="modal-body padding10 inline-form">
						<jsp:include page="../../../common/responseMsgPopUp.jsp"></jsp:include>
					<div class="col-md-12 no-padding" id="divGenerate">
							<div class="box-body inline-form accordion-body collapse in" style="padding: 0px;">
								<div class="col-md-12 no-padding">
									<spring:message code="json.plugin.generate.attributes" var="tooltip" ></spring:message>
									<div class="table-cell-label" style="text-align: left;">${tooltip}<span class="required">*</span></div>
								</div>
								<div class="col-md-12 no-padding" style="height: 200px">
									<div class="form-group">
					                   <textarea id="jsonTextArea" style="width: 100%; max-width: 100%; height:200px; max-height:200px"></textarea>
				                    </div> 	
							  	</div>
							</div>
						</div>
						
				</div>
				<div class="modal-footer padding10" id="generateAttrButtonsDiv">
				<button class="btn btn-grey btn-xs" type="button" id ="generate_parser_attr" 
						onclick="generateParserAttrPopup();">
						<spring:message code="btn.label.generate" ></spring:message>
					</button>
					<button id='close_btn' type="button" class="btn btn-grey btn-xs "
							onclick="closeGenerateAttrPopUp();">
							<spring:message code="btn.label.close" ></spring:message>
					</button>
				</div>
				<div id="processbarDiv" class="modal-footer padding10 text-left" style="display: none;">
						<label>Processing Request </label> <img src="img/processing-bar.gif">
				</div>
	</div>
 
<script type="text/javascript">
jsSpringMsg.invalidJsonString = "<spring:message code='generate.json.parser.attributes..invalid.json.string'></spring:message>";
function displayGenerateAttributesPopup(){
	clearAllMessages();
	clearAllMessagesPopUp();
	$("#generate_attributes").click();
}
function closeGenerateAttrPopUp() {
	$("#jsonTextArea").val("");
	closeFancyBox();
	clearAllMessages();
	clearAllMessagesPopUp();
}
function generateParserAttrPopup(){
	var jsonString = $("#jsonTextArea").val();
	if(jsonString == '') {
		showErrorMsgPopUp(jsSpringMsg.invalidJsonString);
		return false;
	}
	var parserMappingId='${mappingId}';
	var parserType='${plugInType}';
	var reqAction = '${REQUEST_ACTION_TYPE}';
	
	var oMyForm = new FormData();
    oMyForm.append("jsonString", jsonString);
    oMyForm.append("parserMappingId", parserMappingId);
    oMyForm.append("parserType", parserType);
    oMyForm.append("actionType", reqAction);
   	$('#processbarDiv').show();
   	$('#generateAttrButtonsDiv').hide();
   
	$.ajax({dataType : 'json',
        url : '<%=ControllerConstants.GENERATE_JSON_PARSER_ATTR_DATA%>',
        data : oMyForm ,
        type : "POST",
        processData: false,
        contentType:false,
        success : function(response) {
    	  	var responsedata = response[0];
		 	var responseCode = responsedata.code;
			var responseMsg = responsedata.msg;
			var objectData=responsedata.object;
			if(responseCode === "200"){
	 			$('#processbarDiv').hide();
	 		    $('#generateAttrButtonsDiv').show();
	 		    $("#jsonTextArea").val("");
				closeFancyBox();
				closeFancyBoxFromChildIFrame();
				reloadAttributeGridData();
				showSuccessMsg(responseMsg);
			}else{
			 	$('#processbarDiv').hide();
			 	$('#generateAttrButtonsDiv').show();
			 	showErrorMsgPopUp(responseMsg);
			}
        },
        error : function(){
      	  $('#processbarDiv').hide();
      	  $('#generateAttrButtonsDiv').show();
        }
    });	
}
</script>
