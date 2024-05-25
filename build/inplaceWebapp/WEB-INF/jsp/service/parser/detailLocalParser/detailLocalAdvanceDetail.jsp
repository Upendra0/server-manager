<%@page import="com.elitecore.sm.common.constants.BaseConstants"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="/WEB-INF/ShowErrorTag.tld" prefix="elitecoreError"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<!--  -->
<style>
.left{float:left}
.working-thread-left{width: 67%; float:left;}
</style>
<div class="box box-warning">
	<div class="box-header with-border">
		<h3 class="box-title">
			<spring:message
				code="detail.local.plugin.advance.details.heading.label" ></spring:message> <!-- saumil.vachheta -->
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
			<spring:message code="detail.local.plugin.advance.details.vendor.seperator.present.label" var="label" ></spring:message>
			<spring:message code="detail.local.plugin.advance.details.vendor.seperator.present.tooltip" var="tooltip" ></spring:message>
			<!--  Enable/Disable of Vendor Name Seperator -->
			<div class="form-group">
				<div class="table-cell-label">${label}</div>
				<div class="input-group ">
					<form:select path="vendorNameSeparatorEnable" cssClass="form-control table-cell input-sm" tabindex="4"
						id="vendorNameSeparatorEnable"
						data-toggle="tooltip" data-placement="bottom"
						title="${tooltip }">
					<c:forEach var="trueFalseEnum" items="${trueFalseEnum}">
						<form:option value="${trueFalseEnum.name}">${trueFalseEnum}</form:option>
					</c:forEach>
					</form:select>
					<spring:bind path="vendorNameSeparatorEnable">
						<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
					</spring:bind>
				</div>
			</div>
		</div>
		
		<!--  Vendor Name Seperator -->
		<div class="col-md-6 no-padding" id="vendorSeparatorValueDiv">
			<spring:message code="detail.local.plugin.advance.details.vendor.seperator.value.label" var="label" ></spring:message>
			<spring:message code="detail.local.plugin.advance.details.vendor.seperator.value.tooltip" var="tooltip" ></spring:message>
			<div class="form-group">
				<div class="table-cell-label">${label}</div>
				<div class="input-group ">
					
					<form:input path="vendorSeparatorValue"
										cssClass="form-control table-cell input-sm" tabindex="4"
										id="fileFooterContains" data-toggle="tooltip"
										data-placement="top" 
										title="${tooltip }" ></form:input>
					<spring:bind path="vendorSeparatorValue">
						<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
					</spring:bind>
				</div>
			</div>
		</div>
		
		<!-- Attribute seperator  -->
		<div class="col-md-6 no-padding" id="attributeSeperatorDiv">
			<spring:message code="detail.local.plugin.advance.details.attribute.seperator.label" var="label" ></spring:message>
			<spring:message code="detail.local.plugin.advance.details.attribute.seperator.tooltip" var="tooltip" ></spring:message>
			<div class="form-group">
				<div class="table-cell-label">${label}</div>
				<div class="input-group ">
					
					<form:input path="attributeSeperator"
										cssClass="form-control table-cell input-sm" tabindex="4"
										id="attributeSeperator" data-toggle="tooltip"
										data-placement="top" 
										title="${tooltip }" ></form:input>
					<spring:bind path="attributeSeperator">
						<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
					</spring:bind>
				</div>
			</div>
		</div>
		</div>
	</div>
