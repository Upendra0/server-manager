<%@ taglib uri="http://www.springframework.org/security/tags"	prefix="sec"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="/WEB-INF/ShowErrorTag.tld" prefix="elitecoreError"%>

<style>
.left{float:left}
.working-thread-left{width: 67%; float:left;}
</style>
			
			<!-- section-2 start here -->
			<div class="box box-warning" id="service_execution_param_div">
				<div class="box-header with-border">
					<h3 class="box-title">
						<spring:message code="service.execution.param.section.heading" ></spring:message>
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
					
					<c:if test="${(serviceType ne 'DIAMETER_COLLECTION_SERVICE')}">
						<div class="col-md-6 no-padding">
								<spring:message code="service.startupmode.label" var="label" ></spring:message>
								<spring:message code="service.startupmode.label.tooltip" var="tooltip" ></spring:message>
								<div class="form-group">
									<div class="table-cell-label">${label}<span class="required">*</span></div>
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
												errorMessage="${status.errorMessage}" errorId="svcExecParams-startupMode_error"></elitecoreError:showError>
										</spring:bind>
									</div>
								</div>
							</div>
						</c:if>
						
						<c:if test="${(serviceType ne 'NATFLOW_COLLECTION_SERVICE') and (serviceType ne 'NATFLOWBINARY_COLLECTION_SERVICE')
						and (serviceType ne 'HTTP2_COLLECTION_SERVICE') and (serviceType ne 'MQTT_COLLECTION_SERVICE') and (serviceType ne 'COAP_COLLECTION_SERVICE') and (serviceType ne 'SYSLOG_COLLECTION_SERVICE') and (serviceType ne 'GTPPRIME_COLLECTION_SERVICE') and (serviceType ne 'DIAMETER_COLLECTION_SERVICE') }">
						<div class="col-md-6 no-padding">
							<spring:message code="service.execution.interval.label" var="label" ></spring:message>
							<spring:message code="service.execution.interval.label.tooltip" var="tooltip" ></spring:message>
							
							<div class="form-group">
								<div class="table-cell-label">${label}</div>
								<div class="input-group ">
									<form:input path="svcExecParams.executionInterval"
										cssClass="form-control table-cell input-sm" tabindex="4"
										id="svcExecParams-executionInterval" data-toggle="tooltip"
										data-placement="bottom" maxlength="4"
										title="${tooltip }" onkeydown='isNumericOnKeyDown(event)'></form:input>
									<spring:bind path="svcExecParams.executionInterval">
										<elitecoreError:showError
											errorMessage="${status.errorMessage}" errorId="svcExecParams-executionInterval_error"></elitecoreError:showError>
									</spring:bind>
								</div>
							</div>
						</div>
						</c:if>
						
						<c:if test="${(serviceType ne 'NATFLOW_COLLECTION_SERVICE') and (serviceType ne 'NATFLOWBINARY_COLLECTION_SERVICE')
						and (serviceType ne 'HTTP2_COLLECTION_SERVICE') and (serviceType ne 'MQTT_COLLECTION_SERVICE') and (serviceType ne 'COAP_COLLECTION_SERVICE') and (serviceType ne 'SYSLOG_COLLECTION_SERVICE') and (serviceType ne 'GTPPRIME_COLLECTION_SERVICE') and (serviceType ne 'DIAMETER_COLLECTION_SERVICE') and (serviceType ne 'RADIUS_COLLECTION_SERVICE')}">
						<div class="col-md-6 no-padding">
							<spring:message code="service.immediate.execution.startup.label" var="label" ></spring:message>
							<spring:message code="service.immediate.execution.startup.label.tooltip" var="tooltip" ></spring:message>
							<div class="form-group">
								<div class="table-cell-label">${label}<span class="required">*</span></div>
								<div class="input-group ">
									<form:select path="svcExecParams.executeOnStartup"
										cssClass="form-control table-cell input-sm" tabindex="4"
										id="svcExecParams-executeOnStartup" data-toggle="tooltip"
										data-placement="bottom" 
										title="${tooltip }">
										<c:forEach var="trueFalseEnum" items="${trueFalseEnum}">
	                             			<form:option value="${trueFalseEnum.name}">${trueFalseEnum}</form:option>
	                             		</c:forEach>
									</form:select>
									<spring:bind path="svcExecParams.executeOnStartup">
										<elitecoreError:showError
											errorMessage="${status.errorMessage}" errorId="svcExecParams-executeOnStartup_error"></elitecoreError:showError>
									</spring:bind>
								</div>
							</div>
						</div>
						</c:if>
						
						<div class="col-md-6 no-padding">
							<spring:message code="service.execution.queue.size"	var="label" ></spring:message>
							<spring:message code="service.execution.queue.size.tooltip"	var="tooltip" ></spring:message>
							<div class="form-group">
								<div class="table-cell-label">${label}<span class="required">*</span></div>
								<div class="input-group ">
									<form:input path="svcExecParams.queueSize"
										cssClass="form-control table-cell input-sm" tabindex="4"
										id="svcExecParams-queueSize" data-toggle="tooltip"
										data-placement="bottom" maxlength="7"
										title="${tooltip }" onkeydown='isNumericOnKeyDown(event)' ></form:input>
									<spring:bind path="svcExecParams.queueSize">
										<elitecoreError:showError
											errorMessage="${status.errorMessage}" errorId="svcExecParams-queueSize_error"></elitecoreError:showError>
									</spring:bind>
								</div>
							</div>
						</div>
						
						<div class="col-md-6 no-padding">
						
							
								<spring:message code="service.execution.working.thread.label" var="tooltip"></spring:message>
		                    	<spring:message code="service.execution.working.thread.min.label" var="minTooltip"></spring:message>
		                    	<spring:message code="service.execution.working.thread.max.label" var="maxTooltip"></spring:message>
								<div class="table-cell-label left" style="float:left">${tooltip}<span class="required">*</span></div>
							
							
							<div class="input-group">
							<div class="col-md-6 no-padding">
								<div class="form-group">
		                          	<div class="input-group">
		                          		<form:input path="svcExecParams.minThread" id="svcExecParams-minThread" cssClass="form-control table-cell input-sm" tabindex="4" data-toggle="tooltip" data-placement="bottom" title="${minTooltip}" onkeydown='isNumericOnKeyDown(event)' maxlength="3"></form:input>
	        							<spring:bind path="svcExecParams.minThread">
											<elitecoreError:showError errorMessage="${status.errorMessage}" errorId="svcExecParams-minThread_error"></elitecoreError:showError>
										</spring:bind> 
		                          	</div>
		                         </div> 	
							</div>
							<div class="col-md-6 no-padding">
								<div class="form-group">
		                          	<div class="input-group"> 
		                          		<form:input path="svcExecParams.maxThread" id="svcExecParams-maxThread" cssClass="form-control table-cell input-sm" tabindex="4" data-toggle="tooltip" data-placement="bottom" title="${maxTooltip}" onkeydown='isNumericOnKeyDown(event)' maxlength="3" ></form:input>
	        							<spring:bind path="svcExecParams.maxThread">
											<elitecoreError:showError errorMessage="${status.errorMessage}" errorId="svcExecParams-maxThread_error"></elitecoreError:showError>
										</spring:bind>
		                          	</div>
		                         </div> 	
							</div>
							</div>
		                    	
        				</div>
						
						<c:if test="${(serviceType ne 'NATFLOW_COLLECTION_SERVICE') and (serviceType ne 'NATFLOWBINARY_COLLECTION_SERVICE')
						and (serviceType ne 'HTTP2_COLLECTION_SERVICE') and (serviceType ne 'MQTT_COLLECTION_SERVICE') and (serviceType ne 'COAP_COLLECTION_SERVICE') and (serviceType ne 'SYSLOG_COLLECTION_SERVICE') and (serviceType ne 'GTPPRIME_COLLECTION_SERVICE') and (serviceType ne 'DIAMETER_COLLECTION_SERVICE') and (serviceType ne 'RADIUS_COLLECTION_SERVICE')}">
						<div class="col-md-6 no-padding">
							<spring:message	code="service.execution.batch.size.label"	var="label" ></spring:message>
							<spring:message	code="service.execution.batch.size.label.tooltip"	var="tooltip" ></spring:message>
							
							<div class="form-group">
								<div class="table-cell-label">${label}<span class="required">*</span></div>
								<div class="input-group ">
									<form:input path="svcExecParams.fileBatchSize"
										cssClass="form-control table-cell input-sm" tabindex="4"
										id="svcExecParams-fileBatchSize" data-toggle="tooltip"
										data-placement="bottom" maxlength="3" 
										title="${tooltip }" size="9" onkeydown='isNumericOnKeyDown(event)'></form:input>
									<spring:bind path="svcExecParams.fileBatchSize">
										<elitecoreError:showError
											errorMessage="${status.errorMessage}" errorId="svcExecParams-fileBatchSize_error"></elitecoreError:showError>
									</spring:bind>
								</div>
							</div>
						</div>
						</c:if>
						
						<c:if test="${(serviceType ne 'NATFLOW_COLLECTION_SERVICE') and (serviceType ne 'NATFLOWBINARY_COLLECTION_SERVICE')
						and (serviceType ne 'HTTP2_COLLECTION_SERVICE') and (serviceType ne 'MQTT_COLLECTION_SERVICE') and (serviceType ne 'COAP_COLLECTION_SERVICE') and (serviceType ne 'SYSLOG_COLLECTION_SERVICE') and (serviceType ne 'GTPPRIME_COLLECTION_SERVICE') and (serviceType ne 'DIAMETER_COLLECTION_SERVICE') and (serviceType ne 'RADIUS_COLLECTION_SERVICE')}">
						<div class="col-md-6 no-padding">
							<spring:message code="service.execution.sorting.type.label" var="label" ></spring:message>
							<spring:message code="service.execution.sorting.type.label.tooltip" var="tooltip" ></spring:message>
							<div class="form-group">
								<div class="table-cell-label">${label}<span class="required">*</span></div>
								<div class="input-group ">
									<form:select path="svcExecParams.sortingType"
										cssClass="form-control table-cell input-sm" tabindex="4"
										id="svcExecParams-sortingType"
										data-toggle="tooltip" data-placement="bottom"
										 title="${tooltip }">
										<c:forEach items="${sortingType}" var="sortingType">
											<form:option value="${sortingType.value}" >${sortingType}</form:option>
										</c:forEach>
									</form:select>
									<spring:bind path="svcExecParams.sortingType">
										<elitecoreError:showError
											errorMessage="${status.errorMessage}" errorId="svcExecParams-sortingType_error"></elitecoreError:showError>
									</spring:bind>
								</div>
							</div>
						</div>
						</c:if>
						
						<c:if test="${(serviceType ne 'NATFLOW_COLLECTION_SERVICE') and (serviceType ne 'NATFLOWBINARY_COLLECTION_SERVICE')
						and (serviceType ne 'HTTP2_COLLECTION_SERVICE') and (serviceType ne 'MQTT_COLLECTION_SERVICE') and (serviceType ne 'COAP_COLLECTION_SERVICE') and (serviceType ne 'SYSLOG_COLLECTION_SERVICE') and (serviceType ne 'GTPPRIME_COLLECTION_SERVICE') and (serviceType ne 'DIAMETER_COLLECTION_SERVICE') and (serviceType ne 'RADIUS_COLLECTION_SERVICE') }">
						<div class="col-md-6 no-padding">
							<spring:message code="service.execution.sorting.on.label" var="label" ></spring:message>
							<spring:message code="service.execution.sorting.on.label.tooltip" var="tooltip" ></spring:message>
							
							<div class="form-group">
								<div class="table-cell-label">${label}<span class="required">*</span></div>
								<div class="input-group ">
									<form:select path="svcExecParams.sortingCriteria"
										cssClass="form-control table-cell input-sm" tabindex="4"
										id="svcExecParams-sortingCriteria"
										data-toggle="tooltip" data-placement="bottom"
										 title="${tooltip }">
										<c:forEach items="${sortingCriteria}" var="sortingCriteria">
											<form:option value="${sortingCriteria.value}" >${sortingCriteria}</form:option>
										</c:forEach>
									</form:select>
									<spring:bind path="svcExecParams.sortingCriteria">
										<elitecoreError:showError
											errorMessage="${status.errorMessage}" errorId="svcExecParams-sortingCriteria_error"></elitecoreError:showError>
									</spring:bind>
								</div>
							</div>
						</div>
						</c:if>
						 
					</div>
				</div>
				<!-- Form content end here  -->
			</div>
			<!-- section-2 end here -->
