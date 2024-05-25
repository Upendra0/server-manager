<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="/WEB-INF/ShowErrorTag.tld" prefix="elitecoreError"%>
<%@page import="com.elitecore.sm.common.model.TrueFalseEnum"%>

<div class="box box-warning" id="option_template_param_div">
				<div class="box-header with-border">
					<h3 class="box-title">
						<spring:message code="netflow.collSer.config.template.param.popup.header" ></spring:message>
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
							<spring:message code="netflow.collSer.config.template.param.enable.temp" var="tooltip" ></spring:message>
							<div class="form-group">
								<div class="table-cell-label">${tooltip}</div>
								<div class="input-group">
									<form:select path="optionTemplateEnable" cssClass="form-control table-cell input-sm" tabindex="4" id="service-optionTemplateEnable" data-toggle="tooltip" data-placement="bottom"  title="${tooltip}" value="${NETFLOW_COLLECTION_SERVICE_CONFIGURATION_FORM_BEAN.optionTemplateEnable}" onchange="changeOptionTempateOption(this.value);">
									<%
									 for(TrueFalseEnum enumlist : TrueFalseEnum.values()) { 
										 %>
										 	<form:option value="<%=  enumlist.getName() %>"><%= enumlist.toString() %></form:option>
										 <%
									     }
									%>
									</form:select>
									<spring:bind path="optionTemplateEnable">
										<elitecoreError:showError errorMessage="${status.errorMessage}" errorId="service-optionTemplateEnable_error"></elitecoreError:showError>
									</spring:bind>
								</div>
							</div>
						</div>

						<div class="col-md-6 no-padding">
							<spring:message code="netflow.collSer.config.template.param.option.temp.id" var="tooltip" ></spring:message>
							<div class="form-group">
								<div class="table-cell-label">${tooltip}</div>
								<div class="input-group ">
									<form:input path="optionTemplateId" cssClass="form-control table-cell input-sm" tabindex="4" readonly="false" id="service-optionTemplateId" data-toggle="tooltip" data-placement="bottom" title="${tooltip }" onkeypress="return isTemplateValue(arguments[0] || window.event);"></form:input>
									<spring:bind path="optionTemplateId">
										<elitecoreError:showError errorMessage="${status.errorMessage}" errorId="service-optionTemplateId_error"></elitecoreError:showError>
									</spring:bind>
								</div>
							</div>
						</div>
						
						<div class="col-md-6 no-padding">
							<spring:message code="netflow.collSer.config.template.param.key.id" var="tooltip" ></spring:message>
							<div class="form-group">
								<div class="table-cell-label">${tooltip}</div>
								<div class="input-group ">
									<form:input path="optionTemplateKey" cssClass="form-control table-cell input-sm" tabindex="4" readonly="false"	id="service-optionTemplateKey" data-toggle="tooltip" data-placement="bottom" title="${tooltip }" onkeypress="return isNumber(arguments[0] || window.event);"></form:input>
									<spring:bind path="optionTemplateKey">
										<elitecoreError:showError errorMessage="${status.errorMessage}" errorId="service-optionTemplateKey_error"></elitecoreError:showError>
									</spring:bind>
								</div>
							</div>
						</div>
						
						<div class="col-md-6 no-padding">
							<spring:message code="netflow.collSer.config.template.param.key.val" var="tooltip" ></spring:message>
							<div class="form-group">
								<div class="table-cell-label">${tooltip}</div>
								<div class="input-group ">
									<form:input path="optionTemplateValue" cssClass="form-control table-cell input-sm" tabindex="4" readonly="false"	id="service-optionTemplateValue" data-toggle="tooltip" data-placement="bottom" title="${tooltip }" onkeypress="return isNumber(arguments[0] || window.event);"></form:input>
									<spring:bind path="optionTemplateValue">
										<elitecoreError:showError errorMessage="${status.errorMessage}" errorId="service-optionTemplateValue_error"></elitecoreError:showError>
									</spring:bind>
								</div>
							</div>
						</div>
						
						<div class="col-md-6 no-padding">
							<spring:message code="netflow.collSer.config.template.param.temp.id" var="tooltip" ></spring:message>
							<div class="form-group">
								<div class="table-cell-label">${tooltip}</div>
								<div class="input-group ">
									<form:input path="optionCopytoTemplateId" cssClass="form-control table-cell input-sm" tabindex="4" readonly="false"	id="service-optionCopytoTemplateId" data-toggle="tooltip" data-placement="bottom" title="${tooltip }" onkeypress="return isTemplateValue(arguments[0] || window.event);"></form:input>
									<spring:bind path="optionCopytoTemplateId">
										<elitecoreError:showError errorMessage="${status.errorMessage}" errorId="service-optionCopytoTemplateId_error"></elitecoreError:showError>
									</spring:bind>
								</div>
							</div>
						</div>
						
						<div class="col-md-6 no-padding">
							<spring:message code="netflow.collSer.config.template.param.field.val" var="tooltip" ></spring:message>
							<div class="form-group">
								<div class="table-cell-label">${tooltip}</div>
								<div class="input-group ">
									<form:input path="optionCopyTofield" cssClass="form-control table-cell input-sm" tabindex="4" readonly="false" id="service-optionCopyTofield" data-toggle="tooltip" data-placement="bottom" title="${tooltip }" onkeypress="return isTemplateValue(arguments[0] || window.event);"></form:input>
									<spring:bind path="optionCopyTofield">
										<elitecoreError:showError errorMessage="${status.errorMessage}" errorId="service-optionCopyTofield_error"></elitecoreError:showError>
									</spring:bind>
								</div>
							</div>
						</div>
						
					</div>
				</div>
				<!-- Form content end here  -->
			</div>
