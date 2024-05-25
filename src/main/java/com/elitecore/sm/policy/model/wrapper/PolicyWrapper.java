package com.elitecore.sm.policy.model.wrapper;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.elitecore.sm.policy.model.DatabaseQuery;
import com.elitecore.sm.policy.model.Policy;
import com.elitecore.sm.policy.model.PolicyAction;
import com.elitecore.sm.policy.model.PolicyCondition;
import com.elitecore.sm.policy.model.PolicyGroup;
import com.elitecore.sm.policy.model.PolicyRule;

@XmlRootElement
@XmlType(propOrder = { "policyList", "policyGroupList", "policyRuleList",
		"policyConditionList", "policyActionList", "databaseQueryList" })
public class PolicyWrapper {

	private List<Policy> policyList = new ArrayList<>(0);
	private List<PolicyGroup> policyGroupList = new ArrayList<>(0);
	private List<PolicyRule> policyRuleList = new ArrayList<>(0);
	private List<PolicyCondition> policyConditionList = new ArrayList<>(0);
	private List<PolicyAction> policyActionList = new ArrayList<>(0);
	private List<DatabaseQuery> databaseQueryList = new ArrayList<>(0);

	/**
	 * Gets Policy List
	 * 
	 * @return the policy list
	 */
	@XmlElement
	public List<Policy> getPolicyList() {
		return policyList;
	}

	/**
	 * Sets Policy List
	 * 
	 * @param policyList
	 *            the Policy List to be set
	 */
	public void setPolicyList(List<Policy> policyList) {
		this.policyList = policyList;
	}

	/**
	 * Gets Policy Group List
	 * 
	 * @return the policy group list
	 */
	@XmlElement
	public List<PolicyGroup> getPolicyGroupList() {
		return policyGroupList;
	}

	/**
	 * Sets Policy Group List
	 * 
	 * @param policyGroupList
	 *            the policy group list to be set
	 */
	public void setPolicyGroupList(List<PolicyGroup> policyGroupList) {
		this.policyGroupList = policyGroupList;
	}

	/**
	 * Gets Policy rule list
	 * 
	 * @return the policy rule list
	 */
	@XmlElement
	public List<PolicyRule> getPolicyRuleList() {
		return policyRuleList;
	}

	/**
	 * Sets Policy rule list
	 * 
	 * @param policyRuleList
	 *            the policy rule list to set
	 */
	public void setPolicyRuleList(List<PolicyRule> policyRuleList) {
		this.policyRuleList = policyRuleList;
	}

	/**
	 * Gets Policy Condition List
	 * 
	 * @return the policy condition list
	 */
	@XmlElement
	public List<PolicyCondition> getPolicyConditionList() {
		return policyConditionList;
	}

	/**
	 * Sets Policy Condition List
	 * 
	 * @param policyConditionList
	 *            the policy condition list to be set
	 */
	public void setPolicyConditionList(List<PolicyCondition> policyConditionList) {
		this.policyConditionList = policyConditionList;
	}

	/**
	 * Gets Policy Action List
	 * 
	 * @return the policy action list
	 */
	@XmlElement
	public List<PolicyAction> getPolicyActionList() {
		return policyActionList;
	}

	/**
	 * Sets Policy Action List
	 * 
	 * @param policyActionList
	 *            the policy action list to be set
	 */
	public void setPolicyActionList(List<PolicyAction> policyActionList) {
		this.policyActionList = policyActionList;
	}

	/**
	 * Gets Database Query List
	 * 
	 * @return the database query list
	 */
	@XmlElement
	public List<DatabaseQuery> getDatabaseQueryList() {
		return databaseQueryList;
	}

	/**
	 * Sets Database Query List
	 * 
	 * @param the
	 *            database query list
	 */
	public void setDatabaseQueryList(List<DatabaseQuery> databaseQueryList) {
		this.databaseQueryList = databaseQueryList;
	}
}
