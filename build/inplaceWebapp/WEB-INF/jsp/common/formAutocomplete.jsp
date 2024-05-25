<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib 	uri="http://www.springframework.org/tags" 			prefix="spring"%>
<%@page import="com.elitecore.sm.parser.model.UnifiedFieldEnum"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<style>
   .ui-autocomplete
   {
       z-index:4000 !important;
       max-height: 200px;
       overflow-y: auto;
       overflow-x: hidden;
   }
</style>
<script src="${pageContext.request.contextPath}/customJS/autoSuggestionForUnifiedField.js<%= "?v=" + Math.random() %>" type="text/javascript"></script>

<script>
var unifiedField1;
$(document).ready(function() {
	unifiedField1 = "<%=request.getParameter("unifiedField")%>";
	loadUnfiedAutoComplete("<%=request.getParameter("unifiedField")%>",'getAllUnifiedField');
});


</script>
<form:input path='<%=request.getParameter("unifiedFieldPath")%>' cssClass="form-control table-cell input-sm" tabindex="4" id='<%=request.getParameter("unifiedField")%>' data-toggle="tooltip" data-placement="bottom" title="${tooltip}" maxlength="100"/>