<%@page import="com.elitecore.sm.util.MapCache"%>
<%@page import="com.elitecore.sm.common.constants.SystemParametersConstant"%>
<%@page import="com.elitecore.sm.common.constants.BaseConstants"%>
<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="/WEB-INF/ShowErrorTag.tld" prefix="elitecoreError"%>
<html>
<head>
<style type="text/css">

.table-roaming-bordered {
    border: 1px solid #D3D3D3;
}
</style>
<script type="text/javascript">
$("#partnerNameId").val(document.getElementById("partner").value);
$("#lobId").val(document.getElementById("lob").value);
$("#primaryTadigId").val(document.getElementById("primaryTadig").value);
$("#secondaryTadigId").val(document.getElementById("secondaryTadig").value);

function saveOrUpdateFileManagement(){
	
	var testMaxRecords = $("#testMaxRecordsInTapOut_id").val();
	var testMaxFileSize = $("#testMaxFileSizeOfTapOut_id").val();
	
	var testMaxRecordsNrtrde = $("#testMaxRecordsInNrtrdeOut_id").val();
	var testMaxFileSizeNrtrde = $("#testMaxfileSizeOfnrtrdeOut_id").val();
	
	var commercialMaxRecords = $("#commercialMaxRecordsInTapOut_id").val();
	var commercialMaxFileSize = $("#commercialMaxFileSizeOfTapOut_id").val();
	
	var commercialMaxRecordsNrtrde = $("#commercialMaxRecordsInNrtrdeOut_id").val();
	var commercialMaxFileSizeNrtrde = $("#commercialMaxfileSizeOfnrtrdeOut_id").val();
	$("#partnerNameId").val(document.getElementById("partner").value);
	$("#lobId").val(document.getElementById("lob").value);
	$("#primaryTadigId").val(document.getElementById("primaryTadig").value);
	$("#secondaryTadigId").val(document.getElementById("secondaryTadig").value);
	
	
	
	
	if(testMaxRecords == '' || testMaxRecords == null){
		$("#testMaxRecordsInTapOut_id").val(0);
	}
	if(testMaxFileSize == '' || testMaxFileSize == null){
		$("#testMaxFileSizeOfTapOut_id").val(0);
	}
	if(testMaxRecordsNrtrde == '' || testMaxRecordsNrtrde == null){
		$("#testMaxRecordsInNrtrdeOut_id").val(0);
	}
	if(testMaxFileSizeNrtrde == '' || testMaxFileSizeNrtrde == null){
		$("#testMaxfileSizeOfnrtrdeOut_id").val(0);
	}
	if(commercialMaxRecords == '' || commercialMaxRecords == null){
		$("#commercialMaxRecordsInTapOut_id").val(0);
	}if(commercialMaxFileSize == '' || commercialMaxFileSize == null){
		$("#commercialMaxFileSizeOfTapOut_id").val(0);
	}
	if(commercialMaxRecordsNrtrde == '' || commercialMaxRecordsNrtrde == null){
		$("#commercialMaxRecordsInNrtrdeOut_id").val(0);
	}
	if(commercialMaxFileSizeNrtrde == '' || commercialMaxFileSizeNrtrde == null){
		$("#commercialMaxfileSizeOfnrtrdeOut_id").val(0);
	}
	
	$("#save-file-management-form").attr("action","<%=ControllerConstants.MODIFY_FILE_MANAGEMENT%>");
	$("#save-file-management-form").submit();	
}

function resetFileManagementForm(){
	$("#testMaxRecordsInTapOut_id").val('');
	$("#testMaxFileSizeOfTapOut_id").val('');
	$("#testMaxRecordsInNrtrdeOut_id").val('');
	$("#testMaxfileSizeOfnrtrdeOut_id").val('');
	$("#commercialMaxRecordsInTapOut_id").val('');
	$("#commercialMaxFileSizeOfTapOut_id").val('');
	$("#commercialMaxRecordsInNrtrdeOut_id").val('');
	$("#commercialMaxfileSizeOfnrtrdeOut_id").val('');	
}


</script>





</head>
<body class="skin-blue sidebar-mini sidebar-collapse">
	<div class="fullwidth inline-form"
		style="padding-left: 0px !important;">
		<div class="box-body padding0 mtop10 clearfix" id="host_Confg_Block">
			<div class="fullwidth mtop10">
				<form:form modelAttribute="file_management_form_bean" method="POST"
					action="addService" id="save-file-management-form">
					<div class="box box-warning">
						<div class="box-header with-border table-roaming-bordered"
							style="padding-left: 5px !important;">
							<h3 class="box-title">
								<spring:message
									code="roaming.Configuration.file.management.heading" ></spring:message>
							</h3>
							<div class="box-tools pull-right" style="top: -3px;">
								<button class="btn btn-box-tool" data-widget="collapse">
									<i class="fa fa-minus"></i>
								</button>
							</div>
							<!-- /.box-tools -->
						</div>
						<!-- /.box-header -->
						<div class="box-body"style="padding-left: 0px !important;">
						<div class="fullwidtd inline-form">
							<table class = "table  table-bordered">
								<tr>
									<td class="col-md-4" valign="top">
										<table>
											<tr>
												<td><div class="title2">
			<spring:message code="fileManagement.tap.configuration.heading" ></spring:message>
</div></td>
											</tr>
											<tr>
												<td>
													<div class="form-group">
														<spring:message code="filemanagement.tap.in.version.lable"
															var="tapInVersion" ></spring:message>
														<spring:message
															code="filemanagement.tap.in.version.tooltip"
															var="tooltip" ></spring:message>
														<div class="table-cell-label">${tapInVersion}<span
																class="required">*</span>
														</div>
														<div class="input-group ">
															<form:select path="testTapInVersion" id="testTapInVersion_id"
																cssClass="form-control table-cell input-sm"
																data-toggle="tooltip" data-placement="bottom"
																title="${tooltip }">
																<form:option value = "3.12"></form:option>
																<form:option value="1.11">1.11</form:option>
																<form:option value="1.12">1.12</form:option>
																<form:option value="2.11">2.11</form:option>
																<form:option value="2.12">2.12</form:option>
																<form:option value="3.11">3.11</form:option>
																<form:option value="3.12">3.12</form:option>
																
																
															</form:select>
															<spring:bind path="testTapInVersion">
																<elitecoreError:showError
																	errorId="testTapInVersion_err"
																	errorMessage="${status.errorMessage}"></elitecoreError:showError>
															</spring:bind>
														</div>
													</div>
												</td>
											</tr>
											<tr>
												<td>

													<div class="form-group">
														<spring:message
															code="filemanagement.tap.out.version.lable"
															var="tapOutVersion" ></spring:message>
														<spring:message
															code="filemanagement.tap.out.version.tooltip"
															var="tooltip" ></spring:message>
														<div class="table-cell-label">${tapOutVersion}<span
																class="required">*</span>
														</div>
														<div class="input-group ">
															<form:select path="testTapOutVersion" id="testTapOutVersion_id"
																cssClass="form-control table-cell input-sm"
																data-toggle="tooltip" data-placement="bottom"
																title="${tooltip }">
																	<form:option value = "3.12"></form:option>
																<form:option value="1.11">1.11</form:option>
																<form:option value="1.12">1.12</form:option>
																<form:option value="2.11">2.11</form:option>
																<form:option value="2.12">2.12</form:option>
																<form:option value="3.11">3.11</form:option>
																<form:option value="3.12">3.12</form:option>
															</form:select>
															<spring:bind path="testTapOutVersion">
																<elitecoreError:showError errorId="testTapOutVersion_err"
																	errorMessage="${status.errorMessage}"></elitecoreError:showError>
															</spring:bind>
														</div>
													</div>
												</td>
											</tr>
											<tr>
												<td>
													<div class="form-group">
														<spring:message
															code="filemanagement.max.records.in.tap.out.lable"
															var="maxRecordsInTapOut" ></spring:message>
														<spring:message
															code="filemanagement.max.records.in.tap.out.tooltip"
															var="tooltip" ></spring:message>
														<div class="table-cell-label">${maxRecordsInTapOut}<span
																class="required">*</span></div>
														<div class="input-group">
															<form:input path="testMaxRecordsInTapOut"
																id="testMaxRecordsInTapOut_id" onkeydown="isNumericOnKeyDown(event)"
																cssClass="form-control table-cell input-sm"
																maxlength="10" data-toggle="tooltip"
																data-placement="bottom" title="${tooltip }" ></form:input>
															<spring:bind path="testMaxRecordsInTapOut">
																<elitecoreError:showError
																	errorId="testMaxRecordsInTapOut_err"
																	errorMessage="${status.errorMessage}"></elitecoreError:showError>
															</spring:bind>
														</div>
													</div>
												</td>
											</tr>
											<tr>
												<td>
													<div class="form-group">
														<spring:message
															code="filemanagement.max.filesize.of.tap.out.lable"
															var="maxFileSizeOfTapOut" ></spring:message>
														<spring:message
															code="filemanagement.max.filesize.of.tap.out.tooltip"
															var="tooltip" ></spring:message>
														<div class="table-cell-label">${maxFileSizeOfTapOut}<span
																class="required">*</span></div>
														<div class="input-group max-file-width">
															<form:input path="testMaxFileSizeOfTapOut" onkeydown="isNumericOnKeyDown(event)"
																id="testMaxFileSizeOfTapOut_id"
																cssClass="form-control table-cell input-sm"
																maxlength="10" data-toggle="tooltip"
																data-placement="bottom" title="${tooltip }" cssStyle="width :89%;"></form:input>
																<span>MB</span>
															
															<spring:bind path="testMaxFileSizeOfTapOut">
																<elitecoreError:showError
																	errorId="testMaxFileSizeOfTapOut_err"
																	errorMessage="${status.errorMessage}"></elitecoreError:showError>
															</spring:bind>
														</div>
													</div>
												</td>
											</tr>
											<tr>
												<td>
													<div class="form-group">
														<spring:message
															code="filemanagement.send.tap.notification.file.lable"
															var="sendTapNotification" ></spring:message>
														<spring:message
															code="filemanagement.send.tap.notification.file.tooltip"
															var="tooltip" ></spring:message>
														<div class="table-cell-lable col-md-5" style="padding-top: 5px;padding-left: 0px">${sendTapNotification}</div>
														 <div class="input-group" style = "padding-left: 10px">
															 <form:radiobutton path="testSendTapNotification" value="Yes" label="Yes" id = "testSendTapNotification_id" ></form:radiobutton>
            												 <form:radiobutton path="testSendTapNotification" value="No" label="No" id = "testSendTapNotification_id"></form:radiobutton>
															<spring:bind path="testSendTapNotification">
																<elitecoreError:showError
																	errorId="testSendTapNotification_err"
																	errorMessage="${status.errorMessage}"></elitecoreError:showError>
															</spring:bind>
														 </div>
													</div>
												</td>
											</tr>
											<tr>
												<td>
													<div class="form-group">
														<spring:message
															code="filemanagement.regenerated.tap.out.filesequence.lable"
															var="regeneratedTapOutFileSequence" ></spring:message>
														<spring:message
															code="filemanagement.regenerated.tap.out.filesequence.tooltip"
															var="tooltip" ></spring:message>
														<div class="table-cell-label">${regeneratedTapOutFileSequence}<span
																class="required">*</span>
														</div>
														<div class="input-group ">
															<form:select path="testRegeneratedTapOutFileSequence"
																id="testRegeneratedTapOutFileSequence_id"
																cssClass="form-control table-cell input-sm"
																data-toggle="tooltip" data-placement="bottom"
																title="${tooltip }">
																<c:forEach var="fileSequenceEnum"
																	items="${fileSequenceEnum}">
																	<form:option value="${fileSequenceEnum.name}">${fileSequenceEnum}</form:option>
																</c:forEach>
															</form:select>
															<spring:bind path="testRegeneratedTapOutFileSequence">
																<elitecoreError:showError
																	errorId="testRegeneratedTapOutFileSequence_err"
																	errorMessage="${status.errorMessage}"></elitecoreError:showError>
															</spring:bind>
														</div>
													</div>
												</td>
											</tr>
											<tr>
												<td>
													<div class="form-group">
														<spring:message
															code="filemanagement.test.file.validation.lable"
															var="testFileValidation" ></spring:message>
														<spring:message
															code="filemanagement.test.file.validation.tooltip"
															var="tooltip" ></spring:message>
														<div class="table-cell-label">${testFileValidation}<span
																class="required">*</span>
														</div>
														<div class="input-group ">
															<form:select path="testFileValidation"
																id="testFileValidation_id"
																cssClass="form-control table-cell input-sm"
																data-toggle="tooltip" data-placement="bottom"
																title="${tooltip }">
																<c:forEach var="enalbeDisableEnum"
																	items="${enalbeDisableEnum}">
																	<form:option value="${enalbeDisableEnum.name}">${enalbeDisableEnum}</form:option>
																</c:forEach>
															</form:select>
															<spring:bind path="testFileValidation">
																<elitecoreError:showError
																	errorId="testFileValidation_err"
																	errorMessage="${status.errorMessage}"></elitecoreError:showError>
															</spring:bind>
														</div>
													</div>
												</td>
											</tr>
										</table>
									</td>

									<td class="col-md-4"  valign="top">
										<table>
											<tr>
												<td><div class="title2">
														<spring:message
															code="filemanagement.rap.configuration.heading" ></spring:message>
													</div></td>
											</tr>
											<tr>
												<td>
													<div class="form-group">
														<spring:message code="filemanagement.rap.in.version.lable"
															var="rapInVersion" ></spring:message>
														<spring:message
															code="filemanagement.rap.in.version.tooltip"
															var="tooltip" ></spring:message>
														<div class="table-cell-label">${rapInVersion}<span
																class="required">*</span>
														</div>
														<div class="input-group ">
															<form:select path="testRapInVersion" id="testRapInVersion_id"
																cssClass="form-control table-cell input-sm"
																data-toggle="tooltip" data-placement="bottom"
																title="${tooltip }" cssStyle="width :100%">
																	<form:option value = "3.12"></form:option>
																<form:option value="1.11">1.11</form:option>
																<form:option value="1.12">1.12</form:option>
																<form:option value="2.11">2.11</form:option>
																<form:option value="2.12">2.12</form:option>
																<form:option value="3.11">3.11</form:option>
																<form:option value="3.12">3.12</form:option>
															</form:select>
															<spring:bind path="testRapInVersion">
																<elitecoreError:showError errorId="testRapInVersion_err"
																	errorMessage="${status.errorMessage}"></elitecoreError:showError>
															</spring:bind>
														</div>
													</div>
												</td>
											</tr>
											<tr>
												<td>

													<div class="form-group">
														<spring:message
															code="filemanagement.rap.out.version.lable"
															var="rapOutVersion" ></spring:message>
														<spring:message
															code="filemanagement.rap.out.version.tooltip"
															var="tooltip" ></spring:message>
														<div class="table-cell-label">${rapOutVersion}<span
																class="required">*</span>
														</div>
														<div class="input-group ">
															<form:select path="testRapOutVersion" id="testRapOutVersion_id"
																cssClass="form-control table-cell input-sm"
																data-toggle="tooltip" data-placement="bottom"
																title="${tooltip }">
																	<form:option value = "3.12"></form:option>
																<form:option value="1.11">1.11</form:option>
																<form:option value="1.12">1.12</form:option>
																<form:option value="2.11">2.11</form:option>
																<form:option value="2.12">2.12</form:option>
																<form:option value="3.11">3.11</form:option>
																<form:option value="3.12">3.12</form:option>
															</form:select>
															<spring:bind path="testRapOutVersion">
																<elitecoreError:showError errorId="testRapOutVersion_err"
																	errorMessage="${status.errorMessage}"></elitecoreError:showError>
															</spring:bind>
														</div>
													</div>
												</td>
											</tr>

										</table>
									</td>
									<td class="col-md-4" valign="top">
										<table>
											<tr>
												<td><div class="title2">
														<spring:message
															code="filemanagement.nrtrde.configuration.heading" ></spring:message>
													</div></td>
											</tr>

											<tr>
												<td>
													<div class="form-group">
														<spring:message code="filemanagement.send.nrtrde.lable"
															var="sendNrtrde" ></spring:message>
														<spring:message code="filemanagement.send.nrtrde.tooltip"
															var="tooltip" ></spring:message>
														<div class="table-cell-lable col-md-5" style="padding-top: 5px;padding-left: 0px">${sendNrtrde}</div>
														<div class="input-group" style = "padding-left: 10px">
															<form:radiobutton path="testSendNrtrde" value="Yes" label="Yes" id = "testSendNrtrde_id" ></form:radiobutton>
            												 <form:radiobutton path="testSendNrtrde" value="No" label="No" id ="testSendNrtrde_id"></form:radiobutton>
															<spring:bind path="testSendNrtrde">
																<elitecoreError:showError errorId="sendNrtrde_err"
																	errorMessage="${status.errorMessage}"></elitecoreError:showError>
															</spring:bind>
														</div>
														
													</div>



												</td>

											</tr>
											<tr>
												<td>

													<div class="form-group">
														<spring:message
															code="filemanagement.nrtrde.in.version.lable"
															var="nrtrdeInVersion" ></spring:message>
														<spring:message
															code="filemanagement.nrtrde.in.version.tooltip"
															var="tooltip" ></spring:message>
														<div class="table-cell-label">${nrtrdeInVersion}<span
																class="required">*</span>
														</div>
														<div class="input-group ">
															<form:select path="testNrtrdeInVersion"
																id="testNrtrdeInVersion_id"
																cssClass="form-control table-cell input-sm"
																data-toggle="tooltip" data-placement="bottom"
																title="${tooltip }">
																	<form:option value = "3.12"></form:option>
																<form:option value="1.11">1.11</form:option>
																<form:option value="1.12">1.12</form:option>
																<form:option value="2.11">2.11</form:option>
																<form:option value="2.12">2.12</form:option>
																<form:option value="3.11">3.11</form:option>
																<form:option value="3.12">3.12</form:option>
															</form:select>
															<spring:bind path="testNrtrdeInVersion">
																<elitecoreError:showError errorId="testNrtrdeInVersion_err"
																	errorMessage="${status.errorMessage}"></elitecoreError:showError>
															</spring:bind>
														</div>
													</div>
												</td>
											</tr>
											<tr>
												<td>
													<div class="form-group">
														<spring:message
															code="filemanagement.nrtrde.out.version.lable"
															var="nrtrdeOutVersion" ></spring:message>
														<spring:message
															code="filemanagement.nrtrde.out.version.tooltip"
															var="tooltip" ></spring:message>
														<div class="table-cell-label">${nrtrdeOutVersion}<span
																class="required">*</span>
														</div>
														<div class="input-group ">
															<form:select path="testNrtrdeOutVersion"
																id="testNrtrdeOutVersion_id"
																cssClass="form-control table-cell input-sm"
																data-toggle="tooltip" data-placement="bottom"
																title="${tooltip }">
																	<form:option value = "3.12"></form:option>
																<form:option value="1.11">1.11</form:option>
																<form:option value="1.12">1.12</form:option>
																<form:option value="2.11">2.11</form:option>
																<form:option value="2.12">2.12</form:option>
																<form:option value="3.11">3.11</form:option>
																<form:option value="3.12">3.12</form:option>
															</form:select>
															<spring:bind path="testNrtrdeOutVersion">
																<elitecoreError:showError errorId="testNrtrdeOutVersion_err"
																	errorMessage="${status.errorMessage}"></elitecoreError:showError>
															</spring:bind>
														</div>
													</div>
												</td>
											</tr>
											<tr>
												<td>

													<div class="form-group">
														<spring:message
															code="filemanagement.max.records.nrtrde.out.lable"
															var="maxRecordsInNrtrdeOut" ></spring:message>
														<spring:message
															code="filemanagement.max.records.nrtrde.out.tooltip"
															var="tooltip" ></spring:message>
														<div class="table-cell-label">${maxRecordsInNrtrdeOut}<span
																class="required">*</span></div>
														<div class="input-group">
															<form:input path="testMaxRecordsInNrtrdeOut"
																id="testMaxRecordsInNrtrdeOut_id" onkeydown="isNumericOnKeyDown(event)"
																cssClass="form-control table-cell input-sm"
																maxlength="10" data-toggle="tooltip"
																data-placement="bottom" title="${tooltip }"></form:input>
															<spring:bind path="testMaxRecordsInNrtrdeOut">
																<elitecoreError:showError
																	errorId="testMaxRecordsInNrtrdeOut_err"
																	errorMessage="${status.errorMessage}"></elitecoreError:showError>
															</spring:bind>
														</div>
													</div>
												</td>
											</tr>
											<tr>
												<td>
													<div class="form-group">
														<spring:message
															code="filemanagement.max.file.size.nrtrde.out.lable"
															var="maxfileSizeOfnrtrdeOut" ></spring:message>
														<spring:message
															code="filemanagement.max.file.size.nrtrde.out.tooltip"
															var="tooltip" ></spring:message>
														<div class="table-cell-label">${maxfileSizeOfnrtrdeOut}<span
																class="required">*</span></div>
														<div class="input-group">
															<form:input path="testMaxfileSizeOfnrtrdeOut"
																id="testMaxfileSizeOfnrtrdeOut_id" onkeydown="isNumericOnKeyDown(event)"
																cssClass="form-control table-cell input-sm "
																maxlength="10" data-toggle="tooltip"
																data-placement="bottom" title="${tooltip }" cssStyle="width :89%;"></form:input>
																<span style="vertical-align: text-bottom;">MB</span>
																
															<spring:bind path="testMaxfileSizeOfnrtrdeOut">
																<elitecoreError:showError
																	errorId="testMaxfileSizeOfnrtrdeOut_err"
																	errorMessage="${status.errorMessage}"></elitecoreError:showError>
															</spring:bind>
																
														</div>
													</div>
											</td>
											</tr>
										</table>
									</td>
								</tr>
							</table>
							</div>
						</div>
					</div>
					
					<div>
					
					<div class="box box-warning">
						<div class="box-header with-border table-roaming-bordered"
							style="padding-left: 5px !important;">
							<h3 class="box-title">
								<spring:message
									code="roaming.Configuration.file.management.commercial.heading"></spring:message>
							</h3>
							<div class="box-tools pull-right" style="top: -3px;">
								<button class="btn btn-box-tool" data-widget="collapse">
									<i class="fa fa-minus"></i>
								</button>
							</div>
							<!-- /.box-tools -->
						</div>
						<!-- /.box-header -->
						<div class="box-body"style="padding-left: 0px !important;">
						<div class="fullwidtd inline-form">
							<table class = "table  table-bordered">
								<tr>
									<td class="col-md-4"  valign="top">
										<table>
											<tr>
												<td><div class="title2">
			<spring:message code="fileManagement.tap.configuration.heading" ></spring:message>
</div></td>
											</tr>
											<tr>
												<td>
													<div class="form-group">
														<spring:message code="filemanagement.tap.in.version.lable"
															var="tapInVersion" ></spring:message>
														<spring:message
															code="filemanagement.tap.in.version.tooltip"
															var="tooltip" ></spring:message>
														<div class="table-cell-label">${tapInVersion}<span
																class="required">*</span>
														</div>
														<div class="input-group ">
															<form:select path="commercialTapInVersion" id="commercialTapInVersion_id"
																cssClass="form-control table-cell input-sm"
																data-toggle="tooltip" data-placement="bottom"
																title="${tooltip }">
																	<form:option value = "3.12"></form:option>
																<form:option value="1.11">1.11</form:option>
																<form:option value="1.12">1.12</form:option>
																<form:option value="2.11">2.11</form:option>
																<form:option value="2.12">2.12</form:option>
																<form:option value="3.11">3.11</form:option>
																<form:option value="3.12">3.12</form:option>
															</form:select>
															<spring:bind path="commercialTapInVersion">
																<elitecoreError:showError
																	errorId="commercialTapInVersion_err"
																	errorMessage="${status.errorMessage}"></elitecoreError:showError>
															</spring:bind>
														</div>
													</div>
												</td>
											</tr>
											<tr>
												<td>

													<div class="form-group">
														<spring:message
															code="filemanagement.tap.out.version.lable"
															var="tapOutVersion" ></spring:message>
														<spring:message
															code="filemanagement.tap.out.version.tooltip"
															var="tooltip" ></spring:message>
														<div class="table-cell-label">${tapOutVersion}<span
																class="required">*</span>
														</div>
														<div class="input-group ">
															<form:select path="commercialTapOutVersion" id="commercialTapOutVersion_id"
																cssClass="form-control table-cell input-sm"
																data-toggle="tooltip" data-placement="bottom"
																title="${tooltip }">
																	<form:option value = "3.12"></form:option>
																<form:option value="1.11">1.11</form:option>
																<form:option value="1.12">1.12</form:option>
																<form:option value="2.11">2.11</form:option>
																<form:option value="2.12">2.12</form:option>
																<form:option value="3.11">3.11</form:option>
																<form:option value="3.12">3.12</form:option>
															</form:select>
															<spring:bind path="commercialTapOutVersion">
																<elitecoreError:showError errorId="commercialTapOutVersion_err"
																	errorMessage="${status.errorMessage}"></elitecoreError:showError>
															</spring:bind>
														</div>
													</div>
												</td>
											</tr>
											<tr>
												<td>
													<div class="form-group">
														<spring:message
															code="filemanagement.max.records.in.tap.out.lable"
															var="maxRecordsInTapOut" ></spring:message>
														<spring:message
															code="filemanagement.max.records.in.tap.out.tooltip"
															var="tooltip" ></spring:message>
														<div class="table-cell-label">${maxRecordsInTapOut}<span
																class="required">*</span></div>
														<div class="input-group">
															<form:input path="commercialMaxRecordsInTapOut"
																id="commercialMaxRecordsInTapOut_id" onkeydown="isNumericOnKeyDown(event)"
																cssClass="form-control table-cell input-sm"
																maxlength="10" data-toggle="tooltip"
																data-placement="bottom" title="${tooltip }" ></form:input>
															<spring:bind path="commercialMaxRecordsInTapOut">
																<elitecoreError:showError
																	errorId="commercialMaxRecordsInTapOut_err"
																	errorMessage="${status.errorMessage}"></elitecoreError:showError>
															</spring:bind>
														</div>
													</div>
												</td>
											</tr>
											<tr>
												<td>
													<div class="form-group">
														<spring:message
															code="filemanagement.max.filesize.of.tap.out.lable"
															var="maxFileSizeOfTapOut" ></spring:message>
														<spring:message
															code="filemanagement.max.filesize.of.tap.out.tooltip"
															var="tooltip" ></spring:message>
														<div class="table-cell-label">${maxFileSizeOfTapOut}<span
																class="required">*</span></div>
														<div class="input-group">
															<form:input path="commercialMaxFileSizeOfTapOut"
																id="commercialMaxFileSizeOfTapOut_id" onkeydown="isNumericOnKeyDown(event)"
																cssClass="form-control table-cell input-sm"
																maxlength="10" data-toggle="tooltip"
																data-placement="bottom" title="${tooltip }" cssStyle="width :89%;"></form:input>
																<span>MB</span>
															<spring:bind path="commercialMaxFileSizeOfTapOut">
																<elitecoreError:showError
																	errorId="commercialMaxFileSizeOfTapOut_err"
																	errorMessage="${status.errorMessage}"></elitecoreError:showError>
															</spring:bind>
														</div>
													</div>
												</td>
											</tr>
											<tr>
												<td>
													<div class="form-group">
														<spring:message
															code="filemanagement.send.tap.notification.file.lable"
															var="sendTapNotification" ></spring:message>
														<spring:message
															code="filemanagement.send.tap.notification.file.tooltip"
															var="tooltip" ></spring:message>
														<div class="table-cell-lable col-md-5" style="padding-top: 5px;padding-left: 0px">${sendTapNotification}</div>
														 <div class="input-group" style="padding-left: 10px">
															 <form:radiobutton path="commercialSendTapNotification" value="Yes" label="Yes" id = "commercialSendTapNotification_id" ></form:radiobutton>
            												 <form:radiobutton path="commercialSendTapNotification" value="No" label="No" id = "commercialSendTapNotification_id" ></form:radiobutton>
															<spring:bind path="commercialSendTapNotification">
																<elitecoreError:showError
																	errorId="sendTapNotification_err"
																	errorMessage="${status.errorMessage}"></elitecoreError:showError>
															</spring:bind>
														 </div>
													</div>
												</td>
											</tr>
											<tr>
												<td>
													<div class="form-group">
														<spring:message
															code="filemanagement.regenerated.tap.out.filesequence.lable"
															var="regeneratedTapOutFileSequence" ></spring:message>
														<spring:message
															code="filemanagement.regenerated.tap.out.filesequence.tooltip"
															var="tooltip" ></spring:message>
														<div class="table-cell-label">${regeneratedTapOutFileSequence}<span
																class="required">*</span>
														</div>
														<div class="input-group ">
															<form:select path="commercialRegeneratedTapOutFileSequence"
																id="commercialRegeneratedTapOutFileSequence_id"
																cssClass="form-control table-cell input-sm"
																data-toggle="tooltip" data-placement="bottom"
																title="${tooltip }">
																<c:forEach var = "fileSequenceEnum" items="${fileSequenceEnum}">
																<form:option value="${fileSequenceEnum.name}">${fileSequenceEnum}</form:option>
																
																</c:forEach>
															</form:select>
															<spring:bind path="commercialRegeneratedTapOutFileSequence">
																<elitecoreError:showError
																	errorId="commercialRegeneratedTapOutFileSequence_err"
																	errorMessage="${status.errorMessage}"></elitecoreError:showError>
															</spring:bind>
														</div>
													</div>
												</td>
											</tr>
											<tr>
												<td>
													<div class="form-group">
														<spring:message
															code="filemanagement.test.file.validation.lable"
															var="testFileValidation" ></spring:message>
														<spring:message
															code="filemanagement.test.file.validation.tooltip"
															var="tooltip" ></spring:message>
														<div class="table-cell-label">${testFileValidation}<span
																class="required">*</span>
														</div>
														<div class="input-group ">
															<form:select path="commercialFileValidation"
																id="commercialFileValidation_id"
																cssClass="form-control table-cell input-sm"
																data-toggle="tooltip" data-placement="bottom"
																title="${tooltip }">
																<c:forEach var="enalbeDisableEnum"
																	items="${enalbeDisableEnum}">
																	<form:option value="${enalbeDisableEnum.name}">${enalbeDisableEnum}</form:option>
																</c:forEach>
															</form:select>
															<spring:bind path="commercialFileValidation">
																<elitecoreError:showError
																	errorId="commercialFileValidation_err"
																	errorMessage="${status.errorMessage}"></elitecoreError:showError>
															</spring:bind>
														</div>
													</div>
												</td>
											</tr>
										</table>
									</td>

									<td class="col-md-4"  valign="top">
										<table>
											<tr>
												<td><div class="title2">
														<spring:message
															code="filemanagement.rap.configuration.heading" ></spring:message>
													</div></td>
											</tr>
											<tr>
												<td>
													<div class="form-group">
														<spring:message code="filemanagement.rap.in.version.lable"
															var="rapInVersion" ></spring:message>
														<spring:message
															code="filemanagement.rap.in.version.tooltip"
															var="tooltip" ></spring:message>
														<div class="table-cell-label">${rapInVersion}<span
																class="required">*</span>
														</div>
														<div class="input-group ">
															<form:select path="commercialRapInVersion" id="commercialRapInVersion_id"
																cssClass="form-control table-cell input-sm"
																data-toggle="tooltip" data-placement="bottom"
																title="${tooltip }">
																	<form:option value = "3.12"></form:option>
																<form:option value="1.11">1.11</form:option>
																<form:option value="1.12">1.12</form:option>
																<form:option value="2.11">2.11</form:option>
																<form:option value="2.12">2.12</form:option>
																<form:option value="3.11">3.11</form:option>
																<form:option value="3.12">3.12</form:option>
															</form:select>
															<spring:bind path="commercialRapInVersion">
																<elitecoreError:showError errorId="commercialRapInVersion_err"
																	errorMessage="${status.errorMessage}"></elitecoreError:showError>
															</spring:bind>
														</div>
													</div>
												</td>
											</tr>
											<tr>
												<td>

													<div class="form-group">
														<spring:message
															code="filemanagement.rap.out.version.lable"
															var="rapOutVersion" ></spring:message>
														<spring:message
															code="filemanagement.rap.out.version.tooltip"
															var="tooltip" ></spring:message>
														<div class="table-cell-label">${rapOutVersion}<span
																class="required">*</span>
														</div>
														<div class="input-group ">
															<form:select path="commercialRapOutVersion" id="commercialRapOutVersion_id"
																cssClass="form-control table-cell input-sm"
																data-toggle="tooltip" data-placement="bottom"
																title="${tooltip }">
																	<form:option value = "3.12"></form:option>
																<form:option value="1.11">1.11</form:option>
																<form:option value="1.12">1.12</form:option>
																<form:option value="2.11">2.11</form:option>
																<form:option value="2.12">2.12</form:option>
																<form:option value="3.11">3.11</form:option>
																<form:option value="3.12">3.12</form:option>
															</form:select>
															<spring:bind path="commercialRapOutVersion">
																<elitecoreError:showError errorId="commercialRapOutVersion_err"
																	errorMessage="${status.errorMessage}"></elitecoreError:showError>
															</spring:bind>
														</div>
													</div>
												</td>
											</tr>

										</table>
									</td>
									<td class="col-md-4"  valign="top">
										<table>
											<tr>
												<td><div class="title2">
														<spring:message
															code="filemanagement.nrtrde.configuration.heading" ></spring:message>
													</div></td>
											</tr>

											<tr>
												<td>
													<div class="form-group">
														<spring:message code="filemanagement.send.nrtrde.lable"
															var="sendNrtrde" ></spring:message>
														<spring:message code="filemanagement.send.nrtrde.tooltip"
															var="tooltip" ></spring:message>
														<div class="table-cell-lable col-md-5" style="padding-top: 5px;padding-left: 0px">${sendNrtrde}</div> 
														<div class="input-group " style="padding-left: 10px;">
															<form:radiobutton path="commercialSendNrtrde" value="Yes" label="Yes"  id= "commercialSendNrtrde_id"></form:radiobutton>
            												 <form:radiobutton path="commercialSendNrtrde" value="No" label="No" id= "commercialSendNrtrde_id" ></form:radiobutton>
															<spring:bind path="commercialSendNrtrde">
																<elitecoreError:showError errorId="commercialSendNrtrde_err"
																	errorMessage="${status.errorMessage}"></elitecoreError:showError>
															</spring:bind>
														</div>
														
													</div>



												</td>

											</tr>
											<tr>
												<td>

													<div class="form-group">
														<spring:message
															code="filemanagement.nrtrde.in.version.lable"
															var="nrtrdeInVersion" ></spring:message>
														<spring:message
															code="filemanagement.nrtrde.in.version.tooltip"
															var="tooltip" ></spring:message>
														<div class="table-cell-label">${nrtrdeInVersion}<span
																class="required">*</span>
														</div>
														<div class="input-group ">
															<form:select path="commercialNrtrdeInVersion"
																id="commercialNrtrdeInVersion_id"
																cssClass="form-control table-cell input-sm"
																data-toggle="tooltip" data-placement="bottom"
																title="${tooltip }">
																	<form:option value = "3.12"></form:option>
																<form:option value="1.11">1.11</form:option>
																<form:option value="1.12">1.12</form:option>
																<form:option value="2.11">2.11</form:option>
																<form:option value="2.12">2.12</form:option>
																<form:option value="3.11">3.11</form:option>
																<form:option value="3.12">3.12</form:option>
															</form:select>
															<spring:bind path="commercialNrtrdeInVersion">
																<elitecoreError:showError errorId="commercialNrtrdeInVersion_err"
																	errorMessage="${status.errorMessage}"></elitecoreError:showError>
															</spring:bind>
														</div>
													</div>
												</td>
											</tr>
											<tr>
												<td>
													<div class="form-group">
														<spring:message
															code="filemanagement.nrtrde.out.version.lable"
															var="nrtrdeOutVersion" ></spring:message>
														<spring:message
															code="filemanagement.nrtrde.out.version.tooltip"
															var="tooltip" ></spring:message>
														<div class="table-cell-label">${nrtrdeOutVersion}<span
																class="required">*</span>
														</div>
														<div class="input-group ">
															<form:select path="commercialNrtrdeOutVersion"
																id="commercialNrtrdeOutVersion_id"
																cssClass="form-control table-cell input-sm"
																data-toggle="tooltip" data-placement="bottom"
																title="${tooltip }">
																<form:option value = "3.12"></form:option>
																<form:option value="1.11">1.11</</form:option>
																<form:option value="1.12">1.12</form:option>
																<form:option value="2.11">2.11</form:option>
																<form:option value="2.12">2.12</form:option>
																<form:option value="3.11">3.11</form:option>
																<form:option value="3.12">3.12</form:option>
															</form:select>
															<spring:bind path="commercialNrtrdeOutVersion">
																<elitecoreError:showError errorId="commercialNrtrdeOutVersion_err"
																	errorMessage="${status.errorMessage}"></elitecoreError:showError>
															</spring:bind>
														</div>
													</div>
												</td>
											</tr>
											<tr>
												<td>

													<div class="form-group">
														<spring:message
															code="filemanagement.max.records.nrtrde.out.lable"
															var="maxRecordsInNrtrdeOut" ></spring:message>
														<spring:message
															code="filemanagement.max.records.nrtrde.out.tooltip"
															var="tooltip" ></spring:message>
														<div class="table-cell-label">${maxRecordsInNrtrdeOut}<span
																class="required">*</span></div>
														<div class="input-group">
															<form:input path="commercialMaxRecordsInNrtrdeOut"
																id="commercialMaxRecordsInNrtrdeOut_id" onkeydown="isNumericOnKeyDown(event)"
																cssClass="form-control table-cell input-sm"
																maxlength="10" data-toggle="tooltip"
																data-placement="bottom" title="${tooltip }"></form:input>
															<spring:bind path="commercialMaxRecordsInNrtrdeOut">
																<elitecoreError:showError
																	errorId="commercialMaxRecordsInNrtrdeOut_err"
																	errorMessage="${status.errorMessage}"></elitecoreError:showError>
															</spring:bind>
														</div>
													</div>
												</td>
											</tr>
											<tr>
												<td>
													<div class="form-group">
														<spring:message
															code="filemanagement.max.file.size.nrtrde.out.lable"
															var="maxfileSizeOfnrtrdeOut" ></spring:message>
														<spring:message
															code="filemanagement.max.file.size.nrtrde.out.tooltip"
															var="tooltip" ></spring:message>
														<div class="table-cell-label">${maxfileSizeOfnrtrdeOut}<span
																class="required">*</span></div>
														<div class="input-group">
															<form:input path="commercialMaxfileSizeOfnrtrdeOut"
																id="commercialMaxfileSizeOfnrtrdeOut_id" onkeydown="isNumericOnKeyDown(event)"
																cssClass="form-control table-cell input-sm"
																maxlength="10" data-toggle="tooltip"
																data-placement="bottom" title="${tooltip }" cssStyle="width :89%;" ></form:input>
																<span>MB</span>
															<spring:bind path="commercialMaxfileSizeOfnrtrdeOut">
																<elitecoreError:showError
																	errorId="commercialMaxfileSizeOfnrtrdeOut_err"
																	errorMessage="${status.errorMessage}"></elitecoreError:showError>
															</spring:bind>
														</div>
													</div>
												</td>
											</tr>
										</table>
									</td>
								</tr>
							</table>
							</div>
						</div>
					</div>
					
					
					
					</div>
					<form:hidden path="partnerName" id="partnerNameId"
						value="${partner}" ></form:hidden>
					<form:hidden path="lob" id="lobId" value="${lob}" ></form:hidden>
					<form:hidden path="secondaryTadig" id="secondaryTadigId"
						value="${secondaryTadig}" ></form:hidden>
					<form:hidden path="primaryTadig" id="primaryTadigId" value="${primaryTadig}" ></form:hidden>
				</form:form>
			</div>
		</div>
		
		<footer class="main-footer positfix">
			<div class="fooinn">
				<div class="fullwidth">
					<div class="padleft-right" id="generalParameterBtnsDiv">
						<button class="btn btn-grey btn-xs " id="host-conf-save-btn"
							type="button" onclick="saveOrUpdateFileManagement();">
							<spring:message code="btn.label.save" ></spring:message>
						</button>
						<button class="btn btn-grey btn-xs" id="host-conf-reset-btn"
							type="button" onclick="resetFileManagementForm();">
							<spring:message code="btn.label.reset" ></spring:message>
						</button>
						<%-- <button class="btn btn-grey btn-xs"  id="host-conf-cancel-btn" type="button" onclick="cancelHostConfigurationForm();"><spring:message code="btn.label.cancel" ></spring:message></button> --%>
						<%-- <button class="btn btn-grey btn-xs"  id="edit-btn"  type="button" onclick="editSystemParam();"><spring:message code="btn.label.edit" ></spring:message></button> --%>
					</div>

				</div>
			</div>
			<jsp:include page="../common/newfooter.jsp"></jsp:include>
		</footer>


	</div>

</body>
</html>
