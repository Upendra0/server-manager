<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:output method="xml" indent="yes"></xsl:output>
	<xsl:template match="/">
		<snmp>
		<xsl:for-each select="serverInstance/selfSNMPServerConfig">
			<xsl:if test="status != 'DELETED'">
			
		<ipAddress><xsl:value-of select="hostIP"></xsl:value-of></ipAddress>
		<port><xsl:value-of select="port"></xsl:value-of></port>
		<offset><xsl:if test="portOffset != '-1'"><xsl:value-of select="portOffset"></xsl:value-of></xsl:if></offset>
		<community>
		<!-- <xsl:if test="community = 'Public'"> <xsl:text>public</xsl:text></xsl:if>
		<xsl:if test="community = 'Private'"> <xsl:text>private</xsl:text></xsl:if> -->
		<xsl:value-of select="community"></xsl:value-of>
		</community>
		
		</xsl:if>
			</xsl:for-each>
		</snmp>	
	</xsl:template>
</xsl:stylesheet>