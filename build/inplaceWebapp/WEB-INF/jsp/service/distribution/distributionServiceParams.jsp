<%@ taglib uri="http://www.springframework.org/security/tags"	prefix="sec"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="/WEB-INF/ShowErrorTag.tld" prefix="elitecoreError"%>

<script type="text/javascript">

$(document).ready(function() {
	var enabled = $("#fileMergeEnabled option:selected").text();
	handleMergeParams(enabled,'remainingFileMergeEnabled','fileMergeGroupingBy');
});
function handleMergeParams(value, remainingFileMergeEnabledId,fileMergeGroupingById){
	if(value == 'true' || value == 'True'){
		$("#"+remainingFileMergeEnabledId).prop('disabled', false);
		$("#"+fileMergeGroupingById).prop('disabled', false);
	}else{
		$("#"+remainingFileMergeEnabledId).prop('disabled', true);
		$("#"+fileMergeGroupingById).prop('disabled', true);
	}
}
</script>

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
	
	<div class="box-body">
		<div class="fullwidth inline-form">

			<div class="col-md-6 no-padding">
				<spring:message code="distribution.service.operation.third.party.transfer.label"	var="label" ></spring:message>
				<spring:message	code="distribution.service.operation.third.party.transfer.label.tooltip" var="tooltip" ></spring:message>
				<div class="form-group">
					<div class="table-cell-label">${label}
					</div>
					<div class="input-group ">
						<form:select path="thirdPartyTransferEnabled"
							cssClass="form-control table-cell input-sm" tabindex="12"
							id="thirdPartyTransferEnabled" data-toggle="tooltip"
							data-placement="bottom" title="${tooltip }">
							<c:forEach var="trueFalseEnum" items="${trueFalseEnum}">
								<form:option value="${trueFalseEnum.name}">${trueFalseEnum}</form:option>
							</c:forEach>
						</form:select>
						<spring:bind path="thirdPartyTransferEnabled">
							<elitecoreError:showError errorMessage="${status.errorMessage}" errorId="thirdPartyTransferEnabled_error"></elitecoreError:showError>
						</spring:bind>
					</div>
				</div>
			</div>

			<div class="col-md-6 no-padding">
				<spring:message code="distribution.service.operation.file.merge.label" var="label" ></spring:message>
				<spring:message	code="distribution.service.operation.file.merge.label.tooltip" var="tooltip" ></spring:message>
				<div class="form-group">
					<div class="table-cell-label">${label}
					</div>
					<div class="input-group ">
						<form:select path="fileMergeEnabled"  onchange="handleMergeParams(this.options[this.selectedIndex].value, 'remainingFileMergeEnabled', 'fileMergeGroupingBy')"
							cssClass="form-control table-cell input-sm" tabindex="13"
							id="fileMergeEnabled" data-toggle="tooltip"
							data-placement="bottom" title="${tooltip }">
							<c:forEach var="trueFalseEnum" items="${trueFalseEnum}">
								<form:option value="${trueFalseEnum.name}">${trueFalseEnum}</form:option>
							</c:forEach>
						</form:select>
						<spring:bind path="fileMergeEnabled">
							<elitecoreError:showError errorMessage="${status.errorMessage}" errorId="fileMergeEnabled_error"></elitecoreError:showError>
						</spring:bind>
					</div>
				</div>
			</div>

			<div class="col-md-6 no-padding">
				<spring:message code="distribution.service.operation.file.merge.grouping.by.label" var="label" ></spring:message>
				<spring:message	code="distribution.service.operation.file.merge.grouping.by.label.tooltip" var="tooltip" ></spring:message>
				<div class="form-group">
					<div class="table-cell-label">${label}
					</div>
					<div class="input-group ">
						<form:select path="fileMergeGroupingBy"
							cssClass="form-control table-cell input-sm" tabindex="13"
							id="fileMergeGroupingBy" data-toggle="tooltip"
							data-placement="bottom" title="${tooltip }">
							<c:forEach var="fileMergeGroupingByEnum" items="${fileMergeGroupingByEnum}">
								<form:option value="${fileMergeGroupingByEnum.value}">${fileMergeGroupingByEnum}</form:option>
							</c:forEach>
						</form:select>
						<spring:bind path="fileMergeGroupingBy">
							<elitecoreError:showError errorMessage="${status.errorMessage}" errorId="fileMergeGroupingBy_error"></elitecoreError:showError>
						</spring:bind>
					</div>
				</div>
			</div>

			<div class="col-md-6 no-padding">
				<spring:message code="distribution.service.operation.remaining.file.merge.label" var="label" ></spring:message>
				<spring:message	code="distribution.service.operation.remaining.file.merge.label.tooltip" var="tooltip" ></spring:message>
				<div class="form-group">
					<div class="table-cell-label">${label}
					</div>
					<div class="input-group ">
						<form:select path="remainingFileMergeEnabled" 
							cssClass="form-control table-cell input-sm" tabindex="13"
							id="remainingFileMergeEnabled" data-toggle="tooltip"
							data-placement="bottom" title="${tooltip }">
							<c:forEach var="trueFalseEnum" items="${trueFalseEnum}">
								<form:option value="${trueFalseEnum.name}">${trueFalseEnum}</form:option>
							</c:forEach>
						</form:select>
						<spring:bind path="remainingFileMergeEnabled">
							<elitecoreError:showError errorMessage="${status.errorMessage}" errorId="remainingFileMergeEnabled_error"></elitecoreError:showError>
						</spring:bind>
					</div>
				</div>
			</div>

			<div class="col-md-6 no-padding">
				<spring:message code="distribution.service.operation.process.record.limit.label" var="label" ></spring:message>
				<spring:message code="distribution.service.operation.process.record.limit.label.tooltip" var="tooltip" ></spring:message>
				
				<div class="form-group">
					<div class="table-cell-label">${label}<span class="required">*</span></div>
					<div class="input-group ">
						<form:input path="processRecordLimit"
							cssClass="form-control table-cell input-sm" tabindex="14"
							id="processRecordLimit" data-toggle="tooltip"
							data-placement="bottom" title="${tooltip }"  onkeydown="isNumericOnKeyDown(event)"></form:input>
						<spring:bind path="processRecordLimit">
							<elitecoreError:showError errorMessage="${status.errorMessage}" errorId="processRecordLimit_error"></elitecoreError:showError>
						</spring:bind>
					</div>
				</div>
			</div>

			<div class="col-md-6 no-padding">
				<spring:message code="distribution.service.operation.timesten.datasource.label" var="label" ></spring:message>
				<spring:message code="distribution.service.operation.timesten.datasource.label.tooltip"	var="tooltip" ></spring:message>
				<div class="form-group">
					<div class="table-cell-label">${label}</div>
					<div class="input-group ">
						<form:input path="timestenDatasourceName"
							cssClass="form-control table-cell input-sm" tabindex="15"
							id="timestenDatasourceName" data-toggle="tooltip"
							data-placement="bottom" title="${tooltip }" ></form:input>
						<spring:bind path="timestenDatasourceName">
							<elitecoreError:showError errorMessage="${status.errorMessage}" errorId="timestenDatasourceName_error"></elitecoreError:showError>
						</spring:bind>
					</div>
				</div>
			</div>


			<div class="col-md-6 no-padding">
				<spring:message code="distribution.service.operation.write.record.limit.label" var="label" ></spring:message>
				<spring:message code="distribution.service.operation.write.record.limit.label.tooltip"	var="tooltip" ></spring:message>
				<div class="form-group">
					<div class="table-cell-label">${label}</div>
					<div class="input-group ">
						<form:input path="writeRecordLimit"
							cssClass="form-control table-cell input-sm" tabindex="16"
							id="writeRecordLimit" data-toggle="tooltip"
							data-placement="bottom" title="${tooltip }"  onkeydown="isNumericOnKeyDown(event)"></form:input>
						<spring:bind path="writeRecordLimit">
							<elitecoreError:showError errorMessage="${status.errorMessage}" errorId="writeRecordLimit_error"></elitecoreError:showError>
						</spring:bind>
					</div>
				</div>
			</div>
			
			<div class="col-md-6 no-padding">
				<spring:message code="parsingService.configuration.file.error.path.label" var="label" ></spring:message>
				<spring:message code="parsingService.configuration.file.error.path.label" var="tooltip" ></spring:message>
				<div class="form-group">
					<div class="table-cell-label">${label}</div>
					<div class="input-group ">
						<form:input path="errorPath"
							cssClass="form-control table-cell input-sm"
							id="parsingService-errorPath" data-toggle="tooltip"
							data-placement="bottom" title="${tooltip }" ></form:input>
						<spring:bind path="errorPath">
							<elitecoreError:showError errorMessage="${status.errorMessage}" errorId="parsingService-errorPath_error"></elitecoreError:showError>
						</spring:bind>
					</div>
				</div>
			</div>

		</div>
	</div>
</div>
