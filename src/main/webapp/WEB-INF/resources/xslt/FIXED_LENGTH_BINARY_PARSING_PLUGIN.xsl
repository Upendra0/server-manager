<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
                xmlns:fn="http://www.w3.org/2005/xpath-functions" version="1.0">
<xsl:output method="xml" indent="yes"></xsl:output>
 <xsl:template match="/">
	<xsl:variable name="serviceType" select="*/svctype/alias" />
        
<fixed-length-binary-parser-plugin>
<xsl:for-each select="*/svcPathList/parserWrappers">

<xsl:if test="parserType/alias = 'FIXED_LENGTH_BINARY_PARSING_PLUGIN'">

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
		
		<record-length><xsl:value-of select="parserMapping/recordLength"/></record-length>
		
	    <attribute-list>
	    <xsl:for-each select="parserMapping/parserAttributes">
			<xsl:if test="status != 'DELETED'">
	    	<attribute>
	    		<unified-field><xsl:value-of select="unifiedField"/></unified-field>
				<start-length><xsl:value-of select="startLength"/></start-length>
				<end-length><xsl:value-of select="endLength"/></end-length>
				
				<read-as-bits><xsl:value-of select="readAsBits"/></read-as-bits>
				<bit-start-length><xsl:if test="bitStartLength != '-1'"><xsl:value-of select="bitStartLength"/></xsl:if></bit-start-length>
				<bit-end-length><xsl:if test="bitEndLength != '-1'"><xsl:value-of select="bitEndLength"/></xsl:if></bit-end-length>
				
				<prefix><xsl:value-of select="prefix"/></prefix>
				<postfix><xsl:value-of select="postfix"/></postfix>
				<default-value><xsl:value-of select="defaultValue"/></default-value>
				<trim-chars><xsl:value-of select="trimChars"/></trim-chars>
				<trim-position><xsl:value-of select="trimPosition"/></trim-position>
				<source-field-format><xsl:value-of select="sourceFieldFormat"/></source-field-format>
	            <description><xsl:value-of select="description"/></description>
	            <length><xsl:if test="length != '-1'"><xsl:value-of select="length"/></xsl:if></length>
	    		<multi-record><xsl:value-of select="multiRecord"/></multi-record>
	    	</attribute>
	    	</xsl:if>
	        </xsl:for-each>      
	    </attribute-list>
   </instance>
   </xsl:if>
	</xsl:if>
	</xsl:for-each>
</fixed-length-binary-parser-plugin>

    </xsl:template>
</xsl:stylesheet>
