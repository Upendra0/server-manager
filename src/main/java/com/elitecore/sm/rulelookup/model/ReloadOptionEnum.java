package com.elitecore.sm.rulelookup.model;

public enum ReloadOptionEnum {
	Full("Full"),Delta("Delta");
	
	private final String name;       

    private ReloadOptionEnum(String s) {
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
