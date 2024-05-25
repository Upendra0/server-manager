/**
 * 
 */
package com.elitecore.sm.parser.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.elitecore.sm.common.dao.GenericDAOImpl;
import com.elitecore.sm.common.model.StateEnum;
import com.elitecore.sm.parser.model.PluginTypeMaster;

/**
 * @author vandana.awatramani
 *
 */
@Repository(value="PluginTypeDao")
public class PluginTypeDaoImpl extends GenericDAOImpl<PluginTypeMaster> implements PluginTypeDao{

	/**
	 * Fetch only enabal service type from db 
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<PluginTypeMaster> getEnablePluginTypeList(String pluginCategory){
		Criteria criteria=getCurrentSession().createCriteria(PluginTypeMaster.class);
		criteria.add(Restrictions.eq("status", StateEnum.ACTIVE));
		criteria.add(Restrictions.eq("category", pluginCategory));
		criteria.addOrder(Order.asc("id"));
		return  criteria.list();
	}

	/**
	 * Method will get plugin by type
	 * @param type
	 * @return PluginTypeMaster
	 */
	@SuppressWarnings("unchecked")
	@Override
	public PluginTypeMaster getPluginByType(String type) {
		Criteria criteria=getCurrentSession().createCriteria(PluginTypeMaster.class);
		criteria.add(Restrictions.eq("status", StateEnum.ACTIVE));
		criteria.add(Restrictions.eq("alias", type));
		
		List<PluginTypeMaster> pluginList = criteria.list();
		if(pluginList != null && !pluginList.isEmpty()){
			return pluginList.get(0);	
		}else{
			return null;
		}

	}

}
