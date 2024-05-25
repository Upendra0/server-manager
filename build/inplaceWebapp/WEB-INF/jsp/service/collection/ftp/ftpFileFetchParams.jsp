<%@taglib uri="/WEB-INF/ShowErrorTag.tld" prefix="elitecoreError" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<div class="fullwidth mtop15">
            <div class="box box-warning">
                <div class="box-header with-border">
                    <h3 class="box-title"><spring:message code="ftp.driver.mgmt.config.file.fetch.header"></spring:message></h3>
                    <div class="box-tools pull-right">
                        <button class="btn btn-box-tool" data-widget="collapse"><i class="fa fa-minus"></i></button>
                    </div>
                    <!-- /.box-tools --> 
                </div>	
                <!-- /.box-header -->
                <div class="box-body inline-form " >
                	<div class="col-md-6 no-padding">
                    	
                    	<spring:message code="ftp.driver.mgmt.config.file.fetch.enable.file.fatch" var="label"></spring:message>
                    	<spring:message code="ftp.driver.mgmt.config.file.fetch.enable.file.fatch.tooltip" var="tooltip"></spring:message>
                    	
                      	<div class="form-group">
                        	<div class="table-cell-label">${label}</div>
                          	<div class="input-group ">
	                            <form:select path="myFileFetchParams.fileFetchRuleEnabled" cssClass="form-control table-cell input-sm" id="ftp-myFileFetchParams-fileFetchRuleEnabled" onChange="disableFileFetchParam();" tabindex="14" data-toggle="tooltip" data-placement="bottom"  title="${tooltip }">
	                             	<c:forEach var="trueFalseEnum" items="${trueFalseEnum}">
                             			<form:option value="${trueFalseEnum.name}">${trueFalseEnum}</form:option>
                             		</c:forEach>
								</form:select>
								<spring:bind path="myFileFetchParams.fileFetchRuleEnabled">
									<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
								</spring:bind>		
                            </div>
                         </div>
                    </div>
                    <div class="col-md-6 no-padding">
                    	<spring:message code="ftp.driver.mgmt.config.file.fetch.type" var="label"></spring:message>
                    	<spring:message code="ftp.driver.mgmt.config.file.fetch.type.tooltip" var="tooltip"></spring:message>
                      	<div class="form-group">
                        	<div class="table-cell-label">${label}<span class="required">*</span></div>
                          	<div class="input-group ">
	                            <form:select path="myFileFetchParams.fileFetchType" cssClass="form-control table-cell input-sm" id="ftp-myFileFetchParams-fileFetchType" tabindex="15" data-toggle="tooltip" data-placement="bottom"  title="${tooltip }">
	                             	<c:forEach var="fileFetchType" items="${fileFetchType}">
                             			<form:option value="${fileFetchType.value}">${fileFetchType}</form:option>
                             		</c:forEach>
								</form:select>
								<spring:bind path="myFileFetchParams.fileFetchType">
									<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
								</spring:bind>		
                            </div>
                         </div>
                    </div>
                    <div class="col-md-6 no-padding">
                    	<spring:message code="ftp.driver.mgmt.config.file.fetch.interval" var="label"></spring:message>
                    	<spring:message code="ftp.driver.mgmt.config.file.fetch.interval.tooltip" var="tooltip"></spring:message>
                    	
                      	<div class="form-group">
                        	<div class="table-cell-label">${label}</div>
                          	<div class="input-group ">
	                           	<form:input path="myFileFetchParams.FileFetchIntervalMin" cssClass="form-control table-cell input-sm" id="ftp-myFileFetchParams-FileFetchIntervalMin" onkeydown="isNumericOnKeyDown(event)" tabindex="16" data-toggle="tooltip" data-placement="bottom"  title="${tooltip }"></form:input>
								<spring:bind path="myFileFetchParams.FileFetchIntervalMin">
									<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
								</spring:bind> 	
                            </div>
                         </div>
                     </div>
                     <div class="col-md-6 no-padding">
                    	<spring:message code="ftp.driver.mgmt.config.file.fetch.timezone" var="label"></spring:message>
                    	<spring:message code="ftp.driver.mgmt.config.file.fetch.timezone.tooltip" var="tooltip"></spring:message>
                      	<div class="form-group">
                        	<div class="table-cell-label">${label}</div>
                          	<div class="input-group ">
	                           	<form:input path="myFileFetchParams.timeZone" cssClass="form-control table-cell input-sm" id="ftp-myFileFetchParams-timeZone" tabindex="17" data-toggle="tooltip" data-placement="bottom"  title="${tooltip }"></form:input>
								<spring:bind path="myFileFetchParams.timeZone">
									<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
								</spring:bind> 	
                            </div>
                         </div>
                     </div>
                </div>
                <!-- /.box-body --> 
            </div>
        </div>
