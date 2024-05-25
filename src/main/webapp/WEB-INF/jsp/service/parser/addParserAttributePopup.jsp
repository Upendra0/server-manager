<%@page import="com.elitecore.sm.parser.model.UnifiedFieldEnum"%>
<%@taglib 	uri="http://www.springframework.org/tags" 			prefix="spring"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="elitecore" %> 
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<html>

<jsp:include page="../../common/newheader.jsp"></jsp:include>

<script>

	$(document).ready(function() {
		var unifieldAttrList = {};
		<% 
			for (UnifiedFieldEnum p : UnifiedFieldEnum.values()){
				%>
					unifieldAttrList['<%= p %>']='<%= p.getName() %>';
				<%
			}
		%>
		fillDropBox(unifieldAttrList);
	});
	
	function fillDropBox(unifieldAttrList){
		$.each(unifieldAttrList, function(key,value){
			$('#unifiedField').append($('<option>', {
			    value: key,
			    text: value
			}));
		});
	}
</script>

	
	<body class="skin-blue sidebar-mini sidebar-collapse">
		<div id="parser-attribute" style="width: 100%;">
			<div class="modal-content">
				<div class="modal-header padding10">
					<h4 class="modal-title">
						<i class="icon-hdd"></i> 
						<spring:message code="parsing.service.add.attr.popup.header"></spring:message>
					</h4>
				</div>
				<div id="attributeDiv" class="modal-body padding10 inline-form">
					<jsp:include page="../../common/responseMsg.jsp" ></jsp:include>
					
					<div class="form-group">
               			<spring:message code="parsing.service.add.attr.attr.name" var="tooltip"></spring:message>
            			<div class="table-cell-label">${tooltip}</div>
                		<div class="input-group">
                			<input type="text" name="sourceField" id="sourceField" class="form-control table-cell input-sm" data-toggle="tooltip" data-placement="bottom" title="${tooltip}"  readonly="readonly">
                			<span class="input-group-addon add-on last" > <i class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="left" title=""></i></span>
                		</div>
               		</div>
               		<div class="form-group">
               			<spring:message code="parsing.service.add.attr.uni.field" var="tooltip"></spring:message>
            			<div class="table-cell-label">${tooltip}</div>
                		<div class="input-group">
                			<select name="unifiedField" id="unifiedField" class="form-control table-cell input-sm" data-toggle="tooltip" data-placement="bottom" title="${tooltip}"  >
                			</select>
<%--                 			<input type="text" name="unifiedField" id="unifiedField" class="form-control table-cell input-sm" data-toggle="tooltip" data-placement="bottom" title="${tooltip}" > --%>
                			<span class="input-group-addon add-on last" > <i class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="left" title=""></i></span>
                		</div>
               		</div>
               		<div class="form-group">
               			<spring:message code="parsing.service.add.attr.desc" var="tooltip"></spring:message>
            			<div class="table-cell-label">${tooltip}</div>
                		<div class="input-group">
                			<input type="text" name="description" id="description" class="form-control table-cell input-sm" data-toggle="tooltip" data-placement="bottom" title="${tooltip}" >
                			<span class="input-group-addon add-on last" > <i class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="left" title=""></i></span>
                		</div>
               		</div>
               		<div class="form-group">
               			<spring:message code="parsing.service.add.attr.default.val" var="tooltip"></spring:message>
            			<div class="table-cell-label">${tooltip}</div>
                		<div class="input-group">
                			<input type="text" name="defaultValue" id="defaultValue" class="form-control table-cell input-sm" data-toggle="tooltip" data-placement="bottom" title="${tooltip}" >
                			<span class="input-group-addon add-on last" > <i class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="left" title=""></i></span>
                		</div>
               		</div>
               		<div class="form-group">
               			<spring:message code="parsing.service.add.attr.trim.char" var="tooltip"></spring:message>
            			<div class="table-cell-label">${tooltip}</div>
                		<div class="input-group">
                			<input type="text" name="trimChars" id="trimChars" class="form-control table-cell input-sm" data-toggle="tooltip" data-placement="bottom" title="${tooltip}"  >
                			<span class="input-group-addon add-on last" > <i class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="left" title=""></i></span>
                		</div>
               		</div>
					
				</div>
				<div id="buttons-div" class="modal-footer padding10 text-left">
					<button type="button" class="btn btn-grey btn-xs " onclick="parent.closeFancyBox();">
						<spring:message code="btn.label.add"></spring:message>
					</button>
					<button type="button" class="btn btn-grey btn-xs " onclick="parent.closeFancyBox();">
						<spring:message code="btn.label.cancel"></spring:message>
					</button>
				</div>
				<div id="progress-bar-div" class="modal-footer padding10 text-left" style="display: none;">
					<jsp:include page="../../common/processing-bar.jsp"></jsp:include>
				</div>
				
			</div>
			<!-- /.modal-content -->
		</div>
	</body>
	
	<script>
		$(document).ready(function() {
			$('#sourceField').val(parent.$('#selectedAttr').val());
		});
	</script>
</html>
