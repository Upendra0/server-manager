<?xml version="1.0"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:output method="xml" indent="yes"></xsl:output>
	<xsl:template match="/">
	<xsl:if test="parsingService/status != 'DELETED'">
	<parsing-service>
		<minimum-thread><xsl:value-of select="parsingService/svcExecParams/minThread"/></minimum-thread>
		<sorting-criteria><xsl:value-of select="parsingService/svcExecParams/sortingCriteria"/></sorting-criteria>
		<maximum-thread><xsl:value-of select="parsingService/svcExecParams/maxThread"/></maximum-thread>
		<file-batch-size><xsl:value-of select="parsingService/svcExecParams/fileBatchSize"/></file-batch-size>
		<queue-size><xsl:value-of select="parsingService/svcExecParams/queueSize"/></queue-size>
		<file-sequence-order><xsl:value-of select="parsingService/fileSeqOrderEnable"/></file-sequence-order>
		<store-file-statatics-in-db><xsl:value-of select="parsingService/fileStatInsertEnable"/></store-file-statatics-in-db>
		<no-files-alert-interval>
			<xsl:if test="parsingService/noFileAlert != '-1' ">
				<xsl:value-of select="parsingService/noFileAlert"/>
			</xsl:if>
		</no-files-alert-interval>
		<startup-mode><xsl:value-of select="parsingService/svcExecParams/startupMode"/></startup-mode>
		<file-range>
			<xsl:if test="parsingService/minFileRange != '-1'  and  parsingService/maxFileRange != '-1'">
				<xsl:value-of select="parsingService/minFileRange"/>-<xsl:value-of select="parsingService/maxFileRange"/>
			</xsl:if>
		</file-range>
		<execution-interval><xsl:value-of select="parsingService/svcExecParams/executionInterval"/></execution-interval>
		<immediate-execute-on-startup><xsl:value-of select="parsingService/svcExecParams/executeOnStartup"/></immediate-execute-on-startup>
			<xsl:choose>
				<xsl:when test="parsingService/fileCDRSummaryFlag = 'true' and parsingService/enableDBStats = 'true' ">
					<store-cdr-file-summary-in-db>true</store-cdr-file-summary-in-db>
				</xsl:when>
				<xsl:otherwise>
					<store-cdr-file-summary-in-db>false</store-cdr-file-summary-in-db>
				</xsl:otherwise>
			</xsl:choose>
		<equal-check-field><xsl:value-of select="parsingService/equalCheckField"/></equal-check-field>
		<equal-check-function><xsl:value-of select="parsingService/equalCheckFunction"/></equal-check-function>
		<equal-check-value><xsl:value-of select="parsingService/equalCheckValue"/></equal-check-value>
		<sorting-type><xsl:value-of select="parsingService/svcExecParams/sortingType"/></sorting-type>
		<cdr-date-summary>
			<date><xsl:value-of select="parsingService/dateFieldForSummary"/></date>
			<type><xsl:value-of select="parsingService/typeForSummary"/></type>
			<override-file-date><xsl:value-of select="parsingService/overrideFileDateEnabled"/></override-file-date>
			<override-file-date-type><xsl:value-of select="parsingService/overrideFileDateType"/></override-file-date-type>
		</cdr-date-summary>
		<!-- <file-copy-folders><xsl:value-of select="parsingService/fileCopyFolders"/></file-copy-folders>  -->
		<file-grouping><xsl:attribute name="enabled">
						     <xsl:value-of select="parsingService/fileGroupingParameter/fileGroupEnable"/>
						   </xsl:attribute>
                <grouping-type><xsl:value-of select="parsingService/fileGroupingParameter/groupingType"/></grouping-type>
                <grouping-date-type><xsl:value-of select="parsingService/fileGroupingParameter/groupingDateType"/></grouping-date-type>
                <archive-path><xsl:value-of select="parsingService/fileGroupingParameter/archivePath"/></archive-path>
                <sourcewise-archive><xsl:value-of select="parsingService/fileGroupingParameter/sourcewiseArchive"/></sourcewise-archive>
        </file-grouping>
       <record-batch-size><xsl:value-of select="parsingService/recordBatchSize"/></record-batch-size>
       <error-path><xsl:value-of select="parsingService/errorPath"/></error-path>
       <path-list>
       <xsl:for-each select="parsingService/svcPathList">
				<xsl:if test="status != 'DELETED'">
				<path>
					<path-id><xsl:value-of select="pathId"/></path-id>
					<read-file-path><xsl:value-of select="readFilePath"/></read-file-path>
					
					<parent-device>
						<xsl:value-of select="parentDevice/id"/>
					</parent-device>
					
					<device-name>
						<xsl:value-of select="parentDevice/name" />
					</device-name>
					
					<device-type>
						<xsl:value-of select="parentDevice/deviceType/name" />
					</device-type>
					
					<circle-id>
						<xsl:value-of select="circle/id" />
					</circle-id>
					<mandatory-fields>
						<xsl:value-of select="mandatoryFields" />
					</mandatory-fields>
					
					<file-date><xsl:attribute name="enabled"><xsl:value-of select="fileGrepDateEnabled"/></xsl:attribute>
						<date-format><xsl:value-of select="dateFormat"/></date-format>
						<position><xsl:value-of select="position"/></position>
						<start-index><xsl:value-of select="startIndex"/></start-index>
						<end-index><xsl:value-of select="endIndex"/></end-index>
					</file-date>					
					<plugin-list>
					<xsl:for-each select="parserWrappers">
					<xsl:if test="status != 'DELETED'">
					<plugin>
						<plugin-name><xsl:value-of select="parserType/alias"/></plugin-name>
						<is-compressed-input-file><xsl:value-of select="compressInFileEnabled"/></is-compressed-input-file>
						<is-compressed-output-file><xsl:value-of select="compressOutFileEnabled"/></is-compressed-output-file>
						<file-name-pattern><xsl:value-of select="fileNamePattern"/></file-name-pattern>
						<read-filename-prefix><xsl:value-of select="readFilenamePrefix"/></read-filename-prefix>
						<read-filename-suffix><xsl:value-of select="readFilenameSuffix"/></read-filename-suffix>
						<read-filename-contains><xsl:value-of select="readFilenameContains"/></read-filename-contains>
						<read-filename-exclude-types><xsl:value-of select="readFilenameExcludeTypes"/></read-filename-exclude-types>
						<write-file-path><xsl:value-of select="writeFilePath"/></write-file-path>
						<write-file-splitting><xsl:value-of select="writeFileSplit"/></write-file-splitting>
						<write-filename-prefix><xsl:value-of select="writeFilenamePrefix"/></write-filename-prefix>
						<max-files-count-alert><xsl:if test="maxFileCountAlert != '-1'"><xsl:value-of select="maxFileCountAlert"/></xsl:if></max-files-count-alert>
						<write-cdr-header-footer><xsl:value-of select="writeCdrHeaderFooterEnabled"/></write-cdr-header-footer>
						<write-cdr-default-attributes><xsl:value-of select="writeCdrDefaultAttributes"/></write-cdr-default-attributes>
						<plugin-instance-id><xsl:value-of select="id"/></plugin-instance-id>
					</plugin>
					</xsl:if>
					</xsl:for-each>
					</plugin-list>
				</path>
				</xsl:if>
		</xsl:for-each>
		</path-list>			
	</parsing-service>
	</xsl:if>
	</xsl:template>
</xsl:stylesheet>
