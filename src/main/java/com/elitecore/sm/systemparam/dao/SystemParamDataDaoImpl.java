/**
 * 
 */
package com.elitecore.sm.systemparam.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.elitecore.sm.common.dao.GenericDAOImpl;
import com.elitecore.sm.systemparam.model.SystemParameterData;
import com.elitecore.sm.systemparam.model.SystemParameterValuePoolData;

/**
 * @author vandana.awatramani
 * 
 */
@Repository(value = "systemParamDataDao")
public class SystemParamDataDaoImpl extends GenericDAOImpl<SystemParameterData> implements SystemParamDataDao {

	/**
	 * Load System parameter value pool data
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<SystemParameterValuePoolData> loadSystemParamValuePoolData() {
		Criteria criteria = getCurrentSession().createCriteria(SystemParameterValuePoolData.class);
		criteria.addOrder(Order.asc("id"));
		return  (List<SystemParameterValuePoolData>) criteria.list();
	}
	
	/**
	 * Find system parameter data by alias
	 */
	@Override
	public SystemParameterData getSystemParameterByAlias(String alias){
		return getUniqueEntityByNamedQuery("findSysParamDataByAlias",new Object[]{alias});
	}
	
	/**
	 * Load all System parameter
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<SystemParameterData> loadSystemParameterList(){
		
		Criteria criteriaForParam = getCurrentSession().createCriteria(SystemParameterData.class, "systemParam");

		criteriaForParam.add(Restrictions.and(Restrictions.eq("systemParam.enabled", true)));
		criteriaForParam.addOrder(Order.asc("systemParam.displayOrder"));

		return (List<SystemParameterData>)criteriaForParam.list();
	}
	
}
