<%@page import="com.elitecore.sm.common.constants.BaseConstants"%>
<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<%@taglib 	uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib 	uri="http://www.springframework.org/security/tags" prefix="sec"  %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>		    
	<div class="modal-content">
			
			<input type="hidden" id="importServiceInstanceId" name="importServiceInstanceId"  value=""/>	
			<div class="modal-header padding10">
				<h4 class="modal-title">
					<spring:message code="serviceMgmt.import.config.popup.header" />  
				</h4>
			</div>
				
			<div class="modal-body padding10 inline-form">
				<jsp:include page="../common/responseMsgPopUp.jsp" />
				<div id="divValidationList"></div>
				<div id="importContent">
				<div class="form-group">
					<spring:message
						code="serviceMgmt.import.config.popup.select.file.label"
						var="tooltip" />
					<div class="table-cell-label">${tooltip}<span
							class="required">*</span>
					</div>
					<div class="input-group ">
						<input type="file" class="filestyle form-control" tabindex="14"
							data-buttonText="<spring:message code="serviceMgmt.browse.file"/>"
							id="configFile" name="configFile" accept='text/xml'>
					</div>
				</div>
				<%-- <p>
					<i class="icon-lightbulb icon-large"></i><span class="note">
						<spring:message code="label.important.note" />
					</span><br />
				</p>
				<p>
					<spring:message code="serviceMgmt.import.config.popup.note.content" />
				</p> --%>
				<p>
			        <i class="icon-lightbulb icon-large"></i><span class="note"><spring:message code="label.important.note"/></span><br/>
		        </p>
				<p>
					<table>
					<!-- <tr id="importServiceInstanceAddMessage">
							<td width="25%"><strong><spring:message code="btn.label.import.and.add"/>&nbsp;&nbsp;:</strong></td>
							<td>New entities get added, existing entities remain as it is</td>
						</tr>
						<tr id="importServiceInstanceUpdateMessage">
							<td><strong><spring:message code="btn.label.import.and.update"/>&nbsp;&nbsp;:</strong></td>
							<td>New entities get added, existing entities, if duplicate &#45; gets updated</td>
						</tr>
						<tr id="importServiceInstanceKeepbothMessage">
							<td><strong><spring:message code="btn.label.import.and.keepboth"/>&nbsp;&nbsp;:</strong></td>
							<td>New entities get added, all existing entities remain as it is, even if duplicate</td>
						</tr> -->	
						<tr id="importServiceInstanceOverwriteMessage">
							<td><strong><spring:message code="btn.label.import.and.overwrite"/>&nbsp;&nbsp;:</strong></td>
							<td>New entities get added, existing entities, if duplicate &#45; gets replaced by new values</td>
						</tr>
					</table>	
		        </p>
				
				<div id="syncWarningMsgImport" style="color: #FF0000 ; display: none;">
			        <p>	
						<spring:message code="serviceMgmt.import.popup.note.sync.warning.content"/>           	
			        </p>
			    </div>
			
			</div>
			</div>
			<div id="import-config-buttons" class="modal-footer padding10">
				<button type="button" onclick="importServiceInstance(<%=BaseConstants.IMPORT_MODE_ADD%>)" id="importServiceInstanceAdd" class="btn btn-grey btn-xs ">
					Import & Add
				</button>
				<button type="button" onclick="importServiceInstance(<%=BaseConstants.IMPORT_MODE_UPDATE%>)" id="importServiceInstanceUpdate" class="btn btn-grey btn-xs ">
					Import & Update
				</button>
				<button type="button" onclick="importServiceInstance(<%=BaseConstants.IMPORT_MODE_KEEP_BOTH%>)" id="importServiceInstanceKeepboth" class="btn btn-grey btn-xs ">
					Import & KeepBoth
				</button>
				<button type="button" onclick="importServiceInstance(<%=BaseConstants.IMPORT_MODE_OVERWRITE%>)" id="importServiceInstanceOverwrite" class="btn btn-grey btn-xs ">
					Import & Overwrite
				</button>
				<button type="button" id="import-popup-close-btn" class="btn btn-grey btn-xs "
					data-dismiss="modal" onclick="closeFancyBox();clearFileContent();">
					<spring:message code="btn.label.close"/>
				</button>
				
				<div id="import-config-progress-bar-div" class="modal-footer padding10 text-left" style="display: none;border-top: 0px;float: right;">
					<img src="img/processing-bar.gif">
				</div>
			</div>
	</div>

	<!-- /.modal-content --> 
<script type="text/javascript">

function importConfigPopup(){
   	 $('#importServiceInstanceAdd').hide();
     $('#importServiceInstanceUpdate').hide();
     $('#importServiceInstanceKeepboth').hide();
     $('#importServiceInstanceOverwrite').show();
	 clearAllMessages();
	 clearResponseMsgPopUp();
	 clearFileContent();
	 $("#importContent").show();
	 $("#divValidationList").html('');
	 $('#import-config-progress-bar-div').hide();
	 $('#importServiceInstance').show();
	 $('#import-popup-close-btn').show();
	 $("#syncWarningMsgImport").hide();
	 var rowId,syncStatus;
	if(currentTab == 'SERVICE_MANAGEMENT'){
		if(ckIntanceSelected.length == 0){
			$("#lessWarn").show();
			$("#moreWarn").hide();
			$("#deleteWarnMsg").click();
			return;
		}else if(ckIntanceSelected.length > 1){
			$("#lessWarn").hide();
			$("#moreWarn").show();
			$("#deleteWarnMsg").click();
			return;
		}else{
			// set instance id which is selected for import to submit with form
			$("#importServiceInstanceId").val(parent.ckIntanceSelected[0]);
			rowId=parent.ckIntanceSelected[0];
			syncStatus=$(jQuery('#serviceInstanceList').jqGrid ('getCell', rowId, 'sync_status')).closest("img").attr("alt");
			
			if(syncStatus == 'false'){
				$("#syncWarningMsgImport").show();
			}
			
			$('#importconfig').click();
		}
	} else {
		var serviceId ='${serviceId}';
		var syncStatus='${syncStatus}';
		
		$("#importServiceInstanceId").val(serviceId);
		
		if(syncStatus == 'false')
			$("#syncWarningMsgImport").show();
		if(currentTab == "AGGREGATION_SERVICE_SUMMARY"){
			$("#importServiceInstanceKeepboth").hide();
/* 			$("#importServiceInstanceKeepboth").prop('disabled','true');
			$("#importServiceInstanceKeepboth").prop('onclick','');
			$("#importServiceInstanceKeepbothMessage").find("td:eq(1)").append(". This is not applicable for Aggregation service");
			$("#importServiceInstanceKeepbothMessage").css('vertical-align','top'); */
		}
		$('#importconfig').click();
	}
}

	function importServiceInstance(importMode) {
		var file = $("#configFile").val();
		
		if (file == '') {
			showErrorMsgPopUp("<spring:message code='serviceMgmt.import.no.file.select' />");
			//$("#importMessage").html();
			return;
		}else if((files[0].size/ 1024) > 1024 * 50){
	    	showErrorMsgPopUp("<spring:message code='failed.file.import.size.message' />");
	    	return;
	    }else{
			var serviceId = $("#importServiceInstanceId").val();
			var serverId = $("#server-instanceId").val();
			if(serverId == null || serverId == ''){
				serverId = '0';
			}
			var actionUrl = '<%=ControllerConstants.IMPORT_SERVICE_INSTANCE_CONFIG%>';
			uploadAndImportServiceInstance(actionUrl,serviceId,serverId,importMode);
			$("#syncWarningMsgImport").hide();
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