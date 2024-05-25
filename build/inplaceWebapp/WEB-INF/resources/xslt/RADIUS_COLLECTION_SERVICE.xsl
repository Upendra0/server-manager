<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="xml" indent="yes"></xsl:output>
	<xsl:template match="/">
	<xsl:if test="radiusCollectionService/status != 'DELETED'">
		<radius-collection-service>
			<service-address><xsl:value-of select="radiusCollectionService/serverIp"/>:<xsl:value-of select="radiusCollectionService/netFlowPort"/></service-address>
			<startup-mode><xsl:value-of select="radiusCollectionService/svcExecParams/startupMode"/></startup-mode>
			<queue-size><xsl:value-of select="radiusCollectionService/svcExecParams/queueSize"/></queue-size>
			<minimum-thread><xsl:value-of select="radiusCollectionService/svcExecParams/minThread"/></minimum-thread>
			<maximum-thread><xsl:value-of select="radiusCollectionService/svcExecParams/maxThread"/></maximum-thread>
			<max-idle-communication-time-interval><xsl:value-of select="radiusCollectionService/maxIdelCommuTime"/></max-idle-communication-time-interval>
			<parallel-file-write-count><xsl:value-of select="radiusCollectionService/parallelFileWriteCount"/></parallel-file-write-count>
			<max-packet-size-in-byte><xsl:value-of select="radiusCollectionService/maxPktSize"/></max-packet-size-in-byte>
			<max-writebuffer-in-mb><xsl:value-of select="radiusCollectionService/maxWriteBufferSize"/></max-writebuffer-in-mb>
			<clients>
				<xsl:for-each select="radiusCollectionService/netFLowClientList">
				<xsl:if test="status != 'DELETED'">
					<client>
					<client-ip><xsl:value-of select="clientIpAddress"/></client-ip>
					<client-port><xsl:if test="clientPort != -1"><xsl:value-of select="clientPort"/></xsl:if></client-port>
					<request-expiry-time><xsl:value-of select="requestExpiryTime"></xsl:value-of></request-expiry-time>
					<request-retry><xsl:value-of select="requestRetry"></xsl:value-of></request-retry>
					<file-name><xsl:value-of select="fileNameFormat"/></file-name>
					<file-sequence><xsl:value-of select="appendFileSequenceInFileName"/></file-sequence>
					<sequence-range><xsl:if test="minFileSeqValue != '-1' and maxFileSeqValue != '-1'"><xsl:value-of select="minFileSeqValue"/>-<xsl:value-of select="maxFileSeqValue"/></xsl:if></sequence-range>
					<file-location><xsl:value-of select="outFileLocation"/></file-location>
					<secret-key><xsl:value-of select="sharedSecretKey"/></secret-key>
					<xsl:if test="rollingType = 'BOTH'">
						<rolling-time-unit><xsl:value-of select="timeLogRollingUnit"/></rolling-time-unit>
						<rolling-volume-unit><xsl:value-of select="volLogRollingUnit"/></rolling-volume-unit>
					</xsl:if>
					<file-compression><xsl:value-of select="inputCompressed"/></file-compression>
					</client>
				</xsl:if>
				</xsl:for-each>
			</clients>
		</radius-collection-service>
	</xsl:if>
	</xsl:template>
</xsl:stylesheet>