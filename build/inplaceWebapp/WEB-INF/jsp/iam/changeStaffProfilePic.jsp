<%@page import="com.elitecore.sm.common.constants.BaseConstants"%>
<%@page import="com.elitecore.sm.util.EliteUtils"%>
<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page import="com.elitecore.sm.util.MapCache"%>
<%@page import="com.elitecore.sm.common.constants.SystemParametersConstant"%>
<%@taglib uri="/WEB-INF/ShowErrorTag.tld" prefix="elitecoreError" %>
<!DOCTYPE html>

<html>

<style>
	.error{
		color: red;
	}
</style>

  	<!-- topnavigation Start -->

	<jsp:include page="../common/newheader.jsp"></jsp:include>
 
 <body class="skin-blue sidebar-mini">
    <div class="wrapper">

   	<!-- Header Start -->
    
   	<jsp:include page="../common/newtopNavigationPanel.jsp"></jsp:include>
    	 
   	<!-- Header End -->
   	
   	 <jsp:include page="../common/newleftMenu.jsp"></jsp:include>
    	 
    <!-- Content Wrapper. Contains page content -->
    <div class="content-wrapper">
        <!-- Content Header (Page header) -->
        <section class="content-header">
        	<div class="fullwidth">
        		<div style="padding:0 10px 0 4px;">
        			<div id="content-scroll-d" class="content-scroll">
          
          				<!-- Content Wrapper. Contains page content Start -->
         				<div class="fullwidth">
        
        	 				<h4 style="margin-top:10px; margin-bottom:0px; font-size:11px;">
                            	<img src="img/tile-icon.png" class="vmiddle" alt="Title Icon"/> 
                            	<span class="spanBreadCrumb" style="line-height: 30px;font-weight: bold;">
                            		<a href="<%= ControllerConstants.STAFF_MANAGER %>">
				                    	<spring:message code="staffManager.page.heading" ></spring:message>
				                    </a>&nbsp;/&nbsp;	
	                    			 <a href="<%= ControllerConstants.STAFF_MANAGER %>?REQUEST_ACTION_TYPE=<%= BaseConstants.ADD_STAFF %>">
				                    	<spring:message code="staffManager.staff.mgmt.tab" ></spring:message>
				                    </a>&nbsp;&nbsp;
                            	</span>
	                        </h4>
	                        
          					<div class="row">
            					<div class="col-xs-12">
              						<div class="box  martop" style="border:none; box-shadow: none;">
                						<!-- /.box-header -->
                						<div class="box-body table-responsive no-padding">
                							<div class="nav-tabs-custom">
                								<sec:authorize access="hasAnyAuthority('ADD_STAFF') or hasAnyAuthority('EDIT_STAFF')">
                									<jsp:include page="../common/responseMsg.jsp" ></jsp:include>
                								<!-- Tabs within a box -->
								               <ul class="nav nav-tabs pull-right">
								                  <li class="disabled"><a href="#"><spring:message code="addStaff.add.staff.page.heading" ></spring:message></a></li>
								                  <li class="disabled" ><a href="#" data-toggle="tab"><spring:message code="addStaff.assign.access.group" ></spring:message></a></li>
								                  <li class="active" id="change-profile-pic-a"><a href="#change-profile-pic"><spring:message code="addStaff.change.profile.pic.tab.heading" ></spring:message></a></li>
								                </ul>
                
               									<div class="col-md-6 no-left-padding">
       			  									<div class="tab-content no-padding clearfix">
                  										<div role="tabpanel" class="tab-pane active" id="create-staff">
                  											
                  										</div>
                  										<div class="chart tab-pane active" id="change-profile-pic">
                  										<div class="fullwidth inline-form">
                  										<form:form modelAttribute="staff_form_bean" method="post" action="<%=ControllerConstants.CHANGE_STAFF_PROFILE_PIC %>" id="staff-profilePic-form" encType='multipart/form-data'>
								
															<form:input type="hidden" path="id" id="id" ></form:input>
															<input type="hidden" name="<%= BaseConstants.REQUEST_ACTION_TYPE %>"  id="<%= BaseConstants.REQUEST_ACTION_TYPE %>" value="<%= BaseConstants.CHANGE_STAFF_PROFILE_PIC %>" />
																
									
                  													<div class="col-md-12 mtop20">
												                	<div id="profilePicPreview" style="height:100px;width:100px;margin-bottom:5px;border:1px solid #ccc">
												                		<img alt="Staff" src="${STAFF_PROFILE_PIC}" width="100px" height="100px">
												                		<div id="imageStatus" class="error" style="color: red;"></div>
												                	</div> 
												                	
												                	<div class="clearfix"></div>
												                	<div class="col-md-5 col-sm-12 col-xs-7" style="padding-left:0;">
												                	<div class="form-group mtop10" >
												                		<input type="file" class="filestyle form-control" tabindex="14" data-buttonText="<spring:message code="serviceMgmt.browse.file"></spring:message>" id="profilePicFile" name="profilePicFile" accept='image/*' title="${tooltip}">
												                		<spring:bind path="profilePic">
									   											<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
									   									</spring:bind>
												                	</div>
												                	</div>
												                	<script type="text/javascript">
																		$("#profilePicFile").on("change", function(){
																			var files = !!this.files ? this.files : [];
																		    if (!files.length || !window.FileReader) return; // no file selected, or no FileReader support
																			
																		    console.log("1");
																			if (/^image/.test( files[0].type)){ // only image file
																				// Reference: http://stackoverflow.com/questions/19816705/javascript-jquery-size-and-dimension-of-uploaded-image-file
																				var _URL = window.URL || window.webkitURL;
																				var file = files[0];
																				var sizeKB = file.size / 1024 ;
																				console.log("2");	
																				if(sizeKB > <%= MapCache.getConfigValueAsInteger(SystemParametersConstant.PROFILE_IMAGE_SIZE, 10) %>){
																					$("#sizeLimit").val('N');
																					var img = new Image();
																					img.onload = function(){
																						$('#profilePicPreview').html('');
																						$('#profilePicPreview').append(img);
																						$('#profilePicPreview img').css("height", "100px");
																					}
																					
																					$("#imageStatus").html("&nbsp;<spring:message code='staff.image.size.exceed'></spring:message>");
																				}else{
																					console.log("3");
																					$("#sizeLimit").val('Y');
																					var img = new Image();
																					img.onload = function(){
																						$('#profilePicPreview').html('');
																						$('#profilePicPreview').append(img);
																						$('#profilePicPreview img').css("height", "100px");
																					}
																					console.log("4");
																					
																					console.log("5");
																					console.log("Loaded img is :: " + img);
																					console.log("Loaded File is :: " + file);
																					img.src = _URL.createObjectURL(file);
																				}
																			}
																		});
																	</script>
													             
												                </div>  
                  											
                  											</form:form>
                  											</div>
                  										</div>
                									</div>
               									</div>
              									</sec:authorize>
              								</div>
                							
                						</div>
                						<!-- /.box-body -->
              						</div><!-- /.box -->
                					
                        		</div>
            				</div>
            
          				</div>
         				<!-- Content Wrapper. Contains page content End --> 
        			</div>
          		</div>
          </div>
       </section>
    </div><!-- /.content-wrapper -->

    <!-- Footer Start -->
	<footer class="main-footer positfix">
    	<div class="fooinn">
        	<div class="fullwidth">
        		<div class="button-set">
	            	<div class="padleft-right" id="staffChangeProfilePic">
	            		<button class="btn btn-grey btn-xs " tabindex="21" id="editbutton" onclick="displayBrowseOption();">
							<spring:message code="btn.label.edit" ></spring:message>
						</button>
						<button class="btn btn-grey btn-xs " tabindex="23" id="uploadButton" onclick="uploadStaffProfilePic();">
							<spring:message code="btn.label.upload" ></spring:message>
						</button>
						<a class="btn btn-grey btn-xs " style="text-decoration:none;" tabindex="23" id = "skipProfilePicBtn" href="<%= ControllerConstants.STAFF_MANAGER %>?REQUEST_ACTION_TYPE=<%= BaseConstants.ADD_STAFF %>">
							<spring:message code="btn.label.skip" ></spring:message>
						</a>
					</div>	
				</div>
        	</div>
        	
     		<jsp:include page="../common/newfooter.jsp"></jsp:include>
     	</div>
	</footer>
    <!-- Footer End -->			

    </div><!-- ./wrapper -->
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
				console.log("function..!!!");
			
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
				$("#staff-form").attr("action",'<%= ControllerConstants.GO_BACK_TO_ADD_STAFF_FROM_ASSIGN_ACCESS_GROUP %>');
				$("#staff-form").submit();
			}
			
			
		</script>
		
</body>
</html>
