<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="/WEB-INF/ShowErrorTag.tld" prefix="elitecoreError" %>
<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>

 <div class="fullwidth mtop15">
            <div class="box box-warning">
                <div class="box-header with-border">
                    <h3 class="box-title"><spring:message code="file.grouping.param.section.header"></spring:message></h3>
                    <div class="box-tools pull-right">
                        <button class="btn btn-box-tool" data-widget="collapse"><i class="fa fa-minus"></i></button>
                    </div>
                    <!-- /.box-tools --> 
                </div>	
                <!-- /.box-header -->
                <div class="box-body inline-form " >
                
                <!-- start for Duplicate -->
                      	<c:if test="${(driverTypeAlias eq 'FTP_COLLECTION_DRIVER') or (driverTypeAlias eq 'SFTP_COLLECTION_DRIVER') or (driverTypeAlias eq 'LOCAL_COLLECTION_DRIVER')}">
                    <div class="col-md-6 no-padding">
                    	<spring:message code="file.grouping.enable.for.duplicate" var="label"></spring:message>
                    	<spring:message code="file.grouping.enable.for.duplicate.tooltip" var="tooltip"></spring:message>
                      	<div class="form-group">
                        	<div class="table-cell-label">${label}</div>
                          	<div class="input-group ">
	                            <form:select path="fileGroupingParameter.enableForDuplicate" cssClass="form-control table-cell input-sm" id="fileGroupingParameter-enableForDuplicate" tabindex="19" data-toggle="tooltip" data-placement="top"  title="${tooltip }">
	                             	<c:forEach var="trueFalseEnum" items="${trueFalseEnum}">
                             			<form:option value="${trueFalseEnum.name}">${trueFalseEnum}</form:option>
                             		</c:forEach>
								</form:select>
								<spring:bind path="fileGroupingParameter.enableForDuplicate">
									<elitecoreError:showError errorMessage="${status.errorMessage}" errorId="fileGroupingParameter-enableForDuplicate_error"></elitecoreError:showError>
								</spring:bind>		
                            </div>
                        </div>
                    </div>
                      <script type="text/javascript">
									$("#fileGroupingParameter-enableForDuplicate").on('change', function() {
										disableFileGroupParamForDuplicate();
									});
				    	</script>
                    </c:if>
                      	<!-- End of for Duplicate -->
                
                	<div class="col-md-6 no-padding">
                    	<spring:message code="file.grouping.enable.label" var="label"></spring:message>
                    	<spring:message code="file.grouping.enable.label.tooltip" var="tooltip"></spring:message>
                    	
                      	<div class="form-group">
                      	
                      	
                        	<div class="table-cell-label">${label}</div>
                          	<div class="input-group ">
	                            <form:select path="fileGroupingParameter.fileGroupEnable" cssClass="form-control table-cell input-sm" id="fileGroupingParameter-fileGroupEnable" tabindex="18" data-toggle="tooltip" data-placement="bottom"  title="${tooltip }">
	                             	<c:forEach var="trueFalseEnum" items="${trueFalseEnum}">
                             			<form:option value="${trueFalseEnum.name}">${trueFalseEnum}</form:option>
                             		</c:forEach>
								</form:select>
								<spring:bind path="fileGroupingParameter.fileGroupEnable">
									<elitecoreError:showError errorMessage="${status.errorMessage}" errorId="fileGroupingParameter-fileGroupEnable_error"></elitecoreError:showError>
								</spring:bind>	
								
								<c:if test="${service_config_form_bean.svctype.alias eq 'IPLOG_PARSING_SERVICE'}">
								<script>
									$("#fileGroupingParameter-fileGroupEnable").on('change', function() {
										enableFileGroupOptionForIplog($("#fileGroupingParameter-fileGroupEnable").find(":selected").val());
									});
								</script>
								</c:if>
								<c:if test="${service_config_form_bean.svctype.alias eq 'PARSING_SERVICE'}">
								<script>
									$("#fileGroupingParameter-fileGroupEnable").on('change', function() {
										enableFileGroupOptionForParsing($("#fileGroupingParameter-fileGroupEnable").find(":selected").val());
									});
								</script>
								</c:if>
								<c:if test="${(driverTypeAlias eq 'FTP_COLLECTION_DRIVER') or (driverTypeAlias eq 'SFTP_COLLECTION_DRIVER') or (driverTypeAlias eq 'LOCAL_COLLECTION_DRIVER')}">
								<script type="text/javascript">								 
									 $("#fileGroupingParameter-fileGroupEnable").on('change', function() {
										disableFileGroupParamForFTP();
									});
								</script>
								</c:if>	
								
                            </div>
                        </div>
                    </div>
                    
                    <!-- start of Duplicate Path -->
                    <c:if test="${(driverTypeAlias eq 'FTP_COLLECTION_DRIVER') or (driverTypeAlias eq 'SFTP_COLLECTION_DRIVER') or (driverTypeAlias eq 'LOCAL_COLLECTION_DRIVER')}">
					<div class="col-md-6 no-padding">
						<spring:message code="collection.configuration.file.group.duplicatepath" var="label"></spring:message>
                    	<spring:message code="collection.configuration.file.group.duplicatepath.tooltip" var="tooltip"></spring:message>
                      	
						<div class="form-group">
							<div class="table-cell-label">${label}</div>
							<div class="input-group ">
									<form:input path="fileGroupingParameter.duplicateDirPath"
										cssClass="form-control table-cell input-sm"
										id="fileGrouping-duplicateDirPath" data-toggle="tooltip"
										data-placement="bottom" 
										title="${tooltip }"></form:input>
									<spring:bind path="fileGroupingParameter.duplicateDirPath">
										<elitecoreError:showError
											errorMessage="${status.errorMessage}" errorId="fileGrouping-duplicateDirPath_error"></elitecoreError:showError>
									</spring:bind>
								</div>
						</div>
					</div>
					
					</c:if>
                    <!--End of Duplicate Path  -->
                    <div class="col-md-6 no-padding">
                    	<spring:message code="file.grouping.type.label" var="label"></spring:message>
                    	<spring:message code="file.grouping.type.label.tooltip" var="tooltip"></spring:message>
                      	<div class="form-group">
                        	<div class="table-cell-label">${label}</div>
                          	<div class="input-group ">
	                            <form:select path="fileGroupingParameter.groupingType" cssClass="form-control table-cell input-sm" id="fileGroupingParameter-groupingType" tabindex="19" data-toggle="tooltip" data-placement="top"  title="${tooltip }">
	                             	<c:forEach var="groupingType" items="${groupingType}">
                             			<form:option value="${groupingType}">${groupingType}</form:option>
                             		</c:forEach>
								</form:select>
								<spring:bind path="fileGroupingParameter.groupingType">
									<elitecoreError:showError errorMessage="${status.errorMessage}" errorId="fileGroupingParameter-groupingType_error"></elitecoreError:showError>
								</spring:bind>	
								
                            </div>
                        </div>
                    </div>
                    

			<c:if test="${(driverTypeAlias ne 'FTP_COLLECTION_DRIVER') and (driverTypeAlias ne 'SFTP_COLLECTION_DRIVER') and (driverTypeAlias ne 'LOCAL_COLLECTION_DRIVER')}">
					<div class="col-md-6 no-padding">
						<spring:message code="iplog.parsing.service.pathlist.archivePath" var="label" ></spring:message>
						<spring:message code="iplog.parsing.service.pathlist.archivePath" var="tooltip" ></spring:message>
						<div class="form-group">
							<div class="table-cell-label">${label}</div>
							<div class="input-group ">
									<form:input path="fileGroupingParameter.archivePath"
										cssClass="form-control table-cell input-sm"
										id="fileGrouping-archivePath" data-toggle="tooltip"
										data-placement="bottom" 
										title="${tooltip }"></form:input>
									<spring:bind path="fileGroupingParameter.archivePath">
										<elitecoreError:showError
											errorMessage="${status.errorMessage}" errorId="fileGrouping-archivePath_error"></elitecoreError:showError>
									</spring:bind>
								</div>
						</div>
					</div>
					
					</c:if>
					

		<!-- 	<c:if test="${(driverTypeAlias eq 'FTP_COLLECTION_DRIVER') or (driverTypeAlias eq 'SFTP_COLLECTION_DRIVER') or (driverTypeAlias eq 'LOCAL_COLLECTION_DRIVER')}">
                    <div class="col-md-6 no-padding">
                    	<spring:message code="file.grouping.enable.for.duplicate" var="label"></spring:message>
                    	<spring:message code="file.grouping.enable.for.duplicate.tooltip" var="tooltip"></spring:message>
                      	<div class="form-group">
                        	<div class="table-cell-label">${label}</div>
                          	<div class="input-group ">
	                            <form:select path="fileGroupingParameter.enableForDuplicate" cssClass="form-control table-cell input-sm" id="fileGroupingParameter-enableForDuplicate" tabindex="19" data-toggle="tooltip" data-placement="top"  title="${tooltip }">
	                             	<c:forEach var="trueFalseEnum" items="${trueFalseEnum}">
                             			<form:option value="${trueFalseEnum.name}">${trueFalseEnum}</form:option>
                             		</c:forEach>
								</form:select>
								<spring:bind path="fileGroupingParameter.enableForDuplicate">
									<elitecoreError:showError errorMessage="${status.errorMessage}" errorId="fileGroupingParameter-enableForDuplicate_error"></elitecoreError:showError>
								</spring:bind>		
                            </div>
                        </div>
                    </div>
                      <script type="text/javascript">
									$("#fileGroupingParameter-enableForDuplicate").on('change', function() {
										disableFileGroupParamForDuplicate();
									});
				    	</script>
                    </c:if>     -->
                    
            <!--     <c:if test="${(driverTypeAlias eq 'FTP_COLLECTION_DRIVER') or (driverTypeAlias eq 'SFTP_COLLECTION_DRIVER') or (driverTypeAlias eq 'LOCAL_COLLECTION_DRIVER')}">
					<div class="col-md-6 no-padding">
						<spring:message code="collection.configuration.file.group.duplicatepath" var="label"></spring:message>
                    	<spring:message code="collection.configuration.file.group.duplicatepath.tooltip" var="tooltip"></spring:message>
                      	
						<div class="form-group">
							<div class="table-cell-label">${label}</div>
							<div class="input-group ">
									<form:input path="fileGroupingParameter.duplicateDirPath"
										cssClass="form-control table-cell input-sm"
										id="fileGrouping-duplicateDirPath" data-toggle="tooltip"
										data-placement="bottom" 
										title="${tooltip }"></form:input>
									<spring:bind path="fileGroupingParameter.duplicateDirPath">
										<elitecoreError:showError
											errorMessage="${status.errorMessage}" errorId="fileGrouping-duplicateDirPath_error"></elitecoreError:showError>
									</spring:bind>
								</div>
						</div>
					</div>
					
					</c:if>  -->
                    
                    <c:if test="${(service_config_form_bean.svctype.alias eq 'PARSING_SERVICE') or 
                    				(distribution_service_configuration_form_bean.svctype.alias eq 'DISTRIBUTION_SERVICE')}">
                    <div class="col-md-6 no-padding">							
                    	<spring:message code="file.grouping.sourcewise.archive" var="label"></spring:message>
                    	<spring:message code="file.grouping.sourcewise.archive.tooltip" var="tooltip"></spring:message>
                      	<div class="form-group">
                        	<div class="table-cell-label">${label}</div>
                          	<div class="input-group ">
	                            <form:select path="fileGroupingParameter.sourcewiseArchive" cssClass="form-control table-cell input-sm" id="fileGroupingParameter-sourcewiseArchive"
	                             tabindex="19" data-toggle="tooltip" data-placement="top"  title="${tooltip }">
	                             	<c:forEach var="trueFalseEnum" items="${trueFalseEnum}">
                             			<form:option value="${trueFalseEnum.name}">${trueFalseEnum}</form:option>
                             		</c:forEach>
								</form:select>
								<spring:bind path="fileGroupingParameter.sourcewiseArchive">
									<elitecoreError:showError errorMessage="${status.errorMessage}" errorId="fileGroupingParameter-sourcewiseArchive_error"></elitecoreError:showError>
								</spring:bind>		
                            </div>
                        </div>
                    </div>
                    </c:if>
                    
                    <c:if test="${(driverTypeAlias eq 'FTP_COLLECTION_DRIVER') or (driverTypeAlias eq 'SFTP_COLLECTION_DRIVER') or (driverTypeAlias eq 'LOCAL_COLLECTION_DRIVER')
                    or (service_config_form_bean.svctype.alias eq 'PARSING_SERVICE') or 
                    				(distribution_service_configuration_form_bean.svctype.alias eq 'DISTRIBUTION_SERVICE')}">
	                    <div class="col-md-6 no-padding">
	                    	<spring:message code="file.grouping.date.type" var="label"></spring:message>
	                    	<spring:message code="file.grouping.date.type.tooltip" var="tooltip"></spring:message>
	                      	<div class="form-group">
	                        	<div class="table-cell-label">${label}</div>
	                          	<div class="input-group ">
		                            <form:select path="fileGroupingParameter.groupingDateType" cssClass="form-control table-cell input-sm" id="fileGroupingParameter-groupingDateType" tabindex="20" data-toggle="tooltip" data-placement="top"  title="${tooltip }">
		                             	<c:forEach var="groupDateTypeEnum" items="${groupDateTypeEnum}">
	                             			<form:option value="${groupDateTypeEnum.value}">${groupDateTypeEnum}</form:option>
	                             		</c:forEach>
									</form:select>
									<spring:bind path="fileGroupingParameter.groupingDateType">
										<elitecoreError:showError errorMessage="${status.errorMessage}" errorId="fileGroupingParameter-groupingDateType_error"></elitecoreError:showError>
									</spring:bind>		
	                            </div>
	                        </div>
	                    </div>
                    </c:if>
					
					 <%-- <c:if test="${service_config_form_bean.svctype.alias eq 'IPLOG_PARSING_SERVICE'}">
						<div class="col-md-6 no-padding">
							<spring:message code="file.grouping.archive.path" var="tooltip" ></spring:message>
							<div class="form-group">
								<div class="table-cell-label">${tooltip}<i class='fa fa-square'></i></div>
								<div class="input-group ">
									<form:input path="fileGroupingParameter.archivePath" cssClass="form-control table-cell input-sm" tabindex="4" id="fileGroupingParameter-archivePath" data-toggle="tooltip" data-placement="bottom" title="${tooltip}"></form:input>
									<!-- <script>
										enableArchiveOption('${iplog_parsing_service_configuration_form_bean.fileGroupingParameter.enableForArchive}');
										enableFileGroupOption('${iplog_parsing_service_configuration_form_bean.fileGroupingParameter.fileGroupEnable}');
									</script> -->
									<spring:bind path="fileGroupingParameter.archivePath">
										<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
									</spring:bind>
								</div>
							</div>
						</div>
                 </c:if> --%> 
               
                </div>
                <!-- /.box-body --> 
            </div>        
        </div>
        
