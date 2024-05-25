<%@ taglib uri="http://www.springframework.org/security/tags"	prefix="sec"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="/WEB-INF/ShowErrorTag.tld" prefix="elitecoreError"%>

			
			<div class="box box-warning">
				<div class="box-header with-border">
					<h3 class="box-title">
						<spring:message
							code="service.configuration.operational.section.heading" ></spring:message>
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
							<spring:message code="aggregationService.service.config.file.range" var="tooltip"></spring:message>
	                    	<spring:message code="aggregationService.service.config.min.file.range" var="minTooltip"></spring:message>
	                    	<spring:message code="aggregationService.service.config.max.file.range" var="maxTooltip"></spring:message>
							<div class="table-cell-label left" style="float:left">${tooltip}</div>
							<div class="input-group">
								<div class="col-md-6 no-padding">
									<div class="form-group">
			                          	<div class="input-group">
			                          		<form:input path="minFileRange" id="service-minFileRange" cssClass="form-control table-cell input-sm" tabindex="4" data-toggle="tooltip" data-placement="bottom" title="${minTooltip}" onkeydown='isNumericOnKeyDown(event)' maxlength="4"></form:input>
		        							<spring:bind path="minFileRange">
												<elitecoreError:showError errorMessage="${status.errorMessage}" errorId="minFileRange_error"></elitecoreError:showError>
											</spring:bind> 
			                          	</div>
			                         </div> 	
								</div>
								<div class="col-md-6 no-padding">
									<div class="form-group">
			                          	<div class="input-group"> 
			                          		<form:input path="maxFileRange" id="service-maxFileRange" cssClass="form-control table-cell input-sm" tabindex="4" data-toggle="tooltip" data-placement="bottom" title="${maxTooltip}" onkeydown='isNumericOnKeyDown(event)' maxlength="4" ></form:input>
		        							<spring:bind path="maxFileRange">
												<elitecoreError:showError errorMessage="${status.errorMessage}" errorId="maxFileRange_error"></elitecoreError:showError>
											</spring:bind>
			                          	</div>
			                         </div> 	
								</div>
							</div>	
        				</div>
						<div class="col-md-6 no-padding">
							<spring:message
								code="aggregationService.service.config.no.file.alert.interval"	var="tooltip" ></spring:message>
							<div class="form-group">
								<div class="table-cell-label">${tooltip}</div>
								<div class="input-group ">
									<form:input path="noFileAlert" cssClass="form-control table-cell input-sm" tabindex="4"
										id="noFileAlert" data-toggle="tooltip" maxlength="4"
										data-placement="bottom" onkeydown='isNumericOnKeyDown(event)'
										title="${tooltip }" ></form:input>
									<spring:bind path="noFileAlert">
										<elitecoreError:showError
											errorMessage="${status.errorMessage}"></elitecoreError:showError>
									</spring:bind>
								</div>
							</div>
						</div>	
						
						<div class="col-md-6 no-padding">
							<spring:message
								code="aggregationService.configuration.errorpath"	var="tooltip" ></spring:message>
							<div class="form-group">
								<div class="table-cell-label">${tooltip}</div>
								<div class="input-group ">
									<form:input path="errorPath"
										cssClass="form-control table-cell input-sm" tabindex="4"
										id="errorFilePath" data-toggle="tooltip"
										data-placement="bottom" title="${tooltip }"></form:input>
									<spring:bind path="errorPath">
										<elitecoreError:showError
											errorMessage="${status.errorMessage}"></elitecoreError:showError>
									</spring:bind>
								</div>
							</div>
						</div>
						
						<div class="col-md-6 no-padding">
							<spring:message code="aggregationService.configuration.delimiter" var="tooltip" ></spring:message>
							<div class="form-group">
								<div class="table-cell-label">${tooltip}</div>
								<div class="input-group ">
									<form:input path="delimiter"
										cssClass="form-control table-cell input-sm" tabindex="4" maxlength="1"
										id="delimiter" data-toggle="tooltip"
										data-placement="bottom" title="${tooltip }"></form:input>
									<spring:bind path="delimiter">
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
			<!-- section-2 end here -->
