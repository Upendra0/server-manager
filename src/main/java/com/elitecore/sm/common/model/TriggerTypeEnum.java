package com.elitecore.sm.common.model;

import java.util.ArrayList;
import java.util.List;

public enum TriggerTypeEnum {

	SELECT("SELECT SCHEDULER TYPE"), MINUTE("Minute"), HOURLY("Hourly"), DAILY("Daily"), WEEKLY("Weekly") ,MONTHLY("Monthly");
	
	private String value;
	

	TriggerTypeEnum(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
	
	public static List<String> getAllValues(){
		List<String> triggerTypeEnumList = new ArrayList<String>();
		for(TriggerTypeEnum triggerTypeEnum : TriggerTypeEnum.values()){
			triggerTypeEnumList.add(triggerTypeEnum.getValue());
		}
		
		return triggerTypeEnumList;
	}
}
