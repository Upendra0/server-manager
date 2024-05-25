<%@page import="com.elitecore.sm.common.constants.BaseConstants"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="elitecore"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="/WEB-INF/ShowErrorTag.tld" prefix="elitecoreError"%>
<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<%@ taglib uri="http://www.springframework.org/security/tags"
	prefix="sec"%>
<script src="${pageContext.request.contextPath}/customJS/autoSuggestionForUnifiedField.js<%= "?v=" + Math.random() %>" type="text/javascript"></script>
<div class="modal-content">
	<div class="modal-header padding10">
		<h4 class="modal-title">
			<span id="add_label" style="display: none;"><spring:message
					code="parser.attribute.create.heading.label" ></spring:message></span> <span
				id="update_label" style="display: none;"><spring:message
					code="parser.attribute.update.heading.label" ></spring:message></span>
		</h4>
	</div>
	<div id="parserGroupAttribute" class="modal-body padding10 inline-form">
		<div id="AddPopUpMsg">
			<jsp:include page="../../../common/responseMsgPopUp.jsp" ></jsp:include>
		</div>
		<div class="form-group">
			<spring:message code="parsing.service.add.attr.attr.name"
				var="tooltip" ></spring:message>
			<div class="table-cell-label">${tooltip}</div>
			<div class="input-group">
				<input type="text" name="sourceField" id="sourceField"
					class="form-control table-cell input-sm" data-toggle="tooltip"
					data-placement="bottom" title="${tooltip}"> <span
					class="input-group-addon add-on last"> <i
					class="glyphicon glyphicon-alert" data-toggle="tooltip"
					data-placement="left" title=""></i></span>
			</div>
		</div>
		<div class="form-group">
			<spring:message code="parsing.service.add.attr.uni.field"
				var="tooltip" ></spring:message>
			<div class="table-cell-label">${tooltip}</div>
			<jsp:include page="../../../common/autocomplete.jsp">
				<jsp:param name="unifiedField" value="unifiedField" ></jsp:param>
			</jsp:include>
		</div>
		<div class="form-group">
			<spring:message code="parsing.service.add.attr.default.val"
				var="tooltip" ></spring:message>
			<div class="table-cell-label">${tooltip}</div>
			<div class="input-group">
				<input type="text" name="defaultValue" id="defaultValue"
					class="form-control table-cell input-sm" data-toggle="tooltip"
					data-placement="bottom" title="${tooltip}"> <span
					class="input-group-addon add-on last"> <i
					class="glyphicon glyphicon-alert" data-toggle="tooltip"
					data-placement="left" title=""></i></span>
			</div>
		</div>
		<div class="form-group">
			<spring:message code="parsing.service.add.attr.trim.char" var="tooltip" ></spring:message>
			<div class="table-cell-label">${tooltip}</div>
			<div class="input-group">
				<input type="text" name="trimChars" id="trimChars"
					class="form-control table-cell input-sm" data-toggle="tooltip"
					data-placement="bottom" title="${tooltip}"> <span
					class="input-group-addon add-on last"> <i
					class="glyphicon glyphicon-alert" data-toggle="tooltip"
					data-placement="left" title=""></i></span>
			</div>
		</div>
		<div class="form-group">
			<spring:message code="parsing.service.add.attr.trim.position" var="tooltip" ></spring:message>
			<div class="table-cell-label">${tooltip}</div>
			<div class="input-group">
				<select name="trimPosition" id="trimPosition"
					class="form-control table-cell input-sm" data-toggle="tooltip"
					data-placement="top" title="${tooltip}">
					<option value="">Select trim position</option>
					<c:forEach var="trimPosition" items="${trimPosition}">
						<option value="${trimPosition.value}">${trimPosition}</option>
					</c:forEach>
				</select> <span class="input-group-addon add-on last"> <i
					class="glyphicon glyphicon-alert" data-toggle="tooltip"
					data-placement="left" title=""></i></span>
			</div>
		</div>
		<div class="form-group">
			<spring:message code="parser.grid.field.startsWith"
				var="tooltip" ></spring:message>
			<div class="table-cell-label">${tooltip}</div>
			<div class="input-group">
				<input type="text" name="startsWith" id="startsWith"
					class="form-control table-cell input-sm" data-toggle="tooltip"
					data-placement="bottom" title="${tooltip}" > <span
					class="input-group-addon add-on last" > <i
					class="glyphicon glyphicon-alert" data-toggle="tooltip"
					data-placement="left" title=""></i></span>
			</div>
		</div>
		<div class="form-group">
			<spring:message code="parser.grid.field.table.footer" var="tooltip" ></spring:message>
			<div class="table-cell-label">${tooltip}</div>
			<div class="input-group">
				<select name="tableFooter" id="tableFooter" 
					class="form-control table-cell input-sm" data-toggle="tooltip"
					data-placement="bottom" title="${tooltip}">
					<c:forEach var="trueFalseEnum" items="${trueFalseEnum}" >
						<option value="${trueFalseEnum.name}">${trueFalseEnum}</option>
					</c:forEach>
				</select> 
				<span class="input-group-addon add-on last"> 
					<i class="glyphicon glyphicon-alert" data-toggle="tooltip"
					data-placement="left" title=""></i>
				</span>
			</div>
		</div>
		<div class="form-group">
			<spring:message code="parser.grid.field.excel.row"
				var="tooltip" ></spring:message>
			<div class="table-cell-label">${tooltip}</div>
			<div class="input-group">
				<input type="text" name="excelRow" id="excelRow"
					class="form-control table-cell input-sm" data-toggle="tooltip"
					data-placement="bottom" title="${tooltip}" onkeydown="isNumericOnKeyDown(event)" maxlength="7"> <span
					class="input-group-addon add-on last"    > <i
					class="glyphicon glyphicon-alert" data-toggle="tooltip"
					data-placement="left" title=""></i></span>
			</div>
		</div>
		
		<div class="form-group">
			<spring:message code="parser.grid.field.excel.col"
				var="tooltip" ></spring:message>
			<div class="table-cell-label">${tooltip}</div>
			<div class="input-group">
				<input type="text" name="excelCol" id="excelCol"
					class="form-control table-cell input-sm" data-toggle="tooltip"
					data-placement="bottom" title="${tooltip}" onkeydown="isNumericOnKeyDown(event)" maxlength="7"> <span
					class="input-group-addon add-on last"    > <i
					class="glyphicon glyphicon-alert" data-toggle="tooltip"
					data-placement="left" title=""></i></span>
			</div>
		</div>
		
		<div class="form-group">
			<spring:message code="parser.grid.field.relative.excel.row"
				var="tooltip" ></spring:message>
			<div class="table-cell-label">${tooltip}</div>
			<div class="input-group">
				<input type="text" name="relativeExcelRow" id="relativeExcelRow"
					class="form-control table-cell input-sm" data-toggle="tooltip"
					data-placement="bottom" title="${tooltip}" onkeydown="isNumericOnKeyDown(event)" maxlength="7"> <span
					class="input-group-addon add-on last"    > <i
					class="glyphicon glyphicon-alert" data-toggle="tooltip"
					data-placement="left" title=""></i></span>
			</div>
		</div>
		
		<div class="form-group">
			<spring:message code="parser.grid.field.column.starts.with"
				var="tooltip" ></spring:message>
			<div class="table-cell-label">${tooltip}</div>
			<div class="input-group">
				<input type="text" name="columnStartsWith" id="columnStartsWith"
					class="form-control table-cell input-sm" data-toggle="tooltip"
					data-placement="bottom" title="${tooltip}" > <span
					class="input-group-addon add-on last" > <i
					class="glyphicon glyphicon-alert" data-toggle="tooltip"
					data-placement="left" title=""></i></span>
			</div>
		</div>
		<div class="form-group">
			<spring:message code="parser.grid.field.column.contains"
				var="tooltip" ></spring:message>
			<div class="table-cell-label">${tooltip}</div>
			<div class="input-group">
				<input type="text" name="columnContains" id="columnContains"
					class="form-control table-cell input-sm" data-toggle="tooltip"
					data-placement="bottom" title="${tooltip}" > <span
					class="input-group-addon add-on last" > <i
					class="glyphicon glyphicon-alert" data-toggle="tooltip"
					data-placement="left" title=""></i></span>
			</div>
		</div>
		<div class="form-group">
			<spring:message code="parser.grid.field.table.row.attribute" var="tooltip" ></spring:message>
			<div class="table-cell-label">${tooltip}</div>
			<div class="input-group">
				<select name="tableRowAttribute" id="tableRowAttribute" 
					class="form-control table-cell input-sm" data-toggle="tooltip"
					data-placement="bottom" title="${tooltip}">
					<c:forEach var="trueFalseEnum" items="${trueFalseEnum}" >
						<option value="${trueFalseEnum.name}">${trueFalseEnum}</option>
					</c:forEach>
				</select> 
				<span class="input-group-addon add-on last"> 
					<i class="glyphicon glyphicon-alert" data-toggle="tooltip"
					data-placement="left" title=""></i>
				</span>
			</div>
		</div>
		
	</div>
	<div class="modal-footer padding10">
		<sec:authorize access="hasAuthority('CREATE_PARSER')">
			<button type="button" id="addNewAttribute"
				class="btn btn-grey btn-xs " style="display: none"
				onclick="createUpdateParserAttribute('<%=ControllerConstants.ADD_EDIT_XLS_PARSER_ATTRIBUTE%>',0,'ADD','YES');">
				<spring:message code="btn.label.add" ></spring:message>
			</button>
		</sec:authorize>
		<sec:authorize access="hasAuthority('UPDATE_PARSER')">
			<button type="button" id="editAttributeForParser"
				class="btn btn-grey btn-xs " style="display: none"
				onclick="createUpdateParserAttribute('<%=ControllerConstants.ADD_EDIT_XLS_PARSER_ATTRIBUTE%>',0,'EDIT');">
				<spring:message code="btn.label.update" ></spring:message>
			</button>
		</sec:authorize>
		<button type="button" class="btn btn-grey btn-xs "
			data-dismiss="modal"
			onclick="closeFancyBox();reloadAttributeGridData();">
			<spring:message code="btn.label.close" ></spring:message>
		</button>
	</div>
</div>
