package com.elitecore.sm.common.model;

import java.util.ArrayList;
import java.util.List;

import com.elitecore.sm.policy.model.PolicyConditionOperator;

public enum PolicyConditionOperatorEnum {
	
	BEST_MATCH_OPERATOR	("Best Match Operator", "BestMatchOperator"),
	CONTAINS_OPERATOR	("Contains Operator", "ContainsOperator"),
	DOESNOT_CONTAINS_OPERATOR	("DoesNot Contains Operator", "DoesNotContainsOperator"),
	ENDWITH_OPERATOR	("EndWith Operator", "EndWithOperator"),
	EQUALSTO_OPERATOR	("EqualsTo Operator", "EqualsToOperator"),
	IN_OPERATOR	("In Operator", "InOperator"),
	ISBLANK_OPERATOR	("IsBlank Operator", "IsBlankOperator"),
	ISGREATORTHEN_OPERATOR	("IsGreatorThen Operator", "IsGreatorThenOperator"),
	ISGREATORTHEN_OPERATOR_FOR_NUMERIC	("IsGreatorThen Operator For Numeric", "IsGreatorThenOperatorForNumeric"),
	ISGREATORTHEN_OR_EQUALSTOOPERATOR	("IsGreatorThen Or EqualsToOperator", "IsGreatorThenOrEqualsToOperator"),
	ISGREATORTHEN_OR_EQUALSTOOPERATOR_FOR_NUMERIC	("IsGreatorThen Or EqualsToOperator For Numeric", "IsGreatorThenOrEqualsToOperatorForNumeric"),
	ISLESSTHEN_OPERATOR	("IsLessThen Operator", "IsLessThenOperator"),
	ISLESSTHEN_OPERATOR_FOR_NUMERIC	("IsLessThen Operator For Numeric", "IsLessThenOperatorForNumeric"),
	ISLESSTHEN_OR_EQUALSTOOPERATOR	("IsLessThen Or EqualsToOperator", "IsLessThenOrEqualsToOperator"),
	ISLESSTHEN_OR_EQUALSTOOPERATOR_FOR_NUMERIC	("IsLessThen Or EqualsToOperator For Numeric", "IsLessThenOrEqualsToOperatorForNumeric"),
	NOT_BLANK_OPERATOR	("Not Blank Operator", "IsNotBlankOperator"),
	NOT_NULL_OPERATOR	("Not Null Operator", "IsNotNullOperator"),
	NULL_OPERATOR	("Null Operator", "IsNullOperator"),
	NOTENDWITHOPERATOR	("NotEndWithOperator", "NotEndWithOperator"),
	NOT_EQUALSTO_OPERATOR	("Not EqualsTo Operator", "NotEqualsToOperator"),
	NOTIN_OPERATOR	("NotIn Operator", "NotInOperator"),
	NOTSTARTWITHOPERATOR	("NotStartWithOperator", "NotStartWithOperator"),
	REGULAR_EXPRESSION	("Regular Expression", "RegularExpression"),
	STARTWITH_OPERATOR	("StartWith Operator", "StartWithOperator"),
	ISGREATORTHEN_OPERATOR_FOR_DATE	("IsGreatorThen Operator For Date", "IsGreatorThenOperatorForDate"),
	ISGREATORTHEN_OR_EQUALSTOOPERATOR_FOR_DATE	("IsGreatorThen Or EqualsToOperator For Date", "IsGreatorThenOrEqualsToOperatorForDate"),
	ISLESSTHEN_OPERATOR_FOR_DATE	("IsLessThen Operator For Date", "IsLessThenOperatorForDate"),
	ISLESSTHEN_OR_EQUALSTOOPERATOR_FOR_DATE	("IsLessThen Or EqualsToOperator For Date", "IsLessThenOrEqualsToOperatorForDate");
	
	
	private String name;
	private String value;
	
	private PolicyConditionOperatorEnum(String name, String value) {
		this.name = name;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	public static List<PolicyConditionOperator> getPolicyConditionList() {
		List<PolicyConditionOperator> policyConditionOperatorList = new ArrayList<>();
		PolicyConditionOperatorEnum[] policyConditionOperators = PolicyConditionOperatorEnum.values();
		
		for(int i = 0; i < policyConditionOperators.length; i++) {
			PolicyConditionOperator policyConditionOperator = new PolicyConditionOperator(policyConditionOperators[i].getName(), 
					policyConditionOperators[i].getValue());
			policyConditionOperatorList.add(policyConditionOperator);
		}
		return policyConditionOperatorList;
	}
	
}
