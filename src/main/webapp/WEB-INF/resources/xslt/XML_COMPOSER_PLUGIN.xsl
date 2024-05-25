<?xml version="1.0"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fn="http://www.w3.org/2005/xpath-functions">
	
	<xsl:output method="xml" indent="yes"></xsl:output>
	<xsl:template match="/">
		<xml-composer-plugin>
			<xsl:for-each select="*/myDrivers/driverPathList/composerWrappers">
				<xsl:if test="composerType/alias = 'XML_COMPOSER_PLUGIN'">
					<xsl:if test="status != 'DELETED'">
						<instance><xsl:attribute name="id"><xsl:value-of select="id"/></xsl:attribute>
						<destination-charset-name><xsl:value-of select="composerMapping/destCharset"/></destination-charset-name>
						<destination-date-format><xsl:value-of select="composerMapping/destDateFormat"/></destination-date-format>
						<destination-file-extension><xsl:value-of select="composerMapping/destFileExt"/></destination-file-extension>
						<attribute-list>
							<xsl:for-each select="composerMapping/attributeList">
							<xsl:if test="status != 'DELETED'">
							<attribute>
							   <sequence-number><xsl:value-of select="sequenceNumber"/></sequence-number>
								<destination-field><xsl:value-of select="destinationField"/></destination-field>
								<unified-field><xsl:value-of select="unifiedField"/></unified-field>
								<description><xsl:value-of select="description"/></description>
								<data-type><xsl:value-of select="dataType"/></data-type>
							</attribute>	
							</xsl:if>
							</xsl:for-each>
						</attribute-list> 
						</instance>
					</xsl:if>
				</xsl:if>
			</xsl:for-each>
		</xml-composer-plugin>
	</xsl:template>
</xsl:stylesheet>
	