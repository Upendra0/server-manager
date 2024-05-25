<%@page import="com.elitecore.sm.common.constants.BaseConstants"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="/WEB-INF/ShowErrorTag.tld" prefix="elitecoreError"%>
<div class="box-body inline-form ">
	<div class="col-md-6 no-padding">
				<spring:message code="asn1.plugin.advance.details.record.main.attribute"
					var="tooltip" ></spring:message>
				<div class="form-group">
					<div class="table-cell-label">${tooltip}<span class="required">*</span></div>
					<div class="input-group ">
						<form:input path="recMainAttribute"
							cssClass="form-control table-cell input-sm" 
							id="recMainAttribute" data-toggle="tooltip" tabindex="1" data-placement="bottom"
							title="${tooltip }" ></form:input>
						<spring:bind path="recMainAttribute">
							<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
						</spring:bind>
					</div>	
				</div>
		</div>
		<div class="col-md-6 no-padding">
				<spring:message code="asn1.plugin.advance.details.start.format"
					var="tooltip" ></spring:message>
				<div class="form-group">
					<div class="table-cell-label">${tooltip}</div>
					<div class="input-group ">
						<form:input path="startFormat"
							cssClass="form-control table-cell input-sm" 
							id="startFormat" data-toggle="tooltip" tabindex="2" data-placement="bottom"
							title="${tooltip }" ></form:input>
						<spring:bind path="startFormat">
							<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
						</spring:bind>
				</div>
			</div>			
		</div>
</div>
