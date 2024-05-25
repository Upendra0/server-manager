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
		<!-- Form content start here  -->
		<div class="box-body">
			<div class="fullwidth inline-form">
				<div class="col-md-6 no-padding">
					<spring:message code="xml.plugin.advance.details.recordWiseXmlFormat.label" var="name" ></spring:message>
					<spring:message code="xml.plugin.advance.details.recordWiseXmlFormat.label.tooltip" var="tooltip" ></spring:message>
					<div class="form-group">
						<div class="table-cell-label">${name}</div>
						<div class="input-group ">
							<form:select path="recordWiseXmlFormat" cssClass="form-control table-cell input-sm" tabindex="2"
								id="recordWiseXmlFormat"
								data-toggle="tooltip" data-placement="bottom"
								title="${tooltip }">
												 
								<c:forEach var="trueFalseEnum" items="${trueFalseEnum}">
									<form:option value="${trueFalseEnum.name}">${trueFalseEnum}</form:option>
								</c:forEach>
												
							</form:select>
							<spring:bind path="recordWiseXmlFormat">
								<elitecoreError:showError
									errorMessage="${status.errorMessage}"></elitecoreError:showError>
							</spring:bind>
						</div>
					</div>
				</div>
			<div class="col-md-6 no-padding">
					<spring:message code="xml.plugin.advance.details.commonFields.label" var="name" ></spring:message>
					<spring:message code="xml.plugin.advance.details.commonFields.label.tooltip" var="tooltip" ></spring:message>
					<div class="form-group">
						<div class="table-cell-label">${name}</div>
						<div class="input-group ">
							<form:input path="commonFields"
								cssClass="form-control table-cell input-sm" tabindex="3"
								id="commonFields" data-toggle="tooltip"
								data-placement="bottom" 
								title="${tooltip }"></form:input>
							<spring:bind path="commonFields">
								<elitecoreError:showError
									errorMessage="${status.errorMessage}"></elitecoreError:showError>
							</spring:bind>
						</div>
					</div>
				</div>
			</div>
		</div>
		<!-- Form content end here  -->
</div>

			
