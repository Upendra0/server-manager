package com.elitecore.sm.policy.dao;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.dao.GenericDAOImpl;
import com.elitecore.sm.common.model.StateEnum;
import com.elitecore.sm.policy.model.PolicyGroupRuleRel;

/**
 * 
 * @author chintan.patel
 *
 */
@Repository(value = "policyGroupRuleRelDao")
public class PolicyGroupRuleRelDaoImpl extends GenericDAOImpl<PolicyGroupRuleRel> implements IPolicyGroupRuleRelDao {

	/*
	 * (non-Javadoc)
	 * @see com.elitecore.sm.policy.dao.IPolicyGroupRuleRelDao#deletePolicyRuleGroupRel(com.elitecore.sm.policy.model.PolicyGroupRuleRel)
	 */
	public void deletePolicyRuleGroupRel(PolicyGroupRuleRel policyGroupRuleRel) {
		getCurrentSession().delete(policyGroupRuleRel);
		
	}
	
	@SuppressWarnings("unchecked")
	public List<PolicyGroupRuleRel> getPolicyGroupRulePaginatedList(Class<PolicyGroupRuleRel> classInstance, List<Criterion> conditions,
			Map<String, String> aliases, int offset, int limit, String sortColumn, String sortOrder) {
		
		List<PolicyGroupRuleRel> resultList;
		Criteria criteria = getCurrentSession().createCriteria(classInstance);
		
/*		if(StringUtils.equals(sortColumn, BaseConstants.NAME) || 
				StringUtils.equals(sortColumn, BaseConstants.DESCRIPTION) || 
				StringUtils.equals(sortColumn, BaseConstants.APPLICATION_ORDER)) {*/
		if(StringUtils.isNotEmpty(sortColumn)) {
			criteria.addOrder(StringUtils.equals(sortOrder, BaseConstants.DESC) ? Order.desc(sortColumn) : Order.asc(sortColumn));
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

	@Override
	public int getMaxApplicationOrder() {
		PolicyGroupRuleRel policyGroupRuleRel = 
			    (PolicyGroupRuleRel) getCurrentSession().createCriteria(PolicyGroupRuleRel.class)
			    .addOrder(Order.desc("applicationOrder"))
			    .setMaxResults(1)
			    .uniqueResult();
		return policyGroupRuleRel != null ? policyGroupRuleRel.getApplicationOrder() : 0;
	}

}
