<%@page import="com.elitecore.sm.common.constants.BaseConstants"%>
<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<%@taglib 	uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib 	uri="http://www.springframework.org/security/tags" prefix="sec"  %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>		    
	   
	    <div class="modal-content">
		    <input type="hidden" id="importInstanceId" name="importInstanceId" />
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
						<!-- 	<tr>
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
							</tr> -->
							<tr>
								<td><strong><spring:message code="btn.label.import.and.overwrite"></spring:message>&nbsp;&nbsp;:</strong></td>
								<td>New entities get added, existing entities, if duplicate &#45; gets replaced by new values</td>
							</tr>
						</table>	
			        </p>
			        </div>
		        </div>
		        <div id="import-config-buttons" class="modal-footer padding10">
		        	<button type="button" class="btn btn-grey btn-xs " id="import-add-btn" onclick="importConfig('<%= BaseConstants.IMPORT_MODE_ADD %>');"><spring:message code="btn.label.import.and.add"></spring:message></button>
		        	<button type="button" class="btn btn-grey btn-xs " id="import-update-btn" onclick="importConfig('<%= BaseConstants.IMPORT_MODE_UPDATE %>');">Import & Update</button>
		        	<button type="button" class="btn btn-grey btn-xs " id="import-keepboth-btn" onclick="importConfig('<%= BaseConstants.IMPORT_MODE_KEEP_BOTH %>');">Import & KeepBoth</button>
		        	<button type="button" class="btn btn-grey btn-xs " id="import-overwrite-btn" onclick="importConfig('<%= BaseConstants.IMPORT_MODE_OVERWRITE %>');"><spring:message code="btn.label.import.and.overwrite"></spring:message></button>
		            <button type="button" class="btn btn-grey btn-xs " id="import-popup-close-btn" data-dismiss="modal" onclick="clearFileContent();closeFancyBox();searchInstanceCriteria();submitInitForm();"><spring:message code="btn.label.close"></spring:message></button>
		            <button type="button" class="btn btn-grey btn-xs " id="import-popup-close-btn-summary" data-dismiss="modal" style="display:none" onclick="clearFileContent();closeFancyBox();submitInitForm();"><spring:message code="btn.label.close"></spring:message></button>
		        </div>
		        <div id="import-config-progress-bar-div" class="modal-footer padding10 text-left" style="display: none;">
					<jsp:include page="../common/processing-bar.jsp"></jsp:include>
			</div>
		       
		    </div>
		    <!-- /.modal-content --> 
		    
		    <script type="text/javascript">
		    
		    function importConfigPopup(){
				clearAllMessages();
				clearResponseMsgPopUp();
				
			
				$("#importContent").show();
				$("#divValidationList").html('');
				$('#import-config-progress-bar-div').hide();
				$("#import-update-btn").hide();
				$("#import-keepboth-btn").hide();
				$("#import-add-btn").hide();
			    $("#import-overwrite-btn").show();
			    $('#import-popup-close-btn').show();
			    $("#import-popup-close-btn-summary").hide();
				
			    clearFileContent();
			    
			    var rowId,syncStatus;
			    
			    if(currentTab == 'SERVER_MANAGEMENT'){
			    	
					if(ckIntanceSelected.length == 0){
						
						$("#lessWarn").show();
						$("#moreWarn").hide();
						$("#message").click();
						return;
					}else if(ckIntanceSelected.length > 1){
						$("#lessWarn").hide();
						$("#moreWarn").show();
						$("#message").click();
						return;
					}else{
						// set instance id which is selected for import to submit with form
						var state = checkInstanceState(ckIntanceSelected[0]);
						if(state=='CHECK'){
							$("#inactivewarn").click();
							return;
						}
						$("#importInstanceId").val(ckIntanceSelected[0]);
						$('#importconfig').click();
					}
				}
			    
				if(currentTab == 'UPDATE_INSTANCE_SUMMARY'){
					
					var serverInstanceId =	$('#instance-id').val();
					
					$("#importInstanceId").val(serverInstanceId);
					
					$('#importconfig').click();
				}
			}
		    
		    function importConfig(mode){
				var file = $("#configFile").val();
				if (file == '') {
					showErrorMsgPopUp("<spring:message code='server.instance.import.no.file.select' ></spring:message>");
					//$("#importMessage").html();
					return;
				}else if((files[0].size/ 1024) > 1024 * 50){
			    	
			    	showErrorMsgPopUp("<spring:message code='failed.file.import.size.message'></spring:message>");
			    	return;
			    }else{
					$("#importMode").val(mode);
					var serverInstanceId = $("#importInstanceId").val();
					var importmode=$("#importMode").val();
					var actionUrl = '<%=ControllerConstants.IMPORT_SERVER_INSTANCE_CONFIG%>';
					uploadAndImportServerInstance(actionUrl,serverInstanceId,importmode,currentTab);
				}
			}
		    
		    $(document).on("change","#configFile",function(event) {
		 	   files=event.target.files;
		 	   $('#configFile').html(files[0].name);
		 	});

		    
		    function clearFileContent(){
		    	$(":file").filestyle('clear');
		    }	
		    </script>
