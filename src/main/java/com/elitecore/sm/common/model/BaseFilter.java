package com.elitecore.sm.common.model;

import java.util.List;

import org.springframework.stereotype.Component;

/**
 * @author vishal.lakhyani
 *
 */
@Component
public class BaseFilter {
	
	private List<Class<?>> liKlass;
	private Class<?> klass;
	private String field;
	private String value;
	
	public BaseFilter(){
		//default no-arg constructor
		
	}
	
	public BaseFilter(List<Class<?>> liklass,Class<?> klass,String field,String value){
		this.liKlass=liklass;
		this.klass=klass;
		this.field = field;
		this.value=value;
	}
	
	public List<Class<?>> getLiKlass() {
		return liKlass;
	}

	public void setLiKlass(List<Class<?>> liKlass) {
		this.liKlass = liKlass;
	}

	public Class<?> getKclass() {
		return klass;
	}
	public void setKclass(Class<?> klass) {
		this.klass = klass;
	}
	public String getField() {
		return field;
	}
	public void setField(String field) {
		this.field = field;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
}
