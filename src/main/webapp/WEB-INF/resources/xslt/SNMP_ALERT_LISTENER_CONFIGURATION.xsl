<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:output method="xml" indent="yes"></xsl:output>
	<xsl:template match="/">
		<snmp-listeners>
		<xsl:for-each select="serverInstance/snmpListeners">
			<xsl:if test="status = 'ACTIVE'">
		<listener>
		<id><xsl:value-of select="id"></xsl:value-of></id>
		<name><xsl:value-of select="name"></xsl:value-of></name>
		<ipAddress><xsl:value-of select="hostIP"></xsl:value-of></ipAddress>
		<port><xsl:value-of select="port"></xsl:value-of></port>
		<version>
		<xsl:if test="version = 'V0'"> <xsl:text>0</xsl:text></xsl:if>
		<xsl:if test="version = 'V1'"> <xsl:text>1</xsl:text></xsl:if>
		<xsl:if test="version = 'V2'"> <xsl:text>2</xsl:text></xsl:if>
		<xsl:if test="version = 'V3'"> <xsl:text>3</xsl:text></xsl:if>
		</version>
		<snmp-v3-configuration>
			<auth-algorithm><xsl:value-of select="snmpV3AuthAlgorithm"></xsl:value-of></auth-algorithm>
			<auth-password><xsl:value-of select="snmpV3AuthPassword"></xsl:value-of></auth-password>
			<privacy-algorithm><xsl:value-of select="snmpV3PrivAlgorithm"></xsl:value-of></privacy-algorithm>
			<priv-password><xsl:value-of select="snmpV3PrivPassword"></xsl:value-of></priv-password>
		</snmp-v3-configuration>
		<advance><xsl:value-of select="advance"></xsl:value-of></advance>
		<community>
		<!-- <xsl:if test="community = 'Public'"> <xsl:text>public</xsl:text></xsl:if>
		<xsl:if test="community = 'Private'"> <xsl:text>private</xsl:text></xsl:if> -->
		<xsl:value-of select="community"></xsl:value-of>
		</community>
		
		<alerts>
		<xsl:for-each select="configuredAlerts/alert">
		<id><xsl:value-of select="alertId"></xsl:value-of></id>
		</xsl:for-each>
		</alerts>
		</listener>
		</xsl:if>
			</xsl:for-each>
		</snmp-listeners>	
	</xsl:template>
</xsl:stylesheet>