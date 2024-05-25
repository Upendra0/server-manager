<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:output method="xml" indent="yes"></xsl:output>
	<xsl:template match="/">
		<kafka-datasources>
			<xsl:for-each select="kafkaDataSourceObjectWrapper/kafkaDataSourceConfigList">
				<xsl:if test="status != 'DELETED'">
				<kafka-datasource>
					<kafka-datasource-name>
						<xsl:value-of select="name"></xsl:value-of>
					</kafka-datasource-name>
					<kafka-bootstrap-servers>
						<xsl:value-of select="kafkaServerIpAddress"></xsl:value-of>:<xsl:value-of select="kafkaServerPort"/>
					</kafka-bootstrap-servers>
			        <max-retry-attempt>
			        	<xsl:value-of select="maxRetryCount"></xsl:value-of>
			        </max-retry-attempt>
			        <max-response-wait>
			        	<xsl:value-of select="maxResponseWait"></xsl:value-of>
			        </max-response-wait>
			        <kafka-producer-properties-retries>	
			        	<xsl:value-of select="kafkaProducerRetryCount"></xsl:value-of>
			        </kafka-producer-properties-retries>
			        <kafka-producer-properties-request-timeout-ms>
			        	<xsl:value-of select="kafkaProducerRequestTimeout"></xsl:value-of>
			        </kafka-producer-properties-request-timeout-ms>
			        <kafka-producer-properties-retry-backoff-ms>
			        	<xsl:value-of select="kafkaProducerRetryBackoff"></xsl:value-of>
			        </kafka-producer-properties-retry-backoff-ms>
			        <kafka-producer-properties-delivery-timeout-ms>
			        	<xsl:value-of select="kafkaProducerDeliveryTimeout"></xsl:value-of>
			        </kafka-producer-properties-delivery-timeout-ms>
				</kafka-datasource>
				</xsl:if>
			</xsl:for-each>
		</kafka-datasources>	
	</xsl:template>
</xsl:stylesheet>