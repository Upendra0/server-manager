<?xml version="1.0"?>
<xsl:stylesheet version="2.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fn="http://www.w3.org/2005/xpath-functions">
	<xsl:output method="xml" indent="yes"></xsl:output>	
	<xsl:template match="/">
		<policy-conditions>
			<xsl:for-each select="serverInstance/policyConditionList">
				<xsl:if test="status != 'DELETED'">
					<policy-condition>
						<xsl:attribute name="alias">
					<xsl:value-of select="alias" />
				</xsl:attribute>
						<policy-condition-name>
							<xsl:value-of select="name" />
						</policy-condition-name>
						<description>
							<xsl:value-of select="description" />
						</description>
						<unified-field-name>
							<xsl:value-of select="unifiedField" />
						</unified-field-name>
						<operator>
							<xsl:value-of select="operator" />
						</operator>
						<value>
							<xsl:value-of select="value" />
						</value>
						<type>
							<xsl:value-of select="type" />
						</type>
						<expression>
							<xsl:value-of select="conditionExpressionForSync" />
						</expression>						
					</policy-condition>
				</xsl:if>
			</xsl:for-each>
		</policy-conditions>
	</xsl:template>
</xsl:stylesheet>
