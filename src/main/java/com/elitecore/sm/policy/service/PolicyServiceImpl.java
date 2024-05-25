package com.elitecore.sm.policy.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.criterion.Criterion;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.elitecore.core.util.mbean.data.live.CrestelNetServerDetails;
import com.elitecore.sm.common.aspect.Auditable;
import com.elitecore.sm.common.constants.AuditConstants;
import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.exception.SMException;
import com.elitecore.sm.common.model.StateEnum;
import com.elitecore.sm.common.util.ResponseCode;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.common.validator.ImportValidationErrors;
import com.elitecore.sm.integration.engine.RemoteJMXHelper;
import com.elitecore.sm.policy.dao.IDatabaseQueryDao;
import com.elitecore.sm.policy.dao.IPolicyActionDao;
import com.elitecore.sm.policy.dao.IPolicyConditionDao;
import com.elitecore.sm.policy.dao.IPolicyDao;
import com.elitecore.sm.policy.dao.IPolicyGroupDao;
import com.elitecore.sm.policy.dao.IPolicyGroupRelDao;
import com.elitecore.sm.policy.dao.IPolicyGroupRuleRelDao;
import com.elitecore.sm.policy.dao.IPolicyRuleDao;
import com.elitecore.sm.policy.model.DatabaseQuery;
import com.elitecore.sm.policy.model.Policy;
import com.elitecore.sm.policy.model.PolicyAction;
import com.elitecore.sm.policy.model.PolicyCondition;
import com.elitecore.sm.policy.model.PolicyGroup;
import com.elitecore.sm.policy.model.PolicyGroupRel;
import com.elitecore.sm.policy.model.PolicyGroupRuleRel;
import com.elitecore.sm.policy.model.PolicyRule;
import com.elitecore.sm.policy.model.PolicyRuleActionRel;
import com.elitecore.sm.policy.model.PolicyRuleConditionRel;
import com.elitecore.sm.policy.model.PolicyRuleHistory;
import com.elitecore.sm.policy.model.SearchPolicy;
import com.elitecore.sm.policy.model.SearchPolicyAction;
import com.elitecore.sm.policy.model.SearchPolicyCondition;
import com.elitecore.sm.policy.validator.PolicyActionValidator;
import com.elitecore.sm.policy.validator.PolicyConditionValidator;
import com.elitecore.sm.policy.validator.PolicyRuleGroupValidator;
import com.elitecore.sm.policy.validator.PolicyRuleValidator;
import com.elitecore.sm.policy.validator.PolicyValidator;
import com.elitecore.sm.serverinstance.dao.ServerInstanceDao;
import com.elitecore.sm.serverinstance.model.ServerInstance;
import com.elitecore.sm.services.dao.ServicesDao;
import com.elitecore.sm.services.service.SynchronizationProcessor;
import com.elitecore.sm.util.EliteUtils;
/**
 * The Class PolicyServiceImpl.
 */
@org.springframework.stereotype.Service(value = "policyService")
public class PolicyServiceImpl implements IPolicyService {
	
	private Logger logger = Logger.getLogger(this.getClass().getName());

	/** The policy dao. */
	@Autowired
	IPolicyDao policyDao;
	
	/** The policy action dao. */
	@Autowired
	private IPolicyActionDao policyActionDao;
	
	/** The policy condition dao. */
	@Autowired
	private IPolicyConditionDao policyConditionDao;
	
	/** The policy group dao. */
	@Autowired
	IPolicyGroupDao policyGroupDao;
	
	/** The server instance dao. */
	@Autowired
	ServerInstanceDao serverInstanceDao;
	
	/** The policy group rel dao. */
	@Autowired
	IPolicyGroupRelDao policyGroupRelDao;
	
	@Autowired
	PolicyValidator policyValidator;
	
	@Autowired
	PolicyConditionValidator policyConditionValidator;
	
	@Autowired
	PolicyActionValidator policyActionValidator;
	
	@Autowired
	PolicyRuleValidator policyRuleValidator;
	
	@Autowired
	PolicyRuleGroupValidator policyGroupValidator;
	
	@Autowired
	IPolicyRuleDao policyRuleDao;
	
	@Autowired
	IPolicyGroupRuleRelDao policyGroupRuleRelDao;
	
	@Autowired
	IDatabaseQueryDao databaseQueryDao;
	
	@Autowired
	ServicesDao servicesDao;
	
	/** The xslt processor. */
	SynchronizationProcessor xsltProcessor = new SynchronizationProcessor();
	
	public static final String CONDITIONS = "conditions";
	public static final String ALIASES = "aliases";
	/*
	 * (non-Javadoc)
	 * @see com.elitecore.sm.policy.service.IPolicyService#getTotalPolicyCount(com.elitecore.sm.policy.model.SearchPolicy)
	 */
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public long getTotalPolicyCount(SearchPolicy policy) {
		Map<String, Object> policyConditions = policyDao.getPolicyCriteriaBySearchParams(policy);
		return policyDao.getQueryCount(Policy.class, (List<Criterion>) policyConditions.get(CONDITIONS),
				(HashMap<String, String>) policyConditions.get(ALIASES));
	}
	
	
	
	/*
	 * (non-Javadoc)
	 * @see com.elitecore.sm.policy.service.IPolicyService#getPaginatedList(com.elitecore.sm.policy.model.SearchPolicy, int, int, java.lang.String, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public List<Policy> getPaginatedList(SearchPolicy searchPolicy, int startIndex, int limit, String sidx, String sord) {
		Map<String, Object> policyConditions = policyDao.getPolicyCriteriaBySearchParams(searchPolicy);		
		List<Policy> policyList = policyDao.getPolicyPaginatedList(Policy.class, (List<Criterion>) policyConditions.get(CONDITIONS),
				(HashMap<String, String>) policyConditions.get(ALIASES), startIndex, limit, sidx, sord);
		if(policyList != null) {
			for(Policy policy : policyList) {
				if(policy.getProcessingPathList() != null && !policy.getProcessingPathList().isEmpty()) {
					policy.setAssociationStatus(BaseConstants.ASSOCIATED);
				} else {
					policy.setAssociationStatus(BaseConstants.NONASSOCIATED);
				}
				policyDao.iterateOverPolicy(policy);
			}
		}
		return policyList;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.elitecore.sm.policy.service.IPolicyService#getTotalPolicyGroupCount(com.elitecore.sm.policy.model.SearchPolicy)
	 */
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public long getTotalPolicyGroupCount(SearchPolicy policy , Integer[] existingIDS) {
		Map<String, Object> policyGroupConditions = policyGroupDao.getPolicyGroupCriteriaBySearchParams(policy , existingIDS );
		return policyGroupDao.getQueryCount(PolicyGroup.class, (List<Criterion>) policyGroupConditions.get(CONDITIONS),
				(HashMap<String, String>) policyGroupConditions.get(ALIASES));
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.elitecore.sm.policy.service.IPolicyService#getPolicyGroupPaginatedList(com.elitecore.sm.policy.model.SearchPolicy, int, int, java.lang.String, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public List<PolicyGroup> getPolicyGroupPaginatedList(SearchPolicy policy , Integer[] existingIDS, int startIndex, int limit, String sidx, String sord) {
		Map<String, Object> policyGroupConditions = policyGroupDao.getPolicyGroupCriteriaBySearchParams(policy , existingIDS);
		List<PolicyGroup> policyGroupList =  policyGroupDao.getPolicyGroupPaginatedList(PolicyGroup.class, (List<Criterion>) policyGroupConditions.get(CONDITIONS),
				(HashMap<String, String>) policyGroupConditions.get(ALIASES), startIndex, limit, sidx, sord);
		if(policyGroupList != null) {
			for(PolicyGroup policyGroup : policyGroupList) {
				if(policyGroup.getPolicyGroupRelSet() != null && !policyGroup.getPolicyGroupRelSet().isEmpty()) {
					policyGroup.setAssociationStatus(BaseConstants.ASSOCIATED);
				} else {
					policyGroup.setAssociationStatus(BaseConstants.NONASSOCIATED);
				}
			}
		}
		return policyGroupList;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.elitecore.sm.policy.service.IPolicyService#savePolicy(com.elitecore.sm.policy.model.Policy)
	 */
	@Transactional
	@Auditable(auditActivity = AuditConstants.CREATE_POLICY, actionType = BaseConstants.CREATE_ACTION, currentEntity = Policy.class, ignorePropList= "server,associationStatus,policyGroupRelSet")
	public ResponseObject savePolicy(Policy policy, String policyGroups, int staffId, int serverId) {
		ResponseObject responseObject = new ResponseObject();
		
		if(policyDao.getPolicyCountByAlias(policy.getAlias(),serverId) > 0) {
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.DUPLICATE_POLICY);
		} else {
			List<PolicyGroupRel> policyGroupRelSet = new ArrayList<>();
			
			if(StringUtils.isNotEmpty(policyGroups)) {
				JSONArray jsonArray = new JSONArray(policyGroups);
				for(int i = 0; i < jsonArray.length(); i++) {
					JSONObject jsonObj = jsonArray.getJSONObject(i);
					int groupId = jsonObj.getInt("id");
					PolicyGroup policyGroup = policyGroupDao.findByPrimaryKey(PolicyGroup.class, groupId);
					PolicyGroupRel policyGroupRel = new PolicyGroupRel();
					policyGroupRel.setApplicationOrder(i + 1);
					policyGroupRel.setCreatedByStaffId(staffId);
					policyGroupRel.setCreatedDate(new Date());
					policyGroupRel.setGroup(policyGroup);
					policyGroupRel.setLastUpdatedByStaffId(staffId);
					policyGroupRel.setLastUpdatedDate(new Date());
					policyGroupRel.setPolicy(policy);
					policyGroupRelSet.add(policyGroupRel);
				}
			}
			ServerInstance server = serverInstanceDao.findByPrimaryKey(ServerInstance.class, serverId);
			policy.setPolicyGroupRelSet(policyGroupRelSet);
			policy.setServer(server);
			policyDao.save(policy);
			responseObject.setSuccess(true);
			responseObject.setResponseCode(ResponseCode.POLICY_CREATE_SUCCESS);
		}
		
		return responseObject;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.elitecore.sm.policy.service.IPolicyService#getPolicyGroupPaginatedListByPolicy(int, int, int, java.lang.String, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public List<PolicyGroupRel> getPolicyGroupPaginatedListByPolicy(int policyId, int startIndex, int limit, String sidx,
			String sord) {
		Map<String, Object> policyGroupConditions = policyGroupDao.getPolicyGroupsCriteriaByPolicyId(policyId);
		return policyGroupRelDao.getPolicyGroupRelPaginatedList(PolicyGroupRel.class, (List<Criterion>) policyGroupConditions.get(CONDITIONS),
				(HashMap<String, String>) policyGroupConditions.get(ALIASES), startIndex, limit, sidx, sord);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.elitecore.sm.policy.service.IPolicyService#getTotalPolicyGroupCount(int)
	 */
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public long getTotalPolicyGroupCount(int policyId) {
		Map<String, Object> policyGroupConditions = policyGroupDao.getPolicyGroupsCriteriaByPolicyId(policyId);
		return policyGroupRelDao.getQueryCount(PolicyGroupRel.class, (List<Criterion>) policyGroupConditions.get(CONDITIONS),
				(HashMap<String, String>) policyGroupConditions.get(ALIASES));
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.elitecore.sm.policy.service.IPolicyService#updatePolicy(com.elitecore.sm.policy.model.Policy, java.lang.String, int, int)
	 */
	@Transactional
	@Auditable(auditActivity = AuditConstants.UPDATE_POLICY, actionType = BaseConstants.UPDATE_ACTION, currentEntity = Policy.class, ignorePropList= "server,associationStatus,policyGroupRelSet")
	public ResponseObject updatePolicy(Policy policy, String policyGroups, int staffId, int serverId) {
		ResponseObject responseObject = new ResponseObject();
		
		if(policyDao.getPolicyCountByAlias(policy.getAlias(), serverId) > 1) {
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.DUPLICATE_POLICY);
		} else {
			List<PolicyGroupRel> policyGroupRelSet = new ArrayList<>();
			
			if(StringUtils.isNotEmpty(policyGroups)) {
				JSONArray jsonArray = new JSONArray(policyGroups);
				for(int i = 0; i < jsonArray.length(); i++) {
					JSONObject jsonObj = jsonArray.getJSONObject(i);
					int groupId = jsonObj.getInt("id");
					PolicyGroup policyGroup = policyGroupDao.findByPrimaryKey(PolicyGroup.class, groupId);
					PolicyGroupRel policyGroupRel = new PolicyGroupRel();
					policyGroupRel.setApplicationOrder(i + 1);
					policyGroupRel.setCreatedByStaffId(staffId);
					policyGroupRel.setCreatedDate(new Date());
					policyGroupRel.setGroup(policyGroup);
					policyGroupRel.setLastUpdatedByStaffId(staffId);
					policyGroupRel.setLastUpdatedDate(new Date());
					policyGroupRel.setPolicy(policy);
					policyGroupRelSet.add(policyGroupRel);
				}
			}
			ServerInstance server = serverInstanceDao.findByPrimaryKey(ServerInstance.class, serverId);
			policy.setPolicyGroupRelSet(policyGroupRelSet);
			policy.setServer(server);
			Policy oldPolicy = policyDao.findByPrimaryKey(Policy.class, policy.getId());
			for(PolicyGroupRel policyGroupRel : oldPolicy.getPolicyGroupRelSet()) {
				policyGroupRelDao.deletePolicyGroupRel(policyGroupRel);
			}
			policyDao.merge(policy);
			
			responseObject.setSuccess(true);
			responseObject.setResponseCode(ResponseCode.POLICY_UPDATE_SUCCESS);
		}
		return responseObject;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.elitecore.sm.policy.service.IPolicyService#removePolicy(int)
	 */
	@Transactional
	@Auditable(auditActivity = AuditConstants.DELETE_POLICY, actionType = BaseConstants.DELETE_ACTION, currentEntity = Policy.class, ignorePropList= "server,associationStatus,policyGroupRelSet")
	public ResponseObject removePolicy(int policyId) {
		ResponseObject responseObject = new ResponseObject();
		
		Policy policy = policyDao.findByPrimaryKey(Policy.class, policyId);
		policy.setStatus(StateEnum.DELETED);
		policy.setName(EliteUtils.checkForNames(BaseConstants.ACTION_DELETE, policy.getName()));
		policy.setAlias(EliteUtils.checkForNames(BaseConstants.ACTION_DELETE, policy.getAlias()));
		
		Policy oldPolicy = policyDao.findByPrimaryKey(Policy.class, policy.getId());
			for(PolicyGroupRel policyGroupRel : oldPolicy.getPolicyGroupRelSet()) {
					policyGroupRelDao.deletePolicyGroupRel(policyGroupRel);
			}
			Iterator<PolicyGroupRel> policyGroupRuleRelIter = oldPolicy.getPolicyGroupRelSet().iterator();
			
			while(policyGroupRuleRelIter.hasNext()) {
				policyGroupRuleRelIter.next();
				policyGroupRuleRelIter.remove();
			}
			policyDao.merge(oldPolicy);
		
		policyDao.merge(policy);
		
		responseObject.setSuccess(true);
		responseObject.setResponseCode(ResponseCode.POLICY_REMOVE_SUCCESS);
		
		return responseObject;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.elitecore.sm.policy.service.IPolicyService#getPolicyById(int)
	 */
	@Transactional(readOnly = true)
	public Policy getPolicyById(int policyId) {
		
		return policyDao.findByPrimaryKey(Policy.class, policyId);
	}
	
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public long getPolicyRuleCountByRuleGroup(int ruleGroupId) {
		Map<String, Object> policyRuleConditions = policyRuleDao.getPolicyRulesCriteriaByRuleGroupId(ruleGroupId);
		return policyGroupRuleRelDao.getQueryCount(PolicyGroupRuleRel.class, (List<Criterion>) policyRuleConditions.get(CONDITIONS),
				(HashMap<String, String>) policyRuleConditions.get(ALIASES));
	}

	/*
	 * (non-Javadoc)
	 * @see com.elitecore.sm.policy.service.IPolicyService#getTotalPolicyRuleCount(com.elitecore.sm.policy.model.SearchPolicy)
	 */
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public long getTotalPolicyRuleCount(SearchPolicy policy) {
		Map<String, Object> policyRuleConditions = policyRuleDao.getPolicyRulesCriteriaBySearchParams(policy);
		return policyRuleDao.getQueryCount(PolicyRule.class, (List<Criterion>) policyRuleConditions.get(CONDITIONS),
				(HashMap<String, String>) policyRuleConditions.get(ALIASES));
	}
	

	/*
	 * (non-Javadoc)
	 * @see com.elitecore.sm.policy.service.IPolicyService#getPolicyRulePaginatedList(com.elitecore.sm.policy.model.SearchPolicy, int, int, java.lang.String, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public List<PolicyRule> getPolicyRulePaginatedList(SearchPolicy searchPolicy, int startIndex, int limit,
			String sidx, String sord) {
		Map<String, Object> policyRuleConditions = policyRuleDao.getPolicyRulesCriteriaBySearchParams(searchPolicy);
		List<PolicyRule> policyRuleList = policyRuleDao.getPaginatedList(PolicyRule.class, (List<Criterion>) policyRuleConditions.get(CONDITIONS), 
					(HashMap<String, String>) policyRuleConditions.get(ALIASES), startIndex, limit, sidx, sord);
		
		if(policyRuleList != null && !policyRuleList.isEmpty()) {
			for(PolicyRule policyRule : policyRuleList) {
				if(policyRule.getPolicyGroupRuleRelSet() != null && !policyRule.getPolicyGroupRuleRelSet().isEmpty()) {
					policyRule.setAssociationStatus(BaseConstants.ASSOCIATED);
				} else {
					policyRule.setAssociationStatus(BaseConstants.NONASSOCIATED);
				}
			}
		}
		
		return policyRuleList;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.elitecore.sm.policy.service.IPolicyService#savePolicyGroup(com.elitecore.sm.policy.model.PolicyGroup, java.lang.String, int, int)
	 */
	@Transactional
	@Auditable(auditActivity = AuditConstants.CREATE_POLICY_RULE_GROUP, actionType = BaseConstants.CREATE_ACTION, currentEntity = PolicyGroup.class, ignorePropList= "server,policyGroupRelSet,policyGroupRuleRelSet")
	public ResponseObject savePolicyGroup(PolicyGroup policyGroup, String groupRules, int staffId, int serverId) {
		ResponseObject responseObject = new ResponseObject();
		
		if(policyGroupDao.getPolicyGroupCountByAlias(policyGroup.getAlias(),serverId) > 0) {
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.DUPLICATE_POLICY_GROUP);
		} else {
			List<PolicyGroupRuleRel> policyGroupRuleRelSet = new ArrayList<>();
			
			if(StringUtils.isNotEmpty(groupRules)) {
				JSONArray jsonArray = new JSONArray(groupRules);
				for(int i = 0; i < jsonArray.length(); i++) {
					JSONObject jsonObj = jsonArray.getJSONObject(i);
					int ruleId = jsonObj.getInt("id");
					PolicyRule policyRule = policyRuleDao.findByPrimaryKey(PolicyRule.class, ruleId);
					PolicyGroupRuleRel groupRuleRel = new PolicyGroupRuleRel();
					groupRuleRel.setApplicationOrder(i + 1);
					groupRuleRel.setCreatedByStaffId(staffId);
					groupRuleRel.setCreatedDate(new Date());
					groupRuleRel.setGroup(policyGroup);
					groupRuleRel.setLastUpdatedByStaffId(staffId);
					groupRuleRel.setLastUpdatedDate(new Date());
					groupRuleRel.setPolicyRule(policyRule);
					policyGroupRuleRelSet.add(groupRuleRel);
				}
			}
			ServerInstance server = serverInstanceDao.findByPrimaryKey(ServerInstance.class, serverId);
			policyGroup.setPolicyGroupRuleRelSet(policyGroupRuleRelSet);
			policyGroup.setServer(server);
			policyGroupDao.save(policyGroup);
			responseObject.setSuccess(true);
			responseObject.setResponseCode(ResponseCode.POLICY_GROUP_CREATE_SUCCESS);
		}
		
		return responseObject;
	}

	/*
	 * (non-Javadoc)
	 * @see com.elitecore.sm.policy.service.IPolicyService#updatePolicyGroup(com.elitecore.sm.policy.model.PolicyGroup, java.lang.String, int, int)
	 */
	@Transactional
	@Auditable(auditActivity = AuditConstants.UPDATE_POLICY_RULE_GROUP, actionType = BaseConstants.UPDATE_ACTION, currentEntity = PolicyGroup.class, ignorePropList= "server,associationStatus,policyGroupRelSet,policyGroupRuleRelSet")
	public ResponseObject updatePolicyGroup(PolicyGroup policyGroup, String groupRules, int staffId, int serverId) {
		ResponseObject responseObject = new ResponseObject();
		
		if(policyGroupDao.getPolicyGroupCountByAlias(policyGroup.getAlias(),serverId) > 1) {
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.DUPLICATE_POLICY_GROUP);
		} else {
			List<PolicyGroupRuleRel> groupRuleRelSet = new ArrayList<>();
			if(StringUtils.isNotEmpty(groupRules)) {
				JSONArray jsonArray = new JSONArray(groupRules);
				for(int i = 0; i < jsonArray.length(); i++) {
					JSONObject jsonObj = jsonArray.getJSONObject(i);
					int ruleId = jsonObj.getInt("id");
					PolicyRule policyRule = policyRuleDao.findByPrimaryKey(PolicyRule.class, ruleId);
					PolicyGroupRuleRel groupRuleRel = new PolicyGroupRuleRel();
					groupRuleRel.setApplicationOrder(i + 1);
					groupRuleRel.setId(0);
					groupRuleRel.setCreatedByStaffId(staffId);
					groupRuleRel.setCreatedDate(new Date());
					groupRuleRel.setGroup(policyGroup);
					groupRuleRel.setLastUpdatedByStaffId(staffId);
					groupRuleRel.setLastUpdatedDate(new Date());
					groupRuleRel.setPolicyRule(policyRule);
					
					groupRuleRelSet.add(groupRuleRel);
				}
			}
			ServerInstance server = serverInstanceDao.findByPrimaryKey(ServerInstance.class, serverId);
			policyGroup.setPolicyGroupRuleRelSet(groupRuleRelSet);
			policyGroup.setServer(server);
			PolicyGroup oldPolicyGroup = policyGroupDao.findByPrimaryKey(PolicyGroup.class, policyGroup.getId());
			
			Iterator<PolicyGroupRuleRel> policyGroupRuleRelIter = oldPolicyGroup.getPolicyGroupRuleRelSet().iterator();
			
			oldPolicyGroup.setAlias(policyGroup.getAlias());
			oldPolicyGroup.setName(policyGroup.getName());
			oldPolicyGroup.setDescription(policyGroup.getDescription());
			
			while(policyGroupRuleRelIter.hasNext()) {
				policyGroupRuleRelIter.next();
				policyGroupRuleRelIter.remove();
			}
			
			for(PolicyGroupRuleRel groupRuleRel : groupRuleRelSet) {
				oldPolicyGroup.getPolicyGroupRuleRelSet().add(groupRuleRel);
			}
			
			policyGroupDao.merge(oldPolicyGroup);
			
			responseObject.setSuccess(true);
			responseObject.setResponseCode(ResponseCode.POLICY_GROUP_UPDATE_SUCCESS);
		}
		return responseObject;
	}

	/*
	 * (non-Javadoc)
	 * @see com.elitecore.sm.policy.service.IPolicyService#removePolicyGroup(int)
	 */
	@Transactional
	@Auditable(auditActivity = AuditConstants.DELETE_POLICY_RULE_GROUP, actionType = BaseConstants.DELETE_ACTION, currentEntity = PolicyGroup.class, ignorePropList= "server,associationStatus,policyGroupRelSet,policyGroupRuleRelSet")
	public ResponseObject removePolicyGroup(int policyGroupId) {
		ResponseObject responseObject = new ResponseObject();
		
		PolicyGroup policyGroup = policyGroupDao.findByPrimaryKey(PolicyGroup.class, policyGroupId);
		policyGroup.setStatus(StateEnum.DELETED);
		policyGroup.setName(EliteUtils.checkForNames(BaseConstants.ACTION_DELETE, policyGroup.getName()));
		policyGroup.setAlias(EliteUtils.checkForNames(BaseConstants.ACTION_DELETE, policyGroup.getAlias()));
		PolicyGroup oldPolicyGroup = policyGroupDao.findByPrimaryKey(PolicyGroup.class, policyGroup.getId());
		
		Iterator<PolicyGroupRuleRel> policyGroupRuleRelIter = oldPolicyGroup.getPolicyGroupRuleRelSet().iterator();
		
		while(policyGroupRuleRelIter.hasNext()) {
			policyGroupRuleRelIter.next();
			policyGroupRuleRelIter.remove();
		}
		policyGroupDao.merge(oldPolicyGroup);
		policyGroupDao.merge(policyGroup);
		
		responseObject.setSuccess(true);
		responseObject.setResponseCode(ResponseCode.POLICY_GROUP_REMOVE_SUCCESS);
		
		return responseObject;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.elitecore.sm.policy.service.IPolicyService#getPolicyGroupById(int)
	 */
	@Transactional(readOnly = true)
	public PolicyGroup getPolicyGroupById(int policyGroupId) {
		return policyGroupDao.findByPrimaryKey(PolicyGroup.class, policyGroupId);
	}
	
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public List<PolicyGroupRuleRel> getPolicyRulePaginatedListByRuleGroup(int ruleGroupId, int startIndex, int limit, String sidx,
			String sord) {
		Map<String, Object> policyRuleConditions = policyRuleDao.getPolicyRulesCriteriaByRuleGroupId(ruleGroupId);
		return policyGroupRuleRelDao.getPolicyGroupRulePaginatedList(PolicyGroupRuleRel.class, (List<Criterion>) policyRuleConditions.get(CONDITIONS), 
				(HashMap<String, String>) policyRuleConditions.get(ALIASES), startIndex, limit, sidx, sord);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public long getTotalPolicyActionCount(SearchPolicyAction searchPolicyAction) {
		Map<String, Object> policyActionConditions = policyActionDao.getPolicyActionBySearchParameters(searchPolicyAction);
		return policyActionDao.getQueryCount(PolicyAction.class, (List<Criterion>) policyActionConditions.get(CONDITIONS),
				(HashMap<String, String>) policyActionConditions.get(ALIASES));
	}
	
	/* (non-Javadoc)
	 * @see com.elitecore.sm.policy.service.IPolicyService#getPaginatedList(com.elitecore.sm.policy.model.SearchPolicyAction, int, int, java.lang.String, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<PolicyAction> getPaginatedList(SearchPolicyAction searchPolicyAction, int startIndex, int limit, String sidx, String sord) {
		Map<String, Object> policyActionConditions = policyActionDao.getPolicyActionBySearchParameters(searchPolicyAction);
		List<PolicyAction> policyList = policyActionDao.getPaginatedList(PolicyAction.class, (List<Criterion>) policyActionConditions.get(CONDITIONS),
				(HashMap<String, String>) policyActionConditions.get(ALIASES), startIndex, limit, sidx, sord);
		if(policyList != null){
			for(PolicyAction policyAction : policyList){
				//Hibernate.initialize(policyAction.getPolicyRuleSet());
				Hibernate.initialize(policyAction.getPolicyRuleActionRel());
			}
		}
		return policyList;
		
	}
	
	/* (non-Javadoc)
	 * @see com.elitecore.sm.policy.service.IPolicyService#saveAction(com.elitecore.sm.policy.model.PolicyAction)
	 */
	@Override
	@Transactional
	@Auditable(auditActivity = AuditConstants.CREATE_POLICY_ACTION, actionType = BaseConstants.CREATE_ACTION, currentEntity = PolicyAction.class, ignorePropList= "server")
	public ResponseObject saveAction(PolicyAction policyAction) {
		ResponseObject responseObject = new ResponseObject();
		
		int actionId = policyActionDao.getActionByAlies(policyAction.getAlias(),policyAction.getServer().getId());
		if (actionId != 0 ) {
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.DUPLICATE_POLICY_ACTION);
		} else {
			if("dynamic".equalsIgnoreCase(policyAction.getType())) {
				String action = policyAction.getAction();
				if(action != null) {
					String[] aliasArray = action.split("=");
					if(aliasArray != null && aliasArray.length > 0) {
						String alias = aliasArray[1];
						if(alias != null) {
							DatabaseQuery databaseQuery = databaseQueryDao.getDatabaseQueryByAlias(alias, policyAction.getServer().getId());
							if(databaseQuery != null) {
								policyAction.setDatabaseQuery(databaseQuery);
								policyAction.setDatabaseQueryAlias(alias);
							}
						}
					}
					
				}
			} else {
				policyAction.setDatabaseQuery(null);
				policyAction.setDatabaseQueryAlias(null);
			}
			
			String actionExpressionForSync = policyAction.getActionExpression();
			if(actionExpressionForSync!=null){
				policyAction.setActionExpressionForSync(actionExpressionForSync.replaceAll("'", ""));//NOSONAR
			}
			
			policyActionDao.save(policyAction);
			
			if (policyAction.getId() != 0) {
				responseObject.setSuccess(true);
				responseObject.setResponseCode(ResponseCode.POLICY_ACTION_INSERT_SUCCESS);
			} else {
				responseObject.setSuccess(false);
				responseObject.setResponseCode(ResponseCode.POLICY_ACTION_INSERT_FAIL);
			}
		}
		return responseObject;
	}
	
	/* (non-Javadoc)
	 * @see com.elitecore.sm.policy.service.IPolicyService#updateAction(com.elitecore.sm.policy.model.PolicyAction)
	 */
	@Override
	@Transactional
	@Auditable(auditActivity = AuditConstants.UPDATE_POLICY_ACTION, actionType = BaseConstants.UPDATE_ACTION, currentEntity = PolicyAction.class, ignorePropList= "server")
	public ResponseObject updateAction(PolicyAction policyAction) {
		ResponseObject responseObject = new ResponseObject();
		
		int actionId = policyActionDao.getActionByAlies(policyAction.getAlias(),policyAction.getServer().getId());
		if (actionId != 0 && actionId != policyAction.getId()) {
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.DUPLICATE_POLICY_ACTION);
		} else {
			if(BaseConstants.POLICY_ACTION_TYPE_DYNAMIC.equalsIgnoreCase(policyAction.getType())) {
				String action = policyAction.getAction();
				if(action != null) {
					String[] aliasArray = action.split("=");
					if(aliasArray != null && aliasArray.length > 0) {
						String alias = aliasArray[1];
						if(alias != null) {
							DatabaseQuery databaseQuery = databaseQueryDao.getDatabaseQueryByAlias(alias, policyAction.getServer().getId());
							if(databaseQuery != null) {
								policyAction.setDatabaseQuery(databaseQuery);
								policyAction.setDatabaseQueryAlias(alias);
							}
						}
					}
					
				}
			} else {
				policyAction.setDatabaseQuery(null);
				policyAction.setDatabaseQueryAlias(null);
			}
			if(BaseConstants.POLICY_ACTION_TYPE_STATIC.equalsIgnoreCase(policyAction.getType())) {
				policyAction.setActionExpression(BaseConstants.EMPTY_STRING);
			}
			
			String actionExpressionForSync = policyAction.getActionExpression();
			if(actionExpressionForSync!=null){
				policyAction.setActionExpressionForSync(actionExpressionForSync.replaceAll("'", ""));//NOSONAR
			}
			
			policyActionDao.merge(policyAction);
			
			if (policyAction.getId() != 0) {
				responseObject.setSuccess(true);
				responseObject.setResponseCode(ResponseCode.POLICY_ACTION_UPDATED_SUCCESSFULL);
			} else {
				responseObject.setSuccess(false);
				responseObject.setResponseCode(ResponseCode.POLICY_ACTION_UPDATE_FAILED);
			}
		}
		return responseObject;
	}
	
	/* (non-Javadoc)
	 * @see com.elitecore.sm.policy.service.IPolicyService#getPolicyActionById(int)
	 */
	@Override
	@Transactional
	public PolicyAction getPolicyActionById(int id) {
		
		return policyActionDao.findByPrimaryKey(PolicyAction.class, id);
	}
	
	/* (non-Javadoc)
	 * @see com.elitecore.sm.policy.service.IPolicyService#deletePolicyAction(int, int)
	 */
	@Override
	@Transactional
	@Auditable(auditActivity = AuditConstants.DELETE_POLICY_ACTION, actionType = BaseConstants.DELETE_ACTION, currentEntity = PolicyAction.class, ignorePropList= "server")
	public ResponseObject deletePolicyAction(int actionId, int staffId) {
		ResponseObject responseObject = new ResponseObject();
		PolicyAction policyAction = policyActionDao.findByPrimaryKey(PolicyAction.class, actionId);
		if (policyAction != null) {
			policyAction.setName(EliteUtils.checkForNames(BaseConstants.ACTION_DELETE,policyAction.getName()));
			policyAction.setAlias(EliteUtils.checkForNames(BaseConstants.ACTION_DELETE,policyAction.getAlias()));
			policyAction.setStatus(StateEnum.DELETED);
			policyAction.setLastUpdatedByStaffId(staffId);
			policyAction.setLastUpdatedDate(new Date());
			policyActionDao.merge(policyAction);
			
			responseObject.setSuccess(true);
			responseObject.setObject(policyAction);
			responseObject.setResponseCode(ResponseCode.POLICY_ACTION_DELETE_SUCCESSFULL);
		} else {
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.POLICY_ACTION_DELETE_FAIL);
		}
		return responseObject;
	}

	/*
	 * (non-Javadoc)
	 * @see com.elitecore.sm.policy.service.IPolicyService#validatePolictForImport(com.elitecore.sm.policy.model.Policy, java.util.List)
	 */
	@Transactional
	public List<ImportValidationErrors> validatePolictForImport(Policy policy, List<ImportValidationErrors> importErrorList,int serverId) {
		policyValidator.validatePolicyParameters(policy, null, importErrorList, null, true);
		//policyValidator.validatePolicyUniqueName(policy, null, importErrorList, null, true,serverId);
		return importErrorList;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public long getTotalPolicyConditionCount(SearchPolicyCondition searchPolicyCondition) {
		Map<String, Object> policyConditionConditions = policyConditionDao.getPolicyConditionBySearchParameters(searchPolicyCondition);
		return policyConditionDao.getQueryCount(PolicyCondition.class, (List<Criterion>) policyConditionConditions.get(CONDITIONS),
				(HashMap<String, String>) policyConditionConditions.get(ALIASES));
	}
	
	
	// METHODS OF CONDITION
	
	/* (non-Javadoc)
	 * @see com.elitecore.sm.policy.service.IPolicyService#getPaginatedList(com.elitecore.sm.policy.model.SearchPolicyCondition, int, int, java.lang.String, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<PolicyCondition> getPaginatedList(SearchPolicyCondition searchPolicyCondition, int startIndex, int limit, String sidx, String sord) {
		Map<String, Object> policyConditionConditions = policyConditionDao.getPolicyConditionBySearchParameters(searchPolicyCondition);
		List<PolicyCondition> policyList = policyConditionDao.getPaginatedList(PolicyCondition.class, (List<Criterion>) policyConditionConditions.get(CONDITIONS),
				(HashMap<String, String>) policyConditionConditions.get(ALIASES), startIndex, limit, sidx, sord);
		if(policyList != null){
			for(PolicyCondition policyCondition : policyList){
			//	Hibernate.initialize(policyCondition.getPolicyRuleSet());
				Hibernate.initialize(policyCondition.getPolicyRuleConditionRel());
			}
		}
		return policyList;
	}
	
	/* (non-Javadoc)
	 * @see com.elitecore.sm.policy.service.IPolicyService#saveCondition(com.elitecore.sm.policy.model.PolicyCondition)
	 */
	@Override
	@Transactional
	@Auditable(auditActivity = AuditConstants.CREATE_POLICY_CONDITION, actionType = BaseConstants.CREATE_ACTION, currentEntity = PolicyCondition.class, ignorePropList= "server")
	public ResponseObject saveCondition(PolicyCondition policyCondition) {
		ResponseObject responseObject = new ResponseObject();
		
		int conditionId = policyConditionDao.getConditionByAlies(policyCondition.getAlias(),policyCondition.getServer().getId());
		if (conditionId != 0 ) {
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.DUPLICATE_POLICY_CONDITION);
		} else {
			if("dynamic".equalsIgnoreCase(policyCondition.getType())) {
				DatabaseQuery databaseQuery = databaseQueryDao.getDatabaseQueryByAlias(policyCondition.getValue(), policyCondition.getServer().getId());
				if(databaseQuery != null) {
					policyCondition.setDatabaseQuery(databaseQuery);
					policyCondition.setDatabaseQueryAlias(policyCondition.getValue());
				}
			} else {
				policyCondition.setDatabaseQuery(null);
				policyCondition.setDatabaseQueryAlias(null);
			}
			String conditionExpressionForSync = policyCondition.getConditionExpression();
			if(conditionExpressionForSync!=null){
				policyCondition.setConditionExpressionForSync(conditionExpressionForSync.replaceAll("'", ""));//NOSONAR
			}

			policyConditionDao.save(policyCondition);
			
			if (policyCondition.getId() != 0) {
				responseObject.setSuccess(true);
				responseObject.setResponseCode(ResponseCode.POLICY_CONDITION_INSERT_SUCCESS);
			} else {
				responseObject.setSuccess(false);
				responseObject.setResponseCode(ResponseCode.POLICY_CONDITION_INSERT_FAIL);
			}
		}
		return responseObject;
	}
	
	/* (non-Javadoc)
	 * @see com.elitecore.sm.policy.service.IPolicyService#updateCondition(com.elitecore.sm.policy.model.PolicyCondition)
	 */
	@Override
	@Transactional
	@Auditable(auditActivity = AuditConstants.UPDATE_POLICY_CONDITION, actionType = BaseConstants.UPDATE_ACTION, currentEntity = PolicyCondition.class, ignorePropList= "server")
	public ResponseObject updateCondition(PolicyCondition policyCondition) {
		ResponseObject responseObject = new ResponseObject();
		
		int conditionId = policyConditionDao.getConditionByAlies(policyCondition.getAlias(),policyCondition.getServer().getId());
		if (conditionId != 0 && conditionId != policyCondition.getId()) {
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.DUPLICATE_POLICY_CONDITION);
		} else {
			if("dynamic".equalsIgnoreCase(policyCondition.getType())) {
				DatabaseQuery databaseQuery = databaseQueryDao.getDatabaseQueryByAlias(policyCondition.getValue(), policyCondition.getServer().getId());
				if(databaseQuery != null) {
					policyCondition.setDatabaseQuery(databaseQuery);
					policyCondition.setDatabaseQueryAlias(policyCondition.getValue());
				}
			} else {
				policyCondition.setDatabaseQuery(null);
				policyCondition.setDatabaseQueryAlias(null);
			}
			String conditionExpressionForSync = policyCondition.getConditionExpression();
			if(conditionExpressionForSync!=null){				
				policyCondition.setConditionExpressionForSync(conditionExpressionForSync.replaceAll("'", ""));//NOSONAR
			}
			policyConditionDao.merge(policyCondition);
			
			if (policyCondition.getId() != 0) {
				responseObject.setSuccess(true);
				responseObject.setResponseCode(ResponseCode.POLICY_CONDITION_UPDATED_SUCCESSFULL);
			} else {
				responseObject.setSuccess(false);
				responseObject.setResponseCode(ResponseCode.POLICY_CONDITION_UPDATE_FAILED);
			}
		}
		return responseObject;
	}
	
	/* (non-Javadoc)
	 * @see com.elitecore.sm.policy.service.IPolicyService#getPolicyConditionById(int)
	 */
	@Override
	@Transactional
	public PolicyCondition getPolicyConditionById(int id) {
		
		return policyConditionDao.findByPrimaryKey(PolicyCondition.class, id);
	}
	
	/* (non-Javadoc)
	 * @see com.elitecore.sm.policy.service.IPolicyService#deletePolicyCondition(int, int)
	 */
	@Override
	@Transactional
	@Auditable(auditActivity = AuditConstants.DELETE_POLICY_CONDITION, actionType = BaseConstants.DELETE_ACTION, currentEntity = PolicyCondition.class, ignorePropList= "server")
	public ResponseObject deletePolicyCondition(int conditionId, int staffId) {
		ResponseObject responseObject = new ResponseObject();
		PolicyCondition policyCondition = policyConditionDao.findByPrimaryKey(PolicyCondition.class, conditionId);
		if (policyCondition != null) {
			policyCondition.setName(EliteUtils.checkForNames(BaseConstants.ACTION_DELETE,policyCondition.getName()));
			policyCondition.setAlias(EliteUtils.checkForNames(BaseConstants.ACTION_DELETE,policyCondition.getAlias()));
			policyCondition.setStatus(StateEnum.DELETED);
			policyCondition.setLastUpdatedByStaffId(staffId);
			policyCondition.setLastUpdatedDate(new Date());
			policyConditionDao.merge(policyCondition);
			
			responseObject.setSuccess(true);
			responseObject.setObject(policyCondition);
			responseObject.setResponseCode(ResponseCode.POLICY_CONDITION_DELETE_SUCCESSFULL);
		} else {
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.POLICY_CONDITION_DELETE_FAIL);
		}
		return responseObject;
	}

	/*
	 * (non-Javadoc)
	 * @see com.elitecore.sm.policy.service.IPolicyService#getPolicyRuleById(int)
	 */
	@Transactional(readOnly = true)
	public PolicyRule getPolicyRuleById(int ruleId) {
		return policyRuleDao.findByPrimaryKey(PolicyRule.class, ruleId);
	}



	/*
	 * (non-Javadoc)
	 * @see com.elitecore.sm.policy.service.IPolicyService#getPolicyConditionCountByRule(int)
	 */
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public long getPolicyConditionCountByRule(int ruleId) {
		Map<String, Object> policyConditionCriterias = policyConditionDao.getPolicyConditionCriteriaByRuleId(ruleId);
		return policyConditionDao.getQueryCount(PolicyCondition.class, (List<Criterion>) policyConditionCriterias.get(CONDITIONS),
				(HashMap<String, String>) policyConditionCriterias.get(ALIASES));
	}

	/*
	 * (non-Javadoc)
	 * @see com.elitecore.sm.policy.service.IPolicyService#getPolicyActionCountByRule(int)
	 */
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public long getPolicyActionCountByRule(int ruleId) {
		Map<String, Object> policyActionCriterias = policyActionDao.getPolicyActionCriteriaByRuleId(ruleId);
		return policyActionDao.getQueryCount(PolicyAction.class, (List<Criterion>) policyActionCriterias.get(CONDITIONS),
				(HashMap<String, String>) policyActionCriterias.get(ALIASES));
	}

	/**
	 * (non-Javadoc)
	 * @see com.elitecore.sm.policy.service.IPolicyService#getPolicyConditionPaginatedListByRule(int, int, int, java.lang.String, java.lang.String)
	 *
	 *
	 *public List<PolicyCondition> getPolicyConditionPaginatedListByRule(int ruleId, int startIndex, int limit,
			String sidx, String sord) {
	 *
	 */
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public List<PolicyRuleConditionRel> getPolicyConditionPaginatedListByRule(int ruleId, int startIndex, int limit,
			String sidx, String sord) {
		Map<String, Object> policyConditionCriterias = policyConditionDao.getPolicyConditionCriteria_RuleId(ruleId);
		
		
		return policyRuleDao.getPolicyRuleConditionsPaginatedListByRuleID(PolicyRuleConditionRel.class, (List<Criterion>) policyConditionCriterias.get(CONDITIONS), 
				(HashMap<String, String>) policyConditionCriterias.get(ALIASES), startIndex, limit, sidx, sord);
	}

	/**
	 * (non-Javadoc)
	 * @see com.elitecore.sm.policy.service.IPolicyService#getPolicyActionPaginatedListByRule(int, int, int, java.lang.String, java.lang.String)
	 *
	 *public List<PolicyAction> getPolicyActionPaginatedListByRule(int ruleId, int startIndex, int limit, String sidx,
			String sord) {
	 */
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public List<PolicyRuleActionRel> getPolicyActionPaginatedListByRule(int ruleId, int startIndex, int limit, String sidx,
			String sord) {
		Map<String, Object> policyActionCriterias = policyActionDao.getPolicyActionCriteria_RuleId(ruleId);
		
		return policyRuleDao.getPolicyRuleActionsPaginatedListByRuleID(PolicyRuleActionRel.class, (List<Criterion>) policyActionCriterias.get(CONDITIONS), 
				(HashMap<String, String>) policyActionCriterias.get(ALIASES), startIndex, limit, sidx, sord);
		
		
	}


	/** get Policy Rule Count By Alias **/
	
	@Transactional
	public long getPolicyRuleCountByAlias(PolicyRule policyRule, int serverId) {		
		return policyRuleDao.getPolicyRuleCountByAlias(policyRule.getName(),serverId);
	}
	/*
	 * .(non-Javadoc)
	 * @see com.elitecore.sm.policy.service.IPolicyService#savePolicyRule(com.elitecore.sm.policy.model.PolicyRule, java.lang.String, java.lang.String, int, int)
	 *@Auditable(auditActivity = AuditConstants.CREATE_POLICY_RULE, actionType = BaseConstants.CREATE_ACTION, currentEntity = PolicyRule.class, ignorePropList= "server, policyConditionSet, policyActionSet")
	 *
	 */
	@Transactional	
	@Auditable(auditActivity = AuditConstants.CREATE_POLICY_RULE, actionType = BaseConstants.CREATE_ACTION, currentEntity = PolicyRule.class, ignorePropList= "server, policyRuleConditionRel,policyRuleActionRel")
	public ResponseObject savePolicyRule(PolicyRule policyRule, String ruleCondition, String ruleAction, int staffId, int serverId) {
		ResponseObject responseObject = new ResponseObject();
		
		if(policyRuleDao.getPolicyRuleCountByAlias(policyRule.getAlias(),serverId) > 0) {
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.DUPLICATE_POLICY_RULE);
		} else {
			
			
			List<PolicyRuleConditionRel> ruleConditionRelList = new ArrayList<>();
			
			if(StringUtils.isNotEmpty(ruleCondition)) {				
				JSONArray jsonArray = new JSONArray(ruleCondition);				
				for(int i = 0; i < jsonArray.length(); i++) {
					
					JSONObject jsonObj = jsonArray.getJSONObject(i);
					int conditionID = jsonObj.getInt("id");
					
					PolicyCondition policyCondition = policyConditionDao.findByPrimaryKey(PolicyCondition.class,  conditionID );
					PolicyRuleConditionRel policyRuleConditionRel = new PolicyRuleConditionRel();					
					
					policyRuleConditionRel.setApplicationOrder(i + 1);
					policyRuleConditionRel.setId(0);										
					policyRuleConditionRel.setPolicyRuleCon( policyRule );										
					policyRuleConditionRel.setCondition( policyCondition );
					
					ruleConditionRelList.add( policyRuleConditionRel );
				}
			}
			
			List<PolicyRuleActionRel> ruleActionRelList = new ArrayList<>();
			
			if(StringUtils.isNotEmpty(ruleCondition)) {				
				JSONArray jsonArray = new JSONArray(ruleAction );				
				for(int i = 0; i < jsonArray.length(); i++) {
					
					JSONObject jsonObj = jsonArray.getJSONObject(i);
					int aID = jsonObj.getInt("id");
						
					PolicyAction policyAction = policyActionDao.findByPrimaryKey(PolicyAction.class,  aID );
					PolicyRuleActionRel policyRuleActionRel = new PolicyRuleActionRel();					
					
					policyRuleActionRel.setApplicationOrder(i + 1);
					policyRuleActionRel.setId(0);										
					policyRuleActionRel.setPolicyRuleAction( policyRule );										
					policyRuleActionRel.setAction( policyAction );
					
					ruleActionRelList.add( policyRuleActionRel );
				}
			}
			
			ServerInstance server = serverInstanceDao.findByPrimaryKey(ServerInstance.class, serverId);
		//	policyRule.setPolicyConditionSet(policyConditionList);
		//	policyRule.setPolicyActionSet(policyActionList);
			policyRule.setPolicyRuleConditionRel( ruleConditionRelList );
			policyRule.setPolicyRuleActionRel( ruleActionRelList );
			
			policyRule.setServer(server);
			policyRuleDao.save(policyRule);
			responseObject.setSuccess(true);
			responseObject.setResponseCode(ResponseCode.POLICY_RULE_CREATE_SUCCESS);
		}
		
		return responseObject;
	}

	/*
	 * (non-Javadoc)
	 * @see com.elitecore.sm.policy.service.IPolicyService#updatePolicyRule(com.elitecore.sm.policy.model.PolicyRule, java.lang.String, java.lang.String, int, int)
	 *
	 *@Auditable(auditActivity = AuditConstants.UPDATE_POLICY_RULE, actionType = BaseConstants.UPDATE_ACTION, currentEntity = PolicyRule.class, ignorePropList= "server,policyConditionSet,policyActionSet")
	
	 */
	@Transactional
	@Auditable(auditActivity = AuditConstants.UPDATE_POLICY_RULE, actionType = BaseConstants.UPDATE_ACTION, currentEntity = PolicyRule.class, ignorePropList= "server,policyRuleConditionRel,policyRuleActionRel")
	public ResponseObject updatePolicyRule(PolicyRule policyRule, String ruleCondition, String ruleAction, int staffId,	int serverId) {
		ResponseObject responseObject = new ResponseObject();
		
		if(policyRuleDao.getPolicyRuleCountByAlias(policyRule.getAlias(),serverId) > 1) {
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.DUPLICATE_POLICY_RULE);
		} else {
			
			
			List<PolicyRuleConditionRel> ruleConditionRelList = new ArrayList<>();
			
			if(StringUtils.isNotEmpty(ruleCondition)) {				
				JSONArray jsonArray = new JSONArray(ruleCondition);				
				for(int i = 0; i < jsonArray.length(); i++) {
					
					JSONObject jsonObj = jsonArray.getJSONObject(i);
					int conditionID = jsonObj.getInt("id");
					
					PolicyCondition policyCondition = policyConditionDao.findByPrimaryKey(PolicyCondition.class,  conditionID );
					PolicyRuleConditionRel policyRuleConditionRel = new PolicyRuleConditionRel();					
					
					policyRuleConditionRel.setApplicationOrder(i + 1);
				//	policyRuleConditionRel.setId(0);										
					policyRuleConditionRel.setPolicyRuleCon( policyRule );										
					policyRuleConditionRel.setCondition( policyCondition );
					
					ruleConditionRelList.add( policyRuleConditionRel );
				}
			}
			
			
			/** start for new action list **/
			List<PolicyRuleActionRel> ruleActionRelList = new ArrayList<>();
			
			if(StringUtils.isNotEmpty(ruleCondition)) {				
				JSONArray jsonArray = new JSONArray(ruleAction );				
				for(int i = 0; i < jsonArray.length(); i++) {
					
					JSONObject jsonObj = jsonArray.getJSONObject(i);
					int aID = jsonObj.getInt("id");
						
					PolicyAction policyAction = policyActionDao.findByPrimaryKey(PolicyAction.class,  aID );
					PolicyRuleActionRel policyRuleActionRel = new PolicyRuleActionRel();					
					
					policyRuleActionRel.setApplicationOrder(i + 1);
					policyRuleActionRel.setId(0);										
					policyRuleActionRel.setPolicyRuleAction( policyRule );										
					policyRuleActionRel.setAction( policyAction );
					
					ruleActionRelList.add( policyRuleActionRel );
				}
			}			
			/**end for new action list **/
			
			PolicyRule oldPolicyRule = policyRuleDao.findByPrimaryKey(PolicyRule.class, policyRule.getId());
			
			oldPolicyRule.setAlert(policyRule.getAlert());
			oldPolicyRule.setAlertDescription(policyRule.getAlertDescription());
			oldPolicyRule.setAlias(policyRule.getAlias());
			oldPolicyRule.setDescription(policyRule.getDescription());
			oldPolicyRule.setGlobalSequenceRuleId(policyRule.getGlobalSequenceRuleId());
			oldPolicyRule.setLastUpdatedByStaffId(staffId);
			oldPolicyRule.setLastUpdatedDate(new Date());
			oldPolicyRule.setOperator(policyRule.getOperator());
			oldPolicyRule.setName(policyRule.getName());
			oldPolicyRule.setCategory(policyRule.getCategory());
			oldPolicyRule.setSeverity(policyRule.getSeverity());
			oldPolicyRule.setErrorCode(policyRule.getErrorCode());
			
			
			
			Iterator<PolicyRuleConditionRel> policyRuleConditionRelIter = oldPolicyRule.getPolicyRuleConditionRel().iterator();			
			while(policyRuleConditionRelIter.hasNext()) {
				policyRuleConditionRelIter.next();
				policyRuleConditionRelIter.remove();
			}			
			for(PolicyRuleConditionRel ruleConditionRel : ruleConditionRelList) {
				oldPolicyRule.getPolicyRuleConditionRel().add( ruleConditionRel );
			}
			
			
			Iterator<PolicyRuleActionRel> policyActionIter = oldPolicyRule.getPolicyRuleActionRel().iterator();
			while(policyActionIter.hasNext()) {
				policyActionIter.next();
				policyActionIter.remove();
			}
			for(PolicyRuleActionRel policyAction : ruleActionRelList) {
				oldPolicyRule.getPolicyRuleActionRel().add(policyAction);
			}
			

			policyRuleDao.merge(oldPolicyRule);
			
			responseObject.setSuccess(true);
			responseObject.setResponseCode(ResponseCode.POLICY_RULE_UPDATE_SUCCESS);
		}
		return responseObject;
	}

	/*
	 * (non-Javadoc)
	 * @see com.elitecore.sm.policy.service.IPolicyService#removePolicyRule(int)
	 *@Auditable(auditActivity = AuditConstants.DELETE_POLICY_RULE, actionType = BaseConstants.DELETE_ACTION, currentEntity = PolicyRule.class, ignorePropList= "server, policyConditionSet, policyActionSet")
	 *
	 */
	@Transactional
	@Auditable(auditActivity = AuditConstants.DELETE_POLICY_RULE, actionType = BaseConstants.DELETE_ACTION, currentEntity = PolicyRule.class, ignorePropList= "server, policyRuleConditionRel,policyRuleActionRel")
	public ResponseObject removePolicyRule(int policyRuleId) {
		ResponseObject responseObject = new ResponseObject();
		
		PolicyRule policyRule = policyRuleDao.findByPrimaryKey(PolicyRule.class, policyRuleId);
		policyRule.setStatus(StateEnum.DELETED);
		policyRule.setName(EliteUtils.checkForNames(BaseConstants.ACTION_DELETE, policyRule.getName()));
		policyRule.setAlias(EliteUtils.checkForNames(BaseConstants.ACTION_DELETE, policyRule.getAlias()));
		PolicyRule oldPolicyRule = policyRuleDao.findByPrimaryKey(PolicyRule.class, policyRule.getId());
		
		
		Iterator<PolicyRuleConditionRel> policyRuleConditionRelIter = oldPolicyRule.getPolicyRuleConditionRel().iterator();			
		while(policyRuleConditionRelIter.hasNext()) {
			policyRuleConditionRelIter.next();
			policyRuleConditionRelIter.remove();
		}			
				
		Iterator<PolicyRuleActionRel> policyActionIter = oldPolicyRule.getPolicyRuleActionRel().iterator();
		while(policyActionIter.hasNext()) {
			policyActionIter.next();
			policyActionIter.remove();
		}
		
		policyRuleDao.merge(oldPolicyRule);
		policyRuleDao.merge(policyRule);
		
		responseObject.setSuccess(true);
		responseObject.setResponseCode(ResponseCode.POLICY_RULE_REMOVE_SUCCESS);
		
		return responseObject;
	}
	
	@Transactional(rollbackFor = SMException.class)
	@Override
	public void iteratePolicyCondition(ServerInstance serverInstance, PolicyCondition exportedPolicyCondition, Map<String, Integer> policyConditionMap, boolean isImport) {

		if (isImport) { // import call
			
			String oldPolicyConditionName = exportedPolicyCondition.getAlias();
			exportedPolicyCondition.setId(0);
			exportedPolicyCondition.setName(exportedPolicyCondition.getName());
			exportedPolicyCondition.setAlias( exportedPolicyCondition.getAlias());
			exportedPolicyCondition.setCreatedByStaffId(serverInstance.getLastUpdatedByStaffId());
			exportedPolicyCondition.setCreatedDate(new Date());
			exportedPolicyCondition.setServer(serverInstance);
			exportedPolicyCondition.setLastUpdatedByStaffId(serverInstance.getLastUpdatedByStaffId());
			
			policyConditionDao.save(exportedPolicyCondition);
			
			logger.debug("After Policy Condition Save , new Id for policy condition name is : " + exportedPolicyCondition.getName() + " id is ::  " + exportedPolicyCondition.getId());
			if (policyConditionMap != null && oldPolicyConditionName != null && !oldPolicyConditionName.isEmpty()) {
				policyConditionMap.put(oldPolicyConditionName, exportedPolicyCondition.getId());
			}
			

		} else { // delete call
			exportedPolicyCondition.setName(EliteUtils.checkForNames(BaseConstants.ACTION_DELETE, exportedPolicyCondition.getName()));
			exportedPolicyCondition.setAlias(EliteUtils.checkForNames(BaseConstants.ACTION_DELETE, exportedPolicyCondition.getAlias()));
			exportedPolicyCondition.setStatus(StateEnum.DELETED);
			exportedPolicyCondition.setLastUpdatedDate(new Date());

		}

		exportedPolicyCondition.setLastUpdatedByStaffId(serverInstance.getLastUpdatedByStaffId());
		exportedPolicyCondition.setLastUpdatedDate(new Date());

	}

	@Transactional(rollbackFor = SMException.class)
	@Override
	public void iteratePolicyAction(ServerInstance serverInstance, PolicyAction exportedPolicyAction, Map<String, Integer> policyActionMap, boolean isImport) {

		if (isImport) { // import call

			String oldPolicyActionName = exportedPolicyAction.getAlias();
			exportedPolicyAction.setId(0);
			exportedPolicyAction.setName(exportedPolicyAction.getName());
			exportedPolicyAction.setAlias(exportedPolicyAction.getAlias());
			exportedPolicyAction.setCreatedByStaffId(serverInstance.getLastUpdatedByStaffId());
			exportedPolicyAction.setCreatedDate(new Date());
			exportedPolicyAction.setServer(serverInstance);
			exportedPolicyAction.setLastUpdatedByStaffId(serverInstance.getLastUpdatedByStaffId());
			
			policyActionDao.save(exportedPolicyAction);
			
			logger.debug("After Policy Action Save , new Id for policy action name is : " + exportedPolicyAction.getName() + " id is ::  " + exportedPolicyAction.getId());
			if (policyActionMap != null && oldPolicyActionName != null && !oldPolicyActionName.isEmpty()) {
				policyActionMap.put(oldPolicyActionName, exportedPolicyAction.getId());
			}
			

		} else { // delete call
			exportedPolicyAction.setName(EliteUtils.checkForNames(BaseConstants.ACTION_DELETE, exportedPolicyAction.getName()));
			exportedPolicyAction.setAlias(EliteUtils.checkForNames(BaseConstants.ACTION_DELETE, exportedPolicyAction.getAlias()));
			exportedPolicyAction.setStatus(StateEnum.DELETED);
			exportedPolicyAction.setLastUpdatedDate(new Date());

		}

		exportedPolicyAction.setLastUpdatedByStaffId(serverInstance.getLastUpdatedByStaffId());
		exportedPolicyAction.setLastUpdatedDate(new Date());

	}
	
	@Transactional(rollbackFor = SMException.class)
	@Override
	public void iteratePolicyRule(ServerInstance serverInstance, PolicyRule exportedPolicyRule, Map<String, Integer> policyRuleMap, Map<String, Integer> policyConditionMap, Map<String, Integer> policyActionMap, boolean isImport) {

		if (isImport) { // import call

			String oldPolicyRuleName = exportedPolicyRule.getAlias();
			exportedPolicyRule.setId(0);
			exportedPolicyRule.setName(exportedPolicyRule.getName());
			exportedPolicyRule.setAlias(exportedPolicyRule.getAlias());
			exportedPolicyRule.setCreatedByStaffId(serverInstance.getLastUpdatedByStaffId());
			exportedPolicyRule.setCreatedDate(new Date());
			exportedPolicyRule.setServer(serverInstance);
			exportedPolicyRule.setLastUpdatedByStaffId(serverInstance.getLastUpdatedByStaffId());
			
			List<String> policyConditions = exportedPolicyRule.getPolicyConditions();
			//List<PolicyCondition> policyConditionSet = new ArrayList<>();
			
			List<PolicyRuleConditionRel> ruleConditionRelList = new ArrayList<>();
			
			for (String policyCondition : policyConditions) {
				if (policyConditionMap.containsKey(policyCondition)) {
					
					// policyConditionSet.add(policyConditionDao.findByPrimaryKey(PolicyCondition.class, policyConditionMap.get(policyCondition)));
										
					PolicyRuleConditionRel policyRuleConditionRel = new PolicyRuleConditionRel();	
					policyRuleConditionRel.setPolicyRuleCon( exportedPolicyRule );
					policyRuleConditionRel.setCondition(  policyConditionDao.findByPrimaryKey(PolicyCondition.class, policyConditionMap.get(policyCondition)));					
					ruleConditionRelList.add( policyRuleConditionRel );
				}
			}
				
			exportedPolicyRule.setPolicyRuleConditionRel( ruleConditionRelList );			
		//	exportedPolicyRule.setPolicyConditionSet(policyConditionSet);
			
			
			List<String> policyActions = exportedPolicyRule.getPolicyActions();
		//	List<PolicyAction> policyActionSet = new ArrayList<>();
			List<PolicyRuleActionRel> ruleActionRelList = new ArrayList<>();
			
			for (String policyAction : policyActions) {
				if (policyActionMap.containsKey(policyAction)) {
					//policyActionSet.add(policyActionDao.findByPrimaryKey(PolicyAction.class, policyActionMap.get(policyAction)));
					
					PolicyRuleActionRel policyRuleActionRel = new PolicyRuleActionRel();					
					policyRuleActionRel.setPolicyRuleAction( exportedPolicyRule );
					policyRuleActionRel.setAction( policyActionDao.findByPrimaryKey(PolicyAction.class, policyActionMap.get(policyAction)) );					
					ruleActionRelList.add( policyRuleActionRel );
				}
			}
			
			exportedPolicyRule.setPolicyRuleActionRel( ruleActionRelList );
		//	exportedPolicyRule.setPolicyActionSet(policyActionSet);
			
			policyRuleDao.save(exportedPolicyRule);
			
			logger.debug("After Policy Save , new Id for policy name is : " + exportedPolicyRule.getName() + " id is ::  " + exportedPolicyRule.getId());
			if (policyRuleMap != null && oldPolicyRuleName != null && !oldPolicyRuleName.isEmpty()) {
				policyRuleMap.put(oldPolicyRuleName, exportedPolicyRule.getId());
			}
			

		} else { // delete call
			exportedPolicyRule.setName(EliteUtils.checkForNames(BaseConstants.ACTION_DELETE, exportedPolicyRule.getName()));
			exportedPolicyRule.setAlias(EliteUtils.checkForNames(BaseConstants.ACTION_DELETE, exportedPolicyRule.getAlias()));
			exportedPolicyRule.setStatus(StateEnum.DELETED);
			exportedPolicyRule.setLastUpdatedDate(new Date());

		}

		exportedPolicyRule.setLastUpdatedByStaffId(serverInstance.getLastUpdatedByStaffId());
		exportedPolicyRule.setLastUpdatedDate(new Date());

	}
	
	@Transactional(rollbackFor = SMException.class)
	@Override
	public void iteratePolicyGroup(ServerInstance serverInstance, PolicyGroup exportedPolicyGroup, Map<String, Integer> policyGroupMap, Map<String, Integer> policyRuleMap, boolean isImport) {

		if (isImport) { // import call

			String oldPolicyGroupName = exportedPolicyGroup.getAlias();
			exportedPolicyGroup.setId(0);
			exportedPolicyGroup.setName(exportedPolicyGroup.getName());
			exportedPolicyGroup.setAlias(exportedPolicyGroup.getAlias());
			exportedPolicyGroup.setCreatedByStaffId(serverInstance.getLastUpdatedByStaffId());
			exportedPolicyGroup.setCreatedDate(new Date());
			exportedPolicyGroup.setServer(serverInstance);
			exportedPolicyGroup.setLastUpdatedByStaffId(serverInstance.getLastUpdatedByStaffId());
			
			List<String> policyRules = exportedPolicyGroup.getPolicyRules();
			List<PolicyGroupRuleRel> policyGroupRuleRelSet = new ArrayList<>();
			int applicationOrader = 1;
			for (String policyRule : policyRules) {
				if (policyRuleMap.containsKey(policyRule)) {
					PolicyRule tempPolicyRule = policyRuleDao.findByPrimaryKey(PolicyRule.class, policyRuleMap.get(policyRule));
					PolicyGroupRuleRel tempPolicyGroupRuleRel = new PolicyGroupRuleRel();
					tempPolicyGroupRuleRel.setId(0);
					tempPolicyGroupRuleRel.setCreatedByStaffId(serverInstance.getLastUpdatedByStaffId());
					tempPolicyGroupRuleRel.setCreatedDate(new Date());
					tempPolicyGroupRuleRel.setLastUpdatedByStaffId(serverInstance.getLastUpdatedByStaffId());
					tempPolicyGroupRuleRel.setLastUpdatedDate(new Date());
					tempPolicyGroupRuleRel.setApplicationOrder(applicationOrader++);
					tempPolicyGroupRuleRel.setPolicyRule(tempPolicyRule);
					tempPolicyGroupRuleRel.setGroup(exportedPolicyGroup);
					policyGroupRuleRelSet.add(tempPolicyGroupRuleRel);
				}
			}
			exportedPolicyGroup.setPolicyGroupRuleRelSet(policyGroupRuleRelSet);
			
			
			policyGroupDao.save(exportedPolicyGroup);
			
			logger.debug("After Policy Save , new Id for policy name is : " + exportedPolicyGroup.getName() + " id is ::  " + exportedPolicyGroup.getId());
			if (policyGroupMap != null && oldPolicyGroupName != null && !oldPolicyGroupName.isEmpty()) {
				policyGroupMap.put(oldPolicyGroupName, exportedPolicyGroup.getId());
			}

		} else { // delete call
			exportedPolicyGroup.setName(EliteUtils.checkForNames(BaseConstants.ACTION_DELETE, exportedPolicyGroup.getName()));
			exportedPolicyGroup.setAlias(EliteUtils.checkForNames(BaseConstants.ACTION_DELETE, exportedPolicyGroup.getAlias()));
			exportedPolicyGroup.setStatus(StateEnum.DELETED);
			exportedPolicyGroup.setLastUpdatedDate(new Date());

		}

		exportedPolicyGroup.setLastUpdatedByStaffId(serverInstance.getLastUpdatedByStaffId());
		exportedPolicyGroup.setLastUpdatedDate(new Date());

	}
	
	@Transactional(rollbackFor = SMException.class)
	@Override
	public void iteratePolicy(ServerInstance serverInstance, Policy exportedPolicy, Map<String, Integer> policyMap, Map<String, Integer> policyGroupMap, boolean isImport) {

		if (isImport) { // import call

			String oldPolicyName = exportedPolicy.getAlias();
			exportedPolicy.setId(0);
			exportedPolicy.setName(exportedPolicy.getName());
			exportedPolicy.setAlias(exportedPolicy.getAlias());
			exportedPolicy.setCreatedByStaffId(serverInstance.getLastUpdatedByStaffId());
			exportedPolicy.setCreatedDate(new Date());
			exportedPolicy.setServer(serverInstance);
			exportedPolicy.setLastUpdatedByStaffId(serverInstance.getLastUpdatedByStaffId());
			
			List<String> policyGroups = exportedPolicy.getPolicyGroups();
			List<PolicyGroupRel> policyGroupRelSet = new ArrayList<>();
			int applicationOrader = 1;
			for (String policyGroup : policyGroups) {
				if (policyGroupMap.containsKey(policyGroup)) {
					PolicyGroup tempPolicyGroup = policyGroupDao.findByPrimaryKey(PolicyGroup.class, policyGroupMap.get(policyGroup));
					PolicyGroupRel tempPolicyRuleRel = new PolicyGroupRel();
					tempPolicyRuleRel.setId(0);
					tempPolicyRuleRel.setCreatedByStaffId(serverInstance.getLastUpdatedByStaffId());
					tempPolicyRuleRel.setCreatedDate(new Date());
					tempPolicyRuleRel.setLastUpdatedByStaffId(serverInstance.getLastUpdatedByStaffId());
					tempPolicyRuleRel.setLastUpdatedDate(new Date());
					tempPolicyRuleRel.setApplicationOrder(applicationOrader++);
					tempPolicyRuleRel.setGroup(tempPolicyGroup);
					tempPolicyRuleRel.setPolicy(exportedPolicy);
					policyGroupRelSet.add(tempPolicyRuleRel);
				}
			}
			exportedPolicy.setPolicyGroupRelSet(policyGroupRelSet);
			
			
			policyDao.save(exportedPolicy);
			
			logger.debug("After Policy Save , new Id for policy name is : " + exportedPolicy.getName() + " id is ::  " + exportedPolicy.getId());
			if (policyMap != null && oldPolicyName != null && !oldPolicyName.isEmpty()) {
				policyMap.put(oldPolicyName, exportedPolicy.getId());
			}

		} else { // delete call
			exportedPolicy.setName(EliteUtils.checkForNames(BaseConstants.ACTION_DELETE, exportedPolicy.getName()));
			exportedPolicy.setAlias(EliteUtils.checkForNames(BaseConstants.ACTION_DELETE, exportedPolicy.getAlias()));
			exportedPolicy.setStatus(StateEnum.DELETED);
			exportedPolicy.setLastUpdatedDate(new Date());

		}

		exportedPolicy.setLastUpdatedByStaffId(serverInstance.getLastUpdatedByStaffId());
		exportedPolicy.setLastUpdatedDate(new Date());

	}


	/**
	 * Method will get all rule list by service and action type. 
	 * 
	 */
	@Override
	@Transactional
	public ResponseObject getAllRuleByServiceAndAction(Integer[] serviceIds, String actionType,String reasonCategory,String reasonSeverity,String reasonErrorCode) {
		ResponseObject responseObject = new ResponseObject();
		
		Map<String,List<Object[]>> objectList  = policyRuleDao.getAllRuleByServiceAndAction(serviceIds, actionType,reasonCategory,reasonSeverity,reasonErrorCode);
		if(objectList != null && !objectList.isEmpty()){
			logger.info("rule details fetch successfully!");
			JSONArray jsonArray = new JSONArray();
			JSONObject jsonObject ;
			List<Object[]> groupList = objectList.get("Group");
			if(groupList != null && !groupList.isEmpty()){
				for (Object[] object : groupList) {
					jsonObject = new JSONObject();
					jsonObject.put("id", object[0]);
					jsonObject.put("name", object[1]);
					jsonArray.put(jsonObject);
				}
			}
			
			List<Object[]> ruleList = objectList.get("Rule");
			if(ruleList != null && !ruleList.isEmpty()){
				for (Object[] object : ruleList) {
					jsonObject = new JSONObject();
					jsonObject.put("id", object[0]);
					jsonObject.put("name", object[1]);
					jsonArray.put(jsonObject);
				}
			}
			
			List<Object[]> naList = objectList.get("NA");
			if(naList != null && !naList.isEmpty()){
				jsonObject = new JSONObject();
				jsonObject.put("id", naList.get(0)[0]);
				jsonObject.put("name", "DEFAULT");
				jsonArray.put(jsonObject);
			}
			
			responseObject.setSuccess(true);
			responseObject.setObject(jsonArray);
		}else{
			logger.info("Failed to get rule details.!");
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.FAIL_GET_POLICY_RULE);
		}
		
		return responseObject;
	}

	@Override
	public List<ImportValidationErrors> validatePolicyConditionForImport(PolicyCondition policyCondition,
			List<ImportValidationErrors> importErrorList, int serverId) {
		//policyConditionValidator.validateNameForUniqueness(policyCondition, null, importErrorList, null, true,serverId);
		return importErrorList;
	}

	@Override
	public List<ImportValidationErrors> validatePolicyActionForImport(PolicyAction policyAction, List<ImportValidationErrors> importErrorList,
			int serverId) {
		//policyActionValidator.validateNameForUniqueness(policyAction, null, importErrorList, null, true,serverId);
		return importErrorList;
	}

	@Override
	public List<ImportValidationErrors> validatePolicyRuleForImport(PolicyRule policyRule,
			List<ImportValidationErrors> importErrorList, int serverId) {
		//policyRuleValidator.validateNameForUniqueness(policyRule, null, importErrorList, null, true,serverId);
		return importErrorList;
	}



	@Override
	public List<ImportValidationErrors> validatePolicyGroupForImport(PolicyGroup policyGroup,
			List<ImportValidationErrors> importErrorList, int serverId) {
		//policyGroupValidator.validateNameForUniqueness(policyGroup,null,importErrorList,null,true,serverId);
		return importErrorList;
	}



	@Override
	@Transactional
	public void saveRuleHistory(PolicyRuleHistory policyRuleHistory) {
		policyRuleDao.saveRuleHistory(policyRuleHistory);
		
	}
	
	@Override
	@Transactional(readOnly = true)
	public ResponseObject validateActionExpression(String serverInstanceId, String expressionStr) {		
		ResponseObject responseObject = new ResponseObject();
		ServerInstance serverInstance = serverInstanceDao.getServerInstance(Integer.parseInt(serverInstanceId));
		if (serverInstance != null) {
			RemoteJMXHelper jmxConnection = new RemoteJMXHelper(serverInstance.getServer().getIpAddress(), serverInstance.getPort(), serverInstance.getMaxConnectionRetry(), serverInstance.getRetryInterval(),
					serverInstance.getConnectionTimeout());
			boolean responseMsg;
			String versionInfo = jmxConnection.versionInformation();

			if (versionInfo != null && jmxConnection.getErrorMessage() == null) {

				if (isServerRunning(serverInstance)) {
					responseMsg = (boolean) jmxConnection.isActionExpressionValid(expressionStr);
					responseObject.setSuccess(true);
					responseObject.setObject(responseMsg);
				} else {
					responseObject.setObject(null);
					responseObject.setSuccess(false);
					responseObject.setResponseCode(ResponseCode.EXPRESSION_VALIDATION_FAILED);
				}
			} else {
				responseObject.setObject(null);
				responseObject.setSuccess(false);
				responseObject.setResponseCode(ResponseCode.EXPRESSION_VALIDATION_FAILED);
			}

		} else {
			responseObject.setObject(null);
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.SERVER_DOES_NOT_EXIST);
		}

		return responseObject;
	}
	
	@Override
	@Transactional(readOnly = true)
	public ResponseObject validateConditionExpression(String serverInstanceId, String expressionStr) {		
		ResponseObject responseObject = new ResponseObject();
		ServerInstance serverInstance = serverInstanceDao.getServerInstance(Integer.parseInt(serverInstanceId));
		if (serverInstance != null) {
			RemoteJMXHelper jmxConnection = new RemoteJMXHelper(serverInstance.getServer().getIpAddress(), serverInstance.getPort(), serverInstance.getMaxConnectionRetry(), serverInstance.getRetryInterval(),
					serverInstance.getConnectionTimeout());
			boolean responseMsg;
			String versionInfo = jmxConnection.versionInformation();

			if (versionInfo != null && jmxConnection.getErrorMessage() == null) {

				if (isServerRunning(serverInstance)) {
					responseMsg = (boolean) jmxConnection.isConditionExpressionValid(expressionStr);
					responseObject.setSuccess(true);
					responseObject.setObject(responseMsg);
				} else {
					responseObject.setObject(null);
					responseObject.setSuccess(false);
					responseObject.setResponseCode(ResponseCode.EXPRESSION_VALIDATION_FAILED);
				}
			} else {
				responseObject.setObject(null);
				responseObject.setSuccess(false);
				responseObject.setResponseCode(ResponseCode.EXPRESSION_VALIDATION_FAILED);
			}

		} else {
			responseObject.setObject(null);
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.SERVER_DOES_NOT_EXIST);
		}

		return responseObject;
	}

	@Transactional(readOnly = true)
	public boolean isServerRunning(ServerInstance serverInstance) {

		RemoteJMXHelper jmxCall = new RemoteJMXHelper(serverInstance.getServer().getIpAddress(), serverInstance.getPort(),
				serverInstance.getMaxConnectionRetry(), serverInstance.getRetryInterval(), serverInstance.getConnectionTimeout());

		boolean runningStatus = false;
		CrestelNetServerDetails crestelNetServerDetails = jmxCall.readServerDetails();

		if (crestelNetServerDetails != null) {
			runningStatus = true;
		} else {
			logger.info("ServerInstance is not running: " + serverInstance.getPort());
			runningStatus = false;
		}
		return runningStatus;
	}
	
}
