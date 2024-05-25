<?xml version="1.0"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="xml" indent="yes"></xsl:output>
	<xsl:template match="/">

		<ftp-collection-driver>
			<xsl:for-each select="collectionService/myDrivers">
				<xsl:param name="entityId" />

				<xsl:if
					test="(id = $entityId) and (driverType/alias = 'FTP_COLLECTION_DRIVER')">

					<xsl:if test="status != 'DELETED'">
						<file-transfer-mode>
							<xsl:value-of select="ftpConnectionParams/fileTransferMode" />
						</file-transfer-mode>
						<file-range>
							<xsl:if test="minFileRange != '-1' and maxFileRange != '-1'">
								<xsl:value-of select="minFileRange" />-<xsl:value-of select="maxFileRange" />
							</xsl:if>
						</file-range>
						<file-sequence-order>
							<xsl:value-of select="fileSeqOrder" />
						</file-sequence-order>
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
						<ftp-timeout>
							<xsl:value-of select="ftpConnectionParams/timeout" />
						</ftp-timeout>
						<path-list>
							<xsl:for-each select="driverPathList">
								<xsl:if test="status != 'DELETED'">
									<path>
										<path-instance-id>
											<xsl:value-of select="id" />
										</path-instance-id>
										<path-id>
											<xsl:value-of select="pathId" />
										</path-id>
										<read-file-path>
											<xsl:value-of select="readFilePath" />
										</read-file-path>
										<file-name-pattern>
											<xsl:value-of select="fileNamePattern" />
										</file-name-pattern>
										<read-filename-prefix>
											<xsl:value-of select="readFilenamePrefix" />
										</read-filename-prefix>
										<read-filename-suffix>
											<xsl:value-of select="readFilenameSuffix" />
										</read-filename-suffix>
										<max-files-count-alert>
											<xsl:if test="maxFilesCountAlert != '-1'">
												<xsl:value-of select="maxFilesCountAlert" />
											</xsl:if>
										</max-files-count-alert>
										<read-filename-contains>
											<xsl:value-of select="readFilenameContains" />
										</read-filename-contains>
										<write-file-path>
											<xsl:value-of select="writeFilePath" />
										</write-file-path>
										<write-filename-prefix>
											<xsl:value-of select="writeFilenamePrefix" />
										</write-filename-prefix>
										
										<reference-device-name>
											<xsl:value-of select="referenceDevice" />
										</reference-device-name>
										
										<device-name>
											<xsl:value-of select="parentDevice/name" />
										</device-name>
										
										<duplicate-check-params>
											<xsl:value-of select="duplicateCheckParamName" />
										</duplicate-check-params>
										
										<duplicate-file-suffix>
											<xsl:value-of select="duplicateFileSuffix" />
										</duplicate-file-suffix>
										
										<time-interval>
											<xsl:value-of select="timeInterval" />
										</time-interval>
										
										
										<filter-pattern>
											<action>
												<xsl:value-of select="remoteFileAction" />
											</action>
											<param>
												<xsl:attribute name="key"><xsl:value-of
													select="remoteFileActionParamName" /></xsl:attribute>
												<xsl:attribute name="value"><xsl:value-of
													select="remoteFileActionValue" /></xsl:attribute>
											</param>
											<xsl:choose>
												<xsl:when test="remoteFileAction = 'moveandrename'">
													<param>
														<xsl:attribute name="key"><xsl:value-of
															select="remoteFileActionParamNameTwo" /></xsl:attribute>
														<xsl:attribute name="value"><xsl:value-of
															select="remoteFileActionValueTwo" /></xsl:attribute>
													</param>													
												</xsl:when>
											</xsl:choose>
										</filter-pattern>
										<file-date>
											<xsl:attribute name="enabled"><xsl:value-of
												select="fileGrepDateEnabled" /></xsl:attribute>
											<date-format>
												<xsl:value-of select="dateFormat" />
											</date-format>
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
										</file-date>
										
										<file-sequence-alert>
											<xsl:attribute name="enabled"><xsl:value-of
												select="fileSeqAlertEnabled" /></xsl:attribute>
											<start-index>
												<xsl:if test="seqStartIndex != '-1'">
													<xsl:value-of select="seqStartIndex" />
												</xsl:if>
											</start-index>
											<end-index>
												<xsl:if test="seqEndIndex != '-1'">
													<xsl:value-of select="seqEndIndex" />
												</xsl:if>
											</end-index>
											<!-- <max-counter-limit>
												<xsl:value-of select="maxCounterLimit" />
											</max-counter-limit> -->
											
											<missing-sequence-frequency>
												<xsl:value-of select="missingFileSequenceId/resetFrequency" />
											</missing-sequence-frequency>
											
											<missing-sequence-min-range>
												<xsl:value-of select="missingFileSequenceId/minValue" />
											</missing-sequence-min-range>
											
											<missing-sequence-max-range>
												<xsl:value-of select="missingFileSequenceId/maxValue" />
											</missing-sequence-max-range>
											
										</file-sequence-alert>
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
                                                <padding>
                                                	<xsl:attribute name="type">
                                                		<xsl:value-of select="paddingType"/>
                                                	</xsl:attribute>
                                                	<xsl:value-of select="paddingValue"/>
                                                </padding>
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
												<date-format><xsl:value-of select="srcDateFormat"/></date-format>
                                                <date-type><xsl:value-of select="dateType"/></date-type>
										</character>
												</xsl:if>
						                    </xsl:for-each>
									</character-renaming-operation>
									<file-size-check>
											<xsl:attribute name="enabled">
											<xsl:value-of select="fileSizeCheckEnabled" />
											</xsl:attribute>
											<min-file-size>
												<xsl:value-of select="fileSizeCheckMinValue" />
											</min-file-size>
											<max-file-size>
												<xsl:value-of select="fileSizeCheckMaxValue" />
											</max-file-size>
									</file-size-check>
									</path>
								</xsl:if>
							</xsl:for-each>
						</path-list>
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
						<file-grouping>
							<xsl:attribute name="enabled"><xsl:value-of
								select="fileGroupingParameter/fileGroupEnable" /></xsl:attribute>
							<grouping-type>
								<xsl:value-of select="fileGroupingParameter/groupingType" />
							</grouping-type>
							<for-duplicate>
								<xsl:value-of select="fileGroupingParameter/enableForDuplicate" />
							</for-duplicate>
							<duplicate-file-path>
								<xsl:value-of select="fileGroupingParameter/duplicateDirPath" />
							</duplicate-file-path>
							<grouping-date-type>
								<xsl:value-of select="fileGroupingParameter/groupingDateType" />
							</grouping-date-type>
						</file-grouping>
						<file-fetch-rule>
							<xsl:attribute name="enabled"><xsl:value-of
								select="myFileFetchParams/fileFetchRuleEnabled" /></xsl:attribute>
							<file-fetch-type>
								<xsl:value-of select="myFileFetchParams/fileFetchType" />
							</file-fetch-type>
							<file-fetch-interval>
								<xsl:value-of select="myFileFetchParams/fileFetchIntervalMin" />
							</file-fetch-interval>
							<time-zone>
								<xsl:value-of select="myFileFetchParams/timeZone" />
							</time-zone>
						</file-fetch-rule>
						<no-files-alert-interval>
							<xsl:if test="noFileAlert != '-1'">
								<xsl:value-of select="noFileAlert" />
							</xsl:if>
						</no-files-alert-interval>
					</xsl:if>
				</xsl:if>

			</xsl:for-each>
		</ftp-collection-driver>
	</xsl:template>


</xsl:stylesheet>
