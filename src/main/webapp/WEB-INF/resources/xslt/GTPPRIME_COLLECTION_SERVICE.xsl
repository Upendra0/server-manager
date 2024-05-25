<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="xml" indent="yes"></xsl:output>
	<xsl:template match="/">
	<xsl:if test="gtpPrimeCollectionService != 'DELETED'">
		<gtp-prime-service>
			<service-address><xsl:value-of select="gtpPrimeCollectionService/serverIp"/>:<xsl:value-of select="gtpPrimeCollectionService/netFlowPort"/></service-address>
			<startup-mode><xsl:value-of select="gtpPrimeCollectionService/svcExecParams/startupMode"/></startup-mode>
			<queue-size><xsl:value-of select="gtpPrimeCollectionService/svcExecParams/queueSize"/></queue-size>
			<minimum-thread><xsl:value-of select="gtpPrimeCollectionService/svcExecParams/minThread"/></minimum-thread>
			<maximum-thread><xsl:value-of select="gtpPrimeCollectionService/svcExecParams/maxThread"/></maximum-thread>
			<max-idle-communication-time-interval><xsl:value-of select="gtpPrimeCollectionService/maxIdelCommuTime"/></max-idle-communication-time-interval>
			<redirection-ip><xsl:value-of select="gtpPrimeCollectionService/redirectionIP"/></redirection-ip>
			<clients>
				<xsl:for-each select="gtpPrimeCollectionService/netFLowClientList">
				<xsl:if test="status != 'DELETED'">
					<client>
						<client-ip><xsl:value-of select="clientIpAddress"/></client-ip>
						<client-port><xsl:if test="clientPort != -1"><xsl:value-of select="clientPort"/></xsl:if></client-port>
						<node-alive-request><xsl:value-of select="nodeAliveRequest"/></node-alive-request>
						<echo-request><xsl:value-of select="echoRequest"/></echo-request>
						<request-expiry-time><xsl:value-of select="requestExpiryTime"/></request-expiry-time>
						<request-retry><xsl:value-of select="requestRetry"/></request-retry>
						<redirection-ip><xsl:value-of select="redirectionIp"/></redirection-ip>
						<file-name><xsl:value-of select="fileNameFormat"/></file-name>
						<file-sequence><xsl:value-of select="appendFileSequenceInFileName"/></file-sequence>
						<sequence-range>
						<xsl:attribute name="padding"><xsl:value-of select="appendFilePaddingInFileName"/></xsl:attribute>
						<xsl:if test="minFileSeqValue != '-1' and maxFileSeqValue != '-1'"><xsl:value-of select="minFileSeqValue"/>-<xsl:value-of select="maxFileSeqValue"/></xsl:if></sequence-range>
						<file-location><xsl:value-of select="outFileLocation"/></file-location>
						<xsl:if test="rollingType = 'BOTH'">
							<rolling-time-unit><xsl:value-of select="timeLogRollingUnit"/></rolling-time-unit>
							<rolling-volume-unit><xsl:value-of select="volLogRollingUnit"/></rolling-volume-unit>
						</xsl:if>
						<xsl:if test="rollingType = 'TIME_BASED'">
							<rolling-time-unit><xsl:value-of select="timeLogRollingUnit"/></rolling-time-unit>
							<rolling-volume-unit>-1</rolling-volume-unit>
						</xsl:if>
						<xsl:if test="rollingType = 'VOLUME_BASED'">
							<rolling-time-unit>-1</rolling-time-unit>
							<rolling-volume-unit><xsl:value-of select="volLogRollingUnit"/></rolling-volume-unit>
						</xsl:if>
						<request-buffer-count><xsl:value-of select="requestBufferCount"/></request-buffer-count>
						<is-client-enable>
							<xsl:if test="status = 'ACTIVE'"> <xsl:text>true</xsl:text></xsl:if>
							<xsl:if test="status = 'INACTIVE'"> <xsl:text>false</xsl:text></xsl:if>
						</is-client-enable>
					</client>
				</xsl:if>
				</xsl:for-each>
			</clients>
		</gtp-prime-service>
	</xsl:if>
	</xsl:template>
</xsl:stylesheet>