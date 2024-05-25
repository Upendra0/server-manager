<%@page import="com.elitecore.sm.common.constants.BaseConstants"%>
<%@taglib 	uri="http://www.springframework.org/tags" 			prefix="spring"%>
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
							<span id="display_label">
								<spring:message code="label.additional.information" ></spring:message>
							</span> 
						</h4>
					</div>
					<div class="modal-body padding10 inline-form">
					<div id="viewPopUpMsg">
						</div>
						<div class="form-group">
							<spring:message code="policymgmt.databaseQueries.database.query.value"
								var="tooltip" ></spring:message>
							<div class="table-cell-label">${tooltip}</div>
							<div class="input-group">
								<input type="text" id="queryValuePopup" 
									class="form-control table-cell input-sm" data-toggle="tooltip"
									data-placement="bottom" title="${tooltip}" disabled="disabled"/> <span
									class="input-group-addon add-on last"> <i
									class="glyphicon glyphicon-alert" data-toggle="tooltip"
									data-placement="left" title=""></i></span>
							</div>
						</div>
						<div class="form-group">
							<spring:message code="policymgmt.databaseQueries.outputDBField"
								var="tooltip" ></spring:message>
							<div class="table-cell-label">${tooltip}</div>
							<div class="input-group" >
								<input type="text" id="outputDBFieldPopup" 
									class="form-control table-cell input-sm" data-toggle="tooltip"
									data-placement="bottom" title="${tooltip}" disabled="disabled"/> <span
									class="input-group-addon add-on last"> <i
									class="glyphicon glyphicon-alert" data-toggle="tooltip"
									data-placement="left" title=""></i></span>
							</div>
						</div>
						<div class="form-group">
							<spring:message code="policymgmt.databaseQueries.conditionExpression"
								var="tooltip" ></spring:message>
							<div class="table-cell-label">${tooltip}</div>
							<div class="input-group">
								<input type="text" id="conditionExpressionPopup"
									class="form-control table-cell input-sm" data-toggle="tooltip"
									data-placement="bottom" title="${tooltip}" disabled="disabled"/> <span
									class="input-group-addon add-on last"> <i
									class="glyphicon glyphicon-alert" data-toggle="tooltip"
									data-placement="left" title=""></i></span>
							</div>
						</div>
						
						<div class="form-group">
							<spring:message code="policymgmt.databaseQueries.logical"
								var="tooltip" ></spring:message>
							<div class="table-cell-label">${tooltip}</div>
							<div class="input-group" >
								<select id="logicalOperatorPopup"
									class="form-control table-cell input-sm" data-toggle="tooltip"
									data-placement="bottom" title="${tooltip}" disabled="disabled">
									<option value = "or">Or</option>
									<option value = "and">And</option>
								</select>
								 <span
									class="input-group-addon add-on last"> <i
									class="glyphicon glyphicon-alert" data-toggle="tooltip"
									data-placement="left" title=""></i></span>
							</div>
						</div>
						<div class="form-group">
							<spring:message code="policymgmt.databaseQueries.conditionExpression.enable"
								var="tooltip" ></spring:message>
							<div class="table-cell-label">${tooltip}</div>
							<div class="input-group" >
								<select id="conditionExpressionEnablePopup"
									class="form-control table-cell input-sm" data-toggle="tooltip"
									data-placement="bottom" title="${tooltip}" disabled="disabled">
									<option value = "true">True</option>
									<option value = "false">False</option>
								</select>
								<span
									class="input-group-addon add-on last"> <i
									class="glyphicon glyphicon-alert" data-toggle="tooltip"
									data-placement="left" title=""></i></span>
							</div>
						</div>
						<div class="form-group">
							<spring:message code="policymgmt.databaseQueries.returnMultipleRows"
								var="tooltip" ></spring:message>
							<div class="table-cell-label">${tooltip}</div>
							<div class="input-group" >
								<select id="returnMultipleRowsEnablePopup"
									class="form-control table-cell input-sm" data-toggle="tooltip"
									data-placement="bottom" title="${tooltip}" disabled="disabled">
								<option value = "true">True</option>
								<option value = "false">False</option>
							</select>
								<span
									class="input-group-addon add-on last"> <i
									class="glyphicon glyphicon-alert" data-toggle="tooltip"
									data-placement="left" title=""></i></span>
							</div>
						</div>
						<div class="form-group">
							<spring:message code="policymgmt.databaseQueries.cache"
								var="tooltip" ></spring:message>
							<div class="table-cell-label">${tooltip}</div>
							<div class="input-group" >
								<select id="cacheEnablePopup"
									class="form-control table-cell input-sm" data-toggle="tooltip"
									data-placement="bottom" title="${tooltip}" disabled="disabled">
								<option value = "true">True</option>
								<option value = "false">False</option>
							</select>
								<span
									class="input-group-addon add-on last"> <i
									class="glyphicon glyphicon-alert" data-toggle="tooltip"
									data-placement="left" title=""></i></span>
							</div>
						</div>
						
					</div>
					<div class="modal-footer padding10">
						<button type="button" class="btn btn-grey btn-xs "
							data-dismiss="modal"
							onclick="closeFancyBox();">
							<spring:message code="btn.label.close" ></spring:message>
						</button>
					</div>
				</div>
<script>
$(document).ready(function() {
	changeConditionList();
});
				
</script>
