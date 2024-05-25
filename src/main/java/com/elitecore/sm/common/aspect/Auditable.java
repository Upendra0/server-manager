package com.elitecore.sm.common.aspect;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD,ElementType.TYPE})
public @interface Auditable {
	
	String actionType();
	String auditActivity();
	Class<?> currentEntity();
	String ignorePropList();
	
}
	