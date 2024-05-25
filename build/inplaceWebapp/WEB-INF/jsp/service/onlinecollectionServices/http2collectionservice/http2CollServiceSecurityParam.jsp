<%@page import="com.elitecore.sm.common.model.TrueFalseEnum"%>
<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<%@page import="com.elitecore.sm.common.model.SecureSchemeTypeEnum"%>
<%@ taglib uri="http://www.springframework.org/security/tags"	prefix="sec"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="/WEB-INF/ShowErrorTag.tld" prefix="elitecoreError"%>
<div class="box box-warning" id="http2_coll_services_security_param_div">
				<div class="box-header with-border">
					<h3 class="box-title">
						<spring:message
							code="service.security.param.section.heading" ></spring:message>
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
					
						<div class="col-md-6 no-padding">
							<spring:message code="http2.collSer.config.security.param.secure.encryption" var="label" ></spring:message>
							<spring:message code="http2.collSer.config.security.param.secure.encryption.tooltip" var="tooltip" ></spring:message>
							<div class="form-group">
								<div class="table-cell-label">${label}</div>
								<div class="input-group ">
									<form:select path="encryption" cssClass="form-control table-cell input-sm" tabindex="4" id="service-encryption" data-toggle="tooltip"  onchange="changeEncryption(this.value);"  data-placement="bottom"  title="${tooltip }" value="${service_config_form_bean.encryption}">
										<%
										 for(TrueFalseEnum enumlist : TrueFalseEnum.values()) { 
											 %>
											 	<form:option value="<%=  enumlist.getName() %>"><%= enumlist.toString() %></form:option>
											 <%
										     } 
										%>
									</form:select>
									<spring:bind path="encryption">
										<elitecoreError:showError errorMessage="${status.errorMessage}" errorId="service-encryption_error"></elitecoreError:showError>
									</spring:bind>
								</div>
							</div>
						</div>
						
						<div class="col-md-6 no-padding">
							<spring:message code="http2.collSer.config.security.param.secure.scheme" var="label" ></spring:message>
							<spring:message code="http2.collSer.config.security.param.secure.scheme.tooltip" var="tooltip" ></spring:message>
							<div class="form-group">
								<div class="table-cell-label">${label}</div>
								<div class="input-group ">
									<form:select path="secureScheme" cssClass="form-control table-cell input-sm" tabindex="4" id="service-secureScheme"
									 data-toggle="tooltip" data-placement="bottom"  title="${tooltip }" value="${service_config_form_bean.secureScheme}">
									<%
									 for(SecureSchemeTypeEnum enumlist : SecureSchemeTypeEnum.values()) { 
										 %>
										 	<form:option value="<%=  enumlist.toString() %>"><%= enumlist.toString() %></form:option>
										 <%
									     }
									%>
									</form:select>
									<spring:bind path="secureScheme">
										<elitecoreError:showError errorMessage="${status.errorMessage}" errorId="service-secureScheme_error"></elitecoreError:showError>
									</spring:bind>
								</div>
							</div>
						</div>
						
						<div class="col-md-6 no-padding">
							<spring:message code="http2.collSer.config.security.param.secure.keystore.password" var="label" ></spring:message>
							<spring:message code="http2.collSer.config.security.param.secure.keystore.password.tooltip" var="tooltip" ></spring:message>
							<div class="form-group">
								<div class="table-cell-label">${label}</div>
								<div class="input-group ">
									<form:input type="password" path="keystorePassword" value="${keystorePassword}" cssClass="form-control table-cell input-sm" tabindex="4" readonly="false" id="service-keystorePassword" data-toggle="tooltip" data-placement="bottom" title="${tooltip }"></form:input>
									<spring:bind path="keystorePassword">
										<elitecoreError:showError errorMessage="${status.errorMessage}" errorId="service-keystorePassword_error"></elitecoreError:showError>
									</spring:bind>
								</div>
							</div>
						</div>
						
						<div class="col-md-6 no-padding">
							<spring:message code="http2.collSer.config.security.param.secure.keymanager.password" var="label" ></spring:message>
							<spring:message code="http2.collSer.config.security.param.secure.keymanager.password.tooltip" var="tooltip" ></spring:message>
							<div class="form-group">
								<div class="table-cell-label">${label}</div>
								<div class="input-group ">
									<form:input type="password" path="keymanagerPassword" value="${keymanagerPassword}" cssClass="form-control table-cell input-sm" tabindex="4" readonly="false" id="service-keymanagerPassword" data-toggle="tooltip" data-placement="bottom" title="${tooltip }"></form:input>
									<spring:bind path="keymanagerPassword">
										<elitecoreError:showError errorMessage="${status.errorMessage}" errorId="service-keymanagerPassword_error"></elitecoreError:showError>
									</spring:bind>
								</div>
							</div>
						</div>
						
						<div class="col-md-6 no-padding">
							<spring:message code="http2.collSer.config.security.param.secure.keystore.filename" var="label" ></spring:message>
							<spring:message code="http2.collSer.config.security.param.secure.keystore.filename.tooltip" var="tooltip" ></spring:message>
							<div class="form-group">
								<div class="table-cell-label">${label}</div>
								<div class="input-group ">
									<form:input path="keystoreFileName" value="${keystoreFileName}" cssClass="form-control table-cell input-sm" tabindex="4" readonly="true" id="service-keystoreFileName" data-toggle="tooltip" data-placement="bottom" title="${tooltip }"></form:input>
									<spring:bind path="keystoreFileName">
										<elitecoreError:showError errorMessage="${status.errorMessage}" errorId="service-keystoreFileName_error"></elitecoreError:showError>
									</spring:bind>
								</div>
							</div>
						</div>
						
						<div class="col-md-6 no-padding" style="height: 45px" id="service-keystoreFileDiv">
							<div class="col-md-9 no-padding" style="height: 45px">
								<spring:message code="http2.collSer.config.security.param.secure.keyresouce.path" var="tooltip" ></spring:message>
								<div class="form-group">
								   <div class="table-cell-label">${tooltip}</div>
			                          <div class="input-group">
			                          	<div class="fullwidth" id="control">
				                            <input type="file" class="filestyle form-control"
						             		 id="keystorefile" name="keystorefile">
					             		 </div>
			                          </div>
			                     </div> 	
						    </div>
							<div class="col-md-3 no-padding" style="height: 45px">
								<div class="form-group">
			                          <div class="input-group">
			                          	<div class="fullwidth" id="buttonsDiv" >
						             		 <button class="btn btn-grey btn-xs" style="width : 150%" type="button" id ="upload_keystore_file"  onclick="uploadKeystoreFile();"><spring:message code="http2.collSer.config.security.param.keystore.file.upload"></spring:message></button>
					             		 </div>
					             		 <div id="processbar" class="modal-footer padding10 text-left" style="display: none;">
												<img src="img/processing-bar.gif">
										</div>
			                          </div>
			                     </div> 	
					    	</div>
					    </div>	
						
						
						</div>
				</div>
				<!-- Form content end here  -->
			</div>
			<!-- section-2 end here -->
<script type="text/javascript">		
var jsSpringMsg = {};
jsSpringMsg.fileNotSelectMsg= "<spring:message code='file.not.selected' ></spring:message>";
var files;
$(document).ready(function() {
	changeEncryption('${service_config_form_bean.encryption}');
});
function changeEncryption(value){
	if(value=='false'){
		$('#service-secureScheme').attr('readonly',true);
		$('#service-keystorePassword').attr('readonly',true);
		$('#service-keymanagerPassword').attr('readonly',true);
		$('#service-keystoreFileDiv').attr('readonly',true);
	} else {
		$('#service-secureScheme').attr('readonly',false);
		$('#service-keystorePassword').attr('readonly',false);
		$('#service-keymanagerPassword').attr('readonly',false);
		$('#service-keystoreFileDiv').attr('readonly',false);
	}
}

function resetHttp2SecurityParam() {
	$("#service-encryption").val('false');
	$("#service-secureScheme").val('HTTP');
	$("#service-keystorePassword").val('');
	$("#service-keymanagerPassword").val('');
}

function uploadKeystoreFile(){
	clearAllMessages();
	clearAllMessagesPopUp();
	var file = $("#keystorefile").val();
	var serviceId=$("#serviceId").val();
	
	if(file === ''){
		showErrorMsg(jsSpringMsg.fileNotSelectMsg);
		return false;
	}
	
	var oMyForm = new FormData();
    oMyForm.append("file", files[0]);
    oMyForm.append("serviceId", serviceId);
    
    $('#processbar').show();
    $('#buttonsDiv').hide();
   
	  $.ajax({dataType : 'json',
          url : '<%=ControllerConstants.UPLOAD_KEY_STORE_FILE%>',
          data : oMyForm ,
          type : "POST",
          enctype: 'multipart/form-data',
          processData: false,
          contentType:false,
          success : function(response) {
        	var responsedata = response[0];
  		 	var responseCode = response.code;
  			var responseMsg = response.msg;
  			var responseObj = response.object;
  			if(responseCode === "200"){
  				 $('#processbar').hide();
  	        	 $('#buttonsDiv').show();
  	        	 $('#service-keystoreFileName').val(responseObj);
  			  	 showSuccessMsg(responseMsg);
			}else{
				 $('#processbar').hide();
	        	 $('#buttonsDiv').show();
				 showErrorMsg(responseMsg);
			}
          },
          error : function(){
        	  $('#processbar').hide();
        	  $('#buttonsDiv').show();
          }
      });	
}
function clearFileContent(){
	$(":file").filestyle('clear');
}
$(document).on("change", "#keystorefile", function(event) {
	files = event.target.files;
	$('#keystorefile').html(files[0].name);
});
</script>
