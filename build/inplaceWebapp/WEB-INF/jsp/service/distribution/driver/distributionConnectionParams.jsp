<%@taglib uri="/WEB-INF/ShowErrorTag.tld" prefix="elitecoreError" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<div class="col-md-6 no-padding">
	<spring:message code="ftp.driver.mgmt.config.conn.dtl.host.ip" var="label"></spring:message>
	<spring:message code="ftp.driver.mgmt.config.conn.dtl.host.ip.tooltip" var="tooltip"></spring:message>
	
    <div class="form-group">
    	<div class="table-cell-label">${label}<span class="required">*</span>
    	 &nbsp;<i class="fa fa-square"></i>
    	</div>
        <div class="input-group ">
        	<form:input path="ftpConnectionParams.iPAddressHost" cssClass="form-control table-cell input-sm" id="ftp-ftpConnectionParams-iPAddressHost" tabindex="2" data-toggle="tooltip" data-placement="bottom"  title="${tooltip }"></form:input>
			<spring:bind path="ftpConnectionParams.iPAddressHost">
				<elitecoreError:showError errorMessage="${status.errorMessage}" errorId="ftp-ftpConnectionParams-iPAddressHost_error"></elitecoreError:showError>
			</spring:bind> 	
	    </div>
     </div>
</div>
<div class="col-md-6 no-padding">
	<spring:message code="ftp.driver.mgmt.config.conn.dtl.host.port" var="label"></spring:message>
	<spring:message code="ftp.driver.mgmt.config.conn.dtl.host.port.tooltip" var="tooltip"></spring:message>
	
    <div class="form-group">
    	<div class="table-cell-label">${label}<span class="required">*</span>&nbsp;<i class="fa fa-square"></i></div>
        <div class="input-group ">
        	<form:input path="ftpConnectionParams.port" maxlength="5" cssClass="form-control table-cell input-sm" id="ftp-ftpConnectionParams-port" tabindex="3" onkeydown="isNumericOnKeyDown(event)"  data-toggle="tooltip" data-placement="bottom"  title="${tooltip }"></form:input>
			<spring:bind path="ftpConnectionParams.port">
				<elitecoreError:showError errorMessage="${status.errorMessage}" errorId="ftp-ftpConnectionParams-port_error"></elitecoreError:showError>
			</spring:bind> 	
       	</div>
     </div>
</div>
<c:if test="${(driverTypeAlias eq 'SFTP_DISTRIBUTION_DRIVER')}">
		<div class="col-md-6 no-padding">
			<spring:message code="ftp.driver.mgmt.config.conn.dtl.host.distribution.type" var="label"></spring:message>
			<spring:message code="ftp.driver.mgmt.config.conn.dtl.host.distribution.type.tooltip" var="tooltip"></spring:message>
			
		    <div class="form-group">
		    	<div class="table-cell-label">${label}</div>
		        <div class="input-group ">
		        	<form:checkbox path="ftpConnectionParams.activeDistribution"  id="ftp-ftpConnectionParams-activeDistribution" tabindex="2" data-toggle="tooltip" data-placement="bottom"  title="${tooltip }"></form:checkbox>
					<spring:bind path="ftpConnectionParams.activeDistribution">
						<elitecoreError:showError errorMessage="${status.errorMessage}" errorId="ftp-ftpConnectionParams-activeDistribution_error"></elitecoreError:showError>
					</spring:bind> 	
			    </div>
		     </div>
		</div>
	</c:if>	
<div class="col-md-6 no-padding">
	<spring:message code="ftp.driver.mgmt.config.conn.dtl.usernm" var="label"></spring:message>
	<spring:message code="ftp.driver.mgmt.config.conn.dtl.usernm.tooltip" var="tooltip"></spring:message>
	
    <div class="form-group">
    	<div class="table-cell-label">${label}<span class="required">*</span>&nbsp;<i class="fa fa-square"></i></div>
        <div class="input-group ">
        	<form:input path="ftpConnectionParams.username" cssClass="form-control table-cell input-sm" id="ftp-ftpConnectionParams-username" tabindex="4" data-toggle="tooltip" data-placement="bottom"  title="${tooltip }"></form:input>
			<spring:bind path="ftpConnectionParams.username">
				<elitecoreError:showError errorMessage="${status.errorMessage}" errorId="ftp-ftpConnectionParams-username_error"></elitecoreError:showError>
			</spring:bind> 	
        </div>
    </div>
</div>
<c:if test="${(driverTypeAlias eq 'FTP_DISTRIBUTION_DRIVER')}">
	<div class="col-md-6 no-padding">	
		<spring:message code="ftp.driver.mgmt.config.conn.dtl.pass" var="label"></spring:message>
		<spring:message code="ftp.driver.mgmt.config.conn.dtl.pass.tooltip" var="tooltip"></spring:message>
	    <div class="form-group">
	    	<div class="table-cell-label">${label}<span class="required">*</span>&nbsp;<i class="fa fa-square"></i></div>
	        <div class="input-group ">
	        	<form:input type="password" path="ftpConnectionParams.password" cssClass="form-control table-cell input-sm" id="ftp-ftpConnectionParams-password" tabindex="5" data-toggle="tooltip" data-placement="bottom"  title="${tooltip }"></form:input>
				<spring:bind path="ftpConnectionParams.password">
					<elitecoreError:showError errorMessage="${status.errorMessage}" errorId="ftp-ftpConnectionParams-password_error"></elitecoreError:showError>		
				</spring:bind> 	
	       	</div>
	     </div>
	</div> 
</c:if>
<c:if test="${(driverTypeAlias eq 'SFTP_DISTRIBUTION_DRIVER')}">
	<div class="col-md-6 no-padding">	
		<spring:message code="ftp.driver.mgmt.config.conn.dtl.pass" var="label"></spring:message>
		<spring:message code="ftp.driver.mgmt.config.conn.dtl.pass.tooltip" var="tooltip"></spring:message>
	    <div class="form-group">
	    	<div class="table-cell-label">${label}</div>
	        <div class="input-group ">
	        	<form:input type="password" path="ftpConnectionParams.password" cssClass="form-control table-cell input-sm" id="ftp-ftpConnectionParams-password" tabindex="5" data-toggle="tooltip" data-placement="bottom"  title="${tooltip }"></form:input>
				<spring:bind path="ftpConnectionParams.password">
					<elitecoreError:showError errorMessage="${status.errorMessage}" errorId="ftp-ftpConnectionParams-password_error"></elitecoreError:showError>		
				</spring:bind> 	
	       	</div>
	     </div>
	</div> 

	<div class="col-md-6 no-padding">	
		<spring:message code="ftp.driver.mgmt.config.conn.dtl.key.file.location" var="label"></spring:message>
		<spring:message code="ftp.driver.mgmt.config.conn.dtl.key.file.location.tooltip" var="tooltip"></spring:message>
	    <div class="form-group">
	    	<div class="table-cell-label">${label}</div>
	        <div class="input-group ">
	        	<form:input type="text" path="ftpConnectionParams.keyFileLocation" cssClass="form-control table-cell input-sm" id="ftp-ftpConnectionParams-keyFileLocation" tabindex="5" data-toggle="tooltip" data-placement="bottom"  title="${tooltip }"></form:input>
				<spring:bind path="ftpConnectionParams.keyFileLocation">
					<elitecoreError:showError errorMessage="${status.errorMessage}" errorId="ftp-ftpConnectionParams-keyFileLocation_error"></elitecoreError:showError>		
				</spring:bind> 	
	       	</div>
	     </div>
	</div> 
</c:if>
<div class="col-md-12 no-padding">	
	    <div class="form-group">
	        <div class="input-group ">
	               <jsp:include page="../../../common/testFtpSftpConnection.jsp">
			        	<jsp:param name="serviceType" value="DISTRIBUTION" ></jsp:param>
			        </jsp:include>
	       	</div>
	     </div>
</div>
