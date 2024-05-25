<%@page import="com.elitecore.sm.common.constants.BaseConstants"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/security/tags"
	prefix="sec"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="/WEB-INF/ShowErrorTag.tld" prefix="elitecoreError"%>

<div class="col-md-6 no-padding" id="enableFileFooter"
	style="display: none">
	<spring:message
		code="ascii.plugin.advance.details.fileFooter.enable.label"
		var="label" ></spring:message>
	<spring:message
		code="ascii.plugin.advance.details.fileFooter.enable.label.tooltip"
		var="tooltip" ></spring:message>
	<div class="form-group">
		<div class="table-cell-label">${label}</div>
		<div class="input-group ">

			<form:select path="fileFooterEnable"
				cssClass="form-control table-cell input-sm" tabindex="4"
				id="fileFooterEnable" data-toggle="tooltip" data-placement="bottom"
				title="${tooltip }">

				<c:forEach var="trueFalseEnum" items="${trueFalseEnum}">
					<form:option value="${trueFalseEnum.name}">${trueFalseEnum}</form:option>
				</c:forEach>

			</form:select>
			<spring:bind path="fileFooterEnable">
				<elitecoreError:showError errorMessage="${status.errorMessage}" errorId="fileFooterEnable_error"></elitecoreError:showError>
			</spring:bind>
		</div>
	</div>
</div>
