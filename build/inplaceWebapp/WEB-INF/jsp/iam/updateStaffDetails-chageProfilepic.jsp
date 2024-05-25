<%@page import="com.elitecore.sm.iam.model.Staff"%>
<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<%@page import="com.elitecore.sm.common.constants.BaseConstants"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@page import="com.elitecore.sm.util.MapCache"%>
<%@page
	import="com.elitecore.sm.common.constants.SystemParametersConstant"%>
<%@taglib uri="/WEB-INF/ShowErrorTag.tld" prefix="elitecoreError"%>
<div
	class="fullwidth borbot tab-pane <c:if test="${(REQUEST_ACTION_TYPE eq 'UPDATE_STAFF_PROFILE_PIC') or (REQUEST_ACTION_TYPE eq 'EDIT_STAFF_PROFILE_PIC') or (REQUEST_ACTION_TYPE eq 'UPDATE_MY_PROFILE_PIC')}"><c:out value="active"></c:out></c:if>"
	id="change-profile-pic">
	<div class="fullwidth padding0">
		<div class="col-md-6 no-left-padding">

			<div class="fullwidth inline-form">
				<form:form modelAttribute="staff_form_bean" method="post"
					action="<%=ControllerConstants.CHANGE_STAFF_PROFILE_PIC%>"
					id="staff-profilePic-form" encType='multipart/form-data'>

					<form:input type="hidden" path="id" id="id" ></form:input>
					<input type="hidden"
						name="<%=BaseConstants.REQUEST_ACTION_TYPE%>"
						id="<%=BaseConstants.REQUEST_ACTION_TYPE%>"
						value="<%=BaseConstants.UPDATE_STAFF_PROFILE_PIC%>" />


					<div class="col-md-12 mtop20">
						<div id="profilePicPreview"
							style="height: 100px; width: 100px; margin-bottom: 5px; border: 1px solid #ccc">
							<img alt="Staff" src="${STAFF_PROFILE_PIC}" width="100px"
								height="100px">
							<div id="imageStatus" class="error" style="color: red;"></div>
						</div>

						<div class="clearfix"></div>
						<div class="col-md-5 col-sm-12 col-xs-7" style="padding-left: 0;">
							<div class="form-group mtop10">
								<input type="file" class="filestyle form-control" tabindex="14"
									data-buttonText="<spring:message code="serviceMgmt.browse.file"></spring:message>"
									id="profilePicFile" name="profilePicFile" accept='image/*'
									title="${tooltip}">
								<spring:bind path="profilePic">
									<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
								</spring:bind>
							</div>
						</div>
						<script type="text/javascript">
						$("#profilePicFile").on("change", function(){
							
							var files = !!this.files ? this.files : [];
					    	if (!files.length || !window.FileReader) return; // no file selected, or no FileReader support

							if (/^image/.test( files[0].type)){ // only image file
								// Reference: http://stackoverflow.com/questions/19816705/javascript-jquery-size-and-dimension-of-uploaded-image-file
								var _URL = window.URL || window.webkitURL;
								var file = files[0];
								var img = new Image();
								var sizeKB = file.size / 1024;
								img.onload = function(){
									$('#profilePicPreview').html('');
									$('#profilePicPreview').append(img);
									$('#profilePicPreview img').css("height", "100px");
									$('#profilePicPreview img').css("width", "100px");
								}
								img.src = _URL.createObjectURL(file);
							}
						});
						</script>

					</div>

				</form:form>

			</div>
		</div>
	</div>
</div>

<c:choose>
	<c:when test="${REQUEST_ACTION_TYPE eq 'EDIT_STAFF_PROFILE_PIC'}">
		<script type="text/javascript">
				$(document).ready(function() {
					displayBrowseOption();
				});
			</script>
	</c:when>
	<c:otherwise>
		<script type="text/javascript">
				
					$( document ).ready(function() {
						$("#staffChangeProfilePic").show();
					
						$("#profilePicFile").parent("div").hide();
					
						$("#editbutton").show();
						$("#uploadButton").hide();
					});
								
			</script>
	</c:otherwise>
</c:choose>

<script type="text/javascript">
		
			
			function displayBrowseOption(){
				$("#staffChangeProfilePic").show();
				
				$("#profilePicFile").parent("div").show();
				$("#editbutton").hide();
				$("#uploadButton").show();
			}
			
			function uploadStaffProfilePic(){
				
var file = $("#profilePicFile").val();
				
				console.log("File object is : " + file);
				
				if(file == null || file=='' || file == undefined || file== 'undefined'){
				
						showErrorMsg("<spring:message code='image.no.file.select' ></spring:message>");
						
						return;
					
					    		
				}else if(!ValidateImageExtension(file)){
					
			    	showErrorMsg("<spring:message code='failed.file.extension.message'></spring:message>");

			    	return;
				}
				else{
					if(!validateSize()){
						showErrorMsg("<spring:message code='failed.file.size.message'></spring:message>");
					}
					else{
					$("#staff-profilePic-form").submit();
					}
				}
				
			}
			
			function ValidateImageExtension(sFileName) {
				
				var validFileExtensions = [".jpg", ".jpeg", ".bmp", ".gif", ".png"];
				var blnValid = false;
			            
			            if (sFileName.length > 0) {
			                for (var j = 0; j < validFileExtensions.length; j++) {
			                    var sCurExtension = validFileExtensions[j];
			                    if (sFileName.substr(sFileName.length - sCurExtension.length, sCurExtension.length).toLowerCase() == sCurExtension.toLowerCase()) {
			                        blnValid = true;
			                        break;
			                    }
			                }
			            }
			            
			            if (!blnValid) {
			                   
			                    return false;
			                }
			           
			       
			   
			  
			    return blnValid;
			}
			
			function validateSize(){
				var fileUpload = document.getElementById("profilePicFile");
				var maxSize=<%= MapCache.getConfigValueAsInteger(SystemParametersConstant.PROFILE_IMAGE_SIZE, 1024) %>;
		    console.log("max size is::"+maxSize);
				if (typeof (fileUpload.files) != "undefined") {
		            var size = parseFloat(fileUpload.files[0].size / 1024).toFixed(2);
		            console.log(size + " KB.");
		            if(size>maxSize){
		            	
		            	return false;
		            }else{
		            	
		            	return true;
		            }
		            
		        } else {
		        	console.log("This browser does not support HTML5.");
		        	return true;
		        }
			}
			
			function goBackToAddStaff(){
				$("#staff-form").attr("action",'<%=ControllerConstants.GO_BACK_TO_ADD_STAFF_FROM_ASSIGN_ACCESS_GROUP%>');
		$("#staff-form").submit();
	}

	function skipChangeProfilepic() {
		$("#staff-skip-profilePic-form").submit();
	}
</script>
