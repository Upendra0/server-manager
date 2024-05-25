/**
 * 
 */
package com.elitecore.sm.datasource.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.elitecore.sm.common.aspect.Auditable;
import com.elitecore.sm.common.constants.AuditConstants;
import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.exception.SMException;
import com.elitecore.sm.common.model.StateEnum;
import com.elitecore.sm.common.util.ResponseCode;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.datasource.dao.DataSourceDao;
import com.elitecore.sm.datasource.model.DataSourceConfig;
import com.elitecore.sm.serverinstance.dao.ServerInstanceDao;
import com.elitecore.sm.serverinstance.model.ServerInstance;
import com.elitecore.sm.util.EliteUtils;

/**
 * @author Sunil Gulabani
 * Jul 30, 2015
 */
@Service(value = "dataSourceService")
public class DataSourceServiceImpl implements DataSourceService{
	
	private Logger logger = Logger.getLogger(this.getClass().getName());
	
	@Autowired
	private DataSourceDao dataSourceDao;
	
	@Autowired
	private ServerInstanceDao serverInstanceDao;
	
	/**
	 * Add's the Datasource into database while creating new server Instance
	 */
	@Override
	@Transactional
	//@Auditable(auditActivity = AuditConstants.CREATE_DATASOURCE, actionType = BaseConstants.CREATE_ACTION, currentEntity = DataSourceConfig.class, ignorePropList= "")
	public ResponseObject addDataSourceConfig(DataSourceConfig defaultDSConfig) {
		ResponseObject responseObject = new ResponseObject();
		//Check for Unique Datasource
		dataSourceDao.save(defaultDSConfig);
		if(defaultDSConfig.getId() !=0){
			responseObject.setSuccess(true);
			responseObject.setResponseCode(ResponseCode.DATA_SOURCE_INSERT_SUCCESS);
		}else{
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.DATA_SOURCE_INSERT_FAIL);
		}
		return responseObject;
	}
	
	/**
	 * Add's the Datasource into database while creating new DS explicitly from GUI
	 */
	@Override
	@Transactional
	@Auditable(auditActivity = AuditConstants.CREATE_DATASOURCE, actionType = BaseConstants.CREATE_ACTION, currentEntity = DataSourceConfig.class, ignorePropList= "")	
	public ResponseObject addNewDataSourceConfig(DataSourceConfig defaultDSConfig) {
		ResponseObject responseObject = new ResponseObject();
		//check if same DataSource Name already exits in DB
		if(checkForDuplicateDSName(defaultDSConfig.getName())){
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.DATASOURCE_ADD_FAIL_DUPLICATE_DATASOURCE_NAME);
			return responseObject;
		}
		List<DataSourceConfig> dsConfigList = dataSourceDao.getDataBaseConfigurationByUrlAndUsernameAndType(defaultDSConfig.getUsername(), defaultDSConfig.getConnURL(),defaultDSConfig.getType());
		boolean dsConfigExists = false;
		if (dsConfigList != null && !dsConfigList.isEmpty()){
			//Data Source with same user name and URL already exist in DB
			dsConfigExists= true;
			
		}else{
			//Check for Unique Data-source
			if(defaultDSConfig.getPassword()!=null) {
				defaultDSConfig.setPassword(EliteUtils.encryptData(defaultDSConfig.getPassword()));
			}
			dataSourceDao.save(defaultDSConfig);
		}
		if(defaultDSConfig.getId() !=0){
			responseObject.setSuccess(true);
			responseObject.setObject(defaultDSConfig);
			responseObject.setResponseCode(ResponseCode.DATA_SOURCE_INSERT_SUCCESS);
		}else if(!dsConfigExists){
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.DATA_SOURCE_INSERT_FAIL);
		}else{
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.DATASOURCE_ADD_FAIL_DUPLICATE_URL_USERNAME);
		}
		return responseObject;
	}
	
	private boolean checkForDuplicateDSName(String dataSourceName){
		boolean duplicateExists = false;
		List<DataSourceConfig> dsConfigList = dataSourceDao.getDataBaseConfigurationName(dataSourceName);
		if(dsConfigList!=null && !dsConfigList.isEmpty())
			duplicateExists = true;
		
		return duplicateExists;
	}
	/**
	 * Fetch DataSource Details  for synchronization
	 */
	@Override
	@Transactional(readOnly = true)
	public ResponseObject getDSConfigList() {
		
		ResponseObject responseObject = new ResponseObject();
		
		List<DataSourceConfig> dsConfigList = dataSourceDao.getAllObject();
		
		if (dsConfigList != null){
			responseObject.setSuccess(true);
			responseObject.setObject(dsConfigList);
		} else {
			responseObject.setSuccess(false);
		}
		
		return responseObject;
	}
	/**
	 * Update the DataSource Configuration Details  
	 * 
	 */
	
	@Override
	@Transactional
	@Auditable(auditActivity = AuditConstants.UPDATE_DATASOURCE, actionType = BaseConstants.UPDATE_ACTION, currentEntity = DataSourceConfig.class, ignorePropList= "")
	public ResponseObject updateDataSourceConfigurationDetails(DataSourceConfig dataSourceConfig) {

		ResponseObject responseObject = new ResponseObject();
	
		if(isDataSourceUniqueForUpdate(dataSourceConfig.getId(), dataSourceConfig.getName()))
		{	
			
			List<DataSourceConfig> dsConfigList = dataSourceDao.getDataBaseConfigurationByUrlAndUsernameAndType(dataSourceConfig.getUsername(), dataSourceConfig.getConnURL(),dataSourceConfig.getType());
			boolean dsConfigExists = false;
			if (dsConfigList != null && !dsConfigList.isEmpty()){
				//Data Source with same user name and URL already exist in DB
				if(dsConfigList.size()==1 && dsConfigList.get(0).getId()==dataSourceConfig.getId()){
					dsConfigExists= false;
				}
				else{
					dsConfigExists= true;
				}
				
				
			}
			if(!dsConfigExists){
			dataSourceConfig.setLastUpdatedByStaffId(dataSourceConfig.getLastUpdatedByStaffId());
			dataSourceConfig.setLastUpdatedDate(new Date());
			if(dataSourceConfig.getPassword()!=null) {
				dataSourceConfig.setPassword(EliteUtils.encryptData(dataSourceConfig.getPassword()));
			}
			dataSourceDao.merge(dataSourceConfig);			
			logger.info("DataSourceConfiguration  details updated successfully.");	
			responseObject.setSuccess(true);
			responseObject.setResponseCode(ResponseCode.DATASOURCE_UPDATE_SUCCESS);
			responseObject.setObject(dataSourceConfig);
			}
			else{
				logger.debug("inside updatedataSourceConfiguration : duplicate dataSourceConfiguration url and username combination with dbType found in update:" + dataSourceConfig.getName());
				responseObject.setSuccess(false);
				responseObject.setResponseCode(ResponseCode.DATASOURCE_ADD_FAIL_DUPLICATE_URL_USERNAME);
			}
			
		}
		else
		{
			logger.debug("inside updatedataSourceConfiguration : duplicate dataSourceConfiguration name found in update:" + dataSourceConfig.getName());
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.DUPLICATE_DATASOURCE_NAME);
			
		}

		
		return responseObject;
	}
	
	/**
	 * Check dataSource name is unique in case of update 
	 * @param dataSourceIdPathList
	 * @param datasourceName
	 * @return ResponseObject
	 */
	@Transactional
	public boolean  isDataSourceUniqueForUpdate(int dataSourceConfigId,String dataSourceName){
		List<DataSourceConfig>  dataSourceConfigList=dataSourceDao.getDataBaseConfigurationName(dataSourceName);
		boolean isUnique=false;
		if(dataSourceConfigList!=null && !dataSourceConfigList.isEmpty()){
			for( DataSourceConfig dataSourceConfig:dataSourceConfigList){
				//If ID is same , then it is same DS object
				if(dataSourceConfigId == (dataSourceConfig.getId())){
					isUnique=true;
				}else{ // It is another DS object , but name is same
					isUnique=false;
				}
			}
		}else if(dataSourceConfigList!=null && dataSourceConfigList.isEmpty()){ // No DS found with same name 
			isUnique=true;
		}
		
		return isUnique;
	}

	/**
	 * 	Method Delete the DataSourceConfiguration Details 
	 *  
	 */
	
	@Override
	@Transactional
	@Auditable(auditActivity = AuditConstants.DELETE_DATASOURCE, actionType = BaseConstants.DELETE_ACTION, currentEntity = DataSourceConfig.class, ignorePropList= "")
	public ResponseObject deleteDataSourceDetails(int dataSourceId)  {
		ResponseObject responseObject = new ResponseObject();
		DataSourceConfig dsConfigList = dataSourceDao.findByPrimaryKey(DataSourceConfig.class, dataSourceId);
		if(dsConfigList !=null){
			/*Check if there is no server ID bound to this DSID*/

			List<ServerInstance> serverInstanceList = serverInstanceDao.getServerInstanceListByAssociatedDSId(dataSourceId);
			if(serverInstanceList != null && !serverInstanceList.isEmpty()){
				//if active server instances are still pointing to same DB
				//nothing to do with DSConfig entry
				responseObject.setSuccess(false);
				responseObject.setResponseCode(ResponseCode.DATASOURCE_DELETE_FAIL_SERVER_MAPPING_EXISTS);
				
			}else{
				//mark the status for the DSID as DELETED
				
				dsConfigList.setName(EliteUtils.checkForNames(BaseConstants.ACTION_DELETE,dsConfigList.getName()));
				dsConfigList.setStatus(StateEnum.DELETED);
				dsConfigList.setLastUpdatedByStaffId(dsConfigList.getLastUpdatedByStaffId());
				dsConfigList.setLastUpdatedDate(new Date());

				dataSourceDao.merge(dsConfigList);

				responseObject.setSuccess(true);
				responseObject.setObject(dsConfigList);
				responseObject.setResponseCode(ResponseCode.DATASOURCE_DELETE_SUCCESS);
			}
		}else{
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.DATASOURCE_DELETE_FAIL);
		}
		return responseObject;
	}

	/**
	 * 	Method will get Datasource object by Name.
	 * @param name 
	 */
	@Override
	@Transactional
	public ResponseObject getDataSourceByName(String name) {
		ResponseObject responseObject = new ResponseObject();
		List<DataSourceConfig> dataSourceList =  dataSourceDao.getDataBaseConfigurationName(name);
		if(dataSourceList != null && !dataSourceList.isEmpty()){
			responseObject.setSuccess(true);
			responseObject.setObject(dataSourceList.get(0));
		}else{
			responseObject.setSuccess(false);
		}
		return responseObject;
	}
	
	/**
	 * 	Method will get Datasource object by Type.
	 * @param name 
	 */
	@Override
	@Transactional
	public ResponseObject getDataSourceListByType(String dsType) {
		ResponseObject responseObject = new ResponseObject();
		List<DataSourceConfig> dataSourceList =  dataSourceDao.getDataBaseConfigurationByDSType(dsType);
		if(dataSourceList != null && !dataSourceList.isEmpty()){
			responseObject.setSuccess(true);
			responseObject.setObject(dataSourceList);
		}else{
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.NO_DS_BY_THIS_TYPE);
			String dsTypeName;
			if(("1").equalsIgnoreCase(dsType))
			{
				dsTypeName="ORACLE";
			}
			else if(("3").equalsIgnoreCase(dsType))
			{
				dsTypeName="MYSQL";
			}
			else
			{
				dsTypeName="POSTGRESQL";
			}
			responseObject.setArgs(new Object[] { dsTypeName });
		}
		return responseObject;
	}
	
	/**
	 * Method will check if datasource with same config exists or not, if not will create new DS
	 * @param exportedDataSource
	 * @param serverInstanceDB
	 * @return ResponseObject
	 */
	@Override
	@Transactional
	public ResponseObject createDataSourceForImport(DataSourceConfig exportedDataSource,ServerInstance serverInstanceDB) throws SMException {
		ResponseObject responseObject = new ResponseObject();
		List<DataSourceConfig> dsConfigList = dataSourceDao.getDataBaseConfigurationByUrlAndUsernameAndType(exportedDataSource.getUsername(), exportedDataSource.getConnURL(),exportedDataSource.getType());
		if (dsConfigList != null && !dsConfigList.isEmpty()){
			responseObject.setObject(dsConfigList.get(0));
			responseObject.setSuccess(true);
		}
		else{
			exportedDataSource.setId(0);
			exportedDataSource.setName(EliteUtils.checkForNames(BaseConstants.ACTION_IMPORT,exportedDataSource.getName()));
			exportedDataSource.setLastUpdatedByStaffId(serverInstanceDB.getCreatedByStaffId());
			exportedDataSource.setCreatedByStaffId(serverInstanceDB.getCreatedByStaffId());
			exportedDataSource.setCreatedDate(new Date());
			boolean isHexadecimal = EliteUtils.isHexadecimal(exportedDataSource.getPassword());
			if(!isHexadecimal){
				String password = exportedDataSource.getPassword();
				exportedDataSource.setPassword(EliteUtils.encryptData(password));
			}
			dataSourceDao.save(exportedDataSource);
			responseObject.setObject(exportedDataSource);
			responseObject.setSuccess(true);
		}
		return responseObject;
	}
	
	@Override
	public void importDatasourceConfig(ServerInstance dbServerInstance, ServerInstance exportedServerInstance) throws SMException {
		ResponseObject responseObject;
		if(dbServerInstance.getServerManagerDatasourceConfig() == null 
				&& exportedServerInstance.getServerManagerDatasourceConfig() != null) {
			responseObject = createDataSourceForImport(exportedServerInstance.getServerManagerDatasourceConfig(), dbServerInstance);//NOSONAR
			if(responseObject.isSuccess() && responseObject.getObject()!=null){
				dbServerInstance.setServerManagerDatasourceConfig((DataSourceConfig) responseObject.getObject());
			}
		}
		if("IPLMS".equalsIgnoreCase(dbServerInstance.getServer().getServerType().getAlias()) 
				&& dbServerInstance.getIploggerDatasourceConfig() == null 
				&& exportedServerInstance.getIploggerDatasourceConfig() != null) {
			responseObject = createDataSourceForImport(exportedServerInstance.getIploggerDatasourceConfig(), dbServerInstance);//NOSONAR
			if(responseObject.isSuccess() && responseObject.getObject()!=null){
				dbServerInstance.setIploggerDatasourceConfig((DataSourceConfig) responseObject.getObject());
			}
		}
	}

	@SuppressWarnings("unused")
	@Override
	@Transactional
	public ResponseObject getDataBaseConnection(DataSourceConfig defaultDSConfig)   {
		ResponseObject responseObject = new ResponseObject();
		java.sql.Connection connection=null;
				List<DataSourceConfig> dsConfigList = dataSourceDao.getDataBaseConfigurationByUrlAndUsernameAndType(defaultDSConfig.getUsername(), defaultDSConfig.getConnURL(),defaultDSConfig.getType());
				boolean dsConfigExists = false;
				if (dsConfigList != null && !dsConfigList.isEmpty()){
					//Data Source with same user name and URL already exist in DB
					dsConfigExists= true;
				}
		try{
			String url=defaultDSConfig.getConnURL();
			String userName=defaultDSConfig.getUsername();
			String password=defaultDSConfig.getPassword();
			String dbString="";
			if(("1").equals(defaultDSConfig.getType())){
				dbString="ORACLE";
				Class.forName(BaseConstants.ORACLE_JDBS_DRIVER_ORACLEDRIVER);
			}else if(("2").equals(defaultDSConfig.getType())){
				dbString="POSTGRESQL";
				Class.forName(BaseConstants.ORG_POSTGRESQL_DRIVER);

			}else{
				dbString="MYSQL";
				Class.forName(BaseConstants.COM_MYSQL_JDBC_DRIVER);
			}
			
			ExecutorService executor = Executors.newSingleThreadExecutor();
			Future<Connection> future = executor.submit(new Callable<Connection>() {

			    @Override
			    public Connection call() throws Exception {
			        Connection connection = DriverManager.getConnection(url, userName, password);
			        return connection;
			    }
			});
			connection=future.get(10, TimeUnit.SECONDS);
			String subString=urlString(url);
			if(connection!=null&&dbString.equalsIgnoreCase(subString)){
				responseObject.setSuccess(true);
				responseObject.setResponseCode(ResponseCode.DATA_SOURCE_CONNECTED_SUCCESS);
			}else if(!dsConfigExists){
				responseObject.setSuccess(false);
				responseObject.setResponseCode(ResponseCode.DATA_SOURCE_NOT_CONNECTED);
			}
		}catch(Exception exception){//NOSONAR
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.DATA_SOURCE_NOT_CONNECTED);
		} 
		finally {
				if(connection!=null)
				{
					try {
						connection.close();
					} catch (SQLException e) {
						logger.error(e);
					}
				}
		}
		return  responseObject;
	}
	
	@Transactional
	@Override
	public void migrateDataSourcePassword(){
		ResponseObject responseObject = this.getDSConfigList();
		if(responseObject.isSuccess()) {
			List<DataSourceConfig> listDataSourceConfigs = (List<DataSourceConfig>) responseObject.getObject();
			for(DataSourceConfig config : listDataSourceConfigs){
				if(config.getPassword()!=null){
					config.setPassword(EliteUtils.encryptData(config.getPassword()));
					dataSourceDao.merge(config);
				}
			}
		}
	}
	
	private String urlString(String url){
		String str[]=url.split(":");
		return str[1];
		
	}
}