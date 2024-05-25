package com.elitecore.sm.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

import org.apache.log4j.Logger;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import com.elitecore.sm.common.constants.SystemParametersConstant;
import com.elitecore.sm.common.model.BaseModel;
import com.elitecore.sm.systemparam.model.SystemParameterData;
import com.elitecore.sm.systemparam.model.SystemParameterValuePoolData;

/**
 * MapCache is used to cache the data
 * @author Sunil Gulabani
 * Mar 24, 2015
 */
public class MapCache {
	
	public static final  String CONFIG_COLLECTION = "CONFIG_COLLECTION";
	private static ConcurrentNavigableMap<String, ConcurrentNavigableMap<String, Object>> map;
	private static Logger logger = Logger.getLogger("MapCache");
	
	static{
		initializeMap();
	}
	
	private MapCache(){
		// not to be directly instantiated
	}
	
	
	/**
	 * Add Config Object into Cache
	 * @param key
	 * @param object
	 */
	@Deprecated
	public static void addConfigObject(String key,Object object){
		addObject(CONFIG_COLLECTION, key, object);
	}
	
	/**
	 * Method will add new map cache object with single map object. 
	 * @param collectionName
	 * @param key
	 * @param object
	 */
	public static void addConfigObject(String collectionName,String key, Object object){
		addObject(collectionName, key, object);
	}
	
	
	/**
	 * Add System Parameter List in Cache
	 * @param collectionName
	 * @param Key
	 * @param object
	 */
	public static void addSystemParameterList(String collectionName,String key,Object object){
		addObject(collectionName,key,object);
	}
	
	/**
	 * Return Value in int
	 * @param key
	 * @param defaultValue
	 * @return int
	 */
	public static int getConfigValueAsInteger(String key, int defaultValue){
		return Integer.parseInt(getObject(CONFIG_COLLECTION, key,defaultValue).toString());
	}
	
	public static Float getConfigValueAsFloat(String key, float defaultValue){
		return Float.parseFloat(getObject(CONFIG_COLLECTION, key,defaultValue).toString());
	}
	
	/**
	 * Return Value in String
	 * @param key
	 * @param defaultValue
	 * @return String
	 */
	public static String getConfigValueAsString(String key, String defaultValue){
		return getObject(CONFIG_COLLECTION, key,defaultValue).toString();
	}
	
	
	/**
	 * Return Value boolean
	 * @param key
	 * @param defaultValue
	 * @return boolean
	 */
	public static boolean getConfigValueAsBoolean(String key, boolean defaultValue){
		return Boolean.parseBoolean(getObject(CONFIG_COLLECTION, key,defaultValue).toString());
	}
	
	/**
	 * Return Value in Object
	 * @param key
	 * @return Object
	 */
	public static Object getConfigValueAsObject(String key){
		return getObject(CONFIG_COLLECTION, key,null);
	}
	
	@Deprecated
	public static Object getConfigValueAsObject(String key,String defaultValue){
		return getObject(CONFIG_COLLECTION, key,defaultValue);
	}
	
	/**
	 * Method will get Collection map with provide key and collection name.
	 * @param collectionName
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public static Object getConfigCollectionAsObject(String collectionName, String key,String defaultValue){
			
		return getObject(collectionName, key);
	}
		
	/**
	 * Return SystemParameter List
	 * @param collectionName
	 * @return  List<SystemParameterData>
	 */
	public static List<SystemParameterData> getSystemParameterList(String collectionName){
		List<SystemParameterData> systemParameterList = new ArrayList<>();
		if(collectionName!=null){
			initializeMap();
			
			ConcurrentNavigableMap<String, Object> childMap = getChildMap(collectionName);					
			for(Map.Entry<String, Object> entry:childMap.entrySet()){
				SystemParameterData sysparamDataFromPassword = (SystemParameterData)entry.getValue();
				/*if(SystemParametersConstant.FROM_EMAIL_PASSWORD.equals(sysparamDataFromPassword.getAlias()) && sysparamDataFromPassword.getValue()!=null 
						|| SystemParametersConstant.SSO_ADMIN_PASSWORD.equals(sysparamDataFromPassword.getAlias()) && sysparamDataFromPassword.getValue()!=null){
					sysparamDataFromPassword.setValue(EliteUtils.decryptData(sysparamDataFromPassword.getValue()));
				}*/
				systemParameterList.add((SystemParameterData)entry.getValue());
			}		
			Collections.sort(systemParameterList,new DisplayOrderComparator());
		}
		return systemParameterList;
	}
	
	/**
	 * Return SystemParameter Value Pool List
	 * @param collectionName
	 * @return List<SystemParameterValuePoolData>
	 */
	public static List<SystemParameterValuePoolData> getSystemParameterValuePoolList(String collectionName){
		List<SystemParameterValuePoolData> systemParameterList = new ArrayList<>();
		if(collectionName!=null){
			initializeMap();
			
			ConcurrentNavigableMap<String, Object> childMap = getChildMap(collectionName);
			
			if(childMap!=null){
				for(Map.Entry<String, Object> entry:childMap.entrySet()){
					systemParameterList.add((SystemParameterValuePoolData)entry.getValue());
				}
			}
		}
		return systemParameterList;
	}
	
	/**
	 * Add object in Cache
	 * @param collectionName
	 * @param key
	 * @param object
	 */
	private static void addObject(String collectionName, String key,Object object){
		if(collectionName != null && key != null && !"".equals(key)){
			initializeMap();
			ConcurrentNavigableMap<String, Object> childMap = getChildMap(collectionName);
			if(null == object) {
				childMap.put(key,org.apache.commons.lang.StringUtils.EMPTY);
			} else {
				childMap.put(key,object);
			}
			map.put(collectionName, childMap);
		}
	}
	
	/**
	 * Method will load object from map cache with the given id.
	 * @param id
	 * @param mapKey
	 */
	@SuppressWarnings("unchecked")
	public static Object loadMasterEntityById(int id, String mapKey){
		Object masterEntity = null;
		if(id > 0 && !StringUtils.isEmpty(mapKey)){
			List<? super BaseModel> objectList =  (List<? super BaseModel>) MapCache.getConfigValueAsObject(mapKey);
			for(Object object: objectList){
				Field field = ReflectionUtils.findField(object.getClass(), "id");
				if (field != null) {
					field.setAccessible(true);
					Object value;
					try {
						value = field.get(object);
						if (value != null && Integer.parseInt(value.toString()) == id) {
							masterEntity = object;
						}
					} catch (IllegalArgumentException | IllegalAccessException e) {
						logger.error(e.getMessage(), e);
					}
			}
		 }
		}else{
			logger.debug("Unable to get value from map cache for id " + id + " for key " + mapKey);
		}
	  return masterEntity;
	}
	
	/**
	 * Method will load object from map cache with the given alias.
	 * @param id
	 * @param mapKey
	 */
	@SuppressWarnings("unchecked")
	public static Object loadMasterEntityByAlias(String alias, String mapKey){
		Object masterEntity = null;
		if(!StringUtils.isEmpty(mapKey)){
			List<? super BaseModel> objectList =  (List<? super BaseModel>) MapCache.getConfigValueAsObject(mapKey);
			for(Object object: objectList){
				Field field = ReflectionUtils.findField(object.getClass(), "alias");
				if (field != null) {
					field.setAccessible(true);
					Object value;
					try {
						value = field.get(object);
						if (value != null && value.equals(alias)) {
							masterEntity = object;
						}
					} catch (IllegalArgumentException | IllegalAccessException e) {
						logger.error(e.getMessage(), e);
					}
			}
		 }
		}else{
			logger.debug("Unable to get value from map cache for alias " + alias + " for key " + mapKey);
		}
	  return masterEntity;
	}

	
	/**
	 * Get Object from cache
	 * @param collectionName
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	private static Object getObject(String collectionName, String key,Object defaultValue){
		if(collectionName!=null && key!=null && !"".equals(key)){
			initializeMap();
			ConcurrentNavigableMap<String, Object> childMap = getChildMap(collectionName);
			if(childMap!=null && childMap.containsKey(key) && !StringUtils.isEmpty(childMap.get(key)))
				return childMap.get(key);
		}
		
		logger.debug("Failed to get object with give key " + key + " returning  default value " + defaultValue);
	    return defaultValue;
	}
	
	private static Object getObject(String collectionName, String key){
		if(key != null && !"".equals(key)){
			initializeMap();
			ConcurrentNavigableMap<String, Object> childMap = getChildMap(collectionName);
			if(childMap!=null && childMap.containsKey(key))
				return childMap.get(key);
		}
		return null;
	}
	
	@SuppressWarnings("unused")
	public static void removeObject(String collectionName, String key){
		if(key!=null){
			initializeMap();
			ConcurrentNavigableMap<String, Object> childMap = getChildMap(collectionName);
			if(childMap!=null && childMap.containsKey(key))
				childMap.remove(key);
			map.put(collectionName, childMap);
		}
	}
	
	/**
	 * Return Child Map based on collectionName
	 * @param collectionName
	 * @return
	 */
	public static synchronized ConcurrentNavigableMap<String, Object> getChildMap(String collectionName){
		ConcurrentNavigableMap<String, Object> childMap = null;
		
			if(map != null && map.containsKey(collectionName)){
				childMap = map.get(collectionName);
			}
			if(childMap == null){
				childMap = new ConcurrentSkipListMap<>();
			}
		
		return childMap;
	}
	
	/**
	 * Intialize Map
	 */
	private static void initializeMap(){
		if(map == null){
			map = new ConcurrentSkipListMap<>();
		}
	}
	
	public static void forceInitializeMap(){
		map = new ConcurrentSkipListMap<>();
	}
	
	@SuppressWarnings("unused")
	private static void destroyMap(){
		if(map!=null){
			map = null;
		}
	}

	public static ConcurrentNavigableMap<String, ConcurrentNavigableMap<String, Object>> getMap() {
		return map;
	}

	public static void setMap(ConcurrentNavigableMap<String, ConcurrentNavigableMap<String, Object>> map) {
		MapCache.map = map;
	}
	
}