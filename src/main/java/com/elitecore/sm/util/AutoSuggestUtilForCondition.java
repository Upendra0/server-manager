package com.elitecore.sm.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import org.apache.log4j.Logger;
import com.elitecore.sm.parser.model.PolicyConditionExpressionEnum;
public class AutoSuggestUtilForCondition {
	
	private AutoSuggestUtilForCondition(){
	}
	
	private static List<String> unifiedDataList = new ArrayList<>();
	private static Map<String,Integer> dataFuncsLengthMap = new HashMap<>();
	private static List<String> logicalOperatorList = new ArrayList<>();
	private static List<String> comparisonOperatorsList = new ArrayList<>();
	private static List<String> dynamicFunctionList = new ArrayList<>();
	
	private static Integer funcBrecketCnts = 0;
	private static Integer dataCnts = 0;
	private static boolean bDataOpeCompleted = false;
	private static boolean bBreketStartFound = false;
	private static boolean notStartFound = false;
	private static boolean bDataOnly = false;
	
	private static List<Map<String,Integer>> dataMapList = new ArrayList<>();
	protected static Logger logger = Logger.getLogger("com.elitecore.sm.util.AutoSuggestUtilForCondition");

	static {
		comparisonOperatorsList.add("==");
		comparisonOperatorsList.add("!=");
		comparisonOperatorsList.add(">=");
		comparisonOperatorsList.add(">");
		comparisonOperatorsList.add("<");
		comparisonOperatorsList.add("<=");
		
		for(PolicyConditionExpressionEnum policyActionExpressionEnum :  PolicyConditionExpressionEnum.values()){
			String functionName = policyActionExpressionEnum.getFunctionName();
			int functionLenght = policyActionExpressionEnum.getFunctionLenght();
			
			dynamicFunctionList.add(functionName);
			dataFuncsLengthMap.put(functionName,functionLenght);
		}
		
		logicalOperatorList.add("AND");
		logicalOperatorList.add("OR");
		
	}
	
	public static List<String> simulateSearchResult(String tagName, List<String> unifiedList) {
		
		logger.info("Enter into condition method to get autocomplete data for String : " + tagName);
		funcBrecketCnts = 0;
		dataCnts = 0;
		bBreketStartFound = false;
		bDataOpeCompleted = false;
		dataMapList = new ArrayList<Map<String,Integer>>();
		bDataOnly = false;
		notStartFound = false;
		unifiedDataList= unifiedList;
		List<String> result = new ArrayList<String>();
		try{
			String tagNameWithoutSpace = tagName.replace(" ", "");
			result = getDataReccursively(tagNameWithoutSpace);
		}catch(Exception e){
			logger.error("Error while getting auto complete data for policy condition, Reason : " + 
					e.getMessage());
			logger.error(e);
		}
		logger.info("Autocomplete data list :: " + result);
		
		return result;
	}
	
	private static List<String> getDataReccursively(String dataStr){
		
		List<String> result = new ArrayList<>();
		if(dataStr == null || "".equals(dataStr)){
			if(!( funcBrecketCnts > 0) ){
				if( !bDataOnly){
					for(String x : dynamicFunctionList){
						result.add(x);
					}
				}
				if(!notStartFound){
					if(!bBreketStartFound){
						result.add("(");
						result.add("!");
					}
				}
				
			}
			if((funcBrecketCnts > 0) || !notStartFound){
				for (String tag : unifiedDataList) {
					result.add(tag);
				}
			}
		} else if(dataStr.startsWith("!")){
			notStartFound = true;
			String subStringVal = dataStr.substring(1);
			result = getDataReccursively(subStringVal);
			
		}else {
			
			boolean bStartWithData = false;
			boolean bStartWithFunc = false;
			boolean bStartWithStatBrek = false;
			
			String markerVal = "";
			for(String unifiedField : unifiedDataList){
				if(unifiedField.toLowerCase().startsWith(markerVal.toLowerCase()) && dataStr.toLowerCase().startsWith(unifiedField.toLowerCase())){
					bStartWithData = true;
					markerVal = unifiedField;
					if(!markerVal.startsWith("General") || markerVal.length() != 8)
						break;
				}
			}
			
			if(!bStartWithData){
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
					funcBrecketCnts++;
					notStartFound = false;
					dataMapList.add(functionMap);
					result = getDataReccursively(subStringVal);
					
				}else{
					if(dataStr.startsWith("(")){
						markerVal = "(";
						bStartWithStatBrek = true;
					}
					
					if(bStartWithStatBrek  && !bBreketStartFound){
						bDataOnly = true;
						bBreketStartFound = true;
						String tempDataStr = dataStr.substring(markerVal.length());
						result = getDataReccursively(tempDataStr);
						
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
					}
				}
				
			}else{
				
				if(funcBrecketCnts <= 0){
					bDataOnly = true;
					if(dataCnts > 0 ){
						dataCnts--;
						bDataOpeCompleted = true;
						bDataOnly = false;
					}else{
						dataCnts++;
					}
				}
				String tempDataStr = dataStr.substring(markerVal.length());
				result = preCallLogicForOperator(tempDataStr);
			}
			
		}
		return result;
	}
	
	
	private static List<String> preCallLogicForOperator(String tempDataStr){
		List<String> result = new ArrayList<>();
		if( !tempDataStr.startsWith(")")){
			if(bDataOpeCompleted){
				if ( bBreketStartFound ){
					result.add(")");
				}
				return result;
			}
			if(!dataMapList.isEmpty() ){
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
			if(tempDataStr.startsWith(")")){
				if( funcBrecketCnts > 0){
					if(!dataMapList.isEmpty()){
						tempDataStr = tempDataStr.replaceFirst("\\)", "");
						dataMapList.remove(dataMapList.size() - 1);
						funcBrecketCnts--;
						if(bBreketStartFound){
							result.add(")");
						}else{
							result = logicForLogicalOperator(tempDataStr);
						}
					}
				}else if(bDataOpeCompleted || bBreketStartFound){
					if(bBreketStartFound)
						bBreketStartFound = false;
					bDataOpeCompleted = false;
					tempDataStr = tempDataStr.replaceFirst("\\)", "");
					result = logicForLogicalOperator(tempDataStr);
				}else{
					return new ArrayList<>();
				}
			}
		}
		return result;
	}
	
	private static List<String> logicForLogicalOperator(String tempDataStr){
		
		bBreketStartFound = false;
		List<String> result = new ArrayList<>();
		if(tempDataStr == null || "".equals(tempDataStr)){
			for(String unifiedField : logicalOperatorList){
				result.add(unifiedField);
			}
		}else{
			String markerVal  = "";
			boolean bStartWithOperator = false;
			for(String unifiedField : logicalOperatorList){
				if(tempDataStr.startsWith(unifiedField)){
					markerVal = unifiedField;
					bStartWithOperator = true;
					break;
				}
			}
			if(bStartWithOperator){
				result = getDataReccursively(tempDataStr.substring(markerVal.length()));
			}
		}
		return result;
	}
	
	private static List<String> logicForOperator(String tempDataStr){
		
		List<String> result = new ArrayList<>();
		if(tempDataStr == null || "".equals(tempDataStr)){
			if(! ( funcBrecketCnts > 0)){
				for(String unifiedField : comparisonOperatorsList){
					result.add(unifiedField);
				}
			}
			if(!dataMapList.isEmpty()){
				int  currentCommaStatus = dataMapList.get(dataMapList.size() - 1).values().iterator().next();
				if(dataFuncsLengthMap.get(dataMapList.get(dataMapList.size() - 1).keySet().iterator().next()) != (currentCommaStatus + 1)){
					result.add(",");
				}else if(funcBrecketCnts > 0){
					result.add(")");
				}
			}
		}else {
			String markerVal  = "";
			boolean bStartWithOperator = false;
			
			for(String unifiedField : comparisonOperatorsList){
				if(tempDataStr.startsWith(unifiedField)){
					markerVal = unifiedField;
					bStartWithOperator = true;
					break;
				}
			}
			if(!bStartWithOperator){
				if(tempDataStr.startsWith(",")){
					Set<Entry<String, Integer>> entry =  dataMapList.get(dataMapList.size() - 1).entrySet();
					Entry<String, Integer> dataEnty = entry.iterator().next();
					String key = dataEnty.getKey();
					Integer value = dataEnty.getValue();
					dataMapList.get(dataMapList.size() - 1).put(key, value + 1);
					markerVal = ",";
					bStartWithOperator = true;
				}
			}
			
			if(bStartWithOperator){
				result = getDataReccursively(tempDataStr.substring(markerVal.length()));
			}
		}
		return result;
	}
}