<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="xml" indent="yes"></xsl:output>
	<xsl:template match="/">
	<xsl:if test="ipLogParsingService/status != 'DELETED'">
		<iplog-parsing-service>
			<source-path><xsl:value-of select="ipLogParsingService/svcPathList/readFilePath"/></source-path>
			<sorting-type><xsl:value-of select="ipLogParsingService/svcExecParams/sortingType"/></sorting-type>
			<sorting-criteria><xsl:value-of select="ipLogParsingService/svcExecParams/sortingCriteria"/></sorting-criteria>
			
			<minimum-thread><xsl:value-of select="ipLogParsingService/svcExecParams/minThread"/></minimum-thread>
			<maximum-thread><xsl:value-of select="ipLogParsingService/svcExecParams/maxThread"/></maximum-thread>
			<file-batch-size><xsl:value-of select="ipLogParsingService/svcExecParams/fileBatchSize"/></file-batch-size>
			<queue-size><xsl:value-of select="ipLogParsingService/svcExecParams/queueSize"/></queue-size>
			<record-batch-size><xsl:value-of select="ipLogParsingService/recordBatchSize"/></record-batch-size>
			<startup-mode><xsl:value-of select="ipLogParsingService/svcExecParams/startupMode"/></startup-mode>
			<execution-interval><xsl:value-of select="ipLogParsingService/svcExecParams/executionInterval"/></execution-interval>
			<immediate-execute-on-startup><xsl:value-of select="ipLogParsingService/svcExecParams/executeOnStartup"/></immediate-execute-on-startup>
			<xsl:for-each select="ipLogParsingService/svcPathList">
				<xsl:if test="status != 'DELETED'">
					<xsl:for-each select="parserWrappers">
						<xsl:if test="status != 'DELETED'">
							<source-file-types><xsl:value-of select="readFilenameSuffix"/></source-file-types>
							<select-file-on-prefixes><xsl:value-of select="readFilenamePrefix"/></select-file-on-prefixes>
        					<select-file-on-suffixes><xsl:value-of select="readFilenameSuffix"/></select-file-on-suffixes>
        					<!-- <write-filename-prefix><xsl:value-of select="writeFilenamePrefix"/></write-filename-prefix> -->
        					<exclude-file-types><xsl:value-of select="readFilenameExcludeTypes"/></exclude-file-types>
        					<is-compressed><xsl:value-of select="compressInFileEnabled"/></is-compressed>
						   <file-compression><xsl:value-of select="compressOutFileEnabled"/></file-compression>
						   <plugin-name><xsl:value-of select="parserType/alias"/></plugin-name>
        					        	
						</xsl:if>
					</xsl:for-each>
				</xsl:if> 
			</xsl:for-each>
        	<file-grouping><xsl:attribute name="enabled">
						     <xsl:value-of select="ipLogParsingService/fileGroupingParameter/fileGroupEnable"/>
						   </xsl:attribute>
                <grouping-type><xsl:value-of select="ipLogParsingService/fileGroupingParameter/groupingType"/></grouping-type>
                <for-archive><xsl:value-of select="ipLogParsingService/fileGroupingParameter/fileGroupEnable"/></for-archive>
                <archive-path><xsl:value-of select="ipLogParsingService/fileGroupingParameter/archivePath"/></archive-path>
        	</file-grouping>
			
			
			
			
			
			<syslog-output-configuration>
				<xsl:for-each select="ipLogParsingService/svcPathList">
					<xsl:if test="status != 'DELETED'">
						<xsl:for-each select="parserWrappers">
							<xsl:if test="status != 'DELETED'">
								<destination-directory-path><xsl:value-of select="writeFilePath"/></destination-directory-path>
							</xsl:if>
						</xsl:for-each>
					</xsl:if>
				</xsl:for-each>
		        <file-partition>
		                <field-seperator><xsl:value-of select="ipLogParsingService/hashSeparator"/></field-seperator>
		                <partition-type><xsl:value-of select="ipLogParsingService/indexType"/></partition-type> <!-- hash-base/ip-base -->
		                <field-based-hash-partition>
		                	<xsl:for-each select="ipLogParsingService/partionParamList">
								<xsl:if test="status != 'DELETED'">
									<key-field >
										<xsl:attribute name="type">
						        			<xsl:value-of select="partitionField"/>
						   	 			</xsl:attribute> 
		                                <unified-field-name><xsl:value-of select="unifiedField"/></unified-field-name>
		                                <range><xsl:value-of select="partitionRange"/></range>
		                                	<xsl:if test="partitionField != 'Date'">
			                                	<base-unified-field-name><xsl:value-of select="baseUnifiedField"/></base-unified-field-name>
			                                	
			                                	<xsl:choose>
													<xsl:when test="netMask != '-1' ">
														<net-mask><xsl:value-of select="netMask"/></net-mask> 
													</xsl:when>
													<xsl:otherwise>
														<net-mask /> 
													</xsl:otherwise>
												</xsl:choose>
			                                </xsl:if>
		                        	</key-field>
								</xsl:if>
							</xsl:for-each>
		                </field-based-hash-partition>
		        </file-partition>
        		<inprocess-file-purge>
                	<purge-interval><xsl:value-of select="ipLogParsingService/purgeInterval"/></purge-interval>
                	<max-rename-interval><xsl:value-of select="ipLogParsingService/purgeDelayInterval"/></max-rename-interval> <!-- to be asked --> 
        		</inprocess-file-purge>
    		</syslog-output-configuration>
    		<equal-check-field><xsl:value-of select="ipLogParsingService/equalCheckField"/></equal-check-field>
    		<equal-check-function><xsl:value-of select="ipLogParsingService/equalCheckFunction"/></equal-check-function>
    		<equal-check-value><xsl:value-of select="ipLogParsingService/equalCheckValue"/></equal-check-value>
    		<output-file-header><xsl:value-of select="ipLogParsingService/outputFileHeader"/></output-file-header>
    		<file-statistics><xsl:attribute name="enabled">
						        <xsl:value-of select="ipLogParsingService/fileStatsEnabled"/>
						   	 </xsl:attribute>
					<file-statistics-path> <xsl:value-of select="ipLogParsingService/fileStatsLoc"/></file-statistics-path>
			</file-statistics>
    		<pre-correlation>
    						<xsl:attribute name="enabled">
						        <xsl:value-of select="ipLogParsingService/correlEnabled"/>
						   	 </xsl:attribute> <!-- With this field pre-correlation can be made as enable or disable -->
				<cdr-type-field><xsl:value-of select="ipLogParsingService/mappedSourceField"/></cdr-type-field> <!-- Please note that this field is case-sensitive so configure unified field correctly as per the file header -->
				<destination-port-field><xsl:value-of select="ipLogParsingService/destPortField"/></destination-port-field> <!-- Please note that this field is case-sensitive so configure unified field correctly as per the file header -->
				<destination-ports-filter><xsl:value-of select="ipLogParsingService/destPortFilter"/></destination-ports-filter> <!-- Comma separated values without having space in between -->
				<c-data-destination-path><xsl:value-of select="ipLogParsingService/createRecDestPath"/></c-data-destination-path>
				<d-data-destination-path><xsl:value-of select="ipLogParsingService/deleteRecDestPath"/></d-data-destination-path>
			</pre-correlation>
    		
		</iplog-parsing-service>
	</xsl:if>
	</xsl:template>
</xsl:stylesheet>