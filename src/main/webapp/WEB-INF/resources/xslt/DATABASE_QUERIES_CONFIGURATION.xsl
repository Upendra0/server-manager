<?xml version="1.0"?>
<xsl:stylesheet version="1.0" 
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
	xmlns:fn="http://www.w3.org/2005/xpath-functions">
	<xsl:output method="xml" indent="yes"></xsl:output>
	<xsl:template match="/">
	
	<database-queries>
		<xsl:for-each select="serverInstance/databaseQueryList">
			<xsl:if test="status != 'DELETED'">
			<database-query>
				<xsl:attribute name="alias">
					<xsl:value-of select="alias"/>
				</xsl:attribute>
				<database-query-name><xsl:value-of select="queryName"/></database-query-name>
				<database-query-value><xsl:value-of select="queryValue"/></database-query-value>
				<return-multiple-rows><xsl:value-of select="returnMultipleRowsEnable"/></return-multiple-rows>
				<cache-enable><xsl:value-of select="cacheEnable"/></cache-enable>
				<logical-opearator><xsl:value-of select="logicalOperator"/></logical-opearator>
				
				<condition-expression-enable><xsl:value-of select="conditionExpressionEnable"/></condition-expression-enable>
				<condition-expression><xsl:value-of select="conditionExpression"/></condition-expression>
				
				<condition-list>
					<xsl:for-each select="databaseQueryConditions">
						<xsl:if test="status != 'DELETED'">
								<condition>
									<check-db-field-name><xsl:value-of select="databaseFieldName"/></check-db-field-name>
									<check-operator><xsl:value-of select="policyConditionOperatorEnum"/></check-operator>
									<chack-unified-field-name><xsl:value-of select="unifiedField"/></chack-unified-field-name>
									<check-db-key><xsl:value-of select="databaseKey"/></check-db-key>
								</condition>
						</xsl:if>
					</xsl:for-each>
				</condition-list>
				
				<action-list>
					<xsl:for-each select="databaseQueryActions">
						<xsl:if test="status != 'DELETED'">
							<action>
								<db-field-name><xsl:value-of select="databaseFieldName"/></db-field-name>
								<unified-field-name><xsl:value-of select="unifiedField"/></unified-field-name>
							</action>
						</xsl:if>
					</xsl:for-each>
				</action-list>
				
				<output-db-field-name><xsl:value-of select="outputDbField"/></output-db-field-name>
				<description><xsl:value-of select="description"/></description>
			</database-query>
			
			</xsl:if>
		</xsl:for-each>
	</database-queries>
	</xsl:template>
</xsl:stylesheet>
