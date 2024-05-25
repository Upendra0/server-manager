package com.elitecore.sm.server.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.elitecore.sm.common.constants.SystemParametersConstant;
import com.elitecore.sm.common.model.StateEnum;
import com.elitecore.sm.server.dao.ServerTypeDao;
import com.elitecore.sm.server.model.ServerType;
import com.elitecore.sm.util.MapCache;

@Service(value = "serverTypeService")
public class ServerTypeServiceImpl implements ServerTypeService {

	private Logger logger = Logger.getLogger(this.getClass().getName());

	@Autowired
	private ServerTypeDao servertypeDao;

	@Autowired
	private ServerService serverService;

	@SuppressWarnings({ "unchecked", "deprecation" })
	@Override
	public void setActiveProductType(String productType) {
		logger.debug("Apply profiling for selected product " + productType);
		List<ServerType> serverType = (List<ServerType>) MapCache.getConfigValueAsObject(SystemParametersConstant.ALL_SERVER_TYPE_LIST);
		for (ServerType serverTypeObj : serverType) {
			if (productType.indexOf(serverTypeObj.getAlias()) == -1) {
				serverTypeObj.setStatus(StateEnum.INACTIVE);
			} else {
				serverTypeObj.setStatus(StateEnum.ACTIVE);
			}
			servertypeDao.merge(serverTypeObj);
		}
		MapCache.addConfigObject(SystemParametersConstant.ACTIVE_SERVER_TYPE_LIST, serverService.getActiveServerTypeList());
	}
	@Override
	@Transactional
	public ServerType getServerTypeById(int serverTypeId){
		return servertypeDao.findByPrimaryKey(ServerType.class, serverTypeId);
	}
}