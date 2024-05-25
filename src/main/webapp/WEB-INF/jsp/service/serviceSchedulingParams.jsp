<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="/WEB-INF/ShowErrorTag.tld" prefix="elitecoreError"%>


	<div class="box box-warning">
				<div class="box-header with-border">
					<h3 class="box-title">
						<spring:message code="service.configuration.schedule.section.header" ></spring:message>
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
							<spring:message code="service.configuration.enable.scheduling.label" var="label" ></spring:message>
							<spring:message code="service.configuration.enable.scheduling.label.tooltip" var="tooltip" ></spring:message>
							<div class="form-group">
								<div class="table-cell-label">${label}</div>
								<div class="input-group ">
									<form:select path="serviceSchedulingParams.schedulingEnabled" cssClass="form-control table-cell input-sm" tabindex="4"
										id="serviceSchedulingParams-schedulingEnabled"
										data-toggle="tooltip" data-placement="bottom"
										 title="${tooltip }" onchange="disableExecuationInteval()">
										 
										<c:forEach var="trueFalseEnum" items="${trueFalseEnum}">
	                             			<form:option value="${trueFalseEnum.name}">${trueFalseEnum}</form:option>
	                             		</c:forEach>
										
									</form:select>
									<spring:bind path="serviceSchedulingParams.schedulingEnabled">
										<elitecoreError:showError
											errorMessage="${status.errorMessage}" errorId="serviceSchedulingParams-schedulingEnabled_error"></elitecoreError:showError>
									</spring:bind>
								</div>
							</div>
						</div>
						

						 <div class="col-md-6 no-padding">
							<spring:message	code="service.configuration.scheduling.type.label"	var="label" ></spring:message>
							<spring:message	code="service.configuration.scheduling.type.label.tooltip"	var="tooltip" ></spring:message>
							
							
							<div class="form-group">
								<div class="table-cell-label">${label}</div>
								<div class="input-group ">
									 <form:select path="serviceSchedulingParams.schType"
										cssClass="form-control table-cell input-sm" tabindex="4"
										id="serviceSchedulingParams-schType"
										data-toggle="tooltip" data-placement="bottom"
										 title="${tooltip }" onchange="changeScheduleParams()">
										<c:forEach items="${schType}" var="schType">
											<form:option value="${schType.value}">${schType}</form:option>
										</c:forEach>
									</form:select>
									<spring:bind path="serviceSchedulingParams.schType">
										<elitecoreError:showError
											errorMessage="${status.errorMessage}" errorId="serviceSchedulingParams-schType_error"></elitecoreError:showError>
									</spring:bind> 
								</div>
							</div>
						</div> 
						
						
						<div class="col-md-6 no-padding">
							<spring:message
								code="service.configuration.collection.day.label"
								var="label" ></spring:message>
							<spring:message
								code="service.configuration.collection.day.label.tooltip"
								var="tooltip" ></spring:message>
							<div class="form-group">
								<div class="table-cell-label">${label}</div>
								<div class="input-group ">
									<form:select path="serviceSchedulingParams.day" cssClass="form-control table-cell input-sm" tabindex="4"
										id="serviceSchedulingParams-day"
										data-toggle="tooltip" data-placement="bottom"
										 title="${tooltip }">
										
										<c:forEach items="${day}" var="day">
											<form:option value="${day.dayVal}" >${day}</form:option>
										</c:forEach>
									</form:select>
									<spring:bind path="serviceSchedulingParams.day">
										<elitecoreError:showError
											errorMessage="${status.errorMessage}" errorId="serviceSchedulingParams-day_error"></elitecoreError:showError>
									</spring:bind>
								</div>
							</div>
						</div>
						
						<div class="col-md-6 no-padding">
							<spring:message code="service.configuration.collection.date.label" var="label" ></spring:message>
							<spring:message code="service.configuration.collection.date.label.tooltip" var="tooltip" ></spring:message>
							<div class="form-group">
								<div class="table-cell-label">${label}</div>
								<div class="input-group ">
									<form:select path="serviceSchedulingParams.date" cssClass="form-control table-cell input-sm" tabindex="4"
										id="serviceSchedulingParams-date"
										data-toggle="tooltip" data-placement="bottom"
										 title="${tooltip }">
										
										<c:forEach items="${date}" var="date">
											<form:option value="${date.numVal}" >${date}</form:option>
										</c:forEach>
									</form:select>
									<spring:bind path="serviceSchedulingParams.date">
										<elitecoreError:showError
											errorMessage="${status.errorMessage}" errorId="serviceSchedulingParams-date_error"></elitecoreError:showError>
									</spring:bind>
								</div>
							</div>
						</div>
						
						<div class="col-md-6 no-padding">
							<spring:message code="service.configuration.collection.time.label" 	var="label" ></spring:message>
							<spring:message code="service.configuration.collection.time.label.tooltip" 	var="tooltip" ></spring:message>
							<div class="form-group">
								<div class="table-cell-label">${label}</div>
								<div class="input-group ">
									<form:input path="serviceSchedulingParams.time"
										cssClass="form-control table-cell input-sm" tabindex="4"
										id="serviceSchedulingParams-time" data-toggle="tooltip"
										data-placement="bottom" 
										title="${tooltip }" ></form:input>
									<spring:bind path="serviceSchedulingParams.time">
										<elitecoreError:showError
											errorMessage="${status.errorMessage}" errorId="serviceSchedulingParams-time_error"></elitecoreError:showError>
									</spring:bind>
								</div>
							</div>
						</div>
						
					</div>	
				</div>
				<!-- Form content end here  -->
		</div>
	
	<script type="text/javascript">
		
			function changeScheduleParams(){
				
				var schType=$("#serviceSchedulingParams-schType").find(":selected").text();
				
				if(schType == 'Daily'){
					$("#serviceSchedulingParams-date").prop("disabled", true);
					$("#serviceSchedulingParams-day").prop("disabled", true);
				}else if(schType == 'Weekly'){
					$("#serviceSchedulingParams-date").prop("disabled", true);
					$("#serviceSchedulingParams-day").prop("disabled", false);
				}else if (schType == 'Monthly'){
					$("#serviceSchedulingParams-date").prop("disabled", false);
					$("#serviceSchedulingParams-day").prop("disabled", true);
				}
			}
			
			function disableExecuationInteval(){
				
				var enableScheduling=$("#serviceSchedulingParams-schedulingEnabled").find(":selected").text();
				
				if(enableScheduling == 'False'){
					
					$("#svcExecParams-executionInterval").prop('readonly', false);
					$("#serviceSchedulingParams-schType").prop('disabled', true);
					$("#serviceSchedulingParams-time").prop('readonly', true);
					$("#serviceSchedulingParams-date").prop('disabled', true);
					$("#serviceSchedulingParams-day").prop('disabled', true);
					
				}else{
					$("#svcExecParams-executionInterval").prop('readonly', true);
					$("#serviceSchedulingParams-schType").prop('disabled', false);
					$("#serviceSchedulingParams-time").prop('readonly', false);
				}
				
			}
			$(document).ready(function() {
				disableExecuationInteval();
				changeScheduleParams();
				
     		});
	</script>		
