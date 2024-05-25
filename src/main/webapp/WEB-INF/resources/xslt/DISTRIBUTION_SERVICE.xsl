<?xml version="1.0"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:output method="xml" indent="yes"></xsl:output>
<xsl:template match="/">
	<xsl:if test="distributionService/status != 'DELETED'">
		<distribution-service>
				<startup-mode><xsl:value-of select="distributionService/svcExecParams/startupMode"/></startup-mode>
				<sorting-type><xsl:value-of select="distributionService/svcExecParams/sortingType"/></sorting-type>
				<sorting-criteria><xsl:value-of select="distributionService/svcExecParams/sortingCriteria"/></sorting-criteria>
				<execution-interval><xsl:value-of select="distributionService/svcExecParams/executionInterval"/></execution-interval>	
				<third-party-transfer><xsl:value-of select="distributionService/thirdPartyTransferEnabled"/></third-party-transfer>
				<immediate-execute-on-startup><xsl:value-of select="distributionService/svcExecParams/executeOnStartup"/></immediate-execute-on-startup>
    			<minimum-thread><xsl:value-of select="distributionService/svcExecParams/minThread"/></minimum-thread>
				<maximum-thread><xsl:value-of select="distributionService/svcExecParams/maxThread"/></maximum-thread>
    		    <thread-queue-size><xsl:value-of select="distributionService/svcExecParams/queueSize"/></thread-queue-size>
    		    <file-batch-size><xsl:value-of select="distributionService/svcExecParams/fileBatchSize"/></file-batch-size>
    		    <timesten-datasource-name><xsl:value-of select="distributionService/timestenDatasourceName"/></timesten-datasource-name>
    		    <process-record-limit><xsl:value-of select="distributionService/processRecordLimit"/></process-record-limit>
    		    <write-record-limit>
    		    	<xsl:if test="distributionService/writeRecordLimit != '-1'">
    		    		<xsl:value-of select="distributionService/writeRecordLimit"/>
    		    	</xsl:if>
    		    </write-record-limit>
			    <!-- <file-merge><xsl:value-of select="distributionService/fileMergeEnabled"/></file-merge> -->
			    <file-merge>
					<xsl:attribute name="enabled"><xsl:value-of
					select="distributionService/fileMergeEnabled" /></xsl:attribute>
					<group-by><xsl:value-of select="distributionService/fileMergeGroupingBy" /></group-by>
					<remaining-file><xsl:value-of select="distributionService/remainingFileMergeEnabled" /></remaining-file>
				</file-merge>
				<error-path><xsl:value-of select="distributionService/errorPath"/></error-path>
				<driver-list>
					<xsl:for-each select="distributionService/myDrivers">
					<xsl:if test="status != 'DELETED'">
					<driver>
						<application-order><xsl:value-of select="applicationOrder"/></application-order>
						<driver-name><xsl:value-of select="driverType/alias"/></driver-name>
						<enabled>
							<xsl:choose>
								<xsl:when test="status = 'INACTIVE'">false</xsl:when>
								<xsl:otherwise>true</xsl:otherwise>
							</xsl:choose>
						</enabled>
					</driver>
					</xsl:if>
					</xsl:for-each>
				</driver-list>
				
				<scheduling>
					<xsl:attribute name="enabled">
					<xsl:value-of select="distributionService/serviceSchedulingParams/schedulingEnabled"/>
					</xsl:attribute>
					<scheduling-type><xsl:value-of select="distributionService/serviceSchedulingParams/schType"/></scheduling-type>
					<scheduling-time><xsl:value-of select="distributionService/serviceSchedulingParams/time"/></scheduling-time>
					<scheduling-day><xsl:value-of select="distributionService/serviceSchedulingParams/day"/></scheduling-day>
					<scheduling-date><xsl:value-of select="distributionService/serviceSchedulingParams/date"/></scheduling-date>
				</scheduling>
				
				<file-grouping><xsl:attribute name="enabled"><xsl:value-of select="distributionService/fileGroupingParameter/fileGroupEnable"/></xsl:attribute>
               	 	<grouping-type><xsl:value-of select="distributionService/fileGroupingParameter/groupingType"/></grouping-type>
                	<archive-path><xsl:value-of select="distributionService/fileGroupingParameter/archivePath"/></archive-path>
                	<sourcewise-archive><xsl:value-of select="distributionService/fileGroupingParameter/sourcewiseArchive"/></sourcewise-archive>
                	<grouping-date-type><xsl:value-of select="distributionService/fileGroupingParameter/groupingDateType"/></grouping-date-type>
        		</file-grouping>
        		
        		<xsl:choose>
					<xsl:when test="distributionService/fileCDRSummaryFlag = 'true' and distributionService/enableDBStats = 'true' ">
						<store-cdr-file-summary-in-db>true</store-cdr-file-summary-in-db>
					</xsl:when>
					<xsl:otherwise>
						<store-cdr-file-summary-in-db>false</store-cdr-file-summary-in-db>
					</xsl:otherwise>
				</xsl:choose>
        		
		</distribution-service>
	</xsl:if>
</xsl:template>
</xsl:stylesheet>



<!-- ========================================================== -->
<!-- Details of each configuration parameter                    -->
<!-- ========================================================== -->

<!--
<distribution-service>

    <startup-mode>Manual</startup-mode>
        Startup mode of the service, can be either Manual or Automatic. In case
        of Manual mode, the service will be added to the list but will not be
        started, but in case of Automatic the service is started automatically
        when the server is started.
        Change in value requires service restart.
        
    <is-sorting-required>true</is-sorting-required>
		Is sorting required can be either true or false. If it is true then collection service 
		will do collection according to the sort order. Sorting is based on last modified date.
    
    <execution-interval>1</execution-interval>
        Service will wait for these much time on next execution

	<is-compressed-input>false</is-compressed-input>
		We have a support of compressed input file in distribution service. If value of this tag is true then mediation server can
		able to read the .gz file according to the given extension.
	
    <immediate-execute-on-startup>true</immediate-execute-on-startup>
        If it is true then Service will start when it is called

    <source-path>C:\MEDIATION_ROOT\DISTRIBUTION_ROOT\input</source-path>
        Source folder from which the files are placed for distribution

    <source-file-types>.csv</source-file-types>
        Service has to take these types of files for processing only.

     <file-grouping enabled="false"> To enable file grouping
	<grouping-type>day</grouping-type>
		Service will insert files into archived folder in form of,
	        If Day, duplicate/Year/Month/Day/
    	    If Month, duplicate/Year/Month/
        	If Year, duplicate/Year/
	<for-archive>true</for-archive>
		To move or not file in archive.
	</file-grouping>

    <minimum-thread>5</minimum-thread>
        Minimum number of threads that should stay in the pool to handle the
        request. All the threads will be created during initilization.
        Change in value requires soft restart of the service after reloading
        the configuration.

    <maximum-thread>7</maximum-thread>
        Maximum number of threads that should be created during load.
        Change in value requires soft restart of the service after reloading
        the configuration.

    <thread-queue-size>15000</thread-queue-size>
        Maximum requests that can stay in the queue, any new request recevied
        when the queue is full will be dropped.
        Change in value requires soft restart of the service after reloading
        the configuration.

    <file-batch-size>100</file-batch-size>
        Service will pick up these much files to process at a time.
        
    <timesten-datasource-name></timesten-datasource-name>
    	Service will initialize data source name specified for this tag. Basically this
    	tag is useful for database distribution driver to provide timesten database support.
        
    <process-record-limit>500</process-record-limit>
		This much record will process at a time.
     
    <select-file-on-prefixes>new</select-file-on-prefixes>
		Only those file will parse who's prefix is new.

    <driver-list>
        List of dirvers configured for parlay charging service.

        <driver>
          Driver details.

            <application-order>1</application-order>
                The order in which the driver to be assigned the task.

            <driver-name>DATABASE_DRIVER</driver-name>
                The name of the driver, must match to any of the name available
                in service mapping file (system-mapping.xml).

            <timeout>2000</timeout>
                Timeout in milli seconds for driver process, if the driver fails to
                respond within this specified time, response will be given back
                without any ip allocation.

            <next-driver>false</next-driver>
                Can be used to specify whether to give the request that is successfully
                processed by this driver. If configured to true, on successful completion
                of process by this driver, the request will be given to next available
                driver and the response given by the next driver will be returned back
                to the client. In case next driver is not present, the response of current
                driver will be returned.

        </driver>

    </driver-list>

</distribution-service>
 -->

<!-- ========================================================== -->
<!-- End of configuration details                               -->
<!-- ========================================================== -->
