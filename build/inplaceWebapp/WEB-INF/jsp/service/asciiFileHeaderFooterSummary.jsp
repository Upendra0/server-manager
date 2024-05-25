<%@page import="com.elitecore.sm.common.constants.BaseConstants"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="/WEB-INF/ShowErrorTag.tld" prefix="elitecoreError"%>

<div class="col-md-6 no-padding" id="enableFileHeaderSummary" style="display:none">
	<spring:message code="ascii.plugin.advance.details.enable.fileHeader.summary.label" var="label" ></spring:message>
	<spring:message code="ascii.plugin.advance.details.enable.fileHeader.summary.label.tooltip" var="tooltip" ></spring:message>
	<div class="form-group">
		<div class="table-cell-label">${label}</div>
		<div class="input-group ">
			<form:select path="fileHeaderSummaryEnable" cssClass="form-control table-cell input-sm" tabindex="4" id="fileHeaderSummaryEnable"
						 data-toggle="tooltip" data-placement="bottom"
					 	 onChange="enableDisableSummaryFields('fileHeaderSummaryEnable');" title="${tooltip }">
					<c:forEach var="trueFalseEnum" items="${trueFalseEnum}">
						<form:option value="${trueFalseEnum.name}">${trueFalseEnum}</form:option>
					</c:forEach>
					</form:select>
					<spring:bind path="fileHeaderSummaryEnable">
						<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
					</spring:bind>
				</div>
			</div>
		</div>
		
		<div class="col-md-6 no-padding" id="enableFileFooterSummary" style="display:none">
			<spring:message code="ascii.plugin.advance.details.enable.fileFooter.summary.label" var="label" ></spring:message>
			<spring:message code="ascii.plugin.advance.details.enable.fileFooter.summary.label.tooltip" var="tooltip" ></spring:message>
			<div class="form-group">
				<div class="table-cell-label">${label}</div>
				<div class="input-group ">

			<form:select path="fileFooterSummaryEnable"
				cssClass="form-control table-cell input-sm" tabindex="4"
				id="fileFooterSummaryEnable" data-toggle="tooltip"
				data-placement="bottom"
				onChange="enableDisableSummaryFields('fileFooterSummaryEnable');" title="${tooltip }">

				<c:forEach var="trueFalseEnum" items="${trueFalseEnum}">
					<form:option value="${trueFalseEnum.name}">${trueFalseEnum}</form:option>
				</c:forEach>

			</form:select>
			<spring:bind path="fileFooterSummaryEnable">
						<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
					</spring:bind>
				</div>
			</div>
		</div>
		

		<div class="col-md-6 no-padding" id="headerSummaryFunctions" style="display:block;" >
			<spring:message code="ascii.plugin.advance.details.enable.fileHeaderfooter.summary.utility.function.label" var="label" ></spring:message>
			<spring:message code="ascii.plugin.advance.details.enable.fileHeaderfooter.summary.utility.function.tooltip" var="tooltip" ></spring:message>
			<div class="form-group">
				<div class="table-cell-label">${label}</div>
				<div class="input-group " style="width: 92%">
					
					<form:select path="" class="form-control table-cell input-sm" tabindex="4"
										id="fileHeaderSummaryFunction" onchange="appendValueToSummaryArea('fileHeaderSummaryFunction','fileHeaderSummary')"
										data-toggle="tooltip" data-placement="bottom"
										 title="${tooltip }" >
										 
					 <c:forEach var="fileUtilityFunctionEnum" items="${fileUtilityFunctionEnum}">
						<form:option value="${fileUtilityFunctionEnum.value}">${fileUtilityFunctionEnum.name}</form:option>
					</c:forEach>
					</form:select>
				</div>
			</div>
		</div>
		
		<div class="col-md-6 no-padding" id="footerSummaryFunctions" style="display:block;width: " >
			<spring:message code="ascii.plugin.advance.details.enable.fileHeaderfooter.summary.utility.function.label" var="label" ></spring:message>
			<spring:message code="ascii.plugin.advance.details.enable.fileHeaderfooter.summary.utility.function.tooltip" var="tooltip" ></spring:message>
			<div class="form-group">
				<div class="table-cell-label">${label}</div>
				<div class="input-group " style="width: 92%">
					
					<form:select  path="" class="form-control table-cell input-sm" tabindex="4"
										id="fileFooterSummaryFunction" onchange="appendValueToSummaryArea('fileFooterSummaryFunction','fileFooterSummary')"
										data-toggle="tooltip" data-placement="bottom"
										 title="${tooltip }">
										 
					<c:forEach var="fileUtilityFunctionEnum" items="${fileUtilityFunctionEnum}">
						<form:option value="${fileUtilityFunctionEnum.value}">${fileUtilityFunctionEnum.name}</form:option>
					</c:forEach>
					</form:select>
				</div>
			</div>
		</div>
		<div class="col-md-6 no-padding">
				<spring:message code="ascii.plugin.advance.details.textarea.fileHeader.summary.label" var="label" ></spring:message>
				<spring:message code="ascii.plugin.advance.details.textarea.fileHeader.summary.label.tooltip" var="tooltip" ></spring:message>
			<div class="form-group">
				<div class="table-cell-label" style="vertical-align: top;">${label}</div>
				<div class="input-group">
					<form:textarea path="fileHeaderSummary" cssClass="form-control input-sm" id="fileHeaderSummary" data-toggle="tooltip" rows="4" data-placement="bottom" maxlength="4000" title="${tooltip}"></form:textarea>
					<spring:bind path="fileHeaderSummary">
						<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
					</spring:bind>
				</div>
			</div>
		</div>
		
		<div class="col-md-6 no-padding">
				<spring:message code="ascii.plugin.advance.details.textarea.fileFooter.summary.label" var="label" ></spring:message>
				<spring:message code="ascii.plugin.advance.details.textarea.fileFooter.summary.label.tooltip" var="tooltip" ></spring:message>
			<div class="form-group">
				<div class="table-cell-label" style="vertical-align: top;">${label}</div>
				<div class="input-group">
				    <form:textarea path="fileFooterSummary" cssClass="form-control input-sm" id="fileFooterSummary" data-toggle="tooltip" rows="4" data-placement="bottom" maxlength="4000" title="${tooltip}"></form:textarea>
				    <spring:bind path="fileFooterSummary">
						<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
					</spring:bind>
				</div>
			</div>
		</div>
		
