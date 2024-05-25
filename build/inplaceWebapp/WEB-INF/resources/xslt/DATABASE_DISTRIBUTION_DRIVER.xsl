<?xml version="1.0"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:output method="xml" indent="yes"></xsl:output>
	<xsl:template match="/">
	<database-distibution-driver>
			<xsl:for-each select="distributionService/myDrivers">
			<xsl:param name="entityId" />
			
			<xsl:if test="(id = $entityId) and (driverType/alias = 'DATABASE_DISTRIBUTION_DRIVER')">
				<xsl:if test="status != 'DELETED'">
					<table-name><xsl:value-of select="tableName"/></table-name>
					<file-range><xsl:if test="minFileRange != '-1'"><xsl:value-of select="minFileRange"/>-</xsl:if><xsl:if test="maxFileRange != '-1'"><xsl:value-of select="maxFileRange"/></xsl:if></file-range>
					<file-sequence-order><xsl:value-of select="fileSeqOrder"/></file-sequence-order>
					<no-files-alert-interval ><xsl:if test="noFileAlert != '-1'"><xsl:value-of select="noFileAlert"/></xsl:if></no-files-alert-interval>
					<attribute-list>
							<xsl:for-each select="attributeList">
								<xsl:if test="status != 'DELETED'">
									<attribute>
										<database-field-name><xsl:value-of select="databaseFieldName"/></database-field-name>       
				                        <unified-packet-name><xsl:value-of select="unifiedFieldName"/></unified-packet-name>
				                        <data-type><xsl:value-of select="dataType"/></data-type>     
				                        <default-value><xsl:value-of select="defualtValue"/></default-value>
				                        <padding><xsl:attribute name="enable"><xsl:value-of select="paddingEnable"/></xsl:attribute>
											<length><xsl:value-of select="length"/></length>
											<padding-type><xsl:value-of select="paddingType"/></padding-type>
											<padding-char><xsl:value-of select="paddingChar"/></padding-char>
											<prefix><xsl:value-of select="prefix"/></prefix>
											<suffix><xsl:value-of select="suffix"/></suffix>
										</padding>    
									</attribute>
								</xsl:if>
							</xsl:for-each>  
					</attribute-list> 
					<path-list>
							<xsl:for-each select="driverPathList">
								<xsl:if test="status != 'DELETED'">
									<path>
										<path-id><xsl:value-of select="pathId"/></path-id>
										<read-file-path><xsl:value-of select="readFilePath"/></read-file-path>        <!-- source path from where we have to read file -->
				                        <archive-path><xsl:value-of select="archivePath"/></archive-path>
				                        <read-filename-prefix><xsl:value-of select="readFilenamePrefix"/></read-filename-prefix>     <!-- Read only those file whose prefix is this -->
				                        <read-filename-suffix><xsl:value-of select="readFilenameSuffix"/></read-filename-suffix>     <!-- Read only those file whose suffix is this -->
				                        <max-files-count-alert>
				                        	<xsl:if test="maxFilesCountAlert != '-1'">
				                        		<xsl:value-of select="maxFilesCountAlert"/>
				                        	</xsl:if>
				                        </max-files-count-alert>  <!-- Provide Limit for maximum files to generate alert -->
				                        <read-filename-contains><xsl:value-of select="readFilenameContains"/></read-filename-contains> <!-- Read only those file whose name contains this -->
				                        <read-filename-exclude-types><xsl:value-of select="readFilenameExcludeTypes"/></read-filename-exclude-types>     <!-- Read files except this extension list -->
				                        <is-compressed-input-file><xsl:value-of select="compressInFileEnabled"/></is-compressed-input-file> <!-- If input file is compressed or not compressed  -->
										<write-file-path><xsl:value-of select="writeFilePath"/></write-file-path> <!-- Comma separated destination path -->							
										<device-name>
											<xsl:value-of select="parentDevice/name" />
										</device-name>
									</path>
								</xsl:if>
							</xsl:for-each>  
					</path-list>
				</xsl:if>					
			</xsl:if>
			</xsl:for-each>
	</database-distibution-driver>
</xsl:template>
</xsl:stylesheet>
