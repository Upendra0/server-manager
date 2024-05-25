/**
 * 
 */
package com.elitecore.sm.datasource.dao;

import java.util.List;

import com.elitecore.sm.common.dao.GenericDAO;
import com.elitecore.sm.datasource.model.DataSourceConfig;

/**
 * @author Sunil Gulabani
 * Jul 30, 2015
 */
public interface DataSourceDao extends GenericDAO<DataSourceConfig> {
	
	public List<DataSourceConfig> getAllObject();
	
	public List<DataSourceConfig> getDataBaseConfigurationName(String dbConfigurationName);

	void merge(DataSourceConfig dataSourceConfig);

	public List<DataSourceConfig> getDataBaseConfigurationByDSType(String dsType);

	public List<DataSourceConfig> getDataBaseConfigurationByUrlAndUsernameAndType(String dbUserName, String dbUrl, String dbType);
	
}
