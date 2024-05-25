<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="xml" indent="yes"></xsl:output>
	<xsl:template match="/">
	<xsl:if test="mqttCollectionService/status != 'DELETED'">
		<mqtt-collection-service>
			<service-address><xsl:value-of select="mqttCollectionService/serverIp"/>:<xsl:value-of select="mqttCollectionService/netFlowPort"/></service-address>
			<startup-mode><xsl:value-of select="mqttCollectionService/svcExecParams/startupMode"/></startup-mode>
			<queue-size><xsl:value-of select="mqttCollectionService/svcExecParams/queueSize"/></queue-size>
			<minimum-thread><xsl:value-of select="mqttCollectionService/svcExecParams/minThread"/></minimum-thread>
			<maximum-thread><xsl:value-of select="mqttCollectionService/svcExecParams/maxThread"/></maximum-thread>
			<max-read-rate><xsl:value-of select="mqttCollectionService/maxReadRate"/></max-read-rate>
			<receive-buffer-size><xsl:value-of select="mqttCollectionService/receiverBufferSize"/></receive-buffer-size>
			<connect-attempts-max><xsl:value-of select="mqttCollectionService/connectAttemptsMax"/></connect-attempts-max>
			<reconnect-attempts-max><xsl:value-of select="mqttCollectionService/reconnectAttemptsMax"/></reconnect-attempts-max>
			<reconnect-delay><xsl:value-of select="mqttCollectionService/reconnectDelay"/></reconnect-delay>
			
			<clients>
				<xsl:for-each select="mqttCollectionService/netFLowClientList">
				<xsl:if test="status != 'DELETED'">
					<client>
				    <client-name><xsl:value-of select="name"/></client-name>
					<topic-name><xsl:value-of select="topicName"/></topic-name>
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
					</client>
				</xsl:if>
				</xsl:for-each>
			</clients>
		</mqtt-collection-service>
	</xsl:if>
	</xsl:template>
</xsl:stylesheet>