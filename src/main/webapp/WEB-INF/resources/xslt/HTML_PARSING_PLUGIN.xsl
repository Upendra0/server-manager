<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
                xmlns:fn="http://www.w3.org/2005/xpath-functions" version="1.0">
<xsl:output method="xml" indent="yes"></xsl:output>
 <xsl:template match="/">
	<xsl:variable name="serviceType" select="*/svctype/alias" />
        
<html-parser-plugin>
<xsl:for-each select="*/svcPathList/parserWrappers">

<xsl:if test="parserType/alias = 'HTML_PARSING_PLUGIN'">

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
	    
	    <record-wise-excel-format><xsl:value-of select="parserMapping/recordWiseExcelFormat"/></record-wise-excel-format> 
	    
	    <source-date-format><xsl:value-of select="parserMapping/srcDateFormat"/></source-date-format>  	    
	    
	    <is-file-parsed><xsl:value-of select="parserMapping/isFileParsed"/></is-file-parsed>  
		
		<attribute-list>
	   	    <xsl:for-each select="parserMapping/parserAttributes">
			<xsl:if test="status != 'DELETED' and associatedByGroup = 'false'" >
	    	<attribute>
	    	    <source-field><xsl:value-of select="sourceField"/></source-field>
	    		<unified-field><xsl:value-of select="unifiedField"/></unified-field>
				<default-value><xsl:value-of select="defaultValue"/></default-value>
				<field-identifier><xsl:value-of select="fieldIdentifier"/></field-identifier>
				<field-extraction-method><xsl:value-of select="fieldExtractionMethod"/></field-extraction-method>
				<field-section-id><xsl:value-of select="fieldSectionId"/></field-section-id>
				<contains-field-attribute><xsl:value-of select="containsFieldAttribute"/></contains-field-attribute>
				<value-separator><xsl:value-of select="valueSeparator"/></value-separator>
				<value-index><xsl:value-of select="valueIndex"/></value-index>
	    	</attribute>
	    	</xsl:if>
	        </xsl:for-each>  
	        <group-attributes>
	        	<xsl:for-each select="parserMapping/parserAttributes">
				<xsl:if test="status != 'DELETED' and associatedByGroup = 'true'">
					  <attribute>			
					  <source-field><xsl:value-of select="sourceField"/></source-field>
	    			  <unified-field><xsl:value-of select="unifiedField"/></unified-field>
	    			   <td-no><xsl:value-of select="tdNo"/></td-no>
					  <default-value><xsl:value-of select="defaultValue"/></default-value>
					  </attribute>
		        </xsl:if>
	       		</xsl:for-each> 
	       		<table-start-identifier><xsl:value-of select="parserMapping/groupAttributeList/tableStartIdentifier"/></table-start-identifier>
	        	<table-start-identifier-td-num><xsl:value-of select="parserMapping/groupAttributeList/tableStartIdentifierTdNo"/></table-start-identifier-td-num>
	        	<table-end-identifier><xsl:value-of select="parserMapping/groupAttributeList/tableEndIdentifier"/></table-end-identifier>
	     		<table-end-identifier-td-num><xsl:value-of select="parserMapping/groupAttributeList/tableEndIdentifierTdNo"/></table-end-identifier-td-num>	     		 
	       		<table-rows-to-ignore><xsl:value-of select="parserMapping/groupAttributeList/tableRowsToIgnore"/></table-rows-to-ignore>	      		
	        </group-attributes>    
	    </attribute-list>
	  
   </instance>
   </xsl:if>
	</xsl:if>
	</xsl:for-each>
</html-parser-plugin>

    </xsl:template>
</xsl:stylesheet>
