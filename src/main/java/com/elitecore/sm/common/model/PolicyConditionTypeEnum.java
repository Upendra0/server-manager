package com.elitecore.sm.common.model;

import java.util.HashMap;
import java.util.Map;

public enum PolicyConditionTypeEnum {
	
	Dynamic ("dynamic"),
	Expression ("expression"),
	Function ("function"),
	Static ("static");
	
	private String typeValue;
	
	private PolicyConditionTypeEnum(String typeValue) {
		this.typeValue = typeValue;
	}
	
	public static Map<String, String> getConditionTypeMap() {
		Map<String, String> conditionTypeMap = new HashMap<>();
		PolicyConditionTypeEnum[] conditionTypes = PolicyConditionTypeEnum.values();
		for(int i = 0; i < conditionTypes.length; i++) {
			conditionTypeMap.put(conditionTypes[i].name(), conditionTypes[i].typeValue);
		}
		return conditionTypeMap;
	}
}
