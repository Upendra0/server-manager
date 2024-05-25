/**
 * 
 */
package com.elitecore.sm.scripteditor.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.elitecore.sm.common.dao.GenericDAOImpl;
import com.elitecore.sm.common.model.StateEnum;
import com.elitecore.sm.scripteditor.model.ServerConfiguration;

/**
 * @author hiral.panchal
 *
 */
@Repository(value = "serverConfigDAO")
public class ServerConfigurationDaoImpl extends GenericDAOImpl<ServerConfiguration> implements ServerConfigurationDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<ServerConfiguration> getAllActiveServers() {
		
		logger.info("ServerConfigurationDaoImpl.getAllActiveServerName() called.");
		
		Criteria criteria = getCurrentSession().createCriteria(ServerConfiguration.class);
		//criteria.add(Restrictions.eq("status",StateEnum.ACTIVE));
		
		logger.info("ServerConfigurationDaoImpl.getAllActiveServerName() end.");
		
		return criteria.list();
	}

}
