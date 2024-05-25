<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="/WEB-INF/ShowErrorTag.tld" prefix="elitecoreError" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">


<div class="box box-warning">
			
                 <div class="box-header with-border">
                     <h3 class="box-title"> <spring:message code="parserConfiguration.device.block.heading.label" ></spring:message></h3>
                     <div class="box-tools pull-right">
                         <button class="btn btn-box-tool" data-widget="collapse"><i class="fa fa-minus"></i></button>
                     </div>
                     <!-- /.box-tools --> 
                 </div>	
                 <!-- /.box-header -->
                 <div class="box-body inline-form " >
						<div class="col-md-6 no-padding">
							<spring:message code="device.list.grid.device.type.label" var="tooltip" ></spring:message> 
							<div class="form-group">
								<div class="table-cell-label">${tooltip}</div>
								<div class="input-group ">
										<select  name = "deviceType" class="form-control table-cell input-sm"  tabindex="3" id="deviceType" data-toggle="tooltip" data-placement="bottom"  title="${tooltip }"  onblur="getVendorByDeviceType('getVendorListByDeviceType',this.value,'vendorType',' ','null');"   onchange="getVendorByDeviceType('getVendorListByDeviceType',this.value,'vendorType',' ','null');">
					             			<option  value="-1" selected="selected"><spring:message code="device.type.dropdown.default.selection.label" ></spring:message></option>
					             			<c:if test="${deviceTypeList !=null && fn:length(deviceTypeList) gt 0}">
							             		<c:forEach items="${deviceTypeList}" var="deviceType">
							             			<option value="${deviceType.id}">${deviceType.name}</option>
							             		</c:forEach>
						             		</c:if>
					             		</select>		
										<span class="input-group-addon add-on last"> 
											<i class="glyphicon glyphicon-alert" data-toggle="tooltip"	data-placement="left" title=""></i>
										</span>
									
								</div>
							</div>
						</div>
						<div class="col-md-6 no-padding">
							<spring:message code="device.list.grid.device.vendor.type.label" var="tooltip" ></spring:message> 
							<div class="form-group">
								<div class="table-cell-label">${tooltip}</div>
								<div class="input-group ">
										<select  name = "vendorType" class="form-control table-cell input-sm"  tabindex="3" id="vendorType" data-toggle="tooltip" data-placement="bottom"  title="${tooltip }" >
						             		<option  value="-1" selected="selected"><spring:message code="vendor.type.dropdown.default.selection.label" ></spring:message></option>
						             	</select>
										<span class="input-group-addon add-on last"> 
											<i	class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="left" title=""></i>
										</span>
								</div>
							</div>
						</div>
						<div class="col-md-6 no-padding">
							<spring:message code="device.list.grid.device.name.label" var="tooltip" ></spring:message> 
							<div class="form-group">
								<div class="table-cell-label">${tooltip}</div>
								<div class="input-group ">
										<select  name = "deviceName" class="form-control table-cell input-sm"  tabindex="3" id="deviceName" data-toggle="tooltip" data-placement="bottom"  title="${tooltip }"  >
						             		<option  value="-1" selected="selected"><spring:message code="device.name.dropdown.default.selection.label" ></spring:message></option>
						             	</select> 
										<span class="input-group-addon add-on last"> 
											<i	class="glyphicon glyphicon-alert" data-toggle="tooltip"	data-placement="left" title=""></i>
										</span>
								</div>
							</div>
						</div>
						<div class="col-md-6 no-padding">
							<spring:message code="device.list.grid.device.mapping.name.label" var="label" ></spring:message> 
							<div class="form-group">
								<div class="table-cell-label">${label}</div>
								<div class="input-group ">
										<select  name = "mappingName" class="form-control table-cell input-sm"  tabindex="3" id="mappingName" data-toggle="tooltip" data-placement="bottom"  title="${label }" >
						             		<option  value="-1" selected="selected"><spring:message code="mapping.name.dropdown.default.selection.label" ></spring:message></option>
						             	</select> 
										<span class="input-group-addon add-on last">
										 	<i	class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="left" title=""></i>
										</span>
								</div>
							</div>
						</div>
					</div>
                 <!-- /.box-body --> 
                 <input type="hidden" value="" id="mappingTypeVal" name="mappingTypeVal">
             </div>
