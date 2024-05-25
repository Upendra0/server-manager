<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="xml" indent="yes"></xsl:output>
	<xsl:template match="/">
	<xsl:if test="netflowCollectionService/status != 'DELETED'">
		<natflow-collection-service>
			<service-address><xsl:value-of select="netflowCollectionService/serverIp"/>:<xsl:value-of select="netflowCollectionService/netFlowPort"/></service-address>
			<startup-mode><xsl:value-of select="netflowCollectionService/svcExecParams/startupMode"/></startup-mode>
			<queue-size><xsl:value-of select="netflowCollectionService/svcExecParams/queueSize"/></queue-size>
			<minimum-thread><xsl:value-of select="netflowCollectionService/svcExecParams/minThread"/></minimum-thread>
			<maximum-thread><xsl:value-of select="netflowCollectionService/svcExecParams/maxThread"/></maximum-thread>
			<max-idle-communication-time-interval><xsl:value-of select="netflowCollectionService/maxIdelCommuTime"/></max-idle-communication-time-interval>						
			<bulk-write-limit><xsl:value-of select="netflowCollectionService/bulkWriteLimit"/></bulk-write-limit>
			<max-writebuffer-in-mb><xsl:value-of select="netflowCollectionService/maxWriteBufferSize"/></max-writebuffer-in-mb>
			<read-template-initialy><xsl:value-of select="netflowCollectionService/readTemplateOnInit"/></read-template-initialy>
			<max-packet-size-in-byte><xsl:value-of select="netflowCollectionService/maxPktSize"/></max-packet-size-in-byte>
			<option-template-lookup><xsl:attribute name="enabled">
			<xsl:value-of select="netflowCollectionService/optionTemplateEnable"/>
			</xsl:attribute>
					<option-template>
						<option-template-id><xsl:value-of select="netflowCollectionService/optionTemplateId"/></option-template-id>
						<key-field><xsl:value-of select="netflowCollectionService/optionTemplateKey"/></key-field>
						<value-field><xsl:value-of select="netflowCollectionService/optionTemplateValue"/></value-field>
					</option-template>
					<template>
						<template-id><xsl:value-of select="netflowCollectionService/optionCopytoTemplateId"/></template-id>
						<field><xsl:value-of select="netflowCollectionService/optionCopyTofield"/></field>
					</template>
			</option-template-lookup>
			<is-parallel-binary-write-enable><xsl:value-of select="netflowCollectionService/enableParallelBinaryWrite"/></is-parallel-binary-write-enable>
			<clients>
				<xsl:for-each select="netflowCollectionService/netFLowClientList">
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
					<binary-file-location><xsl:value-of select="bkpBinaryfileLocation"/></binary-file-location>
					<abnormal-time-alert><xsl:attribute name="enabled">
							<xsl:value-of select="snmpAlertEnable"/>
						</xsl:attribute>
						<time-interval><xsl:value-of select="alertInterval"/></time-interval>
					</abnormal-time-alert>
					</client>
				</xsl:if>
				</xsl:for-each>
			</clients>
			<proxy><xsl:attribute name="enable">
			<xsl:value-of select="netflowCollectionService/proxyClientEnable"/>
			</xsl:attribute>
					<proxy-service-port>
						<xsl:value-of select="netflowCollectionService/proxyServicePort"/>
					</proxy-service-port>
					<proxy-list>
						<xsl:for-each select="netflowCollectionService/natFlowProxyClients">
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
			<parallel-file-write-count><xsl:value-of select="netflowCollectionService/parallelFileWriteCount"/></parallel-file-write-count>
			<is-tcp-protocol><xsl:value-of select="netflowCollectionService/isTCPProtocol"/></is-tcp-protocol>
		</natflow-collection-service>
	</xsl:if>
	</xsl:template>
</xsl:stylesheet>