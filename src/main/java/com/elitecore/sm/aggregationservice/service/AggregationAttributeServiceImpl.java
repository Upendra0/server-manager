package com.elitecore.sm.aggregationservice.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.elitecore.sm.aggregationservice.dao.IAggregationAttributeDao;
import com.elitecore.sm.aggregationservice.dao.IAggregationDefinitionDao;
import com.elitecore.sm.aggregationservice.model.AggregationAttribute;
import com.elitecore.sm.aggregationservice.model.AggregationDefinition;
import com.elitecore.sm.common.model.StateEnum;
import com.elitecore.sm.services.dao.ServicesDao;
import com.elitecore.sm.util.EliteUtils;

@Service(value = "cAttributeService")
public class AggregationAttributeServiceImpl implements IAggregationAttributeService{
	
	private Logger logger = Logger.getLogger(this.getClass().getName());

	@Autowired
	IAggregationAttributeDao aggregationAttributeDao;
	 
	@Autowired
	IAggregationDefinitionDao aggregationDefinitionDao;
	@Autowired
	private ServicesDao servicesDao;

	/*
	@Transactional
	@Override
	public ResponseObject addAggregationAttributeList(AggregationAttribute aggregationAttribute, int iAggDefId){
		ResponseObject responseObject = new ResponseObject();
		AggregationDefinition aggDefinition = aggregationDefinitionDao.getAggregationDefintionListById(iAggDefId);		
		if (aggDefinition != null) {
			aggregationAttribute.setAggregationDefinition(aggDefinition);
			aggregationAttributeDao.save(aggregationAttribute);		
			if (aggregationAttribute.getId() != 0) {
				responseObject.setSuccess(true);
				responseObject.setResponseCode(ResponseCode.AGGREGATION_DEFINITION_ATTRIBUTE_ADD_SUCCESS);
				responseObject.setObject(aggregationAttribute);
				logger.info("Aggregation Defination Attribute added successfully.");
			} else {
				responseObject.setSuccess(false);
				responseObject.setResponseCode(ResponseCode.AGGREGATION_DEFINITION_ATTRIBUTE_ADD_FAIL);
				logger.info("Aggregation Defination Attribute added failed.");
			}
		} else {
			logger.info("Aggregation Defination Attribute added failed.");
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.AGGREGATION_DEFINITION_ATTRIBUTE_ADD_FAIL);
		}
		return responseObject;
	}
		
	@Transactional
	@Override
	public ResponseObject updateAggregationAttributeList(AggregationAttribute aggregationAttribute, int iAggDefId){
		ResponseObject responseObject = new ResponseObject();
		
		return responseObject;
	}
	
	@Transactional
	@Override
	public ResponseObject getAggregationAttributeById(int id) {
		ResponseObject responseObject = new ResponseObject();
		AggregationAttribute aggregationAttribute = (AggregationAttribute)aggregationAttributeDao.findByPrimaryKey(AggregationAttribute.class, id);
		if(aggregationAttribute != null) {
			responseObject.setSuccess(true);
			responseObject.setResponseCode(ResponseCode.AGGREGATION_DEFINITION_ATTRIBUTE_VIEW_SUCCESS);
			responseObject.setObject(aggregationAttribute);
		} else {
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.AGGREGATION_DEFINITION_ATTRIBUTE_VIEW_FAIL);
		}
		return responseObject;
	}
	*/	
	@Transactional
	@Override
	public void importDataAggregationAttributeAddAndKeepBothMode(AggregationAttribute exportedAttribute, AggregationDefinition aggregationDefinition, int importMode){
		exportedAttribute.setId(0);
		exportedAttribute.setCreatedDate(EliteUtils.getDateForImport(false));
		exportedAttribute.setLastUpdatedDate(EliteUtils.getDateForImport(false));
		exportedAttribute.setCreatedByStaffId(aggregationDefinition.getCreatedByStaffId());
		exportedAttribute.setLastUpdatedByStaffId(aggregationDefinition.getLastUpdatedByStaffId());
		exportedAttribute.setAggregationDefinition(aggregationDefinition);
	}
	
	@Override
	public void importDataAggregationAttributeUpdateMode(AggregationAttribute dbAttribute, AggregationAttribute exportedAttribute) {
		dbAttribute.setOutputDataType(exportedAttribute.getOutputDataType());
		dbAttribute.setOutputExpression(exportedAttribute.getOutputExpression());
	}
	
	@Override
	public AggregationAttribute getDataAggregationAttributeFromList(List<AggregationAttribute> aggAttributeList, String fieldName) {
		if(!CollectionUtils.isEmpty(aggAttributeList)) {
			int length = aggAttributeList.size();
			for(int i = length-1; i >= 0; i--) {
				AggregationAttribute aggregationAttribute = aggAttributeList.get(i);
				if(aggregationAttribute != null && !aggregationAttribute.getStatus().equals(StateEnum.DELETED)
						&& aggregationAttribute.getOutputFieldName().equalsIgnoreCase(fieldName)) {
					return aggAttributeList.remove(i);
				}
			}
		}
		return null;
	}

}