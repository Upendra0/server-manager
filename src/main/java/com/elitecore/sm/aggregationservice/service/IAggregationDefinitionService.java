package com.elitecore.sm.aggregationservice.service;

import java.util.List;

import org.json.JSONArray;

import com.elitecore.sm.aggregationservice.model.AggregationAttribute;
import com.elitecore.sm.aggregationservice.model.AggregationCondition;
import com.elitecore.sm.aggregationservice.model.AggregationDefinition;
import com.elitecore.sm.aggregationservice.model.AggregationKeyAttribute;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.services.model.AggregationService;

public interface IAggregationDefinitionService {
	
	public ResponseObject saveAggregationDefintion(AggregationDefinition aggDefinitionList,String defConditions,
			String defAggAttributes,String defKeyAttributes,int staffId);
	
	public ResponseObject updateAggregationDefintion(AggregationDefinition aggDefinition,String defConditions,
			String defAggAttributes,String defKeyAttributes,int staffId);
	
	public ResponseObject getAllAggregationDefintionName();
	
	public ResponseObject getAggregationDefintionData(String aggDefName);
	
	public boolean isUniqueDefinitionName(int defId,String newName);
	
	public ResponseObject getAggregationDefinitionByServiceId(int serviceId);
	
	public void importDefinitionAddAndKeepBothMode(AggregationDefinition exportedDefinition, AggregationService service, int importMode);
	
	public void importDefinitionUpdateMode(AggregationDefinition dbDefinition, AggregationDefinition exportedDefinition, int importMode);
	
	public JSONArray getJSONFromAggConditionList(List<AggregationCondition> aggConditionList);
	
	public JSONArray getJSONFromAggAttrList(List<AggregationAttribute> aggAttrList);
	
	public JSONArray getJSONFromKeyAttrList(List<AggregationKeyAttribute> aggregationKeyAttribute);
}
