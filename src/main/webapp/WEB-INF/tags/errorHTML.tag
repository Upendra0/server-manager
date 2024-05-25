<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<%@ attribute name="errorCode" required="false" %>
<%@ attribute name="errorBindValue" required="false" %>
<%@ attribute name="errorId" required="false" %>
	<c:choose>
			<c:when test="${not empty errorCode}">
				<c:set var="errorValue" scope="page"><spring:message code="${errorCode}" /></c:set>
			</c:when>
			<c:when test="${not empty errorBindValue}">
				<c:set var="errorValue" scope="page">${errorBindValue}</c:set>
			</c:when>
			<c:otherwise>
				<c:set var="errorValue" scope="page" value=""></c:set>
			</c:otherwise>
		</c:choose>
	
		<span class="input-group-addon add-on last" >
			<i id="${errorId}"class="glyphicon glyphicon-alert" data-toggle="tooltip" data-placement="left" title="${errorValue}">
			</i>
		</span>