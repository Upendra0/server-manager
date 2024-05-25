<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="/WEB-INF/ShowErrorTag.tld" prefix="elitecoreError"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<div id="diameter_coll_services_operational_param_div">
<div class="box box-warning">
	<div class="box-header with-border">
		<h3 class="box-title">
			<spring:message code="diameter.service.config.operationalparam.header" ></spring:message>
		</h3>
		<div class="box-tools pull-right" style="top: -3px;">
			<button class="btn btn-box-tool" data-widget="collapse">
				<i class="fa fa-minus"></i>
			</button>
		</div>
		<!-- /.box-tools -->
	</div>
	<!-- /.box-header end here -->
	<div class="box-body">
		<div class="fullwidth inline-form">
			<div class="col-md-6 no-padding">
				<spring:message code="diameter.service.config.operationalparam.session.cleanup" var="label" ></spring:message>
				<spring:message code="diameter.service.config.operationalparam.session.cleanup.tooltip" var="tooltip" ></spring:message>
				<div class="form-group">
					<div class="table-cell-label">${label}<span class="required">*</span></div>
					<div class="input-group ">
						<form:input path="sessionCleanupInterval" cssClass="form-control table-cell input-sm" tabindex="4"	id="service-sessionCleanupInterval" data-toggle="tooltip" data-placement="bottom" title="${tooltip }" onkeydown='isNumericOnKeyDown(event)'></form:input>
						<spring:bind path="sessionCleanupInterval">
							<elitecoreError:showError errorMessage="${status.errorMessage}" errorId="service-sessionCleanupInterval_error"></elitecoreError:showError>
						</spring:bind>
					</div>
				</div>
			</div>
			
			<div class="col-md-6 no-padding">
				<spring:message code="diameter.service.config.operationalparam.session.timeout" var="label" ></spring:message>
				<spring:message code="diameter.service.config.operationalparam.session.timeout.tooltip" var="tooltip" ></spring:message>
				<div class="form-group">
					<div class="table-cell-label">${label}<span class="required">*</span></div>
					<div class="input-group ">
						<form:input path="sessionTimeout" cssClass="form-control table-cell input-sm" tabindex="4"	id="service-sessionTimeout" data-toggle="tooltip" data-placement="bottom" title="${tooltip }" onkeydown='isNumericOnKeyDown(event)'></form:input>
						<spring:bind path="sessionTimeout">
							<elitecoreError:showError errorMessage="${status.errorMessage}" errorId="service-sessionTimeout_error"></elitecoreError:showError>
						</spring:bind>
					</div>
				</div>
			</div>
			
			<div class="col-md-6 no-padding">
				<spring:message code="diameter.service.config.operationalparam.action.on.overload" var="label" ></spring:message>
				<spring:message code="diameter.service.config.operationalparam.action.on.overload.tooltip" var="tooltip" ></spring:message>
				<div class="form-group">
					<div class="table-cell-label">${label}<span class="required">*</span></div>
					<div class="input-group ">
						<form:select path="actionOnOverload" cssClass="form-control table-cell input-sm" tabindex="4" onchange="enableResultCode();"
							id="service-actionOnOverload" data-toggle="tooltip" data-placement="bottom" title="${tooltip }"> 
								<c:forEach items="${actionOnOverload}" var="actionOnOverload">
									<form:option value="${actionOnOverload}">${actionOnOverload}</form:option>
								</c:forEach>
						</form:select>
						<spring:bind path="actionOnOverload">
							<elitecoreError:showError
								errorMessage="${status.errorMessage}" errorId="service-actionOnOverload_error"></elitecoreError:showError>
						</spring:bind>
					</div>
				</div>
			</div>
			
			<div class="col-md-6 no-padding">
				<spring:message code="diameter.service.config.operationalparam.resultcode.on.overload" var="label" ></spring:message>
				<spring:message code="diameter.service.config.operationalparam.resultcode.on.overload.tooltip" var="tooltip" ></spring:message>
				<div class="form-group">
					<div class="table-cell-label">${label}<span class="required">*</span></div>
					<div class="input-group ">
						<form:input path="resultCodeOnOverload" cssClass="form-control table-cell input-sm" tabindex="4" id="service-resultCodeOnOverload" data-toggle="tooltip" data-placement="bottom" title="${tooltip }" onkeydown='isNumericOnKeyDown(event)'></form:input>
						<spring:bind path="resultCodeOnOverload">
							<elitecoreError:showError errorMessage="${status.errorMessage}" errorId="service-resultCodeOnOverload_error"></elitecoreError:showError>
						</spring:bind>
					</div>
				</div>
			</div>
			
			<div class="col-md-6 no-padding">
				<spring:message code="diameter.service.config.operationalparam.duplicate.request.check" var="label" ></spring:message>
				<spring:message code="diameter.service.config.operationalparam.duplicate.request.check.tooltip" var="tooltip" ></spring:message>
				<div class="form-group">
					<div class="table-cell-label">${label}<span class="required">*</span></div>
					<div class="input-group ">
						<form:select path="duplicateRequestCheck" cssClass="form-control table-cell input-sm" tabindex="4" onchange="enablePurgeInterval();"
							id="service-duplicateRequestCheck" data-toggle="tooltip"
							data-placement="bottom" title="${tooltip }">
							<c:forEach var="trueFalseEnum" items="${truefalseEnum}">
	                        	<form:option value="${trueFalseEnum.name}">${trueFalseEnum}</form:option>
	                        </c:forEach>
						</form:select>
						<spring:bind path="duplicateRequestCheck">
							<elitecoreError:showError
								errorMessage="${status.errorMessage}" errorId="service-duplicateRequestCheck_error"></elitecoreError:showError>
						</spring:bind>
					</div>
				</div>
			</div>
			
			<div class="col-md-6 no-padding">
				<spring:message code="diameter.service.config.operationalparam.duplicate.purge.interval" var="label" ></spring:message>
				<spring:message code="diameter.service.config.operationalparam.duplicate.purge.interval.tooltip" var="tooltip" ></spring:message>
				<div class="form-group">
					<div class="table-cell-label">${label}<span class="required">*</span></div>
					<div class="input-group ">
						<form:input path="duplicatePurgeInterval" cssClass="form-control table-cell input-sm" tabindex="4" id="service-duplicatePurgeInterval" data-toggle="tooltip" data-placement="bottom" title="${tooltip }" onkeydown='isNumericOnKeyDown(event)'></form:input>
						<spring:bind path="duplicatePurgeInterval">
							<elitecoreError:showError errorMessage="${status.errorMessage}" errorId="service-duplicatePurgeInterval_error"></elitecoreError:showError>
						</spring:bind>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
	<!-- Form content end here  -->
</div>
