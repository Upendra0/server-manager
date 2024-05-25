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
			<spring:message code="pdf.plugin.advance.details.file.parsed.label" var="label" ></spring:message>
			<spring:message code="pdf.plugin.advance.details.file.parsed.label.tooltip" var="tooltip" ></spring:message>
			<div class="form-group">
				<div class="table-cell-label">${label}</div>
				<div class="input-group ">
					<form:select path="fileParsed" cssClass="form-control table-cell input-sm" tabindex="1"
										id="fileParsed"
										data-toggle="tooltip" data-placement="bottom"
										 title="${tooltip }">
										 
					<c:forEach var="trueFalseEnum" items="${trueFalseEnum}">
						<form:option value="${trueFalseEnum.name}">${trueFalseEnum}</form:option>
					</c:forEach>
					</form:select>
					<spring:bind path="fileParsed">
						<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
					</spring:bind>
				</div>
			</div>
		</div>
		
		<div class="col-md-6 no-padding">
			<spring:message code="pdf.plugin.advance.details.record.wise.pdf.format.label" var="label" ></spring:message>
			<spring:message code="pdf.plugin.advance.details.record.wise.pdf.format.label.tooltip" var="tooltip" ></spring:message>
			<div class="form-group">
				<div class="table-cell-label">${label}</div>
				<div class="input-group ">
					<form:select path="recordWisePdfFormat" cssClass="form-control table-cell input-sm" tabindex="1"
										id="recordWisePdfFormat"
										data-toggle="tooltip" data-placement="bottom"
										 title="${tooltip }">
										 
					<c:forEach var="trueFalseEnum" items="${trueFalseEnum}">
						<form:option value="${trueFalseEnum.name}">${trueFalseEnum}</form:option>
					</c:forEach>
					</form:select>
					<spring:bind path="recordWisePdfFormat">
						<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
					</spring:bind>
				</div>
			</div>
		</div>
		
		<div class="col-md-6 no-padding">
			<spring:message code="pdf.plugin.advance.details.file.multi.invoice.label" var="label" ></spring:message>
			<spring:message code="pdf.plugin.advance.details.file.multi.invoice.label.tooltip" var="tooltip" ></spring:message>
			<div class="form-group">
				<div class="table-cell-label">${label}</div>
				<div class="input-group ">
					<form:select path="multiInvoice" cssClass="form-control table-cell input-sm" tabindex="1"
										id="multiInvoice"
										data-toggle="tooltip" data-placement="bottom"
										 title="${tooltip }">
										 
					<c:forEach var="trueFalseEnum" items="${trueFalseEnum}">
						<form:option value="${trueFalseEnum.name}">${trueFalseEnum}</form:option>
					</c:forEach>
					</form:select>
					<spring:bind path="recordWisePdfFormat">
						<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
					</spring:bind>
				</div>
			</div>
		</div>
		
		<div class="col-md-6 no-padding">
			<spring:message code="pdf.plugin.advance.details.file.multi.pages.label" var="label" ></spring:message>
			<spring:message code="pdf.plugin.advance.details.file.multi.pages.label.tooltip" var="tooltip" ></spring:message>
			<div class="form-group">
				<div class="table-cell-label">${label}</div>
				<div class="input-group ">
					<form:select path="multiPages" cssClass="form-control table-cell input-sm" tabindex="1"
										id="multiPages"
										data-toggle="tooltip" data-placement="bottom"
										 title="${tooltip}">
										 
					<c:forEach var="trueFalseEnum" items="${trueFalseEnum}">
						<form:option value="${trueFalseEnum.name}">${trueFalseEnum}</form:option>
					</c:forEach>
					</form:select>
					<spring:bind path="recordWisePdfFormat">
						<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
					</spring:bind>
				</div>
			</div>
		</div>
		
	<!-- /.box-body -->
</div>
