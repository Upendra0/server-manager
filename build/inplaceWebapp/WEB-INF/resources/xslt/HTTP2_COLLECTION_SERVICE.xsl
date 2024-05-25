<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="xml" indent="yes"></xsl:output>
	<xsl:template match="/">
	<xsl:if test="http2CollectionService/status != 'DELETED'">
		<http2-collection-service>
			<service-address><xsl:value-of select="http2CollectionService/serverIp"/>:<xsl:value-of select="http2CollectionService/netFlowPort"/></service-address>
			<startup-mode><xsl:value-of select="http2CollectionService/svcExecParams/startupMode"/></startup-mode>
			<queue-size><xsl:value-of select="http2CollectionService/svcExecParams/queueSize"/></queue-size>
			<minimum-thread><xsl:value-of select="http2CollectionService/svcExecParams/minThread"/></minimum-thread>
			<maximum-thread><xsl:value-of select="http2CollectionService/svcExecParams/maxThread"/></maximum-thread>
			<bulk-write-limit><xsl:value-of select="http2CollectionService/bulkWriteLimit"/></bulk-write-limit>
			<max-writebuffer-in-mb><xsl:value-of select="http2CollectionService/maxWriteBufferSize"/></max-writebuffer-in-mb>
			<max-packet-size-in-byte><xsl:value-of select="http2CollectionService/maxPktSize"/></max-packet-size-in-byte>
			<parallel-file-write-count><xsl:value-of select="http2CollectionService/parallelFileWriteCount"/></parallel-file-write-count>
			<secure-scheme><xsl:value-of select="http2CollectionService/secureScheme"/></secure-scheme>
			<encription><xsl:value-of select="http2CollectionService/encryption"/></encription>
			<keystore-resource><xsl:value-of select="http2CollectionService/keystoreFilePath"/></keystore-resource>
			<keystore-password><xsl:value-of select="http2CollectionService/keystorePassword"/></keystore-password>
			<keymanager-password><xsl:value-of select="http2CollectionService/keymanagerPassword"/></keymanager-password>
			
			<clients>
				<xsl:for-each select="http2CollectionService/netFLowClientList">
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
						<content-type><xsl:value-of select="contentType"/></content-type>
						<json-validation><xsl:value-of select="jsonValidate"/></json-validation>
						<uri><xsl:value-of select="uri"/></uri>
					</client>
				</xsl:if>
				</xsl:for-each>
			</clients>
		</http2-collection-service>
	</xsl:if>
	</xsl:template>
</xsl:stylesheet>