<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:output method="xml" indent="yes"></xsl:output>
	<xsl:template match="/">
		<datasources>
			<datasource>
				<datasource-name>
					<xsl:value-of select="serverInstance/engineDatasourceConfig/name"></xsl:value-of>
				</datasource-name>
				<datasource-type>
					<xsl:value-of select="serverInstance/engineDatasourceConfig/type"></xsl:value-of>
				</datasource-type>
		        <connection-url>
		        	<xsl:value-of select="serverInstance/engineDatasourceConfig/connURL"></xsl:value-of>
		        </connection-url>
		        <user-name>
		        	<xsl:value-of select="serverInstance/engineDatasourceConfig/username"></xsl:value-of>
		        </user-name>
		        <password>	
		        	<xsl:value-of select="serverInstance/engineDatasourceConfig/password"></xsl:value-of>
		        </password>
		        <minimum-pool-size>
		        	<xsl:value-of select="serverInstance/engineDatasourceConfig/maxPoolsize"></xsl:value-of>
		        </minimum-pool-size>
		        <maximum-pool-size>
		        	<xsl:value-of select="serverInstance/engineDatasourceConfig/maxPoolsize"></xsl:value-of>
		        </maximum-pool-size>
		        <failover-timeout>
		        	<xsl:value-of select="serverInstance/engineDatasourceConfig/failTimeout"></xsl:value-of>
		        </failover-timeout>
			</datasource>
			
			<datasource>
				<datasource-name>
					<xsl:value-of select="serverInstance/serverManagerDatasourceConfig/name"></xsl:value-of>
				</datasource-name>
				<datasource-type>
					<xsl:value-of select="serverInstance/serverManagerDatasourceConfig/type"></xsl:value-of>
				</datasource-type>
		        <connection-url>
		        	<xsl:value-of select="serverInstance/serverManagerDatasourceConfig/connURL"></xsl:value-of>
		        </connection-url>
		        <user-name>
		        	<xsl:value-of select="serverInstance/serverManagerDatasourceConfig/username"></xsl:value-of>
		        </user-name>
		        <password>	
		        	<xsl:value-of select="serverInstance/serverManagerDatasourceConfig/password"></xsl:value-of>
		        </password>
		        <minimum-pool-size>
		        	<xsl:value-of select="serverInstance/serverManagerDatasourceConfig/maxPoolsize"></xsl:value-of>
		        </minimum-pool-size>
		        <maximum-pool-size>
		        	<xsl:value-of select="serverInstance/serverManagerDatasourceConfig/maxPoolsize"></xsl:value-of>
		        </maximum-pool-size>
		        <failover-timeout>
		        	<xsl:value-of select="serverInstance/serverManagerDatasourceConfig/failTimeout"></xsl:value-of>
		        </failover-timeout>
			</datasource>
			
			<datasource>
				<datasource-name>
					<xsl:value-of select="serverInstance/iploggerDatasourceConfig/name"></xsl:value-of>
				</datasource-name>
				<datasource-type>
					<xsl:value-of select="serverInstance/iploggerDatasourceConfig/type"></xsl:value-of>
				</datasource-type>
		        <connection-url>
		        	<xsl:value-of select="serverInstance/iploggerDatasourceConfig/connURL"></xsl:value-of>
		        </connection-url>
		        <user-name>
		        	<xsl:value-of select="serverInstance/iploggerDatasourceConfig/username"></xsl:value-of>
		        </user-name>
		        <password>	
		        	<xsl:value-of select="serverInstance/iploggerDatasourceConfig/password"></xsl:value-of>
		        </password>
		        <minimum-pool-size>
		        	<xsl:value-of select="serverInstance/iploggerDatasourceConfig/maxPoolsize"></xsl:value-of>
		        </minimum-pool-size>
		        <maximum-pool-size>
		        	<xsl:value-of select="serverInstance/iploggerDatasourceConfig/maxPoolsize"></xsl:value-of>
		        </maximum-pool-size>
		        <failover-timeout>
		        	<xsl:value-of select="serverInstance/iploggerDatasourceConfig/failTimeout"></xsl:value-of>
		        </failover-timeout>
			</datasource>
		</datasources>	
	</xsl:template>
</xsl:stylesheet>