<?xml version="1.0"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fn="http://www.w3.org/2005/xpath-functions">
	
	<xsl:output method="xml" indent="yes"></xsl:output>
	<xsl:template match="/">
	<fixed-length-ascii-composer-plugin>
		<xsl:for-each select="*/myDrivers/driverPathList/composerWrappers">
			<xsl:if test="composerType/alias = 'FIXED_LENGTH_ASCII_COMPOSER_PLUGIN'">
				<xsl:if test="status != 'DELETED'">
					<instance><xsl:attribute name="id"><xsl:value-of select="id"/></xsl:attribute>
						<header-summary> <xsl:attribute name="present"><xsl:value-of select="composerMapping/fileHeaderSummaryEnable"/></xsl:attribute>
							<xsl:value-of select="composerMapping/fileHeaderSummary"/>
						</header-summary>
						
						<footer-summary> <xsl:attribute name="present"><xsl:value-of select="composerMapping/fileFooterSummaryEnable"/></xsl:attribute>
							<xsl:value-of select="composerMapping/fileFooterSummary"/>
						</footer-summary>
   						<destination-charset-name><xsl:value-of select="composerMapping/destCharset"/></destination-charset-name>
						<destination-date-format><xsl:value-of select="composerMapping/destDateFormat"/></destination-date-format>
						<field-seperator><xsl:value-of select="composerMapping/fieldSeparator"/></field-seperator>
						<destination-file-extension><xsl:value-of select="composerMapping/destFileExt"/></destination-file-extension>
					    <attribute-list>
					    	<xsl:for-each select="composerMapping/attributeList">
							<xsl:if test="status != 'DELETED'">
					    	<attribute>
								<sequence-number><xsl:value-of select="sequenceNumber"/></sequence-number>
								<length><xsl:value-of select="fixedLength"/></length>
								<prefix><xsl:value-of select="prefix"/></prefix>
								<postfix><xsl:value-of select="suffix"/></postfix>
					    		<unified-field><xsl:value-of select="unifiedField"/></unified-field>
								<default-value><xsl:value-of select="defualtValue"/></default-value>
								<trim-chars><xsl:value-of select="trimchars"/></trim-chars>
					           	<description><xsl:value-of select="description"/></description>
								<data-type><xsl:value-of select="dataType"/></data-type>
								<date-format><xsl:value-of select="fixedLengthDateFormat"/></date-format>
								<padding><xsl:attribute name="type"><xsl:value-of select="paddingType"/></xsl:attribute>
								<xsl:attribute name="value"><xsl:value-of select="paddingChar"/></xsl:attribute></padding>
					    	</attribute>
							</xsl:if>
							</xsl:for-each>
						</attribute-list>
					</instance>
					</xsl:if>
				</xsl:if>
			</xsl:for-each>
		</fixed-length-ascii-composer-plugin>
	</xsl:template>
</xsl:stylesheet>
