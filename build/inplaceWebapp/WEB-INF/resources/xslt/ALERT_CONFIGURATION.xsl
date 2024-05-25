<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="xml" indent="yes"></xsl:output>
	<xsl:template match="/">
	<alerts>
	<xsl:for-each select="snmpAlertCustomObjectWrapper/alertCustomObjectList">
			<xsl:if test="status != 'DELETED'">
			<alert>
			<id><xsl:value-of select="alertId"></xsl:value-of></id>
			<name><xsl:value-of select="alertName"></xsl:value-of></name>
			<description><xsl:value-of select="description"></xsl:value-of></description>
			<service-threshold-list>
					<xsl:for-each select="servicecThresholdList">
				<xsl:if test="status != 'DELETED'">
				<service-threshold>
					<service-id><xsl:value-of select="svcAlias"></xsl:value-of>-<xsl:value-of select="serviceId"></xsl:value-of></service-id>
					<threshold><xsl:value-of select="servicecThreshold"></xsl:value-of></threshold>
                </service-threshold>
                </xsl:if>
			</xsl:for-each>
			</service-threshold-list>
			</alert>
			</xsl:if>
			</xsl:for-each>
	</alerts>
	</xsl:template>
</xsl:stylesheet>