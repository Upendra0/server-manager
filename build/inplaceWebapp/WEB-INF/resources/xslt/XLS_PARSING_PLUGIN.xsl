<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:fn="http://www.w3.org/2005/xpath-functions" version="1.0">
	<xsl:output method="xml" indent="yes"></xsl:output>
	<xsl:template match="/">
		<xsl:variable name="serviceType" select="*/svctype/alias" />

		<excel-parser-plugin>
			<xsl:for-each select="*/svcPathList/parserWrappers">

				<xsl:if test="parserType/alias = 'XLS_PARSING_PLUGIN'">

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

							<record-wise-excel-format>
								<xsl:value-of select="parserMapping/recordWiseExcelFormat" />
							</record-wise-excel-format>

							<source-date-format>
								<xsl:value-of select="parserMapping/srcDateFormat" />
							</source-date-format>

							<is-file-parsed>
								<xsl:value-of select="parserMapping/isFileParsed" />
							</is-file-parsed>

							<attribute-list>
								<xsl:for-each select="parserMapping/parserAttributes">
									<xsl:if test="status != 'DELETED' and associatedByGroup != 'true'">
										<attribute>
											<source-field>
												<xsl:value-of select="sourceField" />
											</source-field>
											<unified-field>
												<xsl:value-of select="unifiedField" />
											</unified-field>
											<default-value>
												<xsl:value-of select="defaultValue" />
											</default-value>
											<trim-chars>
												<xsl:value-of select="trimChars" />
											</trim-chars>
											<trim-position>
												<xsl:value-of select="trimPosition" />
											</trim-position>
											<starts-with>
												<xsl:value-of select="startsWith" />
											</starts-with>
											<is-table-footer>
												<xsl:value-of select="tableFooter" />
											</is-table-footer>
											<excel-row>
												<xsl:value-of select="excelRow" />
											</excel-row>
											<excel-col>
												<xsl:value-of select="excelCol" />
											</excel-col>
											<relative-excel-row>
												<xsl:value-of select="relativeExcelRow" />
											</relative-excel-row>
											<is-table-row-attribute>
												<xsl:value-of select="tableRowAttribute" />
											</is-table-row-attribute>
											<col-starts-with>
												<xsl:value-of select="columnStartsWith" />
											</col-starts-with>
											<col-contains>
												<xsl:value-of select="columnContains" />
											</col-contains>
										</attribute>
									</xsl:if>
								</xsl:for-each>
								<group-attributes>
									<xsl:for-each select="parserMapping/parserAttributes">
										<xsl:if test="status != 'DELETED' and associatedByGroup = 'true'">
											<attribute>
												<source-field>
													<xsl:value-of select="sourceField" />
												</source-field>
												<unified-field>
													<xsl:value-of select="unifiedField" />
												</unified-field>
												<default-value>
													<xsl:value-of select="defaultValue" />
												</default-value>
												<trim-chars>
													<xsl:value-of select="trimChars" />
												</trim-chars>
												<trim-position>
													<xsl:value-of select="trimPosition" />
												</trim-position>
												<starts-with>
													<xsl:value-of select="startsWith" />
												</starts-with>
												<is-table-footer>
													<xsl:value-of select="tableFooter" />
												</is-table-footer>
												<excel-row>
													<xsl:value-of select="excelRow" />
												</excel-row>
												<excel-col>
													<xsl:value-of select="excelCol" />
												</excel-col>
												<relative-excel-row>
													<xsl:value-of select="relativeExcelRow" />
												</relative-excel-row>
												<is-table-row-attribute>
												<xsl:value-of select="tableRowAttribute" />
													</is-table-row-attribute>
												<col-starts-with>
													<xsl:value-of select="columnStartsWith" />
												</col-starts-with>
												<col-contains>
													<xsl:value-of select="columnContains" />
												</col-contains>
											</attribute>
										</xsl:if>
									</xsl:for-each>
									<table-start-identifier>
										<xsl:value-of
											select="parserMapping/groupAttributeList/tableStartIdentifier" />
									</table-start-identifier>
									<table-end-identifier>
										<xsl:value-of
											select="parserMapping/groupAttributeList/tableEndIdentifier" />
									</table-end-identifier>
									<table-start-identifier-col>
										<xsl:value-of
											select="parserMapping/groupAttributeList/tableStartIdentifierCol" />
									</table-start-identifier-col>
									<table-end-identifier-col>
										<xsl:value-of
											select="parserMapping/groupAttributeList/tableEndIdentifierCol" />
									</table-end-identifier-col>
								</group-attributes>
							</attribute-list>


						</instance>
					</xsl:if>
				</xsl:if>
			</xsl:for-each>
		</excel-parser-plugin>

	</xsl:template>
</xsl:stylesheet>
