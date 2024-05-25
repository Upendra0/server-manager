<%@page import="com.elitecore.sm.common.constants.BaseConstants"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="/WEB-INF/ShowErrorTag.tld" prefix="elitecoreError" %>
   <script>
       
       function isNumberKey(evt)
       {
          var charCode = (evt.which) ? evt.which : evt.keyCode;
          if (charCode != 46 && charCode > 31 
            && (charCode < 48 || charCode > 57))
             return false;

          return true;
       }
       
       function allowNegativeNumber(e) 
       {
         var charCode = (e.which) ? e.which : event.keyCode
         if (charCode > 31 && (charCode < 45 || charCode > 57 )) {
           return false;
         }
         return true;
           
       }
    </script>
<div class="tab-pane <c:if test="${(REQUEST_ACTION_TYPE eq 'UPDATE_INSTANCE_ADVANCE_CONFIG')}"><c:out value="active"></c:out></c:if>" id="Advanced-Config">
 	<form:form modelAttribute="server_instance_form_bean" method="POST" action="<%= ControllerConstants.UPDATE_ADVANCE_CONFIG %>" id="serverInstance-form" enctype="multipart/form-data">
     <div class="box-body padding0">
     
     	<input type="hidden" id="instanceStatus" name="instanceStatus" value="${serverInstanceStatus}" />
     	<input type="hidden" id="instanceName" name="instanceName" value="${instanceName}"/>
     	<form:input type="hidden" path="id"  id="instance-id" ></form:input>
     	<form:input type="hidden" path="fileStorageLocation" name="fileStorageLocation"></form:input>
     	<form:input type="hidden" path="fileStatInDBEnable"  id="instance-fileStatInDBEnable" ></form:input>
     	<form:input type="hidden" path="snmpAlertEnable"  id="instance-snmpAlertEnable" ></form:input>
     	<form:input type="hidden" path="webservicesEnable"  id="instance-webservicesEnable" ></form:input>
     	<form:input type="hidden" path="logsDetail.level"  id="instance-logsDetail-level" ></form:input>
     	<form:input type="hidden" path="lastUpdatedDate" name="lastUpdatedDate" id="instance-lastUpdate"></form:input>
     	<form:input type="hidden" path="fileCdrSummaryDBEnable"  id="instance-fileCdrSummaryDBEnable" value="" ></form:input>
     	
     	<form:input type="hidden" path="server.id" name="server.id" id="instance-server-id"></form:input>
		<form:input type="hidden" path="server.name" name="server.name" id="instance-server-name"></form:input>
		<form:input type="hidden" path="server.description" name="server.description" id="instance-server-description"></form:input>
		<form:input type="hidden" path="server.utilityPort" name="server.utilityPort" id="instance-server-utilityport"></form:input>
		<form:input type="hidden" path="server.serverType.id" name="server.serverType.id" id="instance-server-serverType"></form:input>
		<form:input type="hidden" path="javaHome" name="javaHome" id="instance-javaHome"></form:input>
     	
     	<form:input type="hidden" path="logsDetail.level" name="logsDetail.level"></form:input>
		<form:input type="hidden" path="logsDetail.rollingType" name="logsDetail.rollingType"></form:input>
		<form:input type="hidden" path="logsDetail.rollingValue" name="logsDetail.rollingValue"></form:input>
		<form:input type="hidden" path="logsDetail.maxRollingUnit" name="logsDetail.maxRollingUnit"></form:input>
		<form:input type="hidden" path="logsDetail.logPathLocation" name="logsDetail.logPathLocation"></form:input>
		<form:input type="hidden" path="thresholdSysAlertEnable" name="thresholdSysAlertEnable"></form:input>
		<form:input type="hidden" path="thresholdMemory" name="thresholdMemory"></form:input>
		<form:input type="hidden" path="thresholdTimeInterval" name="thresholdTimeInterval"></form:input>
		<form:input type="hidden" path="loadAverage" name="loadAverage"></form:input>
		<%-- <form:input type="hidden" path="minMemoryAllocation" name="minMemoryAllocation"></form:input>
		<form:input type="hidden" path="maxMemoryAllocation" name="maxMemoryAllocation"></form:input> --%>
     	
		<input type="hidden" name="<%= BaseConstants.REQUEST_ACTION_TYPE %>"  id="<%= BaseConstants.REQUEST_ACTION_TYPE %>" value="<%= BaseConstants.UPDATE_INSTANCE_ADVANCE_CONFIG %>" />
        <div class="fullwidth ">
             <div class="box box-warning">
                 <div class="box-header with-border">
                     <h3 class="box-title"><spring:message code="updtInstacne.basic.details.tab.header"></spring:message></h3>
                     <div class="box-tools pull-right">
                         <button class="btn btn-box-tool" data-widget="collapse"><i class="fa fa-minus"></i></button>
                     </div>
                     <!-- /.box-tools --> 
                 </div>
                 <!-- /.box-header -->
                 <div class="box-body inline-form " >
                        <div class="col-md-6 no-padding">
                         	<spring:message code="addServerInstance.instance.name" var="label"></spring:message>
                         	<spring:message code="updtInstacne.srver.instance.name.tooltip" var="tooltip"></spring:message>
                          	<div class="form-group">
                            	<div class="table-cell-label">${label}<span class="required">*</span></div>
                              	<div class="input-group ">
                              		<form:input path="name" cssClass="form-control table-cell input-sm" tabindex="4" id="instance-name" data-toggle="tooltip" data-placement="bottom"   title="${tooltip }"></form:input>
                                    <spring:bind path="name">
										<elitecoreError:showError errorMessage="${status.errorMessage}" errorId="instance-name_error"></elitecoreError:showError>
									</spring:bind>	
                                </div>
                             </div>
                          </div>
                        <div class="col-md-6 no-padding">
                          	<spring:message code="updtInstacne.instance.desc" var="label"></spring:message>
                          	<spring:message code="updtInstacne.instance.desc.tooltip" var="tooltip"></spring:message>
                            <div class="form-group">
                            	<div class="table-cell-label">${label}</div>
                              	<div class="input-group ">
                              		<form:input path="description" cssClass="form-control table-cell input-sm" tabindex="4" id="instance-description" data-toggle="tooltip" data-placement="bottom"  title="${tooltip }"></form:input>
                                    <spring:bind path="description">
										<elitecoreError:showError errorMessage="${status.errorMessage}" errorId="instance-description_error"></elitecoreError:showError>
									</spring:bind>	
                                </div>
                            </div>
                          </div>
                        <div class="col-md-6 no-padding">
                          	<spring:message code="addServerInstance.instance.server.ip" var="label"></spring:message>
                          	<spring:message code="updtInstacne.host.ip.addr.tooltip" var="tooltip"></spring:message>
                            <div class="form-group">
                            	<div class="table-cell-label">${label}</div>
                              	<div class="input-group ">
                              		<form:input path="server.ipAddress" cssClass="form-control table-cell input-sm" tabindex="4" id="instance-server-ip" data-toggle="tooltip" data-placement="bottom"  title="${tooltip }" readonly="true"></form:input>
                                    <spring:bind path="server.ipAddress">
										<elitecoreError:showError errorMessage="${status.errorMessage}" errorId="instance-server-ip_error"></elitecoreError:showError>
									</spring:bind>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-6 no-padding">
                          	<spring:message code="addServerInstance.instance.port" var="label"></spring:message>
                          	<spring:message code="updtInstacne.host.port.tooltip" var="tooltip"></spring:message>
                           	<div class="form-group">
                                <div class="table-cell-label">${label}</div>
                              	<div class="input-group ">
                              		<form:input path="port" cssClass="form-control table-cell input-sm" tabindex="4" id="instance-port" data-toggle="tooltip" data-placement="bottom"  title="${tooltip }" readonly="true"></form:input>
                                    <spring:bind path="port">
										<elitecoreError:showError errorMessage="${status.errorMessage}" errorId="instance-port_error"></elitecoreError:showError>
									</spring:bind>
                                 </div>
                            </div>
                        </div>
                        <div class="col-md-6 no-padding">
                        	<spring:message code="addServerInstance.instance.max.connection.retry" var="tooltip"></spring:message>
                            <div class="form-group">
                            	<div class="table-cell-label">${tooltip}<span class="required">*</span></div>
                           		<div class="input-group ">
                              		<form:input path="maxConnectionRetry" cssClass="form-control table-cell input-sm" onkeydown='isNumericOnKeyDown(event)' tabindex="4" id="instance-maxConnectionRetry" data-toggle="tooltip" data-placement="bottom"  title="${tooltip }"></form:input>
                                    <spring:bind path="maxConnectionRetry">
										<elitecoreError:showError errorMessage="${status.errorMessage}" errorId="instance-maxConnectionRetry_error"></elitecoreError:showError>
									</spring:bind>
                                </div>
                             </div>
                        </div>
                        <div class="col-md-6 no-padding">
                         	<spring:message code="addServerInstance.instance.connection.retry.interval" var="tooltip"></spring:message>
                            <div class="form-group">
                            	<div class="table-cell-label">${tooltip}<span class="required">*</span></div>
                              	<div class="input-group ">
                              		<form:input path="retryInterval" cssClass="form-control table-cell input-sm" onkeydown='isNumericOnKeyDown(event)' tabindex="4" id="instance-retryInterval" data-toggle="tooltip" data-placement="bottom"  title="${tooltip }"></form:input>
                                    <spring:bind path="retryInterval">
										<elitecoreError:showError errorMessage="${status.errorMessage}" errorId="instance-retryInterval_error"></elitecoreError:showError>
									</spring:bind>
                                </div>
                             </div>
                        </div>
                        <div class="col-md-6 no-padding">
                           	<spring:message code="updtInstacne.connection.timeout" var="label"></spring:message>
                           	<spring:message code="updtInstacne.connection.timeout.tooltip" var="tooltip"></spring:message>
                            <div class="form-group">
                            	<div class="table-cell-label">${label}<span class="required">*</span></div>
                              	<div class="input-group ">
                              		<form:input path="connectionTimeout" cssClass="form-control table-cell input-sm" onkeydown='isNumericOnKeyDown(event)' tabindex="4" id="instance-connectionTimeout" data-toggle="tooltip" data-placement="bottom"  title="${tooltip }" ></form:input>
                                    <spring:bind path="connectionTimeout">
										<elitecoreError:showError errorMessage="${status.errorMessage}" errorId="instance-connectionTimeout_error"></elitecoreError:showError>
									</spring:bind>
                                </div>
                            </div>
                        </div>
                        
                        <div class="col-md-6 no-padding">
                           	<spring:message code="addServerInstance.instance.min.memory.alloc" var="label"></spring:message>
                           	<spring:message code="addServerInstance.instance.min.memory" var="tooltip"></spring:message>
                            <div class="form-group">
                            	<div class="table-cell-label">${label}<span class="required">*</span></div>
                              	<div class="input-group ">
                              		<form:input path="minMemoryAllocation" cssClass="form-control table-cell input-sm" onkeypress="return isNumber(arguments[0] || window.event);" tabindex="4" id="instance-connectionTimeout" data-toggle="tooltip" data-placement="bottom"  title="${tooltip }" ></form:input>
                                    <spring:bind path="minMemoryAllocation">
										<elitecoreError:showError errorMessage="${status.errorMessage}" errorId="instance-minMemoryAllocation_error"></elitecoreError:showError>
									</spring:bind>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-6 no-padding">
                           	<spring:message code="addServerInstance.instance.max.memory.alloc" var="label"></spring:message>
                           	<spring:message code="addServerInstance.instance.max.memory" var="tooltip"></spring:message>
                            <div class="form-group">
                            	<div class="table-cell-label">${label}<span class="required">*</span></div>
                              	<div class="input-group">
                              		<form:input path="maxMemoryAllocation" cssClass="form-control table-cell input-sm" onkeypress="return isNumber(arguments[0] || window.event);" tabindex="4" id="instance-connectionTimeout" data-toggle="tooltip" data-placement="bottom"  title="${tooltip }" ></form:input>
                                    <spring:bind path="maxMemoryAllocation">
										<elitecoreError:showError errorMessage="${status.errorMessage}" errorId="instance-maxMemoryAllocation_error"></elitecoreError:showError>
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
                     <h3 class="box-title"><spring:message code="updtInstacne.advance.config.tab.header"></spring:message></h3>
                     <div class="box-tools pull-right">
                         <button class="btn btn-box-tool" data-widget="collapse"><i class="fa fa-minus"></i></button>
                     </div>
                     <!-- /.box-tools --> 
                 </div>
                 <!-- /.box-header -->
                 <div class="box-body inline-form">
					<div class="col-md-6  no-padding">
						<spring:message code="addServerInstance.instance.script" var="label"></spring:message>
						<spring:message code="updtInstacne.script.name.tooltip" var="tooltip"></spring:message>
                       	<div class="form-group">
                           	<div class="table-cell-label">${label}<span class="required">*</span></div>
                            <div class="input-group ">
                            	<form:input path="scriptName" cssClass="form-control table-cell input-sm" tabindex="4" id="instance-scriptName" data-toggle="tooltip" data-placement="bottom"  title="${tooltip }"></form:input>
                                <spring:bind path="scriptName">
									<elitecoreError:showError errorMessage="${status.errorMessage}" errorId="instance-scriptName_error"></elitecoreError:showError>
								</spring:bind>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-6  no-padding">
						<spring:message code="updtInstacne.server.home" var="label"></spring:message>
						<spring:message code="updtInstacne.server.home.tooltip" var="tooltip"></spring:message>
                        <div class="form-group">
                        	<div class="table-cell-label">${label}</div>
                            <div class="input-group ">
                             	<form:input path="serverHome" cssClass="form-control table-cell input-sm" tabindex="4" id="instance-serverHome" data-toggle="tooltip" data-placement="bottom"  title="${tooltip }" readonly="true"></form:input>
                                <spring:bind path="serverHome">
									<elitecoreError:showError errorMessage="${status.errorMessage}" errorId="instance-serverHome_error"></elitecoreError:showError>
								</spring:bind>
                            </div>
                        </div>
                    </div>		
                    <div class="col-md-6  no-padding">
						<spring:message code="updtInstacne.minimum.disk.space" var="label"></spring:message>
						<spring:message code="updtInstacne.minimum.disk.space.tooltip" var="tooltip"></spring:message>
                        <div class="form-group">
                        	<div class="table-cell-label">${label}<span class="required">*</span></div>
                            <div class="input-group ">
                             	<form:input path="minDiskSpace" cssClass="form-control table-cell input-sm" tabindex="4" id="instance-minDiskSpace" onkeypress="return allowNegativeNumber(event);" data-toggle="tooltip" data-placement="bottom"  title="${tooltip }"></form:input>
                                <spring:bind path="minDiskSpace">
									<elitecoreError:showError errorMessage="${status.errorMessage}" errorId="instance-minDiskSpace_error"></elitecoreError:showError>
								</spring:bind>
                            </div>
                        </div>
                     </div>
                     
                     <div class="col-md-6  no-padding">
						<spring:message code="updtInstacne.engine.root.name" var="label"></spring:message>
						<spring:message code="updtInstacne.engine.root.name.tooltip" var="tooltip"></spring:message>
                        <div class="form-group">
                        	<div class="table-cell-label">${label}</div>
                            <div class="input-group ">
                             	<form:input path="mediationRoot" cssClass="form-control table-cell input-sm" tabindex="4" id="instance-mediationRoot" data-toggle="tooltip" data-placement="bottom"  title="${tooltip }" ></form:input>
                                <spring:bind path="mediationRoot">
									<elitecoreError:showError errorMessage="${status.errorMessage}" errorId="instance-mediationRoot_error"></elitecoreError:showError>
								</spring:bind>
                            </div>
                        </div>
                    </div>
                    
                     <div class="col-md-6  no-padding">
						<spring:message code="updtInstacne.engine.reprocessing.backup.path" var="label"></spring:message>
						<spring:message code="updtInstacne.engine.reprocessing.backup.path.tooltip" var="tooltip"></spring:message>
                        <div class="form-group">
                        	<div class="table-cell-label">${label}</div>
                            <div class="input-group ">
                             	<form:input path="reprocessingBackupPath" cssClass="form-control table-cell input-sm" tabindex="4" id="instance-reprocessingBackupPath" data-toggle="tooltip" data-placement="bottom"  title="${tooltip }" ></form:input>
                                <spring:bind path="reprocessingBackupPath">
									<elitecoreError:showError errorMessage="${status.errorMessage}" errorId="instance-reprocessingBackupPath_error"></elitecoreError:showError>
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
                     <h3 class="box-title"><spring:message code="updtInstacne.datasource.config.tab.header"></spring:message></h3>
                     <div class="box-tools pull-right">
                         <button class="btn btn-box-tool" data-widget="collapse"><i class="fa fa-minus"></i></button>
                     </div>
                     <!-- /.box-tools --> 
                 </div>
                 <!-- /.box-header -->
                 <div class="box-body inline-form">
                 	<div class="col-md-6  no-padding">
						<spring:message code="updtInstacne.data.source.enabled" var="label"></spring:message>
						<spring:message code="updtInstacne.data.source.enabled.tooltip" var="tooltip"></spring:message>
                        <div class="form-group">
                        	<div class="table-cell-label">${label}</div>
                            <div class="input-group ">
                             	<form:select path="databaseInit"
										cssClass="form-control table-cell input-sm" tabindex="4"
										id="databaseInit"
										data-toggle="tooltip" data-placement="bottom"
										 title="${tooltip }" onchange="toggleOnDataSourceInit();">
										<c:forEach items="${trueFalseEnum}" var="trueFalseEnum">
											<form:option value="${trueFalseEnum.name}" >${trueFalseEnum}</form:option>
										</c:forEach>
								</form:select>
								<spring:bind path="databaseInit">
									<elitecoreError:showError
										errorMessage="${status.errorMessage}" errorId="databaseInit_error"></elitecoreError:showError>
								</spring:bind>
                            </div>
                        </div>
                    </div>
                    
                    <div class="col-md-6  no-padding">
						<spring:message code="dbconfig.database.configuration.add.type.label" var="label"></spring:message>
						<spring:message code="dbconfig.database.configuration.add.type.label" var="tooltip"></spring:message>
                        <div class="form-group">
                        	<div class="table-cell-label">${label}</div>
                            <div class="input-group ">
                           
                             	<form:select path="serverManagerDatasourceConfig.type"
										cssClass="form-control table-cell input-sm" tabindex="4"
										id="databaseType"
										data-toggle="tooltip" data-placement="bottom"
										 title="${tooltip }" onchange="checkAndpopulateDSBasedOnType();">
										<c:forEach items="${dbSourcetypeEnumVal}" var="dbSourcetypeEnumVal">
											<form:option value="${dbSourcetypeEnumVal.value}" >${dbSourcetypeEnumVal}</form:option>
										</c:forEach>
								</form:select>
								
								<spring:bind path="serverManagerDatasourceConfig.type">
									<elitecoreError:showError
										errorMessage="${status.errorMessage}" errorId="databaseType_error"></elitecoreError:showError>
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
                     <h3 class="box-title"><spring:message code="updtInstacne.serverManager.datasource.config.tab.header"></spring:message></h3>
                     <div class="box-tools pull-right">
                         <button class="btn btn-box-tool" data-widget="collapse"><i class="fa fa-minus"></i></button>
                     </div>
                     <!-- /.box-tools --> 
                 </div>
                 <!-- /.box-header -->
                 <div class="box-body inline-form">
               	<div class="col-md-6  no-padding">
						<spring:message code="updtInstacne.data.source.name" var="label"></spring:message>
						<spring:message code="updtInstacne.data.source.name.tooltip" var="tooltip"></spring:message>
                        <div class="form-group">
                        	<div class="table-cell-label">${label}<span class="required">*</span></div>
                            <div class="input-group ">
                            	<c:set var="chk1" value="${server_instance_form_bean.serverManagerDatasourceConfig.id}"></c:set>
                             	
                             	<script>
                             		dsConfigList = {};
                             	    <c:forEach var="dsConfig" items="${dsConfigList}" >
                             	    	dsConfigList["${dsConfig.id}"]=["${dsConfig.name}","${dsConfig.connURL}","${dsConfig.username}","${dsConfig.password}"]
                             	    </c:forEach>
                             	</script>
                             	
                             	<form:select path="serverManagerDatasourceConfig.id" cssClass="form-control table-cell input-sm" tabindex="4" id="instance-serverManagerDatasourceConfig_name" data-toggle="tooltip" data-placement="bottom"  title="${tooltip }" onchange="changeDSConfigSM(this.value)">
                             	<c:forEach items="${dsConfigList}" var="dsConfig">
                             		<c:set var="val2" value="${dsConfig.id}"></c:set>
                             		<c:choose> 
                             			<c:when test="${chk1==val2}">
										<form:option value="${dsConfig.id}" selected="true">${dsConfig.name}</form:option>
										</c:when>
										<c:otherwise>
											<form:option value="${dsConfig.id}">${dsConfig.name}</form:option>
											</c:otherwise>
										</c:choose>
								</c:forEach>
								</form:select>
                                <spring:bind path="serverManagerDatasourceConfig.name">
									<elitecoreError:showError errorMessage="${status.errorMessage}" errorId="instance-serverManagerDatasourceConfig_name_error"></elitecoreError:showError>
								</spring:bind>
                             </div>
                        </div>
                     </div>
                    <div class="col-md-6  no-padding">
						<spring:message code="updtInstacne.data.source.url" var="label"></spring:message>
						<spring:message code="updtInstacne.data.source.url.tooltip" var="tooltip"></spring:message>
                        <div class="form-group">
                        	<div class="table-cell-label">${label}</div>
                            <div class="input-group ">
                             	<form:input path="serverManagerDatasourceConfig.connURL" cssClass="form-control table-cell input-sm" tabindex="4" id="instance-serverManagerDatasourceConfig.connURL" data-toggle="tooltip" data-placement="bottom"  title="${tooltip }" readonly="true"></form:input>
                               	<spring:bind path="serverManagerDatasourceConfig.connURL">
									<elitecoreError:showError errorMessage="${status.errorMessage}" errorId="instance-serverManagerDatasourceConfig.connURL_error"></elitecoreError:showError>
								</spring:bind>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-6  no-padding">
						<spring:message code="updtInstacne.data.source.username" var="label"></spring:message>
						<spring:message code="updtInstacne.data.source.username.tooltip" var="tooltip"></spring:message>
                        <div class="form-group">
                        	<div class="table-cell-label">${label}</div>
                            <div class="input-group ">
                             	<form:input path="serverManagerDatasourceConfig.username" cssClass="form-control table-cell input-sm" tabindex="4" id="instance-serverManagerDatasourceConfig.username" data-toggle="tooltip" data-placement="bottom"  title="${tooltip }" readonly="true"></form:input>
                               	<spring:bind path="serverManagerDatasourceConfig.username">
									<elitecoreError:showError errorMessage="${status.errorMessage}" errorId="instance-serverManagerDatasourceConfig.username_error"></elitecoreError:showError>
								</spring:bind>
                            </div>
                        </div>
                   </div>
                    <div class="col-md-6  no-padding">
						<spring:message code="updtInstacne.data.source.password" var="label"></spring:message>
						<spring:message code="updtInstacne.data.source.password.tooltip" var="tooltip"></spring:message>
                        <div class="form-group">
                        	<div class="table-cell-label">${label}</div>
                            <div class="input-group ">
                             	<form:input path="serverManagerDatasourceConfig.password" type="password" cssClass="form-control table-cell input-sm" tabindex="4" id="instance-serverManagerDatasourceConfig.password" data-toggle="tooltip" data-placement="bottom"  title="${tooltip }" readonly="true"></form:input>
                               	<spring:bind path="serverManagerDatasourceConfig.password">
									<elitecoreError:showError errorMessage="${status.errorMessage}" errorId="instance-serverManagerDatasourceConfig.password_error"></elitecoreError:showError>
								</spring:bind>
                            </div>
                        </div>
                   	</div>		
                 </div>
                 <!-- /.box-body --> 
             </div>
         </div> 
         
         
         
         
         
          
         <div class="fullwidth" id="iploggerDivId">
             <div class="box box-warning">
                 <div class="box-header with-border">
                     <h3 class="box-title"><spring:message code="updtInstacne.iplogger.datasource.config.tab.header"></spring:message></h3>
                     <div class="box-tools pull-right">
                         <button class="btn btn-box-tool" data-widget="collapse"><i class="fa fa-minus"></i></button>
                     </div>
                     <!-- /.box-tools --> 
                 </div>
                 <!-- /.box-header -->
                 <div class="box-body inline-form">
         	<div class="col-md-6  no-padding">
						<spring:message code="updtInstacne.data.source.name" var="label"></spring:message>
						<spring:message code="updtInstacne.data.source.name.tooltip" var="tooltip"></spring:message>
                        <div class="form-group">
                        	<div class="table-cell-label">${label}<span class="required">*</span></div>
                            <div class="input-group ">
                            	<c:set var="chk1" value="${server_instance_form_bean.iploggerDatasourceConfig.id}"></c:set>
                             	
                             	<script>
                             		dsConfigList = {};
                             	    <c:forEach var="dsConfig" items="${dsConfigList}" >
                             	    	dsConfigList["${dsConfig.id}"]=["${dsConfig.name}","${dsConfig.connURL}","${dsConfig.username}","${dsConfig.password}"]
                             	    </c:forEach>
                             	</script>
                             	
                             	<form:select path="iploggerDatasourceConfig.id" cssClass="form-control table-cell input-sm" tabindex="4" id="instance-iploggerDatasourceConfig_name" data-toggle="tooltip" data-placement="bottom"  title="${tooltip }" onchange="changeDSConfigIplogger(this.value)">
                             	<c:forEach items="${dsConfigList}" var="dsConfig">
                             		<c:set var="val2" value="${dsConfig.id}"></c:set>
                             		<c:choose> 
                             			<c:when test="${chk1==val2}">
										<form:option value="${dsConfig.id}" selected="true">${dsConfig.name}</form:option>
										</c:when>
										<c:otherwise>
											<form:option value="${dsConfig.id}">${dsConfig.name}</form:option>
											</c:otherwise>
										</c:choose>
								</c:forEach>
								</form:select>
                                <spring:bind path="iploggerDatasourceConfig.name">
									<elitecoreError:showError errorMessage="${status.errorMessage}" errorId="instance-iploggerDatasourceConfig_name_error"></elitecoreError:showError>
								</spring:bind>
                             </div>
                        </div>
                     </div>
                    <div class="col-md-6  no-padding">
						<spring:message code="updtInstacne.data.source.url" var="label"></spring:message>
						<spring:message code="updtInstacne.data.source.url.tooltip" var="tooltip"></spring:message>
                        <div class="form-group">
                        	<div class="table-cell-label">${label}</div>
                            <div class="input-group ">
                             	<form:input path="iploggerDatasourceConfig.connURL" cssClass="form-control table-cell input-sm" tabindex="4" id="instance-iploggerDatasourceConfig.connURL" data-toggle="tooltip" data-placement="bottom"  title="${tooltip }" readonly="true"></form:input>
                               	<spring:bind path="iploggerDatasourceConfig.connURL">
									<elitecoreError:showError errorMessage="${status.errorMessage}" errorId="instance-iploggerDatasourceConfig.connURL_error"></elitecoreError:showError>
								</spring:bind>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-6  no-padding">
						<spring:message code="updtInstacne.data.source.username" var="label"></spring:message>
						<spring:message code="updtInstacne.data.source.username.tooltip" var="tooltip"></spring:message>
                        <div class="form-group">
                        	<div class="table-cell-label">${label}</div>
                            <div class="input-group ">
                             	<form:input path="iploggerDatasourceConfig.username" cssClass="form-control table-cell input-sm" tabindex="4" id="instance-iploggerDatasourceConfig.username" data-toggle="tooltip" data-placement="bottom"  title="${tooltip }" readonly="true"></form:input>
                               	<spring:bind path="iploggerDatasourceConfig.username">
									<elitecoreError:showError errorMessage="${status.errorMessage}" errorId="instance-iploggerDatasourceConfig.username_error"></elitecoreError:showError>
								</spring:bind>
                            </div>
                        </div>
                   </div>
                    <div class="col-md-6  no-padding">
						<spring:message code="updtInstacne.data.source.password" var="label"></spring:message>
						<spring:message code="updtInstacne.data.source.password.tooltip" var="tooltip"></spring:message>
                        <div class="form-group">
                        	<div class="table-cell-label">${label}</div>
                            <div class="input-group ">
                             	<form:input path="iploggerDatasourceConfig.password" type="password" cssClass="form-control table-cell input-sm" tabindex="4" id="instance-iploggerDatasourceConfig.password" data-toggle="tooltip" data-placement="bottom"  title="${tooltip }" readonly="true"></form:input>
                               	<spring:bind path="iploggerDatasourceConfig.password">
									<elitecoreError:showError errorMessage="${status.errorMessage}" errorId="instance-iploggerDatasourceConfig.password_error"></elitecoreError:showError>
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
 
<script type="text/javascript">
function checkAndpopulateDSBasedOnType(){
	if($("#databaseInit").val() === 'true'){
		populateDSBasedOnType();
	}
}


function resetAdvanceConfigFields(){
	resetWarningDisplay();
	$("#instance-name").val('');
	$("#instance-description").val('');
	$("#instance-scriptName").val('');
	$("#instance-maxConnectionRetry").val('0');
	$('#instance-retryInterval').val('0');
	$('#instance-connectionTimeout').val('0');
	$('#instance-minDiskSpace').val('0');
}

function submitAdvanceConfigForm(){
	$("#instance-serverManagerDatasourceConfig_name").attr("disabled", false);
	$("#instance-iploggerDatasourceConfig_name").attr("disabled", false);
	var smDSName=$("#instance-serverManagerDatasourceConfig_name").val();
	var iploggerDSName=$("#instance-iploggerDatasourceConfig_name").val();
	var databaseInitVar=$("#databaseInit").val();
	var serverTyperId=$("#instance-server-serverType").val();
	if($('#instance-minDiskSpace').val()==''){
		$('#instance-minDiskSpace').val('-1');
	}
	if(serverTyperId!=3){
		$("#iploggerDivId").hide();	
	}
	
	 if(serverTyperId!=3 && databaseInitVar==true && smDSName==null ){
		showErrorMsg("<spring:message code='server.ds.select.mandatory'></spring:message>");
		
	}
	
	else if(serverTyperId==3 && iploggerDSName==null || (databaseInitVar==true && smDSName==null)){
		showErrorMsg("<spring:message code='server.ds.select.mandatory'></spring:message>");
		
	}
	
	else{
	$("#progress-bar-div").show();
	$("#serverInstance-form").submit();
		$("#instance-serverManagerDatasourceConfig_name").attr("disabled", true);
 	} 
}


$(document).ready(function() {
	var serverTyperId=$("#instance-server-serverType").val();
		if(serverTyperId!=3){
		$("#iploggerDivId").hide();	
	}
	//$('#lblInstanceName').text($('#instance-name').val());
	$('#lblInstanceHost').text($('#instance-server-ip').val());
	var smUrl=$("#instance-serverManagerDatasourceConfig.connURL").val();
	var iploggerUrl=$("#instance-iploggerDatasourceConfig.connURL").val();
	if(smUrl==undefined){
		$("#instance-serverManagerDatasourceConfig_name").attr("disabled", false);
		changeDSConfigSM($('#instance-serverManagerDatasourceConfig_name').val());
	}
	if(iploggerUrl==undefined){
		$("#instance-iploggerDatasourceConfig_name").attr("disabled", false);
		changeDSConfigIplogger($('#instance-iploggerDatasourceConfig_name').val());
	}

	$("#serverInstance-form input,#serverInstance-form select,#serverInstance-form textarea").keypress(function (event) {
	    if (event.which == 13) {
	    	submitAdvanceConfigForm();
	    }
	});
	//populateDSBasedOnType();
	toggleOnDataSourceInit();
});

function toggleOnDataSourceInit(){
	;
	if($("#databaseInit").val() === 'true'){
		
		$("#instance-serverManagerDatasourceConfig_name").val($('#instance-serverManagerDatasourceConfig_name').val());
		$("#instance-serverManagerDatasourceConfig_name").attr("disabled", false);
	}else{
		
		$("#instance-serverManagerDatasourceConfig_name").val($('#instance-serverManagerDatasourceConfig_name').val());
		$("#instance-serverManagerDatasourceConfig_name").attr("disabled", true);
		
		$("#instance-fileCdrSummaryDBEnable").val('false'); // setting cdr db summary flag disable as db init is false.
		
	}
}

function populateDSBasedOnType() {

  clearAllMessages();
  resetWarningDisplay();
  clearResponseMsgDiv();
  clearResponseMsg();
  clearErrorMsg();

  $.ajax({
    url: '<%=ControllerConstants.GET_DS_LIST_BY_TYPE%>',
    cache: false,
    async: true,
    dataType: 'json',
    type: "POST",
    data: {
      "dsType": $("#databaseType").val(),

    },
    success: function(data) {
     
    
      var response = eval(data);
      var responseCode = response.code;
      var responseMsg = response.msg;
      var responseObject = response.object;
      if (responseCode == "200") {



        var dataEvalVar = eval(responseObject);
              $('#instance-serverManagerDatasourceConfig_name option').each(function(index, option) {
            $(option).remove();
          });
        $('#instance-iploggerDatasourceConfig_name option').each(function(index, option) {
            $(option).remove();
          });
   
        $("#instance-serverManagerDatasourceConfig.connURL").val("");
        $("#instance-serverManagerDatasourceConfig.username").val("");
        $("#instance-serverManagerDatasourceConfig.password").val("");
        $("#instance-iploggerDatasourceConfig.connURL").val("");
        $("#instance-iploggerDatasourceConfig.username").val("");
        $("#instance-iploggerDatasourceConfig.password").val("");
        for (i = 0; i < dataEvalVar.length; i++) {
            var valObjVar=dataEvalVar[i];
            if(i==0){
          
            $('#instance-serverManagerDatasourceConfig_name').append("<option selected='selected' value=\"" + valObjVar.id + "\">" + valObjVar.name + "</option>");
            $('#instance-iploggerDatasourceConfig_name').append("<option selected='selected' value=\"" + valObjVar.id + "\">" + valObjVar.name + "</option>");
            document.getElementById("instance-serverManagerDatasourceConfig.connURL").value =valObjVar.connURL;
            document.getElementById("instance-serverManagerDatasourceConfig.username").value =valObjVar.username;
            document.getElementById("instance-serverManagerDatasourceConfig.password").value =valObjVar.password;
            document.getElementById("instance-iploggerDatasourceConfig.connURL").value =valObjVar.connURL;
            document.getElementById("instance-iploggerDatasourceConfig.username").value =valObjVar.username;
            document.getElementById("instance-iploggerDatasourceConfig.password").value =valObjVar.password;
            }
            else{
            	 $('#instance-serverManagerDatasourceConfig_name').append("<option value=\"" + valObjVar.id + "\">" + valObjVar.name + "</option>");
            	 $('#instance-iploggerDatasourceConfig_name').append("<option value=\"" + valObjVar.id + "\">" + valObjVar.name + "</option>");
            }
            
            }dbSourcetypeEnumVal
        
      

      } else if (responseObject != undefined && responseObject != 'undefined' && responseCode == "400") {
        // showErrorMsg(responseMsg);
   
         $('#instance-serverManagerDatasourceConfig_name option').each(function(index, option) {
             $(option).remove();
           });
         $('#instance-iploggerDatasourceConfig_name option').each(function(index, option) {
             $(option).remove();
           });
  		document.getElementById("instance-serverManagerDatasourceConfig.connURL").value = "";
 		document.getElementById("instance-serverManagerDatasourceConfig.username").value = "";
 		document.getElementById("instance-serverManagerDatasourceConfig.password").value = "";
 		document.getElementById("instance-iploggerDatasourceConfig.connURL").value = "";
 		document.getElementById("instance-iploggerDatasourceConfig.username").value = "";
 		document.getElementById("instance-iploggerDatasourceConfig.password").value = "";

        } else {
        // showErrorMsg(responseMsg);
       // $("#databaseType").val(valObjVar.tpe);
        
         $('#instance-serverManagerDatasourceConfig_name option').each(function(index, option) {
             $(option).remove();
           });
         $('#instance-iploggerDatasourceConfig_name option').each(function(index, option) {
             $(option).remove();
           });
 		document.getElementById("instance-serverManagerDatasourceConfig.connURL").value = "";
		document.getElementById("instance-serverManagerDatasourceConfig.username").value = "";
		document.getElementById("instance-serverManagerDatasourceConfig.password").value = "";
		document.getElementById("instance-iploggerDatasourceConfig.connURL").value = "";
		document.getElementById("instance-iploggerDatasourceConfig.username").value = "";
		document.getElementById("instance-iploggerDatasourceConfig.password").value = "";
      }
    },
    error: function(xhr, st, err) {
//       hideProgressBar("migration");
      handleGenericError(xhr, st, err);
    }
  });
}

function changeDSConfigSM(id) {
	var obj = dsConfigList[id];
	
	if(obj != undefined && obj.length > 0){
		$("#instance-serverManagerDatasourceConfig_name").val(id);
		document.getElementById("instance-serverManagerDatasourceConfig.connURL").value = obj[1];
		document.getElementById("instance-serverManagerDatasourceConfig.username").value = obj[2];
		document.getElementById("instance-serverManagerDatasourceConfig.password").value = obj[3];
	}
}

function changeDSConfigIplogger(id) {
	if(id!=0){
	var obj = dsConfigList[id];
	
	if(obj != undefined && obj.length > 0){
		$("#instance-iploggerDatasourceConfig_name").val(id);
		document.getElementById("instance-iploggerDatasourceConfig.connURL").value = obj[1];
		document.getElementById("instance-iploggerDatasourceConfig.username").value = obj[2];
		document.getElementById("instance-iploggerDatasourceConfig.password").value = obj[3];
	}
	}
	else{
		document.getElementById("instance-iploggerDatasourceConfig.connURL").value = '';
		document.getElementById("instance-iploggerDatasourceConfig.username").value = '';
		document.getElementById("instance-iploggerDatasourceConfig.password").value = '';
	}
}



</script>
