<%@page import="com.elitecore.sm.common.constants.BaseConstants"%>
<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<%@taglib 	uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib 	uri="http://www.springframework.org/security/tags" prefix="sec"  %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>		   

<script>
 		var jsLocaleMsg = {};
 		jsLocaleMsg.entityName = "<spring:message code='server.instance.import.header.label.entity.name'></spring:message>";
 		jsLocaleMsg.propertyName = "<spring:message code='server.instance.import.header.label.property.name'></spring:message>";
 		jsLocaleMsg.propertyValue = "<spring:message code='server.instance.import.header.label.property.value'></spring:message>";
 		jsLocaleMsg.errorMessage = "<spring:message code='server.instance.import.header.label.error.message'></spring:message>";
 		jsLocaleMsg.LineNo = "<spring:message code='server.instance.import.header.label.line.number'></spring:message>";
 		jsLocaleMsg.moduleName="<spring:message code='server.instance.import.header.label.module.name'></spring:message>";
 	</script> 
	   
	    <div class="modal-content">
		    <input type="hidden" id="importPolicyId" name="importPolicyId" />
		    <input type="hidden" id="importMode" name="importMode" />
		   
		        <div class="modal-header padding10">
		            <h4 class="modal-title"><spring:message code="serverMgmt.import.config.popup.header"></spring:message></h4>
		        </div>
		        <div class="modal-body padding10 inline-form">
		        	
		        	<jsp:include page="../common/responseMsgPopUp.jsp" ></jsp:include>
		        	<div id="divValidationList"></div>
		        	<div id="importContent">
		        	<div class="form-group" >
		        	
		            	<spring:message code="serverMgmt.import.config.popup.select.file.label" var="tooltip"></spring:message>
		         		<div class="table-cell-label">${tooltip}<span class="required">*</span></div>
		             	<div class="input-group ">
		             		<input type="file" class="form-control" tabindex="14" data-buttonText="<spring:message code="serviceMgmt.browse.file"></spring:message>" id="configFile" name="configFile" accept='text/xml'>
		             	</div>
		         	</div>
			        <p>
			        	<i class="icon-lightbulb icon-large"></i><span class="note"><spring:message code="label.important.note"></spring:message></span><br/>
			        </p>
					<p>
						<table>
							<!-- <tr>
								<td width="25%"><strong><spring:message code="btn.label.import.and.add"></spring:message>&nbsp;&nbsp;:</strong></td>
								<td>New entities get added, existing entities remain as it is</td>
							</tr>
							<tr>
								<td><strong><spring:message code="btn.label.import.and.update"></spring:message>&nbsp;&nbsp;:</strong></td>
								<td>New entities get added, existing entities, if duplicate &#45; gets updated</td>
							</tr>
							<tr>
								<td><strong><spring:message code="btn.label.import.and.keepboth"></spring:message>&nbsp;&nbsp;:</strong></td>
								<td>New entities get added, all existing entities remain as it is, even if duplicate</td>
							</tr>-->
							<tr>
								<td><strong><spring:message code="btn.label.import.and.overwrite"></spring:message>&nbsp;&nbsp;:</strong></td>
								<td>New entities get added, existing entities, if duplicate &#45; gets replaced by new values</td>
							</tr>
						</table>	
			        </p>
			        </div>
		        </div>
		        <div id="import-config-buttons" class="modal-footer padding10">
		        	<button type="button" class="btn btn-grey btn-xs " id="import-add-btn"  onclick="importConfig('<%= BaseConstants.IMPORT_MODE_ADD %>');"><spring:message code="btn.label.import.and.add"></spring:message></button>
		        	<button type="button" class="btn btn-grey btn-xs " id="import-update-btn"  onclick="importConfig('<%= BaseConstants.IMPORT_MODE_UPDATE %>');">Import & Update</button>
		        	<button type="button" class="btn btn-grey btn-xs " id="import-keepboth-btn"  onclick="importConfig('<%= BaseConstants.IMPORT_MODE_KEEP_BOTH %>');">Import & KeepBoth</button>
		        	<button type="button" class="btn btn-grey btn-xs " id="import-overwrite-btn" onclick="importConfig('<%= BaseConstants.IMPORT_MODE_OVERWRITE %>');"><spring:message code="btn.label.import.and.overwrite"></spring:message></button>
		            <button type="button" class="btn btn-grey btn-xs " id="import-popup-close-btn" data-dismiss="modal" onclick="clearFileContent();closeFancyBox();reloadPolicyGridData();"><spring:message code="btn.label.close"></spring:message></button>
		        </div>
		        <div id="import-config-progress-bar-div" class="modal-footer padding10 text-left" style="display: none;">
					<jsp:include page="../common/processing-bar.jsp"></jsp:include>
			</div>
		       
		    </div>
		    <!-- /.modal-content --> 
		    
		    <script type="text/javascript">
		    
		    function importConfig(mode){
				var file = $("#configFile").val();
				if (file == '') {
					showErrorMsgPopUp("<spring:message code='policy.import.no.file.select' ></spring:message>");
					//$("#importMessage").html();
					return;
				}else if((files[0].size/ 1024) > 1024 * 50){
			    	
			    	showErrorMsgPopUp("<spring:message code='failed.file.import.size.message'></spring:message>");
			    	return;
			    }else{
					$("#importMode").val(parseInt(mode));
					var serverInstanceId = $("#server-instance-id").val();
					var importmode=$("#importMode").val();
					var actionUrl = '<%=ControllerConstants.IMPORT_POLICY_CONFIG%>';
					var selectedPolicyId = $("input[name='policyRadio']:checked").val();
					uploadAndImportPolicy(actionUrl, serverInstanceId, selectedPolicyId, parseInt(mode));
				}
			}
		    
		    $(document).on("change","#configFile",function(event) {
		 	   files=event.target.files;
		 	   $('#configFile').html(files[0].name);
		 	});

		    
		    function clearFileContent(){
		    	$(":file").filestyle('clear');
		    }	
		    
		    function uploadAndImportPolicy(actionUrl, serverInstanceId, policyId, importMode) {
		    	if(policyId==null||policyId==undefined ||policyId=="undefined"){
		    		policyId=0;
		    	}
		    	var oMyForm = new FormData();
		    	oMyForm.append("configFile", files[0]);
		    	oMyForm.append("importInstanceId", serverInstanceId);
		    	oMyForm.append("importPolicyId", policyId);
		    	oMyForm.append("importMode", importMode);
		    	// check for file size length
		    	
		    	clearResponseMsgPopUp();
		    	
		    	updateButtonVisibility(false);

		    	$
		    	.ajax({
		    		dataType : 'json',
		    		url : actionUrl,
		    		data : oMyForm,
		    		type : "POST",
		    		enctype : 'multipart/form-data',
		    		processData : false,
		    		contentType : false,
		    		success : function(response) {
		    			
		    			var responseCode = response.code;
		    			var responseMsg = response.msg;
		    			var responseObject = response.object;
		    			
		    			if (responseCode == "200") {
		    				$('#import-popup-close-btn').show();
		    				$('#import-config-progress-bar-div').hide();
		    				
		    				closeFancyBox();
		    				showSuccessMsg(responseMsg);
		    				parent.resizeFancyBox();
		    				clearFileContent();
		    				reloadPolicyGridData();
		    			} else if (responseObject != undefined
		    					&& responseObject != 'undefined') {
		    				
		    				$("#importContent").hide();
		    				$("#divValidationList").html('');
		    				var tableString = '<table class="table table-hover">';
		    				var jsonObjectLength = 0;
		    				if(responseObject && Array.isArray(responseObject)) {
		    					jsonObjectLength = Object.keys(responseObject[0]).length;
		    				}
		    				
		    				if(jsonObjectLength == 0 && responseObject) {
		    					showErrorMsgPopUp(responseMsg + " : "+responseObject);
		    				} else {
		    					showErrorMsgPopUp(responseMsg);
		    				}
		    				
		    				
		    				if (jsonObjectLength == 2) {
		    					// Length is 2 , when XSD validation fail
		    					tableString += "<tr>";
		    					tableString += "<th>" + jsLocaleMsg.LineNo
		    					+ "</th>";
		    					tableString += "<th>" + jsLocaleMsg.errorMessage
		    					+ "</th>";
		    					tableString += "</tr>";
		    				} else if (jsonObjectLength == 5) {
		    					// Length is 5 , when Entity validation fail
		    					
		    					tableString += "<tr>";
		    					tableString += "<th>" + jsLocaleMsg.moduleName
		    					+ "</th>";
		    					tableString += "<th>" + jsLocaleMsg.entityName
		    					+ "</th>";
		    					tableString += "<th>" + jsLocaleMsg.propertyName
		    					+ "</th>";
		    					tableString += "<th>" + jsLocaleMsg.propertyValue
		    					+ "</th>";
		    					tableString += "<th>" + jsLocaleMsg.errorMessage
		    					+ "</th>";
		    					tableString += "</tr>";
		    					
		    				}
		    				
		    				if(Array.isArray(responseObject)) {
		    					$.each(responseObject, function(index, responseObject) {
			    					
			    					if (jsonObjectLength == 2) {
			    						
			    						tableString += "<tr>";
			    						tableString += "<td>" + responseObject[0]
			    						+ "</td>";
			    						tableString += "<td>" + responseObject[1]
			    						+ "</td>";
			    						tableString += "</tr>";
			    						
			    					} else if (jsonObjectLength == 5) {
			    						
			    						tableString += "<tr>";
			    						tableString += "<td>" + responseObject[0]
			    						+ "</td>";
			    						tableString += "<td>" + responseObject[1]
			    						+ "</td>";
			    						tableString += "<td>" + responseObject[2]
			    						+ "</td>";
			    						tableString += "<td>" + responseObject[3]
			    						+ "</td>";
			    						tableString += "<td>" + responseObject[4]
			    						+ "</td>";
			    						tableString += "</tr>";
			    					}
			    					
			    				});
		    				}
		    				
		    				tableString += "</table>";
		    					
		    				if(jsonObjectLength === 0) {
		    					// In case when no need to display table, only one validation message is there
		    					$("#importContent").show();
		    					$("#import-add-btn").hide();
		    					$("#import-overwrite-btn").show();
		    					$('#import-popup-close-btn').show();
		    					$('#import-config-progress-bar-div').hide();
		    					if(responseObject) {
			    					showErrorMsgPopUp(responseMsg + " : "+responseObject);
			    				} else {
			    					showErrorMsgPopUp(responseMsg);
			    				}
		    					parent.resizeFancyBox();
		    					clearFileContent();
		    				} else {
		    					$("#divValidationList").html(tableString);
		    					$("#import-add-btn").hide();
		    					$("#import-overwrite-btn").show();
		    					$('#import-popup-close-btn').show();
		    					$('#import-config-progress-bar-div').hide();
		    					resetWarningDisplay();
		    					parent.resizeFancyBox();
		    					clearFileContent();
		    				}
		    				
		    			} else {
		    				updateButtonVisibility(true);
		    				showErrorMsgPopUp(responseMsg);
		    				parent.resizeFancyBox();
		    				clearFileContent();
		    			}
		    		},
		    		error : function(response) {
		    			updateButtonVisibility(true);
		    		}
		    	});
		    }
		    
		    function updateButtonVisibility(isDisplay) {
		    	if(isDisplay) {
		    		$("#import-add-btn").hide();
	    			$("#import-overwrite-btn").show();
	    			$("#import-update-btn").hide();
    				$("#import-keepboth-btn").hide();
	    			$('#import-popup-close-btn').show();
	    			$('#import-config-progress-bar-div').hide();
		    	} else {
		    		$("#import-add-btn").hide();
			    	$("#import-overwrite-btn").hide();
			    	$("#import-update-btn").hide();
			    	$("#import-keepboth-btn").hide();
			    	$('#import-popup-close-btn').hide();
			    	$("#import-config-progress-bar-div").show();
		    	}
		    }
		    </script>
