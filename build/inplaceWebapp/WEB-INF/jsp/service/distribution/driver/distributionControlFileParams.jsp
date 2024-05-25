<%@taglib uri="/WEB-INF/ShowErrorTag.tld" prefix="elitecoreError"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<script type="text/javascript" src="${pageContext.request.contextPath}/js/bootstrap-multiselect.js"></script>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap-multiselect.css" type="text/css"/>

<style>
 .customLink:hover{
 	color:white;!important
 }
 .checkbox{
	color: #000 !important;
 }
 .btn .caret {
    margin-left: -10px;
 }
 .btn-group{
	width: 100% !important;
 }
.btn-group button{
	width: 100% !important;
 }
 .btn-default span{
 	width: 100% !important;
	background:none !important ;
 }
.dropdown-menu {
	width: 100% !important;
}

.fa {
	display: inline; !important
}

</style>

<div class="fullwidth mtop15">
	<div class="box box-warning">
		<div class="box-header with-border">
			<h3 class="box-title"> <spring:message code="ftp.driver.mgmt.config.control.file.generation.param.header" ></spring:message> </h3>
			<div class="box-tools pull-right">
				<button class="btn btn-box-tool" data-widget="collapse">
					<i class="fa fa-minus"></i>
				</button>
			</div>
		</div>

		<div class="box-body inline-form ">
		
			<div class="col-md-6 no-padding" style="height: 45px">
				<spring:message code="ftp.driver.mgmt.config.control.file.enabled" var="label" ></spring:message> 
				<spring:message code="ftp.driver.mgmt.config.control.file.enabled.tooltip" var="tooltip" ></spring:message>
				<form:hidden path="controlFileSeq.id" id="controlFileSeqId" ></form:hidden>
				<div class="form-group">
					<div class="table-cell-label">${label}</div>
					<div class="input-group ">
						<form:select path="driverControlFileParams.controlFileEnabled" cssClass="form-control table-cell input-sm" id="ftp-controlFileEnabled" tabindex="9" data-toggle="tooltip" data-placement="top"  title="${tooltip }" onchange="ChangecontrolFileParam();">
	                        <c:forEach var="trueFalseEnum" items="${trueFalseEnum}">
                            	<form:option value="${trueFalseEnum.name}">${trueFalseEnum}</form:option>
                            </c:forEach>
						</form:select>
						<spring:bind path="driverControlFileParams.controlFileEnabled">
							<elitecoreError:showError errorMessage="${status.errorMessage}" errorId="ftp-controlFileEnabled_error"></elitecoreError:showError>
						</spring:bind>		
					</div>
				</div>
			</div>
			
			<div class="col-md-6 no-padding" style="height: 45px">
				<spring:message code="ftp.driver.mgmt.config.control.file.location" var="label" ></spring:message> 
				<spring:message code="ftp.driver.mgmt.config.control.file.location.tooltip" var="tooltip" ></spring:message>

				<div class="form-group">
					<div class="table-cell-label">${label}<span class="required">*</span></div>
					<div class="input-group ">
						<form:input path="driverControlFileParams.controlFileLocation" cssClass="form-control table-cell input-sm"
								id="ftp-controlFileLocation" tabindex="9" data-toggle="tooltip" data-placement="bottom" 
								title="${tooltip}" ></form:input>
						<spring:bind path="driverControlFileParams.controlFileLocation">
							<elitecoreError:showError errorMessage="${status.errorMessage}" errorId="ftp-controlFileLocation_error"></elitecoreError:showError>
						</spring:bind>
					</div>
				</div>
			</div>
			
			<div class="col-md-6 no-padding" style="height: 45px">
				<spring:message code="ftp.driver.mgmt.config.control.file.attributes" var="label" ></spring:message> 
				<spring:message code="ftp.driver.mgmt.config.control.file.attributes.tooltip" var="tooltip" ></spring:message>

				<div class="form-group">
					<div class="table-cell-label">${label}<span class="required">*</span></div>
					<div class="input-group "> 
						<select tabindex='9' class='form-control table-cell input-sm' id='ftp-attributes_dropdown' 
								data-toggle='tooltip' data-placement='bottom' title='${tooltip}' multiple='multiple'>
							<c:forEach var="attributesEnumVar" items="${attributesEnum}">
								<option value='${attributesEnumVar.getValue()}'>${attributesEnumVar}</option>
                            </c:forEach>
						</select>
						<form:hidden path="driverControlFileParams.attributes" id="ftp-attributes" ></form:hidden>
						<spring:bind path="driverControlFileParams.attributes">
							<elitecoreError:showError errorMessage="${status.errorMessage}" errorId="ftp-attributes_error"></elitecoreError:showError>
						</spring:bind>
					</div>
				</div>
			</div>
			
			<div class="col-md-6 no-padding" style="height: 45px">
				<spring:message code="ftp.driver.mgmt.config.control.file.attribute.separator" var="label" ></spring:message> 
				<spring:message code="ftp.driver.mgmt.config.control.file.attribute.separator.tooltip" var="tooltip" ></spring:message>
				<div class="form-group">
					<div class="table-cell-label">${label}<span class="required">*</span></div>
					<div class="input-group ">
						<select tabindex='9' class='form-control table-cell input-sm' id='ftp-attributesSep_dropdown' 
								data-toggle='tooltip' data-placement='bottom' title='${tooltip}'>
							<c:forEach var="SeparatorEnum" items="${separatorEnum}">
						          <option value="${SeparatorEnum.value}">${SeparatorEnum.input}</option>
						   </c:forEach>
						</select>
						<form:hidden path="driverControlFileParams.attributeSeparator" id="ftp-attributeSeparator" ></form:hidden>
						<spring:bind path="driverControlFileParams.attributeSeparator">
							<elitecoreError:showError errorMessage="${status.errorMessage}" errorId="ftp-attributeSeparator_error"></elitecoreError:showError>
						</spring:bind>		
					</div>
				</div>
			</div>
			
			<div class="col-md-6 no-padding" style="height: 45px">
				<spring:message code="ftp.driver.mgmt.config.control.file.rolling.duration" var="label" ></spring:message> 
				<spring:message code="ftp.driver.mgmt.config.control.file.rolling.duration.tooltip" var="tooltip" ></spring:message>

				<div class="form-group">
					<div class="table-cell-label">${label}<span class="required">*</span></div>
					<div class="input-group ">
						<form:input path="driverControlFileParams.fileRollingDuration" cssClass="form-control table-cell input-sm"
								id="ftp-fileRollingDuration" tabindex="9" data-toggle="tooltip" data-placement="bottom" 
								title="${tooltip}" onkeydown="isNumericOnKeyDown(event)" ></form:input>
						<spring:bind path="driverControlFileParams.fileRollingDuration">
							<elitecoreError:showError errorMessage="${status.errorMessage}" errorId="ftp-fileRollingDuration_error"></elitecoreError:showError>
						</spring:bind>
					</div>
				</div>
			</div>
			
			<div class="col-md-6 no-padding" style="height: 45px">
				<spring:message code="ftp.driver.mgmt.config.control.file.rolling.start.time" var="label" ></spring:message> 
				<spring:message code="ftp.driver.mgmt.config.control.file.rolling.start.time.tooltip" var="tooltip" ></spring:message>

				<div class="form-group">
					<div class="table-cell-label">${label}<span class="required">*</span></div>
					<div class="input-group ">
						<form:input path="driverControlFileParams.fileRollingStartTime" cssClass="form-control table-cell input-sm"
								id="ftp-fileRollingStartTime" tabindex="9" data-toggle="tooltip" data-placement="bottom" 
								title="${tooltip}" placeholder="HH:mm" maxlength="5"></form:input>
						<spring:bind path="driverControlFileParams.fileRollingStartTime">
							<elitecoreError:showError errorMessage="${status.errorMessage}" errorId="ftp-fileRollingStartTime_error"></elitecoreError:showError>
						</spring:bind>
					</div>
				</div>
			</div>
			
			<div class="col-md-6 no-padding" style="height: 45px">
				<spring:message code="ftp.driver.mgmt.config.control.file.name.convention" var="label" ></spring:message> 
				<spring:message code="ftp.driver.mgmt.config.control.file.name.convention.tooltip" var="tooltip" ></spring:message>

				<div class="form-group">
					<div class="table-cell-label">${label}<span class="required">*</span></div>
					<div class="input-group ">
						<form:input path="driverControlFileParams.controlFileName" cssClass="form-control table-cell input-sm"
								id="ftp-controlFileName" tabindex="9" data-toggle="tooltip" data-placement="bottom" 
								title="${tooltip}" ></form:input>
						<spring:bind path="driverControlFileParams.controlFileName">
							<elitecoreError:showError errorMessage="${status.errorMessage}" errorId="ftp-controlFileName_error"></elitecoreError:showError>
						</spring:bind>
					</div>
				</div>
			</div>
			
			<div class="col-md-6 no-padding" style="height: 45px">
				<spring:message code="ftp.driver.mgmt.config.control.file.enable.seq" var="label" ></spring:message> 
				<spring:message code="ftp.driver.mgmt.config.control.file.enable.seq.tooltip" var="tooltip" ></spring:message>

				<div class="form-group">
					<div class="table-cell-label">${label}</div>
					<div class="input-group ">
						<form:select path="driverControlFileParams.fileSeqEnable" cssClass="form-control table-cell input-sm" id="ftp-fileSeqEnable" tabindex="9" data-toggle="tooltip" data-placement="top"  title="${tooltip }" onchange="changeFileSequenceParams();">
	                        <c:forEach var="trueFalseEnum" items="${trueFalseEnum}">
                            	<form:option value="${trueFalseEnum.name}">${trueFalseEnum}</form:option>
                            </c:forEach>
						</form:select>
						<spring:bind path="driverControlFileParams.fileSeqEnable">
							<elitecoreError:showError errorMessage="${status.errorMessage}" errorId="ftp-fileSeqEnable_error"></elitecoreError:showError>
						</spring:bind>		
					</div>
				</div>
			</div>
			
			<div class="col-md-6 no-padding" style="height: 45px">
				<spring:message code="ftp.driver.mgmt.config.control.file.reset.frequency" var="label" ></spring:message> 
				<spring:message code="ftp.driver.mgmt.config.control.file.reset.frequency.tooltip" var="tooltip" ></spring:message>

				<div class="form-group">
					<div class="table-cell-label">${label}</div>
					<div class="input-group ">
						<form:select path="controlFileSeq.resetFrequency" cssClass="form-control table-cell input-sm" id="controlFileSeq-resetFrequency" tabindex="9" data-toggle="tooltip" data-placement="top"  title="${tooltip }">
	                        <c:forEach var="resetFreqEnum" items="${resetFreqEnum}">
                            	<form:option value="${resetFreqEnum.value}">${resetFreqEnum}</form:option>
                            </c:forEach>
						</form:select>
						<spring:bind path="controlFileSeq.resetFrequency">
							<elitecoreError:showError errorMessage="${status.errorMessage}" errorId="controlFileSeq-resetFrequency_error"></elitecoreError:showError>
						</spring:bind>		
					</div>
				</div>
			</div>
			
			<div class="col-md-6 no-padding">
				<spring:message code="ftp.driver.mgmt.config.control.file.seq.range" var="tooltip" ></spring:message>
				<spring:message code="ftp.driver.mgmt.config.control.file.seq.range.min.tooltip" var="minRange" ></spring:message>
				<spring:message code="ftp.driver.mgmt.config.control.file.seq.range.max.tooltip" var="maxRange" ></spring:message>

				<div class="form-group">
					<div class="table-cell-label">${tooltip}<span class="required">*</span></div>
					<div class="input-group ">
						<form:input path="controlFileSeq.startRange" cssClass="form-control table-cell input-sm" id="controlFileSeq-startRange" tabindex="9" onkeydown="isNumericOnKeyDown(event)" data-toggle="tooltip" data-placement="bottom" title="${minRange}"></form:input>
						<spring:bind path="controlFileSeq.startRange">
							<elitecoreError:showError errorMessage="${status.errorMessage}" errorId="controlFileSeq-startRange_error"></elitecoreError:showError>
						</spring:bind> 	
						<form:input path="controlFileSeq.endRange" cssClass="form-control table-cell input-sm" id="controlFileSeq-endRange" tabindex="9" onkeydown="isNumericOnKeyDown(event)" data-toggle="tooltip" data-placement="bottom" title="${maxRange}"></form:input>
						<spring:bind path="controlFileSeq.endRange">
							<elitecoreError:showError errorMessage="${status.errorMessage}" errorId="controlFileSeq-endRange_error"></elitecoreError:showError>
						</spring:bind> 	
					</div>
				</div>
			</div>
			
			<div class="col-md-6 no-padding" style="height: 45px">
				<spring:message code="ftp.driver.mgmt.config.control.file.file.seq.padding" var="label" ></spring:message> 
				<spring:message code="ftp.driver.mgmt.config.control.file.file.seq.padding.tooltip" var="tooltip" ></spring:message>

				<div class="form-group">
					<div class="table-cell-label">${label}</div>
					<div class="input-group ">
						<form:select path="controlFileSeq.paddingEnable" cssClass="form-control table-cell input-sm" id="controlFileSeq-paddingEnable" tabindex="9" data-toggle="tooltip" data-placement="top"  title="${tooltip }">
	                        <c:forEach var="trueFalseEnum" items="${trueFalseEnum}">
                            	<form:option value="${trueFalseEnum.name}">${trueFalseEnum}</form:option>
                            </c:forEach>
						</form:select>
						<spring:bind path="controlFileSeq.paddingEnable">
							<elitecoreError:showError errorMessage="${status.errorMessage}" errorId="controlFileSeq-paddingEnable_error"></elitecoreError:showError>
						</spring:bind>		
					</div>
				</div>
			</div>

			<div style="font-size:10px;">
				<i class="fa fa-square" style="font-size: 9px"></i>
					&nbsp;&nbsp;<spring:message code="ftp.driver.mgmt.config.control.file.sync.not.required" ></spring:message>
			</div>
		</div>
	</div>
</div>
