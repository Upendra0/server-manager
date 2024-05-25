<%@ taglib uri="http://www.springframework.org/security/tags"	prefix="sec"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="/WEB-INF/ShowErrorTag.tld" prefix="elitecoreError"%>

<!-- section-1 start here -->
			<div class="box box-warning">
				<div class="box-header with-border">
					<h3 class="box-title">
						<spring:message
							code="parsingService.configuration.startup.section.heading" ></spring:message>
					</h3>
					<div class="box-tools pull-right" style="top: -3px;">
						<button class="btn btn-box-tool" data-widget="collapse">
							<i class="fa fa-minus"></i>
						</button>
					</div>
					<!-- /.box-tools -->
				</div>
				<!-- /.box-header end here -->
				<!-- Form content start here  -->
				<div class="box-body">
					<div class="fullwidth inline-form">

						<div class="col-md-6 no-padding">
							<spring:message code="service.startupmode.label" var="tooltip" ></spring:message>
							<div class="form-group">
								<div class="table-cell-label">${tooltip}<span class="required">*</span></div>
								<div class="input-group ">
									<form:select path="svcExecParams.startupMode"
										cssClass="form-control table-cell input-sm" tabindex="4"
										id="svcExecParams-startupMode"
										data-toggle="tooltip" data-placement="bottom"
										 title="${tooltip }">
										<c:forEach items="${startupMode}" var="startupMode">
											<form:option value="${startupMode}">${startupMode}</form:option>
										</c:forEach>
									</form:select>
									<spring:bind path="svcExecParams.startupMode">
										<elitecoreError:showError
											errorMessage="${status.errorMessage}"></elitecoreError:showError>
									</spring:bind>
								</div>
							</div>
						</div>

						<div class="col-md-6 no-padding">
							<spring:message
								code="service.immediate.execution.startup.label"
								var="tooltip" ></spring:message>
							<div class="form-group">
								<div class="table-cell-label">${tooltip}<span class="required">*</span></div>
								<div class="input-group ">
									<form:select path="svcExecParams.executeOnStartup"
										cssClass="form-control table-cell input-sm" tabindex="4"
										id="instance-fileStatInDBEnable" data-toggle="tooltip"
										data-placement="bottom" 
										title="${tooltip }">
										<c:forEach var="trueFalseEnum" items="${trueFalseEnum}">
	                             			<form:option value="${trueFalseEnum.name}">${trueFalseEnum}</form:option>
	                             		</c:forEach>
									</form:select>
									<spring:bind path="svcExecParams.executeOnStartup">
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
			<!-- section-1 end here -->
