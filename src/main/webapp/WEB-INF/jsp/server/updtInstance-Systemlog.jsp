<%@page import="com.elitecore.sm.common.constants.BaseConstants"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="/WEB-INF/ShowErrorTag.tld" prefix="elitecoreError" %>


<div class="tab-pane <c:if test="${(REQUEST_ACTION_TYPE eq 'UPDATE_INSTANCE_SYSTEM_LOG')}"><c:out value="active"></c:out></c:if>" id="System-log">
 	<form:form modelAttribute="server_instance_form_bean" method="POST" action="<%= ControllerConstants.UPDATE_SYSTEM_LOG_CONFIG %>" id="serverInstance-updateSystemLog-form" enctype="multipart/form-data">
     <div class="box-body padding0">
     	
     	<input type="hidden" id="instanceStatus" name="instanceStatus" value="${serverInstanceStatus}" />
     	
     	<form:input type="hidden" path="id"  id="instance-id" ></form:input>
     	
   
     	<form:input type="hidden" path="fileStorageLocation" name="fileStorageLocation"></form:input>
     	
     	<form:input type="hidden" path="fileStatInDBEnable"  id="instance-fileStatInDBEnable" ></form:input>
     	<form:input type="hidden" path="snmpAlertEnable"  id="instance-snmpAlertEnable" ></form:input>
     	<form:input type="hidden" path="webservicesEnable"  id="instance-webservicesEnable" ></form:input>
     	<form:input type="hidden" path="lastUpdatedDate" name="lastUpdatedDate" id="instance-lastUpdate"></form:input>
     	<form:input type="hidden" path="fileCdrSummaryDBEnable"  id="instance-fileCdrSummaryDBEnable" ></form:input>
     	
     	<form:input type="hidden" path="name" name="name" id="instance-name"></form:input>
		<form:input type="hidden" path="description" name="description"></form:input>
		<form:input type="hidden" path="server.id" name="server.id" id="instance-server-id"></form:input>
		<form:input type="hidden" path="server.name" name="server.name" id="instance-server-name"></form:input>
		<form:input type="hidden" path="server.description" name="server.description" id="instance-server-description"></form:input>
		<form:input type="hidden" path="server.ipAddress" name="server.ipAddress" id="instance-server-ip"></form:input>
<%-- 		<form:input type="hidden" path="server.issynced" name="server.issynced" id="instance-server-issynced"></form:input> --%>
		<form:input type="hidden" path="server.utilityPort" name="server.utilityPort" id="instance-server-utilityport"></form:input>
		<form:input type="hidden" path="port" name="port"></form:input>
		<form:input type="hidden" path="minMemoryAllocation" name="minMemoryAllocation"></form:input>
		<form:input type="hidden" path="maxMemoryAllocation" name="maxMemoryAllocation"></form:input>
		<form:input type="hidden" path="maxConnectionRetry" name="maxConnectionRetry"></form:input>
		<form:input type="hidden" path="retryInterval" name="retryInterval"></form:input>
		<form:input type="hidden" path="connectionTimeout" name="connectionTimeout"></form:input>
		<form:input type="hidden" path="scriptName" name="scriptName"></form:input>
		<form:input type="hidden" path="serverHome" name="serverHome"></form:input>
		<form:input type="hidden" path="server.serverType.id" name="server.serverType.id" id="instance-server-serverType"></form:input>
		<form:input type="hidden" path="javaHome" name="javaHome"></form:input>
		<form:input type="hidden" path="minDiskSpace" name="minDiskSpace"></form:input>
		<form:input type="hidden" path="serverManagerDatasourceConfig.id" name="serverManagerDatasourceConfig.id"></form:input>
		<form:input type="hidden" path="serverManagerDatasourceConfig.name" name="serverManagerDatasourceConfig.name"></form:input>
		<form:input type="hidden" path="serverManagerDatasourceConfig.username" name="serverManagerDatasourceConfig.username"></form:input>
		<form:input type="hidden" path="serverManagerDatasourceConfig.password" name="serverManagerDatasourceConfig.password"></form:input>
		<form:input type="hidden" path="iploggerDatasourceConfig.id" name="iploggerDatasourceConfig.id"></form:input>
		<form:input type="hidden" path="iploggerDatasourceConfig.name" name="iploggerDatasourceConfig.name"></form:input>
		<form:input type="hidden" path="iploggerDatasourceConfig.username" name="iploggerDatasourceConfig.username"></form:input>
		<form:input type="hidden" path="iploggerDatasourceConfig.password" name="iploggerDatasourceConfig.password"></form:input>
     	
     	<input type="hidden" name="<%= BaseConstants.REQUEST_ACTION_TYPE %>"  id="<%= BaseConstants.REQUEST_ACTION_TYPE %>" value="<%= BaseConstants.UPDATE_INSTANCE_SYSTEM_LOG %>" />

         <div class="fullwidth ">
             <div class="box box-warning">
                 <div class="box-header with-border">
                     <h3 class="box-title"><spring:message code="updtInstacne.system.log.management.tab.header"></spring:message></h3>
                     <div class="box-tools pull-right">
                         <button class="btn btn-box-tool" data-widget="collapse"><i class="fa fa-minus"></i></button>
                     </div>
                     <!-- /.box-tools --> 
                 </div>	
                 <!-- /.box-header -->
                 <div class="box-body inline-form " >
                         <div class="col-md-6 no-padding">
                         	<spring:message code="updtInstacne.system.log.type" var="label"></spring:message>
                         	<spring:message code="updtInstacne.system.log.type.tooltip" var="tooltip"></spring:message>
                          	<div class="form-group">
                            	<div class="table-cell-label">${label}<span class="required">*</span></div>
                              	<div class="input-group ">
                              		<c:set var="chk1" value="${server_instance_form_bean.logsDetail.level}"></c:set>
	                               	<form:select path="logsDetail.level" cssClass="form-control table-cell input-sm" tabindex="4" name="logsDetail.level" id="instance-logsDetail-level" data-toggle="tooltip" data-placement="bottom"  title="${tooltip }">
		                              	<c:forEach items="${logType}" var="log">
		                             		<c:set var="val2" value="${log}"></c:set>
		                             		<c:choose> 
		                             			<c:when test="${chk1==val2}">
													<form:option value="${log}" selected="true">${log}</form:option>
												</c:when>
												<c:otherwise>
													<form:option value="${log}">${log}</form:option>
													</c:otherwise>
											</c:choose>
										</c:forEach>
									</form:select> 
                                    <spring:bind path="logsDetail.level">
										<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
									</spring:bind>	
                                </div>
                             </div>
                          </div>
                         <div class="col-md-6 no-padding">
                          	<spring:message code="updtInstacne.system.log.file.rolling.type" var="label"></spring:message>
                          	<spring:message code="updtInstacne.system.log.file.rolling.type.tooltip" var="tooltip"></spring:message>
                          	
                            <div class="form-group">
                            	<div class="table-cell-label">${label}<span class="required">*</span></div>
                              	<div class="input-group ">
                              		<form:select path="logsDetail.rollingType" cssClass="form-control table-cell input-sm" tabindex="4" name="logsDetail.rollingType" id="instance-logsDetail-rollingType" data-toggle="tooltip" data-placement="bottom"  title="${tooltip }">
		                              	<c:forEach items="${rollingType}" var="rolling">
											<form:option value="${rolling.value}">${rolling}</form:option>
										</c:forEach>
									</form:select> 
                                    <spring:bind path="logsDetail.rollingType">
										<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
									</spring:bind>	
                                </div>
                                
                                
                            </div>
                          </div>
                         <div class="col-md-6 no-padding">
                          	<spring:message code="updtInstacne.system.log.rolling.value" var="label"></spring:message>
                          	<spring:message code="updtInstacne.system.log.rolling.value.tooltip" var="tooltip"></spring:message>
                          	
                            <div class="form-group">
                            	<div class="table-cell-label">${label}<span class="required">*</span></div>
                              	<div class="input-group ">
                              		<form:input path="logsDetail.rollingValue" cssClass="form-control table-cell input-sm" onkeydown='isNumericOnKeyDown(event)' tabindex="4" name="logsDetail.rollingValue" id="instance-logsDetail-rollingValue" data-toggle="tooltip" data-placement="bottom"  title="${tooltip }"></form:input>
                                    <spring:bind path="logsDetail.rollingValue">
										<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
									</spring:bind>
                                </div>
                            </div>
                          </div>
                         <div class="col-md-6 no-padding">
                          	<spring:message code="updtInstacne.system.log.rolling.unit" var="label"></spring:message>
                          	<spring:message code="updtInstacne.system.log.rolling.unit.tooltip" var="tooltip"></spring:message>
                           	<div class="form-group">
                                <div class="table-cell-label">${label}<span class="required">*</span></div>
                              	<div class="input-group ">
                              		<form:input path="logsDetail.maxRollingUnit" cssClass="form-control table-cell input-sm" onkeydown='isNumericOnKeyDown(event)' tabindex="4" name="logsDetail.maxRollingUnit" id="instance-logsDetail-maxRollingUnit" data-toggle="tooltip" data-placement="bottom"  title="${tooltip }"></form:input>
                                    <spring:bind path="logsDetail.maxRollingUnit">
										<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
									</spring:bind>
                                 </div>
                            </div>
                          </div>
                         
                         <div class="col-md-6 no-padding">
                        	<spring:message code="updtInstacne.system.log.file.location" var="label"></spring:message>
                        	<spring:message code="updtInstacne.system.log.file.location.tooltip" var="tooltip"></spring:message>
                            <div class="form-group">
                            	<div class="table-cell-label">${label}</div>
                           		<div class="input-group ">
                              		<form:input path="logsDetail.logPathLocation" cssClass="form-control table-cell input-sm" tabindex="4" name="logsDetail.logPathLocation" id="instance-logsDetail-logPathLocation" data-toggle="tooltip" data-placement="bottom"  title="${tooltip }"></form:input>
                                    <spring:bind path="logsDetail.logPathLocation">
										<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
									</spring:bind>
                                </div>
                             </div>
                         </div>
                 </div>
                 <!-- /.box-body --> 
             </div>
         </div>
         <div class="fullwidth">
             <div class="box box-warning">
                 <div class="box-header with-border">
                     <h3 class="box-title"><spring:message code="updtInstacne.system.log.server.threshold.tab.header"></spring:message></h3>
                     <div class="box-tools pull-right">
                         <button class="btn btn-box-tool" data-widget="collapse"><i class="fa fa-minus"></i></button>
                     </div>
                     <!-- /.box-tools --> 
                 </div>
                 <!-- /.box-header -->
                 <div class="box-body inline-form">
					<div class="col-md-6  no-padding">
						<spring:message code="updtInstacne.system.log.threshold.enable.check" var="label"></spring:message>
						<spring:message code="updtInstacne.system.log.threshold.enable.check.tooltip" var="tooltip"></spring:message>
                        <div class="form-group">
                        	<div class="table-cell-label">${label}<span class="required">*</span></div>
                            <div class="input-group ">
                            	<form:select path="thresholdSysAlertEnable" cssClass="form-control table-cell input-sm" tabindex="4" name="thresholdSysAlertEnable" id="instance-thresholdSysAlertEnable" data-toggle="tooltip" data-placement="bottom"  title="${tooltip }" onchange="executionInterval()">
                            		<form:option value="true">True</form:option>
                            		<form:option value="false">False</form:option>
                            	</form:select>
                                <spring:bind path="thresholdSysAlertEnable">
									<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
								</spring:bind>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-6  no-padding">
						<spring:message code="updtInstacne.system.log.threshold.memory" var="label"></spring:message>
						<spring:message code="updtInstacne.system.log.threshold.memory.tooltip" var="tooltip"></spring:message>
                       	<div class="form-group">
                           	<div class="table-cell-label">${label}<span class="required">*</span></div>
                           	<div class="input-group ">
                           		<form:input path="thresholdMemory" cssClass="form-control table-cell input-sm" onkeydown='isNumericOnKeyDown(event)' tabindex="4" name="thresholdMemory" id="instance-thresholdMemory" data-toggle="tooltip" data-placement="bottom"  title="${tooltip }"></form:input>
                                <spring:bind path="thresholdMemory">
									<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
								</spring:bind>
      	                    </div>
                        </div>
                    </div>
                    <div class="col-md-6  no-padding">
						<spring:message code="updtInstacne.system.log.threshold.alert.interval" var="label"></spring:message>
						<spring:message code="updtInstacne.system.log.threshold.alert.interval.tooltip" var="tooltip"></spring:message>
                       	<div class="form-group">
                           	<div class="table-cell-label">${label}<span class="required">*</span></div>
                           	<div class="input-group ">
                           		<form:input path="thresholdTimeInterval" cssClass="form-control table-cell input-sm" onkeydown='isNumericOnKeyDown(event)' tabindex="4" name="thresholdTimeInterval" id="instance-thresholdTimeInterval" data-toggle="tooltip" data-placement="bottom"  title="${tooltip }"></form:input>
                                  <spring:bind path="thresholdTimeInterval">
									<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
								</spring:bind>
      	                    </div>
                         </div>
                    </div>
                    <div class="col-md-6  no-padding">
						<spring:message code="updtInstacne.system.log.threshold.load.average" var="tooltip"></spring:message>
                       	<div class="form-group">
                           	<div class="table-cell-label">${tooltip}<span class="required">*</span></div>
                            <div class="input-group ">
                            	<form:input path="loadAverage" cssClass="form-control table-cell input-sm" onkeydown='isNumericOnKeyDown(event)' tabindex="4" name="loadAverage" id="instance-loadAverage" data-toggle="tooltip" data-placement="bottom"  title="${tooltip }"></form:input>
                                <spring:bind path="loadAverage">
									<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
								</spring:bind>
       	                    </div>
                        </div>
                    </div>		
                 </div>
                 <!-- /.box-body --> 
             </div>
         </div>
    </div>
</form:form>
 </div>
 
 <script>
function submitSystemLogForm(){
	

	$("#serverInstance-updateSystemLog-form").submit();
} 

//* Code for Dependent parameter Closed

function executionInterval(){
	
	//document.write("Function Called or not");
	
	
	var thresholdSysAlertEnable = $("#instance-thresholdSysAlertEnable").val();
	
	

	if(thresholdSysAlertEnable=='false')
		
	{
			
			//document.write("Function Called or not");
		
		
			$("#instance-thresholdTimeInterval").attr('readonly',true);
			$("#instance-thresholdMemory").attr('readonly',true);
			$("#instance-loadAverage").attr('readonly',true);
		
	}
	else
	{
		//alert("Rang");
		$("#instance-thresholdTimeInterval").attr('readonly',false);
		$("#instance-thresholdMemory").attr('readonly',false);
		$("#instance-loadAverage").attr('readonly',false);
		
		
	}
} 

function resetSystemLogFields(){
	resetWarningDisplay();
	$("#instance-logsDetail-rollingValue").val(0);
	$("#instance-logsDetail-maxRollingUnit").val(0);
	$("#instance-logsDetail-logPathLocation").val('');
	$("#instance-thresholdMemory").val(0);
	$("#instance-thresholdTimeInterval").val(0);
	$("#instance-loadAverage").val(0);	
}	

$(document).ready(function() {
	
	
	
	 executionInterval();	
	 
	$('#lblInstanceName').text($('#instance-name').val());
	$('#lblInstanceHost').text($('#instance-server-ip').val());
	
	$("#serverInstance-updateSystemLog-form input,#serverInstance-updateSystemLog-form select,#serverInstance-updateSystemLog-form textarea").keypress(function (event) {
	    if (event.which == 13) {
	    	submitSystemLogForm();
	    }
	});
});


</script>
