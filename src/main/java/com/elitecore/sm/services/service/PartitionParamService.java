package com.elitecore.sm.services.service;

import java.util.List;

import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.common.validator.ImportValidationErrors;
import com.elitecore.sm.services.model.PartitionFieldEnum;
import com.elitecore.sm.services.model.PartitionParam;
import com.elitecore.sm.services.model.Service;

public interface PartitionParamService {

	public ResponseObject updatePartitionParamStatus(int paramId,String state);
	
	public PartitionParam getPartitionParamById(int paramId);
	
	public void validatePartitionParamForImport(List<PartitionParam> partitionParamList,List<ImportValidationErrors> importErrorList);
	
	public void importPartitionParamUpdateMode(PartitionParam dbParam, PartitionParam exportedParam);
	
	public void importPartitionParamAddAndKeepBothMode(PartitionParam exportedParam, Service parsingService);
	
	public PartitionParam getPartitionParamFromList(List<PartitionParam> partitionParamList, PartitionFieldEnum partitionField);
}
