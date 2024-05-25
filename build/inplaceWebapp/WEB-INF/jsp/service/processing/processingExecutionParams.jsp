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
								<spring:message code="parsing.service.config.file.range" var="range" ></spring:message>
								<spring:message code="parsing.service.config.min.file.range" var="minRange" ></spring:message>
								<spring:message code="parsing.service.config.max.file.range" var="maxRange" ></spring:message>
								<div class="form-group">
									<div class="table-cell-label">${range}</div>
									<div class="input-group ">
										<form:input path="minFileRange" id="service-minFileRange" cssClass="form-control table-cell input-sm" tabindex="4" data-toggle="tooltip" data-placement="bottom" title="${minRange}" onkeydown='isNumericOnKeyDown(event)'></form:input>
	        							<spring:bind path="minFileRange">
											<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
										</spring:bind> 
										<form:input path="maxFileRange" id="service-maxFileRange" cssClass="form-control table-cell input-sm" tabindex="4" data-toggle="tooltip" data-placement="bottom" title="${maxRange}" onkeydown='isNumericOnKeyDown(event)' ></form:input>
	        							<spring:bind path="maxFileRange">
											<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
										</spring:bind>
									</div>
								</div>
						</div>
						
						<div class="col-md-6 no-padding">
							<spring:message
								code="processingService.configuration.file.sequence.order"
								var="tooltip" ></spring:message>
							<div class="form-group">
								<div class="table-cell-label">${tooltip}</div>
								<div class="input-group ">
									<form:select path="fileSeqOrderEnable"
										cssClass="form-control table-cell input-sm" tabindex="4"
										id="fileSeqOrderEnable"
										data-toggle="tooltip" data-placement="bottom"
										 title="${tooltip }">
										<c:forEach items="${trueFalseEnum}" var="trueFalseEnum">
											<form:option value="${trueFalseEnum}" >${trueFalseEnum}</form:option>
										</c:forEach>
									</form:select>
									<spring:bind path="fileSeqOrderEnable">
										<elitecoreError:showError
											errorMessage="${status.errorMessage}"></elitecoreError:showError>
									</spring:bind>
								</div>
							</div>
						</div>
						
						<div class="col-md-6 no-padding">
							<spring:message
								code="processingService.configuration.record.batch.size"	var="tooltip" ></spring:message>
							<div class="form-group">
								<div class="table-cell-label">${tooltip}<span class="required">*</span></div>
								<div class="input-group ">
									<form:input path="recordBatchSize"
										cssClass="form-control table-cell input-sm" tabindex="4"
										id="recordBatchSize" data-toggle="tooltip"
										data-placement="bottom" 
										title="${tooltip }" ></form:input>
									<spring:bind path="recordBatchSize">
										<elitecoreError:showError
											errorMessage="${status.errorMessage}"></elitecoreError:showError>
									</spring:bind>
								</div>
							</div>
						</div>
						
						<div class="col-md-6 no-padding">
							<spring:message
								code="processingService.configuration.no.file.alert.interval"	var="tooltip" ></spring:message>
							<div class="form-group">
								<div class="table-cell-label">${tooltip}</div>
								<div class="input-group ">
									<form:input path="noFileAlert" cssClass="form-control table-cell input-sm" tabindex="4"
										id="noFileAlert" data-toggle="tooltip"
										data-placement="bottom" 
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
								code="processingService.configuration.errorpath"	var="tooltip" ></spring:message>
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
					</div>
				</div>
				<!-- Form content end here  -->
			</div>
			<!-- section-2 end here -->
