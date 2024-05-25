<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="/WEB-INF/ShowErrorTag.tld" prefix="elitecoreError"%>
<%@page import="com.elitecore.sm.common.model.TrueFalseEnum"%>

<div class="box box-warning" id="filter_param_div">
	<div class="box-header with-border">
		<h3 class="box-title">
			<spring:message code="netflow.collSer.config.filter.param.popup.header"></spring:message>
		</h3>
		<div class="box-tools pull-right" style="top: -3px;">
			<button class="btn btn-box-tool" data-widget="collapse">
				<i class="fa fa-minus"></i>
			</button>
		</div>
		<!-- /.box-tools -->
	</div>
	<!-- /.box-header end here -->
	<div class="box-body">
		<div class="fullwidth inline-form">
			<div class="col-md-6 no-padding">
				<spring:message
					code="netflow.collSer.config.filter.param.enable.temp"
					var="tooltip">
				</spring:message>
				<div class="form-group">
					<div class="table-cell-label">${tooltip}</div>
					<div class="input-group">
						<form:select path="filterEnable"
							cssClass="form-control table-cell input-sm" tabindex="4"
							id="service-filterEnable" data-toggle="tooltip"
							data-placement="bottom" title="${tooltip}">							
							<form:option value="true" label="True" />
    						<form:option value="false" label="False" />
						</form:select>
						<spring:bind path="filterEnable">
							<elitecoreError:showError errorMessage="${status.errorMessage}"
								errorId="service-filterEnable_error"></elitecoreError:showError>
						</spring:bind>
					</div>
				</div>
			</div>
			<div class="col-md-6 no-padding">
				<spring:message
					code="netflow.collSer.config.filter.param.protocol.temp"
					var="tooltip">
				</spring:message>
				<div class="form-group">
					<div class="table-cell-label">${tooltip}</div>
					<div class="input-group">
						<form:select path="filterProtocol"
							cssClass="form-control table-cell input-sm" tabindex="4"
							id="service-filterProtocol" data-toggle="tooltip"
							data-placement="bottom" title="${tooltip}">
							<form:option label="SELECT PROTOCOL" value=""/>
							<form:option value="DNS" label="DNS" />
						</form:select>
						<spring:bind path="filterProtocol">
							<elitecoreError:showError errorMessage="${status.errorMessage}"
								errorId="service-filterProtocol_error"></elitecoreError:showError>
						</spring:bind>
					</div>
				</div>
			</div>
			<div class="col-md-6 no-padding" id="filterTransportDiv">
				<spring:message
					code="netflow.collSer.config.filter.param.transport.temp"
					var="tooltip">
				</spring:message>
				<div class="form-group">
					<div class="table-cell-label">${tooltip}</div>
					<div class="input-group">
						<form:select path="filterTransport"
							cssClass="form-control table-cell input-sm" tabindex="4"
							id="service-filterTransport" data-toggle="tooltip"
							data-placement="bottom" title="${tooltip}" multiple="multiple">
							<form:option value="UDP" label="UDP" />
    						<form:option value="TCP" label="TCP" />
    						<form:option value="SCTP" label="SCTP" />
						</form:select>
						<spring:bind path="filterTransport">
							<elitecoreError:showError errorMessage="${status.errorMessage}"
								errorId="service-filterTransport_error"></elitecoreError:showError>
						</spring:bind>
					</div>
				</div>
			</div>
			<div class="col-md-6 no-padding">
				<spring:message 
					code="netflow.collSer.config.filter.param.port.tooltip"
					var="tooltip">
				</spring:message>
				<spring:message 
					code="netflow.collSer.config.filter.param.port.label"
					var="label">
				</spring:message>
				<div class="form-group">
					<div class="table-cell-label">${label}</div>
					<div class="input-group ">
						<form:input path="filterPort"
							cssClass="form-control table-cell input-sm" tabindex="4"
							readonly="false" id="service-filterPort"
							data-toggle="tooltip" data-placement="bottom" title="${tooltip}"
							onkeypress="return isTemplateValue(arguments[0] || window.event);"></form:input>
						<spring:bind path="filterPort">
							<elitecoreError:showError errorMessage="${status.errorMessage}"
								errorId="service-filterPort_error"></elitecoreError:showError>
						</spring:bind>
					</div>
				</div>
			</div>
		</div>
	</div>
	<!-- Form content end here  -->
</div>
