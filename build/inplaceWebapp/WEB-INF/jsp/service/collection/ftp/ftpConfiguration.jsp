<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="/WEB-INF/ShowErrorTag.tld" prefix="elitecoreError" %>
<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>

<div class="tab-pane <c:if test="${(REQUEST_ACTION_TYPE eq 'FTP_CONFIGURATION')}"><c:out value="active"></c:out></c:if>" id="ftpConfig">
     <div class="tab-content no-padding  mtop15">    
	
	<form:form modelAttribute="driver_config_form_bean" method="POST" action="" id="ftp-configuration-form">
    	
    	
    	<form:hidden path="service.id" id="serviceId" value="${serviceId}"></form:hidden>
    	<input type="hidden" name="serviceName" id="serviceName" value="${serviceName}"/>
    	<input type="hidden" id="serviceInstanceId" name="serviceInstanceId" value="${serviceInstanceId}">
    	<input type="hidden" name="oldDriverName" id="oldDriverName" value="${driverName}"/>
    	<form:hidden path="id" id="driverId" value="${id}"></form:hidden>
    	<form:hidden path="driverType.id" id="driverTypeId" value="${driverType.id}"></form:hidden>
    	<form:hidden path="timeout" id="timeout" value="${timeout}"></form:hidden>
    	<form:hidden path="applicationOrder" id="applicationOrder" value="${applicationOrder}"></form:hidden>
    	<form:hidden path="status" id="status" value="${status}"></form:hidden>
		<form:hidden path="driverType.category" id="driverTypecategory" value="${driverType.category}"></form:hidden>
		<form:hidden path="driverType.description" id="driverTypedescription" value="${driverType.description}"></form:hidden>
		<form:hidden path="driverType.alias" id="driverAlias" value="${driverType.alias}"></form:hidden>
		<form:hidden path="driverType.driverFullClassName" id="driverFullClassName" value="${driverType.driverFullClassName}"></form:hidden>
		<form:hidden path="fileGroupingParameter.id" id="fileGroupingParameterId" value="${fileGroupingParameter.id}"></form:hidden>
		
		<c:if test="${(driverTypeAlias eq 'FTP_COLLECTION_DRIVER') or (driverTypeAlias eq 'SFTP_COLLECTION_DRIVER')}">
		<form:hidden path="ftpConnectionParams.id" id="ftpConnectionParamsid" value="${ftpConnectionParams.id}"></form:hidden>	

		</c:if>
		
    	<div class="fullwidth mtop15">
    	
             <div class="box box-warning">
                 <div class="box-header with-border">
                     <h3 class="box-title"><spring:message code="ftp.driver.mgmt.config.conn.dtl.header"></spring:message></h3>
                     <div class="box-tools pull-right">
                         <button class="btn btn-box-tool" data-widget="collapse"><i class="fa fa-minus"></i></button>
                     </div>
                     <!-- /.box-tools --> 
                 </div>	
                 <!-- /.box-header -->
                 <div class="box-body inline-form " >
                 	<div class="col-md-6 no-padding">
                    	<spring:message code="ftp.driver.mgmt.config.conn.dtl.driver.type" var="label"></spring:message>
                      	<spring:message code="ftp.driver.mgmt.config.conn.dtl.driver.type.tooltip" var="tooltip"></spring:message>
                      	<div class="form-group">
                        	<div class="table-cell-label">${label}</div>
                          	<div class="input-group ">
								<form:input path="driverType.type"
										cssClass="form-control table-cell input-sm" 
										id="ftp-driver-type" data-toggle="tooltip"
										data-placement="bottom" 
										title="${tooltip }" readonly="true"></form:input>
									<spring:bind path="driverType.type">
										<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
									</spring:bind>
								</div>
                         </div>
                    </div>
                 	<div class="col-md-6 no-padding">
                    	<spring:message code="ftp.driver.mgmt.config.conn.dtl.driver.nm" var="label"></spring:message>
                      	<spring:message code="ftp.driver.mgmt.config.conn.dtl.driver.nm.tooltip" var="tooltip"></spring:message>
                       	<div class="form-group">
                        	<div class="table-cell-label">${label}<span class="required">*</span></div>
                          	<div class="input-group ">
								<form:input path="name"
										cssClass="form-control table-cell input-sm" tabindex="1"
										id="ftp-driver-name" data-toggle="tooltip"
										data-placement="bottom" 
										title="${tooltip }"></form:input>
									<spring:bind path="name">
										<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
									</spring:bind> 	
                            </div>
                         </div>
                     </div>
                      <c:choose>
						<c:when test="${(driverTypeAlias eq 'FTP_COLLECTION_DRIVER') or (driverTypeAlias eq 'SFTP_COLLECTION_DRIVER')}">
                     		<jsp:include page="ftpConnectionDetail.jsp"></jsp:include>
                     	</c:when>
                     </c:choose>
                 </div>
                 <!-- /.box-body --> 
             </div>
        </div>
        <div class="fullwidth mtop15">
            <div class="box box-warning">
                <div class="box-header with-border">
                    <h3 class="box-title"><spring:message code="ftp.driver.mgmt.config.oper.param.header"></spring:message></h3>
                    <div class="box-tools pull-right">
                        <button class="btn btn-box-tool" data-widget="collapse"><i class="fa fa-minus"></i></button>
                    </div>
                    <!-- /.box-tools --> 
                </div>	
                <!-- /.box-header -->
                <div class="box-body inline-form " >
                
                	<c:choose>
						<c:when test="${(driverTypeAlias eq 'FTP_COLLECTION_DRIVER') or (driverTypeAlias eq 'SFTP_COLLECTION_DRIVER')}">
                     
                			<jsp:include page="ftpOperationalParams.jsp"></jsp:include>
                			
                		</c:when>
                	</c:choose>
					
                	<div class="col-md-6 no-padding" style="height:45px"> 
                    	<spring:message code="ftp.driver.mgmt.config.oper.param.file.seq.order" var="label"></spring:message>
                    	<spring:message code="ftp.driver.mgmt.config.oper.param.file.seq.order.tooltip" var="tooltip"></spring:message>
                      	
                      	<div class="form-group">
                        	<div class="table-cell-label">${label}</div>
                          	<div class="input-group ">
	                            <form:select path="fileSeqOrder" cssClass="form-control table-cell input-sm" id="ftp-ftpConnectionParams-fileSeqOrderEnable" tabindex="9" data-toggle="tooltip" data-placement="bottom"  title="${tooltip }">
	                             	<c:forEach var="trueFalseEnum" items="${trueFalseEnum}">
                             			<form:option value="${trueFalseEnum.name}">${trueFalseEnum}</form:option>
                             		</c:forEach>
								</form:select>
								<spring:bind path="fileSeqOrder">
									<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
								</spring:bind>		
                            </div>
                         </div>
                    </div>
                    
                    <div class="col-md-6 no-padding">
                    	<spring:message code="ftp.driver.mgmt.config.oper.param.max.retry.count" var="label"></spring:message>
                    	<spring:message code="ftp.driver.mgmt.config.oper.param.max.retry.count.tooltip" var="tooltip"></spring:message>
                      	<div class="form-group">
                        	<div class="table-cell-label">${label}</div>
                          	<div class="input-group ">
	                           	<form:input path="maxRetrycount" cssClass="form-control table-cell input-sm" id="ftp-maxRetrycount" onkeydown="isNumericOnKeyDown(event)" tabindex="13" data-toggle="tooltip" data-placement="bottom"  title="${tooltip }"></form:input>
								<spring:bind path="maxRetrycount">
									<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
								</spring:bind> 	
                            </div>
                         </div>
                     </div>
                     <div class="col-md-6 no-padding">
                    	<spring:message code="ftp.driver.mgmt.config.oper.param.file.range" var="tooltip"></spring:message>
                    	<spring:message code="ftp.driver.mgmt.config.oper.param.minfile.range" var="minRange"></spring:message>
                    	<spring:message code="ftp.driver.mgmt.config.oper.param.maxfile.range" var="maxRange"></spring:message>
                    	
                      	<div class="form-group">
                        	<div class="table-cell-label">${tooltip}</div>
                          	<div class="input-group ">
                          		<div class="col-md-6 no-padding">
                          			<div class="form-group">
                          				<div class="input-group">
                          					<form:input path="minFileRange" cssClass="form-control table-cell input-sm" id="ftp-minFileRange" tabindex="11" onkeydown="isNumericOnKeyDown(event)" data-toggle="tooltip" data-placement="bottom" title="${minRange}"></form:input>
											<spring:bind path="minFileRange">
												<elitecoreError:showError errorMessage="${status.errorMessage}" errorId="minFileRange_error"></elitecoreError:showError>
											</spring:bind> 	
                          				</div>
                          			</div>
                          		</div>
                          		<div class="col-md-6 no-padding">
                          			<div class="form-group">
                          				<div class="input-group">
                          					<form:input path="maxFileRange" cssClass="form-control table-cell input-sm" id="ftp-maxFileRange" tabindex="12" onkeydown="isNumericOnKeyDown(event)" data-toggle="tooltip" data-placement="bottom" title="${maxRange}"></form:input>
											<spring:bind path="maxFileRange">
												<elitecoreError:showError errorMessage="${status.errorMessage}" errorId="maxFileRange_error"></elitecoreError:showError>
											</spring:bind>
                          				</div>
                          			</div>
                          		</div>
                            </div>
                         </div>
                     </div> 
                   
                    <div class="col-md-6 no-padding">
						<spring:message code="ftp.driver.mgmt.config.oper.param.no.file.alert" var="label"></spring:message>
						<spring:message code="ftp.driver.mgmt.config.oper.param.no.file.alert.tooltip" var="tooltip"></spring:message>
						
					    <div class="form-group">
					    	<div class="table-cell-label">${label}</div>
					        <div class="input-group ">
					        	<form:input path="noFileAlert" cssClass="form-control table-cell input-sm" id="ftp-ftpConnectionParams-noFileAlert" tabindex="8" data-toggle="tooltip" data-placement="bottom"  title="${tooltip }"></form:input>
								<spring:bind path="noFileAlert">
									<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
								</spring:bind> 	
					       	</div>
					     </div>
					</div>
					
                </div>
                <!-- /.box-body --> 
            </div>
        </div>
        
        <c:choose>
			<c:when test="${(driverTypeAlias eq 'FTP_COLLECTION_DRIVER') or (driverTypeAlias eq 'SFTP_COLLECTION_DRIVER')}">
        		<jsp:include page="ftpFileFetchParams.jsp"></jsp:include>
        	</c:when>
        </c:choose>
        
       <jsp:include page="../../fileGroupingParameters.jsp"></jsp:include>
        <br/>
        
        <%-- <c:choose>
		  <c:when test="${(driverTypeAlias eq 'FTP_COLLECTION_DRIVER')}">
					</form:form>
		  </c:when>
		  <c:otherwise> --%>
		 </form:form>
		  
    </div>
</div>

<script>
	function resetFTPConfiguration(){
		resetWarningDisplay();
		clearAllMessages();
		
		var driverType = '${driverTypeAlias}';
		
		if(driverType == 'FTP_COLLECTION_DRIVER' || driverType == 'SFTP_COLLECTION_DRIVER'){
			$('#ftp-driver-name').val('');
			$('#ftp-ftpConnectionParams-iPAddressHost').val('');
			$('#ftp-ftpConnectionParams-port').val('');
			$('#ftp-ftpConnectionParams-username').val('');
			$('#ftp-ftpConnectionParams-password').val('');
			$('#ftp-ftpConnectionParams-activeDistribution').val('');
			$('#ftp-ftpConnectionParams-fileSeparator').val('');
			$('#ftp-ftpConnectionParams-timeout').val('');
			$('#ftp-ftpConnectionParams-noFileAlert').val('');
			$('#ftp-minFileRange').val('-1');
			$('#ftp-maxFileRange').val('-1');
			$('#ftp-maxRetrycount').val('');
			var fetchRuleValue=$("#ftp-myFileFetchParams-fileFetchRuleEnabled").find(":selected").text();
			
			if(fetchRuleValue == 'True'){
				$('#ftp-myFileFetchParams-FileFetchIntervalMin').val('');
				$('#ftp-myFileFetchParams-timeZone').val('');
			}
			
		}else if(driverType == 'LOCAL_COLLECTION_DRIVER'){
			$('#ftp-driver-name').val('');
			$('#ftp-ftpConnectionParams-noFileAlert').val('');
			$('#ftp-minFileRange').val('-1');
			$('#ftp-maxFileRange').val('-1');
			$('#ftp-maxRetrycount').val('');
		}
	}
	
	
	function updateConfiguration(){
		var driverType = '${driverTypeAlias}';
		var ftpMaxRetryCount=$('#ftp-maxRetrycount').val();
		var ftpNoFileAlert=$('#ftp-ftpConnectionParams-noFileAlert').val();
		var minFileRange = $('#ftp-minFileRange').val();
		var maxFileRange = $('#ftp-maxFileRange').val();
		if(ftpMaxRetryCount==null || ftpMaxRetryCount==''){
			$('#ftp-maxRetrycount').val('-1');
		} 
		if(ftpNoFileAlert==null || ftpNoFileAlert==''){
				$('#ftp-ftpConnectionParams-noFileAlert').val('-1');
		} 
		
		if(minFileRange == null || minFileRange == ''){
			$('#ftp-minFileRange').val("-1");
		}
		if(maxFileRange == null || maxFileRange == ''){
			$('#ftp-maxFileRange').val("-1");
		}
		
		if(driverType == 'FTP_COLLECTION_DRIVER'){
			$("#ftp-configuration-form").attr("action",'<%=ControllerConstants.UPDATE_FTP_COLLECTION_DRIVER_CONFIGURATION%>');
		}else if(driverType == 'SFTP_COLLECTION_DRIVER'){
			$("#ftp-configuration-form").attr("action",'<%=ControllerConstants.UPDATE_SFTP_COLLECTION_DRIVER_CONFIGURATION%>');
		}else if(driverType == 'LOCAL_COLLECTION_DRIVER'){
			$("#ftp-configuration-form").attr("action",'<%=ControllerConstants.UPDATE_LOCAL_COLLECTION_DRIVER_CONFIGURATION%>');
		}
		$("#ftp-configuration-form").submit();
	}
	function disableFileFetchParam(){
		
		var fetchRuleValue=$("#ftp-myFileFetchParams-fileFetchRuleEnabled").find(":selected").text();
		
		if(fetchRuleValue == 'False'){
			
			$("#ftp-myFileFetchParams-fileFetchType").prop('disabled', true);
			$("#ftp-myFileFetchParams-FileFetchIntervalMin").prop('readonly', true);
			$("#ftp-myFileFetchParams-timeZone").prop('readonly', true);
			
		}else{
			$("#ftp-myFileFetchParams-fileFetchType").prop('disabled', false);
			$("#ftp-myFileFetchParams-FileFetchIntervalMin").prop('readonly', false);
			$("#ftp-myFileFetchParams-timeZone").prop('readonly', false);
		}
		
	}
	
	function disableFileGroupParamForFTP(){		
	var fileGroupEnable=$("#fileGroupingParameter-fileGroupEnable").find(":selected").text();
		if(fileGroupEnable == 'False'){		
			$("#fileGroupingParameter-groupingType").prop('disabled', true);
			$("#ftp-fileGroupingParameter-archivePath").prop('readonly', true);
			$("#fileGroupingParameter-groupingDateType").prop('disabled', true);
		/** $("#fileGroupingParameter-enableForDuplicate").prop('disabled', true);
			$("#fileGrouping-duplicateDirPath").prop('disabled', true);  **/		
		}else{	
			$("#fileGroupingParameter-groupingType").prop('disabled', false);
			$("#ftp-fileGroupingParameter-archivePath").prop('readonly', false);
			$("#fileGroupingParameter-groupingDateType").prop('disabled', false);
        /** $("#fileGroupingParameter-enableForDuplicate").prop('disabled', false);
 			$("#fileGroupingParameter-enableForDuplicate").prop('disabled', false); 
 			$("#fileGrouping-duplicateDirPath").prop('disabled', false);  **/
		}
	}
	
	function disableFileGroupParamForDuplicate(){
		var fileGroupEnable=$("#fileGroupingParameter-fileGroupEnable").find(":selected").text();
		var enableForDuplicate=$("#fileGroupingParameter-enableForDuplicate").find(":selected").text();			
			if(enableForDuplicate == 'False'){
				$("#fileGroupingParameter-fileGroupEnable").prop('disabled', true);
				$("#fileGroupingParameter-groupingType").prop('disabled', true);
				$("#ftp-fileGroupingParameter-archivePath").prop('readonly', true);
				$("#fileGrouping-duplicateDirPath").prop('disabled', true);	
				$("#fileGroupingParameter-groupingDateType").prop('disabled', true);
			}else{			 
				$("#fileGroupingParameter-fileGroupEnable").prop('disabled', false);
				$("#fileGroupingParameter-groupingType").prop('disabled', false);
				$("#ftp-fileGroupingParameter-archivePath").prop('readonly', false);	 		 
	 			$("#fileGrouping-duplicateDirPath").prop('disabled', false);
	 			$("#fileGroupingParameter-groupingDateType").prop('disabled', false);
			}			
			if(fileGroupEnable == 'False' || enableForDuplicate == 'False'){				
				$("#fileGroupingParameter-groupingType").prop('disabled', true);	
				$("#fileGroupingParameter-groupingDateType").prop('disabled', true);
			}else{			 
				$("#fileGroupingParameter-groupingType").prop('disabled', false);
				$("#fileGroupingParameter-groupingDateType").prop('disabled', false);
			}
		}
	function disableFileGrpPrmForDBInit(serviceDbStats){				
			if(serviceDbStats == 'false'){
				$("#fileGroupingParameter-enableForDuplicate").prop('disabled', true);
				$("#fileGroupingParameter-fileGroupEnable").prop('disabled', true);
				$("#fileGroupingParameter-groupingType").prop('disabled', true);				
				$("#fileGrouping-duplicateDirPath").prop('disabled', true);
				$("#fileGroupingParameter-groupingDateType").prop('disabled', true);
				
			}
		}
	$(document).ready(function() {
		disableFileFetchParam();
		var serviceDbStats = '${serviceDbStats}';
		if(serviceDbStats=='false'){
			disableFileGrpPrmForDBInit(serviceDbStats);			
		}else{
			disableFileGroupParamForDuplicate();
		}
	//	disableFileGroupParamForFTP();
		
	});
	

</script>           
