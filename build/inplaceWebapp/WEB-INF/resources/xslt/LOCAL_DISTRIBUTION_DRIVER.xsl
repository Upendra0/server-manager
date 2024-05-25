<?xml version="1.0"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:output method="xml" indent="yes"></xsl:output>
	<xsl:template match="/">
	<local-distribution-driver>
			<xsl:for-each select="distributionService/myDrivers">
			<xsl:param name="entityId" />
			
			<xsl:if test="(id = $entityId) and (driverType/alias = 'LOCAL_DISTRIBUTION_DRIVER')">
				<xsl:if test="status != 'DELETED'">
					<path-list>
							<xsl:for-each select="driverPathList">
								<xsl:if test="status != 'DELETED'">
									<path>
										<path-id><xsl:value-of select="pathId"/></path-id>
										<read-file-path><xsl:value-of select="readFilePath"/></read-file-path>        <!-- source path from where we have to read file -->
										<file-name-pattern><xsl:value-of select="fileNamePattern" /></file-name-pattern>     <!-- Read only those file whose Regex is this -->
				                        <read-filename-prefix><xsl:value-of select="readFilenamePrefix"/></read-filename-prefix>     <!-- Read only those file whose prefix is this -->
				                        <read-filename-suffix><xsl:value-of select="readFilenameSuffix"/></read-filename-suffix>     <!-- Read only those file whose suffix is this -->
				                        <discard-read-filename-suffix><xsl:value-of select="dbReadFileNameExtraSuffix"/></discard-read-filename-suffix> 
				                        <max-files-count-alert>
				                        	<xsl:if test="maxFilesCountAlert != '-1'">	
				                        		<xsl:value-of select="maxFilesCountAlert"/>
				                        	</xsl:if>	
				                       	</max-files-count-alert>  <!-- Provide Limit for maximum files to generate alert -->
				                        <read-filename-contains><xsl:value-of select="readFilenameContains"/></read-filename-contains> <!-- Read only those file whose name contains this -->
				                        <read-filename-exclude-types><xsl:value-of select="readFilenameExcludeTypes"/></read-filename-exclude-types>     <!-- Read files except this extension list -->
				                        <is-compressed-input-file><xsl:value-of select="compressInFileEnabled"/></is-compressed-input-file> <!-- If input file is compressed or not compressed  -->
				                        <is-compressed-output-file><xsl:value-of select="compressOutFileEnabled"/></is-compressed-output-file> <!--  If output file is compressed or not compressed -->
				                        <write-file-path><xsl:value-of select="writeFilePath"/></write-file-path> <!-- Comma separated destination path -->
				                        <file-date><xsl:attribute name="enabled"><xsl:value-of select="fileGrepDateEnabled"/></xsl:attribute>
											<date-format><xsl:value-of select="dateFormat"/></date-format>
											<position><xsl:value-of select="position"/></position>
											<start-index><xsl:value-of select="startIndex"/></start-index>
											<end-index><xsl:value-of select="endIndex"/></end-index>
										</file-date>
																				
										<device-name>
											<xsl:value-of select="parentDevice/name" />
										</device-name>
											
				                        <plugin-list>
				                        		<xsl:for-each select="composerWrappers">
													<xsl:if test="status != 'DELETED'">
														<plugin>
						                                        <plugin-name><xsl:value-of select="composerType/alias"/></plugin-name>
						                                        <plugin-instance-id><xsl:value-of select="id"/> </plugin-instance-id>
						                                        <file-name-prefix><xsl:value-of select="writeFilenamePrefix"/></file-name-prefix>
						                                        <file-name-postfix><xsl:value-of select="writeFilenameSuffix"/></file-name-postfix>
						                                        <destination-path><xsl:value-of select="destPath"/></destination-path>
						                                        <file-extension-after-rename><xsl:value-of select="fileExtension"/></file-extension-after-rename>
 																<default-file-extension-remove-enabled><xsl:value-of select="defaultFileExtensionRemoveEnabled" /></default-file-extension-remove-enabled>
 																<file-split-enabled><xsl:value-of select="fileSplitEnabled" /></file-split-enabled>						                                        
						                                        
						                                        <character-renaming-operation>
						                                        	<xsl:for-each select="charRenameOperationList">
						                                        		<xsl:if test="status != 'DELETED'">
						                                                <character>
						                                                        <sequence-no><xsl:value-of select="sequenceNo"/></sequence-no>
						                                                        <query><xsl:value-of select="query"/></query>
						                                                        <position><xsl:value-of select="position"/></position>
						                                                        <start-index>
						                                                        	<xsl:if test="startIndex != '-1'">
						                                                        		<xsl:value-of select="startIndex"/>
						                                                        	</xsl:if>
						                                                        </start-index>
						                                                        <end-index>
						                                                        	<xsl:if test="endIndex != '-1'">
						                                                        		<xsl:value-of select="endIndex"/>
						                                                        	</xsl:if>	
						                                                        </end-index>
						                                                        <padding><xsl:attribute name="type"><xsl:value-of select="paddingType"/></xsl:attribute><xsl:value-of select="paddingValue"/></padding>
						                                                        <default-value ><xsl:value-of select="defaultValue"/></default-value>
																					<xsl:choose>
																						<xsl:when test="length != '-1'">
																							<length>
																								<xsl:value-of select="length" />
																							</length>
																						</xsl:when>
																						<xsl:otherwise>
																							<length />
																						</xsl:otherwise>
																					</xsl:choose>	
																				<cache-enable><xsl:value-of select="cacheEnable"/></cache-enable>	
						                                                </character>
						                                                </xsl:if>
						                                             </xsl:for-each>   
						                                        </character-renaming-operation>
						                                </plugin>		
				                        			</xsl:if>
												</xsl:for-each>
				                        </plugin-list>
									</path>
								</xsl:if>
							</xsl:for-each>  
					</path-list>
					<!-- Found issues and added later need to test it. -->
					<max-retry-count><xsl:if test="maxRetrycount != '-1'"><xsl:value-of select="maxRetrycount"/></xsl:if></max-retry-count>
					<file-range><xsl:if test="minFileRange != '-1'"><xsl:value-of select="minFileRange"/>-</xsl:if><xsl:if test="maxFileRange != '-1'"><xsl:value-of select="maxFileRange"/></xsl:if></file-range>
					<file-sequence-order><xsl:value-of select="fileSeqOrder"/></file-sequence-order>
					<no-files-alert-interval ><xsl:if test="noFileAlert != '-1'"><xsl:value-of select="noFileAlert"/></xsl:if></no-files-alert-interval>
					<control-file>
							<xsl:attribute name="enabled"><xsl:value-of select="driverControlFileParams/controlFileEnabled" /></xsl:attribute>
    						<write-file-location><xsl:value-of select="driverControlFileParams/controlFileLocation" /></write-file-location>
       						<control-file-attributes><xsl:value-of select="driverControlFileParams/attributes" /></control-file-attributes>
        					<attribute-separator><xsl:value-of select="driverControlFileParams/attributeSeparator" /></attribute-separator>
					        <control-file-rolling-duration><xsl:value-of select="driverControlFileParams/fileRollingDuration" /></control-file-rolling-duration>
					        <control-file-rolling-start-time><xsl:value-of select="driverControlFileParams/fileRollingStartTime" /></control-file-rolling-start-time> 
					        <control-file-name-convention><xsl:value-of select="driverControlFileParams/controlFileName" /></control-file-name-convention>
					        <control-file-sequence>
					        <xsl:attribute name="enabled"><xsl:value-of select="driverControlFileParams/fileSeqEnable" /></xsl:attribute>
								<sequence-id><xsl:value-of select="controlFileSeq/id" /></sequence-id>
							</control-file-sequence>
						</control-file>
				</xsl:if>					
			</xsl:if>
			</xsl:for-each>
	</local-distribution-driver>
</xsl:template>
</xsl:stylesheet>
