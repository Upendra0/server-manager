package com.elitecore.sm.common.model;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;

/**
 * @author vishal.lakhyani
 *
 */
@XmlEnum(Boolean.class)
public enum TrueFalseEnum {
	@XmlEnumValue("false") False(false),@XmlEnumValue("true")True(true);

	private boolean name;

	private TrueFalseEnum(boolean name) {
		this.name = name;
	}

	public boolean getTrueFalseEnum() {
		return this.name;
	}

	/**
	 * @return the name
	 */
	public boolean getName() {
		return name;
	}

}
