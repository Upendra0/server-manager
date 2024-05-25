package com.elitecore.sm.common.model;


public enum WeekEnum {
	
	Monday("MON"),Tuesday("TUE"),Wednesday("WED"),Thursday("THU"),Friday("FRI"),Saturday("SAT"),Sunday("SUN");

	private String name;
	
	private WeekEnum(String name){
		this.name = name;
	}
	
	public String getWeekEnum(){
		return name;
	}
	
	public String getName(){
		return name;
	}
	
}
