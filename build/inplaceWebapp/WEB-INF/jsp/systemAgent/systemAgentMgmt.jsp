<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="com.elitecore.sm.common.constants.ControllerConstants"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="elitecore"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="/WEB-INF/ShowErrorTag.tld" prefix="elitecoreError"%>
<%@ taglib uri="http://www.springframework.org/security/tags"
	prefix="sec"%>
<%@page import="com.elitecore.sm.common.constants.BaseConstants"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<div
	class="tab-pane <c:if test="${(REQUEST_ACTION_TYPE eq 'UPDATE_SYSTEM_AGENT_CONFIG')}"><c:out value="active"></c:out></c:if>"
	id="SystemAgent">

		<label> <spring:message
				code="updtInstacne.summary.agent.start.stop.name.lbl" ></spring:message> :&nbsp;
		</label> <label><select id="systemAgent"
			class='form-control table-cell input-sm' data-toggle='tooltip'
			data-placement='bottom' title='${tooltip }'
			onChange='profileServiceList();'>

				<c:forEach items="${agentList}" var="agentList">
					<c:if test="${agentList.alias eq 'FILE_RENAME_AGENT'}">
					<sec:authorize access="hasAuthority('FILE_RENAME_AGENT')">
						<option value="${agentList.id}">${agentList.type}</option>
					</sec:authorize>
					</c:if>
					<c:if test="${agentList.alias eq 'PACKET_STATISTICS_AGENT'}">
					<sec:authorize access="hasAuthority('PACKET_STATASTIC_AGENT')">
						<option value="${agentList.id}">${agentList.type}</option>
					</sec:authorize>
					</c:if>
				</c:forEach>
			<option value="-1" selected="selected"><spring:message code="updtInstacne.summary.agent.select.agent.name.lbl"></spring:message></option>
		</select></label>


<c:if test="${selectedAgentType != null }">
<sec:authorize access="hasAuthority('PACKET_STATASTIC_AGENT')">
	<c:if test="${selectedAgentType == 'PACKET_STATISTICS_AGENT' }">
		<div id="packetStatDiv" class="mtop10">
		<jsp:include page="packetStatasticDetails.jsp"></jsp:include>
	</div>
	</c:if>
	</sec:authorize>
	<sec:authorize access="hasAuthority('FILE_RENAME_AGENT')">
	<c:if test="${selectedAgentType == 'FILE_RENAME_AGENT' }">
		<div id="fileRenameDiv" class="mtop10">
		<jsp:include page="fileRenameAgent.jsp"></jsp:include> 
	</div>
	</c:if>
	</sec:authorize>
</c:if>

<c:if test="${selectedAgentTypeId != null }">
						
							<script type="text/javascript">
							$(document).ready(function() {
								var selectedAgentTypeId='${selectedAgentTypeId}';
								$("#systemAgent").val(selectedAgentTypeId);
							});
							</script>
						
					</c:if>


</div>
<div style="display: none;">
	<form id="system_agent_type_form" method="POST" action="<%=ControllerConstants.SPECIFIC_SYSTEM_AGENT_CONFIG%>">
		<input type="hidden" id="systemAgentTypeId" name="systemAgentTypeId" value="${systemAgentTypeId}">
		<input type="hidden" id="agent_server_Instance_Id" name="agent_server_Instance_Id" value='${serverInstanceId}' /> 
		<input type="hidden" id="<%=BaseConstants.REQUEST_ACTION_TYPE%>" name="<%=BaseConstants.REQUEST_ACTION_TYPE%>"
			value="<%=BaseConstants.UPDATE_SYSTEM_AGENT_CONFIG%>" />

	</form>
</div>


<script>

$(document).ready(function() {
	$('#lblInstanceHost').text('${lblInstanceHost}');
});

	
	function profileServiceList() {
			
		var selectedSystemAgent = $("#systemAgent").find(":selected").val();

		if(selectedSystemAgent!=-1){
		$("#systemAgentTypeId").val(selectedSystemAgent);
		$("#system_agent_type_form").submit();
		}

	}
</script>
