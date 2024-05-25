
<%@page import="com.elitecore.sm.util.MapCache"%>
<%@page
	import="com.elitecore.sm.common.constants.SystemParametersConstant"%>
<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<%@page import="com.elitecore.sm.util.EliteUtils"%>
<%@page import="com.elitecore.sm.common.constants.BaseConstants"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/security/tags"
	prefix="sec"%>
<%@taglib uri="/WEB-INF/ShowErrorTag.tld" prefix="elitecoreError"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<html>
<body class="skin-blue sidebar-mini sidebar-collapse">
	<%-- <jsp:include page="../common/newheader.jsp" ></jsp:include>
<jsp:include page="../common/newleftMenu.jsp"></jsp:include> --%>

	<div class="fullwidth inline-form"
		style="padding-left: 0px !important;">
		<div class="box-body padding0 mtop10 clearfix" id="host_Confg_Block">
			<div class="fullwidth mtop10">
				<form:form modelAttribute="roaming_form_bean" method="POST"
					action="addService" id="save-roaming-parm-form">
					<div class="box box-warning">
						<div class="box-header with-border"
							style="padding-left: 22px !important;">
							<h3 class="box-title">
								<spring:message code="roamingconfiguration.roamingparameter.tab" ></spring:message>
							</h3>
							<div class="box-tools pull-right" style="top: -3px;">
								<button class="btn btn-box-tool" data-widget="collapse">
									<i class="fa fa-minus"></i>
								</button>
							</div>
							<!-- /.box-tools -->
						</div>
						<!-- /.box-header -->
						<div class="box-body">

							<spring:message
								code="roamingconfiguration.roaming.parameter.tapin.label"
								var="tapinFrequency" ></spring:message>
							<spring:message
								code="roamingconfiguration.roaming.parameter.tapin.tooltip"
								var="tooltip" ></spring:message>
							<spring:message
								code="roaming.configuration.frequency.of.tap.in.files.(mins).description"
								var="tapinDescription" ></spring:message>
							<div class="col-md-2 mtop10">${tapinFrequency}<span
									class="required">*</span>
							</div>

							<div class="col-md-4" style="padding-left: 0;">
								<div class="form-group">
									<div class="input-group">
										<form:input path="tapinFrequency" id="tapin_frequencyId"
											cssClass="form-control table-cell input-sm" maxlength="50"
											data-toggle="tooltip" data-placement="bottom"
											title="${tooltip }" ></form:input>
										<spring:bind path="tapinFrequency">
											<elitecoreError:showError errorId="name_err"
												errorMessage="${status.errorMessage}"></elitecoreError:showError>
										</spring:bind>
									</div>
								</div>
							</div>
							<div class="col-md-6">
								<p>${tapinDescription}</p>
							</div>
							<div class="clearfix"></div>


							<spring:message
								code="roamingconfiguration.roaming.parameter.nrtrde.in.label"
								var="nrtrdeInFrequency" ></spring:message>
							<spring:message
								code="roamingconfiguration.roaming.parameter.nrtrde.in.tooltip"
								var="tooltip" ></spring:message>
							<spring:message
								code="roaming.configuration.frequency.of.nrtrde.in.files.(mins).description"
								var="nrtrdeInDescription" ></spring:message>
							<div class="col-md-2 mtop10">${nrtrdeInFrequency}<span
									class="required">*</span>
							</div>
							<div class="col-md-4" style="padding-left: 0;">
								<div class="form-group">
									<div class="input-group">
										<form:input path="nrtrdeInFrequency"
											id="nrtrde_in_frequencyId"
											cssClass="form-control table-cell input-sm" rows="4"
											data-toggle="tooltip" data-placement="bottom"
											title="${tooltip }" ></form:input>
										<spring:bind path="nrtrdeInFrequency">
											<elitecoreError:showError errorId="description_err"
												errorMessage="${status.errorMessage}"></elitecoreError:showError>
										</spring:bind>

									</div>
								</div>
							</div>
							<div class="col-md-6">
								<p>${nrtrdeInDescription}</p>
							</div>
							<div class="clearfix"></div>



							<spring:message
								code="roamingconfiguration.roaming.parameter.nrtrde.out.label"
								var="nrtrdeOutFrequency" ></spring:message>
							<spring:message
								code="roamingconfiguration.roaming.parameter.nrtrde.out.tooltip"
								var="tooltip" ></spring:message>
							<spring:message
								code="roaming.configuration.frequency.of.nrtrde.out.files.(mins).description"
								var="nrtrdeOutDescription" ></spring:message>
							<div class="col-md-2 mtop10">${nrtrdeOutFrequency}<span
									class="required">*</span>
							</div>
							<div class="col-md-4" style="padding-left: 0;">
								<div class="form-group">
									<div class="input-group">
										<form:input path="nrtrdeOutFrequency"
											id="nrtrde_out_frequencyId"
											cssClass="form-control table-cell input-sm"
											data-toggle="tooltip" data-placement="bottom"
											title="${tooltip }" ></form:input>
										<spring:bind path="nrtrdeOutFrequency">
											<elitecoreError:showError errorId="host_timezone_err"
												errorMessage="${status.errorMessage}"></elitecoreError:showError>
										</spring:bind>

									</div>
								</div>
							</div>
							<div class="col-md-6">
								<p>${nrtrdeOutDescription}</p>
							</div>
							<div class="clearfix"></div>


<%-- 							<spring:message
								code="roamingconfiguration.roaming.parameter.notify.record.label"
								var="notifyOnRecord" ></spring:message>
							<spring:message
								code="roamingconfiguration.roaming.parameter.notify.record.tooltip"
								var="tooltip" ></spring:message>
							<spring:message
								code="roaming.configuration.notify.on.no.record.found.description"
								var="notifyDescription" ></spring:message>
							<div class="col-md-2 mtop10">${notifyOnRecord}<span
									class="required">*</span>
							</div>
							<div class="col-md-4" style="padding-left: 0;">
								<div class="form-group">
									<div class="input-group">
										<form:select path="notifyOnRecord" id="notify_on_recordId"
											cssClass="form-control table-cell input-sm"
											data-toggle="tooltip" data-placement="bottom"
											title="${tooltip }">
											<c:forEach var="enalbeDisableEnum"
												items="${enalbeDisableEnum}">
												<form:option value="${enalbeDisableEnum.name}">${enalbeDisableEnum}</form:option>
											</c:forEach>
										</form:select>
										<spring:bind path="notifyOnRecord">
											<elitecoreError:showError errorId="pmncode_err"
												errorMessage="${status.errorMessage}"></elitecoreError:showError>
										</spring:bind>

									</div>
								</div>
							</div>
							<div class="col-md-6">
								<p>${notifyDescription}</p>
							</div>
							<div class="clearfix"></div> --%>
						</div>
					</div>
				</form:form>
			</div>

		</div>
		<footer class="main-footer positfix">
			<div class="fooinn">
				<div class="fullwidth">
					<div class="padleft-right" id="generalParameterBtnsDiv"">
						<button class="btn btn-grey btn-xs " id="host-conf-save-btn"
							type="button" onclick="validateRoamingParameterFields();">
							<spring:message code="btn.label.save" ></spring:message>
						</button>
						<button class="btn btn-grey btn-xs" id="host-conf-reset-btn"
							type="button" onclick="resetRoamingParameterForm();">
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

<script type="text/javascript">
function validateRoamingParameterFields(){
	$("#save-roaming-parm-form").attr("action","<%=ControllerConstants.MODIFY_ROAMING_PARAMETER%>")
		$("#save-roaming-parm-form").submit();

	}
	function resetRoamingParameterForm() {
		$("#tapin_frequencyId").val('');
		$("#nrtrde_in_frequencyId").val('');
		$("#nrtrde_out_frequencyId").val('');
		resetWarningDisplay();
	}
</script>
</html>
