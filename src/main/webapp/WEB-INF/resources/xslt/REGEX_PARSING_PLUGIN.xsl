<?xml version="1.0"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:output method="xml" indent="yes"></xsl:output>
 <xsl:template match="/">
 <xsl:variable name="serviceType" select="*/svctype/alias" />
<regex-parser-plugin>
<xsl:for-each select="*/svcPathList/parserWrappers">
				
	<xsl:if test="parserType/alias = 'REGEX_PARSING_PLUGIN'">
					
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
		<log-pattern-regex><xsl:value-of select="parserMapping/logPatternRegex"/></log-pattern-regex>
		<log-pattern-regexid><xsl:value-of select="parserMapping/logPatternRegexId"/></log-pattern-regexid>
		<pattern-list>
		<xsl:for-each select="parserMapping/patternList">
		<xsl:if test="status != 'DELETED'">
			<pattern>
				<pattern-regexid><xsl:value-of select="patternRegExId"/></pattern-regexid>
				<pattern-regex><xsl:value-of select="patternRegEx"/></pattern-regex>
				<attribute-list>
				<xsl:for-each select="attributeList">
				<xsl:if test="status != 'DELETED'">
				
					<attribute>
						<unified-field><xsl:value-of select="unifiedField"/></unified-field>
						<sequence-no><xsl:value-of select="seqNumber"/></sequence-no>
						<regex>
							<xsl:value-of select="regex"/>
						</regex>
						<default-value><xsl:value-of select="defaultValue"/></default-value>
						<trim-chars><xsl:value-of select="trimChars"/></trim-chars>
						<description><xsl:value-of select="description"/></description>
					</attribute>
					
					</xsl:if>
					</xsl:for-each>
				</attribute-list>
			</pattern>
			</xsl:if>
			</xsl:for-each>
		</pattern-list>
	</instance>
	</xsl:if>
	</xsl:if>
	</xsl:for-each>
</regex-parser-plugin>

    </xsl:template>
</xsl:stylesheet>
