<?xml version="1.0"?>
<xsl:stylesheet
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:fn="http://www.w3.org/2005/xpath-functions" version="1.0">
	<xsl:output method="xml" indent="yes"></xsl:output>
	<xsl:template match="/">
		<xsl:variable name="serviceType" select="*/svctype/alias" />

		<mt-siemens-binary-parser-plugin>
			<xsl:for-each select="*/svcPathList/parserWrappers">

				<xsl:if test="parserType/alias = 'MTSIEMENS_BINARY_PARSING_PLUGIN'">

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
							<source-charset-name>
								<xsl:value-of select="parserMapping/srcCharSetName" />
							</source-charset-name>

							<source-date-format>
								<xsl:value-of select="parserMapping/srcDateFormat" />
							</source-date-format>

							
							<attribute-list>

								<xsl:for-each
									select="parserMapping/parserAttributes">
									<xsl:if test="status != 'DELETED'">
										<attribute>
											<unified-field>
												<xsl:value-of select="unifiedField" />
											</unified-field>
											<description>
												<xsl:value-of select="description" />
											</description>
											<source-field>
												<xsl:value-of select="sourceField" />
											</source-field>
											<default-value>
												<xsl:value-of select="defaultValue" />
											</default-value>
											<trim-chars>
												<xsl:value-of disable-output-escaping="yes"
													select="trimChars" />
											</trim-chars>
										</attribute>
									</xsl:if>
								</xsl:for-each>
							</attribute-list>
						</instance>
					</xsl:if>
				</xsl:if>
			</xsl:for-each>

		</mt-siemens-binary-parser-plugin>


	</xsl:template>
</xsl:stylesheet>
