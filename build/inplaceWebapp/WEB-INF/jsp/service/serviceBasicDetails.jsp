<%@ taglib uri="http://www.springframework.org/security/tags"	prefix="sec"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="/WEB-INF/ShowErrorTag.tld" prefix="elitecoreError"%>
<!-- section-1 start here -->
			<div class="box box-warning" id="service_basic_details_div">
				<div class="box-header with-border">
					<h3 class="box-title">
						<spring:message
							code="service.configuration.basic.detail.section.heading" ></spring:message>
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
							<spring:message code="serviceManager.add.service.name" var="name" ></spring:message>
							<spring:message code="collectionServiceManager.add.service.name.tooltip" var="tooltip" ></spring:message>
							<div class="form-group">
								<div class="table-cell-label">${name}<span class="required">*</span></div>
								<div class="input-group ">
									<form:input path="name"
										cssClass="form-control table-cell input-sm" tabindex="4"
										id="name" data-toggle="tooltip"
										data-placement="bottom" 
										title="${tooltip }"></form:input>
									<spring:bind path="name">
										<elitecoreError:showError
											errorMessage="${status.errorMessage}" errorId="name_error"></elitecoreError:showError>
									</spring:bind>
								</div>
							</div>
						</div>

						<div class="col-md-6 no-padding">
							<spring:message
								code="serviceManager.add.service.description"
								var="name" ></spring:message>
							<spring:message
								code="collectionServiceManager.add.service.description.tooltip"
								var="tooltip" ></spring:message>
							<div class="form-group">
								<div class="table-cell-label">${name}</div>
								<div class="input-group ">
									<form:input path="description"
										cssClass="form-control table-cell input-sm" tabindex="4"
										id="description" data-toggle="tooltip"
										data-placement="bottom" 
										title="${tooltip }"></form:input>
									<spring:bind path="description">
										<elitecoreError:showError
											errorMessage="${status.errorMessage}" errorId="description_error"></elitecoreError:showError>
									</spring:bind>
								</div>
							</div>
						</div>

						

					</div>
				</div>
				<!-- Form content end here  -->
			</div>
			<!-- section-1 end here -->
