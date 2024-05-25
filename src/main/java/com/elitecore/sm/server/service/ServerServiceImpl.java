/**
 * 
 */
package com.elitecore.sm.server.service;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.elitecore.sm.common.aspect.Auditable;
import com.elitecore.sm.common.constants.AuditConstants;
import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.constants.SystemParametersConstant;
import com.elitecore.sm.common.model.StateEnum;
import com.elitecore.sm.common.util.ResponseCode;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.integration.engine.RemoteJMXHelper;
import com.elitecore.sm.license.service.LicenseService;
import com.elitecore.sm.nfv.commons.constants.NFVResponseCode;
import com.elitecore.sm.server.dao.ServerDao;
import com.elitecore.sm.server.model.Server;
import com.elitecore.sm.server.model.ServerType;
import com.elitecore.sm.serverinstance.model.ServerInstance;
import com.elitecore.sm.serverinstance.service.ServerInstanceService;
import com.elitecore.sm.util.MapCache;

/**
 * @author Sunil Gulabani Jul 2, 2015
 */
@org.springframework.stereotype.Service(value = "serverService")
public class ServerServiceImpl implements ServerService {
	
	private Logger logger = Logger.getLogger(this.getClass().getName());
	
	@Autowired
	LicenseService	licenservice;
	
	@Autowired
	ServletContext servletContext;  // to get class path location

	@Autowired
	private ServerDao serverDao;
	
	@Autowired
	private ServerInstanceService serverInstanceService;
	

	/**
	 * Get's the All Server available in database.
	 */
	@Override
	@Transactional(readOnly = true)
	public List<Server> getServerList() {
		return serverDao.getServerList();
	}

	/**
	 * Adds the Server in database.
	 */
	@Override
	@Transactional
	@Auditable(auditActivity = AuditConstants.CREATE_SERVER,actionType = BaseConstants.CREATE_ACTION , currentEntity = Server.class, ignorePropList= "")
	public ResponseObject addServer(Server server) {
		ResponseObject responseObject = new ResponseObject();
		
		try {
			if (serverDao.getServerCount(server.getName(), server.getIpAddress(), 0) > 0) {
				responseObject.setSuccess(false);
				responseObject.setResponseCode(ResponseCode.DUPLICATE_SERVER);
			}
			/*else if (!server.isContainerEnvironment() && serverDao.getServerCountByIpAndType(server.getIpAddress(), server.getServerType().getId(), 0) > 0) {
				responseObject.setSuccess(false);
				responseObject.setResponseCode(ResponseCode.DUPLICATE_SERVER_SAME_TYPE);
			}*/
			else if (/*server.isContainerEnvironment() && */serverDao.getServerCountByIpAndTypeAndUtilityPort(server.getIpAddress(),
					server.getServerType().getId(), 0, server.getUtilityPort()) > 0) {
				responseObject.setSuccess(false);
				responseObject.setResponseCode(ResponseCode.DUPLICATE_SERVER_SAME_TYPE_CONTAINER);
			}
			else {				
				if(Boolean.TRUE.toString().equals((String)MapCache.getConfigValueAsObject(BaseConstants.KUBERNETES_ENV))) {
					server.setCreatedDate(new Date());
					server.setLastUpdatedDate(server.getCreatedDate());
					server.setLastUpdatedByStaffId(server.getCreatedByStaffId());
					server.setStatus(StateEnum.ACTIVE);
					
					serverDao.save(server);
					if (server.getId() !=0) {
						
						responseObject.setSuccess(true);
						responseObject.setResponseCode(ResponseCode.SERVER_INSERT_SUCCESS);
						
					} else {
						responseObject.setSuccess(false);
						responseObject.setResponseCode(ResponseCode.SERVER_INSERT_FAIL);
						responseObject.setResponseCodeNFV(NFVResponseCode.SERVER_INSERT_FAIL);
					}
					responseObject.setObject(server);
				}else {
					// Check if port avaliable or not
					RemoteJMXHelper serverMgmtRemoteJMXCall = new RemoteJMXHelper(server.getIpAddress(), server.getUtilityPort(),3,5000,20);
					String crestelHome = serverMgmtRemoteJMXCall.getCrestelPEngineHome();
					if(crestelHome != null ){
						server.setCreatedDate(new Date());
						server.setLastUpdatedDate(server.getCreatedDate());
						server.setLastUpdatedByStaffId(server.getCreatedByStaffId());
						server.setStatus(StateEnum.ACTIVE);
						
						serverDao.save(server);
						if (server.getId() !=0) {
							
							responseObject.setSuccess(true);
							responseObject.setResponseCode(ResponseCode.SERVER_INSERT_SUCCESS);
							
							// Apply default full license for engine
							boolean isLicenseApplied = serverMgmtRemoteJMXCall.activateDefaultFullLicence();
							
						} else {
							responseObject.setSuccess(false);
							responseObject.setResponseCode(ResponseCode.SERVER_INSERT_FAIL);
							responseObject.setResponseCodeNFV(NFVResponseCode.SERVER_INSERT_FAIL);
						}
						responseObject.setObject(server);
						
					}else if (serverMgmtRemoteJMXCall.getErrorMessage() != null){
						
						responseObject.setSuccess(false);
						responseObject.setResponseCode(ResponseCode.P_ENGINE_NOT_RUNNING);
						responseObject.setResponseCodeNFV(NFVResponseCode.P_ENGINE_NOT_RUNNING);
						responseObject.setArgs(new Object[] {server.getIpAddress(),String.valueOf(server.getUtilityPort())});
						responseObject.setModuleName(BaseConstants.SERVERINSTANCE);
					}
				}
			}
			//MED-8664 : start : show message for license container limit exceeds at the time of creation of server.
			//if(server.isContainerEnvironment()){
				long createdContainers = 0;
				ResponseObject responseObject1=getServerByHostAddress(server.getIpAddress());
				if (responseObject1.isSuccess()) {
					createdContainers = (long) responseObject1.getObject();
				}
				String hostIPName = server.getIpAddress().replaceAll("\\.", "_");
				String repositoryPath = servletContext.getRealPath(BaseConstants.LICENSE_PATH)+File.separator;
				String containerLicPath = repositoryPath + hostIPName + File.separator;
				int licensedContainers = licenservice.getLicensedContainerForHost(repositoryPath , containerLicPath);
				logger.debug("licensedContainers : "+licensedContainers);
				if(createdContainers > licensedContainers){
					responseObject.setModuleName(BaseConstants.LICENSE_WARNING);
				}
			// }
			//MED-8664 : end
			return responseObject;
		}catch (HibernateException e) {
			logger.error("hibernate issue in add server",e);			
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.SERVER_INSERT_FAIL);
			return responseObject;
		} catch (Exception e) {
			logger.error("generic issue in add server",e);		
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.SERVER_INSERT_FAIL);
			return responseObject;
		}
	}

	/**
	 * Provides the Server based on id
	 */
	@Override
	@Transactional(readOnly = true)
	public Server getServer(int id) {
		return serverDao.findByPrimaryKey(Server.class, id);
	}

	/**
	 * Updates the Server details
	 */
	@Override
	@Transactional
	@Auditable(auditActivity = AuditConstants.UPDATE_SERVER,actionType = BaseConstants.UPDATE_ACTION, currentEntity = Server.class, ignorePropList= "")
	public ResponseObject updateServer(Server server) {
		ResponseObject responseObject = new ResponseObject();
		if (serverDao.getServerCount(server.getName(), server.getIpAddress(), server.getId()) > 0) {
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.DUPLICATE_SERVER);
		}
		/*else if (!server.isContainerEnvironment() && serverDao.getServerCountByIpAndType(server.getIpAddress(), server.getServerType().getId(), server.getId()) > 0) {
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.DUPLICATE_SERVER_SAME_TYPE);
		}*/
		else if (/*server.isContainerEnvironment() &&*/ serverDao.getServerCountByIpAndTypeAndUtilityPort(server.getIpAddress(),
				server.getServerType().getId(), server.getId(), server.getUtilityPort()) > 0) {
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.DUPLICATE_SERVER_SAME_TYPE_CONTAINER);
		}
		else {
			RemoteJMXHelper serverMgmtRemoteJMXCall = new RemoteJMXHelper(server.getIpAddress(), server.getUtilityPort(),
					3,5000,20);
			String crestelHome = serverMgmtRemoteJMXCall.getCrestelPEngineHome();
			if(crestelHome != null){
			Server dbServer = serverDao.findByPrimaryKey(Server.class, server.getId());
			if (dbServer != null) {
				dbServer.setServerType(server.getServerType());
				dbServer.setName(server.getName());
				dbServer.setServerId(server.getServerId());
				dbServer.setDescription(server.getDescription());
				dbServer.setIpAddress(server.getIpAddress());
				dbServer.setUtilityPort(server.getUtilityPort());
				dbServer.setLastUpdatedByStaffId(server.getLastUpdatedByStaffId());
				dbServer.setGroupServerId(server.getGroupServerId());
				//dbServer.setContainerEnvironment(true);
				dbServer.setLastUpdatedDate(new Date());

				serverDao.update(dbServer);
				responseObject.setSuccess(true);
				responseObject.setResponseCode(ResponseCode.SERVER_UPDATE_SUCCESS);
				responseObject.setObject(dbServer);
			} 
			else {
				responseObject.setSuccess(false);
				responseObject.setResponseCode(ResponseCode.SERVER_DOES_NOT_EXIST);
			}
			}
			else if (serverMgmtRemoteJMXCall.getErrorMessage() != null){
				
				responseObject.setSuccess(false);
				responseObject.setResponseCode(ResponseCode.P_ENGINE_NOT_RUNNING);
				responseObject.setArgs(new Object[] {server.getIpAddress(),String.valueOf(server.getUtilityPort())});
				responseObject.setModuleName(BaseConstants.SERVERINSTANCE);
			}

		}
		return responseObject;
	}

	/**
	 * Logical Deletes the Server
	 */
	@Override
	@Transactional
	@Auditable(auditActivity = AuditConstants.DELETE_SERVER,actionType = BaseConstants.DELETE_ACTION , currentEntity = Server.class, ignorePropList= "")
	public ResponseObject deleteServer(int serverId,int staffid) {
		ResponseObject responseObject = new ResponseObject();
		List<ServerInstance> serverInstanceList=serverInstanceService.getServerInstanceByServerId(serverId);
		if(serverInstanceList!=null && serverInstanceList.isEmpty()){
			
			Server dbServer = serverDao.findByPrimaryKey(Server.class, serverId);
			if (dbServer != null && dbServer.getStatus()!= null && !dbServer.getStatus().toString().equals(StateEnum.DELETED.toString())) {
				dbServer.setLastUpdatedByStaffId(staffid);
				dbServer.setLastUpdatedDate(new Date());

				// Renaming the name and ip address so that it other user's can
				// reuse the name/ip address.
				dbServer.setName(dbServer.getName() + BaseConstants.DELETED_MODEL_SUFFIX + dbServer.getLastUpdatedDate().getTime());
				dbServer.setIpAddress(dbServer.getIpAddress() + BaseConstants.DELETED_MODEL_SUFFIX + dbServer.getLastUpdatedDate().getTime());

				dbServer.setStatus(StateEnum.DELETED);

				serverDao.update(dbServer);
				responseObject.setSuccess(true);
				responseObject.setResponseCode(ResponseCode.SERVER_DELETE_SUCCESS);
				responseObject.setObject(dbServer);
			} else {
				responseObject.setSuccess(false);
				responseObject.setResponseCode(ResponseCode.SERVER_DOES_NOT_EXIST);
			}
		}else{
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.SERVER_DELETE_FAIL_SERVERINSTANCE_ACTIVE);
		}
		
			return responseObject;
	}
	
	
	/**
	 * Check whether Server is eligible for deletion
	 * @param serverId
	 * @return
	 */
	@Override
	@Transactional
	public ResponseObject deleteServerCheck(int serverId) {
		ResponseObject responseObject = new ResponseObject();
		
		List<ServerInstance> serverInstanceList=serverInstanceService.getServerInstanceByServerId(serverId);
		if(serverInstanceList==null || serverInstanceList.isEmpty()){
			
			Server dbServer = serverDao.findByPrimaryKey(Server.class, serverId);
			if (dbServer != null && dbServer.getStatus()!= null && !dbServer.getStatus().toString().equals(StateEnum.DELETED.toString())) {

				responseObject.setSuccess(true);
				responseObject.setResponseCode(ResponseCode.SERVER_DELETE_SUCCESS);
			} else {
				responseObject.setSuccess(false);
				responseObject.setResponseCode(ResponseCode.SERVER_DOES_NOT_EXIST);
			}
		}else{
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.SERVER_DELETE_FAIL_SERVERINSTANCE_ACTIVE);
		}
		
			return responseObject;
	}
	
	
	
	

	/**
	 * Add Server Type in database.
	 */
	@Override
	@Transactional
	public ServerType addServerType(ServerType serverType) {
		return serverDao.addServerType(serverType);
	}

	/**
	 * Provides the Server Type List from database.
	 */
	@Override
	@Transactional(readOnly = true)
	public List<ServerType> getAllServerTypeList() {
		return serverDao.getAllServerTypeList();
	}
	
	/**
	 * Provides the Active Server Type List from database.
	 */
	@Override
	@Transactional(readOnly = true)
	public List<ServerType> getActiveServerTypeList() {
		return serverDao.getActiveServerTypeList();
	}

	/**
	 * Provides the Server Type based on input provided.
	 */
	@Override
	@Transactional(readOnly = true)
	public ServerType getServerType(int id) {
		return serverDao.getServerType(id);
	}

	/**
	 * Method will get Server details by ipAddress.
	 * @param ipAddress
	 */
	@Override
	@Transactional(readOnly = true)
	public ResponseObject getServerByIpAddress(String ipAddress) {
		logger.debug("Fetching server details for ipAddress : " + ipAddress);
		ResponseObject responseObject = new ResponseObject();
		Server server = serverDao.getServerByServerIpAddress(ipAddress);
		if(server != null ){
			logger.info("Server details found successfully for ipaddress " + ipAddress);
			responseObject.setSuccess(true);
			responseObject.setObject(server);
		}else{
			logger.info("Failed to get server details for ipaddress " + ipAddress);
			responseObject.setSuccess(false);
		}
		return responseObject;
	}

	/**
	 * Method will get server details for give ipaddress and server type. 
	 * 
	 */
	@Override
	@Transactional(readOnly = true)
	public ResponseObject getServerByIpAndType(String ipAddress, int serverType) {
		logger.debug("Fetching server details for ipaddress " + ipAddress + " server type id : " + serverType);
		ResponseObject responseObject = new ResponseObject();
		List<Server> serverList = serverDao.getServerListByIpAndType(ipAddress, serverType);
		if (serverList != null && !serverList.isEmpty()) {
			responseObject.setObject(serverList.get(0));
			responseObject.setSuccess(true);
		}else{
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.SERVER_GET_IPADDRESS_FAIL);
			responseObject.setResponseCodeNFV(NFVResponseCode.SERVER_GET_IPADDRESS_FAIL);
			
			responseObject.setArgs(new Object[] { ipAddress });
			logger.info("Failed to get server details for ipaddress " + ipAddress );
		}
		return responseObject;
	}
	
	
	@Override
	@Transactional(readOnly = true)
	public ResponseObject getServerByIpAndTypeAndUtility(String ipAddress, int serverType,int utilityPort) {
		ResponseObject responseObject = new ResponseObject();
		List<Server> serverList = serverDao.getServerListByIpAndTypeAndUtility(ipAddress, serverType,utilityPort);
		if (serverList != null && !serverList.isEmpty()) {
			responseObject.setObject(serverList.get(0));
			responseObject.setSuccess(true);
		}else{
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.SERVER_GET_IPADDRESS_FAIL);
			responseObject.setResponseCodeNFV(NFVResponseCode.SERVER_GET_IPADDRESS_FAIL);
			
			responseObject.setArgs(new Object[] { ipAddress });
			logger.info("Failed to get server details for ipaddress " + ipAddress );
		}
		return responseObject;
	}
	
	/**
	 * Method will get server list for given server type. 
	 * 
	 */
	@Override
	@Transactional(readOnly = true)
	public ResponseObject getServerListByServerType( int serverType) {
		logger.debug("Fetching server details for serverType : " + serverType);
		ResponseObject responseObject = new ResponseObject();
		List<Server> serverList = serverDao.getServerListByServerType(serverType);
		if (serverList != null && !serverList.isEmpty()) {
			responseObject.setObject(serverList);
			responseObject.setSuccess(true);
		}else{
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.MIGRATION_NO_SERVER_FOUND_FOR_PRODUCT_TYPE);
			logger.info("Failed to get server details for serverType " + serverType);
		}
		return responseObject;
	}
	
	/**
	 * Method will get Server details by ipAddress.
	 * @param ipAddress
	 */
	@Override
	@Transactional(readOnly = true)
	public ResponseObject getServerByHostAddress(String ipAddress) {
		logger.debug("Fetching server details for ipAddress : " + ipAddress + " and container env is true");
		ResponseObject responseObject = new ResponseObject();
		Long count = serverDao.getContainerServerList(ipAddress);
		logger.debug("Container count for ipAddress : " + ipAddress + " are " + count);
		if (count != null) {
			responseObject.setSuccess(true);
			responseObject.setObject(count);
		} else {
			responseObject.setSuccess(false);
		}
		return responseObject;
	}
	
	@SuppressWarnings({ "null", "unused" })
	@Override
	@Transactional
	public ResponseObject getListOfGroupIds() {
		logger.debug("Fetching Group Server Id details");
		ResponseObject responseObject = new ResponseObject();
		List<String> groupServerIDList = serverDao.getGroupServerIDList();
		if (groupServerIDList != null) {
			responseObject.setSuccess(true);
			responseObject.setObject(groupServerIDList);
		}else if(groupServerIDList.isEmpty()) {//NOSONAR
			responseObject.setSuccess(true);
		}else {
			responseObject.setSuccess(false);
		}	
		return responseObject;
	}

}