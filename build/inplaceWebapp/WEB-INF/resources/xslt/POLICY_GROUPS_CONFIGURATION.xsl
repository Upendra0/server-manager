<?xml version="1.0"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fn="http://www.w3.org/2005/xpath-functions">
	<xsl:output method="xml" indent="yes"></xsl:output>
	<xsl:template match="/">
		<policy-groups>
			<xsl:for-each select="serverInstance/policyGroupList">
			<xsl:if test="status != 'DELETED'">
			<policy-group>
				<xsl:attribute name="alias">
					<xsl:value-of select="alias"/>
				</xsl:attribute>
				<policy-group-name><xsl:value-of select="name"/></policy-group-name	>
				<policy-rules>
					<xsl:value-of select="policyRuleStr"/>
				</policy-rules>
				<description><xsl:value-of select="description"/></description>
				<enable>
					<xsl:choose>
						<xsl:when test="status = 'INACTIVE'">false</xsl:when>
						<xsl:otherwise>true</xsl:otherwise>
					</xsl:choose>
				</enable>
			</policy-group>	
			</xsl:if>
			</xsl:for-each>
		</policy-groups>
	</xsl:template>
</xsl:stylesheet>
