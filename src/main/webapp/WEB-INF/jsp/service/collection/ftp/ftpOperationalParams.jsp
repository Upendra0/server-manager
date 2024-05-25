<%@taglib uri="/WEB-INF/ShowErrorTag.tld" prefix="elitecoreError"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<div class="col-md-6 no-padding">
	<spring:message
		code="ftp.driver.mgmt.config.oper.param.file.trans.mode" var="label" ></spring:message>
	<spring:message
		code="ftp.driver.mgmt.config.oper.param.file.trans.mode.tooltip" var="tooltip" ></spring:message>
	<div class="form-group">
		<div class="table-cell-label">${label}<span class="required">*</span>
		</div>
		<div class="input-group ">
			<form:select path="ftpConnectionParams.fileTransferMode"
				cssClass="form-control table-cell input-sm"
				id="ftp-ftpConnectionParams-fileSeqOrderEnable" tabindex="6"
				data-toggle="tooltip" data-placement="bottom" title="${tooltip }">
				<c:forEach var="transferModeEnum" items="${transferModeEnum}">
					<form:option value="${transferModeEnum}">${transferModeEnum}</form:option>
				</c:forEach>
			</form:select>
			<spring:bind path="ftpConnectionParams.fileTransferMode">
				<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
			</spring:bind>
		</div>
	</div>
</div>
<div class="col-md-6 no-padding">
	<spring:message code="ftp.driver.mgmt.config.oper.param.ftp.timeout"
		var="label" ></spring:message>
	<spring:message code="ftp.driver.mgmt.config.oper.param.ftp.timeout.tooltip"
		var="tooltip" ></spring:message>
		
	<div class="form-group">
		<div class="table-cell-label">${label}<span class="required">*</span>
		</div>
		<div class="input-group ">
			<form:input path="ftpConnectionParams.timeout"
				cssClass="form-control table-cell input-sm"
				id="ftp-ftpConnectionParams-timeout" tabindex="7"
				data-toggle="tooltip" data-placement="bottom" title="${tooltip }"
				onkeydown="isNumericOnKeyDown(event)" ></form:input>
			<spring:bind path="ftpConnectionParams.timeout">
				<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
			</spring:bind>
		</div>
	</div>
</div>
<div class="col-md-6 no-padding">
	<spring:message
		code="ftp.driver.mgmt.config.oper.param.remote.file.sep" var="label" ></spring:message>
	<spring:message
		code="ftp.driver.mgmt.config.oper.param.remote.file.sep.tooltip" var="tooltip" ></spring:message>
			
	<div class="form-group">
		<div class="table-cell-label">${label}<span class="required">*</span>
		</div>
		<div class="input-group ">
			<form:input path="ftpConnectionParams.fileSeparator"
				cssClass="form-control table-cell input-sm"
				id="ftp-ftpConnectionParams-fileSeparator" tabindex="10"
				data-toggle="tooltip" data-placement="bottom" title="${tooltip }" ></form:input>
			<spring:bind path="ftpConnectionParams.fileSeparator">
				<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
			</spring:bind>
		</div>
	</div>
</div>
