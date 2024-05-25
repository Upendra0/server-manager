<%@page import="com.itextpdf.text.Document"%>
<%@page import="com.elitecore.sm.util.MapCache"%>
<%@page
	import="com.elitecore.sm.common.constants.SystemParametersConstant"%>
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
<script type="text/javascript">

function resetFileSequenceForm(){
	$("#test-tap-in").val('');
	$("#commercial-tap-in").val('');
	$("#commercial-rap-out").val('');
	$("#test-rap-out").val('');
	$("#commercail-rap-in").val('');
	$("#test-rap-in").val('');
	$("#commercial-tap-out").val('');
	$("#test-tap-out").val('');
	$("#nrtrdeOut").val('');
	$("#nrtrdeIn").val('');
	resetWarningDisplay();
}
$("#partnerNameId").val(document.getElementById("partner").value);
$("#lobId").val(document.getElementById("lob").value);
$("#primaryTadigId").val(document.getElementById("primaryTadig").value);
$("#secondaryTadigId").val(document.getElementById("secondaryTadig").value);
function validateFileSequenceFields(){
	$("#partnerNameId").val(document.getElementById("partner").value);
	$("#lobId").val(document.getElementById("lob").value);
	$("#primaryTadigId").val(document.getElementById("primaryTadig").value);
	$("#secondaryTadigId").val(document.getElementById("secondaryTadig").value);
	
	var testTapIn  =  $("#test-tap-in").val(); 
	var commercialTapIn = $("#commercial-tap-in").val();
	var commercialRapOut = $("#commercial-rap-out").val();
	var testRapOut = $("#test-rap-out").val();
	var commercialrapIn = $("#commercail-rap-in").val();
	var testRapIn = $("#test-rap-in").val();
	var commercialTapOut = $("#commercial-tap-out").val();
	var testTapOut = $("#test-tap-out").val();
	var nrtrdeOut = $("#nrtrdeOut").val();
	var nrtrdeIn = $("#nrtrdeIn").val();
	
		
		
		if(testTapIn == '' || testTapIn == null){
			$("#test-tap-in").val(0);
		}
		if(commercialTapIn == '' || commercialTapIn == null){
			$("#commercial-tap-in").val(0);
		}
		if(testTapIn == '' || testTapIn == null){
			$("#commercial-rap-out").val(0);
		}
		if(testTapIn == '' || testTapIn == null){
			$("#test-rap-out").val(0);
		}
		if(testTapIn == '' || testTapIn == null){
			$("#commercail-rap-in").val(0);
		}
		if(testTapIn == '' || testTapIn == null){
			$("#test-rap-in").val(0);
		}
		if(testTapIn == '' || testTapIn == null){
			$("#commercial-tap-out").val(0);
		}
		if(testTapIn == '' || testTapIn == null){
			 $("#test-tap-out").val(0);
		}
		if(testTapIn == '' || testTapIn == null){
			$("#nrtrdeOut").val(0);
		}
		if(testTapIn == '' || testTapIn == null){
			$("#nrtrdeIn").val(0);
		}
	
	$("#save-file-sequence-form").attr("action","<%=ControllerConstants.MODIFY_FILE_SEQUENCE_MANAGEMENT%>")
		$("#save-file-sequence-form").submit();
	}
$(document).ready(function () {
	displaySequencyNumber();
});
function displaySequencyNumber(){
	var testTapIn  =  $("#test-tap-in").val(); 
	var commercialTapIn = $("#commercial-tap-in").val();
	var commercialRapOut = $("#commercial-rap-out").val();
	var testRapOut = $("#test-rap-out").val();
	var commercialrapIn = $("#commercail-rap-in").val();
	var testRapIn = $("#test-rap-in").val();
	var commercialTapOut = $("#commercial-tap-out").val();
	var testTapOut = $("#test-tap-out").val();
	var nrtrdeOut = $("#nrtrdeOut").val();
	var nrtrdeIn = $("#nrtrdeIn").val();
	
	 $('#testTapInSequenceId').html( "<p>"+"<spring:message code="roaming.configuration.file.sequence.test.tap.in.description" ></spring:message>"+testTapIn+"</p>");
	 $('#commercialTapInSequenceId').html( "<p>"+"<spring:message code="roaming.configuration.file.sequence.commercial.tap.in.description"></spring:message>"+commercialTapIn+"</p>");
	 $('#testTapOutSequenceId').html( "<p>"+"<spring:message code="roaming.configuration.file.sequence.test.tap.out.description"></spring:message>"+testTapOut+"</p>");
	 $('#commercialTapOutSequenceId').html( "<p>"+"<spring:message code="roaming.configuration.file.sequence.commercial.tap.out.description" ></spring:message>"+commercialTapOut+"</p>");
	 $('#testRapInSequenceId').html( "<p>"+"<spring:message code="roaming.configuration.file.sequence.test.rap.in.description"></spring:message>"+testRapIn+"</p>"); 
	 $('#commercialRapInSequenceId').html( "<p>"+"<spring:message code="roaming.configuration.file.sequence.commercial.rap.in.description" ></spring:message>"+commercialrapIn+"</p>");
	 $('#testRapOutSequenceId').html( "<p>"+"<spring:message code="roaming.configuration.file.sequence.test.rap.out.description"></spring:message>"+testRapOut+"</p>");
	 $('#commercialRapOutSequenceId').html( "<p>"+"<spring:message code="roaming.configuration.file.sequence.commercial.rap.out.description" ></spring:message>"+commercialRapOut+"</p>");
	 $('#nrtrdeInSequenceId').html( "<p>"+"<spring:message code="roaming.configuration.file.sequence.nrtrde.in.description" ></spring:message>"+nrtrdeIn+"</p>");
	 $('#nrtrdeOutSequenceId').html(  "<p>"+"<spring:message code="roaming.configuration.file.sequence.nrtrde.out.description" ></spring:message>"+nrtrdeOut+"</p>");
}





</script>

</head>
<body class="skin-blue sidebar-mini sidebar-collapse">
	<%-- <jsp:include page="../common/newheader.jsp" ></jsp:include>
	<jsp:include page="../common/newleftMenu.jsp"></jsp:include> --%>
	<div class="fullwidth inline-form"
		style="padding-left: 0px !important;">
		<div class="box-body padding0 mtop10 clearfix" id="host_Confg_Block">
			<div class="fullwidth mtop10">
				<form:form modelAttribute="file_sequence_management" method="POST"
					action="addService" id="save-file-sequence-form">




					<div class="box box-warning">
						<!-- <div class="fullwidth"> -->
						<div class="box-header with-border"
							style="padding-left: 10px !important;">
							<h3 class="box-title">
								<spring:message
									code="roaming.configuration.file.sequence.heading" ></spring:message>
							</h3>
						</div>


						<form:hidden path="partnerName" id="partnerNameId" value="${lob}" ></form:hidden>
						<form:hidden path="lob" id="lobId" value="${partner}" ></form:hidden>
						<form:hidden path="secondaryTadig" id="secondaryTadigId"
						value="${secondaryTadig}" ></form:hidden>
					<form:hidden path="primaryTadig" id="primaryTadigId" value="${primaryTadig}" ></form:hidden>
						<div class="box-body inline-form">
							<div class="col-md-6 ">
								<div class="form-group ">
									<spring:message
										code="roaming.configuration.file.sequence.test.tap.in.label"
										var="testTapIn" ></spring:message>
									<spring:message
										code="roaming.configuration.file.sequence.test.tap.in.tooltip"
										var="tooltip" ></spring:message>
									<div class="table-cell-label " >${testTapIn}<span
											class="required">*</span>
									</div>
									<div class="input-group ">
										<form:input path="testTapIn"  onkeydown='isNumericOnKeyDown(event)'
											cssClass="form-control table-cell input-sm" id="test-tap-in"
											data-toggle="tooltip" maxlength="5" data-placement="bottom"
											title="${tooltip}"></form:input>
										<spring:bind path="testTapIn">
											<elitecoreError:showError
												errorMessage="${status.errorMessage}"
												errorId="test-tap-in_error"></elitecoreError:showError>
										</spring:bind>
										<div id = "testTapInSequenceId" class="input-group" ></div>
									</div>
									</div>

								
							</div>

							<div class="col-md-6">
								<div class="form-group">
									<spring:message
										code="roaming.configuration.file.sequence.commercial.tap.in.label"
										var="commercialTapIn" ></spring:message>
									<spring:message
										code="roaming.configuration.file.sequence.commercial.tap.in.tooltip"
										var="tooltip" ></spring:message>
									<div class="table-cell-label">${commercialTapIn}
										<span class="required">*</span>
									</div>
									<div class="input-group ">
										<form:input path="commercialTapIn" onkeydown="isNumericOnKeyDown(event)"
											cssClass ="form-control table-cell input-sm" tabindex="2"
											id="commercial-tap-in" maxlength="5" data-toggle="tooltip"
											data-placement="bottom" title="${tooltip}" ></form:input>
										<spring:bind path="commercialTapIn">
											<elitecoreError:showError
												errorMessage="${status.errorMessage}"
												errorId="commercial-tap-in_error"></elitecoreError:showError>
										</spring:bind>
										<div id = "commercialTapInSequenceId" ></div>
									</div>

								</div>
							</div>

							<div class="col-md-6">
								<div class="form-group">
									<spring:message
										code="roaming.configuration.file.sequence.test.tap.out.label"
										var="testTapOut" ></spring:message>
									<spring:message
										code="roaming.configuration.file.sequence.test.tap.out.tooltip"
										var="tooltip" ></spring:message>
									<div class="table-cell-label" >${testTapOut}<span
											class="required">*</span>
									</div>
									<div class="input-group" >
										<form:input path="testTapOut" onkeydown="isNumericOnKeyDown(event)"
											cssClass="form-control table-cell input-sm" tabindex="3"
											id="test-tap-out" maxlength="5" data-toggle="tooltip"
											data-placement="bottom" title="${tooltip}" ></form:input>
										<spring:bind path="testTapOut">
											<elitecoreError:showError
												errorMessage="${status.errorMessage}"
												errorId="test-tap-out_error"></elitecoreError:showError>
										</spring:bind>
										<div id = "testTapOutSequenceId" ></div>
									</div>
									
								</div>
							</div>

							<div class="col-md-6">

								<div class="form-group">
									<spring:message
										code="roaming.configuration.file.sequence.commercial.tap.out.label"
										var="commercialTapOut" ></spring:message>
									<spring:message
										code="roaming.configuration.file.sequence.commercial.tap.out.tooltip"
										var="tooltip" ></spring:message>
									<div class="table-cell-label ">${commercialTapOut}<span
											class="required">*</span>
									</div>
									<div class="input-group ">
										<form:input path="commercialTapOut" onkeydown="isNumericOnKeyDown(event)"
											cssClass="form-control table-cell input-sm" tabindex="4"
											id="commercial-tap-out" maxlength="5" data-toggle="tooltip"
											data-placement="bottom" title="${tooltip}" ></form:input>
										<spring:bind path="commercialTapOut">
											<elitecoreError:showError
												errorMessage="${status.errorMessage}"
												errorId="commercial-tap-out_error"></elitecoreError:showError>
										</spring:bind>
										<div id = "commercialTapOutSequenceId" ></div>
									</div>

								</div>
							</div>

							<div class="col-md-6">
								<div class="form-group">
									<spring:message
										code="roaming.configuration.file.sequence.test.rap.in.label"
										var="testRapIn" ></spring:message>
									<spring:message
										code="roaming.configuration.file.sequence.test.rap.in.tooltip"
										var="tooltip" ></spring:message>
									<div class="table-cell-label ">${testRapIn}<span
											class="required">*</span>
									</div>
									<div class="input-group ">
										<form:input path="testRapIn" onkeydown="isNumericOnKeyDown(event)"
											cssClass="form-control table-cell input-sm" tabindex="5"
											id="test-rap-in" maxlength="5" data-toggle="tooltip"
											data-placement="bottom" title="${tooltip}" ></form:input>
										<spring:bind path="testRapIn">
											<elitecoreError:showError
												errorMessage="${status.errorMessage}"
												errorId="test-rap-in_error"></elitecoreError:showError>
										</spring:bind>
										<div id = "testRapInSequenceId" ></div>
									</div>

								</div>
							</div>



							<div class="col-md-6">
								<div class="form-group">
									<spring:message
										code="roaming.configuration.file.sequence.commercial.rap.in.label"
										var="commercialRapIn" ></spring:message>
									<spring:message
										code="roaming.configuration.file.sequence.commercial.rap.in.tooltip"
										var="tooltip" ></spring:message>
									<div class="table-cell-label ">${commercialRapIn}<span
											class="required">*</span>
									</div>
									<div class="input-group ">
										<form:input path="commercialRapIn" onkeydown="isNumericOnKeyDown(event)"
											cssClass="form-control table-cell input-sm" tabindex="6"
											id="commercail-rap-in" maxlength="5" data-toggle="tooltip"
											data-placement="bottom" title="${tooltip}" ></form:input>
										<spring:bind path="commercialRapIn">
											<elitecoreError:showError
												errorMessage="${status.errorMessage}"
												errorId="commercail-rap-in_error"></elitecoreError:showError>
										</spring:bind>
										<div id = "commercialRapInSequenceId"></div>
									</div>

								</div>
							</div>

							<div class="col-md-6">
								<div class="form-group">
									<spring:message
										code="roaming.configuration.file.sequence.test.rap.out.label"
										var="testRapOut" ></spring:message>
									<spring:message
										code="roaming.configuration.file.sequence.test.rap.out.tooltip"
										var="tooltip" ></spring:message>
									<div class="table-cell-label ">${testRapOut}<span
											class="required">*</span>
									</div>
									<div class="input-group ">
										<form:input path="testRapOut" onkeydown="isNumericOnKeyDown(event)"
											cssClass="form-control table-cell input-sm" tabindex="7"
											id="test-rap-out" maxlength="5" data-toggle="tooltip"
											data-placement="bottom" title="${tooltip}" ></form:input>
										<spring:bind path="testRapOut">
											<elitecoreError:showError
												errorMessage="${status.errorMessage}"
												errorId="test-rap-out_error"></elitecoreError:showError>
										</spring:bind>
										<div id = "testRapOutSequenceId"></div>
									</div>

								</div>
							</div>

							<div class="col-md-6">
								<div class="form-group">
									<spring:message
										code="roaming.configuration.file.sequence.commercial.rap.out.label"
										var="commercialRapOut" ></spring:message>
									<spring:message
										code="roaming.configuration.file.sequence.commercial.rap.out.tooltip"
										var="tooltip" ></spring:message>
									<div class="table-cell-label">${commercialRapOut}<span
											class="required">*</span>
									</div>
									<div class="input-group">
										<form:input path="commercialRapOut" onkeydown="isNumericOnKeyDown(event)"
											cssClass="form-control table-cell input-sm" tabindex="8"
											id="commercial-rap-out" maxlength="5" data-toggle="tooltip"
											data-placement="bottom" title="${tooltip}" ></form:input>
										<spring:bind path="commercialRapOut">
											<elitecoreError:showError
												errorMessage="${status.errorMessage}"
												errorId="commercial-rap-out_error"></elitecoreError:showError>
										</spring:bind>
										<div id = "commercialRapOutSequenceId"></div>
									</div>

								</div>
							</div>

							<div class="col-md-6">
								<div class="form-group">

									<spring:message
										code="roaming.configuration.file.sequence.nrtrde.in.label"
										var="nrtrdeIn" ></spring:message>
									<spring:message
										code="roaming.configuration.file.sequence.nrtrde.in.tooltip"
										var="tooltip" ></spring:message>
									
									<div class="table-cell-label">${nrtrdeIn}<span
											class="required">*</span>
									</div>
									<div class="input-group">
										<form:input path="nrtrdeIn" onkeydown="isNumericOnKeyDown(event)"
											cssClass="form-control table-cell input-sm" tabindex="9"
											id="nrtrdeIn" maxlength="7" data-toggle="tooltip"
											data-placement="bottom" title="${tooltip}" ></form:input>
										<spring:bind path="nrtrdeIn">
											<elitecoreError:showError
												errorMessage="${status.errorMessage}"
												errorId="nrtrdeIn_error"></elitecoreError:showError>
										</spring:bind>
										<div id = "nrtrdeInSequenceId"></div>
									</div>

								</div>
							</div>



							<div class="col-md-6">
								<div class="form-group">
									<spring:message
										code="roaming.configuration.file.sequence.nrtrde.out.label"
										var="nrtrdeOut" ></spring:message>
									<spring:message
										code="roaming.configuration.file.sequence.nrtrde.out.tooltip"
										var="tooltip" ></spring:message>
									
									<div class="table-cell-label">${nrtrdeOut}<span
											class="required">*</span>
									</div>
									<div class="input-group">
										<form:input path="nrtrdeOut" onkeydown="isNumericOnKeyDown(event)"
											cssClass="form-control table-cell input-sm" tabindex="10"
											id="nrtrdeOut" maxlength="7" data-toggle="tooltip"
											data-placement="bottom" title="${tooltip}"></form:input>
										<spring:bind path="nrtrdeOut">
											<elitecoreError:showError
												errorMessage="${status.errorMessage}"
												errorId="nrtrdeOut_error"></elitecoreError:showError>
										</spring:bind>
										<div id = "nrtrdeOutSequenceId"></div>
									</div>

								</div>
							</div>
						</div>

						<!-- </div> -->
					</div>


				</form:form>

				<footer class="main-footer positfix">
					<div class="fooinn">
						<div class="fullwidth">
							<div class="padleft-right" id="generalParameterBtnsDiv">
								<button class="btn btn-grey btn-xs " id="host-conf-save-btn"
									type="button" onclick="validateFileSequenceFields();">
									<spring:message code="btn.label.save" ></spring:message>
								</button>
								<button class="btn btn-grey btn-xs" id="host-conf-reset-btn"
									type="button" onclick="resetFileSequenceForm();">
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
		</div>

	</div>

</body>


</html>
