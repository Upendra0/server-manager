<%@page import="com.elitecore.sm.common.constants.BaseConstants"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="elitecore" %> 
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="/WEB-INF/ShowErrorTag.tld" prefix="elitecoreError"%>
<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<%@ taglib uri="http://www.springframework.org/security/tags"
	prefix="sec"%>

			<div class="modal-content">
					<div class="modal-header padding10">
						<h4 class="modal-title">
							<span id="grpAdd_label" style="display: none;"><spring:message
									code="roaming.group.attribute.create.heading.label" ></spring:message></span> 
							<span id="grpUpdate_label" style="display: none;"><spring:message
									code="roaming.group.attribute.update.heading.label" ></spring:message></span>
							<span id="grpView_label" style="display: none;"><spring:message
									code="roaming.group.attribute.view.heading.label" ></spring:message></span>
						</h4>
					</div>
					
					<div class="modal-body padding10 inline-form">
						<div id="AddPopUpMsg">
							<jsp:include page="../../common/responseMsgPopUp.jsp" ></jsp:include>
						</div>
						<div class="no-padding" id="grpName">
							<div class="form-group">
								<spring:message code="roaming.group.attr.grid.group.name" var="tooltip" ></spring:message>
								<div class="table-cell-label">${tooltip }<span class="required">*</span></div>
								<div class="input-group">
									<form:input path="name" cssClass="form-control table-cell input-sm" tabindex="4"
										id="name" data-toggle="tooltip" data-placement="bottom" title="${tooltip }" ></form:input>
									<spring:bind path="name">
										<elitecoreError:showError errorMessage="${status.errorMessage}"></elitecoreError:showError>
									</spring:bind>
								</div>
							</div>
						</div>
					
						<div class="fullwidth" id="group_attribute_div">
							<div class="title2">
							<spring:message code="roaming.group.attribute.grid.heading.label" ></spring:message>
							</div>
						</div>
						<div class="col-md-12 inline-form no-padding" id="view_groupAttr_grid_div">
							<div class="box-body table-responsive no-padding box" id="view_group_attribute">
								<table class="table table-hover" id="viewGroupAttributeList"></table>
								<div id="viewGroupAttributeListDiv"></div>
								<div class="clearfix"></div>
								<div id="divLoading" align="center" style="display: none;">
								<img src="img/preloaders/Preloader_10.gif" />
								</div>
							</div>
						</div>
						<div class="col-md-12 inline-form no-padding" id="update_groupAttr_grid_div">
							<div class="box-body table-responsive no-padding box" id="update_group_attribute">
								<table class="table table-hover" id="updateGroupAttributeList"></table>
								<div id="updateGroupAttributeListDiv"></div>
								<div class="clearfix"></div>
								<div id="divLoading" align="center" style="display: none;">
								<img src="img/preloaders/Preloader_10.gif" />
								</div>
							</div>
						</div>
		
						<div class="fullwidth" id="attribute_div">
							<div class="title2">
							<spring:message code="parserConfiguration.attribute.grid.heading.label" ></spring:message>
							</div>
						</div>
						<div class="col-md-12 inline-form no-padding" id="view_AttrList_grid_div">
							<div class="box-body table-responsive no-padding box" id="view_attribute_list">
								<table class="table table-hover" id="viewAttributeList"></table>
								<div id="viewAttributeListDiv"></div>
								<div class="clearfix"></div>
								<div id="divLoading" align="center" style="display: none;">
									<img src="img/preloaders/Preloader_10.gif" />
								</div>
							</div>
						</div>
						<div class="col-md-12 inline-form no-padding" id="update_AttrList_grid_div">
							<div class="box-body table-responsive no-padding box" id="update_attribute_list">
								<table class="table table-hover" id="updateAttributeList"></table>
								<div id="updateAttributeListDiv"></div>
								<div class="clearfix"></div>
								<div id="divLoading" align="center" style="display: none;">
									<img src="img/preloaders/Preloader_10.gif" />
								</div>
							</div>
						</div>
					</div>
					
					<div class="modal-footer padding10">
					<sec:authorize access="hasAuthority('CREATE_COMPOSER')">
						<button type="button" id="grpAddNewAttribute" class="btn btn-grey btn-xs " style="display: none" onclick="createUpdateGroupAttribute('ADD');">
							<spring:message code="btn.label.add" ></spring:message>
						</button>
						</sec:authorize>
						<sec:authorize access="hasAuthority('UPDATE_COMPOSER')">
						<button type="button" id="grpEditAttributeDistribution" class="btn btn-grey btn-xs " style="display: none" onclick="createUpdateGroupAttribute('EDIT');">
							<spring:message code="btn.label.update" ></spring:message>
						</button>
						</sec:authorize>
						<button type="button" class="btn btn-grey btn-xs "
							data-dismiss="modal" id="grpClosebtn"
							onclick="closeFancyBox();reloadGroupAttributeGridData();reloadAttributeGridData();">
							<spring:message code="btn.label.close" ></spring:message>
						</button>
						<button type="button" class="btn btn-grey btn-xs "
							data-dismiss="modal" id="grpViewClose"
							onclick="closeFancyBox();">
							<spring:message code="btn.label.close" ></spring:message>
						</button>
					</div>
				</div>
				
<script type="text/javascript">
$(document).ready(function() {
});

/*Function will add/edit composer attribute details */
function createUpdateGroupAttribute(actionType){
	
	$.ajax({
		url: '<%=ControllerConstants.ADD_EDIT_GROUP_ATTRIBUTE %>',
		cache: false,
		async: true,
		dataType:'json',
		type:'POST',
		data:
		 {
			"id" 					: $("#id").val(),
			"name" 					: $("#name").val(),
			"actionType" 			: actionType,
			"plugInType"    		: $("#selplugInType").val(),
			"mappingId"    			: $("#selConfigMappingId").val(),
			"myComposer.id"    		: $("#selConfigMappingId").val(),
			"groupAttrLists"		: ckIntanceSelectedGroupAttrListOnPopUp.toString(),
			"attrLists"				: ckIntanceSelectedAttrListOnPopUp.toString()
		}, 
		success: function(data){
			var response = eval(data);
			var responseCode = response.code; 
			var responseMsg = response.msg; 
			var responseObject = response.object;
		
			clearAllMessagesPopUp();
			resetWarningDisplayPopUp();
			if(responseCode === "200"){
				$("#grpEditAttributeDistribution").hide();
				$("#grpAddNewAttribute").hide();
				reloadGroupAttributeGridData();
				closeFancyBox();
				showSuccessMsg(responseMsg);
				reloadAttributeGridData();
			}else if(responseObject !== undefined && responseObject !== 'undefined' && responseCode === "400"){			
				addErrorIconAndMsgForAjaxPopUp(responseObject);
			}else{
				showErrorMsgPopUp(responseMsg);
			}
		},
	    error: function (xhr,st,err){
			handleGenericError(xhr,st,err);
		}
	});
}


</script>				
