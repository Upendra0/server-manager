/**
 * 
 */
package com.elitecore.sm.parser.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.elitecore.sm.common.dao.GenericDAOImpl;
import com.elitecore.sm.common.model.StateEnum;
import com.elitecore.sm.parser.model.NatFlowDictionaryAttribute;

/**
 * @author Ranjitsinh Reval
 *
 */
@Repository(value="natFlowDictionaryAttributeDao")
public class NatFlowDictionaryAttributeDaoImpl extends GenericDAOImpl<NatFlowDictionaryAttribute> implements NatFlowDictionaryAttributeDao{

	/**
	 * Method will get all Default Attribute list for Natflow Parser.
	 * @return List<NatFlowDictionaryAttribute>
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<NatFlowDictionaryAttribute> getAllDefaulAttributeList() {
		logger.info(">> getAllDefaulAttributeList ");
		
		Criteria criteria = getCurrentSession().createCriteria(NatFlowDictionaryAttribute.class);
		criteria.add(Restrictions.eq("status", StateEnum.ACTIVE));
		criteria.addOrder(Order.asc("id"));
		
		logger.info("<< getAllDefaulAttributeList ");
		return criteria.list();
	}

	@Override
	@Transactional
	public void addAttribute(NatFlowDictionaryAttribute natflowAttr) {
		getCurrentSession().save(natflowAttr);
	}

}
