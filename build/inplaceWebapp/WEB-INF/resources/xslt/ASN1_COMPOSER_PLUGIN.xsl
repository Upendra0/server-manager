<?xml version="1.0"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fn="http://www.w3.org/2005/xpath-functions">

	<!--Identity template, provides default behavior that copies all content 
		into the output -->
	<xsl:output method="xml" indent="yes"></xsl:output>
	<xsl:template match="/">
		<xsl:variable name="serviceType" select="*/svctype/alias" />

	<asn1-composer-plugin>
		<xsl:for-each select="*/myDrivers/driverPathList/composerWrappers">
		
				<xsl:if test="composerType/alias = 'ASN1_COMPOSER_PLUGIN'">
	
						<xsl:if test="status != 'DELETED'">
							<instance>
								<xsl:attribute name="id">
									<xsl:value-of select="id"/>
								</xsl:attribute>
								<record-main-attribute>
									<xsl:value-of select="composerMapping/recMainAttribute" />
								</record-main-attribute>
								<record-start-format>
									<xsl:value-of select="composerMapping/startFormat" />
								</record-start-format>
								<destination-date-format>
									<xsl:value-of select="composerMapping/destDateFormat" />
								</destination-date-format>
								<destination-file-extension>
									<xsl:value-of select="composerMapping/destFileExt" />
								</destination-file-extension>
								<multi-container-delimiter>
									<xsl:value-of select="composerMapping/multiContainerDelimiter" />
								</multi-container-delimiter>
								<header-attribute-list>
								<xsl:for-each select="composerMapping/attributeList">
									<xsl:if test="status != 'DELETED' and attrType ='HEADER'">
											<attribute>
											<unified-field>
												<xsl:value-of select="unifiedField" />
											</unified-field>
											<destination-field>
												<xsl:value-of select="destinationField" />
											</destination-field>
											<asn1-data-type>
												<xsl:value-of select="asn1DataType" />
											</asn1-data-type>
											<argument-data-type>
												<xsl:value-of select="argumentDataType" />
											</argument-data-type>
											<destination-field-format>
												<xsl:value-of select="destFieldDataFormat" />
											</destination-field-format>
											<default-value>
												<xsl:value-of select="defualtValue" />
											</default-value>
											<trim-chars>
												<xsl:value-of select="trimchars" />
											</trim-chars>
											<description>
												<xsl:value-of select="description" />
											</description>
											<choice-id>
												<xsl:value-of select="choiceId" />
											</choice-id>
											<child-attributes>
												<xsl:value-of select="childAttributes" />
											</child-attributes>
										</attribute>
									</xsl:if>
								</xsl:for-each>	
								</header-attribute-list>
								
								<attribute-list>
									<xsl:for-each select="composerMapping/attributeList">
									<xsl:if test="status != 'DELETED' and attrType ='ATTRIBUTE'">
										<attribute>
											<unified-field>
												<xsl:value-of select="unifiedField" />
											</unified-field>
											<destination-field>
												<xsl:value-of select="destinationField" />
											</destination-field>
											<asn1-data-type>
												<xsl:value-of select="asn1DataType" />
											</asn1-data-type>
											<argument-data-type>
												<xsl:value-of select="argumentDataType" />
											</argument-data-type>
											<destination-field-format>
												<xsl:value-of select="destFieldDataFormat" />
											</destination-field-format>
											<default-value>
												<xsl:value-of select="defualtValue" />
											</default-value>
											<trim-chars>
												<xsl:value-of select="trimchars" />
											</trim-chars>
											<description>
												<xsl:value-of select="description" />
											</description>
											<choice-id>
												<xsl:value-of select="choiceId" />
											</choice-id>
											<child-attributes>
												<xsl:value-of select="childAttributes" />
											</child-attributes>
										</attribute>
									</xsl:if>
									</xsl:for-each>
								</attribute-list>
							   <trailer-attribute-list>
							   	<xsl:for-each select="composerMapping/attributeList">
								<xsl:if test="status != 'DELETED' and attrType ='TRAILER'">
									<attribute>
										<unified-field>
											<xsl:value-of select="unifiedField" />
										</unified-field>
										<destination-field>
											<xsl:value-of select="destinationField" />
										</destination-field>
										<asn1-data-type>
											<xsl:value-of select="asn1DataType" />
										</asn1-data-type>
										<argument-data-type>
											<xsl:value-of select="argumentDataType" />
										</argument-data-type>
										<destination-field-format>
											<xsl:value-of select="destFieldDataFormat" />
										</destination-field-format>
										<default-value>
											<xsl:value-of select="defualtValue" />
										</default-value>
										<trim-chars>
											<xsl:value-of select="trimchars" />
										</trim-chars>
										<description>
											<xsl:value-of select="description" />
										</description>
										<choice-id>
											<xsl:value-of select="choiceId" />
										</choice-id>
										<child-attributes>
											<xsl:value-of select="childAttributes" />
										</child-attributes>
									</attribute>
								</xsl:if>
							</xsl:for-each>
						</trailer-attribute-list>
					</instance>
				</xsl:if>
			</xsl:if>
		</xsl:for-each>    
	</asn1-composer-plugin>
</xsl:template>
</xsl:stylesheet>