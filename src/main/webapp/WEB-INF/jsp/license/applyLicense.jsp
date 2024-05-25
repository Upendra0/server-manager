<%@page import="com.elitecore.sm.common.constants.LicenseConstants"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<div class="box-body padding0 mtop15">
	<div class="fullwidth">
			<div class="col-sm-12 inline-form" >
			<c:if test="${not empty failMessage}">
				<c:set var = "responseMsg" scope = "page" value = "${failMessage}"></c:set>
				<script>
					showErrorMsg('${responseMsg}');
				</script>
			</c:if>
			
			<input type="hidden" name="hostIP" id="hostIP" value="${hostIP}">
			<c:if test="${(componentType ne 'SERVER_MANAGER_CONTAINER')}">
				<div class="form-group">
					<div class="table-cell-label"><label><spring:message code="license.activation.crestel.product.label" ></spring:message><span class="required">*</span></label></div>
					<div class="input-group ">
						<c:if test="${SERVER_TYPE_LIST !=null && fn:length(SERVER_TYPE_LIST) gt 0}">
		             		<c:forEach items="${SERVER_TYPE_LIST}" var="serverType">
		             			<input class="checkboxbg"data-on="On" data-off="Off"  type="checkbox" id="checkboxes[${serverType.id}]" data-size="mini"   name="productType" value="${serverType.alias}">&nbsp;${serverType.name}&nbsp;
		             		</c:forEach>
	             		</c:if>
         			</div>
				</div>
			</c:if>
			</div>
			
		 <c:if test="${(componentType ne 'SERVER_MANAGER_CONTAINER')}">
			<div class="col-md-3 padding10 " >
				<spring:message code="license.apply.download.file.label" ></spring:message>
			</div>
			<div class="col-md-9 padding10 ">
				<c:choose>
				    <c:when test="${(componentType eq 'SERVER_MANAGER')}">
				 	      <a href="#" ><img src="<%=request.getContextPath()%>/img/download_license.png" height="20" width="20" onclick="downloadLicenseRegistrationForm();"/></a>
				    </c:when>
				    <c:otherwise>
				        <a href="#" onclick="downloadEngineLicenseRegistrationForm();"><img src="<%=request.getContextPath()%>/img/download_license.png" height="20" width="20"/></a>
				    </c:otherwise>
				</c:choose>
			</div>
		</c:if>
		
			<div class="col-md-3 padding10">
				<spring:message code="license.apply.upload.file.label" ></spring:message>	
			</div>
			<div class="col-md-3 padding10">
				<input type="file" id="licenseActivationKey" class="filestyle" data-buttonText="<spring:message code="serviceMgmt.browse.file"></spring:message>">
			</div>
			<div class="col-md-6 padding10">
				
			</div>
			<div class="col-md-12 padding10">
				
				<div align="left">
							<c:choose>
								<c:when test="${(componentType eq 'SERVER_MANAGER')}">
									<div id="activate_full_license_btn" >
										<button class="btn btn-grey btn-xs"  id ="activate_full_license_btn_apply" type="button" onclick="activateFullLicense('<%=ControllerConstants.ACTIVATE_FULL_LICENSE%>','${componentType}');" >
										<c:choose>
					   						 <c:when test="${(licenseType eq 'APPLY_LICENSE')}">
													<spring:message code="btn.label.activate.full.license" ></spring:message>
											  </c:when>	
												<c:otherwise>
													<spring:message code="btn.label.activate.renew.license" ></spring:message>
												</c:otherwise>
											</c:choose>	
										</button>
									</div>	
								</c:when>
								<c:when test="${(componentType eq 'SERVER_MANAGER_CONTAINER')}">
									<div id="activate_full_license_btn" >
										<button class="btn btn-grey btn-xs"  id ="activate_full_license_btn_apply" type="button" onclick="activateLicenseContainer('<%=ControllerConstants.ACTIVATE_LICENSE_CONTAINER%>','${componentType}');" >
											<spring:message code="btn.label.upgrade.container.license" ></spring:message>
										</button>
									</div>	
								</c:when>
								<c:otherwise>
									<div id="activate_full_license_btn" >
										<%-- <button class="btn btn-grey btn-xs" type="button" id ="activate_full_license_btn_apply" onclick="activateEngineFullLicense('<%=ControllerConstants.ACTIVATE_ENGINE_FULL_LICENSE%>','${serverInstanceId}');"> --%>
										<button class="btn btn-grey btn-xs" type="button" id ="activate_full_license_btn_apply" onclick="activateEngineFullLicense('<%=ControllerConstants.UPGRADE_ENGINE_DEFAULT_LICENSE%>','${serverTypeId}');">
											<c:choose>
						   						 <c:when test="${(licenseType eq 'APPLY_LICENSE')}">
														<spring:message code="btn.label.activate.full.license" ></spring:message>
												  </c:when>	
													<c:otherwise>
														<spring:message code="btn.label.activate.renew.license" ></spring:message>
													</c:otherwise>
												</c:choose>	
										</button>
									</div>
								</c:otherwise>
							</c:choose>
							<div id="activate_full_license_processbar" style="display: none;">
								<img src="img/processing-bar.gif">
							</div>
				</div>
			</div>
			<div class="col-md-12 padding10 note" >
				<label><spring:message code="label.important.note" ></spring:message></label>
			</div>
			<div class="col-md-12 padding10">
				<spring:message code="license.apply.key.duration.text" ></spring:message>
			</div>
			<div class="col-md-12 padding10">
				<c:choose>
				    <c:when test="${(licenseType eq 'APPLY_LICENSE')}">
				        <spring:message	code="license.apply.full.steps.title.text" ></spring:message>
				    </c:when>
				    <c:otherwise>
				        <spring:message	code="license.apply.renew.steps.title.text" ></spring:message>
				    </c:otherwise>
				</c:choose>
			</div>
			<div class="col-md-12 padding10">
				<ol>
					<li><spring:message code="license.apply.steps.text1" ></spring:message></li>
					<li><spring:message code="license.apply.steps.text2" ></spring:message></li>
					<li><spring:message code="license.apply.steps.text3" ></spring:message></li>
					<li><spring:message code="license.apply.steps.text4" ></spring:message></li>
				</ol>	
			</div>
	</div>
</div>
<!-- Product validation pop-up start here -->
<a href="#divcheckboxvalidation" class="fancybox" style="display: none;" id="checkboxServer">#</a>
		<div id="divcheckboxvalidation" style=" width:100%; display: none;" >
		    <div class="modal-content">
		        <div class="modal-header padding10">
		            <h4 class="modal-title"><spring:message code="product.Type.Select.header.label"></spring:message></h4>
		        </div>
		        <div class="modal-body padding10 inline-form">
			        <p id="Alert-License-product-checkbox-warning">
			       		 <spring:message code="license.alert.product.not.selected" ></spring:message>
			        </p>
		        </div>
			        <div id="delete-license-popup-progress-bar-div" class="modal-footer padding10" style="display: none;">
					<jsp:include page="../common/processing-bar.jsp"></jsp:include>
				</div>
					<div class="modal-footer padding10">
		            <button type="button" class="btn btn-grey btn-xs " data-dismiss="modal" onclick="closeFancyBox();"><spring:message code="btn.label.close"></spring:message></button>
		        </div>
		    </div>
		    <!-- /.modal-content --> 
		</div>
	<!-- ./wrapper -->


<!-- Product validation pop-up end here -->
<form action="<%=request.getContextPath()%>/<%= ControllerConstants.DOWNLOAD_LICENSE_FORM %>" method="POST" id="license_download_form">
</form>

<form action="<%=request.getContextPath()%>/<%= ControllerConstants.DOWNLOAD_ENGINE_LICENSE_FORM%>" method="POST" id="license_engine_download_form">
	<input type="hidden" id="serverTypeId" name="serverTypeId" value="${serverTypeId}" >
</form> 
