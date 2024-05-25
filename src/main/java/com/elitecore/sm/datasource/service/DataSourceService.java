/**
 * 
 */
package com.elitecore.sm.datasource.service;

import com.elitecore.sm.common.exception.SMException;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.datasource.model.DataSourceConfig;
import com.elitecore.sm.serverinstance.model.ServerInstance;

/**
 * @author Sunil Gulabani
 * Jul 30, 2015
 */
public interface DataSourceService {
	
	public ResponseObject addDataSourceConfig(DataSourceConfig defaultDSConfig);
	
	public ResponseObject getDSConfigList();
	
	public ResponseObject updateDataSourceConfigurationDetails(DataSourceConfig datasourceconfig);
	
	public ResponseObject deleteDataSourceDetails(int dataSourceId);

	public ResponseObject addNewDataSourceConfig(DataSourceConfig defaultDSConfig);
	
	public ResponseObject getDataSourceByName(String name);

	public ResponseObject getDataSourceListByType(String dsType);

	public ResponseObject createDataSourceForImport(DataSourceConfig exportedDataSource, ServerInstance serverInstanceDB) throws SMException;
	
	public void importDatasourceConfig(ServerInstance dbServerInstance, ServerInstance exportedServerInstance) throws SMException;
	
	public ResponseObject getDataBaseConnection(DataSourceConfig defaultDSConfig); 
	
	public void migrateDataSourcePassword();
	
}