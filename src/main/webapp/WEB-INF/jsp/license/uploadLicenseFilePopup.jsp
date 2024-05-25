<%@page import="com.elitecore.sm.util.MapCache"%>
<%@page import="com.elitecore.sm.common.constants.BaseConstants"%>
<%@page import="com.elitecore.sm.common.constants.FormBeanConstants"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="elitecore"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="/WEB-INF/ShowErrorTag.tld" prefix="elitecoreError"%>
<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<%@ taglib uri="http://www.springframework.org/security/tags"
	prefix="sec"%>
<%@page
	import="com.elitecore.sm.common.constants.SystemParametersConstant"%>

<div class="modal-content">
	<div class="modal-header padding10">
		<h4 class="modal-title">
			<span id="upload_label">Upload License Key</span>
		</h4>
	</div>
	<div class="modal-body padding10 inline-form">
		<div id="UploadPopUpMsg">
			<jsp:include page="../common/responseMsgPopUp.jsp" ></jsp:include>
		</div>

		<input id="uploadTableId" name="downloadTableId"
			style="display: none;" cssClass="form-control table-cell input-sm"
			tabindex="4" data-toggle="tooltip" data-placement="bottom" /> <input
			type="hidden" id="includeDataForSampleFile" name="includeData"
			value="false" />

		<div class="col-md-12 no-padding" id="divImport">
			<div class="box-body inline-form accordion-body collapse in"
				style="padding: 0px;">
				<div class="col-md-6 no-padding" style="height: 45px">
					<spring:message code="dictionary.data.mgmt.upload.file.label"
						var="tooltip" ></spring:message>
					<div class="form-group">
						<div class="table-cell-label">${tooltip}<span
								class="required">*</span>
						</div>

						<div class="input-group">
							<div class="fullwidth" id="control">
								<input type="file" class="filestyle form-control" tabindex="14"
									data-buttonText="<spring:message code="serviceMgmt.browse.file"></spring:message>"
									id="dataFile" name="dataFile">
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
		</form>
	</div>
	<div class="modal-footer padding10" id="buttonsDiv">
		<button class="btn btn-grey btn-xs" type="button"
			id="upload_lookup_data_update"
			onclick="uploadDataConfirmationPopup('update','true');">Upload</button>

		<button id='close_btn' type="button" class="btn btn-grey btn-xs "
			data-dismiss="modal"
			onclick="closeFancyBox();clearAllMessages();clearAllMessagesPopUp();">
			<spring:message code="btn.label.close" ></spring:message>
		</button>
	</div>

	<div id="activate_full_license_processbar"
		class="modal-footer padding10 text-left" style="display: none;">
		<label>Processing Request </label> <img src="img/processing-bar.gif">
	</div>
</div>
<script type="text/javascript">
var jsSpringMsg = {};
jsSpringMsg.fileNotSelectMsg= "<spring:message code='file.not.selected' ></spring:message>";
jsSpringMsg.fileUploadSucessMsg = "<spring:message code='license.apply.upload.success.msg'></spring:message>";
var files;

var uploadedSummary ="";
var datawrittenmode="";

$(document).on("change", "#dataFile", function(event) {
	files = event.target.files;
	$('#dataFile').html(files[0].name);
});

function uploadDataConfirmationPopup(mode,sync) {
	clearAllMessages();
	clearAllMessagesPopUp();
	var file = $("#dataFile").val();
	if(file === ''){
		showErrorMsgPopUp(jsSpringMsg.fileNotSelectMsg);
		return false;
	}else if(sync=='false'){
		datawrittenmode = mode ;
		uploadLicenseKey('<%=ControllerConstants.UPLOAD_LICENSE_KEY%>');
	}else{
		datawrittenmode = mode ;
		uploadLicenseKey('<%=ControllerConstants.UPLOAD_LICENSE_KEY%>');
		}
	}

	function uploadLicenseKey(urlAction) {
		clearAllMessages();
		clearAllMessagesPopUp();
		var file = $("#dataFile").val();
		var id = $("#uploadTableId").val();
		var oMyForm = new FormData();
		oMyForm.append("file", files[0]);
		oMyForm.append("id", id);

		var summaryMODE = datawrittenmode;
		datawrittenmode = "";

		$('#activate_full_license_processbar').show();
		$('#buttonsDiv').hide();

		$.ajax({
			dataType : 'json',
			url : urlAction,
			data : oMyForm,
			type : "POST",
			enctype : 'multipart/form-data',
			processData : false,
			contentType : false,
			success : function(response) {
				var responsedata = response[0];
				uploadedSummary = "File Uploaded Successfully !";

				var responseCode = response.code;
				var responseMsg = response.msg;
				if (responseCode === "200") {
					$('#activate_full_license_processbar').hide();
					$('#buttonsDiv').show();
					showSuccessMsg(jsSpringMsg.fileUploadSucessMsg);
					closeFancyBox();
					reloadGridData();
				} else {
					$('#activate_full_license_processbar').hide();
					$('#buttonsDiv').show();
					$('#activate_full_license_btn').show();
					showErrorMsgPopUp(responseMsg);
				}
			},
			error : function() {
				$('#activate_full_license_processbar').hide();
				$('#buttonsDiv').show();
				$('#activate_full_license_btn').show();
			}
		});
	}
</script>
