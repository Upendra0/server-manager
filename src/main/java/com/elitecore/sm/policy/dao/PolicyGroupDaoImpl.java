package com.elitecore.sm.policy.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.dao.GenericDAOImpl;
import com.elitecore.sm.common.model.StateEnum;
import com.elitecore.sm.policy.model.PolicyGroup;
import com.elitecore.sm.policy.model.SearchPolicy;
import com.elitecore.sm.serverinstance.dao.ServerInstanceDao;

@Repository(value = "policyGroupDao")
public class PolicyGroupDaoImpl extends GenericDAOImpl<PolicyGroup> implements IPolicyGroupDao {

	@Autowired
	ServerInstanceDao serverInstanceDao;
	/*
	 * (non-Javadoc)
	 * @see com.elitecore.sm.policy.dao.IPolicyGroupDao#getPolicyGroupCriteriaBySearchParams(com.elitecore.sm.policy.model.SearchPolicy)
	 */
	public Map<String, Object> getPolicyGroupCriteriaBySearchParams(SearchPolicy policy , Integer[] existingIDS) {
		
		Map<String, Object> returnMap = new HashMap<>();
		HashMap<String, String> aliases = new HashMap<>();
		List<Criterion> conditionList = new ArrayList<>();
		
		if(StringUtils.isNotEmpty(policy.getName())) {
			conditionList.add(Restrictions.like(BaseConstants.NAME, "%" + StringUtils.trim(policy.getName()) + "%").ignoreCase());
		}
		if(StringUtils.isNotEmpty(policy.getDescription())) {
			conditionList.add(Restrictions.like(BaseConstants.DESCRIPTION, "%" + StringUtils.trim(policy.getDescription()) + "%").ignoreCase());
		}
		if(StringUtils.isNotEmpty(policy.getAssociationStatus()) && !StringUtils.equalsIgnoreCase(BaseConstants.ALL, policy.getAssociationStatus())) {
			if(StringUtils.equals(policy.getAssociationStatus(), BaseConstants.ASSOCIATED)) {
				conditionList.add(Restrictions.isNotEmpty("policyGroupRelSet"));
			} else if(StringUtils.equals(policy.getAssociationStatus(), BaseConstants.NONASSOCIATED)) {
				conditionList.add(Restrictions.isEmpty("policyGroupRelSet"));
			}
		}
		conditionList.add(Restrictions.eq("server.id", policy.getServerInstanceId()));
		conditionList.add(Restrictions.ne(BaseConstants.STATUS,StateEnum.DELETED));
		
		if( existingIDS != null && existingIDS.length > 0){
			conditionList.add( Restrictions.not(Restrictions.in("id", existingIDS ) )	);
		}
		
		returnMap.put("aliases", aliases);
		returnMap.put("conditions", conditionList);
		
		return returnMap;
	}

	/*
	 * (non-Javadoc)
	 * @see com.elitecore.sm.policy.dao.IPolicyDao#getPolicyPaginatedList(java.lang.Class, java.util.List, java.util.Map, int, int, java.lang.String, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public List<PolicyGroup> getPolicyGroupPaginatedList(Class<PolicyGroup> classInstance, List<Criterion> conditions, Map<String, String> aliases, 
											   int offset, int limit, String sortColumn, String sortOrder) {
		List<PolicyGroup> resultList;
		Criteria criteria = getCurrentSession().createCriteria(classInstance);
		
		if(StringUtils.equals(sortColumn, BaseConstants.NAME) || 
				StringUtils.equals(sortColumn, BaseConstants.DESCRIPTION) || 
				StringUtils.equals(sortColumn, "associationstatus")) {
			criteria.addOrder(StringUtils.equals(sortOrder, "desc") ? Order.desc(sortColumn) : Order.asc(sortColumn));
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

	/*
	 * (non-Javadoc)
	 * @see com.elitecore.sm.policy.dao.IPolicyGroupDao#getPolicyGroupsCriteriaByPolicyId(int)
	 */
	public Map<String, Object> getPolicyGroupsCriteriaByPolicyId(int policyId) {
		
		Map<String, Object> returnMap = new HashMap<>();
		HashMap<String, String> aliases = new HashMap<>();
		List<Criterion> conditionList = new ArrayList<>();
		aliases.put("policy", "policy");
		conditionList.add(Restrictions.eq("policy.id", policyId ));
		
		returnMap.put(BaseConstants.ALIASES, aliases);
		returnMap.put(BaseConstants.CONDITIONS, conditionList);
		
		return returnMap;
	}

	/*
	 * (non-Javadoc)
	 * @see com.elitecore.sm.policy.dao.IPolicyGroupDao#getPolicyGroupCountByAlias(java.lang.String)
	 */
	public long getPolicyGroupCountByAlias(String alias,int serverId) {
		Criteria criteria = getCurrentSession().createCriteria(PolicyGroup.class);
		criteria.add(Restrictions.eq("alias", alias));
		criteria.add(Restrictions.eq("server.id", serverId));
		criteria.setProjection(Projections.rowCount());
		return (long)criteria.uniqueResult();
	}
	
	/**
	 * Mark server instance dirty
	 */
	@Override
	public void save(PolicyGroup policyGroup){
		serverInstanceDao.markServerInstanceChildFlagDirty(policyGroup.getServer());
		getCurrentSession().save(policyGroup);
	}
	
	/**
	 * Mark server instance dirty
	 */
	@Override
	public void merge(PolicyGroup policyGroup){
		serverInstanceDao.markServerInstanceChildFlagDirty(policyGroup.getServer());
		getCurrentSession().merge(policyGroup);
		getCurrentSession().flush();
		
	}
	
	/**
	 * Fetch PolicyGroup for sync
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<PolicyGroup> getPolicyGroupforServerInstance(int serverInstanceId) {
		Criteria criteria = getCurrentSession().createCriteria(PolicyGroup.class);
		if (serverInstanceId != 0) {
			criteria.add(Restrictions.eq(BaseConstants.SERVER_ID, serverInstanceId));
		}
		criteria.add(Restrictions.ne(BaseConstants.STATUS, StateEnum.DELETED));
		criteria.addOrder(Order.asc("createdDate"));
		return criteria.list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public PolicyGroup getPolicyGroupByAlias(String alias, int serverId) {
		Criteria criteria = getCurrentSession().createCriteria(PolicyGroup.class);
		criteria.add(Restrictions.eq("alias", alias));
		criteria.add(Restrictions.eq(BaseConstants.SERVER_ID, serverId));
		criteria.add(Restrictions.ne(BaseConstants.STATUS, StateEnum.DELETED));
		List<PolicyGroup> policyGroups = criteria.list();
		return (policyGroups != null && !policyGroups.isEmpty()) ? policyGroups.get(0) : null;
	}
	
}
