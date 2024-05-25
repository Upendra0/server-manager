<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="xml" indent="yes"></xsl:output>
	<xsl:template match="/">
	<xsl:if test="sysLogCollectionService/status != 'DELETED'">
		<syslog-collection-service>
			<service-address><xsl:value-of select="sysLogCollectionService/serverIp"/>:<xsl:value-of select="sysLogCollectionService/netFlowPort"/></service-address>
			<startup-mode><xsl:value-of select="sysLogCollectionService/svcExecParams/startupMode"/></startup-mode>
			<queue-size><xsl:value-of select="sysLogCollectionService/svcExecParams/queueSize"/></queue-size>
			<minimum-thread><xsl:value-of select="sysLogCollectionService/svcExecParams/minThread"/></minimum-thread>
			<maximum-thread><xsl:value-of select="sysLogCollectionService/svcExecParams/maxThread"/></maximum-thread>
			
			<max-idle-communication-time-interval><xsl:value-of select="sysLogCollectionService/maxIdelCommuTime"/></max-idle-communication-time-interval>
			<newline-character-availability><xsl:value-of select="sysLogCollectionService/newLineCharAvailable"/></newline-character-availability>
			<bulk-write-limit><xsl:value-of select="sysLogCollectionService/bulkWriteLimit"/></bulk-write-limit>
			<max-packet-size-in-byte><xsl:value-of select="sysLogCollectionService/maxPktSize"/></max-packet-size-in-byte>
			<max-writebuffer-in-mb><xsl:value-of select="sysLogCollectionService/maxWriteBufferSize"/></max-writebuffer-in-mb>
			<clients>
				<xsl:for-each select="sysLogCollectionService/netFLowClientList">
				<xsl:if test="status != 'DELETED'">
					<client>
					<client-ip><xsl:value-of select="clientIpAddress"/></client-ip>
					<client-port><xsl:if test="clientPort != -1"><xsl:value-of select="clientPort"/></xsl:if></client-port>
					<file-name><xsl:value-of select="fileNameFormat"/></file-name>
					<file-sequence><xsl:value-of select="appendFileSequenceInFileName"/></file-sequence>
					<sequence-range><xsl:if test="minFileSeqValue != '-1' and maxFileSeqValue != '-1'"><xsl:value-of select="minFileSeqValue"/>-<xsl:value-of select="maxFileSeqValue"/></xsl:if></sequence-range>
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
					<file-compression><xsl:value-of select="inputCompressed"/></file-compression>

					<abnormal-time-alert><xsl:attribute name="enable">
							<xsl:value-of select="snmpAlertEnable"/>
						</xsl:attribute>
						<time-interval><xsl:value-of select="alertInterval"/></time-interval>
					</abnormal-time-alert>

					<is-client-enable>
						<xsl:if test="status = 'ACTIVE'"> <xsl:text>true</xsl:text></xsl:if>
						<xsl:if test="status = 'INACTIVE'"> <xsl:text>false</xsl:text></xsl:if>
					</is-client-enable>

					</client>
				</xsl:if>
				</xsl:for-each>
			</clients>
			<parallel-file-write-count><xsl:value-of select="sysLogCollectionService/parallelFileWriteCount"/></parallel-file-write-count>
			<is-tcp-protocol><xsl:value-of select="sysLogCollectionService/isTCPProtocol"/></is-tcp-protocol>
		</syslog-collection-service>
	</xsl:if>
	</xsl:template>
</xsl:stylesheet>