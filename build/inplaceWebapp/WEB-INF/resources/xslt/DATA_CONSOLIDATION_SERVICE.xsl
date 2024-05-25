<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="xml" indent="yes"></xsl:output>
	<xsl:template match="/">
	<xsl:if test="dataConsolidationService/status != 'DELETED'">
	<data-consolidation-service>
		<startup-mode><xsl:value-of select="dataConsolidationService/svcExecParams/startupMode"/></startup-mode>
		<immediate-execute-on-startup><xsl:value-of select="dataConsolidationService/svcExecParams/executeOnStartup"/></immediate-execute-on-startup>
		<execution-interval><xsl:value-of select="dataConsolidationService/svcExecParams/executionInterval"/></execution-interval>
		<sorting-type><xsl:value-of select="dataConsolidationService/svcExecParams/sortingType"/></sorting-type>
		<file-range>
			<xsl:if test="dataConsolidationService/minFileRange != '-1'  and  dataConsolidationService/maxFileRange != '-1'">
					<xsl:value-of select="dataConsolidationService/minFileRange"/>-<xsl:value-of select="dataConsolidationService/maxFileRange"/>
			</xsl:if>
		</file-range><!-- <file-range>16-49</file-range> -->
		<no-files-alert-interval>
			<xsl:if test="dataConsolidationService/noFileAlertInterval != '-1'">
					<xsl:value-of select="dataConsolidationService/noFileAlertInterval"/>
			</xsl:if>
		</no-files-alert-interval>	 <!-- Interval in Seconds and apply if interval is greater than Zero and if any files not received since configurable time then send the trap to ME. -->
		<sorting-criteria><xsl:value-of select="dataConsolidationService/svcExecParams/sortingCriteria"/></sorting-criteria>
		<processing-type><xsl:value-of select="dataConsolidationService/processingType"/></processing-type>
		<minimum-thread><xsl:value-of select="dataConsolidationService/svcExecParams/minThread"/></minimum-thread>
		<maximum-thread><xsl:value-of select="dataConsolidationService/svcExecParams/maxThread"/></maximum-thread>
		<thread-queue-size><xsl:value-of select="dataConsolidationService/svcExecParams/queueSize"/></thread-queue-size>
		<file-batch-size><xsl:value-of select="dataConsolidationService/svcExecParams/fileBatchSize"/></file-batch-size>
		<merge-delimiter><xsl:value-of select="dataConsolidationService/mergeDelimiter"/></merge-delimiter>
		<xsl:choose>
			<xsl:when test="dataConsolidationService/fileCDRSummaryFlag = 'true' and dataConsolidationService/enableDBStats = 'true' ">
				<store-cdr-file-summary-in-db>true</store-cdr-file-summary-in-db>
			</xsl:when>
			<xsl:otherwise>
				<store-cdr-file-summary-in-db>false</store-cdr-file-summary-in-db>
			</xsl:otherwise>
		</xsl:choose>
		<data-consolidation-parameters>
			<consolidation-type><xsl:value-of select="dataConsolidationService/consolidationType"/></consolidation-type>
			<across-file-processing-type><xsl:value-of select="dataConsolidationService/acrossFileProcessingType"/></across-file-processing-type>
			<across-file-partition><xsl:value-of select="dataConsolidationService/acrossFilePartition"/></across-file-partition>
			<across-file-min-batch-size><xsl:value-of select="dataConsolidationService/acrossFileMinBatchSize"/></across-file-min-batch-size>
			<across-file-max-batch-size><xsl:value-of select="dataConsolidationService/acrossFileMaxBatchSize"/></across-file-max-batch-size>
		</data-consolidation-parameters>
		<file-grouping>
			<xsl:attribute name="enabled">
					<xsl:value-of select="dataConsolidationService/fileGroupParam/fileGroupEnable"/>
			</xsl:attribute>
			<grouping-type><xsl:value-of select="dataConsolidationService/fileGroupParam/groupingType"/></grouping-type>
			<archive-path><xsl:value-of select="dataConsolidationService/fileGroupParam/archivePath"/></archive-path>
            <sourcewise-archive><xsl:value-of select="dataConsolidationService/fileGroupParam/sourcewiseArchive"/></sourcewise-archive>
		</file-grouping>
		<consolidation-list>
			<xsl:for-each select="dataConsolidationService/consolidation">
		    	 <xsl:if test="status != 'DELETED'">
					<consolidation>
					    <xsl:attribute name="name">
								<xsl:value-of select="consName"/>
						</xsl:attribute> 
						<consolidate-across-file-partition>
							<xsl:if test="acrossFilePartition != '-1'">
								<xsl:value-of select="acrossFilePartition"/>
							</xsl:if>
						</consolidate-across-file-partition>
						<group-attribute-list>
						
							<date-field-name><xsl:value-of select="dateFieldName"/></date-field-name>
							<date-segregation-type><xsl:value-of select="segregationType"/></date-segregation-type>
							
							<grouping-attribute-list>	
							<xsl:for-each select="consGrpAttList">
								<xsl:if test="status != 'DELETED'">
									<group-attribute>
										<group-field-name><xsl:value-of select="groupingField"/></group-field-name>
										<regex-extraction-expression>
											<xsl:attribute name="regex-enable">
												<xsl:value-of select="regExEnable"/>
											</xsl:attribute> 	
											<regex-expression><xsl:value-of select="regExExpression"/></regex-expression>
											<destination-field><xsl:value-of select="destinationField"/></destination-field>
										</regex-extraction-expression>
										<table-lookup>
											<xsl:attribute name="lookup-enable">
												<xsl:value-of select="lookUpEnable"/>
											</xsl:attribute> 	
											<table-name><xsl:value-of select="lookUpTableName"/></table-name>
											<table-column><xsl:value-of select="lookUpTableColumnName"/></table-column>
										</table-lookup>
									</group-attribute>
								</xsl:if>
							</xsl:for-each>
							</grouping-attribute-list>
						</group-attribute-list>
						<consolidation-attribute-list>
							<xsl:for-each select="consAttList">
								<xsl:if test="status != 'DELETED'">
									<attribute>
										<field-name><xsl:value-of select="fieldName"/></field-name>
										<data-type><xsl:value-of select="dataType"/></data-type>
										<operation><xsl:value-of select="operation"/></operation>
										<description><xsl:value-of select="description"/></description>
									</attribute>
								</xsl:if>
							</xsl:for-each>
						</consolidation-attribute-list>
					</consolidation>
				</xsl:if>
			</xsl:for-each>					
		</consolidation-list>
		<path-list>
		<xsl:for-each select="dataConsolidationService/svcPathList">
		   	<xsl:if test="status != 'DELETED'">
				<path>
					<path-id><xsl:value-of select="pathId"/></path-id>
					<source-path><xsl:value-of select="readFilePath"/></source-path>
					<read-filename-suffix><xsl:value-of select="readFilenameSuffix"/></read-filename-suffix>
					<max-files-count-alert><xsl:if test="maxCounterLimit != '-1'"><xsl:value-of select="maxCounterLimit"/></xsl:if></max-files-count-alert>  <!-- Provide Limit for maximum files to generate alert -->
					<read-filename-prefix><xsl:value-of select="readFilenamePrefix"/></read-filename-prefix>
					<is-compressed-input><xsl:value-of select="compressInFileEnabled"/></is-compressed-input>
					<device-name><xsl:value-of select="parentDevice/name" /></device-name>
					<consolidation-mapping-list>
						<xsl:for-each select="conMappingList">
		   					<xsl:if test="status != 'DELETED'">
								<consolidation-mapping>
									<consolidation-name><xsl:value-of select="mappingName"/></consolidation-name>
									<condition-list><xsl:value-of select="conditionList"/></condition-list>
									<condition-logical-operation><xsl:value-of select="logicalOperator"/></condition-logical-operation>
									<process-record-limit>
										<xsl:if test="processRecordLimit != '-1'">
													<xsl:value-of select="processRecordLimit"/>
										</xsl:if>
									</process-record-limit>
									<destination-path><xsl:value-of select="destPath"/></destination-path>
									<is-compressed-output><xsl:value-of select="compressedOutput"/></is-compressed-output>
									<write-configured-attributes-only><xsl:value-of select="writeOnlyConfiguredAttribute"/></write-configured-attributes-only>
									<field-name-for-count><xsl:value-of select="fieldNameForCount"/></field-name-for-count>
									<record-sorting-type><xsl:value-of select="recordSortingType"/></record-sorting-type>
									<record-sorting-field><xsl:value-of select="recordSortingField"/></record-sorting-field>
									<record-sorting-field-type><xsl:value-of select="recordSortingFieldType"/></record-sorting-field-type>
									<file-parameters>
										<file-name><xsl:value-of select="fileName"/></file-name>
										<file-sequence><xsl:value-of select="fileSequence"/></file-sequence>
										<sequence-range>
											<xsl:if test="minSeqRange != '-1'  and  maxSeqRange != '-1'">
													<xsl:value-of select="minSeqRange"/>-<xsl:value-of select="maxSeqRange"/>
											</xsl:if>
										</sequence-range>
									</file-parameters>
								</consolidation-mapping>
							</xsl:if>
						</xsl:for-each>
					</consolidation-mapping-list>
				</path>
			</xsl:if>
		</xsl:for-each>
		</path-list>
		</data-consolidation-service>
	</xsl:if>
	</xsl:template>
</xsl:stylesheet>