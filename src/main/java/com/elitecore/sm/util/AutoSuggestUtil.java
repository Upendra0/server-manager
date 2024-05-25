package com.elitecore.sm.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import com.elitecore.sm.parser.model.PolicyActionExpressionEnum;
import com.elitecore.sm.parser.model.PolicyActionStaticExpressionEnum;

public class AutoSuggestUtil {
	
	private AutoSuggestUtil(){
	}
	
	private static final Map<String,Integer> dataFuncsLengthMap = new HashMap<>();
	private static final List<String> staticFuncList = new ArrayList<>();
	private static List<String> unifiedDataList = new ArrayList<>();
	private static final List<String> operatorList = new ArrayList<>();
	private static final List<String> dynamicFunctionList = new ArrayList<>();
	
	private static Integer brecketCnts = 0;
	private static Integer doubleQouteCnts = 0;
	private static List<Map<String,Integer>> dataMapList = new ArrayList<>();

	protected static Logger logger = Logger.getLogger("com.elitecore.sm.util.AutoSuggestUtil");
	
	static {
		
		operatorList.add("+");
		operatorList.add("*");
		operatorList.add("-");
		operatorList.add("/");
		
		for(PolicyActionExpressionEnum policyActionExpressionEnum :  PolicyActionExpressionEnum.values()){
			String functionName = policyActionExpressionEnum.getFunctionName();
			int functionLenght = policyActionExpressionEnum.getFunctionLenght();
			
			dynamicFunctionList.add(functionName);
			dataFuncsLengthMap.put(functionName,functionLenght);
		}
		
		for(PolicyActionStaticExpressionEnum policyActionExpressionEnum :  PolicyActionStaticExpressionEnum.values()){
			staticFuncList.add(policyActionExpressionEnum.getName());
		}
		
	}
	
	
	public static List<String> simulateSearchResult(String tagName,List<String> unifiedList) {
		
		brecketCnts = 0;
		doubleQouteCnts  = 0 ;
		dataMapList = new ArrayList<>();
		logger.info("Enter into action method to get autocomplete data for String : " + tagName);
		List<String> result = new ArrayList<>();
		unifiedDataList= unifiedList;
		try{
			// iterate a list and filter by tagName
			if( unifiedDataList.contains(tagName.trim()) && !tagName.contains("=")){
				result.add("=");
			}else if(!tagName.contains("=")){
				tagName = tagName.trim();
				for (String tag : unifiedDataList) {
					if (tag.toLowerCase().startsWith(tagName.toLowerCase())) {
						result.add(tag);
					}
				}
			}else{
				for (String tag : unifiedDataList) {
					String tagNameWithoutSpace = tagName.replace(" ", "");
					if ((tagNameWithoutSpace).contains(tag+"="/*+x*/)) {
						result = getDataReccursively(tagNameWithoutSpace.substring((tag+"=").length()));
						return result;
					}
				}
			}
		}catch(Exception e){
			logger.error("Error while getting auto complete data for policy action, Reason : " + 
					e.getMessage());
			logger.error(e);
		}
		logger.info("Autocomplete data list :: " + result);
		return result;
	}
	
	private static List<String> getDataReccursively(String dataStr){
		
		List<String> result = new ArrayList<>();
		if(dataStr == null || "".equals(dataStr)){
			for(String x : dynamicFunctionList){
				result.add(x);
			}
			for (String tag : unifiedDataList) {
				result.add(tag);
			}
			for (String tag : staticFuncList) {
				result.add(tag);
			}
		}else {
			
			boolean bStartWithData = false;
			boolean bStartWithFunc = false;
			boolean bStartWithStatFunc = false;
			
			
			if(brecketCnts > 0){
				if(dataStr.startsWith("\"")){
					dataStr = dataStr.substring(dataStr.indexOf("\"")+1, dataStr.length());
					if(dataStr.indexOf("\"") != -1 ){
						dataStr = dataStr.substring(dataStr.indexOf("\"")+1);
						doubleQouteCnts++;
						result = preCallLogicForOperator(dataStr);
					}else{
						result.add("\"");
					}
					return result;
				}
				if(Character.isDigit(dataStr.charAt(0))){
					int length = 1;
					String tmpDataStr = dataStr.substring(1);
					while(tmpDataStr.length() > 0 && Character.isDigit(tmpDataStr.charAt(0))){
						length++;
						tmpDataStr = tmpDataStr.substring(1);
						if(tmpDataStr.length() == 0)
							break;
					}
					dataStr = dataStr.substring(length);
					doubleQouteCnts++;
					result = preCallLogicForOperator(dataStr);
					return result;
				}
			}
			
			String markerVal = "";
			for(String unifiedField : unifiedDataList){
				if(unifiedField.toLowerCase().startsWith(markerVal.toLowerCase()) && dataStr.toLowerCase().startsWith(unifiedField.toLowerCase())){
					bStartWithData = true;
					markerVal = unifiedField;
					if(!markerVal.startsWith("General") || markerVal.length() != 8)
						break;
				}
			}
			
			if(!(doubleQouteCnts > 0) && !bStartWithData){
				for(String unifiedField : dynamicFunctionList){
					if(dataStr.toLowerCase().startsWith(unifiedField.toLowerCase())){
						markerVal = unifiedField;
						bStartWithFunc = true;
						break;
					}
				}
				
				if(bStartWithFunc){
					String subStringVal = dataStr.substring(markerVal.length());
					Map<String,Integer> functionMap = new HashMap<>();
					functionMap.put(markerVal, 0);
					brecketCnts++;
					dataMapList.add(functionMap);
					result = getDataReccursively(subStringVal);
					
					
				}else{
					
					for(String unifiedField : staticFuncList){
						if(dataStr.toLowerCase().startsWith(unifiedField.toLowerCase())){
							markerVal = unifiedField;
							bStartWithStatFunc = true;
							break;
						}
					}
					
					if(bStartWithStatFunc){
						String tempDataStr = dataStr.substring(markerVal.length());
						result = preCallLogicForOperator(tempDataStr); 
						
					}else{
						for(String unifiedField : unifiedDataList){
							if(unifiedField.toLowerCase().startsWith(dataStr.toLowerCase())){
								bStartWithData = true;
								result.add(unifiedField);
							}
						}
						if(bStartWithData){
							return result;
						}
						
						for(String unifiedField : dynamicFunctionList){
							if(unifiedField.toLowerCase().startsWith(dataStr.toLowerCase())){
								bStartWithFunc = true;
								result.add(unifiedField);
							}
						}
						if(bStartWithFunc){
							return result;
						}
						
						for(String unifiedField : staticFuncList){
							if(unifiedField.toLowerCase().startsWith(dataStr.toLowerCase())){
								bStartWithStatFunc = true;
								result.add(unifiedField);
							}
						}
						if(bStartWithStatFunc){
							return result;
						}
					}
				}
				
			}else{
				
				String tempDataStr = dataStr.substring(markerVal.length());
				result = preCallLogicForOperator(tempDataStr);
			}
			
		}
		return result;
	}
	
	
	private static List<String> preCallLogicForOperator(String tempDataStr){
		List<String> result = new ArrayList<>();
		if( !tempDataStr.startsWith(")")){
			if(dataMapList.size() > 0 ){
				int  currentCommaStatus = dataMapList.get(dataMapList.size() - 1).values().iterator().next();
				Integer val = dataFuncsLengthMap.get(dataMapList.get(dataMapList.size() - 1).keySet().iterator().next());
				if(val != null){
					if(currentCommaStatus == (val -1 )){
						result = logicForOperator(tempDataStr);
						return result;
					}
				}
			}
			result = logicForOperator(tempDataStr);
		}else{
			while(tempDataStr.startsWith(")")){
				if(doubleQouteCnts > 0){
					doubleQouteCnts--;
				}
				if(!dataMapList.isEmpty()){
					tempDataStr = tempDataStr.replaceFirst("\\)", "");
					dataMapList.remove(dataMapList.size() - 1);
					brecketCnts--;
				}else{
					break;
				}
			}
			result = logicForOperator(tempDataStr);
		}
		return result;
		
	}
	
	private static List<String> logicForOperator(String tempDataStr){
		
		List<String> result = new ArrayList<>();
		boolean bBrecketRearched = false;
		if(!dataMapList.isEmpty()){
			int  currentCommaStatus = dataMapList.get(dataMapList.size() - 1).values().iterator().next();
			bBrecketRearched = dataFuncsLengthMap.get(dataMapList.get(dataMapList.size() - 1).keySet().iterator().next()) == (currentCommaStatus + 1);
		}
		if(!( doubleQouteCnts > 0) && (tempDataStr == null || "".equals(tempDataStr))){
			for(String unifiedField : operatorList){
				if(!"-".equals(unifiedField))
					result.add(unifiedField);
			}
			if(!bBrecketRearched && !dataMapList.isEmpty()){
				result.add(",");
			}else if(brecketCnts > 0){
				result.add(")");
			}
		}else {
			String markerVal  = "";
			boolean bStartWithOperator = false;
			
			for(String unifiedField : operatorList){
				if(tempDataStr.startsWith(unifiedField)){
					markerVal = unifiedField;
					bStartWithOperator = true;
					break;
				}
			}
			if(!bStartWithOperator){
				if(tempDataStr != null && tempDataStr.startsWith(",")){
					Set<Entry<String, Integer>> entry =  dataMapList.get(dataMapList.size() - 1).entrySet();
					Entry<String, Integer> dataEnty = entry.iterator().next();
					String key = dataEnty.getKey();
					Integer value = dataEnty.getValue();
					dataMapList.get(dataMapList.size() - 1).put(key, value + 1);
					markerVal = ",";
					bStartWithOperator = true;
				}else if (tempDataStr!= null && tempDataStr.length() > 0 && 
						Character.isDigit(tempDataStr.charAt(0))){
					int length = 1;
					String localDataStr = tempDataStr.substring(1);
					while(localDataStr.length() > 0  && Character.isDigit(localDataStr.charAt(0))){
						length++;
						localDataStr = localDataStr.substring(1);
						if(localDataStr.length() == 0)
							break;
					}
					markerVal = tempDataStr.substring(0,length); 
					if(localDataStr.length() > 0){
						bStartWithOperator = true;
					}else{
						result.add(",");
					}
				}else if(bBrecketRearched){
					result.add(")");
				}else if(doubleQouteCnts > 0){
					result.add(",");
				}
				if(doubleQouteCnts > 0)
					doubleQouteCnts--;
			}
			
			if(bStartWithOperator)
				result = getDataReccursively(tempDataStr.substring(markerVal.length()));
		}
		return result;
	}
}
