<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<%@page import="com.elitecore.sm.common.constants.BaseConstants"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>

<html>
 
<!-- topnavigation Start -->

<jsp:include page="../common/newheader.jsp"></jsp:include>

<script type="text/javascript">
	var currentTab = '${REQUEST_ACTION_TYPE}';
</script>
 
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
                    	 <!-- Content Wrapper. Contains page content Start -->
         					<div class="fullwidth">
                            	<h4 style="margin-top:10px; margin-bottom:0px; font-size:11px;">
                                    <a href="home.html"><img src="img/tile-icon.png" class="vmiddle" alt="Title Icon"/></a> 
                                    <span class="spanBreadCrumb" style="line-height: 30px;font-weight: bold;">
                                   		<strong>&nbsp;
                                   			<spring:message code="staffManager.page.heading" ></spring:message>
                                   		</strong>&nbsp;
                                    </span>
                                </h4>
         						<jsp:include page="../common/responseMsg.jsp" ></jsp:include>
         						 <div class="row">
         						 	
            						<div class="col-xs-12">
              							<div class="box  martop" style="border:none; box-shadow: none;">
							                <!-- /.box-header -->
							                <div class="box-body table-responsive no-padding">
                
								                <div class="nav-tabs-custom tab-content">
								                <!-- Tabs within a box -->
									                <sec:authorize access="hasAnyAuthority('STAFF_MANAGER_MENU_VIEW')">
										                <ul class="nav nav-tabs pull-right">
											                <sec:authorize access="hasAnyAuthority('VIEW_STAFF')">
										                   		<li class="<c:if test="${(REQUEST_ACTION_TYPE eq 'ADD_STAFF') or (REQUEST_ACTION_TYPE eq 'STAFF_MANAGEMENT')}"><c:out value="active"></c:out></c:if>" id="staff">
										                   			<a href="#staff-mgmt-tab" data-toggle="tab" id="staff-mgmt-tab-a" onclick="changeTab('<%=ControllerConstants.STAFF_MANAGER%>','GET','STAFF_MANAGEMENT');setTimeout(function() { resizeStaffGrid(); }, 5);">
										                   				<spring:message code="staffManager.staff.mgmt.tab" ></spring:message>
										                   			</a>
										                   		</li>
											                </sec:authorize>
											                <sec:authorize access="hasAnyAuthority('VIEW_ACCESS_GROUP')">
									                       		<li class="<c:if test="${(REQUEST_ACTION_TYPE eq 'ACCESS_GROUP_MANAGEMENT') or (REQUEST_ACTION_TYPE eq 'ADD_ACCESS_GROUP') or (REQUEST_ACTION_TYPE eq 'UPDATE_ACCESS_GROUP') or (REQUEST_ACTION_TYPE eq 'VIEW_ACCESS_GROUP')}"><c:out value="active"></c:out></c:if>" id="access_grp" >
									                       			<a href="#access-group-mgmt-tab" data-toggle="tab" id="access-group-mgmt-tab-a" onclick="changeTab('<%=ControllerConstants.STAFF_MANAGER%>','GET','ACCESS_GROUP_MANAGEMENT');setTimeout(function() { resizeGrid(); }, 5)">
									                       				<spring:message code="staffManager.access.group.mgmt.tab" ></spring:message>
									                       			</a>
									                       		</li>
											                </sec:authorize>
											                <sec:authorize access="hasAnyAuthority('VIEW_AUDIT')">
									                       		<li class="<c:if test="${(REQUEST_ACTION_TYPE eq 'STAFF_AUDIT_MANAGEMENT')}"><c:out value="active"></c:out></c:if>" id="staff_audit" >
									                       			<a href="#staff-audit-mgmt-tab" data-toggle="tab" id="staff-audit-mgmt-tab-a" onclick="changeTab('<%=ControllerConstants.STAFF_MANAGER%>','GET','STAFF_AUDIT_MANAGEMENT');">
									                       				<spring:message code="staffManager.audit.tab.label" ></spring:message> 
									                       			</a>
									                       		</li>
											                </sec:authorize>
										                </ul>
									                </sec:authorize>
                  									
                  									<sec:authorize access="hasAnyAuthority('VIEW_STAFF')">
											          	<div id="staff-mgmt-tab" class="fullwidth mtop10 tab-pane  <c:if test="${(REQUEST_ACTION_TYPE eq 'ADD_STAFF') or (REQUEST_ACTION_TYPE eq 'STAFF_MANAGEMENT')}"><c:out value="active"></c:out></c:if>">
										                      <jsp:include page="staffMgmt.jsp" ></jsp:include>
											        	</div>
											        </sec:authorize>
											        <sec:authorize access="hasAnyAuthority('VIEW_ACCESS_GROUP')">
											        	<div id="access-group-mgmt-tab" class="fullwidth mtop10 tab-pane  <c:if test="${(REQUEST_ACTION_TYPE eq 'ACCESS_GROUP_MANAGEMENT') or (REQUEST_ACTION_TYPE eq 'ADD_ACCESS_GROUP') or (REQUEST_ACTION_TYPE eq 'UPDATE_ACCESS_GROUP') or (REQUEST_ACTION_TYPE eq 'SEARCH_ACCESS_GROUP')}"><c:out value="active"></c:out></c:if>">
									                           <jsp:include page="accessGroupMgmt.jsp" ></jsp:include>
									                    </div>
							                       </sec:authorize>
													<sec:authorize access="hasAnyAuthority('VIEW_AUDIT')">
											        	<div id="staff-audit-mgmt-tab" class="fullwidth mtop10 tab-pane  <c:if test="${(REQUEST_ACTION_TYPE eq 'STAFF_AUDIT_MANAGEMENT')}"><c:out value="active"></c:out></c:if>">
									                           <jsp:include page="auditMgmt.jsp" ></jsp:include>
									                    </div>
							                        </sec:authorize>
									                
             									</div>
                  
                							</div>
                
                							<!-- /.box-body -->
              							</div>
              							<!-- /.box -->
            						</div>
          						</div>
          						<div id="cofirmation" style=" width:100%; display: none;" >
                                     <div class="modal-content">
                                         <div class="modal-header padding10">
                                             <h4 class="modal-title"><spring:message code="label.confirmation.header" ></spring:message></h4>
                                         </div>
                                         <div class="modal-body padding10 inline-form">
                                             <p> <spring:message code="staffManager.delete.warn.msg" ></spring:message> </p>
                                         </div>
                                         <div class="modal-footer padding10">
                                             <button type="button" class="btn btn-grey btn-xs "><spring:message code="btn.label.yes" ></spring:message></button>
                                             <button onclick="javascript:$.fancybox.close();" class="btn btn-grey btn-xs "><spring:message code="btn.label.no" ></spring:message></button>
                                         </div>
                                     </div>
                                     <!-- /.modal-content --> 
                                     
                                 </div>
          					</div>
         				 <!-- Content Wrapper. Contains page content Start -->
          			</div>
          		</div>
          </div>
        </section>
     </div>
		<form id="staff-manager" action="" method="GET">
			<input type="hidden" id="REQUEST_ACTION_TYPE" name="REQUEST_ACTION_TYPE" value=""/> 
		</form>
	<!-- Footer Start -->
	<footer class="main-footer positfix">      
			<div class="fooinn">
        		<div class="fullwidth">
        
					<jsp:include page="../common/newfooter.jsp"></jsp:include>
				</div>
			</div>
		</footer>
    <!-- Footer End -->	 

    </div>
    
    <!-- ./wrapper -->
    
		
		<script type="text/javascript">
			function changeTab(formAction, formMethod,requestAction){
				$("#staff-manager").attr("action",formAction);
				$("#staff-manager").attr("method",'GET');
				$("#REQUEST_ACTION_TYPE").val(requestAction);
				$('#staff-manager').submit();	
			}
		</script>
	
		
		
        
	<!-- <script>
		
		$(document).ready(function() {
			$('.fancybox').fancybox({
				maxWidth	: 1100,
				maxHeight	: 800,
				fitToView	: false,
				width		: '90%',
				height		: 'auto',
				autoSize	: false,
				closeClick	: false,
				openEffect	: 'none',
				closeEffect	: 'none'
			});
		});
		

	</script> -->

  </body>
</html>
