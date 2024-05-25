<%@ tag body-content="tagdependent" isELIgnored="false" %>


<%@ attribute name="labelClass" required="true" %>
<%@ attribute name="i18NLabelCode" required="true" %>

<%@ attribute name="id" required="true" %>
<%@ attribute name="labelValueClass" required="true" %>
<%@ attribute name="labelValue" required="false" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<div class="form-group">
	<div class="${labelClass}">
		<spring:message code="${i18NLabelCode}"/>
	</div>
	<div class="input-group ">
		<label class="${labelValueClass}" id="${id}">${labelValue}</label>
	</div>
</div>