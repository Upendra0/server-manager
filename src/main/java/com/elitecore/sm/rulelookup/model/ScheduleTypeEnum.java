package com.elitecore.sm.rulelookup.model;

public enum ScheduleTypeEnum {
	Immediate("Immediate"),Schedule("Schedule");
	
	private final String name;       

    private ScheduleTypeEnum(String s) {
        name = s;
    }

    public boolean equalsName(String otherName) {
        // (otherName == null) check is not needed because name.equals(null) returns false 
        return name.equals(otherName);
    }

    public String toString() {
       return this.name;
    }
}
