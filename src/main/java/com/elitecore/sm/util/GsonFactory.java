/**
 * 
 */
package com.elitecore.sm.util;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


/**
 * @author Ranjitsinh Reval
 *
 */
public class GsonFactory {

	
	GsonFactory(){
		//Default constructor
	}
	
	/**
	 * Method will use to create json string to pojo class mapping.
	 * @return
	 */
	public static Gson create() {
		return new GsonBuilder().disableHtmlEscaping().setFieldNamingPolicy(FieldNamingPolicy.IDENTITY).setPrettyPrinting().serializeNulls().create();
	}
	
	
}
