<%@page import="com.elitecore.sm.common.constants.SystemParametersConstant"%>
<%@page import="com.elitecore.sm.common.constants.BaseConstants"%>
<%@ taglib prefix="sec"	uri="http://www.springframework.org/security/tags"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page import="com.elitecore.sm.util.MapCache"%>

<!doctype html>
<html>

<jsp:include page="../common/newheader.jsp" ></jsp:include>

<body class="skin-blue sidebar-mini sidebar-collapse">
<div class="wrapper"> 
		<!-- Header Start -->
     
     	 <jsp:include page="../common/newtopNavigationPanel.jsp"></jsp:include>
     	 
    	<!-- Header End -->
    	<!-- sidebar Start -->
    	
    	<jsp:include page="../common/newleftMenu.jsp"></jsp:include>
            
    	<!-- sidebar End -->
        <div class="content-wrapper">	
			<section class="content-header">
				<div class="fullwidth">
				<div style="padding:0 10px 0 4px;">
				<div id="content-scroll-d" class="content-scroll">
				
				<div class="fullwidth">
                	<h4 style="margin-top:10px; margin-bottom:0px; font-size:11px;">
                	<a href="home.html"><img src="img/tile-icon.png" class="vmiddle" alt="Title Icon"/></a> 
                                    <span class="spanBreadCrumb" style="line-height: 30px;font-weight: bold;">
                                   		<strong>&nbsp;
                                   			<spring:message code="systemParameterManager.page.heading" ></spring:message>
                                   		</strong>&nbsp;
                                    </span>
                    </h4>
                    <sec:authorize access="hasAnyAuthority('VIEW_SYSTEM_PARAMETER') or hasAnyAuthority('MODIFY_SYSTEM_PARAMETER')">
                    <jsp:include page="../common/responseMsg.jsp" ></jsp:include>
                 	<div class="row">
                		<div class="col-xs-12">
                  		<div class="box martop" style="border:none; box-shadow: none;">
                    	<!-- /.box-header -->
                    	<div class="box-body table-responsive no-padding">
                    	
                    	<c:set var="generalParam" value="<%=SystemParametersConstant.GENERAL_PARAMETERS%>" scope="page" />
                    	<c:set var="editGenParam" value="<%=SystemParametersConstant.EDIT_GEN_SYSTEM_PARAM%>" scope="page" />
                    	<c:set var="passParam" value="<%=SystemParametersConstant.PASSWORD_PARAMETERS%>" scope="page" />
                    	<c:set var="editPassParam" value="<%=SystemParametersConstant.EDIT_PWD_SYSTEM_PARAM%>" scope="page" />
                    	<c:set var="custParam" value="<%=SystemParametersConstant.CUSTOMER_PARAMETERS%>" scope="page" />
                    	<c:set var="editCustParam" value="<%=SystemParametersConstant.EDIT_CUST_SYSTEM_PARAM%>" scope="page" />
                    	<c:set var="custLogoParam" value="<%=SystemParametersConstant.CUSTOMER_LOGO_PARAMETERS%>" scope="page" />
                    	<c:set var="editCustLogoParam" value="<%=SystemParametersConstant.EDIT_CUST_LOGO_SYSTEM_PARAM%>" scope="page" />
                    	<c:set var="fileReprocessingParam" value="<%=SystemParametersConstant.FILE_REPROCESSING_PARAMETERS%>" scope="page" />
                    	<c:set var="editFileReprocessingParam" value="<%=SystemParametersConstant.EDIT_FILE_REPROCESSING_PARAM%>" scope="page" />
                    	<c:set var="emailParam" value="<%=SystemParametersConstant.EMAIL_PARAM%>" scope="page" />
                    	<c:set var="editEmailParam" value="<%=SystemParametersConstant.EDIT_EMAIL_PARAMETER%>" scope="page" />
                    	<c:set var="ldapParam" value="<%=SystemParametersConstant.LOGIN_PARAM%>" scope="page" />
                    	<c:set var="ssoParam" value="<%=SystemParametersConstant.SSO_PARAM%>" scope="page" />
                    	<c:set var="editSsoParam" value="<%=SystemParametersConstant.EDIT_SSO_PARAMETER%>" scope="page" />
                    	<c:set var="editLdapParam" value="<%=SystemParametersConstant.EDIT_LOGIN_PARAMETER%>" scope="page" />
                    	<c:set var="isSSOEnabled" value="${isSSOEnabled}" scope="page" ></c:set>
                        
	                    <div class="nav-tabs-custom">
                    
                    	<!-- Tabs within a box -->
                    	  <ul class="nav nav-tabs">
                    		
                      		<li class="<c:if test="${(REQUEST_ACTION_TYPE eq generalParam) or (REQUEST_ACTION_TYPE eq editGenParam)}"><c:out value="active"></c:out></c:if>"><a href="#genGroup-tab" data-toggle="tab" id="genGroup-tab-a" onclick="systemParametersTabClicked('GENERAL_PARAM');"><spring:message code="systemParameter.genGroup.tab" ></spring:message></a></li>
							<c:if test="${isSSOEnabled eq 'false' }">  
								<li class="<c:if test="${(REQUEST_ACTION_TYPE eq passParam) or (REQUEST_ACTION_TYPE eq editPassParam)}"><c:out value="active"></c:out></c:if>"><a href="#pwdGroup-tab" data-toggle="tab" id="pwdGroup-tab-a" onclick="systemParametersTabClicked('PASSWORD_PARAM');"><spring:message code="systemParameter.pwdGroup.tab" ></spring:message></a></li>
                      		</c:if>
                      		<li class="<c:if test="${(REQUEST_ACTION_TYPE eq custParam) or (REQUEST_ACTION_TYPE eq editCustParam)}"><c:out value="active"></c:out></c:if>"><a href="#custGroup-tab" data-toggle="tab" id="custGroup-tab-a" onclick="systemParametersTabClicked('CUSTOMER_PARAM');"><spring:message code="systemParameter.custGroup.tab" ></spring:message></a></li>
                      		
                      		<li class="<c:if test="${(REQUEST_ACTION_TYPE eq custLogoParam) or (REQUEST_ACTION_TYPE eq editCustLogoParam)}"><c:out value="active"></c:out></c:if>"><a href="#custLogo-tab" data-toggle="tab" id="custLogo-tab-a" onclick="systemParametersTabClicked('CUSTOMER_LOGO');"><spring:message code="systemParameter.custLogo.tab" ></spring:message></a></li>
                      		
                      		<li class="<c:if test="${(REQUEST_ACTION_TYPE eq fileReprocessingParam) or (REQUEST_ACTION_TYPE eq editFileReprocessingParam)}"><c:out value="active"></c:out></c:if>"><a href="#fileReprocessing-tab" data-toggle="tab" id="fileReprocessing-tab-a" onclick="systemParametersTabClicked('FILE_REPROCESSING_PARAM');"><spring:message code="systemParameter.fileReprocessing.tab" ></spring:message></a></li>
                      		
                      		<sec:authorize access="hasAnyAuthority('MODIFY_EMAIL_SYSTEM_PARAMETER')">
                      			<li class="<c:if test="${(REQUEST_ACTION_TYPE eq emailParam) or (REQUEST_ACTION_TYPE eq editEmailParam)}"><c:out value="active"></c:out></c:if>"><a href="#emailParam-tab" data-toggle="tab" id="emailParam-tab-a" onclick="systemParametersTabClicked('EMAIL_PARAM');"><spring:message code="systemParameter.emailparam.tab" ></spring:message></a></li>
                      		</sec:authorize>
							<c:if test="${isSSOEnabled eq 'false' }">	
                      		<sec:authorize access="hasAnyAuthority('MODIFY_LDAP_SYSTEM_PARAMETER')">
                      			<li class="<c:if test="${(REQUEST_ACTION_TYPE eq ldapParam) or (REQUEST_ACTION_TYPE eq editLdapParam)}"><c:out value="active"></c:out></c:if>"><a href="#ldapParam-tab" data-toggle="tab" id="ldapParam-tab-a" onclick="systemParametersTabClicked('LOGIN_PARAM');"><spring:message code="systemParameter.loginparam.tab" ></spring:message></a></li>
                      		</sec:authorize>
                      		 </c:if> 
                      		<sec:authorize access="hasAnyAuthority('MODIFY_SSO_SYSTEM_PARAMETER')">
                      			<li class="<c:if test="${(REQUEST_ACTION_TYPE eq ssoParam) or (REQUEST_ACTION_TYPE eq editSsoParam)}"><c:out value="active"></c:out></c:if>"><a href="#ssoParam-tab" data-toggle="tab" id="ssoParam-tab-a" onclick="systemParametersTabClicked('SSO_PARAM');"><spring:message code="systemParameter.ssoparam.tab" ></spring:message></a></li>
                      		</sec:authorize> 
                    	  </ul> 
                    	  <div class="fullwidth tab-content no-padding">
                    	  	
                    	  	<div class="tab-pane <c:if test="${(REQUEST_ACTION_TYPE eq generalParam) or (REQUEST_ACTION_TYPE eq editGenParam)}"><c:out value="active"></c:out></c:if>" id="genGroup-tab" >
                    	  		<jsp:include page="systemParameter.jsp" ></jsp:include>
                    	  	</div>
                    	  	
						  	<div class="tab-pane <c:if test="${(REQUEST_ACTION_TYPE eq passParam) or (REQUEST_ACTION_TYPE eq editPassParam)}"><c:out value="active"></c:out></c:if>" id="pwdGroup-tab">
						  		<jsp:include page="passwordParams.jsp" ></jsp:include>
						  	</div>
						  	
						  	<div class="tab-pane <c:if test="${(REQUEST_ACTION_TYPE eq custParam) or (REQUEST_ACTION_TYPE eq editCustParam)}"><c:out value="active"></c:out></c:if>" id="custGroup-tab">
						  		<jsp:include page="customerParameters.jsp" ></jsp:include>
						  	</div>
						  	
						  	<div class="tab-pane <c:if test="${(REQUEST_ACTION_TYPE eq custLogoParam) or (REQUEST_ACTION_TYPE eq editCustLogoParam)}"><c:out value="active"></c:out></c:if>" id="custLogo-tab">
						  		<jsp:include page="customerLogoParameters.jsp" ></jsp:include>
						  	</div>
						  	
						  	<div class="tab-pane <c:if test="${(REQUEST_ACTION_TYPE eq fileReprocessingParam) or (REQUEST_ACTION_TYPE eq editFileReprocessingParam)}"><c:out value="active"></c:out></c:if>" id="fileReprocessing-tab" >
                    	  		<jsp:include page="fileReprocessingParameters.jsp" ></jsp:include>
                    	  	</div>
                    	  	
                    	  	<sec:authorize access="hasAnyAuthority('MODIFY_EMAIL_SYSTEM_PARAMETER')">
	                    	  	<div class="tab-pane <c:if test="${(REQUEST_ACTION_TYPE eq emailParam) or (REQUEST_ACTION_TYPE eq editEmailParam)}"><c:out value="active"></c:out></c:if>" id="emailParam-tab" >
	                    	  		<jsp:include page="emailParameter.jsp" ></jsp:include>
	                    	  	</div>
                    	  	</sec:authorize>
                    	  	
                    	  	<sec:authorize access="hasAnyAuthority('MODIFY_LDAP_SYSTEM_PARAMETER')">
	                    	  	<div class="tab-pane <c:if test="${(REQUEST_ACTION_TYPE eq ldapParam) or (REQUEST_ACTION_TYPE eq editLdapParam)}"><c:out value="active"></c:out></c:if>" id="ldapParam-tab" >
	                    	  		<jsp:include page="ldapParameter.jsp" ></jsp:include>
	                    	  	</div>
                    	  	</sec:authorize> 
                    	  	 <sec:authorize access="hasAnyAuthority('MODIFY_SSO_SYSTEM_PARAMETER')">
	                    	  	<div class="tab-pane <c:if test="${(REQUEST_ACTION_TYPE eq ssoParam) or (REQUEST_ACTION_TYPE eq editSsoParam)}"><c:out value="active"></c:out></c:if>" id="ssoParam-tab" >
	                    	  		<jsp:include page="ssoParameter.jsp" ></jsp:include>
	                    	  	</div>
                    	  	</sec:authorize>                 	  			
						  </div>
					    </div>
					  	</div>
					 </div>
					 </div>
					 </div>
					 
					 <div id="sysParamDBValue">
					 	
						<c:forEach var="systemParamDB" items="${system_param_db_wrapper}" varStatus="counter">
							<input type="hidden" id="systemParam[${counter.index}].hidden" value="${systemParamDB.value}" />
						</c:forEach>
						
						<input type="hidden" id="custSmallLogo" value="${CUSTOMER_LOGO}" />
						<input type="hidden" id="custLargeLogo" value="${CUSTOMER_LOGO_LARGE}" />
						
					 </div>
					 
				</sec:authorize>
			 </div>
			 </div>
			 </div>
			</div>
		</section>
	</div>

	<!-- Footer Start -->
	<footer class="main-footer positfix">
    	<div class="fooinn">
        	<div class="fullwidth">
               	<sec:authorize access="hasAuthority('MODIFY_SYSTEM_PARAMETER')">
	            	<div class="padleft-right" id="generalParameterBtnsDiv" style="display: none;">
                		<button class="btn btn-grey btn-xs " id="save-btn" type="button" onclick="validateFields();"><spring:message code="btn.label.save" ></spring:message></button>
           				<button class="btn btn-grey btn-xs"  id="reset-btn" type="button" onclick="resetForm();"><spring:message code="btn.label.reset" ></spring:message></button>
           				<button class="btn btn-grey btn-xs"  id="cancel-btn" type="button" onclick="cancelForm();"><spring:message code="btn.label.cancel" ></spring:message></button>
						<button class="btn btn-grey btn-xs"  id="edit-btn" style="display: none;" type="button" onclick="editSystemParam();"><spring:message code="btn.label.edit" ></spring:message></button>											
						<c:choose>
							<c:when test="${REQUEST_ACTION_TYPE eq 'EDIT_GEN_SYSTEM_PARAM'}">
										
								<script type="text/javascript">
									$( document ).ready(function() {
										editSystemParam();
									});
								</script>
							</c:when>
							<c:otherwise>
								<script type="text/javascript">
									$( document ).ready(function() {
										$("#genGroup-tab-a").html("<spring:message code='systemParameter.genGroup.tab' ></spring:message>");
										$("#page-heading-a").html("<spring:message code='systemParameter.genGroup.tab' ></spring:message>");
										
										var count = '${generalTotalRowCount}';
										for ( var i = 0; i < count; i++) {
											// loop through the value text boxes and disable them.
											document.getElementById("genParamListValue[" + i + "]").disabled =true;
										}
										$("#save-btn").hide();
										$("#reset-btn").hide();
										$("#cancel-btn").hide();
										$("#edit-btn").show();
									});

								</script>
							</c:otherwise>
						</c:choose>
		           	</div>
		           	
            		<div class="padleft-right" id="passwordParameterBtnsDiv" style="display: none;">
	            		<button class="btn btn-grey btn-xs " id="pwd-save-btn" type="button" onclick="validatePwdFields();"><spring:message code="btn.label.save" ></spring:message></button>
    				 	<button class="btn btn-grey btn-xs"  id="pwd-reset-btn" type="button" onclick="resetPwdForm();"><spring:message code="btn.label.reset" ></spring:message></button>
    				 	<button class="btn btn-grey btn-xs"  id="pwd-cancel-btn" type="button" onclick="cancelPwdForm();"><spring:message code="btn.label.cancel" ></spring:message></button>
    				 	<button class="btn btn-grey btn-xs" style="display:none;"  id="pwd-edit-btn" type="button" onclick="editPwdSystemParam();"><spring:message code="btn.label.edit" ></spring:message></button>
    				 	<c:choose>
							<c:when test="${REQUEST_ACTION_TYPE eq 'EDIT_PWD_SYSTEM_PARAM'}">
								<script type="text/javascript">
									$( document ).ready(function() {
										editPwdSystemParam();
									});
								</script>
							</c:when>
							<c:otherwise>
								<script type="text/javascript">
									$( document ).ready(function() {
										$("#pwdGroup-tab-a").html("<spring:message code='systemParameter.pwdGroup.tab' ></spring:message>");
										$("#page-heading-a").html("<spring:message code='systemParameter.pwdGroup.tab' ></spring:message>");
															
										document.getElementById("pwdTypeCombo").disabled =true;
															
										var count = '${passwordTotalRowCount}';
										for ( var i = 0; i < 2; i++) {
											document.getElementById("pwdParamListValue[" + i + "]").disabled =true;
										}
										for ( var i = 4; i < count; i++) {
											document.getElementById("pwdParamListValue[" + i + "]").disabled =true;
										}
										$("#pwd-save-btn").hide();
										$("#pwd-reset-btn").hide();
										$("#pwd-cancel-btn").hide();
										$("#pwd-edit-btn").show();
									});
								</script>
							</c:otherwise>
						</c:choose>
            		</div>
            		
            		<div class="padleft-right" id="fileReprocessingParameterBtnsDiv" style="display: none;">
	            		<button class="btn btn-grey btn-xs " id="fileReprocessing-save-btn" type="button" onclick="validateFileReprocessingFields();"><spring:message code="btn.label.save" ></spring:message></button>
    				 	<button class="btn btn-grey btn-xs"  id="fileReprocessing-reset-btn" type="button" onclick="resetFileReprocessingForm();"><spring:message code="btn.label.reset" ></spring:message></button>
    				 	<button class="btn btn-grey btn-xs"  id="fileReprocessing-cancel-btn" type="button" onclick="cancelFileReprocessingForm();"><spring:message code="btn.label.cancel" ></spring:message></button>
    				 	<button class="btn btn-grey btn-xs" style="display:none;"  id="fileReprocessing-edit-btn" type="button" onclick="editFileReprocessingParam();"><spring:message code="btn.label.edit" ></spring:message></button>
    				 	<c:choose>
							<c:when test="${REQUEST_ACTION_TYPE eq 'EDIT_FILE_REPROCESSING_PARAM'}">
								<script type="text/javascript">
									$( document ).ready(function() {
										editFileReprocessingParam();
									});
								</script>
							</c:when>
							<c:otherwise>
								<script type="text/javascript">
									$( document ).ready(function() {										
										var count = '${fileReprocessingTotalRowCount}';					
										for ( var i = 0; i < count; i++) {
											document.getElementById("fileReprocessingParamListValue[" + i + "]").disabled =true;
										}
										
										$("#fileReprocessing-save-btn").hide();
										$("#fileReprocessing-reset-btn").hide();
										$("#fileReprocessing-cancel-btn").hide();
										$("#fileReprocessing-edit-btn").show();
									});
								</script>
							</c:otherwise>
						</c:choose>
            		</div>
            		
            		<div class="padleft-right" id="customerParameterBtnsDiv" style="display: none;">
            			<button class="btn btn-grey btn-xs " id="cust-save-btn" type="button" onclick="validateCustFields();"><spring:message code="btn.label.save" ></spring:message></button>
					 	<button class="btn btn-grey btn-xs"  id="cust-reset-btn" type="button" onclick="resetCustForm();"><spring:message code="btn.label.reset" ></spring:message></button>
					 	<button class="btn btn-grey btn-xs"  id="cust-cancel-btn" type="button" onclick="cancelCustForm();"><spring:message code="btn.label.cancel" ></spring:message></button>
						<button class="btn btn-grey btn-xs"  id="cust-edit-btn" style="display: none;" type="button" onclick="editCustSystemParam();"><spring:message code="btn.label.edit" ></spring:message></button>
					 	<c:choose>
							<c:when test="${REQUEST_ACTION_TYPE eq 'EDIT_CUST_SYSTEM_PARAM'}">
								<script type="text/javascript">
									$( document ).ready(function() {
										editCustSystemParam();														
									});
								</script>
							</c:when>
							<c:otherwise>
								<script type="text/javascript">
									$( document ).ready(function() {
										$("#custGroup-tab-a").html("<spring:message code='systemParameter.custGroup.tab' ></spring:message>");
										$("#page-heading-a").html("<spring:message code='systemParameter.custGroup.tab' ></spring:message>");
															
										var count = '${customerTotalRowCount}';
										for ( var i = 0; i < count; i++) {
											document.getElementById("custParamListValue[" + i + "]").disabled =true;
										}
										$("#cust-save-btn").hide();
										$("#cust-reset-btn").hide();
										$("#cust-cancel-btn").hide();
										$("#custLogofile").parent("div").hide();
										$("#custLargeLogofile").parent("div").hide();
										$("#cust-edit-btn").show();
									});
								</script>
							</c:otherwise>
						</c:choose>
            		</div>
            		
            		<div class="padleft-right" id="customerLogoParameterBtnsDiv" style="display: none;">
            			
						<button class="btn btn-grey btn-xs"  id="custLogo-edit-btn" style="display: none;" type="button" onclick="editCustLogoSystemParam();"><spring:message code="btn.label.edit" ></spring:message></button>
					 	<c:choose>
							<c:when test="${REQUEST_ACTION_TYPE eq 'EDIT_CUST_LOGO_SYSTEM_PARAM'}">
								<script type="text/javascript">
									$( document ).ready(function() {
										editCustLogoSystemParam();														
									});
								</script>
							</c:when>
							<c:otherwise>
								<script type="text/javascript">
									$( document ).ready(function() {
										$("#custLogo-tab-a").html("<spring:message code='systemParameter.custLogo.tab' ></spring:message>");
										$("#page-heading-a").html("<spring:message code='systemParameter.custLogo.tab' ></spring:message>");
															
										var count = '${customerLogoTotalRowCount}';
										for ( var i = 1; i < count; i++) {
											document.getElementById("custLogoParamListValue[" + i + "]").disabled =true;
										}
										$("#custLogofile").parent("div").hide();
										$("#custLargeLogofile").parent("div").hide();
										$("#small-logo-upload").hide();
										$("#large-logo-upload").hide();
										$("#custLogo-edit-btn").show();
									});
								</script>
							</c:otherwise>
						</c:choose>
            		</div>
            		<div class="padleft-right" id="emailParameterBtnsDiv" style="display: none;">
            			<button class="btn btn-grey btn-xs " id="email-save-btn" type="button" onclick="validateEmailParamFields();"><spring:message code="btn.label.save" ></spring:message></button>
					 	<button class="btn btn-grey btn-xs"  id="email-reset-btn" type="button" onclick="resetEmailParamForm();"><spring:message code="btn.label.reset" ></spring:message></button>
					 	<button class="btn btn-grey btn-xs"  id="email-cancel-btn" type="button" onclick="cancelEmailParamForm();"><spring:message code="btn.label.cancel" ></spring:message></button>
						<button class="btn btn-grey btn-xs"  id="email-edit-btn" style="display: none;" type="button" onclick="editEmailParam();"><spring:message code="btn.label.edit" ></spring:message></button>
					 	<c:choose>
							<c:when test="${REQUEST_ACTION_TYPE eq 'EDIT_EMAIL_PARAM'}">
								<script type="text/javascript">
									$( document ).ready(function() {
										editEmailParam();														
									});
								</script>
							</c:when>
							<c:otherwise>
								<script type="text/javascript">
									$( document ).ready(function() {
										$("#emailParam-tab-a").html("<spring:message code='systemParameter.emailparam.tab' ></spring:message>");
										$("#page-heading-a").html("<spring:message code='systemParameter.emailparam.tab' ></spring:message>");
										var count = '${emailTotalRowCount}';
										for ( var i = 0; i < count; i++) {
											document.getElementById("emailParamListValue[" + i + "]").disabled =true;
										}
										$("#footerImageFile").attr("disabled",true);
										$("#email-footer-image-upload").hide();
										$("#email-save-btn").hide();
										$("#email-reset-btn").hide();
										$("#email-cancel-btn").hide();
										$("#email-footer-logo-form").hide();
										$("#custLogofile").parent("div").hide();
										$("#custLargeLogofile").parent("div").hide();
										$("#email-edit-btn").show();
									});
								</script>
							</c:otherwise>
						</c:choose>
            		</div>
            		<div class="padleft-right" id="ldapParameterBtnsDiv" style="display: none;">
            			<button class="btn btn-grey btn-xs " id="ldap-save-btn" type="button" onclick="validateLdapParamFields();"><spring:message code="btn.label.save" ></spring:message></button>            			
					 	<button class="btn btn-grey btn-xs"  id="ldap-reset-btn" type="button" onclick="resetLdapParamForm();"><spring:message code="btn.label.reset" ></spring:message></button>
					 	<button class="btn btn-grey btn-xs"  id="ldap-cancel-btn" type="button" onclick="cancelLdapParamForm();"><spring:message code="btn.label.cancel" ></spring:message></button>					 	
						<button class="btn btn-grey btn-xs"  id="ldap-edit-btn" style="display: none;" type="button" onclick="editLdapParam();"><spring:message code="btn.label.edit" ></spring:message></button>
												
					 	<c:choose>
							<c:when test="${REQUEST_ACTION_TYPE eq 'EDIT_LOGIN_PARAM'}">
								<script type="text/javascript">
									$( document ).ready(function() {
										editLdapParam();														
									});
								</script>
							</c:when>
							<c:otherwise>
								<script type="text/javascript">
									$( document ).ready(function() {
										$("#ldapParam-tab-a").html("<spring:message code='systemParameter.loginparam.tab' ></spring:message>");
										$("#page-heading-a").html("<spring:message code='systemParameter.loginparam.tab' ></spring:message>");
										var count = '${ldapTotalRowCount}';
										if(!${isSSOEnabled}){
											for ( var i = 0; i < count; i++) {
												document.getElementById("ldapParamListValue[" + i + "]").disabled =true;
											}
										}
										$("#ldap-save-btn").hide();
										$("#ldap-reset-btn").hide();
										$("#ldap-cancel-btn").hide();
										$("#custLogofile").parent("div").hide();
										$("#custLargeLogofile").parent("div").hide();
										$("#ldap-edit-btn").show();
									});
								</script>
							</c:otherwise>
						</c:choose>
            		</div>
            		<div class="padleft-right" id="ssoParameterBtnsDiv" style="display: none;">
            			<button class="btn btn-grey btn-xs " id="sso-save-btn" type="button" onclick="validateSSOParamFields();"><spring:message code="btn.label.save" ></spring:message></button>            			
					 	<button class="btn btn-grey btn-xs"  id="sso-reset-btn" type="button" onclick="resetSSOParamForm();"><spring:message code="btn.label.reset" ></spring:message></button>
					 	<button class="btn btn-grey btn-xs"  id="sso-cancel-btn" type="button" onclick="cancelSSOParamForm();"><spring:message code="btn.label.cancel" ></spring:message></button>					 	
						<button class="btn btn-grey btn-xs"  id="sso-edit-btn" style="display: none;" type="button" onclick="editSSOParam();"><spring:message code="btn.label.edit" ></spring:message></button>
						<c:choose>
						<c:when test="${isSSOEnabled eq 'true' }">
							<button class="btn btn-grey btn-xs " id="staff-migrate-btn" type="button" onclick="migrateStaffToKeycloak();"><spring:message code="btn.label.migrate.staff" ></spring:message></button>
						</c:when>
						<c:otherwise>
							<button class="btn btn-grey btn-xs " id="staff-migrate-btn" type="button" disabled="disabled"><spring:message code="btn.label.migrate.staff" ></spring:message></button>
						</c:otherwise>
						</c:choose>
												
					 	<c:choose>
							<c:when test="${REQUEST_ACTION_TYPE eq 'EDIT_SSO_PARAM'}">
								<script type="text/javascript">
									$( document ).ready(function() {
										editSSOParam();														
									});
								</script>
							</c:when>
							<c:otherwise>
								<script type="text/javascript">
									$( document ).ready(function() {
										$("#ssoParam-tab-a").html("<spring:message code='systemParameter.ssoparam.tab' ></spring:message>");
										$("#page-heading-a").html("<spring:message code='systemParameter.ssoparam.tab' ></spring:message>");
										var count = '${ssoTotalRowCount}';
										for ( var i = 0; i < count; i++) {
											document.getElementById("ssoParamListValue[" + i + "]").disabled =true;
										}
								
										$("#sso-save-btn").hide();
										$("#sso-reset-btn").hide();
										$("#sso-cancel-btn").hide();
										$("#staff-migrate-btn").show();
										$("#custLogofile").parent("div").hide();
										$("#custLargeLogofile").parent("div").hide();
										$("#sso-edit-btn").show();
									});
								</script>
							</c:otherwise>
						</c:choose>
            		</div>
            		</sec:authorize>
                	<sec:authorize access="!hasAuthority('MODIFY_SYSTEM_PARAMETER')">
                	<script type="text/javascript">
                		$( document ).ready(function() {
                			var count = '${generalTotalRowCount}';
							for ( var i = 0; i < count; i++) {
								// loop through the value text boxes and disable them.
								document.getElementById("genParamListValue[" + i + "]").disabled =true;
							}
							
							document.getElementById("pwdTypeCombo").disabled =true;
							
							var count = '${passwordTotalRowCount}';
							for ( var i = 0; i < 2; i++) {
								document.getElementById("pwdParamListValue[" + i + "]").disabled =true;
							}
							for ( var i = 4; i < count; i++) {
								document.getElementById("pwdParamListValue[" + i + "]").disabled =true;
							}
							var count = '${customerTotalRowCount}';
							for ( var i = 2; i < count; i++) {
								document.getElementById("custParamListValue[" + i + "]").disabled =true;
							}
							var count = '${customerLogoTotalRowCount}';
							for ( var i = 1; i < count; i++) {
								document.getElementById("custLogoParamListValue[" + i + "]").disabled =true;
							}
							for ( var i = 0; i < 2; i++) {
								document.getElementById("fileReprocessingParamListValue[" + i + "]").disabled =true;
							}
							$("#custLogofile").parent("div").hide();
							$("#custLargeLogofile").parent("div").hide();
                		
                		});
                	</script>
                	
                </sec:authorize>
        	</div>
        	<script type="text/javascript">
        		function systemParametersTabClicked(tabType){
        			clearResponseMsg();
        			showHideButtonBasedOnTabsSelected(tabType);
        		}
        		function showHideButtonBasedOnTabsSelected(tabType){
        			
        			$("#generalParameterBtnsDiv").hide();
    				$("#passwordParameterBtnsDiv").hide();
    				$("#customerParameterBtnsDiv").hide();
    				$("#customerLogoParameterBtnsDiv").hide();
    				$("#fileReprocessingParameterBtnsDiv").hide();
    				$("#emailParameterBtnsDiv").hide();
    				$("#ldapParameterBtnsDiv").hide();
    				$("#ssoParameterBtnsDiv").hide();
    				
        			if(tabType == 'GENERAL_PARAM'){
        				$("#generalParameterBtnsDiv").show();
        			}else if(tabType == 'PASSWORD_PARAM'){
        				$("#passwordParameterBtnsDiv").show();
        			}else if(tabType == 'CUSTOMER_PARAM'){
        				$("#customerParameterBtnsDiv").show();
        			}else if(tabType == 'CUSTOMER_LOGO'){
        				$("#customerLogoParameterBtnsDiv").show();
        			}else if(tabType == 'FILE_REPROCESSING_PARAM'){
        				$("#fileReprocessingParameterBtnsDiv").show();
        			}else if(tabType == 'EMAIL_PARAM'){
        				$("#emailParameterBtnsDiv").show();
        			}else if(tabType == 'LOGIN_PARAM'){
        				$("#ldapParameterBtnsDiv").show();
        			}else if(tabType == 'SSO_PARAM'){
        				$("#ssoParameterBtnsDiv").show();
        			}
        			
        		}
        		$( document ).ready(function() {
        			var activeTab = $(".nav-tabs li.active a");
        			var id = activeTab.attr("id");

        			if(id == 'genGroup-tab-a'){
        				showHideButtonBasedOnTabsSelected('GENERAL_PARAM');
        			}else if(id == 'pwdGroup-tab-a'){
        				showHideButtonBasedOnTabsSelected('PASSWORD_PARAM');
        			}else if(id == 'custGroup-tab-a'){
        				showHideButtonBasedOnTabsSelected('CUSTOMER_PARAM');
        			}else if(id == 'custLogo-tab-a'){
        				showHideButtonBasedOnTabsSelected('CUSTOMER_LOGO');
        			}else if(id == 'fileReprocessing-tab-a'){
        				showHideButtonBasedOnTabsSelected('FILE_REPROCESSING_PARAM');
        			}else if(id == 'emailParam-tab-a'){
        				showHideButtonBasedOnTabsSelected('EMAIL_PARAM');
        			}else if(id == 'ldapParam-tab-a'){
        				showHideButtonBasedOnTabsSelected('LOGIN_PARAM');
        			}else if(id == 'ssoParam-tab-a'){
        				showHideButtonBasedOnTabsSelected('SSO_PARAM');
        			}
        		});        		       	
        	</script>
     		<jsp:include page="../common/newfooter.jsp"></jsp:include>
     	</div>
	</footer>
    <!-- Footer End -->	 
</div>
</body>
</html>
