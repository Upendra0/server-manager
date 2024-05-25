<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
                xmlns:fn="http://www.w3.org/2005/xpath-functions" version="1.0">
<xsl:output method="xml" indent="yes"></xsl:output>
 <xsl:template match="/">
	<xsl:variable name="serviceType" select="*/svctype/alias" />
        
<variable-length-binary-parser-plugin>
<xsl:for-each select="*/svcPathList/parserWrappers">

<xsl:if test="parserType/alias = 'VARIABLE_LENGTH_BINARY_PARSING_PLUGIN'">
					
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

		<source-charset-name><xsl:value-of select="parserMapping/srcCharSetName"/></source-charset-name>
		<source-date-format><xsl:value-of select="parserMapping/srcDateFormat"/></source-date-format>
		
		<skip-file-header><xsl:value-of select="parserMapping/skipFileHeader"/></skip-file-header> 
		<file-header-size><xsl:value-of select="parserMapping/fileHeaderSize"/></file-header-size>
		<skip-subfile-header><xsl:value-of select="parserMapping/skipSubFileHeader"/></skip-subfile-header>
		<subfile-header-size><xsl:value-of select="parserMapping/subFileHeaderSize"/></subfile-header-size>
		<sub-file-length><xsl:value-of select="parserMapping/subFileLength"/></sub-file-length> 
		
		<!-- Extraction rule for Cootel_SMG_CDR -->
		<extraction-rule>
			<key><xsl:value-of select="parserMapping/extractionRuleKey"/></key>
			<value><xsl:value-of select="parserMapping/extractionRuleValue"/></value>
		</extraction-rule>
																		
		<record-length-attribute><xsl:value-of select="parserMapping/recordLengthAttribute"/></record-length-attribute>
		
		<header-attribute-list>
			<xsl:for-each select="parserMapping/parserAttributes">
				<xsl:if test="status != 'DELETED' and attrType ='HEADER'">
					<attribute>
						<unified-field><xsl:value-of select="unifiedField"/></unified-field>
						<source-field-format><xsl:value-of select="sourceFieldFormat"/></source-field-format>
						<date-format><xsl:value-of select="dateFormat"/></date-format>
						<description><xsl:value-of select="description"/></description>
						<source-field-name><xsl:value-of select="sourceFieldName" /></source-field-name>
						<start-length><xsl:value-of select="startLength" /></start-length>
						<end-length><xsl:value-of select="endLength" /></end-length>
					</attribute>
				</xsl:if>
			</xsl:for-each>
		</header-attribute-list>
		
		<attribute-list>
			<xsl:for-each select="parserMapping/parserAttributes">
			<xsl:if test="status != 'DELETED' and attrType ='ATTRIBUTE'">
	        <attribute>
	            <unified-field><xsl:value-of select="unifiedField"/></unified-field>
	            <description><xsl:value-of select="description"/></description>
	            <source-field><xsl:value-of select="sourceField"/></source-field>
	            <source-field-name><xsl:value-of select="sourceFieldName"/></source-field-name>                            
	            <default-value><xsl:value-of select="defaultValue"/></default-value>
	            <trim-chars><xsl:value-of select="trimChars"/></trim-chars>
	            <source-field-format><xsl:value-of select="sourceFieldFormat"/></source-field-format>
	            <date-format><xsl:value-of select="dateFormat"/></date-format>
	        	<trim-position><xsl:value-of select="trimPosition"/></trim-position>
				<prefix><xsl:value-of select="prefix"/></prefix>
				<postfix><xsl:value-of select="postfix"/></postfix>
				<length><xsl:value-of select="length"/></length>
				<right-delimiter><xsl:value-of select="rightDelimiter"/></right-delimiter>
	        </attribute> 
	        </xsl:if>
	        </xsl:for-each>       
		</attribute-list>
		<definition-file-path><xsl:value-of select="parserMapping/dataDefinitionPath"/></definition-file-path>
	</instance>
	</xsl:if>
	</xsl:if>
	</xsl:for-each>
	
	</variable-length-binary-parser-plugin>


    </xsl:template>
</xsl:stylesheet>
