<%@page import="com.elitecore.sm.common.constants.BaseConstants"%>
<%@taglib 	uri="http://www.springframework.org/tags" 			prefix="spring"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="elitecore" %> 
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="/WEB-INF/ShowErrorTag.tld" prefix="elitecoreError"%>
<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>


			<div class="modal-content">
					<div class="modal-header padding10">
						<h4 class="modal-title">
							<span id="add_page_label" style="display: none;"><spring:message
									code="parser.group.page.create.heading.label" ></spring:message></span> <span
								id="update_page_label" style="display: none;"><spring:message
									code="parser.group.page.update.heading.label" ></spring:message></span>
						</h4>
					</div>
					
					<input type="hidden"  id="pageId" name="pageId" />
					<div class="modal-body padding10 inline-form">
						
						<div class="form-group">
							<spring:message code="parsing.service.attr.grid.column.pageNumber"
								var="tooltip" ></spring:message>
							<div class="table-cell-label">${tooltip}</div>
							<div class="input-group">
								<input type="text" name="pageNumber" id="pageNumber"
									class="form-control table-cell input-sm" data-toggle="tooltip"
									data-placement="bottom" title="${tooltip}"> <span
									class="input-group-addon add-on last"> <i
									class="glyphicon glyphicon-alert" data-toggle="tooltip"
									data-placement="left" title=""></i></span>
							</div>
						</div>
						
						<div class="form-group">
							<spring:message code="parserConfiguration.page.config.grid.column.pageSize"
								var="tooltip" ></spring:message>
							<div class="table-cell-label">${tooltip}</div>
							<div class="input-group">
								<input type="text" name="pageSize" id="pageSize"
									class="form-control table-cell input-sm" data-toggle="tooltip"
									data-placement="bottom" title="${tooltip}"> <span
									class="input-group-addon add-on last"> <i
									class="glyphicon glyphicon-alert" data-toggle="tooltip"
									data-placement="left" title=""></i></span>
							</div>
						</div>
						
						<div class="form-group">
							<spring:message code="parserConfiguration.page.config.grid.column.tableLocation"
								var="tooltip" ></spring:message>
							<div class="table-cell-label">${tooltip}</div>
							<div class="input-group">
								<input type="text" name="tableLocation" id="tableLocation"
									class="form-control table-cell input-sm" data-toggle="tooltip"
									data-placement="bottom" title="${tooltip}"> <span
									class="input-group-addon add-on last"> <i
									class="glyphicon glyphicon-alert" data-toggle="tooltip"
									data-placement="left" title=""></i></span>
							</div>
						</div>
						
						<div class="form-group">
							<spring:message code="parserConfiguration.page.config.grid.column.tableCols"
								var="tooltip" ></spring:message>
							<div class="table-cell-label">${tooltip}</div>
							<div class="input-group">
								<input type="text" name="tableCols" id="tableCols"
									class="form-control table-cell input-sm" data-toggle="tooltip"
									data-placement="bottom" title="${tooltip}"> <span
									class="input-group-addon add-on last"> <i
									class="glyphicon glyphicon-alert" data-toggle="tooltip"
									data-placement="left" title=""></i></span>
							</div>
						</div>
						
						<div class="form-group">
							<spring:message code="parserConfiguration.page.config.grid.column.extractionMethod"
								var="tooltip" ></spring:message>
							<div class="table-cell-label">${tooltip}</div>
							<div class="input-group">
								<input type="text" name="extractionMethod" id="extractionMethod"
									class="form-control table-cell input-sm" data-toggle="tooltip"
									data-placement="bottom" title="${tooltip}"> <span
									class="input-group-addon add-on last"> <i
									class="glyphicon glyphicon-alert" data-toggle="tooltip"
									data-placement="left" title=""></i></span>
							</div>
						</div>
						
					</div>
					<div class="modal-footer padding10">
						<sec:authorize access="hasAuthority('CREATE_PARSER')">
						
							<button type="button" id="addNewPage" class="btn btn-grey btn-xs " style="display: none"
								onclick="createUpdateParserPageConfig('<%=ControllerConstants.ADD_EDIT_PAGE_CONFIG_DETAILS%>',0,'ADD');">
								<spring:message code="btn.label.add" ></spring:message>
							</button>
							
						</sec:authorize>
						<sec:authorize access="hasAuthority('UPDATE_PARSER')">
						
							<button type="button" id="editPageForParser"
								class="btn btn-grey btn-xs " style="display: none"
								onclick="createUpdateParserPageConfig('<%=ControllerConstants.ADD_EDIT_PAGE_CONFIG_DETAILS%>',0,'EDIT');">
								<spring:message code="btn.label.update" ></spring:message>
							</button>
							
						</sec:authorize>
						<button type="button" class="btn btn-grey btn-xs "
							data-dismiss="modal"
							onclick="closeFancyBox();">
							<spring:message code="btn.label.close" ></spring:message>
						</button>
					</div>
				</div>
