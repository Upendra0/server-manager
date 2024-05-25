<?xml version="1.0"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="xml" indent="yes"></xsl:output>
	<xsl:template match="/">
	<xsl:variable name="serviceType" select="*/svctype/alias" />
		<natflow-asn-parser-plugin>
			<xsl:for-each
				select="*/svcPathList/parserWrappers">
				
				<xsl:if test="parserType/alias = 'NATFLOW_ASN_PARSING_PLUGIN'">
					
					<xsl:if test="status != 'DELETED'">
						<instance>
							<xsl:attribute name="id">
						<xsl:choose>
						<xsl:when test="$serviceType='IPLOG_PARSING_SERVICE'">
							<xsl:text>000</xsl:text>
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="id"/>
						</xsl:otherwise>
						</xsl:choose>
						</xsl:attribute>
							<read-template-initialy>
								<xsl:value-of select="parserMapping/readTemplatesInitially" />
							</read-template-initialy>

							<option-template-lookup>
								<xsl:attribute name="enable">
				<xsl:value-of select="parserMapping/optionTemplateEnable" />
				</xsl:attribute>
								<option-template>
									<option-template-id>
										<xsl:value-of select="parserMapping/optionTemplateId" />
									</option-template-id>
									<key-field>
										<xsl:value-of select="parserMapping/optionTemplateKey" />
									</key-field>
									<value-field>
										<xsl:value-of select="parserMapping/optionTemplateValue" />
									</value-field>
								</option-template>

								<template>
									<template-id>
										<xsl:value-of select="parserMapping/optionCopytoTemplateId" />
									</template-id>
									<field>
										<xsl:value-of select="parserMapping/optionCopyTofield" />
									</field>
								</template>
							</option-template-lookup>
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
											<description>
												<xsl:value-of select="description" />
											</description>
	
										</attribute>
								    </xsl:if>
								</xsl:for-each>
							</attribute-list>
						</instance>
					</xsl:if>
				</xsl:if>
			</xsl:for-each>
		</natflow-asn-parser-plugin>
	</xsl:template>
</xsl:stylesheet>
