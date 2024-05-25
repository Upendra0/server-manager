<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="xml" indent="yes"></xsl:output>
	<xsl:template match="/">
	<xsl:if test="diameterCollectionService/status != 'DELETED'">
	
	<diameter-collection-service>
		<!-- <startup-mode><xsl:value-of select="diameterCollectionService/svcExecParams/startupMode"/></startup-mode> -->
		<stack-identity><xsl:value-of select="diameterCollectionService/stackIdentity"/></stack-identity>
		<stack-realm><xsl:value-of select="diameterCollectionService/stackRealm"/></stack-realm>
		<stack-ip><xsl:value-of select="diameterCollectionService/stackIp"/></stack-ip>
		<stack-port><xsl:value-of select="diameterCollectionService/stackPort"/></stack-port>
		<session-cleanup-interval><xsl:value-of select="diameterCollectionService/sessionCleanupInterval"/></session-cleanup-interval>
		<session-time-out><xsl:value-of select="diameterCollectionService/sessionTimeout"/></session-time-out>
		<max-request-queue-size><xsl:value-of select="diameterCollectionService/svcExecParams/queueSize"/></max-request-queue-size>
		<minimum-thread><xsl:value-of select="diameterCollectionService/svcExecParams/minThread"/></minimum-thread>
		<maximum-thread><xsl:value-of select="diameterCollectionService/svcExecParams/maxThread"/></maximum-thread>
		<action-on-overload><xsl:value-of select="diameterCollectionService/actionOnOverload"/></action-on-overload> <!-- Two possible values only 1. REJECT  2. DROP -->
		<result-code-on-overload><xsl:value-of select="diameterCollectionService/resultCodeOnOverload"/></result-code-on-overload>  <!-- 3004 -Diameter too busy -->
		<duplicate-request-check>
			<xsl:if test="diameterCollectionService/duplicateRequestCheck = 'true'">Enable</xsl:if>
			<xsl:if test="diameterCollectionService/duplicateRequestCheck = 'false'">Disable</xsl:if>
		</duplicate-request-check>
		<duplicate-purge-interval><xsl:value-of select="diameterCollectionService/duplicatePurgeInterval"/></duplicate-purge-interval>
		<field-separator><xsl:value-of select="diameterCollectionService/fieldSeparator"/></field-separator>
		<key-value-separator><xsl:value-of select="diameterCollectionService/keyValueSeparator"/></key-value-separator>
		<group-field-separator><xsl:value-of select="diameterCollectionService/groupFieldSeparator"/></group-field-separator>

		<peer-list>
					<xsl:for-each select="diameterCollectionService/diameterPeerList">
					<xsl:if test="status != 'DELETED'">
						<peer>
							<is-peer-enable>
								<xsl:if test="status = 'ACTIVE'"> <xsl:text>true</xsl:text></xsl:if>
								<xsl:if test="status = 'INACTIVE'"> <xsl:text>false</xsl:text></xsl:if>
							</is-peer-enable>
							<peer-name><xsl:value-of select="name"/></peer-name>
							<peer-identity><xsl:value-of select="identity"/></peer-identity>
							<realm-name><xsl:value-of select="realmName"/></realm-name>
							<watchdog-interval><xsl:value-of select="watchDogInterval"/></watchdog-interval>
							<request-timeout><xsl:value-of select="requestTimeOut"/></request-timeout>
							<file-name><xsl:value-of select="fileNameFormat"/></file-name>
							<file-sequence><xsl:value-of select="fileSeqEnable"/></file-sequence>
							<sequence-range>
								<xsl:attribute name="padding"><xsl:value-of select="appendFilePaddingInFileName"/></xsl:attribute>
								<xsl:if test="minFileSeq != '-1' and maxFileSeq != '-1'"><xsl:value-of select="minFileSeq"/>-<xsl:value-of select="maxFileSeq"/></xsl:if>
							</sequence-range>
							<file-location><xsl:value-of select="outFileLocation"/></file-location>
							<rolling-time-unit><xsl:value-of select="logRollingUnitTime"/></rolling-time-unit>
							<rolling-volume-unit><xsl:value-of select="logRollingUnitVol"/></rolling-volume-unit>
							<file-compression><xsl:value-of select="inputCompressed"/></file-compression>
							<additional-avp-response-list>
							<xsl:for-each select="diameterAVPs">
								<xsl:if test="status != 'DELETED'">
									<avp>
										<vendor-id><xsl:value-of select="vendorId" /></vendor-id>
										<attribute-id><xsl:value-of select="attributeId" /></attribute-id>
										<value><xsl:value-of select="value" /></value>
									</avp>	
								</xsl:if>
							</xsl:for-each>			
							</additional-avp-response-list>
						</peer>
					</xsl:if>
					</xsl:for-each>
		</peer-list>	
	</diameter-collection-service>
	</xsl:if>
	</xsl:template>
</xsl:stylesheet>