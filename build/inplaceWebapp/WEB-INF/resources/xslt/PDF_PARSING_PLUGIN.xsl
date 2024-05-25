<?xml version="1.0"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<!--Identity template, provides default behavior that copies all content 
		into the output -->
	<xsl:output method="xml" indent="yes"></xsl:output>
	<xsl:template match="/">
		<xsl:variable name="serviceType" select="*/svctype/alias" />

		<pdf-parser-plugin>
			<xsl:for-each select="*/svcPathList/parserWrappers">

				<xsl:if test="parserType/alias = 'PDF_PARSING_PLUGIN'">

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
							<record-wise-pdf-format>
								<xsl:value-of select="parserMapping/recordWisePdfFormat" />
							</record-wise-pdf-format>
							<source-date-format>
								<xsl:value-of select="parserMapping/srcDateFormat" />
							</source-date-format>
							
							<is-file-parsed>
								<xsl:value-of select="parserMapping/fileParsed" />
							</is-file-parsed>
							
							<is-multi-invoice>
								<xsl:value-of select="parserMapping/multiInvoice" />
							</is-multi-invoice>
							
							<pdf-with-multiple-pages>
								<xsl:value-of select="parserMapping/multiPages" />
							</pdf-with-multiple-pages>

							<attribute-list>
								<xsl:for-each select="parserMapping/parserAttributes">
									<xsl:if test="status != 'DELETED' and associatedByGroup = 'false'">
											<xsl:call-template name="ShowAttribute"/>
									</xsl:if>
									
								</xsl:for-each>
								<group-attribute-list>
									<xsl:for-each select="parserMapping/groupAttributeList">
										<xsl:if test="status != 'DELETED'">
											<group-attributes>
												<xsl:call-template name="GroupAttribute">
													<xsl:with-param  name="attributes" select="attributeList"/>
													<xsl:with-param  name="pageList" select="parserPageConfigurationList"/>
												</xsl:call-template>
											</group-attributes>
										</xsl:if>
									</xsl:for-each>
								</group-attribute-list>
								
							</attribute-list>
							
						</instance>
					</xsl:if>
				</xsl:if>
			</xsl:for-each>
		</pdf-parser-plugin>
	</xsl:template>
	
	<xsl:template name="GroupAttribute">
		
		<xsl:param name="attributes"/>
		<xsl:param name="pageList"/>
		
		<group-name><xsl:value-of select="name" /></group-name>
		
		<page-configurations>
			<xsl:for-each select="$pageList">
				<xsl:if test="status != 'DELETED'">
					<xsl:call-template name="ShowPage"/>
				</xsl:if>
			</xsl:for-each>
		</page-configurations>
		
		<table-start-identifier><xsl:value-of select="tableStartIdentifier" /></table-start-identifier>
		<table-start-identifier-col><xsl:value-of select="tableStartIdentifierCol" /></table-start-identifier-col>
		<table-end-identifier><xsl:value-of select="tableEndIdentifier" /></table-end-identifier> 
		<table-end-identifier-col><xsl:value-of select="tableEndIdentifierCol" /></table-end-identifier-col>	
		<table-end-identifier-row-location><xsl:value-of select="tableEndIdentifierRowLocation"/></table-end-identifier-row-location>
		<table-end-identifier-occurrence><xsl:value-of select="tableEndIdentifierOccurence" /></table-end-identifier-occurrence>
		<table-row-identifier><xsl:value-of select="tableRowIdentifier" /></table-row-identifier>
		
		
		
		<xsl:for-each select="$attributes">
			<xsl:if test="status != 'DELETED'">
				<xsl:call-template name="ShowAttributeOfGA"/>
			</xsl:if>
		</xsl:for-each>
		
	</xsl:template>
	
	<xsl:template name="ShowAttribute">
		<attribute>
			<unified-field>
				<xsl:value-of select="unifiedField" />
			</unified-field>
			<source-field>
				<xsl:value-of select="sourceField" />
			</source-field>
			<location>
				<xsl:value-of select="location" />
			</location>	 
			<column-start-location>
				<xsl:value-of select="columnStartLocation" />
			</column-start-location>
			<column-identifier>
				<xsl:value-of select="columnIdentifier" />
			</column-identifier>
			<page-number>
				<xsl:value-of select="pageNumber" />
			</page-number>
			<ref-row>
				<xsl:value-of select="referenceRow" />
			</ref-row>
			
			<col-starts-with>
				<xsl:value-of select="columnStartsWith" />
			</col-starts-with>

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
			<value-separator>
				<xsl:value-of select="valueSeparator" />
			</value-separator>
			<col-ends-with>
				<xsl:value-of select="columnEndsWith" />
			</col-ends-with>
			<mandatory>
				<xsl:value-of select="mandatory" />
			</mandatory>
			<multiple-values>
				<xsl:value-of select="multipleValues" />
			</multiple-values>
		</attribute>
	</xsl:template>
	
	<xsl:template name="ShowAttributeOfGA">
		<attribute>
			<unified-field>
				<xsl:value-of select="unifiedField" />
			</unified-field>
			<source-field>
				<xsl:value-of select="sourceField" />
			</source-field>
			
			<ref-col>
				<xsl:value-of select="referenceCol" />
			</ref-col>
			
			<ref-row>
				<xsl:value-of select="referenceRow" />
			</ref-row>
			
			<col-starts-with>
				<xsl:value-of select="columnStartsWith" />
			</col-starts-with>

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
			
			<is-table-footer>
				<xsl:value-of select="tableFooter" />
			</is-table-footer>
			
			<multiline-attribute>
				<xsl:value-of select="multiLineAttribute" />
			</multiline-attribute>
			
			<row-text-alignment>
				<xsl:value-of select="rowTextAlignment" />
			</row-text-alignment>
			
			<mandatory>
				<xsl:value-of select="mandatory" />
			</mandatory>
		</attribute>
	</xsl:template>
	
	<xsl:template name="ShowPage">
		<page-configuration> 
			<page-number><xsl:value-of select="pageNumber" /></page-number>
			<page-conf-id><xsl:value-of select="id" /></page-conf-id>
			<page-size><xsl:value-of select="pageSize" /></page-size>
			<table-location><xsl:value-of select="tableLocation" /></table-location>
			<table-cols><xsl:value-of select="tableCols" /></table-cols>
			<extraction-method><xsl:value-of select="extractionMethod" /></extraction-method>
		</page-configuration>
	</xsl:template>
	
</xsl:stylesheet>
