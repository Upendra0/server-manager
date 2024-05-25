<?xml version="1.0"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:output method="xml" indent="yes"></xsl:output>
	<xsl:template match="/">
	<xsl:if test="collectionService/status != 'DELETED'">
		<collection-service>
			<startup-mode><xsl:value-of select="collectionService/svcExecParams/startupMode"/></startup-mode>
			<execution-interval><xsl:value-of select="collectionService/svcExecParams/executionInterval"/></execution-interval>
			<immediate-execute-on-startup><xsl:value-of select="collectionService/svcExecParams/executeOnStartup"/></immediate-execute-on-startup>
			<sorting-criteria><xsl:value-of select="collectionService/svcExecParams/sortingCriteria"/></sorting-criteria>
			
			<queue-size><xsl:value-of select="collectionService/svcExecParams/queueSize"/></queue-size>
			<minimum-thread><xsl:value-of select="collectionService/svcExecParams/minThread"/></minimum-thread>
			<maximum-thread><xsl:value-of select="collectionService/svcExecParams/maxThread"/></maximum-thread>
			<file-batch-size><xsl:value-of select="collectionService/svcExecParams/fileBatchSize"/></file-batch-size>
			<xsl:choose>
				<xsl:when test="collectionService/fileCDRSummaryFlag = 'true' and collectionService/enableDBStats = 'true' ">
					<store-cdr-file-summary-in-db>true</store-cdr-file-summary-in-db>
				</xsl:when>
				<xsl:otherwise>
					<store-cdr-file-summary-in-db>false</store-cdr-file-summary-in-db>
				</xsl:otherwise>
			</xsl:choose>
			<sorting-type><xsl:value-of select="collectionService/svcExecParams/sortingType"/></sorting-type>
			<minimum-disk-space-required></minimum-disk-space-required>
			<scheduling><xsl:attribute name="enabled">
			<xsl:value-of select="collectionService/serviceSchedulingParams/schedulingEnabled"/>
			</xsl:attribute>
			<scheduling-type><xsl:value-of select="collectionService/serviceSchedulingParams/schType"/></scheduling-type>
			<scheduling-time><xsl:value-of select="collectionService/serviceSchedulingParams/time"/></scheduling-time>
			<scheduling-day><xsl:value-of select="collectionService/serviceSchedulingParams/day"/></scheduling-day>
			<scheduling-date><xsl:value-of select="collectionService/serviceSchedulingParams/date"/></scheduling-date>
			</scheduling>
			<driver-list>
				<xsl:for-each select="collectionService/myDrivers">
				<xsl:if test="status != 'DELETED'">
				
				<driver>
					<application-order><xsl:value-of select="applicationOrder"/></application-order>
					<driver-name><xsl:value-of select="driverType/alias"/></driver-name>
					
					<enabled><xsl:choose><xsl:when test="status = 'INACTIVE'">false</xsl:when>
					<xsl:otherwise>true</xsl:otherwise>
					</xsl:choose></enabled>
					
					
				</driver>
				
				</xsl:if>
				</xsl:for-each>
			</driver-list>
		</collection-service>
		</xsl:if>
	</xsl:template>
</xsl:stylesheet>
