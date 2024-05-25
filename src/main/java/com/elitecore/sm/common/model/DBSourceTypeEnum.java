package com.elitecore.sm.common.model;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;

@XmlEnum(String.class)
public enum DBSourceTypeEnum {
//ORACLE,MYSQL,POSTGRESQL

@XmlEnumValue("1")ORACLE("1"), @XmlEnumValue("3")MYSQL("3"),@XmlEnumValue("2")POSTGRESQL("2");

private String value;

DBSourceTypeEnum(String value) {
	this.value = value;
}


public String getValue() {
	return value;
}
}
