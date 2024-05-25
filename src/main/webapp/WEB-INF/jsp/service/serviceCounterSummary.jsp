<%@page import="com.elitecore.sm.common.constants.BaseConstants"%>
<%@page import="com.elitecore.sm.util.MapCache"%>
<%@page import="com.elitecore.sm.common.constants.EngineConstants"%>
<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="/WEB-INF/ShowErrorTag.tld" prefix="elitecoreError"%>

<style>
.table-cell-label {
	display: table-cell;
	line-height: 14px;
	vertical-align: middle;
	width: 280px;
}
</style>
<c:if test="${Boolean.TRUE.toString() ne  MapCache.getConfigValueAsObject(BaseConstants.KUBERNETES_ENV)}">
<div class="box box-warning">
	<div class="box-header with-border">
		<h3 class="box-title">
			<spring:message
				code="parsingService.summary.section.counter.status.heading" ></spring:message>
			<spring:message code="service.counterstatus.refresh" var="tooltip" ></spring:message>
			<a href="#" id="counterstatus" class="padding10" data-toggle="tooltip"
				data-placement="bottom" title="${tooltip}"
				onclick="getServiceCounterDetails('<%=ControllerConstants.GET_COLLECTION_SERVICE_COUNTER_STATUS%>','${serviceId}','${serviceType}','action');"><img
				src="<%=request.getContextPath()%>/img/refresh.png" class="vmiddle"
				alt="Refresh" /></a>
			<sec:authorize access="hasAuthority('UPDATE_SERVICE_INSTANCE')">
				<spring:message code="service.counterstatus.reset" var="tooltip" ></spring:message>
				<a href="#" id="resetcounter" data-toggle="tooltip" data-placement="bottom"
					title="${tooltip}"
					onclick="resetServiceCounterDetails('<%=ControllerConstants.RESET_COLLECTION_SERVICE_COUNTER_STATUS%>','${serviceId}','${serviceType}');"><img
					src="<%=request.getContextPath()%>/img/reset.png" class="vmiddle"
					alt="Refresh" /></a>
			</sec:authorize>
		</h3>
		<div class="box-tools pull-right" style="top: -3px;">
			<span class="title2 rightfield"
				style="text-decoration: none; font-size: 11px;"> <span
				style="color: #fe7b26;"> <c:choose>
						<c:when test="${(enableFileStats eq true)}">
							<i class="fa fa-file"></i>
						</c:when>
						<c:otherwise>
							<i class="fa fa-file grey"></i>
						</c:otherwise>
					</c:choose>
			</span> <spring:message code="parsingService.summary.state.file.label" ></spring:message>
			</span> <span class="title2 rightfield"
				style="text-decoration: none; font-size: 11px;"> <span
				style="color: #fe7b26;"> <c:choose>
						<c:when test="${(enableDBStats eq true)}">
							<i class="fa fa-fw fa-database"></i>
						</c:when>
						<c:otherwise>
							<i class="fa fa-fw fa-database grey"></i>
						</c:otherwise>
					</c:choose>
			</span> <spring:message code="parsingService.summary.state.db.label" ></spring:message>
			</span>
			<button class="btn btn-box-tool" data-widget="collapse">
				<i class="fa fa-minus"></i>
			</button>
		</div>
		<!-- /.box-tools -->
	</div>
	<!-- /.box-header end here -->
	<!-- Form content start here  -->

	<div class="box-body">
		<div class="fullwidth inline-form" id="counter_details">

			<c:if test="${serviceType eq 'NATFLOW_COLLECTION_SERVICE'}">
				<div class="col-md-6 no-padding">
					<spring:message
						code="service.summary.section.counter.totalNetFlowReceived.packets.label"
						var="tooltip" ></spring:message>
					<div class="form-group">
						<div class="table-cell-label">${tooltip}
							<span class="padding10">:</span>
						</div>
						<div class="input-group " id="total_NetFlowReceived_packets">

						</div>
					</div>
				</div>
			</c:if>
			<c:if test="${serviceType eq 'NATFLOW_COLLECTION_SERVICE'}">
				<div class="col-md-6 no-padding">
					<spring:message
						code="service.summary.section.counter.totalNetFlowProcessed.packets.label"
						var="tooltip" ></spring:message>
					<div class="form-group">
						<div class="table-cell-label">${tooltip}
							<span class="padding10">:</span>
						</div>
						<div class="input-group " id="total_NetFlowProcessed_packets">

						</div>
					</div>
				</div>
			</c:if>
			<c:if test="${serviceType eq 'NATFLOW_COLLECTION_SERVICE'}">
				<div class="col-md-6 no-padding">
					<spring:message
						code="service.summary.section.counter.totalNetFlow.request.label"
						var="tooltip" ></spring:message>
					<div class="form-group">
						<div class="table-cell-label">${tooltip}
							<span class="padding10">:</span>
						</div>
						<div class="input-group " id="total_NetFlow_request"></div>
					</div>
				</div>
			</c:if>
			<c:if test="${serviceType eq 'NATFLOW_COLLECTION_SERVICE'}">
				<div class="col-md-6 no-padding">
					<spring:message
						code="service.summary.section.counter.totalNetFlow.templates.request.label"
						var="tooltip" ></spring:message>
					<div class="form-group">
						<div class="table-cell-label">${tooltip}
							<span class="padding10">:</span>
						</div>
						<div class="input-group " id="total_NetFlow_template_request">


						</div>
					</div>
				</div>
			</c:if>

				<c:if test="${serviceType eq 'NATFLOW_COLLECTION_SERVICE'}">
				<div class="col-md-6 no-padding">
					<spring:message
						code="service.summary.section.counter.totalNetFlow.option.templates.request.label"
						var="tooltip" ></spring:message>
					<div class="form-group">
						<div class="table-cell-label">${tooltip}
							<span >:</span>
						</div>
						<div class="input-group " id="total_NetFlow_option_templates_request">
						</div>
					</div>
				</div>
			</c:if>

			<c:if test="${serviceType eq 'NATFLOW_COLLECTION_SERVICE'}">
				<div class="col-md-6 no-padding">
					<spring:message
						code="service.summary.section.counter.totalNetFlow.flowData.request.label"
						var="tooltip" ></spring:message>
					<div class="form-group">
						<div class="table-cell-label">${tooltip}
							<span class="padding10">:</span>
						</div>
						<div class="input-group " id="total_NetFlow_flowData_request">


						</div>
					</div>
				</div>
			</c:if>
			<c:if
				test="${(serviceType eq 'NATFLOW_COLLECTION_SERVICE') or (serviceType eq 'NATFLOWBINARY_COLLECTION_SERVICE') or (serviceType eq 'HTTP2_COLLECTION_SERVICE')}">
				<div class="col-md-6 no-padding">
					<spring:message
						code="service.summary.section.counter.totalNetFlow.malform.packets.label"
						var="tooltip" ></spring:message>
					<div class="form-group">
						<div class="table-cell-label">${tooltip}
							<span class="padding10">:</span>
						</div>
						<div class="input-group " id="total_NetFlow_Malform_packets">
						</div>
					</div>
				</div>
			</c:if>
			<c:if test="${serviceType eq 'NATFLOW_COLLECTION_SERVICE'}">
				<div class="col-md-6 no-padding">
					<spring:message
						code="service.summary.section.counter.totalNetFlow.pending.request.label"
						var="tooltip" ></spring:message>
					<div class="form-group">
						<div class="table-cell-label">${tooltip}
							<span class="padding10">:</span>
						</div>
						<div class="input-group " id="total_NetFlow_pending_request">


						</div>
					</div>
				</div>
			</c:if>
			<c:if
				test="${(serviceType eq 'IPLOG_PARSING_SERVICE') or (serviceType eq 'PARSING_SERVICE')}">
				<div class="col-md-6 no-padding">
					<spring:message
						code="parsingService.summary.section.counter.totalprocessedfiles.label"
						var="tooltip" ></spring:message>
					<div class="form-group">
						<div class="table-cell-label">${tooltip}
							<span class="padding10">:</span>
						</div>
						<div class="input-group " id="total_parsed_files"></div>
					</div>
				</div>
			</c:if>
			<c:if
				test="${(serviceType ne 'NATFLOW_COLLECTION_SERVICE') and (serviceType ne 'NATFLOWBINARY_COLLECTION_SERVICE')
				 and (serviceType ne 'DISTRIBUTION_SERVICE') and (serviceType ne 'SYSLOG_COLLECTION_SERVICE') and (serviceType ne 'HTTP2_COLLECTION_SERVICE') and (serviceType ne 'MQTT_COLLECTION_SERVICE') and (serviceType ne 'COAP_COLLECTION_SERVICE') and  (serviceType ne 'GTPPRIME_COLLECTION_SERVICE') and (serviceType ne 'PROCESSING_SERVICE') and (serviceType ne 'IPLOG_PARSING_SERVICE') and (serviceType ne 'PARSING_SERVICE') 
				 and (serviceType ne 'DIAMETER_COLLECTION_SERVICE') and (serviceType ne 'RADIUS_COLLECTION_SERVICE') }">
				<div class="col-md-6 no-padding">
					<spring:message
						code="parsingService.summary.section.counter.totalfiles.label"
						var="tooltip" ></spring:message>
					<div class="form-group">
						<div class="table-cell-label">${tooltip}
							<span class="padding10">:</span>
						</div>
						<div class="input-group " id="total_files"></div>
					</div>
				</div>
			</c:if>
			
			<c:if
				test="${(serviceType eq 'PROCESSING_SERVICE') or (serviceType eq 'DISTRIBUTION_SERVICE') }">
				<div class="col-md-6 no-padding">
					<spring:message
						code="parsingService.summary.section.counter.totalprocessedfiles.label"
						var="tooltip" ></spring:message>
					<div class="form-group">
						<div class="table-cell-label">${tooltip}
							<span class="padding10">:</span>
						</div>
						<div class="input-group " id="total_process_files"></div>
					</div>
				</div>
			</c:if>
			
			<c:if
				test="${(serviceType ne 'COLLECTION_SERVICE') and (serviceType ne 'SYSLOG_COLLECTION_SERVICE') and (serviceType ne 'HTTP2_COLLECTION_SERVICE') and (serviceType ne 'MQTT_COLLECTION_SERVICE')  and (serviceType ne 'COAP_COLLECTION_SERVICE') and
				(serviceType ne 'GTPPRIME_COLLECTION_SERVICE') and (serviceType ne 'DATA_CONSOLIDATION_SERVICE') and 
				(serviceType ne 'NATFLOW_COLLECTION_SERVICE') and (serviceType ne 'NATFLOWBINARY_COLLECTION_SERVICE') and 
				(serviceType ne 'DIAMETER_COLLECTION_SERVICE') and (serviceType ne 'RADIUS_COLLECTION_SERVICE')}">
				<div class="col-md-6 no-padding">
					<spring:message
						code="parsingService.summary.section.counter.totalrecords.label"
						var="tooltip" ></spring:message>
					<div class="form-group">
						<div class="table-cell-label">${tooltip}
							<span class="padding10">:</span>
						</div>
						<div class="input-group " id="total_records"></div>
					</div>
				</div>
			</c:if>
			
			<c:if
				test="${(serviceType eq 'AGGREGATION_SERVICE')}">
				<div class="col-md-6 no-padding">
					<spring:message
						code="aggregationService.summary.section.counter.totalaggregatedfiles.label"
						var="tooltip" ></spring:message>
					<div class="form-group">
						<div class="table-cell-label">${tooltip}
							<span class="padding10">:</span>
						</div>
						<div class="input-group " id="total_aggregated_files"></div>
					</div>
				</div>
			</c:if>
			
			<c:if
				test="${(serviceType eq 'AGGREGATION_SERVICE')}">
				<div class="col-md-6 no-padding">
					<spring:message
						code="aggregationService.summary.section.counter.totalaggregatedrecords.label"
						var="tooltip" ></spring:message>
					<div class="form-group">
						<div class="table-cell-label">${tooltip}
							<span class="padding10">:</span>
						</div>
						<div class="input-group " id="total_aggregated_records"></div>
					</div>
				</div>
			</c:if>
			
			<c:if
				test="${(serviceType ne 'NATFLOW_COLLECTION_SERVICE') and (serviceType ne 'NATFLOWBINARY_COLLECTION_SERVICE')
						and (serviceType ne 'SYSLOG_COLLECTION_SERVICE') and (serviceType ne 'HTTP2_COLLECTION_SERVICE') and (serviceType ne 'MQTT_COLLECTION_SERVICE') and (serviceType ne 'COAP_COLLECTION_SERVICE') and (serviceType ne 'GTPPRIME_COLLECTION_SERVICE')
						and (serviceType ne 'AGGREGATION_SERVICE') and (serviceType ne 'DIAMETER_COLLECTION_SERVICE') and (serviceType ne 'RADIUS_COLLECTION_SERVICE') }">
				<div class="col-md-6 no-padding">
					<c:if
						test="${(serviceType eq 'COLLECTION_SERVICE')}">
						<spring:message
							code="collectionService.summary.section.counter.totalcollected.files.label"
							var="tooltip" ></spring:message>
					</c:if>
					<c:if
						test="${(serviceType ne 'COLLECTION_SERVICE')}">
						<spring:message
							code="collectionService.summary.section.counter.totalsuccess.files.label"
							var="tooltip" ></spring:message>
					</c:if>
					<div class="form-group">
						<div class="table-cell-label">${tooltip}
							<span class="padding10">:</span>
						</div>
						<div class="input-group " id="total_success_files"></div>
					</div>
				</div>
			</c:if>
			<c:if
				test="${(serviceType ne 'NATFLOW_COLLECTION_SERVICE') and (serviceType ne 'NATFLOWBINARY_COLLECTION_SERVICE') and (serviceType ne 'HTTP2_COLLECTION_SERVICE') and (serviceType ne 'MQTT_COLLECTION_SERVICE') and (serviceType ne 'COAP_COLLECTION_SERVICE')
						and (serviceType ne 'SYSLOG_COLLECTION_SERVICE') and (serviceType ne 'COLLECTION_SERVICE') and (serviceType ne 'PARSING_SERVICE') and (serviceType ne 'IPLOG_PARSING_SERVICE') and (serviceType ne 'GTPPRIME_COLLECTION_SERVICE') and (serviceType ne 'DATA_CONSOLIDATION_SERVICE')
						and (serviceType ne 'AGGREGATION_SERVICE') and (serviceType ne 'DIAMETER_COLLECTION_SERVICE') and (serviceType ne 'RADIUS_COLLECTION_SERVICE') }">
				<div class="col-md-6 no-padding">
					<spring:message
						code="parsingService.summary.section.counter.totalsuccess.records.label"
						var="tooltip" ></spring:message>
					<div class="form-group">
						<div class="table-cell-label">${tooltip}
							<span class="padding10">:</span>
						</div>
						<div class="input-group " id="total_success_records"></div>
					</div>
				</div>
			</c:if>

			<c:if test="${(serviceType eq 'PROCESSING_SERVICE')}">
				<div class="col-md-6 no-padding">
					<spring:message
						code="parsingService.summary.section.counter.totalinvalid.records.label"
						var="tooltip" ></spring:message>
					<div class="form-group">
						<div class="table-cell-label">${tooltip}
							<span class="padding10">:</span>
						</div>
						<div class="input-group " id="total_invalid_records"></div>
					</div>
				</div>
			</c:if>

			<c:if test="${(serviceType eq 'PROCESSING_SERVICE')}">
				<div class="col-md-6 no-padding">
					<spring:message
						code="parsingService.summary.section.counter.totalfilter.records.label"
						var="tooltip" ></spring:message>
					<div class="form-group">
						<div class="table-cell-label">${tooltip}
							<span class="padding10">:</span>
						</div>
						<div class="input-group " id="total_filter_records"></div>
					</div>
				</div>
			</c:if>

			<c:if test="${(serviceType eq 'PROCESSING_SERVICE')}">
				<div class="col-md-6 no-padding">
					<spring:message
						code="parsingService.summary.section.counter.totalclone.records.label"
						var="tooltip" ></spring:message>
					<div class="form-group">
						<div class="table-cell-label">${tooltip}
							<span class="padding10">:</span>
						</div>
						<div class="input-group " id="total_clone_records"></div>
					</div>
				</div>
			</c:if>

			<c:if test="${(serviceType eq 'PROCESSING_SERVICE')}">
				<div class="col-md-6 no-padding">
					<spring:message
						code="parsingService.summary.section.counter.totalduplicate.records.label"
						var="tooltip" ></spring:message>
					<div class="form-group">
						<div class="table-cell-label">${tooltip}
							<span class="padding10">:</span>
						</div>
						<div class="input-group " id="total_duplicate_records"></div>
					</div>
				</div>
			</c:if>

			<c:if
				test="${(serviceType ne 'NATFLOW_COLLECTION_SERVICE') and (serviceType ne 'NATFLOWBINARY_COLLECTION_SERVICE')
						and (serviceType ne 'SYSLOG_COLLECTION_SERVICE') and (serviceType ne 'GTPPRIME_COLLECTION_SERVICE') 
						and (serviceType ne 'DIAMETER_COLLECTION_SERVICE') and (serviceType ne 'RADIUS_COLLECTION_SERVICE') and (serviceType ne 'HTTP2_COLLECTION_SERVICE') and (serviceType ne 'COAP_COLLECTION_SERVICE') and (serviceType ne 'MQTT_COLLECTION_SERVICE')  }">
				<div class="col-md-6 no-padding">
					<spring:message
						code="parsingService.summary.section.counter.totalfailure.files.label"
						var="tooltip" ></spring:message>
					<div class="form-group">
						<div class="table-cell-label">${tooltip}
							<span class="padding10">:</span>
						</div>
						<div class="input-group " id="total_fail_files"></div>
					</div>
				</div>
			</c:if>
			<c:if
				test="${(serviceType ne 'NATFLOW_COLLECTION_SERVICE') and (serviceType ne 'NATFLOWBINARY_COLLECTION_SERVICE') and (serviceType ne 'HTTP2_COLLECTION_SERVICE') and (serviceType ne 'MQTT_COLLECTION_SERVICE') and (serviceType ne 'COAP_COLLECTION_SERVICE')
						and (serviceType ne 'SYSLOG_COLLECTION_SERVICE') and (serviceType ne 'COLLECTION_SERVICE') and (serviceType ne 'PARSING_SERVICE') and (serviceType ne 'IPLOG_PARSING_SERVICE') and (serviceType ne 'GTPPRIME_COLLECTION_SERVICE') and (serviceType ne 'DATA_CONSOLIDATION_SERVICE')
						and (serviceType ne 'DIAMETER_COLLECTION_SERVICE') and (serviceType ne 'RADIUS_COLLECTION_SERVICE') }">
				<div class="col-md-6 no-padding">
					<spring:message
						code="parsingService.summary.section.counter.totalfailure.records.label"
						var="tooltip" ></spring:message>
					<div class="form-group">
						<div class="table-cell-label">${tooltip}
							<span class="padding10">:</span>
						</div>
						<div class="input-group " id="total_fail_records"></div>
					</div>
				</div>
			</c:if>
			<c:if
				test="${(serviceType ne 'NATFLOW_COLLECTION_SERVICE') and (serviceType ne 'NATFLOWBINARY_COLLECTION_SERVICE')
						and (serviceType ne 'SYSLOG_COLLECTION_SERVICE') and (serviceType ne 'GTPPRIME_COLLECTION_SERVICE') and (serviceType ne 'HTTP2_COLLECTION_SERVICE') and (serviceType ne 'MQTT_COLLECTION_SERVICE') and (serviceType ne 'COAP_COLLECTION_SERVICE')
						and (serviceType ne 'DIAMETER_COLLECTION_SERVICE') and (serviceType ne 'RADIUS_COLLECTION_SERVICE')}">
				<div class="col-md-6 no-padding">
					<spring:message
						code="parsingService.summary.section.counter.totalinprocess.files.label"
						var="tooltip" ></spring:message>
					<div class="form-group">
						<div class="table-cell-label">${tooltip}
							<span class="padding10">:</span>
						</div>
						<div class="input-group " id="total_inprocess_files"></div>
					</div>
				</div>
			</c:if>
			<c:if test="${(serviceType eq 'COAP_COLLECTION_SERVICE')}">
				<div class="col-md-6 no-padding">
					<spring:message
						code="coap.summary.section.counter.total.requests"
						var="tooltip" ></spring:message>
					<div class="form-group">
						<div class="table-cell-label">${tooltip}
							<span class="padding10">:</span>
						</div>
						<div class="input-group " id="total_requests"></div>
					</div>
				</div>
				
				<div class="col-md-6 no-padding">
					<spring:message
						code="coap.summary.section.counter.total.invalid.or.malformed.requests"
						var="tooltip" ></spring:message>
					<div class="form-group">
						<div class="table-cell-label">${tooltip}
							<span class="padding10">:</span>
						</div>
						<div class="input-group " id="total_invalid_malformed_requests"></div>
					</div>
				</div>
				
				<div class="col-md-6 no-padding">
					<spring:message
						code="coap.summary.section.counter.total.successful.responses"
						var="tooltip" ></spring:message>
					<div class="form-group">
						<div class="table-cell-label">${tooltip}
							<span class="padding10">:</span>
						</div>
						<div class="input-group " id="total_successful_responses"></div>
					</div>
				</div>						
			</c:if>
			<c:if
				test="${(serviceType eq 'NATFLOWBINARY_COLLECTION_SERVICE') or (serviceType eq 'HTTP2_COLLECTION_SERVICE') or (serviceType eq 'MQTT_COLLECTION_SERVICE')
						or (serviceType eq 'COAP_COLLECTION_SERVICE') or (serviceType eq 'SYSLOG_COLLECTION_SERVICE') and (serviceType ne 'DATA_CONSOLIDATION_SERVICE')}">
				<div class="col-md-6 no-padding">
					<spring:message
						code="service.summary.section.counter.totalReceived.packets.label"
						var="tooltip" ></spring:message>
					<div class="form-group">
						<div class="table-cell-label">${tooltip}
							<span class="padding10">:</span>
						</div>
						<div class="input-group " id="total_received_packets"></div>
					</div>
				</div>
			</c:if>
			
				<c:if test="${(serviceType eq 'IPLOG_PARSING_SERVICE') or (serviceType eq 'PARSING_SERVICE')}">
				<div class="col-md-6 no-padding">
					<spring:message
						code="service.summary.section.counter.total.packets.label"
						var="tooltip" ></spring:message>
					<div class="form-group">
						<div class="table-cell-label">${tooltip}
							<span class="padding10">:</span>
						</div>
						<div class="input-group " id="total_packets"></div>
					</div>
				</div>
			</c:if>
			
			<c:if test="${(serviceType eq 'PARSING_SERVICE')}">
				<div class="col-md-6 no-padding">
					<spring:message
						code="service.summary.section.counter.total.success.packets.label"
						var="tooltip" ></spring:message>
					<div class="form-group">
						<div class="table-cell-label">${tooltip}
							<span class="padding10">:</span>
						</div>
						<div class="input-group " id="total_success_packets"></div>
					</div>
				</div>
			</c:if>
			
			<c:if test="${(serviceType eq 'PARSING_SERVICE')}">
				<div class="col-md-6 no-padding">
					<spring:message
						code="service.summary.section.counter.total.malform.packets.label"
						var="tooltip" ></spring:message>
					<div class="form-group">
						<div class="table-cell-label">${tooltip}
							<span class="padding10">:</span>
						</div>
						<div class="input-group " id="total_malform_packets"></div>
					</div>
				</div>
			</c:if>
			
			<c:if
				test="${(serviceType eq 'NATFLOWBINARY_COLLECTION_SERVICE') or (serviceType eq 'HTTP2_COLLECTION_SERVICE') or (serviceType eq 'MQTT_COLLECTION_SERVICE')
						or (serviceType eq 'COAP_COLLECTION_SERVICE') or (serviceType eq 'SYSLOG_COLLECTION_SERVICE') and (serviceType ne 'DATA_CONSOLIDATION_SERVICE')}">
				<div class="col-md-6 no-padding">
					<spring:message
						code="service.summary.section.counter.totalProcessed.packets.label"
						var="tooltip" ></spring:message>
					<div class="form-group">
						<div class="table-cell-label">${tooltip}
							<span class="padding10">:</span>
						</div>
						<div class="input-group " id="total_processed_packets"></div>
					</div>
				</div>
			</c:if>
			<c:if
				test="${(serviceType eq 'NATFLOWBINARY_COLLECTION_SERVICE') or (serviceType eq 'HTTP2_COLLECTION_SERVICE') or (serviceType eq 'NATFLOWBINARY_COLLECTION_SERVICE')}">
				<div class="col-md-6 no-padding">
					<spring:message
						code="service.summary.section.counter.totalProcessed.records.label"
						var="tooltip" ></spring:message>
					<div class="form-group">
						<div class="table-cell-label">${tooltip}
							<span class="padding10">:</span>
						</div>
						<div class="input-group " id="total_processed_records"></div>
					</div>
				</div>
			</c:if>
			<c:if
				test="${(serviceType eq 'NATFLOW_COLLECTION_SERVICE') or (serviceType eq 'NATFLOWBINARY_COLLECTION_SERVICE') or (serviceType eq 'HTTP2_COLLECTION_SERVICE')
						or (serviceType eq 'COAP_COLLECTION_SERVICE') or (serviceType eq 'SYSLOG_COLLECTION_SERVICE') or (serviceType eq 'MQTT_COLLECTION_SERVICE')}">
				<div class="col-md-6 no-padding">
					<spring:message
						code="service.summary.section.counter.totalBuffered.packets.label"
						var="tooltip" ></spring:message>
					<div class="form-group">
						<div class="table-cell-label">${tooltip}
							<span class="padding10">:</span>
						</div>
						<div class="input-group " id="total_buffered_packets"></div>
					</div>
				</div>
			</c:if>

			<c:if
				test="${(serviceType eq 'NATFLOW_COLLECTION_SERVICE') or (serviceType eq 'NATFLOWBINARY_COLLECTION_SERVICE') or (serviceType eq 'HTTP2_COLLECTION_SERVICE')
						or (serviceType eq 'SYSLOG_COLLECTION_SERVICE') or (serviceType eq 'MQTT_COLLECTION_SERVICE')}">
				<div class="col-md-6 no-padding">
					<spring:message
						code="service.summary.section.counter.totalDropped.packets.label"
						var="tooltip" ></spring:message>
					<div class="form-group">
						<div class="table-cell-label">${tooltip}
							<span class="padding10">:</span>
						</div>
						<div class="input-group " id="total_dropped_packets"></div>
					</div>
				</div>
			</c:if>
			
			<c:if test="${(serviceType eq 'HTTP2_COLLECTION_SERVICE')}">
				<div class="col-md-6 no-padding">
					<spring:message
						code="service.summary.section.counter.total.responses.sent.label"
						var="tooltip" ></spring:message>
						
					<div class="form-group">
						<div class="table-cell-label">${tooltip}
							<span class="padding10">:</span>
						</div>
						<div class="input-group " id="total_respones_sent"></div>
					</div>
				</div>
			</c:if>
			
			<c:if test="${(serviceType eq 'NATFLOW_COLLECTION_SERVICE') or (serviceType eq 'NATFLOWBINARY_COLLECTION_SERVICE')}">
				<div class="col-md-6 no-padding">
					<spring:message
						code="service.summary.section.counter.totalforward.proxy.packets.label"
						var="tooltip" ></spring:message>
						
					<div class="form-group">
						<div class="table-cell-label">${tooltip}
							<span class="padding10">:</span>
						</div>
						<div class="input-group " id="total_forward_proxy_packets"></div>
					</div>
				</div>
			</c:if>
			
				<c:if
				test="${(serviceType eq 'SYSLOG_COLLECTION_SERVICE') or (serviceType eq 'MQTT_COLLECTION_SERVICE')}">
				<div class="col-md-6 no-padding">
					<spring:message
						code="service.summary.section.counter.totalMalformInvalid.packets.label"
						var="tooltip" ></spring:message>
					<div class="form-group">
						<div class="table-cell-label">${tooltip}
							<span class="padding10">:</span>
						</div>
						<div class="input-group " id="total_malform_packets"></div>
					</div>
				</div>
			</c:if>
			
			<c:if
				test="${(serviceType eq 'PARSING_SERVICE') or (serviceType eq 'IPLOG_PARSING_SERVICE')}">
				<div class="col-md-6 no-padding">
					<spring:message
						code="service.summary.section.counter.parsing.total.partially.successful.files.label"
						var="tooltip" ></spring:message>
					<div class="form-group">
						<div class="table-cell-label">${tooltip}
							<span class="padding10">:</span>
						</div>
						<div class="input-group " id="total_partially_successful_files">


						</div>
					</div>
				</div>
			</c:if>
			
			<c:if test="${(serviceType eq 'AGGREGATION_SERVICE')}">
				<div class="col-md-6 no-padding">
					<spring:message
						code="aggregationService.summary.section.counter.totalpartialsuccessfiles.label"
						var="tooltip" ></spring:message>
					<div class="form-group">
						<div class="table-cell-label">${tooltip}
							<span class="padding10">:</span>
						</div>
						<div class="input-group " id="total_partially_successful_files">


						</div>
					</div>
				</div>
			</c:if>
						
			<%-- <c:if test="${(serviceType ne 'COLLECTION_SERVICE') and (serviceType ne 'DIAMETER_COLLECTION_SERVICE') } ">
				<div class="col-md-6 no-padding">
					<spring:message
						code="parsingService.summary.section.counter.average.tps.label"
						var="tooltip" ></spring:message>
					<div class="form-group">
						<div class="table-cell-label">${tooltip}
							<span class="padding10">:</span>
						</div>
						<div class="input-group " id="average_tps"></div>
					</div>
				</div>
			</c:if> --%>

			<c:if
				test="${(serviceType ne 'SYSLOG_COLLECTION_SERVICE') and (serviceType ne 'NATFLOWBINARY_COLLECTION_SERVICE') and (serviceType ne 'NATFLOW_COLLECTION_SERVICE') and (serviceType ne 'GTPPRIME_COLLECTION_SERVICE')
				 and (serviceType ne 'AGGREGATION_SERVICE') and (serviceType ne 'DIAMETER_COLLECTION_SERVICE') and (serviceType ne 'RADIUS_COLLECTION_SERVICE') and (serviceType ne 'HTTP2_COLLECTION_SERVICE') and (serviceType ne 'COAP_COLLECTION_SERVICE') and (serviceType ne 'MQTT_COLLECTION_SERVICE') }">
				<div class="col-md-6 no-padding">
					<spring:message
						code="parsingService.summary.section.counter.totalpending.files.label"
						var="tooltip" ></spring:message>
					<div class="form-group">
						<div class="table-cell-label">${tooltip}
							<span class="padding10">:</span>
						</div>
						<div class="input-group " id="total_pending_files"></div>
					</div>
				</div>
			</c:if>
			<c:if
				test="${(serviceType ne 'COLLECTION_SERVICE') and (serviceType ne 'AGGREGATION_SERVICE') and (serviceType ne 'PARSING_SERVICE') and (serviceType ne 'IPLOG_PARSING_SERVICE') and (serviceType ne 'DISTRIBUTION_SERVICE')
							and (serviceType ne 'DIAMETER_COLLECTION_SERVICE') and (serviceType ne 'PROCESSING_SERVICE') and (serviceType ne 'DATA_CONSOLIDATION_SERVICE') and (serviceType ne 'RADIUS_COLLECTION_SERVICE') and (serviceType ne 'COAP_COLLECTION_SERVICE') and (serviceType ne 'NATFLOWBINARY_COLLECTION_SERVICE') }">
				<div class="col-md-6 no-padding">
					<spring:message
						code="parsingService.summary.section.counter.current.tps.label"
						var="tooltip" ></spring:message>
					<div class="form-group">
						<div class="table-cell-label">${tooltip}
							<span class="padding10">:</span>
						</div>
						<div class="input-group " id="current_tps"></div>
					</div>
				</div>
			</c:if>
			<c:if
				test="${(serviceType eq 'PARSING_SERVICE') or (serviceType eq 'PROCESSING_SERVICE') or (serviceType eq 'DISTRIBUTION_SERVICE') 
				or (serviceType eq 'DATA_CONSOLIDATION_SERVICE')}">
				<div class="col-md-6 no-padding">
					<spring:message
						code="parsingService.summary.section.counter.average.tps.label"
						var="tooltip" ></spring:message>
					<div class="form-group">
						<div class="table-cell-label">${tooltip}
							<span class="padding10">:</span>
						</div>
						<div class="input-group " id="avg_tps"></div>
					</div>
				</div>
			</c:if>
			<c:if test="${serviceType eq 'GTPPRIME_COLLECTION_SERVICE'}">
				<div class="col-md-6 no-padding">
					<spring:message
						code="gtpprime.summary.section.counter.total.request"
						var="tooltip" ></spring:message>
					<div class="form-group">
						<div class="table-cell-label">${tooltip}
							<span class="padding10">:</span>
						</div>
						<div class="input-group " id="total_request"></div>
					</div>
				</div>
			</c:if>
			<c:if test="${serviceType eq 'GTPPRIME_COLLECTION_SERVICE'}">
				<div class="col-md-6 no-padding">
					<spring:message
						code="gtpprime.summary.section.counter.total.echo.request"
						var="tooltip" ></spring:message>
					<div class="form-group">
						<div class="table-cell-label">${tooltip}
							<span class="padding10">:</span>
						</div>
						<div class="input-group " id="total_echo_request"></div>
					</div>
				</div>
			</c:if>
			<c:if test="${serviceType eq 'GTPPRIME_COLLECTION_SERVICE'}">
				<div class="col-md-6 no-padding">
					<spring:message
						code="gtpprime.summary.section.counter.total.node.alive.request"
						var="tooltip" ></spring:message>
					<div class="form-group">
						<div class="table-cell-label">${tooltip}
							<span class="padding10">:</span>
						</div>
						<div class="input-group " id="total_node_alive_request"></div>
					</div>
				</div>
			</c:if>
			<c:if test="${serviceType eq 'GTPPRIME_COLLECTION_SERVICE'}">
				<div class="col-md-6 no-padding">
					<spring:message
						code="gtpprime.summary.section.counter.total.redirection.request"
						var="tooltip" ></spring:message>
					<div class="form-group">
						<div class="table-cell-label">${tooltip}
							<span class="padding10">:</span>
						</div>
						<div class="input-group " id="total_redirection_request"></div>
					</div>
				</div>
			</c:if>
			<c:if test="${serviceType eq 'GTPPRIME_COLLECTION_SERVICE'}">
				<div class="col-md-6 no-padding">
					<spring:message
						code="gtpprime.summary.section.counter.total.data.record.transfer.request"
						var="tooltip" ></spring:message>
					<div class="form-group">
						<div class="table-cell-label">${tooltip}
							<span>:</span>
						</div>
						<div class="input-group " id="total_data_record_transfer_request">
						</div>
					</div>
				</div>
			</c:if>
			<c:if test="${serviceType eq 'GTPPRIME_COLLECTION_SERVICE'}">
				<div class="col-md-6 no-padding">
					<spring:message
						code="gtpprime.summary.section.counter.total.data.record.transfer.records"
						var="tooltip" ></spring:message>
					<div class="form-group">
						<div class="table-cell-label">${tooltip}
							<span class="padding10">:</span>
						</div>
						<div class="input-group " id="total_data_record_transfer_records">
						</div>
					</div>
				</div>
			</c:if>
			<c:if test="${serviceType eq 'GTPPRIME_COLLECTION_SERVICE'}">
				<div class="col-md-6 no-padding">
					<spring:message
						code="gtpprime.summary.section.counter.total.redirection.response.success"
						var="tooltip" ></spring:message>
					<div class="form-group">
						<div class="table-cell-label">${tooltip}
							<span>:</span>
						</div>
						<div class="input-group " id="total_redirection_response_success">
						</div>
					</div>
				</div>
			</c:if>
			<c:if test="${serviceType eq 'GTPPRIME_COLLECTION_SERVICE'}">
				<div class="col-md-6 no-padding">
					<spring:message
						code="gtpprime.summary.section.counter.total.redirection.response.failure"
						var="tooltip" ></spring:message>
					<div class="form-group">
						<div class="table-cell-label">${tooltip}
							<span class="padding10">:</span>
						</div>
						<div class="input-group " id="total_redirection_response_failure">
						</div>
					</div>
				</div>
			</c:if>
			<c:if test="${serviceType eq 'GTPPRIME_COLLECTION_SERVICE'}">
				<div class="col-md-6 no-padding">
					<spring:message
						code="gtpprime.summary.section.counter.total.echo.response"
						var="tooltip" ></spring:message>
					<div class="form-group">
						<div class="table-cell-label">${tooltip}
							<span class="padding10">:</span>
						</div>
						<div class="input-group " id="total_echo_response"></div>
					</div>
				</div>
			</c:if>
			<c:if test="${serviceType eq 'GTPPRIME_COLLECTION_SERVICE'}">
				<div class="col-md-6 no-padding">
					<spring:message
						code="gtpprime.summary.section.counter.total.node.alive.response"
						var="tooltip" ></spring:message>
					<div class="form-group">
						<div class="table-cell-label">${tooltip}
							<span class="padding10">:</span>
						</div>
						<div class="input-group " id="total_node_alive_response"></div>
					</div>
				</div>
			</c:if>
			<c:if test="${serviceType eq 'GTPPRIME_COLLECTION_SERVICE'}">
				<div class="col-md-6 no-padding">
					<spring:message
						code="gtpprime.summary.section.counter.total.data.record.transfer.responsesuccess"
						var="tooltip" ></spring:message>
					<div class="form-group">
						<div class="table-cell-label">${tooltip}
							<span>:</span>
						</div>
						<div class="input-group "
							id="total_data_record_transfer_responsesuccess"></div>
					</div>
				</div>
			</c:if>
			<c:if test="${serviceType eq 'GTPPRIME_COLLECTION_SERVICE'}">
				<div class="col-md-6 no-padding">
					<spring:message
						code="gtpprime.summary.section.counter.total.data.record.transfer.responsefailure"
						var="tooltip" ></spring:message>
					<div class="form-group">
						<div class="table-cell-label">${tooltip}
							<span class="padding10">:</span>
						</div>
						<div class="input-group "
							id="total_data_record_transfer_responsefailure"></div>
					</div>
				</div>
			</c:if>
			<c:if test="${serviceType eq 'GTPPRIME_COLLECTION_SERVICE'}">
				<div class="col-md-6 no-padding">
					<spring:message
						code="gtpprime.summary.section.counter.total.version.not.supported.response"
						var="tooltip" ></spring:message>
					<div class="form-group">
						<div class="table-cell-label">${tooltip}
							<span class="padding10">:</span>
						</div>
						<div class="input-group "
							id="total_version_not_supported_response"></div>
					</div>
				</div>
			</c:if>
			<c:if test="${serviceType eq 'GTPPRIME_COLLECTION_SERVICE'}">
				<div class="col-md-6 no-padding">
					<spring:message
						code="gtpprime.summary.section.counter.total.invalid.client.request"
						var="tooltip" ></spring:message>
					<div class="form-group">
						<div class="table-cell-label">${tooltip}
							<span class="padding10">:</span>
						</div>
						<div class="input-group " id="total_invalid_client_request">
						</div>
					</div>
				</div>
			</c:if>
			<c:if test="${serviceType eq 'GTPPRIME_COLLECTION_SERVICE'}">
				<div class="col-md-6 no-padding">
					<spring:message
						code="gtpprime.summary.section.counter.total.dropped.echo.request"
						var="tooltip" ></spring:message>
					<div class="form-group">
						<div class="table-cell-label">${tooltip}
							<span class="padding10">:</span>
						</div>
						<div class="input-group " id="total_dropped_echo_request"></div>
					</div>
				</div>
			</c:if>
			<c:if test="${serviceType eq 'GTPPRIME_COLLECTION_SERVICE'}">
				<div class="col-md-6 no-padding">
					<spring:message
						code="gtpprime.summary.section.counter.total.dropped.node.alive.request"
						var="tooltip" ></spring:message>
					<div class="form-group">
						<div class="table-cell-label">${tooltip}
							<span class="padding10">:</span>
						</div>
						<div class="input-group " id="total_dropped_node_alive_request">
						</div>
					</div>
				</div>
			</c:if>
			<c:if test="${serviceType eq 'GTPPRIME_COLLECTION_SERVICE'}">
				<div class="col-md-6 no-padding">
					<spring:message
						code="gtpprime.summary.section.counter.total.dropped.redirection.request"
						var="tooltip" ></spring:message>
					<div class="form-group">
						<div class="table-cell-label">${tooltip}
							<span class="padding10">:</span>
						</div>
						<div class="input-group " id="total_dropped_redirection_request">
						</div>
					</div>
				</div>
			</c:if>
			<c:if test="${serviceType eq 'GTPPRIME_COLLECTION_SERVICE'}">
				<div class="col-md-6 no-padding">
					<spring:message
						code="gtpprime.summary.section.counter.total.dropped.data.transfer.request"
						var="tooltip" ></spring:message>
					<div class="form-group">
						<div class="table-cell-label">${tooltip}
							<span class="padding10">:</span>
						</div>
						<div class="input-group " id="total_dropped_data_transfer_request">
						</div>
					</div>
				</div>
			</c:if>
			<c:if test="${serviceType eq 'GTPPRIME_COLLECTION_SERVICE'}">
				<div class="col-md-6 no-padding">
					<spring:message
						code="gtpprime.summary.section.counter.total.dropped.request"
						var="tooltip" ></spring:message>
					<div class="form-group">
						<div class="table-cell-label">${tooltip}
							<span class="padding10">:</span>
						</div>
						<div class="input-group " id="total_dropped_request"></div>
					</div>
				</div>
			</c:if>
			<c:if test="${serviceType eq 'GTPPRIME_COLLECTION_SERVICE'}">
				<div class="col-md-6 no-padding">
					<spring:message
						code="gtpprime.summary.section.counter.total.malformed.request.packet"
						var="tooltip" ></spring:message>
					<div class="form-group">
						<div class="table-cell-label">${tooltip}
							<span class="padding10">:</span>
						</div>
						<div class="input-group " id="total_malformed_request_packet">
						</div>
					</div>
				</div>
			</c:if>
			<c:if test="${serviceType eq 'GTPPRIME_COLLECTION_SERVICE'}">
				<div class="col-md-6 no-padding">
					<spring:message
						code="gtpprime.summary.section.counter.total.malformed.echo.response.received"
						var="tooltip" ></spring:message>
					<div class="form-group">
						<div class="table-cell-label">${tooltip}
							<span class="padding10">:</span>
						</div>
						<div class="input-group "
							id="total_malformed_echo_response_received"></div>
					</div>
				</div>
			</c:if>
			<c:if test="${serviceType eq 'GTPPRIME_COLLECTION_SERVICE'}">
				<div class="col-md-6 no-padding">
					<spring:message
						code="gtpprime.summary.section.counter.total.malformed.node.alive.response.received"
						var="tooltip" ></spring:message>
					<div class="form-group">
						<div class="table-cell-label">${tooltip}
							<span>:</span>
						</div>
						<div class="input-group "
							id="total_malformed_node_alive_response_received"></div>
					</div>
				</div>
			</c:if>
			<c:if test="${serviceType eq 'GTPPRIME_COLLECTION_SERVICE'}">
				<div class="col-md-6 no-padding">
					<spring:message
						code="gtpprime.summary.section.counter.total.echo.request.sent"
						var="tooltip" ></spring:message>
					<div class="form-group">
						<div class="table-cell-label">${tooltip}
							<span class="padding10">:</span>
						</div>
						<div class="input-group " id="total_echo_request_sent"></div>
					</div>
				</div>
			</c:if>
			<c:if test="${serviceType eq 'GTPPRIME_COLLECTION_SERVICE'}">
				<div class="col-md-6 no-padding">
					<spring:message
						code="gtpprime.summary.section.counter.total.echo.request.retry"
						var="tooltip" ></spring:message>
					<div class="form-group">
						<div class="table-cell-label">${tooltip}
							<span class="padding10">:</span>
						</div>
						<div class="input-group " id="total_echo_request_retry"></div>
					</div>
				</div>
			</c:if>
			<c:if test="${serviceType eq 'GTPPRIME_COLLECTION_SERVICE'}">
				<div class="col-md-6 no-padding">
					<spring:message
						code="gtpprime.summary.section.counter.total.node.alive.request.sent"
						var="tooltip" ></spring:message>
					<div class="form-group">
						<div class="table-cell-label">${tooltip}
							<span class="padding10">:</span>
						</div>
						<div class="input-group " id="total_node_alive_request_sent">
						</div>
					</div>
				</div>
			</c:if>
			<c:if test="${serviceType eq 'GTPPRIME_COLLECTION_SERVICE'}">
				<div class="col-md-6 no-padding">
					<spring:message
						code="gtpprime.summary.section.counter.total.node.alive.request.retry"
						var="tooltip" ></spring:message>
					<div class="form-group">
						<div class="table-cell-label">${tooltip}
							<span class="padding10">:</span>
						</div>
						<div class="input-group " id="total_node_alive_request_retry">
						</div>
					</div>
				</div>
			</c:if>
			<c:if test="${serviceType eq 'GTPPRIME_COLLECTION_SERVICE'}">
				<div class="col-md-6 no-padding">
					<spring:message
						code="gtpprime.summary.section.counter.total.echo.response.received"
						var="tooltip" ></spring:message>
					<div class="form-group">
						<div class="table-cell-label">${tooltip}
							<span class="padding10">:</span>
						</div>
						<div class="input-group " id="total_echo_response_received">
						</div>
					</div>
				</div>
			</c:if>
			<c:if test="${serviceType eq 'GTPPRIME_COLLECTION_SERVICE'}">
				<div class="col-md-6 no-padding">
					<spring:message
						code="gtpprime.summary.section.counter.total.node.alive.response.received"
						var="tooltip" ></spring:message>
					<div class="form-group">
						<div class="table-cell-label">${tooltip}
							<span class="padding10">:</span>
						</div>
						<div class="input-group " id="total_node_alive_response_received">
						</div>
					</div>
				</div>
			</c:if>
			<c:if
				test="${(serviceType eq 'PARSING_SERVICE') or (serviceType eq 'IPLOG_PARSING_SERVICE')}">
				<div class="col-md-6 no-padding">
					<spring:message
						code="service.summary.section.counter.parsing.total.malform.flowsets.label"
						var="tooltip" ></spring:message>
					<div class="form-group">
						<div class="table-cell-label">${tooltip}
							<span class="padding10">:</span>
						</div>
						<div class="input-group " id="total_malform_flowset"></div>
					</div>
				</div>
				
				<div class="col-md-6 no-padding">
					<spring:message
						code="parsingService.summary.section.counter.average.eps.label"
						var="tooltip" ></spring:message>
					<div class="form-group">
						<div class="table-cell-label">${tooltip}
							<span class="padding10">:</span>
						</div>
						<div class="input-group " id="avg_eps"></div>
					</div>
				</div>
			</c:if>
			
			<c:if test="${serviceType eq 'DISTRIBUTION_SERVICE'}">
				<div class="col-md-6 no-padding">
					<spring:message
						code="distribution.summary.section.counter.file.merge.status"
						var="tooltip" ></spring:message>
					<div class="form-group">
						<div class="table-cell-label">${tooltip}
							<span class="padding10">:</span>
						</div>
						<div class="input-group " id="file_merge_status_summary">
						<span id="file_merge_status_summary_span"></span>
						</div>
					</div>
				</div>
			</c:if>
			
			<c:if test="${serviceType eq 'COLLECTION_SERVICE'}">
				<div class="col-md-6 no-padding">
					<spring:message
						code="collectionService.summary.section.counter.totalmissing.files.label"
						var="tooltip" ></spring:message>
					<div class="form-group">
						<div class="table-cell-label">${tooltip}
							<span class="padding10">:</span>
						</div>
						<div class="input-group " id="total_missing_files"></div>
					</div>
				</div>
				<div class="col-md-6 no-padding">
					<spring:message
						code="collectionService.summary.section.counter.totalduplicate.files.label"
						var="tooltip" ></spring:message>
					<div class="form-group">
						<div class="table-cell-label">${tooltip}
							<span class="padding10">:</span>
						</div>
						<div class="input-group " id="total_duplicate_files"></div>
					</div>
				</div>
				<div class="col-md-6 no-padding">
					<spring:message
						code="collectionService.summary.section.counter.totalinvalid.files.label"
						var="tooltip" ></spring:message>
					<div class="form-group">
						<div class="table-cell-label">${tooltip}
							<span class="padding10">:</span>
						</div>
						<div class="input-group " id="total_invalid_files"></div>
					</div>
				</div>
			</c:if> 
			
			<c:if test="${(serviceType eq 'DIAMETER_COLLECTION_SERVICE')}">
				<div class="col-md-6 no-padding">
					<spring:message
						code="diameter.summary.section.counter.total.requests.received"
						var="tooltip" ></spring:message>
					<div class="form-group">
						<div class="table-cell-label">${tooltip}
							<span class="padding10">:</span>
						</div>
						<div class="input-group " id="total_requests_received"></div>
					</div>
				</div>
				
				<div class="col-md-6 no-padding">
					<spring:message
						code="diameter.summary.section.counter.total.responses.sent"
						var="tooltip" ></spring:message>
					<div class="form-group">
						<div class="table-cell-label">${tooltip}
							<span class="padding10">:</span>
						</div>
						<div class="input-group " id="total_responses_sent"></div>
					</div>
				</div>
				
				<div class="col-md-6 no-padding">
					<spring:message
						code="diameter.summary.section.counter.total.duplicate.requests"
						var="tooltip" ></spring:message>
					<div class="form-group">
						<div class="table-cell-label">${tooltip}
							<span class="padding10">:</span>
						</div>
						<div class="input-group " id="total_duplicate_requests"></div>
					</div>
				</div>
				
				<div class="col-md-6 no-padding">
					<spring:message
						code="diameter.summary.section.counter.total.requests.dropped"
						var="tooltip" ></spring:message>
					<div class="form-group">
						<div class="table-cell-label">${tooltip}
							<span class="padding10">:</span>
						</div>
						<div class="input-group " id="total_requests_dropped"></div>
					</div>
				</div>
				
				<div class="col-md-6 no-padding">
					<spring:message
						code="diameter.summary.section.counter.total.malformed.packets"
						var="tooltip" ></spring:message>
					<div class="form-group">
						<div class="table-cell-label">${tooltip}
							<span class="padding10">:</span>
						</div>
						<div class="input-group " id="total_malformed_packets"></div>
					</div>
				</div>
				
			</c:if>
			<c:if test="${(serviceType eq 'COAP_COLLECTION_SERVICE')}">
				<div class="col-md-6 no-padding">
					<spring:message
						code="coap.summary.section.counter.total.dropped.responses"
						var="tooltip" ></spring:message>
					<div class="form-group">
						<div class="table-cell-label">${tooltip}
							<span class="padding10">:</span>
						</div>
						<div class="input-group " id="total_dropped_responses"></div>
					</div>
				</div>
				<div class="col-md-6 no-padding">
					<spring:message
						code="coap.summary.section.counter.total.produced.kafka.packets"
						var="tooltip" ></spring:message>
					<div class="form-group">
						<div class="table-cell-label">${tooltip}
							<span class="padding10">:</span>
						</div>
						<div class="input-group " id="total_produced_kafka_packets"></div>
					</div>
				</div>
				<div class="col-md-6 no-padding">
					<spring:message
						code="coap.summary.section.counter.total.wrote.kafka.packets.in.file"
						var="tooltip" ></spring:message>
					<div class="form-group">
						<div class="table-cell-label">${tooltip}
							<span class="padding10">:</span>
						</div>
						<div class="input-group " id="total_wrote_packets_in_file"></div>
					</div>
				</div>	
				<div class="col-md-6 no-padding">
					<spring:message
						code="coap.summary.section.counter.current.tps"
						var="tooltip" ></spring:message>
					<div class="form-group">
						<div class="table-cell-label">${tooltip}
							<span class="padding10">:</span>
						</div>
						<div class="input-group " id="coap_current_tps"></div>
					</div>
				</div>
			</c:if>
			
			<c:if test="${(serviceType eq 'RADIUS_COLLECTION_SERVICE')}">
				<div class="col-md-6 no-padding">
					<div class="form-group">
						<div class="table-cell-label"><spring:message code="radius.collection.service.total.receiverd.radius.packets" ></spring:message> :</div>
						<div class="input-group " id="total_received_radius_packets"></div>
					</div>
				</div>
				
				<div class="col-md-6 no-padding">
					<div class="form-group">
						<div class="table-cell-label"><spring:message code="radius.collection.service.total.processed.radius.packets"></spring:message> :</div>
						<div class="input-group " id="total_processed_radius_packets"></div>
					</div>
				</div>
				
				<div class="col-md-6 no-padding">
					<div class="form-group">
						<div class="table-cell-label"><spring:message code="radius.collection.service.total.receiverd.radius.requests"></spring:message> :</div>
						<div class="input-group " id="total_received_radius_requests"></div>
					</div>
				</div>
				
				<div class="col-md-6 no-padding">
					<div class="form-group">
						<div class="table-cell-label"><spring:message code="radius.collection.service.total.malform.requests" ></spring:message> :</div>
						<div class="input-group " id="total_malform_requests"></div>
					</div>
				</div>
				<div class="col-md-6 no-padding">
					<div class="form-group">
						<div class="table-cell-label"><spring:message code="radius.collection.service.total.invalid.client.requests" ></spring:message> :</div>
						<div class="input-group " id="total_invalid_client_requests"></div>
					</div>
				</div>
				<div class="col-md-6 no-padding">
					<div class="form-group">
						<div class="table-cell-label"><spring:message code="radius.collection.service.average.tps"></spring:message> :</div>
						<div class="input-group " id="radius_average_tps"></div>
					</div>
				</div>
				<div class="col-md-6 no-padding">
					<div class="form-group">
						<div class="table-cell-label"><spring:message code="radius.collection.service.current.tps" ></spring:message> :</div>
						<div class="input-group " id="radius_current_tps"></div>
					</div>
				</div>
			</c:if>
			
			<!-- KPI additions (pps, bps, opps, opbs) for natflow collector -->
		    <c:if test="${(serviceType eq 'NATFLOWBINARY_COLLECTION_SERVICE')}">
		    
		    <div class="col-md-6 no-padding">
					<spring:message
						code="natflowBinaryservice.summary.section.counter.totalOverProcessed.packets.label"
						var="tooltip" ></spring:message>
						
					<div class="form-group">
						<div class="table-cell-label">${tooltip}
							<span class="padding10">:</span>
						</div>
						<div class="input-group " id="total_over_processed_packets"></div>
					</div>
				</div>
				
				<div class="col-md-6 no-padding">
					<spring:message
						code="natflowBinaryservice.summary.section.counter.totalReceived.bits.label"
						var="tooltip" ></spring:message>
						
					<div class="form-group">
						<div class="table-cell-label">${tooltip}
							<span class="padding10">:</span>
						</div>
						<div class="input-group " id="total_received_bits"></div>
					</div>
				</div>
				
				<div class="col-md-6 no-padding">
					<spring:message
						code="natflowBinaryservice.summary.section.counter.totalProcessed.bits.label"
						var="tooltip" ></spring:message>
						
					<div class="form-group">
						<div class="table-cell-label">${tooltip}
							<span class="padding10">:</span>
						</div>
						<div class="input-group " id="total_processed_bits"></div>
					</div>
				</div>
				
				<div class="col-md-6 no-padding">
					<spring:message
						code="natflowBinaryservice.summary.section.counter.totalOverProcessed.bits.label"
						var="tooltip" ></spring:message>
						
					<div class="form-group">
						<div class="table-cell-label">${tooltip}
							<span class="padding10">:</span>
						</div>
						<div class="input-group " id="total_over_processed_bits"></div>
					</div>
				</div>
				
				<div class="col-md-6 no-padding">
					<spring:message
						code="natflowBinaryService.summary.section.counter.current.pps.label"
						var="tooltip" ></spring:message>
						
					<div class="form-group">
						<div class="table-cell-label">${tooltip}
							<span class="padding10">:</span>
						</div>
						<div class="input-group " id="current_pps"></div>
					</div>
				</div>
				
				<div class="col-md-6 no-padding">
					<spring:message
						code="natflowBinarService.summary.section.counter.average.pps.label"
						var="tooltip" ></spring:message>
						
					<div class="form-group">
						<div class="table-cell-label">${tooltip}
							<span class="padding10">:</span>
						</div>
						<div class="input-group " id="avg_pps"></div>
					</div>
				</div>
				
				<div class="col-md-6 no-padding">
					<spring:message
						code="natflowBinarService.summary.section.counter.current.opps.label"
						var="tooltip" ></spring:message>
						
					<div class="form-group">
						<div class="table-cell-label">${tooltip}
							<span class="padding10">:</span>
						</div>
						<div class="input-group " id="current_opps"></div>
					</div>
				</div>
				
				<div class="col-md-6 no-padding">
					<spring:message
						code="natflowBinarService.summary.section.counter.avg.opps.label"
						var="tooltip" ></spring:message>
						
					<div class="form-group">
						<div class="table-cell-label">${tooltip}
							<span class="padding10">:</span>
						</div>
						<div class="input-group " id="avg_opps"></div>
					</div>
				</div>
				
				<div class="col-md-6 no-padding">
					<spring:message
						code="natflowBinaryService.summary.section.counter.current.bps.label"
						var="tooltip" ></spring:message>
						
					<div class="form-group">
						<div class="table-cell-label">${tooltip}
							<span class="padding10">:</span>
						</div>
						<div class="input-group " id="current_bps"></div>
					</div>
				</div>
				
				<div class="col-md-6 no-padding">
					<spring:message
						code="natflowBinaryService.summary.section.counter.average.bps.label"
						var="tooltip" ></spring:message>
						
					<div class="form-group">
						<div class="table-cell-label">${tooltip}
							<span class="padding10">:</span>
						</div>
						<div class="input-group " id="avg_bps"></div>
					</div>
				</div>
				
				<div class="col-md-6 no-padding">
					<spring:message
						code="natflowBinaryService.summary.section.counter.current.opbs.label"
						var="tooltip" ></spring:message>
						
					<div class="form-group">
						<div class="table-cell-label">${tooltip}
							<span class="padding10">:</span>
						</div>
						<div class="input-group " id="current_opbs"></div>
					</div>
				</div>
				
				<div class="col-md-6 no-padding">
					<spring:message
						code="natflowBinaryService.summary.section.counter.average.opbs.label"
						var="tooltip" ></spring:message>
						
					<div class="form-group">
						<div class="table-cell-label">${tooltip}
							<span class="padding10">:</span>
						</div>
						<div class="input-group " id="avg_opbs"></div>
					</div>
				</div>
				
			</c:if>
			
		</div>
	</div>
	<!-- Form content end here  -->
</div>
</c:if>

<script type="text/javascript">
	
	var SvcType;
	
	function getServiceCounterDetails(actionUrl,serviceId,serviceType,actionType){
		//resetWarningDisplay();
		//clearAllMessages();
		SvcType=serviceType;
		 $.ajax({
			url: actionUrl,
			cache: false,
			async: true,
			dataType: 'json',
			type: "POST",
			data:
			 {
				"serviceId"     : '${serviceId}'
			 }, 
			
			success: function(data){
				var response = eval(data);
				var responseCode = response.code; 
				var responseMsg = response.msg; 
				var responseObject = response.object;
				if(responseCode == "200"){
					// Commented below lines for MED-4665
					//resetWarningDisplay();
					//clearAllMessages();
				
					var couterJson = responseObject["COUNTER_STATUS"];
					var servId = responseObject["servInstanceId"];
					var serviceInstanceName =  serviceType+"-"+servId;
					$.each(couterJson,function(index,counterStatus){
						getCounterStatusByService(serviceType,counterStatus,serviceInstanceName);
			 		});					
				}else{
					// Commented below lines for MED-4665
					//resetWarningDisplay();
					//clearAllMessages();
					if(actionType != 'ONLOAD'){
						//showErrorMsg(responseMsg);	
					}
				}
			},
		    error: function (xhr,st,err){
				handleGenericError(xhr,st,err);
			}
		}); 
	}
	
	function resetServiceCounterDetails(actionUrl,serviceId,serviceType){
		resetWarningDisplay();
		clearAllMessages();
		SvcType=serviceType;
		 $.ajax({
			url: actionUrl,
			cache: false,
			async: true,
			dataType: 'json',
			type: "POST",
			data:
			 {
				"serviceId"     : '${serviceId}'
			 }, 
			
			success: function(data){
				var response = eval(data);
				var responseCode = response.code; 
				var responseMsg = response.msg; 
				var responseObject = response.object;
				if(responseCode == "200"){
					resetWarningDisplay();
					clearAllMessages();
					responseReset = responseObject["RESET_COUNTER"];
					//$("#counter_details").html(responseObject);
					
					var servId = responseObject["servInstanceId"];
					<%-- getServiceCounterDetails('<%=ControllerConstants.GET_COLLECTION_SERVICE_COUNTER_STATUS%>',serviceId,serviceType,'action');  --%>
					var serviceInstanceName =  serviceType+"-"+servId;
					$.each(responseReset,function(index,counterStatus){
			
						getCounterStatusByService(serviceType,counterStatus,serviceInstanceName);
				
					});
				}
					else{
					resetWarningDisplay();
					clearAllMessages();
					showErrorMsg(responseMsg);
				}
			},
		    error: function (xhr,st,err){
				handleGenericError(xhr,st,err);
			}
		}); 
	}
	
	
	
	function getCounterStatusByService(serviceType,counterStatus,serviceInstanceName)
	{
	
			if(serviceType == 'DISTRIBUTION_SERVICE' && counterStatus[serviceInstanceName] != null){
				var counterObject = counterStatus[serviceInstanceName];
				
				$("#total_process_files").html(counterObject["TOTAL_FILES"]);
				$("#total_records").html(counterObject["TOTAL_RECORDS_RECIEVED"]);
				$("#total_success_files").html(counterObject["TOTAL_SUCCESS_FILES"]);
				$("#total_success_records").html(counterObject["TOTAL_SUCCESS_RECORDS"]);
				$("#total_fail_files").html(counterObject["TOTAL_FAILED_FILES"]);
				$("#total_fail_records").html(counterObject["TOTAL_FAILED_RECORDS"]);
				$("#total_inprocess_files").html(counterObject["TOTAL_INPROCESS_FILES"]);
				$("#avg_tps").html(counterObject["AVG_TPS"]);
				$("#total_pending_files").html(counterObject["TOTAL_PENDING_FILES"]);
				
			//	$('#container2 .1').remove();
				$('#file_merge_status_summary #file_merge_status_summary_span').remove();
				$('<span id="file_merge_status_summary_span">'+counterObject["FILE_MERGE_STATUS"]+'</span>').appendTo('#file_merge_status_summary');
				//$("#file_merge_status_summary").html(counterObject["FILE_MERGE_STATUS"]);
			}
			
			else if(serviceType == 'HTTP2_COLLECTION_SERVICE' && counterStatus[serviceInstanceName] != null){
				var counterObject = counterStatus[serviceInstanceName];
				
				$("#total_processed_records").html(counterObject["TOTAL_RECORDS_RECIEVED"]);
				$("#current_tps").html(counterObject["CURRENT_TPS"]);
				$("#total_received_packets").html(counterObject["TOTAL_RECIEVED_PACKETS"]);
				$("#total_processed_packets").html(counterObject["TOTAL_PROCCESSED_PACKETS"]);
				$("#total_buffered_packets").html(counterObject["TOTAL_BUFFERED_PACKETS"]);
				$("#total_dropped_packets").html(counterObject["TOTAL_PACKETS_DROPPED"]);
				$("#total_NetFlow_Malform_packets").html(counterObject["TOTAL_MALFORM_REQUEST"]);
				$("#total_respones_sent").html(counterObject["TOTAL_RESPONSE_SENT"]);
			}
			
			else if(serviceType == 'NATFLOWBINARY_COLLECTION_SERVICE' && counterStatus[serviceInstanceName] != null){
				var counterObject = counterStatus[serviceInstanceName];
				$("#total_files").html(counterObject["TOTAL_FILES"]);
				$("#total_processed_records").html(counterObject["TOTAL_RECORDS_RECIEVED"]);
				$("#total_pending_files").html(counterObject["TOTAL_PENDING_FILES"]);
				$("#total_received_packets").html(counterObject["TOTAL_RECIEVED_PACKETS"]);
				$("#total_processed_packets").html(counterObject["TOTAL_PROCCESSED_PACKETS"]);
				$("#total_over_processed_packets").html(counterObject["TOTAL_OVER_PROCESSED_PACKETS"]);
				$("#total_buffered_packets").html(counterObject["TOTAL_BUFFERED_PACKETS"]);
				$("#total_dropped_packets").html(counterObject["TOTAL_PACKETS_DROPPED"]);
				$("#total_NetFlow_Malform_packets").html(counterObject["TOTAL_MALFORM_REQUEST"]);
				$("#total_forward_proxy_packets").html(counterObject["TOTAL_CLONE_PACKETS_FOR_PROXY_CLINET"]);
				$("#total_received_bits").html(counterObject["TOTAL_RECEIVED_BITS"]);
				$("#total_processed_bits").html(counterObject["TOTAL_PROCESSED_BITS"]);
				$("#total_over_processed_bits").html(counterObject["TOTAL_OVER_PROCESSED_BITS"]);
				$("#avg_pps").html(counterObject["AVG_PPS"]);
				$("#current_pps").html(counterObject["CURRENT_PPS"]);
				$("#current_opps").html(counterObject["CURRENT_OPPS"]);
				$("#avg_opps").html(counterObject["AVG_OPPS"]);
				$("#avg_bps").html(counterObject["AVG_BPS"]);
				$("#current_bps").html(counterObject["CURRENT_BPS"]);
				$("#current_opbs").html(counterObject["CURRENT_OPBS"]);
				$("#avg_opbs").html(counterObject["AVG_OPBS"]);			
			}
			
			else if(serviceType == 'NATFLOW_COLLECTION_SERVICE' && counterStatus[serviceInstanceName] != null){
				var counterObject = counterStatus[serviceInstanceName];
				
				$("#total_NetFlowReceived_packets").html(counterObject["TOTAL_RECIEVED_PACKETS"]);
				$("#total_NetFlowProcessed_packets").html(counterObject["TOTAL_PROCCESSED_PACKETS"]);
				$("#total_NetFlow_request").html(counterObject["TOTAL_RECORDS_RECIEVED"]);
				$("#total_NetFlow_template_request").html(counterObject["TOTAL_TEMPLATE_REQUEST"]);
				$("#total_NetFlow_flowData_request").html(counterObject["TOTAL_FLOW_REQUEST"]);
				$("#total_NetFlow_Malform_packets").html(counterObject["TOTAL_MALFORM_REQUEST"]);
				$("#total_NetFlow_pending_request").html(counterObject["TOTAL_BUFFERED_PENDING_TO_WRITE_PACKETS"]);
				$("#total_NetFlow_option_templates_request").html(counterObject["TOTAL_OPTION_TEMPLATE_REQUEST"]);
				
				$("#total_files").html(counterObject["TOTAL_FILES"]);
				
				$("#current_tps").html(counterObject["CURRENT_TPS"]);
				
				$("#total_buffered_packets").html(counterObject["TOTAL_BUFFERED_PACKETS"]);
				$("#total_dropped_packets").html(counterObject["TOTAL_PACKETS_DROPPED"]);
				$("#total_forward_proxy_packets").html(counterObject["TOTAL_CLONE_PACKETS_FOR_PROXY_CLINET"]);
				
			}
			
			
			else if(serviceType == 'SYSLOG_COLLECTION_SERVICE' && counterStatus[serviceInstanceName] != null){
				var counterObject = counterStatus[serviceInstanceName];
				
				$("#total_files").html(counterObject["TOTAL_FILES"]);
				$("#total_records").html(counterObject["TOTAL_RECORDS_RECIEVED"]);
				$("#total_pending_files").html(counterObject["TOTAL_PENDING_FILES"]);
				$("#current_tps").html(counterObject["CURRENT_TPS"]);
				$("#total_received_packets").html(counterObject["TOTAL_RECIEVED_PACKETS"]);
				$("#total_processed_packets").html(counterObject["TOTAL_PROCCESSED_PACKETS"]);
				$("#total_buffered_packets").html(counterObject["TOTAL_BUFFERED_PACKETS"]);
				$("#total_dropped_packets").html(counterObject["TOTAL_PACKETS_DROPPED"]);
				$("#total_malform_packets").html(counterObject["TOTAL_MALFORM_REQUEST"]);
				
				
			}
			
			else if(serviceType == 'MQTT_COLLECTION_SERVICE' && counterStatus[serviceInstanceName] != null){
				var counterObject = counterStatus[serviceInstanceName];
				
				$("#total_files").html(counterObject["TOTAL_FILES"]);
				$("#total_records").html(counterObject["TOTAL_RECORDS_RECIEVED"]);
				$("#total_pending_files").html(counterObject["TOTAL_PENDING_FILES"]);
				$("#current_tps").html(counterObject["CURRENT_TPS"]);
				$("#total_received_packets").html(counterObject["TOTAL_RECIEVED_PACKETS"]);
				$("#total_processed_packets").html(counterObject["TOTAL_PROCCESSED_PACKETS"]);
				$("#total_buffered_packets").html(counterObject["TOTAL_BUFFERED_PACKETS"]);
				$("#total_dropped_packets").html(counterObject["TOTAL_PACKETS_DROPPED"]);
				$("#total_malform_packets").html(counterObject["TOTAL_MALFORM_REQUEST"]);
				
				
			}
			
			else if(serviceType == 'COAP_COLLECTION_SERVICE' && counterStatus[serviceInstanceName] != null){
				var counterObject = counterStatus[serviceInstanceName];
				
				$("#total_requests").html(counterObject["TOTAL_REQUEST_SENT"]);
				$("#total_successful_responses").html(counterObject["TOTAL_RECEIVED_RESPONSES"]);
				$("#total_invalid_malformed_requests").html(counterObject["TOTAL_MALFORM_REQUEST_SENT"]);
				$("#total_dropped_responses").html(counterObject["TOTAL_PACKETS_DROPPED"]);
				$("#total_produced_kafka_packets").html(counterObject["TOTAL_PRODUCED_KAFKA_PACKETS"]);
				$("#total_wrote_packets_in_file").html(counterObject["TOTAL_WROTE_PACKETS_IN_FILE"]);
				$("#total_received_packets").html(counterObject["TOTAL_RECIEVED_PACKETS"]);
				$("#total_processed_packets").html(counterObject["TOTAL_PROCCESSED_PACKETS"]);
				$("#total_buffered_packets").html(counterObject["TOTAL_BUFFERED_PACKETS"]);
				$("#coap_current_tps").html(counterObject["CURRENT_TPS"]);
				
				
			}
			
			else if(serviceType == 'GTPPRIME_COLLECTION_SERVICE' && counterStatus[serviceInstanceName] != null){
				var counterObject = counterStatus[serviceInstanceName];
				$("#total_files").html(counterObject["TOTAL_FILES"]);
				$("#total_pending_files").html(counterObject["TOTAL_PENDING_FILES"]);
				$("#total_received_packets").html(counterObject["TOTAL_RECIEVED_PACKETS"]);
				$("#total_processed_packets").html(counterObject["TOTAL_PROCCESSED_PACKETS"]);
				$("#total_buffered_packets").html(counterObject["TOTAL_BUFFERED_PACKETS"]);
				$("#total_dropped_packets").html(counterObject["TOTAL_PACKETS_DROPPED"]);
				$("#current_tps").html(counterObject["CURRENT_TPS"]);
				$("#total_request").html(counterObject["TOTAL_REQUEST"]);
				$("#total_echo_request").html(counterObject["TOTAL_ECHO_REQUEST"]);
				$("#total_node_alive_request").html(counterObject["TOTAL_NODE_ALIVE_REQUEST"]);
				$("#total_redirection_request").html(counterObject["TOTAL_REDIRECTION_REQUEST"]);
				$("#total_data_record_transfer_request").html(counterObject["TOTAL_DATA_RECORD_TRANSFER_REQUEST"]);
				$("#total_data_record_transfer_records").html(counterObject["TOTAL_DATA_RECORD_TRANSFER_RECORDS"]);
				$("#total_redirection_response_success").html(counterObject["TOTAL_REDIRECTION_RESPONSE_SUCCESS"]);
				$("#total_redirection_response_failure").html(counterObject["TOTAL_REDIRECTION_RESPONSE_FAILURE"]);
				$("#total_echo_response").html(counterObject["TOTAL_ECHO_RESPONSE"]);
				$("#total_node_alive_response").html(counterObject["TOTAL_NODE_ALIVE_RESPONSE"]);
				$("#total_data_record_transfer_responsesuccess").html(counterObject["TOTAL_DATA_RECORD_TRANSFER_RESPONSESUCCESS"]);
				$("#total_data_record_transfer_responsefailure").html(counterObject["TOTAL_DATA_RECORD_TRANSFER_RESPONSEFAILURE"]);
				$("#total_version_not_supported_response").html(counterObject["TOTAL_VERSION_NOT_SUPPORTED_RESPONSE"]);
				$("#total_invalid_client_request").html(counterObject["TOTAL_INVALID_CLIENT_REQUEST"]);
				$("#total_dropped_echo_request").html(counterObject["TOTAL_DROPPED_ECHO_REQUEST"]);
				$("#total_dropped_node_alive_request").html(counterObject["TOTAL_DROPPED_NODE_ALIVE_REQUEST"]);
				$("#total_dropped_redirection_request").html(counterObject["TOTAL_DROPPED_REDIRECTION_REQUEST"]);
				$("#total_dropped_data_transfer_request").html(counterObject["TOTAL_DROPPED_DATA_TRANSFER_REQUEST"]);
				$("#total_dropped_request").html(counterObject["TOTAL_DROPPED_REQUEST"]);
				$("#total_malformed_request_packet").html(counterObject["TOTAL_MALFORMED_REQUEST_PACKET"]);
				$("#total_malformed_echo_response_received").html(counterObject["TOTAL_MALFORMED_ECHO_RESPONSE_RECEIVED"]);
				$("#total_malformed_node_alive_response_received").html(counterObject["TOTAL_MALFORMED_NODE_ALIVE_RESPONSE_RECEIVED"]);
				$("#total_echo_request_sent").html(counterObject["TOTAL_ECHO_REQUEST_SENT"]);
				$("#total_echo_request_retry").html(counterObject["TOTAL_ECHO_REQUEST_RETRY"]);
				$("#total_node_alive_request_sent").html(counterObject["TOTAL_NODE_ALIVE_REQUEST_SENT"]);
				$("#total_node_alive_request_retry").html(counterObject["TOTAL_NODE_ALIVE_REQUEST_RETRY"]);
				$("#total_echo_response_received").html(counterObject["TOTAL_ECHO_RESPONSE_RECEIVED"]);
				$("#total_node_alive_response_received").html(counterObject["TOTAL_NODE_ALIVE_RESPONSE_RECEIVED"]);
				
			} else if(serviceType == 'COLLECTION_SERVICE' && counterStatus[serviceInstanceName] != null) {
             	var counterObject = counterStatus[serviceInstanceName];
                                     
             $("#total_files").html(counterObject["TOTAL_FILES"]); 
         
             $("#total_success_files").html(counterObject["TOTAL_SUCCESS_FILES"]); 
          
             $("#total_fail_files").html(counterObject["TOTAL_FAILED_FILES"]);
           
             $("#total_inprocess_files").html(counterObject["TOTAL_INPROCESS_FILES"]);
             $("#total_pending_files").html(counterObject["TOTAL_PENDING_FILES"]);
             $("#current_tps").html(counterObject["CURRENT_TPS"]);
             $("#total_missing_files").html(counterObject["TOTAL_COLLECTION_MISSING_FILES"]);
             $("#total_duplicate_files").html(counterObject["TOTAL_COLLECTION_DUPLICATE_FILES"]);
             $("#total_invalid_files").html(counterObject["TOTAL_COLLECTION_INVALID_FILES"]);
 

      }else if(serviceType == 'IPLOG_PARSING_SERVICE' && counterStatus[serviceInstanceName] != null)
      {
     	 
     		
     	 var counterObject = counterStatus[serviceInstanceName];

     
  	        $("#total_parsed_files").html(counterObject["TOTAL_FILES"]);
				$("#total_records").html(counterObject["TOTAL_RECORDS_RECIEVED"]);
				$("#total_success_files").html(counterObject["TOTAL_SUCCESS_FILES"]);
				$("#total_success_records").html(counterObject["TOTAL_SUCCESS_RECORDS"]);
				$("#total_fail_files").html(counterObject["TOTAL_FAILED_FILES"]);
				$("#total_fail_records").html(counterObject["TOTAL_FAILED_RECORDS"]);
				$("#total_inprocess_files").html(counterObject["TOTAL_INPROCESS_FILES"]);
				$("#total_pending_files").html(counterObject["TOTAL_PENDING_FILES"]);
				$("#current_tps").html(counterObject["CURRENT_TPS"]);
				$("#total_malform_flowset").html(counterObject["TOTAL_MALFORM_RECORDS"]);
				$("#avg_eps").html(counterObject["AVG_EPS"]);
				$("#total_partially_successful_files").html(counterObject["TOTAL_PARTIALLY_SUCCESS_FILES"]);
				$("#total_packets").html(counterObject["TOTAL_PACKETS"]);
				
		         }
			
			
      else if(serviceType == 'PARSING_SERVICE' && counterStatus[serviceInstanceName] != null)
      {
     	 var counterObject = counterStatus[serviceInstanceName];
     
     	 
     	   $("#total_parsed_files").html(counterObject["TOTAL_FILES"]);
  	        $("#total_files").html(counterObject["TOTAL_FILES"]);
				$("#total_records").html(counterObject["TOTAL_RECORDS_RECIEVED"]);
				$("#total_success_files").html(counterObject["TOTAL_SUCCESS_FILES"]);
				$("#total_success_records").html(counterObject["TOTAL_SUCCESS_RECORDS"]);
				$("#total_fail_files").html(counterObject["TOTAL_FAILED_FILES"]);
				$("#total_fail_records").html(counterObject["TOTAL_FAILED_RECORDS"]);
				$("#total_inprocess_files").html(counterObject["TOTAL_INPROCESS_FILES"]);
				$("#avg_tps").html(counterObject["AVG_TPS"]);
				$("#avg_eps").html(counterObject["AVG_EPS"]);
				$("#total_pending_files").html(counterObject["TOTAL_PENDING_FILES"]);
				$("#total_partially_successful_files").html(counterObject["TOTAL_PARTIALLY_SUCCESS_FILES"]);
				$("#total_malform_flowset").html(counterObject["TOTAL_MALFORM_RECORDS"]);
				$("#total_packets").html(counterObject["TOTAL_PACKETS"]);
				$("#total_success_packets").html(counterObject["TOTAL_SUCCESS_PACKETS"]);
				$("#total_malform_packets").html(counterObject["TOTAL_MALFORM_PACKETS"]);
				
   	  }
      else if(serviceType == 'PROCESSING_SERVICE' && counterStatus[serviceInstanceName] != null)
      {
    	    var counterObject = counterStatus[serviceInstanceName];
 	        $("#total_process_files").html(counterObject["TOTAL_FILES"]);
			$("#total_records").html(counterObject["TOTAL_RECORDS_RECIEVED"]);
			$("#total_success_files").html(counterObject["TOTAL_SUCCESS_FILES"]);
			$("#total_success_records").html(counterObject["TOTAL_SUCCESS_RECORDS"]);
			$("#total_invalid_records").html(counterObject["TOTAL_INVALID_RECORDS"]);
			$("#total_filter_records").html(counterObject["TOTAL_FILTER_RECORDS"]);
			$("#total_clone_records").html(counterObject["TOTAL_CLONE_RECORDS"]);
			$("#total_duplicate_records").html(counterObject["TOTAL_DUPLICATE_RECORDS"]);
			$("#total_fail_files").html(counterObject["TOTAL_FAILED_FILES"]);
			$("#total_fail_records").html(counterObject["TOTAL_FAILED_RECORDS"]);
			$("#total_inprocess_files").html(counterObject["TOTAL_INPROCESS_FILES"]);
			$("#total_pending_files").html(counterObject["TOTAL_PENDING_FILES"]);
			$("#avg_tps").html(counterObject["AVG_TPS"]);
			$("#total_partially_successful_files").html(counterObject["TOTAL_PARTIALLY_SUCCESS_FILES"]);
			$("#total_malform_flowset").html(counterObject["TOTAL_MALFORM_RECORDS"]);

   	  }
      else if(serviceType == 'DATA_CONSOLIDATION_SERVICE' && counterStatus[serviceInstanceName] != null)
      {
     	 	var counterObject = counterStatus[serviceInstanceName];
            $("#total_files").html(counterObject["TOTAL_FILES"]);
            $("#total_success_files").html(counterObject["TOTAL_SUCCESS_FILES"]);
            $("#total_pending_files").html(counterObject["TOTAL_PENDING_FILES"]);
			$("#total_inprocess_files").html(counterObject["TOTAL_INPROCESS_FILES"]);
			$("#total_fail_files").html(counterObject["TOTAL_FAILED_FILES"]);
			$("#avg_tps").html(counterObject["AVG_TPS"]);
   	  }
      else if(serviceType == 'AGGREGATION_SERVICE' && counterStatus[serviceInstanceName] != null)
      {
     	 	var counterObject = counterStatus[serviceInstanceName];
     		$("#current_tps").html(counterObject["CURRENT_TPS"]);
     		$("#total_files").html(counterObject["TOTAL_FILES"]);
     		$("#total_records").html(counterObject["TOTAL_RECORDS_RECIEVED"]);
     		$("#total_inprocess_files").html(counterObject["TOTAL_INPROCESS_FILES"]);
     		var driverInfoObject = counterObject["DRIVER_INFO"];
     		var dPathObjectValue = Object.values(driverInfoObject)[0];
     		var dcounterObject = Object.values(dPathObjectValue)[0];      		
     		$("#total_aggregated_files").html(dcounterObject["TOTAL_SUCCESS_FILES"]);
            $("#total_aggregated_records").html(dcounterObject["TOTAL_SUCCESS_RECORDS"]);
            $("#total_fail_files").html(dcounterObject["TOTAL_FAILED_FILES"]);
            $("#total_fail_records").html(dcounterObject["TOTAL_FAILED_RECORDS"]);
            $("#total_partially_successful_files").html(dcounterObject["TOTAL_PARTIALLY_SUCCESS_FILES"]);
   	  }
      else if(serviceType == 'DIAMETER_COLLECTION_SERVICE' && counterStatus[serviceInstanceName] != null)
      {
    	  var counterObject = counterStatus[serviceInstanceName];  
    	  $("#total_duplicate_requests").html(counterObject["TOTAL_DUPLICATE_REQUESTS"]);
    	  $("#total_requests_received").html(counterObject["TOTAL_REQUESTS_RECEIVED"]);
    	  $("#total_malformed_packets").html(counterObject["TOTAL_MALFORMED_PACKETS"]);
    	  $("#total_requests_dropped").html(counterObject["TOTAL_REQUESTS_DROPPED"]);
    	  $("#total_responses_sent").html(counterObject["TOTAL_RESPONSE_SENT"]);
      }else if(serviceType == 'RADIUS_COLLECTION_SERVICE' && counterStatus[serviceInstanceName] != null)
      {
    	  var counterObject = counterStatus[serviceInstanceName];  
    	  $("#total_received_radius_packets").html(counterObject["TOTAL_RECIEVED_PACKETS"]);
    	  $("#total_processed_radius_packets").html(counterObject["TOTAL_PROCCESSED_PACKETS"]);
    	  $("#total_received_radius_requests").html(counterObject["TOTAL_RECORDS_RECIEVED"]);
    	  $("#total_malform_requests").html(counterObject["TOTAL_MALFORM_REQUEST"]);
    	  $("#radius_average_tps").html(counterObject["AVG_TPS"]);
    	  $("#radius_current_tps").html(counterObject["CURRENT_TPS"]);
    	  $("#total_invalid_client_requests").html(counterObject["TOTAL_INVALID_REQUEST"]);
      }
	}
	
	$(document).ready(function() {
		if(currentTab == 'DISTRIBUTION_SERVICE_SUMMARY'){
			getServiceCounterDetails('<%=ControllerConstants.GET_COLLECTION_SERVICE_COUNTER_STATUS%>','${serviceId}','${serviceType}','ONLOAD');	
		}else if(currentTab == 'AGGREGATION_SERVICE_SUMMARY'){
			getServiceCounterDetails('<%=ControllerConstants.GET_AGGREGATION_SERVICE_COUNTER_STATUS%>','${serviceId}','${serviceType}','ONLOAD');	
		}
		$("#counterstatus").click();
	}); 
	
	</script>






