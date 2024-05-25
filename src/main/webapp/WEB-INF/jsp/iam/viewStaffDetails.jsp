<%@page import="com.elitecore.sm.util.MapCache"%>
<%@page import="com.elitecore.sm.common.constants.SystemParametersConstant"%>
<%@page import="com.elitecore.sm.common.constants.BaseConstants"%>
<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="/WEB-INF/ShowErrorTag.tld" prefix="elitecoreError" %>

<!DOCTYPE html>

<html>

<script>
var accessgrouplist = new Array();
</script>

<style>
		.required
		{
			position:relative;
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
        
    <!-- sidebar End -->

    <!-- Content Wrapper. Contains page content -->
    <div class="content-wrapper">
        <!-- Content Header (Page header) -->
        <section class="content-header">
        	<sec:authorize access="hasAnyAuthority('ADD_STAFF') or hasAnyAuthority('EDIT_STAFF')">
	        <div class="fullwidth">
        		<div style="padding:0 10px 0 4px;">
        			<div id="content-scroll-d" class="content-scroll">
         
         				<!-- Content Wrapper. Contains page content Start -->
         			       <div class="fullwidth">
	          				<h4 style="margin-top:10px; margin-bottom:0px; font-size:11px;">
	                            <a href="home.html"><img src="img/tile-icon.png" class="vmiddle" alt="Title Icon"/></a> 
	                            <span class="spanBreadCrumb" style="line-height: 30px;font-weight: bold;">
	                            	<a href="<%= ControllerConstants.STAFF_MANAGER %>">
				                    	<spring:message code="staffManager.page.heading" ></spring:message>
				                    </a>&nbsp;/&nbsp;	
			                    	<a href="#">
				              			<spring:message code="updateStaff.page.heading" ></spring:message>
				               		</a>
	                            </span>
	                        </h4>
        					
        					<jsp:include page="../common/responseMsg.jsp" ></jsp:include>
        					<sec:authorize access="hasAnyAuthority('SEARCH_STAFF','EDIT_STAFF')">
          					<div class="row">
            					<div class="col-xs-12">
              						<div class="box  martop" style="border:none; box-shadow: none;">
                						<div class="box-body table-responsive no-padding">
                							
                							<div class="nav-tabs-custom tab-content">
								                <ul class="nav nav-tabs pull-right">
								                  <li class="<c:if test="${(REQUEST_ACTION_TYPE eq 'UPDATE_STAFF_BASIC_DETAIL')}"><c:out value="active"></c:out></c:if>" onclick="showButtons('update-staff-details-a');">
								                  	<a data-toggle="tab" id="update-staff-details-a" href="#update-staff-details">
								                  		<spring:message code="update.staff.details.tab.heading" ></spring:message>
								                  	</a>
								                  </li>
								                  <li class="<c:if test="${(REQUEST_ACTION_TYPE eq 'UPDATE_STAFF_ACCESS_GROUP')}"><c:out value="active"></c:out></c:if>" onclick="showButtons('update-staff-access-group-a');">
								                  	<a data-toggle="tab" id="update-staff-access-group-a" onclick="clearResponseMsgDiv();" href="#update-staff-access-group">
								                  		<spring:message code="update.staff.access.group.tab.heading" ></spring:message>
								                  	</a>
								                  </li>
								                   <li class="<c:if test="${(REQUEST_ACTION_TYPE eq 'UPDATE_STAFF_PROFILE_PIC') or (REQUEST_ACTION_TYPE eq 'EDIT_STAFF_PROFILE_PIC')}"><c:out value="active"></c:out></c:if>" onclick="showButtons('change-profile-pic-a');">
								                  	<a data-toggle="tab" id="change-profile-pic-a" onclick="clearResponseMsgDiv();" href="#change-profile-pic">
								                  		<spring:message code="addStaff.change.profile.pic.tab.heading" ></spring:message>
								                  	</a>
								                  </li>
								                  <c:if test="${(staff_form_bean.stafftype ne 'LDAP') or (staff_form_bean.stafftype ne 'SSO')}">
													<li class="<c:if test="${(REQUEST_ACTION_TYPE eq 'UPDATE_STAFF_CHANGE_PASSWORD')}"><c:out value="active"></c:out></c:if>" onclick="showButtons('update-staff-change-password-a');">
								                  	<a data-toggle="tab" id="update-staff-change-password-a" onclick="clearResponseMsgDiv();" href="#update-staff-change-password">
								                  		<spring:message code="update.staff.change.password.tab.heading" ></spring:message>
								                  	</a>
								                  	</li>								                  
								                  </c:if>
								                </ul>
                								<jsp:include page="updateStaffDetails-basicDetails.jsp" ></jsp:include>
							                    <jsp:include page="updateStaffDetails-accessGroup.jsp" ></jsp:include>
							                    <jsp:include page="updateStaffDetails-chageProfilepic.jsp" ></jsp:include>
							                    <jsp:include page="updateStaffDetails-changePassword.jsp" ></jsp:include>
		              						</div>
		                				</div>
									</div>
              					</div><!-- /.box -->
                        	</div>
          					</sec:authorize>
          					</div>
           				<!-- Content Wrapper. Contains page content End -->
          
          			</div>
          		</div>
          </div>
          </sec:authorize>
       </section>
     
     </div><!-- /.content-wrapper -->
     
    <!-- Footer Start -->
	<footer class="main-footer positfix">
    	<div class="fooinn">
        	<div class="fullwidth">
        		<div class="button-set">
	            	<div class="padleft-right" id="staffDetailDiv" style="display: none;">
	            		<c:if test="${Boolean.parseBoolean(MapCache.getConfigValueAsObject(SystemParametersConstant.SSO_ENABLE) eq 'false' )}">
		            		<button id="updateStaffBtn" class="btn btn-grey btn-xs " tabindex="21"  onclick="validateFields();">
								<spring:message code="btn.label.update" ></spring:message>
							</button>
							<button id="resetStaffBtn" class="btn btn-grey btn-xs " tabindex="22" onclick="resetBasicDetailsForm();">
								<spring:message code="btn.label.reset" ></spring:message>
	
							</button>
							<a class="btn btn-grey btn-xs " style="text-decoration:none;" tabindex="22" href="<%= ControllerConstants.STAFF_MANAGER %>">
								<spring:message code="btn.label.cancel" ></spring:message>
							</a>
						</c:if>
					</div>	
		           	
            		<div class="padleft-right" id="accessGroupDiv" style="display: none;">
	            		<c:if test="${Boolean.parseBoolean(MapCache.getConfigValueAsObject(SystemParametersConstant.SSO_ENABLE) eq 'false' )}">	
		            		<button id="updateAccessGroupBtn" class="btn btn-grey btn-xs " tabindex="3" onclick="validateFieldsForAccessGroup();">
								<spring:message code="btn.label.update" ></spring:message>
							</button>
							<button id= "resetAccessGroupBtn" class="btn btn-grey btn-xs " tabindex="4" onclick="deselectAllAccessGroup();">
								<spring:message code="btn.label.reset" ></spring:message>
	
							</button>
							<a  class="btn btn-grey btn-xs " style="text-decoration:none;" tabindex="5" href="<%= ControllerConstants.STAFF_MANAGER %>">
								<spring:message code="btn.label.cancel" ></spring:message>
							</a>
						</c:if>
            		</div>
            		
            		<div class="padleft-right" id="changePassDiv" style="display: none;">
	            		<c:if test="${Boolean.parseBoolean(MapCache.getConfigValueAsObject(SystemParametersConstant.SSO_ENABLE) eq 'false' )}">
            				<button id="updateStaffPasswordBtn" class="btn btn-grey btn-xs " tabindex="4" onclick="validateFieldsForChangePassword();">
								<spring:message code="btn.label.update" ></spring:message>
							</button>
							<button id= "resetStaffPasswordBtn" class="btn btn-grey btn-xs " tabindex="5" onclick="resetChangePasswordForm();">
								<spring:message code="btn.label.reset" ></spring:message>
							</button>
							<a class="btn btn-grey btn-xs " style="text-decoration:none;" tabindex="22" href="<%= ControllerConstants.STAFF_MANAGER %>">
								<spring:message code="btn.label.cancel" ></spring:message>
							</a>
						</c:if>
						
            		</div>
            		
            			<div class="padleft-right" id="staffChangeProfilePic" style="display: none;">
						<c:if test="${Boolean.parseBoolean(MapCache.getConfigValueAsObject(SystemParametersConstant.SSO_ENABLE) eq 'false' )}">
	            		
	            			<button class="btn btn-grey btn-xs " tabindex="21" id="editbutton" onclick="displayBrowseOption();">
								<spring:message code="btn.label.edit" ></spring:message>
							</button>
							<button class="btn btn-grey btn-xs " tabindex="23" id="uploadButton" onclick="uploadStaffProfilePic();">
								<spring:message code="btn.label.upload" ></spring:message>
							</button>
							<a class="btn btn-grey btn-xs " style="text-decoration:none;" tabindex="23" id="skipChangeProfilePic" href="<%= ControllerConstants.STAFF_MANAGER %>?REQUEST_ACTION_TYPE=<%= BaseConstants.ADD_STAFF %>">
								<spring:message code="btn.label.skip" ></spring:message>
							</a>
						</c:if>
					</div>
				</div>
        	</div>
        	
       		<script type="text/javascript">
       		function systemParametersTabClicked(tabType){
       			clearResponseMsg();
       			showHideButtonBasedOnTabsSelected(tabType);
       		}
       		function showHideButtonBasedOnTabsSelected(tabType){
       			$("#staffDetailDiv").hide();
   				$("#accessGroupDiv").hide();
   				$("#changePassDiv").hide();
   				$("#staffChangeProfilePic").hide();
   				
       			if(tabType == 'BASIC_DETAIL'){
       				$("#staffDetailDiv").show();
       			}else if(tabType == 'ACCESS_GROUP'){
       				$("#accessGroupDiv").show();
       			}else if(tabType == 'CHANGE_PASSWORD'){
       				$("#changePassDiv").show();
       			}else if(tabType == 'CHANGE_PROFILE_PIC'){
       				$("#staffChangeProfilePic").show();
       			}
       		}
       		
       		function showButtons(id)
       		{
       			if(id == 'update-staff-details-a'){
       				showHideButtonBasedOnTabsSelected('BASIC_DETAIL');
       			}else if(id == 'update-staff-access-group-a'){
       				showHideButtonBasedOnTabsSelected('ACCESS_GROUP');
       			}else if(id == 'update-staff-change-password-a'){
       				showHideButtonBasedOnTabsSelected('CHANGE_PASSWORD');
       			}else if(id == 'change-profile-pic-a'){
       				showHideButtonBasedOnTabsSelected('CHANGE_PROFILE_PIC');
       			}
       		}
       		
       		$(document).ready(function() {
       			var activeTab = $(".nav-tabs li.active a");
    			var id = activeTab.attr("id");
    			showButtons(id);
     		});
       		
       	</script>
     		<jsp:include page="../common/newfooter.jsp"></jsp:include>
     	</div>
	</footer>
    <!-- Footer End -->	 

    </div><!-- ./wrapper -->

	<script type="text/javascript">
       
       $(window).load(function(){
    		$('#dateRangePicker').datepicker({
    			minView : 'month',
    			maxView : 'month',
    	        onClose: function(dateText, inst) { 
    	            $(this).focus();              //doesn't work
    			}
    		});
       });
    		
    </script>

	

  </body>
</html>
