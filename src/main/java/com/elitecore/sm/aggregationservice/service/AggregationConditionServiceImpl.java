package com.elitecore.sm.aggregationservice.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.elitecore.sm.aggregationservice.model.AggregationCondition;
import com.elitecore.sm.aggregationservice.model.AggregationDefinition;
import com.elitecore.sm.common.model.StateEnum;
import com.elitecore.sm.util.EliteUtils;

@Service(value = "aggregationConditionService")
public class AggregationConditionServiceImpl implements IAggregationConditionService{
	
	private Logger logger = Logger.getLogger(this.getClass().getName());

	@Transactional
	@Override
	public void importDataAggregationConditionAddAndKeepBothMode(AggregationCondition exportedCondition, AggregationDefinition aggregationDefinition, int importMode){
		exportedCondition.setId(0);
		exportedCondition.setCreatedDate(EliteUtils.getDateForImport(false));
		exportedCondition.setLastUpdatedDate(EliteUtils.getDateForImport(false));
		exportedCondition.setCreatedByStaffId(aggregationDefinition.getCreatedByStaffId());
		exportedCondition.setLastUpdatedByStaffId(aggregationDefinition.getLastUpdatedByStaffId());
		exportedCondition.setAggregationDefinition(aggregationDefinition);
	}
	
	@Transactional
	@Override
	public void importDataConditionUpdateMode(AggregationCondition dbCondition, AggregationCondition exportedCondition) {
		dbCondition.setCondAction(exportedCondition.getCondAction());
	}
	
	@Transactional
	@Override
	public AggregationCondition getDataConditionFromList(List<AggregationCondition> aggConditionList, String condExpression) {
		if(!CollectionUtils.isEmpty(aggConditionList)) {
			int length = aggConditionList.size();
			for(int i = length-1; i >= 0; i--) {
				AggregationCondition aggregationCondition = aggConditionList.get(i);
				if(aggregationCondition != null && !aggregationCondition.getStatus().equals(StateEnum.DELETED)
						&& aggregationCondition.getCondExpression().equalsIgnoreCase(condExpression)) {
					return aggConditionList.remove(i);
				}
			}
		}
		return null;
	}
}