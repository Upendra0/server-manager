/**
 * 
 */
package com.elitecore.sm.util;

import com.elitecore.sm.config.model.EntityRegexCache;
import com.elitecore.sm.config.model.EntityValidationRange;


/**
 * @author Sunil Gulabani
 * May 4, 2015
 */
public class Regex {
	
	public static final  String DEFAULT_ALPHABETS_REGEX = "^[a-zA-Z]+$";
	public static final  String DEFAULT_NUMERIC_REGEX = "^[0-9]+$";
	public static final  String DEFAULT_ALPHANUMERIC_REGEX = "^[a-zA-Z0-9]+$";
	
	Regex(){
		//Default Constructor
	}
	
	/**
	 * Method will get regex value by provided key.
	 * @param key
	 * @return
	 */
	public static String get(String key){
		return MapCache.getConfigValueAsString(key, DEFAULT_ALPHABETS_REGEX);
	}
	
	/**
	 * Method will get provide collection object with provided key value.
	 * @param key
	 * @param collectionName
	 * @return
	 */
	public static String get(String key,String collectionName){
		Object regexObject = (EntityRegexCache) MapCache.getConfigCollectionAsObject(collectionName, key, DEFAULT_ALPHABETS_REGEX);
		if(regexObject != null ){
			EntityRegexCache entitiesRegex = (EntityRegexCache) regexObject;
			return entitiesRegex.getEntitiesRegex().getValue();
		}else { 
			return DEFAULT_ALPHABETS_REGEX;
		}
	}
	
	/**
	 * Method will get validation range object from map cache using fully class name.
	 * @param collectionName
	 * @param key
	 * @param fullyClassNameKey
	 * @return
	 */
	public static EntityValidationRange getValidationRange(String collectionName,String key,String fullyClassNameKey){
		EntityRegexCache entitiesRegex = (EntityRegexCache) MapCache.getConfigCollectionAsObject(collectionName, key, DEFAULT_ALPHABETS_REGEX);
		if(entitiesRegex != null && entitiesRegex.getEntityValidationRange() != null){
			return entitiesRegex.getEntityValidationRange().get(fullyClassNameKey);
		}else return null;
	}
	
	
	/**
	 * Method will get regex entity cache Object
	 * @param key
	 * @return
	 */
	public static Object getRegexObject(String key){
		return MapCache.getConfigValueAsObject(key);
	}
	
}