package com.elitecore.sm.roaming.model;

import javax.xml.bind.annotation.XmlEnumValue;

public enum LOBEnum {
	
		@XmlEnumValue("International Roaming")InternationalRoaming("International Roaming"),@XmlEnumValue("Domestic Roaming")DomesticRoaming("Domestic Roaming");
		
		
		private String name;
		private LOBEnum(String name) {
			this.name = name;
		}

		public String getLOBEnum(){
			return this.name;
		}

		/**
		 * @return the name
		 */
		public String getName() {
			return name;
		}

	}


