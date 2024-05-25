<?xml version="1.0"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<!--Identity template, provides default behavior that copies all content 
		into the output -->
	<xsl:output method="xml" indent="yes"></xsl:output>
	<xsl:template match="/">
		<xsl:variable name="serviceType" select="*/svctype/alias" />

		<asn1-parser-plugin>
			<xsl:for-each select="*/svcPathList/parserWrappers">

				<xsl:if test="parserType/alias = 'ASN1_PARSING_PLUGIN'">

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
							<record-main-attribute>
								<xsl:value-of select="parserMapping/recMainAttribute" />
							</record-main-attribute>
							<source-date-format>
								<xsl:value-of select="parserMapping/srcDateFormat" />
							</source-date-format>

							<header-attribute-list>
								<xsl:for-each select="parserMapping/parserAttributes">
									<xsl:if test="status != 'DELETED' and attrType ='HEADER'">
										<attribute>
											<unified-field>
												<xsl:value-of select="unifiedField" />
											</unified-field>
											<source-field>
												<xsl:value-of select="sourceField" />
											</source-field>
											<asn1-data-type>
												<xsl:value-of select="ASN1DataType" />
											</asn1-data-type>
											<source-field-format>
												<xsl:value-of select="srcDataFormat" />
											</source-field-format>

											<default-value>
												<xsl:value-of select="defaultValue" />
											</default-value>
											<description>
												<xsl:value-of select="description" />
											</description>
											<trim-chars>
												<xsl:value-of select="trimChars" />
											</trim-chars>
											<trim-position>
												<xsl:value-of select="trimPosition" />
											</trim-position>
											<child-attributes>
												<xsl:value-of select="childAttributes" />
											</child-attributes>
											<record-initializer>
												<xsl:value-of select="recordInitilializer" />
											</record-initializer>
											<unified-field-for-choice-id>
												<xsl:value-of select="unifiedFieldHoldsChoiceId" />
											</unified-field-for-choice-id>
										</attribute>
									</xsl:if>
								</xsl:for-each>

							</header-attribute-list>
							<remove-additional-byte>
								<xsl:attribute name="enabled"><xsl:value-of
								select="parserMapping/removeAddByte"/></xsl:attribute>
								<header-offset><xsl:value-of select="parserMapping/headerOffset" /></header-offset>
								<record-offset><xsl:value-of select="parserMapping/recOffset" /></record-offset>
							</remove-additional-byte>
							
							<remove-variable-length-fillers >
								<xsl:attribute name="enabled"><xsl:value-of
								select="parserMapping/removeFillers" /></xsl:attribute>
								<record-start-ids><xsl:value-of select="parserMapping/recordStartIds" /></record-start-ids>
							</remove-variable-length-fillers>

							<skip-attribute-mapping-and-decode >
							<xsl:attribute name="enabled"><xsl:value-of
								select="parserMapping/skipAttributeMapping" /></xsl:attribute>
								<root-node-name><xsl:value-of select="parserMapping/rootNodeName" /></root-node-name>
								<decode-type><xsl:value-of select="parserMapping/decodeFormat" /></decode-type>
							</skip-attribute-mapping-and-decode>
							<remove-additional-header-footer>
								<xsl:value-of select="parserMapping/removeAddHeaderFooter" />
							</remove-additional-header-footer>
							
							<attribute-list>
								<xsl:for-each select="parserMapping/parserAttributes">
									<xsl:if test="status != 'DELETED' and attrType ='ATTRIBUTE'">
										<attribute>
											<unified-field>
												<xsl:value-of select="unifiedField" />
											</unified-field>
											<source-field>
												<xsl:value-of select="sourceField" />
											</source-field>
											<asn1-data-type>
												<xsl:value-of select="ASN1DataType" />
											</asn1-data-type>
											<source-field-format>
												<xsl:value-of select="srcDataFormat" />
											</source-field-format>

											<default-value>
												<xsl:value-of select="defaultValue" />
											</default-value>
											<description>
												<xsl:value-of select="description" />
											</description>
											<trim-chars>
												<xsl:value-of select="trimChars" />
											</trim-chars>
											<trim-position><xsl:value-of select="trimPosition"/></trim-position>
											<child-attributes>
												<xsl:value-of select="childAttributes" />
											</child-attributes>
											<record-initializer>
												<xsl:value-of select="recordInitilializer" />
											</record-initializer>
											<unified-field-for-choice-id>
												<xsl:value-of select="unifiedFieldHoldsChoiceId" />
											</unified-field-for-choice-id>
										</attribute>
									</xsl:if>
								</xsl:for-each>
							</attribute-list>

							<trailer-attribute-list>
								<xsl:for-each select="parserMapping/parserAttributes">
									<xsl:if test="status != 'DELETED' and attrType ='TRAILER'">
										<attribute>
											<unified-field>
												<xsl:value-of select="unifiedField" />
											</unified-field>
											<source-field>
												<xsl:value-of select="sourceField" />
											</source-field>
											<asn1-data-type>
												<xsl:value-of select="ASN1DataType" />
											</asn1-data-type>
											<source-field-format>
												<xsl:value-of select="srcDataFormat" />
											</source-field-format>

											<default-value>
												<xsl:value-of select="defaultValue" />
											</default-value>
											<description>
												<xsl:value-of select="description" />
											</description>
											<trim-chars>
												<xsl:value-of select="trimChars" />
											</trim-chars>
											<trim-position><xsl:value-of select="trimPosition"/></trim-position>
											<child-attributes>
												<xsl:value-of select="childAttributes" />
											</child-attributes>
											<record-initializer>
												<xsl:value-of select="recordInitilializer" />
											</record-initializer>
											<unified-field-for-choice-id>
												<xsl:value-of select="unifiedFieldHoldsChoiceId" />
											</unified-field-for-choice-id>
										</attribute>
									</xsl:if>
								</xsl:for-each>
							</trailer-attribute-list>
						</instance>
					</xsl:if>
				</xsl:if>
			</xsl:for-each>
		</asn1-parser-plugin>
	</xsl:template>
</xsl:stylesheet>
