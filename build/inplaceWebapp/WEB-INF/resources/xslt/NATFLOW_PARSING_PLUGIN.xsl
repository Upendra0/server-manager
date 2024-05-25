<?xml version="1.0"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="xml" indent="yes"></xsl:output>
	<xsl:template match="/">
	<xsl:variable name="serviceType" select="*/svctype/alias" />
		<natflow-parser-plugin>
			<xsl:for-each
				select="*/svcPathList/parserWrappers">
				
				<xsl:if test="parserType/alias = 'NATFLOW_PARSING_PLUGIN'">
					
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

							<skip-attribute-for-validation>
								<xsl:value-of select="parserMapping/skipAttributeForValidation" />
							</skip-attribute-for-validation>
							<override-template>
								<xsl:value-of select="parserMapping/overrideTemplate" />
							</override-template>
							<enable-default-template>
								<xsl:value-of select="parserMapping/defaultTemplate" />
							</enable-default-template>
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
							
							<filtering-rules>
								<xsl:attribute name="enable">
									<xsl:value-of select="parserMapping/filterEnable" />
								</xsl:attribute>
							
								<filter-protocol>
									<xsl:value-of select="parserMapping/filterProtocol" />
								</filter-protocol>
								<filter-transport>
									<xsl:value-of select="parserMapping/filterTransport" />
								</filter-transport>
								<filter-port>
									<xsl:value-of select="parserMapping/filterPort" />
								</filter-port>
							</filtering-rules>
							
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
											 <source-field-format>
											 <xsl:value-of select="sourceFieldFormat"/>
											 </source-field-format>
											 <destination-field-date-format>
											 <xsl:value-of select="destDateFormat"/>
											 </destination-field-date-format>
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
	
										</attribute>
									</xsl:if>	
								</xsl:for-each>
							</attribute-list>
						</instance>
					</xsl:if>
				</xsl:if>
			</xsl:for-each>
		</natflow-parser-plugin>
	</xsl:template>
</xsl:stylesheet>
