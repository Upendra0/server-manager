<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
                xmlns:fn="http://www.w3.org/2005/xpath-functions" version="1.0">
<xsl:output method="xml" indent="yes"></xsl:output>
 <xsl:template match="/">
	<xsl:variable name="serviceType" select="*/svctype/alias" />
        
<fixed-length-ascii-parser-plugin>
<xsl:for-each select="*/svcPathList/parserWrappers">

<xsl:if test="parserType/alias = 'FIXED_LENGTH_ASCII_PARSING_PLUGIN'">

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
	    <file-header><xsl:attribute name="present"><xsl:value-of select="parserMapping/fileHeaderEnable"/></xsl:attribute>
	        <file-header-parser><xsl:text>STANDARD</xsl:text></file-header-parser>
	    </file-header>
	
	    <source-charset-name><xsl:value-of select="parserMapping/srcCharSetName"/></source-charset-name> 
	    
	    <source-date-format><xsl:value-of select="parserMapping/srcDateFormat"/></source-date-format>  
	
	    <attribute-list>
	    <xsl:for-each select="parserMapping/parserAttributes">
			<xsl:if test="status != 'DELETED'">
	    	<attribute>
	    		<unified-field><xsl:value-of select="unifiedField"/></unified-field>
				<start-length><xsl:if test="startLength != '-1'"><xsl:value-of select="startLength"/></xsl:if></start-length>
				<end-length><xsl:if test="endLength != '-1'"><xsl:value-of select="endLength"/></xsl:if></end-length>
				<prefix><xsl:value-of select="prefix"/></prefix>
				<postfix><xsl:value-of select="postfix"/></postfix>
				<default-value><xsl:value-of select="defaultValue"/></default-value>
				<trim-chars><xsl:value-of select="trimChars"/></trim-chars>
				<trim-position><xsl:value-of select="trimPosition"/></trim-position>
	            <description><xsl:value-of select="description"/></description>
				<length><xsl:if test="length != '-1'"><xsl:value-of select="length"/></xsl:if></length>
				<right-delimiter><xsl:value-of select="rightDelimiter"/></right-delimiter>
	    	</attribute>
	    	</xsl:if>
	        </xsl:for-each>      
	    </attribute-list>
   </instance>
   </xsl:if>
	</xsl:if>
	</xsl:for-each>
</fixed-length-ascii-parser-plugin>

    </xsl:template>
</xsl:stylesheet>
