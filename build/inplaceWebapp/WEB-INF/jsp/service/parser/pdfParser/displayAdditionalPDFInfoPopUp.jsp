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
							<spring:message code="parsing.service.attr.grid.column.location"
								var="tooltip" ></spring:message>
							<div class="table-cell-label">${tooltip}</div>
							<div class="input-group">
								<input type="text" id="locationPopUp"
									class="form-control table-cell input-sm" data-toggle="tooltip"
									data-placement="bottom" title="${tooltip}" disabled="disabled"> <span
									class="input-group-addon add-on last"> <i
									class="glyphicon glyphicon-alert" data-toggle="tooltip"
									data-placement="left" title=""></i></span>
							</div>
						</div>
						<div class="form-group">
							<spring:message code="parsing.service.attr.grid.column.columnStartLocation"
								var="tooltip" ></spring:message>
							<div class="table-cell-label">${tooltip}</div>
							<div class="input-group" >
								<input type="text" id="columnStartLocationPopUp"
									class="form-control table-cell input-sm" data-toggle="tooltip"
									data-placement="bottom" title="${tooltip}" disabled="disabled"> <span
									class="input-group-addon add-on last"> <i
									class="glyphicon glyphicon-alert" data-toggle="tooltip"
									data-placement="left" title=""></i></span>
							</div>
						</div>
						<div class="form-group">
							<spring:message code="parsing.service.attr.grid.column.columnIdentifier"
								var="tooltip" ></spring:message>
							<div class="table-cell-label">${tooltip}</div>
							<div class="input-group">
								<input type="text" id="columnIdentifierPopUp"
									class="form-control table-cell input-sm" data-toggle="tooltip"
									data-placement="bottom" title="${tooltip}" disabled="disabled"> <span
									class="input-group-addon add-on last"> <i
									class="glyphicon glyphicon-alert" data-toggle="tooltip"
									data-placement="left" title=""></i></span>
							</div>
						</div>
						
						<div class="form-group">
							<spring:message code="parsing.service.attr.grid.column.pageNumber"
								var="tooltip" ></spring:message>
							<div class="table-cell-label">${tooltip}</div>
							<div class="input-group">
								<input type="text" id="pageNumberPopUp"
									class="form-control table-cell input-sm" data-toggle="tooltip"
									data-placement="bottom" title="${tooltip}" disabled="disabled"> <span
									class="input-group-addon add-on last"> <i
									class="glyphicon glyphicon-alert" data-toggle="tooltip"
									data-placement="left" title=""></i></span>
							</div>
						</div>
						
						<div class="form-group">
							<spring:message code="parsing.service.attr.grid.column.referenceRow"
								var="tooltip" ></spring:message>
							<div class="table-cell-label">${tooltip}</div>
							<div class="input-group" >
								<input type="text" id="referenceRowPopUp"
									class="form-control table-cell input-sm" data-toggle="tooltip"
									data-placement="bottom" title="${tooltip}" disabled="disabled"> <span
									class="input-group-addon add-on last"> <i
									class="glyphicon glyphicon-alert" data-toggle="tooltip"
									data-placement="left" title=""></i></span>
							</div>
						</div>
						<c:if test="${(isGroupConfiguration) }">
							<div class="form-group">
								<spring:message code="parsing.service.attr.grid.column.referenceCol"
									var="tooltip" ></spring:message>
								<div class="table-cell-label">${tooltip}</div>
								<div class="input-group" >
									<input type="text" id="referenceColPopUp"
										class="form-control table-cell input-sm" data-toggle="tooltip"
										data-placement="bottom" title="${tooltip}" disabled="disabled"> <span
										class="input-group-addon add-on last"> <i
										class="glyphicon glyphicon-alert" data-toggle="tooltip"
										data-placement="left" title=""></i></span>
								</div>
							</div>
							
							<div class="form-group">
								<spring:message code="parsing.service.attr.grid.column.multiline.attribute"
									var="tooltip" ></spring:message>
								<div class="table-cell-label">${tooltip}</div>
								<div class="input-group">
									<input type="text" id="multiLineAttributePopUp"
										class="form-control table-cell input-sm" data-toggle="tooltip"
										data-placement="bottom" title="${tooltip}" disabled="disabled"> <span
										class="input-group-addon add-on last"> <i
										class="glyphicon glyphicon-alert" data-toggle="tooltip"
										data-placement="left" title=""></i></span>
								</div>
							</div>
							
							<div class="form-group">
								<spring:message code="parsing.service.attr.grid.column.row.text.alignment"
									var="tooltip" ></spring:message>
								<div class="table-cell-label">${tooltip}</div>
								<div class="input-group">
									<input type="text" id="rowTextAlignmentPopUp"
										class="form-control table-cell input-sm" data-toggle="tooltip"
										data-placement="bottom" title="${tooltip}" disabled="disabled"> <span
										class="input-group-addon add-on last"> <i
										class="glyphicon glyphicon-alert" data-toggle="tooltip"
										data-placement="left" title=""></i></span>
								</div>
							</div>
							
						</c:if>
						<div class="form-group">
							<spring:message code="parsing.service.attr.grid.column.default.val"
								var="tooltip" ></spring:message>
							<div class="table-cell-label">${tooltip}</div>
							<div class="input-group" >
								<input type="text" id="defaultTextPopUp"
									class="form-control table-cell input-sm" data-toggle="tooltip"
									data-placement="bottom" title="${tooltip}" disabled="disabled"> <span
									class="input-group-addon add-on last"> <i
									class="glyphicon glyphicon-alert" data-toggle="tooltip"
									data-placement="left" title=""></i></span>
							</div>
						</div>
						<div class="form-group">
							<spring:message code="parsing.service.attr.grid.column.columnStartsWith"
								var="tooltip" ></spring:message>
							<div class="table-cell-label">${tooltip}</div>
							<div class="input-group" >
								<input type="text" id="columnStartsWithPopUp"
									class="form-control table-cell input-sm" data-toggle="tooltip"
									data-placement="bottom" title="${tooltip}" disabled="disabled"> <span
									class="input-group-addon add-on last"> <i
									class="glyphicon glyphicon-alert" data-toggle="tooltip"
									data-placement="left" title=""></i></span>
							</div>
						</div>
						
						<div class="form-group">
							<spring:message code="parsing.service.attr.grid.column.valueSeparator.field.attribute"
								var="tooltip" ></spring:message>
							<div class="table-cell-label">${tooltip}</div>
							<div class="input-group">
								<input type="text" id="valueSeparatorPopUp"
									class="form-control table-cell input-sm" data-toggle="tooltip"
									data-placement="bottom" title="${tooltip}" disabled="disabled"> <span
									class="input-group-addon add-on last"> <i
									class="glyphicon glyphicon-alert" data-toggle="tooltip"
									data-placement="left" title=""></i></span>
							</div>
						</div>
						
						<div class="form-group">
							<spring:message code="parsing.service.attr.grid.column.columnEndsWith"
								var="tooltip" ></spring:message>
							<div class="table-cell-label">${tooltip}</div>
							<div class="input-group">
								<input type="text" id="columnEndsWithPopUp"
									class="form-control table-cell input-sm" data-toggle="tooltip"
									data-placement="bottom" title="${tooltip}" disabled="disabled"> <span
									class="input-group-addon add-on last"> <i
									class="glyphicon glyphicon-alert" data-toggle="tooltip"
									data-placement="left" title=""></i></span>
							</div>
						</div>
						
						
						<div class="form-group">
							<spring:message code="parsing.service.attr.grid.column.mandatory"
								var="tooltip" ></spring:message>
							<div class="table-cell-label">${tooltip}</div>
							<div class="input-group">
								<input type="text" id="mandatoryPopUp"
									class="form-control table-cell input-sm" data-toggle="tooltip"
									data-placement="bottom" title="${tooltip}" disabled="disabled"> <span
									class="input-group-addon add-on last"> <i
									class="glyphicon glyphicon-alert" data-toggle="tooltip"
									data-placement="left" title=""></i></span>
							</div>
						</div>
						
						<div class="form-group">
							<spring:message code="parsing.service.attr.grid.column.multipleValues"
								var="tooltip" ></spring:message>
							<div class="table-cell-label">${tooltip}</div>
							<div class="input-group">
								<input type="text" id="multipleValuesPopUp"
									class="form-control table-cell input-sm" data-toggle="tooltip"
									data-placement="bottom" title="${tooltip}" disabled="disabled"> <span
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
