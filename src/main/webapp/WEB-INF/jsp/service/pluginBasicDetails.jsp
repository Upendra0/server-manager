<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<div class="box box-warning">
	<div class="box-header with-border">
		<h3 class="box-title">
			<spring:message
				code="netflowConfiguration.plugin.basic.details.heading.label" ></spring:message>
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
			<spring:message code="netflowConfiguration.plugin.id.label"
				var="tooltip" ></spring:message>
			<div class="form-group">
				<div class="table-cell-label">${tooltip}</div>
				<div class="input-group ">
					<input type="text" id="parserId" name="parserId"
						readonly="readonly" class="form-control table-cell input-sm"
						data-toggle="tooltip" value="${plugInId}" data-placement="bottom"
						title="${tooltip }"> <span
						class="input-group-addon add-on last"> <i
						class="glyphicon glyphicon-alert" data-toggle="tooltip"
						data-placement="left" title=""></i></span>
				</div>
			</div>
		</div>

		<div class="col-md-6 no-padding">
			<spring:message code="netflowConfiguration.plugin.type.label"
				var="tooltip" ></spring:message>
			<div class="form-group">
				<div class="table-cell-label">${tooltip}</div>
				<div class="input-group ">
					<input type="text" id="parserType" name="parserType"
						readonly="readonly" class="form-control table-cell input-sm"
						data-toggle="tooltip" value="${plugInType}"
						data-placement="bottom" title="${tooltip }"> <span
						class="input-group-addon add-on last"> <i
						class="glyphicon glyphicon-alert" data-toggle="tooltip"
						data-placement="left" title=""></i></span>
				</div>
			</div>
		</div>

		<div class="col-md-6 no-padding">
			<spring:message code="netflowConfiguration.plugin.name.label"
				var="tooltip" ></spring:message>
			<div class="form-group">
				<div class="table-cell-label">${tooltip}</div>
				<div class="input-group ">
					<input type="text" id="parserName" name="parserName"
						readonly="readonly" class="form-control table-cell input-sm"
						data-toggle="tooltip" value="${plugInName}"
						data-placement="bottom" title="${tooltip }"> <span
						class="input-group-addon add-on last"> <i
						class="glyphicon glyphicon-alert" data-toggle="tooltip"
						data-placement="left" title=""></i></span>
				</div>
			</div>
		</div>
	</div>
	<!-- /.box-body -->
</div>


