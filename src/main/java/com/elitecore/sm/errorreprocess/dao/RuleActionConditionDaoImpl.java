/**
 * 
 */
package com.elitecore.sm.errorreprocess.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.dao.GenericDAOImpl;
import com.elitecore.sm.common.model.StateEnum;
import com.elitecore.sm.errorreprocess.model.RuleConditionDetails;


/**
 * @author Ranjitsinh Reval
 *
 */

@Repository(value = "ruleActionConditionDao")
public class RuleActionConditionDaoImpl  extends GenericDAOImpl<RuleConditionDetails> implements RuleActionConditionDao {

	
	
	/* (non-Javadoc)
	 * @see com.elitecore.sm.errorreprocess.dao.RuleActionConditionDao#getRuleConditionList()
	 */
	@Override
	public Map<String, Object> getRuleConditionList() {
		Map<String, Object> returnMap = new HashMap<>();
		HashMap<String, String> aliases = new HashMap<>();
		List<Criterion> conditions = new ArrayList<>();

		conditions.add(Restrictions.ne(BaseConstants.STATUS, StateEnum.DELETED));

		returnMap.put("aliases", aliases);
		returnMap.put("conditions", conditions);
		return returnMap;
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<RuleConditionDetails> getRuleConditionPaginatedList(Class<RuleConditionDetails> ruleConditionDetails, List<Criterion> conditions, Map<String, String> aliases, int offset, int limit, String sortColumn, String sortOrder) {

			List<RuleConditionDetails> resultList;
			Criteria criteria = getCurrentSession().createCriteria(ruleConditionDetails);

			logger.debug("Sort column =" + sortColumn);
			
			if (("desc").equalsIgnoreCase(sortOrder)) {
				if (("id").equalsIgnoreCase(sortColumn)) {
					criteria.addOrder(Order.desc("id"));
				} else {
					criteria.addOrder(Order.desc(sortColumn)); 
				} 
			} else if (("asc").equalsIgnoreCase(sortOrder)) {
				if (("id").equalsIgnoreCase(sortColumn)) {
					criteria.addOrder(Order.asc("id"));
				} else {
					criteria.addOrder(Order.asc(sortColumn));
				}
			}
			
			if (conditions != null) {
				for (Criterion condition : conditions) {
					criteria.add(condition);
				}
			}
			criteria.add(Restrictions.ne(BaseConstants.STATUS, StateEnum.DELETED));

			if (aliases != null) {
				for (Entry<String, String> entry : aliases.entrySet()) {
					criteria.createAlias(entry.getKey(), entry.getValue());
				}
			}
			criteria.setFirstResult(offset);// first record is 2
			criteria.setMaxResults(limit);// records from 2 to (2+3) 5
			resultList = criteria.list();
			return resultList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<RuleConditionDetails> getLastApplicationOrder() {
		Criteria criteria = getCurrentSession().createCriteria(RuleConditionDetails.class);
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(RuleConditionDetails.class).setProjection(Projections.max("applicationOrder"));
		detachedCriteria.add(Restrictions.ne(BaseConstants.STATUS, StateEnum.DELETED));
		criteria.add( Property.forName("applicationOrder").eq(detachedCriteria));
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<RuleConditionDetails> getAllActiveRuleList() {
		Criteria criteria = getCurrentSession().createCriteria(RuleConditionDetails.class);
		criteria.add(Restrictions.eq(BaseConstants.STATUS, StateEnum.ACTIVE));
		return criteria.list();
	}
	
	
}
