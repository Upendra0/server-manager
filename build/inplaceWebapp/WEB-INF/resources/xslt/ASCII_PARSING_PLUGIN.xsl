<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
                xmlns:fn="http://www.w3.org/2005/xpath-functions" version="1.0">
<xsl:output method="xml" indent="yes"></xsl:output>
 <xsl:template match="/">
	<xsl:variable name="serviceType" select="*/svctype/alias" />
        
<ascii-parser-plugin>
<xsl:for-each select="*/svcPathList/parserWrappers">

<xsl:if test="parserType/alias = 'ASCII_PARSING_PLUGIN'">
					
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
		
		<key-value-record><xsl:value-of select="parserMapping/keyValueRecordEnable"/></key-value-record> 
		
		<key-value-separator><xsl:value-of select="parserMapping/keyValueSeparator"/></key-value-separator> 
		
		<field-separator><xsl:value-of select="parserMapping/fieldSeparator"/></field-separator>
		
		<!-- MEDSUP-1878
		<xsl:choose>
		 	<xsl:when test="parserMapping/fieldSeparator = '|'">
		 	<field-separator>\|</field-separator>
 			</xsl:when> 
			<xsl:otherwise><field-separator><xsl:value-of select="parserMapping/fieldSeparator"/></field-separator></xsl:otherwise>
		</xsl:choose>
		-->
		<xsl:choose>
		 	<xsl:when test="parserMapping/find != ''">
 				<find-and-replace>
					<xsl:variable name="findValue" select="parserMapping/find"/>
					<!-- 
						MEDSUP-934
						<xsl:value-of select="replace($findValue , '[s]', ' ')"/> 
					-->
					<xsl:value-of select="parserMapping/find"/>					
					<xsl:text>,</xsl:text>
					<xsl:variable name="replaceValue" select="parserMapping/replace"/>			
					<!-- MEDSUP-934 
						<xsl:value-of select="replace($replaceValue , '[s]', ' ')" /> 
					-->
					<xsl:value-of select="parserMapping/replace" />
				</find-and-replace>
 			</xsl:when> 
			<xsl:otherwise><find-and-replace /></xsl:otherwise>
		</xsl:choose>
		
		<linear-key-value-record><xsl:value-of select="parserMapping/linearKeyValueRecordEnable"></xsl:value-of></linear-key-value-record>
		<record-header-identifier><xsl:value-of select="parserMapping/recordHeaderIdentifier"></xsl:value-of></record-header-identifier>
		<exclude-characters-within-index>
		<xsl:value-of select="parserMapping/excludeCharactersMin"></xsl:value-of>
		<xsl:text>,</xsl:text>
		<xsl:value-of select="parserMapping/excludeCharactersMax"></xsl:value-of>
		</exclude-characters-within-index>
		<exclude-lines-starting-with><xsl:value-of select="parserMapping/excludeLinesStart"></xsl:value-of></exclude-lines-starting-with>
		
		
		<record-header> <xsl:attribute name="present"><xsl:value-of select="parserMapping/recordHeaderEnable"/></xsl:attribute>
			<record-header-separator><xsl:value-of select="parserMapping/recordHeaderSeparator"/></record-header-separator> 
			<record-header-length><xsl:value-of select="parserMapping/recordHeaderLength"/></record-header-length> 
		</record-header>
		
		<file-header> <xsl:attribute name="present"><xsl:value-of select="parserMapping/fileHeaderEnable"/></xsl:attribute>
			<file-header-parser><xsl:value-of select="parserMapping/fileHeaderParser"/></file-header-parser> 
			<contains-fields><xsl:value-of select="parserMapping/fileHeaderContainsFields"/></contains-fields> 
		</file-header>
		
		<file-footer> <xsl:attribute name="present"><xsl:value-of select="parserMapping/fileFooterEnable"/></xsl:attribute>
			<file-footer-parser><xsl:value-of select="parserMapping/fileFooterParser"/></file-footer-parser>
			<file-footer-contains><xsl:value-of select="parserMapping/fileFooterContains"/></file-footer-contains>
		</file-footer>
		
		<attribute-list>
			
			<xsl:for-each select="parserMapping/parserAttributes">
			<xsl:if test="status != 'DELETED'">
	        <attribute>
	            <unified-field><xsl:value-of select="unifiedField"/></unified-field>
	            <description><xsl:value-of select="description"/></description>
	            <source-field><xsl:value-of select="sourceField"/></source-field>                            
	            <default-value><xsl:value-of select="defaultValue"/></default-value>
	            <trim-chars><xsl:value-of disable-output-escaping="yes" select="trimChars"/></trim-chars>
	            <source-field-format><xsl:value-of select="sourceFieldFormat"/></source-field-format>
	             <date-format><xsl:value-of select="dateFormat"/></date-format>
	        	<trim-position><xsl:value-of select="trimPosition"/></trim-position>
	        	<split-ip-port><xsl:attribute name="seperator"><xsl:value-of
					select="ipPortSeperator" /></xsl:attribute>
					<xsl:value-of select="portUnifiedField"/>
				</split-ip-port>
	        </attribute> 
	        </xsl:if>
	        </xsl:for-each>       
		</attribute-list>
	</instance>
	</xsl:if>
	</xsl:if>
	</xsl:for-each>
	
	</ascii-parser-plugin>


    </xsl:template>
</xsl:stylesheet>
