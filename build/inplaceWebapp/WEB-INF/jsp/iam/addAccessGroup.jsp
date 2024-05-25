<%@page import="com.elitecore.sm.common.model.StateEnum"%>
<%@page import="com.elitecore.sm.common.constants.SystemParametersConstant"%>
<%@page import="com.elitecore.sm.common.constants.BaseConstants"%>
<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="/WEB-INF/ShowErrorTag.tld" prefix="error" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page import="com.elitecore.sm.util.MapCache"%>

<!doctype html>
<!--[if lt IE 7]>      <html class="no-js lt-ie9 lt-ie8 lt-ie7 lt-ie10"> <![endif]-->
<!--[if IE 7]>         <html class="no-js lt-ie9 lt-ie8 lt-ie10"> <![endif]-->
<!--[if IE 8]>         <html class="no-js lt-ie9 lt-ie10"> <![endif]-->
<!--[if IE 9]>         <html class="no-js lt-ie10"> <![endif]-->
<html>

	<jsp:include page="../common/newheader.jsp" ></jsp:include>
    
<body class="skin-blue sidebar-mini">
<div class="wrapper">
	<!-- Header Start -->
     
  	<jsp:include page="../common/newtopNavigationPanel.jsp"></jsp:include>
 	<!-- Header End -->
 	
 	<!-- sidebar Start -->
 	
 	<jsp:include page="../common/newleftMenu.jsp"></jsp:include>
 	<!-- sidebar End -->
 	
 	<!-- Content Wrapper. Contains page content -->
   	<div class="content-wrapper">
    <!-- Content Header (Page header) -->
    <section class="content-header">
    	<div class="fullwidth">
        
       		<div style="padding:0 10px 0 4px;">
        
        		<div id="content-scroll-d" class="content-scroll">
        		<!-- Content Wrapper. Contains page content Start -->
        		<sec:authorize access="hasAuthority('ADD_ACCESS_GROUP') or hasAuthority('SEARCH_ACCESS_GROUP') or hasAuthority('EDIT_ACCESS_GROUP')">
         			<div class="fullwidth">
						<h4 style="margin-top:10px; margin-bottom:0px; font-size:11px;">
			            	<a href="home"><img src="img/tile-icon.png" class="vmiddle" alt="Title Icon"/></a> 
			               	<span class="spanBreadCrumb" style="line-height: 30px;">
			               	<strong>
			               		<a href="javascript:;" onclick="loadBredthcrumLinks('<%=ControllerConstants.STAFF_MANAGER%>','GET','');"><spring:message code="staffManager.page.heading" ></spring:message></a> &nbsp;&nbsp; / &nbsp;&nbsp;
			               		<a href="javascript:;" onclick="loadBredthcrumLinks('<%=ControllerConstants.STAFF_MANAGER%>','GET','ACCESS_GROUP_MANAGEMENT');"><spring:message code="staffManager.access.group.mgmt.tab" ></spring:message></a>
			               	</strong>
			               	</span>		
			           	</h4>
			           	
			           	<jsp:include page="../common/responseMsg.jsp" ></jsp:include>
						<div class="row">
							<div class="col-xs-12">
								<div class="box  martop" style="border:none; box-shadow: none;">
								<!-- /.box-header -->
                					<div class="box-body table-responsive no-padding">
                						<div class="nav-tabs-custom">
                						<!-- Tabs within a box -->
                							<ul class="nav nav-tabs pull-right">
                  								<li class="active"><a href="#add-access-group-tab" data-toggle="tab" id="add-access-group-tab-a" onclick='$("#change-status-tab").hide()'>
                       								<spring:message code="addAccessGroup-add.access.group.page.heading" ></spring:message></a>
                       							</li>
                							</ul>
                							<form:form modelAttribute="access_group_form_bean" method="POST" action="addAccessGroup" id="access-group-form">
                							<div class="fullwidth mtop10">
                							
                							<div class="col-md-6 no-left-padding inline-form">
                							
													<div class="form-group">
														<spring:message code="addAccessGroup-access.group.name" var="tooltip"></spring:message>
            											<div class="table-cell-label">${tooltip }<span class="required">*</span></div>
               											<div class="input-group ">
               												<form:input path="name"  id="access-group-name" cssClass="form-control table-cell input-sm" data-toggle="tooltip" data-placement="bottom" title="${tooltip}" maxlength="60"></form:input>
               												<spring:bind path="name">
								   								<error:showError errorMessage="${status.errorMessage}" errorId="access-group-name_error"></error:showError>
								  	 						</spring:bind>
               											</div>
                									</div>
               										<sec:authorize access="hasAuthority('MODIFY_LDAP_SYSTEM_PARAMETER')">
              										<div class="form-group">
                									<spring:message code="addAccessGroup-access.group.type" var="tooltip"></spring:message>
                										<div class="table-cell-label">${tooltip }</div>
               											<div class="input-group ">
	                										 <form:select path="accessGroupType" cssClass="form-control table-cell input-sm" tabindex="4" id="accessGroupType" data-toggle="tooltip" data-placement="bottom" title="${tooltip }" items="${accessGroupList }">
															</form:select>
														</div>
                									</div>
                 								</sec:authorize>
              									<div class="form-group">
                										<spring:message code="addAccessGroup-access.group.description" var="tooltip"></spring:message>
            											<div class="table-cell-label">${tooltip }</div>
               											<div class="input-group ">
               												<form:textarea path="description" cssClass="form-control input-sm" id="access-group-description" data-toggle="tooltip" rows="5" data-placement="bottom" title="${tooltip}" maxlength="250"></form:textarea>
               												<spring:bind path="description">
								   								<error:showError errorMessage="${status.errorMessage}" errorId="access-group-description_error"></error:showError>
								  	 						</spring:bind>
               											</div>
                									</div>
                									<div id="childActions" style="display: none;"></div>
                									
                									<form:hidden path="id" id="id" ></form:hidden>
													<form:hidden path="accessGroupState" id="accessGroupState" ></form:hidden>
													<form:hidden path="createDate" id="createDate" ></form:hidden>
													<form:hidden path="lastUpdateDate" id="lastUpdateDate" ></form:hidden>
													<form:hidden path="lastUpdateByStaffId" id="lastUpdateByStaffId" ></form:hidden>
													<form:hidden path="createdByStaffId" id="createdByStaffId" ></form:hidden>
											
												</div>
												
												<div class="col-md-6 left-border">
													<jsp:include page="modulesTree.jsp"></jsp:include>
												</div>
												</div>
												</form:form>
											</div>
										</div>
									</div>
								</div>
							</div>
						</div>
					</sec:authorize>
				</div>
			</div>
		</div>
	</section>
</div>
<div style="display: none;">
	<form id="access_group_form" method="POST" action="<%= ControllerConstants.INIT_UPDATE_ACCESS_GROUP %>">
		<input type="hidden" id="accessGroupId" name="accessGroupId">
		<input type="hidden" id="requestAfterChangeStatus" name="requestAfterChangeStatus" value="false">
		<input type="hidden" id="changeStatusMsg" name="changeStatusMsg" value="">
	</form>
</div>
<footer class="main-footer positfix">
												
	<div class="fooinn">
		<div class="fullwidth">
			<div class="padleft-right">
				<c:choose>
				<c:when test="${REQUEST_ACTION_TYPE == 'ADD_ACCESS_GROUP'}">
    				<button class="btn btn-grey btn-xs " type="button" id="save-btn-div" onclick="validateFields();"><spring:message code="addAccessGroup-access.group.save" ></spring:message></button>
    				<button class="btn btn-grey btn-xs" type="button" id="reset-btn-div" onclick="resetForm();"><spring:message code="addAccessGroup-access.group.reset" ></spring:message></button>
    				<script type="text/javascript">
						$( document ).ready(function() {
							$("#add-access-group-tab-a").html("<spring:message code='addAccessGroup-add.access.group.page.heading' ></spring:message>");
							$("#page-heading-a").html("<spring:message code='addAccessGroup-add.access.group.page.heading' ></spring:message>");
						});
					</script>
				</c:when>
				<c:when test="${REQUEST_ACTION_TYPE == 'VIEW_ACCESS_GROUP'}">
				<sec:authorize access="hasAuthority('EDIT_ACCESS_GROUP')">
					<button class="btn btn-grey btn-xs " type="button" id="update-btn-div" onclick="editAccessGroup();"><spring:message code="addAccessGroup-access.group.edit" ></spring:message></button>
				</sec:authorize>	
					<script type="text/javascript">
						$( document ).ready(function() {
							$("#add-access-group-tab-a").html("<spring:message code='addAccessGroup-view.access.group.page.heading' ></spring:message>");
							$("#page-heading-a").html("<spring:message code='addAccessGroup-view.access.group.page.heading' ></spring:message>");
							$("#access-group-name").prop('disabled',true);
							$("#access-group-description").prop('disabled',true);
							$("#accessGroupType").attr("disabled",true);
						});
					</script>
				</c:when>
				<c:when test="${REQUEST_ACTION_TYPE == 'UPDATE_ACCESS_GROUP'}">
				<button class="btn btn-grey btn-xs " type="button" id="update-btn-AG" onclick="validateFields();"><spring:message code="addAccessGroup-access.group.update" ></spring:message></button>
				<button class="btn btn-grey btn-xs " type="button" id="reset-btn-div" onclick="resetForm();"><spring:message code="addAccessGroup-access.group.reset" ></spring:message></button>
				<script type="text/javascript">
					$( document ).ready(function() {
						$("#access-group-form").attr("action","<%= ControllerConstants.UPDATE_ACCESS_GROUP %>");
						$("#add-access-group-tab-a").html("<spring:message code='addAccessGroup-update.access.group.page.heading' ></spring:message>");
						$("#page-heading-a").html("<spring:message code='addAccessGroup-update.access.group.page.heading' ></spring:message>");
						//TODO: Wireframe changed. Change status moved to Search Screen
						//$("#change-status-tab-li").show();
						<c:if test="${IS_REQUEST_AFTER_CHANGE_STATUS == 'true'}">
							$("#change-status-tab-li a").click();
							showSuccessMsg("${CHANGE_STATUS_MSG}");
						</c:if>
					});
				</script>
				<!-- Used in case of Update -->
				<input type="hidden" id="assignedUnassignedToStaff" name="assignedUnassignedToStaff" value="${ASSIGNED_UNASSIGNED_TO_STAFF}" />
				<c:choose>
					<c:when test="${ASSIGNED_UNASSIGNED_TO_STAFF == 'Assigned'}">
					<script type="text/javascript">
						$("#access-group-name").attr("disabled",true);
						$("#accessGroupType").attr("disabled",true);
					</script>
					</c:when>
					<c:when test="${ASSIGNED_UNASSIGNED_TO_STAFF == 'Unassigned'}">
						<script type="text/javascript">
							$("#access-group-name").attr("disabled",false);
						</script>
					</c:when>
				</c:choose>
				</c:when>
				</c:choose>
				<button class="btn btn-grey btn-xs " style="text-decoration:none;" onclick="loadBredthcrumLinks('<%=ControllerConstants.STAFF_MANAGER%>','GET','ACCESS_GROUP_MANAGEMENT');"><spring:message code="addAccessGroup-access.group.cancel" ></spring:message></button>
			

			</div>
		</div>
		<jsp:include page="../common/newfooter.jsp"></jsp:include>
	</div>
</footer>
 <form id="staff-manager-form" action="" method="GET">
	<input type="hidden" id="REQUEST_ACTION_TYPE" name="REQUEST_ACTION_TYPE" value=""/> 
</form>
</div>
</body>
            									
<script>

	$( document ).ready(function() {
		
		loadMenuTabAndActionList();
		
		$("#access-group-name").keypress(function(e) {
	          	// Enter pressed?
	          	if(e.which == 10 || e.which == 13) {
	          		validateFields();
	          	}
	      	});
		
		$("#access-group-description").keypress(function(e) {
	          	// Enter pressed?
	          	if(e.which == 10 || e.which == 13) {
	          		validateFields();
	          	}
	      	});
	});
							
	
	
	function validateFields(){
		var aclSelected = $('#acls-tree').jstree(true).get_selected();
		
		$("#childActions").html('');
		var isACLsActionSelected = false;
		var index = 0;
		$.each( aclSelected, function( key, value ){
			if(value.indexOf("ACTION_")>= 0){
				isACLsActionSelected = true;
				value = value.replace("ACTION_","");
				$("#childActions").append(
									'<input id="actions[' + index + '].id" type="text" value="' + value + '" name="actions[' + index + '].id" />'
								);
				index++
			}
		});
		
		$("#access-group-name").attr("disabled",false);
		$("#accessGroupType").attr("disabled",false);
		
		$("#access-group-form").submit();
	}
	
	function resetForm(){
		if(!$('#access-group-name').is(':disabled')){
			$("#access-group-name").val('');
		}
		$("#access-group-description").val('');
		$("#childActions").html('');
		$('#acls-tree').jstree(true).deselect_all();
		resetWarningDisplay();
	}
	
	function validateFieldsForChangeState(){
		$.ajax({
			url: '<%= ControllerConstants.CHANGE_ACCESS_GROUP_STATE %>',
		    cache: false,
			async: true,
			dataType: 'json',
			type: "POST",
			data: {
				accessGroupId: 	$("#change-status-form #id").val(),
				state: 			$("#change-status-form #state").val(),
				reason: 		$("#change-status-form #reason-for-change").val()
			},
			success: function(data){
				var response = eval(data);
		    	response.msg = decodeMessage(response.msg);
		    	response.msg = replaceAll("+"," ",response.msg);
	
		    	if(response.code == 200 || response.code == "200") {
		    		$("#change-status-form #reason-for-change").val('');
	
		    		$("#changeStatusMsg").val(response.msg);
		    		$("#requestAfterChangeStatus").val('true');
		    		$("#accessGroupId").val($("#id").val());
					$("#access_group_form").submit();
		    	}else{
		    		$("#change-status-form #reason-for-change").focus();
		    		$("#reason-for-change-errors").html(response.msg);
		    	}
			},
		    error: function (jqXHR, textStatus, errorThrown){
		    	handleGenericError(jqXHR, textStatus, errorThrown);
			}
		});
	}
	
	function editAccessGroup(){
		$("#accessGroupId").val($("#id").val());
		$("#access_group_form").submit();
	}
	function loadBredthcrumLinks(formAction, formMethod,requestAction){
		$("#staff-manager-form").attr("action",formAction);
		$("#staff-manager-form").attr("method",formMethod);
		$("#REQUEST_ACTION_TYPE").val(requestAction);
		$('#staff-manager-form').submit();	
	}
</script>
		
</html>
