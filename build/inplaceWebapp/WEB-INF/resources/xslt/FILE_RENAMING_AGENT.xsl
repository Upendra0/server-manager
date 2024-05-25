<?xml version="1.0"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:output method="xml" indent="yes"></xsl:output>
	<xsl:template match="/">
	
		<file-rename-agent>
		<xsl:choose><xsl:when test="serverInstance/status = 'INACTIVE'">false</xsl:when>
					<xsl:otherwise>true</xsl:otherwise>
					</xsl:choose>
		<xsl:for-each select="serverInstance/agentList">
			<xsl:if test="agentType/alias = 'FILE_RENAME_AGENT'">
			<xsl:if test="status != 'DELETED'">
				
				<initial-delay><xsl:value-of select="initialDelay"/></initial-delay>
				<execution-interval><xsl:value-of select="executionInterval"/></execution-interval>
				<service-list>
				<xsl:for-each select="serviceList">
				<xsl:if test="status != 'DELETED'">				
					<service>
						<name><xsl:value-of select="service/svctype/alias"/>-<xsl:value-of select="service/servInstanceId"/></name>
						<dest-path><xsl:value-of select="destinationPath"/></dest-path>
						<file-extension-list><xsl:value-of select="fileExtensitonList"/></file-extension-list>
						<extension-after-rename><xsl:value-of select="extAfterRename"/></extension-after-rename>
						<character-renaming-operation>
						<xsl:attribute name="enable"><xsl:value-of select="charRenameOperationEnable"/></xsl:attribute>
 						
 						<xsl:if test="charRenameOperationEnable = 'true'">
						    <xsl:for-each select="charRenameOpList">
						   		<xsl:if test="status = 'ACTIVE'">
						    	<character>
						    		<sequence-no><xsl:value-of select="sequenceNo"/></sequence-no>
						    		<position><xsl:value-of select="position"/></position>
						    		<start-index><xsl:value-of select="startIndex"/></start-index>
						    		<end-index><xsl:value-of select="endIndex"/></end-index>
						    		<padding><xsl:attribute name="type"><xsl:value-of select="paddingType"/></xsl:attribute></padding>
						    		<default-value ><xsl:value-of select="defaultValue"/></default-value>
						    		<length ><xsl:value-of select="length"/></length>
						    	</character>
 						   		</xsl:if> 
						   </xsl:for-each>
 					    </xsl:if>   
						</character-renaming-operation>
					</service>
				</xsl:if>
				</xsl:for-each>
				</service-list>
			</xsl:if>
			</xsl:if>
		</xsl:for-each>		
		</file-rename-agent>
	</xsl:template>
</xsl:stylesheet>