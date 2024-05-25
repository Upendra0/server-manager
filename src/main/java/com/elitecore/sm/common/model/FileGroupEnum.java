/**
 * 
 */
package com.elitecore.sm.common.model;

import java.util.ArrayList;
import java.util.List;

public enum FileGroupEnum {
	DAY("DAY"), MONTH("MONTH"), YEAR("YEAR"), YEAR_MONTH("YEAR-MONTH"), YEAR_MONTH_DAY("YEAR-MONTH-DAY") ,NA("NA");
	
	private String fileGroupEnum;
	
	FileGroupEnum(String fileGroupEnum){
		this.fileGroupEnum = fileGroupEnum;
	}
	
	public String getFileGroupEnum(){
		return fileGroupEnum;
	}
	
	
	public static List<String> getValues(){
		List<String> fileGroupEnumList = new ArrayList<String>();
		for(FileGroupEnum fileGroupEnum : FileGroupEnum.values()){
			if(fileGroupEnum.getFileGroupEnum().contains("-")){
				continue;
			}
			fileGroupEnumList.add(fileGroupEnum.getFileGroupEnum());
		}
		
		return fileGroupEnumList;
	}
	
	public static List<String> getAllValues(){
		List<String> fileGroupEnumList = new ArrayList<String>();
		for(FileGroupEnum fileGroupEnum : FileGroupEnum.values()){
			fileGroupEnumList.add(fileGroupEnum.getFileGroupEnum());
		}
		
		return fileGroupEnumList;
	}

}
