<%@page import="com.elitecore.sm.common.constants.BaseConstants"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="/WEB-INF/ShowErrorTag.tld" prefix="elitecoreError"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<style>
.left{float:left}
.working-thread-left{width: 67%; float:left;}
</style>
<div class="box box-warning">
	<div class="box-header with-border">
		<h3 class="box-title">
			<spring:message
				code="ascii.plugin.advance.details.heading.label" ></spring:message>
		</h3>
		<div class="box-tools pull-right">
			<button class="btn btn-box-tool" data-widget="collapse">
				<i class="fa fa-minus"></i>
			</button>
		</div>
		<!-- /.box-tools -->
	</div>
	<!-- /.box-header -->
	<div class="box-body inline-form ">
		<div class="col-md-6 no-padding">
			<spring:message code="var.length.binary.plugin.advance.details.skip.file.header.label" var="label" ></spring:message>
			<spring:message code="var.length.binary.plugin.advance.details.skip.file.header.label.tooltip" var="tooltip" ></spring:message>
			<div class="form-group">
				<div class="table-cell-label">${label}</div>
				<div class="input-group ">
					<form:select path="skipFileHeader" cssClass="form-control table-cell input-sm" tabindex="4"
										id="skipFileHeader"
										data-toggle="tooltip" data-placement="bottom"
										 title="${tooltip }">
										 
					<c:forEach var="trueFalseEnum" items="${trueFalseEnum}">
						<form:option value="${trueFalseEnum.name}">${trueFalseEnum}</form:option>
					</c:forEach>
										
					</form:select>
					<spring:bind path="skipFileHeader">
						<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
					</spring:bind>
				</div>
			</div>
		</div>
		
		<div class="col-md-6 no-padding" id="fileHeaderSizeDiv">
			<spring:message code="var.length.binary.plugin.advance.details.file.header.size.label" var="label" ></spring:message>
			<spring:message code="var.length.binary.plugin.advance.details.file.header.size.label.tooltip" var="tooltip" ></spring:message>
			<div class="form-group">
				<div class="table-cell-label">${label}</div>
				<div class="input-group ">
					
					<form:input path="fileHeaderSize"
										cssClass="form-control table-cell input-sm" tabindex="4"
										id="fileHeaderSize" data-toggle="tooltip"
										data-placement="bottom" 
										title="${tooltip }" onkeydown='isNumericOnKeyDown(event)' ></form:input>
					<spring:bind path="fileHeaderSize">
						<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
					</spring:bind>
				</div>
			</div>
		</div>
		
		<div class="col-md-6 no-padding">
			<spring:message code="var.length.binary.plugin.advance.details.skip.subfile.header.label" var="label" ></spring:message>
			<spring:message code="var.length.binary.plugin.advance.details.skip.subfile.header.label.tooltip" var="tooltip" ></spring:message>
			<div class="form-group">
				<div class="table-cell-label">${label}</div>
				<div class="input-group ">
					<form:select path="skipSubFileHeader" cssClass="form-control table-cell input-sm" tabindex="4"
										id="skipSubFileHeader"
										data-toggle="tooltip" data-placement="bottom"
										 title="${tooltip }">
										 
					<c:forEach var="trueFalseEnum" items="${trueFalseEnum}">
						<form:option value="${trueFalseEnum.name}">${trueFalseEnum}</form:option>
					</c:forEach>
										
					</form:select>
					<spring:bind path="skipSubFileHeader">
						<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
					</spring:bind>
				</div>
			</div>
		</div>
		
		<div class="col-md-6 no-padding" id="subFileHeaderSizeDiv">
			<spring:message code="var.length.binary.plugin.advance.details.subfile.header.size.label" var="label" ></spring:message>
			<spring:message code="var.length.binary.plugin.advance.details.subfile.header.size.label.tooltip" var="tooltip" ></spring:message>
			<div class="form-group">
				<div class="table-cell-label">${label}</div>
				<div class="input-group ">
					
					<form:input path="subFileHeaderSize"
										cssClass="form-control table-cell input-sm" tabindex="4"
										id="subFileHeaderSize" data-toggle="tooltip"
										data-placement="bottom" 
										title="${tooltip }" onkeydown='isNumericOnKeyDown(event)' ></form:input>
					<spring:bind path="subFileHeaderSize">
						<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
					</spring:bind>
				</div>
			</div>
		</div>
		
		<div class="col-md-6 no-padding" id="subFileLengthDiv">
			<spring:message code="var.length.binary.plugin.advance.details.subfile.length.label" var="label" ></spring:message>
			<spring:message code="var.length.binary.plugin.advance.details.subfile.length.label.tooltip" var="tooltip" ></spring:message>
			<div class="form-group">
				<div class="table-cell-label">${label}</div>
				<div class="input-group ">
					
					<form:input path="subFileLength"
										cssClass="form-control table-cell input-sm" tabindex="4"
										id="subFileLength" data-toggle="tooltip"
										data-placement="bottom" 
										title="${tooltip }" onkeydown='isNumericOnKeyDown(event)' ></form:input>
					<spring:bind path="subFileLength">
						<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
					</spring:bind>
				</div>
			</div>
		</div>
		
		<div class="col-md-6 no-padding" id="extractionRuleKeyDiv">
			<spring:message code="var.length.binary.plugin.advance.details.extraction.rule.key.label" var="label" ></spring:message>
			<spring:message code="var.length.binary.plugin.advance.details.extraction.rule.key.label.tooltip" var="tooltip" ></spring:message>
			<div class="form-group">
				<div class="table-cell-label">${label}</div>
				<div class="input-group ">
					
					<form:input path="extractionRuleKey"
										cssClass="form-control table-cell input-sm" tabindex="4"
										id="extractionRuleKey" data-toggle="tooltip"
										data-placement="bottom" 
										title="${tooltip }"></form:input>
					<spring:bind path="extractionRuleKey">
						<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
					</spring:bind>
				</div>
			</div>
		</div>
		
		<div class="col-md-6 no-padding" id="extractionRuleValueDiv">
			<spring:message code="var.length.binary.plugin.advance.details.extraction.rule.value.label" var="label" ></spring:message>
			<spring:message code="var.length.binary.plugin.advance.details.extraction.rule.value.label.tooltip" var="tooltip" ></spring:message>
			<div class="form-group">
				<div class="table-cell-label">${label}</div>
				<div class="input-group ">
					
					<form:input path="extractionRuleValue"
										cssClass="form-control table-cell input-sm" tabindex="4"
										id="extractionRuleValue" data-toggle="tooltip"
										data-placement="bottom" 
										title="${tooltip }" ></form:input>
					<spring:bind path="extractionRuleValue">
						<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
					</spring:bind>
				</div>
			</div>
		</div>
		
		<div class="col-md-6 no-padding" id="recordLengthAttributeDiv">
			<spring:message code="var.length.binary.plugin.advance.details.record.length.attribute.label" var="label" ></spring:message>
			<spring:message code="var.length.binary.plugin.advance.details.record.length.attribute.label.tooltip" var="tooltip" ></spring:message>
			<div class="form-group">
				<div class="table-cell-label">${label}</div>
				<div class="input-group ">
					
					<form:input path="recordLengthAttribute"
										cssClass="form-control table-cell input-sm" tabindex="4"
										id="recordLengthAttribute" data-toggle="tooltip"
										data-placement="bottom" 
										title="${tooltip }" ></form:input>
					<spring:bind path="recordLengthAttribute">
						<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
					</spring:bind>
				</div>
			</div>
		</div>

	</div>
	<!-- /.box-body -->
</div>
