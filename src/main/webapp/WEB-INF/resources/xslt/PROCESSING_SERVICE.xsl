<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="xml" indent="yes"></xsl:output>
	<xsl:template match="/">
	<xsl:if test="processingService/status != 'DELETED'">
		<processing-service>
	    	<path-list>
		    	<xsl:for-each select="processingService/svcPathList">
		    		<xsl:if test="status != 'DELETED'">
			    		<path>
			    			<path-id><xsl:value-of select="pathId"/></path-id>
			    			<read-file-path><xsl:value-of select="readFilePath"/></read-file-path>
			    			<archive-path>
			    				<xsl:value-of select="archivePath"/>
			    			</archive-path>
							<read-filename-prefix><xsl:value-of select="readFilenamePrefix"/></read-filename-prefix>
							<read-filename-suffix><xsl:value-of select="readFilenameSuffix"/></read-filename-suffix>
							<max-files-count-alert>
								<xsl:if test="maxFilesCountAlert != '-1'">
									<xsl:value-of select="maxFilesCountAlert"/>
								</xsl:if>
							</max-files-count-alert>	
							<read-filename-contains><xsl:value-of select="readFilenameContains"/></read-filename-contains>
							<read-filename-exclude-types><xsl:value-of select="readFilenameExcludeTypes"/></read-filename-exclude-types>
							<is-compressed-input-file><xsl:value-of select="compressInFileEnabled"/></is-compressed-input-file>
							<is-compressed-output-file><xsl:value-of select="compressOutFileEnabled"/></is-compressed-output-file>
							<write-file-path><xsl:value-of select="writeFilePath"/></write-file-path>
														
							<device-name>
								<xsl:value-of select="parentDevice/name" />
							</device-name>
							
							<policy-name>
							    <xsl:if test="policyAlias != '' ">
									<xsl:value-of select="policyAlias"/>
								</xsl:if>
							</policy-name>
							<file-date><xsl:attribute name="enabled"><xsl:value-of select="fileGrepDateEnabled"/></xsl:attribute>
								<date-format><xsl:value-of select="dateFormat"/></date-format>
								<position><xsl:value-of select="position"/></position>
								<start-index><xsl:value-of select="startIndex"/></start-index>
								<end-index><xsl:value-of select="endIndex"/></end-index>
							</file-date>
							<write-cdr-header-footer><xsl:value-of select="writeCdrHeaderFooterEnabled"/></write-cdr-header-footer>
						<duplicate-record-policy>
						    <xsl:attribute name="enabled">
								<xsl:value-of select="duplicateRecordPolicyEnabled"/>
							</xsl:attribute>
							<xsl:attribute name="type">
								<xsl:value-of select="duplicateRecordPolicyType"/>
							</xsl:attribute>  
							<duplicate-check-across-file-parameters>
								<duplicate-check-date-unified-field>
									<xsl:if test="duplicateRecordPolicyType = 'ACROSS_FILE' ">
										<xsl:value-of select="acrossFileDuplicateDateField"/>
									</xsl:if>	
								</duplicate-check-date-unified-field>
								<duplicate-check-date-unified-field-format>
									<xsl:if test="duplicateRecordPolicyType = 'ACROSS_FILE' ">
										<xsl:value-of select="acrossFileDuplicateDateFieldFormat"/>
									</xsl:if>	
								</duplicate-check-date-unified-field-format>
								<duplicate-check-interval-type>
									<xsl:if test="duplicateRecordPolicyType = 'ACROSS_FILE' ">	
										<xsl:value-of select="acrossFileDuplicateDateIntervalType"/>
									</xsl:if>	
								</duplicate-check-interval-type> <!-- DAY/HOUR/MINUTE -->
								<duplicate-check-interval>
									<xsl:if test="duplicateRecordPolicyType = 'ACROSS_FILE' ">
										<xsl:value-of select="acrossFileDuplicateDateInterval"/>
									</xsl:if>	
								</duplicate-check-interval>
								<cache-limit>
									<xsl:if test="duplicateRecordPolicyType = 'ACROSS_FILE' ">
										<xsl:value-of select="acrossFileDuplicateCDRCacheLimit"/>
									</xsl:if>	
								</cache-limit>
							</duplicate-check-across-file-parameters>
							<unified-fields><xsl:value-of select="unifiedFields"/></unified-fields>
							<!-- <alert-id><xsl:value-of select="alertId"/></alert-id>
							<alert-description><xsl:value-of select="alertDescription"/></alert-description> -->
						</duplicate-record-policy>

			    		</path>
		    		</xsl:if>
		    	</xsl:for-each>		
	    	</path-list>
	    	<sorting-type><xsl:value-of select="processingService/svcExecParams/sortingType"/></sorting-type>
	    	<sorting-criteria><xsl:value-of select="processingService/svcExecParams/sortingCriteria"/></sorting-criteria> <!-- LAST_MODIFIED_DATE/FILE_NAME -->
			<global-sequence>
			    <xsl:attribute name="enabled">
					<xsl:value-of select="processingService/globalSeqEnabled"/>
				</xsl:attribute>
				<global-seq-device-name>
					<xsl:value-of select="processingService/globalSeqDeviceName"/>
				</global-seq-device-name>
				<global-seq-max-limit>
				<xsl:if test="processingService/globalSeqMaxLimit != '-1'">
					<xsl:value-of select="processingService/globalSeqMaxLimit"/>
					</xsl:if>
				</global-seq-max-limit>
			</global-sequence>
			<queue-size><xsl:value-of select="processingService/svcExecParams/queueSize"/></queue-size>
			<startup-mode><xsl:value-of select="processingService/svcExecParams/startupMode"/></startup-mode>
			<file-range>
				<xsl:if test="processingService/minFileRange != '-1'  and  processingService/maxFileRange != '-1'">
					<xsl:value-of select="processingService/minFileRange"/>-<xsl:value-of select="processingService/maxFileRange"/>
				</xsl:if>
			</file-range>
			<file-sequence-order><xsl:value-of select="processingService/fileSeqOrderEnable"/></file-sequence-order>
			<no-files-alert-interval>
				<xsl:if test="processingService/noFileAlert != '-1'">
					<xsl:value-of select="processingService/noFileAlert"/>
				</xsl:if>
			</no-files-alert-interval>
			<minimum-thread><xsl:value-of select="processingService/svcExecParams/minThread"/></minimum-thread>
			<maximum-thread><xsl:value-of select="processingService/svcExecParams/maxThread"/></maximum-thread>
			<execution-interval><xsl:value-of select="processingService/svcExecParams/executionInterval"/></execution-interval>
			<immediate-execute-on-startup><xsl:value-of select="processingService/svcExecParams/executeOnStartup"/></immediate-execute-on-startup>
	    	<file-batch-size><xsl:value-of select="processingService/svcExecParams/fileBatchSize"/></file-batch-size>
	    	<record-batch-size><xsl:value-of select="processingService/recordBatchSize"/></record-batch-size>
	    	<error-file-path><xsl:value-of select="processingService/errorPath"/></error-file-path>
	    	<!-- <rule-group-name>
				
			</rule-group-name>  -->
			<purge-cache-interval>
					<xsl:value-of select="processingService/acrossFileDuplicatePurgeCacheInterval"/>
			</purge-cache-interval>
			<cdr-date-summary>
			<xsl:attribute name="enabled">
						     <xsl:value-of select="processingService/storeCDRFileSummaryDB"/>
				</xsl:attribute>
			<date><xsl:value-of select="processingService/dateFieldForSummary"/></date>
			<type><xsl:value-of select="processingService/typeForSummary"/></type>
			<override-file-date><xsl:value-of select="processingService/overrideFileDateEnabled"/></override-file-date>
			<override-file-date-type><xsl:value-of select="processingService/overrideFileDateType"/></override-file-date-type>
		</cdr-date-summary>
		
	       	<file-grouping>
	       		<xsl:attribute name="enabled">
						     <xsl:value-of select="processingService/fileGroupingParameter/fileGroupEnable"/>
				</xsl:attribute>
	       		<policy-grouping><xsl:value-of select="processingService/fileGroupingParameter/filterGroupType"/></policy-grouping> <!-- rulewise/groupwise/NA -->
				<grouping-type><xsl:value-of select="processingService/fileGroupingParameter/groupingType"/></grouping-type>
				<grouping-date-type><xsl:value-of select="processingService/fileGroupingParameter/groupingDateType"/></grouping-date-type>
				<for-duplicate><xsl:value-of select="processingService/fileGroupingParameter/enableForDuplicate"/></for-duplicate>
				<for-filtered><xsl:value-of select="processingService/fileGroupingParameter/enableForFilter"/></for-filtered>
				<for-invalid><xsl:value-of select="processingService/fileGroupingParameter/enableForInvalid"/></for-invalid>
				<for-archive><xsl:value-of select="processingService/fileGroupingParameter/enableForArchive"/></for-archive>
				
				<duplicate-file-path><xsl:value-of select="processingService/fileGroupingParameter/duplicateDirPath"/></duplicate-file-path>
				<filtered-file-path><xsl:value-of select="processingService/fileGroupingParameter/filterDirPath"/></filtered-file-path>
				<invalid-file-path><xsl:value-of select="processingService/fileGroupingParameter/invalidDirPath"/></invalid-file-path>
				<archive-file-path><xsl:value-of select="processingService/fileGroupingParameter/archivePath"/></archive-file-path>
				<sourcewise-archive><xsl:value-of select="processingService/fileGroupingParameter/sourcewiseArchive"/></sourcewise-archive>
				
			</file-grouping>
		    
		    <xsl:choose>
				<xsl:when test="processingService/fileCDRSummaryFlag = 'true' and processingService/enableDBStats = 'true' ">
					<store-cdr-file-summary-in-db>true</store-cdr-file-summary-in-db>
				</xsl:when>
				<xsl:otherwise>
					<store-cdr-file-summary-in-db>false</store-cdr-file-summary-in-db>
				</xsl:otherwise>
			</xsl:choose>
		</processing-service>
	</xsl:if>
	</xsl:template>
</xsl:stylesheet>

