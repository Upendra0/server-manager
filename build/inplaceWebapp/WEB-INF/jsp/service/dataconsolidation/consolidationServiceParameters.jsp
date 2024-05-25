<%@taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="/WEB-INF/ShowErrorTag.tld" prefix="elitecoreError"%>

<style>
.left {
	float: left
}

.working-thread-left {
	width: 67%;
	float: left;
}
</style>

<!-- section-2 start here -->
<div class="box box-warning">
	<div class="box-header with-border">
		<h3 class="box-title">
			<spring:message code="service.operational.param.section.heading" ></spring:message>
		</h3>
		<div class="box-tools pull-right" style="top: -3px;">
			<button class="btn btn-box-tool" data-widget="collapse">
				<i class="fa fa-minus"></i>
			</button>
		</div>
		<!-- /.box-tools -->
	</div>
	<!-- /.box-header end here -->
	<!-- Form content start here  -->
	<div class="box-body">
		<div class="fullwidth inline-form">
			
			<div class="col-md-6 no-padding">
				<spring:message code="consolidation.service.config.min.file.range" var="minRange" ></spring:message>
				<spring:message code="consolidation.service.config.max.file.range" var="maxRange" ></spring:message>
				<spring:message code="consolidation.service.config.consolidation.detail.filerange" var="label" ></spring:message>
				<spring:message code="consolidation.service.config.consolidation.detail.filerange.tooltip" var="tooltip" ></spring:message>

				<div class="form-group">
					<div class="table-cell-label">${label}</div>
					<div class="input-group ">
						<form:input path="minFileRange" id="con-service-minFileRange"
							cssClass="form-control table-cell input-sm" tabindex="4"
							data-toggle="tooltip" data-placement="bottom" title="${minRange}"
							onkeydown='isNumericOnKeyDown(event)' ></form:input>
						<spring:bind path="minFileRange">
							<elitecoreError:showError errorMessage="${status.errorMessage}" errorId="con-service-minFileRange_error"></elitecoreError:showError>
						</spring:bind>
						<form:input path="maxFileRange" id="con-service-maxFileRange"
							cssClass="form-control table-cell input-sm" tabindex="4"
							data-toggle="tooltip" data-placement="bottom" title="${maxRange}"
							onkeydown='isNumericOnKeyDown(event)' ></form:input>
						<spring:bind path="maxFileRange">
							<elitecoreError:showError errorMessage="${status.errorMessage}" errorId="con-service-maxFileRange_error"></elitecoreError:showError>
						</spring:bind>
					</div>
				</div>
			</div>

			<div class="col-md-6 no-padding">
				<spring:message
					code="consolidation.service.config.consolidation.detail.no.file.alert.interval"
					var="label" ></spring:message>
				<spring:message
					code="consolidation.service.config.consolidation.detail.no.file.alert.interval.tooltip"
					var="tooltip" ></spring:message>

				<div class="form-group">
					<div class="table-cell-label">${label}</div>
					<div class="input-group ">
						<form:input path="noFileAlertInterval" cssClass="form-control table-cell input-sm"
							tabindex="4" id="noFileAlertInterval"
							data-toggle="tooltip" data-placement="bottom" title="${tooltip}"
							size="9" onkeydown='isNumericOnKeyDown(event) ' ></form:input>
						<spring:bind path="noFileAlertInterval">
							<elitecoreError:showError errorMessage="${status.errorMessage}" errorId="noFileAlertInterval_error"></elitecoreError:showError>
						</spring:bind>
					</div>
				</div>
			</div>
			<div class="col-md-6 no-padding">
				<spring:message
					code="consolidation.service.config.consolidation.detail.operation.processing.type.label"
					var="label" ></spring:message>
				<spring:message
					code="consolidation.service.config.consolidation.detail.operation.processing.type.label.tooltip"
					var="tooltip" ></spring:message>
				<div class="form-group">
					<div class="table-cell-label">${label}<span class="required">*</span>
					</div>
					<div class="input-group ">
						<form:select path="processingType"
							cssClass="form-control table-cell input-sm" tabindex="4"
							id="processingType" data-toggle="tooltip"
							data-placement="bottom" title="${tooltip }">
							<c:forEach items="${processingType}" var="processingType">
								<form:option value="${processingType.value}">${processingType.name}</form:option>
							</c:forEach>
						</form:select>
						<spring:bind path="processingType">
							<elitecoreError:showError errorMessage="${status.errorMessage}" errorId="svcExecParams-startupMode_error"></elitecoreError:showError>
						</spring:bind>
					</div>
				</div>
			</div>

			<div class="col-md-6 no-padding">
				<spring:message
					code="consolidation.service.config.consolidation.detail.merge.delimiter"
					var="label" ></spring:message>
				<spring:message
					code="consolidation.service.config.consolidation.detail.merge.delimiter.tooltip"
					var="tooltip" ></spring:message>

				<div class="form-group">
					<div class="table-cell-label">${label}</div>
					<div class="input-group ">
						<form:input path="mergeDelimiter" cssClass="form-control table-cell input-sm"
							tabindex="4" id="mergeDelimiter"
							data-toggle="tooltip" data-placement="bottom" title="${tooltip }"
							size="9" ></form:input>
						<spring:bind path="mergeDelimiter">
							<elitecoreError:showError errorMessage="${status.errorMessage}" errorId="svcExecParams-fileBatchSize_error"></elitecoreError:showError>
						</spring:bind>
					</div>
				</div>
			</div>
		</div>
	</div>
	<!-- Form content end here  -->
</div>
<!-- section-2 end here -->
