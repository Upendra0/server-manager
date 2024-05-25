/**
 * 
 */
package com.elitecore.sm.config.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.elitecore.sm.config.dao.ConfigDAO;
import com.elitecore.sm.config.model.EntitiesRegex;

/**
 * @author Sunil Gulabani
 * Apr 14, 2015
 */
@Service(value = "configService")
public class ConfigServiceImpl implements ConfigService {
	
	private Logger logger = Logger.getLogger(this.getClass().getName());

	@Autowired
	private ConfigDAO configDAO;
	
	/**
	 * It will add the system parameters list.
	 */
	@Override
	@Transactional
	public void addEntitiesRegex(List<EntitiesRegex> systemParametersList){
		for(EntitiesRegex systemParameters : systemParametersList){
			this.configDAO.save(systemParameters);
		}
	}
	
	/**
	 * It will load the system parameter list.
	 */
	@Override
	@Transactional(readOnly = true)
	public List<EntitiesRegex> loadSystemParameters() {
		List<EntitiesRegex> systemParametersList = configDAO.getAllObject(EntitiesRegex.class);
		logger.info("systemParametersList: " + systemParametersList);
		return systemParametersList;
	}
}
