package com.elitecore.sm.roaming.model;


import javax.xml.bind.annotation.XmlEnumValue;

public enum FileSequenceEnum {
		@XmlEnumValue("same")Same("same"),@XmlEnumValue("different")Different("different");
		private String name;
		private FileSequenceEnum(String name) {
			this.name = name;
		}
		public String getFileSequenceEnum() {
			return this.name;
		}
		public String getName() {
			return name;
		}

}	


