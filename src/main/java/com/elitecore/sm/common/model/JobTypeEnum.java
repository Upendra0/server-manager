package com.elitecore.sm.common.model;

import java.util.ArrayList;
import java.util.List;

public enum JobTypeEnum {
	AutoErrorReprocess("com.elitecore.sm.scheduler.AutoErrorReprocessJob"),
	AutoReloadCache("com.elitecore.sm.scheduler.AutoReloadCacheJob"),
	AutoUpload("com.elitecore.sm.scheduler.AutoUploadJob");
	
	private String value;
	
	JobTypeEnum(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
	
	public static List<String> getAllValues(){
		List<String> triggerTypeEnumList = new ArrayList<String>();
		for(JobTypeEnum triggerTypeEnum : JobTypeEnum.values()){
			triggerTypeEnumList.add(triggerTypeEnum.getValue());
		}
		
		return triggerTypeEnumList;
	}
}
