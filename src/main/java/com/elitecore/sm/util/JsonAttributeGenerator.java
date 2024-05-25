package com.elitecore.sm.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonAttributeGenerator {

	 	public static Map<String, String> jsonToMap(Object json) throws JSONException {

	        if(json instanceof JSONObject)
	            return _jsonToMap_((JSONObject)json) ;

	        else if (json instanceof String)
	        {
	            JSONObject jsonObject = new JSONObject((String)json) ;
	            return _jsonToMap_(jsonObject) ;
	        }
	        return null ;
	    }
	 
	 	private static Map<String, String> _jsonToMap_(JSONObject json) throws JSONException {
	        Map<String, String> retMap = new HashMap<String, String>();
	        if(json != JSONObject.NULL) {
	            retMap = toMap(json,null,retMap);
	        }
	        return retMap;
	    }

	    private static Map<String, String> toMap(JSONObject object,String key1,Map<String, String> map) throws JSONException {
	
	        @SuppressWarnings("unchecked")
			Iterator<String> keysItr = object.keys();
	        while(keysItr.hasNext()) {
	            String key = keysItr.next();
	            Object value = object.get(key);
	            if(value instanceof JSONArray) {
	                value = toList((JSONArray) value,key,map);
	            }else if(value instanceof JSONObject) {
	                value = toMap((JSONObject) value,key,map);
	            }
	            if(map.containsKey(key)) {
	            	if(!map.get(key).equals(key1+"."+key)) {
	            		generatedKey(key, map,key1+"."+key,2);
	            	}
	            }else {
	            	if(key1!=null) {
	            		map.put(key,key1+"."+key);
	            	}
	            }
	        }
	        return map;
	    }

	    
	    private static void generatedKey(String key1,Map<String, String> map, String value,int count) {	
	    	String fieldKey="";
	    	fieldKey=key1+count;
	    	if(map.containsKey(fieldKey)) {
	            generatedKey(key1, map,value,++count);
	        }else {
	           	map.put(fieldKey,value);
	         }
	    	
	    }

	    public static Map<String, String> toList(JSONArray array,String key,Map<String, String> map) throws JSONException {
	        for(int i = 0; i < array.length(); i++) {
	            Object value = array.get(i);
	            if(value instanceof JSONArray) {
	                value = toList((JSONArray) value,key,map);
	            }else if(value instanceof JSONObject) {	            	
	            	  value = toMap((JSONObject) value,key,map);
	            }
	        }
	        return map;
	    }
}
