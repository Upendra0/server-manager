<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:output method="xml" indent="yes"></xsl:output>
	<xsl:template match="/">
	<xsl:variable name="serverInstanceId" select="serverInstance/id"/>
		<server-configuration>
			<server-id>
				<xsl:value-of select="serverInstance/server/serverId"></xsl:value-of>
			</server-id>
			<group-server-id>
                <xsl:value-of select="serverInstance/server/groupServerId"></xsl:value-of>
            </group-server-id>
			<service-list>
			<xsl:for-each select="serverInstance/services">
			<xsl:if test="status != 'DELETED'">
				<service>
					<service-name><xsl:value-of select="svctype/alias"></xsl:value-of></service-name>
					<service-instance-id><xsl:value-of select="servInstanceId"></xsl:value-of></service-instance-id>
					<service-instance-name><xsl:value-of select="name"></xsl:value-of></service-instance-name>
					<enabled><xsl:choose><xsl:when test="status = 'INACTIVE'">false</xsl:when>
					<xsl:otherwise>true</xsl:otherwise>
					</xsl:choose></enabled>
					<file-statistics><xsl:value-of select="enableFileStats"/></file-statistics>
				</service>
				</xsl:if>
			</xsl:for-each>
			</service-list>
			<logging>
				<log-level><xsl:value-of select="serverInstance/logsDetail/level"></xsl:value-of></log-level>
				<rolling><xsl:attribute name="type"><xsl:value-of select="serverInstance/logsDetail/rollingType"></xsl:value-of></xsl:attribute>
				<xsl:attribute name="value"><xsl:value-of select="serverInstance/logsDetail/rollingValue"></xsl:value-of></xsl:attribute>
				</rolling>
				<max-file-units><xsl:value-of select="serverInstance/logsDetail/maxRollingUnit"></xsl:value-of></max-file-units>
				<location><xsl:value-of select="serverInstance/logsDetail/logPathLocation"/></location>
			</logging>
			<mediation-root><xsl:value-of select="serverInstance/mediationRoot"/></mediation-root>
			<reprocessing-backup-path><xsl:value-of select="serverInstance/reprocessingBackupPath"/></reprocessing-backup-path>
			
			<xsl:choose>
			<xsl:when test="serverInstance/databaseInit = 'true'">
			<database-ds init="true">
				<servermanager-datasource-name><xsl:value-of select="serverInstance/serverManagerDatasourceConfig/name"></xsl:value-of></servermanager-datasource-name>
				<iplogger-datasource-name><xsl:value-of select="serverInstance/iploggerDatasourceConfig/name"></xsl:value-of></iplogger-datasource-name>
				<datasource-type>
				<xsl:choose>
				 <xsl:when test="serverInstance/serverManagerDatasourceConfig/type = '1'">
				 <xsl:text>oracle</xsl:text>
				 </xsl:when>
				  <xsl:when test="serverInstance/serverManagerDatasourceConfig/type = '2'">
				 <xsl:text>PostgreSql</xsl:text>
				 </xsl:when>
				  <xsl:when test="serverInstance/serverManagerDatasourceConfig/type = '3'">
				 <xsl:text>MySql</xsl:text>
				 </xsl:when>
				</xsl:choose>
				</datasource-type>
			</database-ds>
			</xsl:when>
			<xsl:otherwise>
				<database-ds init="false"> 
				<servermanager-datasource-name><xsl:value-of select="serverInstance/serverManagerDatasourceConfig/name"></xsl:value-of></servermanager-datasource-name>
				<iplogger-datasource-name><xsl:value-of select="serverInstance/iploggerDatasourceConfig/name"></xsl:value-of></iplogger-datasource-name>
				<datasource-type>
				<xsl:choose>
				 <xsl:when test="serverInstance/serverManagerDatasourceConfig/type = '1'">
				 <xsl:text>oracle</xsl:text>
				 </xsl:when>
				  <xsl:when test="serverInstance/serverManagerDatasourceConfig/type = '2'">
				 <xsl:text>PostgreSql</xsl:text>
				 </xsl:when>
				  <xsl:when test="serverInstance/serverManagerDatasourceConfig/type = '3'">
				 <xsl:text>MySql</xsl:text>
				 </xsl:when>
				</xsl:choose>
				</datasource-type>
				</database-ds>
			</xsl:otherwise>
			</xsl:choose>
			
			<file-statistics-path><xsl:value-of select="serverInstance/fileStorageLocation"/></file-statistics-path>
			<store-cdr-file-summary-in-db><xsl:value-of select="serverInstance/fileCdrSummaryDBEnable"></xsl:value-of></store-cdr-file-summary-in-db>
			
			<xsl:choose>
			<xsl:when test="count(serverInstance/agentList) > '1' ">
			<xsl:for-each select="serverInstance/agentList">
			 <xsl:if test="agentType/alias != 'PACKET_STATISTICS_AGENT'">
			<xsl:choose>
			<xsl:when test="agentType/alias = 'FILE_DISTRIBUTION_AGENT'">
			<file-rename-agent>false</file-rename-agent>
			<file-distribution-agent>true</file-distribution-agent>
			<database-reprocessing-agent>false</database-reprocessing-agent></xsl:when>
			<xsl:when test="agentType/alias = 'DATABASE_REPROCESSING_AGENT'">
			<file-rename-agent>false</file-rename-agent>
			<file-distribution-agent>false</file-distribution-agent>
			<database-reprocessing-agent>true</database-reprocessing-agent>c
			</xsl:when>
			<xsl:when test="agentType/alias = 'FILE_RENAME_AGENT'">
			<xsl:choose>
               <xsl:when test="status = 'ACTIVE'">
                     <file-rename-agent>true</file-rename-agent>
                        </xsl:when>
                          <xsl:otherwise>
                           <file-rename-agent>false</file-rename-agent>
                             </xsl:otherwise>
                               </xsl:choose>
			<!-- <file-rename-agent>true</file-rename-agent> -->
			<file-distribution-agent>false</file-distribution-agent>
    		<database-reprocessing-agent>false</database-reprocessing-agent></xsl:when>
			</xsl:choose>
			</xsl:if>
			</xsl:for-each>
			</xsl:when>
			<xsl:otherwise>
			<file-rename-agent>false</file-rename-agent>
			<file-distribution-agent>false</file-distribution-agent>
			<database-reprocessing-agent>false</database-reprocessing-agent>
			</xsl:otherwise>
			</xsl:choose>
			<web-service-status><xsl:choose><xsl:when test="serverInstance/webservicesEnable = 'false'">OFF</xsl:when>
					<xsl:otherwise>ON</xsl:otherwise>
					</xsl:choose></web-service-status>
			<rest-web-service-status><xsl:choose><xsl:when test="serverInstance/restWebservicesEnable = 'false'">OFF</xsl:when>
					<xsl:otherwise>ON</xsl:otherwise>
					</xsl:choose></rest-web-service-status>
			<minimum-disk-space-required><xsl:value-of select="serverInstance/minDiskSpace"></xsl:value-of></minimum-disk-space-required>
			<alerts>
				<snmp-alert><xsl:choose><xsl:when test="serverInstance/snmpAlertEnable = 'false'">OFF</xsl:when>
					<xsl:otherwise>ON</xsl:otherwise>
					</xsl:choose></snmp-alert>
					<xsl:choose><xsl:when test="serverInstance/thresholdSysAlertEnable = 'false'">
					<threshold-system-alert enable="false">
					<time-interval><xsl:value-of select="serverInstance/thresholdTimeInterval"></xsl:value-of></time-interval>
					<memory><xsl:value-of select="serverInstance/thresholdMemory"></xsl:value-of></memory>
					<load-average><xsl:value-of select="serverInstance/loadAverage"></xsl:value-of></load-average>
					</threshold-system-alert>
					</xsl:when>
					<xsl:otherwise>
					<threshold-system-alert enable="true">
					<time-interval><xsl:value-of select="serverInstance/thresholdTimeInterval"></xsl:value-of></time-interval>
					<memory><xsl:value-of select="serverInstance/thresholdMemory"></xsl:value-of></memory>
					<load-average><xsl:value-of select="serverInstance/loadAverage"></xsl:value-of></load-average>
					</threshold-system-alert>
					</xsl:otherwise>
					</xsl:choose>
			</alerts>
		</server-configuration>
	</xsl:template>
</xsl:stylesheet>
