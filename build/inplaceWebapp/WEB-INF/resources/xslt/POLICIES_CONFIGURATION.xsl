<?xml version="1.0"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fn="http://www.w3.org/2005/xpath-functions">
	<xsl:output method="xml" indent="yes"></xsl:output>
	<xsl:template match="/">
		<policies>
			<xsl:for-each select="serverInstance/policyList">
			<xsl:if test="status != 'DELETED'">
			<policy>
				<xsl:attribute name="alias">
					<xsl:value-of select="alias"/>
				</xsl:attribute>
				<policy-name><xsl:value-of select="name"/></policy-name>
				<policy-groups>
					<xsl:value-of select="policyGroupStr"/>
				</policy-groups>
				<description><xsl:value-of select="description"/></description>
				<enable>
					<xsl:choose>
						<xsl:when test="status = 'INACTIVE'">false</xsl:when>
						<xsl:otherwise>true</xsl:otherwise>
					</xsl:choose>
				</enable>
			</policy>	
			</xsl:if>
			</xsl:for-each>
		</policies>
	</xsl:template>
</xsl:stylesheet>
