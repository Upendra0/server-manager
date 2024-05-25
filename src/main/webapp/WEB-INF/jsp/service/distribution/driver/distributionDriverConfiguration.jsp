<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="/WEB-INF/ShowErrorTag.tld" prefix="elitecoreError" %>
<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>

<div class="tab-pane <c:if test="${(REQUEST_ACTION_TYPE eq 'DISTRIBUTION_DRIVER_CONFIGURATION')}"><c:out value="active"></c:out></c:if>" id="ftpConfig">
     <div class="tab-content no-padding  mtop15">    
	<form:form modelAttribute="driver_config_form_bean" method="POST" action="" id="ftp-configuration-form">
    	
    	<input type="hidden" name="instanceId" id="instanceId" value="${instanceId}"/>
    	<form:hidden path="service.id" id="serviceId" value="${serviceId}"></form:hidden>
    	<input type="hidden" name="serviceName" id="serviceName" value="${serviceName}"/>
    	<input type="hidden" name="oldDriverName" id="oldDriverName" value="${driverName}"/>
    	<input type="hidden" id="serviceInstanceId" name="serviceInstanceId" value="${serviceInstanceId}">
    	<input type="hidden" id="databaseInit" name="databaseInit" value="${databaseInit}">
    	<form:hidden path="id" id="driverId" value="${id}"></form:hidden>
    	<form:hidden path="driverType.id" id="driverTypeId" value="${driverType.id}"></form:hidden>
    	<form:hidden path="timeout" id="timeout" value="${timeout}"></form:hidden>
    	<form:hidden path="applicationOrder" id="applicationOrder" value="${applicationOrder}"></form:hidden>
    	<form:hidden path="status" id="status" value="${status}"></form:hidden>
    	
		<form:hidden path="driverType.category" id="driverTypecategory" value="${driverType.category}"></form:hidden>
		<form:hidden path="driverType.description" id="driverTypedescription" value="${driverType.description}"></form:hidden>
		<form:hidden path="driverType.alias" id="driverAlias" value="${driverType.alias}"></form:hidden>
		<form:hidden path="driverType.driverFullClassName" id="driverFullClassName" value="${driverType.driverFullClassName}"></form:hidden>
		
		
		<c:if test="${(driverTypeAlias eq 'FTP_DISTRIBUTION_DRIVER') or (driverTypeAlias eq 'SFTP_DISTRIBUTION_DRIVER')}">
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
										<elitecoreError:showError errorMessage="${status.errorMessage}" errorId="ftp-driver-type_error"></elitecoreError:showError>
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
										<elitecoreError:showError errorMessage="${status.errorMessage}" errorId="ftp-driver-name_error"></elitecoreError:showError>
									</spring:bind> 	
                            </div>
                         </div>
                     </div>
                      <c:choose>
						<c:when test="${(driverTypeAlias eq 'FTP_DISTRIBUTION_DRIVER') or (driverTypeAlias eq 'SFTP_DISTRIBUTION_DRIVER')}">
                     		<jsp:include page="distributionConnectionParams.jsp"></jsp:include>
                     	</c:when>
                     </c:choose>
                 </div>
                 <!-- /.box-body --> 
             </div>
        </div>
        
        <c:choose>
			<c:when test="${(driverTypeAlias eq 'DATABASE_DISTRIBUTION_DRIVER')}">
          		<div class="fullwidth mtop15">
    	
    		         <div class="box box-warning">
            		     <div class="box-header with-border">
                    		 <h3 class="box-title"><spring:message code="database.driver.mgmt.config.db.dtl.header"></spring:message></h3>
                    			 <div class="box-tools pull-right">
                     			    <button class="btn btn-box-tool" data-widget="collapse"><i class="fa fa-minus"></i></button>
                     			 </div>
                     <!-- /.box-tools --> 
                 		</div>	
                	 <!-- /.box-header -->
                 <div class="box-body inline-form " >
                    <div class="col-md-6 no-padding">
						<spring:message code="database.driver.mgmt.config.db.dtl.tbl.name.label" var="label"></spring:message>
						<spring:message code="database.driver.mgmt.config.db.dtl.tbl.name.label.tooltip" var="tooltip"></spring:message>
					    <div class="form-group">
					    	<div class="table-cell-label">${label}<span class="required">*</span></div>
					        <div class="input-group ">
					        	<form:input path="tableName" cssClass="form-control table-cell input-sm" id="database-tableName" tabindex="8" data-toggle="tooltip" data-placement="bottom"  title="${tooltip }"></form:input>
								<spring:bind path="tableName">
									<elitecoreError:showError errorMessage="${status.errorMessage}" errorId="database-tableName_error"></elitecoreError:showError>
								</spring:bind> 	
					       	</div>
					     </div>
					</div>
                 </div>
                 <!-- /.box-body --> 
             </div>
        </div>
        </c:when>
        </c:choose>
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
						<c:when test="${(driverTypeAlias eq 'FTP_DISTRIBUTION_DRIVER') or (driverTypeAlias eq 'SFTP_DISTRIBUTION_DRIVER')}">
                			<jsp:include page="distributionOperationalParams.jsp"></jsp:include>
                		</c:when>
                	</c:choose>
					
                	<div class="col-md-6 no-padding" style="height:45px"> 
                    	<spring:message code="ftp.driver.mgmt.config.oper.param.file.seq.order" var="label"></spring:message>
                    	<spring:message code="ftp.driver.mgmt.config.oper.param.file.seq.order.tooltip" var="tooltip"></spring:message>
                      	
                      	<div class="form-group">
                        	<div class="table-cell-label">${label}</div>
                          	<div class="input-group ">
	                            <form:select path="fileSeqOrder" cssClass="form-control table-cell input-sm" id="ftp-ftpConnectionParams-fileSeqOrderEnable" tabindex="9" data-toggle="tooltip" data-placement="top"  title="${tooltip }">
	                             	<c:forEach var="trueFalseEnum" items="${trueFalseEnum}">
                             			<form:option value="${trueFalseEnum.name}">${trueFalseEnum}</form:option>
                             		</c:forEach>
								</form:select>
								<spring:bind path="fileSeqOrder">
									<elitecoreError:showError errorMessage="${status.errorMessage}" errorId="ftp-ftpConnectionParams-fileSeqOrderEnable_error"></elitecoreError:showError>
								</spring:bind>		
                            </div>
                         </div>
                    </div>
                    
              <c:choose>
              <c:when test="${(driverTypeAlias eq 'FTP_DISTRIBUTION_DRIVER') or (driverTypeAlias eq 'SFTP_DISTRIBUTION_DRIVER') or (driverTypeAlias eq 'LOCAL_DISTRIBUTION_DRIVER')}">
                    <div class="col-md-6 no-padding">
                    	<spring:message code="ftp.driver.mgmt.config.oper.param.max.retry.count" var="label"></spring:message>
                    	<spring:message code="ftp.driver.mgmt.config.oper.param.max.retry.count.tooltip" var="tooltip"></spring:message>
                      	<div class="form-group">
                        	<div class="table-cell-label">${label}</div>
                          	<div class="input-group ">
	                           	<form:input path="maxRetrycount" cssClass="form-control table-cell input-sm" id="ftp-maxRetrycount" onkeydown="isNumericOnKeyDown(event)" tabindex="13" data-toggle="tooltip" data-placement="top"  title="${tooltip }"></form:input>
								<spring:bind path="maxRetrycount">
									<elitecoreError:showError errorMessage="${status.errorMessage}" errorId="ftp-maxRetrycount_error"></elitecoreError:showError>
								</spring:bind> 	
                            </div>
                         </div>
                     </div>
                 </c:when>
            </c:choose>
                     <div class="col-md-6 no-padding">
                    	<spring:message code="ftp.driver.mgmt.config.oper.param.file.range" var="tooltip"></spring:message>
                    	<spring:message code="ftp.driver.mgmt.config.oper.param.minfile.range" var="minRange"></spring:message>
                    	<spring:message code="ftp.driver.mgmt.config.oper.param.maxfile.range" var="maxRange"></spring:message>
                    	
                      	<div class="form-group">
                        	<div class="table-cell-label">${tooltip}</div>
                          	<div class="input-group ">
	                           	<form:input path="minFileRange" cssClass="form-control table-cell input-sm" id="ftp-minFileRange" tabindex="11" onkeydown="isNumericOnKeyDown(event)" data-toggle="tooltip" data-placement="bottom" title="${minRange}"></form:input>
								<spring:bind path="minFileRange">
									<elitecoreError:showError errorMessage="${status.errorMessage}" errorId="ftp-minFileRange_error"></elitecoreError:showError>
								</spring:bind> 	
								<form:input path="maxFileRange" cssClass="form-control table-cell input-sm" id="ftp-maxFileRange" tabindex="12" onkeydown="isNumericOnKeyDown(event)" data-toggle="tooltip" data-placement="bottom" title="${maxRange}"></form:input>
								<spring:bind path="maxFileRange">
									<elitecoreError:showError errorMessage="${status.errorMessage}" errorId="ftp-maxFileRange_error"></elitecoreError:showError>
								</spring:bind> 	
                            </div>
                         </div>
                     </div> 
                   
                    <div class="col-md-6 no-padding">
						<spring:message code="ftp.driver.mgmt.config.oper.param.no.file.alert" var="label"></spring:message>
						<spring:message code="ftp.driver.mgmt.config.oper.param.no.file.alert.tooltip" var="tooltip"></spring:message>
						
					    <div class="form-group">
					    	<div class="table-cell-label">${label}</div>
					        <div class="input-group ">
					        	<form:input path="noFileAlert" cssClass="form-control table-cell input-sm" id="ftp-ftpConnectionParams-noFileAlert" tabindex="8" data-toggle="tooltip" data-placement="top"  title="${tooltip }" onkeydown="isNumericOnKeyDown(event)"></form:input>
								<spring:bind path="noFileAlert">
									<elitecoreError:showError errorMessage="${status.errorMessage}" errorId="ftp-ftpConnectionParams-noFileAlert_error"></elitecoreError:showError>
								</spring:bind> 	
					       	</div>
					     </div>
					</div>
					
                </div>
                <!-- /.box-body --> 
            </div>
        </div>
        <!-- Control File Generation Parameters -->
        <c:choose>
			<c:when test="${(driverTypeAlias eq 'FTP_DISTRIBUTION_DRIVER') or (driverTypeAlias eq 'SFTP_DISTRIBUTION_DRIVER') or (driverTypeAlias eq 'LOCAL_DISTRIBUTION_DRIVER')}">
         		<jsp:include page="distributionControlFileParams.jsp"></jsp:include>
         	</c:when>
        </c:choose>
	</form:form>
		  
    </div>
</div>

<script>
	function resetFTPConfiguration(){
		resetWarningDisplay();
		clearAllMessages();
		
		var driverType = '${driverTypeAlias}';
		
		if(driverType == 'FTP_DISTRIBUTION_DRIVER' || driverType == 'SFTP_DISTRIBUTION_DRIVER'){
			$('#ftp-driver-name').val('');
			$('#ftp-ftpConnectionParams-iPAddressHost').val('');
			$('#ftp-ftpConnectionParams-port').val('');
			$('#ftp-ftpConnectionParams-username').val('');
			$('#ftp-ftpConnectionParams-password').val('');
			$('#ftp-ftpConnectionParams-fileSeparator').val('');
			$('#ftp-ftpConnectionParams-timeout').val('');
			$('#ftp-ftpConnectionParams-noFileAlert').val('');
			$('#ftp-minFileRange').val('');
			$('#ftp-maxFileRange').val('');
			$('#ftp-maxRetrycount').val('');
			
		}else if(driverType == 'LOCAL_DISTRIBUTION_DRIVER'){
			$('#ftp-driver-name').val('');
			$('#ftp-ftpConnectionParams-noFileAlert').val('');
			$('#ftp-minFileRange').val('');
			$('#ftp-maxFileRange').val('');
			$('#ftp-maxRetrycount').val('');
		}else if(driverType == 'DATABASE_DISTRIBUTION_DRIVER'){
			$('#ftp-driver-name').val('');
			$('#database-tableName').val('');
			$('#ftp-ftpConnectionParams-noFileAlert').val('');
			$('#ftp-minFileRange').val('');
			$('#ftp-maxFileRange').val('');
		}
		
		if(driverType != 'DATABASE_DISTRIBUTION_DRIVER'){
			$('#ftp-controlFileEnabled').val($('#ftp-controlFileEnabled option:first').val());
			$('#ftp-controlFileLocation').val('');
			$('#ftp-attributesSep_dropdown').val($('#ftp-attributesSep_dropdown option:first').val());
			$('#ftp-fileRollingDuration').val('');
			$('#ftp-fileRollingStartTime').val('');
			$('#ftp-controlFileName').val('');
			$('#ftp-fileSeqEnable').val($('#ftp-fileSeqEnable option:first').val());
			$('#controlFileSeq-resetFrequency').val($('#controlFileSeq-resetFrequency option:first').val());
			$('#controlFileSeq-startRange').val('');
			$('#controlFileSeq-endRange').val('');
			$('#controlFileSeq-paddingEnable').val($('#controlFileSeq-paddingEnable option:first').val());
		}		
	}
	
	$( document ).ready(function() {
		var driverType = '${driverTypeAlias}';
		
		if(driverType == 'FTP_DISTRIBUTION_DRIVER' || driverType == 'SFTP_DISTRIBUTION_DRIVER' || driverType == 'LOCAL_DISTRIBUTION_DRIVER'){
			$('#ftp-attributes_dropdown').multiselect({
		    	maxHeight: '200',
		    	buttonWidth: 'auto',
		        nonSelectedText: 'Select',
		        nSelectedText: 'selected',
		        numberDisplayed: 4,
		        enableFiltering: false
		    });
		    
			var ftp_attributes = $('#ftp-attributes').val();
			var ftp_attributesArr = ftp_attributes.split(",");
			$('#ftp-attributes_dropdown').val(ftp_attributesArr);
			$('#ftp-attributes_dropdown').multiselect("refresh");
		    
			var ftp_attrSep = $("#ftp-attributeSeparator").val();
			if (ftp_attrSep == " ") {
				$('#ftp-attributesSep_dropdown').val("s");
			} else {
				$('#ftp-attributesSep_dropdown').val(ftp_attrSep);
			}
			
			ChangecontrolFileParam();
		    removeSeparators();
		}
	});
	
	function removeSeparators()
	{
		$('#ftp-attributesSep_dropdown option').filter('[value="."],[value=":"],[value=";"],[value="-"],[value="_"],[value="Other"]').remove();
	}
	
	function ChangecontrolFileParam()
	{
		var controlFileEnable = $('#ftp-controlFileEnabled').val();
		if(controlFileEnable == 'true'){
			$('#ftp-controlFileLocation').attr('readonly',false);
			$('#ftp-attributes_dropdown').attr('disabled',false);
			$('#ftp-attributes_dropdown').multiselect('enable');
			$('#ftp-attributesSep_dropdown').attr('disabled',false);
			$('#ftp-fileRollingDuration').attr('readonly',false);
			$('#ftp-fileRollingStartTime').attr('readonly',false);
			$('#ftp-controlFileName').attr('readonly',false);
			
			var databaseInit = $("#databaseInit").val();
			if (databaseInit == 'true')
				$('#ftp-fileSeqEnable').attr('disabled',false);
			else {
				$('#ftp-fileSeqEnable').attr('disabled',true);
				$('#ftp-fileSeqEnable').val('false');
			}
			changeFileSequenceParams();
		} else {
			$('#ftp-controlFileLocation').attr('readonly',true);
			$('#ftp-attributes_dropdown').attr('disabled',true);
			$('#ftp-attributes_dropdown').multiselect('disable');
			$('#ftp-attributesSep_dropdown').attr('disabled',true);
			$('#ftp-fileRollingDuration').attr('readonly',true);
			$('#ftp-fileRollingStartTime').attr('readonly',true);
			$('#ftp-controlFileName').attr('readonly',true);
			$('#ftp-fileSeqEnable').attr('disabled',true);
			$('#controlFileSeq-resetFrequency').attr('disabled',true);
			$('#controlFileSeq-startRange').attr('readonly',true);
			$('#controlFileSeq-endRange').attr('readonly',true);
			$('#controlFileSeq-paddingEnable').attr('disabled',true);
			$('#ftp-fileSeqEnable').val('false');
		}
	}
	
	function changeFileSequenceParams(){
		var seqEnable = $('#ftp-fileSeqEnable').val();
		var databaseInit = $("#databaseInit").val();
		if(seqEnable=='true' && databaseInit == 'true'){
			$('#controlFileSeq-resetFrequency').attr('disabled',false);
			$('#controlFileSeq-paddingEnable').attr('disabled',false);
			$('#controlFileSeq-startRange').attr('readonly',false);
			$('#controlFileSeq-endRange').attr('readonly',false);
		} else {
			$('#controlFileSeq-resetFrequency').attr('disabled',true);
			$('#controlFileSeq-paddingEnable').attr('disabled',true);
			$('#controlFileSeq-startRange').attr('readonly',true);
			$('#controlFileSeq-endRange').attr('readonly',true);
		}
	}
		 
</script>

                                                    
