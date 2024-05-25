package com.elitecore.sm.roaming.model;


import javax.xml.bind.annotation.XmlEnumValue;

public enum VersionEnum {

	
		@XmlEnumValue("1.32")weer("1.334"),
		@XmlEnumValue("1.34")srferre("1.33");
		private String name;
		private VersionEnum(String name) {
			this.name = name;
		}
		public String getVersionEnum() {
			return this.name;
		}
		public String getName() {
			return name;
		}

}	


