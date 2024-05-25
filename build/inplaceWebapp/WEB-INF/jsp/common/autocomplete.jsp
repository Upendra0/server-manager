<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib 	uri="http://www.springframework.org/tags" 			prefix="spring"%>
<%@page import="com.elitecore.sm.parser.model.UnifiedFieldEnum"%>
<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<style>
   .ui-autocomplete
   {
       z-index:4000 !important;
       max-height: 200px;
       overflow-y: auto;
       overflow-x: hidden;
   }
</style>
<script>
$(document).ready(function() {
	var url = '<%=request.getParameter("url")%>'
	if(url === null)
		loadUnfiedAutoComplete("<%=request.getParameter("unifiedField")%>",'getFixedUnifiedField');
	else
		loadUnfiedAutoComplete("<%=request.getParameter("unifiedField")%>",url);
});
</script>
<div class="input-group">
<input type="text" name="<%=request.getParameter("unifiedField")%>" id="<%=request.getParameter("unifiedField")%>"
	class="form-control table-cell input-sm" data-toggle="tooltip"
	maxlength="100" data-placement="bottom" title="${tooltip}"> <span
	class="input-group-addon add-on last"> <i
	class="glyphicon glyphicon-alert" data-toggle="tooltip"
	data-placement="left" title=""></i></span>
</div>