<?xml version="1.0"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fn="http://www.w3.org/2005/xpath-functions">
	<xsl:output method="xml" indent="yes"></xsl:output>
	<xsl:template match="/">
		<policy-rules>
			<xsl:for-each select="serverInstance/policyRuleList">
			<xsl:if test="status != 'DELETED'">
			<policy-rule>
				<xsl:attribute name="alias">
					<xsl:value-of select="alias"/>
				</xsl:attribute>
				<policy-rule-name><xsl:value-of select="name"/></policy-rule-name>
				<policy-actions>
					<xsl:value-of select="policyActionStr"/>
				</policy-actions>
				<policy-conditions>
					<xsl:value-of select="policyConditionStr"/>
				</policy-conditions>
				<logical-operator-for-conditions><xsl:value-of select="operator"/></logical-operator-for-conditions>
				<global-sequence-rule-id><xsl:value-of select="globalSequenceRuleId"/></global-sequence-rule-id>
				<description><xsl:value-of select="description"/></description>
				<alert-id><xsl:value-of select="alert/alertId"/></alert-id>
				<alert-description><xsl:value-of select="alertDescription"/></alert-description>
				<enable>
					<xsl:choose>
						<xsl:when test="status = 'INACTIVE'">false</xsl:when>
						<xsl:otherwise>true</xsl:otherwise>
					</xsl:choose>
				</enable>
			</policy-rule>	
			</xsl:if>
			</xsl:for-each>
		</policy-rules>
	</xsl:template>
</xsl:stylesheet>