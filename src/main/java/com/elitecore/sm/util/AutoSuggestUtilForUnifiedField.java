package com.elitecore.sm.util;

import java.util.ArrayList;
import java.util.List;

import org.javers.common.collections.Arrays;

import com.elitecore.sm.parser.model.UnifiedFieldEnum;

public class AutoSuggestUtilForUnifiedField {
	
	private AutoSuggestUtilForUnifiedField() {
		
	}
	
	public static List<String> unifiedFieldList(String tagName){
		List<String> unifiedFieldList = new ArrayList<>();
		List<Object> unifiedFieldEnumList = Arrays.asList(UnifiedFieldEnum.values()); 
		for(Object obj : unifiedFieldEnumList){
			String unifiedField = ((UnifiedFieldEnum)obj).getName();
			if(unifiedField.toLowerCase().contains(tagName.toLowerCase())){
				unifiedFieldList.add(unifiedField);
			}
		}
		return unifiedFieldList;
	}

}
