<%@page import="com.elitecore.sm.common.model.TrueFalseEnum"%>
<%@ taglib uri="http://www.springframework.org/security/tags"	prefix="sec"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="/WEB-INF/ShowErrorTag.tld" prefix="elitecoreError"%>
<div class="box box-warning" id="online_coll_services_operational_param_div">
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
						<c:if test="${(serviceType eq 'SYSLOG_COLLECTION_SERVICE') or (serviceType eq 'NATFLOW_COLLECTION_SERVICE') or (serviceType eq 'GTPPRIME_COLLECTION_SERVICE') or (serviceType eq 'RADIUS_COLLECTION_SERVICE') or (serviceType eq 'COAP_COLLECTION_SERVICE')}">
						<div class="col-md-6 no-padding">
							<spring:message code="netflow.collSer.config.iosizing.param.max.idel.conn.time" var="label" ></spring:message>
							<spring:message code="netflow.collSer.config.iosizing.param.max.idel.conn.time.tooltip" var="tooltip" ></spring:message>
							<div class="form-group">
								<div class="table-cell-label">${label}<span class="required">*</span></div>
								<div class="input-group ">
									<form:input path="maxIdelCommuTime" cssClass="form-control table-cell input-sm" tabindex="4" readonly="false" id="service-maxIdelCommuTime" data-toggle="tooltip" data-placement="bottom" title="${tooltip }" onkeypress="return isNumber(arguments[0] || window.event);"></form:input>
									<spring:bind path="maxIdelCommuTime">
										<elitecoreError:showError errorMessage="${status.errorMessage}" errorId="service-maxIdelCommuTime_error"></elitecoreError:showError>
									</spring:bind>
								</div>
							</div>
						</div>
						</c:if>
						
						<c:if test="${(serviceType ne 'MQTT_COLLECTION_SERVICE') and (serviceType ne 'GTPPRIME_COLLECTION_SERVICE') and (serviceType ne 'RADIUS_COLLECTION_SERVICE')}">
						<div class="col-md-6 no-padding">
							<spring:message code="netflow.collSer.config.iosizing.param.buff.write.limit" var="label" ></spring:message>
							<spring:message code="netflow.collSer.config.iosizing.param.buff.write.limit.tooltip" var="tooltip" ></spring:message>
							<div class="form-group">
								<div class="table-cell-label">${label}<span class="required">*</span></div>
								<div class="input-group ">
									<form:input path="bulkWriteLimit" cssClass="form-control table-cell input-sm" tabindex="4" readonly="false"	id="service-bulkWriteLimit" data-toggle="tooltip" data-placement="bottom" title="${tooltip }" onkeypress="return isNumber(arguments[0] || window.event);"></form:input>
									<spring:bind path="bulkWriteLimit">
										<elitecoreError:showError errorMessage="${status.errorMessage}" errorId="service-bulkWriteLimit_error"></elitecoreError:showError>
									</spring:bind>
								</div>
							</div>							
						</div>		
						</c:if>
						<c:if test="${(serviceType eq 'MQTT_COLLECTION_SERVICE')}">
						<div class="col-md-6 no-padding">
							<spring:message code="netflow.collSer.config.iosizing.param.max.read.rate" var="label" ></spring:message>
							<spring:message code="netflow.collSer.config.iosizing.param.max.read.rate" var="tooltip" ></spring:message>
							<div class="form-group">
								<div class="table-cell-label">${label}<span class="required">*</span></div>
								<div class="input-group ">
									<form:input path="maxReadRate" value="${maxReadRate}" cssClass="form-control table-cell input-sm" tabindex="4" readonly="false"	id="service-maxReadRate" data-toggle="tooltip" data-placement="bottom" title="${tooltip }"></form:input>
									<spring:bind path="maxReadRate">
										<elitecoreError:showError errorMessage="${status.errorMessage}" errorId="service-maxReadRate_error"></elitecoreError:showError>
									</spring:bind>
								</div>
							</div>
						</div>
						<div class="col-md-6 no-padding">
							<spring:message code="netflow.collSer.config.iosizing.param.receiver.buffer.size" var="label" ></spring:message>
							<spring:message code="netflow.collSer.config.iosizing.param.receiver.buffer.size" var="tooltip" ></spring:message>
							<div class="form-group">
								<div class="table-cell-label">${label}<span class="required">*</span></div>
								<div class="input-group ">
									<form:input path="receiverBufferSize" value="${receiverBufferSize}" cssClass="form-control table-cell input-sm" tabindex="4" readonly="false"	id="service-receiverBufferSize" data-toggle="tooltip" data-placement="bottom" title="${tooltip }" ></form:input>
									<spring:bind path="receiverBufferSize">
										<elitecoreError:showError errorMessage="${status.errorMessage}" errorId="service-receiverBufferSize_error"></elitecoreError:showError>
									</spring:bind>
								</div>
							</div>
						</div>
						<div class="col-md-6 no-padding">
							<spring:message code="netflow.collSer.config.iosizing.param.connect.attempts.max" var="label" ></spring:message>
							<spring:message code="netflow.collSer.config.iosizing.param.connect.attempts.max" var="tooltip" ></spring:message>
							<div class="form-group">
								<div class="table-cell-label">${label}<span class="required">*</span></div>
								<div class="input-group ">
									<form:input path="connectAttemptsMax" value="${connectAttemptsMax}" cssClass="form-control table-cell input-sm" tabindex="4" readonly="false"	id="service-receiverBufferSize" data-toggle="tooltip" data-placement="bottom" title="${tooltip }"></form:input>
									<spring:bind path="connectAttemptsMax">
										<elitecoreError:showError errorMessage="${status.errorMessage}" errorId="service-receiverBufferSize_error"></elitecoreError:showError>
									</spring:bind>
								</div>
							</div>
						</div>
						<div class="col-md-6 no-padding">
							<spring:message code="netflow.collSer.config.iosizing.param.reconnect.attempts.max" var="label" ></spring:message>
							<spring:message code="netflow.collSer.config.iosizing.param.reconnect.attempts.max" var="tooltip" ></spring:message>
							<div class="form-group">
								<div class="table-cell-label">${label}<span class="required">*</span></div>
								<div class="input-group ">
									<form:input path="reconnectAttemptsMax" value="${reconnectAttemptsMax}" cssClass="form-control table-cell input-sm" tabindex="4" readonly="false"	id="service-receiverBufferSize" data-toggle="tooltip" data-placement="bottom" title="${tooltip }" ></form:input>
									<spring:bind path="reconnectAttemptsMax">
										<elitecoreError:showError errorMessage="${status.errorMessage}" errorId="service-receiverBufferSize_error"></elitecoreError:showError>
									</spring:bind>
								</div>
							</div>
						</div>
						<div class="col-md-6 no-padding">
							<spring:message code="netflow.collSer.config.iosizing.param.reconnect.delay" var="label" ></spring:message>
							<spring:message code="netflow.collSer.config.iosizing.param.reconnect.delay" var="tooltip" ></spring:message>
							<div class="form-group">
								<div class="table-cell-label">${label}<span class="required">*</span></div>
								<div class="input-group ">
									<form:input path="reconnectDelay" value="${reconnectDelay}" cssClass="form-control table-cell input-sm" tabindex="4" readonly="false"	id="service-receiverBufferSize" data-toggle="tooltip" data-placement="bottom" title="${tooltip }"></form:input>
									<spring:bind path="reconnectDelay">
										<elitecoreError:showError errorMessage="${status.errorMessage}" errorId="service-receiverBufferSize_error"></elitecoreError:showError>
									</spring:bind>
								</div>
							</div>
						</div>
						</c:if>
						<c:if test="${(serviceType ne 'GTPPRIME_COLLECTION_SERVICE') and (serviceType ne 'MQTT_COLLECTION_SERVICE') and (serviceType ne 'COAP_COLLECTION_SERVICE')}">
						<div class="col-md-6 no-padding">
							<spring:message code="netflow.collSer.config.iosizing.param.max.packt.size" var="label" ></spring:message>
							<spring:message code="netflow.collSer.config.iosizing.param.max.packt.size.tooltip" var="tooltip" ></spring:message>
							<div class="form-group">
								<div class="table-cell-label">${label}<span class="required">*</span></div>
								<div class="input-group ">
									<form:input path="maxPktSize" cssClass="form-control table-cell input-sm" tabindex="4" readonly="false"	id="service-maxPktSize" data-toggle="tooltip" data-placement="bottom" title="${tooltip }" onkeypress="return isNumber(arguments[0] || window.event);"></form:input>
									<spring:bind path="maxPktSize">
										<elitecoreError:showError errorMessage="${status.errorMessage}" errorId="service-maxPktSize_error"></elitecoreError:showError>
									</spring:bind>
								</div>
							</div>
						</div>
						</c:if>
						
						<c:if test="${(serviceType ne 'GTPPRIME_COLLECTION_SERVICE') and (serviceType ne 'MQTT_COLLECTION_SERVICE')}">
						<div class="col-md-6 no-padding">
							<spring:message code="netflow.collSer.config.iosizing.param.max.write.buff" var="label" ></spring:message>
							<spring:message code="netflow.collSer.config.iosizing.param.max.write.buff.tooltip" var="tooltip" ></spring:message>
							<div class="form-group">
								<div class="table-cell-label">${label}<span class="required">*</span></div>
								<div class="input-group ">
									<form:input path="maxWriteBufferSize" cssClass="form-control table-cell input-sm" tabindex="4" readonly="false"	id="service-maxWriteBufferSize" data-toggle="tooltip" data-placement="bottom" title="${tooltip }" onkeypress="return isNumber(arguments[0] || window.event);"></form:input>
									<spring:bind path="maxWriteBufferSize">
										<elitecoreError:showError errorMessage="${status.errorMessage}" errorId="service-maxWriteBufferSize_error"></elitecoreError:showError>
									</spring:bind>
								</div>
							</div>
						</div>
						</c:if>
						
											
				
			 		<c:if test="${ (serviceType eq 'SYSLOG_COLLECTION_SERVICE') or (serviceType eq 'COAP_COLLECTION_SERVICE') }">	
						<div class="col-md-6 no-padding">
							<spring:message code="netflow.collSer.config.oper.param.new.line.char.avail" var="label" ></spring:message>
							<spring:message code="netflow.collSer.config.oper.param.new.line.char.avail.tooltip" var="tooltip" ></spring:message>
							<div class="form-group">
								<div class="table-cell-label">${label}</div>
								<div class="input-group ">
									<form:select path="newLineCharAvailable" cssClass="form-control table-cell input-sm" tabindex="4" id="service-newLineCharAvailable" data-toggle="tooltip" data-placement="bottom"  title="${tooltip }" value="${service_config_form_bean.newLineCharAvailable}">
										<%
										 for(TrueFalseEnum enumlist : TrueFalseEnum.values()) { 
											 %>
											 	<form:option value="<%=  enumlist.getName() %>"><%= enumlist.toString() %></form:option>
											 <%
										     }
										%>
									</form:select>
									<spring:bind path="newLineCharAvailable">
										<elitecoreError:showError errorMessage="${status.errorMessage}" errorId="service-newLineCharAvailable_error"></elitecoreError:showError>
									</spring:bind>
								</div>
							</div>
						</div>
					</c:if>	

						<c:if test="${(serviceType ne 'GTPPRIME_COLLECTION_SERVICE') and (serviceType ne 'MQTT_COLLECTION_SERVICE') and (serviceType ne 'COAP_COLLECTION_SERVICE')}">
						<div class="col-md-6 no-padding">
							<spring:message code="netflow.collSer.config.iosizing.param.parall.file.write.count" var="tooltip" ></spring:message>
							<div class="form-group">
								<div class="table-cell-label">${tooltip}</div>
								<div class="input-group ">
									<form:input path="parallelFileWriteCount" cssClass="form-control table-cell input-sm" tabindex="4" readonly="false"	id="service-parallelFileWriteCount" data-toggle="tooltip" data-placement="bottom" title="${tooltip }"></form:input>
									<spring:bind path="parallelFileWriteCount">
										<elitecoreError:showError errorMessage="${status.errorMessage}" errorId="service-parallelFileWriteCount_error"></elitecoreError:showError>
									</spring:bind>
								</div>
							</div>
						</div>
						</c:if>
						
						<c:if test="${(serviceType ne 'GTPPRIME_COLLECTION_SERVICE') and (serviceType ne 'RADIUS_COLLECTION_SERVICE') and (serviceType ne 'HTTP2_COLLECTION_SERVICE') and (serviceType ne 'MQTT_COLLECTION_SERVICE') and (serviceType ne 'COAP_COLLECTION_SERVICE')}">
							<div class="col-md-6 no-padding">
								<spring:message code="netflow.collSer.config.iosizing.param.is.tcp.protocol" var="tooltip" ></spring:message>
								<div class="form-group">
									<div class="table-cell-label">${tooltip}</div>
									<div class="input-group ">
										<form:select path="isTCPProtocol" cssClass="form-control table-cell input-sm" tabindex="4" id="service-isTCPProtocol"
										 data-toggle="tooltip" data-placement="bottom"  title="${tooltip }" value="${service_config_form_bean.isTCPProtocol}">
										<%
										 for(TrueFalseEnum enumlist : TrueFalseEnum.values()) { 
											 %>
											 	<form:option value="<%=  enumlist.getName() %>"><%= enumlist.toString() %></form:option>
											 <%
										     }
										%>
										</form:select>
										<spring:bind path="isTCPProtocol">
											<elitecoreError:showError errorMessage="${status.errorMessage}" errorId="service-enableParallelBinaryWrite_error"></elitecoreError:showError>
										</spring:bind>
									</div>
								</div>
							</div>
						</c:if>	
						
						<c:if test="${service_config_form_bean.svctype.alias eq 'NATFLOW_COLLECTION_SERVICE'}">
						<div class="col-md-6 no-padding">
							<spring:message code="netflow.collSer.config.iosizing.param.read.temp.initial" var="tooltip" ></spring:message>
							<div class="form-group">
								<div class="table-cell-label">${tooltip}</div>
								<div class="input-group ">
									<form:select path="readTemplateOnInit" cssClass="form-control table-cell input-sm" tabindex="4" id="service-readTemplateOnInit" data-toggle="tooltip" data-placement="bottom"  title="${tooltip }" value="${service_config_form_bean.readTemplateOnInit}">
										<%
										 for(TrueFalseEnum enumlist : TrueFalseEnum.values()) { 
											 %>
											 	<form:option value="<%=  enumlist.getName() %>"><%= enumlist.toString() %></form:option>
											 <%
										     }
										%>
									</form:select>
									<spring:bind path="readTemplateOnInit">
										<elitecoreError:showError errorMessage="${status.errorMessage}" errorId="service-readTemplateOnInit_error"></elitecoreError:showError>
									</spring:bind>
								</div>
							</div>
						</div>
						<div class="col-md-6 no-padding">
							<spring:message code="netflow.collSer.config.iosizing.param.parall.write.enable" var="tooltip" ></spring:message>
							<div class="form-group">
								<div class="table-cell-label">${tooltip}</div>
								<div class="input-group ">
									<form:select path="enableParallelBinaryWrite" cssClass="form-control table-cell input-sm" tabindex="4" id="service-enableParallelBinaryWrite" data-toggle="tooltip" data-placement="bottom"  title="${tooltip }" value="${service_config_form_bean.enableParallelBinaryWrite}" onchange="changeBinaryWriteOption(this.value)">
									<%
									 for(TrueFalseEnum enumlist : TrueFalseEnum.values()) { 
										 %>
										 	<form:option value="<%=  enumlist.getName() %>"><%= enumlist.toString() %></form:option>
										 <%
									     }
									%>
									</form:select>
									<spring:bind path="enableParallelBinaryWrite">
										<elitecoreError:showError errorMessage="${status.errorMessage}" errorId="service-enableParallelBinaryWrite_error"></elitecoreError:showError>
									</spring:bind>
								</div>
							</div>
						</div>		
						</c:if>
						<!-- threshold configuration for total packet processed and byte processed for natflow binary collector -->
						<c:if test="${(serviceType) eq 'NATFLOWBINARY_COLLECTION_SERVICE'}">
						<div class="col-md-6 no-padding">
							<spring:message code="netflow.binary.collSer.config.packet.threshold" var="label" ></spring:message>
							<spring:message code="netflow.binary.collSer.config.packet.threshold.tooltip" var="tooltip" ></spring:message>
							<div class="form-group">
								<div class="table-cell-label">${label}<span class="required">*</span></div>
								<div class="input-group ">
									<form:input path="packetThreshold" cssClass="form-control table-cell input-sm" tabindex="4" readonly="false"	id="service-packetThreshold" data-toggle="tooltip" data-placement="bottom" title="${tooltip }" type="number"></form:input>
									<spring:bind path="packetThreshold">
										<elitecoreError:showError errorMessage="${status.errorMessage}" errorId="service-packetThreshold_error"></elitecoreError:showError>
									</spring:bind>
								</div>
							</div>							
						</div>
						<div class="col-md-6 no-padding">
							<spring:message code="netflow.binary.collSer.config.bit.threshold" var="label" ></spring:message>
							<spring:message code="netflow.binary.collSer.config.bit.threshold.tooltip" var="tooltip" ></spring:message>
							<div class="form-group">
								<div class="table-cell-label">${label}<span class="required">*</span></div>
								<div class="input-group ">
									<form:input path="bitThreshold" cssClass="form-control table-cell input-sm" tabindex="4" readonly="false"	id="service-byteThreshold" data-toggle="tooltip" data-placement="bottom" title="${tooltip }" type="number"></form:input>
									<spring:bind path="bitThreshold">
										<elitecoreError:showError errorMessage="${status.errorMessage}" errorId="service-bitThreshold_error"></elitecoreError:showError>
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
