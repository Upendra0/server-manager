<?xml version="1.0"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="xml" indent="yes"></xsl:output>
	<xsl:template match="/">

		<packet-statistics-agent>
			<xsl:for-each select="serverInstance/agentList">

				<xsl:if test="agentType/alias = 'PACKET_STATISTICS_AGENT'">

					<xsl:if test="status != 'DELETED'">
						<storage-location>
							<xsl:value-of select="storageLocation" />
						</storage-location>
						<execution-interval>
							<xsl:value-of select="executionInterval" />
						</execution-interval>
						<service-list>
							<xsl:for-each select="serviceList">
								<xsl:if test="service/status != 'DELETED'">
									<service>
										<name>
											<xsl:value-of select="service/svctype/alias" />-<xsl:value-of select="service/servInstanceId" />
										</name>
										<enable>
											<xsl:value-of select="enable" />
										</enable>
									</service>
								</xsl:if>
							</xsl:for-each>
						</service-list>

					</xsl:if>
				</xsl:if>
			</xsl:for-each>
		</packet-statistics-agent>
	</xsl:template>

</xsl:stylesheet>
