<%@ taglib uri="http://www.springframework.org/security/tags"	prefix="sec"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="/WEB-INF/ShowErrorTag.tld" prefix="elitecoreError"%>


			
			<!-- section-2 start here -->
			<div class="box box-warning">
				<div class="box-header with-border">
					<h3 class="box-title">
						<spring:message
							code="service.operational.param.section.heading" ></spring:message>
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
							<spring:message
								code="service.execution.sorting.type.label"
								var="tooltip" ></spring:message>
							<div class="form-group">
								<div class="table-cell-label">${tooltip}</div>
								<div class="input-group ">
									<form:select path="svcExecParams.startupMode"
										cssClass="form-control table-cell input-sm" tabindex="4"
										id="svcExecParams-startupMode"
										data-toggle="tooltip" data-placement="bottom"
										title="${tooltip }">
										<c:forEach items="${sortingType}" var="sortingType">
											<form:option value="${sortingType}" >${sortingType}</form:option>
										</c:forEach>
									</form:select>
									<spring:bind path="svcExecParams.sortingType">
										<elitecoreError:showError
											errorMessage="${status.errorMessage}"></elitecoreError:showError>
									</spring:bind>
								</div>
							</div>
						</div>
						<div class="col-md-6 no-padding">
							<spring:message
								code="service.execution.sorting.on.label"
								var="tooltip" ></spring:message>
							<div class="form-group">
								<div class="table-cell-label">${tooltip}</div>
								<div class="input-group ">
									<form:select path="svcExecParams.startupMode"
										cssClass="form-control table-cell input-sm" tabindex="4"
										id="svcExecParams-startupMode"
										data-toggle="tooltip" data-placement="bottom"
										 title="${tooltip }">
										<c:forEach items="${sortingCriteria}" var="sortingCriteria">
											<form:option value="${sortingCriteria}" >${sortingCriteria}</form:option>
										</c:forEach>
									</form:select>
									<spring:bind path="svcExecParams.sortingCriteria">
										<elitecoreError:showError
											errorMessage="${status.errorMessage}"></elitecoreError:showError>
									</spring:bind>
								</div>
							</div>
						</div>
						
						 <div class="col-md-6 no-padding">
        					<div class="form-group" style="display:inline-flex;">
								<spring:message code="service.execution.working.thread.min.label" var="minTooltip"></spring:message>
								<spring:message code="service.execution.working.thread.max.label" var="maxTooltip"></spring:message>
								<div class="table-cell-label"><spring:message code="addServerInstance.instance.min.max.memory"></spring:message></div>
 								<div class="input-group" style="width: 39%;">
									<form:input path="svcExecParams.minThread" id="minThread" cssClass="form-control table-cell input-sm" data-toggle="tooltip" data-placement="bottom" title="${minTooltip}"></form:input>
        							<spring:bind path="svcExecParams.minThread">
										<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
									</spring:bind>
         						</div>
         						<div class="input-group" style="width: 39%;">
									<form:input path="svcExecParams.maxThread" id="maxThread" cssClass="form-control table-cell input-sm" data-toggle="tooltip" data-placement="bottom" title="${maxTooltip}"  ></form:input>
        							<spring:bind path="svcExecParams.maxThread">
										<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
									</spring:bind>
         						</div>
        					</div>
        				</div>
						<div class="col-md-6 no-padding">
							<spring:message
								code="service.execution.batch.size.label"	var="tooltip" ></spring:message>
							<div class="form-group">
								<div class="table-cell-label">${tooltip}</div>
								<div class="input-group ">
									<form:input path="svcExecParams.fileBatchSize"
										cssClass="form-control table-cell input-sm" tabindex="4"
										id="svcExecParams-fileBatchSize" data-toggle="tooltip"
										data-placement="bottom" 
										title="${tooltip }" ></form:input>
									<spring:bind path="svcExecParams.fileBatchSize">
										<elitecoreError:showError
											errorMessage="${status.errorMessage}"></elitecoreError:showError>
									</spring:bind>
								</div>
							</div>
						</div>
						
						<div class="col-md-6 no-padding">
							<spring:message
								code="service.execution.queue.size"	var="tooltip" ></spring:message>
							<div class="form-group">
								<div class="table-cell-label">${tooltip}</div>
								<div class="input-group ">
									<form:input path="svcExecParams.queueSize"
										cssClass="form-control table-cell input-sm" tabindex="4"
										id="svcExecParams-queueSize" data-toggle="tooltip"
										data-placement="bottom" 
										title="${tooltip }" ></form:input>
									<spring:bind path="svcExecParams.queueSize">
										<elitecoreError:showError
											errorMessage="${status.errorMessage}"></elitecoreError:showError>
									</spring:bind>
								</div>
							</div>
						</div>
						
							<div class="col-md-6 no-padding">
							<spring:message
								code="parsingService.configuration.min.disk.space"	var="tooltip" ></spring:message>
							<div class="form-group">
								<div class="table-cell-label">${tooltip}</div>
								<div class="input-group ">
									<form:input path="svcExecParams.minDiskSpaceRequiredGB"
										cssClass="form-control table-cell input-sm" tabindex="4"
										id="minDiskSpaceRequiredGB" data-toggle="tooltip"
										data-placement="bottom" 
										title="${tooltip }" ></form:input>
									<spring:bind path="svcExecParams.minDiskSpaceRequiredGB">
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
			
