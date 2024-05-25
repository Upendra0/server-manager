package com.elitecore.sm.policy.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.dao.GenericDAOImpl;
import com.elitecore.sm.common.exception.SMException;
import com.elitecore.sm.common.model.StateEnum;
import com.elitecore.sm.policy.model.Policy;
import com.elitecore.sm.policy.model.PolicyGroupRel;
import com.elitecore.sm.policy.model.SearchPolicy;
import com.elitecore.sm.serverinstance.dao.ServerInstanceDao;

@Repository(value = "policyDao")
public class PolicyDaoImpl extends GenericDAOImpl<Policy> implements IPolicyDao {

	@Autowired
	ServerInstanceDao serverInstanceDao;
	/*
	 * (non-Javadoc)
	 * @see com.elitecore.sm.policy.dao.IPolicyDao#getPolicyCriteriaBySearchParams(com.elitecore.sm.policy.model.SearchPolicy)
	 */
	public Map<String, Object> getPolicyCriteriaBySearchParams(SearchPolicy policy) {
		
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
				conditionList.add(Restrictions.isNotEmpty("processingPathList"));
			} else if(StringUtils.equals(policy.getAssociationStatus(), BaseConstants.NONASSOCIATED)) {
				conditionList.add(Restrictions.isEmpty("processingPathList"));
			}
		}
		
		conditionList.add(Restrictions.eq("server.id", policy.getServerInstanceId()));
		conditionList.add(Restrictions.ne(BaseConstants.STATUS,StateEnum.DELETED));
		
		returnMap.put("aliases", aliases);
		returnMap.put("conditions", conditionList);
		
		return returnMap;
	}

	/*
	 * (non-Javadoc)
	 * @see com.elitecore.sm.policy.dao.IPolicyDao#getPolicyPaginatedList(java.lang.Class, java.util.List, java.util.Map, int, int, java.lang.String, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public List<Policy> getPolicyPaginatedList(Class<Policy> classInstance, List<Criterion> conditions, Map<String, String> aliases, 
											   int offset, int limit, String sortColumn, String sortOrder) {
		List<Policy> resultList;
		Criteria criteria = getCurrentSession().createCriteria(classInstance);

		if(StringUtils.equals(sortColumn, "policyname") || 
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
		criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);

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
	 * @see com.elitecore.sm.policy.dao.IPolicyDao#iterateOverPolicy(com.elitecore.sm.policy.model.Policy)
	 */
	public void iterateOverPolicy(Policy policy) {
		
		if(policy != null && policy.getPolicyGroupRelSet() != null) {
				Hibernate.initialize(policy.getPolicyGroupRelSet());
			}
		}

	/*
	 * (non-Javadoc)
	 * @see com.elitecore.sm.policy.dao.IPolicyDao#getPolicyCountByAlias(java.lang.String)
	 */
	public long getPolicyCountByAlias(String alias, int serverId) {
		Criteria criteria = getCurrentSession().createCriteria(Policy.class);
		criteria.add(Restrictions.eq("alias", alias));
		criteria.add(Restrictions.eq("server.id", serverId));
		criteria.setProjection(Projections.rowCount());
		return (long)criteria.uniqueResult();
	}
	
	/**
	 * Mark server instance dirty
	 */
	@Override
	public void save(Policy policy){
		serverInstanceDao.markServerInstanceChildFlagDirty(policy.getServer());
		getCurrentSession().save(policy);
	}
	
	/**
	 * Mark server instance dirty
	 */
	@Override
	public void merge(Policy policy){
		serverInstanceDao.markServerInstanceChildFlagDirty(policy.getServer());
		getCurrentSession().merge(policy);
		//getCurrentSession().flush();
		
	}

	@Override
	public Policy getPolicyByAlias(String alias,int serverId) {
		Criteria criteria = getCurrentSession().createCriteria(Policy.class);
		criteria.add(Restrictions.eq("alias", alias));
		if (serverId != 0) {
			criteria.add(Restrictions.eq(BaseConstants.SERVER_ID, serverId));
		}
		criteria.add(Restrictions.ne(BaseConstants.STATUS, StateEnum.DELETED));
		return (Policy)criteria.uniqueResult();
	}
	
	/**
	 * Fetch Policy for sync
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<Policy> getPolicyforServerInstance(int serverInstanceId) {
		Criteria criteria = getCurrentSession().createCriteria(Policy.class);
		if (serverInstanceId != 0) {
			criteria.add(Restrictions.eq(BaseConstants.SERVER_ID, serverInstanceId));
		}
		criteria.add(Restrictions.ne(BaseConstants.STATUS, StateEnum.DELETED));
		criteria.addOrder(Order.asc("createdDate"));
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Policy getPolicyFullHierarchy(int policyId) throws SMException {
		logger.debug("Fetch Policy full hierarchy without using marshlling:: "+policyId);

		Criteria criteria = getCurrentSession().createCriteria(Policy.class);
		criteria.add(Restrictions.eq("id", policyId));
		criteria.add(Restrictions.ne("status", StateEnum.DELETED));

		List<Policy> policyList = (List<Policy>) criteria.list();
		Policy policy = policyList.get(0);
		
		if(policy != null){
			Hibernate.initialize(policy.getPolicyGroupRelSet());
			
			List<PolicyGroupRel> policyGroupRels = policy.getPolicyGroupRelSet();
			if(policyGroupRels != null && !policyGroupRels.isEmpty()) {
				int policyGroupLength = policyGroupRels.size();
				for(int i = policyGroupLength-1; i >= 0; i--) {
					PolicyGroupRel policyGroupRel = policyGroupRels.get(i);
					if(policyGroupRel != null) {
						if(StateEnum.DELETED.equals(policyGroupRel.getStatus())) {
							policyGroupRels.remove(i);
						} 
					}
				}
			}
			policy.setPolicyGroupRelSet(policyGroupRels);
			
		}
			
		return policy;
	}
	
}
