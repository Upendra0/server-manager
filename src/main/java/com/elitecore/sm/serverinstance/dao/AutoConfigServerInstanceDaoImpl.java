package com.elitecore.sm.serverinstance.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.elitecore.sm.common.dao.GenericDAOImpl;
import com.elitecore.sm.serverinstance.model.AutoConfigServerInstance;

/**
 * 
 * @author hardik.loriya
 *
 */
@Repository(value = "autoConfigServerInstanceDao")
public class AutoConfigServerInstanceDaoImpl extends GenericDAOImpl<AutoConfigServerInstance> implements AutoConfigServerInstanceDao {

	@SuppressWarnings("unchecked")
	@Override
	public AutoConfigServerInstance getAutoConfigSIByIPAndUtilityPortAndSIPort(String ipAddress, int utilityPort, int port) {
		AutoConfigServerInstance autoConfigServerInstance = null;
		logger.info(">> getAutoConfigSIByIPAndUtilityPortAndSIPort ");
		Criteria criteria=getCurrentSession().createCriteria(AutoConfigServerInstance.class);
		criteria.add(Restrictions.eq("ipaddress",ipAddress));
		criteria.add(Restrictions.eq("utilityPort",utilityPort));
		criteria.add(Restrictions.eq("siPort",port));
		List<AutoConfigServerInstance> list = criteria.list();
		if(list!=null && !list.isEmpty()){
			autoConfigServerInstance = list.get(0);
		}
		logger.info("<< getAutoConfigSIByIPAndUtilityPortAndSIPort ");
		return autoConfigServerInstance;
	}

}
