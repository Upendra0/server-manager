<%@ tag body-content="tagdependent" isELIgnored="false" %>

<%@ attribute name="type" required="true" %>
<%@ attribute name="id" required="true" %>
<%@ attribute name="name" required="true" %>
<%@ attribute name="inputValue" required="false" %>
<%@ attribute name="inputClassName" required="false" %>
<%@ attribute name="i18NCode" required="false" %>
<%@ attribute name="i18NValue" required="false" %>
<%@ attribute name="i18NToolTipCode" required="true" %>
<%@ attribute name="errorCode" required="false" %>
<%@ attribute name="errorBindValue" required="false" %>
<%@ attribute name="errorId" required="false" %>

<%@ attribute name="labelClass" required="true" %>
<%@ attribute name="isMandatoryField" required="false" %>

<%@ attribute name="isNumeric" required="false" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="elitecore" %>

<c:if test="${i18NValue == null && i18NCode !=null && i18NToolTipCode !=null}">
	<spring:message code="${i18NCode}" var="i18NValue"/>
	<spring:message code="${i18NToolTipCode}" var="i18NToolTip"/>
</c:if>

<div class="form-group">
	<div class="${labelClass}">
		${i18NValue}
		<c:if test="${isMandatoryField eq 'true'}">
			<span class="required">*</span>
		</c:if>
	</div>
	<div class="input-group ">
		<c:choose>
			<c:when test="${isNumeric eq 'true'}">
				<input 
					type="${type}" 
					value="${inputValue}" 
					class="${inputClassName}" 
					id="${id}" 
					name="${name}" 
					title="${i18NToolTip}"
					data-toggle="tooltip"
					data-placement="bottom"
					onkeydown='isNumericOnKeyDown(event)'
					 />
			</c:when>
			<c:otherwise>
				<input 
					type="${type}" 
					value="${inputValue}" 
					class="${inputClassName}" 
					id="${id}" 
					name="${name}" 
					title="${i18NToolTip}"
					data-toggle="tooltip"
					data-placement="bottom" />
			</c:otherwise>
		</c:choose>
	
		<elitecore:errorHTML
			errorCode="${errorCode}"
			errorBindValue="${errorBindValue}"
			errorId="${id}_err"
		 />
	</div>

</div>