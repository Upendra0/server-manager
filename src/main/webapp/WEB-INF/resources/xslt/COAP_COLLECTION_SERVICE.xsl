<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="xml" indent="yes"></xsl:output>
	<xsl:template match="/">
	<xsl:if test="coAPCollectionService/status != 'DELETED'">
		<coap-collection-service>
			<service-address><xsl:value-of select="coAPCollectionService/serverIp"/>:<xsl:value-of select="coAPCollectionService/netFlowPort"/></service-address>
			<startup-mode><xsl:value-of select="coAPCollectionService/svcExecParams/startupMode"/></startup-mode>
			<queue-size><xsl:value-of select="coAPCollectionService/svcExecParams/queueSize"/></queue-size>
			<minimum-thread><xsl:value-of select="coAPCollectionService/svcExecParams/minThread"/></minimum-thread>
			<maximum-thread><xsl:value-of select="coAPCollectionService/svcExecParams/maxThread"/></maximum-thread>
			<max-idle-communication-time-interval><xsl:value-of select="coAPCollectionService/maxIdelCommuTime"/></max-idle-communication-time-interval>
			<newline-character-availability><xsl:value-of select="coAPCollectionService/newLineCharAvailable"/></newline-character-availability>
			<bulk-write-limit><xsl:value-of select="coAPCollectionService/bulkWriteLimit"/></bulk-write-limit>
			<max-writebuffer-in-mb><xsl:value-of select="coAPCollectionService/maxWriteBufferSize"/></max-writebuffer-in-mb>
			<clients>
				<xsl:for-each select="coAPCollectionService/netFLowClientList">
				<xsl:if test="status != 'DELETED'">
				<client>
					<client-name><xsl:value-of select="name"/></client-name>
				    <server-ip><xsl:value-of select="clientIpAddress"/></server-ip>
					<server-port><xsl:if test="clientPort != -1"><xsl:value-of select="clientPort"/></xsl:if></server-port>
					<resource-name><xsl:value-of select="resourcesName"/></resource-name>
					<register-observer><xsl:value-of select="registerObserver"/></register-observer>
					<observer-cancel><xsl:value-of select="observerTimeout"/></observer-cancel>
					<request-type><xsl:value-of select="requestType"/></request-type>
					<message-type><xsl:value-of select="messageType"/></message-type>
					<req-timeout><xsl:value-of select="requestTimeout"/></req-timeout>
					<req-retrive-count><xsl:value-of select="requestRetryCount"/></req-retrive-count>
					<req-execution-interval><xsl:value-of select="reqExecutionInterval"/></req-execution-interval>
					<req-execution-frequency><xsl:value-of select="reqExecutionFreq"/></req-execution-frequency>
					<exchange-life-time><xsl:value-of select="exchangeLifeTime"/></exchange-life-time>
					<is-security-enable><xsl:value-of select="enableSecurity"/></is-security-enable>
	                <security-type><xsl:value-of select="securityType"/></security-type>
	                <security-identity><xsl:value-of select="securityIdentity"/></security-identity>
	                <secrete-key><xsl:value-of select="securityKey"/></secrete-key>   
	                <security-certificate-location><xsl:value-of select="secCerLocation"/></security-certificate-location>
	                <security-certificate-password><xsl:value-of select="secCerPasswd"/></security-certificate-password>
	                <is-proxy-enable><xsl:value-of select="enableProxy"/></is-proxy-enable>
	                <proxy-server-ip><xsl:value-of select="proxyServerIp"/></proxy-server-ip>
	                <proxy-server-port><xsl:if test="proxyServerPort != -1"><xsl:value-of select="proxyServerPort"/></xsl:if></proxy-server-port>
	    		    <proxy-resource-name><xsl:value-of select="proxyResources"/></proxy-resource-name>	
	                <proxy-schema><xsl:value-of select="proxySchema"/></proxy-schema>
					<kafka-enable><xsl:value-of select="enableKafka"/></kafka-enable>
					<topic-name><xsl:value-of select="topicName"/></topic-name>
					<kafka-datasource><xsl:if test="enableKafka = 'true'"><xsl:value-of select="kafkaDataSourceConfig/name"/></xsl:if></kafka-datasource>
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
		</coap-collection-service>
	</xsl:if>
	</xsl:template>
</xsl:stylesheet>