<%@page import="com.elitecore.sm.common.constants.BaseConstants"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="/WEB-INF/ShowErrorTag.tld" prefix="elitecoreError"%>

<div class="col-md-6 no-padding" id="enableFileHeader" style="display:none">
			<spring:message code="ascii.plugin.advance.details.enable.fileHeader.label" var="label" ></spring:message>
			<spring:message code="ascii.plugin.advance.details.enable.fileHeader.label.tooltip" var="tooltip" ></spring:message>
			<div class="form-group">
				<div class="table-cell-label">${label}</div>
				<div class="input-group ">
					
					<form:select path="fileHeaderEnable" cssClass="form-control table-cell input-sm" tabindex="4"
										id="fileHeaderEnable"
										data-toggle="tooltip" data-placement="bottom"
										 title="${tooltip }" onChange="changeFileHeaderParam();">
										 
					<c:forEach var="trueFalseEnum" items="${trueFalseEnum}">
						<form:option value="${trueFalseEnum.name}">${trueFalseEnum}</form:option>
					</c:forEach>
										
					</form:select>
					<spring:bind path="fileHeaderEnable">
						<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
					</spring:bind>
				</div>
			</div>
		</div>
		

		<div class="col-md-6 no-padding" id="fieldSep" style="display:none">
			<spring:message code="ascii.plugin.advance.details.field.separator.label" var="label" ></spring:message>
			<spring:message code="ascii.plugin.advance.details.field.separator.label.tooltip" var="tooltip" ></spring:message>
			<div class="form-group">
				<div class="table-cell-label">${label}<span class="required">*</span></div>
				<div class="input-group ">
				<form:select path="fieldSeparatorEnum" cssClass="form-control table-cell input-sm" tabindex="4"
										id="fieldSepValues"
										data-toggle="tooltip" data-placement="bottom"
										 title="${tooltip }">
										 
					<c:forEach var="SeparatorEnum" items="${SeparatorEnum}">
	                   <form:option value="${SeparatorEnum.value}">${SeparatorEnum.input}</form:option>
	                </c:forEach>
										
					</form:select>
					<spring:bind path="fieldSeparatorEnum">
						<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
					</spring:bind>
					
					<form:input path="fieldSeparator"
										cssClass="form-control table-cell input-sm" tabindex="4"
										id="fieldSeparator" data-toggle="tooltip"
										data-placement="bottom" 
										title="${tooltip }" ></form:input>
					<spring:bind path="fieldSeparator">
						<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
					</spring:bind>
				</div>
			</div>
		</div>
		
			
		
		<div class="col-md-6 no-padding" id="fileHeaderType" style="display:none">
			<spring:message code="ascii.plugin.advance.details.fileHeader.type.label" var="label" ></spring:message>
			<spring:message code="ascii.plugin.advance.details.fileHeader.type.label.tooltip" var="tooltip" ></spring:message>
			<div class="form-group">
				<div class="table-cell-label">${label}</div>
				<div class="input-group ">
					
					<form:select path="fileHeaderParser" cssClass="form-control table-cell input-sm" tabindex="4"
										id="fileHeaderParser"
										data-toggle="tooltip" data-placement="bottom"
										 title="${tooltip }" >
										 
					<c:forEach var="headerFooterTypeEnum" items="${headerFooterTypeEnum}">
						<form:option value="${headerFooterTypeEnum}">${headerFooterTypeEnum}</form:option>
					</c:forEach>
										
					</form:select>
					<spring:bind path="fileHeaderParser">
						<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
					</spring:bind>
				</div>
			</div>
		</div>
		
			
		<div class="col-md-6 no-padding" id="headerContains" style="display:none">
			<spring:message code="ascii.plugin.advance.details.fileHeader.containsField.label" var="label" ></spring:message>
			<spring:message code="ascii.plugin.advance.details.fileHeader.containsField.label.tooltip" var="tooltip" ></spring:message>
			<div class="form-group">
				<div class="table-cell-label">${label}</div>
				<div class="input-group ">
					
					<form:select path="fileHeaderContainsFields" cssClass="form-control table-cell input-sm" tabindex="4"
										id="fileHeaderContainsFields"
										data-toggle="tooltip" data-placement="top"
										 title="${tooltip }" >
										 
					<c:forEach var="trueFalseEnum" items="${trueFalseEnum}">
						<form:option value="${trueFalseEnum.name}">${trueFalseEnum}</form:option>
					</c:forEach>
										
					</form:select>
					<spring:bind path="fileHeaderContainsFields">
						<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
					</spring:bind>
				</div>
			</div>
		</div>
