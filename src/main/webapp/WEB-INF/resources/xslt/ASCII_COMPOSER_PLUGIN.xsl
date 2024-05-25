<?xml version="1.0"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fn="http://www.w3.org/2005/xpath-functions">
	
	<xsl:output method="xml" indent="yes"></xsl:output>
	<xsl:template match="/">
		<ascii-composer-plugin>
			<xsl:for-each select="*/myDrivers/driverPathList/composerWrappers">
				<xsl:if test="composerType/alias = 'ASCII_COMPOSER_PLUGIN'">
					<xsl:if test="status != 'DELETED'">
						<instance><xsl:attribute name="id"><xsl:value-of select="id"/></xsl:attribute>
						 <file-header>
						 	<xsl:attribute name="present"><xsl:value-of select="composerMapping/fileHeaderEnable"/></xsl:attribute>
						 	<file-header-parser><xsl:value-of select="composerMapping/fileHeaderParser"/></file-header-parser>
							<contains-fields><xsl:value-of select="composerMapping/fileHeaderContainsFields"/></contains-fields>
						</file-header>
						
						 <file-footer>
						 	<xsl:attribute name="present"><xsl:value-of select="composerMapping/fileFooterEnable"/></xsl:attribute>
						</file-footer>
						
						<header-summary> <xsl:attribute name="present"><xsl:value-of select="composerMapping/fileHeaderSummaryEnable"/></xsl:attribute>
							<xsl:value-of select="composerMapping/fileHeaderSummary"/>
						</header-summary>
						
						<footer-summary> <xsl:attribute name="present"><xsl:value-of select="composerMapping/fileFooterSummaryEnable"/></xsl:attribute>
							<xsl:value-of select="composerMapping/fileFooterSummary"/>
						</footer-summary>
						
						<destination-charset-name><xsl:value-of select="composerMapping/destCharset"/></destination-charset-name>
						<destination-date-format><xsl:value-of select="composerMapping/destDateFormat"/></destination-date-format>
						<field-separator><xsl:value-of select="composerMapping/fieldSeparator"/></field-separator>
						<destination-file-extension><xsl:value-of select="composerMapping/destFileExt"/></destination-file-extension>
						<attribute-list>
							<xsl:for-each select="composerMapping/attributeList">
							<xsl:if test="status != 'DELETED'">
							<attribute>
								<description><xsl:value-of select="description"/></description>
								<sequence-number><xsl:value-of select="sequenceNumber"/></sequence-number>
								<destination-field><xsl:value-of select="destinationField"/></destination-field>
								<unified-field><xsl:value-of select="unifiedField"/></unified-field>
								<replace-condition-list><xsl:value-of select="replaceConditionList"/></replace-condition-list>
								<default-value><xsl:value-of select="defualtValue"/></default-value>
								<date-format><xsl:value-of select="dateFormat"/></date-format>
								<data-type><xsl:value-of select="dataType"/></data-type>
								<trim-chars><xsl:value-of select="trimchars"/></trim-chars>
								<trim-position><xsl:value-of select="trimPosition"/></trim-position>
								<padding><xsl:attribute name="enable"><xsl:value-of select="paddingEnable"/></xsl:attribute>
									<length><xsl:value-of select="length"/></length>
									<padding-type><xsl:value-of select="paddingType"/></padding-type>
									<padding-char><xsl:value-of select="paddingChar"/></padding-char>
									<prefix><xsl:value-of select="prefix"/></prefix>
									<suffix><xsl:value-of select="suffix"/></suffix>
								</padding>
							</attribute>	
							</xsl:if>
							</xsl:for-each>
						</attribute-list> 
						</instance>
					</xsl:if>
				</xsl:if>
			</xsl:for-each>
		</ascii-composer-plugin>
	</xsl:template>
</xsl:stylesheet>
	