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
			<spring:message code="parsing.service.attr.grid.column.td.no"
				var="tooltip" ></spring:message>
			<div class="table-cell-label">${tooltip}</div>
			<div class="input-group">
				<input type="text" name="tdNo" id="tdNo"
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
			<button type="button" id="addNewAttribute"
				class="btn btn-grey btn-xs " style="display: none"
				onclick="createUpdateParserAttribute('<%=ControllerConstants.ADD_EDIT_HTML_PARSER_ATTRIBUTE%>',0,'ADD','YES');">
				<spring:message code="btn.label.add" ></spring:message>
			</button>
		</sec:authorize>
		<sec:authorize access="hasAuthority('UPDATE_PARSER')">
			<button type="button" id="editAttributeForParser"
				class="btn btn-grey btn-xs " style="display: none"
				onclick="createUpdateParserAttribute('<%=ControllerConstants.ADD_EDIT_HTML_PARSER_ATTRIBUTE%>',0,'EDIT');">
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
