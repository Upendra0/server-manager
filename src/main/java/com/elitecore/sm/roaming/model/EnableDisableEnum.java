package com.elitecore.sm.roaming.model;


import javax.xml.bind.annotation.XmlEnumValue;

public enum EnableDisableEnum {
		@XmlEnumValue("enable")Enable(true),@XmlEnumValue("disable")Disable(false);
		private boolean name;
		private EnableDisableEnum(boolean name) {
			this.name = name;
		}
		public boolean getEnableDisableEnum() {
			return this.name;
		}
		public boolean getName() {
			return name;
		}

}	


