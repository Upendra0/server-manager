package com.elitecore.sm.policy.service;

import java.io.File;
import java.util.Map;

import com.elitecore.sm.common.exception.SMException;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.policy.model.DatabaseQuery;
import com.elitecore.sm.policy.model.Policy;
import com.elitecore.sm.policy.model.PolicyAction;
import com.elitecore.sm.policy.model.PolicyCondition;
import com.elitecore.sm.policy.model.PolicyGroup;
import com.elitecore.sm.policy.model.PolicyRule;
import com.elitecore.sm.serverinstance.model.ServerInstance;


public interface IPolicyImportExportService {
	
	public ResponseObject getPolicyFullHierarchy(int policyId, boolean isExportForDelete, String tempPathForExport)
			throws SMException;
	
	public ResponseObject importPolicyConfig(int importServerInstanceId, int importPolicyId, File importFile, int staffId, int importMode, String jaxbXMLPath,
			boolean isCopy) throws SMException;
	
	public void importPolicyCondition(ServerInstance serverInstance, PolicyCondition exportedPolicyCondition, Map<String, Integer> policyConditionMap, Map<String, Integer> databaseQueryMap, int importMode);
	
	public void importPolicyAction(ServerInstance serverInstance, PolicyAction exportedPolicyAction, Map<String, Integer> policyActionMap, Map<String, Integer> databaseQueryMap, int importMode);
	
	public void importDatabaseQuery(ServerInstance serverInstance, DatabaseQuery exportedDatabaseQuery, Map<String, Integer> databaseQueryMap, int importMode);
	
	public void importPolicyRule(ServerInstance serverInstance, PolicyRule exportedPolicyRule, Map<String, Integer> policyRuleMap, Map<String, Integer> policyConditionMap, Map<String, Integer> policyActionMap, int importMode);
	
	public void importPolicyGroup(ServerInstance serverInstance, PolicyGroup exportedPolicyGroup, Map<String, Integer> policyGroupMap, Map<String, Integer> policyRuleMap, int importMode);
	
	public void importPolicy(ServerInstance serverInstance, Policy exportedPolicy, int dbPolicyId, Map<String, Integer> policyMap, Map<String, Integer> policyGroupMap);
	
	public void importPolicyForServerInstance(ServerInstance serverInstance, Policy exportedPolicy, Map<String, Integer> policyMap, Map<String, Integer> policyGroupMap, int importMode);
	
	public ResponseObject removePolicyDependant(String policyAlias,int serverInstanceId,int dbPolicyId);
		
}
