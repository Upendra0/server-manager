<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<%@taglib uri="/WEB-INF/ShowErrorTag.tld" prefix="elitecoreError" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">


		<div class="box box-warning">
                 <div class="box-header with-border">
                     <h3 class="box-title"> <spring:message code="parserConfiguration.read.template.block.heading.label" ></spring:message></h3>
                     <div class="box-tools pull-right">
                         <button class="btn btn-box-tool" data-widget="collapse"><i class="fa fa-minus"></i></button>
                     </div>
                     <!-- /.box-tools --> 
                 </div>	
                 <!-- /.box-header -->
                 <div class="box-body inline-form " >
						<div class="col-md-6 no-padding">
							<spring:message code="netflow.collSer.config.iosizing.param.read.temp.initial" var="tooltip" ></spring:message> 
							<div class="form-group">
								<div class="table-cell-label">${tooltip}</div>
								<div class="input-group ">
									 <form:select path="readTemplatesInitially"
										cssClass="form-control table-cell input-sm"
										id="readTemplateInitially" tabindex="1" data-toggle="tooltip"
										data-placement="bottom" title="${tooltip }">
										<c:forEach var="trueFalseEnum" items="${trueFalseEnum}">
											<form:option value="${trueFalseEnum}">${trueFalseEnum}</form:option>
										</c:forEach>
									</form:select>
									<spring:bind path="readTemplatesInitially">
										<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
									</spring:bind> 
								</div>
							</div>
						</div>
						<div class="col-md-6 no-padding">
							<spring:message code="netflow.collSer.config.iosizing.param.override.template" var="tooltip" ></spring:message> 
							<div class="form-group">
								<div class="table-cell-label">${tooltip}</div>
								<div class="input-group ">
									 <form:select path="overrideTemplate"
										cssClass="form-control table-cell input-sm"
										id="overrideTemplate" tabindex="1" data-toggle="tooltip"
										data-placement="bottom" title="${tooltip }">
										<c:forEach var="trueFalseEnum" items="${trueFalseEnum}">
											<form:option value="${trueFalseEnum}">${trueFalseEnum}</form:option>
										</c:forEach>
									</form:select>
									<spring:bind path="overrideTemplate">
										<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
									</spring:bind> 
								</div>
							</div>
						</div>
						<div class="col-md-6 no-padding">
							<spring:message code="netflow.collSer.config.iosizing.param.skipped.attribute" var="tooltip" ></spring:message> 
							<div class="form-group">
								<div class="table-cell-label">${tooltip}</div>
								<div class="input-group ">
									<form:input path="skipAttributeForValidation" tabindex="2" id="skipAttributeForValidation" cssClass="form-control table-cell input-sm" data-toggle="tooltip" data-placement="bottom" onkeypress="return isTemplateValue(arguments[0] || window.event);" title="${tooltip}" ></form:input>
									<spring:bind path="skipAttributeForValidation">
										<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
									</spring:bind> 
								</div>
							</div>
						</div>
						<!--  enable/disable default Template -->
						<div class="col-md-6 no-padding">
							<spring:message code="netflow.collSer.config.iosizing.param.enable.default.template" var="vDefaultTemplate" ></spring:message> 
							<spring:message code="netflow.collSer.config.iosizing.param.enable.default.template.tooltip" var="tooltip" ></spring:message> 
							<div class="form-group">
								<div class="table-cell-label">${vDefaultTemplate}</div>
								<div class="input-group ">
									 <form:select path="defaultTemplate"
										cssClass="form-control table-cell input-sm"
										id="defaultTemplate" tabindex="1" data-toggle="tooltip"
										data-placement="bottom" title="${tooltip}">
										<c:forEach var="trueFalseEnum" items="${trueFalseEnum}">
											<form:option value="${trueFalseEnum}">${trueFalseEnum}</form:option>
										</c:forEach>
									</form:select>
									<spring:bind path="defaultTemplate">
										<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
									</spring:bind> 
								</div>
							</div>
						</div>
					</div>
                 <!-- /.box-body --> 
             </div>

