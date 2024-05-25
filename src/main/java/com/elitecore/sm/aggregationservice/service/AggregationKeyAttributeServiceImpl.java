package com.elitecore.sm.aggregationservice.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.elitecore.sm.aggregationservice.model.AggregationDefinition;
import com.elitecore.sm.aggregationservice.model.AggregationKeyAttribute;
import com.elitecore.sm.common.model.StateEnum;
import com.elitecore.sm.util.EliteUtils;

@Service(value = "aggregationKeyAttributeService")
public class AggregationKeyAttributeServiceImpl implements IAggregationKeyAttributeService{
	
	private Logger logger = Logger.getLogger(this.getClass().getName());

	@Transactional
	@Override
	public void importDataAggregationKeyAttributeAddAndKeepBothMode(AggregationKeyAttribute exportedKeyAttribute, AggregationDefinition aggregationDefinition, int importMode){
		exportedKeyAttribute.setId(0);
		exportedKeyAttribute.setCreatedDate(EliteUtils.getDateForImport(false));
		exportedKeyAttribute.setLastUpdatedDate(EliteUtils.getDateForImport(false));
		exportedKeyAttribute.setCreatedByStaffId(aggregationDefinition.getCreatedByStaffId());
		exportedKeyAttribute.setLastUpdatedByStaffId(aggregationDefinition.getLastUpdatedByStaffId());
		exportedKeyAttribute.setAggregationDefinition(aggregationDefinition);
	}
	
	@Transactional
	@Override
	public AggregationKeyAttribute getDataAggregationKeyAttributeFromList(List<AggregationKeyAttribute> aggKeyAttributeList, String fieldName) {
		if(!CollectionUtils.isEmpty(aggKeyAttributeList)) {
			int length = aggKeyAttributeList.size();
			for(int i = length-1; i >= 0; i--) {
				AggregationKeyAttribute aggregationKeyAttribute = aggKeyAttributeList.get(i);
				if(aggregationKeyAttribute != null && !aggregationKeyAttribute.getStatus().equals(StateEnum.DELETED)
						&& aggregationKeyAttribute.getFieldName().equalsIgnoreCase(fieldName)) {
					return aggKeyAttributeList.remove(i);
				}
			}
		}
		return null;
	}
}