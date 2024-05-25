<%@page import="org.springframework.http.MediaType"%>
<%
	response.setContentType(MediaType.APPLICATION_JSON.toString());
%>
${ERROR_MSG}