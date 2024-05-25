<?xml version="1.0"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<!--Identity template, provides default behavior that copies all content 
		into the output -->
	<xsl:output method="xml" indent="yes"></xsl:output>
	<xsl:template match="/">
		<xsl:variable name="serviceType" select="*/svctype/alias" />

		<xml-parser-plugin>
			<xsl:for-each select="*/svcPathList/parserWrappers">

				<xsl:if test="parserType/alias = 'XML_PARSING_PLUGIN'">

					<xsl:if test="status != 'DELETED'">
						<instance>
							<xsl:attribute name="id">
								<xsl:choose>
									<xsl:when test="$serviceType='IPLOG_PARSING_SERVICE'">
										<xsl:text>000</xsl:text>
									</xsl:when>
									<xsl:otherwise>
										<xsl:value-of select="id" />
									</xsl:otherwise>
								</xsl:choose>
							</xsl:attribute>
							<record-wise-xml-format>
								<xsl:value-of select="parserMapping/recordWiseXmlFormat" />
							</record-wise-xml-format>
							<source-date-format>
								<xsl:value-of select="parserMapping/srcDateFormat" />
							</source-date-format>
							<common-fields>
								<xsl:value-of select="parserMapping/commonFields" />
							</common-fields>
							<attribute-list>
								<xsl:for-each select="parserMapping/parserAttributes">
									<xsl:if test="status != 'DELETED'">
										<attribute>
											<unified-field>
												<xsl:value-of select="unifiedField" />
											</unified-field>
											<source-field>
												<xsl:value-of select="sourceField" />
											</source-field>
											<default-value>
												<xsl:value-of select="defaultValue" />
											</default-value>
											<trim-chars>
												<xsl:value-of select="trimChars" />
											</trim-chars>
											<trim-position><xsl:value-of select="trimPosition"/></trim-position>
											<description>
												<xsl:value-of select="description" />
											</description>
											<source-field-format>
												<xsl:value-of select="sourceFieldFormat" />
											</source-field-format>
										</attribute>
									</xsl:if>
								</xsl:for-each>
							</attribute-list>
						</instance>
					</xsl:if>
				</xsl:if>
			</xsl:for-each>
		</xml-parser-plugin>
	</xsl:template>
</xsl:stylesheet>
