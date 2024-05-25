<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="xml" indent="yes"></xsl:output>
	<xsl:template match="/">
	<xsl:if test="netflowBinaryCollectionService/status != 'DELETED'">
		<natflow-binary-collection-service>
			<service-address><xsl:value-of select="netflowBinaryCollectionService/serverIp"/>:<xsl:value-of select="netflowBinaryCollectionService/netFlowPort"/></service-address>
			<startup-mode><xsl:value-of select="netflowBinaryCollectionService/svcExecParams/startupMode"/></startup-mode>
			<queue-size><xsl:value-of select="netflowBinaryCollectionService/svcExecParams/queueSize"/></queue-size>
			<minimum-thread><xsl:value-of select="netflowBinaryCollectionService/svcExecParams/minThread"/></minimum-thread>
			<maximum-thread><xsl:value-of select="netflowBinaryCollectionService/svcExecParams/maxThread"/></maximum-thread>
			
			<bulk-write-limit><xsl:value-of select="netflowBinaryCollectionService/bulkWriteLimit"/></bulk-write-limit>
			<max-writebuffer-in-mb><xsl:value-of select="netflowBinaryCollectionService/maxWriteBufferSize"/></max-writebuffer-in-mb>
			<max-packet-size-in-byte><xsl:value-of select="netflowBinaryCollectionService/maxPktSize"/></max-packet-size-in-byte>
			<packet-threshold><xsl:value-of select="netflowBinaryCollectionService/packetThreshold"/></packet-threshold>
			<bit-threshold><xsl:value-of select="netflowBinaryCollectionService/bitThreshold"/></bit-threshold>
			<clients>
				<xsl:for-each select="netflowBinaryCollectionService/netFLowClientList">
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
					<is-client-enable>
					<xsl:if test="status = 'ACTIVE'"> <xsl:text>true</xsl:text></xsl:if>
					<xsl:if test="status = 'INACTIVE'"> <xsl:text>false</xsl:text></xsl:if>
					</is-client-enable>
					<abnormal-time-alert><xsl:attribute name="enable">
							<xsl:value-of select="snmpAlertEnable"/>
						</xsl:attribute>
						<time-interval><xsl:value-of select="alertInterval"/></time-interval>
					</abnormal-time-alert>
					</client>
				</xsl:if>
				</xsl:for-each>
			</clients>
			<proxy><xsl:attribute name="enable">
			<xsl:value-of select="netflowBinaryCollectionService/proxyClientEnable"/>
			</xsl:attribute>
					<proxy-service-port>
						<xsl:value-of select="netflowBinaryCollectionService/proxyServicePort"/>
					</proxy-service-port>
					<proxy-list>
						<xsl:for-each select="netflowBinaryCollectionService/natFlowProxyClients">
							<xsl:if test="status != 'DELETED'">
								<proxy-info>
									<proxy-ip>
										<xsl:value-of select="proxyIp"/>
									</proxy-ip>
									<proxy-port>
										<xsl:value-of select="proxyPort"/>
									</proxy-port>
								</proxy-info>
							</xsl:if>
						</xsl:for-each>
					</proxy-list>
			</proxy>
			<parallel-file-write-count><xsl:value-of select="netflowBinaryCollectionService/parallelFileWriteCount"/></parallel-file-write-count>
			<is-tcp-protocol><xsl:value-of select="netflowBinaryCollectionService/isTCPProtocol"/></is-tcp-protocol>
		</natflow-binary-collection-service>
	</xsl:if>
	</xsl:template>
</xsl:stylesheet>