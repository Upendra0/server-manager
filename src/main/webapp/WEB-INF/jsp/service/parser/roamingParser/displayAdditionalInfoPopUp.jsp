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
							<spring:message code="parser.grid.srcDataFormat.format.label"
								var="tooltip" ></spring:message>
							<div class="table-cell-label">${tooltip}</div>
							<div class="input-group">
								<input type="text" id="srcDataFormatPopUp"
									class="form-control table-cell input-sm" data-toggle="tooltip"
									data-placement="bottom" title="${tooltip}" disabled="disabled"> <span
									class="input-group-addon add-on last"> <i
									class="glyphicon glyphicon-alert" data-toggle="tooltip"
									data-placement="left" title=""></i></span>
							</div>
						</div>
						<div class="form-group">
							<spring:message code="parsing.service.add.attr.default.val"
								var="tooltip" ></spring:message>
							<div class="table-cell-label">${tooltip}</div>
							<div class="input-group" >
								<input type="text" id="defaultValuePopUp"
									class="form-control table-cell input-sm" data-toggle="tooltip"
									data-placement="bottom" title="${tooltip}" disabled="disabled"> <span
									class="input-group-addon add-on last"> <i
									class="glyphicon glyphicon-alert" data-toggle="tooltip"
									data-placement="left" title=""></i></span>
							</div>
						</div>
						<div class="form-group">
							<spring:message code="parsing.service.add.attr.trim.char"
								var="tooltip" ></spring:message>
							<div class="table-cell-label">${tooltip}</div>
							<div class="input-group">
								<input type="text" id="trimCharsPopUp"
									class="form-control table-cell input-sm" data-toggle="tooltip"
									data-placement="bottom" title="${tooltip}" disabled="disabled"> <span
									class="input-group-addon add-on last"> <i
									class="glyphicon glyphicon-alert" data-toggle="tooltip"
									data-placement="left" title=""></i></span>
							</div>
						</div>
						
						<div class="form-group">
							<spring:message code="parser.grid.recordInitilializer.format.label.tooltip"
								var="tooltip" ></spring:message>
							<div class="table-cell-label">${tooltip}</div>
							<div class="input-group" >
								<input type="text" id="recordInitilializerPopUp"
									class="form-control table-cell input-sm" data-toggle="tooltip"
									data-placement="bottom" title="${tooltip}" disabled="disabled"> <span
									class="input-group-addon add-on last"> <i
									class="glyphicon glyphicon-alert" data-toggle="tooltip"
									data-placement="left" title=""></i></span>
							</div>
						</div>
						<div class="form-group">
							<spring:message code="parser.grid.unifiedFieldHoldsChoiceId.format.label.tooltip"
								var="tooltip" ></spring:message>
							<div class="table-cell-label">${tooltip}</div>
							<div class="input-group">
								<input name="unifiedFieldHoldsChoiceId" id="recordInitunifiedFieldHoldsChoiceIdilializerPopup"
									class="form-control table-cell input-sm" data-toggle="tooltip"
									data-placement="bottom" title="${tooltip}" disabled="disabled">
								 
								<span	class="input-group-addon add-on last"> 
									<i class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="left" title=""></i>
									</span>
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
