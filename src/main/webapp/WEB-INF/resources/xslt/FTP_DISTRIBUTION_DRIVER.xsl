<?xml version="1.0"?>
<xsl:stylesheet version="2.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="xml" indent="yes"></xsl:output>
	<xsl:template match="/">
		<ftp-distribution-driver>
			<xsl:for-each select="distributionService/myDrivers">
				<xsl:param name="entityId" />

				<xsl:if
					test="(id = $entityId) and (driverType/alias = 'FTP_DISTRIBUTION_DRIVER')">
					<xsl:if test="status != 'DELETED'">
						<file-transfer-mode>
							<xsl:value-of select="ftpConnectionParams/fileTransferMode" />
						</file-transfer-mode>
						<host-list>
				
						 <xsl:for-each select="ftpConnectionParams/iPAddressHostList"> 
							
							<host>
								<xsl:value-of select="iPAddressHost" />
							</host>
						</xsl:for-each>  
							
							
						</host-list>
						<port>
							<xsl:value-of select="ftpConnectionParams/port" />
						</port>
						<file-range>
							<xsl:if test="minFileRange != '-1' and maxFileRange != '-1'">
								<xsl:value-of select="minFileRange" />-<xsl:value-of select="maxFileRange" />
							</xsl:if>
						</file-range>
						
						<file-sequence-order>
							<xsl:value-of select="fileSeqOrder" />
						</file-sequence-order>
						<no-files-alert-interval>
							<xsl:if test="noFileAlert != '-1'">
								<xsl:value-of select="noFileAlert" />
							</xsl:if>
						</no-files-alert-interval>
						<ftp-timeout>
							<xsl:value-of select="ftpConnectionParams/timeout" />
						</ftp-timeout>
						<user-name>
							<xsl:value-of select="ftpConnectionParams/username" />
						</user-name>
						<password>
							<xsl:value-of select="ftpConnectionParams/password" />
						</password>
						<max-retry-count>
							<xsl:if test="maxRetrycount != '-1'">
								<xsl:value-of select="maxRetrycount" />
							</xsl:if>
						</max-retry-count>
						<remote-system-file-seperator>
							<xsl:value-of select="ftpConnectionParams/fileSeparator" />
						</remote-system-file-seperator>
					    <validate-inprocess-file>
							<xsl:value-of select="ftpConnectionParams/validateInProcessFile" />
						</validate-inprocess-file>
						<path-list>
							<xsl:for-each select="driverPathList">
								<xsl:if test="status != 'DELETED'">
									<path>
										<path-id><xsl:value-of select="pathId"/></path-id>
										<read-file-path>
											<xsl:value-of select="readFilePath" />
										</read-file-path>        <!-- source path from where we have to read file -->
										<file-name-pattern>
											<xsl:value-of select="fileNamePattern" />
										</file-name-pattern>     <!-- Read only those file whose Regex is this -->
										<read-filename-prefix>
											<xsl:value-of select="readFilenamePrefix" />
										</read-filename-prefix>     <!-- Read only those file whose prefix is this -->
										<read-filename-suffix>
											<xsl:value-of select="readFilenameSuffix" />
										</read-filename-suffix>     <!-- Read only those file whose suffix is this -->
										<discard-read-filename-suffix><xsl:value-of select="dbReadFileNameExtraSuffix"/></discard-read-filename-suffix> 
										<max-files-count-alert>
											<xsl:if test="maxFilesCountAlert != '-1'">
												<xsl:value-of select="maxFilesCountAlert" />
											</xsl:if>
										</max-files-count-alert>  <!-- Provide Limit for maximum files to generate alert -->
										<read-filename-contains>
											<xsl:value-of select="readFilenameContains" />
										</read-filename-contains> <!-- Read only those file whose name contains this -->
										<read-filename-exclude-types>
											<xsl:value-of select="readFilenameExcludeTypes" />
										</read-filename-exclude-types>     <!-- Read files except this extension list -->
										<is-compressed-input-file>
											<xsl:value-of select="compressInFileEnabled" />
										</is-compressed-input-file> <!-- If input file is compressed or not compressed -->
										<is-compressed-output-file>
											<xsl:value-of select="compressOutFileEnabled" />
										</is-compressed-output-file> <!-- If output file is compressed or not compressed -->
										<write-file-path>
											<xsl:value-of select="writeFilePath" />
										</write-file-path> <!-- Comma separated destination path -->
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
														<plugin-name>
															<xsl:value-of select="composerType/alias" />
														</plugin-name>
														<plugin-instance-id>
															<xsl:value-of select="id" />
														</plugin-instance-id>
														<file-name-prefix>
															<xsl:value-of select="writeFilenamePrefix" />
														</file-name-prefix>
														<file-name-postfix>
															<xsl:value-of select="writeFilenameSuffix" />
														</file-name-postfix>
														<destination-path>
															<xsl:value-of select="destPath" />
														</destination-path>
														<distributed-file-backup-path>
															<xsl:value-of select="fileBackupPath" />
														</distributed-file-backup-path>
														<file-extension-after-rename>
															<xsl:value-of select="fileExtension" />
														</file-extension-after-rename>
														<default-file-extension-remove-enabled>
															<xsl:value-of select="defaultFileExtensionRemoveEnabled" />
														</default-file-extension-remove-enabled>
														<file-split-enabled>
															<xsl:value-of select="fileSplitEnabled" />
														</file-split-enabled>

														<character-renaming-operation>
															<xsl:for-each select="charRenameOperationList">
																<xsl:if test="status != 'DELETED'">
																	<character>
																		<sequence-no>
																			<xsl:value-of select="sequenceNo" />
																		</sequence-no>
																		<query>
																			<xsl:value-of select="query" />
																		</query>
																		<position>
																			<xsl:value-of select="position" />
																		</position>
																		
																		<start-index>
																			<xsl:if test="startIndex != '-1'">
																				<xsl:value-of select="startIndex" />
																			</xsl:if>
																		</start-index>
																		<end-index>
																			<xsl:if test="endIndex != '-1'">
																				<xsl:value-of select="endIndex" />
																			</xsl:if>
																		</end-index>
																		<padding>
																			<xsl:attribute name="type"><xsl:value-of
																				select="paddingType" /></xsl:attribute>
																			<xsl:value-of select="paddingValue" />
																		</padding>
																		<default-value>
																			<xsl:value-of select="defaultValue" />
																		</default-value>

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
		</ftp-distribution-driver>

	</xsl:template>
</xsl:stylesheet>



<!-- ========================================================== -->
<!-- Details of each configuration parameter -->
<!-- ========================================================== -->

<!-- <ftp-collection-driver> <file-transfer-mode>BINARY</file-transfer-mode> 
	Transfer mode of the files, can be either ASCII or BINARY. The files will 
	be transfered in the mode specified. <host>192.168.1.120</host> The IP address 
	to which the server will connect and fetch the raw files and copies it to 
	the collected folder of collection service. Change in value requires service 
	restart. <port>21</port> Defines the FTP Port to which the server will connect. 
	Change in value requires service restart. <ftp-timeout>60000</ftp-timeout> 
	Specify the time in milisecond. Upto this much time ftp server will trying 
	to create a connection with host in case of failure. <source-path>nirav</source-path> 
	The path from which the raw files are to be collected. <user-name>admin</user-name> 
	User Name to connect FTP Server <password>admin</password> Password to connect 
	FTP Server <source-file-types>.nbd</source-file-types> Represents the extension 
	type of the files which are to be picked for collection from the source path. 
	<is-compressed-output>false</is-compressed-output> We have a support of compressed 
	output file in distribution drivers. If value of this tag is true then mediation 
	server can able to write the .gz file according to the given extension. <filter-pattern> 
	<action>rename</action> <param name="destpath" value=".col"/> Represents 
	the extension type by which the files are to be renamed on the FTP Server 
	which are collected to the destination path for filtering, so that the files 
	are not picked up again. <action>move</action> <param name="destpath" value="D:\device\SSTP1"/> 
	Above action will move the files from soure to specified destination after 
	colletion. </filter-pattern> <file-grouping enabled="true"> To enable file 
	grouping <grouping-type>day</grouping-type> Service will insert files into 
	archived folder in form of, If Day, duplicate/Year/Month/Day/ If Month, duplicate/Year/Month/ 
	If Year, duplicate/Year/ <for-archive>false</for-archive> To move or not 
	file in archieve. <for-duplicate>false</for-duplicate> To move or not file 
	in duplicate. </file-grouping> <plugin-list> <plugin> <plugin-name>DEFAULT_COMPOSER_PLUGIN</plugin-name> 
	<file-name-prefix></file-name-prefix> <file-name-postfix></file-name-postfix> 
	<plugin-instance-id>000</plugin-instance-id> This would be the instance id 
	configured in the plugin <destination-path>/u03/EliteMediation/SQA/primary</destination-path> 
	<distributed-file-backup-path>D:/MTNICE_MEDIATION/MEDIATION_ROOT/DISTRIBUTED_FILE/sentfile</distributed-file-backup-path> 
	Backup path for the output file. <file-extension-after-rename>.csv</file-extension-after-rename> 
	Specify the Extension of the output file. <character-renaming-operation> 
	<character> <sequence-no>1</sequence-no> <query>select lpad(SEQ_FILEDISTRIBUTIONSUMMARY.nextval,5, 
	'0') from dual</query> <position>left</position> <start-index>0</start-index> 
	<end-index>2</end-index> <padding type="right"></padding> <default-value></default-value> 
	<length></length> </character> </character-renaming-operation> </plugin> 
	</plugin-list> <max-retry-count>10</max-retry-count> Number of times by which 
	the service will retry in terms of any failure. <recursive>true</recursive> 
	If true, collects raw data files recursively from the source path. <remote-system-file-seperator>/</remote-system-file-seperator> 
	Represents the file seperator of the remote server. </ftp-collection-driver> -->

<!-- ========================================================== -->
<!-- End of configuration details -->
<!-- ========================================================== -->
