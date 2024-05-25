<?xml version="1.0"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fn="http://www.w3.org/2005/xpath-functions">
	<xsl:output method="xml" indent="yes"></xsl:output>
	<xsl:template match="/">
		<policy-actions>
			<xsl:for-each select="serverInstance/policyActionList">
			<xsl:if test="status != 'DELETED'">
			<policy-action>
				<xsl:attribute name="alias">
					<xsl:value-of select="alias"/>
				</xsl:attribute>
				<policy-action-name><xsl:value-of select="name"/></policy-action-name>
				<description><xsl:value-of select="description"/></description>
				<action><xsl:value-of select="action"/></action>
				<type><xsl:value-of select="type"/></type>
				<expression><xsl:value-of select="actionExpressionForSync"/></expression>
			</policy-action>	
			</xsl:if>
			</xsl:for-each>
		</policy-actions>
	</xsl:template>
</xsl:stylesheet>
