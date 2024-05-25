<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="xml" indent="yes"></xsl:output>
	<xsl:template match="/">
	<xsl:if test="aggregationService/status != 'DELETED'">
	<aggregation-service>
		<startup-mode><xsl:value-of select="aggregationService/svcExecParams/startupMode"/></startup-mode>
		<immediate-execute-on-startup><xsl:value-of select="aggregationService/svcExecParams/executeOnStartup"/></immediate-execute-on-startup>
		<execution-interval><xsl:value-of select="aggregationService/svcExecParams/executionInterval"/></execution-interval>
		<sorting-type><xsl:value-of select="aggregationService/svcExecParams/sortingType"/></sorting-type>
		<sorting-criteria><xsl:value-of select="aggregationService/svcExecParams/sortingCriteria"/></sorting-criteria>
		<file-range>
			<xsl:if test="aggregationService/minFileRange != '-1'  and  aggregationService/maxFileRange != '-1'">
					<xsl:value-of select="aggregationService/minFileRange"/>-<xsl:value-of select="aggregationService/maxFileRange"/>
			</xsl:if>
		</file-range><!-- <file-range>16-49</file-range> -->
		<no-files-alert-interval>
			<xsl:if test="aggregationService/noFileAlert != '-1'">
					<xsl:value-of select="aggregationService/noFileAlert"/>
			</xsl:if>
		</no-files-alert-interval>	 <!-- Interval in Seconds and apply if interval is greater than Zero and if any files not received since configurable time then send the trap to ME. -->
		<minimum-thread><xsl:value-of select="aggregationService/svcExecParams/minThread"/></minimum-thread>
		<maximum-thread><xsl:value-of select="aggregationService/svcExecParams/maxThread"/></maximum-thread>	
		<thread-queue-size><xsl:value-of select="aggregationService/svcExecParams/queueSize"/></thread-queue-size>
		<file-batch-size><xsl:value-of select="aggregationService/svcExecParams/fileBatchSize"/></file-batch-size>
		<xsl:choose>
			<xsl:when test="aggregationService/fileCDRSummaryFlag = 'true' and aggregationService/enableDBStats = 'true' ">
				<store-cdr-file-summary-in-db>true</store-cdr-file-summary-in-db>
			</xsl:when>
			<xsl:otherwise>
				<store-cdr-file-summary-in-db>false</store-cdr-file-summary-in-db>
			</xsl:otherwise>
		</xsl:choose>
		<error-path><xsl:value-of select="aggregationService/errorPath"/></error-path>
		<merge-delimiter><xsl:value-of select="aggregationService/delimiter"/></merge-delimiter>
		<scheduling>
			<xsl:attribute name="enabled">
				<xsl:value-of select="aggregationService/serviceSchedulingParams/schedulingEnabled"/>
			</xsl:attribute>
			<scheduling-type><xsl:value-of select="aggregationService/serviceSchedulingParams/schType"/></scheduling-type>
			<scheduling-time><xsl:value-of select="aggregationService/serviceSchedulingParams/time"/></scheduling-time>
			<scheduling-day><xsl:value-of select="aggregationService/serviceSchedulingParams/day"/></scheduling-day>
			<scheduling-date><xsl:value-of select="aggregationService/serviceSchedulingParams/date"/></scheduling-date>
		</scheduling>				
		<file-grouping>
			<xsl:attribute name="enabled">
				<xsl:value-of select="aggregationService/fileGroupingParameter/fileGroupEnable"/>
			</xsl:attribute>
			<grouping-type><xsl:value-of select="aggregationService/fileGroupingParameter/groupingType"/></grouping-type>
			<sourcewise-archive><xsl:value-of select="aggregationService/fileGroupingParameter/sourcewiseArchive"/></sourcewise-archive>
        	<archive-path><xsl:value-of select="aggregationService/fileGroupingParameter/archivePath"/></archive-path>
	    </file-grouping>
    	<aggregation-definition>
			<aggregation-name><xsl:value-of select="aggregationService/aggregationDefinition/aggDefName"/></aggregation-name>
			<date-partition-count><xsl:value-of select="aggregationService/aggregationDefinition/noOfPartition"/></date-partition-count>
			<partial-cdr-indicator-field><xsl:value-of select="aggregationService/aggregationDefinition/partCDRField"/></partial-cdr-indicator-field>
			<first-leg-indication-value><xsl:value-of select="aggregationService/aggregationDefinition/fLegVal"/></first-leg-indication-value>
			<last-leg-indication-value><xsl:value-of select="aggregationService/aggregationDefinition/lLegVal"/></last-leg-indication-value>
			<unified-date-field><xsl:value-of select="aggregationService/aggregationDefinition/unifiedDateFiled"/></unified-date-field>
			<aggregation-interval><xsl:value-of select="aggregationService/aggregationDefinition/aggInterval"/></aggregation-interval>
			<output-file-fields-type><xsl:value-of select="aggregationService/aggregationDefinition/outputFileField"/></output-file-fields-type>
			<condition-list>
			<xsl:for-each select="aggregationService/aggregationDefinition/aggConditionList">
				<condition>
					<condition-expression><xsl:value-of select="condExpression"/></condition-expression>
					<condition-action><xsl:value-of select="condAction"/></condition-action>
				</condition>
			</xsl:for-each>
			</condition-list>
			<grouping-attribute-list>
			<xsl:for-each select="aggregationService/aggregationDefinition/aggKeyAttrList">
				<group-field-name><xsl:value-of select="fieldName"/></group-field-name>
			</xsl:for-each>
			</grouping-attribute-list>	
			<aggregation-attribute-list>
			<xsl:for-each select="aggregationService/aggregationDefinition/aggAttrList">
				<attribute>
					<field-name><xsl:value-of select="outputFieldName"/></field-name>
					<data-type><xsl:value-of select="outputDataType"/></data-type>
					<operation-expression><xsl:value-of select="outputExpression"/></operation-expression>
				</attribute>
			</xsl:for-each>
			</aggregation-attribute-list>
		</aggregation-definition>
		 <!-- <xsl:if test="aggregationService/aggregationDefinition != ''"> s </xsl:if> -->
	    <path-list>
		<xsl:for-each select="aggregationService/svcPathList">
		   	<xsl:if test="status != 'DELETED'">
		   		<path>
		   			<path-id><xsl:value-of select="pathId"/></path-id>
					<source-path><xsl:value-of select="readFilePath"/></source-path>
					<read-filename-suffix><xsl:value-of select="readFilenameSuffix"/></read-filename-suffix>
					<max-files-count-alert><xsl:if test="maxFilesCountAlert != '-1'"><xsl:value-of select="maxFilesCountAlert"/></xsl:if></max-files-count-alert>  <!-- Provide Limit for maximum files to generate alert -->
					<read-filename-prefix><xsl:value-of select="readFilenamePrefix"/></read-filename-prefix>
					<is-compressed-input><xsl:value-of select="compressInFileEnabled"/></is-compressed-input>
					<is-compressed-output><xsl:value-of select="compressOutFileEnabled" /></is-compressed-output>
					<aggregated-destination-path><xsl:value-of select="writeFilePath" /></aggregated-destination-path>
					<non-aggregated-destination-path><xsl:value-of select="wPathNonAggregate" /></non-aggregated-destination-path>
					<error-aggregated-destination-path><xsl:value-of select="wPathAggregateError" /></error-aggregated-destination-path>
					<output-file-parameters>
						<file-name><xsl:value-of select="oFilePathName" /></file-name>
						<file-sequence><xsl:value-of select="oFileSeqEnables"/></file-sequence>
						<sequence-range>
							<xsl:if test="oFileMinRange != '-1'  and  oFileMaxRange != '-1'">
								<xsl:value-of select="oFileMinRange"/>-<xsl:value-of select="oFileMaxRange"/>
							</xsl:if>
						</sequence-range>						
					</output-file-parameters>
					<output-file-parameters-nonaggregated>
						<file-name><xsl:value-of select="oFilePathNameForNonAgg" /></file-name>
						<file-sequence><xsl:value-of select="oFileSeqEnablesForNonAgg"/></file-sequence>
						<sequence-range>
							<xsl:if test="oFileMinRangeForNonAgg != '-1'  and  oFileMaxRangeForNonAgg != '-1'">
								<xsl:value-of select="oFileMinRangeForNonAgg"/>-<xsl:value-of select="oFileMaxRangeForNonAgg"/>
							</xsl:if>
						</sequence-range>						
					</output-file-parameters-nonaggregated>
					<output-file-parameters-error>
						<file-name><xsl:value-of select="oFilePathNameForError" /></file-name>
						<file-sequence><xsl:value-of select="oFileSeqEnablesForError"/></file-sequence>
						<sequence-range>
							<xsl:if test="oFileMinRangeForError != '-1'  and  oFileMaxRangeForError != '-1'">
								<xsl:value-of select="oFileMinRangeForError"/>-<xsl:value-of select="oFileMaxRangeForError"/>
							</xsl:if>
						</sequence-range>						
					</output-file-parameters-error>
					
					<device-name>
						<xsl:value-of select="parentDevice/name" />
					</device-name>
					
					<file-date>
						<xsl:attribute name="enabled"><xsl:value-of select="fileGrepDateEnabled"/></xsl:attribute>
						<date-format><xsl:value-of select="dateFormat"/></date-format>
						<position><xsl:value-of select="position"/></position>
						<start-index><xsl:value-of select="startIndex"/></start-index>
						<end-index><xsl:value-of select="endIndex"/></end-index>
					</file-date>
				</path>
			</xsl:if>
		</xsl:for-each>
		</path-list>
		</aggregation-service>
	</xsl:if>
	</xsl:template>
</xsl:stylesheet>