<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<!-- Start of hiddenValues.jsp -->

	<input type="hidden" id="JSP_PAGE_NAME" value="${pageContext.request.servletPath }" />
	
	<input type="hidden" id="REQUEST_ACTION_TYPE_HIDDEN" value="${REQUEST_ACTION_TYPE}" />
	
	<input type="hidden" id="GENERIC_ERROR_MESSAGE" value="<spring:message code="generic.error.msg.comment" ></spring:message>" />
	
	<c:if test="${sessionScope.STAFF_DETAILS != null}">
		<input type="hidden" id="loggedInStaffId" value="${sessionScope.STAFF_DETAILS.id}" />
	</c:if>
	
<!-- End of hiddenValues.jsp -->
