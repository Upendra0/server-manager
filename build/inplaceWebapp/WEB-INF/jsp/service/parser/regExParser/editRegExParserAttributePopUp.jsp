<%@page import="com.elitecore.sm.common.constants.BaseConstants"%>
<%@taglib 	uri="http://www.springframework.org/tags" 			prefix="spring"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="elitecore" %> 
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="/WEB-INF/ShowErrorTag.tld" prefix="elitecoreError"%>
<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<script src="${pageContext.request.contextPath}/customJS/autoSuggestionForUnifiedField.js<%= "?v=" + Math.random() %>" type="text/javascript"></script> 
 
 <div class="modal-content">
    	<input type="hidden" id="updateBlockId" name="updateBlockId"/>
    	<input type="hidden" id="updateRowId" name="updateRowId"/>
    	<input type="hidden" id="editmappingId" name="editmappingId"/>
    	<input type="hidden" id="patternId" name="patternId"/>
    	<input type="hidden" id="attributeId" name="attributeId"/>
        <div class="modal-header padding10">
            <h4 class="modal-title"><spring:message code="iplog.parsing.service.pathlist.plugin.detail.header"></spring:message></h4>
        </div>
        <div class="modal-body padding10 inline-form">
        <div id="editPopUpMsg">
        	<jsp:include page="../../../common/responseMsgPopUp.jsp" ></jsp:include>
        	</div>
        	<div class="col-md-12 no-padding">
           		<spring:message code="regex.parser.attr.grid.sequence.no" var="tooltip"></spring:message>
            	<div class="form-group">
         			<div class="table-cell-label">${tooltip}<span class="required">*</span></div>
             		<div class="input-group">
             			<input tabindex="4" id="seqNumber" class="form-control table-cell input-sm" data-toggle="tooltip" data-placement="bottom" title="${tooltip}" readonly="readonly"/>
             			<span class="input-group-addon add-on last" > <i class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="left" title=""></i></span>
             		</div>
            	</div>
            </div>
            <div class="col-md-12 no-padding">
           		<spring:message code="regex.parser.attr.grid.sample.data" var="tooltip"></spring:message>
            	<div class="form-group">
         			<div class="table-cell-label">${tooltip}<span class="required">*</span></div>
             		<div class="input-group">
             			<input tabindex="4" id="sampleData" class="form-control table-cell input-sm" data-toggle="tooltip" data-placement="bottom" title="${tooltip}" readonly="readonly"/>
             			<span class="input-group-addon add-on last" > <i class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="left" title=""></i></span>
             		</div>
            	</div>
            </div>
        	<div class="col-md-12 no-padding">
           		<spring:message code="regex.parser.attr.grid.unified.field.name" var="tooltip"></spring:message>
            	<div class="form-group">
         			<div class="table-cell-label">${tooltip}<span class="required">*</span></div>
         			<jsp:include page="../../../common/autocomplete.jsp">
						<jsp:param name="unifiedField" value="unifiedField" ></jsp:param>
					</jsp:include>
            	</div>
            </div>
        	<div class="col-md-12 no-padding">
           		<spring:message code="regex.parser.attr.grid.descripation" var="tooltip"></spring:message>
            	<div class="form-group">
         			<div class="table-cell-label">${tooltip}</div>
             		<div class="input-group">
             			<input tabindex="4" id="description" class="form-control table-cell input-sm" data-toggle="tooltip" data-placement="bottom" title="${tooltip}"/>
             			<span class="input-group-addon add-on last" > <i class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="left" title=""></i></span>
             		</div>
            	</div>
            </div>
        	<div class="col-md-12 no-padding">
           		<spring:message code="regex.parser.attr.grid.regEx" var="tooltip"></spring:message>
            	<div class="form-group">
         			<div class="table-cell-label">${tooltip}</div>
             		<div class="input-group">
             			<input tabindex="4" id="regex" class="form-control table-cell input-sm" data-toggle="tooltip" data-placement="bottom" title="${tooltip}"/>
             			<span class="input-group-addon add-on last" > <i class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="left" title=""></i></span>
             		</div>
            	</div>
            </div>
			<div class="col-md-12 no-padding">
           		<spring:message code="regex.parser.attr.grid.default.value" var="tooltip"></spring:message>
            	<div class="form-group">
         			<div class="table-cell-label">${tooltip}</div>
             		<div class="input-group">
             			<input tabindex="4" id="defaultValue" class="form-control table-cell input-sm" data-toggle="tooltip" data-placement="bottom" title="${tooltip}"/>
             			<span class="input-group-addon add-on last" > <i class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="left" title=""></i></span>
             		</div>
            	</div>
            </div>
            <div class="col-md-12 no-padding">
           		<spring:message code="regex.parser.attr.grid.trim.char" var="tooltip"></spring:message>
            	<div class="form-group">
         			<div class="table-cell-label">${tooltip}</div>
             		<div class="input-group">
             			<input tabindex="4" id="trimChars" class="form-control table-cell input-sm" data-toggle="tooltip" data-placement="bottom" title="${tooltip}"/>
             			<span class="input-group-addon add-on last" > <i class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="left" title=""></i></span>
             		</div>
            	</div>
            </div>
        </div>
        <div id="update_buttons-div" class="modal-footer padding10">
         	<button type="button" class="btn btn-grey btn-xs " data-dismiss="modal" onclick="updateAttributeDetail();"><spring:message code="btn.label.update"></spring:message></button>
         	<button type="button" class="btn btn-grey btn-xs " data-dismiss="modal" onclick="closeFancyBox();"><spring:message code="btn.label.close"></spring:message></button>
        </div>
        <div id="update_progress-bar-div" class="modal-footer padding10 text-left" style="display: none;">
			<jsp:include page="../../../common/processing-bar.jsp"></jsp:include>
		</div>	
        
    </div>
    <!-- /.modal-content --> 
    
 <script type="text/javascript">
 
 function openEditPopup(patternListCounter,rowId,trId){
	 		
		clearAllMessages();

	 	clearAllMessagesPopUp();
		resetWarningDisplay();
		
		
		
		if(rowId == null || rowId == undefined || rowId=='undefined'){
			//rowId = jQuery("#"+patternListCounter+"_grid").jqGrid('getGridParam', 'selrow');
		//	rowId = jQuery("#"+patternListCounter+"_grid").jqGrid('getGridParam', 'rowId');
			rowId=trId;
		//	alert(rowId);
		//	$("#attributeId").val(rowId);
			
		}
		
		var dataFromTheGrid = jQuery("#"+patternListCounter+"_grid").jqGrid ('getRowData', rowId);
		
		$("#updateBlockId").val(patternListCounter);
		$("#updateRowId").val(rowId);
		$("#editmappingId").val($('#'+patternListCounter+'_parserMapping_id').val());
		$("#patternId").val(dataFromTheGrid.patternId);
		$("#attributeId").val(rowId);
		$("#seqNumber").val(dataFromTheGrid.seqNumber);
		$("#sampleData").val(dataFromTheGrid.sampleData);
		$("#unifiedField").val(dataFromTheGrid.unifiedField);
		$("#description").val(dataFromTheGrid.description);
		$("#regex").val(dataFromTheGrid.regex);
		$("#defaultValue").val(dataFromTheGrid.defaultValue);
		$("#trimChars").val(dataFromTheGrid.trimChars);
		
		$('#editPopup').click();
		
	}
 
 function updateAttributeDetail(){
	 var patternId=$("#patternId").val();
	 resetWarningDisplay();
	 clearAllMessagesPopUp();
	 showProgressBar('<%= BaseConstants.UPDATE_MODE%>');
	 var patternId=$("#patternId").val();
	 var patternListCounter=$("#updateBlockId").val();
	 
	 if(patternId !=null && patternId!='' && patternId!='undefined' && patternId!= undefined){
		 
		 $.ajax({
				url: '<%=ControllerConstants.UPDATE_REGEX_PATTERN_ATTRIBUTE%>',
				cache: false,
				async: true,
				dataType: 'json',
				type: "POST",
				data:
				 {
					"id"								:   $("#attributeId").val(),
					"pattern.id" 						:	$("#patternId").val(),
					"pattern.parserMapping.id"			:   $("#editmappingId").val(),
					//"parserType.id"      				:	parserObj['parserType.id'] ,
					"seqNumber" 						:	$("#seqNumber").val() ,
					"sampleData" 						:	$("#sampleData").val(),
					"unifiedField" 						:	$("#unifiedField").val(),
					"description" 						:   $("#description").val(),
					"regex"								:	$("#regex").val(),
					"defaultValue"						:   $("#defaultValue").val(),
					"trimChars"							:   $("#trimChars").val(),
				}, 
				
				success: function(data){
					

					var response = eval(data);
					
					var responseCode = response.code; 
					var responseMsg = response.msg; 
					var responseObject = response.object;
					hideProgressBar('<%= BaseConstants.UPDATE_MODE%>');
					
					if(responseCode == "200"){
						resetWarningDisplay();
						 clearAllMessagesPopUp();
						showSuccessMsg(responseMsg);

						var parserAttribute = eval(responseObject);
		 				
						var rowData = {};
		 				rowData.id							= parseInt(parserAttribute.id);
		 				rowData.patternId					= patternId;
		 				rowData.seqNumber					= parserAttribute.seqNumber;
		 				rowData.sampleData					= parserAttribute.sampleData;
		 				rowData.unifiedField				= parserAttribute.unifiedField;
		 				rowData.description					= parserAttribute.description;
		 				rowData.regex						= parserAttribute.regex;
		 				rowData.defaultValue				= parserAttribute.defaultValue;
		 				rowData.trimChars					= parserAttribute.trimChars;
		 				rowData.edit						="<a href='#' class='link' onclick=openEditPopup('"+patternListCounter+"','"+rowData.id+"'); ><i class='fa fa-pencil-square-o' aria-hidden='true'></i></a>";
		 				rowData.patternListCounter          = patternListCounter;
		 				
		 				$("#"+patternListCounter+"_grid").jqGrid('setRowData', rowData.id, rowData);

		 				closeFancyBox();
					}else if(responseObject != undefined && responseObject != 'undefined' && responseCode == "400"){
						resetWarningDisplay();
						 clearAllMessagesPopUp();
						addErrorIconAndMsgForAjaxPopUp(responseObject); 
					}else{
						resetWarningDisplay();
						 clearAllMessagesPopUp();
						showErrorMsgPopUp(responseMsg);
					}
				},
			    error: function (xhr,st,err){
			    	hideProgressBar('<%= BaseConstants.UPDATE_MODE%>');
					handleGenericError(xhr,st,err);
				}
			});
	 }else{
		 
		 $.ajax({
				url: '<%=ControllerConstants.VALIDATE_REGEX_PATTERN_ATTRIBUTE%>',
				cache: false,
				async: true,
				dataType: 'json',
				type: "POST",
				data:
				 {
				//	"id"								:   $("#attributeId").val(),
					"sampleData" 						:	$("#sampleData").val(),
					"description" 						:   $("#description").val(),
					"regex"								:	$("#regex").val(),
					"defaultValue"						:   $("#defaultValue").val(),
					"trimChars"							:   $("#trimChars").val(),
				}, 
				
				success: function(data){
					

					var response = eval(data);
					
					var responseCode = response.code; 
					var responseMsg = response.msg; 
					var responseObject = response.object;
					hideProgressBar('<%= BaseConstants.UPDATE_MODE%>');
					
					if(responseCode == "200"){
						
						resetWarningDisplay();
						 clearAllMessagesPopUp();
						var rowData = {};
						rowData.id							= parseInt($("#attributeId").val());
						rowData.patternId					= parseInt($("#patternId").val());
						rowData.seqNumber					= $("#seqNumber").val();
						rowData.sampleData					= $("#sampleData").val();
						rowData.unifiedField				= $("#unifiedField").val();
						rowData.description					= $("#description").val();
						rowData.regex						= $("#regex").val();
						rowData.defaultValue				= $("#defaultValue").val();
						rowData.trimChars					= $("#trimChars").val();
						rowData.edit						="<a href='#' class='link' onclick=openEditPopup('"+patternListCounter+"','"+rowData.id+"','"+rowData.id+"'); ><i class='fa fa-pencil-square-o' aria-hidden='true'></i></a>";
						rowData.patternListCounter          = patternListCounter;
						
						$("#"+patternListCounter+"_grid").jqGrid('setRowData', rowData.id, rowData);

						closeFancyBox();

					}else if(responseObject != undefined && responseObject != 'undefined' && responseCode == "400"){
						
						addErrorIconAndMsgForAjaxPopUp(responseObject); 
						
					}else{
						resetWarningDisplay();
						 clearAllMessagesPopUp();
						showErrorMsgPopUp(responseMsg);
					}
				},
			    error: function (xhr,st,err){
			    	hideProgressBar('<%= BaseConstants.UPDATE_MODE %>');
					handleGenericError(xhr,st,err);
				}
			});
		 
		 
	 }
	 
	 
	 
 }

 </script> 
